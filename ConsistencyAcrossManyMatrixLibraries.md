# Introduction #

`An important advantage of ScalaLab is that it can utilize effectively and flexibly different Java/Scala Matrix libraries. Each such library has its own data representations with the consequent advantages/disadvantages and implements its own numerical routines.`

`However, at the original ScalaSci design, the complexity and non-uniformity of the Matrix libraries, resulted in an inconsistent ScalaSci interface of the Matrix routines. Therefore, we redesign ScalaSci in order to utilize Scala features that enforce the consistency with the help of the Scala compiler. Below we present those design features. `


# Library Independent Functionality #

`Many important classes of ScalaSci, such as the ` _`Vec`_ `, the one-indexed matrix class ` _`Matrix`_ `and the highly convenient ` _`RichDoubleDoubleArray`_ `classes, are independent of the particular zero-indexed Matrix library. Therefore, these routines are always available, independently of the particular zero-indexed library at which the Scala Interpreter switches. `

`We placed the definitions of these libraries in a ` _`Scala trait`_ `, which all the library specific Scala objects mix-in, in order to obtain that functionality. This trait is the ` **`scalaSci.StaticScalaSciGlobal`** `trait`

`Currently, the contents of this trait, that define a large part of library independent functionality are:`

```


package scalaSci

import scalaSci.math.array.DoubleArray
import java.util._
import Jama._
import scalaSci.math.LinearAlgebra.LinearAlgebra
import JSci.maths.wavelet.Signal
import scalaExec.Interpreter.GlobalValues

/*
 
 Defines some global operations available in ScalaSci, independent of the library that the interpreter uses.
 Other static global operations exist in objects RichDoubleArray, RichDoubleDoubleArray, Vec, Matrix, Sparse, CCMatrix
 which are always imported.
 The basic problem that this trait solves is to import properly overloaded versions of a method
 For example the method  sin() has some fixed signatures independent of the particular library,
 which now are:
    sin(x: Double), sin(x: Array[Double]), sin(x: Array[Array[Double]]),  sin(x: Matrix),  sin(x: Vec) 
 (of course we can extend the list with other matrix types, for example sparse matrices).
 
 All the Scala Objects for Static Math Operations (SOSMOs) should mixin this trait in order to have common 
 functionality.  Additionally, each SOSMO should implement its  library specific routines.
 For example the StaticMathsEJML object,  by mixing the StaticScalaSciGlobal trait,
 acquires all the implementations of the sin() for the library independent types.
 Then it implements the sin() for the EJML matrix type as:
   def sin(x: Mat) = {
      scalaSci.EJML.Mat.sin(x)
 }
 
 IMPORTANT: 
   New import statements can overwrite symbols with the same name acquired from previous 
   imports. Thefore usually imports of the SOSMOs should be placed after objects containing
   symbols with the same names.
   For example, if we write:
      import  scalaSci.EJML.StaticMathsEJML._
      import scalaSci.EJML.Mat._
      
 then the statement:
   var  x = sin(3.4)  
 fails,   
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
       
 
  
*/
trait  StaticScalaSciGlobal {
  // some type aliases for convenient code writing
     type AD = Array[Double]
     type AAD = Array[Array[Double]]
     
     type AI = Array[Int]
          
     val PI = java.lang.Math.PI
     
  // define implicit conversions
     implicit def vecToMatrix(x: Vec) = { scalaSci.Matrix.vecToMatrix(x) } // implicit conversion of a Vec to Matrix

     implicit def DoubleToRichNumber(x: Double) = {  new RichNumber(x)}  // implicit conversion of a Double number to RichNumber
     implicit def IntToRichNumber(x: Int) =  { new RichNumber(x)  }  // implicit conversion of a Int number to RichNumber
     implicit def ShortToRichNumber(x: Short) = { new RichNumber(x) }   // implicit conversion of a Short number to RichNumber
     implicit def LongToRichNumber(x: Long) = { new RichNumber(x)  }   // implicit conversion of a Long number to RichNumber

     implicit def DoubleArrayToRichDoubleArray(x: Array[Double]) = {  new RichDoubleArray(x)}  // implicit conversion of an Array[Double] to RichDoubleNumber
     implicit def DoubleDoubleArrayToRichDoubleArray(x: Array[Array[Double]]) =  {  new RichDoubleDoubleArray(x)}  // implicit conversion of an Array[Array[Double]] to RichDoubleDoubleArray
     
     implicit def RichDoubleArrayToVec(x: RichDoubleArray) = {  new Vec(x.getv)}  // implicit conversion of a RichDoubleArray to Vec
     implicit def RichDoubleArrayToDoubleArray(x: RichDoubleArray) = {  x.getv} 
     implicit def RichDoubleDoubleArrayToDoubleDoubleArray(x: RichDoubleDoubleArray) = {  x.getv} 
     
  
// used for plotting routines
   implicit def VecToDoubleArr(x: Vec) = { x.getv }
   implicit def MatrixToDoubleArr(x: Matrix) = { x.getRow(1) }

  
  implicit def ArrayIntToRichDoubleArray(x: Array[Int]) = {
    var N = x.length
    var rda = new Array[Double](N)
    for (k<-0 until N)
      rda(k) = x(k)
    new RichDoubleArray(rda)
  }
  

  def AD(s: String) =   scalaSci.RichDoubleArray.AD(s)
  
  def AAD(s: String) = scalaSci.RichDoubleDoubleArray.AAD(s)
  
  def AAD1(s: String) = scalaSci.RichDoubleDoubleArray.AAD1(s)

  def M1(s: String) =   scalaSci.Matrix.M1(s)
 
import  _root_.scalaSci.Complex

def zeroReal = Complex(0.0) _
object i extends Complex(0.0, 1.0)
implicit def DoubleToComplex(d: Double) = Complex(d)(0.0)

  def V(s: String) =  scalaSci.Vec.V(s) 

     
def abs(x: Double) = {
    java.lang.Math.abs(x)
}

def abs(x: Array[Double]) = {
    scalaSci.RichDoubleArray.abs(x)
}

def abs(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.abs(x)
}

def abs(x: Matrix) = {
    scalaSci.Matrix.abs(x)
}

def abs(x: Vec) = {
    scalaSci.Vec.abs(x)
}

     
def sin(x: Double) = {
    java.lang.Math.sin(x)
}


def sin(x: Array[Double]) = {
    scalaSci.RichDoubleArray.sin(x)
}

def sin(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.sin(x)
}


def sin(x: Matrix) = {
    scalaSci.Matrix.sin(x)
}

def sin(x: Vec) = {
    scalaSci.Vec.sin(x)
}

  
// cos

def cos(x: Double) = {
    java.lang.Math.cos(x)
}


def cos(x: Array[Double]) = {
    scalaSci.RichDoubleArray.cos(x)
}


def cos(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.cos(x)
}

def cos(x: Matrix) = {
    scalaSci.Matrix.cos(x)
}


def cos(x: Vec) = {
    scalaSci.Vec.cos(x)
}


//tan


def tan(x: Double) = {
    java.lang.Math.tan(x)
}


def tan(x: Array[Double]) = {
    scalaSci.RichDoubleArray.tan(x)
}


def tan(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.tan(x)
}

def tan(x: Matrix) = {
    scalaSci.Matrix.tan(x)
}

def tan(x: Vec) = {
    scalaSci.Vec.tan(x)
}


// cosh
def cosh(x: Double) = {
    java.lang.Math.cosh(x)
}

def cosh(x: Array[Double]) = {
    scalaSci.RichDoubleArray.cosh(x)
}


def cosh(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.cosh(x)
}

def cosh(x: Matrix) = {
    scalaSci.Matrix.cosh(x)
}

def cosh(x: Vec) = {
    scalaSci.Vec.cosh(x)
}



// sinh
def sinh(x: Double) = {
    java.lang.Math.sinh(x)
}


def sinh(x: Array[Double]) = {
    scalaSci.RichDoubleArray.sinh(x)
}

def sinh(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.sinh(x)
}

def sinh(x: Matrix) = {
    scalaSci.Matrix.sinh(x)
}

def sinh(x: Vec) = {
    scalaSci.Vec.sinh(x)
}

  

// tanh
def tanh(x: Double) = {
    java.lang.Math.tanh(x)
}

def tanh(x: Array[Double]) = {
    scalaSci.RichDoubleArray.tanh(x)
}


def tanh(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.tanh(x)
}

def tanh(x: Matrix) = {
    scalaSci.Matrix.tanh(x)
}

def tanh(x: Vec) = {
    scalaSci.Vec.tanh(x)
}



// asin

def asin(x: Double) = {
    java.lang.Math.asin(x)
}

def asin(x: Array[Double]) = {
    scalaSci.RichDoubleArray.asin(x)
}

def asin(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.asin(x)
}

def asin(x: Matrix) = {
    scalaSci.Matrix.asin(x)
}


def asin(x: Vec) = {
    scalaSci.Vec.asin(x)
}


    // acos

def acos(x: Double) = {
    java.lang.Math.acos(x)
}

def acos(x: Array[Double]) = {
    scalaSci.RichDoubleArray.acos(x)
}


def acos(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.acos(x)
}

def acos(x: Matrix) = {
    scalaSci.Matrix.acos(x)
}

def acos(x: Vec) = {
    scalaSci.Vec.acos(x)
}


  
// atan

def atan(x: Double) = {
    java.lang.Math.atan(x)
}

def atan(x: Array[Double]) = {
    scalaSci.RichDoubleArray.atan(x)
}


def atan(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.atan(x)
}

def atan(x: Matrix) = {
    scalaSci.Matrix.atan(x)
}

def atan(x: Vec) = {
    scalaSci.Vec.atan(x)
}


  


    // exp

def exp(x: Double) = {
    java.lang.Math.exp(x)
}

def exp(x: Array[Double]) = {
    scalaSci.RichDoubleArray.exp(x)
}


def exp(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.exp(x)
}

def exp(x: Matrix) = {
    scalaSci.Matrix.exp(x)
}


def exp(x: Vec) = {
    scalaSci.Vec.exp(x)
}



  
// pow
def pow(x: Double, value: Double) = {
    java.lang.Math.pow(x, value)
}


def pow(x: Array[Double], value: Double) = {
    scalaSci.RichDoubleArray.pow(x, value)
}


def pow(x: Matrix, value: Double) = {
    scalaSci.Matrix.pow(x, value)
}


def pow(x: Array[Array[Double]], value:Double) = {
    scalaSci.RichDoubleDoubleArray.pow(x, value)
}


def pow(x: Vec, value: Double) = {
    scalaSci.Vec.pow(x, value)
}

def pow(x: RichDoubleArray, value: Double) = {
    scalaSci.RichDoubleArray.pow(x, value)
}

def pow(x: RichDoubleDoubleArray, value: Double) = {
    scalaSci.RichDoubleDoubleArray.pow(x, value)
}

    
  
// log


def log(x: Double) = {
    java.lang.Math.log(x)
}

def log(x: Array[Double]) = {
    scalaSci.RichDoubleArray.log(x)
}


def log(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.log(x)
}

def log(x: Matrix) = {
    scalaSci.Matrix.log(x)
}


def log(x: Vec) = {
    scalaSci.Vec.log(x)
}

    
    // log2


def log2(x: Double) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}


def log2(x: Array[Double]) = {
    scalaSci.RichDoubleArray.log2(x)
}

def log2(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.log2(x)
}

def log2(x: Matrix) = {
    scalaSci.Matrix.log2(x)
}


def log2(x: Vec) = {
    scalaSci.Vec.log2(x)
}

    
// log10

def log10(x: Double) = {
    java.lang.Math.log10(x)
}

def log10(x: Array[Double]) = {
    scalaSci.RichDoubleArray.log10(x)
}


def log10(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.log10(x)
}

def log10(x: Matrix) = {
    scalaSci.Matrix.log10(x)
}

def log10(x: Vec) = {
    scalaSci.Vec.log10(x)
}

  


  
    // ceil

def ceil(x: Double) = {
    java.lang.Math.ceil(x)
}

def ceil(x: Array[Double]) = {
    scalaSci.RichDoubleArray.ceil(x)
}


def ceil(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.ceil(x)
}

def ceil(x: Matrix) = {
    scalaSci.Matrix.ceil(x)
}


def ceil(x: Vec) = {
    scalaSci.Vec.ceil(x)
}


  
    // floor

def floor(x: Double) = {
    java.lang.Math.floor(x)
}

def floor(x: Array[Double]) = {
    scalaSci.RichDoubleArray.floor(x)
}


def floor(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.floor(x)
}

def floor(x: Matrix) = {
    scalaSci.Matrix.floor(x)
}


def floor(x: Vec) = {
    scalaSci.Vec.floor(x)
}

  
  
// round

def round(x: Double) = {
    java.lang.Math.round(x)
}

def round(x: Array[Double]) = {
    scalaSci.RichDoubleArray.round(x)
}

def round(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.round(x)
}

def round(x: Matrix) = {
    scalaSci.Matrix.round(x)
}


def round(x: Vec) = {
    scalaSci.Vec.round(x)
}


    

// sqrt

def sqrt(x: Double) = {
    java.lang.Math.sqrt(x)
}

def sqrt(x: Array[Double]) = {
    scalaSci.RichDoubleArray.sqrt(x)
}


def sqrt(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.sqrt(x)
}

def sqrt(x: Matrix) = {
    scalaSci.Matrix.sqrt(x)
}

def sqrt(x: Vec) = {
    scalaSci.Vec.sqrt(x)
}


  
    // toDegrees

def toDegrees(x: Double) = {
    java.lang.Math.toDegrees(x)
}

def toDegrees(x: Array[Double]) = {
    scalaSci.RichDoubleArray.toDegrees(x)
}


def toDegrees(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.toDegrees(x)
}

def toDegrees(x: Matrix) = {
    scalaSci.Matrix.toDegrees(x)
}


def toDegrees(x: Vec) = {
    scalaSci.Vec.toDegrees(x)
}

def toDegrees(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.toDegrees(x)
}
  
  
 // toRadians

def toRadians(x: Double) = {
    java.lang.Math.toRadians(x)
}


def toRadians(x: Array[Double]) = {
    scalaSci.RichDoubleArray.toRadians(x)
}


def toRadians(x: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.toRadians(x)
}

def toRadians(x: Matrix) = {
    scalaSci.Matrix.toRadians(x)
}


def toRadians(x: Vec) = {
    scalaSci.Vec.toRadians(x)
}

def  diag(n:Int) = {
       scalaSci.RichDoubleDoubleArray.diag(n)
 }

def  diag1(n:Int) = {
       scalaSci.Matrix.diag(n)
 }

 
def  diag(a: Array[Double]) = {
       scalaSci.RichDoubleDoubleArray.diag(a)
 }

def  diag1(a: Array[Double]) = {
       scalaSci.Matrix.diag(a)
}


def  diag(a: scalaSci.Vec) = {
       scalaSci.RichDoubleDoubleArray.diag(a)
}

def  diag1(a: scalaSci.Vec) = {
       scalaSci.Matrix.diag(a)
}

def  diag0(a: scalaSci.Vec) = {
       scalaSci.Mat.diag(a)
}
  
  
def eye1(n: Int): Matrix = {
  scalaSci.Matrix.eye(n)
}

def eye(n: Int) = {
  scalaSci.RichDoubleDoubleArray.eye(n)
}

def eye1(n: Int, m: Int): Matrix = {
  scalaSci.Matrix.eye(n, m)
}

def eye(n: Int, m: Int) = {
  scalaSci.RichDoubleDoubleArray.eye(n, m)
}


def ones1(n: Int, m:Int): Matrix = {
     scalaSci.Matrix.ones(n, m)
}

def ones1(n: Int): Matrix = {
 scalaSci.Matrix.ones(n, n)
}

def ones(n: Int, m: Int)  = {
  scalaSci.RichDoubleDoubleArray.ones(n, m)
} 
  
def ones(n: Int)  = {
  scalaSci.RichDoubleDoubleArray.ones(n, n)
}

def vones(n: Int): Vec = {
        scalaSci.Vec.vones(n)
    }

def zeros(n: Int, m: Int)  = {
  scalaSci.RichDoubleDoubleArray.zeros(n, m)
} 
  
def zeros(n: Int)  = {
  scalaSci.RichDoubleDoubleArray.zeros(n,n)
}

def zeros1(n: Int, m: Int): Matrix = {
  scalaSci.Matrix.zeros(n, m)
}
    
def zeros1(n: Int): Matrix = {
 scalaSci.Matrix.zeros(n, n)
}

 def  vzeros(n: Int): Vec = {
     scalaSci.Vec.vzeros(n)
 }


def rand() = Math.random
  
    // create random Matrix
 def rand1(n: Int, m:Int): Matrix = {
       scalaSci.Matrix.rand(n,m)
    }

def rand1(n: Int): Matrix = {
       scalaSci.Matrix.rand(n,n)
}

// normally distributed with mean 0, and standard deviation 1
def randn(m: Int, n: Int) = {
     var dda = scalaSci.math.array.StatisticSample.randomNormal(m, n, 0.0, 1.0)
     new scalaSci.RichDoubleDoubleArray(dda)
  }
  
 def rand(n: Int, m:Int) = {
        scalaSci.RichDoubleDoubleArray.rand(n, m)
}

  
 def rand(n: Int) = {
        scalaSci.RichDoubleDoubleArray.rand(n, n)
}
  
 
// returns uniformly distributed vectors  
 def vrand(n:Int)  = {
    var dv = scalaSci.math.array.StatisticSample.randomUniform(n, 0.0, 1.0)   
    new Vec(dv)
}

  // returns normally distributed vectors, mean 0.0, standard deviation 1.0
def vrandn(n: Int) = {
  var dv = scalaSci.math.array.StatisticSample.randomNormal(n, 0.0, 1.0)  
  new Vec(dv)
}   
  
  // returns normally distributed vectors, mean  mu, standard deviation sigma
def vrandn(n: Int, mu: Double, sigma: Double) = {
  var dv = scalaSci.math.array.StatisticSample.randomNormal(n, mu, sigma)   
  new Vec(dv)
}   

  
 def vfill(n: Int, value: Double): Vec = {
      scalaSci.Vec.vfill(n, value)
}


def fill(n: Int, m:Int, vl: Double)  = {
       scalaSci.RichDoubleDoubleArray.fill(n, m, vl)
}

def fill(n: Int,  vl: Double)  = {
       scalaSci.RichDoubleDoubleArray.fill(n, n, vl)
}
  
def fill1(n: Int, m:Int, vl: Double): Matrix = {
    scalaSci.Matrix.fill(n, m, vl)
}

def fill1(n: Int, vl:Double): Matrix = {
 scalaSci.Matrix.fill(n, n, vl)
}  

  

def Rand(n: Int, m:Int): Array[Array[Double]] = {
        scalaSci.RichDoubleDoubleArray.Rand(n, m)
 }

 def Rand(n: Int): Array[Array[Double]] = {
        scalaSci.RichDoubleDoubleArray.Rand(n, n)
    }
 
def  Ones(n:Int, m:Int)  = {
      scalaSci.RichDoubleDoubleArray.Ones(n, m)
 }


 def  Ones(n:Int)  = {
        scalaSci.RichDoubleDoubleArray.Ones(n,n)
  }

def  Zeros(n:Int, m:Int)  = {
      scalaSci.RichDoubleDoubleArray.Zeros(n, m)
 }


 def  Zeros(n:Int)  = {
        scalaSci.RichDoubleDoubleArray.Zeros(n, n)
  }
  
  def  Fill(n:Int, m:Int, v: Double)  = {
      scalaSci.RichDoubleDoubleArray.Fill(n, m, v)
 }


 def  Fill(n:Int, v: Double)  = {
        scalaSci.RichDoubleArray.Fill(n, v)
  }
  
  def inc(begin: Double, pitch: Double,  end: Double): Vec = {
     scalaSci.Vec.inc(begin, pitch, end)
    }

 def  Inc(begin: Double, pitch: Double,  end: Double) = {
    scalaSci.RichDoubleArray.Inc(begin, pitch, end)
    }

def linspace(startv: Double, endv: Double) = scalaSci.Vec.linspace(startv, endv)

def linspace(startv: Double, endv: Double, nP: Int) = scalaSci.Vec.linspace(startv, endv, nP)

def logspace(startv: Double, endv: Double, nP: Int, logBase: Double) = scalaSci.Vec.logspace(startv, endv, nP, logBase)

def logspace(startv: Double, endv: Double, nP: Int) = scalaSci.Vec.logspace(startv, endv, nP, 10.0)  // Matlab-like defaults

def logspace(startv: Double, endv: Double) = scalaSci.Vec.logspace(startv, endv, 50, 10.0)  // Matlab-like defaults

def Linspace(startv: Double, endv: Double) = scalaSci.RichDoubleArray.linspace(startv, endv)

def Linspace(startv: Double, endv: Double, nP: Int) = scalaSci.RichDoubleArray.linspace(startv, endv, nP)

def Logspace(startv: Double, endv: Double, nP: Int, logBase: Double) = scalaSci.RichDoubleArray.logspace(startv, endv, nP, logBase)

def Logspace(startv: Double, endv: Double, nP: Int) = scalaSci.RichDoubleArray.logspace(startv, endv, nP, 10.0)  // Matlab-like defaults

def Logspace(startv: Double, endv: Double) = scalaSci.RichDoubleArray.logspace(startv, endv, 50, 10.0)  // Matlab-like defaults

// implicit conversion from Double[Array] to Vec
   def toVector(da: Array[Double]): scalaSci.Vec = {
       scalaSci.Vec.toVector(da)
   }

// dot
def dot(a: scalaSci.Vec, b: scalaSci.Vec): Double = {
   a.dot(b)
   }  

  def dot(a: scalaSci.Matrix, b: scalaSci.Matrix): Double = {
  a.dot(b)
   }  

  
// cross (pointwise) product
def cross(a: scalaSci.Vec, b: scalaSci.Vec): scalaSci.Vec = {
   a.cross(b)
   }  
   
def cross(a: scalaSci.Matrix, b: scalaSci.Matrix): scalaSci.Matrix = {
  a.cross(b)
   }  

    // Signal Processing Routines
 def  fft(sig: Array[Double]) = {
       scalaSci.FFT.ApacheFFT.fft(sig)
   }

 def fft(sig: Vec) = {
     scalaSci.FFT.ApacheFFT.fft(sig)
 }

  
  def ifft(sig: Array[org.apache.commons.math.complex.Complex]) = {
    scalaSci.FFT.ApacheFFT.ifft(sig)
  }

 def getReParts(sig: Array[org.apache.commons.math.complex.Complex]) = {
   scalaSci.FFT.FFTCommon.getReParts(sig)
 }

 def getImParts(sig: Array[org.apache.commons.math.complex.Complex]) = {
   scalaSci.FFT.FFTCommon.getImParts(sig)
 }

def mean(v: Vec): Double = {
    mean(v.getv)
}

def sum(v: Vec):Double = {
    sum(v.getv)
}    

def size(v: Vec): Int =    v.length 

def length(v: Vec): Int =  v.length 

def size(a: scalaSci.Matrix)  =  a.size()

def size(a: scalaSci.RichDoubleDoubleArray)  =  a.size()

def  length(a: scalaSci.Matrix): Int = { a.length }

   
// aggregation routines
// COLUMNWISE 
def sum( v: Matrix)  = {
   scalaSci.Matrix.sum(v)
}


def sum( v: Array[Double]): Double = {
   scalaSci.RichDoubleArray.sum(v)
}

def  sum(v: Array[Array[Double]])  = {
    scalaSci.RichDoubleDoubleArray.sum(v)
}


def mean( v: Matrix)  = {
   scalaSci.Matrix.mean(v)
}

def mean( v: Array[Double]): Double = {
   scalaSci.RichDoubleArray.mean(v)
}

def  mean(v: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.mean(v)
}

def prod( v: Matrix) = {
   scalaSci.Matrix.prod(v)
}

def prod( v: Array[Double]): Double = {
   scalaSci.RichDoubleArray.prod(v)
}

def prod(x: scalaSci.Vec) =   scalaSci.Vec.prod(x)  
  
def prod( v: Array[Int]): Int = {
   scalaSci.RichDoubleArray.prod(v)
}

def prod( v: Array[Float]): Float = {
   scalaSci.RichDoubleArray.prod(v)
}

def  prod(v: Array[Array[Double]]) = {
    scalaSci.RichDoubleDoubleArray.prod(v)
}


def min(darr: Array[Double]): Double = {
   scalaSci.RichDoubleArray.min(darr)
}


def max(darr: Array[Double]): Double = {
   scalaSci.RichDoubleArray.max(darr)
}

def min(darr: Array[Int]): Int= {
   scalaSci.RichDoubleArray.min(darr)
}


def max(darr: Array[Float]): Float = {
   scalaSci.RichDoubleArray.max(darr)
}

  
def max(darr: Array[Int]): Int = {
   scalaSci.RichDoubleArray.max(darr)
}

  def min(darr: Array[Float]): Float= {
   scalaSci.RichDoubleArray.min(darr)
}



def min(darr: Array[Array[Double]]) = {
   scalaSci.RichDoubleDoubleArray.min(darr)
}

def max(darr: Array[Array[Double]])  = {
   scalaSci.RichDoubleDoubleArray.max(darr)
}

def min( v: Matrix): Array[Double] = {
   scalaSci.Matrix.min(v)
}


def min(v: Vec): Double = {
  scalaSci.Vec.min(v)
}


  def min(args: Int*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  def max(args: Int*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg >  mnAll)
          mnAll = arg
      mnAll  
    }
  
  
  def min(args: Float*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  
  def min(args: Double*) =    {
      var mnAll = args(0)
      for (arg <- args)  
        if  (arg < mnAll)
          mnAll = arg
      mnAll  
    }
  
  def max(args: Double*) =    {
      var mxAll = args(0)
      for (arg <- args)  
        if  (arg >  mxAll)
          mxAll = arg
      mxAll  
    }
  
  
  def sum(args: Int*) =    {
      var smAll = 0
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  def sum(args: Double*) =    {
      var smAll = 0.0
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  def sum(args: Float*) =    {
      var smAll = 0.0f
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  
  def prod(args: Int*) =    {
      var prAll = 1
      for (arg <- args)  
          prAll *= arg
      prAll  
    }
  
  def prod(args: Double*) =    {
      var prAll = 1.0
      for (arg <- args)  
          prAll  *= arg
      prAll  
    }
  
  def prod(args: Float*) =    {
      var prAll = 1.0f
      for (arg <- args)  
          prAll *= arg
      prAll  
    }
  
  def max( v: Matrix)  = {
   scalaSci.Matrix.max(v)
}

def max(v: Vec): Double = {
  scalaSci.Vec.max(v)
}

// ROWWISE 
def sumR( v: Matrix) = {
   scalaSci.Matrix.sumR(v)
}



def meanR( v: Matrix) = {
   scalaSci.Matrix.meanR(v)
}

def prodR( v: Matrix) = {
   scalaSci.Matrix.prodR(v)
}


def minR( v: Matrix)  = {
   scalaSci.Matrix.minR(v)
}

def maxR( v: Matrix) = {
   scalaSci.Matrix.maxR(v)
}


// transpose operations
def T(v: Matrix): Matrix = {
  scalaSci.Matrix.T(v)
}


// transpose operations
def trans(v: Matrix): Matrix = {
  scalaSci.Matrix.transpose(v)
}


def transpose(v: Matrix): Matrix = {
  scalaSci.Matrix.transpose(v)
}

def reshape(v: Matrix, n: Int, m: Int): Matrix = {
  scalaSci.Matrix.reshape(v, n, m)
}

def reshape(v: Array[Array[Double]], n: Int, m: Int) = {
  scalaSci.RichDoubleDoubleArray.reshape(v, n, m)
}

def resample(v: Matrix, n: Int, m: Int): Matrix = {
  scalaSci.Matrix.resample(v, n, m)
}

  
def resample(v: RichDoubleDoubleArray, n: Int, m: Int): RichDoubleDoubleArray = {
  scalaSci.RichDoubleDoubleArray.resample(v, n, m)
}

def  corr(v1: Array[Double], v2: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
     r
 }


def correlation(v1: Array[Double], v2: Array[Double]): Double = {
  var r = corr(v1, v2)
  r
}

def  corr(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
     r
 }

def  corr(v1: RichDoubleDoubleArray, v2: RichDoubleDoubleArray) =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1.getv, v2.getv);
     r
 }

def  Var(v: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.variance(v);
     r
 }

def variance(v: Array[Double]): Double = {
  var r = Var(v)
  r
}

def variance(m: Matrix): Array[Double] = {
    scalaSci.Matrix.variance(m)
}

def Var(m: Matrix): Array[Double] = {
    scalaSci.Matrix.variance(m)
}

def  std(v: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.stddeviation(v);
     r
 }

def stddeviation(v: Array[Double]): Double = {
  var r = std(v)
  r
}

def  cov(v1: Array[Double], v2: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1, v2);
     r
 }


def covariance(v1: Array[Double], v2: Array[Double]): Double = {
  var r = cov(v1, v2)
  r
}

def  cov(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1, v2);
     r
 }

def  covariance(v1: Array[Array[Double]], v2: Array[Array[Double]]) =   {
     var r = cov(v1, v2)
     r
 }

def  cov(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }

 def  covariance(v: Array[Array[Double]]) =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }

    
def correlation(v1: Array[Array[Double]], v2: Array[Array[Double]]) = {
  var r = corr(v1, v2)
  r
}


def covariance(v1: Matrix, v2: Matrix): Matrix = {
  scalaSci.Matrix.covariance(v1, v2)
}


def cov(v1: Matrix, v2: Matrix): Matrix = {
  scalaSci.Matrix.covariance(v1, v2)
}




def  corr(v: Array[Array[Double]])  =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v, v);
     r
 }


def  correlation(v: Array[Array[Double]]) =   {
     var r =  corr(v,v);
     r
 }
 

def  det(M: Matrix) = {
  scalaSci.Matrix.det(M)
}

def det(A: Array[Array[Double]]) = {
  scalaSci.RichDoubleDoubleArray.det(A)
}

def rank(M: Matrix): Int = {
  var aux = new Array[Double](8)
  aux(2) = 1.0e-5  // relative tolerance
  aux(4) = 8  // controlling pivoting
  var len = M.length
  var ri = new Array[Int](len+1)
  var ci = new Array[Int](len+1)
  numal.Linear_algebra.gsselm( M.getv, M.length, aux,  ri, ci)
  var rankv:Int = Math.round(aux(3)).asInstanceOf[Int]
  rankv
}  

def trace(M: Matrix): Double = {
   LinearAlgebra.trace( M.getv)
}

def trace(DD: Array[Array[Double]]) = {
LinearAlgebra.trace(DD)
}



//////////////////


// JSCI based routines
//  Compute the L2 norm 
def  norm(vin: Vec): Double = {
   JSci.maths.wavelet.Signal.norm(vin.getv)
 }

def  cfft(vin: Vec): Array[JSci.maths.Complex] = {
   var vlen = length(vin)
   println(" in FFT, vlen = "+vlen)
   var pow2i = floor(log2(vlen))
   var newSize = pow(2, pow2i).asInstanceOf[Int]
   var pow2Sig = vin(0, newSize-1)
   JSci.maths.wavelet.Signal.fft(pow2Sig.getv)
}

def absFFT(vin: Vec)  = {
   var vlen = length(vin)
   var pow2i = floor(log2(vlen))
   var newSize = pow(2, pow2i).asInstanceOf[Int]
   var pow2Sig = vin(0, newSize-1)
   JSci.maths.wavelet.Signal.absFFT(pow2Sig.getv)
}

def cfftInverse(vcompl:  Array[JSci.maths.Complex]): Array[JSci.maths.Complex] = {
   JSci.maths.wavelet.Signal.fftInverse(vcompl)
}

    // gets the real parts from a Complex array
def getRealValues(carr: Array[JSci.maths.Complex]) = {
    var len = carr.length
    var doubleArr = new Array[Double](len)
    for (k<-0 to len-1)
      doubleArr(k) = carr(k).real()
    doubleArr
}
/*
 def inc2d(beginX: Double, pitchX: Double,  endX: Double, beginY: Double, pitchY: Double,  endY: Double): Vec = {
     var siz = ((end - begin) / pitch).asInstanceOf[Int]
     var doubleArr = new Array[Double](siz)
     var i=0;  while (i < siz) {   doubleArr(i) = begin + i * pitch;  i += 1;  }
   new Vec(doubleArr)
	}
*/

def fwt(v: Vec, j: Int) = {
  var sig = new Signal(v.getv)
  var wcoeffs = sig.fwt(j)
  wcoeffs.getCoefs
}

  
  
def pinv(m: scalaSci.Mat): scalaSci.Mat = {
  scalaSci.Mat.pinv(m)
}  

  def pinv(m: scalaSci.Matrix): scalaSci.Matrix = {
  scalaSci.Matrix.pinv(m)
}  

  def pinv(m: scalaSci.EJML.Mat): scalaSci.EJML.Mat = {
  scalaSci.EJML.Mat.pinv(m)
}  

  def pinv(m: scalaSci.MTJ.Mat): scalaSci.MTJ.Mat = {
  scalaSci.MTJ.Mat.pinv(m)
}  

def pinv(m: scalaSci.CommonMaths.Mat): scalaSci.CommonMaths.Mat = {
  scalaSci.CommonMaths.Mat.pinv(m)
}  

def pinv(m: scalaSci.RichDoubleDoubleArray): scalaSci.RichDoubleDoubleArray = {
  scalaSci.RichDoubleDoubleArray.pinv(m)
}    
 
    // some linear algebra operations using the NUMAL library

def inv(m: Matrix): Matrix = {
     var mcopy = new Matrix(m)
     scalaSci.Matrix.decinv(mcopy)
  }


def inv(a: Array[Array[Double]]) = {
  scalaSci.RichDoubleDoubleArray.inv(a)
}


def svdRank(a: Array[Array[Double]]) = {
  scalaSci.RichDoubleDoubleArray.svdRank(a)
}

def svd(a: Array[Array[Double]]) = {
  scalaSci.RichDoubleDoubleArray.svd(a)
}


def eig(da: Array[Array[Double]]) = {
  scalaSci.RichDoubleDoubleArray.eig(da)
}

def eig(m: Matrix) = {
   scalaSci.Matrix.eigcom(m)
 }

 
def eig(ar:  Matrix, ai: Matrix)= {
   scalaSci.Matrix.eig(ar, ai)
}

// return eigenvalues using Native BLAS
  /* test example
   var N = 5
   var a = rand(N, N); 
   tic; var ev = eig(a); var tm = toc
   tic; var evjblas = eigVals(a); var tmjblas = toc
   
   */
  
def eigVals(a: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.eigVals(a)
}
    
def gsssol( a: Matrix, b: Array[Double]): Matrix = {
    scalaSci.Matrix.gsssol(a, b)
   }

def gsssol( a: Matrix, aux: Array[Double], b: Array[Double]): Matrix = {
    scalaSci.Matrix.gsssol(a, aux,  b)
   }

  // determines the generalized inverse A^+ of the mXn matrix A (m >=n). 
def psdInv( a: Matrix):Matrix = {
    var em = new Array[Double](8)
      // pass default values to simplify the interface
    em(0) = 1.0e-14  // the machine precision
    em(2) = 1.0e-12  // the relative precision of singular values
    em(4) = 80.0  // the maximal number of iterations to be performed
    em(6) = 1.0e-10  // the minimal nonneglected singular value
    
    var N = a.Nrows; var M = a.Ncols
        var transPsdInv = new Matrix(a)   // on exit:  the tanspose of the pseudo-inverse
        numal.Linear_algebra.psdinv(transPsdInv.getv, N-1, M-1, em)
        transPsdInv
}


def lu(a: Matrix):(Matrix, Matrix, Matrix) = scalaSci.Matrix.LU(a)



     // determines the eigenvalues
def subm(a: Matrix, rs: Int, incr: Int, re: Int, cs: Int, incc: Int, ce: Int) = {
   a.subm(rs, incr, re, cs, incc, ce)
  }

def submr(a: Matrix, rs: Int, incr: Int, re: Int) = {
   a.submr(rs, incr, re)
  }

def submc(a: Matrix, cs: Int, incc: Int, ce: Int) = {
   a.submc(cs, incc, ce)
  }

def norm1(a: Array[Array[Double]]) =  { scalaSci.RichDoubleDoubleArray.norm1(a) }
def norm1(a: Matrix) =  { scalaSci.Matrix.norm1(a) }
def norm1(a: scalaSci.Mat) =  { scalaSci.Mat.norm1(a) }


def norm2(a: Array[Array[Double]]) =  { scalaSci.RichDoubleDoubleArray.norm2(a) }
def norm2(a: Matrix) =  { scalaSci.Matrix.norm2(a) }
def norm2(a: scalaSci.Mat) =  { scalaSci.Mat.norm2(a) }

def normF(a: Array[Array[Double]]) =  { scalaSci.RichDoubleDoubleArray.normF(a) }
def normF(a: Matrix) =  { scalaSci.Matrix.normF(a) }
def normF(a: scalaSci.Mat) =  { scalaSci.Mat.normF(a) }


def normInf(a: Array[Array[Double]]) =  { scalaSci.RichDoubleDoubleArray.normInf(a) }
def normInf(a: Matrix) =  { scalaSci.Matrix.normInf(a) }
def normInf(a: scalaSci.Mat) =  { scalaSci.Mat.normInf(a) }

  
// Matlab like norm function
def norm(a: Array[Array[Double]], normType: Integer) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}
def norm(a:Matrix, normType: Integer) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}



def Singular_cond(M: Array[Array[Double]]): Double = {
   LinearAlgebra.singular( M).cond()
}


def  Singular_S(M: Array[Array[Double]]) = {
   LinearAlgebra.singular(M).getS().getArray()
}

def  S(M: Array[Array[Double]]) = {
  Singular_S( M)
}

def  Singular_values(M: Array[Array[Double]]) = {
   LinearAlgebra.singular(M).getSingularValues()  
}

def  Singular_U(M: Array[Array[Double]] ) = {
   LinearAlgebra.singular(M).getU().getArray()
}

def   Singular_V(M: Array[Array[Double]])  = {
   LinearAlgebra.singular(M).getV().getArray()
}

def Singular_norm2(M: Array[Array[Double]] ): Double = {
  LinearAlgebra.singular( M).norm2()
}

def  Singular_rank(M: Array[Array[Double]]): Int = {
   LinearAlgebra.singular( M).rank()
}


def  rank(M: Array[Array[Double]]): Int = {
  LinearAlgebra.rank( M)
}

def Eigen_V(M: Array[Array[Double]]) = {
    LinearAlgebra.eigen(M).getV()
}

def V(M: Array[Array[Double]]) = {
   Eigen_V( M)
 }

def  Eigen_D(M: Array[Array[Double]]) = {
   LinearAlgebra.eigen(M).getD()
}

def  D(M: Array[Array[Double]]) = {
   Eigen_D( M)
}

def leig(M: Array[Array[Double]]) = {
  var MNrows = M.length
  var MNcols = M(0).length
    
  var realEvs = new Array[Double](MNrows)
  var imEvs = new Array[Double](MNcols)
  var leftEvecs = Array.ofDim[Double](MNrows, MNcols)
  var rightEvecs = Array.ofDim[Double](MNrows, MNcols)
  scalaSci.ILapack.Eig(M)
  }



def  transpose(Mt: Array[Array[Double]]) =  {
    DoubleArray.transpose(Mt)
	}

  def  T(Mt: Array[Array[Double]]) =  {
     DoubleArray.transpose( Mt ) 
	}
 

//  START:  operations for RichDoubleArray, RichDoubleDoubleArray types
def sin(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.sin(x)
}

def sin(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.sin(x)
}

def cos(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.cos(x)
 
def cos(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.cos(x)

  
def tan(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.tan(x)
 
def tan(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.tan(x)

def cosh(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.cosh(x)
 
def cosh(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.cosh(x)

def sinh(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.sinh(x)
 
def sinh(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.sinh(x)
  
def tanh(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.tanh(x)
 
def tanh(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.tanh(x)
  
  def asin(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.asin(x)
 
def asin(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.asin(x)
 
def acos(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.acos(x)
 
def acos(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.acos(x)

def atan(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.atan(x)
 
def atan(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.atan(x)
  
def exp(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.exp(x)
 
def exp(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.exp(x)

def log(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.log(x)
 
def log(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.log(x)

 def abs(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.abs(x)
 
def abs(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.abs(x)
 
 def ceil(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.ceil(x)
 
def ceil(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.ceil(x)
 
def floor(x: RichDoubleArray) = 
   scalaSci.RichDoubleArray.floor(x)
 
def floor(x: RichDoubleDoubleArray) = 
   scalaSci.RichDoubleDoubleArray.floor(x)

def toRadians(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.acos(x)
}

def toRadians(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.acos(x)
}


def toDegrees(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.toDegrees(x)
}

def sqrt(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.sqrt(x)
}

def sqrt(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.sqrt(x)
}

  def round(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.round(x)
}

def round(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.round(x)
}

def log2(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.log2(x)
}

def log2(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.log2(x)
}

def log10(x: RichDoubleArray) = {
    scalaSci.RichDoubleArray.log10(x)
}

def log10(x: RichDoubleDoubleArray) = {
    scalaSci.RichDoubleDoubleArray.log10(x)
}

def inv(a: RichDoubleDoubleArray) = {
  new RichDoubleDoubleArray(scalaSci.RichDoubleDoubleArray.inv(a.getv))
}
  
def svdRank(a: RichDoubleDoubleArray) = {
  scalaSci.RichDoubleDoubleArray.svdRank(a.getv)
}

def svd(a: RichDoubleDoubleArray) = {
  scalaSci.RichDoubleDoubleArray.svd(a.getv)
}


def eig(da: RichDoubleDoubleArray) = {
  scalaSci.RichDoubleDoubleArray.eig(da.getv)
}
  

def Singular_cond(M: RichDoubleDoubleArray): Double = {
   LinearAlgebra.singular( M.getv).cond()
}

def cond(M: RichDoubleDoubleArray): Double = {
   LinearAlgebra.cond( M.getv)
}

def  Singular_S(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
   new RichDoubleDoubleArray(LinearAlgebra.singular(M.getv).getS().getArray())
}

def  S(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
  Singular_S( M)
}

def  Singular_values(M: RichDoubleDoubleArray): RichDoubleArray = {
   new RichDoubleArray(LinearAlgebra.singular(M.getv).getSingularValues())  
}

def  Singular_U(M: RichDoubleDoubleArray ):  RichDoubleDoubleArray = {
   new RichDoubleDoubleArray(LinearAlgebra.singular(M.getv).getU().getArray())
}

def   Singular_V(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
   new RichDoubleDoubleArray(LinearAlgebra.singular(M.getv).getV().getArray())
}

def Singular_norm2(M: RichDoubleDoubleArray ): Double = {
  LinearAlgebra.singular( M.getv).norm2()
}

def  Singular_rank(M: RichDoubleDoubleArray): Int = {
   LinearAlgebra.singular( M.getv).rank()
}


def  rank(M: RichDoubleDoubleArray): Int = {
  LinearAlgebra.rank( M.getv)
}

def Eigen_V(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
    new RichDoubleDoubleArray(LinearAlgebra.eigen(M.getv).getV())
}

def V(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
   new RichDoubleDoubleArray(Eigen_V( M.getv))
 }

def  Eigen_D(M: RichDoubleDoubleArray) = {
   new RichDoubleDoubleArray(LinearAlgebra.eigen(M.getv).getD())
}

def  D(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
   new RichDoubleDoubleArray(Eigen_D( M.getv))
}

def leig(M: RichDoubleDoubleArray) = {
  var MNrows = M.Nrows
  var MNcols = M.Ncols
    
  var realEvs = new Array[Double](MNrows)
  var imEvs = new Array[Double](MNcols)
  var leftEvecs = Array.ofDim[Double](MNrows, MNcols)
  var rightEvecs = Array.ofDim[Double](MNrows, MNcols)
  scalaSci.ILapack.Eig(M.getv)
  }



def  transpose(Mt: RichDoubleDoubleArray):  RichDoubleDoubleArray =  {
    new RichDoubleDoubleArray(DoubleArray.transpose(Mt.getv))
	}

  def  T(Mt: RichDoubleDoubleArray): RichDoubleDoubleArray =  {
     new RichDoubleDoubleArray(DoubleArray.transpose( Mt .getv) )
	}
 


   
      def rfill(n: Int, m:Int, vl: Double): RichDoubleDoubleArray = {
        new RichDoubleDoubleArray(scalaSci.RichDoubleDoubleArray.Fill(n, m, vl))
    }

    def  rones(n:Int, m:Int): RichDoubleDoubleArray = {
        new RichDoubleDoubleArray(scalaSci.RichDoubleDoubleArray.ones(n, m))
    }


//  END:  operations for RichDoubleArray, RichDoubleDoubleArray types

  def la_eig(a: Array[Array[Double]]) = scalaSci.ILapack.Eig(a)

 
  // compute the determinant of A using  EJML library
  /*
  val a = Rand(3, 3)
  val det_a = ejml_det(a)
  */
 def ejml_det(A: Array[Array[Double]]) = {
   val  ejmlM = new scalaSci.EJML.Mat(A)
   ejmlM.det
 } 
 
 def mtj_det(A: Array[Array[Double]]) = {
   val  mtjM = new scalaSci.MTJ.Mat(A)
   mtjM.det
 } 
  

 
  // compute the trace of A using EJML library
  /*
    val a = Rand(3, 3)
    val trace_a = ejml_trace(a)
*/
 def ejml_trace(A: Array[Array[Double]]) = {
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
 def ejml_inv(A: Array[Array[Double]]) = {
   val ejmlM = new scalaSci.EJML.Mat(A)
   val invM = ejmlM.invert
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   for (r<-0 until nrows)
     for (c<-0 until ncols)
       invDA(r)(c) = invM.get(r,c)
    invDA
 }  
  
 
 def mtj_inv(A: Array[Array[Double]]) = {
   val mtjM = new scalaSci.MTJ.Mat(A)
   val invM = mtjM.inv()
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   for (r<-0 until nrows)
     for (c<-0 until ncols)
       invDA(r)(c) = invM(r,c)
    invDA
 }  
  
  
 def ac_inv(A: Array[Array[Double]]) = {
   val acM = new scalaSci.CommonMaths.Mat(A)
   val invM = acM.inv()
    // return inverse matrix as 2-D double array
   val nrows = A.length
   val ncols = A(0).length
   val invDA = Array.ofDim[Double](nrows, ncols)
   for (r<-0 until nrows)
     for (c<-0 until ncols)
       invDA(r)(c) = invM.get(r,c)
    invDA
 }  

def conditionP2( a: scalaSci.EJML.Mat) =   a.conditionP2

def conditionP(a: scalaSci.EJML.Mat,  p: Double) = a.conditionP(p)  

def conditionP2( a: RichDoubleDoubleArray) =   a.conditionP2

def conditionP(a: RichDoubleDoubleArray,  p: Double) = a.conditionP(p)  
 
def cond(a: scalaSci.EJML.Mat) = a.conditionP2

def cond(a: scalaSci.MTJ.Mat):Double  = cond(a.toDoubleArray())

def cond(a: scalaSci.CommonMaths.Mat):Double  = cond(a.toDoubleArray())

def cond(M: scalaSci.Mat): Double = {
    scalaSci.Mat.cond(M)
}

  
def cond(M: Array[Array[Double]]): Double = {
   LinearAlgebra.cond( M)
}

def find(M: RichDoubleDoubleArray) =
  scalaSci.RichDoubleDoubleArray.find(M)

def find(M: Array[Array[Double]]) =
  scalaSci.RichDoubleDoubleArray.find(new RichDoubleDoubleArray(M))

def find(M: Matrix) = 
  scalaSci.Matrix.find(M)   

def find(v: Vec) = 
  scalaSci.Vec.find(v)

def find(da: Array[Double]) = 
  scalaSci.Vec.find(new Vec(da))

  // solution methods for the "global" types Array[Array[Double]], RichDoubleDoubleArray

 def LU(A: RichDoubleDoubleArray):(RichDoubleDoubleArray,  RichDoubleDoubleArray,  RichDoubleDoubleArray) = { 
    val LUdecomp = LinearAlgebra.LU( A.getv)   // perform the LU decomposition
    val L =  LUdecomp.getL().getArray()
    val U =  LUdecomp.getU().getArray()
    val N = L.length
    var P = Array.ofDim[Double](N, N)
    var p = LUdecomp.getPivot()
 // construct permutation matrix
    for (k<-0 until N)  
      P(k)( p(k)) = 1
 
  (new RichDoubleDoubleArray(L), new RichDoubleDoubleArray(U),  new RichDoubleDoubleArray(P))
 } 

   def la_LUSolve(a: Array[Array[Double]], b: Array[Double]) =
    scalaSci.ILapack.LUSolve(a, b)


def  jama_LUSolve(A: Array[Array[Double]],  b:  Array[Array[Double]]) = {
    val  LUDec = LinearAlgebra.LU(A)
    val  jb = new jMatrix(b)
    val  solvedMat = LUDec.solve(jb)
    solvedMat.getArray()
}

  
def  LU_solve(A: RichDoubleDoubleArray,  b:  RichDoubleDoubleArray): RichDoubleDoubleArray = {
    var  LUDec = LinearAlgebra.LU(A.getv)
    var  jb = new jMatrix(b.getv)
    var  solvedMat = LUDec.solve(jb)
    new  RichDoubleDoubleArray(solvedMat.getArray())
}

def  QR_H(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
  new RichDoubleDoubleArray(LinearAlgebra.QR( M.getv).getH().getArray)
}

def  QR_Q(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
  new RichDoubleDoubleArray(LinearAlgebra.QR( M.getv).getQ().getArray)
}

def  Q(M: RichDoubleDoubleArray): RichDoubleDoubleArray = {
    new RichDoubleDoubleArray(QR_Q( M.getv))
 }

def   QR_R(M: RichDoubleDoubleArray ) = {
   new RichDoubleDoubleArray(LinearAlgebra.QR( M.getv).getR().getArray)
}

def  R(M: RichDoubleDoubleArray) =  {
   QR_R( M)
 }

def QR(M: RichDoubleDoubleArray) = {
  (Q(M), R(M))
}

def lu(A: RichDoubleDoubleArray) = LU(A)
def qr(M: RichDoubleDoubleArray) = QR(M)


def  QR_solve(A: RichDoubleDoubleArray, b: RichDoubleDoubleArray) = {
  var  QRDec = LinearAlgebra.QR(A.getv)
  var  solvedMat = QRDec.solve(new Jama.jMatrix(b.getv))
  new RichDoubleDoubleArray(solvedMat.getArray)
}

  
// global operations on Array[Array[Double]]
  def LU(A: Array[Array[Double]]):(Array[Array[Double]], Array[Array[Double]], Array[Array[Double]]) = { 
    val LUdecomp = LinearAlgebra.LU( A)   // perform the LU decomposition
    val L =  LUdecomp.getL().getArray()
    val U =  LUdecomp.getU().getArray()
    val N = L.length
    var P = Array.ofDim[Double](N, N)
    var p = LUdecomp.getPivot()
 // construct permutation matrix
    for (k<-0 until N)  
      P(k)( p(k)) = 1
 
  (L, U, P)
 } 

  

def  QR_H(M: Array[Array[Double]])  = {
  LinearAlgebra.QR( M).getH().getArray
}

def  QR_Q(M: Array[Array[Double]]) = {
  LinearAlgebra.QR( M).getQ().getArray
}

def  Q(M: Array[Array[Double]]) = {
    QR_Q( M)
 }

def   QR_R(M: Array[Array[Double]]) = {
   LinearAlgebra.QR( M).getR().getArray
}

def  R(M: Array[Array[Double]]) =  {
   QR_R( M)
 }

def QR(M: Array[Array[Double]]) = {
  (Q(M), R(M))
}

def lu(A: Array[Array[Double]]) = LU(A)
def qr(M: Array[Array[Double]]) = QR(M)


def  QR_solve(A: Array[Array[Double]], b: Array[Array[Double]]) = {
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

  
def solve(A: scalaSci.Mat,  b: scalaSci.Mat) = {
    scalaSci.Mat.solve(A, b)
}



  def solve(A: scalaSci.EJML.Mat, b: scalaSci.EJML.Mat) = 
   A.solve(b)
  
  // solve with the CSparse
def solve(A: scalaSci.Sparse,  b: Array[Double]) = {
    scalaSci.Sparse.sparseSolve(A, b)
}

// solve with the MTJ Column Compressed Matrix
def solve(A: scalaSci.CCMatrix, b: Array[Double]) = {
    scalaSci.CCMatrix.BiCGSolve(A, b)
}

  def solve(A: Array[Array[Double]],  b: Array[Array[Double]]) = {
   LinearAlgebra.solve( A, b)
}

  def solve(A: scalaSci.CommonMaths.Mat, b: scalaSci.CommonMaths.Mat): scalaSci.CommonMaths.Mat = 
   new scalaSci.CommonMaths.Mat(solve(A.v, b.v) )
 
  
 def solve(A: scalaSci.JBLAS.Mat, B: scalaSci.JBLAS.Mat) =  {
   new scalaSci.JBLAS.Mat(org.jblas.Solve.solve(A.dm, B.dm))
 }

def solve(A: RichDoubleDoubleArray,  b: RichDoubleDoubleArray) = {
   new RichDoubleDoubleArray(LinearAlgebra.solve( A.getv, b.getv))
}

  def solve(A: RichDoubleDoubleArray,  b: RichDoubleArray): RichDoubleArray = {
    var N = b.length
    var bb = new RichDoubleDoubleArray(N,1)
    for (k<-0 until N)
      bb(k, 0) = b(k)
    var sol = solve(A, bb)
    // convert the solution to a RichDoubleArray
    var x = new Array[Double](sol.numRows)
    for (k<-0 until sol.numRows)
      x(k) = sol(k, 0)
    new RichDoubleArray(x)
    
}

  def solve(A:scalaSci.MTJ.Mat, B: scalaSci.MTJ.Mat) = 
      A.solve(B)

//   applies the predicate to all the array elements and returns true in positions that fullfill the predicate 
//   and false otherwise  

  /* e.g.
  def pred(x: Double) = if (x > 0) true else false
   var Y = vrand(20)-0.5
   var f= filterIndices(Y,  _>0)
  */
 def  filterIndices(X: Array[Double], predicate:  Double => Boolean) = {
      var len = X.length
      var result = new Array[Boolean](len)
      for (k<-0 until len)
        result(k) = if (predicate(X(k)))  true else false
          
    result
  }	

  
  
  // e.g. 
  //   var   rdd = rand0(20, 30)
  //   def pred(k: Int) = if (k % 2 == 0) true else false
//     rdd.filterRows(pred)
// return rows according to the predicate
  def  filterRows(X: RichDoubleDoubleArray, predicate:  Int  => Boolean) = {
      var rowCnt = 0
      for (r<-0 until X.numRows()) {
          if (predicate(r))
           rowCnt += 1
     }
 
    var newMat = new RichDoubleDoubleArray(rowCnt, X.numColumns())
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
 def  filterColumns(X: RichDoubleDoubleArray, predicate:  Int => Boolean) = {
    var colCnt = 0
    for (c<-0 until X.numColumns()) {
      if (predicate(c))
       colCnt += 1
  }
  var cCnt=0
  var newMat = new RichDoubleDoubleArray(X.numRows(), colCnt)
    for (c<-0 until X.numColumns()) {
      if (predicate(c) )  {  // copy the column
      for (r<-0 until  X.numRows()) 
        newMat(r, cCnt) = X(r,c)
      cCnt += 1
      }
    }
      newMat     
  
   }		 


  
}

```


