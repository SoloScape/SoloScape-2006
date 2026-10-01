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
import java.net.Socket;

/** Generic closes must not unload the tutorial HUD, including stair teleport cleanup. */
public final class TutorialChatboxLifecycleChecks {
    public static void main(String[] args) throws Exception {
        for (boolean tutorial : new boolean[] {false, true}) {
            for (int opcode : new int[] {178, 149, 160, 146, 221, 32, 51}) {
                reset(tutorial);
                byte[] payload = opcode == 149 || opcode == 160
                        ? new byte[] {1, (byte) (300 + 128)}
                        : opcode == 146 ? new byte[] {(byte) 140, 0, 0, 15}
                        : opcode == 221 ? new byte[] {1, 44, (byte) 143, 0}
                        : new byte[0];
                if (opcode == 221) Class31.state = 35;
                if (opcode == 178) {
                    Class39_Sub11.anInt1478 = 275;
                    StillGraphic.anInt2338 = 15;
                    Class39_Sub5_Sub4.widgetsLoaded[15] = false;
                }
                readPacket(opcode, payload);
                checkChatbox(tutorial, "packet " + opcode);
                if (opcode == 178) {
                    require(Class39_Sub11.anInt1478 == -1 && StillGraphic.anInt2338 == -1,
                            "Generic close left another interface open");
                    // Repeat the same cleanup used by successive up/down stair traversals.
                    readPacket(opcode, payload);
                    checkChatbox(tutorial, "repeated stair cleanup");
                }
            }
            for (int viewport : new int[] {-1, 12, 269, 275, 300, 308, 312, 465}) {
                reset(tutorial);
                Class39_Sub11.anInt1478 = viewport;
                if (viewport >= 0) Class39_Sub5_Sub4.widgetsLoaded[viewport] = false;
                Class55.method999(31121);
                require(Class39_Sub11.anInt1478 == -1, "Local close left viewport open");
                checkChatbox(tutorial, "local close of " + viewport);
            }
        }
        reset(true);
        // NPC dialogue must still replace the instructions and close normally.
        JSocket.sanitizeTutorialChatText((214 << 16) | 5,
                Class39_Sub5_Sub9.createJstring("__tutorial_dialogue__"));
        Class39_Sub5_Sub14.anInt1912 = 241;
        readPacket(178, new byte[0]);
        require(Class39_Sub5_Sub14.anInt1912 == -1, "NPC dialogue became impossible to close");

        reset(true);
        // Finishing Tutorial Island releases group 214 back to ordinary dialogue/chat.
        JSocket.sanitizeTutorialChatText((214 << 16) | 5,
                Class39_Sub5_Sub9.createJstring("__dialogue__"));
        Class39_Sub5_Sub4.widgetsLoaded[214] = false;
        readPacket(178, new byte[0]);
        checkChatbox(false, "tutorial exit");

        reset(false);
        Class55.characterDesignActive = true;
        Class55.method999(31121);
        require(Class39_Sub5_Sub14.anInt1912 == 214,
                "Character design lost instructions before the login marker arrived");
        System.out.println("Tutorial chatbox lifecycle checks passed.");
    }

    private static void reset(boolean tutorial) {
        Class39_Sub5_Sub4.widgetsLoaded = new boolean[500];
        Class62_Sub1.widgets = new Widget[500][];
        for (int group : new int[] {12, 15, 300}) {
            Class39_Sub5_Sub4.widgetsLoaded[group] = true;
            Class62_Sub1.widgets[group] = new Widget[0];
        }
        Class62_Sub1.widgets[214] = new Widget[0];
        JSocket.sanitizeTutorialChatText((214 << 16) | 5,
                Class39_Sub5_Sub9.createJstring(tutorial ? "__tutorial__" : "__dialogue__"));
        // An accidental unload of live instructions would touch the absent cache loader.
        Class39_Sub5_Sub4.widgetsLoaded[214] = tutorial;
        Class39_Sub11.anInt1478 = -1;
        StillGraphic.anInt2338 = -1;
        SubNode.anInt1348 = -1;
        ClientScript.anInt1713 = -1;
        Class39_Sub5_Sub14.anInt1912 = 214;
        Class39_Sub5_Sub4_Sub4.anInt2285 = 0;
        Class55.characterDesignActive = false;
        FrameBuffer.outgoingGameBuffer = new FrameBuffer(32);
        FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
    }

    private static void readPacket(int opcode, final byte[] payload) throws Exception {
        Class39_Sub5_Sub11.gameBuffer = new FrameBuffer(32);
        Class37.gameSocket = new JSocket(new Socket() {
            public void setSoTimeout(int timeout) {}
            public void setTcpNoDelay(boolean enabled) {}
            public InputStream getInputStream() {
                return new ByteArrayInputStream(payload.length == 0 ? new byte[] {0} : payload);
            }
            public OutputStream getOutputStream() { return new ByteArrayOutputStream(); }
        }, null);
        Class4.frameId = opcode;
        Huffmans.frameSize = payload.length;
        require(Bzip2Block.readFrame(), "Packet not handled: " + opcode);
        require(Class4.frameId == -1, "Packet failed before completion: " + opcode);
    }

    private static void checkChatbox(boolean tutorial, String action) {
        require(Class39_Sub5_Sub14.anInt1912 == (tutorial ? 214 : -1),
                "Incorrect chatbox after " + action);
        if (tutorial) {
            require(Class39_Sub5_Sub4.widgetsLoaded[214], "Instructions unloaded after " + action);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
