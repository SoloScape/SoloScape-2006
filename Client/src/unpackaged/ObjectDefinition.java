package unpackaged;

import jagex.world.actors.GroundItem;
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.SubNode;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.JSocket;
import jagex.world.actors.Npc;
import jagex.world.map.TraversalMap;
import jagex.io.Buffer;

/* Class39_Sub5_Sub15 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class ObjectDefinition extends SubNode
{
    public int[] anIntArray1920;
    public static JString aClass3_1921
	= Class39_Sub5_Sub9.createJstring("Loading interfaces )2 ");
    public int anInt1922;
    public static int anInt1923;
    public int anInt1924;
    public int anInt1925;
    public static FileTable modelFileLoader;
    public static IndexedColorSprite aClass39_Sub5_Sub10_Sub4_1927;
    public boolean aBoolean1928 = true;
    public boolean aBoolean1929;
    public int anInt1930;
    public int anInt1931;
    public JString aClass3_1932 = Buffer.aClass3_1370;
    public int anInt1933;
    public int anInt1934;
    public static JSocket aClass16_1935;
    public int anInt1936;
    public static JString aClass3_1937;
    public int[] anIntArray1938;
    public int anInt1939;
    public static JString aClass3_1940
	= Class39_Sub5_Sub9.createJstring("Aus");
    public int anInt1941 = -1;
    public static int odStage;
    public int anInt1943;
    public int anInt1944;
    public int anInt1945;
    public static JString aClass3_1946;
    public static JString aClass3_1947;
    public int anInt1948;
    public boolean aBoolean1949;
    public boolean aBoolean1950;
    public boolean aBoolean1951;
    public int anInt1952;
    public boolean aBoolean1953;
    public int[] anIntArray1954;
    public int anInt1955;
    public int anInt1956;
    public static JString aClass3_1957;
    public int anInt1958;
    public boolean aBoolean1959;
    public int[] anIntArray1960;
    public int[] anIntArray1961;
    public int anInt1962;
    public static Resource aClass56_1963;
    public JString[] aClass3Array1964;
    public int anInt1965;
    public static JString aClass3_1966;
    public int anInt1967;
    public int anInt1968;
    public boolean aBoolean1969;
    public int[] anIntArray1970;
    public boolean aBoolean1971;
    public int anInt1972;
    public static JString aClass3_1973;
    public int anInt1974;
    
    public boolean method731(int i) {
	if (anIntArray1954 == null)
	    return true;
	boolean bool = true;
	for (int i_0_ = i; i_0_ < anIntArray1954.length; i_0_++)
	    bool &= (Npc.modelFileLoader.isArchiveLoaded
		     (anIntArray1954[i_0_] & 0xffff, 0));
	return bool;
    }
    
    public static void method732(int i) {
	Class39_Sub5_Sub9.anIntArray1809 = new int[104];
	NameTable.anIntArray187 = new int[104];
	Class39_Sub14.aByteArrayArrayArray1516 = new byte[4][104][104];
	Class14.anIntArray239 = new int[104];
	ItemDefinition.aByteArrayArrayArray1671 = new byte[4][105][105];
	IsaacPrng.aByteArrayArrayArray1081 = new byte[4][104][104];
	Class67.aByteArrayArrayArray1179 = new byte[4][104][104];
	TraversalMap.anInt518 = 99;
	ArchiveRequest.anIntArrayArrayArray1412 = new int[4][105][105];
	Class20.anIntArrayArray391 = new int[105][105];
	Class33.aByteArrayArrayArray596 = new byte[4][104][104];
	Class39_Sub5_Sub16.anIntArray1984 = new int[104];
	Class39_Sub5_Sub4_Sub2.anIntArray2231 = new int[104];
    }
    
    public ObjectDefinition method733(int i) {
	int i_1_ = -1;
	if (anInt1934 == -1) {
	    if (anInt1968 != -1)
		i_1_ = Class66.stateValues[anInt1968];
	} else
	    i_1_ = Class44.getVarbitValue(anInt1934);
	if (i_1_ < 0 || i_1_ >= anIntArray1961.length
	    || anIntArray1961[i_1_] == -1)
	    return null;
	return Canvas_Sub1.method40(anIntArray1961[i_1_], (byte) 112);
    }
    
    public boolean method734(int i) {
	if (anIntArray1961 == null) {
	    if (anInt1945 == -1 && anIntArray1920 == null)
		return false;
	    return true;
	}
	for (int i_2_ = 0; i_2_ < anIntArray1961.length; i_2_++) {
	    if (anIntArray1961[i_2_] != -1) {
		ObjectDefinition class39_sub5_sub15_3_
		    = Canvas_Sub1.method40(anIntArray1961[i_2_], (byte) 29);
		if (class39_sub5_sub15_3_.anInt1945 != -1
		    || class39_sub5_sub15_3_.anIntArray1920 != null)
		    return true;
	    }
	}
	return false;
    }
    
    public void decodeOpcode(boolean bool, Buffer class39_sub6, int i) {
	if (i == 1) {
	    int i_4_ = class39_sub6.getUbyte();
	    if (i_4_ > 0) {
		if (anIntArray1954 != null && !JMouseListener.aBoolean785)
		    class39_sub6.offset += i_4_ * 3;
		else {
		    anIntArray1954 = new int[i_4_];
		    anIntArray1960 = new int[i_4_];
		    for (int i_5_ = 0; i_4_ > i_5_; i_5_++) {
			anIntArray1954[i_5_] = class39_sub6.getUword();
			anIntArray1960[i_5_]
			    = class39_sub6.getUbyte();
		    }
		}
	    }
	} else if (i == 2)
	    aClass3_1932 = class39_sub6.getJstr();
	else if (i == 5) {
	    int i_6_ = class39_sub6.getUbyte();
	    if (i_6_ > 0) {
		if (anIntArray1954 != null && !JMouseListener.aBoolean785)
		    class39_sub6.offset += i_6_ * 2;
		else {
		    anIntArray1960 = null;
		    anIntArray1954 = new int[i_6_];
		    for (int i_7_ = 0; i_7_ < i_6_; i_7_++)
			anIntArray1954[i_7_] = class39_sub6.getUword();
		}
	    }
	} else if (i != 14) {
	    if (i == 15)
		anInt1948 = class39_sub6.getUbyte();
	    else if (i != 17) {
		if (i == 18)
		    aBoolean1949 = false;
		else if (i != 19) {
		    if (i != 21) {
			if (i != 22) {
			    if (i != 23) {
				if (i == 24) {
				    anInt1939 = class39_sub6.getUword();
				    if (anInt1939 == 65535)
					anInt1939 = -1;
				} else if (i == 28)
				    anInt1936
					= class39_sub6.getUbyte();
				else if (i != 29) {
				    if (i != 39) {
					if (i >= 30 && i < 35) {
					    aClass3Array1964[i - 30]
						= class39_sub6
						      .getJstr();
					    if (aClass3Array1964[i - 30]
						    .method97
						(FileLoader.aClass3_1299,
						 -66))
						aClass3Array1964[i - 30]
						    = null;
					} else if (i != 40) {
					    if (i != 60) {
						if (i == 62)
						    aBoolean1950 = true;
						else if (i == 64)
						    aBoolean1959 = false;
						else if (i != 65) {
						    if (i == 66)
							anInt1952
							    = (class39_sub6
								   .getUword
							       ());
						    else if (i != 67) {
							if (i == 68)
							    anInt1972
								= (class39_sub6
								       .getUword
								   ());
							else if (i == 69)
							    anInt1958
								= (class39_sub6
								       .getUbyte
								   ());
							else if (i != 70) {
							    if (i != 71) {
								if (i != 72) {
								    if (i
									== 73)
									aBoolean1969
									    = true;
								    else if (i
									     == 74)
									aBoolean1953
									    = true;
								    else if (i
									     != 75) {
									if (i
									    == 77) {
									    anInt1934 = class39_sub6.getUword();
									    if (anInt1934 == 65535)
										anInt1934 = -1;
									    anInt1968 = class39_sub6.getUword();
									    if (anInt1968 == 65535)
										anInt1968 = -1;
									    int i_8_ = class39_sub6.getUbyte();
									    anIntArray1961 = new int[i_8_ + 1];
									    for (int i_9_ = 0; i_9_ <= i_8_; i_9_++) {
										anIntArray1961[i_9_] = class39_sub6.getUword();
										if (anIntArray1961[i_9_] == 65535)
										    anIntArray1961[i_9_] = -1;
									    }
									} else if (i == 78) {
									    anInt1945 = class39_sub6.getUword();
									    anInt1924 = class39_sub6.getUbyte();
									} else if (i == 79) {
									    anInt1956 = class39_sub6.getUword();
									    anInt1930 = class39_sub6.getUword();
									    anInt1924 = class39_sub6.getUbyte();
									    int i_10_ = class39_sub6.getUbyte();
									    anIntArray1920 = new int[i_10_];
									    for (int i_11_ = 0; i_10_ > i_11_; i_11_++)
										anIntArray1920[i_11_] = class39_sub6.getUword();
									}
								    } else
									anInt1967
									    = class39_sub6.getUbyte();
								} else
								    anInt1922
									= (class39_sub6.getWord
									   ());
							    } else
								anInt1955
								    = (class39_sub6
									   .getWord
								       ());
							} else
							    anInt1933
								= (class39_sub6
								       .getWord
								   ());
						    } else
							anInt1944
							    = (class39_sub6
								   .getUword
							       ());
						} else
						    anInt1943
							= class39_sub6
							      .getUword();
					    } else
						anInt1962
						    = class39_sub6
							  .getUword();
					} else {
					    int i_12_
						= class39_sub6
						      .getUbyte();
					    anIntArray1970 = new int[i_12_];
					    anIntArray1938 = new int[i_12_];
					    for (int i_13_ = 0; i_13_ < i_12_;
						 i_13_++) {
						anIntArray1938[i_13_]
						    = class39_sub6
							  .getUword();
						anIntArray1970[i_13_]
						    = class39_sub6
							  .getUword();
					    }
					}
				    } else
					anInt1965
					    = class39_sub6.getByte() * 5;
				} else
				    anInt1974 = class39_sub6.getByte();
			    } else
				aBoolean1929 = true;
			} else
			    aBoolean1971 = true;
		    } else
			aBoolean1951 = true;
		} else
		    anInt1941 = class39_sub6.getUbyte();
	    } else
		aBoolean1928 = false;
	} else
	    anInt1925 = class39_sub6.getUbyte();
	if (bool) {
	    /* empty */
	}
    }
    
    public Model method736
	(int i, boolean bool, boolean bool_14_, byte i_15_, int i_16_) {
	Model class39_sub5_sub4_sub6 = null;
	if (anIntArray1960 == null) {
	    if (i_16_ != 10)
		return null;
	    if (anIntArray1954 == null)
		return null;
	    int i_17_ = anIntArray1954.length;
	    boolean bool_18_ = i > 3 ^ aBoolean1950;
	    for (int i_19_ = 0; i_17_ > i_19_; i_19_++) {
		int i_20_ = anIntArray1954[i_19_];
		if (bool_18_)
		    i_20_ += 65536;
		class39_sub5_sub4_sub6
		    = ((Model)
		       Class43.aClass7_811.get((long) i_20_));
		if (class39_sub5_sub4_sub6 == null) {
		    class39_sub5_sub4_sub6
			= (Model.getModel
			   (Npc.modelFileLoader,
			    i_20_ & 0xffff, 0));
		    if (class39_sub5_sub4_sub6 == null)
			return null;
		    if (bool_18_)
			class39_sub5_sub4_sub6.method543();
		    Class43.aClass7_811.put(class39_sub5_sub4_sub6,
						  (long) i_20_, (byte) -31);
		}
		if (i_17_ > 1)
		    GroundItem.aClass39_Sub5_Sub4_Sub6Array2240
			[i_19_]
			= class39_sub5_sub4_sub6;
	    }
	    if (i_17_ > 1)
		class39_sub5_sub4_sub6 = (new Model
					  ((GroundItem
					    .aClass39_Sub5_Sub4_Sub6Array2240),
					   i_17_));
	} else {
	    int i_21_ = -1;
	    for (int i_22_ = 0; i_22_ < anIntArray1960.length; i_22_++) {
		if (i_16_ == anIntArray1960[i_22_]) {
		    i_21_ = i_22_;
		    break;
		}
	    }
	    if (i_21_ == -1)
		return null;
	    int i_23_ = anIntArray1954[i_21_];
	    boolean bool_24_ = i > 3 ^ aBoolean1950;
	    if (bool_24_)
		i_23_ += 65536;
	    class39_sub5_sub4_sub6
		= ((Model)
		   Class43.aClass7_811.get((long) i_23_));
	    if (class39_sub5_sub4_sub6 == null) {
		class39_sub5_sub4_sub6
		    = (Model.getModel
		       (Npc.modelFileLoader,
			i_23_ & 0xffff, 0));
		if (class39_sub5_sub4_sub6 == null)
		    return null;
		if (bool_24_)
		    class39_sub5_sub4_sub6.method543();
		Class43.aClass7_811.put(class39_sub5_sub4_sub6,
					      (long) i_23_, (byte) -14);
	    }
	}
	boolean bool_25_;
	if (anInt1933 != 0 || anInt1955 != 0 || anInt1922 != 0)
	    bool_25_ = true;
	else
	    bool_25_ = false;
	if (i_15_ != 107)
	    return null;
	boolean bool_26_;
	if (anInt1943 != 128 || anInt1952 != 128 || anInt1944 != 128)
	    bool_26_ = true;
	else
	    bool_26_ = false;
	Model class39_sub5_sub4_sub6_27_
	    = new Model(class39_sub5_sub4_sub6,
					 i == 0 && !bool_26_ && !bool_25_,
					 anIntArray1938 == null, true);
	i &= 0x3;
	if (i != 1) {
	    if (i == 2)
		class39_sub5_sub4_sub6_27_.method562();
	    else if (i == 3)
		class39_sub5_sub4_sub6_27_.method551();
	} else
	    class39_sub5_sub4_sub6_27_.method555();
	if (anIntArray1938 != null) {
	    for (int i_28_ = 0; anIntArray1938.length > i_28_; i_28_++)
		class39_sub5_sub4_sub6_27_.method563(anIntArray1938[i_28_],
						     anIntArray1970[i_28_]);
	}
	if (bool_26_)
	    class39_sub5_sub4_sub6_27_.method559(anInt1943, anInt1952,
						 anInt1944);
	if (bool_25_)
	    class39_sub5_sub4_sub6_27_.method554(anInt1933, anInt1955,
						 anInt1922);
	if (bool)
	    class39_sub5_sub4_sub6_27_.method553();
	class39_sub5_sub4_sub6_27_.method548(anInt1974 + 64,
					     anInt1965 * 5 + 768, -50, -10,
					     -50, bool_14_);
	return class39_sub5_sub4_sub6_27_;
    }
    
    public Model method737
	(int i, int i_29_, int i_30_, int i_31_, int i_32_, int i_33_,
	 int i_34_, Class39_Sub5_Sub11 class39_sub5_sub11, int i_35_) {
	long l;
	if (anIntArray1960 == null)
	    l = (long) ((anInt1931 << 10) + i_34_);
	else
	    l = (long) (i_34_ + (anInt1931 << 10) + (i_31_ << 3));
	Model class39_sub5_sub4_sub6
	    = ((Model)
	       Class39_Sub5_Sub6.aClass7_1767.get(l));
	if (class39_sub5_sub4_sub6 == null) {
	    class39_sub5_sub4_sub6
		= method736(i_34_, true, true, (byte) 107, i_31_);
	    if (class39_sub5_sub4_sub6 == null)
		return null;
	    Class39_Sub5_Sub6.aClass7_1767.put(class39_sub5_sub4_sub6, l,
						     (byte) 116);
	}
	if (class39_sub5_sub11 == null && !aBoolean1951)
	    return class39_sub5_sub4_sub6;
	if (class39_sub5_sub11 == null)
	    class39_sub5_sub4_sub6 = class39_sub5_sub4_sub6.method567(true);
	else
	    class39_sub5_sub4_sub6
		= class39_sub5_sub11.method707(class39_sub5_sub4_sub6, -3,
					       i_30_, i_34_);
	if (aBoolean1951) {
	    int i_36_ = (i + (i_32_ + i_35_) + i_29_) / 4;
	    for (int i_37_ = 0; class39_sub5_sub4_sub6.anInt2367 > i_37_;
		 i_37_++) {
		int i_38_ = class39_sub5_sub4_sub6.anIntArray2376[i_37_];
		int i_39_ = class39_sub5_sub4_sub6.anIntArray2359[i_37_];
		int i_40_ = i + (i_38_ + 64) * (i_29_ - i) / 128;
		int i_41_ = (i_38_ + 64) * (-i_35_ + i_32_) / 128 + i_35_;
		int i_42_ = (i_40_ - i_41_) * (i_39_ + 64) / 128 + i_41_;
		class39_sub5_sub4_sub6.anIntArray2356[i_37_] += i_42_ - i_36_;
	    }
	}
	return class39_sub5_sub4_sub6;
    }
    
    public static void method738(int i) {
	aClass3_1937 = null;
	aClass3_1940 = null;
	aClass3_1957 = null;
	aClass3_1946 = null;
	aClass39_Sub5_Sub10_Sub4_1927 = null;
	aClass3_1966 = null;
	aClass3_1921 = null;
	aClass56_1963 = null;
	modelFileLoader = null;
	aClass16_1935 = null;
	aClass3_1973 = null;
	aClass3_1947 = null;
    }
    
    public void method739(boolean bool) {
	if (anInt1941 == -1) {
	    anInt1941 = 0;
	    if (anIntArray1954 != null
		&& (anIntArray1960 == null || anIntArray1960[0] == 10))
		anInt1941 = 1;
	    for (int i = 0; i < 5; i++) {
		if (aClass3Array1964[i] != null)
		    anInt1941 = 1;
	    }
	}
	if (anInt1967 == -1)
	    anInt1967 = !aBoolean1928 ? 0 : 1;
    }
    
    public boolean method740(int i, int i_43_) {
	if (anIntArray1960 != null) {
	    for (int i_44_ = 0; anIntArray1960.length > i_44_; i_44_++) {
		if (i_43_ == anIntArray1960[i_44_])
		    return (Npc.modelFileLoader.isArchiveLoaded
			    (anIntArray1954[i_44_] & 0xffff, 0));
	    }
	    return true;
	}
	if (anIntArray1954 == null)
	    return true;
	if (i_43_ != 10)
	    return true;
	boolean bool = true;
	for (int i_45_ = 0; anIntArray1954.length > i_45_; i_45_++)
	    bool &= (Npc.modelFileLoader.isArchiveLoaded
		     (anIntArray1954[i_45_] & 0xffff, 0));
	return bool;
    }
    
    public void decode(Buffer buffer) {
	for (;;) {
	    int opcode = buffer.getUbyte();
	    if (opcode == 0)
		break;
	    decodeOpcode(false, buffer, opcode);
	}
    }
    
    public Model method742(int i, int i_47_, int i_48_,
					    int i_49_, int i_50_, int i_51_,
					    int i_52_) {
	long l;
	if (anIntArray1960 != null)
	    l = (long) ((i_47_ << 3) + (anInt1931 << 10) + i_49_);
	else
	    l = (long) ((anInt1931 << 10) + i_49_);
	Model class39_sub5_sub4_sub6
	    = ((Model)
	       Varbit.aClass7_1796.get(l));
	if (class39_sub5_sub4_sub6 == null) {
	    class39_sub5_sub4_sub6
		= method736(i_49_, false, !aBoolean1971, (byte) 107, i_47_);
	    if (class39_sub5_sub4_sub6 == null)
		return null;
	    Varbit.aClass7_1796.put(class39_sub5_sub4_sub6, l,
						     (byte) 99);
	}
	if (aBoolean1951 || aBoolean1971)
	    class39_sub5_sub4_sub6
		= new Model(class39_sub5_sub4_sub6,
					     aBoolean1951, aBoolean1971);
	if (aBoolean1951) {
	    int i_53_ = (i_48_ + i_52_ + i_51_ + i_50_) / 4;
	    for (int i_54_ = 0; class39_sub5_sub4_sub6.anInt2367 > i_54_;
		 i_54_++) {
		int i_55_ = class39_sub5_sub4_sub6.anIntArray2376[i_54_];
		int i_56_ = class39_sub5_sub4_sub6.anIntArray2359[i_54_];
		int i_57_ = (i_55_ + 64) * (-i_48_ + i_52_) / 128 + i_48_;
		int i_58_ = (i_55_ + 64) * (i_51_ - i_50_) / 128 + i_50_;
		int i_59_ = i_57_ + (i_58_ - i_57_) * (i_56_ + 64) / 128;
		class39_sub5_sub4_sub6.anIntArray2356[i_54_] += -i_53_ + i_59_;
	    }
	}
	return class39_sub5_sub4_sub6;
    }
    
    public ObjectDefinition() {
	aBoolean1929 = false;
	anInt1934 = -1;
	anInt1948 = 1;
	anInt1936 = 16;
	anInt1922 = 0;
	anInt1952 = 128;
	aBoolean1953 = false;
	anInt1939 = -1;
	anInt1945 = -1;
	anInt1930 = 0;
	anInt1956 = 0;
	aBoolean1959 = true;
	aBoolean1951 = false;
	anInt1944 = 128;
	aBoolean1949 = true;
	anInt1958 = 0;
	anInt1955 = 0;
	aClass3Array1964 = new JString[5];
	anInt1943 = 128;
	anInt1924 = 0;
	anInt1965 = 0;
	anInt1962 = -1;
	anInt1925 = 1;
	aBoolean1950 = false;
	anInt1967 = -1;
	anInt1968 = -1;
	aBoolean1969 = false;
	aBoolean1971 = false;
	anInt1972 = -1;
	anInt1974 = 0;
	anInt1933 = 0;
    }
    
    static {
	aClass3_1937 = Class39_Sub5_Sub9.createJstring("redstone2");
	aClass3_1946 = Class39_Sub5_Sub9.createJstring("l");
	odStage = 0;
	aClass3_1947 = Class39_Sub5_Sub9.createJstring("gleiten:");
	aClass3_1957
	    = Class39_Sub5_Sub9.createJstring("No response from server)3");
	aClass3_1966 = aClass3_1921;
	aClass3_1973 = aClass3_1957;
    }
}
