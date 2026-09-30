package unpackaged;

import jagex.graphics.AbstractImage;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.world.actors.StillGraphic;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;

/* Class55 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class55 implements Interface2
{
    public static FileLoader fileLoader13;
    public static IndexedColorSprite[] aClass39_Sub5_Sub10_Sub4Array1243;
    public static JString aClass3_1244;
    public static byte[][][] tileFlags;
    public static JString aClass3_1246;
    public static IndexedColorSprite[] aClass39_Sub5_Sub10_Sub4Array1247;
    public static AbstractImage aClass57_1248;
    public static JString aClass3_1249;
    public static JString aClass3_1250;
    public static Class62 aClass62_1251;
    public static int anInt1252 = 0;
    // Set while the native revision-443 character-design interface is active.
    // The Accept clientscript clears the main-interface variable before opcode
    // 3103 runs, so this latch lets the close-all routine still distinguish
    // character creation from ordinary modal closes.
    public static boolean characterDesignActive = false;
    public static JString aClass3_1253;
    public Deque aClass49_1254 = new Deque();
    public static int[] anIntArray1255;
    public Class39_Sub8[] aClass39_Sub8Array1256;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_1257;
    public int anInt1258 = 0;
    public FileTable aClass9_1259;
    public int anInt1260;
    public int anInt1261 = 128;
    public double aDouble1262 = 1.0;
    
    public int[] method8(int i, int i_0_) {
	if (i_0_ != -9039)
	    method998((byte) -28);
	Class39_Sub8 class39_sub8 = aClass39_Sub8Array1256[i];
	if (class39_sub8 != null) {
	    if (class39_sub8.anIntArray1395 != null) {
		aClass49_1254.offerFirst(class39_sub8);
		class39_sub8.aBoolean1398 = true;
		return class39_sub8.anIntArray1395;
	    }
	    boolean bool
		= class39_sub8.method853(aDouble1262, anInt1261, aClass9_1259);
	    if (bool) {
		if (anInt1258 != 0)
		    anInt1258--;
		else {
		    Class39_Sub8 class39_sub8_1_
			= (Class39_Sub8) aClass49_1254.pollLast();
		    class39_sub8_1_.method854();
		}
		aClass49_1254.offerFirst(class39_sub8);
		class39_sub8.aBoolean1398 = true;
		return class39_sub8.anIntArray1395;
	    }
	}
	return null;
    }
    
    public static void method998(byte i) {
	aClass62_1251 = null;
	aClass3_1244 = null;
	tileFlags = null;
	anIntArray1255 = null;
	aClass3_1246 = null;
	aClass39_Sub5_Sub10_Sub4_1257 = null;
	fileLoader13 = null;
	aClass39_Sub5_Sub10_Sub4Array1243 = null;
	aClass3_1253 = null;
	aClass3_1249 = null;
	aClass57_1248 = null;
	aClass3_1250 = null;
	aClass39_Sub5_Sub10_Sub4Array1247 = null;
    }
    
    public static void method999(int i) {
	boolean preserveTutorialInstructions = characterDesignActive
		&& Class39_Sub5_Sub14.anInt1912 == 214;
	characterDesignActive = false;
	FrameBuffer.outgoingGameBuffer.putFrame(70);
	if (StillGraphic.anInt2338 != -1) {
	    Class62_Sub2.method1084((byte) 126,
				    StillGraphic.anInt2338);
	    StillGraphic.anInt2338 = -1;
	    IsaacPrng.aBoolean1089 = true;
	    Class39_Sub14.aBoolean1520 = true;
	    Class39_Sub10.anInt1420 = -1;
	}
	if (Class39_Sub5_Sub14.anInt1912 != -1 && !preserveTutorialInstructions) {
	    Class62_Sub2.method1084((byte) 122, Class39_Sub5_Sub14.anInt1912);
	    Class39_Sub5_Sub14.anInt1912 = -1;
	    Class14.aBoolean245 = true;
	    Class39_Sub10.anInt1420 = -1;
	} else if (preserveTutorialInstructions) {
	    // Keep native tutorial chat group 214 alive across character-design Accept
	    // and repaint it immediately instead of waiting for another input event.
	    Class14.aBoolean245 = true;
	    Class39_Sub10.anInt1420 = -1;
	}
	if (SubNode.anInt1348 != -1) {
	    Class62_Sub2.method1084((byte) 119, SubNode.anInt1348);
	    SubNode.anInt1348 = -1;
	    Class39_Sub14.setState(30);
	}
	if (ClientScript.anInt1713 != -1) {
	    Class62_Sub2.method1084((byte) -31, ClientScript.anInt1713);
	    ClientScript.anInt1713 = -1;
	}
	if (Class39_Sub11.anInt1478 != -1) {
	    Class62_Sub2.method1084((byte) -75, Class39_Sub11.anInt1478);
	    Class39_Sub10.anInt1420 = -1;
	    Class39_Sub11.anInt1478 = -1;
	}
    }
    
    public static void method1000(int i, int i_2_, int i_3_, int i_4_,
				  int i_5_, int i_6_, int i_7_, boolean bool) {
	if (bool)
	    aClass3_1246 = null;
	if (i_7_ >= 1 && i_4_ >= 1 && i_7_ <= 102 && i_4_ <= 102) {
	    if (!Class45.aBoolean867 || i_2_ == NameTable.height) {
		int i_8_ = -1;
		boolean bool_9_ = false;
		boolean bool_10_ = false;
		int i_11_ = 0;
		if (i == 0)
		    i_11_ = Class44.aClass38_836.method379(i_2_, i_7_, i_4_);
		if (i == 1)
		    i_11_ = Class44.aClass38_836.method363(i_2_, i_7_, i_4_);
		if (i == 2)
		    i_11_ = Class44.aClass38_836.method384(i_2_, i_7_, i_4_);
		if (i == 3)
		    i_11_ = Class44.aClass38_836.method404(i_2_, i_7_, i_4_);
		if (i_11_ != 0) {
		    i_8_ = i_11_ >> 14 & 0x7fff;
		    int i_12_ = Class44.aClass38_836.method359(i_2_, i_7_,
							       i_4_, i_11_);
		    int i_13_ = i_12_ & 0x1f;
		    int i_14_ = (i_12_ & 0xdd) >> 6;
		    if (i == 0) {
			Class44.aClass38_836.method380(i_2_, i_7_, i_4_);
			ObjectDefinition class39_sub5_sub15
			    = Canvas_Sub1.method40(i_8_, (byte) 94);
			if (class39_sub5_sub15.aBoolean1928)
			    Class39_Sub5_Sub12.aClass27Array1857[i_2_]
				.method299
				((byte) 40, i_14_, i_7_, i_4_, i_13_,
				 class39_sub5_sub15.aBoolean1949);
		    }
		    if (i == 1)
			Class44.aClass38_836.method366(i_2_, i_7_, i_4_);
		    if (i == 2) {
			Class44.aClass38_836.method398(i_2_, i_7_, i_4_);
			ObjectDefinition class39_sub5_sub15
			    = Canvas_Sub1.method40(i_8_, (byte) 26);
			if (i_7_ + class39_sub5_sub15.anInt1925 > 103
			    || i_4_ + class39_sub5_sub15.anInt1925 > 103
			    || i_7_ + class39_sub5_sub15.anInt1948 > 103
			    || i_4_ + class39_sub5_sub15.anInt1948 > 103)
			    return;
			if (class39_sub5_sub15.aBoolean1928)
			    Class39_Sub5_Sub12.aClass27Array1857[i_2_]
				.method295
				(i_14_, class39_sub5_sub15.anInt1948, i_4_,
				 i_7_, (byte) -27,
				 class39_sub5_sub15.aBoolean1949,
				 class39_sub5_sub15.anInt1925);
		    }
		    if (i == 3) {
			Class44.aClass38_836.method364(i_2_, i_7_, i_4_);
			ObjectDefinition class39_sub5_sub15
			    = Canvas_Sub1.method40(i_8_, (byte) 75);
			if (class39_sub5_sub15.aBoolean1928
			    && class39_sub5_sub15.anInt1941 == 1)
			    Class39_Sub5_Sub12.aClass27Array1857[i_2_]
				.method300(i_7_, i_4_, (byte) 61);
		    }
		}
		if (i_5_ >= 0) {
		    int i_15_ = i_2_;
		    if (i_15_ < 3 && (tileFlags[1][i_7_][i_4_]
				      & 0x2) == 2)
			i_15_++;
		    Class39_Sub5_Sub12.method712((Class39_Sub5_Sub12
						  .aClass27Array1857[i_2_]),
						 i_7_, i_3_, i_2_, i_15_, i_5_,
						 i_6_, Class44.aClass38_836,
						 -114, i_4_);
		}
	    }
	}
    }
    
    public static Class39_Sub5_Sub12 method1001(byte i, int i_16_) {
	Class39_Sub5_Sub12 class39_sub5_sub12
	    = ((Class39_Sub5_Sub12)
	       ArchiveWorker.aClass7_1197.get((long) i_16_));
	if (class39_sub5_sub12 != null)
	    return class39_sub5_sub12;
	class39_sub5_sub12
	    = NameTable.method182(i_16_, NpcDefinition.aClass9_1860,
				Node.aClass9_739, 66, false);
	if (class39_sub5_sub12 != null)
	    ArchiveWorker.aClass7_1197.put(class39_sub5_sub12, (long) i_16_,
					   (byte) -52);
	return class39_sub5_sub12;
    }
    
    public boolean method10(int i, int i_17_) {
	if (i != -11819)
	    return false;
	if (anInt1261 != 64)
	    return false;
	return true;
    }
    
    public void method1002(int i) {
	for (int i_18_ = 0; i_18_ < aClass39_Sub8Array1256.length; i_18_++) {
	    if (aClass39_Sub8Array1256[i_18_] != null)
		aClass39_Sub8Array1256[i_18_].method854();
	}
	aClass49_1254 = new Deque();
	anInt1258 = anInt1260;
    }
    
    public void method1003(double d, boolean bool) {
	aDouble1262 = d;
	method1002(66);
    }
    
    public int method7(int i, int i_19_) {
	if (i != 105)
	    method8(90, 89);
	if (aClass39_Sub8Array1256[i_19_] != null)
	    return aClass39_Sub8Array1256[i_19_].anInt1396;
	return 0;
    }
    
    public boolean method9(byte i, int i_20_) {
	if (i != -50)
	    aClass9_1259 = null;
	return aClass39_Sub8Array1256[i_20_].aBoolean1393;
    }
    
    public void method1004(int i, boolean bool) {
	for (int i_21_ = 0; i_21_ < aClass39_Sub8Array1256.length; i_21_++) {
	    Class39_Sub8 class39_sub8 = aClass39_Sub8Array1256[i_21_];
	    if (class39_sub8 != null && class39_sub8.anInt1394 != 0
		&& class39_sub8.aBoolean1398) {
		class39_sub8.method855(i);
		class39_sub8.aBoolean1398 = false;
	    }
	}
    }
    
    public Class55(FileTable class9, FileTable class9_22_, int i, double d,
		   int i_23_) {
	aClass9_1259 = class9_22_;
	aDouble1262 = d;
	anInt1261 = i_23_;
	anInt1260 = i;
	anInt1258 = anInt1260;
	int[] is = class9.getChildrenEntries(0);
	int i_24_ = is.length;
	aClass39_Sub8Array1256
	    = new Class39_Sub8[class9.getAmountChildren(0)];
	for (int i_25_ = 0; i_24_ > i_25_; i_25_++) {
	    Buffer class39_sub6
		= new Buffer(class9.lookupFile(0, is[i_25_]));
	    aClass39_Sub8Array1256[is[i_25_]] = new Class39_Sub8(class39_sub6);
	}
    }
    
    static {
	aClass3_1249
	    = Class39_Sub5_Sub9
		  .createJstring("zur-Uck auf die RuneScape)2Webseite gehen");
	aClass3_1250
	    = Class39_Sub5_Sub9
		  .createJstring("Wen m-Ochten Sie der Liste hinzuf-Ugen?");
	aClass3_1244 = Class39_Sub5_Sub9.createJstring("scroll:");
	tileFlags = new byte[4][104][104];
	aClass3_1246
	    = Class39_Sub5_Sub9
		  .createJstring("-5berpr-Ufen Sie Ihr Mitteilungsfach)3");
	aClass3_1253 = aClass3_1244;
    }
}
