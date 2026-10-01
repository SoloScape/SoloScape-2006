import com.rs2.cache.js5.Js5CacheStore;
import com.rs2.util.ChatCodec;
import com.rs2.ServerSettings;
import com.rs2.model.World;
import com.rs2.model.player.Player;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.PacketDispatcher;
import jagex.utils.Huffmans;
import java.io.File;
import java.io.DataInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.lang.reflect.Field;

/** Run from Server with Client/build/classes on the test classpath. */
public final class ChatCodecCompatibilityChecks {
    public static void main(String[] args) throws Exception {
        byte[] table;
        try (Js5CacheStore store = new Js5CacheStore(new File("cache"))) {
            table = store.readFile(10, "huffman", "");
        }
        Huffmans client = new Huffmans(table);
        ChatCodec server = ChatCodec.get();
        Charset charset = Charset.forName("windows-1252");
        for (String text : new String[] {"", "h", "hi", "hello world!", "A private message!",
                "£ coins", "Mixed CASE: [hello] @friend + 123?", repeat('e', 80), repeat('x', 128)}) {
            byte[] plain = text.getBytes(charset);
            byte[] wire = new byte[2 + plain.length * 4];
            int offset = plain.length < 128 ? 1 : 2;
            if (offset == 1) wire[0] = (byte) plain.length;
            else {
                wire[0] = (byte) ((plain.length + 32768) >>> 8);
                wire[1] = (byte) (plain.length + 32768);
            }
            int length = client.encode(plain, 0, wire, offset, plain.length);
            wire = Arrays.copyOf(wire, offset + length);
            String decoded = server.decode(wire);
            require(text.equals(decoded), "Client -> server: expected " + text + ", got " + decoded);
            byte[] encoded = server.encode(text);
            require(Arrays.equals(wire, encoded), "Server wire differs from client for " + text);
            byte[] received = new byte[plain.length];
            client.decode(0, received, received.length, offset, encoded);
            require(Arrays.equals(plain, received), "Server -> client: " + text);
        }
        boolean rejected = false;
        try { server.decode(new byte[] {5}); }
        catch (IllegalArgumentException expected) { rejected = true; }
        require(rejected, "Truncated payload accepted");
        // A live JVM retains the old codec object across a constructor HotSwap.
        Field codes = ChatCodec.class.getDeclaredField("codes");
        codes.setAccessible(true);
        ((int[]) codes.get(server))[0] = 4194144; // Observed obsolete 443 table signature.
        ChatCodec refreshed = ChatCodec.get();
        require(refreshed != server, "HotSwap retained the obsolete codec instance");
        require(((int[]) codes.get(refreshed))[0] == 0, "Replacement is not the native wordpack");
        require(ChatCodec.get() == refreshed, "Compatible codec was unnecessarily reloaded");
        checkPrivateMessageDelivery(client, charset);
        System.out.println("Chat codec compatibility checks passed in both directions with the actual 443 client.");
        System.exit(0);
    }

    private static void checkPrivateMessageDelivery(Huffmans client, Charset charset) throws Exception {
        ServerSettings.clientBuild = 443;
        PacketDispatcher.registerHandlers();
        try (ServerSocketChannel listener = ServerSocketChannel.open(); Selector selector = Selector.open()) {
            listener.bind(new InetSocketAddress("127.0.0.1", 0));
            try (SocketChannel receiver = SocketChannel.open(listener.getLocalAddress());
                    SocketChannel transport = listener.accept()) {
                receiver.socket().setSoTimeout(2000);
                transport.configureBlocking(false);
                Player recipient = new Player(transport.register(selector, SelectionKey.OP_READ));
                recipient.setNameHash(2L);
                recipient.setOutboundCipher(new IsaacCipher(new int[4]));
                Player sender = new Player(null);
                sender.setNameHash(1L);
                IsaacCipher cipher = new IsaacCipher(new int[4]);
                DataInputStream input = new DataInputStream(receiver.socket().getInputStream());
                Player previous = World.players[1];
                World.players[1] = recipient;
                try {
                    int lastId = 0;
                    for (String text : new String[] {"h", "hi", "hello world!", "a private message!", repeat('e', 80)}) {
                        byte[] plain = text.getBytes(charset);
                        byte[] wire = new byte[1 + plain.length * 4];
                        wire[0] = (byte) plain.length;
                        int length = 1 + client.encode(plain, 0, wire, 1, plain.length);
                        ByteBuffer payload = ByteBuffer.allocate(8 + length);
                        payload.putLong(recipient.getNameHash()).put(wire, 0, length).flip();
                        PacketDispatcher.dispatchPacket(sender,
                                new IncomingPacket(50, payload.remaining(), PacketBuffer.wrapReader(payload)));
                        require(((input.readUnsignedByte() - cipher.nextInt()) & 255) == 25, "Wrong PM opcode");
                        byte[] packet = new byte[input.readUnsignedByte()];
                        input.readFully(packet);
                        ByteBuffer body = ByteBuffer.wrap(packet);
                        require(body.getLong() == sender.getNameHash(), "Wrong sender");
                        require(body.getShort() == 0, "Unexpected message ID high word");
                        int id = ((body.get() & 255) << 16) | ((body.get() & 255) << 8) | (body.get() & 255);
                        require(id > lastId, "Duplicate PM would be hidden by client");
                        lastId = id;
                        require(body.get() == sender.getPlayerRights(), "Wrong rights");
                        require((body.get() & 255) == plain.length, "Wrong PM text length");
                        byte[] received = new byte[plain.length];
                        client.decode(0, received, received.length, body.position(), packet);
                        require(Arrays.equals(plain, received), "PM delivery corrupted: " + text);
                    }
                } finally {
                    World.players[1] = previous;
                }
            }
        }
        System.out.println("Private messages passed: client encoding -> social handler -> socket -> client decoding, repeated IDs.");
    }

    private static String repeat(char value, int count) {
        char[] text = new char[count];
        Arrays.fill(text, value);
        return new String(text);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
