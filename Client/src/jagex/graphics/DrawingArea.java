package jagex.graphics;

import jagex.utils.SubNode;

/* Class39_Sub5_Sub10 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class DrawingArea extends SubNode {

    public static int[] buffer;
    public static int areaHeight;
    public static int areaOffsetY = 0;
    public static int bufferWidth;
    public static int bufferHeight;
    public static int areaWidth = 0;
    public static int areaOffsetX;

    public static void drawVerticalLine(int x, int y, int length, int color) {
        if (x >= areaOffsetX && x < areaWidth) {
            if (y < areaOffsetY) {
                length -= areaOffsetY - y;
                y = areaOffsetY;
            }
            if (y + length > areaHeight) {
                length = areaHeight - y;
            }
            int offset = x + y * bufferWidth;
            for (int i = 0; i < length; i++) {
                buffer[offset + i * bufferWidth] = color;
            }
        }
    }

    public static void setBuffer(int[] src, int w, int h) {
        buffer = src;
        bufferWidth = w;
        bufferHeight = h;
        setDimensions(0, 0, w, h);
    }

    public static void drawQuad(int i, int i_6_, int i_7_, int i_8_,
            int i_9_) {
        if (i < areaOffsetX) {
            i_7_ -= areaOffsetX - i;
            i = areaOffsetX;
        }
        if (i_6_ < areaOffsetY) {
            i_8_ -= areaOffsetY - i_6_;
            i_6_ = areaOffsetY;
        }
        if (i + i_7_ > areaWidth) {
            i_7_ = areaWidth - i;
        }
        if (i_6_ + i_8_ > areaHeight) {
            i_8_ = areaHeight - i_6_;
        }
        int i_10_ = bufferWidth - i_7_;
        int i_11_ = i + i_6_ * bufferWidth;
        for (int i_12_ = -i_8_; i_12_ < 0; i_12_++) {
            for (int i_13_ = -i_7_; i_13_ < 0; i_13_++) {
                buffer[i_11_++] = i_9_;
            }
            i_11_ += i_10_;
        }
    }

    public static void method612(int i, int i_14_, int i_15_, int i_16_,
            int i_17_) {
        i_15_ -= i;
        i_16_ -= i_14_;
        if (i_16_ == 0) {
            if (i_15_ >= 0) {
                drawHorizontalLine(i, i_14_, i_15_ + 1, i_17_);
            } else {
                drawHorizontalLine(i + i_15_, i_14_, -i_15_ + 1, i_17_);
            }
        } else if (i_15_ == 0) {
            if (i_16_ >= 0) {
                drawVerticalLine(i, i_14_, i_16_ + 1, i_17_);
            } else {
                drawVerticalLine(i, i_14_ + i_16_, -i_16_ + 1, i_17_);
            }
        } else {
            if (i_15_ + i_16_ < 0) {
                i += i_15_;
                i_15_ = -i_15_;
                i_14_ += i_16_;
                i_16_ = -i_16_;
            }
            if (i_15_ > i_16_) {
                i_14_ <<= 16;
                i_14_ += 32768;
                i_16_ <<= 16;
                int i_18_ = (int) Math.floor((double) i_16_ / (double) i_15_ + 0.5);
                i_15_ += i;
                if (i < areaOffsetX) {
                    i_14_ += i_18_ * (areaOffsetX - i);
                    i = areaOffsetX;
                }
                if (i_15_ >= areaWidth) {
                    i_15_ = areaWidth - 1;
                }
                for (/**/; i <= i_15_; i++) {
                    int i_19_ = i_14_ >> 16;
                    if (i_19_ >= areaOffsetY && i_19_ < areaHeight) {
                        buffer[i + i_19_ * bufferWidth] = i_17_;
                    }
                    i_14_ += i_18_;
                }
            } else {
                i <<= 16;
                i += 32768;
                i_15_ <<= 16;
                int i_20_ = (int) Math.floor((double) i_15_ / (double) i_16_ + 0.5);
                i_16_ += i_14_;
                if (i_14_ < areaOffsetY) {
                    i += i_20_ * (areaOffsetY - i_14_);
                    i_14_ = areaOffsetY;
                }
                if (i_16_ >= areaHeight) {
                    i_16_ = areaHeight - 1;
                }
                for (/**/; i_14_ <= i_16_; i_14_++) {
                    int i_21_ = i >> 16;
                    if (i_21_ >= areaOffsetX && i_21_ < areaWidth) {
                        buffer[i_21_ + i_14_ * bufferWidth] = i_17_;
                    }
                    i += i_20_;
                }
            }
        }
    }

    public static void destroy() {
        buffer = null;
    }

    public static void drawQuadOutline(int i, int i_22_, int i_23_, int i_24_,
            int i_25_) {
        drawHorizontalLine(i, i_22_, i_23_, i_25_);
        drawHorizontalLine(i, i_22_ + i_24_ - 1, i_23_, i_25_);
        drawVerticalLine(i, i_22_, i_24_, i_25_);
        drawVerticalLine(i + i_23_ - 1, i_22_, i_24_, i_25_);
    }

    public static void resetDimensions() {
        areaOffsetX = 0;
        areaOffsetY = 0;
        areaWidth = bufferWidth;
        areaHeight = bufferHeight;
    }

    public static void drawHorizontalLine(int x, int y, int len, int color) {
        if (y >= areaOffsetY && y < areaHeight) {
            if (x < areaOffsetX) {
                len -= areaOffsetX - x;
                x = areaOffsetX;
            }
            if (x + len > areaWidth) {
                len = areaWidth - x;
            }
            int i_29_ = x + y * bufferWidth;
            for (int i_30_ = 0; i_30_ < len; i_30_++) {
                buffer[i_29_ + i_30_] = color;
            }
        }
    }

    public static void drawHorizontalOverlayLine(int x, int y, int len, int color, int fillAlpha) {
        if (y >= areaOffsetY && y < areaHeight) {
            if (x < areaOffsetX) {
                len -= areaOffsetX - x;
                x = areaOffsetX;
            }
            if (x + len > areaWidth) {
                len = areaWidth - x;
            }
            int alpha = 256 - fillAlpha;
            int rChannel = (color >> 16 & 0xff) * fillAlpha;
            int gChannel = (color >> 8 & 0xff) * fillAlpha;
            int bChannel = (color & 0xff) * fillAlpha;
            int offset = x + y * bufferWidth;
            for (int i = 0; i < len; i++) {
                int rUnderlay = (buffer[offset] >> 16 & 0xff) * alpha;
                int gUnderlay = (buffer[offset] >> 8 & 0xff) * alpha;
                int bUnderlay = (buffer[offset] & 0xff) * alpha;
                int i_44_ = ((rChannel + rUnderlay >> 8 << 16) + (gChannel + gUnderlay >> 8 << 8)  + (bChannel + bUnderlay >> 8));
                buffer[offset++] = i_44_;
            }
        }
    }

    public static void drawVerticalOverlayLine(int x, int y, int len, int color,
            int fillAlpha) {
        if (x >= areaOffsetX && x < areaWidth) {
            if (y < areaOffsetY) {
                len -= areaOffsetY - y;
                y = areaOffsetY;
            }
            if (y + len > areaHeight) {
                len = areaHeight - y;
            }
            int alpha = 256 - fillAlpha;
            int rChannel = (color >> 16 & 0xff) * fillAlpha;
            int gChannel = (color >> 8 & 0xff) * fillAlpha;
            int bChannel = (color & 0xff) * fillAlpha;
            int offset = x + y * bufferWidth;
            for (int i_54_ = 0; i_54_ < len; i_54_++) {
                int rUnderlay = (buffer[offset] >> 16 & 0xff) * alpha;
                int gUnderlay = (buffer[offset] >> 8 & 0xff) * alpha;
                int bUnderlay = (buffer[offset] & 0xff) * alpha;
                int i_58_ = ((rChannel + rUnderlay >> 8 << 16) + (gChannel + gUnderlay >> 8 << 8) + (bChannel + bUnderlay >> 8));
                buffer[offset] = i_58_;
                offset += bufferWidth;
            }
        }
    }

    public static void setDimensions(int offx, int offy, int w, int h) {
        if (offx < 0) {
            offx = 0;
        }
        if (offy < 0) {
            offy = 0;
        }
        if (w > bufferWidth) {
            w = bufferWidth;
        }
        if (h > bufferHeight) {
            h = bufferHeight;
        }
        areaOffsetX = offx;
        areaOffsetY = offy;
        areaWidth = w;
        areaHeight = h;
    }

    public static void drawQuadOutlineOverlay(int i, int i_62_, int i_63_, int i_64_,
            int i_65_, int i_66_) {
        drawHorizontalOverlayLine(i, i_62_, i_63_, i_65_, i_66_);
        drawHorizontalOverlayLine(i, i_62_ + i_64_ - 1, i_63_, i_65_, i_66_);
        if (i_64_ >= 3) {
            drawVerticalOverlayLine(i, i_62_ + 1, i_64_ - 2, i_65_, i_66_);
            drawVerticalOverlayLine(i + i_63_ - 1, i_62_ + 1, i_64_ - 2, i_65_, i_66_);
        }
    }

    public static void setDimensions(int[] is) {
        areaOffsetX = is[0];
        areaOffsetY = is[1];
        areaWidth = is[2];
        areaHeight = is[3];
    }

    public static void reset() {
        int i = 0;
        int i_67_ = bufferWidth * bufferHeight - 7;
        while (i < i_67_) {
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
            buffer[i++] = 0;
        }
        i_67_ += 7;
        while (i < i_67_) {
            buffer[i++] = 0;
        }
    }

    public static void getDimensions(int[] is) {
        is[0] = areaOffsetX;
        is[1] = areaOffsetY;
        is[2] = areaWidth;
        is[3] = areaHeight;
    }

    public static void drawQuadOverlay(int i, int i_68_, int i_69_, int i_70_,
            int i_71_, int i_72_) {
        if (i < areaOffsetX) {
            i_69_ -= areaOffsetX - i;
            i = areaOffsetX;
        }
        if (i_68_ < areaOffsetY) {
            i_70_ -= areaOffsetY - i_68_;
            i_68_ = areaOffsetY;
        }
        if (i + i_69_ > areaWidth) {
            i_69_ = areaWidth - i;
        }
        if (i_68_ + i_70_ > areaHeight) {
            i_70_ = areaHeight - i_68_;
        }
        int i_73_ = 256 - i_72_;
        int i_74_ = (i_71_ >> 16 & 0xff) * i_72_;
        int i_75_ = (i_71_ >> 8 & 0xff) * i_72_;
        int i_76_ = (i_71_ & 0xff) * i_72_;
        int i_77_ = bufferWidth - i_69_;
        int i_78_ = i + i_68_ * bufferWidth;
        for (int i_79_ = 0; i_79_ < i_70_; i_79_++) {
            for (int i_80_ = -i_69_; i_80_ < 0; i_80_++) {
                int i_81_ = (buffer[i_78_] >> 16 & 0xff) * i_73_;
                int i_82_ = (buffer[i_78_] >> 8 & 0xff) * i_73_;
                int i_83_ = (buffer[i_78_] & 0xff) * i_73_;
                int i_84_ = ((i_74_ + i_81_ >> 8 << 16) + (i_75_ + i_82_ >> 8 << 8)
                        + (i_76_ + i_83_ >> 8));
                buffer[i_78_++] = i_84_;
            }
            i_78_ += i_77_;
        }
    }

    static {
        areaHeight = 0;
        areaOffsetX = 0;
    }
}
