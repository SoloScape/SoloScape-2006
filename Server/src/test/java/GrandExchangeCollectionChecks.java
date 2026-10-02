import com.rs2.ServerSettings;
import com.rs2.model.grandexchange.GrandExchangeCatalog;
import com.rs2.model.item.ItemContainer;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.GrandExchangeManager;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Original GE collection must use notes only when the entire quantity cannot fit. */
public final class GrandExchangeCollectionChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                ItemContainer inventory = player.getInventoryManager().getContainer();
                require(ItemDefinition.forId(436).hasNote(), "Ore fixture has no note");
                check(player, 436, 1, 1, false, 436, 1, 0);
                check(player, 436, 5, 5, false, 436, 5, 0);
                check(player, 436, 28, 28, false, 436, 28, 0);
                check(player, 436, 6, 5, false, 437, 6, 0);
                check(player, 436, 29, 28, false, 437, 29, 0);
                check(player, 436, 1, 0, false, 436, 0, 1);
                // Cancelled sell offers return the same item form as purchased items.
                check(player, 436, 4, 4, true, 436, 4, 0);
                check(player, 436, 5, 4, true, 437, 5, 0);
                check(player, 556, 1000, 1, false, 556, 1000, 0);
                check(player, 995, 1000, 1, true, 995, 1000, 0);
                reset(player, 556, 1000, 0, false);
                inventory.setItem(0, new ItemStack(556, 10));
                GrandExchangeManager.collectOfferItem(player, 0, 556, 1);
                require(inventory.getItemAmount(556) == 1010 && player.grandExchangePrimaryCollectAmounts[0] == 0,
                        "Existing stack could not be collected into a full inventory");
                reset(player, 436, 20, 0, false);
                inventory.setItem(0, new ItemStack(437, 10));
                GrandExchangeManager.collectOfferItem(player, 0, 436, 1);
                require(inventory.getItemAmount(437) == 30, "Existing note stack was not reused");
                reset(player, 556, 1, 0, false);
                inventory.setItem(0, new ItemStack(556, Integer.MAX_VALUE));
                GrandExchangeManager.collectOfferItem(player, 0, 556, 1);
                require(inventory.getItemAmount(556) == Integer.MAX_VALUE
                        && player.grandExchangePrimaryCollectAmounts[0] == 1, "Overflow lost collection items");
                // Coins cannot be noted; empty collection clicks must be harmless.
                check(player, 995, 0, 1, true, 995, 0, 0);
                int unnotableId = -1;
                for (int id = 0; id <= 11883; id++) {
                    ItemDefinition definition = ItemDefinition.forId(id);
                    if (GrandExchangeCatalog.isExchangeable(id) && !definition.isStackable()
                            && !definition.hasNote()) {
                        unnotableId = id;
                        break;
                    }
                }
                require(unnotableId >= 0, "No unnotable tradeable fixture found");
                check(player, unnotableId, 5, 2, false, unnotableId, 2, 3);
                check(player, unnotableId, 5, 0, false, unnotableId, 0, 5);
                // A completed offer clears only after its collection has been emptied.
                reset(player, 436, 2, 2, false);
                player.grandExchangeCompletedQuantities[0] = player.grandExchangeQuantities[0];
                GrandExchangeManager.collectOfferItem(player, 0, 436, 1);
                require(player.grandExchangeItemIds[0] == -1 && player.grandExchangeQuantities[0] == 0,
                        "Completed offer did not clear after collection");
            }
        }
        System.out.println("Grand Exchange collection checks passed.");
        System.exit(0);
    }

    private static void reset(Player player, int itemId, int amount, int freeSlots, boolean sell) {
        ItemContainer inventory = player.getInventoryManager().getContainer();
        for (int i = 0; i < 28; i++) inventory.setItem(i, i < 28 - freeSlots ? new ItemStack(438) : null);
        player.selectedGrandExchangeSlot = 0;
        player.grandExchangeSellOfferFlags[0] = sell;
        player.grandExchangeItemIds[0] = sell && itemId == 995 ? 436 : itemId;
        player.grandExchangeQuantities[0] = amount + 1;
        player.grandExchangeCompletedQuantities[0] = amount;
        player.grandExchangeCancelledFlags[0] = false;
        player.grandExchangePrimaryCollectAmounts[0] = sell && itemId != 995 ? 0 : amount;
        player.grandExchangeSecondaryCollectAmounts[0] = sell && itemId != 995 ? amount : 0;
    }

    private static void check(Player player, int itemId, int amount, int freeSlots, boolean sell,
                              int collectedId, int collectedAmount, int remaining) {
        reset(player, itemId, amount, freeSlots, sell);
        int collectionSlot = sell && itemId != 995 ? 1 : 0;
        GrandExchangeManager.collectOfferItem(player, collectionSlot, itemId, 1);
        require(player.getInventoryManager().getContainer().getItemAmount(collectedId) == collectedAmount,
                "Wrong item form or amount for " + itemId + " x " + amount + " with " + freeSlots + " free slots");
        require((collectionSlot == 0 ? player.grandExchangePrimaryCollectAmounts[0]
                : player.grandExchangeSecondaryCollectAmounts[0]) == remaining, "Wrong collection remainder");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
