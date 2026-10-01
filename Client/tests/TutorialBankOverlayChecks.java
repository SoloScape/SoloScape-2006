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

/** Exercise the bank-open packet and close action with and without instructions. */
public final class TutorialBankOverlayChecks {
    public static void main(String[] args) throws Exception {
        Field mode = JSocket.class.getDeclaredField("tutorialInstructionMode");
        mode.setAccessible(true);
        for (boolean tutorial : new boolean[] {false, true}) {
            mode.setBoolean(null, tutorial);
            Class39_Sub5_Sub4.widgetsLoaded = new boolean[500];
            Class62_Sub1.widgets = new Widget[500][];
            for (int group : new int[] {12, 15}) {
                Class39_Sub5_Sub4.widgetsLoaded[group] = true;
                Class62_Sub1.widgets[group] = new Widget[0];
            }
            Class39_Sub11.anInt1478 = -1;
            StillGraphic.anInt2338 = -1;
            SubNode.anInt1348 = -1;
            ClientScript.anInt1713 = -1;
            Class39_Sub5_Sub14.anInt1912 = 214;
            Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(32);
            Class37.gameSocket = new JSocket(new Socket() {
                public void setSoTimeout(int timeout) {}
                public void setTcpNoDelay(boolean enabled) {}
                public InputStream getInputStream() {
                    return new ByteArrayInputStream(new byte[] {(byte) 140, 0, 0, 15});
                }
                public OutputStream getOutputStream() { return new ByteArrayOutputStream(); }
            }, null);
            Class4.frameId = 146;
            Huffmans.frameSize = 4;
            require(Bzip2Block.readFrame(), "Bank packet was not handled");
            require(Class39_Sub11.anInt1478 == 12 && StillGraphic.anInt2338 == 15,
                    "Bank or inventory did not open");
            require(Class39_Sub5_Sub14.anInt1912 == (tutorial ? 214 : -1),
                    "Bank opening lost instructions or retained ordinary chatbox");
            Class39_Sub5_Sub4.widgetsLoaded[12] = false;
            Class39_Sub5_Sub4.widgetsLoaded[15] = false;
            FrameBuffer.outgoingGameBuffer = new FrameBuffer(32);
            FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
            Class55.method999(31121);
            require(Class39_Sub11.anInt1478 == -1 && StillGraphic.anInt2338 == -1,
                    "Bank did not close");
            require(Class39_Sub5_Sub14.anInt1912 == (tutorial ? 214 : -1),
                    "Bank closing lost instructions");
        }
        System.out.println("Tutorial bank packet and close checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
