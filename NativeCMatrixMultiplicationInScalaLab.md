# `JNI based native matrix multiplication` #

`Matrix multiplication is a common operation that it is important to run as fast as possible. Although NVIDIA's CUDA offers the potential for huge increase of the matrix multiplication speed (e.g. many hundreds times faster) it requires a fast NVIDIA card and the installation of the CUDA framework.`

`Although Java multiplies matrices fast enough, we can do with careful use of the JNI (Java Native Interface) a little better. We pass the matrices as 1-D arrays to the following C routine:`

```

extern "C"
JNIEXPORT void JNICALL Java_NROps_NROps_mul 
 (JNIEnv *env, jobject obj, jdoubleArray a, jint n1, jint n2, 
	jdoubleArray b, jint n3, jdoubleArray c) {
	double *aa = (double *)env->GetDoubleArrayElements( a, NULL);
	double *bb = (double *)env->GetDoubleArrayElements( b, NULL);
	double *cc = (double *)env->GetDoubleArrayElements( c, NULL);
		
	register double *pA;
	register double *pB;
	register double *pC;
	register double smrowcol;
  /*  
  for (int i= 0;  i< n1;  i++) {
    for (int j = 0;  j < n3;  j++) {
	   double smrowcol = 0;
            
            for (int k = 0;  k <  n2; k++) { 
			  smrowcol += aa[i*n2+k]  * bb[k*n3+j];
			}
			cc[i *n3 + j ] = smrowcol;
	   }
     }
  */
  
  pC = cc;
  for (int i=0; i<n1; i++) {
 for (int j=0; j<n3; j++) {

	  smrowcol = 0;
	  pA = aa + i*n2;
	  pB = bb+j;
	  
	  for (int k=0; k<n2; k++) {
		smrowcol += *pA * *pB;
		pA++;
		//pB += n3;
		pB++;
		}
		*pC++ = smrowcol;
	}
  }
   
	env->ReleaseDoubleArrayElements(a, aa, 0);
	env->ReleaseDoubleArrayElements( b, bb, 0);
	env->ReleaseDoubleArrayElements(c, cc, 0);
	
	
}

```

`The important point at this optimized C routine is that matrix B should be passed transposed. Otherwise, pointer ` _`pB`_ ` accesses distantly spaced elements in the array for large matrices, and therefore the CPU caches are not helpful. We observed a speedup of more than 10 times, by simply passing matrix B transposed and using ` _`pB++`_ `instead of` _`pB += n3`_ ` to access the next element of matrix B. `

`The operator` **`cc`** ` multiples ` _`RichDouble2DArray`_ ` objects using the C optimized multiplication. The following script presents an example.`

```

var N = 2110
var M = 2165
var K = 2204
var x = ones(N, M)
var y = ones(M, K)
var x0 = ones0(N, M)
var y0 = ones0(M, K) 

// multiply using optimized C routine
tic
var xy = x cc y
var tm= toc


// multiply using Java
tic
var xy0 = x0 * y0
var tm0Java = toc


```

`On a Linux based i7 machine I obtained: `
```
Native Multiplication time: 9.164598568 sec
Java Multiplication time: 13.812656643 sec
```

`On a Windows64 based i2 machine the corresponding times are: `
```
Native Multiplication time: 13.22 sec
Java Multiplication time: 21.12 sec
```

`Therefore optimized C code can improve the speed of matrix multiplication. `

`We also implemented native C multiplication for the EJML library, that since it can store the matrix in a row-major one dimensional double array, fits better with the handling of matrix with the JNI routines. The routine ` **`cc`** `again performs native C multiplication. The performance results however, do not differ significantly from those presented above. We illustrate an example. `

```


var N = 2000
var M = 2200
var K = 2400

var xx = ones0(N, M)
var yy = ones0(M, K)

// native C based multiplication applied on the internal EJML one-dimensional
// matrix storage
tic
var zz = xx cc yy
var tmC = toc

// the "normal" Java based EJML multiplication
tic
var zzj = xx * yy
var tmJ = toc

```

`The results on a Linux 64 i7 machine are: `
```
tmC = 9.520930121 sec
tmJ =  14.535624897 sec
```

`Therefore, optimized C matrix implementation performs a little better than Java, but the difference is not significant.`
