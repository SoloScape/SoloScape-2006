package com.rs2.model.skill.woodcutting;

import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

public final class WoodcuttingSwingSoundTask extends TickTask {
    private final Player player;
    private final int actionSequence;

    public WoodcuttingSwingSoundTask(Player player, int actionSequence) {
        super(2);
        this.player = player;
        this.actionSequence = actionSequence;
    }

    @Override
    public final void execute() {
        if (this.player.isCurrentActionSequence(this.actionSequence)) {
            this.player.packetSender.sendSoundEffect(472, 1, 0);
        }
        this.stop();
    }
}
