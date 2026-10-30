import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.Team;
import com.rs2.model.player.Player;

import java.lang.reflect.Field;
import java.util.Map;

/** The 2005 Castle Wars 'Take from' action requires 180 seconds with the enemy standard. */
public final class CastleWarsTeammateFlagTakeChecks {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Class<?> manager = CastleWarsManager.class;
        Field inProgress = field(manager, "gameInProgress");
        Field saradominHolder = field(manager, "saradominFlagHolder");
        Field zamorakHolder = field(manager, "zamorakFlagHolder");
        Field saradominSince = field(manager, "saradominFlagHeldSinceMillis");
        Field zamorakSince = field(manager, "zamorakFlagHeldSinceMillis");
        Map<Player, Team> teams = (Map<Player, Team>) field(manager, "gamePlayers").get(null);
        boolean oldProgress = inProgress.getBoolean(null);
        Object oldSaradomin = saradominHolder.get(null);
        Object oldZamorak = zamorakHolder.get(null);
        long oldSaradominSince = saradominSince.getLong(null);
        long oldZamorakSince = zamorakSince.getLong(null);

        Player taker = new Player(null);
        Player carrier = new Player(null);
        Player opponent = new Player(null);
        long acquiredAt = 10_000L;
        try {
            teams.put(taker, Team.SARADOMIN);
            teams.put(carrier, Team.SARADOMIN);
            teams.put(opponent, Team.ZAMORAK);
            inProgress.setBoolean(null, true);
            saradominHolder.set(null, null);
            saradominSince.setLong(null, 0L);
            zamorakHolder.set(null, carrier);
            zamorakSince.setLong(null, acquiredAt);

            require(!eligible(taker, carrier, acquiredAt + 179_999L),
                    "Take from appeared before three minutes");
            require(eligible(taker, carrier, acquiredAt + 180_000L),
                    "Take from did not unlock at three minutes");
            require(!eligible(carrier, carrier, acquiredAt + 200_000L),
                    "Carrier must not take the flag from themselves");
            require(!eligible(opponent, carrier, acquiredAt + 200_000L),
                    "Opponent must not take a teammate-only flag");

            // The action applies only to the enemy flag, not one's own team's flag.
            zamorakHolder.set(null, null);
            saradominHolder.set(null, carrier);
            saradominSince.setLong(null, acquiredAt);
            require(!eligible(taker, carrier, acquiredAt + 200_000L),
                    "Could take the team's own flag from its carrier");

            zamorakHolder.set(null, carrier);
            saradominHolder.set(null, taker);
            require(!eligible(taker, carrier, acquiredAt + 200_000L),
                    "A player already carrying a flag must not take another");
            saradominHolder.set(null, null);

            // After a transfer the new holder needs a fresh three-minute window.
            zamorakHolder.set(null, taker);
            zamorakSince.setLong(null, acquiredAt + 180_000L);
            require(!eligible(carrier, taker, acquiredAt + 180_001L),
                    "New carrier's timer did not reset");
            require(eligible(carrier, taker, acquiredAt + 360_000L),
                    "New carrier's three-minute window did not expire");

            inProgress.setBoolean(null, false);
            require(!eligible(carrier, taker, acquiredAt + 400_000L),
                    "Take from remained available outside the game");
        } finally {
            teams.remove(taker);
            teams.remove(carrier);
            teams.remove(opponent);
            inProgress.setBoolean(null, oldProgress);
            saradominHolder.set(null, oldSaradomin);
            zamorakHolder.set(null, oldZamorak);
            saradominSince.setLong(null, oldSaradominSince);
            zamorakSince.setLong(null, oldZamorakSince);
        }
        System.out.println("Castle Wars teammate flag-take checks passed");
    }

    private static boolean eligible(Player taker, Player carrier, long now) {
        return CastleWarsManager.canTakeEnemyFlagFromTeammate(taker, carrier, now);
    }

    private static Field field(Class<?> owner, String name) throws Exception {
        Field result = owner.getDeclaredField(name);
        result.setAccessible(true);
        return result;
    }

    private static void require(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }
}
