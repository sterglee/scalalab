
package scalaSci

import scalaSci.math.array.DoubleArray
import java.util._
import Jama._
import scalaSci.math.LinearAlgebra.LinearAlgebra
import JSci.maths.wavelet.Signal
import scalaExec.Interpreter.GlobalValues

import scalaSci.MTJ.MTJBandMat


import org.apache.commons.math.linear.RealMatrix

import  org.apache.commons.math3.linear.SingularValueDecomposition
import  org.apache.commons.math3.linear.Array2DRowRealMatrix
import  org.apache.commons.math3.linear.ArrayRealVector

import no.uib.cipr.matrix.DenseMatrix
import org.ejml.data.DenseMatrix64F

/*
 
 This important trait defines some global operations available in ScalaSci, 
 independently of the library that the interpreter uses.
 
 The basic problem that this trait solves is to import properly overloaded versions of a method
 For example the method  sin()  has some fixed signatures independent of the particular library,
 as for example:
    sin(x: Double), sin(x: Array[Double]), sin(x: Array[Array[Double]]), sin(x: Mat), sin(x: Matrix),  
    sin(x: Vec), sin(x: RichDouble2DArray), sin(x: RichDouble1DArray)
 (of course we can extend the list with other matrix types, for example sparse matrices).
 
 All the Scala Objects for Static Math Operations (SOSMOs) should mixin this trait in order to have common 
 functionality.  Additionally, each SOSMO should implement its  library specific routines.
 For example the StaticMathsEJML object,  by mixing the StaticScalaSciGlobal trait,
 acquires all the implementations of the sin() for the library independent types.
 Then it implements the sin() for the EJML matrix type as:
   final def sin(x: Mat) = {
      scalaSci.EJML.Mat.sin(x)
 }
 
 IMPORTANT: 
   New import statements can overwrite symbols with the same name acquired from previous 
   imports. Thefore usually imports of the SOSMOs should be placed after objects containing
   symbols with the same names.
   For example, if we write:
      import  scalaSci.EJML.StaticMathsEJML._
      import scalaSci.EJML.Mat._

 Suppose that we have  defined sin(x: Double) also in scalaSci.EJML.Mat
 
  Then the statement:
   var  x = sin(3.4)  
 will fail,   
  while the statements:
      var g = scalaSci.EJML.StaticMathsEJML.rand0(2,3)  // g is EJML matrix
      sin(g)
  succeds.
  
  
 The problem is that the second import statement (i.e. import scalaSci.EJML.Mat._)
 imports the sin() from the scalaSci.EJML.Mat class that is defined only 
 for scalaSci.EJML.Mat,
 and overwrites the sin() from the scalaSci.EJML.StaticMathsEJML,
 that defines many more overloaded versions of sin().
 
 So, the proper order of the imports is:
    import scalaSci.EJML.Mat._
    import  scalaSci.EJML.StaticMathsEJML._
       
The former works in the Scala Interpreter that overlaps import final definitions but 
unfortenately fails with a stand alone compilation of the script.  
  
For thar reason at the updated version of ScalaLab we have avoided carefully having duplicate final definitions. 
*/

trait  StaticScalaSciGlobal {
  // some type aliases for convenient code writing
     type AD = Array[Double]
     type AAD = Array[Array[Double]]
     type AI = Array[Int]
   
     type RDDA = scalaSci.RichDouble2DArray
     type RDA = scalaSci.RichDouble1DArray
     
           
     val PI = java.lang.Math.PI
     
  // final define implicit conversions
   
     implicit final def doubleArrayToVec(x: Array[Double])  = new Vec(x)
     
     implicit final def vecToMatrix(x: Vec): Matrix = { vec2Matrix(x) } // implicit conversion of a Vec to Matrix

     implicit final def DoubleToRichNumber(x: Double) = {  new RichNumber(x)}  // implicit conversion of a Double number to RichNumber
     implicit final def IntToRichNumber(x: Int) =  { new RichNumber(x)  }  // implicit conversion of a Int number to RichNumber
     implicit final def ShortToRichNumber(x: Short) = { new RichNumber(x) }   // implicit conversion of a Short number to RichNumber
     implicit final def LongToRichNumber(x: Long) = { new RichNumber(x)  }   // implicit conversion of a Long number to RichNumber

    implicit final def DoubleArrayToRichDouble1DArray(x: Array[Double]) = {  new RichDouble1DArray(x)}  // implicit conversion of an Array[Double] to RichDoubleNumber
    implicit final def DoubleDoubleArrayToRichDouble2DArray(x: Array[Array[Double]]) =  {  new RichDouble2DArray(x)}  // implicit conversion of an Array[Array[Double]] to RichDouble2DArray
     
     implicit final def RichDouble1DArrayToVec(x: RichDouble1DArray) = {  new Vec(x.getv)}  // implicit conversion of a RichDouble1DArray to Vec
     implicit final def RichDouble1DArrayToDoubleArray(x: RichDouble1DArray) = {  x.getv}  // implicit conversion of a RichDouble1AArray to double []
     implicit final def RichDouble2DArrayToDoubleDoubleArray(x: RichDouble2DArray) = {  x.getv}  // implicit conversion of a RichDouble2DArray to double [][]
     
  
// used for plotting routines
   implicit final def VecToDoubleArr(x: Vec) = { x.getv }
   implicit final def MatrixToDoubleArr(x: Matrix) = { x.getRow(1) }

  implicit final def Double2A(v: Double)  = new scalaSci.MatlabRange.MatlabRangeStart(v)
  

  
// sorting utility. Sorts the array x, performing the same interchanges on array y
// userful for example for plotting 
final def sorta(x: Array[Double], y: Array[Double]) = {
	var N = x.length
	var xy = Array.ofDim[Double](N, 2)
	var k = 0
	while (k<N) {
		xy(k)(0) = x(k)
		xy(k)(1) = y(k)
		k += 1
	}
        // sort the array xy based on the zeroth column
	xy = scalaSci.math.array.DoubleArray.sort(xy, 0)
	var xs = new Array[Double](N)
	var ys = new Array[Double](N)
	k=0
	while (k<N) {
		xs(k) = xy(k)(0)
		ys(k) = xy(k)(1)
		k += 1
	}
	(xs, ys)
}
  
  
final def oneDDoubleArray(x: Array[Array[Double]]) = 
  {
    var Nrows = x.length
    var Ncols = x(0).length
    var fa = new Array[Double](Nrows*Ncols)
    var cnt = 0
    var r = 0; var c = 0
    while (r < Nrows)  {
      c = 0
      while (c < Ncols) {
           fa(cnt) = x(r)(c)
           cnt += 1
           c += 1
      }
      r += 1
      
    }
    fa
  }  
  
    
  implicit final def ArrayIntToRichDouble1DArray(x: Array[Int]) = {
    var N = x.length
    var rda = new Array[Double](N)
    var k = 0
    while  (k< N) {
      rda(k) = x(k)
      k += 1
    }
    new RichDouble1DArray(rda)
  }
  
  
  final def DoubleArrayToRichDouble2DArray(x: Array[Double]) = {
    var N = x.length
    var rdda = new RichDouble2DArray(N, 1)
    var k = 0
    while (k <  N) {
      rdda(k,0) = x(k)
      k += 1
    }
    rdda
  }
  
  final def RichDouble1DArrayToRichDouble2DArray(x: RichDouble1DArray) = {
    var N = x.length
    var rdda = new RichDouble2DArray(N, 1)
    var k = 0
    while (k <  N) {
      rdda(k,0) = x(k)
      k += 1
    }
    rdda
  }
  

  // Some routines that construct objects from String representations follow

// construct an Array[Double] from a String 
// var ad = AD("3.4 -6.7 -1.2 5. 6")
final def AD(s: String) =  {
    
// count how many numbers 
var strtok = new java.util.StringTokenizer(s, ", ")  // elements are separated by ',' or ' '
var nCols = 0
while (strtok.hasMoreTokens) {
   strtok.nextToken
   nCols += 1  
}

var numbersArray = new Array[Double](nCols)   
strtok = new java.util.StringTokenizer(s,  ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(c) = tok.toDouble
   c += 1
}

 new RichDouble1DArray(numbersArray)
 }  

// construct a  1-indexed RichDouble1DArray from a String 
// var ad = AD1("3.4 -6.7 -1.2 5. 6")
final def AD1(s: String) =  {
    
// count how many numbers 
var strtok = new java.util.StringTokenizer(s, ", ")  // elements are separated by ',' or ' '
var nCols = 0
while (strtok.hasMoreTokens) {
   strtok.nextToken
   nCols += 1  
}

var numbersArray = new Array[Double](nCols+1)   
strtok = new java.util.StringTokenizer(s,  ", ")  // elements are separated by ',' or ' '
var c=1 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(c) = tok.toDouble
   c += 1
}

 new RichDouble1DArray(numbersArray)
 }  

  
  // construct a Matrix from a String 
// var m = AAD("3.4 -6.7; -1.2 5.6")

// construct a RichDouble2DArray from a String 
final def AAD(s: String) =  {
    var nRows = 1
    var nCols = 0
    var i = 0
    while  (i < s.length-1)  { // count how many rows are specified
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

var numbersArray = Array.ofDim[Double](nRows, nCols)   
    
    var k = 0
    while  (k < nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(k)(c) = tok.toDouble
    c += 1
     }   // read row
     k += 1
   }  // read array
   new RichDouble2DArray(numbersArray)
 }  
 
// construct an 1-indexed  RichDouble2DArray  from a String 
// var m = AAD1("3.4 -6.7; -1.2 5.6")
final def AAD1(s: String) =  {
    var zd = AAD(s)  
    
    val Nrows = zd.Nrows
    val Ncols = zd.Ncols
    val zd1 = Array.ofDim[Double](Nrows+1, Ncols+1)
    
    var k = 0
    while  (k <  Nrows)  {
      var  l = 0
      while  (l <  Ncols)  {
         zd1(k+1)(l+1) = zd(k, l)
         l += 1 
      }
      k += 1
    }
    new RichDouble2DArray(zd1)
    
 }  
 
// construct a one-indexed Matrix from a String 
// var m = M1("3.4 -6.7; -1.2 5.6")
final def M1(s: String) =  {
    var nRows = 1
    var nCols = 0
    var i = 0
    while (i < s.length)   {   // count how many rows are specified
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

var numbersArray = Array.ofDim[Double](nRows, nCols)   
    var k = 0
    while  (k < nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(k)(c) = tok.toDouble
    c += 1
     }   // read row
     k += 1
   }  // read array
   new Matrix(numbersArray)
 }  


// construct a Vector  from a String 
// var data = V("3.4 -6.7 -1.2 5.6")
final def V(s: String) =  {
    
// count how many numbers 
var strtok = new java.util.StringTokenizer(s, ", ")  // elements are separated by ',' or ' '
var nCols = 0
while (strtok.hasMoreTokens) {
   strtok.nextToken
   nCols += 1  
}

var numbersArray = new Array[Double](nCols)   
strtok = new java.util.StringTokenizer(s,  ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(c) = tok.toDouble
   c += 1
}

 new Vec(numbersArray)
 }  

  

  
  
import  _root_.scalaSci.Complex
object i extends Complex(0.0, 1.0)

  // operations for Complex numbers
final def abs(x: Complex) = x.abs

final def conjugate(x: Complex) = x.conjugate
final def conj(x: Complex) = x.conj

final def  sin(x: Complex) = x.sin
final def  cos(x: Complex) = x.cos
final def  tan(x: Complex) = x.tan
final def  sinh(x: Complex) = x.sinh
final def  cosh(x: Complex) = x.cosh
final def  tanh(x: Complex) = x.tanh
final def  asin(x: Complex) = x.asin
final def  acos(x: Complex) = x.acos
final def  atan(x: Complex) = x.atan

final def exp(x: Complex) = x.exp
final def log(x: Complex) = x.log
final def pow(x: Complex, y: Complex) = x.pow(y)
final def pow(x: Complex, y: Double) = x.pow(y)
final def sqrt(x: Complex) = x.sqrt

  // return the square root of 1-this^2
 final def sqrt1z(x: Complex) = x.sqrt1z
 
  
  /* Compute the argument of this complex number.
  The argument is the angle phi between the positive real axis and the 
  point  representing this number in the complex plane.
  The value returned is between -PI (not inclusive)
  and PI (inclusive), with negative values returned for numbers with negative imaginary parts.
  If either real or imaginary part (or both) is NaN, NaN is returned.
  Infinite parts are handled as Math.atan2 handles them,
  essentially treating finite parts as zero in the presence of an infinite coordinate
  and returning a multiple of pi/4 depending on the signs of the infinite parts.
   */
  final def getArg(x: Complex) = x.getArg
  final def getArgument(x: Complex) = x.getArgument
  
 final def nthRoot(x: Complex, n: Int) = x.nthRoot(n)
  
     
final def abs(x: Double) =     java.lang.Math.abs(x)
final def sin(x: Double) =      java.lang.Math.sin(x)
final def cos(x: Double) =     java.lang.Math.cos(x)
final def tan(x: Double) =     java.lang.Math.tan(x)
final def cosh(x: Double) =   java.lang.Math.cosh(x)
final def sinh(x: Double) =    java.lang.Math.sinh(x)
final def tanh(x: Double) =    java.lang.Math.tanh(x)
final def asin(x: Double) =    java.lang.Math.asin(x)
final def acos(x: Double) =   java.lang.Math.acos(x)
final def atan(x: Double) =    java.lang.Math.atan(x)
final def exp(x: Double) =    java.lang.Math.exp(x)
final def pow(x: Double, value: Double) =   java.lang.Math.pow(x, value)
final def log(x: Double) =    java.lang.Math.log(x)
final def log2(x: Double) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}
final def log10(x: Double) =     java.lang.Math.log10(x)
final def ceil(x: Double) =    java.lang.Math.ceil(x)
final def floor(x: Double) =    java.lang.Math.floor(x)
final def round(x: Double) =   java.lang.Math.round(x)
final def sqrt(x: Double) =     java.lang.Math.sqrt(x)
final def toDegrees(x: Double) =    java.lang.Math.toDegrees(x)
 // toRadians
final def toRadians(x: Double) = {
    java.lang.Math.toRadians(x)
}

final def eye1(tuple: (Int, Int)): Matrix = eye1(tuple._1, tuple._2)
final def eye( tuple: (Int, Int)): RichDouble2DArray = eye(tuple._1, tuple._2) 
final def zeros(tuple: (Int, Int)): RichDouble2DArray = zeros( tuple._1, tuple._2)
final def rand() = scalaSci.StaticScalaSciCommonOps.rndGen.doub()

   // create a random matrix using arguments a tuple of two Ints,
final def Zeros(tuple: (Int, Int)): Array[Array[Double]]= 
    Zeros(tuple._1, tuple._2)


// create a ones matrix using arguments a tuple of two Ints,
final def Ones(tuple: (Int, Int)): Array[Array[Double]] = 
    Ones(tuple._1, tuple._2)
  
  final def Fill(tuple: (Int, Int), v: Double): Array[Array[Double]] = 
    Fill(tuple._1, tuple._2, v)


  
  // create a random matrix using arguments a tuple of two Ints,
  final def rand(tuple: (Int, Int)): RichDouble2DArray = 
     rand(tuple._1, tuple._2)
  

  // create a random matrix using arguments a tuple of two Ints,
  final def rand1(tuple: (Int, Int)):  Matrix = 
    rand1(tuple._1, tuple._2)

// returns uniformly distributed vectors  
 final def vrand(n:Int)  = {
    var dv = scalaSci.math.array.StatisticSample.randomUniform(n, 0.0, 1.0)   
    new Vec(dv)
}

  // returns normally distributed vectors, mean 0.0, standard deviation 1.0
final def vrandn(n: Int) = {
  var dv = scalaSci.math.array.StatisticSample.randomNormal(n, 0.0, 1.0)  
  new Vec(dv)
}   
  
  // returns normally distributed vectors, mean  mu, standard deviation sigma
final def vrandn(n: Int, mu: Double, sigma: Double) = {
  var dv = scalaSci.math.array.StatisticSample.randomNormal(n, mu, sigma)   
  new Vec(dv)
}   

  

 
 final def  Inc(begin: Double, pitch: Double,  end: Double) = {
     val siz = ((end - begin) / pitch).asInstanceOf[Int]
     val doubleArr = new Array[Double](siz)
     var i=0;  
     while (i < siz) {  
       doubleArr(i) = begin + i * pitch
       i += 1
     }
  new RichDouble1DArray(doubleArr)
    }

