package unpackaged;

import jagex.io.FrameBuffer;

/**
 * Client-side ::fps diagnostics. The ping is an application-level round trip
 * to the game server, not an estimate based on time between server updates.
 */
public final class FpsDebugOverlay {
    private static final int PING_OPCODE = 254;
    private static final long PING_INTERVAL_NANOS = 2_000_000_000L;
    private static final long PING_TIMEOUT_NANOS = 5_000_000_000L;
    private static final int YELLOW = 0xFFFF00;

    private static long lastSentNanos;
    private static long pendingSinceNanos;
    private static int nextPingId;
    private static int pendingPingId;
    private static int latencyMillis = -1;

    private FpsDebugOverlay() {
    }

    public static void toggle() {
        OndemandRequest.aBoolean1718 = !OndemandRequest.aBoolean1718;
        clearPing();
    }

    private static void clearPing() {
        lastSentNanos = 0L;
        pendingSinceNanos = 0L;
        latencyMillis = -1;
    }

    /** Called on the game tick before the outgoing packet buffer is flushed. */
    public static void tick() {
        if (!OndemandRequest.aBoolean1718 || Class37.gameSocket == null) {
            clearPing();
            return;
        }
        long now = System.nanoTime();
        if (pendingSinceNanos != 0L) {
            if (now - pendingSinceNanos < PING_TIMEOUT_NANOS) {
                return;
            }
            // A lost response must not leave a stale latency value displayed.
            pendingSinceNanos = 0L;
            latencyMillis = -1;
        }
        if (lastSentNanos != 0L && now - lastSentNanos < PING_INTERVAL_NANOS) {
            return;
        }
        pendingPingId = ++nextPingId;
        lastSentNanos = now;
        pendingSinceNanos = now;
        FrameBuffer.outgoingGameBuffer.putFrame(PING_OPCODE);
        FrameBuffer.outgoingGameBuffer.putDword(pendingPingId);
    }

    /** A reply is accepted only for the current in-flight request. */
    public static void onPingReply(int pingId) {
        if (!OndemandRequest.aBoolean1718 || pendingSinceNanos == 0L
                || pingId != pendingPingId) {
            return;
        }
        long duration = System.nanoTime() - pendingSinceNanos;
        if (duration >= 0L) {
            latencyMillis = (int) Math.min(Integer.MAX_VALUE,
                    (duration + 500_000L) / 1_000_000L);
        }
        pendingSinceNanos = 0L;
    }

    public static void draw() {
        if (!OndemandRequest.aBoolean1718 || Class39_Sub5_Sub14.p12fullFont == null) {
            return;
        }
        Runtime runtime = Runtime.getRuntime();
        long allocated = runtime.totalMemory();
        String[] lines = formatLines(Class39_Sub5_Sub18.anInt2119, latencyMillis,
                allocated - runtime.freeMemory(), allocated, runtime.maxMemory());
        // The scene framebuffer is 512 pixels wide. method630 right-aligns text.
        for (int index = 0; index < lines.length; index++) {
            Class39_Sub5_Sub14.p12fullFont.method630(
                    Class39_Sub5_Sub9.createJstring(lines[index]),
                    507, 20 + index * 15, YELLOW);
        }
    }

    static String[] formatLines(int fps, int pingMs, long used, long allocated, long maximum) {
        return new String[] {
                "Framerate: " + fps + " FPS",
                "Latency: " + (pingMs < 0 ? "N/A" : pingMs + " ms RTT"),
                "RAM usage: " + toMiB(used) + " MB",
                "Memory: " + toMiB(allocated) + " / "
                        + (maximum <= 0L ? "N/A" : toMiB(maximum) + " MB")
        };
    }

    private static long toMiB(long bytes) {
        return Math.max(0L, bytes) / (1024L * 1024L);
    }
}
