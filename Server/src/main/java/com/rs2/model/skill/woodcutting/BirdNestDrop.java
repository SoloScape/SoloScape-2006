package com.rs2.model.skill.woodcutting;

import com.rs2.model.World;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;
import com.rs2.util.GameUtil;

/** Feedback and short lifetime for nests falling from ordinary or farmed trees. */
public final class BirdNestDrop {
    // Legacy sound ID; AudioIds443 maps it to woodcutting_birdsnest (1516).
    private static final int CHIRP_SOUND = 2516;

    private BirdNestDrop() {
    }

    public static GroundItem spawn(Player player, int nestId) {
        final GroundItem nest = new GroundItem(new ItemStack(nestId), player);
        GroundItemManager.getInstance().spawn(nest);
        player.packetSender.sendGameMessage("A bird's nest falls out of the tree.");
        player.packetSender.sendSoundEffect(CHIRP_SOUND, 1, 0);
        // This lifetime belongs to the tree drop, not to nests later dropped by hand.
        World.scheduleTickTask(new TickTask((int) GameUtil.secondsToTicks(30)) {
            @Override
            public void execute() {
                // Safe if it was picked up already, even if another nest replaced it.
                GroundItemManager.getInstance().remove(nest);
                stop();
            }
        });
        return nest;
    }
}
