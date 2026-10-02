import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.ground.GroundItemVisibility;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.woodcutting.BirdNestDrop;
import com.rs2.model.task.TickTask;
import com.rs2.net.IsaacCipher;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/** Real ground-item lifetime, pickup, and outbound feedback checks. */
public final class BirdNestDropChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        GroundItemManager manager = GroundItemManager.getInstance();
        try (ServerSocketChannel listener = ServerSocketChannel.open()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            try (Socket client = new Socket()) {
                client.connect(listener.getLocalAddress());
                client.setSoTimeout(100);
                try (SocketChannel transport = listener.accept()) {
                    Player player = new Player(null);
                    socket.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setIndex(1);
                    player.setEncodedIndex(32769);
                    player.setPosition(new Position(3200, 3200));
                    player.getLastKnownRegionPosition().set(player.getPosition());
                    player.refreshLocalViewArea();
                    player.setQuestState(0, 1);
                    World.players[1] = player;
                    drain(client);
                    for (int id = 5070; id <= 5074; id++) {
                        GroundItem nest = BirdNestDrop.spawn(player, id);
                        TickTask expiry = lastTask();
                        byte[] feedback = drain(client);
                        require(new String(feedback, StandardCharsets.ISO_8859_1)
                                .contains("A bird's nest falls out of the tree."), "Missing nest message");
                        require(hasChirp(feedback), "Missing mapped chirp packet");
                        require(nest.getVisibility() == GroundItemVisibility.PRIVATE
                                && nest.getOwner().resolve() == player, "Wrong nest ownership");
                        for (int tick = 1; tick <= 49; tick++) advance(expiry);
                        require(manager.contains(nest) && player.getVisibleGroundItems().contains(nest),
                                "Nest disappeared before 30 seconds");
                        require(nest.getVisibility() == GroundItemVisibility.PRIVATE, "Nest became public");
                        advance(expiry);
                        require(!manager.contains(nest) && !player.getVisibleGroundItems().contains(nest)
                                && !expiry.isActive(), "Nest survived 30 seconds");
                        require(drain(client).length > 0, "No client removal packet");
                    }
                    GroundItem pickedUp = BirdNestDrop.spawn(player, 5073);
                    TickTask expiry = lastTask();
                    require(manager.removeForPickup(pickedUp, player), "Nest pickup failed");
                    GroundItem redropped = new GroundItem(new ItemStack(5073), player);
                    manager.spawn(redropped);
                    GroundItem ordinary = new GroundItem(new ItemStack(1511), player);
                    manager.spawn(ordinary);
                    for (int tick = 1; tick <= 50; tick++) advance(expiry);
                    require(manager.contains(redropped) && manager.contains(ordinary),
                            "Nest timer removed a re-dropped nest or changed ordinary item lifetime");
                    manager.remove(redropped);
                    manager.remove(ordinary);
                    World.players[1] = null;
                }
            }
        }
        System.out.println("Bird nest drop checks passed (all five nests, feedback, 49/50 ticks, pickup/re-drop, ordinary lifetime).");
        System.exit(0);
    }

    private static TickTask lastTask() {
        return (TickTask) World.getTaskScheduler().getTasks()
                .get(World.getTaskScheduler().getTasks().size() - 1);
    }

    private static void advance(TickTask expiry) {
        World.tickCount++;
        expiry.tick();
        GroundItemManager.getInstance().run();
    }

    private static boolean hasChirp(byte[] bytes) {
        for (int i = 0; i + 6 <= bytes.length; i++) {
            if ((bytes[i + 1] & 255) == (1516 >> 8) && (bytes[i + 2] & 255) == (1516 & 255)
                    && bytes[i + 3] == 1 && bytes[i + 4] == 0 && bytes[i + 5] == 0) return true;
        }
        return false;
    }

    private static byte[] drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(chunk)) != -1) bytes.write(chunk, 0, count);
        } catch (SocketTimeoutException drained) {
            // Collect synchronous writes until the socket is quiet.
        }
        return bytes.toByteArray();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
