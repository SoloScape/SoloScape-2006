package com.rs2.model.interaction;

import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.skill.mining.MiningManager;
import com.rs2.model.skill.woodcutting.TreeDefinition;

/** Only move an object action when its existing gameplay owner is known. */
public final class ObjectActionRouter {
    private ObjectActionRouter() { }

    public static InteractionType semanticRoute(ObjectDefinition definition, int slot) {
        if (definition == null) return null;
        String action = definition.getAction(slot);
        if (action == null) return null;
        // Castle Wars main gates may put Open/Close and Attack into different
        // 443 cache slots. Always route by action label, not menu position.
        InteractionType gateRoute = castleWarsMainDoorRoute(definition.getObjectId(), action);
        if (gateRoute != null) return gateRoute;
        // Barricade Attack may occupy a different cache option slot. Route
        // every Attack label through the same continuous melee interaction.
        if ((definition.getObjectId() == 4421 || definition.getObjectId() == 4422)
                && "Attack".equalsIgnoreCase(action)) return InteractionType.FIRST_OBJECT;
        if ("bank booth".equalsIgnoreCase(definition.getName())) {
            if ("Use".equalsIgnoreCase(action)) return InteractionType.FIRST_OBJECT;
            if ("Use-quickly".equalsIgnoreCase(action)) return InteractionType.SECOND_OBJECT;
            if ("Collect".equalsIgnoreCase(action)) return InteractionType.THIRD_OBJECT;
        }
        if (("Chop down".equalsIgnoreCase(action) || "Chop-down".equalsIgnoreCase(action))
                && TreeDefinition.forObjectId(definition.getObjectId()) != null) return InteractionType.FIRST_OBJECT;
        if ("Mine".equalsIgnoreCase(action) && MiningManager.isMineableRockObjectId(definition.getObjectId())) {
            return InteractionType.FIRST_OBJECT;
        }
        return null;
    }

    /** Revision-443 Castle Wars gate options are semantic, not ordinal. */
    public static InteractionType castleWarsMainDoorRoute(int objectId, String action) {
        if (!CastleWarsEngineeringManager.isMainDoorObject(objectId) || action == null) {
            return null;
        }
        if ("Attack".equalsIgnoreCase(action)) {
            return InteractionType.SECOND_OBJECT;
        }
        if ("Open".equalsIgnoreCase(action) || "Close".equalsIgnoreCase(action)
                || "Repair".equalsIgnoreCase(action)) {
            return InteractionType.FIRST_OBJECT;
        }
        return null;
    }

    public static InteractionType legacyRoute(int option) {
        return option == 1 ? InteractionType.FIRST_OBJECT : option == 2 ? InteractionType.SECOND_OBJECT
                : option == 3 ? InteractionType.THIRD_OBJECT : option == 4 ? InteractionType.FOURTH_OBJECT : null;
    }
}
