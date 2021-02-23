package scalaSci.math.array.util;

import java.util.Vector;

import scalaSci.math.array.DoubleArray;

import static scalaSci.math.LinearAlgebra.LinearAlgebra.*;

public class Slicing {

	public final static String AUTO_BOUNDS = "AUTO";

	public final static String UNIFORM_BOUNDS = "UNIFORM";

	//public static double epsilon = 10E-7;

	private double[][] M;

	private int numDimensions;

	private Slice[] slice;

	public Slicing(double[][] m, int[] n, String bounds) {
		DoubleArray.checkColumnDimension(m, n.length);
		M = m;
		numDimensions = M[0].length;
		if (bounds.equals(UNIFORM_BOUNDS)) {
			setSlicesUniformBounds(n);
		} else if (bounds.equals(AUTO_BOUNDS)) {
			setSlicesAutoBounds(n);
		} else {
			throw new IllegalArgumentException("The bounds type : " + bounds + "is unknown. You must specify AUTO or UNIFORM.");
		}

	}

	public Slicing(double[][] m, double[][] bounds) {
		DoubleArray.checkColumnDimension(m, bounds.length);
		M = m;
		numDimensions = M[0].length;
		setSlicesBounds(bounds);
	}

	public Slicing(double[][] m, double[][] centers, double[][] widths) {
		DoubleArray.checkColumnDimension(m, centers.length);
		M = m;
		numDimensions = M[0].length;
		setSlicesCentersnWidths(centers, widths);
	}

	/*public Slicing(double[][] m, int[][] n) {
	    M = m;
	    numDimensions = M[0].length;
	    setSlices(n);
	}*/

	public double[][] getSlicingMatrix() {
		double[][] X = new double[slice.length][numDimensions * 2 + 1];
		for (int i = 0; i < slice.length; i++) {
			System.arraycopy(slice[i].center, 0, X[i], 0, numDimensions);
			X[i][numDimensions] = slice[i].cardinal;
			System.arraycopy(slice[i].width, 0, X[i], numDimensions + 1, numDimensions);
		}
		return X;
	}

	public double[] getSlicesCardinals() {
		double[] cards = new double[slice.length];
		for (int i = 0; i < slice.length; i++) {
			cards[i] = slice[i].cardinal;
		}
		return cards;
	}

	public double[][] getSlicesWidths() {
		double[][] w = new double[slice.length][numDimensions];
		for (int i = 0; i < slice.length; i++) {
			System.arraycopy(slice[i].width, 0, w[i], 0, numDimensions);
		}
		return w;
	}

	public double[][] getSlicesCenters() {
		double[][] c = new double[slice.length][numDimensions];
		for (int i = 0; i < slice.length; i++) {
			System.arraycopy(slice[i].center, 0, c[i], 0, numDimensions);
		}
		return c;
	}

	public int findSlice(double[] x) {
		DoubleArray.checkLength(x, numDimensions);
		int s = -1;
		for (int i = 0; i < slice.length; i++) {
			if (slice[i].isIn(x)) {
				s = i;
			}
		}
		return s;
	}

	public int[][] getSlicesIndexes() {
		int[][] s = new int[slice.length][0];
		for (int i = 0; i < slice.length; i++) {
			s[i] = slice[i].getIndexesArray();
		}
		return s;
	}

	/**
	 * Method used to build slices centers and widths from a definied number of
	 * slices per dimension.
	 * 
	 * @param bounds
	 *            bounds of the slices
	 */

	private void setSlicesBounds(double[][] bounds) {
		DoubleArray.checkRowDimension(bounds, numDimensions);

		int[] numberSlicesPerDimension = new int[numDimensions];
		for (int i = 0; i < numDimensions; i++) {
			numberSlicesPerDimension[i] = bounds[i].length - 1;
		}

		slice = new Slice[cumProd(numberSlicesPerDimension)];

		int[] counter = new int[numDimensions];
		double[] sliceWidth = new double[numDimensions];
		double[] sliceCenter = new double[numDimensions];

		for (int i = 0; i < slice.length; i++) {
			for (int j = 0; j < numDimensions; j++) {
				sliceWidth[j] = bounds[j][counter[j] + 1] - bounds[j][counter[j]];
				sliceCenter[j] = (bounds[j][counter[j] + 1] + bounds[j][counter[j]]) / 2;
			}

			slice[i] = new Slice(sliceCenter, sliceWidth);
			// slice[i].toCommandeLine("slice "+i);

			if (i < (slice.length - 1)) {
				incCounter(counter, numberSlicesPerDimension);
			}
		}

		// for slicesCardinals & elementsIndexes & slices
		countFromBounds();
	}

