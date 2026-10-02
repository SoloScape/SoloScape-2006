package unpackaged;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import jagex.graphics.sprites.DirectColorSprite;

/** Selected revision-530 artwork, bundled independently of the revision-443 cache. */
public final class GrandExchangeAssets {
    private static final Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    private static BufferedImage image(String name) {
        BufferedImage cached = images.get(name);
        if (cached != null) return cached;
        String path = "/assets/grand-exchange/" + name + ".png";
        try (InputStream stream = GrandExchangeAssets.class.getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Missing bundled exchange asset: " + path);
            cached = ImageIO.read(stream);
            if (cached == null) throw new IOException("Invalid exchange asset: " + path);
            images.put(name, cached);
            return cached;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static DirectColorSprite sprite(String name) {
        return sprite(image(name));
    }

    private static DirectColorSprite sprite(BufferedImage image) {
        DirectColorSprite sprite = new DirectColorSprite(image.getWidth(), image.getHeight());
        image.getRGB(0, 0, sprite.width, sprite.height, sprite.anIntArray2476, 0, sprite.width);
        for (int i = 0; i < sprite.anIntArray2476.length; i++) {
            int rgba = sprite.anIntArray2476[i];
            sprite.anIntArray2476[i] = (rgba >>> 24) == 0 ? 0 : Math.max(1, rgba & 0xffffff);
        }
        return sprite;
    }

    public static DirectColorSprite button(int width, int height) {
        return button(width, height, null);
    }

    public static DirectColorSprite button(int width, int height, String icon) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image("1146-0"), 0, 0, width, height, null);
        if (icon != null) {
            BufferedImage glyph = image(icon);
            g.drawImage(glyph, (width - glyph.getWidth()) / 2, (height - glyph.getHeight()) / 2, null);
        }
        g.dispose();
        return sprite(result);
    }
}
