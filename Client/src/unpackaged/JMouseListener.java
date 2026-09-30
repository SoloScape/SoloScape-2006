package unpackaged;

/* Class42 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import jagex.world.actors.Projectile;
import jagex.utils.IsaacPrng;
import jagex.utils.JString;
import jagex.io.Buffer;
import jagex.io.JSocket;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;

public class JMouseListener
        implements MouseListener, MouseMotionListener, FocusListener {

    public static boolean aBoolean785 = false;
    public static Calendar aCalendar786 = Calendar.getInstance();
    public static int anInt787 = 0;
    public static Buffer odArchiveBuffer;
    public static JString aClass3_789;
    public static JString aClass3_790 = Class39_Sub5_Sub9.createJstring("sich mit einer anderen Welt zu verbinden)3");
    public static volatile int anInt791;
    public static JString aClass3_792;
    public static int anInt793;
    public static JString aClass3_794;
    public static JString aClass3_795;
    public static JString[] strVariables;
    private static boolean middleMouseDown = false;
    private static int middleMouseX = 0;
    private static int middleMouseY = 0;

    public static int method901(int i, int i_0_, int i_1_, boolean bool) {
        i &= 0x3;
        if (i == 0) {
            return i_0_;
        }
        if (i == 1) {
            return i_1_;
        }
        if (i == 2) {
            return 7 - i_0_;
        }
        return 7 - i_1_;
    }

    public void focusGained(FocusEvent focusevent) {
        /* empty */
    }

    public synchronized void mouseExited(MouseEvent mouseevent) {
        middleMouseDown = false;
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            Projectile.anInt2208 = -1;
            Class10.anInt172 = -1;
        }
    }

    public synchronized void mouseReleased(MouseEvent mouseevent) {
        if (mouseevent.getButton() == MouseEvent.BUTTON2) {
            middleMouseDown = false;
            mouseevent.consume();
            return;
        }
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            ClientScript.anInt1699 = 0;
        }
        if (mouseevent.isPopupTrigger()) {
            mouseevent.consume();
        }
    }

    public static void method902(JString class3, JString class3_2_, boolean bool,
            int i) {
        if (i == 0 && IsaacPrng.anInt1095 != -1) {
            OndemandRequest.aClass3_1714 = class3_2_;
            Class46.anInt887 = 0;
        }
        if (Class39_Sub5_Sub14.anInt1912 == -1) {
            Class14.aBoolean245 = true;
        }
        for (int i_3_ = 99; i_3_ > 0; i_3_--) {
            Client.anIntArray1268[i_3_] = Client.anIntArray1268[i_3_ - 1];
            Class39_Sub11.aClass3Array1462[i_3_] = Class39_Sub11.aClass3Array1462[i_3_ - 1];
            Class2.aClass3Array52[i_3_] = Class2.aClass3Array52[i_3_ - 1];
        }
        Client.anIntArray1268[0] = i;
        Class39_Sub11.aClass3Array1462[0] = class3;
        Class2.aClass3Array52[0] = class3_2_;
    }

    public synchronized void mousePressed(MouseEvent mouseevent) {
        if (mouseevent.getButton() == MouseEvent.BUTTON2) {
            middleMouseDown = true;
            middleMouseX = mouseevent.getX();
            middleMouseY = mouseevent.getY();
            FileLoader.anInt1302 = 0;
            mouseevent.consume();
            return;
        }
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            anInt791 = mouseevent.getX();
            Class41.anInt784 = mouseevent.getY();
            Class2.aLong55 = Class2.getSystemTime();
            if (mouseevent.isMetaDown()) {
                Class23.anInt430 = 2;
                ClientScript.anInt1699 = 2;
            } else {
                Class23.anInt430 = 1;
                ClientScript.anInt1699 = 1;
            }
        }
        if (mouseevent.isPopupTrigger()) {
            mouseevent.consume();
        }
    }

    public synchronized void mouseDragged(MouseEvent mouseevent) {
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            Projectile.anInt2208 = mouseevent.getX();
            Class10.anInt172 = mouseevent.getY();
            if (middleMouseDown) {
                int deltaX = mouseevent.getX() - middleMouseX;
                int deltaY = mouseevent.getY() - middleMouseY;
                middleMouseX = mouseevent.getX();
                middleMouseY = mouseevent.getY();

                Class34.anInt605 = (Class34.anInt605 - deltaX * 2) & 0x7ff;
                JSocket.anInt301 += deltaY;
                if (JSocket.anInt301 < 128) {
                    JSocket.anInt301 = 128;
                } else if (JSocket.anInt301 > 383) {
                    JSocket.anInt301 = 383;
                }
                mouseevent.consume();
            }
        }
    }

    public synchronized void mouseMoved(MouseEvent mouseevent) {
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            Projectile.anInt2208 = mouseevent.getX();
            Class10.anInt172 = mouseevent.getY();
        }
    }

    public synchronized void focusLost(FocusEvent focusevent) {
        middleMouseDown = false;
        if (Class39_Sub13.aClass42_1502 != null) {
            ClientScript.anInt1699 = 0;
        }
    }

    public synchronized void mouseEntered(MouseEvent mouseevent) {
        if (Class39_Sub13.aClass42_1502 != null) {
            FileLoader.anInt1302 = 0;
            Projectile.anInt2208 = mouseevent.getX();
            Class10.anInt172 = mouseevent.getY();
        }
    }

    public void mouseClicked(MouseEvent mouseevent) {
        if (mouseevent.isPopupTrigger()) {
            mouseevent.consume();
        }
    }

    public static void method903(int i) {
        aClass3_795 = null;
        aClass3_794 = null;
        aClass3_789 = null;
        aClass3_792 = null;
        odArchiveBuffer = null;
        aCalendar786 = null;
        strVariables = null;
        aClass3_790 = null;
    }

    static {
        aClass3_789 = Class39_Sub5_Sub9.createJstring("green:");
        aClass3_792 = Class39_Sub5_Sub9.createJstring(":chalreq:");
        anInt791 = 0;
        aClass3_794 = Class39_Sub5_Sub9.createJstring("@gr2@");
        aClass3_795 = aClass3_789;
    }
}
