package com.rs2.model.skill.woodcutting;

import com.rs2.ServerSettings;
import com.rs2.model.GameplayHelper;
import com.rs2.model.Position;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.player.Player;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.ItemCombinationHandler;
import com.rs2.model.skill.SkillActionHelper;
import com.rs2.model.task.CycleEvent;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;

/** Tai Bwo Wannai Cleanup jungle cutting as it existed in the 443-era cache. */
public final class JungleCutting extends CycleEvent {
    private static final long CLEANUP_STATE_MASK = 0xffL;
    private static final long CLEANUP_STARTED_BIT = 1L;
    private static final int JUNGLE_POTION_QUEST_ID = 56;
    private static final int RESOURCE_RESPAWN_TICKS = 146;
    private static final int[] MACHETES = {975, 6313, 6315, 6317};
    private static final int[] MACHETE_ANIMATIONS = {2382, 6085, 6086, 6087};
    private static final double[] MACHETE_SPEED = {1.0, 1.08, 1.16, 1.25};

    private final Player player;
    private final int actionSequence;
    private final JungleDefinition definition;
    private final int x;
    private final int y;
    private final int macheteId;
    private final int macheteAnimation;
    private final int cycleTicks;
    private int currentObjectId;

    private JungleCutting(Player player, int actionSequence, JungleDefinition definition,
                          int x, int y, int objectId, int macheteId) {
        this.player = player;
        this.actionSequence = actionSequence;
        this.definition = definition;
        this.x = x;
        this.y = y;
        this.currentObjectId = objectId;
        this.macheteId = macheteId;
        int tier = macheteTier(macheteId);
        this.macheteAnimation = MACHETE_ANIMATIONS[tier];
        this.cycleTicks = cycleTicks(player.getSkillManager().getCurrentLevels()[8]);
    }

    public static boolean isJungleObject(int objectId) {
        return JungleDefinition.forObjectId(objectId) != null;
    }

    public static boolean isSpecialResource(int objectId) {
        return objectId >= 9030 && objectId <= 9033;
    }

    public static int getFavour(Player player) {
        return Math.max(0, Math.min(100, (int)((player.reservedSaveLong1 >>> 1) & 0x7fL)));
    }

    public static boolean hasStarted(Player player) {
        return (player.reservedSaveLong1 & CLEANUP_STARTED_BIT) != 0L;
    }

    private static void setStarted(Player player) {
        player.reservedSaveLong1 |= CLEANUP_STARTED_BIT;
    }

    public static boolean handleMurcaily(Player player, int npcId) {
        if (npcId != 2529 && npcId != 2530) return false;
        if (!player.isMember() || ServerSettings.freeToPlayWorld) {
            player.packetSender.sendGameMessage("You need to be on a members world with a members account to do this.");
            return true;
        }
        if (player.getQuestState(JUNGLE_POTION_QUEST_ID) != 1) {
            player.packetSender.sendGameMessage("Murcaily tells you to help Trufitus with his Jungle Potion first.");
            return true;
        }
        setStarted(player);
        player.packetSender.sendGameMessage("Murcaily explains how clearing the jungle helps Tai Bwo Wannai.");
        player.packetSender.sendGameMessage("You can now earn favour by hacking jungle with a machete.");
        return true;
    }

    private static void addFavour(Player player, int amount) {
        int favour = Math.min(100, getFavour(player) + amount);
        long cleanupState = (hasStarted(player) ? CLEANUP_STARTED_BIT : 0L) | ((long)favour << 1);
        player.reservedSaveLong1 = (player.reservedSaveLong1 & ~CLEANUP_STATE_MASK) | cleanupState;
        player.packetSender.sendGameMessage("Tai Bwo Wannai favour: " + favour + "%.");
    }

