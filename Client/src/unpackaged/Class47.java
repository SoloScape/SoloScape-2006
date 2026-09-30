package unpackaged;

import jagex.graphics.sprites.DirectColorSprite;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;

/* Class47 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class47
{
    public static JString aClass3_889
	= Class39_Sub5_Sub9
	      .createJstring("Verbindung konnte nicht hergestellt werden)3");
    public static JString aClass3_890;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_891;
    public static JString aClass3_892
	= Class39_Sub5_Sub9.createJstring("Connecting to update server");
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array893;
    public static boolean aBoolean894;
    public static JString aClass3_895;
    public static JString aClass3_896;
    public static JString aClass3_897;
    public static FileTable aClass9_898;
    
    public static void method945(boolean bool) {
	aClass3_890 = null;
	aClass3_892 = null;
	aClass3_889 = null;
	aClass3_895 = null;
	aClass3_897 = null;
	aClass39_Sub5_Sub10_Sub3Array893 = null;
	aClass39_Sub5_Sub10_Sub4_891 = null;
	aClass9_898 = null;
	aClass3_896 = null;
    }
    
    public static boolean method946(byte i) {
	if (Class55.aClass62_1251 == null)
	    return false;
	return true;
    }
    
    public static void setNpcFileLoaders(FileTable class9, FileTable class9_0_) {
	Varbit.npcFileLoader = class9;
	ObjectDefinition.modelFileLoader = class9_0_;
    }
    
    public static void method948(int i) {
	if (Class46.anInt887 == 1) {
	    if (Class39_Sub4.anInt1329 >= 539 && Class39_Sub4.anInt1329 <= 573
		&& Bzip2Block.anInt1054 >= 169 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[0] != -1) {
		Node.anInt728 = 0;
		IsaacPrng.aBoolean1089 = true;
		Class39_Sub14.aBoolean1520 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 569 && Class39_Sub4.anInt1329 <= 599
		&& Bzip2Block.anInt1054 >= 168 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[1] != -1) {
		Node.anInt728 = 1;
		IsaacPrng.aBoolean1089 = true;
		Class39_Sub14.aBoolean1520 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 597 && Class39_Sub4.anInt1329 <= 627
		&& Bzip2Block.anInt1054 >= 168 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[2] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		Node.anInt728 = 2;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 625 && Class39_Sub4.anInt1329 <= 669
		&& Bzip2Block.anInt1054 >= 168 && Bzip2Block.anInt1054 < 203
		&& Class39_Sub5_Sub14.anIntArray1914[3] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		Node.anInt728 = 3;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 666 && Class39_Sub4.anInt1329 <= 696
		&& Bzip2Block.anInt1054 >= 168 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[4] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		IsaacPrng.aBoolean1089 = true;
		Node.anInt728 = 4;
	    }
	    if (Class39_Sub4.anInt1329 >= 694 && Class39_Sub4.anInt1329 <= 724
		&& Bzip2Block.anInt1054 >= 168 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[5] != -1) {
		IsaacPrng.aBoolean1089 = true;
		Node.anInt728 = 5;
		Class39_Sub14.aBoolean1520 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 722 && Class39_Sub4.anInt1329 <= 756
		&& Bzip2Block.anInt1054 >= 169 && Bzip2Block.anInt1054 < 205
		&& Class39_Sub5_Sub14.anIntArray1914[6] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		Node.anInt728 = 6;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 540 && Class39_Sub4.anInt1329 <= 574
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 502
		&& Class39_Sub5_Sub14.anIntArray1914[7] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		Node.anInt728 = 7;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 572 && Class39_Sub4.anInt1329 <= 602
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 503
		&& Class39_Sub5_Sub14.anIntArray1914[8] != -1) {
		Node.anInt728 = 8;
		Class39_Sub14.aBoolean1520 = true;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 599 && Class39_Sub4.anInt1329 <= 629
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 503
		&& Class39_Sub5_Sub14.anIntArray1914[9] != -1) {
		Node.anInt728 = 9;
		Class39_Sub14.aBoolean1520 = true;
		IsaacPrng.aBoolean1089 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 627 && Class39_Sub4.anInt1329 <= 671
		&& Bzip2Block.anInt1054 >= 467 && Bzip2Block.anInt1054 < 502
		&& Class39_Sub5_Sub14.anIntArray1914[10] != -1) {
		Node.anInt728 = 10;
		IsaacPrng.aBoolean1089 = true;
		Class39_Sub14.aBoolean1520 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 669 && Class39_Sub4.anInt1329 <= 699
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 503
		&& Class39_Sub5_Sub14.anIntArray1914[11] != -1) {
		IsaacPrng.aBoolean1089 = true;
		Class39_Sub14.aBoolean1520 = true;
		Node.anInt728 = 11;
	    }
	    if (Class39_Sub4.anInt1329 >= 696 && Class39_Sub4.anInt1329 <= 726
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 503
		&& Class39_Sub5_Sub14.anIntArray1914[12] != -1) {
		Node.anInt728 = 12;
		IsaacPrng.aBoolean1089 = true;
		Class39_Sub14.aBoolean1520 = true;
	    }
	    if (Class39_Sub4.anInt1329 >= 724 && Class39_Sub4.anInt1329 <= 758
		&& Bzip2Block.anInt1054 >= 466 && Bzip2Block.anInt1054 < 502
		&& Class39_Sub5_Sub14.anIntArray1914[13] != -1) {
		Class39_Sub14.aBoolean1520 = true;
		IsaacPrng.aBoolean1089 = true;
		Node.anInt728 = 13;
	    }
	}
    }
    
    public static void method949(int i) {
	Class39_Sub5_Sub6.aClass7_1762.method134(i ^ ~0x7da1);
	if (i != -5794)
	    method945(false);
    }
    
    static {
	aClass3_890
	    = Class39_Sub5_Sub9.createJstring("Geben Sie Ihren Benutzernamen");
	aClass3_896 = aClass3_892;
	aClass3_897
	    = (Class39_Sub5_Sub9.createJstring
	       ("To play on this world move to a free area first"));
	aClass3_895 = aClass3_897;
    }
}
