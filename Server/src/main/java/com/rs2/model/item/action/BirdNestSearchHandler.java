package com.rs2.model.item.action;

import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.util.GameUtil;

public final class BirdNestSearchHandler {
    // Late-2006 seed-nest weighting, normalized from the historical 992-weight table.
    private static final int[] seedNestRewards = new int[]{5312, 5283, 5313, 5284, 5285, 5286, 5314, 5287, 5288, 5315, 5289, 5290, 5317, 5316};
    private static final int[] seedNestWeights = new int[]{214, 170, 135, 108, 85, 68, 54, 42, 34, 27, 22, 17, 11, 5};
    private static final int seedNestWeightTotal = 992;
    private static int[] commonRingNestRewards = new int[]{1635, 1637};
    private static int[] uncommonRingNestRewards = new int[]{1639};
    private static int[] rareRingNestRewards = new int[]{1641};
    private static int[] veryRareRingNestRewards = new int[]{1643};

    private static int rollSeedNestReward() {
        int roll = GameUtil.randomInt(seedNestWeightTotal);
        int cumulativeWeight = 0;
        for (int index = 0; index < seedNestRewards.length; ++index) {
            cumulativeWeight += seedNestWeights[index];
            if (roll < cumulativeWeight) {
                return seedNestRewards[index];
            }
        }
        return seedNestRewards[seedNestRewards.length - 1];
    }

    public static boolean searchNest(Player player, int value5) {
        int[] integerValues;
        int[] integerValues2;
        Object value2;
        int[] integerValues3;
        switch (value5) {
            case 5070: {
                player.getInventoryManager().removeItem(new ItemStack(value5));
                player.getInventoryManager().addOrDropItem(new ItemStack(5076));
                player.getInventoryManager().addOrDropItem(new ItemStack(5075));
                return true;
            }
            case 5071: {
                player.getInventoryManager().removeItem(new ItemStack(value5));
                player.getInventoryManager().addOrDropItem(new ItemStack(5078));
                player.getInventoryManager().addOrDropItem(new ItemStack(5075));
                return true;
            }
            case 5072: {
                player.getInventoryManager().removeItem(new ItemStack(value5));
                player.getInventoryManager().addOrDropItem(new ItemStack(5077));
                player.getInventoryManager().addOrDropItem(new ItemStack(5075));
                return true;
            }
            case 5073: {
                int value4 = rollSeedNestReward();
                player.packetSender.sendGameMessage("You search the nest...and find something in it!");
                player.getInventoryManager().removeItem(new ItemStack(value5));
                player.getInventoryManager().addOrDropItem(new ItemStack(value4));
                player.getInventoryManager().addOrDropItem(new ItemStack(5075));
                return true;
            }
            case 5074: {
                integerValues3 = commonRingNestRewards;
                value2 = uncommonRingNestRewards;
                integerValues2 = rareRingNestRewards;
                integerValues = veryRareRingNestRewards;
                break;
            }
            default: {
                return false;
            }
        }
        int value3 = GameUtil.randomInclusive(100);
        int value4 = value3 <= 60 ? integerValues3[GameUtil.randomInclusive(integerValues3.length - 1)] : (value3 <= 80 ? ((int[])value2)[GameUtil.randomInclusive(((int[])value2).length - 1)] : (value3 <= 95 ? integerValues2[GameUtil.randomInclusive(integerValues2.length - 1)] : integerValues[GameUtil.randomInclusive(0)]));
        Player player2 = player;
        value2 = player2;
        player2.packetSender.sendGameMessage("You search the nest...and find something in it!");
        player.getInventoryManager().removeItem(new ItemStack(value5));
        player.getInventoryManager().addOrDropItem(new ItemStack(value4));
        player.getInventoryManager().addOrDropItem(new ItemStack(5075));
        return true;
    }
}

