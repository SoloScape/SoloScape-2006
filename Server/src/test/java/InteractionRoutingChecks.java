import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.*;
import com.rs2.model.interaction.*;
import com.rs2.model.item.*;
import com.rs2.model.npc.*;
import com.rs2.model.objects.ObjectDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.shop.*;
import com.rs2.model.skill.fishing.*;
import com.rs2.model.task.TickTask;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.*;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/** Cache-backed packet-to-gameplay checks, including rejection and cancellation. */
public final class InteractionRoutingChecks {
    private static final int X = 3220, Y = 3220;
    private static final NpcInteractionPacketHandler handler = new NpcInteractionPacketHandler();
    private static int[][] terrain;

    public static void main(String[] args) {
        try {
            ServerSettings.clientBuild = 443;
            ServerSettings.membershipRequirementMode = 1;
            ServerSettings.freeToPlayWorld = false;
            ServerSettings.fishingEnabled = true;
            ServerSettings.thievingEnabled = true;
            ServerSettings.craftingEnabled = true;
            QuestDefinition.loadDefinitions();
            ItemDefinition.loadDefinitions();
            NpcDefinition.loadDefinitions();
            ObjectDefinition.loadRevision443();
            ShopManager.loadShops();
            Interfaces.load();
            openTerrain();
            checkBank();
            checkPickpocketAndTrade();
            checkFishing();
            checkTeleport();
            checkTanning();
            checkCancellationAndReach();
            checkShopRestrictions();
            checkShearingAndRewards();
            checkOtherBindings();
            System.out.println("Interaction routing checks passed (bank, thieving, regional fishing, teleport, tanning, shearing, healing, rewards, restrictions, cancellation, slot independence).");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkBank() throws Exception {
        Player player = player();
        Npc banker = npc(494);
        click(player, 3).execute();
        require(player.getOpenInterfaceId() == 5292, "Bank click did not open the bank");
        // Missing cache actions must not invoke a handler merely because of their numeric slot.
        World.getTaskScheduler().getTasks().clear();
        handler.handle(player, npcPacket(2));
        require(World.getTaskScheduler().getTasks().isEmpty(), "Absent banker action scheduled gameplay");
        Field actions = NpcDefinition.class.getDeclaredField("actions");
        actions.setAccessible(true);
        String[] original = (String[]) actions.get(banker.getDefinition());
        try {
            actions.set(banker.getDefinition(), new String[]{null, null, null, null, "Bank"});
            player.setOpenInterfaceId(-1);
            click(player, 5).execute();
            require(player.getOpenInterfaceId() == 5292, "Bank in fifth cache slot was not routed");
        } finally { actions.set(banker.getDefinition(), original); }
    }

    private static void checkPickpocketAndTrade() throws Exception {
        Player player = player();
        Npc man = npc(1);
        click(player, 3).execute();
        require(player.isActionLocked() && player.getUpdateState().getAnimationId() == 881,
                "Pickpocket click did not start thieving");
        // An NPC can have both operations: the clicked action, not shop metadata, decides.
        Field actions = NpcDefinition.class.getDeclaredField("actions");
        Field shop = NpcDefinition.class.getDeclaredField("shopId");
        actions.setAccessible(true); shop.setAccessible(true);
        String[] original = (String[]) actions.get(man.getDefinition());
        int originalShop = shop.getInt(man.getDefinition());
        try {
            actions.set(man.getDefinition(), new String[]{"Talk-to", "Attack", "Pickpocket", null, "Trade"});
            shop.setInt(man.getDefinition(), NpcDefinition.forId(520).getShopId());
            player = player();
            click(player, 5).execute();
            require(player.getOpenInterfaceId() == 3824 && !player.isActionLocked(), "Trade entered thieving");
            player = player();
            click(player, 3).execute();
            require(player.isActionLocked() && player.getCurrentShopId() != shop.getInt(man.getDefinition()),
                    "Pickpocket opened the NPC's shop");
        } finally {
            actions.set(man.getDefinition(), original);
            shop.setInt(man.getDefinition(), originalShop);
        }
    }

    private static void checkFishing() throws Exception {
        Player player = player();
        Npc spot = npc(316);
        terrain[(X + 1) & 63][Y & 63] = 0x200000; // Water cannot be walked onto.
        require(!com.rs2.util.GameUtil.hasClearPath(player.getPosition(), spot.getPosition(), true),
                "Water fixture was walkable");
        FishingSpotManager.activeSpotsByPosition.put(spot.getPosition(), spot);
        player.getSkillManager().getCurrentLevels()[10] = 99;
        player.getInventoryManager().getContainer().setItem(0, new ItemStack(307));
        player.getInventoryManager().getContainer().setItem(1, new ItemStack(313, 10));
        click(player, 3).execute();
        require(player.getActiveCycleEvent() instanceof FishingTask
                && player.getUpdateState().getAnimationId() == FishingSpotDefinition.BAIT.getAnimationId(),
                "Bait click did not start rod fishing");
        Player net = player();
        net.getInventoryManager().getContainer().setItem(0, new ItemStack(303));
        click(net, 1).execute();
        require(net.getActiveCycleEvent() instanceof FishingTask
                && net.getUpdateState().getAnimationId() == FishingSpotDefinition.SMALL_NET.getAnimationId(),
                "Net click stopped working");
        terrain[(X + 1) & 63][Y & 63] = 0;
        require(!net.getFishingHandler().handleFishingSpot(npc(520), 1), "Non-fishing NPC was handled as a fishing spot");
        FishingSpotManager.activeSpotsByPosition.clear();
        player = player();
        npc(323); // Regional Net/Bait variant, absent from the old canonical spot table.
        player.getSkillManager().getCurrentLevels()[10] = 99;
        player.getInventoryManager().getContainer().setItem(0, new ItemStack(307));
        player.getInventoryManager().getContainer().setItem(1, new ItemStack(313, 10));
        click(player, 3).execute();
        require(player.getActiveCycleEvent() instanceof FishingTask, "Regional fishing variant was not routed");
    }

    private static void checkTeleport() throws Exception {
        Player player = player();
        npc(553);
        player.setQuestState(14, 0);
        click(player, 4).execute();
        require(!player.isActionLocked(), "Essence teleport bypassed the quest gate");
        player.setQuestState(14, 1);
        click(player, 4).execute();
        require(player.isActionLocked() && player.getAbyssMageNpcId() == 553, "Aubury teleport was not invoked");
    }

    private static void checkTanning() throws Exception {
        Player player = player();
        npc(804);
        player.getInventoryManager().getContainer().setItem(0, new ItemStack(1739));
        player.getInventoryManager().getContainer().setItem(1, new ItemStack(1739));
        player.getInventoryManager().getContainer().setItem(2, new ItemStack(995, 10));
        click(player, 3).execute();
        require("tanning".equals(player.interfaceAction)
                && InterfaceBridge.translateGroup(player.getOpenInterfaceId(), "test") == 324,
                "Tanner Trade did not open native tanning");
        require(Interfaces.forId(324, 148).actionType == 1, "Tan-1 cache fixture changed");
        require(InterfaceBridge.toLegacyComponent(324 << 16 | 148) == 14817, "Tan-1 button is not mapped");
        ByteBuffer button = ByteBuffer.allocate(4).putInt(324 << 16 | 148);
        button.flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(button)));
        require(player.getInventoryManager().getItemAmount(1741) == 1
                && player.getInventoryManager().getItemAmount(1739) == 1
                && player.getInventoryManager().getItemAmount(995) == 9,
                "Tanning did not exchange one hide and one coin");
        // Tan-all must not remove unaffordable hides or grant free leather.
        player.getInventoryManager().getContainer().setItem(2, new ItemStack(995, 1));
        click(player, 3).execute();
        GameplayHelper.tanHide(player, 1, 3, 1739, 1743);
        require(player.getInventoryManager().getItemAmount(1743) == 0
                && player.getInventoryManager().getItemAmount(1739) == 1
                && player.getInventoryManager().getItemAmount(995) == 1,
                "Unaffordable tanning changed inventory");
    }

