
package scalaSci.EJML

import scalaExec.Interpreter.GlobalValues
import scalaSci.Matrix
import scalaSci.RichDouble2DArray
import scalaSci.RichNumber
import scalaSci.RichDouble1DArray
import scalaSci.Vec
import org.ejml.data.Complex64F
import org.ejml.data.DenseMatrix64F
import org.ejml.ops._
import org.ejml.simple.SimpleMatrix
import org.ejml.data.Eigenpair64F

import org.ejml.factory.DecompositionFactory
import JSci.maths.wavelet.Signal
import Jama.jMatrix
import java.util.Random

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray


import scala.language.postfixOps

// allows static methods of ScalaSci's mathematical objects (e.g. Mat) to be conveniently available,
// e.g. sin(m) instead of scalaSci.EJML.Mat.sin(m)

//  Different StaticMaths objects are used for different zero indexed Matrix types.
//  This one is for the 0-indexed  EJML  based Matrix 



object StaticMathsEJML  extends AnyRef with scalaSci.StaticScalaSciCommonOps[scalaSci.EJML.Mat] with scalaSci.StaticScalaSciGlobal   
                            {

  final def length(a: scalaSci.EJML.Mat) = a.length

  final def size(a: scalaSci.EJML.Mat) = a.size
 
final def wrap( M: Int, N: Int, x: Array[Double]) = {
  var dm = org.ejml.data.DenseMatrix64F.wrap(M, N, x)
  var sm = new SimpleMatrix(dm)
  new Mat(sm)
}  
  
final def  setMemoryUsageFaster = { org.ejml.EjmlParameters.MEMORY = org.ejml.EjmlParameters.MemoryUsage.FASTER }
final def  setMemoryUsageLowMemory = { org.ejml.EjmlParameters.MEMORY = org.ejml.EjmlParameters.MemoryUsage.LOW_MEMORY }
  
final def  setBlockWidth(blockWidth: Int) = { 
    org.ejml.EjmlParameters.BLOCK_WIDTH = blockWidth
    org.ejml.EjmlParameters.BLOCK_SIZE = blockWidth*blockWidth
}

final def  setBlockCholWidth(blockCholWidth: Int) = org.ejml.EjmlParameters.BLOCK_WIDTH_CHOL = blockCholWidth

final def setTransposeSwitch(transposeSwitch: Int) = org.ejml.EjmlParameters.TRANSPOSE_SWITCH = transposeSwitch

  // At what point does it switch from a small matrix multiply to the reorder version.
final def setMultColumnSwitch(multColumnSwitch: Int) = { org.ejml.EjmlParameters.MULT_COLUMN_SWITCH = multColumnSwitch }
final def setMultTranabColumnSwitch(multTranabColumnSwitch: Int) = { org.ejml.EjmlParameters.MULT_TRANAB_COLUMN_SWITCH = multTranabColumnSwitch }

 // At which point should it switch to the block cholesky and qr algorithm.
final def setSwitchNlock64Cholesky(switchBlock64Cholesky: Int) = { org.ejml.EjmlParameters.SWITCH_BLOCK64_CHOLESKY = switchBlock64Cholesky }
final def setSwitchNlock64QR(switchBlock64QR: Int) = { org.ejml.EjmlParameters.SWITCH_BLOCK64_QR = switchBlock64QR }



final def ones0b(n: Int) = {
   scalaSci.EJML.BMat.ones(n,n)
  }

final def ones0b(n: Int, m: Int) = {
   scalaSci.EJML.BMat.ones(n,m)
  }


final def fill0b(n: Int, m: Int, value: Double) = {
   scalaSci.EJML.BMat.fill(n, m, value)
  }


final def fill0b(n: Int, value: Double) = {
   scalaSci.EJML.BMat.fill(n, n, value)
  }
  
final def extractDiag(x: Mat) = x.extractDiag    // ?? test
/* example:
var mMat = rand0(3, 6); var vMat = rand0(1,10)
var isNotVec = isVector(mMat)
var isVec = isVector(vMat)
*/
final def isVector(x: Mat) =    x.isVector



 
/* ?example:
var f58 = fill0(5, 8)
var f55 = fill0(5)
*/
   
 
  final def rand0b(n: Int, m:Int) = {
       scalaSci.EJML.BMat.random(n,m)
    }

  final def rand0b(n: Int) = {
       scalaSci.EJML.BMat.random(n,n)
    }

  
 
final def zeros0b(n: Int, m: Int) = {
   scalaSci.EJML.BMat.zeros(n,m)
  }

  
final def zeros0b(n: Int) = {
   scalaSci.EJML.BMat.zeros(n,n)
  }



final def sin(m: BMat) = {
   scalaSci.EJML.BMat.sin(m)
  }

final def cos(m: BMat) = {
   scalaSci.EJML.BMat.cos(m)
  }

final def tan(m: BMat) = {
   scalaSci.EJML.BMat.tan(m)
  }

final def asin(m: BMat) = {
   scalaSci.EJML.BMat.asin(m)
  }

final def acos(m: BMat) = {
   scalaSci.EJML.BMat.acos(m)
  }

final def atan(m: BMat) = {
   scalaSci.EJML.BMat.atan(m)
  }


final def sinh(m: BMat) = {
   scalaSci.EJML.BMat.sinh(m)
  }

final def cosh(m: BMat) = {
   scalaSci.EJML.BMat.cosh(m)
  }

final def tanh(m: BMat) = {
   scalaSci.EJML.BMat.tanh(m)
  }


final def rank(A: Mat) = A.rank() 

final def trace(M: Mat): Double = {
 M.trace
}

// svd Singular value decomposition.
//    (U,S,V) = svd(X) produces a diagonal matrix S, of the same 
//    dimension as X and with nonnegative diagonal elements in
//    decreasing order, and unitary matrices U and V so that X = U*S*V'.
 
final def svd(am: Mat) = am.svd()



final def eigAll(am: Mat) =  {
   var eigd = scalaSci.EJML.StaticMathsEJML.eigEJML(am)    // get the EigenDecomposition
   var N = eigd.getNumberOfEigenvalues
    // allocate space for eigenvalues and eigenvectors
   var eigvals  =  new Array[Double](N)
   var eigvecs = Array.ofDim[Double](N, N)
   for (k<-0 to N-1) {
     var evk = eigd.getEigenvalue(k)
     if (evk != null) {
        eigvals(k) = eigd.getEigenvalue(k).getReal
        var  cevec = eigd.getEigenVector(k)
     if (cevec!= null)
      for (l<-0 to N-1)
        eigvecs(k)(l) = cevec.get(l, 0)
     }
   }
   ( eigvecs, eigvals, eigd)
}
   
  
// get the eigenvalues and eigenvectors of m
final def   eig(am: Mat, denseVector: Boolean) =  {
   var eigd = scalaSci.EJML.StaticMathsEJML.eigEJML(am)    // get the EigenDecomposition
   var N = eigd.getNumberOfEigenvalues
    // allocate space for eigenvalues and eigenvectors
   var eigvals  =  new Array[Complex64F](N)
   var eigvecs = new Array[DenseMatrix64F](N)
   for (k<-0 to N-1) {
     eigvals(k) = eigd.getEigenvalue(k)
     if (eigvecs != null)
       eigvecs(k) = eigd.getEigenVector(k).getMatrix
  }

   (eigvecs, eigvals)
}

// get the eigenvalues of m
final def eigD(am: Mat) = {   
   var eigd = scalaSci.EJML.StaticMathsEJML.eigEJML(am)    // get the EigenDecomposition
   var N = eigd.getNumberOfEigenvalues
 // allocate space for eigenvalues and eigenvectors
   var eigvals  =  new Array[Complex64F](N)
   for (k<-0 to N-1) 
     eigvals(k) = eigd.getEigenvalue(k)
     
 eigvals
}


// get the eigenvectors of m
final def   eigV(am: Mat) =  {
   var eigd = scalaSci.EJML.StaticMathsEJML.eigEJML(am)    // get the EigenDecomposition
   var N = eigd.getNumberOfEigenvalues
    // allocate space for eigenvalues and eigenvectors
   var eigvecs = new Array[DenseMatrix64F](N)
   if (eigvecs != null)
     for (k<-0 to N-1) 
     eigvecs(k) = eigd.getEigenVector(k).getMatrix
  eigvecs
}

  
// return the Kronecker product of two matrices
final def kron(Mr: Mat, M: Mat) = {
  Mr.kron(M)
}

//final def  rank(M: Mat): Int = {
//  M.rank
//}

//final def trace(M: Mat): Double = {
//   M.trace
//}

/*
final def Eigen_V(M: Mat): Mat = {
   new Mat(LinearAlgebra.eigen(M.v).getV().getArray)
}

final def V(M: Mat): Mat = {
   Eigen_V( M)
 }

final def  Eigen_D(M: Mat) = {
   new Mat(LinearAlgebra.eigen(M.v).getD().getArray)
}

final def  D(M: Mat):Mat = {
   Eigen_D( M)
}
*/

final def inv(a: scalaSci.EJML.Mat) = a.inv()

final def  pinv(a: scalaSci.EJML.Mat) = a.pinv()

 
 


//  Computes the Frobenius normal of the matrix, sqrt(sum(diag(X'*X))).
  /* ?? test
//   The condition p = 2 number of a matrix is used to measure the sensitivity of the linear
//     system <b>Ax=b</b>.  A value near one indicates that it is a well conditioned matrix.
 
var a = rand0(4)
var nf = normF(a)
*/   

   // Normalizes the matrix such that the Frobenius norm is equal to one
final def normalizeF(a: scalaSci.EJML.Mat) = a.normalizeF
 
final def fastNormF(a: scalaSci.EJML.Mat) = a.fastNormF
 
  // Computes either the vector p-norm or the induced matrix p-norm depending on  A being
  // a vector or a matrix respectively 
final def normP(a: scalaSci.EJML.Mat, p: Double)  = a.normP(p)

  
  

  // Computes the p=1 norm. If A is a matrix then the induced norm is computed
final def normP1(a: scalaSci.EJML.Mat) = a.normP1  

  // Computes the p=1 norm. If A is a matrix then the induced norm is computed
final def normP2(a: scalaSci.EJML.Mat) = a.normP2

  //  Computes the p=2 nrm. If A is a matrix then the induced norm is computed. 
  // This implementation is faster, but more prone to buffer overflow or underflow problems
 final def fastNormP2(a: scalaSci.EJML.Mat) = a.fastNormP2

 final def normPInf(a: scalaSci.EJML.Mat) = a.normPInf
 
 final def inducedP1(a: scalaSci.EJML.Mat)  = a.inducedP1
 
 final def inducedP2(a: scalaSci.EJML.Mat)  = a.inducedP2
 
 final def inducedPInf(a: scalaSci.EJML.Mat) = a.inducedPInf

  
 
final def T(m: Mat): Mat = {
   m.transpose()
}

final def trans(m: Mat): Mat = {
   m.transpose()
}

final def  transpose(m: scalaSci.EJML.Mat)  = {
   m.transpose
   }


final def invertInPlace(m: scalaSci.EJML.Mat): Boolean = {
  m.invertInPlace
}  

final def invert(m: scalaSci.EJML.Mat): scalaSci.EJML.Mat = {
  m.invert
}  
  

final def identity0(width: Int) = {
  new scalaSci.EJML.Mat(new SimpleMatrix(CommonOps.identity(width)))
}  

final def identity0(numRows: Int, numCols: Int) = {
  new scalaSci.EJML.Mat(new SimpleMatrix(CommonOps.identity(numRows, numCols)))
 }
 
 

  /* example:
var ll = List(9, -8.8, 6.5)
var  mm = diag(ll)
*/

  // COVARIANCE-OPS
// This is a fairly light weight check to see of a covariance matrix is valid.
// It checks to see if the diagonal elements are all postive, which they should be
// if it is valid.  Not all invalid covariance matrices will be caught by this method.
 final def isValidFast(m: scalaSci.EJML.Mat) = 
   CovarianceOps.isValidFast(m.sm.getMatrix)
 
  // Performs a variety of tests to see if the provided matrix is a valid covariance matrix.
 final def isValid(m: scalaSci.EJML.Mat) = 
   CovarianceOps.isValid(m.sm.getMatrix)
  
 // Performs a matrix inversion operation that takes advantage of the special 
 // properties of a covariance matrix
 //  cov:  a covariance matrix, on input is a covariance matrix, on output if it is 
 //  possible to inverse the matrix, is the modified matrix
 final def covinvert(covm: scalaSci.EJML.Mat): Boolean = 
   CovarianceOps.invert(covm.sm.getMatrix)

  
 // Performs a matrix inversion operations that takes advantage of the special
//  properties of a covariance matrix.
// cov A:  covariance matrix. Not modified.
// cov_inv:  The inverse of cov.  Modified.
// return  true if it could invert the matrix false if it could not.
final def covinvert(covm: scalaSci.EJML.Mat, rcovm: scalaSci.EJML.Mat): Boolean =
  {
    CovarianceOps.invert(covm.sm.getMatrix, rcovm.sm.getMatrix)
  }
  
  

  // EIGEN-OPS
  

 
// generates a bound for the largest eigenvalue of the provided matrix using Perron-Frobenius 
// theorem. This function only applies to non-negative real matrices
// For "stochastic" matrices (Markov process) this should return one for the upper and lower bound
// A:  a square matrix with positive elements. Not modified
// bound:  where the results are stored. If null then a matrix will be declared. Modified
// returns the lower and upper bound in the first and second elements respectively
final def boundLargestEigenValue(A: scalaSci.EJML.Mat, bound: Array[Double]) = {
  EigenOps.boundLargestEigenValue(A.sm.getMatrix, bound)
}
  
  
// a diagonal matrix where a real diagonal element contains a real eigenvalue. If an eigenvalue is
// imaginary then zero is stored in its place  
// eig: An eigenvalue decomposition which has already decomposed a matrix
// A:   A diagonal matrix containing the eigenvalues
final def createMatrixD(eig: org.ejml.interfaces.decomposition.EigenDecomposition[DenseMatrix64F]): scalaSci.EJML.Mat = {
    var deig = org.ejml.ops.EigenOps.createMatrixD(eig)
    new Mat(new SimpleMatrix(deig))
}

// puts all the real eigenvectors into the columns of a matrix. If an eigenvalue is imaginary
// then the corresponding eigenvector will have zeros in its column
// eig: An eigenvalue decomposition which has already decomposed a matrix
// return an m by m matrix containing eigenvectors in its columns
final def createMatrixV(eig: org.ejml.interfaces.decomposition.EigenDecomposition[DenseMatrix64F]): scalaSci.EJML.Mat = {
  new Mat(new SimpleMatrix(EigenOps.createMatrixV(eig)))
}
  
  

  // wrapper for RandomMatrices EJML interface
 
// creates a randomly generated set of orthonormal vectors.  At most it can generate the same 
// number of vectors as the dimension of the vectors
// this is done be creating random vectors then ensuring that they are orthogonal to all the ones previously
// created with reflectors
// NOTE: This employs a brute force O(N<sup>3</sup>) algorithm.
//  @param dimen dimension of the space which the vectors will span.
//  @param numVectors How many vectors it should generate.
//  @param rand Used to create random vectors.
//  @return Array of N random orthogonal vectors of unit length.
//  e.g. 
/* 
     var  rg = new java.util.Random
     var  ejmlMats = createSpan(15, 10, rg) 
  */
  final def  createSpan(dimen: Int, numVectors: Int, rg: java.util.Random) =  {
     var denseMatrices =   org.ejml.ops.RandomMatrices.createSpan(dimen, numVectors, rg)
    var ejmlMats = new Array[Mat](numVectors)
    for (k<-0 until numVectors)
      ejmlMats(k) = new Mat(new SimpleMatrix(denseMatrices(k)))
    ejmlMats
 }
  
  
  
//   Creates a random vector that is inside the specified span.
//     @param span The span the random vector belongs in.
//     @param rg  RNG
//     @return A random vector within the specified span.
/* 
     var  rg = new java.util.Random
     var  ejmlMats = createSpan(15, 5, rg) 
     var inSpanVec = createInSpan(ejmlMats, 0, 2, rg)
   */
  
final def  createInSpan(span: Array[Mat], min: Double, max: Double, rg: java.util.Random)  = {
   val splen = span.length
   var dm64Fs = new Array[org.ejml.data.DenseMatrix64F](splen)
   for (k<-0 until splen)
     dm64Fs(k) = span(k).sm.getMatrix()  // get the DenseMatrix64Fs
   
    var dm64 = org.ejml.ops.RandomMatrices.createInSpan(dm64Fs, min, max, rg)
    
  new Mat(new SimpleMatrix(dm64))  
}
  
  
  
    // Creates a random symmetric positive definite matrix.
    // @param width The width of the square matrix it returns.
     // @param rg Random number generator used to make the matrix.
     // @return The random symmetric  positive definite matrix.
   /* e.g.
     var rnd = new java.util.Random
     var width = 8
     var  symmPosDefEJML = scalaSci.EJML.StaticMathsEJML.createSymmPosDef(width, rnd)
     */
final def createSymmPosDef(width: Int, rg: java.util.Random) = {
  val spdDense64 = org.ejml.ops.RandomMatrices.createSymmPosDef(width, rg)
  new Mat(new SimpleMatrix(spdDense64))
}
  
  // sets the provided square matrix to be a random symmetric matrix whose values are selected
  // from a uniform distribution, from min to max inclusive
  final def createSymmetric(A: Mat, min: Double, max: Double, rand: Random):Unit = {
    RandomMatrices.createSymmetric(A.sm.getMatrix, min, max, rand)
  }
  

   // Creates a random symmetric matrix whose values are selected from an uniform distribution
  //  from min to max, inclusive.
  //   @param length Width and height of the matrix.
  //    @param mn Minimum value an element can have.
  //   @param mx Maximum value an element can have.
  //   @param rg Random number generator.
  //    @return A symmetric matrix.
  /* e.g.
     var rnd = new java.util.Random
     var N = 8; var mn = -7.5; var mx = 4.5
     var symmEJML = scalaSci.EJML.StaticMathsEJML.createSymmetric(N, mn, mx,  rnd)
     */
 final def createSymmetric(length: Int, mn: Double, mx: Double, rg: java.util.Random) = {
    val symmDense64 = org.ejml.ops.RandomMatrices.createSymmetric(length, mn, mx, rg)
    new Mat(new SimpleMatrix(symmDense64))
  }  
  
   
  
  //  Creates an upper triangular matrix whose values are selected from a uniform distribution.  If hessenberg
  //     is greater than zero then a hessenberg matrix of the specified degree is created instead.
//   @param dimen Number of rows and columns in the matrix..
//  @param hessenberg 0 for triangular matrix and > 0 for hessenberg matrix.
//  @param mn minimum value an element can be.
//  @param mx maximum value an element can be.
//  @param rg random number generator used.
//  @return The randomly generated matrix.
  /* e.g.
     var rnd = new java.util.Random
     var dimen = 8; var hessenberg = 0; var mn = -7.5; var mx = 4.5
     var triagEJML = scalaSci.EJML.StaticMathsEJML.createUpperTriangle(dimen, hessenberg, mn, mx,  rnd)
     hessenberg = 1
     var hessenbergEJML = scalaSci.EJML.StaticMathsEJML.createUpperTriangle(dimen, hessenberg, mn, mx, rnd)
     */
final def createUpperTriangle(dimen: Int, hessenberg: Int, mn: Double, mx: Double, rg: java.util.Random) = {
    val upperTriangleDense64 = org.ejml.ops.RandomMatrices.createUpperTriangle(dimen, hessenberg, mn, mx, rg)
    new Mat(new SimpleMatrix(upperTriangleDense64))
  }
  
 
  
    //  Creates a random orthogonal or isometric matrix, depending on the number of rows and columns.
     // The number of rows must be more than or equal to the number of columns.
     //  @param numRows Number of rows in the generated matrix.
     //  @param numCols Number of columns in the generated matrix.
     // @param rg  Random number generator used to create matrices.
     //  @return A new isometric matrix.
     /* e,g.
     var rnd = new java.util.Random
     var numRows = 5; var numCols = 5;  
     var orthogEJML = scalaSci.EJML.StaticMathsEJML.createOrthogonal(numRows, numCols, rnd)
        // verify orthogonality
     var shouldBeIdentity = (orthogEJML~) * orthogEJML 
     */
 final def createOrthogonal(numRows: Int, numCols: Int, rg: java.util.Random) = {
    val orthogDense64 = org.ejml.ops.RandomMatrices.createOrthogonal(numRows, numCols, rg)
    new Mat(new SimpleMatrix(orthogDense64))
  }
  
  
  
    //   Creates a random diagonal matrix where the diagonal elements are selected from a uniform
    //  distribution that goes from min to max.
    //   @param N, M Dimension of the matrix.
    //  @param mn Minimum value of a diagonal element.
    //  @param mx Maximum value of a diagonal element.
     //  @param rg Random number generator.
     // @return A random diagonal matrix.
     /* e.g
     var rnd = new java.util.Random
     var N = 10; var mn = 0.2; var mx = 3.5; 
     var diagEJML = scalaSci.EJML.StaticMathsEJML.createDiagonal(N, mn, mx, rnd)
     */
  final def createDiagonal(N: Int, mn: Double, mx: Double, rg: java.util.Random) = {
    val diagDense64 = org.ejml.ops.RandomMatrices.createDiagonal(N, mn, mx, rg)
    new Mat(new SimpleMatrix(diagDense64))
  } 
  
  
      /* e.g
     var rnd = new java.util.Random
     var N = 10; var M=15; var mn = -2.2; var mx = 3.5; 
     var diagEJML = scalaSci.EJML.StaticMathsEJML.createDiagonal(N, M,  mn, mx, rnd)
     */
  final def createDiagonal(N: Int, M: Int, mn: Double, mx: Double, rg: java.util.Random) = {
    val diagDense64 = org.ejml.ops.RandomMatrices.createDiagonal(N, M, mn, mx, rg)
    new Mat(new SimpleMatrix(diagDense64))
  }
  
  
  
  
   //   Creates a random matrix which will have the provided singular values.  The length of sv
   //  is assumed to be the rank of the matrix.  This can be useful for testing purposes when one
   //  needs to ensure that a matrix is not singular but randomly generated.
     //  @param numRows Number of rows in generated matrix.
     // @param numCols NUmber of columns in generated matrix.
     // @param rg Random number generator.
     //  @param sv Singular values of the matrix.
     // @return A new matrix with the specified singular values.
     /* e.g
     var rnd = new java.util.Random
     var N = 5; var M=8; 
     var sv = Array(0.2, 3.4, -4.5, 5.6, 0.34)
     var singEJML = scalaSci.EJML.StaticMathsEJML.createSingularValues(N, M,  rnd, sv)
     */
    
   final def createSingularValues(numRows: Int, numCols: Int, rg: java.util.Random, sv:Array[Double]) = {
     val singDense64 = org.ejml.ops.RandomMatrices.createSingularValues(numRows, numCols, rg, sv: _*)
     new Mat(new SimpleMatrix(singDense64))
   }
   
  
  
// creates a new random symmetric matrix that will have the specified real eigenvalues
//  num: Dimension of the resulting matrix
//  rand: Random number generator
//  eigenvalues: Set of real eigenvalues that the matrix will have
//  return:  a random matrix with the specified eigenvalues
final def createEigenvaluesSymm(num: Int, rand: Random, eigenvalues: Double*): Mat = {
  new Mat(new SimpleMatrix(RandomMatrices.createEigenvaluesSymm(num, rand, eigenvalues: _*)))
}
  
// returns a matrix where all the elements are selected independently from
// a uniform distribution between 0 and 1 inclusive
//   numRow: number of rows in the new matrix
//   numCol:  number of columns in the new matrix
//   rand:  Random number generator used to fill the matrix
//   return: The randomly generated matrix
final def  createRandom(numRow: Int, numCol: Int, rand: Random): Mat = {
  new Mat(new SimpleMatrix(RandomMatrices.createRandom(numRow, numCol, rand)))
}  

  // adds random values to each element in the matrix from a uniform distribution
  // A: The matrix who is to be randomized. Modified
  // min: The minimum value each element can be
  // max: The maximum value each element can be
  // rand: Random number generator used to fill the matrix
final def addRandom(A: Mat, min: Double, max: Double, rand: Random) = {
  RandomMatrices.addRandom(A.sm.getMatrix, min, max, rand)
}
  
  // returns a matrix where all the elements are selected independently from a uniform distribution
  // between 'min' and 'max' inclusive
 final def createRandom(numRow: Int, numCol: Int, min: Double, max: Double, rand: Random): Mat = 
   {
     new Mat(new SimpleMatrix(RandomMatrices.createRandom(numRow, numCol, min, max, rand)))
   }
  
   
 
     

  // COVARIANCE-OPS

//  This is a fairly light weight check to see of a covariance matrix is valid.
//   It checks to see if the diagonal elements are all positive, which they should be
//   if it is valid.  Not all invalid covariance matrices will be caught by this method.
 final def  isValidCovarianceMatrixFast(a: Mat) = 
   org.ejml.ops.CovarianceOps.isValidFast(a.sm.getMatrix())
 
  // performs a variety of tests to see if the provided matrix is a valid
  // covariance matrix
  //   @return  0 = is valid 1 = failed positive diagonal, 2 = failed on symmetry, 2 = failed on positive definite
final def isValidCovarianceMatrix(a: Mat) = 
  org.ejml.ops.CovarianceOps.isValid(a.sm.getMatrix())

 //  Performs a matrix inversion operation that takes advantage of the special
 //   properties of a covariance matrix.
 //   @param cov  On input it is a covariance matrix, on output it is the inverse.  Modified.
 //  @return true if it could invert the matrix false if it could not.
 final def  invertCovarianceMatrix(cv: Mat) = {
    org.ejml.ops.CovarianceOps.invert(cv.sm.getMatrix())
 }
 //  Performs a matrix inversion operations that takes advantage of the special
 //    properties of a covariance matrix.
 //         @param cov A covariance matrix. Not modified.
 //         @param cov_inv The inverse of cov.  Modified.
 //         @return true if it could invert the matrix false if it could not.
final def invertCovarianceMatrix(cv: Mat, cv_inv: Mat ) = 
  org.ejml.ops.CovarianceOps.invert(cv.sm.getMatrix(), cv_inv.sm.getMatrix())

 //  Sets vector to a random value based upon a zero-mean multivariate Gaussian distribution with
 //   covariance 'cov'.
 //    @param cov The distirbutions covariance.  Not modified.
 //    @param vector The random vector. Modified.
 //    @param rand Random number generator.
 final def  randomVector(cv: Mat, vector: Mat, rg: java.util.Random) = 
   org.ejml.ops.CovarianceOps.randomVector(cv.sm.getMatrix(), vector.sm.getMatrix, rg)
 
  
  
  // EIGEN OPS
//  Given matrix A and an eigenvector of A, compute the corresponding eigenvalue. 
//       This is the Rayleigh quotient 
//   @param A Matrix. Not modified.
//   @param eigenVector An eigen vector of A. Not modified.
//   @return The corresponding eigen value.
final def  computeEigenValue(A: Mat, eigenVector: Mat) =
  org.ejml.ops.EigenOps.computeEigenValue(A.sm.getMatrix(), eigenVector.sm.getMatrix())
  
   //    Given an eigenvalue it computes an eigenvector using inverse iteration:
   //    NOTE: If there is another eigenvalue that is very similar to the provided one then there
   //    is a chance of it converging towards that one instead.  The larger a matrix is the more
   //    likely this is to happen.
   //  @param A Matrix whose eigenvector is being computed.  Not modified.
   //  @param eigenvalue The eigenvalue in the eigen pair.
   // @return The eigenvector or null if none could be found.
 final def  computeEigenVector(A: Mat, eigenvalue: Double) = 
    org.ejml.ops.EigenOps.computeEigenVector(A.sm.getMatrix(), eigenvalue)
  
   //  Computes the dominant eigenvector for a matrix.  The dominant eigenvector is an
   //  eigen vector associated with the largest eigenvalue.
   //  WARNING: This function uses the power method.  There are known cases where it will not converge.
   //   It also seems to converge to non-dominant eigenvectors some times.  Use at your own risk.
   //   @param A A matrix.  Not modified.
   /* e.g. 
   var A = M0(" 1 2 -1; 2 1 -2.5; -1 -2.5 -1")
   var dmevec = scalaSci.EJML.StaticMathsEJML.dominantEigenvec(A)
  
  */
 final def  dominantEigenpair(A: Mat) = 
   org.ejml.ops.EigenOps.dominantEigenpair(A.sm.getMatrix())
 
 
   
  
  final def LU(A: scalaSci.EJML.Mat) = A.LU
  
  final def lu(A: scalaSci.EJML.Mat) = A.LU
  
  final def CholeskyBlock(A: scalaSci.EJML.Mat) = A.CholeskyBlock
  
  final def  QR(A: scalaSci.EJML.Mat) = A.QR
  
  final def qr(A: scalaSci.EJML.Mat) = QR(A)
  
  final def QRHouseholder(A: scalaSci.EJML.Mat) = A.QRHouseholder
  
  final def QRHouseholderSolve(A: scalaSci.EJML.Mat, B: scalaSci.EJML.Mat):  scalaSci.EJML.Mat  = {
    var X = A.QRHouseholderSolve(B)    
    X
  }

 final def  RandomNormal(m: Int,  n: Int,  mu:Double, sigma:Double): Mat  = {
       var g =  scalaSci.math.array.StatisticSample.randomNormal(m, n, mu, sigma)
        var matRN = new Mat(g)
        matRN
}

final def  RandomUniform(m: Int,  n: Int,  mn:Double, mx:Double): Mat  = {
   var g =  scalaSci.math.array.StatisticSample.randomUniform(m, n, mn, mx)
   var matRN = new Mat(g)
   matRN
}

  
final def cov(v1: Mat, v2: Mat): Mat = {
  var r = cov(v1.toDoubleArray, v2.toDoubleArray)
  new Mat(r)
}

final def covariance(v1: Mat, v2: Mat): Mat = {
  cov(v1, v2)
}

 final def cov(mt: Mat): Mat = {
     cov(mt, mt)
 }
 

 final def covariance(mt: Mat): Mat = {
     cov(mt)
 }
 
 final def  corr(mt1: Mat, mt2: Mat): Mat = {
     var r = scalaSci.math.array.StatisticSample.correlation(mt1.toDoubleArray, mt2.toDoubleArray);
     new Mat(r)
 }

 
 final def  correlation(mt1: Mat, mt2: Mat): Mat = {
     corr(mt1, mt2)
 }
 
 
 final def  correlation(mt: Mat): Mat = {
     corr(mt, mt)
 }

 
 final def  corr(mt: Mat): Mat = {
     corr(mt, mt)
 }


 final def LU_solve(A: Mat, b: Mat) = { new Mat(1,1) }  // Sterg-TODO

 final  def solve(A: Mat, b: Mat) = A.solve(b)
 final  def  / (A: Mat, b: Mat) = A.solve(b)


  
  // SOSSOS - from Mat
  

// Computes a full Singular Value Decomposition (SVD) of this matrix with the
//  eigenvalues ordered from largest to smallest.
//   @return SVD
final def svdEJML(m: Mat) = {
      m.sm.svd()
    }


//  Computes the SVD in either  compact format or full format.
//  @return SVD of this matrix.
final def  svdEJML(m:Mat, compact: Boolean ) = {
      m.sm.svd(compact)
    }



// return the EJML decomposition directly from EJML library
final def eigEJML(m: Mat) =
  m.sm.eig()


 //  Returns the Eigen Value Decomposition (EVD) of this matrix.
final def eig(m: Mat) = m.eig()  

    // compute eigenvalues using LAPACK
    
final def leig(M: Mat) = {
  val Mmat = M.getMatrix
  val nRows = Mmat.getNumRows
  val nCols =  Mmat.getNumCols
  val data = Array.ofDim[Double](nRows, nCols)
  var r = 0
  while (r < nRows) {
    var c = 0
    while  (c <  nCols) {
      data(r)(c) = Mmat.get(r, c)
      c += 1
    }
    r += 1
  }
    var DM = new  no.uib.cipr.matrix.DenseMatrix(data)  // make a DenseMatrix from SimpleMatrix
    var evdObj = no.uib.cipr.matrix.EVD.factorize(DM)
 
    (evdObj.getRealEigenvalues(), evdObj.getImaginaryEigenvalues(),
    evdObj.getLeftEigenvectors().toDoubleArray,  evdObj.getRightEigenvectors().toDoubleArray)

}  

  
// convert from EJMLMat to Matrix
final def toMatrix(m: Mat) = {
  var dataArray = m.getv   // get the data array
  var matrix = new scalaSci.Matrix(dataArray)
  matrix
}

// convert from Matrix to EJMLMat to 
final def toEJMLMat(m: scalaSci.Matrix) = {
  var dataArray = m.getv   // get the data array
  var sm = new _root_.org.ejml.simple.SimpleMatrix(dataArray)
  var EJMLmat = new Mat(sm)
  EJMLmat
 }

  final def det(m: scalaSci.EJML.Mat) = 
   m.det
   
  
  

final def testMat(N: Int, M: Int) = {
  var a = new Mat(N, M)
  var rows = 0
  while (rows < N) {
    var cols = 0
    while  (cols < M)  {
      a(rows, cols) = rows*10+cols
      cols += 1
    }
  rows += 1
  }
  a
}

 final def norm1(a: Mat) = {
  NormOps.normP1(a.sm.getMatrix)
}  

final def norm2(a: Mat) = {
  NormOps.normP2(a.sm.getMatrix)
}  
//  Computes the Frobenius normal of the matrix:
final def  normF(a: Mat) = {
      NormOps.normF(a.sm.getMatrix)
    }

final def normInf(a: Mat) = {
   NormOps.normPInf(a.sm.getMatrix)
}   

    // COLUMNWISE OPERATIONS
// columnwise sum
final def sum(matr: Mat): Array[Double] = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 0
    while ( ccol  < Ncols) {
     sm=0.0
     var crow = 0
     while  (crow < Nrows) {
       sm += matr(crow, ccol)
       crow += 1
     }
     res(ccol) = sm
     
      ccol += 1
     }
 res
}

