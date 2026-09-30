package unpackaged;

/* Class34_Sub1 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Class34_Sub1 extends Class34 implements MouseWheelListener
{
    public int anInt1319 = 0;
    
    public void method332(int i, Component component) {
	if (i == 99)
	    component.removeMouseWheelListener(this);
    }
    
    public synchronized int method338(int i) {
	int i_0_ = anInt1319;
	anInt1319 = 0;
	if (i < 124)
	    anInt1319 = 62;
	return i_0_;
    }
    
    public synchronized void mouseWheelMoved(MouseWheelEvent mousewheelevent) {
	anInt1319 += mousewheelevent.getWheelRotation();
    }
    
    public void method337(Component component, byte i) {
	if (i > 69)
	    component.addMouseWheelListener(this);
    }
}
