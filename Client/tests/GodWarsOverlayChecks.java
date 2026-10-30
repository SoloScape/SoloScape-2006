package unpackaged;

import com.rs2.net.packet.InterfaceBridge;
import jagex.io.FrameBuffer;
import jagex.io.JSocket;
import jagex.utils.Huffmans;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/** Reproduce opening, updating and leaving the God Wars overlay without a cache. */
public final class GodWarsOverlayChecks {
    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void overlayPacket(int group) throws Exception {
        final byte[] payload = {(byte)(group >> 8), (byte)(group + 128)};
        Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(32);
        Class37.gameSocket = new JSocket(new Socket() {
            public void setSoTimeout(int timeout) {}
            public void setTcpNoDelay(boolean enabled) {}
            public InputStream getInputStream() { return new ByteArrayInputStream(payload); }
            public OutputStream getOutputStream() { return new ByteArrayOutputStream(); }
        }, null);
        Class4.frameId = 219;
        Huffmans.frameSize = 2;
        require(Bzip2Block.readFrame(), "walkable-interface packet handled");
        require(Class26.anInt485 == group, "walkable-interface state matches packet");
    }

    public static void main(String[] args) throws Exception {
        require(InterfaceBridge.translateGroup(19556) == 506, "legacy overlay maps to runtime group");
        Class62_Sub1.widgets = new Widget[500][];
        Class39_Sub5_Sub4.widgetsLoaded = new boolean[500];
        require(!GodWarsWidgets.load(19556), "legacy flat ID is not a native group");
        require(JSocket.loadWidget(506), "runtime overlay loads without cache files");
        for (int index = 0; index < 4; index++) {
            int packed = InterfaceBridge.translate(19562 + index);
            require(packed == (506 << 16 | index + 6), "kill count component maps correctly");
            Widget count = Class37.getWidget(packed);
            require(count != null && count.type == 4, "kill count resolves to a text widget");
            count.aClass3_2029 = GrandExchangeWidgets.literal("40");
        }
        Widget count = Class37.getWidget(506 << 16 | 6);
        require(GodWarsWidgets.load(506)
                && Class37.getWidget(506 << 16 | 6) == count, "reloading preserves updated counters");
        // Reproduce the reported stale legacy ID during packet 219 cleanup.
        Class26.anInt485 = 19556;
        overlayPacket(506);
        overlayPacket(-1);
        require(Class62_Sub1.widgets[506][6] == count, "closing preserves runtime widget storage");
        overlayPacket(506);
        Class68.method1112(19556, (byte)110);
        Class68.method1112(65535, (byte)110);
        require(count.aClass3_2029.isEqual(GrandExchangeWidgets.literal("40")), "counter survives close and reopen");
        System.out.println("God Wars overlay packet and lifecycle checks passed.");
    }
}
