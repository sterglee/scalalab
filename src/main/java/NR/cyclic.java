
package NR;

import static NR.Common.*;
import static NR.tridiag.*;

public class cyclic {
    
    
public static void  cyclic(double []  a,  double [] b,  double [] c,  double  alpha,
	double  beta,  double [] r,  double [] x) throws NRException
{
    int i;
    double  fact,gamma;

    int n = a.length;
    if (n <= 2) 
        throw new NRException("n too small in cyclic");
        
    double [] bb = new double[n];
    double [] u = new double[n];
    double [] z = new double [n];
	
    gamma = -b[0];
    bb[0]=b[0]-gamma;
    bb[n-1]=b[n-1]-alpha*beta/gamma;
    for (i=1; i<n-1; i++)  bb[i]=b[i];
    tridag(a,bb,c,r,x);
    u[0]=gamma;
    u[n-1]=alpha;
    for (i=1;i<n-1;i++) u[i]=0.0;
    tridag(a,bb,c,u,z);
    fact=(x[0]+beta*x[n-1]/gamma)/
    (1.0+z[0]+beta*z[n-1]/gamma);
    for (i=0;i<n;i++) x[i] -= fact*z[i];
}

}
