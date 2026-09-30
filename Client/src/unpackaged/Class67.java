package unpackaged;

import jagex.utils.Node;
import jagex.utils.JString;
import jagex.io.JSocket;

/* Class67 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class67
{
    public int anInt1166;
    public static JString aClass3_1167;
    public int anInt1168;
    public static JString aClass3_1169;
    public static FileLoader fileLoader2;
    public int anInt1171;
    public static long[] aLongArray1172 = new long[32];
    public static JString aClass3_1173;
    public static JString aClass3_1174;
    public static int[][][] heightMap;
    public static JString aClass3_1176;
    public Class39_Sub5_Sub4 aClass39_Sub5_Sub4_1177;
    public static JString aClass3_1178;
    public static byte[][][] aByteArrayArrayArray1179;
    public int anInt1180;
    public int anInt1181;
    public Class39_Sub5_Sub4 aClass39_Sub5_Sub4_1182;
    public Class39_Sub5_Sub4 aClass39_Sub5_Sub4_1183;
    public static int anInt1184;
    
    public static void method1107(int i) {
	JString.aClass7_1227.method134(27392);
	ArchiveWorker.aClass7_1197.method134(27392);
    }
    
    public static void method1108(byte i) {
	aClass3_1169 = null;
	aClass3_1174 = null;
	fileLoader2 = null;
	aClass3_1176 = null;
	aClass3_1178 = null;
	heightMap = null;
	aClass3_1173 = null;
	aClass3_1167 = null;
	aLongArray1172 = null;
	aByteArrayArrayArray1179 = null;
    }
    
    public static void insertArchive(CacheIO cache, int hash, byte[] payload) {
	ArchiveRequest request = new ArchiveRequest();
	request.hash = (long) hash;
	request.payload = payload;
	request.type = 0;
	request.cache = cache;
	synchronized (JSocket.archiveRequests) {
	    JSocket.archiveRequests.offerLast(request);
	}
	Class65.method1095(120);
    }
    
    public static void method1110(FileTable class9, FileTable class9_0_,
				  FileTable class9_1_, int i) {
	NpcDefinition.aClass9_1860 = class9_1_;
	Node.aClass9_739 = class9;
	Class47.aClass9_898 = class9_0_;
    }
    
    static {
	aClass3_1169 = Class39_Sub5_Sub9.createJstring("backleft2");
	aClass3_1174
	    = Class39_Sub5_Sub9
		  .createJstring(" zuerst von Ihrer Freunde)2Liste(Q");
	aClass3_1167
	    = Class39_Sub5_Sub9
		  .createJstring("To create a new account you need to");
	aClass3_1176 = aClass3_1167;
	aClass3_1178
	    = (Class39_Sub5_Sub9.createJstring
	       ("Gegenstand konnte nicht gefunden werden)1 verk-Urzen Sie den Suchbegriff)3"));
	heightMap = new int[4][105][105];
	anInt1184 = 0;
	aClass3_1173 = Class39_Sub5_Sub9.createJstring("Entfernen");
    }
}
