
package NR;

import static NR.Common.*;

public class tridiag {

    
public static void  tridag(double [] a,  double [] b,  double [] c,  double []  r,  double [] u) throws NRException
{
    int  j;
    double   bet;

    int n = a.length;
    double [] gam = new double [n];
    if (b[0] == 0.0) throw new NRException("Error 1 in tridag");
    u[0]=r[0]/(bet=b[0]);
    for (j=1;j<n;j++) {
        gam[j] = c[j-1]/bet;
        bet = b[j]-a[j] * gam[j];
        if (bet == 0.0) throw new NRException("Error 2 in tridag");
        u[j]=(r[j]-a[j]*u[j-1])/bet;
	}
        for (j=(n-2);j>=0;j--)
            u[j] -= gam[j+1]*u[j+1];
 }
}
