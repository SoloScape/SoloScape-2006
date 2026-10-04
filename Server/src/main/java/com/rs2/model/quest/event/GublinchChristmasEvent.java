package com.rs2.model.quest.event;

import com.rs2.ServerSettings;
import com.rs2.model.GameplayHelper;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.combat.AttackStyleDefinition;
import com.rs2.model.combat.ProjectileTiming;
import com.rs2.model.c.ProjectileDefinition;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcMovementMode;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.LoadedWorldObject;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestHook;
import com.rs2.model.task.TickTask;
import com.rs2.model.skill.woodcutting.WoodcuttingHandler;
import com.rs2.util.GameUtil;
import com.rs2.util.path.WalkingCollisionMap;
import com.rs2.util.path.ProjectileCollisionMap;

/** The 2006 Christmas rescue. Slot 5 is part of the existing saved event-state array. */
public final class GublinchChristmasEvent extends QuestHook {
    public static final int STATE_SLOT = 5;
    public static final int SHANTY_CLAWS = 828;
    public static final int SNOWBALL = 10501;
    // Original Gublinch snowball-making sequence in the revision 443 cache.
    private static final int MAKE_SNOWBALL_ANIMATION = 5067;
    // Sequence 5067 lasts 150 client frames (3 seconds): five 600 ms server ticks.
    private static final int MAKE_SNOWBALL_TICKS = 5;
    private static final ProjectileDefinition SNOWBALL_PROJECTILE =
            new ProjectileDefinition(860, ProjectileTiming.STANDARD);
    public static final int SHARDS = 10506;
    public static final int REINDEER_HAT = 10507;
    public static final int WINTUMBER_TREE = 10508;
    public static final int CAGE = 19036;
    public static final int ENTRANCE = 19039;
    public static final int LADDER = 19040;
    private static final int STARTED = 1 << 10;
    private static final int COMPLETE = 1 << 11;
    private static final int TREE_CLAIMED = 1 << 12;
    private static final int ALL_CAGES = (1 << 10) - 1;
    private static final int DOCK_X = 2904, DOCK_Y = 3186;
    private static final int ENTRY_X = 2843, ENTRY_Y = 3141;
    private static final int[][] SNOW_PILES = {
        {2841, 3147}, {2844, 3147}, {2840, 3150}, {2842, 3153}
    };
    private static boolean shantiesScheduled;
    // Both cave loops, including the northern furnace chamber. Keep spawn
    // anchors on walkable floor; normal NPC pathfinding clips their wandering.
    private static final int[][] GUBLINCH_SPAWNS = {
        {3164, 5324}, {3162, 5327}, {3164, 5330}, {3163, 5333},
        {3145, 5326}, {3147, 5333}, {3152, 5343}, {3146, 5358},
        {3153, 5367}, {3166, 5359}, {3163, 5344}, {3173, 5351},
        {3183, 5367}, {3187, 5356}, {3190, 5343}, {3184, 5332}
    };
    private static final int[][] CAGES = {
        {2907, 3183}, {2907, 3184}, {2907, 3185},
        {2904, 3188}, {2904, 3189}, {2904, 3190}, {2904, 3191},
        {2907, 3188}, {2907, 3190}, {2907, 3189}
    };

    public GublinchChristmasEvent() { super(STATE_SLOT); }

