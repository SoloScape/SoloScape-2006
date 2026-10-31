package com.rs2.net.packet.handler;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.player.Player;

/**
 * Reproduces the right-click Follow action from both the legacy and
 * revision-443 player-option handlers. Follow must never act as Whack.
 */
public final class FollowPlayerTaskChecks {
    public static void main(String[] args) {
        Player follower = new Player(null);
        Player target = new Player(null);
        Player otherTarget = new Player(null);
        follower.isBot = true;
        target.isBot = true;
        otherTarget.isBot = true;
        follower.setPosition(new Position(2441, 3090, 0));
        target.setPosition(new Position(2442, 3090, 0));
        otherTarget.setPosition(new Position(2443, 3090, 0));
        World.registerPlayer(follower);
        World.registerPlayer(target);
        World.registerPlayer(otherTarget);
        try {
            follower.setInteractionTarget(target);
            follower.setAttackRange(1);
            follower.setMovementTarget(target);
            FollowPlayerTask follow = new FollowPlayerTask(null, 1, target, follower,
                    follower.nextActionSequence());

            // The original bug played animation 1833 and cancelled Follow
            // as soon as the follower was standing next to the target.
            follow.execute();
            require(follow.isActive(), "Follow stopped on reaching the target");
            require(follower.getMovementTarget() == target,
                    "Follow lost its movement target at one-tile distance");
            require(!follower.getUpdateState().isAnimationUpdateRequired(),
                    "Follow triggered the rubber-chicken whack animation");

            target.getPosition().translate(2, 0);
            follow.execute();
            require(follow.isActive() && follower.getMovementTarget() == target,
                    "Follow did not continue when its target moved away");
            require(!follower.getUpdateState().isAnimationUpdateRequired(),
                    "Follow animated when the target moved");

            // An unrelated right-click action must supersede Follow. The
            // cancelled task cannot destroy the newer target or animation.
            follower.setMovementTarget(otherTarget);
            follower.setInteractionTarget(otherTarget);
            follower.nextActionSequence();
            follow.execute();
            require(!follow.isActive(), "Old Follow task did not stop");
            require(follower.getMovementTarget() == otherTarget
                    && follower.getInteractionTarget() == otherTarget,
                    "Old Follow task interrupted a newer action");

            // A target on another Castle Wars floor cannot be followed.
            follower.setInteractionTarget(target);
            follower.setMovementTarget(target);
            FollowPlayerTask otherFloor = new FollowPlayerTask(null, 1, target, follower,
                    follower.nextActionSequence());
            target.getPosition().setPlane(1);
            otherFloor.execute();
            require(!otherFloor.isActive(), "Cross-plane Follow did not end");
            require(follower.getMovementTarget() == null
                    && follower.getInteractionTarget() == null,
                    "Cross-plane Follow left a stale target");

            // Likewise, disappearing from the world ends following safely.
            target.getPosition().setPlane(0);
            follower.setInteractionTarget(target);
            follower.setMovementTarget(target);
            FollowPlayerTask loggedOut = new FollowPlayerTask(null, 1, target, follower,
                    follower.nextActionSequence());
            World.getPlayers()[target.getIndex()] = null;
            loggedOut.execute();
            require(!loggedOut.isActive() && follower.getMovementTarget() == null,
                    "Follow did not end when the target logged out");
            System.out.println("Follow-player regressions passed");
        } finally {
            World.getPlayers()[follower.getIndex()] = null;
            World.getPlayers()[target.getIndex()] = null;
            World.getPlayers()[otherTarget.getIndex()] = null;
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
