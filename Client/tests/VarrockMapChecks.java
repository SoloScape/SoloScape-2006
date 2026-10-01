import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.cache.js5.MapGroups;
import jagex.io.Buffer;
import unpackaged.ObjectDefinition;
import java.io.File;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/** Run from Server with both client/server classes and Server/lib on the classpath. */
public final class VarrockMapChecks {
    public static void main(String[] args) throws Exception {
        try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
            Map<Integer, byte[]> definitions = store.readFiles(2, 6);
            Set<Integer> models = new HashSet<Integer>();
            for (int model : store.readReferenceTable(7).getGroupIds()) models.add(model);
            boolean bankWall = false, bankBooth = false;
            int[][] squares = {{49, 53, 1000}, {49, 54, 300}, {50, 54, 1500}, {51, 54, 600}};
            for (int[] square : squares) {
                Buffer buffer = new Buffer(MapGroups.readLocations(store,
                        store.readReferenceTable(5), square[0], square[1]));
                int id = -1, walls = 0;
                for (;;) {
                    int delta = buffer.getSmartB();
                    if (delta == 0) break;
                    id += delta;
                    byte[] bytes = definitions.get(id);
                    check(bytes != null, "Missing 443 object definition " + id);
                    ObjectDefinition definition = new ObjectDefinition();
                    definition.decode(new Buffer(bytes));
                    if (definition.anIntArray1954 != null) {
                        for (int model : definition.anIntArray1954)
                            check(models.contains(model), "Missing 443 model " + model);
                    }
                    int position = 0;
                    for (;;) {
                        int step = buffer.getSmartB();
                        if (step == 0) break;
                        position += step - 1;
                        int type = buffer.getUbyte() >> 2;
                        if (type <= 3) walls++;
                        int x = square[0] * 64 + (position >> 6 & 63);
                        int y = square[1] * 64 + (position & 63);
                        if (position >> 12 == 0 && id == 1902 && type == 0
                                && x == 3180 && y == 3434) bankWall = true;
                        if (position >> 12 == 0 && id == 2213 && type == 10
                                && x == 3186 && y == 3436) bankBooth = true;
                    }
                }
                check(walls >= square[2], "Incomplete Varrock walls in "
                        + square[0] + "_" + square[1] + ": " + walls);
                check(buffer.offset == buffer.payload.length, "Trailing map bytes");
                System.out.println(square[0] + "_" + square[1] + ": " + walls + " walls");
            }
            check(bankWall && bankBooth, "West bank wall or booth is missing");
        }
        System.out.println("Varrock map checks passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
