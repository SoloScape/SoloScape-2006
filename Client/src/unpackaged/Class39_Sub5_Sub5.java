package unpackaged;

import jagex.utils.HashTable;
import jagex.world.actors.StillGraphic;
import jagex.world.actors.Projectile;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;

/* Class39_Sub5_Sub5 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub5_Sub5 extends SubNode
{
    public static JString aClass3_1735
	= Class39_Sub5_Sub9.createJstring("Versteckt");
    public static JString aClass3_1736;
    public static JString aClass3_1737;
    public static JString aClass3_1738;
    public static int anInt1739 = 0;
    public static FileTable aClass9_1740;
    public static JString aClass3_1741;
    public static int anInt1742 = 3353893;
    public static JString aClass3_1743
	= Class39_Sub5_Sub9.createJstring("Your account is already logged in)3");
    public static JString aClass3_1744;
    public static JString aClass3_1745;
    public int anInt1746 = 0;
    public static JString aClass3_1747;
    public static FileTable aClass9_1748;
    public static boolean aBoolean1749;
    /*synthetic*/ public static Class aClass1750;
    
    public static void method569(byte i) {
	if (Class46.anInt887 == 1) {
	    if (Class39_Sub4.anInt1329 >= 6 && Class39_Sub4.anInt1329 <= 106
		&& Bzip2Block.anInt1054 >= 467 && Bzip2Block.anInt1054 <= 499) {
		Projectile.anInt2203++;
		Bzip2Block.anInt1051 = (Bzip2Block.anInt1051 + 1) % 4;
		Class14.aBoolean245 = true;
		Class39_Sub5_Sub4_Sub4.aBoolean2253 = true;
		FrameBuffer.outgoingGameBuffer.putFrame(76);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Bzip2Block.anInt1051);
		FrameBuffer.outgoingGameBuffer
		    .putByte(NameTable.anInt177);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Cache.anInt118);
	    }
	    if (Class39_Sub4.anInt1329 >= 135 && Class39_Sub4.anInt1329 <= 235
		&& Bzip2Block.anInt1054 >= 467 && Bzip2Block.anInt1054 <= 499) {
		Projectile.anInt2203++;
		NameTable.anInt177 = (NameTable.anInt177 + 1) % 3;
		Class39_Sub5_Sub4_Sub4.aBoolean2253 = true;
		Class14.aBoolean245 = true;
		FrameBuffer.outgoingGameBuffer.putFrame(76);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Bzip2Block.anInt1051);
		FrameBuffer.outgoingGameBuffer
		    .putByte(NameTable.anInt177);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Cache.anInt118);
	    }
	    if (Class39_Sub4.anInt1329 >= 273 && Class39_Sub4.anInt1329 <= 373
		&& Bzip2Block.anInt1054 >= 467 && Bzip2Block.anInt1054 <= 499) {
		Class39_Sub5_Sub4_Sub4.aBoolean2253 = true;
		Cache.anInt118 = (Cache.anInt118 + 1) % 3;
		Projectile.anInt2203++;
		Class14.aBoolean245 = true;
		FrameBuffer.outgoingGameBuffer.putFrame(76);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Bzip2Block.anInt1051);
		FrameBuffer.outgoingGameBuffer
		    .putByte(NameTable.anInt177);
		FrameBuffer.outgoingGameBuffer
		    .putByte(Cache.anInt118);
	    }
	    if (Class39_Sub4.anInt1329 >= 412 && Class39_Sub4.anInt1329 <= 512
		&& Bzip2Block.anInt1054 >= 467 && Bzip2Block.anInt1054 <= 499) {
		if (Class39_Sub11.anInt1478 == -1) {
		    Class55.method999(31121);
		    if (Class48.anInt907 != -1) {
			Class39_Sub12.aBoolean1487 = false;
			Class26.anInt473 = Class39_Sub11.anInt1478
			    = Class48.anInt907;
			Class66.aClass3_1163 = Class66.blankString;
		    }
		} else
		    JMouseListener.method902(Class66.blankString,
				      Class31.aClass3_572, false, 0);
	    }
	}
    }
    
    public void method570(byte i, Buffer class39_sub6, int i_0_) {
	if (i >= 9) {
	    if (i_0_ == 2)
		anInt1746 = class39_sub6.getUword();
	}
    }
    
    public static void method571(int i) {
	aClass3_1735 = null;
	aClass3_1743 = null;
	aClass3_1747 = null;
	aClass3_1745 = null;
	aClass3_1744 = null;
	aClass3_1741 = null;
	aClass9_1740 = null;
	aClass3_1737 = null;
	aClass9_1748 = null;
	aClass3_1738 = null;
	aClass3_1736 = null;
    }
    
    public void method572(Buffer class39_sub6, byte i) {
	for (;;) {
	    int i_1_ = class39_sub6.getUbyte();
	    if (i_1_ == 0)
		break;
	    method570((byte) 31, class39_sub6, i_1_);
	}
	int i_2_ = 111 / ((i + 88) / 34);
    }
    
    public static void method573(int i) {
	if (Class20.aClass13_395 != null) {
	    long l = Class2.getSystemTime();
	    if (Class39_Sub5_Sub16.aLong1985 < l) {
		Class20.aClass13_395.method193(l);
		int i_3_ = (int) (l + -Class39_Sub5_Sub16.aLong1985);
		Class39_Sub5_Sub16.aLong1985 = l;
		synchronized (aClass1750 != null ? aClass1750
			      : (aClass1750 = method575("unpackaged.Class13"))) {
		    StillGraphic.anInt2334
			+= i_3_ * Class15.anInt274;
		    int i_4_ = ((-(Class15.anInt274 * 2000)
				 + StillGraphic.anInt2334)
				/ 1000);
		    if (i_4_ > 0) {
			if (ScriptState.aClass39_Sub1_439 != null)
			    ScriptState.aClass39_Sub1_439.method417(i_4_);
			StillGraphic.anInt2334 -= i_4_ * 1000;
		    }
		}
	    }
	}
    }
    
    public static Class39_Sub5_Sub9 method574(int i, byte i_5_) {
	Class39_Sub5_Sub9 class39_sub5_sub9
	    = ((Class39_Sub5_Sub9)
	       Class2.aClass7_54.get((long) i));
	if (class39_sub5_sub9 != null)
	    return class39_sub5_sub9;
	byte[] is = HashTable.aClass9_369.lookupFile(1, i);
	class39_sub5_sub9 = new Class39_Sub5_Sub9();
	if (is != null)
	    class39_sub5_sub9.method603(i, (byte) 90, new Buffer(is));
	class39_sub5_sub9.method608(-84);
	Class2.aClass7_54.put(class39_sub5_sub9, (long) i, (byte) -112);
	return class39_sub5_sub9;
    }
    
    /*synthetic*/ public static Class method575(String string) {
	try {
	    return Class.forName(string);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new NoClassDefFoundError(classnotfoundexception
					       .getMessage());
	}
    }
    
    static {
	aClass3_1738
	    = Class39_Sub5_Sub9
		  .createJstring("This computers address has been blocked");
	aClass3_1744
	    = (Class39_Sub5_Sub9.createJstring
	       ("Too many incorrect logins from your address)3"));
	aClass3_1747 = aClass3_1743;
	aClass3_1745 = aClass3_1738;
	aClass3_1737
	    = Class39_Sub5_Sub9.createJstring("Prepared visibility map");
	aClass3_1736 = aClass3_1744;
	aClass3_1741 = aClass3_1737;
	aBoolean1749 = false;
    }
}
