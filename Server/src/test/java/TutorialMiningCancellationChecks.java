import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.mining.MiningTask;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.MovementPacketHandler;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Checks actual instruction text sent when a ground/minimap click cancels mining. */
public final class TutorialMiningCancellationChecks {
    public static void main(String[] args) throws Exception {
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        for (int build : new int[] {443}) {
            ServerSettings.clientBuild = build;
            for (int stage : new int[] {33, 34}) {
                for (boolean minimap : new boolean[] {false, true}) {
                    try (ServerSocketChannel listener = ServerSocketChannel.open()) {
                        listener.bind(new InetSocketAddress("127.0.0.1", 0));
                        try (Socket client = new Socket();
                             SocketChannel transport = connect(client, listener)) {
                            Player player = new Player(null);
                            socketField.set(player, transport);
                            player.setOutboundCipher(new IsaacCipher(new int[4]));
                            player.setPosition(new Position(3080, 9505, 0));
                            player.movementSystemMode = 0;
                            player.setQuestState(0, stage);
                            int sequence = player.nextActionSequence();
                            player.setActiveCycleEvent(new MiningTask(player.getMiningManager(),
                                    sequence, stage == 33 ? 3043 : 3042, 3076, 9504,
                                    null, 0, 0, 0, 0, 0, 0));
                            player.getDialogueManager().showTutorialInstructionOverlay("Please wait.", "",
                                    "Your character is now attempting to mine the rock.",
                                    "This should only take a few seconds.", "", true);
                            require(drain(client).contains("Please wait."), "Waiting prompt was not sent");

                            // Revision 443 one-step path with the run flag set to zero.
                            byte[] path = {(byte) ((3081 & 255) + 128), (byte) (3081 >> 8),
                                    0, (byte) (9505 & 255), (byte) (9505 >> 8)};
                            int opcode = minimap ? 80 : 99;
                            new MovementPacketHandler().handle(player, new IncomingPacket(opcode,
                                    minimap ? 19 : 5, PacketBuffer.wrapReader(ByteBuffer.wrap(path))));
                            String sent = drain(client);
                            require(sent.contains(stage == 33 ? "one tin ore." : "Now you have some tin ore"),
                                    "Mining instruction missing: build=" + build + " stage=" + stage + " minimap=" + minimap);
                            require(!sent.contains("Please wait."), "Waiting prompt was sent again");
                            require(!player.isCurrentActionSequence(sequence), "Mining was not cancelled");
                            require(player.getQuestState(0) == stage, "Cancellation advanced tutorial");
                            require(player.getOpenInterfaceId() == 6179, "Instruction overlay closed");
                        }
                    }
                }
            }
        }
        System.out.println("Tutorial mining cancellation checks passed (tin/copper, ground/minimap, revision 443).");
        System.exit(0);
    }

    private static SocketChannel connect(Socket client, ServerSocketChannel listener) throws Exception {
        client.connect(listener.getLocalAddress());
        client.setSoTimeout(100);
        return listener.accept();
    }

    private static String drain(Socket client) throws Exception {
        InputStream input = client.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        try {
            int count;
            while ((count = input.read(chunk)) != -1) {
                bytes.write(chunk, 0, count);
            }
        } catch (SocketTimeoutException drained) {
            // The synchronous handler finished writing; no more packets arrived.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
