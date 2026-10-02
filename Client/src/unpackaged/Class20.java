package unpackaged;

import jagex.graphics.BitmapFont;
import jagex.graphics.DrawingArea;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.world.actors.Projectile;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.utils.Cache;

/* Class20 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class20 {

    public static JString aClass3_387;
    public static JString aClass3_388;
    public static JString aClass3_389 = Class39_Sub5_Sub9.createJstring("(U2");
    public static Resource aClass56_390;
    public static int[][] anIntArrayArray391;
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array392;
    public static int anInt393;
    public static JString aClass3_394;
    public static Class13 aClass13_395;
    public static Deque[][][] groundItems;
    public static int anInt397;

    static boolean isSelectedSpellWidget(Widget widget) {
        if (widget == null || !IsaacPrng.aBoolean1100 || Class31.anInt570 != -1
                || Class41.anInt775 != widget.anInt2084) {
            return false;
        }
        int group = widget.anInt2084 >>> 16;
        return group == 192 || group == 193;
    }

    static boolean isGuideIconDialogueWidget(Widget widget) {
        return widget != null
                && widget.anInt2084 == ((244 << 16) | 0)
                && widget.anInt2009 == 2
                && widget.anInt2026 == 0xFFFF;
    }

    public static void method246(long l, byte i) {
        if (l != 0L) {
            for (int i_0_ = 0; i_0_ < Class4.anInt62; i_0_++) {
                if (ClientApplet.aLongArray2[i_0_] == l) {
                    Class39_Sub14.aBoolean1520 = true;
                    Class4.anInt62--;
                    for (int i_1_ = i_0_; Class4.anInt62 > i_1_; i_1_++) {
                        Projectile.aClass3Array2188[i_1_] = (Projectile.aClass3Array2188[i_1_ + 1]);
                        Player.anIntArray2533[i_1_] = (Player.anIntArray2533[i_1_ + 1]);
                        ClientApplet.aLongArray2[i_1_] = ClientApplet.aLongArray2[i_1_ + 1];
                    }
                    FrameBuffer.outgoingGameBuffer.putFrame(159);
                    FrameBuffer.outgoingGameBuffer.putQword(l);
                    break;
                }
            }
            int i_2_ = -61 / ((-84 - i) / 38);
        }
    }

    public static void method247(byte i) {
        aClass13_395 = null;
        aClass3_387 = null;
        aClass39_Sub5_Sub10_Sub3Array392 = null;
        aClass56_390 = null;
        aClass3_388 = null;
        aClass3_394 = null;
        aClass3_389 = null;
        anIntArrayArray391 = null;
        groundItems = null;
    }

    public static boolean drawWidgets(Widget[] widgets, int i, int i_3_, int i_4_,
            int i_5_, int i_6_, int i_7_, int i_8_, int i_9_, int i_10_) {
        DrawingArea.setDimensions(i_10_, i_5_, i_7_, i_9_);
        boolean bool = true;
        for (int i_11_ = 0; i_11_ < widgets.length; i_11_++) {
            Widget widget = widgets[i_11_];
            if (widget != null && (widget.anInt2084 >>> 16) >= 500
                    && (widget.anInt2084 >>> 16) <= 504 && widget.aBoolean2055) continue;
            if (widget != null
                    && widget.anInt2050 == i_4_) {
                if (widget.anInt2078 > 0) {
                    CacheIO.method123(widget, 114);
                }
                int x = -i_6_ + i_10_ + widget.anInt2091;
                int y = widget.anInt2021 + (i_5_ - i_3_);
                int fillAlpha = widget.anInt2030;
                if (NpcDefinition.aClass39_Sub5_Sub17_1864
                        == widget) {
                    Widget class39_sub5_sub17_15_ = Class44.method914(widget, (byte) -126);
                    if (class39_sub5_sub17_15_ == null) {
                        NpcDefinition.aClass39_Sub5_Sub17_1864 = null;
                    } else {
                        int[] is = Class65.method1094(-6622,
                                class39_sub5_sub17_15_);
                        int[] is_16_ = Class65.method1094(i_8_ ^ ~0x3fae,
                                widget);
                        if (!widget.aBoolean2108) {
                            fillAlpha = 128;
                        }
                        int i_17_ = Class33.anInt599 - FileLoader.anInt1303;
                        int i_18_ = -ItemDefinition.anInt1684 + IsaacPrng.anInt1091;
                        if (i_18_ <= widget.anInt2056
                                && -widget.anInt2056 <= i_18_
                                && i_17_ <= widget.anInt2056
                                && i_17_ >= -widget.anInt2056
                                && !Class37.aBoolean654) {
                            i_18_ = 0;
                            i_17_ = 0;
                        } else if ((widget.anInt2008
                                < ClientApplet.anInt8)
                                || Class37.aBoolean654) {
                            Class37.aBoolean654 = true;
                        } else {
                            i_17_ = 0;
                            i_18_ = 0;
                        }
                        int i_19_ = is_16_[1] - is[1] + i_17_;
                        if (i_19_ < 0) {
                            i_19_ = 0;
                        }
                        int i_20_ = i_18_ + is_16_[0] - is[0];
                        if (i_20_ < 0) {
                            i_20_ = 0;
                        }
                        if (widget.quadWidth + i_20_
                                > class39_sub5_sub17_15_.quadWidth) {
                            i_20_ = (class39_sub5_sub17_15_.quadWidth
                                    - widget.quadWidth);
                        }
                        if (class39_sub5_sub17_15_.quadHeight
                                < i_19_ + widget.quadHeight) {
                            i_19_ = (class39_sub5_sub17_15_.quadHeight
                                    - widget.quadHeight);
                        }
                        x = i_20_ + is[0];
                        y = i_19_ + is[1];
                    }
                }
                if ((!widget.aBoolean2013
                        || (x <= DrawingArea.areaWidth
                        && y <= DrawingArea.areaHeight
                        && (DrawingArea.areaOffsetX
                        <= x + widget.quadWidth)
                        && (DrawingArea.areaOffsetY
                        <= y + widget.quadHeight)))
                        && (!widget.aBoolean2013
                        || !widget.method754(125, HashTable.aBoolean361))) {
                    if (widget.type == 0) {
                        if (widget.runtimeSprite != null) widget.runtimeSprite.method670(x, y);
                        if (!widget.aBoolean2013
                                && widget.method754(127, HashTable.aBoolean361)
                                && !ItemDefinition.method473(i, -1, i_11_)) {
                            continue;
                        }
                        if (!widget.aBoolean2013) {
                            if (widget.anInt1994
                                    > (-widget.quadHeight
                                    + widget.anInt2095)) {
                                widget.anInt1994 = (-widget.quadHeight
                                        + widget.anInt2095);
                            }
                            if (widget.anInt1994 < 0) {
                                widget.anInt1994 = 0;
                            }
                        }
                        bool &= drawWidgets(widgets, i,
                                widget.anInt1994,
                                widget.anInt2084, y,
                                widget.anInt2064,
                                x + widget.quadWidth,
                                i_8_,
                                widget.quadHeight + y,
                                x);
                        if (widget.aClass39_Sub5_Sub17Array2025
                                != null) {
                            bool &= drawWidgets((widget.aClass39_Sub5_Sub17Array2025),
                                    i, widget.anInt1994,
                                    widget.anInt2084,
                                    y,
                                    widget.anInt2064,
                                    (widget.quadWidth
                                    + x),
                                    9843,
                                    (y
                                    + widget.quadHeight),
                                    x);
                        }
                        DrawingArea.setDimensions(i_10_, i_5_, i_7_, i_9_);
                        if ((widget.quadHeight
                                < widget.anInt2095)
                                && !widget.aBoolean2013) {
                            Class4.method102((widget.quadWidth
                                    + x),
                                    widget.anInt1994,
                                    widget.anInt2095,
                                    y, 18734,
                                    widget.quadHeight);
                        }
                    }
                    if (widget.type != 1) {
                        if (widget.type == 2) {
                            int i_21_ = 0;
                            for (int i_22_ = 0;
                                    i_22_ < widget.quadHeight;
                                    i_22_++) {
                                for (int i_23_ = 0;
                                        widget.quadWidth > i_23_;
                                        i_23_++) {
                                    int i_24_ = x + (widget.anInt2000
                                            + 32) * i_23_;
                                    int i_25_ = ((widget.anInt2010
                                            + 32) * i_22_
                                            + y);
                                    if (i_21_ < 20) {
                                        i_25_ += (widget.anIntArray2037[i_21_]);
                                        i_24_ += (widget.anIntArray2028[i_21_]);
                                    }
                                    if ((widget.anIntArray2087[i_21_])
                                            > 0) {
                                        int i_26_ = ((widget.anIntArray2087[i_21_])
                                                - 1);
                                        boolean bool_27_ = false;
                                        boolean bool_28_ = false;
                                        if (((DrawingArea.areaOffsetX - 32
                                                < i_24_)
                                                && (DrawingArea.areaWidth
                                                > i_24_)
                                                && i_25_ > (DrawingArea.areaOffsetY) - 32
                                                && (DrawingArea.areaHeight
                                                > i_25_))
                                                || (Class30.anInt534 != 0
                                                && i_21_ == (ArchiveRequest.anInt1403))) {
                                            DirectColorSprite class39_sub5_sub10_sub3;
                                            if (Class13.anInt208 != 1
                                                    || i_21_ != Class23.anInt428
                                                    || ((widget.anInt2084)
                                                    != (Class39_Sub10.anInt1430))) {
                                                class39_sub5_sub10_sub3 = (FrameBuffer.method841(i_26_, 3153952, false,
                                                        (widget.anIntArray2073[i_21_]),
                                                        i_8_ ^ 0x264d, 1));
                                            } else {
                                                class39_sub5_sub10_sub3 = (FrameBuffer.method841(i_26_, 0, false,
                                                        (widget.anIntArray2073[i_21_]),
                                                        115, 2));
                                            }
                                            if (class39_sub5_sub10_sub3
                                                    == null) {
                                                bool = false;
                                            } else if (Class30.anInt534 != 0
                                                    && (ArchiveRequest.anInt1403
                                                    == i_21_)
                                                    && (ArchiveWorker.anInt1203
                                                    == (widget.anInt2084))) {
                                                int i_29_ = (Class33.anInt599
                                                        - (OndemandRequest.anInt1720));
                                                if (i_29_ < 5 && i_29_ > -5) {
                                                    i_29_ = 0;
                                                }
                                                int i_30_ = (IsaacPrng.anInt1091
                                                        - (ClientScript.anInt1702));
                                                if (i_30_ < 5 && i_30_ > -5) {
                                                    i_30_ = 0;
                                                }
                                                if ((Widget.anInt2031)
                                                        < 5) {
                                                    i_30_ = 0;
                                                    i_29_ = 0;
                                                }
                                                class39_sub5_sub10_sub3.method676(i_30_ + i_24_,
                                                        i_29_ + i_25_, 128);
                                                if (i_4_ != -1) {
                                                    Widget class39_sub5_sub17_31_ = (widgets[i_4_ & 0xffff]);
                                                    if (((DrawingArea.areaOffsetY)
                                                            > i_29_ + i_25_)
                                                            && (class39_sub5_sub17_31_.anInt1994) > 0) {
                                                        int i_32_ = ((-i_29_
                                                                + ((DrawingArea.areaOffsetY)
                                                                - i_25_))
                                                                * (Class45.anInt856)
                                                                / 3);
                                                        if (i_32_
                                                                > (Class45.anInt856
                                                                * 10)) {
                                                            i_32_ = ((Class45.anInt856)
                                                                    * 10);
                                                        }
                                                        if ((class39_sub5_sub17_31_.anInt1994)
                                                                < i_32_) {
                                                            i_32_ = (class39_sub5_sub17_31_.anInt1994);
                                                        }
                                                        OndemandRequest.anInt1720 += i_32_;
                                                        class39_sub5_sub17_31_.anInt1994 -= i_32_;
                                                    }
                                                    if (((DrawingArea.areaHeight)
                                                            < i_25_ + i_29_ + 32)
                                                            && ((class39_sub5_sub17_31_.anInt1994)
                                                            < (-(class39_sub5_sub17_31_.quadHeight)
                                                            + (class39_sub5_sub17_31_.anInt2095)))) {
                                                        int i_33_ = ((-(DrawingArea.areaHeight)
                                                                + (i_29_
                                                                + i_25_)
                                                                + 32)
                                                                * (Class45.anInt856)
                                                                / 3);
                                                        if (i_33_
                                                                > (Class45.anInt856
                                                                * 10)) {
                                                            i_33_ = ((Class45.anInt856)
                                                                    * 10);
                                                        }
                                                        if ((-(class39_sub5_sub17_31_.anInt1994)
                                                                + (-(class39_sub5_sub17_31_.quadHeight)
                                                                + (class39_sub5_sub17_31_.anInt2095)))
                                                                < i_33_) {
                                                            i_33_ = (-(class39_sub5_sub17_31_.quadHeight)
                                                                    + (class39_sub5_sub17_31_.anInt2095)
                                                                    - (class39_sub5_sub17_31_.anInt1994));
                                                        }
                                                        OndemandRequest.anInt1720 -= i_33_;
                                                        class39_sub5_sub17_31_.anInt1994 += i_33_;
                                                    }
                                                }
                                            } else if (Class25.anInt459 == 0
                                                    || (i_21_
                                                    != (Class39_Sub5_Sub5.anInt1739))
                                                    || (Class65.anInt1137
                                                    != (widget.anInt2084))) {
                                                class39_sub5_sub10_sub3.method670(i_24_, i_25_);
                                            } else {
                                                class39_sub5_sub10_sub3.method676(i_24_, i_25_, 128);
                                            }
                                        }
                                    } else if ((widget.anIntArray2053) != null
                                            && i_21_ < 20) {
                                        DirectColorSprite class39_sub5_sub10_sub3 = (widget.method759(i_21_, i_8_ ^ ~0x2673));
                                        if (class39_sub5_sub10_sub3 == null) {
                                            if (Class39_Sub5_Sub12.aBoolean1856) {
                                                bool = false;
                                            }
                                        } else {
                                            class39_sub5_sub10_sub3.method670(i_24_, i_25_);
                                        }
                                    }
                                    i_21_++;
                                }
                            }
                        } else if (widget.type == 3) {
                            int color;
                            if (Huffmans.parseClientScript(widget)) {
                                color = widget.inactiveQuadColor;
                                if (ItemDefinition.method473(i, i_8_ - 9844, i_11_) && widget.anInt2086 != 0) {
                                    color = widget.anInt2086;
                                }
                            } else {
                                color = widget.activeQuadColor;
                                if (ItemDefinition.method473(i, -1, i_11_) && widget.anInt2041 != 0) {
                                    color = widget.anInt2041;
                                }
                            }
                            if (fillAlpha == 0) {
                                if (!widget.drawSolidQuad) {
                                    DrawingArea.drawQuadOutline(x, y, widget.quadWidth, widget.quadHeight, color);
                                } else {
                                    DrawingArea.drawQuad(x, y, widget.quadWidth, widget.quadHeight, color);
                                }
                            } else if (widget.drawSolidQuad) {
                                DrawingArea.drawQuadOverlay(x, y, widget.quadWidth, widget.quadHeight, color, 256 - (fillAlpha & 0xff));
                            } else {
                                DrawingArea.drawQuadOutlineOverlay(x, y, widget.quadWidth, widget.quadHeight, color, -(fillAlpha & 0xff) + 256);
                            }
                        } else if (widget.type == 4) {
                            if (widget.runtimeSprite != null) widget.runtimeSprite.method670(x, y);
                            BitmapFont font = widget.getFont();
                            if (font == null) {
                                if (Class39_Sub5_Sub12.aBoolean1856) {
                                    bool = false;
                                }
                            } else {
                                JString class3 = widget.aClass3_2029;
                                int i_35_;
                                if (!Huffmans.parseClientScript(widget)) {
                                    i_35_ = widget.activeQuadColor;
                                    if (ItemDefinition.method473(i, -1,
                                            i_11_)
                                            && widget.anInt2041 != 0) {
                                        i_35_ = widget.anInt2041;
                                    }
                                } else {
                                    i_35_ = widget.inactiveQuadColor;
                                    if (ItemDefinition.method473(i, -1,
                                            i_11_)
                                            && widget.anInt2086 != 0) {
                                        i_35_ = widget.anInt2086;
                                    }
                                    if (widget.aClass3_2048.getLength()
                                            > 0) {
                                        class3 = widget.aClass3_2048;
                                    }
                                }
                                if (widget.aBoolean2013
                                        && widget.anInt1997 != -1) {
                                    ItemDefinition class39_sub5_sub1 = Class26.getItemDefinition((widget.anInt1997));
                                    class3 = class39_sub5_sub1.aClass3_1661;
                                    if (class3 == null) {
                                        class3 = Class36.aClass3_633;
                                    }
                                    if ((class39_sub5_sub1.anInt1662 == 1
                                            || widget.anInt2096 != 1)
                                            && widget.anInt2096 != -1) {
                                        class3 = (Class39_Sub5_Sub11.method708((new JString[]{class3, Class15.aClass3_279,
                                                    (Class62_Sub2.method1083((widget.anInt2096),
                                                    (byte) -113))})));
                                    }
                                }
                                if ((widget.anInt2084
                                        == Class39_Sub10.anInt1420)
                                        && (JString.anInt1231
                                        == widget.anInt2102)) {
                                    class3 = Class39_Sub5_Sub4_Sub2.aClass3_2230;
                                    i_35_ = widget.activeQuadColor;
                                }
                                if (DrawingArea.bufferWidth == 479) {
                                    if (i_35_ == 16776960) {
                                        i_35_ = 255;
                                    }
                                    if (i_35_ == 49152) {
                                        i_35_ = 16777215;
                                    }
                                }
                                class3 = Class50.method972(widget,
                                        0, class3);
                                font.method625(class3, x, y,
                                        widget.quadWidth,
                                        widget.quadHeight, i_35_,
                                        widget.aBoolean2059,
                                        widget.anInt2032,
                                        widget.anInt1996,
                                        widget.anInt2036);
                            }
                        } else if (widget.type == 5) {
                            if (!widget.aBoolean2013) {
                                DirectColorSprite class39_sub5_sub10_sub3 = (widget.method774(i_8_ - 9844,
                                        Huffmans.parseClientScript(widget)));
                                if (class39_sub5_sub10_sub3 == null) {
                                    if (Class39_Sub5_Sub12.aBoolean1856) {
                                        bool = false;
                                    }
                                } else {
                                    if (isSelectedSpellWidget(widget)) {
                                        class39_sub5_sub10_sub3.drawOutlined(x, y, 0xFFFFFF);
                                    } else {
                                        class39_sub5_sub10_sub3.method670(x, y);
                                    }
                                }
                            } else {
                                DirectColorSprite class39_sub5_sub10_sub3;
                                if (widget.anInt1997 != -1) {
                                    class39_sub5_sub10_sub3 = (FrameBuffer.method841(widget.anInt1997,
                                            widget.anInt2003,
                                            false,
                                            widget.anInt2096, 68,
                                            widget.anInt2022));
                                } else {
                                    class39_sub5_sub10_sub3 = widget.method774(-1,
                                            false);
                                }
                                if (class39_sub5_sub10_sub3 == null) {
                                    if (Class39_Sub5_Sub12.aBoolean1856) {
                                        bool = false;
                                    }
                                } else {
                                    int i_36_ = class39_sub5_sub10_sub3.anInt2475;
                                    int i_37_ = class39_sub5_sub10_sub3.anInt2477;
                                    if (!widget.aBoolean2014) {
                                        int i_38_ = (widget.quadWidth
                                                * 4096 / i_36_);
                                        if (widget.anInt2051 != 0) {
                                            class39_sub5_sub10_sub3.method684(x + (widget.quadWidth) / 2,
                                                    (widget.quadHeight
                                                    / 2) + y,
                                                    widget.anInt2051,
                                                    i_38_);
                                        } else if (fillAlpha != 0) {
                                            class39_sub5_sub10_sub3.method680(x, y,
                                                    widget.quadWidth,
                                                    widget.quadHeight,
                                                    256 - (fillAlpha & 0xff));
                                        } else if ((widget.quadWidth
                                                != i_36_)
                                                || (widget.quadHeight) != i_37_) {
                                            class39_sub5_sub10_sub3.method687(x, y,
                                                    widget.quadWidth,
                                                    widget.quadHeight);
                                        } else {
                                            class39_sub5_sub10_sub3.method670(x, y);
                                        }
                                    } else {
                                        int i_39_ = x;
                                        int[] is = new int[4];
                                        DrawingArea.getDimensions(is);
                                        if (i_39_ < is[0]) {
                                            i_39_ = is[0];
                                        }
                                        int i_40_ = y;
                                        if (i_40_ < is[1]) {
                                            i_40_ = is[1];
                                        }
                                        int i_41_ = (widget.quadWidth
                                                + x);
                                        int i_42_ = (widget.quadHeight
                                                + y);
                                        if (is[3] < i_42_) {
                                            i_42_ = is[3];
                                        }
                                        if (is[2] < i_41_) {
                                            i_41_ = is[2];
                                        }
                                        DrawingArea.setDimensions(i_39_,
                                                i_40_,
                                                i_41_,
                                                i_42_);
                                        int i_43_ = ((widget.quadHeight
                                                + i_37_ - 1)
                                                / i_37_);
                                        int i_44_ = ((widget.quadWidth
                                                - (-i_36_ + 1))
                                                / i_36_);
                                        for (int i_45_ = 0; i_45_ < i_44_;
                                                i_45_++) {
                                            for (int i_46_ = 0; i_43_ > i_46_;
                                                    i_46_++) {
                                                if ((widget.anInt2051)
                                                        != 0) {
                                                    class39_sub5_sub10_sub3.method684((i_45_ * i_36_ + x
                                                            + i_36_ / 2),
                                                            (y + i_37_ * i_46_
                                                            + i_37_ / 2),
                                                            (widget.anInt2051),
                                                            4096);
                                                } else if (fillAlpha != 0) {
                                                    class39_sub5_sub10_sub3.method676(x + i_45_ * i_36_,
                                                            i_37_ * i_46_ + y,
                                                            (-(fillAlpha & 0xff)
                                                            + 256));
                                                } else {
                                                    class39_sub5_sub10_sub3.method670(i_45_ * i_36_ + x,
                                                            (i_46_ * i_37_
                                                            + y));
                                                }
                                            }
                                        }
                                        DrawingArea.setDimensions(is);
                                    }
                                }
                            }
                        } else if (widget.type == 6) {
                            if (isGuideIconDialogueWidget(widget)) {
                                DirectColorSprite[] mapFunctions = Projectile.aClass39_Sub5_Sub10_Sub3Array2205;
                                if (mapFunctions != null && mapFunctions.length > 55 && mapFunctions[55] != null) {
                                    DirectColorSprite guideIcon = mapFunctions[55];
                                    int scale = 5;
                                    int iconWidth = guideIcon.anInt2475 * scale;
                                    int iconHeight = guideIcon.anInt2477 * scale;
                                    guideIcon.method687(x + (widget.quadWidth - iconWidth) / 2,
                                            y + (widget.quadHeight - iconHeight) / 2,
                                            iconWidth, iconHeight);
                                }
                                continue;
                            }
                            boolean bool_47_ = Huffmans.parseClientScript(widget);
                            int i_48_;
                            if (bool_47_) {
                                i_48_ = widget.anInt2052;
                            } else {
                                i_48_ = widget.anInt2103;
                            }
                            Object object = null;
                            Model class39_sub5_sub4_sub6;
                            if (widget.anInt2009 == 5) {
                                if (widget.anInt2026 == 0) {
                                    class39_sub5_sub4_sub6 = (ClientScript.aClass45_1705.method922(-27537, null, null, -1, -1));
                                } else {
                                    class39_sub5_sub4_sub6 = Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.method489(true);
                                }
                            } else if (i_48_ == -1) {
                                class39_sub5_sub4_sub6 = (widget.method775(i_8_ - 9861, -1, bool_47_,
                                        (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.aClass45_2516),
                                        null));
                                if (class39_sub5_sub4_sub6 == null
                                        && Class39_Sub5_Sub12.aBoolean1856) {
                                    bool = false;
                                }
                            } else {
                                Class39_Sub5_Sub11 class39_sub5_sub11 = Class62_Sub1.method1064(i_48_,
                                        (byte) 54);
                                class39_sub5_sub4_sub6 = (widget.method775(-99, widget.anInt1999,
                                        bool_47_,
                                        (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.aClass45_2516),
                                        class39_sub5_sub11));
                                if (class39_sub5_sub4_sub6 == null
                                        && Class39_Sub5_Sub12.aBoolean1856) {
                                    bool = false;
                                }
                            }
                            int i_49_ = widget.anInt2098;
                            int i_50_ = widget.anInt2011;
                            int i_51_ = widget.anInt2007;
                            int i_52_ = widget.anInt2072;
                            int i_53_ = widget.anInt2058;
                            int i_54_ = widget.anInt2074;
                            if (widget.anInt1997 != -1) {
                                ItemDefinition class39_sub5_sub1 = Class26.getItemDefinition((widget.anInt1997));
                                if (class39_sub5_sub1 != null) {
                                    class39_sub5_sub1 = (class39_sub5_sub1.method476(widget.anInt2096,
                                            (byte) 71));
                                    class39_sub5_sub4_sub6 = class39_sub5_sub1.method468(1, true, (byte) -123);
                                    i_52_ = class39_sub5_sub1.anInt1674;
                                    i_54_ = class39_sub5_sub1.anInt1649;
                                    i_49_ = class39_sub5_sub1.anInt1669;
                                    i_51_ = class39_sub5_sub1.anInt1656;
                                    i_53_ = class39_sub5_sub1.anInt1654;
                                    if (widget.quadWidth > 0) {
                                        i_54_ = (i_54_ * 32
                                                / widget.quadWidth);
                                    }
                                    i_50_ = class39_sub5_sub1.anInt1676;
                                }
                            }
                            int modelCenterX = x + widget.quadWidth / 2;
                            int modelCenterY = y + widget.quadHeight / 2;
                            if (widget.anInt2084 == ((249 << 16) | 0)
                                    && widget.anInt2026 == 1511) {
                                i_54_ = i_54_ * 4 / 5;
                                modelCenterX += 21;
                                modelCenterY += 4;
                            }
                            Class39_Sub5_Sub10_Sub2.method659(modelCenterX,
                                    modelCenterY);
                            int i_55_ = ((Class39_Sub5_Sub10_Sub2.cosineTable[i_49_]) * i_54_
                                    >> 16);
                            int i_56_ = ((Class39_Sub5_Sub10_Sub2.sineTable[i_49_]) * i_54_
                                    >> 16);
                            if (class39_sub5_sub4_sub6 != null) {
                                if (!widget.aBoolean2013) {
                                    class39_sub5_sub4_sub6.method549(0, i_50_,
                                            0, i_49_,
                                            0, i_56_,
                                            i_55_);
                                } else {
                                    class39_sub5_sub4_sub6.method542();
                                    if (widget.aBoolean2081) {
                                        class39_sub5_sub4_sub6.render(0, i_50_, i_51_, i_49_, i_52_,
                                                (class39_sub5_sub4_sub6.anInt1726
                                                / 2) + i_56_ + i_53_,
                                                i_55_ + i_53_, i_54_);
                                    } else {
                                        class39_sub5_sub4_sub6.method549(0, i_50_, i_51_, i_49_, i_52_,
                                                i_53_ + (class39_sub5_sub4_sub6.anInt1726) / 2 + i_56_,
                                                i_55_ + i_53_);
                                    }
                                }
                            }
                            Class39_Sub5_Sub10_Sub2.method654();
                        } else {
                            if (widget.type == 7) {
                                BitmapFont class39_sub5_sub10_sub1 = widget.getFont();
                                if (class39_sub5_sub10_sub1 == null) {
                                    if (Class39_Sub5_Sub12.aBoolean1856) {
                                        bool = false;
                                    }
                                    continue;
                                }
                                int i_57_ = 0;
                                for (int i_58_ = 0;
                                        i_58_ < widget.quadHeight;
                                        i_58_++) {
                                    for (int i_59_ = 0;
                                            i_59_ < widget.quadWidth;
                                            i_59_++) {
                                        if ((widget.anIntArray2087[i_57_])
                                                > 0) {
                                            ItemDefinition class39_sub5_sub1 = (Class26.getItemDefinition((widget.anIntArray2087[i_57_]) - 1));
                                            JString class3 = (class39_sub5_sub1.aClass3_1661);
                                            if (class3 == null) {
                                                class3 = Class36.aClass3_633;
                                            }
                                            if ((class39_sub5_sub1.anInt1662
                                                    == 1)
                                                    || ((widget.anIntArray2073[i_57_])
                                                    != 1)) {
                                                class3 = (Class39_Sub5_Sub11.method708((new JString[]{class3,
                                                            Class15.aClass3_279,
                                                            (Class62_Sub2.method1083((widget.anIntArray2073[i_57_]),
                                                            (byte) -115))})));
                                            }
                                            int i_60_ = ((i_59_
                                                    * (115
                                                    + (widget.anInt2000)))
                                                    + x);
                                            int i_61_ = (i_58_ * ((widget.anInt2010)
                                                    + 12)
                                                    + y);
                                            if (widget.anInt2032
                                                    != 0) {
                                                if ((widget.anInt2032)
                                                        != 1) {
                                                    class39_sub5_sub10_sub1.method627(class3,
                                                            (i_60_
                                                            + (widget.quadWidth)
                                                            - 1),
                                                            i_61_,
                                                            (widget.activeQuadColor),
                                                            (widget.aBoolean2059));
                                                } else {
                                                    class39_sub5_sub10_sub1.method636(class3,
                                                            (i_60_
                                                            + (widget.quadWidth) / 2),
                                                            i_61_,
                                                            (widget.activeQuadColor),
                                                            (widget.aBoolean2059));
                                                }
                                            } else {
                                                class39_sub5_sub10_sub1.method635(class3, i_60_, i_61_,
                                                        (widget.activeQuadColor),
                                                        (widget.aBoolean2059));
                                            }
                                        }
                                        i_57_++;
                                    }
                                }
                            }
                            if (widget.type == 8
                                    && Class53.method986(i_11_, i, 1)
                                    && (Class30.anInt548
                                    == FrameBuffer.anInt2157)) {
                                int i_62_ = 0;
                                int i_63_ = 0;
                                JString class3 = widget.aClass3_2029;
                                BitmapFont class39_sub5_sub10_sub1 = (Class39_Sub5_Sub14.p12fullFont);
                                class3 = Class50.method972(widget,
                                        0, class3);
                                while (class3.getLength() > 0) {
                                    int i_64_ = class3.method80(22938,
                                            (Class39_Sub5_Sub9.aClass3_1814));
                                    JString class3_65_;
                                    if (i_64_ != -1) {
                                        class3_65_ = class3.method59(0, -1, i_64_);
                                        class3 = class3.method85(-58, i_64_ + 2);
                                    } else {
                                        class3_65_ = class3;
                                        class3 = Class66.blankString;
                                    }
                                    int i_66_ = class39_sub5_sub10_sub1.method646(class3_65_);
                                    if (i_66_ > i_63_) {
                                        i_63_ = i_66_;
                                    }
                                    i_62_ += (class39_sub5_sub10_sub1.anInt2425
                                            + 1);
                                }
                                i_63_ += 6;
                                int i_67_ = (widget.quadWidth - 5
                                        + (x - i_63_));
                                i_62_ += 7;
                                int i_68_ = widget.quadHeight + 5 + y;
                                if (i_9_ < i_68_ + i_62_) {
                                    i_68_ = -i_62_ + i_9_;
                                }
                                if (x + 5 > i_67_) {
                                    i_67_ = x + 5;
                                }
                                if (i_7_ < i_67_ + i_63_) {
                                    i_67_ = -i_63_ + i_7_;
                                }
                                DrawingArea.drawQuad(i_67_, i_68_,
                                        i_63_, i_62_,
                                        16777120);
                                DrawingArea.drawQuadOutline(i_67_, i_68_,
                                        i_63_, i_62_, 0);
                                class3 = widget.aClass3_2029;
                                int i_69_ = (class39_sub5_sub10_sub1.anInt2425
                                        + 2 + i_68_);
                                class3 = Class50.method972(widget,
                                        0, class3);
                                while (class3.getLength() > 0) {
                                    int i_70_ = class3.method80(i_8_ ^ 0x7fe9,
                                            (Class39_Sub5_Sub9.aClass3_1814));
                                    JString class3_71_;
                                    if (i_70_ != -1) {
                                        class3_71_ = class3.method59(0, -1, i_70_);
                                        class3 = class3.method85(i_8_ ^ ~0x264a,
                                                i_70_ + 2);
                                    } else {
                                        class3_71_ = class3;
                                        class3 = Class66.blankString;
                                    }
                                    class39_sub5_sub10_sub1.method635(class3_71_, i_67_ + 3, i_69_, 0,
                                            false);
                                    i_69_ += (class39_sub5_sub10_sub1.anInt2425
                                            + 1);
                                }
                            }
                            if (widget.type == 9) {
                                if (widget.anInt2083 == 1) {
                                    DrawingArea.method612(x, y,
                                            widget.quadWidth + x,
                                            y + widget.quadHeight,
                                            widget.activeQuadColor);
                                } else {
                                    int i_72_ = (widget.quadWidth >= 0
                                            ? widget.quadWidth
                                            : -widget.quadWidth);
                                    int i_73_ = (widget.quadHeight >= 0
                                            ? widget.quadHeight
                                            : -widget.quadHeight);
                                    int i_74_ = i_72_;
                                    if (i_74_ < i_73_) {
                                        i_74_ = i_73_;
                                    }
                                    if (i_74_ != 0) {
                                        int i_75_ = ((widget.quadWidth
                                                << 16)
                                                / i_74_);
                                        int i_76_ = ((widget.quadHeight
                                                << 16)
                                                / i_74_);
                                        if (i_76_ <= i_75_) {
                                            i_75_ = -i_75_;
                                        } else {
                                            i_76_ = -i_76_;
                                        }
                                        int i_77_ = ((widget.anInt2083
                                                * i_76_)
                                                >> 17);
                                        int i_78_ = (i_76_ * (widget.anInt2083) + 1
                                                >> 17);
                                        int i_79_ = ((widget.anInt2083
                                                * i_75_)
                                                >> 17);
                                        int i_80_ = ((widget.anInt2083
                                                * i_75_) + 1
                                                >> 17);
                                        int i_81_ = i_77_ + x;
                                        int i_82_ = (widget.quadWidth
                                                + (x - i_78_));
                                        int i_83_ = (y
                                                + (widget.quadHeight
                                                - i_80_));
                                        int i_84_ = -i_80_ + y;
                                        int i_85_ = -i_78_ + x;
                                        int i_86_ = (x
                                                + (widget.quadWidth
                                                + i_77_));
                                        int i_87_ = (i_79_ + y
                                                + widget.quadHeight);
                                        Class39_Sub5_Sub10_Sub2.method666(i_81_, i_85_, i_82_);
                                        int i_88_ = i_79_ + y;
                                        Class39_Sub5_Sub10_Sub2.method662(i_88_, i_84_, i_83_, i_81_, i_85_,
                                                i_82_,
                                                widget.activeQuadColor);
                                        Class39_Sub5_Sub10_Sub2.method666(i_81_, i_82_, i_86_);
                                        Class39_Sub5_Sub10_Sub2.method662(i_88_, i_83_, i_87_, i_81_, i_82_,
                                                i_86_,
                                                widget.activeQuadColor);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (i_8_ != 9843) {
            return true;
        }
        return bool;
    }

    static {
        aClass3_388 = Class39_Sub5_Sub9.createJstring("Please try again)3");
        aClass3_394 = Class39_Sub5_Sub9.createJstring("@or1@");
        anInt393 = 0;
        aClass3_387 = aClass3_388;
        groundItems = new Deque[4][104][104];
        anInt397 = 0;
    }
}
