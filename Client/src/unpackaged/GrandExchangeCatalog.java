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
        return itemId >= 0 && itemId < allowed.length && allowed[itemId];
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
                result[id] = true;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load Grand Exchange catalogue", e);
        }
        return result;
    }
}
