package jagex.audio;

import jagex.io.Buffer;

/* Class28 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Envelope
{
    public int anInt522;
    public int anInt523 = 2;
    public int anInt524;
    public int anInt525;
    public int[] anIntArray526 = new int[2];
    public int[] anIntArray527 = new int[2];
    public int anInt528;
    public int anInt529;
    public int anInt530;
    public int anInt531;
    public int anInt532;
    
    public void method311(Buffer class39_sub6) {
	anInt524 = class39_sub6.getUbyte();
	anInt522 = class39_sub6.getDword();
	anInt525 = class39_sub6.getDword();
	method313(class39_sub6);
    }
    
    public void method312() {
	anInt531 = 0;
	anInt532 = 0;
	anInt529 = 0;
	anInt528 = 0;
	anInt530 = 0;
    }
    
    public void method313(Buffer class39_sub6) {
	anInt523 = class39_sub6.getUbyte();
	anIntArray526 = new int[anInt523];
	anIntArray527 = new int[anInt523];
	for (int i = 0; i < anInt523; i++) {
	    anIntArray526[i] = class39_sub6.getUword();
	    anIntArray527[i] = class39_sub6.getUword();
	}
    }
    
    public int method314(int i) {
	if (anInt530 >= anInt531) {
	    anInt528 = anIntArray527[anInt532++] << 15;
	    if (anInt532 >= anInt523)
		anInt532 = anInt523 - 1;
	    anInt531 = (int) ((double) anIntArray526[anInt532] / 65536.0
			      * (double) i);
	    if (anInt531 > anInt530)
		anInt529 = (((anIntArray527[anInt532] << 15) - anInt528)
			    / (anInt531 - anInt530));
	}
	anInt528 += anInt529;
	anInt530++;
	return anInt528 - anInt529 >> 15;
    }
    
    public Envelope() {
	anIntArray526[0] = 0;
	anIntArray526[1] = 65535;
	anIntArray527[0] = 0;
	anIntArray527[1] = 65535;
    }
}
