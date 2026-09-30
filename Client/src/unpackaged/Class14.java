package unpackaged;

/* Class14 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.HashTable;
import jagex.graphics.AbstractImage;
import jagex.world.actors.StillGraphic;
import jagex.utils.JString;
import jagex.world.actors.Player;
import jagex.world.map.TraversalMap;
import jagex.utils.Deque;
import jagex.io.Buffer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;

public class Class14
{
    public static JString aClass3_210;
    public static Player[] aClass39_Sub5_Sub4_Sub4_Sub2Array211;
    public static JString aClass3_212;
    public static HashTable aClass19_213;
    public static JString aClass3_214;
    public static JString aClass3_215;
    public static JString aClass3_216 = null;
    public static JString aClass3_217;
    public static JString aClass3_218;
    public static JString aClass3_219;
    public static JString aClass3_220;
    public static int[][] anIntArrayArray221;
    public static JString aClass3_222;
    public static JString aClass3_223;
    public static int anInt224;
    public static JString aClass3_225;
    public static JString aClass3_226;
    public static int anInt227;
    public static JString aClass3_228;
    public static CacheIO tableCache;
    public static JString aClass3_230;
    public static int anInt231;
    public static int anInt232;
    public static JString aClass3_233;
    public static JString aClass3_234;
    public static JString aClass3_235;
    public static JString aClass3_236;
    public static JString aClass3_237;
    public static JString aClass3_238;
    public static int[] anIntArray239;
    public static JString aClass3_240;
    public static JString aClass3_241;
    public static JString aClass3_242;
    public static JString aClass3_243;
    public static JString aClass3_244;
    public static boolean aBoolean245;
    public static AbstractImage aClass57_246;
    public static JString aClass3_247;
    public static JString aClass3_248;
    public static JString aClass3_249;
    public static JString aClass3_250;
    public static JString aClass3_251;
    public static JString aClass3_252;
    public static JString aClass3_253;
    public static JString aClass3_254;
    public static JString aClass3_255;
    public static JString aClass3_256;
    public static JString aClass3_257;
    public static JString aClass3_258;
    public static JString aClass3_259;
    public static JString aClass3_260;
    public static JString aClass3_261;
    public static JString aClass3_262;
    public static JString aClass3_263;
    public static int anInt264;
    public static JString aClass3_265;
    public static JString aClass3_266;
    public static JString aClass3_267;
    public static JString aClass3_268;
    public static JString aClass3_269;
    public static JString aClass3_270;
    public static JString aClass3_271;
    public static JString aClass3_272;
    public static JString aClass3_273;
    
    public static boolean method207(boolean bool, int i, int i_0_, byte[] is) {
	boolean bool_1_ = true;
	int i_2_ = -1;
	Buffer class39_sub6 = new Buffer(is);
	for (;;) {
	    int i_3_ = class39_sub6.getSmartB();
	    if (i_3_ == 0)
		break;
	    i_2_ += i_3_;
	    boolean bool_4_ = false;
	    int i_5_ = 0;
	    for (;;) {
		if (bool_4_) {
		    int i_6_ = class39_sub6.getSmartB();
		    if (i_6_ == 0)
			break;
		    class39_sub6.getUbyte();
		} else {
		    int i_7_ = class39_sub6.getSmartB();
		    if (i_7_ == 0)
			break;
		    i_5_ += i_7_ - 1;
		    int i_8_ = i_5_ >> 6 & 0x3f;
		    int i_9_ = class39_sub6.getUbyte() >> 2;
		    int i_10_ = i_5_ & 0x3f;
		    int i_11_ = i_10_ + i;
		    int i_12_ = i_8_ + i_0_;
		    if (i_12_ > 0 && i_11_ > 0 && i_12_ < 103 && i_11_ < 103) {
			ObjectDefinition class39_sub5_sub15
			    = Canvas_Sub1.method40(i_2_, (byte) 122);
			if (i_9_ != 22 || !Class45.aBoolean867
			    || class39_sub5_sub15.anInt1941 != 0
			    || class39_sub5_sub15.aBoolean1969) {
			    if (!class39_sub5_sub15.method731(0)) {
				bool_1_ = false;
				RuntimeException_Sub1.anInt1222++;
			    }
			    bool_4_ = true;
			}
		    }
		}
	    }
	}
	return bool_1_;
    }
    
    public static void method208(boolean bool) {
	Class53.itemDefinitionCache.method134(27392);
	StillGraphic.aClass7_2324.method134(27392);
	ItemDefinition.aClass7_1663.method134(27392);
    }
    
    public static void method209(int i, int i_13_, int i_14_, byte[] is,
				 TraversalMap[] class27s, int i_15_, int i_16_,
				 int i_17_, int i_18_, int i_19_) {
	for (int i_20_ = 0; i_20_ < 8; i_20_++) {
	    for (int i_21_ = 0; i_21_ < 8; i_21_++) {
		if (i_16_ + i_20_ > 0 && i_20_ + i_16_ < 103
		    && i_21_ + i_19_ > 0 && i_21_ + i_19_ < 103)
		    class27s[i_14_].adjancency[i_16_ + i_20_]
			[i_21_ + i_19_]
			= Class34.method342((class27s[i_14_].adjancency
					     [i_16_ + i_20_][i_21_ + i_19_]),
					    -16777217);
	    }
	}
	Buffer class39_sub6 = new Buffer(is);
	for (int i_22_ = 0; i_22_ < 4; i_22_++) {
	    for (int i_23_ = 0; i_23_ < 64; i_23_++) {
		for (int i_24_ = 0; i_24_ < 64; i_24_++) {
		    if (i_22_ == i_18_ && i_23_ >= i_13_ && i_23_ < i_13_ + 8
			&& i_24_ >= i_17_ && i_17_ + 8 > i_24_)
			Deque.loadMapTile
			    (class39_sub6, i_15_, 0,
			     i_19_ + Class39_Sub7.method848(i_23_ & 0x7, i_15_,
							    (byte) -93,
							    i_24_ & 0x7),
			     0,
			     JMouseListener.method901(i_15_, i_23_ & 0x7, i_24_ & 0x7,
					       true) + i_16_,
			     i_14_);
		    else
			Deque.loadMapTile(class39_sub6, 0, 0, -1, 0,
					  -1, 0);
		}
	    }
	}
    }
    
    public static int method210(int i, int i_25_, int i_26_, int i_27_,
				int i_28_, int i_29_, int i_30_) {
	i_28_ &= 0x3;
	if ((i_27_ & 0x1) == 1) {
	    int i_31_ = i;
	    i = i_26_;
	    i_26_ = i_31_;
	}
	if (i_28_ == i_30_)
	    return i_29_;
	if (i_28_ == 1)
	    return 1 - (i + i_25_ - 7);
	if (i_28_ == 2)
	    return -i_26_ - (-1 + i_29_ - 7);
	return i_25_;
    }
    
    public static byte[] unpackContainer(byte[] src) {
	Buffer buffer = new Buffer(src);
	int compression = buffer.getUbyte();
	int i_33_ = buffer.getDword();
	if (i_33_ < 0
	    || Class39_Sub14.anInt1517 != 0 && i_33_ > Class39_Sub14.anInt1517)
	    throw new RuntimeException();
	if (compression != 0) {
	    int i_34_ = buffer.getDword();
	    if (i_34_ < 0 || (Class39_Sub14.anInt1517 != 0 && Class39_Sub14.anInt1517 < i_34_))
		throw new RuntimeException();
	    byte[] is_35_ = new byte[i_34_];
	    if (compression != 1) {
		try {
		    DataInputStream datainputstream
			= (new DataInputStream
			   (new GZIPInputStream
			    (new ByteArrayInputStream(src, 9, i_33_))));
		    datainputstream.readFully(is_35_);
		    datainputstream.close();
		} catch (java.io.IOException ioexception) {
		    /* empty */
		}
	    } else
		Bzip2Decompressor.method269(is_35_, i_34_, src, i_33_, 9);
	    return is_35_;
	}
	byte[] is_36_ = new byte[i_33_];
	buffer.getBytes(is_36_, 0, i_33_);
	return is_36_;
    }
    
    public static int method212(int i, int i_37_, int i_38_, int i_39_) {
	int i_40_ = i_39_ >> 7;
	int i_41_ = i >> 7;
	if (i_40_ < 0 || i_41_ < 0 || i_40_ > 103 || i_41_ > 103)
	    return 0;
	int i_42_ = i_38_;
	if (i_42_ < 3
	    && (Class55.tileFlags[1][i_40_][i_41_] & 0x2) == 2)
	    i_42_++;
	int i_43_ = i_39_ & 0x7f;
	int i_44_ = i & 0x7f;
	int i_45_ = (((Class67.heightMap[i_42_][i_40_][i_41_]
		       * (128 - i_43_))
		      + i_43_ * (Class67.heightMap[i_42_]
				 [i_40_ + 1][i_41_]))
		     >> 7);
	int i_46_ = ((i_43_ * (Class67.heightMap[i_42_]
			       [i_40_ + 1][i_41_ + 1])
		      + (-i_43_ + 128) * (Class67.heightMap
					  [i_42_][i_40_][i_41_ + 1]))
		     >> 7);
	return (-i_44_ + 128) * i_45_ + i_44_ * i_46_ >> 7;
    }
    
    public static void method213(int i) {
	aClass3_252 = null;
	aClass3_244 = null;
	aClass3_236 = null;
	aClass3_270 = null;
	aClass3_259 = null;
	aClass3_243 = null;
	aClass3_235 = null;
	aClass3_217 = null;
	aClass3_263 = null;
	aClass3_255 = null;
	aClass3_262 = null;
	aClass3_258 = null;
	aClass3_210 = null;
	aClass3_261 = null;
	aClass3_242 = null;
	aClass3_254 = null;
	aClass3_260 = null;
	anIntArray239 = null;
	aClass3_228 = null;
	aClass57_246 = null;
	aClass3_269 = null;
	aClass3_273 = null;
	aClass3_223 = null;
	aClass3_250 = null;
	anIntArrayArray221 = null;
	aClass3_220 = null;
	aClass3_212 = null;
	aClass3_249 = null;
	aClass3_214 = null;
	aClass3_268 = null;
	aClass3_256 = null;
	aClass3_234 = null;
	aClass3_267 = null;
	aClass39_Sub5_Sub4_Sub4_Sub2Array211 = null;
	aClass3_216 = null;
	aClass3_240 = null;
	aClass3_238 = null;
	aClass3_225 = null;
	aClass3_257 = null;
	aClass3_219 = null;
	aClass3_266 = null;
	tableCache = null;
	aClass3_241 = null;
	aClass3_233 = null;
	aClass3_247 = null;
	aClass3_253 = null;
	aClass3_215 = null;
	aClass3_218 = null;
	aClass3_222 = null;
	aClass19_213 = null;
	aClass3_248 = null;
	aClass3_251 = null;
	aClass3_272 = null;
	aClass3_237 = null;
	aClass3_265 = null;
	aClass3_271 = null;
	aClass3_226 = null;
	aClass3_230 = null;
    }
    
    static {
	aClass3_210 = Class39_Sub5_Sub9.createJstring("Hierhin gehen");
	aClass39_Sub5_Sub4_Sub4_Sub2Array211
	    = new Player[2048];
	anInt224 = -1;
	aClass3_226
	    = Class39_Sub5_Sub9.createJstring("and choose the (Wcreate account(W");
	anInt232 = 0;
	aClass3_230 = Class39_Sub5_Sub9.createJstring("Loading wordpack )2 ");
	aClass3_218 = aClass3_230;
	aClass3_228 = aClass3_226;
	aClass3_233
	    = (Class39_Sub5_Sub9.createJstring
	       ("Account locked as we suspect it has been stolen)3"));
	aClass3_222 = (Class39_Sub5_Sub9.createJstring
		       ("You are standing in a members)2only area)3"));
	anInt231 = 0;
	aClass3_223 = aClass3_222;
	aClass3_225
	    = Class39_Sub5_Sub9
		  .createJstring("Please try using a different world)3");
	aClass3_214 = aClass3_225;
	aClass3_212 = aClass3_225;
	aClass3_234 = aClass3_225;
	aClass3_215 = aClass3_233;
	aClass3_219 = aClass3_225;
	aClass3_220 = aClass3_225;
	aClass3_217 = aClass3_225;
	aClass19_213 = new HashTable(32);
	aBoolean245 = false;
	anInt264 = 5063219;
	aClass3_262 = Class39_Sub5_Sub9.createJstring("");
	aClass3_244 = aClass3_262;
	aClass3_237 = aClass3_262;
	aClass3_254 = aClass3_262;
	aClass3_266 = aClass3_262;
	aClass3_268 = aClass3_262;
	aClass3_248 = aClass3_262;
	aClass3_258 = aClass3_262;
	aClass3_243 = aClass3_262;
	aClass3_238 = aClass3_262;
	aClass3_240 = aClass3_262;
	aClass3_259 = aClass3_262;
	aClass3_257 = aClass3_262;
	aClass3_253 = aClass3_262;
	aClass3_250 = aClass3_262;
	aClass3_256 = aClass3_262;
	aClass3_235 = aClass3_262;
	aClass3_263 = aClass3_262;
	aClass3_267 = aClass3_262;
	aClass3_241 = aClass3_262;
	aClass3_265 = aClass3_262;
	aClass3_251 = aClass3_262;
	aClass3_249 = aClass3_262;
	aClass3_236 = aClass3_262;
	aClass3_252 = aClass3_262;
	aClass3_242 = aClass3_262;
	aClass3_255 = aClass3_262;
	aClass3_261 = aClass3_262;
	aClass3_260 = aClass3_262;
	aClass3_269 = aClass3_262;
	aClass3_247 = aClass3_262;
	aClass3_272 = aClass3_262;
	aClass3_271 = aClass3_262;
	aClass3_270 = aClass3_262;
	aClass3_273 = aClass3_262;
    }
}
