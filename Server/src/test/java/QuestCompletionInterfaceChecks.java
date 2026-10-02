import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestScript;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.InterfaceBridge;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Reproduces the absent flat table and verifies native completion packets. */
public final class QuestCompletionInterfaceChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        InterfaceDefinition.interfaceCount = 0;
        Interfaces.load();
        require(!QuestScript.usesLegacyCompletionInterface(), "443 selected ancient spell picker");
        require(InterfaceBridge.translateGroup(1689) == 388, "Ancient autocast picker changed");
        require(InterfaceBridge.translate(6161) == (388 << 16 | 6), "Ancient cancel changed");
        require(InterfaceBridge.translateGroup(12140) == 277, "Wrong completion group");
        int[] ids = {12144, 12145, 12147, 12150, 12151, 12152, 12153, 12154, 12155};
        for (int id : ids) {
            Interfaces.Component component = Interfaces.forPackedId(InterfaceBridge.translate(id));
            require(component != null && component.groupId == 277
                    && component.type == (id == 12145 ? 6 : 4), "Invalid completion widget " + id);
        }
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new java.net.InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(2000);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                socketField.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                DataInputStream in = new DataInputStream(client.getInputStream());
                for (int id : ids) {
                    if (id == 12145) continue;
                    String text = id == 12144 ? "You have completed Cook's Assistant!" : "Reward " + id;
                    player.packetSender.sendInterfaceText(text, id);
                    require(opcode(in, decoder) == 180, "Missing completion text packet");
                    byte[] payload = new byte[in.readUnsignedShort()];
                    in.readFully(payload);
                    require(ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getInt()
                            == InterfaceBridge.translate(id), "Wrong text destination");
                    require(new String(payload, 4, payload.length - 5, "ISO-8859-1").equals(text), "Wrong text");
                }
                player.packetSender.sendInterfaceModel(QuestScript.usesLegacyCompletionInterface() ? 6161 : 12145, 250, 1891);
                require(opcode(in, decoder) == 137, "Missing cake image packet");
                require(in.readInt() == 250 && in.readInt() == (277 << 16 | 3)
                        && in.readUnsignedShort() == 1891, "Wrong cake image");
                player.packetSender.showInterface(QuestScript.usesLegacyCompletionInterface() ? 1689 : 12140);
                require(opcode(in, decoder) == 160, "Missing completion open packet");
                require((in.readUnsignedByte() << 8 | ((in.readUnsignedByte() - 128) & 255)) == 277,
                        "Opened combat picker instead of completion scroll");
            }
        }
        ServerSettings.clientBuild = 317;
        require(QuestScript.usesLegacyCompletionInterface(), "Old legacy cache behavior changed");
        InterfaceDefinition.interfaceCount = 13000;
        require(!QuestScript.usesLegacyCompletionInterface(), "New legacy cache behavior changed");
        System.out.println("Quest completion interface checks passed (native cache widgets, packets, autocast, legacy selection).");
        System.exit(0);
    }

    private static int opcode(DataInputStream in, IsaacCipher cipher) throws Exception {
        return (in.readUnsignedByte() - cipher.nextInt()) & 255;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
