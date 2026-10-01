import com.rs2.net.packet.InterfaceBridge;
import com.rs2.net.packet.SpellWidgets;

public final class Revision443SpellSelectionChecks {
    public static void main(String[] args) {
        int windStrike = 192 << 16;
        require(SpellWidgets.toLegacySpellButton(windStrike, 0xFFFF) == 1152,
                "Wind Strike packed-widget selection did not map to legacy 1152");
        require(SpellWidgets.toLegacySpellButton(windStrike, 0) == 1152,
                "Wind Strike explicit child did not map to legacy 1152");
        require(SpellWidgets.toLegacySpellButton((192 << 16) | 531, 0xFFFF) == 12445,
                "Tele Block did not map from its native child");
        require(SpellWidgets.toLegacySpellButton((193 << 16) | 5, 0xFFFF) == 12861,
                "Ice Rush did not map from its native child");
        require(SpellWidgets.toLegacySpellButton(149 << 16, 0xFFFF) == InterfaceBridge.UNMAPPED,
                "Non-spell widget unexpectedly mapped as a spell");
        System.out.println("Revision 443 spell selection mapping checks passed.");
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
