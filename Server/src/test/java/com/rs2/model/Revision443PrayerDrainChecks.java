package com.rs2.model;

import com.rs2.model.player.Player;

/**
 * Exercise the same tick path as revision-443 human players. Their main
 * Player.process() is intentionally bypassed; prayer still has to drain.
 */
public final class Revision443PrayerDrainChecks {
    public static void main(String[] args) {
        Player player = new Player(null);
        // Suppress network writes in the headless test while exercising the
        // actual revision-443 logic directly.
        player.isBot = true;
        player.setPosition(new Position(2441, 3090, 0));
        player.getSkillManager().setCurrentLevel(5, 20);

        int initialPoints = player.getSkillManager().getCurrentLevels()[5];
        player.getPrayerManager().togglePrayer(0); // Thick Skin, 3 drain units.
        require(player.getActivePrayers()[0], "Prayer failed to activate");
        require(player.prayerDrainRate == 3, "Prayer drain rate not set");

        int ticksPerPoint = player.getPrayerDrainThreshold() / player.prayerDrainRate + 2;
        require(ticksPerPoint >= 2 && ticksPerPoint < 10000, "Invalid prayer drain threshold");
        for (int tick = 0; tick < ticksPerPoint; tick++) {
            World.processRevision443Player(player);
        }
        int reducedPoints = player.getSkillManager().getCurrentLevels()[5];
        require(reducedPoints < initialPoints && reducedPoints > 0,
                "Active prayer did not drain through the revision-443 tick");

        player.getPrayerManager().togglePrayer(0); // Deactivate.
        require(!player.getActivePrayers()[0], "Prayer failed to deactivate");
        require(player.prayerDrainRate == 0, "Inactive prayer still drains");
        for (int tick = 0; tick < ticksPerPoint * 3; tick++) {
            World.processRevision443Player(player);
        }
        require(player.getSkillManager().getCurrentLevels()[5] == reducedPoints,
                "Prayer drained while all prayers were inactive");

        player.getSkillManager().setCurrentLevel(5, 2);
        player.getPrayerManager().togglePrayer(0);
        for (int tick = 0; tick < ticksPerPoint * 3; tick++) {
            World.processRevision443Player(player);
        }
        require(player.getSkillManager().getCurrentLevels()[5] == 0,
                "Prayer points did not reach zero");
        require(!player.getActivePrayers()[0] && player.prayerDrainRate == 0,
                "Prayers stayed active after running out of points");

        System.out.println("Revision 443 prayer drain checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
