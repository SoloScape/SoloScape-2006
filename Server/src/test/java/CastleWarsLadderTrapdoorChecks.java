import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.player.Player;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/** Ground-floor ladder approaches and separate Open/Climb-down trapdoor actions. */
public final class CastleWarsLadderTrapdoorChecks {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ObjectDefinition.loadRevision443();
        Field teamField = CastleWarsManager.class.getDeclaredField("gamePlayers");
        teamField.setAccessible(true);
        Map<Player, CastleWarsManager.Team> teams =
                (Map<Player, CastleWarsManager.Team>) teamField.get(null);
        Player player = new Player(null);
        player.isBot = true;
        player.setIndex(75);
        player.setEncodedIndex(32768 + 75);
        World.players[75] = player;
        teams.put(player, CastleWarsManager.Team.ZAMORAK);
        try {
            testLadder(player);
            testTrapdoors(player, teams);
            System.out.println("Castle Wars ladder destination and two-step trapdoor checks passed");
        } finally {
            Method reset = CastleWarsManager.class.getDeclaredMethod("resetCastleWarsTrapdoors");
            reset.setAccessible(true);
            reset.invoke(null);
            teams.remove(player);
            World.players[75] = null;
            player.setIndex(-1);
        }
    }

    private static void testLadder(Player player) throws Exception {
        player.setPosition(new Position(2378, 3135, 0));
        Position approach = CastleWarsManager.getStairTraversalApproach(
                player, 1747, 2378, 3134);
        require(approach != null && approach.equals(new Position(2378, 3133, 0)),
                "Zamorak Climb-up must route to 2378,3133,0, not 2378,3135,0");
        Method climb = CastleWarsManager.class.getDeclaredMethod(
                "handleCastleWarsTraversal", Player.class, int.class, int.class, int.class);
        climb.setAccessible(true);
        player.setPosition(approach);
        require((Boolean) climb.invoke(null, player, 1747, 2378, 3134),
                "Climb-up not handled");
        require(player.getPosition().equals(new Position(2378, 3133, 1)),
                "Climb-up must arrive above the ladder at 2378,3133,1");
        require(CastleWarsManager.getStairTraversalApproach(
                        player, 4911, 2378, 3134).equals(new Position(2378, 3133, 1)),
                "Climb-down should use the same inner ladder tile");
        require((Boolean) climb.invoke(null, player, 4911, 2378, 3134),
                "Climb-down not handled");
        require(player.getPosition().equals(new Position(2378, 3133, 0)),
                "Climb-down must return to 2378,3133,0");
    }

    private static void testTrapdoors(Player player,
            Map<Player, CastleWarsManager.Team> teams) {
        // Surface tunnel trapdoor: Open must not descend. The opened model's
        // Climb-down action should descend, and Close must restore the original.
        testTrapdoor(player, 1568, 2399, 3099, 0,
                new Position(2399, 9500, 0));
        testTrapdoor(player, 1568, 2400, 3108, 0,
                new Position(2400, 9507, 0));
        testTrapdoor(player, CastleWarsManager.ZAMORAK_SPAWN_TRAPDOOR_ID,
                2370, 3132, 2, new Position(2370, 3132, 1));
        teams.put(player, CastleWarsManager.Team.SARADOMIN);
        testTrapdoor(player, CastleWarsManager.SARADOMIN_SPAWN_TRAPDOOR_ID,
                2429, 3075, 2, new Position(2429, 3075, 1));
    }

    private static void testTrapdoor(Player player, int id, int x, int y, int plane,
                                      Position destination) {
        Position origin = new Position(x, y, plane);
        player.setPosition(origin.copy());
        require(CastleWarsManager.handleFirstObjectAction(player, id, x, y),
                "Closed trapdoor Open was not handled: " + id);
        require(player.getPosition().equals(origin),
                "Opening trapdoor incorrectly teleported player: " + id);
        DynamicObject opened = ObjectManager.findDynamicObjectAt(x, y, plane);
        require(opened != null && opened.getWorldObject().getObjectId() == 1570,
                "Open action did not spawn Climb-down/Close model: " + id);
        require(opened.getWorldObject().getType() == 22,
                "Opened trapdoor used a different scene slot from its type-22"
                        + " closed model, causing overlapping trapdoor/ladder graphics: " + id);
        require(CastleWarsManager.handleFirstObjectAction(player, 1570, x, y),
                "Climb-down not handled for open trapdoor");
        require(player.getPosition().equals(destination),
                "Climb-down did not use intended destination: " + id);
        player.setPosition(origin);
        require(CastleWarsManager.handleSecondObjectAction(player, 1570, x, y),
                "Close action on opened trapdoor failed");
        require(ObjectManager.findDynamicObjectAt(x, y, plane) == null,
                "Trapdoor model did not close cleanly");
    }

    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
}