# Abstraction of Matrix Library Dependent Functionality #

`In order to abstract the functionality that each ScalaSci matrix class should implement we have designed two traits, the ` _`scalaSciMatrix`_  `that defines the ` **`mandatory`** `functionality from each Matrix class and the ` _`StaticScalaSciGlobal`_ `trait, that defines also the ` **`mandatory`** `functionality of the static routines that correspond to the currently utilized zero-indexed class. `

`We should note also that this trait defines a large number of in-place operations, e.g. `

```
var a = ones0(20, 30)
a.sin  // take the sine of all matrix elements,in-place, i.e. without creating another matrix 
```

`Each scalaSci matrix wrapper class mixes-in the ` **`scalaSciMatrix`** `trait, an initial code snapshot of which is: `

```

/*
Defines and enforces  the basic functionality of all scalaSci Matrix types.
It also implements the common patterns of functionality for all matrix types.

The user should use these routines in order to have portable scalaSci code, 
that can dirrectly use different lower-level matrix libraries.
 */
package scalaSci

trait  scalaSciMatrix[specificMatrix] {
     var   Nrows: Int   // keeps  the number of rows of the Matrix
     var   Ncols: Int  //  keeps  the number of columns of the Matrix
     
     
     def getv: AnyRef   //  returns the data representation of the Matrix, whatever is, 
           //  common representations are for example, a one-dimensional array of doubles arranged 
           //  in either row-storage format or column-storage format, or a two-dimensional array of doubles
                  
     def length(): Int = this.length   // returns the length of each Matrix object, i.e. the number of its elements
    
     def size(): (Int, Int) = this.size    // returns the number of rows and columns, for banded matrix returns the lower-band width
                                //  and upper band width
     
     def numRows(): Int = this.numRows   // returns the number of rows
     def numColumns(): Int = this.numColumns  // returns the number of columns
     
     def copy(): specificMatrix   // makes a copy of the Matrix . It is not implemented in general terms 
                         //  in order to allow efficient implememntations tha exploit the low-level storage format of a particular matrix
     
  
  // the common apply methods
  def apply(r:Int, c: Int): Double   // gets the element at row r and column c

    
// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ':') corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummyChar). if low>high then rows are returned in reverse
 def apply(rowL: Int, rowH: Int, allColsChar: Char): specificMatrix  = {
   var rowStart = rowL; var rowEnd=rowH;
   var colStart = 0;     var colEnd =  Ncols-1;   // all columns
   var colNum = Ncols
   var colInc = 1

if (rowStart <= rowEnd) {   // positive increment
    var rowInc = 1
    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    var rowNum = rowEnd-rowStart+1
    var  subMatr =  scalaSci.MatrixFactory(this, rowNum, colNum)  // create a Mat to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd )   { 
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       } // crow <= rowEnd
subMatr.asInstanceOf[specificMatrix]  // return the submatrix

} // rowStart <= rowEnd
else { // rowStart > rowEnd
    var rowInc = -1
    var rowNum = rowStart-rowEnd+1
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Mat to keep the extracted range
    //
      // fill the created matrix with values
    var crow = rowStart  // indexes current row at the source matrix
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new Mat
    while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }

subMatr.asInstanceOf[specificMatrix]   // return the submatrix

} // rowStart > rowEnd

}

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ':') corresponds to Matlab's m(2:4:12, :)'
def apply(rowL: Int, rowInc: Int, rowH: Int, allColsChar: Char): specificMatrix = {
    var rowStart = rowL;     var rowEnd =  rowH;
    var colStart = 0;  var colEnd = Ncols-1;   // all columns
    var colNum = Ncols
    var colInc = 1

  if (rowInc > 0) { // positive increment
    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1

      /*SOS-here check for out of range
      var  remainder  = (rowH - rowL)/rowInc
    var  iremainder = remainder.asInstanceOf[Int]
    var diff = remainder  - iremainder
    if (diff>0.0)
        rowNum -= 1
      */
    
    var colStart =0;     var colEnd =  Ncols-1   // all columns
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx = 0; var colIdx = 0  // indexes at the new Mat
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }
     subMatr.asInstanceOf[specificMatrix]   // return the submatrix
     }  // positive increment
  else  {  //  negative increment
     var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
     var subMatr = scalaSci.MatrixFactory(this,  rowNum, colNum)  // create a Matrix to keep the extracted range
        // fill the created matrix with values
     var  crow = rowStart   // indexes current row at the source matrix
     var  ccol = colStart
     var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
     while (crow >= rowEnd)  {
         ccol = colStart;  colIdx = 0
         while (ccol <= colEnd)  {
             subMatr(rowIdx, colIdx) = this(crow, ccol)
             colIdx += 1
             ccol += colInc
         }
         rowIdx += 1
         crow += rowInc
      }
       subMatr.asInstanceOf[specificMatrix]  // return the submatrix
     }  // negative increment
}



// extracts a submatrix, e.g. m( ':', 2,  12 ) corresponds to Matlab's m(:, 2:12)'
  def apply(allRowsChar: Char, colLow: Int,  colHigh: Int): specificMatrix  = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = 1
    var colInc = 1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = colEnd-colStart+1
        var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += rowInc
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]
} // positive increment
  else {  // negative increment
    var colNum = colEnd-colStart+1
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
   }



// extracts a submatrix, e.g. m( ':', 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  def apply(allRowsChar: Char, colLow: Int, colInc: Int, colHigh: Int): specificMatrix = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]
} // positive increment
  else {  // negative increment
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
   }


// extracts a submatrix, e.g. m( 2, 3, 12, 4, 2,  8 ) corresponds to Matlab's m(2:3:12, 4:2:8)'
  def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): specificMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]
} // positive increment
  else if  (rowStart >= rowEnd && colStart <= colEnd)   {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
else if  (rowStart <= rowEnd && colStart >= colEnd)   {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
else {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow > rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
    }
  }


// extracts a specific row, take all columns, e.g. m(2) corresponds to Matlab's m(2, :)'
  def apply(row: Int, ch: Char): specificMatrix = {
    var colStart = 0;     var colEnd =  Ncols-1;   // all columns
    var rowNum = 1;  var colNum = colEnd-colStart+1;
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var ccol = colStart
    while  (ccol <= colEnd)   {
          subMatr(0, ccol) = this(row, ccol)
          ccol += 1
         }

     subMatr.asInstanceOf[specificMatrix]
}


// extracts a specific column, take all rows, e.g. m(':', 2) corresponds to Matlab's m(:,2:)'
  def apply(colonChar:Char, col: Int): specificMatrix = {
    var rowStart = 0;     var rowEnd =  Nrows-1;   // all rows
    var colNum = 1;  var rowNum = rowEnd-rowStart+1;
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart
    while  (crow <= rowEnd)   {
          subMatr(crow-rowStart, 0) = this(crow,  col)
          crow += 1
         }

     subMatr.asInstanceOf[specificMatrix]
}


// extracts a submatrix, e.g. m( 2,  12, 4,   8 ) corresponds to Matlab's m(2:12, 4:8)'
  def apply(rowLow: Int,  rowHigh: Int, colLow: Int, colHigh: Int): specificMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = if (rowHigh > rowLow) 1 else -1
    var colInc = if (colHigh > colLow) 1 else -1

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]
} // positive increment
  else if  (rowStart >= rowEnd && colStart <= colEnd)   {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
else if  (rowStart <= rowEnd && colStart >= colEnd)   {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }
else {
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow > rowEnd
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
   }

   }




              // extracts a submatrix, e.g. m(3:2:7, :)
  def apply(rowLow: Int, rowInc: Int, rowHigh: Int): specificMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh;    if (rowEnd < rowStart) { rowStart = rowHigh; rowEnd = rowLow; }
    var colStart = 1;     var colEnd =  Ncols-1;
    var colInc = 1
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var inc = 1
    var ccol = colStart
    var rowIdx = 0; var colIdx = 0;  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)    {
                subMatr(rowIdx, colIdx) = this(crow, ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }
     subMatr.asInstanceOf[specificMatrix]
}

  

    
 
/* extract the columns specified with indices specified with  the array colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 The columns at the new matrix are arranged in the order specified with the array colIndices
 e.g. 
 var testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(3, 1)
 var extract3_1cols = testMat(':', colIndices)
   */
  def apply(allRowsChar: Char, colIndices: Array[Int]): specificMatrix  = {
    var lv = colIndices.length
    if (lv > Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  colFiltered =  scalaSci.MatrixFactory(this, Nrows, lv)
      for (col<-0 until lv)  {
           var currentColumn = colIndices(col)  // the specified column
           for (row<-0 until Nrows)  // copy the corresponding row
               colFiltered(row, col) = this(row, currentColumn)
       }  
    
      colFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
    } // dimension of array with column indices to use is correct
  }
  


  /* extract the rows specified with indices specified with  the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 The rows at the new matrix are arranged in the order specified with the array rowIndices
 e.g. 
 var testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 var rowIndices = Array(3, 1)
 var extract3_1rows = testMat(rowIndices, ':')
   */
  def apply(rowIndices: Array[Int], allColsChar: Char): specificMatrix = {
    var lv = rowIndices.length
    if (lv > Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  rowFiltered =  scalaSci.MatrixFactory(this, lv, Ncols)
      for (row<-0 until lv)  {
           var currentRow = rowIndices(row)  // the specified row
           for (col<-0 until Ncols)  // copy the corresponding row
               rowFiltered(row, col) = this(currentRow, col)
       }  
    
      rowFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
   } // dimension of array with column indices to use is correct
  }
  
  
  
  
/* extract the columns specified with true values at the array  colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 e.g. 
 var testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(true, false, true, false)
 var extract0_2cols = testMat(':', colIndices)
   */
  def apply(allRowsChar: Char, colIndices: Array[Boolean]): specificMatrix = {
    var lv = colIndices.length
    if (lv != Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }
      else {  // dimension of array with column indices to use is correct
        // count the number of trues
        var ntrues = 0
        for (k<-0 until Ncols)
          if (colIndices(k)==true)  
            ntrues += 1
        
      // allocate array
      var  colFiltered =  scalaSci.MatrixFactory(this, Nrows, ntrues)
      var currentColumn=0
      for (col<-0 until Ncols)  {
         if (colIndices(col))   { // copy the corresponding column
             for (row<-0 until Nrows) 
               colFiltered(row, currentColumn) = this(row, col)
             currentColumn += 1
         }  // copy the corresponding column
      }        
    
      colFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
      
      } // dimension of array with column indices to use is correct
  }
  
  
    
/* extract the rows specified with true values at the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 e.g. 
 var testMat = M(" 1.0 2.0 3.0 ; 5.0 6.0 7.0 ; 8 9 10 ; 11 12 13")
 var rowIndices = Array(false, true, false, true)
 var extract1_3rows = testMat(rowIndices, ':')
   */
  def apply(rowIndices: Array[Boolean], allRowsChar: Char): specificMatrix = {
    var lv = rowIndices.length
    if (lv != Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }
      else {  // dimension of array with row indices to use is correct
        // count the number of trues
        var ntrues = 0
        for (k<-0 until Nrows)
          if (rowIndices(k))  
            ntrues += 1
        
      // allocate array
      var  rowFiltered =  scalaSci.MatrixFactory(this,  ntrues, Ncols)
      
      var currentRow=0
      for (row<-0 until Nrows) 
          if (rowIndices(row))  {  // copy the corresponding row
            for (col<-0 until Ncols)
               rowFiltered(currentRow, col) = this(row, col)
             currentRow += 1
          }
        rowFiltered.asInstanceOf[specificMatrix]
      }  // dimension of array with row indices to use is correct 
          
    }
    
  
     
  
// returns the corresponding row of the Mat class as an Array[Double]
def getRow(row: Int): Array[Double] = {
    var colStart = 0;     var colEnd =  Nrows-1;   // all columns
    var colNum = colEnd-colStart+1;
    var rowArray = new Array[Double](colNum)
    for (ccol<-0 to colEnd)
       rowArray(ccol) = this(row, ccol)
    rowArray
  }

  
// returns the corresponding row of the Mat class as an Array[Double]
def getCol(col: Int): Array[Double] = {
    var rowStart = 0;     var rowEnd =  Nrows-1;   // all rows
    var rowNum = rowEnd-rowStart+1;
    var colArray = new Array[Double](rowNum)
    for (rrow<-0 to rowEnd)
       colArray(rrow) = this(rrow, col)
    colArray
  }
  
  // apply the function f to all the elements of the Matrix and return the results with a new Matrix
 // apply the function f to all the elements of the Mat and return the results with a new Mat

def  map( f: (Double => Double)): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
   
    for (r<-0 until Nrows)
      for (c<-0 until Ncols) 
       mres(r, c) = f(this(r, c) )
   
   mres.asInstanceOf[specificMatrix]
 }
 
  
//   the  Ncols(fromR:incR:toR, fromC:incC:toC)  operation
def  subm(fromR:Int,  incR: Int,  toR: Int, fromC:Int,  incC: Int,  toC: Int)  =  
    apply(fromR, incR, toR, fromC, incC, toC)

//   the  Ncols(fromR:incR:toR, :)  operation
def submr(fromR: Int, incR: Int, toR: Int) = 
    apply(':', fromR, incR, toR)

//   the  Ncols(:, fromC:incC:toC)  operation
def submc(fromC: Int, incC: Int, toC: Int) = 
    apply(fromC, incC, toC, ':')



  /*  filter all the rows/columns of the matrix according to the predicate
   the predicate is a function from the Int index of row/column to a boolean value
   For example, to return all the even numbered rows and columns of a matrix: 
    
      val  x = rand0(10, 13)
      def isEven(n: Int) = if (n % 2 == 0) true else false   // define the predicate
      val xevenRows = x filterRows isEven
      val xevenCols = x filterColumns isEven
   
  */
   
  def  filterRows( predicate:  Int  => Boolean): specificMatrix = {
      var rowCnt = 0
      for (r<-0 until this.numRows()) {
          if (predicate(r))
           rowCnt += 1
     }
    var  newMat =  scalaSci.MatrixFactory(this, rowCnt, this.numColumns())
    var rCnt=0
    for (r<-0 until this.numRows())  {
      if (predicate(r)) {  // copy the row
      for (c<-0 until this.numColumns())
          newMat(rCnt,c) = this(r,c)
          rCnt += 1
                    }
     }
                    
    newMat.asInstanceOf[specificMatrix]     
  
  }		 

  // return cols according to the predicate
 def  filterColumns( predicate:  Int => Boolean): specificMatrix = {
    var colCnt = 0
    for (c<-0 until this.numColumns()) {
      if (predicate(c))
       colCnt += 1
  }
  var cCnt=0
  var newMat = scalaSci.MatrixFactory(this, this.numRows(), colCnt)
    for (c<-0 until this.numColumns()) {
      if (predicate(c) )  {  // copy the column
      for (r<-0 until  this.numRows()) 
        newMat(r, cCnt) = this(r,c)
      cCnt += 1
      }
    }
      newMat.asInstanceOf[specificMatrix]     
  
   }		 

    
  // the common update methods
 def  update(n: Int, m: Int, value: Double): Unit
 
    
  
  // update a single row with index r to have the value v, no automatic resize
 def update(r: Int, v: Double)   {
   val  row  = if ( r < 1)  Nrows+r else r
   var i = 1
   while (i <= Ncols) {
     this(row, i) = v
     i += 1
   }
 }
  
  
 def update(ch: Char, c: Int, v: Vec)  {
   if (v.length != Nrows-1)
      throw new IllegalArgumentException("Nrows (%d) != v.length (%d)".format(Nrows, v.length))
   val col = if (c < 1) Ncols + c  else c
   var i = 1
   while (i <= v.length)  {
     this(i, col) = v(i-1)
     i += 1
   }
 }
 
 def update(r: Int, ch: Char, v: Vec)  {
   if (v.length != Ncols-1)
     throw new IllegalArgumentException("Ncols (%d) != v.length (%d)".format(Ncols, v.length))
   val row = if (r < 1) Nrows + r else r
   var i=1
   while (i <= v.length) {
     this(row, i) = v(i-1)
     i += 1
   }
 }
 
// update a Matrix subrange by assigning a Matrix, e.g. var m=rand0(20, 30);  m( 2, 1, 3, 3, 1, 4) = ones0(2,2)
 def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: Matrix ): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rinc = rincp; var cinc = cincp
    var rlow=rlowp; var rhigh=rlow+mrM*rinc 
    var clow=clowp; var chigh=clow+mrN*cinc
  
        if (rhigh >= Nrows || chigh >= Ncols)  {   
          println("accessing out of range element")
      }   // dynamically increase the size of the matrix

    else {

    val rhighp = rlowp + mrM * rinc
    val chighp = clowp + mrN * cinc
    
     if (rhigh < rlow)  {
            if (rinc > 0)  {
                println("negative row subrange increment is required")
                this
            }
            var tmp=rhighp; var rhigh=rlowp; rlow = tmp;
            rinc  = -rinc
        }
    if (chigh < clow)  {
            if (cinc > 0)  {
                println("negative column subrange increment is required")
                this
            }
            var tmp=chighp; var chigh=clowp; clow = tmp;
            cinc  = -cinc
        }
    
     var rangeLenRow = ((rhigh-rlow)/rinc).asInstanceOf[Int]+1    // row length of target range
     var rangeLenCol = ((chigh-clow)/cinc).asInstanceOf[Int]+1    // col length of target range

     
  
     // copy the values of the mr
        var rrow=1; var rcol=1; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=1
            lcolidx = clow   // starting column within the "subassigned" matrix
            while (rcol < mr.Ncols)  {   // for all cols of the right-hand side matrix
                this(lrowidx, lcolidx) = mr(rrow, rcol)
                lcolidx += cinc
                rcol += 1
            }
            lrowidx += rinc
            rrow += 1
          }

        }
 }



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand(20, 30);  mm(2, 3, ':') = ones(2,2);
 def update(rlowp:Int, clowp:Int, ch: Char, mr: Matrix): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rlow=rlowp; var rhigh=rlow+mrM; var rinc = 1;
    var clow=clowp; var chigh=clow+mrN; var cinc = 1;
        
     if (rhigh >= Nrows || chigh >= Ncols)  {   // dynamically increase the size of the matrix when subassigning out of its range
       println("accessing out of range element")
     }   // dynamically increase the size of the matrix

    else {

     // copy the values of the mr
        var rrow=1; var rcol=1; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=1
            lcolidx = clow   // starting column within the "subassigned" matrix
            while (rcol < mr.Ncols)  {   // for all cols of the right-hand side matrix
                this(lrowidx, lcolidx) = mr(rrow, rcol)
                lcolidx += cinc
                rcol += 1
            }
            lrowidx += rinc
            rrow += 1
          }

        }
 }


 def print() =  {
   val digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    for (r <- 0 until  Nrows) {
          for (c <- 0 until  Ncols) 
            System.out.print(digitFormat.format(this(r, c)) + "  ")
          println("\n")
      }
   }
   
  
def p = print   // short "print" method

  
    // ROW - APPEND MAT TO MAT
// append rowwise the Matrix Ncols
def  ::(rowsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (rowsToAppend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToAppend.Nrows   //  number of rows of the new matrix
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
    // copy "this" Matrix
    for (r<-0 to this.Nrows-1)
       for (c<-0 to this.Ncols-1)
         res(r, c) = this(r, c)
     
    for (r<-0 to rowsToAppend.Nrows-1)
       for (c<-0 to rowsToAppend.Ncols-1)
         res(Nrows+r, c) = rowsToAppend(r, c)
    res.asInstanceOf[specificMatrix]
}


    // ROW - PREPEND MAT TO MAT
// prepend rowwise the Matrix Ncols
def  :::(rowsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (rowsToAppend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToAppend.Nrows   // new number of rows
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
    // copy "this" Matrix
    for (r<-0 to rowsToAppend.Nrows-1)
       for (c<-0 to rowsToAppend.Ncols-1)
         res(r, c) = rowsToAppend(r, c)
    for (r<-0 to this.Nrows-1)
       for (c<-0 to this.Ncols-1)
         res(rowsToAppend.Nrows+r, c) = this(r, c)

    res.asInstanceOf[specificMatrix]
}


    // COL - APPEND MAT TO MAT
// append columnwise the Matrix Ncols
def  >::(colsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (colsToAppend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToAppend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Ncols, excols)
 
    // copy "this" Matrix
    for (r<-0 to this.Nrows-1)
       for (c<-0 to this.Ncols-1)
         res(r, c) = this(r, c)
    for (r<-0 to colsToAppend.Nrows-1)
       for (c<-0 to colsToAppend.Ncols-1)
         res(r, Ncols+c) = colsToAppend(r, c)
    res.asInstanceOf[specificMatrix]
}


    // COL - PREPEND MAT TO MAT
// prepend columnwise the Matrix Ncols
def  >:::(colsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (colsToAppend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToAppend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)
    // copy "this" Matrix
    for (r<-0 to colsToAppend.Nrows-1)
       for (c<-0 to colsToAppend.Ncols-1)
         res(r, c) = colsToAppend(r, c)
    for (r<-0 to this.Nrows-1)
       for (c<-0 to this.Ncols-1)
         res(r, colsToAppend.Ncols+c) = this(r, c)

    res.asInstanceOf[specificMatrix]
}

   
  // IN-PLACE Operations: Update directly the receiver, avoiding creating a new return object

    // Mat + Mat
def ++ (that: Mat): specificMatrix =  {
  if (Nrows != that.Nrows || Ncols != that.Ncols) {  // incompatible dimensions
      println("incompatible matrix dimensions in ++")  
      this.asInstanceOf[specificMatrix]
  }
      else {
         var i=0; var j=0;
        while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) += that(i, j)
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
  }
}

    // Mat + Double
def ++ (that: Double): specificMatrix  =  {
  var i=0; var j=0
  while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) += that
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
}



    // Mat - Mat
def -- (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
  if (Nrows != that.Nrows || Ncols != that.Ncols) { // incompatible dimensions
       println("incompatible matrix dimensions in --")  
       this.asInstanceOf[specificMatrix]
   }
     else {
         var i=0; var j=0;
        while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) -= that(i, j)
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
  }
}

    // Mat - Double
def -- (that: Double): specificMatrix =  {
  var i=0; var j=0
  while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) -= that
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
}

    // Mat * Double
def ** (that: Double): specificMatrix =  {
  var i=0; var j=0
  while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) *= that
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
}


    // Mat / Double
def /| (that: Double): specificMatrix =  {
  var i=0; var j=0
  while (i<Nrows) {
       j=0
    while (j<Ncols) {
      this(i, j) /= that
      j +=1
    }
    i += 1
   }
   this.asInstanceOf[specificMatrix]
}

  
    // END OF IN-PLACE OPERATIONS

  
// convert to Vec
def toVec(): Vec = {
  var v = new Vec(Nrows*Ncols)
  var cnt=0
  for (r<-0 until Nrows)
    for (c<-0 until Ncols)  {
      v(cnt) = this(r, c)
      cnt += 1
      }
    v
  }
  
def toDoubleArray()   = {
    var da = Array.ofDim[Double](Nrows, Ncols)
    for (r<-0 until Nrows)
      for (c<-0 until Ncols)
        da(r)(c) = this(r,c)
    da
}

  

// Mat + Double
def + (that: Double): specificMatrix =  {
   var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
   var i=0; var j=0;
   while (i<Nrows) {
       j=0
    while (j<Ncols) {
      nv(i, j) = this(i, j)+that
      j +=1
    }
    i += 1
   }
   nv.asInstanceOf[specificMatrix]
}

// Mat - Double
def - (that: Double): specificMatrix =  {
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(i, j) = this(i, j)-that
      j += 1
    }
    i += 1
   }
   nv.asInstanceOf[specificMatrix]
}


    // Mat -< Vec : column oriented subtraction
def -< (that: Vec): specificMatrix =  {
    var vl = that.length
    if (vl != this.Nrows)  {
      println("vector length not equal to the number of rows in -<")
      return this.asInstanceOf[specificMatrix]
    }
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
  var i=0; var j=0
  while (j<Ncols) {   // for all columns
       i=0
    while (i<Nrows) {  // for all rows 
      nv(i, j) = this(i, j)-that(i)
      i +=1
    }
    j += 1
   }
   nv.asInstanceOf[specificMatrix]
}

   // Mat +< Vec : column oriented subtraction
def +< (that: Vec): specificMatrix =  {
    var vl = that.length
    if (vl != this.Nrows)  {
      println("vector length not equal to the number of rows in +<")
      return this.asInstanceOf[specificMatrix]
    }
    var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
    var i=0; var j=0
   while (j<Ncols) {   // for all columns
       i=0
    while (i<Nrows) {  // for all rows 
      nv(i, j) = this(i, j)+that(i)
      i +=1
    }
    j += 1
   }
   nv.asInstanceOf[specificMatrix]
}

   // Mat (NXM)*< Vec(MX1) : Mat(NX1) Matrix-Vector multiplication
def *< (that: Vec): specificMatrix =  {
    var vl = that.length
    if (vl != this.Ncols )  {
      println("vector length not equal to the number of rows in *<")
      return this.asInstanceOf[specificMatrix]
    }
    var sm=0.0
    var nv  = scalaSci.MatrixFactory(this, this.Nrows, 1)
    for (r<-0 to Nrows-1) {      // all rows of the Mat
     sm = 0.0
     for (c<-0 to Ncols-1)
       sm += this(r, c)*that(c)
     nv(r,0) = sm
    }
   
   nv.asInstanceOf[specificMatrix]
}

    // unary Minus applied to a Mat implies negation of all of its elements
def unary_- : specificMatrix =  {
   var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)  // get a Mat of the same dimension
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(i, j) = -this(i, j)  // negate element
      j += 1
    }
    i += 1
   }
   nv.asInstanceOf[specificMatrix]
}

// transpose the Matrix
def trans: specificMatrix = {
  var nv  = scalaSci.MatrixFactory(this, this.Ncols, this.Nrows) // get a Matrix of dimension MXN
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(j, i) = this(i, j)  // negate element
      j += 1
    }
    i += 1
   }

   nv.asInstanceOf[specificMatrix]
}


// transpose the Matrix
def T: specificMatrix = {
  var nv  = scalaSci.MatrixFactory(this, this.Ncols, this.Nrows) // get a Matrix of dimension MXN
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(j, i) = this(i, j)  // negate element
      j += 1
    }
    i += 1
   }

   nv.asInstanceOf[specificMatrix]
}


// transpose the Matrix
def ~ :specificMatrix = {
  var nv  = scalaSci.MatrixFactory(this, this.Ncols, this.Nrows) // get a Matrix of dimension MXN
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(j, i) = this(i, j)  // negate element
      j += 1
    }
    i += 1
   }

   nv.asInstanceOf[specificMatrix]
}

    // multiply Mat*Double
def * (that: Double): specificMatrix =  {
   var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
   var i=0; var j=0;
   while (i<Nrows) {
       j=0
    while (j<Ncols)  {
      nv(i, j) = this(i, j)*that
      j+=1
    }
    i+=1
   }
   nv.asInstanceOf[specificMatrix]
}

    // divide Mat/Double
def  / (that: Double): specificMatrix =  {
   var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
   var i=0; var j=0;
   while (i<Nrows) {
       j=0
    while (j<Ncols)  {
      nv(i, j) = this(i, j)/that
      j+=1
    }
    i+=1
   }
   nv.asInstanceOf[specificMatrix]
}

  /*
   var  m = ones0(2,4)
   var  v =  vones(2)
   var  mv2 = m+v
   var v4 = vones(4)
   var mv4 = m+v4
    
   */
// Mat + Vec
 def +(that: Vec): specificMatrix = {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length

    var rm  = scalaSci.MatrixFactory(this, rN, rM)
    if (rN == sN) {  // add column-wise
      for (r<-0 until rN)  // rows
        for (c<-0 until rM)  // columns
          rm(r, c) = this(r, c)+that(r)
      rm.asInstanceOf[specificMatrix]
 }
 else if (rM == sN) { // add row-wise
        for (r<-0 until rN)
          for (c<-0 until sN)
            rm(r, c) = this(r, c)+that(c)
      rm.asInstanceOf[specificMatrix]
  }
  else
    rm.asInstanceOf[specificMatrix]
 }
 
  /*
   var m = ones0(3, 5)
   var v3 = vones(3)
   var v5 = vones(5)
   var mv3 = m * v3
   var mv5 = m * v5
   */
def *(that: Vec): Vec= {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
        for (c<-0 until rM) {
           sm = 0.0
         for (r<-0 until rN) 
             sm += (this(r, c)*that(r))
          rv(c) =  sm
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
       for (r<-0 until rN) {
           sm = 0.0 
          for (c<-0 until sN)
            sm += (this(r, c)*that(c))
          rv(r) = sm
       }
         rv
 }
 else
   new Vec(1)   
  }
  
 
 
def -(that: Vec): specificMatrix = {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length
 
    var rm = scalaSci.MatrixFactory(this, rN,  rM)
    if (rN == sN) {  // subtract column-wise
      for (r<-0 until rN)
        for (c<-0 until rM)
          rm(r, c) = this(r, c)-that(r)
      rm.asInstanceOf[specificMatrix]
 }
 else if (rM == sN) { // subtract row-wise
        for (r<-0 until rN)
          for (c<-0 until sN)
            rm(r, c) = this(r, c)-that(c)
      rm.asInstanceOf[specificMatrix]
  }
  else
    rm.asInstanceOf[specificMatrix]
 }
 
  

// Mat + Mat
def  + (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions

  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
       var i=0; var j=0;
   while (i<sN)  {
        j=0
       while (j<sM)  {
           nv(i, j) = this(i, j) + that(i, j)
           j+=1
     }
     i += 1
   }
   nv.asInstanceOf[specificMatrix]
  }
  else { // incompatible dimensions
   that.asInstanceOf[specificMatrix]
     }
}


// common matrix transformations
def abs(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.abs(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }



def sin(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sin(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }

  

def cos(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.cos(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }

  
def tan(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.tan(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }

 def asin(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.asin(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
  
  
 def acos(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.acos(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
   
  
 def atan(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.atan(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
  
 def sinh(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sinh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
  
  
 def cosh(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.cosh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
  
 def tanh(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.tanh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
    
  
 def pow(v: Double): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.pow(this(i, j), v)
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
 
 def log(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
 
  
   // time non-trait: 0.266, time trait: 0.281
def log2(): specificMatrix= {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
       var i=0; var j=0;
       val conv = java.lang.Math.log(2.0)
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }

  
  def log10(): specificMatrix= {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
       var i=0; var j=0;
       val conv = java.lang.Math.log(10.0)
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }

  
 def ceil(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.ceil(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
    
  
 def round(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.round(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
  
  
  
 def floor(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.floor(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
  
  
  
  
  
 def sqrt(): specificMatrix  = {
    var Nrows = this.Nrows; var Ncols = this.Ncols;
    var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var i=0; var j=0;
    while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sqrt(this(i, j))
               j += 1
            }
            i += 1
       }
          nm.asInstanceOf[specificMatrix]
    }
  


 def toDegrees(): specificMatrix = {
   var Nrows = this.Nrows; var Ncols = this.Ncols;
   var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
           j=0
           while (j < Ncols)  {
             nm(i, j) = java.lang.Math.toDegrees(this(i, j))
             j += 1
            }
            i += 1
        }
          nm.asInstanceOf[specificMatrix]
    }




 def toRadians(): specificMatrix = {
   var Nrows = this.Nrows; var Ncols = this.Ncols;
   var nm = scalaSci.MatrixFactory(this, Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
           j=0
           while (j < Ncols)  {
             nm(i, j) = java.lang.Math.toRadians(this(i, j))
             j += 1
            }
            i += 1
        }
          nm.asInstanceOf[specificMatrix]
    }
  
  
  // IN-PLACE OPERATIONS
def absi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.abs(this(i, j))
               j += 1
            }
            i += 1
       }
       this   
    }



def sini()  = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sin(this(i, j))
               j += 1
            }
            i += 1
       }
       this   
    }



def cosi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.cos(this(i, j))
               j += 1
            }
            i += 1
       }
       this   
    }

def tani() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.tan(this(i, j))
               j += 1
            }
            i += 1
       }
       this 
      }

def asini() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.asin(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  
def acosi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.acos(this(i, j))
               j += 1
            }
            i += 1
       }
        this  
    }
    
    
def atani() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.atan(this(i, j))
               j += 1
            }
            i += 1
       }
      this    
    }


def sinhi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sinh(this(i, j))
               j += 1
            }
            i += 1
       }
    this
    }

  

def coshi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.cosh(this(i, j))
               j += 1
            }
            i += 1
       }
       this
     }

  
def tanhi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.tanh(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }
  
  
def powi(v: Double) = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.pow(this(i, j), v)
               j += 1
            }
            i += 1
       }
      this   
    }
    
  
def logi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j))
               j += 1
            }
            i += 1
       }
       this
      }

  
def log2i() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       val conv = java.lang.Math.log(2.0)
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
        this  
    }
    
 
def log10i() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       val conv = java.lang.Math.log(10.0)
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          this
    }
    
  def ceili() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.ceil(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  def floori() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.floor(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

    
  def roundi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.round(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  def sqrti() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=0; var j=0;
       while  (i< Nrows) {
            j=0
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sqrt(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  
  

  // allows to display the contents of the matrix with a JTable and to interactively manipulate them
  def browse() =  scalaExec.gui.watchMatrix.display(this.toDoubleArray(), false)
  // pass also the varName in order be displayed at the browser's window title
  def browse(varName: String) =  scalaExec.gui.watchMatrix.display(this.toDoubleArray(), false, varName)


  /*
  def eigvals(): RichDoubleArray  // compute the eigenvalues 
  def eigvecs(): RichDoubleDoubleArray  // compute the eigenvectors
  def eig(): (RichDoubleArray, RichDoubleDoubleArray)  // compute both eigenvalues/eigenvectors
  */

  // columnwise sum
def sum(): RichDoubleArray = {
    var N = this.Nrows;     var M = this.Ncols
    var sm = 0.0
    var res = new Array[Double](M)
    for (ccol <-0  to M-1) {
     sm=0.0
     for (crow<- 0 to N-1)
       sm += this(crow, ccol)
     res(ccol) = sm 
     }
    new RichDoubleArray(res)
}

  
// columnwise mean
def mean(): RichDoubleArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    for (ccol <-0  to Ncols-1) {
     sm=0.0
     for (crow <- 0 to Nrows-1)
       sm += this(crow, ccol)
     res(ccol) = sm/Nrows
     }
 new RichDoubleArray(res)
}

// columnwise product
def prod(): RichDoubleArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Ncols)
    for (ccol <- 0  to Ncols-1) {
     pd=1.0
     for (crow <- 0 to Nrows-1)
       pd *= this(crow, ccol)
     res(ccol) = pd
     }
    new RichDoubleArray(res)
}

  // columnwise min
 def min(): RichDoubleArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Ncols)
     var ccol = 0
     while  (ccol <= Ncols-1) {
         var mn = this(0, ccol)  // keeps the running min element
         var crow = 0
         while  (crow <= Nrows-1)  {
              var tmp = this(crow, ccol)
               if (tmp  < mn)  mn = tmp
               crow += 1
           }
           res(ccol) = mn   // min element for the ccol column
           ccol += 1
     }
     new RichDoubleArray(res)
 }

  // columnwise max
 def max(): RichDoubleArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Ncols)
     for (ccol <- 0 to Ncols-1) {
         var mx = this(0, ccol)  // keeps the running max element
         for (crow <- 1 to Nrows-1)  {
              var tmp = this(crow, ccol)
               if (tmp  > mx)  mx = tmp
           }
        res(ccol) = mx   // max element for the ccol column
     }
     new RichDoubleArray(res)
 }

  
  
// ROWWISE OPERATIONS
// rowwise sum
def sumR(): RichDoubleArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // sum for all rows
    for (crow<- 0 to Nrows-1)  {
        sm=0.0
        for (ccol <-0  to Ncols-1)   // sum across column
         sm += this(crow, ccol)
     res(crow) = sm
     }
 new RichDoubleArray(res)
}

  
  // rowwise mean
def meanR(): RichDoubleArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // mean for all rows
    for (crow <-0  to Nrows-1) {
     sm=0.0
     for (ccol<- 0 to Ncols-1)  // sum across column
       sm += this(crow, ccol)
     res(crow) = sm/Ncols
     }
 new RichDoubleArray(res)
}

  
// rowwise product
def prodR(): RichDoubleArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Nrows)
    for (crow <-0  to Nrows-1) {
     pd=1.0
     for (ccol<- 0 to Ncols-1)  // product across column
       pd *= this(crow, ccol)

     res(crow) = pd
     }
    new RichDoubleArray(res)
}
  
  
 // rowwise min
 def minR(): RichDoubleArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Nrows)
     for (crow <- 0 to Nrows-1) {
         var mn = this(crow, 0)  // keeps the running min element
         for (ccol <- 1 to Ncols-1)  {
              var tmp = this(crow, ccol)
               if (tmp  < mn)  mn = tmp
           }
           res(crow) = mn   // min element for the crow row
     }
      new RichDoubleArray(res)
 }

  
  // rowwise max
 def maxR(): RichDoubleArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Nrows)
     for (crow <- 0 to Nrows-1) {
         var mx = this(crow, 0)  // keeps the running max element
         for (ccol <- 1 to Ncols-1)  {
              var tmp = this(crow, ccol)
               if (tmp  > mx)  mx = tmp
           }
        res(crow) = mx   // max element for the ccol column
     }
     new RichDoubleArray(res)
 }

  
def resample( n: Int,  m: Int): specificMatrix  = {
    var rows = this.Nrows
    var cols = this.Ncols
    var  rRows =  (rows/n).asInstanceOf[Int]
    var  rCols =  (cols/m).asInstanceOf[Int]
    var newMat = scalaSci.MatrixFactory(this, rRows, rCols)
    var r=0; var c=0
    while (r<rRows) {
        c = 0
        while  (c<rCols) {
            newMat(r, c) = this(r*n, m*c)
            c += 1
          }
      r += 1
}
   newMat.asInstanceOf[specificMatrix]
}


//  Reshapes a  matrix a to new dimension n X m
def  reshape(n: Int, m: Int): specificMatrix = {
   var  aCols  = this.Ncols   // columns of matrix
   var  aRows = this.Nrows   // rows of matrix
   var Nrows = n; var Ncols = m;
  // m,n must be positive
 if (Ncols<=0 || Nrows<=0)  {  Nrows = 1; Ncols = aRows*aCols;  }  // default to reshaping in a large row
 if (Nrows*Ncols != aRows*aCols)  {
     return   this.asInstanceOf[specificMatrix]  // invalid new size: return the original matrix
    }
 var nm = scalaSci.MatrixFactory(this, Nrows, Ncols) // create the new matrix
// keep two set of indices, i.e. iorig, jorig: indices at the original matrix and inew, jnew: indices at the new matrix
var iorig=0; var jorig=0; var inew=0; var jnew=0
while (iorig < aRows)  {
    while (jorig < aCols)  {
        nm(inew, jnew) = this(iorig, jorig)
        jorig += 1
        jnew += 1
        if (jnew>=Ncols) {  // next row of the reshaped new matrix
            jnew = 0
            inew += 1
           }
      } // jorig < aCols
    iorig += 1
    jorig = 0
  } // iorig < aRows
nm.asInstanceOf[specificMatrix]  // return the new Matrix
}

  
}







```


