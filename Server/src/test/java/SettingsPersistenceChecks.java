import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.CharacterFileRecord;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.util.CharacterFileManager;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;

/** Verifies the login flag, logout save, slider packets, and character-file compatibility. */
public final class SettingsPersistenceChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        for (int stage : new int[] {3, 44, 1}) {
            Path file = Paths.get("data/characters/qa_settings_" + stage + ".dat");
            require(!Files.exists(file), "Test account already exists: " + file);
            try (ServerSocketChannel listener = ServerSocketChannel.open();
                 Socket client = new Socket(); Selector selector = Selector.open()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress());
                try (SocketChannel transport = listener.accept()) {
                    transport.configureBlocking(false);
                    Player player = new Player(transport.register(selector, SelectionKey.OP_READ));
                    player.setUsername("qa_settings_" + stage);
                    player.setPassword("test");
                    player.setQuestState(0, stage);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.lastLoginHostAddress = "127.0.0.1";
                    // Closed transport keeps this check focused on login state and disk saves.
                    transport.close();
                    player.packetSender.sendPostLoginState();
                    require(player.loginInitializationComplete, "Login incomplete at tutorial stage " + stage);
                    for (int brightness = 1; brightness <= 4; brightness++) {
                        click(player, 6 + brightness);
                        require(player.getBrightness() == brightness, "Brightness click ignored");
                    }
                    for (int volume = 0; volume <= 4; volume++) {
                        click(player, 15 - volume);
                        click(player, 20 - volume);
                        click(player, 33 - volume);
                        require(player.getMusicVolume() == volume, "Music click ignored");
                        require(player.getEffectVolume() == volume, "Effects click ignored");
                        require(player.configStates[872] == volume, "Area sound click ignored");
                    }
                    player.disconnect();
                    require(Files.exists(file), "Logout did not save the account");
                    Player restored = load(player.getUsername());
                    check(restored, 4);
                    require(restored.getQuestState(0) == stage, "Tutorial progress changed");
                    // A version-30 non-bot file ends with botEnabled. Remove only the new area value.
                    byte[] current = Files.readAllBytes(file);
                    byte[] legacy = new byte[current.length - 4];
                    System.arraycopy(current, 0, legacy, 0, current.length - 5);
                    legacy[0] = 0;
                    legacy[1] = 30;
                    legacy[legacy.length - 1] = current[current.length - 1];
                    Files.write(file, legacy);
                    check(load(player.getUsername()), 0);
                    // Offline character-record rewrites must also preserve area sound.
                    java.lang.reflect.Method reader = CharacterFileManager.class.getDeclaredMethod(
                            "readCharacterFileRecord", String.class, String.class, boolean.class);
                    reader.setAccessible(true);
                    CharacterFileRecord record = (CharacterFileRecord) reader.invoke(null,
                            "./data/characters/", player.getUsername(), true);
                    require(record != null, "Legacy save did not load");
                    record.configStates[872] = 3;
                    CharacterFileManager.saveCharacterFileRecord(record);
                    check(load(player.getUsername()), 3);
                }
            } finally {
                Files.deleteIfExists(file);
            }
        }
        System.out.println("Settings persistence checks passed: tutorial/completed login, every slider level, logout/reload, legacy saves, record rewrite.");
        System.exit(0);
    }

    private static void click(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt((261 << 16) | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player, new IncomingPacket(
                ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(payload)));
    }

    private static Player load(String username) {
        Player player = new Player(null);
        player.setUsername(username);
        CharacterFileManager.loadPlayerFromFile("./data/characters/", player);
        return player;
    }

    private static void check(Player player, int areaVolume) {
        require(player.getBrightness() == 4, "Brightness lost after login");
        require(player.getMusicVolume() == 4, "Music volume lost after login");
        require(player.getEffectVolume() == 4, "Effect volume lost after login");
        require(player.configStates[872] == areaVolume, "Area volume lost after login");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
