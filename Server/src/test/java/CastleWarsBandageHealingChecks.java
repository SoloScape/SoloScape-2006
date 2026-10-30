import com.rs2.model.gameplay.castlewars.CastleWarsManager;

/** Castle Wars self-healing and teammate-healing must share floor-rounded 10% healing. */
public final class CastleWarsBandageHealingChecks {
    public static void main(String[] args) {
        check(1, 0);
        check(9, 0);
        check(10, 1);
        check(74, 7);
        check(99, 9);
        check(100, 10);
        check(109, 10);
        check(120, 12);
        System.out.println("Castle Wars bandage healing checks passed");
    }

    private static void check(int maxHitpoints, int expectedHealing) {
        int actual = CastleWarsManager.calculateBandageHealing(maxHitpoints);
        if (actual != expectedHealing) {
            throw new AssertionError("Expected " + expectedHealing
                    + " bandage healing for " + maxHitpoints + " max HP, got " + actual);
        }
    }
}
