import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.interaction.AlKharidGateToll;
import com.rs2.model.interaction.InteractionDispatcher;
import com.rs2.model.interaction.SecondObjectActionTask;
import com.rs2.model.interaction.FirstNpcActionTask;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.WorldObject;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

/** Al-Kharid Pay-toll action, two-way charging, and Prince Ali Rescue exemption. */
public final class AlKharidGateTollChecks {
    private static Field socket;
    private static SocketChannel transport;

    public static void main(String[] args) {
        try {
            ServerSettings.clientBuild = 443;
            QuestDefinition.loadDefinitions();
            NpcDefinition.loadDefinitions();
            ItemDefinition.loadDefinitions();
            ObjectDefinition.loadRevision443();
            WorldObjectLookup.loadWorldObjects();
            WalkingCollisionMap.loadCollisionMaps();
            require(WorldObjectLookup.findObjectByIdAt(2882, 3268, 3227, 0) != null,
                    "South gate leaf missing from cache");
            require(WorldObjectLookup.findObjectByIdAt(2883, 3268, 3228, 0) != null,
                    "North gate leaf missing from cache");

            transport = SocketChannel.open();
            transport.close();
            socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);

            require(AlKharidGateToll.isTollGate(2882, 3268, 3227, 0), "South leaf not recognized");
            require(AlKharidGateToll.isTollGate(2883, 3268, 3228, 0), "North leaf not recognized");
            require(!AlKharidGateToll.isTollGate(2882, 3268, 3229, 0), "Wrong gate coordinates accepted");
            require(!AlKharidGateToll.isTollGate(2882, 3268, 3227, 1), "Wrong plane accepted");
            require(AlKharidGateToll.isTollGate(2883, 3268, 3227, 0),
                    "Alternate gate-leaf mapping should accept pay-toll");
            require(AlKharidGateToll.isTollGate(2882, 3268, 3228, 0),
                    "Alternate north gate-leaf mapping should accept pay-toll");

            // The actual second object action must be wired, not just the guard dialogue.
            checkPayTollClick(2882, 3227, 3267, 0, 20, 10, true);
            checkPayTollClick(2883, 3228, 3268, 0, 20, 10, true);
            checkPayTollClick(2882, 3227, 3267, 0, 9, 9, false);
            checkPayTollClick(2883, 3228, 3268, 9, 20, 10, true);
            checkPayTollClick(2882, 3227, 3267, 1, 0, 0, true);
            checkPayTollClick(2883, 3228, 3268, 1, 20, 20, true);
            checkBorderGuardDialogue();
            System.out.println("Al Kharid gate toll checks passed (gate, funds, quest, guard dialogue).");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkPayTollClick(int gateId, int gateY, int playerX,
                                          int questStage, int coins, int expectedCoins,
                                          boolean shouldCross) throws Exception {
        ObjectManager.activeDynamicObjects.clear();
        Player player = new Player(null);
        player.setIndex(1);
        player.setEncodedIndex(32768);
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setPosition(new Position(playerX, gateY, 0));
        player.setQuestState(11, questStage);
        if (coins > 0) {
            player.getInventoryManager().getContainer().setItem(0, new ItemStack(995, coins));
        }
        player.setInteractionTargetId(gateId);
        player.setInteractionTargetX(3268);
        player.setInteractionTargetY(gateY);
        player.setInteractionTargetPlane(0);
        ObjectManager.prepareObjectInteractionMovement(player, gateId, 3268, gateY);
        WorldObject gate = com.rs2.model.skill.SkillActionHelper.findWorldObjectById(gateId, 3268, gateY, 0);
        require(gate != null, "Gate not found for second-click");
        require(InteractionDispatcher.canReachObjectInteraction(player, gate),
                "Pay-toll cannot be reached from x=" + playerX);

        new SecondObjectActionTask(1, true, player, player.nextActionSequence(),
                gateId, 3268, gateY, 0).execute();
        require(player.getInventoryManager().getItemAmount(995) == expectedCoins,
                "Wrong toll for quest stage " + questStage + " from x=" + playerX);
        require(player.forcedMovementActive == shouldCross,
                "Unexpected gate traversal for quest stage " + questStage + " from x=" + playerX);
        if (shouldCross) {
            player.getMovementQueue().process();
            require(player.getPosition().getX() == (playerX < 3268 ? playerX + 1 : playerX - 1),
                    "Crossing did not move through the gate from x=" + playerX);
        }
    }

    private static void checkBorderGuardDialogue() throws Exception {
        int borderGuardId = -1;
        for (int id = 0; id < World.getNpcDefinitions().length; id++) {
            NpcDefinition definition = World.getNpcDefinitions()[id];
            if (definition != null && "Border Guard".equalsIgnoreCase(definition.getName())) {
                borderGuardId = id;
                break;
            }
        }
        require(borderGuardId >= 0, "Border Guard is missing from NPC definitions");
        System.out.println("Al Kharid cached Border Guard NPC id=" + borderGuardId);

        Npc guard = new Npc(borderGuardId);
        guard.setPosition(new Position(3267, 3226, 0));
        // A manually instantiated non-combat NPC may have zero configured HP.
        guard.setCurrentHitpoints(1);
        require(AlKharidGateToll.isBorderGuard(guard), "Gate guard not recognized");
        guard.setPosition(new Position(3200, 3226, 0));
        require(!AlKharidGateToll.isBorderGuard(guard), "Remote border guard incorrectly matched");
        guard.setPosition(new Position(3267, 3226, 0));

        Player player = new Player(null);
        player.setIndex(1);
        player.setEncodedIndex(32768);
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setUsername("TollTester");
        player.setPosition(new Position(3266, 3226, 0));
        player.setSize(1);
        guard.setSize(1);
        player.setQuestState(11, 0);
        player.getInventoryManager().getContainer().setItem(0, new ItemStack(995, 20));
        player.setInteractionTargetId(borderGuardId);

        require(player.isWithinReach(guard, 1), "Guard interaction is not within reach");
        new FirstNpcActionTask(1, true, player, player.nextActionSequence(), guard).execute();
        require(player.getDialogueManager().getDialogueId() == 9999,
                "Talking to border guard did not open toll dialogue");
        require(DialogueManager.continueDialogue(player, 9999, 2, 0),
                "Border guard did not explain toll");
        require(DialogueManager.continueDialogue(player, 9999, 3, 0),
                "Border guard did not present dialogue options");
        require(DialogueManager.continueDialogue(player, 9999, 4, 2),
                "Border guard did not explain where payment goes");
        require(player.getInventoryManager().removeItem(new ItemStack(995, 20)),
                "Could not empty test inventory for no-money dialogue");
        require(DialogueManager.startDialogue(player, 9999), "Could not restart toll dialogue");
        require(DialogueManager.continueDialogue(player, 9999, 2, 0),
                "Border guard did not request payment with zero coins");
        require(DialogueManager.continueDialogue(player, 9999, 7, 0),
                "Border guard did not offer no-money dialogue choices");
        require(DialogueManager.continueDialogue(player, 9999, 8, 2),
                "Border guard did not acknowledge insufficient coins");
        require(player.getDialogueManager().getDialogueStep() > 9000,
                "Insufficient-funds reply did not finish the conversation");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
