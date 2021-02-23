package scalaSci.JBLAS

import java.util._
import Jama._

import org.jblas.DoubleMatrix._ 
import org.jblas.DoubleMatrix
import org.jblas.ComplexDoubleMatrix


import scala.language.postfixOps
import scalaSci.Vec
import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray


// allows static methods of ScalaSci's mathematical objects (e.g. Mat) to be conveniently available,
// e.g. sin(m) instead of scalaSci.JBLAS.Mat.sin(m)

//  Different StaticMaths objects are used for different zero indexed Matrix types.
//  This one is for the 0-indexed  JBLAS  based Matrix 

object StaticMathsJBLAS  extends scalaSci.StaticScalaSciCommonOps[scalaSci.JBLAS.Mat] with scalaSci.StaticScalaSciGlobal    
                               {

//  ??? final def norm1(a: Mat) =  { scalaSci.Mat.norm1(a) }

// ??? final def norm2(a: Mat) =  { scalaSci.Mat.norm2(a) }

// ??? final def normF(a: Mat) =  { scalaSci.Mat.normF(a) }


// ??? final def normInf(a: Mat) =  { scalaSci.Mat.normInf(a) }


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

  

    /////////////////  Mat routines adapted from JAMA
/*final def CholeskyL(Mt: Mat): Mat = {
     scalaSci.Mat.CholeskyL(Mt)
}

final def  Cholesky_SPD(Md: Mat): Mat = {
    scalaSci.Mat.Cholesky_SPD(Md)
}

final def  Cholesky_solve(Md: Mat,  b: Mat ) = {
    scalaSci.Mat.Cholesky_solve(Md, b)
 }

final def  LU_L(M: Mat):Mat = {
    scalaSci.Mat.LU_L(M)
}

final def  L(M: Mat): Mat = {
   scalaSci.Mat.L(M)
}


final def  LU_U(M: Mat): Mat = {
   scalaSci.Mat.LU_U(M)
}

final def U(M: Mat): Mat = {
   scalaSci.Mat.U(M)
 }

final def LU_det(M: Mat) = {
   scalaSci.Mat.LU_det(M)
}

final def  det(M: Mat) = {
  scalaSci.Mat.det(M)
}


final def  LU_solve(A:Mat,  b: Mat): Mat = {
    scalaSci.Mat.LU_solve(A, b)
}

final def  QR_H(M: Mat): Mat = {
    scalaSci.Mat.QR_H(M)
}

final def  QR_Q(M: Mat): Mat = {
    scalaSci.Mat.QR_Q(M)
}

final def  Q(M: Mat): Mat = {
    scalaSci.Mat.Q(M)
 }

final def   QR_R(M: Mat) = {
  scalaSci.Mat.QR_R(M)
}

final def  R(M: Mat) =  {
   scalaSci.Mat.R(M)
 }

final def  QR_solve(A: Mat, b: Mat) = {
    scalaSci.Mat.QR_solve(A, b)
 }

final def solve(A: Mat,  b: Mat) = {
    scalaSci.Mat.solve(A, b)
}

final def Singular_cond(M: Mat): Double = {
    scalaSci.Mat.Singular_cond(M)
}


final def  Singular_S(M: Mat): Mat = {
    scalaSci.Mat.Singular_S(M)
}

final def  S(M: Mat): Mat = {
  scalaSci.Mat.S(M)
}

final def  Singular_values(M: Mat): Mat = {
    scalaSci.Mat.Singular_values(M)
}

final def  Singular_U(M: Mat): Mat = {
    scalaSci.Mat.Singular_U(M)
}

final def   Singular_V(M: Mat): Mat = {
    scalaSci.Mat.Singular_V(M)
}

final def Singular_norm2(M: Mat): Double = {
    scalaSci.Mat.Singular_norm2(M)
}

final def  Singular_rank(M: Mat): Int = {
    scalaSci.Mat.Singular_rank(M)
}


final def  rank(M: Mat): Int = {
  scalaSci.Mat.rank(M)
}

final def trace(M: Mat): Double = {
    scalaSci.Mat.trace(M)
}

final def eigV(M: Mat): Mat = {
    scalaSci.Mat.Eigen_V(M)
}

final def V(M: Mat): Mat = {
   scalaSci.Mat.V(M)
 }

final def  eigD(M: Mat) = {
    scalaSci.Mat.Eigen_D(M)
}

final def  D(M: Mat):Mat = {
   scalaSci.Mat.D(M)
}

final def inv(m: Mat): Mat = {
    scalaSci.Mat.inv(m)
}

final def svd(a: Mat) = {
  scalaSci.Mat.svd(a)
}

final def eig(m: Mat) = {
   scalaSci.Mat.eig(m)
}

 final def leig(m: Mat) = {
   scalaSci.Mat.leig(m)   
 }
 
 final def  toMat(a: Matrix): Mat = {
    scalaSci.Mat.MatFromOneIndexedDArr(a.getv)
}

final def toMatrix(a: Mat): Matrix = {
   scalaSci.Matrix.MatrixFromZeroIndexedDArr(a.getv)
}

final def LU(a: Mat):(Mat, Mat, Mat) =  scalaSci.Mat.LU(a)

final def LU(a: Matrix):(Matrix, Matrix, Matrix) = scalaSci.Matrix.LU(a)


final def lu(a: Mat):(Mat, Mat, Mat) = scalaSci.Mat.LU(a)

final def QR(a: Mat) = scalaSci.Mat.QR(a)





final def norm(a:Mat, normType: Integer) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}

*/

     
  final def LU_solve(A: Mat, b: Mat) = { 
        println("method not implemented yet with JBLAS. Use RichDouble2DArray instead"); 
   new Mat(1,1) }  // Sterg-TODO
     
// SOSSOS - from Mat
  // construct a Matrix from a String 
// var m = M("3.4 -6.7; -1.2 5.6")


  final def randn(rows: Int, columns: Int) = {
   new Mat(DoubleMatrix.randn(rows, columns))
  }
  
 final def svd(m: Mat) = m.svd()  
  
  
  /*
  
  var a = scalaSci.JBLAS.StaticMathsJBLAS.rand0(2200, 2300)-0.5  
  tic
  var a1 = scalaSci.JBLAS.Mat.abs(a)
  var tm2D = toc
  
  tic
  var a2 = scalaSci.JBLAS.Mat.abs2(a)
  var tm1D = toc
  
  tic
  var a3 = scalaSci.JBLAS.Mat.abs3(a)
  var tm2DAbs = toc
  
 
    tic
  var alog = scalaSci.JBLAS.Mat.log(a)
  var tmlog2D = toc
 
   
    tic
  var alog1D = scalaSci.JBLAS.Mat.log1D(a)
  var tmlog1D = toc
 
   println("2-D  indexing Math.abs function time = "+tm2DAbs)
 println("1-D indexing time = "+tm1D)
  println("2-D indexing time = "+tm2D)
  println("2-D indexing log = "+tmlog2D)
  println("1-D indexing log = "+tmlog1D)
  
  */
  

  // compute and return the eigenvalues
final def eigenvalues(v: Mat) =  org.jblas.Eigen.eigenvalues(v.dm)

 // compute and return the eigenvectors
final def eigenvectors(v: Mat) = org.jblas.Eigen.eigenvectors(v.dm)
 

  

final def find(M: Mat) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   var xi = 0
  while (xi < n) {
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
  while  (col < m)  {
    var row = 0
    while  (row <   n)  {
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

  
  /*
   
var a = rand0(5,5)
var b = rand0(5,1)
var x = solve(a,b)
var residual = a*x-b   // should be zero
   */
  
  final def solve(A: Mat,  b: Mat) = {
   new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.solve( A.toDoubleArray, b.toDoubleArray))
}

  /*final def  solve(A: Mat, b: Mat): Mat = {
  val  da = A.dm
    val db = b.dm
    val ipiv = new Array[Int](A.numRows)
    val x = org.jblas.SimpleBlas.gesv(da, ipiv, db)
    new Mat(x)
    
    }*/
  
  // use EJML to return eigenvalues/eigenvectors in standard form
  final def eig(A: Mat) =  A.eig()
    
  

  
}
