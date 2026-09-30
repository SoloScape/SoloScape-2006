package jagex.io;

import jagex.graphics.BitmapFont;
import jagex.graphics.DrawingArea;
import jagex.graphics.AbstractImage;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.utils.Cache;
import unpackaged.*;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;

/* Class39_Sub6_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class FrameBuffer extends Buffer {

    public IsaacPrng isaacCipher;
    public static JString aClass3_2142 = Class39_Sub5_Sub9.createJstring("glow1:");
    public static JString aClass3_2143 = Class39_Sub5_Sub9.createJstring("bevor Sie den Vorgang wiederholen)3");
    public static JString aClass3_2144;
    public int bitOffset;
    public static FrameBuffer outgoingGameBuffer;
    public static JString aClass3_2147;
    public static BitmapFont aClass39_Sub5_Sub10_Sub1_2148;
    public static JString aClass3_2149 = Class39_Sub5_Sub9.createJstring("redstone3");
    public static int[] anIntArray2150 = new int[50];
    public static JString aClass3_2151;
    public static JString aClass3_2152;
    public static boolean aBoolean2153;
    public static JString b12_full;
    public static AbstractImage aClass57_2155;
    public static int anInt2156;
    public static int anInt2157;
    public static Cache aClass7_2158;
    public static int anInt2159;

    public int getRemainingBits(int offset) {
        return offset * 8 - bitOffset;
    }

    public void initBitAccess() {
        bitOffset = offset * 8;
    }

    public void initIsaacCipher(int[] seeds) {
        isaacCipher = new IsaacPrng(seeds);
    }

    public void finishBitAccess() {
        offset = (bitOffset + 7) / 8;
    }

    public FrameBuffer(int i) {
        super(i);
    }

    public static void method839(boolean bool) {
        aClass39_Sub5_Sub10_Sub1_2148 = null;
        b12_full = null;
        aClass57_2155 = null;
        anIntArray2150 = null;
        aClass3_2152 = null;
        outgoingGameBuffer = null;
        aClass3_2142 = null;
        aClass7_2158 = null;
        aClass3_2147 = null;
        aClass3_2143 = null;
        aClass3_2151 = null;
        aClass3_2144 = null;
        aClass3_2149 = null;
    }

    public static void method840() {
        Class2.aClass7_54.method134(27392);
    }

    public static DirectColorSprite method841(int i, int i_1_, boolean bool, int i_2_, int i_3_, int i_4_) {
        long l = ((long) i - -((long) i_2_ << 16) + ((long) i_4_ << 38)
                + ((long) i_1_ << 40));
        if (!bool) {
            DirectColorSprite class39_sub5_sub10_sub3 = ((DirectColorSprite) ItemDefinition.aClass7_1663.get(l));
            if (class39_sub5_sub10_sub3 != null) {
                return class39_sub5_sub10_sub3;
            }
        }
        ItemDefinition class39_sub5_sub1 = Class26.getItemDefinition(i);
        if (i_2_ > 1 && class39_sub5_sub1.anIntArray1670 != null) {
            int i_5_ = -1;
            for (int i_6_ = 0; i_6_ < 10; i_6_++) {
                if (class39_sub5_sub1.anIntArray1646[i_6_] <= i_2_
                        && class39_sub5_sub1.anIntArray1646[i_6_] != 0) {
                    i_5_ = class39_sub5_sub1.anIntArray1670[i_6_];
                }
            }
            if (i_5_ != -1) {
                class39_sub5_sub1 = Class26.getItemDefinition(i_5_);
            }
        }
        Model class39_sub5_sub4_sub6 = class39_sub5_sub1.method468(1, true, (byte) -114);
        if (class39_sub5_sub4_sub6 == null) {
            return null;
        }
        DirectColorSprite class39_sub5_sub10_sub3 = null;
        if (class39_sub5_sub1.anInt1644 != -1) {
            class39_sub5_sub10_sub3 = method841(class39_sub5_sub1.anInt1651, 0, true, 10, 84, 1);
            if (class39_sub5_sub10_sub3 == null) {
                return null;
            }
        }
        int[] is = DrawingArea.buffer;
        int i_7_ = DrawingArea.bufferWidth;
        int i_8_ = DrawingArea.bufferHeight;
        int i_9_ = DrawingArea.areaWidth;
        int i_10_ = DrawingArea.areaOffsetX;
        int i_11_ = DrawingArea.areaOffsetY;
        int i_12_ = DrawingArea.areaHeight;
        int[] is_13_ = Class39_Sub5_Sub10_Sub2.method661();
        int i_14_ = Class39_Sub5_Sub10_Sub2.anInt2461;
        int i_15_ = Class39_Sub5_Sub10_Sub2.anInt2463;
        DirectColorSprite class39_sub5_sub10_sub3_16_ = new DirectColorSprite(36, 32);
        DrawingArea.setBuffer(class39_sub5_sub10_sub3_16_.anIntArray2476, 36, 32);
        Class39_Sub5_Sub16.anIntArray1977 = Class39_Sub5_Sub10_Sub2.method657(Class39_Sub5_Sub16.anIntArray1977);
        DrawingArea.drawQuad(0, 0, 36, 32, 0);
        Class39_Sub5_Sub10_Sub2.aBoolean2471 = false;
        Class39_Sub5_Sub10_Sub2.method659(16, 16);
        int i_17_ = class39_sub5_sub1.anInt1649;
        if (!bool) {
            if (i_4_ == 2) {
                i_17_ *= 1.04;
            }
        } else {
            i_17_ *= 1.5;
        }
        int i_18_ = ((Class39_Sub5_Sub10_Sub2.cosineTable[class39_sub5_sub1.anInt1669]) * i_17_
                >> 16);
        int i_19_ = ((Class39_Sub5_Sub10_Sub2.sineTable[class39_sub5_sub1.anInt1669]) * i_17_
                >> 16);
        class39_sub5_sub4_sub6.method542();
        class39_sub5_sub4_sub6.method549(0, class39_sub5_sub1.anInt1676,
                class39_sub5_sub1.anInt1656,
                class39_sub5_sub1.anInt1669,
                class39_sub5_sub1.anInt1674,
                (i_19_
                + (class39_sub5_sub4_sub6.anInt1726
                / 2)
                + class39_sub5_sub1.anInt1654),
                class39_sub5_sub1.anInt1654 + i_18_);
        if (i_4_ >= 1) {
            class39_sub5_sub10_sub3_16_.method683(1);
        }
        if (i_4_ >= 2) {
            class39_sub5_sub10_sub3_16_.method683(16777215);
        }
        if (i_1_ != 0) {
            class39_sub5_sub10_sub3_16_.method672(i_1_);
        }
        DrawingArea.setBuffer(class39_sub5_sub10_sub3_16_.anIntArray2476, 36, 32);
        if (class39_sub5_sub1.anInt1644 != -1) {
            class39_sub5_sub10_sub3.method670(0, 0);
        }
        if (!bool && (class39_sub5_sub1.anInt1662 == 1 || i_2_ != 1)
                && i_2_ != -1) {
            aClass39_Sub5_Sub10_Sub1_2148.method647(Class44.method911(i_2_, (byte) 93), 1, 10, 1);
            aClass39_Sub5_Sub10_Sub1_2148.method647(Class44.method911(i_2_, (byte) 75), 0, 9, 16776960);
        }
        if (!bool) {
            ItemDefinition.aClass7_1663.put(class39_sub5_sub10_sub3_16_, l, (byte) 120);
        }
        DrawingArea.setBuffer(is, i_7_, i_8_);
        DrawingArea.setDimensions(i_10_, i_11_, i_9_, i_12_);
        Class39_Sub5_Sub10_Sub2.method657(is_13_);
        Class39_Sub5_Sub10_Sub2.anInt2463 = i_15_;
        Class39_Sub5_Sub10_Sub2.anInt2461 = i_14_;
        Class39_Sub5_Sub10_Sub2.method654();
        Class39_Sub5_Sub10_Sub2.aBoolean2471 = true;
        return class39_sub5_sub10_sub3_16_;
    }

    public void putFrame(int id) {
        payload[offset++] = (byte) (id + isaacCipher.getNextValue());
    }

    public int getBits(int amount) {
        int byteOffset = bitOffset >> 3;
        int i_23_ = 8 - (bitOffset & 0x7);
        bitOffset += amount;
        int i_24_ = 0;
        for (/**/; amount > i_23_; i_23_ = 8) {
            i_24_ += (Class39_Sub5_Sub9.bitMasks[i_23_]
                    & payload[byteOffset++]) << -i_23_ + amount;
            amount -= i_23_;
        }
        if (i_23_ != amount) {
            i_24_ += (payload[byteOffset] >> -amount + i_23_
                    & Class39_Sub5_Sub9.bitMasks[amount]);
        } else {
            i_24_ += (payload[byteOffset]
                    & Class39_Sub5_Sub9.bitMasks[i_23_]);
        }
        return i_24_;
    }

    public int getFrame() {
        return ((payload[offset++] - isaacCipher.getNextValue()) & 0xff);
    }

    static {
        aClass3_2147 = Class39_Sub5_Sub9.createJstring("@lre@");
        aClass3_2152 = Class39_Sub5_Sub9.createJstring("Spiel)2Engine wird gestartet)3)3)3");
        b12_full = Class39_Sub5_Sub9.createJstring("b12_full");
        anInt2157 = 0;
        aClass3_2151 = Class39_Sub5_Sub9.createJstring("null");
        aClass3_2144 = aClass3_2142;
        outgoingGameBuffer = new FrameBuffer(5000);
        aClass7_2158 = new Cache(64);
        anInt2159 = 0;
    }
}
