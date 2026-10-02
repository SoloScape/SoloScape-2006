package com.rs2.model.skill.woodcutting;

import com.rs2.ServerSettings;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.player.Player;
import com.rs2.model.skill.ItemCombinationHandler;
import com.rs2.model.task.CycleEvent;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.util.GameUtil;

/** Animal Magnetism's NPC trees are not the axe-breaking tree ent random event. */
public final class UndeadTreeCutting extends CycleEvent {
    public static final int BLESSED_AXE = 10491;
    public static final int UNDEAD_TWIGS = 10490;
    private final Player player;
    private final Npc tree;
    private final int sequence;

    private UndeadTreeCutting(Player player, Npc tree, int sequence) {
        this.player = player;
        this.tree = tree;
        this.sequence = sequence;
    }

    public static boolean isUndeadTree(int npcId) {
        return npcId == 5207 || npcId == 5208;
    }

    private static boolean hasBlessedAxe(Player player) {
        return player.getInventoryManager().containsItem(BLESSED_AXE)
                || player.getEquipmentManager().getItemIdAtSlot(3) == BLESSED_AXE;
    }

    private static boolean canCut(Player player) {
        String message = null;
        if (!ServerSettings.woodcuttingEnabled) {
            message = "This skill is currently disabled.";
        } else if (!player.isMember() || ServerSettings.freeToPlayWorld) {
            message = "You need to be on a members world with a members account to cut this tree.";
        } else if (!hasBlessedAxe(player)) {
            message = ItemCombinationHandler.findUsableGatheringTool(player, 8) == null
                    ? "You don't have an axe which could possibly affect this wood."
                    : "The axe bounces off the undead wood.";
        } else if (player.getSkillManager().getCurrentLevels()[8] < 35) {
            message = "You need a Woodcutting level of 35 or more in order to cut this tree.";
        } else if (player.getSkillManager().getCurrentLevels()[18] < 18) {
            message = "You need a Slayer level of 18 or more in order to properly use the axe on these trees.";
        } else if (player.getInventoryManager().getContainer().getFreeSlots() == 0) {
            message = "Not enough space in your inventory.";
        }
        if (message != null) player.packetSender.sendGameMessage(message);
        return message == null;
    }

    public static void start(Player player, Npc tree) {
        if (!isUndeadTree(tree.getNpcId()) || tree.isDead()
                || !player.isWithinReach(tree, 1) || player.isOverlapping(tree) || !canCut(player)) return;
        int sequence = player.nextActionSequence();
        UndeadTreeCutting task = new UndeadTreeCutting(player, tree, sequence);
        player.setActiveCycleEvent(task);
        player.packetSender.sendGameMessage("You swing your blessed axe at the undead tree.");
        task.swing();
        CycleEventHandler.getInstance().schedule(player, task, 4);
    }

    private void swing() {
        player.getUpdateState().setFaceEntity(tree.getEncodedIndex());
        player.getUpdateState().setAnimation(5383, 0);
        player.packetSender.sendSoundEffect(472, 1, 0);
    }

    @Override
    public void execute(CycleEventContainer cycle) {
        if (!player.isCurrentActionSequence(sequence) || tree.isDead() || !isUndeadTree(tree.getNpcId())
                || !player.isWithinReach(tree, 1) || player.isOverlapping(tree) || !canCut(player)) {
            cycle.stop();
            return;
        }
        // Approximate attempts using the existing level-35 tree curve and mithril axe speed.
        // The exact historical undead-tree success rate is not documented.
        if (GameUtil.rollLevelScaledChance(TreeDefinition.TEAK.getCutChanceLow(),
                TreeDefinition.TEAK.getCutChanceHigh(), player.getSkillManager().getCurrentLevels()[8], 2.5)) {
            player.getInventoryManager().addItem(new ItemStack(UNDEAD_TWIGS));
            player.getSkillManager().addExperience(8, 5.0);
            player.packetSender.sendGameMessage("You cut some undead twigs from the tree.");
            if (player.getInventoryManager().getContainer().getFreeSlots() == 0) {
                cycle.stop();
                return;
            }
        }
        swing();
    }

    @Override
    public void onStop() {
        player.getUpdateState().setAnimation(-1, 0);
    }
}
