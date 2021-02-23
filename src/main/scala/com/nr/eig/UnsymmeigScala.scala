
package com.nr.eig
import com.nr.Complex;

/**
 * Computes all eigenvalues and eigenvectors of a real nonsymmetric matrix by
 * reduction to Hessenberg form followed by QR iteration. - PTC
 */

  /**
   * Computes all eigenvalues and (optionally) eigenvectors of a real
   * nonsymmetric matrix a[0..n-1][0..n-1] by reduction to Hessenberg form
   * followed by QR iteration. If yesvecs is input as true (the default), then
   * the eigenvectors are computed. Otherwise, only the eigenvalues are
   * computed. If hessen is input as false (the default), the matrix is first
   * reduced to Hessenberg form. Otherwise it is assumed that the matrix is
   * already in Hessenberg from. On output, wri[0..n-1] contains the
   * eigenvalues of a sorted into descending order, while zz[0..n-1][0..n-1] is
   * a matrix whose columns contain the corresponding eigenvectors. For a
   * complex eigenvalue, only the eigenvector corresponding to the eigen- value
   * with a positive imaginary part is stored, with the real part in
   * zz[0..n-1][i] and the imaginary part in h.zz[0..n-1][i+1]. The eigenvectors
   * are not normalized.
   */ 
/* Example

import com.nr.eig.UnsymmeigScala._
var x = rand(10,10)
var (evecs, evals) = unsymmeig(x)
 */
object UnsymmeigScala {
  def  unsymmeig(aa: Array[Array[Double]], yesvec: Boolean = true, hesenb: Boolean = false) = {
    val unseig = new Unsymmeig(aa, yesvec, hesenb)
    val eigenvalues = unseig.wri
    val eigenvectors = unseig.zz
    (eigenvectors, eigenvalues )
  }
  
  def unsymmeig(aa: Array[Array[Double]]) = {
    val unseig = new Unsymmeig(aa, true, false)
    val eigenvalues = unseig.wri
    val eigenvectors = unseig.zz
    (eigenvectors, eigenvalues )
    
  }
}