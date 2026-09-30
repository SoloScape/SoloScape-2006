import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.interaction.InteractionDispatcher;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.LoadedWorldObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.objects.WorldObject;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.TutorialQuest;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.handler.ObjectInteractionPacketHandler;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

/** Cache-backed regression checks; run from Server with its libraries on the classpath. */
public final class QuestGuideDoorChecks {
    public static void main(String[] args) {
        try {
            QuestDefinition.loadDefinitions();
            Npc guide = new Npc(949);
            guide.setIndex(0);
            guide.setPosition(new Position(3086, 3122, 0));
            World.getNpcs()[0] = guide;
            WorldObjectLookup.loadWorldObjects();
            WalkingCollisionMap.loadCollisionMaps();
            LoadedWorldObject door = WorldObjectLookup.findObjectByIdAt(3019, 3086, 3126, 0);
            require(door != null && door.getType() == 0 && door.getOrientation() == 3,
                    "Unexpected Quest Guide door placement in the cache");
            int wallFlags = WalkingCollisionMap.getTileFlags(3085, 3126, 0);
            int doorFlags = WalkingCollisionMap.getTileFlags(3086, 3126, 0);
            TutorialQuest tutorial = new TutorialQuest(0);
            // A closed transport discards packets while retaining real-player movement semantics.
            SocketChannel transport = SocketChannel.open();
            transport.close();
            Field socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);

            Player approaching = new Player(null);
            approaching.setIndex(1);
            approaching.setEncodedIndex(32768);
            socket.set(approaching, transport);
            approaching.setOutboundCipher(new IsaacCipher(new int[4]));
            approaching.setPosition(new Position(3086, 3128, 0));
            approaching.setQuestState(0, 24);
            approaching.setInteractionTargetId(3019);
            approaching.setInteractionTargetX(3086);
            approaching.setInteractionTargetY(3126);
            approaching.setInteractionTargetPlane(0);
            ObjectManager.prepareObjectInteractionMovement(approaching, 3019, 3086, 3126);
            WorldObject closedDoor = new WorldObject(3019, door.getType(), door.getOrientation(),
                    new Position(3086, 3126, 0));
            require(approaching.interactionApproachY == 3126,
                    "Door approach is not one tile closer");
            require(ObjectInteractionPacketHandler.queueObjectInteractionMovement(approaching, closedDoor),
                    "Could not path to the closer approach tile");
            approaching.getMovementQueue().process();
            require(approaching.getPosition().getY() == 3127
                            && !InteractionDispatcher.canReachObjectInteraction(approaching, closedDoor),
                    "Door can open before the player reaches the closer tile");
            approaching.getMovementQueue().process();
            require(approaching.getPosition().getY() == 3126
                            && InteractionDispatcher.canReachObjectInteraction(approaching, closedDoor),
                    "Closer tile was not reachable with the door closed");
            require(!approaching.forcedMovementActive && ObjectManager.activeDynamicObjects.isEmpty(),
                    "Approach opened the door or bypassed collision");

            // Saved characters stranded after the old one-tile step can retry
            // without advancing or resetting their Quest Guide dialogue stage.
            for (boolean running : new boolean[] {false, true}) {
                for (int stage = 24; stage <= 28; stage++) {
                    for (int approachY : new int[] {3128, 3127, 3126, 3125}) {
                        ObjectManager.activeDynamicObjects.clear();
                        Player player = new Player(null);
                        player.setIndex(1);
                        player.setEncodedIndex(32768);
                        socket.set(player, transport);
                        player.setOutboundCipher(new IsaacCipher(new int[4]));
                        player.setPosition(new Position(3086, approachY, 0));
                        player.setQuestState(0, stage);
                        player.getMovementQueue().setRunning(running);
                        player.setRunEnergyPercent(100);
                        require(tutorial.handleFirstObjectAction(player, 3019, 3086, 3126, stage),
                                "Door click was not handled");
                        DynamicObject opened = ObjectManager.findDynamicObjectByIdAt(3019, 3086, 3125, 0);
                        require(opened != null && opened.getWorldObject().getOrientation() == 0,
                                "Door did not swing inward");
                        require(ObjectManager.findDynamicObjectAt(3085, 3126, 0) == null,
                                "Door replaced the neighbouring wall");
                        require(player.forcedMovementActive, "Door traversal did not bypass static clipping");
                        // World processes object expiry and events before movement.
                        for (int tick = 0; tick < 3; tick++) {
                            ObjectManager.getInstance().processObjects();
                            CycleEventHandler.getInstance().process();
                            player.getMovementQueue().process();
                            if (!player.getMovementQueue().getSteps().isEmpty()) {
                                require(player.forcedMovementActive, "Clipping bypass ended before arrival");
                                require(ObjectManager.findDynamicObjectByIdAt(3019, 3086, 3125, 0) != null,
                                        "Door closed before arrival");
                            }
                        }
                        int expectedY = approachY >= 3126 ? 3125 : 3126;
                        require(player.getPosition().getY() == expectedY,
                                "Player did not cross the doorway from " + approachY);
                        require(player.getQuestState(0) == (stage == 24 ? 25 : stage),
                                "Door changed tutorial progress incorrectly");
                        require(WalkingCollisionMap.getTileFlags(3085, 3126, 0) == wallFlags
                                        && WalkingCollisionMap.getTileFlags(3086, 3126, 0) == doorFlags,
                                "Scripted door changed static collision");
                        for (int tick = 0; tick < 3; tick++) {
                            ObjectManager.getInstance().processObjects();
                            CycleEventHandler.getInstance().process();
                        }
                        require(!player.forcedMovementActive && !player.isActionLocked(),
                                "Traversal did not release movement/action locks");
                        require(ObjectManager.activeDynamicObjects.isEmpty(),
                                "Door animation did not expire");
                    }
                }
            }
            System.out.println("Quest Guide door checks passed (40 walking/running traversal/recovery cases).");
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
