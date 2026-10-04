package unpackaged;

import com.rs2.cache.js5.Js5CacheStore;
import java.io.File;
import java.util.Arrays;
import jagex.graphics.DrawingArea;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.Cache;
import jagex.world.actors.Player;
import jagex.world.actors.Projectile;
import jagex.world.map.TraversalMap;

/** Run from Server with client/server classes and Server/lib on the classpath. */
public final class ChristmasMinimapChecks {
    public static void main(String[] args) throws Exception {
        try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
            int group = store.readReferenceTable(8).getGroupId("mapfunction");
            Class39_Sub7.decodeBitmapFont(store.readFiles(8, group).values().iterator().next());
        }
        int icon = 72;
        DirectColorSprite present = new DirectColorSprite(
                Class39_Sub5_Sub9.anIntArray1799[icon], Class39_Sub14.anIntArray1512[icon]);
        for (int i = 0; i < present.anIntArray2476.length; i++)
            present.anIntArray2476[i] = Class39_Sub11.anIntArray1460[TraversalMap.aByteArrayArray517[icon][i] & 255];
        Projectile.aClass39_Sub5_Sub10_Sub3Array2205 = new DirectColorSprite[73];
        Projectile.aClass39_Sub5_Sub10_Sub3Array2205[icon] = present;
        int dungeonIcon = 12;
        DirectColorSprite dungeon = new DirectColorSprite(
                Class39_Sub5_Sub9.anIntArray1799[dungeonIcon], Class39_Sub14.anIntArray1512[dungeonIcon]);
        for (int i = 0; i < dungeon.anIntArray2476.length; i++)
            dungeon.anIntArray2476[i] = Class39_Sub11.anIntArray1460[TraversalMap.aByteArrayArray517[dungeonIcon][i] & 255];
        require(Arrays.stream(dungeon.anIntArray2476).filter(colour ->
                (colour >> 16 & 255) > 180 && (colour >> 8 & 255) < 100 && (colour & 255) < 100).count() > 4,
                "Dungeon marker must be the red exclamation sprite");
        Projectile.aClass39_Sub5_Sub10_Sub3Array2205[dungeonIcon] = dungeon;
        Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109 = new Player();
        Class65.anInt1145 = 2856;
        JKeyListener.anInt618 = 3136;
        setPlayer(2906,3184);
        NameTable.height = 0;
        int[] pixels = new int[172 * 156];
        DrawingArea.setBuffer(pixels,172,156);
        ChristmasEventMinimap.draw();
        require(Arrays.stream(pixels).anyMatch(p -> p != 0), "Present marker was not drawn");
        int[] first = pixels.clone();
        Arrays.fill(pixels,0);
        Class34.anInt605 = 512;
        ChristmasEventMinimap.draw();
        require(Arrays.stream(pixels).anyMatch(p -> p != 0) && !Arrays.equals(first,pixels),
                "Marker did not follow the minimap rotation");
        // The same world positions in a different loaded region must project identically.
        Arrays.fill(pixels,0);
        Class34.anInt605 = 0;
        Class65.anInt1145 += 8;
        JKeyListener.anInt618 += 8;
        setPlayer(2906,3184);
        ChristmasEventMinimap.draw();
        require(Arrays.equals(first,pixels), "Marker shifted when the region base changed");
        Arrays.fill(pixels,0);
        NameTable.height = 1;
        ChristmasEventMinimap.draw();
        require(Arrays.stream(pixels).allMatch(p -> p == 0), "Marker leaked onto another plane");
        NameTable.height = 0;
        Class65.anInt1145 = 2800;
        JKeyListener.anInt618 = 3100;
        setPlayer(2843,3141);
        ChristmasEventMinimap.draw();
        int[] caveFrame = pixels.clone();
        require(Arrays.stream(pixels).anyMatch(p -> p != 0), "Dungeon marker missing at new entrance");
        Arrays.fill(pixels,0);
        Class65.anInt1145 += 8;
        JKeyListener.anInt618 += 8;
        setPlayer(2843,3141);
        ChristmasEventMinimap.draw();
        require(Arrays.equals(caveFrame,pixels), "Dungeon marker shifted after rebase");
        Arrays.fill(pixels,0);
        NameTable.height = 1;
        ChristmasEventMinimap.draw();
        require(Arrays.stream(pixels).allMatch(p -> p == 0), "Dungeon marker leaked into cave");
        NameTable.height = 0;
        Class65.anInt1145 = 3200;
        JKeyListener.anInt618 = 3200;
        ChristmasEventMinimap.draw();
        require(Arrays.stream(pixels).allMatch(p -> p == 0), "Marker leaked into another region");
        Projectile.aClass39_Sub5_Sub10_Sub3Array2205 = null;
        ChristmasEventMinimap.draw();
        System.out.println("Christmas minimap checks passed: native sprite, drawing, rotation, region rebasing and plane filtering.");
    }

    private static void setPlayer(int x, int y) {
        Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anInt2301 = (x-Class65.anInt1145)*128+64;
        Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anInt2275 = (y-JKeyListener.anInt618)*128+64;
    }
    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
