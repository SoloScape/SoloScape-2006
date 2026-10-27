package com.rs2.net.packet.handler;

import com.rs2.model.player.Player;
import com.rs2.net.packet.IncomingPacket;
import com.rs2.net.packet.PacketBuffer;
import com.rs2.net.packet.PacketHandler;
import com.rs2.net.packet.PacketWriter;

/** Echoes an FPS-overlay ping ID, so the client can measure real server RTT. */
public final class PingPacketHandler implements PacketHandler {
    private static final int PING_OPCODE = 254;

    @Override
    public void handle(Player player, IncomingPacket packet) {
        if (packet.getLength() != 4 || player.isBot) {
            return;
        }
        int pingId = packet.getReader().readInt();
        PacketWriter reply = PacketBuffer.allocateWriter(5);
        reply.writeOpcode(player.getOutboundCipher(), PING_OPCODE);
        reply.writeInt(pingId);
        player.writePacketBuffer(reply.getBuffer());
    }
}
