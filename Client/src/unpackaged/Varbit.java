package unpackaged;

/* Class39_Sub5_Sub8 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.io.BufferedFile;
import jagex.world.actors.GroundItem;
import jagex.graphics.JImage;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Npc;
import jagex.world.map.TraversalMap;
import jagex.utils.Queue;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.awt.Graphics;

public class Varbit extends SubNode
{
    public static int anInt1790 = 0;
    public int settingId;
    public static JString[] globalStrVars = new JString[1000];
    public int anInt1793;
    public int anInt1794;
    public static FileTable npcFileLoader;
    public static Cache aClass7_1796 = new Cache(10);
    public static JString aClass3_1797
	= Class39_Sub5_Sub9.createJstring("@whi@ )4 ");
    public static int anInt1798 = 0;
    
    public static void method590(boolean bool) {
	try {
	    Graphics graphics = Class41.aCanvas778.getGraphics();
	    FrameBuffer.aClass57_2155.draw(graphics, 0, 4);
	    Class55.aClass57_1248.draw(graphics, 0, 357);
	    JImage.aClass57_1576.draw(graphics, 722, 4);
	    NameTable.aClass57_182.draw(graphics, 743, 205);
	    Class43.aClass57_812.draw(graphics, 0, 0);
	    Class63.aClass57_1122.draw(graphics, 516, 4);
	    TraversalMap.aClass57_516.draw(graphics, 516, 205);
	    Widget.aClass57_2114.draw(graphics, 496,
							357);
	    Queue.aClass57_981.draw(graphics, 0, 338);
	} catch (Exception exception) {
	    Class41.aCanvas778.repaint();
	}
    }
    
    public static void method591(byte i) {
	Class39_Sub11 class39_sub11
	    = (Class39_Sub11) Class15.aClass49_278.getFirst();
	if (i != 92)
	    method591((byte) -28);
	for (/**/; class39_sub11 != null;
	     class39_sub11 = ((Class39_Sub11)
			      Class15.aClass49_278.getNext())) {
	    if (class39_sub11.anInt1456 > 0)
		class39_sub11.anInt1456--;
	    if (class39_sub11.anInt1456 == 0) {
		if (class39_sub11.anInt1471 < 0
		    || ArchiveRequest.method862(i - 92, class39_sub11.anInt1451,
					      class39_sub11.anInt1471)) {
		    Class55.method1000(class39_sub11.anInt1469,
				       class39_sub11.anInt1458,
				       class39_sub11.anInt1449,
				       class39_sub11.anInt1474,
				       class39_sub11.anInt1471,
				       class39_sub11.anInt1451,
				       class39_sub11.anInt1466, false);
		    class39_sub11.unlinkDeque();
		}
	    } else {
		if (class39_sub11.anInt1476 > 0)
		    class39_sub11.anInt1476--;
		if (class39_sub11.anInt1476 == 0
		    && class39_sub11.anInt1466 >= 1
		    && class39_sub11.anInt1474 >= 1
		    && class39_sub11.anInt1466 <= 102
		    && class39_sub11.anInt1474 <= 102
		    && (class39_sub11.anInt1459 < 0
			|| ArchiveRequest.method862(0, class39_sub11.anInt1457,
						  class39_sub11.anInt1459))) {
		    Class55.method1000(class39_sub11.anInt1469,
				       class39_sub11.anInt1458,
				       class39_sub11.anInt1472,
				       class39_sub11.anInt1474,
				       class39_sub11.anInt1459,
				       class39_sub11.anInt1457,
				       class39_sub11.anInt1466, false);
		    class39_sub11.anInt1476 = -1;
		    if (class39_sub11.anInt1459 != class39_sub11.anInt1471
			|| class39_sub11.anInt1471 != -1) {
			if (class39_sub11.anInt1459 == class39_sub11.anInt1471
			    && (class39_sub11.anInt1472
				== class39_sub11.anInt1449)
			    && (class39_sub11.anInt1451
				== class39_sub11.anInt1457))
			    class39_sub11.unlinkDeque();
		    } else
			class39_sub11.unlinkDeque();
		}
	    }
	}
    }
    
    public void decode(Buffer buffer) {
	for (;;) {
	    int opcode = buffer.getUbyte();
	    if (opcode == 0)
		break;
	    decodeOpcode(opcode, buffer);
	}
    }
    
    public static void method593(int i, int i_1_, boolean bool, byte[] is,
				 int i_2_) {
	if (Class55.aClass62_1251 != null) {
	    if (GroundItem.anInt2239 < 0) {
		if (ArchiveRequest.anInt1415 != 0) {
		    JKeyListener.aBoolean628 = bool;
		    Class39_Sub5_Sub6.aByteArray1768 = is;
		    Class39_Sub5_Sub18.anInt2120 = i;
		} else
		    ArchiveRequest.method857(i, bool, is, false);
	    } else {
		Queue.anInt987 = i_2_;
		if (GroundItem.anInt2239 != 0) {
		    int i_3_ = (Class39_Sub5_Sub4_Sub4.method512
				(929, GroundItem.anInt2239));
		    i_3_ -= Class65.anInt1141;
		    ArchiveRequest.anInt1415 = (i_3_ + 3600) / i_2_;
		    if (ArchiveRequest.anInt1415 < 1)
			ArchiveRequest.anInt1415 = 1;
		} else
		    ArchiveRequest.anInt1415 = 1;
		Class39_Sub5_Sub6.aByteArray1768 = is;
		Class39_Sub5_Sub18.anInt2120 = i;
		JKeyListener.aBoolean628 = bool;
	    }
	}
    }
    
    public static void method594(int i, int i_4_) {
	if (JSocket.loadWidget(i_4_)) {
	    Widget[] class39_sub5_sub17s
		= Class62_Sub1.widgets[i_4_];
	    for (int i_5_ = 0; i_5_ < class39_sub5_sub17s.length; i_5_++) {
		Widget class39_sub5_sub17
		    = class39_sub5_sub17s[i_5_];
		if (class39_sub5_sub17 != null) {
		    class39_sub5_sub17.anInt2079 = 0;
		    class39_sub5_sub17.anInt1999 = 0;
		}
	    }
	}
    }
    
    public static void method595(int i) {
	synchronized (Npc.aClass35_2499) {
	    Buffer.anInt1368 = Class39_Sub5_Sub4.anInt1731;
	    if (BufferedFile.anInt341 < 0) {
		for (int i_6_ = 0; i_6_ < 112; i_6_++)
		    Class13.aBooleanArray200[i_6_] = false;
		BufferedFile.anInt341 = Class46.anInt879;
	    } else {
		while (BufferedFile.anInt341 != Class46.anInt879) {
		    int i_7_
			= Class39_Sub5_Sub11.anIntArray1847[Class46.anInt879];
		    Class46.anInt879 = Class46.anInt879 + 1 & 0x7f;
		    if (i_7_ >= 0)
			Class13.aBooleanArray200[i_7_] = true;
		    else
			Class13.aBooleanArray200[i_7_ ^ 0xffffffff] = false;
		}
	    }
	    Class39_Sub5_Sub4.anInt1731 = Class39_Sub5_Sub4_Sub4.anInt2299;
	}
    }
    
    public void decodeOpcode(int opcode, Buffer buffer) {
	if (opcode == 1) {
	    settingId = buffer.getUword();
	    anInt1793 = buffer.getUbyte();
	    anInt1794 = buffer.getUbyte();
	}
    }
    
    public static void method597(int i) {
	ArchiveWorker.aClass57_1196.method1006(10);
    }
    
    public static void method598(byte i) {
	aClass3_1797 = null;
	globalStrVars = null;
	aClass7_1796 = null;
	npcFileLoader = null;
    }
}
