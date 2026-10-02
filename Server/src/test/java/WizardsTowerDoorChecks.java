import com.rs2.model.objects.*;
import com.rs2.model.objects.functions.DoorHandler;
import com.rs2.model.player.Player;
import com.rs2.util.path.WalkingCollisionMap;
import com.rs2.util.path.ProjectileCollisionMap;

/** Cache-backed collision regression; run from Server. */
public final class WizardsTowerDoorChecks {
    public static void main(String[] args) throws Exception {
        WorldObjectLookup.loadWorldObjects();
        WalkingCollisionMap.loadCollisionMaps();
        ProjectileCollisionMap.loadCollisionMaps();
        checkDoor(3107, 3162, 9, 1, 3108, 3162, 2);
        checkDoor(3109, 3167, 0, 3, 3109, 3166, 0);
        checkOccupiedDoor();
        System.out.println("Wizards' Tower door checks passed.");
    }

    private static void checkOccupiedDoor() throws Exception {
        java.lang.reflect.Field states = DoorHandler.class.getDeclaredField("doorStates");
        states.setAccessible(true);
        ((java.util.List<?>) states.get(null)).clear();
        Player player = new Player(null);
        player.setOutboundCipher(new com.rs2.net.IsaacCipher(new int[4]));
        java.nio.channels.SocketChannel transport = java.nio.channels.SocketChannel.open();
        transport.close();
        java.lang.reflect.Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        socket.set(player, transport);
        player.setPosition(new com.rs2.model.Position(3107, 3162, 0));
        com.rs2.model.World.getPlayers()[1] = player;
        try {
            DoorHandler.handleDoor(player, 11993, 3107, 3162, 0);
            DoorHandler.handleDoor(player, 11994, 3108, 3162, 0);
            require(ObjectManager.findDynamicObjectByIdAt(11994, 3108, 3162, 0) != null,
                    "Diagonal door closed on a player standing in its doorway");
            player.setPosition(new com.rs2.model.Position(3107, 3163, 0));
            Player other = new Player(null);
            other.setPosition(new com.rs2.model.Position(3107, 3162, 0));
            com.rs2.model.World.getPlayers()[2] = other;
            DoorHandler.handleDoor(player, 11994, 3108, 3162, 0);
            require(ObjectManager.findDynamicObjectByIdAt(11994, 3108, 3162, 0) != null,
                    "Diagonal door closed on another player");
            com.rs2.model.World.getPlayers()[2] = null;
            DoorHandler.handleDoor(player, 11994, 3108, 3162, 0);
            require(ObjectManager.findDynamicObjectByIdAt(11993, 3107, 3162, 0) != null,
                    "Door did not close after the player left");
        } finally {
            com.rs2.model.World.getPlayers()[1] = null;
            com.rs2.model.World.getPlayers()[2] = null;
        }
    }

    private static void checkDoor(int x, int y, int type, int orientation,
                                  int openX, int openY, int openOrientation) throws Exception {
        LoadedWorldObject original = WorldObjectLookup.findObjectByIdAt(11993, x, y, 0);
        require(original != null && original.getType() == type
                && original.getOrientation() == orientation, "Unexpected cached door placement");
        Player player = new Player(null);
        player.setOutboundCipher(new com.rs2.net.IsaacCipher(new int[4]));
        java.nio.channels.SocketChannel transport = java.nio.channels.SocketChannel.open();
        transport.close();
        java.lang.reflect.Field socket = Player.class.getDeclaredField("socketChannel");
        socket.setAccessible(true);
        socket.set(player, transport);
        for (int cycle = 0; cycle < 2; cycle++) {
            require(DoorHandler.handleDoor(player, 11993, x, y, 0), "Door did not open");
            DynamicObject open = ObjectManager.findDynamicObjectByIdAt(11994, openX, openY, 0);
            require(open != null && open.getWorldObject().getOrientation() == openOrientation,
                    "Incorrect open door placement");
            checkCollision(openX, openY, type, openOrientation);
            if (type == 9) {
                require((WalkingCollisionMap.getTileFlags(x, y, 0) & 256) == 0,
                        "Opening did not clear the old door tile");
            }
            require(DoorHandler.handleDoor(player, 11994, openX, openY, 0), "Door did not close");
            checkCollision(x, y, type, orientation);
            if (type == 9) {
                require((WalkingCollisionMap.getTileFlags(openX, openY, 0) & 256) == 0,
                        "Closing left collision at the open door tile");
            }
        }
    }

    private static void checkCollision(int x, int y, int type, int orientation) {
        if (type == 9) {
            require((WalkingCollisionMap.getTileFlags(x, y, 0) & 256) != 0,
                    "Visible diagonal door has no collision at " + x + "," + y);
            require(!WalkingCollisionMap.canTravelBetween(x, y + 1, x, y, 0, 1, 1),
                    "Player can walk into the diagonal door");
        } else {
            int[] flags = {128, 2, 8, 32};
            int[] dx = {-1, 0, 1, 0};
            int[] dy = {0, 1, 0, -1};
            require((WalkingCollisionMap.getTileFlags(x, y, 0) & flags[orientation]) != 0,
                    "Visible straight door has no wall collision");
            require(!WalkingCollisionMap.canTravelBetween(x, y, x + dx[orientation],
                    y + dy[orientation], 0, 1, 1), "Player can cross the door edge");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
