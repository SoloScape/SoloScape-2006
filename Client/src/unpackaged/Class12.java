package unpackaged;

import jagex.world.actors.GroundItem;
import jagex.world.actors.StillGraphic;
import jagex.world.actors.Projectile;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.utils.Deque;
import jagex.io.FrameBuffer;
import jagex.utils.Cache;

/* Class12 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class12 {

    public static JString aClass3_191 = Class39_Sub5_Sub9.createJstring(")2");
    public static JString aClass3_192;
    public static JString aClass3_193;
    public static int anInt194 = -1;
    public static JString aClass3_195;
    public static int[] anIntArray196;

    public static void parseEntityFrame(int i) {
        if (Class4.frameId == 79) {
            int locationHash = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int x = Node.mSectorX + (locationHash >> 4 & 0x7);
            int y = (locationHash & 0x7) + IsaacPrng.mSectorY;
            int i_3_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            int i_4_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            int i_5_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Deque class49 = (Class20.groundItems[NameTable.height][x][y]);
                if (class49 != null) {
                    for (GroundItem class39_sub5_sub4_sub3 = ((GroundItem) class49.getFirst());
                            class39_sub5_sub4_sub3 != null;
                            class39_sub5_sub4_sub3 = ((GroundItem) class49.getNext())) {
                        if ((i_3_ & 0x7fff) == class39_sub5_sub4_sub3.itemId
                                && i_4_ == class39_sub5_sub4_sub3.anInt2243) {
                            class39_sub5_sub4_sub3.anInt2243 = i_5_;
                            break;
                        }
                    }
                    Class65.updateGroundItems(x, y);
                }
            }
        } else if (Class4.frameId == 94) {
            int i_6_ = Class39_Sub5_Sub11.gameBuffer.method818(i ^ ~0xb12);
            int i_7_ = Class39_Sub5_Sub11.gameBuffer.method818(-1);
            int i_8_ = Class39_Sub5_Sub11.gameBuffer.method815((byte) -17);
            int i_9_ = Node.mSectorX + ((i_8_ & 0x7a) >> 4);
            int i_10_ = (i_8_ & 0x7) + IsaacPrng.mSectorY;
            int i_11_ = Class39_Sub5_Sub11.gameBuffer.method818(-1);
            if (i_9_ >= 0 && i_10_ >= 0 && i_9_ < 104 && i_10_ < 104
                    && Class39_Sub13.anInt1501 != i_6_) {
                GroundItem class39_sub5_sub4_sub3 = new GroundItem();
                class39_sub5_sub4_sub3.anInt2243 = i_7_;
                class39_sub5_sub4_sub3.itemId = i_11_;
                if ((Class20.groundItems[NameTable.height][i_9_][i_10_])
                        == null) {
                    Class20.groundItems[NameTable.height][i_9_][i_10_] = new Deque();
                }
                Class20.groundItems[NameTable.height][i_9_][i_10_].offerLast(class39_sub5_sub4_sub3);
                Class65.updateGroundItems(i_9_, i_10_);
            }
        } else if (Class4.frameId == 84) {
            int i_12_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_13_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int i_14_ = (i_13_ & 0x7) + IsaacPrng.mSectorY;
            int i_15_ = Node.mSectorX + ((i_13_ & 0x7a) >> 4);
            if (i_15_ >= 0 && i_14_ >= 0 && i_15_ < 104 && i_14_ < 104) {
                Deque class49 = (Class20.groundItems[NameTable.height][i_15_][i_14_]);
                if (class49 != null) {
                    for (GroundItem class39_sub5_sub4_sub3 = ((GroundItem) class49.getFirst());
                            class39_sub5_sub4_sub3 != null;
                            class39_sub5_sub4_sub3 = ((GroundItem) class49.getNext())) {
                        if (class39_sub5_sub4_sub3.itemId
                                == (i_12_ & 0x7fff)) {
                            class39_sub5_sub4_sub3.unlinkDeque();
                            break;
                        }
                    }
                    if (class49.getFirst() == null) {
                        Class20.groundItems[NameTable.height][i_15_][i_14_] = null;
                    }
                    Class65.updateGroundItems(i_15_, i_14_);
                }
            }
        } else if (Class4.frameId == 69) {
            int i_16_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int i_17_ = i_16_ >> 2;
            int i_18_ = i_16_ & 0x3;
            int i_19_ = Class33.anIntArray598[i_17_];
            int i_20_ = Class39_Sub5_Sub11.gameBuffer.method788((byte) 22);
            int i_21_ = (i_20_ >> 4 & 0x7) + Node.mSectorX;
            int i_22_ = IsaacPrng.mSectorY + (i_20_ & 0x7);
            if (i_21_ >= 0 && i_22_ >= 0 && i_21_ < 104 && i_22_ < 104) {
                NpcDefinition.method722(0, i_22_, -1, NameTable.height,
                        (byte) 93, i_18_, i_17_, i_19_,
                        -1, i_21_);
            }
        } else if (Class4.frameId == 101) {
            int i_23_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int i_24_ = (i_23_ & 0x7) + IsaacPrng.mSectorY;
            int i_25_ = Node.mSectorX + ((i_23_ & 0x7c) >> 4);
            int i_26_ = i_25_ + Class39_Sub5_Sub11.gameBuffer.getByte();
            int i_27_ = i_24_ + Class39_Sub5_Sub11.gameBuffer.getByte();
            int i_28_ = Class39_Sub5_Sub11.gameBuffer.getWord();
            int i_29_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            int i_30_ = (Class39_Sub5_Sub11.gameBuffer.getUbyte()
                    * 4);
            int i_31_ = (Class39_Sub5_Sub11.gameBuffer.getUbyte()
                    * 4);
            int i_32_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            int i_33_ = Class39_Sub5_Sub11.gameBuffer.getUword();
            int i_34_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int i_35_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            if (i_25_ >= 0 && i_24_ >= 0 && i_25_ < 104 && i_24_ < 104
                    && i_26_ >= 0 && i_27_ >= 0 && i_26_ < 104 && i_27_ < 104
                    && i_29_ != 65535) {
                i_27_ = i_27_ * 128 + 64;
                i_25_ = i_25_ * 128 + 64;
                i_24_ = i_24_ * 128 + 64;
                i_26_ = i_26_ * 128 + 64;
                Projectile projectile = (new Projectile(i_29_, NameTable.height, i_25_, i_24_,
                        -i_30_ + Class14.method212(i_24_, 9990,
                        NameTable.height, i_25_),
                        i_32_ + Class2.logicCycle, Class2.logicCycle + i_33_, i_34_,
                        i_35_, i_28_, i_31_));
                projectile.method497(-i_31_ + Class14.method212(i_27_, 9990, NameTable.height, i_26_), 63, i_27_, i_32_ + Class2.logicCycle, i_26_);
                Class23.projectiles.offerLast(projectile);
            }
        } else if (Class4.frameId == 207) {
            int i_36_ = Class39_Sub5_Sub11.gameBuffer.method818(-1);
            int i_37_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_38_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
            int i_39_ = ((i_38_ & 0x7a) >> 4) + Node.mSectorX;
            int i_40_ = IsaacPrng.mSectorY + (i_38_ & 0x7);
            if (i_39_ >= 0 && i_40_ >= 0 && i_39_ < 104 && i_40_ < 104) {
                GroundItem class39_sub5_sub4_sub3 = new GroundItem();
                class39_sub5_sub4_sub3.itemId = i_37_;
                class39_sub5_sub4_sub3.anInt2243 = i_36_;
                if ((Class20.groundItems[NameTable.height][i_39_][i_40_])
                        == null) {
                    Class20.groundItems[NameTable.height][i_39_][i_40_] = new Deque();
                }
                Class20.groundItems[NameTable.height][i_39_][i_40_].offerLast(class39_sub5_sub4_sub3);
                Class65.updateGroundItems(i_39_, i_40_);
            }
        } else {
            if (i != 2834) {
                method186(-9);
            }
            if (Class4.frameId == 170) {
                int i_41_ = Class39_Sub5_Sub11.gameBuffer.method833((byte) 117);
                int i_42_ = Class39_Sub5_Sub11.gameBuffer.method788((byte) 47);
                int i_43_ = i_42_ >> 2;
                int i_44_ = i_42_ & 0x3;
                int type = Class33.anIntArray598[i_43_];
                int i_46_ = Class39_Sub5_Sub11.gameBuffer.method804(34);
                int i_47_ = ((i_46_ & 0x7e) >> 4) + Node.mSectorX;
                int i_48_ = (i_46_ & 0x7) + IsaacPrng.mSectorY;
                if (i_47_ >= 0 && i_48_ >= 0 && i_47_ < 103 && i_48_ < 103) {
                    int i_49_ = (Class67.heightMap[NameTable.height][i_47_][i_48_]);
                    int i_50_ = (Class67.heightMap[NameTable.height][i_47_ + 1][i_48_]);
                    int i_51_ = (Class67.heightMap[NameTable.height][i_47_ + 1][i_48_ + 1]);
                    int i_52_ = (Class67.heightMap[NameTable.height][i_47_][i_48_ + 1]);
                    if (type == 0) {
                        Class36 class36 = Class44.aClass38_836.method356(NameTable.height,
                                i_47_, i_48_);
                        if (class36 != null) {
                            int i_53_ = class36.anInt637 >> 14 & 0x7fff;
                            if (i_43_ == 2) {
                                class36.aClass39_Sub5_Sub4_646 = (new Class39_Sub5_Sub4_Sub2(i_53_, 2, 4 + i_44_, i_49_, i_50_,
                                        i_51_, i_52_, i_41_, false,
                                        class36.aClass39_Sub5_Sub4_646));
                                class36.aClass39_Sub5_Sub4_641 = (new Class39_Sub5_Sub4_Sub2(i_53_, 2, i_44_ + 1 & 0x3, i_49_,
                                        i_50_, i_51_, i_52_, i_41_, false,
                                        class36.aClass39_Sub5_Sub4_641));
                            } else {
                                class36.aClass39_Sub5_Sub4_646 = (new Class39_Sub5_Sub4_Sub2(i_53_, i_43_, i_44_, i_49_, i_50_,
                                        i_51_, i_52_, i_41_, false,
                                        class36.aClass39_Sub5_Sub4_646));
                            }
                        }
                    }
                    if (type == 1) {
                        Class44 class44 = Class44.aClass38_836.method358(NameTable.height,
                                i_47_, i_48_);
                        if (class44 != null) {
                            class44.aClass39_Sub5_Sub4_846 = (new Class39_Sub5_Sub4_Sub2(class44.anInt842 >> 14 & 0x7fff, 4, 0,
                                    i_49_, i_50_, i_51_, i_52_, i_41_, false,
                                    class44.aClass39_Sub5_Sub4_846));
                        }
                    }
                    if (type == 2) {
                        if (i_43_ == 11) {
                            i_43_ = 10;
                        }
                        Class10 class10 = Class44.aClass38_836.method374(NameTable.height,
                                i_47_, i_48_);
                        if (class10 != null) {
                            class10.aClass39_Sub5_Sub4_154 = (new Class39_Sub5_Sub4_Sub2(class10.anInt157 >> 14 & 0x7fff, i_43_,
                                    i_44_, i_49_, i_50_, i_51_, i_52_, i_41_,
                                    false, class10.aClass39_Sub5_Sub4_154));
                        }
                    }
                    if (type == 3) {
                        Class50 class50 = Class44.aClass38_836.method400(NameTable.height,
                                i_47_, i_48_);
                        if (class50 != null) {
                            class50.aClass39_Sub5_Sub4_933 = (new Class39_Sub5_Sub4_Sub2(class50.anInt935 >> 14 & 0x7fff, 22, i_44_,
                                    i_49_, i_50_, i_51_, i_52_, i_41_, false,
                                    class50.aClass39_Sub5_Sub4_933));
                        }
                    }
                }
            } else if (Class4.frameId == 122) {
                int i_54_ = Class39_Sub5_Sub11.gameBuffer.method815((byte) 121);
                int i_55_ = IsaacPrng.mSectorY + (i_54_ & 0x7);
                int i_56_ = ((i_54_ & 0x78) >> 4) + Node.mSectorX;
                int i_57_ = Class39_Sub5_Sub11.gameBuffer.getUword();
                int i_58_ = Class39_Sub5_Sub11.gameBuffer.method788((byte) 17);
                int i_59_ = i_58_ & 0x3;
                int i_60_ = i_58_ >> 2;
                int i_61_ = Class33.anIntArray598[i_60_];
                if (i_56_ >= 0 && i_55_ >= 0 && i_56_ < 104 && i_55_ < 104) {
                    NpcDefinition.method722(0, i_55_, -1,
                            NameTable.height, (byte) 106,
                            i_59_, i_60_, i_61_, i_57_,
                            i_56_);
                }
            } else if (Class4.frameId == 115) {
                int i_62_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
                int i_63_ = Node.mSectorX + (i_62_ >> 4 & 0x7);
                int i_64_ = (i_62_ & 0x7) + IsaacPrng.mSectorY;
                int i_65_ = Class39_Sub5_Sub11.gameBuffer.getUword();
                int i_66_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
                int i_67_ = Class39_Sub5_Sub11.gameBuffer.getUword();
                if (i_63_ >= 0 && i_64_ >= 0 && i_63_ < 104 && i_64_ < 104) {
                    i_64_ = i_64_ * 128 + 64;
                    i_63_ = i_63_ * 128 + 64;
                    StillGraphic class39_sub5_sub4_sub5 = (new StillGraphic(i_65_, NameTable.height, i_63_, i_64_,
                            Class14.method212(i_64_, 9990, NameTable.height,
                            i_63_) - i_66_,
                            i_67_, Class2.logicCycle));
                    RuntimeException_Sub1.aClass49_1217.offerLast(class39_sub5_sub4_sub5);
                }
            } else {
                if (Class4.frameId == 129) {
                    int i_68_ = Class39_Sub5_Sub11.gameBuffer.method810(4);
                    int i_69_ = Class39_Sub5_Sub11.gameBuffer.method799(4606);
                    int i_70_ = Class39_Sub5_Sub11.gameBuffer.method818(-1);
                    int i_71_ = Class39_Sub5_Sub11.gameBuffer.method799(4606);
                    int i_72_ = Class39_Sub5_Sub11.gameBuffer.method813((byte) 62);
                    int i_73_ = Class39_Sub5_Sub11.gameBuffer.method818(i ^ ~0xb12);
                    int i_74_ = Class39_Sub5_Sub11.gameBuffer.method815((byte) 122);
                    int i_75_ = i_74_ >> 2;
                    int i_76_ = Class33.anIntArray598[i_75_];
                    int i_77_ = i_74_ & 0x3;
                    int i_78_ = Class39_Sub5_Sub11.gameBuffer.method804(22);
                    int i_79_ = Node.mSectorX + ((i_78_ & 0x77) >> 4);
                    int i_80_ = (i_78_ & 0x7) + IsaacPrng.mSectorY;
                    int i_81_ = Class39_Sub5_Sub11.gameBuffer.getUword();
                    int i_82_ = Class39_Sub5_Sub11.gameBuffer.method833((byte) 118);
                    Player class39_sub5_sub4_sub4_sub2;
                    if (i_82_ == Class39_Sub13.anInt1501) {
                        class39_sub5_sub4_sub4_sub2 = Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109;
                    } else {
                        class39_sub5_sub4_sub4_sub2 = (Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_82_]);
                    }
                    if (class39_sub5_sub4_sub4_sub2 != null) {
                        ObjectDefinition class39_sub5_sub15 = Canvas_Sub1.method40(i_70_, (byte) 77);
                        int i_83_ = (Class67.heightMap[NameTable.height][i_79_][i_80_ + 1]);
                        int i_84_ = (Class67.heightMap[NameTable.height][i_79_][i_80_]);
                        int i_85_ = (Class67.heightMap[NameTable.height][i_79_ + 1][i_80_]);
                        int i_86_ = (Class67.heightMap[NameTable.height][i_79_ + 1][i_80_ + 1]);
                        Model class39_sub5_sub4_sub6 = class39_sub5_sub15.method742(66, i_75_, i_84_,
                                i_77_, i_83_, i_86_,
                                i_85_);
                        if (class39_sub5_sub4_sub6 != null) {
                            NpcDefinition.method722(i_73_ + 1, i_80_,
                                    i_81_ + 1,
                                    NameTable.height,
                                    (byte) 98, 0, 0,
                                    i_76_, -1, i_79_);
                            if (i_71_ > i_69_) {
                                int i_87_ = i_71_;
                                i_71_ = i_69_;
                                i_69_ = i_87_;
                            }
                            class39_sub5_sub4_sub4_sub2.anInt2512 = Class2.logicCycle + i_73_;
                            class39_sub5_sub4_sub4_sub2.aClass39_Sub5_Sub4_Sub6_2520 = class39_sub5_sub4_sub6;
                            if (i_68_ < i_72_) {
                                int i_88_ = i_72_;
                                i_72_ = i_68_;
                                i_68_ = i_88_;
                            }
                            class39_sub5_sub4_sub4_sub2.anInt2523 = Class2.logicCycle + i_81_;
                            int i_89_ = class39_sub5_sub15.anInt1925;
                            int i_90_ = class39_sub5_sub15.anInt1948;
                            if (i_77_ == 1 || i_77_ == 3) {
                                i_90_ = class39_sub5_sub15.anInt1925;
                                i_89_ = class39_sub5_sub15.anInt1948;
                            }
                            class39_sub5_sub4_sub4_sub2.anInt2514 = i_79_ * 128 + i_89_ * 64;
                            class39_sub5_sub4_sub4_sub2.anInt2518 = i_80_ * 128 + i_90_ * 64;
                            class39_sub5_sub4_sub4_sub2.anInt2519 = (Class14.method212(class39_sub5_sub4_sub4_sub2.anInt2518,
                                    9990, NameTable.height,
                                    class39_sub5_sub4_sub4_sub2.anInt2514));
                            class39_sub5_sub4_sub4_sub2.anInt2513 = i_79_ + i_69_;
                            class39_sub5_sub4_sub4_sub2.anInt2510 = i_72_ + i_80_;
                            class39_sub5_sub4_sub4_sub2.anInt2511 = i_71_ + i_79_;
                            class39_sub5_sub4_sub4_sub2.anInt2522 = i_80_ + i_68_;
                        }
                    }
                }
                if (Class4.frameId == 109) {
                    int i_91_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
                    int i_92_ = ((i_91_ & 0x75) >> 4) + Node.mSectorX;
                    int i_93_ = IsaacPrng.mSectorY + (i_91_ & 0x7);
                    int i_94_ = Class39_Sub5_Sub11.gameBuffer.getUword();
                    int i_95_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
                    int i_96_ = (i_95_ & 0xf0) >> 4;
                    int i_97_ = Class39_Sub5_Sub11.gameBuffer.getUbyte();
                    int i_98_ = i_95_ & 0x7;
                    if (i_92_ >= 0 && i_93_ >= 0 && i_92_ < 104
                            && i_93_ < 104) {
                        int i_99_ = i_96_ + 1;
                        if ((Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2314[0]) >= -i_99_ + i_92_
                                && (i_92_ + i_99_
                                >= (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2314[0]))
                                && (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2255[0]) >= i_93_ - i_99_
                                && (i_93_ + i_99_
                                >= (Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.anIntArray2255[0]))
                                && FileTable.anInt149 != 0 && i_98_ > 0
                                && Projectile.anInt2180 < 50) {
                            ArchiveWorker.anIntArray1201[(Projectile.anInt2180)] = i_94_;
                            FileLoader.anIntArray1295[(Projectile.anInt2180)] = i_98_;
                            Class39_Sub5_Sub9.anIntArray1811[Projectile.anInt2180] = i_97_;
                            Class15.aClass52Array285[(Projectile.anInt2180)] = null;
                            FrameBuffer.anIntArray2150[Projectile.anInt2180] = i_96_ + (i_93_ << 8) + (i_92_ << 16);
                            Projectile.anInt2180++;
                        }
                    }
                }
            }
        }
    }

    public static void method186(int i) {
        aClass3_195 = null;
        aClass3_192 = null;
        aClass3_193 = null;
        aClass3_191 = null;
        anIntArray196 = null;
    }

    static {
        aClass3_193 = Class39_Sub5_Sub9.createJstring("Der Server wird gerade aktualisiert)3");
        aClass3_192 = Class39_Sub5_Sub9.createJstring(" )2> ");
        aClass3_195 = Class39_Sub5_Sub9.createJstring("Abbrechen");
        anIntArray196 = new int[500];
    }
}