// columnwise mean
final def mean(matr: Mat): Array[Double]  = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
     sm=0.0
     var crow = 0
     while  (crow < Nrows) {
       sm += matr(crow, ccol)
       crow += 1
     }
     res(ccol) = sm/Nrows
     
      ccol += 1
     }
 res
}

// columnwise product
final def prod(matr: Mat): Array[Double]  = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var pd = 1.0
    var res = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
     pd=1.0
     var crow = 0
     while  (crow < Nrows) {
       pd *= matr(crow, ccol)
       crow += 1
     }
     res(ccol) = pd
     
      ccol += 1
     }
    res
}

 // columnwise min
 final def min(matr: Mat): Array[Double] = {
     var Nrows = matr.Nrows;   var Ncols = matr.Ncols
     var res = new Array[Double](Ncols)
     var ccol = 0
     while  (ccol <= Ncols-1) {
         var mn = matr(0, ccol)  // keeps the running min element
         var crow = 0
         while  (crow <= Nrows-1)  {
              var tmp = matr(crow, ccol)
               if (tmp  < mn)  mn = tmp
               crow += 1
           }
           res(ccol) = mn   // min element for the ccol column
           ccol += 1
     }
     res
 }

  // columnwise max
 final def max(matr: Mat): Array[Double] = {
     var Nrows = matr.Nrows;   var Ncols = matr.Ncols
     var res = new Array[Double](Ncols)
     var ccol = 0
     while  (ccol < Ncols) {
         var mx = matr(0, ccol)  // keeps the running max element
         var crow = 1
         while  (crow < Nrows)  {
              var tmp = matr(crow, ccol)
               if (tmp  > mx)  mx = tmp
               crow += 1
           }
        res(ccol) = mx   // max element for the ccol column
        
      ccol += 1
     }
     res
 }


