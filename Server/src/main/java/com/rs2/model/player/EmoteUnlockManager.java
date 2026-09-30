package com.rs2.model.player;

/**
 * Persistent unlock tracking for emotes which are earned outside of the
 * default emote set. The bits are stored in the character file's previously
 * unused reservedSaveLong1 field.
 */
public final class EmoteUnlockManager {
    private static final int GOBLIN_BOW = 23;
    private static final int GOBLIN_SALUTE = 24;
    private static final int GLASS_BOX = 25;
    private static final int CLIMB_ROPE = 26;
    private static final int LEAN = 27;
    private static final int GLASS_WALL = 28;
    private static final int IDEA = 29;
    private static final int STAMP = 30;
    private static final int FLAP = 31;
    private static final int SLAP_HEAD = 32;
    private static final int ZOMBIE_WALK = 33;
    private static final int ZOMBIE_DANCE = 34;
    private static final int SCARED = 36;
    private static final int RABBIT_HOP = 37;

    private static final int[] LOCKED_EMOTE_CHILDREN = {
        GOBLIN_BOW, GOBLIN_SALUTE, GLASS_BOX, CLIMB_ROPE, LEAN, GLASS_WALL,
        IDEA, STAMP, FLAP, SLAP_HEAD, ZOMBIE_WALK, ZOMBIE_DANCE, SCARED,
        RABBIT_HOP
    };

    private static final String[] UNLOCK_HINTS = {
        "You need to unlock this emote during The Lost Tribe quest.",
        "You need to unlock this emote during The Lost Tribe quest.",
        "You need to unlock this emote from the Mime random event.",
        "You need to unlock this emote from the Mime random event.",
        "You need to unlock this emote from the Mime random event.",
        "You need to unlock this emote from the Mime random event.",
        "Search the Box of Health in the Stronghold of Security to unlock this emote.",
        "Search the Cradle of Life in the Stronghold of Security to unlock this emote.",
        "Search the Gift of Peace in the Stronghold of Security to unlock this emote.",
        "Search the Grain of Plenty in the Stronghold of Security to unlock this emote.",
        "You need to unlock this emote from the Gravedigger random event.",
        "You need to unlock this emote from the Gravedigger random event.",
        "You need to unlock this emote from the 2005 Halloween event.",
        "You need to unlock this emote from the 2006 Easter event."
    };

    private EmoteUnlockManager() {
    }

    public static boolean isLockedChild(int child) {
        for (int lockedChild : LOCKED_EMOTE_CHILDREN) {
            if (lockedChild == child) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUnlocked(Player player, int child) {
        int bit = bitForChild(child);
        return bit < 0 || (player.reservedSaveLong1 & (1L << bit)) != 0;
    }

    /** Call this from the associated quest/event/reward completion handler. */
    public static void unlock(Player player, int child) {
        int bit = bitForChild(child);
        if (bit >= 0) {
            player.reservedSaveLong1 |= 1L << bit;
        }
    }

    public static String getUnlockHint(int child) {
        for (int i = 0; i < LOCKED_EMOTE_CHILDREN.length; i++) {
            if (LOCKED_EMOTE_CHILDREN[i] == child) {
                return UNLOCK_HINTS[i];
            }
        }
        return null;
    }

    private static int bitForChild(int child) {
        for (int i = 0; i < LOCKED_EMOTE_CHILDREN.length; i++) {
            if (LOCKED_EMOTE_CHILDREN[i] == child) {
                return i;
            }
        }
        return -1;
    }
}