    public static void start(Player player, int objectId, int x, int y) {
        JungleDefinition definition = JungleDefinition.forObjectId(objectId);
        if (definition == null || definition.isDepleted(objectId)) return;
        if (!ServerSettings.woodcuttingEnabled) {
            player.packetSender.sendGameMessage("This skill is currently disabled.");
            return;
        }
        if (!player.isMember() || ServerSettings.freeToPlayWorld) {
            player.packetSender.sendGameMessage("You need to be on a members world with a members account to do this.");
            return;
        }
        if (player.getQuestState(JUNGLE_POTION_QUEST_ID) != 1) {
            player.packetSender.sendGameMessage("You need to complete Jungle Potion before helping with the cleanup.");
            return;
        }
        if (!hasStarted(player)) {
            player.packetSender.sendGameMessage("You should speak to Murcaily before starting the cleanup.");
            return;
        }
        if (player.getSkillManager().getCurrentLevels()[8] < definition.getRequiredLevel()) {
            player.packetSender.sendGameMessage("You need a Woodcutting level of "
                    + definition.getRequiredLevel() + " to hack this jungle.");
            return;
        }
        int macheteId = player.getEquipmentManager().getItemIdAtSlot(3);
        if (macheteTier(macheteId) < 0) {
            player.packetSender.sendGameMessage("You need to wield a machete to hack the jungle.");
            return;
        }
        if (player.getInventoryManager().getContainer().getFirstFreeSlot() == -1) {
            player.packetSender.sendGameMessage("Not enough space in your inventory.");
            return;
        }
        DynamicObject dynamic = ObjectManager.findDynamicObjectAt(x, y, player.getPosition().getPlane());
        if (dynamic != null && dynamic.getWorldObject().getObjectId() != objectId) return;

        int sequence = player.nextActionSequence();
        JungleCutting task = new JungleCutting(player, sequence, definition, x, y, objectId, macheteId);
        player.setActiveCycleEvent(task);
        player.packetSender.sendGameMessage("You hack away at the jungle.");
        task.swing();
        CycleEventHandler.getInstance().schedule(player, task, task.cycleTicks);
    }

    private void swing() {
        WoodcuttingHandler.faceTree(player, currentObjectId, x, y);
        player.getUpdateState().setAnimation(macheteAnimation, 0);
    }

    @Override
    public void execute(CycleEventContainer cycle) {
        if (!canContinue()) {
            cycle.stop();
            return;
        }
        if (trySpawnEvent()) {
            cycle.stop();
            return;
        }
        player.jungleEventChanceCounter = Math.min(7, player.jungleEventChanceCounter + 1);

        if (rollSuccess()) {
            player.getInventoryManager().addItem(new ItemStack(definition.getSparItemId(), 1));
            player.getSkillManager().addExperience(8, definition.getExperience());
            player.packetSender.sendGameMessage("You cut a thatch spar from the jungle.");
            addFavour(player, 1);

            int nextObjectId = currentObjectId + 1;
            replaceJungleObject(nextObjectId, definition.isDepleted(nextObjectId)
                    ? RESOURCE_RESPAWN_TICKS : 99999);
            currentObjectId = nextObjectId;
            if (definition.isDepleted(currentObjectId)) {
                cycle.stop();
                return;
            }
            if (player.getInventoryManager().getContainer().getFirstFreeSlot() == -1) {
                cycle.stop();
                return;
            }
        }
        swing();
    }

    private boolean canContinue() {
        if (!player.isCurrentActionSequence(actionSequence)) return false;
        if (player.getEquipmentManager().getItemIdAtSlot(3) != macheteId) return false;
        if (player.getSkillManager().getCurrentLevels()[8] < definition.getRequiredLevel()) return false;
        if (player.getInventoryManager().getContainer().getFirstFreeSlot() == -1) return false;
        DynamicObject dynamic = ObjectManager.findDynamicObjectAt(x, y, player.getPosition().getPlane());
        if (currentObjectId == definition.getBaseObjectId()) {
            return dynamic == null || dynamic.getWorldObject().getObjectId() == currentObjectId;
        }
        return dynamic != null && dynamic.getWorldObject().getObjectId() == currentObjectId;
    }

