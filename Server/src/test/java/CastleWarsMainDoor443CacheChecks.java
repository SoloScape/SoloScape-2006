import com.rs2.ServerSettings;
import com.rs2.cache.js5.Definitions;
import com.rs2.util.ByteArrayReader;
import java.util.Map;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.interaction.ObjectActionRouter;
import com.rs2.model.interaction.InteractionType;

/** Prints and verifies the actual 443 gate object actions (not made-up test labels). */
public final class CastleWarsMainDoor443CacheChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ObjectDefinition.loadRevision443();
        Map<Integer, byte[]> raw = Definitions.readGroup(6);
        for (int id : new int[]{1568, 1569, 1570, 1571, 4471, 4472}) {
            byte[] bytes = raw.get(id);
            if (bytes == null) continue;
            ByteArrayReader reader = new ByteArrayReader(bytes);
            int first = reader.readUnsignedByte();
            StringBuilder shapes = new StringBuilder();
            if (first == 1) {
                int count = reader.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    int model = reader.readUnsignedShort();
                    int type = reader.readUnsignedByte();
                    shapes.append(" model=").append(model).append(":type=").append(type);
                }
            } else if (first == 5) {
                int count = reader.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    shapes.append(" model=").append(reader.readUnsignedShort())
                            .append(":type=10");
                }
            }
            System.out.println("TRAPDOOR-MODEL id=" + id + " firstOpcode=" + first
                    + " shapes=" + shapes.toString());
        }
        int[] inspectIds = {1568, 1747, 4911, 4471, 4472, 6280, 6281};
        for (int id = 1568; id <= 1577; id++) printDefinition(id);
        for (int id = 4471; id <= 4480; id++) printDefinition(id);
        for (int id = 6278; id <= 6290; id++) printDefinition(id);
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
