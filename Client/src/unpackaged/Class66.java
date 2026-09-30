package unpackaged;

/* Class66 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.AbstractImage;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.utils.Cache;
import java.awt.Graphics;

public class Class66
{
    public static JString aClass3_1148
	= (Class39_Sub5_Sub9.createJstring
	   ("Press (Wrecover a locked account(W on front page)3"));
    public static JString aClass3_1149
	= Class39_Sub5_Sub9.createJstring("m-Ochte mit Ihnen handeln)3");
    public static JString aClass3_1150;
    public static JString aClass3_1151;
    public static int[] stateValues;
    public static int anInt1153;
    public static JString aClass3_1154;
    public static FileLoader fileLoader10;
    public static AbstractImage aClass57_1156;
    public static int anInt1157;
    public static int anInt1158 = 0;
    public static JString aClass3_1159;
    public static JString aClass3_1160;
    public static JString aClass3_1161;
    public static JString aClass3_1162;
    public static JString aClass3_1163;
    public static JString blankString;
    public static JString aClass3_1165;
    
    public static void method1100(int i) {
	ItemDefinition.aClass7_1663.method134(27392);
    }
    
    public static boolean method1101(int i, Signlink class21, int i_0_,
				     boolean bool) {
	if (!CacheIO.method122(class21, 21597, bool))
	    return false;
	if (i > 0)
	    Npc.aClass7_2493 = new Cache(i);
	return true;
    }
    
    public static void method1102(byte i) {
	blankString = null;
	stateValues = null;
	fileLoader10 = null;
	aClass3_1150 = null;
	aClass3_1161 = null;
	aClass3_1154 = null;
	aClass3_1151 = null;
	aClass3_1163 = null;
	aClass3_1160 = null;
	aClass3_1148 = null;
	aClass3_1162 = null;
	aClass57_1156 = null;
	aClass3_1149 = null;
	aClass3_1165 = null;
	aClass3_1159 = null;
    }
    
    public static void method1103(int i) {
	Npc.aClass7_2490.method134(27392);
    }
    
    public static void method1104(boolean bool) {
	try {
	    Graphics graphics = Class41.aCanvas778.getGraphics();
	    ArchiveWorker.aClass57_1196.draw(graphics, 550, 4);
	} catch (Exception exception) {
	    Class41.aCanvas778.repaint();
	}
    }
    
    public static void method1105
	(int i, Class39_Sub5_Sub4_Sub4 class39_sub5_sub4_sub4) {
	class39_sub5_sub4_sub4.anInt2303 = class39_sub5_sub4_sub4.anInt2317;
	if (class39_sub5_sub4_sub4.anInt2312 == 0)
	    class39_sub5_sub4_sub4.anInt2274 = 0;
	else {
	    if (class39_sub5_sub4_sub4.anInt2268 != -1
		&& class39_sub5_sub4_sub4.anInt2305 == 0) {
		Class39_Sub5_Sub11 class39_sub5_sub11
		    = Class62_Sub1.method1064(class39_sub5_sub4_sub4.anInt2268,
					      (byte) 54);
		if (class39_sub5_sub4_sub4.anInt2254 > 0
		    && class39_sub5_sub11.anInt1840 == 0) {
		    class39_sub5_sub4_sub4.anInt2274++;
		    return;
		}
		if (class39_sub5_sub4_sub4.anInt2254 <= 0
		    && class39_sub5_sub11.anInt1837 == 0) {
		    class39_sub5_sub4_sub4.anInt2274++;
		    return;
		}
	    }
	    int i_1_ = class39_sub5_sub4_sub4.anInt2301;
	    int i_2_ = class39_sub5_sub4_sub4.anInt2275;
	    int i_3_ = ((class39_sub5_sub4_sub4.anIntArray2255
			 [class39_sub5_sub4_sub4.anInt2312 - 1]) * 128
			+ class39_sub5_sub4_sub4.anInt2297 * 64);
	    int i_4_ = ((class39_sub5_sub4_sub4.anIntArray2314
			 [class39_sub5_sub4_sub4.anInt2312 - 1]) * 128
			+ class39_sub5_sub4_sub4.anInt2297 * 64);
	    if (i_4_ - i_1_ > 256 || i_4_ - i_1_ < -256 || -i_2_ + i_3_ > 256
		|| -i_2_ + i_3_ < -256) {
		class39_sub5_sub4_sub4.anInt2275 = i_3_;
		class39_sub5_sub4_sub4.anInt2301 = i_4_;
	    } else {
		if (i_4_ > i_1_) {
		    if (i_2_ < i_3_)
			class39_sub5_sub4_sub4.anInt2294 = 1280;
		    else if (i_2_ > i_3_)
			class39_sub5_sub4_sub4.anInt2294 = 1792;
		    else
			class39_sub5_sub4_sub4.anInt2294 = 1536;
		} else if (i_1_ <= i_4_) {
		    if (i_3_ <= i_2_) {
			if (i_3_ < i_2_)
			    class39_sub5_sub4_sub4.anInt2294 = 0;
		    } else
			class39_sub5_sub4_sub4.anInt2294 = 1024;
		} else if (i_3_ <= i_2_) {
		    if (i_2_ > i_3_)
			class39_sub5_sub4_sub4.anInt2294 = 256;
		    else
			class39_sub5_sub4_sub4.anInt2294 = 512;
		} else
		    class39_sub5_sub4_sub4.anInt2294 = 768;
		int i_5_ = ((class39_sub5_sub4_sub4.anInt2294
			     - class39_sub5_sub4_sub4.anInt2251)
			    & 0x7ff);
		int i_6_ = class39_sub5_sub4_sub4.anInt2262;
		int i_7_ = 4;
		if ((class39_sub5_sub4_sub4.anInt2294
		     != class39_sub5_sub4_sub4.anInt2251)
		    && class39_sub5_sub4_sub4.anInt2260 == -1
		    && class39_sub5_sub4_sub4.anInt2250 != 0)
		    i_7_ = 2;
		if (i_5_ > 1024)
		    i_5_ -= 2048;
		if (i_5_ >= -256 && i_5_ <= 256)
		    i_6_ = class39_sub5_sub4_sub4.anInt2264;
		else if (i_5_ < 256 || i_5_ >= 768) {
		    if (i_5_ >= -768 && i_5_ <= -256)
			i_6_ = class39_sub5_sub4_sub4.anInt2257;
		} else
		    i_6_ = class39_sub5_sub4_sub4.anInt2282;
		if (class39_sub5_sub4_sub4.anInt2312 > 2)
		    i_7_ = 6;
		if (i_6_ == -1)
		    i_6_ = class39_sub5_sub4_sub4.anInt2264;
		class39_sub5_sub4_sub4.anInt2303 = i_6_;
		if (class39_sub5_sub4_sub4.anInt2312 > 3)
		    i_7_ = 8;
		if (class39_sub5_sub4_sub4.anInt2274 > 0
		    && class39_sub5_sub4_sub4.anInt2312 > 1) {
		    i_7_ = 8;
		    class39_sub5_sub4_sub4.anInt2274--;
		}
		if (class39_sub5_sub4_sub4.aBooleanArray2284
		    [class39_sub5_sub4_sub4.anInt2312 - 1])
		    i_7_ <<= 1;
		if (i_2_ < i_3_) {
		    class39_sub5_sub4_sub4.anInt2275 += i_7_;
		    if (i_3_ < class39_sub5_sub4_sub4.anInt2275)
			class39_sub5_sub4_sub4.anInt2275 = i_3_;
		} else if (i_2_ > i_3_) {
		    class39_sub5_sub4_sub4.anInt2275 -= i_7_;
		    if (class39_sub5_sub4_sub4.anInt2275 < i_3_)
			class39_sub5_sub4_sub4.anInt2275 = i_3_;
		}
		if (i_7_ >= 8
		    && (class39_sub5_sub4_sub4.anInt2303
			== class39_sub5_sub4_sub4.anInt2264)
		    && class39_sub5_sub4_sub4.anInt2293 != -1)
		    class39_sub5_sub4_sub4.anInt2303
			= class39_sub5_sub4_sub4.anInt2293;
		if (i_4_ > i_1_) {
		    class39_sub5_sub4_sub4.anInt2301 += i_7_;
		    if (class39_sub5_sub4_sub4.anInt2301 > i_4_)
			class39_sub5_sub4_sub4.anInt2301 = i_4_;
		} else if (i_1_ > i_4_) {
		    class39_sub5_sub4_sub4.anInt2301 -= i_7_;
		    if (i_4_ > class39_sub5_sub4_sub4.anInt2301)
			class39_sub5_sub4_sub4.anInt2301 = i_4_;
		}
		if (class39_sub5_sub4_sub4.anInt2301 == i_4_
		    && i_3_ == class39_sub5_sub4_sub4.anInt2275) {
		    class39_sub5_sub4_sub4.anInt2312--;
		    if (class39_sub5_sub4_sub4.anInt2254 > 0)
			class39_sub5_sub4_sub4.anInt2254--;
		}
	    }
	}
    }
    
    public static void method1106(int i, Class39_Sub11 class39_sub11) {
	int i_8_ = -1;
	int i_9_ = 0;
	if (class39_sub11.anInt1469 == 0)
	    i_9_ = Class44.aClass38_836.method379(class39_sub11.anInt1458,
						  class39_sub11.anInt1466,
						  class39_sub11.anInt1474);
	int i_10_ = 0;
	int i_11_ = 0;
	if (class39_sub11.anInt1469 == 1)
	    i_9_ = Class44.aClass38_836.method363(class39_sub11.anInt1458,
						  class39_sub11.anInt1466,
						  class39_sub11.anInt1474);
	if (class39_sub11.anInt1469 == 2)
	    i_9_ = Class44.aClass38_836.method384(class39_sub11.anInt1458,
						  class39_sub11.anInt1466,
						  class39_sub11.anInt1474);
	if (class39_sub11.anInt1469 == 3)
	    i_9_ = Class44.aClass38_836.method404(class39_sub11.anInt1458,
						  class39_sub11.anInt1466,
						  class39_sub11.anInt1474);
	if (i_9_ != 0) {
	    int i_12_ = Class44.aClass38_836.method359(class39_sub11.anInt1458,
						       class39_sub11.anInt1466,
						       class39_sub11.anInt1474,
						       i_9_);
	    i_11_ = (i_12_ & 0xd6) >> 6;
	    i_8_ = i_9_ >> 14 & 0x7fff;
	    i_10_ = i_12_ & 0x1f;
	}
	class39_sub11.anInt1451 = i_10_;
	class39_sub11.anInt1471 = i_8_;
	class39_sub11.anInt1449 = i_11_;
    }
    
    static {
	stateValues = new int[2000];
	aClass3_1162 = aClass3_1148;
	anInt1153 = 0;
	anInt1157 = 1;
	blankString = Class39_Sub5_Sub9.createJstring("");
	aClass3_1151 = blankString;
	aClass3_1150 = blankString;
	aClass3_1159 = blankString;
	aClass3_1160 = blankString;
	aClass3_1154 = blankString;
	aClass3_1163 = blankString;
	aClass3_1161
	    = Class39_Sub5_Sub9.createJstring("Malformed login packet)3");
	aClass3_1165 = aClass3_1161;
    }
}
