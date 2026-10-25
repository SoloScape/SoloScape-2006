import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.model.skill.fletching.logs.LogFletchingMenu;
import com.rs2.model.skill.fletching.logs.LogFletchingTask;
import com.rs2.model.task.CycleEventContainer;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.InterfaceInputPacketHandler;
import com.rs2.net.packet.handler.ItemActionPacketHandler;
import java.nio.ByteBuffer;

/** Knife-on-logs must open 443 model choices with per-item Make X/10/5/1. */
public final class LogFletchingChecks {
    private static final int[] MODELS = {101, 89, 108};
    private static final int[] LABELS = {102, 90, 109};
    private static final int[] ROOTS = {96, 91, 103};

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        ServerSettings.membershipRequirementMode = 0;
        ServerSettings.freeToPlayWorld = false;
        ItemDefinition.loadDefinitions();
        Interfaces.load();

        require(InterfaceBridge.translateGroup(8880) == 459, "Wrong native production group");
        require(Interfaces.group(459).size() == 127, "Production widget changed");
        for (int i = 0; i < 3; i++) {
            require(Interfaces.forId(459, MODELS[i]).type == 6, "Product item model missing");
            require(Interfaces.forId(459, LABELS[i]).type == 4, "Product label missing");
            require(Interfaces.forId(459, ROOTS[i]).type == 0, "Make-X choice root missing");
            require(InterfaceBridge.translate(20100 + i) == (459 << 16 | MODELS[i]),
                    "Wrong product model bridge");
            require(InterfaceBridge.translate(20103 + i) == (459 << 16 | LABELS[i]),
                    "Wrong product label bridge");
            for (int kind = 0; kind < 4; kind++) {
                int legacy = 8886 + i * 4 + kind;
                int packed = InterfaceBridge.translate(legacy);
                require((packed >>> 16) == 459, "Native make action not mapped");
                require(Interfaces.forPackedId(packed).actionType == 1,
                        "Make-X/10/5/1 not an interactive native widget");
            }
        }

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
                requireNativeMenu(player);
                double xp = player.getSkillManager().getExperience()[9];
                click(player, recipe[1], 3); // Make 1, for every product
                require(player.getActiveCycleEvent() instanceof LogFletchingTask, "No fletching task");
                tick(player);
                require(player.getInventoryManager().getItemAmount(recipe[0]) == 2, "Log not consumed");
                require(player.getInventoryManager().getItemAmount(recipe[2]) == (recipe[2] == 52 ? 15 : 1),
                        "Wrong product for log " + recipe[0] + " choice " + recipe[1]);
                require(player.getInventoryManager().containsItem(946), "Knife consumed");
                require(player.getSkillManager().getExperience()[9] > xp, "No fletching XP");
            }
        }

        // Make 1, 5, 10, and X all use the same menu and a specific item slot.
        for (int kind = 0; kind < 4; kind++) {
            for (int product = 0; product < 3; product++) {
                Player player = newPlayer(1511, 12);
                useKnife(player, false);
                requireNativeMenu(player);
                click(player, product, kind);
                int count = new int[]{7, 10, 5, 1}[kind];
                if (kind == 0) {
                    require(player.getSelectedInterfaceId() == 8886 + product * 4,
                            "Make-X did not request the selected product's quantity");
                    enterAmount(player, count);
                }
                require(player.getActiveCycleEvent() instanceof LogFletchingTask, "No quantity task");
                for (int tick = 0; tick < 34; tick++) {
                    if (player.getActiveCycleEvent() == null) break;
                    tick(player);
                }
                int item = new int[]{52, 50, 48}[product];
                require(player.getInventoryManager().getItemAmount(item) ==
                        (item == 52 ? count * 15 : count),
                        "Wrong quantity: product " + product + " kind " + kind);
            }
        }

        Player player = newPlayer(1511, 2);
        useKnife(player, false);
        click(player, 1, 3); // Shortbow needs Fletching 5.
        require(player.getActiveCycleEvent() instanceof LogFletchingTask,
                "Valid fletching failed");
        player = newPlayer(1511, 2);
        useKnife(player, false);
        player.getSkillManager().getCurrentLevels()[9] = 1;
        click(player, 1, 3);
        require(player.getActiveCycleEvent() == null, "Low level accepted");

        player = newPlayer(1513, 2);
        useKnife(player, false);
        player.getSkillManager().getCurrentLevels()[9] = 79;
        click(player, 0, 2);
        require(player.getActiveCycleEvent() == null, "Magic batch bypassed level 80");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        player.getInventoryManager().removeItem(new ItemStack(946));
        click(player, 0, 3);
        require(player.getActiveCycleEvent() == null, "Missing knife accepted");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        player.packetSender.closeInterfaces();
        click(player, 0, 3);
        require(player.getActiveCycleEvent() == null, "Closed menu accepted");

        player = newPlayer(1511, 2);
        useKnife(player, false);
        ServerSettings.fletchingEnabled = false;
        click(player, 0, 3);
        require(player.getActiveCycleEvent() == null, "Disabled skill accepted");
        ServerSettings.fletchingEnabled = true;

        // The right-most slot must not be craftable on two-product log types.
        player = newPlayer(1521, 2);
        useKnife(player, false);
        click(player, 2, 3);
        require(player.getActiveCycleEvent() == null, "Hidden product crafted");

        // Make-X must not restart from a consumed or invalid quantity prompt.
        player = newPlayer(1511, 2);
        useKnife(player, false);
        click(player, 2, 0);
        enterAmount(player, 0);
        require(player.getActiveCycleEvent() == null, "Zero Make-X quantity accepted");

        player = newPlayer(1511, 2);
        LogFletchingMenu.restoreSpinningInterface(player);
        require(player.getOpenInterfaceId() == 8880 && "spinning".equals(player.interfaceAction),
                "Spinning screen was not restored");
        require(!LogFletchingMenu.handleButton(player, 8889),
                "Spinning Make 1 intercepted by fletching");
        player.setInterfaceAction("unrelated");
        require(!LogFletchingMenu.handleButton(player, 8889),
                "Unrelated production action intercepted");
        ServerSettings.clientBuild = 377;
        require(!LogFletchingMenu.open(player, "normalCutting"), "Legacy build intercepted");

        System.out.println("Log fletching checks passed: 443 item models/labels, every recipe, "
                + "per-product Make X/10/5/1, guards and spinning isolation.");
    }

    private static Player newPlayer(int log, int count) {
        Player player = new Player(null);
        player.isBot = true; // Avoid network packets in this cache-backed fixture.
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

    private static void requireNativeMenu(Player player) {
        require(player.getOpenInterfaceId() == 8880, "Knife did not open native item-model menu");
        require(InterfaceBridge.translateGroup(player.getOpenInterfaceId()) == 459,
                "Menu did not map to native 459");
    }

    private static void click(Player player, int product, int kind) {
        int legacyButton = 8886 + product * 4 + kind; // X/10/5/1
        int packed = InterfaceBridge.translate(legacyButton);
        require(packed != InterfaceBridge.UNMAPPED, "Unmapped quantity action");
        ByteBuffer bytes = ByteBuffer.allocate(4).putInt(packed);
        bytes.flip();
        new InterfaceActionPacketHandler().handle(player,
                new IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4, PacketBuffer.wrapReader(bytes)));
    }

    private static void enterAmount(Player player, int amount) {
        ByteBuffer bytes = ByteBuffer.allocate(4).putInt(amount);
        bytes.flip();
        new InterfaceInputPacketHandler().handle(player,
                new IncomingPacket(208, 4, PacketBuffer.wrapReader(bytes)));
    }

    private static void tick(Player player) {
        require(player.getActiveCycleEvent() != null, "Missing task");
        new CycleEventContainer(player, player.getActiveCycleEvent(), 1).execute();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
