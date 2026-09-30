package unpackaged;

import jagex.utils.Huffmans;
import jagex.graphics.JImage;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.world.actors.Projectile;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.io.Buffer;
import jagex.utils.ArrayUtils;

/* Class9 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public abstract class FileTable {

    public int amountEntries;
    public NameTable aClass11_130;
    public static JString aClass3_131;
    public boolean removeArchive;
    public static int anInt133;
    public int[] amountChildren;
    public static JString aClass3_135 = Class39_Sub5_Sub9.createJstring("Loaded sprites");
    public NameTable[] childNameTables;
    public byte[][] archiveBuffers;
    public byte[][][] childBuffers;
    public int[][] childrenEntries;
    public static JString aClass3_140;
    public static DirectColorSprite aClass39_Sub5_Sub10_Sub3_141;
    public boolean removeChild;
    public int localChecksum;
    public int[] versions;
    public int[] checksums;
    public int[][] childNameHashes;
    public int[] entries;
    public static JString aClass3_148;
    public static int anInt149;
    public int[] archiveNameHashes;

    public int lookupChild(int id, JString childName) {
        childName = childName.method77();
        return childNameTables[id].method184(true, childName.getHashCode());
    }

    public void method144(JString archiveName, byte i) {
        archiveName = archiveName.method77();
        int i_1_ = aClass11_130.method184(true, archiveName.getHashCode());
    }

    public void method145(int i) {
        for (int i_2_ = i; i_2_ < childBuffers.length; i_2_++) {
            if (childBuffers[i_2_] != null) {
                for (int i_3_ = 0; childBuffers[i_2_].length > i_3_; i_3_++) {
                    childBuffers[i_2_][i_3_] = null;
                }
            }
        }
    }

    public int[] getChildrenEntries(int archiveId) {
        return childrenEntries[archiveId];
    }

    public void method147(int i) {
        
    }

    public int amountArchives() {
        return childBuffers.length;
    }

    public void method149(int id) {
    }

    public static void method150(int i) {
        aClass3_135 = null;
        aClass3_148 = null;
        aClass39_Sub5_Sub10_Sub3_141 = null;
        aClass3_131 = null;
        aClass3_140 = null;
    }

    public byte[] fetchArchive(int id) {
        if (childBuffers.length == 1) {
            return lookupFile(0, id);
        }
        if (childBuffers[id].length == 1) {
            return lookupFile(id, 0);
        }
        throw new RuntimeException();
    }

    public boolean method152(int i, JString class3, JString class3_7_) {
        class3_7_ = class3_7_.method77();
        class3 = class3.method77();
        int i_8_ = aClass11_130.method184(true, class3_7_.getHashCode());
        int i_9_ = childNameTables[i_8_].method184(true,
                class3.getHashCode());
        return isArchiveLoaded(i_8_, i_9_);
    }

    public void removeChildBuffers(int archiveId) {
        for (int i = 0; i < childBuffers[archiveId].length; i++) {
            childBuffers[archiveId][i] = null;
        }
    }

    public byte[] lookupFile(int archiveId, int childId) {
        if (archiveId < 0 || childBuffers.length <= archiveId || childBuffers[archiveId] == null || childId < 0 || childId >= childBuffers[archiveId].length) {
            return null;
        }
        if (childBuffers[archiveId][childId] == null) {
            boolean bool = unpackArchive(archiveId, null);
            if (!bool) {
                method149(archiveId);
                bool = unpackArchive(archiveId, null);
                if (!bool) {
                    return null;
                }
            }
        }
        byte[] is = childBuffers[archiveId][childId];
        return is;
    }

    public boolean isArchiveLoaded(int archiveId, int childId) {
        if (archiveId < 0 || childBuffers.length <= archiveId
                || childBuffers[archiveId] == null || childId < 0
                || childBuffers[archiveId].length <= childId) {
            return false;
        }
        if (childBuffers[archiveId][childId] != null) {
            return true;
        }
        if (archiveBuffers[archiveId] != null) {
            return true;
        }
        method149(archiveId);
        if (archiveBuffers[archiveId] != null) {
            return true;
        }
        return false;
    }

    public static void method156(byte i) {
        Class1.method50(-120);
        if (Class31.state == 10) {
            int i_16_ = Class46.anInt887;
            int i_17_ = Class39_Sub4.anInt1329 - 202;
            int i_18_ = Bzip2Block.anInt1054 - 171;
            if (Class10.anInt155 == 0) {
                int i_19_ = 120;
                int i_20_ = 100;
                if (i_16_ == 1 && i_17_ >= i_20_ - 75 && i_17_ <= i_20_ + 75
                        && i_19_ - 20 <= i_18_ && i_19_ + 20 >= i_18_) {
                    Class43.anInt815 = 0;
                    Class10.anInt155 = 3;
                }
                i_20_ = 260;
                if (i_16_ == 1 && i_20_ - 75 <= i_17_ && i_20_ + 75 >= i_17_
                        && i_18_ >= i_19_ - 20 && i_18_ <= i_19_ + 20) {
                    Class10.anInt155 = 2;
                    Class39_Sub5_Sub14.aClass3_1899 = Class14.aClass3_241;
                    Class39_Sub5_Sub14.aClass3_1916 = Class14.aClass3_235;
                    Class39_Sub5_Sub14.aClass3_1896 = Class25.aClass3_470;
                    Class43.anInt815 = 0;
                }
            } else if (Class10.anInt155 != 2) {
                if (Class10.anInt155 == 3) {
                    int i_21_ = 180;
                    int i_22_ = 150;
                    if (i_16_ == 1 && i_17_ >= i_21_ - 75
                            && i_21_ + 75 >= i_17_ && i_18_ >= i_22_ - 20
                            && i_22_ + 20 >= i_18_) {
                        Class10.anInt155 = 0;
                    }
                }
            } else {
                int i_23_ = 150;
                int i_24_ = 100;
                int i_25_ = 60;
                i_25_ += 30;
                if (i_16_ == 1 && i_18_ >= i_25_ - 15 && i_18_ < i_25_) {
                    Class43.anInt815 = 0;
                }
                i_25_ += 15;
                if (i_16_ == 1 && i_18_ >= i_25_ - 15 && i_25_ > i_18_) {
                    Class43.anInt815 = 1;
                }
                i_25_ += 15;
                if (i_16_ == 1 && i_17_ >= i_24_ - 75 && i_24_ + 75 >= i_17_
                        && i_23_ - 20 <= i_18_ && i_18_ <= i_23_ + 20) {
                    Class39_Sub5_Sub14.aClass3_1897 = Class39_Sub5_Sub14.aClass3_1897.method81(-32769).formatUsername();
                    Class37.method349(8845, Class39_Sub4.aClass3_1334,
                            Class14.aClass3_267,
                            Class14.aClass3_238);
                    Class39_Sub14.setState(20);
                } else {
                    i_24_ = 260;
                    if (i_16_ == 1 && i_24_ - 75 <= i_17_
                            && i_17_ <= i_24_ + 75 && i_23_ - 20 <= i_18_
                            && i_18_ <= i_23_ + 20) {
                        Class10.anInt155 = 0;
                        Class39_Sub5_Sub14.aClass3_1905 = Class39_Sub5_Sub14.aClass3_1906;
                        Class39_Sub5_Sub14.aClass3_1897 = Class39_Sub5_Sub14.aClass3_1906;
                    }
                    while (Class39_Sub5_Sub7.method588(-4)) {
                        boolean bool = false;
                        for (int i_26_ = 0;
                                i_26_ < Class31.aClass3_565.getLength();
                                i_26_++) {
                            if (Projectile.anInt2191
                                    == Class31.aClass3_565.charAt(i_26_)) {
                                bool = true;
                                break;
                            }
                        }
                        if (Class43.anInt815 != 0) {
                            if (Class43.anInt815 == 1) {
                                if (Class15.anInt287 == 85
                                        && Class39_Sub5_Sub14.aClass3_1905.getLength() > 0) {
                                    Class39_Sub5_Sub14.aClass3_1905 = (Class39_Sub5_Sub14.aClass3_1905.method59(0, -1,
                                            Class39_Sub5_Sub14.aClass3_1905.getLength() - 1));
                                }
                                if (Class15.anInt287 == 84
                                        || Class15.anInt287 == 80) {
                                    Class43.anInt815 = 0;
                                }
                                if (bool && Class39_Sub5_Sub14.aClass3_1905.getLength() < 20) {
                                    Class39_Sub5_Sub14.aClass3_1905 = (Class39_Sub5_Sub14.aClass3_1905.createConcatChar(Projectile.anInt2191));
                                }
                            }
                        } else {
                            if (Class15.anInt287 == 85
                                    && Class39_Sub5_Sub14.aClass3_1897.getLength() > 0) {
                                Class39_Sub5_Sub14.aClass3_1897 = (Class39_Sub5_Sub14.aClass3_1897.method59(0, -1, Class39_Sub5_Sub14.aClass3_1897.getLength() - 1));
                            }
                            if (Class15.anInt287 == 84
                                    || Class15.anInt287 == 80) {
                                Class43.anInt815 = 1;
                            }
                            if (bool && Class39_Sub5_Sub14.aClass3_1897.getLength() < 12) {
                                Class39_Sub5_Sub14.aClass3_1897 = (Class39_Sub5_Sub14.aClass3_1897.createConcatChar(Projectile.anInt2191));
                            }
                        }
                    }
                }
            }
        }
    }

    public byte[] lookupFile(int archiveId, int childId, int[] cipherKeys) {
        if (archiveId < 0 || childBuffers.length <= archiveId || childBuffers[archiveId] == null || childId < 0 || childId >= childBuffers[archiveId].length) {
            return null;
        }
        if (childBuffers[archiveId][childId] == null) {
            boolean bool = unpackArchive(archiveId, cipherKeys);
            if (!bool) {
                method149(archiveId);
                bool = unpackArchive(archiveId, cipherKeys);
                if (!bool) {
                    return null;
                }
            }
        }
        byte[] archive = childBuffers[archiveId][childId];
        if (removeChild) {
            childBuffers[archiveId][childId] = null;
        }
        return archive;
    }

    public int getAmountChildren(int archiveId) {
        return childBuffers[archiveId].length;
    }

    public boolean getArchivesLoaded() {
        boolean archivesLoaded = true;
        for (int i = 0; entries.length > i; i++) {
            int archiveId = entries[i];
            if (archiveBuffers[archiveId] == null) {
                method149(archiveId);
                if (archiveBuffers[archiveId] == null) {
                    archivesLoaded = false;
                }
            }
        }
        return archivesLoaded;
    }

    public static byte[] method160(int i, int i_33_, int i_34_, FileTable class9,
            int i_35_) {
        long l = ((long) (i + i_34_ * 37 & 0xffff) + (long) (i_34_ << 16)
                + ((long) i_35_ << 32));
        if (Npc.aClass7_2493 != null) {
            Class39_Sub5_Sub7 class39_sub5_sub7 = ((Class39_Sub5_Sub7) Npc.aClass7_2493.get(l));
            if (class39_sub5_sub7 != null) {
                return class39_sub5_sub7.aByteArray1785;
            }
        }
        byte[] is = class9.lookupFile(i_34_, i);
        if (is == null) {
            return null;
        }
        if (Npc.aClass7_2493 != null) {
            Npc.aClass7_2493.put(new Class39_Sub5_Sub7(is), l, (byte) -116);
        }
        return is;
    }

    public boolean unpackArchive(int id, int[] keys) {
        if (archiveBuffers[id] == null) {
            return false;
        }
        int children = amountChildren[id];
        byte[][] buffers = childBuffers[id];
        boolean childrenLoaded = true;
        int[] childEntries = childrenEntries[id];
        for (int i = 0; i < children; i++) {
            if (buffers[childEntries[i]] == null) {
                childrenLoaded = false;
                break;
            }
        }
        if (childrenLoaded) {
            return true;
        }
        byte[] archiveBytes;
        if (keys == null || keys[0] == 0 && keys[1] == 0 && keys[2] == 0 && keys[3] == 0) {
            archiveBytes = archiveBuffers[id];
        } else {
            archiveBytes = new byte[archiveBuffers[id].length];
            ArrayUtils.arrayCopy(archiveBuffers[id], 0, archiveBytes, 0, archiveBytes.length);
            Buffer buffer = new Buffer(archiveBytes);
            buffer.decipherXtea(keys, 0, buffer.payload.length, 5);
        }
        byte[] src = Class14.unpackContainer(archiveBytes);
        if (removeArchive) {
            archiveBuffers[id] = null;
        }
        if (children <= 1) {
            buffers[childEntries[0]] = src;
        } else {
            int offset = src.length;
            int iterations = src[--offset] & 0xff;
            int[] offsets = new int[children];
            offset -= children * iterations * 4;
            Buffer buffer = new Buffer(src);
            buffer.offset = offset;
            for (int i = 0; iterations > i; i++) {
                int counter = 0;
                for (int i_48_ = 0; children > i_48_; i_48_++) {
                    counter += buffer.getDword();
                    offsets[i_48_] += counter;
                }
            }
            for (int i = 0; children > i; i++) {
                if (buffers[childEntries[i]] == null) {
                    buffers[childEntries[i]] = new byte[offsets[i]];
                }
                offsets[i] = 0;
            }
            int counter = 0;
            buffer.offset = offset;
            for (int i = 0; iterations > i; i++) {
                int length = 0;
                for (int j = 0; children > j; j++) {
                    length += buffer.getDword();
                    ArrayUtils.arrayCopy(src, counter, buffers[childEntries[j]], offsets[j], length);
                    offsets[j] += length;
                    counter += length;
                }
            }
        }
        return true;
    }

    public static void method162(byte i) {
        int i_54_ = Class39_Sub5_Sub11.gameBuffer.getBits(8);
        if (i_54_ < TraversalMap.anInt515) {
            for (int i_55_ = i_54_; i_55_ < TraversalMap.anInt515; i_55_++) {
                Class26.anIntArray496[Huffmans.anInt749++] = Class1.anIntArray40[i_55_];
            }
        }
        if (TraversalMap.anInt515 < i_54_) {
            throw new RuntimeException("gppov1");
        }
        TraversalMap.anInt515 = 0;
        for (int i_56_ = 0; i_56_ < i_54_; i_56_++) {
            int i_57_ = Class1.anIntArray40[i_56_];
            Player class39_sub5_sub4_sub4_sub2 = Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_57_];
            int i_58_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
            if (i_58_ == 0) {
                Class1.anIntArray40[TraversalMap.anInt515++] = i_57_;
                class39_sub5_sub4_sub4_sub2.anInt2290 = Class2.logicCycle;
            } else {
                int i_59_ = Class39_Sub5_Sub11.gameBuffer.getBits(2);
                if (i_59_ == 0) {
                    Class1.anIntArray40[TraversalMap.anInt515++] = i_57_;
                    class39_sub5_sub4_sub4_sub2.anInt2290 = Class2.logicCycle;
                    ArchiveRequest.anIntArray1400[JImage.anInt1586++] = i_57_;
                } else if (i_59_ == 1) {
                    Class1.anIntArray40[TraversalMap.anInt515++] = i_57_;
                    class39_sub5_sub4_sub4_sub2.anInt2290 = Class2.logicCycle;
                    int i_60_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                    class39_sub5_sub4_sub4_sub2.method518(-1, i_60_, false);
                    int i_61_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                    if (i_61_ == 1) {
                        ArchiveRequest.anIntArray1400[JImage.anInt1586++] = i_57_;
                    }
                } else if (i_59_ == 2) {
                    Class1.anIntArray40[TraversalMap.anInt515++] = i_57_;
                    class39_sub5_sub4_sub4_sub2.anInt2290 = Class2.logicCycle;
                    int i_62_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                    class39_sub5_sub4_sub4_sub2.method518(-1, i_62_, true);
                    int i_63_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                    class39_sub5_sub4_sub4_sub2.method518(-1, i_63_, true);
                    int i_64_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                    if (i_64_ == 1) {
                        ArchiveRequest.anIntArray1400[JImage.anInt1586++] = i_57_;
                    }
                } else if (i_59_ == 3) {
                    Class26.anIntArray496[Huffmans.anInt749++] = i_57_;
                }
            }
        }
    }

    public int lookupArchive(JString archiveName) {
        archiveName = archiveName.method77();
        return aClass11_130.method184(true, archiveName.getHashCode());
    }

    public boolean hasArchive(int id) {
        if (archiveBuffers[id] != null) {
            return true;
        }
        method149(id);
        if (archiveBuffers[id] != null) {
            return true;
        }
        return false;
    }

    public byte[] method166(int i, boolean bool) {
        if (childBuffers.length == 1) {
            return lookupFile(0, i);
        }
        if (childBuffers[i].length == 1) {
            return lookupFile(i, 0);
        }
        throw new RuntimeException();
    }

    public byte[] lookupFile(JString class3, JString class3_67_) {
        class3 = class3.method77();
        class3_67_ = class3_67_.method77();
        int archiveId = aClass11_130.method184(true, class3.getHashCode());
        int childId = childNameTables[archiveId].method184(true, class3_67_.getHashCode());
        return lookupFile(archiveId, childId);
    }

    public void decode(byte[] src) {
        localChecksum = Class13.calculateChecksum(src, src.length);
        Buffer buffer = new Buffer(Class14.unpackContainer(src));
        int version = buffer.getUbyte();
        if (version == 5) {
            int settingFlags = buffer.getUbyte();
            int counter = 0;
            int maximumEntry = -1;
            amountEntries = buffer.getUword();
            entries = new int[amountEntries];
            for (int i = 0; amountEntries > i; i++) {
                entries[i] = counter += buffer.getUword();
                if (entries[i] > maximumEntry) {
                    maximumEntry = entries[i];
                }
            }
            amountChildren = new int[maximumEntry + 1];
            childrenEntries = new int[maximumEntry + 1][];
            versions = new int[maximumEntry + 1];
            checksums = new int[maximumEntry + 1];
            childBuffers = new byte[maximumEntry + 1][][];
            archiveBuffers = new byte[maximumEntry + 1][];
            if (settingFlags != 0) {
                archiveNameHashes = new int[maximumEntry + 1];
                for (int i = 0; amountEntries > i; i++) {
                    archiveNameHashes[entries[i]] = buffer.getDword();
                }
                aClass11_130 = new NameTable(archiveNameHashes);
            }
            for (int i_76_ = 0; i_76_ < amountEntries; i_76_++) {
                checksums[entries[i_76_]] = buffer.getDword();
            }
            for (int i_77_ = 0; amountEntries > i_77_; i_77_++) {
                versions[entries[i_77_]] = buffer.getDword();
            }
            for (int i_78_ = 0; i_78_ < amountEntries; i_78_++) {
                amountChildren[entries[i_78_]] = buffer.getUword();
            }
            for (int i_79_ = 0; amountEntries > i_79_; i_79_++) {
                counter = 0;
                int fileId = entries[i_79_];
                int amtChildren = amountChildren[fileId];
                int maximumChild = -1;
                childrenEntries[fileId] = new int[amtChildren];
                for (int i_83_ = 0; amtChildren > i_83_; i_83_++) {
                    int i_84_ = (childrenEntries[fileId][i_83_] = counter += buffer.getUword());
                    if (maximumChild < i_84_) {
                        maximumChild = i_84_;
                    }
                }
                childBuffers[fileId] = new byte[maximumChild + 1][];
            }
            if (settingFlags != 0) {
                childNameHashes = new int[maximumEntry + 1][];
                childNameTables = new NameTable[maximumEntry + 1];
                for (int i_85_ = 0; amountEntries > i_85_; i_85_++) {
                    int i_86_ = entries[i_85_];
                    int i_87_ = amountChildren[i_86_];
                    childNameHashes[i_86_] = new int[childBuffers[i_86_].length];
                    for (int i_88_ = 0; i_88_ < i_87_; i_88_++) {
                        childNameHashes[i_86_][(childrenEntries[i_86_][i_88_])] = buffer.getDword();
                    }
                    childNameTables[i_86_] = new NameTable(childNameHashes[i_86_]);
                }
            }
        }
    }

    public FileTable(boolean bool, boolean bool_90_) {
        removeArchive = bool;
        removeChild = bool_90_;
    }

    static {
        aClass3_131 = Class39_Sub5_Sub9.createJstring("auf der Hautpseite)3");
        aClass3_140 = Class39_Sub5_Sub9.createJstring("Nehmen");
        aClass3_148 = aClass3_135;
        anInt133 = 0;
        anInt149 = 127;
    }
}
