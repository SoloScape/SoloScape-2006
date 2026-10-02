import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingHandler;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;
import java.util.Random;

/** Exercises initial interaction and scheduled chopping separately. */
public final class WoodcuttingRandomEventTimingChecks {
    private static Field socket;
    private static Field random;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.woodcuttingEnabled = true;
        ServerSettings.randomEventsMode = 0;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        random = GameUtil.class.getDeclaredField("random");
        random.setAccessible(true);
        Object original = random.get(null);
        try {
            Rolls lossRoll = new Rolls(1);
            random.set(null, lossRoll);
            Player loss = player();
            start(loss);
            require(loss.getInventoryManager().containsItem(492), "Initial hit did not detach head");
            require(loss.getActiveCycleEvent() == null && loss.ownedNpc == null,
                    "Initial axe loss scheduled chopping or another hazard");
            require(lossRoll.initialRolls == 1, "Axe loss did not short-circuit initial hazards");

            Rolls spiritRoll = new Rolls(2);
            random.set(null, spiritRoll);
            Player spirit = player();
            start(spirit);
            require(spirit.ownedNpc != null && spirit.ownedNpc.getNpcId() >= 438
                    && spirit.ownedNpc.getNpcId() <= 443, "No Tree Spirit on initial attempt");
            require(spirit.getActiveCycleEvent() == null, "Spirit also scheduled chopping");
            require(spirit.getInventoryManager().containsItem(1351), "Spirit detached axe head");

            Rolls continuedRoll = new Rolls(0);
            random.set(null, continuedRoll);
            Player continued = player();
            start(continued);
            require(continuedRoll.initialRolls == 2, "Initial hazard rolls missing");
            WoodcuttingTask task = (WoodcuttingTask) continued.getActiveCycleEvent();
            CycleEventContainer cycle = new CycleEventContainer(continued, task, 4);
            for (int i = 0; i < 3; i++) task.execute(cycle);
            require(continuedRoll.initialRolls == 2 && continued.ownedNpc == null,
                    "Continued chopping rolled a Tree Spirit or initial-hit axe loss");
            require(continuedRoll.continuedRolls == 3, "Continued axe loss chance missing");
            continuedRoll.loseOnContinuation = true;
            int branches = continued.getInventoryManager().getItemAmount(771);
            task.execute(cycle);
            require(!cycle.isActive() && continued.getInventoryManager().containsItem(492),
                    "Continued axe loss failed to cancel chopping");
            require(continued.getInventoryManager().getItemAmount(771) == branches,
                    "Continued axe loss also awarded a branch");

            Rolls blockedRoll = new Rolls(1);
            random.set(null, blockedRoll);
            Player blocked = player();
            blocked.getInventoryManager().removeItem(new ItemStack(1351));
            start(blocked);
            require(blockedRoll.initialRolls == 0, "Invalid attempt rolled a hazard");
            ServerSettings.randomEventsMode = 1;
            Player disabled = player();
            start(disabled);
            require(blockedRoll.initialRolls == 0 && disabled.ownedNpc == null
                    && disabled.getInventoryManager().containsItem(1351), "Disabled events rolled");
            ServerSettings.randomEventsMode = 0;
            Player tutorial = player();
            tutorial.setPosition(new Position(3090, 3100));
            start(tutorial);
            require(blockedRoll.initialRolls == 0 && tutorial.ownedNpc == null,
                    "Tutorial attempt rolled a hazard");
        } finally {
            random.set(null, original);
        }
        System.out.println("Woodcutting random-event timing checks passed (initial spirit/loss, continued loss, cancellation, invalid/disabled/tutorial attempts).");
        System.exit(0);
    }

    private static void start(Player player) {
        // Dramen branches have no XP or depletion, avoiding unrelated rewards in this test.
        WoodcuttingHandler.startWoodcutting(player, 1292, 3201, 3200, false);
    }

    private static Player player() throws Exception {
        Player player = new Player(null);
        SocketChannel transport = SocketChannel.open();
        transport.close();
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setIndex(1);
        player.setEncodedIndex(32769);
        player.setPosition(new Position(3200, 3200));
        player.refreshLocalViewArea();
        player.setQuestState(0, 1);
        player.getSkillManager().getCurrentLevels()[8] = 99;
        player.getInventoryManager().addItem(new ItemStack(1351));
        return player;
    }

    private static final class Rolls extends Random {
        private final int winningInitialRoll;
        int initialRolls;
        int continuedRolls;
        boolean loseOnContinuation;

        Rolls(int winningInitialRoll) { this.winningInitialRoll = winningInitialRoll; }

        @Override public int nextInt(int bound) {
            if (bound == 2001) return ++initialRolls == winningInitialRoll ? 0 : 1;
            if (bound == 20001) {
                continuedRolls++;
                return loseOnContinuation ? 0 : 1;
            }
            return 0;
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
