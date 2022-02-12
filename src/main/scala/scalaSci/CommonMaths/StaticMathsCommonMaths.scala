
package scalaSci.CommonMaths


import scala.language.postfixOps
import scalaExec.Interpreter.GlobalValues
import scalaSci.Matrix
import scalaSci.RichDouble2DArray
import scalaSci.RichNumber
import scalaSci.RichDouble1DArray
import scalaSci.Vec

import scalaSci.math.LinearAlgebra.LinearAlgebra
import JSci.maths.wavelet.Signal

import Jama.jMatrix
import org.apache.commons.math3.linear._
import scalaSci.math.array.DoubleArray


// allows static methods of ScalaSci's mathematical objects (e.g. Mat) to be conveniently available,
// e.g. sin(m) instead of scalaSci.CommonMaths,Mat.sin(m)

//  Different StaticMaths objects are used for different zero indexed Matrix types.
//  This one is for the 0-indexed  Appache Commons  based Matrix 


object StaticMathsCommonMaths  extends AnyRef with scalaSci.StaticScalaSciGlobal 
                           with scalaSci.StaticScalaSciCommonOps[scalaSci.CommonMaths.Mat] {
   
    


final def LU_solve(A: Mat, b: Mat) = { 
    println("LU unimplemented currently with Apache Commons. ?Use RichDoubleDouble Array")
    
    new Mat(1,1)
  
}

  
 final  def solve(A: Mat, b: Mat) = A.solve(b)
 final  def  / (A: Mat, b: Mat) = A.solve(b)

  
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



  
  // SOSSSOS - from Mat
  
  final def svdCommonMaths(a: Mat) =  {
  var svdObj = new  SingularValueDecomposition(a.rm)
  
    var U = svdObj.getU().getData
    var V = svdObj.getV().getData
    var S =  svdObj.getV().getData
    
    (S, U, V )
  
}

  final def svd(a: Mat) = a.svd()
  
  
  
  // Computes the Frobenius norm. This implementation is overflow resistant
 final def  normF(a: Mat) = {
     var  scale = 0.0
     var  ssq = 1.0
     var r = 0
     while (r < a.Nrows) {
       var c = 0
       while  (c < a.Ncols) {
            var Aval = a(r, c)
            if (Aval != 0.0) {
                var absxi = java.lang.Math.abs(Aval)
                if (scale < absxi) {
                    ssq = 1 + ssq * java.lang.Math.pow(scale / absxi, 2)
                    scale = absxi
                } else
                    ssq = ssq + java.lang.Math.pow(absxi / scale, 2)
            }
            c += 1
        }
        r += 1
     }
         scale * java.lang.Math.sqrt(ssq)
    
 }
 
  // Computes the infinity norm
 final def  normInf(a: Mat) = {
     var  columnSum = new Array[Double](a.Ncols)
     var r = 0
     while  (r < a.Nrows) {
       var c = 0
       while  (c < a.Ncols) {
         columnSum(c) += java.lang.Math.abs(a(r,c))
         c += 1
        }
        r += 1
       }
       var mx = columnSum(0)
       var c = 1
       while  (c < a.Ncols) {
         if (columnSum(c) > mx)
           mx = columnSum(c)
         c += 1
       }
       
  mx  
 }
 
final def norm2(a: Mat)  = {
  var aa = Array.ofDim[Double](a.Nrows, a.Ncols)
  var r = 0
  while  (r < a.Nrows) {
     var c = 0
     while  (c < a.Ncols) {
      aa(r)(c) = a(r, c)
      c += 1
     }
     r += 1
  }
  var aard = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.norm2(aard)
}  

  
 final def det(a: Mat) = {
   var aa = Array.ofDim[Double](a.Nrows, a.Ncols)
   var r = 0
   while  (r < a.Nrows) {
     var c = 0
     while  (c < a.Ncols) {
      aa(r)(c) = a(r, c)
      c += 1
     }
     r += 1
   }
  var rdaa = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.det(rdaa)
  }
  

  
 final def inv(a: Mat) = {
    a.inv()
  }
   
  
final def pinv(m: scalaSci.CommonMaths.Mat): scalaSci.CommonMaths.Mat = {
  m.pinv()
}  

  
 final def norm1(a: Mat) = {
  var colSum = new Array[Double](a.Ncols)   
  var c = 0 
  while  (c < a.Ncols) {
       var r = 0
       while  (r < a.Nrows) {
         colSum(c) += java.lang.Math.abs(a(r,c))
         r += 1
        }
        c += 1
       }
     var mx = colSum(0)
     c = 1
     while  (c < a.Ncols)  {
         if (colSum(c) > mx)
           mx = colSum(c)
         c += 1
     }
       
  mx  
 }
 
   
final def find(M: Mat) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   var xi = 0
   while (xi < n) {
     var yi = 0
     while (yi < m) {
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
  while  (col < m)  {
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

//   compute eigenvalues/eigenvectors using EJML
final def eig(A: Mat) = {
  var ejmlA = new scalaSci.EJML.Mat(A.getv)
  scalaSci.EJML.StaticMathsEJML.eig(ejmlA)
}

}
  
  










