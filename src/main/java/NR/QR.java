
package NR;

import static NR.Common.*; 

public class QR {
     int  n;
    double [][]   qt, r;
    boolean  sing;

    // constructs the QR decomposition of a[0..n-1][0..n-1].  The upper triangular matrix R is returned in the 
    // upper triangle of a, except for the diagonal elements of R which are returned in d[0..n-1]
    // The orthogonal matrix Q is represented as a product of n-1 Householder matrices Q0, .. Q_{n-2}
    // sing[0]  returns true if singularity is encountered during the decomposition, but the decomposition is still completed in this case;
    // otherwise it returns false
        
public static void  qrdcmp(double [][]  a,  double [] c,  double [] d, boolean [] sing)
{
	int  i,j,k;
	double scale,sigma,sum,tau;

	int n = a.length;
	sing[0] = false;
	for (k =0; k<n-1; k++) {
                        scale = 0.0;
                        for (i=k ;i<n; i++) 
                               scale = MAX( scale, Math.abs( a[i][k] ) );
	         if (scale == 0.0) {
                                sing[0] = true;
                                c[k] = d[k] = 0.0;
	   } else {
	for (i=k;i<n;i++) a[i][k] /= scale;
	for (sum=0.0,i=k;i<n;i++) sum += (  a[i][k] * a[i][k]);
	sigma = SIGN(Math.sqrt(sum), a[k][k] );
	a[k][k] += sigma;
	c[k]=sigma*a[k][k];
	d[k] = -scale*sigma;
	for (j=k+1;j<n;j++) {
                        for (sum=0.0,i=k;i<n;i++) sum += a[i][k]*a[i][j];
                            tau=sum/c[k];
                        for (i=k;i<n;i++) a[i][j] -= tau*a[i][k];
                        }
	}
                }
	d[n-1]=a[n-1][n-1];
	if (d[n-1] == 0.0) sing[0] = true;
}

// Solves the set of n linear equations Ax = b, a[0..n-1][0..n-1], c[0..n-1], and
// d[0..n-1] are input as the output of the routine qrdcmp and are not modified. 
// b[0..n-1] is input as the right-hand side vector, and is overwritten with the solution vector on output
public static void  qrsolv( double [][] a,   double [] c, double [] d, double [] b)
 {
    int  i, j;
   double   sum, tau;

    int n = a.length;
    for (j = 0;j < n-1; j++) {
        for (sum = 0.0, i = j ; i < n; i++) sum += a[i][j]*b[i];
        tau = sum / c[j];
        for (i = j; i < n; i++ ) b[i] -= tau*a[i][j];
    }
    rsolv(a, d, b);
}

// Solves the set of n linear equations Rx = b, where R is upper triangular matrix stored in a and  d
// a[0..n-1][0..n-1] and d[0..n-1] are input as the output of the routine qrdcmp and are not modified
// b[0..n-1] is input as the right-hand side vector, and is overwritten with the solution vector on output
public  static void  rsolv(double [][] a, double [] d, double [] b)
{
	int i, j;
	double  sum;

	int n = a.length;
	b[n-1] /= d[n-1];
	for (i=n-2;i>=0;i--) {
		for (sum=0.0,j=i+1;j<n;j++) sum += a[i][j]*b[j];
		b[i]=(b[i]-sum)/d[i];
	}
}


// Given the QR decomposition of some nXn matrix, calculates the QR decomposition of the "updated" matrix
// Quantities are dimensioned as r[0..n-1][0..n-1], qt[0..n-1][0..n-1], u[0..n-1], and v[0..n-1]
public static  void   qrupdt(double [][] r,  double [][]  qt,  double [] u, double [] v)
{
    int i, k;

    int n = u.length;
    for (k = n-1 ;k >= 0; k--)
        if (u[k] != 0.0) break;
        if (k < 0) k=0;
        for (i = k-1; i>= 0; i--) {
	rotate(r,qt,i,u[i],-u[i+1]);
	if (u[i] == 0.0)
	u[i] = Math.abs(u[i+1]);
	else if (Math.abs(u[i]) > Math.abs(u[i+1]))
                        u[i] = Math.abs(u[i]) * Math.sqrt(1.0 + SQR(u[i+1]/u[i]));
	 else u[i] = Math.abs(u[i+1]) * Math.sqrt(1.0+SQR(u[i]/u[i+1]));
	}
	for (i=0; i<n; i++) r[0][i] += u[0]*v[i];
	for (i=0;i<k;i++)
                        rotate(r, qt, i, r[i][i], -r[i+1][i]);
}


// Given matrices r[0..n-1][0..n-1] and qt[0..n-1][0..n-1], carry out a Jacobi rotation on rows i and
// i+1 of each matrix. a and b are the parameters of the rotation
public static void  rotate(double [][] r,  double [][]  qt,  int  i,  double  a, double b)
{
    int   j;
   double  c, fact, s, w, y;

    int n = r.length;
    if (a == 0.0) {
        c=0.0;
        s=(b >= 0.0 ? 1.0 : -1.0);
    } 
    else 
        if (Math.abs(a) > Math.abs(b)) {
            fact = b/a;
            c = SIGN(1.0/Math.sqrt(1.0+(fact*fact)), a);
            s = fact * c;
           } else {
        fact = a/b;
        s = SIGN(1.0/Math.sqrt(1.0+(fact*fact)),b);
        c=fact*s;
	}
        for (j=i; j<n; j++) {
            y = r[i][j];
            w = r[i+1][j];
            r[i][j] = c*y-s*w;
            r[i+1][j]=s*y+c*w;
            }
        for (j=0;j<n;j++) {
            y = qt[i][j];
            w = qt[i+1][j];
            qt[i][j] = c*y-s*w;
            qt[i+1][j] = s*y+c*w;
            }
}


}
