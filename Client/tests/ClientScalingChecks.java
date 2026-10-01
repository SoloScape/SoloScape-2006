package unpackaged;

import jagex.utils.Deque;
import jagex.utils.IsaacPrng;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

public final class ClientScalingChecks {
    public static void main(String[] args) throws Exception {
        IsaacPrng.anInt1087 = 765;
        Deque.anInt919 = 503;
        Canvas_Sub1 canvas = new Canvas_Sub1(new Canvas());
        final MouseEvent[] received = new MouseEvent[1];
        MouseAdapter listener = new MouseAdapter() {
            public void mousePressed(MouseEvent event) { received[0] = event; event.consume(); }
            public void mouseMoved(MouseEvent event) { received[0] = event; }
            public void mouseDragged(MouseEvent event) { received[0] = event; }
        };
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        for (int[] size : new int[][] {{765, 503}, {1530, 1006}, {2000, 1315}}) {
            canvas.setSize(size[0], size[1]);
            BufferedImage image = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
            Graphics graphics = canvas.scaleGameGraphics(image.createGraphics());
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, 765, 503);
            graphics.setColor(Color.GREEN);
            graphics.fillRect(500, 300, 20, 20);
            graphics.dispose();
            require(image.getRGB(size[0] - 1, size[1] - 1) == Color.RED.getRGB(), "Drawing did not fill canvas");
            require(image.getRGB(canvas.toCanvasX(510), canvas.toCanvasY(310)) == Color.GREEN.getRGB(), "Interface section did not scale");
            for (int id : new int[] {MouseEvent.MOUSE_PRESSED, MouseEvent.MOUSE_MOVED, MouseEvent.MOUSE_DRAGGED}) {
                int button = id == MouseEvent.MOUSE_PRESSED ? MouseEvent.BUTTON3 : MouseEvent.NOBUTTON;
                MouseEvent event = new MouseEvent(canvas, id, 123, InputEvent.BUTTON3_DOWN_MASK,
                    canvas.toCanvasX(510), canvas.toCanvasY(310), 1, true, button);
                canvas.dispatchEvent(event);
                require(received[0].getX() == 510 && received[0].getY() == 310, "Incorrect click/hover/drag mapping");
                require(received[0].getButton() == button && received[0].isPopupTrigger(), "Mouse metadata changed");
                if (id == MouseEvent.MOUSE_PRESSED) require(event.isConsumed(), "Consumption was lost");
            }
            for (int x = 0; x < 765; x++) {
                MouseEvent event = new MouseEvent(canvas, MouseEvent.MOUSE_MOVED, 123, 0,
                    canvas.toCanvasX(x), canvas.toCanvasY(502), 0, false);
                canvas.dispatchEvent(event);
                require(received[0].getX() == x && received[0].getY() == 502, "Web coordinate round trip failed");
            }
        }
        Class41.aCanvas778 = canvas;
        Class10.frame = new Frame();
        try {
            Client client = new Client();
            Method position = ClientApplet.class.getDeclaredMethod("positionGameCanvas");
            position.setAccessible(true);
            Class10.frame.setSize(2560, 1392);
            position.invoke(client);
            require(canvas.getHeight() == 1392 && canvas.getWidth() == 2117, "Maximized viewport did not fit");
            require(canvas.getX() == 221 && canvas.getY() == 0, "Viewport not centered");
            Class10.frame.setSize(765, 503);
            position.invoke(client);
            require(canvas.getWidth() == 765 && canvas.getHeight() == 503 && canvas.getX() == 0 && canvas.getY() == 0,
                "Restoring did not return to original resolution");
        } finally {
            Class10.frame.dispose();
            Class10.frame = null;
        }
        System.out.println("Client scaling checks passed: rendering, mouse input, web coordinates, fitting and restoring.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
