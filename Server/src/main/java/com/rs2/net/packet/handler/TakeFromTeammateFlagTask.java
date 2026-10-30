package com.rs2.net.packet.handler;

import com.rs2.model.EntityTargetMovement;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

/** Walk to a Castle Wars teammate before taking their long-held enemy standard. */
public final class TakeFromTeammateFlagTask extends TickTask {
    private final Player taker;
    private final Player carrier;
    private final int actionSequence;

    public TakeFromTeammateFlagTask(Player taker, Player carrier, int actionSequence) {
        super(1);
        this.taker = taker;
        this.carrier = carrier;
        this.actionSequence = actionSequence;
    }

    @Override
    public void execute() {
        if (taker.isDead() || carrier == null || carrier.isDead()
                || !taker.isCurrentActionSequence(actionSequence)
                || !CastleWarsManager.canTakeEnemyFlagFromTeammate(
                        taker, carrier, System.currentTimeMillis())) {
            finishInteraction();
            return;
        }

        if (taker.isWithinReach(carrier, 1) && !taker.isOverlapping(carrier)
                && !EntityTargetMovement.isDiagonalTo(
                        taker.getPosition(), carrier.getPosition())) {
            CastleWarsManager.takeEnemyFlagFromTeammate(taker, carrier);
            taker.getUpdateState().setFacePosition(carrier.getPosition());
            finishInteraction();
        }
    }

    private void finishInteraction() {
        EntityTargetMovement.clearMovementTarget(taker);
        taker.setInteractionTarget(null);
        taker.getMovementQueue().clear();
        stop();
    }
}
