
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
