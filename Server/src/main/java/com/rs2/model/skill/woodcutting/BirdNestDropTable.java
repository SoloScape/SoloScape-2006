package com.rs2.model.skill.woodcutting;

import com.rs2.model.player.Player;
import com.rs2.util.GameUtil;

/** Shared nest rolls for ordinary and farmed trees. */
public final class BirdNestDropTable {
    private static final int STRUNG_RABBIT_FOOT = 10132;

    private BirdNestDropTable() {
    }

    public static int rollNest(Player player) {
        return rollNest(isWearingRabbitFoot(player));
    }

    public static boolean shouldDropNest(Player player, int baseRollBound) {
        // The archived Knowledge Base says wearing the necklace increases nest frequency:
        // https://2011.rs/kb/woodcutting_extra_features
        // Its exact historical multiplier is unknown; use a 2x gameplay approximation.
        // Keep each caller's existing base chance (ordinary 1/256, farmed 1/257).
        return GameUtil.randomInt(baseRollBound) < (isWearingRabbitFoot(player) ? 2 : 1);
    }

    private static boolean isWearingRabbitFoot(Player player) {
        return player.getEquipmentManager().getItemIdAtSlot(2) == STRUNG_RABBIT_FOOT;
    }

    public static int rollNest(boolean wearingRabbitFoot) {
        // Mod Ash, 26 October 2018: 0=red, 1=blue, 2=green, 3..34=rings.
        // https://oldschool.runescape.wiki/w/Bird_nest
        // These OSRS weights are the best available approximation for 2006;
        // period observations establish rare eggs, but not exact percentages.
        // Preserve the existing contents approximation separately from nest frequency.
        int roll = GameUtil.randomInt(wearingRabbitFoot ? 95 : 100);
        if (roll < 3) {
            return 5070 + roll;
        }
        return roll < 35 ? 5074 : 5073;
    }
}
