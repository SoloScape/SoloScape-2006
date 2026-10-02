package unpackaged;

import jagex.io.JSocket;

public final class JinglePlaybackChecks {
    public static void main(String[] args) {
        Playback playback = new Playback();
        Class55.aClass62_1251 = playback;
        Class39_Sub5_Sub4_Sub4.anInt2313 = 128;
        ArchiveWorker.anInt1205 = 42;
        Class37.method351(1, 320, 152);
        // A slow cache response must not let area music replace the request.
        tick(100);
        Class45.method916(43, 118);
        require(ArchiveWorker.anInt1205 == 43, "New area music was not remembered");
        require(Class39_Sub5_Sub4.anInt1730 == 152, "Area music replaced loading jingle");
        Class39_Sub5_Sub5.aBoolean1749 = false;
        Class39_Sub5_Sub6.aByteArray1768 = new byte[1];
        tick(30);
        Class39_Sub5_Sub6.aByteArray1768 = null;
        playback.playing = true;
        tick(480); // Quest MIDI lasts 9.6 seconds, well beyond its 320ms packet.
        playback.playing = false;
        ClientScript.method477((byte) 0);
        require(JSocket.anInt313 == 0, "Finished jingle kept music locked");
        require(Class39_Sub5_Sub4.anInt1730 == 43, "Latest area music did not resume");

        Class37.method351(1, 0, 69);
        require(JSocket.anInt313 > 0, "Zero-duration jingle did not lock music");
        tick(5);
        Class39_Sub5_Sub5.aBoolean1749 = false;
        ClientScript.method477((byte) 0);
        require(JSocket.anInt313 == 0, "Failed playback did not release music");

        Class37.method351(1, 320, 152);
        Class55.aClass62_1251 = null;
        for (int i = 0; i < 16; i++) ClientScript.method477((byte) 0);
        require(JSocket.anInt313 == 0, "Missing audio backend kept music locked");
        System.out.println("Jingle playback checks passed (loading, full playback, area changes, zero duration, unavailable audio).");
    }

    private static void tick(int count) {
        for (int i = 0; i < count; i++) {
            ClientScript.method477((byte) 0);
            require(JSocket.anInt313 > 0, "Background music resumed before jingle finished");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static final class Playback extends Class62 {
        boolean playing;
        public boolean isPlaying() { return playing; }
        public void destroy() {}
        public void method1048(boolean value) {}
        public void method1051(int volume, byte[] bytes, byte value, boolean loop) {}
        public void method1053(int volume, byte value) {}
        public void method1054(int value) {}
        public void method1055(int a, int b, int c) {}
    }
}

