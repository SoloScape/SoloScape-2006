package unpackaged;

/* Class17 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class17
{
    public int[] anIntArray314;
    public boolean aBoolean315 = true;
    public int anInt316;
    public static int[][] anIntArrayArray317;
    public int[] anIntArray318;
    public int[] anIntArray319;
    public int anInt320;
    public static int[] anIntArray321 = new int[6];
    public static int[] anIntArray322;
    public int[] anIntArray323;
    public int anInt324;
    public int[] anIntArray325;
    public static int[] anIntArray326;
    public int[] anIntArray327;
    public int anInt328;
    public int[] anIntArray329;
    public static int[] anIntArray330;
    public static int[][] anIntArrayArray331;
    public int[] anIntArray332;
    public static int[] anIntArray333;
    public int[] anIntArray334;
    public int[] anIntArray335;
    
    public static void method223() {
	anIntArray326 = null;
	anIntArray333 = null;
	anIntArray321 = null;
	anIntArray330 = null;
	anIntArray322 = null;
	anIntArrayArray331 = null;
	anIntArrayArray317 = null;
    }
    
    public Class17(int i, int i_0_, int i_1_, int i_2_, int i_3_, int i_4_,
		   int i_5_, int i_6_, int i_7_, int i_8_, int i_9_, int i_10_,
		   int i_11_, int i_12_, int i_13_, int i_14_, int i_15_,
		   int i_16_, int i_17_) {
	if (i_4_ != i_5_ || i_4_ != i_6_ || i_4_ != i_7_)
	    aBoolean315 = false;
	anInt320 = i;
	anInt316 = i_0_;
	anInt328 = i_16_;
	anInt324 = i_17_;
	int i_18_ = 128;
	int i_19_ = i_18_ / 2;
	int i_20_ = i_18_ / 4;
	int i_21_ = i_18_ * 3 / 4;
	int[] is = anIntArrayArray331[i];
	int i_22_ = is.length;
	anIntArray334 = new int[i_22_];
	anIntArray325 = new int[i_22_];
	anIntArray335 = new int[i_22_];
	int[] is_23_ = new int[i_22_];
	int[] is_24_ = new int[i_22_];
	int i_25_ = i_2_ * i_18_;
	int i_26_ = i_3_ * i_18_;
	for (int i_27_ = 0; i_27_ < i_22_; i_27_++) {
	    int i_28_ = is[i_27_];
	    if ((i_28_ & 0x1) == 0 && i_28_ <= 8)
		i_28_ = (i_28_ - i_0_ - i_0_ - 1 & 0x7) + 1;
	    if (i_28_ > 8 && i_28_ <= 12)
		i_28_ = (i_28_ - 9 - i_0_ & 0x3) + 9;
	    if (i_28_ > 12 && i_28_ <= 16)
		i_28_ = (i_28_ - 13 - i_0_ & 0x3) + 13;
	    int i_29_;
	    int i_30_;
	    int i_31_;
	    int i_32_;
	    int i_33_;
	    if (i_28_ == 1) {
		i_29_ = i_25_;
		i_30_ = i_26_;
		i_31_ = i_4_;
		i_32_ = i_8_;
		i_33_ = i_12_;
	    } else if (i_28_ == 2) {
		i_29_ = i_25_ + i_19_;
		i_30_ = i_26_;
		i_31_ = i_4_ + i_5_ >> 1;
		i_32_ = i_8_ + i_9_ >> 1;
		i_33_ = i_12_ + i_13_ >> 1;
	    } else if (i_28_ == 3) {
		i_29_ = i_25_ + i_18_;
		i_30_ = i_26_;
		i_31_ = i_5_;
		i_32_ = i_9_;
		i_33_ = i_13_;
	    } else if (i_28_ == 4) {
		i_29_ = i_25_ + i_18_;
		i_30_ = i_26_ + i_19_;
		i_31_ = i_5_ + i_6_ >> 1;
		i_32_ = i_9_ + i_10_ >> 1;
		i_33_ = i_13_ + i_14_ >> 1;
	    } else if (i_28_ == 5) {
		i_29_ = i_25_ + i_18_;
		i_30_ = i_26_ + i_18_;
		i_31_ = i_6_;
		i_32_ = i_10_;
		i_33_ = i_14_;
	    } else if (i_28_ == 6) {
		i_29_ = i_25_ + i_19_;
		i_30_ = i_26_ + i_18_;
		i_31_ = i_6_ + i_7_ >> 1;
		i_32_ = i_10_ + i_11_ >> 1;
		i_33_ = i_14_ + i_15_ >> 1;
	    } else if (i_28_ == 7) {
		i_29_ = i_25_;
		i_30_ = i_26_ + i_18_;
		i_31_ = i_7_;
		i_32_ = i_11_;
		i_33_ = i_15_;
	    } else if (i_28_ == 8) {
		i_29_ = i_25_;
		i_30_ = i_26_ + i_19_;
		i_31_ = i_7_ + i_4_ >> 1;
		i_32_ = i_11_ + i_8_ >> 1;
		i_33_ = i_15_ + i_12_ >> 1;
	    } else if (i_28_ == 9) {
		i_29_ = i_25_ + i_19_;
		i_30_ = i_26_ + i_20_;
		i_31_ = i_4_ + i_5_ >> 1;
		i_32_ = i_8_ + i_9_ >> 1;
		i_33_ = i_12_ + i_13_ >> 1;
	    } else if (i_28_ == 10) {
		i_29_ = i_25_ + i_21_;
		i_30_ = i_26_ + i_19_;
		i_31_ = i_5_ + i_6_ >> 1;
		i_32_ = i_9_ + i_10_ >> 1;
		i_33_ = i_13_ + i_14_ >> 1;
	    } else if (i_28_ == 11) {
		i_29_ = i_25_ + i_19_;
		i_30_ = i_26_ + i_21_;
		i_31_ = i_6_ + i_7_ >> 1;
		i_32_ = i_10_ + i_11_ >> 1;
		i_33_ = i_14_ + i_15_ >> 1;
	    } else if (i_28_ == 12) {
		i_29_ = i_25_ + i_20_;
		i_30_ = i_26_ + i_19_;
		i_31_ = i_7_ + i_4_ >> 1;
		i_32_ = i_11_ + i_8_ >> 1;
		i_33_ = i_15_ + i_12_ >> 1;
	    } else if (i_28_ == 13) {
		i_29_ = i_25_ + i_20_;
		i_30_ = i_26_ + i_20_;
		i_31_ = i_4_;
		i_32_ = i_8_;
		i_33_ = i_12_;
	    } else if (i_28_ == 14) {
		i_29_ = i_25_ + i_21_;
		i_30_ = i_26_ + i_20_;
		i_31_ = i_5_;
		i_32_ = i_9_;
		i_33_ = i_13_;
	    } else if (i_28_ == 15) {
		i_29_ = i_25_ + i_21_;
		i_30_ = i_26_ + i_21_;
		i_31_ = i_6_;
		i_32_ = i_10_;
		i_33_ = i_14_;
	    } else {
		i_29_ = i_25_ + i_20_;
		i_30_ = i_26_ + i_21_;
		i_31_ = i_7_;
		i_32_ = i_11_;
		i_33_ = i_15_;
	    }
	    anIntArray334[i_27_] = i_29_;
	    anIntArray325[i_27_] = i_31_;
	    anIntArray335[i_27_] = i_30_;
	    is_23_[i_27_] = i_32_;
	    is_24_[i_27_] = i_33_;
	}
	int[] is_34_ = anIntArrayArray317[i];
	int i_35_ = is_34_.length / 4;
	anIntArray314 = new int[i_35_];
	anIntArray323 = new int[i_35_];
	anIntArray329 = new int[i_35_];
	anIntArray332 = new int[i_35_];
	anIntArray319 = new int[i_35_];
	anIntArray318 = new int[i_35_];
	if (i_1_ != -1)
	    anIntArray327 = new int[i_35_];
	int i_36_ = 0;
	for (int i_37_ = 0; i_37_ < i_35_; i_37_++) {
	    int i_38_ = is_34_[i_36_];
	    int i_39_ = is_34_[i_36_ + 1];
	    int i_40_ = is_34_[i_36_ + 2];
	    int i_41_ = is_34_[i_36_ + 3];
	    i_36_ += 4;
	    if (i_39_ < 4)
		i_39_ = i_39_ - i_0_ & 0x3;
	    if (i_40_ < 4)
		i_40_ = i_40_ - i_0_ & 0x3;
	    if (i_41_ < 4)
		i_41_ = i_41_ - i_0_ & 0x3;
	    anIntArray314[i_37_] = i_39_;
	    anIntArray323[i_37_] = i_40_;
	    anIntArray329[i_37_] = i_41_;
	    if (i_38_ == 0) {
		anIntArray332[i_37_] = is_23_[i_39_];
		anIntArray319[i_37_] = is_23_[i_40_];
		anIntArray318[i_37_] = is_23_[i_41_];
		if (anIntArray327 != null)
		    anIntArray327[i_37_] = -1;
	    } else {
		anIntArray332[i_37_] = is_24_[i_39_];
		anIntArray319[i_37_] = is_24_[i_40_];
		anIntArray318[i_37_] = is_24_[i_41_];
		if (anIntArray327 != null)
		    anIntArray327[i_37_] = i_1_;
	    }
	}
	int i_42_ = i_4_;
	int i_43_ = i_5_;
	if (i_5_ < i_42_)
	    i_42_ = i_5_;
	if (i_5_ > i_43_)
	    i_43_ = i_5_;
	if (i_6_ < i_42_)
	    i_42_ = i_6_;
	if (i_6_ > i_43_)
	    i_43_ = i_6_;
	if (i_7_ < i_42_)
	    i_42_ = i_7_;
	if (i_7_ > i_43_)
	    i_43_ = i_7_;
	i_42_ /= 14;
	i_43_ /= 14;
    }
    
    static {
	anIntArrayArray317
	    = new int[][] { { 0, 1, 2, 3, 0, 0, 1, 3 },
			    { 1, 1, 2, 3, 1, 0, 1, 3 },
			    { 0, 1, 2, 3, 1, 0, 1, 3 },
			    { 0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3 },
			    { 0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4 },
			    { 0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4 },
			    { 0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3 },
			    { 0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3 },
			    { 0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5 },
			    { 0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5 },
			    { 0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3,
			      1, 5, 4, 3, 1, 4, 2, 3 },
			    { 1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3,
			      0, 5, 4, 3, 0, 4, 2, 3 },
			    { 1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3,
			      0, 5, 2, 3, 0, 1, 2, 5 } };
	anIntArray330 = new int[6];
	anIntArrayArray331
	    = new int[][] { { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7 },
			    { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 },
			    { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 },
			    { 1, 3, 5, 7, 2, 6 }, { 1, 3, 5, 7, 2, 8 },
			    { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 11, 12 },
			    { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 13, 14 } };
	anIntArray322 = new int[6];
	anIntArray326 = new int[6];
	anIntArray333 = new int[6];
    }
}
