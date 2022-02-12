package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import JSci.maths.wavelet.Signal
import scalaExec.Interpreter.GlobalValues
import scalaSci.math.array.DoubleArray
import java.util._
import Jama._

// allows static methods of ScalaSci's mathematical objects (e.g. Mat) to be conveniently available,
// e.g. sin(m) instead of scalaSci.Mat.sin(m)

//  Different StaticMaths objects are used for different zero indexed Matrix types.
//  This one is for the 0-indexed  JAMA based Matrix 

// supports the JAMA based 0-indexed matrix type 

object StaticMaths  extends AnyRef with scalaSci.StaticScalaSciCommonOps[scalaSci.Mat]  with scalaSci.StaticScalaSciGlobal  {
  
  implicit final def vecToMat(x: Vec): scalaSci.Mat = { vec2Mat(x) } // implicit conversion of a Vec to Mat
  implicit final def MatToDoubleArr(x: scalaSci.Mat) = { x.getRow(0) }

    // approximates the matrix size for which multithreading becomes faster than serial implementation
    def  computeProperMatrixSizeThresholdForMultithreading = {
    
        System.gc
        var  testedSize  = 50.0  // starting value
        
        var breakFlag = false
        var m = rand(testedSize.asInstanceOf[Int], testedSize.asInstanceOf[Int])  // create a matrix to test the serial case vs the multithreaded case
        var iterCnt=0
    while (breakFlag == false)  {
            
            scalaSciCommands.BasicCommands.tic
            
            // benchmark serial multiplication
            m = m.smul(m).smul(m).smul(m).smul(m).smul(m).smul(m)   // perform a serial multiplication in order to time it
            var  delaySerial = scalaSciCommands.BasicCommands.toc
            
            // benchmark parallel multiplication
            scalaSciCommands.BasicCommands.tic()
            m = m.pmul(m).pmul(m).pmul(m).pmul(m).pmul(m).pmul(m)   // perform a parallel multiplication in order to time it
            var  delayParallel = scalaSciCommands.BasicCommands.toc();
            
     // println("parallel delay = "+delayParallel+" delaySerial = "+delaySerial)
            if (delayParallel > delaySerial)   // increment to larger size
                testedSize += testedSize;
            else breakFlag = true  // exit the loop
                  
       if (iterCnt > 5) { breakFlag = true }  // avoid many iterations
       iterCnt += 1
        }
        
         testedSize  // return size about at which parallel implementation becomes faster
        
        
    }
    

final def correlation(v1: Mat, v2: Mat): Mat = {
  var r = correlation(v1.getv, v2.getv)
  new Mat(r)
}


final def corr(v1: Mat, v2: Mat): Mat = {
  var r = corr(v1.getv, v2.getv)
  new Mat(r)
}


final def covariance(v1: Mat, v2: Mat): Mat = {
  var r = covariance(v1.getv, v2.getv)
  new Mat(r)
}


final def cov(v1: Mat, v2: Mat): Mat = {
  var r = cov(v1.getv, v2.getv)
  new Mat(r)
}

 final def cov(mt: Mat): Mat = {
     var v = mt.getv
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     new Mat(r)
 }
 

