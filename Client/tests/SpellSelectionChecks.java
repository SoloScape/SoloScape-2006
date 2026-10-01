package unpackaged;

import jagex.graphics.DrawingArea;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.IsaacPrng;

public final class SpellSelectionChecks {
    public static void main(String[] args) {
        Widget windStrike = new Widget();
        windStrike.anInt2084 = 192 << 16;
        IsaacPrng.aBoolean1100 = true;
        Class31.anInt570 = -1;
        Class41.anInt775 = windStrike.anInt2084;
        require(Class20.isSelectedSpellWidget(windStrike),
                "Selected Wind Strike was not marked for the white outline");

        Widget otherSpell = new Widget();
        otherSpell.anInt2084 = (192 << 16) | 2;
        require(!Class20.isSelectedSpellWidget(otherSpell),
                "Unselected spell was marked as selected");
        IsaacPrng.aBoolean1100 = false;
        require(!Class20.isSelectedSpellWidget(windStrike),
                "Deselected spell retained selected outline state");

        int[] canvas = new int[25];
        DrawingArea.setBuffer(canvas, 5, 5);
        DirectColorSprite sprite = new DirectColorSprite(1, 1);
        sprite.anIntArray2476[0] = 0xAA3300;
        sprite.drawOutlined(2, 2, 0xFFFFFF);
        require(canvas[2 + 2 * 5] == 0xAA3300, "Outlined sprite lost its source pixel");
        require(canvas[1 + 2 * 5] == 0xFFFFFF, "Missing left silhouette outline pixel");
        require(canvas[3 + 2 * 5] == 0xFFFFFF, "Missing right silhouette outline pixel");
        require(canvas[2 + 1 * 5] == 0xFFFFFF, "Missing top silhouette outline pixel");
        require(canvas[2 + 3 * 5] == 0xFFFFFF, "Missing bottom silhouette outline pixel");
        require(canvas[1 + 1 * 5] == 0, "Outline incorrectly filled the corner like a box");
        System.out.println("Spell selection outline checks passed.");
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
