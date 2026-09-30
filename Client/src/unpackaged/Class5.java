package unpackaged;

import jagex.io.Buffer;

/* Class5 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class5
{
    public static byte[] aByteArray83
	= { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	    2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	    2, 2, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    public Buffer aClass39_Sub6_84 = new Buffer(null);
    public long aLong85;
    public int[] anIntArray86;
    public int anInt87;
    public int[] anIntArray88;
    public int[] anIntArray89;
    public int anInt90;
    public int[] anIntArray91;
    
    public int method105() {
	return anIntArray86.length;
    }
    
    public int method106(int i) {
	int i_0_ = aClass39_Sub6_84.payload[aClass39_Sub6_84.offset];
	if (i_0_ < 0) {
	    i_0_ &= 0xff;
	    anIntArray89[i] = i_0_;
	    aClass39_Sub6_84.offset++;
	} else
	    i_0_ = anIntArray89[i];
	if (i_0_ == 240 || i_0_ == 247) {
	    int i_1_ = aClass39_Sub6_84.method798(-110);
	    if (i_0_ == 247 && i_1_ > 0) {
		int i_2_ = ((aClass39_Sub6_84.payload
			     [aClass39_Sub6_84.offset])
			    & 0xff);
		if (i_2_ >= 241 && i_2_ <= 243 || i_2_ == 246 || i_2_ == 248
		    || i_2_ >= 250 && i_2_ <= 252 || i_2_ == 254) {
		    aClass39_Sub6_84.offset++;
		    anIntArray89[i] = i_2_;
		    return method117(i, i_2_);
		}
	    }
	    aClass39_Sub6_84.offset += i_1_;
	    return 0;
	}
	return method117(i, i_0_);
    }
    
    public int method107(int i) {
	int i_3_ = method106(i);
	return i_3_;
    }
    
    public void method108() {
	aClass39_Sub6_84.payload = null;
	anIntArray88 = null;
	anIntArray86 = null;
	anIntArray91 = null;
	anIntArray89 = null;
    }
    
    public boolean method109() {
	if (aClass39_Sub6_84.offset >= 0)
	    return false;
	return true;
    }
    
    public int method110() {
	int i = anIntArray86.length;
	int i_4_ = -1;
	int i_5_ = 2147483647;
	for (int i_6_ = 0; i_6_ < i; i_6_++) {
	    if (anIntArray86[i_6_] >= 0 && anIntArray91[i_6_] < i_5_) {
		i_4_ = i_6_;
		i_5_ = anIntArray91[i_6_];
	    }
	}
	return i_4_;
    }
    
    public boolean method111() {
	int i = anIntArray86.length;
	for (int i_7_ = 0; i_7_ < i; i_7_++) {
	    if (anIntArray86[i_7_] >= 0)
		return false;
	}
	return true;
    }
    
    public static void method112() {
	aByteArray83 = null;
    }
    
    public void method113(int i) {
	anIntArray86[i] = aClass39_Sub6_84.offset;
    }
    
    public void method114(byte[] is) {
	aClass39_Sub6_84.payload = is;
	aClass39_Sub6_84.offset = 10;
	int i = aClass39_Sub6_84.getUword();
	anInt90 = aClass39_Sub6_84.getUword();
	anInt87 = 500000;
	anIntArray88 = new int[i];
	int i_8_ = 0;
	while (i_8_ < i) {
	    int i_9_ = aClass39_Sub6_84.getDword();
	    int i_10_ = aClass39_Sub6_84.getDword();
	    if (i_9_ == 1297379947) {
		anIntArray88[i_8_] = aClass39_Sub6_84.offset;
		i_8_++;
	    }
	    aClass39_Sub6_84.offset += i_10_;
	}
	anIntArray86 = new int[anIntArray88.length];
	for (int i_11_ = 0; i_11_ < anIntArray86.length; i_11_++)
	    anIntArray86[i_11_] = anIntArray88[i_11_];
	anIntArray91 = new int[i];
	anIntArray89 = new int[i];
    }
    
    public void method115(int i) {
	int i_12_ = aClass39_Sub6_84.method798(-93);
	anIntArray91[i] += i_12_;
    }
    
    public void method116(int i) {
	aClass39_Sub6_84.offset = anIntArray86[i];
    }
    
    public int method117(int i, int i_13_) {
	if (i_13_ == 255) {
	    int i_14_ = aClass39_Sub6_84.getUbyte();
	    int i_15_ = aClass39_Sub6_84.method798(-97);
	    if (i_14_ == 47) {
		aClass39_Sub6_84.offset += i_15_;
		return 1;
	    }
	    if (i_14_ == 81) {
		int i_16_ = aClass39_Sub6_84.getUtri((byte) -62);
		i_15_ -= 3;
		int i_17_ = anIntArray91[i];
		aLong85 += (long) i_17_ * (long) (anInt87 - i_16_);
		anInt87 = i_16_;
		aClass39_Sub6_84.offset += i_15_;
		return 2;
	    }
	    aClass39_Sub6_84.offset += i_15_;
	    return 3;
	}
	byte i_18_ = aByteArray83[i_13_ - 128];
	int i_19_ = i_13_;
	if (i_18_ >= 1)
	    i_19_ |= aClass39_Sub6_84.getUbyte() << 8;
	if (i_18_ >= 2)
	    i_19_ |= aClass39_Sub6_84.getUbyte() << 16;
	return i_19_;
    }
    
    public void method118() {
	aClass39_Sub6_84.offset = -1;
    }
    
    public void method119(long l) {
	aLong85 = l;
	int i = anIntArray86.length;
	for (int i_20_ = 0; i_20_ < i; i_20_++) {
	    anIntArray91[i_20_] = 0;
	    anIntArray89[i_20_] = 0;
	    aClass39_Sub6_84.offset = anIntArray88[i_20_];
	    method115(i_20_);
	    anIntArray86[i_20_] = aClass39_Sub6_84.offset;
	}
    }
    
    public long method120(int i) {
	return aLong85 + (long) i * (long) anInt87;
    }
    
    public boolean method121() {
	if (aClass39_Sub6_84.payload == null)
	    return false;
	return true;
    }
}
