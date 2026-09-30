import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Verifies the combat gift remains visible, sends both models and accepts Continue. */
public final class TutorialCombatHandoffChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        Npc instructor = new Npc(944);
        instructor.setIndex(0);
        instructor.setPosition(new Position(3105, 9508, 0));
        World.getNpcs()[0] = instructor;
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress());
                client.setSoTimeout(100);
                try (SocketChannel transport = listener.accept()) {
                    Player player = new Player(null);
                    socketField.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setPosition(new Position(3105, 9508, 0));
                    player.setQuestState(0, 43);
                    require(DialogueManager.startDialogue(player, 944), "Instructor dialogue missing");
                    drain(client);
                    continueDialogue(player, nativeClient, (242 << 16) | 4);
                    verifyGift(player, drain(client));
                    require(player.getQuestState(0) == 43, "Instructions advanced before Continue");
                    continueDialogue(player, nativeClient, (102 << 16) | 3);
                    require(player.getQuestState(0) == 44, "Continue did not advance tutorial");
                    require(player.getOpenInterfaceId() == 6179, "Continue stuck on item dialogue");
                    require(drain(client).contains("Unequipping items."), "Equipment instructions not sent");
                    require(player.getInventoryManager().getItemAmount(1277) == 1
                            && player.getInventoryManager().getItemAmount(1171) == 1,
                            "Continue duplicated equipment");

                    player.getInventoryManager().removeItem(new ItemStack(1277, 1));
                    player.getInventoryManager().removeItem(new ItemStack(1171, 1));
                    drain(client);
                    require(DialogueManager.startDialogue(player, 944), "Replacement gift missing");
                    verifyGift(player, drain(client));
                    continueDialogue(player, nativeClient, (102 << 16) | 3);
                    require(player.getQuestState(0) == 44, "Replacement Continue advanced tutorial");
                    require(player.getOpenInterfaceId() == 6179, "Replacement Continue stuck");
                    require(drain(client).contains("Unequipping items."), "Replacement instructions missing");
                }
            }
        }
        System.out.println("Tutorial combat hand-off checks passed (text, blue names, both icons, both Continue handlers, replacement).");
        System.exit(0);
    }

    private static void verifyGift(Player player, String packets) throws Exception {
        require(player.getOpenInterfaceId() == 4950, "Gift dialogue overwritten");
        require(!player.getDialogueManager().isDialogueInactive(), "Gift Continue is inactive");
        require(packets.contains("The Combat Guide gives you a @blu@Bronze sword@bla@ and a")
                && packets.contains("@blu@Wooden Shield!"), "Gift text or blue names incorrect");
        for (int[] icon : new int[][] {{0, 1277}, {4, 1171}}) {
            byte[] model = ByteBuffer.allocate(10).putInt(170)
                    .putInt((102 << 16) | icon[0]).putShort((short) icon[1]).array();
            require(packets.contains(new String(model, "ISO-8859-1")), "Missing item icon " + icon[1]);
        }
        require(player.getInventoryManager().getItemAmount(1277) == 1
                && player.getInventoryManager().getItemAmount(1171) == 1, "Gift items incorrect");
    }

    private static void continueDialogue(Player player, boolean nativeClient, int widget) {
        if (nativeClient) {
            ByteBuffer payload = ByteBuffer.allocate(6);
            payload.putShort((short) -1).putInt(widget).flip();
            new InterfaceActionPacketHandler().handle(player, new IncomingPacket(ClientPackets.WIDGET_SELECT,
                    6, PacketBuffer.wrapReader(payload)));
        } else {
            new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
        }
    }

    private static String drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) bytes.write(buffer, 0, count);
        } catch (SocketTimeoutException drained) {
            // All synchronous writes collected.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
