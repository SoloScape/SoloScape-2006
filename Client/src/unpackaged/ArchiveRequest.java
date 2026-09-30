package unpackaged;

/* Class39_Sub9 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.io.BufferedFile;
import jagex.graphics.DrawingArea;
import jagex.world.actors.GroundItem;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.awt.Graphics;

public class ArchiveRequest extends Node
{
    public static int[] anIntArray1400;
    public static int anInt1401;
    public static JString aClass3_1402
	= Class39_Sub5_Sub9.createJstring("W-=hlen Sie eine Option");
    public static int anInt1403;
    public byte[] payload;
    public static int anInt1405;
    public FileLoader aClass9_Sub1_1406;
    public static int anInt1407;
    public static int anInt1408;
    public CacheIO cache;
    public int type;
    public static int anInt1411;
    public static int[][][] anIntArrayArrayArray1412;
    public static int anInt1413;
    public static JString aClass3_1414;
    public static int anInt1415;
    
    public static void method857(int i, boolean bool, byte[] is,
				 boolean bool_0_) {
	if (Class55.aClass62_1251 != null) {
	    if (GroundItem.anInt2239 >= 0) {
		Class55.aClass62_1251.method1048(false);
		GroundItem.anInt2239 = -1;
		Class65.anInt1141 = 0;
		Class39_Sub5_Sub6.aByteArray1768 = null;
		anInt1415 = 20;
	    }
	    if (is != null) {
		if (anInt1415 > 0) {
		    Class55.aClass62_1251.method1053(i, (byte) 58);
		    anInt1415 = 0;
		}
		GroundItem.anInt2239 = i;
		Class55.aClass62_1251.method1051(i, is, (byte) 84, bool);
	    }
	}
    }
    
    public static void method858(byte i) {
	Class39_Sub4.method459(SubNode.anInt1348, (byte) 108);
	if (ClientScript.anInt1713 != -1)
	    Class39_Sub4.method459(ClientScript.anInt1713, (byte) 116);
	Class45.anInt856 = 0;
	Class34.aClass57_610.method1006(10);
	Buffer.anIntArray1355
	    = Class39_Sub5_Sub10_Sub2.method657(Buffer.anIntArray1355);
	DrawingArea.reset();
	Deque.method955(503, 765, 0, SubNode.anInt1348, 0, -1, 0);
	if (ClientScript.anInt1713 != -1)
	    Deque.method955(503, 765, 0, ClientScript.anInt1713, 0, -1,
			      0);
	if (!Class39_Sub12.aBoolean1493) {
	    JString.method95(18);
	    Class31.method320(-23401);
	} else
	    Class1.method49(-112);
	try {
	    Graphics graphics = Class41.aCanvas778.getGraphics();
	    Class34.aClass57_610.draw(graphics, 0, 0);
	} catch (Exception exception) {
	    Class41.aCanvas778.repaint();
	}
    }
    
    public static void method859(boolean bool) {
	aClass3_1402 = null;
	anIntArray1400 = null;
	anIntArrayArrayArray1412 = null;
	aClass3_1414 = null;
    }
    
    public static IndexedColorSprite method860(FileTable class9, int i,
						    int i_1_) {
	if (!TraversalMap.method310(class9, i_1_, 84))
	    return null;
	return Bzip2Block.method1036(false);
    }
    
    public static int method861(byte i, FileTable class9) {
	int i_2_ = 0;
	if (class9.method152(22411, Class30.aClass3_533,
			     Class39_Sub11.aClass3_1455))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Class43.aClass3_803))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Class34.aClass3_609))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Bzip2Block.aClass3_1056))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Class30.aClass3_545))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Cache.aClass3_107))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, IsaacPrng.aClass3_1103))
	    i_2_++;
	if (class9.method152(i ^ ~0x57b5, Class30.aClass3_533,
			     Class67.aClass3_1169))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, BufferedFile.aClass3_348))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Class41.aClass3_771))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533,
			     Class39_Sub5_Sub14.aClass3_1904))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533,
			     Class39_Sub5_Sub18.aClass3_2125))
	    i_2_++;
	if (i != -63)
	    method859(false);
	if (class9.method152(22411, Class30.aClass3_533,
			     Class39_Sub4.aClass3_1340))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, Class43.aClass3_822))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, IsaacPrng.aClass3_1090))
	    i_2_++;
	if (class9.method152(i + 22474, Class30.aClass3_533,
			     Npc.aClass3_2489))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533,
			     ObjectDefinition.aClass3_1937))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533,
			     FrameBuffer.aClass3_2149))
	    i_2_++;
	if (class9.method152(22411, Class30.aClass3_533, ArchiveWorker.aClass3_1200))
	    i_2_++;
	return i_2_;
    }
    
    public static boolean method862(int i, int i_3_, int i_4_) {
	ObjectDefinition class39_sub5_sub15
	    = Canvas_Sub1.method40(i_4_, (byte) 89);
	if (i_3_ == 11)
	    i_3_ = 10;
	if (i_3_ >= 5 && i_3_ <= 8)
	    i_3_ = 4;
	return class39_sub5_sub15.method740(127, i_3_);
    }
    
    public static void method863(int i, int i_5_) {
	Class39_Sub13 class39_sub13
	    = ((Class39_Sub13)
	       Class14.aClass19_213.fetch((long) i));
	if (class39_sub13 != null) {
	    for (int i_6_ = 0; i_6_ < class39_sub13.anIntArray1505.length;
		 i_6_++) {
		class39_sub13.anIntArray1505[i_6_] = -1;
		class39_sub13.anIntArray1504[i_6_] = 0;
	    }
	}
    }
    
    static {
	anIntArray1400 = new int[2048];
	anInt1407 = 0;
	anInt1411 = -1;
	anInt1401 = 0;
	anInt1405 = 0;
	aClass3_1414 = Class39_Sub5_Sub9.createJstring("::qa_op_test");
	anInt1403 = 0;
	anInt1413 = 0;
	anInt1415 = 0;
    }
}
