package jagex.utils;

import unpackaged.ArchiveRequest;
import unpackaged.ArchiveWorker;
import jagex.graphics.BitmapFont;
import jagex.io.Buffer;
import unpackaged.Bzip2Block;
import unpackaged.CacheIO;
import unpackaged.Canvas_Sub1;
import unpackaged.Class1;
import unpackaged.Class10;
import unpackaged.Class12;
import unpackaged.Class13;
import unpackaged.Class14;
import unpackaged.Class15;
import unpackaged.Class2;
import unpackaged.Class20;
import unpackaged.Class23;
import unpackaged.Class25;
import unpackaged.Class26;
import unpackaged.Class30;
import unpackaged.Class31;
import unpackaged.Class32;
import unpackaged.Class33;
import unpackaged.Class34;
import unpackaged.Class36;
import unpackaged.Class37;
import unpackaged.Class39_Sub10;
import unpackaged.Class39_Sub11;
import unpackaged.Class39_Sub12;
import unpackaged.Class39_Sub13;
import unpackaged.Class39_Sub14;
import unpackaged.Class39_Sub4;
import unpackaged.Class39_Sub5_Sub11;
import unpackaged.Class39_Sub5_Sub12;
import unpackaged.Class39_Sub5_Sub14;
import unpackaged.Class39_Sub5_Sub16;
import unpackaged.Class39_Sub5_Sub18;
import unpackaged.Class39_Sub5_Sub4;
import unpackaged.Class39_Sub5_Sub4_Sub2;
import unpackaged.Class39_Sub5_Sub4_Sub4;
import unpackaged.Class39_Sub5_Sub5;
import unpackaged.Class39_Sub5_Sub6;
import unpackaged.Class39_Sub5_Sub7;
import unpackaged.Class39_Sub5_Sub9;
import unpackaged.Class39_Sub7;
import unpackaged.Class4;
import unpackaged.Class41;
import unpackaged.Class43;
import unpackaged.Class44;
import unpackaged.Class45;
import unpackaged.Class46;
import unpackaged.Class46_Sub1;
import unpackaged.Class47;
import unpackaged.Class48;
import unpackaged.Class50;
import unpackaged.Class53;
import unpackaged.Class55;
import unpackaged.Class62;
import unpackaged.Class62_Sub1;
import unpackaged.Class62_Sub2;
import unpackaged.Class63;
import unpackaged.Class65;
import unpackaged.Class66;
import unpackaged.Class67;
import unpackaged.Class68;
import unpackaged.Client;
import unpackaged.ClientApplet;
import unpackaged.ClientScript;
import unpackaged.FileLoader;
import unpackaged.FileTable;
import jagex.io.FrameBuffer;
import jagex.world.actors.GroundItem;
import unpackaged.ItemDefinition;
import jagex.graphics.JImage;
import unpackaged.JKeyListener;
import unpackaged.JMouseListener;
import jagex.io.JSocket;
import unpackaged.NameTable;
import jagex.world.actors.Npc;
import unpackaged.ObjectDefinition;
import unpackaged.OndemandRequest;
import jagex.world.actors.Player;
import jagex.world.actors.Projectile;
import unpackaged.RuntimeException_Sub1;
import unpackaged.ScriptState;
import unpackaged.Signlink;
import jagex.world.actors.StillGraphic;
import jagex.world.map.TraversalMap;
import unpackaged.Widget;