final def linspace(startv: Double, endv: Double): RichDouble1DArray  = {
    val nP = 100  // use 100 as final default number of points
    val data = new Vec(nP)
    val dx = (endv-startv)/(nP-1)
    var i = 0
    while  (i < nP) {
      data(i) = startv +  i * dx
      i += 1
    }
 new RichDouble1DArray(data)
 }

final def linspace(startv: Double, endv: Double, nP: Int):  RichDouble1DArray =  {
   val data = new RichDouble1DArray(nP)
   val dx = (endv-startv)/(nP-1)
   var i = 0
   while  (i < nP)  {
      data(i) = startv +  i * dx
      i += 1
    }
 data
 }

  final def vlinspace(startv: Double, endv: Double): scalaSci.Vec  = {
    val nP = 100  // use 100 as final default number of points
    val data = new Vec(nP)
    val dx = (endv-startv)/(nP-1)
    var i = 0
    while  (i < nP) {
      data(i) = startv +  i * dx
      i += 1
    }
 data
 }

final def vlinspace(startv: Double, endv: Double, nP: Int): scalaSci.Vec =  {
   val data = new Vec(nP)
   val dx = (endv-startv)/(nP-1)
   var i = 0
   while  (i < nP)  {
      data(i) = startv +  i * dx
      i += 1
    }
 data
 }

  final def Linspace(startv: Double, endv: Double): Array[Double] = {
    val nP = 100  // use 100 as final default number of points
    val data = new Array[Double](nP)
    val dx = (endv-startv)/(nP-1)
    var i = 0
    while  (i < nP) {
      data(i) = startv +  i * dx
      i += 1
    }
 data
 }

final def Linspace(startv: Double, endv: Double, nP: Int): Array[Double] =  {
   val data = new Array[Double](nP)
   val dx = (endv-startv)/(nP-1)
   var i = 0
   while  (i < nP)  {
      data(i) = startv +  i * dx
      i += 1
    }
 data
 }

// implicit conversion from Double[Array] to Vec
   final def toVector(da: Array[Double]): scalaSci.Vec = {
       new Vec(da)
   }
// dot
final def dot(a: scalaSci.Vec, b: scalaSci.Vec): Double = {
   a.dot(b)
   }  

  final def dot(a: scalaSci.Matrix, b: scalaSci.Matrix): Double = {
  a.dot(b)
   }  

  
// cross (pointwise) product
final def cross(a: scalaSci.Vec, b: scalaSci.Vec): scalaSci.Vec = {
   a.cross(b)
   }  
   
final def cross(a: scalaSci.Matrix, b: scalaSci.Matrix): scalaSci.Matrix = {
  a.cross(b)
   }  

    // Signal Processing Routines
 final def  fft(sig: Array[Double]):(Array[Double], Array[Double]) =  scalaSci.FFT.FFTScala.fft(sig)
       //scalaSci.FFT.ApacheFFT.fft(sig)
   

   // Signal Processing Routines
 final def  fft(matr: Array[Array[Double]]):(Array[Array[Double]], Array[Array[Double]]) =  scalaSci.FFT.FFTScala.fft(matr)
 
 final def fft(sig: Vec):(Array[Double], Array[Double]) =   scalaSci.FFT.FFTScala.fft(sig.getv) 

 final def  fft(sig: Array[Double],  SF: Double):(Array[Double], Array[Double], Array[Double]) =   scalaSci.FFT.FFTScala.fft(sig, SF)
       //scalaSci.FFT.ApacheFFT.fft(sig)
   

 final def fft(sig: Vec, SF: Double):(Array[Double], Array[Double], Array[Double]) =      scalaSci.FFT.FFTScala.fft(sig.getv, SF) 

//// Apache FFT 
 final def  FFT(sig: Array[Double]) =   scalaSci.FFT.ApacheFFT.fft(sig)

 final def FFT(sig: Vec):Array[org.apache.commons.math3.complex.Complex] =       FFT(sig.getv) 
  
 final def ifft(realffts: Array[Double], imffts: Array[Double]):Array[Double] =  scalaSci.FFT.FFTScala.ifft(realffts, imffts)

 final def IFFT(sig: Array[org.apache.commons.math3.complex.Complex]): Array[org.apache.commons.math3.complex.Complex] = {
    scalaSci.FFT.ApacheFFT.ifft(sig)
  }

 final def getReParts(sig: Array[org.apache.commons.math3.complex.Complex]) = {
   scalaSci.FFT.FFTCommon.getReParts(sig)
 }

 final def getImParts(sig: Array[org.apache.commons.math3.complex.Complex]) = {
   scalaSci.FFT.FFTCommon.getImParts(sig)
 }



final def size(v: Vec): Int =    v.length 

final def length(v: Vec): Int =  v.length 

final def size(a: scalaSci.Matrix)  =  a.size()

final def size(a: scalaSci.RichDouble2DArray)  =  a.size()

final def  length(a: scalaSci.Matrix): Int = { a.length }

   
  final def min(args: Int*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  final def max(args: Int*) =    {
      var mxAll = args(0)
      for (arg <- args)  
        if  (arg >  mxAll)
          mxAll = arg
      mxAll  
    }
  
  
  final def min(args: Float*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  
  final def min(args: Double*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  final def max(args: Double*) =    {
      var mxAll = args(0)
      for (arg <- args)  
        if  (arg >  mxAll)
          mxAll = arg
      mxAll  
    }
  
  
  final def sum(args: Int*) =    {
      var smAll = 0
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  final def sum(args: Double*) =    {
      var smAll = 0.0
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  final def sum(args: Float*) =    {
      var smAll = 0.0f
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  
  final def prod(args: Int*) =    {
      var prAll = 1
      for (arg <- args)  
          prAll *= arg
      prAll  
    }
  
  final def prod(args: Double*) =    {
      var prAll = 1.0
      for (arg <- args)  
          prAll  *= arg
      prAll  
    }
  
  final def prod(args: Float*) =    {
      var prAll = 1.0f
      for (arg <- args)  
          prAll *= arg
      prAll  
    }

final def  corr(v1: Array[Double], v2: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
     r
 }

final def correlation(v1: Array[Double], v2: Array[Double]): Double = {
  var r = corr(v1, v2)
  r
}

final def  corr(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
     r
 }

final def  cov(v1: Array[Double], v2: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1, v2);
     r
 }

final def  cov(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1, v2);
     r
 }

final def  covariance(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = cov(v1, v2)
     r
 }

final def  cov(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }

final def  covariance(v: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }
    
final def correlation(v1: Array[Array[Double]], v2: Array[Array[Double]]) = {
  var r = corr(v1, v2)
  r
}

final def  corr(v: Array[Array[Double]])  =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v, v);
     r
 }

final def  correlation(v: Array[Array[Double]]) =   {
     var r =  corr(v,v);
     r
 }

final def  det(M: Matrix) = {
  M.det()
}

final def rank(M: Matrix): Int = {
  M.rank()
}  

final def trace(M: Matrix): Double = {
   M.trace()
}

final def trace(DD: Array[Array[Double]]) = {
LinearAlgebra.trace(DD)
}



//////////////////


// JSCI based routines
//  Compute the L2 norm 
final def  norm(vin: Vec): Double = {
   JSci.maths.wavelet.Signal.norm(vin.getv)
 }

final def  cfft(vin: Vec): Array[JSci.maths.Complex] = {
   var vlen = length(vin)
   println(" in FFT, vlen = "+vlen)
   var pow2i = floor(log2(vlen))
   var newSize = pow(2, pow2i).asInstanceOf[Int]
   var pow2Sig = vin(0, newSize-1)
   JSci.maths.wavelet.Signal.fft(pow2Sig.getv)
}

final def absFFT(vin: Vec)  = {
   var vlen = length(vin)
   var pow2i = floor(log2(vlen))
   var newSize = pow(2, pow2i).asInstanceOf[Int]
   var pow2Sig = vin(0, newSize-1)
   JSci.maths.wavelet.Signal.absFFT(pow2Sig.getv)
}

final def cfftInverse(vcompl:  Array[JSci.maths.Complex]): Array[JSci.maths.Complex] = {
   JSci.maths.wavelet.Signal.fftInverse(vcompl)
}

    // gets the real parts from a Complex array
final def getRealValues(carr: Array[JSci.maths.Complex]) = {
    var len = carr.length
    var doubleArr = new Array[Double](len)
    for (k<-0 to len-1)
      doubleArr(k) = carr(k).real()
    doubleArr
}
/*
 final def inc2d(beginX: Double, pitchX: Double,  endX: Double, beginY: Double, pitchY: Double,  endY: Double): Vec = {
     var siz = ((end - begin) / pitch).asInstanceOf[Int]
     var doubleArr = new Array[Double](siz)
     var i=0;  while (i < siz) {   doubleArr(i) = begin + i * pitch;  i += 1;  }
   new Vec(doubleArr)
	}
*/

final def fwt(v: Vec, j: Int) = {
  var sig = new Signal(v.getv)
  var wcoeffs = sig.fwt(j)
  wcoeffs.getCoefs
}

  
  
 
  // using the NUMAL library
 final def pinv(m: scalaSci.Matrix): scalaSci.Matrix = {
    m.pinv()
}  


/*var N=400
var x  = rand(N,  N)

tic
nreig(x)
var tm=toc  
// tm = 1.3 sec
  
  tic
 eig(x)
 var tmMTJ = toc
 */
 // tm = 10.8 sec
 
  // using Numerical Recipes
final def nreig(da: Array[Array[Double]]) = {
 da.nreig
}

  // using JBLAS
final def feig(da: Array[Array[Double]]) = {
  da.feig
}  
  
  // using LAPACK
final def leig(M: RichDouble2DArray) = {
  var MNrows = M.Nrows
  var MNcols = M.Ncols
    
  var realEvs = new Array[Double](MNrows)
  var imEvs = new Array[Double](MNcols)
  var realEvecs = Array.ofDim[Double](MNrows, MNcols)
  var imEvecs = Array.ofDim[Double](MNrows, MNcols)
  scalaSci.ILapack.Eig(M.getv)
  }

  final def la_eig(a: Array[Array[Double]]) = scalaSci.ILapack.Eig(a)


  // same as eig for SciLab compatibility
final def spec(a: RichDouble2DArray) = {
   eig(a)  
}  

  // determines the generalized inverse A^+ of the mXn matrix A (m >=n). 
final def psdInv( a: Matrix):Matrix = {
    var em = new Array[Double](8)
      // pass final default values to simplify the interface
    em(0) = 1.0e-14  // the machine precision
    em(2) = 1.0e-12  // the relative precision of singular values
    em(4) = 80.0  // the maximal number of iterations to be performed
    em(6) = 1.0e-10  // the minimal nonneglected singular value
    
    var N = a.Nrows; var M = a.Ncols
        var transPsdInv = new Matrix(a)   // on exit:  the tanspose of the pseudo-inverse
        numal.Linear_algebra.psdinv(transPsdInv.getv, N-1, M-1, em)
        transPsdInv
}



  
// Matlab like norm function
final def norm(a: Array[Array[Double]], normType: Integer) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}
final def norm(a:Matrix, normType: Integer) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}



final def Singular_cond(M: Array[Array[Double]]): Double = {
   LinearAlgebra.singular( M).cond()
}


final def  Singular_S(M: Array[Array[Double]]) = {
   LinearAlgebra.singular(M).getS().getArray
}

final def  S(M: Array[Array[Double]]) = {
  Singular_S( M)
}

final def  Singular_values(M: Array[Array[Double]]) = {
   LinearAlgebra.singular(M).getSingularValues()  
}

final def  Singular_U(M: Array[Array[Double]] ) = {
   LinearAlgebra.singular(M).getU().getArray
}

final def   Singular_V(M: Array[Array[Double]])  = {
   LinearAlgebra.singular(M).getV().getArray
}

final def Singular_norm2(M: Array[Array[Double]] ): Double = {
  LinearAlgebra.singular( M).norm2()
}

final def  Singular_rank(M: Array[Array[Double]]): Int = {
   LinearAlgebra.singular( M).rank()
}


final def  rank(M: Array[Array[Double]]): Int = {
  LinearAlgebra.rank( M)
}

final def Eigen_V(M: Array[Array[Double]]) = {
    LinearAlgebra.eigen(M).getV()
}

final def V(M: Array[Array[Double]]) = {
   Eigen_V( M)
 }

final def  Eigen_D(M: Array[Array[Double]]) = {
   LinearAlgebra.eigen(M).getD()
}

final def  D(M: Array[Array[Double]]) = {
   Eigen_D( M)
}

final def leig(M: Array[Array[Double]]) = {
  var MNrows = M.length
  var MNcols = M(0).length
    
  var realEvs = new Array[Double](MNrows)
  var imEvs = new Array[Double](MNcols)
  var leftEvecs = Array.ofDim[Double](MNrows, MNcols)
  var rightEvecs = Array.ofDim[Double](MNrows, MNcols)
  scalaSci.ILapack.Eig(M)
  }



final def  transpose(Mt: Array[Array[Double]]) =  {
    DoubleArray.transpose(Mt)
	}

  final def  T(Mt: Array[Array[Double]]) =  {
     DoubleArray.transpose( Mt ) 
	}
 


  

final def Singular_cond(M: RichDouble2DArray): Double = {
   LinearAlgebra.singular( M.getv).cond()
}

final def cond(M: RichDouble2DArray): Double = {
   LinearAlgebra.cond( M.getv)
}

final def  Singular_S(M: RichDouble2DArray): RichDouble2DArray = {
   new RichDouble2DArray(LinearAlgebra.singular(M.getv).getS().getArray)
}

final def  S(M: RichDouble2DArray): RichDouble2DArray = {
  Singular_S( M)
}

final def  Singular_values(M: RichDouble2DArray): RichDouble1DArray = {
   new RichDouble1DArray(LinearAlgebra.singular(M.getv).getSingularValues())  
}

final def  Singular_U(M: RichDouble2DArray ):  RichDouble2DArray = {
   new RichDouble2DArray(LinearAlgebra.singular(M.getv).getU().getArray)
}

final def   Singular_V(M: RichDouble2DArray): RichDouble2DArray = {
   new RichDouble2DArray(LinearAlgebra.singular(M.getv).getV().getArray)
}

final def Singular_norm2(M: RichDouble2DArray ): Double = {
  LinearAlgebra.singular( M.getv).norm2()
}

final def  Singular_rank(M: RichDouble2DArray): Int = {
   LinearAlgebra.singular( M.getv).rank()
}


final def  rank(M: RichDouble2DArray): Int = {
  LinearAlgebra.rank( M.getv)
}

final def Eigen_V(M: RichDouble2DArray): RichDouble2DArray = {
    new RichDouble2DArray(LinearAlgebra.eigen(M.getv).getV())
}

final def V(M: RichDouble2DArray): RichDouble2DArray = {
   new RichDouble2DArray(Eigen_V( M.getv))
 }

final def  Eigen_D(M: RichDouble2DArray) = {
   new RichDouble2DArray(LinearAlgebra.eigen(M.getv).getD())
}

final def  D(M: RichDouble2DArray): RichDouble2DArray = {
   new RichDouble2DArray(Eigen_D( M.getv))
}



final def  transpose(Mt: RichDouble2DArray):  RichDouble2DArray =  {
    new RichDouble2DArray(DoubleArray.transpose(Mt.getv))
	}

  final def  T(Mt: RichDouble2DArray): RichDouble2DArray =  {
     new RichDouble2DArray(DoubleArray.transpose( Mt .getv) )
	}
 

   

