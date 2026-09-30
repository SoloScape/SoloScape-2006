import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.mining.ProspectingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import java.lang.reflect.*;
import java.nio.channels.SocketChannel;
public final class ProspectingResultChecks {
    public static void main(String[] args) throws Exception {
        QuestDefinition.loadDefinitions();
        Npc instructor = new Npc(948);
        instructor.setIndex(0);
        instructor.setPosition(new Position(3080, 9505, 0));
        World.getNpcs()[0] = instructor;
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        Method nativeContinue = InterfaceActionPacketHandler.class.getDeclaredMethod("handleDialogueContinue", Player.class, int.class);
        nativeContinue.setAccessible(true);
        for (boolean nativeClient : new boolean[] {false, true}) {
            for (int stage : new int[] {30, 31}) {
                Player player = new Player(null);
                player.setIndex(1);
                player.setEncodedIndex(32768);
                player.setPosition(new Position(3080, 9505, 0));
                player.setQuestState(0, stage);
                SocketChannel transport = SocketChannel.open();
                transport.close();
                socket.set(player, transport);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setActionLocked(true);
                ProspectingTask task = new ProspectingTask(player.getMiningManager(), stage == 30 ? 2094 : 2090, stage == 30 ? "tin" : "copper");
                task.execute(new CycleEventContainer(player, task, 5));
                require(player.getQuestState(0) == stage + 1, "Prospecting did not advance tutorial");
                require(player.getOpenInterfaceId() == 356, "Result was overwritten by tutorial instructions");
                require(!player.isActionLocked(), "Prospecting left actions locked");
                if (nativeClient) {
                    require(Boolean.FALSE.equals(nativeContinue.invoke(new InterfaceActionPacketHandler(), player, (210 << 16) | 2)), "Non-Continue child accepted");
                    require(Boolean.TRUE.equals(nativeContinue.invoke(new InterfaceActionPacketHandler(), player, (210 << 16) | 1)), "Native continue unhandled");
                } else {
                    new InterfaceInputPacketHandler().handle(player, new IncomingPacket(40, 0, null));
                }
                require(player.getOpenInterfaceId() == 6179, "Continue did not restore tutorial instructions");
                require(player.getQuestState(0) == stage + 1, "Continue advanced tutorial twice");
            }
        }
        System.out.println("Prospecting result checks passed (tin/copper results and both continue handlers).");
        System.exit(0);
    }
    private static void require(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
