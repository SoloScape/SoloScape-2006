import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;

/** Continue on the tutorial furnace explanation restores the current instruction. */
public final class FurnaceContinueChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        Method nativeContinue = InterfaceActionPacketHandler.class.getDeclaredMethod(
                "handleDialogueContinue", Player.class, int.class);
        nativeContinue.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            for (int stage : new int[] {33, 35}) {
                try (SocketChannel transport = SocketChannel.open()) {
                    transport.close();
                    Player player = new Player(null);
                    socket.set(player, transport);
                    player.setOutboundCipher(new IsaacCipher(new int[4]));
                    player.setPosition(new Position(3080, 9505, 0));
                    player.setQuestState(0, stage);
                    player.getQuestManager().refreshQuestJournal();
                    player.getDialogueManager().showThreeLineStatement(
                            "This is a furnace for smelting metal. To use it simply click on the",
                            "ore you wish to smelt then click on the furnace you would like to", "use.");
                    require(player.getOpenInterfaceId() == 363, "Furnace explanation missing");
                    if (nativeClient) {
                        InterfaceActionPacketHandler handler = new InterfaceActionPacketHandler();
                        require(Boolean.FALSE.equals(nativeContinue.invoke(handler, player, (212 << 16) | 4)),
                                "Accepted a non-Continue child");
                        // Actual client trace: opcode 153, widget 13893635 (212:3), child parameter 65535.
                        ByteBuffer payload = ByteBuffer.allocate(6);
                        payload.putShort((short) -1).putInt((212 << 16) | 3).flip();
                        handler.handle(player, new IncomingPacket(ClientPackets.WIDGET_SELECT, 6,
                                PacketBuffer.wrapReader(payload)));
                    } else {
                        new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
                    }
                    require(player.getOpenInterfaceId() == 6179, "Continue did not restore tutorial instruction");
                    require(player.getQuestState(0) == stage, "Furnace explanation advanced tutorial");
                    require(!player.getDialogueManager().continueTutorialStatement(),
                            "Persistent instruction treated as a Continue statement");
                    player.setQuestState(0, 1);
                    player.getDialogueManager().showThreeLineStatement("Normal", "furnace", "message");
                    require(!player.getDialogueManager().continueTutorialStatement(),
                            "Normal gameplay entered tutorial handling");
                }
            }
        }
        System.out.println("Furnace Continue checks passed (both handlers, tutorial instruction restored without advancing).");
        System.exit(0);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
