package unpackaged;

import jagex.world.actors.StillGraphic;

public final class BrowserAudioStartupChecks {
    public static void main(String[] args) throws Exception {
        System.setProperty("client.transport", "lan-websocket");
        // No Signlink, canvas, MIDI device or audio device is available in this check.
        Class39_Sub5_Sub9.signlink = null;
        Class41.aCanvas778 = null;
        Class26.anInt484 = 45;
        Class39_Sub12.method870((byte) 0);

        require(Class26.anInt484 == 50, "Browser startup did not advance to fonts");
        require(Class39_Sub7.anInt1387 == 35, "Loading progress did not advance");
        require(Class55.aClass62_1251 == null, "Browser startup opened a MIDI player");
        require(Class20.aClass13_395 != null
                && Class20.aClass13_395.getClass() == Class13.class,
                "Browser startup did not select silent audio output");
        require(Class15.anInt274 == 22050, "Silent mixer has an invalid sample rate");
        require(!StillGraphic.aBoolean2342, "Silent mixer should use mono samples");
        require(Class1.aClass39_Sub1_Sub1_32 != null, "Effect mixer was not initialized");
        require(ScriptState.aClass39_Sub1_439 == Class1.aClass39_Sub1_Sub1_32,
                "Effect mixer was not registered");
        require(ClientScript.aClass43_1693 != null, "Audio resampler was not initialized");
        // Later ticks and shutdown must still be safe with the silent output.
        Class20.aClass13_395.method193(0L);
        Class20.aClass13_395.method187();
        // Exercise the real game key listener, including soft-keyboard-only text.
        Class41.aCanvas778 = new java.awt.Canvas();
        jagex.world.actors.Npc.aClass35_2499 = new JKeyListener();
        Class41.aCanvas778.addKeyListener(jagex.world.actors.Npc.aClass35_2499);
        Class39_Sub5_Sub4_Sub4.anInt2299 = 0;
        jagex.io.Buffer.anInt1368 = 0;
        jagex.io.BufferedFile.anInt341 = 0;
        BrowserClientBootstrap.sendKey(0, 'a');
        BrowserClientBootstrap.sendKey(8, 8);
        BrowserClientBootstrap.sendKey(10, 10);
        java.awt.EventQueue.invokeAndWait(() -> { });
        require(Class39_Sub5_Sub4_Sub4.anInt2299 == 3,
                "Phone keyboard did not enqueue exactly three game inputs");
        require(Class46_Sub1.anIntArray1558[0] == 'a',
                "Phone typing did not reach the game character queue");
        require(Class46.anIntArray883[1] == 85,
                "Phone Backspace did not reach the game's delete key");
        require(Class46.anIntArray883[2] == 84,
                "Phone Enter did not reach the game's submit key");
        Class41.aCanvas778 = null;
        BrowserClientBootstrap.sendKey(0, 'b');
        java.awt.EventQueue.invokeAndWait(() -> { });
        require(Class39_Sub5_Sub4_Sub4.anInt2299 == 3,
                "Keyboard input should be ignored when no game canvas exists");
        System.out.println("Browser audio startup and phone keyboard checks passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
