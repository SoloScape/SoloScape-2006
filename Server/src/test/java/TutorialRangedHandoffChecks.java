import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.cache.js5.Interfaces;
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

/** Exercises the ranged gift and replacement Continue paths through real packets. */
public final class TutorialRangedHandoffChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        // Dialogue tests need NPC identities only, not the combat-animation data.
        World.getNpcDefinitions()[944] = NpcDefinition.createFallback(944);
        World.getNpcDefinitions()[950] = NpcDefinition.createFallback(950);
        for (int i = 0; i < 2; i++) {
            Npc npc = new Npc(i == 0 ? 944 : 950);
            npc.setIndex(i);
            npc.setPosition(new Position(3105, 9508, 0));
            World.getNpcs()[i] = npc;
        }
        for (int child : new int[] {0, 4}) {
            Interfaces.Component icon = Interfaces.forId(102, child);
            require(icon.x + icon.width <= Interfaces.forId(102, 1).x
                    && icon.x + icon.width <= Interfaces.forId(102, 2).x,
                    "Gift icon is not left of the text");
        }
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress());
                client.setSoTimeout(100);
                try (SocketChannel transport = listener.accept()) {
                    Player player = new Player(null);
                    player.setUsername("rangedtest");
                    socketField.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setPosition(new Position(3105, 9508, 0));
                    player.setQuestState(0, 48);
                    player.getInventoryManager().addItem(new ItemStack(1277, 1));
                    player.getInventoryManager().addItem(new ItemStack(1171, 1));
                    require(DialogueManager.startDialogue(player, 944), "Instructor dialogue missing");
                    drain(client);
                    click(player, nativeClient, 64, 3);
                    click(player, nativeClient, 243, 5);
                    click(player, nativeClient, 244, 6);
                    verifyGift(player, drain(client));
                    require(player.getQuestState(0) == 48, "Stage advanced before Continue");
                    click(player, nativeClient, 102, 3);
                    verifyInstructions(player, drain(client));

                    for (int missing : new int[] {0, 882, 841}) {
                        if (missing == 0 || missing == 882)
                            player.getInventoryManager().removeItem(new ItemStack(882, 50));
                        if (missing == 0 || missing == 841)
                            player.getInventoryManager().removeItem(new ItemStack(841, 1));
                        drain(client);
                        require(DialogueManager.startDialogue(player, 944), "Replacement dialogue missing");
                        String packets = drain(client);
                        if (missing == 0) verifyGift(player, packets);
                        else {
                            require(player.getOpenInterfaceId() == 306, "Single replacement overwritten");
                            require(!player.getDialogueManager().isDialogueInactive(), "Replacement inactive");
                            require(packets.contains(missing == 882 ? "@blu@Bronze arrows!"
                                    : "@blu@Shortbow!"), "Replacement text incorrect");
                        }
                        click(player, nativeClient, missing == 0 ? 102 : 249, missing == 0 ? 3 : 2);
                        verifyInstructions(player, drain(client));
                    }
                }
            }
        }
        System.out.println("Tutorial ranged hand-off checks passed (blue text, left icons, both Continue handlers, all replacements).");
        System.exit(0);
    }

    private static void verifyGift(Player player, String packets) throws Exception {
        require(player.getOpenInterfaceId() == 4950, "Gift dialogue overwritten");
        require(!player.getDialogueManager().isDialogueInactive(), "Gift Continue inactive");
        require(packets.contains("The Combat Guide gives you some @blu@Bronze arrows@bla@ and")
                && packets.contains("a @blu@Shortbow!"), "Gift wording or blue text incorrect");
        for (int[] icon : new int[][] {{0, 882}, {4, 841}}) {
            byte[] model = ByteBuffer.allocate(10).putInt(170)
                    .putInt((102 << 16) | icon[0]).putShort((short) icon[1]).array();
            require(packets.contains(new String(model, "ISO-8859-1")), "Missing gift icon");
        }
    }

    private static void verifyInstructions(Player player, String packets) {
        require(player.getQuestState(0) == 49 && player.getOpenInterfaceId() == 6179,
                "Continue stuck or advanced too far");
        require(packets.contains("Rat ranging."), "Ranging instructions missing");
        require(player.getInventoryManager().getItemAmount(841) == 1
                && player.getInventoryManager().getItemAmount(882) == 50, "Incorrect or duplicated gift");
    }

    private static void click(Player player, boolean nativeClient, int group, int child) {
        if (nativeClient) {
            ByteBuffer payload = ByteBuffer.allocate(6);
            payload.putShort((short) -1).putInt((group << 16) | child).flip();
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
            // Synchronous writes collected.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
