package unpackaged;

import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.JString;

/* Class39_Sub5_Sub4_Sub2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub5_Sub4_Sub2 extends Class39_Sub5_Sub4
{
    public int anInt2211;
    public int anInt2212;
    public static JString aClass3_2213
	= Class39_Sub5_Sub9.createJstring("::fpson");
    public Class39_Sub5_Sub11 aClass39_Sub5_Sub11_2214;
    public int anInt2215;
    public int anInt2216;
    public static JString aClass3_2217;
    public static JString aClass3_2218;
    public static DirectColorSprite aClass39_Sub5_Sub10_Sub3_2219;
    public static int anInt2220;
    public int anInt2221;
    public static JString aClass3_2222;
    public int anInt2223;
    public static JString aClass3_2224;
    public static int[] queuedStateValues;
    public int anInt2226;
    public static JString aClass3_2227
	= Class39_Sub5_Sub9.createJstring("Please wait)3)3)3");
    public static JString aClass3_2228;
    public int anInt2229;
    public static JString aClass3_2230;
    public static int[] anIntArray2231;
    public int anInt2232;
    
    public static void method499(int i) {
	queuedStateValues = null;
	aClass3_2218 = null;
	aClass39_Sub5_Sub10_Sub3_2219 = null;
	aClass3_2213 = null;
	aClass3_2228 = null;
	aClass3_2230 = null;
	anIntArray2231 = null;
	aClass3_2222 = null;
	aClass3_2227 = null;
	aClass3_2217 = null;
	aClass3_2224 = null;
    }
    
    public static void method500(byte i, int i_0_, int i_1_, int i_2_,
				 ObjectDefinition class39_sub5_sub15,
				 int i_3_) {
	Class39_Sub10 class39_sub10 = new Class39_Sub10();
	class39_sub10.anInt1432 = i_2_ * 128;
	class39_sub10.anInt1427 = i_0_ * 128;
	int i_4_ = class39_sub5_sub15.anInt1948;
	class39_sub10.anIntArray1416 = class39_sub5_sub15.anIntArray1920;
	class39_sub10.anInt1423 = i_3_;
	class39_sub10.anInt1426 = class39_sub5_sub15.anInt1956;
	class39_sub10.anInt1446 = class39_sub5_sub15.anInt1924 * 128;
	class39_sub10.anInt1433 = class39_sub5_sub15.anInt1945;
	class39_sub10.anInt1439 = class39_sub5_sub15.anInt1930;
	int i_5_ = class39_sub5_sub15.anInt1925;
	if (i_1_ == 1 || i_1_ == 3) {
	    i_5_ = class39_sub5_sub15.anInt1948;
	    i_4_ = class39_sub5_sub15.anInt1925;
	}
	class39_sub10.anInt1438 = (i_2_ + i_5_) * 128;
	class39_sub10.anInt1434 = (i_4_ + i_0_) * 128;
	if (class39_sub5_sub15.anIntArray1961 != null) {
	    class39_sub10.aClass39_Sub5_Sub15_1428 = class39_sub5_sub15;
	    class39_sub10.method864(-106);
	}
	Class4.aClass49_74.offerLast(class39_sub10);
	if (class39_sub10.anIntArray1416 != null)
	    class39_sub10.anInt1442
		= (class39_sub10.anInt1426
		   + (int) ((double) (class39_sub10.anInt1439
				      - class39_sub10.anInt1426)
			    * Math.random()));
    }
    
    public Model method489(boolean bool) {
	if (aClass39_Sub5_Sub11_2214 != null) {
	    int i = Class2.logicCycle - anInt2216;
	    if (i > 100 && aClass39_Sub5_Sub11_2214.anInt1839 > 0)
		i = 100;
	    while (i > aClass39_Sub5_Sub11_2214.anIntArray1831[anInt2211]) {
		i -= aClass39_Sub5_Sub11_2214.anIntArray1831[anInt2211];
		anInt2211++;
		if (anInt2211
		    >= aClass39_Sub5_Sub11_2214.anIntArray1833.length) {
		    anInt2211 -= aClass39_Sub5_Sub11_2214.anInt1839;
		    if (anInt2211 < 0
			|| anInt2211 >= (aClass39_Sub5_Sub11_2214
					 .anIntArray1833).length) {
			aClass39_Sub5_Sub11_2214 = null;
			break;
		    }
		}
	    }
	    anInt2216 = Class2.logicCycle - i;
	}
	ObjectDefinition class39_sub5_sub15
	    = Canvas_Sub1.method40(anInt2215, (byte) 121);
	if (class39_sub5_sub15.anIntArray1961 != null)
	    class39_sub5_sub15 = class39_sub5_sub15.method733(0);
	if (bool != true)
	    return null;
	if (class39_sub5_sub15 == null)
	    return null;
	return class39_sub5_sub15.method737(anInt2221, anInt2223, anInt2211,
					    anInt2226, anInt2229, 105,
					    anInt2232,
					    aClass39_Sub5_Sub11_2214,
					    anInt2212);
    }
    
    public Class39_Sub5_Sub4_Sub2
	(int i, int i_6_, int i_7_, int i_8_, int i_9_, int i_10_, int i_11_,
	 int i_12_, boolean bool, Class39_Sub5_Sub4 class39_sub5_sub4) {
	anInt2215 = i;
	anInt2223 = i_10_;
	anInt2229 = i_9_;
	anInt2221 = i_11_;
	anInt2232 = i_7_;
	anInt2226 = i_6_;
	anInt2212 = i_8_;
	if (i_12_ != -1) {
	    aClass39_Sub5_Sub11_2214
		= Class62_Sub1.method1064(i_12_, (byte) 54);
	    anInt2211 = 0;
	    anInt2216 = Class2.logicCycle - 1;
	    if (aClass39_Sub5_Sub11_2214.anInt1830 == 0
		&& class39_sub5_sub4 != null
		&& class39_sub5_sub4 instanceof Class39_Sub5_Sub4_Sub2) {
		Class39_Sub5_Sub4_Sub2 class39_sub5_sub4_sub2_13_
		    = (Class39_Sub5_Sub4_Sub2) class39_sub5_sub4;
		if (class39_sub5_sub4_sub2_13_.aClass39_Sub5_Sub11_2214
		    == aClass39_Sub5_Sub11_2214) {
		    anInt2216 = class39_sub5_sub4_sub2_13_.anInt2216;
		    anInt2211 = class39_sub5_sub4_sub2_13_.anInt2211;
		    return;
		}
	    }
	    if (bool && aClass39_Sub5_Sub11_2214.anInt1839 != -1) {
		anInt2211 = (int) ((double) (aClass39_Sub5_Sub11_2214
					     .anIntArray1833).length
				   * Math.random());
		anInt2216 -= (int) ((double) (aClass39_Sub5_Sub11_2214
					      .anIntArray1831[anInt2211])
				    * Math.random());
	    }
	}
    }
    
    static {
	aClass3_2222 = aClass3_2227;
	aClass3_2230 = aClass3_2227;
	aClass3_2228 = Class39_Sub5_Sub9.createJstring("mod_icons");
	queuedStateValues = new int[2000];
	aClass3_2224
	    = Class39_Sub5_Sub9
		  .createJstring("auf einer freien Welt zu spielen)3");
	aClass3_2217 = Class39_Sub5_Sub9.createJstring("yellow:");
	aClass3_2218 = aClass3_2217;
    }
}
