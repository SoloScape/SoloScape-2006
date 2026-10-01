import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingHandler;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

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
        for (int[] offset : new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
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
            }
        }
        System.out.println("Woodcutting facing checks passed (four sides, initial swing, repeated cycles, cancellation).");
        System.exit(0);
    }

    private static void requireFacing(Player player) {
        require(player.getUpdateState().isFacePositionUpdateRequired(), "Missing facing update");
        require(player.getUpdateState().getFacePosition().equals(new Position(3200, 3200, 0)), "Not facing tree");
        require(player.getUpdateState().getFaceEntityId() == 65535, "Previous entity still controls facing");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
