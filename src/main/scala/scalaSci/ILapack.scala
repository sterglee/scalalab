
package scalaSci

import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.DenseVector
import no.uib.cipr.matrix.EVD
import no.uib.cipr.matrix.NotConvergedException
import org.netlib.util.intW
import org.netlib.lapack.LAPACK
import java.lang.Math._
import no.uib.cipr.matrix.LQ
import no.uib.cipr.matrix.LowerTriangDenseMatrix

// provides operations using LAPACK for Array[Array[Double]] or  RichDouble2DArray 
// thse operations supplement the MTJ interface with the classes  scalaSci.MTJ.Mat, scalaSci.MTJ.MTJBandMat
object ILapack {

def ld(n: Int)  =  Math.max(1, n)  // the "leading dimension""

  // the pivot indices, ia row i of the matrix was interchanged with row lpiv(i)
  /* e.g.  
   
   // interchange row 0 with 2
    var lpiv = Array(2, 1, 2)

    // construct permutation matrix
   var P = scalaSci.ILapack.permMatrixFromIndex( lpiv )

    // take an example matrix
  var A = AAD(" 1 2 3; 4 5 6; 7 8 9")

   //  permute it
  var PA = P*A
  
 */
 def permMatrixFromIndex( lpiv: Array[Int]) =  {
   val  N = lpiv.length
   val P = Array.ofDim[Double](N, N)  // the permutation matrix
   var r = 0
   while  (r < N) {
     P(r)(r) = 1.0
     r += 1
   }
   r = 0
   while (r < N) {
     if (r != lpiv(r))   // interchange rows
       interchange(r, lpiv(r), P)
   r += 1
   }
        P
   
 }
 
  def interchange(r1: Int, r2: Int, P: Array[Array[Double]]  ) {
    var N = P(0).length
    var buffer = new Array[Double](N)
    var c = 0
    while  (c < N) {  //  buffer row r1
      buffer(c) = P(r1)(c)
      c += 1
    }
    c = 0
    while (c < N)  { // transfer r2 to r1
      P(r1)(c) = P(r2)(c)
      c += 1
    }
    c = 0
    while  (c < N)  { // transfer r1 to r2
      P(r2)(c) = buffer(c)
      c += 1
   }
  }
  
  /*
  var  A = AAD("3 0 1; 0 -3 0; 1 0 3")
  var  (realEvs, imEvs, realEvecs, imEvecs) = Eig(A)
   */
  // compute eigenvalues using LAPACK and return them as tuple (realEvs, imEvs, leftEvecs, rightEvecs)
def  Eig(inM: Array[Array[Double]]) =   {
             val  DM = new  DenseMatrix(inM)
             val evdObj = no.uib.cipr.matrix.EVD.factorize(DM)
          
            val realEvs = evdObj.getRealEigenvalues
            val imEvs = evdObj.getImaginaryEigenvalues
            val realEvecs = evdObj.getLeftEigenvectors().toDoubleArray
            val imEvecs = evdObj.getRightEigenvectors().toDoubleArray
    
      (realEvs, imEvs, realEvecs, imEvecs)
 }
 
/*
 var  A = AAD("3 0 1; 0 -3 0; 1 0 3")
 var  eigresults = MTJEig(A)
 var reivs = eigresults.realEvs  // real parts  of the    eigenvalues
 var ieivs = eigresults.imEvs // imag parts of the eigenvalues
 var lefteigvecs = eigresults.realEvecs   // real eigenvectors
 var imeigvecs = eigresults.imEvecs  // im eigenvectors
 */
      
     // compute eigenvalues using LAPACK and return results with an EigResults structure
def MTJEig(inM: Array[Array[Double]]) =    {
             var DM = new  DenseMatrix(inM)
             var evdObj:EVD = null
        try {
            evdObj = no.uib.cipr.matrix.EVD.factorize(DM);
        } catch  {
          case ex: NotConvergedException  =>     println("NotCovergedException in MTJEig")
          case ex: Exception => println("general exception  in MTJEig")  
        }
          
             var  er = new EigResults()
             er.realEvs  = evdObj.getRealEigenvalues()
             er.imEvs = evdObj.getImaginaryEigenvalues()
             er.realEvecs = denseMatrixToDoubleArray(evdObj.getLeftEigenvectors())
             er.imEvecs = denseMatrixToDoubleArray(evdObj.getRightEigenvectors())
    
             er    
}

