import com.rs2.model.Position;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.Team;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.objects.ObjectDefinition;
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
        ObjectDefinition[] oldDefinitions = ObjectDefinition.definitionsById;
        if (oldDefinitions == null) {
            // The isolated test does not run the game cache bootstrap.
            ObjectDefinition.definitionsById = new ObjectDefinition[10000];
        }
        try {
            testTicketClaims();
            testBucketSpawnsAndFirefighting();
            System.out.println("Castle Wars ticket claims and bucket supplies OK");
        } finally {
            ObjectDefinition.definitionsById = oldDefinitions;
        }
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
        require(player.getInventoryManager().removeItem(new ItemStack(EMPTY, 1)),
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
        require(positions.length == 3, "Expected one Saradomin and two Zamorak bucket pickups");
        require(positions[0].equals(new Position(2425, 3077, 0)),
                "Saradomin bucket must be downstairs, not in the respawn room");
        require(positions[1].equals(new Position(2371, 3127, 0)),
                "First Zamorak bucket must spawn at 2371,3127,0");
        require(positions[2].equals(new Position(2368, 3131, 0)),
                "Second Zamorak bucket must spawn at 2368,3131,0");
        Field tinderboxPositionsField = field(engine, "TINDERBOX_SPAWN_POSITIONS");
        Position[] tinderboxPositions = (Position[]) tinderboxPositionsField.get(null);
        require(tinderboxPositions.length == 1
                        && tinderboxPositions[0].equals(new Position(2368, 3135, 0)),
                "Zamorak tinderbox must spawn at 2368,3135,0");
        for (Position position : positions) {
            require(position.getPlane() == 0,
                    "No bucket may respawn in the upstairs team barrier/death area");
        }

        Method reset = engine.getDeclaredMethod("resetBucketSupplies");
        reset.setAccessible(true);
        Method clear = engine.getDeclaredMethod("clearBucketSupplies");
        clear.setAccessible(true);
        Field spawnsField = field(engine, "bucketSupplySpawns");
        Field tinderboxSpawnsField = field(engine, "tinderboxSupplySpawns");
        try {
            reset.invoke(null);
            List<GroundItem> spawns = (List<GroundItem>) spawnsField.get(null);
            require(spawns.size() == positions.length, "Not all team buckets spawned");
            for (GroundItem spawned : spawns) {
                require(spawned.getItem().getId() == EMPTY
                                && GroundItemManager.getInstance().contains(spawned),
                        "Empty bucket was not available on the ground");
            }
            List<GroundItem> tinderboxes = (List<GroundItem>) tinderboxSpawnsField.get(null);
            require(tinderboxes.size() == 1, "Wrong tinderbox spawn count");
            require(tinderboxes.get(0).getItem().getId()
                            == CastleWarsEngineeringManager.TINDERBOX_ITEM_ID
                    && tinderboxes.get(0).getPosition().equals(new Position(2368, 3135, 0))
                    && GroundItemManager.getInstance().contains(tinderboxes.get(0)),
                    "Respawning ground-floor tinderbox missing");
        } finally {
            clear.invoke(null);
        }

        Player player = botPlayer(new Position(2414, 3088, 0));
        require(player.getInventoryManager().addItem(new ItemStack(EMPTY, 1)),
                "Could not obtain an empty bucket for the water source test");
        require(player.getCookingManager().handleWaterSourceItem(EMPTY, 4482),
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
        // getGameTeam() evicts disconnected participants; register this fixture
        // in World.players as the live game loop does.
        final int fixtureIndex = 31;
        Player previousPlayer = com.rs2.model.World.players[fixtureIndex];
        player.setIndex(fixtureIndex);
        com.rs2.model.World.players[fixtureIndex] = player;
        teams.put(player, Team.ZAMORAK);
        require(player.getCookingManager().handleWaterSourceItem(EMPTY, 4482),
                "Could not refill bucket for catapult ownership test");
        burning.setBoolean(null, true);
        try {
            require(CastleWarsEngineeringManager.extinguishOwnCatapult(
                            player, CastleWarsEngineeringManager.SARADOMIN_BURNING_CATAPULT_ID),
                    "Enemy catapult ownership was not handled");
            require(player.getInventoryManager().getItemAmount(WATER) == 1,
                    "Opponent may not spend water on an enemy catapult");
            teams.put(player, Team.SARADOMIN);
            require(CastleWarsEngineeringManager.extinguishOwnCatapult(
                            player, CastleWarsEngineeringManager.SARADOMIN_BURNING_CATAPULT_ID),
                    "Owner could not extinguish burning catapult");
            require(!burning.getBoolean(null)
                            && player.getInventoryManager().getItemAmount(EMPTY) == 1,
                    "Saradomin catapult firefighting failed to return an empty bucket");

            // Mirror the whole refill/extinguish action for Zamorak's catapult.
            Field zamorakBurning = field(engine, "zamorakCatapultBurning");
            boolean originalZamorakBurning = zamorakBurning.getBoolean(null);
            try {
                player.setPosition(CastleWarsEngineeringManager.ZAMORAK_CATAPULT.copy());
                teams.put(player, Team.ZAMORAK);
                require(player.getCookingManager().handleWaterSourceItem(EMPTY, 4482),
                        "Zamorak team could not refill an empty bucket");
                zamorakBurning.setBoolean(null, true);
                require(CastleWarsEngineeringManager.extinguishOwnCatapult(
                                player, CastleWarsEngineeringManager.ZAMORAK_BURNING_CATAPULT_ID),
                        "Zamorak team could not extinguish its burning catapult");
                require(!zamorakBurning.getBoolean(null)
                                && player.getInventoryManager().getItemAmount(WATER) == 0
                                && player.getInventoryManager().getItemAmount(EMPTY) == 1,
                        "Zamorak catapult firefighting failed to return the bucket");
            } finally {
                zamorakBurning.setBoolean(null, originalZamorakBurning);
            }
        } finally {
            burning.setBoolean(null, original);
            teams.remove(player);
            com.rs2.model.World.players[fixtureIndex] = previousPlayer;
            player.setIndex(-1);
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