`Also, the trait ` _`StaticScalaSciCommonOps`_ `defines operations that adapt to the currently utilized zero-indexed matrix type. Its current snapshot of code is: `

```



package scalaSci

//  Defines and implements most common static operations that mandatory 
//  implement all the scalaSci Matrix types
//  Defines a common functionality that is assured by all the library-specific scalaSci wrapper classes

// for each matrix library we can exploit the data representation (e.g. 2-D Double array, 1-D Double array, banded representation, 
// sparse representatioj) in order to implement these operations effectively
trait StaticScalaSciCommonOps[specificMatrix]   {
  
  def size(A: scalaSciMatrix[specificMatrix]): (Int, Int) = A.size // tuple with the number of roew and columns
  def length(A: scalaSciMatrix[specificMatrix]): Int  = A.length // total length
   
  def abs(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.abs
  
  // trigonometric routines
  def sin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sin 
  def cos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.cos
  def tan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.tan
  def asin(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.asin
  def acos(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.acos
  def atan(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.atan
  def sinh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sinh
  def cosh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.cosh
  def tanh(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.tanh

  // other mathematical routines
  def pow(A: scalaSciMatrix[specificMatrix], value: Double): specificMatrix = A.pow(value)
  def log(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.log
  def log2(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.log2
  def ceil(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.ceil
  def floor(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.floor
  def round(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.round
  def sqrt(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.sqrt
  def toDegrees(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.toDegrees
  def toRadians(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.toRadians
  
  
  
  //def dot(A: scalaSciMatrix[specificMatrix], b: scalaSciMatrix[specificMatrix]): Double = A.dot(b)
  //def cross(A: scalaSciMatrix[specificMatrix], b: scalaSciMatrix[specificMatrix]): specificMatrix = A.cross(b)
  
  
  // statistical routines
      // across columns
  def sum(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.sum
  def mean(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.mean
  def prod(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.prod
  def min(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.min
  def max(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.max
    // across rows
  def sumR(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.sumR
  def meanR(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.meanR
  def prodR(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.prodR
  def minR(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.minR
  def maxR(A: scalaSciMatrix[specificMatrix]): RichDoubleArray = A.maxR 
  
  def resample(A: scalaSciMatrix[specificMatrix], n: Int, m: Int): specificMatrix = A.resample(n, m)
  def reshape(A: scalaSciMatrix[specificMatrix], n: Int, m: Int): specificMatrix = A.reshape(n, m)
  //def correlation(A: specificMatrix, B: specificMatrix): specificMatrix
 // def corr(A: specificMatrix, B: specificMatrix): specificMatrix
 // def covariance(A: specificMatrix, B: specificMatrix): specificMatrix
 // def cov(A: specificMatrix, B: specificMatrix): specificMatrix
  
  def LU_solve(A: specificMatrix, b: specificMatrix): specificMatrix
  
  
  def inv(A: specificMatrix): specificMatrix    // matrix inverse
  
  def T(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.T
  def trans(A: scalaSciMatrix[specificMatrix]): specificMatrix = A.trans
  
  def rand0(n: Int, m: Int) = {
    var  rndMat =  scalaSci.MatrixFactory( this, n, m).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    for (r<-0 until n)
      for (c<-0 until m)
        rndMat(r, c) = 2.0*com.nr.test.NRTestUtil.ran.doub() - 1.0
    
    rndMat.asInstanceOf[specificMatrix]
  }

  def rand0(n: Int): specificMatrix = rand0(n, n)
  
  def ones0(numRows: Int, numCols: Int) = {
   var ret = scalaSci.MatrixFactory(this, numRows, numCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r, c=0
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

def ones0(n: Int): specificMatrix  = ones0(n, n)

def zeros0(numRows: Int, numCols: Int) = {
   var ret = scalaSci.MatrixFactory(this, numRows, numCols).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var r, c=0
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


def zeros0(n: Int): specificMatrix = zeros0(n, n)
  
def fill0(numRows: Int, numCols: Int, value: Double) = {
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

def fill0(n: Int, value: Double): specificMatrix = fill0(n, n, value)

def diag0(n: Int, m: Int) = {
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
 
def diag0(n: Int): specificMatrix =  diag0(n, n)
 
def  diag0(x: scalaSci.Vec) = {
    var n = x.length
    var om = scalaSci.MatrixFactory(this, n, n).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var i=0;
    while  (i< n) {
      om(i, i) = x(i)
       i += 1
      }
          om.asInstanceOf[specificMatrix]
  }
  
def diag0( x: Array[Double]) = {
    var n = x.length
    var om = scalaSci.MatrixFactory(this, n, n).asInstanceOf[scalaSciMatrix[specificMatrix]]  // create a Matrix
    var i=0;
    while  (i< n) {
      om(i, i) = x(n)
       i += 1
      }
          om.asInstanceOf[specificMatrix]
    }
    


  
def  eye0(n:Int): specificMatrix = {
    eye0(n, n)
}

def  eye0(n:Int, m:Int)  = {
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

def M0(s: String) =  {
    var nRows = 1
    var nCols = 0
    for (i<-0 to s.length-1)   // count how many rows are specified
      if  (s(i)==';')
        nRows += 1

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
    for (k <- 0 until  nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersMatrix(k, c) = tok.toDouble
    c += 1
     }   // read row
   }  // read array
   numbersMatrix.asInstanceOf[specificMatrix]
 }  

  
  def toDoubleArray(A: scalaSciMatrix[specificMatrix]) =   A.toDoubleArray // returns a Java/Scala 2-D array representation of the matrix contents  

  def find(A: specificMatrix): Array[Array[Int]]
  
  
  /*
  def eigvals(a: specificMatrix ): RichDoubleArray   // compute the eigenvalues 
  
  def eigvecs(a: specificMatrix ): RichDoubleDoubleArray  // compute the eigenvectors
  def eig(a: specificMatrix ): (RichDoubleArray, RichDoubleDoubleArray)  // compute both eigenvalues/eigenvectors
*/
}

```