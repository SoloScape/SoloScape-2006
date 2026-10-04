package unpackaged;

import jagex.io.FrameBuffer;
import jagex.utils.SubNode;

/** Ctrl-click uses the existing server teleport command with the picked terrain tile. */
public final class CtrlClickTeleport {
    private static boolean pending;
    private static int requestedCycle;

    private CtrlClickTeleport() {}

    public static boolean beginClick(int button, int screenX, int screenY) {
        if (button == 0) return false;
        pending = false;
        if (button != 1 || !Class13.aBooleanArray200[82]
                || screenX < 4 || screenX >= 516 || screenY < 4 || screenY >= 338
                || SubNode.anInt1348 != -1 || ClientScript.anInt1713 != -1
                || Class44.aClass38_836 == null) return false;
        pending = true;
        requestedCycle = Class2.logicCycle;
        Class39_Sub12.aBoolean1493 = false;
        Class44.aClass38_836.method355(screenX - 4, screenY - 4);
        return true;
    }

    static boolean completePick(int localX, int localY) {
        if (!pending) return false;
        pending = false;
        if (Class2.logicCycle - requestedCycle > 3 || localX < 0 || localY < 0
                || localX >= 104 || localY >= 104) return false;
        sendCommand("tele " + (Class65.anInt1145 + localX) + " "
                + (JKeyListener.anInt618 + localY) + " " + NameTable.height);
        sendCommand("pos");
        return true;
    }

    private static void sendCommand(String text) {
        FrameBuffer.outgoingGameBuffer.putFrame(174);
        FrameBuffer.outgoingGameBuffer.putByte(text.length() + 1);
        FrameBuffer.outgoingGameBuffer.putJstr((byte) 81, Class39_Sub5_Sub9.createJstring(text));
    }
}
