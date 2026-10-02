import com.rs2.ServerSettings;
import com.rs2.model.grandexchange.GrandExchangeCatalog;
import com.rs2.model.item.ItemContainer;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.GrandExchangeManager;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/** Sell selection must reject untradeable items with feedback and retain valid offer state. */
public final class GrandExchangeSellSelectionChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.instantGrandExchangePriceFluctuationEnabled = false;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(2000);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setQuestState(0, 1);
                ItemContainer inventory = player.getInventoryManager().getContainer();

                // A bird nest is a valid inventory item, but absent from the GE catalogue.
                require(!GrandExchangeCatalog.isExchangeable(5070), "Rejection fixture is tradeable");
                inventory.setItem(0, new ItemStack(5070));
                player.selectedGrandExchangeItemId = 436;
                player.selectedGrandExchangeQuantity = 7;
                player.selectedGrandExchangeUnitPrice = 123;
                GrandExchangeManager.selectSellOfferItem(player, 0, 5070, 1);
                requireSelection(player, 436, 7, 123);
                DataInputStream input = new DataInputStream(client.getInputStream());
                IsaacCipher cipher = new IsaacCipher(new int[4]);
                require(((input.readUnsignedByte() - cipher.nextInt()) & 255) == 157,
                        "Rejection did not send a game message");
                byte[] message = new byte[input.readUnsignedByte()];
                input.readFully(message);
                require(new String(message, 0, message.length - 1, StandardCharsets.ISO_8859_1)
                        .equals("That item cannot be traded on this Grand Exchange."), "Wrong rejection message");

                inventory.setItem(0, new ItemStack(995, 100));
                GrandExchangeManager.selectSellOfferItem(player, 0, 995, 1);
                inventory.setItem(0, null);
                GrandExchangeManager.selectSellOfferItem(player, 0, 436, 1);
                inventory.setItem(0, new ItemStack(436));
                GrandExchangeManager.selectSellOfferItem(player, 0, 438, 1);
                inventory.setItem(0, new ItemStack(436, 0));
                GrandExchangeManager.selectSellOfferItem(player, 0, 436, 1);
                requireSelection(player, 436, 7, 123);

                inventory.setItem(0, new ItemStack(436));
                GrandExchangeManager.selectSellOfferItem(player, 0, 436, 1);
                requireSelection(player, 436, 1, GrandExchangeManager.getGuidePrice(436));
                require(ItemDefinition.forId(437).isNote()
                        && ItemDefinition.forId(437).getUnnotedId() == 436, "Wrong noted fixture");
                inventory.setItem(0, new ItemStack(437, 12));
                GrandExchangeManager.selectSellOfferItem(player, 0, 437, 1);
                requireSelection(player, 436, 12, GrandExchangeManager.getGuidePrice(436));
                require(inventory.getItemAt(0).getId() == 437 && inventory.getItemAt(0).getAmount() == 12,
                        "Selection changed inventory");
            }
        }
        System.out.println("Grand Exchange sell selection checks passed (rejection feedback, invalid selections, normal and noted items).");
        System.exit(0);
    }

    private static void requireSelection(Player player, int id, int quantity, int price) {
        require(player.selectedGrandExchangeItemId == id
                && player.selectedGrandExchangeQuantity == quantity
                && player.selectedGrandExchangeUnitPrice == price, "Unexpected offer selection");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
