# Introduction #

`Thanks, to the efficient JIT Compiler of the Java Virtual Machine the performance of ScalaLab is usually better than unoptimized C code and comes close to even optimized C code.`

`For example, here is an SVD implementation implemented in pure C. `

```

#include <math.h>

#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#define NR_END 1
#define FREE_ARG char*

#define SQR(x) (x*x)
#define FMAX(a,b) (a > b ? a : b)
#define IMAX(a,b) (a > b ? a : b)
#define IMIN(a,b) (a > b ? b : a)
#define SIGN(a, b) (b >= 0 ? (a >= 0 ? a : -a) : (a >= 0 ? -a : a))

 
#include <time.h>


double pythag(double a, double b)
{
	double absa,absb;
	absa=fabs(a);
	absb=fabs(b);
	if (absa > absb) return absa*sqrt(1.0+SQR(absb/absa));
	else return (absb == 0.0 ? 0.0 : absb*sqrt(1.0+SQR(absa/absb)));
}

double  *vector(int nl, int nh)
/* allocate a double vector with subscript range v[nl..nh] */
{
	double *v;

	v=(double *)malloc((size_t) ((nh-nl+1+NR_END)*sizeof(double)));
	if (!v) printf("allocation failure in vector()");
	return v-nl+NR_END;
}



double **matrix(int nrl, int nrh, int ncl, int nch)
/* allocate a float matrix with subscript range m[nrl..nrh][ncl..nch] */
{
        int i, nrow=nrh-nrl+1,ncol=nch-ncl+1;
	double **m;

	/* allocate pointers to rows */
	m=(double **) malloc((size_t)((nrow+NR_END)*sizeof(double*)));
	if (!m) printf("allocation failure 1 in matrix() \n");
	m += NR_END;
	m -= nrl;

	/* allocate rows and set pointers to them */
	m[nrl]=(double *) malloc((size_t)((nrow*ncol+NR_END)*sizeof(double)));
	if (!m[nrl]) printf("allocation failure 2 in matrix() \n");
	m[nrl] += NR_END;
	m[nrl] -= ncl;

	for(i=nrl+1;i<=nrh;i++) m[i]=m[i-1]+ncol;

	/* return pointer to array of pointers to rows */
	return m;
}


void free_vector(double *v, long nl, long nh)
/* free a double vector allocated with vector() */
{
	free((FREE_ARG) (v+nl-NR_END));
}




void svdcmp(double **a, int m, int n, double w[], double **v)
{
	int flag,i,its,j,jj,k,l,nm;
	double anorm,c,f,g,h,s,scale,x,y,z,*rv1;

	rv1=vector(1,n);
	g=scale=anorm=0.0;
	for (i=1;i<=n;i++) {
		l=i+1;
		rv1[i]=scale*g;
		g=s=scale=0.0;
		if (i <= m) {
			for (k=i;k<=m;k++) scale += fabs(a[k][i]);
			if (scale) {
				for (k=i;k<=m;k++) {
					a[k][i] /= scale;
					s += a[k][i]*a[k][i];
				}
				f=a[i][i];
				g = -SIGN(sqrt(s),f);
				h=f*g-s;
				a[i][i]=f-g;
				for (j=l;j<=n;j++) {
					for (s=0.0,k=i;k<=m;k++) s += a[k][i]*a[k][j];
					f=s/h;
					for (k=i;k<=m;k++) a[k][j] += f*a[k][i];
				}
				for (k=i;k<=m;k++) a[k][i] *= scale;
			}
		}
		w[i]=scale *g;
		g=s=scale=0.0;
		if (i <= m && i != n) {
			for (k=l;k<=n;k++) scale += fabs(a[i][k]);
			if (scale) {
				for (k=l;k<=n;k++) {
					a[i][k] /= scale;
					s += a[i][k]*a[i][k];
				}
				f=a[i][l];
				g = -SIGN(sqrt(s),f);
				h=f*g-s;
				a[i][l]=f-g;
				for (k=l;k<=n;k++) rv1[k]=a[i][k]/h;
				for (j=l;j<=m;j++) {
					for (s=0.0,k=l;k<=n;k++) s += a[j][k]*a[i][k];
					for (k=l;k<=n;k++) a[j][k] += s*rv1[k];
				}
				for (k=l;k<=n;k++) a[i][k] *= scale;
			}
		}
		anorm=FMAX(anorm,(fabs(w[i])+fabs(rv1[i])));
	}
	for (i=n;i>=1;i--) {
		if (i < n) {
			if (g) {
				for (j=l;j<=n;j++)
					v[j][i]=(a[i][j]/a[i][l])/g;
				for (j=l;j<=n;j++) {
					for (s=0.0,k=l;k<=n;k++) s += a[i][k]*v[k][j];
					for (k=l;k<=n;k++) v[k][j] += s*v[k][i];
				}
			}
			for (j=l;j<=n;j++) v[i][j]=v[j][i]=0.0;
		}
		v[i][i]=1.0;
		g=rv1[i];
		l=i;
	}
	for (i=IMIN(m,n);i>=1;i--) {
		l=i+1;
		g=w[i];
		for (j=l;j<=n;j++) a[i][j]=0.0;
		if (g) {
			g=1.0/g;
			for (j=l;j<=n;j++) {
				for (s=0.0,k=l;k<=m;k++) s += a[k][i]*a[k][j];
				f=(s/a[i][i])*g;
				for (k=i;k<=m;k++) a[k][j] += f*a[k][i];
			}
			for (j=i;j<=m;j++) a[j][i] *= g;
		} else for (j=i;j<=m;j++) a[j][i]=0.0;
		++a[i][i];
	}
	for (k=n;k>=1;k--) {
		for (its=1;its<=30;its++) {
			flag=1;
			for (l=k;l>=1;l--) {
				nm=l-1;
				if ((double)(fabs(rv1[l])+anorm) == anorm) {
					flag=0;
					break;
				}
				if ((double)(fabs(w[nm])+anorm) == anorm) break;
			}
			if (flag) {
				c=0.0;
				s=1.0;
				for (i=l;i<=k;i++) {
					f=s*rv1[i];
					rv1[i]=c*rv1[i];
					if ((double)(fabs(f)+anorm) == anorm) break;
					g=w[i];
					h=pythag(f,g);
					w[i]=h;
					h=1.0/h;
					c=g*h;
					s = -f*h;
					for (j=1;j<=m;j++) {
						y=a[j][nm];
						z=a[j][i];
						a[j][nm]=y*c+z*s;
						a[j][i]=z*c-y*s;
					}
				}
			}
			z=w[k];
			if (l == k) {
				if (z < 0.0) {
					w[k] = -z;
					for (j=1;j<=n;j++) v[j][k] = -v[j][k];
				}
				break;
			}
			if (its == 30) printf("no convergence in 30 svdcmp iterations\n");
			x=w[l];
			nm=k-1;
			y=w[nm];
			g=rv1[nm];
			h=rv1[k];
			f=((y-z)*(y+z)+(g-h)*(g+h))/(2.0*h*y);
			g=pythag(f,1.0);
			f=((x-z)*(x+z)+h*((y/(f+SIGN(g,f)))-h))/x;
			c=s=1.0;
			for (j=l;j<=nm;j++) {
				i=j+1;
				g=rv1[i];
				y=w[i];
				h=s*g;
				g=c*g;
				z=pythag(f,h);
				rv1[j]=z;
				c=f/z;
				s=h/z;
				f=x*c+g*s;
				g = g*c-x*s;
				h=y*s;
				y *= c;
				for (jj=1;jj<=n;jj++) {
					x=v[jj][j];
					z=v[jj][i];
					v[jj][j]=x*c+z*s;
					v[jj][i]=z*c-x*s;
				}
				z=pythag(f,h);
				w[j]=z;
				if (z) {
					z=1.0/z;
					c=f*z;
					s=h*z;
				}
				f=c*g+s*y;
				x=c*y-s*g;
				for (jj=1;jj<=m;jj++) {
					y=a[jj][j];
					z=a[jj][i];
					a[jj][j]=y*c+z*s;
					a[jj][i]=z*c-y*s;
				}
			}
			rv1[l]=0.0;
			rv1[k]=f;
			w[k]=x;
		}
	}
	free_vector(rv1,1,n);
}



int main(int argc, char **argv) {
  
  
  int m;  // # rows 
  int n;  // # columns

  m = atoi(argv[1]);  // get the number of rows of the matrix passed as argument
  n = atoi(argv[2]);   //get the number of columns of the matrix passed as argument
  
  int i,j;
  double **a = matrix(1,m,1, n); // =  (double **)malloc(m*n);
  for ( i=1; i<=m; i++)
   for (j=1; j<=n; j++)
     a[i][j] = rand();

  int r, c;

  double *w = vector(1,n); //*w =  (double *)malloc(m);
  double **v = matrix(1,n,1, m); //**v =  (double **)malloc(m*n);

//  printf("performing SVD\n");
 clock_t start, end;

 start = clock();


  svdcmp(a, m, n, w, v);
end = clock();

double tm = ((double)end- (double)start)/(double)CLOCKS_PER_SEC;
printf("The time to perform SVD for matrix size %d X %d  was: %lf secs\n",m, n,  tm); 
  
} 


```

