package unpackaged;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public final class DeveloperToolsServer implements AutoCloseable {
    private static volatile DeveloperToolsServer current;
    private static final List<String> PROTOCOLS = Arrays.asList("2025-03-26", "2025-06-18", "2025-11-25");
    private final HttpServer server;
    private final ThreadPoolExecutor workers;
    private final List<Object> log = new ArrayList<Object>();
    private final Object inputLock = new Object();
    private long sequence;

    private DeveloperToolsServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 16);
        workers = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(64), runnable -> {
                    Thread thread = new Thread(runnable, "soloscape-mcp-http");
                    thread.setDaemon(true); return thread;
                }, new ThreadPoolExecutor.AbortPolicy());
        server.setExecutor(workers);
        server.createContext("/", this::handle);
        server.start();
    }

    static synchronized DeveloperToolsServer start(int port) throws IOException {
        if (current != null) throw new IllegalStateException("Developer tools already running");
        current = new DeveloperToolsServer(port);
        return current;
    }

    public static void startFromProperties() {
        int port = Integer.getInteger("soloscape.mcp.port", -1);
        if (port == -1) return;
        try {
            if (port < 1 || port > 65535) throw new IllegalArgumentException("Invalid MCP port");
            start(port);
            System.out.println("[developer-tools] MCP: http://127.0.0.1:" + port + "/mcp");
            System.out.println("[developer-tools] Dashboard: http://127.0.0.1:" + port + "/");
        } catch (Exception exception) {
            System.err.println("[developer-tools] Could not start: " + exception.getMessage());
        }
    }

    public static boolean isRunning() { return current != null; }
    int port() { return server.getAddress().getPort(); }

    public static void stop() {
        DeveloperToolsServer active = current;
        if (active != null) active.close();
    }

    @Override public void close() {
        synchronized (DeveloperToolsServer.class) {
            if (current == this) current = null;
        }
        server.stop(0); workers.shutdownNow();
        DeveloperToolsClient.reset();
    }

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String host = exchange.getRequestHeaders().getFirst("Host");
            String origin = exchange.getRequestHeaders().getFirst("Origin");
            String localhost = "localhost:" + port(), loopback = "127.0.0.1:" + port();
            if ((!localhost.equals(host) && !loopback.equals(host))
                    || (origin != null && !origin.equals("http://" + localhost) && !origin.equals("http://" + loopback))) {
                send(exchange, 403, "text/plain", "Local requests only"); return;
            }
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            if ("/mcp".equals(path)) {
                if (!"POST".equals(method)) { send(exchange, 405, "text/plain", "Use POST"); return; }
                String protocol = exchange.getRequestHeaders().getFirst("MCP-Protocol-Version");
                if (protocol != null && !PROTOCOLS.contains(protocol)) {
                    send(exchange, 400, "text/plain", "Unsupported MCP protocol version"); return;
                }
                String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
                if (contentType == null || !contentType.toLowerCase(java.util.Locale.ROOT).startsWith("application/json")) {
                    send(exchange, 415, "text/plain", "Expected application/json"); return;
                }
                Object decoded;
                try { decoded = McpJson.parse(new String(read(exchange.getRequestBody(), 1024 * 1024), StandardCharsets.UTF_8)); }
                catch (IllegalArgumentException exception) {
                    sendJson(exchange, 400, error(null, -32700, "Parse error")); return;
                }
                if (!(decoded instanceof Map)) { sendJson(exchange, 400, error(null, -32600, "Invalid request")); return; }
                Map<String, Object> request = McpJson.asObject(decoded);
                Object id = request.get("id");
                if (!"2.0".equals(request.get("jsonrpc")) || !(request.get("method") instanceof String)
                        || (request.containsKey("id") && !(id instanceof String) && !(id instanceof Number))) {
                    sendJson(exchange, 400, error(null, -32600, "Invalid request")); return;
                }
                if (!request.containsKey("id")) {
                    send(exchange, 202, "application/json", ""); return;
                }
                Object response;
                try { response = McpJson.object("jsonrpc", "2.0", "id", id, "result", dispatch(request)); }
                catch (UnknownMethod exception) { response = error(id, -32601, "Method not found"); }
                catch (IllegalArgumentException exception) { response = error(id, -32602, exception.getMessage()); }
                sendJson(exchange, 200, response);
            } else if ("GET".equals(method) && "/log".equals(path)) {
                long after = 0;
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("after=")) {
                    try { after = Long.parseLong(query.substring(6)); }
                    catch (NumberFormatException exception) { send(exchange, 400, "text/plain", "Invalid log cursor"); return; }
                }
                List<Object> entries = new ArrayList<Object>();
                synchronized (log) {
                    for (Object entry : log) if (((Number) McpJson.asObject(entry).get("id")).longValue() > after) entries.add(entry);
                    sendJson(exchange, 200, McpJson.object("entries", entries, "lastId", sequence));
                }
            } else if ("GET".equals(method) && "/".equals(path)) {
                try (InputStream stream = DeveloperToolsServer.class.getResourceAsStream("/assets/developer-tools.html")) {
                    if (stream == null) { send(exchange, 500, "text/plain", "Dashboard asset missing; rebuild the client"); return; }
                    send(exchange, 200, "text/html; charset=utf-8", new String(read(stream, 1024 * 1024), StandardCharsets.UTF_8));
                }
            } else send(exchange, 404, "text/plain", "Not found");
        } catch (IOException exception) {
            throw exception;
        } catch (Exception exception) {
            send(exchange, 500, "text/plain", "Developer tools request failed");
        } finally { exchange.close(); }
    }

    private Object dispatch(Map<String, Object> request) throws UnknownMethod {
        Map<String, Object> params = request.containsKey("params") ? McpJson.asObject(request.get("params")) : McpJson.object();
        switch ((String) request.get("method")) {
            case "initialize": {
                String requested = DeveloperToolsClient.string(params, "protocolVersion", "2025-06-18");
                return McpJson.object("protocolVersion", PROTOCOLS.contains(requested) ? requested : "2025-06-18",
                        "capabilities", McpJson.object("tools", McpJson.object("listChanged", false)),
                        "serverInfo", McpJson.object("name", "soloscape-developer-tools", "version", "1.0.0"),
                        "instructions", "Tools control the real revision-443 client. Log in manually first. Widget x/y are parent-relative. Prefer act_and_wait to combine action, confirmation and fresh text results. Use get_snapshot for routine inspection; use screenshots for visual checks.");
            }
            case "ping": return McpJson.object();
            case "tools/list": return McpJson.object("tools", DeveloperToolsClient.tools());
            case "tools/call": {
                if (!(params.get("name") instanceof String)) throw new IllegalArgumentException("Tool name required");
                String name = (String) params.get("name");
                Map<String, Object> args = params.containsKey("arguments") ? McpJson.asObject(params.get("arguments")) : McpJson.object();
                DeveloperToolsClient.validate(name, args);
                long started = System.currentTimeMillis();
                Object result;
                boolean failed = false;
                try { result = call(name, args); }
                catch (Exception exception) {
                    failed = true;
                    Throwable cause = exception;
                    while (cause.getCause() != null) cause = cause.getCause();
                    result = McpJson.object("content", Collections.singletonList(McpJson.object("type", "text", "text",
                            cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage())), "isError", true);
                }
                synchronized (log) {
                    log.add(McpJson.object("id", ++sequence, "name", name, "arguments", args,
                            "timestamp", started, "durationMs", System.currentTimeMillis() - started,
                            "isError", failed, "result", result));
                    if (log.size() > 100) log.remove(0);
                }
                return result;
            }
            default: throw new UnknownMethod();
        }
    }

    private Object call(String name, Map<String, Object> args) throws Exception {
        if ("screenshot".equals(name)) {
            BufferedImage screen = WebClientBridge.snapshot();
            int x = DeveloperToolsClient.bounded(args, "x", 0, 0, 764);
            int y = DeveloperToolsClient.bounded(args, "y", 0, 0, 502);
            int width = DeveloperToolsClient.bounded(args, "width", 765 - x, 1, 765 - x);
            int height = DeveloperToolsClient.bounded(args, "height", 503 - y, 1, 503 - y);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(screen.getSubimage(x, y, width, height), "png", out);
            return McpJson.object("content", Collections.singletonList(McpJson.object("type", "image", "mimeType", "image/png",
                    "data", Base64.getEncoder().encodeToString(out.toByteArray()))), "isError", false);
        }
        if (Arrays.asList("click", "hover", "drag", "press_key", "type_chat", "enter_input").contains(name)) {
            synchronized (inputLock) { DeveloperToolsClient.input(name, args); }
            return textResult(McpJson.object("inputDispatched", true));
        }
        if ("wait_for".equals(name)) {
            int timeout = DeveloperToolsClient.bounded(args, "timeoutMs", 10000, 0, 30000);
            return textResult(DeveloperToolsClient.await(null, args, timeout));
        }
        if ("act_and_wait".equals(name)) {
            String action = DeveloperToolsClient.string(args, "action", "");
            if (!Arrays.asList("interact_npc", "interact_object", "pickup_item", "item_action",
                    "widget_action", "click_menu_option", "walk_to", "continue_dialogue", "select_option").contains(action))
                throw new IllegalArgumentException("Use a direct game action with act_and_wait");
            Map<String, Object> actionArgs = McpJson.asObject(args.get("arguments"));
            Map<String, Object> wait = McpJson.asObject(args.get("wait"));
            DeveloperToolsClient.validate(action, actionArgs);
            DeveloperToolsClient.validate("wait_for", wait);
            int timeout = DeveloperToolsClient.bounded(wait, "timeoutMs", 10000, 0, 30000);
            return textResult(DeveloperToolsClient.await(() -> DeveloperToolsClient.read(action, actionArgs), wait, timeout));
        }
        return textResult(DeveloperToolsClient.onClientThread(() -> DeveloperToolsClient.read(name, args)));
    }

    private static Object textResult(Object value) {
        return McpJson.object("content", Collections.singletonList(McpJson.object("type", "text", "text", McpJson.write(value))), "isError", false);
    }

    private static Object error(Object id, int code, String message) {
        return McpJson.object("jsonrpc", "2.0", "id", id, "error", McpJson.object("code", code, "message", message));
    }

    private static byte[] read(InputStream in, int limit) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) != -1) {
            if (out.size() + count > limit) throw new IllegalArgumentException("Request is too large");
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    private static void sendJson(HttpExchange exchange, int status, Object value) throws IOException {
        send(exchange, status, "application/json; charset=utf-8", McpJson.write(value));
    }

    private static void send(HttpExchange exchange, int status, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.getResponseHeaders().set("Content-Security-Policy", "default-src 'self'; script-src 'unsafe-inline'; style-src 'unsafe-inline'; img-src 'self' data:; frame-ancestors 'none'");
        exchange.sendResponseHeaders(status, status == 202 ? -1 : bytes.length);
        if (status != 202) exchange.getResponseBody().write(bytes);
    }

    private static final class UnknownMethod extends Exception { }
}
