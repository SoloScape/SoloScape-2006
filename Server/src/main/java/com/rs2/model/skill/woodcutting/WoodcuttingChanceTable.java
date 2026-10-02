package com.rs2.model.skill.woodcutting;

import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.util.GameUtil;

/** Explicit tree/axe success markers used by the classic 4-tick Woodcutting roll. */
public final class WoodcuttingChanceTable {
    private static final int[][] LOW = {
        {64, 96, 128, 144, 160, 192, 224, 240},
        {32, 48, 64, 72, 80, 96, 112, 120},
        {16, 24, 32, 36, 40, 48, 56, 60},
        {15, 23, 31, 35, 39, 47, 55, 60},
        {8, 12, 16, 18, 20, 24, 28, 30},
        {18, 28, 36, 42, 46, 59, 64, 67},
        {8, 12, 16, 18, 20, 25, 29, 34},
        {4, 6, 8, 9, 10, 12, 14, 15},
        {2, 3, 4, 5, 5, 6, 7, 7}
    };

    private static final int[][] HIGH = {
        {200, 300, 400, 450, 500, 600, 700, 750},
        {100, 150, 200, 225, 250, 300, 350, 375},
        {50, 75, 100, 112, 125, 150, 175, 187},
        {46, 70, 93, 102, 117, 140, 164, 190},
        {25, 37, 50, 56, 62, 75, 87, 93},
        {26, 40, 54, 57, 68, 81, 94, 101},
        {25, 38, 50, 54, 63, 75, 88, 94},
        {12, 19, 25, 28, 31, 37, 44, 47},
        {6, 9, 12, 13, 15, 18, 21, 22}
    };

    private WoodcuttingChanceTable() {
    }

    public static boolean roll(TreeDefinition tree, GatheringToolDefinition axe, int level) {
        int row = row(tree);
        int column = axeColumn(axe);
        if (row < 0 || column < 0) {
            return GameUtil.rollLevelScaledChance(tree.getCutChanceLow(), tree.getCutChanceHigh(),
                    level, axe.getToolSpeed());
        }
        return GameUtil.rollLevelScaledChance(LOW[row][column], HIGH[row][column], level);
    }

    static int low(TreeDefinition tree, GatheringToolDefinition axe) {
        int row = row(tree);
        int column = axeColumn(axe);
        return row < 0 || column < 0 ? -1 : LOW[row][column];
    }

    static int high(TreeDefinition tree, GatheringToolDefinition axe) {
        int row = row(tree);
        int column = axeColumn(axe);
        return row < 0 || column < 0 ? -1 : HIGH[row][column];
    }

    private static int row(TreeDefinition tree) {
        switch (tree) {
            case TREE:
            case ACHEY_TREE: return 0;
            case OAK: return 1;
            case WILLOW: return 2;
            case TEAK: return 3;
            case MAPLE: return 4;
            case HOLLOW_TREE: return 5;
            case MAHOGANY: return 6;
            case YEW: return 7;
            case MAGIC: return 8;
            default: return -1;
        }
    }

    private static int axeColumn(GatheringToolDefinition axe) {
        switch (axe) {
            case BRONZE_AXE: return 0;
            case IRON_AXE: return 1;
            case STEEL_AXE: return 2;
            case BLACK_AXE: return 3;
            case MITHRIL_AXE: return 4;
            case ADAMANT_AXE: return 5;
            case RUNE_AXE: return 6;
            case DRAGON_AXE: return 7;
            default: return -1;
        }
    }
}
