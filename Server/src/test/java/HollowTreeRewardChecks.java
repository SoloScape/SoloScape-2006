import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.woodcutting.TreeDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;
import java.util.Random;

/** Checks the historical hollow-tree no-bark/bark reward split. */
public final class HollowTreeRewardChecks {
    private static Field socket;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.randomEventsMode = 1;
        ServerSettings.xpRate = 1.0;
        ServerSettings.progressiveXpMode = 0;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);

        Field random = GameUtil.class.getDeclaredField("random");
        random.setAccessible(true);
        Object original = random.get(null);
        try {
            random.set(null, new BarkRoll(1));
            Player ordinary = player(3300, 3300);
            runUntilSuccess(ordinary, 2289, 3301, 3300);
            require(ordinary.getInventoryManager().getItemAmount(3239) == 0,
                    "Ordinary hollow-tree success incorrectly awarded bark");
            require(Math.abs(ordinary.getSkillManager().getExperience()[8] - 82.5) < 0.001,
                    "Ordinary hollow-tree success did not award 82.5 XP");

            random.set(null, new BarkRoll(0));
            Player bark = player(3400, 3400);
            runUntilSuccess(bark, 2289, 3401, 3400);
            require(bark.getInventoryManager().getItemAmount(3239) == 1,
                    "Bark outcome did not award bark");
            require(Math.abs(bark.getSkillManager().getExperience()[8] - 357.7) < 0.001,
                    "Bark outcome did not award 357.7 XP");
        } finally {
            random.set(null, original);
        }
        System.out.println("Hollow-tree reward checks passed (82.5 XP no-bark, 357.7 XP bark outcome).");
        System.exit(0);
    }

    private static void runUntilSuccess(Player player, int objectId, int x, int y) {
        int sequence = player.nextActionSequence();
        WoodcuttingTask task = new WoodcuttingTask(player, sequence, TreeDefinition.HOLLOW_TREE,
                x, y, GatheringToolDefinition.DRAGON_AXE, objectId);
        CycleEventContainer cycle = new CycleEventContainer(player, task, 4);
        for (int i = 0; i < 1000 && player.getSkillManager().getExperience()[8] == 0.0; i++) {
            task.execute(cycle);
        }
        require(player.getSkillManager().getExperience()[8] > 0.0, "Hollow-tree success never rolled");
    }

    private static Player player(int x, int y) throws Exception {
        Player player = new Player(null);
        SocketChannel transport = SocketChannel.open();
        transport.close();
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setIndex(1);
        player.setEncodedIndex(32769);
        player.setPosition(new Position(x, y, 0));
        player.refreshLocalViewArea();
        player.setQuestState(0, 1);
        player.getSkillManager().getCurrentLevels()[8] = 99;
        return player;
    }

    private static final class BarkRoll extends Random {
        private final int barkRoll;
        BarkRoll(int barkRoll) { this.barkRoll = barkRoll; }
        @Override public int nextInt(int bound) {
            return bound == 8 ? barkRoll : Math.min(1, bound - 1);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
