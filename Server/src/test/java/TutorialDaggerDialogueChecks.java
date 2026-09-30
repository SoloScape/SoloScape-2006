import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.smithing.SmithingBarDefinition;
import com.rs2.model.skill.smithing.SmithingHandler;
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

/** Verifies the actual smithing result and both Continue packet paths. */
public final class TutorialDaggerDialogueChecks {
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
                    player.getSkillManager().getCurrentLevels()[13] = 1;
                    player.getInventoryManager().addItem(new ItemStack(2347, 1));
                    player.getInventoryManager().addItem(new ItemStack(2349, 1));
                    player.setSelectedSmithingBarItemId(2349);
                    player.setSelectedSmithingBarDefinition(SmithingBarDefinition.forBarItemId(2349));
                    // A stale instructor dialogue must not intercept the result's Continue.
                    player.getDialogueManager().setDialogueId(948);
                    player.getDialogueManager().setDialogueStep(3);
                    drain(client);
                    SmithingHandler.startSmithingTask(player, 1205, 1);
                    CycleEvent task = player.getActiveCycleEvent();
                    require(task != null, "Smithing task not started");
                    CycleEventContainer event = new CycleEventContainer(player, task, 5);
                    task.execute(event);
                    require(drain(client).contains("You hammer the bronze and make a dagger."),
                            "Dagger result text incorrect");
                    require(player.getOpenInterfaceId() == 356, "Result dialogue overwritten");
                    require(player.getQuestState(0) == 39 && !event.isActive(), "Smithing did not finish");
                    require(player.getInventoryManager().getItemAmount(1205) == 1
                            && !player.getInventoryManager().containsItem(2349), "Incorrect smithing output");
                    if (nativeClient) {
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.putShort((short) -1).putInt((210 << 16) | 1).flip();
                        new InterfaceActionPacketHandler().handle(player,
                                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(payload)));
                    } else {
                        new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
                    }
                    require(player.getOpenInterfaceId() == 6179 && player.getQuestState(0) == 39,
                            "Continue stuck or advanced tutorial twice");
                    require(drain(client).contains("You've finished in this area."), "Next instructions missing");
                    require(player.getInventoryManager().getItemAmount(1205) == 1, "Continue made another dagger");
                }
            }
        }
        System.out.println("Tutorial dagger dialogue checks passed (exact text, smithing output, both Continue handlers).");
        System.exit(0);
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
