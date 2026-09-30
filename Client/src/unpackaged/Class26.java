package unpackaged;

/* Class26 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.graphics.DrawingArea;
import jagex.world.actors.GroundItem;
import jagex.graphics.AbstractImage;
import jagex.world.actors.StillGraphic;
import jagex.utils.Node;
import jagex.utils.JString;
import jagex.utils.Queue;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.awt.FontMetrics;

public class Class26 {

    public static int anInt473;
    public static JString aClass3_474 = Class39_Sub5_Sub9.createJstring("oder ung-Ultiges Passwort)3");
    public int anInt475;
    public int anInt476;
    public int anInt477;
    public static int anInt478;
    public int anInt479;
    public int anInt480;
    public int anInt481;
    public static int anInt482;
    public static AbstractImage aClass57_483;
    public static int anInt484;
    public static int anInt485;
    public static JString aClass3_486;
    public int anInt487;
    public int anInt488;
    public static JString aClass3_489;
    public static JString aClass3_490;
    public static FontMetrics aFontMetrics491;
    public int anInt492;
    public int anInt493;
    public int anInt494;
    public int anInt495;
    public static int[] anIntArray496;
    public int anInt497;
    public int anInt498;
    public int anInt499;
    public int anInt500;
    public int anInt501;
    public int anInt502;
    public static int anInt503;

    public static void method289(int i) {
        aClass3_486 = null;
        anIntArray496 = null;
        aClass3_489 = null;
        aClass3_490 = null;
        aClass57_483 = null;
        aClass3_474 = null;
        aFontMetrics491 = null;
    }

    public static boolean method290(int i, int i_0_) {
        if (i < 32) {
            return false;
        }
        if (i == 127) {
            return false;
        }
        if (i >= 129 && i <= 159) {
            return false;
        }
        return true;
    }

    public static ItemDefinition getItemDefinition(int id) {
        ItemDefinition definition = ((ItemDefinition) Class53.itemDefinitionCache.get((long) id));
        if (definition != null) {
            return definition;
        }
        byte[] src = Class31.itemFileLoader.lookupFile(10, id);
        definition = new ItemDefinition();
        definition.id = id;
        if (src != null) {
            definition.decode(0, new Buffer(src));
        }
        definition.method475((byte) 111);
        if (definition.anInt1644 != -1) {
            definition.method467(1,
                    getItemDefinition(definition.anInt1651),
                    getItemDefinition((definition.anInt1644)));
        }
        if (!Class47.aBoolean894 && definition.aBoolean1653) {
            definition.aClass3Array1657 = null;
            definition.aClass3Array1672 = null;
            definition.anInt1650 = 0;
            definition.aClass3_1661 = Class39_Sub5_Sub4_Sub4.aClass3_2289;
        }
        Class53.itemDefinitionCache.put(definition, (long) id,
                (byte) 102);
        return definition;
    }

    public static void method292(boolean bool, boolean bool_2_, int i,
            int i_3_, byte[] is) {
        if (Class55.aClass62_1251 != null) {
            if (GroundItem.anInt2239 >= 0) {
                i -= 20;
                if (i < 1) {
                    i = 1;
                }
                ArchiveRequest.anInt1415 = i;
                if (GroundItem.anInt2239 != 0) {
                    int i_4_ = (Class39_Sub5_Sub4_Sub4.method512(929, GroundItem.anInt2239));
                    i_4_ -= Class65.anInt1141;
                    Queue.anInt987 = (i_4_ + i - 1 + 3600) / i;
                } else {
                    Queue.anInt987 = 0;
                }
                Class39_Sub5_Sub6.aByteArray1768 = is;
                JKeyListener.aBoolean628 = bool;
                Class39_Sub5_Sub18.anInt2120 = i_3_;
            } else if (ArchiveRequest.anInt1415 != 0) {
                Class39_Sub5_Sub6.aByteArray1768 = is;
                Class39_Sub5_Sub18.anInt2120 = i_3_;
                JKeyListener.aBoolean628 = bool;
            } else {
                ArchiveRequest.method857(i_3_, bool, is, false);
            }
        }
    }

    public static boolean method293(int i, int i_5_, int i_6_, int i_7_, int i_8_, int i_9_, int i_10_,
            boolean bool, int i_11_, int i_12_, int i_13_, int i_14_) {
        for (int i_15_ = 0; i_15_ < 104; i_15_++) {
            for (int i_16_ = 0; i_16_ < 104; i_16_++) {
                NameTable.anIntArrayArray186[i_15_][i_16_] = 0;
                Class33.anIntArrayArray602[i_15_][i_16_] = 99999999;
            }
        }
        NameTable.anIntArrayArray186[i_12_][i_13_] = 99;
        int i_17_ = i_12_;
        Class33.anIntArrayArray602[i_12_][i_13_] = 0;
        int i_18_ = 0;
        if (i != 24134) {
            getItemDefinition(44);
        }
        int i_19_ = 0;
        int i_20_ = i_13_;
        JString.anIntArray1239[i_18_] = i_12_;
        Widget.anIntArray2112[i_18_++] = i_13_;
        int i_21_ = JString.anIntArray1239.length;
        int[][] is = (Class39_Sub5_Sub12.aClass27Array1857[NameTable.height].adjancency);
        boolean bool_22_ = false;
        while (i_18_ != i_19_) {
            i_20_ = Widget.anIntArray2112[i_19_];
            i_17_ = JString.anIntArray1239[i_19_];
            i_19_ = (i_19_ + 1) % i_21_;
            if (i_17_ == i_14_ && i_20_ == i_6_) {
                bool_22_ = true;
                break;
            }
            if (i_5_ != 0) {
                if (i_5_ >= 5 && i_5_ != 10
                        || !(Class39_Sub5_Sub12.aClass27Array1857[NameTable.height].method309(i_6_, i_20_, i_14_, i_7_, 28543, i_17_, i_5_ - 1))) {
                    if (i_5_ < 10 && (Class39_Sub5_Sub12.aClass27Array1857[NameTable.height].method307(i_6_, i_14_, (byte) -119, i_5_ - 1,
                            i_7_, i_20_, i_17_))) {
                        bool_22_ = true;
                        break;
                    }
                } else {
                    bool_22_ = true;
                    break;
                }
            }
            if (i_9_ != 0 && i_10_ != 0
                    && (Class39_Sub5_Sub12.aClass27Array1857[NameTable.height].method297(i_11_, (byte) -103, i_20_, i_6_, i_17_, i_10_, i_14_,
                    i_9_))) {
                bool_22_ = true;
                break;
            }
            int i_23_ = Class33.anIntArrayArray602[i_17_][i_20_] + 1;
            if (i_17_ > 0 && NameTable.anIntArrayArray186[i_17_ - 1][i_20_] == 0
                    && (is[i_17_ - 1][i_20_] & 0x1280108) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ - 1;
                Widget.anIntArray2112[i_18_] = i_20_;
                NameTable.anIntArrayArray186[i_17_ - 1][i_20_] = 2;
                Class33.anIntArrayArray602[i_17_ - 1][i_20_] = i_23_;
                i_18_ = (i_18_ + 1) % i_21_;
            }
            if (i_17_ < 103
                    && NameTable.anIntArrayArray186[i_17_ + 1][i_20_] == 0
                    && (is[i_17_ + 1][i_20_] & 0x1280180) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ + 1;
                Widget.anIntArray2112[i_18_] = i_20_;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_ + 1][i_20_] = 8;
                Class33.anIntArrayArray602[i_17_ + 1][i_20_] = i_23_;
            }
            if (i_20_ > 0 && NameTable.anIntArrayArray186[i_17_][i_20_ - 1] == 0
                    && (is[i_17_][i_20_ - 1] & 0x1280102) == 0) {
                JString.anIntArray1239[i_18_] = i_17_;
                Widget.anIntArray2112[i_18_] = i_20_ - 1;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_][i_20_ - 1] = 1;
                Class33.anIntArrayArray602[i_17_][i_20_ - 1] = i_23_;
            }
            if (i_20_ < 103
                    && NameTable.anIntArrayArray186[i_17_][i_20_ + 1] == 0
                    && (is[i_17_][i_20_ + 1] & 0x1280120) == 0) {
                JString.anIntArray1239[i_18_] = i_17_;
                Widget.anIntArray2112[i_18_] = i_20_ + 1;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_][i_20_ + 1] = 4;
                Class33.anIntArrayArray602[i_17_][i_20_ + 1] = i_23_;
            }
            if (i_17_ > 0 && i_20_ > 0
                    && NameTable.anIntArrayArray186[i_17_ - 1][i_20_ - 1] == 0
                    && (is[i_17_ - 1][i_20_ - 1] & 0x128010e) == 0
                    && (is[i_17_ - 1][i_20_] & 0x1280108) == 0
                    && (is[i_17_][i_20_ - 1] & 0x1280102) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ - 1;
                Widget.anIntArray2112[i_18_] = i_20_ - 1;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_ - 1][i_20_ - 1] = 3;
                Class33.anIntArrayArray602[i_17_ - 1][i_20_ - 1] = i_23_;
            }
            if (i_17_ < 103 && i_20_ > 0
                    && NameTable.anIntArrayArray186[i_17_ + 1][i_20_ - 1] == 0
                    && (is[i_17_ + 1][i_20_ - 1] & 0x1280183) == 0
                    && (is[i_17_ + 1][i_20_] & 0x1280180) == 0
                    && (is[i_17_][i_20_ - 1] & 0x1280102) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ + 1;
                Widget.anIntArray2112[i_18_] = i_20_ - 1;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_ + 1][i_20_ - 1] = 9;
                Class33.anIntArrayArray602[i_17_ + 1][i_20_ - 1] = i_23_;
            }
            if (i_17_ > 0 && i_20_ < 103
                    && NameTable.anIntArrayArray186[i_17_ - 1][i_20_ + 1] == 0
                    && (is[i_17_ - 1][i_20_ + 1] & 0x1280138) == 0
                    && (is[i_17_ - 1][i_20_] & 0x1280108) == 0
                    && (is[i_17_][i_20_ + 1] & 0x1280120) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ - 1;
                Widget.anIntArray2112[i_18_] = i_20_ + 1;
                i_18_ = (i_18_ + 1) % i_21_;
                NameTable.anIntArrayArray186[i_17_ - 1][i_20_ + 1] = 6;
                Class33.anIntArrayArray602[i_17_ - 1][i_20_ + 1] = i_23_;
            }
            if (i_17_ < 103 && i_20_ < 103
                    && NameTable.anIntArrayArray186[i_17_ + 1][i_20_ + 1] == 0
                    && (is[i_17_ + 1][i_20_ + 1] & 0x12801e0) == 0
                    && (is[i_17_ + 1][i_20_] & 0x1280180) == 0
                    && (is[i_17_][i_20_ + 1] & 0x1280120) == 0) {
                JString.anIntArray1239[i_18_] = i_17_ + 1;
                Widget.anIntArray2112[i_18_] = i_20_ + 1;
                NameTable.anIntArrayArray186[i_17_ + 1][i_20_ + 1] = 12;
                i_18_ = (i_18_ + 1) % i_21_;
                Class33.anIntArrayArray602[i_17_ + 1][i_20_ + 1] = i_23_;
            }
        }
        CacheIO.anInt99 = 0;
        if (!bool_22_) {
            if (!bool) {
                return false;
            }
            int i_24_ = 10;
            int i_25_ = 1000;
            int i_26_ = 100;
            for (int i_27_ = -i_24_ + i_14_; i_14_ + i_24_ >= i_27_; i_27_++) {
                for (int i_28_ = -i_24_ + i_6_; i_28_ <= i_6_ + i_24_;
                        i_28_++) {
                    if (i_27_ >= 0 && i_28_ >= 0 && i_27_ < 104 && i_28_ < 104
                            && Class33.anIntArrayArray602[i_27_][i_28_] < 100) {
                        int i_29_ = 0;
                        int i_30_ = 0;
                        if (i_28_ < i_6_) {
                            i_30_ = -i_28_ + i_6_;
                        } else if (i_10_ + i_6_ - 1 < i_28_) {
                            i_30_ = i_28_ - i_6_ - i_10_ + 1;
                        }
                        if (i_14_ > i_27_) {
                            i_29_ = -i_27_ + i_14_;
                        } else if (i_27_ > i_14_ + i_9_ - 1) {
                            i_29_ = -i_9_ - (i_14_ - 1 - i_27_);
                        }
                        int i_31_ = i_29_ * i_29_ + i_30_ * i_30_;
                        if (i_31_ < i_25_
                                || i_31_ == i_25_ && i_26_ > (Class33.anIntArrayArray602[i_27_][i_28_])) {
                            i_17_ = i_27_;
                            i_26_ = Class33.anIntArrayArray602[i_27_][i_28_];
                            i_20_ = i_28_;
                            i_25_ = i_31_;
                        }
                    }
                }
            }
            if (i_25_ == 1000) {
                return false;
            }
            if (i_17_ == i_12_ && i_20_ == i_13_) {
                return false;
            }
            CacheIO.anInt99 = 1;
        }
        i_19_ = 0;
        JString.anIntArray1239[i_19_] = i_17_;
        Widget.anIntArray2112[i_19_++] = i_20_;
        int i_33_;
        int i_32_ = i_33_ = NameTable.anIntArrayArray186[i_17_][i_20_];
        while (i_12_ != i_17_ || i_13_ != i_20_) {
            if (i_33_ != i_32_) {
                JString.anIntArray1239[i_19_] = i_17_;
                i_33_ = i_32_;
                Widget.anIntArray2112[i_19_++] = i_20_;
            }
            if ((i_32_ & 0x1) != 0) {
                i_20_++;
            } else if ((i_32_ & 0x4) != 0) {
                i_20_--;
            }
            if ((i_32_ & 0x2) != 0) {
                i_17_++;
            } else if ((i_32_ & 0x8) != 0) {
                i_17_--;
            }
            i_32_ = NameTable.anIntArrayArray186[i_17_][i_20_];
        }
        if (i_19_ > 0) {
            i_21_ = i_19_;
            if (i_21_ > 25) {
                i_21_ = 25;
            }
            i_19_--;
            int i_34_ = Widget.anIntArray2112[i_19_];
            int i_35_ = JString.anIntArray1239[i_19_];
            if (i_8_ == 0) {
                FrameBuffer.outgoingGameBuffer.putFrame(99);
                FrameBuffer.outgoingGameBuffer.putByte(i_21_ + 3 + i_21_);
            }
            if (i_8_ == 1) {
                FrameBuffer.outgoingGameBuffer.putFrame(80);
                FrameBuffer.outgoingGameBuffer.putByte(i_21_ + 3 + (i_21_ + 14));
            }
            if (i_8_ == 2) {
                FrameBuffer.outgoingGameBuffer.putFrame(81);
                FrameBuffer.outgoingGameBuffer.putByte(i_21_ + (i_21_ + 3));
            }
            FrameBuffer.outgoingGameBuffer.method796(-21994, i_35_ + Class65.anInt1145);
            FrameBuffer.outgoingGameBuffer.method785(1474, Class13.aBooleanArray200[82] ? 1 : 0);
            FrameBuffer.outgoingGameBuffer.putWordLe(JKeyListener.anInt618 + i_34_);
            Class30.anInt544 = JString.anIntArray1239[0];
            ArchiveRequest.anInt1407 = Widget.anIntArray2112[0];
            for (int i_36_ = 1; i_36_ < i_21_; i_36_++) {
                i_19_--;
                FrameBuffer.outgoingGameBuffer.putByte(-i_35_ + JString.anIntArray1239[i_19_]);
                FrameBuffer.outgoingGameBuffer.method797(false, Widget.anIntArray2112[i_19_] - i_34_);
            }
            return true;
        }
        if (i_8_ == 1) {
            return false;
        }
        return true;
    }

    public static void method294(boolean bool, JString class3,
            JString class3_37_, int i) {
        int i_38_ = 151;
        i_38_ -= 3;
        if (ClientScript.aBoolean1690) {
            ClientScript.aBoolean1690 = false;
            Varbit.method590(false);
            CacheIO.method127(17);
            Class62_Sub2.method1079(8);
            Class66.method1104(false);
            ArchiveWorker.method1119(Cache.anInt118, Bzip2Block.anInt1051,
                    (Class39_Sub5_Sub14.p12fullFont),
                    41, NameTable.anInt177);
            Class39_Sub5_Sub16.method748(Class39_Sub5_Sub14.anIntArray1914,
                    (StillGraphic.anInt2338
                    == -1),
                    (byte) 105, -1, Node.anInt728);
            Class39_Sub5_Sub14.aBoolean1908 = true;
            Class44.aBoolean833 = true;
            NameTable.aBoolean183 = true;
        }
        ClientScript.method481(-6414);
        Class39_Sub5_Sub14.p12fullFont.method629(class3, 257,
                i_38_, 0);
        Class39_Sub5_Sub14.p12fullFont.method629(class3, 256, i_38_ - 1, 16777215);
        if (class3_37_ != null) {
            i_38_ += 15;
            if (bool) {
                int i_39_ = (Class39_Sub5_Sub14.p12fullFont.method637(class3_37_)
                        + 4);
                DrawingArea.drawQuad(-(i_39_ / 2) + 257, i_38_ - 11,
                        i_39_, 11, 0);
            }
            Class39_Sub5_Sub14.p12fullFont.method629(class3_37_, 257, i_38_, 0);
            Class39_Sub5_Sub14.p12fullFont.method629(class3_37_, 256, i_38_ - 1, 16777215);
        }
        Class46.method940((byte) -103);
    }

    static {
        anInt473 = -1;
        anInt484 = 0;
        aClass3_486 = Class39_Sub5_Sub9.createJstring(" Sekunde(Xn(Y -Ubertragen)3");
        anIntArray496 = new int[1000];
        anInt485 = -1;
        aClass3_489 = Class39_Sub5_Sub9.createJstring("Loading sprites )2 ");
        anInt503 = 0;
        aClass3_490 = aClass3_489;
    }
}
