import com.rs2.ServerSettings;
import com.rs2.cache.js5.Definitions;
import com.rs2.model.HistoricalExamines;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Full cache coverage plus real examine packet responses on the wire. */
public final class ExamineTextChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        ObjectDefinition.loadRevision443();
        Path directory = Paths.get("../qa-output/examine-checks");
        Files.createDirectories(directory);
        try (PrintWriter output = new PrintWriter(Files.newBufferedWriter(directory.resolve("coverage.tsv"), StandardCharsets.UTF_8))) {
            output.println("kind\tid\tname\ttext\tnamed-fallback");
            for (String kind : new String[]{"item", "npc", "object"}) {
                int group = kind.equals("item") ? 10 : kind.equals("npc") ? 9 : 6;
                int count = 0, fallback = 0, named = 0;
                for (int id : new TreeSet<>(Definitions.readGroup(group).keySet())) {
                    String name, text;
                    if (kind.equals("item")) {
                        name = ItemDefinition.forId(id).getName(); text = ItemDefinition.forId(id).getDescription();
                    } else if (kind.equals("npc")) {
                        name = NpcDefinition.forId(id).getName(); text = NpcDefinition.forId(id).getDescription();
                    } else {
                        name = ObjectDefinition.forId(id).getName(); text = ObjectDefinition.forId(id).getDescription();
                    }
                    require(text != null && !text.trim().isEmpty(), "Missing " + kind + " " + id);
                    require(text.getBytes(StandardCharsets.ISO_8859_1).length < 255,
                            "Examine exceeds game message packet length: " + kind + " " + id);
                    boolean generic = text.equals("It's " + name + ".");
                    if (name != null && !name.isEmpty() && !name.equalsIgnoreCase("null")) named++;
                    if (generic) fallback++;
                    output.println(kind + "\t" + id + "\t" + name + "\t" + text + "\t" + generic);
                    count++;
                }
                System.out.println(kind + ": " + count + " definitions, " + named + " named, " + fallback + " named fallbacks");
            }
        }
        require(NpcDefinition.forId(0).getDescription().equals("Servant of the Duke of Lumbridge."), "Hans description");
        require(ItemDefinition.forId(437).getDescription().equals("Swap this note at any bank for the equivalent item."), "Note description");
        require(HistoricalExamines.find("npc", 0, "Custom Hans") == null, "Replaced NPC inherited unrelated text");
        require(HistoricalExamines.resolve("npc", 0, "Hans", "Cache text.").equals("Cache text."), "Cache precedence");
        require(ObjectDefinition.forId(-1) == null, "Negative object ID");
        checkPackets();
        System.out.println("Examine text checks passed (full cache coverage, notes, overrides, NPC/object/item packet responses).");
        System.exit(0);
    }

    private static void checkPackets() throws Exception {
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress()); client.setSoTimeout(2000);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                player.setQuestState(0, 1);
                Field socket = Player.class.getDeclaredField("socketChannel"); socket.setAccessible(true); socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                DataInputStream input = new DataInputStream(client.getInputStream());
                IsaacCipher cipher = new IsaacCipher(new int[4]);
                new NpcInteractionPacketHandler().handle(player, packet(ClientPackets.NPC_EXAMINE, new byte[]{0, 0}));
                message(input, cipher, NpcDefinition.forId(0).getDescription());
                new ObjectInteractionPacketHandler().handle(player, packet(ClientPackets.OBJECT_EXAMINE, new byte[]{(byte)221, 8}));
                message(input, cipher, ObjectDefinition.forId(2269).getDescription());
                new ItemActionPacketHandler().handle(player, packet(ClientPackets.ITEM_EXAMINE, new byte[]{1, (byte)(436 + 128)}));
                message(input, cipher, ItemDefinition.forId(436).getDescription());
                new NpcInteractionPacketHandler().handle(player, packet(ClientPackets.NPC_EXAMINE, new byte[]{(byte)255, (byte)255}));
                new ObjectInteractionPacketHandler().handle(player, packet(ClientPackets.OBJECT_EXAMINE, new byte[]{(byte)255, (byte)255}));
                new ItemActionPacketHandler().handle(player, packet(ClientPackets.ITEM_EXAMINE, new byte[]{(byte)255, 127}));
                require(client.getInputStream().available() == 0, "Invalid examine IDs sent messages");
                player.setActionLocked(true);
                new NpcInteractionPacketHandler().handle(player, packet(ClientPackets.NPC_EXAMINE, new byte[]{0, 0}));
                message(input, cipher, NpcDefinition.forId(0).getDescription());
                new ObjectInteractionPacketHandler().handle(player, packet(ClientPackets.OBJECT_EXAMINE, new byte[]{(byte)221, 8}));
                message(input, cipher, ObjectDefinition.forId(2269).getDescription());
                require(player.isActionLocked(), "Examining interrupted the current action");
            }
        }
    }

    private static IncomingPacket packet(int opcode, byte[] data) {
        return new IncomingPacket(opcode, data.length, PacketBuffer.wrapReader(ByteBuffer.wrap(data)));
    }

    private static void message(DataInputStream input, IsaacCipher cipher, String expected) throws Exception {
        require(((input.readUnsignedByte() - cipher.nextInt()) & 255) == 157, "Expected game message");
        byte[] data = new byte[input.readUnsignedByte()]; input.readFully(data);
        require(new String(data, 0, data.length - 1, StandardCharsets.ISO_8859_1).equals(expected), "Wrong examine response");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
