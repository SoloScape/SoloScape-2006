package unpackaged;

import jagex.world.actors.GroundItem;
import jagex.graphics.AbstractImage;
import jagex.utils.JString;
import jagex.utils.Queue;

/* Class44 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class44
{
    public int anInt831;
    public int anInt832;
    public static boolean aBoolean833 = false;
    public int anInt834;
    public static JString aClass3_835;
    public static Class38 aClass38_836;
    public static boolean[] aBooleanArray837 = new boolean[5];
    public static JString aClass3_838
	= Class39_Sub5_Sub9.createJstring("Classic");
    public static JString aClass3_839;
    public int anInt840;
    public static Widget aClass39_Sub5_Sub17_841;
    public int anInt842 = 0;
    public static JString aClass3_843
	= Class39_Sub5_Sub9.createJstring("Add friend");
    public int anInt844 = 0;
    public int anInt845;
    public Class39_Sub5_Sub4 aClass39_Sub5_Sub4_846;
    public static JString aClass3_847;
    
    public static JString method911(int i, byte i_0_) {
	if (i < 100000)
	    return AbstractImage.method1007((byte) 71, i);
	if (i < 10000000)
	    return (Class39_Sub5_Sub11.method708
		    (new JString[] { AbstractImage.method1007((byte) 71, i / 1000),
				    Class37.aClass3_659 }));
	return Class39_Sub5_Sub11.method708((new JString[]
					     { AbstractImage.method1007((byte) 71,
								  i / 1000000),
					       (ClientScript
						.aClass3_1698) }));
    }
    
    public static void method912(int i) {
	if (i == -19093 && Class55.aClass62_1251 != null) {
	    if (GroundItem.anInt2239 < 0) {
		if (ArchiveRequest.anInt1415 > 0) {
		    ArchiveRequest.anInt1415--;
		    if (ArchiveRequest.anInt1415 == 0) {
			if (Class39_Sub5_Sub6.aByteArray1768 == null)
			    Class55.aClass62_1251.method1053(256, (byte) 53);
			else {
			    Class55.aClass62_1251.method1053
				(Class39_Sub5_Sub18.anInt2120, (byte) 124);
			    GroundItem.anInt2239
				= Class39_Sub5_Sub18.anInt2120;
			    Class55.aClass62_1251.method1051
				(Class39_Sub5_Sub18.anInt2120,
				 Class39_Sub5_Sub6.aByteArray1768, (byte) 126,
				 JKeyListener.aBoolean628);
			    Class39_Sub5_Sub6.aByteArray1768 = null;
			}
			Class65.anInt1141 = 0;
		    }
		}
	    } else if (ArchiveRequest.anInt1415 > 0) {
		Class65.anInt1141 += Queue.anInt987;
		Class55.aClass62_1251.method1055(Class65.anInt1141,
						 (GroundItem
						  .anInt2239),
						 i ^ ~0x4a94);
		ArchiveRequest.anInt1415--;
		if (ArchiveRequest.anInt1415 == 0) {
		    Class55.aClass62_1251.method1048(false);
		    GroundItem.anInt2239 = -1;
		    ArchiveRequest.anInt1415 = 20;
		}
	    }
	    Class55.aClass62_1251.method1054(0);
	}
    }
    
    public static int getVarbitValue(int id) {
	Varbit varbit = Class25.getVarbit(id);
	int i_2_ = varbit.anInt1793;
	int i_3_ = varbit.anInt1794;
	int mask = ClientScript.bitMasks[i_3_ - i_2_];
	int settingId = varbit.settingId;
	return Class66.stateValues[settingId] >> i_2_ & mask;
    }
    
    public static Widget method914
	(Widget class39_sub5_sub17, byte i) {
	Widget class39_sub5_sub17_6_
	    = class39_sub5_sub17.method767((byte) 94);
	if (class39_sub5_sub17_6_ == null)
	    class39_sub5_sub17_6_
		= class39_sub5_sub17.aClass39_Sub5_Sub17_2040;
	return class39_sub5_sub17_6_;
    }
    
    public static void method915(boolean bool) {
	aClass3_843 = null;
	aClass3_847 = null;
	aBooleanArray837 = null;
	aClass38_836 = null;
	aClass3_838 = null;
	aClass3_835 = null;
	aClass3_839 = null;
	aClass39_Sub5_Sub17_841 = null;
    }
    
    static {
	aClass3_839
	    = Class39_Sub5_Sub9.createJstring("Regeln versto-8en hat)3");
	aClass3_835 = aClass3_843;
	aClass3_847 = aClass3_838;
    }
}