//  END:  operations for RichDouble1DArray, RichDouble2DArray types

  
 
  // compute the determinant of A using  EJML library
  /*
  val a = Rand(3, 3)
  val det_a = ejml_det(a)
  */
 final def ejml_det(A: Array[Array[Double]]) = {
   val  ejmlM = new scalaSci.EJML.Mat(A)
   ejmlM.det
 } 
 
 final def mtj_det(A: Array[Array[Double]]) = {
   val  mtjM = new scalaSci.MTJ.Mat(A)
   mtjM.det
 } 
  

 
  // compute the trace of A using EJML library
  /*
    val a = Rand(3, 3)
    val trace_a = ejml_trace(a)
*/
 final def ejml_trace(A: Array[Array[Double]]) = {
   val  ejmlM = new scalaSci.EJML.Mat(A)
   ejmlM.trace
 } 
 
  
  
 // invert the matrix A using EJML library
 /*
  val a = Rand(3, 3)
  val ia = ejml_inv(a)
  val mtjinv = mtj_inv(a)
  val acinv = ac_inv(a)
  val isOnes = a*ia
  */
 final def ejml_inv(A: Array[Array[Double]]) = {
   val ejmlM = new scalaSci.EJML.Mat(A)
   val invM = ejmlM.invert
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   var r = 0; var c = 0
   while (r < nrows) {
    c = 0
    while (c < ncols)   {
       invDA(r)(c) = invM.get(r,c)
       c += 1
     }
    r += 1
   }
    invDA
 }  
  
 
 final def mtj_inv(A: Array[Array[Double]]) = {
   val mtjM = new scalaSci.MTJ.Mat(A)
   val invM = mtjM.inv()
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   var r = 0; var c = 0
   while (r < nrows)  { 
     c = 0
     while (c < ncols) {
       invDA(r)(c) = invM(r,c)
       c += 1
      }
      r += 1
   }
    invDA
 }  
  
  
 final def ac_inv(A: Array[Array[Double]]) = {
   val acM = new scalaSci.CommonMaths.Mat(A)
   val invM = acM.inv()
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   var r = 0; var c = 0
   while ( r < nrows )   {
     c = 0
     while ( c < ncols )    {
       invDA(r)(c) = invM.get(r,c)
       c += 1
        }
      r += 1
   }
    invDA
 }  

final def conditionP2( a: scalaSci.EJML.Mat) =   a.conditionP2

final def conditionP(a: scalaSci.EJML.Mat,  p: Double) = a.conditionP(p)  

final def conditionP2( a: RichDouble2DArray) =   a.conditionP2

final def conditionP(a: RichDouble2DArray,  p: Double) = a.conditionP(p)  
 
final def cond(a: scalaSci.EJML.Mat) = a.conditionP2

final def cond(a: scalaSci.MTJ.Mat):Double  = cond(a.toDoubleArray)

final def cond(a: scalaSci.CommonMaths.Mat):Double  = cond(a.toDoubleArray)


  
final def cond(M: Array[Array[Double]]): Double = {
   LinearAlgebra.cond( M)
}


 final def LU(A: RichDouble2DArray):(RichDouble2DArray,  RichDouble2DArray,  RichDouble2DArray) = { 
    val LUdecomp = LinearAlgebra.LU( A.getv)   // perform the LU decomposition
    val L =  LUdecomp.getL().getArray
    val U =  LUdecomp.getU().getArray
    val N = L.length
    var P = Array.ofDim[Double](N, N)
    var p = LUdecomp.getPivot()
 // construct permutation matrix
  var k=0
  while (k<N) {  
      P(k)( p(k)) = 1
      k += 1
  }
 
  (new RichDouble2DArray(L), new RichDouble2DArray(U),  new RichDouble2DArray(P))
 } 

   final def la_LUSolve(a: Array[Array[Double]], b: Array[Double]) =
    scalaSci.ILapack.LUSolve(a, b)


final def  jama_LUSolve(A: Array[Array[Double]],  b:  Array[Array[Double]]) = {
    val  LUDec = LinearAlgebra.LU(A)
    val  jb = new jMatrix(b)
    val  solvedMat = LUDec.solve(jb)
    solvedMat.getArray
}

  
final def  LU_solve(A: RichDouble2DArray,  b:  RichDouble2DArray): RichDouble2DArray = {
    var  luA = new com.nr.la.LUdcmp(A.v)
    var   res = Array.ofDim[Double](b.Nrows, b.Ncols)
    luA.solve(b, res)
    
    new RichDouble2DArray(res)
}

final def  QR_H(M: RichDouble2DArray): RichDouble2DArray = {
  new RichDouble2DArray(LinearAlgebra.QR( M.getv).getH().getArray)
}

final def  QR_Q(M: RichDouble2DArray): RichDouble2DArray = {
  new RichDouble2DArray(LinearAlgebra.QR( M.getv).getQ().getArray)
}

final def  Q(M: RichDouble2DArray): RichDouble2DArray = {
    new RichDouble2DArray(QR_Q( M.getv))
 }

final def   QR_R(M: RichDouble2DArray ) = {
   new RichDouble2DArray(LinearAlgebra.QR( M.getv).getR().getArray)
}

final def  R(M: RichDouble2DArray) =  {
   QR_R( M)
 }

final def QR(M: RichDouble2DArray) = {
  (Q(M), R(M))
}

final def lu(A: RichDouble2DArray) = LU(A)
final def qr(M: RichDouble2DArray) = QR(M)


final def  QR_solve(A: RichDouble2DArray, b: RichDouble2DArray) = {
  var  QRDec = LinearAlgebra.QR(A.getv)
  var  solvedMat = QRDec.solve(new Jama.jMatrix(b.getv))
  new RichDouble2DArray(solvedMat.getArray)
}

  
// global operations on Array[Array[Double]]
  final def LU(A: Array[Array[Double]]):(Array[Array[Double]], Array[Array[Double]], Array[Array[Double]]) = { 
    val LUdecomp = LinearAlgebra.LU( A)   // perform the LU decomposition
    val L =  LUdecomp.getL().getArray
    val U =  LUdecomp.getU().getArray
    val N = L.length
    var P = Array.ofDim[Double](N, N)
    var p = LUdecomp.getPivot()
 // construct permutation matrix
    for (k<-0 until N)  
      P(k)( p(k)) = 1
 
  (L, U, P)
 } 

  

final def  QR_H(M: Array[Array[Double]])  = {
  LinearAlgebra.QR( M).getH().getArray
}

final def  QR_Q(M: Array[Array[Double]]) = {
  LinearAlgebra.QR( M).getQ().getArray
}

final def  Q(M: Array[Array[Double]]) = {
    QR_Q( M)
 }

final def   QR_R(M: Array[Array[Double]]) = {
   LinearAlgebra.QR( M).getR().getArray
}

final def  R(M: Array[Array[Double]]) =  {
   QR_R( M)
 }

final def QR(M: Array[Array[Double]]) = {
  (Q(M), R(M))
}

final def lu(A: Array[Array[Double]]) = LU(A)
final def qr(M: Array[Array[Double]]) = QR(M)

  
// solve using EJML library
// EJML
// 
// e,g,
/*
  var A = RD2D(3,3, 
   2.3, 4.5,  -0.23,
   0.34, -7.7, 0.4,
   -3.4, 6.5, -0.44)
   
   var b = Array(5.6, 4.5, -4.5)
   
   var x = solveEJML(A, b)
   
   A*x-b
   */
final  def  solveEJML(dA: RichDouble2DArray,  db: Array[Double]): Array[Double] = {
    val   ddA = new DenseMatrix64F(dA)
    val   ddb = new DenseMatrix64F(db.length, 1, true, db: _*)
    val   dsol = new DenseMatrix64F(db.length, 1)
            
    val solutionOK = org.ejml.ops.CommonOps.solve(ddA, ddb, dsol)
    
    dsol.getData()
}


final def solveEJML(A: RichDouble2DArray,  b: RichDouble2DArray): RichDouble2DArray = {
    //  solve for the matrix b columns one by one
    val arows = A.Nrows; val  acols = A.Ncols
    val brows = b.Nrows; val  bcols = b.Ncols
    if (arows != brows) {
        println("in solveEJML incompatible matrix dimensions, arows = "+arows+", brows = "+brows)
        A
    }
    else  {
    val  results = new RichDouble2DArray(arows, bcols)
    var col = 0
    while  (col < bcols) {
        var  currentCol = new Array[Double](brows)
        var r = 0
        while ( r < brows) {
          currentCol(r) = b(r, col)
          r += 1
        }
          // solve for the current column
        var  colSolution = solveEJML(A.getArray, currentCol)
           // copy solution
        r = 0   
        while (r < brows) {
            results(r, col) =  colSolution(r)
            r += 1
        }
          col += 1
    }
     results
    }
}

final def  QR_solve(A: Array[Array[Double]], b: Array[Array[Double]]) = {
  var  QRDec = LinearAlgebra.QR(A)
  var  solvedMat = QRDec.solve(new Jama.jMatrix(b))
  solvedMat
}

// COMMON-OPS
 
//   Solves for X in the following equation:
//      x = a^{-1}  b
//   where 'a' is this matrix and 'b' is an n by p matrix.
//   If the system could not be solved then SingularMatrixException is thrown.  Even
//   if no exception is thrown 'a' could still be singular or nearly singular.
//      @param b n by p matrix. Not modified.
//      @return The solution for 'x' that is n by p.

  
  
  // solve with the CSparse
final def solve(A: scalaSci.Sparse,  b: Array[Double]) = {
    scalaSci.Sparse.sparseSolve(A, b)
}

// solve with the MTJ Column Compressed Matrix
final def solve(A: scalaSci.CCMatrix, b: Array[Double]) = {
    scalaSci.CCMatrix.BiCGSolve(A, b)
}

  final def solve(A: Array[Array[Double]],  b: Array[Array[Double]]):Array[Array[Double]] = {
    var  luA = new com.nr.la.LUdcmp(A)
    var   res = Array.ofDim[Double](b.length, b(0).length)
    luA.solve(b, res)
    
    res
}


final def solve(A: RichDouble2DArray,  b: RichDouble2DArray):RichDouble2DArray = {
   
    var  luA = new com.nr.la.LUdcmp(A)
    var   res = Array.ofDim[Double](b.Nrows, b.Ncols)
    luA.solve(b, res)
    
    new RichDouble2DArray(res)
}

  final def solve(A: RichDouble2DArray,  b: RichDouble1DArray): RichDouble1DArray = {
    var N = b.length
    var bb = new RichDouble2DArray(N,1)
    var k=0
    while (k< N) {
      bb(k, 0) = b(k)
      k += 1
    }
    var sol = solve(A, bb)
    // convert the solution to a RichDouble1DArray
    var x = new Array[Double](sol.numRows)
    k = 0
    while (k< sol.numRows) {
      x(k) = sol(k, 0)
      k+=1
    }
    new RichDouble1DArray(x)
    
}


//   applies the predicate to all the array elements and returns true in positions that fullfill the predicate 
//   and false otherwise  

  /* e.g.
   
   var Y = vrand(20)-0.5
   var f= filterIndices(Y,  _>0)
  */
 final def  filterIndices(X: Array[Double], predicate:  Double => Boolean) = {
      var len = X.length
      var result = new Array[Boolean](len)
      for (k<-0 until len)
        result(k) = if (predicate(X(k)))  true else false
          
    result
  }	

  
  
  // e.g. 
  //   var   rdd = rand0(20, 30)
  //   final def pred(k: Int) = if (k % 2 == 0) true else false
//     rdd.filterRows(pred)
// return rows according to the predicate
  final def  filterRows(X: RichDouble2DArray, predicate:  Int  => Boolean) = {
      var rowCnt = 0
      for (r<-0 until X.numRows()) {
          if (predicate(r))
           rowCnt += 1
     }
 
    var newMat = new RichDouble2DArray(rowCnt, X.numColumns())
    var rCnt=0
    for (r<-0 until X.numRows())  {
      if (predicate(r)) {  // copy the row
      for (c<-0 until X.numColumns())
          newMat(rCnt,c) = X(r,c)
          rCnt += 1
                    }
     }
                    
    newMat     
  
  }		 

  // return cols according to the predicate
 final def  filterColumns(X: RichDouble2DArray, predicate:  Int => Boolean) = {
    var colCnt = 0
    for (c<-0 until X.numColumns()) {
      if (predicate(c))
       colCnt += 1
  }
  var cCnt=0
  var newMat = new RichDouble2DArray(X.numRows(), colCnt)
    for (c<-0 until X.numColumns()) {
      if (predicate(c) )  {  // copy the column
      for (r<-0 until  X.numRows()) 
        newMat(r, cCnt) = X(r,c)
      cCnt += 1
      }
    }
      newMat     
  
   }		 

  
// operations for Complex Matrices
  final def sqrt(x: ComplexMatrix) = x.sqrt
  final def abs(x: ComplexMatrix) = x.abs
  final def sin(x: ComplexMatrix) = x.sin
  final def cos(x: ComplexMatrix) = x.cos
  final def asin(x: ComplexMatrix) =  x.asin 
  final def sinh(x: ComplexMatrix) = x.sinh
  final def acos(x: ComplexMatrix) = x.acos
  final def cosh(x: ComplexMatrix) = x.cosh
  final def tan(x: ComplexMatrix) = x.tan
  final def atan(x: ComplexMatrix) = x.atan
  final def tanh(x: ComplexMatrix) = x.tanh
  final def sqrt1z(x: ComplexMatrix) = x.sqrt1z
  final def exp(x: ComplexMatrix) = x.exp
  final def log(x: ComplexMatrix) = x.log
  final def getArg(x: ComplexMatrix) = x.getArg
  final def getArgument(x: ComplexMatrix) = x.getArgument

 final def crand(N: Int, M: Int)   = {
   var cm = new ComplexMatrix(N, M)
    var r = 0; var c=0
      
    while (r<N) {
          c=0
          while (c<2*M) {
             cm.v(r)(c) = scalaSci.StaticScalaSciCommonOps.rndGen.doub()
             c += 1
             }
          r += 1
        }
       cm
 }
 
final def cones(N: Int, M: Int)   = {
   var cm = new ComplexMatrix(N, M)
    var r = 0; var c=0
      
    while (r<N) {
          c=0
          while (c<2*M) {
             cm.v(r)(c) = 1.0
             c += 1
             }
          r += 1
        }
       cm
 }
 
               
 
final def czeros(N: Int, M: Int)   = {
   var cm = new ComplexMatrix(N, M)
    var r = 0; var c=0
      
    while (r<N) {
          c=0
          while (c<2*M) {
             cm.v(r)(c) = 0.0
             c += 1
             }
          r += 1
        }
       cm
 }
 
                   
 
final def cfill(N: Int, M: Int, v: Complex)   = {
   var cm = new ComplexMatrix(N, M)
   var r = 0; var c=0
      
   while (r<N) {
          c=0
          while (c<2*M) {
             cm.v(r)(2*c) = v.re
             cm.v(r)(2*c+1) = v.im
             c += 2
             }
          r += 1
        }
       cm
 }
 
         
final def real(cm: ComplexMatrix) = cm.real  // get the real parts as a RichDouble2DArray
final def imag(cm: ComplexMatrix) = cm.imag   // get the imaginary parts as a RichDouble2DArray
    
//    RichDouble1DArray relevant routines
  final def  Var(v: Vec): Double = Var(v.getv)
  final def  Var(v: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.variance(v);
     r
 }

  /*
   

var x = vrand(10).getv
var y = vrand(10).getv

covariance(x, x)
   */

  final def covariance(v1: Vec, v2: Vec): Double = covariance(v1.getv, v2.getv)
  
  final def  covariance( v1:Array[Double], v2: Array[Double]): Double = {    // TODO: check
   var n = v1.length
   var degrees = n-1
   var c = 0.0;  var s1 = 0.0;  var  s2 = 0.0
   var k=0
   while  (k< n)  {
       s1 += v1(k);   s2 += v2(k); k+=1
   }
   s1 /= n;  s2 /= n
   k=0
   while  (k< n)  {     c += (v1(k)-s1)*(v2(k)-s2);  k+=1 }
    var  X = c / degrees
    X
   }

final def variance(v: Vec): Double = variance(v.getv)

final def variance(v: Array[Double]): Double = {
  var r = Var(v)
  r
}

final def  std(v: Vec): Double = std(v.getv)
final def  std(v: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.stddeviation(v);
     r
 }

final def stddeviation(v: Vec): Double = stddeviation(v.getv)
final def stddeviation(v: Array[Double]): Double = {
  var r = std(v)
  r
}

  


final def  mean(v: Array[Double]): Double =  {
       var  mn = 0.0
       var  m = v.length
       var i=0
       while  (i < m) {
            mn += v(i)
            i += 1
          }
        mn /=  m;
       mn
    }


