package unpackaged;

import java.nio.charset.StandardCharsets;
import jagex.io.FrameBuffer;
import jagex.utils.IsaacPrng;
import jagex.utils.SubNode;

public final class CtrlClickTeleportChecks {
    public static void main(String[] args) {
        Class44.aClass38_836 = new Class38(4,104,104,new int[4][105][105]);
        SubNode.anInt1348 = -1;
        ClientScript.anInt1713 = -1;
        Class65.anInt1145 = 2800;
        JKeyListener.anInt618 = 3100;
        NameTable.height = 2;
        Class2.logicCycle = 100;
        FrameBuffer.outgoingGameBuffer = new FrameBuffer(256);
        FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
        Class13.aBooleanArray200[82] = false;
        require(!CtrlClickTeleport.beginClick(1,250,170), "Normal click intercepted");
        Class13.aBooleanArray200[82] = true;
        require(!CtrlClickTeleport.beginClick(2,250,170), "Right click intercepted");
        require(!CtrlClickTeleport.beginClick(1,600,170), "Sidebar click intercepted");
        require(!CtrlClickTeleport.beginClick(1,250,400), "Chat click intercepted");
        SubNode.anInt1348 = 123;
        require(!CtrlClickTeleport.beginClick(1,250,170), "Full-screen interface click intercepted");
        SubNode.anInt1348 = -1;
        require(CtrlClickTeleport.beginClick(1,250,170), "Ctrl-click not accepted");
        require(Class38.anInt675 == 246 && Class38.anInt697 == 166, "Wrong terrain picking coordinates");
        // A modifier release between the click and the next scene render must not lose the teleport.
        Class13.aBooleanArray200[82] = false;
        Class2.logicCycle++;
        require(CtrlClickTeleport.completePick(40,45), "Terrain pick not teleported");
        FrameBuffer sent = FrameBuffer.outgoingGameBuffer;
        IsaacPrng decoder = new IsaacPrng(new int[4]);
        int offset = checkCommand(sent,0,decoder,"tele 2840 3145 2");
        offset = checkCommand(sent,offset,decoder,"pos");
        require(offset == sent.offset, "Unexpected additional packet");
        require(!CtrlClickTeleport.completePick(40,45), "Pick teleported twice");
        Class13.aBooleanArray200[82] = true;
        CtrlClickTeleport.beginClick(1,250,170);
        Class2.logicCycle += 4;
        require(!CtrlClickTeleport.completePick(41,46), "Stale empty-space pick teleported later click");
        CtrlClickTeleport.beginClick(1,250,170);
        CtrlClickTeleport.beginClick(2,250,170);
        require(!CtrlClickTeleport.completePick(41,46), "Right click did not cancel pending teleport");
        require(sent.offset == offset, "Cancelled click sent commands");
        System.out.println("Ctrl-click teleport checks passed: terrain picking, exact world tile/plane, tele+pos packets, modifier release, normal clicks, UI bounds and stale picks.");
    }
    private static int checkCommand(FrameBuffer packet,int offset,IsaacPrng cipher,String text) {
        require(((packet.payload[offset++] & 255) - cipher.getNextValue() & 255) == 174, "Incorrect command opcode");
        int length = packet.payload[offset++] & 255;
        require(length == text.length()+1, "Wrong command packet length");
        require(new String(packet.payload,offset,length-1,StandardCharsets.ISO_8859_1).equals(text), "Wrong command text");
        require(packet.payload[offset+length-1] == 0, "Command is not null terminated");
        return offset+length;
    }
    private static void require(boolean condition,String message) {
        if (!condition) throw new AssertionError(message);
    }
}
