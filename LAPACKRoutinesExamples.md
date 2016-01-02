# Introduction #

`The LAPACK library is a solid framework for linear algebra applications. LAPACK algorithms are available for Java programmers with the JLAPACK package available in the standard libraries of ScalaLab (with netlib). The Matrix Toolkit for Java project offers higher level interface to LAPACK functions. We present examples of using LAPACK from ScalaLab.`


# Examples #

## Eigendecomposition ##

```

// construct a 9X9 RichDoubleDoubleArray
var a = Rand(9)

// realEv:  real parts of the eigenvalues
// imEv: imaginary parts of the eigenvalues
// eigvs: left eigenvectors

var (realEv, imEv, eigvs)  = la_eig(a)

```


# Using JLAPACK directly #

`Using the MTJ object oriented wrapping to JLAPACK, we sacrifice much of the potential of JLAPACK for the benefits of easy of use and flexibility. In order to exploit directly the JLAPACK algorithms, we develop routines that operate directly on the ` _`RichDoubleDoubleArray`_ `type. `

`These are at an initial stage and much work have to be done, but here are some of these routines.`


```

 
 /*
  
  var a = AAD("1 0 0; 0 9.8 0; 0 0 8.9")
  
   var a= Rand(300, 300)
  tic
   var (ev1, ev2, ev3) = scalaSci.ILapack.eig(a)
   var tmEV=toc
   
    tic
   var (evv1, evv2) = scalaSci.ILapack.eigv(a)
   var tmEVv=toc

tic
var (evNumal, eigNumal) = eig(a)
var tmnumal = toc
*/ 

  // computes eigenvalues right eigenvectors also
  def eig(inM: Array[Array[Double]]) = {
    
    val n = inM.length

    // Allocate space for the decomposition
    var Wr = new DenseVector(n)
    var Wi = new DenseVector(n)

    var Vr = new DenseMatrix(n,n)

    // Find the needed workspace
    val worksize = Array.ofDim[Double](1);
    val info = new intW(0)
    LAPACK.getInstance.dgeev(
      "N",   // left eigenvectors of A are not computed
      "V",  // right eigenvectors of A are computed
      n,  // the order of the matrix, number of rows
      Array.empty[Double], 
      Math.max(1,n),  // leading dimension of the array
      Array.empty[Double],   // WR: real parts of the computed eigenvalues
      Array.empty[Double],  // WI: imaginary parts of the computed eigenvalues
      Array.empty[Double],   //  if JOBVL='V' the left eigenvectors u(j) are stored one after 
                                        // another in the columns of VL, in the same order as their eigenvalues
                                        // if JOBVL='N'm VL is not referenced'
      Math.max(1,n),     // the leading dimension of the array VL
      Array.empty[Double], 
      Math.max(1,n),
      worksize, 
      -1,  // a workspace query is assumed
      info)

    // Allocate the workspace
    val lwork: Int = 
      if (info.`val` != 0)
       Math.max(1,4*n);
    else
      Math.max(1,worksize(0).toInt);

    val work = Array.ofDim[Double](lwork);

    // Factor it!

    val A = new DenseMatrix(inM)
    LAPACK.getInstance.dgeev(
      "N",  // left eigenvectors of A are not computed
      "V",  // right eigenvectors of A are computed
      n,  // the order of the matrix, number of rows
      A.getData,   // (input/output) array, on entry the nXn matrix A, on exit A is overwritten
      Math.max(1,n), // leading dimension of the array
      Wr.getData,   // WR: real parts of the computed eigenvalues
      Wi.getData,   // WI: imaginary parts of the computed eigenvalues
      Array.empty[Double], 
      Math.max(1,n),
      Vr.getData, 
      Math.max(1,n),
      work, 
      work.length, info);

    if (info.`val` > 0)
      throw new NotConvergedException(NotConvergedException.Reason.Iterations)
    else if (info.`val` < 0)
      throw new IllegalArgumentException()

    // prepare the results
    var rWr = new RichDoubleArray(scalaSci.JILapack.denseVectorToDoubleArray(Wr))
    var rWi =  new RichDoubleArray(scalaSci.JILapack.denseVectorToDoubleArray(Wi))  
    var rVr =  new RichDoubleDoubleArray(scalaSci.JILapack.denseMatrixToDoubleArray(Vr))  
    (rWr, rWi, rVr)
  
  }
  
  
  // computes eigenvalues right eigenvectors also
  def eigv(inM: Array[Array[Double]]) = {
    
    val n = inM.length

    // Allocate space for the decomposition
    var Wr = new DenseVector(n)
    var Wi = new DenseVector(n)

    var Vr = new DenseMatrix(1,1)

    // Find the needed workspace
    val worksize = Array.ofDim[Double](1);
    val info = new intW(0)
    LAPACK.getInstance.dgeev(
      "V",   // left eigenvectors of A are not computed
      "V",  // right eigenvectors of A are computed
      n,  // the order of the matrix, number of rows
      Array.empty[Double], 
      Math.max(1,n),  // leading dimension of the array
      Array.empty[Double],   // WR: real parts of the computed eigenvalues
      Array.empty[Double],  // WI: imaginary parts of the computed eigenvalues
      Array.empty[Double],   //  if JOBVL='V' the left eigenvectors u(j) are stored one after 
                                        // another in the columns of VL, in the same order as their eigenvalues
                                        // if JOBVL='N'm VL is not referenced'
      Math.max(1,n),     // the leading dimension of the array VL
      Array.empty[Double], 
      Math.max(1,n),
      worksize, 
      -1,  // a workspace query is assumed
      info)

    // Allocate the workspace
    val lwork: Int = 
      if (info.`val` != 0)
       Math.max(1,4*n);
    else
      Math.max(1,worksize(0).toInt);

    val work = Array.ofDim[Double](lwork);

    // Factor it!

    val A = new DenseMatrix(inM)
    LAPACK.getInstance.dgeev(
      "N",  // left eigenvectors of A are not computed
      "N",  // right eigenvectors of A are computed
      n,  // the order of the matrix, number of rows
      A.getData,   // (input/output) array, on entry the nXn matrix A, on exit A is overwritten
      Math.max(1,n), // leading dimension of the array
      Wr.getData,   // WR: real parts of the computed eigenvalues
      Wi.getData,   // WI: imaginary parts of the computed eigenvalues
      Array.empty[Double], 
      Math.max(1,n),
      Vr.getData, 
      Math.max(1,n),
      work, 
      work.length, info);

    if (info.`val` > 0)
      throw new NotConvergedException(NotConvergedException.Reason.Iterations)
    else if (info.`val` < 0)
      throw new IllegalArgumentException()

    // prepare the results
    var rWr = new RichDoubleArray(scalaSci.JILapack.denseVectorToDoubleArray(Wr))
    var rWi =  new RichDoubleArray(scalaSci.JILapack.denseVectorToDoubleArray(Wi))  
      (rWr, rWi)
  }
  

  class NotConvergedException (val reason: NotConvergedException.Reason.Value, msg: String = "")
          extends Exception(msg) { }

  object NotConvergedException {
    object Reason extends Enumeration {
      val Iterations, Divergence, Breakdown = Value
    }
  }
  
  


/*
 import scalaSci.ILapack._
 
   var A = AAD("1 2 0 ; -1 0 -2; -3 -5 1 ")
   var b = AD("3 -5 -4")
   
   var x = LUSolve(A, b)
 */
// solves the system Ax = b
def  LUSolve(A: Array[Array[Double]],  b: Array[Double]) = {
   val nrows = A.length; val ncols = A(0).length
    var  Adm = new DenseMatrix(A)  // matrix for A
    
    val bc = new Array[Double](nrows)
    for (k<-0 until nrows)
      bc(k) = b(k)

    val  piv = new Array[Int](nrows)

    var info = new intW(0)
    val N = nrows  // the number of linear equations, i.e. the order of the matrix, N>=0
    val NRHS = 1 // the number of right hand sides, i.e. the number of columns of the matrix B, NRHS >= 0
    val LDA = nrows  // the leading dimension of A    
    val LDB = nrows  // the leading dimension of b
    
    LAPACK.getInstance().dgesv(N, NRHS, Adm.getData(), LDA, piv, bc, LDB, info)

    if (info.`val` > 0)
         println("factor U is exactly singular, so the solution could not be computed")
       else if (info.`val` < 0)
         println("argument has illegal value")
         
         
    bc
           
}


```


