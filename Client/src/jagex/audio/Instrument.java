package jagex.audio;

/* Class59 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.util.Random;
import jagex.io.Buffer;

public class Instrument
{
    public Envelope aClass28_1013;
    public Envelope aClass28_1014;
    public int anInt1015;
    public Envelope aClass28_1016;
    public static int[] anIntArray1017 = new int[32768];
    public Envelope aClass28_1018;
    public int anInt1019 = 0;
    public int anInt1020;
    public int anInt1021 = 0;
    public Filter aClass58_1022;
    public static int[] anIntArray1023;
    public int[] anIntArray1024;
    public Envelope aClass28_1025;
    public Envelope aClass28_1026;
    public Envelope aClass28_1027;
    public int[] anIntArray1028 = new int[5];
    public static int[] anIntArray1029;
    public int[] anIntArray1030;
    public Envelope aClass28_1031;
    public Envelope aClass28_1032;
    public static int[] anIntArray1033;
    public static int[] anIntArray1034;
    public static int[] anIntArray1035;
    public static int[] anIntArray1036;
    public static int[] anIntArray1037;
    
    public void decode(Buffer buffer) {
	aClass28_1013 = new Envelope();
	aClass28_1013.method311(buffer);
	aClass28_1016 = new Envelope();
	aClass28_1016.method311(buffer);
	int i = buffer.getUbyte();
	if (i != 0) {
	    buffer.offset--;
	    aClass28_1026 = new Envelope();
	    aClass28_1026.method311(buffer);
	    aClass28_1014 = new Envelope();
	    aClass28_1014.method311(buffer);
	}
	i = buffer.getUbyte();
	if (i != 0) {
	    buffer.offset--;
	    aClass28_1025 = new Envelope();
	    aClass28_1025.method311(buffer);
	    aClass28_1027 = new Envelope();
	    aClass28_1027.method311(buffer);
	}
	i = buffer.getUbyte();
	if (i != 0) {
	    buffer.offset--;
	    aClass28_1031 = new Envelope();
	    aClass28_1031.method311(buffer);
	    aClass28_1018 = new Envelope();
	    aClass28_1018.method311(buffer);
	}
	for (int i_0_ = 0; i_0_ < 10; i_0_++) {
	    int i_1_ = buffer.getSmartB();
	    if (i_1_ == 0)
		break;
	    anIntArray1030[i_0_] = i_1_;
	    anIntArray1028[i_0_] = buffer.getSmartA();
	    anIntArray1024[i_0_] = buffer.getSmartB();
	}
	anInt1021 = buffer.getSmartB();
	anInt1020 = buffer.getSmartB();
	anInt1015 = buffer.getUword();
	anInt1019 = buffer.getUword();
	aClass58_1022 = new Filter();
	aClass28_1032 = new Envelope();
	aClass58_1022.method1023(buffer, aClass28_1032);
    }
    
    public static void method1028() {
	anIntArray1029 = null;
	anIntArray1017 = null;
	anIntArray1023 = null;
	anIntArray1035 = null;
	anIntArray1036 = null;
	anIntArray1033 = null;
	anIntArray1037 = null;
	anIntArray1034 = null;
    }
    
    public int method1029(int i, int i_2_, int i_3_) {
	if (i_3_ == 1) {
	    if ((i & 0x7fff) < 16384)
		return i_2_;
	    return -i_2_;
	}
	if (i_3_ == 2)
	    return anIntArray1023[i & 0x7fff] * i_2_ >> 14;
	if (i_3_ == 3)
	    return ((i & 0x7fff) * i_2_ >> 14) - i_2_;
	if (i_3_ == 4)
	    return anIntArray1017[i / 2607 & 0x7fff] * i_2_;
	return 0;
    }
    
    public int[] method1030(int i, int i_4_) {
	for (int i_5_ = 0; i_5_ < i; i_5_++)
	    anIntArray1029[i_5_] = 0;
	if (i_4_ < 10)
	    return anIntArray1029;
	double d = (double) i / ((double) i_4_ + 0.0);
	aClass28_1013.method312();
	aClass28_1016.method312();
	int i_6_ = 0;
	int i_7_ = 0;
	int i_8_ = 0;
	if (aClass28_1026 != null) {
	    aClass28_1026.method312();
	    aClass28_1014.method312();
	    i_6_ = (int) ((double) (aClass28_1026.anInt525
				    - aClass28_1026.anInt522)
			  * 32.768 / d);
	    i_7_ = (int) ((double) aClass28_1026.anInt522 * 32.768 / d);
	}
	int i_9_ = 0;
	int i_10_ = 0;
	int i_11_ = 0;
	if (aClass28_1025 != null) {
	    aClass28_1025.method312();
	    aClass28_1027.method312();
	    i_9_ = (int) ((double) (aClass28_1025.anInt525
				    - aClass28_1025.anInt522)
			  * 32.768 / d);
	    i_10_ = (int) ((double) aClass28_1025.anInt522 * 32.768 / d);
	}
	for (int i_12_ = 0; i_12_ < 5; i_12_++) {
	    if (anIntArray1030[i_12_] != 0) {
		anIntArray1035[i_12_] = 0;
		anIntArray1036[i_12_]
		    = (int) ((double) anIntArray1024[i_12_] * d);
		anIntArray1033[i_12_] = (anIntArray1030[i_12_] << 14) / 100;
		anIntArray1037[i_12_]
		    = (int) ((double) (aClass28_1013.anInt525
				       - aClass28_1013.anInt522)
			     * 32.768
			     * Math.pow(1.0057929410678534,
					(double) anIntArray1028[i_12_])
			     / d);
		anIntArray1034[i_12_]
		    = (int) ((double) aClass28_1013.anInt522 * 32.768 / d);
	    }
	}
	for (int i_13_ = 0; i_13_ < i; i_13_++) {
	    int i_14_ = aClass28_1013.method314(i);
	    int i_15_ = aClass28_1016.method314(i);
	    if (aClass28_1026 != null) {
		int i_16_ = aClass28_1026.method314(i);
		int i_17_ = aClass28_1014.method314(i);
		i_14_ += method1029(i_8_, i_17_, aClass28_1026.anInt524) >> 1;
		i_8_ += (i_16_ * i_6_ >> 16) + i_7_;
	    }
	    if (aClass28_1025 != null) {
		int i_18_ = aClass28_1025.method314(i);
		int i_19_ = aClass28_1027.method314(i);
		i_15_ = i_15_ * ((method1029(i_11_, i_19_,
					     aClass28_1025.anInt524)
				  >> 1)
				 + 32768) >> 15;
		i_11_ += (i_18_ * i_9_ >> 16) + i_10_;
	    }
	    for (int i_20_ = 0; i_20_ < 5; i_20_++) {
		if (anIntArray1030[i_20_] != 0) {
		    int i_21_ = i_13_ + anIntArray1036[i_20_];
		    if (i_21_ < i) {
			anIntArray1029[i_21_]
			    += method1029(anIntArray1035[i_20_],
					  i_15_ * anIntArray1033[i_20_] >> 15,
					  aClass28_1013.anInt524);
			anIntArray1035[i_20_]
			    += ((i_14_ * anIntArray1037[i_20_] >> 16)
				+ anIntArray1034[i_20_]);
		    }
		}
	    }
	}
	if (aClass28_1031 != null) {
	    aClass28_1031.method312();
	    aClass28_1018.method312();
	    int i_22_ = 0;
	    boolean bool = false;
	    boolean bool_23_ = true;
	    for (int i_24_ = 0; i_24_ < i; i_24_++) {
		int i_25_ = aClass28_1031.method314(i);
		int i_26_ = aClass28_1018.method314(i);
		int i_27_;
		if (bool_23_)
		    i_27_
			= aClass28_1031.anInt522 + (((aClass28_1031.anInt525
						      - aClass28_1031.anInt522)
						     * i_25_)
						    >> 8);
		else
		    i_27_
			= aClass28_1031.anInt522 + (((aClass28_1031.anInt525
						      - aClass28_1031.anInt522)
						     * i_26_)
						    >> 8);
		i_22_ += 256;
		if (i_22_ >= i_27_) {
		    i_22_ = 0;
		    bool_23_ = !bool_23_;
		}
		if (bool_23_)
		    anIntArray1029[i_24_] = 0;
	    }
	}
	if (anInt1021 > 0 && anInt1020 > 0) {
	    int i_28_ = (int) ((double) anInt1021 * d);
	    for (int i_29_ = i_28_; i_29_ < i; i_29_++)
		anIntArray1029[i_29_]
		    += anIntArray1029[i_29_ - i_28_] * anInt1020 / 100;
	}
	if (aClass58_1022.anIntArray1009[0] > 0
	    || aClass58_1022.anIntArray1009[1] > 0) {
	    aClass28_1032.method312();
	    int i_30_ = aClass28_1032.method314(i + 1);
	    int i_31_ = aClass58_1022.method1026(0, (float) i_30_ / 65536.0F);
	    int i_32_ = aClass58_1022.method1026(1, (float) i_30_ / 65536.0F);
	    if (i >= i_31_ + i_32_) {
		int i_33_ = 0;
		int i_34_ = i_32_;
		if (i_34_ > i - i_31_)
		    i_34_ = i - i_31_;
		for (/**/; i_33_ < i_34_; i_33_++) {
		    int i_35_ = (int) (((long) anIntArray1029[i_33_ + i_31_]
					* (long) Filter.anInt1011)
				       >> 16);
		    for (int i_36_ = 0; i_36_ < i_31_; i_36_++)
			i_35_ += (int) (((long) (anIntArray1029
						 [i_33_ + i_31_ - 1 - i_36_])
					 * (long) (Filter.anIntArrayArray1012
						   [0][i_36_]))
					>> 16);
		    for (int i_37_ = 0; i_37_ < i_33_; i_37_++)
			i_35_
			    -= (int) (((long) anIntArray1029[i_33_ - 1 - i_37_]
				       * (long) (Filter.anIntArrayArray1012[1]
						 [i_37_]))
				      >> 16);
		    anIntArray1029[i_33_] = i_35_;
		    i_30_ = aClass28_1032.method314(i + 1);
		}
		i_34_ = 128;
		for (;;) {
		    if (i_34_ > i - i_31_)
			i_34_ = i - i_31_;
		    for (/**/; i_33_ < i_34_; i_33_++) {
			int i_38_
			    = (int) (((long) anIntArray1029[i_33_ + i_31_]
				      * (long) Filter.anInt1011)
				     >> 16);
			for (int i_39_ = 0; i_39_ < i_31_; i_39_++)
			    i_38_
				+= (int) (((long) (anIntArray1029
						   [i_33_ + i_31_ - 1 - i_39_])
					   * (long) (Filter
						     .anIntArrayArray1012[0]
						     [i_39_]))
					  >> 16);
			for (int i_40_ = 0; i_40_ < i_32_; i_40_++)
			    i_38_ -= (int) (((long) (anIntArray1029
						     [i_33_ - 1 - i_40_])
					     * (long) (Filter
						       .anIntArrayArray1012[1]
						       [i_40_]))
					    >> 16);
			anIntArray1029[i_33_] = i_38_;
			i_30_ = aClass28_1032.method314(i + 1);
		    }
		    if (i_33_ >= i - i_31_)
			break;
		    i_31_ = aClass58_1022.method1026(0,
						     (float) i_30_ / 65536.0F);
		    i_32_ = aClass58_1022.method1026(1,
						     (float) i_30_ / 65536.0F);
		    i_34_ += 128;
		}
		for (/**/; i_33_ < i; i_33_++) {
		    int i_41_ = 0;
		    for (int i_42_ = i_33_ + i_31_ - i; i_42_ < i_31_; i_42_++)
			i_41_ += (int) (((long) (anIntArray1029
						 [i_33_ + i_31_ - 1 - i_42_])
					 * (long) (Filter.anIntArrayArray1012
						   [0][i_42_]))
					>> 16);
		    for (int i_43_ = 0; i_43_ < i_32_; i_43_++)
			i_41_
			    -= (int) (((long) anIntArray1029[i_33_ - 1 - i_43_]
				       * (long) (Filter.anIntArrayArray1012[1]
						 [i_43_]))
				      >> 16);
		    anIntArray1029[i_33_] = i_41_;
		    i_30_ = aClass28_1032.method314(i + 1);
		}
	    }
	}
	for (int i_44_ = 0; i_44_ < i; i_44_++) {
	    if (anIntArray1029[i_44_] < -32768)
		anIntArray1029[i_44_] = -32768;
	    if (anIntArray1029[i_44_] > 32767)
		anIntArray1029[i_44_] = 32767;
	}
	return anIntArray1029;
    }
    
    public Instrument() {
	anInt1020 = 100;
	anInt1015 = 500;
	anIntArray1024 = new int[5];
	anIntArray1030 = new int[5];
    }
    
    static {
	Random random = new Random(0L);
	for (int i = 0; i < 32768; i++)
	    anIntArray1017[i] = (random.nextInt() & 0x2) - 1;
	anIntArray1023 = new int[32768];
	for (int i = 0; i < 32768; i++)
	    anIntArray1023[i]
		= (int) (Math.sin((double) i / 5215.1903) * 16384.0);
	anIntArray1029 = new int[220500];
	anIntArray1033 = new int[5];
	anIntArray1035 = new int[5];
	anIntArray1034 = new int[5];
	anIntArray1036 = new int[5];
	anIntArray1037 = new int[5];
    }
}