    private boolean rollSuccess() {
        int tier = macheteTier(macheteId);
        return GameUtil.rollLevelScaledChance(definition.getChanceLow(), definition.getChanceHigh(),
                player.getSkillManager().getCurrentLevels()[8], MACHETE_SPEED[tier]);
    }

    private boolean trySpawnEvent() {
        int chance = player.jungleEventChanceCounter;
        if (chance <= 0) return false;
        int eventRollTicks = Math.max(1, cycleTicks - 5);
        for (int i = 0; i < eventRollTicks; i++) {
            if (GameUtil.randomInt(101) < chance) {
                player.jungleEventChanceCounter = 0;
                spawnEvent();
                return true;
            }
        }
        return false;
    }

    private void spawnEvent() {
        int roll = GameUtil.randomInt(100);
        if (roll < 22) spawnThreat(2491, "A jungle spider leaps out of the undergrowth!");
        else if (roll < 43) spawnThreat(2496, "A tribesman bursts out of the jungle!");
        else if (roll < 64) spawnThreat(2489, "A bush snake slithers out of the jungle!");
        else if (roll < 72) spawnThreat(2493, "A large mosquito flies out of the jungle!");
        else if (roll < 79) spawnThreat(2494, "A mosquito swarm pours out of the jungle!");
        else if (roll < 85) spawnThreat(2495, "A mosquito swarm pours out of the jungle!");
        else if (roll < 90) spawnResource(9030, "You uncover a gem rock in the jungle.");
        else if (roll < 94) spawnThreat(2498, "A broodoo victim appears from the jungle!");
        else if (roll < 97) spawnThreat(2500, "A broodoo victim appears from the jungle!");
        else if (roll < 99) spawnThreat(2502, "A broodoo victim appears from the jungle!");
        else spawnResource(9033, "You hack away the remaining bush to reveal an unusual plant.");
    }

    private void spawnThreat(int npcId, String message) {
        Position spawn = findThreatSpawn();
        GameplayHelper.spawnNpcWithRemovalDelay(new Npc(npcId), spawn.getX(), spawn.getY(),
                spawn.getPlane(), 100);
        player.packetSender.sendGameMessage(message);
    }

