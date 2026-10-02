import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
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

/** Verifies Tutorial Island blocks Lumbridge Home Teleport and Continue recovers cleanly. */
public final class TutorialHomeTeleportChecks {
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
                    drain(client);

                    ByteBuffer click = ByteBuffer.allocate(4);
                    click.putInt((192 << 16) | 591).flip();
                    new InterfaceActionPacketHandler().handle(player,
                            new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4,
                                    PacketBuffer.wrapReader(click)));
                    String blocked = drain(client);
                    require(blocked.contains("You can't cast this spell until you have completed the Tutorial."),
                            "Tutorial teleport message incorrect");
                    require(player.getOpenInterfaceId() == 356, "Tutorial teleport statement did not open");
                    require(player.getDialogueManager().isDialogueInactive(),
                            "Tutorial teleport statement should use tutorial Continue handling");
                    if (nativeClient) {
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.putShort((short) -1).putInt((210 << 16) | 1).flip();
                        new InterfaceActionPacketHandler().handle(player,
                                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6,
                                        PacketBuffer.wrapReader(payload)));
                    } else {
                        new InterfaceInputPacketHandler().handle(player,
                                new IncomingPacket(40, 0, null));
                    }

                    String continued = drain(client);
                    require(player.getOpenInterfaceId() == 6179,
                            "Continue did not restore Tutorial Island instructions");
                    require(player.getQuestState(0) == 38,
                            "Continue unexpectedly advanced the tutorial");
                    require(continued.contains("Smithing a dagger."),
                            "Continue did not send replacement tutorial text");
                    require(!continued.contains("Please wait..."),
                            "Continue response left a Please wait message");

                    player.setQuestState(0, 1);
                    player.setPosition(new Position(3200, 3200, 0));
                    click.clear();
                    click.putInt((192 << 16) | 591).flip();
                    long before = System.currentTimeMillis();
                    new InterfaceActionPacketHandler().handle(player,
                            new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4,
                                    PacketBuffer.wrapReader(click)));
                    String cast = drain(client);
                    require(!cast.contains("runes required"), "Home teleport required runes");
                    require(player.homeTeleportAvailableAtMillis >= before + 1800000L,
                            "Home teleport did not start a 30 minute cooldown");
                    require(player.isActionLocked(), "Home teleport was not scheduled");
                    require(player.getUpdateState().getAnimationId() == 4847,
                            "Generic cast animation replaced the circle drawing");
                    require(player.getUpdateState().getGraphicId() == 800,
                            "Generic teleport graphic replaced the circle");
                    long availableAt = player.homeTeleportAvailableAtMillis;
                    player.setActionLocked(false);
                    require(!player.getTeleportManager().castHomeTeleport(),
                            "Home teleport bypassed the cooldown");
                    require(drain(client).contains("30 minutes"), "Cooldown message incorrect");
                    require(player.homeTeleportAvailableAtMillis == availableAt,
                            "Rejected cast reset the cooldown");
                    player.homeTeleportAvailableAtMillis = System.currentTimeMillis() - 1L;
                    require(player.getTeleportManager().castHomeTeleport(),
                            "Expired cooldown blocked home teleport");
                    verifyHomeAnimation(player);

                    player.setActionLocked(false);
                    player.homeTeleportAvailableAtMillis = 0L;
                    player.setPosition(new Position(3200, 3900, 0));
                    require(!player.getTeleportManager().castHomeTeleport(),
                            "Home teleport bypassed wilderness restrictions");
                    require(player.homeTeleportAvailableAtMillis == 0L,
                            "Blocked teleport consumed the cooldown");
                }
            }
        }
        System.out.println("Home teleport checks passed (rune-free cast, cooldown, expiry, restrictions and tutorial)." );
        System.exit(0);
    }

    private static void verifyHomeAnimation(Player player) {
        player.setPosition(new Position(3200, 3200, 0));
        player.getSkillManager().getCurrentLevels()[3] = 10;
        com.rs2.model.skill.magic.HomeTeleportTask task =
                new com.rs2.model.skill.magic.HomeTeleportTask(player);
        com.rs2.model.task.CycleEventContainer container =
                new com.rs2.model.task.CycleEventContainer(player, task, 1);
        task.start();
        for (int tick = 1; tick <= 43; tick++) {
            container.execute();
            if (tick == 12) require(player.getUpdateState().getAnimationId() == 4850,
                    "Home teleport skipped sitting down");
            if (tick == 21) require(player.getUpdateState().getAnimationId() == 4853,
                    "Home teleport skipped opening the book");
            if (tick == 27) require(player.getUpdateState().getAnimationId() == 4855,
                    "Home teleport skipped reading the book");
            if (tick == 34) require(player.getUpdateState().getAnimationId() == 4857,
                    "Home teleport skipped disappearing");
            if (tick < 43) require(player.getPosition().getX() == 3200,
                    "Home teleport moved before the animation finished");
        }
        require(player.getPosition().getX() == 3222 && player.getPosition().getY() == 3218,
                "Home teleport did not arrive at Lumbridge");
        require(!container.isActive() && !player.isActionLocked(),
                "Home teleport did not release the action lock");
        player.setActionLocked(true);
        task = new com.rs2.model.skill.magic.HomeTeleportTask(player);
        container = new com.rs2.model.task.CycleEventContainer(player, task, 1);
        task.start();
        player.getSkillManager().getCurrentLevels()[3] = 9;
        container.execute();
        require(!container.isActive() && !player.isActionLocked(),
                "Damage did not interrupt the home teleport");
    }
    private static String drain(Socket socket) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = socket.getInputStream().read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
        } catch (SocketTimeoutException drained) {
            // All synchronous writes collected.
        }
        return new String(bytes.toByteArray(), "ISO-8859-1");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
