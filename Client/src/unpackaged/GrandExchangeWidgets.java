package unpackaged;

import java.util.Arrays;
import jagex.utils.JString;

/** Runtime exchange interfaces for the custom content absent from cache 443. */
public final class GrandExchangeWidgets {
    private static final int[] ROOTS = {18890, 18939, 18984, 19018, 19101};
    private static final int[] SIZES = {49, 45, 34, 83, 2};
    public static boolean searching;
    private static final int[] nextExtra = new int[5];
    private static final Widget[] itemNames = new Widget[3];
    private static final Widget[] itemExamines = new Widget[3];
    private static final Widget[] progressShadows = new Widget[7];
    private static Widget searchGlass;

    public static boolean load(int group) {
        if (group == 505) {
            if (Class62_Sub1.widgets.length < 506) {
                Class62_Sub1.widgets = Arrays.copyOf(Class62_Sub1.widgets, 506);
                Class39_Sub5_Sub4.widgetsLoaded = Arrays.copyOf(Class39_Sub5_Sub4.widgetsLoaded, 506);
            }
            if (Class62_Sub1.widgets[group] != null && Class39_Sub5_Sub4.widgetsLoaded[group]) return true;
            // The developer picker uses only the GE chat-panel overlay.
            Widget root = new Widget();
            root.anInt2084 = group << 16;
            root.anInt2050 = -1;
            root.type = 0;
            Class62_Sub1.widgets[group] = new Widget[] {root};
            Class39_Sub5_Sub4.widgetsLoaded[group] = true;
            return true;
        }
        if (group < 500 || group > 504) return false;
        if (Class62_Sub1.widgets.length < 505) {
            Class62_Sub1.widgets = Arrays.copyOf(Class62_Sub1.widgets, 505);
            Class39_Sub5_Sub4.widgetsLoaded = Arrays.copyOf(Class39_Sub5_Sub4.widgetsLoaded, 505);
        }
        if (Class62_Sub1.widgets[group] != null && Class39_Sub5_Sub4.widgetsLoaded[group]) return true;
        int root = ROOTS[group - 500];
        nextExtra[group - 500] = SIZES[group - 500];
        Widget[] widgets = new Widget[SIZES[group - 500] + (group == 504 ? 0 : 80)];
        Class62_Sub1.widgets[group] = widgets;
        Class39_Sub5_Sub4.widgetsLoaded[group] = true;
        for (int i = 0; i < widgets.length; i++) {
            Widget w = widgets[i] = new Widget();
            w.anInt2084 = group << 16 | i;
            w.anInt2050 = group << 16;
            w.aBoolean2055 = true;
        }
        Widget container = widgets[0];
        container.anInt2050 = -1;
        container.type = 0;
        container.quadWidth = container.anInt2020 = group == 504 ? 190 : 512;
        container.quadHeight = container.anInt2095 = group == 504 ? 261 : 334;
        container.aBoolean2055 = false;
        if (group == 504) {
            inventory(19102, 16, 8, 4, 7, "Offer");
        } else {
            Widget background = show(root + 1, 5, 0, 0, 512, 334);
            background.runtimeSprite = GrandExchangeAssets.sprite(group == 503 ? "overview" : "editor");
            text(root + 2, 55, 30, 400, "Grand Exchange");
            get(root + 2).anInt2032 = 1;
            button(root + 3, 473, 30, 16, "Close", 3);
            icon(root + 3, "831-0");
            if (group == 503) overview();
            else if (group == 502) status();
            else editor(root, group == 501);
            Widget emblem = extra(group, group << 16, 5, 25, 26, 25, 25);
            emblem.runtimeSprite = GrandExchangeAssets.sprite("1164-0");
        }
        Class39_Sub5_Sub4.widgetsLoaded[group] = true;
        return true;
    }

    static Widget extra(int group, int parent, int type, int x, int y, int width, int height) {
        Widget w = Class62_Sub1.widgets[group][nextExtra[group - 500]++];
        w.anInt2050 = parent;
        w.type = type;
        w.anInt2090 = w.anInt2091 = x;
        w.anInt2024 = w.anInt2021 = y;
        w.quadWidth = width;
        w.quadHeight = w.anInt2095 = height;
        w.aBoolean2055 = false;
        return w;
    }

