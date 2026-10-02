package unpackaged;

import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import jagex.utils.Cache;
import jagex.utils.JString;
import jagex.io.FrameBuffer;
import jagex.world.actors.Player;

public final class DeveloperToolsChecks {
    private static int checks;
    private static int port;

    public static void main(String[] args) throws Exception {
        jsonChecks();
        require(!DeveloperToolsServer.isRunning(), "Developer tools enabled by default");
        Class31.state = 10;
        Class66.stateValues = new int[2000];
        Class62_Sub1.widgets = new Widget[400][];
        Class39_Sub5_Sub4.widgetsLoaded = new boolean[400];
        Class39_Sub5_Sub4.widgetsLoaded[241] = true;
        Widget label = new Widget();
        label.anInt2084 = (241 << 16) | 3;
        label.aClass3_2029 = Class39_Sub5_Sub9.createJstring("Hello adventurer");
        label.quadWidth = 300; label.quadHeight = 20;
        Class62_Sub1.widgets[241] = new Widget[] {null, null, null, label};
        Class39_Sub5_Sub14.anInt1912 = 241;
        Class39_Sub11.anInt1478 = -1;
        Player local = new Player();
        local.anInt2301 = 50 * 128 + 64; local.anInt2275 = 51 * 128 + 64;
        local.aClass3_2521 = Class39_Sub5_Sub9.createJstring("Test player");
        Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109 = local;
        Class65.anInt1145 = 3200; JKeyListener.anInt618 = 3200;
        AtomicBoolean running = new AtomicBoolean(true);
        DeveloperToolsServer server = DeveloperToolsServer.start(0);
        port = server.port();
        Thread game = new Thread(() -> {
            while (running.get()) {
                Class2.logicCycle++;
                DeveloperToolsClient.pump();
                try { Thread.sleep(10); } catch (InterruptedException exception) { return; }
            }
        });
        game.start();
        try {
            Map<String, Object> init = rpc("initialize", McpJson.object("protocolVersion", "2025-06-18"));
            require("2025-06-18".equals(McpJson.asObject(init.get("result")).get("protocolVersion")), "Protocol negotiation");
            Map<String, Object> list = McpJson.asObject(rpc("tools/list", McpJson.object()).get("result"));
            require(((List<?>) list.get("tools")).size() >= 27, "Missing tools");
            Response notification = post(McpJson.object("jsonrpc", "2.0", "method", "notifications/initialized"), null, null);
            require(notification.status == 202 && notification.body.isEmpty(), "Notification must return empty 202");
            require(request("GET", "/mcp", null, null, null).status == 405, "Optional SSE GET must return 405");
            require(post(McpJson.object("jsonrpc", "2.0", "id", 1, "method", "ping"), "https://example.com", null).status == 403, "Cross-origin request accepted");
            require(post(McpJson.object("jsonrpc", "2.0", "id", 1, "method", "ping"), null, "2099-01-01").status == 400, "Unsupported protocol accepted");
            require(request("POST", "/mcp", "{bad", null, null).status == 400, "Malformed JSON accepted");
            require(request("POST", "/mcp", "[]", null, null).status == 400, "JSON-RPC batch accepted");
            require(McpJson.asObject(rpc("unknown", McpJson.object()).get("error")).get("code").equals(-32601L), "Unknown method code");
            Map<String, Object> state = McpJson.asObject(toolData("get_client_state", McpJson.object()));
            require(Boolean.FALSE.equals(state.get("loggedIn")) && state.get("player") == null, "Login state exposes stale player");
            Map<String, Object> widget = McpJson.asObject(toolData("get_widget", McpJson.object("componentId", label.anInt2084)));
            require("Hello adventurer".equals(widget.get("text")), "JString widget extraction");
            require(((List<?>) toolData("dump_interface", McpJson.object("interfaceId", 241))).size() == 1, "Interface dump");
            Map<String, Object> missing = toolResult("get_widget", McpJson.object("componentId", 0));
            require(Boolean.TRUE.equals(missing.get("isError")), "Missing widget must be tool error");
            Map<String, Object> invalid = rpc("tools/call", McpJson.object("name", "get_widget", "arguments", McpJson.object()));
            require(invalid.containsKey("error"), "Missing required argument accepted");
            require(rpc("tools/call", McpJson.object("name", "get_widget", "arguments", McpJson.object("componentId", 0.5))).containsKey("error"), "Fractional component ID accepted");
            require(McpJson.asObject(toolData("wait_for", McpJson.object("condition", "interface_open", "interfaceId", 241))).get("matched").equals(true), "Interface wait");
            require(Boolean.TRUE.equals(toolResult("wait_for", McpJson.object("condition", "logged_in", "timeoutMs", 25)).get("isError")), "Wait timeout must be error");
            DeveloperToolsClient.onClientThread(() -> { Class31.state = 30; return null; });
            state = McpJson.asObject(toolData("get_client_state", McpJson.object()));
            Map<String, Object> player = McpJson.asObject(state.get("player"));
            require(player.get("x").equals(3250L) && player.get("y").equals(3251L), "World coordinate axes");
            require(McpJson.asObject(toolData("wait_for", McpJson.object("condition", "at_tile", "x", 3250, "y", 3251))).get("matched").equals(true), "Tile wait");
            DeveloperToolsClient.onClientThread(() -> {
                Class66.stateValues[43] = 99;
                DeveloperToolsClient.chat(Class39_Sub5_Sub9.createJstring("Bob"), Class39_Sub5_Sub9.createJstring("Some logs"), 0);
                DeveloperToolsClient.script(42, new Object[] {42, 17, Class39_Sub5_Sub9.createJstring("Test")});
                return null;
            });
            Thread.sleep(30);
            require(!((List<?>) toolData("get_var_history", McpJson.object())).isEmpty(), "Varp history");
            require(McpJson.asObject(toolData("get_var", McpJson.object("type", "varp", "id", 43))).get("value").equals(99L), "Varp read");
            require(((List<?>) toolData("get_chat_history", McpJson.object())).size() == 1, "Chat history");
            require(((List<?>) toolData("get_script_history", McpJson.object())).size() == 1, "Script history");
            require(McpJson.asObject(toolData("wait_for", McpJson.object("condition", "chat_message", "textContains", "logs"))).get("matched").equals(true), "Chat wait");
            DeveloperToolsClient.onClientThread(() -> { actionChecks(label); return null; });
            fastPathChecks(label);
            int[] pixels = new int[765 * 503]; Arrays.fill(pixels, 0x123456);
            WebClientBridge.blit(pixels, 765, 503, 0, 0);
            Map<String, Object> screenshot = toolResult("screenshot", McpJson.object("x", 10, "y", 20, "width", 32, "height", 16));
            Map<String, Object> image = McpJson.asObject(((List<?>) screenshot.get("content")).get(0));
            BufferedImage decoded = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode((String) image.get("data"))));
            require(decoded.getWidth() == 32 && decoded.getHeight() == 16 && (decoded.getRGB(0, 0) & 0xffffff) == 0x123456, "Screenshot capture/crop");
            require(Boolean.TRUE.equals(toolResult("screenshot", McpJson.object("x", 760, "width", 20)).get("isError")), "Out-of-bounds crop accepted");
            AtomicInteger keyEvents = new AtomicInteger();
            Class41.aCanvas778 = new Canvas();
            Class41.aCanvas778.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.VK_SPACE && event.getKeyChar() == ' ') keyEvents.incrementAndGet();
                }
                @Override public void keyReleased(KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.VK_SPACE) keyEvents.incrementAndGet();
                }
            });
            toolData("press_key", McpJson.object("key", "SPACE"));
            EventQueue.invokeAndWait(() -> { });
            require(keyEvents.get() == 2, "Injected keys must reach client handlers while dashboard has focus");
            AtomicInteger mouseEvents = new AtomicInteger();
            Class41.aCanvas778.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mousePressed(java.awt.event.MouseEvent event) { mouseEvents.incrementAndGet(); }
                @Override public void mouseReleased(java.awt.event.MouseEvent event) { mouseEvents.incrementAndGet(); }
            });
            toolData("click", McpJson.object("x", 100, "y", 100));
            EventQueue.invokeAndWait(() -> { });
            require(mouseEvents.get() == 2, "Fast click lost a press or release");
            StringBuilder typed = new StringBuilder();
            Class41.aCanvas778.addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent event) { typed.append(event.getKeyChar()); }
            });
            char[] letters = new char[200]; Arrays.fill(letters, 'a');
            toolData("enter_input", McpJson.object("text", new String(letters)));
            EventQueue.invokeAndWait(() -> { });
            require(typed.toString().equals(new String(letters) + "\n"), "Batched typing lost or reordered characters/Enter");
            Class41.aCanvas778 = null;
            Response dashboard = request("GET", "/", null, null, null);
            require(dashboard.status == 200 && dashboard.body.contains("SoloScape developer tools"), "Dashboard asset");
            for (int i = 0; i < 105; i++) toolData("get_client_state", McpJson.object());
            Map<String, Object> log = McpJson.asObject(McpJson.parse(request("GET", "/log?after=0", null, null, null).body));
            List<?> entries = (List<?>) log.get("entries");
            require(entries.size() == 100, "Log retention");
            long last = ((Number) log.get("lastId")).longValue();
            Map<String, Object> incremental = McpJson.asObject(McpJson.parse(request("GET", "/log?after=" + last, null, null, null).body));
            require(((List<?>) incremental.get("entries")).isEmpty(), "Log cursor");
        } finally {
            running.set(false); game.join(2000); server.close();
        }
        require(!DeveloperToolsServer.isRunning(), "Server failed to stop");
        System.out.println("PASS: " + checks + " developer-tools checks");
    }

    private static void fastPathChecks(Widget label) throws Exception {
        Map<String, Object> snapshot = McpJson.asObject(toolData("get_snapshot", McpJson.object()));
        require(snapshot.containsKey("state") && snapshot.containsKey("dialogue") && snapshot.containsKey("inventory"), "Compact snapshot sections");
        Map<String, Object> inventory = McpJson.asObject(snapshot.get("inventory"));
        require(((List<?>) inventory.get("items")).size() == 1, "Compact inventory retains occupied slots");
        require(McpJson.asObject(toolData("wait_for", McpJson.object("condition", "chat_message", "textContains", "SOME LOGS"))).get("matched").equals(true), "Chat wait must ignore case");
        require(Boolean.TRUE.equals(toolResult("act_and_wait", McpJson.object("action", "screenshot", "arguments", McpJson.object(),
                "wait", McpJson.object("condition", "logged_in"))).get("isError")), "Read-only action accepted");
        DeveloperToolsClient.onClientThread(() -> { FrameBuffer.outgoingGameBuffer = buffer(); return null; });
        Map<String, Object> unchanged = toolResult("act_and_wait", McpJson.object("action", "widget_action",
                "arguments", McpJson.object("componentId", label.anInt2084),
                "wait", McpJson.object("condition", "dialogue_changed", "timeoutMs", 25)));
        require(Boolean.TRUE.equals(unchanged.get("isError")), "Unchanged dialogue confirmed prematurely");
        DeveloperToolsClient.onClientThread(() -> { FrameBuffer.outgoingGameBuffer = buffer(); return null; });
        java.util.concurrent.CompletableFuture<Object> response = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try { return toolData("act_and_wait", McpJson.object("action", "widget_action",
                    "arguments", McpJson.object("componentId", label.anInt2084),
                    "wait", McpJson.object("condition", "dialogue_changed", "timeoutMs", 2000))); }
            catch (Exception exception) { throw new RuntimeException(exception); }
        });
        long deadline = System.nanoTime() + java.util.concurrent.TimeUnit.SECONDS.toNanos(2);
        while (Boolean.FALSE.equals(DeveloperToolsClient.onClientThread(() -> FrameBuffer.outgoingGameBuffer.offset > 0))) {
            if (System.nanoTime() > deadline) throw new AssertionError("Action was not dispatched");
        }
        DeveloperToolsClient.onClientThread(() -> { label.aClass3_2029 = Class39_Sub5_Sub9.createJstring("Fresh dialogue result"); return null; });
        Map<String, Object> result = McpJson.asObject(response.get(3, java.util.concurrent.TimeUnit.SECONDS));
        require(Boolean.TRUE.equals(result.get("matched")) && result.containsKey("actionResult"), "Combined action confirmation");
        Map<String, Object> fresh = McpJson.asObject(McpJson.asObject(result.get("snapshot")).get("dialogue"));
        require("Fresh dialogue result".equals(McpJson.asObject(((List<?>) fresh.get("lines")).get(0)).get("text")), "Combined action returned stale dialogue");
        DeveloperToolsClient.onClientThread(() -> { FrameBuffer.outgoingGameBuffer = buffer(); return null; });
        require(Boolean.TRUE.equals(toolResult("act_and_wait", McpJson.object("action", "widget_action",
                "arguments", McpJson.object("componentId", label.anInt2084),
                "wait", McpJson.object("condition", "chat_message", "textContains", "logs", "timeoutMs", 25))).get("isError")), "Combined action matched old chat");
        DeveloperToolsClient.onClientThread(() -> { FrameBuffer.outgoingGameBuffer = buffer(); return null; });
        require(Boolean.TRUE.equals(toolResult("act_and_wait", McpJson.object("action", "widget_action",
                "arguments", McpJson.object("componentId", label.anInt2084),
                "wait", McpJson.object("condition", "unknown", "timeoutMs", 25))).get("isError")), "Invalid condition accepted");
        require(((Number) DeveloperToolsClient.onClientThread(() -> FrameBuffer.outgoingGameBuffer.offset)).intValue() == 0,
                "Invalid wait dispatched action before validation");
    }

    private static void jsonChecks() {
        String value = "quote\" slash\\ newline\n café";
        Map<String, Object> source = McpJson.object("text", value, "number", 2147483648L, "array", Arrays.asList(true, null, -2));
        require(value.equals(McpJson.asObject(McpJson.parse(McpJson.write(source))).get("text")), "JSON round trip");
        for (String invalid : new String[] {"01", "1.", "1e", "[1,]", "{\"x\":1,\"x\":2}", "true false", "\"\n\""}) {
            boolean rejected = false;
            try { McpJson.parse(invalid); } catch (IllegalArgumentException exception) { rejected = true; }
            require(rejected, "Invalid JSON accepted: " + invalid);
        }
    }

    private static void actionChecks(Widget label) {
        Class39_Sub5_Sub11.anInt1841 = 0;
        Class12.anIntArray196 = new int[10]; Class43.anIntArray820 = new int[10];
        NameTable.anIntArray176 = new int[10]; JKeyListener.anIntArray621 = new int[10];
        Class33.aClass3Array601 = new JString[10];
        Class1.anInt36 = 50;
        Class1.aClass3Array35 = new JString[50];
        JString savedChat = Class39_Sub5_Sub9.createJstring("Chat history must stay intact");
        Class1.aClass3Array35[0] = savedChat;
        Class14.aClass19_213 = new jagex.utils.HashTable(32);
        ItemDefinition coinDefinition = new ItemDefinition(); coinDefinition.id = 995;
        coinDefinition.aClass3_1661 = Class39_Sub5_Sub9.createJstring("Coins");
        Class53.itemDefinitionCache.put(coinDefinition, 995L, (byte) -125);
        jagex.utils.Huffmans.method889(0, 0, 995, 28, 100);
        Map<String, Object> inventory = McpJson.asObject(DeveloperToolsClient.read("get_inventory", McpJson.object("inventoryId", 93)));
        require(Boolean.TRUE.equals(inventory.get("loaded")), "Server inventory container mapping");
        Map<String, Object> coins = McpJson.asObject(((List<?>) inventory.get("items")).get(0));
        require(((Number) coins.get("id")).intValue() == 995 && ((Number) coins.get("quantity")).intValue() == 100, "Inventory item id and quantity");
        Widget items = new Widget(); items.anInt2084 = 149 << 16;
        items.anIntArray2087 = new int[] {996}; items.anIntArray2073 = new int[] {100};
        Class62_Sub1.widgets[149] = new Widget[] {items};
        Class39_Sub5_Sub4.widgetsLoaded[149] = true;
        Map<String, Object> itemWidget = McpJson.asObject(DeveloperToolsClient.read("get_widget", McpJson.object("componentId", items.anInt2084)));
        require(((int[]) itemWidget.get("itemIdsPlusOne"))[0] == 996 && ((int[]) itemWidget.get("itemQuantities"))[0] == 100, "Widget item id and quantity");
        FrameBuffer expected = buffer();
        expected.putFrame(153); expected.putWordLe(-1); expected.putDword(label.anInt2084);
        FrameBuffer.outgoingGameBuffer = buffer();
        Class39_Sub10.anInt1420 = -1;
        label.anInt2089 = 6;
        DeveloperToolsClient.read("continue_dialogue", McpJson.object());
        require(Arrays.equals(packet(expected), packet(FrameBuffer.outgoingGameBuffer)), "Continue must emit normal revision-443 packet");
        require(Class39_Sub10.anInt1420 == label.anInt2084, "Continue did not set pending response");
        require(Class39_Sub5_Sub11.anInt1841 == 0 && Class12.anIntArray196[0] == 0 && Class33.aClass3Array601[0] == null,
                "Synthetic menu action changed the live menu");
        Widget option = new Widget(); option.anInt2084 = (241 << 16) | 4;
        option.anInt2021 = label.anInt2021 + 20; option.anInt2089 = 6;
        Class62_Sub1.widgets[241] = new Widget[] {null, null, null, label, option};
        Class39_Sub10.anInt1420 = -1;
        expected = buffer(); expected.putFrame(153); expected.putWordLe(-1); expected.putDword(option.anInt2084);
        FrameBuffer.outgoingGameBuffer = buffer();
        DeveloperToolsClient.read("select_option", McpJson.object("option", 2));
        require(Arrays.equals(packet(expected), packet(FrameBuffer.outgoingGameBuffer)), "Selected option used wrong widget packet");
        label.anInt2089 = 1; Class62_Sub1.widgets[241] = new Widget[] {null, null, null, label};
        expected = buffer(); expected.putFrame(54); expected.putDword(label.anInt2084);
        FrameBuffer.outgoingGameBuffer = buffer();
        DeveloperToolsClient.read("widget_action", McpJson.object("componentId", label.anInt2084));
        require(Arrays.equals(packet(expected), packet(FrameBuffer.outgoingGameBuffer)), "Button must emit normal revision-443 packet");
        require(Class1.anInt36 == 50 && Class1.aClass3Array35[0] == savedChat, "Menu action changed chat history");
        label.aBoolean2055 = true;
        boolean hiddenRejected = false;
        try { DeveloperToolsClient.read("widget_action", McpJson.object("componentId", label.anInt2084)); }
        catch (IllegalArgumentException exception) { hiddenRejected = true; }
        require(hiddenRejected, "Hidden button action accepted");
        label.aBoolean2055 = false;
    }

    private static FrameBuffer buffer() {
        FrameBuffer buffer = new FrameBuffer(256);
        buffer.initIsaacCipher(new int[] {1, 2, 3, 4});
        return buffer;
    }

    private static byte[] packet(FrameBuffer buffer) { return Arrays.copyOf(buffer.payload, buffer.offset); }

    private static Map<String, Object> rpc(String method, Map<String, Object> params) throws Exception {
        Response response = post(McpJson.object("jsonrpc", "2.0", "id", 1, "method", method, "params", params), null, null);
        require(response.status == 200, "RPC HTTP status: " + response.status + " " + response.body);
        return McpJson.asObject(McpJson.parse(response.body));
    }

    private static Map<String, Object> toolResult(String name, Map<String, Object> arguments) throws Exception {
        Map<String, Object> response = rpc("tools/call", McpJson.object("name", name, "arguments", arguments));
        require(response.containsKey("result"), "Tool protocol error: " + McpJson.write(response));
        return McpJson.asObject(response.get("result"));
    }

    private static Object toolData(String name, Map<String, Object> arguments) throws Exception {
        Map<String, Object> result = toolResult(name, arguments);
        require(!Boolean.TRUE.equals(result.get("isError")), "Tool failed: " + McpJson.write(result));
        return McpJson.parse((String) McpJson.asObject(((List<?>) result.get("content")).get(0)).get("text"));
    }

    private static Response post(Object value, String origin, String protocol) throws Exception {
        return request("POST", "/mcp", McpJson.write(value), origin, protocol);
    }

    private static Response request(String method, String path, String body, String origin, String protocol) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://127.0.0.1:" + port + path).openConnection();
        connection.setConnectTimeout(2000); connection.setReadTimeout(7000);
        connection.setRequestMethod(method);
        if (origin != null) connection.setRequestProperty("Origin", origin);
        if (protocol != null) connection.setRequestProperty("MCP-Protocol-Version", protocol);
        if (body != null) {
            connection.setDoOutput(true); connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().close();
        }
        int status = connection.getResponseCode();
        InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (stream != null) try (InputStream in = stream) {
            byte[] buffer = new byte[4096]; int count;
            while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
        }
        connection.disconnect();
        return new Response(status, new String(out.toByteArray(), StandardCharsets.UTF_8));
    }

    private static void require(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }

    private static final class Response {
        final int status; final String body;
        Response(int status, String body) { this.status = status; this.body = body; }
    }
}