    @Override public void initialize() {
        // Permanent single-player content, independent of the calendar-based rare drops.
        if (ServerSettings.cacheVersion != 443) return;
        spawnOnce(SHANTY_CLAWS, DOCK_X, DOCK_Y);
        if (!shantiesScheduled) {
            shantiesScheduled = true;
            World.scheduleTickTask(new TickTask(10, false) {
                private int verse;
                @Override public void execute() {
                    for (Npc npc : World.getNpcs()) {
                        if (npc != null && npc.getOriginalNpcId() == SHANTY_CLAWS
                                && npc.getSpawnPosition().equals(new Position(DOCK_X, DOCK_Y, 0))) {
                            npc.getUpdateState().setForcedText(GublinchChristmasDialogue.SHANTIES[verse]);
                        }
                    }
                    verse = (verse + 1) % GublinchChristmasDialogue.SHANTIES.length;
                }
            });
        }
        placeObject(ENTRANCE, ENTRY_X, ENTRY_Y);
        for (int i = 0; i < SNOW_PILES.length; i++)
            placeObject(i % 2 == 0 ? 19030 : 19031, SNOW_PILES[i][0], SNOW_PILES[i][1]);
        for (int[] cage : CAGES) placeObject(CAGE, cage[0], cage[1]);
        for (int[] tile : GUBLINCH_SPAWNS) {
            spawnOnce(5017, tile[0], tile[1]);
        }
        // Native animated scenery 20100 at (3160,5363) already contains Jeff at
        // (3161,5363), Jill at (3163,5363), and Jack at (3165,5363), plus the logs
        // and furnace. Separate dungeon NPCs duplicate those original children.
        removeDungeonChildCopies();
        spawnOnce(5046, 2905, 3185);
        spawnOnce(5047, 2905, 3186);
        spawnOnce(5048, 2905, 3187);
    }

    private static void placeObject(int id, int x, int y) {
        if (ObjectManager.findDynamicObjectAt(x, y, 0) != null) return;
        int orientation = id == CAGE ? cageOrientation(x) : id == ENTRANCE ? 2 : 0;
        if (id == ENTRANCE) {
            LoadedWorldObject tree = WorldObjectLookup.findObjectByIdAt(1303, x, y, 0);
            if (tree != null) {
                WalkingCollisionMap.removeObjectCollision(1303, x, y, 0, tree.getOrientation(), tree.getType());
                ProjectileCollisionMap.removeObjectCollision(1303, x, y, 0, tree.getOrientation(), tree.getType());
            }
        }
        // New scenery has no old object to restore; register its own collision.
        new DynamicObject(id, x, y, 0, orientation, 10, -1, 999999, false);
        WalkingCollisionMap.addObjectCollision(id, x, y, 0, orientation, 10, false);
        ProjectileCollisionMap.addObjectCollision(id, x, y, 0, orientation, 10, false);
    }

    // The cage model opens south at rotation 0, unlike wall orientation values.
    private static int cageOrientation(int x) { return x == 2907 ? 1 : 3; }

    private static void spawnOnce(int id, int x, int y) {
        for (Npc npc : World.getNpcs()) {
            if (npc != null && npc.getOriginalNpcId() == id
                    && npc.getSpawnPosition().equals(new Position(x, y, 0))) {
                if (isGublinch(id)) npc.setMovementMode(NpcMovementMode.ROAMING);
                return;
            }
        }
        GameplayHelper.spawnNpc(id, x, y, 0, isGublinch(id) ? 1 : id == SHANTY_CLAWS ? 3 : 4);
    }

    private static void removeDungeonChildCopies() {
        for (Npc npc : World.getNpcs()) {
            if (npc != null && npc.getOriginalNpcId() >= 5023 && npc.getOriginalNpcId() <= 5025)
                World.unregisterNpc(npc);
        }
    }

