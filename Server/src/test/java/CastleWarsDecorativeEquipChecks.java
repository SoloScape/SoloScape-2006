import com.rs2.ServerSettings;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.quest.QuestDefinition;

/** Inventory action and equipment metadata audit for Lanthus's full reward range. */
public final class CastleWarsDecorativeEquipChecks {
    private static final int[][] REWARDS = {
        {4071, 0}, {4072, 5}, {4070, 7}, {4069, 4}, {4068, 3},
        {4506, 0}, {4507, 5}, {4505, 7}, {4504, 4}, {4503, 3},
        {4511, 0}, {4512, 5}, {4510, 7}, {4509, 4}, {4508, 3},
        {4516, 1}, {4515, 0}, {4514, 1}, {4513, 0}
    };

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.cacheVersion = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        int mismatches = 0;
        for (int[] reward : REWARDS) {
            int id = reward[0], expectedSlot = reward[1];
            ItemDefinition def = ItemDefinition.forId(id);
            StringBuilder actions = new StringBuilder();
            for (int slot = 0; slot < 5; slot++) {
                actions.append(slot).append('=').append(def.getInventoryAction(slot)).append(' ');
            }
            System.out.println("LANTHUS-EQUIP id=" + id + " name=" + def.getName()
                    + " slot=" + def.getEquipmentSlot() + " expected=" + expectedSlot
                    + " actions=" + actions.toString());
            if (def.getEquipmentSlot() != expectedSlot) mismatches++;
        }
        System.out.println("LANTHUS-EQUIP slot mismatches=" + mismatches);
    }
}
