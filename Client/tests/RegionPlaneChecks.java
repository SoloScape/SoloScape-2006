package unpackaged;

import jagex.utils.Deque;

/** Regression checks for the Class65:38 region-load crash (plane 246). */
public final class RegionPlaneChecks {
    public static void main(String[] args) {
        for (int plane = 0; plane < 4; plane++) {
            require(OndemandRequest.safeRegionPlane(plane) == plane,
                    "Valid map plane must be preserved: " + plane);
        }
        require(OndemandRequest.safeRegionPlane(246) == 0,
                "Unsigned -10 plane must recover instead of crashing");
        require(OndemandRequest.safeRegionPlane(-10) == 0,
                "Negative world plane must recover");
        require(OndemandRequest.safeRegionPlane(4) == 0,
                "An out-of-range fifth plane must recover");

        int savedPlane = NameTable.height;
        Deque[][][] savedGroundItems = Class20.groundItems;
        try {
            Class20.groundItems = new Deque[4][104][104];
            NameTable.height = 246;
            Class65.updateGroundItems(0, 0);
            NameTable.height = -10;
            Class65.updateGroundItems(0, 0);
            // The absence of an exception matters; map-packet readers own
            // recovery to a valid plane before the region is drawn.
        } finally {
            NameTable.height = savedPlane;
            Class20.groundItems = savedGroundItems;
        }
        System.out.println("Revision 443 region-plane client checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
