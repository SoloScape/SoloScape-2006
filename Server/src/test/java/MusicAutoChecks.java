import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.music.*;
import com.rs2.model.player.*;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.util.CharacterFileManager;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.Arrays;

/** Native music mode/audio packets, region refreshes and save compatibility. */
public final class MusicAutoChecks {
    public static void main(String[] args) throws Exception {
        try {
            ServerSettings.clientBuild = 443;
            QuestDefinition.loadDefinitions();
            InterfaceDefinition.loadDefinitions();
            ItemDefinition.loadDefinitions();
            MusicTrackDefinition.loadDefinitions();
            MusicAreaDefinition.loadDefinitions();
            InitialVarps.send(null);
            checkClient();
            checkPersistence();
            System.out.println("Music Auto checks passed: native mode/audio packets, region changes, Manual mode, save/load, record rewrites and old saves.");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkClient() throws Exception {
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(100);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                player.setIndex(1);
                player.setEncodedIndex(32769);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, transport);
                player.setQuestState(0, 1);
                player.setSidebarInterfaceId(13, 962);
                MusicManager.unlockAllTracks(player);
                drain(client);
                int[] regions = {11313, 12627, 11569};
                int[] tracks = {208, 209, 210};
                for (int i = 0; i < regions.length; i++) {
                    player.setPosition(position(regions[i]));
                    require(MusicManager.playManualTrack(player, 172), "Manual track failed");
                    drain(client);
                    click(player, 180);
                    require(player.automaticMusicEnabled && player.configStates[18] == 1, "Auto state not synchronized");
                    byte[] auto = drain(client);
                    require(auto.length >= 7 && auto[1] == (byte)255 && auto[2] == 0 && auto[3] == 18, "Auto varp packet missing");
                    require(player.musicManagerTrackId == tracks[i] && player.currentMusicTrackId == tracks[i], "Auto did not play region music");
                    require((auto[auto.length-2] & 255) == tracks[i] && auto[auto.length-1] == 0, "Audio packet missing");
                    int next = (i + 1) % regions.length;
                    player.setPosition(position(regions[next]));
                    player.getMovementQueue().process();
                    require(player.musicManagerTrackId == tracks[next] && player.currentMusicTrackId == tracks[next], "Native movement skipped region music");
                    require(drain(client).length >= 3, "Region refresh did not send audio");
                    click(player, 181);
                    require(!player.automaticMusicEnabled && player.configStates[18] == 0, "Manual mode not synchronized");
                    drain(client);
                    player.setPosition(position(regions[i]));
                    player.getMovementQueue().process();
                    require(player.currentMusicTrackId == tracks[next], "Manual mode changed track on region change");
                    require(drain(client).length == 0, "Manual mode sent automatic music");
                }
                for (boolean automatic : new boolean[]{false, true}) {
                    player.automaticMusicEnabled = automatic;
                    InitialVarps.send(player);
                    byte[] login = drain(client);
                    require(login.length >= 4 && login[1] == (automatic ? (byte)255 : 0)
                            && login[2] == 0 && login[3] == 18, "Login did not restore mode varp");
                }
            }
        }
    }

    private static void checkPersistence() throws Exception {
        String username = "qa_music_auto";
        Path file = Paths.get("data/characters/" + username + ".dat");
        require(!Files.exists(file), "Test account already exists");
        try {
            Player player = new Player(null);
            player.setUsername(username);
            player.setPassword("test");
            player.lastLoginHostAddress = "127.0.0.1";
            Field host = Player.class.getDeclaredField("hostAddress");
            host.setAccessible(true);
            host.set(player, "127.0.0.1");
            player.setQuestState(0, 1);
            player.homeTeleportAvailableAtMillis = 123456789L;
            Method writer = CharacterFileManager.class.getDeclaredMethod("writePlayerFile", Player.class);
            writer.setAccessible(true);
            Method reader = CharacterFileManager.class.getDeclaredMethod("readCharacterFileRecord", String.class, String.class, boolean.class);
            reader.setAccessible(true);
            for (boolean automatic : new boolean[]{true, false}) {
                player.automaticMusicEnabled = automatic;
                writer.invoke(null, player);
                require(load(username).automaticMusicEnabled == automatic, "Save/load lost music mode");
                CharacterFileRecord record = (CharacterFileRecord)reader.invoke(null, "./data/characters/", username, true);
                require(record != null && record.automaticMusicEnabled == automatic, "Record reader lost music mode");
                CharacterFileManager.saveCharacterFileRecord(record);
                require(load(username).automaticMusicEnabled == automatic, "Offline rewrite lost music mode");
            }
            byte[] current = Files.readAllBytes(file);
            Files.write(file, Arrays.copyOf(current, current.length - 1));
            Player legacy = load(username);
            require(legacy.automaticMusicEnabled, "Old saves must default to Auto");
            require(legacy.homeTeleportAvailableAtMillis == 123456789L, "Music mode damaged cooldown");
            Files.write(file, Arrays.copyOf(current, current.length - 9));
            require(load(username).automaticMusicEnabled, "Older saves failed to default to Auto");
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private static Player load(String username) {
        Player player = new Player(null);
        player.setUsername(username);
        CharacterFileManager.loadPlayerFromFile("./data/characters/", player);
        return player;
    }

    private static Position position(int region) {
        return new Position((region >> 8) * 64 + 32, (region & 255) * 64 + 32, 0);
    }

    private static void click(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt(239 << 16 | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player, new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(payload)));
    }

    private static byte[] drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            while (true) {
                int value = socket.getInputStream().read();
                if (value < 0) break;
                bytes.write(value);
            }
        } catch (SocketTimeoutException expected) { }
        return bytes.toByteArray();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
