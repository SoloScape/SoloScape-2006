package jagex.graphics;

/* Class39_Sub5_Sub10_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.DrawingArea;
import jagex.utils.JString;
import java.util.Random;
import unpackaged.Class30;
import unpackaged.Class39_Sub5_Sub9;

public class BitmapFont extends DrawingArea
{
    public byte[][] aByteArrayArray2423;
    public static JString aClass3_2424;
    public int anInt2425 = 0;
    public Random aRandom2426;
    public static JString aClass3_2427;
    public static JString aClass3_2428;
    public static JString aClass3_2429;
    public static JString aClass3_2430;
    public int[] anIntArray2431;
    public static JString aClass3_2432
	= Class39_Sub5_Sub9.createJstring("mag");
    public int[] anIntArray2433;
    public static JString aClass3_2434;
    public static JString aClass3_2435;
    public static JString aClass3_2436;
    public static JString aClass3_2437;
    public static JString aClass3_2438;
    public static JString aClass3_2439;
    public static JString[] aClass3Array2440;
    public static JString aClass3_2441;
    public static JString aClass3_2442;
    public static JString aClass3_2443;
    public static JString aClass3_2444;
    public static JString aClass3_2445;
    public static JString aClass3_2446;
    public static JString aClass3_2447;
    public int[] anIntArray2448;
    public int anInt2449;
    public int anInt2450;
    public boolean aBoolean2451;
    
    public void method625(JString class3, int i, int i_0_, int i_1_, int i_2_,
			  int i_3_, boolean bool, int i_4_, int i_5_,
			  int i_6_) {
	if (class3 != null) {
	    if (i_6_ == 0)
		i_6_ = anInt2450;
	    boolean bool_7_ = true;
	    if (i_2_ < anInt2450 + anInt2449 + i_6_ && i_2_ < i_6_ + i_6_)
		bool_7_ = false;
	    int i_8_ = method648(class3, i_1_, bool_7_);
	    if (i_5_ == 3 && i_8_ == 1)
		i_5_ = 1;
	    int i_9_;
	    if (i_5_ == 0)
		i_9_ = i_0_ + anInt2450;
	    else if (i_5_ == 1)
		i_9_ = i_0_ + anInt2450 + (i_2_ - anInt2450 - anInt2449
					   - (i_8_ - 1) * i_6_) / 2;
	    else if (i_5_ == 2)
		i_9_ = i_0_ + i_2_ - anInt2449 - (i_8_ - 1) * i_6_;
	    else {
		int i_10_ = ((i_2_ - anInt2450 - anInt2449 - (i_8_ - 1) * i_6_)
			     / (i_8_ + 1));
		if (i_10_ < 0)
		    i_10_ = 0;
		i_9_ = i_0_ + anInt2450 + i_10_;
		i_6_ += i_10_;
	    }
	    for (int i_11_ = 0; i_11_ < i_8_; i_11_++) {
		if (i_4_ == 0)
		    method635(aClass3Array2440[i_11_], i, i_9_, i_3_, bool);
		else if (i_4_ == 1)
		    method636(aClass3Array2440[i_11_], i + i_1_ / 2, i_9_,
			      i_3_, bool);
		else if (i_4_ == 2)
		    method627(aClass3Array2440[i_11_], i + i_1_, i_9_, i_3_,
			      bool);
		else if (i_11_ == i_8_ - 1)
		    method635(aClass3Array2440[i_11_], i, i_9_, i_3_, bool);
		else
		    method633(aClass3Array2440[i_11_], i, i_9_, i_3_, bool,
			      i_1_);
		i_9_ += i_6_;
	    }
	}
    }
    
    public void method626(JString class3, int i, int i_12_, int i_13_,
			  int i_14_, int i_15_) {
	if (class3 != null) {
	    double d = 7.0 - (double) i_15_ / 8.0;
	    if (d < 0.0)
		d = 0.0;
	    i -= method637(class3) / 2;
	    i_12_ -= anInt2425;
	    for (int i_16_ = 0; i_16_ < class3.getLength(); i_16_++) {
		int i_17_ = class3.bytes[i_16_] & 0xff;
		if (i_17_ != 32)
		    method642(aByteArrayArray2423[i_17_], i,
			      (i_12_ + anIntArray2431[i_17_]
			       + (int) (Math.sin((double) i_16_ / 1.5
						 + (double) i_14_)
					* d)),
			      anIntArray2448[i_17_], anIntArray2433[i_17_],
			      i_13_);
		i += anIntArray2448[i_17_];
	    }
	}
    }
    
    public void method627(JString class3, int i, int i_18_, int i_19_,
			  boolean bool) {
	method635(class3, i - method646(class3), i_18_, i_19_, bool);
    }
    
    public void method628(byte[] is, int i, int i_20_, int i_21_, int i_22_,
			  int i_23_, int i_24_) {
	int i_25_ = i + i_20_ * DrawingArea.bufferWidth;
	int i_26_ = DrawingArea.bufferWidth - i_21_;
	int i_27_ = 0;
	int i_28_ = 0;
	if (i_20_ < DrawingArea.areaOffsetY) {
	    int i_29_ = DrawingArea.areaOffsetY - i_20_;
	    i_22_ -= i_29_;
	    i_20_ = DrawingArea.areaOffsetY;
	    i_28_ += i_29_ * i_21_;
	    i_25_ += i_29_ * DrawingArea.bufferWidth;
	}
	if (i_20_ + i_22_ > DrawingArea.areaHeight)
	    i_22_ -= i_20_ + i_22_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_30_ = DrawingArea.areaOffsetX - i;
	    i_21_ -= i_30_;
	    i = DrawingArea.areaOffsetX;
	    i_28_ += i_30_;
	    i_25_ += i_30_;
	    i_27_ += i_30_;
	    i_26_ += i_30_;
	}
	if (i + i_21_ > DrawingArea.areaWidth) {
	    int i_31_ = i + i_21_ - DrawingArea.areaWidth;
	    i_21_ -= i_31_;
	    i_27_ += i_31_;
	    i_26_ += i_31_;
	}
	if (i_21_ > 0 && i_22_ > 0)
	    method640(DrawingArea.buffer, is, i_23_, i_28_,
		      i_25_, i_21_, i_22_, i_26_, i_27_, i_24_);
    }
    
    public void method629(JString class3, int i, int i_32_, int i_33_) {
	method647(class3, i - method637(class3) / 2, i_32_, i_33_);
    }
    
    public void method630(JString class3, int i, int i_34_, int i_35_) {
	method647(class3, i - method637(class3), i_34_, i_35_);
    }
    
    public int method631(JString class3, int i) {
	int i_36_ = method648(class3, i, true);
	int i_37_ = 0;
	for (int i_38_ = 0; i_38_ < i_36_; i_38_++) {
	    int i_39_ = method637(aClass3Array2440[i_38_]);
	    if (i_39_ > i_37_)
		i_37_ = i_39_;
	}
	return i_37_;
    }
    
    public int method632(JString class3) {
	if (class3.isEqual(aClass3_2437))
	    return 16711680;
	if (class3.isEqual(aClass3_2447))
	    return 65280;
	if (class3.isEqual(aClass3_2445))
	    return 255;
	if (class3.isEqual(aClass3_2446))
	    return 16776960;
	if (class3.isEqual(aClass3_2434))
	    return 65535;
	if (class3.isEqual(aClass3_2432))
	    return 16711935;
	if (class3.isEqual(aClass3_2428))
	    return 16777215;
	if (class3.isEqual(aClass3_2436))
	    return 0;
	if (class3.isEqual(aClass3_2444))
	    return 16748608;
	if (class3.isEqual(aClass3_2439))
	    return 8388608;
	if (class3.isEqual(aClass3_2427))
	    return 128;
	if (class3.isEqual(aClass3_2443))
	    return 16756736;
	if (class3.isEqual(aClass3_2430))
	    return 16740352;
	if (class3.isEqual(aClass3_2424))
	    return 16723968;
	if (class3.isEqual(aClass3_2442))
	    return 12648192;
	if (class3.isEqual(aClass3_2438))
	    return 8453888;
	if (class3.isEqual(aClass3_2441))
	    return 4259584;
	if (class3.isEqual(aClass3_2429))
	    aBoolean2451 = true;
	return -1;
    }
    
    public void method633(JString class3, int i, int i_40_, int i_41_,
			  boolean bool, int i_42_) {
	class3 = class3.method69((byte) -45);
	int i_43_ = 0;
	for (int i_44_ = 0; i_44_ < class3.getLength(); i_44_++) {
	    if (class3.charAt(i_44_) == 32)
		i_43_++;
	}
	int i_45_ = 0;
	int i_46_ = 0;
	if (i_43_ > 0)
	    i_45_ = (i_42_ - method646(class3)) * 256 / i_43_;
	aBoolean2451 = false;
	int i_47_ = i;
	if (class3 != null) {
	    i_40_ -= anInt2425;
	    for (int i_48_ = 0; i_48_ < class3.length; i_48_++) {
		if (class3.bytes[i_48_] == 64
		    && i_48_ + 4 < class3.length
		    && class3.bytes[i_48_ + 4] == 64) {
		    int i_49_
			= method632(class3.method59(i_48_ + 1, -1, i_48_ + 4));
		    if (i_49_ != -1)
			i_41_ = i_49_;
		    i_48_ += 4;
		} else {
		    int i_50_ = class3.bytes[i_48_] & 0xff;
		    if (i_50_ != 32) {
			if (bool)
			    method642(aByteArrayArray2423[i_50_], i + 1,
				      i_40_ + anIntArray2431[i_50_] + 1,
				      anIntArray2448[i_50_],
				      anIntArray2433[i_50_], 0);
			method642(aByteArrayArray2423[i_50_], i,
				  i_40_ + anIntArray2431[i_50_],
				  anIntArray2448[i_50_], anIntArray2433[i_50_],
				  i_41_);
		    }
		    i += anIntArray2448[i_50_];
		    if (i_50_ == 32) {
			i_46_ += i_45_;
			i += i_46_ / 256;
			i_46_ &= 0xff;
		    }
		}
	    }
	    if (aBoolean2451)
		DrawingArea.drawHorizontalLine(i_47_,
					     i_40_ + (int) ((double) anInt2425
							    * 0.7),
					     i - i_47_, 8388608);
	}
    }
    
    public int method634(JString class3, int i) {
	return method648(class3, i, true);
    }
    
    public void method635(JString class3, int i, int i_51_, int i_52_,
			  boolean bool) {
	aBoolean2451 = false;
	int i_53_ = i;
	if (class3 != null) {
	    i_51_ -= anInt2425;
	    for (int i_54_ = 0; i_54_ < class3.length; i_54_++) {
		if (class3.bytes[i_54_] == 64
		    && i_54_ + 4 < class3.length
		    && class3.bytes[i_54_ + 4] == 64) {
		    int i_55_
			= method632(class3.method59(i_54_ + 1, -1, i_54_ + 4));
		    if (i_55_ != -1)
			i_52_ = i_55_;
		    i_54_ += 4;
		} else {
		    int i_56_ = class3.bytes[i_54_] & 0xff;
		    if (i_56_ != 32) {
			if (bool)
			    method642(aByteArrayArray2423[i_56_], i + 1,
				      i_51_ + anIntArray2431[i_56_] + 1,
				      anIntArray2448[i_56_],
				      anIntArray2433[i_56_], 0);
			method642(aByteArrayArray2423[i_56_], i,
				  i_51_ + anIntArray2431[i_56_],
				  anIntArray2448[i_56_], anIntArray2433[i_56_],
				  i_52_);
		    }
		    i += anIntArray2448[i_56_];
		}
	    }
	    if (aBoolean2451)
		DrawingArea.drawHorizontalLine(i_53_,
					     i_51_ + (int) ((double) anInt2425
							    * 0.7),
					     i - i_53_, 8388608);
	}
    }
    
    public void method636(JString class3, int i, int i_57_, int i_58_,
			  boolean bool) {
	method635(class3, i - method646(class3) / 2, i_57_, i_58_, bool);
    }
    
    public int method637(JString class3) {
	if (class3 == null)
	    return 0;
	int i = 0;
	for (int i_59_ = 0; i_59_ < class3.length; i_59_++)
	    i += anIntArray2448[class3.bytes[i_59_] & 0xff];
	return i;
    }
    
    public void method638(JString class3, int i, int i_60_, int i_61_,
			  int i_62_) {
	if (class3 != null) {
	    i -= method637(class3) / 2;
	    i_60_ -= anInt2425;
	    for (int i_63_ = 0; i_63_ < class3.length; i_63_++) {
		int i_64_ = class3.bytes[i_63_] & 0xff;
		if (i_64_ != 32)
		    method642(aByteArrayArray2423[i_64_], i,
			      (i_60_ + anIntArray2431[i_64_]
			       + (int) (Math.sin((double) i_63_ / 2.0
						 + (double) i_62_ / 5.0)
					* 5.0)),
			      anIntArray2448[i_64_], anIntArray2433[i_64_],
			      i_61_);
		i += anIntArray2448[i_64_];
	    }
	}
    }
    
    public void method639(JString class3, int i, int i_65_, int i_66_,
			  boolean bool, int i_67_) {
	if (class3 != null) {
	    aRandom2426.setSeed((long) i_67_);
	    int i_68_ = (aRandom2426.nextInt() & 0x1f) + 192;
	    i_65_ -= anInt2425;
	    for (int i_69_ = 0; i_69_ < class3.length; i_69_++) {
		if (class3.bytes[i_69_] == 64
		    && i_69_ + 4 < class3.length
		    && class3.bytes[i_69_ + 4] == 64) {
		    int i_70_
			= method632(class3.method59(i_69_ + 1, -1, i_69_ + 4));
		    if (i_70_ != -1)
			i_66_ = i_70_;
		    i_69_ += 4;
		} else {
		    int i_71_ = class3.bytes[i_69_] & 0xff;
		    if (i_71_ != 32) {
			if (bool)
			    method628(aByteArrayArray2423[i_71_], i + 1,
				      i_65_ + anIntArray2431[i_71_] + 1,
				      anIntArray2448[i_71_],
				      anIntArray2433[i_71_], 0, 192);
			method628(aByteArrayArray2423[i_71_], i,
				  i_65_ + anIntArray2431[i_71_],
				  anIntArray2448[i_71_], anIntArray2433[i_71_],
				  i_66_, i_68_);
		    }
		    i += anIntArray2448[i_71_];
		    if ((aRandom2426.nextInt() & 0x3) == 0)
			i++;
		}
	    }
	}
    }
    
    public void method640(int[] is, byte[] is_72_, int i, int i_73_, int i_74_,
			  int i_75_, int i_76_, int i_77_, int i_78_,
			  int i_79_) {
	i = ((i & 0xff00ff) * i_79_ & ~0xff00ff) + ((i & 0xff00) * i_79_
						    & 0xff0000) >> 8;
	i_79_ = 256 - i_79_;
	for (int i_80_ = -i_76_; i_80_ < 0; i_80_++) {
	    for (int i_81_ = -i_75_; i_81_ < 0; i_81_++) {
		if (is_72_[i_73_++] != 0) {
		    int i_82_ = is[i_74_];
		    is[i_74_++] = ((((i_82_ & 0xff00ff) * i_79_ & ~0xff00ff)
				    + ((i_82_ & 0xff00) * i_79_ & 0xff0000))
				   >> 8) + i;
		} else
		    i_74_++;
	    }
	    i_74_ += i_77_;
	    i_73_ += i_78_;
	}
    }
    
    public void method641(int[] is, byte[] is_83_, int i, int i_84_, int i_85_,
			  int i_86_, int i_87_, int i_88_, int i_89_) {
	int i_90_ = -(i_86_ >> 2);
	i_86_ = -(i_86_ & 0x3);
	for (int i_91_ = -i_87_; i_91_ < 0; i_91_++) {
	    for (int i_92_ = i_90_; i_92_ < 0; i_92_++) {
		if (is_83_[i_84_++] != 0)
		    is[i_85_++] = i;
		else
		    i_85_++;
		if (is_83_[i_84_++] != 0)
		    is[i_85_++] = i;
		else
		    i_85_++;
		if (is_83_[i_84_++] != 0)
		    is[i_85_++] = i;
		else
		    i_85_++;
		if (is_83_[i_84_++] != 0)
		    is[i_85_++] = i;
		else
		    i_85_++;
	    }
	    for (int i_93_ = i_86_; i_93_ < 0; i_93_++) {
		if (is_83_[i_84_++] != 0)
		    is[i_85_++] = i;
		else
		    i_85_++;
	    }
	    i_85_ += i_88_;
	    i_84_ += i_89_;
	}
    }
    
    public void method642(byte[] is, int i, int i_94_, int i_95_, int i_96_,
			  int i_97_) {
	int i_98_ = i + i_94_ * DrawingArea.bufferWidth;
	int i_99_ = DrawingArea.bufferWidth - i_95_;
	int i_100_ = 0;
	int i_101_ = 0;
	if (i_94_ < DrawingArea.areaOffsetY) {
	    int i_102_ = DrawingArea.areaOffsetY - i_94_;
	    i_96_ -= i_102_;
	    i_94_ = DrawingArea.areaOffsetY;
	    i_101_ += i_102_ * i_95_;
	    i_98_ += i_102_ * DrawingArea.bufferWidth;
	}
	if (i_94_ + i_96_ > DrawingArea.areaHeight)
	    i_96_ -= i_94_ + i_96_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_103_ = DrawingArea.areaOffsetX - i;
	    i_95_ -= i_103_;
	    i = DrawingArea.areaOffsetX;
	    i_101_ += i_103_;
	    i_98_ += i_103_;
	    i_100_ += i_103_;
	    i_99_ += i_103_;
	}
	if (i + i_95_ > DrawingArea.areaWidth) {
	    int i_104_ = i + i_95_ - DrawingArea.areaWidth;
	    i_95_ -= i_104_;
	    i_100_ += i_104_;
	    i_99_ += i_104_;
	}
	if (i_95_ > 0 && i_96_ > 0)
	    method641(DrawingArea.buffer, is, i_97_, i_101_,
		      i_98_, i_95_, i_96_, i_99_, i_100_);
    }
    
    public static void method643() {
	aClass3_2429 = null;
	aClass3_2435 = null;
	aClass3_2437 = null;
	aClass3_2447 = null;
	aClass3_2445 = null;
	aClass3_2446 = null;
	aClass3_2434 = null;
	aClass3_2432 = null;
	aClass3_2428 = null;
	aClass3_2436 = null;
	aClass3_2444 = null;
	aClass3_2439 = null;
	aClass3_2427 = null;
	aClass3_2443 = null;
	aClass3_2430 = null;
	aClass3_2424 = null;
	aClass3_2442 = null;
	aClass3_2438 = null;
	aClass3_2441 = null;
	aClass3Array2440 = null;
    }
    
    public void method644(JString class3, int i, int i_105_, int i_106_,
			  int i_107_) {
	if (class3 != null) {
	    i -= method637(class3) / 2;
	    i_105_ -= anInt2425;
	    for (int i_108_ = 0; i_108_ < class3.getLength(); i_108_++) {
		int i_109_ = class3.bytes[i_108_] & 0xff;
		if (i_109_ != 32)
		    method642(aByteArrayArray2423[i_109_],
			      i + (int) (Math.sin((double) i_108_ / 5.0
						  + (double) i_107_ / 5.0)
					 * 5.0),
			      (i_105_ + anIntArray2431[i_109_]
			       + (int) (Math.sin((double) i_108_ / 3.0
						 + (double) i_107_ / 5.0)
					* 5.0)),
			      anIntArray2448[i_109_], anIntArray2433[i_109_],
			      i_106_);
		i += anIntArray2448[i_109_];
	    }
	}
    }
    
    public int method645(int i) {
	return anIntArray2448[i & 0xff];
    }
    
    public int method646(JString class3) {
	if (class3 == null)
	    return 0;
	int i = 0;
	for (int i_110_ = 0; i_110_ < class3.length; i_110_++) {
	    if (class3.bytes[i_110_] == 64
		&& i_110_ + 4 < class3.length
		&& class3.bytes[i_110_ + 4] == 64)
		i_110_ += 4;
	    else
		i += anIntArray2448[class3.bytes[i_110_] & 0xff];
	}
	return i;
    }
    
    public void method647(JString class3, int i, int i_111_, int i_112_) {
	if (class3 != null) {
	    i_111_ -= anInt2425;
	    for (int i_113_ = 0; i_113_ < class3.length; i_113_++) {
		int i_114_ = class3.bytes[i_113_] & 0xff;
		if (i_114_ != 32)
		    method642(aByteArrayArray2423[i_114_], i,
			      i_111_ + anIntArray2431[i_114_],
			      anIntArray2448[i_114_], anIntArray2433[i_114_],
			      i_112_);
		i += anIntArray2448[i_114_];
	    }
	}
    }
    
    public int method648(JString class3, int i, boolean bool) {
	int i_115_ = 0;
	int i_116_ = 0;
	JString class3_117_ = Class30.method317(100, (byte) -105);
	int i_118_ = -1;
	int i_119_ = 0;
	JString class3_120_ = null;
	int i_121_ = 0;
	boolean bool_122_ = false;
	int i_123_ = 0;
	int i_124_ = 0;
	int i_125_ = class3.getLength();
	for (int i_126_ = 0; i_126_ < i_125_; i_126_++) {
	    int i_127_ = class3.charAt(i_126_);
	    if (i_127_ == 64 && i_126_ + 4 < i_125_
		&& class3.charAt(i_126_ + 4) == 64) {
		JString class3_128_ = class3.method59(i_126_, -1, i_126_ + 5);
		if (class3_128_.isEqual(aClass3_2429)) {
		    bool_122_ = true;
		    i_123_ = class3_117_.getLength();
		} else {
		    class3_120_ = class3_128_;
		    i_121_ = class3_117_.getLength();
		}
		class3_117_.method82(27681, class3_128_);
		i_126_ += 4;
	    } else if (i_127_ == 92 && i_126_ + 1 < i_125_
		       && class3.charAt(i_126_ + 1) == 110) {
		aClass3Array2440[i_124_++]
		    = class3_117_.method59
			  (i_116_, -1, class3_117_.getLength())
			  .method69((byte) -45);
		i_116_ = class3_117_.getLength();
		i_115_ = 0;
		i_118_ = -1;
		i_126_++;
		class3_120_ = null;
		bool_122_ = false;
	    } else {
		class3_117_.concatChar(i_127_);
		i_115_ += method645(i_127_);
		if (i_127_ == 32 || i_127_ == 45) {
		    i_118_ = class3_117_.getLength();
		    i_119_ = i_115_;
		}
		if (bool && i_115_ > i && i_118_ >= 0) {
		    aClass3Array2440[i_124_++]
			= class3_117_.method59(i_116_, -1, i_118_)
			      .method69((byte) -45);
		    i_116_ = i_118_;
		    i_118_ = -1;
		    i_115_ -= i_119_;
		    if (class3_120_ != null && i_121_ < i_116_ && i_116_ > 4) {
			i_116_ -= 5;
			class3_117_.method63(i_116_, (byte) 84, class3_120_);
		    }
		    if (bool_122_ && i_123_ < i_116_ && i_116_ > 4) {
			i_116_ -= 5;
			class3_117_.method63(i_116_, (byte) 84, aClass3_2435);
		    }
		}
	    }
	}
	if (class3_117_.getLength() > i_116_)
	    aClass3Array2440[i_124_++]
		= class3_117_.method59
		      (i_116_, -1, class3_117_.getLength())
		      .method69((byte) -45);
	return i_124_;
    }
    
    public BitmapFont(int[] is, int[] is_129_, int[] is_130_, int[] is_131_, byte[][] is_132_) {
	aByteArrayArray2423 = new byte[256][];
	aRandom2426 = new Random();
	aBoolean2451 = false;
	anIntArray2431 = is;
	anIntArray2448 = is_129_;
	anIntArray2433 = is_130_;
	byte i = 0;
	for (int i_133_ = 1; i_133_ < is_131_.length; i_133_++) {
	    if (is_131_[i_133_] == 1)
		i = (byte) i_133_;
	}
	aByteArrayArray2423 = is_132_;
	int i_134_ = 2147483647;
	int i_135_ = -2147483648;
	for (int i_136_ = 0; i_136_ < 256; i_136_++) {
	    if (anIntArray2431[i_136_] < i_134_)
		i_134_ = anIntArray2431[i_136_];
	    if (anIntArray2431[i_136_] + anIntArray2433[i_136_] > i_135_)
		i_135_ = anIntArray2431[i_136_] + anIntArray2433[i_136_];
	    byte[] is_137_ = aByteArrayArray2423[i_136_];
	    int i_138_ = is_137_.length;
	    for (int i_139_ = 0; i_139_ < i_138_; i_139_++)
		is_137_[i_139_] = is_137_[i_139_] == i ? (byte) 0 : (byte) 1;
	}
	anInt2425 = anIntArray2431[32] + anIntArray2433[32];
	anInt2450 = anInt2425 - i_134_;
	anInt2449 = i_135_ - anInt2425;
    }
    
    public BitmapFont(byte[] is) {
	aByteArrayArray2423 = new byte[256][];
	aRandom2426 = new Random();
	aBoolean2451 = false;
	anIntArray2448 = new int[is.length];
	for (int i = 0; i < anIntArray2448.length; i++)
	    anIntArray2448[i] = is[i] & 0xff;
    }
    
    static {
	aClass3_2424 = Class39_Sub5_Sub9.createJstring("or3");
	aClass3_2430 = Class39_Sub5_Sub9.createJstring("or2");
	aClass3Array2440 = new JString[100];
	aClass3_2428 = Class39_Sub5_Sub9.createJstring("whi");
	aClass3_2435 = Class39_Sub5_Sub9.createJstring("@str@");
	aClass3_2434 = Class39_Sub5_Sub9.createJstring("cya");
	aClass3_2427 = Class39_Sub5_Sub9.createJstring("dbl");
	aClass3_2441 = Class39_Sub5_Sub9.createJstring("gr3");
	aClass3_2442 = Class39_Sub5_Sub9.createJstring("gr1");
	aClass3_2443 = Class39_Sub5_Sub9.createJstring("or1");
	aClass3_2439 = Class39_Sub5_Sub9.createJstring("dre");
	aClass3_2429 = Class39_Sub5_Sub9.createJstring("str");
	aClass3_2437 = Class39_Sub5_Sub9.createJstring("red");
	aClass3_2447 = Class39_Sub5_Sub9.createJstring("gre");
	aClass3_2446 = Class39_Sub5_Sub9.createJstring("yel");
	aClass3_2444 = Class39_Sub5_Sub9.createJstring("lre");
	aClass3_2445 = Class39_Sub5_Sub9.createJstring("blu");
	aClass3_2438 = Class39_Sub5_Sub9.createJstring("gr2");
	aClass3_2436 = Class39_Sub5_Sub9.createJstring("bla");
    }
}
