package jagex.utils;

/* Class40 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.utils.Queue;
import jagex.io.Buffer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import unpackaged.Class14;
import unpackaged.Class37;
import unpackaged.Class39_Sub13;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub18;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class4;
import unpackaged.Class43;
import unpackaged.Class68;
import unpackaged.FileTable;
import unpackaged.RuntimeException_Sub1;
import unpackaged.Signlink;
import unpackaged.Widget;

public class Huffmans
{
    public byte[] codes;
    public static JString aClass3_745;
    public static JString aClass3_746;
    public static JString aClass3_747
	= Class39_Sub5_Sub9.createJstring("gelb:");
    public static int anInt748;
    public static int anInt749;
    public static JString aClass3_750
	= Class39_Sub5_Sub9.createJstring("leuchten3:");
    public static JString aClass3_751;
    public static long aLong752;
    public static JString aClass3_753;
    public static int frameSize;
    public static JString aClass3_755;
    public static int anInt756;
    public static int anInt757;
    public static JString aClass3_758;
    public int[] codeLengths;
    public static FileTable aClass9_760;
    public int[] anIntArray761;
    public static int[] anIntArray762;
    public static JString aClass3_763;
    public static boolean aBoolean764;
    
    public static Class39_Sub5_Sub18 method881(int i, int i_0_) {
	Class39_Sub5_Sub18 class39_sub5_sub18
	    = ((Class39_Sub5_Sub18)
	       Class4.aClass7_70.get((long) i_0_));
	if (class39_sub5_sub18 != null)
	    return class39_sub5_sub18;
	byte[] is = Class43.aClass9_816.lookupFile(13, i_0_);
	class39_sub5_sub18 = new Class39_Sub5_Sub18();
	class39_sub5_sub18.anInt2137 = i_0_;
	if (is != null)
	    class39_sub5_sub18.method779(180, new Buffer(is));
	Class4.aClass7_70.put(class39_sub5_sub18, (long) i_0_,
				    (byte) -123);
	return class39_sub5_sub18;
    }
    
    public static void method882(int i) {
	if (Signlink.javaVendor.toLowerCase().indexOf("microsoft") != -1) {
	    Player.anIntArray2532[189] = 26;
	    Player.anIntArray2532[192] = 58;
	    Player.anIntArray2532[187] = 27;
	    Player.anIntArray2532[186] = 57;
	    Player.anIntArray2532[221] = 43;
	    Player.anIntArray2532[220] = 74;
	    Player.anIntArray2532[188] = 71;
	    Player.anIntArray2532[190] = 72;
	    Player.anIntArray2532[222] = 59;
	    Player.anIntArray2532[191] = 73;
	    Player.anIntArray2532[223] = 28;
	    Player.anIntArray2532[219] = 42;
	} else {
	    Player.anIntArray2532[45] = 26;
	    Player.anIntArray2532[61] = 27;
	    if (Signlink.aMethod398 == null) {
		Player.anIntArray2532[222] = 59;
		Player.anIntArray2532[192] = 58;
	    } else {
		Player.anIntArray2532[520] = 59;
		Player.anIntArray2532[192] = 28;
		Player.anIntArray2532[222] = 58;
	    }
	    Player.anIntArray2532[44] = 71;
	    Player.anIntArray2532[91] = 42;
	    Player.anIntArray2532[47] = 73;
	    Player.anIntArray2532[59] = 57;
	    Player.anIntArray2532[92] = 74;
	    Player.anIntArray2532[46] = 72;
	    Player.anIntArray2532[93] = 43;
	}
    }
    
    public static boolean parseClientScript(Widget widget) {
	if (widget.conditionOpcodes == null)
	    return false;
	for (int i = 0; widget.conditionOpcodes.length > i; i++) {
	    int value = Class68.getScriptValue(widget, i);
	    int compareTo = widget.conditionValues[i];
	    if (widget.conditionOpcodes[i] == 2) {
		if (compareTo <= value)
		    return false;
	    } else if (widget.conditionOpcodes[i] == 3) {
		if (compareTo >= value)
		    return false;
	    } else if (widget.conditionOpcodes[i] != 4) {
		if (value != compareTo)
		    return false;
	    } else if (value == compareTo)
		return false;
	}
	return true;
    }
    
    public static String method884(Throwable throwable, byte i)
	throws IOException {
	String string;
	if (throwable instanceof RuntimeException_Sub1) {
	    RuntimeException_Sub1 runtimeexception_sub1
		= (RuntimeException_Sub1) throwable;
	    throwable = runtimeexception_sub1.aThrowable1213;
	    string = runtimeexception_sub1.aString1215 + " | ";
	} else
	    string = "";
	StringWriter stringwriter = new StringWriter();
	PrintWriter printwriter = new PrintWriter(stringwriter);
	throwable.printStackTrace(printwriter);
	printwriter.close();
	String string_4_ = stringwriter.toString();
	BufferedReader bufferedreader
	    = new BufferedReader(new StringReader(string_4_));
	String string_5_ = bufferedreader.readLine();
	for (;;) {
	    String string_6_ = bufferedreader.readLine();
	    if (string_6_ == null)
		break;
	    int i_7_ = string_6_.indexOf('(');
	    int i_8_ = string_6_.indexOf(')', i_7_ + 1);
	    if (i_7_ >= 0 && i_8_ >= 0) {
		String string_9_ = string_6_.substring(i_7_ + 1, i_8_);
		int i_10_ = string_9_.indexOf(".java:");
		if (i_10_ >= 0) {
		    string_9_ = (string_9_.substring(0, i_10_)
				 + string_9_.substring(i_10_ + 5));
		    string += string_9_ + ' ';
		    continue;
		}
		string_6_ = string_6_.substring(0, i_7_);
	    }
	    string_6_ = string_6_.trim();
	    string_6_ = string_6_.substring(string_6_.lastIndexOf(' ') + 1);
	    string_6_ = string_6_.substring(string_6_.lastIndexOf('\t') + 1);
	    string += string_6_ + ' ';
	}
	string += "| " + (String) string_5_;
	return string;
    }
    
    public int decode(int destOff, byte[] dest, int length, int i_12_, byte[] src) {
	if (length == 0)
	    return 0;
	length += destOff;
	int i_15_ = 0;
	int i_16_ = i_12_;
	for (;;) {
	    byte value = src[i_16_];
	    if (value >= 0)
		i_15_++;
	    else
		i_15_ = anIntArray761[i_15_];
	    int i_18_;
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (length <= destOff)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x40) == 0)
		i_15_++;
	    else
		i_15_ = anIntArray761[i_15_];
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (length <= destOff)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x20) == 0)
		i_15_++;
	    else
		i_15_ = anIntArray761[i_15_];
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (destOff >= length)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x10) != 0)
		i_15_ = anIntArray761[i_15_];
	    else
		i_15_++;
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (destOff >= length)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x8) != 0)
		i_15_ = anIntArray761[i_15_];
	    else
		i_15_++;
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (length <= destOff)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x4) == 0)
		i_15_++;
	    else
		i_15_ = anIntArray761[i_15_];
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (destOff >= length)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x2) != 0)
		i_15_ = anIntArray761[i_15_];
	    else
		i_15_++;
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (length <= destOff)
		    break;
		i_15_ = 0;
	    }
	    if ((value & 0x1) == 0)
		i_15_++;
	    else
		i_15_ = anIntArray761[i_15_];
	    if ((i_18_ = anIntArray761[i_15_]) < 0) {
		dest[destOff++] = (byte) (i_18_ ^ 0xffffffff);
		if (length <= destOff)
		    break;
		i_15_ = 0;
	    }
	    i_16_++;
	}
	return i_16_ + 1 - i_12_;
    }
    
    public int encode(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
	len += srcOff;
	int current = 0;
	int bitOffset = destOff << 3;
	for (/**/; len > srcOff; srcOff++) {
	    int value = src[srcOff] & 0xff;
	    int code = codes[value];
	    int codeLength = codeLengths[value];
	    if (code == 0)
		throw new RuntimeException("No codeword for data value " + value);
	    int byteOffset = bitOffset >> 3;
	    int localBit = bitOffset & 0x7;
	    current &= -localBit >> 31;
	    int endOffset = byteOffset + (code + (localBit - 1) >> 3);
	    localBit += 24;
	    dest[byteOffset] = (byte) (current = Queue.or(current, codeLength >>> localBit));
	    if (byteOffset < endOffset) {
		localBit -= 8;
		byteOffset++;
		dest[byteOffset] = (byte) (current = codeLength >>> localBit);
		if (endOffset > byteOffset) {
		    byteOffset++;
		    localBit -= 8;
		    dest[byteOffset] = (byte) (current = codeLength >>> localBit);
		    if (byteOffset < endOffset) {
			byteOffset++;
			localBit -= 8;
			dest[byteOffset] = (byte) (current = codeLength >>> localBit);
			if (endOffset > byteOffset) {
			    localBit -= 8;
			    byteOffset++;
			    dest[byteOffset] = (byte) (current = codeLength << -localBit);
			}
		    }
		}
	    }
	    bitOffset += code;
	}
	return -destOff + (bitOffset + 7 >> 3);
    }
    
    public static void method887(byte i) {
	Class39_Sub5_Sub11.npcDefinitionCache.method134(27392);
	Class37.aClass7_655.method134(27392);
    }
    
    public static void method888(int i) {
	if (Class39_Sub13.aClass42_1502 != null) {
	    synchronized (Class39_Sub13.aClass42_1502) {
		Class39_Sub13.aClass42_1502 = null;
	    }
	}
	if (i != 2372)
	    aLong752 = 96L;
    }
    
    public static void method889(int i, int i_31_, int i_32_, int i_33_,
				 int i_34_) {
	Class39_Sub13 class39_sub13
	    = ((Class39_Sub13)
	       Class14.aClass19_213.fetch((long) i_31_));
	if (class39_sub13 == null) {
	    class39_sub13 = new Class39_Sub13();
	    Class14.aClass19_213.put((long) i_31_, class39_sub13);
	}
	if (class39_sub13.anIntArray1505.length <= i) {
	    int[] is = new int[i + 1];
	    int[] is_35_ = new int[i + 1];
	    for (int i_36_ = 0; i_36_ < class39_sub13.anIntArray1505.length;
		 i_36_++) {
		is[i_36_] = class39_sub13.anIntArray1505[i_36_];
		is_35_[i_36_] = class39_sub13.anIntArray1504[i_36_];
	    }
	    for (int i_37_ = class39_sub13.anIntArray1505.length; i_37_ < i;
		 i_37_++) {
		is[i_37_] = -1;
		is_35_[i_37_] = 0;
	    }
	    class39_sub13.anIntArray1504 = is_35_;
	    class39_sub13.anIntArray1505 = is;
	}
	class39_sub13.anIntArray1505[i] = i_32_;
	class39_sub13.anIntArray1504[i] = i_34_;
    }
    
    public Huffmans(byte[] is) {
	int i = is.length;
	anIntArray761 = new int[8];
	codeLengths = new int[i];
	int[] is_38_ = new int[33];
	codes = is;
	int i_39_ = 0;
	for (int i_40_ = 0; i > i_40_; i_40_++) {
	    int i_41_ = is[i_40_];
	    if (i_41_ != 0) {
		int i_42_ = is_38_[i_41_];
		int i_43_ = 1 << -i_41_ + 32;
		codeLengths[i_40_] = i_42_;
		int i_44_;
		if ((i_42_ & i_43_) == 0) {
		    for (int i_45_ = i_41_ - 1; i_45_ >= 1; i_45_--) {
			int i_46_ = is_38_[i_45_];
			if (i_42_ != i_46_)
			    break;
			int i_47_ = 1 << -i_45_ + 32;
			if ((i_46_ & i_47_) == 0)
			    is_38_[i_45_] = Queue.or(i_47_, i_46_);
			else {
			    is_38_[i_45_] = is_38_[i_45_ - 1];
			    break;
			}
		    }
		    i_44_ = i_43_ | i_42_;
		} else
		    i_44_ = is_38_[i_41_ - 1];
		is_38_[i_41_] = i_44_;
		for (int i_48_ = i_41_ + 1; i_48_ <= 32; i_48_++) {
		    if (is_38_[i_48_] == i_42_)
			is_38_[i_48_] = i_44_;
		}
		int i_49_ = 0;
		for (int i_50_ = 0; i_41_ > i_50_; i_50_++) {
		    int i_51_ = -2147483648 >>> i_50_;
		    if ((i_51_ & i_42_) != 0) {
			if (anIntArray761[i_49_] == 0)
			    anIntArray761[i_49_] = i_39_;
			i_49_ = anIntArray761[i_49_];
		    } else
			i_49_++;
		    i_51_ >>>= 1;
		    if (anIntArray761.length <= i_49_) {
			int[] is_52_ = new int[anIntArray761.length * 2];
			for (int i_53_ = 0; anIntArray761.length > i_53_;
			     i_53_++)
			    is_52_[i_53_] = anIntArray761[i_53_];
			anIntArray761 = is_52_;
		    }
		}
		anIntArray761[i_49_] = i_40_ ^ 0xffffffff;
		if (i_39_ <= i_49_)
		    i_39_ = i_49_ + 1;
	    }
	}
    }
    
    public static void method890(boolean bool) {
	aClass3_751 = null;
	aClass9_760 = null;
	aClass3_753 = null;
	aClass3_745 = null;
	aClass3_758 = null;
	aClass3_746 = null;
	anIntArray762 = null;
	aClass3_747 = null;
	aClass3_750 = null;
	aClass3_763 = null;
	aClass3_755 = null;
    }
    
    static {
	anInt749 = 0;
	aLong752 = 0L;
	aClass3_746
	    = (Class39_Sub5_Sub9.createJstring
	       ("Moderator option: Mute player for 48 hours: <ON>"));
	aClass3_751 = aClass3_746;
	aClass3_753
	    = Class39_Sub5_Sub9
		  .createJstring("Enter name of friend to delete from list");
	aClass3_745
	    = Class39_Sub5_Sub9.createJstring("Bitte versuchen Sie es erneut)3");
	aClass3_758
	    = Class39_Sub5_Sub9.createJstring("Bitte versuchen Sie)1");
	anInt748 = 0;
	aClass3_763 = (Class39_Sub5_Sub9.createJstring
		       ("Bitte wenden Sie sich an den Kundendienst)3"));
	anIntArray762 = new int[256];
	aBoolean764 = false;
	frameSize = 0;
	anInt756 = 0;
	aClass3_755 = aClass3_753;
    }
}
