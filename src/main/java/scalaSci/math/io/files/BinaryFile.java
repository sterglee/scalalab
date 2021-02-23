package scalaSci.math.io.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import scalaSci.math.io.stream.BinaryInputStream;
import scalaSci.math.io.stream.BinaryOutputStream;

// 9-Jan
public class BinaryFile {
    public static String LITTLE_ENDIAN = "LITTLE_ENDIAN";
    public static String BIG_ENDIAN = "BIG_ENDIAN";
    private boolean bigEndian;
    private File file;

    public BinaryFile(File f, String endian) {
        file = f;
        bigEndian = isBigEndian(endian);
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

    public static double[] readDoubleArray(File f, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        return bf.readDoubleArray();
    }

    public static float[] readFloatArray(File f, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        return bf.readFloatArray();
    }

    public static int[] readIntArray(File f, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        return bf.readIntArray();
    }

    public static void writeDoubleArray(File f, double[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeDoubleArray(array, false);
    }

    public static void appendDoubleArray(File f, double[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeDoubleArray(array, true);
    }

    public static void writeFloatArray(File f, float[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeFloatArray(array, false);
    }

    public static void appendFloatArray(File f, float[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeFloatArray(array, true);
    }

    public static void writeIntArray(File f, int[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeIntArray(array, false);
    }

    public static void appendIntArray(File f, int[] array, String endian) {
        BinaryFile bf = new BinaryFile(f, endian);
        bf.writeIntArray(array, true);
    }

    /**
     * Read a binary File
     * 
     * @return int[]
     */
    public int[] readIntArray() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        BinaryInputStream bs = new BinaryInputStream(bis, bigEndian);
        return bs.readIntArray();
    }

    /**
     * Read a binary File
     * 
     * @return float[]
     */
    public float[] readFloatArray() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        BinaryInputStream bs = new BinaryInputStream(bis, bigEndian);
        return bs.readFloatArray();
    }

    /**
     * Read a binary File
     * 
     * @return double[]
     */
    public double[] readDoubleArray() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        BinaryInputStream bs = new BinaryInputStream(bis, bigEndian);
        return bs.readDoubleArray();
    }

    /**
     * Read a binary File
     * 
     * @return byte[]
     */
    public byte[] readByteArray() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        BinaryInputStream bs = new BinaryInputStream(bis, bigEndian);
        return bs.readByteArray();
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
        if (file.exists()) {
            System.out.println("Warning : the file " + file.getName()
                    + " already exists !");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BinaryOutputStream bs = new BinaryOutputStream(bos, bigEndian);
        bs.writeIntArray(array, append);
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
        if (file.exists()) {
            System.out.println("Warning : the file " + file.getName()
                    + " already exists !");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BinaryOutputStream bs = new BinaryOutputStream(bos, bigEndian);
        bs.writeFloatArray(array, append);
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
        if (file.exists()) {
            System.out.println("Warning : the file " + file.getName()
                    + " already exists !");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BinaryOutputStream bs = new BinaryOutputStream(bos, bigEndian);
        bs.writeDoubleArray(array, append);
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
        if (file.exists()) {
            System.out.println("Warning : the file " + file.getName()
                    + " already exists !");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BinaryOutputStream bs = new BinaryOutputStream(bos, bigEndian);
        bs.writeByteArray(bytes, append);
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

    public static void main(String[] args) {
        if (args[0].equals("-readarray")) {
            String man = "Usage: BinaryFile -readarray file [option]\n[options] are:\n  -endian <big|little, default = big>\n  -data <double|float|int, default = double>";

            File file = null;
            String data = "double";
            String endian = BIG_ENDIAN;

            for (int i = 1; i < args.length; i++) {
                if (args[i].equals("-endian")) {
                    if (args[i + 1].equals("little"))
                        endian = LITTLE_ENDIAN;
                    i++;
                } else if (args[i].equals("-data")) {
                    data = args[i + 1];
                    i++;
                } else {
                    file = new File(args[i]);
                    if (!file.exists())
                        System.out.println("File " + file
                                + " doesn't exists.\n" + man);
                    i++;
                }
            }

            if (data.equals("double")) {
                double[] d = readDoubleArray(file, endian);
                for (int j = 0; j < d.length; j++) {
                    System.out.println(d[j] + "");
                }
            } else if (data.equals("float")) {
                float[] d = readFloatArray(file, endian);
                for (int j = 0; j < d.length; j++) {
                    System.out.println(d[j] + "");
                }
            } else if (data.equals("int")) {
                int[] d = readIntArray(file, endian);
                for (int j = 0; j < d.length; j++) {
                    System.out.println(d[j] + "");
                }
            } else
                System.out.println(man);

        } else {
            System.out.println("Option not implemented.");
        }

    }
}