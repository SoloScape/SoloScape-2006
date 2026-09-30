package unpackaged;

/* Class39_Sub5_Sub12 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.BitmapFont;
import jagex.graphics.DrawingArea;
import jagex.graphics.JImage;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.world.actors.StillGraphic;
import jagex.utils.SubNode;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Queue;
import jagex.utils.Deque;
import jagex.utils.Cache;
import java.awt.Graphics;

public class Class39_Sub5_Sub12 extends SubNode
{
    public static int anInt1848;
    public static JString aClass3_1849
	= Class39_Sub5_Sub9.createJstring("wave2:");
    public static JString aClass3_1850 = aClass3_1849;
    public static JString aClass3_1851
	= Class39_Sub5_Sub9.createJstring("Verbinde mit Server)3)3)3");
    public static JString aClass3_1852
	= Class39_Sub5_Sub9.createJstring("Menge eingeben:");
    public static Cache aClass7_1853;
    public static int anInt1854;
    public Class51[] aClass51Array1855;
    public static boolean aBoolean1856 = false;
    public static TraversalMap[] aClass27Array1857;
    public static JString aClass3_1858;
    
    public static void method709
	(BitmapFont class39_sub5_sub10_sub1, int i,
	 BitmapFont class39_sub5_sub10_sub1_0_) {
	Class66.aClass57_1156.method1006(10);
	if (Class31.state == 0 || Class31.state == 5) {
	    int i_1_ = 20;
	    int i_2_ = -i_1_ + 82;
	    class39_sub5_sub10_sub1_0_.method629(Class62_Sub2.aClass3_1601,
						 180, -i_1_ + 74, 16777215);
	    DrawingArea.drawQuadOutline(28, i_2_, 304, 34, 9179409);
	    DrawingArea.drawQuadOutline(29, i_2_ + 1, 302, 32, 0);
	    DrawingArea.drawQuad(30, i_2_ + 2,
					 Class39_Sub7.anInt1387 * 3, 30,
					 9179409);
	    DrawingArea.drawQuad(Class39_Sub7.anInt1387 * 3 + 30,
					 i_2_ + 2,
					 -(Class39_Sub7.anInt1387 * 3) + 300,
					 30, 0);
	    class39_sub5_sub10_sub1_0_.method629((Class39_Sub5_Sub14
						  .aClass3_1918),
						 180, -i_1_ + 105, 16777215);
	}
	if (Class31.state == 20) {
	    Class55.aClass39_Sub5_Sub10_Sub4_1257.method695(0, 0);
	    int i_3_ = 40;
	    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
						  .aClass3_1916),
						 180, i_3_, 16776960, true);
	    i_3_ += 15;
	    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
						  .aClass3_1896),
						 180, i_3_, 16776960, true);
	    i_3_ += 15;
	    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
						  .aClass3_1899),
						 180, i_3_, 16776960, true);
	    i_3_ += 15;
	    i_3_ += 10;
	    class39_sub5_sub10_sub1_0_.method635
		(Class39_Sub5_Sub11.method708((new JString[]
					       { JSocket.aClass3_305,
						 (Class39_Sub5_Sub14
						  .aClass3_1897) })),
		 90, i_3_, 16777215, true);
	    i_3_ += 15;
	    class39_sub5_sub10_sub1_0_.method635
		((Class39_Sub5_Sub11.method708
		  (new JString[] { Deque.aClass3_910,
				  Class39_Sub5_Sub14.aClass3_1905
				      .formatPassword() })),
		 92, i_3_, 16777215, true);
	    i_3_ += 15;
	}
	if (Class31.state == 10) {
	    Class55.aClass39_Sub5_Sub10_Sub4_1257.method695(0, 0);
	    if (Class10.anInt155 != 0) {
		if (Class10.anInt155 == 2) {
		    int i_4_ = 40;
		    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
							  .aClass3_1916),
							 180, i_4_, 16776960,
							 true);
		    i_4_ += 15;
		    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
							  .aClass3_1896),
							 180, i_4_, 16776960,
							 true);
		    int i_5_ = 100;
		    int i_6_ = 150;
		    i_4_ += 15;
		    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub14
							  .aClass3_1899),
							 180, i_4_, 16776960,
							 true);
		    i_4_ += 15;
		    i_4_ += 10;
		    class39_sub5_sub10_sub1_0_.method635
			((Class39_Sub5_Sub11.method708
			  (new JString[] { JSocket.aClass3_305,
					  Class39_Sub5_Sub14.aClass3_1897,
					  (!(Class2.logicCycle % 40 < 20
					     & Class43.anInt815 == 0)
					   ? Class39_Sub5_Sub14.aClass3_1906
					   : JImage.aClass3_1582) })),
			 90, i_4_, 16777215, true);
		    i_4_ += 15;
		    class39_sub5_sub10_sub1_0_.method635
			((Class39_Sub5_Sub11.method708
			  ((new JString[]
			    { Deque.aClass3_910,
			      Class39_Sub5_Sub14.aClass3_1905
				  .formatPassword(),
			      (Class43.anInt815 == 1 & Class2.logicCycle % 40 < 20
			       ? JImage.aClass3_1582
			       : Class39_Sub5_Sub14.aClass3_1906) }))),
			 92, i_4_, 16777215, true);
		    i_4_ += 15;
		    StillGraphic.aClass39_Sub5_Sub10_Sub4_2337
			.method695(i_5_ - 73, i_6_ - 20);
		    class39_sub5_sub10_sub1_0_.method636(Class46.aClass3_875,
							 i_5_, i_6_ + 5,
							 16777215, true);
		    i_5_ = 260;
		    StillGraphic.aClass39_Sub5_Sub10_Sub4_2337
			.method695(i_5_ - 73, i_6_ - 20);
		    class39_sub5_sub10_sub1_0_.method636
			(Player.aClass3_2531, i_5_,
			 i_6_ + 5, 16777215, true);
		} else if (Class10.anInt155 == 3) {
		    int i_7_ = 65;
		    class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub11
							  .aClass3_1845),
							 180, 40, 16776960,
							 true);
		    int i_8_ = 150;
		    class39_sub5_sub10_sub1_0_.method636(Class67.aClass3_1176,
							 180, i_7_, 16777215,
							 true);
		    i_7_ += 15;
		    int i_9_ = 180;
		    class39_sub5_sub10_sub1_0_.method636(Class4.aClass3_79,
							 180, i_7_, 16777215,
							 true);
		    i_7_ += 15;
		    class39_sub5_sub10_sub1_0_.method636(Class14.aClass3_228,
							 180, i_7_, 16777215,
							 true);
		    i_7_ += 15;
		    class39_sub5_sub10_sub1_0_.method636(Class31.aClass3_549,
							 180, i_7_, 16777215,
							 true);
		    StillGraphic.aClass39_Sub5_Sub10_Sub4_2337
			.method695(i_9_ - 73, i_8_ - 20);
		    i_7_ += 15;
		    class39_sub5_sub10_sub1_0_.method636
			(Player.aClass3_2531, i_9_,
			 i_8_ + 5, 16777215, true);
		}
	    } else {
		int i_10_ = 100;
		int i_11_ = 120;
		int i_12_ = 80;
		class39_sub5_sub10_sub1_0_.method636((Class39_Sub5_Sub7
						      .aClass3_1777),
						     180, i_12_, 16776960,
						     true);
		i_12_ += 30;
		StillGraphic.aClass39_Sub5_Sub10_Sub4_2337
		    .method695(i_10_ - 73, i_11_ - 20);
		class39_sub5_sub10_sub1_0_.method625((Class39_Sub5_Sub6
						      .aClass3_1766),
						     i_10_ - 73, i_11_ - 20,
						     144, 40, 16777215, true,
						     1, 1, 0);
		i_10_ = 260;
		StillGraphic.aClass39_Sub5_Sub10_Sub4_2337
		    .method695(i_10_ - 73, i_11_ - 20);
		class39_sub5_sub10_sub1_0_.method625(Class46.aClass3_876,
						     i_10_ - 73, i_11_ - 20,
						     144, 40, 16777215, true,
						     1, 1, 0);
	    }
	}
	CacheIO.method128((byte) -115);
	do {
	    try {
		Graphics graphics = Class41.aCanvas778.getGraphics();
		Class66.aClass57_1156.draw(graphics, 202, 171);
		Class31.aClass57_551.draw(graphics, 0, 0);
		Class68.aClass57_1186.draw(graphics, 637, 0);
		if (i != -27620)
		    method712(null, 51, -50, 92, 6, -45, -40, null, 24, 91);
		if (!ClientScript.aBoolean1690)
		    break;
		ClientScript.aBoolean1690 = false;
		Class26.aClass57_483.draw(graphics, 128, 0);
		Widget.aClass57_2115.draw(graphics,
							    202, 371);
		Client.aClass57_1267.draw(graphics, 0, 265);
		Player.aClass57_2525
		    .draw(graphics, 562, 265);
		Class14.aClass57_246.draw(graphics, 128,
						171);
		Class32.aClass57_585.draw(graphics, 562, 171);
	    } catch (Exception exception) {
		Class41.aCanvas778.repaint();
	    }
	    break;
	} while (false);
    }
    
    public boolean method710(int i, byte i_13_) {
	return aClass51Array1855[i].aBoolean940;
    }
    
    public Class39_Sub5_Sub12(FileTable class9, FileTable class9_14_, int i,
			      boolean bool) {
	Deque class49 = new Deque();
	int i_15_ = class9.getAmountChildren(i);
	aClass51Array1855 = new Class51[i_15_];
	int[] is = class9.getChildrenEntries(i);
	for (int i_16_ = 0; i_16_ < is.length; i_16_++) {
	    byte[] is_17_ = class9.lookupFile(i, is[i_16_]);
	    Class39_Sub12 class39_sub12 = null;
	    int i_18_ = is_17_[0] << 8 & 0xff00 | is_17_[1] & 0xff;
	    for (Class39_Sub12 class39_sub12_19_
		     = (Class39_Sub12) class49.getFirst();
		 class39_sub12_19_ != null;
		 class39_sub12_19_
		     = (Class39_Sub12) class49.getNext()) {
		if (class39_sub12_19_.anInt1494 == i_18_) {
		    class39_sub12 = class39_sub12_19_;
		    break;
		}
	    }
	    if (class39_sub12 == null) {
		byte[] is_20_;
		if (bool)
		    is_20_ = class9_14_.lookupFile(0, i_18_);
		else
		    is_20_ = class9_14_.lookupFile(i_18_, 0);
		class39_sub12 = new Class39_Sub12(i_18_, is_20_);
		class49.offerLast(class39_sub12);
	    }
	    aClass51Array1855[is[i_16_]] = new Class51(is_17_, class39_sub12);
	}
    }
    
    public static void method711(int i) {
	aClass3_1852 = null;
	aClass27Array1857 = null;
	aClass3_1850 = null;
	aClass3_1858 = null;
	aClass3_1851 = null;
	aClass3_1849 = null;
	aClass7_1853 = null;
    }
    
    public static void method712(TraversalMap class27, int i, int i_21_, int i_22_,
				 int i_23_, int i_24_, int i_25_,
				 Class38 class38, int i_26_, int i_27_) {
	int i_28_ = Class67.heightMap[i_23_][i][i_27_];
	int i_29_ = Class67.heightMap[i_23_][i + 1][i_27_];
	int i_30_ = Class67.heightMap[i_23_][i + 1][i_27_ + 1];
	int i_31_ = Class67.heightMap[i_23_][i][i_27_ + 1];
	int i_32_ = i_31_ + (i_30_ + i_29_ + i_28_) >> 2;
	ObjectDefinition class39_sub5_sub15
	    = Canvas_Sub1.method40(i_24_, (byte) 115);
	int i_33_ = (i_24_ << 14) + 1073741824 + ((i_27_ << 7) + i);
	int i_34_ = (i_21_ << 6) + i_25_;
	if (class39_sub5_sub15.anInt1967 == 1)
	    i_34_ += 256;
	if (class39_sub5_sub15.anInt1941 == 0)
	    i_33_ -= -2147483648;
	if (i_25_ == 22) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 == -1
		&& class39_sub5_sub15.anIntArray1961 == null)
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 22, i_28_, i_21_, i_31_,
						   i_30_, i_29_);
	    else
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 22, i_21_, i_28_,
						 i_29_, i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    class38.method381(i_22_, i, i_27_, i_32_, class39_sub5_sub4, i_33_,
			      i_34_);
	    if (class39_sub5_sub15.aBoolean1928
		&& class39_sub5_sub15.anInt1941 == 1)
		class27.method306((byte) -85, i_27_, i);
	} else if (i_25_ == 10 || i_25_ == 11) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 != -1
		|| class39_sub5_sub15.anIntArray1961 != null)
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 10, i_21_, i_28_,
						 i_29_, i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    else
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 10, i_28_, i_21_, i_31_,
						   i_30_, i_29_);
	    if (class39_sub5_sub4 != null) {
		int i_35_;
		int i_36_;
		if (i_21_ == 1 || i_21_ == 3) {
		    i_36_ = class39_sub5_sub15.anInt1948;
		    i_35_ = class39_sub5_sub15.anInt1925;
		} else {
		    i_35_ = class39_sub5_sub15.anInt1948;
		    i_36_ = class39_sub5_sub15.anInt1925;
		}
		int i_37_ = 0;
		if (i_25_ == 11)
		    i_37_ += 256;
		class38.method383(i_22_, i, i_27_, i_32_, i_36_, i_35_,
				  class39_sub5_sub4, i_37_, i_33_, i_34_);
	    }
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method304(class39_sub5_sub15.aBoolean1949, i,
				  class39_sub5_sub15.anInt1925, (byte) -54,
				  i_27_, class39_sub5_sub15.anInt1948, i_21_);
	} else if (i_25_ >= 12) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 == -1
		&& class39_sub5_sub15.anIntArray1961 == null)
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, i_25_, i_28_, i_21_,
						   i_31_, i_30_, i_29_);
	    else
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, i_25_, i_21_, i_28_,
						 i_29_, i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    class38.method383(i_22_, i, i_27_, i_32_, 1, 1, class39_sub5_sub4,
			      0, i_33_, i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method304(class39_sub5_sub15.aBoolean1949, i,
				  class39_sub5_sub15.anInt1925, (byte) -54,
				  i_27_, class39_sub5_sub15.anInt1948, i_21_);
	} else if (i_25_ == 0) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 == -1
		&& class39_sub5_sub15.anIntArray1961 == null)
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 0, i_28_, i_21_, i_31_,
						   i_30_, i_29_);
	    else
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 0, i_21_, i_28_, i_29_,
						 i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    class38.method405(i_22_, i, i_27_, i_32_, class39_sub5_sub4, null,
			      Widget.anIntArray2005[i_21_], 0,
			      i_33_, i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method302(i, 16384, class39_sub5_sub15.aBoolean1949,
				  i_27_, i_21_, i_25_);
	} else if (i_25_ == 1) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 != -1
		|| class39_sub5_sub15.anIntArray1961 != null)
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 1, i_21_, i_28_, i_29_,
						 i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    else
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 1, i_28_, i_21_, i_31_,
						   i_30_, i_29_);
	    class38.method405(i_22_, i, i_27_, i_32_, class39_sub5_sub4, null,
			      Class62_Sub1.anIntArray1588[i_21_], 0, i_33_,
			      i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method302(i, 16384, class39_sub5_sub15.aBoolean1949,
				  i_27_, i_21_, i_25_);
	} else if (i_25_ == 2) {
	    int i_38_ = i_21_ + 1 & 0x3;
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    Class39_Sub5_Sub4 class39_sub5_sub4_39_;
	    if (class39_sub5_sub15.anInt1939 == -1
		&& class39_sub5_sub15.anIntArray1961 == null) {
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 2, i_28_, 4 + i_21_,
						   i_31_, i_30_, i_29_);
		class39_sub5_sub4_39_
		    = class39_sub5_sub15.method742(66, 2, i_28_, i_38_, i_31_,
						   i_30_, i_29_);
	    } else {
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 2, i_21_ + 4, i_28_,
						 i_29_, i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
		class39_sub5_sub4_39_
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 2, i_38_, i_28_, i_29_,
						 i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    }
	    class38.method405(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
			      class39_sub5_sub4_39_,
			      Widget.anIntArray2005[i_21_],
			      Widget.anIntArray2005[i_38_], i_33_,
			      i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method302(i, 16384, class39_sub5_sub15.aBoolean1949,
				  i_27_, i_21_, i_25_);
	} else if (i_25_ == 3) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 == -1
		&& class39_sub5_sub15.anIntArray1961 == null)
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, 3, i_28_, i_21_, i_31_,
						   i_30_, i_29_);
	    else
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, 3, i_21_, i_28_, i_29_,
						 i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    class38.method405(i_22_, i, i_27_, i_32_, class39_sub5_sub4, null,
			      Class62_Sub1.anIntArray1588[i_21_], 0, i_33_,
			      i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method302(i, 16384, class39_sub5_sub15.aBoolean1949,
				  i_27_, i_21_, i_25_);
	} else if (i_25_ == 9) {
	    Class39_Sub5_Sub4 class39_sub5_sub4;
	    if (class39_sub5_sub15.anInt1939 != -1
		|| class39_sub5_sub15.anIntArray1961 != null)
		class39_sub5_sub4
		    = new Class39_Sub5_Sub4_Sub2(i_24_, i_25_, i_21_, i_28_,
						 i_29_, i_30_, i_31_,
						 class39_sub5_sub15.anInt1939,
						 true, null);
	    else
		class39_sub5_sub4
		    = class39_sub5_sub15.method742(66, i_25_, i_28_, i_21_,
						   i_31_, i_30_, i_29_);
	    class38.method383(i_22_, i, i_27_, i_32_, 1, 1, class39_sub5_sub4,
			      0, i_33_, i_34_);
	    if (class39_sub5_sub15.aBoolean1928)
		class27.method304(class39_sub5_sub15.aBoolean1949, i,
				  class39_sub5_sub15.anInt1925, (byte) -54,
				  i_27_, class39_sub5_sub15.anInt1948, i_21_);
	} else {
	    if (class39_sub5_sub15.aBoolean1951) {
		if (i_21_ == 1) {
		    int i_40_ = i_31_;
		    i_31_ = i_30_;
		    i_30_ = i_29_;
		    i_29_ = i_28_;
		    i_28_ = i_40_;
		} else if (i_21_ != 2) {
		    if (i_21_ == 3) {
			int i_41_ = i_31_;
			i_31_ = i_28_;
			i_28_ = i_29_;
			i_29_ = i_30_;
			i_30_ = i_41_;
		    }
		} else {
		    int i_42_ = i_31_;
		    i_31_ = i_29_;
		    i_29_ = i_42_;
		    i_42_ = i_30_;
		    i_30_ = i_28_;
		    i_28_ = i_42_;
		}
	    }
	    if (i_25_ == 4) {
		Class39_Sub5_Sub4 class39_sub5_sub4;
		if (class39_sub5_sub15.anInt1939 != -1
		    || class39_sub5_sub15.anIntArray1961 != null)
		    class39_sub5_sub4
			= new Class39_Sub5_Sub4_Sub2(i_24_, 4, 0, i_28_, i_29_,
						     i_30_, i_31_,
						     (class39_sub5_sub15
						      .anInt1939),
						     true, null);
		else
		    class39_sub5_sub4
			= class39_sub5_sub15.method742(66, 4, i_28_, 0, i_31_,
						       i_30_, i_29_);
		class38.method365(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
				  Widget.anIntArray2005[i_21_],
				  i_21_ * 512, 0, 0, i_33_, i_34_);
	    } else if (i_25_ == 5) {
		int i_43_ = class38.method379(i_22_, i, i_27_);
		int i_44_ = 16;
		if (i_43_ > 0)
		    i_44_ = Canvas_Sub1.method40((i_43_ & 0x1fffca9b) >> 14,
						 (byte) 124).anInt1936;
		Class39_Sub5_Sub4 class39_sub5_sub4;
		if (class39_sub5_sub15.anInt1939 == -1
		    && class39_sub5_sub15.anIntArray1961 == null)
		    class39_sub5_sub4
			= class39_sub5_sub15.method742(66, 4, i_28_, 0, i_31_,
						       i_30_, i_29_);
		else
		    class39_sub5_sub4
			= new Class39_Sub5_Sub4_Sub2(i_24_, 4, 0, i_28_, i_29_,
						     i_30_, i_31_,
						     (class39_sub5_sub15
						      .anInt1939),
						     true, null);
		class38.method365(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
				  Widget.anIntArray2005[i_21_],
				  i_21_ * 512,
				  Queue.anIntArray978[i_21_] * i_44_,
				  Class53.anIntArray964[i_21_] * i_44_, i_33_,
				  i_34_);
	    } else if (i_25_ == 6) {
		Class39_Sub5_Sub4 class39_sub5_sub4;
		if (class39_sub5_sub15.anInt1939 == -1
		    && class39_sub5_sub15.anIntArray1961 == null)
		    class39_sub5_sub4
			= class39_sub5_sub15.method742(66, 4, i_28_, 0, i_31_,
						       i_30_, i_29_);
		else
		    class39_sub5_sub4
			= new Class39_Sub5_Sub4_Sub2(i_24_, 4, 0, i_28_, i_29_,
						     i_30_, i_31_,
						     (class39_sub5_sub15
						      .anInt1939),
						     true, null);
		class38.method365(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
				  256, i_21_, 0, 0, i_33_, i_34_);
	    } else if (i_25_ == 7) {
		Class39_Sub5_Sub4 class39_sub5_sub4;
		if (class39_sub5_sub15.anInt1939 == -1
		    && class39_sub5_sub15.anIntArray1961 == null)
		    class39_sub5_sub4
			= class39_sub5_sub15.method742(66, 4, i_28_, 0, i_31_,
						       i_30_, i_29_);
		else
		    class39_sub5_sub4
			= new Class39_Sub5_Sub4_Sub2(i_24_, 4, 0, i_28_, i_29_,
						     i_30_, i_31_,
						     (class39_sub5_sub15
						      .anInt1939),
						     true, null);
		class38.method365(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
				  512, i_21_, 0, 0, i_33_, i_34_);
	    } else if (i_25_ == 8) {
		Class39_Sub5_Sub4 class39_sub5_sub4;
		if (class39_sub5_sub15.anInt1939 == -1
		    && class39_sub5_sub15.anIntArray1961 == null)
		    class39_sub5_sub4
			= class39_sub5_sub15.method742(66, 4, i_28_, 0, i_31_,
						       i_30_, i_29_);
		else
		    class39_sub5_sub4
			= new Class39_Sub5_Sub4_Sub2(i_24_, 4, 0, i_28_, i_29_,
						     i_30_, i_31_,
						     (class39_sub5_sub15
						      .anInt1939),
						     true, null);
		class38.method365(i_22_, i, i_27_, i_32_, class39_sub5_sub4,
				  768, i_21_, 0, 0, i_33_, i_34_);
	    }
	}
    }
    
    public static IndexedColorSprite[] method713
	(byte i, FileTable class9, JString class3, JString class3_45_) {
	int i_46_ = class9.lookupArchive(class3_45_);
	int i_47_ = class9.lookupChild(i_46_, class3);
	return JImage.method1015(0, i_46_, i_47_, class9);
    }
    
    static {
	aClass7_1853 = new Cache(64);
	aClass27Array1857 = new TraversalMap[4];
	aClass3_1858
	    = Class39_Sub5_Sub9.createJstring("Keine Antwort vom Server)3");
    }
}