## SVD with LAPACK ##

`The following routine performs a Singular Value Decomposition (SVD) using LAPACK for the RichDoubleDoubleArray and Array[Array[Double]] ScalaLab types. `

```


  /**
   * Computes the SVD of a m by n 
   * Returns an m*m matrix U, a vector of singular values, and a n*n matrix V'
   */
  /*
   Based on dgesdd that computes thye singular value decomposition (SVD) of a m-by-n matrix  A, optionally computing the left
   and right singular vectors. If singular vectors are desired, it uses a divide and conquer algorithm.
   The SVD is written
       A = U*Σ*V^H
    where Σ is an m-by-n matrix which is zero except for its min(m, n) diagonal elements,
    U is an m-by-m orthogonal/unitary matrix, and V is an n-by-n orthogonal/unitary matrix.
    The diagonal elements of Σ, are the singular values of A; they are real and non-negative, and are returned in descending order.
    The first min(m, n) columns of U and V are the left and right singular vectors of A.
   */
  
  /*
   val A = AAD("1 0 1; 1 1 0")
   var (u, s, vt) = scalaSci.ILapack.svd(A)
   // prove that U and V are orthogonal
   val uorth = u *(u~)
   val vorth = vt*(vt~)
   
   
   */
def svd(A: Array[Array[Double]]) = {
    val M = A.length  // the number of rows of A 
    val N = A(0).length  // the number of columns  of A
    val  dataM =  new DenseMatrix(A)
    val UCOL = min(M, N)
    val LDA = max(1, M)  // the leading dimension of matrix A
    val S = new DenseVector(UCOL)  // output: the singular values of A, sorted so that S(i) >= S(i+1)
    val U =  new DenseMatrix(M, M)  // output: the M-by-M orthogonal/unitary matrix U
    val LDU = max(1, M)  // the leading dimension of matrix U
    val LDVT = max(1, N)   // the leading dimension of the array VT 
    val VT = new DenseMatrix(LDVT, N)  // output: the N-by-N unitary matrix V^T
    val  LWORK = 3*min(M, N) + min(M, N) +  max( max(M, N), 4 * min(M, N) * min(M, N) + 4 * min(M, N) )
    val  WORK = new Array[Double](LWORK)  
    val IWORK = new Array[Int](8*min(M, N))
    val INFO = new intW(0)
                   
    LAPACK.getInstance.dgesdd(
      "A", M, N,   dataM.getData,    LDA,  
      S.getData, U.getData, 
      LDU,  VT.getData, 
      LDVT, WORK, LWORK,
      IWORK, INFO)

    if (INFO.`val` > 0)
      throw new NotConvergedException(NotConvergedException.Reason.Iterations)
    else if (INFO.`val` < 0)
      throw new IllegalArgumentException()

    (U.toDoubleArray(), S.getData, VT.toDoubleArray())
  }


```