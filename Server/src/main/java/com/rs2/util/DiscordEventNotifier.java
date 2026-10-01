package com.rs2.util;

import com.rs2.model.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Sends lightweight gameplay events to the optional SoloScape Discord relay.
 *
 * The Discord webhook itself never belongs in the game server. Each local server
 * only knows the relay URL and its own revocable relay key.
 */
public final class DiscordEventNotifier {
    private static final String CONFIG_FILE_NAME = "discord-relay.properties";
    private static final Properties CONFIG = loadConfig();

    private static final String RELAY_URL = setting("relay.url", "SOLOSCAPE_DISCORD_RELAY_URL");
    private static final String RELAY_KEY = setting("relay.key", "SOLOSCAPE_DISCORD_RELAY_KEY");
    private static final String SERVER_NAME = defaultIfBlank(
            setting("server.name", "SOLOSCAPE_DISCORD_SERVER_NAME"), "SoloScape");
    private static final Set<Integer> BOSS_IDS = parseBossIds(
            setting("boss.ids", "SOLOSCAPE_DISCORD_BOSS_IDS"));
    private static final Set<String> BOSS_NAMES = buildBossNames(
            setting("boss.names", "SOLOSCAPE_DISCORD_BOSS_NAMES"));
    private static final ConcurrentHashMap<String, Long> RECENT_BOSS_KILLS = new ConcurrentHashMap<String, Long>();
    private static final long BOSS_DEDUPE_MILLIS = 1500L;

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "soloscape-discord-events");
            thread.setDaemon(true);
            return thread;
        }
    });

    private DiscordEventNotifier() {
    }

    public static void levelUp(Player player, String skillName, int level) {
        if (!isConfigured() || player == null || player.isBot) {
            return;
        }
        String json = "{"
                + "\"type\":\"level_up\","
                + "\"server\":\"" + escapeJson(SERVER_NAME) + "\","
                + "\"player\":\"" + escapeJson(player.getUsername()) + "\","
                + "\"skill\":\"" + escapeJson(skillName) + "\","
                + "\"level\":" + level
                + "}";
        sendAsync(json);
    }

    public static void bossKill(Player player, int npcId, String npcName) {
        if (!isConfigured() || player == null || player.isBot || !isBoss(npcId, npcName)) {
            return;
        }

        String playerName = player.getUsername();
        String dedupeKey = defaultIfBlank(playerName, "unknown").toLowerCase(Locale.ENGLISH) + "|" + npcId;
        long now = System.currentTimeMillis();
        Long previous = RECENT_BOSS_KILLS.put(dedupeKey, now);
        if (previous != null && now - previous.longValue() < BOSS_DEDUPE_MILLIS) {
            return;
        }
        if (RECENT_BOSS_KILLS.size() > 1000) {
            RECENT_BOSS_KILLS.clear();
            RECENT_BOSS_KILLS.put(dedupeKey, now);
        }

        String json = "{"
                + "\"type\":\"boss_kill\","
                + "\"server\":\"" + escapeJson(SERVER_NAME) + "\","
                + "\"player\":\"" + escapeJson(playerName) + "\","
                + "\"boss\":\"" + escapeJson(npcName) + "\","
                + "\"npcId\":" + npcId
                + "}";
        sendAsync(json);
    }

    private static boolean isBoss(int npcId, String npcName) {
        if (BOSS_IDS.contains(Integer.valueOf(npcId))) {
            return true;
        }
        if (npcName == null) {
            return false;
        }
        return BOSS_NAMES.contains(npcName.trim().toLowerCase(Locale.ENGLISH));
    }

    private static void sendAsync(final String json) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                post(json);
            }
        });
    }

    private static void post(String json) {
        HttpURLConnection connection = null;
        try {
            byte[] body = json.getBytes(StandardCharsets.UTF_8);
            connection = (HttpURLConnection)new URL(RELAY_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Authorization", "Bearer " + RELAY_KEY);
            connection.setRequestProperty("User-Agent", "SoloScape-Discord-Events/1.0");
            connection.setFixedLengthStreamingMode(body.length);

            OutputStream output = connection.getOutputStream();
            try {
                output.write(body);
            } finally {
                output.close();
            }

            int status = connection.getResponseCode();
            InputStream input = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
            if (input != null) {
                try {
                    byte[] buffer = new byte[256];
                    while (input.read(buffer) != -1) {
                        // Drain the response so HttpURLConnection can reuse/close cleanly.
                    }
                } finally {
                    input.close();
                }
            }
            if (status < 200 || status >= 300) {
                System.err.println("Discord relay returned HTTP " + status + ".");
            }
        } catch (IOException exception) {
            System.err.println("Discord relay event failed: " + exception.getMessage());
        } catch (RuntimeException exception) {
            System.err.println("Discord relay event failed: " + exception.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static boolean isConfigured() {
        return !RELAY_URL.isEmpty() && !RELAY_KEY.isEmpty();
    }

    private static Set<Integer> parseBossIds(String raw) {
        if (raw.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Integer> ids = new HashSet<Integer>();
        String[] values = raw.split(",");
        for (String value : values) {
            try {
                ids.add(Integer.valueOf(Integer.parseInt(value.trim())));
            } catch (NumberFormatException ignored) {
                // Ignore invalid optional config entries rather than preventing startup.
            }
        }
        return Collections.unmodifiableSet(ids);
    }

    private static Set<String> buildBossNames(String extraNames) {
        Set<String> names = new HashSet<String>(Arrays.asList(
                "king black dragon",
                "kalphite queen",
                "chaos elemental",
                "giant mole",
                "tz-tok jad",
                "tztok-jad",
                "dagannoth rex",
                "dagannoth prime",
                "dagannoth supreme",
                "general graardor",
                "kree'arra",
                "commander zilyana",
                "k'ril tsutsaroth",
                "corporeal beast"
        ));
        if (!extraNames.isEmpty()) {
            String[] values = extraNames.split(",");
            for (String value : values) {
                String normalized = value.trim().toLowerCase(Locale.ENGLISH);
                if (!normalized.isEmpty()) {
                    names.add(normalized);
                }
            }
        }
        return Collections.unmodifiableSet(names);
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        File configFile = new File(CONFIG_FILE_NAME);
        if (!configFile.isFile()) {
            File fromRepositoryRoot = new File("Server", CONFIG_FILE_NAME);
            if (!fromRepositoryRoot.isFile()) {
                return properties;
            }
            configFile = fromRepositoryRoot;
        }

        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
        } catch (IOException exception) {
            System.err.println("Could not read " + configFile.getPath() + ": " + exception.getMessage());
        }
        return properties;
    }

    private static String setting(String propertyName, String environmentName) {
        String environmentValue = env(environmentName);
        if (!environmentValue.isEmpty()) {
            return environmentValue;
        }
        String propertyValue = CONFIG.getProperty(propertyName);
        return propertyValue == null ? "" : propertyValue.trim();
    }

    private static String env(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value.trim();
    }

    private static String defaultIfBlank(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder(value.length() + 16);
        for (int index = 0; index < value.length(); ++index) {
            char character = value.charAt(index);
            switch (character) {
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (character < 0x20) {
                        escaped.append(String.format("\\u%04x", Integer.valueOf(character)));
                    } else {
                        escaped.append(character);
                    }
                    break;
            }
        }
        return escaped.toString();
    }
}
