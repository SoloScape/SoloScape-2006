import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.BankManager;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.ItemActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Native cache, outgoing inventory packets, and all five deposit actions. */
public final class DepositBoxChecks {
    private static final int ITEMS = 11 << 16 | 61;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        Interfaces.load();
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Interfaces.Component grid = Interfaces.forPackedId(ITEMS);
        require(grid.type == 2 && grid.width * grid.height == 28, "Wrong deposit grid");
        String data = new String(grid.getData(), "ISO-8859-1");
        for (String action : new String[] {"Deposit 1", "Deposit 5", "Deposit 10", "Deposit All", "Deposit X"}) {
            require(data.contains(action), "Missing native action: " + action);
        }
        require(Interfaces.forId(11, 63).actionType == 3, "Missing native close button");
        require(InterfaceBridge.translate(7423) == ITEMS, "Missing outgoing mapping");
        require(InterfaceBridge.toLegacyComponent(ITEMS) == 7423, "Missing click mapping");
        checkOpeningPackets();
        for (int option = 1; option <= 5; option++) checkDeposit(option);
        checkClosedBox();
        System.out.println("Deposit box checks passed: native 28-slot grid, sidebar, 1/5/10/All/X and closed-box rejection.");
        System.exit(0);
    }

    private static Player player() {
        Player p = new Player(null);
        p.setQuestState(0, 1);
        p.isBot = true;
        p.getInventoryManager().getContainer().add(new ItemStack(995, 100), 0);
        return p;
    }

    private static void checkOpeningPackets() throws Exception {
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new java.net.InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(2000);
            try (SocketChannel transport = listener.accept()) {
                Player p = player();
                p.isBot = false;
                socket.set(p, transport);
                p.setOutboundCipher(new IsaacCipher(new int[4]));
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                DataInputStream in = new DataInputStream(client.getInputStream());
                p.setInventoryOverlayInterfaceId(5063);
                BankManager.openDepositBox(p);
                require(((in.readUnsignedByte() - decoder.nextInt()) & 255) == 228, "Inventory not sent");
                byte[] payload = new byte[in.readUnsignedShort()];
                in.readFully(payload);
                ByteBuffer bytes = ByteBuffer.wrap(payload);
                require(bytes.getInt() == ITEMS, "Inventory sent to wrong widget");
                bytes.getShort();
                require(bytes.getShort() == 28, "Wrong inventory size");
                require((-bytes.get() & 255) == 100 && bytes.getShort() == 996, "Coins not visible");
                require(((in.readUnsignedByte() - decoder.nextInt()) & 255) == 160, "Unexpected sidebar overlay");
                int group = in.readUnsignedByte() << 8 | (in.readUnsignedByte() - 128 & 255);
                require(group == 11 && p.getOpenInterfaceId() == 4465, "Wrong main interface");
                Field overlay = Player.class.getDeclaredField("inventoryOverlayInterfaceId");
                overlay.setAccessible(true);
                require(overlay.getInt(p) == 0, "Stale sidebar overlay");
                require(Boolean.TRUE.equals(p.getAttributes().get("isBanking")), "Banking state missing");
            }
        }
    }

    private static void checkDeposit(int option) {
        Player p = player();
        BankManager.openDepositBox(p);
        click(p, option);
        int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : option == 4 ? 100 : 7;
        if (option == 5) {
            require(p.getSelectedInterfaceId() == 7423, "X prompt targeted wrong container");
            require(p.getBankContainer().getItemAmount(995) == 0, "X deposited before input");
            input(p, amount);
        }
        require(p.getBankContainer().getItemAmount(995) == amount, "Wrong bank amount for option " + option);
        require(p.getInventoryManager().getItemAmount(995) == 100 - amount, "Wrong remaining inventory");
        require(p.getOpenInterfaceId() == 4465, "Deposit closed the box");
    }

    private static void checkClosedBox() {
        Player p = player();
        click(p, 1);
        require(p.getInventoryManager().getItemAmount(995) == 100, "Closed box accepted click");
        BankManager.openDepositBox(p);
        click(p, 5);
        p.packetSender.closeInterfaces();
        input(p, 7);
        require(p.getBankContainer().getItemAmount(995) == 0, "Closed box accepted X input");
    }

    private static void click(Player p, int option) {
        PacketWriter out = PacketBuffer.allocateWriter(8);
        if (option == 2 || option == 4) {
            out.writeInt(ITEMS, com.rs2.net.packet.ByteOrder.INVERSE_MIDDLE);
            if (option == 2) out.writeShort(995, com.rs2.net.packet.ByteOrder.LITTLE);
            out.writeShort(0, ByteTransform.ADD, com.rs2.net.packet.ByteOrder.LITTLE);
            if (option == 4) out.writeShort(995);
        } else if (option == 3) {
            out.writeShort(0, ByteTransform.ADD, com.rs2.net.packet.ByteOrder.LITTLE);
            out.writeShort(995, ByteTransform.ADD, com.rs2.net.packet.ByteOrder.LITTLE);
            out.writeInt(ITEMS, com.rs2.net.packet.ByteOrder.MIDDLE);
        } else {
            out.writeShort(0, ByteTransform.ADD, option == 1 ? com.rs2.net.packet.ByteOrder.LITTLE : com.rs2.net.packet.ByteOrder.BIG);
            out.writeInt(ITEMS);
            out.writeShort(995, com.rs2.net.packet.ByteOrder.LITTLE);
        }
        int[] opcodes = {0, 144, 113, 188, 221, 171};
        ByteBuffer payload = out.getBuffer();
        payload.flip();
        new ItemActionPacketHandler().handle(p, new IncomingPacket(opcodes[option], 8, PacketBuffer.wrapReader(payload)));
    }

    private static void input(Player p, int amount) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt(amount);
        payload.flip();
        new InterfaceInputPacketHandler().handle(p, new IncomingPacket(ClientPackets.AMOUNT_INPUT, 4, PacketBuffer.wrapReader(payload)));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
