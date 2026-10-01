import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.LoadedWorldObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.TutorialQuest;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

/** Cache-backed checks that both bank doors swing clear of adjacent walls. */
public final class TutorialBankDoorChecks {
    public static void main(String[] args) {
        try {
            QuestDefinition.loadDefinitions();
            Npc advisor = new Npc(947);
            advisor.setIndex(0);
            advisor.setPosition(new Position(3127, 3124, 0));
            World.getNpcs()[0] = advisor;
            Npc monk = new Npc(954);
            monk.setIndex(1);
            monk.setPosition(new Position(3124, 3107, 0));
            World.getNpcs()[1] = monk;
            WorldObjectLookup.loadWorldObjects();
            WalkingCollisionMap.loadCollisionMaps();
            SocketChannel transport = SocketChannel.open();
            transport.close();
            Field socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);
            TutorialQuest tutorial = new TutorialQuest(0);
            for (int[] doorCase : new int[][] {{3024, 3125, 52}, {3025, 3130, 54}}) {
                int objectId = doorCase[0];
                int doorX = doorCase[1];
                int stage = doorCase[2];
                LoadedWorldObject door = WorldObjectLookup.findObjectByIdAt(objectId, doorX, 3124, 0);
                require(door != null && door.getType() == 0 && door.getOrientation() == 0,
                        "Unexpected tutorial bank door placement: " + objectId);
                require(WorldObjectLookup.findObjectByIdAt(1902, doorX, 3125, 0) != null,
                        "Expected wall beside door is missing from cache: " + objectId);
                int wallFlags = WalkingCollisionMap.getTileFlags(doorX, 3125, 0);
                int doorFlags = WalkingCollisionMap.getTileFlags(doorX, 3124, 0);
                for (boolean running : new boolean[] {false, true}) {
                    Player player = new Player(null);
                    player.setIndex(1);
                    player.setEncodedIndex(32768);
                    socket.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setPosition(new Position(doorX - 1, 3124, 0));
                    player.setQuestState(0, stage);
                    player.getMovementQueue().setRunning(running);
                    player.setRunEnergyPercent(100);
                    require(tutorial.handleFirstObjectAction(player, objectId, doorX, 3124, stage),
                            "Bank door click was not handled");
                    DynamicObject opened = ObjectManager.findDynamicObjectByIdAt(objectId, doorX - 1, 3124, 0);
                    require(opened != null && opened.getWorldObject().getOrientation() == 1,
                            "Bank door did not swing west into the room: " + objectId);
                    require(ObjectManager.findDynamicObjectAt(doorX, 3125, 0) == null,
                            "Bank door animation overwrote the neighbouring wall");
                    require(player.forcedMovementActive, "Door crossing did not bypass door clipping");
                    for (int tick = 0; tick < 4; tick++) {
                        ObjectManager.getInstance().processObjects();
                        CycleEventHandler.getInstance().process();
                        player.getMovementQueue().process();
                    }
                    require(player.getPosition().equals(new Position(doorX, 3124, 0)),
                            "Player did not cross the doorway");
                    require(player.getQuestState(0) == stage + 1, "Door did not advance the tutorial");
                    require(WalkingCollisionMap.getTileFlags(doorX, 3125, 0) == wallFlags
                                    && WalkingCollisionMap.getTileFlags(doorX, 3124, 0) == doorFlags,
                            "Bank door animation changed static collision");
                    require(ObjectManager.activeDynamicObjects.isEmpty(), "Door animation did not expire");
                    require(!player.forcedMovementActive && !player.isActionLocked(),
                            "Door traversal did not release movement locks");
                }
            }
            System.out.println("Tutorial bank door checks passed (both doors, walking and running).");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
