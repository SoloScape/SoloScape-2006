package unpackaged;

import jagex.world.actors.StillGraphic;

/* Class39_Sub1_Sub2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub1_Sub2 extends Class39_Sub1
{
    public boolean aBoolean1622;
    public int anInt1623;
    public int anInt1624;
    public int anInt1625;
    public int anInt1626;
    public int anInt1627;
    public int anInt1628;
    public int anInt1629;
    public int anInt1630;
    public int anInt1631;
    public int anInt1632;
    public int anInt1633;
    public int anInt1634;
    public int anInt1635;
    
    public static int method426
	(int i, byte[] is, int[] is_0_, int i_1_, int i_2_, int i_3_, int i_4_,
	 int i_5_, int i_6_, int i_7_, int i_8_, int i_9_, int i_10_,
	 Class39_Sub1_Sub2 class39_sub1_sub2) {
	i_1_ >>= 8;
	i_10_ >>= 8;
	i_3_ <<= 8;
	i_4_ <<= 8;
	i_6_ <<= 8;
	i_7_ <<= 8;
	if ((i_8_ = i_2_ + i_10_ - i_1_) > i_9_)
	    i_8_ = i_9_;
	i_2_ <<= 1;
	i_8_ <<= 1;
	i_8_ -= 6;
	while (i_2_ < i_8_) {
	    i = is[i_1_++];
	    is_0_[i_2_++] += i * i_3_ >> i_5_;
	    i_3_ += i_6_;
	    is_0_[i_2_++] += i * i_4_ >> i_5_;
	    i_4_ += i_7_;
	    i = is[i_1_++];
	    is_0_[i_2_++] += i * i_3_ >> i_5_;
	    i_3_ += i_6_;
	    is_0_[i_2_++] += i * i_4_ >> i_5_;
	    i_4_ += i_7_;
	    i = is[i_1_++];
	    is_0_[i_2_++] += i * i_3_ >> i_5_;
	    i_3_ += i_6_;
	    is_0_[i_2_++] += i * i_4_ >> i_5_;
	    i_4_ += i_7_;
	    i = is[i_1_++];
	    is_0_[i_2_++] += i * i_3_ >> i_5_;
	    i_3_ += i_6_;
	    is_0_[i_2_++] += i * i_4_ >> i_5_;
	    i_4_ += i_7_;
	}
	i_8_ += 6;
	while (i_2_ < i_8_) {
	    i = is[i_1_++];
	    is_0_[i_2_++] += i * i_3_ >> i_5_;
	    i_3_ += i_6_;
	    is_0_[i_2_++] += i * i_4_ >> i_5_;
	    i_4_ += i_7_;
	}
	class39_sub1_sub2.anInt1635 = i_3_ >> 8;
	class39_sub1_sub2.anInt1634 = i_4_ >> 8;
	class39_sub1_sub2.anInt1630 = i_1_ << 8;
	return i_2_ >> 1;
    }
    
    public static int method427
	(int i, int i_11_, byte[] is, int[] is_12_, int i_13_, int i_14_,
	 int i_15_, int i_16_, int i_17_, int i_18_, int i_19_, int i_20_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_21_, int i_22_) {
	if (i_21_ == 0
	    || (i_18_ = i_14_ + (i_20_ + 256 - i_13_ + i_21_) / i_21_) > i_19_)
	    i_18_ = i_19_;
	while (i_14_ < i_18_) {
	    i_11_ = i_13_ >> 8;
	    i = is[i_11_ - 1];
	    is_12_[i_14_++]
		+= (((i << 8) + (is[i_11_] - i) * (i_13_ & 0xff)) * i_15_
		    >> i_16_);
	    i_15_ += i_17_;
	    i_13_ += i_21_;
	}
	if (i_21_ == 0
	    || (i_18_ = i_14_ + (i_20_ - i_13_ + i_21_) / i_21_) > i_19_)
	    i_18_ = i_19_;
	i = i_22_;
	i_11_ = i_21_;
	while (i_14_ < i_18_) {
	    is_12_[i_14_++]
		+= (((i << 8) + (is[i_13_ >> 8] - i) * (i_13_ & 0xff)) * i_15_
		    >> i_16_);
	    i_15_ += i_17_;
	    i_13_ += i_11_;
	}
	class39_sub1_sub2.anInt1635 = i_15_;
	class39_sub1_sub2.anInt1630 = i_13_;
	return i_14_;
    }
    
    public static int method428(int i, byte[] is, int[] is_23_, int i_24_,
				int i_25_, int i_26_, int i_27_, int i_28_,
				int i_29_, int i_30_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i_24_ >>= 8;
	i_30_ >>= 8;
	i_26_ <<= 8;
	i_27_ <<= 8;
	if ((i_28_ = i_25_ + i_24_ - (i_30_ - 1)) > i_29_)
	    i_28_ = i_29_;
	i_25_ <<= 1;
	i_28_ <<= 1;
	i_28_ -= 6;
	while (i_25_ < i_28_) {
	    i = is[i_24_--];
	    is_23_[i_25_++] += i * i_26_;
	    is_23_[i_25_++] += i * i_27_;
	    i = is[i_24_--];
	    is_23_[i_25_++] += i * i_26_;
	    is_23_[i_25_++] += i * i_27_;
	    i = is[i_24_--];
	    is_23_[i_25_++] += i * i_26_;
	    is_23_[i_25_++] += i * i_27_;
	    i = is[i_24_--];
	    is_23_[i_25_++] += i * i_26_;
	    is_23_[i_25_++] += i * i_27_;
	}
	i_28_ += 6;
	while (i_25_ < i_28_) {
	    i = is[i_24_--];
	    is_23_[i_25_++] += i * i_26_;
	    is_23_[i_25_++] += i * i_27_;
	}
	class39_sub1_sub2.anInt1630 = i_24_ << 8;
	return i_25_ >> 1;
    }
    
    public int method429() {
	if (anInt1634 < 0)
	    return anInt1635;
	return (int) (((double) anInt1635
		       * Math.sqrt((double) (256 - anInt1634) * 0.0078125))
		      + 0.5);
    }
    
    public int method430() {
	if (anInt1634 < 0)
	    return -anInt1635;
	return (int) (((double) anInt1635
		       * Math.sqrt((double) anInt1634 * 0.0078125))
		      + 0.5);
    }
    
    public synchronized void method417(int i) {
	if (!method451()) {
	    if (anInt1629 > 0) {
		if (i >= anInt1629) {
		    if (anInt1623 == -2147483648) {
			anInt1635 = 0;
			this.unlinkDeque();
			i = anInt1629;
		    } else
			anInt1635 = anInt1623;
		    anInt1634 = anInt1628;
		    anInt1629 = 0;
		} else {
		    anInt1635 += anInt1631 * i;
		    if (StillGraphic.aBoolean2342)
			anInt1634 += anInt1627 * i;
		    anInt1629 -= i;
		}
	    }
	    anInt1630 += anInt1626 * i;
	    Class39_Sub3_Sub1 class39_sub3_sub1
		= (Class39_Sub3_Sub1) aClass39_Sub3_1320;
	    int i_31_ = anInt1624 << 8;
	    int i_32_ = anInt1625 << 8;
	    int i_33_ = class39_sub3_sub1.aByteArray1639.length << 8;
	    int i_34_ = i_32_ - i_31_;
	    if (i_34_ <= 0)
		anInt1633 = 0;
	    if (anInt1633 < 0) {
		if (aBoolean1622) {
		    if (anInt1626 < 0) {
			if (anInt1630 >= i_31_)
			    return;
			anInt1630 = i_31_ + i_31_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
		    }
		    while (anInt1630 >= i_32_) {
			anInt1630 = i_32_ + i_32_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
			if (anInt1630 >= i_31_)
			    break;
			anInt1630 = i_31_ + i_31_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
		    }
		} else if (anInt1626 < 0) {
		    if (anInt1630 < i_31_)
			anInt1630
			    = i_32_ - 1 - (i_32_ - 1 - anInt1630) % i_34_;
		} else if (anInt1630 >= i_32_)
		    anInt1630 = i_31_ + (anInt1630 - i_31_) % i_34_;
	    } else {
		do {
		    if (anInt1633 > 0) {
			if (aBoolean1622) {
			    if (anInt1626 < 0) {
				if (anInt1630 >= i_31_)
				    return;
				anInt1630 = i_31_ + i_31_ - 1 - anInt1630;
				anInt1626 = -anInt1626;
				if (--anInt1633 == 0)
				    break;
			    }
			    do {
				if (anInt1630 < i_32_)
				    return;
				anInt1630 = i_32_ + i_32_ - 1 - anInt1630;
				anInt1626 = -anInt1626;
				if (--anInt1633 == 0)
				    break;
				if (anInt1630 >= i_31_)
				    return;
				anInt1630 = i_31_ + i_31_ - 1 - anInt1630;
				anInt1626 = -anInt1626;
			    } while (--anInt1633 != 0);
			} else if (anInt1626 < 0) {
			    if (anInt1630 >= i_31_)
				return;
			    int i_35_ = (i_32_ - 1 - anInt1630) / i_34_;
			    if (i_35_ >= anInt1633) {
				anInt1630 += i_34_ * anInt1633;
				anInt1633 = 0;
			    } else {
				anInt1630 += i_34_ * i_35_;
				anInt1633 -= i_35_;
				return;
			    }
			} else {
			    if (anInt1630 < i_32_)
				return;
			    int i_36_ = (anInt1630 - i_31_) / i_34_;
			    if (i_36_ >= anInt1633) {
				anInt1630 -= i_34_ * anInt1633;
				anInt1633 = 0;
			    } else {
				anInt1630 -= i_34_ * i_36_;
				anInt1633 -= i_36_;
				return;
			    }
			}
		    }
		} while (false);
		if (anInt1626 < 0) {
		    if (anInt1630 < 0) {
			anInt1630 = 0;
			this.unlinkDeque();
		    }
		} else if (anInt1630 >= i_33_) {
		    anInt1630 = i_33_ - 1;
		    this.unlinkDeque();
		}
	    }
	}
    }
    
    public static int method431
	(int i, byte[] is, int[] is_37_, int i_38_, int i_39_, int i_40_,
	 int i_41_, int i_42_, int i_43_, int i_44_, int i_45_, int i_46_,
	 int i_47_, Class39_Sub1_Sub2 class39_sub1_sub2) {
	i_38_ >>= 8;
	i_47_ >>= 8;
	i_40_ <<= 8;
	i_41_ <<= 8;
	i_43_ <<= 8;
	i_44_ <<= 8;
	if ((i_45_ = i_39_ + i_38_ - (i_47_ - 1)) > i_46_)
	    i_45_ = i_46_;
	i_39_ <<= 1;
	i_45_ <<= 1;
	i_45_ -= 6;
	while (i_39_ < i_45_) {
	    i = is[i_38_--];
	    is_37_[i_39_++] += i * i_40_ >> i_42_;
	    i_40_ += i_43_;
	    is_37_[i_39_++] += i * i_41_ >> i_42_;
	    i_41_ += i_44_;
	    i = is[i_38_--];
	    is_37_[i_39_++] += i * i_40_ >> i_42_;
	    i_40_ += i_43_;
	    is_37_[i_39_++] += i * i_41_ >> i_42_;
	    i_41_ += i_44_;
	    i = is[i_38_--];
	    is_37_[i_39_++] += i * i_40_ >> i_42_;
	    i_40_ += i_43_;
	    is_37_[i_39_++] += i * i_41_ >> i_42_;
	    i_41_ += i_44_;
	    i = is[i_38_--];
	    is_37_[i_39_++] += i * i_40_ >> i_42_;
	    i_40_ += i_43_;
	    is_37_[i_39_++] += i * i_41_ >> i_42_;
	    i_41_ += i_44_;
	}
	i_45_ += 6;
	while (i_39_ < i_45_) {
	    i = is[i_38_--];
	    is_37_[i_39_++] += i * i_40_ >> i_42_;
	    i_40_ += i_43_;
	    is_37_[i_39_++] += i * i_41_ >> i_42_;
	    i_41_ += i_44_;
	}
	class39_sub1_sub2.anInt1635 = i_40_ >> 8;
	class39_sub1_sub2.anInt1634 = i_41_ >> 8;
	class39_sub1_sub2.anInt1630 = i_38_ << 8;
	return i_39_ >> 1;
    }
    
    public static int method432
	(int i, int i_48_, byte[] is, int[] is_49_, int i_50_, int i_51_,
	 int i_52_, int i_53_, int i_54_, int i_55_, int i_56_, int i_57_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_58_, int i_59_) {
	if (i_58_ == 0
	    || (i_55_ = i_51_ + (i_57_ - i_50_ + i_58_ - 257) / i_58_) > i_56_)
	    i_55_ = i_56_;
	while (i_51_ < i_55_) {
	    i_48_ = i_50_ >> 8;
	    i = is[i_48_];
	    is_49_[i_51_++]
		+= (((i << 8) + (is[i_48_ + 1] - i) * (i_50_ & 0xff)) * i_52_
		    >> i_53_);
	    i_52_ += i_54_;
	    i_50_ += i_58_;
	}
	if (i_58_ == 0
	    || (i_55_ = i_51_ + (i_57_ - i_50_ + i_58_ - 1) / i_58_) > i_56_)
	    i_55_ = i_56_;
	i_48_ = i_59_;
	while (i_51_ < i_55_) {
	    i = is[i_50_ >> 8];
	    is_49_[i_51_++]
		+= ((i << 8) + (i_48_ - i) * (i_50_ & 0xff)) * i_52_ >> i_53_;
	    i_52_ += i_54_;
	    i_50_ += i_58_;
	}
	class39_sub1_sub2.anInt1635 = i_52_;
	class39_sub1_sub2.anInt1630 = i_50_;
	return i_51_;
    }
    
    public synchronized void method433(int i, int i_60_) {
	anInt1635 = i;
	anInt1634 = i_60_;
	anInt1629 = 0;
    }
    
    public static int method434(byte[] is, int[] is_61_, int i, int i_62_,
				int i_63_, int i_64_, int i_65_, int i_66_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i >>= 8;
	i_66_ >>= 8;
	i_63_ <<= 8;
	if ((i_64_ = i_62_ + i - (i_66_ - 1)) > i_65_)
	    i_64_ = i_65_;
	i_64_ -= 3;
	while (i_62_ < i_64_) {
	    is_61_[i_62_++] += is[i--] * i_63_;
	    is_61_[i_62_++] += is[i--] * i_63_;
	    is_61_[i_62_++] += is[i--] * i_63_;
	    is_61_[i_62_++] += is[i--] * i_63_;
	}
	i_64_ += 3;
	while (i_62_ < i_64_)
	    is_61_[i_62_++] += is[i--] * i_63_;
	class39_sub1_sub2.anInt1630 = i << 8;
	return i_62_;
    }
    
    public int method435(int[] is, int i, int i_67_, int i_68_, int i_69_) {
	if (anInt1629 > 0) {
	    int i_70_ = i + anInt1629;
	    if (i_70_ > i_68_)
		i_70_ = i_68_;
	    anInt1629 += i;
	    if (anInt1626 == -256 && (anInt1630 & 0xff) == 0) {
		if (StillGraphic.aBoolean2342)
		    i = method431(0,
				  (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				   .aByteArray1639),
				  is, anInt1630, i, anInt1635, anInt1634,
				  anInt1632, anInt1631, anInt1627, 0, i_70_,
				  i_67_, this);
		else
		    i = method442((((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				   .aByteArray1639),
				  is, anInt1630, i, anInt1635, anInt1632,
				  anInt1631, 0, i_70_, i_67_, this);
	    } else if (StillGraphic.aBoolean2342)
		i = method436(0, 0,
			      (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			       .aByteArray1639),
			      is, anInt1630, i, anInt1635, anInt1634,
			      anInt1632, anInt1631, anInt1627, 0, i_70_, i_67_,
			      this, anInt1626, i_69_);
	    else
		i = method427(0, 0,
			      (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			       .aByteArray1639),
			      is, anInt1630, i, anInt1635, anInt1632,
			      anInt1631, 0, i_70_, i_67_, this, anInt1626,
			      i_69_);
	    anInt1629 -= i;
	    if (anInt1629 != 0)
		return i;
	    anInt1634 = anInt1628;
	    if (anInt1623 == -2147483648) {
		anInt1635 = 0;
		this.unlinkDeque();
		return i_68_;
	    }
	    anInt1635 = anInt1623;
	}
	if (anInt1626 == -256 && (anInt1630 & 0xff) == 0) {
	    if (StillGraphic.aBoolean2342)
		return method428(0,
				 (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				  .aByteArray1639),
				 is, anInt1630, i, method429(), method430(), 0,
				 i_68_, i_67_, this);
	    return method434((((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			      .aByteArray1639),
			     is, anInt1630, i, anInt1635, 0, i_68_, i_67_,
			     this);
	}
	if (StillGraphic.aBoolean2342)
	    return method450(0, 0,
			     (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			      .aByteArray1639),
			     is, anInt1630, i, method429(), method430(), 0,
			     i_68_, i_67_, this, anInt1626, i_69_);
	return method449(0, 0,
			 (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			  .aByteArray1639),
			 is, anInt1630, i, anInt1635, 0, i_68_, i_67_, this,
			 anInt1626, i_69_);
    }
    
    public int method416() {
	int i = anInt1635 * 3;
	i = (i ^ i >> 31) + (i >>> 31);
	if (anInt1633 == 0)
	    i -= i * anInt1630 / ((((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				   .aByteArray1639).length
				  << 8);
	else if (anInt1633 >= 0)
	    i -= i * anInt1624 / (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				  .aByteArray1639).length;
	if (i > 255)
	    return 255;
	return i;
    }
    
    public static int method436
	(int i, int i_71_, byte[] is, int[] is_72_, int i_73_, int i_74_,
	 int i_75_, int i_76_, int i_77_, int i_78_, int i_79_, int i_80_,
	 int i_81_, int i_82_, Class39_Sub1_Sub2 class39_sub1_sub2, int i_83_,
	 int i_84_) {
	if (i_83_ == 0
	    || (i_80_ = i_74_ + (i_82_ + 256 - i_73_ + i_83_) / i_83_) > i_81_)
	    i_80_ = i_81_;
	i_74_ <<= 1;
	i_80_ <<= 1;
	while (i_74_ < i_80_) {
	    i_71_ = i_73_ >> 8;
	    i = is[i_71_ - 1];
	    i = (i << 8) + (is[i_71_] - i) * (i_73_ & 0xff);
	    is_72_[i_74_++] += i * i_75_ >> i_77_;
	    i_75_ += i_78_;
	    is_72_[i_74_++] += i * i_76_ >> i_77_;
	    i_76_ += i_79_;
	    i_73_ += i_83_;
	}
	if (i_83_ == 0
	    || ((i_80_ = (i_74_ >> 1) + (i_82_ - i_73_ + i_83_) / i_83_)
		> i_81_))
	    i_80_ = i_81_;
	i_80_ <<= 1;
	i_71_ = i_84_;
	while (i_74_ < i_80_) {
	    i = (i_71_ << 8) + (is[i_73_ >> 8] - i_71_) * (i_73_ & 0xff);
	    is_72_[i_74_++] += i * i_75_ >> i_77_;
	    i_75_ += i_78_;
	    is_72_[i_74_++] += i * i_76_ >> i_77_;
	    i_76_ += i_79_;
	    i_73_ += i_83_;
	}
	class39_sub1_sub2.anInt1635 = i_75_;
	class39_sub1_sub2.anInt1634 = i_76_;
	class39_sub1_sub2.anInt1630 = i_73_;
	return i_74_ >> 1;
    }
    
    public boolean method437() {
	return this.isLinked();
    }
    
    public int method438(int[] is, int i, int i_85_, int i_86_, int i_87_) {
	if (anInt1629 > 0) {
	    int i_88_ = i + anInt1629;
	    if (i_88_ > i_86_)
		i_88_ = i_86_;
	    anInt1629 += i;
	    if (anInt1626 == 256 && (anInt1630 & 0xff) == 0) {
		if (StillGraphic.aBoolean2342)
		    i = method426(0,
				  (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				   .aByteArray1639),
				  is, anInt1630, i, anInt1635, anInt1634,
				  anInt1632, anInt1631, anInt1627, 0, i_88_,
				  i_85_, this);
		else
		    i = method441((((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				   .aByteArray1639),
				  is, anInt1630, i, anInt1635, anInt1632,
				  anInt1631, 0, i_88_, i_85_, this);
	    } else if (StillGraphic.aBoolean2342)
		i = method452(0, 0,
			      (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			       .aByteArray1639),
			      is, anInt1630, i, anInt1635, anInt1634,
			      anInt1632, anInt1631, anInt1627, 0, i_88_, i_85_,
			      this, anInt1626, i_87_);
	    else
		i = method432(0, 0,
			      (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			       .aByteArray1639),
			      is, anInt1630, i, anInt1635, anInt1632,
			      anInt1631, 0, i_88_, i_85_, this, anInt1626,
			      i_87_);
	    anInt1629 -= i;
	    if (anInt1629 != 0)
		return i;
	    anInt1634 = anInt1628;
	    if (anInt1623 == -2147483648) {
		anInt1635 = 0;
		this.unlinkDeque();
		return i_86_;
	    }
	    anInt1635 = anInt1623;
	}
	if (anInt1626 == 256 && (anInt1630 & 0xff) == 0) {
	    if (StillGraphic.aBoolean2342)
		return method440(0,
				 (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
				  .aByteArray1639),
				 is, anInt1630, i, method429(), method430(), 0,
				 i_86_, i_85_, this);
	    return method445((((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			      .aByteArray1639),
			     is, anInt1630, i, anInt1635, 0, i_86_, i_85_,
			     this);
	}
	if (StillGraphic.aBoolean2342)
	    return method439(0, 0,
			     (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			      .aByteArray1639),
			     is, anInt1630, i, method429(), method430(), 0,
			     i_86_, i_85_, this, anInt1626, i_87_);
	return method446(0, 0,
			 (((Class39_Sub3_Sub1) aClass39_Sub3_1320)
			  .aByteArray1639),
			 is, anInt1630, i, anInt1635, 0, i_86_, i_85_, this,
			 anInt1626, i_87_);
    }
    
    public static int method439
	(int i, int i_89_, byte[] is, int[] is_90_, int i_91_, int i_92_,
	 int i_93_, int i_94_, int i_95_, int i_96_, int i_97_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_98_, int i_99_) {
	if (i_98_ == 0
	    || (i_95_ = i_92_ + (i_97_ - i_91_ + i_98_ - 257) / i_98_) > i_96_)
	    i_95_ = i_96_;
	i_92_ <<= 1;
	i_95_ <<= 1;
	while (i_92_ < i_95_) {
	    i_89_ = i_91_ >> 8;
	    i = is[i_89_];
	    i = (i << 8) + (is[i_89_ + 1] - i) * (i_91_ & 0xff);
	    is_90_[i_92_++] += i * i_93_;
	    is_90_[i_92_++] += i * i_94_;
	    i_91_ += i_98_;
	}
	if (i_98_ == 0
	    || ((i_95_ = (i_92_ >> 1) + (i_97_ - i_91_ + i_98_ - 1) / i_98_)
		> i_96_))
	    i_95_ = i_96_;
	i_95_ <<= 1;
	i_89_ = i_99_;
	while (i_92_ < i_95_) {
	    i = is[i_91_ >> 8];
	    i = (i << 8) + (i_89_ - i) * (i_91_ & 0xff);
	    is_90_[i_92_++] += i * i_93_;
	    is_90_[i_92_++] += i * i_94_;
	    i_91_ += i_98_;
	}
	class39_sub1_sub2.anInt1630 = i_91_;
	return i_92_ >> 1;
    }
    
    public static int method440(int i, byte[] is, int[] is_100_, int i_101_,
				int i_102_, int i_103_, int i_104_, int i_105_,
				int i_106_, int i_107_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i_101_ >>= 8;
	i_107_ >>= 8;
	i_103_ <<= 8;
	i_104_ <<= 8;
	if ((i_105_ = i_102_ + i_107_ - i_101_) > i_106_)
	    i_105_ = i_106_;
	i_102_ <<= 1;
	i_105_ <<= 1;
	i_105_ -= 6;
	while (i_102_ < i_105_) {
	    i = is[i_101_++];
	    is_100_[i_102_++] += i * i_103_;
	    is_100_[i_102_++] += i * i_104_;
	    i = is[i_101_++];
	    is_100_[i_102_++] += i * i_103_;
	    is_100_[i_102_++] += i * i_104_;
	    i = is[i_101_++];
	    is_100_[i_102_++] += i * i_103_;
	    is_100_[i_102_++] += i * i_104_;
	    i = is[i_101_++];
	    is_100_[i_102_++] += i * i_103_;
	    is_100_[i_102_++] += i * i_104_;
	}
	i_105_ += 6;
	while (i_102_ < i_105_) {
	    i = is[i_101_++];
	    is_100_[i_102_++] += i * i_103_;
	    is_100_[i_102_++] += i * i_104_;
	}
	class39_sub1_sub2.anInt1630 = i_101_ << 8;
	return i_102_ >> 1;
    }
    
    public static int method441(byte[] is, int[] is_108_, int i, int i_109_,
				int i_110_, int i_111_, int i_112_, int i_113_,
				int i_114_, int i_115_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i >>= 8;
	i_115_ >>= 8;
	i_110_ <<= 8;
	i_112_ <<= 8;
	if ((i_113_ = i_109_ + i_115_ - i) > i_114_)
	    i_113_ = i_114_;
	i_113_ -= 3;
	while (i_109_ < i_113_) {
	    is_108_[i_109_++] += is[i++] * i_110_ >> i_111_;
	    i_110_ += i_112_;
	    is_108_[i_109_++] += is[i++] * i_110_ >> i_111_;
	    i_110_ += i_112_;
	    is_108_[i_109_++] += is[i++] * i_110_ >> i_111_;
	    i_110_ += i_112_;
	    is_108_[i_109_++] += is[i++] * i_110_ >> i_111_;
	    i_110_ += i_112_;
	}
	i_113_ += 3;
	while (i_109_ < i_113_) {
	    is_108_[i_109_++] += is[i++] * i_110_ >> i_111_;
	    i_110_ += i_112_;
	}
	class39_sub1_sub2.anInt1635 = i_110_ >> 8;
	class39_sub1_sub2.anInt1630 = i << 8;
	return i_109_;
    }
    
    public static int method442(byte[] is, int[] is_116_, int i, int i_117_,
				int i_118_, int i_119_, int i_120_, int i_121_,
				int i_122_, int i_123_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i >>= 8;
	i_123_ >>= 8;
	i_118_ <<= 8;
	i_120_ <<= 8;
	if ((i_121_ = i_117_ + i - (i_123_ - 1)) > i_122_)
	    i_121_ = i_122_;
	i_121_ -= 3;
	while (i_117_ < i_121_) {
	    is_116_[i_117_++] += is[i--] * i_118_ >> i_119_;
	    i_118_ += i_120_;
	    is_116_[i_117_++] += is[i--] * i_118_ >> i_119_;
	    i_118_ += i_120_;
	    is_116_[i_117_++] += is[i--] * i_118_ >> i_119_;
	    i_118_ += i_120_;
	    is_116_[i_117_++] += is[i--] * i_118_ >> i_119_;
	    i_118_ += i_120_;
	}
	i_121_ += 3;
	while (i_117_ < i_121_) {
	    is_116_[i_117_++] += is[i--] * i_118_ >> i_119_;
	    i_118_ += i_120_;
	}
	class39_sub1_sub2.anInt1635 = i_118_ >> 8;
	class39_sub1_sub2.anInt1630 = i << 8;
	return i_117_;
    }
    
    public synchronized void method443(int i) {
	method433(i, method447());
    }
    
    public static Class39_Sub1_Sub2 method444
	(Class39_Sub3_Sub1 class39_sub3_sub1, int i, int i_124_) {
	if (class39_sub3_sub1.aByteArray1639 == null
	    || class39_sub3_sub1.aByteArray1639.length == 0)
	    return null;
	return new Class39_Sub1_Sub2(class39_sub3_sub1,
				     (int) ((long) class39_sub3_sub1.anInt1636
					    * 256L * (long) i
					    / (long) (Class15.anInt274 * 100)),
				     i_124_);
    }
    
    public static int method445(byte[] is, int[] is_125_, int i, int i_126_,
				int i_127_, int i_128_, int i_129_, int i_130_,
				Class39_Sub1_Sub2 class39_sub1_sub2) {
	i >>= 8;
	i_130_ >>= 8;
	i_127_ <<= 8;
	if ((i_128_ = i_126_ + i_130_ - i) > i_129_)
	    i_128_ = i_129_;
	i_128_ -= 3;
	while (i_126_ < i_128_) {
	    is_125_[i_126_++] += is[i++] * i_127_;
	    is_125_[i_126_++] += is[i++] * i_127_;
	    is_125_[i_126_++] += is[i++] * i_127_;
	    is_125_[i_126_++] += is[i++] * i_127_;
	}
	i_128_ += 3;
	while (i_126_ < i_128_)
	    is_125_[i_126_++] += is[i++] * i_127_;
	class39_sub1_sub2.anInt1630 = i << 8;
	return i_126_;
    }
    
    public static int method446
	(int i, int i_131_, byte[] is, int[] is_132_, int i_133_, int i_134_,
	 int i_135_, int i_136_, int i_137_, int i_138_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_139_, int i_140_) {
	if (i_139_ == 0
	    || ((i_136_ = i_134_ + (i_138_ - i_133_ + i_139_ - 257) / i_139_)
		> i_137_))
	    i_136_ = i_137_;
	while (i_134_ < i_136_) {
	    i_131_ = i_133_ >> 8;
	    i = is[i_131_];
	    is_132_[i_134_++]
		+= (((i << 8) + (is[i_131_ + 1] - i) * (i_133_ & 0xff))
		    * i_135_);
	    i_133_ += i_139_;
	}
	if (i_139_ == 0
	    || ((i_136_ = i_134_ + (i_138_ - i_133_ + i_139_ - 1) / i_139_)
		> i_137_))
	    i_136_ = i_137_;
	i_131_ = i_140_;
	while (i_134_ < i_136_) {
	    i = is[i_133_ >> 8];
	    is_132_[i_134_++]
		+= ((i << 8) + (i_131_ - i) * (i_133_ & 0xff)) * i_135_;
	    i_133_ += i_139_;
	}
	class39_sub1_sub2.anInt1630 = i_133_;
	return i_134_;
    }
    
    public synchronized int method415(int[] is, int i, int i_141_) {
	if (method451())
	    return 0;
	if (anInt1635 == 0 && (anInt1629 == 0 || anInt1623 == 0
			       || anInt1623 == -2147483648)) {
	    method417(i_141_);
	    return 0;
	}
	Class39_Sub3_Sub1 class39_sub3_sub1
	    = (Class39_Sub3_Sub1) aClass39_Sub3_1320;
	int i_142_ = anInt1624 << 8;
	int i_143_ = anInt1625 << 8;
	int i_144_ = class39_sub3_sub1.aByteArray1639.length << 8;
	int i_145_ = i_143_ - i_142_;
	if (i_145_ <= 0)
	    anInt1633 = 0;
	int i_146_ = i;
	i_141_ += i;
	if (anInt1633 < 0) {
	    if (aBoolean1622) {
		if (anInt1626 < 0) {
		    i_146_ = method435(is, i_146_, i_142_, i_141_,
				       (class39_sub3_sub1.aByteArray1639
					[anInt1624]));
		    if (anInt1630 >= i_142_)
			return 1;
		    anInt1630 = i_142_ + i_142_ - 1 - anInt1630;
		    anInt1626 = -anInt1626;
		}
		for (;;) {
		    i_146_ = method438(is, i_146_, i_143_, i_141_,
				       (class39_sub3_sub1.aByteArray1639
					[anInt1625 - 1]));
		    if (anInt1630 < i_143_)
			return 1;
		    anInt1630 = i_143_ + i_143_ - 1 - anInt1630;
		    anInt1626 = -anInt1626;
		    i_146_ = method435(is, i_146_, i_142_, i_141_,
				       (class39_sub3_sub1.aByteArray1639
					[anInt1624]));
		    if (anInt1630 >= i_142_)
			return 1;
		    anInt1630 = i_142_ + i_142_ - 1 - anInt1630;
		    anInt1626 = -anInt1626;
		}
	    }
	    if (anInt1626 < 0) {
		for (;;) {
		    i_146_ = method435(is, i_146_, i_142_, i_141_,
				       (class39_sub3_sub1.aByteArray1639
					[anInt1625 - 1]));
		    if (anInt1630 >= i_142_)
			return 1;
		    anInt1630 = i_143_ - 1 - (i_143_ - 1 - anInt1630) % i_145_;
		}
	    }
	    for (;;) {
		i_146_
		    = method438(is, i_146_, i_143_, i_141_,
				class39_sub3_sub1.aByteArray1639[anInt1624]);
		if (anInt1630 < i_143_)
		    return 1;
		anInt1630 = i_142_ + (anInt1630 - i_142_) % i_145_;
	    }
	}
	do {
	    if (anInt1633 > 0) {
		if (aBoolean1622) {
		    if (anInt1626 < 0) {
			i_146_ = method435(is, i_146_, i_142_, i_141_,
					   (class39_sub3_sub1.aByteArray1639
					    [anInt1624]));
			if (anInt1630 >= i_142_)
			    return 1;
			anInt1630 = i_142_ + i_142_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
			if (--anInt1633 == 0)
			    break;
		    }
		    do {
			i_146_ = method438(is, i_146_, i_143_, i_141_,
					   (class39_sub3_sub1.aByteArray1639
					    [anInt1625 - 1]));
			if (anInt1630 < i_143_)
			    return 1;
			anInt1630 = i_143_ + i_143_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
			if (--anInt1633 == 0)
			    break;
			i_146_ = method435(is, i_146_, i_142_, i_141_,
					   (class39_sub3_sub1.aByteArray1639
					    [anInt1624]));
			if (anInt1630 >= i_142_)
			    return 1;
			anInt1630 = i_142_ + i_142_ - 1 - anInt1630;
			anInt1626 = -anInt1626;
		    } while (--anInt1633 != 0);
		} else if (anInt1626 < 0) {
		    for (;;) {
			i_146_ = method435(is, i_146_, i_142_, i_141_,
					   (class39_sub3_sub1.aByteArray1639
					    [anInt1625 - 1]));
			if (anInt1630 >= i_142_)
			    return 1;
			int i_147_ = (i_143_ - 1 - anInt1630) / i_145_;
			if (i_147_ >= anInt1633) {
			    anInt1630 += i_145_ * anInt1633;
			    anInt1633 = 0;
			    break;
			}
			anInt1630 += i_145_ * i_147_;
			anInt1633 -= i_147_;
		    }
		} else {
		    for (;;) {
			i_146_ = method438(is, i_146_, i_143_, i_141_,
					   (class39_sub3_sub1.aByteArray1639
					    [anInt1624]));
			if (anInt1630 < i_143_)
			    return 1;
			int i_148_ = (anInt1630 - i_142_) / i_145_;
			if (i_148_ >= anInt1633) {
			    anInt1630 -= i_145_ * anInt1633;
			    anInt1633 = 0;
			    break;
			}
			anInt1630 -= i_145_ * i_148_;
			anInt1633 -= i_148_;
		    }
		}
	    }
	} while (false);
	if (anInt1626 < 0) {
	    method435(is, i_146_, 0, i_141_, 0);
	    if (anInt1630 < 0) {
		anInt1630 = 0;
		this.unlinkDeque();
	    }
	} else {
	    method438(is, i_146_, i_144_, i_141_, 0);
	    if (anInt1630 >= i_144_) {
		anInt1630 = i_144_ - 1;
		this.unlinkDeque();
	    }
	}
	return 1;
    }
    
    public synchronized int method447() {
	if (anInt1629 > 0)
	    return anInt1628;
	return anInt1634;
    }
    
    public synchronized void method448(int i) {
	anInt1633 = i;
    }
    
    public static int method449
	(int i, int i_149_, byte[] is, int[] is_150_, int i_151_, int i_152_,
	 int i_153_, int i_154_, int i_155_, int i_156_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_157_, int i_158_) {
	if (i_157_ == 0
	    || ((i_154_ = i_152_ + (i_156_ + 256 - i_151_ + i_157_) / i_157_)
		> i_155_))
	    i_154_ = i_155_;
	while (i_152_ < i_154_) {
	    i_149_ = i_151_ >> 8;
	    i = is[i_149_ - 1];
	    is_150_[i_152_++]
		+= ((i << 8) + (is[i_149_] - i) * (i_151_ & 0xff)) * i_153_;
	    i_151_ += i_157_;
	}
	if (i_157_ == 0
	    || ((i_154_ = i_152_ + (i_156_ - i_151_ + i_157_) / i_157_)
		> i_155_))
	    i_154_ = i_155_;
	i = i_158_;
	i_149_ = i_157_;
	while (i_152_ < i_154_) {
	    is_150_[i_152_++]
		+= (((i << 8) + (is[i_151_ >> 8] - i) * (i_151_ & 0xff))
		    * i_153_);
	    i_151_ += i_149_;
	}
	class39_sub1_sub2.anInt1630 = i_151_;
	return i_152_;
    }
    
    public static int method450
	(int i, int i_159_, byte[] is, int[] is_160_, int i_161_, int i_162_,
	 int i_163_, int i_164_, int i_165_, int i_166_, int i_167_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_168_, int i_169_) {
	if (i_168_ == 0
	    || ((i_165_ = i_162_ + (i_167_ + 256 - i_161_ + i_168_) / i_168_)
		> i_166_))
	    i_165_ = i_166_;
	i_162_ <<= 1;
	i_165_ <<= 1;
	while (i_162_ < i_165_) {
	    i_159_ = i_161_ >> 8;
	    i = is[i_159_ - 1];
	    i = (i << 8) + (is[i_159_] - i) * (i_161_ & 0xff);
	    is_160_[i_162_++] += i * i_163_;
	    is_160_[i_162_++] += i * i_164_;
	    i_161_ += i_168_;
	}
	if (i_168_ == 0
	    || ((i_165_ = (i_162_ >> 1) + (i_167_ - i_161_ + i_168_) / i_168_)
		> i_166_))
	    i_165_ = i_166_;
	i_165_ <<= 1;
	i_159_ = i_169_;
	while (i_162_ < i_165_) {
	    i = (i_159_ << 8) + (is[i_161_ >> 8] - i_159_) * (i_161_ & 0xff);
	    is_160_[i_162_++] += i * i_163_;
	    is_160_[i_162_++] += i * i_164_;
	    i_161_ += i_168_;
	}
	class39_sub1_sub2.anInt1630 = i_161_;
	return i_162_ >> 1;
    }
    
    public boolean method451() {
	if (anInt1629 != 0
	    && StillGraphic.aBoolean2342 != (anInt1627
						       != -2147483648)) {
	    anInt1635 = anInt1623 == -2147483648 ? 0 : anInt1623;
	    anInt1634 = anInt1628;
	    anInt1629 = 0;
	    if (anInt1623 == -2147483648) {
		this.unlinkDeque();
		return true;
	    }
	}
	return false;
    }
    
    public static int method452
	(int i, int i_170_, byte[] is, int[] is_171_, int i_172_, int i_173_,
	 int i_174_, int i_175_, int i_176_, int i_177_, int i_178_,
	 int i_179_, int i_180_, int i_181_,
	 Class39_Sub1_Sub2 class39_sub1_sub2, int i_182_, int i_183_) {
	if (i_182_ == 0
	    || ((i_179_ = i_173_ + (i_181_ - i_172_ + i_182_ - 257) / i_182_)
		> i_180_))
	    i_179_ = i_180_;
	i_173_ <<= 1;
	i_179_ <<= 1;
	while (i_173_ < i_179_) {
	    i_170_ = i_172_ >> 8;
	    i = is[i_170_];
	    i = (i << 8) + (is[i_170_ + 1] - i) * (i_172_ & 0xff);
	    is_171_[i_173_++] += i * i_174_ >> i_176_;
	    i_174_ += i_177_;
	    is_171_[i_173_++] += i * i_175_ >> i_176_;
	    i_175_ += i_178_;
	    i_172_ += i_182_;
	}
	if (i_182_ == 0 || (i_179_ = (i_173_ >> 1) + (i_181_ - i_172_ + i_182_
						      - 1) / i_182_) > i_180_)
	    i_179_ = i_180_;
	i_179_ <<= 1;
	i_170_ = i_183_;
	while (i_173_ < i_179_) {
	    i = is[i_172_ >> 8];
	    i = (i << 8) + (i_170_ - i) * (i_172_ & 0xff);
	    is_171_[i_173_++] += i * i_174_ >> i_176_;
	    i_174_ += i_177_;
	    is_171_[i_173_++] += i * i_175_ >> i_176_;
	    i_175_ += i_178_;
	    i_172_ += i_182_;
	}
	class39_sub1_sub2.anInt1635 = i_174_;
	class39_sub1_sub2.anInt1634 = i_175_;
	class39_sub1_sub2.anInt1630 = i_172_;
	return i_173_ >> 1;
    }
    
    public Class39_Sub1_Sub2(Class39_Sub3_Sub1 class39_sub3_sub1, int i,
			     int i_184_) {
	aClass39_Sub3_1320 = class39_sub3_sub1;
	anInt1624 = class39_sub3_sub1.anInt1638;
	anInt1625 = class39_sub3_sub1.anInt1637;
	aBoolean1622 = class39_sub3_sub1.aBoolean1640;
	anInt1626 = i;
	anInt1635 = i_184_;
	anInt1634 = 128;
	anInt1630 = 0;
    }
}
