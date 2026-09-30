package jagex.world.actors;


import jagex.utils.Deque;
import jagex.utils.Cache;
import jagex.audio.Effect;
import jagex.graphics.AbstractImage;
import jagex.graphics.BitmapFont;
import jagex.io.BufferedFile;
import unpackaged.Bzip2Block;
import unpackaged.Canvas_Sub1;
import unpackaged.Class1;
import unpackaged.Class26;
import unpackaged.Class38;
import unpackaged.Class39_Sub10;
import unpackaged.Class39_Sub1_Sub2;
import unpackaged.Class39_Sub3_Sub1;
import unpackaged.Class39_Sub4;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class4;
import unpackaged.Class43;
import unpackaged.Class44;
import unpackaged.Class45;
import unpackaged.Class62_Sub1;
import unpackaged.Class62_Sub2;
import unpackaged.ClientScript;
import unpackaged.FileTable;
import jagex.utils.Huffmans;
import jagex.graphics.sprites.IndexedColorSprite;
import unpackaged.JKeyListener;
import jagex.utils.JString;
import unpackaged.Model;
import unpackaged.NameTable;
import jagex.utils.Node;
import unpackaged.NpcDefinition;
import unpackaged.ObjectDefinition;
import unpackaged.ScriptState;

/* Class39_Sub5_Sub4_Sub4_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Npc extends Class39_Sub5_Sub4_Sub4
{
    public static JString aClass3_2487;
    public static JString aClass3_2488
	= Class39_Sub5_Sub9.createJstring("purple:");
    public static JString aClass3_2489
	= Class39_Sub5_Sub9.createJstring("redstone1");
    public static Cache aClass7_2490;
    public static FileTable modelFileLoader;
    public NpcDefinition aClass39_Sub5_Sub13_2492;
    public static Cache aClass7_2493;
    public static JString aClass3_2494
	= Class39_Sub5_Sub9.createJstring("(U5");
    public static BitmapFont aClass39_Sub5_Sub10_Sub1_2495;
    public static JString aClass3_2496;
    public static JString aClass3_2497;
    public static JString aClass3_2498;
    public static JKeyListener aClass35_2499;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_2500;
    public static BufferedFile[] indexFiles;
    public static JString aClass3_2502;
    public static int[] anIntArray2503;
    public static JString aClass3_2504;
    
    public Model method489(boolean bool) {
	if (aClass39_Sub5_Sub13_2492 == null)
	    return null;
	Class39_Sub5_Sub11 class39_sub5_sub11
	    = (anInt2268 == -1 || anInt2305 != 0 ? null
	       : Class62_Sub1.method1064(anInt2268, (byte) 54));
	if (bool != true)
	    method525((byte) -57);
	Class39_Sub5_Sub11 class39_sub5_sub11_0_
	    = (anInt2303 != -1 && (anInt2303 != anInt2317
				   || class39_sub5_sub11 == null)
	       ? Class62_Sub1.method1064(anInt2303, (byte) 54) : null);
	Model class39_sub5_sub4_sub6
	    = aClass39_Sub5_Sub13_2492.method725(anInt2265, 65535,
						 class39_sub5_sub11_0_,
						 class39_sub5_sub11,
						 anInt2306);
	if (class39_sub5_sub4_sub6 == null)
	    return null;
	class39_sub5_sub4_sub6.method542();
	anInt2308 = class39_sub5_sub4_sub6.anInt1726;
	if (anInt2270 != -1 && anInt2276 != -1) {
	    Model class39_sub5_sub4_sub6_1_
		= Huffmans.method881(0, anInt2270).method778(180, anInt2276);
	    if (class39_sub5_sub4_sub6_1_ != null) {
		Model[] class39_sub5_sub4_sub6s
		    = { class39_sub5_sub4_sub6, class39_sub5_sub4_sub6_1_ };
		class39_sub5_sub4_sub6_1_.method554(0, -anInt2288, 0);
		class39_sub5_sub4_sub6
		    = new Model(class39_sub5_sub4_sub6s, 2,
						 true);
	    }
	}
	if (aClass39_Sub5_Sub13_2492.anInt1870 == 1)
	    class39_sub5_sub4_sub6.aBoolean2374 = true;
	return class39_sub5_sub4_sub6;
    }
    
    public static JString method519(int i, boolean bool, int i_2_) {
	if (i != 10)
	    method523(-15);
	return Class43.method907(bool, 10, i ^ ~0x42, i_2_);
    }
    
    public static void method520(int i, int i_3_, int i_4_, int i_5_,
				 int i_6_) {
	for (Class39_Sub10 class39_sub10
		 = (Class39_Sub10) Class4.aClass49_74.getFirst();
	     class39_sub10 != null;
	     class39_sub10
		 = (Class39_Sub10) Class4.aClass49_74.getNext()) {
	    if (class39_sub10.anInt1433 != -1
		|| class39_sub10.anIntArray1416 != null) {
		int i_7_ = 0;
		if (class39_sub10.anInt1438 >= i_4_) {
		    if (class39_sub10.anInt1432 > i_4_)
			i_7_ += class39_sub10.anInt1432 - i_4_;
		} else
		    i_7_ += -class39_sub10.anInt1438 + i_4_;
		if (class39_sub10.anInt1434 >= i_5_) {
		    if (class39_sub10.anInt1427 > i_5_)
			i_7_ += -i_5_ + class39_sub10.anInt1427;
		} else
		    i_7_ += i_5_ - class39_sub10.anInt1434;
		if (i_7_ - 64 > class39_sub10.anInt1446 || FileTable.anInt149 == 0
		    || i_6_ != class39_sub10.anInt1423) {
		    if (class39_sub10.aClass39_Sub1_Sub2_1418 != null) {
			Class1.aClass39_Sub1_Sub1_32
			    .method424(class39_sub10.aClass39_Sub1_Sub2_1418);
			class39_sub10.aClass39_Sub1_Sub2_1418 = null;
		    }
		    if (class39_sub10.aClass39_Sub1_Sub2_1441 != null) {
			Class1.aClass39_Sub1_Sub1_32
			    .method424(class39_sub10.aClass39_Sub1_Sub2_1441);
			class39_sub10.aClass39_Sub1_Sub2_1441 = null;
		    }
		} else {
		    i_7_ -= 64;
		    if (i_7_ < 0)
			i_7_ = 0;
		    int i_8_ = ((class39_sub10.anInt1446 - i_7_)
				* FileTable.anInt149 / class39_sub10.anInt1446);
		    if (class39_sub10.aClass39_Sub1_Sub2_1418 != null)
			class39_sub10.aClass39_Sub1_Sub2_1418.method443(i_8_);
		    else if (class39_sub10.anInt1433 >= 0) {
			Effect class52
			    = Effect.createEffect(Node.fileLoader4,
						class39_sub10.anInt1433, 0);
			if (class52 != null) {
			    Class39_Sub3_Sub1 class39_sub3_sub1
				= (class52.method981().method455
				   (ClientScript.aClass43_1693));
			    Class39_Sub1_Sub2 class39_sub1_sub2
				= Class39_Sub1_Sub2
				      .method444(class39_sub3_sub1, 100, i_8_);
			    class39_sub1_sub2.method448(-1);
			    Class1.aClass39_Sub1_Sub1_32
				.method421(class39_sub1_sub2);
			    class39_sub10.aClass39_Sub1_Sub2_1418
				= class39_sub1_sub2;
			}
		    }
		    if (class39_sub10.aClass39_Sub1_Sub2_1441 != null) {
			class39_sub10.aClass39_Sub1_Sub2_1441.method443(i_8_);
			if (!class39_sub10.aClass39_Sub1_Sub2_1441.method437())
			    class39_sub10.aClass39_Sub1_Sub2_1441 = null;
		    } else if (class39_sub10.anIntArray1416 != null
			       && (class39_sub10.anInt1442 -= i_3_) <= 0) {
			int i_9_ = (int) (Math.random()
					  * (double) (class39_sub10
						      .anIntArray1416).length);
			Effect class52
			    = Effect.createEffect(Node.fileLoader4,
						(class39_sub10.anIntArray1416
						 [i_9_]),
						0);
			if (class52 != null) {
			    Class39_Sub3_Sub1 class39_sub3_sub1
				= (class52.method981().method455
				   (ClientScript.aClass43_1693));
			    Class39_Sub1_Sub2 class39_sub1_sub2
				= Class39_Sub1_Sub2
				      .method444(class39_sub3_sub1, 100, i_8_);
			    class39_sub1_sub2.method448(0);
			    Class1.aClass39_Sub1_Sub1_32
				.method421(class39_sub1_sub2);
			    class39_sub10.aClass39_Sub1_Sub2_1441
				= class39_sub1_sub2;
			    class39_sub10.anInt1442
				= (class39_sub10.anInt1426
				   + (int) ((double) (class39_sub10.anInt1439
						      - (class39_sub10
							 .anInt1426))
					    * Math.random()));
			}
		    }
		}
	    }
	}
	int i_10_ = 39 / ((i + 25) / 33);
    }
    
    public static JString method521(int i, int i_11_) {
	if (i < i_11_)
	    return AbstractImage.method1007((byte) 71, i);
	return ScriptState.aClass3_456;
    }
    
    public static int xor(int v0, int v1) {
	return v0 ^ v1;
    }
    
    public static void method523(int i) {
	aClass35_2499 = null;
	aClass3_2502 = null;
	aClass7_2490 = null;
	aClass3_2496 = null;
	aClass3_2488 = null;
	aClass3_2497 = null;
	modelFileLoader = null;
	aClass3_2489 = null;
	aClass3_2504 = null;
	aClass3_2498 = null;
	aClass7_2493 = null;
	aClass39_Sub5_Sub10_Sub4_2500 = null;
	aClass3_2494 = null;
	indexFiles = null;
	anIntArray2503 = null;
	aClass3_2487 = null;
	aClass39_Sub5_Sub10_Sub1_2495 = null;
    }
    
    public boolean method510(byte i) {
	if (aClass39_Sub5_Sub13_2492 == null)
	    return false;
	return true;
    }
    
    public static void method524(byte i) {
	Class45.aBoolean867 = true;
	Class38.aBoolean684 = true;
    }
    
    public static void method525(byte i) {
	if (Deque.odSocket != null)
	    Deque.odSocket.stop();
	if (i != -127)
	    method525((byte) 36);
    }
    
    public static boolean method526(int i, int i_13_, boolean bool,
				    int i_14_) {
	int i_15_ = (i & 0x1ffffc8f) >> 14;
	int i_16_ = Class44.aClass38_836.method359(NameTable.height, i_13_,
						   i_14_, i);
	if (i_16_ == -1)
	    return false;
	int i_17_ = i_16_ >> 6 & 0x3;
	int i_18_ = i_16_ & 0x1f;
	if (i_18_ == 10 || i_18_ == 11 || i_18_ == 22) {
	    ObjectDefinition class39_sub5_sub15
		= Canvas_Sub1.method40(i_15_, (byte) 60);
	    int i_19_;
	    int i_20_;
	    if (i_17_ != 0 && i_17_ != 2) {
		i_20_ = class39_sub5_sub15.anInt1948;
		i_19_ = class39_sub5_sub15.anInt1925;
	    } else {
		i_19_ = class39_sub5_sub15.anInt1948;
		i_20_ = class39_sub5_sub15.anInt1925;
	    }
	    int i_21_ = class39_sub5_sub15.anInt1958;
	    if (i_17_ != 0)
		i_21_ = (i_21_ >> -i_17_ + 4) + (i_21_ << i_17_ & 0xf);
	    Class26.method293(24134, 0, i_14_, 0, 2, i_20_, i_19_, true, i_21_,
			      (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109
			       .anIntArray2314[0]),
			      (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109
			       .anIntArray2255[0]),
			      i_13_);
	} else
	    Class26.method293(24134, i_18_ + 1, i_14_, i_17_, 2, 0, 0, true, 0,
			      (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109
			       .anIntArray2314[0]),
			      (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109
			       .anIntArray2255[0]),
			      i_13_);
	Class62_Sub2.anInt1596 = Bzip2Block.anInt1054;
	Class4.anInt80 = 2;
	Class26.anInt503 = 0;
	Class62_Sub1.anInt1590 = Class39_Sub4.anInt1329;
	return true;
    }
    
    static {
	aClass3_2487 = (Class39_Sub5_Sub9.createJstring
			("RuneScape wird geladen )2 bitte warten)3)3)3"));
	aClass3_2496 = aClass3_2488;
	aClass3_2497
	    = Class39_Sub5_Sub9.createJstring("Ihr Charakter)2Profil wird in:");
	aClass7_2490 = new Cache(64);
	indexFiles = new BufferedFile[14];
	aClass3_2502 = Class39_Sub5_Sub9.createJstring("Accept trade");
	aClass3_2504 = aClass3_2502;
	aClass3_2498
	    = Class39_Sub5_Sub9.createJstring(" hat sich eingeloggt)3");
	aClass35_2499 = new JKeyListener();
    }
}
