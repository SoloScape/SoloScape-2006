package unpackaged;

import jagex.io.Buffer;

/* Class51 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class51
{
    public Class39_Sub12 aClass39_Sub12_938 = null;
    public static int[] anIntArray939;
    public boolean aBoolean940 = false;
    public static int[] anIntArray941;
    public int[] anIntArray942;
    public static int[] anIntArray943 = new int[500];
    public int[] anIntArray944;
    public int[] anIntArray945;
    public int[] anIntArray946;
    public static int[] anIntArray947;
    public int anInt948 = -1;
    
    public static void method977() {
	anIntArray939 = null;
	anIntArray941 = null;
	anIntArray943 = null;
	anIntArray947 = null;
    }
    
    public Class51(byte[] is, Class39_Sub12 class39_sub12) {
	aClass39_Sub12_938 = class39_sub12;
	Buffer class39_sub6 = new Buffer(is);
	Buffer class39_sub6_0_ = new Buffer(is);
	class39_sub6.offset = 2;
	int i = class39_sub6.getUbyte();
	int i_1_ = -1;
	int i_2_ = 0;
	class39_sub6_0_.offset = class39_sub6.offset + i;
	for (int i_3_ = 0; i_3_ < i; i_3_++) {
	    int i_4_ = class39_sub6.getUbyte();
	    if (i_4_ > 0) {
		if (aClass39_Sub12_938.anIntArray1497[i_3_] != 0) {
		    for (int i_5_ = i_3_ - 1; i_5_ > i_1_; i_5_--) {
			if (aClass39_Sub12_938.anIntArray1497[i_5_] == 0) {
			    anIntArray939[i_2_] = i_5_;
			    anIntArray941[i_2_] = 0;
			    anIntArray943[i_2_] = 0;
			    anIntArray947[i_2_] = 0;
			    i_2_++;
			    break;
			}
		    }
		}
		anIntArray939[i_2_] = i_3_;
		int i_6_ = 0;
		if (aClass39_Sub12_938.anIntArray1497[i_3_] == 3)
		    i_6_ = 128;
		if ((i_4_ & 0x1) != 0)
		    anIntArray941[i_2_]
			= class39_sub6_0_.getSmartA();
		else
		    anIntArray941[i_2_] = i_6_;
		if ((i_4_ & 0x2) != 0)
		    anIntArray943[i_2_] = class39_sub6_0_.getSmartA();
		else
		    anIntArray943[i_2_] = i_6_;
		if ((i_4_ & 0x4) != 0)
		    anIntArray947[i_2_]
			= class39_sub6_0_.getSmartA();
		else
		    anIntArray947[i_2_] = i_6_;
		i_1_ = i_3_;
		i_2_++;
		if (aClass39_Sub12_938.anIntArray1497[i_3_] == 5)
		    aBoolean940 = true;
	    }
	}
	if (class39_sub6_0_.offset != is.length)
	    throw new RuntimeException();
	anInt948 = i_2_;
	anIntArray945 = new int[i_2_];
	anIntArray942 = new int[i_2_];
	anIntArray946 = new int[i_2_];
	anIntArray944 = new int[i_2_];
	for (int i_7_ = 0; i_7_ < i_2_; i_7_++) {
	    anIntArray945[i_7_] = anIntArray939[i_7_];
	    anIntArray942[i_7_] = anIntArray941[i_7_];
	    anIntArray946[i_7_] = anIntArray943[i_7_];
	    anIntArray944[i_7_] = anIntArray947[i_7_];
	}
    }
    
    static {
	anIntArray941 = new int[500];
	anIntArray939 = new int[500];
	anIntArray947 = new int[500];
    }
}
