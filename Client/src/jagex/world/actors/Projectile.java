package jagex.world.actors;

import jagex.utils.Node;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.io.FrameBuffer;
import unpackaged.ArchiveRequest;
import unpackaged.Class14;
import unpackaged.Class26;
import unpackaged.Class31;
import unpackaged.Class32;
import unpackaged.Class34;
import unpackaged.Class39_Sub10;
import unpackaged.Class39_Sub11;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub18;
import unpackaged.Class39_Sub5_Sub4;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class39_Sub7;
import unpackaged.Class43;
import unpackaged.Class45;
import unpackaged.Class46_Sub1;
import unpackaged.Class53;
import unpackaged.Class62_Sub1;
import unpackaged.ClientScript;
import jagex.graphics.sprites.DirectColorSprite;
import unpackaged.FileLoader;
import jagex.utils.Huffmans;
import unpackaged.Model;
import unpackaged.NameTable;
import unpackaged.OndemandRequest;
import unpackaged.ScriptState;

/* Class39_Sub5_Sub4_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Projectile extends Class39_Sub5_Sub4
{
    public int anInt2170;
    public double aDouble2171;
    public double aDouble2172;
    public double aDouble2173;
    public int anInt2174;
    public int anInt2175 = 0;
    public int anInt2176;
    public static FileLoader fileLoader6;
    public int anInt2178;
    public int anInt2179;
    public static int anInt2180;
    public int anInt2181;
    public static JString aClass3_2182;
    public double aDouble2183;
    public static JString aClass3_2184;
    public static JString aClass3_2185
	= Class39_Sub5_Sub9
	      .createJstring("We suspect someone knows your password)3");
    public double aDouble2186;
    public int anInt2187;
    public static JString[] aClass3Array2188 = new JString[200];
    public int anInt2189 = 0;
    public static JString aClass3_2190;
    public static int anInt2191;
    public static int anInt2192;
    public int anInt2193;
    public static JString aClass3_2194;
    public int anInt2195;
    public static JString aClass3_2196;
    public static int anInt2197;
    public Class39_Sub5_Sub11 aClass39_Sub5_Sub11_2198;
    public double aDouble2199;
    public boolean aBoolean2200 = false;
    public int anInt2201;
    public int anInt2202;
    public static int anInt2203;
    public int anInt2204;
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array2205;
    public int anInt2206;
    public static JString aClass3_2207;
    public static volatile int anInt2208;
    public double aDouble2209;
    public double aDouble2210;
    
    public static Class34 method492(boolean bool) {
	try {
	    return (Class34) Class.forName("unpackaged.Class34_Sub1").newInstance();
	} catch (Throwable throwable) {
	    return null;
	}
    }
    
    public static void method493(int i) {
	aClass3_2185 = null;
	aClass3Array2188 = null;
	aClass3_2196 = null;
	aClass3_2190 = null;
	aClass39_Sub5_Sub10_Sub3Array2205 = null;
	aClass3_2207 = null;
	aClass3_2182 = null;
	aClass3_2194 = null;
	fileLoader6 = null;
	aClass3_2184 = null;
    }
    
    public static void method494
	(int i, DirectColorSprite class39_sub5_sub10_sub3, int i_0_,
	 int i_1_) {
	int i_2_ = i_1_ * i_1_ + i_0_ * i_0_;
	if (i_2_ > 4225 && i_2_ < 90000) {
	    int i_3_ = ArchiveRequest.anInt1401 + Class34.anInt605 & 0x7ff;
	    int i_4_ = Model.anIntArray2394[i_3_];
	    i_4_ = i_4_ * 256 / (Class39_Sub7.anInt1386 + 256);
	    int i_5_ = Model.anIntArray2418[i_3_];
	    i_5_ = i_5_ * 256 / (Class39_Sub7.anInt1386 + 256);
	    int i_6_ = -(i_0_ * i_4_) + i_1_ * i_5_ >> 16;
	    int i_7_ = i_1_ * i_4_ + i_5_ * i_0_ >> 16;
	    double d = Math.atan2((double) i_7_, (double) i_6_);
	    int i_8_ = (int) (Math.sin(d) * 63.0);
	    int i_9_ = (int) (Math.cos(d) * 57.0);
	    ClientScript.aClass39_Sub5_Sub10_Sub3_1712.method686
		(i_8_ + 94 + 4 - 10, -i_9_ - 20 + 83, 20, 20, 15, 15, d, 256);
	} else
	    Player
		.method528(i_1_, class39_sub5_sub10_sub3, i_0_, 10064);
    }
    
    public static void requestArchive(int indexId, int archiveId) {
	long l = (long) (archiveId + (indexId << 16));
	OndemandRequest request = ((OndemandRequest) Class31.requests.fetch(l));
	if (request != null)
	    Class32.regularRequestsQueue.offerLast(request);
    }
    
    public static void method496(int i) {
	int i_11_ = Class26.anInt478 * 128 + 64;
	int i_12_ = GroundItem.anInt2238 * 128 + 64;
	int i_13_
	    = (Class14.method212(i_11_, i + 7942, NameTable.height, i_12_)
	       - Class45.anInt869);
	if (Class39_Sub11.anInt1470 < i_12_) {
	    Class39_Sub11.anInt1470
		+= ((i_12_ - Class39_Sub11.anInt1470) * Class43.anInt807 / 1000
		    + Class46_Sub1.anInt1568);
	    if (Class39_Sub11.anInt1470 > i_12_)
		Class39_Sub11.anInt1470 = i_12_;
	}
	if (i_13_ > Class39_Sub10.anInt1437) {
	    Class39_Sub10.anInt1437
		+= Class46_Sub1.anInt1568 + ((i_13_ - Class39_Sub10.anInt1437)
					     * Class43.anInt807 / 1000);
	    if (Class39_Sub10.anInt1437 > i_13_)
		Class39_Sub10.anInt1437 = i_13_;
	}
	if (Class39_Sub11.anInt1470 > i_12_) {
	    Class39_Sub11.anInt1470
		-= (Class43.anInt807 * (-i_12_ + Class39_Sub11.anInt1470)
		    / 1000) + Class46_Sub1.anInt1568;
	    if (i_12_ > Class39_Sub11.anInt1470)
		Class39_Sub11.anInt1470 = i_12_;
	}
	if (Class39_Sub10.anInt1437 > i_13_) {
	    Class39_Sub10.anInt1437
		-= (Class43.anInt807 * (-i_13_ + Class39_Sub10.anInt1437)
		    / 1000) + Class46_Sub1.anInt1568;
	    if (i_13_ > Class39_Sub10.anInt1437)
		Class39_Sub10.anInt1437 = i_13_;
	}
	if (Node.anInt742 < i_11_) {
	    Node.anInt742
		+= (Class46_Sub1.anInt1568
		    + Class43.anInt807 * (i_11_ - Node.anInt742) / 1000);
	    if (Node.anInt742 > i_11_)
		Node.anInt742 = i_11_;
	}
	if (i_11_ < Node.anInt742) {
	    Node.anInt742
		-= (Class46_Sub1.anInt1568
		    + Class43.anInt807 * (-i_11_ + Node.anInt742) / 1000);
	    if (Node.anInt742 < i_11_)
		Node.anInt742 = i_11_;
	}
	i_12_ = Class53.anInt965 * 128 + 64;
	i_11_ = Class39_Sub5_Sub18.anInt2121 * 128 + 64;
	i_13_ = (Class14.method212(i_11_, 9990, NameTable.height, i_12_)
		 - ScriptState.anInt454);
	int i_14_ = i_11_ - Node.anInt742;
	int i_15_ = i_12_ - Class39_Sub11.anInt1470;
	int i_16_ = i_13_ - Class39_Sub10.anInt1437;
	int i_17_ = (int) Math.sqrt((double) (i_14_ * i_14_ + i_15_ * i_15_));
	int i_18_
	    = ((int) (Math.atan2((double) i_16_, (double) i_17_) * 325.949)
	       & 0x7ff);
	int i_19_
	    = ((int) (Math.atan2((double) i_15_, (double) i_14_) * -325.949)
	       & 0x7ff);
	if (i_18_ < 128)
	    i_18_ = 128;
	int i_20_ = i_19_ - Class39_Sub5_Sub4_Sub4.anInt2315;
	if (i_20_ > 1024)
	    i_20_ -= 2048;
	if (i_18_ > 383)
	    i_18_ = 383;
	if (i_20_ < -1024)
	    i_20_ += 2048;
	if (i_18_ > Class43.anInt799) {
	    Class43.anInt799
		+= (FrameBuffer.anInt2156
		    + (-Class43.anInt799 + i_18_) * Class32.anInt590 / 1000);
	    if (Class43.anInt799 > i_18_)
		Class43.anInt799 = i_18_;
	}
	if (Class43.anInt799 > i_18_) {
	    Class43.anInt799
		-= (FrameBuffer.anInt2156
		    + Class32.anInt590 * (Class43.anInt799 - i_18_) / 1000);
	    if (i_18_ > Class43.anInt799)
		Class43.anInt799 = i_18_;
	}
	if (i_20_ > 0) {
	    Class39_Sub5_Sub4_Sub4.anInt2315
		+= (FrameBuffer.anInt2156
		    + i_20_ * Class32.anInt590 / 1000);
	    Class39_Sub5_Sub4_Sub4.anInt2315 &= 0x7ff;
	}
	if (i_20_ < 0) {
	    Class39_Sub5_Sub4_Sub4.anInt2315
		-= (FrameBuffer.anInt2156
		    + -i_20_ * Class32.anInt590 / 1000);
	    Class39_Sub5_Sub4_Sub4.anInt2315 &= 0x7ff;
	}
	int i_21_ = i_19_ - Class39_Sub5_Sub4_Sub4.anInt2315;
	if (i_21_ > 1024)
	    i_21_ -= 2048;
	if (i_21_ < -1024)
	    i_21_ += 2048;
	if (i == 2048) {
	    if (i_21_ < 0 && i_20_ > 0 || i_21_ > 0 && i_20_ < 0)
		Class39_Sub5_Sub4_Sub4.anInt2315 = i_19_;
	}
    }
    
    public Model method489(boolean bool) {
	Class39_Sub5_Sub18 class39_sub5_sub18
	    = Huffmans.method881(0, anInt2170);
	Model class39_sub5_sub4_sub6
	    = class39_sub5_sub18.method778(180, anInt2175);
	if (class39_sub5_sub4_sub6 == null)
	    return null;
	if (bool != true)
	    aClass39_Sub5_Sub11_2198 = null;
	class39_sub5_sub4_sub6.method565(anInt2202);
	return class39_sub5_sub4_sub6;
    }
    
    public void method497(int i, int i_22_, int i_23_, int i_24_, int i_25_) {
	if (!aBoolean2200) {
	    double d = (double) (i_25_ - anInt2174);
	    double d_26_ = (double) (i_23_ - anInt2179);
	    double d_27_ = Math.sqrt(d * d + d_26_ * d_26_);
	    aDouble2183
		= d_26_ * (double) anInt2176 / d_27_ + (double) anInt2179;
	    aDouble2186 = (double) anInt2181;
	    aDouble2173 = (double) anInt2176 * d / d_27_ + (double) anInt2174;
	}
	double d = (double) (anInt2204 + 1 - i_24_);
	aDouble2172 = (-aDouble2183 + (double) i_23_) / d;
	aDouble2199 = ((double) i_25_ - aDouble2173) / d;
	aDouble2171
	    = Math.sqrt(aDouble2199 * aDouble2199 + aDouble2172 * aDouble2172);
	if (!aBoolean2200)
	    aDouble2209
		= -aDouble2171 * Math.tan((double) anInt2178 * 0.02454369);
	aDouble2210
	    = ((double) i - aDouble2186 - d * aDouble2209) * 2.0 / (d * d);
    }
    
    public void method498(int i, int i_28_) {
	aBoolean2200 = true;
	aDouble2183 += aDouble2172 * (double) i_28_;
	aDouble2186
	    += ((double) i_28_ * aDouble2209
		+ (double) i_28_ * ((double) i_28_ * (aDouble2210 * 0.5)));
	aDouble2173 += aDouble2199 * (double) i_28_;
	aDouble2209 += (double) i_28_ * aDouble2210;
	anInt2206 = (i + (int) (Math.atan2(aDouble2199, aDouble2172) * 325.949)
		     & 0x7ff);
	anInt2202
	    = (int) (Math.atan2(aDouble2209, aDouble2171) * 325.949) & 0x7ff;
	if (aClass39_Sub5_Sub11_2198 != null) {
	    anInt2189 += i_28_;
	    while (anInt2189
		   > aClass39_Sub5_Sub11_2198.anIntArray1831[anInt2175]) {
		anInt2189
		    -= aClass39_Sub5_Sub11_2198.anIntArray1831[anInt2175];
		anInt2175++;
		if (anInt2175
		    >= aClass39_Sub5_Sub11_2198.anIntArray1833.length) {
		    anInt2175 -= aClass39_Sub5_Sub11_2198.anInt1839;
		    if (anInt2175 < 0 || anInt2175 >= (aClass39_Sub5_Sub11_2198
						       .anIntArray1833).length)
			anInt2175 = 0;
		}
	    }
	}
    }
    
    public Projectile(int i, int i_29_, int i_30_, int i_31_,
				  int i_32_, int i_33_, int i_34_, int i_35_,
				  int i_36_, int i_37_, int i_38_) {
	anInt2187 = i_29_;
	anInt2179 = i_31_;
	anInt2181 = i_32_;
	anInt2193 = i_37_;
	anInt2170 = i;
	anInt2178 = i_35_;
	anInt2176 = i_36_;
	anInt2174 = i_30_;
	aBoolean2200 = false;
	anInt2204 = i_34_;
	anInt2195 = i_38_;
	anInt2201 = i_33_;
	int i_39_ = Huffmans.method881(0, anInt2170).anInt2126;
	if (i_39_ != -1)
	    aClass39_Sub5_Sub11_2198
		= Class62_Sub1.method1064(i_39_, (byte) 54);
	else
	    aClass39_Sub5_Sub11_2198 = null;
    }
    
    static {
	aClass3_2184
	    = Class39_Sub5_Sub9.createJstring("Login limit exceeded)3");
	anInt2197 = -1;
	anInt2192 = 0;
	anInt2180 = 0;
	aClass3_2194 = Class39_Sub5_Sub9.createJstring("Take");
	aClass3_2196 = aClass3_2185;
	aClass3_2182 = aClass3_2184;
	aClass3_2190 = aClass3_2194;
	aClass3_2207 = Class39_Sub5_Sub9.createJstring("@gr3@");
	anInt2208 = -1;
    }
}
