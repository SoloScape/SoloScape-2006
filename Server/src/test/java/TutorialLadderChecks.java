import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.combat.AttackStyleDefinition;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.TutorialQuest;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

/** Checks delayed ladder arrival without requiring a running game client. */
public final class TutorialLadderChecks {
    public static void main(String[] args) {
        try {
            QuestDefinition.loadDefinitions();
            Npc instructor = new Npc(948);
            instructor.setIndex(0);
            World.getNpcs()[0] = instructor;
            checkLadder(3029, 3088, 3119, 28, 3088, 9520);
            checkLadder(3030, 3111, 9526, 50, 3111, 3125);
            checkOrdinaryLadder();
            System.out.println("Tutorial ladder checks passed (both tutorial exits and ordinary travel).");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static Player newPlayer(int x, int y, int stage) throws Exception {
        Player player = new Player(null);
        player.setIndex(0);
        player.setEncodedIndex(32768);
        SocketChannel transport = SocketChannel.open();
        transport.close();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setPosition(new Position(x, y, 0));
        player.setQuestState(0, stage);
        return player;
    }

    private static void checkLadder(int id, int x, int y, int stage, int targetX, int targetY)
            throws Exception {
        Player player = newPlayer(x, y, stage);
        TutorialQuest tutorial = new TutorialQuest(0);
        tutorial.refreshQuestJournal(player, stage);
        require(player.getOpenInterfaceId() == 6179, "Initial tutorial instructions missing");
        require(tutorial.handleFirstObjectAction(player, id, x, y, stage), "Ladder unhandled");
        require(player.isActionLocked(), "Climbing did not lock actions");
        require(player.getOpenInterfaceId() == 6179,
                "Starting the climb exposed the ordinary chatbox");
        require(player.getQuestState(0) == stage, "Tutorial advanced before arrival");
        CycleEventHandler events = CycleEventHandler.getInstance();
        events.process();
        require(player.getPosition().getY() == y, "Climb delay was skipped");
        require(player.getQuestState(0) == stage, "Tutorial advanced during climb");
        require(player.getOpenInterfaceId() == 6179,
                "Tutorial instructions disappeared during the climb animation");
        events.process();
        require(player.getPosition().getX() == targetX && player.getPosition().getY() == targetY,
                "Player did not arrive at the ladder destination");
        require(player.getQuestState(0) == stage + 1, "Arrival did not advance tutorial");
        require(!player.isActionLocked(), "Arrival left actions locked");
        require(player.getOpenInterfaceId() == 6179, "Teleport cleanup removed new instructions");
        events.process();
        require(player.getOpenInterfaceId() == 6179, "Instructions disappeared after arrival");
        require(player.getQuestState(0) == stage + 1, "Tutorial advanced twice");
        require(!player.isActionLocked(), "Teleport continuation left actions locked");
    }

    private static void checkOrdinaryLadder() throws Exception {
        Player player = newPlayer(3200, 3200, 1);
        AttackStyleDefinition.startDelayedObjectMove(player, new Position(3200, 9600, 0));
        CycleEventHandler events = CycleEventHandler.getInstance();
        events.process();
        events.process();
        events.process();
        require(player.getPosition().getY() == 9600, "Ordinary ladder did not move player");
        require(player.getQuestState(0) == 1 && player.getOpenInterfaceId() == 0,
                "Ordinary ladder opened tutorial instructions");
        require(!player.isActionLocked(), "Ordinary ladder left actions locked");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
