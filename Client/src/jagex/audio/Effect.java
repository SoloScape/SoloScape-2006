package jagex.audio;

import jagex.io.Buffer;
import unpackaged.Class39_Sub3_Sub1;
import unpackaged.FileTable;

/* Class52 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Effect
{
    public int anInt949;
    public int anInt950;
    public Instrument[] instruments;
    
    public byte[] method978() {
	int i = 0;
	for (int i_0_ = 0; i_0_ < 10; i_0_++) {
	    if (instruments[i_0_] != null
		&& (instruments[i_0_].anInt1015
		    + instruments[i_0_].anInt1019) > i)
		i = (instruments[i_0_].anInt1015
		     + instruments[i_0_].anInt1019);
	}
	if (i == 0)
	    return new byte[0];
	int i_1_ = i * 22050 / 1000;
	byte[] is = new byte[i_1_];
	for (int i_2_ = 0; i_2_ < 10; i_2_++) {
	    if (instruments[i_2_] != null) {
		int i_3_ = instruments[i_2_].anInt1015 * 22050 / 1000;
		int i_4_ = instruments[i_2_].anInt1019 * 22050 / 1000;
		int[] is_5_
		    = instruments[i_2_]
			  .method1030(i_3_, instruments[i_2_].anInt1015);
		for (int i_6_ = 0; i_6_ < i_3_; i_6_++) {
		    int i_7_ = is[i_6_ + i_4_] + (is_5_[i_6_] >> 8);
		    if ((i_7_ + 128 & ~0xff) != 0)
			i_7_ = i_7_ >> 31 ^ 0x7f;
		    is[i_6_ + i_4_] = (byte) i_7_;
		}
	    }
	}
	return is;
    }
    
    public int method979() {
	int i = 9999999;
	for (int i_8_ = 0; i_8_ < 10; i_8_++) {
	    if (instruments[i_8_] != null
		&& instruments[i_8_].anInt1019 / 20 < i)
		i = instruments[i_8_].anInt1019 / 20;
	}
	if (anInt950 < anInt949 && anInt950 / 20 < i)
	    i = anInt950 / 20;
	if (i == 9999999 || i == 0)
	    return 0;
	for (int i_9_ = 0; i_9_ < 10; i_9_++) {
	    if (instruments[i_9_] != null)
		instruments[i_9_].anInt1019 -= i * 20;
	}
	if (anInt950 < anInt949) {
	    anInt950 -= i * 20;
	    anInt949 -= i * 20;
	}
	return i;
    }
    
    public static Effect createEffect(FileTable fileTable, int archiveId, int childId) {
	byte[] src = fileTable.lookupFile(archiveId, childId);
	if (src == null)
	    return null;
	return new Effect(new Buffer(src));
    }
    
    public Effect(Buffer buffer) {
	instruments = new Instrument[10];
	for (int i = 0; i < 10; i++) {
	    int i_11_ = buffer.getUbyte();
	    if (i_11_ != 0) {
		buffer.offset--;
		instruments[i] = new Instrument();
		instruments[i].decode(buffer);
	    }
	}
	anInt950 = buffer.getUword();
	anInt949 = buffer.getUword();
    }
    
    public Effect() {
	instruments = new Instrument[10];
    }
    
    public Class39_Sub3_Sub1 method981() {
	byte[] is = method978();
	return new Class39_Sub3_Sub1(22050, is, anInt950 * 22050 / 1000,
				     anInt949 * 22050 / 1000);
    }
}
