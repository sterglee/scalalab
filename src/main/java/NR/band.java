
package NR;

import static NR.Common.*;

public class band {
    
    public static  void  bandec(double [][]  a,  int m1,  int m2, double [][] al, int  [] indx,  double [] d)
    {
	final double TINY = 1.0e-20;
	int i, j, k, l, mm;
	double  dum;

	int n=a.length;
	mm = m1+m2+1;
	l = m1;
	for (i=0;i<m1;i++) {
                        for (j=m1-i;j<mm;j++) a[i][j-l]=a[i][j];
                        l--;
                        for (j=mm-l-1;j<mm;j++) a[i][j]=0.0;
	}
	d[0] = 1.0;
	l = m1;
	for (k=0;k<n;k++) {
                        dum=a[k][0];
                        i=k;
                        if (l<n) l++;
                        for (j=k+1;j<l;j++) {
                            if (Math.abs(a[j][0]) > Math.abs(dum)) {
		dum=a[j][0];
		i=j;
                            }
	}
	indx[k]=i+1;
	if (dum == 0.0) a[k][0] = TINY;
	if (i != k) {
                        d[0]  = -d[0];
	 for (j=0;j<mm;j++) swap(a, k, j, i, j);
	   }
	 for (i=k+1;i<l;i++) {
	   dum=a[i][0]/a[k][0];
	   al[k][i-k-1]=dum;
	  for (j=1;j<mm;j++) a[i][j-1]=a[i][j]-dum*a[k][j];
	    a[i][mm-1]=0.0;
	}
	}
}
    
    
public static void  banbks(double [][] a,  int  m1,  int m2,  double [][] al,  int []  indx,  double [] b)
{
    int i,j,k,l,mm;
    double  dum;

    int n = a.length;
    mm = m1+m2+1;
    l = m1;
    for (k=0; k<n; k++) {
        j = indx[k]-1;
        if (j!=k) swap(b, k, j);
        if (l<n) l++;
        for (j=k+1; j<l; j++) b[j] -= al[k][j-k-1]*b[k];
   }
	l=1;
	for (i=n-1;i>=0;i--) {
		dum=b[i];
		for (k=1;k<l;k++) dum -= a[i][k]*b[k+i];
		b[i]=dum / a[i][0];
		if (l<mm)  l++;
	}
}


public static  void  banmul(double [][] a,  int  m1,  int  m2,  double [] x,  double [] b)
{
    int i,j,k,tmploop;

    int n = a.length;
    for (i=0; i<n; i++) {
        k = i-m1;
        tmploop = MIN(m1+m2+1, (int)(n-k));
        b[i] = 0.0;
        for (j = MAX(0,-k); j<tmploop; j++) 
            b[i] += a[i][j]*x[j+k];
	}
}


}
