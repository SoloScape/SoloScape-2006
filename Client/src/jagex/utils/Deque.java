package jagex.utils;

import jagex.io.Buffer;
import jagex.utils.Cache;
import unpackaged.Canvas_Sub1;
import unpackaged.Class14;
import unpackaged.Class20;
import unpackaged.Class31;
import unpackaged.Class33;
import unpackaged.Class34;
import unpackaged.Class38;
import unpackaged.Class39_Sub14;
import unpackaged.Class39_Sub5_Sub12;
import unpackaged.Class39_Sub5_Sub14;
import unpackaged.Class39_Sub5_Sub4_Sub2;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class44;
import unpackaged.Class45;
import unpackaged.Class46_Sub1;
import unpackaged.Class50;
import unpackaged.Class55;
import unpackaged.Class62;
import unpackaged.Class62_Sub1;
import unpackaged.Class67;
import jagex.graphics.sprites.DirectColorSprite;
import unpackaged.FileTable;
import jagex.io.JSocket;
import unpackaged.NameTable;
import jagex.world.actors.Projectile;
import jagex.world.actors.StillGraphic;
import unpackaged.Widget;

/* Class49 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Deque
{
    public static JString aClass3_909;
    public static JString aClass3_910;
    public static int[] anIntArray911
	= { 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800,
	    12800, 12800, 12800, 12800, 12800, 12800, 12800 };
    public static int anInt912;
    public static JString aClass3_913;
    public static boolean aBoolean914;
    public static int anInt915 = 0;
    public static Cache fontCache;
    public static JString aClass3_917;
    public static FileTable objectFileLoader;
    public static int anInt919;
    public static DirectColorSprite aClass39_Sub5_Sub10_Sub3_920;
    public static JString aClass3_921;
    public Node head = new Node();
    public static JSocket odSocket;
    public Node iterator;
    public static JString aClass3_925;
    public static JString aClass3_926;
    public static JString aClass3_927;
    
    public void removeAll(int i) {
	for (;;) {
	    Node node = head.nextNode;
	    if (node == head)
		break;
	    node.unlinkDeque();
	}
    }
    
    public static JString decodeBase37(long l) {
	if (l <= 0L || l >= 6582952005840035281L)
	    return null;
	if (l % 37L == 0L)
	    return null;
	long l_0_ = l;
	int i_1_ = 0;
	while (l_0_ != 0L) {
	    l_0_ /= 37L;
	    i_1_++;
	}
	byte[] is = new byte[i_1_];
	while (l != 0L) {
	    long l_2_ = l;
	    l /= 37L;
	    is[--i_1_] = Cache.aByteArray111[(int) (-(l * 37L) + l_2_)];
	}
	JString class3 = new JString();
	class3.bytes = is;
	class3.length = is.length;
	return class3;
    }
    
    public static boolean method955(int i, int i_3_, int i_4_, int i_5_,
				    int i_6_, int i_7_, int i_8_) {
	if (!JSocket.loadWidget(i_5_))
	    return false;
	return Class20.drawWidgets((Class62_Sub1
				  .widgets[i_5_]),
				 i_8_, 0, -1, i_6_, 0, i_3_, 9843, i, i_4_);
    }
    
    public void offerLast(Node node) {
	if (node.prevNode != null)
	    node.unlinkDeque();
	node.nextNode = head;
	node.prevNode = head.prevNode;
	node.prevNode.nextNode = node;
	node.nextNode.prevNode = node;
    }
    
    public Node getPrev() {
	Node class39 = iterator;
	if (head == class39) {
	    iterator = null;
	    return null;
	}
	iterator = class39.prevNode;
	return class39;
    }
    
    public static void method958(byte i, int i_9_) {
	int[] is = (Class39_Sub5_Sub4_Sub2.aClass39_Sub5_Sub10_Sub3_2219
		    .anIntArray2476);
	int i_10_ = is.length;
	for (int i_11_ = 0; i_10_ > i_11_; i_11_++)
	    is[i_11_] = 0;
	for (int i_12_ = 1; i_12_ < 103; i_12_++) {
	    int i_13_ = (52736 - i_12_ * 512) * 4 + 24628;
	    for (int i_14_ = 1; i_14_ < 103; i_14_++) {
		if ((Class55.tileFlags[i_9_][i_14_][i_12_]
		     & 0x18)
		    == 0)
		    Class44.aClass38_836.method387(is, i_13_, 512, i_9_, i_14_,
						   i_12_);
		if (i_9_ < 3 && ((Class55.tileFlags[i_9_ + 1]
				  [i_14_][i_12_])
				 & 0x8) != 0)
		    Class44.aClass38_836.method387(is, i_13_, 512, i_9_ + 1,
						   i_14_, i_12_);
		i_13_ += 4;
	    }
	}
	Class39_Sub5_Sub4_Sub2.aClass39_Sub5_Sub10_Sub3_2219.method679();
	int i_15_ = (((int) (Math.random() * 20.0) - 10 + 238 << 8)
		     + ((int) (Math.random() * 20.0) + 238 - 10 << 16)
		     - (-(int) (Math.random() * 20.0) + 10) + 238);
	int i_16_ = (int) (Math.random() * 20.0) + 228 << 16;
	for (int i_17_ = 1; i_17_ < 103; i_17_++) {
	    for (int i_18_ = 1; i_18_ < 103; i_18_++) {
		if ((Class55.tileFlags[i_9_][i_18_][i_17_]
		     & 0x18)
		    == 0)
		    Class50.method971(i_9_, 1536, i_15_, i_18_, i_17_, i_16_);
		if (i_9_ < 3 && ((Class55.tileFlags[i_9_ + 1]
				  [i_18_][i_17_])
				 & 0x8) != 0)
		    Class50.method971(i_9_ + 1, 1536, i_15_, i_18_, i_17_,
				      i_16_);
	    }
	}
	anInt915 = 0;
	for (int i_19_ = 0; i_19_ < 104; i_19_++) {
	    for (int i_20_ = 0; i_20_ < 104; i_20_++) {
		int i_21_ = Class44.aClass38_836.method404(NameTable.height,
							   i_19_, i_20_);
		if (i_21_ != 0) {
		    i_21_ = (i_21_ & 0x1fffd23c) >> 14;
		    int i_22_
			= Canvas_Sub1.method40(i_21_, (byte) 39).anInt1962;
		    if (i_22_ >= 0) {
			int i_23_ = i_20_;
			int i_24_ = i_19_;
			if (i_22_ != 22 && i_22_ != 29 && i_22_ != 34
			    && i_22_ != 36 && i_22_ != 46 && i_22_ != 47
			    && i_22_ != 48) {
			    int[][] is_25_
				= (Class39_Sub5_Sub12.aClass27Array1857
				   [NameTable.height].adjancency);
			    for (int i_26_ = 0; i_26_ < 10; i_26_++) {
				int i_27_ = (int) (Math.random() * 4.0);
				if (i_27_ == 0 && i_24_ > 0
				    && i_19_ - 3 < i_24_
				    && ((is_25_[i_24_ - 1][i_23_] & 0x1280108)
					== 0))
				    i_24_--;
				if (i_27_ == 1 && i_24_ < 103
				    && i_19_ + 3 > i_24_
				    && ((is_25_[i_24_ + 1][i_23_] & 0x1280180)
					== 0))
				    i_24_++;
				if (i_27_ == 2 && i_23_ > 0
				    && i_20_ - 3 < i_23_
				    && ((is_25_[i_24_][i_23_ - 1] & 0x1280102)
					== 0))
				    i_23_--;
				if (i_27_ == 3 && i_23_ < 103
				    && i_20_ + 3 > i_23_
				    && ((is_25_[i_24_][i_23_ + 1] & 0x1280120)
					== 0))
				    i_23_++;
			    }
			}
			Class31.aClass39_Sub5_Sub10_Sub3Array560[anInt915]
			    = (Projectile
			       .aClass39_Sub5_Sub10_Sub3Array2205[i_22_]);
			Class46_Sub1.anIntArray1549[anInt915] = i_24_;
			Widget.anIntArray2111[anInt915] = i_23_;
			anInt915++;
		    }
		}
	    }
	}
    }
    
    public void method959(byte i, Node class39, Node class39_28_) {
	if (class39_28_.prevNode != null)
	    class39_28_.unlinkDeque();
	class39_28_.prevNode = class39.prevNode;
	class39_28_.nextNode = class39;
	class39_28_.prevNode.nextNode = class39_28_;
	class39_28_.nextNode.prevNode = class39_28_;
    }
    
    public Node pollLast() {
	Node node = head.prevNode;
	if (node == head)
	    return null;
	node.unlinkDeque();
	return node;
    }
    
    public static void loadMapTile(Buffer buffer, int i_29_,
				 int i_30_, int y, int i_32_, int x,
				 int height) {
	if (x < 0 || x >= 104 || y < 0 || y >= 104) {
	    for (;;) {
		int i_35_ = buffer.getUbyte();
		if (i_35_ == 0)
		    break;
		if (i_35_ == 1) {
		    buffer.getUbyte();
		    break;
		}
		if (i_35_ <= 49)
		    buffer.getUbyte();
	    }
	} else {
	    Class55.tileFlags[height][x][y] = (byte) 0;
	    for (;;) {
		int i_36_ = buffer.getUbyte();
		if (i_36_ == 0) {
		    if (height != 0)
			Class67.heightMap[height][x][y]
			    = (Class67.heightMap[height - 1]
			       [x][y]) - 240;
		    else
			Class67.heightMap[0][x][y]
			    = (-Class62.getPerlinNoise(i_32_ + y + 556238,
						   932731 + x + i_30_, 1)
			       * 8);
		    break;
		}
		if (i_36_ == 1) {
		    int i_37_ = buffer.getUbyte();
		    if (i_37_ == 1)
			i_37_ = 0;
		    if (height != 0)
			Class67.heightMap[height][x][y]
			    = (Class67.heightMap[height - 1]
			       [x][y]) - i_37_ * 8;
		    else
			Class67.heightMap[0][x][y]
			    = -i_37_ * 8;
		    break;
		}
		if (i_36_ <= 49) {
		    Class33.aByteArrayArrayArray596[height][x][y]
			= buffer.getByte();
		    Class67.aByteArrayArrayArray1179[height][x][y]
			= (byte) ((i_36_ - 2) / 4);
		    IsaacPrng.aByteArrayArrayArray1081[height][x][y]
			= (byte) Class34.method342(i_36_ + i_29_ - 2, 3);
		} else if (i_36_ <= 81)
		    Class55.tileFlags[height][x][y]
			= (byte) (i_36_ - 49);
		else
		    Class39_Sub14.aByteArrayArrayArray1516[height][x][y]
			= (byte) (i_36_ - 81);
	    }
	}
    }
    
    public Node getLast() {
	Node node = head.prevNode;
	if (head == node) {
	    iterator = null;
	    return null;
	}
	iterator = node.prevNode;
	return node;
    }
    
    public Node getNext() {
	Node node = iterator;
	if (node == head) {
	    iterator = null;
	    return null;
	}
	iterator = node.nextNode;
	return node;
    }
    
    public static void method964(int i) {
	Class45.aBoolean867 = false;
	Class38.aBoolean684 = false;
    }
    
    public Node pollFirst() {
	Node node = head.nextNode;
	if (node == head)
	    return null;
	node.unlinkDeque();
	return node;
    }
    
    public void offerFirst(Node node) {
	if (node.prevNode != null)
	    node.unlinkDeque();
	node.prevNode = head;
	node.nextNode = head.nextNode;
	node.prevNode.nextNode = node;
	node.nextNode.prevNode = node;
    }
    
    public static void method967(Widget class39_sub5_sub17,
				 byte i) {
	int i_38_ = class39_sub5_sub17.anInt2084 >> 16;
	if (Class39_Sub5_Sub14.anInt1912 == i_38_
	    || IsaacPrng.anInt1095 == i_38_)
	    Class14.aBoolean245 = true;
	if (StillGraphic.anInt2338 == i_38_
	    || i_38_ == Class39_Sub5_Sub14.anIntArray1914[Node.anInt728])
	    Class39_Sub14.aBoolean1520 = true;
    }
    
    public static void method968(byte i) {
	anIntArray911 = null;
	aClass3_910 = null;
	aClass3_921 = null;
	aClass3_925 = null;
	objectFileLoader = null;
	aClass39_Sub5_Sub10_Sub3_920 = null;
	aClass3_917 = null;
	aClass3_913 = null;
	aClass3_909 = null;
	fontCache = null;
	aClass3_926 = null;
	aClass3_927 = null;
	odSocket = null;
    }
    
    public Node getFirst() {
	Node node = head.nextNode;
	if (head == node) {
	    iterator = null;
	    return null;
	}
	iterator = node.nextNode;
	return node;
    }
    
    public Deque() {
	head.prevNode = head;
	head.nextNode = head;
    }
    
    static {
	aBoolean914 = false;
	aClass3_917 = Class39_Sub5_Sub9.createJstring("welle:");
	aClass3_921 = Class39_Sub5_Sub9.createJstring("Password: ");
	anInt912 = 78;
	aClass3_909 = Class39_Sub5_Sub9.createJstring("Handel)4Duell");
	aClass3_913 = Class39_Sub5_Sub9.createJstring("Offline");
	aClass3_910 = aClass3_921;
	fontCache = new Cache(20);
	aClass3_925 = Class39_Sub5_Sub9.createJstring("Loading fonts )2 ");
	aClass3_927 = Class39_Sub5_Sub9.createJstring("mapdots");
	aClass3_926 = aClass3_925;
    }
}
