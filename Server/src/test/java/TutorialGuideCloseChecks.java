import com.rs2.ServerSettings;
import com.rs2.model.player.Player;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.handler.CloseInterfacePacketHandler;

public final class TutorialGuideCloseChecks {
    public static void main(String[] args) {
        ServerSettings.clientBuild = 443;
        CloseInterfacePacketHandler handler = new CloseInterfacePacketHandler();

        for (int interfaceId : new int[] {8714, 8134}) {
            Player tutorial = new Player(null);
            tutorial.isBot = true;
            tutorial.setQuestState(0, 41);
            tutorial.setOpenInterfaceId(interfaceId);
            handler.handle(tutorial,
                    new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
            require(tutorial.getOpenInterfaceId() == 6179,
                    "Tutorial guide close lost instructions for " + interfaceId);

            Player normal = new Player(null);
            normal.isBot = true;
            normal.setQuestState(0, 1);
            normal.setOpenInterfaceId(interfaceId);
            handler.handle(normal,
                    new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
            require(normal.getOpenInterfaceId() == 0,
                    "Normal guide close did not close for " + interfaceId);
        }
        System.out.println("Tutorial guide close checks passed for skill and quest guides.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
