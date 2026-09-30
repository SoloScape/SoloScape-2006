package unpackaged;

import jagex.graphics.BitmapFont;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.utils.Queue;

/* Class46_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class46_Sub1 extends Class46
{
    public static int anInt1547;
    public static int[] anIntArray1548;
    public static int[] anIntArray1549;
    public static int anInt1550 = 2;
    public int anInt1551;
    public static JString[] aClass3Array1552;
    public static int anInt1553;
    public int anInt1554;
    public static int[] anIntArray1555;
    public static JString aClass3_1556;
    public static JString aClass3_1557;
    public static int[] anIntArray1558;
    public static JString aClass3_1559;
    public int anInt1560;
    public static int anInt1561;
    public static int anInt1562;
    public long[] aLongArray1563 = new long[10];
    public static int[] anIntArray1564;
    public long aLong1565;
    public int anInt1566;
    public static JString aClass3_1567;
    public static int anInt1568;
    
    public int method939(int i, int i_0_, int i_1_) {
	int i_2_ = anInt1566;
	int i_3_ = anInt1551;
	anInt1551 = 300;
	anInt1566 = 1;
	if (i != 4)
	    method934(-54);
	aLong1565 = Class2.getSystemTime();
	if (aLongArray1563[anInt1554] != 0L) {
	    if (aLong1565 > aLongArray1563[anInt1554])
		anInt1551 = (int) ((long) (i_1_ * 2560)
				   / (-aLongArray1563[anInt1554] + aLong1565));
	} else {
	    anInt1551 = i_3_;
	    anInt1566 = i_2_;
	}
	if (anInt1551 < 25)
	    anInt1551 = 25;
	if (anInt1551 > 256) {
	    anInt1551 = 256;
	    anInt1566
		= (int) (-((aLong1565 + -aLongArray1563[anInt1554]) / 10L)
			 + (long) i_1_);
	}
	if (anInt1566 > i_1_)
	    anInt1566 = i_1_;
	aLongArray1563[anInt1554] = aLong1565;
	anInt1554 = (anInt1554 + 1) % 10;
	if (anInt1566 > 1) {
	    for (int i_4_ = 0; i_4_ < 10; i_4_++) {
		if (aLongArray1563[i_4_] != 0L)
		    aLongArray1563[i_4_]
			= aLongArray1563[i_4_] + (long) anInt1566;
	    }
	}
	if (i_0_ > anInt1566)
	    anInt1566 = i_0_;
	Class45.sleep((long) anInt1566);
	int i_5_ = 0;
	for (/**/; anInt1560 < 256; anInt1560 += anInt1551)
	    i_5_++;
	anInt1560 &= 0xff;
	return i_5_;
    }
    
    public static BitmapFont createBitmapFont(FileTable fileTable, JString archiveName, JString childName) {
	int archiveId = fileTable.lookupArchive(archiveName);
	int childId = fileTable.lookupChild(archiveId, childName);
	return Queue.createBitmapFont(fileTable, archiveId, childId);
    }
    
    public static void method943(boolean bool) {
	aClass3_1557 = null;
	anIntArray1549 = null;
	aClass3_1567 = null;
	aClass3_1559 = null;
	anIntArray1555 = null;
	anIntArray1558 = null;
	anIntArray1548 = null;
	anIntArray1564 = null;
	aClass3Array1552 = null;
	aClass3_1556 = null;
    }
    
    public void method938(byte i) {
	if (i < -20) {
	    anInt1551 = 256;
	    anInt1566 = 1;
	    anInt1560 = 0;
	    aLong1565 = Class2.getSystemTime();
	    for (int i_9_ = 0; i_9_ < 10; i_9_++)
		aLongArray1563[i_9_] = aLong1565;
	}
    }
    
    public void method934(int i) {
	for (int i_10_ = i; i_10_ < 10; i_10_++)
	    aLongArray1563[i_10_] = 0L;
    }
    
    public static void method944
	(Player class39_sub5_sub4_sub4_sub2, int i,
	 int i_11_, int i_12_) {
	if (i == class39_sub5_sub4_sub4_sub2.anInt2268 && i != -1) {
	    int i_13_ = Class62_Sub1.method1064(i, (byte) 54).anInt1830;
	    if (i_13_ == 1) {
		class39_sub5_sub4_sub4_sub2.anInt2305 = i_12_;
		class39_sub5_sub4_sub4_sub2.anInt2291 = 0;
		class39_sub5_sub4_sub4_sub2.anInt2265 = 0;
		class39_sub5_sub4_sub4_sub2.anInt2311 = 0;
	    }
	    if (i_13_ == 2)
		class39_sub5_sub4_sub4_sub2.anInt2291 = 0;
	} else if (i == -1 || class39_sub5_sub4_sub4_sub2.anInt2268 == -1
		   || (Class62_Sub1.method1064(i, (byte) 54).anInt1826
		       >= (Class62_Sub1.method1064
			   (class39_sub5_sub4_sub4_sub2.anInt2268, (byte) 54)
			   .anInt1826))) {
	    class39_sub5_sub4_sub4_sub2.anInt2305 = i_12_;
	    class39_sub5_sub4_sub4_sub2.anInt2254
		= class39_sub5_sub4_sub4_sub2.anInt2312;
	    class39_sub5_sub4_sub4_sub2.anInt2291 = 0;
	    class39_sub5_sub4_sub4_sub2.anInt2265 = 0;
	    class39_sub5_sub4_sub4_sub2.anInt2311 = 0;
	    class39_sub5_sub4_sub4_sub2.anInt2268 = i;
	}
    }
    
    public Class46_Sub1() {
	method938((byte) -112);
    }
    
    static {
	anInt1547 = 0;
	anIntArray1555 = new int[] { 9104, 10275, 7595, 3610, 7975, 8526, 918,
				     38802, 24466, 10145, 58654, 5027, 1457,
				     16565, 34991, 25486 };
	aClass3_1557
	    = Class39_Sub5_Sub9.createJstring("Lade Benutzeroberfl-=che )2 ");
	aClass3_1559 = Class39_Sub5_Sub9.createJstring("Icons redrawn");
	anIntArray1549 = new int[1000];
	anInt1561 = 0;
	anInt1562 = 0;
	anInt1553 = 256;
	aClass3Array1552 = new JString[100];
	aClass3_1556
	    = Class39_Sub5_Sub9
		  .createJstring("Verbindung mit Update)2Server)3)3)3");
	anIntArray1558 = new int[128];
	aClass3_1567
	    = Class39_Sub5_Sub9
		  .createJstring("Ihr Spielkonto wird bereits benutzt)3");
    }
}
