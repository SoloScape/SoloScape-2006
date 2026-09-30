package unpackaged;

/* InputStream_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.InputStream;

public class InputStream_Sub1 extends InputStream
{
    public byte[] aByteArray25 = new byte[65536];
    public int[] anIntArray26
	= { 0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4,
	    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
	    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6,
	    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
	    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
	    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
    public boolean aBoolean27;
    public int[] anIntArray28 = new int[256];
    
    public int read() {
	byte[] is = new byte[1];
	read(is, 0, 1);
	return is[0];
    }
    
    public synchronized int read(byte[] is, int i, int i_0_) {
	try {
	    if (aBoolean27)
		return -1;
	    if (i_0_ > 256) {
		read(is, i, 256);
		read(is, i + 256, i_0_ - 256);
		return i_0_;
	    }
	    Class13.method190(anIntArray28, i_0_);
	    for (int i_1_ = 0; i_1_ < i_0_; i_1_++) {
		int i_2_ = anIntArray28[i_1_];
		if ((i_2_ + 8388608 & ~0xffffff) != 0)
		    anIntArray28[i_1_] = 0x7fffff ^ i_2_ >> 31;
	    }
	    method43(aByteArray25, anIntArray28, is, 0, i, i_0_);
	    return i_0_;
	} catch (Exception exception) {
	    aBoolean27 = true;
	    Class39_Sub7.method849(exception, 64, null);
	    return -1;
	}
    }
    
    public static void method43(byte[] is, int[] is_3_, byte[] is_4_, int i,
				int i_5_, int i_6_) {
	for (i = 0; i < i_6_; i++)
	    is_4_[i_5_++] = is[(is_3_[i] >> 8) + 32768];
    }
    
    public byte method44(int i) {
	int i_7_ = i >> 8 & 0x80;
	if (i_7_ != 0)
	    i = -i;
	if (i > 32635)
	    i = 32635;
	i += 132;
	int i_8_ = anIntArray26[i >> 7 & 0xff];
	int i_9_ = i >> i_8_ + 3 & 0xf;
	byte i_10_ = (byte) ((i_7_ | i_8_ << 4 | i_9_) ^ 0xffffffff);
	return i_10_;
    }
    
    public InputStream_Sub1() {
	for (int i = -32768; i < 32768; i++)
	    aByteArray25[i + 32768] = method44(i);
    }
}
