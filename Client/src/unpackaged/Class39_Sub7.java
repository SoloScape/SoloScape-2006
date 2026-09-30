package unpackaged;

/* Class39_Sub7 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.Huffmans;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Npc;
import jagex.world.map.TraversalMap;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.io.DataInputStream;
import java.net.URL;

public class Class39_Sub7 extends Node
{
    public static int[] intStack = new int[1000];
    public int anInt1372;
    public static volatile boolean aBoolean1373 = false;
    public static long[] aLongArray1374 = new long[32];
    public static int[] anIntArray1375;
    public int[] anIntArray1376;
    public Resource[] aClass56Array1377;
    public int[] anIntArray1378;
    public int[] anIntArray1379;
    public static int anInt1380 = 0;
    public byte[][][] aByteArrayArrayArray1381;
    public int anInt1382;
    public Resource[] aClass56Array1383;
    public static JString aClass3_1384;
    public static long[] aLongArray1385;
    public static int anInt1386;
    public static int anInt1387;
    public static JString aClass3_1388;
    
    public static void decodeBitmapFont(byte[] is) {
	Buffer class39_sub6 = new Buffer(is);
	class39_sub6.offset = is.length - 2;
	JSocket.anInt302 = class39_sub6.getUword();
	Class39_Sub5_Sub9.anIntArray1799 = new int[JSocket.anInt302];
	TraversalMap.aByteArrayArray517 = new byte[JSocket.anInt302][];
	SubNode.anIntArray1352 = new int[JSocket.anInt302];
	Class39_Sub14.anIntArray1512 = new int[JSocket.anInt302];
	Class46_Sub1.anIntArray1548 = new int[JSocket.anInt302];
	class39_sub6.offset = is.length - 7 - JSocket.anInt302 * 8;
	Class13.anInt203 = class39_sub6.getUword();
	Class39_Sub5_Sub12.anInt1854 = class39_sub6.getUword();
	int i_0_ = (class39_sub6.getUbyte() & 0xff) + 1;
	for (int i_1_ = 0; JSocket.anInt302 > i_1_; i_1_++)
	    Class46_Sub1.anIntArray1548[i_1_] = class39_sub6.getUword();
	for (int i_2_ = 0; JSocket.anInt302 > i_2_; i_2_++)
	    SubNode.anIntArray1352[i_2_] = class39_sub6.getUword();
	for (int i_3_ = 0; JSocket.anInt302 > i_3_; i_3_++)
	    Class39_Sub5_Sub9.anIntArray1799[i_3_]
		= class39_sub6.getUword();
	for (int i_4_ = 0; i_4_ < JSocket.anInt302; i_4_++)
	    Class39_Sub14.anIntArray1512[i_4_] = class39_sub6.getUword();
	class39_sub6.offset
	    = 3 - i_0_ * 3 + (is.length - 7 - JSocket.anInt302 * 8);
	Class39_Sub11.anIntArray1460 = new int[i_0_];
	for (int i_5_ = 1; i_0_ > i_5_; i_5_++) {
	    Class39_Sub11.anIntArray1460[i_5_]
		= class39_sub6.getUtri((byte) -62);
	    if (Class39_Sub11.anIntArray1460[i_5_] == 0)
		Class39_Sub11.anIntArray1460[i_5_] = 1;
	}
	class39_sub6.offset = 0;
	for (int i_6_ = 0; JSocket.anInt302 > i_6_; i_6_++) {
	    int i_7_ = Class39_Sub14.anIntArray1512[i_6_];
	    int i_8_ = Class39_Sub5_Sub9.anIntArray1799[i_6_];
	    int i_9_ = i_7_ * i_8_;
	    byte[] is_10_ = new byte[i_9_];
	    TraversalMap.aByteArrayArray517[i_6_] = is_10_;
	    int i_11_ = class39_sub6.getUbyte();
	    if (i_11_ == 0) {
		for (int i_12_ = 0; i_12_ < i_9_; i_12_++)
		    is_10_[i_12_] = class39_sub6.getByte();
	    } else if (i_11_ == 1) {
		for (int i_13_ = 0; i_13_ < i_8_; i_13_++) {
		    for (int i_14_ = 0; i_14_ < i_7_; i_14_++)
			is_10_[i_8_ * i_14_ + i_13_]
			    = class39_sub6.getByte();
		}
	    }
	}
    }
    
    public static void method846(Class39_Sub5_Sub4_Sub4 class39_sub5_sub4_sub4,
				 int i) {
	class39_sub5_sub4_sub4.aBoolean2298 = false;
	if (i != 1000000)
	    method849(null, 39, null);
	if (class39_sub5_sub4_sub4.anInt2303 != -1) {
	    Class39_Sub5_Sub11 class39_sub5_sub11
		= Class62_Sub1.method1064(class39_sub5_sub4_sub4.anInt2303,
					  (byte) 54);
	    if (class39_sub5_sub11 != null
		&& class39_sub5_sub11.anIntArray1833 != null) {
		class39_sub5_sub4_sub4.anInt2283++;
		if ((class39_sub5_sub4_sub4.anInt2306
		     < class39_sub5_sub11.anIntArray1833.length)
		    && ((class39_sub5_sub11.anIntArray1831
			 [class39_sub5_sub4_sub4.anInt2306])
			< class39_sub5_sub4_sub4.anInt2283)) {
		    class39_sub5_sub4_sub4.anInt2306++;
		    class39_sub5_sub4_sub4.anInt2283 = 1;
		}
		if (class39_sub5_sub11.anIntArray1833.length
		    <= class39_sub5_sub4_sub4.anInt2306) {
		    class39_sub5_sub4_sub4.anInt2306 = 0;
		    class39_sub5_sub4_sub4.anInt2283 = 0;
		}
	    } else
		class39_sub5_sub4_sub4.anInt2303 = -1;
	}
	if (class39_sub5_sub4_sub4.anInt2270 != -1
	    && Class2.logicCycle >= class39_sub5_sub4_sub4.anInt2272) {
	    if (class39_sub5_sub4_sub4.anInt2276 < 0)
		class39_sub5_sub4_sub4.anInt2276 = 0;
	    int i_15_
		= (Huffmans.method881
		   (i ^ 0xf4240, class39_sub5_sub4_sub4.anInt2270).anInt2126);
	    if (i_15_ != -1) {
		Class39_Sub5_Sub11 class39_sub5_sub11
		    = Class62_Sub1.method1064(i_15_, (byte) 54);
		if (class39_sub5_sub11 != null
		    && class39_sub5_sub11.anIntArray1833 != null) {
		    class39_sub5_sub4_sub4.anInt2304++;
		    if ((class39_sub5_sub4_sub4.anInt2276
			 < class39_sub5_sub11.anIntArray1833.length)
			&& (class39_sub5_sub4_sub4.anInt2304
			    > (class39_sub5_sub11.anIntArray1831
			       [class39_sub5_sub4_sub4.anInt2276]))) {
			class39_sub5_sub4_sub4.anInt2276++;
			class39_sub5_sub4_sub4.anInt2304 = 1;
		    }
		    if ((class39_sub5_sub11.anIntArray1833.length
			 <= class39_sub5_sub4_sub4.anInt2276)
			&& (class39_sub5_sub4_sub4.anInt2276 < 0
			    || (class39_sub5_sub4_sub4.anInt2276
				>= class39_sub5_sub11.anIntArray1833.length)))
			class39_sub5_sub4_sub4.anInt2270 = -1;
		} else
		    class39_sub5_sub4_sub4.anInt2270 = -1;
	    } else
		class39_sub5_sub4_sub4.anInt2270 = -1;
	}
	if (class39_sub5_sub4_sub4.anInt2268 != -1
	    && class39_sub5_sub4_sub4.anInt2305 <= 1) {
	    Class39_Sub5_Sub11 class39_sub5_sub11
		= Class62_Sub1.method1064(class39_sub5_sub4_sub4.anInt2268,
					  (byte) 54);
	    if (class39_sub5_sub11.anInt1840 == 1
		&& class39_sub5_sub4_sub4.anInt2254 > 0
		&& Class2.logicCycle >= class39_sub5_sub4_sub4.anInt2256
		&& Class2.logicCycle > class39_sub5_sub4_sub4.anInt2261) {
		class39_sub5_sub4_sub4.anInt2305 = 1;
		return;
	    }
	}
	if (class39_sub5_sub4_sub4.anInt2268 != -1
	    && class39_sub5_sub4_sub4.anInt2305 == 0) {
	    Class39_Sub5_Sub11 class39_sub5_sub11
		= Class62_Sub1.method1064(class39_sub5_sub4_sub4.anInt2268,
					  (byte) 54);
	    if (class39_sub5_sub11 != null
		&& class39_sub5_sub11.anIntArray1833 != null) {
		class39_sub5_sub4_sub4.anInt2311++;
		if ((class39_sub5_sub11.anIntArray1833.length
		     > class39_sub5_sub4_sub4.anInt2265)
		    && ((class39_sub5_sub11.anIntArray1831
			 [class39_sub5_sub4_sub4.anInt2265])
			< class39_sub5_sub4_sub4.anInt2311)) {
		    class39_sub5_sub4_sub4.anInt2265++;
		    class39_sub5_sub4_sub4.anInt2311 = 1;
		}
		if (class39_sub5_sub4_sub4.anInt2265
		    >= class39_sub5_sub11.anIntArray1833.length) {
		    class39_sub5_sub4_sub4.anInt2291++;
		    if (class39_sub5_sub4_sub4.anInt2291
			>= class39_sub5_sub11.anInt1829)
			class39_sub5_sub4_sub4.anInt2268 = -1;
		    class39_sub5_sub4_sub4.anInt2265
			-= class39_sub5_sub11.anInt1839;
		    if (class39_sub5_sub4_sub4.anInt2265 < 0
			|| (class39_sub5_sub11.anIntArray1833.length
			    <= class39_sub5_sub4_sub4.anInt2265))
			class39_sub5_sub4_sub4.anInt2268 = -1;
		}
		class39_sub5_sub4_sub4.aBoolean2298
		    = class39_sub5_sub11.aBoolean1838;
	    } else
		class39_sub5_sub4_sub4.anInt2268 = -1;
	}
	if (class39_sub5_sub4_sub4.anInt2305 > 0)
	    class39_sub5_sub4_sub4.anInt2305--;
    }
    
    public static void method847(byte i) {
	aLongArray1374 = null;
	anIntArray1375 = null;
	aLongArray1385 = null;
	aClass3_1384 = null;
	aClass3_1388 = null;
	intStack = null;
    }
    
    public static int method848(int i, int i_16_, byte i_17_, int i_18_) {
	i_16_ &= 0x3;
	if (i_16_ == 0)
	    return i_18_;
	if (i_16_ == 1)
	    return -i + 7;
	if (i_16_ == 2)
	    return -i_18_ + 7;
	return i;
    }
    
    public static void method849(Throwable throwable, int i, String string) {
	do {
	    try {
		String error = "";
		if (throwable != null)
		    error = Huffmans.method884(throwable, (byte) -27);
		if (string != null) {
		    if (throwable != null)
			error += " | ";
		    error += (String) string;
		}
		System.out.println("Error: " + error);
		error = error.replace(':', '.');
		error = error.replace('@', '_');
		error = error.replace('&', '_');
		error = error.replace('#', '_');
		Resource class56 = (Cache.aClass21_108.requestDataInputStream(new URL(Cache.aClass21_108.anApplet416.getCodeBase(),("clienterror.ws?c=" + ArchiveWorker.anInt1198+ "&u=" + Client.aLong1278 + "&v1="
				 + Signlink.javaVendor + "&v2="
				 + Signlink.javaVersion + "&e=" + error))));
		while (class56.returnCode == 0)
		    Class45.sleep(1L);
		if (class56.returnCode != 1)
		    break;
		DataInputStream datainputstream = (DataInputStream) class56.returnObject;
		datainputstream.read();
		datainputstream.close();
	    } catch (Exception exception) {
		break;
	    }
	    break;
	} while (false);
    }
    
    public static FileLoader createFileLoader(int i, boolean bool, boolean bool_20_, boolean bool_21_) {
	CacheIO cache = null;
	if (Class43.mainFile != null)
	    cache = new CacheIO(i, Class43.mainFile, Npc.indexFiles[i], 1000000);
	return new FileLoader(cache, Class14.tableCache, i, bool_21_, bool, bool_20_);
    }
    
    public static int method851(int i, int i_23_, int i_24_, int i_25_,
				int i_26_) {
	int i_27_
	    = (65536 - Class39_Sub5_Sub10_Sub2.cosineTable[i * 1024 / i_25_]
	       >> 1);
	return ((-i_27_ + 65536) * i_24_ >> 16) + (i_27_ * i_23_ >> 16);
    }
    
    public static DirectColorSprite[] method852
	(JString class3, FileTable class9, JString class3_28_, int i) {
	int i_29_ = class9.lookupArchive(class3);
	int i_30_ = class9.lookupChild(i_29_, class3_28_);
	return Class23.method273(i_30_, (byte) -49, i_29_, class9);
    }
    
    static {
	anIntArray1375 = new int[256];
	aClass3_1384 = Class39_Sub5_Sub9.createJstring("overlay_multiway");
	for (int i = 0; i < 256; i++) {
	    int i_31_ = i;
	    for (int i_32_ = 0; i_32_ < 8; i_32_++) {
		if ((i_31_ & 0x1) == 1)
		    i_31_ = ~0x12477cdf ^ i_31_ >>> 1;
		else
		    i_31_ >>>= 1;
	    }
	    anIntArray1375[i] = i_31_;
	}
	anInt1386 = 0;
	aLongArray1385 = new long[100];
	anInt1387 = 10;
	aClass3_1388 = Class39_Sub5_Sub9.createJstring("mapfunction");
    }
}