final def min(v: Array[Double]): Double = {
  var mn = v(0)
  var k = 1
  while  (k < v.length)  {
    if (v(k) < mn) mn = v(k)
    k += 1
  }
mn
}

  
final def min(v: Array[Int]): Int = {
    var mn = v(0)
    var  k = 1
    while (k < v.length)  {
       if (v(k) < mn) mn = v(k)
       k += 1
    }
mn
}

 final def min(v: Array[Float]): Float= {
  var mn = v(0)
  var k = 1
  while  (k < v.length)  {
     if (v(k) < mn) mn = v(k)
     k += 1
  }
mn
}

final def max(v: Array[Double]): Double = {
  var mx=v(0)
  var k = 1
  while (k < v.length)  {
    if (v(k) > mx) mx = v(k)
    k += 1
  }
mx
}

  
final def max(v: RichDouble1DArray): Double = {
  var mx = v(0)
  var k = 1
  while  (k < v.length)  {
     if (v(k) > mx)  mx = v(k)
     k += 1
  }
mx
}
  
  // example:   test the speed of different overloaded versions of max
final def max(v: Array[Int]): Int = {
  var mx=v(0)
  var k = 1
  while  (k <  v.length)  {
    if (v(k) > mx) mx = v(k)
    k += 1
  }
mx
}

  
final def max(v: Array[Float]): Float= {
  var mx = v(0)
  var k = 1
  while  (k < v.length) {
    if (v(k) > mx) mx=v(k)
    k += 1
  }
mx
}

final def sum(v: Array[Double]): Double = {
    var sm = 0.0
    var m = v.length
    var i=0
    while (i < m) {
        sm += v(i)
        i += 1
    }
    sm
}

  final def sum(v: Array[Int]): Int = {
    var sm = 0
    var m = v.length
    var i=0
    while (i < m) {
        sm += v(i)
        i += 1
    }
    sm
}

  final def sum(v: Array[Float]): Float= {
    var sm = 0.0f
    var m = v.length
    var i=0
    while (i < m) {
        sm += v(i)
        i += 1
    }
    sm
}

final def prod(v: Array[Double]): Double = {
    var pd = 1.0
    var m = v.length
    var i=0
    while (i < m) {
        pd *= v(i)
        i += 1
    }
    pd
}

  final def prod(v: Array[Int]): Int= {
    var pd = 1
    var m = v.length
    var i=0
    while (i < m) {
        pd *= v(i)
        i += 1
    }
    pd
}

  
final def prod(v: Array[Float]): Float= {
    var pd = 1.0f
    var m = v.length
    var i=0
    while (i < m) {
        pd *= v(i)
        i += 1
    }
    pd
}

        
  
// use by final default logspace=10
final def vlogspace(startOrig: Double, endOrig: Double, nP: Int): scalaSci.Vec = {
    logspace(startOrig, endOrig, nP, 10.0)
}

final def vlogspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double)  = {
    var  positiveTransformed = false
    var  transformToPositive=0.0
                
    var  start = startOrig; var  end=endOrig  // use these values to handle negative values
    var  axisReversed = false
    if (start > end)   {   // reverse axis
            start = endOrig; end = startOrig; axisReversed = true;
        }
                
     if (start <= 0)  {  // make values positive
             transformToPositive = -start+1;  start = 1;     
             end = end+transformToPositive;  positiveTransformed = true;
        }
     var   logBaseFactor = 1/java.lang.Math.log10(logBase)
     var   start_tmp = java.lang.Math.log10(start)*logBaseFactor
     var   end_tmp = java.lang.Math.log10(end)*logBaseFactor
     //println("logBaseFactor = "+logBaseFactor+"  start_tmp = "+start_tmp+"  end_tmp = "+end_tmp)
                
    var  values = new Array[Double](nP)
    var  dx     = (end_tmp-start_tmp) / (nP-1)
    var   i = 0
    while  (i  < nP) {
        values(i) = java.lang.Math.pow( logBase, (start_tmp +  i * dx)) 
        i += 1
    }

    if  (positiveTransformed)    // return to the original range of values
                {
        var i = 0          
        while  (i < nP )  {
          values( i) = values(i)-transformToPositive
          i += 1
        }
      start = start-transformToPositive
       }

       if (axisReversed)  {
          var valuesNew = new Vec(nP);
          valuesNew(0) = values(nP-1)
          var i = 1
          while ( i < nP)  {
            valuesNew(i) = valuesNew( i-1)-(values(i)-values(i-1))
            i += 1
      }
     valuesNew
     }
                          
    new scalaSci.Vec(values)
}

        

// use by final default logspace=10
final def logspace(startOrig: Double, endOrig: Double, nP: Int): RichDouble1DArray = {
    logspace(startOrig, endOrig, nP, 10.0)
}

final def logspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double)  = {
    var  positiveTransformed = false
    var  transformToPositive=0.0
                
    var  start = startOrig; var  end=endOrig  // use these values to handle negative values
    var  axisReversed = false
    if (start > end)   {   // reverse axis
            start = endOrig; end = startOrig; axisReversed = true;
        }
                
     if (start <= 0)  {  // make values positive
             transformToPositive = -start+1;  start = 1;     
             end = end+transformToPositive;  positiveTransformed = true;
        }
     var   logBaseFactor = 1/java.lang.Math.log10(logBase)
     var   start_tmp = java.lang.Math.log10(start)*logBaseFactor
     var   end_tmp = java.lang.Math.log10(end)*logBaseFactor
     //println("logBaseFactor = "+logBaseFactor+"  start_tmp = "+start_tmp+"  end_tmp = "+end_tmp)
                
    var  values = new Array[Double](nP)
    var  dx     = (end_tmp-start_tmp) / (nP-1)
    var   i = 0
    while  (i  < nP) {
        values(i) = java.lang.Math.pow( logBase, (start_tmp +  i * dx)) 
        i += 1
    }

    if  (positiveTransformed)    // return to the original range of values
                {
        var i = 0          
        while  (i < nP )  {
          values( i) = values(i)-transformToPositive
          i += 1
        }
      start = start-transformToPositive
       }

       if (axisReversed)  {
          var valuesNew = new Vec(nP);
          valuesNew(0) = values(nP-1)
          var i = 1
          while ( i < nP)  {
            valuesNew(i) = valuesNew( i-1)-(values(i)-values(i-1))
            i += 1
      }
     valuesNew
     }
                          
    new RichDouble1DArray(values)
}


// use by final default logspace=10
final def Logspace(startOrig: Double, endOrig: Double, nP: Int): Array[Double] = {
    logspace(startOrig, endOrig, nP, 10.0)
}

final def Logspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double)  = {
    var  positiveTransformed = false
    var  transformToPositive=0.0
                
    var  start = startOrig; var  end=endOrig  // use these values to handle negative values
    var  axisReversed = false
    if (start > end)   {   // reverse axis
            start = endOrig; end = startOrig; axisReversed = true;
        }
                
     if (start <= 0)  {  // make values positive
             transformToPositive = -start+1;  start = 1;     
             end = end+transformToPositive;  positiveTransformed = true;
        }
     var   logBaseFactor = 1/java.lang.Math.log10(logBase)
     var   start_tmp = java.lang.Math.log10(start)*logBaseFactor
     var   end_tmp = java.lang.Math.log10(end)*logBaseFactor
     //println("logBaseFactor = "+logBaseFactor+"  start_tmp = "+start_tmp+"  end_tmp = "+end_tmp)
                
    var  values = new Array[Double](nP)
    var  dx     = (end_tmp-start_tmp) / (nP-1)
    var   i = 0
    while  (i  < nP) {
        values(i) = java.lang.Math.pow( logBase, (start_tmp +  i * dx)) 
        i += 1
    }

    if  (positiveTransformed)    // return to the original range of values
                {
        var i = 0          
        while  (i < nP )  {
          values( i) = values(i)-transformToPositive
          i += 1
        }
      start = start-transformToPositive
       }

       if (axisReversed)  {
          var valuesNew = new Vec(nP);
          valuesNew(0) = values(nP-1)
          var i = 1
          while ( i < nP)  {
            valuesNew(i) = valuesNew( i-1)-(values(i)-values(i-1))
            i += 1
      }
     valuesNew
     }
                          
    values
}


		


final def ones(n:Int) = {
     var v = new Array[Double](n)
     var i=0
     while (i<n)  {  
       v(i) = 1.0;   
       i += 1; 
     }
     new RichDouble1DArray(v)
  }

final def  zeros(n: Int) = {
          var v = new Array[Double](n)
          var i=0;  while (i<n)  { v(i) =0.0; i += 1; }
         new RichDouble1DArray(v)
  }

 
final def Ones(n:Int) = {
          var v = new Array[Double](n)
          var i=0;    while (i<n)  {   v(i) = 1.0;    i += 1; }
         v
  }

final def  Zeros(n: Int) = {
          var v = new Array[Double](n)
          var i=0;  while (i<n)  { v(i) =0.0; i += 1; }
         v
  }
  
final def  fill(n:Int, value: Double)= {
         var v = new Array[Double](n)
         var i=0;  while (i < n) {  v(i) = value; i += 1; }
         new RichDouble1DArray(v)
  }

