import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Exercises native prayer clicks and decodes the resulting audio/state packets. */
public final class PrayerSoundChecks {
    private static final int[] SOUNDS = {
        446, 449, 436, 441, 434, 448, 451, 443, 337,
        439, 450, 440, 438, 444, 433, 1703, 1705, 1704
    };

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.freeToPlayWorld = false;
        Interfaces.load();
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
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                player.getSkillManager().getExperience()[5] = 13034431;
                player.getSkillManager().getCurrentLevels()[5] = 99;
                for (int prayer = 0; prayer < 18; prayer++) {
                    Interfaces.Component widget = Interfaces.forId(271, prayer);
                    require(widget.actionType == 4, "Wrong native prayer click target");
                    byte[] data = widget.getData();
                    require(data[25] == 5 && (data[27] & 255) == 83 + prayer,
                            "Prayer varp differs from bundled cache");
                    click(player, prayer);
                    Packets on = decode(drain(client), decoder);
                    require(on.sound == AudioIds443.sound(SOUNDS[prayer]), "Missing/wrong activation audio " + prayer);
                    require(Integer.valueOf(1).equals(on.varps.get(83 + prayer)), "Missing active config " + prayer);
                    require(player.getActivePrayers()[prayer] && player.prayerDrainRate > 0, "Prayer did not activate");
                    click(player, prayer);
                    Packets off = decode(drain(client), decoder);
                    require(off.sound == AudioIds443.sound(435), "Missing deactivation audio " + prayer);
                    require(Integer.valueOf(0).equals(off.varps.get(83 + prayer)), "Missing inactive config " + prayer);
                    require(!player.getActivePrayers()[prayer] && player.prayerDrainRate == 0, "Prayer did not deactivate");
                }
                click(player, 0);
                decode(drain(client), decoder);
                click(player, 3);
                Packets switched = decode(drain(client), decoder);
                require(switched.sound == AudioIds443.sound(441), "Switch duplicated or lost sound");
                require(Integer.valueOf(0).equals(switched.varps.get(83)), "Conflicting prayer stayed lit");
                require(!player.getActivePrayers()[0] && player.getActivePrayers()[3], "Conflict state incorrect");
                player.getPrayerManager().deactivateAll();
                Packets reset = decode(drain(client), decoder);
                require(reset.sound == -1 && reset.varps.size() == 24, "Reset lost prayer configs or added toggle audio");
                player.getSkillManager().getCurrentLevels()[5] = 0;
                click(player, 0);
                Packets empty = decode(drain(client), decoder);
                require(empty.sound == AudioIds443.sound(437) && empty.varps.size() == 24,
                        "Depleted prayer points lost failure audio or reset configs");
                require(!player.getActivePrayers()[0], "Empty prayer activated");
                player.getSkillManager().getCurrentLevels()[5] = 99;
                player.setSidebarInterfaceId(5, -1);
                click(player, 0);
                require(drain(client).length == 0 && !player.getActivePrayers()[0], "Hidden prayer tab accepted click");
            }
        }
        System.out.println("Prayer sound checks passed (18 native on/off clicks, audio, cache varps, drain, conflicts, reset, depleted points, hidden tab).");
        System.exit(0);
    }

    private static void click(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt(271 << 16 | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player, new IncomingPacket(
                ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(payload)));
    }

    private static byte[] drain(Socket client) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int count;
            while ((count = client.getInputStream().read(buffer)) >= 0) out.write(buffer, 0, count);
        } catch (SocketTimeoutException done) { }
        return out.toByteArray();
    }

    private static Packets decode(byte[] data, IsaacCipher cipher) {
        ByteBuffer bytes = ByteBuffer.wrap(data);
        Packets result = new Packets();
        while (bytes.hasRemaining()) {
            int opcode = ((bytes.get() & 255) - cipher.nextInt()) & 255;
            if (opcode == 81) {
                require(result.sound == -1, "Duplicate sound packet");
                result.sound = bytes.getShort() & 65535;
                require(bytes.get() == 1 && bytes.getShort() == 0, "Incorrect sound loops/delay");
            } else if (opcode == VarpPacket.SMALL_OPCODE) {
                int value = -bytes.get();
                result.varps.put(bytes.getShort() & 65535, value);
            } else {
                throw new AssertionError("Unexpected packet opcode " + opcode);
            }
        }
        return result;
    }

    private static final class Packets {
        int sound = -1;
        final Map<Integer, Integer> varps = new LinkedHashMap<Integer, Integer>();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
