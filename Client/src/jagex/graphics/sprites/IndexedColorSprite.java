package jagex.graphics.sprites;

import jagex.graphics.DrawingArea;

/* Class39_Sub5_Sub10_Sub4 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class IndexedColorSprite extends DrawingArea
{
    public int offsetY;
    public int anInt2480;
    public int anInt2481;
    public int anInt2482;
    public int anInt2483;
    public int offsetX;
    public int[] colors;
    public byte[] index;
    
    public void method691() {
	byte[] is = new byte[anInt2480 * anInt2481];
	int i = 0;
	for (int i_0_ = 0; i_0_ < anInt2481; i_0_++) {
	    for (int i_1_ = anInt2480 - 1; i_1_ >= 0; i_1_--)
		is[i++] = index[i_1_ + i_0_ * anInt2480];
	}
	index = is;
	offsetX = anInt2482 - anInt2480 - offsetX;
    }
    
    public IndexedColorSprite method692() {
	IndexedColorSprite class39_sub5_sub10_sub4_2_ = new IndexedColorSprite(anInt2480, anInt2481, colors.length);
	class39_sub5_sub10_sub4_2_.anInt2482 = anInt2482;
	class39_sub5_sub10_sub4_2_.anInt2483 = anInt2483;
	class39_sub5_sub10_sub4_2_.offsetX = offsetX;
	class39_sub5_sub10_sub4_2_.offsetY = offsetY;
	int i = index.length;
	for (int i_3_ = 0; i_3_ < i; i_3_++)
	    class39_sub5_sub10_sub4_2_.index[i_3_]
		= index[i_3_];
	i = colors.length;
	for (int i_4_ = 0; i_4_ < i; i_4_++)
	    class39_sub5_sub10_sub4_2_.colors[i_4_]
		= colors[i_4_];
	return class39_sub5_sub10_sub4_2_;
    }
    
    public void method693(int i, int i_5_, int i_6_) {
	for (int i_7_ = 0; i_7_ < colors.length; i_7_++) {
	    int i_8_ = colors[i_7_] >> 16 & 0xff;
	    i_8_ += i;
	    if (i_8_ < 0)
		i_8_ = 0;
	    else if (i_8_ > 255)
		i_8_ = 255;
	    int i_9_ = colors[i_7_] >> 8 & 0xff;
	    i_9_ += i_5_;
	    if (i_9_ < 0)
		i_9_ = 0;
	    else if (i_9_ > 255)
		i_9_ = 255;
	    int i_10_ = colors[i_7_] & 0xff;
	    i_10_ += i_6_;
	    if (i_10_ < 0)
		i_10_ = 0;
	    else if (i_10_ > 255)
		i_10_ = 255;
	    colors[i_7_] = (i_8_ << 16) + (i_9_ << 8) + i_10_;
	}
    }
    
    public void method694() {
	byte[] is = new byte[anInt2480 * anInt2481];
	int i = 0;
	for (int i_11_ = anInt2481 - 1; i_11_ >= 0; i_11_--) {
	    for (int i_12_ = 0; i_12_ < anInt2480; i_12_++)
		is[i++] = index[i_12_ + i_11_ * anInt2480];
	}
	index = is;
	offsetY = anInt2483 - anInt2481 - offsetY;
    }
    
    public void method695(int i, int i_13_) {
	i += offsetX;
	i_13_ += offsetY;
	int i_14_ = i + i_13_ * DrawingArea.bufferWidth;
	int i_15_ = 0;
	int i_16_ = anInt2481;
	int i_17_ = anInt2480;
	int i_18_ = DrawingArea.bufferWidth - i_17_;
	int i_19_ = 0;
	if (i_13_ < DrawingArea.areaOffsetY) {
	    int i_20_ = DrawingArea.areaOffsetY - i_13_;
	    i_16_ -= i_20_;
	    i_13_ = DrawingArea.areaOffsetY;
	    i_15_ += i_20_ * i_17_;
	    i_14_ += i_20_ * DrawingArea.bufferWidth;
	}
	if (i_13_ + i_16_ > DrawingArea.areaHeight)
	    i_16_ -= i_13_ + i_16_ - DrawingArea.areaHeight;
	if (i < DrawingArea.areaOffsetX) {
	    int i_21_ = DrawingArea.areaOffsetX - i;
	    i_17_ -= i_21_;
	    i = DrawingArea.areaOffsetX;
	    i_15_ += i_21_;
	    i_14_ += i_21_;
	    i_19_ += i_21_;
	    i_18_ += i_21_;
	}
	if (i + i_17_ > DrawingArea.areaWidth) {
	    int i_22_ = i + i_17_ - DrawingArea.areaWidth;
	    i_17_ -= i_22_;
	    i_19_ += i_22_;
	    i_18_ += i_22_;
	}
	if (i_17_ > 0 && i_16_ > 0)
	    method696(DrawingArea.buffer, index,
		      colors, i_15_, i_14_, i_17_, i_16_, i_18_,
		      i_19_);
    }
    
    public static void method696(int[] dest, byte[] is_23_, int[] src, int i,
				 int destOff, int i_26_, int i_27_, int i_28_,
				 int i_29_) {
	int i_30_ = -(i_26_ >> 2);
	i_26_ = -(i_26_ & 0x3);
	for (int i_31_ = -i_27_; i_31_ < 0; i_31_++) {
	    for (int i_32_ = i_30_; i_32_ < 0; i_32_++) {
		int i_33_ = is_23_[i++];
		if (i_33_ != 0)
		    dest[destOff++] = src[i_33_ & 0xff];
		else
		    destOff++;
		i_33_ = is_23_[i++];
		if (i_33_ != 0)
		    dest[destOff++] = src[i_33_ & 0xff];
		else
		    destOff++;
		i_33_ = is_23_[i++];
		if (i_33_ != 0)
		    dest[destOff++] = src[i_33_ & 0xff];
		else
		    destOff++;
		i_33_ = is_23_[i++];
		if (i_33_ != 0)
		    dest[destOff++] = src[i_33_ & 0xff];
		else
		    destOff++;
	    }
	    for (int i_34_ = i_26_; i_34_ < 0; i_34_++) {
		int i_35_ = is_23_[i++];
		if (i_35_ != 0)
		    dest[destOff++] = src[i_35_ & 0xff];
		else
		    destOff++;
	    }
	    destOff += i_28_;
	    i += i_29_;
	}
    }
    
    public void method697() {
	if (anInt2480 != anInt2482 || anInt2481 != anInt2483) {
	    byte[] is = new byte[anInt2482 * anInt2483];
	    int i = 0;
	    for (int i_36_ = 0; i_36_ < anInt2481; i_36_++) {
		for (int i_37_ = 0; i_37_ < anInt2480; i_37_++)
		    is[i_37_ + offsetX + (i_36_ + offsetY) * anInt2482]
			= index[i++];
	    }
	    index = is;
	    anInt2480 = anInt2482;
	    anInt2481 = anInt2483;
	    offsetX = 0;
	    offsetY = 0;
	}
    }
    
    public IndexedColorSprite() {
	/* empty */
    }
    
    public IndexedColorSprite(int width, int height, int amountColors) {
	anInt2482 = anInt2480 = width;
	anInt2483 = anInt2481 = height;
	offsetX = offsetY = 0;
	index = new byte[width * height];
	colors = new int[amountColors];
    }
}