    static void label(Widget w, String label) {
        w.anInt2105 = 496;
        w.aBoolean2059 = true;
        w.activeQuadColor = w.inactiveQuadColor = 0xffcc66;
        w.aClass3_2029 = w.aClass3_2048 = literal(label);
    }

    private static void icon(int id, String sprite) {
        Widget w = get(id);
        w.type = 5;
        w.runtimeSprite = GrandExchangeAssets.sprite(sprite);
        w.quadWidth = w.runtimeSprite.width;
        w.quadHeight = w.runtimeSprite.height;
    }

    public static int packed(int legacy) {
        for (int i = 0; i < ROOTS.length; i++) {
            if (legacy >= ROOTS[i] && legacy < ROOTS[i] + SIZES[i])
                return (500 + i) << 16 | legacy - ROOTS[i];
        }
        return -1;
    }

    public static Widget get(int legacy) {
        int hash = packed(legacy);
        if (hash < 0) return null;
        load(hash >>> 16);
        return Class62_Sub1.widgets[hash >>> 16][hash & 65535];
    }

    private static Widget show(int id, int type, int x, int y, int width, int height) {
        Widget w = get(id);
        w.type = type;
        w.anInt2090 = w.anInt2091 = x;
        w.anInt2024 = w.anInt2021 = y;
        w.quadWidth = width;
        w.quadHeight = height;
        w.aBoolean2055 = false;
        return w;
    }

    private static void text(int id, int x, int y, int width, String label) {
        Widget w = show(id, 4, x, y, width, 16);
        label(w, label);
        w.anInt2032 = 1;
    }

    private static void button(int id, int x, int y, int width, String label, int action) {
        text(id, x, y, width, label);
        Widget w = get(id);
        w.anInt2089 = action;
        w.anInt2049 = w.anInt1998 = 0x400000;
        w.aClass3_2068 = literal(label);
        w.quadHeight = 35;
        w.runtimeSprite = GrandExchangeAssets.button(width, 35);
        w.anInt2032 = w.anInt1996 = 1;
    }

    private static void inventory(int id, int x, int y, int columns, int rows, String action) {
        Widget w = show(id, 2, x, y, columns, rows);
        w.anInt2000 = 10;
        w.anInt2010 = 4;
        w.anIntArray2087 = new int[columns * rows];
        w.anIntArray2073 = new int[columns * rows];
        w.anIntArray2037 = new int[20];
        w.anIntArray2028 = new int[20];
        w.anIntArray2053 = new int[20];
        Arrays.fill(w.anIntArray2053, -1);
        w.aClass3Array2043 = new JString[5];
        if (action != null) {
            w.aClass3Array2043[0] = Class39_Sub5_Sub9.createJstring(action);
            w.anInt2049 = w.anInt1998 = 1 << 23;
        }
    }

