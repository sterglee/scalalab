
package com.nr.eig

import scalaSci.RichDouble1DArray
import scalaSci.RichDouble2DArray



object  JacobiScala  {

   def Jacobi(a: Array[Array[Double]]) = {
   /**
   * Computes all eigenvalues and eigenvectors of a real symmetric matrix
   * a[0..n-1][0..n-1]. On output, jc.d[0..n-1] contains the eigenvalues of a
   * sorted into descending order, while jc.v[0..n-1][0..n-1] is a matrix whose
   * columns contain the corresponding normalized eigenvector
   */
      var jc = new com.nr.eig.Jacobi(a)
      var eigenvalues = jc.d   // the eigenvalues of a sorted into descending order
      var eigenvectors = jc.v  // a matrix whose columns contain the corresponding normalized eigenvector
      ( new RichDouble2DArray(eigenvectors), new RichDouble1DArray(eigenvalues))
   }

}

/* 
var A = RD2D(3,3,  3,  4.5, 3,  4.5, 5.6, 8.9, 3, 8.9, -4.5)

var (evecs, eigs) =  com.nr.eig.JacobiScala.Jacobi(A)

 var evec0 = evecs(::, 0)  // take first eigenvector
 var nrm = norm(evec0)  // norm should be 1 since eigenvector is normalized
 var dnrm = evec0 dot evec0  // also dot product equals to norm
 var evec1 = evecs(::, 1)  // take second eigenvector

 var shouldBeZero = evec0 dot evec1  // since eigenvectors are orthogonal this should be zero
 var fromEigDefinitionShouldBeZero = A*evec0  -  eigs(0)*evec0
 
A*evec0

   */