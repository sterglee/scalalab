package scalaSci.math.array;

import edu.emory.mathcs.utils.ConcurrencyUtils;
import java.util.concurrent.Future;

import scalaSci.math.array.util.Function;
import scalaSci.math.array.util.IndexFunction;
import scalaSci.math.array.util.Random;
import scalaSci.math.array.util.Sorting;
import scalaExec.Interpreter.GlobalValues;


public class DoubleArray {
    static public double log2Conv = Math.log(2.0);

    // Basic Manipulation methods

/**
 * Adds a single value to a matrix, e.g. adds the constant value a
* to all element of the matrix M.
*
* @returns The resulting matrix.
*/
public static final double[][] add(double[][] M, double a) {
  for (int i = 0; i < M.length; i++)
     for (int j = 0; j < M[i].length; j++)
      M[i][j] = M[i][j] + a;
  return M;
 }

 public static final double[][] Subtract(double[][] M, double a) {
  for (int i = 0; i < M.length; i++)
    for (int j = 0; j < M[i].length; j++)
        M[i][j] = M[i][j] - a;
  return M;
}

public static final double[][] Add(double[][] M, double a) {
   int Rows = M.length;  int Cols = M[0].length;
   double [][]res = new double[Rows][Cols];
   for (int i = 0; i < Rows; i++)
   for (int j = 0; j < Cols; j++)
      res[i][j] = M[i][j] + a;
return res;
 }

public static final double[][] Add(double[][] M, double [][]M2) {
     int Rows = M.length;  int Cols = M[0].length;
     double [][]res = new double[Rows][Cols];
     for (int i = 0; i < Rows; i++)
        for (int j = 0; j < Cols; j++)
           res[i][j] = M[i][j] + M2[i][j];
   return res;
}

public static final double[][] Subtract(double[][] M, double [][]M2) {
    int Rows = M.length;  int Cols = M[0].length;
    double [][]res = new double[Rows][Cols];
      for (int i = 0; i < Rows; i++)
          for (int j = 0; j < Cols; j++)
	res[i][j] = M[i][j] - M2[i][j];
 return res;
}

        

 public static final double[][]  psin(final double [][] M) {
      final int    Nrows =M.length;   final int  Ncols = M[0].length;
        
      
  final double [][]   vr = new double[Nrows][Ncols];   // for computing the return Matrix
  int  nthreads = GlobalValues.numOfThreads;
  nthreads = Math.min(nthreads, Nrows);  // larger number of threads than the number of cores of the system deteriorate performance
  
  
  Future<?>[] futures = new Future[nthreads];
            
  int   rowsPerThread = (int)(Nrows / nthreads)+1;  // how many rows the thread processes

  int threadId = 0;  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    final int  firstRow = threadId * rowsPerThread;
    final int  lastRow =   threadId == nthreads-1? Nrows: firstRow+rowsPerThread;
    
    
 futures[threadId] = GlobalValues.execService.submit(new Runnable() {
     public void run()  {
         for (int row=firstRow; row< lastRow; row++) 
             for (int col = 0; col < Ncols; col++ )
                 vr[row][col] = Math.sin(M[row][col]);
      }
   });

        threadId++;
        
  }  // for all threads

   // wait for all the multiplication worker threads to complete
  ConcurrencyUtils.waitForCompletion(futures);
  
  return vr;
	
    
    }


     
 public static final double[][]  sin(double [][] M) {
       double [][] Ms = new double[M.length][M[0].length];
       for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[i].length; j++)
	 Ms[i][j] = Math.sin(M[i][j]);
  return Ms;
 }


  
  public static final double[][]  cos(double [][] M) {
      double [][] Ms = new double[M.length][M[0].length];
      for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
          Ms[i][j] = Math.cos(M[i][j]);
 return Ms;
}
  
  public static final double[][]  tan(double [][] M) {
       double [][] Ms = new double[M.length][M[0].length];
       for (int i = 0; i < M.length; i++)
       for (int j = 0; j < M[i].length; j++)
         Ms[i][j] = Math.tan(M[i][j]);
  return Ms;
 }
  
  
  public static final double[][]  asin(double [][] M) {
     double [][] Ms = new double[M.length][M[0].length];
      for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
	 Ms[i][j] = Math.asin(M[i][j]);
   return Ms;
 }
  
  
  public static final double[][]  acos(double [][] M) {
      double [][] Ms = new double[M.length][M[0].length];
      for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.acos(M[i][j]);
   return Ms;
}
  
 public static final double[][]  atan(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.atan(M[i][j]);
    return Ms;
}
  
  
public static final double[][]  sinh(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.sinh(M[i][j]);
    return Ms;
}
  
  
public static final double[][]  cosh(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
    Ms[i][j] = Math.cosh(M[i][j]);
return Ms;
}
  
  
 public static final double[][]  tanh(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.tanh(M[i][j]);
return Ms;
}
  
  public static final  double[][]  exp(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.exp(M[i][j]);
return Ms;
}
  
  public static final double[][]  log(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
      for (int j = 0; j < M[i].length; j++)
        Ms[i][j] = Math.log(M[i][j]);
 return Ms;
}
  
  public static final double[][]  log2(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
     for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[i].length; j++)
	Ms[i][j] = Math.log(M[i][j])/log2Conv;
   return Ms;
}
  
  
  public static final double[][]  log10(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.log10(M[i][j]);
    return Ms;
}
  
  
  public static final double[][]  abs(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.abs(M[i][j]);
    return Ms;
}
  
  
  public static final double[][]  ceil(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.ceil(M[i][j]);
    return Ms;
}
  
  
  public static final double[][]  floor(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.floor(M[i][j]);
    return Ms;
}
  
public static final  double[][]  round(double [][] M) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[i].length; j++)
	Ms[i][j] = Math.round(M[i][j]);
 return Ms;
}
  
  public static final double[][]  pow(double [][] M, double exponent ) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.pow(M[i][j],  exponent );
    return Ms;
}
  
  
  public static final double[][]  sqrt(double [][] M ) {
     double [][] Ms = new double[M.length][M[0].length];
     for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.sqrt(M[i][j]);
    return Ms;
}
  
  
  public static final double[][]  toDegress(double [][] M ) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.toDegrees(M[i][j]);
return Ms;
}
  
  public static final double[][]  toRadians(double [][] M ) {
    double [][] Ms = new double[M.length][M[0].length];
    for (int i = 0; i < M.length; i++)
        for (int j = 0; j < M[i].length; j++)
            Ms[i][j] = Math.toRadians(M[i][j]);
    return Ms;
}
  
  
  /**
* Adds a matrix to another matrix, which should be of the same dimension.
*
* @returns The resulting matrix.
*/
public static final double[][] add(double[][] M1, double[][] M2) {
	if (M1.length != M2.length) {
	System.err.println("Matrices must be of the same dimension");
	return M1;
	}
    double[][] M = new double[M1.length][M1[0].length];
    for (int i = 0; i < M1.length; i++) {
        if (M1[i].length != M2[i].length) {
            System.err.println("Matrices must be of the same dimension");
            return M1;
	}
        for (int j = 0; j < M1[i].length; j++) {
            M[i][j] = M1[i][j] + M2[i][j];
    }
  }
    return M;
}

// Create methods

// Generates an m x m identity matrix. Result has ones along the diagonal
public static final double[][] identity(int m) {
   return diagonal(m, 1.0);
 }

//  Returns an m x m matrix. result has constants along the diagonal
// and zeros everywhere else. 
public static final double[][] diagonal(int m, double c) {
    if (m < 1)
      throw new IllegalArgumentException("First argument must be > 0");
    double[][] I = new double[m][m];
    for (int i = 0; i < I.length; i++)
        I[i][i] = c;
    return I;
}