    private static void overview() {
        Widget hint = extra(503, 503 << 16, 4, 21, 60, 471, 14);
        label(hint, "Shown below is a summary of all your current offers.");
        hint.anInt2105 = 494;
        hint.anInt2032 = 1;
        for (int slot = 0; slot < 6; slot++) {
            int x = 30 + slot % 3 * 156, y = 80 + slot / 3 * 120;
            text(19095 + slot, x, y + 4, 140, "Empty");
            Widget empty = show(19023 + slot * 3, 0, x, y, 140, 110);
            empty.anInt2095 = 110;
            empty.runtimeSprite = GrandExchangeAssets.sprite("empty-slot");
            button(19024 + slot * 3, 20, 48, 35, "Buy", 1);
            button(19025 + slot * 3, 83, 48, 35, "Sell", 1);
            icon(19024 + slot * 3, "1170-0");
            icon(19025 + slot * 3, "1168-0");
            get(19024 + slot * 3).anInt2050 = empty.anInt2084;
            get(19025 + slot * 3).anInt2050 = empty.anInt2084;
            Widget offer = show(19041 + slot * 9, 0, x, y, 140, 110);
            offer.anInt2095 = 110;
            offer.runtimeSprite = GrandExchangeAssets.sprite("active-slot");
            offer.aBoolean2055 = true;
            Widget view = show(19042 + slot * 9, 4, 0, 0, 140, 110);
            view.anInt2089 = 1;
            view.aClass3_2068 = literal("View offer");
            view.anInt2049 = view.anInt1998 = 0x400000;
            label(view, "");
            inventory(19044 + slot * 9, 11, 29, 1, 1, null);
            text(19045 + slot * 9, 52, 30, 82, "Item name");
            get(19045 + slot * 9).quadHeight = 27;
            get(19045 + slot * 9).anInt2032 = 0;
            text(19046 + slot * 9, 48, 53, 86, "0 coins");
            get(19045 + slot * 9).anInt2105 = get(19046 + slot * 9).anInt2105 = 494;
            Widget bar = show(19049 + slot * 9, 3, 7, 81, 0, 13);
            bar.drawSolidQuad = true;
            bar.activeQuadColor = bar.inactiveQuadColor = 0x005f00;
            progressShadows[slot + 1] = extra(503, offer.anInt2084, 3, 7, 81, 0, 3);
            progressShadows[slot + 1].drawSolidQuad = true;
            progressShadows[slot + 1].activeQuadColor = progressShadows[slot + 1].inactiveQuadColor = 0x004a00;
            for (int id : new int[] {19042, 19044, 19045, 19046, 19049})
                get(id + slot * 9).anInt2050 = offer.anInt2084;
        }
    }

    private static void editor(int root, boolean sell) {
        int shift = sell ? 49 : 0;
        int group = sell ? 501 : 500;
        fixed(group, 43, 66, 159, sell ? "Sell Offer" : "Buy Offer", 496);
        decorate(group, 175, 67, sell ? "1158-0" : "1157-0");
        itemNames[group - 500] = fixed(group, 210, 66, 250, "Choose an item to exchange", 496);
        itemExamines[group - 500] = show(18918 + shift, 4, 210, 94, 250, 33);
        label(itemExamines[group - 500], sell ? "Select an item in your inventory to sell."
                : "Click the icon to the left to search for items.");
        itemExamines[group - 500].anInt2105 = 494;
        itemExamines[group - 500].anInt2032 = 1;
        itemExamines[group - 500].anInt1996 = 1;
        button(18907 + shift, 25, 276, 35, "Back", 1);
        icon(18907 + shift, "1147-0");
        button(18896 + shift, 200, 270, 120, "Confirm Offer", 1);
        get(18896 + shift).quadHeight = 43;
        get(18896 + shift).runtimeSprite = GrandExchangeAssets.sprite("confirm");
        if (!sell) {
            button(18897, 102, 92, 40, "Choose item", 1);
            icon(18897, "1137-0");
            searchGlass = decorate(group, 105, 97, "1154-0");
        }
        itemIcon(sell ? 18983 : 18938, 104, 94);
        fixed(group, 47, 157, 208, "Quantity:", 496);
        fixed(group, 262, 157, 202, "Price per item:", 496);
        text(18919 + shift, 230, 131, 213, "N/A");
        get(18919 + shift).anInt2105 = 494;
        get(18919 + shift).aBoolean2055 = true;
        decorate(group, 211, 132, "1158-0");
        text(18920 + shift, 83, 180, 136, "0");
        text(18921 + shift, 295, 180, 136, "0 coins");
        text(18922 + shift, 186, 241, 148, "0 coins");
        button(18908 + shift, 55, 181, 16, "Decrease quantity", 1);
        icon(18908 + shift, "1152-0");
        button(18909 + shift, 235, 181, 16, "Increase quantity", 1);
        icon(18909 + shift, "1153-0");
        String[] quantities = sell ? new String[] {"1", "10", "100", "All", "..."}
                : new String[] {"+1", "+10", "+100", "+1k", "..."};
        for (int i = 0; i < quantities.length; i++) {
            button(18898 + shift + i, 52 + i * 41, 200, 35, quantities[i], 1);
            get(18898 + shift + i).anInt2105 = 494;
        }
        button(18910 + shift, 269, 181, 16, "Decrease price", 1);
        icon(18910 + shift, "1152-0");
        button(18911 + shift, 444, 181, 16, "Increase price", 1);
        icon(18911 + shift, "1153-0");
        int[] priceIds = {18903, 18904, 18906, 18905};
        String[] glyphs = {"1150-0", "1158-0", "1151-0", null};
        String[] actions = {"Minimum price", "Guide price", "Maximum price", "Enter price"};
        for (int i = 0; i < priceIds.length; i++) {
            int id = priceIds[i] + shift;
            button(id, 266 + i * 53, 200, 35, i == 3 ? "..." : "", 1);
            get(id).aClass3_2068 = literal(actions[i]);
            get(id).runtimeSprite = GrandExchangeAssets.button(35, 35, glyphs[i]);
        }
    }

