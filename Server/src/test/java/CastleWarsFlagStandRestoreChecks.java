import com.rs2.model.Position;
import com.rs2.model.objects.ObjectManager;

/** A returned Castle Wars flag must be restored in clients that saw the stand empty. */
public final class CastleWarsFlagStandRestoreChecks {
    public static void main(String[] args) {
        Position stand = new Position(2429, 3074, 3);

        // The old 60-tile distance limit skipped this player even though
        // the client had already received the "empty stand" object.
        require(ObjectManager.shouldSendDynamicObjectRestore(
                new Position(2369, 3134, 3), stand, true),
                "Previously seen stand must be restored beyond 60 tiles");

        require(ObjectManager.shouldSendDynamicObjectRestore(
                new Position(2428, 3074, 3), stand, false),
                "Nearby players must still receive dynamic-object restoration");

        require(!ObjectManager.shouldSendDynamicObjectRestore(
                new Position(2369, 3134, 3), stand, false),
                "Do not broadcast an unrelated distant object to a new viewer");

        require(!ObjectManager.shouldSendDynamicObjectRestore(
                new Position(2429, 3074, 0), stand, true),
                "Do not apply a third-floor stand update to the ground floor");

        System.out.println("Castle Wars flag-stand restoration visibility checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
