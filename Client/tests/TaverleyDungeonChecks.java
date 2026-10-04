import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.cache.js5.MapGroups;
import com.rs2.model.Position;
import com.rs2.model.interaction.FirstObjectActionTask;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import com.rs2.util.path.WalkingCollisionMap;
import jagex.io.Buffer;
import unpackaged.ObjectDefinition;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;
import java.util.Map;

/** Run from Server with client/server classes and Server/lib on the classpath. */
public final class TaverleyDungeonChecks {
    public static void main(String[] args) throws Exception {
        try {
            try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
                Map<Integer, byte[]> definitions = store.readFiles(2, 6);
                checkMap(store, definitions, 53, 1759);
                checkMap(store, definitions, 153, 1755);
            }
            WorldObjectLookup.loadWorldObjects();
            WalkingCollisionMap.loadCollisionMaps();
            QuestDefinition.loadDefinitions();
            // The generic object router probes Castle Wars for every click.
            // Keep this isolated check from spawning its unrelated lobby NPC.
            Field castleWars = com.rs2.model.gameplay.castlewars.CastleWarsManager.class
                    .getDeclaredField("initialized");
            castleWars.setAccessible(true);
            castleWars.setBoolean(null, true);
            Player player = new Player(null);
            player.setIndex(0);
            player.setEncodedIndex(32768);
            player.setOutboundCipher(new IsaacCipher(new int[4]));
            SocketChannel transport = SocketChannel.open();
            transport.close();
            Field socket = Player.class.getDeclaredField("socketChannel");
            socket.setAccessible(true);
            socket.set(player, transport);
            player.setQuestState(0, 100);
            player.setPosition(new Position(2884, 3398, 0));
            climb(player, 1759, 3397, 9798);
            climb(player, 1755, 9797, 3398);
            System.out.println("Taverley dungeon checks passed: visible ladders and round-trip travel.");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkMap(Js5CacheStore store, Map<Integer, byte[]> definitions,
                                 int squareY, int ladderId) throws Exception {
        Buffer buffer = new Buffer(MapGroups.readLocations(store,
                store.readReferenceTable(5), 45, squareY));
        int id = -1, matches = 0;
        for (;;) {
            int delta = buffer.getSmartB();
            if (delta == 0) break;
            id += delta;
            int position = 0;
            for (;;) {
                int step = buffer.getSmartB();
                if (step == 0) break;
                position += step - 1;
                int info = buffer.getUbyte();
                if (id != ladderId) continue;
                require(position == (4 << 6 | 5) && info == 40,
                        "Unexpected ladder placement or shape");
                matches++;
            }
        }
        require(matches == 1 && buffer.offset == buffer.payload.length,
                "Missing/duplicate ladder or malformed map stream");
        ObjectDefinition definition = new ObjectDefinition();
        definition.decode(new Buffer(definitions.get(ladderId)));
        require(definition.anIntArray1954 != null, "Ladder has no visible model");
        if (definition.anIntArray1960 != null) {
            boolean supports = false;
            for (int shape : definition.anIntArray1960) supports |= shape == 10;
            require(supports, "Ladder definition cannot render type 10");
        }
        for (int model : definition.anIntArray1954)
            require(store.readFiles(7, model).get(0) != null, "Missing ladder model");
        String expected = squareY == 53 ? "Climb-Down" : "Climb-up";
        require(definition.aClass3Array1964[0] != null && expected.equals(new String(
                definition.aClass3Array1964[0].bytes, 0,
                definition.aClass3Array1964[0].length)), "Missing ladder click option");
    }

    private static void climb(Player player, int id, int objectY, int destinationY) {
        require(WorldObjectLookup.findObjectByIdAt(id, 2884, objectY, 0) != null,
                "Server cannot find ladder");
        player.setInteractionTargetId(id);
        player.setInteractionTargetX(2884);
        player.setInteractionTargetY(objectY);
        player.setInteractionTargetPlane(0);
        int sequence = player.nextActionSequence();
        new FirstObjectActionTask(1, true, player, sequence, id, 2884, objectY, 0,
                "Ladder").execute();
        require(player.isActionLocked(), "Ladder click did not start climbing");
        for (int tick = 0; tick < 4; tick++) CycleEventHandler.getInstance().process();
        require(player.getPosition().getX() == 2884
                && player.getPosition().getY() == destinationY
                && player.getPosition().getPlane() == 0 && !player.isActionLocked(),
                "Ladder did not finish travel: " + player.getPosition().getX() + ","
                + player.getPosition().getY() + "," + player.getPosition().getPlane()
                + " locked=" + player.isActionLocked());
        require((WalkingCollisionMap.getTileFlags(2884, destinationY, 0) & 256) == 0,
                "Ladder landed inside a blocking object");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
