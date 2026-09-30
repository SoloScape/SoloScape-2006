package unpackaged;

import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.Node;
import jagex.io.Buffer;

/* Class39_Sub8 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub8 extends Node
{
    public int[] anIntArray1389;
    public int[] anIntArray1390;
    public int anInt1391;
    public static int[] anIntArray1392;
    public boolean aBoolean1393;
    public int anInt1394;
    public int[] anIntArray1395;
    public int anInt1396;
    public int[] anIntArray1397;
    public boolean aBoolean1398 = false;
    public int[] anIntArray1399;
    
    public boolean method853(double d, int i, FileTable class9) {
	for (int i_0_ = 0; i_0_ < anIntArray1390.length; i_0_++) {
	    if (class9.method166(anIntArray1390[i_0_], true) == null)
		return false;
	}
	int i_1_ = i * i;
	anIntArray1395 = new int[i_1_ * 4];
	for (int i_2_ = 0; i_2_ < anIntArray1390.length; i_2_++) {
	    IndexedColorSprite class39_sub5_sub10_sub4
		= ArchiveRequest.method860(class9, -84, anIntArray1390[i_2_]);
	    class39_sub5_sub10_sub4.method697();
	    byte[] is = class39_sub5_sub10_sub4.index;
	    int[] is_3_ = class39_sub5_sub10_sub4.colors;
	    int i_4_ = anIntArray1389[i_2_];
	    if ((i_4_ & ~0xffffff) == 50331648) {
		int i_5_ = i_4_ & 0xff00ff;
		int i_6_ = i_4_ >> 8 & 0xff;
		for (int i_7_ = 0; i_7_ < is_3_.length; i_7_++) {
		    int i_8_ = is_3_[i_7_];
		    if ((i_8_ & 0xffff) == i_8_ >> 8) {
			i_8_ &= 0xff;
			is_3_[i_7_] = (i_5_ * i_8_ >> 8 & 0xff00ff
				       | i_6_ * i_8_ & 0xff00);
		    }
		}
	    }
	    for (int i_9_ = 0; i_9_ < is_3_.length; i_9_++)
		is_3_[i_9_]
		    = Class39_Sub5_Sub10_Sub2.method663(is_3_[i_9_], d);
	    int i_10_;
	    if (i_2_ == 0)
		i_10_ = 0;
	    else
		i_10_ = anIntArray1399[i_2_ - 1];
	    if (i_10_ == 0) {
		if (class39_sub5_sub10_sub4.anInt2480 == i) {
		    for (int i_11_ = 0; i_11_ < i_1_; i_11_++)
			anIntArray1395[i_11_] = is_3_[is[i_11_] & 0xff];
		} else if (class39_sub5_sub10_sub4.anInt2480 == 64
			   && i == 128) {
		    int i_12_ = 0;
		    for (int i_13_ = 0; i_13_ < i; i_13_++) {
			for (int i_14_ = 0; i_14_ < i; i_14_++)
			    anIntArray1395[i_12_++]
				= is_3_[(is[(i_14_ >> 1) + (i_13_ >> 1 << 6)]
					 & 0xff)];
		    }
		} else if (class39_sub5_sub10_sub4.anInt2480 == 128
			   && i == 64) {
		    int i_15_ = 0;
		    for (int i_16_ = 0; i_16_ < i; i_16_++) {
			for (int i_17_ = 0; i_17_ < i; i_17_++)
			    anIntArray1395[i_15_++]
				= is_3_[(is[(i_17_ << 1) + (i_16_ << 1 << 7)]
					 & 0xff)];
		    }
		} else
		    throw new RuntimeException();
	    }
	}
	for (int i_18_ = 0; i_18_ < i_1_; i_18_++) {
	    anIntArray1395[i_18_] &= 0xf8f8ff;
	    int i_19_ = anIntArray1395[i_18_];
	    anIntArray1395[i_18_ + i_1_] = i_19_ - (i_19_ >>> 3) & 0xf8f8ff;
	    anIntArray1395[i_18_ + i_1_ + i_1_]
		= i_19_ - (i_19_ >>> 2) & 0xf8f8ff;
	    anIntArray1395[i_18_ + i_1_ + i_1_ + i_1_]
		= i_19_ - (i_19_ >>> 2) - (i_19_ >>> 3) & 0xf8f8ff;
	}
	return true;
    }
    
    public void method854() {
	anIntArray1395 = null;
    }
    
    public void method855(int i) {
	if (anIntArray1395 != null) {
	    if (anInt1394 == 1 || anInt1394 == 3) {
		if (anIntArray1392 == null
		    || anIntArray1392.length < anIntArray1395.length)
		    anIntArray1392 = new int[anIntArray1395.length];
		int i_20_;
		if (anIntArray1395.length == 16384)
		    i_20_ = 64;
		else
		    i_20_ = 128;
		int i_21_ = anIntArray1395.length / 4;
		int i_22_ = i_20_ * i * anInt1391;
		int i_23_ = i_21_ - 1;
		if (anInt1394 == 1)
		    i_22_ = -i_22_;
		for (int i_24_ = 0; i_24_ < i_21_; i_24_++) {
		    int i_25_ = i_24_ + i_22_ & i_23_;
		    anIntArray1392[i_24_] = anIntArray1395[i_25_];
		    anIntArray1392[i_24_ + i_21_]
			= anIntArray1395[i_25_ + i_21_];
		    anIntArray1392[i_24_ + i_21_ + i_21_]
			= anIntArray1395[i_25_ + i_21_ + i_21_];
		    anIntArray1392[i_24_ + i_21_ + i_21_ + i_21_]
			= anIntArray1395[i_25_ + i_21_ + i_21_ + i_21_];
		}
		int[] is = anIntArray1395;
		anIntArray1395 = anIntArray1392;
		anIntArray1392 = is;
	    }
	    if (anInt1394 == 2 || anInt1394 == 4) {
		if (anIntArray1392 == null
		    || anIntArray1392.length < anIntArray1395.length)
		    anIntArray1392 = new int[anIntArray1395.length];
		int i_26_;
		if (anIntArray1395.length == 16384)
		    i_26_ = 64;
		else
		    i_26_ = 128;
		int i_27_ = anIntArray1395.length / 4;
		int i_28_ = i * anInt1391;
		int i_29_ = i_26_ - 1;
		if (anInt1394 == 2)
		    i_28_ = -i_28_;
		for (int i_30_ = 0; i_30_ < i_27_; i_30_ += i_26_) {
		    for (int i_31_ = 0; i_31_ < i_26_; i_31_++) {
			int i_32_ = i_30_ + i_31_;
			int i_33_ = i_30_ + (i_31_ + i_28_ & i_29_);
			anIntArray1392[i_32_] = anIntArray1395[i_33_];
			anIntArray1392[i_32_ + i_27_]
			    = anIntArray1395[i_33_ + i_27_];
			anIntArray1392[i_32_ + i_27_ + i_27_]
			    = anIntArray1395[i_33_ + i_27_ + i_27_];
			anIntArray1392[i_32_ + i_27_ + i_27_ + i_27_]
			    = anIntArray1395[i_33_ + i_27_ + i_27_ + i_27_];
		    }
		}
		int[] is = anIntArray1395;
		anIntArray1395 = anIntArray1392;
		anIntArray1392 = is;
	    }
	}
    }
    
    public static void method856() {
	anIntArray1392 = null;
    }
    
    public Class39_Sub8(Buffer class39_sub6) {
	anInt1396 = class39_sub6.getUword();
	aBoolean1393 = class39_sub6.getUbyte() == 1;
	int i = class39_sub6.getUbyte();
	if (i < 1 || i > 4)
	    throw new RuntimeException();
	anIntArray1390 = new int[i];
	for (int i_34_ = 0; i_34_ < i; i_34_++)
	    anIntArray1390[i_34_] = class39_sub6.getUword();
	if (i > 1) {
	    anIntArray1399 = new int[i - 1];
	    for (int i_35_ = 0; i_35_ < i - 1; i_35_++)
		anIntArray1399[i_35_] = class39_sub6.getUbyte();
	}
	if (i > 1) {
	    anIntArray1397 = new int[i - 1];
	    for (int i_36_ = 0; i_36_ < i - 1; i_36_++)
		anIntArray1397[i_36_] = class39_sub6.getUbyte();
	}
	anIntArray1389 = new int[i];
	for (int i_37_ = 0; i_37_ < i; i_37_++)
	    anIntArray1389[i_37_] = class39_sub6.getDword();
	anInt1394 = class39_sub6.getUbyte();
	anInt1391 = class39_sub6.getUbyte();
	anIntArray1395 = null;
    }
}
