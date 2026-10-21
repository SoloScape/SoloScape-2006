package com.rs2.model.skill.fletching.logs;

import com.rs2.ServerSettings;
import com.rs2.model.player.Player;

/** Revision 443 log cutting through the supported native option chatboxes. */
public final class LogFletchingMenu {
    private static final String[] ACTIONS = {
        "normalCutting", "oakCutting", "acheyCutting", "willowCutting",
        "mapleCutting", "yewCutting", "magicCutting"
    };
    private static final String[][] PRODUCTS = {
        {"Arrow shafts", "Shortbow", "Longbow"},
        {"Oak shortbow", "Oak longbow"}, {"Unstrung comp bow"},
        {"Willow shortbow", "Willow longbow"}, {"Maple shortbow", "Maple longbow"},
        {"Yew shortbow", "Yew longbow"}, {"Magic shortbow", "Magic longbow"}
    };

    private LogFletchingMenu() {
    }

    private static int actionIndex(String action) {
        for (int i = 0; i < ACTIONS.length; i++) {
            if (ACTIONS[i].equals(action)) return i;
        }
        return -1;
    }

    public static boolean open(Player player, String action) {
        int index = actionIndex(action);
        if (ServerSettings.clientBuild != 443 || index == -1) return false;
        player.getDialogueManager().finishDialogue();
        player.setSelectedInterfaceId(-1);
        String[] products = PRODUCTS[index];
        if (products.length == 3) {
            player.getDialogueManager().showThreeOptionsWithTitle("What would you like to make?",
                    products[0], products[1], products[2]);
        } else if (products.length == 2) {
            player.getDialogueManager().showTwoOptionsWithTitle("What would you like to make?",
                    products[0], products[1]);
        } else {
            player.setSelectedInterfaceId(0);
            showQuantities(player);
        }
        // Opening a chatbox resets interface action state.
        player.setInterfaceAction(ACTIONS[index]);
        return true;
    }

    private static void showQuantities(Player player) {
        player.getDialogueManager().showFourOptionsWithTitle("How many would you like to make?",
                "Make 1", "Make 5", "Make 10", "Make all");
    }

    public static boolean handleButton(Player player, int button) {
        int index = actionIndex(player.interfaceAction);
        if (ServerSettings.clientBuild != 443 || index == -1) return false;
        int root = player.getOpenInterfaceId();
        int productRoot = PRODUCTS[index].length == 3 ? 2469 : 2459;
        int firstButton = productRoot == 2469 ? 2471 : 2461;
        if (PRODUCTS[index].length > 1 && root == productRoot
                && button >= firstButton && button < firstButton + PRODUCTS[index].length) {
            if (player.isActionLocked()) return true;
            int product = button - firstButton;
            showQuantities(player);
            player.setSelectedInterfaceId(product);
            player.setInterfaceAction(ACTIONS[index]);
            return true;
        }
        if (root != 2480 || button < 2482 || button > 2485) return false;
        if (player.isActionLocked()) return true;
        int product = player.getSelectedInterfaceId();
        if (product < 0 || product >= PRODUCTS[index].length) return true;
        int recipeButton = index == 0 ? 8889 + product * 4 : index == 2 ? 2799 : 8874 + product * 4;
        LogFletchingAction action;
        switch (index) {
            case 0: action = NormalLogFletchingAction.create(player, recipeButton, 0); break;
            case 1: action = OakLogFletchingAction.create(player, recipeButton, 0); break;
            case 2: action = AcheyLogFletchingAction.create(player, recipeButton, 0); break;
            case 3: action = WillowLogFletchingAction.create(player, recipeButton, 0); break;
            case 4: action = MapleLogFletchingAction.create(player, recipeButton, 0); break;
            case 5: action = YewLogFletchingAction.create(player, recipeButton, 0); break;
            case 6: action = MagicLogFletchingAction.create(player, recipeButton, 0); break;
            default: return false;
        }
        if (action != null) {
            // Logs are unstackable; all stops naturally when inventory logs run out.
            action.menuQuantity = new int[]{1, 5, 10, 28}[button - 2482];
            player.setSelectedInterfaceId(-1);
            action.start();
        }
        return true;
    }
}
