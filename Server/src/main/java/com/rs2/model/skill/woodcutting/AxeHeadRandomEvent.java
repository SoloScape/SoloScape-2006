package com.rs2.model.skill.woodcutting;

import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;

/** The pre-July-2007 axe head loss event. */
public final class AxeHeadRandomEvent {
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    private AxeHeadRandomEvent() {
    }

    public static boolean tryTrigger(Player player, GatheringToolDefinition axe) {
        return isEligible(player, axe) && GameUtil.randomInt(2001) == 0
                && detachHead(player, axe) != null;
    }

    private static boolean isEligible(Player player, GatheringToolDefinition axe) {
        return axe != null && axe.getSkillId() == 8 && ServerSettings.randomEventsMode == 0
                && !player.botEnabled && !player.isInTutorialIsland() && player.getQuestState(0) == 1
                && player.ownedNpc == null
                && ItemDefinition.isDefined(axe.getToolHeadItemId())
                && ItemDefinition.isDefined(axe.getToolHandleItemId())
                && (player.getEquipmentManager().getItemIdAtSlot(3) == axe.getToolItemId()
                    || player.getInventoryManager().containsItem(axe.getToolItemId()));
    }

    /** Performs the event separately from its random roll. */
    public static GroundItem detachHead(Player player, GatheringToolDefinition axe) {
        if (!isEligible(player, axe)) {
            return null;
        }
        Position landing = findLandingPosition(player.getPosition());
        if (player.getEquipmentManager().getItemIdAtSlot(3) == axe.getToolItemId()) {
            player.getEquipmentManager().replaceSlotItem(axe.getToolHandleItemId(), 3);
        } else {
            // Removing first preserves space for the handle even with a full inventory.
            player.getInventoryManager().replaceItem(new ItemStack(axe.getToolItemId()),
                    new ItemStack(axe.getToolHandleItemId()));
        }
        GroundItem head = new GroundItem(new ItemStack(axe.getToolHeadItemId()), player, landing);
        GroundItemManager.getInstance().spawn(head);
        player.nextActionSequence();
        player.getUpdateState().setAnimation(-1);
        player.packetSender.sendGameMessage("Your axe head flies off! Retrieve it and use it on the handle to reattach it.");
        return head;
    }

    private static Position findLandingPosition(Position origin) {
        int firstDirection = GameUtil.randomInt(DIRECTIONS.length);
        for (int distance = 3; distance >= 1; distance--) {
            for (int offset = 0; offset < DIRECTIONS.length; offset++) {
                int[] direction = DIRECTIONS[(firstDirection + offset) % DIRECTIONS.length];
                int x = origin.getX() + direction[0] * distance;
                int y = origin.getY() + direction[1] * distance;
                if (WalkingCollisionMap.canTravelBetween(origin.getX(), origin.getY(), x, y,
                        origin.getPlane(), 1, 1)) {
                    return new Position(x, y, origin.getPlane());
                }
            }
        }
        return origin.copy();
    }
}
