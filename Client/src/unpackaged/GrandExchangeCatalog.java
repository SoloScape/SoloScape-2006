package unpackaged;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Client-side mirror of the server's 12 December 2006 Grand Exchange catalogue. */
public final class GrandExchangeCatalog {
    private static final boolean[] allowed = load();

    private GrandExchangeCatalog() {
    }

    public static boolean contains(int itemId) {
        return itemId >= 0 && itemId < allowed.length
                && !isNonTradeableProp(itemId) && allowed[itemId];
    }

    /** Unobtainable cutscene, world-scene, scrapped and minigame-only props.
     * These can share names and models with actual tradeable equipment.
     * Keep their definitions for historical content; never list GE offers. */
    private static boolean isNonTradeableProp(int itemId) {
        switch (itemId) {
            case 740:
            case 1867:
            case 2422:
            case 2513:
            case 2529:
            case 3006:
            case 4178:
            case 4180:
            case 4182:
            case 4692:
            case 4838:
            case 5356:
            case 5551:
            case 6561:
            case 6796:
            case 6820:
            case 6888:
            case 6893:
            case 6894:
            case 6895:
            case 6896:
            case 6897:
            case 6903:
            case 6964:
            case 6967:
            case 6968:
            case 6969:
                return true;
            default:
                return false;
        }
    }

    private static boolean[] load() {
        boolean[] result = new boolean[20000];
        InputStream stream = GrandExchangeCatalog.class.getResourceAsStream(
                "/assets/grand-exchange/catalog.txt");
        if (stream == null) throw new IllegalStateException("Missing Grand Exchange catalogue resource");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.charAt(0) == '#') continue;
                int id = Integer.parseInt(line);
                if (id < 0 || id >= result.length) throw new IllegalStateException("GE item id out of range: " + id);
                if (!isNonTradeableProp(id)) result[id] = true;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load Grand Exchange catalogue", e);
        }
        return result;
    }
}
