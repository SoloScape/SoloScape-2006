package unpackaged;

import jagex.utils.Queue;

/* Class62_Sub1_Sub2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class62_Sub1_Sub2 extends Class62_Sub1 implements Runnable
{
    public static Runnable_Impl1 aRunnable_Impl1_2163;
    public static boolean aBoolean2164;
    public static Class5 aClass5_2165 = new Class5();
    public static int anInt2166;
    public static int anInt2167;
    public static int[] anIntArray2168 = new int[256];
    public static boolean aBoolean2169;
    
    public static void method1069(int i, int i_0_, int i_1_, int i_2_,
				  byte i_3_) {
	if (anIntArray2168.length <= anInt2167) {
	    aRunnable_Impl1_2163.method4(anIntArray2168, anInt2167);
	    anInt2167 = 0;
	}
	if (i_3_ >= -72)
	    method1071(-56);
	anIntArray2168[anInt2167++] = -anInt2166 + i_0_;
	anInt2166 = i_0_;
	anIntArray2168[anInt2167++]
	    = Queue.or(Queue.or(i_1_ << 8, i), i_2_ << 16);
    }
    
    public void method1065(int i, int i_4_, int i_5_, long l) {
	method1069(i, (int) l, i_4_, i_5_, (byte) -85);
    }
    
    @Override
    public synchronized boolean isPlaying() {
        return aClass5_2165.method121();
    }

    public void destroy() {
	synchronized (this) {
	    aBoolean2164 = true;
	}
	for (;;) {
	    synchronized (this) {
		if (!aBoolean2164)
		    break;
	    }
	    Class45.sleep(20L);
	}
	aRunnable_Impl1_2163.method1((byte) -123);
	
    }
    
    public synchronized void method1054(int i) {
	if (aClass5_2165.method121()) {
	    long l = ((long) (anInt2166 + 200
			      - aRunnable_Impl1_2163.method2(30982))
		      * (long) (aClass5_2165.anInt90 * 1000));
	    for (;;) {
		int i_6_ = aClass5_2165.method110();
		int i_7_ = aClass5_2165.anIntArray91[i_6_];
		long l_8_ = aClass5_2165.method120(i_7_);
		if (l_8_ > l)
		    break;
		while (aClass5_2165.anIntArray91[i_6_] == i_7_) {
		    aClass5_2165.method116(i_6_);
		    method1070((byte) -94, i_6_, l_8_);
		    if (aClass5_2165.method109()) {
			aClass5_2165.method113(i_6_);
			if (aClass5_2165.method111()) {
			    if (aBoolean2169)
				aClass5_2165.method119(l_8_);
			    else {
				this.method1062
				    ((long) (int) (l_8_ / (long) ((aClass5_2165
								   .anInt90)
								  * 1000)),
				     i);
				aClass5_2165.method108();
				method1071(-7521);
				return;
			    }
			}
			break;
		    }
		    aClass5_2165.method115(i_6_);
		    aClass5_2165.method113(i_6_);
		}
	    }
	    if (i != 0)
		aBoolean2164 = false;
	    method1071(-7521);
	}
    }
    
    public Class62_Sub1_Sub2(Signlink class21, Runnable_Impl1 runnable_impl1) {
	aRunnable_Impl1_2163 = runnable_impl1;
	aRunnable_Impl1_2163.method3(4443);
	aRunnable_Impl1_2163.method5((byte) 120);
	this.method1062((long) anInt2166, 0);
	aRunnable_Impl1_2163.method4(anIntArray2168, anInt2167);
	anInt2167 = 0;
	class21.requestThread(this, 10);
    }
    
    public void run() {
	try {
	    for (;;) {
		synchronized (this) {
		    if (aBoolean2164) {
			aBoolean2164 = false;
			break;
		    }
		    method1054(0);
		}
		Class45.sleep(100L);
	    }
	} catch (Exception exception) {
	    Class39_Sub7.method849(exception, 64, null);
	}
    }
    
    public void method1070(byte i, int i_9_, long l) {
	int i_10_ = 57 % ((i + 18) / 53);
	int i_11_ = aClass5_2165.method107(i_9_);
	if (i_11_ == 1)
	    aClass5_2165.method118();
	else if ((i_11_ & 0x80) != 0) {
	    int i_12_ = i_11_ & 0xff;
	    int i_13_ = (int) (l / (long) (aClass5_2165.anInt90 * 1000));
	    int i_14_ = i_11_ >> 8 & 0xff;
	    int i_15_ = i_11_ >> 16 & 0xff;
	    if (!this.method1060(i_12_, i_14_, i_15_, (long) i_13_))
		method1069(i_12_, i_13_, i_14_, i_15_, (byte) -105);
	}
    }
    
    public synchronized void method1048(boolean bool) {
	aRunnable_Impl1_2163.method5((byte) 124);
	this.method1062((long) anInt2166, 0);
	aRunnable_Impl1_2163.method4(anIntArray2168, anInt2167);
	if (!bool) {
	    anInt2167 = 0;
	    aClass5_2165.method108();
	}
    }
    
    public synchronized void method1051(int i, byte[] is, byte i_16_,
					boolean bool) {
	aClass5_2165.method114(is);
	boolean bool_17_ = true;
	anInt2166 = 0;
	aBoolean2169 = bool;
	aRunnable_Impl1_2163.method5((byte) 124);
	this.method1068((byte) -9, 0, (long) anInt2166, i);
	int i_18_ = aClass5_2165.method105();
	if (i_16_ < 73)
	    anInt2167 = -45;
	for (int i_19_ = 0; i_18_ > i_19_; i_19_++) {
	    aClass5_2165.method116(i_19_);
	    while (!aClass5_2165.method109()) {
		aClass5_2165.method115(i_19_);
		if (aClass5_2165.anIntArray91[i_19_] != 0) {
		    bool_17_ = false;
		    break;
		}
		method1070((byte) -77, i_19_, 0L);
	    }
	    aClass5_2165.method113(i_19_);
	}
	if (bool_17_) {
	    if (aBoolean2169)
		throw new RuntimeException();
	    this.method1062((long) anInt2166, 0);
	    aClass5_2165.method108();
	}
	method1071(-7521);
    }
    
    public static void method1071(int i) {
	if (i == -7521) {
	    if (anInt2167 > 0) {
		aRunnable_Impl1_2163.method4(anIntArray2168, anInt2167);
		anInt2167 = 0;
	    }
	}
    }
    
    public synchronized void method1053(int i, byte i_20_) {
	this.method1061((long) anInt2166, true, i);
	aRunnable_Impl1_2163.method4(anIntArray2168, anInt2167);
	anInt2167 = 0;
	if (i_20_ < 44)
	    method1053(17, (byte) 126);
    }
    
    public synchronized void method1055(int i, int i_21_, int i_22_) {
	this.method1068((byte) -9, i, (long) anInt2166, i_21_);
	if (i_22_ != 0)
	    method1070((byte) -5, 102, -70L);
    }
}
