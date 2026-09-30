package jagex.utils;

import jagex.utils.Node;
import jagex.utils.JString;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import unpackaged.Class15;
import unpackaged.Class2;
import unpackaged.Class39_Sub14;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.FileTable;

/* Class19 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class HashTable
{
    public Node iterator;
    public static JString aClass3_356;
    public static byte[] chunkBuffer = new byte[520];
    public int size;
    public static JString aClass3_359;
    public long lastFetchedHash;
    public static boolean aBoolean361 = false;
    public Node[] nodes;
    public static int anInt363;
    public Node lastLookup;
    public static Deque aClass49_365;
    public int lookupPosition = 0;
    public static boolean isMembers = false;
    public static JString aClass3_368;
    public static FileTable aClass9_369;
    public static HashTable pendingRegularRequests;
    public static int languageId;
    public static JString aClass3_372;
    public static JString aClass3_373;
    public static JString aClass3_374;
    public static JString aClass3_375;
    public static JString aClass3_376;
    public static int[] anIntArray377;
    public static JString aClass3_378;
    public static JString aClass3_379;
    public static JString aClass3_380;
    public static long aLong381;
    public static JString aClass3_382;
    public static JString aClass3_383;
    public static JString aClass3_384;
    public static JString aClass3_385;
    public static JString aClass3_386;
    
    public static void method237(long l, byte i) {
	if (l != 0L) {
	    for (int i_0_ = 0; Class15.amountIgnores > i_0_; i_0_++) {
		if (Class39_Sub5_Sub9.ignoreUsernames[i_0_] == l) {
		    Class39_Sub14.aBoolean1520 = true;
		    Class15.amountIgnores--;
		    for (int i_1_ = i_0_; i_1_ < Class15.amountIgnores; i_1_++)
			Class39_Sub5_Sub9.ignoreUsernames[i_1_]
			    = Class39_Sub5_Sub9.ignoreUsernames[i_1_ + 1];
		    FrameBuffer.outgoingGameBuffer
			.putFrame(250);
		    FrameBuffer.outgoingGameBuffer.putQword(l);
		    break;
		}
	    }
	}
    }
    
    public void put(long hash, Node node) {
	if (node.prevNode != null)
	    node.unlinkDeque();
	Node head = nodes[(int) (hash & (long) (size - 1))];
	node.prevNode = head.prevNode;
	node.nextNode = head;
	node.prevNode.nextNode = node;
	node.nextNode.prevNode = node;
	node.hash = hash;
    }
    
    public Node quickLookup() {
	lookupPosition = 0;
	return lookup();
    }
    
    public static JString createJstring(byte[] src, int off, int len) {
	JString jstr = new JString();
	jstr.length = 0;
	jstr.bytes = new byte[len];
	for (int i = off; off + len > i; i++) {
	    if (src[i] != 0)
		jstr.bytes[jstr.length++] = src[i];
	}
	return jstr;
    }
    
    public Node lookup() {
	if (lookupPosition > 0 && nodes[lookupPosition - 1] != lastLookup) {
	    Node node = lastLookup;
	    lastLookup = node.nextNode;
	    return node;
	}
	while (size > lookupPosition) {
	    Node node = nodes[lookupPosition++].nextNode;
	    if (node != nodes[lookupPosition - 1]) {
		lastLookup = node.nextNode;
		return node;
	    }
	}
	return null;
    }
    
    public HashTable(int i) {
	size = i;
	nodes = new Node[i];
	for (int i_6_ = 0; i_6_ < i; i_6_++) {
	    Node node = nodes[i_6_] = new Node();
	    node.prevNode = node;
	    node.nextNode = node;
	}
    }
    
    public Node lookupLast() {
	if (iterator == null)
	    return null;
	for (Node head = nodes[(int) (lastFetchedHash & (long) (size - 1))]; head != iterator; iterator = iterator.nextNode) {
	    if (lastFetchedHash == iterator.hash) {
		Node node = iterator;
		iterator = iterator.nextNode;
		return node;
	    }
	}
	iterator = null;
	return null;
    }
    
    public static void method243(int i) {
	aClass49_365 = null;
	aClass9_369 = null;
	anIntArray377 = null;
	aClass3_380 = null;
	aClass3_386 = null;
	aClass3_356 = null;
	aClass3_378 = null;
	aClass3_384 = null;
	aClass3_374 = null;
	aClass3_385 = null;
	pendingRegularRequests = null;
	aClass3_379 = null;
	aClass3_372 = null;
	aClass3_382 = null;
	chunkBuffer = null;
	aClass3_376 = null;
	aClass3_368 = null;
	aClass3_375 = null;
	aClass3_383 = null;
	aClass3_373 = null;
	aClass3_359 = null;
    }
    
    public static void method244(Class39_Sub5_Sub4_Sub4 class39_sub5_sub4_sub4,
				 boolean bool) {
	class39_sub5_sub4_sub4.anInt2274 = 0;
	if (class39_sub5_sub4_sub4.anInt2292 == 0)
	    class39_sub5_sub4_sub4.anInt2294 = 1024;
	if (class39_sub5_sub4_sub4.anInt2292 == 1)
	    class39_sub5_sub4_sub4.anInt2294 = 1536;
	int i = class39_sub5_sub4_sub4.anInt2256 - Class2.logicCycle;
	if (class39_sub5_sub4_sub4.anInt2292 == 2)
	    class39_sub5_sub4_sub4.anInt2294 = 0;
	int i_8_ = (class39_sub5_sub4_sub4.anInt2297 * 64
		    + class39_sub5_sub4_sub4.anInt2287 * 128);
	class39_sub5_sub4_sub4.anInt2301
	    += (i_8_ - class39_sub5_sub4_sub4.anInt2301) / i;
	if (class39_sub5_sub4_sub4.anInt2292 == 3)
	    class39_sub5_sub4_sub4.anInt2294 = 512;
	int i_9_ = (class39_sub5_sub4_sub4.anInt2266 * 128
		    + class39_sub5_sub4_sub4.anInt2297 * 64);
	class39_sub5_sub4_sub4.anInt2275
	    += (i_9_ - class39_sub5_sub4_sub4.anInt2275) / i;
    }
    
    public Node fetch(long hash) {
	lastFetchedHash = hash;
	Node head = nodes[(int) (hash & (long) (size - 1))];
	for (iterator = head.nextNode; head != iterator; iterator = iterator.nextNode) {
	    if (hash == iterator.hash) {
		Node node = iterator;
		iterator = iterator.nextNode;
		return node;
	    }
	}
	iterator = null;
	return null;
    }
    
    static {
	aClass3_359
	    = Class39_Sub5_Sub9
		  .createJstring("Enter name of friend to add to list");
	aClass3_356 = aClass3_359;
	aClass3_368 = Class39_Sub5_Sub9.createJstring("@yel@");
	aClass49_365 = new Deque();
	languageId = 0;
	pendingRegularRequests = new HashTable(4096);
	aClass3_373 = Class39_Sub5_Sub9.createJstring("Wordpack geladen)3");
	aClass3_375 = Class39_Sub5_Sub9.createJstring(" )2> @lre@");
	aClass3_376 = Class39_Sub5_Sub9.createJstring("Loaded textures");
	aClass3_379 = Class39_Sub5_Sub9.createJstring("Select");
	aClass3_378
	    = Class39_Sub5_Sub9
		  .createJstring("Fehler bei der Verbindung zum Server)3");
	aClass3_372
	    = Class39_Sub5_Sub9.createJstring("Could not complete login)3");
	aClass3_383 = aClass3_379;
	aClass3_380
	    = (Class39_Sub5_Sub9.createJstring
	       ("(WSpielkonto wiederherstellen(W Option auf der Hauptseite)3"));
	aClass3_384
	    = Class39_Sub5_Sub9.createJstring("Lade Ignorieren)2Liste)3)3)3");
	aClass3_382
	    = Class39_Sub5_Sub9
		  .createJstring("You have only just left another world)3");
	aClass3_374 = aClass3_382;
	aClass3_385 = aClass3_372;
	aClass3_386 = aClass3_376;
    }
}
