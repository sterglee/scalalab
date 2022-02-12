package scalaSci.math.io.stream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import scalaSci.math.io.littleendian.LEDataOutputStream;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class BinaryOutputStream extends OutputStream {

    private OutputStream stream;

    public static String LITTLE_ENDIAN = "LITTLE_ENDIAN";

    public static String BIG_ENDIAN = "BIG_ENDIAN";

    protected boolean bigEndian;

    public BinaryOutputStream(OutputStream out, String endian) {
        bigEndian = isBigEndian(endian);
        stream = out;
    }

    public BinaryOutputStream(OutputStream out, boolean endian) {
        bigEndian = endian;
        stream = out;
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

    public static void writeDoubleArray(OutputStream out, double[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeDoubleArray(array, false);
    }

    public static void appendDoubleArray(OutputStream out, double[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeDoubleArray(array, true);
    }

    public static void writeFloatArray(OutputStream out, float[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeFloatArray(array, false);
    }

    public static void appendFloatArray(OutputStream out, float[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeFloatArray(array, true);
    }

    public static void writeIntArray(OutputStream out, int[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeIntArray(array, false);
    }

    public static void appendIntArray(OutputStream out, int[] array,
            String endian) {
        BinaryOutputStream bs = new BinaryOutputStream(out, endian);
        bs.writeIntArray(array, true);
    }

    /**
     * Write an int array in a binary File
     * 
     * @param array
     *            int[]
     * @param append
     *            boolean
     */

    public void writeIntArray(int[] array, boolean append) {
        try {
            DataOutput dos;
            if (bigEndian) {
                dos = new DataOutputStream(stream);
            } else {
                dos = new LEDataOutputStream(stream);
            }

            for (int i = 0; i < array.length; i++) {
                dos.writeInt(array[i]);
            }

            stream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unwritable : "
                    + e.toString());
        }
    }

    /**
     * Write a float array in a binary File
     * 
     * @param array
     *            float[]
     * @param append
     *            boolean
     */

    public void writeFloatArray(float[] array, boolean append) {
        try {
            DataOutput dos;
            if (bigEndian) {
                dos = new DataOutputStream(stream);
            } else {
                dos = new LEDataOutputStream(stream);
            }

            for (int i = 0; i < array.length; i++) {
                dos.writeFloat(array[i]);
            }

            stream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unwritable : "
                    + e.toString());
        }
    }

    /**
     * Write a double array in a binary File
     * 
     * @param array
     *            float[]
     * @param append
     *            boolean
     */

    public void writeDoubleArray(double[] array, boolean append) {
        try {
            DataOutput dos;
            if (bigEndian) {
                dos = new DataOutputStream(stream);
            } else {
                dos = new LEDataOutputStream(stream);
            }

            for (int i = 0; i < array.length; i++) {
                dos.writeDouble(array[i]);
            }

            stream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unwritable : "
                    + e.toString());
        }
    }

    /**
     * Write a text in a binary File
     * 
     * @param bytes
     *            byte[]
     * @param append
     *            boolean
     */

    public void writeByteArray(byte[] bytes, boolean append) {
        try {
            DataOutputStream dos = new DataOutputStream(stream);

            dos.write(bytes);

            stream.close();

        } catch (IOException e) {
            throw new IllegalArgumentException("InputStream is unwritable : "
                    + e.toString());
        }
    }

    /**
     * Write an int in a binary File
     * 
     * @param i
     *            int
     * @param append
     *            boolean
     */

    public void writeInt(int i, boolean append) {
        writeIntArray(new int[] { i }, append);
    }

    /**
     * Write a float in a binary File
     * 
     * @param f
     *            float
     * @param append
     *            boolean
     */

    public void writeFloat(float f, boolean append) {
        writeFloatArray(new float[] { f }, append);
    }

    /**
     * Write a double in a binary File
     * 
     * @param d
     *            double
     * @param append
     *            boolean
     */

    public void writeDouble(double d, boolean append) {
        writeDoubleArray(new double[] { d }, append);
    }

    /**
     * Write a text in a binary File
     * 
     * @param b
     *            byte
     * @param append
     *            boolean
     */

    public void writeByte(byte b, boolean append) {
        writeByteArray(new byte[] { b }, append);
    }

    /**
     * Write a text in a binary File
     * 
     * @param b
     *            byte
     * @param append
     *            boolean
     */

    public void write(int i) {
        writeInt(i, false);
    }

}