package unpackaged;

/* Class6 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.io.BufferedFile;
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.world.actors.GroundItem;
import jagex.graphics.JImage;
import jagex.graphics.AbstractImage;
import jagex.world.actors.StillGraphic;
import jagex.world.actors.Projectile;
import jagex.utils.SubNode;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import java.awt.Graphics;

public class CacheIO
{
    public BufferedFile aClass18_92 = null;
    public BufferedFile aClass18_93 = null;
    public int id;
    public static HashTable pendingPriorityRequests;
    public static JString aClass3_96;
    public static int anInt97 = 0;
    public int anInt98 = 65000;
    public static int anInt99;
    public static JString aClass3_100;
    
    public static boolean method122(Signlink class21, int i, boolean bool) {
	ArchiveRequest.anInt1415 = 20;
	try {
	    Class55.aClass62_1251
		= (Class62) Class.forName("unpackaged.Class62_Sub1_Sub1").newInstance();
	    return true;
	} catch (Throwable throwable) {
	    Runnable_Impl1 runnable_impl1 = class21.method251(i ^ i);
	    if (runnable_impl1 != null) {
		Class55.aClass62_1251
		    = new Class62_Sub1_Sub2(class21, runnable_impl1);
		return true;
	    }
	    return false;
	}
    }
    
    public static void method123(Widget class39_sub5_sub17,
				 int i) {
	int i_0_ = class39_sub5_sub17.anInt2078;
	if (i_0_ >= 1 && i_0_ <= 100 || i_0_ >= 701 && i_0_ <= 800) {
	    if (Class14.anInt232 == 0) {
		if (i_0_ == 1) {
		    class39_sub5_sub17.aClass3_2029 = Class15.aClass3_286;
		    class39_sub5_sub17.anInt2089 = 0;
		    return;
		}
		if (i_0_ == 2) {
		    class39_sub5_sub17.aClass3_2029
			= Class39_Sub5_Sub4_Sub2.aClass3_2230;
		    class39_sub5_sub17.anInt2089 = 0;
		    return;
		}
	    }
	    if (Class14.anInt232 == 1) {
		if (i_0_ == 1) {
		    class39_sub5_sub17.aClass3_2029
			= SubNode.aClass3_1346;
		    class39_sub5_sub17.anInt2089 = 0;
		    return;
		}
		if (i_0_ == 2) {
		    class39_sub5_sub17.aClass3_2029
			= Class39_Sub5_Sub4_Sub2.aClass3_2222;
		    class39_sub5_sub17.anInt2089 = 0;
		    return;
		}
		if (i_0_ == 3) {
		    class39_sub5_sub17.aClass3_2029 = Class14.aClass3_256;
		    class39_sub5_sub17.anInt2089 = 0;
		    return;
		}
	    }
	    if (i_0_ > 700)
		i_0_ -= 601;
	    else
		i_0_--;
	    int i_1_ = Class4.anInt62;
	    if (Class14.anInt232 != 2)
		i_1_ = 0;
	    if (i_1_ <= i_0_) {
		class39_sub5_sub17.anInt2089 = 0;
		class39_sub5_sub17.aClass3_2029 = Class66.blankString;
	    } else {
		class39_sub5_sub17.aClass3_2029
		    = Projectile.aClass3Array2188[i_0_];
		class39_sub5_sub17.anInt2089 = 1;
	    }
	} else if (i_0_ >= 101 && i_0_ <= 200 || i_0_ >= 801 && i_0_ <= 900) {
	    if (i_0_ > 800)
		i_0_ -= 701;
	    else
		i_0_ -= 101;
	    int i_2_ = Class4.anInt62;
	    if (Class14.anInt232 != 2)
		i_2_ = 0;
	    if (i_0_ >= i_2_) {
		class39_sub5_sub17.aClass3_2029 = Class66.blankString;
		class39_sub5_sub17.anInt2089 = 0;
	    } else {
		if (Player.anIntArray2533[i_0_] == 0)
		    class39_sub5_sub17.aClass3_2029
			= Class39_Sub5_Sub11.method708((new JString[]
							{ (Class39_Sub10
							   .aClass3_1421),
							  (Class45
							   .aClass3_864) }));
		else if (Player.anIntArray2533[i_0_]
			 >= 5000) {
		    if (Player.anIntArray2533[i_0_]
			!= BufferedFile.worldId)
			class39_sub5_sub17.aClass3_2029
			    = (Class39_Sub5_Sub11.method708
			       ((new JString[]
				 { HashTable.aClass3_368, Class44.aClass3_847,
				   (AbstractImage.method1007
				    ((byte) 71,
				     (Player
				      .anIntArray2533[i_0_]) - 5000)) })));
		    else
			class39_sub5_sub17.aClass3_2029
			    = (Class39_Sub5_Sub11.method708
			       ((new JString[]
				 { Class41.aClass3_783, Class44.aClass3_847,
				   (AbstractImage.method1007
				    ((byte) 71,
				     (Player
				      .anIntArray2533[i_0_]) - 5000)) })));
		} else if (Player.anIntArray2533[i_0_]
			   != BufferedFile.worldId)
		    class39_sub5_sub17.aClass3_2029
			= (Class39_Sub5_Sub11.method708
			   ((new JString[]
			     { HashTable.aClass3_368, Class39_Sub11.aClass3_1465,
			       AbstractImage.method1007((byte) 71,
						  (Player
						   .anIntArray2533[i_0_])) })));
		else
		    class39_sub5_sub17.aClass3_2029
			= (Class39_Sub5_Sub11.method708
			   ((new JString[]
			     { Class41.aClass3_783, Class39_Sub11.aClass3_1465,
			       AbstractImage.method1007((byte) 71,
						  (Player
						   .anIntArray2533[i_0_])) })));
		class39_sub5_sub17.anInt2089 = 1;
	    }
	} else if (i_0_ == 203) {
	    int i_3_ = Class4.anInt62;
	    if (Class14.anInt232 != 2)
		i_3_ = 0;
	    class39_sub5_sub17.anInt2095 = i_3_ * 15 + 20;
	    if (class39_sub5_sub17.quadHeight >= class39_sub5_sub17.anInt2095)
		class39_sub5_sub17.anInt2095
		    = class39_sub5_sub17.quadHeight + 1;
	} else if (i_0_ >= 401 && i_0_ <= 500) {
	    i_0_ -= 401;
	    if (i_0_ == 0 && Class14.anInt232 == 0) {
		class39_sub5_sub17.anInt2089 = 0;
		class39_sub5_sub17.aClass3_2029
		    = StillGraphic.aClass3_2353;
	    } else if (i_0_ == 1 && Class14.anInt232 == 0) {
		class39_sub5_sub17.aClass3_2029
		    = Class39_Sub5_Sub4_Sub2.aClass3_2230;
		class39_sub5_sub17.anInt2089 = 0;
	    } else {
		int i_4_ = Class15.amountIgnores;
		if (Class14.anInt232 == 0)
		    i_4_ = 0;
		if (i_4_ <= i_0_) {
		    class39_sub5_sub17.aClass3_2029 = Class66.blankString;
		    class39_sub5_sub17.anInt2089 = 0;
		} else {
		    class39_sub5_sub17.aClass3_2029
			= Deque.decodeBase37
			      (Class39_Sub5_Sub9.ignoreUsernames[i_0_])
			      .formatUsername();
		    class39_sub5_sub17.anInt2089 = 1;
		}
	    }
	} else if (i_0_ == 503) {
	    class39_sub5_sub17.anInt2095 = Class15.amountIgnores * 15 + 20;
	    if (class39_sub5_sub17.quadHeight >= class39_sub5_sub17.anInt2095)
		class39_sub5_sub17.anInt2095
		    = class39_sub5_sub17.quadHeight + 1;
	} else if (i_0_ == 324) {
	    if (FileLoader.anInt1287 == -1) {
		Class23.anInt436 = class39_sub5_sub17.anInt2034;
		FileLoader.anInt1287 = class39_sub5_sub17.anInt2093;
	    }
	    if (!ClientScript.aClass45_1705.aBoolean854)
		class39_sub5_sub17.anInt2093 = Class23.anInt436;
	    else
		class39_sub5_sub17.anInt2093 = FileLoader.anInt1287;
	} else if (i_0_ == 325) {
	    if (FileLoader.anInt1287 == -1) {
		Class23.anInt436 = class39_sub5_sub17.anInt2034;
		FileLoader.anInt1287 = class39_sub5_sub17.anInt2093;
	    }
	    if (ClientScript.aClass45_1705.aBoolean854)
		class39_sub5_sub17.anInt2093 = Class23.anInt436;
	    else
		class39_sub5_sub17.anInt2093 = FileLoader.anInt1287;
	} else if (i_0_ == 327) {
	    class39_sub5_sub17.anInt2098 = 150;
	    class39_sub5_sub17.anInt2011
		= ((int) (Math.sin((double) Class2.logicCycle / 40.0) * 256.0)
		   & 0x7ff);
	    class39_sub5_sub17.anInt2009 = 5;
	    class39_sub5_sub17.anInt2026 = 0;
	} else if (i_0_ == 328) {
	    class39_sub5_sub17.anInt2098 = 150;
	    class39_sub5_sub17.anInt2011
		= ((int) (Math.sin((double) Class2.logicCycle / 40.0) * 256.0)
		   & 0x7ff);
	    class39_sub5_sub17.anInt2009 = 5;
	    class39_sub5_sub17.anInt2026 = 1;
	} else {
	    int i_5_ = -5 % ((i - 52) / 55);
	    if (i_0_ == 600)
		class39_sub5_sub17.aClass3_2029
		    = Class39_Sub5_Sub11.method708((new JString[]
						    { Class66.aClass3_1163,
						      Class4.aClass3_73 }));
	    else if (i_0_ == 620) {
		if (anInt97 >= 1) {
		    if (Class39_Sub12.aBoolean1487) {
			class39_sub5_sub17.aClass3_2029 = Huffmans.aClass3_751;
			class39_sub5_sub17.activeQuadColor = 16711680;
		    } else {
			class39_sub5_sub17.aClass3_2029 = Class10.aClass3_173;
			class39_sub5_sub17.activeQuadColor = 16777215;
		    }
		} else
		    class39_sub5_sub17.aClass3_2029 = Class66.blankString;
	    }
	}
    }
    
    public boolean insert(byte[] src, int i_6_, int i_7_) {
	synchronized (aClass18_93) {
	    if (i_7_ < 0 || anInt98 < i_7_)
		throw new IllegalArgumentException();
	    boolean bool = insert((byte) -122, i_6_, true, i_7_, src);
	    if (!bool)
		bool = insert((byte) -70, i_6_, false, i_7_, src);
	    return bool;
	}
    }
    
    public boolean insert(byte i, int i_8_, boolean bool, int i_9_,
			     byte[] is) {
	synchronized (aClass18_93) {
	    try {
		if (i >= -37)
		    aClass3_100 = null;
		int i_10_;
		if (!bool) {
		    i_10_
			= (int) ((aClass18_93.method228(-122) - -519L) / 520L);
		    if (i_10_ == 0)
			i_10_ = 1;
		} else {
		    if (aClass18_92.method228(-85) < (long) (i_8_ * 6 + 6))
			return false;
		    aClass18_92.method224(0, (long) (i_8_ * 6));
		    aClass18_92.method227(HashTable.chunkBuffer, 6, -13443, 0);
		    i_10_ = ((HashTable.chunkBuffer[3] << 16 & 0xff0000)
			     + ((HashTable.chunkBuffer[4] & 0xff) << 8)
			     + (HashTable.chunkBuffer[5] & 0xff));
		    if (i_10_ <= 0
			|| (long) i_10_ > aClass18_93.method228(92) / 520L)
			return false;
		}
		HashTable.chunkBuffer[5] = (byte) i_10_;
		HashTable.chunkBuffer[4] = (byte) (i_10_ >> 8);
		HashTable.chunkBuffer[0] = (byte) (i_9_ >> 16);
		int i_11_ = 0;
		HashTable.chunkBuffer[1] = (byte) (i_9_ >> 8);
		HashTable.chunkBuffer[2] = (byte) i_9_;
		HashTable.chunkBuffer[3] = (byte) (i_10_ >> 16);
		aClass18_92.method224(0, (long) (i_8_ * 6));
		aClass18_92.write(HashTable.chunkBuffer,0, 6);
		int i_12_ = 0;
		int i_13_;
		for (/**/; i_9_ > i_11_; i_11_ += i_13_) {
		    int i_14_ = 0;
		    if (bool) {
			aClass18_93.method224(0, (long) (i_10_ * 520));
			try {
			    aClass18_93.method227(HashTable.chunkBuffer, 8,
						  -13443, 0);
			} catch (java.io.EOFException eofexception) {
			    break;
			}
			i_14_ = ((HashTable.chunkBuffer[6] & 0xff)
				 + ((HashTable.chunkBuffer[5] << 8 & 0xff00)
				    + (HashTable.chunkBuffer[4] << 16
				       & 0xff0000)));
			i_13_ = ((HashTable.chunkBuffer[0] << 8 & 0xff00)
				 + (HashTable.chunkBuffer[1] & 0xff));
			int i_15_ = HashTable.chunkBuffer[7] & 0xff;
			int i_16_ = (((HashTable.chunkBuffer[2] & 0xff) << 8)
				     + (HashTable.chunkBuffer[3] & 0xff));
			if (i_13_ != i_8_ || i_16_ != i_12_
			    || id != i_15_)
			    return false;
			if (i_14_ < 0 || ((long) i_14_
					  > aClass18_93.method228(109) / 520L))
			    return false;
		    }
		    if (i_14_ == 0) {
			bool = false;
			i_14_ = (int) ((aClass18_93.method228(122) + 519L)
				       / 520L);
			if (i_14_ == 0)
			    i_14_++;
			if (i_10_ == i_14_)
			    i_14_++;
		    }
		    HashTable.chunkBuffer[3] = (byte) i_12_;
		    i_13_ = -i_11_ + i_9_;
		    HashTable.chunkBuffer[2] = (byte) (i_12_ >> 8);
		    if (i_9_ - i_11_ <= 512)
			i_14_ = 0;
		    HashTable.chunkBuffer[6] = (byte) i_14_;
		    HashTable.chunkBuffer[4] = (byte) (i_14_ >> 16);
		    i_12_++;
		    HashTable.chunkBuffer[5] = (byte) (i_14_ >> 8);
		    HashTable.chunkBuffer[7] = (byte) id;
		    if (i_13_ > 512)
			i_13_ = 512;
		    HashTable.chunkBuffer[0] = (byte) (i_8_ >> 8);
		    HashTable.chunkBuffer[1] = (byte) i_8_;
		    aClass18_93.method224(0, (long) (i_10_ * 520));
		    i_10_ = i_14_;
		    aClass18_93.write(HashTable.chunkBuffer,0, 8);
		    aClass18_93.write(is, i_11_, i_13_);
		}
		return true;
	    } catch (java.io.IOException ioexception) {
		return false;
	    }
	}
    }
    
    public byte[] lookup(int id) {
	synchronized (aClass18_93) {
	    try {
		if (aClass18_92.method228(115) < (long) (id * 6 + 6))
		    return null;
		aClass18_92.method224(0, (long) (id * 6));
		aClass18_92.method227(HashTable.chunkBuffer, 6, -13443, 0);
		int i_18_
		    = ((HashTable.chunkBuffer[2] & 0xff)
		       + (((HashTable.chunkBuffer[1] & 0xff) << 8)
			  + (HashTable.chunkBuffer[0] << 16 & 0xff0000)));
		int i_19_ = ((HashTable.chunkBuffer[5] & 0xff)
			     + ((HashTable.chunkBuffer[4] << 8 & 0xff00)
				+ ((HashTable.chunkBuffer[3] & 0xff) << 16)));
		if (i_18_ < 0 || i_18_ > anInt98)
		    return null;
		if (i_19_ <= 0
		    || (long) i_19_ > aClass18_93.method228(-125) / 520L)
		    return null;
		int i_20_ = 0;
		int i_21_ = 0;
		byte[] is = new byte[i_18_];
		while (i_21_ < i_18_) {
		    if (i_19_ == 0)
			return null;
		    int i_22_ = i_18_ - i_21_;
		    aClass18_93.method224(0, (long) (i_19_ * 520));
		    if (i_22_ > 512)
			i_22_ = 512;
		    aClass18_93.method227(HashTable.chunkBuffer, i_22_ + 8,
					  -13443, 0);
		    int i_23_ = ((HashTable.chunkBuffer[6] & 0xff)
				 + ((HashTable.chunkBuffer[4] & 0xff) << 16)
				 + ((HashTable.chunkBuffer[5] & 0xff) << 8));
		    int i_24_ = HashTable.chunkBuffer[7] & 0xff;
		    int i_25_ = ((HashTable.chunkBuffer[3] & 0xff)
				 + ((HashTable.chunkBuffer[2] & 0xff) << 8));
		    int i_26_ = ((HashTable.chunkBuffer[1] & 0xff)
				 + (HashTable.chunkBuffer[0] << 8 & 0xff00));
		    if (i_26_ != id || i_20_ != i_25_ || this.id != i_24_)
			return null;
		    if (i_23_ < 0
			|| aClass18_93.method228(-100) / 520L < (long) i_23_)
			return null;
		    for (int i_27_ = 0; i_22_ > i_27_; i_27_++)
			is[i_21_++] = HashTable.chunkBuffer[i_27_ + 8];
		    i_20_++;
		    i_19_ = i_23_;
		}
		return is;
	    } catch (java.io.IOException ioexception) {
		return null;
	    }
	}
    }
    
    @Override
    public String toString() {
	return "Cache:" + id;
    }
    
    public static void method127(int i) {
	try {
	    Graphics graphics = Class41.aCanvas778.getGraphics();
	    TraversalMap.aClass57_514.draw(graphics, i, 357);
	} catch (Exception exception) {
	    Class41.aCanvas778.repaint();
	}
    }
    
    public static void method128(byte i) {
	if (Class20.anInt397 > 0) {
	    for (int i_28_ = 0; i_28_ < 256; i_28_++) {
		if (Class20.anInt397 > 768)
		    GroundItem.anIntArray2245[i_28_]
			= JImage.method1011(BufferedFile.anIntArray338[i_28_],
						  -Class20.anInt397 + 1024,
						  Class23.anIntArray423[i_28_],
						  (byte) -27);
		else if (Class20.anInt397 > 256)
		    GroundItem.anIntArray2245[i_28_]
			= Class23.anIntArray423[i_28_];
		else
		    GroundItem.anIntArray2245[i_28_]
			= JImage.method1011(Class23.anIntArray423[i_28_],
						  -Class20.anInt397 + 256,
						  BufferedFile.anIntArray338[i_28_],
						  (byte) 122);
	    }
	} else if (Canvas_Sub1.anInt24 <= 0) {
	    for (int i_29_ = 0; i_29_ < 256; i_29_++)
		GroundItem.anIntArray2245[i_29_]
		    = BufferedFile.anIntArray338[i_29_];
	} else {
	    for (int i_30_ = 0; i_30_ < 256; i_30_++) {
		if (Canvas_Sub1.anInt24 <= 768) {
		    if (Canvas_Sub1.anInt24 > 256)
			GroundItem.anIntArray2245[i_30_]
			    = GroundItem.anIntArray2247[i_30_];
		    else
			GroundItem.anIntArray2245[i_30_]
			    = (JImage.method1011
			       (GroundItem.anIntArray2247[i_30_],
				-Canvas_Sub1.anInt24 + 256,
				BufferedFile.anIntArray338[i_30_], (byte) -100));
		} else
		    GroundItem.anIntArray2245[i_30_]
			= JImage.method1011(BufferedFile.anIntArray338[i_30_],
						  -Canvas_Sub1.anInt24 + 1024,
						  (GroundItem
						   .anIntArray2247[i_30_]),
						  (byte) -64);
	    }
	}
	int i_31_ = 256;
	for (int i_32_ = 0; i_32_ < 33920; i_32_++)
	    Class31.aClass57_551.buffer[i_32_]
		= Deque.aClass39_Sub5_Sub10_Sub3_920.anIntArray2476[i_32_];
	int i_33_ = 0;
	int i_34_ = 1152;
	for (int i_35_ = 1; i_35_ < i_31_ - 1; i_35_++) {
	    int i_36_
		= (-i_35_ + i_31_) * Huffmans.anIntArray762[i_35_] / i_31_;
	    int i_37_ = 22 + i_36_;
	    if (i_37_ < 0)
		i_37_ = 0;
	    i_33_ += i_37_;
	    for (int i_38_ = i_37_; i_38_ < 128; i_38_++) {
		int i_39_ = ArchiveWorker.anIntArray1202[i_33_++];
		if (i_39_ != 0) {
		    int i_40_ = i_39_;
		    int i_41_ = Class31.aClass57_551.buffer[i_34_];
		    int i_42_ = -i_39_ + 256;
		    i_39_ = GroundItem.anIntArray2245[i_39_];
		    Class31.aClass57_551.buffer[i_34_++]
			= (Class34.method342((i_40_ * Class34.method342(i_39_,
									65280)
					      + (i_42_
						 * Class34.method342(65280,
								     i_41_))),
					     16711680)
			   + (Class34.method342
			      (-16711936,
			       (i_40_ * Class34.method342(16711935, i_39_)
				+ i_42_ * Class34.method342(i_41_,
							    16711935))))) >> 8;
		} else
		    i_34_++;
	    }
	    i_34_ += i_37_;
	}
	int i_43_ = -58 % ((-21 - i) / 52);
	i_33_ = 0;
	for (int i_44_ = 0; i_44_ < 33920; i_44_++)
	    Class68.aClass57_1186.buffer[i_44_]
		= IsaacPrng.aClass39_Sub5_Sub10_Sub3_1096.anIntArray2476[i_44_];
	i_34_ = 1176;
	for (int i_45_ = 1; i_45_ < i_31_ - 1; i_45_++) {
	    int i_46_
		= (-i_45_ + i_31_) * Huffmans.anIntArray762[i_45_] / i_31_;
	    int i_47_ = -i_46_ + 103;
	    i_34_ += i_46_;
	    for (int i_48_ = 0; i_48_ < i_47_; i_48_++) {
		int i_49_ = ArchiveWorker.anIntArray1202[i_33_++];
		if (i_49_ != 0) {
		    int i_50_ = i_49_;
		    int i_51_ = 256 - i_49_;
		    i_49_ = GroundItem.anIntArray2245[i_49_];
		    int i_52_ = Class68.aClass57_1186.buffer[i_34_];
		    Class68.aClass57_1186.buffer[i_34_++]
			= ((Class34.method342(-16711936,
					      ((Class34.method342(i_49_,
								  16711935)
						* i_50_)
					       + (i_51_
						  * Class34.method342(16711935,
								      i_52_))))
			    + (Class34.method342
			       (16711680,
				(i_51_ * Class34.method342(i_52_, 65280)
				 + i_50_ * Class34.method342(65280, i_49_)))))
			   >> 8);
		} else
		    i_34_++;
	    }
	    i_34_ += -i_47_ + 128 - i_46_;
	    i_33_ += 128 - i_47_;
	}
    }
    
    public static void method129(boolean bool) {
	aClass3_100 = null;
	pendingPriorityRequests = null;
	aClass3_96 = null;
    }
    
    public CacheIO(int i, BufferedFile class18, BufferedFile class18_53_, int i_54_) {
	aClass18_92 = class18_53_;
	anInt98 = i_54_;
	id = i;
	aClass18_93 = class18;
    }
    
    static {
	aClass3_96 = Class39_Sub5_Sub9.createJstring("Empf-=nger:");
	anInt99 = 0;
	pendingPriorityRequests = new HashTable(32);
	aClass3_100
	    = (Class39_Sub5_Sub9.createJstring
	       ("Um ein neues Spielkonto zu erstellen)1 m-Ussen Sie"));
    }
}
