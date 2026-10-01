import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.cache.js5.Js5ReferenceTable;
import com.rs2.cache.js5.MapGroups;
import com.rs2.cache.js5.XteaKeys;
import jagex.io.Buffer;
import unpackaged.Model;
import unpackaged.ObjectDefinition;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Run from Server; classpath includes client/server classes and Server/lib/*. */
public final class WorldMapChecks {
    public static void main(String[] args) throws Exception {
        try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
            Map<Integer, ObjectDefinition> definitions = new HashMap<>();
            for (Map.Entry<Integer, byte[]> entry : store.readFiles(2, 6).entrySet()) {
                ObjectDefinition definition = new ObjectDefinition();
                definition.decode(new Buffer(entry.getValue()));
                definitions.put(entry.getKey(), definition);
            }
            Js5ReferenceTable maps = store.readReferenceTable(5);
            Set<Integer> availableModels = new HashSet<>();
            for (int model : store.readReferenceTable(7).getGroupIds()) availableModels.add(model);
            Set<Integer> usedModels = new HashSet<>();
            Set<Integer> checkedDefinitions = new HashSet<>();
            List<String> report = new ArrayList<>();
            report.add("region\tsource\tplacements\twalls\tstatus");
            int squares = 0, placements = 0, fallbackCount = 0;
            Map<String, Integer> wallCounts = new HashMap<>();
            for (int x = 0; x < 256; x++) {
                for (int y = 0; y < 256; y++) {
                    if (maps.getGroupId("m" + x + "_" + y) < 0) continue;
                    String region = x + "_" + y;
                    check(maps.getGroupId("l" + region) >= 0, "Missing locations: " + region);
                    verifyTerrain(MapGroups.readTerrain(store, maps, x, y), region);
                    Buffer buffer = new Buffer(MapGroups.readLocations(store, maps, x, y));
                    int id = -1, count = 0, walls = 0;
                    for (;;) {
                        int delta = buffer.getSmartB();
                        if (delta == 0) break;
                        id += delta;
                        verifyDefinition(id, definitions, availableModels, usedModels, checkedDefinitions);
                        ObjectDefinition definition = definitions.get(id);
                        int position = 0;
                        for (;;) {
                            int step = buffer.getSmartB();
                            if (step == 0) break;
                            position += step - 1;
                            int type = buffer.getUbyte() >> 2;
                            check(position >= 0 && position < 16384 && type <= 22,
                                    "Invalid placement: " + region + " object " + id);
                            check(supportsShape(definition, type),
                                    "Invisible placement: " + region + " object " + id + " type " + type);
                            count++;
                            if (type <= 3) walls++;
                        }
                    }
                    check(buffer.offset == buffer.payload.length, "Trailing location bytes: " + region);
                    int[] key = XteaKeys.forMapSquare(x, y);
                    boolean fallback = (key[0] | key[1] | key[2] | key[3]) == 0
                            && Files.isRegularFile(Paths.get("cache/map-fallback/l" + region + ".dat"));
                    if (fallback) fallbackCount++;
                    report.add(region + "\t" + (fallback ? "fallback" : "native-443")
                            + "\t" + count + "\t" + walls + "\tPASS");
                    wallCounts.put(region, walls);
                    squares++;
                    placements += count;
                }
            }
            check(squares == 792, "Unexpected map coverage: " + squares);
            String[] repaired = {"42_54", "45_54", "48_54", "51_52", "53_54", "54_54"};
            int[] minimumWalls = {1100, 750, 680, 500, 329, 650};
            for (int i = 0; i < repaired.length; i++)
                check(wallCounts.get(repaired[i]) >= minimumWalls[i], "Buildings missing: " + repaired[i]);
            for (int id : usedModels) {
                byte[] bytes = store.readFiles(7, id).get(0);
                check(bytes != null && bytes.length >= 18, "Missing model data: " + id);
                Model model = new Model(bytes);
                for (int face = 0; face < model.anInt2366; face++) {
                    check(validVertex(model, model.anIntArray2385[face])
                            && validVertex(model, model.anIntArray2363[face])
                            && validVertex(model, model.anIntArray2368[face]), "Invalid model face: " + id);
                }
            }
            if (args.length > 0) Files.write(Paths.get(args[0]), report);
            System.out.println("World map checks passed: " + squares + " squares, " + fallbackCount
                    + " fallbacks, " + placements + " placements, " + usedModels.size() + " decoded models.");
        }
    }

    private static void verifyTerrain(byte[] bytes, String region) {
        check(bytes != null, "Missing terrain: " + region);
        Buffer buffer = new Buffer(bytes);
        for (int tile = 0; tile < 4 * 64 * 64; tile++) {
            for (;;) {
                int opcode = buffer.getUbyte();
                if (opcode == 0) break;
                if (opcode == 1) { buffer.getUbyte(); break; }
                if (opcode <= 49) buffer.getUbyte();
            }
        }
        check(buffer.offset == bytes.length, "Trailing terrain bytes: " + region);
    }

    private static void verifyDefinition(int id, Map<Integer, ObjectDefinition> definitions,
            Set<Integer> availableModels, Set<Integer> usedModels, Set<Integer> checked) {
        ObjectDefinition definition = definitions.get(id);
        check(definition != null, "Missing object definition: " + id);
        if (!checked.add(id)) return;
        if (definition.anIntArray1954 != null) {
            for (int model : definition.anIntArray1954) {
                check(availableModels.contains(model), "Missing model " + model + " for object " + id);
                usedModels.add(model);
            }
        }
        if (definition.anIntArray1961 != null) {
            for (int target : definition.anIntArray1961)
                if (target != -1) verifyDefinition(target, definitions, availableModels, usedModels, checked);
        }
    }

    private static boolean supportsShape(ObjectDefinition definition, int type) {
        // The scene builder uses model shape 10 for placement 11 and shape 4 for wall decorations.
        if (type == 11) type = 10;
        if (type >= 5 && type <= 8) type = 4;
        // Model-free entries include intentional clipping/sound markers and transformed objects.
        if (definition.anIntArray1961 != null || definition.anIntArray1954 == null) return true;
        if (definition.anIntArray1960 == null) return type == 10;
        for (int shape : definition.anIntArray1960) if (shape == type) return true;
        return false;
    }

    private static boolean validVertex(Model model, int vertex) {
        return vertex >= 0 && vertex < model.anInt2367;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
