package unpackaged;

import jagex.graphics.AbstractImage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class GrandExchangeSearchPaintChecks {
    public static void main(String[] args) {
        for (int scale : new int[] {1, 2}) {
            final BufferedImage screen = new BufferedImage(765 * scale, 503 * scale, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = screen.createGraphics();
            graphics.scale(scale, scale);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(0, 338, 516, 130);
            GrandExchangeWidgets.searching = true;
            // Full frame plus the chat surround, buttons and lower tab strip.
            for (int[] bounds : new int[][] {
                    {0, 0, 765, 503}, {0, 357, 17, 96}, {496, 357, 20, 96},
                    {0, 338, 516, 19}, {0, 453, 496, 50}, {496, 466, 269, 37}}) {
                AbstractImage background = new AbstractImage() {
                    public void setComponent(Component component, int width, int height) { }
                    public void draw(Graphics target, int x, int y) {
                        target.setColor(Color.RED);
                        target.fillRect(x, y, width, height);
                        // Check immediately after each underlying draw, before any overlay repaint.
                        for (int sy = 338 * scale; sy < 468 * scale; sy++) {
                            for (int sx = 0; sx < 516 * scale; sx++) {
                                require(screen.getRGB(sx, sy) == Color.GREEN.getRGB(),
                                        "Background exposed beneath search");
                            }
                        }
                    }
                };
                background.width = bounds[2];
                background.height = bounds[3];
                GrandExchangeSearch.drawBehindSearch(background, graphics, bounds[0], bounds[1]);
            }
            require(screen.getRGB(600 * scale, 480 * scale) == Color.RED.getRGB(),
                    "Uncovered frame did not redraw");
            require(graphics.getClip() == null, "Caller clip changed");
            graphics.dispose();
            GrandExchangeWidgets.searching = false;
            Graphics normal = screen.createGraphics();
            AbstractImage chat = new AbstractImage() {
                public void setComponent(Component component, int width, int height) { }
                public void draw(Graphics target, int x, int y) {
                    target.setColor(Color.BLUE);
                    target.fillRect(x, y, 516 * scale, 130 * scale);
                }
            };
            GrandExchangeSearch.drawBehindSearch(chat, normal, 0, 338 * scale);
            require(screen.getRGB(10 * scale, 350 * scale) == Color.BLUE.getRGB(),
                    "Chat background did not resume after closing search");
            normal.dispose();
        }
        System.out.println("Grand Exchange search paint checks passed (normal and scaled canvas).");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
