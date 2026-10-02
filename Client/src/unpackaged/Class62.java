package unpackaged;

import jagex.utils.Huffmans;
import jagex.world.actors.GroundItem;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.world.actors.Projectile;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Npc;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;

/* Class62 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public abstract class Class62
{
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array1106;
    public static int anInt1107;
    public static JString aClass3_1108;
    public static JString aClass3_1109;
    public static JString aClass3_1110;
    public static JString aClass3_1111
	= Class39_Sub5_Sub9.createJstring("The server is being updated)3");
    public static boolean aBoolean1112;
    public static JString aClass3_1113;
    public static JString aClass3_1114
	= Class39_Sub5_Sub9.createJstring("Bitte entfernen Sie ");
    public static JString aClass3_1115;
    public static JString aClass3_1116;
    
    public static void method1045(boolean bool) {
	Class47.method949(-5794);
	FrameBuffer.method840();
	Class66.method1103(256);
	Class43.method905(3090);
	Huffmans.method887((byte) 112);
	Class14.method208(true);
	Class67.method1107(-46);
	Class34.method341(bool);
	Class63.method1085(84);
	Class39_Sub5_Sub9.method604(false);
	Buffer.method787(3);
	ScriptState.method277(bool);
	((Class55) Class39_Sub5_Sub10_Sub2.anInterface2_2458).method1002(94);
	Class45.clientScriptCache.method134(27392);
	Buffer.fileLoader0.method145(0);
	Class15.fileLoader1.method145(0);
	Node.fileLoader3.method145(0);
	Node.fileLoader4.method145(0);
	JSocket.fileLoader5.method145(0);
	Projectile.fileLoader6.method145(0);
	Class45.fileLoader7.method145(0);
	TraversalMap.fileLoader8.method145(0);
	Widget.fileLoader9.method145(0);
	Class66.fileLoader10.method145(0);
	Class36.fileLoader11.method145(0);
	Class33.fileLoader12.method145(0);
    }
    
    public static void method1046(byte i) {
	if (i != 22)
	    aClass3_1109 = null;
	if (NpcDefinition.aClass39_Sub5_Sub17_1864 != null) {
	    Widget class39_sub5_sub17
		= NpcDefinition.aClass39_Sub5_Sub17_1864;
	    Widget class39_sub5_sub17_0_
		= Class44.method914(class39_sub5_sub17, (byte) -123);
	    if (class39_sub5_sub17_0_ == null)
		NpcDefinition.aClass39_Sub5_Sub17_1864 = null;
	    else {
		ClientApplet.anInt8++;
		int[] is = Class65.method1094(-6622, class39_sub5_sub17_0_);
		int[] is_1_
		    = Class65.method1094(i ^ ~0x19cb, class39_sub5_sub17);
		int i_2_ = IsaacPrng.anInt1091 - ItemDefinition.anInt1684;
		int i_3_ = -FileLoader.anInt1303 + Class33.anInt599;
		if (class39_sub5_sub17.anInt2056 >= i_2_
		    && i_2_ >= -class39_sub5_sub17.anInt2056
		    && class39_sub5_sub17.anInt2056 >= i_3_
		    && i_3_ >= -class39_sub5_sub17.anInt2056
		    && !Class37.aBoolean654) {
		    i_3_ = 0;
		    i_2_ = 0;
		} else if (class39_sub5_sub17.anInt2008 < ClientApplet.anInt8
			   || Class37.aBoolean654)
		    Class37.aBoolean654 = true;
		else {
		    i_3_ = 0;
		    i_2_ = 0;
		}
		int i_4_ = i_2_ + (is_1_[0] - is[0]);
		int i_5_ = i_3_ + (is_1_[1] - is[1]);
		if (i_5_ < 0)
		    i_5_ = 0;
		if (i_4_ < 0)
		    i_4_ = 0;
		if (class39_sub5_sub17_0_.quadHeight
		    < class39_sub5_sub17.quadHeight + i_5_)
		    i_5_ = (-class39_sub5_sub17.quadHeight
			    + class39_sub5_sub17_0_.quadHeight);
		if (class39_sub5_sub17_0_.quadWidth
		    < i_4_ + class39_sub5_sub17.quadWidth)
		    i_4_ = (-class39_sub5_sub17.quadWidth
			    + class39_sub5_sub17_0_.quadWidth);
		if ((NpcDefinition.aClass39_Sub5_Sub17_1864
		     .anObjectArray2018) != null
		    && Class37.aBoolean654)
		    Class39_Sub5_Sub4_Sub4.executeClientScript(0,
						     (class39_sub5_sub17
						      .anObjectArray2018),
						     null, i - 63, i_4_,
						     class39_sub5_sub17, i_5_);
		if (Class30.anInt541 == 0) {
		    if (Class37.aBoolean654) {
			int i_6_ = -is[1] + Class33.anInt599;
			int i_7_ = -is[0] + IsaacPrng.anInt1091;
			if (i_6_ < 0)
			    i_6_ = 0;
			if (i_7_ < 0)
			    i_7_ = 0;
			if (i_7_ > class39_sub5_sub17_0_.quadWidth - 1)
			    i_7_ = class39_sub5_sub17_0_.quadWidth - 1;
			if (i_6_ > class39_sub5_sub17_0_.quadHeight - 1)
			    i_6_ = class39_sub5_sub17_0_.quadHeight - 1;
			Widget class39_sub5_sub17_8_
			    = (GroundItem.method504
			       (i_7_, i_6_, i - 22, class39_sub5_sub17,
				class39_sub5_sub17_0_));
			if ((NpcDefinition.aClass39_Sub5_Sub17_1864
			     .anObjectArray2088)
			    != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2088,
				 class39_sub5_sub17_8_, 122, i_4_,
				 class39_sub5_sub17, i_5_);
			if (class39_sub5_sub17_8_ != null
			    && (class39_sub5_sub17.method767((byte) 120)
				!= null)) {
			    FrameBuffer.outgoingGameBuffer
				.putFrame(46);
			    FrameBuffer.outgoingGameBuffer.method796
				(-21994, class39_sub5_sub17_8_.anInt2102);
			    FrameBuffer.outgoingGameBuffer.method796
				(-21994, class39_sub5_sub17.anInt2102);
			    FrameBuffer.outgoingGameBuffer.putDwordLe
				(class39_sub5_sub17_8_.anInt2084);
			    FrameBuffer.outgoingGameBuffer.method827
				(class39_sub5_sub17.anInt2084, -334352184);
			}
		    } else {
			if ((NpcDefinition.aClass39_Sub5_Sub17_1864
			     .anObjectArray2097)
			    != null)
			    Class39_Sub5_Sub4_Sub4.executeClientScript
				(0, class39_sub5_sub17.anObjectArray2097, null,
				 126, i_4_, class39_sub5_sub17, i_5_);
			if ((Class45.anInt868 != 1
			     && !Class33.method327((Class39_Sub5_Sub11
						    .anInt1841) - 1,
						   (byte) -128))
			    || Class39_Sub5_Sub11.anInt1841 <= 2) {
			    if (Class39_Sub5_Sub11.anInt1841 > 0)
				ScriptState.method278((Class39_Sub5_Sub11.anInt1841
						   - 1),
						  1);
			} else
			    Class39_Sub5_Sub9.method607(701);
		    }
		    NpcDefinition.aClass39_Sub5_Sub17_1864 = null;
		}
	    }
	}
    }
    
    public abstract void destroy();
    
    public abstract void method1048(boolean bool);
    
    public static void method1049(byte i) {
	for (int i_9_ = -1; TraversalMap.anInt515 > i_9_; i_9_++) {
	    int i_10_;
	    if (i_9_ == -1)
		i_10_ = 2047;
	    else
		i_10_ = Class1.anIntArray40[i_9_];
	    Player class39_sub5_sub4_sub4_sub2
		= Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_10_];
	    if (class39_sub5_sub4_sub4_sub2 != null
		&& class39_sub5_sub4_sub4_sub2.anInt2259 > 0) {
		class39_sub5_sub4_sub4_sub2.anInt2259--;
		if (class39_sub5_sub4_sub4_sub2.anInt2259 == 0)
		    class39_sub5_sub4_sub4_sub2.aClass3_2295 = null;
	    }
	}
	for (int i_11_ = 0; ArchiveWorker.anInt1210 > i_11_; i_11_++) {
	    int i_12_ = Class39_Sub5_Sub4.anIntArray1734[i_11_];
	    Npc class39_sub5_sub4_sub4_sub1
		= (GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241
		   [i_12_]);
	    if (class39_sub5_sub4_sub4_sub1 != null
		&& class39_sub5_sub4_sub4_sub1.anInt2259 > 0) {
		class39_sub5_sub4_sub4_sub1.anInt2259--;
		if (class39_sub5_sub4_sub4_sub1.anInt2259 == 0)
		    class39_sub5_sub4_sub4_sub1.aClass3_2295 = null;
	    }
	}
    }
    
    public static void method1050(int i, int i_13_, int i_14_, int i_15_,
				  int i_16_, int i_17_, int i_18_) {
	if (JSocket.loadWidget(i_13_))
	    IsaacPrng.method1038(-1, i_14_, 1, 0,
			       (Class62_Sub1.widgets
				[i_13_]),
			       i, i_18_, 0, i_17_, i_15_);
    }
    
    public boolean isPlaying() {
        return false;
    }

    public abstract void method1051(int i, byte[] is, byte i_19_,
				    boolean bool);
    
    public static Class39_Sub5_Sub14 method1052(int i, byte i_20_) {
	Class39_Sub5_Sub14 class39_sub5_sub14
	    = ((Class39_Sub5_Sub14)
	       Class39_Sub5_Sub6.aClass7_1762.get((long) i));
	if (class39_sub5_sub14 != null)
	    return class39_sub5_sub14;
	byte[] is = ItemDefinition.aClass9_1680.lookupFile(4, i);
	class39_sub5_sub14 = new Class39_Sub5_Sub14();
	if (is != null)
	    class39_sub5_sub14.method730(-1, new Buffer(is), i);
	class39_sub5_sub14.method728((byte) -125);
	Class39_Sub5_Sub6.aClass7_1762.put(class39_sub5_sub14, (long) i,
						 (byte) -55);
	return class39_sub5_sub14;
    }
    
    public abstract void method1053(int i, byte i_21_);
    
    public abstract void method1054(int i);
    
    public abstract void method1055(int i, int i_22_, int i_23_);
    
    public static Class39_Sub5_Sub16 method1056(int i, int i_24_) {
	Class39_Sub5_Sub16 class39_sub5_sub16
	    = ((Class39_Sub5_Sub16)
	       Class39_Sub5_Sub12.aClass7_1853.get((long) i));
	if (class39_sub5_sub16 != null)
	    return class39_sub5_sub16;
	byte[] is = Class39_Sub5_Sub5.aClass9_1748.lookupFile(16, i);
	class39_sub5_sub16 = new Class39_Sub5_Sub16();
	if (is != null)
	    class39_sub5_sub16.decode(new Buffer(is));
	Class39_Sub5_Sub12.aClass7_1853.put(class39_sub5_sub16, (long) i,
						  (byte) 120);
	return class39_sub5_sub16;
    }
    
    public static int getPerlinNoise(int i, int i_25_, int i_26_) {
	int i_27_
	    = (Class32.method323(4, 113, i_25_ + 45365, i + 91923)
	       - (-(Class32.method323(2, 54, i_25_ + 10294, i + 37821) - 128
		    >> 1)
		  + 128)
	       + (Class32.method323(i_26_, i_26_ + 85, i_25_, i) - 128 >> 2));
	i_27_ = (int) ((double) i_27_ * 0.3) + 35;
	if (i_27_ < 10)
	    i_27_ = 10;
	else if (i_27_ > 60)
	    i_27_ = 60;
	return i_27_;
    }
    
    public static void method1058(int i) {
	aClass3_1110 = null;
	aClass3_1114 = null;
	aClass3_1109 = null;
	aClass3_1108 = null;
	aClass3_1113 = null;
	aClass3_1111 = null;
	aClass3_1115 = null;
	aClass3_1116 = null;
	aClass39_Sub5_Sub10_Sub3Array1106 = null;
    }
    
    static {
	aClass3_1108 = Class39_Sub5_Sub9.createJstring("Handel akzeptieren");
	aClass3_1115
	    = Class39_Sub5_Sub9.createJstring("Starting game engine)3)3)3");
	aClass3_1113 = aClass3_1115;
	aClass3_1110
	    = Class39_Sub5_Sub9.createJstring("RuneScape has been updated(Q");
	aClass3_1116 = aClass3_1111;
	anInt1107 = 0;
	aBoolean1112 = false;
	aClass3_1109 = aClass3_1110;
    }
}
