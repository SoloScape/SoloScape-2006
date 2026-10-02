import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.magic.StandardTeleportTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.model.travel.WorldTeleportMenu;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.AudioIds443;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/** Checks the menu's scheduled departure sound, animation and arrival. */
public final class WorldTeleportMenuChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(40);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setIndex(1);
                player.setQuestState(0, 1);
                player.setPosition(new Position(3200, 3200, 0));
                WorldTeleportMenu.open(player);
                drain(client);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                require(WorldTeleportMenu.handleButton(player, 19611), "Destination click unhandled");
                require(player.getOpenInterfaceId() == 0, "Menu remained open during departure");
                require(player.isActionLocked(), "Teleport did not lock actions");
                require(Boolean.FALSE.equals(player.getAttributes().get("canTakeDamage")),
                        "Teleport did not protect player");
                require(player.getPosition().getX() == 3200, "Menu still teleports instantly");
                require(!WorldTeleportMenu.handleButton(player, 19610), "Duplicate click was accepted");
                WorldTeleportMenu.open(player);
                require(player.getOpenInterfaceId() == 0, "Locked player reopened teleport menu");

                Field pending = CycleEventHandler.class.getDeclaredField("pendingEvents");
                pending.setAccessible(true);
                CycleEventContainer teleport = null;
                for (Object event : (Queue<?>) pending.get(CycleEventHandler.getInstance())) {
                    CycleEventContainer candidate = (CycleEventContainer) event;
                    if (candidate.getEntity() == player && candidate.getEvent() instanceof StandardTeleportTask) {
                        require(teleport == null, "More than one teleport scheduled");
                        teleport = candidate;
                    }
                }
                require(teleport != null, "Standard teleport not scheduled");
                require(player.getUpdateState().getAnimationId() == 714, "Missing departure animation");
                require(player.getUpdateState().getGraphicId() == 301, "Missing departure graphic");
                ByteBuffer sound = ByteBuffer.wrap(drain(client));
                require(sound.remaining() == 7, "Expected menu close and one immediate departure sound packet");
                require(((sound.get() & 255) - decoder.nextInt() & 255) == 178, "Menu close packet missing");
                require(((sound.get() & 255) - decoder.nextInt() & 255) == 81, "Wrong sound opcode");
                require((sound.getShort() & 65535) == AudioIds443.sound(202), "Wrong teleport sound");
                require(sound.get() == 1 && sound.getShort() == 0, "Wrong sound loops/delay");
                require(player.getPosition().getX() == 3200, "Moved before departure finished");
                teleport.execute();
                require(drain(client).length == 0, "Departure sound repeated");
                teleport.execute();
                require(player.getPosition().getX() == 3210 && player.getPosition().getY() == 3424
                        && player.getPosition().getPlane() == 0, "Wrong Varrock destination");
                require(player.getUpdateState().getAnimationId() == 715, "Missing arrival animation");
                require(new String(drain(client), "ISO-8859-1").contains("Teleported to Varrock."),
                        "Missing arrival message");
                teleport.execute();
                teleport.execute();
                require(!teleport.isActive() && !player.isActionLocked(), "Teleport did not release actions");
                require(Boolean.TRUE.equals(player.getAttributes().get("canTakeDamage")),
                        "Damage stayed disabled after arrival");
                require(drain(client).length == 0, "Teleport emitted extra effects after arrival");
                player.setDead(true);
                WorldTeleportMenu.open(player);
                require(player.getOpenInterfaceId() == 0, "Dead player opened teleport menu");
            }
        }
        System.out.println("World teleport menu checks passed (scheduled movement, departure audio/graphic, arrival, locks and duplicate clicks).");
        System.exit(0);
    }

    private static byte[] drain(Socket client) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = client.getInputStream().read(buffer)) >= 0) bytes.write(buffer, 0, count);
        } catch (SocketTimeoutException done) { }
        return bytes.toByteArray();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
