package com.rs2.model.interaction;

import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.player.Player;

/** Toll-controlled passage through the Lumbridge/Al-Kharid border gates. */
public final class AlKharidGateToll {
    private static final int PRINCE_ALI_RESCUE_QUEST = 11;
    private static final int COINS = 995;
    private static final int TOLL = 10;
    private static final int GATE_X = 3268;

    private AlKharidGateToll() {
    }

    public static boolean isTollGate(int objectId, int x, int y, int plane) {
        if (plane != 0 || x != GATE_X || (y != 3227 && y != 3228)) {
            return false;
        }
        // Both gate leaves, including alternate cache/packet leaf mappings.
        if (objectId == 2882 || objectId == 2883) {
            return true;
        }
        ObjectDefinition definition = ObjectDefinition.forId(objectId);
        if (definition == null || !"Gate".equalsIgnoreCase(definition.getName())) {
            return false;
        }
        for (int slot = 0; slot < 5; slot++) {
            String action = definition.getAction(slot);
            if (action != null && action.toLowerCase(java.util.Locale.ROOT).startsWith("pay-toll")) {
                return true;
            }
        }
        return false;
    }

    /** Only the border guards at the Al Kharid/Lumbridge toll gate. */
    public static boolean isBorderGuard(Npc npc) {
        return npc != null && npc.getDefinition() != null
                && "Border Guard".equalsIgnoreCase(npc.getDefinition().getName())
                && npc.getPosition().getPlane() == 0
                && Math.abs(npc.getPosition().getX() - GATE_X) <= 8
                && Math.abs(npc.getPosition().getY() - 3227) <= 8;
    }

    public static boolean hasFreePassage(Player player) {
        // Stage 9 is still an unfinished quest; only completion waives the toll.
        return player.getQuestState(PRINCE_ALI_RESCUE_QUEST) == 1;
    }

    /**
     * Used by both the gate's Pay-toll object action and its guard dialogue.
     * Charges once per crossing, from either side of the gate.
     */
    public static boolean tryPass(Player player) {
        if (player.isStunned() || player.isMovementLocked()) {
            return false;
        }
        if (!hasFreePassage(player)) {
            if (!player.getInventoryManager().containsItemAmount(COINS, TOLL)
                    || !player.getInventoryManager().removeItem(new ItemStack(COINS, TOLL))) {
                player.packetSender.sendGameMessage("You need 10 gold coins to pass through the gate.");
                return false;
            }
        }

        player.getPacketSender().queueRelativeMovementStep(
                player.getPosition().getX() < GATE_X ? 1 : -1, 0, true);
        player.getPacketSender().openDoubleDoorPair(2882, 2883, GATE_X, 3227, GATE_X, 3228, 0);
        return true;
    }
}
