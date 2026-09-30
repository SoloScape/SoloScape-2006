package com.rs2.model.music;

import com.rs2.cache.js5.ConfigReader;
import com.rs2.cache.js5.Interfaces;
import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.cache.js5.Js5ReferenceTable;
import com.rs2.model.player.Player;
import com.rs2.net.packet.AudioIds443;
import com.rs2.net.packet.VarpPacket;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Uses the stock music tab's labels, audio assets and colour conditions. */
public final class Music {
    private static Map<Integer, Track> tracks = Collections.emptyMap();
    private Music() { }

    public static void load() throws IOException {
        Map<Integer, Track> loaded = new LinkedHashMap<Integer, Track>();
        try (Js5CacheStore cache = new Js5CacheStore(new File("cache"))) {
            Js5ReferenceTable audio = cache.readReferenceTable(6);
            for (Interfaces.Component component : Interfaces.group(239).values()) {
                if (component.modern || component.type != 4 || component.actionType != 1) continue;
                ConfigReader reader = new ConfigReader(component.getData());
                reader.skip(17); // IF1 header through hover target
                int count = reader.readUnsignedByte();
                int[] comparisons = new int[count];
                int[] expected = new int[count];
                for (int i = 0; i < count; i++) {
                    comparisons[i] = reader.readUnsignedByte();
                    expected[i] = reader.readUnsignedShort();
                }
                int scripts = reader.readUnsignedByte();
                Condition[] conditions = new Condition[count];
                for (int i = 0; i < scripts; i++) {
                    int length = reader.readUnsignedShort();
                    int opcode = reader.readUnsignedShort();
                    int config, start, end;
                    if (opcode == 13 && length == 4) {
                        config = reader.readUnsignedShort();
                        start = end = reader.readUnsignedShort();
                    } else if (opcode == 14 && length == 3) {
                        int varbit = reader.readUnsignedShort();
                        byte[] data = cache.readFiles(2, 14).get(varbit);
                        if (data == null) throw new IOException("Missing music varbit " + varbit);
                        ConfigReader bits = new ConfigReader(data);
                        if (bits.readUnsignedByte() != 1) throw new IOException("Unexpected music varbit");
                        config = bits.readUnsignedShort();
                        start = bits.readUnsignedByte();
                        end = bits.readUnsignedByte();
                    } else {
                        throw new IOException("Unexpected music script at child " + component.childId);
                    }
                    if (reader.readUnsignedShort() != 0 || i >= count) {
                        throw new IOException("Unexpected music script terminator/count");
                    }
                    conditions[i] = new Condition(config, start, end, opcode == 13);
                }
                reader.skip(6); // alignment, font, shadow
                String name = reader.readString();
                reader.readString();
                int normalColour = reader.readInt();
                int conditionalColour = reader.readInt();
                // Exclude AUTO, MANUAL and LOOP controls.
                if (normalColour != 0xff0000 && normalColour != 0x00ff00) continue;
                int legacy = -1;
                for (int id = 0; id < MusicTrackDefinition.trackCount; id++) {
                    if (name.equalsIgnoreCase(MusicTrackDefinition.forTrackId(id).getName())) {
                        legacy = id;
                        break;
                    }
                }
                int asset = audio.getGroupId(name);
                if (asset < 0 && legacy >= 0) asset = AudioIds443.track(legacy);
                if (asset < 0) {
                    String alias = name.equals("All's Fairy in Love'n'War") ? "Alls Fairy in Love n War"
                            : name.equals("Eagles' Peak") ? "Eagle Peak" : name;
                    asset = audio.getGroupId(alias);
                }
                if (asset < 0) {
                    for (String alias : new String[] {name.replace("'", ""), name.replace(" ", ""),
                            name.replace("'", "").replace(" ", ""), name.replace("Vampyre", "Vampire")}) {
                        asset = audio.getGroupId(alias);
                        if (asset >= 0) break;
                    }
                }
                if (asset >= 0) {
                    try {
                        if (cache.readFiles(6, asset).isEmpty()) asset = -1;
                    } catch (IOException missingAsset) {
                        asset = -1;
                    }
                }
                // Legacy saves use bit 30 for the last track in several varps.
                if (legacy >= 0) {
                    MusicTrackDefinition old = MusicTrackDefinition.forTrackId(legacy);
                    if (old.getUnlockConfigId() < 0 && conditions.length == 1
                            && conditions[0] != null && conditions[0].bitTest
                            && comparisons[0] == 1 && expected[0] == 1 && normalColour == 0xff0000) {
                        old.setNativeUnlock(conditions[0].config, conditions[0].sourceMask);
                    }
                    for (Condition condition : conditions) {
                        if (condition != null && condition.bitTest
                                && condition.config == old.getUnlockConfigId()) {
                            condition.sourceMask = old.getUnlockBitMask();
                        }
                    }
                }
                loaded.put(component.childId, new Track(name, asset, legacy, comparisons,
                        expected, conditions, normalColour, conditionalColour));
            }
        }
        tracks = Collections.unmodifiableMap(loaded);
    }

