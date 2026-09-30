package unpackaged;

/* Class65 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.world.actors.GroundItem;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.awt.Component;

public class Class65 {

    public static int[] anIntArray1132;
    public static JString aClass3_1133;
    public static JString aClass3_1134;
    public static int anInt1135;
    public static Widget aClass39_Sub5_Sub17_1136;
    public static int anInt1137 = 0;
    public static int odErrors = 0;
    public static JString aClass3_1139;
    public static JString aClass3_1140;
    public static int anInt1141;
    public static JString aClass3_1142;
    public static int anInt1143;
    public static int odConnectionDelay;
    public static int anInt1145;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_1146;
    public static int anInt1147;

    public static void updateGroundItems(int x, int y) {
        Deque deque = Class20.groundItems[NameTable.height][x][y];
        if (deque == null) {
            Class44.aClass38_836.method373(NameTable.height, x, y);
        } else {
            int i_2_ = -99999999;
            GroundItem class39_sub5_sub4_sub3 = null;
            for (GroundItem groundItem = (GroundItem) deque.getFirst();
                    groundItem != null;
                    groundItem = ((GroundItem) deque.getNext())) {
                ItemDefinition class39_sub5_sub1 = Class26.getItemDefinition(groundItem.itemId);
                int i_4_ = class39_sub5_sub1.anInt1686;
                if (class39_sub5_sub1.anInt1662 == 1) {
                    i_4_ *= groundItem.anInt2243 + 1;
                }
                if (i_4_ > i_2_) {
                    class39_sub5_sub4_sub3 = groundItem;
                    i_2_ = i_4_;
                }
            }
            if (class39_sub5_sub4_sub3 == null) {
                Class44.aClass38_836.method373(NameTable.height, x, y);
            } else {
                deque.offerFirst(class39_sub5_sub4_sub3);
                GroundItem class39_sub5_sub4_sub3_5_ = null;
                int i_6_ = x - (-(y << 7) - 1610612736);
                GroundItem class39_sub5_sub4_sub3_7_ = (GroundItem) deque.getFirst();
                GroundItem class39_sub5_sub4_sub3_8_ = null;
                for (/**/; class39_sub5_sub4_sub3_7_ != null;
                        class39_sub5_sub4_sub3_7_ = ((GroundItem) deque.getNext())) {
                    if (class39_sub5_sub4_sub3_7_.itemId
                            != class39_sub5_sub4_sub3.itemId) {
                        if (class39_sub5_sub4_sub3_5_ == null) {
                            class39_sub5_sub4_sub3_5_ = class39_sub5_sub4_sub3_7_;
                        }
                        if ((class39_sub5_sub4_sub3_7_.itemId
                                != class39_sub5_sub4_sub3_5_.itemId)
                                && class39_sub5_sub4_sub3_8_ == null) {
                            class39_sub5_sub4_sub3_8_ = class39_sub5_sub4_sub3_7_;
                        }
                    }
                }
                Class44.aClass38_836.method391(NameTable.height, x, y,
                        Class14.method212(y * 128 + 64, 9990, NameTable.height,
                        x * 128 + 64),
                        class39_sub5_sub4_sub3, i_6_, class39_sub5_sub4_sub3_5_,
                        class39_sub5_sub4_sub3_8_);
            }
        }
    }

    public static void method1093(int i) {
        anIntArray1132 = null;
        aClass3_1134 = null;
        aClass3_1140 = null;
        aClass3_1139 = null;
        aClass3_1142 = null;
        aClass39_Sub5_Sub17_1136 = null;
        aClass39_Sub5_Sub10_Sub4_1146 = null;
        aClass3_1133 = null;
    }

    public static int[] method1094(int i,
            Widget class39_sub5_sub17) {
        int i_9_ = class39_sub5_sub17.anInt2084 >> 16;
        if (!JSocket.loadWidget(i_9_)) {
            return null;
        }
        int i_10_ = class39_sub5_sub17.anInt2091;
        int i_11_ = class39_sub5_sub17.anInt2021;
        Widget class39_sub5_sub17_12_;
        for (int i_13_ = class39_sub5_sub17.anInt2050; i_13_ != -1;
                i_13_ = class39_sub5_sub17_12_.anInt2050) {
            class39_sub5_sub17_12_ = (Class62_Sub1.widgets[i_9_][i_13_ & 0xffff]);
            i_11_ += (class39_sub5_sub17_12_.anInt2021
                    - class39_sub5_sub17_12_.anInt1994);
            i_10_ += (-class39_sub5_sub17_12_.anInt2064
                    + class39_sub5_sub17_12_.anInt2091);
        }
        int[] is = new int[2];
        is[1] = i_11_;
        is[0] = i_10_;
        return is;
    }

    public static void method1095(int i) {
        synchronized (Class4.archiveWorkerLock) {
            if (Class23.archiveWorkerKeepAlive == 0) {
                Class39_Sub5_Sub9.signlink.requestThread(new ArchiveWorker(), 5);
            }
            Class23.archiveWorkerKeepAlive = 600;
        }
    }

    public static Class39_Sub1_Sub1 method1096(boolean bool, Signlink class21,
            Component component, int i,
            int i_14_) {
        Class15.method214(i_14_, component, bool, class21, 0);
        Class39_Sub1_Sub1 class39_sub1_sub1 = new Class39_Sub1_Sub1();
        Class13.method191((byte) 121, class39_sub1_sub1);
        return class39_sub1_sub1;
    }

    public static void method1097(int i, long l) {
        if (l != 0L) {
            if (Class15.amountIgnores >= 100) {
                JMouseListener.method902(Class66.blankString, Class23.aClass3_431,
                        false, 0);
            } else {
                JString class3 = Deque.decodeBase37(l).formatUsername();
                for (int i_15_ = 0; Class15.amountIgnores > i_15_; i_15_++) {
                    if (l == Class39_Sub5_Sub9.ignoreUsernames[i_15_]) {
                        JMouseListener.method902(Class66.blankString,
                                (Class39_Sub5_Sub11.method708((new JString[]{class3,
                                    SubNode.aClass3_1343}))),
                                false, 0);
                        return;
                    }
                }
                for (int i_16_ = i; i_16_ < Class4.anInt62; i_16_++) {
                    if (l == ClientApplet.aLongArray2[i_16_]) {
                        JMouseListener.method902(Class66.blankString,
                                (Class39_Sub5_Sub11.method708((new JString[]{Class39_Sub11.aClass3_1467,
                                    class3,
                                    (RuntimeException_Sub1.aClass3_1214)}))),
                                false, 0);
                        return;
                    }
                }
                if (!class3.isEqual((Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.aClass3_2521))) {
                    Class39_Sub5_Sub9.ignoreUsernames[Class15.amountIgnores++] = l;
                    Class39_Sub14.aBoolean1520 = true;
                    FrameBuffer.outgoingGameBuffer.putFrame(198);
                    FrameBuffer.outgoingGameBuffer.putQword(l);
                }
            }
        }
    }

    public static Class39_Sub5_Sub5 method1098(byte i, int i_17_) {
        Class39_Sub5_Sub5 class39_sub5_sub5 = ((Class39_Sub5_Sub5) FrameBuffer.aClass7_2158.get((long) i_17_));
        if (class39_sub5_sub5 != null) {
            return class39_sub5_sub5;
        }
        byte[] is = Bzip2Block.aClass9_1072.lookupFile(5, i_17_);
        class39_sub5_sub5 = new Class39_Sub5_Sub5();
        if (is != null) {
            class39_sub5_sub5.method572(new Buffer(is), (byte) 24);
        }
        FrameBuffer.aClass7_2158.put(class39_sub5_sub5,
                (long) i_17_, (byte) 122);
        return class39_sub5_sub5;
    }

    public static void method1099(byte i, FileTable class9) {
        ItemDefinition.aClass9_1680 = class9;
    }

    static {
        aClass3_1134 = Class39_Sub5_Sub9.createJstring("Please use a different world)3");
        aClass3_1133 = Class39_Sub5_Sub9.createJstring("Spiel)2Fenster geladen)3");
        aClass3_1140 = aClass3_1134;
        aClass3_1139 = Class39_Sub5_Sub9.createJstring("Ausw-=hlen");
        odConnectionDelay = 0;
        aClass3_1142 = aClass3_1134;
        anInt1141 = 0;
    }
}
