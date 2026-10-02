package com.rs2.model.interaction;

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

    public static InteractionType legacyRoute(int option) {
        return option == 1 ? InteractionType.FIRST_OBJECT : option == 2 ? InteractionType.SECOND_OBJECT
                : option == 3 ? InteractionType.THIRD_OBJECT : option == 4 ? InteractionType.FOURTH_OBJECT : null;
    }
}
