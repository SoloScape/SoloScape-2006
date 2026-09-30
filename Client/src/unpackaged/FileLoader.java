package unpackaged;

import jagex.graphics.sprites.IndexedColorSprite;
import jagex.world.actors.Projectile;
import jagex.utils.JString;
import jagex.world.actors.Npc;
import jagex.utils.Deque;
import jagex.io.Buffer;

/* Class9_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class FileLoader extends FileTable {

    public static JString aClass3_1281;
    public CacheIO localCache;
    public static int anInt1283;
    public volatile boolean[] archivesLoaded;
    public static OndemandRequest currentOdRequest;
    public static JString aClass3_1286;
    public static int anInt1287 = -1;
    public boolean requestImmediately = false;
    public static JString aClass3_1289;
    public static int anInt1290;
    public static JString aClass3_1291;
    public static JString aClass3_1292;
    public static JString aClass3_1293;
    public CacheIO tableCache;
    public static int[] anIntArray1295;
    public static IndexedColorSprite[] aClass39_Sub5_Sub10_Sub4Array1296;
    public volatile boolean isUninitialized = false;
    public int indexId;
    public static JString aClass3_1299;
    public int anInt1300 = -1;
    public int givenChecksum;
    public static volatile int anInt1302;
    public static int anInt1303;

    public int method169(int i) {
        int totalPercentage = 0;
        int percentage = 0;
        for (int i_2_ = 0; archiveBuffers.length > i_2_; i_2_++) {
            if (amountChildren[i_2_] > 0) {
                percentage += method171((byte) -114, i_2_);
                totalPercentage += 100;
            }
        }
        if (totalPercentage == 0) {
            return 100;
        }
        int i_3_ = percentage * 100 / totalPercentage;
        return i_3_;
    }

    public void setChecksum(int checksum) {
        givenChecksum = checksum;
        if (tableCache == null) {
            Class39_Sub10.requestArchive(this, 255, indexId, true, givenChecksum, (byte) 0);
        } else {
            ArchiveWorker.fetchArchive(this, tableCache, indexId);
        }
    }

    @Override
    public void method147(int archiveId) {
        Projectile.requestArchive(indexId, archiveId);
    }

    public int method171(byte i, int i_5_) {
        if (i > -98) {
            return -95;
        }
        if (archiveBuffers[i_5_] != null) {
            return 100;
        }
        if (archivesLoaded[i_5_]) {
            return 100;
        }
        return Class30.method319(indexId, i_5_, -20500);
    }

    public static Class39_Sub5_Sub6 method172(int i, int i_6_) {
        Class39_Sub5_Sub6 class39_sub5_sub6 = ((Class39_Sub5_Sub6) Npc.aClass7_2490.get((long) i));
        if (i_6_ != 3) {
            return null;
        }
        if (class39_sub5_sub6 != null) {
            return class39_sub5_sub6;
        }
        byte[] is = Buffer.aClass9_1362.lookupFile(3, i);
        class39_sub5_sub6 = new Class39_Sub5_Sub6();
        if (is != null) {
            class39_sub5_sub6.method582((byte) -9, new Buffer(is));
        }
        Npc.aClass7_2490.put(class39_sub5_sub6, (long) i, (byte) 98);
        return class39_sub5_sub6;
    }

    @Override
    public void method149(int archiveId) {
        if (localCache != null && archivesLoaded != null && archivesLoaded[archiveId]) {
            ArchiveWorker.fetchArchive(this, localCache, archiveId);
        } else {
            Class39_Sub10.requestArchive(this, indexId, archiveId, true, checksums[archiveId], (byte) 2);
        }
    }

    public void validateArchive(byte[] src, int i, boolean isPriority, CacheIO cache) {
        if (tableCache == cache) {
            if (isUninitialized) {
                throw new RuntimeException();
            }
            if (src == null) {
                Class39_Sub10.requestArchive(this, 255, indexId, true, givenChecksum, (byte) 0);
            } else {
                Class23.crc.reset();
                Class23.crc.update(src, 0, src.length);
                int checksum = (int) Class23.crc.getValue();
                if (checksum != givenChecksum) {
                    Class39_Sub10.requestArchive(this, 255, indexId, true, givenChecksum, (byte) 0);
                } else {
                    this.decode(src);
                    initialize();
                }
            }
        } else {
            if (!isPriority && anInt1300 == i) {
                isUninitialized = true;
            }
            if (src == null || src.length <= 2) {
                archivesLoaded[i] = false;
                if (requestImmediately || isPriority) {
                    Class39_Sub10.requestArchive(this, indexId, i, isPriority, checksums[i], (byte) 2);
                }
            } else {
                Class23.crc.reset();
                Class23.crc.update(src, 0, src.length - 2);
                int crc = (int) Class23.crc.getValue();
                int version = ((src[src.length - 1] & 0xff) + ((src[src.length - 2] & 0xff) << 8));
                if (crc != checksums[i] || version != versions[i]) {
                    archivesLoaded[i] = false;
                    if (requestImmediately || isPriority) {
                        Class39_Sub10.requestArchive(this, indexId, i, isPriority, checksums[i], (byte) 2);
                    }
                } else {
                    archivesLoaded[i] = true;
                    if (isPriority) {
                        archiveBuffers[i] = src;
                    }
                }
            }
        }
    }

    public static void method174(int i) {
        Class15.aClass49_283 = new Deque();
    }

    public void method175(byte[] src, boolean cacheArchive, boolean isTableArchive, int archiveId) {
        if (isTableArchive) {
            if (isUninitialized) {
                throw new RuntimeException();
            }
            if (tableCache != null) {
                Class67.insertArchive(tableCache, indexId, src);
            }
            this.decode(src);
            initialize();
        } else {
            src[src.length - 2] = (byte) (versions[archiveId] >> 8);
            src[src.length - 1] = (byte) versions[archiveId];
            if (localCache != null) {
                Class67.insertArchive(localCache, archiveId, src);
                archivesLoaded[archiveId] = true;
            }
            if (cacheArchive) {
                archiveBuffers[archiveId] = src;
            }
        }
    }

    public FileLoader(CacheIO class6, CacheIO class6_14_, int i, boolean bool, boolean bool_15_, boolean bool_16_) {
        super(bool, bool_15_);
        localCache = class6;
        requestImmediately = bool_16_;
        tableCache = class6_14_;
        indexId = i;
        Class62_Sub2.getTableChecksum(this, indexId);
    }

    public static void method176(byte i) {
        aClass3_1299 = null;
        aClass3_1286 = null;
        aClass3_1281 = null;
        aClass3_1293 = null;
        aClass39_Sub5_Sub10_Sub4Array1296 = null;
        aClass3_1292 = null;
        aClass3_1291 = null;
        aClass3_1289 = null;
        anIntArray1295 = null;
        currentOdRequest = null;
    }

    public int method177(int i) {
        if (isUninitialized) {
            return 100;
        }
        if (archiveBuffers != null) {
            return 99;
        }
        int i_17_ = Class30.method319(255, indexId, -20500);
        if (i_17_ >= 100) {
            i_17_ = 99;
        }
        return i_17_;
    }

    public void initialize() {
        archivesLoaded = new boolean[archiveBuffers.length];
        for (int i = 0; archivesLoaded.length > i; i++) {
            archivesLoaded[i] = false;
        }
        if (localCache == null) {
            isUninitialized = true;
        } else {
            anInt1300 = -1;
            for (int i_19_ = 0; archivesLoaded.length > i_19_;
                    i_19_++) {
                if (amountChildren[i_19_] > 0) {
                    JKeyListener.fetchArchive(true, localCache, this, i_19_);
                    anInt1300 = i_19_;
                }
            }
            if (anInt1300 == -1) {
                isUninitialized = true;
            }

        }
    }

    static {
        aClass3_1281 = Class39_Sub5_Sub9.createJstring("Hidden");
        aClass3_1291 = Class39_Sub5_Sub9.createJstring("Your account has been disabled)3");
        aClass3_1289 = Class39_Sub5_Sub9.createJstring("leuchten2:");
        anIntArray1295 = new int[50];
        anInt1290 = 0;
        aClass3_1292 = Class39_Sub5_Sub9.createJstring("Ihr Spielkonto wurde deaktiviert)3");
        anInt1283 = 0;
        aClass3_1299 = aClass3_1281;
        aClass3_1286 = aClass3_1291;
        aClass3_1293 = Class39_Sub5_Sub9.createJstring(":tradereq:");
        anInt1302 = 0;
        anInt1303 = 0;
    }
}
