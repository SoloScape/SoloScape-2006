import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
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

/** Verifies Tutorial Island blocks Lumbridge Home Teleport and Continue recovers cleanly. */
public final class TutorialHomeTeleportChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
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
                    player.setPosition(new Position(3083, 9499, 0));
                    player.setQuestState(0, 38);
                    drain(client);

                    ByteBuffer click = ByteBuffer.allocate(4);
                    click.putInt((192 << 16) | 591).flip();
                    new InterfaceActionPacketHandler().handle(player,
                            new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4,
                                    PacketBuffer.wrapReader(click)));
                    String blocked = drain(client);
                    require(blocked.contains("You can't cast this spell until you have completed the Tutorial."),
                            "Tutorial teleport message incorrect");
                    require(player.getOpenInterfaceId() == 356, "Tutorial teleport statement did not open");
                    require(player.getDialogueManager().isDialogueInactive(),
                            "Tutorial teleport statement should use tutorial Continue handling");
                    if (nativeClient) {
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.putShort((short) -1).putInt((210 << 16) | 1).flip();
                        new InterfaceActionPacketHandler().handle(player,
                                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6,
                                        PacketBuffer.wrapReader(payload)));
                    } else {
                        new InterfaceInputPacketHandler().handle(player,
                                new IncomingPacket(40, 0, null));
                    }

                    String continued = drain(client);
                    require(player.getOpenInterfaceId() == 6179,
                            "Continue did not restore Tutorial Island instructions");
                    require(player.getQuestState(0) == 38,
                            "Continue unexpectedly advanced the tutorial");
                    require(continued.contains("Smithing a dagger."),
                            "Continue did not send replacement tutorial text");
                    require(!continued.contains("Please wait..."),
                            "Continue response left a Please wait message");
                }
            }
        }
        System.out.println("Tutorial home teleport checks passed (message and both Continue paths)." );
        System.exit(0);
    }
    private static String drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
        } catch (SocketTimeoutException drained) {
            // All synchronous writes collected.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
