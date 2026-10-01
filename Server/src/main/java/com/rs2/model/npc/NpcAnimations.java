package com.rs2.model.npc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** Verified combat sets for the model rigs in the bundled 443 cache. */
final class NpcAnimations {
    private static final Map<Integer, int[]> SETS = new HashMap<Integer, int[]>();

    private NpcAnimations() { }

    static void load() throws IOException {
        Map<Integer, int[]> sets = new HashMap<Integer, int[]>();
        // Each record contains four big-endian signed 32-bit IDs: idle, attack, block, death.
        ByteBuffer data = ByteBuffer.wrap(Files.readAllBytes(Paths.get("data/npcs/animations.dat")));
        if (data.remaining() == 0 || data.remaining() % 16 != 0) {
            throw new IOException("Invalid animations.dat length: expected complete 16-byte records");
        }
        while (data.hasRemaining()) {
            int record = data.position() / 16 + 1;
            int idle = data.getInt();
            int[] set = {data.getInt(), data.getInt(), data.getInt()};
            if (idle < 0 || set[0] < -1 || set[1] < -1 || set[2] < -1
                    || sets.put(idle, set) != null) {
                throw new IOException("Invalid or duplicate animations.dat record " + record);
            }
        }
        SETS.clear();
        SETS.putAll(sets);
    }

    static int resolve(int idle, int animation, int role) {
        // Zero in these legacy definitions means no combat animation.
        if ((idle == -1 || idle == 808 || idle == 813) && animation == 0) return -1;
        // These cache models use the human skeleton; the old bone rig is incompatible.
        if (idle == 808) {
            if (role == 0 && animation == 260) return 412;
            if (role == 1 && animation == 261) return 403;
            if (role == 2 && animation == 263) return 836;
        }
        int[] set = SETS.get(idle);
        if (set == null) return animation;
        // Repair human defaults on non-human variants sharing a verified rig.
        if (animation == 0 || animation == (role == 0 ? 422 : role == 1 ? 404 : 2304)) {
            return set[role];
        }
        if ((role == 0 && (animation == 412 || animation == 451))
                || (role == 2 && animation == 836)) return set[role];
        int legacy = -1;
        switch (idle) {
            case 4932: legacy = role == 0 ? 138 : role == 1 ? 139 : 141; break;
            case 4914: legacy = role == 0 ? 30 : role == 1 ? 31 : 36; break;
            case 4919: case 4920:
                legacy = role == 0 ? 41 : role == 1 ? 42 : 44; break;
            case 4650: case 4656: case 4662: case 4663: case 4670:
                legacy = role == 0 ? 128 : role == 1 ? 129 : 131; break;
            case 4866: legacy = role == 0 ? 1035 : role == 1 ? 1036 : 1037; break;
            case 5318: case 5326:
                legacy = role == 0 ? 143 : role == 1 ? 144 : 146;
                if (role == 1 && animation == 147) return set[role];
                break;
            case 279:
                if (role == 1 && animation == 279) return set[role];
                if (role == 2 && animation == 273) return set[role];
                break;
            case 4639: legacy = role == 0 ? 80 : role == 1 ? 89 : 92; break;
            case 1282: if (role == 2 && animation == 1285) return set[role]; break;
            case 2603: if (role == 2 && animation == 2307) return set[role]; break;
            case 3906: if (role == 2 && animation == 3903) return set[role]; break;
            default: break;
        }
        return legacy != -1 && animation == legacy ? set[role] : animation;
    }

    /** Also translate legacy IDs embedded in specialized combat providers. */
    static int resolveAttack(int configuredAttack, int suppliedAttack) {
        switch (configuredAttack) {
            case 4636:
                if (suppliedAttack == 80) return 4636;
                if (suppliedAttack == 81) return 4637;
                return suppliedAttack;
            case 4933: return suppliedAttack == 138 ? configuredAttack : suppliedAttack;
            case 4915: return suppliedAttack == 30 ? configuredAttack : suppliedAttack;
            case 4925: return suppliedAttack == 41 ? configuredAttack : suppliedAttack;
            case 4652: case 4666: case 4672:
                return suppliedAttack == 128 ? configuredAttack : suppliedAttack;
            case 4868: return suppliedAttack == 1035 ? configuredAttack : suppliedAttack;
            case 5319: case 5327:
                return suppliedAttack == 143 ? configuredAttack : suppliedAttack;
            default: return suppliedAttack;
        }
    }
}
