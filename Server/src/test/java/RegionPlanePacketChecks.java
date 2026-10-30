import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.PacketWriter;
import com.rs2.net.packet.RegionPacket;
import java.nio.ByteBuffer;

/** Regression coverage for invalid revision-443 map-plane values. */
public final class RegionPlanePacketChecks {
    public static void main(String[] args) {
        for (int plane = 0; plane < 4; plane++) {
            PacketWriter writer = PacketBuffer.allocateWriter(512);
            RegionPacket.write(writer, new IsaacCipher(new int[]{1, 2, 3, 4}),
                    384, 384, 50, 51, plane);
            ByteBuffer data = writer.getBuffer();
            require((data.get(9) & 255) == plane + 128,
                    "Valid plane encoded incorrectly: " + plane);
        }
        checkRejectedPlane(246);
        checkRejectedPlane(-10);
        checkRejectedPlane(4);
        System.out.println("Revision 443 region-plane server checks passed");
    }

    private static void checkRejectedPlane(int plane) {
        PacketWriter writer = PacketBuffer.allocateWriter(512);
        boolean rejected = false;
        try {
            RegionPacket.write(writer, new IsaacCipher(new int[]{1, 2, 3, 4}),
                    384, 384, 50, 51, plane);
        } catch (IllegalArgumentException expected) {
            rejected = expected.getMessage().contains("plane");
        }
        require(rejected, "Invalid server plane must be rejected: " + plane);
        require(writer.getBuffer().position() == 0,
                "Rejected region must not write a partial packet");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
