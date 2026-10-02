import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.guide.SkillGuideCategory;
import com.rs2.model.skill.guide.SkillGuideEntry;
import com.rs2.model.skill.guide.SkillGuideManager;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Native action-type-6 categories must refresh rows and release the Continue lock. */
public final class SkillGuideCategoryChecks {
    private static final int[] CHILDREN = {
        131, 108, 109, 112, 122, 125, 128, 143, 146, 149, 159, 162, 165
    };

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        Interfaces.load();
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        SkillGuideManager.initialize();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(1000);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setQuestState(0, 1);
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                DataInputStream input = new DataInputStream(client.getInputStream());
                InterfaceActionPacketHandler handler = new InterfaceActionPacketHandler();
                String[] fields = {"agilityCategories", "hunterCategories", "constructionCategories"};
                int[] skills = {8, 21, 22};
                int switches = 0;
                for (int skill = 0; skill < skills.length; skill++) {
                    player.getSkillGuideManager().selectedSkillIndex = skills[skill];
                    Field categoriesField = SkillGuideManager.class.getDeclaredField(fields[skill]);
                    categoriesField.setAccessible(true);
                    List<?> categories = (List<?>) categoriesField.get(null);
                    player.getSkillGuideManager().showSelectedSkillCategory(1);
                    readRefresh(input, decoder, (SkillGuideCategory) categories.get(0), 0);
                    // Include returning to the first category and repeating its click.
                    for (int index = 0; index < categories.size() + 2; index++) {
                        int categoryIndex = index % categories.size();
                        int child = CHILDREN[categoryIndex];
                        require(Interfaces.forId(308, child).actionType == 6,
                                "Category is not a native Continue control: " + child);
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.order(java.nio.ByteOrder.LITTLE_ENDIAN).putShort((short) -1);
                        payload.order(java.nio.ByteOrder.BIG_ENDIAN).putInt((308 << 16) | child).flip();
                        handler.handle(player, new IncomingPacket(ClientPackets.WIDGET_SELECT, 6,
                                PacketBuffer.wrapReader(payload)));
                        readRefresh(input, decoder, (SkillGuideCategory) categories.get(categoryIndex), child);
                        require(player.getOpenInterfaceId() == 8714, "Category closed the guide");
                        switches++;
                    }
                }
                System.out.println("Skill guide checks passed: " + switches
                        + " native category switches, correct rows/icons/labels and Continue-lock reset.");
            }
        }
        System.exit(0);
    }

    private static void readRefresh(DataInputStream input, IsaacCipher decoder,
                                    SkillGuideCategory category, int categoryChild) throws Exception {
        SkillGuideEntry first = (SkillGuideEntry) category.entries.get(0);
        boolean firstRow = false;
        boolean categoryLabel = categoryChild == 0;
        boolean icons = false;
        while (true) {
            int opcode = (input.readUnsignedByte() - decoder.nextInt()) & 255;
            if (opcode == 180 || opcode == 228) {
                byte[] bytes = new byte[input.readUnsignedShort()];
                input.readFully(bytes);
                ByteBuffer packet = ByteBuffer.wrap(bytes);
                if (opcode == 180) {
                    int widget = packet.order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                    String text = new String(bytes, 4, bytes.length - 5, StandardCharsets.ISO_8859_1);
                    if (widget == ((308 << 16) | 45)) firstRow = text.endsWith(first.getDisplayLabel());
                    if (widget == ((308 << 16) | categoryChild)) categoryLabel = text.equals(category.name);
                } else {
                    require(packet.getInt() == ((308 << 16) | 132), "Wrong guide icon container");
                    icons = true;
                }
            } else if (opcode == 88) {
                input.readFully(new byte[6]);
            } else if (opcode == 234) {
                input.readFully(new byte[5]);
            } else if (opcode == 160) {
                int group = (input.readUnsignedByte() << 8) | ((input.readUnsignedByte() - 128) & 255);
                require(group == 308, "Wrong guide group");
                // Client packet 160 resets Class39_Sub10.anInt1420, restoring clickable labels.
                require(firstRow, "Selected category did not update its first advancement row");
                require(categoryLabel, "Selected category label was not restored");
                require(icons, "Selected category did not refresh icons");
                return;
            } else {
                throw new AssertionError("Unexpected refresh opcode: " + opcode);
            }
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
