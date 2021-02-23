package scalaSci.math.LinearAlgebra;

import scalaSci.math.array.*;
import Jama.JamaCholeskyDecomposition;

import Jama.JamaEigenvalueDecomposition;
import Jama.JamaLUDecomposition;
import Jama.JamaQRDecomposition;
import Jama.jMatrix;
import Jama.JamaSingularValueDecomposition;

/**
 * A collection of static methods for performing math operations on matrices and arrays.
 * Advanced Linear Algebra methods (decompositions, norm, ...) are just call for JAMA routines.
 * 
 * @author richet
 */
public class LinearAlgebra extends DoubleArray {

   // linear algebra methods

  /**
 * Element-wise subtraction of two arrays. Arrays must be same size.
 * @param v1 Minuend.
 * @param v2 Subtrahend
 * @return Array v1 - v2
*/
  public static double[] minus(double[] v1, double[] v2) {
    checkLength(v2, v1.length);
    double[] array = new double[v1.length];
    for (int i = 0; i < v1.length; i++)
        array[i] = v1[i] - v2[i];
    return array;
 }

	/**
	 * Subtracts a scalar value from each element of an array
	 * @param v1 Minuend Array.
	 * @param v Subtrahend scalar
	 * @return Array v1 - v
	 */
	public static double[] minus(double[] v1, double v) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] - v;
		return array;
	}

	/**
	 * Subtracts each element of an array from a scalar value.
	 * @param v Scalar Minuend
	 * @param v1 Subtrahend array
	 * @return Array v - v1
	 */
	public static double[] minus(double v, double[] v1) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v - v1[i];
		return array;
	}

	/**
	 * Element-wise subtraction of two matrices. Matrices must be same size.
	 * @param v1 Minuend matrix
	 * @param v2 Subtrahend matrix
	 * @return jMatrix v1 - v2
	 */
	public static double[][] minus(double[][] v1, double[][] v2) {
		checkRowDimension(v2, v1.length);
		checkColumnDimension(v2, v1[0].length);
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v1[i][j] - v2[i][j];
		return array;
	}

	/**
	 * Subtract a scalar from each element of a matrix.
	 * @param v1 Minuend matrix
	 * @param v2 Scalar subtrahend
	 * @return jMatrix v1 - v2
	 */
	public static double[][] minus(double[][] v1, double v2) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v1[i][j] - v2;
		return array;
	}

	/**
	 * Subtract each element of a matrix from a scalar.
	 * @param v2 Scalar minuend
	 * @param v1 jMatrix subtrahend
	 * @return jMatrix v2 - v1
	 */
	public static double[][] minus(double v2, double[][] v1) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v2 - v1[i][j];
		return array;
	}

	/**
	 * Element-wise sum of any number of arrays. Each array must be of same length.
	 * @param v Any number of arrays
	 * @return Element-wise sum of input arrays.
	 */
	public static double[] plus(double[]... v) {
		double[] array = new double[v[0].length];
		for (int j = 0; j < v.length; j++)
			for (int i = 0; i < v[j].length; i++)
				array[i] += v[j][i];
		return array;
	}

	/**
	 * Add a scalar value to each element of an array.
	 * @param v1 Array
	 * @param v Scalar
	 * @return v1 + v
	 */
	public static double[] plus(double[] v1, double v) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] + v;
		return array;
	}

