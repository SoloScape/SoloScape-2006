import com.rs2.model.Position;
import com.rs2.model.interaction.FirstObjectActionTask;
import com.rs2.model.interaction.InteractionDispatcher;
import com.rs2.model.objects.LoadedWorldObject;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

/** Cache-backed issue #4 regression for both Wizards' Tower basement ladder directions. */
public final class WizardsTowerLadderChecks {
    public static void main(String[] args) throws Exception {
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        QuestDefinition.loadDefinitions();
        Field castleWars = com.rs2.model.gameplay.castlewars.CastleWarsManager.class
                .getDeclaredField("initialized");
        castleWars.setAccessible(true);
        castleWars.setBoolean(null, true);

        checkLadderDefinition(2147, 3104, 3162, "Climb-down");
        checkLadderDefinition(2148, 3103, 9576, "Climb-up");

        Player player = new Player(null);
        player.setIndex(0);
        player.setEncodedIndex(32768);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        SocketChannel transport = SocketChannel.open();
        transport.close();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        socket.set(player, transport);
        player.setQuestState(0, 100);
        player.setPosition(new Position(3105, 3162, 0));

        climb(player, 2147, 3104, 3162, 3104, 9576, 827);
        climb(player, 2148, 3103, 9576, 3105, 3162, 828);
        System.out.println("Wizards' Tower ladder round trip passed.");
        System.exit(0);
    }

    private static void checkLadderDefinition(int id, int x, int y, String option) {
        LoadedWorldObject object = WorldObjectLookup.findObjectByIdAt(id, x, y, 0);
        require(object != null && object.getType() == 10, "Missing cached ladder " + id);
        ObjectDefinition definition = ObjectDefinition.forId(id);
        require("Ladder".equals(definition.getName())
                && option.equals(definition.getAction(0)), "Wrong ladder click option " + id);
    }

    private static void climb(Player player, int id, int x, int y,
                              int targetX, int targetY, int expectedAnimation) {
        LoadedWorldObject object = WorldObjectLookup.findObjectByIdAt(id, x, y, 0);
        player.setInteractionTargetId(id);
        player.setInteractionTargetX(x);
        player.setInteractionTargetY(y);
        player.setInteractionTargetPlane(0);
        ObjectManager.prepareObjectInteractionMovement(player, id, x, y);
        System.out.println("Ladder " + id + " orientation=" + object.getOrientation()
                + " exact approach=" + player.interactionApproachX + ","
                + player.interactionApproachY);
        if (id == 2148) {
            require(player.interactionApproachX == 3104
                    && player.interactionApproachY == 9576,
                    "Basement ladder approach must be its reachable east-side tile");
        }
        boolean reachable = InteractionDispatcher.canReachObjectInteraction(player, object.getWorldObject());
        System.out.println("Ladder " + id + " reachability from "
                + player.getPosition().getX() + "," + player.getPosition().getY()
                + ": " + reachable);
        int sequence = player.nextActionSequence();
        new FirstObjectActionTask(1, true, player, sequence, id, x, y, 0, "ladder").execute();
        require(player.isActionLocked(), "Ladder " + id + " click did not begin climb");
        require(player.getUpdateState().getAnimationId() == expectedAnimation,
                "Ladder " + id + " played animation "
                + player.getUpdateState().getAnimationId() + " instead of " + expectedAnimation);
        for (int tick = 0; tick < 4; tick++) CycleEventHandler.getInstance().process();
        require(player.getPosition().getX() == targetX
                && player.getPosition().getY() == targetY
                && player.getPosition().getPlane() == 0 && !player.isActionLocked(),
                "Ladder " + id + " failed arrival at " + targetX + "," + targetY);
        require((WalkingCollisionMap.getTileFlags(targetX, targetY, 0) & 256) == 0,
                "Ladder " + id + " landed inside a blocking object");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
