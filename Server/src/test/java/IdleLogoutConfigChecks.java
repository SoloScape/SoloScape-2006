import com.rs2.Server;
import com.rs2.model.player.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/** Simple Java 8 regression checks for hot-reloaded AFK logout configuration. */
public final class IdleLogoutConfigChecks {
    private static Boolean parse(String config) throws IOException {
        return Server.parseIdleLogoutSetting(new BufferedReader(new StringReader(config)));
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void invalid(String config) throws IOException {
        try {
            parse(config);
            throw new AssertionError("Malformed value was accepted: " + config);
        } catch (IOException expected) {
            // A bad edit must not silently reset the existing AFK policy.
        }
    }

    public static void main(String[] args) throws Exception {
        // The old five-minute timeout must not remain in the actual runtime policy.
        check(Player.IDLE_LOGOUT_TIMEOUT_MILLIS == 90_000L,
                "AFK logout is configured for 90 seconds");
        long lastInputMillis = 1_000_000L;
        check(!Player.hasIdleLogoutTimedOut(lastInputMillis, lastInputMillis),
                "input immediately resets AFK duration");
        check(!Player.hasIdleLogoutTimedOut(lastInputMillis, lastInputMillis + 89_999L),
                "89.999 seconds is not timed out");
        check(Player.hasIdleLogoutTimedOut(lastInputMillis, lastInputMillis + 90_000L),
                "exactly 90 seconds is timed out");
        check(Player.hasIdleLogoutTimedOut(lastInputMillis, lastInputMillis + 300_000L),
                "AFK timeout remains active after 90 seconds");
        check(!Player.hasIdleLogoutTimedOut(0L, 999_999L),
                "uninitialized input clock must not force logout");
        check(!Player.hasIdleLogoutTimedOut(lastInputMillis, lastInputMillis - 1L),
                "backward clock must not force logout");
        check(Boolean.FALSE.equals(parse("[IDLE_LOGOUT];0\n")),
                "zero disables the 90-second idle logout");
        check(Boolean.TRUE.equals(parse("[IDLE_LOGOUT];1\n")),
                "one enables the 90-second idle logout");
        check(Boolean.FALSE.equals(parse(
                "// A server.cfg comment\n[IDLE_LOGOUT];1\n"
                + "[IDLE_LOGOUT];0\n")), "last config entry wins");
        check(Boolean.FALSE.equals(parse("  [IDLE_LOGOUT] ; 0  \n")),
                "whitespace around the value is allowed");
        check(parse("[P2P_WORLD];1\n") == null,
                "missing idle setting must not silently change its value");
        invalid("[IDLE_LOGOUT];\n");
        invalid("[IDLE_LOGOUT];disabled\n");
        invalid("[IDLE_LOGOUT];0;1\n");
        System.out.println("Idle logout config checks passed.");
    }
}