    public static boolean isGublinch(int id) { return id >= 5017 && id <= 5019; }
    public static boolean isComplete(Player player) { return (player.questHookStates[STATE_SLOT] & COMPLETE) != 0; }
    static boolean started(Player player) { return (player.questHookStates[STATE_SLOT] & STARTED) != 0; }
    static boolean membersReward(Player player) { return player.isMember() && !ServerSettings.freeToPlayWorld; }
    static void start(Player player) { player.questHookStates[STATE_SLOT] |= STARTED; }
    static int cagesRemaining(Player player) { return 10 - Integer.bitCount(player.questHookStates[STATE_SLOT] & ALL_CAGES); }
    static boolean treeClaimed(Player player) {
        // Remember rewards held by accounts completed before separate handoffs were added.
        if (player.ownsItem(WINTUMBER_TREE)) player.questHookStates[STATE_SLOT] |= TREE_CLAIMED;
        return (player.questHookStates[STATE_SLOT] & TREE_CLAIMED) != 0;
    }
    static boolean giveReward(Player player, int item) {
        if (cagesRemaining(player) != 0 || item == WINTUMBER_TREE && !membersReward(player)) return false;
        if (!player.ownsItem(item) && !player.getInventoryManager().addItem(new ItemStack(item, 1))) return false;
        player.questHookStates[STATE_SLOT] |= item == REINDEER_HAT ? COMPLETE : TREE_CLAIMED;
        return true;
    }

    public static boolean isNpcVisible(Player player, int id) {
        boolean rescued = (player.questHookStates[STATE_SLOT] & ALL_CAGES) == ALL_CAGES;
        // The dungeon children are part of scenery 20100. Hide old NPC copies
        // on every update too: live reload does not rerun initialize().
        if (id >= 5023 && id <= 5025) return false;
        if (id >= 5046 && id <= 5048) return rescued;
        return true;
    }

    private static int cageIndex(int x, int y) {
        for (int i = 0; i < CAGES.length; i++) if (CAGES[i][0] == x && CAGES[i][1] == y) return i;
        return -1;
    }

    public static int cageDisplayId(Player player, int id, int x, int y, int plane) {
        int cage = cageIndex(x, y);
        return id == CAGE && plane == 0 && cage >= 0
                && (player.questHookStates[STATE_SLOT] & (1 << cage)) != 0 ? 19037 : id;
    }

    /** Filled cages have the same geometry and server position as the empty cage. */
    public static boolean isCageAppearance(int id, int x, int y, int plane) {
        return id == 19037 && plane == 0 && cageIndex(x, y) >= 0;
    }

    @Override public boolean handleFirstNpcAction(Player player, int id, int state) {
        if (id == SHANTY_CLAWS || (id == 970 && isComplete(player))
                || id >= 5023 && id <= 5025 || id >= 5046 && id <= 5048) {
            return DialogueManager.startDialogue(player, id);
        }
        return false;
    }

    @Override public boolean handleNpcDialogue(Player player, int id, int step, int option, int state) {
        if (id == 970 && isComplete(player)) {
            say(player, reclaimRewards(player) ? "Diango returns any missing Christmas rewards."
                    : "Make room in your inventory for your Christmas rewards.");
        } else if (GublinchChristmasDialogue.handle(player, id, step, option)) {
            return true;
        } else if (id >= 5023 && id <= 5025) {
            say(player, "Please help us! The gublinch make us stoke their furnaces.");
        } else return false;
        return true;
    }

    @Override public boolean handleItemOnNpc(Player player, int npcId, int item, int state) {
        if (npcId != SHANTY_CLAWS) return false;
        return DialogueManager.startDialogue(player, item == SHARDS
                ? GublinchChristmasDialogue.ITEM_SHARDS : GublinchChristmasDialogue.ITEM_OTHER);
    }

    @Override public boolean handleSecondObjectAction(Player player, int id, int x, int y, int state) {
        if (id != ENTRANCE || x != ENTRY_X || y != ENTRY_Y || player.getPosition().getPlane() != 0) return false;
        return DialogueManager.startDialogue(player, GublinchChristmasDialogue.SEARCH_ENTRANCE);
    }

    private static void say(Player player, String... lines) {
        player.getDialogueManager().showNpcDialogue(lines, 588);
        player.getDialogueManager().finishDialogue();
    }