public static double[] plus(double v, double v1[]) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] + v;
		return array;
	}

        /**
	 * Element-wise sum of two matrices
	 * @param v1 jMatrix
	 * @param v2 jMatrix
	 * @return jMatrix v1 + v2
	 */
	public static double[][] plus(double[][] v1, double[][] v2) {
		checkRowDimension(v2, v1.length);
		checkColumnDimension(v2, v1[0].length);
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v1[i][j] + v2[i][j];
		return array;
	}

	/**
	 * Add a scalar to each element of a matrix.
	 * @param v1 jMatrix
	 * @param v2 Scalar
	 * @return jMatrix v1 + v2
	 */
	public static double[][] plus(double[][] v1, double v2) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v1[i][j] + v2;
		return array;
	}

        public static double[][] plus( double v2, double[][] v1) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[0].length; j++)
				array[i][j] = v1[i][j] + v2;
		return array;
	}

	/**
	 * Element-wise product of any number of arrays. Each array must be same size. 
	 * @param v Any number of arrays.
	 * @return Array. i'th element = v1(i)*v2(i)*v3(i)...
	 */
	public static double[] times(double[]... v) {
		double[] array = fill(v[0].length, 1.0);
		for (int j = 0; j < v.length; j++)
			for (int i = 0; i < v[j].length; i++)
				array[i] *= v[j][i];
		return array;
	}

	/**
	 * Element-wise ratio of two arrays.
	 * @param v1 Numerators
	 * @param v2 Denominators
	 * @return Array. i'th element = v1(i)/v2(i)
	 */
	public static double[] divide(double[] v1, double[] v2) {
		checkLength(v1, v2.length);
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] / v2[i];
		return array;
	}

	/**
	 * Multiply each element of an array by a scalar.
	 * @param v1 Array
	 * @param v Scalar
	 * @return v1 * v
	 */
	public static double[] times(double[] v1, double v) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] * v;
		return array;
	}

        public static double[] times(double v, double[] v1) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] * v;
		return array;
	}
        
	/**
	 * Multiply each element in a matrix by a scalar
	 * @param v1 jMatrix
	 * @param v Scalar
	 * @return v1 * v
	 */
	public static double[][] times(double[][] v1, double v) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[i].length; j++)
				array[i][j] = v1[i][j] * v;
		return array;
	}

        public static double[][] times(double v, double [][] v1) {
                double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[i].length; j++)
				array[i][j] = v1[i][j] * v;
		return array;
	}
	/**
	 * Divide each element of an array by a scalar.
	 * @param v1 Numerator Array
	 * @param v Scalar denominator
	 * @return Array. i'th element is v1(i)/v
	 */
	public static double[] divide(double[] v1, double v) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = v1[i] / v;
		return array;
	}

	/**
	 * Divide each element of a matrix by a scalar
	 * @param v1 jMatrix numerator
	 * @param v Scalar denominator
	 * @return jMatrix v1 / v
	 */
	public static double[][] divide(double[][] v1, double v) {
		double[][] array = new double[v1.length][v1[0].length];
		for (int i = 0; i < v1.length; i++)
			for (int j = 0; j < v1[i].length; j++)
				array[i][j] = v1[i][j] / v;
		return array;
	}

	/**
	 * Raise each element of an array to a scalar power.
	 * @param v Array
	 * @param n Scalar exponent
	 * @return Array. i'th element is v(i)^n
	 */
	public static double[] raise(double[] v1, double n) {
		double[] array = new double[v1.length];
		for (int i = 0; i < v1.length; i++)
			array[i] = Math.pow(v1[i], n);
		return array;
	}

        
	public static double[][] raise(double[][] v, double n) {
		double[][] array = new double[v.length][v[0].length];
		for (int i = 0; i < v.length; i++)
			for (int j = 0; j < v[i].length; j++)
				array[i][j] = Math.pow(v[i][j], n);
		return array;
	}

	
    public static double[][] times(double[][] v1, double[][] v2) {  
        
        int v1Rows = v1.length;  // # rows of the result matrix
        int v2Cols = v2[0].length;  // # cols of the result matrix
        double [][] result = new double[v1Rows][v2Cols];
        int v1Cols = v1[0].length;
       double[] v1Colj = new double[v1Cols];

    for (int j = 0; j < v2Cols; j++) {
      for (int k = 0; k < v1Cols; k++) {
        v1Colj[k] =v2[k][j];
      }
      for (int i = 0; i < v1Rows; i++) {
        double[] Arowi = v1[i];
        double s = 0;
        for (int k = 0; k < v1Cols; k++) {
          s += Arowi[k]*v1Colj[k];
        }
       result[i][j] = s;
            }
      }
      return result;
   }
    
    	public static double[] times(double[][] v1, double[] v2) {
		checkLength(v2, v1[0].length);
		return getColumnCopy(times(v1, columnVector(v2)), 0);
	}

	// Now follows JAMA methods calls //

	public static double[][] divideLU(double[][] v1, double[]... v2) {
		return LU(v2).solve(jMatrix.constructWithCopy(v1)).getArray();
	}

	public static double[][] divideQR(double[][] v1, double[]... v2) {
		return  QR(v2).solve(jMatrix.constructWithCopy(v1)).getArray(); 
	}

	public static double[][] divide(double[][] v1, double[]... v2) {
		return divideQR(v1, v2);
	}

        
	
        public static double[][] inverseLU(double[][] v1) {
		checkColumnDimension(v1, v1.length);
		return LU(v1).solve(jMatrix.identity(v1.length, v1.length)).getArray();
	}

	public static double[][] inverseQR(double[][] v1) {
		checkColumnDimension(v1, v1.length);
		return QR(v1).solve(jMatrix.identity(v1.length, v1.length)).getArray();
	}

	public static double[][] inverse(double[][] v1) {
		return new jMatrix(v1).inverse().getArray();
	}

	public static double[][] solve(double[][] A, double[][] B) {
		return new jMatrix(A).solve(new jMatrix(B)).getArray();
	}

	public static Jama.JamaEigenvalueDecomposition eigen(double[][] v) {
		return new Jama.JamaEigenvalueDecomposition(v);
	}

	public static Jama.JamaQRDecomposition QR(double[][] v) {
		return new Jama.JamaQRDecomposition(jMatrix.constructWithCopy(v));
	}

	public static Jama.JamaLUDecomposition LU(double[][] v) {
		return new Jama.JamaLUDecomposition(jMatrix.constructWithCopy(v));
	}

	public static Jama.JamaCholeskyDecomposition cholesky(double[][] v) {
		return new Jama.JamaCholeskyDecomposition(new jMatrix(v));
	}

	public static Jama.JamaSingularValueDecomposition singular(double[][] v) {
		return new Jama.JamaSingularValueDecomposition(new jMatrix(v));
	}

	public static double cond(double[][] v) {
		return new jMatrix(v).cond();
	}

	public static double det(double[][] v) {
		return new jMatrix(v).det();
	}

	public static int rank(double[][] v) {
		return new jMatrix(v).rank();
	}

	public static double trace(double[][] v) {
		return new jMatrix(v).trace();
	}

	public static double norm1(double[][] v) {
		return new jMatrix(v).norm1();
	}

	public static double norm2(double[][] v) {
		return new jMatrix(v).norm2();
	}

	public static double normF(double[][] v) {
		return new jMatrix(v).normF();
	}

	public static double normInf(double[][] v) {
		return new jMatrix(v).normInf();
	}

}