    private static Widget fixed(int group, int x, int y, int width, String text, int font) {
        Widget w = extra(group, group << 16, 4, x, y, width, 16);
        label(w, text);
        w.anInt2105 = font;
        w.anInt2032 = 1;
        return w;
    }

    private static Widget decorate(int group, int x, int y, String sprite) {
        Widget w = extra(group, group << 16, 5, x, y, 32, 32);
        w.runtimeSprite = GrandExchangeAssets.sprite(sprite);
        return w;
    }

    private static void itemIcon(int legacy, int x, int y) {
        Widget w = show(legacy, 5, x, y, 36, 32);
        w.aBoolean2013 = true;
        w.anInt2022 = 1;
        w.anInt2009 = 0;
        w.anInt2096 = 1;
    }

    public static void itemChosen(int hash, int id) {
        int group = hash >>> 16;
        if (group < 500 || group > 502 || itemNames[group - 500] == null) return;
        if (id >= 0) label(itemNames[group - 500], GrandExchangeSearch.name(id));
        if (group == 500 && searchGlass != null) searchGlass.aBoolean2055 = id >= 0;
    }

    public static boolean isItemIcon(int hash) {
        return hash == packed(18938) || hash == packed(18983) || hash == packed(19008);
    }

    public static void opened(int group) {
        if (group == 505) {
            load(group);
            startSearch();
            return;
        }
        if (group != 500 && group != 501) return;
        load(group);
        int shift = group == 501 ? 49 : 0;
        Widget item = get(group == 501 ? 18983 : 18938);
        item.anInt1997 = -1;
        if (group == 500 && searchGlass != null) searchGlass.aBoolean2055 = false;
        label(itemNames[group - 500], "Choose an item to exchange");
        if (itemExamines[group - 500] != null)
            label(itemExamines[group - 500], group == 501 ? "Select an item in your inventory to sell."
                    : "Click the icon to the left to search for items.");
        get(18919 + shift).aClass3_2029 = literal("N/A");
        get(18920 + shift).aClass3_2029 = literal("0");
        get(18921 + shift).aClass3_2029 = get(18922 + shift).aClass3_2029 = literal("0 coins");
        if (group == 500) click(packed(18897));
    }