  /*
  
  var a = AAD("1 1 3.4; 0 9.8 0.4; 0.45 -0.78 8.9")
  var (rev,  iev, revecs) = scalaSci.ILapack.eigr(a)
   
*/ 

  // computes eigenvalues and right eigenvectors also
  // this routine does not use the MTJ factorize() but instead prepares the LAPACK call by itself
  // returns: (real parts of the computed eigenvalues. imag parts of the computed eigenvectors, matrix of eigenvectors)
  def eigr(inM: Array[Array[Double]]) = {
    
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
    var rWr = new RichDouble1DArray(scalaSci.ILapack.denseVectorToDoubleArray(Wr))
    var rWi =  new RichDouble1DArray(scalaSci.ILapack.denseVectorToDoubleArray(Wi))  
    var rVr =  new RichDouble2DArray(scalaSci.ILapack.denseMatrixToDoubleArray(Vr))  
    (rWr, rWi, rVr)
  
  }
  
  
  // computes eigenvalues and left eigenvectors also
  // returns: (rWr, rWi, lEigenVecs) i.e. real parts of the right eigenvalues, imag parts of the right eigenvalues, left eigenvectors
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
      "V",   // left eigenvectors of A are  computed
      "N",  // right eigenvectors of A are not computed
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
      "V",  // left eigenvectors of A are computed
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
    var rWr = new RichDouble1DArray(scalaSci.ILapack.denseVectorToDoubleArray(Wr))
    var rWi =  new RichDouble1DArray(scalaSci.ILapack.denseVectorToDoubleArray(Wi))  
    var lEigenVecs = new RichDouble2DArray(scalaSci.ILapack.denseMatrixToDoubleArray(Vr))
      (rWr, rWi, lEigenVecs)
  }
  
  
  // convert an MTJ LowerTriagDenseMatrix to an Array[Array[Double]]
def  lowerTriangDenseMatrixToDoubleArray( ltdm: LowerTriangDenseMatrix  ) = {
  val nrows = ltdm.numRows()
  val ncols = ltdm.numColumns()
  val dres = Array.ofDim[Double](nrows, ncols)
  var r = 0
  while (r < nrows)  {
      var c = 0
      while  (c <  ncols) {
          dres(r)(c) = ltdm.get(r, c)
          c += 1
      }
  r += 1
      }
  dres
          
}

    
/* var N = 1500
var x = new no.uib.cipr.matrix.DenseMatrix(N, N)

tic
var y = scalaSci.ILapack.denseMatrixToDoubleArray(x)
for (k<-0 until 10)
 y = scalaSci.ILapack.denseMatrixToDoubleArray(x)
var tm = toc
// tm = 0.483 with for, 0.374 with while
    */
    
  // convert an MTJ DenseMatrix to an Array[Array[Double]]
def  denseMatrixToDoubleArray(dm: DenseMatrix)  = {
  val nrows = dm.numRows()
  val ncols = dm.numColumns()
  val dres = Array.ofDim[Double](nrows, ncols)
  var r = 0
  while  (r < nrows)  {
      var c = 0
        while  (c <  ncols) {
          dres(r)(c) = dm.get(r, c)
          c += 1
        }
  r += 1
  }
  dres
         
}

  // convert an MTJ DenseVector to an Array[Double]
def  denseVectorToDoubleArray(dv: DenseVector) =  {
  val  vlen = dv.size()
  val  vres = new Array[Double](vlen)
  var v = 0
  while  ( v <  vlen )  {
      vres(v) = dv.get(v)
      v += 1
  }
  
   vres
          
}

 


/*
 import scalaSci.ILapack._
 
   var A = AAD("1 2 0 ; -1 0 -2; -3 -5 1 ")
   var b = AD("3 -5 -4")
   
   var x = LUSolve(A, b)
 */