 final def covariance(mt: Mat): Mat = {
     var v = mt.getv
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     new Mat(r)
 }

 
 final def  corr(mt: Mat): Mat = {
     var v =  mt.getv
     var r = scalaSci.math.array.StatisticSample.correlation(v, v);
     new Mat(r)
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


final def toMatrix(a: Mat): Matrix = {
   scalaSci.StaticMaths.MatrixFromZeroIndexedDArr(a.getv)
}


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


  
// convert Vec to Mat
final def vec2Mat( that: Vec): Mat = {   
  var Nrows = that.length
  var rmat = new Mat(Nrows, 1)
  var k=0
  while (k<Nrows) {
     rmat(k, 0) = that(k)
     k += 1
     }
rmat
}

 

////////   scalaSci.math.scalaSci.math.LinearAlgebra.LinearAlgebra.scalaSci.math.LinearAlgebra.LinearAlgebra routines
final def CholeskyL(Mt: Mat): Mat = {
 var choleskDec = scalaSci.math.LinearAlgebra.LinearAlgebra.cholesky(Mt.v)
 var choleskMatDoubles = choleskDec.getL().getArray()
 new Mat(choleskMatDoubles)
}

final def  Cholesky_SPD(Md: Mat): Mat = {
      var CholeskyDecomposition =  scalaSci.math.LinearAlgebra.LinearAlgebra.cholesky(Md.v)
      var  choleskMatDoubles = CholeskyDecomposition.getL().getArray()
      new Mat(choleskMatDoubles)
}

final def  Cholesky_solve(Md: Mat,  b: Mat ) = {
      var   choleskDec = scalaSci.math.LinearAlgebra.LinearAlgebra.cholesky(Md.v)
      var   jb = new jMatrix(b.v)
      var   solvedMat = choleskDec.solve(jb)
      new Mat(solvedMat.getArray())
}

final def  LU_L(Mmat: Mat):Mat = {
    new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.LU( Mmat.v).getL().getArray())
}

final def  L(Mmat: Mat): Mat = {
   LU_L( Mmat)
}


final def  LU_U(Mmat: Mat): Mat = {
   new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.LU( Mmat.v).getU().getArray())
}

final def U(Mmat: Mat): Mat = {
   LU_U( Mmat)
 }

final def LU_det(Mmat: Mat) = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.LU( Mmat.v).det()
}

final def LU(A: Mat):(Mat, Mat, Mat) = { 
  val LUdecomp = scalaSci.math.LinearAlgebra.LinearAlgebra.LU( A.v)   // perform the LU decomposition
  var L =  new Mat(LUdecomp.getL().getArray())
  var U =  new Mat(LUdecomp.getU().getArray())
  var P = new Mat(A.Nrows, A.Nrows)
  var p = LUdecomp.getPivot()
 // construct permutation matrix
    for (k<-0 until A.Nrows)  
      P(k, p(k)) = 1
 
  (L, U, P)
 } 


final def  det(Mmat: Mat) = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.det(Mmat.v)
}

final def  LU_solve(A:Mat,  b: Mat): Mat = {
  
    var  luA = new com.nr.la.LUdcmp(A.v)
    var   res = Array.ofDim[Double](b.Nrows, b.Ncols)
    luA.solve(b.v, res)
    
    new Mat(res)
}



final def  QR_H(Mmat: Mat): Mat = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.QR( Mmat.v).getH())
}

final def  QR_Q(Mmat: Mat): Mat = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.QR( Mmat.v).getQ())
}

final def  Q(Mmat: Mat): Mat = {
    QR_Q( Mmat)
 }

final def   QR_R(Mmat: Mat) = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.QR( Mmat.v).getR())
}

final def  R(Mmat: Mat) =  {
   QR_R( Mmat)
 }

final def QR(Mmat: Mat) = {
  (Q(Mmat), R(Mmat))
}

final def  qr(Mmat: Mat) = QR(Mmat)
  
final def  QR_solve(A: Mat, b: Mat) = {
  var  QRDec = scalaSci.math.LinearAlgebra.LinearAlgebra.QR(A.v)
  var  solvedMat = QRDec.solve(new Jama.jMatrix(b.v))
  new Mat(solvedMat)
}

final def solve(A: Mat,  b: Mat) = {
    var  luA = new com.nr.la.LUdcmp(A.v)
    var   res = Array.ofDim[Double](b.Nrows, b.Ncols)
    luA.solve(b.v, res)
    
    new Mat(res)
}

final def Singular_cond(Mmat: Mat): Double = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.singular( Mmat.v).cond()
}

final def cond(Mmat: Mat): Double = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.cond( Mmat.v)
}

final def  Singular_S(Mmat: Mat): Mat = {
   new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.singular(Mmat.v).getS().getArray())
}

final def  S(Mmat: Mat): Mat = {
  Singular_S( Mmat)
}

final def  Singular_values(Mmat: Mat): Mat = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.singular(Mmat.v).getSingularValues())
}

final def  Singular_U(Mmat: Mat): Mat = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.singular(Mmat.v).getU().getArray())
}

final def   Singular_V(Mmat: Mat): Mat = {
  new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.singular(Mmat.v).getV().getArray())
}

final def Singular_norm2(Mmat: Mat): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.singular( Mmat.v).norm2()
}

