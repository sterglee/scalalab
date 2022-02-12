package NR;

import  static NR.Common.*;

/* Linear equation solution by Gauss-Jordan elimination. 
The input matrix a[0..n-1][0..n-1], b[0..n-1][0..n-1] is input containing the m  right-hand size vectors.
 On output, a is replaced by its matrix inverse, and b is replaced by the corresponding set of solution vectors
 */
public class gaussj {

    // inverse of a matrix with gaussj
    public static double [][] invgj(double [][]a) {
        int  n = a.length; int m = a[0].length;
        double [][] b = new double [n][1];
        for (int r=0; r<b.length; r++)
            a[r][0] = 1.0;
        
        double [][] ac = Common.copy(a);
        gaussj(ac, b);
        return ac;
    }

    public static void gaussj(double [][]  a, double [][]b)

{
    int  i, icol = 0, irow = 0, j, k, l, ll, n = a.length, m = b[0].length;
    double  big, dum, pivinv;
    int  [] indxc = new int [n];
    int  [] indxr = new int[n];
    int  [] ipiv = new int[n];
    
    for (j = 0; j < n; j++) {
            ipiv[j] = 0;
      }
        for (i = 0; i < n; i++) {
            big = 0.0;
            for (j = 0; j < n; j++) {
                if (ipiv[j] != 1) {
                    for (k = 0; k < n; k++) {
                        if (ipiv[k] == 0) {
                            if (Math.abs(a[j][k]) >= big) {
                                big = Math.abs(a[j][k]);
                                irow = j;
                                icol = k;
                            }
                        }
                    }
                }
            }
            ++(ipiv[icol]);
            if (irow != icol) {
                for (l = 0; l < n; l++) {
                    swap(a, irow, l, icol, l);
                }
                for (l = 0; l < m; l++) {
                    swap(b, irow, l, icol, l);
                }
            }
            indxr[i] = irow;
            indxc[i] = icol;
            if (a[icol][icol] == 0.0) {
                System.out.println("gaussj: Singular Matrix");
                return;
            }
            pivinv = 1.0 / a[icol][icol];
            a[icol][icol] = 1.0;
            for (l = 0; l < n; l++) {
                a[icol][l] *= pivinv;
            }
            for (l = 0; l < m; l++) {
                b[icol][l] *= pivinv;
            }
            for (ll = 0; ll < n; ll++) {
                if (ll != icol) {
                    dum = a[ll][icol];
                    a[ll][icol] = 0.0;
                    for (l = 0; l < n; l++) {
                        a[ll][l] -= a[icol][l] * dum;
                    }
                    for (l = 0; l < m; l++) {
                        b[ll][l] -= b[icol][l] * dum;
                    }
                }
            }
        }
        for (l = n - 1; l >= 0; l--) {
            if (indxr[l] != indxc[l]) {
                for (k = 0; k < n; k++) {
                    swap(a, k, indxr[l], k, indxc[l]);
                }
            }
        }
    }

}
