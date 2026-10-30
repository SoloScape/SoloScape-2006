import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.task.CycleEventHandler;

import java.lang.reflect.Field;
import java.util.Map;

/** Rockslide pickaxes must mine over several ticks; potions must remain instant. */
public final class CastleWarsRockslideMiningChecks {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Player player = new Player(null);
        player.setIndex(1);
        World.players[1] = player;
        Field teamsField = CastleWarsManager.class.getDeclaredField("gamePlayers");
        teamsField.setAccessible(true);
        Map<Player, CastleWarsManager.Team> teams =
                (Map<Player, CastleWarsManager.Team>) teamsField.get(null);
        teams.put(player, CastleWarsManager.Team.SARADOMIN);
        try {
            player.setPosition(new Position(2393, 9501, 0));
            player.getSkillManager().getCurrentLevels()[14] = 1;
            player.getInventoryManager().addItem(new ItemStack(
                    CastleWarsEngineeringManager.BRONZE_PICKAXE_ID, 1));
            require(CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Rockslide should start collapsed");

            require(CastleWarsEngineeringManager.clearRockslideWithPickaxe(player, 0),
                    "Pickaxe mining did not start");
            require(CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Pickaxe cleared rockslide immediately");
            for (int i = 0; i < 7; ++i) {
                tick();
                require(CastleWarsEngineeringManager.isRockslideCollapsed(0),
                        "Bronze mining finished before eight ticks");
                // Repeated bot interactions must not restart the mining timer.
                if (i == 2) {
                    require(CastleWarsEngineeringManager.clearRockslideWithPickaxe(player, 0),
                            "Repeated mining request was not handled");
                }
            }
            tick();
            require(!CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Bronze pickaxe did not finish clearing after eight ticks");

            // Movement interrupts a collapse before it can crush another player.
            require(CastleWarsEngineeringManager.collapseRockslide(player, 0),
                    "Collapse mining did not start");
            tick();
            player.setPosition(new Position(2394, 9501, 0));
            tick(10);
            require(!CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Moving away did not cancel pickaxe collapse");

            player.setPosition(new Position(2393, 9501, 0));
            player.getInventoryManager().addItem(new ItemStack(
                    CastleWarsEngineeringManager.EXPLOSIVE_POTION_ID, 1));
            require(CastleWarsEngineeringManager.collapseRockslide(player, 0),
                    "Explosive collapse failed");
            require(CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Explosive did not collapse the tunnel immediately");
            require(player.getInventoryManager().getItemAmount(
                    CastleWarsEngineeringManager.EXPLOSIVE_POTION_ID) == 0,
                    "Explosive potion was not consumed");

            require(CastleWarsEngineeringManager.clearRockslideWithPickaxe(player, 0),
                    "Second mining action did not start");
            tick();
            player.nextActionSequence();
            tick(10);
            require(CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "A newer action did not cancel mining");

            require(CastleWarsEngineeringManager.clearRockslideWithPickaxe(player, 0),
                    "Final mining action did not start");
            tick(2);
            player.getInventoryManager().addItem(new ItemStack(
                    CastleWarsEngineeringManager.EXPLOSIVE_POTION_ID, 1));
            require(CastleWarsEngineeringManager.clearRockslide(player, 0),
                    "Explosive clearing failed");
            require(!CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Explosive did not clear the passage immediately");
            tick(10);
            require(!CastleWarsEngineeringManager.isRockslideCollapsed(0),
                    "Stale mining action changed the passage after an explosive");
            System.out.println("Castle Wars rockslide mining timing and cancellation checks passed");
        } finally {
            teams.remove(player);
            World.players[1] = null;
            player.setIndex(-1);
        }
    }

    private static void tick() {
        CycleEventHandler.getInstance().process();
    }

    private static void tick(int count) {
        for (int i = 0; i < count; ++i) {
            tick();
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
