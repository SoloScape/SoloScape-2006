package unpackaged;

import jagex.world.actors.GroundItem;
import jagex.graphics.JImage;
import jagex.world.actors.Projectile;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Cache;

/* Class39_Sub5_Sub3 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class OndemandRequest extends SubNode
{
    public static JString aClass3_1714;
    public byte footerSize;
    public static JString aClass3_1716
	= (Class39_Sub5_Sub9.createJstring
	   ("Please check your message)2centre for details)3"));
    public static long wroteGatewayTime;
    public static boolean aBoolean1718;
    public FileLoader fileLoader;
    public static int anInt1720;
    public static JString aClass3_1721;
    public static JString aClass3_1722
	= Class39_Sub5_Sub9.createJstring("rot:");
    public static JString aClass3_1723;
    public static int anInt1724;
    public int checksum;
    
    public static void method485(int i) {
	aClass3_1723 = null;
	aClass3_1714 = null;
	aClass3_1722 = null;
	aClass3_1721 = null;
	aClass3_1716 = null;
    }
    
    public static void method486(int i, int i_0_, byte i_1_, int i_2_,
				 int i_3_, int i_4_) {
	if (i_4_ != Class62_Sub2.anInt1597 || i_0_ != Class65.anInt1147
	    || i != JImage.anInt1573 && Class45.aBoolean867) {
	    Class62_Sub2.anInt1597 = i_4_;
	    Class65.anInt1147 = i_0_;
	    JImage.anInt1573 = i;
	    if (!Class45.aBoolean867)
		JImage.anInt1573 = 0;
	    Class39_Sub14.setState(25);
	    Class26.method294(false, NameTable.aClass3_190, null, 118);
	    int i_5_ = Class65.anInt1145;
	    Class65.anInt1145 = i_4_ * 8 - 48;
	    int i_6_ = JKeyListener.anInt618;
	    int i_7_ = -i_5_ + Class65.anInt1145;
	    i_5_ = Class65.anInt1145;
	    JKeyListener.anInt618 = i_0_ * 8 - 48;
	    int i_8_ = -i_6_ + JKeyListener.anInt618;
	    i_6_ = JKeyListener.anInt618;
	    for (int i_9_ = 0; i_9_ < 32768; i_9_++) {
		Npc class39_sub5_sub4_sub4_sub1
		    = (GroundItem
		       .aClass39_Sub5_Sub4_Sub4_Sub1Array2241[i_9_]);
		if (class39_sub5_sub4_sub4_sub1 != null) {
		    for (int i_10_ = 0; i_10_ < 10; i_10_++) {
			class39_sub5_sub4_sub4_sub1.anIntArray2314[i_10_]
			    -= i_7_;
			class39_sub5_sub4_sub4_sub1.anIntArray2255[i_10_]
			    -= i_8_;
		    }
		    class39_sub5_sub4_sub4_sub1.anInt2275 -= i_8_ * 128;
		    class39_sub5_sub4_sub4_sub1.anInt2301 -= i_7_ * 128;
		}
	    }
	    for (int i_11_ = 0; i_11_ < 2048; i_11_++) {
		Player class39_sub5_sub4_sub4_sub2
		    = Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_11_];
		if (class39_sub5_sub4_sub4_sub2 != null) {
		    for (int i_12_ = 0; i_12_ < 10; i_12_++) {
			class39_sub5_sub4_sub4_sub2.anIntArray2314[i_12_]
			    -= i_7_;
			class39_sub5_sub4_sub4_sub2.anIntArray2255[i_12_]
			    -= i_8_;
		    }
		    class39_sub5_sub4_sub4_sub2.anInt2275 -= i_8_ * 128;
		    class39_sub5_sub4_sub4_sub2.anInt2301 -= i_7_ * 128;
		}
	    }
	    NameTable.height = i;
	    int i_13_ = 0;
	    int i_14_ = 104;
	    int i_15_ = 1;
	    Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109
		.method508(i_3_, false, (byte) -120, i_2_);
	    int i_16_ = 0;
	    if (i_7_ < 0) {
		i_13_ = 103;
		i_14_ = -1;
		i_15_ = -1;
	    }
	    int i_17_ = 104;
	    int i_18_ = 1;
	    if (i_8_ < 0) {
		i_17_ = -1;
		i_18_ = -1;
		i_16_ = 103;
	    }
	    for (int i_19_ = i_13_; i_19_ != i_14_; i_19_ += i_15_) {
		for (int i_20_ = i_16_; i_17_ != i_20_; i_20_ += i_18_) {
		    int i_21_ = i_7_ + i_19_;
		    int i_22_ = i_20_ + i_8_;
		    for (int i_23_ = 0; i_23_ < 4; i_23_++) {
			if (i_21_ >= 0 && i_22_ >= 0 && i_21_ < 104
			    && i_22_ < 104)
			    Class20.groundItems[i_23_]
				[i_19_][i_20_]
				= (Class20.groundItems[i_23_]
				   [i_21_][i_22_]);
			else
			    Class20.groundItems[i_23_]
				[i_19_][i_20_]
				= null;
		    }
		}
	    }
	    for (Class39_Sub11 class39_sub11
		     = (Class39_Sub11) Class15.aClass49_278.getFirst();
		 class39_sub11 != null;
		 class39_sub11 = (Class39_Sub11) Class15.aClass49_278
						     .getNext()) {
		class39_sub11.anInt1474 -= i_8_;
		class39_sub11.anInt1466 -= i_7_;
		if (class39_sub11.anInt1466 < 0 || class39_sub11.anInt1474 < 0
		    || class39_sub11.anInt1466 >= 104
		    || class39_sub11.anInt1474 >= 104)
		    class39_sub11.unlinkDeque();
	    }
	    Projectile.anInt2180 = 0;
	    if (Class30.anInt544 != 0) {
		ArchiveRequest.anInt1407 -= i_8_;
		Class30.anInt544 -= i_7_;
	    }
	    ArchiveRequest.anInt1411 = -1;
	    TraversalMap.aBoolean504 = false;
	    RuntimeException_Sub1.aClass49_1217.removeAll(-3);
	    Class23.projectiles.removeAll(-3);
	}
    }
    
    static {
	aClass3_1721 = aClass3_1716;
	aClass3_1714 = null;
	aClass3_1723 = Class39_Sub5_Sub9.createJstring(":");
	anInt1720 = 0;
	aBoolean1718 = false;
    }
}
