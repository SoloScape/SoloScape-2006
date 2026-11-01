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
            String shapes = modelShapes(bytes);
            System.out.println("TRAPDOOR-MODEL id=" + id + " firstOpcode=" + first
                    + " shapes=" + shapes);
        }
        int[] inspectIds = {1568, 1747, 4911, 4471, 4472, 6280, 6281};
        for (int id = 1568; id <= 1577; id++) printDefinition(id);
        for (int id = 4471; id <= 4480; id++) printDefinition(id);
        for (int id = 6278; id <= 6290; id++) printDefinition(id);
        for (int id : inspectIds) printDefinition(id);
        for (int id = 4418; id <= 4435; ++id) printDefinition(id);
    }

    private static String modelShapes(byte[] bytes) throws Exception {
        ByteArrayReader reader = new ByteArrayReader(bytes);
        StringBuilder shapes = new StringBuilder();
        while (reader.position < bytes.length) {
            int op = reader.readUnsignedByte();
            if (op == 0) break;
            if (op == 1 || op == 5) {
                int count = reader.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    int model = reader.readUnsignedShort();
                    int type = op == 1 ? reader.readUnsignedByte() : 10;
                    shapes.append(" ").append(model).append(":").append(type);
                }
            } else if (op == 2 || op == 3 || (op >= 30 && op <= 34)) {
                while (reader.readUnsignedByte() != 0) { }
            } else if (op == 14 || op == 15 || op == 19 || op == 28 || op == 69
                    || op == 75 || op == 81) {
                reader.readUnsignedByte();
            } else if (op == 17 || op == 18 || op == 21 || op == 22 || op == 23
                    || op == 27 || op == 62 || op == 64 || op == 73 || op == 74) {
                // No payload.
            } else if (op == 24 || op == 60 || op == 65 || op == 66 || op == 67
                    || op == 68 || op == 82) {
                reader.readUnsignedShort();
            } else if (op == 29 || op == 39) {
                reader.readByte();
            } else if (op == 40 || op == 41) {
                int n = reader.readUnsignedByte();
                for (int i = 0; i < n; i++) {
                    reader.readUnsignedShort();
                    reader.readUnsignedShort();
                }
            } else if (op == 70 || op == 71 || op == 72) {
                reader.readShort();
            } else if (op == 77 || op == 92) {
                reader.readUnsignedShort();
                reader.readUnsignedShort();
                if (op == 92) reader.readUnsignedShort();
                int n = reader.readUnsignedByte();
                for (int i = 0; i <= n; i++) reader.readUnsignedShort();
            } else if (op == 78) {
                reader.readUnsignedShort();
                reader.readUnsignedByte();
            } else if (op == 79) {
                reader.readUnsignedShort();
                reader.readUnsignedShort();
                reader.readUnsignedByte();
                int n = reader.readUnsignedByte();
                for (int i = 0; i < n; i++) reader.readUnsignedShort();
            } else if (op == 249) {
                int n = reader.readUnsignedByte();
                for (int i = 0; i < n; i++) {
                    boolean str = reader.readUnsignedByte() == 1;
                    reader.readUnsignedByte(); reader.readUnsignedByte();
                    reader.readUnsignedByte();
                    if (str) {
                        while (reader.readUnsignedByte() != 0) {}
                    } else {
                        reader.readInt();
                    }
                }
            } else {
                throw new IllegalStateException("Unknown opcode " + op);
            }
        }
        return shapes.toString();
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
