package jagex.world.actors;

import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.io.Buffer;
import jagex.utils.Cache;
import unpackaged.ArchiveRequest;
import unpackaged.Canvas_Sub1;
import unpackaged.Class25;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub18;
import unpackaged.Class39_Sub5_Sub4;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class62_Sub1;
import unpackaged.FileLoader;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.graphics.JImage;
import unpackaged.Model;
import unpackaged.NameTable;
import unpackaged.Widget;

/* Class39_Sub5_Sub4_Sub5 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class StillGraphic extends Class39_Sub5_Sub4 {

    public int anInt2320;
    public int anInt2321;
    public int anInt2322;
    public int anInt2323;
    public static Cache aClass7_2324 = new Cache(50);
    public Class39_Sub5_Sub11 aClass39_Sub5_Sub11_2325;
    public static int anInt2326;
    public static JString aClass3_2327;
    public static JString aClass3_2328;
    public static JString aClass3_2329;
    public static JString aClass3_2330 = Class39_Sub5_Sub9.createJstring(" more options");
    public static long aLong2331;
    public static JString aClass3_2332;
    public boolean aBoolean2333 = false;
    public static int anInt2334;
    public static JString aClass3_2335;
    public int anInt2336;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_2337;
    public static int anInt2338;
    public static JString aClass3_2339 = Class39_Sub5_Sub9.createJstring("titlebutton");
    public static JString aClass3_2340;
    public static JString aClass3_2341 = (Class39_Sub5_Sub9.createJstring("sind fehlgeschlagen)3 Bitte warten Sie 5 Minuten)1"));
    public static boolean aBoolean2342;
    public static JString aClass3_2343;
    public static JString aClass3_2344;
    public static JString aClass3_2345;
    public static JString aClass3_2346;
    public int anInt2347;
    public static JString aClass3_2348;
    public int anInt2349 = 0;
    public static JString aClass3_2350;
    public static JString aClass3_2351;
    public int anInt2352 = 0;
    public static JString aClass3_2353;

    public static void method530(int i, int i_0_) {
        if (i_0_ != -1 && Class39_Sub5_Sub4.widgetsLoaded[i_0_]
                && Class62_Sub1.widgets[i_0_] != null) {
            for (int i_1_ = 0;
                    i_1_ < (Class62_Sub1.widgets[i_0_]).length;
                    i_1_++) {
                Widget class39_sub5_sub17 = (Class62_Sub1.widgets[i_0_][i_1_]);
                if (class39_sub5_sub17 != null) {
                    class39_sub5_sub17.anInt2049 = class39_sub5_sub17.anInt1998;
                }
            }
        }
    }

    public static void method531(byte i) {
        aClass3_2330 = null;
        aClass3_2346 = null;
        aClass3_2341 = null;
        aClass3_2353 = null;
        aClass3_2328 = null;
        aClass3_2327 = null;
        aClass3_2329 = null;
        aClass3_2344 = null;
        aClass3_2351 = null;
        aClass3_2335 = null;
        aClass3_2343 = null;
        aClass39_Sub5_Sub10_Sub4_2337 = null;
        aClass3_2340 = null;
        aClass3_2348 = null;
        aClass3_2350 = null;
        aClass7_2324 = null;
        aClass3_2345 = null;
        aClass3_2339 = null;
        aClass3_2332 = null;
    }

    public void method532(int i, int i_2_) {
        if (!aBoolean2333) {
            anInt2349 += i;
            while (anInt2349
                    > aClass39_Sub5_Sub11_2325.anIntArray1831[anInt2352]) {
                anInt2349 -= aClass39_Sub5_Sub11_2325.anIntArray1831[anInt2352];
                anInt2352++;
                if (anInt2352
                        >= aClass39_Sub5_Sub11_2325.anIntArray1833.length) {
                    aBoolean2333 = true;
                    break;
                }
            }
        }
    }

    public static void method533(int i) {
        Class39_Sub5_Sub11.gameBuffer.initBitAccess();
        int i_3_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
        if (i_3_ != 0) {
            if (i != 0) {
                decodeHuffmans(null, 48);
            }
            int i_4_ = Class39_Sub5_Sub11.gameBuffer.getBits(2);
            if (i_4_ == 0) {
                ArchiveRequest.anIntArray1400[JImage.anInt1586++] = 2047;
            } else if (i_4_ == 1) {
                int i_5_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.method518(i - 1, i_5_,
                        false);
                int i_6_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                if (i_6_ == 1) {
                    ArchiveRequest.anIntArray1400[JImage.anInt1586++] = 2047;
                }
            } else if (i_4_ == 2) {
                int i_7_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.method518(-1, i_7_,
                        true);
                int i_8_ = Class39_Sub5_Sub11.gameBuffer.getBits(3);
                Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.method518(-1, i_8_,
                        true);
                int i_9_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                if (i_9_ == 1) {
                    ArchiveRequest.anIntArray1400[JImage.anInt1586++] = 2047;
                }
            } else if (i_4_ == 3) {
                int i_10_ = Class39_Sub5_Sub11.gameBuffer.getBits(7);
                int i_11_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                if (i_11_ == 1) {
                    ArchiveRequest.anIntArray1400[JImage.anInt1586++] = 2047;
                }
                NameTable.height = Class39_Sub5_Sub11.gameBuffer.getBits(2);
                int i_12_ = Class39_Sub5_Sub11.gameBuffer.getBits(7);
                int i_13_ = Class39_Sub5_Sub11.gameBuffer.getBits(1);
                Cache.aClass39_Sub5_Sub4_Sub4_Sub2_109.method508(i_12_, i_13_ == 1, (byte) 10, i_10_);
            }
        }
    }

    public static void method534(int i) {
        for (;;) {
            ArchiveRequest request;
            synchronized (JSocket.archiveRequests) {
                request = (ArchiveRequest) HashTable.aClass49_365.pollFirst();
            }
            if (request == null) {
                break;
            }
            request.aClass9_Sub1_1406.validateArchive(request.payload, (int) request.hash, false, request.cache);
        }
    }

    public static void method535(byte i, int i_14_) {
        FileLoader.anInt1302 = i_14_;
    }

    public Model method489(boolean bool) {
        Class39_Sub5_Sub18 class39_sub5_sub18 = Huffmans.method881(0, anInt2320);
        if (bool != true) {
            method535((byte) -14, -80);
        }
        Model class39_sub5_sub4_sub6;
        if (!aBoolean2333) {
            class39_sub5_sub4_sub6 = class39_sub5_sub18.method778(180, anInt2352);
        } else {
            class39_sub5_sub4_sub6 = class39_sub5_sub18.method778(180, -1);
        }
        if (class39_sub5_sub4_sub6 == null) {
            return null;
        }
        return class39_sub5_sub4_sub6;
    }

    public static JString decodeHuffmans(Buffer buffer, int length) {
        try {
            JString jstr = new JString();
            jstr.length = buffer.getSmartB();
            if (jstr.length > length) {
                jstr.length = length;
            }
            jstr.bytes = new byte[jstr.length];
            buffer.offset += Class25.huffmans.decode(0, jstr.bytes, jstr.length, buffer.offset, buffer.payload);
            return jstr;
        } catch (Exception exception) {
            return Canvas_Sub1.aClass3_20;
        }
    }

    public StillGraphic(int i, int i_16_, int i_17_, int i_18_,
            int i_19_, int i_20_, int i_21_) {
        anInt2322 = i_16_;
        anInt2323 = i_17_;
        anInt2321 = i_18_;
        anInt2347 = i_19_;
        anInt2336 = i_20_ + i_21_;
        anInt2320 = i;
        int i_22_ = Huffmans.method881(0, anInt2320).anInt2126;
        if (i_22_ == -1) {
            aBoolean2333 = true;
        } else {
            aBoolean2333 = false;
            aClass39_Sub5_Sub11_2325 = Class62_Sub1.method1064(i_22_, (byte) 54);
        }
    }

    static {
        aClass3_2332 = Class39_Sub5_Sub9.createJstring("Enter name:");
        anInt2326 = 2301979;
        anInt2338 = -1;
        aClass3_2344 = Class39_Sub5_Sub9.createJstring("Keine Antwort vom Anmelde)2Server)3");
        aClass3_2328 = Class39_Sub5_Sub9.createJstring("Mem:");
        aClass3_2340 = aClass3_2332;
        aClass3_2346 = aClass3_2330;
        aClass3_2335 = Class39_Sub5_Sub9.createJstring("Absender:");
        aClass3_2351 = Class39_Sub5_Sub9.createJstring("Loading ignore list");
        aLong2331 = 0L;
        aClass3_2329 = Class39_Sub5_Sub9.createJstring("Sichtbare Karte vorbereitet)3");
        aClass3_2350 = Class39_Sub5_Sub9.createJstring("Close");
        aClass3_2348 = Class39_Sub5_Sub9.createJstring("Stufe)2");
        aClass3_2353 = aClass3_2351;
        aClass3_2345 = Class39_Sub5_Sub9.createJstring("Please reload this page)3");
        aClass3_2327 = aClass3_2345;
        aClass3_2343 = aClass3_2350;
    }
}
