

package scalaSci

//   Defines and implements most common static operations that are
//   mandatory for all the scalaSci Matrix types.
//   This trait is analogous to the scalaSciMatrix trait but for static operations.
//   It defines a common functionality that is assured by all the library-specific scalaSci wrapper classes

// Note here that the many routines, e.g. sin(), delegate the call on the receiver object, e.g.
//   final def sin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sin 
// Therefore,  for each matrix library we can exploit the data representation
//  (e.g. 2-D Double array, 1-D Double array, banded representation, 
// sparse representation) in order to implement these operations effectively.

trait StaticScalaSciCommonOps[specificMatrix]   {


import StaticScalaSciCommonOps._
  
  final def size(A: scalaSciMatrix[specificMatrix]): (Int, Int) = A.size // tuple with the number of rows and columns
  final def length(A: scalaSciMatrix[specificMatrix]): Int  = A.length // total length
   
  final def abs(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.abs
  
  // trigonometric routines
  final def sin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sin 
  final def cos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.cos
  final def tan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.tan
  final def asin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.asin
  final def acos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.acos
  final def atan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.atan
  final def sinh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sinh
  final def cosh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.cosh
  final def tanh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.tanh

  // other mathematical routines
  final def pow(A: scalaSciMatrix[specificMatrix], value: Double): specificMatrix = A.pow(value)
  final def log(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.log
  final def log2(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.log2
  final def ceil(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ceil
  final def floor(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.floor
  final def round(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.round
  final def sqrt(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sqrt
  final def toDegrees(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.toDegrees
  final def toRadians(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.toRadians
    
  // parallel (i.e. multithreaded) versions
  
  // trigonometric routines
  final def psin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.psin 
  final def pcos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pcos
  final def ptan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ptan
  final def pasin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pasin
  final def pacos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pacos
  final def patan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.patan
  final def psinh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.psinh
  final def pcosh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pcosh
  final def ptanh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ptanh

  // other mathematical routines
  final def ppow(A: scalaSciMatrix[specificMatrix], value: Double): specificMatrix = A.ppow(value)
  final def pexp(A: scalaSciMatrix[specificMatrix], value: Double): specificMatrix = A.pexp()
  final def plog(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.plog
  final def plog2(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.plog2
  final def plog10(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.plog10
  final def pabs(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pabs
  final def pceil(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pceil
  final def pfloor(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pfloor
  final def pround(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.pround
  final def psqrt(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.psqrt
  final def ptoDegrees(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ptoDegrees
  final def ptoRadians(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ptoRadians
  
  
  // trigonometric routines
  final def psin(A: RichDouble2DArray) = A.psin 
  final def pcos(A: RichDouble2DArray) = A.pcos
  final def ptan(A: RichDouble2DArray) = A.ptan
  final def pasin(A: RichDouble2DArray) = A.pasin
  final def pacos(A: RichDouble2DArray) = A.pacos
  final def patan(A: RichDouble2DArray) = A.patan
  final def psinh(A: RichDouble2DArray) = A.psinh
  final def pcosh(A: RichDouble2DArray) = A.pcosh
  final def ptanh(A: RichDouble2DArray) = A.ptanh

  // other mathematical routines
  final def ppow(A: RichDouble2DArray, value: Double)= A.ppow(value)
  final def pexp(A: RichDouble2DArray, value: Double)= A.pexp()
  final def plog(A: RichDouble2DArray) = A.plog
  final def plog2(A: RichDouble2DArray) = A.plog2
  final def plog10(A: RichDouble2DArray) = A.plog10
  final def pabs(A: RichDouble2DArray) = A.pabs
  final def pceil(A: RichDouble2DArray)  = A.pceil
  final def pfloor(A: RichDouble2DArray) = A.pfloor
  final def pround(A: RichDouble2DArray) = A.pround
  final def psqrt(A: RichDouble2DArray) = A.psqrt
  final def ptoDegrees(A: RichDouble2DArray) = A.ptoDegrees
  final def ptoRadians(A: RichDouble2DArray) = A.ptoRadians
  
  
  final def dot(A: scalaSciMatrix[specificMatrix], b: scalaSciMatrix[specificMatrix]): Double = A.dot(b)
  final def cross(A: scalaSciMatrix[specificMatrix], b: scalaSciMatrix[specificMatrix]): specificMatrix = A.cross(b)
  
  
  // statistical routines
      // across columns
  final def sum(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.sum
  final def mean(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.mean
  final def prod(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.prod
  final def min(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.min
  final def max(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.max
    // across rows
  final def sumR(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.sumR
  final def meanR(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.meanR
  final def prodR(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.prodR
  final def minR(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.minR
  final def maxR(A: scalaSciMatrix[specificMatrix]): RichDouble1DArray = A.maxR 
  
  final def resample(A: scalaSciMatrix[specificMatrix], n: Int, m: Int): specificMatrix = A.resample(n, m)
  final def reshape(A: scalaSciMatrix[specificMatrix], n: Int, m: Int): specificMatrix = A.reshape(n, m)
  def correlation(A: specificMatrix, B: specificMatrix): specificMatrix
  def corr(A: specificMatrix, B: specificMatrix): specificMatrix
  def covariance(A: specificMatrix, B: specificMatrix): specificMatrix
  def cov(A: specificMatrix, B: specificMatrix): specificMatrix
  
  def LU_solve(A: specificMatrix, b: specificMatrix): specificMatrix
  
  final def inv(A: scalaSciMatrix[specificMatrix]) = A.inv    // matrix inverse
  
  final def pinv(A: scalaSciMatrix[specificMatrix]) =  A.pinv   // matrix pseudo-inverse
  
  final def  det(A: scalaSciMatrix[specificMatrix]) = A.det
  
  final def trace(A: scalaSciMatrix[specificMatrix]) = A.trace
  
  final def cond(A: scalaSciMatrix[specificMatrix]) =  A.cond
 
  final def rank(A: scalaSciMatrix[specificMatrix]) =  A.rank
  
  final def eig( A: scalaSciMatrix[specificMatrix]) = A.eig
  
  final def rref( A: scalaSciMatrix[specificMatrix]) = A.rref
  
    
/* (U,S,V) = svd() produces a diagonal matrix S, of the same 
    dimension as X and with nonnegative diagonal elements in
    decreasing order, and unitary matrices U and V so that
    X = U*S*V'.  
    */
  final def svd( A: scalaSciMatrix[specificMatrix]) = A.svd()
  
  final def T(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.T
  final def trans(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.trans


final def fromDoubleArray(x: Array[Array[Double]])  = {
  var nrows = x.length
  var ncols = x(0).length
  var  newMat =  scalaSci.MatrixFactory( this, nrows, ncols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
  var r = 0; var c = 0
  while (r < nrows) {
    c = 0
    while (c < ncols) {
      newMat(r, c) = x(r)(c)
      c += 1
    }
    r += 1
  }
  newMat.asInstanceOf[specificMatrix]
}  

  
   // create a new random generator
  final def  newRndGen(seed: Long ) = { 
    rndGen = new com.nr.ran.Ran(seed)
   }

  final def rand0(tuple: (Int, Int)): specificMatrix = {
  rand0(tuple._1, tuple._2)
}  

  
final def rand0(n: Int, m: Int) = {
    var  rndMat =  scalaSci.MatrixFactory( this, n, m).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r = 0; var c = 0
    while  (r <  n) {
      c = 0
      while (c < m) {
        rndMat(r, c) = rndGen.doub() 
        c += 1
      }
      r += 1
    }
    rndMat.asInstanceOf[specificMatrix]
  }

  final def rand0(n: Int): specificMatrix = rand0(n, n)

  final def ones0(tuple: (Int, Int)): specificMatrix = ones0(tuple._1, tuple._2)

  final def ones0(numRows: Int, numCols: Int) = {
   var ret = scalaSci.MatrixFactory(this, numRows, numCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
   var r=0; var  c=0
   while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  1.0
             c += 1
             }
          r += 1
        }
        ret.asInstanceOf[specificMatrix]
 }

final def ones0(n: Int): specificMatrix  = ones0(n, n)

final def zeros0(tuple: (Int, Int)): specificMatrix = zeros0( tuple._1, tuple._2) 
  
final def zeros0(numRows: Int, numCols: Int) = {
   var ret = scalaSci.MatrixFactory(this, numRows, numCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r=0; var  c=0
        while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  0.0
             c += 1
             }
          r += 1
        }
        ret.asInstanceOf[specificMatrix]
 }


final def zeros0(n: Int): specificMatrix = zeros0(n, n)
  
final def fill0(tuple: (Int, Int), value: Double): specificMatrix = fill0(tuple._1, tuple._2, value)

final def fill0(numRows: Int, numCols: Int, value: Double) = {
   var ret = scalaSci.MatrixFactory(this, numRows, numCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r, c=0
        while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  value
             c += 1
             }
          r += 1
        }
        ret.asInstanceOf[specificMatrix]
 }

final def fill0(n: Int, value: Double): specificMatrix = fill0(n, n, value)

final def diag0( tuple: (Int, Int)): specificMatrix = diag0(tuple._1, tuple._2)
  
final def diag0(n: Int, m: Int) = {
    var mn = n; if (m<mn) mn = m
    var ret = scalaSci.MatrixFactory(this, n, m).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r, c=0
        while (r<mn) {
          c=0
          while (c<mn) {
             ret(r, c) =  0.0
             c += 1
             }
          r += 1
        }
        for (r<-0 until mn)
          ret(r, c) = 1.0
        ret.asInstanceOf[specificMatrix]
 }
 
final def diag0(n: Int): specificMatrix =  diag0(n, n)
 
final def  diag0(x: scalaSci.Vec) = {
    var n = x.length
    var om = scalaSci.MatrixFactory(this, n, n).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var i=0;
    while  (i< n) {
      om(i, i) = x(i)
       i += 1
      }
          om.asInstanceOf[specificMatrix]
  }
  
final def diag0( x: Array[Double]) = {
    var n = x.length
    var om = scalaSci.MatrixFactory(this, n, n).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var i=0;
    while  (i< n) {
      om(i, i) = x(i)
       i += 1
      }
          om.asInstanceOf[specificMatrix]
    }
    


final def  eye0(tuple: (Int, Int)): specificMatrix = eye0(tuple._1, tuple._2)
  
final def  eye0(n:Int): specificMatrix = {
    eye0(n, n)
}

final def  eye0(n:Int, m:Int)  = {
     var om = scalaSci.MatrixFactory(this, n, n).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
     var minCoord = n
     if (m<minCoord)
        minCoord = m
     var i=0
     while  (i< minCoord) {
             om(i, i) = 1.0
              i += 1
         }
          om.asInstanceOf[specificMatrix]
     }

  
 // construct a zero-indexed Matrix from a String 
// var m = M0("3.4 -6.7; -1.2 5.6")

final def M0(s: String) =  {
    var nRows = 1
    var nCols = 0
    var i = 0
    while  (i < s.length) {   // count how many rows are specified
      if  (s(i)==';')
        nRows += 1
     i += 1
    }

  // seperate rows to an ArrayBuffer
   var buf = new scala.collection.mutable.ArrayBuffer[String]()
   var strtok = new java.util.StringTokenizer(s, ";")  // rows are separated by ';'
   while (strtok.hasMoreTokens) {
         val tok = strtok.nextToken
         buf += tok
      }    

// count how many numbers each row has. Assuming that each line has the same number of elements
 val firstLine = buf(0)
 strtok = new java.util.StringTokenizer(firstLine, ", ")  // elements are separated by ',' or ' '
 while (strtok.hasMoreTokens) {
   val tok = strtok.nextToken
   nCols += 1  
}

var numbersMatrix  = scalaSci.MatrixFactory(this, nRows, nCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
var k = 0
while  (k <  nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersMatrix(k, c) = tok.toDouble
    c += 1
     }   // read row
     k += 1
   }  // read array
   numbersMatrix.asInstanceOf[specificMatrix]
 }  

  // construct a RichDouble2DArray Matrix from a String 
// var m = M("3.4 -6.7; -1.2 5.6")

final def M(s: String) =  {
    var nRows = 1
    var nCols = 0
    var i = 0
    while  (i < s.length) {   // count how many rows are specified
      if  (s(i)==';')
        nRows += 1
     i += 1
    }

  // seperate rows to an ArrayBuffer
   var buf = new scala.collection.mutable.ArrayBuffer[String]()
   var strtok = new java.util.StringTokenizer(s, ";")  // rows are separated by ';'
   while (strtok.hasMoreTokens) {
         val tok = strtok.nextToken
         buf += tok
      }    

// count how many numbers each row has. Assuming that each line has the same number of elements
 val firstLine = buf(0)
 strtok = new java.util.StringTokenizer(firstLine, ", ")  // elements are separated by ',' or ' '
 while (strtok.hasMoreTokens) {
   val tok = strtok.nextToken
   nCols += 1  
}

var numbersMatrix  = new RichDouble2DArray(nRows, nCols)      // create a RichDouble2DArray
var k = 0
while  (k <  nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersMatrix(k, c) = tok.toDouble
    c += 1
     }   // read row
     k += 1
   }  // read array
   numbersMatrix
 }  

  
 
  
  final def toDoubleArray(A: scalaSciMatrix[specificMatrix]) =   A.toDoubleArray // returns a Java/Scala 2-D array representation of the matrix contents  

  def find(A: specificMatrix): Array[Array[Int]]
  
  
  /*
  final def eigvals(a: specificMatrix ): RichDouble1DArray   // compute the eigenvalues 
  
  final def eigvecs(a: specificMatrix ): RichDouble2DArray  // compute the eigenvectors
  final def eig(a: specificMatrix ): (RichDouble1DArray, RichDouble2DArray)  // compute both eigenvalues/eigenvectors
*/
}




object StaticScalaSciCommonOps {
  var defaultSeed = 10101L
  var  rndGen = new com.nr.ran.Ran(defaultSeed)
      }
  
