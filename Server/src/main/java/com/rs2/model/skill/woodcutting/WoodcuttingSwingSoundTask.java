package com.rs2.model.skill.woodcutting;

import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

public final class WoodcuttingSwingSoundTask extends TickTask {
    private final Player player;
    private final int actionSequence;
    private final int clientDelay;

    public WoodcuttingSwingSoundTask(Player player, int actionSequence) {
        this(player, actionSequence, 1, 15);
    }

    public WoodcuttingSwingSoundTask(Player player, int actionSequence, int serverTicks, int clientDelay) {
        super(serverTicks);
        this.player = player;
        this.actionSequence = actionSequence;
        this.clientDelay = clientDelay;
    }

    @Override
    public final void execute() {
        if (this.player.isCurrentActionSequence(this.actionSequence)) {
            this.player.packetSender.sendSoundEffect(472, 1, this.clientDelay);
        }
        this.stop();
    }
}
