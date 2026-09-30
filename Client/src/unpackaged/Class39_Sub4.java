package unpackaged;

import jagex.io.BufferedFile;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.world.actors.StillGraphic;
import jagex.world.actors.Projectile;
import jagex.utils.Node;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.io.FrameBuffer;

/* Class39_Sub4 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub4 extends Node
{
    public static JString aClass3_1324
	= Class39_Sub5_Sub9.createJstring("Hide");
    public JString aClass3_1325;
    public static JString aClass3_1326;
    public static JString aClass3_1327
	= Class39_Sub5_Sub9.createJstring("Walk here");
    public static JString aClass3_1328;
    public static int anInt1329;
    public static ScriptState[] scriptStateStack;
    public static int odBlockOffset;
    public static JString aClass3_1332;
    public static JString aClass3_1333;
    public static JString aClass3_1334;
    public static int anInt1335 = 0;
    public static int anInt1336;
    public static JString aClass3_1337
	= Class39_Sub5_Sub9.createJstring("Verbindung abgebrochen)3");
    public static JString aClass3_1338 = aClass3_1327;
    public static JString aClass3_1339;
    public static JString aClass3_1340;
    
    public static void method456(byte i, int i_0_, int i_1_, int i_2_,
				 int i_3_, int i_4_, int i_5_, int i_6_,
				 Widget class39_sub5_sub17) {
	if (!RuntimeException_Sub1.aBoolean1221)
	    Class46_Sub1.anInt1561 = 0;
	else
	    Class46_Sub1.anInt1561 = 32;
	RuntimeException_Sub1.aBoolean1221 = false;
	if (i_0_ > i_1_ || i_0_ + 16 <= i_1_ || i_5_ > i_2_
	    || i_2_ >= i_5_ + 16) {
	    if (i_0_ <= i_1_ && i_1_ < i_0_ + 16 && i_4_ + i_5_ - 16 <= i_2_
		&& i_2_ < i_5_ + i_4_) {
		if (i_3_ == 2 || i_3_ == 3)
		    Class14.aBoolean245 = true;
		if (i_3_ == 1)
		    Class39_Sub14.aBoolean1520 = true;
		class39_sub5_sub17.anInt1994 += Class46_Sub1.anInt1547 * 4;
	    } else if (i_1_ >= -Class46_Sub1.anInt1561 + i_0_
		       && i_1_ < i_0_ + 16 + Class46_Sub1.anInt1561
		       && i_5_ + 16 <= i_2_ && i_2_ < i_4_ + (i_5_ - 16)
		       && Class46_Sub1.anInt1547 > 0) {
		RuntimeException_Sub1.aBoolean1221 = true;
		if (i_3_ == 2 || i_3_ == 3)
		    Class14.aBoolean245 = true;
		if (i_3_ == 1)
		    Class39_Sub14.aBoolean1520 = true;
		int i_7_ = (i_4_ - 32) * i_4_ / i_6_;
		if (i_7_ < 8)
		    i_7_ = 8;
		int i_8_ = -i_7_ + (i_4_ - 32);
		int i_9_ = -(i_7_ / 2) - 16 + i_2_ - i_5_;
		class39_sub5_sub17.anInt1994 = (-i_4_ + i_6_) * i_9_ / i_8_;
	    }
	} else {
	    class39_sub5_sub17.anInt1994 -= Class46_Sub1.anInt1547 * 4;
	    if (i_3_ == 1)
		Class39_Sub14.aBoolean1520 = true;
	    if (i_3_ == 2 || i_3_ == 3)
		Class14.aBoolean245 = true;
	}
	if (Class55.anInt1252 != 0) {
	    int i_10_ = class39_sub5_sub17.quadWidth;
	    if (i_3_ == -1)
		i_10_ = 479;
	    if (i_0_ - i_10_ <= i_1_ && i_5_ <= i_2_ && i_1_ < i_0_ + 16
		&& i_5_ + i_4_ >= i_2_) {
		class39_sub5_sub17.anInt1994 += Class55.anInt1252 * 45;
		if (i_3_ == 2 || i_3_ == 3)
		    Class14.aBoolean245 = true;
		if (i_3_ == 1)
		    Class39_Sub14.aBoolean1520 = true;
	    }
	}
    }
    
    public static void method457(int i, int i_11_) {
	if (i_11_ != -1 && JSocket.loadWidget(i_11_)) {
	    Widget[] class39_sub5_sub17s
		= Class62_Sub1.widgets[i_11_];
	    for (int i_12_ = 0; i_12_ < class39_sub5_sub17s.length; i_12_++) {
		Widget class39_sub5_sub17
		    = class39_sub5_sub17s[i_12_];
		if (class39_sub5_sub17 != null
			&& class39_sub5_sub17.anObjectArray2033 != null)
		    Class39_Sub5_Sub4_Sub4.executeClientScript(0,
						     (class39_sub5_sub17
						      .anObjectArray2033),
						     null, 120, 0,
						     class39_sub5_sub17, 0);
	    }
	}
    }
    
    public static void method458(int i) {
	aClass3_1326 = null;
	aClass3_1324 = null;
	aClass3_1333 = null;
	aClass3_1327 = null;
	aClass3_1340 = null;
	aClass3_1337 = null;
	aClass3_1338 = null;
	scriptStateStack = null;
	aClass3_1334 = null;
	aClass3_1332 = null;
	aClass3_1328 = null;
	aClass3_1339 = null;
    }
    
    public static boolean method459(int i, byte i_13_) {
	if (!JSocket.loadWidget(i))
	    return false;
	boolean bool = false;
	Widget[] class39_sub5_sub17s
	    = Class62_Sub1.widgets[i];
	for (int i_14_ = 0; class39_sub5_sub17s.length > i_14_; i_14_++) {
	    Widget class39_sub5_sub17 = class39_sub5_sub17s[i_14_];
	    if (class39_sub5_sub17 != null
		&& class39_sub5_sub17.type == 6) {
		if (class39_sub5_sub17.anInt2103 != -1
		    || class39_sub5_sub17.anInt2052 != -1) {
		    boolean bool_15_
			= Huffmans.parseClientScript(class39_sub5_sub17);
		    int i_16_;
		    if (!bool_15_)
			i_16_ = class39_sub5_sub17.anInt2103;
		    else
			i_16_ = class39_sub5_sub17.anInt2052;
		    if (i_16_ != -1) {
			Class39_Sub5_Sub11 class39_sub5_sub11
			    = Class62_Sub1.method1064(i_16_, (byte) 54);
			class39_sub5_sub17.anInt2079 += Class45.anInt856;
			while ((class39_sub5_sub11.anIntArray1831
				[class39_sub5_sub17.anInt1999])
			       < class39_sub5_sub17.anInt2079) {
			    class39_sub5_sub17.anInt2079
				-= (class39_sub5_sub11.anIntArray1831
				    [class39_sub5_sub17.anInt1999]);
			    class39_sub5_sub17.anInt1999++;
			    bool = true;
			    if (class39_sub5_sub17.anInt1999
				>= class39_sub5_sub11.anIntArray1833.length) {
				class39_sub5_sub17.anInt1999
				    -= class39_sub5_sub11.anInt1839;
				if (class39_sub5_sub17.anInt1999 < 0
				    || (class39_sub5_sub17.anInt1999
					>= (class39_sub5_sub11
					    .anIntArray1833).length))
				    class39_sub5_sub17.anInt1999 = 0;
			    }
			}
		    }
		}
		if (class39_sub5_sub17.anInt2069 != 0
		    && !class39_sub5_sub17.aBoolean2013) {
		    bool = true;
		    int i_17_ = class39_sub5_sub17.anInt2069 << 16 >> 16;
		    int i_18_ = class39_sub5_sub17.anInt2069 >> 16;
		    i_18_ *= Class45.anInt856;
		    i_17_ *= Class45.anInt856;
		    class39_sub5_sub17.anInt2011
			= i_17_ + class39_sub5_sub17.anInt2011 & 0x7ff;
		    class39_sub5_sub17.anInt2098
			= class39_sub5_sub17.anInt2098 + i_18_ & 0x7ff;
		}
	    }
	}
	return bool;
    }
    
    public static void method460
	(int i, int i_19_, int i_20_, int i_21_, int i_22_, int i_23_,
	 int i_24_, int i_25_, int i_26_,
	 Widget[] class39_sub5_sub17s, int i_27_, int i_28_) {
	if (i_26_ <= i_27_ && i_22_ <= i_24_ && i_27_ < i_20_
	    && i_28_ > i_24_) {
	    if (i != -1)
		aClass3_1333 = null;
	    for (int i_29_ = 0; i_29_ < class39_sub5_sub17s.length; i_29_++) {
		Widget class39_sub5_sub17
		    = class39_sub5_sub17s[i_29_];
		if (class39_sub5_sub17 != null
		    && i_23_ == class39_sub5_sub17.anInt2050
		    && (!class39_sub5_sub17.aBoolean2013
			|| !class39_sub5_sub17
				.method754(121, HashTable.aBoolean361))) {
		    int i_30_ = class39_sub5_sub17.anInt2021 + (i_22_ - i_21_);
		    int i_31_ = -i_19_ + class39_sub5_sub17.anInt2091 + i_26_;
		    if ((class39_sub5_sub17.anInt2057 >= 0
			 || class39_sub5_sub17.anInt2041 != 0)
			&& i_31_ <= i_27_ && i_24_ >= i_30_
			&& i_31_ + class39_sub5_sub17.quadWidth > i_27_
			&& i_24_ < class39_sub5_sub17.quadHeight + i_30_) {
			if (class39_sub5_sub17.anInt2057 < 0)
			    Class48.anInt904 = i_29_;
			else
			    Class48.anInt904 = class39_sub5_sub17.anInt2057;
		    }
		    if (class39_sub5_sub17.type == 8 && i_31_ <= i_27_
			&& i_24_ >= i_30_
			&& class39_sub5_sub17.quadWidth + i_31_ > i_27_
			&& i_30_ + class39_sub5_sub17.quadHeight > i_24_)
			Projectile.anInt2197 = i_29_;
		    if (class39_sub5_sub17.type == 0) {
			if (!class39_sub5_sub17.aBoolean2013
			    && class39_sub5_sub17
				   .method754(i + 128, HashTable.aBoolean361)
			    && !ItemDefinition.method473(i_25_, -1, i_29_))
			    continue;
			method460(-1, class39_sub5_sub17.anInt2064,
				  i_31_ + class39_sub5_sub17.quadWidth,
				  class39_sub5_sub17.anInt1994, i_30_,
				  class39_sub5_sub17.anInt2084, i_24_, i_25_,
				  i_31_, class39_sub5_sub17s, i_27_,
				  i_30_ + class39_sub5_sub17.quadHeight);
			if (class39_sub5_sub17.aClass39_Sub5_Sub17Array2025
			    != null)
			    method460(-1, class39_sub5_sub17.anInt2064,
				      class39_sub5_sub17.quadWidth + i_31_,
				      class39_sub5_sub17.anInt1994, i_30_,
				      class39_sub5_sub17.anInt2084, i_24_,
				      i_25_, i_31_,
				      (class39_sub5_sub17
				       .aClass39_Sub5_Sub17Array2025),
				      i_27_,
				      i_30_ + class39_sub5_sub17.quadHeight);
			if ((class39_sub5_sub17.quadHeight
			     < class39_sub5_sub17.anInt2095)
			    && !class39_sub5_sub17.aBoolean2013)
			    method456((byte) 121,
				      i_31_ + class39_sub5_sub17.quadWidth,
				      i_27_, i_24_, i_25_,
				      class39_sub5_sub17.quadHeight, i_30_,
				      class39_sub5_sub17.anInt2095,
				      class39_sub5_sub17);
			if (!class39_sub5_sub17.aBoolean2013)
			    continue;
		    }
		    if (class39_sub5_sub17.anInt2089 == 1 && i_27_ >= i_31_
			&& i_24_ >= i_30_
			&& i_27_ < class39_sub5_sub17.quadWidth + i_31_
			&& class39_sub5_sub17.quadHeight + i_30_ > i_24_) {
			boolean bool = false;
			if (class39_sub5_sub17.anInt2078 != 0)
			    bool = Class39_Sub5_Sub9
				       .method602(class39_sub5_sub17, true);
			if (!bool)
			    JString.method55(0, class39_sub5_sub17.aClass3_2068,
					    class39_sub5_sub17.anInt2084,
					    Class66.blankString, (byte) -51,
					    0, 20);
		    }
		    if (class39_sub5_sub17.anInt2089 == 2
			&& !IsaacPrng.aBoolean1100 && i_31_ <= i_27_
			&& i_30_ <= i_24_
			&& class39_sub5_sub17.quadWidth + i_31_ > i_27_
			&& class39_sub5_sub17.quadHeight + i_30_ > i_24_) {
			JString class3
			    = class39_sub5_sub17.method762(HashTable.aBoolean361,
							   64);
			if (class3 != null) {
			    Class39_Sub11.anInt1473++;
			    JString.method55(-1, class3,
					    class39_sub5_sub17.anInt2084,
					    (Class39_Sub5_Sub11.method708
					     ((new JString[]
					       { Class41.aClass3_783,
						 (class39_sub5_sub17
						  .aClass3_2066) }))),
					    (byte) -72, 0, 14);
			}
		    }
		    if (class39_sub5_sub17.anInt2089 == 3 && i_27_ >= i_31_
			&& i_24_ >= i_30_
			&& i_31_ + class39_sub5_sub17.quadWidth > i_27_
			&& class39_sub5_sub17.quadHeight + i_30_ > i_24_) {
			int i_32_;
			if (i_25_ == 3)
			    i_32_ = 24;
			else
			    i_32_ = 29;
			JString.method55(0, StillGraphic.aClass3_2343,
					class39_sub5_sub17.anInt2084,
					Class66.blankString, (byte) -45, 0,
					i_32_);
		    }
		    if (class39_sub5_sub17.anInt2089 == 4 && i_27_ >= i_31_
			&& i_24_ >= i_30_
			&& i_31_ + class39_sub5_sub17.quadWidth > i_27_
			&& i_30_ + class39_sub5_sub17.quadHeight > i_24_)
			JString.method55(0, class39_sub5_sub17.aClass3_2068,
					class39_sub5_sub17.anInt2084,
					Class66.blankString, (byte) -118, 0,
					36);
		    if (class39_sub5_sub17.anInt2089 == 5 && i_31_ <= i_27_
			&& i_30_ <= i_24_
			&& i_27_ < class39_sub5_sub17.quadWidth + i_31_
			&& i_24_ < class39_sub5_sub17.quadHeight + i_30_)
			JString.method55(0, class39_sub5_sub17.aClass3_2068,
					class39_sub5_sub17.anInt2084,
					Class66.blankString, (byte) -89, 0,
					41);
		    if (class39_sub5_sub17.anInt2089 == 6
			&& Class39_Sub10.anInt1420 == -1 && i_31_ <= i_27_
			&& i_30_ <= i_24_
			&& i_31_ + class39_sub5_sub17.quadWidth > i_27_
			&& i_30_ + class39_sub5_sub17.quadHeight > i_24_) {
			Class4.anInt65++;
			JString.method55(-1, class39_sub5_sub17.aClass3_2068,
					class39_sub5_sub17.anInt2084,
					Class66.blankString, (byte) -6, 0, 8);
		    }
		    if (class39_sub5_sub17.type == 2) {
			int i_33_ = 0;
			for (int i_34_ = 0;
			     class39_sub5_sub17.quadHeight > i_34_; i_34_++) {
			    for (int i_35_ = 0;
				 class39_sub5_sub17.quadWidth > i_35_;
				 i_35_++) {
				int i_36_ = ((class39_sub5_sub17.anInt2000
					      + 32) * i_35_
					     + i_31_);
				int i_37_ = ((class39_sub5_sub17.anInt2010
					      + 32) * i_34_
					     + i_30_);
				if (i_33_ < 20) {
				    i_37_ += (class39_sub5_sub17.anIntArray2037
					      [i_33_]);
				    i_36_ += (class39_sub5_sub17.anIntArray2028
					      [i_33_]);
				}
				if (i_36_ <= i_27_ && i_24_ >= i_37_
				    && i_36_ + 32 > i_27_
				    && i_37_ + 32 > i_24_) {
				    Class41.anInt768
					= class39_sub5_sub17.anInt2084;
				    Class14.anInt231 = i_33_;
				    if ((class39_sub5_sub17.anIntArray2087
					 [i_33_])
					> 0) {
					ItemDefinition class39_sub5_sub1
					    = (Class26.getItemDefinition
					       ((class39_sub5_sub17
						 .anIntArray2087[i_33_]) - 1));
					if (Class13.anInt208 == 1
					    && class39_sub5_sub17
						   .method769(105)) {
					    if ((class39_sub5_sub17.anInt2084
						 != Class39_Sub10.anInt1430)
						|| i_33_ != Class23.anInt428)
						JString.method55
						    (i_33_,
						     (Class39_Sub5_Sub4_Sub4
						      .aClass3_2310),
						     (class39_sub5_sub17
						      .anInt2084),
						     (Class39_Sub5_Sub11
							  .method708
						      ((new JString[]
							{ (Class39_Sub10
							   .aClass3_1436),
							  HashTable.aClass3_375,
							  (class39_sub5_sub1
							   .aClass3_1661) }))),
						     (byte) -33,
						     (class39_sub5_sub1
						      .id),
						     18);
					} else if (!IsaacPrng.aBoolean1100
						   || !class39_sub5_sub17
							   .method769(82)) {
					    JString[] class3s
						= (class39_sub5_sub1
						   .aClass3Array1657);
					    if (Class45.aBoolean862)
						class3s
						    = (BufferedFile.method225
						       ((byte) 127, class3s));
					    if (class39_sub5_sub17
						    .method769(i ^ ~0x32)) {
						for (int i_38_ = 4; i_38_ >= 3;
						     i_38_--) {
						    if (class3s == null
							|| (class3s[i_38_]
							    == null)) {
							if (i_38_ == 4)
							    JString.method55
								(i_33_,
								 (Class39_Sub13
								  .aClass3_1509),
								 (class39_sub5_sub17
								  .anInt2084),
								 (Class39_Sub5_Sub11
								      .method708
								  ((new JString[]
								    { (FrameBuffer
								       .aClass3_2147),
								      (class39_sub5_sub1
								       .aClass3_1661) }))),
								 (byte) -103,
								 (class39_sub5_sub1
								  .id),
								 19);
						    } else {
							int i_39_;
							if (i_38_ == 3)
							    i_39_ = 32;
							else
							    i_39_ = 19;
							JString.method55
							    (i_33_,
							     class3s[i_38_],
							     (class39_sub5_sub17
							      .anInt2084),
							     (Class39_Sub5_Sub11
								  .method708
							      ((new JString[]
								{ (FrameBuffer
								   .aClass3_2147),
								  (class39_sub5_sub1
								   .aClass3_1661) }))),
							     (byte) -99,
							     (class39_sub5_sub1
							      .id),
							     i_39_);
						    }
						}
					    }
					    if (class39_sub5_sub17
						    .method758(i ^ ~0x51))
						JString.method55
						    (i_33_,
						     (Class39_Sub5_Sub4_Sub4
						      .aClass3_2310),
						     (class39_sub5_sub17
						      .anInt2084),
						     (Class39_Sub5_Sub11
							  .method708
						      ((new JString[]
							{ (FrameBuffer
							   .aClass3_2147),
							  (class39_sub5_sub1
							   .aClass3_1661) }))),
						     (byte) -73,
						     (class39_sub5_sub1
						      .id),
						     23);
					    if (class39_sub5_sub17
						    .method769(i ^ ~0x6c)
						&& class3s != null) {
						for (int i_40_ = 2; i_40_ >= 0;
						     i_40_--) {
						    if (class3s[i_40_]
							!= null) {
							int i_41_ = 0;
							if (i_40_ == 0)
							    i_41_ = 28;
							if (i_40_ == 1)
							    i_41_ = 16;
							if (i_40_ == 2)
							    i_41_ = 54;
							JString.method55
							    (i_33_,
							     class3s[i_40_],
							     (class39_sub5_sub17
							      .anInt2084),
							     (Class39_Sub5_Sub11
								  .method708
							      ((new JString[]
								{ (FrameBuffer
								   .aClass3_2147),
								  (class39_sub5_sub1
								   .aClass3_1661) }))),
							     (byte) -117,
							     (class39_sub5_sub1
							      .id),
							     i_41_);
						    }
						}
					    }
					    class3s = (class39_sub5_sub17
						       .aClass3Array2043);
					    if (Class45.aBoolean862)
						class3s
						    = (BufferedFile.method225
						       ((byte) 127, class3s));
					    if (class3s != null) {
						for (int i_42_ = 4; i_42_ >= 0;
						     i_42_--) {
						    if (class3s[i_42_]
							!= null) {
							int i_43_ = 0;
							if (i_42_ == 0)
							    i_43_ = 35;
							if (i_42_ == 1)
							    i_43_ = 30;
							if (i_42_ == 2)
							    i_43_ = 26;
							if (i_42_ == 3)
							    i_43_ = 25;
							if (i_42_ == 4)
							    i_43_ = 49;
							JString.method55
							    (i_33_,
							     class3s[i_42_],
							     (class39_sub5_sub17
							      .anInt2084),
							     (Class39_Sub5_Sub11
								  .method708
							      ((new JString[]
								{ (FrameBuffer
								   .aClass3_2147),
								  (class39_sub5_sub1
								   .aClass3_1661) }))),
							     (byte) -117,
							     (class39_sub5_sub1
							      .id),
							     i_43_);
						    }
						}
					    }
					    JString.method55
						(i_33_,
						 (RuntimeException_Sub1
						  .aClass3_1224),
						 class39_sub5_sub17.anInt2084,
						 (Class39_Sub5_Sub11.method708
						  ((new JString[]
						    { (FrameBuffer
						       .aClass3_2147),
						      (class39_sub5_sub1
						       .aClass3_1661) }))),
						 (byte) -53,
						 class39_sub5_sub1.id,
						 1004);
					} else if ((Class41.anInt776 & 0x10)
						   == 16)
					    JString.method55
						(i_33_, Client.aClass3_1273,
						 class39_sub5_sub17.anInt2084,
						 (Class39_Sub5_Sub11.method708
						  ((new JString[]
						    { Class14.aClass3_216,
						      HashTable.aClass3_375,
						      (class39_sub5_sub1
						       .aClass3_1661) }))),
						 (byte) -109,
						 class39_sub5_sub1.id,
						 5);
				    }
				}
				i_33_++;
			    }
			}
		    }
		    if (class39_sub5_sub17.aBoolean2013) {
			if (IsaacPrng.aBoolean1100) {
			    if (class39_sub5_sub17.method750(12117)
				&& (Class41.anInt776 & 0x20) == 32
				&& i_27_ >= i_31_ && i_24_ >= i_30_
				&& i_31_ + class39_sub5_sub17.quadWidth > i_27_
				&& (class39_sub5_sub17.quadHeight + i_30_
				    > i_24_))
				JString.method55(class39_sub5_sub17.anInt2102,
						Client.aClass3_1273,
						class39_sub5_sub17.anInt2084,
						(Class39_Sub5_Sub11.method708
						 ((new JString[]
						   { Class14.aClass3_216,
						     Class12.aClass3_192,
						     (class39_sub5_sub17
						      .aClass3_2065) }))),
						(byte) -91, 0, 39);
			} else if (i_27_ >= i_31_ && i_30_ <= i_24_
				   && (class39_sub5_sub17.quadWidth + i_31_
				       > i_27_)
				   && i_24_ < (class39_sub5_sub17.quadHeight
					       + i_30_)) {
			    for (int i_44_ = 9; i_44_ >= 5; i_44_--) {
				JString class3 = (class39_sub5_sub17.method771
						 (HashTable.aBoolean361,
						  (byte) 50, i_44_));
				if (class3 != null) {
				    Class39_Sub12.anInt1490++;
				    JString.method55
					(class39_sub5_sub17.anInt2102, class3,
					 class39_sub5_sub17.anInt2084,
					 class39_sub5_sub17.aClass3_2065,
					 (byte) -64, i_44_ + 1, 51);
				}
			    }
			    JString class3
				= class39_sub5_sub17
				      .method762(HashTable.aBoolean361, i + 65);
			    if (class3 != null) {
				Class39_Sub11.anInt1473++;
				JString.method55(class39_sub5_sub17.anInt2102,
						class3,
						class39_sub5_sub17.anInt2084,
						(class39_sub5_sub17
						 .aClass3_2065),
						(byte) -20, 0, 14);
			    }
			    for (int i_45_ = 4; i_45_ >= 0; i_45_--) {
				JString class3_46_
				    = (class39_sub5_sub17.method771
				       (HashTable.aBoolean361, (byte) -105,
					i_45_));
				if (class3_46_ != null) {
				    Class39_Sub12.anInt1490++;
				    JString.method55
					(class39_sub5_sub17.anInt2102,
					 class3_46_,
					 class39_sub5_sub17.anInt2084,
					 class39_sub5_sub17.aClass3_2065,
					 (byte) -54, i_45_ + 1, 51);
				}
			    }
			    if (class39_sub5_sub17.method768(125)) {
				Class4.anInt65++;
				JString.method55(-1,
						(class39_sub5_sub17
						 .aClass3_2068),
						class39_sub5_sub17.anInt2084,
						Class66.blankString,
						(byte) -126, 0, 8);
			    }
			}
		    }
		}
	    }
	}
    }
    
    static {
	scriptStateStack = new ScriptState[50];
	odBlockOffset = 0;
	anInt1329 = 0;
	anInt1336 = 0;
	aClass3_1339 = Class39_Sub5_Sub9.createJstring(")3");
	aClass3_1328 = aClass3_1324;
	aClass3_1333
	    = Class39_Sub5_Sub9.createJstring("Connecting to server)3)3)3");
	aClass3_1334 = aClass3_1333;
	aClass3_1340 = Class39_Sub5_Sub9.createJstring("backvmid2");
	aClass3_1332 = Class39_Sub5_Sub9.createJstring("System update in: ");
	aClass3_1326 = aClass3_1332;
    }
}
