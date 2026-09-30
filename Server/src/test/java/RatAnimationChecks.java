import com.rs2.cache.js5.ConfigReader;
import com.rs2.cache.js5.Definitions;
import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.model.npc.NpcDefinition;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

/** Checks giant-rat combat sequences against the actual bundled 443 skeleton. */
public final class RatAnimationChecks {
    public static void main(String[] args) throws Exception {
        NpcDefinition.loadDefinitions();
        int[] giantRats = {86, 87, 88, 224, 446, 748, 950, 978, 2033, 2722,
                2723, 3647, 3662, 4920, 4921, 4922, 4923, 4924, 4925, 4926,
                4927, 4928, 4929, 4936, 4937, 4938, 4939, 4940, 4941, 4942,
                4943, 4944, 4945};
        for (int id : giantRats) {
            NpcDefinition rat = NpcDefinition.forId(id);
            require(rat.getAttackAnimationId() == 4933, "Attack for rat " + id);
            require(rat.getBlockAnimationId() == 4934, "Block for rat " + id);
            require(rat.getDeathAnimationId() == 4935, "Death for rat " + id);
        }
        for (int id : new int[] {47, 2682}) {
            NpcDefinition rat = NpcDefinition.forId(id);
            require(rat.getAttackAnimationId() == 2705, "Small rat attack " + id);
            require(rat.getBlockAnimationId() == 2706, "Small rat block " + id);
            require(rat.getDeathAnimationId() == 2707, "Small rat death " + id);
        }
        // An old-rig idle must not receive the modern combat sequences.
        Method decode = NpcDefinition.class.getDeclaredMethod("decodeRevision443",
                NpcDefinition.class, byte[].class);
        decode.setAccessible(true);
        NpcDefinition oldRig = new NpcDefinition();
        decode.invoke(null, oldRig, new byte[] {13, 0, (byte) 137, 0});
        require(oldRig.getAttackAnimationId() == 422, "Other rigs were changed");

        Map<Integer, byte[]> sequences = Definitions.readGroup(12);
        try (Js5CacheStore cache = new Js5CacheStore(new File("cache"))) {
            int skeleton = skeletons(cache, sequences.get(4932), -1);
            for (int id : new int[] {4931, 4933, 4934, 4935}) {
                skeletons(cache, sequences.get(id), skeleton);
            }
            require(skeletons(cache, sequences.get(138), -1) != skeleton,
                    "Legacy attack unexpectedly matches the modern skeleton");
        }
        System.out.println("Rat animation checks passed");
    }

    private static int skeletons(Js5CacheStore cache, byte[] sequence, int expected)
            throws Exception {
        ConfigReader reader = new ConfigReader(sequence);
        int opcode;
        while ((opcode = reader.readUnsignedByte()) != 1) {
            if (opcode == 2 || opcode == 6 || opcode == 7) reader.skip(2);
            else if (opcode == 3) reader.skip(reader.readUnsignedByte());
            else if (opcode == 4) { /* Boolean flag. */ }
            else if (opcode >= 5 && opcode <= 11) reader.skip(1);
            else throw new AssertionError("Unexpected sequence opcode " + opcode);
        }
        int count = reader.readUnsignedByte();
        reader.skip(count * 2); // Frame durations.
        int[] frames = new int[count];
        for (int i = 0; i < count; i++) frames[i] = reader.readUnsignedShort();
        for (int i = 0; i < count; i++) frames[i] |= reader.readUnsignedShort() << 16;
        for (int frame : frames) {
            byte[] data = cache.readFiles(0, frame >>> 16).get(frame & 65535);
            int skeleton = (data[0] & 255) << 8 | (data[1] & 255);
            if (expected == -1) expected = skeleton;
            require(skeleton == expected, "Animation frame uses a different skeleton");
        }
        return expected;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