// ROWWISE OPERATIONS
// rowwise sum
final def sumR(matr: Mat): Array[Double] = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // sum for all rows
    var crow = 0
    while  (crow < Nrows-1)  {
        sm=0.0
        var ccol = 0
        while  (ccol  < Ncols-1)  {  // sum across column
         sm += matr(crow, ccol)
         ccol += 1
        }
     res(crow) = sm
     crow += 1
     }
 res
}

// rowwise mean
final def meanR(matr: Mat): Array[Double]  = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // mean for all rows
    var crow = 0
    while  (crow < Nrows) {
     sm=0.0
     var ccol = 0
     while  (ccol <  Ncols) {  // sum across column
       sm += matr(crow, ccol)
       ccol += 1
     }
     res(crow) = sm/Ncols
     
      crow += 1
     }
 res
}

// rowwise product
final def prodR(matr: Mat): Array[Double]  = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var pd = 1.0
    var res = new Array[Double](Nrows)
    var crow = 0
    while  (crow < Nrows) {
     pd=1.0
     var ccol = 0
     while  (ccol < Ncols)  {  // product across column
       pd *= matr(crow, ccol)
       ccol += 1
     }

     res(crow) = pd
     crow += 1
     }
    res
}

 // rowwise min
 final def minR(matr: Mat): Array[Double] = {
     var Nrows = matr.Nrows;   var Ncols = matr.Ncols
     var res = new Array[Double](Nrows)
     var crow = 0 
     while  (crow < Nrows)  {
         var mn = matr(crow, 0)  // keeps the running min element
         var ccol = 1
         while  (ccol <  Ncols)  {
              var tmp = matr(crow, ccol)
               if (tmp  < mn)  mn = tmp
               ccol += 1
           }
           res(crow) = mn   // min element for the crow row
           crow += 1
     }
      res
 }

  // rowwise max
 final def maxR(matr: Mat): Array[Double] = {
     var Nrows = matr.Nrows;   var Ncols = matr.Ncols
     var res = new Array[Double](Nrows)
     var crow = 0
     while  (crow < Nrows) {
         var mx = matr(crow, 0)  // keeps the running max element
         var ccol = 1
         while  (ccol <  Ncols)  {
              var tmp = matr(crow, ccol)
               if (tmp  > mx)  mx = tmp
               ccol += 1
           }
        res(crow) = mx   // max element for the ccol column
        crow += 1
     }
     res
 }


 final def toDegrees(v: Mat): Mat = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
           j=0
           while (j < Ncols)  {
             om(i, j) = java.lang.Math.toDegrees(v(i, j))
             j += 1
            }
            i += 1
        }
          om
    }



 final def toRadians(v: Mat): Mat = {
   val Nrows = v.Nrows; val Ncols = v.Ncols;
   val om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
         j=0
         while (j < Ncols)  {
             om(i, j) = java.lang.Math.toRadians(v(i, j))
             j += 1
            }
            i += 1
        }
          om
    }




