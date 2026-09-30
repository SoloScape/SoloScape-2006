package unpackaged;

import jagex.graphics.AbstractImage;
import jagex.world.actors.StillGraphic;
import jagex.world.actors.Projectile;
import jagex.utils.SubNode;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.Buffer;

/* Class39_Sub5_Sub18 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub5_Sub18 extends SubNode
{
    public static JString aClass3_2118
	= Class39_Sub5_Sub9.createJstring(" (X");
    public static int anInt2119 = 0;
    public static int anInt2120;
    public static int anInt2121;
    public int anInt2122;
    public static JString aClass3_2123;
    public static int anInt2124;
    public static JString aClass3_2125
	= Class39_Sub5_Sub9.createJstring("backvmid1");
    public int anInt2126;
    public int anInt2127 = 0;
    public int anInt2128;
    public static JString aClass3_2129;
    public static AbstractImage aClass57_2130;
    public static JString aClass3_2131;
    public int anInt2132;
    public int anInt2133 = 128;
    public static JString aClass3_2134;
    public static JString aClass3_2135;
    public int[] anIntArray2136;
    public int anInt2137;
    public static JString aClass3_2138;
    public int anInt2139;
    public int[] anIntArray2140;
    
    public static void method776(byte i) {
	synchronized (Class39_Sub13.aClass42_1502) {
	    Class30.anInt541 = ClientScript.anInt1699;
	    IsaacPrng.anInt1091 = Projectile.anInt2208;
	    Class33.anInt599 = Class10.anInt172;
	    Class46.anInt887 = Class23.anInt430;
	    Class39_Sub4.anInt1329 = JMouseListener.anInt791;
	    Bzip2Block.anInt1054 = Class41.anInt784;
	    StillGraphic.aLong2331 = Class2.aLong55;
	    Class23.anInt430 = 0;
	}
    }
    
    public void method777(Buffer class39_sub6, int i, int i_0_) {
	if (i != 1) {
	    if (i != 2) {
		if (i != 4) {
		    if (i != 5) {
			if (i == 6)
			    anInt2128 = class39_sub6.getUword();
			else if (i == 7)
			    anInt2127 = class39_sub6.getUbyte();
			else if (i != 8) {
			    if (i >= 40 && i < 50)
				anIntArray2140[i - 40]
				    = class39_sub6.getUword();
			    else if (i >= 50 && i < 60)
				anIntArray2136[i - 50]
				    = class39_sub6.getUword();
			} else
			    anInt2132 = class39_sub6.getUbyte();
		    } else
			anInt2139 = class39_sub6.getUword();
		} else
		    anInt2133 = class39_sub6.getUword();
	    } else
		anInt2126 = class39_sub6.getUword();
	} else
	    anInt2122 = class39_sub6.getUword();
	int i_1_ = 43 / ((i_0_ + 61) / 47);
    }
    
    public Model method778(int i, int i_2_) {
	Model class39_sub5_sub4_sub6
	    = ((Model)
	       Class39_Sub5_Sub16.aClass7_1975.get((long) anInt2137));
	if (class39_sub5_sub4_sub6 == null) {
	    class39_sub5_sub4_sub6
		= Model
		      .getModel(Class39_Sub5_Sub5.aClass9_1740, anInt2122, 0);
	    if (class39_sub5_sub4_sub6 == null)
		return null;
	    for (int i_3_ = 0; i_3_ < 6; i_3_++) {
		if (anIntArray2140[0] != 0)
		    class39_sub5_sub4_sub6.method563(anIntArray2140[i_3_],
						     anIntArray2136[i_3_]);
	    }
	    class39_sub5_sub4_sub6.method553();
	    class39_sub5_sub4_sub6.method548(64 + anInt2127, anInt2132 + 850,
					     -30, -50, -30, true);
	    Class39_Sub5_Sub16.aClass7_1975.put(class39_sub5_sub4_sub6,
						      (long) anInt2137,
						      (byte) -119);
	}
	if (i != 180)
	    return null;
	Model class39_sub5_sub4_sub6_4_;
	if (anInt2126 != -1 && i_2_ != -1)
	    class39_sub5_sub4_sub6_4_
		= Class62_Sub1.method1064(anInt2126, (byte) 54)
		      .method705(class39_sub5_sub4_sub6, i_2_, i ^ ~0xc4);
	else
	    class39_sub5_sub4_sub6_4_ = class39_sub5_sub4_sub6.method547(true);
	if (anInt2133 != 128 || anInt2139 != 128)
	    class39_sub5_sub4_sub6_4_.method559(anInt2133, anInt2139,
						anInt2133);
	if (anInt2128 != 0) {
	    if (anInt2128 == 90)
		class39_sub5_sub4_sub6_4_.method555();
	    if (anInt2128 == 180) {
		class39_sub5_sub4_sub6_4_.method555();
		class39_sub5_sub4_sub6_4_.method555();
	    }
	    if (anInt2128 == 270) {
		class39_sub5_sub4_sub6_4_.method555();
		class39_sub5_sub4_sub6_4_.method555();
		class39_sub5_sub4_sub6_4_.method555();
	    }
	}
	return class39_sub5_sub4_sub6_4_;
    }
    
    public void method779(int i, Buffer class39_sub6) {
	for (;;) {
	    int i_5_ = class39_sub6.getUbyte();
	    if (i_5_ == 0)
		break;
	    method777(class39_sub6, i_5_, 86);
	}
    }
    
    public static void method780(boolean bool) {
	aClass3_2138 = null;
	aClass3_2129 = null;
	aClass3_2123 = null;
	aClass3_2135 = null;
	aClass3_2131 = null;
	aClass3_2118 = null;
	aClass3_2134 = null;
	aClass3_2125 = null;
	aClass57_2130 = null;
    }
    
    public Class39_Sub5_Sub18() {
	anInt2132 = 0;
	anIntArray2136 = new int[6];
	anInt2126 = -1;
	anInt2128 = 0;
	anIntArray2140 = new int[6];
	anInt2139 = 128;
    }
    
    static {
	aClass3_2123 = Class39_Sub5_Sub9.createJstring("::errortest");
	aClass3_2134 = Class39_Sub5_Sub9.createJstring("(U");
	anInt2124 = 0;
	aClass3_2129 = Class39_Sub5_Sub9.createJstring("Weiter");
	aClass3_2131
	    = Class39_Sub5_Sub9.createJstring("Gegenstand f-Ur Mitglieder");
	aClass3_2135 = Class39_Sub5_Sub9.createJstring("Benutzen");
	aClass3_2138 = Class39_Sub5_Sub9.createJstring("::clientdrop");
    }
}
