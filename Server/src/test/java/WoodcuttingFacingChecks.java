import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.player.ModernPlayerUpdateTask;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingHandler;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.PacketWriter;

/** Checks initial facing, continued chopping, and cancellation from every side. */
public final class WoodcuttingFacingChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.woodcuttingEnabled = true;
        ServerSettings.randomEventsMode = 1;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        ObjectDefinition tree = ObjectDefinition.forId(1292);
        require(tree.width == 2 && tree.length == 2, "Expected a real 2x2 tree from the cache");
        for (int[] offset : new int[][] {{-1, 0}, {2, 0}, {0, -1}, {0, 2}}) {
            Player player = new Player(null);
            try (SocketChannel transport = SocketChannel.open()) {
                transport.close();
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3200 + offset[0], 3200 + offset[1], 0));
                player.setQuestState(0, 1);
                player.getSkillManager().getCurrentLevels()[8] = 99;
                player.getInventoryManager().addItem(new ItemStack(1351));
                player.getUpdateState().setFaceEntity(42);
                // Dramen branches grant no XP and never deplete: this check sends no relay events.
                WoodcuttingHandler.startWoodcutting(player, 1292, 3200, 3200, false);
                require(player.getActiveCycleEvent() instanceof WoodcuttingTask, "Chopping did not start");
                requireFacing(player);
                if (args.length > 0) Files.write(Paths.get(args[0]), facingPacket(player));
                WoodcuttingTask task = (WoodcuttingTask) player.getActiveCycleEvent();
                CycleEventContainer cycle = new CycleEventContainer(player, task, 4);
                for (int swing = 0; swing < 3; swing++) {
                    player.getUpdateState().reset();
                    player.getUpdateState().setFaceEntity(42);
                    player.getUpdateState().setFacePosition(new Position(3190, 3190));
                    task.execute(cycle);
                    require(cycle.isActive(), "Chopping stopped unexpectedly");
                    requireFacing(player);
                }
                player.nextActionSequence();
                player.getUpdateState().reset();
                task.execute(cycle);
                require(!cycle.isActive(), "Cancelled chopping remained active");
                require(!player.getUpdateState().isFacePositionUpdateRequired(), "Cancelled chopping turned player");
                player.getUpdateState().setFacePosition(new Position(3200, 3200));
                require(player.getUpdateState().getFacePositionHalfX() == 6401
                        && player.getUpdateState().getFacePositionHalfY() == 6401,
                        "Ordinary tile facing inherited the tree footprint");
            }
        }
        System.out.println("Woodcutting facing checks passed (2x2 trunk centre on the wire, four sides, repeated cycles, cancellation, ordinary facing).");
        System.exit(0);
    }

    private static void requireFacing(Player player) throws Exception {
        require(player.getUpdateState().isFacePositionUpdateRequired(), "Missing facing update");
        require(player.getUpdateState().getFacePosition().equals(new Position(3200, 3200, 0)), "Not facing tree");
        require(player.getUpdateState().getFaceEntityId() == 65535, "Previous entity still controls facing");
        byte[] packet = facingPacket(player);
        require(packet.length == 7 && (packet[0] & 0xff) == 0x12, "Wrong facing mask");
        require(((packet[1] - 128) & 0xff) == 255 && (packet[2] & 0xff) == 255,
                "Entity facing was not cleared on the wire");
        int x = ((packet[3] - 128) & 0xff) | ((packet[4] & 0xff) << 8);
        int y = (packet[5] & 0xff) | ((packet[6] & 0xff) << 8);
        require(x == 6402 && y == 6402, "Packet targets southwest tile instead of trunk centre: " + x + "," + y);
    }

    private static byte[] facingPacket(Player player) throws Exception {
        Method write = ModernPlayerUpdateTask.class.getDeclaredMethod("writeUpdateMask",
                Player.class, Player.class, PacketWriter.class, int.class);
        write.setAccessible(true);
        PacketWriter writer = PacketBuffer.allocateWriter(32);
        write.invoke(null, player, player, writer, 0x12);
        ByteBuffer buffer = writer.getBuffer();
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
