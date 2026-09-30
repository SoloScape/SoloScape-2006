package unpackaged;

import jagex.graphics.JImage;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;

/* Class39_Sub5_Sub11 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub5_Sub11 extends SubNode
{
    public int anInt1825;
    public int anInt1826;
    public static int anInt1827;
    public static int anInt1828 = 2;
    public int anInt1829 = 99;
    public int anInt1830;
    public int[] anIntArray1831;
    public int[] anIntArray1832;
    public int[] anIntArray1833;
    public static FrameBuffer gameBuffer;
    public int[] anIntArray1835;
    public int anInt1836;
    public int anInt1837;
    public boolean aBoolean1838;
    public int anInt1839;
    public int anInt1840;
    public static int anInt1841;
    public static JString aClass3_1842;
    public static int anInt1843;
    public static Cache npcDefinitionCache;
    public static JString aClass3_1845;
    public static JString aClass3_1846;
    public static int[] anIntArray1847;
    
    public void method698(Buffer class39_sub6, int i) {
	for (;;) {
	    int i_0_ = class39_sub6.getUbyte();
	    if (i_0_ == 0)
		break;
	    method700(class39_sub6, i_0_, 0);
	}
    }
    
    public static int method699(byte i) {
	return Class39_Sub5_Sub7.anInt1779++;
    }
    
    public void method700(Buffer class39_sub6, int i, int i_1_) {
	if (i_1_ == 0) {
	    if (i == 1) {
		int i_2_ = class39_sub6.getUbyte();
		anIntArray1831 = new int[i_2_];
		for (int i_3_ = 0; i_3_ < i_2_; i_3_++)
		    anIntArray1831[i_3_] = class39_sub6.getUword();
		anIntArray1833 = new int[i_2_];
		for (int i_4_ = 0; i_2_ > i_4_; i_4_++)
		    anIntArray1833[i_4_]
			= class39_sub6.getUword();
		for (int i_5_ = 0; i_5_ < i_2_; i_5_++)
		    anIntArray1833[i_5_] = ((class39_sub6.getUword() << 16)
					    + anIntArray1833[i_5_]);
	    } else if (i != 2) {
		if (i == 3) {
		    int i_6_ = class39_sub6.getUbyte();
		    anIntArray1835 = new int[i_6_ + 1];
		    for (int i_7_ = 0; i_6_ > i_7_; i_7_++)
			anIntArray1835[i_7_]
			    = class39_sub6.getUbyte();
		    anIntArray1835[i_6_] = 9999999;
		} else if (i == 4)
		    aBoolean1838 = true;
		else if (i == 5)
		    anInt1826 = class39_sub6.getUbyte();
		else if (i == 6)
		    anInt1825 = class39_sub6.getUword();
		else if (i != 7) {
		    if (i == 8)
			anInt1829 = class39_sub6.getUbyte();
		    else if (i != 9) {
			if (i != 10) {
			    if (i != 11) {
				if (i == 12) {
				    int i_8_
					= class39_sub6.getUbyte();
				    anIntArray1832 = new int[i_8_];
				    for (int i_9_ = 0; i_8_ > i_9_; i_9_++)
					anIntArray1832[i_9_]
					    = class39_sub6.getUword();
				    for (int i_10_ = 0; i_8_ > i_10_; i_10_++)
					anIntArray1832[i_10_]
					    = (class39_sub6.getUword()
					       << 16) + anIntArray1832[i_10_];
				}
			    } else
				anInt1830 = class39_sub6.getUbyte();
			} else
			    anInt1837 = class39_sub6.getUbyte();
		    } else
			anInt1840 = class39_sub6.getUbyte();
		} else
		    anInt1836 = class39_sub6.getUword();
	    } else
		anInt1839 = class39_sub6.getUword();
	}
    }
    
    public Model method701
	(int i, Model class39_sub5_sub4_sub6,
	 Class39_Sub5_Sub11 class39_sub5_sub11_11_, int i_12_, int i_13_) {
	i = anIntArray1833[i];
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = Class55.method1001((byte) 110, i >> 16);
	i &= 0xffff;
	if (class39_sub5_sub12 == null)
	    return class39_sub5_sub11_11_.method703(class39_sub5_sub4_sub6,
						    i_13_, true);
	i_13_ = class39_sub5_sub11_11_.anIntArray1833[i_13_];
	Class39_Sub5_Sub12 class39_sub5_sub12_14_
	    = Class55.method1001((byte) 81, i_13_ >> i_12_);
	i_13_ &= 0xffff;
	if (class39_sub5_sub12_14_ == null) {
	    Model class39_sub5_sub4_sub6_15_
		= class39_sub5_sub4_sub6
		      .method567(!class39_sub5_sub12.method710(i, (byte) 52));
	    class39_sub5_sub4_sub6_15_.method557(class39_sub5_sub12, i);
	    return class39_sub5_sub4_sub6_15_;
	}
	Model class39_sub5_sub4_sub6_16_
	    = (class39_sub5_sub4_sub6.method567
	       (!class39_sub5_sub12.method710(i, (byte) 52)
		& !class39_sub5_sub12_14_.method710(i_13_, (byte) 52)));
	class39_sub5_sub4_sub6_16_.method538(class39_sub5_sub12, i,
					     class39_sub5_sub12_14_, i_13_,
					     anIntArray1835);
	return class39_sub5_sub4_sub6_16_;
    }
    
    public void method702(byte i) {
	if (anInt1837 == -1) {
	    if (anIntArray1835 != null)
		anInt1837 = 2;
	    else
		anInt1837 = 0;
	}
	if (anInt1840 == -1) {
	    if (anIntArray1835 != null)
		anInt1840 = 2;
	    else
		anInt1840 = 0;
	}
    }
    
    public Model method703
	(Model class39_sub5_sub4_sub6, int i, boolean bool) {
	i = anIntArray1833[i];
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = Class55.method1001((byte) 90, i >> 16);
	i &= 0xffff;
	if (class39_sub5_sub12 == null)
	    return class39_sub5_sub4_sub6.method567(true);
	Model class39_sub5_sub4_sub6_17_
	    = class39_sub5_sub4_sub6
		  .method567(!class39_sub5_sub12.method710(i, (byte) 52));
	class39_sub5_sub4_sub6_17_.method557(class39_sub5_sub12, i);
	return class39_sub5_sub4_sub6_17_;
    }
    
    public static void method704(byte i) {
	anIntArray1847 = null;
	aClass3_1842 = null;
	aClass3_1845 = null;
	aClass3_1846 = null;
	npcDefinitionCache = null;
	gameBuffer = null;
    }
    
    public Model method705
	(Model class39_sub5_sub4_sub6, int i, int i_18_) {
	i = anIntArray1833[i];
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = Class55.method1001((byte) 94, i >> 16);
	i &= 0xffff;
	if (class39_sub5_sub12 == null)
	    return class39_sub5_sub4_sub6.method547(true);
	Model class39_sub5_sub4_sub6_19_
	    = class39_sub5_sub4_sub6
		  .method547(!class39_sub5_sub12.method710(i, (byte) 52));
	class39_sub5_sub4_sub6_19_.method557(class39_sub5_sub12, i);
	return class39_sub5_sub4_sub6_19_;
    }
    
    public Model method706
	(int i, Model class39_sub5_sub4_sub6, int i_20_) {
	int i_21_ = anIntArray1833[i_20_];
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = Class55.method1001((byte) 113, i_21_ >> 16);
	i_21_ &= 0xffff;
	if (class39_sub5_sub12 == null)
	    return class39_sub5_sub4_sub6.method567(true);
	Class39_Sub5_Sub12 class39_sub5_sub12_22_ = null;
	int i_23_ = 0;
	if (anIntArray1832 != null && i_20_ < anIntArray1832.length) {
	    i_23_ = anIntArray1832[i_20_];
	    class39_sub5_sub12_22_
		= Class55.method1001((byte) 85, i_23_ >> 16);
	    i_23_ &= 0xffff;
	}
	if (class39_sub5_sub12_22_ == null || i_23_ == 65535) {
	    Model class39_sub5_sub4_sub6_24_
		= (class39_sub5_sub4_sub6.method567
		   (!class39_sub5_sub12.method710(i_21_, (byte) 52)));
	    class39_sub5_sub4_sub6_24_.method557(class39_sub5_sub12, i_21_);
	    return class39_sub5_sub4_sub6_24_;
	}
	Model class39_sub5_sub4_sub6_25_
	    = (class39_sub5_sub4_sub6.method567
	       (!class39_sub5_sub12.method710(i_21_, (byte) 52)
		& !class39_sub5_sub12_22_.method710(i_23_, (byte) 52)));
	class39_sub5_sub4_sub6_25_.method557(class39_sub5_sub12, i_21_);
	class39_sub5_sub4_sub6_25_.method557(class39_sub5_sub12_22_, i_23_);
	return class39_sub5_sub4_sub6_25_;
    }
    
    public Model method707
	(Model class39_sub5_sub4_sub6, int i, int i_26_,
	 int i_27_) {
	i_26_ = anIntArray1833[i_26_];
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = Class55.method1001((byte) 109, i_26_ >> 16);
	i_26_ &= 0xffff;
	if (class39_sub5_sub12 == null)
	    return class39_sub5_sub4_sub6.method567(true);
	Model class39_sub5_sub4_sub6_28_
	    = class39_sub5_sub4_sub6
		  .method567(!class39_sub5_sub12.method710(i_26_, (byte) 52));
	i_27_ &= 0x3;
	if (i_27_ != 1) {
	    if (i_27_ != 2) {
		if (i_27_ == 3)
		    class39_sub5_sub4_sub6_28_.method555();
	    } else
		class39_sub5_sub4_sub6_28_.method562();
	} else
	    class39_sub5_sub4_sub6_28_.method551();
	class39_sub5_sub4_sub6_28_.method557(class39_sub5_sub12, i_26_);
	if (i_27_ != 1) {
	    if (i_27_ != 2) {
		if (i_27_ == 3)
		    class39_sub5_sub4_sub6_28_.method551();
	    } else
		class39_sub5_sub4_sub6_28_.method562();
	} else
	    class39_sub5_sub4_sub6_28_.method555();
	return class39_sub5_sub4_sub6_28_;
    }
    
    public static JString method708(JString[] class3s) {
	if (class3s.length < 2)
	    throw new IllegalArgumentException();
	return JImage.method1013(class3s.length, class3s, 0);
    }
    
    public Class39_Sub5_Sub11() {
	anInt1826 = 5;
	anInt1840 = -1;
	anInt1839 = -1;
	anInt1836 = -1;
	anInt1837 = -1;
	anInt1830 = 2;
	aBoolean1838 = false;
	anInt1825 = -1;
    }
    
    static {
	anInt1827 = 0;
	gameBuffer = new FrameBuffer(5000);
	aClass3_1842
	    = Class39_Sub5_Sub9.createJstring("Lade Konfiguration )2 ");
	anInt1841 = 0;
	anInt1843 = 1;
	npcDefinitionCache = new Cache(64);
	anIntArray1847 = new int[128];
	aClass3_1846
	    = Class39_Sub5_Sub9.createJstring("Create a free account");
	aClass3_1845 = aClass3_1846;
    }
}