    private static void checkCancellationAndReach() throws Exception {
        Player player = player();
        Npc shopkeeper = npc(520);
        TickTask cancelled = click(player, 3);
        player.nextActionSequence();
        cancelled.execute();
        require(!cancelled.isActive() && player.getOpenInterfaceId() != 3824, "Cancelled click opened a shop");
        player.setPosition(new Position(X - 5, Y, 0));
        TickTask pending = click(player, 3);
        pending.execute();
        require(pending.isActive() && player.getOpenInterfaceId() != 3824, "Distant NPC interaction executed early");
        player.setPosition(new Position(X, Y, 0));
        pending.execute();
        require(!pending.isActive() && player.getOpenInterfaceId() == 3824, "Pending click did not finish on arrival");
        player = player();
        TickTask stale = click(player, 3);
        World.getNpcs()[1] = null;
        stale.execute();
        require(!stale.isActive() && player.getOpenInterfaceId() != 3824, "Despawned NPC interaction executed");
        World.getNpcs()[1] = shopkeeper;
        player = player();
        TickTask otherFloor = click(player, 3);
        player.setPosition(new Position(X, Y, 1));
        otherFloor.execute();
        require(!otherFloor.isActive() && player.getOpenInterfaceId() != 3824, "NPC on another floor was interacted with");
        player = player();
        Player owner = player();
        World.getPlayers()[2] = owner;
        shopkeeper.setOwnerPlayerIndex(2);
        click(player, 3).execute();
        require(player.getOpenInterfaceId() != 3824, "Another player's NPC was traded with");
        shopkeeper.setOwnerPlayerIndex(-1);
        World.getPlayers()[2] = null;
    }

