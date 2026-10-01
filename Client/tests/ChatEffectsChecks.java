package unpackaged;

import jagex.graphics.BitmapFont;
import jagex.graphics.DrawingArea;
import jagex.utils.JString;
import java.util.Arrays;

public final class ChatEffectsChecks {
    public static void main(String[] args) {
        String[] colours = {"yellow", "red", "green", "cyan", "purple", "white",
            "flash1", "flash2", "flash3", "glow1", "glow2", "glow3"};
        String[] animations = {"wave", "wave2", "shake", "scroll", "slide"};
        for (int colour = 0; colour < colours.length; colour++) {
            check(colours[colour] + ":Hello!", colour, 0, "Hello!");
            check(":" + colours[colour].toUpperCase() + ":Hello!", colour, 0, "Hello!");
            for (int effect = 0; effect < animations.length; effect++) {
                check(colours[colour] + ":" + animations[effect] + ":Hello!", colour, effect + 1, "Hello!");
                check(":" + animations[effect] + ":" + colours[colour] + ":Hello!", colour, effect + 1, "Hello!");
            }
        }
        for (int effect = 0; effect < animations.length; effect++)
            check(":" + animations[effect] + ":Hello!", 0, effect + 1, "Hello!");
        check(":flash::wave:Hello!", 6, 1, "Hello!");
        check("@Red@wave:Hello!", 1, 1, "Hello!");
        check("red:red:Hello!", 1, 0, "red:Hello!");
        check("wave3:Hello!", 0, 0, "wave3:Hello!");
        check("Hello:wave:", 0, 0, "Hello:wave:");
        check("", 0, 0, "");
        check("wave:", 0, 1, "");

        // Use the same formatted renderer as game-message rows, and inspect its pixels.
        int[] widths = new int[256], heights = new int[256];
        Arrays.fill(widths, 1);
        Arrays.fill(heights, 1);
        byte[][] glyphs = new byte[256][1];
        for (byte[] glyph : glyphs) glyph[0] = 1;
        BitmapFont font = new BitmapFont(new int[256], widths, heights, new int[256], glyphs);
        int[] pixels = new int[100];
        DrawingArea.setBuffer(pixels, 100, 1);
        font.method635(Class39_Sub5_Sub9.createJstring("@red@Harmony"), 0, 1, 0, false);
        for (int i = 0; i < 7; i++) require(pixels[i] == 0xff0000, "Unlock message isn't red");
        require(pixels[7] == 0, "Colour tag occupied visible glyphs");
        font.method635(Class39_Sub5_Sub9.createJstring("Normal"), 20, 1, 0x123456, false);
        require(pixels[20] == 0x123456, "Colour leaked to the next message");
        System.out.println("Chat effects checks passed: all prefixes/combinations and red message pixels.");
    }

    private static void check(String input, int colour, int animation, String text) {
        ChatEffects result = ChatEffects.parse(Class39_Sub5_Sub9.createJstring(input));
        require(result.colour == colour && result.animation == animation
                && result.text.isEqual(Class39_Sub5_Sub9.createJstring(text)), "Bad prefix: " + input);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
