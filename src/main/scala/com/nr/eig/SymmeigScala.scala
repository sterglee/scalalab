
package com.nr.eig

object SymmeigScala {


/**
 * Computes all eigenvalues and eigenvectors of a real symmetric matrix by
 * reduction to tridiagonal form followed by QL iteration.
 */

  def  symmeig(a: Array[Array[Double]],  yesvec: Boolean) = {
  
  /**
   * Computes all eigenvalues and eigenvectors of a real symmetric matrix
   * a[0..n-1][0..n-1] by reduction to tridiagonal form followed by QL
   * iteration. On output, se.d[0..n-1] contains the eigenvalues of a sorted into
   * descending order, while se.z[0..n-1][0..n-1] is a matrix whose columns contain
   * the corresponding normalized eigenvectors. If yesvecs is input as true (the
   * default), then the eigenvectors are computed. If yesvecs is input as false,
   * only the eigenvalues are computed.
   */
    val  se = new Symmeig(a,yesvec)
    val  eigenvalues = se.d
    val  eigenvectors = se.z
    ( eigenvectors, eigenvalues)
  }
  
  /**
   * Computes all eigenvalues and (optionally) eigenvectors of a real,
   * symmetric, tridiagonal matrix by QL iteration. On input, dd[0..n-1]
   * contains the diagonal elements of the tridi- agonal matrix. The vector
   * ee[0..n-1] inputs the subdiagonal elements of the tridiagonal matrix, with
   * ee[0] arbitrary. Output is the same as the constructor above.
   */
 
  def symmeig(dd: Array[Double],  ee: Array[Double]) = {
    val se = new Symmeig(dd, ee)
    val  eigenvalues = se.d
    val  eigenvectors = se.z
    (eigenvectors, eigenvalues)
  }
      
  
  def symmeig(dd: Array[Double],  ee: Array[Double], yesvec: Boolean) = {
    val se = new Symmeig(dd, ee, yesvec)
    val  eigenvalues = se.d
    val  eigenvectors = se.z
    (eigenvectors, eigenvalues )
  }
  

}
