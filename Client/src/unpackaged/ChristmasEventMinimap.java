package unpackaged;

import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.Cache;
import jagex.world.actors.Player;
import jagex.world.actors.Projectile;

/** Minimap markers for the permanent Christmas rescue. */
final class ChristmasEventMinimap {
    private static final int PRESENT_ICON = 72;
    // Red exclamation-mark dungeon sprite in the bundled mapfunction sheet.
    private static final int DUNGEON_ICON = 12;

    static void draw() {
        if (NameTable.height != 0 || Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109 == null) return;
        drawMarker(PRESENT_ICON, 2904, 3186, 1);
        drawMarker(DUNGEON_ICON, 2843, 3141, 2);
    }

    private static void drawMarker(int icon, int worldX, int worldY, int size) {
        DirectColorSprite[] icons = Projectile.aClass39_Sub5_Sub10_Sub3Array2205;
        if (icons == null || icons.length <= icon || icons[icon] == null) return;
        int localX = worldX - Class65.anInt1145;
        int localY = worldY - JKeyListener.anInt618;
        if (localX < 0 || localX >= 104 || localY < 0 || localY >= 104) return;
        Player player = Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109;
        // Centre the cave marker between its four tiles.
        Player.method528(localY * 4 + size * 2 - player.anInt2275 / 32,
                icons[icon], localX * 4 + size * 2 - player.anInt2301 / 32, 10064);
    }
}
