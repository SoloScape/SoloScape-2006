import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.cache.js5.ConfigReader;
import com.rs2.cache.js5.Definitions;
import com.rs2.cache.js5.Js5CacheStore;
import java.io.File;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.dialogue.DialogueManager;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.interaction.NpcActionRouter;
import com.rs2.model.interaction.RoutedNpcActionTask;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcMovementMode;
import com.rs2.model.npc.NpcUpdateTask;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.DynamicObject;
import com.rs2.model.objects.ObjectManager;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.CharacterFileRecord;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.quest.QuestEventRegistry;
import com.rs2.model.quest.event.GublinchChristmasEvent;
import com.rs2.model.skill.SkillActionHelper;
import com.rs2.model.task.TickTask;
import com.rs2.model.task.CycleEventHandler;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.InterfaceBridge;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.MovementPacketHandler;
import java.nio.ByteBuffer;
import com.rs2.util.CharacterFileManager;
import com.rs2.util.path.WalkingCollisionMap;
import com.rs2.util.path.ProjectileCollisionMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.net.InetSocketAddress;
import java.io.DataInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public final class GublinchChristmasEventChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 1;
        ServerSettings.freeToPlayWorld = false;
        // Check real assets too: a later revision's sequence ID can be sent
        // successfully by the server while producing no animation in the client.
        byte[] snowballSequence = Definitions.readGroup(12).get(5067);
        check(snowballSequence != null, "Snowball-making sequence missing from 443 cache");
        ConfigReader sequence = new ConfigReader(snowballSequence);
        int opcode;
        while ((opcode = sequence.readUnsignedByte()) != 1) {
            if (opcode == 2 || opcode == 6 || opcode == 7) sequence.skip(2);
            else if (opcode == 3) sequence.skip(sequence.readUnsignedByte());
            else if (opcode == 4) { /* Boolean flag. */ }
            else if (opcode >= 5 && opcode <= 11) sequence.skip(1);
            else throw new AssertionError("Unexpected snowball sequence opcode " + opcode);
        }
        int frameCount = sequence.readUnsignedByte();
        check(frameCount > 0, "Snowball-making sequence is empty");
        int snowballDuration = 0;
        for (int i = 0; i < frameCount; i++) snowballDuration += sequence.readUnsignedShort();
        check(snowballDuration == 150, "Snowball collection lock must match the cache animation duration");
        int[] frames = new int[frameCount];
        for (int i = 0; i < frameCount; i++) frames[i] = sequence.readUnsignedShort();
        for (int i = 0; i < frameCount; i++) frames[i] |= sequence.readUnsignedShort() << 16;
        try (Js5CacheStore cache = new Js5CacheStore(new File("cache"))) {
            for (int frame : frames) {
                check(cache.readFiles(0, frame >>> 16).get(frame & 65535) != null,
                        "Snowball-making frame missing from 443 cache");
            }
        }
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        ProjectileCollisionMap.loadCollisionMaps();
        QuestEventRegistry.initializeEventHooks();
        checkGublinchSpawns();
        check(WorldObjectLookup.findObjectByIdAt(20100,3160,5363,0) != null,
                "Original animated children are missing from their cache location");
        for (Npc npc : World.getNpcs())
            check(npc == null || npc.getOriginalNpcId() < 5023 || npc.getOriginalNpcId() > 5025,
                    "Separate dungeon NPC duplicates an original scenery child");
        int[][] cages = {{2907,3183}, {2907,3184}, {2907,3185},
                {2904,3188}, {2904,3189}, {2904,3190}, {2904,3191},
                {2907,3188}, {2907,3190}, {2907,3189}};
        for (int[] tile : cages) {
            check(ObjectManager.findDynamicObjectByIdAt(19036, tile[0], tile[1], 0) != null,
                    "Missing cage at " + tile[0] + "," + tile[1]);
            check(ObjectManager.findDynamicObjectByIdAt(19036,tile[0],tile[1],0).getWorldObject().getOrientation()
                    == (tile[0] == 2907 ? 1 : 3), "Cage faces the wrong direction");
            int adjacentX = tile[0] == 2904 ? 2905 : 2906;
            check(WalkingCollisionMap.canTravelBetween(2905,3186,adjacentX,tile[1],0,1,1),
                    "Cage cannot be reached at " + tile[0] + "," + tile[1]);
        }
        check(ObjectManager.findDynamicObjectByIdAt(19036,2907,3191,0) == null,
                "Removed cage still exists");
        check((WalkingCollisionMap.getTileFlags(2924,3184,0) & 0x200000) != 0,
                "Incorrect platform was not removed");
        check(ItemDefinition.forId(10501).getEquipmentSlot() == 3, "Snowball must wield in weapon slot");
        check(ItemDefinition.forId(10506).getEquipmentSlot() == -1, "Shards must not be wearable");
        check(ItemDefinition.forId(10508).getEquipmentSlot() == -1, "Tree must not be wearable");
        check(ItemDefinition.forId(10507).getEquipmentSlot() == 0, "Hat must wear in head slot");
        check(ItemDefinition.forId(10507).isUntradeable() && ItemDefinition.forId(10508).isUntradeable(), "Rewards must be untradeable");
        check(QuestEventRegistry.getEventHook(5) instanceof GublinchChristmasEvent, "Event must register");
        int npcCount = countNpcs();
        int scheduledTasks = World.getTaskScheduler().getTasks().size();
        // Reinitialization must also clear copies left by the previous event implementation.
        com.rs2.model.GameplayHelper.spawnNpc(5023,3162,5338,0,4);
        com.rs2.model.GameplayHelper.spawnNpc(5024,3164,5338,0,4);
        com.rs2.model.GameplayHelper.spawnNpc(5025,3166,5338,0,4);
        QuestEventRegistry.getEventHook(5).initialize();
        check(countNpcs() == npcCount, "Initialization duplicated NPCs or retained old child copies");
        check(WorldObjectLookup.findObjectByIdAt(20100,3160,5363,0) != null,
                "Removing child copies also removed their original scenery");
        check(World.getTaskScheduler().getTasks().size() == scheduledTasks, "Initialization duplicated shanties");
        checkGublinchSpawns();
        checkShanties();
        checkCharosDialogue();
        for (Npc npc : World.getNpcs()) {
            if (npc != null && (GublinchChristmasEvent.isGublinch(npc.getNpcId())
                    || npc.getNpcId() == 828 || npc.getNpcId() >= 5023 && npc.getNpcId() <= 5025
                    || npc.getNpcId() >= 5046 && npc.getNpcId() <= 5048)) {
                Position tile = npc.getPosition();
                if (npc.getNpcId() == 828) {
                    check(tile.equals(new Position(2904,3186,0)), "Shanty Claws is at the wrong position");
                    check(npc.getFacingDirection() == 3, "Shanty Claws must face south");
                }
                check((WalkingCollisionMap.getTileFlags(tile.getX(), tile.getY(), 0) & 0x200100) == 0,
                        "Event NPC spawned on blocked terrain: " + tile);
            }
        }
        ObjectManager.getInstance().processObjects();
        check(ObjectManager.findDynamicObjectByIdAt(19039, 2843, 3141, 0) != null, "Entrance expired");
        check(ObjectManager.findDynamicObjectByIdAt(19039, 2856, 3162, 0) == null, "Old eastern entrance remains");
        check((WalkingCollisionMap.getTileFlags(2844,3143,0) & 0x200100) == 0,
                "Cave exit returns onto blocked terrain");
        check(WalkingCollisionMap.canTravelBetween(2844,3143,2842,3143,0,1,1)
                        && WalkingCollisionMap.canTravelBetween(2842,3143,2842,3144,0,1,1),
                "Player cannot walk away from the cave exit");
        Player player = player(1);
        World.getPlayers()[1] = player;
        Player other = player(2);
        World.getPlayers()[2] = other;
        for (int id = 5023; id <= 5025; id++) {
            check(!GublinchChristmasEvent.isNpcVisible(player, id),
                    "Old dungeon child copy remains visible before the rescue: " + id);
        }
        player.setPosition(new Position(2844, 3140, 0));
        check(player.getQuestManager().handleFirstObjectAction(19039, 2843, 3141), "Entrance not routed");
        check(player.getPosition().getY() == 3140, "Entered before speaking to Shanty");
        check(!GublinchChristmasEvent.reclaimRewards(player), "Unstarted account acquired rewards");
        check(player.getQuestManager().handleFirstNpcAction(828), "Shanty dialogue not routed");
        check(player.questHookStates[5] == 0, "Opening the introduction started the event before Continue");
        int staleContinue = continueWidget(player);
        walkAway(player,99);
        continueDialogue(player,staleContinue);
        check(player.questHookStates[5] == 0 && player.getDialogueManager().isDialogueInactive(),
                "Walking away or a stale Continue advanced the introduction");
        player.getQuestManager().handleFirstNpcAction(828);
        continueDialogue(player);
        check(player.questHookStates[5] == 0, "Event started before the instructions were acknowledged");
        staleContinue = continueWidget(player);
        walkAway(player,80);
        continueDialogue(player,staleContinue);
        check(player.questHookStates[5] == 0, "Minimap walking advanced the introduction");
        player.getQuestManager().handleFirstNpcAction(828);
        continueDialogue(player);
        continueDialogue(player);
        advanceToMenu(player, 80);
        check(player.questHookStates[5] == 1024, "Event did not start");
        checkQuestions(player, 80, 5);
        check(player.getQuestManager().handleSecondObjectAction(19039, 2843, 3141), "Entrance search not routed");
        finishConversation(player);
        check(player.questHookStates[5] == 1024, "Searching entrance changed progress");
        check(player.getQuestManager().handleItemOnNpc(828,10506), "Using shards on Shanty not routed");
        check(player.getDialogueManager().getDialogueStep() / 100 == 18, "Wrong shard-use conversation");
        finishConversation(player);
        check(player.getQuestManager().handleItemOnNpc(828,590), "Using another item on Shanty not routed");
        check(player.getDialogueManager().getDialogueStep() / 100 == 19, "Wrong item-use conversation");
        finishConversation(player);
        check(!GublinchChristmasEvent.isNpcVisible(player, 5046), "Rescued child visible too early");
        int[][] snowPiles = {{2841,3147}, {2844,3147}, {2840,3150}, {2842,3153}};
        for (int i = 0; i < snowPiles.length; i++) {
            int id = i % 2 == 0 ? 19030 : 19031;
            int x = snowPiles[i][0], y = snowPiles[i][1];
            check(ObjectManager.findDynamicObjectByIdAt(id,x,y,0) != null, "Missing snow pile at " + x + "," + y);
            check(player.getQuestManager().handleFirstObjectAction(id,x,y), "Snow pile interaction not routed");
            check(player.getInventoryManager().getItemAmount(10501) == 3, "Snow pile did not give three balls");
            check(player.getUpdateState().getAnimationId() == 5067, "Snowball-making animation missing");
            check(player.isActionLocked(), "Snowball collection did not lock actions during animation");
            TickTask collection = (TickTask) World.getTaskScheduler().getTasks().get(
                    World.getTaskScheduler().getTasks().size() - 1);
            for (int tick = 0; tick < 5; tick++) {
                for (int click = 0; click < 10; click++) {
                    player.getQuestManager().handleFirstObjectAction(19031,2844,3147);
                }
                check(player.getInventoryManager().getItemAmount(10501) == 3,
                        "Repeated snow pile clicks bypassed the animation");
                check(player.isActionLocked(), "Snowball collection unlocked before the animation ended");
                collection.tick();
            }
            check(!player.isActionLocked() && !collection.isActive(),
                    "Snowball collection did not unlock after the animation ended");
            player.getInventoryManager().removeItem(new ItemStack(10501,3));
        }
        check(ObjectManager.findDynamicObjectByIdAt(19030,2841,3143,0) == null
                && ObjectManager.findDynamicObjectByIdAt(19031,2844,3143,0) == null, "Old snow piles remain");
        check(!player.getQuestManager().handleFirstObjectAction(19030,2841,3143), "Old snow pile position still accepted");
        player.getQuestManager().handleFirstObjectAction(19030, 2841, 3147);
        check(player.getInventoryManager().getItemAmount(10501) == 3, "Snow must give three balls");
        TickTask collection = (TickTask) World.getTaskScheduler().getTasks().get(
                World.getTaskScheduler().getTasks().size() - 1);
        for (int tick = 0; tick < 5; tick++) collection.tick();
        checkClimb(player, 19039, 2843, 3141, new Position(3168, 5320, 0));
        Npc target = gublinch();
        Position targetTile = target.getPosition().copy();
        check(NpcActionRouter.resolve(target.getDefinition(), 1) == NpcActionRouter.Action.PELT, "Pelt cache action not routed");
        check(!GublinchChristmasEvent.pelt(player, target), "Pelt worked without wielding balls");
        player.getInventoryManager().removeItem(new ItemStack(10501, 3));
        player.getEquipmentManager().getContainer().setItem(3, new ItemStack(10501, 3));
        player.setPosition(new Position(targetTile.getX() + 1, targetTile.getY(), 0));
        player.getLastKnownRegionPosition().set(player.getPosition());
        player.refreshLocalViewArea();
        new RoutedNpcActionTask(player, player.nextActionSequence(), target, 1, NpcActionRouter.Action.PELT).execute();
        for (int i = 0; i < 12; i++) {
            for (Object task : new ArrayList<Object>(World.getTaskScheduler().getTasks())) {
                TickTask tick = (TickTask)task;
                if (tick.isActive()) tick.tick();
            }
        }
        check(player.getEquipmentManager().getContainer().getItemAmount(10501) == 0, "Pelt did not consume three balls");
        GroundItem shards = GroundItemManager.findVisibleItem(player, 10506, targetTile);
        check(shards != null, "Three hits did not drop shards");
        check(GroundItemManager.findVisibleItem(other, 10506, targetTile) == null, "Shards leaked to another account");
        check(!GublinchChristmasEvent.pelt(player, target), "Removed target accepted another pelt");
        checkClimb(player, 19040, 3168, 5319, new Position(2844, 3143, 0));
        player.getInventoryManager().addItem(new ItemStack(10506, 11));
        player.getQuestManager().handleFirstNpcAction(828);
        check(player.getDialogueManager().getDialogueStep() / 100 == 3, "Shards-in-inventory reminder missing");
        advanceToMenu(player, 80);
        check(!GublinchChristmasEvent.useShardsOnCage(player,10506,19036,2907,3191,0),
                "Removed cage accepted shards");
        check(GublinchChristmasEvent.useShardsOnCage(player, 10506, 19036, 2907, 3183, 0), "Cage not handled");
        int left = player.getInventoryManager().getItemAmount(10506);
        player.getQuestManager().handleFirstNpcAction(828);
        check(player.getDialogueManager().getDialogueStep() / 100 == 4, "Caged-gublinch reminder missing");
        advanceToMenu(player, 80);
        GublinchChristmasEvent.useShardsOnCage(player, 10506, 19037, 2907, 3183, 0);
        check(player.getInventoryManager().getItemAmount(10506) == left, "Duplicate cage consumed shards");
        check(GublinchChristmasEvent.cageDisplayId(player, 19036, 2907, 3183, 0) == 19037, "Filled cage appearance missing");
        check(GublinchChristmasEvent.cageDisplayId(other, 19036, 2907, 3183, 0) == 19036, "Cage progress leaked to another account");
        check(SkillActionHelper.findWorldObjectById(19037, 2907, 3183, 0) != null, "Filled cage cannot receive item use");
        persistence(player);
        check(!GublinchChristmasEvent.useShardsOnCage(player, 10506, 19036, 3100, 3100, 0), "Unrelated cage accepted shards");
        for (int i = 1; i < 9; i++) GublinchChristmasEvent.useShardsOnCage(player, 10506, 19036, cages[i][0], cages[i][1], 0);
        check(!GublinchChristmasEvent.isNpcVisible(player, 5046), "Children escaped before all ten required cages were filled");
        GublinchChristmasEvent.useShardsOnCage(player, 10506, 19036, cages[9][0], cages[9][1], 0);
        check(GublinchChristmasEvent.cageDisplayId(player,19036,2907,3188,0) == 19037,
                "New required cage did not record progress");
        check(GublinchChristmasEvent.isNpcVisible(player, 5046) && !GublinchChristmasEvent.isNpcVisible(player, 5023), "Children did not escape");
        player.getInventoryManager().getContainer().clear();
        player.getInventoryManager().addItem(new ItemStack(554, 1));
        player.getInventoryManager().addItem(new ItemStack(555, 1));
        for (int i = 0; i < 25; i++) player.getInventoryManager().addItem(new ItemStack(590, 1));
        check(player.getInventoryManager().getContainer().getFreeSlots() == 1, "Full-inventory fixture wrong: " + player.getInventoryManager().getContainer().getFreeSlots());
        check(player.isMember() && !ServerSettings.freeToPlayWorld, "Members fixture wrong");
        player.getQuestManager().handleFirstNpcAction(828);
        check(!GublinchChristmasEvent.isComplete(player) && !player.ownsItem(10507), "Reward granted before Continue");
        finishConversation(player);
        check(GublinchChristmasEvent.isComplete(player) && player.ownsItem(10507) && !player.ownsItem(10508),
                "One free slot must award the hat and defer the tree");
        player.getInventoryManager().removeItem(new ItemStack(590, 1));
        player.getQuestManager().handleFirstNpcAction(828);
        finishConversation(player);
        check(GublinchChristmasEvent.isComplete(player) && player.ownsItem(10507) && player.ownsItem(10508), "Rewards/completion missing");
        player.getQuestManager().handleFirstNpcAction(828);
        advanceToMenu(player, 90);
        checkQuestions(player, 90, 3);
        check(player.getInventoryManager().getItemAmount(10507) == 1, "Repeated dialogue duplicated hat");
        persistence(player);
        player.getInventoryManager().removeItem(new ItemStack(10507, 1));
        player.getQuestManager().handleFirstNpcAction(970);
        check(player.getInventoryManager().getItemAmount(10507) == 1, "Diango did not recover hat");
        player.getInventoryManager().removeItem(new ItemStack(10507, 1));
        player.getBankContainer().addToTab(new ItemStack(10507, 1), 0);
        player.getQuestManager().handleFirstNpcAction(970);
        check(player.getInventoryManager().getItemAmount(10507) == 0, "Diango duplicated banked hat");
        player.getBankContainer().clear();
        player.getQuestManager().handleFirstNpcAction(970);
        player.getInventoryManager().removeItem(new ItemStack(10508, 1));
        ServerSettings.freeToPlayWorld = true;
        GublinchChristmasEvent.reclaimRewards(player);
        check(!player.ownsItem(10508), "Free world awarded members tree");
        ServerSettings.freeToPlayWorld = false;
        player.getQuestManager().handleFirstNpcAction(970);
        check(player.ownsItem(10508), "Members tree recovery failed");
        player.getEquipmentManager().getContainer().setItem(0, new ItemStack(10507, 1));
        player.setActionLocked(false);
        GublinchChristmasEvent.operateHat(player);
        check(player.getUpdateState().getAnimationId() == 5059, "Hat operation animation missing");
        checkRewardBranches(other);
        checkPeltVisuals();
        checkGublinchRespawns();
        System.out.println("Gublinch Christmas checks passed: dungeon population, roaming, respawns, routing, terrain, snowballs, pelting, private shards, cages, rescue, rewards, reclaim and persistence.");
        System.exit(0);
    }

    private static void checkGublinchSpawns() {
        // Flood-fill real collision through the cave's winding corridors.
        Set<String> reachable = new HashSet<String>();
        ArrayDeque<Position> pending = new ArrayDeque<Position>();
        pending.add(new Position(3168, 5320, 0));
        reachable.add("3168,5320");
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        while (!pending.isEmpty()) {
            Position from = pending.remove();
            for (int[] direction : directions) {
                int x = from.getX() + direction[0], y = from.getY() + direction[1];
                if (x < 3136 || x > 3199 || y < 5312 || y > 5375 || reachable.contains(x + "," + y)) continue;
                if (WalkingCollisionMap.canTravelBetween(from.getX(), from.getY(), x, y, 0, 1, 1)) {
                    reachable.add(x + "," + y);
                    pending.add(new Position(x, y, 0));
                }
            }
        }
        Set<String> anchors = new HashSet<String>();
        int west = 0, east = 0, north = 0;
        for (Npc npc : World.getNpcs()) {
            if (npc == null || !GublinchChristmasEvent.isGublinch(npc.getOriginalNpcId())) continue;
            Position spawn = npc.getSpawnPosition();
            String key = spawn.getX() + "," + spawn.getY();
            check(anchors.add(key), "Duplicate Gublinch spawn at " + spawn);
            check(spawn.getPlane() == 0 && reachable.contains(key), "Gublinch unreachable from ladder: " + spawn);
            check(npc.getMovementMode() == NpcMovementMode.ROAMING, "Gublinch cannot wander: " + spawn);
            int destinations = 0;
            for (int[] direction : directions) {
                int x = spawn.getX() + direction[0], y = spawn.getY() + direction[1];
                if (x >= npc.getSpawnMinPosition().getX() && x < npc.getSpawnMaxPosition().getX()
                        && y >= npc.getSpawnMinPosition().getY() && y < npc.getSpawnMaxPosition().getY()
                        && WalkingCollisionMap.canTravelBetween(spawn.getX(), spawn.getY(), x, y, 0, 1, 1))
                    destinations++;
            }
            check(destinations >= 2, "Gublinch has no room to wander: " + spawn);
            if (spawn.getX() < 3155) west++;
            if (spawn.getX() > 3180) east++;
            if (spawn.getY() >= 5355) north++;
        }
        check(anchors.size() == 16, "Expected sixteen dungeon Gublinch, got " + anchors.size());
        check(west >= 4 && east >= 4 && north >= 4, "Gublinch do not cover both loops and the northern chamber");
    }

    private static void checkGublinchRespawns() throws Exception {
        // Finish the earlier single-target check's pending respawn first.
        advanceEventTicks(12);
        checkGublinchSpawns();
        Player thrower = player(3);
        thrower.questHookStates[5] = 1 << 10;
        thrower.getEquipmentManager().getContainer().setItem(3, new ItemStack(10501, 48));
        Set<String> anchors = new HashSet<String>();
        for (Npc npc : World.getNpcs().clone()) {
            if (npc == null || !GublinchChristmasEvent.isGublinch(npc.getOriginalNpcId())) continue;
            Position spawn = npc.getSpawnPosition();
            anchors.add(spawn.getX() + "," + spawn.getY());
            int index = npc.getIndex();
            check(GublinchChristmasEvent.pelt(thrower, npc), "First snowball failed");
            check(GublinchChristmasEvent.pelt(thrower, npc), "Second snowball failed");
            check(!GublinchChristmasEvent.pelt(thrower, npc), "Third snowball did not remove target");
            check(World.getNpcs()[index] != npc, "Frozen Gublinch was not removed");
            check(!npc.isActive(), "Removed Gublinch remains visible in local NPC lists");
        }
        advanceEventTicks(12);
        checkGublinchSpawns();
        for (Npc npc : World.getNpcs()) {
            if (npc != null && GublinchChristmasEvent.isGublinch(npc.getOriginalNpcId())) {
                Position spawn = npc.getSpawnPosition();
                check(anchors.remove(spawn.getX() + "," + spawn.getY()) && npc.getNpcId() == 5017,
                        "Gublinch respawned at another anchor or remained frozen");
            }
        }
        check(anchors.isEmpty(), "Gublinch respawns missing");
    }

    private static void advanceEventTicks(int count) {
        for (int i = 0; i < count; i++) {
            for (Object task : new ArrayList<Object>(World.getTaskScheduler().getTasks())) {
                TickTask tick = (TickTask) task;
                if (tick.isActive()) tick.tick();
            }
        }
    }

    private static void checkPeltVisuals() throws Exception {
        ConfigReader graphic = new ConfigReader(Definitions.readGroup(13).get(860));
        check(graphic.readUnsignedByte() == 1 && graphic.readUnsignedShort() == 20502,
                "Snowball projectile uses the wrong cache model");
        check(graphic.readUnsignedByte() == 2 && graphic.readUnsignedShort() == 5064,
                "Snowball projectile uses the wrong cache sequence");
        advanceEventTicks(12);
        Npc target = gublinch();
        Position tile = target.getPosition().copy();
        Player thrower = player(3);
        thrower.questHookStates[5] = 1 << 10;
        thrower.setPosition(new Position(tile.getX() + 1, tile.getY(), 0));
        thrower.getEquipmentManager().getContainer().setItem(3, new ItemStack(10501, 3));
        check(thrower.shouldHideEquipmentItemInAppearance(3),
                "Snowball without a worn model must be hidden from player appearance");
        check(!thrower.shouldHideEquipmentItemInAppearance(5),
                "Snowball appearance workaround must not hide the shield slot");
        check(thrower.getStandAnimation() == 808 && thrower.getWalkAnimation() == 819
                && thrower.getRunAnimation() == 824, "Snowball must use player-safe movement animations");
        Player observer = player(4);
        observer.setPosition(thrower.getPosition().copy());
        observer.getLastKnownRegionPosition().set(observer.getPosition());
        observer.refreshLocalViewArea();
        // Capture actual zone packets over loopback, independently of pelting's
        // return value and the server's animation state.
        try (ServerSocketChannel listener = ServerSocketChannel.open()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            try (SocketChannel sender = SocketChannel.open(listener.getLocalAddress());
                 SocketChannel receiver = listener.accept()) {
                receiver.socket().setSoTimeout(2000);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(observer, sender);
                World.getPlayers()[4] = observer;
                observer.getLocalNpcs().add(target);
                IsaacCipher decoder = new IsaacCipher(new int[4]);
                DataInputStream input = new DataInputStream(receiver.socket().getInputStream());
                for (int hit = 0; hit < 3; hit++) {
                    check(GublinchChristmasEvent.pelt(thrower, target) == (hit < 2), "Pelt progression wrong");
                    byte[] packet = new byte[19];
                    input.readFully(packet);
                    check(((packet[0] & 255) - decoder.nextInt() & 255) == 82, "Projectile zone base missing");
                    check(((packet[3] & 255) - decoder.nextInt() & 255) == 101, "Snowball projectile packet missing");
                    check(packet[5] == -1 && packet[6] == 0, "Snowball flies to the wrong tile");
                    check(packet[7] == 0 && packet[8] == 0, "Snowball follows a removed/reused NPC index");
                    check(((packet[9] & 255) << 8 | packet[10] & 255) == 860, "Wrong snowball effect sent");
                    check(thrower.getUpdateState().getGraphicId() == 860, "Animated snowball in the throwing hand missing");
                    check(thrower.getUpdateState().getAnimationId() == (hit < 2 ? 5063 : -1),
                            "Throw animation missing or continued after final hit");
                }
                check(!target.isActive(), "Frozen target remains active");
                check(thrower.getInteractionTarget() == null && thrower.getMovementQueue().getSteps().size() <= 1,
                        "Player still follows the removed target");
                NpcUpdateTask.updatePlayer(observer);
                check(!observer.getLocalNpcs().contains(target), "Frozen target survives the next NPC update");
                byte[] header = new byte[3];
                input.readFully(header);
                check(((header[0] & 255) - decoder.nextInt() & 255) == 238, "NPC update missing");
                byte[] update = new byte[(header[1] & 255) << 8 | header[2] & 255];
                input.readFully(update);
                check((update[0] & 255) == 1 && (update[1] & 0xe0) == 0xe0,
                        "Final hit did not serialize immediate NPC removal");
            } finally {
                World.getPlayers()[4] = null;
            }
        }
    }

    private static void checkClimb(Player player, int id, int x, int y, Position destination) {
        Position before = player.getPosition().copy();
        check(player.getQuestManager().handleFirstObjectAction(id, x, y), "Climb not routed: " + id);
        check(player.getUpdateState().getAnimationId() == 828, "Climb animation missing: " + id);
        check(player.isActionLocked(), "Climb did not lock actions: " + id);
        check(player.getPosition().equals(before), "Climb moved before animation: " + id);
        CycleEventHandler events = CycleEventHandler.getInstance();
        events.process();
        check(player.getPosition().equals(before), "Climb delay skipped: " + id);
        events.process();
        check(player.getPosition().equals(destination), "Climb destination wrong: " + id);
        check(!player.isActionLocked(), "Climb left actions locked: " + id);
        check(player.getUpdateState().getAnimationId() == 65535, "Climb animation not cleared: " + id);
        events.process();
        check(!player.isActionLocked(), "Climb continuation left actions locked: " + id);
    }

    private static void continueDialogue(Player player) {
        continueDialogue(player,continueWidget(player));
    }
    private static int continueWidget(Player player) {
        int group = InterfaceBridge.translateGroup(player.getOpenInterfaceId());
        int child = group >= 241 && group <= 244 ? group - 238 : group >= 64 && group <= 67 ? group - 61
                : group == 210 ? 1 : group >= 211 && group <= 214 ? group - 209 : group == 102 ? 3 : group == 249 ? 2 : -1;
        check(child >= 0,"Missing Continue widget for group " + group);
        return group << 16 | child;
    }
    private static void advanceToMenu(Player player, int step) {
        for (int i = 0; i < 100 && player.getDialogueManager().getDialogueStep() != step; i++) {
            check(!player.getDialogueManager().isDialogueInactive(), "Conversation ended before menu " + step);
            continueDialogue(player);
        }
        check(player.getDialogueManager().getDialogueStep() == step, "Dialogue never reached menu " + step);
    }
    private static void finishConversation(Player player) {
        for (int i = 0; i < 100 && !player.getDialogueManager().isDialogueInactive(); i++) continueDialogue(player);
        check(player.getDialogueManager().isDialogueInactive(), "Conversation did not end");
    }
    private static void checkQuestions(Player player, int menu, int count) {
        for (int option = 1; option < count; option++) {
            selectOption(player, menu, option);
            advanceToMenu(player, menu);
        }
        selectOption(player, menu, count);
        finishConversation(player);
    }
    private static void selectOption(Player player, int menu, int option) {
        continueDialogue(player, InterfaceBridge.translate((menu == 80 ? 2493 : 2470) + option));
    }
    private static void checkCharosDialogue() throws Exception {
        java.io.PrintStream original = System.out;
        java.io.ByteArrayOutputStream trace = new java.io.ByteArrayOutputStream();
        boolean debug = ServerSettings.debugModeEnabled;
        try {
            System.setOut(new java.io.PrintStream(trace, true, "UTF-8"));
            ServerSettings.debugModeEnabled = true;
            Player player = player(3);
            player.getQuestManager().handleFirstNpcAction(828);
            continueDialogue(player);
            check(InterfaceBridge.translateGroup(player.getOpenInterfaceId()) == 243, "Greeting must use three body lines");
            check(trace.toString("UTF-8").contains("text='Raise the mainsail and hoist the anchor...' Eh! Oh, the | "
                    + "names 'Shanty'; 'Shanty Claws' to a landlubber like ye. | "
                    + "And oi've some terrible news for yer deck-swabbin' ears."), "Greeting body lines changed");
            walkAway(player, 99);
            player.setGender(1);
            player.getEquipmentManager().getContainer().setItem(12, new ItemStack(4202, 1));
            player.getQuestManager().handleFirstNpcAction(828);
            continueDialogue(player);
            check(trace.toString("UTF-8").contains("fellow werewolf!"), "Ring of Charos greeting missing");
            check(player.questHookStates[5] == 0, "Charos greeting started rescue early");
            advanceToMenu(player, 80);
            selectOption(player, 80, 2);
            advanceToMenu(player, 80);
            check(trace.toString("UTF-8").contains("lass"), "Female address was not substituted");
            selectOption(player, 80, 4);
            advanceToMenu(player, 80);
            check(trace.toString("UTF-8").contains("Certainly, Qa_Christmas_3."), "Player name was not substituted");
            } finally {
            ServerSettings.debugModeEnabled = debug;
            System.setOut(original);
        }
    }
    private static void checkShanties() {
        String[] expected = {"'Sailing on the sea...happy as can be...'", "'...to exotic places...fair winds in our faces...'",
                "'...and we'll sink a drink to our sailor ways...'", "'...polish ye cutlass's makes 'em rust-less...'",
                "'...a brig's too good, so keelhaul him in the mornin'...'"};
        Npc shanty = null;
        for (Npc npc : World.getNpcs()) if (npc != null && npc.getNpcId() == 828) shanty = npc;
        check(shanty != null, "No Shanty for overhead dialogue");
        TickTask song = null;
        for (Object scheduled : World.getTaskScheduler().getTasks()) {
            TickTask task = (TickTask)scheduled;
            if (task.getClass().getEnclosingClass() == GublinchChristmasEvent.class) song = task;
        }
        check(song != null, "No shanty scheduler");
        for (int i = 0; i < 7; i++) {
            song.execute();
            check(expected[i % expected.length].equals(shanty.getUpdateState().getForcedText()), "Shanties out of order");
        }
    }
    private static void checkRewardBranches(Player player) {
        player.questHookStates[5] = 1024 | 1023;
        for (int i = 0; i < 28; i++) player.getInventoryManager().addItem(new ItemStack(590, 1));
        player.getQuestManager().handleFirstNpcAction(828);
        finishConversation(player);
        check(!GublinchChristmasEvent.isComplete(player) && !player.ownsItem(10507), "Full inventory awarded hat");
        player.getInventoryManager().getContainer().clear();
        ServerSettings.freeToPlayWorld = true;
        player.getQuestManager().handleFirstNpcAction(828);
        finishConversation(player);
        check(player.ownsItem(10507) && !player.ownsItem(10508) && GublinchChristmasEvent.isComplete(player), "Free world reward branch wrong");
        ServerSettings.freeToPlayWorld = false;
        player.getQuestManager().handleFirstNpcAction(828);
        finishConversation(player);
        check(player.ownsItem(10508), "Returning on members world did not award tree");
        player.getInventoryManager().removeItem(new ItemStack(10507, 1));
        player.getInventoryManager().removeItem(new ItemStack(10508, 1));
        player.getQuestManager().handleFirstNpcAction(828);
        advanceToMenu(player, 90);
        check(!player.ownsItem(10507) && !player.ownsItem(10508), "Shanty replaced rewards reserved for Diango");
        player.getQuestManager().handleFirstNpcAction(5046);
        finishConversation(player);
        check(player.getQuestManager().handleSecondObjectAction(19039,2843,3141), "Completed search not handled");
        finishConversation(player);
        Position before = player.getPosition().copy();
        player.getQuestManager().handleFirstObjectAction(19039,2843,3141);
        finishConversation(player);
        check(player.getPosition().equals(before), "Completed player reentered cave");
    }
    private static void continueDialogue(Player player,int widget) {
        ByteBuffer payload = ByteBuffer.allocate(6);
        payload.putShort((short)-1).putInt(widget).flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT,6,PacketBuffer.wrapReader(payload)));
    }
    private static void walkAway(Player player,int opcode) {
        int x = player.getPosition().getX(), y = player.getPosition().getY();
        ByteBuffer payload = ByteBuffer.allocate(opcode == 80 ? 19 : 5);
        payload.put((byte)(x+128)).put((byte)(x>>8)).put((byte)0).put((byte)y).put((byte)(y>>8));
        if (opcode == 80) payload.put(new byte[14]);
        payload.flip();
        new MovementPacketHandler().handle(player,new IncomingPacket(opcode,payload.remaining(),PacketBuffer.wrapReader(payload)));
    }
    private static int countNpcs() { int count = 0; for (Npc n : World.getNpcs()) if (n != null) count++; return count; }
    private static Npc gublinch() { for (Npc n : World.getNpcs()) if (n != null && n.getNpcId() == 5017) return n; throw new AssertionError("No gublinch spawned"); }
    private static Player player(int index) throws Exception {
        Player player = new Player(null);
        player.setUsername("qa_christmas_" + index);
        SocketChannel transport = SocketChannel.open(); transport.close();
        Field socket = Player.class.getDeclaredField("socketChannel"); socket.setAccessible(true); socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setIndex(index); player.setEncodedIndex(32768 + index);
        player.setQuestState(0, 1); player.setPosition(new Position(2918, 3175, 0)); player.refreshLocalViewArea();
        return player;
    }

    private static void persistence(Player player) throws Exception {
        String username = "qa_gublinch_event";
        Path file = Paths.get("data/characters/" + username + ".dat");
        check(!Files.exists(file), "Test account already exists");
        try {
            player.setUsername(username); player.setPassword("test"); player.lastLoginHostAddress = "127.0.0.1";
            Field host = Player.class.getDeclaredField("hostAddress"); host.setAccessible(true); host.set(player, "127.0.0.1");
            Method writer = CharacterFileManager.class.getDeclaredMethod("writePlayerFile", Player.class); writer.setAccessible(true); writer.invoke(null, player);
            Player loaded = new Player(null); loaded.setUsername(username);
            CharacterFileManager.loadPlayerFromFile("./data/characters/", loaded);
            check(loaded.questHookStates[5] == player.questHookStates[5], "Saved cage/completion state lost");
            Method reader = CharacterFileManager.class.getDeclaredMethod("readCharacterFileRecord", String.class, String.class, boolean.class);
            reader.setAccessible(true);
            CharacterFileRecord record = (CharacterFileRecord)reader.invoke(null, "./data/characters/", username, true);
            check(record.questHookStates[5] == player.questHookStates[5], "Offline record lost event state");
            CharacterFileManager.saveCharacterFileRecord(record);
            CharacterFileManager.loadPlayerFromFile("./data/characters/", loaded);
            check(loaded.questHookStates[5] == player.questHookStates[5], "Offline rewrite lost event state");
        } finally { Files.deleteIfExists(file); }
    }
    private static void check(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
