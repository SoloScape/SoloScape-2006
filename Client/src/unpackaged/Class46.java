package unpackaged;

/* Class46 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.JImage;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import jagex.io.Buffer;
import java.awt.Graphics;

public abstract class Class46
{
    public static JString aClass3_872
	= Class39_Sub5_Sub9.createJstring("Login");
    public static JString aClass3_873;
    public static JString aClass3_874;
    public static JString aClass3_875;
    public static JString aClass3_876;
    public static JString aClass3_877;
    public static JString aClass3_878
	= (Class39_Sub5_Sub9.createJstring
	   ("Ihre Freunde)2Liste ist voll(Q Mitglieder k-Onnen 200 Freunde hinzuf-Ugen)1 freie Spieler nur 100)3"));
    public static int anInt879 = 0;
    public static JString aClass3_880;
    public static JString aClass3_881;
    public static int anInt882;
    public static int[] anIntArray883;
    public static int anInt884 = 0;
    public static JString aClass3_885;
    public static JString aClass3_886;
    public static int anInt887;
    public static JString aClass3_888;
    
    public static void method932(int i) {
	aClass3_886 = null;
	aClass3_875 = null;
	aClass3_885 = null;
	aClass3_872 = null;
	aClass3_874 = null;
	aClass3_888 = null;
	aClass3_876 = null;
	anIntArray883 = null;
	aClass3_881 = null;
	aClass3_873 = null;
	aClass3_880 = null;
	aClass3_877 = null;
	aClass3_878 = null;
    }
    
    public static void method933(TraversalMap[] class27s, int i, byte[] is,
				 byte i_0_, int i_1_, int i_2_, int i_3_) {
	for (int i_4_ = 0; i_4_ < 4; i_4_++) {
	    for (int i_5_ = 0; i_5_ < 64; i_5_++) {
		for (int i_6_ = 0; i_6_ < 64; i_6_++) {
		    if (i_1_ + i_5_ > 0 && i_5_ + i_1_ < 103 && i_6_ + i > 0
			&& i_6_ + i < 103)
			class27s[i_4_].adjancency[i_1_ + i_5_]
			    [i + i_6_]
			    = Class34.method342((class27s[i_4_]
						 .adjancency
						 [i_1_ + i_5_][i + i_6_]),
						-16777217);
		}
	    }
	}
	Buffer buffer = new Buffer(is);
	for (int i_7_ = 0; i_7_ < 4; i_7_++) {
	    for (int i_8_ = 0; i_8_ < 64; i_8_++) {
		for (int i_9_ = 0; i_9_ < 64; i_9_++)
		    Deque.loadMapTile(buffer, 0, i_3_, i_9_ + i, i_2_, i_8_ + i_1_, i_7_);
	    }
	}
    }
    
    public abstract void method934(int i);
    
    public static void method935(int i, int i_10_) {
	if (Class47.method946((byte) -62)) {
	    if (!Class39_Sub5_Sub5.aBoolean1749)
		JImage.method1014(i, (byte) 99);
	    else
		Class65.anInt1143 = i;
	}
    }
    
    public static void method936(byte i) {
	for (int i_11_ = 0; JImage.anInt1586 > i_11_; i_11_++) {
	    int i_12_ = ArchiveRequest.anIntArray1400[i_11_];
	    Player class39_sub5_sub4_sub4_sub2
		= Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_12_];
	    int i_13_ = Class39_Sub5_Sub11.gameBuffer
			    .getUbyte();
	    if ((i_13_ & 0x80) != 0)
		i_13_ += Class39_Sub5_Sub11.gameBuffer
			     .getUbyte() << 8;
	    ClientApplet.method25(class39_sub5_sub4_sub4_sub2, (byte) 60, i_12_,
				 i_13_);
	}
    }
    
    public static void method937(int i, FileTable class9) {
	Class39_Sub5_Sub5.aClass9_1748 = class9;
	ObjectDefinition.anInt1923
	    = Class39_Sub5_Sub5.aClass9_1748.getAmountChildren(16);
    }
    
    public abstract void method938(byte i);
    
    public abstract int method939(int i, int i_14_, int i_15_);
    
    public static void method940(byte i) {
	try {
	    Graphics graphics = Class41.aCanvas778.getGraphics();
	    Class23.aClass57_435.draw(graphics, 4, 4);
	} catch (Exception exception) {
	    Class41.aCanvas778.repaint();
	}
    }
    
    public static void method941(byte i) {
	ArchiveRequest.method857(0, false, null, false);
    }
    
    static {
	aClass3_875 = aClass3_872;
	anInt882 = 0;
	aClass3_885 = Class39_Sub5_Sub9.createJstring("Continue");
	aClass3_874 = aClass3_885;
	aClass3_886 = Class39_Sub5_Sub9.createJstring("Off");
	aClass3_880 = Class39_Sub5_Sub9.createJstring("shake:");
	anInt887 = 0;
	aClass3_873 = Class39_Sub5_Sub9.createJstring("Regelversto-8 melden");
	aClass3_881 = aClass3_886;
	anIntArray883 = new int[128];
	aClass3_877 = aClass3_880;
	aClass3_888 = Class39_Sub5_Sub9.createJstring("Existing user");
	aClass3_876 = aClass3_888;
    }
}
