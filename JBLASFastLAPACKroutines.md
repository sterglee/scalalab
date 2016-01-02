# Support for JBLAS both with RichDouble2DArray and JBLAS Matrix type #

`The new versions of ScalaLab (after May 06-2013) offer support for JBLAS both with the ` **`RichDouble2DArray`** ` type and the` **`JBLAS Matrix class.`** `The new version of the JBLAS library has been stabilized and works and for Win64 (the previous version had problems with Win64). `

`JBLAS can improve significantly many common operations. The following script illustrates significant performance difference between Java methods and native ones (I tested on Win32, Win64 and Linux and works as expected.)`

```

var N=300  // a size of the matrix
var x = rand(N)  // constructs a NXN RichDouble2DArray

// compute the eigendecomposition using Java
tic
var (evecs, evals) = eig(x)
var tmeigJ = toc

// compute the eigendecomposition using fast JBLAS routines
tic
var (fevals, fevecs) = x.feig
var tmeigJBLAS  = toc

// compute the SVD
tic
var svdJ = svd(x)
var tmSVDJava = toc

// compute the SVD using fast JBLAS routines
tic
var svdJBLAS = x.ffullSVD
var tmsvdJBLAS  = toc

```

`The fast JBLAS routines for the ` **`RichDouble2DArray`** ` type are described below together with the code. Similar is the interface for the routines that can be called on ` **`JBLAS Matrix`** `objects. `

```
// Fast JBLAS based routines
  
  // compute the eigenvalues/eigenvectors using JBLAS
  def feig() = {
     val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
     val eigVals = org.jblas.Eigen.eigenvalues(dm)  // eigenvalues
     val eigVecs = org.jblas.Eigen.eigenvectors(dm)   // eigenvectors
     
     (eigVals, eigVecs)
     }
     
 // compute the  eigenvalues using JBLAS
 def feigenvalues() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.eigenvalues(dm)
   }
   
  // compute the eigenvectors using JBLAS
  def feigenvectors() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.eigenvectors(dm)
    }
    
  // compute the symmetric eigenvalues using JBLAS
  def fsymmetricEigenvalues() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricEigenvalues(dm)
    }
    
  // compute the symmetric eigenvectors using JBLAS
  def fsymmetricEigenvectors() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricEigenvectors(dm)
    }
    
 //  Computes generalized eigenvalues of the problem A x = L B x.
// @param A symmetric Matrix A. Only the upper triangle will be considered. Refers to this
//  @param B symmetric Matrix B. Only the upper triangle will be considered.
//  @return a vector of eigenvalues L.
  def fsymmetricGeneralizedEigenvalues(B: Array[Array[Double]]) = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricGeneralizedEigenvalues(dm, new DoubleMatrix(B))
    }
    
  
   /**
     * Solve a general problem A x = L B x.
     *
     * @param A symmetric matrix A, refers to this
     * @param B symmetric matrix B
     * @return an array of matrices of length two. The first one is an array of the eigenvectors X
     *         The second one is A vector containing the corresponding eigenvalues L.
     */
def fsymmetricGeneralizedEigenvectors(B: Array[Array[Double]]) = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricGeneralizedEigenvectors(dm, new DoubleMatrix(B))
  }
  
    /**
     * Compute Cholesky decomposition of A ( this )
     * @param A should be symmetric, positive definite matrix (only upper half is used)
     * @return upper triangular matrix U such that  A = U' * U
     */
 def fcholesky() = {
   val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
   org.jblas.Decompose.cholesky(dm)
   }
   
  
/** Solves the linear equation A*X = B , A is this */
def fsolve(B: Array[Array[Double]]) = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Solve.solve(dm, new DoubleMatrix(B))
  }
  
  
/** Solves the linear equation A*X = B for symmetric A, A is this */
def fsolveSymmetric(B: Array[Array[Double]]) = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Solve.solveSymmetric(dm, new DoubleMatrix(B))
  }
  
/** Solves the linear equation A*X = B for symmetric and positive definite A */
def fsolvePositive(B: Array[Double]) = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Solve.solvePositive(dm, new DoubleMatrix(B))
  }
  
  
 /**
     * Compute a singular-value decomposition of A, A is this
     *
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
def ffullSVD() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.fullSVD(dm)
  }
  
     /**
     * Compute a singular-value decomposition of A (sparse variant), A is this
     * Sparse means that the matrices U and V are not square but
     * only have as many columns (or rows) as possible.
     * 
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
def fsparseSVD() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.sparseSVD(dm)
  }
  
def fsparseSVD( Aimag: Array[Array[Double]]) =  {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.sparseSVD(new org.jblas.ComplexDoubleMatrix(dm, new DoubleMatrix(Aimag)))
  }
  
  
  /**
     * Compute the singular values of a matrix.
     *
     * @param A DoubleMatrix of dimension m * n, A is this
     * @return A min(m, n) vector of singular values.
     */
def fSPDValues() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.SVDValues(dm)
  }
  
  
    /**
     * Compute the singular values of a complex matrix.
     *
     * @param Areal, Aimag :Areal is this,  the real and imaginary components of a  ComplexDoubleMatrix of dimension m * n
     * @return A real-valued (!) min(m, n) vector of singular values.
     */
 def fSPDValues(B: Array[Array[Double]]) = {
   val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
   org.jblas.Singular.SVDValues(new ComplexDoubleMatrix(dm, new DoubleMatrix(B)))
          }
  
 
  
}

```


