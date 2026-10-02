import com.rs2.ServerSettings;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.ItemCombinationHandler;
import com.rs2.model.skill.SkillManager;
import com.rs2.model.skill.guide.SkillGuideCategory;
import com.rs2.model.skill.guide.SkillGuideEntry;
import com.rs2.model.skill.guide.SkillGuideManager;
import java.lang.reflect.Field;
import java.util.List;

/** Revision 443 black axe equipment, gathering and guide requirements. */
public final class BlackAxeRequirementChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        ItemDefinition axe = ItemDefinition.forId(1361);
        require(axe.getRequiredLevel(8) == 6, "Black axe equip requirement must be 6 Woodcutting");
        require(axe.getRequiredLevel(0) == 10, "Black axe must still require 10 Attack");
        require(GatheringToolDefinition.BLACK_AXE.getRequiredLevel() == 6,
                "Black axe gathering requirement must be 6 Woodcutting");

        Player player = new Player(null);
        player.getSkillManager().getExperience()[0] = SkillManager.getExperienceForLevel(9);
        player.getInventoryManager().getContainer().add(new ItemStack(1361), 0);
        for (int level : new int[] {5, 6, 10, 11}) {
            player.getSkillManager().getExperience()[8] = SkillManager.getExperienceForLevel(level - 1);
            player.getSkillManager().getCurrentLevels()[8] = level;
            require(player.getEquipmentManager().canEquipItem(1361) == (level >= 6),
                    "Incorrect equip eligibility at Woodcutting " + level);
            require(ItemCombinationHandler.findUsableGatheringTool(player, 8)
                    == (level >= 6 ? GatheringToolDefinition.BLACK_AXE : null),
                    "Incorrect gathering eligibility at Woodcutting " + level);
        }
        player.getSkillManager().getExperience()[0] = SkillManager.getExperienceForLevel(8);
        require(!player.getEquipmentManager().canEquipItem(1361), "Attack requirement bypassed");

        SkillGuideManager.initialize();
        Field categories = SkillGuideManager.class.getDeclaredField("woodcuttingCategories");
        categories.setAccessible(true);
        boolean found = false;
        for (Object category : (List<?>) categories.get(null)) {
            for (Object row : ((SkillGuideCategory) category).entries) {
                SkillGuideEntry entry = (SkillGuideEntry) row;
                if (entry.itemId == 1361) {
                    require("6".equals(entry.getLevelText()), "Guide must show level 6");
                    found = true;
                }
            }
        }
        require(found, "Black axe missing from Woodcutting guide");
        System.out.println("Black axe requirement checks passed.");
        System.exit(0);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
