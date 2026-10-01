import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.ItemActionPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Exercises the stats window's actual open/remove packets and item updates. */
public final class EquipmentWindowChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Npc instructor = new Npc(944);
        instructor.setIndex(0);
        World.getNpcs()[0] = instructor;
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        for (int stage : new int[] {1, 42, 44}) {
            try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress());
                client.setSoTimeout(100);
                try (SocketChannel transport = listener.accept()) {
                    Player player = new Player(null);
                    socket.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setQuestState(0, stage);
                    player.getEquipmentManager().getContainer().setItem(3, new ItemStack(1205, 1));
                    ByteBuffer open = ByteBuffer.allocate(4).putInt((387 << 16) | 24);
                    open.flip();
                    new InterfaceActionPacketHandler().handle(player, new IncomingPacket(
                            ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(open)));
                    require(player.getOpenInterfaceId() == 15106, "Stats window did not open");
                    byte[] opened = drain(client);
                    require(opened.length >= 5 && opened[opened.length - 4] == 3,
                            "Stats window did not select the inventory sidebar tab");
                    require(opened[opened.length - 2] == 1 && (opened[opened.length - 1] & 255) == 81,
                            "Viewport open was not the last packet (tutorial could close it)");
                    require(hasContainer(opened, 1205), "Equipped dagger not sent to stats window");
                    remove(player, 3, 1277);
                    require(player.getEquipmentManager().getItemIdAtSlot(3) == 1205, "Mismatched item removed");
                    remove(player, 14, 1205);
                    require(player.getEquipmentManager().getItemIdAtSlot(3) == 1205, "Invalid slot removed item");
                    remove(player, 3, 1205);
                    require(player.getEquipmentManager().getContainer().getItemAt(3) == null, "Stats click did not unequip");
                    require(player.getInventoryManager().getItemAmount(1205) == 1, "Unequipped dagger missing");
                    require(player.getOpenInterfaceId() == 15106, "Removal closed stats window");
                    require(hasContainer(drain(client), -1), "Stats window retained old dagger icon");
                    for (int i = 0; i < 12; i++) require(((Integer) player.getCombatBonuses().get(i)) == 0, "Stale bonus");
                    player.getEquipmentManager().equipFromInventorySlot(0);
                    byte[] reequip = drain(client);
                    require(player.getEquipmentManager().getItemIdAtSlot(3) == 1205, "Reequip failed");
                    require(player.getOpenInterfaceId() == 15106, "Tutorial equip hid stats window");
                    require(hasContainer(reequip, 1205), "Reequip did not update stats window");
                    player.setOpenInterfaceId(0);
                    remove(player, 3, 1205);
                    require(player.getEquipmentManager().getItemIdAtSlot(3) == 1205, "Closed stats window allowed removal");
                    player.setOpenInterfaceId(15106);
                    while (player.getInventoryManager().getContainer().getFreeSlots() > 0) {
                        player.getInventoryManager().addItem(new ItemStack(1205, 1));
                    }
                    remove(player, 3, 1205);
                    require(player.getEquipmentManager().getItemIdAtSlot(3) == 1205, "Full inventory lost equipped item");
                }
            }
        }
        System.out.println("Equipment window checks passed (open order, icons, click removal, reequip, bonuses, invalid clicks, full inventory).");
        System.exit(0);
    }

    private static void remove(Player player, int slot, int item) {
        PacketWriter payload = PacketBuffer.allocateWriter(8);
        payload.writeShort(slot, ByteTransform.ADD, com.rs2.net.packet.ByteOrder.LITTLE);
        payload.writeInt((465 << 16) | 103);
        payload.writeShort(item, com.rs2.net.packet.ByteOrder.LITTLE);
        ByteBuffer bytes = payload.getBuffer();
        bytes.flip();
        new ItemActionPacketHandler().handle(player, new IncomingPacket(
                ClientPackets.WIDGET_ITEM_OPTION_1, 8, PacketBuffer.wrapReader(bytes)));
    }

    private static boolean hasContainer(byte[] bytes, int weapon) throws Exception {
        PacketWriter expected = PacketBuffer.allocateWriter(64);
        expected.writeInt((465 << 16) | 103);
        expected.writeShort(103);
        expected.writeShort(14);
        for (int slot = 0; slot < 14; slot++) {
            boolean occupied = slot == 3 && weapon >= 0;
            expected.writeByte(occupied ? 1 : 0, ByteTransform.NEGATE);
            expected.writeShort(occupied ? weapon + 1 : 0);
        }
        ByteBuffer buffer = expected.getBuffer();
        byte[] pattern = new byte[buffer.position()];
        buffer.flip();
        buffer.get(pattern);
        return new String(bytes, "ISO-8859-1").contains(new String(pattern, "ISO-8859-1"));
    }

    private static byte[] drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) bytes.write(buffer, 0, count);
        } catch (SocketTimeoutException drained) {
            // All synchronous writes collected.
        }
        return bytes.toByteArray();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
