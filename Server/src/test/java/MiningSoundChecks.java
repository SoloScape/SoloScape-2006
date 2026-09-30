import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.mining.MiningTask;
import com.rs2.model.skill.mining.MiningSwingSoundTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.AudioIds443;
import com.rs2.util.path.WalkingCollisionMap;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Checks every swing between ore rolls, completion audio, and sound cancellation. */
public final class MiningSoundChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        for (int stage : new int[] {33, 34}) {
            try (ServerSocketChannel listener = ServerSocketChannel.open()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                try (Socket client = new Socket()) {
                    client.connect(listener.getLocalAddress());
                    client.setSoTimeout(100);
                    try (SocketChannel transport = listener.accept()) {
                        Player player = new Player(null);
                        socket.set(player, transport);
                        player.setOutboundCipher(new IsaacCipher(new int[4]));
                        player.setPosition(new Position(3080, 9505, 0));
                        player.setQuestState(0, stage);
                        player.getSkillManager().getCurrentLevels()[14] = 1;
                        player.getInventoryManager().addItem(new ItemStack(1265, 1));
                        drain(client);
                        int rock = stage == 33 ? 3043 : 3042;
                        int x = stage == 33 ? 3076 : 3086;
                        int y = stage == 33 ? 9504 : 9501;
                        require(WorldObjectLookup.findObjectByIdAt(rock, x, y, 0) != null,
                                "Tutorial rock missing from cache");
                        player.getMiningManager().startMining(rock, x, y);
                        require(player.getActiveCycleEvent() instanceof MiningTask, "Mining did not start");
                        drain(client);
                        MiningSwingSoundTask swings = (MiningSwingSoundTask) World.getTaskScheduler().getTasks()
                                .get(World.getTaskScheduler().getTasks().size() - 1);
                        for (int tick = 1; tick < 8; tick++) {
                            swings.tick();
                            requireSound(drain(client), 432, "Swing before first bronze ore roll, tick " + tick);
                        }
                        require(!player.getInventoryManager().containsItem(stage == 33 ? 438 : 436),
                                "Swing audio awarded ore between rolls");

                        // Force a failed ore roll to check another swing without changing the rock.
                        int sequence = player.nextActionSequence();
                        MiningTask retry = new MiningTask(player.getMiningManager(), sequence, rock, x, y,
                                GatheringToolDefinition.BRONZE_PICKAXE, -1000, -1000,
                                stage == 33 ? 438 : 436, 18, 1.0, 4);
                        player.setActiveCycleEvent(retry);
                        MiningSwingSoundTask retrySounds = new MiningSwingSoundTask(player, sequence, retry, rock, x, y);
                        CycleEventContainer event = new CycleEventContainer(player, retry, 8);
                        retry.execute(event);
                        require(drain(client).length == 0, "Ore roll duplicated swing audio");
                        retrySounds.tick();
                        requireSound(drain(client), 432, "Strike after failed ore roll");
                        require(event.isActive(), "Failed roll stopped mining");
                        player.resetInteractionState();
                        drain(client);
                        retrySounds.tick();
                        swings.tick();
                        retry.execute(event);
                        require(drain(client).length == 0, "Cancelled mining sent another strike");
                        require(!event.isActive(), "Cancelled mining kept running");
                        require(!retrySounds.isActive() && !swings.isActive(), "Old swing tasks kept running");

                        sequence = player.nextActionSequence();
                        MiningTask success = new MiningTask(player.getMiningManager(), sequence, rock, x, y,
                                GatheringToolDefinition.BRONZE_PICKAXE, 1000, 1000,
                                stage == 33 ? 438 : 436, 18, 1.0, 4);
                        player.setActiveCycleEvent(success);
                        MiningSwingSoundTask successSounds = new MiningSwingSoundTask(player, sequence, success, rock, x, y);
                        CycleEventContainer successEvent = new CycleEventContainer(player, success, 8);
                        success.execute(successEvent);
                        requireSound(drain(client), 429, "Ore completion strike");
                        require(!successEvent.isActive(), "Successful mining did not stop");
                        successSounds.tick();
                        require(drain(client).length == 0 && !successSounds.isActive(), "Completed mining kept sounding");
                    }
                }
            }
        }
        System.out.println("Mining sound checks passed (seven swings before ore roll, retry, cancellation, completion; tin/copper).");
        System.exit(0);
    }

    private static byte[] drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(chunk)) != -1) bytes.write(chunk, 0, count);
        } catch (SocketTimeoutException drained) {
            // The handler writes synchronously; collect until the connection is quiet.
        }
        return bytes.toByteArray();
    }

    private static void requireSound(byte[] bytes, int legacyId, String context) {
        int id = AudioIds443.sound(legacyId);
        for (int offset = 0; offset + 6 <= bytes.length; offset++) {
            if (id >= 0 && (bytes[offset + 1] & 255) == (id >> 8)
                    && (bytes[offset + 2] & 255) == (id & 255) && bytes[offset + 3] == 1
                    && bytes[offset + 4] == 0 && bytes[offset + 5] == 0) return;
        }
        throw new AssertionError(context + " did not send the mapped mining sound packet");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