//  Returns an m x m matrix. result has specified values along the diagonal
// and zeros everywhere else.
 public static final double[][] diagonal(double... c) {
    double[][] I = new double[c.length][c.length];
    for (int i = 0; i < I.length; i++)
        I[i][i] = c[i];
    return I;
}

// Provides an m x n matrix filled with ones.
 public static final double[][] one(int m, int n) {
    return fill(m, n, 1.0);
 }

        public static double[][] zero(int m, int n) {
		return fill(m, n, 0.0);
	}
	/**
	 * Provides an mxn matrix filled with constant c
	 * @param m Number of rows in returned matrix
	 * @param n Number of columns in returned matrix
	 * @param c Constant that fills matrix
	 * @return m x n 2D array of constants
	 * @deprecated use <code>fill(int m, int n, double c)</code> instead.
	 */
	public static double[][] one(int m, int n, double c) {
		return fill(m, n, c);
	}

	/**
	 * Provides an m element array filled with ones
	 * @param m Number of elements in returned array
	 * @return m element array of ones
	 * @deprecated use <code>fill(int m, double c)</code> instead.
	 */
	public static double[] one(int m) {
		return fill(m, 1.0);
	}

	/**
	 * Provides an m element array filled with constant c
	 * @param m Number of elements in returned array
	 * @param c Constant that fills array
	 * @return m element array of constants
	 * @deprecated Use <code>fill(int m, double c)</code> instead.
	 */
	public static double[] one(int m, double c) {
		return fill(m, c);
	}

	// The fill methods are included so we can have equivalent methods in IntegerArray.
	// There are ambiguities between DoubleArray and IntegerArray if we try to implement
	// equivalent versions of the one() methods in both. the fill() methods are delineated
	// by the type of the fill constant. I wish I could get rid of one(m,n,c) and one(m,c)
	// but they are legacy code now.
	/**
	 * Fills an m x n matrix of doubles with constant c. Example:<br>
	 * <code>
	 * double[][] u = fill(2, 3, 1.0);<br>
	 * Result is:<br>
	 * 1.0 1.0 1.0<br>
	 * 1.0 1.0 1.0<br>
	 * </code>
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param c constant that fills matrix
	 * @return m x n 2D array of constants c
	 */
	public static double[][] fill(int m, int n, double c) {
		double[][] o = new double[m][n];
		for (int i = 0; i < o.length; i++)
			for (int j = 0; j < o[i].length; j++)
				o[i][j] = c;
		return o;
	}

	/**
	 * Provides an m element array filled with constant c. Example:<br>
	 * <code>
	 * double[] u = fill(3, 1.0);<br>
	 * Result is:<br>
	 * 1.0 1.0 1.0<br>
	 * </code>
	 * @param m Number of elements in returned array
	 * @param c Constant that fills array
	 * @return m element array of constants
	 */
	public static double[] fill(int m, double c) {
		double[] o = new double[m];
		for (int i = 0; i < o.length; i++)
			o[i] = c;
		return o;
	}

	/**
	 * Generates an m x n matrix of random numbers uniformly distributed
	 * between 0 and 1.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @return 2D array of random numbers.
	 */
	public static double[][] random(int m, int n) {
		double[][] array = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = Random.raw();
			}
		}
		return array;
	}

	/**
	 * Generates an m element array of random numbers uniformly distributed between 0 and 1.
	 * @param m Size of array
	 * @return Array of random numbers.
	 */
	public static double[] random(int m) {
		double[] array = new double[m];
		for (int i = 0; i < m; i++) {
			array[i] = Random.raw();
		}
		return array;
	}

	/**
	 * Generates an mxn matrix of random numbers uniformly distributed between min and max.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param min minimum value of the random numbers
	 * @param max maximum value of the random numbers
	 * @return 2D array of random numbers.
	 */
	public static double[][] random(int m, int n, double min, double max) {
		double[][] array = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = min + Random.raw() * (max - min);
			}
		}
		return array;
	}

	/**
	 * Generates an m element array of random numbers uniformly distributed between min and max.
	 * @param m Size of array
	 * @param min minimum value of the random numbers
	 * @param max maximum value of the random numbers
	 * @return Array of random numbers.
	 */
	public static double[] random(int m, double min, double max) {
		double[] array = new double[m];
		for (int i = 0; i < m; i++) {
			array[i] = min + Random.raw() * (max - min);
		}
		return array;
	}

	/**
	 * Generates an m x n matrix of random numbers uniformly distributed numbers.
	 * The bounds of the random numbers each column are in the arrays min and max.
	 * So the values in column j are uniformly distributed random numbers between min[j] and max[j]
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param min minimum value of the random numbers in a column
	 * @param max maximum value of the random numbers in a column
	 * @return 2D array of random numbers.
	 */
	public static double[][] random(int m, int n, double[] min, double[] max) {
		double[][] array = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = min[j] + Random.raw() * (max[j] - min[j]);
			}
		}
		return array;
	}

	/**
	 * Provides an mxn matrix. Each column is a sequence of successive values of stepsize
	 * <em>pitch</em> and starts at <em>begin</em>. The columns are all identical.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param begin First value in sequence
	 * @param pitch Step size of sequence
	 * @return m x n 2D array
	 */
	public static double[][] increment(int m, int n, double begin, double pitch) {
		double[][] array = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = begin + i * pitch;
			}
		}
		return array;
	}

	/**
	 * Provides a sequence of successive values in an array. e.g. increment(4, 3, 2) = {3,5,7,9}
	 * @param m Size of array
	 * @param begin Starting value
	 * @param pitch Step size
	 * @return m point array. Sequence of values from begin to begin + (m-1)*pitch
	 */
	public static double[] increment(int m, double begin, double pitch) {
		double[] array = new double[m];
		for (int i = 0; i < m; i++) {
			array[i] = begin + i * pitch;
		}
		return array;
	}

	/**
	 * Generates an mxn matrix. Each column is a sequence of succesive values. Each column
	 * has it's own starting value and step size.
	 * @param m Number of rows in matrix
	 * @param n Number of columns in matrix
	 * @param begin Array of starting values for each column. length must = n.
	 * @param pitch Array of step sizes for each column. length must = n.
	 * @return 2D array
	 */
	public static double[][] increment(int m, int n, double[] begin, double[] pitch) {
		if (begin.length != n || pitch.length != n)
			throw new IllegalArgumentException("Length of 3rd and 4th arguments must = second argument");
		double[][] array = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				array[i][j] = begin[j] + i * pitch[j];
			}
		}
		return array;
	}

	/**
	 * Generates an array of successive values from <em>begin</em> to <em>end</em> with step
	 * size <em>pitch</em>.
	 * @param begin First value in sequence
	 * @param pitch Step size of sequence
	 * @param end Last value of sequence
	 * @return Array of successive values
	 */
	public static double[] increment(double begin, double pitch, double end) {
		double[] array = new double[(int) ((end - begin) / pitch)];
		for (int i = 0; i < array.length; i++) {
			array[i] = begin + i * pitch;
		}
		return array;
	}

        
	// Modify rows & columns methods

	/**
	 * Generate a copy of an array
	 * @param M Input array.
	 * @return A copy of the input array.
	 */
	public static double[] copy(double[] M) {
		double[] array = new double[M.length];
		System.arraycopy(M, 0, array, 0, M.length);
		return array;
	}

	/**
	 * Generate a copy of a matrix
	 * @param M Input matrix.
	 * @return A copy of the input matrix
	 */
	public static double[][] copy(double[][] M) {
		double[][] array = new double[M.length][M[0].length];
		for (int i = 0; i < array.length; i++)
			System.arraycopy(M[i], 0, array[i], 0, M[i].length);
		return array;
	}

	/**
	 * Generate a resized copy of a matrix
	 * @param M Input matrix.
	 * @param m number of rows of new matrix.
	 * @param n number of columns of new matrix.
	 * @return A resized copy of the input matrix
	 */
	public static double[][] resize(double[][] M, int m, int n) {
		double[][] array = new double[m][n];
		for (int i = 0; i < Math.min(M.length, m); i++)
			System.arraycopy(M[i], 0, array[i], 0, Math.min(M[i].length, n));
		return array;
	}

	/**
	 * Carve out a submatrix from the input matrix and return a copy.
	 * Result is the intersection of rows i1 through i2
	 * and columns j1 through j2 inclusive. Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] b = getSubMatrixRangeCopy(a, 2, 3, 1, 3);<br>
	 * input is:<br>
	 *  0  1  2  3  4<br>
	 *  1  7  8  9 10<br>
	 *  2 13 14 15 16<br>
	 *  3 19 20 21 22<br>
	 *  4 25 26 27 28<br>
	 * result is:<br>
	 * 13 14 15<br>
	 * 19 20 21<br>
	 * </code>
	 * @param M Input matrix
	 * @param i1 Index of first row in cut
	 * @param i2 Index of last row in cut
	 * @param j1 Index of first column in cut
	 * @param j2 Index of last column in cut
	 * @return submatrix. Input matrix is left unharmed.
	 */
	public static double[][] getSubMatrixRangeCopy(double[][] M, int i1, int i2, int j1, int j2) {
		double[][] array = new double[i2 - i1 + 1][j2 - j1 + 1];
		for (int i = 0; i < i2 - i1 + 1; i++)
			System.arraycopy(M[i + i1], j1, array[i], 0, j2 - j1 + 1);
		return array;
	}

	/**
	 * Extract a range of columns from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] z = getColumnsRangeCopy(a, 2, 4);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 *  2   3   4<br>
	 *  8   9  10<br>
	 * 14  15  16<br>
	 * 20  21  22<br>
	 * 26  27  28<br>
	 * </code>
	 * @param M Input matrix
	 * @param j1 Index of first column to be extracted.
	 * @param j2 Index of last column to be extracted.
	 * @return An mxn matrix where m=number of rows in M and n=j2-j1+1
	 */
	public static double[][] getColumnsRangeCopy(double[][] M, int j1, int j2) {
		double[][] array = new double[M.length][j2 - j1 + 1];
		for (int i = 0; i < M.length; i++)
			System.arraycopy(M[i], j1, array[i], 0, j2 - j1 + 1);
		return array;
	}

	/**
	 * Extract specific columns from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] z = getColumnsCopy(a, 2, 4);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 *  0   2   4<br>
	 *  1   8  10<br>
	 *  2  14  16<br>
	 *  3  20  22<br>
	 *  4  26  28<br>
	 * </code>
	 * @param M Input matrix
	 * @param J Each is the index of a column. There can be as many indices listed as there are columns in M.
	 * @return An mxn matrix where m=number of rows in M and n=number of indices listed
	 */
	public static double[][] getColumnsCopy(double[][] M, int... J) {
		double[][] array = new double[M.length][J.length];
		for (int i = 0; i < M.length; i++)
			for (int j = 0; j < J.length; j++)
				array[i][j] = M[i][J[j]];
		return array;
	}

	/**
	 * Extract one column from a matrix.
	 *
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] z = getColumnCopy(a, 2);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * Result is:<br>
	 * 2   8  14  20  26<br>
	 * </code>
	 * @param M Input matrix
	 * @param j Index of desired column
	 * @return Array of values from extracted column.
	 */
	public static double[] getColumnCopy(double[][] M, int j) {
		double[] array = new double[M.length];
		for (int i = 0; i < M.length; i++)
			array[i] = M[i][j];
		return array;
	}

	/**
	 * Extract one <i>column</i> from a three dimensional array. The result is an array of the
	 * values in the first dimension.
	 * @param M Input 3D array
	 * @param j The index of the second dimension.
	 * @param k The index of the third dimension
	 * @return An array
	 */
	public static double[] getColumnCopy(double[][][] M, int j, int k) {
		double[] array = new double[M.length];
		for (int i = 0; i < M.length; i++)
			array[i] = M[i][j][k];
		return array;
	}

	/**
	 * Extract specific rows from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] z = getRowsCopy(a, 2, 4);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 *  2  13  14  15  16<br>
	 *  4  25  26  27  28<br>
	 * </code>
	 * @param M Input matrix
	 * @param I Each is the index of a column.
	 * @return An mxn matrix where m=number of indices listed and n=number of columns in m
	 */
	public static double[][] getRowsCopy(double[][] M, int... I) {
		double[][] array = new double[I.length][M[0].length];
		for (int i = 0; i < I.length; i++)
			System.arraycopy(M[I[i]], 0, array[i], 0, M[I[i]].length);
		return array;
	}

	/**
	 * Extract a row from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[] z = getRowCopy(a, 2);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 *  2  13  14  15  16<br>
	 * </code>
	 * @param M Input matrix
	 * @param i index of row to copy
	 * @return An array of n values where n = number of columns in M
	 */
	public static double[] getRowCopy(double[][] M, int i) {
		double[] array = new double[M[0].length];
		System.arraycopy(M[i], 0, array, 0, M[i].length);
		return array;
	}

	/**
	 * Extract a range of rows from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[][] z = getRowsRangeCopy(a, 2, 4);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * </code>
	 * @param M Input matrix
	 * @param i1 Index of first row to be extracted.
	 * @param i2 Index of last row to be extracted.
	 * @return An mxn matrix where m=j2-j1+1 and n=number of columns in M
	 */
	public static double[][] getRowsRangeCopy(double[][] M, int i1, int i2) {
		double[][] array = new double[i2 - i1 + 1][M[0].length];
		for (int i = 0; i < i2 - i1 + 1; i++)
			System.arraycopy(M[i + i1], 0, array[i], 0, M[i + i1].length);
		return array;
	}

	/**
	 * Extract a section of an array.
	 * Example:<br>
	 * <code>
	 * double[] a = {00,11,22,33,44,55,66,77,88,99};<br>
	 * double[] z = getRangeCopy(a, 2, 5);<br>
	 * result is:<br>
	 * 22  33  44  55<br>
	 * </code>
	 * @param M Input array
	 * @param j1 Index of first term to get
	 * @param j2 Index of last term to get
	 * @return An array with j2-j1+1 elements
	 */
	public static double[] getRangeCopy(double[] M, int j1, int j2) {
		double[] array = new double[j2 - j1 + 1];
		System.arraycopy(M, j1, array, 0, j2 - j1 + 1);
		return array;
	}

	/**
	 * Extract specific elements from an array.
	 * Example:
	 * <br>
	 * <code>
	 * double[] a = {00,11,22,33,44,55,66,77,88,99};<br>
	 * double[] z = getCopy(a, 2, 5);<br>
	 * result is:<br>
	 * 22  55<br>
	 * </code>
	 * @param M The input array.
	 * @param I the indices of the elements to extract
	 * @return The output array of n elements where n=number of indices listed.
	 */
	public static double[] getCopy(double[] M, int... I) {
		double[] array = new double[I.length];
		for (int i = 0; i < I.length; i++)
			array[i] = M[I[i]];
		return array;
	}

	/**
	 * Get the number of columns in a specified row of a matrix. Used for oddly sized matrices.
	 * @param M Input matrix
	 * @param i Index of row whose column length will be returned
	 * @return The number of columns in row <i>i</i>
	 */
	public static int getColumnDimension(double[][] M, int i) {
		return M[i].length;
	}

	/**
	 * Extract diagonal from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,25,26,27,28}};<br>
	 * double[] z = getDiagonal(a, 1);<br>
	 * input is:<br>
	 * 0   1   2   3   4<br>
	 * 1   7   8   9  10<br>
	 * 2  13  14  15  16<br>
	 * 3  19  20  21  22<br>
	 * 4  25  26  27  28<br>
	 * result is:<br>
	 *  1 8 15 22<br>
	 * </code>
	 * @param M Input matrix
	 * @param I index of diagonal to copy
	 * @return An array
	 */
	public static double[] getDiagonal(double[][] M, int I) {
		int nr = M.length, nc = M.length;
		int nd = 0;
		if (nc < nr) {
			if (I >= 0) {
				nd = nc - I;
			} else if (I < (nc - nr)) {
				nd = nr + I;
			} else {
				nd = nc;
			}
		} else {
			if (I <= 0) {
				nd = nr + I;
			} else if (I > (nc - nr)) {
				nd = nc - I;
			} else {
				nd = nr;
			}
		}

		double[] d = new double[nd];
		for (int i = 0; i < d.length; i++)
			d[i] = M[i + I][i + I];
		return d;
	}

	/**
	 * Combine a set of arrays into a matrix. Each array becomes a row. Rows may be of different lengths.
	 * Example:<br>
	 * <code>
	 * double[] a = {00,11,22,33,44}, b = {55,66,77,88};
	 * double[][] z = mergeRows(a, b);
	 * result is:<br>
	 *   0  11  22  33  44
	 *  55  66  77  88
	 * </code>
	 * @param x Input arrays
	 * @return A matrix. Will be non-rectangular if arrays are not all the same length.
	 */
	public static double[][] mergeRows(double[]... x) {
		double[][] array = new double[x.length][];
		for (int i = 0; i < array.length; i++) {
			array[i] = new double[x[i].length];
			System.arraycopy(x[i], 0, array[i], 0, array[i].length);
		}
		return array;
	}

	/**
	 * Combine a set of arrays into a matrix. Each array becomes a column. Arrays must all be of same length.
	 * Example:<br>
	 * <code>
	 * double[] a = {00,11,22,33,44}, b = {55,66,77,88,99};
	 * double[][] z = mergeColumns(a, b);
	 * result is:<br>
	 *  0  55
	 * 11  66
	 * 22  77
	 * 33  88
	 * 44  99
	 * </code>
	 * @param x Input arrays
	 * @return An mxn matrix where m=size of any input array and n is the number of arrays.
	 */
	public static double[][] mergeColumns(double[]... x) {
		double[][] array = new double[x[0].length][x.length];
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++)
				array[i][j] = x[j][i];
		return array;
	}

	/**
	 * Converts an n element array into an n x 1 matrix. Handy for matrix math.
	 * @param x n element array
	 * @return n x 1 matrix
	 */
	public static double[][] columnVector(double[] x) {
		return mergeColumns(x);
	}

	/**
	 * Converts an n element array into an 1 x n matrix. Handy for matrix math.
	 * @param x n element array
	 * @return 1 x n matrix
	 */
	public static double[][] rowVector(double[] x) {
		return mergeRows(x);
	}

	/**
	 * Concatenates arrays.
	 * Example:<br>
	 * <code>
	 * double[] a = {00,11,22,33,44}, b = {55,66,77,88,99};<br>
	 * double[] z = merge(a, b, a);<br>
	 * result is:<br>
	 *   0  11  22  33  44  55  66  77  88  99   0  11  22  33  44
	 * </code>
	 * @param x Input arrays
	 * @return Output array.
	 */
	public static double[] merge(double[]... x) {
		int[] xlength_array = new int[x.length];
		xlength_array[0] = x[0].length;
		for (int i = 1; i < x.length; i++)
			xlength_array[i] = x[i].length + xlength_array[i - 1];
		double[] array = new double[xlength_array[x.length - 1]];
		System.arraycopy(x[0], 0, array, 0, x[0].length);
		for (int i = 1; i < x.length; i++)
			System.arraycopy(x[i], 0, array, xlength_array[i - 1], x[i].length);
		return array;
	}

	// I didn't favor this insertColumns method because it was so different from insertRows()
	// and it just seemed more likely that someone would want to insert arrays rather than
	// whole matrices. See the new version below.
	/*    public static double[][] insertColumns(double[][] x, int J, double[]... y) {
	 double[][] array = new double[x.length][x[0].length + y[0].length];
	 System.out.println("y[0].length="+y[0].length);
	 for (int i = 0; i < array.length; i++) {
	 System.out.println(tostring("%3.0f",array)+"\n");
	 System.arraycopy(x[i], 0, array[i], 0, J);
	 System.arraycopy(y[i], 0, array[i], J, y[i].length);
	 System.arraycopy(x[i], J, array[i], J + y[i].length, x[i].length - J);
	 }
	 return array;
	 }
	 */
	/**
	 * Insert any number of arrays between 2 columns of a matrix. Size of the arrays must
	 * equal number of rows in the matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,23,24,25,26}};<br>
	 * double[] b = {00,11,22,33,44}, c = {55,66,77,88,99};<br>
	 * double[][] z = insertColumns(a, 2, b, c);<br>
	 * input matrix is:<br>
	 *  0   1   2   3   4<br>
	 *  1   7   8   9  10<br>
	 *  2  13  14  15  16<br>
	 *  3  19  20  21  22<br>
	 *  4  23  24  25  26<br>
	 * result is:<br>
	 *  0   1   0  55   2   3   4<br>
	 *  1   7  11  66   8   9  10<br>
	 *  2  13  22  77  14  15  16<br>
	 *  3  19  33  88  20  21  22<br>
	 *  4  23  44  99  24  25  26<br>
	 * </code>
	 * @param x Input m x n matrix.
	 * @param J Index of column before which the new columns will be inserted.
	 * @param y The arrays to be inserted
	 * @return New matrix with added columns.
	 */
	public static double[][] insertColumns(double[][] x, int J, double[]... y) {
		return transpose(insertRows(transpose(x), J, y));
	}

	/*public static double[][] insertColumn(double[][] x, double[] y, int J) {
	 double[][] array = new double[x.length][x[0].length + 1];
	 for (int i = 0; i < array.length; i++) {
	 System.arraycopy(x[i], 0, array[i], 0, J);
	 array[i][J] = y[i];
	 System.arraycopy(x[i], J, array[i], J + 1, x[i].length - J);
	 }
	 return array;
	 }*/

	/**
	 * Insert any number of arrays between 2 rows of a matrix. Size of the arrays must
	 * equal number of columns in the matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22}};<br>
	 * double[] b = {0,11,22,33,44}, c = {55,66,77,88,99};<br>
	 * double[][] z = insertRows(a, 1, b, c);<br>
	 * result is:<br>
	 *   0   1   2   3   4<br>
	 *   0  11  22  33  44<br>
	 *  55  66  77  88  99<br>
	 *   1   7   8   9  10<br>
	 *   2  13  14  15  16<br>
	 *   3  19  20  21  22<br>
	 * </code>
	 * @param x Input m x n matrix.
	 * @param I Index of row before which the new rows will be inserted.
	 * @param y The arrays to be inserted
	 * @return New matrix with added rows.
	 */
	public static double[][] insertRows(double[][] x, int I, double[]... y) {
		double[][] array = new double[x.length + y.length][x[0].length];
		for (int i = 0; i < I; i++)
			System.arraycopy(x[i], 0, array[i], 0, x[i].length);
		for (int i = 0; i < y.length; i++)
			System.arraycopy(y[i], 0, array[i + I], 0, y[i].length);
		for (int i = 0; i < x.length - I; i++)
			System.arraycopy(x[i + I], 0, array[i + I + y.length], 0, x[i].length);
		return array;
	}

	/*public static double[][] insertRow(double[][] x, double[] y, int I) {
	 double[][] array = new double[x.length + 1][x[0].length];
	 for (int i = 0; i < I; i++)
	 System.arraycopy(x[i], 0, array[i], 0, x[i].length);
	 System.arraycopy(y, 0, array[I], 0, y.length);
	 for (int i = 0; i < x.length - I; i++)
	 System.arraycopy(x[i + I], 0, array[i + I + 1], 0, x[i].length);
	 return array;
	 }*/

	/**
	 * Insert any number of values, or a single array, between 2 elements of an array.
	 * Example:<br>
	 * <code>
	 * double[] b = {00,11,22,33,44}, c = {55,66,77,88,99};<br>
	 * double[] z = insert(b, 2, 333, 444);<br>
	 * result is:<br>
	 * 0  11 333 444  22  33  44
	 * double[] z = insert(b, 2, c);<br>
	 * result is:<br>
	 * 0  11  55  66  77  88  99  22  33  44<br>
	 * </code>
	 * @param x Input array.
	 * @param I Index of element before which the values will be inserted.
	 * @param y Values to be inserted. Can also be a single array.
	 * @return Expanded array.
	 */
	public static double[] insert(double[] x, int I, double... y) {
		double[] array = new double[x.length + y.length];
		System.arraycopy(x, 0, array, 0, I);
		System.arraycopy(y, 0, array, I, y.length);
		System.arraycopy(x, I, array, I + y.length, x.length - I);
		return array;
	}

	/**
	 * Deletes a range of columns from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,23,24,25,26}};<br>
	 * double[][] z = deleteColumnsRange(a, 1, 3);<br>
	 * result is:<br>
	 *  0   4<br>
	 *  1  10<br>
	 *  2  16<br>
	 *  3  22<br>
	 *  4  26<br>
	 * </code>
	 * @param x The input matrix
	 * @param J1 The Index of the first column to delete.
	 * @param J2 The index of the last column to delete.
	 * @return The reduced matrix.
	 */
	public static double[][] deleteColumnsRange(double[][] x, int J1, int J2) {
		double[][] array = new double[x.length][x[0].length - (J2 - J1 + 1)];
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(x[i], 0, array[i], 0, J1);
			//if (J2<x.length-1)
			System.arraycopy(x[i], J2 + 1, array[i], J1, x[i].length - (J2 + 1));
		}
		return array;
	}

	/**
	 * Deletes a list of columns from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,23,24,25,26}};<br>
	 * double[][] z = deleteColumns(a, 1, 3);<br>
	 * result is:<br>
	 *  0  2   4<br>
	 *  1  8  10<br>
	 *  2  14 16<br>
	 *  3  20 22<br>
	 *  4  24 26<br>
	 * </code>
	 * @param x The input matrix
	 * @param J The indices of the columns to be deleted. There must be no more indices listed
	 * than there are columns in the input matrix.
	 * @return The reduced matrix.
	 */
	public static double[][] deleteColumns(double[][] x, int... J) {
		/*double[][] array = new double[x.length][x[0].length - J.length];
		 for (int i = 0; i < array.length; i++) {
		 System.arraycopy(x[i], 0, array[i], 0, J[0]);
		 for (int j = 0; j < J.length - 1; j++)
		 System.arraycopy(x[i], J[j] + 1, array[i], J[j] - j, J[j + 1] - J[j] - 1);
		 System.arraycopy(x[i], J[J.length - 1] + 1, array[i], J[J.length - 1] - J.length + 1, x[i].length - J[J.length - 1] - 1);
		 }
		 return array;*/
		// TODO improve efficiency here
		return transpose(deleteRows(transpose(x), J));
	}

	/**
	 * Deletes a range of rows from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,23,24,25,26}};<br>
	 * double[][] z = deleteRowsRange(a, 1, 3);<br>
	 * result is:<br>
	 *   0   1   2   3   4<br>
	 *   4  23  24  25  26<br>
	 * </code>
	 * @param x The input matrix
	 * @param I1 The Index of the first row to delete.
	 * @param I2 The index of the last row to delete.
	 * @return The reduced matrix.
	 */
	public static double[][] deleteRowsRange(double[][] x, int I1, int I2) {
		double[][] array = new double[x.length - (I2 - I1 + 1)][x[0].length];
		for (int i = 0; i < I1; i++)
			System.arraycopy(x[i], 0, array[i], 0, x[i].length);
		for (int i = 0; i < x.length - I2 - 1; i++)
			System.arraycopy(x[i + I2 + 1], 0, array[i + I1], 0, x[i].length);
		return array;
	}

	/**
	 * Deletes a list of rows from a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,1,2,3,4},{1,7,8,9,10},{2,13,14,15,16},{3,19,20,21,22},{4,23,24,25,26}};<br>
	 * double[][] z = deleteRows(a, 1, 3);<br>
	 * result is:<br>
	 *   0   1   2   3   4<br>
	 *   2  13  14  15  16<br>
	 *   4  23  24  25  26<br>
	 * </code>
	 * @param x The input matrix
	 * @param I The indices of the rows to delete.
	 * @return The reduced matrix.
	 */
	public static double[][] deleteRows(double[][] x, int... I) {
		double[][] array = new double[x.length - I.length][x[0].length];
		int i2 = 0;
		for (int i = 0; i < x.length; i++) {
			if (!into(i, I)) {
				System.arraycopy(x[i], 0, array[i2], 0, x[i].length);
				i2++;
			}
		}
		/*for (int i = 0; i < I[0]; i++)
		 System.arraycopy(x[i], 0, array[i], 0, x[i].length);
		 for (int j = 0; j < I.length - 1; j++)
		 for (int i = I[j] + 1; i < I[j + 1]; i++)
		 System.arraycopy(x[i], 0, array[i - j], 0, x[i].length);
		 for (int i = I[I.length - 1] + 1; i < x.length; i++)
		 System.arraycopy(x[i], 0, array[i - I.length], 0, x[i].length);*/
		return array;
	}

	/**
	 * Delete a range of elements from an array.
	 * Example:<br>
	 * <code>
	 * double[] b = {00,11,22,33,44};<br>
	 * double[] z = deleteRange(b, 1, 3);<br>
	 * Result is:<br>
	 * 0  44<br>
	 * </code>
	 * @param x Input array
	 * @param J1 Index of first element to delete. Must be >= 0 and <= x.length
	 * @param J2 Index of last element to delete. Must be >= J1 and <= x.length
	 * @return Reduced array.
	 */
	public static double[] deleteRange(double[] x, int J1, int J2) {
		double[] array = new double[x.length - (J2 - J1 + 1)];
		System.arraycopy(x, 0, array, 0, J1);
		//if (J2<x.length-1)
		System.arraycopy(x, J2 + 1, array, J1, x.length - (J2 + 1));
		return array;
	}

	/**
	 * Delete a list of elements from an array.
	 * Example:<br>
	 * <code>
	 * double[] b = {00,11,22,33,44};<br>
	 * double[] z = deleteRange(b, 1, 3);<br>
	 * Result is:<br>
	 * 0  22  44<br>
	 * </code>
	 * @param x Input array
	 * @param J Index of elements to delete. Each must be >= 0 and <= x.length
	 * @return Reduced array.
	 */
	public static double[] delete(double[] x, int... J) {
		double[] array = new double[x.length - J.length];
		int j2 = 0;
		for (int j = 0; j < x.length; j++) {
			if (!into(j, J)) {
				array[j2] = x[j];
				j2++;
			}
		}
		/*System.arraycopy(x, 0, array, 0, J[0]);
		 for (int j = 0; j < J.length - 1; j++)
		 System.arraycopy(x, J[j] + 1, array, J[j] - j, J[j + 1] - J[j] - 1);
		 System.arraycopy(x, J[J.length - 1] + 1, array, J[J.length - 1] - J.length + 1, x.length - J[J.length - 1] - 1);*/
		return array;
	}

	/**
	 * Determines if a value is within an array
	 * @param i Value to be searched for.
	 * @param I array to be searched
	 * @return true if found. fales if not.
	 */
	private static boolean into(int i, int[] I) {
		boolean in = false;
		for (int j = 0; j < I.length; j++) {
			in = in || (i == I[j]);
		}
		return in;
	}

	/**
	 * Generate a two column matrix. Second column is just the values in <em>Y</em>
	 * The first column is a uniform sequence of values from Xmin to Xmax. Step size
	 * is automatic. Useful for generating values for an x axis when y is already
	 * defined and bundling the pairs into a matrix.
	 * Example:<br>
	 * <code>
	 * double[] y = {0.0, 1.0, 4.0, 9.0, 16.0};<br>
	 * double[][] xy = buildXY(0.0, 4.0, y);<br>
	 *
	 * result:<br>
	 * 0.0   0.0<br>
	 * 1.0   1.0<br>
	 * 2.0   4.0<br>
	 * 3.0   9.0<br>
	 * 4.0   16.0<br>
	 * </code>
	 *
	 * @param Xmin The first value in the first column
	 * @param Xmax The last value in the first column
	 * @param Y An array that will fill the second column.
	 * @return nx2 array of values where n = length of y.
	 * @throws IllegalArgumentException
	 */
	public static double[][] buildXY(double Xmin, double Xmax, double[] Y) {
		if (Xmax < Xmin)
			throw new IllegalArgumentException("First argument must be less than second");
		int n = Y.length;
		double[][] XY = new double[n][2];
		for (int i = 0; i < n; i++) {
			XY[i][0] = Xmin + (Xmax - Xmin) * (double) i / (double) (n - 1);
			XY[i][1] = Y[i];
		}
		return XY;
	}

	/**
	 * Join two arrays into a matrix. Each array becomes a column in the matrix.
	 * Example:<br>
	 * <code>
	 * double[] x = {0,1,2};<br>
	 * double[] y = {1,7,8};<br>
	 * double[][] z = buildXY(x, y);<br>
	 * result is:<br>
	 *  0   1<br>
	 *  1   7<br>
	 *  2   8<br>
	 * </code>
	 * @see #mergeColumns(double[][])
	 */
	public static double[][] buildXY(double[] X, double[] Y) {
		return mergeColumns(X, Y);
	}

	// min/max methods

	/**
	 * Finds the minimum value in each column of a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,  1,   2,   3,   4},<br>
	 *                 {1, .7,   8,   9,  10},<br>
	 *                 {2, 13, 1.4, 1.5,  16},<br>
	 *                 {3, 19,  20,  21,  22},<br>
	 *                 {4, 23,  24,  25, 2.6}};<br>
	 * double[] z = min(a);<br>
	 * Result is:<br>
	 * 0.0  0.7  1.4  1.5  2.6<br>
	 * </code>
	 * @param M The input matrix
	 * @return Array of minimums from each column of M.
	 */
	public static double[] min(double[][] M) {
		double[] min = new double[M[0].length];
		for (int j = 0; j < min.length; j++) {
			min[j] = M[0][j];
			for (int i = 1; i < M.length; i++)
				min[j] = Math.min(min[j], M[i][j]);
		}
		return min;
	}

	/**
	 * Finds minimum value in either a list of numbers or a single array.
	 * @param M Can be a list of values e.g. min(1,22,333,.4) or an array.
	 * @return The minimum value.
	 */
	public static double min(double... M) {
		double min = M[0];
		for (int i = 1; i < M.length; i++)
			min = Math.min(min, M[i]);
		return min;
	}

	/**
	 * Finds the maximum value in each column of a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,  1,  2,   3,  4},<br>
	 *                 {1,  7, 88,   9, 10},<br>
	 *                 {2, 13, 14,  15, 16},<br>
	 *                 {3, 19, 20, 201, 22},<br>
	 *                 {4, 23, 24,  25, 26}};<br>
	 * double[] z = max(a);<br>
	 * Result is:<br>
	 * 4  23  88  201  26<br>
	 * </code>
	 * @param M The input matrix
	 * @return Array of maximums from each column of M.
	 */
	public static double[] max(double[][] M) {
		double[] max = new double[M[0].length];
		for (int j = 0; j < max.length; j++) {
			max[j] = M[0][j];
			for (int i = 1; i < M.length; i++)
				max[j] = Math.max(max[j], M[i][j]);
		}
		return max;
	}

	/**
	 * Finds maximum value in either a list of numbers or a single array.
	 * @param M Can be a list of values e.g. min(1,22,333,.4) or an array.
	 * @return The maximum value.
	 */
	public static double max(double... M) {
		double max = M[0];
		for (int i = 1; i < M.length; i++)
			max = Math.max(max, M[i]);
		return max;
	}

	/**
	 * Finds the indices of the minimum values in each column of a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,  1,   2,   3,   4},<br>
	 *                 {1, .7,   8,   9,  10},<br>
	 *                 {2, 13, 1.4, 1.5,  16},<br>
	 *                 {3, 19,  20,  21,  22},<br>
	 *                 {4, 23,  24,  25, 2.6}};<br>
	 * int[] z = minIndex(a);<br>
	 * Result is:<br>
	 * 0  1  2  2  4<br>
	 * </code>
	 * @param M Input matrix.
	 * @return Array of indices of the minimums from each column of M.
	 */
	public static int[] minIndex(double[][] M) {
		int[] minI = new int[M[0].length];
		for (int j = 0; j < minI.length; j++) {
			minI[j] = 0;
			for (int i = 1; i < M.length; i++)
				if (M[i][j] < M[minI[j]][j])
					minI[j] = i;

		}
		return minI;
	}

	/**
	 * Finds the index of the minumum value in a list of values or an array.
	 * @param M Can be a list of values e.g. minIndex(11,22,44,2) or an array
	 * @return index of minimum value in M
	 */
	public static int minIndex(double... M) {
		int minI = 0;
		for (int i = 1; i < M.length; i++)
			if (M[i] < M[minI])
				minI = i;
		return minI;
	}

	/**
	 * Finds the indices of the minimum values in each column of a matrix.
	 * Example:<br>
	 *  <code>
	 * double[][] a = {{0,  1,  2,   3,  4},<br>
	 *                 {1,  7, 88,   9, 10},<br>
	 *                 {2, 13, 14,  15, 16},<br>
	 *                 {3, 19, 20, 201, 22},<br>
	 *                 {4, 23, 24,  25, 26}};<br>
	 * int[] z = maxIndex(a);<br>
	 * Result is:<br>
	 * 4  4  1  3  4<br>
	 * </code>
	 * @param M Input matrix.
	 * @return Array of indices of the maximums from each column of M.
	 */
	public static int[] maxIndex(double[][] M) {
		int[] maxI = new int[M[0].length];
		for (int j = 0; j < maxI.length; j++) {
			maxI[j] = 0;
			for (int i = 1; i < M.length; i++)
				if (M[i][j] > M[maxI[j]][j])
					maxI[j] = i;
		}
		return maxI;
	}

	/**
	 * Finds the index of the maximum value in a list of values or an array.
	 * @param M Can be a list of values e.g. maxIndex(11,22,44,2) or an array
	 * @return index of maximum value in M
	 */
	public static int maxIndex(double... M) {
		int maxI = 0;
		for (int i = 1; i < M.length; i++)
			if (M[i] > M[maxI])
				maxI = i;
		return maxI;
	}

	// cumulative methods

	/**
	 * Calculate the sum of the values in an array
	 * @param v input array
	 * @return sum of values in v
	 */
	public static double sum(double[] v) {
		int m = v.length;
		double s = 0;
		for (int i = 0; i < m; i++)
			s += v[i];
		return s;
	}

	/**
	 * Calculates the sum of each column in a matrix.
	 * @param v
	 * @return Array. value of i'th element is sum of values in column(i)
	 */
	public static double[] sum(double[][] v) {
		int m = v.length;
		int n = v[0].length;
		double[] X = new double[n];
		double s;
		for (int j = 0; j < n; j++) {
			s = 0;
			for (int i = 0; i < m; i++)
				s += v[i][j];
			X[j] = s;
		}
		return X;
	}

	/**
	 * Calculates the cumulative sum of an array. Think of it as an integral.
	 * Example:<br>
	 * <code>
	 * double[] b = {0,1,2,3,4};<br>
	 * double[] z = cumSum(b);<br>
	 * result is:<br>
	 *  0    1    3    6   10<br>
	 * </code>
	 * @param v Input array.
	 * @return Output array of same length as v.
	 */
	public static double[] cumSum(double[] v) {
		int m = v.length;
		double[] X = new double[m];
		double s = 0;
		for (int i = 0; i < m; i++) {
			s += v[i];
			X[i] = s;
		}
		return X;
	}

	/**
	 * Calculates the cumulative sum of each column in a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,  1,  2,  3,  4},<br>
	 *                 {1,  7,  8,  9, 10},<br>
	 *                 {2, 13, 14, 15, 16},<br>
	 *                 {3, 19, 20, 21, 22},<br>
	 *                 {4, 23, 24, 25, 26}};<br>
	 * double[][] z = cumSum(a);<br>
	 * result is:<br>
	 *  0    1    2    3    4<br>
	 *  1    8   10   12   14<br>
	 *  3   21   24   27   30<br>
	 *  6   40   44   48   52<br>
	 * 10   63   68   73   78<br>
	 * </code>
	 * @param v
	 * @return Output matrix. Each column is the cumulative sum of each column in v.
	 */
	public static double[][] cumSum(double[][] v) {
		int m = v.length;
		int n = v[0].length;
		double[][] X = new double[m][n];
		double s;
		for (int j = 0; j < n; j++) {
			s = 0;
			for (int i = 0; i < m; i++) {
				s += v[i][j];
				X[i][j] = s;
			}
		}
		return X;
	}

	/**
	 * Calculates the product of the values in an array.
	 * @param v Input array.
	 * @return The product of the values in v.
	 */
	public static double product(double[] v) {
		int m = v.length;
		double p = 1;
		for (int i = 0; i < m; i++)
			p *= v[i];
		return p;
	}

	/**
	 * Calculates the product of the values in each column of a matrix.
	 * @param v Input matrix.
	 * @return An array of n values where n=number of columns in v. The i'th element is product
	 * of the values in the i'th column of v.
	 */
	public static double[] product(double[][] v) {
		int m = v.length;
		int n = v[0].length;
		double[] X = new double[n];
		for (int j = 0; j < n; j++) {
			double p = 1;
			for (int i = 0; i < m; i++)
				p *= v[i][j];
			X[j] = p;
		}
		return X;
	}

	/**
	 * Calculates cumulative product of the values in an array.
	 * Example:<br>
	 * <code>
	 * double[] b = {1,2,3,4};<br>
	 * double[] z = cumProduct(b);<br>
	 * Result is:<br>
	 * 1    2    6   24<br>
	 * </code>
	 * @param v Input array
	 * @return Out put array of same size as v.
	 */
	public static double[] cumProduct(double[] v) {
		int m = v.length;
		double[] X = new double[m];
		double s = 1;
		for (int i = 0; i < m; i++) {
			s *= v[i];
			X[i] = s;
		}
		return X;
	}

	/**
	 * Calculates the cumulative product of each column in a matrix.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{0,  1,  2,  3,  4},<br>
	 *                 {1,  7,  8,  9, 10},<br>
	 *                 {2, 13, 14, 15, 16},<br>
	 *                 {3, 19, 20, 21, 22},<br>
	 *                 {4, 23, 24, 25, 26}};<br>
	 * result is:<br>
	 * 0        1        2        3        4<br>
	 * 0        7       16       27       40<br>
	 * 0       91      224      405      640<br>
	 * 0     1729     4480     8505    14080<br>
	 * 0    39767   107520   212625   366080<br>
	 * </code>
	 * @param v
	 * @return Output matrix. Each column is the cumulative product of each column in v.
	 */
	public static double[][] cumProduct(double[][] v) {
		int m = v.length;
		int n = v[0].length;
		double[][] X = new double[m][n];
		double s;
		for (int j = 0; j < n; j++) {
			s = 1;
			for (int i = 0; i < m; i++) {
				s *= v[i][j];
				X[i][j] = s;
			}
		}
		return X;
	}

	// print methods

	/**
	 * Generates a string that holds a nicely organized space-seperated version of a matrix or array.
	 * An extra space is automatically included.
	 * Note the lower case S. It's tostring() not toString().
	 * Example:<br>
	 * <code>
	 * double[][] a = {{1.234, 4.5, 6.78},{1.2, 3.45, 6.789}};<br>
	 * System.out.println(tostring(a));<br>
	 * result is:<br>
	 * 1.234 4.5 6.78<br>
	 * 1.2 3.45 6.789
	 * </code>
	 * @param v Matrix or array
	 * @return A string of a nicely organized version of the matrix or array.
	 */
	public static String toString(double[]... v) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v[i].length - 1; j++)
				str.append(GlobalValues.fmtString.format(v[i][j]) + " ");
			str.append(GlobalValues.fmtString.format(v[i][v[i].length - 1]));
			if (i < v.length - 1)
				str.append("\n");
		}
		return str.toString();
	}

	/**
	 * Generates a string that holds a nicely organized version of a matrix or array.
	 * Uses format specifier, e.g. "%5.3f", "%11.1E", ...
	 * An extra space is automatically included.
	 * Note the lower case S. It's tostring() not toString().
	 * Example:<br>
	 * <code>
	 * double[][] a = random(2, 3);<br>
	 * System.out.println(tostring("%7.3f", a));<br>
	 * result is:<br>
	 *   0.654   0.115   0.422<br>
	 *   0.560   0.839   0.280<br>
	 * </code>
	 * @param format A standard format specifier for one value
	 * @param v Matrix or array
	 * @return A string of a nicely organized version of the matrix or array.
	 */
	public static String toString(String format, double[]... v) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v[i].length - 1; j++)
				str.append(String.format(format + " ", v[i][j]));
			str.append(String.format(format, v[i][v[i].length - 1]));
			if (i < v.length - 1)
				str.append("\n");
		}
		return str.toString();
	}

	// check methods

	/**
	 * Throws an error exception if an argument is invalid. Handy to make sure the right number
	 * of values is passed around.
	 * @param msg Message to be printed if the error occurs.
	 * @throws IllegalArgumentException
	 */
	public static void throwError(String msg) {
		throw new IllegalArgumentException(msg);
	}

	/**
	 * Checks to make sure each row of a matrix is of a specified dimension. Handy for making sure
	 * matrices are rectangular.
	 * @param M Input matrix
	 * @param n Number of elements that should be in each row of M
	 * @throws IllegalArgumentException
	 */
	public static void checkColumnDimension(double[][] M, int n) {
		for (int i = 0; i < M.length; i++)
			if (M[i].length != n)
				throwError("row " + i + " have " + M[i].length + " columns instead of " + n + " columns expected.");
	}

	/**
	 * Same as checkColumnDimension() but just returns a boolean value rather than throwing an exception.
	 * @param M Input matrix
	 * @param n Number of elements that should be in each row of M
	 * @return true if each row of M has n values. false otherwise.
	 * @see #checkColumnDimension(double[][], int)
	 */
	public static boolean isColumnDimension(double[][] M, int n) {
		for (int i = 0; i < M.length; i++)
			if (M[i].length != n)
				return false;
		return true;
	}

	/**
	 * Checks to make sure each column of a matrix is of a specified dimension. Handy for making sure
	 * matrices are rectangular.
	 * @param M Input matrix
	 * @param m Number of elements that should be in each column of M.
	 * @throws IllegalArgumentException
	 */
	public static void checkRowDimension(double[][] M, int m) {
		if (M.length != m)
			throwError("columns have " + M.length + " rows instead of " + m + " rows expected.");
	}

	/**
	 * Same as checkRowDimension() but just returns a boolean value rather than throwing an exception.
	 * @param M Input matrix
	 * @param m Number of elements that should be in each column of M
	 * @return true if each column of M has n values. false otherwise.
	 * @see #checkRowDimension(double[][], int)
	 */
	public static boolean isRowDimension(double[][] M, int m) {
		if (M.length != m)
			return false;
		return true;
	}

	/**
	 * Checks to make sure an array is of a specified length.
	 * @param M Input array.
	 * @param n Required number of elements in M
	 * @throws IllegalArgumentException
	 */
	public final  static void checkLength(double[] M, int n) {
	  if (M.length != n)
	     throwError("row have " + M.length + " elements instead of " + n + " elements expected.");
	}

	/**
	 * Same as checkLength but returns a boolean value rather than throwing an exception.
	 * @param M Input array.
	 * @param n Required number of elements in M.
	 * @return true if M.length==n. false otherwise.
	 */
	public final static boolean isLength(double[] M, int n) {
		if (M.length != n)
			return false;
		return true;
	}

	//  function methods
	/**
	 * Apply a scalar function to every element of an array.
	 * Must import scalaSci.math.array.util.*; to get Function interface.
	 * The function named f in Function must be overridden. Be careful not
	 * to define any other functions named f in your code.
	 * Example:<br>
	 * <code>
	 * double[] b = {{1,2,3,4},{5,6,7,8}};<br>
	 * Function inverse = new Function() { public double f(double x) { return 1/x; }};<br>
	 * double[] z = f(b, inverse);<br>
	 * Result is: <br>
	 *  1.00  0.50 0.333 0.25<br>
	 *  0.20 0.167 0.143 0.125<br>
	 * </code>
	 * @param M The input array
	 * @param f Function object
	 * @return array of same size as M.
	 */
	public static double[][] f(double[][] M, Function f) {
		double[][] fM = new double[M.length][];
		for (int i = 0; i < fM.length; i++) {
			fM[i] = new double[M[i].length];
			for (int j = 0; j < fM[i].length; j++)
				fM[i][j] = f.f(M[i][j]);
		}
		return fM;
	}

	/**
	 * Apply a scalar function to every element of an array.
	 * Must import scalaSci.math.array.util.*; to get Function interface.
	 * Example:<br>
	 * <code>
	 * double[] b = {1,2,3,4};<br>
	 * Function inverse = new Function() { public double f(double x) { return 1/x; }};<br>
	 * double[] z = f(b, inverse);<br>
	 * Result is: <br>
	 *  1.00 0.50 0.33 0.25
	 * </code>
	 * @param M The input array
	 * @param func Function object whose sole method f(double) has already been overridden.
	 * @return array of same size as M.
	 */
	public static double[] f(double[] M, Function func) {
		double[] fM = new double[M.length];
		for (int i = 0; i < fM.length; i++)
			fM[i] = func.f(M[i]);
		return fM;
	}

	/**
	 * Provides a sequence of values to which some function has been applied.
	 * Sort of a mutant version of increment().
	 * Example:<br>
	 * <code>
	 * IndexFunction square = new IndexFunction() <br>
	 *            { public double fi(int x) { return x*x; }};<br>
	 * double[] z = findex(5, square);<br>
	 * Result is:<br>
	 * 0 1 4 9 16<br>
	 * </code>
	 * @param m Number of terms in the output array. The values 0..m-1 will be fed to the function
	 * @param f An IndexFunction object whose method fi(int) has already been overridden.
	 * @return An array of m elements. fi(0), fi(1), ... fi(m-1)
	 */
	public static double[] findex(int m, IndexFunction f) {
		double[] fm = new double[m];
		for (int i = 0; i < fm.length; i++)
			fm[i] = f.fi(i);
		return fm;
	}

	// sort methods

	/**
	 * Sorts an array in ascending order.
	 * @param values Input array
	 * @return Sorted version of input array.
	 */
	public static double[] sort(double[] values) {
		double[] sorted_values = new double[values.length];
		System.arraycopy(values, 0, sorted_values, 0, values.length);
		new Sorting(sorted_values, false);
		return sorted_values;
	}

	/**
	 * Sorts the rows of a matrix using a specified column as the key.
	 * Example:<br>
	 * <code>
	 * double[][] a = {{3, 5, 2, 1, 2}, <br>
	 *                 {1, 3, 8, 9, 0}, <br>
	 *                 {2, 4, 2, 5, 6}, <br>
	 *                 {0, 1, 2, 3, 4}, <br>
	 *                 {4, 2, 1, 5, 2}};<br>
	 * double[][] z = sort(a,1);<br>
	 * Result is:<br>
	 * 0  1  2  3  4<br>
	 * 4  2  1  5  2<br>
	 * 1  3  8  9  0<br>
	 * 2  4  2  5  6<br>
	 * 3  5  2  1  2<br>
	 * </code>
	 * Fixed (I hope) 3/28/06.
	 * @param values Input matrix
	 * @param column Index of column to be used as the sorting key.
	 * @return Matrix whose rows have been shuffled around.
	 */
	public static double[][] sort(double[][] values, int column) {
		double[][] sorted_values = new double[values.length][values[0].length];
		Sorting s = new Sorting(getColumnCopy(values, column), false);
		for (int i = 0; i < sorted_values.length; i++)
			System.arraycopy(values[s.getIndex(i)], 0, sorted_values[i], 0, values[s.getIndex(i)].length);

		return sorted_values;
	}

	/**
	 * Transposes an mxn matrix into an nxm matrix. Each row of the input matrix becomes a column in the
	 * output matrix.
	 * @param M Input matrix.
	 * @return Transposed version of M.
	 */
	public static double[][] transpose(double[][] M) {
		double[][] tM = new double[M[0].length][M.length];
		for (int i = 0; i < tM.length; i++)
			for (int j = 0; j < tM[0].length; j++)
				tM[i][j] = M[j][i];
		return tM;
	}
        
        public static double[][] dot(double [][]v1, double [][] v2)  {
            int v1X = v1.length; 
            int v2X = v2.length;
            int v1Y = v1[0].length;
            int v2Y = v2[0].length;
            if (v1X != v2X || v1Y != v2Y)  {
                System.out.println("dimensions in dot() do not fit");
                return null;
            }
            double [][] res = new double [v1X][v1Y];
            for (int r=0; r<v1X; r++)
                for (int c=0; c<v1Y; c++)
                    res[r][c] = v1[r][c]*v2[r][c];
            
            return res;
        }
  
            
            
             
        
}