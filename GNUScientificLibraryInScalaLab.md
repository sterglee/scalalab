# Introduction #

`ScalaLab utilizes the project:`

https://github.com/bytedeco/javacpp-presets/tree/master/gsl

`in order to provide MATLAB-like access to the powerful GNU scientific library.`

`We provide some examples.`

## `Example 1` ##


```
  var  x = 5.6

  var y = gsl_sf_bessel_J1(x)  // use the GSL library

```

## `Example 2 ` ##
```

  var  lda = 3
  
  var A = Array[Float]( 0.11f, 0.12f, 0.13f, 0.21f, 0.22f, 0.23f )

  var  ldb = 2
  
  var B = Array[Float]( 1011, 1012, 1021, 1022,1031, 1032 )

  var ldc = 2

  var C = Array[Float]( 0.00f, 0.00f, 0.00f, 0.00f )

  /* Compute C = A B */

  cblas_sgemm (CblasRowMajor, 
               CblasNoTrans, CblasNoTrans, 2, 2, 3,
               1.0f, A, lda, B, ldb, 0.0f, C, ldc)

println("multiplication results, C(0,0) = "+C(0)+" C(0,1) = "+C(1)+" C(1, 0) = "+C(2)+", C(1, 1) = "+C(3))

```

## `Example 3` ##
```
  var  lda = 3
  
  var A = Array( 0.11, 0.12, 0.13, 0.21, 0.22, 0.23 )

  var  ldb = 2
  
  var B = Array[Double]( 1011, 1012, 1021, 1022,1031, 1032 )

  var ldc = 2

  var C = Array[Double]( 0.00, 0.00, 0.00, 0.00 )

  /* Compute C = A B */

  cblas_dgemm (CblasRowMajor, 
               CblasNoTrans, CblasNoTrans, 2, 2, 3,
               1.0, A, lda, B, ldb, 0.0, C, ldc)

println("multiplication results, C(0,0) = "+C(0)+" C(0,1) = "+C(1)+" C(1, 0) = "+C(2)+", C(1, 1) = "+C(3))
```

## `Example 4` ##
`Matrix Multiplication using CBLAS compared to the native ScalaLab multiplication.`

`ScalaLab here finishes much faster, since it combines Native BLAS with Java multithreading. `

```
  
  var M = 2200
  var N = 2400
  var K = 2500
  
  
  var A = Ones(M*N)
  var  lda = N
  
  var B = Ones(N*K)
  var  ldb = K
  
  var C = Ones(M*K)

  var ldc = K
  /* Compute C = A B */

tic
  cblas_dgemm (CblasRowMajor, 
               CblasNoTrans, CblasNoTrans, M, K, N,
               1.0, A, lda, B, ldb, 0.0, C, ldc)
var tm=toc   // time for CBLAS


// compare with native ScalaLab matrix multiplication
var Aj = Ones(M,N)
var Bj = Ones(N, K)

tic
var Cj = Aj*Bj
var tmj = toc

```

## `Example 5 - Discrete Wavelet Transform (DWT) using GSL` ##

```
 var N = 256
 var nc = 20
 var data = new Array[Double](N)
 
  var w = gsl_wavelet_alloc (gsl_wavelet_daubechies, 4)
  var work = gsl_wavelet_workspace_alloc (N)

var k=0
while (k<N) {
    data(k)= sin(0.78*k)
    k+=1
    }
    
    figure(1); subplot(2,1,1); plot(data, "Original Data")
    
  gsl_wavelet_transform_forward (w, data, 1, N, work)
 
 subplot(2,1,2); plot(data, "Wavelet Transformed Data")

```


## `Multiplication using GSL matrices ` ##

```

import org.bytedeco.javacpp.DoublePointer

// dimensions of matrices
var  M = 2000
var N = 1900
var K = 2100

// create sample arrays of 1s as matrices
  var  a = Ones(M*N)
  var  xa = new DoublePointer(a: _*)

  var  b = Ones(N*K)
  var  xb = new DoublePointer(b: _*)

  var c = Ones(M*K)
  var xc = new DoublePointer(c: _*)
  
  
  var A = gsl_matrix_view_array(xa, M, N)
  var B = gsl_matrix_view_array(xb, N, K)
  var C = gsl_matrix_view_array(xc, M, K)

  /* Compute C = A B */

  tic 
  gsl_blas_dgemm (CblasNoTrans, CblasNoTrans,
                  1.0, A.matrix, B.matrix,
                  0.0, C.matrix)
  var tmGLS = toc                
      
      // get now the matrix
      
   var Cs = new RichDouble2DArray(M, K)
   
      for (rows <- 0 until M )
       for (cols <-  0 until  K)
          Cs(rows, cols) =  C.matrix.data.get(rows*K+cols)
          
          
```


## `Eigenvalue computation ` ##

