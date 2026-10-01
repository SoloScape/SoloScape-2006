import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.impl.TutorialQuest;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.handler.ButtonClickPacketHandler;
import com.rs2.util.path.WalkingCollisionMap;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Regression checks for Tutorial Island run unlock, energy sync and completion. */
public final class TutorialRunStateChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        WalkingCollisionMap.loadCollisionMaps();
        Field socketField = Player.class.getDeclaredField("socketChannel");
        socketField.setAccessible(true);
        ButtonClickPacketHandler buttons = new ButtonClickPacketHandler();
        TutorialQuest tutorial = new TutorialQuest(0);

        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(100);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                player.setIndex(1);
                player.setEncodedIndex(32768);
                socketField.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setPosition(new Position(3233, 3229, 0));
                player.setRunEnergyPercent(100);

                player.setQuestState(0, 22);
                buttons.handleButton(player, 153);
                require(!player.getMovementQueue().isRunning(),
                        "Run unlocked before the Tutorial running lesson");
                require(player.getQuestState(0) == 22,
                        "Locked run click advanced the tutorial");

                player.setQuestState(0, 23);
                buttons.handleButton(player, 153);
                require(!player.getMovementQueue().isRunning(),
                        "Run unlocked before the stage-23 settings lesson");
                require(player.getQuestState(0) == 23,
                        "Premature run click advanced the Tutorial");
                player.setTutorialRunSettingsOpened();
                buttons.handleButton(player, 153);
                require(player.getMovementQueue().isRunning(),
                        "Run did not enable after the Tutorial settings lesson");
                require(player.getQuestState(0) == 24,
                        "Run lesson did not advance to stage 24");
                drain(client);
                IsaacCipher expectedCipher = new IsaacCipher(new int[4]);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.getMovementQueue().clear();
                player.getMovementQueue().addStep(new Position(3235, 3229, 0));
                player.getMovementQueue().removeFirstStep();
                int energyBefore = player.getRunEnergyRaw();
                player.getMovementQueue().process();
                byte[] runUpdate = drainBytes(client);
                require(player.getPosition().getX() == 3235,
                        "Enabled run did not consume two movement steps");
                require(player.getRunEnergyRaw() < energyBefore,
                        "Running did not drain run energy");
                require(runUpdate.length >= 2,
                        "Revision 443 running did not send a run-energy update");
                int expectedOpcode = (226 + expectedCipher.nextInt()) & 255;
                require((runUpdate[0] & 255) == expectedOpcode,
                        "Running energy update did not use revision 443 opcode 226");
                require((runUpdate[1] & 255) == player.getRunEnergyPercent(),
                        "Running energy update sent a stale percentage");

                player.setRunEnergyPercent(0);
                player.getMovementQueue().setRunning(true);
                drain(client);
                tutorial.refreshQuestJournal(player, 68);
                require(player.getQuestState(0) == 1,
                        "Tutorial completion did not mark Tutorial Island complete");
                require(player.getRunEnergyPercent() == 100,
                        "Tutorial completion did not restore run energy");
                require(!player.getMovementQueue().isRunning(),
                        "Tutorial completion left run toggled on");
                buttons.handleButton(player, 153);
                require(player.getMovementQueue().isRunning(),
                        "Run could not be enabled after arriving in Lumbridge");

                player.setRunEnergyPercent(0);
                buttons.handleButton(player, 153);
                require(!player.getMovementQueue().isRunning(),
                        "Zero-energy run toggle remained enabled");
            }
        }
        System.out.println("Tutorial run state checks passed (unlock, 443 energy sync, completion reset)." );
        System.exit(0);
    }

    private static String drain(Socket socket) throws Exception {
        return new String(drainBytes(socket), "ISO-8859-1");
    }

    private static byte[] drainBytes(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
        } catch (SocketTimeoutException drained) { }
        return bytes.toByteArray();
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
