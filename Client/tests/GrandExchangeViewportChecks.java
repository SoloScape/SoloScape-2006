package unpackaged;

import jagex.graphics.AbstractImage;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** Ensures modal exchange screens continue presenting the composed viewport. */
public final class GrandExchangeViewportChecks {
    public static void main(String[] args) {
        final BufferedImage screen = new BufferedImage(520, 342, BufferedImage.TYPE_INT_RGB);
        Class41.aCanvas778 = new Canvas_Sub1(new Canvas()) {
            @Override public Graphics getGraphics() { return screen.createGraphics(); }
        };
        final BufferedImage viewport = new BufferedImage(512, 334, BufferedImage.TYPE_INT_RGB);
        Class23.aClass57_435 = new AbstractImage() {
            public void setComponent(Component component, int width, int height) { }
            public void draw(Graphics graphics, int x, int y) {
                require(x == 4 && y == 4, "Viewport origin changed");
                graphics.drawImage(viewport, x, y, null);
            }
        };
        for (int group : new int[] {-1, 500, 501, 502, 503, 504, -1}) {
            Class39_Sub11.anInt1478 = group;
            for (Color color : new Color[] {Color.RED, Color.GREEN}) {
                Graphics graphics = viewport.createGraphics();
                graphics.setColor(color);
                graphics.fillRect(0, 0, 512, 334);
                graphics.dispose();
                Class46.method940((byte) -103);
                require(screen.getRGB(4, 4) == color.getRGB(),
                        "Viewport stopped updating with interface " + group);
                require(screen.getRGB(515, 337) == color.getRGB(),
                        "Viewport was only partially presented with interface " + group);
            }
        }
        System.out.println("Grand Exchange viewport checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
