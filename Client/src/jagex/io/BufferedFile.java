package jagex.io;

/* Class18 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.io.FileOnDisk;
import jagex.graphics.AbstractImage;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.world.actors.StillGraphic;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.io.FrameBuffer;
import jagex.utils.ArrayUtils;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import unpackaged.ArchiveWorker;
import unpackaged.CacheIO;
import unpackaged.Class15;
import unpackaged.Class2;
import unpackaged.Class23;
import unpackaged.Class26;
import unpackaged.Class31;
import unpackaged.Class33;
import unpackaged.Class34;
import unpackaged.Class37;
import unpackaged.Class39_Sub11;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub14;
import unpackaged.Class39_Sub5_Sub16;
import unpackaged.Class39_Sub5_Sub4;
import unpackaged.Class39_Sub5_Sub6;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class39_Sub7;
import unpackaged.Class4;
import unpackaged.Class44;
import unpackaged.Class53;
import unpackaged.Client;
import unpackaged.FileTable;
import unpackaged.ItemDefinition;
import unpackaged.NameTable;

public class BufferedFile {

    public byte[] readBuffer;
    public long aLong337;
    public static int[] anIntArray338;
    public long aLong339;
    public long aLong340 = -1L;
    public static int anInt341;
    public long aLong342;
    public int anInt343;
    public static int worldId = 1;
    public static int[] anIntArray345;
    public static AbstractImage aClass57_346;
    public long aLong347;
    public static JString aClass3_348 = Class39_Sub5_Sub9.createJstring("backright1");
    public long aLong349 = -1L;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_350;
    public static Font aFont351;
    public FileOnDisk fileOnDisk;
    public int anInt353 = 0;
    public byte[] writeBuffer;

    public void method224(int i, long l) {
        if (l >= 0L) {
            aLong339 = l;
        }
    }

    public static JString[] method225(byte i, JString[] class3s) {
        JString[] class3s_0_ = new JString[5];
        for (int i_1_ = 0; i_1_ < 5; i_1_++) {
            class3s_0_[i_1_] = Class39_Sub5_Sub11.method708((new JString[]{AbstractImage.method1007((byte) 71,
                        i_1_),
                        Class31.aClass3_556}));
            if (class3s != null && class3s[i_1_] != null) {
                class3s_0_[i_1_] = Class39_Sub5_Sub11.method708((new JString[]{class3s_0_[i_1_],
                            class3s[i_1_]}));
            }
        }
        return class3s_0_;
    }

    public void method226(byte i) throws IOException {
        method231(-127);
        fileOnDisk.destroy();
    }

    public void method227(byte[] is, int i, int i_2_, int i_3_)
            throws IOException {
        try {
            if (i_3_ + i > is.length) {
                throw new ArrayIndexOutOfBoundsException(-is.length + i
                        + i_3_);
            }
            if (aLong340 != -1L && aLong339 >= aLong340
                    && aLong339 + (long) i <= (long) anInt353 + aLong340) {
                ArrayUtils.arrayCopy(writeBuffer, (int) (aLong339 - aLong340),
                        is, i_3_, i);
                aLong339 += (long) i;
                return;
            }
            long l = aLong339;
            int i_4_ = i_3_;
            int i_5_ = i;
            if (aLong339 >= aLong349
                    && aLong339 < (long) anInt343 + aLong349) {
                int i_6_ = (int) (-aLong339 + (aLong349 + (long) anInt343));
                if (i < i_6_) {
                    i_6_ = i;
                }
                i -= i_6_;
                ArrayUtils.arrayCopy(readBuffer, (int) (aLong339 + -aLong349),
                        is, i_3_, i_6_);
                aLong339 += (long) i_6_;
                i_3_ += i_6_;
            }
            if (readBuffer.length >= i) {
                if (i > 0) {
                    method229(-21473);
                    int i_7_ = i;
                    if (anInt343 < i_7_) {
                        i_7_ = anInt343;
                    }
                    i -= i_7_;
                    ArrayUtils.arrayCopy(readBuffer, 0, is, i_3_, i_7_);
                    i_3_ += i_7_;
                    aLong339 += (long) i_7_;
                }
            } else {
                fileOnDisk.seek(aLong339);
                aLong337 = aLong339;
                while (i > 0) {
                    int i_8_ = fileOnDisk.read(is, i_3_, i);
                    if (i_8_ == -1) {
                        break;
                    }
                    i -= i_8_;
                    i_3_ += i_8_;
                    aLong337 += (long) i_8_;
                    aLong339 += (long) i_8_;
                }
            }
            if (aLong340 != -1L) {
                if (aLong340 > aLong339 && i > 0) {
                    int i_9_ = i_3_ + (int) (-aLong339 + aLong340);
                    if (i + i_3_ < i_9_) {
                        i_9_ = i + i_3_;
                    }
                    while (i_3_ < i_9_) {
                        i--;
                        is[i_3_++] = (byte) 0;
                        aLong339++;
                    }
                }
                long l_10_ = -1L;
                if (l >= aLong340 - -(long) anInt353
                        || aLong340 - -(long) anInt353 > l + (long) i_5_) {
                    if (aLong340 < (long) i_5_ + l
                            && (long) i_5_ + l <= (long) anInt353 + aLong340) {
                        l_10_ = l + (long) i_5_;
                    }
                } else {
                    l_10_ = (long) anInt353 + aLong340;
                }
                long l_11_ = -1L;
                if (aLong340 >= l && aLong340 < (long) i_5_ + l) {
                    l_11_ = aLong340;
                } else if (l >= aLong340 && aLong340 - -(long) anInt353 > l) {
                    l_11_ = l;
                }
                if (l_11_ > -1L && l_11_ < l_10_) {
                    int i_12_ = (int) (-l_11_ + l_10_);
                    ArrayUtils.arrayCopy(writeBuffer, (int) (-aLong340 + l_11_),
                            is, i_4_ + (int) (l_11_ + -l), i_12_);
                    if (aLong339 < l_10_) {
                        i -= l_10_ + -aLong339;
                        aLong339 = l_10_;
                    }
                }
            }
        } catch (IOException ioexception) {
            aLong337 = -1L;
            throw ioexception;
        }
        if (i > 0) {
            throw new EOFException();
        }
    }

    public long method228(int i) {
        return aLong347;
    }

    public void method229(int i) throws IOException {
        anInt343 = 0;
        if (aLong339 != aLong337) {
            fileOnDisk.seek(aLong339);
            aLong337 = aLong339;
        }
        if (i != -21473) {
            readBuffer = null;
        }
        aLong349 = aLong339;
        int i_13_;
        for (/**/; anInt343 < readBuffer.length; anInt343 += i_13_) {
            i_13_ = fileOnDisk.read(readBuffer,
                    anInt343, readBuffer.length - anInt343);
            if (i_13_ == -1) {
                break;
            }
            aLong337 += (long) i_13_;
        }
    }

    public static int method230(boolean bool, FileTable class9,
            FileTable class9_14_) {
        int i = 0;
        if (class9.method152(22411, Class39_Sub5_Sub14.aClass3_1906,
                Class4.aClass3_69)) {
            i++;
        }
        if (class9_14_.method152(22411, Class39_Sub5_Sub14.aClass3_1906,
                Class39_Sub11.aClass3_1481)) {
            i++;
        }
        if (class9_14_.method152(22411, Class39_Sub5_Sub14.aClass3_1906,
                Class23.aClass3_422)) {
            i++;
        }
        if (class9_14_.method152(22411, Class39_Sub5_Sub14.aClass3_1906,
                StillGraphic.aClass3_2339)) {
            i++;
        }
        if (class9_14_.method152(22411, Class39_Sub5_Sub14.aClass3_1906,
                JSocket.aClass3_310)) {
            i++;
        }
        return i;
    }

    public void method231(int i) throws IOException {
        if (aLong340 != -1L) {
            if (aLong340 != aLong337) {
                fileOnDisk.seek(aLong340);
                aLong337 = aLong340;
            }
            long l = -1L;
            fileOnDisk.write(writeBuffer, 0, anInt353);
            if (aLong349 > aLong340 || aLong340 >= (long) anInt343 + aLong349) {
                if (aLong349 >= aLong340 && aLong349 < aLong340 + (long) anInt353) {
                    l = aLong349;
                }
            } else {
                l = aLong340;
            }
            aLong337 += (long) anInt353;
            long l_16_ = -1L;
            if (aLong337 > aLong342) {
                aLong342 = aLong337;
            }
            if (aLong349 >= aLong340 - -(long) anInt353
                    || aLong349 - -(long) anInt343 < (long) anInt353 + aLong340) {
                if (aLong349 - -(long) anInt343 > aLong340
                        && ((long) anInt343 + aLong349
                        <= (long) anInt353 + aLong340)) {
                    l_16_ = (long) anInt343 + aLong349;
                }
            } else {
                l_16_ = aLong340 + (long) anInt353;
            }
            if (l > -1L && l < l_16_) {
                int length = (int) (l_16_ + -l);
                ArrayUtils.arrayCopy(writeBuffer, (int) (l - aLong340), readBuffer, (int) (l - aLong349), length);
            }
            anInt353 = 0;
            aLong340 = -1L;
        }
    }

    public static void method232(int i) {
        anIntArray338 = null;
        anIntArray345 = null;
        aFont351 = null;
        aClass39_Sub5_Sub10_Sub4_350 = null;
        aClass57_346 = null;
        aClass3_348 = null;
    }

    public static void method233(int i, int i_18_,
            FrameBuffer class39_sub6_sub1) {
        if (i == -14) {
            for (;;) {
                Class39_Sub7 class39_sub7 = (Class39_Sub7) Class15.aClass49_283.getFirst();
                if (class39_sub7 == null) {
                    break;
                }
                boolean bool = false;
                for (int i_19_ = 0; class39_sub7.anInt1382 > i_19_; i_19_++) {
                    if (class39_sub7.aClass56Array1377[i_19_] != null) {
                        if (class39_sub7.aClass56Array1377[i_19_].returnCode
                                == 2) {
                            class39_sub7.anIntArray1376[i_19_] = -5;
                        }
                        if (class39_sub7.aClass56Array1377[i_19_].returnCode
                                == 0) {
                            bool = true;
                        }
                    }
                    if (class39_sub7.aClass56Array1383[i_19_] != null) {
                        if (class39_sub7.aClass56Array1383[i_19_].returnCode
                                == 2) {
                            class39_sub7.anIntArray1376[i_19_] = -6;
                        }
                        if (class39_sub7.aClass56Array1383[i_19_].returnCode
                                == 0) {
                            bool = true;
                        }
                    }
                }
                if (bool) {
                    break;
                }
                class39_sub6_sub1.putFrame(i_18_);
                class39_sub6_sub1.putByte(0);
                int i_20_ = class39_sub6_sub1.offset;
                class39_sub6_sub1.putDword(class39_sub7.anInt1372);
                for (int i_21_ = 0; i_21_ < class39_sub7.anInt1382; i_21_++) {
                    if (class39_sub7.anIntArray1376[i_21_] != 0) {
                        class39_sub6_sub1.putByte((class39_sub7.anIntArray1376[i_21_]));
                    } else {
                        try {
                            int i_22_ = class39_sub7.anIntArray1378[i_21_];
                            if (i_22_ != 0) {
                                if (i_22_ == 1) {
                                    Field field = ((Field) (class39_sub7.aClass56Array1377[i_21_].returnObject));
                                    field.setInt(null,
                                            (class39_sub7.anIntArray1379[i_21_]));
                                    class39_sub6_sub1.putByte(0);
                                } else if (i_22_ == 2) {
                                    Field field = ((Field) (class39_sub7.aClass56Array1377[i_21_].returnObject));
                                    int i_23_ = field.getModifiers();
                                    class39_sub6_sub1.putByte(0);
                                    class39_sub6_sub1.putDword(i_23_);
                                }
                            } else {
                                Field field = (Field) (class39_sub7.aClass56Array1377[i_21_].returnObject);
                                int i_24_ = field.getInt(null);
                                class39_sub6_sub1.putByte(0);
                                class39_sub6_sub1.putDword(i_24_);
                            }
                            if (i_22_ == 3) {
                                Method method = (Method) (class39_sub7.aClass56Array1383[i_21_].returnObject);
                                byte[][] is = (class39_sub7.aByteArrayArrayArray1381[i_21_]);
                                Object[] objects = new Object[is.length];
                                for (int i_25_ = 0; i_25_ < is.length;
                                        i_25_++) {
                                    ObjectInputStream objectinputstream = (new ObjectInputStream(new ByteArrayInputStream(is[i_25_])));
                                    objects[i_25_] = objectinputstream.readObject();
                                }
                                Object object = method.invoke(null, objects);
                                if (object == null) {
                                    class39_sub6_sub1.putByte(0);
                                } else if (!(object instanceof Number)) {
                                    if (!(object instanceof JString)) {
                                        class39_sub6_sub1.putByte(4);
                                    } else {
                                        class39_sub6_sub1.putByte(2);
                                        class39_sub6_sub1.putJstr((byte) 72,
                                                ((JString) object));
                                    }
                                } else {
                                    class39_sub6_sub1.putByte(1);
                                    class39_sub6_sub1.putQword(((Number) object).longValue());
                                }
                            } else if (i_22_ == 4) {
                                Method method = (Method) (class39_sub7.aClass56Array1383[i_21_].returnObject);
                                int i_26_ = method.getModifiers();
                                class39_sub6_sub1.putByte(0);
                                class39_sub6_sub1.putDword(i_26_);
                            }
                        } catch (ClassNotFoundException classnotfoundexception) {
                            class39_sub6_sub1.putByte(-10);
                        } catch (java.io.InvalidClassException invalidclassexception) {
                            class39_sub6_sub1.putByte(-11);
                        } catch (java.io.StreamCorruptedException streamcorruptedexception) {
                            class39_sub6_sub1.putByte(-12);
                        } catch (java.io.OptionalDataException optionaldataexception) {
                            class39_sub6_sub1.putByte(-13);
                        } catch (IllegalAccessException illegalaccessexception) {
                            class39_sub6_sub1.putByte(-14);
                        } catch (IllegalArgumentException illegalargumentexception) {
                            class39_sub6_sub1.putByte(-15);
                        } catch (java.lang.reflect.InvocationTargetException invocationtargetexception) {
                            class39_sub6_sub1.putByte(-16);
                        } catch (SecurityException securityexception) {
                            class39_sub6_sub1.putByte(-17);
                        } catch (IOException ioexception) {
                            class39_sub6_sub1.putByte(-18);
                        } catch (NullPointerException nullpointerexception) {
                            class39_sub6_sub1.putByte(-19);
                        } catch (Exception exception) {
                            class39_sub6_sub1.putByte(-20);
                        } catch (Throwable throwable) {
                            class39_sub6_sub1.putByte(-21);
                        }
                    }
                }
                class39_sub6_sub1.putPayloadChecksum(i_20_);
                class39_sub6_sub1.putByteLength(-i_20_ + class39_sub6_sub1.offset);
                class39_sub7.unlinkDeque();
            }
        }
    }

    public void write(byte[] src, int off, int len) throws IOException {
        try {
            if ((long) len + aLong339 > aLong347) {
                aLong347 = (long) len + aLong339;
            }
            if (aLong340 != -1L && (aLong340 > aLong339 || aLong340 + (long) anInt353 < aLong339)) {
                method231(-77);
            }
            if (aLong340 != -1L && ((long) writeBuffer.length + aLong340 < aLong339 + (long) len)) {
                int i_29_ = (int) ((long) writeBuffer.length + aLong340 - aLong339);
                len -= i_29_;
                ArrayUtils.arrayCopy(src, off, writeBuffer, (int) (aLong339 + -aLong340), i_29_);
                aLong339 += (long) i_29_;
                anInt353 = writeBuffer.length;
                method231(125);
                off += i_29_;
            }
            if (len > writeBuffer.length) {
                if (aLong337 != aLong339) {
                    fileOnDisk.seek(aLong339);
                    aLong337 = aLong339;
                }
                fileOnDisk.write(src, off, len);
                long l = -1L;
                if (aLong349 >= aLong339 + (long) len
                        || (long) anInt343 + aLong349 < aLong339 + (long) len) {
                    if (aLong339 < aLong349 - -(long) anInt343
                            && aLong349 - -(long) anInt343 <= (long) len + aLong339) {
                        l = (long) anInt343 + aLong349;
                    }
                } else {
                    l = (long) len + aLong339;
                }
                aLong337 += (long) len;
                if (aLong342 < aLong337) {
                    aLong342 = aLong337;
                }
                long l_30_ = -1L;
                if (aLong339 < aLong349
                        || aLong339 >= aLong349 + (long) anInt343) {
                    if (aLong349 >= aLong339 && aLong349 < (long) len + aLong339) {
                        l_30_ = aLong349;
                    }
                } else {
                    l_30_ = aLong339;
                }
                if (l_30_ > -1L && l_30_ < l) {
                    int i_31_ = (int) (l - l_30_);
                    ArrayUtils.arrayCopy(src, (int) ((long) off + (l_30_ - aLong339)), readBuffer, (int) (-aLong349 + l_30_), i_31_);
                }
                aLong339 += (long) len;
            } else if (len > 0) {
                if (aLong340 == -1L) {
                    aLong340 = aLong339;
                }
                ArrayUtils.arrayCopy(src, off, writeBuffer, (int) (-aLong340 + aLong339), len);
                aLong339 += (long) len;
                if ((long) anInt353 < -aLong340 + aLong339) {
                    anInt353 = (int) (aLong339 - aLong340);
                }
            }
        } catch (IOException ioexception) {
            aLong337 = -1L;
            throw ioexception;
        }
    }

    public static void method235(byte i) {
        if (Class2.anInt53 != 0) {
            int i_32_ = 0;
            if (Class39_Sub7.anInt1380 != 0) {
                i_32_ = 1;
            }
            for (int i_33_ = 0; i_33_ < 100; i_33_++) {
                if (Class2.aClass3Array52[i_33_] != null) {
                    int i_34_ = Client.anIntArray1268[i_33_];
                    JString class3 = Class39_Sub11.aClass3Array1462[i_33_];
                    if (class3 != null
                            && class3.method65(Class37.aClass3_661, false)) {
                        class3 = class3.method85(-58, 5);
                    }
                    if (class3 != null
                            && class3.method65(Class53.aClass3_959, false)) {
                        class3 = class3.method85(-58, 5);
                    }
                    if ((i_34_ == 3 || i_34_ == 7)
                            && (i_34_ == 7 || NameTable.anInt177 == 0
                            || (NameTable.anInt177 == 1
                            && JString.method60(21469, class3)))) {
                        int i_35_ = 329 - i_32_ * 13;
                        if (IsaacPrng.anInt1091 > 4
                                && Class33.anInt599 - 4 > i_35_ - 10
                                && Class33.anInt599 - 4 <= i_35_ + 3) {
                            int i_36_ = (Class39_Sub5_Sub14.p12fullFont.method637(Class39_Sub5_Sub11.method708((new JString[]{Class39_Sub5_Sub16.aClass3_1991,
                                        ArchiveWorker.aClass3_1204, class3,
                                        Class2.aClass3Array52[i_33_]})))) + 25;
                            if (i_36_ > 450) {
                                i_36_ = 450;
                            }
                            if (IsaacPrng.anInt1091 < 4 + i_36_) {
                                Class26.anInt482++;
                                Class39_Sub5_Sub6.anInt1759++;
                                if (CacheIO.anInt97 >= 1) {
                                    Class4.anInt60++;
                                    JString.method55(0, Class34.aClass3_608, 0,
                                            (Class39_Sub5_Sub11.method708((new JString[]{(Class39_Sub5_Sub4.aClass3_1728),
                                                class3}))),
                                            (byte) -128, 0, 2009);
                                }
                                JString.method55(0,
                                        ItemDefinition.aClass3_1683,
                                        0,
                                        (Class39_Sub5_Sub11.method708((new JString[]{(Class39_Sub5_Sub4.aClass3_1728),
                                            class3}))),
                                        (byte) -28, 0, 2021);
                                JString.method55(0, Class44.aClass3_835, 0,
                                        (Class39_Sub5_Sub11.method708((new JString[]{(Class39_Sub5_Sub4.aClass3_1728),
                                            class3}))),
                                        (byte) -114, 0, 2031);
                            }
                        }
                        if (++i_32_ >= 5) {
                            break;
                        }
                    }
                    if ((i_34_ == 5 || i_34_ == 6) && NameTable.anInt177 < 2
                            && ++i_32_ >= 5) {
                        break;
                    }
                }
            }
        }
    }

    public static int method236(boolean bool) {
        return 19;
    }

    public BufferedFile(FileOnDisk class8, int readBufferSize, int writeBufferSize) throws IOException {
        fileOnDisk = class8;
        aLong347 = aLong342 = class8.getLength();
        aLong339 = 0L;
        readBuffer = new byte[readBufferSize];
        writeBuffer = new byte[writeBufferSize];
    }

    static {
        anInt341 = 0;
    }
}
