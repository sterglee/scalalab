package scalaSci.math.io.littleendian;

import java.io.*;

/**
 * This class was designed on the base of Roedy Green LEDataInputStream
 * 
 * Thanks to Roedy Green, Canadian Mind Products mailto:roedy@mindprod.com
 * http://mindprod.com
 * 
 * @author Yann RICHET
 */

public class LEDataInputStream implements DataInput {

    protected DataInputStream d;

    protected InputStream in;

    byte w[];

    public LEDataInputStream(InputStream in) {
        this.in = in;
        this.d = new DataInputStream(in);
        w = new byte[8];
    }

    public short readShort() throws IOException {
        d.readFully(w, 0, 2);
        return (short) ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    public int readUnsignedShort() throws IOException {
        d.readFully(w, 0, 2);
        return ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    public char readChar() throws IOException {
        d.readFully(w, 0, 2);
        return (char) ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    public int readInt() throws IOException {
        d.readFully(w, 0, 4);
        return (w[3]) << 24 | (w[2] & 0xff) << 16 | (w[1] & 0xff) << 8
                | (w[0] & 0xff);
    }

    public long readLong() throws IOException {
        d.readFully(w, 0, 8);
        return (long) (w[7]) << 56 | (long) (w[6] & 0xff) << 48
                | (long) (w[5] & 0xff) << 40 | (long) (w[4] & 0xff) << 32
                | (long) (w[3] & 0xff) << 24 | (long) (w[2] & 0xff) << 16
                | (long) (w[1] & 0xff) << 8 | (long) (w[0] & 0xff);
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public void readFully(byte b[]) throws IOException {
        d.readFully(b, 0, b.length);
    }

    public void readFully(byte b[], int off, int len) throws IOException {
        d.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return d.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return d.readBoolean();
    }

    public byte readByte() throws IOException {
        return d.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return d.readUnsignedByte();
    }

    public String readLine() throws IOException {
        return null;// d.readLine();
    }

    public String readUTF() throws IOException {
        return d.readUTF();
    }

    public static String readUTF(DataInput in) throws IOException {
        return DataInputStream.readUTF(in);
    }

    public void close() throws IOException {
        d.close();
    }
}