```

import org.bytedeco.javacpp.DoublePointer

// dimensions of matrices
var  M = 20

// create sample arrays of 1s as matrices
  var  a = Rand(M*M)

  var  xa = new DoublePointer(a: _*)
  
  
  var m = gsl_matrix_view_array(xa, M, M)
  
  var eval = gsl_vector_complex_alloc(M)
  var evec = gsl_matrix_complex_alloc(M, M)
  
  var w = gsl_eigen_nonsymmv_alloc(M)
  
  gsl_eigen_nonsymmv(m.matrix, eval, evec, w)
  
  gsl_eigen_nonsymmv_free(w)
  
  gsl_eigen_nonsymmv_sort(eval, evec, GSL_EIGEN_SORT_ABS_DESC)
  
  var evals = new RDDA(M, 2)
  var evecsReal = new RDDA(M, M)
  var evecsImag = new RDDA(M, M)
  
  for (i<-0 until M) {
      var eval_i = gsl_vector_complex_get( eval, i)
      
      var evec_i = gsl_matrix_complex_column(evec, i)
      
      // get computed eigenvalues i
      evals(i, 0) = eval_i.dat.get(0)
      evals(i, 1) = eval_i.dat.get(1)
      
      // get computed eigenvector i
      for (j<- 0 until M) {
          var z = gsl_vector_complex_get(evec_i.vector, j)
          evecsReal(i, j) = z.dat.get(0)
          evecsImag(i, j) = z.dat.get(1)
          }
        }
          
  
        gsl_vector_complex_free(eval)
        gsl_matrix_complex_free(evec)
        
        
      
       

```


> ## `Splines example` ##

```

val  N = 200

// number of fit coefficients 
val NCOEFFS = 12

// nbreak = ncoeffs + 2 - k = ncoeffs - 2 since k = 4 
val  NBREAK =  (NCOEFFS - 2)

 val n = N
 val ncoeffs = NCOEFFS
 val nbreak = NBREAK
  
  gsl_rng_env_setup()
  var r = gsl_rng_alloc(gsl_rng_default)

  // allocate a cubic bspline workspace (k = 4) 
  var bw = gsl_bspline_alloc(4, nbreak)
  var B = gsl_vector_alloc(ncoeffs)

  var x = gsl_vector_alloc(n)
  var y = gsl_vector_alloc(n)
  var X = gsl_matrix_alloc(n, ncoeffs)
  var c = gsl_vector_alloc(ncoeffs)
  var w = gsl_vector_alloc(n)
  var cov = gsl_matrix_alloc(ncoeffs, ncoeffs)
  var mw = gsl_multifit_linear_alloc(n, ncoeffs)

  println("#m=0,S=0\n")
  /* this is the data to be fitted */
  for (i <- 0 until n)
    {
      var  xi = (15.0 / (N - 1)) * i
      var  yi = cos(xi) * exp(-0.1 * xi)

      var sigma = 0.1 * yi
      var dy = gsl_ran_gaussian(r, sigma)
      yi += dy

      gsl_vector_set(x, i, xi)
      gsl_vector_set(y, i, yi)
      gsl_vector_set(w, i, 1.0 / (sigma * sigma))

      println(" xi = "+xi +", yi = "+yi)
    }

  // use uniform breakpoints on [0, 15] 
  gsl_bspline_knots_uniform(0.0, 15.0, bw)

  // construct the fit matrix X 
  for (i <- 0 until n)
    {
      var  xi = gsl_vector_get(x, i)

      // compute B_j(xi) for all j 
      gsl_bspline_eval(xi, B, bw)

      // fill in row i of X 
      for (j <- 0 until  ncoeffs)
        {
          var Bj = gsl_vector_get(B, j)
          gsl_matrix_set(X, i, j, Bj)
        }
    }

  var chisq = new Array[Double](1)
  var yi = new Array[Double](1)
  var yerr = new Array[Double](1)
  // do the fit 
  gsl_multifit_wlinear(X, w, y, c, cov, chisq, mw)


  var dof = n - ncoeffs
  var tss = gsl_stats_wtss(w.data, 1, y.data, 1, y.size)
  var Rsq = 1.0 - chisq / tss

  println("chisq/dof = "+ (chisq/dof) + " Rsq = "+Rsq)

  // output the smoothed curve 
    var xi = 0.0
    var lxi = List[Double]()
    var lyi = List[Double]()
    while (xi < 15.0){
        gsl_bspline_eval(xi, B, bw)
        gsl_multifit_linear_est(B, c, cov, yi, yerr)
        lxi = xi :: lxi
        lyi = yi(0) :: lyi
        xi += 0.1
        }
        
        var valsx = lxi.toArray
        var valsy = lyi.toArray
        plot(valsx, valsy)
        

  gsl_rng_free(r)
  gsl_bspline_free(bw)
  gsl_vector_free(B)
  gsl_vector_free(x)
  gsl_vector_free(y)
  gsl_matrix_free(X)
  gsl_vector_free(c)
  gsl_vector_free(w)
  gsl_matrix_free(cov)
  gsl_multifit_linear_free(mw)

```


