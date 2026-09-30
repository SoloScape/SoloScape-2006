package unpackaged;

import jagex.utils.HashTable;
import jagex.graphics.AbstractImage;
import jagex.graphics.sprites.DirectColorSprite;
import jagex.world.actors.StillGraphic;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.Buffer;

/* Class31 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class31 implements Runnable
{
    public static JString aClass3_549;
    public static JString aClass3_550
	= Class39_Sub5_Sub9.createJstring("Ihre Nachricht an: ");
    public static AbstractImage aClass57_551;
    public static JString aClass3_552
	= Class39_Sub5_Sub9.createJstring("button near the top of that page)3");
    public static HashTable requests;
    public boolean aBoolean554 = true;
    public static int[] anIntArray555;
    public static JString aClass3_556 = Class39_Sub5_Sub9.createJstring(": ");
    public static JString aClass3_557;
    public static JString aClass3_558
	= Class39_Sub5_Sub9.createJstring("Hidden)2");
    public Object anObject559 = new Object();
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array560;
    public int anInt561 = 0;
    public int[] anIntArray562 = new int[500];
    public static int state;
    public static JString aClass3_564;
    public static JString aClass3_565;
    public static JString aClass3_566;
    public static JString aClass3_567;
    public static FileTable itemFileLoader;
    public static JString aClass3_569;
    public static int anInt570;
    public int[] anIntArray571 = new int[500];
    public static JString aClass3_572;
    public static DirectColorSprite[] aClass39_Sub5_Sub10_Sub3Array573;
    
    public void run() {
	while (aBoolean554) {
	    synchronized (anObject559) {
		if (anInt561 < 500) {
		    anIntArray571[anInt561] = IsaacPrng.anInt1091;
		    anIntArray562[anInt561] = Class33.anInt599;
		    anInt561++;
		}
	    }
	    Class45.sleep(50L);
	}
    }
    
    public static void method320(int i) {
	if (i == -23401
	    && (Class39_Sub5_Sub11.anInt1841 >= 2 || Class13.anInt208 != 0
		|| IsaacPrng.aBoolean1100)) {
	    JString class3;
	    if (Class13.anInt208 == 1 && Class39_Sub5_Sub11.anInt1841 < 2)
		class3
		    = Class39_Sub5_Sub11.method708((new JString[]
						    { (Class39_Sub5_Sub4_Sub4
						       .aClass3_2310),
						      Class53.aClass3_953,
						      (Class39_Sub10
						       .aClass3_1436),
						      Class13.aClass3_205 }));
	    else if (!IsaacPrng.aBoolean1100
		     || Class39_Sub5_Sub11.anInt1841 >= 2)
		class3 = (Class33.aClass3Array601
			  [Class39_Sub5_Sub11.anInt1841 - 1]);
	    else
		class3
		    = Class39_Sub5_Sub11.method708((new JString[]
						    { Client.aClass3_1273,
						      Class53.aClass3_953,
						      Class14.aClass3_216,
						      Class13.aClass3_205 }));
	    if (Class39_Sub5_Sub11.anInt1841 > 2)
		class3
		    = (Class39_Sub5_Sub11.method708
		       (new JString[] { class3, Varbit.aClass3_1797,
				       AbstractImage.method1007((byte) 71,
							  (Class39_Sub5_Sub11
							   .anInt1841) - 2),
				       StillGraphic.aClass3_2346 }));
	    Class32.aClass39_Sub5_Sub10_Sub1_587.method639(class3, 4, 15,
							   16777215, true,
							   (Class2.logicCycle
							    / 1000));
	}
    }
    
    public static void method321(int i) {
	aClass3_564 = null;
	aClass3_566 = null;
	aClass3_572 = null;
	aClass57_551 = null;
	anIntArray555 = null;
	aClass39_Sub5_Sub10_Sub3Array560 = null;
	requests = null;
	aClass3_558 = null;
	aClass3_569 = null;
	aClass3_549 = null;
	aClass3_550 = null;
	aClass3_552 = null;
	aClass39_Sub5_Sub10_Sub3Array573 = null;
	itemFileLoader = null;
	aClass3_567 = null;
	aClass3_557 = null;
	aClass3_565 = null;
	aClass3_556 = null;
    }
    
    public static void method322(Signlink class21, int i,
				 Buffer class39_sub6, int i_0_) {
	Class39_Sub7 class39_sub7 = new Class39_Sub7();
	class39_sub7.anInt1382 = class39_sub6.getUbyte();
	class39_sub7.anInt1372 = class39_sub6.getDword();
	class39_sub7.anIntArray1378 = new int[class39_sub7.anInt1382];
	class39_sub7.anIntArray1379 = new int[class39_sub7.anInt1382];
	class39_sub7.anIntArray1376 = new int[class39_sub7.anInt1382];
	class39_sub7.aByteArrayArrayArray1381
	    = new byte[class39_sub7.anInt1382][][];
	class39_sub7.aClass56Array1383 = new Resource[class39_sub7.anInt1382];
	class39_sub7.aClass56Array1377 = new Resource[class39_sub7.anInt1382];
	for (int i_1_ = 0; i_1_ < class39_sub7.anInt1382; i_1_++) {
	    try {
		int i_2_ = class39_sub6.getUbyte();
		if (i_2_ == 0 || i_2_ == 1 || i_2_ == 2) {
		    String string
			= new String(class39_sub6.getJstr()
					 .method78(4484));
		    String string_3_
			= new String(class39_sub6.getJstr()
					 .method78(4484));
		    int i_4_ = 0;
		    if (i_2_ == 1)
			i_4_ = class39_sub6.getDword();
		    class39_sub7.anIntArray1378[i_1_] = i_2_;
		    class39_sub7.anIntArray1379[i_1_] = i_4_;
		    class39_sub7.aClass56Array1377[i_1_]
			= class21.requestDeclaredField(Class4.getTypeFromName(string),
					    string_3_);
		} else if (i_2_ == 3 || i_2_ == 4) {
		    String string
			= new String(class39_sub6.getJstr()
					 .method78(4484));
		    String string_5_
			= new String(class39_sub6.getJstr()
					 .method78(4484));
		    int i_6_ = class39_sub6.getUbyte();
		    String[] strings = new String[i_6_];
		    for (int i_7_ = 0; i_7_ < i_6_; i_7_++)
			strings[i_7_]
			    = new String(class39_sub6.getJstr()
					     .method78(4484));
		    byte[][] is = new byte[i_6_][];
		    if (i_2_ == 3) {
			for (int i_8_ = 0; i_8_ < i_6_; i_8_++) {
			    int i_9_ = class39_sub6.getDword();
			    is[i_8_] = new byte[i_9_];
			    class39_sub6.getBytes(is[i_8_], 0, i_9_);
			}
		    }
		    class39_sub7.anIntArray1378[i_1_] = i_2_;
		    Class[] var_classes = new Class[i_6_];
		    for (int i_10_ = 0; i_6_ > i_10_; i_10_++)
			var_classes[i_10_]
			    = Class4.getTypeFromName(strings[i_10_]);
		    class39_sub7.aClass56Array1383[i_1_]
			= class21.requestDeclaredMethod(Class4.getTypeFromName(string), string_5_,
					    var_classes);
		    class39_sub7.aByteArrayArrayArray1381[i_1_] = is;
		}
	    } catch (ClassNotFoundException classnotfoundexception) {
		class39_sub7.anIntArray1376[i_1_] = -1;
	    } catch (SecurityException securityexception) {
		class39_sub7.anIntArray1376[i_1_] = -2;
	    } catch (NullPointerException nullpointerexception) {
		class39_sub7.anIntArray1376[i_1_] = -3;
	    } catch (Exception exception) {
		class39_sub7.anIntArray1376[i_1_] = -4;
	    } catch (Throwable throwable) {
		class39_sub7.anIntArray1376[i_1_] = -5;
	    }
	}
	Class15.aClass49_283.offerLast(class39_sub7);
    }
    
    static {
	aClass3_557
	    = Class39_Sub5_Sub9
		  .createJstring("Spieler kann nicht gefunden werden: ");
	aClass39_Sub5_Sub10_Sub3Array560 = new DirectColorSprite[1000];
	aClass3_549 = aClass3_552;
	anIntArray555 = new int[25];
	state = 0;
	requests = new HashTable(4096);
	aClass3_564 = Class39_Sub5_Sub9.createJstring("Benutzername: ");
	aClass3_566
	    = (Class39_Sub5_Sub9.createJstring
	       ("Please close the interface you have open before using (Wreport abuse(W"));
	aClass3_567
	    = (Class39_Sub5_Sub9.createJstring
	       ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789(Q(R+R(T(U^(V(Z(X(Y)2_=)0[*U]*W;:(W@(S*X)1<)3>)4?*6*V "));
	aClass3_572 = aClass3_566;
	anInt570 = -1;
	aClass3_569
	    = Class39_Sub5_Sub9.createJstring("Bitte warten Sie eine Minute");
	aClass3_565 = aClass3_567;
    }
}
