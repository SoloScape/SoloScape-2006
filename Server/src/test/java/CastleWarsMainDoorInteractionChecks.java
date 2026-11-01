import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.interaction.InteractionType;
import com.rs2.model.interaction.ObjectActionRouter;
import com.rs2.model.player.Player;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/** Both sides of both Castle Wars large-door pairs must be interactable. */
public final class CastleWarsMainDoorInteractionChecks {
    public static void main(String[] args) {
        Player player = new Player(null);
        // Clicking either half must work when already next to the other half.
        check(player, 4423, 2426, 3088, 2427, 3087, true);
        check(player, 4424, 2427, 3088, 2426, 3089, true);
        check(player, 4427, 2373, 3119, 2372, 3118, true);
        check(player, 4428, 2372, 3119, 2373, 3120, true);
        check(player, 4423, 2426, 3088, 2427, 3085, false);
        check(player, 4427, 2373, 3119, 2372, 3122, false);

        // Regression: Attack on one leaf while standing beside the other leaf
        // must not require movement to the clicked leaf's exact approach tile.
        checkSharedDoorway(player, 4423, 2426, 3088, 2427, 3087);
        checkSharedDoorway(player, 4424, 2427, 3088, 2426, 3089);
        checkSharedDoorway(player, 4427, 2373, 3119, 2372, 3118);
        checkSharedDoorway(player, 4428, 2372, 3119, 2373, 3120);
        checkMenuRouting();
        try {
            checkClosedGateClipping();
        } catch (Exception exception) {
            throw new AssertionError("Castle Wars collision regression failed", exception);
        }
        System.out.println("Castle Wars large-door interaction, routing and clipping checks OK");
    }

    private static void checkMenuRouting() {
        int[] ids = {4423, 4424, 4427, 4428, 4429, 4431, 4434};
        for (int id : ids) {
            checkRoute(id, "Open", InteractionType.FIRST_OBJECT);
            checkRoute(id, "Close", InteractionType.FIRST_OBJECT);
            checkRoute(id, "Repair", InteractionType.FIRST_OBJECT);
            checkRoute(id, "Attack", InteractionType.SECOND_OBJECT);
            checkRoute(id, "Examine", null);
        }
        checkRoute(4421, "Attack", null); // Barricades have separate semantics.
    }

    private static void checkRoute(int id, String label, InteractionType expected) {
        InteractionType actual = ObjectActionRouter.castleWarsMainDoorRoute(id, label);
        if (actual != expected) {
            throw new AssertionError("Gate option routing " + id + ":" + label
                    + " expected=" + expected + " actual=" + actual);
        }
    }

    /** Exercise the real collision map without loading the game cache. */
    private static void checkClosedGateClipping() throws Exception {
        Field lookupField = WalkingCollisionMap.class.getDeclaredField("regionLookup");
        lookupField.setAccessible(true);
        WalkingCollisionMap[] previous = (WalkingCollisionMap[]) lookupField.get(null);
        Constructor<WalkingCollisionMap> constructor =
                WalkingCollisionMap.class.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        int regionId = (37 << 8) | 48; // Both castles share this map region.
        WalkingCollisionMap[] testLookup = new WalkingCollisionMap[65536];
        testLookup[regionId] = constructor.newInstance(regionId);
        lookupField.set(null, testLookup);
        try {
            // Saradomin: closed wall is at y=3088 with orientation 3.
            checkGateEdge(2426, 3088, 3, 3087, 3088);
            checkGateEdge(2427, 3088, 3, 3087, 3088);
            // Zamorak: closed wall is at y=3119 with orientation 1.
            checkGateEdge(2373, 3119, 1, 3119, 3120);
            checkGateEdge(2372, 3119, 1, 3119, 3120);
        } finally {
            lookupField.set(null, previous);
        }
    }

    private static void checkGateEdge(int x, int doorY, int orientation,
                                       int firstY, int secondY) {
        if (!WalkingCollisionMap.canTravelBetween(x, firstY, x, secondY, 0, 1, 1)) {
            throw new AssertionError("Test gate edge must initially be clear at " + x + "," + doorY);
        }
        WalkingCollisionMap.setCastleWarsGateEdge(x, doorY, 0, orientation, true);
        if (WalkingCollisionMap.canTravelBetween(x, firstY, x, secondY, 0, 1, 1)
                || WalkingCollisionMap.canTravelBetween(x, secondY, x, firstY, 0, 1, 1)) {
            throw new AssertionError("Closed gate must block crossing in both directions: "
                    + x + "," + doorY);
        }
        WalkingCollisionMap.setCastleWarsGateEdge(x, doorY, 0, orientation, false);
        if (!WalkingCollisionMap.canTravelBetween(x, firstY, x, secondY, 0, 1, 1)
                || !WalkingCollisionMap.canTravelBetween(x, secondY, x, firstY, 0, 1, 1)) {
            throw new AssertionError("Open gate must allow crossing in both directions: "
                    + x + "," + doorY);
        }
    }

    private static void checkSharedDoorway(Player player, int id, int objectX, int objectY,
                                           int playerX, int playerY) {
        player.setPosition(new Position(playerX, playerY, 0));
        Position clickedLeafApproach = CastleWarsEngineeringManager.getMainDoorInteractionApproach(
                player, id, objectX, objectY);
        if (clickedLeafApproach == null
                || clickedLeafApproach.getX() == playerX) {
            throw new AssertionError("Test must start beside the other gate leaf: " + id);
        }
        if (!CastleWarsEngineeringManager.isAtMainDoorInteractionApproach(
                player, id, objectX, objectY)) {
            throw new AssertionError("Attack should already reach the shared doorway: " + id);
        }
    }

    private static void check(Player player, int id, int objectX, int objectY,
                              int playerX, int playerY, boolean expected) {
        player.setPosition(new Position(playerX, playerY, 0));
        boolean actual = CastleWarsEngineeringManager.isAtMainDoorInteractionApproach(
                player, id, objectX, objectY);
        if (actual != expected) {
            throw new AssertionError("Unexpected large-door reach for " + id
                    + " at " + playerX + "," + playerY);
        }
    }
}
