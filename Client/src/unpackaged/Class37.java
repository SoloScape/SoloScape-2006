package unpackaged;

import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.utils.Cache;

/* Class37 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class37
{
    public static int anInt653;
    public static boolean aBoolean654 = false;
    public static Cache aClass7_655;
    public static int anInt656;
    public static JString aClass3_657 = Class39_Sub5_Sub9.createJstring("K");
    public static FileTable aClass9_658;
    public static JString aClass3_659 = aClass3_657;
    public static JString aClass3_660 = aClass3_657;
    public static JString aClass3_661;
    public static int anInt662;
    public static int anInt663;
    public static JSocket gameSocket;
    
    public static void method349(int i, JString class3, JString class3_0_,
				 JString class3_1_) {
	Class39_Sub5_Sub14.aClass3_1896 = class3;
	Class39_Sub5_Sub14.aClass3_1916 = class3_1_;
	Class39_Sub5_Sub14.aClass3_1899 = class3_0_;
    }
    
    public static void method350(int i) {
	gameSocket = null;
	aClass7_655 = null;
	aClass3_659 = null;
	aClass3_661 = null;
	aClass3_660 = null;
	aClass3_657 = null;
	aClass9_658 = null;
    }
    
    public static void method351(int i, int i_2_, int i_3_) {
	if (i != 1)
	    anInt663 = -95;
	if (Class39_Sub5_Sub4_Sub4.anInt2313 != 0 && i_3_ != -1) {
	    Class41.method891(87, 0, 1, false, Class36.fileLoader11, Class39_Sub5_Sub4_Sub4.anInt2313, i_3_);
	    JSocket.anInt313 = i_2_;
	}
    }
    
    public static boolean method352(byte i, int i_4_) {
	if (i_4_ >= 97 && i_4_ <= 122)
	    return true;
	if (i_4_ >= 65 && i_4_ <= 90)
	    return true;
	if (i_4_ >= 48 && i_4_ <= 57)
	    return true;
	return false;
    }
    
    public static Widget getWidget(int widgetHash) {
	GrandExchangeWidgets.load(widgetHash >>> 16);
	int child = widgetHash & 0xffff;
	int parent = widgetHash >> 16;
	if (parent < 0 || parent >= Class62_Sub1.widgets.length)
	    return null;
	Widget[] group = Class62_Sub1.widgets[parent];
	// A parent array can be allocated before all runtime/synthetic children
	// have been configured. Give loadWidget() a chance to expand it before
	// indexing a child such as Tutorial Island's synthetic group-214 child 8.
	if (group == null || child < 0 || child >= group.length || group[child] == null) {
	    boolean bool = JSocket.loadWidget(parent);
	    if (!bool)
		return null;
	    group = Class62_Sub1.widgets[parent];
	    if (group == null || child < 0 || child >= group.length)
		return null;
	}
	return group[child];
    }
    
    public static void method354(byte i) {
	if (ClientScript.anInt1692 > 0)
	    IsaacPrng.method1044();
	else {
	    Class39_Sub14.setState(40);
	    ObjectDefinition.aClass16_1935 = gameSocket;
	    gameSocket = null;
	}
    }
    
    static {
	aClass7_655 = new Cache(50);
	aClass3_661 = Class39_Sub5_Sub9.createJstring("@cr1@");
	anInt662 = 1;
    }
}
