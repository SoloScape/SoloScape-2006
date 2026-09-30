package unpackaged;

import jagex.utils.Node;
import jagex.utils.Deque;

/* Class39_Sub1_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class39_Sub1_Sub1 extends Class39_Sub1
{
    public Deque aClass49_1616;
    public int anInt1617 = 32;
    public Deque[] aClass49Array1618 = new Deque[8];
    public int anInt1619;
    public int anInt1620;
    public int anInt1621;
    
    public int method418(int[] is, int i, int i_0_) {
	anInt1620 -= i_0_;
	if (anInt1620 <= 0) {
	    anInt1620 += Class15.anInt274 >> 4;
	    for (int i_1_ = 0; i_1_ < 8; i_1_++) {
		Deque class49 = aClass49Array1618[i_1_];
		for (Class39_Sub1 class39_sub1
			 = (Class39_Sub1) class49.getFirst();
		     class39_sub1 != null;
		     class39_sub1
			 = (Class39_Sub1) class49.getNext()) {
		    int i_2_ = method425(class39_sub1);
		    if (i_2_ != i_1_)
			aClass49Array1618[i_2_].offerFirst(class39_sub1);
		}
	    }
	}
	for (int i_3_ = 0; i_3_ < 8; i_3_++) {
	    Deque class49 = aClass49Array1618[i_3_];
	    for (Class39_Sub1 class39_sub1
		     = (Class39_Sub1) class49.getFirst();
		 class39_sub1 != null;
		 class39_sub1 = (Class39_Sub1) class49.getNext()) {
		class39_sub1.aBoolean1321 = false;
		if (class39_sub1.aClass39_Sub3_1320 != null)
		    class39_sub1.aClass39_Sub3_1320.anInt1323 = 0;
	    }
	}
	int i_4_ = 0;
	int i_5_ = 255;
	int i_6_ = 7;
	while (i_5_ != 0) {
	    int i_7_;
	    int i_8_;
	    if (i_6_ < 0) {
		i_7_ = i_6_ & 0x3;
		i_8_ = -(i_6_ >> 2);
	    } else {
		i_7_ = i_6_;
		i_8_ = 0;
	    }
	    for (int i_9_ = i_5_ >>> i_7_ & 0x11111111; i_9_ != 0;
		 i_9_ >>>= 4) {
		if ((i_9_ & 0x1) != 0) {
		    i_5_ &= 1 << i_7_ ^ 0xffffffff;
		    Deque class49 = aClass49Array1618[i_7_];
		    for (Class39_Sub1 class39_sub1
			     = (Class39_Sub1) class49.getFirst();
			 class39_sub1 != null;
			 class39_sub1
			     = (Class39_Sub1) class49.getNext()) {
			if (!class39_sub1.aBoolean1321) {
			    Class39_Sub3 class39_sub3
				= class39_sub1.aClass39_Sub3_1320;
			    if (class39_sub3 != null
				&& class39_sub3.anInt1323 > i_8_)
				i_5_ |= 1 << i_7_;
			    else {
				if (i_4_ < anInt1617) {
				    int i_10_
					= class39_sub1.method415(is, i, i_0_);
				    i_4_ += i_10_;
				    if (class39_sub3 != null)
					class39_sub3.anInt1323 += i_10_;
				} else
				    class39_sub1.method417(i_0_);
				class39_sub1.aBoolean1321 = true;
			    }
			}
		    }
		}
		i_7_ += 4;
		i_8_++;
	    }
	    i_6_--;
	}
	return i_4_;
    }
    
    public synchronized void method417(int i) {
	do {
	    if (anInt1619 < 0) {
		method420(i);
		break;
	    }
	    if (anInt1621 + i < anInt1619) {
		anInt1621 += i;
		method420(i);
		break;
	    }
	    int i_11_ = anInt1619 - anInt1621;
	    method420(i_11_);
	    i -= i_11_;
	    anInt1621 += i_11_;
	    method419();
	    Class39_Sub2 class39_sub2
		= (Class39_Sub2) aClass49_1616.getFirst();
	    synchronized (class39_sub2) {
		int i_12_ = class39_sub2.method453(this);
		if (i_12_ < 0) {
		    class39_sub2.anInt1322 = 0;
		    method423(class39_sub2);
		} else {
		    class39_sub2.anInt1322 = i_12_;
		    method422(class39_sub2.nextNode, class39_sub2);
		}
	    }
	} while (i != 0);
    }
    
    public void method419() {
	if (anInt1621 > 0) {
	    for (Class39_Sub2 class39_sub2
		     = (Class39_Sub2) aClass49_1616.getFirst();
		 class39_sub2 != null;
		 class39_sub2
		     = (Class39_Sub2) aClass49_1616.getNext())
		class39_sub2.anInt1322 -= anInt1621;
	    anInt1619 -= anInt1621;
	    anInt1621 = 0;
	}
    }
    
    public void method420(int i) {
	anInt1620 -= i;
	if (anInt1620 < 0)
	    anInt1620 = 0;
	for (int i_13_ = 0; i_13_ < 8; i_13_++) {
	    Deque class49 = aClass49Array1618[i_13_];
	    for (Class39_Sub1 class39_sub1
		     = (Class39_Sub1) class49.getFirst();
		 class39_sub1 != null;
		 class39_sub1 = (Class39_Sub1) class49.getNext())
		class39_sub1.method417(i);
	}
    }
    
    public synchronized void method421(Class39_Sub1 class39_sub1) {
	Deque class49 = aClass49Array1618[method425(class39_sub1)];
	class49.offerFirst(class39_sub1);
    }
    
    public void method422(Node class39, Class39_Sub2 class39_sub2) {
	for (/**/;
	     (class39 != aClass49_1616.head
	      && ((Class39_Sub2) class39).anInt1322 <= class39_sub2.anInt1322);
	     class39 = class39.nextNode) {
	    /* empty */
	}
	aClass49_1616.method959((byte) -15, class39, class39_sub2);
	anInt1619 = (((Class39_Sub2) aClass49_1616.head.nextNode)
		     .anInt1322);
    }
    
    public void method423(Class39_Sub2 class39_sub2) {
	class39_sub2.unlinkDeque();
	class39_sub2.method454();
	Node class39 = aClass49_1616.head.nextNode;
	if (class39 == aClass49_1616.head)
	    anInt1619 = -1;
	else
	    anInt1619 = ((Class39_Sub2) class39).anInt1322;
    }
    
    public synchronized int method415(int[] is, int i, int i_14_) {
	for (;;) {
	    if (anInt1619 < 0)
		return method418(is, i, i_14_);
	    if (anInt1621 + i_14_ < anInt1619) {
		anInt1621 += i_14_;
		return method418(is, i, i_14_);
	    }
	    int i_15_ = anInt1619 - anInt1621;
	    int i_16_ = method418(is, i, i_15_);
	    i += i_15_;
	    i_14_ -= i_15_;
	    anInt1621 += i_15_;
	    method419();
	    Class39_Sub2 class39_sub2
		= (Class39_Sub2) aClass49_1616.getFirst();
	    synchronized (class39_sub2) {
		int i_17_ = class39_sub2.method453(this);
		if (i_17_ < 0) {
		    class39_sub2.anInt1322 = 0;
		    method423(class39_sub2);
		} else {
		    class39_sub2.anInt1322 = i_17_;
		    method422(class39_sub2.nextNode, class39_sub2);
		}
	    }
	    if (i_14_ == 0)
		return i_16_;
	}
    }
    
    public synchronized void method424(Class39_Sub1 class39_sub1) {
	class39_sub1.unlinkDeque();
    }
    
    public static int method425(Class39_Sub1 class39_sub1) {
	return class39_sub1.method416() >> 5;
    }
    
    public Class39_Sub1_Sub1() {
	aClass49_1616 = new Deque();
	anInt1621 = 0;
	anInt1619 = -1;
	anInt1620 = 0;
	for (int i = 0; i < 8; i++)
	    aClass49Array1618[i] = new Deque();
    }
}