// solves the system Ax = b
def  LUSolve(A: Array[Array[Double]],  b: RichDouble1DArray) = {
   val nrows = A.length; val ncols = A(0).length
    var  Adm = new DenseMatrix(A)  // matrix for A
    
    val bc = new Array[Double](nrows)
    var k = 0
    while  (k< nrows) {
      bc(k) = b(k)
      k += 1
    }

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
         
         
    new RichDouble1DArray(bc)
           
}
  
  /**
   * Computes the SVD of a m by n 
   * Returns an m*m matrix U, a vector of singular values, and a n*n matrix V'
   */
  /*
   Based on dgesdd that computes thye singular value decomposition (SVD) of a m-by-n matrix  A, optionally computing the left
   and right singular vectors. If singular vectors are desired, it uses a divide and conquer algorithm.
   The SVD is written
       A = U*Ξ£*V^H
    where Ξ£ is an m-by-n matrix which is zero except for its min(m, n) diagonal elements,
    U is an m-by-m orthogonal/unitary matrix, and V is an n-by-n orthogonal/unitary matrix.
    The diagonal elements of Ξ£, are the singular values of A; they are real and non-negative, and are returned in descending order.
    The first min(m, n) columns of U and V are the left and right singular vectors of A.
   */
  
  /*
   val A = AAD("1 0 1; 1 1 0")
   var (u, s, vt) = scalaSci.ILapack.svd(A)
   // prove that U and V are orthogonal
   val uorth = u *(u~)
   val vorth = vt*(vt~)
   var approxM = (u~)*diag(s)*(vt) - A  // approxM should be zero
   
   
   */
/*def svd(A: Array[Array[Double]]) = {
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

  
  */
def svd(X: Array[Array[Double]]) = {
  val svdM = new Jama.JamaSingularValueDecomposition(new Jama.jMatrix(X))
  val U = svdM.getU.getArray
  val V = svdM.getV.getArray
  val s = svdM.getSingularValues
  val Nrows = s.length
  val S = new Array[Double](Nrows)
  for (k<-0 until Nrows)
     S(k) = s(k)
  (U, S, V)
}
  
/*
 var A = AAD("9.2 -4.3 0.3; -2.3 0.5 0.2; -1.2 2.3 2.6")
 var lq = LQ(A)
 var lower = new RichDouble2DArray(lq.L)
 var qortho = new RichDouble2DArray(lq.Q)
 var isOrthogonal = (qortho~)*qortho- eye(3)
 
 var residual = lower*qortho-A
 var successOfLQ = approxZero(residual)     // should be true if successful decomposition
 */
def  LQ (inM: Array[Array[Double]]) = {
  val  DM = new DenseMatrix(inM)
  val  LQDecomp =  no.uib.cipr.matrix.LQ.factorize(DM)
  val   lq = new LQResults()
  
  val  ltdm = LQDecomp.getL()

  lq.Q = denseMatrixToDoubleArray(LQDecomp.getQ())
  lq.L = lowerTriangDenseMatrixToDoubleArray(LQDecomp.getL())
  
   lq
   
}

  
  /*
 var A = AAD("9.2 -4.3 0.3; -2.3 0.5 0.2; -1.2 2.3 2.6")
 var lu = scalaSci.ILapack.LU(A)
 var lower = new RichDouble2DArray(lu.L)
 var upper = new RichDouble2DArray(lu.U)
 var pivots = lu.Pi
 var P = permMatrixFromIndex(pivots)
 
 var product = P*upper*permutation
 
 var residual = product-A
 var successOfLQ = approxZero(residual)     // should be true if successful decomposition
 */

def  LU(inM: Array[Array[Double]]) =  {

    val  DM = new DenseMatrix(inM)
    val  DMc = DM.copy()
    val numRows = inM.length
    val  numCols = inM(0).length
    // the pivot indices, row i of the matrix was interchanged with row lpiv(i)
    val  lpiv = new Array[Int](Math.min(numRows, numCols))
     // (output) 0 successful exit, < 0 if -i, the ith argument had an illegal value, 
     //  > 0 if i U(i, i) is exactly zero. The factorization has been completed, but the factor U is exactly 
     // singular, and division by zero will occur if it is used a system of equations
    val   info = new intW(0)
    LAPACK.getInstance().dgetrf(numRows, numCols, DMc.getData(),  ld(numRows), lpiv,  info)

     val rLU = new LUResults()
     rLU.L = Array.ofDim[Double](numRows, numCols)
     rLU.U = Array.ofDim[Double](numRows, numCols)
     val minMN = if (numRows > numCols) numCols else numRows
     val ddmc = DMc.toDoubleArray
    var r = 0 
    while (r< numRows)  {
      lpiv(r) = lpiv(r)-1  // convert to zero-indexed array
      r += 1
    }
    r = 0
     while (r < numRows) {
      var c = 0 
      while (c < numCols) {
         if (r>c)
           rLU.L(r)(c)  = ddmc(r)(c)
         else
           rLU.U(r)(c) = ddmc(r)(c)
         rLU.L(r)(r) = 1.0  
       c += 1 
      }
       r += 1
     }
         rLU.Pi = lpiv
   
   
    if (info.`val`!= 0) 
        println("illegal LU with code:  "+info.`val`)
   
    rLU
  }

  
