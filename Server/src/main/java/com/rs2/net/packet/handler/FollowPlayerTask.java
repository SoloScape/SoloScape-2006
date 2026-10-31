package com.rs2.net.packet.handler;

import com.rs2.model.EntityTargetMovement;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;

/**
 * Keeps a Follow player interaction active while the target moves.
 * EntityTargetMovement handles the actual pathing each world tick.
 */
public final class FollowPlayerTask extends TickTask {
    private final Player targetPlayer;
    private final Player requestingPlayer;
    private final int actionSequence;

    public FollowPlayerTask(PlayerInteractionPacketHandler handler, int intervalTicks,
                            Player targetPlayer, Player requestingPlayer, int actionSequence) {
        super(intervalTicks);
        this.targetPlayer = targetPlayer;
        this.requestingPlayer = requestingPlayer;
        this.actionSequence = actionSequence;
    }

    @Override
    public void execute() {
        // An unrelated click or interaction now owns movement; an old follow
        // task must never erase the newer action's movement or animation.
        if (!requestingPlayer.isCurrentActionSequence(actionSequence)) {
            stop();
            return;
        }
        if (requestingPlayer.getMovementTarget() != targetPlayer) {
            if (requestingPlayer.getInteractionTarget() == targetPlayer) {
                requestingPlayer.setInteractionTarget(null);
            }
            stop();
            return;
        }

        if (targetPlayer == null || requestingPlayer.isDead()
                || targetPlayer.isDead()
                || !targetPlayer.getPosition().isWithinDistance(
                        requestingPlayer.getPosition(), 20)
                || !isCurrentWorldPlayer(targetPlayer)) {
            EntityTargetMovement.clearMovementTarget(requestingPlayer);
            if (requestingPlayer.getInteractionTarget() == targetPlayer) {
                requestingPlayer.setInteractionTarget(null);
            }
            requestingPlayer.getMovementQueue().clear();
            stop();
        }

        // Reaching the player is not the end of Follow. In particular, do not
        // play animation 1833 (rubber-chicken whack) or clear the target here.
        // The normal target-movement tick keeps tracking them if they walk away.
    }

    private static boolean isCurrentWorldPlayer(Player player) {
        int index = player.getIndex();
        return index >= 0 && index < com.rs2.model.World.getPlayers().length
                && com.rs2.model.World.getPlayers()[index] == player;
    }
}