`With Linux we can compile this file, named as e.g. ` **`svd1cmp`** ` with the gcc compiler, producing fully optimized code with `
```
gcc  -O3 svd1cmp.c  -lm -o svd
```

`The C based svd, can be executing with command line arguments the matrix sizes, e,g, `
```
./svd 200 200
```


`The corresponding ScalaLab scripts for performing the benches, are of course trivial, e.g.`

```


var x = rand(200,300)


tic
var xs = svd(x)
var tm = toc

```

`Then, here are some times, with Java runtime version: 1.7.0_25, and ScalaLab based on Scala 2.11 M4: `

| **Matrix Size**  | **Optimized C** | **`ScalaLab`** | **`Unoptimized C`** |
|:-----------------|:----------------|:---------------|:--------------------|
| 200X200          | 0.08            | 0.15           | 0.34                |
| 200X300          | 0.17            | 0.2            | 0.61                |
| 300X300          | 0.34            | 0.58           | 1.23                |
| 500X600          | 3.75            | 5.06           | 8.13                |
| 900X1000         | 35.4            | 51.3           |  53.3               |


`Clearly, ScalaLab is faster than unoptimized C and close even to optimized C code!`

`I tested also ` **`Microsoft Visual C compiler `** `on Windows 8, 64-bit. The results are very similar to the gcc case, i.e. ScalaLab is faster than unoptimized (i.e. the default) code produced by the ` **`cl`** ` compiler, but slower when cl compiles with full optimization. The differences are about the same as the table for the gcc compiler illustrates. `