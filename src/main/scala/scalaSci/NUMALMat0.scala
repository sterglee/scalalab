
// simplified interface to NUMAL library for 0-indexed arrays
package scalaSci

import NUMAL._

import _root_.java.util.Vector 
import _root_.numal._ ; 
import _root_.numal.Algebraic_eval._
import _root_.numal.Analytic_eval._
import _root_.numal.Analytic_problems._
import _root_.numal.Approximation._
import _root_.numal.Basic._
import _root_.numal.FFT._
import  _root_.numal.Linear_algebra._
import _root_.numal.Special_functions._
import java.text.DecimalFormat 


object NUMALMat0 {

  // Solves a well-conditioned linear system of equations Ax = b whose order is small relative to the number 
  // of binary digits in the number representation
  def  decsol0(a: Array[Array[Double]], b: Array[Double]) =  {
    val aux = new Array[Double] (4)
    aux(2) = 1.0e-5   // relative tolrerance
    
    val a1 = AA1(a)  // convert array to 1-indexed
    val n = a.length
    
    val b1 =  A1(b)
    
    decsol(a1, n, aux, b1)    //  call the NUMAL routine
    
   var rb = new Array[Double](n)
   var r = 0
   while  (r <  n) {
      rb(r) = b1(r+1)
      r += 1
   }
      
    // prepare the output results
    
 /*    val lowerDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-0 to  r-1)
          lowerDiagonal(r)(c) = a1(r+1)(c+1)
      
    val upperDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-r to n-1)
          upperDiagonal(r)(c) = a1(r+1)(c+1)
    */
    new RichDouble1DArray(rb)
  }
  
  // Solves a linear system of equations Ax = b 
  def  gsssol0(a: Array[Array[Double]], b: Array[Double]) =  {
    val aux = new Array[Double] (8)
    aux(2) = 1.0e-5   // relative tolrerance
    aux(4) = 8 
    
    val a1 = AA1(a)  // convert array to 1-indexed
    val n = a.length
    
    val b1 =  A1(b)
    
    gsssol(a1, n, aux, b1)    //  call the NUMAL routine
    
   var rb = new Array[Double](n)
   var r = 0
   while  (r <  n)  {
      rb(r) = b1(r+1)
      r += 1
   }
      
    // prepare the output results
    
    /*val lowerDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-0 to  r-1)
          lowerDiagonal(r)(c) = a1(r+1)(c+1)
      
    val upperDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-r to n-1)
          upperDiagonal(r)(c) = a1(r+1)(c+1)
    */
    rb
  }
  
  // Solves the nXn system of equations Ax=b, and provides an upper bound
  // for the relative error in x
  def  gsssolerb0(a: Array[Array[Double]], b: Array[Double]) =  {
    val aux = new Array[Double] (12)
    aux(0) = 1.0e-14;   // the machine's precision'
    aux(2) = 1.0e-14;   //  a relative tolerance
    aux(4) = 8   // a value used for controling pivoting. usually aux(4)=8 will give good results
    aux(6) = 1.0e-14;   // an upper bound for the relative precision of the given matrix elements
    
    val a1 = AA1(a)  // convert array to 1-indexed
    val n = a.length
    
    val b1 =  A1(b)
    
    gsssolerb(a1, n, aux, b1)    //  call the NUMAL routine
    
   var rb = new Array[Double](n)
   var r = 0
   while (r <  n)  {
      rb(r) = b1(r+1)
      r += 1
   }
      
    // prepare the output results
    /*
    val lowerDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-0 to  r-1)
          lowerDiagonal(r)(c) = a1(r+1)(c+1)
      
    val upperDiagonal = Array.ofDim[Double](n, n)
    for (r<-0 until n)
       for (c<-r to n-1)
          upperDiagonal(r)(c) = a1(r+1)(c+1)
    */
    val estimatedErrorBound = aux(11)
    (new RichDouble1DArray(rb),  estimatedErrorBound)
  }
  
  // The (ordinary) eigenvalue problem
  
  // Real symmetric tridiagonal matrices
  
  // calulates all or some consecutive eigenvalues in descending order of magnitude, of a real symmetric nXn 
  // tridiagonal matrix T.
  //    def  valsymmtri( d: Array[Double], bb: Array[Double], n1: Int, n2: Int) : Array[Double]
  //       d:    the main diagonal of the symmetric tridiagonal matrix 
  //       bb:  bb: the squares of the codiagonal elements of the symmetric tridiagonal matrix
  //  returns the n2-n1+1 calculated consecutive eigenvalues in nonincreasing order
// def   valsymmtri( d: Array[Double], bb: Array[Double], n1: Int, n2: Int) : Array[Double] =  {
   
    
 }
  
  
