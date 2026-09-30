package jagex.graphics.sprites;

/* Class39_Sub5_Sub10_Sub3 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.sprites.IndexedColorSprite;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import jagex.graphics.DrawingArea;

public class DirectColorSprite extends DrawingArea
{
    public int anInt2472;
    public int anInt2473;
    public int height;
    public int anInt2475;
    public int[] anIntArray2476;
    public int anInt2477;
    public int width;
    
    public void method667(int i, int i_0_, int i_1_) {
	for (int i_2_ = 0; i_2_ < anIntArray2476.length; i_2_++) {
	    int i_3_ = anIntArray2476[i_2_];
	    if (i_3_ != 0) {
		int i_4_ = i_3_ >> 16 & 0xff;
		i_4_ += i;
		if (i_4_ < 1)
		    i_4_ = 1;
		else if (i_4_ > 255)
		    i_4_ = 255;
		int i_5_ = i_3_ >> 8 & 0xff;
		i_5_ += i_0_;
		if (i_5_ < 1)
		    i_5_ = 1;
		else if (i_5_ > 255)
		    i_5_ = 255;
		int i_6_ = i_3_ & 0xff;
		i_6_ += i_1_;
		if (i_6_ < 1)
		    i_6_ = 1;
		else if (i_6_ > 255)
		    i_6_ = 255;
		anIntArray2476[i_2_] = (i_4_ << 16) + (i_5_ << 8) + i_6_;
	    }
	}
    }
    
    public void method668(IndexedColorSprite class39_sub5_sub10_sub4,
			  int i, int i_7_) {
	i += anInt2473;
	i_7_ += anInt2472;
	int i_8_ = i + i_7_ * DrawingArea.bufferWidth;
	int i_9_ = 0;
	int i_10_ = height;
	int i_11_ = width;
	int i_12_ = DrawingArea.bufferWidth - i_11_;
	int i_13_ = 0;
	if (i_7_ < DrawingArea.areaOffsetY) {
	    int i_14_ = DrawingArea.areaOffsetY - i_7_;
	    i_10_ -= i_14_;
	    i_7_ = DrawingArea.areaOffsetY;
	    i_9_ += i_14_ * i_11_;
	    i_8_ += i_14_ * DrawingArea.bufferWidth;
	}
	if (i_7_ + i_10_ > DrawingArea.areaHeight)
	    i_10_ -= i_7_ + i_10_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_15_ = DrawingArea.areaOffsetX - i;
	    i_11_ -= i_15_;
	    i = DrawingArea.areaOffsetX;
	    i_9_ += i_15_;
	    i_8_ += i_15_;
	    i_13_ += i_15_;
	    i_12_ += i_15_;
	}
	if (i + i_11_ > DrawingArea.areaWidth) {
	    int i_16_ = i + i_11_ - DrawingArea.areaWidth;
	    i_11_ -= i_16_;
	    i_13_ += i_16_;
	    i_12_ += i_16_;
	}
	if (i_11_ > 0 && i_10_ > 0)
	    method674(DrawingArea.buffer, anIntArray2476, 0,
		      i_9_, i_8_, i_11_, i_10_, i_12_, i_13_,
		      class39_sub5_sub10_sub4.index);
    }
    
    public void method669(int i, int i_17_, int i_18_, int i_19_, int i_20_,
			  int i_21_) {
	if (i_21_ != 0) {
	    i -= anInt2473 << 4;
	    i_17_ -= anInt2472 << 4;
	    double d = (double) (i_20_ & 0xffff) * 9.587379924285257E-5;
	    int i_22_ = (int) Math.floor(Math.sin(d) * (double) i_21_ + 0.5);
	    int i_23_ = (int) Math.floor(Math.cos(d) * (double) i_21_ + 0.5);
	    int i_24_ = -i * i_23_ + -i_17_ * i_22_;
	    int i_25_ = i * i_22_ + -i_17_ * i_23_;
	    int i_26_ = ((width << 4) - i) * i_23_ + -i_17_ * i_22_;
	    int i_27_ = -((width << 4) - i) * i_22_ + -i_17_ * i_23_;
	    int i_28_ = -i * i_23_ + ((height << 4) - i_17_) * i_22_;
	    int i_29_ = i * i_22_ + ((height << 4) - i_17_) * i_23_;
	    int i_30_ = (((width << 4) - i) * i_23_
			 + ((height << 4) - i_17_) * i_22_);
	    int i_31_ = (-((width << 4) - i) * i_22_
			 + ((height << 4) - i_17_) * i_23_);
	    int i_32_;
	    int i_33_;
	    if (i_24_ < i_26_) {
		i_32_ = i_24_;
		i_33_ = i_26_;
	    } else {
		i_32_ = i_26_;
		i_33_ = i_24_;
	    }
	    if (i_28_ < i_32_)
		i_32_ = i_28_;
	    if (i_30_ < i_32_)
		i_32_ = i_30_;
	    if (i_28_ > i_33_)
		i_33_ = i_28_;
	    if (i_30_ > i_33_)
		i_33_ = i_30_;
	    int i_34_;
	    int i_35_;
	    if (i_25_ < i_27_) {
		i_34_ = i_25_;
		i_35_ = i_27_;
	    } else {
		i_34_ = i_27_;
		i_35_ = i_25_;
	    }
	    if (i_29_ < i_34_)
		i_34_ = i_29_;
	    if (i_31_ < i_34_)
		i_34_ = i_31_;
	    if (i_29_ > i_35_)
		i_35_ = i_29_;
	    if (i_31_ > i_35_)
		i_35_ = i_31_;
	    i_32_ >>= 12;
	    i_33_ = i_33_ + 4095 >> 12;
	    i_34_ >>= 12;
	    i_35_ = i_35_ + 4095 >> 12;
	    i_32_ += i_18_;
	    i_33_ += i_18_;
	    i_34_ += i_19_;
	    i_35_ += i_19_;
	    i_32_ >>= 4;
	    i_33_ = i_33_ + 15 >> 4;
	    i_34_ >>= 4;
	    i_35_ = i_35_ + 15 >> 4;
	    if (i_32_ < DrawingArea.areaOffsetX)
		i_32_ = DrawingArea.areaOffsetX;
	    if (i_33_ > DrawingArea.areaWidth)
		i_33_ = DrawingArea.areaWidth;
	    if (i_34_ < DrawingArea.areaOffsetY)
		i_34_ = DrawingArea.areaOffsetY;
	    if (i_35_ > DrawingArea.areaHeight)
		i_35_ = DrawingArea.areaHeight;
	    i_33_ = i_32_ - i_33_;
	    if (i_33_ < 0) {
		i_35_ = i_34_ - i_35_;
		if (i_35_ < 0) {
		    int i_36_ = i_34_ * DrawingArea.bufferWidth + i_32_;
		    double d_37_ = 1.6777216E7 / (double) i_21_;
		    int i_38_ = (int) Math.floor(Math.sin(d) * d_37_ + 0.5);
		    int i_39_ = (int) Math.floor(Math.cos(d) * d_37_ + 0.5);
		    int i_40_ = (i_32_ << 4) + 8 - i_18_;
		    int i_41_ = (i_34_ << 4) + 8 - i_19_;
		    int i_42_ = (i << 8) - (i_41_ * i_38_ >> 4);
		    int i_43_ = (i_17_ << 8) + (i_41_ * i_39_ >> 4);
		    if (i_39_ == 0) {
			if (i_38_ == 0) {
			    int i_44_ = i_35_;
			    while (i_44_ < 0) {
				int i_45_ = i_36_;
				int i_46_ = i_42_;
				int i_47_ = i_43_;
				int i_48_ = i_33_;
				if (i_46_ >= 0 && i_47_ >= 0
				    && i_46_ - (width << 12) < 0
				    && i_47_ - (height << 12) < 0) {
				    for (/**/; i_48_ < 0; i_48_++) {
					int i_49_
					    = (anIntArray2476
					       [((i_47_ >> 12) * width
						 + (i_46_ >> 12))]);
					if (i_49_ != 0)
					    DrawingArea
						.buffer[i_45_++]
						= i_49_;
					else
					    i_45_++;
				    }
				}
				i_44_++;
				i_36_ += DrawingArea.bufferWidth;
			    }
			} else if (i_38_ < 0) {
			    int i_50_ = i_35_;
			    while (i_50_ < 0) {
				int i_51_ = i_36_;
				int i_52_ = i_42_;
				int i_53_ = i_43_ + (i_40_ * i_38_ >> 4);
				int i_54_ = i_33_;
				if (i_52_ >= 0
				    && i_52_ - (width << 12) < 0) {
				    int i_55_;
				    if ((i_55_ = i_53_ - (height << 12))
					>= 0) {
					i_55_ = (i_38_ - i_55_) / i_38_;
					i_54_ += i_55_;
					i_53_ += i_38_ * i_55_;
					i_51_ += i_55_;
				    }
				    if ((i_55_ = (i_53_ - i_38_) / i_38_)
					> i_54_)
					i_54_ = i_55_;
				    for (/**/; i_54_ < 0; i_54_++) {
					int i_56_
					    = (anIntArray2476
					       [((i_53_ >> 12) * width
						 + (i_52_ >> 12))]);
					if (i_56_ != 0)
					    DrawingArea
						.buffer[i_51_++]
						= i_56_;
					else
					    i_51_++;
					i_53_ += i_38_;
				    }
				}
				i_50_++;
				i_42_ -= i_38_;
				i_36_ += DrawingArea.bufferWidth;
			    }
			} else {
			    int i_57_ = i_35_;
			    while (i_57_ < 0) {
				int i_58_ = i_36_;
				int i_59_ = i_42_;
				int i_60_ = i_43_ + (i_40_ * i_38_ >> 4);
				int i_61_ = i_33_;
				if (i_59_ >= 0
				    && i_59_ - (width << 12) < 0) {
				    if (i_60_ < 0) {
					int i_62_
					    = (i_38_ - 1 - i_60_) / i_38_;
					i_61_ += i_62_;
					i_60_ += i_38_ * i_62_;
					i_58_ += i_62_;
				    }
				    int i_63_;
				    if ((i_63_ = (i_60_ + 1 - (height << 12)
						  - i_38_) / i_38_)
					> i_61_)
					i_61_ = i_63_;
				    for (/**/; i_61_ < 0; i_61_++) {
					int i_64_
					    = (anIntArray2476
					       [((i_60_ >> 12) * width
						 + (i_59_ >> 12))]);
					if (i_64_ != 0)
					    DrawingArea
						.buffer[i_58_++]
						= i_64_;
					else
					    i_58_++;
					i_60_ += i_38_;
				    }
				}
				i_57_++;
				i_42_ -= i_38_;
				i_36_ += DrawingArea.bufferWidth;
			    }
			}
		    } else if (i_39_ < 0) {
			if (i_38_ == 0) {
			    int i_65_ = i_35_;
			    while (i_65_ < 0) {
				int i_66_ = i_36_;
				int i_67_ = i_42_ + (i_40_ * i_39_ >> 4);
				int i_68_ = i_43_;
				int i_69_ = i_33_;
				if (i_68_ >= 0
				    && i_68_ - (height << 12) < 0) {
				    int i_70_;
				    if ((i_70_ = i_67_ - (width << 12))
					>= 0) {
					i_70_ = (i_39_ - i_70_) / i_39_;
					i_69_ += i_70_;
					i_67_ += i_39_ * i_70_;
					i_66_ += i_70_;
				    }
				    if ((i_70_ = (i_67_ - i_39_) / i_39_)
					> i_69_)
					i_69_ = i_70_;
				    for (/**/; i_69_ < 0; i_69_++) {
					int i_71_
					    = (anIntArray2476
					       [((i_68_ >> 12) * width
						 + (i_67_ >> 12))]);
					if (i_71_ != 0)
					    DrawingArea
						.buffer[i_66_++]
						= i_71_;
					else
					    i_66_++;
					i_67_ += i_39_;
				    }
				}
				i_65_++;
				i_43_ += i_39_;
				i_36_ += DrawingArea.bufferWidth;
			    }
			} else if (i_38_ < 0) {
			    int i_72_ = i_35_;
			    while (i_72_ < 0) {
				int i_73_ = i_36_;
				int i_74_ = i_42_ + (i_40_ * i_39_ >> 4);
				int i_75_ = i_43_ + (i_40_ * i_38_ >> 4);
				int i_76_ = i_33_;
				int i_77_;
				if ((i_77_ = i_74_ - (width << 12)) >= 0) {
				    i_77_ = (i_39_ - i_77_) / i_39_;
				    i_76_ += i_77_;
				    i_74_ += i_39_ * i_77_;
				    i_75_ += i_38_ * i_77_;
				    i_73_ += i_77_;
				}
				if ((i_77_ = (i_74_ - i_39_) / i_39_) > i_76_)
				    i_76_ = i_77_;
				if ((i_77_ = i_75_ - (height << 12)) >= 0) {
				    i_77_ = (i_38_ - i_77_) / i_38_;
				    i_76_ += i_77_;
				    i_74_ += i_39_ * i_77_;
				    i_75_ += i_38_ * i_77_;
				    i_73_ += i_77_;
				}
				if ((i_77_ = (i_75_ - i_38_) / i_38_) > i_76_)
				    i_76_ = i_77_;
				for (/**/; i_76_ < 0; i_76_++) {
				    int i_78_ = (anIntArray2476
						 [((i_75_ >> 12) * width
						   + (i_74_ >> 12))]);
				    if (i_78_ != 0)
					DrawingArea.buffer
					    [i_73_++]
					    = i_78_;
				    else
					i_73_++;
				    i_74_ += i_39_;
				    i_75_ += i_38_;
				}
				i_72_++;
				i_42_ -= i_38_;
				i_43_ += i_39_;
				i_36_ += DrawingArea.bufferWidth;
			    }
			} else {
			    int i_79_ = i_35_;
			    while (i_79_ < 0) {
				int i_80_ = i_36_;
				int i_81_ = i_42_ + (i_40_ * i_39_ >> 4);
				int i_82_ = i_43_ + (i_40_ * i_38_ >> 4);
				int i_83_ = i_33_;
				int i_84_;
				if ((i_84_ = i_81_ - (width << 12)) >= 0) {
				    i_84_ = (i_39_ - i_84_) / i_39_;
				    i_83_ += i_84_;
				    i_81_ += i_39_ * i_84_;
				    i_82_ += i_38_ * i_84_;
				    i_80_ += i_84_;
				}
				if ((i_84_ = (i_81_ - i_39_) / i_39_) > i_83_)
				    i_83_ = i_84_;
				if (i_82_ < 0) {
				    i_84_ = (i_38_ - 1 - i_82_) / i_38_;
				    i_83_ += i_84_;
				    i_81_ += i_39_ * i_84_;
				    i_82_ += i_38_ * i_84_;
				    i_80_ += i_84_;
				}
				if ((i_84_ = (i_82_ + 1 - (height << 12)
					      - i_38_) / i_38_)
				    > i_83_)
				    i_83_ = i_84_;
				for (/**/; i_83_ < 0; i_83_++) {
				    int i_85_ = (anIntArray2476
						 [((i_82_ >> 12) * width
						   + (i_81_ >> 12))]);
				    if (i_85_ != 0)
					DrawingArea.buffer
					    [i_80_++]
					    = i_85_;
				    else
					i_80_++;
				    i_81_ += i_39_;
				    i_82_ += i_38_;
				}
				i_79_++;
				i_42_ -= i_38_;
				i_43_ += i_39_;
				i_36_ += DrawingArea.bufferWidth;
			    }
			}
		    } else if (i_38_ == 0) {
			int i_86_ = i_35_;
			while (i_86_ < 0) {
			    int i_87_ = i_36_;
			    int i_88_ = i_42_ + (i_40_ * i_39_ >> 4);
			    int i_89_ = i_43_;
			    int i_90_ = i_33_;
			    if (i_89_ >= 0 && i_89_ - (height << 12) < 0) {
				if (i_88_ < 0) {
				    int i_91_ = (i_39_ - 1 - i_88_) / i_39_;
				    i_90_ += i_91_;
				    i_88_ += i_39_ * i_91_;
				    i_87_ += i_91_;
				}
				int i_92_;
				if ((i_92_ = (i_88_ + 1 - (width << 12)
					      - i_39_) / i_39_)
				    > i_90_)
				    i_90_ = i_92_;
				for (/**/; i_90_ < 0; i_90_++) {
				    int i_93_ = (anIntArray2476
						 [((i_89_ >> 12) * width
						   + (i_88_ >> 12))]);
				    if (i_93_ != 0)
					DrawingArea.buffer
					    [i_87_++]
					    = i_93_;
				    else
					i_87_++;
				    i_88_ += i_39_;
				}
			    }
			    i_86_++;
			    i_43_ += i_39_;
			    i_36_ += DrawingArea.bufferWidth;
			}
		    } else if (i_38_ < 0) {
			int i_94_ = i_35_;
			while (i_94_ < 0) {
			    int i_95_ = i_36_;
			    int i_96_ = i_42_ + (i_40_ * i_39_ >> 4);
			    int i_97_ = i_43_ + (i_40_ * i_38_ >> 4);
			    int i_98_ = i_33_;
			    if (i_96_ < 0) {
				int i_99_ = (i_39_ - 1 - i_96_) / i_39_;
				i_98_ += i_99_;
				i_96_ += i_39_ * i_99_;
				i_97_ += i_38_ * i_99_;
				i_95_ += i_99_;
			    }
			    int i_100_;
			    if ((i_100_ = (i_96_ + 1 - (width << 12)
					   - i_39_) / i_39_)
				> i_98_)
				i_98_ = i_100_;
			    if ((i_100_ = i_97_ - (height << 12)) >= 0) {
				i_100_ = (i_38_ - i_100_) / i_38_;
				i_98_ += i_100_;
				i_96_ += i_39_ * i_100_;
				i_97_ += i_38_ * i_100_;
				i_95_ += i_100_;
			    }
			    if ((i_100_ = (i_97_ - i_38_) / i_38_) > i_98_)
				i_98_ = i_100_;
			    for (/**/; i_98_ < 0; i_98_++) {
				int i_101_
				    = (anIntArray2476
				       [(i_97_ >> 12) * width + (i_96_
								     >> 12)]);
				if (i_101_ != 0)
				    DrawingArea.buffer[i_95_++]
					= i_101_;
				else
				    i_95_++;
				i_96_ += i_39_;
				i_97_ += i_38_;
			    }
			    i_94_++;
			    i_42_ -= i_38_;
			    i_43_ += i_39_;
			    i_36_ += DrawingArea.bufferWidth;
			}
		    } else {
			int i_102_ = i_35_;
			while (i_102_ < 0) {
			    int i_103_ = i_36_;
			    int i_104_ = i_42_ + (i_40_ * i_39_ >> 4);
			    int i_105_ = i_43_ + (i_40_ * i_38_ >> 4);
			    int i_106_ = i_33_;
			    if (i_104_ < 0) {
				int i_107_ = (i_39_ - 1 - i_104_) / i_39_;
				i_106_ += i_107_;
				i_104_ += i_39_ * i_107_;
				i_105_ += i_38_ * i_107_;
				i_103_ += i_107_;
			    }
			    int i_108_;
			    if ((i_108_ = (i_104_ + 1 - (width << 12)
					   - i_39_) / i_39_)
				> i_106_)
				i_106_ = i_108_;
			    if (i_105_ < 0) {
				i_108_ = (i_38_ - 1 - i_105_) / i_38_;
				i_106_ += i_108_;
				i_104_ += i_39_ * i_108_;
				i_105_ += i_38_ * i_108_;
				i_103_ += i_108_;
			    }
			    if ((i_108_ = (i_105_ + 1 - (height << 12)
					   - i_38_) / i_38_)
				> i_106_)
				i_106_ = i_108_;
			    for (/**/; i_106_ < 0; i_106_++) {
				int i_109_
				    = (anIntArray2476
				       [(i_105_ >> 12) * width + (i_104_
								      >> 12)]);
				if (i_109_ != 0)
				    DrawingArea.buffer[i_103_++]
					= i_109_;
				else
				    i_103_++;
				i_104_ += i_39_;
				i_105_ += i_38_;
			    }
			    i_102_++;
			    i_42_ -= i_38_;
			    i_43_ += i_39_;
			    i_36_ += DrawingArea.bufferWidth;
			}
		    }
		}
	    }
	}
    }
    
    public void method670(int i, int i_110_) {
	i += anInt2473;
	i_110_ += anInt2472;
	int i_111_ = i + i_110_ * DrawingArea.bufferWidth;
	int i_112_ = 0;
	int i_113_ = height;
	int i_114_ = width;
	int i_115_ = DrawingArea.bufferWidth - i_114_;
	int i_116_ = 0;
	if (i_110_ < DrawingArea.areaOffsetY) {
	    int i_117_ = DrawingArea.areaOffsetY - i_110_;
	    i_113_ -= i_117_;
	    i_110_ = DrawingArea.areaOffsetY;
	    i_112_ += i_117_ * i_114_;
	    i_111_ += i_117_ * DrawingArea.bufferWidth;
	}
	if (i_110_ + i_113_ > DrawingArea.areaHeight)
	    i_113_ -= i_110_ + i_113_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_118_ = DrawingArea.areaOffsetX - i;
	    i_114_ -= i_118_;
	    i = DrawingArea.areaOffsetX;
	    i_112_ += i_118_;
	    i_111_ += i_118_;
	    i_116_ += i_118_;
	    i_115_ += i_118_;
	}
	if (i + i_114_ > DrawingArea.areaWidth) {
	    int i_119_ = i + i_114_ - DrawingArea.areaWidth;
	    i_114_ -= i_119_;
	    i_116_ += i_119_;
	    i_115_ += i_119_;
	}
	if (i_114_ > 0 && i_113_ > 0)
	    method682(DrawingArea.buffer, anIntArray2476, 0,
		      i_112_, i_111_, i_114_, i_113_, i_115_, i_116_);
    }
    
    public void method671() {
	int[] is = new int[width * height];
	int i = 0;
	for (int i_120_ = height - 1; i_120_ >= 0; i_120_--) {
	    for (int i_121_ = 0; i_121_ < width; i_121_++)
		is[i++] = anIntArray2476[i_121_ + i_120_ * width];
	}
	anIntArray2476 = is;
	anInt2472 = anInt2477 - height - anInt2472;
    }
    
    public void method672(int i) {
	for (int i_122_ = height - 1; i_122_ > 0; i_122_--) {
	    int i_123_ = i_122_ * width;
	    for (int i_124_ = width - 1; i_124_ > 0; i_124_--) {
		if (anIntArray2476[i_124_ + i_123_] == 0
		    && anIntArray2476[i_124_ + i_123_ - 1 - width] != 0)
		    anIntArray2476[i_124_ + i_123_] = i;
	    }
	}
    }
    
    public void method673(int i) {
	if (width != anInt2475 || height != anInt2477) {
	    int i_125_ = i;
	    if (i_125_ > anInt2473)
		i_125_ = anInt2473;
	    int i_126_ = i;
	    if (i_126_ + anInt2473 + width > anInt2475)
		i_126_ = anInt2475 - anInt2473 - width;
	    int i_127_ = i;
	    if (i_127_ > anInt2472)
		i_127_ = anInt2472;
	    int i_128_ = i;
	    if (i_128_ + anInt2472 + height > anInt2477)
		i_128_ = anInt2477 - anInt2472 - height;
	    int i_129_ = width + i_125_ + i_126_;
	    int i_130_ = height + i_127_ + i_128_;
	    int[] is = new int[i_129_ * i_130_];
	    for (int i_131_ = 0; i_131_ < height; i_131_++) {
		for (int i_132_ = 0; i_132_ < width; i_132_++)
		    is[(i_131_ + i_127_) * i_129_ + (i_132_ + i_125_)]
			= anIntArray2476[i_131_ * width + i_132_];
	    }
	    anIntArray2476 = is;
	    width = i_129_;
	    height = i_130_;
	    anInt2473 -= i_125_;
	    anInt2472 -= i_127_;
	}
    }
    
    public static void method674(int[] is, int[] is_133_, int i, int i_134_,
				 int i_135_, int i_136_, int i_137_,
				 int i_138_, int i_139_, byte[] is_140_) {
	int i_141_ = -(i_136_ >> 2);
	i_136_ = -(i_136_ & 0x3);
	for (int i_142_ = -i_137_; i_142_ < 0; i_142_++) {
	    for (int i_143_ = i_141_; i_143_ < 0; i_143_++) {
		i = is_133_[i_134_++];
		if (i != 0 && is_140_[i_135_] == 0)
		    is[i_135_++] = i;
		else
		    i_135_++;
		i = is_133_[i_134_++];
		if (i != 0 && is_140_[i_135_] == 0)
		    is[i_135_++] = i;
		else
		    i_135_++;
		i = is_133_[i_134_++];
		if (i != 0 && is_140_[i_135_] == 0)
		    is[i_135_++] = i;
		else
		    i_135_++;
		i = is_133_[i_134_++];
		if (i != 0 && is_140_[i_135_] == 0)
		    is[i_135_++] = i;
		else
		    i_135_++;
	    }
	    for (int i_144_ = i_136_; i_144_ < 0; i_144_++) {
		i = is_133_[i_134_++];
		if (i != 0 && is_140_[i_135_] == 0)
		    is[i_135_++] = i;
		else
		    i_135_++;
	    }
	    i_135_ += i_138_;
	    i_134_ += i_139_;
	}
    }
    
    public static void method675(int[] is, int[] is_145_, int i, int i_146_,
				 int i_147_, int i_148_, int i_149_,
				 int i_150_) {
	for (int i_151_ = -i_148_; i_151_ < 0; i_151_++) {
	    int i_152_ = i_146_ + i_147_ - 3;
	    while (i_146_ < i_152_) {
		is[i_146_++] = is_145_[i++];
		is[i_146_++] = is_145_[i++];
		is[i_146_++] = is_145_[i++];
		is[i_146_++] = is_145_[i++];
	    }
	    i_152_ += 3;
	    while (i_146_ < i_152_)
		is[i_146_++] = is_145_[i++];
	    i_146_ += i_149_;
	    i += i_150_;
	}
    }
    
    public void method676(int i, int i_153_, int i_154_) {
	i += anInt2473;
	i_153_ += anInt2472;
	int i_155_ = i + i_153_ * DrawingArea.bufferWidth;
	int i_156_ = 0;
	int i_157_ = height;
	int i_158_ = width;
	int i_159_ = DrawingArea.bufferWidth - i_158_;
	int i_160_ = 0;
	if (i_153_ < DrawingArea.areaOffsetY) {
	    int i_161_ = DrawingArea.areaOffsetY - i_153_;
	    i_157_ -= i_161_;
	    i_153_ = DrawingArea.areaOffsetY;
	    i_156_ += i_161_ * i_158_;
	    i_155_ += i_161_ * DrawingArea.bufferWidth;
	}
	if (i_153_ + i_157_ > DrawingArea.areaHeight)
	    i_157_ -= i_153_ + i_157_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_162_ = DrawingArea.areaOffsetX - i;
	    i_158_ -= i_162_;
	    i = DrawingArea.areaOffsetX;
	    i_156_ += i_162_;
	    i_155_ += i_162_;
	    i_160_ += i_162_;
	    i_159_ += i_162_;
	}
	if (i + i_158_ > DrawingArea.areaWidth) {
	    int i_163_ = i + i_158_ - DrawingArea.areaWidth;
	    i_158_ -= i_163_;
	    i_160_ += i_163_;
	    i_159_ += i_163_;
	}
	if (i_158_ > 0 && i_157_ > 0)
	    method685(DrawingArea.buffer, anIntArray2476, 0,
		      i_156_, i_155_, i_158_, i_157_, i_159_, i_160_, i_154_);
    }
    
    public void method677(int i, int i_164_) {
	i += anInt2473;
	i_164_ += anInt2472;
	int i_165_ = i + i_164_ * DrawingArea.bufferWidth;
	int i_166_ = 0;
	int i_167_ = height;
	int i_168_ = width;
	int i_169_ = DrawingArea.bufferWidth - i_168_;
	int i_170_ = 0;
	if (i_164_ < DrawingArea.areaOffsetY) {
	    int i_171_ = DrawingArea.areaOffsetY - i_164_;
	    i_167_ -= i_171_;
	    i_164_ = DrawingArea.areaOffsetY;
	    i_166_ += i_171_ * i_168_;
	    i_165_ += i_171_ * DrawingArea.bufferWidth;
	}
	if (i_164_ + i_167_ > DrawingArea.areaHeight)
	    i_167_ -= i_164_ + i_167_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_172_ = DrawingArea.areaOffsetX - i;
	    i_168_ -= i_172_;
	    i = DrawingArea.areaOffsetX;
	    i_166_ += i_172_;
	    i_165_ += i_172_;
	    i_170_ += i_172_;
	    i_169_ += i_172_;
	}
	if (i + i_168_ > DrawingArea.areaWidth) {
	    int i_173_ = i + i_168_ - DrawingArea.areaWidth;
	    i_168_ -= i_173_;
	    i_170_ += i_173_;
	    i_169_ += i_173_;
	}
	if (i_168_ > 0 && i_167_ > 0)
	    method675(DrawingArea.buffer, anIntArray2476,
		      i_166_, i_165_, i_168_, i_167_, i_169_, i_170_);
    }
    
    public static void method678(int[] is, int[] is_174_, int i, int i_175_,
				 int i_176_, int i_177_, int i_178_,
				 int i_179_, int i_180_, int i_181_,
				 int i_182_, int i_183_, int i_184_) {
	int i_185_ = 256 - i_184_;
	int i_186_ = i_175_;
	for (int i_187_ = -i_180_; i_187_ < 0; i_187_++) {
	    int i_188_ = (i_176_ >> 16) * i_183_;
	    for (int i_189_ = -i_179_; i_189_ < 0; i_189_++) {
		i = is_174_[(i_175_ >> 16) + i_188_];
		if (i != 0) {
		    int i_190_ = is[i_177_];
		    is[i_177_++] = ((((i & 0xff00ff) * i_184_
				      + (i_190_ & 0xff00ff) * i_185_)
				     & ~0xff00ff)
				    + (((i & 0xff00) * i_184_
					+ (i_190_ & 0xff00) * i_185_)
				       & 0xff0000)) >> 8;
		} else
		    i_177_++;
		i_175_ += i_181_;
	    }
	    i_176_ += i_182_;
	    i_175_ = i_186_;
	    i_177_ += i_178_;
	}
    }
    
    public void method679() {
	DrawingArea.setBuffer(anIntArray2476, width, height);
    }
    
    public void method680(int i, int i_191_, int i_192_, int i_193_,
			  int i_194_) {
	if (i_192_ > 0 && i_193_ > 0) {
	    int i_195_ = width;
	    int i_196_ = height;
	    int i_197_ = 0;
	    int i_198_ = 0;
	    int i_199_ = anInt2475;
	    int i_200_ = anInt2477;
	    int i_201_ = (i_199_ << 16) / i_192_;
	    int i_202_ = (i_200_ << 16) / i_193_;
	    if (anInt2473 > 0) {
		int i_203_ = ((anInt2473 << 16) + i_201_ - 1) / i_201_;
		i += i_203_;
		i_197_ += i_203_ * i_201_ - (anInt2473 << 16);
	    }
	    if (anInt2472 > 0) {
		int i_204_ = ((anInt2472 << 16) + i_202_ - 1) / i_202_;
		i_191_ += i_204_;
		i_198_ += i_204_ * i_202_ - (anInt2472 << 16);
	    }
	    if (i_195_ < i_199_)
		i_192_ = ((i_195_ << 16) - i_197_ + i_201_ - 1) / i_201_;
	    if (i_196_ < i_200_)
		i_193_ = ((i_196_ << 16) - i_198_ + i_202_ - 1) / i_202_;
	    int i_205_ = i + i_191_ * DrawingArea.bufferWidth;
	    int i_206_ = DrawingArea.bufferWidth - i_192_;
	    if (i_191_ + i_193_ > DrawingArea.areaHeight)
		i_193_ -= i_191_ + i_193_ - DrawingArea.areaHeight;
	    if (i_191_ < DrawingArea.areaOffsetY) {
		int i_207_ = DrawingArea.areaOffsetY - i_191_;
		i_193_ -= i_207_;
		i_205_ += i_207_ * DrawingArea.bufferWidth;
		i_198_ += i_202_ * i_207_;
	    }
	    if (i + i_192_ > DrawingArea.areaWidth) {
		int i_208_ = i + i_192_ - DrawingArea.areaWidth;
		i_192_ -= i_208_;
		i_206_ += i_208_;
	    }
	    if (i < DrawingArea.areaOffsetX) {
		int i_209_ = DrawingArea.areaOffsetX - i;
		i_192_ -= i_209_;
		i_205_ += i_209_;
		i_197_ += i_201_ * i_209_;
		i_206_ += i_209_;
	    }
	    method678(DrawingArea.buffer, anIntArray2476, 0,
		      i_197_, i_198_, i_205_, i_206_, i_192_, i_193_, i_201_,
		      i_202_, i_195_, i_194_);
	}
    }
    
    public void method681() {
	int[] is = new int[width * height];
	int i = 0;
	for (int i_210_ = 0; i_210_ < height; i_210_++) {
	    for (int i_211_ = width - 1; i_211_ >= 0; i_211_--)
		is[i++] = anIntArray2476[i_211_ + i_210_ * width];
	}
	anIntArray2476 = is;
	anInt2473 = anInt2475 - width - anInt2473;
    }
    
    public static void method682(int[] is, int[] is_212_, int i, int i_213_,
				 int i_214_, int i_215_, int i_216_,
				 int i_217_, int i_218_) {
	int i_219_ = -(i_215_ >> 2);
	i_215_ = -(i_215_ & 0x3);
	for (int i_220_ = -i_216_; i_220_ < 0; i_220_++) {
	    for (int i_221_ = i_219_; i_221_ < 0; i_221_++) {
		i = is_212_[i_213_++];
		if (i != 0)
		    is[i_214_++] = i;
		else
		    i_214_++;
		i = is_212_[i_213_++];
		if (i != 0)
		    is[i_214_++] = i;
		else
		    i_214_++;
		i = is_212_[i_213_++];
		if (i != 0)
		    is[i_214_++] = i;
		else
		    i_214_++;
		i = is_212_[i_213_++];
		if (i != 0)
		    is[i_214_++] = i;
		else
		    i_214_++;
	    }
	    for (int i_222_ = i_215_; i_222_ < 0; i_222_++) {
		i = is_212_[i_213_++];
		if (i != 0)
		    is[i_214_++] = i;
		else
		    i_214_++;
	    }
	    i_214_ += i_217_;
	    i_213_ += i_218_;
	}
    }
    
    public void method683(int i) {
	int[] is = new int[width * height];
	int i_223_ = 0;
	for (int i_224_ = 0; i_224_ < height; i_224_++) {
	    for (int i_225_ = 0; i_225_ < width; i_225_++) {
		int i_226_ = anIntArray2476[i_223_];
		if (i_226_ == 0) {
		    if (i_225_ > 0 && anIntArray2476[i_223_ - 1] != 0)
			i_226_ = i;
		    else if (i_224_ > 0
			     && anIntArray2476[i_223_ - width] != 0)
			i_226_ = i;
		    else if (i_225_ < width - 1
			     && anIntArray2476[i_223_ + 1] != 0)
			i_226_ = i;
		    else if (i_224_ < height - 1
			     && anIntArray2476[i_223_ + width] != 0)
			i_226_ = i;
		}
		is[i_223_++] = i_226_;
	    }
	}
	anIntArray2476 = is;
    }
    
    public void method684(int i, int i_227_, int i_228_, int i_229_) {
	method669(anInt2475 << 3, anInt2477 << 3, i << 4, i_227_ << 4, i_228_,
		  i_229_);
    }
    
    public static void method685(int[] is, int[] is_230_, int i, int i_231_,
				 int i_232_, int i_233_, int i_234_,
				 int i_235_, int i_236_, int i_237_) {
	int i_238_ = 256 - i_237_;
	for (int i_239_ = -i_234_; i_239_ < 0; i_239_++) {
	    for (int i_240_ = -i_233_; i_240_ < 0; i_240_++) {
		i = is_230_[i_231_++];
		if (i != 0) {
		    int i_241_ = is[i_232_];
		    is[i_232_++] = ((((i & 0xff00ff) * i_237_
				      + (i_241_ & 0xff00ff) * i_238_)
				     & ~0xff00ff)
				    + (((i & 0xff00) * i_237_
					+ (i_241_ & 0xff00) * i_238_)
				       & 0xff0000)) >> 8;
		} else
		    i_232_++;
	    }
	    i_232_ += i_235_;
	    i_231_ += i_236_;
	}
    }
    
    public void method686(int i, int i_242_, int i_243_, int i_244_,
			  int i_245_, int i_246_, double d, int i_247_) {
	try {
	    int i_248_ = -i_243_ / 2;
	    int i_249_ = -i_244_ / 2;
	    int i_250_ = (int) (Math.sin(d) * 65536.0);
	    int i_251_ = (int) (Math.cos(d) * 65536.0);
	    i_250_ = i_250_ * i_247_ >> 8;
	    i_251_ = i_251_ * i_247_ >> 8;
	    int i_252_ = (i_245_ << 16) + (i_249_ * i_250_ + i_248_ * i_251_);
	    int i_253_ = (i_246_ << 16) + (i_249_ * i_251_ - i_248_ * i_250_);
	    int i_254_ = i + i_242_ * DrawingArea.bufferWidth;
	    for (i_242_ = 0; i_242_ < i_244_; i_242_++) {
		int i_255_ = i_254_;
		int i_256_ = i_252_;
		int i_257_ = i_253_;
		for (i = -i_243_; i < 0; i++) {
		    int i_258_
			= (anIntArray2476
			   [(i_256_ >> 16) + (i_257_ >> 16) * width]);
		    if (i_258_ != 0)
			DrawingArea.buffer[i_255_++] = i_258_;
		    else
			i_255_++;
		    i_256_ += i_251_;
		    i_257_ -= i_250_;
		}
		i_252_ += i_250_;
		i_253_ += i_251_;
		i_254_ += DrawingArea.bufferWidth;
	    }
	} catch (Exception exception) {
	    /* empty */
	}
    }
    
    public void method687(int i, int i_259_, int i_260_, int i_261_) {
	if (i_260_ > 0 && i_261_ > 0) {
	    int i_262_ = width;
	    int i_263_ = height;
	    int i_264_ = 0;
	    int i_265_ = 0;
	    int i_266_ = anInt2475;
	    int i_267_ = anInt2477;
	    int i_268_ = (i_266_ << 16) / i_260_;
	    int i_269_ = (i_267_ << 16) / i_261_;
	    if (anInt2473 > 0) {
		int i_270_ = ((anInt2473 << 16) + i_268_ - 1) / i_268_;
		i += i_270_;
		i_264_ += i_270_ * i_268_ - (anInt2473 << 16);
	    }
	    if (anInt2472 > 0) {
		int i_271_ = ((anInt2472 << 16) + i_269_ - 1) / i_269_;
		i_259_ += i_271_;
		i_265_ += i_271_ * i_269_ - (anInt2472 << 16);
	    }
	    if (i_262_ < i_266_)
		i_260_ = ((i_262_ << 16) - i_264_ + i_268_ - 1) / i_268_;
	    if (i_263_ < i_267_)
		i_261_ = ((i_263_ << 16) - i_265_ + i_269_ - 1) / i_269_;
	    int i_272_ = i + i_259_ * DrawingArea.bufferWidth;
	    int i_273_ = DrawingArea.bufferWidth - i_260_;
	    if (i_259_ + i_261_ > DrawingArea.areaHeight)
		i_261_ -= i_259_ + i_261_ - DrawingArea.areaHeight;
	    if (i_259_ < DrawingArea.areaOffsetY) {
		int i_274_ = DrawingArea.areaOffsetY - i_259_;
		i_261_ -= i_274_;
		i_272_ += i_274_ * DrawingArea.bufferWidth;
		i_265_ += i_269_ * i_274_;
	    }
	    if (i + i_260_ > DrawingArea.areaWidth) {
		int i_275_ = i + i_260_ - DrawingArea.areaWidth;
		i_260_ -= i_275_;
		i_273_ += i_275_;
	    }
	    if (i < DrawingArea.areaOffsetX) {
		int i_276_ = DrawingArea.areaOffsetX - i;
		i_260_ -= i_276_;
		i_272_ += i_276_;
		i_264_ += i_268_ * i_276_;
		i_273_ += i_276_;
	    }
	    method690(DrawingArea.buffer, anIntArray2476, 0,
		      i_264_, i_265_, i_272_, i_273_, i_260_, i_261_, i_268_,
		      i_269_, i_262_);
	}
    }
    
    public void method688(int i, int i_277_, int i_278_, int i_279_,
			  int i_280_, int i_281_, int i_282_, int i_283_,
			  int[] is, int[] is_284_) {
	try {
	    int i_285_ = -i_278_ / 2;
	    int i_286_ = -i_279_ / 2;
	    int i_287_ = (int) (Math.sin((double) i_282_ / 326.11) * 65536.0);
	    int i_288_ = (int) (Math.cos((double) i_282_ / 326.11) * 65536.0);
	    i_287_ = i_287_ * i_283_ >> 8;
	    i_288_ = i_288_ * i_283_ >> 8;
	    int i_289_ = (i_280_ << 16) + (i_286_ * i_287_ + i_285_ * i_288_);
	    int i_290_ = (i_281_ << 16) + (i_286_ * i_288_ - i_285_ * i_287_);
	    int i_291_ = i + i_277_ * DrawingArea.bufferWidth;
	    for (i_277_ = 0; i_277_ < i_279_; i_277_++) {
		int i_292_ = is[i_277_];
		int i_293_ = i_291_ + i_292_;
		int i_294_ = i_289_ + i_288_ * i_292_;
		int i_295_ = i_290_ - i_287_ * i_292_;
		for (i = -is_284_[i_277_]; i < 0; i++) {
		    DrawingArea.buffer[i_293_++]
			= (anIntArray2476
			   [(i_294_ >> 16) + (i_295_ >> 16) * width]);
		    i_294_ += i_288_;
		    i_295_ -= i_287_;
		}
		i_289_ += i_287_;
		i_290_ += i_288_;
		i_291_ += DrawingArea.bufferWidth;
	    }
	} catch (Exception exception) {
	    /* empty */
	}
    }
    
    public void method689() {
	if (width != anInt2475 || height != anInt2477) {
	    int[] is = new int[anInt2475 * anInt2477];
	    for (int i = 0; i < height; i++) {
		for (int i_296_ = 0; i_296_ < width; i_296_++)
		    is[(i + anInt2472) * anInt2475 + (i_296_ + anInt2473)]
			= anIntArray2476[i * width + i_296_];
	    }
	    anIntArray2476 = is;
	    width = anInt2475;
	    height = anInt2477;
	    anInt2473 = 0;
	    anInt2472 = 0;
	}
    }
    
    public static void method690(int[] is, int[] is_297_, int i, int i_298_,
				 int i_299_, int i_300_, int i_301_,
				 int i_302_, int i_303_, int i_304_,
				 int i_305_, int i_306_) {
	int i_307_ = i_298_;
	for (int i_308_ = -i_303_; i_308_ < 0; i_308_++) {
	    int i_309_ = (i_299_ >> 16) * i_306_;
	    for (int i_310_ = -i_302_; i_310_ < 0; i_310_++) {
		i = is_297_[(i_298_ >> 16) + i_309_];
		if (i != 0)
		    is[i_300_++] = i;
		else
		    i_300_++;
		i_298_ += i_304_;
	    }
	    i_299_ += i_305_;
	    i_298_ = i_307_;
	    i_300_ += i_301_;
	}
    }
    
    public DirectColorSprite() {
	/* empty */
    }
    
    public DirectColorSprite(int i, int i_311_) {
	anIntArray2476 = new int[i * i_311_];
	width = anInt2475 = i;
	height = anInt2477 = i_311_;
	anInt2473 = anInt2472 = 0;
    }
    
    public DirectColorSprite(byte[] src, Component component) {
	try {
	    Image image = Toolkit.getDefaultToolkit().createImage(src);
	    MediaTracker mediatracker = new MediaTracker(component);
	    mediatracker.addImage(image, 0);
	    mediatracker.waitForAll();
	    width = image.getWidth(component);
	    height = image.getHeight(component);
	    anInt2475 = width;
	    anInt2477 = height;
	    anInt2473 = 0;
	    anInt2472 = 0;
	    anIntArray2476 = new int[width * height];
	    PixelGrabber pixelgrabber
		= new PixelGrabber(image, 0, 0, width, height,
				   anIntArray2476, 0, width);
	    pixelgrabber.grabPixels();
	} catch (InterruptedException interruptedexception) {
	    /* empty */
	}
    }
}
