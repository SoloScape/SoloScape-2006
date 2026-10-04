package unpackaged;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/** Lossless RGB PNG with the fast Sub filter and low-latency compression. */
final class FramePngEncoder {
    private FramePngEncoder() { }

    static byte[] encode(int[] pixels, int width, int height) throws IOException {
        if (width <= 0 || height <= 0 || pixels.length != width * height)
            throw new IllegalArgumentException("Invalid frame dimensions");
        byte[] rows = new byte[height * (width * 3 + 1)];
        int position = 0;
        for (int y = 0; y < height; y++) {
            rows[position++] = 1; // PNG Sub filter: difference from the previous pixel.
            int previous = 0;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                rows[position++] = (byte) ((pixel >>> 16) - (previous >>> 16));
                rows[position++] = (byte) ((pixel >>> 8) - (previous >>> 8));
                rows[position++] = (byte) (pixel - previous);
                previous = pixel;
            }
        }
        ByteArrayOutputStream compressed = new ByteArrayOutputStream(256 * 1024);
        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        try {
            DeflaterOutputStream stream = new DeflaterOutputStream(compressed, deflater, 32768);
            stream.write(rows);
            stream.finish();
        } finally {
            deflater.end();
        }
        ByteArrayOutputStream png = new ByteArrayOutputStream(compressed.size() + 57);
        DataOutputStream output = new DataOutputStream(png);
        output.writeLong(0x89504e470d0a1a0aL);
        ByteArrayOutputStream header = new ByteArrayOutputStream(13);
        DataOutputStream fields = new DataOutputStream(header);
        fields.writeInt(width);
        fields.writeInt(height);
        fields.write(new byte[] {8, 2, 0, 0, 0}); // 8-bit RGB, no interlace.
        chunk(output, "IHDR", header.toByteArray());
        chunk(output, "IDAT", compressed.toByteArray());
        chunk(output, "IEND", new byte[0]);
        return png.toByteArray();
    }

    private static void chunk(DataOutputStream output, String name, byte[] data) throws IOException {
        byte[] type = name.getBytes(StandardCharsets.US_ASCII);
        CRC32 checksum = new CRC32();
        checksum.update(type);
        checksum.update(data);
        output.writeInt(data.length);
        output.write(type);
        output.write(data);
        output.writeInt((int) checksum.getValue());
    }
}
