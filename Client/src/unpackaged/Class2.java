package unpackaged;

/* Class2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.utils.HashTable;
import jagex.utils.Huffmans;
import jagex.graphics.JImage;
import jagex.world.actors.StillGraphic;
import jagex.utils.JString;
import jagex.world.map.TraversalMap;
import jagex.utils.Cache;
import java.math.BigInteger;

public class Class2
{
    public static JString aClass3_45
	= Class39_Sub5_Sub9.createJstring("@gr1@");
    public static JString aClass3_46
	= Class39_Sub5_Sub9.createJstring("RuneScape wurde aktualisiert(Q");
    public static BigInteger aBigInteger47;
    public static int logicCycle = 0;
    public static int[] anIntArray49 = new int[5];
    public static int anInt50;
    public static JString aClass3_51;
    public static JString[] aClass3Array52;
    public static int anInt53;
    public static Cache aClass7_54;
    public static volatile long aLong55;
    public static JString aClass3_56;
    public static JString aClass3_57;
    public static Cache aClass7_58;
    
    public static synchronized long getSystemTime() {
	long l = System.currentTimeMillis();
	if (l < HashTable.aLong381)
	    Client.aLong1263 += HashTable.aLong381 + -l;
	HashTable.aLong381 = l;
	return Client.aLong1263 + l;
    }
    
    public static void method52(JString class3, boolean bool) {
	if (class3 == null || class3.getLength() == 0)
	    Class67.anInt1184 = 0;
	else {
	    int i = 0;
	    JString[] class3s = new JString[100];
	    JString class3_0_ = class3;
	    for (;;) {
		int i_1_ = class3_0_.method92((byte) 123, 32);
		if (i_1_ == -1) {
		    class3_0_ = class3_0_.method69((byte) -45);
		    if (class3_0_.getLength() > 0)
			class3s[i++] = class3_0_.method77();
		    break;
		}
		JString class3_2_
		    = class3_0_.method59(0, -1, i_1_).method69((byte) -45);
		if (class3_2_.getLength() > 0)
		    class3s[i++] = class3_2_.method77();
		class3_0_ = class3_0_.method85(-58, i_1_ + 1);
	    }
	    Class67.anInt1184 = 0;
	while_1_:
	    for (int i_3_ = 0; Class37.anInt663 > i_3_; i_3_++) {
		ItemDefinition class39_sub5_sub1
		    = Class26.getItemDefinition(i_3_);
		if (class39_sub5_sub1.anInt1644 == -1
		    && class39_sub5_sub1.aClass3_1661 != null) {
		    JString class3_4_
			= class39_sub5_sub1.aClass3_1661.method77();
		    for (int i_5_ = 0; i > i_5_; i_5_++) {
			if (class3_4_.method80(22938, class3s[i_5_]) == -1)
			    continue while_1_;
		    }
		    Class46_Sub1.aClass3Array1552[Class67.anInt1184]
			= class3_4_;
		    Class62_Sub1.anIntArray1589[Class67.anInt1184] = i_3_;
		    Class67.anInt1184++;
		    if (Class67.anInt1184
			>= Class46_Sub1.aClass3Array1552.length)
			break;
		}
	    }
	}
    }
    
    public static void method53(int i) {
	aClass3_51 = null;
	aClass3_46 = null;
	anIntArray49 = null;
	aClass3Array52 = null;
	aClass3_57 = null;
	aClass7_58 = null;
	aBigInteger47 = null;
	aClass7_54 = null;
	aClass3_45 = null;
	aClass3_56 = null;
    }
    
    public static void parsePlayerUpdate() {
	Huffmans.anInt749 = 0;
	JImage.anInt1586 = 0;
	StillGraphic.method533(0);
	FileTable.method162((byte) -20);
	JImage.method1019((byte) 127);
	Class46.method936((byte) -101);
	for (int i_6_ = 0; Huffmans.anInt749 > i_6_; i_6_++) {
	    int i_7_ = Class26.anIntArray496[i_6_];
	    if (Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_7_].anInt2290
		!= logicCycle)
		Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211[i_7_] = null;
	}
	if (Huffmans.frameSize
	    != Class39_Sub5_Sub11.gameBuffer.offset)
	    throw new RuntimeException("gpp1 pos:"
				       + (Class39_Sub5_Sub11
					  .gameBuffer.offset)
				       + " psize:" + Huffmans.frameSize);
	for (int i_8_ = 0; i_8_ < TraversalMap.anInt515; i_8_++) {
	    if ((Class14.aClass39_Sub5_Sub4_Sub4_Sub2Array211
		 [Class1.anIntArray40[i_8_]])
		== null)
		throw new RuntimeException("gpp2 pos:" + i_8_ + " size:"
					   + TraversalMap.anInt515);
	}
    }
    
	static {
	aBigInteger47
	    = (new BigInteger
	       ("65537"));
	aClass3_51 = Class39_Sub5_Sub9.createJstring("Loaded input handler");
	aClass3Array52 = new JString[100];
	aClass3_56
	    = (Class39_Sub5_Sub9.createJstring
	       ("Ung-Ultige Verbindung mit einem Anmelde)2Server)3"));
	anInt53 = 0;
	aClass3_57 = aClass3_51;
	aLong55 = 0L;
	aClass7_54 = new Cache(64);
	aClass7_58 = new Cache(50);
    }
}
