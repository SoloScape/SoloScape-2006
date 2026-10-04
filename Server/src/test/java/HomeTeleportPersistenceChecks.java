import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.CharacterFileRecord;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.util.CharacterFileManager;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class HomeTeleportPersistenceChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        String username = "qa_home_teleport";
        Path file = Paths.get("data/characters/" + username + ".dat");
        require(!Files.exists(file), "Test account already exists");
        try {
            Player player = new Player(null);
            player.setUsername(username);
            player.setPassword("test");
            player.lastLoginHostAddress = "127.0.0.1";
            java.lang.reflect.Field host = Player.class.getDeclaredField("hostAddress");
            host.setAccessible(true);
            host.set(player, "127.0.0.1");
            player.setQuestState(0, 1);
            long availableAt = System.currentTimeMillis() + 1800000L;
            player.homeTeleportAvailableAtMillis = availableAt;
            Method writer = CharacterFileManager.class.getDeclaredMethod("writePlayerFile", Player.class);
            writer.setAccessible(true);
            writer.invoke(null, player);
            require(load(username).homeTeleportAvailableAtMillis == availableAt,
                    "Player save/reload lost cooldown");
            Method reader = CharacterFileManager.class.getDeclaredMethod(
                    "readCharacterFileRecord", String.class, String.class, boolean.class);
            reader.setAccessible(true);
            CharacterFileRecord record = (CharacterFileRecord) reader.invoke(null,
                    "./data/characters/", username, true);
            require(record != null && record.homeTeleportAvailableAtMillis == availableAt,
                    "Record reader lost cooldown");
            CharacterFileManager.saveCharacterFileRecord(record);
            require(load(username).homeTeleportAvailableAtMillis == availableAt,
                    "Offline record rewrite lost cooldown");
            byte[] current = Files.readAllBytes(file);
            Files.write(file, Arrays.copyOf(current, current.length - 9));
            Player legacy = load(username);
            require(legacy.getQuestState(0) == 1, "Old character file failed to load");
            require(legacy.homeTeleportAvailableAtMillis == 0L,
                    "Old character file acquired a cooldown");
        } finally {
            Files.deleteIfExists(file);
        }
        System.out.println("Home teleport persistence checks passed (player save, record rewrite, old saves).");
        System.exit(0);
    }

    private static Player load(String username) {
        Player player = new Player(null);
        player.setUsername(username);
        CharacterFileManager.loadPlayerFromFile("./data/characters/", player);
        return player;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
