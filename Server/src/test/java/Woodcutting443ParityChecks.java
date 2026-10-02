import com.rs2.ServerSettings;
import com.rs2.cache.InterfaceDefinition;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.objects.WorldObjectLookup;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.skill.GatheringToolDefinition;
import com.rs2.model.skill.ItemCombinationHandler;
import com.rs2.model.skill.woodcutting.JungleCutting;
import com.rs2.model.skill.woodcutting.JungleDefinition;
import com.rs2.model.skill.woodcutting.TreeDefinition;
import com.rs2.model.skill.woodcutting.WoodcuttingChanceTable;
import com.rs2.model.skill.woodcutting.WoodcuttingHandler;
import com.rs2.model.skill.woodcutting.WoodcuttingTask;
import com.rs2.net.IsaacCipher;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;

/** Regression checks for revision-443 Woodcutting parity fixes. */
public final class Woodcutting443ParityChecks {
    private static Field socket;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.woodcuttingEnabled = true;
        ServerSettings.randomEventsMode = 1;
        QuestDefinition.loadDefinitions();
        InterfaceDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);

        checkChanceMarkers();
        checkF2pDragonAxeBlock();
        checkEntBypassesTreeRequirements();
        checkJungleDefinitionsAndStart();

        System.out.println("Woodcutting 443 parity checks passed (markers, F2P axe, Ent bypass, jungle metadata/start timing).");
        System.exit(0);
    }

    private static void checkChanceMarkers() throws Exception {
        Method low = WoodcuttingChanceTable.class.getDeclaredMethod("low",
                TreeDefinition.class, GatheringToolDefinition.class);
        Method high = WoodcuttingChanceTable.class.getDeclaredMethod("high",
                TreeDefinition.class, GatheringToolDefinition.class);
        low.setAccessible(true);
        high.setAccessible(true);
        require((Integer) low.invoke(null, TreeDefinition.MAPLE, GatheringToolDefinition.RUNE_AXE) == 28,
                "Rune/maple low marker drifted");
        require((Integer) high.invoke(null, TreeDefinition.MAPLE, GatheringToolDefinition.RUNE_AXE) == 87,
                "Rune/maple high marker drifted");
        require((Integer) high.invoke(null, TreeDefinition.YEW, GatheringToolDefinition.RUNE_AXE) == 44,
                "Rune/yew high marker still uses generic multiplier");
        require(TreeDefinition.TREE.getRespawnTicksLow() == 50 && TreeDefinition.TREE.getRespawnTicksHigh() == 100,
                "Normal-tree respawn is not 30-60 seconds worth of base ticks");
    }

    private static void checkF2pDragonAxeBlock() throws Exception {
        boolean oldF2p = ServerSettings.freeToPlayWorld;
        int oldMembershipMode = ServerSettings.membershipRequirementMode;
        try {
            ServerSettings.freeToPlayWorld = true;
            ServerSettings.membershipRequirementMode = 1;
            Player player = player();
            player.getSkillManager().getCurrentLevels()[8] = 99;
            player.getInventoryManager().addItem(new ItemStack(6739));
            player.getInventoryManager().addItem(new ItemStack(1359));
            GatheringToolDefinition tool = ItemCombinationHandler.findUsableGatheringTool(player, 8);
            require(tool == GatheringToolDefinition.RUNE_AXE,
                    "F2P Woodcutting selected the members-only Dragon axe");
        } finally {
            ServerSettings.freeToPlayWorld = oldF2p;
            ServerSettings.membershipRequirementMode = oldMembershipMode;
        }
    }

    private static void checkEntBypassesTreeRequirements() throws Exception {
        boolean oldF2p = ServerSettings.freeToPlayWorld;
        try {
            ServerSettings.freeToPlayWorld = false;
            Player player = player();
            player.getSkillManager().getCurrentLevels()[8] = 1;
            player.getEquipmentManager().setSlotItem(1351, 3);
            while (player.getInventoryManager().getContainer().getFreeSlots() > 0) {
                player.getInventoryManager().addItem(new ItemStack(1511));
                player.getOutboundBuffer().clear();
            }
            Npc ent = new Npc(1740);
            ent.moveTo(new Position(3201, 3200, 0));
            World.registerNpc(ent);
            try {
                WoodcuttingHandler.startWoodcutting(player, 1740, 3201, 3200, true);
                require(player.getActiveCycleEvent() instanceof WoodcuttingTask,
                        "Ent incorrectly enforced Yew level or inventory-space requirements");
            } finally {
                World.unregisterNpc(ent);
            }
        } finally {
            ServerSettings.freeToPlayWorld = oldF2p;
        }
    }

    private static void checkJungleDefinitionsAndStart() throws Exception {
        require(JungleDefinition.forObjectId(9010) == JungleDefinition.LIGHT, "Light jungle base missing");
        require(JungleDefinition.forObjectId(9014) == JungleDefinition.LIGHT
                && JungleDefinition.LIGHT.isDepleted(9014), "Light jungle empty stage missing");
        require(JungleDefinition.forObjectId(9015) == JungleDefinition.MEDIUM, "Medium jungle missing");
        require(JungleDefinition.forObjectId(9020) == JungleDefinition.DENSE, "Dense jungle missing");
        require(JungleDefinition.LIGHT.getRequiredLevel() == 10 && JungleDefinition.LIGHT.getExperience() == 32.0,
                "Light jungle period values wrong");
        require(JungleDefinition.MEDIUM.getRequiredLevel() == 20 && JungleDefinition.MEDIUM.getExperience() == 55.0,
                "Medium jungle period values wrong");
        require(JungleDefinition.DENSE.getRequiredLevel() == 30 && JungleDefinition.DENSE.getExperience() == 81.0,
                "Dense jungle period values wrong");

        boolean oldF2p = ServerSettings.freeToPlayWorld;
        int oldMembershipMode = ServerSettings.membershipRequirementMode;
        try {
            ServerSettings.freeToPlayWorld = false;
            ServerSettings.membershipRequirementMode = 1;
            Player player = player();
            player.setQuestState(56, 1);
            require(!JungleCutting.hasStarted(player),
                    "Cleanup unexpectedly started before speaking to Murcaily");
            require(JungleCutting.handleMurcaily(player, 2529) && JungleCutting.hasStarted(player),
                    "Murcaily did not activate Tai Bwo Wannai Cleanup");
            player.getSkillManager().getCurrentLevels()[8] = 10;
            player.getEquipmentManager().setSlotItem(975, 3);
            JungleCutting.start(player, 9010, 3201, 3200);
            require(player.getActiveCycleEvent() instanceof JungleCutting,
                    "Valid 443 light-jungle cut did not start");
            Field cycleTicks = JungleCutting.class.getDeclaredField("cycleTicks");
            cycleTicks.setAccessible(true);
            require((Integer) cycleTicks.get(player.getActiveCycleEvent()) == 16,
                    "Level-10 jungle cycle is not 16 ticks");

            Player highLevel = player();
            highLevel.setQuestState(56, 1);
            require(JungleCutting.handleMurcaily(highLevel, 2530) && JungleCutting.hasStarted(highLevel),
                    "Alternate Murcaily did not activate Cleanup");
            highLevel.getSkillManager().getCurrentLevels()[8] = 99;
            highLevel.getEquipmentManager().setSlotItem(6317, 3);
            JungleCutting.start(highLevel, 9020, 3201, 3200);
            require(highLevel.getActiveCycleEvent() instanceof JungleCutting,
                    "Valid dense-jungle cut did not start");
            require((Integer) cycleTicks.get(highLevel.getActiveCycleEvent()) == 8,
                    "Level-99 jungle cycle is not capped at 8 ticks");
        } finally {
            ServerSettings.freeToPlayWorld = oldF2p;
            ServerSettings.membershipRequirementMode = oldMembershipMode;
        }
    }

    private static Player player() throws Exception {
        Player player = new Player(null);
        SocketChannel transport = SocketChannel.open();
        transport.close();
        socket.set(player, transport);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setIndex(1);
        player.setEncodedIndex(32769);
        player.setPosition(new Position(3200, 3200, 0));
        player.refreshLocalViewArea();
        player.setQuestState(0, 1);
        return player;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
