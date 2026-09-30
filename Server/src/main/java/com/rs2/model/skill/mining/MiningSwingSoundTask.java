package com.rs2.model.skill.mining;

import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

/** Strike audio runs each tick, independently of the pickaxe's ore-roll interval. */
public final class MiningSwingSoundTask extends TickTask {
    private final Player player;
    private final int actionSequence;
    private final MiningTask miningTask;
    private final int rockObjectId;
    private final int x;
    private final int y;

    public MiningSwingSoundTask(Player player, int actionSequence, MiningTask miningTask,
                               int rockObjectId, int x, int y) {
        super(1);
        this.player = player;
        this.actionSequence = actionSequence;
        this.miningTask = miningTask;
        this.rockObjectId = rockObjectId;
        this.x = x;
        this.y = y;
    }

    @Override
    public final void execute() {
        if (!this.player.isCurrentActionSequence(this.actionSequence)
                || this.player.getActiveCycleEvent() != this.miningTask || this.miningTask.isStopped()) {
            this.stop();
            return;
        }
        DynamicObject rock = ObjectManager.findDynamicObjectAt(this.x, this.y, this.player.getPosition().getPlane());
        if (rock != null && rock.getWorldObject().getObjectId() != this.rockObjectId) {
            this.stop();
            return;
        }
        this.player.packetSender.sendSoundEffect(432, 1, 0);
    }
}
