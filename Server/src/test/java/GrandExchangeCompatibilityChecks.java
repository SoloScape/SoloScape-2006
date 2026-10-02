package unpackaged;

import com.rs2.ServerSettings;
import com.rs2.cache.js5.Definitions;
import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.model.player.GrandExchangeManager;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.ClientPackets;
import com.rs2.net.packet.InterfaceBridge;
import jagex.io.FrameBuffer;
import jagex.io.JSocket;
import jagex.utils.Huffmans;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.world.actors.StillGraphic;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.Map;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import jagex.graphics.DrawingArea;
import jagex.graphics.AbstractImage;
import java.awt.Canvas;

/** Exercises real server exchange packets through the paired client's parser. Run from Server. */
public final class GrandExchangeCompatibilityChecks {
    private static final ByteArrayOutputStream capture = new ByteArrayOutputStream();

    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        // The real client creates this in ClientApplet.method28; the headless QA harness must too.
        Class41.aCanvas778 = new Canvas();
        Class41.aCanvas778.setSize(765, 503);
        com.rs2.model.quest.QuestDefinition.loadDefinitions();
        com.rs2.model.item.ItemDefinition.loadDefinitions();
        com.rs2.model.npc.NpcDefinition.loadDefinitions();
        final Map<Integer, byte[]> npcs = Definitions.readGroup(9);
        final Map<Integer, byte[]> items = Definitions.readGroup(10);
        Varbit.npcFileLoader = new FileTable(false, false) {
            public byte[] lookupFile(int group, int id) { return npcs.get(id); }
        };
        Class31.itemFileLoader = new FileTable(false, false) {
            public byte[] lookupFile(int group, int id) { return items.get(id); }
        };
        final Js5CacheStore assetStore = new Js5CacheStore(new File("cache"));
        Class4.aClass9_71 = new FileTable(false, false) {
            public byte[] lookupFile(int group, int id) {
                try { return assetStore.readContainer(7, group); }
                catch (IOException e) { throw new RuntimeException(e); }
            }
        };
        NpcDefinition old = new NpcDefinition();
        old.aClass3_1881 = Class39_Sub5_Sub9.createJstring("Suspect");
        Class39_Sub5_Sub11.npcDefinitionCache.put(old, 3863L, (byte) 104);
        NpcDefinition clerk = ArchiveWorker.getNpcDefinition(3863);
        require(clerk.aClass3_1881.isEqual(Class39_Sub5_Sub9.createJstring("Grand Exchange Clerk")), "Clerk still a Suspect");
        require(clerk.aClass3Array1866[1].isEqual(Class39_Sub5_Sub9.createJstring("Exchange")), "Missing exchange action");
        require(ArchiveWorker.getNpcDefinition(3863) == clerk, "Clerk cache not reused");
        require("Grand Exchange Clerk".equals(com.rs2.model.npc.NpcDefinition.forId(3863).getName()), "Wrong dialogue name");
        Class39_Sub5_Sub4.widgetsLoaded = new boolean[500];
        Class62_Sub1.widgets = new Widget[500][];
        Class39_Sub11.anInt1478 = StillGraphic.anInt2338 = SubNode.anInt1348 = ClientScript.anInt1713 = -1;
        Class39_Sub5_Sub14.anInt1912 = -1;
        Node.anInt728 = 3;
        FrameBuffer.outgoingGameBuffer = new FrameBuffer(512);
        FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
        jagex.utils.Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109 = new jagex.world.actors.Player();
        for (int id = 18890; id <= 19102; id++) {
            int packed = InterfaceBridge.translate(id);
            require(packed == GrandExchangeWidgets.packed(id), "Different widget mappings for " + id);
            require(InterfaceBridge.toLegacyComponent(packed) == id, "Button cannot return to server: " + id);
            require(Class37.getWidget(packed) != null, "Missing widget " + id);
        }
        require(ClientPackets.getLength(19) == 2, "Item selection packet has wrong length");
        try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
            Widget normalInventory = new Widget();
            normalInventory.decodeOldFormat(new jagex.io.Buffer(store.readFiles(3, 149).get(0)));
            Widget sellInventory = GrandExchangeWidgets.get(19102);
            require(sellInventory.anInt2091 == normalInventory.anInt2091
                    && sellInventory.anInt2021 == normalInventory.anInt2021
                    && sellInventory.quadWidth == normalInventory.quadWidth
                    && sellInventory.quadHeight == normalInventory.quadHeight
                    && sellInventory.anInt2000 == normalInventory.anInt2000
                    && sellInventory.anInt2010 == normalInventory.anInt2010,
                    "Sell inventory grid shifts from the normal inventory");
            Class39_Sub7.decodeBitmapFont(store.readFile(8, "p12_full", ""));
            Class39_Sub5_Sub14.p12fullFont = Class39_Sub14.createBitmapFont();
            Class39_Sub7.decodeBitmapFont(store.readFile(8, "b12_full", ""));
            Class32.aClass39_Sub5_Sub10_Sub1_587 = Class39_Sub14.createBitmapFont();
            Class39_Sub7.decodeBitmapFont(store.readFile(8, "p11_full", ""));
            jagex.world.actors.Npc.aClass39_Sub5_Sub10_Sub1_2495 = Class39_Sub14.createBitmapFont();
            FrameBuffer.aClass39_Sub5_Sub10_Sub1_2148 = jagex.world.actors.Npc.aClass39_Sub5_Sub10_Sub1_2495;
            Class39_Sub7.decodeBitmapFont(store.readFile(8, "scrollbar", ""));
            Class62_Sub2.aClass39_Sub5_Sub10_Sub4Array1607 = Class10.method179((byte) 0);
        }
        Class39_Sub5_Sub10_Sub2.method650(0.8);
        render(503, "overview");
        render(500, "buy");
        render(501, "sell");
        Class39_Sub11.anInt1478 = 500;
        Class37.anInt663 = 2;
        ItemDefinition searchedItem = new ItemDefinition();
        searchedItem.aClass3_1661 = Class39_Sub5_Sub9.createJstring("Logs");
        searchedItem.anInt1644 = -1;
        Class53.itemDefinitionCache.put(searchedItem, 1L, (byte) 104);
        require(GrandExchangeWidgets.click(InterfaceBridge.translate(18897)), "Item chooser didn't open");
        require(GrandExchangeWidgets.submitSearch(Class39_Sub5_Sub9.createJstring("logs")), "Item chooser didn't consume input");
        require(FrameBuffer.outgoingGameBuffer.offset == 3, "Item selection wasn't sent");
        require(((FrameBuffer.outgoingGameBuffer.payload[0] - new IsaacCipher(new int[4]).nextInt()) & 255) == 19,
                "Wrong item selection opcode");
        FrameBuffer.outgoingGameBuffer.offset = 0;
        Class53.itemDefinitionCache.method134(27392);
        Class37.anInt663 = java.util.Collections.max(items.keySet()) + 1;
        searchChecks();
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Selector selector = Selector.open()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            try (SocketChannel receiver = SocketChannel.open(listener.getLocalAddress()); SocketChannel transport = listener.accept()) {
                receiver.socket().setSoTimeout(2000);
                transport.configureBlocking(false);
                com.rs2.model.player.Player player = new com.rs2.model.player.Player(transport.register(selector, SelectionKey.OP_READ));
                player.setOutboundCipher(new IsaacCipher(new int[4]));
                player.setQuestState(0, 1);
                IsaacCipher cipher = new IsaacCipher(new int[4]);
                DataInputStream input = new DataInputStream(receiver.socket().getInputStream());
                GrandExchangeManager.openGrandExchange(player);
                pump(input, cipher, player);
                require(Class39_Sub11.anInt1478 == 503, "Overview didn't open");
                click(player, 19024);
                pump(input, cipher, player);
                require(Class39_Sub11.anInt1478 == 500, "Buy editor didn't open");
                require(GrandExchangeWidgets.searching, "Buying did not open chatbox item search");
                java.nio.ByteBuffer choice = java.nio.ByteBuffer.allocate(2).putShort((short) 1511);
                choice.flip();
                new com.rs2.net.packet.handler.ItemSpawnPacketHandler().handle(player,
                        new com.rs2.net.packet.IncomingPacket(19, 2, com.rs2.net.packet.PacketBuffer.wrapReader(choice)));
                pump(input, cipher, player);
                require(player.selectedGrandExchangeItemId == 1511, "Buy item selection wasn't accepted");
                require(GrandExchangeWidgets.get(18938).anInt1997 == 1511, "Selected buy item icon missing");
                render(500, "buy-selected");
                GrandExchangeManager.openGrandExchange(player);
                pump(input, cipher, player);
                player.getInventoryManager().getContainer().setItem(0, new com.rs2.model.item.ItemStack(1511, 1));
                click(player, 19025);
                pump(input, cipher, player);
                require(Class39_Sub11.anInt1478 == 501 && StillGraphic.anInt2338 == 504, "Sell editor/inventory didn't open");
                com.rs2.net.packet.PacketWriter sell = com.rs2.net.packet.PacketBuffer.allocateWriter(8);
                sell.writeShort(0, com.rs2.net.packet.ByteTransform.ADD, com.rs2.net.packet.ByteOrder.LITTLE);
                sell.writeInt(InterfaceBridge.translate(19102));
                sell.writeShort(1511, com.rs2.net.packet.ByteOrder.LITTLE);
                sell.getBuffer().flip();
                new com.rs2.net.packet.handler.ItemActionPacketHandler().handle(player,
                        new com.rs2.net.packet.IncomingPacket(ClientPackets.WIDGET_ITEM_OPTION_1, 8,
                                com.rs2.net.packet.PacketBuffer.wrapReader(sell.getBuffer())));
                pump(input, cipher, player);
                require(player.selectedGrandExchangeItemId == 1511 && player.selectedGrandExchangeQuantity == 1,
                        "Sell inventory action didn't select the item");
                require(GrandExchangeWidgets.get(18983).anInt1997 == 1511,
                        "Selected sell item icon missing");
                render(501, "sell-selected");
                player.grandExchangeItemIds[0] = 1511;
                player.grandExchangeQuantities[0] = 10;
                player.grandExchangeUnitPrices[0] = 5;
                player.grandExchangeCompletedQuantities[0] = 5;
                GrandExchangeManager.openGrandExchange(player);
                pump(input, cipher, player);
                require(GrandExchangeWidgets.get(19049).quadWidth == 63
                        && GrandExchangeWidgets.get(19049).activeQuadColor == 0xd88020,
                        "Half-filled offer must have a half-width yellow bar");
                player.grandExchangeItemIds[1] = 2309;
                player.grandExchangeQuantities[1] = 10;
                player.grandExchangeUnitPrices[1] = 20;
                player.grandExchangeCompletedQuantities[1] = 10;
                player.grandExchangeSellOfferFlags[1] = true;
                GrandExchangeManager.openGrandExchange(player);
                pump(input, cipher, player);
                require(GrandExchangeWidgets.get(19058).quadWidth == 126
                        && GrandExchangeWidgets.get(19058).activeQuadColor == 0x005f00,
                        "Completed sell offer must have a full green bar");
                render(503, "overview-active");
                click(player, 19042);
                pump(input, cipher, player);
                require(Class39_Sub11.anInt1478 == 502, "Offer details didn't open");
                require(GrandExchangeWidgets.get(19008).anInt2096 == 10,
                        "Buy offer icon must show the full offer quantity");
                Widget close = GrandExchangeWidgets.get(18987);
                require(!close.aBoolean2055 && close.anInt2091 == 473 && close.anInt2021 == 30
                        && close.anInt2089 == 3, "Sell item icon overwrote the status close button");
                render(502, "status");
                player.grandExchangeItemIds[1] = 882;
                player.grandExchangeQuantities[1] = 25;
                player.grandExchangeCompletedQuantities[1] = 25;
                player.grandExchangeUnitPrices[1] = 13;
                player.grandExchangeTotalPrices[1] = 325;
                player.grandExchangePrimaryCollectAmounts[1] = 325;
                click(player, 19051);
                pump(input, cipher, player);
                require(GrandExchangeWidgets.get(19008).anInt1997 == 882
                        && GrandExchangeWidgets.get(19008).anInt2096 == 25,
                        "Completed sale of 25 bronze arrows must display a stack of 25");
                require(GrandExchangeWidgets.get(18998).aClass3_2029.isEqual(GrandExchangeWidgets.literal("25")),
                        "Offer quantity text must match the item icon");
                render(502, "status-sell-arrows");
                click(player, 19042);
                pump(input, cipher, player);
                require(GrandExchangeWidgets.get(19008).anInt2096 == 10,
                        "Switching offers must replace the icon quantity");
                player.grandExchangeCancelledFlags[0] = true;
                GrandExchangeManager.refreshSelectedOfferDetails(player);
                pump(input, cipher, player);
                require(GrandExchangeWidgets.get(19011).activeQuadColor == 0xa00000
                        && GrandExchangeWidgets.get(19011).quadWidth == 298, "Cancelled bar incorrect");
                player.packetSender.closeInterfaces();
                pump(input, cipher, player);
                require(Class39_Sub11.anInt1478 == -1, "Exchange didn't close");
                require(!GrandExchangeWidgets.searching, "Exchange close left search active");
            }
        }
        System.out.println("Exchange checks passed: cached clerk repair, all widget/button mappings, actual overview/buy/sell/status/progress/close packets.");
        System.exit(0);
    }

    private static void searchChecks() throws Exception {
        Class39_Sub11.anInt1478 = 500;
        GrandExchangeWidgets.click(InterfaceBridge.translate(18897));
        renderSearch("search-empty");
        for (char c : "diamond".toCharArray()) GrandExchangeSearch.key(-1, c);
        require(GrandExchangeSearch.resultCount() > 5, "Partial-name search missing results");
        renderSearch("search-results");
        GrandExchangeSearch.mouse(100, 10);
        renderSearch("search-preview");
        require(FrameBuffer.outgoingGameBuffer.offset == 0, "Hovering search sent an item selection");
        Class66.aClass3_1151 = GrandExchangeWidgets.literal("necklace");
        GrandExchangeSearch.update(Class66.aClass3_1151);
        require(GrandExchangeSearch.resultCount() > 10, "Scrollable search query has too few results");
        GrandExchangeSearch.key(99, -1);
        java.lang.reflect.Field scrollbarField = GrandExchangeSearch.class.getDeclaredField("scrollbar");
        scrollbarField.setAccessible(true);
        require(((Widget) scrollbarField.get(null)).anInt1994 > 0, "Search scrollbar did not move");
        renderSearch("search-scrolled");
        GrandExchangeSearch.key(85, -1);
        require(Class66.aClass3_1151.isEqual(GrandExchangeWidgets.literal("necklac")), "Backspace failed");
        GrandExchangeSearch.key(0, -1);
        require(!GrandExchangeWidgets.searching && FrameBuffer.outgoingGameBuffer.offset == 0, "Escape sent an item");

        GrandExchangeWidgets.click(InterfaceBridge.translate(18897));
        for (char c : "rune scim".toCharArray()) GrandExchangeSearch.key(-1, c);
        Class39_Sub5_Sub11.anInt1841 = 1;
        jagex.utils.IsaacPrng.anInt1091 = 100;
        Class33.anInt599 = 338 + 10;
        JString.method95(0);
        require(Class39_Sub5_Sub11.anInt1841 == 2, "Search did not build a clickable result menu");
        ScriptState.method278(1, 0);
        require(FrameBuffer.outgoingGameBuffer.offset == 3, "Click did not send one item-selection packet");
        int itemId = (FrameBuffer.outgoingGameBuffer.payload[1] & 255) << 8 | FrameBuffer.outgoingGameBuffer.payload[2] & 255;
        require(itemId == 1333, "Wrong item selected from chatbox search: " + itemId);
        require(!GrandExchangeWidgets.searching, "Search did not close after selecting");
        FrameBuffer.outgoingGameBuffer.offset = 0;

        GrandExchangeWidgets.click(InterfaceBridge.translate(18897));
        for (char c : "zzzzzzzz".toCharArray()) GrandExchangeSearch.key(-1, c);
        require(GrandExchangeSearch.resultCount() == 0, "Unexpected result for missing item");
        GrandExchangeSearch.key(84, -1);
        require(GrandExchangeWidgets.searching && FrameBuffer.outgoingGameBuffer.offset == 0, "No-result Enter sent an item");
        GrandExchangeWidgets.cancelSearch();
    }

    private static void renderSearch(String name) throws Exception {
        GrandExchangeSearch.draw();
        java.lang.reflect.Field panelField = GrandExchangeSearch.class.getDeclaredField("panel");
        panelField.setAccessible(true);
        AbstractImage panel = (AbstractImage) panelField.get(null);
        require(panel != null && panel.buffer != null, "Search panel was not initialized");
        BufferedImage image = new BufferedImage(panel.width, panel.height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, panel.width, panel.height, panel.buffer, 0, panel.width);
        File output = new File("qa-output/grand-exchange/" + name + ".png");
        output.getParentFile().mkdirs();
        ImageIO.write(image, "png", output);
    }

    private static void click(com.rs2.model.player.Player player, int legacy) {
        java.nio.ByteBuffer payload = java.nio.ByteBuffer.allocate(4).putInt(InterfaceBridge.translate(legacy));
        payload.flip();
        new com.rs2.net.packet.handler.InterfaceActionPacketHandler().handle(player,
                new com.rs2.net.packet.IncomingPacket(ClientPackets.INTERFACE_BUTTON, 4,
                        com.rs2.net.packet.PacketBuffer.wrapReader(payload)));
    }

    private static void render(int group, String name) throws Exception {
        int[] pixels = new int[512 * 334];
        DrawingArea.setBuffer(pixels, 512, 334);
        Class39_Sub5_Sub10_Sub2.method653(0, 0, 512, 334, null);
        require(Class20.drawWidgets(Class62_Sub1.widgets[group], -1, 0, -1, 0, 0, 512, 9843, 334, 0), "Screen failed to render: " + name);
        BufferedImage image = new BufferedImage(512, 334, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 512, 334, pixels, 0, 512);
        File output = new File("qa-output/grand-exchange/" + name + ".png");
        output.getParentFile().mkdirs();
        ImageIO.write(image, "png", output);
    }

    private static void pump(DataInputStream input, IsaacCipher cipher, com.rs2.model.player.Player player) throws Exception {
        player.packetSender.sendInterfaceText("__end__", 18892);
        while (true) {
            int opcode = (input.readUnsignedByte() - cipher.nextInt()) & 255;
            int length = opcode == 18 ? 3 : Client.incomingSizes[opcode];
            if (length == -1) length = input.readUnsignedByte();
            if (length == -2) length = input.readUnsignedShort();
            byte[] payload = new byte[length];
            input.readFully(payload);
            Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(Math.max(512, length));
            Class37.gameSocket = new JSocket(new Socket() {
                public void setSoTimeout(int value) {}
                public void setTcpNoDelay(boolean value) {}
                public InputStream getInputStream() { return new ByteArrayInputStream(payload.length == 0 ? new byte[] {0} : payload); }
                public OutputStream getOutputStream() { return capture; }
            }, null);
            Class4.frameId = opcode;
            Huffmans.frameSize = length;
            require(Bzip2Block.readFrame() && Class4.frameId == -1, "Client crashed reading exchange packet " + opcode);
            if (opcode == 180 && GrandExchangeWidgets.get(18892).aClass3_2029.isEqual(Class39_Sub5_Sub9.createJstring("__end__"))) {
                GrandExchangeWidgets.get(18892).aClass3_2029 = Class66.blankString;
                break;
            }
        }
    }

    private static void require(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
