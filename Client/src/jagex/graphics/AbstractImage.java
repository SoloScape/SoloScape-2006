package jagex.graphics;

/* Class57 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import jagex.graphics.sprites.IndexedColorSprite;
import jagex.utils.JString;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import unpackaged.Class43;
import unpackaged.Class46;
import unpackaged.Class46_Sub1;

public abstract class AbstractImage
{
    public int[] buffer;
    public Image image;
    public static boolean aBoolean1000 = true;
    public int width;
    public static IndexedColorSprite[] aClass39_Sub5_Sub10_Sub4Array1002;
    public static int[] globalIntVars = new int[2000];
    public int height;
    
    public abstract void setComponent(Component component,int i, int i_0_);
    
    public void method1006(int i) {
	DrawingArea.setBuffer(buffer, width, height);
    }
    
    public static JString method1007(byte i, int i_2_) {
	if (i != 71)
	    method1007((byte) -77, 115);
	return Class43.method907(false, 10, i ^ ~0x35, i_2_);
    }
    
    public static void method1008(int i) {
	globalIntVars = null;
	aClass39_Sub5_Sub10_Sub4Array1002 = null;
    }
    
    public static Class46 method1009(byte i) {
	try {
	    return (Class46) Class.forName("unpackaged.Class46_Sub2").newInstance();
	} catch (Throwable throwable) {
	    return new Class46_Sub1();
	}
    }
    
    public abstract void draw(Graphics graphics, int width, int height);
}
