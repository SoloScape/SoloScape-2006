import com.rs2.model.skill.woodcutting.BirdNestDropTable;
import com.rs2.util.GameUtil;
import com.rs2.ServerSettings;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

/** Enumerate every possible RNG outcome to verify exact nest weights. */
public final class BirdNestDropTableChecks {
    public static void main(String[] args) throws Exception {
        Field random = GameUtil.class.getDeclaredField("random");
        random.setAccessible(true);
        Object original = random.get(null);
        try {
            check(random, false, 100, new int[] {1, 1, 1, 65, 32});
            check(random, true, 95, new int[] {1, 1, 1, 60, 32});
            ServerSettings.clientBuild = 443;
            QuestDefinition.loadDefinitions();
            ItemDefinition.loadDefinitions();
            Player player = new Player(null);
            checkFrequency(random, player, 1);
            player.getInventoryManager().getContainer().setItem(0, new ItemStack(10132));
            checkFrequency(random, player, 1);
            player.getEquipmentManager().getContainer().setItem(2, new ItemStack(10134));
            checkFrequency(random, player, 1);
            player.getEquipmentManager().getContainer().setItem(2, new ItemStack(10132));
            checkFrequency(random, player, 2);
            player.getEquipmentManager().getContainer().setItem(2, null);
            checkFrequency(random, player, 1);
        } finally {
            random.set(null, original);
        }
        System.out.println("Bird nest distribution and rabbit-foot frequency checks passed.");
    }

    private static void checkFrequency(Field random, Player player, int expected) throws Exception {
        for (final int slots : new int[] {256, 257}) {
            int drops = 0;
            for (int roll = 0; roll < slots; roll++) {
                final int outcome = roll;
                random.set(null, new Random() {
                    @Override
                    public int nextInt(int bound) {
                        if (bound != slots) throw new AssertionError("Wrong frequency bound: " + bound);
                        return outcome;
                    }
                });
                if (BirdNestDropTable.shouldDropNest(player, slots)) drops++;
            }
            if (drops != expected) throw new AssertionError("Wrong nest frequency: " + drops + "/" + slots);
        }
    }

    private static void check(Field random, boolean rabbitFoot, final int slots, int[] expected) throws Exception {
        int[] counts = new int[5];
        for (int roll = 0; roll < slots; roll++) {
            final int outcome = roll;
            random.set(null, new Random() {
                @Override
                public int nextInt(int bound) {
                    if (bound != slots) throw new AssertionError("Wrong nest roll bound: " + bound);
                    return outcome;
                }
            });
            int id = BirdNestDropTable.rollNest(rabbitFoot);
            if (id < 5070 || id > 5074) throw new AssertionError("Invalid nest: " + id);
            counts[id - 5070]++;
        }
        if (!Arrays.equals(counts, expected)) {
            throw new AssertionError("Incorrect nest weights: " + Arrays.toString(counts));
        }
    }
}
