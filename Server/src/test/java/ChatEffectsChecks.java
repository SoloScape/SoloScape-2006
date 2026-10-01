import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.player.Player;
import com.rs2.model.player.ModernPlayerUpdateTask;
import com.rs2.model.quest.QuestDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import com.rs2.net.packet.handler.PublicChatPacketHandler;
import com.rs2.util.ChatCodec;
import java.nio.ByteBuffer;
import java.lang.reflect.Method;

public final class ChatEffectsChecks {
    public static void main(String[] args) throws Exception {
        ServerSettings.clientBuild = 443;
        QuestDefinition.loadDefinitions();
        Interfaces.load();
        Interfaces.Component toggle = Interfaces.forId(261, 1);
        require(toggle.actionType == 4 && new String(toggle.getData(), "ISO-8859-1")
                .contains("Toggle Chat Effects"), "Wrong cache button");
        Player player = new Player(null);
        player.setOutboundCipher(new IsaacCipher(new int[4]));
        player.setQuestState(0, 1);
        // No transport is necessary: writePacketBuffer discards packets for this player.
        player.isBot = true;
        for (int setting : new int[] {0, 1}) {
            player.setPublicChatEffects(setting);
            for (int colour = 0; colour <= 11; colour++) for (int effect = 0; effect <= 5; effect++) {
                byte[] text = ChatCodec.get().encode("Hello!");
                ByteBuffer payload = ByteBuffer.allocate(text.length + 2);
                payload.put((byte) colour).put((byte) effect).put(text).flip();
                new PublicChatPacketHandler().handle(player, new IncomingPacket(ClientPackets.PUBLIC_CHAT,
                        payload.remaining(), PacketBuffer.wrapReader(payload)));
                require(player.getPublicChatEffects() == setting, "Message changed saved toggle");
                require(player.getPublicChatAnimation() == effect, "Animation wasn't received");
                Method write = ModernPlayerUpdateTask.class.getDeclaredMethod("writePublicChat", Player.class, PacketWriter.class);
                write.setAccessible(true);
                PacketWriter writer = PacketBuffer.allocateWriter(256);
                write.invoke(null, player, writer);
                ByteBuffer mask = writer.getBuffer();
                mask.flip();
                require((mask.getShort() & 0xffff) == (colour << 8 | effect), "Incorrect update metadata");
            }
            ByteBuffer click = ByteBuffer.allocate(4).putInt(261 << 16 | 1);
            click.flip();
            new InterfaceActionPacketHandler().handle(player, new IncomingPacket(ClientPackets.INTERFACE_BUTTON,
                    4, PacketBuffer.wrapReader(click)));
            require(player.getPublicChatEffects() == 1 - setting, "Button didn't toggle preference");
        }
        player.setPublicChatEffects(0);
        player.queuePublicChatMessage("Hello", 1, 3);
        require(player.getPublicChatEffects() == 0 && player.getPublicChatAnimation() == 3,
                "Queued chat changed viewer preference");
        System.out.println("Chat effects checks passed: cache button, all received/update metadata, preference isolation.");
        System.exit(0);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
