package unpackaged;

/* Class13_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.world.actors.StillGraphic;
import java.awt.Component;

public abstract class Class13_Sub1 extends Class13 implements Runnable
{
    public int anInt1304;
    public long aLong1305;
    public int anInt1306 = 0;
    public int anInt1307 = 0;
    public int anInt1308;
    public int anInt1309;
    public static int[] anIntArray1310;
    public long aLong1311;
    public long aLong1312;
    public int anInt1313 = 0;
    public boolean aBoolean1314;
    public int[] anIntArray1315;
    public int anInt1316;
    public int anInt1317;
    
    public void method195(int i) {
	int i_0_ = i - anInt1317;
	int i_1_ = anIntArray1315[anInt1307];
	anIntArray1315[anInt1307] = i_0_;
	anInt1313 += i_0_ - i_1_;
	int i_2_ = anInt1307 + 1 & 0x1ff;
	if (i_0_ > anInt1306)
	    anInt1306 = i_0_;
	if (i_0_ < anInt1304)
	    anInt1304 = i_0_;
	if (i_1_ == anInt1306) {
	    int i_3_ = i_0_;
	    for (int i_4_ = i_2_; i_4_ != anInt1307 && i_3_ < anInt1306;
		 i_4_ = i_4_ + 1 & 0x1ff) {
		int i_5_ = anIntArray1315[i_4_];
		if (i_5_ > i_3_)
		    i_3_ = i_5_;
	    }
	    anInt1306 = i_3_;
	}
	if (i_1_ == anInt1304) {
	    int i_6_ = i_0_;
	    for (int i_7_ = i_2_; i_7_ != anInt1307 && i_6_ > anInt1304;
		 i_7_ = i_7_ + 1 & 0x1ff) {
		int i_8_ = anIntArray1315[i_7_];
		if (i_8_ < i_6_)
		    i_6_ = i_8_;
	    }
	    anInt1304 = i_6_;
	}
	anInt1307 = i_2_;
    }
    
    public void method196(long l) {
	if (aLong1311 != 0L) {
	    for (/**/; aLong1305 < l;
		 aLong1305 += (long) (256000 / Class15.anInt274))
		Class13.method194(7, 256);
	    if (l < aLong1311)
		return;
	    try {
		method203(l);
	    } catch (Exception exception) {
		destroy();
		aLong1311 += 5000L;
		return;
	    }
	    aLong1311 = 0L;
	}
	while (aLong1305 < l) {
	    aLong1305 += (long) (250880 / Class15.anInt274);
	    int i;
	    try {
		i = method201();
	    } catch (Exception exception) {
		destroy();
		aLong1311 = l;
		return;
	    }
	    method195(i);
	    int i_9_ = anInt1313 * 3 / 512 - anInt1304 * 2;
	    if (i_9_ < 0)
		i_9_ = 0;
	    else if (i_9_ > anInt1306)
		i_9_ = anInt1306;
	    anInt1317 = anInt1309 - 256 - i_9_;
	    if (anInt1317 < 256)
		anInt1317 = 256;
	    if (anInt1309 < 16384) {
		if (i >= anInt1309) {
		    anInt1308 += 5;
		    if (anInt1308 >= 100) {
			destroy();
			anInt1309 += 2048;
			aLong1311 = l;
			return;
		    }
		} else if (i != anInt1316 && anInt1308 > 0)
		    anInt1308--;
	    }
	    anInt1316 = i;
	    if (i < anInt1317)
		break;
	    Class13.method190(anIntArray1310, 256);
	    try {
		method199();
	    } catch (Exception exception) {
		destroy();
		aLong1311 = l;
		return;
	    }
	    aLong1312 = l;
	    anInt1316 -= 256;
	}
	if (l >= aLong1312 + 5000L) {
	    destroy();
	    aLong1311 = l;
	    for (int i = 0; i < 512; i++)
		anIntArray1315[i] = 0;
	    anInt1304 = anInt1306 = anInt1313 = 0;
	}
    }
    
    public void method197(Signlink class21, int i) throws Exception {
	anInt1309 = i;
	method203(Class2.getSystemTime());
	class21.requestThread(this, 10);
    }
    
    public static void method198() {
	anIntArray1310 = null;
    }
    
    public abstract void method199() throws Exception;
    
    public void method187() {
	synchronized (this) {
	    aBoolean1314 = true;
	}
	for (;;) {
	    synchronized (this) {
		if (!aBoolean1314)
		    break;
	    }
	    Class45.sleep(50L);
	}
	anIntArray1310 = null;
    }
    
    public abstract void method200(int i) throws Exception;
    
    public abstract int method201() throws Exception;
    
    public abstract void method202
	(Component component, int i, boolean bool) throws Exception;
    
    public void run() {
	try {
	    for (;;) {
		synchronized (this) {
		    if (aBoolean1314) {
			if (aLong1311 == 0L)
			    destroy();
			aBoolean1314 = false;
			break;
		    }
		    method193(Class2.getSystemTime());
		}
		Class45.sleep(5L);
	    }
	} catch (Exception exception) {
	    Class39_Sub7.method849(exception, 64, null);
	}
    }
    
    public void method203(long l) throws Exception {
	method200(anInt1309);
	for (;;) {
	    int i = method201();
	    if (i < anInt1317)
		break;
	    method199();
	}
	anInt1308 = 0;
	anInt1316 = 0;
	aLong1305 = l;
	aLong1312 = l;
    }
    
    public synchronized void method193(long l) {
	method196(l);
	if (aLong1305 < l)
	    aLong1305 = l;
    }
    
    public abstract void destroy();
    
    public Class13_Sub1() {
	anInt1304 = 0;
	aLong1311 = 0L;
	aBoolean1314 = false;
	anIntArray1315 = new int[512];
	anInt1317 = 256;
	int i = 256;
	if (StillGraphic.aBoolean2342)
	    i <<= 1;
	anIntArray1310 = new int[i];
    }
}