    private static void status() {
        get(18985).runtimeSprite = GrandExchangeAssets.sprite("status");
        text(18992, 43, 66, 159, "Offer");
        itemNames[2] = fixed(502, 210, 66, 250, "Item", 496);
        text(18997, 230, 131, 213, "N/A");
        get(18997).anInt2105 = 494;
        get(18997).aBoolean2055 = true;
        decorate(502, 211, 132, "1158-0");
        fixed(502, 47, 157, 208, "Quantity:", 496);
        fixed(502, 262, 157, 202, "Price per item:", 496);
        text(18998, 83, 180, 136, "0");
        text(18999, 295, 180, 136, "0 coins");
        text(19000, 186, 241, 148, "0 coins");
        text(19001, 85, 267, 260, "");
        get(19001).quadHeight = 28;
        text(19002, 70, 286, 275, "");
        get(19001).anInt2105 = get(19002).anInt2105 = 494;
        itemIcon(19008, 104, 94);
        inventory(19006, 397, 278, 2, 1, "Collect");
        get(19006).anInt2000 = 17;
        Widget bar = show(19011, 3, 71, 300, 0, 13);
        bar.drawSolidQuad = true;
        bar.activeQuadColor = bar.inactiveQuadColor = 0x005f00;
        progressShadows[0] = extra(502, 502 << 16, 3, 71, 300, 0, 3);
        progressShadows[0].drawSolidQuad = true;
        progressShadows[0].activeQuadColor = progressShadows[0].inactiveQuadColor = 0x004a00;
        Widget cancel = show(19016, 0, 350, 272, 20, 20);
        cancel.anInt2095 = 20;
        button(19017, 0, 0, 20, "Abort offer", 1);
        icon(19017, "1165-0");
        get(19017).anInt2050 = cancel.anInt2084;
        button(18990, 25, 276, 35, "Back", 1);
        icon(18990, "1147-0");
    }

    public static void progress(int legacy, int amount) {
        if (legacy != 19011 && (legacy < 19049 || legacy > 19094 || (legacy - 19049) % 9 != 0)) return;
        Widget w = get(legacy);
        int maxWidth = legacy == 19011 ? 298 : 126;
        int width = maxWidth * Math.max(0, Math.min(amount, 100)) / 100;
        w.quadWidth = width;

        // Original GE bars use a 3px darker top bevel over the 13px fill.
        int color = amount == 250 ? 0xa00000 : amount >= 100 ? 0x005f00 : 0xd88020;
        int shade = amount == 250 ? 0x780000 : amount >= 100 ? 0x004a00 : 0xa86419;
        w.activeQuadColor = w.inactiveQuadColor = color;

        int shadowIndex = legacy == 19011 ? 0 : 1 + (legacy - 19049) / 9;
        Widget shadow = progressShadows[shadowIndex];
        if (shadow != null) {
            shadow.quadWidth = width;
            shadow.activeQuadColor = shadow.inactiveQuadColor = shade;
        }
    }

    public static boolean click(int hash) {
        if (GrandExchangeSearch.click(hash)) return true;
        if (searching && hash != packed(18897)) cancelSearch();
        if (hash != packed(18897)) return false;
        if (Class39_Sub11.anInt1478 != 500) return true;
        startSearch();
        return true;
    }

    private static void startSearch() {
        searching = true;
        Class39_Sub5_Sub4_Sub4.anInt2285 = 2;
        Class39_Sub12.aBoolean1489 = false;
        Class66.aClass3_1151 = Class66.blankString;
        Class14.aBoolean245 = true;
        GrandExchangeSearch.open();
    }

    public static void cancelSearch() {
        if (searching) Class39_Sub5_Sub4_Sub4.anInt2285 = 0;
        searching = false;
        GrandExchangeSearch.close();
        Class14.aBoolean245 = true;
    }

    public static boolean submitSearch(JString name) {
        if (!searching) return false;
        if (!GrandExchangeSearch.hasSearchInterface()) { cancelSearch(); return true; }
        if (name.length == 0) return true;
        for (int id = 0; id < GrandExchangeSearch.itemLimit(); id++) {
            if (GrandExchangeSearch.isSearchable(id)
                    && literal(GrandExchangeSearch.name(id)).method77().isEqual(name.method77())) {
                GrandExchangeSearch.select(id);
                return true;
            }
        }
        GrandExchangeSearch.update(name);
        GrandExchangeSearch.selectFirst();
        return true;
    }

    public static JString literal(String text) {
        // createJstring decodes JODE escape sequences such as '-1' and '(e'.
        // Runtime labels contain ordinary punctuation, like chat received on the wire.
        JString value = new JString();
        value.bytes = text.getBytes(java.nio.charset.Charset.forName("windows-1252"));
        value.length = value.bytes.length;
        return value;
    }
}
