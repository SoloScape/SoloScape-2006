import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.interaction.FirstNpcActionTask;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.npc.Npc;
import com.rs2.model.npc.NpcDefinition;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.model.shop.ShopManager;
import com.rs2.model.task.TickTask;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.NpcInteractionPacketHandler;
import com.rs2.util.path.WalkingCollisionMap;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/** Real cache Trade clicks must open the configured shop, including rapid repeat clicks. */
public final class ShopkeeperTradeChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        NpcDefinition.loadDefinitions();
        ShopManager.loadShops();
        Constructor<WalkingCollisionMap> ctor = WalkingCollisionMap.class.getDeclaredConstructor(int.class);
        ctor.setAccessible(true);
        int regionId = (3220 >> 6 << 8) | (3220 >> 6);
        WalkingCollisionMap region = ctor.newInstance(regionId);
        Field flags = WalkingCollisionMap.class.getDeclaredField("tileFlags");
        flags.setAccessible(true);
        ((int[][][]) flags.get(region))[0] = new int[64][64];
        Field lookup = WalkingCollisionMap.class.getDeclaredField("regionLookup");
        lookup.setAccessible(true);
        ((WalkingCollisionMap[]) lookup.get(null))[regionId] = region;
        Player player = new Player(null);
        player.setSize(1);
        player.setEncodedIndex(32769);
        player.setPosition(new Position(3220, 3220, 0));
        player.setQuestState(0, 1);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        SocketChannel socket = SocketChannel.open();
        socket.close();
        Field transport = Player.class.getDeclaredField("socketChannel");
        transport.setAccessible(true);
        transport.set(player, socket);
        Field overlay = Player.class.getDeclaredField("inventoryOverlayInterfaceId");
        overlay.setAccessible(true);
        NpcInteractionPacketHandler handler = new NpcInteractionPacketHandler();
        for (int id : new int[] {520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 553}) {
            Npc npc = new Npc(id);
            npc.setIndex(1); npc.setEncodedIndex(1); npc.setSize(1);
            npc.setPosition(new Position(3221, 3220, 0));
            World.getNpcs()[1] = npc;
            require(npc.getDefinition().actionStartsWith(2, "trade"), "Fixture Trade slot changed");
            for (int repeat = 0; repeat < 2; repeat++) {
                World.getTaskScheduler().getTasks().clear();
                player.setCurrentShopId(-1);
                player.setOpenInterfaceId(-1);
                // NPC option 3 encodes the index as a little-endian ADD short.
                handler.handle(player, packet(ClientPackets.NPC_OPTION_3, (byte)129, (byte)0));
                TickTask task = (TickTask) World.getTaskScheduler().getTasks().get(0);
                task.execute();
                require(player.getCurrentShopId() == npc.getDefinition().getShopId(), "Trade did not open shop for NPC " + id);
                require(player.getOpenInterfaceId() == 3824 && overlay.getInt(player) == 3822,
                        "Shop and inventory interfaces were not opened");
                require(Boolean.TRUE.equals(player.getAttributes().get("isShopping")), "Shopping state missing");
                require(!task.isActive(), "Trade task did not finish");
            }
            World.getTaskScheduler().getTasks().clear();
            handler.handle(player, packet(ClientPackets.NPC_OPTION_1, (byte)0, (byte)1));
            require(World.getTaskScheduler().getTasks().get(0) instanceof FirstNpcActionTask,
                    "Talk-to was incorrectly routed to trading");
        }
        require(InterfaceBridge.translateGroup(3824, "test") == 300
                && InterfaceBridge.translateGroup(3822, "test") == 301, "Native shop interface mapping changed");
        System.out.println("Shopkeeper trade checks passed (14 NPCs, repeat Trade clicks, shop interfaces, Talk-to routing).");
        System.exit(0);
    }
    private static IncomingPacket packet(int opcode, byte first, byte second) {
        return new IncomingPacket(opcode, 2, PacketBuffer.wrapReader(ByteBuffer.wrap(new byte[]{first, second})));
    }
    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
}