	/**
	 * Method used to build slices centers and widths from a definied number of
	 * slices per dimension.
	 * 
	 * @param centers
	 *            centers of slices per dimension.
	 * @param widths
	 *            widths of slices per dimension.
	 */

	private void setSlicesCentersnWidths(double[][] centers, double[][] widths) {
		DoubleArray.checkRowDimension(centers, numDimensions);
		DoubleArray.checkRowDimension(widths, numDimensions);

		int[] numberSlicesPerDimension = new int[numDimensions];
		for (int i = 0; i < numDimensions; i++) {
			numberSlicesPerDimension[i] = centers[i].length;
		}

		slice = new Slice[cumProd(numberSlicesPerDimension)];

		int[] counter = new int[numDimensions];
		double[] sliceWidth = new double[numDimensions];
		double[] sliceCenter = new double[numDimensions];

		for (int i = 0; i < slice.length; i++) {
			for (int j = 0; j < numDimensions; j++) {
				sliceWidth[j] = widths[j][counter[j]];
				sliceCenter[j] = centers[j][counter[j]];
			}

			slice[i] = new Slice(sliceCenter, sliceWidth);
			// slice[i].toCommandLine("slice " + i);

			if (i < (slice.length - 1)) {
				incCounter(counter, numberSlicesPerDimension);
			}
		}

		// for slicesCardinals & elementsIndexes & slices
		countFromBounds();
	}

	/**
	 * Method used to build slices centers and widths from a definied number of
	 * slices per dimension. each slice have the same width : Uniform bounds
	 * 
	 * @param numberSlicesPerDimension
	 *            array of number of slices per dimension of the matrix to slice
	 */

	private void setSlicesUniformBounds(int[] numberSlicesPerDimension) {
		slice = new Slice[cumProd(numberSlicesPerDimension)];

		int[] counter = new int[numDimensions];
		double[] sliceWidth = new double[numDimensions];
		double[] sliceCenter = new double[numDimensions];

		double[] Mmin = DoubleArray.min(M);
		double[] Mmax = DoubleArray.max(M);
		double[] pitch = new double[numDimensions];
		for (int j = 0; j < numDimensions; j++) {
			pitch[j] = (Mmax[j] - Mmin[j]) / numberSlicesPerDimension[j];
		}

		for (int i = 0; i < slice.length; i++) {
			for (int j = 0; j < numDimensions; j++) {
				// double min = M.getColumn(j).min().toDouble();
				// double max = M.getColumn(j).max().toDouble();
				// double pitch = (max - min) / numberSlicesPerDimension[j];
				// sliceWidth.set(0, j, pitch);
				// sliceCenter.set(0, j, min + (counter[j] + 0.5) * pitch);
				sliceWidth[j] = pitch[j];
				sliceCenter[j] = Mmin[j] + (counter[j] + 0.5) * pitch[j];
			}

			slice[i] = new Slice(sliceCenter, sliceWidth);
			// slice[i].toCommandLine("slice "+i);

			if (i < (slice.length - 1)) {
				incCounter(counter, numberSlicesPerDimension);
			}
		}

		// for slicesCardinals & elementsIndexes & slices
		countFromBounds();
	}

	/**
	 * Method used to build slices centers and widths from a definied number of
	 * slices per dimension. each slice have same number of elements : Auto
	 * bounds
	 * 
	 * @param numberSlicesPerDimension
	 *            array of number of slices per dimension of the matrix to slice
	 */