    /** Reserve enough room for the entire handoff; never drop a reward or duplicate an owned one. */
    public static boolean reclaimRewards(Player player) {
        if ((player.questHookStates[STATE_SLOT] & ALL_CAGES) != ALL_CAGES) return false;
        boolean hat = !player.ownsItem(REINDEER_HAT);
        boolean tree = membersReward(player) && !player.ownsItem(WINTUMBER_TREE);
        if (player.getInventoryManager().getContainer().getFreeSlots() < (hat ? 1 : 0) + (tree ? 1 : 0)) return false;
        if (hat) player.getInventoryManager().addItem(new ItemStack(REINDEER_HAT, 1));
        if (tree) {
            player.getInventoryManager().addItem(new ItemStack(WINTUMBER_TREE, 1));
            player.questHookStates[STATE_SLOT] |= TREE_CLAIMED;
        }
        return true;
    }

    @Override public boolean handleFirstObjectAction(Player player, int id, int x, int y, int state) {
        if ((id == 19030 || id == 19031 || id >= 19033 && id <= 19035)
                && isSnowPile(x, y) && player.getPosition().getPlane() == 0) {
            if (player.isActionLocked()) return true;
            if (!started(player) || isComplete(player)) player.packetSender.sendGameMessage("Speak to Shanty Claws on the Musa Point dock first.");
            else if (player.getInventoryManager().addItem(new ItemStack(SNOWBALL, 3))) {
                player.setActionLocked(true);
                player.getUpdateState().setAnimation(MAKE_SNOWBALL_ANIMATION);
                player.packetSender.sendGameMessage("You make three snowballs.");
                World.scheduleTickTask(new TickTask(MAKE_SNOWBALL_TICKS, false) {
                    @Override public void execute() {
                        player.setActionLocked(false);
                        stop();
                    }
                });
            }
            return true;
        }
        if (id == ENTRANCE && x == ENTRY_X && y == ENTRY_Y && player.getPosition().getPlane() == 0) {
            if (!started(player)) player.packetSender.sendGameMessage("Speak to Shanty Claws on the Musa Point dock first.");
            else if ((state & ALL_CAGES) == ALL_CAGES) DialogueManager.startDialogue(player, GublinchChristmasDialogue.CAVE_EMPTY);
            else AttackStyleDefinition.startDelayedObjectMove(player, new Position(3168, 5320, 0));
            return true;
        }
        if (id == LADDER && x == 3168 && y == 5319 && player.getPosition().getPlane() == 0) {
            AttackStyleDefinition.startDelayedObjectMove(player, new Position(ENTRY_X + 1, ENTRY_Y + 2, 0));
            return true;
        }
        return false;
    }

    private static boolean isSnowPile(int x, int y) {
        for (int[] tile : SNOW_PILES) if (tile[0] == x && tile[1] == y) return true;
        return false;
    }

    public static boolean useShardsOnCage(Player player, int item, int object, int x, int y, int plane) {
        if (item != SHARDS || (object != CAGE && object != 19037) || plane != 0) return false;
        int cage = cageIndex(x, y);
        if (cage < 0) return false;
        int bit = 1 << cage;
        if (!started(player)) player.packetSender.sendGameMessage("Speak to Shanty Claws first.");
        else if ((player.questHookStates[STATE_SLOT] & bit) != 0) player.packetSender.sendGameMessage("This cage already contains a gublinch.");
        else if (player.getInventoryManager().removeItem(new ItemStack(SHARDS, 1))) {
            player.questHookStates[STATE_SLOT] |= bit;
            player.packetSender.sendObjectCreate(19037, x, y, plane, cageOrientation(x), 10);
            player.packetSender.sendGameMessage("You place the shards in the cage.");
            player.packetSender.sendGameMessage("The gublinch rattles the cage doors, but cannot escape.");
            if ((player.questHookStates[STATE_SLOT] & ALL_CAGES) == ALL_CAGES) {
                player.packetSender.sendGameMessage("Jack, Jill and Jeff escape! Speak to Shanty Claws for your rewards.");
            }
        }
        return true;
    }

