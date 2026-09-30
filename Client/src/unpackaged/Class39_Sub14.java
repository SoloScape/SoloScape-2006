package unpackaged;

import jagex.graphics.BitmapFont;
import jagex.graphics.DrawingArea;
import jagex.utils.Huffmans;
import jagex.world.actors.GroundItem;
import jagex.graphics.JImage;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.world.map.TraversalMap;
import jagex.utils.Queue;
import jagex.io.Buffer;

/* Class39_Sub14 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub14 extends Node
{
    public static JString aClass3_1510
	= (Class39_Sub5_Sub9.createJstring
	   ("Spieler)3 Bitte w-=hlen Sie eine andere Welt)3"));
    public static int anInt1511;
    public static int[] anIntArray1512;
    public static JString aClass3_1513;
    public static JString aClass3_1514;
    public static JString aClass3_1515;
    public static byte[][][] aByteArrayArrayArray1516;
    public static int anInt1517;
    public static JString aClass3_1518;
    public static JString[] aClass3Array1519 = new JString[5];
    public static boolean aBoolean1520;
    public static JString aClass3_1521;
    public static JString aClass3_1522;
    public int anInt1523;
    public Class10[] aClass10Array1524;
    public boolean aBoolean1525;
    public int anInt1526;
    public int anInt1527;
    public Class36 aClass36_1528;
    public boolean aBoolean1529;
    public Class44 aClass44_1530;
    public Class17 aClass17_1531;
    public int[] anIntArray1532 = new int[5];
    public int anInt1533;
    public int anInt1534;
    public int anInt1535;
    public int anInt1536;
    public boolean aBoolean1537;
    public int anInt1538;
    public Class50 aClass50_1539;
    public Class39_Sub14 aClass39_Sub14_1540;
    public Class67 aClass67_1541;
    public Class32 aClass32_1542;
    public static int[] anIntArray1543;
    public int anInt1544;
    public int anInt1545;
    public int anInt1546;
    
    public static void method876(int i) {
	JImage.anInt1586 = 0;
	Huffmans.anInt749 = 0;
	Class53.method984(-93);
	Class62_Sub2.method1075((byte) 126);
	Widget.method763(-4322);
	for (int i_0_ = i; i_0_ < Huffmans.anInt749; i_0_++) {
	    int i_1_ = Class26.anIntArray496[i_0_];
	    if ((GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241
		 [i_1_].anInt2290)
		!= Class2.logicCycle) {
		GroundItem
		    .aClass39_Sub5_Sub4_Sub4_Sub1Array2241[i_1_]
		    .aClass39_Sub5_Sub13_2492
		    = null;
		GroundItem
		    .aClass39_Sub5_Sub4_Sub4_Sub1Array2241[i_1_]
		    = null;
	    }
	}
	if (Class39_Sub5_Sub11.gameBuffer.offset
	    != Huffmans.frameSize)
	    throw new RuntimeException("gnp1 pos:"
				       + (Class39_Sub5_Sub11
					  .gameBuffer.offset)
				       + " psize:" + Huffmans.frameSize);
	for (int i_2_ = 0; ArchiveWorker.anInt1210 > i_2_; i_2_++) {
	    if ((GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241
		 [Class39_Sub5_Sub4.anIntArray1734[i_2_]])
		== null)
		throw new RuntimeException("gnp2 pos:" + i_2_ + " size:"
					   + ArchiveWorker.anInt1210);
	}
    }
    
    public static BitmapFont createBitmapFont() {
	BitmapFont bitmapFont
	    = new BitmapFont(SubNode.anIntArray1352,
					  Class39_Sub5_Sub9.anIntArray1799,
					  anIntArray1512,
					  Class39_Sub11.anIntArray1460,
					  TraversalMap.aByteArrayArray517);
	RuntimeException_Sub1.method1123();
	return bitmapFont;
    }
    
    public static int method878(int i, int i_3_, int i_4_) {
	int i_5_ = (Class50.method976(i_3_ - 1, i_4_ - 1, (byte) 118)
		    - (-Class50.method976(i_3_ - 1, i_4_ + 1, (byte) 118)
		       - Class50.method976(i_3_ + 1, i_4_ - 1, (byte) 118)
		       - Class50.method976(i_3_ + 1, i_4_ + 1, (byte) 118)));
	int i_6_ = (Class50.method976(i_3_, i_4_ - 1, (byte) 118)
		    + (Class50.method976(i_3_, i_4_ + 1, (byte) 118)
		       - (-Class50.method976(i_3_ - 1, i_4_, (byte) 118)
			  - Class50.method976(i_3_ + 1, i_4_, (byte) 118))));
	int i_7_ = Class50.method976(i_3_, i_4_, (byte) 118);
	return i_7_ / 4 + i_5_ / 16 + i_6_ / 8;
    }
    
    public static void setState(int state) {
	if (state != Class31.state) {
	    if (Class31.state == 0)
		Class62_Sub2.method1080((byte) -76);
	    if (state == 20 || state == 40) {
		Buffer.anInt1353 = 0;
		Class15.anInt280 = 0;
		ArchiveRequest.anInt1413 = 0;
	    }
	    if (state != 20 && state != 40 && ObjectDefinition.aClass16_1935 != null) {
		ObjectDefinition.aClass16_1935.stop();
		ObjectDefinition.aClass16_1935 = null;
	    }
	    if (Class31.state == 25 || Class31.state == 40) {
		ClientScript.method481(-6414);
		DrawingArea.reset();
	    }
	    if (Class31.state == 25) {
		Class66.anInt1157 = 1;
		Varbit.anInt1798 = 0;
		RuntimeException_Sub1.anInt1222 = 0;
		Class39_Sub5_Sub11.anInt1843 = 1;
		Class1.anInt33 = 0;
	    }
	    if (state == 0 || state == 35) {
		ItemDefinition.method470();
		TraversalMap.method298((byte) 82);
		if (Class34.aClass57_610 == null)
		    Class34.aClass57_610
			= Queue.method994(Class41.aCanvas778, 765, 503,
					    (byte) -106);
	    }
	    if (state == 5 || state == 10 || state == 20) {
		Class34.aClass57_610 = null;
		ItemDefinition.method470();
		Class39_Sub5_Sub4.method490(Class41.aCanvas778,
					    TraversalMap.fileLoader8, 17056,
					    Class66.fileLoader10);
	    }
	    if (state == 25 || state == 30 || state == 40) {
		Class34.aClass57_610 = null;
		TraversalMap.method298((byte) 82);
		Class48.method952(Class41.aCanvas778,
				  TraversalMap.fileLoader8);
	    }
	    ClientScript.aBoolean1690 = true;
	    Class31.state = state;
	}
    }
    
    public static void method880(int i) {
	anIntArray1543 = null;
	aClass3_1510 = null;
	aClass3_1518 = null;
	aClass3_1522 = null;
	aClass3_1514 = null;
	aClass3Array1519 = null;
	aClass3_1513 = null;
	anIntArray1512 = null;
	aClass3_1515 = null;
	if (i == 5) {
	    aClass3_1521 = null;
	    aByteArrayArrayArray1516 = null;
	}
    }
    
    public Class39_Sub14(int i, int i_9_, int i_10_) {
	aClass10Array1524 = new Class10[5];
	anInt1535 = 0;
	anInt1534 = anInt1546 = i;
	anInt1544 = i_10_;
	anInt1526 = i_9_;
    }
    
    static {
	anInt1511 = 0;
	aClass3_1518
	    = Class39_Sub5_Sub9.createJstring("Invalid username or password)3");
	aClass3_1513
	    = Class39_Sub5_Sub9.createJstring("60 Sekunden noch einmal)3)3)3");
	anInt1517 = 0;
	aClass3_1522
	    = Class39_Sub5_Sub9
		  .createJstring(" zuerst von Ihrer Ignorieren)2Liste(Q");
	aClass3_1515 = Class39_Sub5_Sub9.createJstring("To");
	aClass3_1521 = aClass3_1518;
	aBoolean1520 = false;
	aClass3_1514 = aClass3_1515;
	anIntArray1543 = new int[25];
    }
}
