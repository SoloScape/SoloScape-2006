package unpackaged;

/* Class39_Sub3_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub3_Sub1 extends Class39_Sub3
{
    public int anInt1636;
    public int anInt1637;
    public int anInt1638;
    public byte[] aByteArray1639;
    public boolean aBoolean1640;
    
    public Class39_Sub3_Sub1 method455(Class43 class43) {
	aByteArray1639 = class43.method910(aByteArray1639, 32767);
	anInt1636 = class43.method908(102, anInt1636);
	if (anInt1638 == anInt1637)
	    anInt1638 = anInt1637 = class43.method906(anInt1638, 6);
	else {
	    anInt1638 = class43.method906(anInt1638, 6);
	    anInt1637 = class43.method906(anInt1637, 6);
	    if (anInt1638 == anInt1637)
		anInt1638--;
	}
	return this;
    }
    
    public Class39_Sub3_Sub1(int i, byte[] is, int i_0_, int i_1_) {
	anInt1636 = i;
	aByteArray1639 = is;
	anInt1638 = i_0_;
	anInt1637 = i_1_;
    }
}
