package unpackaged;

import jagex.world.actors.Npc;

/* Class22 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Bzip2Decompressor
{
    public static Bzip2Block aClass60_420 = new Bzip2Block();
    
    public static void method262(Bzip2Block class60) {
	boolean bool = false;
	boolean bool_0_ = false;
	boolean bool_1_ = false;
	boolean bool_2_ = false;
	boolean bool_3_ = false;
	boolean bool_4_ = false;
	boolean bool_5_ = false;
	boolean bool_6_ = false;
	boolean bool_7_ = false;
	boolean bool_8_ = false;
	boolean bool_9_ = false;
	boolean bool_10_ = false;
	boolean bool_11_ = false;
	boolean bool_12_ = false;
	boolean bool_13_ = false;
	boolean bool_14_ = false;
	boolean bool_15_ = false;
	boolean bool_16_ = false;
	int i = 0;
	int[] is = null;
	int[] is_17_ = null;
	int[] is_18_ = null;
	class60.anInt1047 = 1;
	if (Npc.anIntArray2503 == null)
	    Npc.anIntArray2503
		= new int[class60.anInt1047 * 100000];
	boolean bool_19_ = true;
	while (bool_19_) {
	    byte i_20_ = method270(class60);
	    if (i_20_ == 23)
		break;
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method270(class60);
	    i_20_ = method263(class60);
	    class60.anInt1071 = 0;
	    int i_21_ = method270(class60);
	    class60.anInt1071 = class60.anInt1071 << 8 | i_21_ & 0xff;
	    i_21_ = method270(class60);
	    class60.anInt1071 = class60.anInt1071 << 8 | i_21_ & 0xff;
	    i_21_ = method270(class60);
	    class60.anInt1071 = class60.anInt1071 << 8 | i_21_ & 0xff;
	    for (int i_22_ = 0; i_22_ < 16; i_22_++) {
		i_20_ = method263(class60);
		if (i_20_ == 1)
		    class60.aBooleanArray1048[i_22_] = true;
		else
		    class60.aBooleanArray1048[i_22_] = false;
	    }
	    for (int i_23_ = 0; i_23_ < 256; i_23_++)
		class60.aBooleanArray1064[i_23_] = false;
	    for (int i_24_ = 0; i_24_ < 16; i_24_++) {
		if (class60.aBooleanArray1048[i_24_]) {
		    for (int i_25_ = 0; i_25_ < 16; i_25_++) {
			i_20_ = method263(class60);
			if (i_20_ == 1)
			    class60.aBooleanArray1064[i_24_ * 16 + i_25_]
				= true;
		    }
		}
	    }
	    method267(class60);
	    int i_26_ = class60.anInt1079 + 2;
	    int i_27_ = method265(3, class60);
	    int i_28_ = method265(15, class60);
	    for (int i_29_ = 0; i_29_ < i_28_; i_29_++) {
		int i_30_ = 0;
		for (;;) {
		    i_20_ = method263(class60);
		    if (i_20_ == 0)
			break;
		    i_30_++;
		}
		class60.aByteArray1059[i_29_] = (byte) i_30_;
	    }
	    byte[] is_31_ = new byte[6];
	    for (byte i_32_ = 0; i_32_ < i_27_; i_32_++)
		is_31_[i_32_] = i_32_;
	    for (int i_33_ = 0; i_33_ < i_28_; i_33_++) {
		byte i_34_ = class60.aByteArray1059[i_33_];
		byte i_35_ = is_31_[i_34_];
		for (/**/; i_34_ > 0; i_34_--)
		    is_31_[i_34_] = is_31_[i_34_ - 1];
		is_31_[0] = i_35_;
		class60.aByteArray1042[i_33_] = i_35_;
	    }
	    for (int i_36_ = 0; i_36_ < i_27_; i_36_++) {
		int i_37_ = method265(5, class60);
		for (int i_38_ = 0; i_38_ < i_26_; i_38_++) {
		    for (;;) {
			i_20_ = method263(class60);
			if (i_20_ == 0)
			    break;
			i_20_ = method263(class60);
			if (i_20_ == 0)
			    i_37_++;
			else
			    i_37_--;
		    }
		    class60.aByteArrayArray1046[i_36_][i_38_] = (byte) i_37_;
		}
	    }
	    for (int i_39_ = 0; i_39_ < i_27_; i_39_++) {
		int i_40_ = 32;
		byte i_41_ = 0;
		for (int i_42_ = 0; i_42_ < i_26_; i_42_++) {
		    if (class60.aByteArrayArray1046[i_39_][i_42_] > i_41_)
			i_41_ = class60.aByteArrayArray1046[i_39_][i_42_];
		    if (class60.aByteArrayArray1046[i_39_][i_42_] < i_40_)
			i_40_ = class60.aByteArrayArray1046[i_39_][i_42_];
		}
		method264(class60.anIntArrayArray1067[i_39_],
			  class60.anIntArrayArray1070[i_39_],
			  class60.anIntArrayArray1069[i_39_],
			  class60.aByteArrayArray1046[i_39_], i_40_, i_41_,
			  i_26_);
		class60.anIntArray1075[i_39_] = i_40_;
	    }
	    int i_43_ = class60.anInt1079 + 1;
	    int i_44_ = -1;
	    int i_45_ = 0;
	    for (int i_46_ = 0; i_46_ <= 255; i_46_++)
		class60.anIntArray1044[i_46_] = 0;
	    int i_47_ = 4095;
	    for (int i_48_ = 15; i_48_ >= 0; i_48_--) {
		for (int i_49_ = 15; i_49_ >= 0; i_49_--) {
		    class60.aByteArray1038[i_47_]
			= (byte) (i_48_ * 16 + i_49_);
		    i_47_--;
		}
		class60.anIntArray1055[i_48_] = i_47_ + 1;
	    }
	    int i_50_ = 0;
	    if (i_45_ == 0) {
		i_44_++;
		i_45_ = 50;
		byte i_51_ = class60.aByteArray1042[i_44_];
		i = class60.anIntArray1075[i_51_];
		is = class60.anIntArrayArray1067[i_51_];
		is_18_ = class60.anIntArrayArray1069[i_51_];
		is_17_ = class60.anIntArrayArray1070[i_51_];
	    }
	    i_45_--;
	    int i_52_ = i;
	    int i_53_;
	    int i_54_;
	    for (i_54_ = method265(i_52_, class60); i_54_ > is[i_52_];
		 i_54_ = i_54_ << 1 | i_53_) {
		i_52_++;
		i_53_ = method263(class60);
	    }
	    int i_55_ = is_18_[i_54_ - is_17_[i_52_]];
	    while (i_55_ != i_43_) {
		if (i_55_ == 0 || i_55_ == 1) {
		    int i_56_ = -1;
		    int i_57_ = 1;
		    do {
			if (i_55_ == 0)
			    i_56_ += i_57_;
			else if (i_55_ == 1)
			    i_56_ += i_57_ * 2;
			i_57_ *= 2;
			if (i_45_ == 0) {
			    i_44_++;
			    i_45_ = 50;
			    byte i_58_ = class60.aByteArray1042[i_44_];
			    i = class60.anIntArray1075[i_58_];
			    is = class60.anIntArrayArray1067[i_58_];
			    is_18_ = class60.anIntArrayArray1069[i_58_];
			    is_17_ = class60.anIntArrayArray1070[i_58_];
			}
			i_45_--;
			i_52_ = i;
			for (i_54_ = method265(i_52_, class60);
			     i_54_ > is[i_52_]; i_54_ = i_54_ << 1 | i_53_) {
			    i_52_++;
			    i_53_ = method263(class60);
			}
			i_55_ = is_18_[i_54_ - is_17_[i_52_]];
		    } while (i_55_ == 0 || i_55_ == 1);
		    i_56_++;
		    i_21_
			= (class60.aByteArray1040
			   [(class60.aByteArray1038[class60.anIntArray1055[0]]
			     & 0xff)]);
		    class60.anIntArray1044[i_21_ & 0xff] += i_56_;
		    for (/**/; i_56_ > 0; i_56_--) {
			Npc.anIntArray2503[i_50_]
			    = i_21_ & 0xff;
			i_50_++;
		    }
		} else {
		    int i_59_ = i_55_ - 1;
		    if (i_59_ < 16) {
			int i_60_ = class60.anIntArray1055[0];
			i_20_ = class60.aByteArray1038[i_60_ + i_59_];
			for (/**/; i_59_ > 3; i_59_ -= 4) {
			    int i_61_ = i_60_ + i_59_;
			    class60.aByteArray1038[i_61_]
				= class60.aByteArray1038[i_61_ - 1];
			    class60.aByteArray1038[i_61_ - 1]
				= class60.aByteArray1038[i_61_ - 2];
			    class60.aByteArray1038[i_61_ - 2]
				= class60.aByteArray1038[i_61_ - 3];
			    class60.aByteArray1038[i_61_ - 3]
				= class60.aByteArray1038[i_61_ - 4];
			}
			for (/**/; i_59_ > 0; i_59_--)
			    class60.aByteArray1038[i_60_ + i_59_]
				= class60.aByteArray1038[i_60_ + i_59_ - 1];
			class60.aByteArray1038[i_60_] = i_20_;
		    } else {
			int i_62_ = i_59_ / 16;
			int i_63_ = i_59_ % 16;
			int i_64_ = class60.anIntArray1055[i_62_] + i_63_;
			i_20_ = class60.aByteArray1038[i_64_];
			for (/**/; i_64_ > class60.anIntArray1055[i_62_];
			     i_64_--)
			    class60.aByteArray1038[i_64_]
				= class60.aByteArray1038[i_64_ - 1];
			class60.anIntArray1055[i_62_]++;
			for (/**/; i_62_ > 0; i_62_--) {
			    class60.anIntArray1055[i_62_]--;
			    class60.aByteArray1038[(class60.anIntArray1055
						    [i_62_])]
				= (class60.aByteArray1038
				   [(class60.anIntArray1055[i_62_ - 1] + 16
				     - 1)]);
			}
			class60.anIntArray1055[0]--;
			class60.aByteArray1038[class60.anIntArray1055[0]]
			    = i_20_;
			if (class60.anIntArray1055[0] == 0) {
			    int i_65_ = 4095;
			    for (int i_66_ = 15; i_66_ >= 0; i_66_--) {
				for (int i_67_ = 15; i_67_ >= 0; i_67_--) {
				    class60.aByteArray1038[i_65_]
					= (class60.aByteArray1038
					   [(class60.anIntArray1055[i_66_]
					     + i_67_)]);
				    i_65_--;
				}
				class60.anIntArray1055[i_66_] = i_65_ + 1;
			    }
			}
		    }
		    class60.anIntArray1044
			[class60.aByteArray1040[i_20_ & 0xff] & 0xff]++;
		    Npc.anIntArray2503[i_50_]
			= class60.aByteArray1040[i_20_ & 0xff] & 0xff;
		    i_50_++;
		    if (i_45_ == 0) {
			i_44_++;
			i_45_ = 50;
			byte i_68_ = class60.aByteArray1042[i_44_];
			i = class60.anIntArray1075[i_68_];
			is = class60.anIntArrayArray1067[i_68_];
			is_18_ = class60.anIntArrayArray1069[i_68_];
			is_17_ = class60.anIntArrayArray1070[i_68_];
		    }
		    i_45_--;
		    i_52_ = i;
		    for (i_54_ = method265(i_52_, class60); i_54_ > is[i_52_];
			 i_54_ = i_54_ << 1 | i_53_) {
			i_52_++;
			i_53_ = method263(class60);
		    }
		    i_55_ = is_18_[i_54_ - is_17_[i_52_]];
		}
	    }
	    class60.anInt1053 = 0;
	    class60.aByte1080 = (byte) 0;
	    class60.anIntArray1065[0] = 0;
	    for (int i_69_ = 1; i_69_ <= 256; i_69_++)
		class60.anIntArray1065[i_69_]
		    = class60.anIntArray1044[i_69_ - 1];
	    for (int i_70_ = 1; i_70_ <= 256; i_70_++)
		class60.anIntArray1065[i_70_]
		    += class60.anIntArray1065[i_70_ - 1];
	    for (int i_71_ = 0; i_71_ < i_50_; i_71_++) {
		i_21_
		    = (byte) (Npc.anIntArray2503[i_71_]
			      & 0xff);
		Npc.anIntArray2503
		    [class60.anIntArray1065[i_21_ & 0xff]]
		    |= i_71_ << 8;
		class60.anIntArray1065[i_21_ & 0xff]++;
	    }
	    class60.anInt1060 = (Npc.anIntArray2503
				 [class60.anInt1071]) >> 8;
	    class60.anInt1066 = 0;
	    class60.anInt1060 = (Npc.anIntArray2503
				 [class60.anInt1060]);
	    class60.anInt1061 = (byte) (class60.anInt1060 & 0xff);
	    class60.anInt1060 >>= 8;
	    class60.anInt1066++;
	    class60.anInt1073 = i_50_;
	    method268(class60);
	    if (class60.anInt1066 == class60.anInt1073 + 1
		&& class60.anInt1053 == 0)
		bool_19_ = true;
	    else
		bool_19_ = false;
	}
    }
    
    public static byte method263(Bzip2Block class60) {
	return (byte) method265(1, class60);
    }
    
    public static void method264(int[] is, int[] is_72_, int[] is_73_,
				 byte[] is_74_, int i, int i_75_, int i_76_) {
	int i_77_ = 0;
	for (int i_78_ = i; i_78_ <= i_75_; i_78_++) {
	    for (int i_79_ = 0; i_79_ < i_76_; i_79_++) {
		if (is_74_[i_79_] == i_78_) {
		    is_73_[i_77_] = i_79_;
		    i_77_++;
		}
	    }
	}
	for (int i_80_ = 0; i_80_ < 23; i_80_++)
	    is_72_[i_80_] = 0;
	for (int i_81_ = 0; i_81_ < i_76_; i_81_++)
	    is_72_[is_74_[i_81_] + 1]++;
	for (int i_82_ = 1; i_82_ < 23; i_82_++)
	    is_72_[i_82_] += is_72_[i_82_ - 1];
	for (int i_83_ = 0; i_83_ < 23; i_83_++)
	    is[i_83_] = 0;
	int i_84_ = 0;
	for (int i_85_ = i; i_85_ <= i_75_; i_85_++) {
	    i_84_ += is_72_[i_85_ + 1] - is_72_[i_85_];
	    is[i_85_] = i_84_ - 1;
	    i_84_ <<= 1;
	}
	for (int i_86_ = i + 1; i_86_ <= i_75_; i_86_++)
	    is_72_[i_86_] = (is[i_86_ - 1] + 1 << 1) - is_72_[i_86_];
    }
    
    public static int method265(int i, Bzip2Block class60) {
	int i_87_;
	for (;;) {
	    if (class60.anInt1052 >= i) {
		int i_88_ = (class60.anInt1039 >> class60.anInt1052 - i
			     & (1 << i) - 1);
		class60.anInt1052 -= i;
		i_87_ = i_88_;
		break;
	    }
	    class60.anInt1039
		= (class60.anInt1039 << 8
		   | class60.aByteArray1057[class60.anInt1077] & 0xff);
	    class60.anInt1052 += 8;
	    class60.anInt1077++;
	    class60.anInt1045++;
	}
	return i_87_;
    }
    
    public static void method266() {
	aClass60_420 = null;
    }
    
    public static void method267(Bzip2Block class60) {
	class60.anInt1079 = 0;
	for (int i = 0; i < 256; i++) {
	    if (class60.aBooleanArray1064[i]) {
		class60.aByteArray1040[class60.anInt1079] = (byte) i;
		class60.anInt1079++;
	    }
	}
    }
    
    public static void method268(Bzip2Block class60) {
	byte i = class60.aByte1080;
	int i_89_ = class60.anInt1053;
	int i_90_ = class60.anInt1066;
	int i_91_ = class60.anInt1061;
	int[] is = Npc.anIntArray2503;
	int i_92_ = class60.anInt1060;
	byte[] is_93_ = class60.aByteArray1062;
	int i_94_ = class60.anInt1076;
	int i_95_ = class60.anInt1068;
	int i_96_ = i_95_;
	int i_97_ = class60.anInt1073 + 1;
    while_8_:
	for (;;) {
	    if (i_89_ > 0) {
		for (;;) {
		    if (i_95_ == 0)
			break while_8_;
		    if (i_89_ == 1)
			break;
		    is_93_[i_94_] = i;
		    i_89_--;
		    i_94_++;
		    i_95_--;
		}
		if (i_95_ == 0) {
		    i_89_ = 1;
		    break;
		}
		is_93_[i_94_] = i;
		i_94_++;
		i_95_--;
	    }
	    boolean bool = true;
	    while (bool) {
		bool = false;
		if (i_90_ == i_97_) {
		    i_89_ = 0;
		    break while_8_;
		}
		i = (byte) i_91_;
		i_92_ = is[i_92_];
		int i_98_ = (byte) (i_92_ & 0xff);
		i_92_ >>= 8;
		i_90_++;
		if (i_98_ != i_91_) {
		    i_91_ = i_98_;
		    if (i_95_ == 0) {
			i_89_ = 1;
			break while_8_;
		    }
		    is_93_[i_94_] = i;
		    i_94_++;
		    i_95_--;
		    bool = true;
		} else if (i_90_ == i_97_) {
		    if (i_95_ == 0) {
			i_89_ = 1;
			break while_8_;
		    }
		    is_93_[i_94_] = i;
		    i_94_++;
		    i_95_--;
		    bool = true;
		}
	    }
	    i_89_ = 2;
	    i_92_ = is[i_92_];
	    int i_99_ = (byte) (i_92_ & 0xff);
	    i_92_ >>= 8;
	    if (++i_90_ != i_97_) {
		if (i_99_ != i_91_)
		    i_91_ = i_99_;
		else {
		    i_89_ = 3;
		    i_92_ = is[i_92_];
		    i_99_ = (byte) (i_92_ & 0xff);
		    i_92_ >>= 8;
		    if (++i_90_ != i_97_) {
			if (i_99_ != i_91_)
			    i_91_ = i_99_;
			else {
			    i_92_ = is[i_92_];
			    i_99_ = (byte) (i_92_ & 0xff);
			    i_92_ >>= 8;
			    i_90_++;
			    i_89_ = (i_99_ & 0xff) + 4;
			    i_92_ = is[i_92_];
			    i_91_ = (byte) (i_92_ & 0xff);
			    i_92_ >>= 8;
			    i_90_++;
			}
		    }
		}
	    }
	}
	int i_100_ = class60.anInt1043;
	class60.anInt1043 += i_96_ - i_95_;
	class60.aByte1080 = i;
	class60.anInt1053 = i_89_;
	class60.anInt1066 = i_90_;
	class60.anInt1061 = i_91_;
	Npc.anIntArray2503 = is;
	class60.anInt1060 = i_92_;
	class60.aByteArray1062 = is_93_;
	class60.anInt1076 = i_94_;
	class60.anInt1068 = i_95_;
    }
    
    public static int method269(byte[] is, int i, byte[] is_101_, int i_102_,
				int i_103_) {
	synchronized (aClass60_420) {
	    aClass60_420.aByteArray1057 = is_101_;
	    aClass60_420.anInt1077 = i_103_;
	    aClass60_420.aByteArray1062 = is;
	    aClass60_420.anInt1076 = 0;
	    aClass60_420.anInt1068 = i;
	    aClass60_420.anInt1052 = 0;
	    aClass60_420.anInt1039 = 0;
	    aClass60_420.anInt1045 = 0;
	    aClass60_420.anInt1043 = 0;
	    method262(aClass60_420);
	    i -= aClass60_420.anInt1068;
	    aClass60_420.aByteArray1057 = null;
	    aClass60_420.aByteArray1062 = null;
	    return i;
	}
    }
    
    public static byte method270(Bzip2Block class60) {
	return (byte) method265(8, class60);
    }
}
