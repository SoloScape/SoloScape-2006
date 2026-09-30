package unpackaged;

/* Class41 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.JString;
import jagex.utils.Queue;
import jagex.io.FrameBuffer;
import java.awt.Canvas;

public class Class41
{
    public static int anInt765;
    public static int queuedRegularRequests;
    public static JString aClass3_767;
    public static int anInt768 = 0;
    public static JString aClass3_769;
    public static JString aClass3_770;
    public static JString aClass3_771;
    public static JString aClass3_772
	= Class39_Sub5_Sub9.createJstring("Freunde");
    public static int anInt773 = 0;
    public static JString aClass3_774;
    public static int anInt775;
    public static int anInt776;
    public static boolean isCurrentRequestPriority;
    public static Canvas aCanvas778;
    public static JString aClass3_779;
    public static int anInt780;
    public static JString aClass3_781;
    public static volatile long aLong782;
    public static JString aClass3_783;
    public static volatile int anInt784;
    
    public static synchronized void method891(int i, int i_0_, int i_1_,
					      boolean bool, FileTable class9,
					      int i_2_, int i_3_) {
	if (Class47.method946((byte) -62)) {
	    Class65.anInt1135 = -1;
	    FrameBuffer.aBoolean2153 = bool;
	    Class39_Sub5_Sub4.anInt1730 = i_3_;
	    Class39_Sub5_Sub14.anInt1919 = i_1_;
	    Class39_Sub10.aClass9_1445 = class9;
	    Class39_Sub5_Sub5.aBoolean1749 = true;
	    Class23.anInt425 = i_0_;
	    JString.anInt1240 = -1;
	    Class65.anInt1143 = i_2_;
	}
    }
    
    public static synchronized void method892(byte i) {
	if (Class47.method946((byte) -62)) {
	    Class46.method941((byte) -77);
	    Class39_Sub5_Sub5.aBoolean1749 = false;
	    Class39_Sub10.aClass9_1445 = null;
	}
    }
    
    public static void method893(int i) {
	ArchiveWorker.aClass46_1206.method934(0);
	for (int i_4_ = 0; i_4_ < 32; i_4_++)
	    Class39_Sub7.aLongArray1374[i_4_] = 0L;
	for (int i_5_ = 0; i_5_ < 32; i_5_++)
	    Class67.aLongArray1172[i_5_] = 0L;
	Class2.anInt50 = 0;
    }
    
    public static synchronized void method894(FileTable class9, int i, int i_6_,
					      boolean bool, int i_7_, int i_8_,
					      int i_9_, int i_10_) {
	if (Class47.method946((byte) -62)) {
	    JString.anInt1240 = -1;
	    Class39_Sub5_Sub14.anInt1919 = i_8_;
	    Class39_Sub5_Sub4.anInt1730 = i;
	    Class65.anInt1135 = i_7_;
	    Class39_Sub10.aClass9_1445 = class9;
	    Class39_Sub5_Sub5.aBoolean1749 = true;
	    Class23.anInt425 = i_6_;
	    Class65.anInt1143 = i_10_;
	    FrameBuffer.aBoolean2153 = bool;
	}
    }
    
    public static synchronized void method895(byte i) {
	if (Class47.method946((byte) -62)) {
	    if (Class39_Sub5_Sub5.aBoolean1749) {
		byte[] is = FileTable.method160(Class23.anInt425, 2,
					     Class39_Sub5_Sub4.anInt1730,
					     Class39_Sub10.aClass9_1445,
					     Class39_Sub5_Sub14.anInt1919);
		if (is != null) {
		    if (Class65.anInt1135 < 0) {
			if (JString.anInt1240 >= 0)
			    Class26.method292(FrameBuffer.aBoolean2153,
					      false, JString.anInt1240,
					      Class65.anInt1143, is);
			else
			    ArchiveRequest.method857(Class65.anInt1143,
						   (FrameBuffer
						    .aBoolean2153),
						   is, false);
		    } else
			Varbit.method593(Class65.anInt1143, 1,
						    (FrameBuffer
						     .aBoolean2153),
						    is, Class65.anInt1135);
		    Class39_Sub5_Sub5.aBoolean1749 = false;
		    Class39_Sub10.aClass9_1445 = null;
		}
	    }
	    Class44.method912(-19093);
	}
    }
    
    public static synchronized void method896(int i) {
	Queue.method987(-30574);
    }
    
    public static void method897(int i) {
	aClass3_771 = null;
	aClass3_781 = null;
	aCanvas778 = null;
	aClass3_772 = null;
	aClass3_783 = null;
	aClass3_767 = null;
	aClass3_769 = null;
	aClass3_779 = null;
	aClass3_770 = null;
	aClass3_774 = null;
    }
    
    public static void method898(int i) {
	if (Class20.aClass13_395 != null) {
	    Class20.aClass13_395.method187();
	    Class20.aClass13_395 = null;
	}
	Class15.anInt274 = i;
    }
    
    public static synchronized void method899
	(int i, boolean bool, int i_11_, JString class3, boolean bool_12_,
	 JString class3_13_, FileTable class9, int i_14_) {
	if (Class47.method946((byte) -62)) {
	    int i_15_ = class9.lookupArchive(class3);
	    int i_16_ = class9.lookupChild(i_15_, class3_13_);
	    method894(class9, i_15_, i_16_, bool_12_, i_14_, i, -30, i_11_);
	}
    }
    
    public static synchronized void method900(byte i, int i_17_) {
	if (Class47.method946((byte) -62)) {
	    Class63.method1087(-26, i_17_);
	    Class39_Sub10.aClass9_1445 = null;
	    Class39_Sub5_Sub5.aBoolean1749 = false;
	}
    }
    
    static {
	aClass3_770 = Class39_Sub5_Sub9.createJstring("Konfig geladen)3");
	queuedRegularRequests = 0;
	aClass3_767 = (Class39_Sub5_Sub9.createJstring
		       ("Sie haben gerade eine andere Welt verlassen)3"));
	aClass3_774 = Class39_Sub5_Sub9.createJstring("Null");
	aClass3_779 = Class39_Sub5_Sub9.createJstring("compass");
	aClass3_769
	    = Class39_Sub5_Sub9.createJstring("Enter message to send to ");
	aClass3_771 = Class39_Sub5_Sub9.createJstring("backright2");
	aClass3_783 = Class39_Sub5_Sub9.createJstring("@gre@");
	anInt780 = 0;
	aClass3_781 = aClass3_769;
	anInt784 = 0;
	aLong782 = 0L;
    }
}
