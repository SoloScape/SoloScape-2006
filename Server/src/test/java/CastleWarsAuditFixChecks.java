import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.combat.AttackBonusType;
import com.rs2.model.combat.DeathCleanupTask;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.Team;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;

import java.lang.reflect.Field;
import java.util.Map;

/** Regression checks for the six issues found in the October 2026 Castle Wars audit. */
public final class CastleWarsAuditFixChecks {
    public static void main(String[] args) throws Exception {
        testLogoutRemovesMatchIssuedSupplies();
        testDeferredCastleWarsDeathAfterMatch();
        testEnemyVersusOwnFlagClassification();
        testContinuousDoorAttackState();
        testAttackStyleDoorDamage();
        testHistoricalTicketOutcomes();
        testCatapultBurningStatus();
        System.out.println("Castle Wars audit fix checks passed");
    }

    /** A death from the arena must remain safe even after endGame clears gamePlayers. */
    @SuppressWarnings("unchecked")
    private static void testDeferredCastleWarsDeathAfterMatch() throws Exception {
        Player player = newPlayer(new Position(2426, 3087, 0));
        Map<Player, Team> participants =
                (Map<Player, Team>) field(CastleWarsManager.class, "gamePlayers").get(null);
        int playerIndex = 32;
        Player previous = World.players[playerIndex];
        Position lobby = new Position(2440, 3090, 0);
        int itemId = 4151; // An ordinary equipped/inventory item, never match-issued.
        int deathCount = player.deathCount;
        player.setIndex(playerIndex);
        World.players[playerIndex] = player;
        try {
            require(player.getInventoryManager().addItemPartial(new ItemStack(itemId, 1)) == 1,
                    "Could not give player an item before Castle Wars death");
            participants.put(player, Team.SARADOMIN);
            require(CastleWarsManager.isInGame(player), "Player should be an active participant");
            player.setDead(true);
            player.setCurrentHitpoints(0);
            DeathCleanupTask pendingDeath = new DeathCleanupTask(1, player, null);

            // Match ends during death animation: gamePlayers no longer has the
            // victim and endGame already teleported them to the Castle Wars lobby.
            participants.remove(player);
            player.setPosition(lobby.copy());
            require(!CastleWarsManager.isInGame(player), "Match must be over for regression");
            pendingDeath.execute();
            require(player.getPosition().equals(lobby), "Safe death moved player out of lobby");
            require(player.getInventoryManager().getItemAmount(itemId) == 1,
                    "Deferred Castle Wars death lost an ordinary inventory item");
            require(!player.isDead(), "Deferred safe-death cleanup left player dead");
            require(player.getCurrentHitpoints() == player.getMaxHitpoints(),
                    "Deferred safe-death cleanup did not restore health");
            require(player.deathCount == deathCount, "Deferred Castle Wars death counted as a normal death");
        } finally {
            participants.remove(player);
            World.players[playerIndex] = previous;
            player.setIndex(-1);
        }
    }

    @SuppressWarnings("unchecked")
    private static void testLogoutRemovesMatchIssuedSupplies() throws Exception {
        Player player = newPlayer(new Position(2425, 3076, 1));
        Map<Player, Team> participants =
                (Map<Player, Team>) field(CastleWarsManager.class, "gamePlayers").get(null);
        try {
            participants.put(player, Team.SARADOMIN);
            int pickaxe = CastleWarsEngineeringManager.BRONZE_PICKAXE_ID;
            int rock = CastleWarsEngineeringManager.ROCK_ITEM_ID;
            require(player.getInventoryManager().addItemPartial(new ItemStack(pickaxe, 1)) == 1,
                    "Cannot prepare player-owned pickaxe");
            require(CastleWarsEngineeringManager.issueSupply(player, pickaxe, 2) == 2,
                    "Cannot issue match pickaxes");
            require(CastleWarsEngineeringManager.issueSupply(player, rock, 2) == 2,
                    "Cannot issue match rocks");
            require(player.getInventoryManager().addItemPartial(new ItemStack(4049, 3)) == 3,
                    "Cannot prepare match bandages");

            CastleWarsManager.handlePlayerLogout(player);
            require(amount(player, pickaxe) == 1, "Logout removed a player-owned pickaxe");
            require(amount(player, rock) == 0, "Logout retained issued catapult rocks");
            require(amount(player, 4049) == 0, "Logout retained match bandages");
            CastleWarsEngineeringManager.cleanupPlayerSupplies(player);
            require(amount(player, pickaxe) == 1, "Cleanup must remain idempotent");
        } finally {
            participants.remove(player);
        }
    }

