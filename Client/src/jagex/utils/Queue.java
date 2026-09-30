package jagex.utils;

/* Class54 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.io.Buffer;
import java.awt.Component;
import jagex.graphics.AbstractImage;
import unpackaged.ArchiveRequest;
import jagex.graphics.BitmapFont;
import unpackaged.Canvas_Sub1;
import unpackaged.Class14;
import unpackaged.Class38;
import unpackaged.Class39_Sub13;
import unpackaged.Class39_Sub14;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class46;
import unpackaged.Class55;
import unpackaged.Class62_Sub1;
import unpackaged.Class62_Sub2;
import unpackaged.Class65;
import unpackaged.Client;
import unpackaged.FileTable;
import jagex.world.actors.GroundItem;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.graphics.JImage;
import unpackaged.JKeyListener;
import jagex.world.actors.Npc;
import unpackaged.ObjectDefinition;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;

public class Queue
{
    public static FileTable aClass9_971;
    public static byte[][] aByteArrayArray972 = new byte[50][];
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_973;
    public static JString aClass3_974
	= Class39_Sub5_Sub9.createJstring("scrollen:");
    public SubNode head = new SubNode();
    public static JString aClass3_976;
    public static JString aClass3_977;
    public static int[] anIntArray978 = { 1, 0, -1, 0 };
    public static int anInt979 = 0;
    public static JString aClass3_980
	= Class39_Sub5_Sub9.createJstring(",Zffentlicher Chat");
    public static AbstractImage aClass57_981;
    public static JString aClass3_982;
    public static JString aClass3_983;
    public static JString aClass3_984 = Class39_Sub5_Sub9.createJstring("(Y");
    public static JString aClass3_985;
    public static JString aClass3_986
	= Class39_Sub5_Sub9
	      .createJstring("Die Adresse dieses Computers wurde gesperrt)1");
    public static int anInt987;
    public static JString aClass3_988;
    public static JString aClass3_989;
    public static JString aClass3_990;
    public static JString aClass3_991;
    
    public static void method987(int i) {
	if (Class55.aClass62_1251 != null) {
	    Class46.method941((byte) -77);
	    if (ArchiveRequest.anInt1415 > 0) {
		Class55.aClass62_1251.method1053(256, (byte) 55);
		ArchiveRequest.anInt1415 = 0;
	    }
	    Class55.aClass62_1251.destroy();
	    Class55.aClass62_1251 = null;
	}
    }
    
    public SubNode peekFirst() {
	SubNode class39_sub5 = head.nextSubnode;
	if (head == class39_sub5)
	    return null;
	return class39_sub5;
    }
    
    public static int or(int v0, int v1) {
	return v0 | v1;
    }
    
    public static void method990
	(boolean bool, Class39_Sub5_Sub4_Sub4 class39_sub5_sub4_sub4) {
	if (class39_sub5_sub4_sub4.anInt2250 != 0) {
	    if (class39_sub5_sub4_sub4.anInt2260 != -1
		&& class39_sub5_sub4_sub4.anInt2260 < 32768) {
		Npc class39_sub5_sub4_sub4_sub1
		    = (GroundItem
		       .aClass39_Sub5_Sub4_Sub4_Sub1Array2241
		       [class39_sub5_sub4_sub4.anInt2260]);
		if (class39_sub5_sub4_sub4_sub1 != null) {
		    int i = (-class39_sub5_sub4_sub4_sub1.anInt2301
			     + class39_sub5_sub4_sub4.anInt2301);
		    int i_1_ = (class39_sub5_sub4_sub4.anInt2275
				- class39_sub5_sub4_sub4_sub1.anInt2275);
		    if (i != 0 || i_1_ != 0)
			class39_sub5_sub4_sub4.anInt2294
			    = (int) (Math.atan2((double) i, (double) i_1_)
				     * 325.949) & 0x7ff;
		}
	    }
	    if (class39_sub5_sub4_sub4.anInt2260 >= 32768) {
		int i = class39_sub5_sub4_sub4.anInt2260 - 32768;
		if (Class39_Sub13.anInt1501 == i)
		    i = 2047;
		Player class39_sub5_sub4_sub4_sub2
		    = Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i];
		if (class39_sub5_sub4_sub4_sub2 != null) {
		    int i_2_ = (-class39_sub5_sub4_sub4_sub2.anInt2301
				+ class39_sub5_sub4_sub4.anInt2301);
		    int i_3_ = (-class39_sub5_sub4_sub4_sub2.anInt2275
				+ class39_sub5_sub4_sub4.anInt2275);
		    if (i_2_ != 0 || i_3_ != 0)
			class39_sub5_sub4_sub4.anInt2294
			    = (int) (Math.atan2((double) i_2_, (double) i_3_)
				     * 325.949) & 0x7ff;
		}
	    }
	    if ((class39_sub5_sub4_sub4.anInt2316 != 0
		 || class39_sub5_sub4_sub4.anInt2300 != 0)
		&& (class39_sub5_sub4_sub4.anInt2312 == 0
		    || class39_sub5_sub4_sub4.anInt2274 > 0)) {
		int i
		    = -((-Class65.anInt1145 + (class39_sub5_sub4_sub4.anInt2316
					       - Class65.anInt1145))
			* 64) + class39_sub5_sub4_sub4.anInt2301;
		int i_4_
		    = (class39_sub5_sub4_sub4.anInt2275
		       - (-JKeyListener.anInt618 + class39_sub5_sub4_sub4.anInt2300
			  - JKeyListener.anInt618) * 64);
		if (i != 0 || i_4_ != 0)
		    class39_sub5_sub4_sub4.anInt2294
			= (int) (Math.atan2((double) i, (double) i_4_)
				 * 325.949) & 0x7ff;
		class39_sub5_sub4_sub4.anInt2300 = 0;
		class39_sub5_sub4_sub4.anInt2316 = 0;
	    }
	    int i = ((-class39_sub5_sub4_sub4.anInt2251
		      + class39_sub5_sub4_sub4.anInt2294)
		     & 0x7ff);
	    if (i != 0) {
		class39_sub5_sub4_sub4.anInt2267++;
		if (i <= 1024) {
		    class39_sub5_sub4_sub4.anInt2251
			+= class39_sub5_sub4_sub4.anInt2250;
		    boolean bool_5_ = true;
		    if (i < class39_sub5_sub4_sub4.anInt2250
			|| -class39_sub5_sub4_sub4.anInt2250 + 2048 < i) {
			bool_5_ = false;
			class39_sub5_sub4_sub4.anInt2251
			    = class39_sub5_sub4_sub4.anInt2294;
		    }
		    if ((class39_sub5_sub4_sub4.anInt2303
			 == class39_sub5_sub4_sub4.anInt2317)
			&& (class39_sub5_sub4_sub4.anInt2267 > 25
			    || bool_5_)) {
			if (class39_sub5_sub4_sub4.anInt2280 != -1)
			    class39_sub5_sub4_sub4.anInt2303
				= class39_sub5_sub4_sub4.anInt2280;
			else
			    class39_sub5_sub4_sub4.anInt2303
				= class39_sub5_sub4_sub4.anInt2264;
		    }
		} else {
		    boolean bool_6_ = true;
		    class39_sub5_sub4_sub4.anInt2251
			-= class39_sub5_sub4_sub4.anInt2250;
		    if (i < class39_sub5_sub4_sub4.anInt2250
			|| i > -class39_sub5_sub4_sub4.anInt2250 + 2048) {
			class39_sub5_sub4_sub4.anInt2251
			    = class39_sub5_sub4_sub4.anInt2294;
			bool_6_ = false;
		    }
		    if ((class39_sub5_sub4_sub4.anInt2303
			 == class39_sub5_sub4_sub4.anInt2317)
			&& (class39_sub5_sub4_sub4.anInt2267 > 25
			    || bool_6_)) {
			if (class39_sub5_sub4_sub4.anInt2263 != -1)
			    class39_sub5_sub4_sub4.anInt2303
				= class39_sub5_sub4_sub4.anInt2263;
			else
			    class39_sub5_sub4_sub4.anInt2303
				= class39_sub5_sub4_sub4.anInt2264;
		    }
		}
		class39_sub5_sub4_sub4.anInt2251 &= 0x7ff;
	    } else
		class39_sub5_sub4_sub4.anInt2267 = 0;
	}
    }
    
    public static BitmapFont createBitmapFont(FileTable fileTable, int archiveId, int childId) {
	if (!Client.decodeBitmapFont(fileTable, archiveId, childId))
	    return null;
	return Class39_Sub14.createBitmapFont();
    }
    
    public void offerLast(SubNode node) {
	if (node.prevSubnode != null)
	    node.unlinkQueue();
	node.nextSubnode = head.nextSubnode;
	node.prevSubnode = head;
	node.prevSubnode.nextSubnode = node;
	node.nextSubnode.prevSubnode = node;
    }
    
    public static void method993(byte[] is, int i, int i_9_, boolean bool,
				 int i_10_, int i_11_, Class38 class38,
				 int i_12_, int i_13_, int i_14_,
				 TraversalMap[] class27s) {
	Buffer class39_sub6 = new Buffer(is);
	int i_15_ = -1;
	for (;;) {
	    int i_16_ = class39_sub6.getSmartB();
	    if (i_16_ == 0)
		break;
	    i_15_ += i_16_;
	    int i_17_ = 0;
	    for (;;) {
		int i_18_ = class39_sub6.getSmartB();
		if (i_18_ == 0)
		    break;
		i_17_ += i_18_ - 1;
		int i_19_ = i_17_ & 0x3f;
		int i_20_ = (i_17_ & 0xfc6) >> 6;
		int i_21_ = i_17_ >> 12;
		int i_22_ = class39_sub6.getUbyte();
		int i_23_ = i_22_ >> 2;
		int i_24_ = i_22_ & 0x3;
		if (i_11_ == i_21_ && i_20_ >= i_12_ && i_12_ + 8 > i_20_
		    && i_13_ <= i_19_ && i_19_ < i_13_ + 8) {
		    ObjectDefinition class39_sub5_sub15
			= Canvas_Sub1.method40(i_15_, (byte) 51);
		    int i_25_
			= (i_10_
			   + Class62_Sub2.method1078(i, i_20_ & 0x7,
						     i_19_ & 0x7, 7, i_24_,
						     (class39_sub5_sub15
						      .anInt1948),
						     (class39_sub5_sub15
						      .anInt1925)));
		    int i_26_
			= (i_14_
			   + Class14.method210(class39_sub5_sub15.anInt1925,
					       i_20_ & 0x7,
					       class39_sub5_sub15.anInt1948,
					       i_24_, i, i_19_ & 0x7, 0));
		    if (i_25_ > 0 && i_26_ > 0 && i_25_ < 103 && i_26_ < 103) {
			TraversalMap class27 = null;
			int i_27_ = i_9_;
			if ((Class55.tileFlags[1][i_25_][i_26_]
			     & 0x2)
			    == 2)
			    i_27_--;
			if (i_27_ >= 0)
			    class27 = class27s[i_27_];
			Class62_Sub1.method1059(i_23_, class38, i_26_, i_15_,
						i_9_, i_25_, class27, true,
						i_24_ + i & 0x3);
		    }
		}
	    }
	}
    }
    
    public static AbstractImage method994(Component component, int i, int i_28_,
				    byte i_29_) {
	try {
	    Class var_class = Class.forName("Class57_Sub1");
	    AbstractImage class57 = (AbstractImage) var_class.newInstance();
	    class57.setComponent(component, i, i_28_);
	    return class57;
	} catch (Throwable throwable) {
	    JImage class57_sub2 = new JImage();
	    class57_sub2.setComponent(component, i, i_28_);
	    return class57_sub2;
	}
    }
    
    public Queue() {
	head.prevSubnode = head;
	head.nextSubnode = head;
    }
    
    public static void method995(byte i) {
	anIntArray978 = null;
	aByteArrayArray972 = null;
	aClass9_971 = null;
	aClass39_Sub5_Sub10_Sub4_973 = null;
	aClass3_974 = null;
	aClass3_990 = null;
	aClass3_984 = null;
	aClass3_983 = null;
	aClass3_986 = null;
	aClass3_977 = null;
	aClass3_991 = null;
	aClass57_981 = null;
	aClass3_976 = null;
	aClass3_985 = null;
	aClass3_980 = null;
	aClass3_982 = null;
	aClass3_988 = null;
	aClass3_989 = null;
    }
    
    public void offerFirst(SubNode node) {
	if (node.prevSubnode != null)
	    node.unlinkQueue();
	node.nextSubnode = head;
	node.prevSubnode = head.prevSubnode;
	node.prevSubnode.nextSubnode = node;
	node.nextSubnode.prevSubnode = node;
    }
    
    public SubNode pollFirst() {
	SubNode node = head.nextSubnode;
	if (node == head)
	    return null;
	node.unlinkQueue();
	return node;
    }
    
    static {
	aClass3_985
	    = Class39_Sub5_Sub9
		  .createJstring("as it was used to break our rules)3");
	aClass3_990
	    = Class39_Sub5_Sub9.createJstring("Connection timed out)3");
	aClass3_977 = aClass3_985;
	aClass3_991
	    = Class39_Sub5_Sub9.createJstring("Bitte warten Sie)3)3)3");
	aClass3_976 = aClass3_990;
	aClass3_988 = Class39_Sub5_Sub9.createJstring("Lade Titelbild )2 ");
	anInt987 = 0;
	aClass3_982 = Class39_Sub5_Sub9.createJstring("Connection lost");
	aClass3_983 = aClass3_982;
	aClass3_989 = Class39_Sub5_Sub9.createJstring(")1");
    }
}
