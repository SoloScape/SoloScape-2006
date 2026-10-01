package unpackaged;

import jagex.utils.JString;

/** Prefixes are transmitted as metadata; the overhead renderer applies the viewer's toggle. */
public final class ChatEffects {
    private static final String[] COLOURS = {
        "yellow", "red", "green", "cyan", "purple", "white",
        "flash1", "flash2", "flash3", "glow1", "glow2", "glow3"
    };
    private static final String[] ANIMATIONS = {"wave", "wave2", "shake", "scroll", "slide"};

    public final int colour;
    public final int animation;
    public final JString text;

    private ChatEffects(int colour, int animation, JString text) {
        this.colour = colour;
        this.animation = animation;
        this.text = text;
    }

    public static ChatEffects parse(JString input) {
        int offset = 0, colour = 0, animation = 0;
        boolean hasColour = false, hasAnimation = false;
        // Accept either order, with at most one colour and one animation.
        for (int count = 0; count < 2 && offset < input.length; count++) {
            int start = offset;
            char delimiter = (char) input.charAt(start);
            boolean wrapped = delimiter == ':' || delimiter == '@';
            if (wrapped) start++;
            else delimiter = ':';
            int end = start;
            while (end < input.length && input.charAt(end) != delimiter) end++;
            if (end == input.length) break;
            StringBuilder token = new StringBuilder();
            for (int pos = start; pos < end; pos++) token.append((char) input.charAt(pos));
            String name = token.toString().toLowerCase(java.util.Locale.ROOT);
            if (name.equals("flash")) name = "flash1";
            if (name.equals("glow")) name = "glow1";
            if (name.equals("wave1")) name = "wave";
            if (name.equals("blu") || name.equals("blue")) name = "cyan";
            if (name.equals("yel")) name = "yellow";
            if (name.equals("gre")) name = "green";
            if (name.equals("cya")) name = "cyan";
            if (name.equals("pur") || name.equals("mag")) name = "purple";
            if (name.equals("whi")) name = "white";
            int colourId = indexOf(COLOURS, name);
            int animationId = indexOf(ANIMATIONS, name);
            if (colourId >= 0 && !hasColour) {
                colour = colourId;
                hasColour = true;
            } else if (animationId >= 0 && !hasAnimation && delimiter != '@') {
                animation = animationId + 1;
                hasAnimation = true;
            } else break;
            offset = end + 1;
        }
        return new ChatEffects(colour, animation, input.method85(-58, offset));
    }

    private static int indexOf(String[] values, String value) {
        for (int i = 0; i < values.length; i++) if (values[i].equals(value)) return i;
        return -1;
    }
}
