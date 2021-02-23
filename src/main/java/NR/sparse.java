
package NR;

import static NR.Common.*;
import static NR.indexx.*;

public class sparse {
    

public static void  sprsin(double [][]  a,  double  thresh, double [] sa, double []ija) throws NRException
{
    int i, j, k;

    int n = a.length;
    int nmax = sa.length;
    for (j=0; j<n; j++)  sa[j] = a[j][j];
    ija[0]=n+1;
    k=n;
    for (i=0;i<n;i++) {
        for (j=0;j<n;j++) {
            if (Math.abs(a[i][j]) >= thresh && i != j) {
	if (++k > nmax)  throw new NRException("sprsin: sa and ija too small");
	sa[k]=a[i][j];
	ija[k]=j;
	}
        }
    ija[i+1] = k+1;
	}
 }

public static void sprsax(int []  sa, int []  ija, int [] x, double [] b) throws NRException
{
    int i,k;

    int n = x.length;
    if (ija[0] != n+1)
        throw new NRException("sprsax: mismatched vector and matrix");
    for (i=0; i<n; i++) {
    b[i] = sa[i]*x[i];
    for (k=ija[i];k<ija[i+1];k++) {
        b[i] += sa[k]*x[ija[k]];
    	}
    }
}


public static void sprstx(int [] sa, int [] ija,  int [] x, double [] b) throws NRException
{
    int i,j,k;

    int n = x.length;
    if (ija[0] != (n+1))
        throw new NRException("mismatched vector and matrix in sprstx");
    for (i=0; i<n; i++)  b[i] = sa[i]*x[i];
    for (i=0; i<n; i++) {
        for (k=ija[i]; k<ija[i+1]; k++) {
            j = ija[k];
            b[j] += sa[k]*x[i];
	}
  }
}


public static void  sprstp(int [] sa,  int [] ija,  double []  sb,  int [] ijb) throws NRException
{
    int j, jl, jm, jp, ju, k, m, n2, noff, inc, iv;
    double  v;

    n2 = ija[0];
    for (j=0; j<n2-1; j++) sb[j] = sa[j];
    
    int []  ija_vec = nfill(ija[n2], ija[n2-1] - ija[0]);
    int []  ijb_vec = new int[ ija[n2-1]-ija[0] ];
    
	indexx(ija_vec,ijb_vec);
	for (j=n2,k=0;j < ija[n2-1];j++,k++) {
		ijb[j]=ijb_vec[k];
	}
	jp=0;
	for (k=ija[0];k<ija[n2-1];k++) {
		m=ijb[k]+n2;
		sb[k]=sa[m];
		for (j=jp;j<ija[m]+1;j++)
			ijb[j]=k;
		jp=ija[m]+1;
		jl=0;
		ju=n2-1;
		while (ju-jl > 1) {
			jm=(ju+jl)/2;
			if (ija[jm] > m) ju=jm; else jl=jm;
		}
		ijb[k]=jl;
	}
	for (j=jp;j<n2;j++) ijb[j]=ija[n2-1];
	for (j=0;j<n2-1;j++) {
		jl=ijb[j+1]-ijb[j];
		noff=ijb[j];
		inc=1;
		do {
			inc *= 3;
			inc++;
		} while (inc <= jl);
		do {
			inc /= 3;
			for (k=noff+inc;k<noff+jl;k++) {
				iv=ijb[k];
				v=sb[k];
				m=k;
				while (ijb[m-inc] > iv) {
					ijb[m]=ijb[m-inc];
					sb[m]=sb[m-inc];
					m -= inc;
					if (m-noff+1 <= inc) break;
				}
				ijb[m]=iv;
				sb[m]=v;
			}
		} while (inc > 1);
	}
}

}
