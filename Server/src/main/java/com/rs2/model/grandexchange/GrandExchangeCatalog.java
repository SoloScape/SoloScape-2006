package com.rs2.model.grandexchange;

import com.rs2.model.item.ItemDefinition;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Tradeable item catalogue frozen to the 12 December 2006 content cutoff. */
public final class GrandExchangeCatalog {
    private static final String PATH = "./data/content/grandExchangeItems2006.tsv";
    private static final int MAX_ITEMS = 20000;
    private static volatile boolean loaded;
    private static boolean[] exchangeable;
    private static int[] guidePrices;
    private static int count;

    private GrandExchangeCatalog() {
    }

    public static boolean isExchangeable(int itemId) {
        ensureLoaded();
        if (isNonTradeableProp(itemId)) return false;
        int id = unnoted(itemId);
        return id >= 0 && id < exchangeable.length
                && !isNonTradeableProp(id) && exchangeable[id];
    }

    public static int getGuidePrice(int itemId) {
        ensureLoaded();
        if (isNonTradeableProp(itemId)) return -1;
        int id = unnoted(itemId);
        return id >= 0 && id < guidePrices.length
                && !isNonTradeableProp(id) && exchangeable[id] ? guidePrices[id] : -1;
    }

    public static int size() {
        ensureLoaded();
        return count;
    }

    /** Unobtainable cutscene, world-scene, scrapped and minigame-only props.
     * These can share names and models with actual tradeable equipment.
     * Keep their definitions for historical content; never list GE offers. */
    public static boolean isNonTradeableProp(int itemId) {
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

    private static int unnoted(int itemId) {
        if (itemId < 0 || itemId >= MAX_ITEMS) return -1;
        ItemDefinition definition = ItemDefinition.forId(itemId);
        return definition.isNote() ? definition.getUnnotedId() : itemId;
    }

    private static synchronized void ensureLoaded() {
        if (loaded) return;
        boolean[] allowed = new boolean[MAX_ITEMS];
        int[] prices = new int[MAX_ITEMS];
        int loadedCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(PATH), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.charAt(0) == '#') continue;
                String[] parts = line.split("\\t", 3);
                if (parts.length < 2) throw new IllegalStateException("Malformed GE catalogue row: " + line);
                int id = Integer.parseInt(parts[0]);
                int price = Integer.parseInt(parts[1]);
                if (id < 0 || id >= MAX_ITEMS) throw new IllegalStateException("GE item id out of range: " + id);
                if (isNonTradeableProp(id)) continue;
                if (allowed[id]) throw new IllegalStateException("Duplicate GE item id: " + id);
                allowed[id] = true;
                prices[id] = price;
                loadedCount++;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load Grand Exchange catalogue " + PATH, e);
        }
        exchangeable = allowed;
        guidePrices = prices;
        count = loadedCount;
        loaded = true;
    }
}
