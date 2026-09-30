package unpackaged;

/* Class13_Sub2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import sun.audio.AudioPlayer;

public class Class13_Sub2 extends Class13
{
    public InputStream_Sub1 anInputStream_Sub1_1318 = new InputStream_Sub1();
    
    public Class13_Sub2() {
	AudioPlayer.player.start(anInputStream_Sub1_1318);
    }
    
    public void method187() {
	AudioPlayer.player.stop(anInputStream_Sub1_1318);
	synchronized (anInputStream_Sub1_1318) {
	    anInputStream_Sub1_1318.aBoolean27 = true;
	}
    }
}
