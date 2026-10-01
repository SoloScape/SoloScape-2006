import com.rs2.ServerSettings;
import com.rs2.model.player.BankManager;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.handler.CloseInterfacePacketHandler;

/** Banking advances once and retains the bank as the active interface. */
public final class TutorialBankOverlayChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        for (int stage : new int[] {51, 52, 1}) {
            Player player = new Player(null);
            player.isBot = true; // Suppress network writes without enabling bot banking.
            player.setQuestState(0, stage);
            player.setOpenInterfaceId(6179);
            BankManager.openBank(player);
            require(player.getQuestState(0) == (stage == 51 ? 52 : stage),
                    "Bank advanced the wrong tutorial stage");
            require(player.getOpenInterfaceId() == 5292, "Tutorial replaced active bank");
            java.lang.reflect.Field overlay = Player.class.getDeclaredField("inventoryOverlayInterfaceId");
            overlay.setAccessible(true);
            require(overlay.getInt(player) == 5063, "Bank inventory missing");
            new CloseInterfacePacketHandler().handle(player,
                    new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
            require(player.getOpenInterfaceId() == (stage == 1 ? 0 : 6179),
                    "Bank X close response removed tutorial instructions");
            if (stage != 1) {
                require(overlay.getInt(player) == 0, "Bank X retained inventory overlay");
            }
            require(Boolean.FALSE.equals(player.getAttributes().get("isBanking")),
                    "Bank X retained banking state");
            if (stage != 1) {
                new CloseInterfacePacketHandler().handle(player,
                        new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
                require(player.getOpenInterfaceId() == 6179,
                        "Duplicate bank close removed tutorial instructions");
            }
            BankManager.openBank(player);
            require(player.getOpenInterfaceId() == 5292, "Reopening lost active bank");
            require(player.getQuestState(0) == (stage == 51 ? 52 : stage),
                    "Reopening advanced tutorial again");
        }
        System.out.println("Tutorial bank overlay checks passed.");
        System.exit(0);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
