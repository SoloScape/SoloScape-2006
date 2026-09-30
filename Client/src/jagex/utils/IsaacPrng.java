package jagex.utils;

import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.map.TraversalMap;
import jagex.io.Buffer;
import unpackaged.ArchiveWorker;
import jagex.io.BufferedFile;
import unpackaged.Bzip2Block;
import unpackaged.Class2;
import unpackaged.Class30;
import unpackaged.Class33;
import unpackaged.Class34;
import unpackaged.Class37;
import unpackaged.Class39_Sub12;
import unpackaged.Class39_Sub14;
import unpackaged.Class39_Sub4;
import unpackaged.Class39_Sub5_Sub10_Sub2;
import unpackaged.Class39_Sub5_Sub12;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class41;
import unpackaged.Class44;
import unpackaged.Class46;
import unpackaged.Class53;
import unpackaged.Class62;
import unpackaged.Class66;
import unpackaged.Client;
import unpackaged.ClientApplet;
import jagex.graphics.sprites.DirectColorSprite;
import unpackaged.FileLoader;
import unpackaged.ItemDefinition;
import unpackaged.NpcDefinition;
import unpackaged.Widget;

/* Class61 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class IsaacPrng
{
    public static byte[][][] aByteArrayArrayArray1081;
    public static JString aClass3_1082;
    public int anInt1083;
    public static int mSectorY;
    public static JString aClass3_1085;
    public static JString aClass3_1086;
    public static int anInt1087;
    public int anInt1088;
    public static boolean aBoolean1089;
    public static JString aClass3_1090
	= Class39_Sub5_Sub9.createJstring("backhmid2");
    public static int anInt1091;
    public static JString aClass3_1092;
    public int anInt1093;
    public static JString aClass3_1094;
    public static int anInt1095;
    public static DirectColorSprite aClass39_Sub5_Sub10_Sub3_1096;
    public static JString aClass3_1097
	= Class39_Sub5_Sub9.createJstring("wishes to duel with you)3");
    public static JString aClass3_1098;
    public static JString aClass3_1099;
    public static boolean aBoolean1100;
    public int anInt1101;
    public int[] anIntArray1102;
    public static JString aClass3_1103;
    public static Buffer[] aClass39_Sub6Array1104;
    public int[] anIntArray1105 = new int[256];
    
    public static void method1038(int i, int i_0_, int i_1_, int i_2_,
				  Widget[] class39_sub5_sub17s,
				  int i_3_, int i_4_, int i_5_, int i_6_,
				  int i_7_) {
	for (int i_8_ = 0; i_8_ < class39_sub5_sub17s.length; i_8_++) {
	    Widget class39_sub5_sub17 = class39_sub5_sub17s[i_8_];
	    if (class39_sub5_sub17 != null
		&& (class39_sub5_sub17.type == 0
		    || class39_sub5_sub17.aBoolean2047)
		&& class39_sub5_sub17 != null
		&& i == class39_sub5_sub17.anInt2050
		&& !class39_sub5_sub17.method754(117, HashTable.aBoolean361)) {
		int i_9_ = i_0_ + (class39_sub5_sub17.anInt2091 - i_5_);
		int i_10_ = -i_2_ + class39_sub5_sub17.anInt2021 + i_7_;
		int i_11_ = i_9_ + class39_sub5_sub17.quadWidth;
		int i_12_ = i_10_ + class39_sub5_sub17.quadHeight;
		int i_13_ = i_10_ <= i_7_ ? i_7_ : i_10_;
		int i_14_ = i_4_ > i_11_ ? i_11_ : i_4_;
		int i_15_ = i_0_ < i_9_ ? i_9_ : i_0_;
		int i_16_ = i_12_ < i_3_ ? i_12_ : i_3_;
		if (class39_sub5_sub17.type == 0) {
		    method1038(class39_sub5_sub17.anInt2084, i_15_, i_1_,
			       class39_sub5_sub17.anInt1994,
			       class39_sub5_sub17s, i_16_, i_14_,
			       class39_sub5_sub17.anInt2064, i_6_, i_13_);
		    if (class39_sub5_sub17.aClass39_Sub5_Sub17Array2025
			!= null)
			method1038(class39_sub5_sub17.anInt2084, i_15_, 1,
				   class39_sub5_sub17.anInt1994,
				   (class39_sub5_sub17
				    .aClass39_Sub5_Sub17Array2025),
				   i_16_, i_14_, class39_sub5_sub17.anInt2064,
				   i_6_, i_13_);
		}
		if (class39_sub5_sub17.aBoolean2047) {
		    boolean bool = false;
		    boolean bool_17_;
		    if (anInt1091 < i_15_ || Class33.anInt599 < i_13_
			|| i_14_ <= anInt1091 || i_16_ <= Class33.anInt599)
			bool_17_ = false;
		    else
			bool_17_ = true;
		    boolean bool_18_ = false;
		    if (Class30.anInt541 == 1 && bool_17_)
			bool = true;
		    if (Class46.anInt887 == 1
			&& Class39_Sub4.anInt1329 >= i_15_
			&& Bzip2Block.anInt1054 >= i_13_
			&& i_14_ > Class39_Sub4.anInt1329
			&& Bzip2Block.anInt1054 < i_16_)
			bool_18_ = true;
		    if (bool_18_
			&& NpcDefinition.aClass39_Sub5_Sub17_1864 == null
			&& (i_6_ & 0x200) != 0 && !Class39_Sub12.aBoolean1493
			&& (Class44.method914(class39_sub5_sub17, (byte) -128)
			    != null)) {
			FileLoader.anInt1303 = Class33.anInt599;
			ClientApplet.anInt8 = 0;
			ItemDefinition.anInt1684 = anInt1091;
			NpcDefinition.aClass39_Sub5_Sub17_1864
			    = class39_sub5_sub17;
			Class37.aBoolean654 = false;
		    }
		    if (NpcDefinition.aClass39_Sub5_Sub17_1864 != null
			|| Class39_Sub12.aBoolean1493) {
			bool_17_ = false;
			bool_18_ = false;
			bool = false;
		    }
		    if (!class39_sub5_sub17.aBoolean2106 && bool_18_
			&& (i_6_ & 0x1) != 0) {
			class39_sub5_sub17.aBoolean2106 = true;
			if (class39_sub5_sub17.anObjectArray2035 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2035, null,
				 125, -i_9_ + Class39_Sub4.anInt1329,
				 class39_sub5_sub17,
				 Bzip2Block.anInt1054 - i_10_);
		    }
		    if (class39_sub5_sub17.aBoolean2106 && bool
			&& (i_6_ & 0x4) != 0
			&& class39_sub5_sub17.anObjectArray2044 != null)
			Class39_Sub5_Sub4_Sub4.executeClientScript
			    (0, class39_sub5_sub17.anObjectArray2044, null,
			     -119, anInt1091 - i_9_, class39_sub5_sub17,
			     Class33.anInt599 - i_10_);
		    if (class39_sub5_sub17.aBoolean2106 && !bool
			&& (i_6_ & 0x2) != 0) {
			class39_sub5_sub17.aBoolean2106 = false;
			if (class39_sub5_sub17.anObjectArray2085 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2085, null,
				 122, anInt1091 - i_9_, class39_sub5_sub17,
				 -i_10_ + Class33.anInt599);
		    }
		    if (bool && (i_6_ & 0x8) != 0
			&& class39_sub5_sub17.anObjectArray2080 != null)
			Class39_Sub5_Sub4_Sub4.executeClientScript
			    (0, class39_sub5_sub17.anObjectArray2080, null,
			     118, anInt1091 - i_9_, class39_sub5_sub17,
			     Class33.anInt599 - i_10_);
		    if (!class39_sub5_sub17.aBoolean2042 && bool_17_
			&& (i_6_ & 0x10) != 0) {
			class39_sub5_sub17.aBoolean2042 = true;
			if (class39_sub5_sub17.anObjectArray2019 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2019, null,
				 127, -i_9_ + anInt1091, class39_sub5_sub17,
				 -i_10_ + Class33.anInt599);
		    }
		    if (class39_sub5_sub17.aBoolean2042 && bool_17_
			&& (i_6_ & 0x40) != 0
			&& class39_sub5_sub17.anObjectArray2075 != null)
			Class39_Sub5_Sub4_Sub4.executeClientScript
			    (0, class39_sub5_sub17.anObjectArray2075, null,
			     i_1_ - 122, anInt1091 - i_9_, class39_sub5_sub17,
			     Class33.anInt599 - i_10_);
		    if (class39_sub5_sub17.aBoolean2042 && !bool_17_
			&& (i_6_ & 0x20) != 0) {
			class39_sub5_sub17.aBoolean2042 = false;
			if (class39_sub5_sub17.anObjectArray2100 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2100, null,
				 -44, -i_9_ + anInt1091, class39_sub5_sub17,
				 Class33.anInt599 - i_10_);
		    }
		    if (class39_sub5_sub17.anObjectArray2016 != null
			&& (i_6_ & 0x80) != 0)
			Class39_Sub5_Sub4_Sub4.executeClientScript(0,
							 (class39_sub5_sub17
							  .anObjectArray2016),
							 null, 106, 0,
							 class39_sub5_sub17,
							 0);
		    if (bool_17_ && Class62.anInt1107 != 0
			&& class39_sub5_sub17.anObjectArray2039 != null
			&& (i_6_ & 0x400) != 0)
			Class39_Sub5_Sub4_Sub4.executeClientScript(0,
							 (class39_sub5_sub17
							  .anObjectArray2039),
							 null, -86, 0,
							 class39_sub5_sub17,
							 Class62.anInt1107);
		    if ((i_6_ & 0x100) != 0) {
			if (class39_sub5_sub17.anInt2012 < Class66.anInt1153
			    && class39_sub5_sub17.anObjectArray2101 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2101, null,
				 -19, 0, class39_sub5_sub17, 0);
			if ((Buffer.anInt1364
			     > class39_sub5_sub17.anInt2012)
			    && class39_sub5_sub17.anObjectArray2061 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2061, null,
				 i_1_ ^ 0x7a, 0, class39_sub5_sub17, 0);
			if (class39_sub5_sub17.anInt2012 < Client.anInt1269
			    && class39_sub5_sub17.anObjectArray2062 != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2062, null,
				 123, 0, class39_sub5_sub17, 0);
			class39_sub5_sub17.anInt2012 = Class2.logicCycle;
		    }
		}
	    }
	}
	if (i_1_ != 1)
	    aByteArrayArrayArray1081 = null;
    }
    
    public int getNextValue() {
	if (anInt1093-- == 0) {
	    method1042(true);
	    anInt1093 = 255;
	}
	return anIntArray1105[anInt1093];
    }
    
    public void method1040(int i) {
	if (i < 59)
	    aClass3_1097 = null;
	int i_20_;
	int i_21_;
	int i_22_;
	int i_23_;
	int i_24_;
	int i_25_;
	int i_26_;
	int i_19_ = (i_20_ = i_21_ = i_22_ = i_23_ = i_24_ = i_25_ = i_26_
		     = -1640531527);
	for (int i_27_ = 0; i_27_ < 4; i_27_++) {
	    i_19_ ^= i_20_ << 11;
	    i_22_ += i_19_;
	    i_20_ += i_21_;
	    i_20_ ^= i_21_ >>> 2;
	    i_23_ += i_20_;
	    i_21_ += i_22_;
	    i_21_ ^= i_22_ << 8;
	    i_22_ += i_23_;
	    i_24_ += i_21_;
	    i_22_ ^= i_23_ >>> 16;
	    i_25_ += i_22_;
	    i_23_ += i_24_;
	    i_23_ ^= i_24_ << 10;
	    i_26_ += i_23_;
	    i_24_ += i_25_;
	    i_24_ ^= i_25_ >>> 4;
	    i_25_ += i_26_;
	    i_19_ += i_24_;
	    i_25_ ^= i_26_ << 8;
	    i_26_ += i_19_;
	    i_20_ += i_25_;
	    i_26_ ^= i_19_ >>> 9;
	    i_21_ += i_26_;
	    i_19_ += i_20_;
	}
	for (int i_28_ = 0; i_28_ < 256; i_28_ += 8) {
	    i_21_ += anIntArray1105[i_28_ + 2];
	    i_23_ += anIntArray1105[i_28_ + 4];
	    i_19_ += anIntArray1105[i_28_];
	    i_24_ += anIntArray1105[i_28_ + 5];
	    i_25_ += anIntArray1105[i_28_ + 6];
	    i_20_ += anIntArray1105[i_28_ + 1];
	    i_19_ ^= i_20_ << 11;
	    i_22_ += anIntArray1105[i_28_ + 3];
	    i_22_ += i_19_;
	    i_26_ += anIntArray1105[i_28_ + 7];
	    i_20_ += i_21_;
	    i_20_ ^= i_21_ >>> 2;
	    i_21_ += i_22_;
	    i_23_ += i_20_;
	    i_21_ ^= i_22_ << 8;
	    i_22_ += i_23_;
	    i_22_ ^= i_23_ >>> 16;
	    i_25_ += i_22_;
	    i_24_ += i_21_;
	    i_23_ += i_24_;
	    i_23_ ^= i_24_ << 10;
	    i_24_ += i_25_;
	    i_26_ += i_23_;
	    i_24_ ^= i_25_ >>> 4;
	    i_19_ += i_24_;
	    i_25_ += i_26_;
	    i_25_ ^= i_26_ << 8;
	    i_20_ += i_25_;
	    i_26_ += i_19_;
	    i_26_ ^= i_19_ >>> 9;
	    i_19_ += i_20_;
	    i_21_ += i_26_;
	    anIntArray1102[i_28_] = i_19_;
	    anIntArray1102[i_28_ + 1] = i_20_;
	    anIntArray1102[i_28_ + 2] = i_21_;
	    anIntArray1102[i_28_ + 3] = i_22_;
	    anIntArray1102[i_28_ + 4] = i_23_;
	    anIntArray1102[i_28_ + 5] = i_24_;
	    anIntArray1102[i_28_ + 6] = i_25_;
	    anIntArray1102[i_28_ + 7] = i_26_;
	}
	for (int i_29_ = 0; i_29_ < 256; i_29_ += 8) {
	    i_20_ += anIntArray1102[i_29_ + 1];
	    i_19_ += anIntArray1102[i_29_];
	    i_21_ += anIntArray1102[i_29_ + 2];
	    i_25_ += anIntArray1102[i_29_ + 6];
	    i_19_ ^= i_20_ << 11;
	    i_22_ += anIntArray1102[i_29_ + 3];
	    i_26_ += anIntArray1102[i_29_ + 7];
	    i_22_ += i_19_;
	    i_23_ += anIntArray1102[i_29_ + 4];
	    i_20_ += i_21_;
	    i_20_ ^= i_21_ >>> 2;
	    i_21_ += i_22_;
	    i_21_ ^= i_22_ << 8;
	    i_23_ += i_20_;
	    i_24_ += anIntArray1102[i_29_ + 5];
	    i_22_ += i_23_;
	    i_22_ ^= i_23_ >>> 16;
	    i_25_ += i_22_;
	    i_24_ += i_21_;
	    i_23_ += i_24_;
	    i_23_ ^= i_24_ << 10;
	    i_24_ += i_25_;
	    i_24_ ^= i_25_ >>> 4;
	    i_19_ += i_24_;
	    i_26_ += i_23_;
	    i_25_ += i_26_;
	    i_25_ ^= i_26_ << 8;
	    i_26_ += i_19_;
	    i_26_ ^= i_19_ >>> 9;
	    i_21_ += i_26_;
	    i_20_ += i_25_;
	    i_19_ += i_20_;
	    anIntArray1102[i_29_] = i_19_;
	    anIntArray1102[i_29_ + 1] = i_20_;
	    anIntArray1102[i_29_ + 2] = i_21_;
	    anIntArray1102[i_29_ + 3] = i_22_;
	    anIntArray1102[i_29_ + 4] = i_23_;
	    anIntArray1102[i_29_ + 5] = i_24_;
	    anIntArray1102[i_29_ + 6] = i_25_;
	    anIntArray1102[i_29_ + 7] = i_26_;
	}
	method1042(true);
	anInt1093 = 256;
    }
    
    public static void method1041(boolean bool) {
	aClass3_1097 = null;
	aClass3_1090 = null;
	aClass39_Sub6Array1104 = null;
	aClass3_1086 = null;
	aByteArrayArrayArray1081 = null;
	aClass3_1092 = null;
	aClass3_1098 = null;
	aClass3_1082 = null;
	aClass3_1085 = null;
	aClass39_Sub5_Sub10_Sub3_1096 = null;
	aClass3_1103 = null;
	aClass3_1099 = null;
	aClass3_1094 = null;
    }
    
    public void method1042(boolean bool) {
	if (bool == true) {
	    anInt1083 += ++anInt1088;
	    for (int i = 0; i < 256; i++) {
		int i_30_ = anIntArray1102[i];
		if ((i & 0x2) != 0) {
		    if ((i & 0x1) != 0)
			anInt1101 ^= anInt1101 >>> 16;
		    else
			anInt1101 ^= anInt1101 << 2;
		} else if ((i & 0x1) != 0)
		    anInt1101 ^= anInt1101 >>> 6;
		else
		    anInt1101 ^= anInt1101 << 13;
		anInt1101 += anIntArray1102[i + 128 & 0xff];
		int i_31_;
		anIntArray1102[i] = i_31_
		    = (anInt1101
		       + (anIntArray1102[Class34.method342(1020, i_30_) >> 2]
			  + anInt1083));
		anIntArray1105[i] = anInt1083
		    = (anIntArray1102
		       [Class34.method342(i_31_, 261338) >> 8 >> 2]) + i_30_;
	    }
	}
    }
    
    public static void method1043(int i) {
	TraversalMap.aClass57_514.method1006(i + 1540585344);
	if (i != -1540585334)
	    method1044();
	BufferedFile.aClass39_Sub5_Sub10_Sub4_350.method695(0, 0);
	Class53.anIntArray970
	    = Class39_Sub5_Sub10_Sub2.method657(Class53.anIntArray970);
    }
    
    public static void method1044() {
	if (Class37.gameSocket != null) {
	    Class37.gameSocket.stop();
	    Class37.gameSocket = null;
	}
	Class62.method1045(false);
	Class44.aClass38_836.method378();
	for (int i_32_ = 0; i_32_ < 4; i_32_++)
	    Class39_Sub5_Sub12.aClass27Array1857[i_32_].reset((byte) -110);
	System.gc();
	Class41.method900((byte) -100, 10);
	ArchiveWorker.anInt1205 = -1;
	JSocket.anInt313 = 0;
	NpcDefinition.method723((byte) 115);
	Class39_Sub14.setState(10);
    }
    
    public IsaacPrng(int[] is) {
	anIntArray1102 = new int[256];
	for (int i = 0; i < is.length; i++)
	    anIntArray1105[i] = is[i];
	method1040(102);
    }
    
    static {
	aClass3_1094
	    = Class39_Sub5_Sub9.createJstring("Error loading your profile)3");
	aClass3_1085 = aClass3_1097;
	aClass3_1092
	    = Class39_Sub5_Sub9
		  .createJstring("Wen m-Ochten Sie von der Liste entfernen?");
	aBoolean1100 = false;
	aClass3_1082 = aClass3_1094;
	aClass3_1099 = Class39_Sub5_Sub9.createJstring("Enter object name");
	aBoolean1089 = false;
	aClass3_1098 = aClass3_1099;
	aClass3_1103 = Class39_Sub5_Sub9.createJstring("backleft1");
	aClass3_1086 = Class39_Sub5_Sub9.createJstring("(U4");
	anInt1091 = 0;
	anInt1095 = -1;
	aClass39_Sub6Array1104 = new Buffer[2048];
    }
}
