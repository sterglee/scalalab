
package NR;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LU {

    public static final double TINY = 1.0e-20;

	/**
	 * Given a matrix a[0..n-1][0..n-1], this routine replaces it by the LU
	 * decomposition of a rowwise permutation of itself. a and n are input. a is input
	 * 
	 * @param a
	 *            the matrix to be decomposed
	 * @param indx
	 *            the array to put the return index into
	 * @return +1 or -1 signifying whether the number of row interchanges is odd
	 *         or even
	 */
   public static double ludcmp(double a[][], int indx[])
			{
	int n = a.length;
	int i = 0, imax = 0, j = 0, k = 0;
	double big, dum, sum, temp;
	double d = 1.0;
	double vv[] = new double[n];
	for (i = 0; i < n; i++) {
                        big = 0.0;
                        for (j = 0; j < n; j++)
                            if ((temp = Math.abs(a[i][j])) > big)
		big = temp;

	if (big == 0.0) {
                try {
                    // no non-zero largest element

                    throw new NRException("Error: Singular linearized system. Computation cannot proceed.");
                } catch (NRException ex) {
                    ex.printStackTrace();
                }

	}
	vv[i] = 1.0 / big;
       }

       for (j = 0; j < n; j++) {
	for (i = 0; i < j; i++) {
                            sum = a[i][j];
                            for (k = 0; k < i; k++)
                            sum -= (a[i][k] * a[k][j]);
                            a[i][j] = sum;
	}
	big = 0.0;
	for (i = j; i < n; i++) {
                            sum = a[i][j];
                            for (k = 0; k < j; k++)
                                sum -= (a[i][k] * a[k][j]);
                            a[i][j] = sum;
	
                            if ((dum = vv[i] * Math.abs(sum)) >= big) {
		big = dum;
		imax = i;
		}
	}
	if (j != imax) {
		for (k = 0; k < n; k++) {
		dum = a[imax][k];
		a[imax][k] = a[j][k];
		a[j][k] = dum;
                                }
                            d = -d;
                            vv[imax] = vv[j];
                    }
	indx[j] = imax;
	// replace zero values with a nigh zero value so that
	// we don't get any divisions by zero.
                  if (a[j][j] == 0.0)
	    a[j][j] = TINY;

	if (j != n-1) {
	dum = 1.0 / (a[j][j]);
	for (i = j + 1; i < n; i++)
	  a[i][j] *= dum;
	}
       }
	return d;
	}

   
	/**
	 * Solves the set of n linear equations AX = B. Here a[0..n-1][0..n-1] is input,
	 * not as the matrix A but rather as its LU decomposition, determined by the
	 * routine ludcmp. indx[1..n] is input as the permutation vector returned by
	 * ludcmp. b[1..n] is input as the right hand side vector B, and returns
	 * with the solution vector X. a, and indx are not modified by this routine
	 * and can be left in place for successive calls with different right-hand
	 * sides b. This routine takes into account the possibility that b will
	 * begin with many zero elements, so it is efficient for use in matrix
	 * inversion.
	 * 
	 * @param a
	 *            the matrix to be solved as described
	 * @param indx
	 *            the array returned by ludcmp
	 * @param b
	 *            the vector to be solbed as described
	 */
 public static double []  solveNR(double a[][],  double[] b) {
   int N = b.length;
   int  [] idxs = new int[N];
   double [][] ac =  Common.copy(a);
   double d = ludcmp(ac, idxs);
   double [] bc = Common.copy(b);
                         
   lubksb(ac, idxs, bc);
   return bc;
   }
  
public static double [] []  solveNR(double [][] a,  double [][]  b) {
    int br = b.length;
    double [] bb = new double[br];
    for (int r=0; r<br; r++)
       bb[r] = b[r][0];
    double [] solx = solveNR(a, bb);
    double [][] solxr = new double[br][1];
    for (int r=0; r<br; r++)
        solxr[r][0] = solx[r];
     return   solxr;
    }
  
                 
/*
     A = [[4.5, 6.7, 3.4], [-3.4, 3.4, 0.3], [0.3, 4.5, 6.7]] as double[][]
     B = [2, 3, 5] as double []
     
     x = NR.LU.solveNR(A, B)
     B

     A*x-B
*/
	public static void lubksb(double a[][], int indx[], double[] b) {
		int ii = 0, ip = 0;

		double sum = 0.0;

		int n = a.length;

	for (int i=0;i<n;i++) {
	  ip=indx[i];
	  sum=b[ip];
	  b[ip]=b[i];
	  if (ii != 0)
	    for (int j=ii-1;j<i;j++) sum -= a[i][j]*b[j];
	  else if (sum != 0.0)
			ii=i+1;
		b[i]=sum;
	}
	for (int i=n-1;i>=0;i--) {
		sum=b[i];
		for (int j=i+1;j<n;j++) sum -= a[i][j]*b[j];
		b[i]=sum/a[i][i];
	}

	}

        /*
            x = AAD (" 3.4, 6.7 ; 4.5, 7.8")
            ix = NR.LU.inv(x)
            tstx = x*ix
         */
public static double [][] inv(double [][] a)     {
  int N = a.length;
  double [] col    = new double[N];
  int [] indx  = new int[N];
  double [][] y = new double[N][N];
  
  double d = ludcmp(a, indx);
  for (int j=0; j<N; j++ ) {
      for (int i=0; i<N; i++) 
           col[i] = 0.0;
      col[j] = 1.0;
      lubksb(a, indx, col);
      for (int i=0; i<N; i++)
          y[i][j] = col[i];
  }
  return y; 
 }

public static void main(String [] args) {
   double [][] a = {{ 3.4, 6.7}, {4.5, 7.8}};  
   
   double [][] b = inv(a);
   
   System.out.println("inva = "+a[0][0]);
}
}