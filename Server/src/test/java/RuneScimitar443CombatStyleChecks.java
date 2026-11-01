import com.rs2.ServerSettings;
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
        System.out.println("Rune scimitar 443 style mapping diagnostic passed");
    }

    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
}
