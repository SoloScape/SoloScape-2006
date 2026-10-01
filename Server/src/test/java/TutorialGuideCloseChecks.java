import com.rs2.ServerSettings;
import com.rs2.model.player.Player;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.handler.CloseInterfacePacketHandler;

public final class TutorialGuideCloseChecks {
    public static void main(String[] args) {
        ServerSettings.clientBuild = 443;
        CloseInterfacePacketHandler handler = new CloseInterfacePacketHandler();

        for (int interfaceId : new int[] {8714, 8134, 994}) {
            Player tutorial = new Player(null);
            tutorial.isBot = true;
            int stage = interfaceId == 994 ? 38 : 41;
            tutorial.setQuestState(0, stage);
            tutorial.setOpenInterfaceId(interfaceId);
            handler.handle(tutorial,
                    new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
            require(tutorial.getOpenInterfaceId() == 6179,
                    "Tutorial guide close lost instructions for " + interfaceId);
            if (interfaceId == 994) {
                handler.handle(tutorial,
                        new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
                require(tutorial.getOpenInterfaceId() == 6179,
                        "Duplicate smithing close lost instructions");
                require(tutorial.getQuestState(0) == stage,
                        "Smithing close advanced tutorial progress");
            }

            Player normal = new Player(null);
            normal.isBot = true;
            normal.setQuestState(0, 1);
            normal.setOpenInterfaceId(interfaceId);
            handler.handle(normal,
                    new IncomingPacket(ClientPackets.CLOSE_INTERFACE, 0, null));
            require(normal.getOpenInterfaceId() == 0,
                    "Normal guide close did not close for " + interfaceId);
        }
        System.out.println("Tutorial close checks passed for skill/quest guides and smithing.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
