package unpackaged;

import jagex.utils.HashTable;
import jagex.world.actors.StillGraphic;
import jagex.utils.SubNode;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import jagex.utils.Cache;

/* RuntimeException_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class RuntimeException_Sub1 extends RuntimeException
{
    public static JString aClass3_1212
	= Class39_Sub5_Sub9.createJstring(" from your friend list first");
    public Throwable aThrowable1213;
    public static JString aClass3_1214;
    public String aString1215;
    public static int anInt1216 = 0;
    public static Deque aClass49_1217;
    public static JString aClass3_1218;
    public static JString aClass3_1219;
    public static Cache aClass7_1220;
    public static boolean aBoolean1221;
    public static int anInt1222;
    public static JString aClass3_1223;
    public static JString aClass3_1224;
    public static boolean[] aBooleanArray1225;
    
    public static void method1122(byte i) {
	aClass3_1212 = null;
	aClass3_1223 = null;
	aClass3_1218 = null;
	aClass7_1220 = null;
	aBooleanArray1225 = null;
	aClass3_1219 = null;
	aClass3_1224 = null;
	aClass3_1214 = null;
	aClass49_1217 = null;
    }
    
    public static void method1123() {
	Class39_Sub5_Sub9.anIntArray1799 = null;
	TraversalMap.aByteArrayArray517 = null;
	SubNode.anIntArray1352 = null;
	Class39_Sub14.anIntArray1512 = null;
	Class39_Sub11.anIntArray1460 = null;
	Class46_Sub1.anIntArray1548 = null;
    }
    
    public RuntimeException_Sub1(Throwable throwable, String string) {
	aThrowable1213 = throwable;
	aString1215 = string;
    }
    
    public static void method1124(byte i) {
	Class44.aBoolean833 = true;
	Class25.method288((byte) -112);
	if (StillGraphic.anInt2338 == -1) {
	    if (Class39_Sub5_Sub14.anIntArray1914[Node.anInt728] != -1) {
		boolean bool
		    = Deque.method955(261, 190, 0,
					(Class39_Sub5_Sub14.anIntArray1914
					 [Node.anInt728]),
					0, -1, 1);
		if (!bool)
		    Class39_Sub14.aBoolean1520 = true;
	    }
	} else {
	    boolean bool = Deque.method955(261, 190, 0,
					     StillGraphic.anInt2338,
					     0, -1, 1);
	    if (!bool)
		Class39_Sub14.aBoolean1520 = true;
	}
	if (Class39_Sub12.aBoolean1493 && Class37.anInt653 == 1) {
	    if (HashTable.languageId != 1)
		Class1.method49(94);
	    else
		Node.method407(false);
	}
	Class62_Sub2.method1079(8);
    }
    
    static {
	aClass3_1214 = aClass3_1212;
	aClass49_1217 = new Deque();
	aClass3_1218
	    = Class39_Sub5_Sub9.createJstring("Ung-Ultiger Benutzername");
	aClass3_1219 = Class39_Sub5_Sub9.createJstring("Einloggen");
	aClass7_1220 = new Cache(260);
	anInt1222 = 0;
	aBooleanArray1225 = new boolean[5];
	aBoolean1221 = false;
	aClass3_1223 = Class39_Sub5_Sub9.createJstring("Examine");
	aClass3_1224 = aClass3_1223;
    }
}
