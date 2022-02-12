package scalaSci.math.io.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import scalaSci.math.io.littleendian.LEDataInputStream;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class BinaryInputStream extends InputStream {

    private InputStream stream;

    public static String LITTLE_ENDIAN = "LITTLE_ENDIAN";

    public static String BIG_ENDIAN = "BIG_ENDIAN";

    protected boolean bigEndian;

    public BinaryInputStream(InputStream in, String endian) {
        bigEndian = isBigEndian(endian);
        stream = in;
    }

    public BinaryInputStream(InputStream in, boolean endian) {
        bigEndian = endian;
        stream = in;
    }

    private boolean isBigEndian(String endian) {
        boolean be;
        if (endian.equals(LITTLE_ENDIAN)) {
            be = false;
        } else if (endian.equals(BIG_ENDIAN)) {
            be = true;
        } else {
            throw new IllegalArgumentException(
                    "The Endian type : "
                            + endian
                            + "is unknown. You must specify LITTLE_ENDIAN or BIG_ENDIAN.");
        }
        return be;
    }

    public static double[] readDoubleArray(InputStream in, String endian) {
        BinaryInputStream bi = new BinaryInputStream(in, endian);
        return bi.readDoubleArray();
    }

    public static float[] readFloatArray(InputStream in, String endian) {
        BinaryInputStream bi = new BinaryInputStream(in, endian);
        return bi.readFloatArray();
    }

    public static int[] readIntArray(InputStream in, String endian) {
        BinaryInputStream bi = new BinaryInputStream(in, endian);
        return bi.readIntArray();
    }

    /**
     * Read a binary File
     * 
     * @return int[]
     */
    public int[] readIntArray() {
        try {
            DataInput dis;
            if (bigEndian) {
                dis = new DataInputStream(stream);
            } else {
                dis = new LEDataInputStream(stream);
            }

            Vector<Integer> intV = new Vector<Integer>();

            try {
                while (true) {
                    int i = dis.readInt();
                    intV.add(new Integer(i));
                }
            } catch (EOFException eof) {
                stream.close();
            }

            int[] array = new int[intV.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ((Integer) intV.get(i)).intValue();
            }

            return array;
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unreadable : "
                    + e.toString());
        }
    }

    /**
     * Read a binary File
     * 
     * @return float[]
     */
    public float[] readFloatArray() {
        try {
            DataInput dis;
            if (bigEndian) {
                dis = new DataInputStream(stream);
            } else {
                dis = new LEDataInputStream(stream);
            }

            Vector<Float> floatV = new Vector<Float>();

            try {
                while (true) {
                    float f = dis.readFloat();
                    floatV.add(new Float(f));
                }
            } catch (EOFException eof) {
                stream.close();
            }

            float[] array = new float[floatV.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ((Float) floatV.get(i)).floatValue();
            }

            return array;
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unreadable : "
                    + e.toString());
        }
    }

    /**
     * Read a binary File
     * 
     * @return double[]
     */
    public double[] readDoubleArray() {
        try {
            DataInput dis;
            if (bigEndian) {
                dis = new DataInputStream(stream);
            } else {
                dis = new LEDataInputStream(stream);
            }

            Vector<Double> doubleV = new Vector<Double>();

            try {
                while (true) {
                    double f = dis.readDouble();
                    doubleV.add(new Double(f));
                }
            } catch (EOFException eof) {
                stream.close();
            }

            double[] array = new double[doubleV.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ((Double) doubleV.get(i)).doubleValue();
            }

            return array;
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unreadable : "
                    + e.toString());
        }
    }

    /**
     * Read a binary File
     * 
     * @return double[]
     */
    public double[] readDoubleArray(int n1, int n2) {
        try {
            DataInput dis;
            if (bigEndian) {
                dis = new DataInputStream(stream);
            } else {
                dis = new LEDataInputStream(stream);
            }

            double[] array = new double[n2 - n1];

            dis.skipBytes(n1 * 4);
            for (int i = 0; i < array.length; i++) {
                array[i] = dis.readDouble();
            }

            return array;
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unreadable : "
                    + e.toString());
        }
    }

    /**
     * Read a binary File
     * 
     * @return byte[]
     */
    public byte[] readByteArray() {
        try {
            DataInput dis;
            if (bigEndian) {
                dis = new DataInputStream(stream);
            } else {
                dis = new LEDataInputStream(stream);
            }

            Vector<Byte> bytesV = new Vector<Byte>();

            try {
                while (true) {
                    byte b = dis.readByte();
                    bytesV.add(new Byte(b));
                }
            } catch (EOFException eof) {
                stream.close();
            }

            byte[] bytes = new byte[bytesV.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = ((Byte) bytesV.get(i)).byteValue();
            }

            return bytes;
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unreadable : "
                    + e.toString());
        }
    }

    /**
     * Read a binary File
     * 
     * @return int
     */
    public int readInt() {
        return readIntArray()[0];
    }

    /**
     * Read a binary File
     * 
     * @return float
     */
    public float readFloat() {
        return readFloatArray()[0];
    }

    /**
     * Read a binary File
     * 
     * @return double
     */
    public double readDouble() {
        return readDoubleArray()[0];
    }

    /**
     * Read a binary File
     * 
     * @return byte
     */
    public byte readByte() {
        return readByteArray()[0];
    }

    /**
     * Read a binary File
     * 
     * @return byte
     */
    public int read() {
        return readInt();
    }

}