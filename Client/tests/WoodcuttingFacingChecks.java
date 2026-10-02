package unpackaged;

import jagex.io.FrameBuffer;
import jagex.utils.Queue;
import jagex.world.actors.Player;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Decode the server check's actual facing mask, then run the client's turning code. */
public final class WoodcuttingFacingChecks {
    public static void main(String[] args) throws Exception {
        byte[] packet = Files.readAllBytes(Paths.get(args[0]));
        for (int base : new int[] {3152, 3192}) {
            Class65.anInt1145 = base;
            JKeyListener.anInt618 = base;
            for (int[] offset : new int[][] {{-1, 0}, {2, 0}, {0, -1}, {0, 2}}) {
                Player player = new Player();
                player.anInt2301 = (3200 + offset[0] - base) * 128 + 64;
                player.anInt2275 = (3200 + offset[1] - base) * 128 + 64;
                player.anInt2260 = 42;
                Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(packet.length);
                Class39_Sub5_Sub11.gameBuffer.payload = packet.clone();
                Class39_Sub5_Sub11.gameBuffer.offset = 1;
                ClientApplet.method25(player, (byte) 0, 0, packet[0] & 0xff);
                require(player.anInt2260 == -1, "Client retained entity facing");
                require(player.anInt2316 == 6402 && player.anInt2300 == 6402,
                        "Client did not decode the 2x2 trunk centre");
                int dx = player.anInt2301 - (3201 - base) * 128;
                int dy = player.anInt2275 - (3201 - base) * 128;
                int expected = (int) (Math.atan2(dx, dy) * 325.949) & 2047;
                for (int frame = 0; frame < 64; frame++) Queue.method990(false, player);
                require(player.anInt2251 == expected, "Visible character did not turn toward trunk");
            }
        }
        System.out.println("Client woodcutting facing checks passed: real server mask, four sides, two region bases, final visible rotation.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
