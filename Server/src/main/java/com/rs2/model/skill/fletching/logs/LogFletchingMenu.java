package com.rs2.model.skill.fletching.logs;

import com.rs2.ServerSettings;
import com.rs2.model.player.Player;

/**
 * Revision 443 log cutting uses the native five-product make-item interface
 * (group 459). The upper three slots show real item models with their names,
 * and each slot already has native Make X/10/5/1 menu actions.
 */
public final class LogFletchingMenu {
    private static final int ROOT = 8880; // native group 459
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
    private static final int[][] PRODUCT_IDS = {
        {52, 50, 48}, {54, 56}, {4825}, {60, 58}, {64, 62}, {68, 66}, {72, 70}
    };

    // The left, middle and right product positions of native group 459.
    // All other children, including the original four-option context menus,
    // remain cache-backed. Synthetic legacy IDs are bridged in InterfaceBridge.
    private static final int[] SLOT_MODELS = {20100, 20101, 20102}; // 459:101,89,108
    private static final int[] SLOT_LABELS = {20103, 20104, 20105}; // 459:102,90,109
    private static final int[] SLOT_ROOTS = {20106, 20107, 20108}; // 459:96,91,103
    private static final int[] EXTRA_ROOTS = {20109, 20110}; // bottom two native choices
    private static final int TITLE = 20111; // 459:88

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
        player.setInterfaceAction(ACTIONS[index]);

        player.packetSender.sendInterfaceText("What would you like to make?", TITLE);
        for (int slot = 0; slot < SLOT_MODELS.length; slot++) {
            boolean shown = slot < PRODUCT_IDS[index].length;
            player.packetSender.setInterfaceHiddenFlag(shown ? 0 : 1, SLOT_ROOTS[slot]);
            player.packetSender.setInterfaceHiddenFlag(shown ? 0 : 1, SLOT_MODELS[slot]);
            player.packetSender.setInterfaceHiddenFlag(shown ? 0 : 1, SLOT_LABELS[slot]);
            if (shown) {
                player.packetSender.sendInterfaceModel(SLOT_MODELS[slot], 180, PRODUCT_IDS[index][slot]);
                player.packetSender.sendInterfaceText(PRODUCTS[index][slot], SLOT_LABELS[slot]);
            }
        }
        for (int root : EXTRA_ROOTS) {
            player.packetSender.setInterfaceHiddenFlag(1, root);
        }
        player.packetSender.showInterface(ROOT);
        return true;
    }

    /**
     * Group 459 is also the spinning-wheel interface. Reset its fields before
     * returning to spinning so fletching does not change the other skill's UI.
     */
    public static void restoreSpinningInterface(Player player) {
        if (ServerSettings.clientBuild != 443) return;
        player.setSelectedInterfaceId(-1);
        player.setInterfaceAction("spinning");
        for (int root : SLOT_ROOTS) player.packetSender.setInterfaceHiddenFlag(0, root);
        for (int root : EXTRA_ROOTS) player.packetSender.setInterfaceHiddenFlag(0, root);
        for (int model : SLOT_MODELS) player.packetSender.setInterfaceHiddenFlag(0, model);
        for (int label : SLOT_LABELS) player.packetSender.setInterfaceHiddenFlag(0, label);
        player.packetSender.sendInterfaceText("What would you like to spin?", TITLE);
        player.packetSender.sendInterfaceModel(SLOT_MODELS[0], 180, 1737);
        player.packetSender.sendInterfaceModel(SLOT_MODELS[1], 180, 1779);
        player.packetSender.sendInterfaceModel(SLOT_MODELS[2], 180, 6051);
        player.packetSender.sendInterfaceText("Wool", SLOT_LABELS[0]);
        player.packetSender.sendInterfaceText("Flax", SLOT_LABELS[1]);
        player.packetSender.sendInterfaceText("Magic roots", SLOT_LABELS[2]);
        player.packetSender.showInterface(ROOT);
    }

    /**
     * Native child actions map to 8886-8889 (left), 8890-8893 (middle),
     * 8894-8897 (right); within each the order is X, 10, 5, 1.
     */
    public static boolean handleButton(Player player, int button) {
        int index = actionIndex(player.interfaceAction);
        if (ServerSettings.clientBuild != 443 || index == -1
                || player.getOpenInterfaceId() != ROOT || button < 8886 || button > 8897) {
            return false;
        }
        int slot = (button - 8886) / 4;
        if (slot >= PRODUCTS[index].length || player.isActionLocked()) return true;
        int kind = (button - 8886) % 4;
        if (kind == 0) {
            player.packetSender.sendEnterInputPrompt(button);
        } else {
            make(player, index, slot, new int[]{0, 10, 5, 1}[kind]);
        }
        return true;
    }

    /** The entered quantity belongs to the pending Make-X item, never another slot. */
    public static boolean handleAmount(Player player, int button, int amount) {
        int index = actionIndex(player.interfaceAction);
        if (ServerSettings.clientBuild != 443 || index == -1
                || button < 8886 || button > 8897 || (button - 8886) % 4 != 0) {
            return false;
        }
        player.setSelectedInterfaceId(-1);
        int slot = (button - 8886) / 4;
        if (player.getOpenInterfaceId() != ROOT || slot >= PRODUCTS[index].length
                || player.isActionLocked() || amount <= 0) {
            return true;
        }
        make(player, index, slot, amount);
        return true;
    }

    private static void make(Player player, int index, int slot, int count) {
        int recipeButton = index == 0 ? 8889 + slot * 4 : index == 2 ? 2799 : 8874 + slot * 4;
        LogFletchingAction action;
        switch (index) {
            case 0: action = NormalLogFletchingAction.create(player, recipeButton, 0); break;
            case 1: action = OakLogFletchingAction.create(player, recipeButton, 0); break;
            case 2: action = AcheyLogFletchingAction.create(player, recipeButton, 0); break;
            case 3: action = WillowLogFletchingAction.create(player, recipeButton, 0); break;
            case 4: action = MapleLogFletchingAction.create(player, recipeButton, 0); break;
            case 5: action = YewLogFletchingAction.create(player, recipeButton, 0); break;
            case 6: action = MagicLogFletchingAction.create(player, recipeButton, 0); break;
            default: return;
        }
        if (action != null) {
            action.menuQuantity = count;
            action.requestedQuantity = count;
            player.setSelectedInterfaceId(-1);
            action.start();
        }
    }
}
