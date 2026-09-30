package unpackaged;

/* Class25 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.io.BufferedFile;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.graphics.JImage;
import jagex.graphics.AbstractImage;
import jagex.world.actors.Projectile;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.map.TraversalMap;
import jagex.utils.Queue;
import jagex.io.FrameBuffer;
import jagex.io.Buffer;
import jagex.utils.Cache;
import java.io.IOException;
import java.net.Socket;

public class Class25 {

    public static JString aClass3_457 = Class39_Sub5_Sub9.createJstring("Remove");
    public static JString aClass3_458;
    public static int anInt459 = 0;
    public static JString aClass3_460;
    public static HashTable priorityRequestsQueue;
    public static JString aClass3_462;
    public static JString aClass3_463 = Class39_Sub5_Sub9.createJstring("Please contact customer support)3");
    public static JString aClass3_464;
    public static ClientApplet anApplet_Sub1_465;
    public static JString aClass3_466;
    public static BufferedFile tableFile;
    public static JString aClass3_468;
    public static JString aClass3_469;
    public static JString aClass3_470;
    public static int anInt471;
    public static Huffmans huffmans;

    public static void method281(int i, int i_0_, int i_1_, boolean bool) {
        Widget class39_sub5_sub17 = Class62_Sub2.method1081(i_1_, i_0_, 0);
        if (class39_sub5_sub17 != null
                && class39_sub5_sub17.anObjectArray2099 != null) {
            Class39_Sub5_Sub4_Sub4.executeClientScript(0,
                    (class39_sub5_sub17.anObjectArray2099),
                    null, 114, 0, class39_sub5_sub17,
                    0);
        }
        IsaacPrng.aBoolean1100 = bool;
        Class31.anInt570 = i_0_;
        Class41.anInt776 = i;
        Class41.anInt775 = i_1_;
    }

    public static int calculateChecksum(byte[] src, int startOff, int endOff) {
        int checksum = -1;
        for (int i = startOff; endOff > i; i++) {
            checksum = (Class39_Sub7.anIntArray1375[(checksum ^ src[i]) & 0xff] ^ checksum >>> 8);
        }
        checksum ^= 0xffffffff;
        return checksum;
    }

    public static void method283(int i, int i_6_, int i_7_, byte i_8_,
            int i_9_, int i_10_, int i_11_, int i_12_,
            int i_13_) {
        if (JSocket.loadWidget(i_11_)) {
            Class39_Sub4.method460(-1, 0, i_12_, 0, i_9_, -1, i_7_, i_10_, i,
                    (Class62_Sub1.widgets[i_11_]),
                    i_6_, i_13_);
        }
    }

    public static void method284(int i, boolean bool) {
        Class63.aBoolean1120 = bool;
        if (i != -519790717) {
            aClass3_466 = null;
        }
        if (!Class63.aBoolean1120) {
            int i_14_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_15_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_16_ = Class39_Sub5_Sub11.gameBuffer.method833((byte) 127);
            int i_17_ = Class39_Sub5_Sub11.gameBuffer.method788((byte) -98);
            int i_18_ = ((Huffmans.frameSize
                    - Class39_Sub5_Sub11.gameBuffer.offset)
                    / 16);
            Class14.anIntArrayArray221 = new int[i_18_][4];
            for (int i_19_ = 0; i_19_ < i_18_; i_19_++) {
                for (int i_20_ = 0; i_20_ < 4; i_20_++) {
                    Class14.anIntArrayArray221[i_19_][i_20_] = Class39_Sub5_Sub11.gameBuffer.method829();
                }
            }
            int i_21_ = Class39_Sub5_Sub11.gameBuffer.method818(-1);
            Class65.anIntArray1132 = new int[i_18_];
            Class55.anIntArray1255 = new int[i_18_];
            Cache.aByteArrayArray104 = new byte[i_18_][];
            Class39_Sub5_Sub16.aByteArrayArray1989 = new byte[i_18_][];
            boolean bool_22_ = false;
            ItemDefinition.anIntArray1682 = new int[i_18_];
            i_18_ = 0;
            if ((i_14_ / 8 == 48 || i_14_ / 8 == 49) && i_21_ / 8 == 48) {
                bool_22_ = true;
            }
            if (i_14_ / 8 == 48 && i_21_ / 8 == 148) {
                bool_22_ = true;
            }
            for (int i_23_ = (i_14_ - 6) / 8; (i_14_ + 6) / 8 >= i_23_;
                    i_23_++) {
                for (int i_24_ = (i_21_ - 6) / 8; i_24_ <= (i_21_ + 6) / 8;
                        i_24_++) {
                    int i_25_ = (i_23_ << 8) + i_24_;
                    if (!bool_22_
                            || (i_24_ != 49 && i_24_ != 149 && i_24_ != 147
                            && i_23_ != 50 && (i_23_ != 49 || i_24_ != 47))) {
                        Class65.anIntArray1132[i_18_] = i_25_;
                        ItemDefinition.anIntArray1682[i_18_] = (JSocket.fileLoader5.lookupArchive((Class39_Sub5_Sub11.method708((new JString[]{ClientApplet.aClass3_4,
                                    AbstractImage.method1007((byte) 71, i_23_),
                                    Class32.aClass3_593,
                                    AbstractImage.method1007((byte) 71, i_24_)})))));
                        Class55.anIntArray1255[i_18_] = (JSocket.fileLoader5.lookupArchive((Class39_Sub5_Sub11.method708((new JString[]{ObjectDefinition.aClass3_1946,
                                    AbstractImage.method1007((byte) 71, i_23_),
                                    Class32.aClass3_593,
                                    AbstractImage.method1007((byte) 71, i_24_)})))));
                        i_18_++;
                    }
                }
            }
            OndemandRequest.method486(i_17_, i_21_, (byte) -113, i_16_,
                    i_15_, i_14_);
        } else {
            Class39_Sub5_Sub11.gameBuffer.initBitAccess();
            for (int i_26_ = 0; i_26_ < 4; i_26_++) {
                for (int i_27_ = 0; i_27_ < 13; i_27_++) {
                    for (int i_28_ = 0; i_28_ < 13; i_28_++) {
                        int i_29_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                        if (i_29_ == 1) {
                            Class39_Sub5_Sub6.anIntArrayArrayArray1755[i_26_][i_27_][i_28_] = Class39_Sub5_Sub11.gameBuffer.getBits(26);
                        } else {
                            Class39_Sub5_Sub6.anIntArrayArrayArray1755[i_26_][i_27_][i_28_] = -1;
                        }
                    }
                }
            }
            Class39_Sub5_Sub11.gameBuffer.finishBitAccess();
            int i_30_ = ((-Class39_Sub5_Sub11.gameBuffer.offset
                    + Huffmans.frameSize)
                    / 16);
            Class14.anIntArrayArray221 = new int[i_30_][4];
            for (int i_31_ = 0; i_30_ > i_31_; i_31_++) {
                for (int i_32_ = 0; i_32_ < 4; i_32_++) {
                    Class14.anIntArrayArray221[i_31_][i_32_] = Class39_Sub5_Sub11.gameBuffer.getDword();
                }
            }
            int i_33_ = Class39_Sub5_Sub11.gameBuffer.method833((byte) 122);
            int i_34_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_35_ = Class39_Sub5_Sub11.gameBuffer.method833((byte) 123);
            int i_36_ = Class39_Sub5_Sub11.gameBuffer.getUwordLe();
            int i_37_ = Class39_Sub5_Sub11.gameBuffer.method815((byte) 16);
            ItemDefinition.anIntArray1682 = new int[i_30_];
            Class65.anIntArray1132 = new int[i_30_];
            Cache.aByteArrayArray104 = new byte[i_30_][];
            Class55.anIntArray1255 = new int[i_30_];
            Class39_Sub5_Sub16.aByteArrayArray1989 = new byte[i_30_][];
            i_30_ = 0;
            for (int i_38_ = 0; i_38_ < 4; i_38_++) {
                for (int i_39_ = 0; i_39_ < 13; i_39_++) {
                    for (int i_40_ = 0; i_40_ < 13; i_40_++) {
                        int i_41_ = (Class39_Sub5_Sub6.anIntArrayArrayArray1755[i_38_][i_39_][i_40_]);
                        if (i_41_ != -1) {
                            int i_42_ = (i_41_ & 0x3ff9) >> 3;
                            int i_43_ = (i_41_ & 0xfff459) >> 14;
                            int i_44_ = i_42_ / 8 + (i_43_ / 8 << 8);
                            for (int i_45_ = 0; i_45_ < i_30_; i_45_++) {
                                if (Class65.anIntArray1132[i_45_] == i_44_) {
                                    i_44_ = -1;
                                    break;
                                }
                            }
                            if (i_44_ != -1) {
                                Class65.anIntArray1132[i_30_] = i_44_;
                                int i_46_ = i_44_ >> 8 & 0xff;
                                int i_47_ = i_44_ & 0xff;
                                ItemDefinition.anIntArray1682[i_30_] = (JSocket.fileLoader5.lookupArchive((Class39_Sub5_Sub11.method708((new JString[]{ClientApplet.aClass3_4,
                                            AbstractImage.method1007((byte) 71,
                                            i_46_),
                                            Class32.aClass3_593,
                                            AbstractImage.method1007((byte) 71,
                                            i_47_)})))));
                                Class55.anIntArray1255[i_30_] = (JSocket.fileLoader5.lookupArchive((Class39_Sub5_Sub11.method708((new JString[]{ObjectDefinition.aClass3_1946,
                                            AbstractImage.method1007((byte) 71,
                                            i_46_),
                                            Class32.aClass3_593,
                                            AbstractImage.method1007((byte) 71,
                                            i_47_)})))));
                                i_30_++;
                            }
                        }
                    }
                }
            }
            OndemandRequest.method486(i_37_, i_36_, (byte) -127, i_35_,
                    i_33_, i_34_);
        }
    }

    public static void method285(int i) {
        try {
            if (Buffer.anInt1353 == 0) {
                if (Class37.gameSocket != null) {
                    Class37.gameSocket.stop();
                    Class37.gameSocket = null;
                }
                Buffer.anInt1353 = 1;
                ObjectDefinition.aClass56_1963 = null;
                Widget.aBoolean2116 = false;
                Class15.anInt280 = 0;
            }
            if (Buffer.anInt1353 == 1) {
                if (ObjectDefinition.aClass56_1963 == null) {
                    ObjectDefinition.aClass56_1963 = Class39_Sub5_Sub9.signlink.requestSocket(HashTable.anInt363);
                }
                if (ObjectDefinition.aClass56_1963.returnCode == 2) {
                    throw new IOException();
                }
                if (ObjectDefinition.aClass56_1963.returnCode == 1) {
                    Class37.gameSocket = new JSocket((Socket) (ObjectDefinition.aClass56_1963.returnObject),
                            Class39_Sub5_Sub9.signlink);
                    Buffer.anInt1353 = 2;
                    ObjectDefinition.aClass56_1963 = null;
                }
            }
            if (Buffer.anInt1353 == 2) {
                long l = (Client.aLong1278 = Class39_Sub5_Sub14.aClass3_1897.encodeBase37());
                FrameBuffer.outgoingGameBuffer.offset = 0;
                FrameBuffer.outgoingGameBuffer.putByte(14);
                int i_48_ = (int) (l >> 16 & 0x1fL);
                FrameBuffer.outgoingGameBuffer.putByte(i_48_);
                Class37.gameSocket.write((FrameBuffer.outgoingGameBuffer.payload), 0, 2);
                Class39_Sub5_Sub11.gameBuffer.offset = 0;
                Buffer.anInt1353 = 3;
            }
            if (Buffer.anInt1353 == 3) {
                int i_49_ = Class37.gameSocket.read();
                if (i_49_ != 0) {
                    Class10.method180(i_49_, 24);
                    return;
                }
                Buffer.anInt1353 = 4;
                Class39_Sub5_Sub11.gameBuffer.offset = 0;
            }
            if (Buffer.anInt1353 == 4) {
                if (Class39_Sub5_Sub11.gameBuffer.offset < 8) {
                    int i_50_ = Class37.gameSocket.available();
                    if ((-Class39_Sub5_Sub11.gameBuffer.offset
                            + 8)
                            < i_50_) {
                        i_50_ = 8 - (Class39_Sub5_Sub11.gameBuffer.offset);
                    }
                    if (i_50_ > 0) {
                        Class37.gameSocket.read((Class39_Sub5_Sub11.gameBuffer.payload), (Class39_Sub5_Sub11.gameBuffer.offset), i_50_);
                        Class39_Sub5_Sub11.gameBuffer.offset += i_50_;
                    }
                }
                if (Class39_Sub5_Sub11.gameBuffer.offset
                        == 8) {
                    Class39_Sub5_Sub11.gameBuffer.offset = 0;
                    JKeyListener.aLong626 = Class39_Sub5_Sub11.gameBuffer.getQword();
                    Buffer.anInt1353 = 5;
                }
            }
            if (Buffer.anInt1353 == 5) {
                FrameBuffer.outgoingGameBuffer.offset = 0;
                int[] is = new int[4];
                is[0] = (int) (Math.random() * 9.9999999E7);
                is[1] = (int) (Math.random() * 9.9999999E7);
                is[3] = (int) JKeyListener.aLong626;
                is[2] = (int) (JKeyListener.aLong626 >> 32);
                FrameBuffer.outgoingGameBuffer.putByte(10);
                FrameBuffer.outgoingGameBuffer.putDword(is[0]);
                FrameBuffer.outgoingGameBuffer.putDword(is[1]);
                FrameBuffer.outgoingGameBuffer.putDword(is[2]);
                FrameBuffer.outgoingGameBuffer.putDword(is[3]);
                FrameBuffer.outgoingGameBuffer.putDword(Class39_Sub5_Sub9.signlink.clientUid);
                FrameBuffer.outgoingGameBuffer.putQword(Class39_Sub5_Sub14.aClass3_1897.encodeBase37());
                FrameBuffer.outgoingGameBuffer.putJstr((byte) 102, Class39_Sub5_Sub14.aClass3_1905);
                FrameBuffer.outgoingGameBuffer.applyRsa(Class2.aBigInteger47, JSocket.aBigInteger292, -17694);
                JString.aClass39_Sub6_Sub1_1236.offset = 0;
                if (Class31.state == 40) {
                    JString.aClass39_Sub6_Sub1_1236.putByte(18);
                } else {
                    JString.aClass39_Sub6_Sub1_1236.putByte(16);
                }
                JString.aClass39_Sub6_Sub1_1236.putByte(FrameBuffer.outgoingGameBuffer.offset + 61);
                JString.aClass39_Sub6_Sub1_1236.putDword(443);
                JString.aClass39_Sub6_Sub1_1236.putByte(!Class45.aBoolean867 ? 0 : 1);
                JString.aClass39_Sub6_Sub1_1236.putDword((Buffer.fileLoader0.localChecksum));
                JString.aClass39_Sub6_Sub1_1236.putDword(Class15.fileLoader1.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Class67.fileLoader2.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Node.fileLoader3.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Node.fileLoader4.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(JSocket.fileLoader5.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Projectile.fileLoader6.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Class45.fileLoader7.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(TraversalMap.fileLoader8.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword((Widget.fileLoader9.localChecksum));
                JString.aClass39_Sub6_Sub1_1236.putDword(Class66.fileLoader10.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Class36.fileLoader11.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Class33.fileLoader12.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putDword(Class55.fileLoader13.localChecksum);
                JString.aClass39_Sub6_Sub1_1236.putBytes(FrameBuffer.outgoingGameBuffer.payload,0,
                        FrameBuffer.outgoingGameBuffer.offset);
                Class37.gameSocket.write(JString.aClass39_Sub6_Sub1_1236.payload, 0, JString.aClass39_Sub6_Sub1_1236.offset);
                FrameBuffer.outgoingGameBuffer.initIsaacCipher(is);
                for (int i_51_ = 0; i_51_ < 4; i_51_++) {
                    is[i_51_] += 50;
                }
                Class39_Sub5_Sub11.gameBuffer.initIsaacCipher(is);
                Buffer.anInt1353 = 6;
            }
            if (Buffer.anInt1353 == 6
                    && Class37.gameSocket.available() > 0) {
                int i_52_ = Class37.gameSocket.read();
                if (i_52_ != 21 || Class31.state != 20) {
                    if (i_52_ == 2) {
                        Buffer.anInt1353 = 9;
                    } else {
                        if (i_52_ == 15 && Class31.state == 40) {
                            JImage.method1017((byte) 74);
                            return;
                        }
                        if (i_52_ == 23 && ArchiveRequest.anInt1413 < 1) {
                            ArchiveRequest.anInt1413++;
                            Buffer.anInt1353 = 0;
                        } else {
                            Class10.method180(i_52_, 24);
                            return;
                        }
                    }
                } else {
                    Buffer.anInt1353 = 7;
                }
            }
            if (Buffer.anInt1353 == 7
                    && Class37.gameSocket.available() > 0) {
                Varbit.anInt1790 = Class37.gameSocket.read() * 60 + 180;
                Buffer.anInt1353 = 8;
            }
            if (Buffer.anInt1353 == 8) {
                Class15.anInt280 = 0;
                Class37.method349(8845, Class53.aClass3_967,
                        (Class39_Sub5_Sub11.method708((new JString[]{AbstractImage.method1007((byte) 71,
                            (Varbit.anInt1790) / 60),
                            TraversalMap.aClass3_519}))),
                        HashTable.aClass3_374);
                if (--Varbit.anInt1790 <= 0) {
                    Buffer.anInt1353 = 0;
                }
            } else {
                if ((Buffer.anInt1353 ^ 0xffffffff) == i
                        && Class37.gameSocket.available() >= 8) {
                    CacheIO.anInt97 = Class37.gameSocket.read();
                    Huffmans.aBoolean764 = Class37.gameSocket.read() == 1;
                    Class39_Sub13.anInt1501 = Class37.gameSocket.read();
                    Class39_Sub13.anInt1501 <<= 8;
                    Class39_Sub13.anInt1501 += Class37.gameSocket.read();
                    Class63.anInt1126 = Class37.gameSocket.read();
                    Class37.gameSocket.read((Class39_Sub5_Sub11.gameBuffer.payload),
                            0, 1);
                    Class39_Sub5_Sub11.gameBuffer.offset = 0;
                    Class4.frameId = Class39_Sub5_Sub11.gameBuffer.getFrame();
                    Class37.gameSocket.read((Class39_Sub5_Sub11.gameBuffer.payload),
                            0, 2);
                    Class39_Sub5_Sub11.gameBuffer.offset = 0;
                    Huffmans.frameSize = Class39_Sub5_Sub11.gameBuffer.getUword();
                    Buffer.anInt1353 = 10;
                }
                if (Buffer.anInt1353 == 10) {
                    if (Class37.gameSocket.available()
                            >= Huffmans.frameSize) {
                        Class39_Sub5_Sub11.gameBuffer.offset = 0;
                        Class37.gameSocket.read((Class39_Sub5_Sub11.gameBuffer.payload),
                                0, Huffmans.frameSize);
                        Class39_Sub5_Sub4_Sub4.method511((byte) 95);
                        Class62_Sub2.anInt1597 = -1;
                        method284(-519790717, false);
                        Class4.frameId = -1;
                    }
                } else {
                    Class15.anInt280++;
                    if (Class15.anInt280 > 2000) {
                        if (ArchiveRequest.anInt1413 < 1) {
                            Buffer.anInt1353 = 0;
                            if (Class39_Sub5_Sub4.anInt1732
                                    == HashTable.anInt363) {
                                HashTable.anInt363 = Bzip2Block.anInt1078;
                            } else {
                                HashTable.anInt363 = Class39_Sub5_Sub4.anInt1732;
                            }
                            ArchiveRequest.anInt1413++;
                        } else {
                            Class10.method180(-3, 24);
                        }
                    }
                }
            }
        } catch (IOException ioexception) {
            if (ArchiveRequest.anInt1413 < 1) {
                ArchiveRequest.anInt1413++;
                Buffer.anInt1353 = 0;
                if (Class39_Sub5_Sub4.anInt1732 == HashTable.anInt363) {
                    HashTable.anInt363 = Bzip2Block.anInt1078;
                } else {
                    HashTable.anInt363 = Class39_Sub5_Sub4.anInt1732;
                }
            } else {
                Class10.method180(-2, i ^ ~0x11);
            }
        }
    }

    public static void method286(int i) {
        aClass3_460 = null;
        aClass3_464 = null;
        aClass3_470 = null;
        aClass3_462 = null;
        if (i != -7753) {
            anInt459 = 57;
        }
        huffmans = null;
        tableFile = null;
        aClass3_468 = null;
        priorityRequestsQueue = null;
        aClass3_457 = null;
        aClass3_469 = null;
        aClass3_458 = null;
        aClass3_466 = null;
        aClass3_463 = null;
    }

    public static Varbit getVarbit(int id) {
        Varbit varbit = ((Varbit) Widget.varbitCache.get((long) id));
        if (varbit != null) {
            return varbit;
        }
        byte[] is = Queue.aClass9_971.lookupFile(14, id);
        varbit = new Varbit();
        if (is != null) {
            varbit.decode(new Buffer(is));
        }
        Widget.varbitCache.put(varbit, (long) id, (byte) -102);
        return varbit;
    }

    public static void method288(byte i) {
        Class39_Sub5_Sub7.aClass57_1778.method1006(10);
        Class45.aClass39_Sub5_Sub10_Sub4_866.method695(0, 0);
        Widget.anIntArray2006 = Class39_Sub5_Sub10_Sub2.method657(Widget.anIntArray2006);
    }

    static {
        aClass3_460 = aClass3_463;
        anApplet_Sub1_465 = null;
        aClass3_464 = Class39_Sub5_Sub9.createJstring("Too many connections from your address)3");
        aClass3_458 = aClass3_464;
        aClass3_462 = aClass3_457;
        aClass3_466 = Class39_Sub5_Sub9.createJstring(": ");
        priorityRequestsQueue = new HashTable(4096);
        aClass3_468 = Class39_Sub5_Sub9.createJstring("Enter your username (V password)3");
        anInt471 = 0;
        aClass3_470 = aClass3_468;
        aClass3_469 = Class39_Sub5_Sub9.createJstring(" weitere Optionen");
    }
}