final def  Fill(n:Int, value: Double)= {
         var v = new Array[Double](n)
         var i=0;  while (i < n) {  v(i) = value; i += 1; }
         v
  }
 
 final def  rand(n: Int) = {
   val v = new Array[Double](n)
   var i=0; 
   while (i<n) { 
     v(i) = Math.random // scalaSci.StaticScalaSciCommonOps.rndGen.doub()
     i+=1; 
     }
   new RichDouble1DArray(v)
 }
  

 final def  Rand(n: Int) = {
   val v = new Array[Double](n)
   var i=0;
   while (i<n)
   { 
     v(i)= Math.random // scalaSci.StaticScalaSciCommonOps.rndGen.doub()
     i+=1
   }
   v
 }
  
  
  
   final def sin(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.sin(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }

final def cos(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.cos(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }

final def tan(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.tan(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


  final def asin(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.asin(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


 final def acos(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.acos(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def atan(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.atan(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def sinh(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.sinh(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


final def cosh(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.cosh(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



  final def tanh(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.tanh(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def exp(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.exp(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


    final def log(vin: RichDouble1DArray):RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.log(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def log2(vin: RichDouble1DArray): RichDouble1DArray = {
      var conv = java.lang.Math.log(2.0)
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.log(vin(i))/conv
          i += 1
      }
      new RichDouble1DArray(v)
  }


final def log10(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.log10(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }

final def abs(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.abs(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def ceil(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.ceil(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


final def floor(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.floor(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def round(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.round(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }

final def sqrt(vin: RichDouble1DArray): RichDouble1DArray= {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.sqrt(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }

final def toDegrees(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math.toDegrees(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }



final def toRadians(vin: RichDouble1DArray): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math. toRadians(vin(i))
          i += 1
      }
      new RichDouble1DArray(v)
  }


final def pow(vin: RichDouble1DArray, exponent:Double): RichDouble1DArray = {
      var  n = vin.length
      var  v = new Array[Double](n)
      var i=0
      while (i<n)  {
          v(i) =  java.lang.Math. pow(vin(i), exponent)
          i += 1
      }
      new RichDouble1DArray(v)
  }


//  Vector relevant  routines 
  
   
    final def diff(v: Vec) = v.diff   


    final def vones(n: Int): Vec = {
         var data = new Vec(n)
         var i=0;   while (i<n) {    data(i) = 1.0; i += 1; }
          data
    }


 final def  vzeros(n: Int): Vec = {
      var data = new Vec(n)
      var i=0;  while (i < n) {  data(i) = 0.0; i += 1; }
      data
  }

final def min(data: Vec): Double = {
  var l = data.length
  var mn = data(0)
  var k = 1
  while  (k <  l) {
    if (data(k) < mn) mn = data(k)
    k += 1
  }
  
mn
}

final def max(data: Vec): Double = {
  var l = data.length
  var mx = data(0)
  var k = 1
  while  (k <  l) {
   if (data(k) > mx) mx = data(k)
   k += 1
  }
mx
}


  
 final def vfill(n: Int, value: Double): Vec = {
      var data = new Vec(n)
      var i=0;  while (i < n) {  data(i) = value; i += 1; }
      data
  }

final def vfill(n: Int, value: Int): Vec = {
      var data = new Vec(n)
      var i=0;  while (i < n) {  data(i) = value; i += 1; }
      data
  }

final def vfill(n: Int, value: Short): Vec = {
      var data = new Vec(n)
      var i=0;  while (i < n) {  data(i) = value; i += 1; }
      data
  }

final def vfill(n: Int, value: Long): Vec = {
      var data = new Vec(n)
      var i=0;  while (i < n) {  data(i) = value; i += 1; }
      data
  }



		

 final def inc(begin: Double, pitch: Double,  end: Double): Vec = {
    var siz = Math.abs((end - begin) / pitch).asInstanceOf[Int]+1
     var doubleArr = new Array[Double](siz)
     var i=0;  while (i < siz) {   doubleArr(i) = begin + i * pitch;  i += 1;  }
   new Vec(doubleArr)
 }


final def sin(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sin(data.data(i))
            i += 1
            }
          ov
    }

    final def cos(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.cos(data.data(i))
            i += 1
            }
          ov
    }

final def tan(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.tan(data.data(i))
            i += 1
            }
          ov
    }

    final def asin(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.asin(data.data(i))
            i += 1
            }
          ov
    }

final def acos(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.acos(data.data(i))
            i += 1
            }
          ov
    }

    final def atan(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.atan(data.data(i))
            i += 1
            }
          ov
    }

    final def sinh(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sinh(data.data(i))
            i += 1
            }
          ov
    }

    final def cosh(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.cosh(data.data(i))
            i += 1
            }
          ov
    }

    final def tanh(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.tanh(data.data(i))
            i += 1
            }
          ov
    }

final def exp(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.exp(data.data(i))
            i += 1
            }
          ov
    }

    final def log(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log(data.data(i))
            i += 1
            }
          ov
    }

    final def log2(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var conv = java.lang.Math.log(2.0)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log(data.data(i))/conv
            i += 1
            }
          ov
    }

    final def log10(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log10(data.data(i))
            i += 1
            }
          ov
    }

    final def abs(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.abs(data.data(i))
            i += 1
            }
          ov
    }

    final def ceil(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.ceil(data.data(i))
            i += 1
            }
          ov
    }

    final def floor(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.floor(data.data(i))
            i += 1
            }
          ov
    }

    final def round(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.round(data.data(i))
            i += 1
            }
          ov
    }

  final def sqrt(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sqrt(data.data(i))
            i += 1
            }
          ov
    }

    final def toDegrees(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.toDegrees(data.data(i))
            i += 1
            }
          ov
    }

    final def toRadians(data: Vec): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.toRadians(data.data(i))
            i += 1
            }
          ov
    }
    
    final def pow(data: Vec, exponent:Double): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.pow(data.data(i), exponent)
            i += 1
            }
          ov
    }
  
final def  mean(data: Vec): Double =  {
       var  mn = 0.0
       var  m = data.length
       var i=0
       while  (i < m) {
            mn += data(i)
            i += 1
          }
        mn /=  m;
       mn
    }

final def sum(data: Vec): Double = {
    var sm = 0.0
    var m = data.length
    var i=0
    while (i < m) {
        sm += data(i)
        i += 1
    }
    sm
}

final def prod(data: Vec): Double = {
    var pd = 1.0
    var m = data.length
    var i=0
    while (i < m) {
        pd *= data(i)
        i += 1
    }
    pd
}

// sum of absolute values
  final def norm1(x: Vec) = {
       var  sm = 0.0
       var  i = 0
       while (i <  x.length) {
            sm += java.lang.Math.abs(x(i))
            i += 1
        }
         sm
  }
  
  // square root of summed squares
  final def norm2(x: Vec) = {
       var  sm = 0.0
       var i = 0
        while  (i <  x.length) {
           sm += x(i)*x(i)
           i += 1  
        }
         java.lang.Math.sqrt(sm)
  }
  
final def norm2_robust(x: Vec) = {
        var scale = 0.0; var  ssq = 1.0
        var i = 0
        while  (i <  x.length)   {
            if (x(i) != 0) {
                var absxi = java.lang.Math.abs(x(i))
                if (scale < absxi) {
                    ssq = 1 + ssq * (scale / absxi) * (scale / absxi);
                    scale = absxi;
                } else
                    ssq += (absxi / scale) * (absxi / scale);
            }
        scale * java.lang.Math.sqrt(ssq)
         i += 1
        }
    }

  
  final def   normInf(x: Vec) = {
        var mx = 0.0
        var i = 0
        while  (i <  x.length)  {
            var curElemAbs = x(i)
            if (curElemAbs <0)  curElemAbs = -curElemAbs
            if (curElemAbs > mx)  mx = curElemAbs
            i += 1  
        }
         mx
    }

  
final def find(V: Vec) = {
   var  n = V.length
   // find number of nonzero elements
   var  no = 0
   var xi = 0
   while  (xi <  n)  {
     if (V(xi) != 0.0)
        no+=1
     xi += 1    
   }
  
  // build return vector
  var   indices = Array.ofDim[Int](no, 1)
  var  i = 0
  var  r = 0
  while  (r <  n)  {
    if (V(r) != 0.0)
         {
  // nonzero element found
  // put element position into return column vector
    indices(i)(0) = r
    i += 1
  }
  r += 1
  }
 
   indices 
}



// Matrix relevant routines

   
//  Reshapes a matrix a to new dimension n X m
final def  reshape(a: Matrix,  n: Int, m: Int): Matrix = {
   var  aCols  = a.Ncols   // columns of matrix 
   var  aRows = a.Nrows   // rows of matrix
   var Nrows = n; var Ncols = m;
  // m,n must be positive
 if (Ncols<=0 || Nrows<=0)  {  Nrows = 1; Ncols = aRows*aCols;  }  // final default to reshaping in a large row
 if (Nrows*Ncols != aRows*aCols)  {
     return   a  // invalid new size: return the original matrix
    }
 var  nm = new Matrix(Nrows, Ncols)  // create the new matrix
// keep two set of indices, i.e. iorig, jorig: indices at the original matrix and inew, jnew: indices at the new matrix
var iorig=1; var jorig=1; var inew=1; var jnew=1
while (iorig < aRows)  {
    while (jorig < aCols)  {
        nm(inew, jnew) = a(iorig, jorig)
        jorig += 1
        jnew += 1
        if (jnew>=Ncols) {  // next row of the reshaped new matrix
            jnew = 1
            inew += 1
           }
      } // jorig < aCols
    iorig += 1
    jorig = 1
  } // iorig < aRows
nm  // return the new Matrix
}

    



 // convert Vec to Matrix
final def vec2Matrix( that: Vec): Matrix = {   
  var Nrows = that.length
  var rmat = new Matrix(Nrows, 1)
  var k = 1
  while  (k <= Nrows)  {
    rmat(k, 1) = that(k-1)
    k += 1
  }
    
 rmat
}

 // COLUMN BASED ROUTINES
// columnwise sum
final def sum(matr: Matrix): Array[Double] = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 1
    while  (ccol < Ncols) {
     sm = 0.0
     var crow = 1
     while  (crow < Nrows) {
       sm += matr.v(crow)(ccol)
       crow += 1
     }
     res(ccol) = sm 
     ccol += 1
     }
    res
}

// columnwise mean
final def mean(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 1
    while (ccol < Ncols) {
     sm=0.0
     var crow = 1
     while  (crow < Nrows) {
       sm += matr.v(crow)(ccol)
       crow += 1
      }
     res(ccol) = sm/(Nrows-1)
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// columnwise product
final def prod(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var pd = 1.0
    var res = new Array[Double](Ncols)
    var  ccol = 1
    while  (ccol < Ncols) {
     pd=1.0
     var crow = 1
     while (crow < Nrows)  {
       pd *= matr.v(crow)(ccol)
       crow += 1
      }
     res(ccol) = pd 
     ccol += 1
     }
  new RichDouble1DArray( res )
}


// columnwise min
final def min(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var res = new Array[Double](Ncols)
    var ccol = 1
    while  (ccol < Ncols) {
     var mn = matr.v(1)(ccol)  
     var crow = 2
     while (crow < Nrows)
        {
       var tmp = matr.v(crow)(ccol)
       if (tmp < mn)  mn = tmp
       crow += 1
       }
     res(ccol) = mn
     ccol += 1
     }
    new RichDouble1DArray(res)
}


// columnwise max
final def max(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var res = new Array[Double](Ncols)
    var ccol = 1
    while  (ccol < Ncols) {
     var mx = matr.v(1)(ccol)  
     var crow = 2
     while  (crow < Nrows)
        {
       var tmp = matr.v(crow)(ccol)
       if (tmp > mx)  mx = tmp
       crow += 1
       }
     res(ccol) = mx
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// ROW BASED ROUTINES

// rowwise sum
final def sumR(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)
    var crow = 1
    while  (crow < Nrows) {
     sm=0.0  // accumulates sum of all row elements
     var  ccol = 1
     while (ccol <  Ncols)  {
        sm += matr.v(crow)(ccol)
        ccol += 1
       }
     res(crow) = sm
     crow += 1
      }
    new RichDouble1DArray(res)
}

// rowwise mean
final def meanR(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)
    var crow = 1
    while (crow < Nrows) {
     sm = 0.0
     var ccol = 1
     while  (ccol < Ncols)  {
       sm += matr.v(crow)(ccol)
       ccol += 1
     }
     res(crow) = sm/(Ncols-1)
     crow += 1 
    }
    new RichDouble1DArray(res)
}

// rowwise product
final def prodR(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var pd = 1.0
    var res = new Array[Double](Nrows)
    var crow = 1
    while  (crow <  Nrows) {
     pd=1.0
     var ccol = 1
     while  (ccol < Ncols)  {
       pd *= matr.v(crow)(ccol)
       ccol += 1
     }
     res(crow) = pd
     crow += 1
     }
    new RichDouble1DArray(res)
}


// rowwise min
final def minR(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var res = new Array[Double](Nrows)
    var crow = 1
    while  (crow < Nrows)  {
     var mn = matr.v(crow)(1)
     var ccol = 2
     while (ccol < Ncols)
        {
       var tmp = matr.v(crow)(ccol)
       if (tmp < mn)  mn = tmp
       ccol += 1
       }
     res(crow) = mn
     crow += 1
     }
    new RichDouble1DArray(res)
}


// rowwise max
final def maxR(matr: Matrix): RichDouble1DArray = {
    var Nrows = matr.Nrows;     var Ncols = matr.Ncols
    var res = new Array[Double](Nrows)
    var crow = 1
    while  (crow < Nrows) {
     var mx = matr.v(crow)(1)
     var ccol = 2
     while (ccol < Ncols)
        {
       var tmp = matr.v(crow)(ccol)
       if (tmp > mx)  mx = tmp
       ccol += 1
       }
     res(crow) = mx
     crow += 1
     }
    new RichDouble1DArray(res)
}


  final def  variance( v: Matrix): RichDouble1DArray = {  // TODO: check
    var n = v.Nrows; var m = v.Ncols;
    var vvar = new Array[Double](m) // columns
    var degrees = n-1
    var c = 0.0; var s = 0.0
    var j=1
    while (j < m) { // all columns
        c = 0.0; s = 0;
        var k=1
        while (k<n) {  // all rows
            s += v(k,j)
            k += 1
           }
        s /= n
        k=1
        while (k<n)  {   // all rows
            c += ( v(k, j)-s)*(v(k, j)-s)
            k += 1
            }
        vvar(j) = c / degrees
        j += 1
     }   // all columns
    new RichDouble1DArray(vvar)
}


 final def stddeviation( v: Matrix): RichDouble1DArray = {  // TODO: check
   var varOfv = variance(v)
   var i=0
   while  (i<varOfv.length) {
     varOfv(i) = Math.sqrt(varOfv(i))
     i+=1
     }
 varOfv
    }

  
final def  covariance( v1: Matrix, v2: Matrix): Matrix  = {   // TODO: check
    var n = v1.Nrows
    var m1 = v1.Ncols
    var m2 = v2.Ncols
    var X = new Matrix(m1, m2)
    var degrees = n-1
    var s1=0.0; var s2 = 0.0; var c = 0.0;
    var i=1; var j=1
    while (i< m1)  {
        j=1
        while  (j<m2) {
            c = 0.0;   s1 = 0.0; s2 = 0.0
            var k=1
            while (k<n)  {
                s1 += v1(k, i)
                s2 += v2(k, i)
                k+=1
            }
          s1 /= m1;  s2 /= m2;
          k=1
          while  (k<n) {    c += (v1(k, i)-s1)*(v2(k, j)-s2); k+=1; }
         X(i, j) = c / degrees
          j+=1
        }
        i+=1
    }
    X    
    }



final def  covariance( v: Matrix):Matrix  = {   // TODO: check
    var n = v.Nrows
    var m = v.Ncols
    var X = new Matrix(m, m)
    var degrees = n-1
    var s1=0.0; var s2 = 0.0; var c = 0.0;
    var i=1; var j=1
    while (i<m)  {
        j=1
        while  (j<m) {
            c = 0.0;   s1 = 0.0; s2 = 0.0
            var k=1
            while  (k<n)  {
                s1 += v(k, i)
                s2 += v(k, i)
                k += 1
            }
         s1 /= m;  s2 /= m;
         k=0
         while (k<n)  {
             c += (v(k, i)-s1)*(v(k, j)-s2)
             k += 1
         }
         X(i, j) = c / degrees
         j+=1
        }
        i+=1
     }
    X
    }

    

  final def diag1(n: Int): Matrix = {
         var om = new Matrix(n, n)
         var i=1;
         while  (i<= n) {
               om.v(i)(i) = 1.0
               i += 1
              }
          om
    }
    
  
  final def diag1(n: Int, m: Int): Matrix = {
         var mn = n; if (mn < m)  mn = m
         var om = new Matrix(mn, mn)
         var i=1;
         while  (i<= n) {
               om.v(i)(i) = 1.0
               i += 1
              }
          om
    }

final def diag1(a: Array[Double], oneIndexed: Boolean): Matrix = {
         var n = a.length-1
         var om = new Matrix(n, n)
         var i=1;
         while  (i<= n) {
               om.v(i)(i) = a(i)
               i += 1
              }
          om
    }

final def diag1(a: Array[Double]): Matrix = {
         var n = a.length
         var om = new Matrix(n, n)
         var i=1
         while  (i<= n) {
               om.v(i)(i) = a(i-1)
               i += 1
              }
          om
    }

     final def ones1(n: Int): Matrix = ones1(n, n)
     final def ones1(n: Int, m:Int): Matrix = {
         var om = new Matrix(n, m)
         var i=1; var j=1;
         while  (i <= n) {
            j=1
            while (j <= m) {
               om.v(i)(j) = 1.0
               j += 1
              }
            i += 1
         }
          om
    }

  
      final def zeros1(n: Int): Matrix = zeros1(n,n)
      final def zeros1(n: Int, m:Int): Matrix = {
         var om = new Matrix(n, m)
         var i=1; var j=1;
         while  (i<= n) {
            j=1
            while (j < m) {
               om.v(i)(j) = 0.0
               j += 1
              }
            i += 1
         }
          om
    }

  final def fill1(n: Int, v: Double): Matrix = fill1(n, n, v)
  final def fill1(n: Int, m:Int, vl: Double): Matrix = {
         var om = new Matrix(n, m)
         var i=1; var j=1;
         while  (i<= n) {
            j=1
            while (j <= m) {
               om.v(i)(j) = vl
               j += 1
              }
            i += 1
         }
          om
    }

    final def rand1(n: Int): Matrix = rand1(n, n)
    final def rand1(n: Int, m:Int): Matrix = {
      var  ran = com.nr.test.NRTestUtil.ran  // global ranno generator
      var om = new Matrix(n, m)
      var i=1; var j=1;
      while  (i<= n) {
            j=1
            while (j <= m) {
             om.v(i)(j) = scalaSci.StaticScalaSciCommonOps.rndGen.doub()
             j += 1
           }
          i += 1
       }
       
          om
    }

  
final def eye1(n: Int): Matrix = {
   eye1(n,n)
}

final def  eye1(n:Int, m:Int): Matrix = {
     var om = new Matrix(n, m)
     var minCoord = n
     if ( m<minCoord)
        minCoord = m
     var i=1 
     while  (i<= minCoord) {
             om.v(i)(i) = 1.0
              i += 1
         }
          om
     }


 final def sin(v: Matrix): Matrix = {
       var Nrows = v.Nrows; var Ncols = v.Ncols;
        var om = new Matrix(Nrows-1, Ncols-1)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               om.v(i)(j) = java.lang.Math.sin(v.v(i)(j))
               j += 1
            }
            i += 1
       }
          om
    }


    final def cos(v: Matrix): Matrix = {
        var Nrows = v.Nrows; var Ncols = v.Ncols;
        var om = new Matrix(Nrows-1, Ncols-1)
        var i=1; var j=1;
        while (i < Nrows) {
            j=1
            while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.cos(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }

final def tan(v: Matrix): Matrix = {
      var Nrows = v.Nrows; var Ncols = v.Ncols;
        var om = new Matrix(Nrows-1, Ncols-1)
        var i=1; var j=1;
        while (i < Nrows) {
            j=1
            while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.tan(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }

    

final def asin(v: Matrix): Matrix = {
      var Nrows = v.Nrows; var Ncols = v.Ncols;
        var om = new Matrix(Nrows-1, Ncols-1)
        var i=1; var j=1;
        while (i < Nrows) {
            j=1
            while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.asin(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }


final def acos(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.acos(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }




final def atan(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.atan(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }

final def sinh(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.sinh(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }



final def cosh(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.cosh(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }



final def tanh(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.tanh(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
 }


final def exp(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.exp(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }

final def log(v: Matrix): Matrix = {
  var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.log(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }


 final def log2(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
var conv = java.lang.Math.log(2.0)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.log(v.v(i)(j))/conv
             j += 1
            }
            i += 1
        }
          om
    }

final def  log10(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
         j=1
         while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.log10(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }


final def abs(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.abs(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }



 final def ceil(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
         j=1
         while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.ceil(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }

  


final def floor(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.floor(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }




 final def round(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
         j=1
         while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.round(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }


   

final def sqrt(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
          j=1
          while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.sqrt(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }



 final def toDegrees(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.toDegrees(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }


 final def toRadians(v: Matrix): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
         j=1
         while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.toRadians(v.v(i)(j))
             j += 1
            }
            i += 1
        }
          om
    }




 final def pow(v: Matrix, exponent:Double): Matrix = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Matrix(Nrows-1, Ncols-1)
   var i=1; var j=1;
     while (i < Nrows) {
           j=1
           while (j < Ncols)  {
             om.v(i)(j) = java.lang.Math.pow(v.v(i)(j), exponent)
             j += 1
            }
            i += 1
        }
          om
    }



final def  transpose(Mt: Matrix): Matrix =  {
   var  transposed = new Matrix(Mt.Ncols-1, Mt.Nrows-1)
   var r = 1
   while (r < Mt.Ncols)  {
     var  c = 1
     while  (c < Mt.Nrows)  {
        transposed(r,c) = Mt(c,r)
        c += 1
     }
     r += 1 
   }
   transposed
}

  final def  T(Mt: Matrix): Matrix =  {
    transpose(Mt)
	}


final def  LU(a: Matrix): (Matrix, Matrix, Matrix) = {
   var aux  = new Array[Double](4)
   var n = a.length
   var p = new Array[Int](n+1) 
   var P = new Matrix(a.Nrows-1, a.Ncols-1)
   var aa = Array.ofDim[Double](a.Nrows, a.Ncols)
   var r = 1
   while  (r < a.Nrows)  {
     var c = 1
     while (c < a.Ncols) {
      aa(r)(c) = a(r,c)
      c += 1
     }
      r += 1
   }
   aux(2) = 1.0e-5
  numal.Linear_algebra.dec(aa, n,  aux, p)

 var k = 1   
 while (k <=  n)  {
   println("p("+k+") == "+p(k))
   k += 1
 }
      
 println()
 // construct permutation matrix
 k = 1
 while ( k <=  n)  {
      P(k, k) = 1
      if (k!=p(k)) {
         P(k, k) = 0
         P(p(k), p(k)) = 0
         P(k, p(k)) = 1
         P(p(k), k) = 1
          }
          k += 1
        }
    
// extract lower and upper matrices from aa
  var lm = Array.ofDim[Double](a.Nrows, a.Ncols)
  var um = Array.ofDim[Double](a.Nrows, a.Ncols)
  r = 1
  while  (r < a.Nrows) {
     var  c = 1
     while  (c <=  r) {
       lm(r)(c) = aa(r)(c)
       c += 1
     }
    r += 1  
  }
  
  r = 1
  while (r < a.Nrows) {
    um(r)(r)=1.0
    var c = r+1
    while  (c < a.Ncols )  {
      um(r)(c) = aa(r)(c)
      c += 1
    }
   um(r)(r) = 1
   
   r += 1
  }


 val oneIndexed = true
 (new Matrix(lm, oneIndexed), new Matrix(um, oneIndexed), P)
}

 
  // solves a well-conditioned linear system of equations Ax=b whose order is small
// relative to the number of binary digits in the number representation
final def sdecsol( a: Matrix, aux: Array[Double], b: Array[Double]): Array[Double] =  {
  numal.Linear_algebra.decsol(a.v, a.Nrows-1, aux, b)
  b
 }

    // copy in place the inverse matrix
 final def decinv( a: Matrix): Matrix = {
     var aux = new Array[Double](4)
     aux(2) = 1.0e-5
     var ainv = new  Matrix(a)
     numal.Linear_algebra.decinv(ainv.v, ainv.Nrows-1, aux)
     ainv
   }


 final def decinv( a: Matrix, aux: Array[Double]): Matrix = {
     numal.Linear_algebra.decinv(a.v, a.Nrows-1, aux)
     a
 }

final def gsssol( a: Matrix, b: Array[Double]): Matrix = {
    var aux = new Array[Double](8)
    aux(2) = 1.0e-5
    aux(4) = 8
    numal.Linear_algebra.gsssol(a.v, a.Nrows-1, aux, b)
    new Matrix(b)
}

final def gsssol( a: Matrix, aux: Array[Double], b: Array[Double]): Matrix = {
    numal.Linear_algebra.gsssol(a.v, a.Nrows-1, aux, b)
    new Matrix(b)
}

  
  
final def eigcom(ar:  Matrix, ai: Matrix, em: Array[Double])= {
    // : (valr: Array[Double], vali: Array[Double], vr: Array[Array[Double]], vi: Array[Array[Double]]) 
    var Nrows = ar.Nrows
    var dvalr = new Array[Double](Nrows); var dvali = new Array[Double](Nrows);
    var dvr = Array.ofDim[Double](Nrows,Nrows); var dvi = Array.ofDim[Double](Nrows,Nrows);

     numal.Linear_algebra.eigcom(ar.v, ai.v, Nrows-1, em, dvalr, dvali, dvr, dvi )
     // return the results as Matrix'es for more convenient handling
     var mdvalr = new Matrix(dvalr)
     var mdvali = new Matrix(dvali)
     var mdvr = new Matrix(dvr)
     var mdvi = new Matrix(dvi)
        (mdvr, mdvi, mdvalr, mdvali)

}

  final def eigcom(ar:  Matrix, ai: Matrix)= {
    // : (valr: Array[Double], vali: Array[Double], vr: Array[Array[Double]], vi: Array[Array[Double]])
    var Nrows = ar.Nrows
    var  em =  new Array[Double](8)
    em(0) = 5.0e-6;  em(2)=1.0e-5; em(4)=10.0; em(6)=10.0;
    var dvalr = new Array[Double](Nrows); var dvali = new Array[Double](Nrows+1);
    var dvr = Array.ofDim[Double](Nrows,Nrows); var dvi = Array.ofDim[Double](Nrows,Nrows);

     numal.Linear_algebra.eigcom(ar.v, ai.v, Nrows-1, em, dvalr, dvali, dvr, dvi )
     var mdvalr = new Matrix(dvalr)
     var mdvali = new Matrix(dvali)
     var mdvr = new Matrix(dvr)
     var mdvi = new Matrix(dvi)
        (mdvr, mdvi, mdvalr, mdvali)
}


  final def eigcom(ar:  Matrix)= {
    // : (valr: Array[Double], vali: Array[Double], vr: Array[Array[Double]], vi: Array[Array[Double]])
    var Nrows = ar.Nrows
    println(" Nrows = "+Nrows)
    var ai = zeros(Nrows, Nrows)
    var  em =  new Array[Double](8)
    em(0) = 5.0e-6;  em(2)=1.0e-5; em(4)=10.0; em(6)=10.0;
    var dvalr = new Array[Double](Nrows); var dvali = new Array[Double](Nrows);
    var dvr = Array.ofDim[Double](Nrows,Nrows); var dvi = Array.ofDim[Double](Nrows,Nrows);

     numal.Linear_algebra.eigcom(ar.v, ai.v, Nrows-1, em, dvalr, dvali, dvr, dvi )
     var mdvalr = new Matrix(dvalr)
     var mdvali = new Matrix(dvali)
     var mdvr = new Matrix(dvr)
     var mdvi = new Matrix(dvi)
        (mdvr, mdvi, mdvalr, mdvali)
}

    final def eig(ar:  Matrix, ai: Matrix, em: Array[Double])= {
    var Nrows = ar.Nrows
    var dvalr = new Array[Double](Nrows); var dvali = new Array[Double](Nrows);
    var dvr = Array.ofDim[Double](Nrows,Nrows); var dvi = Array.ofDim[Double](Nrows,Nrows);

     numal.Linear_algebra.eigcom(ar.v, ai.v, Nrows-1, em, dvalr, dvali, dvr, dvi )
     // return the results as Matrix'es for more convenient handling
     var mdvalr = new Matrix(dvalr)
     var mdvali = new Matrix(dvali)
     var mdvr = new Matrix(dvr)
     var mdvi = new Matrix(dvi)
        (mdvalr, mdvali, mdvr, mdvi)

}

  final def eig(ar:  Matrix, ai: Matrix)= {
    var Nrows = ar.Nrows
    var  em =  new Array[Double](8)
    em(0) = 5.0e-6;  em(2)=1.0e-5; em(4)=10.0; em(6)=10.0;
    var dvalr = new Array[Double](Nrows); var dvali = new Array[Double](Nrows+1);
    var dvr = Array.ofDim[Double](Nrows,Nrows); var dvi = Array.ofDim[Double](Nrows,Nrows);

     numal.Linear_algebra.eigcom(ar.v, ai.v, Nrows-1, em, dvalr, dvali, dvr, dvi )
     var mdvalr = new Matrix(dvalr)
     var mdvali = new Matrix(dvali)
     var mdvr = new Matrix(dvr)
     var mdvi = new Matrix(dvi)
        (mdvalr, mdvali, mdvr, mdvi)
}


  final def eigAll(ar:  Matrix)= {
    var Nrows = ar.Nrows-1
    var arcp = Array.ofDim[Double](Nrows+1, Nrows+1)
      // make a copy of the matrix to pass to comeig1
      var r = 1
      while (r < Nrows)  {
        var c = 1
      while  (c < Nrows)  {
         arcp(r)(c) = ar.v(r)(c)
         c += 1 
      }
      r += 1
   }
      

    var  em =  new Array[Double](10)
    em(0) = 5.0e-6;   // the machine precision
    em(2)=1.0e-5;   // the relative tolerance used for the QR iteration (em(2) > em(0))
    em(4)=10.0;   // the maximum allowed number of iterations
    em(6)=1.0e-5; // the tolerance used for the eigenvectors
    em(8)=5; // the maximum allowed number of inverse iterations for the calculation of each calculation
      // arrays for the real and imaginary parts of the calculated eigenvalues of the given matrix
    var re = new Array[Double](Nrows+1); 
    var im = new Array[Double](Nrows+1);
      // the calculated eigenvectors are delivered in the columns of vec; an eigenvector corresponding to a real eigenvalue given in array re
      // is delivered in the corresponding column of array vec;
      // the real and imaginary part of an eigenvector corresponding to the first member of a nonreal complex conjugate pair
     // of eigenvalues given in the arrays re, im are delivered in the two consecutive columns of array vec corresponding to this pair
     // (the eigenvectors corresponding to the second members of nonreal complex complex conjugate pairs are not delivered,
     // since they are simply the complex conjugate of those corresponding to the first member of such pairs)
    var vec = Array.ofDim[Double](Nrows+1,Nrows+1); 
    numal.Linear_algebra.comeig1(arcp, Nrows, em, re, im, vec )

    var vecEig= Array.ofDim[Double](Nrows, Nrows)  // the eigenvectors
        // the calculated eigenvectors are delivered in the columns of vec
        // one-indexed is also the [][] vec result !!
       r = 0; var c  = 0
     while (r< Nrows) {
       c = 0
       while  (c< Nrows) {
          vecEig(r)(c) = vec(r+1)(c+1)  // copy eigenvectors
          c += 1
       }
       r += 1
     }

    var vecEiv= Array.ofDim[Double](Nrows, 2)  // the eigenvalues
    r = 0
    while  (r<Nrows)  {
        vecEiv(r)(0) = re(r+1)  // real eigenvalues
        vecEiv(r)(1) = im(r+1)  // imaginary eigenvalues
        r += 1
      }
 (vecEig, vecEiv  )

}

  final def eig(ar:  Matrix)= ar.eig()
  

// computes the SVD of a given matrix A
final def svdRank(A: Matrix) = {
  var Ncols = A.Nrows
  var Nrows = A.Ncols
  var singValues = new Array[Double](Nrows+1)    // the singular values
  var U = Array.ofDim[Double](Ncols+1, Nrows+1)
  var V = Array.ofDim[Double](Nrows+1, Nrows+1)

// copy data matrix to U
var r = 1
while (r <= Ncols)  {
  var c = 1
  while  (c<= Nrows)  {
    U(r)(c) = A.v(r)(c)
    c += 1
  }
  
  r += 1    
}

var em = new Array[Double](8)
em(0) = 1.0e-6  // machine's precision
em(2) = 1.0e-5  // the relative precision in the singular values
em(4) = 25.0  // the maximal number of iterations to be performed
em(5) = 1.0e-5   // the minimal nonneglectable singular value

// call the NUMAL routine to perform the SVD
 numal.Linear_algebra.qrisngvaldec(U, Ncols, Nrows, singValues, V, em)

// Return
// em(1): the infinity norm of the matrix
// em(3): the maximal neglected superdiagonal element
// em(5): the number of iterations performed
// em(7): the numerical rank of the matrix; i.e. the number of singular values greater than or equal to em(6)

(new Matrix(U, true), diag1(singValues, true), new Matrix(V, true), em(7), em(1), em(3), em(5))
}

// computes the SVD of a given matrix A
final def svd(A: Matrix) = {
  
var S  = svdRank(A)
  (S._1, S._2, S._3)
}



 

// construct a Matrix from a double [][] array, zero indexed
final def MatrixFromZeroIndexedDArr(vals: Array[Array[Double]]): Matrix = {
     new Matrix(vals)
 }


final def testMatrix(Nrows: Int, Ncols: Int) = {
  var a = new Matrix(Nrows, Ncols)
  var rows = 1
  while (rows <= Nrows) {  
     var cols =1
     while (cols <= Ncols)  {
      a(rows, cols) = (rows-1)*10+cols-1
      cols += 1
     }
     rows += 1
  }
    a
}

final def norm2(Ncols: Matrix): Double = {
  LinearAlgebra.norm2( Ncols.v)
}

final def  norm1(Ncols: Matrix): Double = {
  LinearAlgebra.norm1( Ncols.v)
}

final def  normF(Ncols: Matrix): Double = {
  LinearAlgebra.normF( Ncols.v)
}

final def  normInf(Ncols: Matrix): Double = {
   LinearAlgebra.normInf( Ncols.v)
}
  
final def find(M: Matrix) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   var xi = 1
   while  (xi < n) {
     var yi = 1
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
  var col = 1
  while (col <  m) {
    var row = 1
    while (row < n) {
    
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



//   RichDouble2DArray relevant routines


   // tests the array q if it can be zero within the tolerance tol
   /* e.g.
val x = zeros(20, 20)
val shouldBeZero = approxZero(x)  
x(0,0)=7
val shouldBeZeroIsFalse = approxZero(x) 
*/
 final def approxZero(q: RichDouble2DArray, tol: Double = 0.0000001) = {
     var mxColumns = max(q)  // take columnwise max
     var mxAll = scalaSci.StaticMaths.max(mxColumns)
     if (mxAll < tol) true
     else false
  }
 
 
  
// resamples the RichDouble2DArray every n rows and every m cols
/* can be use for downsampling e.g.
val x = rand(5, 6)
val xr = resample(x, 2, 2)
*/
final def resample(matr: RichDouble2DArray, n: Int,  m: Int):RichDouble2DArray= {
    var matArr = matr.v
    var rows = matr.Nrows
    var cols = matr.Ncols
    var  rRows =  (rows/n).asInstanceOf[Int]
    if (rRows*n == rows-1)
      rRows += 1
    
    var  rCols =  (cols/m).asInstanceOf[Int]
    if (rCols*m == cols-1)
      rCols += 1
    
    var  newMatArr = Array.ofDim[Double](rRows, rCols)
    var r=0; var c=0
    while (r<rRows) {
        c = 0
        while  (c<rCols) {
            newMatArr(r)(c) = matr.v(r*n)(m*c)
            c += 1
          }
      r += 1
}
   var   resampledMat = new RichDouble2DArray(newMatArr)
    resampledMat
}

  
  //  Reshapes a  matrix a to new dimension n X m
 /* e.g.
val x = rand(2, 3)
val xcol = reshape(x, 6, 1)  // reshape as column
val xrow = reshape(x, 1, 6)  // reshape as row
*/
 final def reshape(darr: RichDouble2DArray, n: Int, m: Int): RichDouble2DArray =  {
    var x_dy = darr.Nrows
    var x_dx = darr.Ncols
    if  ((x_dy*x_dx) != (n*m))  { return null }
    // create Matrix
    var values = Array.ofDim[Double](n, m)
    var yii = 0
    var xii = 0
    var xi = 0
    while (xi < m)  {
      var yi = 0
      while  (yi < n) {
          values(yi)(xi) = darr(yii, xii)
          yi += 1
      }
      xi += 1
    }
    
      yii += 1
      if (yii >= x_dy)  {
         yii = 0
         xii += 1
      }
      return  new RichDouble2DArray(values)
     
 }


// columnwise min
final def min(darr: RichDouble2DArray): RichDouble1DArray = {
    var N = darr.Nrows;     var M = darr.Ncols
    var res = new Array[Double](M)
    var ccol = 0 
    while  (ccol < M)   {
     var mn = darr(0, ccol)  
     var  crow = 1
     while  (crow < N)
        {
       var tmp = darr(crow, ccol)
       if (tmp < mn)  mn = tmp
       crow += 1
       }
     res(ccol) = mn
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// columnwise max
final def max(darr: RichDouble2DArray): RichDouble1DArray = {
    var N = darr.Nrows;     var M = darr.Ncols
    var res = new Array[Double](M)
    var ccol = 0
    while  (ccol < M) {
     var mx = darr(0, ccol)  
     var crow = 1
     while  (crow < N)
        {
       var tmp = darr(crow, ccol)
       if (tmp > mx)  mx = tmp
       crow += 1
       }
     res(ccol) = mx
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// columnwise sum
final def sum(darr: RichDouble2DArray): RichDouble1DArray = {
    var N = darr.Nrows;     var M = darr.Ncols
    var sm = 0.0
    var res = new Array[Double](M)
    var ccol = 0
    while  (ccol <  M  )  {
      sm = 0.0
     var crow = 0
     while (crow < N) {
       sm += darr(crow, ccol)
       crow += 1
       }
     res(ccol) = sm 
     ccol += 1
     }
    new RichDouble1DArray(res)
}
 
// columnwise mean
final def mean(darr: RichDouble2DArray): RichDouble1DArray  = {
    var N = darr.Nrows;     var M = darr.Ncols
    var sm = 0.0
    var res = new Array[Double](M)
    var ccol = 0
    while  (ccol < M) {
     sm=0.0
     var crow = 0
     while  (crow < N)  {
       sm += darr(crow, ccol)
       crow += 1
     }
   res(ccol) = sm/N
   ccol += 1
    }
    new RichDouble1DArray(res)
}

// columnwise product
final def prod(darr: RichDouble2DArray): RichDouble1DArray = {
    var N = darr.Nrows;     var M = darr.Ncols
    var pd = 1.0
    var res = new Array[Double](M)
    var ccol = 0
    while  (ccol < M) {
     pd = 1.0
     var crow = 0
     while  (crow < N) {
       pd *= darr(crow, ccol)
       crow += 1
      }
     res(ccol) = pd 
     ccol += 1
     }
    new RichDouble1DArray(res)
}

 final def fill(n: Int, m:Int, vl: Double): RichDouble2DArray = {
       var  v = Array.ofDim[Double](n, m)
       var i = 0; var j = 0;
       while  (i < n) {
            j = 0
            while (j < m) {
              v(i)(j) = vl
              j += 1
              }
            i += 1
         }
          new RichDouble2DArray(v)
    }

final def Fill(n: Int, m:Int, vl: Double): Array[Array[Double]] = {
       var  v = Array.ofDim[Double](n, m)
       var i=0; var j=0;
       while  (i< n) {
            j=0
            while (j < m) {
              v(i)(j) = vl
              j += 1
              }
            i += 1
         }
          v
    }


final def  ones(n:Int, m:Int): RichDouble2DArray = {
     var  v = Array.ofDim[Double](n, m)
       var i=0; var j=0;
       while  (i< n) {
            j=0
            while (j < m) {
              v(i)(j) = 1.0
              j += 1
              }
            i += 1
         }
          new RichDouble2DArray(v)
    }

final def  Ones(n:Int, m:Int): Array[Array[Double]] = {
     var  v = Array.ofDim[Double](n, m)
       var i=0; var j=0;
       while  (i< n) {
            j=0
            while (j < m) {
              v(i)(j) = 1.0
              j += 1
              }
            i += 1
         }
          v
    }


final def  zeros(n:Int, m:Int): RichDouble2DArray = {
     var  v = Array.ofDim[Double](n, m)
       var c=0
       while (c<n) {
          v(c) = new Array[Double](m)
          c += 1
       }
       var i=0; var j=0;
       while  (i< n) {
            j=0
            while (j < m) {
              v(i)(j) = 0.0
              j += 1
              }
            i += 1
         }
          new RichDouble2DArray(v)
    }


final def  Zeros(n:Int, m:Int): Array[Array[Double]] = {
     var  v = Array.ofDim[Double](n, m)
       var c=0
       while (c<n) {
          v(c) = new Array[Double](m)
          c += 1
       }
       var i=0; var j=0;
       while  (i< n) {
            j=0
            while (j < m) {
              v(i)(j) = 0.0
              j += 1
              }
            i += 1
         }
          v
    }

final def rand(n: Int, m:Int): RichDouble2DArray = {
      var  v =Array.ofDim[Double](n, m)
       // use NR  random number generator
      var r, c=0
      
      var numRows = v.length
      var numCols = v(0).length
      while (r<numRows) {
          c=0
          while (c<numCols) {
             v(r)(c) = scalaSci.StaticScalaSciCommonOps.rndGen.doub()
             c += 1
             }
          r += 1
        }
       new RichDouble2DArray(v)
    }

final def Rand(n: Int, m:Int): Array[Array[Double]]= {
      var  v =Array.ofDim[Double](n, m)
       // use NR  random number generator
      var r, c=0
      var numRows = v.length
      var numCols = v(0).length
      while (r<numRows) {
          c=0
          while (c<numCols) {
             v(r)(c) = scalaSci.StaticScalaSciCommonOps.rndGen.doub()
             c += 1
             }
          r += 1
        }
        v
    }

  final def  eye(n:Int, m:Int): RichDouble2DArray = {
     var  v =Array.ofDim[Double](n, m)
      var minCoord = n
     if ( m<minCoord)
        minCoord = m
      var i = 0
     while  (i<minCoord) {
         v(i)(i) = 1.0
         i+=1
     }
     new RichDouble2DArray(v)
    }

final def  eye(n:Int): RichDouble2DArray =  {
    eye(n, n)
}

final def  Eye(n:Int, m:Int): Array[Array[Double]] = {
     var  v =Array.ofDim[Double](n, m)
     var minCoord = n
     if ( m<minCoord)
        minCoord = m
      var i=0
     while  (i<minCoord) {
         v(i)(i) = 1.0
         i+=1
     }
     v
    }

final def  Eye(n:Int): Array[Array[Double]] =  {
    Eye(n, n)
}


  final def sin(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.sin(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  
  final def cos(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.cos(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  

  final def tan(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.tan(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def asin(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.asin(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }



  final def acos(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.acos(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def atan(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; 
        val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
        var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.atan(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  
  
  final def sinh(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
        var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.sinh(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def cosh(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.cosh(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  final def tanh(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.tanh(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def exp(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.exp(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  
  final def log(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
       while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.log(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  
  final def log2(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        val conv = java.lang.Math.log(2.0)
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.log(v(i, j))/conv
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def log10(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.log10(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  


  final def abs(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.abs(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


 
  final def floor(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.floor(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }
    
  
  final def ceil(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.ceil(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }



  

  final def round(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.round(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }


  final def sqrt(v: RichDouble2DArray): RichDouble2DArray = {
        val  N = v.Nrows; val M = v.Ncols;
        val  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.sqrt(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

  

  final def toDegrees(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.toDegrees(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }



  final def toRadians(v: RichDouble2DArray): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.toRadians(v(i, j))
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

 
 
  final def pow(v: RichDouble2DArray, exponent: Double): RichDouble2DArray = {
        val N = v.Nrows; val M = v.Ncols;
        var  vd =Array.ofDim[Double](N, M)
        
       var i=0; var j=0;
        while (i < N) {
            j=0
            while (j < M)  {
              vd(i)(j) = java.lang.Math.pow(v(i, j), exponent)
              j += 1
            }
            i += 1
        }
          new RichDouble2DArray(vd)
    }

/* EJML based routines */


final def   EJMLDenseMatrixtoDoubleArray( dm: org.ejml.data.DenseMatrix64F) = {
    val numR = dm.getNumRows(); val numC = dm.getNumCols()
    val  da = Array.ofDim[Double](numR,  numC)
    var r = 0
    while  (r < numR)  {
      var c = 0
      while  (c < numC)  {
        da(r)( c) = dm.get(r,c)
        c += 1
      }
      r += 1
    }
  
   da
} 
  
// get the inverse using EJML (slightly faster than inv())
  /*
   val N=10
   var a = rand(N, N)
   var ia = einv(a)
   var ii = a*
   */
final def  einv(A: RichDouble2DArray) {
  val  da = new org.ejml.data.DenseMatrix64F(A.getv)
  org.ejml.ops.CommonOps.invert(da)
  new RichDouble2DArray( EJMLDenseMatrixtoDoubleArray(da))
  
}

/*
val N = 10; val M = 8
var a  = rand(N, M)
var b = new RichDouble2DArray (M, N )
var  c = epinv(a, b)
var  cc = pinv(a )
 */

// get the pseudo-inverse using EJML 
final def  epinv(A:  RichDouble2DArray, invA: RichDouble2DArray) = {
  val da = new org.ejml.data.DenseMatrix64F(A.getv)
  val  dinva = new org.ejml.data.DenseMatrix64F(invA.getv)
  
  org.ejml.ops.CommonOps.pinv(da, dinva)
  invA.v = EJMLDenseMatrixtoDoubleArray(dinva)
  
  invA
}


  final def pinv(a: RichDouble2DArray) =  {
    val ejmlM = new scalaSci.EJML.Mat(a.getv)
    val pejml = ejmlM.pinv
    val Nrows = pejml.Nrows
    val Ncols = pejml.Ncols
    var pM = Array.ofDim[Double](Nrows, Ncols)
    var n = 0
    while (n < Nrows)  {
      var m = 0
      while (m < Ncols)  {
        pM(n)(m) = pejml(n, m)
        m += 1
      }
      n += 1
     }
    new RichDouble2DArray(pM)
    
    }
  

final def inv(a: RichDouble2DArray) =  {
  new RichDouble2DArray(scalaSci.math.LinearAlgebra.LinearAlgebra.inverse(a.getv))
}

  // with Native BLAS
final def eigVals(a: RichDouble2DArray) =    {
 val  dmat = new org.jblas.DoubleMatrix(a.v)
 val evals = org.jblas.Eigen.eigenvalues(dmat)
 evals
}

final def eigVecs(a: RichDouble2DArray) =    {
 val  dmat = new org.jblas.DoubleMatrix(a.v)
 val evecs = org.jblas.Eigen.eigenvectors(dmat)
 evecs
}
  
  // compute eigenvalues/eigenvectors
final def eig(A: RichDouble2DArray) = A.eig()

  // compute eigenvalues/eigenvectors, using Numerical Recipes routines
final def nreig(A: RichDouble2DArray) =   com.nr.eig.UnsymmeigScala.unsymmeig(A, true, false)
// compute only eigenvalues
final def nreig(A: RichDouble2DArray, evalsOnly: Boolean ) =   com.nr.eig.SymmeigScala.symmeig(A, false)  

  // compute eigenvalues/eigenvectors of a symmetric matrix
final def seig(A: RichDouble2DArray) =   com.nr.eig.SymmeigScala.symmeig(A, true)
  
// compute only eigenvalues of a symmetric matrix
final def seig(A:  RichDouble2DArray, evalsOnly: Boolean) =   com.nr.eig.SymmeigScala.symmeig(A, false)
    
final def eigNumal(ar:  RichDouble2DArray) = {
    val N = ar.Nrows; val M = ar.Ncols;
         //  the arrays of the NUMAL library are one-indexed, thus copy properly!
    var arcp = Array.ofDim[Double](N+1, N+1)
      // make a copy of the matrix to pass to comeig1
    var   r = 1
    while (r <= N) {
       var c = 1
       while  (c <=  N)  {
         arcp(r)(c) = ar(r-1, c-1)
         c += 1
       }
        r += 1
      }
      
    var  em =  new Array[Double](10)
    em(0) = 5.0e-6;   // the machine precision
    em(2)=1.0e-5;   // the relative tolerance used for the QR iteration (em(2) > em(0))
    em(4)=10.0;   // the maximum allowed number of iterations
    em(6)=1.0e-5; // the tolerance used for the eigenvectors
    em(8)=5; // the maximum allowed number of inverse iterations for the calculation of each calculation
      // arrays for the real and imaginary parts of the calculated eigenvalues of the given matrix
    var re = new Array[Double](N+1); 
    var im = new Array[Double](N+1);
      // the calculated eigenvectors are delivered in the columns of vec; an eigenvector corresponding to a real eigenvalue given in array re
      // is delivered in the corresponding column of array vec;
      // the real and imaginary part of an eigenvector corresponding to the first member of a nonreal complex conjugate pair
     // of eigenvalues given in the arrays re, im are delivered in the two consecutive columns of array vec corresponding to this pair
     // (the eigenvectors corresponding to the second members of nonreal complex complex conjugate pairs are not delivered,
     // since they are simply the complex conjugate of those corresponding to the first member of such pairs)
    var vec = Array.ofDim[Double](N+1,N+1); 
    numal.Linear_algebra.comeig1(arcp, N, em, re, im, vec )
    var vecEig= Array.ofDim[Double](N, N)  // the eigenvectors
        // the calculated eigenvectors are delivered in the columns of vec
        // one-indexed is also the [][] vec result !!
       r = 0; var c  = 0
     while (r< N) {
       c = 0
       while  (c< N) {
          vecEig(r)(c) = vec(r+1)(c+1)  // copy eigenvectors
          c += 1
       }
       r += 1
     }

    var vecEiv= Array.ofDim[Double](N, 2)  // the eigenvalues
    r = 0
    while  (r<N)  {
        vecEiv(r)(0) = re(r+1)  // real eigenvalues
        vecEiv(r)(1) = im(r+1)  // imaginary eigenvalues
        r += 1
      }
 (new RichDouble2DArray(vecEig), new RichDouble2DArray(vecEiv))
}


final def CholeskyL(Md: RichDouble2DArray): RichDouble2DArray = {
 var choleskDec = LinearAlgebra.cholesky(Md.getv)
 var choleskMatDoubles = choleskDec.getL().getArray
 new RichDouble2DArray(choleskMatDoubles)
}

final def  Cholesky_SPD(Md: RichDouble2DArray): RichDouble2DArray = {
      var CholeskyDecomposition  = LinearAlgebra.cholesky(Md.getv)
      var  choleskMatDoubles = CholeskyDecomposition.getL().getArray
      new RichDouble2DArray(choleskMatDoubles)
}


final def   det(Ma: RichDouble2DArray) = {
     LinearAlgebra.det(Ma.getv)
 }

/*
var tstArr:Array[Array[Double]] = Array(Array(4.0, 2.0, -1), Array(-2, 3, 7), Array(2, 3, 1))
var ev = eig(tstArr)
var mtstArr = new Matrix(tstArr)
var mev = eig(mtstArr)
*/
/*
 tstArr = [4, 2, -1;-2, 3, 7;2, 3, 1]
 [ v d ] = eig(tstArr)
*/



// computes the SVD of a given 1-indexed array  A
final def svdRank(A: RichDouble2DArray) = {
  val M = A.Nrows;   val N = A.Ncols
  var singValues = new Array[Double](N)    // the singular values
  var U = Array.ofDim[Double](M, N)
  var V = Array.ofDim[Double](N, N)

// copy data matrix to U
var r = 1
while (r < M) {
  var c = 1
  while (c < N) {
    U(r)(c) = A(r, c)
    c += 1
  }
   r += 1   
}

var em = new Array[Double](8)
em(0) = 1.0e-6  // machine's precision
em(2) = 1.0e-5  // the relative precision in the singular values
em(4) = 25.0  // the maximal number of iterations to be performed
em(5) = 1.0e-5   // the minimal nonneglectable singular value

// call the NUMAL routine to perform the SVD
 numal.Linear_algebra.qrisngvaldec(U, M-1, N-1, singValues, V, em)

// Return
// em(1): the infinity norm of the matrix
// em(3): the maximal neglected superdiagonal element
// em(5): the number of iterations performed
// em(7): the numerical rank of the matrix; i.e. the number of singular values greater than or equal to em(6)

(U,  singValues, V, em(7), em(1), em(3), em(5))
}

// computes the SVD of a given matrix A
final def svd(A: RichDouble2DArray) = A.svd()

  

  
  
final def norm2(M: RichDouble2DArray): Double = {
  LinearAlgebra.norm2( M.getv)
}

final def  norm1(M: RichDouble2DArray): Double = {
  LinearAlgebra.norm1( M.getv)
}

final def  normF(M: RichDouble2DArray): Double = {
  LinearAlgebra.normF( M.getv)
}

final def  normInf(M: RichDouble2DArray): Double = {
   LinearAlgebra.normInf( M.getv)
}

  
final def diag(a: scalaSci.Vec): RichDouble2DArray = {
         var n = a.length
        var  om =Array.ofDim[Double](n, n)
         var i=0;
         while  (i< n) {
               om(i)(i) = a(i)
               i += 1
              }
         new RichDouble2DArray(om)
    }
    
  
final def diag(a: Array[Double]): RichDouble2DArray = {
         var n = a.length
        var  om =Array.ofDim[Double](n, n)
         var i=0;
         while  (i< n) {
               om(i)(i) = a(i)
               i += 1
              }
         new RichDouble2DArray(om)
    }

final def diag(a: RichDouble1DArray): RichDouble2DArray = {
         var n = a.length
        var  om =Array.ofDim[Double](n, n)
         var i=0;
         while  (i< n) {
               om(i)(i) = a(i)
               i += 1
              }
         new RichDouble2DArray(om)
    }

final def diag(n: Int): RichDouble2DArray = {
        var  om =Array.ofDim[Double](n, n)
         var i=0;
         while  (i< n) {
               om(i)(i) = 1.0
               i += 1
              }
         new RichDouble2DArray(om)
    }

  
  final def  cov(v1: RichDouble1DArray, v2: RichDouble1DArray): Double =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1.getv, v2.getv);
     r
 }


final def covariance(v1: RichDouble1DArray, v2: RichDouble1DArray ): Double = {
  var r = cov(v1, v2)
  r
}

final def  cov(v1: RichDouble2DArray, v2: RichDouble2DArray): RichDouble2DArray  =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1.getv, v2.getv);
     new RichDouble2DArray(r)
 }

final def  covariance(v1: RichDouble2DArray, v2: RichDouble2DArray): RichDouble2DArray =   {
     var r = cov(v1, v2)
     r
 }

final def  cov(v: RichDouble2DArray): RichDouble2DArray =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v.getv, v.getv);
     new RichDouble2DArray(r)
 }

 final def  covariance(v: RichDouble2DArray): RichDouble2DArray =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v.getv, v.getv);
     new RichDouble2DArray(r)
 }

final def  corr(v: RichDouble2DArray): RichDouble2DArray =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v.getv, v.getv);
     new RichDouble2DArray(r)
 }


final def  correlation(v: RichDouble2DArray): RichDouble2DArray =   {
     var r =  corr(v,v);
     new RichDouble2DArray(r.getv)
 }


final def  corr(v1: RichDouble1DArray, v2: RichDouble1DArray): Double =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1.getv, v2.getv);
     r
 }


final def correlation(v1: RichDouble1DArray, v2: RichDouble1DArray): Double = {
  var r = corr(v1, v2)
  r
}

final def correlation(v1: RichDouble2DArray, v2: RichDouble2DArray): RichDouble2DArray = {
  var r = corr(v1, v2)
  r
}



final def  corr(v1: RichDouble2DArray, v2: RichDouble2DArray): RichDouble2DArray =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1.getv, v2.getv);
     new RichDouble2DArray(r)
 }


final def  correlation2(v1: RichDouble2DArray, v2: RichDouble2DArray ): RichDouble2DArray = {
    var Varv1 = scalaSci.math.array.StatisticSample.variance(v1.getv)
    var Varv2 = scalaSci.math.array.StatisticSample.variance(v2.getv)
    var cov = scalaSci.math.array.StatisticSample.covariance(v1.getv, v2.getv)
    new RichDouble2DArray (cov )
}

final def find(M: RichDouble2DArray) = {
   var  n = M.Nrows
   var  m = M.Ncols
 // find number of nonzero elements
   var  no = 0
   var  xi = 0
   while  (xi < n) {
     var  yi = 0
     while  (yi <  m)  {
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
    var  row = 0
    while  (row <  n)  {
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


  
  //  MTJBandMatrix relevant routines
    
    final def   random(N: Int,  kl: Int, ku: Int) = {
        var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
       var n = 0
       while  (n < N) {
            ret(n, n) =  java.lang.Math.random
            n += 1
       }
       // form lower band
    var rowElem=0 
    n = 0
    while (n < N)  {
      var lb = 1
      while  (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  java.lang.Math.random
      lb += 1 
      }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n <  N)  {
      var ub = 1
      while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  java.lang.Math.random
        ub += 1 
       }
       n += 1
    }
       ret
}


final def ones(N: Int,  kl: Int, ku: Int) = {
         var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
     var n =  0
     while  (n < N) {
       ret(n, n) =  1.0
       n += 1
     }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb = 1 
      while  (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  1.0
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem = 0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  1.0
         ub += 1
       }
       n += 1
    }
       ret
 }


final def zeros(N: Int,  kl: Int, ku: Int) = {
         var ret = new MTJBandMat(N, kl, ku)
       // form diagonal 
       var n = 0
       while  (n < N) {
            ret(n, n) =  0.0
            n += 1
       }
       
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb = 1
      while  (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  0.0
       lb += 1 
      }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) = 0.0
         ub += 1
       }
       n += 1
    }
       ret
 }


final def fill( N: Int, kl: Int, ku: Int, value: Double) = {
         var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
     var n = 0  
     while  (n < N) {
            ret(n, n) =  value
            n += 1
     }
     
       // form lower band
    var rowElem=0 
    n = 0
    while (n < N)  {
      var lb = 1
      while  (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  value
       lb += 1 
      }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while (ub <= ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  value
        ub += 1 
      }
       n += 1
    }
       ret
 }

  
    
  
final def diag(N: Int,  n: Int, m: Int) = {
    var mn = n; if (m<mn) mn = m
    var ret = new MTJBandMat(N, mn, mn)
    var r, c=0
        while (r<mn) {
          c=0
          while (c<mn) {
             ret(r, c) =  0.0
             c += 1
             }
          r += 1
        }
        r = 0
        while (r<= mn) {
          ret(r, r) = 1.0
          r += 1
        }
        ret
 }
 
  
final def diag(N: Int,  n: Int): MTJBandMat = {
    diag(N, n, n)
 }


final def  sin(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.sin(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.sin(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.sin(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}


final def  cos(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.cos(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.cos(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.cos(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}




final def  tan(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.tan(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.tan(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.tan(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}



final def  acos(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.acos(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.acos(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.acos(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}




final def  asin(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.asin(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.asin(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.asin(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}




final def  atan(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.atan(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.atan(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.atan(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}

  
  

final def  sinh(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.sinh(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.sinh(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.sinh(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}




final def  cosh(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.cosh(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.cosh(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.cosh(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}





final def  tanh(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.tanh(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.tanh(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.tanh(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}



final def  log(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.log(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.log(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.log(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}

  
final def  exp(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.exp(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.exp(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.exp(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}



  
final def  pow(m: MTJBandMat, v: Double) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.pow(m(n, n), v)
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.pow(m(rowElem, n), v)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.pow(m(n, colElem), v)
         ub += 1
       }
       n += 1
    }
       ret
}



  
  
final def  log10(m: MTJBandMat) = {
     val conv = java.lang.Math.log(10.0)
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.log(m(n, n)/conv)
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.log(m(n, n)/conv)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.log(m(n, n)/conv)
         ub += 1
       }
       n += 1
    }
       ret
}



  
  
final def  sqrt(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.sqrt(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.sqrt(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.sqrt(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}


  
final def  abs(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.abs(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.abs(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.abs(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}


  
final def  ceil(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.ceil(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.ceil(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math. ceil(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}


  
  
final def  floor(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.floor(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.floor(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.floor(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}

  
  
  
final def  round(m: MTJBandMat) = {
     var ret = new MTJBandMat(m.N, m.kl, m.ku)
      // form diagonal    
      var n = 0
      while (n< m.N) {
            ret(n, n) =  java.lang.Math.round(m(n, n))
            n += 1
      }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n< m.N)  {
      var lb = 1
      while  (lb <= m.kl) {
         rowElem = n+lb
         if (rowElem < m.N)
           ret(rowElem, n) =  java.lang.Math.round(m(rowElem, n))
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n< m.N)  {
      var ub = 1
      while  (ub <= m.ku) {
         colElem = n+ub
         if (colElem<m.N)
           ret(n, colElem) =  java.lang.Math.round(m(n, colElem))
         ub += 1
       }
       n += 1
    }
       ret
}



  
 // compute the eigenvalue decomposition of general matrix MTJBandMat 
final def  eigAll(m: MTJBandMat) = {
    // compute the eigenvalue decompostion by calling a convenience method for computing the 
    // complete eigenvalue decomposition of the given matrix
    // allocate an EVD object. This EVD object in turn allocates all the necessary space to
   // perform the eigendecomposition, and to keep the results, i.e. the real and imaginary 
   // parts of the eigenvalues and the left and right eigenvectors
   var evdObj = no.uib.cipr.matrix.EVD.factorize(m.getDM)
  
    (evdObj.getRealEigenvalues(), evdObj.getImaginaryEigenvalues(),
     evdObj.getLeftEigenvectors(), evdObj.getRightEigenvectors())
    
  }

// ??TODOTest
final def  eig(m: MTJBandMat) = {
    // compute the eigenvalue decompostion by calling a convenience method for computing the 
    // decomposition of the given matrix given only the right eigenvectors
    // allocate an EVD object. This EVD object in turn allocates all the necessary space to
   // perform the eigendecomposition, and to keep the results, i.e. the real and imaginary 
   // parts of the eigenvalues and the right eigenvectors
   val left = false
   val right = true
   var evdObj = new no.uib.cipr.matrix.EVD(m.numRows, left, right)
   evdObj.factor(new no.uib.cipr.matrix.DenseMatrix(m.getDM))
   
  
    (new scalaSci.RichDouble2DArray(scalaSci.MTJ.Mat.MTJDenseMatrixToDoubleArray(evdObj.getRightEigenvectors())),
     new scalaSci.RichDouble1DArray(evdObj.getRealEigenvalues()))
        
      
  }

  
  //final def eig(): (scalaSci.RichDouble2DArray, scalaSci.RichDouble1DArray) = { (new scalaSci.RichDouble2DArray(1,1),  new scalaSci.RichDouble1DArray(1)) }  // ???
    
  
  
  // computes the singular value decomposition of general matrix MTJBandMat
 final def  svd(m: MTJBandMat)    = {
   // compute the singular value decomposition of the matrix m by calling a convenience method 
   var svdObj = no.uib.cipr.matrix.SVD.factorize(m.getDM)

    // returns a tuple with:
    //     1.   singular values (stored in descending order)
    //     2.   the left singular vectors, column-wise
    //     3.   the right singular vectors, row-wise
    (svdObj.getS,  svdObj.getU.toDoubleArray, svdObj.getVt )
      
 }
  
// Matrix conversion routines
  
def Mat2RD2DA(x: scalaSci.Mat) = new scalaSci.RichDouble2DArray(x.toDoubleArray)
def Mat2Matrix(x: scalaSci.Mat) = new scalaSci.Matrix(x.toDoubleArray)
def Mat2EJMLMat(x: scalaSci.Mat) = new scalaSci.EJML.Mat(x.toDoubleArray)
def Mat2MTJMat(x: scalaSci.Mat) = new scalaSci.MTJ.Mat(x.toDoubleArray)
def Mat2CommonMathsMat(x: scalaSci.Mat) = new scalaSci.CommonMaths.Mat(x.toDoubleArray)
def Mat2JBLASMat(x: scalaSci.Mat) = new scalaSci.JBLAS.Mat(x.toDoubleArray)


def MTJMat2RD2DA(x: scalaSci.MTJ.Mat) = new scalaSci.RichDouble2DArray(x.toDoubleArray)
def MTJMat2Matrix(x: scalaSci.MTJ.Mat) = new scalaSci.Matrix(x.toDoubleArray)
def MTJMat2EJMLMat(x: scalaSci.MTJ.Mat) = new scalaSci.EJML.Mat(x.toDoubleArray)
def MTJMat2Mat(x: scalaSci.MTJ.Mat) = new scalaSci.Mat(x.toDoubleArray)
def MTJMat2CommonMathsMat(x: scalaSci.MTJ.Mat) = new scalaSci.CommonMaths.Mat(x.toDoubleArray)
def MTJMat2JBLASMat(x: scalaSci.Mat) = new scalaSci.JBLAS.Mat(x.toDoubleArray)

def EJMLMat2RD2DA(x: scalaSci.EJML.Mat) = new scalaSci.RichDouble2DArray(x.toDoubleArray)
def EJMLMat2Matrix(x: scalaSci.EJML.Mat) = new scalaSci.Matrix(x.toDoubleArray)
def EJMLMat2Mat(x: scalaSci.EJML.Mat) = new scalaSci.Mat(x.toDoubleArray)
def EJMLMat2MTJMat(x: scalaSci.EJML.Mat) = new scalaSci.MTJ.Mat(x.toDoubleArray)
def EJMLMat2CommonMathsMat(x: scalaSci.EJML.Mat) = new scalaSci.CommonMaths.Mat(x.toDoubleArray)
def EJMLMat2JBLASMat(x: scalaSci.EJML.Mat) = new scalaSci.JBLAS.Mat(x.toDoubleArray)

def JBLASMat2RD2DA(x: scalaSci.JBLAS.Mat) = new scalaSci.RichDouble2DArray(x.toDoubleArray)
def JBLASMat2Matrix(x: scalaSci.JBLAS.Mat) = new scalaSci.Matrix(x.toDoubleArray)
def JBLASMat2EJMLMat(x: scalaSci.JBLAS.Mat) = new scalaSci.EJML.Mat(x.toDoubleArray)
def JBLASMat2MTJMat(x: scalaSci.JBLAS.Mat) = new scalaSci.MTJ.Mat(x.toDoubleArray)
def JBLASMat2CommonMathsMat(x: scalaSci.JBLAS.Mat) = new scalaSci.CommonMaths.Mat(x.toDoubleArray)
def JBLASMat2Mat(x: scalaSci.JBLAS.Mat) = new scalaSci.Mat(x.toDoubleArray)

def CommonMathsMat2RD2DA(x: scalaSci.CommonMaths.Mat) = new scalaSci.RichDouble2DArray(x.toDoubleArray)
def CommonMathsMat2Matrix(x: scalaSci.CommonMaths.Mat) = new scalaSci.Matrix(x.toDoubleArray)
def CommonMathsMat2EJMLMat(x: scalaSci.CommonMaths.Mat) = new scalaSci.EJML.Mat(x.toDoubleArray)
def CommonMathsMat2MTJMat(x: scalaSci.CommonMaths.Mat) = new scalaSci.MTJ.Mat(x.toDoubleArray)
def CommonMathsMat2Mat(x: scalaSci.CommonMaths.Mat) = new scalaSci.Mat(x.toDoubleArray)
def CommonMathsMat2JBLASMat(x: scalaSci.CommonMaths.Mat) = new scalaSci.JBLAS.Mat(x.toDoubleArray)





// perform an SVD using NR
final def nrsvd(dM: Array[Array[Double]]):SvdResults =  {
    var  rsvd = new SvdResults()
    var  nrsvd = new com.nr.la.SVD(dM)
    rsvd.U = nrsvd.u
    rsvd.W = nrsvd.w
    rsvd.V = nrsvd.v
    rsvd
}

final def  nrsvd(dM: RichDoubleDoubleArray):SvdResults =  {
    nrsvd(dM.getv())
}

  // perform an SVD using Apache Common Maths
/*  e.g. 
var N=500; var M=300
var x = rand(N, M)
tic
var nrx = asvd(x)  // perform SVD
var tm = toc()

var shouldBeZero = nrx.U*diag(nrx.W)*(nrx.V~) - x
var shouldBeIdentity = nrx.V*(nrx.V~)  // matrix V is orthogonal
*/
 final def  asvd(x: Array[Array[Double]]):SvdResults = {
    var  rmA = new Array2DRowRealMatrix(x)  // transform it to an Apache Commons Array2DRealMatrix
    var  svdObj = new SingularValueDecomposition(rmA)   // perform an SVD decomposition on A
    var  svdR = new SvdResults()
    svdR.U = svdObj.getU().getData()  // get U matrix as an Array[Array[Double]]
    svdR.W = svdObj.getSingularValues()
    svdR.V = svdObj.getV().getData()
    svdR.conditionNumber = svdObj.getConditionNumber()
    svdR.norm = svdObj.getNorm()
    svdR
}

  // "Adaptive combination of Java matrix libraries for solving basic linear algebra problems"
final def asvd(x: RichDoubleDoubleArray): SvdResults = {
   asvd(x.getv())
   }
   
  
  // perform QR decomposition using Apache Common Maths
  final def  aqr(x: Array[Array[Double]] ): QRResults =  {
    var  rx = new Array2DRowRealMatrix(x)
    var  qrObj = new org.apache.commons.math3.linear.QRDecomposition(rx)
    var  qr = new QRResults()
     qr.Q = qrObj.getQ().getData()
     qr.R = qrObj.getR().getData()
     qr
 }

  final def aqr(x: RichDouble2DArray): QRResults = {
    aqr(x.getv())
        }
        
  // perform QR decomposition using Numerical Recipes 
  final def  nrqr(x: Array[Array[Double]] ): QRResults  =   {
     var  qrdcmp = new com.nr.la.QRdcmp(x)
     var  qr = new QRResults()
     qr.Q = qrdcmp.qt
     qr.R =  qrdcmp.r
      qr
 }
 final def nrqr(x: RichDouble2DArray): QRResults  = {
   nrqr(x.getv())
   }


}                       
               