// invert the double [][] array using JLAPACK

   /* e.g.
 
var N = 500
 var a = Rand(N,N)
 tic
 var ai = invLapack(a)
 var tmlapack = toc
 ai*a
 
 // invert  with  EJML
 tic
 var aei = scalaSci.EJML.StaticMathsEJML.inv(a)
 var tmejml = toc
 
  */

  def  invLapack(inM: Array[Array[Double]]) = {
    val  DM = new DenseMatrix(inM)
    val  DMc = DM.copy()
    val  numRows = inM.length
    val  numCols = inM(0).length
    if (numRows != numCols) {
        println("invLapack() called for non-square matrix")
        inM
    }
    
    val  lpiv = new Array[Int](Math.min(numRows, numCols))
    val info = new intW(0)
    LAPACK.getInstance().dgetrf(numRows, numCols, DMc.getData(),  ld(numRows), lpiv,  info)

    
    var workSize = -1  // issue workspace query
    var work = new Array[Double](2)
    LAPACK.getInstance().dgetri(numRows, DMc.getData(), ld(numRows), lpiv, work, workSize, info)
    workSize =  work(0).asInstanceOf[Int]   // take the computed optimal workspace size
    
    work = new Array[Double](workSize)
    LAPACK.getInstance().dgetri(numRows, DMc.getData(), ld(numRows), lpiv, work, workSize, info)
    
    
    if (info.`val` != 0) 
        println("illegal invLapack with code:  "+info.`val`)
    
     DMc.toDoubleArray  // return the inverse matrix
 }

  
  
  
/*
 simpler interface to LAPACK DGELS routine
solves overdetermined/underdetermined linear systems involving an m-by-n matrix A
using a QR or LQ factorization of A. It is assumed that A has full rank.
The followiiiiig options are provided:
1.     if  m>=n: find the least squares solution of an overdetermined system, i.e., solve the least squares problem
 minimize || b-A*x ||_{2}
2.    if m < n:  find the minimum norm solution of an undetermined system A*x = B
Several right hand side vectors b and solution vectors x can be handled in a single call;
they are stored as the columns of the m-by-nrhs right hand side matrix B and the n-by-nrhs solution matrix X

   Example 1:. for underdetermined system

   var A = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6")
   var B = AAD("4.5; 3.4")
   var x = scalaSci.ILapack.DGELS(A, B)
   var residual = A*x-B
   
We can use muliple columns for B:
   var A = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6")
   var B = AAD("4.5 2.3; 3.4 6.7")
   var x = scalaSci.ILapack.DGELS(A, B)
   var residual = A*x-B
   
   Example 2: for overdetermined system
    var A = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6; 2.3 5.6 6.2; 2.3 -0.2 -0.3")
   var B = AAD("4.5; 3.4; -0.3; 0.6")
   var x = scalaSci.ILapack.DGELS(A, B)
   var residual = A*x-B
   
   
*/
  
