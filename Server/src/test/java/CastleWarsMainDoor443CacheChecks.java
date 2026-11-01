import com.rs2.ServerSettings;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.interaction.ObjectActionRouter;
import com.rs2.model.interaction.InteractionType;

/** Prints and verifies the actual 443 gate object actions (not made-up test labels). */
public final class CastleWarsMainDoor443CacheChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ObjectDefinition.loadRevision443();
        int[] inspectIds = {1568, 1747, 4911, 4471, 4472, 6280, 6281};
        for (int id : inspectIds) printDefinition(id);
        for (int id = 4418; id <= 4435; ++id) printDefinition(id);
    }

    private static void printDefinition(int id) {

            ObjectDefinition def = ObjectDefinition.forId(id);
            StringBuilder actions = new StringBuilder();
            for (int slot = 0; slot < 5; ++slot) {
                String action = def.getAction(slot);
                actions.append(" [").append(slot + 1).append(":").append(action)
                        .append("->").append(ObjectActionRouter.semanticRoute(def, slot)).append("]");
            }
            System.out.println("GATE443 id=" + id + " name=" + def.getName() + actions);
    }
}
