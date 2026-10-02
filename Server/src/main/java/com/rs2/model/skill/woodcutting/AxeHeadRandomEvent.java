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
    // Period guides describe an initial-hit bias, but do not give exact odds.
    private static final int INITIAL_HIT_CHANCE = 2001;
    private static final int CONTINUED_CHOP_CHANCE = 20001;
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    private AxeHeadRandomEvent() {
    }

    public static boolean tryTrigger(Player player, GatheringToolDefinition axe) {
        return tryTrigger(player, axe, CONTINUED_CHOP_CHANCE);
    }

    public static boolean tryTriggerInitialHit(Player player, GatheringToolDefinition axe) {
        return tryTrigger(player, axe, INITIAL_HIT_CHANCE);
    }

    private static boolean tryTrigger(Player player, GatheringToolDefinition axe, int chance) {
        return isEligible(player, axe) && GameUtil.randomInt(chance) == 0
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
        int firstDistance = 3 + GameUtil.randomInt(8); // period descriptions: roughly 3-10 squares
        for (int distanceOffset = 0; distanceOffset < 8; distanceOffset++) {
            int distance = 3 + ((firstDistance - 3 + distanceOffset) % 8);
            for (int directionOffset = 0; directionOffset < DIRECTIONS.length; directionOffset++) {
                int[] direction = DIRECTIONS[(firstDirection + directionOffset) % DIRECTIONS.length];
                int x = origin.getX() + direction[0] * distance;
                int y = origin.getY() + direction[1] * distance;
                if (WalkingCollisionMap.canTravelBetween(origin.getX(), origin.getY(), x, y,
                        origin.getPlane(), 1, 1)) {
                    return new Position(x, y, origin.getPlane());
                }
            }
        }
        // Extremely enclosed areas may have no reachable square at the documented distance.
        // Keep the event recoverable instead of deleting the axe head.
        return origin.copy();
    }
}