    private static void checkShopRestrictions() throws Exception {
        Player player = player();
        Npc keeper = npc(520);
        ShopDefinition shop = (ShopDefinition) ShopManager.getShopDefinitions().get(keeper.getDefinition().getShopId());
        boolean original = shop.isMembersOnly();
        try {
            shop.setMembersOnly(true);
            ServerSettings.membershipRequirementMode = 0;
            click(player, 3).execute();
            require(player.getOpenInterfaceId() != 3824, "Members shop opened for a non-member");
            ServerSettings.membershipRequirementMode = 1;
            ServerSettings.freeToPlayWorld = true;
            click(player, 3).execute();
            require(player.getOpenInterfaceId() != 3824, "Members shop opened on a free world");
        } finally {
            shop.setMembersOnly(original);
            ServerSettings.membershipRequirementMode = 1;
            ServerSettings.freeToPlayWorld = false;
        }
    }

    private static void checkOtherBindings() throws Exception {
        require(ObjectActionRouter.semanticRoute(ObjectDefinition.forId(2213), 1) == InteractionType.SECOND_OBJECT,
                "Bank booth quick-use binding missing");
        require(ObjectActionRouter.semanticRoute(ObjectDefinition.forId(1276), 0) == InteractionType.FIRST_OBJECT,
                "Tree binding missing");
        ItemDefinition sword = ItemDefinition.forId(1277);
        require("Wield".equalsIgnoreCase(sword.getInventoryAction(1)), "Item actions were not loaded");
        Field actions = ItemDefinition.class.getDeclaredField("inventoryActions");
        actions.setAccessible(true);
        String[] original = (String[]) actions.get(sword);
        try {
            actions.set(sword, new String[]{null, null, "Wield", null, "Drop"});
            require(ItemActionRouter.semanticOption(sword, 3) == 2, "Wield depended on its cache slot");
            Player player = player();
            player.getInventoryManager().getContainer().setItem(0, new ItemStack(1277));
            ByteBuffer payload = ByteBuffer.allocate(8);
            payload.putShort((short)128); // inventory slot 0, ADD short
            payload.put(new byte[]{0, 0, 0, (byte)149}); // inventory 149:0, middle-endian int
            payload.put((byte)1277).put((byte)(1277 >>> 8));
            payload.flip();
            new ItemActionPacketHandler().handle(player, new IncomingPacket(ClientPackets.ITEM_OPTION_3,
                    8, PacketBuffer.wrapReader(payload)));
            require(player.getEquipmentManager().getItemIdAtSlot(3) == 1277
                    && player.getInventoryManager().getItemAmount(1277) == 0,
                    "Wield packet did not equip the sword from the shifted cache slot");
        } finally { actions.set(sword, original); }
        for (int group : new int[]{12, 149, 300, 301, 324}) require(!Interfaces.group(group).isEmpty(), "Native UI group missing: " + group);
    }

