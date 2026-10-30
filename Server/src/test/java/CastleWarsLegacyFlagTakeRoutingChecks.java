import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager.Team;
import com.rs2.model.player.Player;
import com.rs2.model.task.TickTask;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.handler.PlayerInteractionPacketHandler;
import com.rs2.net.packet.handler.TakeFromTeammateFlagTask;
import com.rs2.net.packet.handler.TradeRequestTask;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

/** Regression checks for legacy player option 4 and native 443 flag-take routing. */
public final class CastleWarsLegacyFlagTakeRoutingChecks {
    private static final int LEGACY_FOURTH_PLAYER_OPTION = 139;
    private static final int TAKER_INDEX = 1;
    private static final int CARRIER_INDEX = 2;
    private static final int OPPONENT_INDEX = 3;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        int originalBuild = ServerSettings.clientBuild;
        Field progressField = field(CastleWarsManager.class, "gameInProgress");
        Field holderField = field(CastleWarsManager.class, "zamorakFlagHolder");
        Field heldSinceField = field(CastleWarsManager.class, "zamorakFlagHeldSinceMillis");
        Map<Player, Team> teams = (Map<Player, Team>) field(
                CastleWarsManager.class, "gamePlayers").get(null);

        boolean originalProgress = progressField.getBoolean(null);
        Object originalHolder = holderField.get(null);
        long originalHeldSince = heldSinceField.getLong(null);

        Player taker = inventoryPlayer(TAKER_INDEX, new Position(2413, 3088, 0));
        Player carrier = inventoryPlayer(CARRIER_INDEX, new Position(2413, 3089, 0));
        Player opponent = inventoryPlayer(OPPONENT_INDEX, new Position(2414, 3089, 0));
        Player[] originalPlayers = {
                World.players[TAKER_INDEX], World.players[CARRIER_INDEX],
                World.players[OPPONENT_INDEX]
        };
        List<?> scheduled = World.getTaskScheduler().getTasks();
        int initialTaskCount = scheduled.size();

