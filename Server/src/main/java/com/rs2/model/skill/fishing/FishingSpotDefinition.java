package com.rs2.model.skill.fishing;

import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.NpcDefinition;

public enum FishingSpotDefinition {
    SMALL_NET(new int[]{316}, 303, -1, 621, new int[]{1, 15}, new int[]{317, 321}, new double[]{10.0, 40.0}, new int[]{48, 24}, new int[]{256, 128}),
    BAIT(new int[]{316}, 307, 313, 622, new int[]{5, 10}, new int[]{327, 345}, new double[]{20.0, 30.0}, new int[]{32, 24}, new int[]{192, 128}),
    FLY_FISHING(new int[]{309}, 309, 314, 622, new int[]{20, 30}, new int[]{335, 331}, new double[]{50.0, 70.0}, new int[]{32, 16}, new int[]{192, 96}),
    RIVER_BAIT(new int[]{309}, 307, 313, 622, new int[]{25}, new int[]{349}, new double[]{60.0}, new int[]{16}, new int[]{96}),
    LOBSTER_POT(new int[]{312}, 301, -1, 619, new int[]{40}, new int[]{377}, new double[]{90.0}, new int[]{6}, new int[]{95}),
    HARPOON(new int[]{312}, 311, -1, 618, new int[]{35, 50}, new int[]{359, 371}, new double[]{80.0, 100.0}, new int[]{8, 4}, new int[]{64, 48}),
    BIG_NET(new int[]{313}, 305, -1, 620, new int[]{16, 16, 16, 16, 16, 16, 23, 46}, new int[]{353, 405, 1059, 407, 401, 1061, 341, 363}, new double[]{20.0, 10.0, 1.0, 10.0, 1.0, 1.0, 45.0, 100.0}, new int[]{5, 1, 5, 3, 10, 10, 4, 3}, new int[]{65, 2, 5, 7, 10, 10, 55, 40}),
    SHARK_HARPOON(new int[]{313}, 311, -1, 618, new int[]{76}, new int[]{383}, new double[]{110.0}, new int[]{3}, new int[]{40}),
    LAVA_EEL(new int[]{800}, 1585, 313, 622, new int[]{53}, new int[]{2148}, new double[]{0.0}, new int[]{16}, new int[]{96}),
    MONKFISH(new int[]{1174}, 303, -1, 621, new int[]{62}, new int[]{7944}, new double[]{120.0}, new int[]{48}, new int[]{90});

    private final int[] spotNpcIds;
    private final ItemStack toolItem;
    private final int[] requiredLevels;
    private final int[] chanceLowValues;
    private final int[] chanceHighValues;
    private final ItemStack[] catchItems;
    private final double[] experienceRewards;
    private final int animationId;
    private final ItemStack baitItem;

    private FishingSpotDefinition(int[] spotNpcIds, int value4, int baitItem, int animationId, int[] requiredLevels, int[] integerValues32, double[] doubleValues, int[] chanceLowValues, int[] chanceHighValues) {
        this.spotNpcIds = spotNpcIds;
        this.toolItem = new ItemStack(value4);
        this.baitItem = baitItem == -1 ? null : new ItemStack(baitItem);
        this.animationId = animationId;
        this.requiredLevels = requiredLevels;
        ItemStack[] itemStackArray = new ItemStack[integerValues32.length];
        int index = 0;
        while (index < integerValues32.length) {
            itemStackArray[index] = new ItemStack(integerValues32[index]);
            ++index;
        }
        this.catchItems = itemStackArray;
        this.experienceRewards = doubleValues;
        this.chanceLowValues = chanceLowValues;
        this.chanceHighValues = chanceHighValues;
    }

    public final ItemStack getBaitItem() {
        return this.baitItem;
    }

    public final ItemStack[] getCatchItems() {
        return this.catchItems;
    }

    public final int getAnimationId() {
        return this.animationId;
    }

    public final int[] getRequiredLevels() {
        return this.requiredLevels;
    }

    public final int[] getChanceLowValues() {
        return this.chanceLowValues;
    }

    public final int[] getChanceHighValues() {
        return this.chanceHighValues;
    }

    public final double[] getExperienceRewards() {
        return this.experienceRewards;
    }

    public final ItemStack getToolItem() {
        return this.toolItem;
    }

    public final int[] getSpotNpcIds() {
        return this.spotNpcIds;
    }

    public static FishingSpotDefinition forNpcIdAndOption(int npcId, int value2) {
        if (value2 == 1) {
            switch (npcId) {
                case 309: {
                    return FLY_FISHING;
                }
                case 312: {
                    return LOBSTER_POT;
                }
                case 316: {
                    return SMALL_NET;
                }
                case 313: {
                    return BIG_NET;
                }
                case 800: {
                    return LAVA_EEL;
                }
                case 1174: {
                    return MONKFISH;
                }
            }
        } else {
            switch (npcId) {
                case 309: {
                    return RIVER_BAIT;
                }
                case 312: {
                    return HARPOON;
                }
                case 316: {
                    return BAIT;
                }
                case 313: {
                    return SHARK_HARPOON;
                }
            }
        }
        return null;
    }

    public static FishingSpotDefinition forNpcIdAndAction(int npcId, String action) {
        if (action == null) return null;
        FishingWhirlpool whirlpool = FishingWhirlpool.forWhirlpoolNpcId(npcId);
        if (whirlpool != null) npcId = whirlpool.getSourceNpcIds()[0];
        for (int option = 1; option <= 2; option++) {
            FishingSpotDefinition definition = forNpcIdAndOption(npcId, option);
            if (definition == null) continue;
            String label;
            switch (definition) {
                case SMALL_NET: case BIG_NET: case MONKFISH: label = "Net"; break;
                case BAIT: case RIVER_BAIT: case LAVA_EEL: label = "Bait"; break;
                case FLY_FISHING: label = "Lure"; break;
                case LOBSTER_POT: label = "Cage"; break;
                default: label = "Harpoon";
            }
            if (label.equalsIgnoreCase(action)) return definition;
        }
        // 443 has regional variants of the same spots. Their paired action names
        // identify the method; a lone Net/Harpoon is ambiguous and stays unmapped.
        NpcDefinition npc = NpcDefinition.forId(npcId);
        if (!"Fishing spot".equalsIgnoreCase(npc.getName())) return null;
        if (hasAction(npc, "Net") && hasAction(npc, "Bait")) {
            if ("Net".equalsIgnoreCase(action)) return SMALL_NET;
            if ("Bait".equalsIgnoreCase(action)) return BAIT;
        } else if (hasAction(npc, "Lure") && hasAction(npc, "Bait")) {
            if ("Lure".equalsIgnoreCase(action)) return FLY_FISHING;
            if ("Bait".equalsIgnoreCase(action)) return RIVER_BAIT;
        } else if (hasAction(npc, "Cage") && hasAction(npc, "Harpoon")) {
            if ("Cage".equalsIgnoreCase(action)) return LOBSTER_POT;
            if ("Harpoon".equalsIgnoreCase(action)) return HARPOON;
        } else if (hasAction(npc, "Net") && hasAction(npc, "Harpoon")) {
            if ("Net".equalsIgnoreCase(action)) return BIG_NET;
            if ("Harpoon".equalsIgnoreCase(action)) return SHARK_HARPOON;
        }
        return null;
    }

    private static boolean hasAction(NpcDefinition npc, String action) {
        for (int slot = 0; slot < 5; slot++) {
            if (action.equalsIgnoreCase(npc.getAction(slot))) return true;
        }
        return false;
    }
}

