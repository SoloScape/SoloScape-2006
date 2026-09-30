package unpackaged;

import jagex.utils.HashTable;
import jagex.world.actors.GroundItem;
import jagex.graphics.JImage;
import jagex.world.actors.Projectile;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.Buffer;
import jagex.utils.Cache;

/* Class39_Sub5_Sub6 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub5_Sub6 extends SubNode
{
    public static JString aClass3_1751;
    public int[] anIntArray1752;
    public int[] anIntArray1753 = new int[6];
    public boolean aBoolean1754;
    public static int[][][] anIntArrayArrayArray1755 = new int[4][13][13];
    public int[] anIntArray1756 = { -1, -1, -1, -1, -1 };
    public int[] anIntArray1757;
    public int anInt1758;
    public static int anInt1759;
    public static int anInt1760;
    public static JString aClass3_1761
	= Class39_Sub5_Sub9.createJstring("Prepared sound engine");
    public static Cache aClass7_1762;
    public static JString aClass3_1763;
    public static JString aClass3_1764;
    public static int queuedPriorityRequests;
    public static JString aClass3_1766;
    public static Cache aClass7_1767;
    public static byte[] aByteArray1768;
    public static JString aClass3_1769;
    public static JString aClass3_1770;
    public static JString aClass3_1771;
    public static int mode;
    public static int anInt1773;
    public static JString aClass3_1774;
    public static int anInt1775;
    
    public Model method576(int i) {
	Model[] class39_sub5_sub4_sub6s
	    = new Model[5];
	int i_0_ = 0;
	for (int i_1_ = 0; i_1_ < 5; i_1_++) {
	    if (anIntArray1756[i_1_] != -1)
		class39_sub5_sub4_sub6s[i_0_++]
		    = Model.getModel((Class39_Sub10
							.aClass9_1424),
						       anIntArray1756[i_1_],
						       0);
	}
	Model class39_sub5_sub4_sub6
	    = new Model(class39_sub5_sub4_sub6s, i_0_);
	for (int i_2_ = 0; i_2_ < 6; i_2_++) {
	    if (anIntArray1752[i_2_] == 0)
		break;
	    class39_sub5_sub4_sub6.method563(anIntArray1752[i_2_],
					     anIntArray1753[i_2_]);
	}
	return class39_sub5_sub4_sub6;
    }
    
    public boolean method577(int i) {
	if (anIntArray1757 == null)
	    return true;
	boolean bool = true;
	for (int i_3_ = i; i_3_ < anIntArray1757.length; i_3_++) {
	    if (!Class39_Sub10.aClass9_1424.isArchiveLoaded(anIntArray1757[i_3_], 0))
		bool = false;
	}
	return bool;
    }
    
    public static void method578(boolean bool) {
	aClass3_1761 = null;
	aClass7_1767 = null;
	aClass3_1769 = null;
	anIntArrayArrayArray1755 = null;
	aClass3_1770 = null;
	aClass3_1774 = null;
	aClass7_1762 = null;
	aClass3_1751 = null;
	aClass3_1764 = null;
	aClass3_1766 = null;
	aClass3_1763 = null;
	aByteArray1768 = null;
	aClass3_1771 = null;
    }
    
    public static JString method579(int i, int i_4_, boolean bool) {
	int i_5_ = -i_4_ + i;
	if (i_5_ < -9)
	    return Class39_Sub10.aClass3_1421;
	if (i_5_ < -6)
	    return JImage.aClass3_1579;
	if (i_5_ < -3)
	    return GroundItem.aClass3_2246;
	if (i_5_ < 0)
	    return Class20.aClass3_394;
	if (i_5_ > 9)
	    return Class41.aClass3_783;
	if (i_5_ > 6)
	    return Projectile.aClass3_2207;
	if (i_5_ > 3)
	    return JMouseListener.aClass3_794;
	if (i_5_ > 0)
	    return Class2.aClass3_45;
	return HashTable.aClass3_368;
    }
    
    public Model method580(int i) {
	if (anIntArray1757 == null)
	    return null;
	Model[] class39_sub5_sub4_sub6s
	    = new Model[anIntArray1757.length];
	for (int i_6_ = 0; i_6_ < anIntArray1757.length; i_6_++)
	    class39_sub5_sub4_sub6s[i_6_]
		= Model.getModel(Class39_Sub10.aClass9_1424,
						   anIntArray1757[i_6_], 0);
	Model class39_sub5_sub4_sub6;
	if (class39_sub5_sub4_sub6s.length != 1)
	    class39_sub5_sub4_sub6
		= new Model(class39_sub5_sub4_sub6s,
					     class39_sub5_sub4_sub6s.length);
	else
	    class39_sub5_sub4_sub6 = class39_sub5_sub4_sub6s[0];
	for (int i_7_ = 0; i_7_ < 6; i_7_++) {
	    if (anIntArray1752[i_7_] == 0)
		break;
	    class39_sub5_sub4_sub6.method563(anIntArray1752[i_7_],
					     anIntArray1753[i_7_]);
	}
	return class39_sub5_sub4_sub6;
    }
    
    public Class39_Sub5_Sub6() {
	anIntArray1752 = new int[6];
	aBoolean1754 = false;
	anInt1758 = -1;
    }
    
    public boolean method581(byte i) {
	boolean bool = true;
	for (int i_8_ = 0; i_8_ < 5; i_8_++) {
	    if (anIntArray1756[i_8_] != -1
		&& !Class39_Sub10.aClass9_1424
			.isArchiveLoaded(anIntArray1756[i_8_], 0))
		bool = false;
	}
	return bool;
    }
    
    public void method582(byte i, Buffer class39_sub6) {
	for (;;) {
	    int i_9_ = class39_sub6.getUbyte();
	    if (i_9_ == 0)
		break;
	    method583(i ^ 0x2930, i_9_, class39_sub6);
	}
	if (i != -9)
	    anIntArrayArrayArray1755 = null;
    }
    
    public void method583(int i, int i_10_, Buffer class39_sub6) {
	if (i_10_ != 1) {
	    if (i_10_ != 2) {
		if (i_10_ == 3)
		    aBoolean1754 = true;
		else if (i_10_ >= 40 && i_10_ < 50)
		    anIntArray1752[i_10_ - 40]
			= class39_sub6.getUword();
		else if (i_10_ >= 50 && i_10_ < 60)
		    anIntArray1753[i_10_ - 50] = class39_sub6.getUword();
		else if (i_10_ >= 60 && i_10_ < 70)
		    anIntArray1756[i_10_ - 60]
			= class39_sub6.getUword();
	    } else {
		int i_11_ = class39_sub6.getUbyte();
		anIntArray1757 = new int[i_11_];
		for (int i_12_ = 0; i_12_ < i_11_; i_12_++)
		    anIntArray1757[i_12_] = class39_sub6.getUword();
	    }
	} else
	    anInt1758 = class39_sub6.getUbyte();
	if (i != -10553)
	    method581((byte) 15);
    }
    
    static {
	aClass3_1751 = aClass3_1761;
	anInt1760 = -1;
	aClass7_1762 = new Cache(64);
	aClass3_1763 = Class39_Sub5_Sub9.createJstring("New User");
	queuedPriorityRequests = 0;
	aClass3_1766 = aClass3_1763;
	aClass3_1764 = Class39_Sub5_Sub9.createJstring(" has logged in)3");
	aClass3_1769 = aClass3_1764;
	aClass7_1767 = new Cache(30);
	aClass3_1771
	    = Class39_Sub5_Sub9.createJstring("Musik)2Engine vorbereitet)3");
	mode = 0;
	anInt1773 = 1;
	aClass3_1770 = Class39_Sub5_Sub9.createJstring("weiss:");
	aClass3_1774 = Class39_Sub5_Sub9.createJstring("Fps:");
	anInt1775 = 7759444;
    }
}
