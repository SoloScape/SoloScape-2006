import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.Team;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.CharacterFileRecord;
import com.rs2.model.player.Player;
import com.rs2.util.CharacterJsonCodec;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/** Verify full-inventory ticket claims and both teams' bucket firefighting supply. */
public final class CastleWarsTicketAndBucketChecks {
    private static final int TICKETS = CastleWarsManager.CASTLE_WARS_TICKET_ID;
    private static final int EMPTY = CastleWarsEngineeringManager.EMPTY_BUCKET_ITEM_ID;
    private static final int WATER = CastleWarsEngineeringManager.BUCKET_OF_WATER_ITEM_ID;

    public static void main(String[] args) throws Exception {
        testTicketClaims();
        testBucketSpawnsAndFirefighting();
        System.out.println("Castle Wars ticket claims and bucket supplies OK");
    }

    private static void testTicketClaims() throws Exception {
        Player player = botPlayer(new Position(2440, 3090, 0));
        require(player.getInventoryManager().addItemPartial(new ItemStack(EMPTY, 28)) == 28,
                "Cannot fill inventory with buckets");
        require(player.getInventoryManager().getContainer().getFreeSlots() == 0,
                "Ticket-loss regression needs a truly full inventory");

        require(!CastleWarsManager.awardCastleWarsTickets(player, 2),
                "Ticket award unexpectedly fit in full inventory");
        require(player.reservedSaveInt3 == 2,
                "Lost tickets were not stored for later collection");
        require(player.getInventoryManager().getItemAmount(TICKETS) == 0,
                "Failed delivery added tickets anyway");
        require(!CastleWarsManager.claimPendingCastleWarsTickets(player)
                        && player.reservedSaveInt3 == 2,
                "Full inventory must not clear a pending ticket claim");

        require(!CastleWarsManager.awardCastleWarsTickets(player, 1)
                        && player.reservedSaveInt3 == 3,
                "Multiple full-inventory rewards must accumulate");
        require(player.getInventoryManager().removeItem(new ItemStack(EMPTY, 1)),
                "Could not free one space to claim tickets");
        // These legacy tickets are non-stackable. A single empty slot only
        // delivers one ticket and the rest must stay pending.
        require(!CastleWarsManager.claimPendingCastleWarsTickets(player)
                        && player.reservedSaveInt3 == 2
                        && player.getInventoryManager().getItemAmount(TICKETS) == 1,
                "Partial claim must leave the remaining tickets pending");
        require(player.getInventoryManager().removeItem(new ItemStack(EMPTY, 2)),
                "Could not free the remaining ticket claim slots");
        require(CastleWarsManager.claimPendingCastleWarsTickets(player),
                "Claiming pending tickets with sufficient space failed");
        require(player.getInventoryManager().getItemAmount(TICKETS) == 3
                        && player.reservedSaveInt3 == 0,
                "Claim did not deliver all tickets and clear pending balance");
        require(!CastleWarsManager.claimPendingCastleWarsTickets(player)
                        && player.getInventoryManager().getItemAmount(TICKETS) == 3,
                "Repeated claim duplicated tickets");

        // The already-owned ticket items do not create a free stack; these
        // tickets are non-stackable even when the inventory contains some.
        require(player.getInventoryManager().addItem(new ItemStack(EMPTY, 3)),
                "Could not restore the full inventory");
        require(player.getInventoryManager().getContainer().getFreeSlots() == 0,
                "Test did not restore a full inventory");
        require(!CastleWarsManager.awardCastleWarsTickets(player, 2)
                        && player.reservedSaveInt3 == 2
                        && player.getInventoryManager().getItemAmount(TICKETS) == 3,
                "Reward was lost or duplicated in an already ticket-owning full inventory");
        require(player.getInventoryManager().removeItem(new ItemStack(EMPTY, 2)),
                "Could not free space for the second claim");
        require(CastleWarsManager.claimPendingCastleWarsTickets(player)
                        && player.getInventoryManager().getItemAmount(TICKETS) == 5
                        && player.reservedSaveInt3 == 0,
                "Second claim did not deliver every ticket");

        // A partially successful match reward must also credit only its
        // undelivered remainder, not all-or-nothing.
        require(player.getInventoryManager().addItem(new ItemStack(EMPTY, 1)),
                "Could not prepare one free inventory slot");
        require(!CastleWarsManager.awardCastleWarsTickets(player, 2)
                        && player.reservedSaveInt3 == 1
                        && player.getInventoryManager().getItemAmount(TICKETS) == 6,
                "Partial match reward was not correctly split into delivered and pending");
        require(player.getInventoryManager().removeItem(new ItemStack(EMPTY, 1)),
                "Could not free space for the last ticket");
        require(CastleWarsManager.claimPendingCastleWarsTickets(player)
                        && player.reservedSaveInt3 == 0
                        && player.getInventoryManager().getItemAmount(TICKETS) == 7,
                "Partial reward remainder could not be claimed");

        // The claimed balance is an existing versioned character-save field.
        // Verify the actual JSON codec retains a nonzero pending claim.
        File file = Files.createTempFile("castlewars-pending-tickets-", ".json").toFile();
        try {
            CharacterFileRecord record = new CharacterFileRecord();
            record.username = "CastleWarsTest";
            record.reservedSaveInt3 = 7;
            CharacterJsonCodec.save(file, record);
            CharacterFileRecord loaded = CharacterJsonCodec.read(file, record.username);
            require(loaded.reservedSaveInt3 == 7,
                    "Unclaimed tickets did not survive a JSON save/load");
        } finally {
            Files.deleteIfExists(file.toPath());
            Files.deleteIfExists(new File(file.getPath() + ".bak").toPath());
        }
    }

