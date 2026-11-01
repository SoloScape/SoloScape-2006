import com.rs2.ServerSettings;
import com.rs2.model.grandexchange.GrandExchangeCatalog;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.quest.QuestDefinition;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Verifies no decorative/unobtainable props are exposed as tradeable items. */
public final class GrandExchangePropCatalogChecks {
    private static final int[] NON_TRADEABLE_PROPS = {
        740, 1867, 2422, 2513, 2529, 3006,
        4178, 4180, 4182, 4692, 4838, 5356, 5551,
        6561, 6796, 6820, 6888,
        6893, 6894, 6895, 6896, 6897, 6903,
        6964, 6967, 6968, 6969
    };
    private static final int[][] REAL_TRADEABLE_ITEMS = {
        {1149, 64000},    // dragon med helm; not cutscene duplicate 6967
        {4087, 869500},  // dragon platelegs; not cutscene duplicate 4180
        {3140, 10200000},// dragon chainbody; not prop 2513
        {4151, 1500000}, // abyssal whip; not prop 6968
        {3853, 832},     // charged games necklace
        {1061, 16},      // leather boots, not the MTA copy
        {1199, 3313},    // adamant kiteshield, not the MTA copy
        {1145, 1248},    // adamant med helm, not the MTA copy
        {1605, 532},     // emerald, not the MTA copy
        {1303, 19100},   // rune longsword, not the MTA copy
        {1615, 38800}    // dragonstone, not the MTA copy
    };

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();

        Set<Integer> serverIds = loadIds("data/content/grandExchangeItems2006.tsv", true);
        Set<Integer> clientIds = loadIds("../Client/src/assets/grand-exchange/catalog.txt", false);
        require(serverIds.equals(clientIds), "Client and server GE catalogues differ");

        for (int id : NON_TRADEABLE_PROPS) {
            require(!serverIds.contains(id), "Cutscene/scene prop remains in server TSV: " + id);
            require(!GrandExchangeCatalog.isExchangeable(id), "Server permits nontradeable prop: " + id);
            require(GrandExchangeCatalog.getGuidePrice(id) == -1,
                    "Server advertises guide price for nontradeable prop: " + id);
        }
        for (int[] pair : REAL_TRADEABLE_ITEMS) {
            int id = pair[0];
            require(serverIds.contains(id), "Real tradeable item missing: " + id);
            require(GrandExchangeCatalog.isExchangeable(id),
                    "Server incorrectly rejected real item: " + id);
            require(GrandExchangeCatalog.getGuidePrice(id) == pair[1],
                    "Guide price changed for legitimate item: " + id);
        }
        require(GrandExchangeCatalog.size() == serverIds.size(),
                "Loaded GE catalogue size differs from TSV");
        System.out.println("Grand Exchange prop exclusion and client/server catalogue checks passed");
    }

    private static Set<Integer> loadIds(String path, boolean tsv) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        Set<Integer> ids = new HashSet<Integer>();
        for (String line : lines) {
            if (line.isEmpty() || line.charAt(0) == '#') continue;
            String idText = tsv ? line.split("\\t", 2)[0] : line.trim();
            int id = Integer.parseInt(idText);
            require(ids.add(id), "Duplicate GE item id in " + path + ": " + id);
        }
        return ids;
    }

    private static void require(boolean condition, String reason) {
        if (!condition) throw new AssertionError(reason);
    }
}