    private Position findThreatSpawn() {
        Position origin = player.getPosition();
        int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0},
                {1, -1}, {1, 1}, {-1, 1}, {-1, -1}};
        int start = GameUtil.randomInt(directions.length);
        for (int i = 0; i < directions.length; i++) {
            int[] direction = directions[(start + i) % directions.length];
            int tx = origin.getX() + direction[0];
            int ty = origin.getY() + direction[1];
            if (WalkingCollisionMap.canTravelBetween(origin.getX(), origin.getY(), tx, ty,
                    origin.getPlane(), 1, 1)) return new Position(tx, ty, origin.getPlane());
        }
        return new Position(x, y, origin.getPlane());
    }

    private void spawnResource(int resourceObjectId, String message) {
        int orientation = SkillActionHelper.getObjectOrientation(currentObjectId, x, y,
                player.getPosition().getPlane());
        removeCurrentDynamicObject();
        new DynamicObject(resourceObjectId, x, y, player.getPosition().getPlane(), orientation, 10,
                definition.getBaseObjectId(), RESOURCE_RESPAWN_TICKS, false);
        player.packetSender.sendGameMessage(message);
    }

    private void replaceJungleObject(int newObjectId, int remainingTicks) {
        int orientation = SkillActionHelper.getObjectOrientation(currentObjectId, x, y,
                player.getPosition().getPlane());
        removeCurrentDynamicObject();
        new DynamicObject(newObjectId, x, y, player.getPosition().getPlane(), orientation, 10,
                definition.getBaseObjectId(), remainingTicks, false);
    }

    private void removeCurrentDynamicObject() {
        DynamicObject current = ObjectManager.findDynamicObjectAt(x, y, player.getPosition().getPlane());
        if (current != null) {
            ObjectManager.getInstance().removeDynamicObjectAt(x, y, player.getPosition().getPlane(), 0);
        }
    }

    private static int cycleTicks(int level) {
        return Math.max(8, 17 - Math.max(1, level / 10));
    }

    private static int macheteTier(int itemId) {
        for (int i = 0; i < MACHETES.length; i++) if (MACHETES[i] == itemId) return i;
        return -1;
    }

    public static boolean handleFirstObjectAction(Player player, int objectId, int x, int y, int plane) {
        if (!isSpecialResource(objectId)) return false;
        if (!player.isMember() || ServerSettings.freeToPlayWorld) {
            player.packetSender.sendGameMessage("You need to be on a members world with a members account to do this.");
            return true;
        }
        if (objectId == 9033) {
            player.packetSender.sendGameMessage("You need to use a spade on the unusual plant.");
            return true;
        }
        if (objectId < 9030 || objectId > 9032) return false;
        if (player.getSkillManager().getCurrentLevels()[14] < 40) {
            player.packetSender.sendGameMessage("You need a Mining level of 40 to mine this gem rock.");
            return true;
        }
        GatheringToolDefinition pickaxe = ItemCombinationHandler.findUsableGatheringTool(player, 14);
        if (pickaxe == null) {
            player.packetSender.sendGameMessage("You do not have a pickaxe that you can use.");
            return true;
        }
        if (player.getInventoryManager().getContainer().getFreeSlots() < 3) {
            player.packetSender.sendGameMessage("You need three free inventory spaces to mine this gem rock.");
            return true;
        }
        player.getUpdateState().setAnimation(pickaxe.getGatherAnimationId(), 0);
        for (int i = 0; i < 3; i++) {
            player.getInventoryManager().addItem(new ItemStack(rollGem(), 1));
            player.getSkillManager().addExperience(14, 65.0);
        }
        player.packetSender.sendGameMessage("You mine the gems from the exposed rock.");
        removeResourceObject(x, y, plane, objectId);
        return true;
    }

    public static boolean handleItemOnObject(Player player, int itemId, int objectId,
                                             int x, int y, int plane) {
        if (objectId != 9033 || itemId != 952) return false;
        if (!player.isMember() || ServerSettings.freeToPlayWorld) {
            player.packetSender.sendGameMessage("You need to be on a members world with a members account to do this.");
            return true;
        }
        DynamicObject resource = ObjectManager.findDynamicObjectAt(x, y, plane);
        if (resource == null || resource.getWorldObject().getObjectId() != 9033) return false;
        if (player.getInventoryManager().getContainer().getFirstFreeSlot() == -1) {
            player.packetSender.sendGameMessage("Not enough space in your inventory.");
            return true;
        }
        player.getInventoryManager().addItem(new ItemStack(6311, 1));
        player.packetSender.sendGameMessage("You dig up the unusual plant and find a gout tuber.");
        removeResourceObject(x, y, plane, objectId);
        return true;
    }

    private static void removeResourceObject(int x, int y, int plane, int expectedObjectId) {
        DynamicObject resource = ObjectManager.findDynamicObjectAt(x, y, plane);
        if (resource != null && resource.getWorldObject().getObjectId() == expectedObjectId) {
            ObjectManager.getInstance().removeDynamicObjectAt(x, y, plane, 0);
        }
    }

    private static int rollGem() {
        int roll = GameUtil.randomInt(128);
        if (roll == 0) return 1617;       // uncut diamond
        if (roll < 5) return 1619;        // uncut ruby
        if (roll < 13) return 1621;       // uncut emerald
        if (roll < 29) return 1623;       // uncut sapphire
        if (roll < 53) return 1629;       // uncut red topaz
        if (roll < 85) return 1627;       // uncut jade
        return 1625;                      // uncut opal
    }

    @Override
    public void onStop() {
        player.getUpdateState().setAnimation(-1, 0);
    }
}
