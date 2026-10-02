package com.rs2.model.interaction;

import com.rs2.model.item.ItemDefinition;

/** Inventory gameplay slots used by the legacy item handlers. */
public final class ItemActionRouter {
    private ItemActionRouter() { }

    public static int semanticOption(ItemDefinition definition, int option) {
        String action = definition.getInventoryAction(option - 1);
        if (action == null) return option;
        if ("Wear".equalsIgnoreCase(action) || "Wield".equalsIgnoreCase(action)) return 2;
        if ("Eat".equalsIgnoreCase(action) || "Drink".equalsIgnoreCase(action)
                || "Bury".equalsIgnoreCase(action)) return 1;
        if ("Drop".equalsIgnoreCase(action) || "Destroy".equalsIgnoreCase(action)) return 5;
        return option;
    }

    public static boolean isSemantic(ItemDefinition definition, int slot) {
        String action = definition.getInventoryAction(slot);
        return action != null && (action.equalsIgnoreCase("Wear") || action.equalsIgnoreCase("Wield")
                || action.equalsIgnoreCase("Eat") || action.equalsIgnoreCase("Drink")
                || action.equalsIgnoreCase("Bury") || action.equalsIgnoreCase("Drop") || action.equalsIgnoreCase("Destroy"));
    }
}
