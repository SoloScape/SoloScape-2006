package jagex.audio;


import jagex.audio.Envelope;
import jagex.io.Buffer;

/* Class58 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Filter
{
    public int[][][] anIntArrayArrayArray1005;
    public static float aFloat1006;
    public static float[][] aFloatArrayArray1007 = new float[2][8];
    public int[] anIntArray1008;
    public int[] anIntArray1009 = new int[2];
    public int[][][] anIntArrayArrayArray1010;
    public static int anInt1011;
    public static int[][] anIntArrayArray1012 = new int[2][8];
    
    public float method1021(int i, int i_0_, float f) {
	float f_1_ = ((float) anIntArrayArrayArray1005[i][0][i_0_]
		      + f * (float) (anIntArrayArrayArray1005[i][1][i_0_]
				     - anIntArrayArrayArray1005[i][0][i_0_]));
	f_1_ *= 0.0015258789F;
	return 1.0F - (float) Math.pow(10.0, (double) (-f_1_ / 20.0F));
    }
    
    public static float method1022(float f) {
	float f_2_ = (float) Math.pow(2.0, (double) f) * 32.703197F;
	return f_2_ * 3.1415927F / 11025.0F;
    }
    
    public void method1023(Buffer class39_sub6, Envelope class28) {
	int i = class39_sub6.getUbyte();
	anIntArray1009[0] = i >> 4;
	anIntArray1009[1] = i & 0xf;
	if (i != 0) {
	    anIntArray1008[0] = class39_sub6.getUword();
	    anIntArray1008[1] = class39_sub6.getUword();
	    int i_3_ = class39_sub6.getUbyte();
	    for (int i_4_ = 0; i_4_ < 2; i_4_++) {
		for (int i_5_ = 0; i_5_ < anIntArray1009[i_4_]; i_5_++) {
		    anIntArrayArrayArray1010[i_4_][0][i_5_]
			= class39_sub6.getUword();
		    anIntArrayArrayArray1005[i_4_][0][i_5_]
			= class39_sub6.getUword();
		}
	    }
	    for (int i_6_ = 0; i_6_ < 2; i_6_++) {
		for (int i_7_ = 0; i_7_ < anIntArray1009[i_6_]; i_7_++) {
		    if ((i_3_ & 1 << i_6_ * 4 << i_7_) != 0) {
			anIntArrayArrayArray1010[i_6_][1][i_7_]
			    = class39_sub6.getUword();
			anIntArrayArrayArray1005[i_6_][1][i_7_]
			    = class39_sub6.getUword();
		    } else {
			anIntArrayArrayArray1010[i_6_][1][i_7_]
			    = anIntArrayArrayArray1010[i_6_][0][i_7_];
			anIntArrayArrayArray1005[i_6_][1][i_7_]
			    = anIntArrayArrayArray1005[i_6_][0][i_7_];
		    }
		}
	    }
	    if (i_3_ != 0 || anIntArray1008[1] != anIntArray1008[0])
		class28.method313(class39_sub6);
	} else
	    anIntArray1008[0] = anIntArray1008[1] = 0;
    }
    
    public static void method1024() {
	aFloatArrayArray1007 = null;
	anIntArrayArray1012 = null;
    }
    
    public float method1025(int i, int i_8_, float f) {
	float f_9_ = ((float) anIntArrayArrayArray1010[i][0][i_8_]
		      + f * (float) (anIntArrayArrayArray1010[i][1][i_8_]
				     - anIntArrayArrayArray1010[i][0][i_8_]));
	f_9_ *= 1.2207031E-4F;
	return method1022(f_9_);
    }
    
    public Filter() {
	anIntArray1008 = new int[2];
	anIntArrayArrayArray1005 = new int[2][2][4];
	anIntArrayArrayArray1010 = new int[2][2][4];
    }
    
    public int method1026(int i, float f) {
	if (i == 0) {
	    float f_10_
		= ((float) anIntArray1008[0]
		   + (float) (anIntArray1008[1] - anIntArray1008[0]) * f);
	    f_10_ *= 0.0030517578F;
	    aFloat1006 = (float) Math.pow(0.1, (double) (f_10_ / 20.0F));
	    anInt1011 = (int) (aFloat1006 * 65536.0F);
	}
	if (anIntArray1009[i] == 0)
	    return 0;
	float f_11_ = method1021(i, 0, f);
	aFloatArrayArray1007[i][0]
	    = f_11_ * -2.0F * (float) Math.cos((double) method1025(i, 0, f));
	aFloatArrayArray1007[i][1] = f_11_ * f_11_;
	for (int i_12_ = 1; i_12_ < anIntArray1009[i]; i_12_++) {
	    f_11_ = method1021(i, i_12_, f);
	    float f_13_
		= (f_11_ * -2.0F
		   * (float) Math.cos((double) method1025(i, i_12_, f)));
	    float f_14_ = f_11_ * f_11_;
	    aFloatArrayArray1007[i][i_12_ * 2 + 1]
		= aFloatArrayArray1007[i][i_12_ * 2 - 1] * f_14_;
	    aFloatArrayArray1007[i][i_12_ * 2]
		= (aFloatArrayArray1007[i][i_12_ * 2 - 1] * f_13_
		   + aFloatArrayArray1007[i][i_12_ * 2 - 2] * f_14_);
	    for (int i_15_ = i_12_ * 2 - 1; i_15_ >= 2; i_15_--)
		aFloatArrayArray1007[i][i_15_]
		    += (aFloatArrayArray1007[i][i_15_ - 1] * f_13_
			+ aFloatArrayArray1007[i][i_15_ - 2] * f_14_);
	    aFloatArrayArray1007[i][1]
		+= aFloatArrayArray1007[i][0] * f_13_ + f_14_;
	    aFloatArrayArray1007[i][0] += f_13_;
	}
	if (i == 0) {
	    for (int i_16_ = 0; i_16_ < anIntArray1009[0] * 2; i_16_++)
		aFloatArrayArray1007[0][i_16_] *= aFloat1006;
	}
	for (int i_17_ = 0; i_17_ < anIntArray1009[i] * 2; i_17_++)
	    anIntArrayArray1012[i][i_17_]
		= (int) (aFloatArrayArray1007[i][i_17_] * 65536.0F);
	return anIntArray1009[i] * 2;
    }
}