def  DGELS(A: Array[Array[Double]], B: Array[Array[Double]]) = {
    val  dA = new DenseMatrix(A)
    val  dB = new DenseMatrix(B)

    val  dAc = dA.copy()
    var dBc = new DenseMatrix(1,1)
    
    var M = A.length   // number of rows of matrix A
    var N = A(0).length  // number of columns of matrix A
    var  NRHS = B(0).length;  // number of right hand sides, i.e., the number of columns of the matrices B and X
    
    var  solVecLength = N
    if  (M < N)  {  // undetermined problem
       dBc  = new DenseMatrix(N, NRHS)
       var r = 0
       while  ( r < M)  {
         var c = 0
         while ( c < NRHS)  {
               dBc.set(r, c, B(r)(c))
               c += 1
           }
         r += 1
           }
        }
    else  // overdetermined problem
        dBc = dB.copy()
    
    // each column of resultsMatrix will have a solution vector
    var  resultsMatrix = Array.ofDim[Double](solVecLength, NRHS)
    
    
    var info = new intW(0)
    
    var  workSize = -1 // issue workspace query
    var  work = new Array[Double](2)
    
    var  LDA = ld(M)  // leading dimension of A
    var  LDB = Math.max(M, N)  // since LDB >= max(1, M, N)
    
    LAPACK.getInstance().dgels("N", M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB, work, workSize, info)
    workSize =  work(0).asInstanceOf[Int]  // take the computed optimal workspace size
    
    //println("returned worksize = "+workSize)
    
    work = new Array[Double](workSize)
    LAPACK.getInstance().dgels("N", M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB, work, workSize, info)

    if (info.`val` != 0)  {
        println("illegal DGELS with code:  "+info.`val`)
        resultsMatrix
    }
    
    // copy the results to the resultsMatrix
    var c = 0
    while (c < NRHS)  {  // for all RHS
      var v = 0
      while (v < solVecLength) {  // for all variables of the system
            resultsMatrix(v)(c) = dBc.get(v, c)
            v += 1
      }
      c += 1
    }
    
     resultsMatrix
  }

  
  
/* computes the minimum norm solution to a real linear least squares problem
       minimize || b - A * x || _2
    using the singular value decomposition (SVD) of A.
    A is an m-by-n matrix which may be rank-deficient.
	Several right hand side vectors b and solution vectors x can be handled 
	in a single call; they are stored as columns of the m-by-nrhs right hand side matrix B
	and the n-by-nrhs solution matrix X
Example:

   var A = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6")
   var B = AAD("4.5; 3.4")
   var x = scalaSci.ILapack.DGELD(A, B)
   var x_sol = x._1  // the solution
   var x_S = x._2 // the singular values of A in decreasing order
   var x_rank = x._3.`val` // effective rank of  A.
 
   
   

 */  

def  DGELD(A: Array[Array[Double]], B: Array[Array[Double]]) = {
    	val  dA = new DenseMatrix(A)
    	val  dB = new DenseMatrix(B)
 	val  dAc = dA.copy()
    	var dBc = new DenseMatrix(1,1)

	var rcond =  -1.0  // used to determine the effective rank of  A. Singular values S(i) <= rcond*S(1) are treated as zero. If rcond<0 machine precision is used instead
    	var M = A.length   // number of rows of matrix A
    	var N = A(0).length  // number of columns of matrix A
    	var  NRHS = B(0).length;  // number of right hand sides, i.e., the number of columns of the matrices B and X

     var minMN  = N
     if (M<minMN) minMN = M  
     var S = new Array[Double](minMN)   // the singular values of A in decreasing order
     												// the condition number of A in the 2-norm = (S(1)/S(m,n))
     
    var  solVecLength = N
    if  (M < N)  {  // undetermined problem
       dBc  = new DenseMatrix(N, NRHS)
       var r = 0
       while  ( r < M) {
           var c = 0
           while  ( c <  NRHS) {
               dBc.set(r, c, B(r)(c))
               c += 1
           }
       r += 1
          }
    }
    else  // overdetermined problem
        dBc = dB.copy()
    
    // each column of resultsMatrix will have a solution vector
    var  resultsMatrix = Array.ofDim[Double](solVecLength, NRHS)
    
    
    var info = new intW(0)
    
    var  workSize = -1 // issue workspace query
    var  work = new Array[Double](2)
    
    var  LDA = ld(M)  // leading dimension of A
    var  LDB = Math.max(M, N)  // since LDB >= max(1, M, N)

    var rank = new intW(0)
    var iwork = new Array[Int](1)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB, S, rcond, rank,  work, workSize, iwork,  info)
    workSize =  work(0).asInstanceOf[Int]  // take the computed optimal workspace size
    
    work = new Array[Double](workSize)
    iwork = new Array[Int](workSize)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB,S, rcond, rank, work, workSize, iwork, info)

    if (info.`val` != 0)  {
        println("illegal DGELS with code:  "+info.`val`)
        resultsMatrix
    }
    
    // copy the results to the resultsMatrix
    var c = 0
    while (c < NRHS)  { // for all RHS
        var v = 0
        while  (v < solVecLength)  { // for all variables of the system
            resultsMatrix(v)(c) = dBc.get(v, c)
            v += 1
        }
    c += 1
    }
     (resultsMatrix, S, rank)
  }

  
}


  



 