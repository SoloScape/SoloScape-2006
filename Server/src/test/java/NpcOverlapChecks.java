import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.interaction.FirstNpcActionTask;
import com.rs2.model.interaction.SecondNpcActionTask;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/** One-tile overlap escape with terrain, walls, corners and occupied tiles. */
public final class NpcOverlapChecks {
    private static final int X = 3220, Y = 3220;
    private static final int[][] OFFSETS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int[] WALLS = {8, 128, 2, 32};
    private static int[][] flags;

    public static void main(String[] args) {
        try {
            ServerSettings.clientBuild = 443;
            NpcDefinition.loadDefinitions();
            Constructor<WalkingCollisionMap> constructor = WalkingCollisionMap.class.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            int regionId = (X >> 6 << 8) | (Y >> 6);
            WalkingCollisionMap region = constructor.newInstance(regionId);
            Field tileFlags = WalkingCollisionMap.class.getDeclaredField("tileFlags");
            tileFlags.setAccessible(true);
            flags = new int[64][64];
            ((int[][][]) tileFlags.get(region))[0] = flags;
            Field lookup = WalkingCollisionMap.class.getDeclaredField("regionLookup");
            lookup.setAccessible(true);
            ((WalkingCollisionMap[]) lookup.get(null))[regionId] = region;

            for (int npcId : new int[] {948, 494}) {
                clearFlags();
                check(npcId, null, true); // Even bankers must queue exactly one step.
                for (int[] exit : OFFSETS) {
                    clearFlags();
                    for (int[] offset : OFFSETS) setFlag(offset, 0x200000);
                    setFlag(exit, 0);
                    check(npcId, exit, true);
                }
                for (int direction = 0; direction < OFFSETS.length; direction++) {
                    clearFlags();
                    for (int index = 0; index < OFFSETS.length; index++) setFlag(OFFSETS[index], WALLS[index]);
                    // A wall blocks an otherwise empty destination. Only this edge is open.
                    setFlag(OFFSETS[direction], 0);
                    check(npcId, OFFSETS[direction], true);
                }
                clearFlags();
                for (int[] offset : OFFSETS) setFlag(offset, 0x200000);
                check(npcId, null, false); // Open diagonal destinations must not cut corners.
            }
            clearFlags();
            Npc neighbour = npc(948, X - 1, Y, 2);
            World.getNpcs()[2] = neighbour;
            check(948, new int[] {1, 0}, true);
            World.getNpcs()[2] = null;
            checkLargeNpc();
            System.out.println("NPC overlap checks passed (one step, terrain, walls, corners, occupied tiles).");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void check(int npcId, int[] expected, boolean move) {
        Player player = new Player(null);
        player.setEncodedIndex(32769);
        player.setSize(1);
        player.setPosition(new Position(X, Y, 0));
        Npc target = npc(npcId, X, Y, 1);
        World.getNpcs()[1] = target;
        player.setMovementTarget(target);
        target.setFacingDirection(6); // Banker's distance fallback also accepts an overlapping tile.
        int sequence = player.nextActionSequence();
        new FirstNpcActionTask(1, true, player, sequence, target).execute();
        new SecondNpcActionTask(1, true, player, sequence, target).execute();
        require(player.getMovementTarget() == target && player.getInteractionTarget() == null,
                "Interaction ran before escaping the NPC tile");
        player.getTargetMovement().process();
        require(player.getMovementQueue().getSteps().size() == 1, "Escape did not queue exactly one step: " + npcId);
        player.getMovementQueue().process();
        int dx = player.getPosition().getX() - X;
        int dy = player.getPosition().getY() - Y;
        require(move ? Math.max(Math.abs(dx), Math.abs(dy)) == 1 : dx == 0 && dy == 0,
                "Incorrect overlap escape distance: " + npcId);
        if (expected != null) require(dx == expected[0] && dy == expected[1], "Unsafe escape direction");
        require(player.getMovementTarget() == target, "Escape lost the pending NPC interaction");
        Position after = new Position(player.getPosition().getX(), player.getPosition().getY(), 0);
        player.getMovementQueue().process();
        require(player.getPosition().equals(after), "Escape contained extra walking steps");
        World.getNpcs()[1] = null;
    }

    private static Npc npc(int id, int x, int y, int index) {
        Npc npc = new Npc(id);
        npc.setIndex(index);
        npc.setEncodedIndex(index);
        npc.setSize(1);
        npc.setPosition(new Position(x, y, 0));
        return npc;
    }

    private static void checkLargeNpc() {
        clearFlags();
        Player player = new Player(null);
        player.setEncodedIndex(32769);
        player.setSize(1);
        player.setPosition(new Position(X, Y, 0));
        Npc target = npc(948, X - 1, Y - 1, 1);
        target.setSize(3);
        World.getNpcs()[1] = target;
        player.setMovementTarget(target);
        for (int tick = 0; tick < 2; tick++) {
            player.getTargetMovement().process();
            require(player.getMovementQueue().getSteps().size() == 1, "Large NPC escape queued extra steps");
            player.getMovementQueue().process();
        }
        require(!player.isOverlapping(target), "Player stayed inside a large NPC");
        World.getNpcs()[1] = null;
    }

    private static void clearFlags() {
        for (int[] row : flags) java.util.Arrays.fill(row, 0);
    }

    private static void setFlag(int[] offset, int flag) {
        flags[(X + offset[0]) & 63][(Y + offset[1]) & 63] = flag;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
