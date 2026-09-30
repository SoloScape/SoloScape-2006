package unpackaged;

import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;

/* Class30 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class30
{
    public static JString aClass3_533 = Class39_Sub5_Sub9.createJstring("");
    public static int anInt534;
    public static JString aClass3_535;
    public static long aLong536;
    public static JString aClass3_537;
    public static JString aClass3_538;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_539;
    public static int[][] globalArrays;
    public static int anInt541;
    public static int anInt542 = 0;
    public static JString aClass3_543;
    public static int anInt544;
    public static JString aClass3_545;
    public static JString aClass3_546;
    public static JString aClass3_547;
    public static int anInt548;
    
    public static void method316(int i) {
	aClass39_Sub5_Sub10_Sub4_539 = null;
	aClass3_543 = null;
	aClass3_546 = null;
	aClass3_538 = null;
	aClass3_547 = null;
	aClass3_537 = null;
	aClass3_545 = null;
	aClass3_535 = null;
	globalArrays = null;
	aClass3_533 = null;
    }
    
    public static JString method317(int i, byte i_0_) {
	JString class3 = new JString();
	class3.length = 0;
	class3.bytes = new byte[i];
	return class3;
    }
    
    public static void method318(int i) {
	for (int i_1_ = -1; TraversalMap.anInt515 > i_1_; i_1_++) {
	    int i_2_;
	    if (i_1_ == -1)
		i_2_ = 2047;
	    else
		i_2_ = Class1.anIntArray40[i_1_];
	    Player class39_sub5_sub4_sub4_sub2
		= Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_2_];
	    if (class39_sub5_sub4_sub4_sub2 != null)
		ClientScript.method480((byte) 116,
					    class39_sub5_sub4_sub4_sub2, 1);
	}
    }
    
    public static int method319(int i, int i_3_, int i_4_) {
	long l = (long) (i_3_ + (i << 16));
	if (FileLoader.currentOdRequest == null  || FileLoader.currentOdRequest.hash != l)
	    return 0;
	return ((JMouseListener.odArchiveBuffer.offset * 99 / (-FileLoader.currentOdRequest.footerSize + JMouseListener.odArchiveBuffer.payload.length)) + 1);
    }
    
    static {
	aLong536 = 0L;
	anInt541 = 0;
	aClass3_543 = Class39_Sub5_Sub9.createJstring(" x ");
	anInt544 = 0;
	aClass3_538 = (Class39_Sub5_Sub9.createJstring
		       ("Please wait 5 minutes before trying again)3"));
	aClass3_537 = aClass3_538;
	aClass3_547
	    = Class39_Sub5_Sub9.createJstring("wishes to trade with you)3");
	globalArrays = new int[5][5000];
	aClass3_546
	    = Class39_Sub5_Sub9
		  .createJstring(" steht bereits auf Ihrer Freunde)2Liste(Q");
	anInt548 = 50;
	anInt534 = 0;
	aClass3_545 = Class39_Sub5_Sub9.createJstring("backbase2");
	aClass3_535 = aClass3_547;
    }
}
