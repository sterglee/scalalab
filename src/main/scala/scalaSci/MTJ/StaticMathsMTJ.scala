
package scalaSci.MTJ

import scalaExec.Interpreter.GlobalValues
import scalaSci.Matrix
import scalaSci.RichDouble2DArray
import scalaSci.RichNumber
import scalaSci.RichDouble1DArray
import scalaSci.Vec
import JSci.maths.wavelet.Signal
import java.util.Random
import Jama._

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray

import  no.uib.cipr.matrix._ 


import scala.language.postfixOps

// allows static methods of ScalaSci's mathematical objects (e.g. Mat) to be conveniently available,
// e.g. sin(m) instead of scalaSci.MTJ.Mat.sin(m)

//  Different StaticMaths objects are used for different zero indexed Matrix types.
//  This one is for the 0-indexed  MTJ  based Matrix 


object StaticMathsMTJ extends scalaSci.StaticScalaSciCommonOps[scalaSci.MTJ.Mat]  with scalaSci.StaticScalaSciGlobal     {
  
  def rank(A: scalaSci.MTJ.Mat): Int = A.rank()
  def trace(A: scalaSci.MTJ.Mat): Double = A.trace()


def inv(M: Mat) = M.inv()
    
def pinv(M: Mat) = M.pinv()

def lu(a: Mat) = a.lu
def LU(a: Mat) = a.LU
def ql(a: Mat) = a.ql
def QL(a: Mat) = a.QL
def qr(a: Mat) = a.qr
def QR(a: Mat) = a.QR
def rq(a: Mat) = a.rq
def RQ(a: Mat) = a.RQ
  

final def cov(v1: Mat, v2: Mat): Mat = {
  var r = cov(v1.getv, v2.getv)
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
     var r = scalaSci.math.array.StatisticSample.correlation(mt1.getv, mt2.getv);
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

  def LU_solve(A: Mat, b: Mat) = { new Mat(1,1) }  // Sterg-TODO
  
  final def solve(A: Mat,  b: Mat) = {
    new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.solve( A.toDoubleArray, b.toDoubleArray))
}


// SOSSOS - from Mat
def   random(numRows: Int, numCols: Int) = {
       var  ran = com.nr.test.NRTestUtil.ran  // global ranno generator
       var cnt = 0;
       val len = numRows*numCols
       val newData = new Array[Double](len)
       while (cnt<len) {
          newData(cnt) = scalaSci.StaticScalaSciCommonOps.rndGen.doub()
          cnt += 1
      }
   new Mat(new DenseMatrix(newData, numRows,  numCols))
   }
  
  
  
  
 // compute the eigenvalue decomposition of general matrix Mat 
def  eigAll(m: Mat) = {
    // compute the eigenvalue decompostion by calling a convenience method for computing the 
    // complete eigenvalue decomposition of the given matrix
    // allocate an EVD object. This EVD object in turn allocates all the necessary space to
   // perform the eigendecomposition, and to keep the results, i.e. the real and imaginary 
   // parts of the eigenvalues and the left and right eigenvectors
   var evdObj = no.uib.cipr.matrix.EVD.factorize(m.getDM)
  
    (evdObj.getRealEigenvalues(), evdObj.getImaginaryEigenvalues(),
     new Mat(evdObj.getLeftEigenvectors()),  new Mat(evdObj.getRightEigenvectors()))
    
  }  
  
def  eig(m: Mat) = m.eig() 

  
  def  eigLR(m: Mat) = {
    // compute the eigenvalue decompostion by calling a convenience method for computing the 
    // complete eigenvalue decomposition of the given matrix
    // allocate an EVD object. This EVD object in turn allocates all the necessary space to
   // perform the eigendecomposition, and to keep the results, i.e. the real and imaginary 
   // parts of the eigenvalues and the left and right eigenvectors
   var evdObj = no.uib.cipr.matrix.EVD.factorize(m.getDM)
  
    ( new Mat(evdObj.getLeftEigenvectors()),  new Mat(evdObj.getRightEigenvectors()), evdObj.getRealEigenvalues())
    
  }  

  
  // compute the eigenvalue decomposition of a symmetrical, banded matrix
  // kd: number of diagonals to extract
def sbeig(m: Mat, kd: Int) = {
   var sbevdObj = no.uib.cipr.matrix.SymmBandEVD.factorize(m.getDM, kd)
  
     (sbevdObj.getEigenvalues(),   new Mat(sbevdObj.getEigenvectors()))
     
}  
  
 // compute the eigenvalue decomposition of a symmetrical matrix
  // kd: number of diagonals to extract
def seig(m: Mat) = {
   var sevdObj = no.uib.cipr.matrix.SymmDenseEVD.factorize(m.getDM)
  
     (sevdObj.getEigenvalues(),   new Mat(sevdObj.getEigenvectors()))
     
}  
  
def svd(m: Mat) = m.svd()

  // computes the singular value decomposition of general matrix Mat
 def  svdMTJ(m: Mat)    = {
   // compute the singular value decomposition of the matrix m by calling a convenience method 
   var svdObj = no.uib.cipr.matrix.SVD.factorize(m.getDM)

    // returns a tuple with:
    //     1.   singular values (stored in descending order)
    //     2.   the left singular vectors, column-wise
    //     3.   the right singular vectors, row-wise
    (svdObj.getS,  new Mat(svdObj.getU), new Mat(svdObj.getVt) )
      
 }
   
 def norm1(a: Mat) = {
  var  ta = (a~)
  ta.dm.norm1
}  

 def norm2(a: Mat)  = {
  var aa = Array.ofDim[Double](a.Nrows, a.Ncols)
  for (r<-0 until a.Nrows)
    for (c<-0 until a.Ncols)
      aa(r)(c) = a(r, c)
  var rdaa = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.norm2(rdaa)
}  

  def det(a: Mat) = {
   var aa = Array.ofDim[Double](a.Nrows, a.Ncols)
  for (r<-0 until a.Nrows)
    for (c<-0 until a.Ncols)
      aa(r)(c) = a(r, c)
  var rdaa = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.det(rdaa)
  }
  
  
  
    
  
// Computes the Frobenius normal of the matrix:
def  normF(a: Mat) = {
      a.dm.normF
    }

 def normInf(a: Mat) = {
   // Infinity norm is implemented as  maximum column  sum, transpose to use xamimum row sum
   var aa = (a~)
   aa.dm.normInf
  }


// LAPACK related routines
  def Leig(a: Array[Array[Double]]) = scalaSci.ILapack.Eig(a)
  
  

def find(M: Mat) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   for (xi <- 0 until n)
     for (yi <- 0 until  m)
        if (M(xi, yi) != 0.0)
           no+=1
  
  // build return vector
  var   indices = Array.ofDim[Int](no, 3)
  var  i = 0
  for (col <- 0 until m)
    for  (row <- 0  until  n)
    if (M(row, col) != 0.0)
         {
  // nonzero element found
  // put element position into return column vector
    indices(i)(0) = row + col*n
    indices(i)(1) = row
    indices(i)(2) = col
    i += 1
  }
 
   indices 
}

  
}


      
      
      