# JBLAS fast routines #

`The routines that have been used above to implement Scala interfaces are based on JBLAS Java routines that we describe below.`

`JBLAS offers some useful LAPACK routines that are highly optimized and therefore for large matrices can run significantly faster than Java ones. This routines can be utilized from ScalaLab either with Array[Array[Double]] or with RichDouble2DArray types. These routines are provided with the class: ` **`scalaSci.JBLAS.JBLASNativeJavaInterface:`**

```

package scalaSci.JBLAS;

import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;

public class JBLASNativeJavaInterface {

    //  Computes the eigenvalues of a general matrix.
public static ComplexDoubleMatrix jblas_eigenvalues(double [][]dM) {
    return org.jblas.Eigen.eigenvalues(new DoubleMatrix(dM));
}


   
   //   Computes the eigenvalues and eigenvectors of a general matrix.
   //   returns an array of ComplexDoubleMatrix objects containing the eigenvectors
   //          stored as the columns of the first matrix, and the eigenvalues as the
   //         diagonal elements of the second matrix.
public static ComplexDoubleMatrix[]  jblas_eigenvectors(double [][]dM) {
    return org.jblas.Eigen.eigenvectors(new DoubleMatrix(dM));
}
     
//  Compute the eigenvalues for a symmetric matrix.
public static DoubleMatrix  jblas_symmetricEigenvalues(double [][]dM) {
    return org.jblas.Eigen.symmetricEigenvalues(new DoubleMatrix(dM));
}


//  Computes the eigenvalues and eigenvectors for a symmetric matrix.
//  returns an array of DoubleMatrix objects containing the eigenvectors
//         stored as the columns of the first matrix, and the eigenvalues as
//         diagonal elements of the second matrix.
public static DoubleMatrix []  jblas_symmetricEigenvectors(double [][]dM) {
    return org.jblas.Eigen.symmetricEigenvectors(new DoubleMatrix(dM));
}

//  Computes generalized eigenvalues of the problem A x = L B x.
// @param A symmetric Matrix A. Only the upper triangle will be considered.
//  @param B symmetric Matrix B. Only the upper triangle will be considered.
//  @return a vector of eigenvalues L.
    
public static DoubleMatrix jblas_symmetricGeneralizedEigenvalues( double [][] A, double [][] B) {
    return org.jblas.Eigen.symmetricGeneralizedEigenvalues(new DoubleMatrix(A), new DoubleMatrix(B));
}

    /**
     * Solve a general problem A x = L B x.
     *
     * @param A symmetric matrix A
     * @param B symmetric matrix B
     * @return an array of matrices of length two. The first one is an array of the eigenvectors X
     *         The second one is A vector containing the corresponding eigenvalues L.
     */
public static DoubleMatrix [] jblas_symmetricGeneralizedEigenvectors( double [][] A, double [][] B) {
    return org.jblas.Eigen.symmetricGeneralizedEigenvectors(new DoubleMatrix(A), new DoubleMatrix(B));
}

    /**
     * Compute Cholesky decomposition of A
     *
     * @param A symmetric, positive definite matrix (only upper half is used)
     * @return upper triangular matrix U such that  A = U' * U
     */
public static  DoubleMatrix  jblas_cholesky(double [][]A) {
  return org.jblas.Decompose.cholesky(new DoubleMatrix(A));
}

/** Solves the linear equation A*X = B. */
public static DoubleMatrix jblas_solve(double [][]A, double [][] B) {
    return org.jblas.Solve.solve(new DoubleMatrix(A),  new DoubleMatrix(B));
}

/** Solves the linear equation A*X = B for symmetric A. */
public static DoubleMatrix jblas_solveSymmetric(double [][]A, double [][] B) {
    return org.jblas.Solve.solveSymmetric(new DoubleMatrix(A),  new DoubleMatrix(B));
}

/** Solves the linear equation A*X = B for symmetric and positive definite A. */
public static DoubleMatrix jblas_solvePositive(double [][]A, double [][] B) {
    return org.jblas.Solve.solvePositive(new DoubleMatrix(A),  new DoubleMatrix(B));
}

 /**
     * Compute a singular-value decomposition of A.
     *
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */

public static DoubleMatrix []  jblas_fullSVD( double [][]A) {
    return org.jblas.Singular.fullSVD(new DoubleMatrix(A));
}


    /**
     * Compute a singular-value decomposition of A (sparse variant).
     * Sparse means that the matrices U and V are not square but
     * only have as many columns (or rows) as possible.
     * 
     * @param A
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */

public static DoubleMatrix []  jblas_sparseSVD( double [][]A) {
    return org.jblas.Singular.sparseSVD(new DoubleMatrix(A));
}


public static ComplexDoubleMatrix []  jblas_sparseSVD( double [][]Areal, double [][] Aimag) {
    return org.jblas.Singular.sparseSVD(
            new ComplexDoubleMatrix(new DoubleMatrix(Areal),  new DoubleMatrix(Aimag)));
}


  /**
     * Compute the singular values of a matrix.
     *
     * @param A DoubleMatrix of dimension m * n
     * @return A min(m, n) vector of singular values.
     */

public static DoubleMatrix jblas_SPDValues(double [][]A) {
    return  org.jblas.Singular.SVDValues(new DoubleMatrix(A));
}

    /**
     * Compute the singular values of a complex matrix.
     *
     * @param Areal, Aimag : the real and imaginary components of a  ComplexDoubleMatrix of dimension m * n
     * @return A real-valued (!) min(m, n) vector of singular values.
     */

public static DoubleMatrix jblas_SPDValues(double [][]Areal, double [][]Aimag) {
    return  org.jblas.Singular.SVDValues(
            new ComplexDoubleMatrix(new DoubleMatrix(Areal), new DoubleMatrix(Aimag)));
}


}

```

`An example of using them follows: `

```


var A = AAD("3.4 5.6  -3.4; 0.45 0.545 -1.3; 5.3 5.9 -2.3")  // create a RichDouble2DArray

// compute the eigenvalues using JBLAS 
var JBLASeigs = jblas_eigenvalues(A)  

// compute the eigenvectors using JBLAS
var JBLASeigsevecs = jblas_eigenvectors(A)

// compute the eigenvalues/eigenvectors  using EJML
var ejmleigs = scalaSci.EJML.StaticMathsEJML.eig(A)

// create a (large) symmetric matrix
var N = 200
var Mdd = Array.ofDim[Double](N, N)
for (r<-0 until N)
 for (c<-0 until N) {
   var denom = 1.0+r*c
   Mdd(r)(c) = 1.0/ denom
}

// compute with LAPACK specific routine for symmetric matrices the eigenvalues
tic; var JBLASsym = jblas_symmetricEigenvectors(Mdd); var tmJBLASsym = toc

// compute with LAPACK general routine
tic; var JBLASgen = jblas_eigenvectors(Mdd); var tmJBLASgen = toc

// compute with EJML library
tic; var EJMLeigs= scalaSci.EJML.StaticMathsEJML.eig(Mdd); var tmEJML= toc



```