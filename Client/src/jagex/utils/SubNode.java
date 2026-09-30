package jagex.utils;

import jagex.utils.Node;
import jagex.utils.JString;
import unpackaged.Class39_Sub5_Sub9;

/* Class39_Sub5 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class SubNode extends Node
{
    public static boolean aBoolean1341;
    public static int[] anIntArray1342
	= { 16776960, 16711680, 65280, 65535, 16711935, 16777215 };
    public static JString aClass3_1343;
    public static int anInt1344;
    public static JString aClass3_1345;
    public static JString aClass3_1346;
    public static JString aClass3_1347;
    public static int anInt1348;
    public SubNode prevSubnode;
    public static JString aClass3_1350;
    public SubNode nextSubnode;
    public static int[] anIntArray1352;
    
    public void unlinkQueue() {
	if (prevSubnode != null) {
	    prevSubnode.nextSubnode = nextSubnode;
	    nextSubnode.prevSubnode = prevSubnode;
	    prevSubnode = null;
	    nextSubnode = null;
	}
    }
    
    public static void method462(int i) {
	anIntArray1352 = null;
	aClass3_1346 = null;
	if (i != 0)
	    method462(42);
	aClass3_1345 = null;
	aClass3_1343 = null;
	aClass3_1350 = null;
	aClass3_1347 = null;
	anIntArray1342 = null;
    }
    
    static {
	aBoolean1341 = false;
	aClass3_1347 = Class39_Sub5_Sub9.createJstring("Mitteilung");
	aClass3_1345
	    = Class39_Sub5_Sub9.createJstring("Connecting to friendserver");
	anInt1344 = 0;
	aClass3_1346 = aClass3_1345;
	anInt1348 = -1;
	aClass3_1350
	    = Class39_Sub5_Sub9.createJstring(" is already on your ignore list");
	aClass3_1343 = aClass3_1350;
    }
}
