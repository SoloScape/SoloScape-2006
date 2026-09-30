package unpackaged;

import jagex.graphics.AbstractImage;
import jagex.utils.JString;
import jagex.utils.Cache;

/* Class11 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class NameTable
{
    public static int[] anIntArray176;
    public static int anInt177;
    public static int height;
    public static int[] anIntArray179 = { 8, 11, 4, 6, 9, 7, 10 };
    public int[] anIntArray180;
    public static JString aClass3_181;
    public static AbstractImage aClass57_182;
    public static boolean aBoolean183;
    public static Cache aClass7_184;
    public static JString aClass3_185;
    public static int[][] anIntArrayArray186;
    public static int[] anIntArray187;
    public static JString aClass3_188;
    public static JString aClass3_189;
    public static JString aClass3_190;
    
    public static Class39_Sub5_Sub12 method182
	(int i, FileTable class9, FileTable class9_0_, int i_1_, boolean bool) {
	int[] is = class9.getChildrenEntries(i);
	boolean bool_2_ = true;
	for (int i_3_ = 0; i_3_ < is.length; i_3_++) {
	    byte[] is_4_ = class9.lookupFile(i, is[i_3_]);
	    if (is_4_ == null)
		bool_2_ = false;
	    else {
		int i_5_ = (is_4_[0] & 0xff) << 8 | is_4_[1] & 0xff;
		byte[] is_6_;
		if (bool)
		    is_6_ = class9_0_.lookupFile(0, i_5_);
		else
		    is_6_ = class9_0_.lookupFile(i_5_, 0);
		if (is_6_ == null)
		    bool_2_ = false;
	    }
	}
	if (!bool_2_)
	    return null;
	try {
	    return new Class39_Sub5_Sub12(class9, class9_0_, i, bool);
	} catch (Exception exception) {
	    return null;
	}
    }
    
    public static void method183(int i) {
	anIntArray176 = null;
	anIntArray179 = null;
	aClass3_188 = null;
	aClass7_184 = null;
	aClass3_185 = null;
	aClass3_181 = null;
	aClass3_190 = null;
	anIntArrayArray186 = null;
	aClass57_182 = null;
	aClass3_189 = null;
	anIntArray187 = null;
    }
    
    public NameTable(int[] is) {
	int i;
	for (i = 1; i <= (is.length >> 1) + is.length; i <<= 1) {
	    /* empty */
	}
	anIntArray180 = new int[i + i];
	for (int i_7_ = 0; i_7_ < i + i; i_7_++)
	    anIntArray180[i_7_] = -1;
	for (int i_8_ = 0; is.length > i_8_; i_8_++) {
	    int i_9_;
	    for (i_9_ = i - 1 & is[i_8_]; anIntArray180[i_9_ + i_9_ + 1] != -1;
		 i_9_ = i - 1 & i_9_ + 1) {
		/* empty */
	    }
	    anIntArray180[i_9_ + i_9_] = is[i_8_];
	    anIntArray180[i_9_ + 1 + i_9_] = i_8_;
	}
    }
    
    public int method184(boolean bool, int i) {
	int i_10_ = anIntArray180.length - 2;
	int i_11_ = i << 1 & i_10_;
	for (;;) {
	    int i_12_ = anIntArray180[i_11_];
	    if (i == i_12_)
		return anIntArray180[i_11_ + 1];
	    if (i_12_ == -1)
		return -1;
	    i_11_ = i_10_ & i_11_ + 2;
	}
    }
    
    static {
	anIntArray176 = new int[500];
	anInt177 = 0;
	aBoolean183 = false;
	anIntArrayArray186 = new int[104][104];
	aClass3_185 = Class39_Sub5_Sub9.createJstring("::noclip");
	aClass3_181
	    = Class39_Sub5_Sub9.createJstring("Ladevorgang )2 bitte warten Sie)3");
	aClass7_184 = new Cache(64);
	aClass3_188
	    = Class39_Sub5_Sub9.createJstring("Lade Schrifts-=tze )2 ");
	aClass3_189
	    = Class39_Sub5_Sub9.createJstring("Loading )2 please wait)3");
	aClass3_190 = aClass3_189;
    }
}
