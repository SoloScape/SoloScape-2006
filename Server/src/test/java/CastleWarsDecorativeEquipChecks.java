import com.rs2.ServerSettings;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.interaction.ItemActionRouter;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import com.rs2.model.quest.QuestDefinition;

/** Inventory action and equipment metadata audit for Lanthus's full reward range. */
public final class CastleWarsDecorativeEquipChecks {
    private static final int[][] REWARDS = {
        {4071, 0}, {4072, 5}, {4070, 7}, {4069, 4}, {4068, 3},
        {4506, 0}, {4507, 5}, {4505, 7}, {4504, 4}, {4503, 3},
        {4511, 0}, {4512, 5}, {4510, 7}, {4509, 4}, {4508, 3},
        {4516, 1}, {4515, 0}, {4514, 1}, {4513, 0}
    };

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        int mismatches = 0;
        for (int[] reward : REWARDS) {
            int id = reward[0], expectedSlot = reward[1];
            ItemDefinition def = ItemDefinition.forId(id);
            StringBuilder actions = new StringBuilder();
            for (int slot = 0; slot < 5; slot++) {
                actions.append(slot).append('=').append(def.getInventoryAction(slot)).append(' ');
            }
            System.out.println("LANTHUS-EQUIP id=" + id + " name=" + def.getName()
                    + " slot=" + def.getEquipmentSlot() + " expected=" + expectedSlot
                    + " actions=" + actions.toString());
            if (def.getEquipmentSlot() != expectedSlot) mismatches++;
        }
        if (mismatches != 0) {
            throw new AssertionError("Lanthus rewards with invalid equipment slots: " + mismatches);
        }
        for (int[] reward : REWARDS) {
            int itemId = reward[0];
            String expectedAction = reward[1] == 3 ? "Wield" : "Wear";
            ItemDefinition definition = ItemDefinition.forId(itemId);
            if (!expectedAction.equals(definition.getInventoryAction(1))
                    || ItemActionRouter.semanticOption(definition, 2) != 2) {
                throw new AssertionError("Lanthus action routing missing: " + itemId);
            }
        }
        checkWearAndRemove();
        System.out.println("Castle Wars Lanthus decorative equipment checks passed");
    }

    /** Exercise real equipment transfers, not just metadata, for all six pieces. */
    private static void checkWearAndRemove() throws Exception {
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            try (SocketChannel channel = listener.accept()) {
                Player player = new Player(null);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, channel);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.playerRights = 2; // Bypass unrelated membership requirements.
                player.setQuestState(0, 42); // Tutorial equipment lesson complete.
                int[][] items = {
                    {4069, 4}, {4070, 7}, {4504, 4},
                    {4505, 7}, {4509, 4}, {4510, 7}
                };
                for (int[] pair : items) {
                    int itemId = pair[0], slot = pair[1];
                    player.getInventoryManager().getContainer().setItem(0, new ItemStack(itemId));
                    player.getEquipmentManager().equipFromInventorySlot(0);
                    if (player.getEquipmentManager().getItemIdAtSlot(slot) != itemId) {
                        throw new AssertionError("Wear did not equip decorative reward " + itemId
                                + " into slot " + slot);
                    }
                    player.getEquipmentManager().unequipSlot(slot);
                    if (player.getInventoryManager().getItemAmount(itemId) != 1) {
                        throw new AssertionError("Removing decorative reward lost item " + itemId);
                    }
                    player.getInventoryManager().getContainer().setItem(0, null);
                }
            }
        }
    }
}
