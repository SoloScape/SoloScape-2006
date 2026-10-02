import com.rs2.ConfigFile;
import com.rs2.ServerSettings;
import com.rs2.model.World;
import com.rs2.model.player.Player;
import com.rs2.model.skill.woodcutting.TreeDefinition;

public final class TreeRespawnPopulationChecks {
    public static void main(String[] args) {
        int originalPopulation = ServerSettings.effectiveWorldPopulation;
        Player originalPlayer = World.players[1];
        try {
            TreeDefinition[] trees = {TreeDefinition.OAK, TreeDefinition.WILLOW,
                TreeDefinition.MAPLE, TreeDefinition.YEW, TreeDefinition.MAGIC};
            int[] populations = {750, 1000, 1250, 1500, 1750, 2000};
            int[][] targetMillis = {
                {13200, 12200, 11200, 10200, 9200, 8200},
                {13200, 12200, 11200, 10200, 9200, 8200},
                {60000, 55000, 50000, 45000, 40000, 35000},
                {97500, 90000, 82500, 75000, 67500, 60000},
                {190000, 176000, 162000, 148000, 134000, 120000}
            };
            for (int tree = 0; tree < trees.length; tree++) {
                for (int sample = 0; sample < populations.length; sample++) {
                    int elapsedMillis = (trees[tree].getRespawnTicks(populations[sample]) + 1) * 600;
                    int target = targetMillis[tree][sample];
                    require(elapsedMillis >= target && elapsedMillis < target + 600,
                        trees[tree] + " at " + populations[sample] + ": " + elapsedMillis);
                }
                int previous = trees[tree].getRespawnTicks(0);
                for (int population = 1; population <= 2000; population++) {
                    int ticks = trees[tree].getRespawnTicks(population);
                    require(ticks > 0 && ticks <= previous, "Non-monotonic delay");
                    previous = ticks;
                }
                require(trees[tree].getRespawnTicks(Integer.MIN_VALUE) == trees[tree].getRespawnTicks(0), "Lower clamp");
                require(trees[tree].getRespawnTicks(Integer.MAX_VALUE) == previous, "Upper clamp");
            }
            ConfigFile.applyConfigEntry("EFFECTIVE_WORLD_POPULATION", new String[] {"1250"});
            require(ServerSettings.effectiveWorldPopulation == 1250, "Config parsing");
            require(TreeDefinition.MAGIC.getRespawnTicks() == 269, "Configured population selection");
            ConfigFile.applyConfigEntry("EFFECTIVE_WORLD_POPULATION", new String[] {"9999"});
            require(ServerSettings.effectiveWorldPopulation == 2000, "Config upper clamp");
            ConfigFile.applyConfigEntry("EFFECTIVE_WORLD_POPULATION", new String[] {"-99"});
            require(ServerSettings.effectiveWorldPopulation == -1, "Config lower clamp");
            require(TreeDefinition.MAGIC.getRespawnTicks() == TreeDefinition.MAGIC.getRespawnTicks(World.getPlayerCount()), "Live population selection");
            Player bot = new Player(null);
            bot.isBot = true;
            World.players[1] = bot;
            require(World.getPlayerCount() == 1, "Live population includes bots");
            require(TreeDefinition.MAGIC.getRespawnTicks() == TreeDefinition.MAGIC.getRespawnTicks(1), "Live bot population selection");
            for (TreeDefinition tree : TreeDefinition.values()) {
                if (tree == TreeDefinition.OAK || tree == TreeDefinition.WILLOW || tree == TreeDefinition.MAPLE
                    || tree == TreeDefinition.YEW || tree == TreeDefinition.MAGIC) continue;
                for (int sample = 0; sample < 100; sample++) {
                    int ticks = tree.getRespawnTicks(sample * 20);
                    require(ticks >= tree.getRespawnTicksLow() && ticks <= tree.getRespawnTicksHigh(), "Legacy delay: " + tree);
                }
            }
        } finally {
            World.players[1] = originalPlayer;
            ServerSettings.effectiveWorldPopulation = originalPopulation;
        }
        System.out.println("Tree respawn population checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
