import com.rs2.ServerSettings;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.gameplay.castlewars.CastleWarsEngineeringManager;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.player.ModernPlayerUpdateTask;
import com.rs2.model.player.Player;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * End-to-end regression for 443 height changes in Saradomin castle.
 * The supplies already exist on the server; the client must receive a
 * new-floor ground item refresh after descending from its upstairs spawn.
 */
public final class CastleWarsSupplyFloorRefreshChecks {
    private static final int BUCKET = CastleWarsEngineeringManager.EMPTY_BUCKET_ITEM_ID;
    private static final int TINDERBOX = CastleWarsEngineeringManager.TINDERBOX_ITEM_ID;

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        ItemDefinition.loadDefinitions();
        Method reset = CastleWarsEngineeringManager.class.getDeclaredMethod("resetBucketSupplies");
        Method clear = CastleWarsEngineeringManager.class.getDeclaredMethod("clearBucketSupplies");
        reset.setAccessible(true);
        clear.setAccessible(true);

        try (ServerSocketChannel server = ServerSocketChannel.open();
             Socket socketClient = new Socket()) {
            server.bind(new InetSocketAddress("127.0.0.1", 0));
            socketClient.connect(server.getLocalAddress());
            try (SocketChannel channel = server.accept()) {
                Player player = new Player(null);
                Field socket = Player.class.getDeclaredField("socketChannel");
                socket.setAccessible(true);
                socket.set(player, channel);
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setIndex(71);
                player.setEncodedIndex(32768 + 71);
                World.players[71] = player;
                // Stable map region: no map rebuilding is needed to change
                // floors, which is the case the 443 updater used to miss.
                player.getLastKnownRegionPosition().set(new Position(2384, 3024, 0));
                player.setPosition(new Position(2429, 3077, 2));
                player.refreshLocalViewArea();
                try {
                    reset.invoke(null);
                    require(player.getVisibleGroundItems().isEmpty(),
                            "Ground-floor supplies should not be visible upstairs");
                    moveAndUpdate(player, new Position(2429, 3077, 0));
                    assertSupply(player, BUCKET, 2431, 3076, 0);
                    assertSupply(player, BUCKET, 2428, 3079, 0);
                    assertSupply(player, TINDERBOX, 2431, 3072, 0);

                    moveAndUpdate(player, new Position(2423, 3080, 1));
                    assertSupply(player, TINDERBOX, 2423, 3080, 1);
                    require(GroundItemManager.findVisibleItem(player, BUCKET,
                                    new Position(2431, 3076, 0)) == null,
                            "Ground-floor bucket leaked into floor 1");

                    moveAndUpdate(player, new Position(2429, 3077, 0));
                    assertSupply(player, BUCKET, 2431, 3076, 0);
                    assertSupply(player, BUCKET, 2428, 3079, 0);
                    assertSupply(player, TINDERBOX, 2431, 3072, 0);

                    moveAndUpdate(player, new Position(2429, 3077, 2));
                    require(player.getVisibleGroundItems().isEmpty(),
                            "Supplies must not display inside the upstairs respawn room");
                    System.out.println("Castle Wars 443 Saradomin floor-change supply refresh passed");
                } finally {
                    clear.invoke(null);
                    World.players[71] = null;
                    player.setIndex(-1);
                }
            }
        }
    }

    private static void moveAndUpdate(Player player, Position destination) throws Exception {
        player.moveTo(destination);
        player.refreshLocalViewArea();
        ModernPlayerUpdateTask.updatePlayer(player);
        Field pending = Player.class.getDeclaredField("planeChangeRefreshPending");
        pending.setAccessible(true);
        require(!pending.getBoolean(player),
                "443 updater did not clear pending floor-change scene refresh");
    }

    private static void assertSupply(Player player, int id, int x, int y, int plane) {
        require(GroundItemManager.findVisibleItem(player, id, new Position(x, y, plane)) != null,
                "443 floor-change refresh missed supply " + id + " at "
                        + x + "," + y + "," + plane);
    }

    private static void require(boolean ok, String reason) {
        if (!ok) throw new AssertionError(reason);
    }
}