	private void setSlicesAutoBounds(int[] numberSlicesPerDimension) {
		slice = new Slice[cumProd(numberSlicesPerDimension)];

		int[] counter = new int[numDimensions];
		double[] sliceWidth = new double[numDimensions];
		double[] sliceCenter = new double[numDimensions];

		// evaluate centers and width per dimension
		int[] numberOfElement = new int[numDimensions];
		double[][] centers = new double[numDimensions][];
		double[][] widths = new double[numDimensions][];
		//int[] sortIndex;
		for (int j = 0; j < numDimensions; j++) {
			double[] column = DoubleArray.getColumnCopy(M, j);
			numberOfElement[j] = column.length / numberSlicesPerDimension[j];

			new Sorting(column, false);

			centers[j] = new double[numberSlicesPerDimension[j]];
			widths[j] = new double[numberSlicesPerDimension[j]];

			int i_min;
			int i_max = -1;
			for (int i = 0; i < numberSlicesPerDimension[j] - 1; i++) {
				// System.out.println("elements : " + (i * numberOfElement[j]) +
				// " -> " + ( (i +
				// 1) * numberOfElement[j] - 1));
				// System.out.println("i=" + i + " j=" + j + "
				// numberOfElement[j]=" + numberOfElement[j]);

				i_min = i_max + 1;
				i_max = Math.max((i + 1) * numberOfElement[j] - 1, i_min);
				// in case column[i_max] == column[i_max + 1], the two slices i
				// and i+1 contains the same two elements, so an error will be
				// found in countFromBounds()
				// so I decided to add column[i_max + 1] in the slice i and
				// offset the bound of slice i+1
				try {
					while (column[i_max] == column[i_max + 1]) {
						i_max++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new IllegalArgumentException("Too much slices defined for the values to slice... you should try to reduce the nyumber of slices !");
				}

				centers[j][i] = (column[i_min] + column[i_max]) / 2;
				widths[j][i] = column[i_max] - column[i_min];
			}
			// the last slice must contains all remaining elements...
			int i = numberSlicesPerDimension[j] - 1;
			i_min = i_max + 1;
			i_max = column.length - 1;

			if (i_min > i_max) {
				throw new IllegalArgumentException("Too much slices defined for the values to slice... you should try to reduce the nyumber of slices !");
			}

			centers[j][i] = (column[i_min] + column[i_max]) / 2;
			widths[j][i] = column[i_max] - column[i_min];
		}

		for (int i = 0; i < slice.length; i++) {
			for (int j = 0; j < numDimensions; j++) {
				sliceWidth[j] = widths[j][counter[j]];
				sliceCenter[j] = centers[j][counter[j]];
			}

			slice[i] = new Slice(sliceCenter, sliceWidth);
			// slice[i].toCommandLine("slice " + i + " !");

			if (i < (slice.length - 1)) {
				incCounter(counter, numberSlicesPerDimension);
			}
		}

		// for slicesCardinals & elementsIndexes & slices
		countFromBounds();
	}

	/**
	 * Method used to build slices centers and widths from a definied number of
	 * slices per dimension. Warning : no test is performed to verify that your
	 * slices are not superimposed !!!
	 * 
	 * @param s
	 *            slices of the matrix
	 */

	/*private void setSlices(int[][] s) {
	    slice = new Slice[s.length];

	    double[] sliceWidth = new double[numDimensions];
	    double[] sliceCenter = new double[numDimensions];

	    double[][] tmp;
	    double[] tmpMax;
	    double[] tmpMin;
	    for (int i = 0; i < slice.length; i++) {
	        tmp = DoubleArray.getRowsCopy(M, s[i]);
	        tmpMax = DoubleArray.max(tmp);
	        tmpMin = DoubleArray.min(tmp);
	        sliceWidth = minus(tmpMax, tmpMin);
	        sliceCenter = times(plus(tmpMax, tmpMin), 0.5);
	        slice[i] = new Slice(sliceCenter, sliceWidth);

	        // for slicesCardinals & elementsIndexes & slices
	        for (int j = 0; j < tmp.length; j++) {
	            slice[i].add(DoubleArray.getRowCopy(tmp, j), s[i][j]);
	        }
	    }
	}*/

	/**
	 * Method used to build slices cardinales and indexes from pre-defined
	 * slices centers and widths.
	 */

	private void countFromBounds() {

		int numOE = M.length;
		// int in;
		for (int i = 0; i < numOE; i++) {
			Vector<Integer> inv = new Vector<Integer>(0);
			// in = 0;
			for (int j = 0; j < slice.length; j++) {
				if (slice[j].isIn(DoubleArray.getRowCopy(M, i))) {
					// if more than one slice contains the current element:
					// M.getRow(i), it will be ignored...
					// very dangerous if you're not sure of your slicing !!!
					// just comment the following line (and it's closing braket)
					// if you want to make the test "more than one slice
					// containg"
					if (inv.size() == 0) {
						slice[j].add(DoubleArray.getRowCopy(M, i), i);
					}
					inv.add(new Integer(j));

					// System.out.println(M.getRow(i).toString()+" is in group
					// "+j);
				} else {
					// System.out.println(M.getRow(i).toString()+" is not in
					// group "+j);
				}
			}
			// "more than one slice containg" TEST
			// this test may be not passed if your slicing is "AUTO" i.e. if
			// slices are bounded by elements
			// if (inv.size() > 1) {
			//
			// String S = "";
			// for (int j = 0; j < inv.size(); j++) {
			// S += slice[ ( (Integer) inv.get(j)).intValue()].toString("slice["
			// + j + "]");
			// }
			//
			// System.err.println("The element = " + M.getRow(i).toString() +
			// " is counted in the first slice of these " + inv.size() + "
			// slices :" + S);
			//
			// }

			// "no slice containg" TEST
			if (inv.size() == 0) {
				String S = "";
				for (int j = 0; j < slice.length; j++) {
					S += slice[ /* ( (Integer) inv.get(j)).intValue() */j].toString("slice[" + j + "]");
				}

				throw new IllegalArgumentException("The element = " + DoubleArray.toString(DoubleArray.getRowsCopy(M, i, i)) + " is in 0 slices :" + S);

			}
		}
	}

	/**
	 * Returns the cumulative product of an integer array.
	 * 
	 * @param a
	 *            array of integer to product
	 * @return a[0]*a[1]*...*a[n]
	 */

	private static int cumProd(int[] a) {
		int res = 1;
		for (int i = 0; i < a.length; i++) {
			res = res * a[i];
		}
		return res;
	}

	/**
	 * Incrementation of a counter in a non-decimal base.
	 * 
	 * @param counter
	 *            counter to increment
	 * @param counterMaxs
	 *            base of each dimension of the counter
	 */

	private void incCounter(int[] counter, int[] counterMaxs) {
		// System.out.print("\n"+arrayToString(counter)+ " /
		// "+arrayToString(counterMaxs));
		int decToInc = 0;
		for (int i = 0; i < counter.length; i++) {
			if (counter[i] < counterMaxs[i] - 1) {
				decToInc = i;
				break;
			} else {
				decToInc++;
			}
		}

		counter[decToInc]++;
		for (int i = 0; i < decToInc; i++) {
			counter[i] = 0;
		}
		// System.out.print(" -> "+arrayToString(counter)+ " /
		// "+arrayToString(counterMaxs));
	}

	public String toString(String title) {
		StringBuffer s = new StringBuffer("\nSlicing " + title + " :");
		for (int i = 0; i < slice.length; i++) {
			s.append(slice[i].toString("Slice " + i));
		}
		return s.toString();
	}

	private class Slice {

		public Vector<Integer> indexes;

		public int cardinal;

		public double[] center;

		public double[] width;

		public Slice(double[] c, double[] w) {
			center = DoubleArray.copy(c);
			width = DoubleArray.copy(w);
			indexes = new Vector<Integer>();
		}

		public boolean isIn(double[] x) {
			boolean in = true;
			for (int j = 0; j < x.length; j++) {
				// exact test
				// boolean inj = ( (x.get(0, j) >= (center.get(0, j) -
				// (width.get(0, j) / 2))) &&
				// (x.get(0, j) <= (center.get(0, j) + (width.get(0, j) / 2))));

				// not exact test, needed for precision problems. Set epsilon to
				// 0 if you want an exact test.
				boolean inj = ((Math.abs(x[j] - center[j]) - width[j] / 2) <= 0.0/*epsilon*/);

				in = in && inj;
			}
			return in;
		}

		public void add(double[] x, int i) {
			// if (!isIn(x)) throw new IllegalArgumentException("Element x=
			// "+x.toString()+" is not include in this slice !");
			cardinal++;
			indexes.add(new Integer(i));
		}

		public int[] getIndexesArray() {
			int[] array = new int[indexes.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = ((Integer) (indexes.get(i))).intValue();
			}
			return array;
		}

		public String toString(String s) {
			StringBuffer st;
			st = new StringBuffer("\n" + s);
			st.append("  contains " + cardinal + " elements\n");
			// st.append(" center = " + DoubleArray.toString(center) + " width =
			// " +
			// DoubleArray.toString(width));
			st.append("  min = " + DoubleArray.toString(minus(center, times(width, 0.5))) + "  max = " + DoubleArray.toString(plus(center, times(width, 0.5)))
					+ "\n");
			st.append("  elements indices : \n");
			for (int i = 0; i < indexes.size(); i++) {
				st.append(" " + ((Integer) (indexes.get(i))).intValue());
			}
			return st.toString();
		}
	}

	public static void main(String[] args) {
		// double[][] X = DoubleArray.random(100, 1,0,1);
		// //X.toCommandLine("X");
		//
		// for (int i = 0; i < 90; i++) {
		// int i1 = (int) (Math.random() * 100);
		// int i2 = (int) (Math.random() * 100);
		// X[i1][0]= X[i2][0];
		// }
		//
		// Slicing s1 = new Slicing(X, new int[] {50}
		// , "AUTO");
		// s1.toCommandLine("");
		//
		// //double[][] X = DoubleArray.random(10, 2, 0, 1);
		// double[][] X = { {0.1150333685, 0.2249385189}
		// , {0.1010784039, 0.0801458224}
		// , {0.4389139657, 0.3090770470}
		// , {0.3227165908, 0.2137681387}
		// , {0.0439011003, 0.8825942143}
		// , {0.0435162036, 0.6976747539}
		// , {0.8164387558, 0.3543210610}
		// , {0.6281565206, 0.3976606322}
		// , {0.2258957129, 0.1568657272}
		// , {0.3836445557, 0.9809615312}
		// };
		//
		// DoubleArray.toCommandLine(X, "X");
		//
		// Slicing s1 = new Slicing(X, new int[] {3, 2}
		// , "AUTO");
		// s1.toCommandLine("");
		//
		// double[] boundsX = new double[] {0,0.1,0.5,0.9,1};
		// double[] boundsY = new double[] {0,0.5,1};
		// Slicing s2 = new Slicing(X,new double[][] {boundsX,boundsY});
		// s2.toCommandLine("");
		//
		// Slicing s3 = new Slicing(X,new int[][] {{0,1,2,3},{4,5,6,7,8},{9}});
		// s3.toCommandLine("");
		//
		// Matrix centersX = new Matrix(new double[] {0.05, 0.15, 0.25, 0.35,
		// 0.45, 0.55, 0.65, 0.75, 0.85, 0.95}
		// , 10);
		// Matrix widthsX = new Matrix(new double[] {0.1, 0.1, 0.1, 0.1, 0.1,
		// 0.1, 0.1, 0.1, 0.1, 0.1}
		// , 10);
		// Matrix centersY = new Matrix(new double[] {0.1, 0.3, 0.5, 0.7, 0.9}
		// , 5);
		// Matrix widthsY = new Matrix(new double[] {0.2, 0.2, 0.2, 0.2, 0.2}
		// , 5);
		// Slicing s4 = new Slicing(X, new Matrix[] {centersX, centersY}
		// , new Matrix[] {widthsX, widthsY});
		// s4.toCommandLine("");

	}

}