    @SuppressWarnings("unchecked")
    private static void testEnemyVersusOwnFlagClassification() throws Exception {
        Field sarah = field(CastleWarsManager.class, "saradominFlagHolder");
        Field zammy = field(CastleWarsManager.class, "zamorakFlagHolder");
        Object oldSarah = sarah.get(null);
        Object oldZammy = zammy.get(null);
        Player player = newPlayer(new Position(2412, 3100, 0));
        Map<Player, Team> participants =
                (Map<Player, Team>) field(CastleWarsManager.class, "gamePlayers").get(null);
        try {
            participants.put(player, Team.SARADOMIN);
            sarah.set(null, player);
            zammy.set(null, null);
            require(CastleWarsManager.isCarryingFlag(player), "Own flag should be carried");
            require(CastleWarsManager.isCarryingOwnFlag(player), "Own flag should be classified correctly");
            require(!CastleWarsManager.isCarryingEnemyFlag(player),
                    "Own-team standard must not trigger enemy-flag logic");

            sarah.set(null, null);
            zammy.set(null, player);
            require(CastleWarsManager.isCarryingEnemyFlag(player),
                    "Enemy standard must trigger enemy-flag logic");
            require(!CastleWarsManager.isCarryingOwnFlag(player),
                    "Enemy standard was mistaken for own");
        } finally {
            participants.remove(player);
            sarah.set(null, oldSarah);
            zammy.set(null, oldZammy);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void testContinuousDoorAttackState() throws Exception {
        Player player = newPlayer(new Position(2426, 3087, 0));
        int doorId = CastleWarsEngineeringManager.SARADOMIN_MAIN_DOOR_LEFT_CLOSED_ID;
        int worldIndex = 31;
        Player previousWorldPlayer = World.players[worldIndex];
        Map<Player, Team> participants =
                (Map<Player, Team>) field(CastleWarsManager.class, "gamePlayers").get(null);
        Object door = field(CastleWarsEngineeringManager.class, "saradominMainDoor").get(null);
        Field mode = field(door.getClass(), "mode");
        Object previousMode = mode.get(door);
        player.setIndex(worldIndex);
        World.players[worldIndex] = player;
        try {
            participants.put(player, Team.ZAMORAK);
            Class<? extends Enum> modeType = (Class<? extends Enum>) mode.getType();
            mode.set(door, Enum.valueOf(modeType, "CLOSED"));
            require(CastleWarsEngineeringManager.canContinueMainDoorAttack(
                    player, doorId, 2426, 3088),
                    "Enemy should maintain an attack while the door is closed");
            mode.set(door, Enum.valueOf(modeType, "BROKEN"));
            require(!CastleWarsEngineeringManager.canContinueMainDoorAttack(
                    player, doorId, 2426, 3088),
                    "Breaking a door must terminate the repeated attack");
            mode.set(door, Enum.valueOf(modeType, "OPEN"));
            require(!CastleWarsEngineeringManager.canContinueMainDoorAttack(
                    player, doorId, 2426, 3088),
                    "Opening a door must terminate the repeated attack");
            mode.set(door, Enum.valueOf(modeType, "CLOSED"));
            participants.put(player, Team.SARADOMIN);
            require(!CastleWarsEngineeringManager.canContinueMainDoorAttack(
                    player, doorId, 2426, 3088),
                    "Defenders cannot attack their own doors");
        } finally {
            mode.set(door, previousMode);
            participants.remove(player);
            World.players[worldIndex] = previousWorldPlayer;
            player.setIndex(-1);
        }
    }

    private static void testAttackStyleDoorDamage() {
        int stab = CastleWarsEngineeringManager.calculateMainDoorStyleDamage(20, AttackBonusType.STAB);
        int slash = CastleWarsEngineeringManager.calculateMainDoorStyleDamage(20, AttackBonusType.SLASH);
        int crush = CastleWarsEngineeringManager.calculateMainDoorStyleDamage(20, AttackBonusType.CRUSH);
        require(stab == 10 && slash == 15 && crush == 30,
                "Style damage multipliers changed unexpectedly");
        require(stab < slash && slash < crush,
                "Historical style effectiveness must be crush > slash > stab");
        require(CastleWarsEngineeringManager.calculateMainDoorStyleDamage(0, AttackBonusType.CRUSH) == 0,
                "A miss must never damage a door");
    }

    private static void testHistoricalTicketOutcomes() {
        require(reward(1, 0) == 3, "Shutout victory");
        require(reward(2, 1) == 2, "Normal victory");
        require(reward(0, 0) == 2, "Scoreless draw");
        require(reward(1, 1) == 1, "Scored draw");
        require(reward(0, 1) == 0, "Defeat");
        require(reward(1, 2) == 0, "Scored defeat");
        boolean rejected = false;
        try {
            reward(-1, 0);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        require(rejected, "Negative score should be rejected");
    }

    private static int reward(int ours, int theirs) {
        return CastleWarsManager.calculateCastleWarsTicketReward(ours, theirs);
    }

    private static void testCatapultBurningStatus() throws Exception {
        Field operational = field(CastleWarsEngineeringManager.class, "saradominCatapultOperational");
        Field burning = field(CastleWarsEngineeringManager.class, "saradominCatapultBurning");
        boolean oldOperational = operational.getBoolean(null);
        boolean oldBurning = burning.getBoolean(null);
        try {
            operational.setBoolean(null, true);
            burning.setBoolean(null, false);
            require("Operational".equals(CastleWarsEngineeringManager.getCatapultStatusText(Team.SARADOMIN)),
                    "Operational catapult status");
            burning.setBoolean(null, true);
            require("Burning".equals(CastleWarsEngineeringManager.getCatapultStatusText(Team.SARADOMIN)),
                    "Burning catapult must not display as operational");
            operational.setBoolean(null, false);
            require("Destroyed".equals(CastleWarsEngineeringManager.getCatapultStatusText(Team.SARADOMIN)),
                    "Destroyed catapult status");
        } finally {
            operational.setBoolean(null, oldOperational);
            burning.setBoolean(null, oldBurning);
        }
    }

    private static Player newPlayer(Position position) {
        Player player = new Player(null);
        player.isBot = true;
        player.setPosition(position);
        return player;
    }

    private static int amount(Player player, int itemId) {
        return player.getInventoryManager().getItemAmount(itemId);
    }

    private static Field field(Class<?> type, String name) throws Exception {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static void require(boolean ok, String message) {
        if (!ok) {
            throw new AssertionError(message);
        }
    }
}
