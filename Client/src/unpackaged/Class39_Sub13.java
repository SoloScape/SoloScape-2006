package unpackaged;

import jagex.io.BufferedFile;
import jagex.utils.HashTable;
import jagex.world.actors.GroundItem;
import jagex.world.actors.Projectile;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;

/* Class39_Sub13 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub13 extends Node {

    public static int anInt1501 = -1;
    public static JMouseListener aClass42_1502;
    public static JString aClass3_1503;
    public int[] anIntArray1504 = new int[1];
    public int[] anIntArray1505 = {-1};
    public static JString aClass3_1506 = Class39_Sub5_Sub9.createJstring("Loaded update list");
    public static JString aClass3_1507;
    public static JString aClass3_1508;
    public static JString aClass3_1509;

    public static int method873(int i, int i_0_, int i_1_, int i_2_) {
        if (i > 179) {
            i_0_ /= 2;
        }
        if (i > 192) {
            i_0_ /= 2;
        }
        if (i > 217) {
            i_0_ /= 2;
        }
        if (i > 243) {
            i_0_ /= 2;
        }
        int i_3_ = i / 2 + ((i_0_ / 32 << 7) + (i_2_ / 4 << 10));
        return i_3_;
    }

    public static void method874(byte i) {
        aClass3_1507 = null;
        aClass3_1509 = null;
        aClass42_1502 = null;
        aClass3_1503 = null;
        aClass3_1508 = null;
        aClass3_1506 = null;
    }

    public static void method875(int i) {
        int i_4_ = -1;
        if (Class13.anInt208 == 0 && !IsaacPrng.aBoolean1100) {
            JString.method55(IsaacPrng.anInt1091, Class39_Sub4.aClass3_1338,
                    Class33.anInt599, Class66.blankString, (byte) -70,
                    0, 33);
        }
        int i_5_ = 0;
        for (/**/; i_5_ < Model.anInt2413; i_5_++) {
            int hash = Model.anIntArray2408[i_5_];
            int i_7_ = hash & 0x7f;
            int i_8_ = (hash & 0x3f85) >> 7;
            int type = (hash & 0x61f56755) >> 29;
            int i_10_ = hash >> 14 & 0x7fff;
            if (i_4_ != hash) {
                i_4_ = hash;
                if (type == 2 && Class44.aClass38_836.method359(NameTable.height, i_7_, i_8_, hash) >= 0) {
                    ObjectDefinition class39_sub5_sub15 = Canvas_Sub1.method40(i_10_, (byte) 108);
                    if (class39_sub5_sub15.anIntArray1961 != null) {
                        class39_sub5_sub15 = class39_sub5_sub15.method733(0);
                    }
                    if (class39_sub5_sub15 == null) {
                        continue;
                    }
                    if (Class13.anInt208 == 1) {
                        JString.method55(i_7_,
                                Class39_Sub5_Sub4_Sub4.aClass3_2310,
                                i_8_,
                                (Class39_Sub5_Sub11.method708((new JString[]{Class39_Sub10.aClass3_1436,
                                    TraversalMap.aClass3_510,
                                    (class39_sub5_sub15.aClass3_1932)}))),
                                (byte) -97, hash, 45);
                    } else if (!IsaacPrng.aBoolean1100) {
                        JString[] class3s = class39_sub5_sub15.aClass3Array1964;
                        if (Class45.aBoolean862) {
                            class3s = BufferedFile.method225((byte) 127, class3s);
                        }
                        if (class3s != null) {
                            for (int i_11_ = 4; i_11_ >= 0; i_11_--) {
                                if (class3s[i_11_] != null) {
                                    int i_12_ = 0;
                                    if (i_11_ == 0) {
                                        i_12_ = 55;
                                    }
                                    if (i_11_ == 1) {
                                        i_12_ = 57;
                                    }
                                    if (i_11_ == 2) {
                                        i_12_ = 43;
                                    }
                                    if (i_11_ == 3) {
                                        i_12_ = 6;
                                    }
                                    if (i_11_ == 4) {
                                        i_12_ = 1005;
                                    }
                                    JString.method55(i_7_, class3s[i_11_], i_8_,
                                            (Class39_Sub5_Sub11.method708((new JString[]{JString.aClass3_1237,
                                                (class39_sub5_sub15.aClass3_1932)}))),
                                            (byte) -74, hash, i_12_);
                                }
                            }
                        }
                        JString.method55(i_7_,
                                RuntimeException_Sub1.aClass3_1224,
                                i_8_,
                                (Class39_Sub5_Sub11.method708(new JString[]{JString.aClass3_1237,
                                    (class39_sub5_sub15.aClass3_1932)})),
                                (byte) -98,
                                class39_sub5_sub15.anInt1931 << 14,
                                1006);
                    } else if ((Class41.anInt776 & 0x4) == 4) {
                        JString.method55(i_7_, Client.aClass3_1273, i_8_,
                                (Class39_Sub5_Sub11.method708(new JString[]{Class14.aClass3_216,
                                    TraversalMap.aClass3_510,
                                    (class39_sub5_sub15.aClass3_1932)})),
                                (byte) -51, hash, 12);
                    }
                }
                if (type == 1) {
                    Npc npc = (GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241[i_10_]);
                    if ((npc.aClass39_Sub5_Sub13_2492.anInt1870) == 1 && (npc.anInt2301 & 0x7f) == 64 && ((npc.anInt2275 & 0x7f) == 64)) {
                        for (int i_13_ = 0; ArchiveWorker.anInt1210 > i_13_;
                                i_13_++) {
                            Npc class39_sub5_sub4_sub4_sub1_14_ = (GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241[Class39_Sub5_Sub4.anIntArray1734[i_13_]]);
                            if (class39_sub5_sub4_sub4_sub1_14_ != null
                                    && (class39_sub5_sub4_sub4_sub1_14_
                                    != npc)
                                    && (class39_sub5_sub4_sub4_sub1_14_.aClass39_Sub5_Sub13_2492.anInt1870) == 1
                                    && (npc.anInt2301
                                    == (class39_sub5_sub4_sub4_sub1_14_.anInt2301))
                                    && (npc.anInt2275
                                    == (class39_sub5_sub4_sub4_sub1_14_.anInt2275))) {
                                ClientApplet.method24(i_7_,
                                        Class39_Sub5_Sub4.anIntArray1734[i_13_],
                                        (byte) -120, i_8_,
                                        (class39_sub5_sub4_sub4_sub1_14_.aClass39_Sub5_Sub13_2492));
                            }
                        }
                        for (int i_15_ = 0; TraversalMap.anInt515 > i_15_;
                                i_15_++) {
                            Player class39_sub5_sub4_sub4_sub2 = (Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[Class1.anIntArray40[i_15_]]);
                            if (class39_sub5_sub4_sub4_sub2 != null
                                    && (class39_sub5_sub4_sub4_sub2.anInt2301
                                    == npc.anInt2301)
                                    && (class39_sub5_sub4_sub4_sub2.anInt2275
                                    == npc.anInt2275)) {
                                ClientScript.method484(i_7_, Class1.anIntArray40[i_15_], i_8_,
                                        class39_sub5_sub4_sub4_sub2, (byte) 61);
                            }
                        }
                    }
                    ClientApplet.method24(i_7_, i_10_, (byte) -103, i_8_,
                            (npc.aClass39_Sub5_Sub13_2492));
                }
                if (type == 0) {
                    Player class39_sub5_sub4_sub4_sub2 = Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_10_];
                    if ((class39_sub5_sub4_sub4_sub2.anInt2301 & 0x7f) == 64
                            && ((class39_sub5_sub4_sub4_sub2.anInt2275 & 0x7f)
                            == 64)) {
                        for (int i_16_ = 0; i_16_ < ArchiveWorker.anInt1210;
                                i_16_++) {
                            Npc class39_sub5_sub4_sub4_sub1 = (GroundItem.aClass39_Sub5_Sub4_Sub4_Sub1Array2241[Class39_Sub5_Sub4.anIntArray1734[i_16_]]);
                            if (class39_sub5_sub4_sub4_sub1 != null
                                    && (class39_sub5_sub4_sub4_sub1.aClass39_Sub5_Sub13_2492.anInt1870) == 1
                                    && (class39_sub5_sub4_sub4_sub1.anInt2301
                                    == class39_sub5_sub4_sub4_sub2.anInt2301)
                                    && (class39_sub5_sub4_sub4_sub1.anInt2275
                                    == class39_sub5_sub4_sub4_sub2.anInt2275)) {
                                ClientApplet.method24(i_7_,
                                        Class39_Sub5_Sub4.anIntArray1734[i_16_],
                                        (byte) -94, i_8_,
                                        (class39_sub5_sub4_sub4_sub1.aClass39_Sub5_Sub13_2492));
                            }
                        }
                        for (int i_17_ = 0; TraversalMap.anInt515 > i_17_;
                                i_17_++) {
                            Player class39_sub5_sub4_sub4_sub2_18_ = (Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[Class1.anIntArray40[i_17_]]);
                            if (class39_sub5_sub4_sub4_sub2_18_ != null
                                    && (class39_sub5_sub4_sub4_sub2_18_
                                    != class39_sub5_sub4_sub4_sub2)
                                    && (class39_sub5_sub4_sub4_sub2_18_.anInt2301
                                    == class39_sub5_sub4_sub4_sub2.anInt2301)
                                    && (class39_sub5_sub4_sub4_sub2_18_.anInt2275
                                    == class39_sub5_sub4_sub4_sub2.anInt2275)) {
                                ClientScript.method484(i_7_, Class1.anIntArray40[i_17_], i_8_,
                                        class39_sub5_sub4_sub4_sub2_18_,
                                        (byte) 12);
                            }
                        }
                    }
                    ClientScript.method484(i_7_, i_10_, i_8_,
                            class39_sub5_sub4_sub4_sub2,
                            (byte) -121);
                }
                if (type == 3) {
                    Deque deque = (Class20.groundItems[NameTable.height][i_7_][i_8_]);
                    if (deque != null) {
                        for (GroundItem groundItem = ((GroundItem) deque.getLast());
                                groundItem != null;
                                groundItem = ((GroundItem) deque.getPrev())) {
                            ItemDefinition class39_sub5_sub1 = Class26.getItemDefinition((groundItem.itemId));
                            if (Class13.anInt208 != 1) {
                                if (IsaacPrng.aBoolean1100) {
                                    if ((Class41.anInt776 & 0x1) == 1) {
                                        JString.method55(i_7_, Client.aClass3_1273, i_8_,
                                                (Class39_Sub5_Sub11.method708((new JString[]{Class14.aClass3_216,
                                                    HashTable.aClass3_375,
                                                    (class39_sub5_sub1.aClass3_1661)}))),
                                                (byte) -47,
                                                groundItem.itemId,
                                                27);
                                    }
                                } else {
                                    JString[] class3s = class39_sub5_sub1.aClass3Array1672;
                                    if (Class45.aBoolean862) {
                                        class3s = BufferedFile.method225((byte) 127,
                                                class3s);
                                    }
                                    for (int i_19_ = 4; i_19_ >= 0; i_19_--) {
                                        if (class3s == null
                                                || class3s[i_19_] == null) {
                                            if (i_19_ == 2) {
                                                JString.method55(i_7_,
                                                        (Projectile.aClass3_2190),
                                                        i_8_,
                                                        (Class39_Sub5_Sub11.method708((new JString[]{(FrameBuffer.aClass3_2147),
                                                            (class39_sub5_sub1.aClass3_1661)}))),
                                                        (byte) -103,
                                                        (groundItem.itemId),
                                                        34);
                                            }
                                        } else {
                                            int i_20_ = 0;
                                            if (i_19_ == 0) {
                                                i_20_ = 40;
                                            }
                                            if (i_19_ == 1) {
                                                i_20_ = 38;
                                            }
                                            if (i_19_ == 2) {
                                                i_20_ = 34;
                                            }
                                            if (i_19_ == 3) {
                                                i_20_ = 11;
                                            }
                                            if (i_19_ == 4) {
                                                i_20_ = 3;
                                            }
                                            JString.method55(i_7_, class3s[i_19_], i_8_,
                                                    (Class39_Sub5_Sub11.method708((new JString[]{(FrameBuffer.aClass3_2147),
                                                        (class39_sub5_sub1.aClass3_1661)}))),
                                                    (byte) -15,
                                                    (groundItem.itemId),
                                                    i_20_);
                                        }
                                    }
                                    JString.method55(i_7_,
                                            RuntimeException_Sub1.aClass3_1224,
                                            i_8_,
                                            (Class39_Sub5_Sub11.method708((new JString[]{FrameBuffer.aClass3_2147,
                                                (class39_sub5_sub1.aClass3_1661)}))),
                                            (byte) -80,
                                            groundItem.itemId,
                                            1003);
                                }
                            } else {
                                JString.method55(i_7_, Class39_Sub5_Sub4_Sub4.aClass3_2310,
                                        i_8_,
                                        (Class39_Sub5_Sub11.method708((new JString[]{Class39_Sub10.aClass3_1436,
                                            HashTable.aClass3_375,
                                            class39_sub5_sub1.aClass3_1661}))),
                                        (byte) -63,
                                        groundItem.itemId, 4);
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        aClass3_1503 = aClass3_1506;
        aClass3_1507 = Class39_Sub5_Sub9.createJstring("Name eingeben:");
        aClass42_1502 = new JMouseListener();
        aClass3_1508 = Class39_Sub5_Sub9.createJstring("Drop");
        aClass3_1509 = aClass3_1508;
    }
}
