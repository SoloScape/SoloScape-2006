package unpackaged;

import jagex.graphics.AbstractImage;
import jagex.world.actors.StillGraphic;
import jagex.utils.JString;
import jagex.utils.Deque;
import jagex.io.Buffer;

/* Class63 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class63
{
    public static int anInt1117;
    public static JString aClass3_1118;
    public static JString aClass3_1119;
    public static boolean aBoolean1120 = false;
    public static JString aClass3_1121;
    public static AbstractImage aClass57_1122;
    public static JString aClass3_1123;
    public static JString aClass3_1124;
    public static JString aClass3_1125;
    public static int anInt1126;
    public static JString aClass3_1127
	= Class39_Sub5_Sub9
	      .createJstring("Please wait )2 attempting to reestablish");
    public static int anInt1128;
    public static JString aClass3_1129;
    public static JString aClass3_1130;
    public static JString aClass3_1131;
    
    public static void method1085(int i) {
	Widget.varbitCache.method134(27392);
    }
    
    public static void method1086(byte i, int i_0_) {
	Class39_Sub13 class39_sub13
	    = ((Class39_Sub13)
	       Class14.aClass19_213.fetch((long) i_0_));
	if (class39_sub13 != null)
	    class39_sub13.unlinkDeque();
    }
    
    public static void method1087(int i, int i_1_) {
	Varbit.method593(0, 1, false, null, i_1_);
    }
    
    public static void method1088(int i) {
	aClass3_1123 = null;
	aClass57_1122 = null;
	aClass3_1121 = null;
	aClass3_1130 = null;
	aClass3_1125 = null;
	if (i != 0)
	    method1086((byte) -76, -93);
	aClass3_1124 = null;
	aClass3_1118 = null;
	aClass3_1131 = null;
	aClass3_1127 = null;
	aClass3_1129 = null;
	aClass3_1119 = null;
    }
    
    public static void writeOdStatus(boolean bool) {
	if (Deque.odSocket != null) {
	    try {
		Buffer class39_sub6 = new Buffer(4);
		class39_sub6.putByte(!bool ? 3 : 2);
		class39_sub6.putTri(0);
		Deque.odSocket.write(class39_sub6.payload, 0, 4);
	    } catch (java.io.IOException ioexception) {
		try {
		    Deque.odSocket.stop();
		} catch (Exception exception) {
		    /* empty */
		}
		Deque.odSocket = null;
		Class65.odErrors++;
	    }
	}
    }
    
    public static JString decodeHuffmans(Buffer buffer) {
	return StillGraphic.decodeHuffmans(buffer, 32767);
    }
    
    static {
	anInt1126 = 0;
	aClass3_1124 = Class39_Sub5_Sub9.createJstring("glow2:");
	aClass3_1118 = aClass3_1127;
	anInt1117 = 0;
	aClass3_1125 = Class39_Sub5_Sub9.createJstring("k");
	aClass3_1129 = aClass3_1124;
	aClass3_1131 = Class39_Sub5_Sub9.createJstring("Bad session id)3");
	aClass3_1121 = Class39_Sub5_Sub9.createJstring("Texturen geladen)3");
	aClass3_1119 = aClass3_1131;
	aClass3_1130
	    = Class39_Sub5_Sub9.createJstring("Unexpected loginserver response)3");
	aClass3_1123 = aClass3_1130;
    }
}
