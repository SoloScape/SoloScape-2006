import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/** Exercise the actual command and inspect its outgoing chat messages. */
public final class ClueMerchantCommandChecks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static String message(DataInputStream input) throws Exception {
        input.readUnsignedByte(); // ISAAC-encrypted chat opcode
        byte[] bytes = new byte[input.readUnsignedByte()];
        input.readFully(bytes);
        check(bytes.length > 0 && bytes[bytes.length - 1] == 0, "chat string terminator");
        return new String(bytes, 0, bytes.length - 1, StandardCharsets.ISO_8859_1);
    }

    private static Npc npc(int id, int x, int y, int plane) {
        Npc npc = new Npc(id);
        npc.setPosition(new Position(x, y, plane));
        return npc;
    }

    public static void main(String[] args) throws Exception {
        // Use an empty batch to verify scheduling without map/cache dependencies.
        com.rs2.model.quest.event.ServerMaintenanceEventHook hook =
                new com.rs2.model.quest.event.ServerMaintenanceEventHook(0, 0);
        Field count = hook.getClass().getDeclaredField("clueMerchantCount");
        count.setAccessible(true);
        count.setInt(hook, 0);
        com.rs2.model.quest.event.ClueMerchantSpawnTask spawn =
                new com.rs2.model.quest.event.ClueMerchantSpawnTask(hook, 1500);
        spawn.tick();
        check(spawn.getRemainingTicks() == 1500, "first tick executes instead of waiting");
        for (int tick = 0; tick < 1499; tick++) spawn.tick();
        check(spawn.getRemainingTicks() == 1, "replacement waits for the full interval");
        spawn.tick();
        check(spawn.getRemainingTicks() == 1500, "replacement repeats every 1500 ticks");

        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        Player player = new Player(null);
        player.setQuestState(0, 1);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        try (ServerSocketChannel listener = ServerSocketChannel.open()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            try (SocketChannel transport = SocketChannel.open(listener.getLocalAddress());
                 SocketChannel receiver = listener.accept()) {
                receiver.socket().setSoTimeout(5000);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, transport);
                DataInputStream input = new DataInputStream(receiver.socket().getInputStream());

                ServerSettings.clueMerchantEnabled = false;
                player.handleCommand("cluemerchant", new String[0], "");
                check(message(input).equals("No clue merchants are currently spawned."), "empty-world message");
                check(message(input).contains("CLUE_MERCHANT enabled"), "disabled-setting guidance");

                ServerSettings.clueMerchantEnabled = true;
                ServerSettings.freeToPlayWorld = true;
                player.handleCommand("cluemerchant", new String[0], "");
                message(input);
                check(message(input).contains("P2P_WORLD set to 1"), "members-world guidance");

                ServerSettings.freeToPlayWorld = false;
                player.handleCommand("cluemerchant", new String[0], "");
                message(input);
                check(message(input).contains("1500 ticks"), "spawn-wait guidance");

                Npc[] npcs = World.getNpcs();
                npcs[1] = npc(3886, 3000, 3200, 0);
                npcs[2] = npc(3886, 3100, 3300, 2);
                npcs[3] = npc(3886, 3200, 3400, 0);
                npcs[3].setActive(false);
                npcs[4] = npc(3886, 3250, 3450, 0);
                npcs[4].setDead(true);
                npcs[5] = npc(1, 3010, 3210, 0);
                player.handleCommand("ClueMerchant", new String[0], "");
                check(message(input).equals("Clue merchant 1: 3000, 3200, plane 0"), "first active location");
                check(message(input).equals("Clue merchant 2: 3100, 3300, plane 2"), "second active location");

                npcs[1].setPosition(new Position(3005, 3207, 0));
                npcs[2] = null;
                player.handleCommand("cluemerchant", new String[0], "");
                check(message(input).equals("Clue merchant 1: 3005, 3207, plane 0"), "live position and removal");
                check(input.available() == 0, "no output for dead, inactive or unrelated NPCs");
            }
        }
        System.out.println("Clue merchant command checks passed.");
        System.exit(0);
    }
}
