import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.skill.fletching.logs.LogFletchingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.ItemActionPacketHandler;
import java.nio.ByteBuffer;

/** Issue #5: knife/log packets must reach native menus and produce items. */
public final class LogFletchingChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 0;
        ServerSettings.freeToPlayWorld = false;
        ItemDefinition.loadDefinitions();
        Interfaces.load();
        int[][] recipes = {
            {1511, 0, 52}, {1511, 1, 50}, {1511, 2, 48},
            {1521, 0, 54}, {1521, 1, 56}, {2862, 0, 4825},
            {1519, 0, 60}, {1519, 1, 58}, {1517, 0, 64}, {1517, 1, 62},
            {1515, 0, 68}, {1515, 1, 66}, {1513, 0, 72}, {1513, 1, 70}
        };
        for (int[] recipe : recipes) {
            for (boolean reverse : new boolean[]{false, true}) {
                Player player = newPlayer(recipe[0], 3);
                useKnife(player, reverse);
                selectProduct(player, recipe[0], recipe[1]);
                double xp = player.getSkillManager().getExperience()[9];
                click(player, 2482);
                require(player.getActiveCycleEvent() instanceof LogFletchingTask, "No fletching task");
                tick(player);
                require(player.getInventoryManager().getItemAmount(recipe[0]) == 2, "Log not consumed");
                require(player.getInventoryManager().getItemAmount(recipe[2]) == (recipe[2] == 52 ? 15 : 1),
                        "Wrong product for log " + recipe[0] + " choice " + recipe[1]);
                require(player.getInventoryManager().containsItem(946), "Knife consumed");
                require(player.getSkillManager().getExperience()[9] > xp, "No fletching XP");
            }
        }
        for (int quantityButton = 2482; quantityButton <= 2485; quantityButton++) {
            Player player = newPlayer(1511, 12);
            useKnife(player, false);
            selectProduct(player, 1511, 0);
            click(player, quantityButton);
            int expected = new int[]{1, 5, 10, 12}[quantityButton - 2482];
            for (int i = 0; i < 29; i++) tick(player);
            require(player.getInventoryManager().getItemAmount(52) == 15 * expected, "Wrong quantity");
        }
        Player player = newPlayer(1511, 2);
        useKnife(player, false);
        selectProduct(player, 1511, 1);
        player.getSkillManager().getCurrentLevels()[9] = 1;
        click(player, 2482);
        require(player.getActiveCycleEvent() == null, "Low level accepted");

        player = newPlayer(1513, 2);
        useKnife(player, false);
        selectProduct(player, 1513, 0);
        player.getSkillManager().getCurrentLevels()[9] = 79;
        click(player, 2483);
        require(player.getActiveCycleEvent() == null, "Magic batch bypassed level 80");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        selectProduct(player, 1511, 0);
        player.getInventoryManager().removeItem(new ItemStack(946));
        click(player, 2482);
        require(player.getActiveCycleEvent() == null, "Missing knife accepted");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        selectProduct(player, 1511, 0);
        player.packetSender.closeInterfaces();
        click(player, 2482);
        require(player.getActiveCycleEvent() == null, "Closed menu accepted");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        selectProduct(player, 1511, 0);
        ServerSettings.fletchingEnabled = false;
        click(player, 2482);
        require(player.getActiveCycleEvent() == null, "Disabled skill accepted");
        ServerSettings.fletchingEnabled = true;

        // The shared dialogue route must leave unrelated menus and legacy builds alone.
        player = newPlayer(1511, 2);
        player.setInterfaceAction("spinning");
        require(!com.rs2.model.skill.fletching.logs.LogFletchingMenu.handleButton(player, 2482),
                "Unrelated production action intercepted");
        ServerSettings.clientBuild = 377;
        require(!com.rs2.model.skill.fletching.logs.LogFletchingMenu.open(player, "normalCutting"),
                "Legacy build intercepted");
        System.out.println("Log fletching checks passed: item packets, all products, quantities, XP and guards.");
    }

    private static Player newPlayer(int log, int count) {
        Player player = new Player(null);
        player.isBot = true;
        player.setQuestState(0, 1);
        player.getSkillManager().getCurrentLevels()[9] = 99;
        player.getInventoryManager().addItem(new ItemStack(946));
        player.getInventoryManager().addItem(new ItemStack(log, count));
        return player;
    }

    private static void useKnife(Player player, boolean reverse) {
        int selectedSlot = reverse ? 1 : 0;
        int targetSlot = reverse ? 0 : 1;
        int selectedId = player.getInventoryManager().getContainer().getItemAt(selectedSlot).getId();
        int targetId = player.getInventoryManager().getContainer().getItemAt(targetSlot).getId();
        int widget = 149 << 16;
        ByteBuffer bytes = ByteBuffer.allocate(16);
        bytes.put((byte)(selectedSlot >> 8)).put((byte)(selectedSlot + 128));
        bytes.put((byte)selectedId).put((byte)(selectedId >> 8));
        bytes.put((byte)targetId).put((byte)(targetId >> 8));
        bytes.put((byte)widget).put((byte)(widget >> 8)).put((byte)(widget >> 16)).put((byte)(widget >> 24));
        bytes.put((byte)(widget >> 16)).put((byte)(widget >> 24)).put((byte)widget).put((byte)(widget >> 8));
        bytes.putShort((short)targetSlot).flip();
        new ItemActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.ITEM_ON_ITEM, 16, PacketBuffer.wrapReader(bytes)));
    }

    private static void selectProduct(Player player, int log, int choice) {
        int root = log == 1511 ? 2469 : log == 2862 ? 2480 : 2459;
        require(player.getOpenInterfaceId() == root, "Knife did not open supported menu for " + log);
        require(InterfaceBridge.translateGroup(root) != InterfaceBridge.UNMAPPED, "Unmapped menu");
        if (log != 2862) click(player, (log == 1511 ? 2471 : 2461) + choice);
        require(player.getOpenInterfaceId() == 2480, "No quantity menu");
    }

    private static void click(Player player, int legacyButton) {
        int packed = InterfaceBridge.translate(legacyButton);
        require(packed != InterfaceBridge.UNMAPPED, "Unmapped button");
        ByteBuffer bytes = ByteBuffer.allocate(6);
        bytes.putShort((short)0).putInt(packed).flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.WIDGET_SELECT, 6, PacketBuffer.wrapReader(bytes)));
    }

    private static void tick(Player player) {
        require(player.getActiveCycleEvent() != null, "Missing task");
        new CycleEventContainer(player, player.getActiveCycleEvent(), 1).execute();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
