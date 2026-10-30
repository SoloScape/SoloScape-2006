package unpackaged;

import java.util.Arrays;

/** God Wars kill-count overlay absent from the revision-443 cache. */
public final class GodWarsWidgets {
    public static final int GROUP = 506;

    private GodWarsWidgets() {
    }

    public static boolean load(int group) {
        if (group != GROUP) return false;
        int required = GROUP + 1;
        if (Class62_Sub1.widgets == null) {
            Class62_Sub1.widgets = new Widget[required][];
        } else if (Class62_Sub1.widgets.length < required) {
            Class62_Sub1.widgets = Arrays.copyOf(Class62_Sub1.widgets, required);
        }
        if (Class39_Sub5_Sub4.widgetsLoaded == null) {
            Class39_Sub5_Sub4.widgetsLoaded = new boolean[required];
        } else if (Class39_Sub5_Sub4.widgetsLoaded.length < required) {
            Class39_Sub5_Sub4.widgetsLoaded = Arrays.copyOf(Class39_Sub5_Sub4.widgetsLoaded, required);
        }
        if (Class62_Sub1.widgets[GROUP] != null && Class39_Sub5_Sub4.widgetsLoaded[GROUP]) return true;

        Widget[] widgets = new Widget[10];
        Widget root = widgets[0] = new Widget();
        root.anInt2084 = GROUP << 16;
        root.anInt2050 = -1;
        root.type = 0;
        root.quadWidth = root.anInt2020 = 512;
        root.quadHeight = root.anInt2095 = 334;
        root.aBoolean2055 = false;
        widgets[1] = text(1, 370, 12, 130, "God Wars kill count", 0xffcc66);
        String[] names = {"Armadyl", "Bandos", "Saradomin", "Zamorak"};
        for (int index = 0; index < names.length; index++) {
            widgets[index + 2] = text(index + 2, 370, 34 + index * 18, 90, names[index], 0xffffff);
            widgets[index + 6] = text(index + 6, 465, 34 + index * 18, 35, "0", 0xffcc66);
        }
        Class62_Sub1.widgets[GROUP] = widgets;
        Class39_Sub5_Sub4.widgetsLoaded[GROUP] = true;
        return true;
    }

    private static Widget text(int child, int x, int y, int width, String value, int color) {
        Widget widget = new Widget();
        widget.anInt2084 = GROUP << 16 | child;
        widget.anInt2050 = GROUP << 16;
        widget.type = 4;
        widget.aBoolean2055 = false;
        widget.anInt2090 = widget.anInt2091 = x;
        widget.anInt2024 = widget.anInt2021 = y;
        widget.quadWidth = width;
        widget.quadHeight = widget.anInt2095 = 16;
        widget.anInt2105 = 496;
        widget.aBoolean2059 = true;
        widget.activeQuadColor = widget.inactiveQuadColor = color;
        widget.aClass3_2029 = widget.aClass3_2048 = GrandExchangeWidgets.literal(value);
        return widget;
    }
}
