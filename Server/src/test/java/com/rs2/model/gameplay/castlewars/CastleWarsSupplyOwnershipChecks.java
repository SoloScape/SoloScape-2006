package com.rs2.model.gameplay.castlewars;

import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;

/** Supply cleanup must remove issued quantities without consuming player-owned items. */
public final class CastleWarsSupplyOwnershipChecks {
    public static void main(String[] args) {
        Player player = new Player(null);
        int pickaxe = CastleWarsEngineeringManager.BRONZE_PICKAXE_ID;
        int rock = CastleWarsEngineeringManager.ROCK_ITEM_ID;
        int tinderbox = CastleWarsEngineeringManager.TINDERBOX_ITEM_ID;
        int emptyBucket = CastleWarsEngineeringManager.EMPTY_BUCKET_ITEM_ID;
        int waterBucket = CastleWarsEngineeringManager.BUCKET_OF_WATER_ITEM_ID;

        require(player.getInventoryManager().addItemPartial(new ItemStack(pickaxe, 1)) == 1,
                "Could not stage an owned pickaxe");
        require(player.getInventoryManager().addItemPartial(new ItemStack(tinderbox, 1)) == 1,
                "Could not stage an owned tinderbox");
        require(player.getInventoryManager().addItemPartial(new ItemStack(emptyBucket, 1)) == 1,
                "Could not stage an owned bucket");
        require(player.getInventoryManager().addItemPartial(new ItemStack(waterBucket, 1)) == 1,
                "Could not stage an owned water bucket");

        require(CastleWarsEngineeringManager.issueSupply(player, pickaxe, 2) == 2,
                "Pickaxes were not issued");
        require(CastleWarsEngineeringManager.issueSupply(player, rock, 3) == 3,
                "Rocks were not issued");
        require(player.getInventoryManager().removeItem(new ItemStack(pickaxe, 1)),
                "Could not consume one pickaxe");
        require(player.getInventoryManager().removeItem(new ItemStack(rock, 1)),
                "Could not consume one rock");

        CastleWarsEngineeringManager.cleanupPlayerSupplies(player);
        requireAmount(player, pickaxe, 1);
        requireAmount(player, rock, 0);
        requireAmount(player, tinderbox, 1);
        requireAmount(player, emptyBucket, 1);
        requireAmount(player, waterBucket, 1);

        // Death cleanup is idempotent, and a later round must start fresh.
        CastleWarsEngineeringManager.cleanupPlayerSupplies(player);
        requireAmount(player, pickaxe, 1);
        require(CastleWarsEngineeringManager.issueSupply(player, pickaxe, 1) == 1,
                "Could not grant supplies for another life");
        CastleWarsEngineeringManager.cleanupPlayerSupplies(player);
        requireAmount(player, pickaxe, 1);

        Player fullInventory = new Player(null);
        require(fullInventory.getInventoryManager().addItemPartial(new ItemStack(pickaxe, 27)) == 27,
                "Could not fill the inventory");
        require(CastleWarsEngineeringManager.issueSupply(fullInventory, pickaxe, 5) == 1,
                "Should track only the amount actually issued");
        CastleWarsEngineeringManager.cleanupPlayerSupplies(fullInventory);
        requireAmount(fullInventory, pickaxe, 27);

        Player secondPlayer = new Player(null);
        require(secondPlayer.getInventoryManager().addItemPartial(new ItemStack(pickaxe, 1)) == 1,
                "Could not stage second player's item");
        require(CastleWarsEngineeringManager.issueSupply(player, rock, 1) == 1,
                "Could not issue another rock");
        CastleWarsEngineeringManager.cleanupPlayerSupplies(secondPlayer);
        requireAmount(secondPlayer, pickaxe, 1);
        requireAmount(player, rock, 1);
        CastleWarsEngineeringManager.cleanupPlayerSupplies(player);
        requireAmount(player, rock, 0);

        System.out.println("Castle Wars issued supply ownership checks passed");
    }

    private static void requireAmount(Player player, int itemId, int expected) {
        int actual = player.getInventoryManager().getItemAmount(itemId);
        require(actual == expected, "Item " + itemId + ": expected " + expected + ", got " + actual);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