/* Class7 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Cache
{
    public static int anInt101;
    public static JString aClass3_102
	= Class39_Sub5_Sub9.createJstring("Bitte laden Sie die Seite neu)3");
    public static JString aClass3_103;
    public static byte[][] aByteArrayArray104;
    public static JString aClass3_105;
    public static HashTable aClass19_106;
    public static JString aClass3_107;
    public static Signlink aClass21_108;
    public static Player aClass39_Sub5_Sub4_Sub4_Sub2_109;
    public SubNode aClass39_Sub5_110 = new SubNode();
    public static byte[] aByteArray111
	= { 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
	    110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122,
	    48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
    public static int[] anIntArray112;
    public static JString aClass3_113;
    public static JString aClass3_114;
    public static JString aClass3_115;
    public static JString aClass3_116;
    public static JString aClass3_117;
    public static int anInt118;
    public int anInt119;
    public HashTable aClass19_120;
    public static boolean aBoolean121;
    public Queue aClass54_122 = new Queue();
    public static Class31 aClass31_123;
    public int anInt124;
    
    public static void method130(byte i) {
	if (Class2.anInt53 != 0) {
	    BitmapFont class39_sub5_sub10_sub1
		= Class39_Sub5_Sub14.p12fullFont;
	    int i_0_ = 0;
	    if (Class39_Sub7.anInt1380 != 0)
		i_0_ = 1;
	    for (int i_1_ = 0; i_1_ < 100; i_1_++) {
		if (Class2.aClass3Array52[i_1_] != null) {
		    int i_2_ = Client.anIntArray1268[i_1_];
		    JString class3 = Class39_Sub11.aClass3Array1462[i_1_];
		    int i_3_ = 0;
		    if (class3 != null
			&& class3.method65(Class37.aClass3_661, false)) {
			class3 = class3.method85(-58, 5);
			i_3_ = 1;
		    }
		    if (class3 != null
			&& class3.method65(Class53.aClass3_959, false)) {
			class3 = class3.method85(-58, 5);
			i_3_ = 2;
		    }
		    if ((i_2_ == 3 || i_2_ == 7)
			&& (i_2_ == 7 || NameTable.anInt177 == 0
			    || (NameTable.anInt177 == 1
				&& JString.method60(21469, class3)))) {
			int i_4_ = -(i_0_ * 13) + 329;
			i_0_++;
			int i_5_ = 4;
			class39_sub5_sub10_sub1.method647((Class39_Sub5_Sub16
							   .aClass3_1991),
							  i_5_, i_4_, 0);
			class39_sub5_sub10_sub1.method647((Class39_Sub5_Sub16
							   .aClass3_1991),
							  i_5_, i_4_ - 1,
							  65535);
			i_5_
			    += class39_sub5_sub10_sub1
				   .method637(Class39_Sub5_Sub16.aClass3_1991);
			i_5_ += class39_sub5_sub10_sub1.method645(32);
			if (i_3_ == 1) {
			    Class55.aClass39_Sub5_Sub10_Sub4Array1247[0]
				.method695(i_5_, i_4_ - 12);
			    i_5_ += 14;
			}
			if (i_3_ == 2) {
			    Class55.aClass39_Sub5_Sub10_Sub4Array1247[1]
				.method695(i_5_, i_4_ - 12);
			    i_5_ += 14;
			}
			class39_sub5_sub10_sub1.method647
			    ((Class39_Sub5_Sub11.method708
			      (new JString[] { class3, Class31.aClass3_556,
					      Class2.aClass3Array52[i_1_] })),
			     i_5_, i_4_, 0);
			class39_sub5_sub10_sub1.method647
			    ((Class39_Sub5_Sub11.method708
			      (new JString[] { class3, Class31.aClass3_556,
					      Class2.aClass3Array52[i_1_] })),
			     i_5_, i_4_ - 1, 65535);
			if (i_0_ >= 5)
			    break;
		    }
		    if (i_2_ == 5 && NameTable.anInt177 < 2) {
			int i_6_ = 329 - i_0_ * 13;
			i_0_++;
			class39_sub5_sub10_sub1.method647((Class2
							   .aClass3Array52
							   [i_1_]),
							  4, i_6_, 0);
			class39_sub5_sub10_sub1.method647((Class2
							   .aClass3Array52
							   [i_1_]),
							  4, i_6_ - 1, 65535);
			if (i_0_ >= 5)
			    break;
		    }
		    if (i_2_ == 6 && NameTable.anInt177 < 2) {
			int i_7_ = -(i_0_ * 13) + 329;
			class39_sub5_sub10_sub1.method647
			    ((Class39_Sub5_Sub11.method708
			      (new JString[] { Class39_Sub14.aClass3_1514,
					      Class62_Sub2.aClass3_1605,
					      class3, Class31.aClass3_556,
					      Class2.aClass3Array52[i_1_] })),
			     4, i_7_, 0);
			i_0_++;
			class39_sub5_sub10_sub1.method647
			    ((Class39_Sub5_Sub11.method708
			      (new JString[] { Class39_Sub14.aClass3_1514,
					      Class62_Sub2.aClass3_1605,
					      class3, Class31.aClass3_556,
					      Class2.aClass3Array52[i_1_] })),
			     4, i_7_ - 1, 65535);
			if (i_0_ >= 5)
			    break;
		    }
		}
	    }
	}
    }
    
    public void method131(long l, int i) {
	SubNode class39_sub5
	    = (SubNode) aClass19_120.fetch(l);
	if (class39_sub5 != null) {
	    class39_sub5.unlinkDeque();
	    class39_sub5.unlinkQueue();
	    anInt124++;
	}
    }
    
    public static void method132(int i) {
	Class23.aClass3_437 = Class34.aClass3_611;
	Class1.aClass3_44 = Class46.aClass3_878;
	JKeyListener.aClass3_623 = Class50.aClass3_928;
	Class39_Sub11.aClass3_1467 = Class62.aClass3_1114;
	Player.aClass3_2531 = Class12.aClass3_195;
	Class62.aClass3_1109 = Class2.aClass3_46;
	Class39_Sub11.aClass3_1468 = ClientScript.aClass3_1695;
	Class47.aClass3_895 = Class68.aClass3_1185;
	Class39_Sub5_Sub5.aClass3_1741 = StillGraphic.aClass3_2329;
	Class30.aClass3_535 = Class66.aClass3_1149;
	Queue.aClass3_977 = Class43.aClass3_817;
	Class2.aClass3_57 = Class39_Sub5_Sub4.aClass3_1733;
	Class63.aClass3_1129 = FileLoader.aClass3_1289;
	Class23.aClass3_424 = Class31.aClass3_569;
	SubNode.aClass3_1343 = Class39_Sub5_Sub14.aClass3_1911;
	Queue.aClass3_976 = GroundItem.aClass3_2236;
	Class45.aClass3_864 = Deque.aClass3_913;
	Client.aClass3_1264 = StillGraphic.aClass3_2344;
	Class39_Sub4.aClass3_1334 = Class39_Sub5_Sub12.aClass3_1851;
	Class14.aClass3_247 = JMouseListener.aClass3_790;
	Class39_Sub5_Sub6.aClass3_1751 = Class39_Sub5_Sub6.aClass3_1771;
	Class14.aClass3_236 = GroundItem.aClass3_2237;
	Class14.aClass3_272 = ItemDefinition.aClass3_1681;
	Class14.aClass3_219 = Node.aClass3_735;
	Class62_Sub2.aClass3_1601 = Npc.aClass3_2487;
	Class14.aClass3_223 = Class53.aClass3_956;
	Class39_Sub5_Sub7.aClass3_1786 = JString.aClass3_1238;
	Player.aClass3_2528 = HashTable.aClass3_373;
	Class62.aClass3_1113 = FrameBuffer.aClass3_2152;
	Class14.aClass3_260 = ItemDefinition.aClass3_1681;
	Class26.aClass3_490 = Class4.aClass3_66;
	IsaacPrng.aClass3_1085 = Class53.aClass3_966;
	StillGraphic.aClass3_2346 = Class25.aClass3_469;
	JString.aClass3_1234 = Class43.aClass3_805;
	Canvas_Sub1.aClass3_13 = Class39_Sub5_Sub6.aClass3_1770;
	Class14.aClass3_255 = ItemDefinition.aClass3_1681;
	Class39_Sub4.aClass3_1338 = Class14.aClass3_210;
	Class14.aClass3_270 = FrameBuffer.aClass3_2143;
	Class41.aClass3_781 = Class31.aClass3_550;
	Class39_Sub11.aClass3_1454 = Class62.aClass3_1114;
	Class14.aClass3_263 = ItemDefinition.aClass3_1681;
	SubNode.aClass3_1346 = Class36.aClass3_645;
	HashTable.aClass3_374 = Class41.aClass3_767;
	Class14.aClass3_259 = Class39_Sub14.aClass3_1513;
	Class39_Sub5_Sub16.aClass3_1991 = StillGraphic.aClass3_2335;
	Class15.aClass3_286 = ScriptState.aClass3_441;
	OndemandRequest.aClass3_1721 = Class55.aClass3_1246;
	Class37.aClass3_659 = Buffer.aClass3_1369;
	Class14.aClass3_220 = Huffmans.aClass3_758;
	Class13.aClass3_206 = Class47.aClass3_889;
	JKeyListener.aClass3_616 = GroundItem.aClass3_2249;
	ArchiveWorker.aClass3_1195 = Node.aClass3_740;
	Class31.aClass3_549 = Class39_Sub5_Sub16.aClass3_1990;
	Npc.aClass3_2496 = Buffer.aClass3_1359;
	Class39_Sub13.aClass3_1503 = Player.aClass3_2530;
	ClientScript.aClass3_1689 = Class48.aClass3_906;
	TraversalMap.aClass3_519 = Class26.aClass3_486;
	Class39_Sub5_Sub4_Sub4.aClass3_2289 = Class39_Sub5_Sub18.aClass3_2131;
	Class39_Sub12.aClass3_1485 = OndemandRequest.aClass3_1722;
	Class34.aClass3_608 = Class46.aClass3_873;
	Class14.aClass3_250 = JMouseListener.aClass3_790;
	Class25.aClass3_460 = Huffmans.aClass3_763;
	StillGraphic.aClass3_2327 = aClass3_102;
	Class14.aClass3_261 = ItemDefinition.aClass3_1681;
	Class32.aClass3_592 = IsaacPrng.aClass3_1092;
	ClientApplet.aClass3_7 = GroundItem.aClass3_2235;
	JKeyListener.aClass3_624 = Class41.aClass3_772;
	Class14.aClass3_235 = Class47.aClass3_890;
	Class14.aClass3_243 = ItemDefinition.aClass3_1681;
	Class62_Sub1.aClass3_1593 = Class39_Sub14.aClass3_1510;
	Class39_Sub11.aClass3_1475 = Class41.aClass3_770;
	Class39_Sub5_Sub14.aClass3_1913 = JImage.aClass3_1583;
	Class13.aClass3_207 = Class47.aClass3_889;
	Class14.aClass3_248 = GroundItem.aClass3_2237;
	FileLoader.aClass3_1286 = FileLoader.aClass3_1292;
	Class14.aClass3_238 = ItemDefinition.aClass3_1681;
	Class62.aClass3_1116 = Class12.aClass3_193;
	ObjectDefinition.aClass3_1966 = Class46_Sub1.aClass3_1557;
	Class39_Sub5_Sub5.aClass3_1736 = Class68.aClass3_1194;
	Class14.aClass3_273 = ItemDefinition.aClass3_1681;
	Huffmans.aClass3_755 = Class68.aClass3_1187;
	Class39_Sub5_Sub7.aClass3_1777 = Bzip2Block.aClass3_1074;
	Bzip2Block.aClass3_1050 = Class67.aClass3_1178;
	HashTable.aClass3_356 = Class36.aClass3_652;
	Class30.aClass3_537 = StillGraphic.aClass3_2341;
	Queue.aClass3_983 = Class39_Sub4.aClass3_1337;
	Class53.aClass3_953 = Class25.aClass3_466;
	Class34.aClass3_615 = ObjectDefinition.aClass3_1947;
	Class4.aClass3_77 = SubNode.aClass3_1347;
	Class14.aClass3_251 = ItemDefinition.aClass3_1681;
	Class14.aClass3_214 = Huffmans.aClass3_758;
	Class39_Sub5_Sub5.aClass3_1747 = Class46_Sub1.aClass3_1567;
	Bzip2Block.aClass3_1049 = Deque.aClass3_909;
	Class45.aClass3_865 = Class53.aClass3_961;
	Class46.aClass3_875 = RuntimeException_Sub1.aClass3_1219;
	RuntimeException_Sub1.aClass3_1224 = Class39_Sub12.aClass3_1483;
	Class63.aClass3_1118 = Class4.aClass3_72;
	Class39_Sub5_Sub6.aClass3_1766 = Canvas_Sub1.aClass3_19;
	Class66.aClass3_1165 = GroundItem.aClass3_2234;
	Client.aClass3_1277 = Class39_Sub5_Sub11.aClass3_1842;
	Class14.aClass3_237 = ItemDefinition.aClass3_1681;
	Npc.aClass3_2504 = Class62.aClass3_1108;
	Class14.aClass3_252 = ItemDefinition.aClass3_1681;
	Client.aClass3_1279 = Class65.aClass3_1133;
	StillGraphic.aClass3_2343 = Class68.aClass3_1191;
	Class32.aClass3_577 = JImage.aClass3_1578;
	StillGraphic.aClass3_2340 = Class39_Sub13.aClass3_1507;
	Class46.aClass3_877 = Class36.aClass3_639;
	Class14.aClass3_240 = ItemDefinition.aClass3_1681;
	ClientScript.aClass3_1698 = ClientScript.aClass3_1695;
	FileLoader.aClass3_1299 = Class39_Sub5_Sub5.aClass3_1735;
	Class25.aClass3_470 = JSocket.aClass3_300;
	Class39_Sub4.aClass3_1328 = Class39_Sub5_Sub5.aClass3_1735;
	ClientApplet.aClass3_10 = Queue.aClass3_988;
	Canvas_Sub1.aClass3_16 = JKeyListener.aClass3_619;
	HashTable.aClass3_385 = Class39_Sub10.aClass3_1419;
	Class31.aClass3_572 = Class10.aClass3_167;
	Class23.aClass3_431 = Class39_Sub5_Sub9.aClass3_1816;
	Class23.aClass3_427 = Class31.aClass3_569;
	Class14.aClass3_212 = Class50.aClass3_936;
	Class39_Sub10.aClass3_1448 = Class39_Sub10.aClass3_1444;
	Class39_Sub5_Sub4_Sub2.aClass3_2222 = Class43.aClass3_828;
	Deque.aClass3_910 = Class32.aClass3_578;
	Class39_Sub5_Sub5.aClass3_1745 = Queue.aClass3_986;
	ScriptState.aClass3_442 = Class39_Sub5_Sub14.aClass3_1910;
	Class63.aClass3_1123 = Class39_Sub5_Sub4_Sub4.aClass3_2302;
	Class39_Sub5_Sub12.aClass3_1850 = Node.aClass3_731;
	Class14.aClass3_253 = RuntimeException_Sub1.aClass3_1218;
	Class14.aClass3_234 = Class50.aClass3_936;
	Class14.aClass3_267 = ItemDefinition.aClass3_1681;
	JKeyListener.aClass3_622 = Class39_Sub5_Sub9.aClass3_1805;
	Class39_Sub5_Sub4_Sub4.aClass3_2310 = Class39_Sub5_Sub18.aClass3_2135;
	Class36.aClass3_647 = ArchiveRequest.aClass3_1402;
	Class39_Sub14.aClass3_1521 = Class26.aClass3_474;
	Class39_Sub4.aClass3_1326 = Class45.aClass3_860;
	Projectile.aClass3_2190 = FileTable.aClass3_140;
	Class4.aClass3_68 = Class39_Sub5_Sub4.aClass3_1729;
	Class39_Sub5_Sub7.aClass3_1787 = Huffmans.aClass3_750;
	Class14.aClass3_218 = ScriptState.aClass3_444;
	Class14.aClass3_258 = Class44.aClass3_839;
	Class10.aClass3_168 = Class10.aClass3_170;
	Class37.aClass3_660 = Buffer.aClass3_1369;
	Class25.aClass3_458 = Canvas_Sub1.aClass3_18;
	Class55.aClass3_1253 = Queue.aClass3_974;
	JSocket.aClass3_305 = Class31.aClass3_564;
	Node.aClass3_727 = Class39_Sub14.aClass3_1522;
	Class14.aClass3_257 = FileTable.aClass3_131;
	Class44.aClass3_847 = Class53.aClass3_963;
	JImage.aClass3_1574 = Buffer.aClass3_1356;
	JMouseListener.aClass3_795 = Class39_Sub10.aClass3_1425;
	Class39_Sub5_Sub4_Sub2.aClass3_2218 = Huffmans.aClass3_747;
	Class14.aClass3_268 = ItemDefinition.aClass3_1681;
	Class14.aClass3_256 = Queue.aClass3_991;
	Class45.aClass3_859 = Class39_Sub5_Sub12.aClass3_1852;
	Class46.aClass3_874 = Class39_Sub5_Sub18.aClass3_2129;
	Widget.aClass3_2002 = Class68.aClass3_1193;
	NameTable.aClass3_190 = NameTable.aClass3_181;
	Class47.aClass3_896 = Class46_Sub1.aClass3_1556;
	Class39_Sub13.aClass3_1509 = Class33.aClass3_594;
	Class14.aClass3_269 = ItemDefinition.aClass3_1681;
	ObjectDefinition.aClass3_1973 = Class39_Sub5_Sub12.aClass3_1858;
	Class46.aClass3_881 = ObjectDefinition.aClass3_1940;
	Class4.aClass3_59 = Class48.aClass3_902;
	RuntimeException_Sub1.aClass3_1214 = Class67.aClass3_1174;
	HashTable.aClass3_383 = Class65.aClass3_1139;
	IsaacPrng.aClass3_1082 = Class1.aClass3_43;
	Class14.aClass3_241 = ItemDefinition.aClass3_1681;
	IsaacPrng.aClass3_1098 = Class39_Sub5_Sub4_Sub4.aClass3_2278;
	Class39_Sub5_Sub16.aClass3_1981 = Class30.aClass3_546;
	Class39_Sub5_Sub9.aClass3_1803 = Queue.aClass3_980;
	Huffmans.aClass3_751 = JImage.aClass3_1577;
	Class39_Sub14.aClass3_1514 = CacheIO.aClass3_96;
	Class67.aClass3_1176 = CacheIO.aClass3_100;
	Class13.aClass3_209 = Class2.aClass3_56;
	Class46.aClass3_876 = Class39_Sub5_Sub4_Sub4.aClass3_2309;
	Class14.aClass3_249 = Class39_Sub5_Sub4_Sub2.aClass3_2224;
	Class14.aClass3_228 = Canvas_Sub1.aClass3_17;
	HashTable.aClass3_386 = Class63.aClass3_1121;
	Class39_Sub5_Sub4_Sub2.aClass3_2230 = Queue.aClass3_991;
	Class53.aClass3_958 = Deque.aClass3_917;
	ItemDefinition.aClass3_1683 = TraversalMap.aClass3_512;
	Buffer.aClass3_1365 = Class31.aClass3_557;
	Class14.aClass3_217 = Class50.aClass3_936;
	StillGraphic.aClass3_2353 = HashTable.aClass3_384;
	Class13.aClass3_198 = Class36.aClass3_635;
	FrameBuffer.aClass3_2144 = ItemDefinition.aClass3_1687;
	Class36.aClass3_634 = Buffer.aClass3_1363;
	Class44.aClass3_835 = Class62_Sub2.aClass3_1598;
	Class14.aClass3_271 = HashTable.aClass3_380;
	Class14.aClass3_244 = ItemDefinition.aClass3_1681;
	Deque.aClass3_926 = NameTable.aClass3_188;
	Class65.aClass3_1142 = ScriptState.aClass3_445;
	Class39_Sub5_Sub6.aClass3_1769
	    = Npc.aClass3_2498;
	FileTable.aClass3_148 = Class39_Sub12.aClass3_1484;
	Class14.aClass3_265 = Class50.aClass3_936;
	Class66.aClass3_1162 = Player.aClass3_2535;
	Class20.aClass3_387 = Huffmans.aClass3_745;
	Class10.aClass3_173 = Class13.aClass3_202;
	JSocket.aClass3_308 = Class39_Sub5_Sub16.aClass3_1993;
	Class39_Sub5_Sub11.aClass3_1845 = Class39_Sub10.aClass3_1435;
	Class53.aClass3_967 = Npc.aClass3_2497;
	Class14.aClass3_266 = Widget.aClass3_2070;
	Class14.aClass3_215 = Class39_Sub10.aClass3_1447;
	Class39_Sub11.aClass3_1465 = Class39_Sub11.aClass3_1463;
	Class65.aClass3_1140 = ItemDefinition.aClass3_1681;
	Client.aClass3_1270 = StillGraphic.aClass3_2348;
	aClass3_105 = GroundItem.aClass3_2244;
	ClientScript.aClass3_1701 = Class39_Sub5_Sub14.aClass3_1898;
	Class4.aClass3_79 = Class55.aClass3_1249;
	Class63.aClass3_1119 = ScriptState.aClass3_452;
	JSocket.aClass3_299 = HashTable.aClass3_378;
	Projectile.aClass3_2182 = ArchiveWorker.aClass3_1209;
	Projectile.aClass3_2196 = Class10.aClass3_166;
	Class25.aClass3_462 = Class67.aClass3_1173;
	JImage.aClass3_1585 = Class62_Sub2.aClass3_1603;
	Class39_Sub12.aClass3_1496 = Class55.aClass3_1250;
    }
    
    public SubNode get(long hash) {
	SubNode class39_sub5
	    = (SubNode) aClass19_120.fetch(hash);
	if (class39_sub5 != null)
	    aClass54_122.offerFirst(class39_sub5);
	return class39_sub5;
    }
    
    public void method134(int i) {
	for (;;) {
	    SubNode class39_sub5 = aClass54_122.pollFirst();
	    if (class39_sub5 == null)
		break;
	    class39_sub5.unlinkDeque();
	    class39_sub5.unlinkQueue();
	}
	anInt124 = anInt119;
    }
    
    public void put(SubNode class39_sub5, long l, byte i) {
	if (anInt124 != 0)
	    anInt124--;
	else {
	    SubNode class39_sub5_8_ = aClass54_122.pollFirst();
	    class39_sub5_8_.unlinkDeque();
	    class39_sub5_8_.unlinkQueue();
	    if (class39_sub5_8_ == aClass39_Sub5_110) {
		class39_sub5_8_ = aClass54_122.pollFirst();
		class39_sub5_8_.unlinkDeque();
		class39_sub5_8_.unlinkQueue();
	    }
	}
	aClass19_120.put(l, class39_sub5);
	aClass54_122.offerFirst(class39_sub5);
    }
    
    public static void method136(int i) {
	aByteArrayArray104 = null;
	aClass3_114 = null;
	aClass31_123 = null;
	aClass3_102 = null;
	aClass39_Sub5_Sub4_Sub4_Sub2_109 = null;
	aClass19_106 = null;
	aClass3_105 = null;
	aClass3_107 = null;
	aClass3_115 = null;
	aByteArray111 = null;
	aClass3_116 = null;
	aClass3_113 = null;
	anIntArray112 = null;
	aClass21_108 = null;
	aClass3_103 = null;
	aClass3_117 = null;
    }
    
    public Cache(int i) {
	anInt119 = i;
	int i_9_ = 1;
	anInt124 = i;
	for (/**/; i_9_ + i_9_ < i; i_9_ += i_9_) {
	    /* empty */
	}
	aClass19_120 = new HashTable(i_9_);
    }
    
    static {
	anInt101 = 0;
	aClass3_113 = Class39_Sub5_Sub9.createJstring("huffman");
	aClass3_103 = Class39_Sub5_Sub9.createJstring(" @whi@(X");
	aClass3_107 = Class39_Sub5_Sub9.createJstring("backhmid1");
	aClass3_117 = Class39_Sub5_Sub9.createJstring("This world is full)3");
	aClass3_115 = Class39_Sub5_Sub9.createJstring("headicons_hint");
	aClass3_116
	    = Class39_Sub5_Sub9.createJstring("Service unavailable)3");
	aClass3_114 = aClass3_116;
	aClass3_105 = aClass3_117;
	aBoolean121 = false;
	anInt118 = 0;
    }
}
