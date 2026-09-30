package unpackaged;

/* Class23 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.JImage;
import jagex.graphics.AbstractImage;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.utils.Deque;
import jagex.utils.Cache;
import java.util.zip.CRC32;

public class Class23
{
    public static CRC32 crc;
    public static JString aClass3_422
	= Class39_Sub5_Sub9.createJstring("titlebox");
    public static int[] anIntArray423;
    public static JString aClass3_424;
    public static int anInt425;
    public static byte[][] aByteArrayArray426;
    public static JString aClass3_427;
    public static int anInt428;
    public static JString aClass3_429
	= Class39_Sub5_Sub9.createJstring("Please wait 1 minute and try again)3");
    public static volatile int anInt430 = 0;
    public static JString aClass3_431;
    public static JString aClass3_432;
    public static int archiveWorkerKeepAlive;
    public static Deque projectiles;
    public static AbstractImage aClass57_435;
    public static int anInt436;
    public static JString aClass3_437;
    public static JString aClass3_438;
    
    public static void method271(byte i) {
	if (Class45.aBoolean867 && NameTable.height != JImage.anInt1573)
	    OndemandRequest.method486
		(NameTable.height, Class65.anInt1147, (byte) -96,
		 Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2314[0],
		 Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2255[0],
		 Class62_Sub2.anInt1597);
	else if (ArchiveRequest.anInt1411 != NameTable.height) {
	    ArchiveRequest.anInt1411 = NameTable.height;
	    Deque.method958((byte) -112, NameTable.height);
	}
    }
    
    public static void method272(int i) {
	aClass3_429 = null;
	aClass3_432 = null;
	aClass57_435 = null;
	crc = null;
	aClass3_422 = null;
	projectiles = null;
	aClass3_431 = null;
	aClass3_438 = null;
	aClass3_437 = null;
	if (i != -1)
	    anInt430 = 118;
	aClass3_427 = null;
	anIntArray423 = null;
	aByteArrayArray426 = null;
	aClass3_424 = null;
    }
    
    public static DirectColorSprite[] method273
	(int i, byte i_0_, int i_1_, FileTable class9) {
	if (!Client.decodeBitmapFont(class9, i_1_, i))
	    return null;
	return Widget.method764(5563);
    }
    
    public static int method274(byte i) {
	int i_2_ = Class14.method212(Node.anInt742, 9990, NameTable.height,
				     Class39_Sub11.anInt1470);
	if (-Class39_Sub10.anInt1437 + i_2_ < 800
	    && ((Class55.tileFlags[NameTable.height]
		 [Class39_Sub11.anInt1470 >> 7][Node.anInt742 >> 7])
		& 0x4) != 0)
	    return NameTable.height;
	return 3;
    }
    
    static {
	aClass3_424 = aClass3_429;
	aClass3_427 = aClass3_429;
	aByteArrayArray426 = new byte[250][];
	crc = new CRC32();
	archiveWorkerKeepAlive = 0;
	aClass3_432
	    = Class39_Sub5_Sub9
		  .createJstring("Your ignore list is full)3 Max of 100 hit");
	aClass3_431 = aClass3_432;
	projectiles = new Deque();
	anInt436 = -1;
	aClass3_438 = Class39_Sub5_Sub9.createJstring("Ok");
	aClass3_437 = aClass3_438;
    }
}
