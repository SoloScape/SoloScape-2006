import com.rs2.ServerSettings;
import com.rs2.model.combat.AttackBonusType;
import com.rs2.model.combat.AttackStyleDefinition;
import com.rs2.model.combat.AttackXpMode;
import com.rs2.model.combat.CombatAction;
import com.rs2.model.combat.CombatType;
import com.rs2.model.combat.hit.HitDefinition;
import com.rs2.model.combat.hit.HitType;
import com.rs2.model.player.Player;

/** Incoming combat hits preserve tutorial instructions but close ordinary interfaces. */
public final class TutorialCombatOverlayChecks {
    public static void main(String[] args) {
        ServerSettings.clientBuild = 443;
        for (int stage : new int[] {47, 48, 49, 50, 1}) {
            for (int interfaceId : new int[] {6179, 15106, 4882, 0}) {
                for (boolean successful : new boolean[] {false, true}) {
                    Player player = new Player(null);
                    player.setEncodedIndex(32768);
                    player.isBot = true;
                    player.setQuestState(0, stage);
                    player.setOpenInterfaceId(interfaceId);
                    player.getAttributes().put("canTakeDamage", Boolean.TRUE);
                    player.setCurrentHitpoints(10);
                    HitDefinition hit = new HitDefinition(new AttackStyleDefinition(
                            CombatType.MELEE, AttackXpMode.MELEE_ACCURATE,
                            AttackBonusType.STAB), HitType.NORMAL, 1)
                            .setAlwaysHits(successful).setBlockAnimationEnabled(false);
                    // No attacker is needed to exercise incoming-hit interface handling.
                    new CombatAction(null, player, hit).applyHit();
                    boolean preserve = stage == 47 || stage == 49
                            || stage != 1 && interfaceId == 6179;
                    require(player.getOpenInterfaceId() == (preserve ? 6179 : 0),
                            "Wrong interface after hit: stage=" + stage + " interface="
                            + interfaceId + " successful=" + successful);
                    require(player.getCurrentHitpoints() == (successful ? 9 : 10),
                            "Damage changed while preserving instructions");
                    require(player.getQuestState(0) == stage, "Incoming hit advanced tutorial");
                }
            }
        }
        System.out.println("Tutorial combat overlay checks passed (melee/ranging stages, hits/misses, ordinary interfaces).");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
