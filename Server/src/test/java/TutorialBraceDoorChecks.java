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

/** Cache-backed checks for the Brother Brace exit swing and traversal. */
public final class TutorialBraceDoorChecks {
    public static void main(String[] args) {
        try {
            QuestDefinition.loadDefinitions();
            Npc wizard = new Npc(946);
            wizard.setIndex(0);
            wizard.setPosition(new Position(3140, 3087, 0));
            World.getNpcs()[0] = wizard;
            WorldObjectLookup.loadWorldObjects();
            WalkingCollisionMap.loadCollisionMaps();
            LoadedWorldObject door = WorldObjectLookup.findObjectByIdAt(3026, 3122, 3102, 0);
            require(door != null && door.getType() == 0 && door.getOrientation() == 1,
                    "Unexpected Brother Brace door placement");
            int[] flags = new int[3];
            for (int x = 3121; x <= 3123; x++) {
                if (x != 3122) {
                    require(WorldObjectLookup.findObjectByIdAt(1902, x, 3102, 0) != null,
                            "Expected neighbouring wall is missing from the cache");
                }
                flags[x - 3121] = WalkingCollisionMap.getTileFlags(x, 3102, 0);
            }
            SocketChannel transport = SocketChannel.open();
            transport.close();
            Field socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);
            TutorialQuest tutorial = new TutorialQuest(0);
            for (boolean running : new boolean[] {false, true}) {
                Player player = new Player(null);
                player.setIndex(1);
                player.setEncodedIndex(32768);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3122, 3103, 0));
                player.setQuestState(0, 61);
                player.getMovementQueue().setRunning(running);
                player.setRunEnergyPercent(100);
                require(tutorial.handleFirstObjectAction(player, 3026, 3122, 3102, 61),
                        "Brother Brace door click was not handled");
                DynamicObject opened = ObjectManager.findDynamicObjectByIdAt(3026, 3122, 3103, 0);
                require(opened != null && opened.getWorldObject().getOrientation() == 2,
                        "Brother Brace door did not swing north into the chapel");
                require(player.forcedMovementActive, "Door crossing did not bypass clipping");
                for (int tick = 0; tick < 4; tick++) {
                    for (int x : new int[] {3121, 3123}) {
                        require(ObjectManager.findDynamicObjectAt(x, 3102, 0) == null,
                                "Door animation overwrote a neighbouring wall");
                    }
                    ObjectManager.getInstance().processObjects();
                    CycleEventHandler.getInstance().process();
                    player.getMovementQueue().process();
                }
                require(player.getPosition().equals(new Position(3122, 3102, 0)),
                        "Player did not cross the chapel exit");
                require(player.getQuestState(0) == 62, "Door did not advance the tutorial");
                for (int x = 3121; x <= 3123; x++) {
                    require(WalkingCollisionMap.getTileFlags(x, 3102, 0) == flags[x - 3121],
                            "Door animation changed static collision");
                }
                require(ObjectManager.activeDynamicObjects.isEmpty(), "Door animation did not expire");
                require(!player.forcedMovementActive && !player.isActionLocked(),
                        "Door traversal did not release movement locks");
            }
            System.out.println("Brother Brace door checks passed (walking and running).");
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
