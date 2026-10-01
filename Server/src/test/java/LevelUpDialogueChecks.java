import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Native Continue must dismiss standalone level-ups and advance queued ones. */
public final class LevelUpDialogueChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.showSkillUnlocks = false;
        QuestDefinition.loadDefinitions();
        Interfaces.load();
        require(Interfaces.forId(211, 2).actionType == 6,
                "Level-up Continue is not a native dialogue control");
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
                listener.bind(new InetSocketAddress("127.0.0.1", 0));
                client.connect(listener.getLocalAddress());
                client.setSoTimeout(1000);
                try (SocketChannel transport = listener.accept()) {
                    Player player = new Player(null);
                    socket.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setQuestState(0, 1);
                    player.getDialogueManager().resetDialogueState();
                    player.getSkillManager().showLevelUpInterface(0);
                    require(player.getOpenInterfaceId() == 359
                            && player.getDialogueManager().isDialogueInactive(), "Level-up setup failed");
                    drain(client);
                    if (nativeClient) {
                        click(player, 210, 1);
                        require(player.getOpenInterfaceId() == 359, "Stale Continue dismissed level-up");
                        click(player, 211, 0);
                        require(player.getOpenInterfaceId() == 359, "Text click dismissed level-up");
                    }
                    continueLevelUp(player, nativeClient);
                    require(player.getOpenInterfaceId() == 0, "Continue did not restore chatbox");
                    requireClosePacket(client);

                    player.getSkillManager().showLevelUpInterface(1);
                    player.queuedLevelUpSkillIds.add(2);
                    drain(client);
                    continueLevelUp(player, nativeClient);
                    require(player.getOpenInterfaceId() == 359 && player.queuedLevelUpSkillIds.isEmpty(),
                            "Queued level-up was not shown");
                    drain(client);
                    continueLevelUp(player, nativeClient);
                    require(player.getOpenInterfaceId() == 0, "Last queued level-up did not close");
                    requireClosePacket(client);
                }
            }
        }
        System.out.println("Level-up dialogue checks passed (native/legacy Continue, close packet, queue, stale clicks).");
        System.exit(0);
    }

    private static void continueLevelUp(Player player, boolean nativeClient) {
        if (nativeClient) click(player, 211, 2);
        else new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
    }

    private static void click(Player player, int group, int child) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.putShort((short) -1).putInt((group << 16) | child).flip();
        new InterfaceActionPacketHandler().handle(player, new IncomingPacket(ClientPackets.WIDGET_SELECT,
                6, PacketBuffer.wrapReader(payload)));
    }

    private static void requireClosePacket(Socket client) throws Exception {
        require(drain(client) == 1, "Continue must send the one-byte interface-close packet");
    }

    private static int drain(Socket client) throws Exception {
        int bytes = 0;
        byte[] buffer = new byte[4096];
        // Writes are synchronous; reading until quiet captures each action's packets.
        client.setSoTimeout(100);
        try {
            int count;
            while ((count = client.getInputStream().read(buffer)) != -1) bytes += count;
        } catch (java.net.SocketTimeoutException drained) {
            // No more output from the action.
        }
        return bytes;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
