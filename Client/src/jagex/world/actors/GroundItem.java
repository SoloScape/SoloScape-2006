package jagex.world.actors;

import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Npc;
import jagex.utils.Deque;
import jagex.io.Buffer;
import unpackaged.Bzip2Block;
import unpackaged.CacheIO;
import unpackaged.Canvas_Sub1;
import unpackaged.Class1;
import unpackaged.Class14;
import unpackaged.Class2;
import unpackaged.Class25;
import unpackaged.Class26;
import unpackaged.Class31;
import unpackaged.Class32;
import unpackaged.Class39_Sub13;
import unpackaged.Class39_Sub4;
import unpackaged.Class39_Sub5_Sub4;
import unpackaged.Class39_Sub5_Sub6;
import unpackaged.Class39_Sub5_Sub7;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class41;
import unpackaged.Class62_Sub1;
import unpackaged.Class63;
import unpackaged.Class65;
import unpackaged.Client;
import unpackaged.ClientApplet;
import unpackaged.FileLoader;
import unpackaged.FileTable;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import unpackaged.JMouseListener;
import unpackaged.Model;
import unpackaged.OndemandRequest;
import unpackaged.Widget;

/* Class39_Sub5_Sub4_Sub3 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class GroundItem extends Class39_Sub5_Sub4
{
    public static int[][] anIntArrayArray2233;
    public static JString aClass3_2234;
    public static JString aClass3_2235
	= Class39_Sub5_Sub9.createJstring("Angreifen");
    public static JString aClass3_2236;
    public static JString aClass3_2237
	= Class39_Sub5_Sub9.createJstring("und loggen sich dann erneut ein)3");
    public static int anInt2238;
    public static int anInt2239 = -1;
    public static Model[] aClass39_Sub5_Sub4_Sub6Array2240;
    public static Npc[] aClass39_Sub5_Sub4_Sub4_Sub1Array2241;
    public static int anInt2242;
    public int anInt2243;
    public static JString aClass3_2244;
    public static int[] anIntArray2245;
    public static JString aClass3_2246;
    public static int[] anIntArray2247;
    public int itemId;
    public static JString aClass3_2249;
    
    public static void method501(byte i) {
	aClass3_2244 = null;
	anIntArrayArray2233 = null;
	aClass3_2246 = null;
	anIntArray2247 = null;
	aClass39_Sub5_Sub4_Sub4_Sub1Array2241 = null;
	aClass3_2236 = null;
	aClass39_Sub5_Sub4_Sub6Array2240 = null;
	aClass3_2235 = null;
	anIntArray2245 = null;
	aClass3_2234 = null;
	aClass3_2249 = null;
	aClass3_2237 = null;
    }
    
    public Model method489(boolean bool) {
	if (bool != true)
	    return null;
	return Class26.getItemDefinition(itemId).method468(anInt2243, true,
							  (byte) -124);
    }
    
    public static void writeOdConnect(JSocket socket, boolean bool) {
	if (Deque.odSocket != null) {
	    try {
		Deque.odSocket.stop();
	    } catch (Exception exception) {
		/* empty */
	    }
	    Deque.odSocket = null;
	}
	Deque.odSocket = socket;
	Class63.writeOdStatus(bool);
	FileLoader.currentOdRequest = null;
	Class39_Sub4.odBlockOffset = 0;
	ClientApplet.odBuffer.offset = 0;
	JMouseListener.odArchiveBuffer = null;
	for (;;) {
	    OndemandRequest ondemandRequest = (OndemandRequest) CacheIO.pendingPriorityRequests.quickLookup();
	    if (ondemandRequest == null)
		break;
	    Class25.priorityRequestsQueue.put(ondemandRequest.hash, ondemandRequest);
	    Class1.pendingPriorityRequests--;
	    Class39_Sub5_Sub6.queuedPriorityRequests++;
	}
	for (;;) {
	    OndemandRequest request = (OndemandRequest) HashTable.pendingRegularRequests.quickLookup();
	    if (request == null)
		break;
	    Class32.regularRequestsQueue.offerLast(request);
	    Class31.requests.put(request.hash, request);
	    Class41.queuedRegularRequests++;
	    Canvas_Sub1.pendingRegularRequests--;
	}
	if (ClientApplet.encryptionKey != 0) {
	    try {
		Buffer buffer = new Buffer(4);
		buffer.putByte(4);
		buffer.putByte(ClientApplet.encryptionKey);
		buffer.putWord(0);
		Deque.odSocket.write(buffer.payload, 0, 4);
	    } catch (java.io.IOException ioexception) {
		try {
		    Deque.odSocket.stop();
		} catch (Exception exception) {
		    /* empty */
		}
		Class65.odErrors++;
		Deque.odSocket = null;
	    }
	}
	Huffmans.anInt748 = 0;
	Bzip2Block.aLong1063 = Class2.getSystemTime();
    }
    
    public static IndexedColorSprite method503(FileTable class9, int i,
						    int i_0_, boolean bool) {
	if (!Client.decodeBitmapFont(class9, i_0_, i))
	    return null;
	return Bzip2Block.method1036(false);
    }
    
    public static Widget method504
	(int i, int i_1_, int i_2_, Widget class39_sub5_sub17,
	 Widget class39_sub5_sub17_3_) {
	Widget class39_sub5_sub17_4_
	    = Class39_Sub5_Sub7.method585(class39_sub5_sub17_3_.anInt2084, i,
					  class39_sub5_sub17_3_.quadWidth,
					  (Class62_Sub1
					   .widgets
					   [(class39_sub5_sub17_3_.anInt2084
					     >> 16)]),
					  i_1_,
					  class39_sub5_sub17_3_.anInt2064,
					  class39_sub5_sub17_3_.quadHeight,
					  class39_sub5_sub17, i_2_, 0, -123,
					  class39_sub5_sub17_3_.anInt1994);
	if (class39_sub5_sub17_4_ != null)
	    return class39_sub5_sub17_4_;
	if (class39_sub5_sub17_3_.aClass39_Sub5_Sub17Array2025 != null)
	    class39_sub5_sub17_4_
		= Class39_Sub5_Sub7.method585(class39_sub5_sub17_3_.anInt2084,
					      i,
					      class39_sub5_sub17_3_.quadWidth,
					      (class39_sub5_sub17_3_
					       .aClass39_Sub5_Sub17Array2025),
					      i_1_,
					      class39_sub5_sub17_3_.anInt2064,
					      class39_sub5_sub17_3_.quadHeight,
					      class39_sub5_sub17, 0, 0, -114,
					      class39_sub5_sub17_3_.anInt1994);
	return class39_sub5_sub17_4_;
    }
    
    public static int method505(int i, byte i_5_, int i_6_) {
	Class39_Sub13 class39_sub13
	    = ((Class39_Sub13)
	       Class14.aClass19_213.fetch((long) i_6_));
	if (class39_sub13 == null)
	    return 0;
	if (i == -1)
	    return 0;
	int i_7_ = 0;
	for (int i_8_ = 0; i_8_ < class39_sub13.anIntArray1504.length;
	     i_8_++) {
	    if (i == class39_sub13.anIntArray1505[i_8_])
		i_7_ += class39_sub13.anIntArray1504[i_8_];
	}
	return i_7_;
    }
    
    public static int method506(int i, int i_9_, byte i_10_) {
	if (i == -2)
	    return 12345678;
	if (i == -1) {
	    if (i_9_ < 0)
		i_9_ = 0;
	    else if (i_9_ > 127)
		i_9_ = 127;
	    i_9_ = -i_9_ + 127;
	    return i_9_;
	}
	i_9_ = i_9_ * (i & 0x7f) / 128;
	if (i_9_ >= 2) {
	    if (i_9_ > 126)
		i_9_ = 126;
	} else
	    i_9_ = 2;
	return (i & 0xff80) + i_9_;
    }
    
    static {
	aClass3_2236
	    = Class39_Sub5_Sub9
		  .createJstring("Anmelde)2Zeitlimit -Uberschritten)3");
	anIntArrayArray2233
	    = (new int[][]
	       { { 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433,
		   2983, 54193 },
		 { 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094,
		   10153, 56621, 4783, 1341, 16578, 35003, 25239 },
		 { 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430,
		   24094, 10153, 56621, 4783, 1341, 16578, 35003 },
		 { 4626, 11146, 6439, 12, 4758, 10270 },
		 { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 } });
	aClass3_2234
	    = Class39_Sub5_Sub9.createJstring("Ung-Ultiges Anmelde)2Paket)3");
	aClass39_Sub5_Sub4_Sub6Array2240 = new Model[4];
	aClass39_Sub5_Sub4_Sub4_Sub1Array2241
	    = new Npc[32768];
	anInt2242 = 0;
	aClass3_2244
	    = Class39_Sub5_Sub9.createJstring("Diese Welt ist voll)3");
	aClass3_2246 = Class39_Sub5_Sub9.createJstring("@or2@");
	aClass3_2249
	    = Class39_Sub5_Sub9
		  .createJstring("Bitte starten Sie eine Mitgliedschaft");
    }
}
