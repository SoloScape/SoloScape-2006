package unpackaged;

/* Class15 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.world.actors.StillGraphic;
import jagex.utils.JString;
import jagex.utils.Deque;
import jagex.audio.Effect;
import java.awt.Component;

public class Class15 {

    public static int anInt274;
    public static FileLoader fileLoader1;
    public static int anInt276;
    public static int anInt277;
    public static Deque aClass49_278;
    public static JString aClass3_279;
    public static int anInt280;
    public static long aLong281;
    public static int amountIgnores = 0;
    public static Deque aClass49_283;
    public static JString aClass3_284;
    public static Effect[] aClass52Array285;
    public static JString aClass3_286;
    public static int anInt287;

    public static void method214(int i, Component component, boolean bool,
            Signlink class21, int i_0_) {
        anInt274 = i;
        StillGraphic.aBoolean2342 = bool;
        Class39_Sub5_Sub16.aLong1985 = Class2.getSystemTime();
        try {
            Class13_Sub1 class13_sub1 = ((Class13_Sub1) Class.forName("unpackaged.Class13_Sub1_Sub2").newInstance());
            class13_sub1.method202(component, i, bool);
            class13_sub1.method197(class21, 2048);
            Class20.aClass13_395 = class13_sub1;
        } catch (Throwable throwable) {
            try {
                Class13_Sub1_Sub1 class13_sub1_sub1 = new Class13_Sub1_Sub1(class21);
                class13_sub1_sub1.method202(component, i, bool);
                class13_sub1_sub1.method197(class21, 16384);
                Class20.aClass13_395 = class13_sub1_sub1;
            } catch (Throwable throwable_1_) {
                if ((Signlink.javaVendor.toLowerCase().indexOf("microsoft") ^ 0xffffffff) <= -1) {
                    try {
                        Class20.aClass13_395 = new Class13_Sub2();
                        StillGraphic.aBoolean2342 = false;
                        anInt274 = 8000;
                        return;
                    } catch (Throwable throwable_2_) {
                        /* empty */
                    }
                }
                Class20.aClass13_395 = new Class13();
            }
        }
    }

    public static void method215(byte i) {
        aClass52Array285 = null;
        aClass3_284 = null;
        aClass49_283 = null;
        fileLoader1 = null;
        aClass3_286 = null;
        aClass3_279 = null;
        aClass49_278 = null;
    }

    static {
        anInt276 = 20;
        anInt277 = 0;
        aClass3_279 = Class39_Sub5_Sub9.createJstring(" x");
        anInt280 = 0;
        aLong281 = 0L;
        aClass49_278 = new Deque();
        aClass49_283 = new Deque();
        aClass3_284 = Class39_Sub5_Sub9.createJstring("Loading friend list");
        aClass52Array285 = new Effect[50];
        aClass3_286 = aClass3_284;
    }
}