final def sqrt(v: Mat): Mat = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
          j=0
          while (j < Ncols)  {
             om(i,  j) = java.lang.Math.sqrt(v(i, j))
             j += 1
            }
            i += 1
        }
          om
    }

 

final def find(M: Mat) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   var xi = 0
   while  (xi <  n) {
     var yi = 0 
     while  (yi < m) {
        if (M(xi, yi) != 0.0)
           no+=1
        yi += 1  
     }
     xi += 1  
   }
  
  // build return vector
  var   indices = Array.ofDim[Int](no, 3)
  var  i = 0
  var col = 0
  while  (col <  m) {
    var row = 0
    while  (row <  n) {
    if (M(row, col) != 0.0)
         {
  // nonzero element found
  // put element position into return column vector
    indices(i)(0) = row + col*n
    indices(i)(1) = row
    indices(i)(2) = col
    i += 1
  }
  row += 1
    }
   col += 1
  }
 
 indices 
}

  
final def DenseMatrixToDoubleArray(x: DenseMatrix64F) = {
  var nr = x.numRows
  var nc = x.numCols
  var xa = Array.ofDim[Double](nr, nc)
  var row=0; var col=0
  while (row < nr) {
    col = 0
    while (col < nc) {
      xa(row)(col) = x.get(row, col)
      col += 1
    }
    row += 1
  }
  xa
}   
  
}










