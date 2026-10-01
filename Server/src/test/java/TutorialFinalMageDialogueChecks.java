import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.item.ItemDefinition;
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

/** Final Terrova directions must show the Guide icon and advance on Continue. */
public final class TutorialFinalMageDialogueChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Npc mage = new Npc(946);
        mage.setIndex(0);
        mage.setPosition(new Position(3141, 3088, 0));
        World.getNpcs()[0] = mage;
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(100);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                socketField.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3141, 3088, 0));
                player.setQuestState(0, 67);

                require(DialogueManager.continueDialogue(player, 946, 1, 0), "Final Terrova opening missing");
                drain(client);
                require(DialogueManager.continueDialogue(player, 946, 2, 0), "Mainland question missing");
                drain(client);
                require(player.getDialogueManager().handleOptionButton(2462), "No option was not handled");
                String declined = drain(client);
                require(player.getOpenInterfaceId() == 6179, "No did not restore tutorial instructions");
                require(declined.contains("You have almost completed the tutorial!")
                        && declined.contains("with Terrova and he'll teleport you to Lumbridge Castle."),
                        "No did not restore the final tutorial instruction text");
                require(player.getDialogueManager().isDialogueInactive(), "No left the conversation active");
                require(player.getQuestState(0) == 67, "No advanced the tutorial");

                require(DialogueManager.continueDialogue(player, 946, 1, 0), "Terrova could not be reopened");
                drain(client);
                clickTwoLineContinue(player);
                drain(client);
                require(player.getDialogueManager().handleOptionButton(2461), "Yes option was not handled");
                require(drain(client).contains("When you get to the mainland you will find yourself in"),
                        "Yes did not continue the mainland directions");

                require(DialogueManager.continueDialogue(player, 946, 4, 0), "White-beard page missing");
                require(drain(client).contains("a question mark on the end. He also has a white beard"),
                        "Wrong page before Guide directions");
                clickContinue(player);
                String first = drain(client);
                require(player.getDialogueManager().getDialogueStep() == 5, "First Guide page step incorrect");
                require(player.getOpenInterfaceId() == 4900, "First Guide page did not use four-line dialogue");
                require(first.contains("When you get to Lumbridge, look for this icon on your")
                        && first.contains("Guide should be standing slightly to the north-east of"),
                        "First Guide page text missing");
                require(hasGuideIconPacket(first), "First Guide page missing question-mark icon sentinel");

                clickContinue(player);
                String second = drain(client);
                require(player.getDialogueManager().getDialogueStep() == 6, "Second Guide page step incorrect");
                require(player.getOpenInterfaceId() == 4900, "Second Guide page did not use four-line dialogue");
                require(second.contains("the castle's courtyard and the others you will find")
                        && second.contains("scattered around Lumbridge."), "Second Guide page text missing");
                require(hasGuideIconPacket(second), "Second Guide page missing question-mark icon sentinel");

                clickContinue(player);
                String website = drain(client);
                require(player.getDialogueManager().getDialogueStep() == 7, "Website page step incorrect");
                require(player.getOpenInterfaceId() == 4893, "Continue did not replace Guide page");
                require(website.contains("If all else fails, visit the RuneScape website for a whole"),
                        "Continue got stuck instead of showing website page");

                for (boolean nativeContinue : new boolean[] {false, true}) {
                    player.setQuestState(0, 67);
                    require(DialogueManager.continueDialogue(player, 946, 8, 0), "Tutorial departure failed");
                    require(drain(client).contains("Welcome to Lumbridge!"), "Arrival statement missing");
                    require(player.getQuestState(0) == 1 && player.getOpenInterfaceId() == 374,
                            "Arrival did not complete tutorial with welcome statement open");
                    if (nativeContinue) {
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.putShort((short) -1).putInt((214 << 16) | 5).flip();
                        new InterfaceActionPacketHandler().handle(player,
                                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(payload)));
                    } else {
                        new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
                    }
                    require(player.getOpenInterfaceId() == 0, "Welcome Continue did not restore normal chatbox");
                    require(drain(client).length() == 1, "Welcome Continue did not send the close-modal packet");
                    require(player.getQuestState(0) == 1, "Welcome Continue changed tutorial completion");
                }
            }
        }
        System.out.println("Tutorial final mage dialogue checks passed (two Guide-icon pages + native Continue). ");
        System.exit(0);
    }

    private static void clickTwoLineContinue(Player player) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.putShort((short) -1).putInt((242 << 16) | 4).flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(payload)));
    }

    private static void clickContinue(Player player) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.putShort((short) -1).putInt((244 << 16) | 6).flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(payload)));
    }

    private static boolean hasGuideIconPacket(String packets) throws Exception {
        byte[] marker = ByteBuffer.allocate(6).putShort((short) 0xFFFF).putInt(244 << 16).array();
        return packets.contains(new String(marker, "ISO-8859-1"));
    }

    private static String drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) bytes.write(buffer, 0, count);
        } catch (SocketTimeoutException drained) { }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
