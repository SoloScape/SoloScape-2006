package unpackaged;

/* Class39_Sub11 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.Node;
import jagex.utils.JString;
import java.awt.Image;

public class Class39_Sub11 extends Node
{
    public int anInt1449;
    public static JString aClass3_1450;
    public int anInt1451;
    public static JString aClass3_1452
	= Class39_Sub5_Sub9.createJstring("Please remove ");
    public static JString aClass3_1453;
    public static JString aClass3_1454;
    public static JString aClass3_1455;
    public int anInt1456 = -1;
    public int anInt1457;
    public int anInt1458;
    public int anInt1459;
    public static int[] anIntArray1460;
    public static int scriptStatePointer;
    public static JString[] aClass3Array1462;
    public static JString aClass3_1463;
    public static Image anImage1464;
    public static JString aClass3_1465;
    public int anInt1466;
    public static JString aClass3_1467;
    public static JString aClass3_1468;
    public int anInt1469;
    public static int anInt1470;
    public int anInt1471;
    public int anInt1472;
    public static int anInt1473;
    public int anInt1474;
    public static JString aClass3_1475;
    public int anInt1476 = 0;
    public static JString aClass3_1477;
    public static int anInt1478;
    public static JString aClass3_1479;
    public static JString aClass3_1480;
    public static JString aClass3_1481;
    
    public static void method868(int i) {
	aClass3_1467 = null;
	aClass3Array1462 = null;
	aClass3_1454 = null;
	aClass3_1468 = null;
	aClass3_1455 = null;
	aClass3_1475 = null;
	aClass3_1452 = null;
	aClass3_1480 = null;
	anIntArray1460 = null;
	anImage1464 = null;
	aClass3_1450 = null;
	aClass3_1479 = null;
	aClass3_1463 = null;
	aClass3_1465 = null;
	aClass3_1453 = null;
	aClass3_1481 = null;
	aClass3_1477 = null;
    }
    
    public static int method869(int i, int i_0_, int i_1_) {
	Class39_Sub13 class39_sub13
	    = ((Class39_Sub13)
	       Class14.aClass19_213.fetch((long) i_1_));
	if (class39_sub13 == null)
	    return -1;
	if (i_0_ < 0 || class39_sub13.anIntArray1505.length <= i_0_)
	    return -1;
	return class39_sub13.anIntArray1505[i_0_];
    }
    
    static {
	aClass3_1450 = Class39_Sub5_Sub9.createJstring(" million");
	aClass3_1455 = Class39_Sub5_Sub9.createJstring("invback");
	aClass3_1453 = Class39_Sub5_Sub9.createJstring("Loaded config");
	aClass3_1467 = aClass3_1452;
	aClass3_1454 = aClass3_1452;
	aClass3Array1462 = new JString[100];
	aClass3_1463 = Class39_Sub5_Sub9.createJstring("Welt");
	aClass3_1468 = aClass3_1450;
	scriptStatePointer = 0;
	aClass3_1475 = aClass3_1453;
	aClass3_1477 = Class39_Sub5_Sub9.createJstring("World");
	aClass3_1465 = aClass3_1477;
	aClass3_1480 = Class39_Sub5_Sub9.createJstring("headicons_pk");
	aClass3_1479 = Class39_Sub5_Sub9.createJstring("hitmarks");
	anInt1478 = -1;
	aClass3_1481 = Class39_Sub5_Sub9.createJstring("logo");
    }
}