    @SuppressWarnings("unchecked")
    private static void testBucketSpawnsAndFirefighting() throws Exception {
        Class<?> engine = CastleWarsEngineeringManager.class;
        Field positionsField = field(engine, "BUCKET_SPAWN_POSITIONS");
        Position[] positions = (Position[]) positionsField.get(null);
        require(positions.length >= 2, "Each team needs an empty-bucket supply");
        require(inside(positions[0], 2424, 2428, 3075, 3078, 1),
                "Saradomin bucket not in the team spawn-room area");
        require(inside(positions[1], 2370, 2374, 3128, 3131, 1),
                "Zamorak bucket not in the team spawn-room area");

        Method reset = engine.getDeclaredMethod("resetBucketSupplies");
        reset.setAccessible(true);
        Method clear = engine.getDeclaredMethod("clearBucketSupplies");
        clear.setAccessible(true);
        Field spawnsField = field(engine, "bucketSupplySpawns");
        try {
            reset.invoke(null);
            List<GroundItem> spawns = (List<GroundItem>) spawnsField.get(null);
            require(spawns.size() == positions.length, "Not all team buckets spawned");
            for (GroundItem spawned : spawns) {
                require(spawned.getItem().getId() == EMPTY
                                && GroundItemManager.getInstance().contains(spawned),
                        "Empty bucket was not available on the ground");
            }
        } finally {
            clear.invoke(null);
        }

        Player player = botPlayer(new Position(2414, 3088, 0));
        require(player.getInventoryManager().addItem(new ItemStack(EMPTY, 1)),
                "Could not obtain an empty bucket for the water source test");
        require(player.getCookingManager().handleWaterSourceItem(EMPTY, 879),
                "Generic fountain/water-source route did not fill a bucket");
        require(player.getInventoryManager().getItemAmount(WATER) == 1
                        && player.getInventoryManager().getItemAmount(EMPTY) == 0,
                "Empty bucket did not become a bucket of water");

        // A burning barricade can be extinguished and the empty bucket returned.
        Map<String, Object> barricades = (Map<String, Object>) field(engine, "barricades").get(null);
        Position barricadePos = new Position(2415, 3088, 0);
        Class<?> stateType = Class.forName(
                "com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager$BarricadeState");
        Constructor<?> constructor = stateType.getDeclaredConstructor(Team.class, Position.class, int.class);
        constructor.setAccessible(true);
        Object state = constructor.newInstance(Team.SARADOMIN, barricadePos,
                CastleWarsEngineeringManager.BARRICADE_OBJECT_SARADOMIN);
        field(stateType, "burning").setBoolean(state, true);
        String key = barricadePos.getX() + ":" + barricadePos.getY() + ":" + barricadePos.getPlane();
        Object previous = barricades.put(key, state);
        try {
            require(CastleWarsEngineeringManager.extinguishBarricade(player, barricadePos),
                    "Water bucket did not extinguish burning barricade");
            require(!field(stateType, "burning").getBoolean(state)
                            && player.getInventoryManager().getItemAmount(WATER) == 0
                            && player.getInventoryManager().getItemAmount(EMPTY) == 1,
                    "Barricade firefighting failed to return an empty bucket");
        } finally {
            barricades.remove(key);
            if (previous != null) barricades.put(key, previous);
        }

        // Catapult firefighting has an independent team-ownership check.
        Field burning = field(engine, "saradominCatapultBurning");
        boolean original = burning.getBoolean(null);
        Field teamsField = field(CastleWarsManager.class, "gamePlayers");
        Map<Player, Team> teams = (Map<Player, Team>) teamsField.get(null);
        player.setPosition(CastleWarsEngineeringManager.SARADOMIN_CATAPULT.copy());
        teams.put(player, Team.ZAMORAK);
        burning.setBoolean(null, true);
        try {
            require(CastleWarsEngineeringManager.extinguishOwnCatapult(
                            player, CastleWarsEngineeringManager.SARADOMIN_BURNING_CATAPULT_ID),
                    "Enemy catapult ownership was not handled");
            require(player.getInventoryManager().getItemAmount(EMPTY) == 1,
                    "Opponent may not spend a bucket on an enemy catapult");
            teams.put(player, Team.SARADOMIN);
            require(player.getCookingManager().handleWaterSourceItem(EMPTY, 879),
                    "Could not refill bucket for catapult");
            require(CastleWarsEngineeringManager.extinguishOwnCatapult(
                            player, CastleWarsEngineeringManager.SARADOMIN_BURNING_CATAPULT_ID),
                    "Owner could not extinguish burning catapult");
            require(!burning.getBoolean(null)
                            && player.getInventoryManager().getItemAmount(EMPTY) == 1,
                    "Catapult firefighting failed to return an empty bucket");
        } finally {
            burning.setBoolean(null, original);
            teams.remove(player);
        }
    }

    private static boolean inside(Position p, int x1, int x2, int y1, int y2, int z) {
        return p.getPlane() == z && p.getX() >= x1 && p.getX() <= x2
                && p.getY() >= y1 && p.getY() <= y2;
    }

    private static Player botPlayer(Position position) {
        Player p = new Player(null);
        p.isBot = true;
        p.setPosition(position);
        p.setEncodedIndex(32769);
        return p;
    }

    private static Field field(Class<?> type, String name) throws Exception {
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static void require(boolean check, String message) {
        if (!check) throw new AssertionError(message);
    }
}