## `FFT example` ##

```

  
  var  data = new Array[Double](2*128)
    
    
  for (i <- 0 until 128)
    {
        data(2*i) = 0.0 // REAL part
        data(2*i+1) = 0.0 // IMAG part
    }

  data(0) = 1.0

  for (i <- 1 until 10)
    {
        data(2*i) = 1.0  // REAL part
        data(2*(128-i)) = 1.0 // REAL part
    }

  
  gsl_fft_complex_radix2_forward (data, 1, 128)

  for (i <- 0 until  128)
    {
      println ("REAL( "+i+") = "+data(2*i)/sqrt(128)+", IMAG( "+i+" ) = "+ data(2*i+1)/sqrt(128))
    }


plot(data)

```


## `Eigenvalue computation using GSL for RichDouble2DArray ScalaLab type` ##
```

var N=300
var x = rand(N,N)

tic

var xgsl = x.gsleig  // compute eigenvalues using GSL
var tmgsl = toc


tic
var xjava = x.eig  // compute eigenvalues using Java
var tmjava = toc


```

## `Using an EJML matrix` ##
`EJML matrices use an one-dimensional row major array, as GSL does. Thus, we can have slighter less overhead when interfacing EJML with the GSL library, rather than a library that uses two-dimensional array format. `

```

var n = 300

var x = scalaSci.EJML.StaticMathsEJML.rand0(n, n)    // should be an EJML matrix irrespectively of the current library settings

tic
var xgsleigs = x.gsleig
var tmgsl=toc

tic
var xjavaeigs = x.eig
var tmjava = toc
```




## `Solving a linear system using LU-decomposition` ##

```

  import org.bytedeco.javacpp.gsl._
  import org.bytedeco.javacpp.DoublePointer

  var a = Array( 0.18, 0.60, 0.57, 0.96,
                      0.41, 0.24, 0.99, 0.58,
                      0.14, 0.30, 0.97, 0.66,
                      0.51, 0.13, 0.19, 0.85 )
                      
  var a_data = new DoublePointer(a: _*)
                        

  var b = Array( 1.0, 2.0, 3.0, 4.0 )
  var b_data = new DoublePointer(b: _*)
  
  var mv  = gsl_matrix_view_array (a_data, 4, 4)

  var bv= gsl_vector_view_array (b_data, 4)

  var x = gsl_vector_alloc (4)
  
  var p = gsl_permutation_alloc (4)

  var s = new Array[Int](1)
  gsl_linalg_LU_decomp (mv.matrix, p, s)
  
  gsl_linalg_LU_solve (mv.matrix, p, bv.vector, x)

  var xv = new Array[Double](4)
  for (k<-0 until 4)
     xv(k) = x.data.get(k)  

  gsl_permutation_free (p);
  gsl_vector_free (x);

```


## `GSL LU-solver applied to RichDouble2DArray ` ##

```

var N = 1000

var A = rand(N,N)

var b = vrand(N).getv

// solve the system with GSL LU solver
tic
var x = A.gsllusolve(b)
var tmsolveGSLLU = toc

// solve the system with Java
tic
var x2 = A.solve(b)
var tmsolveJava = toc

max(max(abs(x2-x)))  // should be zero or a very small number



```

## `Solving linear systems with QR decomposition` ##

```

var N = 1500

var A = rand(N,N)

var b = vrand(N).getv

// solve the system with GSL QR solver
tic
var xqr = A.gslqrsolve(b)
var tmsolvegslQR = toc


// solve the system with GSL LU solver
tic
var xlu = A.gsllusolve(b)
var tmsolvegslLU = toc


// solve the system with Java
tic
var x2 = A.solve(b)
var tmsolveJava = toc

 

```

## `Singular Valued Decomposition` ##

```
var N= 1500

var A = rand(N,N)

tic
var Asvd = gslsvd(A)
var  tmgslsvd = toc

var U = Asvd._1
var S = diag(Asvd._2)
var V = Asvd._3

var shouldBeZero = U*S*(V~)-A
max(max(shouldBeZero))

tic
var Asvdj = svd(A)
var tmsvdjava=toc

// use CCMath library
tic
var Accsvd = ccsvd(A)
var tmccsvd = toc



```

## `Solution of a system with SVD` ##

```

var N=1000

var A = rand(N,N)

var b = vrand(N).getv

tic
var x = A.gslsvdsolve(b)
var tmgslsvd = toc

tic
var xj = A.solve(b)
var tmjava = toc


A*x-b 

```


## `Matrix inversion with LU decomposition` ##

```
// test matrix inversion
var N=2000
var x = rand(N,N)

tic
var xi = x.gslluinvert
var tmgsl = toc

tic
var xij = inv(x)
var tmj = toc

xi-xij
x*xi


```

