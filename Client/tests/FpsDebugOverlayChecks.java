package unpackaged;

/** Lightweight regressions for the ::fps command and diagnostic labels. */
public final class FpsDebugOverlayChecks {
    public static void main(String[] args) {
        OndemandRequest.aBoolean1718 = false;
        FpsDebugOverlay.toggle();
        require(OndemandRequest.aBoolean1718, "First ::fps must show diagnostics");
        FpsDebugOverlay.toggle();
        require(!OndemandRequest.aBoolean1718, "Second ::fps must hide diagnostics");

        String[] lines = FpsDebugOverlay.formatLines(60, 42,
                48L * 1024 * 1024, 96L * 1024 * 1024, 256L * 1024 * 1024);
        require(lines.length == 4, "Expected four diagnostics");
        require(lines[0].equals("Framerate: 60 FPS"), "Wrong framerate");
        require(lines[1].equals("Latency: 42 ms RTT"), "Wrong latency");
        require(lines[2].equals("RAM usage: 48 MB"), "Wrong used heap");
        require(lines[3].equals("Memory: 96 / 256 MB"), "Wrong committed/max heap");
        require(FpsDebugOverlay.formatLines(0, -1, 0, 0, -1)[1]
                .equals("Latency: N/A"), "Unavailable latency must not be fabricated");
        System.out.println("FPS diagnostics checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
