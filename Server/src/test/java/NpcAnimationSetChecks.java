import com.rs2.cache.js5.ConfigReader;
import com.rs2.cache.js5.Definitions;
import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.model.npc.NpcDefinition;
import java.io.File;
import java.io.DataInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** Verifies every frame of every configured combat set against its idle rig. */
public final class NpcAnimationSetChecks {
    private static final Map<Integer, Map<Integer, byte[]>> FRAME_GROUPS =
            new HashMap<Integer, Map<Integer, byte[]>>();

    public static void main(String[] args) throws Exception {
        NpcDefinition.loadDefinitions();
        Map<Integer, byte[]> sequences = Definitions.readGroup(12);
        int sets = 0;
        long tableBytes = Files.size(Paths.get("data/npcs/animations.dat"));
        require(tableBytes > 0 && tableBytes % 16 == 0, "Invalid binary animation table length");
        try (Js5CacheStore cache = new Js5CacheStore(new File("cache"));
                DataInputStream table = new DataInputStream(
                        Files.newInputStream(Paths.get("data/npcs/animations.dat")))) {
            for (long record = 0; record < tableBytes / 16; record++) {
                int idle = table.readInt();
                int rig = verifySequence(cache, sequences.get(idle), -1, idle);
                for (int i = 1; i < 4; i++) {
                    int sequence = table.readInt();
                    if (sequence != -1) {
                        verifySequence(cache, sequences.get(sequence), rig, sequence);
                    }
                }
                sets++;
            }
            // The specialized metal-dragon breath is separate from its melee set.
            int dragonRig = verifySequence(cache, sequences.get(4639), -1, 4639);
            verifySequence(cache, sequences.get(4637), dragonRig, 4637);
        }
        // Includes transformed quest forms, cubs, and distinct giant weapons.
        check(58, 5327, 5328, 5329);
        check(997, 5319, 5320, 5321);
        check(78, 4915, 4916, 4917);
        check(899, 4925, 4927, 4929);
        check(1196, 4925, 4927, 4929);
        check(117, 4652, 4651, 4653);
        check(112, 4666, 4665, 4668);
        check(110, 4666, 4664, 4668);
        check(111, 4672, 4671, 4673);
        check(1020, 4868, 4869, 4870);
        check(3581, 260, 261, 263);
        check(3648, 309, 312, 313);
        check(1558, 75, 76, 78);
        check(3340, 3312, 3311, 3310);
        check(61, 280, 281, 282);
        check(1477, 246, 247, 248);
        check(1479, 275, 276, 278);
        check(1532, -1, -1, -1);
        check(1475, 343, 344, 345);
        check(1533, -1, -1, -1);
        check(3202, -1, -1, -1);
        check(3851, -1, -1, -1);
        check(459, 412, 403, 836);
        check(1590, 4636, 4640, 4641);
        check(1591, 4636, 4640, 4641);
        check(1592, 4636, 4640, 4641);
        check(1241, 1284, 1283, 1287);
        check(69, 177, 178, 180);
        check(840, 49, 50, 52);
        check(1678, 1612, 1613, 1611);
        check(3201, 3163, 3164, 3162);
        check(3496, 3508, 3505, 3509);
        require(NpcDefinition.forId(1590).resolveAttackAnimationId(81) == 4637,
                "Metal dragon breath uses legacy rig");
        require(NpcDefinition.forId(1590).resolveAttackAnimationId(80) == 4636,
                "Metal dragon melee uses legacy rig");
        // Existing human weapon choices and absent block reactions must survive.
        check(1, 422, 1834, 836);
        check(9, 412, 403, 836);
        require(NpcDefinition.forId(1600).getBlockAnimationId() == -1,
                "Cave crawler acquired a block reaction");
        require(NpcDefinition.forId(110).resolveAttackAnimationId(128) == 4666,
                "Hard-coded giant attack bypasses the new set");
        require(NpcDefinition.forId(909).resolveAttackAnimationId(143) == 5319,
                "Hard-coded spider attack bypasses the new set");
        require(NpcDefinition.forId(55).resolveAttackAnimationId(81) == 81,
                "Dragonfire animation was overwritten");
        System.out.println("NPC animation checks passed: " + sets + " complete cache sets");
    }

    private static int verifySequence(Js5CacheStore cache, byte[] data,
            int expectedRig, int id) throws Exception {
        require(data != null, "Missing sequence " + id);
        ConfigReader reader = new ConfigReader(data);
        int opcode;
        while ((opcode = reader.readUnsignedByte()) != 1) {
            if (opcode == 2 || opcode == 6 || opcode == 7) reader.skip(2);
            else if (opcode == 3) reader.skip(reader.readUnsignedByte());
            else if (opcode == 4) { /* Boolean flag. */ }
            else if (opcode >= 5 && opcode <= 11) reader.skip(1);
            else throw new AssertionError("Unexpected sequence opcode " + opcode);
        }
        int count = reader.readUnsignedByte();
        require(count > 0, "Empty sequence " + id);
        reader.skip(count * 2);
        int[] frames = new int[count];
        for (int i = 0; i < count; i++) frames[i] = reader.readUnsignedShort();
        for (int i = 0; i < count; i++) frames[i] |= reader.readUnsignedShort() << 16;
        for (int frame : frames) {
            int group = frame >>> 16;
            Map<Integer, byte[]> files = FRAME_GROUPS.get(group);
            if (files == null) {
                files = cache.readFiles(0, group);
                FRAME_GROUPS.put(group, files);
            }
            byte[] bytes = files.get(frame & 65535);
            require(bytes != null, "Missing frame in sequence " + id);
            int rig = (bytes[0] & 255) << 8 | (bytes[1] & 255);
            if (expectedRig == -1) expectedRig = rig;
            require(rig == expectedRig, "Sequence " + id + " frame uses rig " + rig
                    + ", expected " + expectedRig);
        }
        return expectedRig;
    }

    private static void check(int id, int attack, int block, int death) {
        NpcDefinition npc = NpcDefinition.forId(id);
        require(npc.getAttackAnimationId() == attack, "NPC " + id + " attack");
        require(npc.getBlockAnimationId() == block, "NPC " + id + " block");
        require(npc.getDeathAnimationId() == death, "NPC " + id + " death");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
