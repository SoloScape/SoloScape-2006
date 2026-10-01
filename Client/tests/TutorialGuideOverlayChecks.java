package unpackaged;

import jagex.io.FrameBuffer;
import jagex.io.JSocket;
import jagex.utils.Huffmans;
import jagex.utils.SubNode;
import jagex.world.actors.StillGraphic;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;

/** Skill/quest guides must keep Tutorial Island instructions beneath them. */
public final class TutorialGuideOverlayChecks {
    public static void main(String[] args) throws Exception {
        Field mode = JSocket.class.getDeclaredField("tutorialInstructionMode");
        mode.setAccessible(true);
        for (int group : new int[] {275, 308}) {
            for (boolean tutorial : new boolean[] {false, true}) {
                mode.setBoolean(null, tutorial);
                Class39_Sub5_Sub4.widgetsLoaded = new boolean[500];
                Class62_Sub1.widgets = new Widget[500][];
                Class39_Sub5_Sub4.widgetsLoaded[group] = true;
                Class62_Sub1.widgets[group] = new Widget[0];
                Class39_Sub11.anInt1478 = -1;
                StillGraphic.anInt2338 = -1;
                SubNode.anInt1348 = -1;
                ClientScript.anInt1713 = -1;
                Class39_Sub5_Sub14.anInt1912 = 214;
                Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(32);
                int high = group >>> 8;
                int lowAdd = (group + 128) & 0xff;
                Class37.gameSocket = new JSocket(new Socket() {
                    public void setSoTimeout(int timeout) {}
                    public void setTcpNoDelay(boolean enabled) {}
                    public InputStream getInputStream() {
                        return new ByteArrayInputStream(new byte[] {(byte) high, (byte) lowAdd});
                    }
                    public OutputStream getOutputStream() { return new ByteArrayOutputStream(); }
                }, null);
                Class4.frameId = 160;
                Huffmans.frameSize = 2;
                require(Bzip2Block.readFrame(), "Guide packet was not handled for group " + group);
                require(Class39_Sub11.anInt1478 == group, "Guide did not open for group " + group);
                require(Class39_Sub5_Sub14.anInt1912 == (tutorial ? 214 : -1),
                        "Guide opening lost instructions or retained ordinary chatbox for group " + group);
                Class39_Sub5_Sub4.widgetsLoaded[group] = false;
                FrameBuffer.outgoingGameBuffer = new FrameBuffer(32);
                FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
                Class55.method999(31121);
                require(Class39_Sub11.anInt1478 == -1, "Guide did not close for group " + group);
                require(Class39_Sub5_Sub14.anInt1912 == (tutorial ? 214 : -1),
                        "Guide closing lost instructions for group " + group);
            }
        }
        System.out.println("Tutorial guide overlay checks passed for quest and skill guides.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
