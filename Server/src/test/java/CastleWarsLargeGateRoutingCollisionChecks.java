import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.gameplay.castlewars.CastleWarsManager;
import com.rs2.model.interaction.InteractionType;
import com.rs2.model.interaction.ObjectActionRouter;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.util.path.WalkingCollisionMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/** Main-gate clicks and walk-through collision must match the visible gate. */
public final class CastleWarsLargeGateRoutingCollisionChecks {
    public static void main(String[] args) throws Exception {
        verifyOptions();
        verifyCollision();
        System.out.println("Castle Wars large-gate routing and collision checks passed");
    }

    private static void verifyOptions() throws Exception {
        // Use real cached action-slot semantics with a synthetic definition.
        // In revision 443 an action can be in option 2/3 instead of option 1.
        for (int objectId = 4423; objectId <= 4434; objectId++) {
            ObjectDefinition definition = makeObject(objectId);
            setAction(definition, 2, "Open");
            require(ObjectActionRouter.semanticRoute(definition, 2)
                    == InteractionType.FIRST_OBJECT,
                    "Open must reach main-door handler (id " + objectId + ")");
            setAction(definition, 2, "Close");
            require(ObjectActionRouter.semanticRoute(definition, 2)
                    == InteractionType.FIRST_OBJECT,
                    "Close must reach main-door handler (id " + objectId + ")");
            setAction(definition, 2, "Attack");
            require(ObjectActionRouter.semanticRoute(definition, 2)
                    == InteractionType.SECOND_OBJECT,
                    "Attack must reach main-door combat (id " + objectId + ")");
        }
        ObjectDefinition unrelated = makeObject(5000);
        setAction(unrelated, 2, "Open");
        require(ObjectActionRouter.semanticRoute(unrelated, 2) == null,
                "Do not change unrelated object actions");
    }

    private static ObjectDefinition makeObject(int objectId) throws Exception {
        Constructor<ObjectDefinition> c = ObjectDefinition.class.getDeclaredConstructor(
                int.class, String.class, String.class, int.class, int.class,
                boolean.class, boolean.class, boolean.class, boolean.class, int.class);
        c.setAccessible(true);
        return c.newInstance(objectId, "gate", "", 1, 1,
                true, true, false, false, 0);
    }

    private static void setAction(ObjectDefinition definition, int index, String action)
            throws Exception {
        Field field = ObjectDefinition.class.getDeclaredField("actions");
        field.setAccessible(true);
        ((String[]) field.get(definition))[index] = action;
    }

    private static void verifyCollision() throws Exception {
        Object saradomin = getDoor("saradominMainDoor");
        Object zamorak = getDoor("zamorakMainDoor");
        Field sMode = modeField(saradomin);
        Field zMode = modeField(zamorak);
        Object sOld = sMode.get(saradomin);
        Object zOld = zMode.get(zamorak);
        try {
            setMode(saradomin, sMode, "CLOSED");
            setMode(zamorak, zMode, "CLOSED");
            require(CastleWarsEngineeringManager.isMainDoorClosed(
                    CastleWarsManager.Team.SARADOMIN), "Saradomin gate state");
            require(CastleWarsEngineeringManager.isMainDoorClosed(
                    CastleWarsManager.Team.ZAMORAK), "Zamorak gate state");

            int[][] leaves = {{2426, 3088}, {2427, 3088},
                    {2372, 3119}, {2373, 3119}};
            for (int[] tile : leaves) {
                require(CastleWarsEngineeringManager.isClosedMainDoorTile(
                        tile[0], tile[1], 0), "Closed gate must occupy tile");
                require((WalkingCollisionMap.getTileFlags(tile[0], tile[1], 0)
                        & 0x100) != 0, "Closed gate must block bots and players");
                require(!CastleWarsEngineeringManager.isClosedMainDoorTile(
                        tile[0], tile[1], 1), "Gate must not block floor 1");
            }
            require(!CastleWarsEngineeringManager.isClosedMainDoorTile(2426, 3089, 0),
                    "Open battlefield approach tile must remain walkable");
            require(!CastleWarsEngineeringManager.isClosedMainDoorTile(2427, 3087, 0),
                    "Open castle approach tile must remain walkable");

            setMode(saradomin, sMode, "OPEN");
            setMode(zamorak, zMode, "BROKEN");
            for (int[] tile : leaves) {
                require(!CastleWarsEngineeringManager.isClosedMainDoorTile(
                        tile[0], tile[1], 0),
                        "Open or broken gate must release the passage");
            }
            require(!CastleWarsEngineeringManager.isMainDoorClosed(
                    CastleWarsManager.Team.SARADOMIN), "Open gate state");
            require(!CastleWarsEngineeringManager.isMainDoorClosed(
                    CastleWarsManager.Team.ZAMORAK), "Broken gate state");
        } finally {
            sMode.set(saradomin, sOld);
            zMode.set(zamorak, zOld);
        }
    }

    private static Object getDoor(String name) throws Exception {
        Field f = CastleWarsEngineeringManager.class.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(null);
    }

    private static Field modeField(Object door) throws Exception {
        Field f = door.getClass().getDeclaredField("mode");
        f.setAccessible(true);
        return f;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setMode(Object door, Field field, String name)
            throws Exception {
        field.set(door, Enum.valueOf((Class) field.getType(), name));
    }

    private static void require(boolean valid, String message) {
        if (!valid) throw new AssertionError(message);
    }
}