    public static Map<Integer, Track> tracks() { return tracks; }

    public static int assetForLegacyTrack(int legacyId) {
        for (Track track : tracks.values()) {
            if (track.legacyId == legacyId) return track.assetId;
        }
        return AudioIds443.track(legacyId);
    }

    public static boolean play(Player player, int child) {
        Track track = tracks.get(child);
        if (track == null) return false;
        if (!track.isUnlocked(player)) {
            player.packetSender.sendGameMessage("You haven't unlocked that song yet.");
            return true;
        }
        if (track.assetId < 0) {
            player.packetSender.sendGameMessage("That song is unavailable in this cache.");
            return true;
        }
        player.packetSender.sendRevision443MusicTrack(track.name, track.assetId);
        player.musicManagerTrackId = track.legacyId >= 0 ? track.legacyId : -2 - child;
        player.automaticMusicEnabled = false;
        return true;
    }

    public static boolean isMusicConfig(int config) {
        for (Track track : tracks.values()) {
            for (Condition condition : track.conditions) {
                if (condition != null && condition.bitTest && condition.config == config) return true;
            }
        }
        return false;
    }

    public static int configValue(Player player, int config) {
        int value = player.configStates[config];
        for (Track track : tracks.values()) {
            for (Condition condition : track.conditions) {
                if (condition != null && condition.bitTest && condition.config == config) {
                    value &= ~(1 << condition.start);
                    if ((player.configStates[config] & condition.sourceMask) != 0) value |= 1 << condition.start;
                }
            }
        }
        return value;
    }

    public static void sendUnlocks(Player player) {
        if (player.isBot) return;
        for (int config = 0; config < player.configStates.length; config++) {
            if (isMusicConfig(config)) VarpPacket.send(player, config, configValue(player, config));
        }
    }

    public static void unlockAll(Player player) {
        for (Track track : tracks.values()) {
            for (Condition condition : track.conditions) {
                if (condition != null && condition.bitTest) {
                    player.configStates[condition.config] |= condition.sourceMask;
                }
            }
        }
        sendUnlocks(player);
    }

    public static final class Track {
        public final String name;
        public final int assetId;
        public final int legacyId;
        private final int[] comparisons;
        private final int[] expected;
        private final Condition[] conditions;
        private final int normalColour;
        private final int conditionalColour;

        private Track(String name, int assetId, int legacyId, int[] comparisons,
                      int[] expected, Condition[] conditions, int normalColour, int conditionalColour) {
            this.name = name;
            this.assetId = assetId;
            this.legacyId = legacyId;
            this.comparisons = comparisons;
            this.expected = expected;
            this.conditions = conditions;
            this.normalColour = normalColour;
            this.conditionalColour = conditionalColour;
        }

        public boolean isUnlocked(Player player) {
            boolean matches = conditions.length > 0;
            for (int i = 0; i < conditions.length; i++) {
                Condition condition = conditions[i];
                int value = condition == null ? -2 : condition.value(player);
                int target = expected[i];
                int comparison = comparisons[i];
                boolean match = comparison == 2 ? value < target : comparison == 3 ? value > target
                        : comparison == 4 ? value != target : value == target;
                matches &= match;
            }
            return (matches ? conditionalColour : normalColour) == 0x00ff00;
        }
    }

    private static final class Condition {
        final int config;
        final int start;
        final int end;
        final boolean bitTest;
        int sourceMask;

        Condition(int config, int start, int end, boolean bitTest) {
            this.config = config;
            this.start = start;
            this.end = end;
            this.bitTest = bitTest;
            sourceMask = 1 << start;
        }

        int value(Player player) {
            if (bitTest) return (player.configStates[config] & sourceMask) == 0 ? 0 : 1;
            return (player.configStates[config] >>> start) & (int)((1L << (end - start + 1)) - 1);
        }
    }
}
