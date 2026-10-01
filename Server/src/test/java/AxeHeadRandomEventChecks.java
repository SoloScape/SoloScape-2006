import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.ground.GroundItemVisibility;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.ItemCombinationHandler;
import com.rs2.model.skill.woodcutting.AxeHeadRandomEvent;
import com.rs2.model.skill.woodcutting.TreeDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;
import java.util.Random;

/** Deterministic loss, pickup, reattachment, and Woodcutting interruption checks. */
public final class AxeHeadRandomEventChecks {
    private static Field socket;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        for (GatheringToolDefinition axe : GatheringToolDefinition.values()) {
            if (axe.getSkillId() != 8) continue;
            for (boolean equipped : new boolean[] {false, true}) {
                Player player = player();
                giveAxe(player, axe, equipped);
                // Full inventory must still retain the handle without dropping it.
                while (player.getInventoryManager().getContainer().getFreeSlots() > 0) {
                    player.getInventoryManager().addItem(new ItemStack(1511));
                    player.getOutboundBuffer().clear();
                }
                int sequence = player.nextActionSequence();
                GroundItem head = AxeHeadRandomEvent.detachHead(player, axe);
                require(head != null, "Missing event for " + axe);
                require(head.getItem().getId() == axe.getToolHeadItemId(), "Wrong head");
                require(head.getOwner().resolve() == player && head.getVisibility() == GroundItemVisibility.PRIVATE,
                        "Head must initially belong to the woodcutter");
                require(GroundItemManager.getInstance().contains(head), "Head not spawned");
                require(!player.isCurrentActionSequence(sequence), "Swing sounds still active");
                require(!player.getInventoryManager().containsItem(axe.getToolItemId())
                        && player.getEquipmentManager().getItemIdAtSlot(3) != axe.getToolItemId(), "Intact axe retained");
                require(equipped ? player.getEquipmentManager().getItemIdAtSlot(3) == axe.getToolHandleItemId()
                        : player.getInventoryManager().containsItem(axe.getToolHandleItemId()), "Handle missing");
                require(head.getPosition().getPlane() == player.getPosition().getPlane(), "Head changed plane");
                require(!head.getPosition().equals(player.getPosition())
                        && WalkingCollisionMap.canTravelBetween(player.getPosition().getX(), player.getPosition().getY(),
                                head.getPosition().getX(), head.getPosition().getY(), head.getPosition().getPlane(), 1, 1),
                        "Head must land nearby and be reachable");
                require(AxeHeadRandomEvent.detachHead(player, axe) == null, "Same axe lost twice");
                player.getInventoryManager().removeItem(new ItemStack(1511, equipped ? 2 : 1));
                if (equipped) {
                    player.getEquipmentManager().removeItem(new ItemStack(axe.getToolHandleItemId()));
                    player.getInventoryManager().addItem(new ItemStack(axe.getToolHandleItemId()));
                }
                player.setPosition(head.getPosition().copy());
                require(GroundItemManager.getInstance().removeForPickup(head, player),
                        "Pickup failed: " + axe + " equipped=" + equipped + " index=" + player.getIndex()
                        + " owner=" + head.getOwner().resolve());
                require(player.getInventoryManager().addItem(head.getItem()), "Head not recovered");
                require(ItemCombinationHandler.handleToolHeadAttachment(player,
                        equipped ? axe.getToolHandleItemId() : axe.getToolHeadItemId(),
                        equipped ? axe.getToolHeadItemId() : axe.getToolHandleItemId()), "Attachment failed");
                require(player.getInventoryManager().getItemAmount(axe.getToolItemId()) == 1, "Wrong restored axe");
                require(!player.getInventoryManager().containsItem(axe.getToolHeadItemId())
                        && !player.getInventoryManager().containsItem(axe.getToolHandleItemId()), "Parts not consumed");
                require(!ItemCombinationHandler.handleToolHeadAttachment(player, axe.getToolHeadItemId(),
                        axe.getToolHandleItemId()), "Absent parts created an axe");
            }
        }
        Player player = player();
        giveAxe(player, GatheringToolDefinition.BRONZE_AXE, false);
        ServerSettings.randomEventsMode = 1;
        require(AxeHeadRandomEvent.detachHead(player, GatheringToolDefinition.BRONZE_AXE) == null, "Disabled event fired");
        ServerSettings.randomEventsMode = 0;
        player.botEnabled = true;
        require(AxeHeadRandomEvent.detachHead(player, GatheringToolDefinition.BRONZE_AXE) == null, "Bot event fired");
        player.botEnabled = false;
        player.setPosition(new Position(3090, 3100));
        require(AxeHeadRandomEvent.detachHead(player, GatheringToolDefinition.BRONZE_AXE) == null, "Tutorial event fired");
        player.setPosition(new Position(3200, 3200));
        require(!ItemCombinationHandler.handleToolHeadAttachment(player, 508, 466), "Pickaxe handle accepted for axe");
        require(!ItemCombinationHandler.handleToolHeadAttachment(player, 508, 0), "Item zero accepted as handle");
        player.getInventoryManager().addItem(new ItemStack(6743));
        player.getInventoryManager().addItem(new ItemStack(492));
        require(ItemCombinationHandler.handleToolHeadAttachment(player, 492, 6743), "Dragon head rejected regular handle");

        // Force the random roll, then exercise the actual Woodcutting cycle.
        Field randomField = GameUtil.class.getDeclaredField("random");
        randomField.setAccessible(true);
        Object oldRandom = randomField.get(null);
        try {
            randomField.set(null, new Random() {
                @Override public int nextInt(int bound) { return 0; }
            });
            int sequence = player.nextActionSequence();
            WoodcuttingTask task = new WoodcuttingTask(player, sequence, TreeDefinition.TREE,
                    3201, 3200, GatheringToolDefinition.BRONZE_AXE, 1276);
            CycleEventContainer cycle = new CycleEventContainer(player, task, 4);
            int logs = player.getInventoryManager().getItemAmount(1511);
            task.execute(cycle);
            require(!cycle.isActive(), "Axe loss did not stop Woodcutting");
            require(player.getInventoryManager().getItemAmount(1511) == logs, "Axe loss also awarded logs");
            require(player.getInventoryManager().containsItem(492), "Woodcutting did not leave a handle");
        } finally {
            randomField.set(null, oldRandom);
        }
        System.out.println("Axe-head event checks passed (all eight axes, equipment/inventory, pickup, attachment, exclusions, chopping cancellation).");
        System.exit(0);
    }

    private static Player player() throws Exception {
        Player player = new Player(null);
        SocketChannel transport = SocketChannel.open();
        transport.close();
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setIndex(1);
        player.setEncodedIndex(32769);
        player.setPosition(new Position(3200, 3200, 0));
        player.refreshLocalViewArea();
        player.setQuestState(0, 1);
        player.getSkillManager().getCurrentLevels()[8] = 99;
        return player;
    }

    private static void giveAxe(Player player, GatheringToolDefinition axe, boolean equipped) {
        if (equipped) player.getEquipmentManager().setSlotItem(axe.getToolItemId(), 3);
        else player.getInventoryManager().addItem(new ItemStack(axe.getToolItemId()));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
