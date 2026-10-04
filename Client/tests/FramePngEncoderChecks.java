package unpackaged;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Random;
import javax.imageio.ImageIO;

public final class FramePngEncoderChecks {
    public static void main(String[] args) throws Exception {
        check(new int[765 * 503], 765, 503);
        int[] pixels = new int[765 * 503];
        Random random = new Random(443);
        for (int i = 0; i < pixels.length; i++) pixels[i] = random.nextInt() & 0xffffff;
        check(pixels, 765, 503);
        check(new int[] {0xff0000, 0x00ff00, 0x0000ff, 0xffffff, 0, 0x123456}, 3, 2);
        System.out.println("PASS: PNG dimensions and every RGB pixel survive independent ImageIO decoding.");
    }
    private static void check(int[] pixels, int width, int height) throws Exception {
        BufferedImage decoded = ImageIO.read(new ByteArrayInputStream(FramePngEncoder.encode(pixels, width, height)));
        if (decoded == null || decoded.getWidth() != width || decoded.getHeight() != height)
            throw new AssertionError("Incorrect PNG dimensions");
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++)
            if ((decoded.getRGB(x, y) & 0xffffff) != pixels[y * width + x])
                throw new AssertionError("Pixel mismatch at " + x + "," + y);
    }
}
