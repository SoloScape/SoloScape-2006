import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.combat.CombatManager;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.SkillManager;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/** Cache-backed native clicks must enforce base Prayer levels and correct client toggles. */
public final class PrayerLevelChecks {
    private static final int[] LEVELS = {8, 9, 26, 27, 44, 45};
    private static final String[] NAMES = {"Sharp Eye", "Mystic Will", "Hawk Eye", "Mystic Lore", "Eagle Eye", "Mystic Might"};

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.freeToPlayWorld = false;
        Interfaces.load();
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        Method multiplier = CombatManager.class.getDeclaredMethod("getPrayerMultiplier", Player.class, int.class);
        multiplier.setAccessible(true);
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
                for (int i = 0; i < 6; i++) {
                    int child = 36 + 2 * i;
                    int prayer = 18 + i;
                    int varp = 862 + i;
                    byte[] data = Interfaces.forId(271, child).getData();
                    require(Interfaces.forId(271, child).actionType == 4
                            && data[25] == 5 && ((data[26] & 255) << 8 | data[27] & 255) == varp,
                            "Wrong cache click/varp for " + NAMES[i]);
                    for (int level : new int[]{1, LEVELS[i] - 1}) {
                        setLevel(player, level, 99); // boosted points cannot unlock a prayer
                        player.setOutboundCipher(new IsaacCipher(new int[4]));
                        click(player, child);
                        byte[] failure = drain(client);
                        require(!player.getActivePrayers()[prayer] && player.prayerDrainRate == 0,
                                "Underlevel activation: " + NAMES[i]);
                        requireFirstConfig(failure, varp, 0);
                    }
                    setLevel(player, LEVELS[i], 1); // current points need not equal the base level
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    click(player, child);
                    requireFirstConfig(drain(client), varp, 1);
                    require(player.getActivePrayers()[prayer] && player.prayerDrainRate == 3 * (1 << (i / 2)),
                            "Eligible prayer/drain failed: " + NAMES[i]);
                    int skill = (i & 1) == 0 ? 4 : 6;
                    require(Math.abs((Double)multiplier.invoke(null, player, skill) - (1.05 + .05 * (i / 2))) < 1e-9,
                            "Combat boost missing: " + NAMES[i]);
                    click(player, child);
                    drain(client);
                    require(!player.getActivePrayers()[prayer] && player.prayerDrainRate == 0,
                            "Prayer did not toggle off: " + NAMES[i]);
                    click(player, child);
                    drain(client);
                    player.getPrayerManager().deactivateAll();
                    byte[] reset = drain(client);
                    require(reset.length == 24 * 4 && player.prayerDrainRate == 0, "Incomplete prayer reset");
                    setLevel(player, LEVELS[i], 0);
                    click(player, child);
                    drain(client);
                    require(!player.getActivePrayers()[prayer], "Zero-point prayer activated");
                }
                setLevel(player, 99, 99);
                player.getPrayerManager().togglePrayer(0); // defence is compatible
                player.getPrayerManager().togglePrayer(1);
                player.getPrayerManager().togglePrayer(2);
                click(player, 44);
                drain(client);
                require(player.getActivePrayers()[0] && player.getActivePrayers()[22]
                        && !player.getActivePrayers()[1] && !player.getActivePrayers()[2], "Offensive conflicts failed");
                click(player, 46);
                drain(client);
                require(!player.getActivePrayers()[22] && player.getActivePrayers()[23], "Ranged/magic conflict failed");
                player.getPrayerManager().togglePrayer(10);
                drain(client);
                require(!player.getActivePrayers()[23] && player.getActivePrayers()[10], "Melee conflict failed");
                player.getPrayerManager().deactivateAll();
                drain(client);
                // Redemption must only toggle on a click, even at low health.
                // Its effect belongs to the combat hit handler.
                player.getSkillManager().getCurrentLevels()[3] = 1;
                int graphicBefore = player.getUpdateState().getGraphicId();
                for (int level : new int[]{1, 48, 49}) {
                    setLevel(player, level, level);
                    click(player, 16);
                    drain(client);
                    require(player.getUpdateState().getGraphicId() == graphicBefore,
                            "Redemption click played its effect at Prayer level " + level);
                    require(player.getSkillManager().getCurrentLevels()[3] == 1,
                            "Redemption click healed before taking damage");
                    require(player.getSkillManager().getCurrentLevels()[5] == level,
                            "Redemption click consumed Prayer points");
                    require(player.getActivePrayers()[16] == (level >= 49),
                            "Redemption level gate failed");
                }
                click(player, 16);
                drain(client);
                require(!player.getActivePrayers()[16]
                        && player.getUpdateState().getGraphicId() == graphicBefore,
                        "Disabling Redemption played its effect");
                player.getPrayerManager().deactivateAll();
                drain(client);
                player.setSidebarInterfaceId(5, -1);
                for (int i = 0; i < 6; i++) click(player, 36 + 2 * i);
                require(drain(client).length == 0 && player.prayerDrainRate == 0, "Hidden tab accepted prayer");
                player.getPrayerManager().togglePrayer(-1);
                player.getPrayerManager().togglePrayer(24);
                player.getPrayerManager().togglePrayer(null);
            }
        }
        System.out.println("Prayer level checks passed (six cache buttons, level 1/boundaries, boosted/depleted points, client corrections, drain, combat boosts, conflicts, reset, Redemption low-health clicks, hidden tab).");
        System.exit(0);
    }

    private static void setLevel(Player player, int base, int points) {
        player.getSkillManager().getExperience()[5] = SkillManager.getExperienceForLevel(base - 1);
        player.getSkillManager().getCurrentLevels()[5] = points;
        require(player.getSkillManager().getBaseLevel(5) == base, "Bad test level");
    }

    private static void click(Player player, int child) {
        ByteBuffer payload = ByteBuffer.allocate(4).putInt(271 << 16 | child);
        payload.flip();
        new InterfaceActionPacketHandler().handle(player, new IncomingPacket(
                ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(payload)));
    }

    private static void requireFirstConfig(byte[] bytes, int varp, int value) {
        require(bytes.length >= 4, "No client correction");
        int opcode = ((bytes[0] & 255) - new IsaacCipher(new int[4]).nextInt()) & 255;
        require(opcode == VarpPacket.SMALL_OPCODE && -bytes[1] == value
                && ((bytes[2] & 255) << 8 | bytes[3] & 255) == varp, "Wrong client prayer config");
    }

    private static byte[] drain(Socket client) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        try {
            int count;
            while ((count = client.getInputStream().read(buffer)) >= 0) out.write(buffer, 0, count);
        } catch (SocketTimeoutException done) { }
        return out.toByteArray();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