        try {
            World.players[TAKER_INDEX] = taker;
            World.players[CARRIER_INDEX] = carrier;
            World.players[OPPONENT_INDEX] = opponent;
            teams.put(taker, Team.SARADOMIN);
            teams.put(carrier, Team.SARADOMIN);
            teams.put(opponent, Team.ZAMORAK);
            progressField.setBoolean(null, true);
            holderField.set(null, carrier);
            long now = System.currentTimeMillis();
            long eligibleSince = now - CastleWarsManager.TEAMMATE_FLAG_TAKE_DELAY_MILLIS - 5_000L;
            heldSinceField.setLong(null, eligibleSince);
            taker.playerOptionTextCache[3] = "Take from";

            // The legacy option 4 sends opcode 139 and a little-endian target
            // index. It must schedule the same task as the 443 native option.
            ServerSettings.clientBuild = 377;
            TickTask legacyTask = assertQueued(taker,
                    legacyOption4(CARRIER_INDEX), TakeFromTeammateFlagTask.class);
            require(taker.getInteractionTarget() == carrier
                            && taker.getMovementTarget() == carrier,
                    "Legacy flag take did not walk toward the carrier");

            // Revalidate on task execution: a flag moved after clicking cannot
            // still be taken merely because the interaction was queued.
            holderField.set(null, null);
            legacyTask.execute();
            require(taker.getInteractionTarget() == null
                            && taker.getMovementTarget() == null,
                    "Cancelled legacy flag take left a stale movement target");
            holderField.set(null, carrier);

            // Three-minute timer and teammate checks prevent forged clicks.
            heldSinceField.setLong(null, now - 1_000L);
            assertRejected(taker, legacyOption4(CARRIER_INDEX));
            heldSinceField.setLong(null, eligibleSince);
            assertRejected(taker, legacyOption4(OPPONENT_INDEX));
            teams.put(carrier, Team.ZAMORAK);
            assertRejected(taker, legacyOption4(CARRIER_INDEX));
            teams.put(carrier, Team.SARADOMIN);

            carrier.setPosition(new Position(2450, 3120, 0));
            assertRejected(taker, legacyOption4(CARRIER_INDEX));
            carrier.setPosition(new Position(2413, 3089, 0));

            progressField.setBoolean(null, false);
            assertRejected(taker, legacyOption4(CARRIER_INDEX));
            progressField.setBoolean(null, true);
            assertRejected(taker, legacyOption4(World.players.length));

            // Ordinary trade requests still use opcode 139 when option 4 is
            // "Trade with"; they must not be hijacked by Castle Wars.
            taker.playerOptionTextCache[3] = "Trade with";
            assertQueued(taker, legacyOption4(CARRIER_INDEX), TradeRequestTask.class);
            taker.playerOptionTextCache[3] = "Take from";

            ServerSettings.clientBuild = 443;
            assertQueued(taker, nativeOption4(CARRIER_INDEX),
                    TakeFromTeammateFlagTask.class);
            heldSinceField.setLong(null, now - 1_000L);
            assertRejected(taker, nativeOption4(CARRIER_INDEX));

            System.out.println("Castle Wars legacy 139 and native 443 teammate flag routing OK");
        } finally {
            while (scheduled.size() > initialTaskCount) {
                scheduled.remove(scheduled.size() - 1);
            }
            teams.remove(taker);
            teams.remove(carrier);
            teams.remove(opponent);
            World.players[TAKER_INDEX] = originalPlayers[0];
            World.players[CARRIER_INDEX] = originalPlayers[1];
            World.players[OPPONENT_INDEX] = originalPlayers[2];
            progressField.setBoolean(null, originalProgress);
            holderField.set(null, originalHolder);
            heldSinceField.setLong(null, originalHeldSince);
            ServerSettings.clientBuild = originalBuild;
        }
    }

    private static Player inventoryPlayer(int index, Position position) {
        Player player = new Player(null);
        player.isBot = true; // Packet sender skips network writes for this fixture.
        player.setIndex(index);
        // In a live session player entity IDs have the high player bit set.
        // Without this, Entity.setInteractionTarget casts the fixture as an NPC.
        player.setEncodedIndex(32768 + index);
        player.setPosition(position);
        return player;
    }

    private static IncomingPacket legacyOption4(int index) {
        ByteBuffer payload = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        payload.putShort((short) index);
        payload.flip();
        return new IncomingPacket(LEGACY_FOURTH_PLAYER_OPTION, 2,
                PacketBuffer.wrapReader(payload));
    }

    private static IncomingPacket nativeOption4(int index) {
        ByteBuffer payload = ByteBuffer.allocate(2);
        // Revision 443 PLAYER_OPTION_4 reads low-byte ADD and then high byte.
        payload.put((byte) ((index & 255) + 128));
        payload.put((byte) (index >>> 8));
        payload.flip();
        return new IncomingPacket(ClientPackets.PLAYER_OPTION_4, 2,
                PacketBuffer.wrapReader(payload));
    }

    private static TickTask assertQueued(Player actor, IncomingPacket packet,
                                         Class<? extends TickTask> expectedType) {
        List<?> tasks = World.getTaskScheduler().getTasks();
        int before = tasks.size();
        new PlayerInteractionPacketHandler().handle(actor, packet);
        require(tasks.size() == before + 1,
                "Expected one task for opcode " + packet.getOpcode());
        Object task = tasks.get(before);
        require(expectedType.isInstance(task),
                "Wrong task for opcode " + packet.getOpcode() + ": " + task.getClass());
        tasks.remove(before);
        return (TickTask) task;
    }

    private static void assertRejected(Player actor, IncomingPacket packet) {
        List<?> tasks = World.getTaskScheduler().getTasks();
        int before = tasks.size();
        new PlayerInteractionPacketHandler().handle(actor, packet);
        require(tasks.size() == before,
                "Invalid flag-take packet queued an interaction task");
        require(actor.getInteractionTarget() == null,
                "Invalid flag-take packet kept a target");
    }

    private static Field field(Class<?> type, String name) throws Exception {
        Field result = type.getDeclaredField(name);
        result.setAccessible(true);
        return result;
    }

    private static void require(boolean assertion, String message) {
        if (!assertion) {
            throw new AssertionError(message);
        }
    }
}
