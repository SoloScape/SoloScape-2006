import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.interaction.FirstObjectActionTask;
import com.rs2.model.interaction.SecondObjectActionTask;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;
import com.rs2.net.packet.ByteOrder;
import com.rs2.net.packet.ByteTransform;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.PacketWriter;
import com.rs2.net.packet.handler.ObjectInteractionPacketHandler;
import java.nio.ByteBuffer;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/** Reproduces actual native-443 object packets on either enemy gate. */
public final class CastleWarsMainDoor443PacketChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ObjectDefinition.loadRevision443();
        Field teamsField = CastleWarsManager.class.getDeclaredField("gamePlayers");
        teamsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Player, CastleWarsManager.Team> teams =
                (Map<Player, CastleWarsManager.Team>) teamsField.get(null);
        Player player = new Player(null);
        player.isBot = true;
        player.setIndex(70);
        player.setEncodedIndex(32768 + 70); // Combat must classify the fixture as a player.
        World.players[70] = player;
        try {
            // Exterior approach, attacking the other team's pair.
            check(player, teams, CastleWarsManager.Team.SARADOMIN,
                    4427, 2373, 3119, 2373, 3118);
            check(player, teams, CastleWarsManager.Team.ZAMORAK,
                    4423, 2426, 3088, 2426, 3089);
            System.out.println("Castle Wars 443 enemy gate Open/Attack packets passed");
        } finally {
            teams.remove(player);
            World.players[70] = null;
            player.setIndex(-1);
            World.getTaskScheduler().getTasks().clear();
        }
    }

    private static void check(Player player, Map<Player, CastleWarsManager.Team> teams,
            CastleWarsManager.Team team, int objectId, int objectX, int objectY,
            int playerX, int playerY) throws Exception {
        player.setPosition(new Position(playerX, playerY, 0));
        teams.put(player, team);
        World.getTaskScheduler().getTasks().clear();
        require(CastleWarsManager.getGameTeam(player) == team, "Test participant lost game team");
        int before = CastleWarsEngineeringManager.getMainDoorHitpoints(
                team == CastleWarsManager.Team.SARADOMIN
                    ? CastleWarsManager.Team.ZAMORAK : CastleWarsManager.Team.SARADOMIN);
        click(player, 1, objectId, objectX, objectY);
        require(player.getInteractionTargetId() == objectId, "Open packet decoded wrong object");
        TickTask open = lastTask();
        require(open instanceof FirstObjectActionTask, "Open did not schedule first object task");
        System.out.println("GATE-PACKET OPEN id=" + objectId + " pos="
                + player.getPosition() + " moving=" + player.isMoving()
                + " recentMove=" + player.hasMovedWithinTicks(1));
        open.execute();

        World.getTaskScheduler().getTasks().clear();
        click(player, 2, objectId, objectX, objectY);
        require(player.getInteractionTargetId() == objectId, "Attack packet decoded wrong object");
        TickTask attack = lastTask();
        require(attack instanceof SecondObjectActionTask, "Attack did not schedule second object task");
        System.out.println("GATE-PACKET ATTACK id=" + objectId + " pos="
                + player.getPosition() + " moving=" + player.isMoving()
                + " recentMove=" + player.hasMovedWithinTicks(1));
        attack.execute();
        int after = CastleWarsEngineeringManager.getMainDoorHitpoints(
                team == CastleWarsManager.Team.SARADOMIN
                    ? CastleWarsManager.Team.ZAMORAK : CastleWarsManager.Team.SARADOMIN);
        System.out.println("GATE-PACKET HP id=" + objectId + " before=" + before + " after=" + after
                + " active=" + attack.isActive());
    }

    private static TickTask lastTask() {
        List tasks = World.getTaskScheduler().getTasks();
        require(!tasks.isEmpty(), "Object packet did not schedule any interaction task");
        return (TickTask) tasks.get(tasks.size() - 1);
    }

    private static void click(Player player, int option, int id, int x, int y) {
        PacketWriter writer = PacketBuffer.allocateWriter(6);
        int opcode;
        if (option == 1) {
            opcode = ClientPackets.OBJECT_OPTION_1;
            writer.writeShort(y, ByteTransform.ADD);
            writer.writeShort(id, ByteTransform.ADD, ByteOrder.LITTLE);
            writer.writeShort(x, ByteTransform.ADD);
        } else {
            opcode = ClientPackets.OBJECT_OPTION_2;
            writer.writeShort(x);
            writer.writeShort(id);
            writer.writeShort(y, ByteTransform.ADD);
        }
        ByteBuffer raw = writer.getBuffer();
        raw.flip();
        new ObjectInteractionPacketHandler().handle(player,
                new IncomingPacket(opcode, raw.remaining(), PacketBuffer.wrapReader(raw)));
    }

    private static void require(boolean condition, String reason) {
        if (!condition) throw new AssertionError(reason);
    }
}
