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

public class LEDataOutputStream implements DataOutput {

    protected DataOutputStream d;

    byte w[];

    public LEDataOutputStream(OutputStream out) {
        this.d = new DataOutputStream(out);
        w = new byte[8];
    }

    public final void writeShort(int v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        d.write(w, 0, 2);
    }

    public final void writeChar(int v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        d.write(w, 0, 2);
    }

    public final void writeInt(int v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        w[2] = (byte) (v >> 16);
        w[3] = (byte) (v >> 24);
        d.write(w, 0, 4);
    }

    public final void writeLong(long v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        w[2] = (byte) (v >> 16);
        w[3] = (byte) (v >> 24);
        w[4] = (byte) (v >> 32);
        w[5] = (byte) (v >> 40);
        w[6] = (byte) (v >> 48);
        w[7] = (byte) (v >> 56);
        d.write(w, 0, 8);
    }

    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public final void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            writeChar(s.charAt(i));
        }
    }

    public final synchronized void write(int b) throws IOException {
        d.write(b);
    }

    public final synchronized void write(byte b[], int off, int len)
            throws IOException {
        d.write(b, off, len);
    }

    public void flush() throws IOException {
        d.flush();
    }

    public final void writeBoolean(boolean v) throws IOException {
        d.writeBoolean(v);
    }

    public final void writeByte(int v) throws IOException {
        d.writeByte(v);
    }

    public final void writeBytes(String s) throws IOException {
        d.writeBytes(s);
    }

    public final void writeUTF(String str) throws IOException {
        d.writeUTF(str);
    }

    public final int size() {
        return d.size();
    }

    public final void write(byte b[]) throws IOException {
        d.write(b, 0, b.length);
    }

    public final void close() throws IOException {
        d.close();
    }

}