    private static void checkShearingAndRewards() throws Exception {
        Player player = player();
        npc(43);
        click(player, 1).execute();
        require(!player.isActionLocked(), "Shearing did not require shears");
        player.getInventoryManager().getContainer().setItem(0, new ItemStack(1735));
        click(player, 1).execute();
        require(player.isActionLocked() && player.getUpdateState().getAnimationId() == 894,
                "Shear click did not start the existing wool gathering operation");
        player = player();
        npc(3103);
        click(player, 4).execute();
        require(player.getOpenInterfaceId() == 15944, "Rewards Guardian Trade-with did not open rewards");
        player = player();
        npc(960);
        player.getSkillManager().getCurrentLevels()[3] = 1;
        click(player, 3).execute();
        require(player.getSkillManager().getCurrentLevels()[3] > 1, "Heal action did not restore hitpoints");
    }

    private static Player player() throws Exception {
        Player player = new Player(null);
        player.setSize(1); player.setIndex(1); player.setEncodedIndex(32769);
        player.setPosition(new Position(X, Y, 0)); player.setQuestState(0, 1);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        SocketChannel socket = SocketChannel.open(); socket.close();
        Field transport = Player.class.getDeclaredField("socketChannel");
        transport.setAccessible(true); transport.set(player, socket);
        player.setOpenInterfaceId(-1); player.setCurrentShopId(-1);
        return player;
    }

    private static Npc npc(int id) {
        Npc npc = new Npc(id);
        npc.setIndex(1); npc.setEncodedIndex(1); npc.setSize(1);
        npc.setFacingDirection(6);
        npc.setPosition(new Position(X + 1, Y, 0));
        World.getNpcs()[1] = npc;
        return npc;
    }

    private static TickTask click(Player player, int option) {
        World.getTaskScheduler().getTasks().clear();
        handler.handle(player, npcPacket(option));
        require(World.getTaskScheduler().getTasks().size() == 1, "Click did not schedule exactly one task: " + option);
        return (TickTask) World.getTaskScheduler().getTasks().get(0);
    }

    private static IncomingPacket npcPacket(int option) {
        int[] opcodes = {0, ClientPackets.NPC_OPTION_1, ClientPackets.NPC_OPTION_2,
                ClientPackets.NPC_OPTION_3, ClientPackets.NPC_OPTION_4, ClientPackets.NPC_OPTION_5};
        byte[] data = option == 3 || option == 4 ? new byte[]{(byte)129, 0} : new byte[]{0, 1};
        return new IncomingPacket(opcodes[option], 2, PacketBuffer.wrapReader(ByteBuffer.wrap(data)));
    }

    private static void openTerrain() throws Exception {
        Constructor<WalkingCollisionMap> constructor = WalkingCollisionMap.class.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        int regionId = (X >> 6 << 8) | (Y >> 6);
        WalkingCollisionMap region = constructor.newInstance(regionId);
        Field flags = WalkingCollisionMap.class.getDeclaredField("tileFlags"); flags.setAccessible(true);
        terrain = new int[64][64];
        ((int[][][]) flags.get(region))[0] = terrain;
        Field lookup = WalkingCollisionMap.class.getDeclaredField("regionLookup"); lookup.setAccessible(true);
        ((WalkingCollisionMap[]) lookup.get(null))[regionId] = region;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
