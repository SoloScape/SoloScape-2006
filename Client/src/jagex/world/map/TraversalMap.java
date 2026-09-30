package jagex.world.map;

import jagex.utils.Queue;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.utils.Cache;
import jagex.graphics.AbstractImage;
import unpackaged.ArchiveWorker;
import jagex.io.BufferedFile;
import unpackaged.Class14;
import unpackaged.Class15;
import unpackaged.Class23;
import unpackaged.Class26;
import unpackaged.Class31;
import unpackaged.Class32;
import unpackaged.Class34;
import unpackaged.Class39_Sub12;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class39_Sub7;
import unpackaged.Class41;
import unpackaged.Class53;
import unpackaged.Class55;
import unpackaged.Class63;
import unpackaged.Class66;
import unpackaged.Class68;
import unpackaged.Client;
import unpackaged.FileLoader;
import unpackaged.FileTable;
import jagex.world.actors.GroundItem;
import jagex.utils.HashTable;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.world.actors.Projectile;
import jagex.world.actors.StillGraphic;
import unpackaged.Widget;

/* Class27 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class TraversalMap
{
    public static boolean aBoolean504;
    public int anInt505;
    public static int[] anIntArray506;
    public int[][] adjancency;
    public static JString aClass3_508
	= Class39_Sub5_Sub9.createJstring(" seconds)3");
    public static JString aClass3_509;
    public static JString aClass3_510;
    public int anInt511;
    public static JString aClass3_512;
    public int anInt513;
    public static AbstractImage aClass57_514;
    public static int anInt515;
    public static AbstractImage aClass57_516;
    public static byte[][] aByteArrayArray517;
    public static int anInt518;
    public static JString aClass3_519;
    public static FileLoader fileLoader8;
    public int anInt521;
    
    public void method295(int i, int i_0_, int i_1_, int i_2_, byte i_3_,
			  boolean bool, int i_4_) {
	int i_5_ = 256;
	if (i == 1 || i == 3) {
	    int i_6_ = i_4_;
	    i_4_ = i_0_;
	    i_0_ = i_6_;
	}
	i_1_ -= anInt513;
	if (bool)
	    i_5_ += 131072;
	i_2_ -= anInt521;
	for (int i_7_ = i_2_; i_7_ < i_2_ + i_4_; i_7_++) {
	    if (i_7_ >= 0 && anInt505 > i_7_) {
		for (int i_8_ = i_1_; i_8_ < i_1_ + i_0_; i_8_++) {
		    if (i_8_ >= 0 && anInt511 > i_8_)
			method301(i_8_, i_7_, (byte) 25, i_5_);
		}
	    }
	}
    }
    
    public static void method296(int i, int i_9_, boolean bool, int i_10_) {
	if (Class39_Sub5_Sub9.anInt1810 != 0 && i_9_ != 0
	    && Projectile.anInt2180 < 50) {
	    ArchiveWorker.anIntArray1201[Projectile.anInt2180] = i_10_;
	    FileLoader.anIntArray1295[Projectile.anInt2180]
		= i_9_;
	    Class39_Sub5_Sub9.anIntArray1811[Projectile.anInt2180]
		= i;
	    Class15.aClass52Array285[Projectile.anInt2180] = null;
	    FrameBuffer.anIntArray2150[Projectile.anInt2180]
		= 0;
	    Projectile.anInt2180++;
	}
    }
    
    public boolean method297(int i, byte i_11_, int i_12_, int i_13_,
			     int i_14_, int i_15_, int i_16_, int i_17_) {
	int i_18_ = i_15_ + i_13_ - 1;
	int i_19_ = i_17_ - 1 + i_16_;
	if (i_14_ >= i_16_ && i_14_ <= i_19_ && i_13_ <= i_12_
	    && i_18_ >= i_12_)
	    return true;
	if (i_14_ == i_16_ - 1 && i_12_ >= i_13_ && i_12_ <= i_18_
	    && (adjancency[-anInt521 + i_14_][-anInt513 + i_12_]
		& 0x8) == 0
	    && (i & 0x8) == 0)
	    return true;
	if (i_19_ + 1 == i_14_ && i_12_ >= i_13_ && i_18_ >= i_12_
	    && (adjancency[-anInt521 + i_14_][-anInt513 + i_12_]
		& 0x80) == 0
	    && (i & 0x2) == 0)
	    return true;
	if (i_12_ == i_13_ - 1 && i_16_ <= i_14_ && i_19_ >= i_14_
	    && (adjancency[-anInt521 + i_14_][-anInt513 + i_12_]
		& 0x2) == 0
	    && (i & 0x4) == 0)
	    return true;
	if (i_12_ == i_18_ + 1 && i_14_ >= i_16_ && i_19_ >= i_14_
	    && (adjancency[i_14_ - anInt521][-anInt513 + i_12_]
		& 0x20) == 0
	    && (i & 0x1) == 0)
	    return true;
	return false;
    }
    
    public static void method298(byte i) {
	if (Class39_Sub12.aBoolean1498) {
	    HashTable.anIntArray377 = null;
	    Class23.anIntArray423 = null;
	    Class14.aClass57_246 = null;
	    Class53.anIntArray969 = null;
	    ArchiveWorker.anIntArray1202 = null;
	    BufferedFile.anIntArray338 = null;
	    IsaacPrng.aClass39_Sub5_Sub10_Sub3_1096 = null;
	    GroundItem.anIntArray2247 = null;
	    Class32.aClass57_585 = null;
	    Cache.anIntArray112 = null;
	    Class31.aClass57_551 = null;
	    StillGraphic.aClass39_Sub5_Sub10_Sub4_2337 = null;
	    Widget.aClass57_2115 = null;
	    GroundItem.anIntArray2245 = null;
	    Class68.aClass57_1186 = null;
	    Client.aClass57_1267 = null;
	    Player.aClass57_2525 = null;
	    Class66.aClass57_1156 = null;
	    Class55.aClass39_Sub5_Sub10_Sub4_1257 = null;
	    AbstractImage.aClass39_Sub5_Sub10_Sub4Array1002 = null;
	    Deque.aClass39_Sub5_Sub10_Sub3_920 = null;
	    Class26.aClass57_483 = null;
	    Class41.method900((byte) -93, 10);
	    Class63.writeOdStatus(true);
	    Class39_Sub12.aBoolean1498 = false;
	}
    }
    
    public void method299(byte i, int i_20_, int i_21_, int i_22_, int i_23_,
			  boolean bool) {
	i_21_ -= anInt521;
	i_22_ -= anInt513;
	if (i_23_ == 0) {
	    if (i_20_ == 0) {
		method301(i_22_, i_21_, (byte) 25, 128);
		method301(i_22_, i_21_ - 1, (byte) 25, 8);
	    }
	    if (i_20_ == 1) {
		method301(i_22_, i_21_, (byte) 25, 2);
		method301(i_22_ + 1, i_21_, (byte) 25, 32);
	    }
	    if (i_20_ == 2) {
		method301(i_22_, i_21_, (byte) 25, 8);
		method301(i_22_, i_21_ + 1, (byte) 25, 128);
	    }
	    if (i_20_ == 3) {
		method301(i_22_, i_21_, (byte) 25, 32);
		method301(i_22_ - 1, i_21_, (byte) 25, 2);
	    }
	}
	if (i_23_ == 1 || i_23_ == 3) {
	    if (i_20_ == 0) {
		method301(i_22_, i_21_, (byte) 25, 1);
		method301(i_22_ + 1, i_21_ - 1, (byte) 25, 16);
	    }
	    if (i_20_ == 1) {
		method301(i_22_, i_21_, (byte) 25, 4);
		method301(i_22_ + 1, i_21_ + 1, (byte) 25, 64);
	    }
	    if (i_20_ == 2) {
		method301(i_22_, i_21_, (byte) 25, 16);
		method301(i_22_ - 1, i_21_ + 1, (byte) 25, 1);
	    }
	    if (i_20_ == 3) {
		method301(i_22_, i_21_, (byte) 25, 64);
		method301(i_22_ - 1, i_21_ - 1, (byte) 25, 4);
	    }
	}
	if (i_23_ == 2) {
	    if (i_20_ == 0) {
		method301(i_22_, i_21_, (byte) 25, 130);
		method301(i_22_, i_21_ - 1, (byte) 25, 8);
		method301(i_22_ + 1, i_21_, (byte) 25, 32);
	    }
	    if (i_20_ == 1) {
		method301(i_22_, i_21_, (byte) 25, 10);
		method301(i_22_ + 1, i_21_, (byte) 25, 32);
		method301(i_22_, i_21_ + 1, (byte) 25, 128);
	    }
	    if (i_20_ == 2) {
		method301(i_22_, i_21_, (byte) 25, 40);
		method301(i_22_, i_21_ + 1, (byte) 25, 128);
		method301(i_22_ - 1, i_21_, (byte) 25, 2);
	    }
	    if (i_20_ == 3) {
		method301(i_22_, i_21_, (byte) 25, 160);
		method301(i_22_ - 1, i_21_, (byte) 25, 2);
		method301(i_22_, i_21_ - 1, (byte) 25, 8);
	    }
	}
	if (bool) {
	    if (i_23_ == 0) {
		if (i_20_ == 0) {
		    method301(i_22_, i_21_, (byte) 25, 65536);
		    method301(i_22_, i_21_ - 1, (byte) 25, 4096);
		}
		if (i_20_ == 1) {
		    method301(i_22_, i_21_, (byte) 25, 1024);
		    method301(i_22_ + 1, i_21_, (byte) 25, 16384);
		}
		if (i_20_ == 2) {
		    method301(i_22_, i_21_, (byte) 25, 4096);
		    method301(i_22_, i_21_ + 1, (byte) 25, 65536);
		}
		if (i_20_ == 3) {
		    method301(i_22_, i_21_, (byte) 25, 16384);
		    method301(i_22_ - 1, i_21_, (byte) 25, 1024);
		}
	    }
	    if (i_23_ == 1 || i_23_ == 3) {
		if (i_20_ == 0) {
		    method301(i_22_, i_21_, (byte) 25, 512);
		    method301(i_22_ + 1, i_21_ - 1, (byte) 25, 8192);
		}
		if (i_20_ == 1) {
		    method301(i_22_, i_21_, (byte) 25, 2048);
		    method301(i_22_ + 1, i_21_ + 1, (byte) 25, 32768);
		}
		if (i_20_ == 2) {
		    method301(i_22_, i_21_, (byte) 25, 8192);
		    method301(i_22_ - 1, i_21_ + 1, (byte) 25, 512);
		}
		if (i_20_ == 3) {
		    method301(i_22_, i_21_, (byte) 25, 32768);
		    method301(i_22_ - 1, i_21_ - 1, (byte) 25, 2048);
		}
	    }
	    if (i_23_ == 2) {
		if (i_20_ == 0) {
		    method301(i_22_, i_21_, (byte) 25, 66560);
		    method301(i_22_, i_21_ - 1, (byte) 25, 4096);
		    method301(i_22_ + 1, i_21_, (byte) 25, 16384);
		}
		if (i_20_ == 1) {
		    method301(i_22_, i_21_, (byte) 25, 5120);
		    method301(i_22_ + 1, i_21_, (byte) 25, 16384);
		    method301(i_22_, i_21_ + 1, (byte) 25, 65536);
		}
		if (i_20_ == 2) {
		    method301(i_22_, i_21_, (byte) 25, 20480);
		    method301(i_22_, i_21_ + 1, (byte) 25, 65536);
		    method301(i_22_ - 1, i_21_, (byte) 25, 1024);
		}
		if (i_20_ == 3) {
		    method301(i_22_, i_21_, (byte) 25, 81920);
		    method301(i_22_ - 1, i_21_, (byte) 25, 1024);
		    method301(i_22_, i_21_ - 1, (byte) 25, 4096);
		}
	    }
	}
    }
    
    public void method300(int i, int i_24_, byte i_25_) {
	i -= anInt521;
	i_24_ -= anInt513;
	adjancency[i][i_24_]
	    = Class34.method342(adjancency[i][i_24_], 14680063);
    }
    
    public void method301(int i, int i_26_, byte i_27_, int i_28_) {
	adjancency[i_26_][i]
	    = Class34.method342(adjancency[i_26_][i],
				-i_28_ + 16777215);
	if (i_27_ != 25)
	    method306((byte) -64, -53, -76);
    }
    
    public void method302(int i, int i_29_, boolean bool, int i_30_, int i_31_,
			  int i_32_) {
	if (i_29_ == 16384) {
	    i_30_ -= anInt513;
	    i -= anInt521;
	    if (i_32_ == 0) {
		if (i_31_ == 0) {
		    method308(i, 128, i_30_, (byte) -69);
		    method308(i - 1, 8, i_30_, (byte) -69);
		}
		if (i_31_ == 1) {
		    method308(i, 2, i_30_, (byte) -69);
		    method308(i, 32, i_30_ + 1, (byte) -69);
		}
		if (i_31_ == 2) {
		    method308(i, 8, i_30_, (byte) -69);
		    method308(i + 1, 128, i_30_, (byte) -69);
		}
		if (i_31_ == 3) {
		    method308(i, 32, i_30_, (byte) -69);
		    method308(i, 2, i_30_ - 1, (byte) -69);
		}
	    }
	    if (i_32_ == 1 || i_32_ == 3) {
		if (i_31_ == 0) {
		    method308(i, 1, i_30_, (byte) -69);
		    method308(i - 1, 16, i_30_ + 1, (byte) -69);
		}
		if (i_31_ == 1) {
		    method308(i, 4, i_30_, (byte) -69);
		    method308(i + 1, 64, i_30_ + 1, (byte) -69);
		}
		if (i_31_ == 2) {
		    method308(i, 16, i_30_, (byte) -69);
		    method308(i + 1, 1, i_30_ - 1, (byte) -69);
		}
		if (i_31_ == 3) {
		    method308(i, 64, i_30_, (byte) -69);
		    method308(i - 1, 4, i_30_ - 1, (byte) -69);
		}
	    }
	    if (i_32_ == 2) {
		if (i_31_ == 0) {
		    method308(i, 130, i_30_, (byte) -69);
		    method308(i - 1, 8, i_30_, (byte) -69);
		    method308(i, 32, i_30_ + 1, (byte) -69);
		}
		if (i_31_ == 1) {
		    method308(i, 10, i_30_, (byte) -69);
		    method308(i, 32, i_30_ + 1, (byte) -69);
		    method308(i + 1, 128, i_30_, (byte) -69);
		}
		if (i_31_ == 2) {
		    method308(i, 40, i_30_, (byte) -69);
		    method308(i + 1, 128, i_30_, (byte) -69);
		    method308(i, 2, i_30_ - 1, (byte) -69);
		}
		if (i_31_ == 3) {
		    method308(i, 160, i_30_, (byte) -69);
		    method308(i, 2, i_30_ - 1, (byte) -69);
		    method308(i - 1, 8, i_30_, (byte) -69);
		}
	    }
	    if (bool) {
		if (i_32_ == 0) {
		    if (i_31_ == 0) {
			method308(i, 65536, i_30_, (byte) -69);
			method308(i - 1, 4096, i_30_, (byte) -69);
		    }
		    if (i_31_ == 1) {
			method308(i, 1024, i_30_, (byte) -69);
			method308(i, 16384, i_30_ + 1, (byte) -69);
		    }
		    if (i_31_ == 2) {
			method308(i, 4096, i_30_, (byte) -69);
			method308(i + 1, 65536, i_30_, (byte) -69);
		    }
		    if (i_31_ == 3) {
			method308(i, 16384, i_30_, (byte) -69);
			method308(i, 1024, i_30_ - 1, (byte) -69);
		    }
		}
		if (i_32_ == 1 || i_32_ == 3) {
		    if (i_31_ == 0) {
			method308(i, 512, i_30_, (byte) -69);
			method308(i - 1, 8192, i_30_ + 1, (byte) -69);
		    }
		    if (i_31_ == 1) {
			method308(i, 2048, i_30_, (byte) -69);
			method308(i + 1, 32768, i_30_ + 1, (byte) -69);
		    }
		    if (i_31_ == 2) {
			method308(i, 8192, i_30_, (byte) -69);
			method308(i + 1, 512, i_30_ - 1, (byte) -69);
		    }
		    if (i_31_ == 3) {
			method308(i, 32768, i_30_, (byte) -69);
			method308(i - 1, 2048, i_30_ - 1, (byte) -69);
		    }
		}
		if (i_32_ == 2) {
		    if (i_31_ == 0) {
			method308(i, 66560, i_30_, (byte) -69);
			method308(i - 1, 4096, i_30_, (byte) -69);
			method308(i, 16384, i_30_ + 1, (byte) -69);
		    }
		    if (i_31_ == 1) {
			method308(i, 5120, i_30_, (byte) -69);
			method308(i, 16384, i_30_ + 1, (byte) -69);
			method308(i + 1, 65536, i_30_, (byte) -69);
		    }
		    if (i_31_ == 2) {
			method308(i, 20480, i_30_, (byte) -69);
			method308(i + 1, 65536, i_30_, (byte) -69);
			method308(i, 1024, i_30_ - 1, (byte) -69);
		    }
		    if (i_31_ == 3) {
			method308(i, 81920, i_30_, (byte) -69);
			method308(i, 1024, i_30_ - 1, (byte) -69);
			method308(i - 1, 4096, i_30_, (byte) -69);
		    }
		}
	    }
	}
    }
    
    public static void method303(int i) {
	aClass3_519 = null;
	aByteArrayArray517 = null;
	aClass57_514 = null;
	aClass3_512 = null;
	aClass3_510 = null;
	aClass3_508 = null;
	fileLoader8 = null;
	aClass57_516 = null;
	anIntArray506 = null;
	aClass3_509 = null;
    }
    
    public void method304(boolean bool, int i, int i_33_, byte i_34_,
			  int i_35_, int i_36_, int i_37_) {
	i -= anInt521;
	i_35_ -= anInt513;
	if (i_37_ == 1 || i_37_ == 3) {
	    int i_38_ = i_33_;
	    i_33_ = i_36_;
	    i_36_ = i_38_;
	}
	int i_39_ = 256;
	if (bool)
	    i_39_ += 131072;
	for (int i_40_ = i; i_40_ < i_33_ + i; i_40_++) {
	    if (i_40_ >= 0 && anInt505 > i_40_) {
		for (int i_41_ = i_35_; i_41_ < i_36_ + i_35_; i_41_++) {
		    if (i_41_ >= 0 && i_41_ < anInt511)
			method308(i_40_, i_39_, i_41_, (byte) -69);
		}
	    }
	}
    }
    
    public void reset(byte i) {
	for (int i_42_ = 0; anInt505 > i_42_; i_42_++) {
	    for (int i_43_ = 0; anInt511 > i_43_; i_43_++) {
		if (i_42_ == 0 || i_43_ == 0 || i_42_ == anInt505 - 1
		    || i_43_ == anInt511 - 1)
		    adjancency[i_42_][i_43_] = 16777215;
		else
		    adjancency[i_42_][i_43_] = 16777216;
	    }
	}
    }
    
    public void method306(byte i, int i_44_, int i_45_) {
	i_44_ -= anInt513;
	i_45_ -= anInt521;
	adjancency[i_45_][i_44_]
	    = Queue.or(adjancency[i_45_][i_44_], 2097152);
    }
    
    public boolean method307(int i, int i_46_, byte i_47_, int i_48_,
			     int i_49_, int i_50_, int i_51_) {
	if (i_46_ == i_51_ && i == i_50_)
	    return true;
	i_46_ -= anInt521;
	i_51_ -= anInt521;
	i_50_ -= anInt513;
	i -= anInt513;
	if (i_48_ == 6 || i_48_ == 7) {
	    if (i_48_ == 7)
		i_49_ = i_49_ + 2 & 0x3;
	    if (i_49_ == 0) {
		if (i_46_ + 1 == i_51_ && i == i_50_
		    && (adjancency[i_51_][i_50_] & 0x80) == 0)
		    return true;
		if (i_51_ == i_46_ && i - 1 == i_50_
		    && (adjancency[i_51_][i_50_] & 0x2) == 0)
		    return true;
	    } else if (i_49_ == 1) {
		if (i_46_ - 1 == i_51_ && i == i_50_
		    && (adjancency[i_51_][i_50_] & 0x8) == 0)
		    return true;
		if (i_51_ == i_46_ && i - 1 == i_50_
		    && (adjancency[i_51_][i_50_] & 0x2) == 0)
		    return true;
	    } else if (i_49_ == 2) {
		if (i_51_ == i_46_ - 1 && i_50_ == i
		    && (adjancency[i_51_][i_50_] & 0x8) == 0)
		    return true;
		if (i_46_ == i_51_ && i + 1 == i_50_
		    && (adjancency[i_51_][i_50_] & 0x20) == 0)
		    return true;
	    } else if (i_49_ == 3) {
		if (i_51_ == i_46_ + 1 && i == i_50_
		    && (adjancency[i_51_][i_50_] & 0x80) == 0)
		    return true;
		if (i_46_ == i_51_ && i_50_ == i + 1
		    && (adjancency[i_51_][i_50_] & 0x20) == 0)
		    return true;
	    }
	}
	if (i_48_ == 8) {
	    if (i_46_ == i_51_ && i + 1 == i_50_
		&& (adjancency[i_51_][i_50_] & 0x20) == 0)
		return true;
	    if (i_46_ == i_51_ && i_50_ == i - 1
		&& (adjancency[i_51_][i_50_] & 0x2) == 0)
		return true;
	    if (i_46_ - 1 == i_51_ && i_50_ == i
		&& (adjancency[i_51_][i_50_] & 0x8) == 0)
		return true;
	    if (i_46_ + 1 == i_51_ && i == i_50_
		&& (adjancency[i_51_][i_50_] & 0x80) == 0)
		return true;
	}
	return false;
    }
    
    public void method308(int i, int i_52_, int i_53_, byte i_54_) {
	if (i_54_ == -69)
	    adjancency[i][i_53_]
		= Queue.or(adjancency[i][i_53_], i_52_);
    }
    
    public boolean method309(int i, int i_55_, int i_56_, int i_57_, int i_58_,
			     int i_59_, int i_60_) {
	if (i_56_ == i_59_ && i == i_55_)
	    return true;
	i_59_ -= anInt521;
	i_56_ -= anInt521;
	i -= anInt513;
	i_55_ -= anInt513;
	if (i_60_ == 0) {
	    if (i_57_ == 0) {
		if (i_56_ - 1 == i_59_ && i == i_55_)
		    return true;
		if (i_56_ == i_59_ && i + 1 == i_55_
		    && (adjancency[i_59_][i_55_] & 0x1280120) == 0)
		    return true;
		if (i_59_ == i_56_ && i_55_ == i - 1
		    && (adjancency[i_59_][i_55_] & 0x1280102) == 0)
		    return true;
	    } else if (i_57_ != 1) {
		if (i_57_ != 2) {
		    if (i_57_ == 3) {
			if (i_56_ == i_59_ && i - 1 == i_55_)
			    return true;
			if (i_56_ - 1 == i_59_ && i_55_ == i
			    && ((adjancency[i_59_][i_55_] & 0x1280108)
				== 0))
			    return true;
			if (i_56_ + 1 == i_59_ && i == i_55_
			    && ((adjancency[i_59_][i_55_] & 0x1280180)
				== 0))
			    return true;
		    }
		} else {
		    if (i_59_ == i_56_ + 1 && i == i_55_)
			return true;
		    if (i_59_ == i_56_ && i + 1 == i_55_
			&& (adjancency[i_59_][i_55_] & 0x1280120) == 0)
			return true;
		    if (i_59_ == i_56_ && i - 1 == i_55_
			&& (adjancency[i_59_][i_55_] & 0x1280102) == 0)
			return true;
		}
	    } else {
		if (i_59_ == i_56_ && i + 1 == i_55_)
		    return true;
		if (i_59_ == i_56_ - 1 && i == i_55_
		    && (adjancency[i_59_][i_55_] & 0x1280108) == 0)
		    return true;
		if (i_56_ + 1 == i_59_ && i_55_ == i
		    && (adjancency[i_59_][i_55_] & 0x1280180) == 0)
		    return true;
	    }
	}
	if (i_60_ == 2) {
	    if (i_57_ == 0) {
		if (i_59_ == i_56_ - 1 && i_55_ == i)
		    return true;
		if (i_56_ == i_59_ && i_55_ == i + 1)
		    return true;
		if (i_59_ == i_56_ + 1 && i == i_55_
		    && (adjancency[i_59_][i_55_] & 0x1280180) == 0)
		    return true;
		if (i_59_ == i_56_ && i - 1 == i_55_
		    && (adjancency[i_59_][i_55_] & 0x1280102) == 0)
		    return true;
	    } else if (i_57_ != 1) {
		if (i_57_ != 2) {
		    if (i_57_ == 3) {
			if (i_59_ == i_56_ - 1 && i == i_55_)
			    return true;
			if (i_59_ == i_56_ && i + 1 == i_55_
			    && ((adjancency[i_59_][i_55_] & 0x1280120)
				== 0))
			    return true;
			if (i_59_ == i_56_ + 1 && i == i_55_
			    && ((adjancency[i_59_][i_55_] & 0x1280180)
				== 0))
			    return true;
			if (i_56_ == i_59_ && i - 1 == i_55_)
			    return true;
		    }
		} else {
		    if (i_59_ == i_56_ - 1 && i == i_55_
			&& (adjancency[i_59_][i_55_] & 0x1280108) == 0)
			return true;
		    if (i_59_ == i_56_ && i + 1 == i_55_
			&& (adjancency[i_59_][i_55_] & 0x1280120) == 0)
			return true;
		    if (i_59_ == i_56_ + 1 && i == i_55_)
			return true;
		    if (i_59_ == i_56_ && i_55_ == i - 1)
			return true;
		}
	    } else {
		if (i_59_ == i_56_ - 1 && i_55_ == i
		    && (adjancency[i_59_][i_55_] & 0x1280108) == 0)
		    return true;
		if (i_59_ == i_56_ && i_55_ == i + 1)
		    return true;
		if (i_59_ == i_56_ + 1 && i == i_55_)
		    return true;
		if (i_56_ == i_59_ && i_55_ == i - 1
		    && (adjancency[i_59_][i_55_] & 0x1280102) == 0)
		    return true;
	    }
	}
	if (i_60_ == 9) {
	    if (i_56_ == i_59_ && i_55_ == i + 1
		&& (adjancency[i_59_][i_55_] & 0x20) == 0)
		return true;
	    if (i_56_ == i_59_ && i_55_ == i - 1
		&& (adjancency[i_59_][i_55_] & 0x2) == 0)
		return true;
	    if (i_59_ == i_56_ - 1 && i == i_55_
		&& (adjancency[i_59_][i_55_] & 0x8) == 0)
		return true;
	    if (i_59_ == i_56_ + 1 && i == i_55_
		&& (adjancency[i_59_][i_55_] & 0x80) == 0)
		return true;
	}
	return false;
    }
    
    public static boolean method310(FileTable class9, int i, int i_61_) {
	byte[] is = class9.fetchArchive(i);
	if (is == null)
	    return false;
	Class39_Sub7.decodeBitmapFont(is);
	return true;
    }
    
    public TraversalMap(int i, int i_62_) {
	anInt505 = i;
	anInt511 = i_62_;
	anInt521 = 0;
	anInt513 = 0;
	adjancency = new int[anInt505][anInt511];
	reset((byte) -108);
    }
    
    static {
	aBoolean504 = false;
	anInt518 = 99;
	anIntArray506 = new int[128];
	aClass3_509 = Class39_Sub5_Sub9.createJstring("Hidden)2use");
	aClass3_519 = aClass3_508;
	aClass3_510 = Class39_Sub5_Sub9.createJstring(" )2> @cya@");
	aClass3_512 = Class39_Sub5_Sub9.createJstring("Ignorieren");
	anInt515 = 0;
    }
}
