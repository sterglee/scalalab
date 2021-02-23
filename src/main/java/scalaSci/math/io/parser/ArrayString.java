package scalaSci.math.io.parser;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */
// 9-Jan
public class ArrayString {
    //private static int decimalSize = 10;
    public static String defaultColumnDelimiter = " ";
    public static String defaultRowDelimiter = "\n";
    public static String printDoubleArray(double[] m) {
        return printDoubleArray(new double[][] { m });
    }

    public static String printDoubleArray(double[][] m) {
        return printDoubleArray(m, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static String printDoubleArray(double[][] m, String wordDelimiter, String sentenceDelimiter) {
        StringBuffer str = new StringBuffer(25 * m.length * m[0].length);
    // //can't use format because of infinty which become "?" strings...
    // DecimalFormat format = new DecimalFormat();
    // format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
    // format.setMinimumIntegerDigits(1);
    // format.setMaximumFractionDigits(decimalSize);
    // format.setMinimumFractionDigits(decimalSize);
    // format.setGroupingUsed(false);

    for (int i = 0; i < m.length; i++) {
        for (int j = 0; j < m[i].length - 1; j++) {
            // String s = format.format(m[i][j]); // format the number
        str = str.append(Double.toString(m[i][j]));
        str = str.append(wordDelimiter);
        }
    str = str.append(Double.toString(m[i][m[i].length - 1]));
    if (i < m.length - 1) {
        str = str.append(sentenceDelimiter);
        }
    }
    return str.toString();
}
    
    public static String printIntArray(int[] m) {
        return printIntArray(new int[][] { m });
    }

    public static String printIntArray(int[][] m) {
        return printIntArray(m, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static String printIntArray(int[][] m, String wordDelimiter, String sentenceDelimiter) {
        StringBuffer str = new StringBuffer(25 * m.length * m[0].length);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length - 1; j++) {
	str = str.append(Integer.toString(m[i][j]));
	str = str.append(wordDelimiter);
    }
    str = str.append(Integer.toString(m[i][m[i].length - 1]));
    if (i < m.length - 1) {
        str = str.append(sentenceDelimiter);
        }
    }
    return str.toString();
    }

    public static double[] readString1DDouble(String s) {
        return readString1DDouble(s, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static double[] readString1DDouble(String s, String columnDelimiter, String sentenceDelimiter) {
        double[][] d = readStringDouble(s, columnDelimiter, sentenceDelimiter);
        double[] d1D = null;
        if (d.length > 1) {
            d1D = new double[d.length];
            for (int i = 0; i < d1D.length; i++)
                d1D[i] = d[i][0];
        } else
        d1D = d[0];
     return d1D;
    }

    public static int[] readString1DInt(String s) {
        return readString1DInt(s, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static int[] readString1DInt(String s, String columnDelimiter, String sentenceDelimiter) {
        int[][] d = readStringInt(s, columnDelimiter, sentenceDelimiter);
        int[] d1D = null;
        if (d.length > 1) {
            d1D = new int[d.length];
            for (int i = 0; i < d1D.length; i++)
                d1D[i] = d[i][0];
        } else
            d1D = d[0];
        return d1D;
    }

    public static double[][] readStringDouble(String s) {
        return readStringDouble(s, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static double[][] readStringDouble(String s, String columnDelimiter, String rowDelimiter) {
        double[][] array;
        String[] rows = s.split(rowDelimiter);
        array = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(columnDelimiter);
            array[i] = new double[cols.length];
            for (int j = 0; j < cols.length; j++) {
                array[i][j] = Double.parseDouble(cols[j]);
            }
    }

    return array;
    }

    public static int[][] readStringInt(String s) {
        return readStringInt(s, defaultColumnDelimiter, defaultRowDelimiter);
    }

    public static int[][] readStringInt(String s, String columnDelimiter, String rowDelimiter) {
        int[][] array;
        String[] rows = s.split(rowDelimiter);
        array = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(columnDelimiter);
            array[i] = new int[cols.length];
            for (int j = 0; j < cols.length; j++) {
	array[i][j] = Integer.parseInt(cols[j]);
                }
            }
    return array;
    }

}
