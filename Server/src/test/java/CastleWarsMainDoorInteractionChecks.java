import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.player.Player;

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
        System.out.println("Castle Wars large-door adjacent interaction checks OK");
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
