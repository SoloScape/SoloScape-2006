import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.ReconnectDestination;

/** Regression checks for the provisional 2006 Castle Wars reconnect policy. */
public final class CastleWarsReconnectChecks {
    private static final long LEFT = 1_000_000L;
    private static final long END = LEFT + 20 * 60_000L;

    public static void main(String[] args) {
        check(true, LEFT, END, LEFT, ReconnectDestination.ARENA, "Immediate reconnect");
        check(true, LEFT, END, LEFT + 119_999L, ReconnectDestination.ARENA, "Inside two minutes");
        check(true, LEFT, END, LEFT + 120_000L, ReconnectDestination.ARENA, "Exact boundary");
        check(true, LEFT, END, LEFT + 120_001L, ReconnectDestination.WAITING_ROOM, "After boundary");
        check(true, LEFT, END, LEFT + 300_000L, ReconnectDestination.WAITING_ROOM, "Late match reconnect");
        check(true, LEFT, END, END, ReconnectDestination.LOBBY, "Match end");
        check(false, LEFT, END, LEFT + 1L, ReconnectDestination.LOBBY, "Finished match");
        check(false, LEFT, END, LEFT + 120_001L, ReconnectDestination.LOBBY, "New match generation");
        check(true, LEFT, END, LEFT - 1L, ReconnectDestination.WAITING_ROOM, "Invalid reversed clock");
        System.out.println("Castle Wars reconnection policy checks passed");
    }

    private static void check(boolean sameActiveMatch, long loggedOutAt, long endAt,
                              long loggedInAt, ReconnectDestination expected, String caseName) {
        ReconnectDestination actual = CastleWarsManager.reconnectDestination(
                sameActiveMatch, loggedOutAt, endAt, loggedInAt);
        if (actual != expected) {
            throw new AssertionError(caseName + ": expected " + expected + ", got " + actual);
        }
    }
}
