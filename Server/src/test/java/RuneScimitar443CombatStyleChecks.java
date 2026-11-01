import com.rs2.ServerSettings;
import com.rs2.model.combat.AttackBonusType;
import com.rs2.model.combat.AttackXpMode;
import com.rs2.model.player.Player;
import com.rs2.net.IsaacCipher;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.combat.AttackStyleDefinition;
import com.rs2.model.combat.WeaponInterfaceDefinition;
import com.rs2.model.combat.WeaponProfile;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.packet.InterfaceBridge;

/** Compares the native 443 Rune scimitar tab to the server's style routing. */
public final class RuneScimitar443CombatStyleChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Interfaces.load();
        WeaponProfile profile = WeaponProfile.forItem(new ItemStack(1333));
        require(profile == WeaponProfile.SCIMITAR, "Rune scimitar not classified correctly");
        require(profile.getInterfaceDefinition() == WeaponInterfaceDefinition.SLASH_SWORD,
                "Rune scimitar does not use sword-style interface");
        System.out.println("SCIMITAR-PROFILE=" + profile.name() + " native-group="
                + InterfaceBridge.translateGroup(profile.getInterfaceDefinition().getSidebarInterfaceId()));
        for (int group : new int[]{78, 81, 82, 89}) {
            for (int child = 0; child <= 20; child++) {
                Interfaces.Component c = Interfaces.forId(group, child);
                if (c == null) continue;
                System.out.println("COMBAT443 group=" + group + " child=" + child
                        + " type=" + c.type + " action=" + c.actionType
                        + " xy=" + c.x + "," + c.y + " size=" + c.width
                        + "x" + c.height + " parent=" + c.parentId
                        + " legacy=" + InterfaceBridge.toLegacyComponent(
                                (group << 16) | child));
            }
        }
        int[] legacyButtons = {2429, 2432, 2431, 2430};
        AttackStyleDefinition[] styles = WeaponInterfaceDefinition.SLASH_SWORD.getAttackStyles();
        for (int i = 0; i < styles.length; i++) {
            int packed = InterfaceBridge.translate(legacyButtons[i]);
            System.out.println("SCIMITAR-STYLE index=" + i + " xp=" + styles[i].getXpMode()
                    + " bonus=" + styles[i].getAttackBonusType()
                    + " button=" + legacyButtons[i] + " group=" + (packed >>> 16)
                    + " child=" + (packed & 0xffff)
                    + " reverse=" + InterfaceBridge.toLegacyComponent(packed));
            require(InterfaceBridge.toLegacyComponent(packed) == legacyButtons[i],
                    "443 scimitar style button does not round trip");
        }
        testScimitarStylePackets(legacyButtons);
        System.out.println("Rune scimitar 443 style mapping and varp synchronisation passed");
    }

    /**
     * The old handler called disableAutocast() first. Revision 443 emits
     * varp 43 during that call, so the client received the PREVIOUS
     * stance even though the server subsequently updated fightMode.
     */
    private static void testScimitarStylePackets(int[] buttons) throws Exception {
        AttackXpMode[] xpModes = {
            AttackXpMode.MELEE_ACCURATE, AttackXpMode.AGGRESSIVE,
            AttackXpMode.CONTROLLED, AttackXpMode.DEFENSIVE
        };
        AttackBonusType[] bonuses = {
            AttackBonusType.SLASH, AttackBonusType.SLASH,
            AttackBonusType.STAB, AttackBonusType.SLASH
        };
        int[] nativeChildren = {2, 5, 4, 3};
        int[] animations = {390, 390, 412, 390};
        try (ServerSocketChannel listener = ServerSocketChannel.open();
                Socket client = new Socket()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            client.connect(listener.getLocalAddress());
            client.setSoTimeout(3000);
            try (SocketChannel transport = listener.accept()) {
                Player player = new Player(null);
                Field socketField = Player.class.getDeclaredField("socketChannel");
                socketField.setAccessible(true);
                socketField.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.getEquipmentManager().getContainer().setItem(
                        3, new ItemStack(1333)); // Rune scimitar.
                InputStream packets = client.getInputStream();

                for (int index = 0; index < buttons.length; index++) {
                    AttackStyleDefinition style = WeaponInterfaceDefinition.SLASH_SWORD
                            .getAttackStyles()[index];
                    require(style.getXpMode() == xpModes[index],
                            "Wrong skill trained by scimitar stance " + index);
                    require(style.getAttackBonusType() == bonuses[index],
                            "Wrong damage type on scimitar stance " + index);
                    require(WeaponProfile.SCIMITAR.getAttackAnimations()[index] == animations[index],
                            "Wrong Rune scimitar attack animation for stance " + index);
                    int packedId = InterfaceBridge.translate(buttons[index]);
                    require((packedId >>> 16) == 78 && (packedId & 65535) == nativeChildren[index],
                            "443 style button points to the wrong icon or position: " + index);

                    player.setFightMode((index + 1) % 4); // Ensure prior value differs.
                    require(player.getEquipmentManager().handleAttackStyleButton(buttons[index]),
                            "443 scimitar style click unhandled: " + buttons[index]);
                    require(player.getFightMode() == index,
                            "Style click selected wrong server stance " + index);

                    // 443 disable-autocast writes varp 439 and then varp 43.
                    byte[] packetBytes = new byte[8];
                    int offset = 0;
                    while (offset < packetBytes.length) {
                        int read = packets.read(packetBytes, offset, packetBytes.length - offset);
                        require(read > 0, "Missing stance update packet at style " + index);
                        offset += read;
                    }
                    require((packetBytes[5] & 255) == ((-index) & 255)
                                    && (packetBytes[6] & 255) == 0
                                    && (packetBytes[7] & 255) == 43,
                            "Varp 43 sent the prior combat stance instead of " + index);
                }
            }
        }
    }

    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
}
