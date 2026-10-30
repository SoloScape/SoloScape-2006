import com.rs2.ServerSettings;
import com.rs2.model.player.Player;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.InterfaceBridge;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;

import java.nio.ByteBuffer;

/** Regression checks for the native revision-443 Castle Wars catapult controls. */
public final class CastleWarsCatapult443Checks {
    private static final int ROOT = 11169;
    private static final int GROUP = 54;
    private static final int[] LEGACY_BUTTONS = {11321, 11322, 11323, 11324, 11329, 11259};
    private static final int[] CHILDREN = {150, 151, 152, 153, 158, 163};

    public static void main(String[] args) {
        ServerSettings.clientBuild = 443;
        require(InterfaceBridge.translateGroup(ROOT) == GROUP,
                "Catapult interface must open native group 54 instead of auto-firing");
        for (int i = 0; i < CHILDREN.length; i++) {
            int packed = GROUP << 16 | CHILDREN[i];
            require(InterfaceBridge.translate(LEGACY_BUTTONS[i]) == packed,
                    "Wrong forward catapult mapping for child " + CHILDREN[i]);
            require(InterfaceBridge.toLegacyComponent(packed) == LEGACY_BUTTONS[i],
                    "Wrong reverse catapult mapping for child " + CHILDREN[i]);
        }
        for (int i = 0; i < 4; i++) {
            require(InterfaceBridge.translate(19700 + i)
                            == (GROUP << 16 | new int[]{116, 117, 132, 133}[i]),
                    "Wrong native digit model mapping " + i);
        }

        Player player = new Player(null);
        player.isBot = true; // This test requires no network connection.
        player.setOpenInterfaceId(ROOT);
        nativeWidgetSelect(player, 163);
        require(player.getOpenInterfaceId() == 0,
                "Native WIDGET_SELECT close did not reach the catapult handler");

        player.setOpenInterfaceId(ROOT);
        nativeWidgetButton(player, 163);
        require(player.getOpenInterfaceId() == 0,
                "Native INTERFACE_BUTTON close did not reach the catapult handler");

        // An unrelated panel must not accept forged group 54 catapult clicks.
        player.setOpenInterfaceId(6179);
        nativeWidgetButton(player, 163);
        nativeWidgetSelect(player, 163);
        require(player.getOpenInterfaceId() == 6179,
                "Closed catapult clicks unexpectedly closed another interface");

        System.out.println("Castle Wars native 443 catapult mappings and click routing OK");
    }

    private static void nativeWidgetSelect(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.order(java.nio.ByteOrder.LITTLE_ENDIAN).putShort((short) 0);
        payload.order(java.nio.ByteOrder.BIG_ENDIAN).putInt(GROUP << 16 | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6,
                        PacketBuffer.wrapReader(payload)));
    }

    private static void nativeWidgetButton(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(4);
        payload.putInt(GROUP << 16 | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4,
                        PacketBuffer.wrapReader(payload)));
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
