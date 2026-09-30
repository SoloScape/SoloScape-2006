package jagex.io;

/* Class8 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileOnDisk
{
    public long currentPosition;
    public long maximumLength;
    public File file;
    public RandomAccessFile randomAccessFile;
    
    public void destroy() throws IOException {
	randomAccessFile.close();
	randomAccessFile = null;
    }
    
    public File getFile() {
	return file;
    }
    
    public void seek(long position) throws IOException {
	randomAccessFile.seek(position);
	currentPosition = position;
    }
    
    public int read(byte[] src, int off, int len)
	throws IOException {
	int amountRead = randomAccessFile.read(src, off, len);
	if (amountRead > 0)
	    currentPosition += (long) amountRead;
	return amountRead;
    }
    
    public FileOnDisk(File f, String accessStr, long l) throws IOException {
	if (l == -1L)
	    l = 9223372036854775807L;
	if (l <= f.length())
	    f.delete();
	randomAccessFile = new RandomAccessFile(f, accessStr);
	currentPosition = 0L;
	file = f;
	maximumLength = l;
    }
    
    public long getLength() throws IOException {
	return randomAccessFile.length();
    }
    
    public void write(byte[] src, int off, int len)
	throws IOException {
	if (maximumLength < (long) len + currentPosition) {
	    randomAccessFile.seek(maximumLength + 1L);
	    randomAccessFile.write(1);
	    throw new EOFException();
	}
	randomAccessFile.write(src, off, len);
	currentPosition += (long) len;
    }
}
