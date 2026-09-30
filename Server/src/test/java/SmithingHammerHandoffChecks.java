import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;

/** First hammer hand-off stays visible until Continue, using both client handlers. */
public final class SmithingHammerHandoffChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        Method nativeContinue = InterfaceActionPacketHandler.class.getDeclaredMethod(
                "handleDialogueContinue", Player.class, int.class);
        nativeContinue.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            try (SocketChannel transport = SocketChannel.open()) {
                transport.close();
                Player player = new Player(null);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3080, 9505, 0));
                player.getInventoryManager().addOrDropItem(new ItemStack(1265, 1));
                player.setQuestState(0, 36);
                require(DialogueManager.continueDialogue(player, 948, 3, 0), "Hand-off unhandled");
                require(player.getOpenInterfaceId() == 306, "Hammer message overwritten");
                require(player.getInventoryManager().getItemAmount(2347) == 1, "Hammer not given once");
                require(player.getQuestState(0) == 36, "Smithing instruction advanced before Continue");
                require(!player.getDialogueManager().isDialogueInactive(), "Hand-off cannot be continued");
                if (nativeClient) {
                    require(Boolean.TRUE.equals(nativeContinue.invoke(new InterfaceActionPacketHandler(),
                            player, (249 << 16) | 2)), "Native item Continue unhandled");
                } else {
                    new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
                }
                require(player.getQuestState(0) == 37, "Continue did not advance to smithing");
                require(player.getOpenInterfaceId() == 6179, "Smithing instruction missing after Continue");
                require(player.getInventoryManager().getItemAmount(2347) == 1, "Continue gave another hammer");
                player.getInventoryManager().removeItem(new ItemStack(2347, 1));
                require(DialogueManager.startDialogue(player, 948), "Replacement hand-off unhandled");
                require(player.getOpenInterfaceId() == 306, "Replacement message missing");
                require(player.getInventoryManager().getItemAmount(2347) == 1, "Replacement missing");
                require(player.getQuestState(0) == 37, "Replacement advanced tutorial");
            }
        }
        System.out.println("Smithing hammer hand-off checks passed (first gift, both Continue handlers, replacement).");
        System.exit(0);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