final def norm2(Mmat: Mat): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.norm2( Mmat.v)
}

final def  norm1(Mmat: Mat): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.norm1( Mmat.v)
}

final def  normF(Mmat: Mat): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.normF( Mmat.v)
}

final def  normInf(Mmat: Mat): Double = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.normInf( Mmat.v)
}

final def  Singular_rank(Mmat: Mat): Int = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.singular( Mmat.v).rank()
}

//    var ([U,S,V) = svd(X) produces a diagonal matrix S, of the same 
//    dimension as X and with nonnegative diagonal elements in
//     decreasing order, and unitary matrices U and V so that    X = U*S*V'.
 final def svdJama(X: Mat) = {
  // perform the Singular Value Decomposition 
   
  val svdM = new Jama.JamaSingularValueDecomposition(new Jama.jMatrix(X.getv))
  val U = svdM.getU.getArray
  val V = svdM.getV.getArray
  val s = svdM.getSingularValues
  val Nrows = s.length
  val S = Array.ofDim[Double](Nrows, Nrows)
  for (k<-0 until Nrows)
     S(k)(k) = s(k)
  (U, S, V)

}

  
final def svd(X: Mat) = X.svd()

final def  rank(Mmat: Mat): Int = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.rank( Mmat.v)
}

final def trace(Mmat: Mat): Double = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.trace( Mmat.v)
}

final def Eigen_V(Mmat: Mat): Mat = {
   new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.eigen(Mmat.v).getV())
}

final def V(Mmat: Mat): Mat = {
   Eigen_V( Mmat)
 }

final def eigvecs(Mmat: Mat): RichDouble2DArray = {
  new RichDouble2DArray(scalaSci.math.LinearAlgebra.LinearAlgebra.eigen(Mmat.v).getV())
}  

final def  Eigen_D(Mmat: Mat) = {
   new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.eigen(Mmat.v).getD())
}

final def  D(Mmat: Mat):Mat = {
   Eigen_D( Mmat)
}
// return eigenvectors, eigenvalues
final def eig(Mmat: Mat) = Mmat.eig()


  
final def eigvals(Mmat: Mat): RichDouble1DArray = {
  var dd = scalaSci.math.LinearAlgebra.LinearAlgebra.eigen(Mmat.v).getD()
  var len = dd.length
  var d = new Array[Double](len)
  var k=0
  while (k<len) {
    d(k) = dd(k)(k)
    k += 1
  }
  new RichDouble1DArray(d)
}  


  
  final def leig(Mmat: Mat) = {
    var realEvs = new Array[Double](Mmat.Nrows)
    var imEvs = new Array[Double](Mmat.Nrows)
    var realEvecs = Array.ofDim[Double](Mmat.Nrows, Mmat.Ncols)
    var imEvecs = Array.ofDim[Double](Mmat.Nrows, Mmat.Ncols)
    scalaSci.ILapack.Eig(Mmat.getv)
  }

final def inv(Mmat: Mat): Mat = {
    var jm = new Jama.jMatrix (Mmat.v)
    var invm = jm.inverse
    new Mat(invm.getArray)
}

 
 

final def inv(Mmat: Array[Array[Double]]): Array[Array[Double]]  = {
    var jm = new Jama.jMatrix (Mmat)
    var invm = jm.inverse
    invm.getArray
}


 

// construct a Mat from a double [][] array, one indexed
final def MatFromOneIndexedDArr(vals: Array[Array[Double]]): Mat = {
       var vals0 = Array.ofDim[Double](vals.length-1, vals(0).length-1)
    var i=1; var j=1;
    while (i<vals.length) {
        j=1
        while (j<vals(0).length) {
       vals0(i-1)(j-1) = vals(i)(j)
       j+=1
        }
        i+=1
     }
     new Mat(vals0)
 }




final def testMat(Nrows: Int, Ncols: Int) = {
  var a = new Mat(Nrows, Ncols)
  for (rows <- 0 until Nrows)
    for (cols <-0 until Ncols)
      a(rows, cols) = rows*10+cols

  a
}

  


final def find(M: Mat) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   for (xi <- 0 until n)
     for (yi <- 0 until  m)
        if (M(xi, yi) != 0.0)
           no+=1
  
  // build return vector90
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
