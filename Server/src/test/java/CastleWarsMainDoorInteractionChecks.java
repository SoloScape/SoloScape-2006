import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.player.Player;
import java.lang.reflect.Field;

/** Both sides of both Castle Wars large-door pairs must be interactable. */
public final class CastleWarsMainDoorInteractionChecks {
    public static void main(String[] args) throws Exception {
        Player player = new Player(null);
        // Clicking either half must work when already next to the other half.
        check(player, 4423, 2426, 3088, 2427, 3087, true);
        check(player, 4424, 2427, 3088, 2426, 3089, true);
        check(player, 4427, 2373, 3119, 2372, 3118, true);
        check(player, 4428, 2372, 3119, 2373, 3120, true);
        check(player, 4423, 2426, 3088, 2427, 3085, false);
        check(player, 4427, 2373, 3119, 2372, 3122, false);

        // The open leaves are on different tiles and have different object IDs.
        // An opened gate must be clickable from either side without restarting.
        checkOpenGate(player, "saradominMainDoor",
                new int[]{4425, 4426}, new int[]{4423, 4424},
                new int[]{2426, 2427}, 3087, 3088, 3086, 3089);
        checkOpenGate(player, "zamorakMainDoor",
                new int[]{4429, 4430}, new int[]{4427, 4428},
                new int[]{2373, 2372}, 3120, 3119, 3118, 3121);
        System.out.println("Castle Wars large-door open/close approach checks OK");
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

    private static void checkOpenGate(Player player, String stateFieldName,
                                      int[] openIds, int[] closedIds, int[] xs,
                                      int openY, int closedY, int lowY, int highY)
            throws Exception {
        // Switch only the in-memory mode for these approach calculations.
        // Do not construct world objects or change collision in this unit test.
        Field stateField = CastleWarsEngineeringManager.class.getDeclaredField(stateFieldName);
        stateField.setAccessible(true);
        Object doorState = stateField.get(null);
        Field modeField = doorState.getClass().getDeclaredField("mode");
        modeField.setAccessible(true);
        Object originalMode = modeField.get(doorState);
        Object openMode = null;
        for (Object candidate : modeField.getType().getEnumConstants()) {
            if ("OPEN".equals(candidate.toString())) {
                openMode = candidate;
                break;
            }
        }
        if (openMode == null) {
            throw new AssertionError("Missing Castle Wars main door OPEN state");
        }
        try {
            modeField.set(doorState, openMode);
            for (int index = 0; index < openIds.length; index++) {
                int id = openIds[index];
                int x = xs[index];
                check(player, id, x, openY, x, lowY, true);
                check(player, id, x, openY, x, highY, true);
                check(player, id, x, openY, x, closedY, false);
                assertApproach(player, id, x, openY, x, lowY, lowY);
                assertApproach(player, id, x, openY, x, highY, highY);
                // A player in the now-empty original doorway must walk to a
                // safe side before closing, rather than shutting the gate on self.
                int nearerSafeY = Math.abs(closedY - lowY) <= Math.abs(closedY - highY)
                        ? lowY : highY;
                assertApproach(player, id, x, openY, x, closedY, nearerSafeY);

                player.setPosition(new Position(x, lowY, 0));
                if (CastleWarsEngineeringManager.getMainDoorInteractionApproach(
                        player, closedIds[index], x, closedY) != null) {
                    throw new AssertionError("Stale closed door object was interactable while open");
                }
            }
        } finally {
            modeField.set(doorState, originalMode);
        }
    }

    private static void assertApproach(Player player, int id, int objectX, int objectY,
                                       int playerX, int playerY, int expectedY) {
        player.setPosition(new Position(playerX, playerY, 0));
        Position approach = CastleWarsEngineeringManager.getMainDoorInteractionApproach(
                player, id, objectX, objectY);
        if (approach == null || approach.getX() != objectX
                || approach.getY() != expectedY || approach.getPlane() != 0) {
            throw new AssertionError("Unexpected main-door approach for " + id
                    + " from " + playerX + "," + playerY);
        }
    }
}
