import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.smithing.SmeltingHandler;
import com.rs2.model.task.CycleEvent;
import com.rs2.model.task.CycleEventContainer;
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

/** Checks exact smelting messages and both Continue packets before/after the bar is made. */
public final class TutorialSmeltingDialogueChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Npc instructor = new Npc(948);
        instructor.setIndex(0);
        instructor.setPosition(new Position(3080, 9505, 0));
        World.getNpcs()[0] = instructor;
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            for (int ore : new int[] {436, 438}) {
                try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
                    listener.bind(new InetSocketAddress("127.0.0.1", 0));
                    client.connect(listener.getLocalAddress());
                    client.setSoTimeout(100);
                    try (SocketChannel transport = listener.accept()) {
                        Player player = new Player(null);
                        socket.set(player, transport);
                        player.setOutboundCipher(new IsaacCipher(new int[4]));
                        player.setPosition(new Position(3080, 9505, 0));
                        player.setQuestState(0, 35);
                        player.getSkillManager().getCurrentLevels()[13] = 1;
                        player.getInventoryManager().addItem(new ItemStack(436, 1));
                        player.getInventoryManager().addItem(new ItemStack(438, 1));
                        drain(client);
                        SmeltingHandler.handleOreOnFurnace(player, ore);
                        require(drain(client).contains("You smelt the copper and tin together in the furnace."),
                                "Initial smelting text incorrect");
                        require(player.getOpenInterfaceId() == 356, "Initial statement missing");
                        continueStatement(player, nativeClient);
                        require(player.getOpenInterfaceId() == 6179 && player.getQuestState(0) == 35,
                                "Initial Continue stuck or advanced tutorial early");
                        require(drain(client).contains("Smelting."), "Initial Continue did not send instructions");

                        CycleEvent task = player.getActiveCycleEvent();
                        CycleEventContainer event = new CycleEventContainer(player, task, 1);
                        for (int tick = 0; tick < 5; tick++) task.execute(event);
                        require(drain(client).contains("You retrieve a bar of bronze."), "Bar result text incorrect");
                        require(player.getOpenInterfaceId() == 356, "Bar result overwritten by tutorial");
                        require(player.getInventoryManager().getItemAmount(2349) == 1
                                && !player.getInventoryManager().containsItem(436)
                                && !player.getInventoryManager().containsItem(438), "Incorrect smelting output");
                        require(player.getQuestState(0) == 36 && !player.isActionLocked(), "Smelting did not finish");
                        continueStatement(player, nativeClient);
                        require(player.getOpenInterfaceId() == 6179 && player.getQuestState(0) == 36,
                                "Result Continue stuck or advanced twice");
                        require(drain(client).contains("You've made a bronze bar!"), "Result Continue did not send instructions");
                    }
                }
            }
        }
        System.out.println("Tutorial smelting dialogue checks passed (exact text, copper/tin use, both Continue handlers).");
        System.exit(0);
    }

    private static void continueStatement(Player player, boolean nativeClient) {
        if (nativeClient) {
            ByteBuffer payload = ByteBuffer.allocate(6);
            payload.putShort((short) -1).putInt((210 << 16) | 1).flip();
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
            // Collect synchronous writes until the connection is quiet.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
