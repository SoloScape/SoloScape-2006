import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.ModernPlayerUpdateTask;
import com.rs2.model.player.Player;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.PacketWriter;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/** Regressions for standing turns and tracking an NPC after its click route ends. */
public final class NpcClickMovementChecks {
    public static void main(String[] args) throws Exception {
        try {
            ServerSettings.clientBuild = 443;
            NpcDefinition.loadDefinitions();
            Player player = new Player(null);
            if (args.length > 0 && args[0].equals("appearance")) {
                checkAppearance(player, 823);
                player.setWalkAnimationOverride(900);
                player.setStandAnimationOverride(901);
                checkAppearance(player, 901);
            } else {
                checkTracking(player);
            }
            System.out.println("NPC click movement checks passed.");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkAppearance(Player player, int expectedTurn) throws Exception {
        Method build = ModernPlayerUpdateTask.class.getDeclaredMethod("buildAppearance", Player.class);
        build.setAccessible(true);
        ByteBuffer appearance = ((PacketWriter) build.invoke(null, player)).getBuffer();
        // The final fields are seven animation shorts, name (8), level (1), skill (2).
        appearance.position(appearance.position() - 25);
        require((appearance.getShort() & 65535) == player.getStandAnimation(), "Wrong idle animation");
        require((appearance.getShort() & 65535) == expectedTurn,
                "Standing turn uses walking animation");
        require((appearance.getShort() & 65535) == player.getWalkAnimation(), "Walk animation changed");
    }

    private static void checkTracking(Player player) throws Exception {
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        player.setUsername("npcclickcheck");
        player.setIndex(1);
        player.setEncodedIndex(32769);
        player.setSize(1);
        player.setPosition(new Position(3080, 9504, 0));
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        SocketChannel transport = SocketChannel.open();
        transport.close();
        Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        socket.set(player, transport);
        Npc instructor = new Npc(948);
        instructor.setIndex(160);
        instructor.setEncodedIndex(160);
        instructor.setSize(1);
        instructor.setPosition(new Position(3079, 9506, 0));
        player.setAttackRange(1);
        player.setMovementTarget(instructor);
        World.getPlayers()[1] = player;
        Position start = new Position(player.getPosition().getX(), player.getPosition().getY(), 0);
        World.processTick();
        require(!player.getPosition().equals(start), "443 player did not resume tracking the NPC");
        // The NPC moves again after the original client route would have finished.
        instructor.setPosition(new Position(3077, 9506, 0));
        start = new Position(player.getPosition().getX(), player.getPosition().getY(), 0);
        World.processTick();
        require(!player.getPosition().equals(start), "443 player did not track the moved NPC");
        for (int tick = 0; tick < 8; tick++) World.processTick();
        require(player.isWithinReach(instructor, 1) && !player.isOverlapping(instructor),
                "Player did not reach an adjacent talking tile");
        World.getPlayers()[1] = null;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
