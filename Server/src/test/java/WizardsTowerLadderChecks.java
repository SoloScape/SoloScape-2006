import com.rs2.model.Position;
import com.rs2.model.objects.LoadedWorldObject;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.util.path.WalkingCollisionMap;

/** Cache-backed diagnostic for issue #4: Wizards' Tower basement ladder. */
public final class WizardsTowerLadderChecks {
    public static void main(String[] args) throws Exception {
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        int found = 0;
        for (int x = 3100; x <= 3108; x++) {
            for (int y = 9571; y <= 9581; y++) {
                LoadedWorldObject object = WorldObjectLookup.findObjectAt(x, y, 0);
                if (object == null) continue;
                int id = object.getWorldObject().getObjectId();
                ObjectDefinition definition = ObjectDefinition.forId(id);
                System.out.println("Basement object " + id + " at " + x + "," + y
                        + " type=" + object.getType()
                        + " name=" + (definition == null ? "null" : definition.getName())
                        + " option0=" + (definition == null ? "null" : definition.getAction(0))
                        + " option1=" + (definition == null ? "null" : definition.getAction(1)));
                if (id == 2148) found++;
            }
        }
        require(found > 0, "Basement climb-up ladder 2148 missing from the cached tower map");
        require(WorldObjectLookup.findObjectByIdAt(2147, 3104, 3162, 0) != null,
                "Ground-floor climb-down ladder 2147 missing");
        System.out.println("Wizards' Tower ladder map checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
