package jagex.utils;

/* Class29 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class ArrayUtils {

    public static void arrayCopy(byte[] src, int srcOff, byte[] dest, int destOff, int len) {
        if (src == dest) {
            if (srcOff == destOff) {
                return;
            }
            if (destOff > srcOff && destOff < srcOff + len) {
                len--;
                srcOff += len;
                destOff += len;
                len = srcOff - len;
                len += 7;
                while (srcOff >= len) {
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                    dest[destOff--] = src[srcOff--];
                }
                len -= 7;
                while (srcOff >= len) {
                    dest[destOff--] = src[srcOff--];
                }
                return;
            }
        }
        len += srcOff;
        len -= 7;
        while (srcOff < len) {
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
            dest[destOff++] = src[srcOff++];
        }
        len += 7;
        while (srcOff < len) {
            dest[destOff++] = src[srcOff++];
        }
    }
}