    @Override public boolean canAttackNpc(Player player, int id, int state) {
        if (!isGublinch(id)) return true;
        player.packetSender.sendGameMessage("Wield snowballs and use Pelt to freeze the gublinch.");
        return false;
    }

    public static void startPelting(Player player, Npc npc) {
        final int sequence = player.nextActionSequence();
        World.scheduleTickTask(new TickTask(3, true) {
            @Override public void execute() {
                if (!player.isCurrentActionSequence(sequence) || npc.isDead() || !isGublinch(npc.getNpcId())
                        || npc.getIndex() < 0 || World.getNpcs()[npc.getIndex()] != npc
                        || player.getPosition().getPlane() != npc.getPosition().getPlane()
                        || GameUtil.getDistance(player.getPosition(), npc.getPosition()) > 6
                        || !GameUtil.hasClearPath(player.getPosition(), npc.getPosition(), true)) { stop(); return; }
                if (!pelt(player, npc)) stop();
            }
        });
    }

    /** Returns true while the same target needs another snowball. */
    public static boolean pelt(Player player, Npc npc) {
        if (!started(player)) {
            player.packetSender.sendGameMessage("Speak to Shanty Claws on the Musa Point dock first.");
            return false;
        }
        if (isComplete(player) || !isGublinch(npc.getNpcId()) || npc.isDead()
                || npc.getIndex() < 0 || World.getNpcs()[npc.getIndex()] != npc) return false;
        if (!player.getEquipmentManager().removeItem(new ItemStack(SNOWBALL, 1))) {
            player.packetSender.sendGameMessage("You must wield snowballs to pelt the gublinch.");
            return false;
        }
        player.getUpdateState().setAnimation(5063);
        player.setAppearanceUpdateRequired(true);
        // This cache effect is animated with the throwing hand; the sequence
        // hides the equipped ball while this effect carries it through the throw.
        player.getUpdateState().setGraphic(860);
        player.getUpdateState().setFacePosition(npc.getPosition().copy());
        // Use the landing tile rather than a client NPC index: the final throw
        // removes that NPC, and its index can be reused before the ball lands.
        new WoodcuttingHandler(player.getPosition().copy(), player.getSize(),
                npc.getPosition().copy(), 0, SNOWBALL_PROJECTILE).sendProjectileToNearbyPlayers();
        if (npc.getNpcId() < 5019) {
            npc.transformToNpcId(npc.getNpcId() + 1, 100);
            return true;
        }
        GroundItemManager.getInstance().spawn(new GroundItem(new ItemStack(SHARDS, 1), npc.getPosition(), false, player));
        Position spawn = npc.getSpawnPosition().copy();
        // Local NPC lists retain references after unregistering. Mark this
        // instance inactive so the next update sends its removal immediately.
        npc.setActive(false);
        World.unregisterNpc(npc);
        player.getUpdateState().setAnimation(-1);
        player.getUpdateState().setFaceEntity(65535);
        player.setInteractionTarget(null);
        player.getMovementQueue().clear();
        World.scheduleTickTask(new TickTask(10, false) {
            @Override public void execute() { spawnOnce(5017, spawn.getX(), spawn.getY()); stop(); }
        });
        player.packetSender.sendGameMessage("The frozen gublinch crumbles into shards. Pick them up for Shanty Claws.");
        return false;
    }

    @Override public boolean handleInventoryItemFirstOption(Player player, int id, int slot, int state) {
        if (id != WINTUMBER_TREE) return false;
        player.packetSender.sendGameMessage("Keep this tree for your house garden. Player-owned houses are not available yet.");
        return true;
    }

    public static void operateHat(Player player) {
        if (!player.getEquipmentManager().containsItem(REINDEER_HAT) || player.isActionLocked()) return;
        player.getUpdateState().setAnimation(5059);
        player.getUpdateState().setGraphic(859);
        player.packetSender.sendGameMessage("You act like a reindeer.");
    }
}
