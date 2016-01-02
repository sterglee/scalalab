# Introduction #

`We describe by means of examples the functionality of ScalaLab independently of the particular library. This functionality is attained with the ` **`StaticMaths`** `object, and with the machinery of implicit conversions to ` **`RichDoubleArray`** `and ` **`RichDoubleDoubleArray`** `types. Also, the 1-indexed Matrix class, provides a lot of functionality based on the powerful NUMAL library.`


# `Global functions provided with the StaticMaths object` #

`The functionality of the StaticMaths object is available independently of the utilized library and consists of many functions that are listed below: `

```


def M1(s: String) = {
  scalaSci.Matrix.M1(s)
}

def MDD(s: String) =   scalaSci.DoubleDoubleArr.MDD(s)

def V(s: String) = {
 scalaSci.Vec.V(s)
}

     
def abs(x: Double) = {
    java.lang.Math.abs(x)
}

def abs(x: Float) = {
    java.lang.Math.abs(x).asInstanceOf[Float]
}

def abs(x: Int) = {
    java.lang.Math.abs(x)
}

def abs(x: Short) = {
    java.lang.Math.abs(x)
}

def abs(x: Long) = {
    java.lang.Math.abs(x)
}

def abs(x: Array[Double]) = {
    scalaSci.DoubleArr.abs(x)
}

def abs(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.abs(x)
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

def sin(x: Float) = {
    java.lang.Math.sin(x).asInstanceOf[Float]
}

def sin(x: Int) = {
    java.lang.Math.sin(x)
}

def sin(x: Short) = {
    java.lang.Math.sin(x)
}

def sin(x: Long) = {
    java.lang.Math.sin(x)
}

def sin(x: Array[Double]) = {
    scalaSci.DoubleArr.sin(x)
}

def sin(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.sin(x)
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


def cos(x: Float) = {
    java.lang.Math.cos(x).asInstanceOf[Float]
}

def cos(x: Int) = {
    java.lang.Math.cos(x)
}

def cos(x: Short) = {
    java.lang.Math.cos(x)
}

def cos(x: Long) = {
    java.lang.Math.cos(x)
}

def cos(x: Array[Double]) = {
    scalaSci.DoubleArr.cos(x)
}


def cos(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.cos(x)
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


def tan(x: Float) = {
    java.lang.Math.tan(x).asInstanceOf[Float]
}

def tan(x: Int) = {
    java.lang.Math.tan(x)
}

def tan(x: Short) = {
    java.lang.Math.tan(x)
}

def tan(x: Long) = {
    java.lang.Math.tan(x)
}

def tan(x: Array[Double]) = {
    scalaSci.DoubleArr.tan(x)
}


def tan(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.tan(x)
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

def cosh(x: Float) = {
    java.lang.Math.cosh(x).asInstanceOf[Float]
}

def cosh(x: Int) = {
    java.lang.Math.cosh(x)
}

def cosh(x: Short) = {
    java.lang.Math.cosh(x)
}

def cosh(x: Long) = {
    java.lang.Math.cosh(x)
}

def cosh(x: Array[Double]) = {
    scalaSci.DoubleArr.cosh(x)
}


def cosh(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.cosh(x)
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

def sinh(x: Float) = {
    java.lang.Math.sinh(x).asInstanceOf[Float]
}

def sinh(x: Int) = {
    java.lang.Math.sinh(x)
}

def sinh(x: Short) = {
    java.lang.Math.sinh(x)
}

def sinh(x: Long) = {
    java.lang.Math.sinh(x)
}

def sinh(x: Array[Double]) = {
    scalaSci.DoubleArr.sinh(x)
}

def sinh(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.sinh(x)
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

def tanh(x: Float) = {
    java.lang.Math.tanh(x).asInstanceOf[Float]
}

def tanh(x: Int) = {
    java.lang.Math.tanh(x)
}

def tanh(x: Short) = {
    java.lang.Math.tanh(x)
}

def tanh(x: Long) = {
    java.lang.Math.tanh(x)
}

def tanh(x: Array[Double]) = {
    scalaSci.DoubleArr.tanh(x)
}


def tanh(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.tanh(x)
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

def asin(x: Float) = {
    java.lang.Math.asin(x).asInstanceOf[Float]
}

def asin(x: Int) = {
    java.lang.Math.asin(x)
}

def asin(x: Short) = {
    java.lang.Math.asin(x)
}

def asin(x: Long) = {
    java.lang.Math.asin(x)
}

def asin(x: Array[Double]) = {
    scalaSci.DoubleArr.asin(x)
}

def asin(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.asin(x)
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

def acos(x: Float) = {
    java.lang.Math.acos(x).asInstanceOf[Float]
}

def acos(x: Int) = {
    java.lang.Math.acos(x)
}

def acos(x: Short) = {
    java.lang.Math.acos(x)
}

def acos(x: Long) = {
    java.lang.Math.acos(x)
}

def acos(x: Array[Double]) = {
    scalaSci.DoubleArr.acos(x)
}


def acos(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.acos(x)
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

def atan(x: Float) = {
    java.lang.Math.atan(x).asInstanceOf[Float]
}

def atan(x: Int) = {
    java.lang.Math.atan(x)
}

def atan(x: Short) = {
    java.lang.Math.atan(x)
}

def atan(x: Long) = {
    java.lang.Math.atan(x)
}

def atan(x: Array[Double]) = {
    scalaSci.DoubleArr.atan(x)
}


def atan(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.atan(x)
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

def exp(x: Float) = {
    java.lang.Math.exp(x).asInstanceOf[Float]
}

def exp(x: Int) = {
    java.lang.Math.exp(x)
}

def exp(x: Short) = {
    java.lang.Math.exp(x)
}

def exp(x: Long) = {
    java.lang.Math.exp(x)
}

def exp(x: Array[Double]) = {
    scalaSci.DoubleArr.exp(x)
}


def exp(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.exp(x)
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


def pow(x: Int, value: Double) = {
    java.lang.Math.pow(x, value)
}

def pow(x: Short, value: Double) = {
    java.lang.Math.pow(x, value)
}

def pow(x: Long, value: Double) = {
    java.lang.Math.pow(x, value)
}

def pow(x: Array[Double], value: Double) = {
    scalaSci.DoubleArr.pow(x, value)
}


def pow(x: Matrix, value: Double) = {
    scalaSci.Matrix.pow(x, value)
}


def pow(x: Array[Array[Double]], value:Double) = {
    scalaSci.DoubleDoubleArr.pow(x, value)
}

  
def pow(x: Vec, value: Double) = {
    scalaSci.Vec.pow(x, value)
}
  
// log


def log(x: Double) = {
    java.lang.Math.log(x)
}

def log(x: Float) = {
    java.lang.Math.log(x).asInstanceOf[Float]
}

def log(x: Int) = {
    java.lang.Math.log(x)
}

def log(x: Short) = {
    java.lang.Math.log(x)
}

def log(x: Long) = {
    java.lang.Math.log(x)
}

def log(x: Array[Double]) = {
    scalaSci.DoubleArr.log(x)
}


def log(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.log(x)
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

def log2(x: Float) = {
    var conv = java.lang.Math.log(2.0f).asInstanceOf[Float]
    java.lang.Math.log(x).asInstanceOf[Float]/conv
}

def log2(x: Int) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}

def log2(x: Short) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}

def log2(x: Long) = {
    var conv = java.lang.Math.log(2.0)
    java.lang.Math.log(x)/conv
}

def log2(x: Array[Double]) = {
    scalaSci.DoubleArr.log2(x)
}

def log2(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.log2(x)
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

def log10(x: Float) = {
    java.lang.Math.log10(x).asInstanceOf[Float]
}

def log10(x: Int) = {
    java.lang.Math.log10(x)
}

def log10(x: Short) = {
    java.lang.Math.log10(x)
}

def log10(x: Long) = {
    java.lang.Math.log10(x)
}

def log10(x: Array[Double]) = {
    scalaSci.DoubleArr.log10(x)
}


def log10(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.log10(x)
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

def ceil(x: Float) = {
    java.lang.Math.ceil(x).asInstanceOf[Float]
}

def ceil(x: Int) = {
    java.lang.Math.ceil(x)
}

def ceil(x: Short) = {
    java.lang.Math.ceil(x)
}

def ceil(x: Long) = {
    java.lang.Math.ceil(x)
}

def ceil(x: Array[Double]) = {
    scalaSci.DoubleArr.ceil(x)
}


def ceil(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.ceil(x)
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

def floor(x: Float) = {
    java.lang.Math.floor(x).asInstanceOf[Float]
}

def floor(x: Int) = {
    java.lang.Math.floor(x)
}

def floor(x: Short) = {
    java.lang.Math.floor(x)
}

def floor(x: Long) = {
    java.lang.Math.floor(x)
}

def floor(x: Array[Double]) = {
    scalaSci.DoubleArr.floor(x)
}


def floor(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.floor(x)
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

def round(x: Float) = {
    java.lang.Math.round(x).asInstanceOf[Float]
}

def round(x: Int) = {
    java.lang.Math.round(x)
}

def round(x: Short) = {
    java.lang.Math.round(x)
}

def round(x: Long) = {
    java.lang.Math.round(x)
}

def round(x: Array[Double]) = {
    scalaSci.DoubleArr.round(x)
}


def round(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.round(x)
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

def sqrt(x: Float) = {
    java.lang.Math.sqrt(x).asInstanceOf[Float]
}

def sqrt(x: Int) = {
    java.lang.Math.sqrt(x)
}

def sqrt(x: Short) = {
    java.lang.Math.sqrt(x)
}

def sqrt(x: Long) = {
    java.lang.Math.sqrt(x)
}

def sqrt(x: Array[Double]) = {
    scalaSci.DoubleArr.sqrt(x)
}


def sqrt(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.sqrt(x)
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

def toDegrees(x: Float) = {
    java.lang.Math.toDegrees(x).asInstanceOf[Float]
}

def toDegrees(x: Int) = {
    java.lang.Math.toDegrees(x)
}

def toDegrees(x: Short) = {
    java.lang.Math.toDegrees(x)
}

def toDegrees(x: Long) = {
    java.lang.Math.toDegrees(x)
}

def toDegrees(x: Array[Double]) = {
    scalaSci.DoubleArr.toDegrees(x)
}


def toDegrees(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.toDegrees(x)
}

def toDegrees(x: Matrix) = {
    scalaSci.Matrix.toDegrees(x)
}


def toDegrees(x: Vec) = {
    scalaSci.Vec.toDegrees(x)
}

 // toRadians

def toRadians(x: Double) = {
    java.lang.Math.toRadians(x)
}

def toRadians(x: Float) = {
    java.lang.Math.toRadians(x).asInstanceOf[Float]
}

def toRadians(x: Int) = {
    java.lang.Math.toRadians(x)
}

def toRadians(x: Short) = {
    java.lang.Math.toRadians(x)
}

def toRadians(x: Long) = {
    java.lang.Math.toRadians(x)
}

def toRadians(x: Array[Double]) = {
    scalaSci.DoubleArr.toRadians(x)
}


def toRadians(x: Array[Array[Double]]) = {
    scalaSci.DoubleDoubleArr.toRadians(x)
}

def toRadians(x: Matrix) = {
    scalaSci.Matrix.toRadians(x)
}


def toRadians(x: Vec) = {
    scalaSci.Vec.toRadians(x)
}


    
 def  diag(n:Int): Matrix = {
       scalaSci.Matrix.diag(n)
 }

 
def eye(n: Int): Matrix = {
  scalaSci.Matrix.eye(n)
}

def Eye(n: Int): Array[Array[Double]] = {
  scalaSci.DoubleDoubleArr.eye(n)
}


def eye(n: Int, m: Int): Matrix = {
  scalaSci.Matrix.eye(n, m)
}

def Eye(n: Int, m: Int): Array[Array[Double]] = {
  scalaSci.DoubleDoubleArr.eye(n, m)
}


def ones(n: Int, m:Int): Matrix = {
     scalaSci.Matrix.ones(n, m)
}

def ones(n: Int): Matrix = {
 scalaSci.Matrix.ones(n, n)
}

 
 def zeros(n: Int, m:Int): Matrix = {
      scalaSci.Matrix.zeros(n,m)
  }

def zeros(n: Int): Matrix = {
 scalaSci.Matrix.zeros(n, n)
}


def zeros0(n: Int): Matrix = {
 scalaSci.Matrix.zeros(n, n)
}


  def fill(n: Int, m:Int, vl: Double): Matrix = {
    scalaSci.Matrix.fill(n, m, vl)
  }

def fill(n: Int, vl:Double): Matrix = {
 scalaSci.Matrix.fill(n, n, vl)
}


    def Fill(n: Int, m:Int, vl: Double): Array[Array[Double]] = {
        scalaSci.DoubleDoubleArr.Fill(n, m, vl)
    }

    def  Ones(n:Int, m:Int): Array[Array[Double]] = {
        scalaSci.DoubleDoubleArr.ones(n, m)
    }

    def  Zeros(n:Int, m:Int): Array[Array[Double]] = {
        scalaSci.DoubleDoubleArr.zeros(n, m)
    }

    // create random Matrix
    def rand(n: Int, m:Int): Matrix = {
       scalaSci.Matrix.rand(n,m)
    }

def rand(n: Int): Matrix = {
       scalaSci.Matrix.rand(n,n)
    }

  def randt(n: Int, m:Int): Matrix = {
       scalaSci.Matrix.randt(n,m)
    }

    def Rand(n: Int, m:Int): Array[Array[Double]] = {
        scalaSci.DoubleDoubleArr.rand(n, m)
    }

   def Rand(n: Int): Array[Array[Double]] = {
        scalaSci.DoubleDoubleArr.rand(n, n)
    }

    def vrand(n:Int):scalaSci.Vec = {
        scalaSci.Vec.rand(n)
    }


    def  Ones(n:Int): Array[Double] = {
        scalaSci.DoubleArr.Ones(n)
    }


    def vones(n: Int): Vec = {
        scalaSci.Vec.vones(n)
    }

    def  Zeros(n: Int): Array[Double] = {
        scalaSci.DoubleArr.Zeros(n)
  }

 def  vzeros(n: Int): Vec = {
     scalaSci.Vec.vzeros(n)
    }

 def vfill(n: Int, value: Double): Vec = {
      scalaSci.Vec.vfill(n, value)
  }

def vfill(n: Int, value: Int): Vec = {
      scalaSci.Vec.vfill(n, value)
  }

def  vFill(n:Int, value: Double): Array[Double] = {
      scalaSci.DoubleArr.Fill(n, value)
      }


 def inc(begin: Double, pitch: Double,  end: Double): Vec = {
     scalaSci.Vec.inc(begin, pitch, end)
    }

 def  Inc(begin: Double, pitch: Double,  end: Double): Array[Double] = {
    scalaSci.DoubleArr.Inc(begin, pitch, end)
    }

def linspace(startv: Double, endv: Double) = scalaSci.Vec.linspace(startv, endv)

def linspace(startv: Double, endv: Double, nP: Int) = scalaSci.Vec.linspace(startv, endv, nP)

def logspace(startv: Double, endv: Double, nP: Int, logBase: Double) = scalaSci.Vec.logspace(startv, endv, nP, logBase)

def logspace(startv: Double, endv: Double, nP: Int) = scalaSci.Vec.logspace(startv, endv, nP, 10.0)  // Matlab-like defaults

def logspace(startv: Double, endv: Double) = scalaSci.Vec.logspace(startv, endv, 50, 10.0)  // Matlab-like defaults

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
       scalaSci.math.array.BasicDSP.fft(sig)
   }

 def fft(sig: Vec) = {
     scalaSci.math.array.BasicDSP.fft(sig)
 }

 
  
  


def mean(v: Vec): Double = {
    mean(v.getv)
}

def sum(v: Vec):Double = {
    sum(v.getv)
}    

def size(v: Vec): Int = {   v.length }

def length(v: Vec): Int = { v.length }

def size(a: scalaSci.Matrix): Array[Int] =  {
  var siz = new Array[Int](2)
  siz(0) = a.Nrows;  siz(1) = a.Ncols
 siz
}


def  length(a: scalaSci.Matrix): Int = { a.length }

   
// aggregation routines
// COLUMNWISE 
def sum( v: Matrix): Array[Double] = {
   scalaSci.Matrix.sum(v)
}

def sum( v: Array[Double]): Double = {
   scalaSci.DoubleArr.sum(v)
}

def  sum(v: Array[Array[Double]]): Array[Double] = {
    scalaSci.DoubleDoubleArr.sum(v)
}


def mean( v: Matrix): Array[Double] = {
   scalaSci.Matrix.mean(v)
}


def mean( v: Array[Double]): Double = {
   scalaSci.DoubleArr.mean(v)
}

def  mean(v: Array[Array[Double]]): Array[Double] = {
    scalaSci.DoubleDoubleArr.mean(v)
}

def prod( v: Matrix): Array[Double] = {
   scalaSci.Matrix.prod(v)
}


def prod( v: Array[Double]): Double = {
   scalaSci.DoubleArr.prod(v)
}

  
def prod( v: Array[Int]): Int = {
   scalaSci.DoubleArr.prod(v)
}

def prod( v: Array[Float]): Float = {
   scalaSci.DoubleArr.prod(v)
}

def  prod(v: Array[Array[Double]]): Array[Double] = {
    scalaSci.DoubleDoubleArr.prod(v)
}


def min(darr: Array[Double]): Double = {
   scalaSci.DoubleArr.min(darr)
}


def max(darr: Array[Double]): Double = {
   scalaSci.DoubleArr.max(darr)
}

def min(darr: Array[Int]): Int= {
   scalaSci.DoubleArr.min(darr)
}


def max(darr: Array[Float]): Float = {
   scalaSci.DoubleArr.max(darr)
}

  
def max(darr: Array[Int]): Int = {
   scalaSci.DoubleArr.max(darr)
}

  def min(darr: Array[Float]): Float= {
   scalaSci.DoubleArr.min(darr)
}



def min(darr: Array[Array[Double]]): Array[Double] = {
   scalaSci.DoubleDoubleArr.min(darr)
}

def max(darr: Array[Array[Double]]): Array[Double] = {
   scalaSci.DoubleDoubleArr.max(darr)
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
      var smAll = args(0)
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  def sum(args: Double*) =    {
      var smAll = args(0)
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  def sum(args: Float*) =    {
      var smAll = args(0)
      for (arg <- args)  
          smAll += arg
      smAll  
    }
  
  
  def prod(args: Int*) =    {
      var prAll = args(0)
      for (arg <- args)  
          prAll *= arg
      prAll  
    }
  
  def prod(args: Double*) =    {
      var prAll = args(0)
      for (arg <- args)  
          prAll  *= arg
      prAll  
    }
  
  def prod(args: Float*) =    {
      var prAll = args(0)
      for (arg <- args)  
          prAll *= arg
      prAll  
    }
  
  
  def max( v: Matrix): Array[Double] = {
   scalaSci.Matrix.max(v)
}


def max(v: Vec): Double = {
  scalaSci.Vec.max(v)
}

// ROWWISE 
def sumR( v: Matrix): Array[Double] = {
   scalaSci.Matrix.sumR(v)
}


def meanR( v: Matrix): Array[Double] = {
   scalaSci.Matrix.meanR(v)
}


def prodR( v: Matrix): Array[Double] = {
   scalaSci.Matrix.prodR(v)
}

def minR( v: Matrix): Array[Double] = {
   scalaSci.Matrix.minR(v)
}

def maxR( v: Matrix): Array[Double] = {
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



def reshape(v: Array[Array[Double]], n: Int, m: Int): Array[Array[Double]] = {
  scalaSci.DoubleDoubleArr.reshape(v, n, m)
}

def resample(v: Matrix, n: Int, m: Int): Matrix = {
  scalaSci.Matrix.resample(v, n, m)
}

def  corr(v1: Array[Double], v2: Array[Double]): Double =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
     r
 }


def correlation(v1: Array[Double], v2: Array[Double]): Double = {
  var r = corr(v1, v2)
  r
}

def  corr(v1: Array[Array[Double]], v2: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v1, v2);
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

def  cov(v1: Array[Array[Double]], v2: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v1, v2);
     r
 }

def  covariance(v1: Array[Array[Double]], v2: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = cov(v1, v2)
     r
 }

def  cov(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }

 def  covariance(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.covariance(v, v);
     r
 }

    
def correlation(v1: Array[Array[Double]], v2: Array[Array[Double]]): Array[Array[Double]] = {
  var r = corr(v1, v2)
  r
}


def covariance(v1: Matrix, v2: Matrix): Matrix = {
  scalaSci.Matrix.covariance(v1, v2)
}


def cov(v1: Matrix, v2: Matrix): Matrix = {
  scalaSci.Matrix.covariance(v1, v2)
}




def  corr(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r = scalaSci.math.array.StatisticSample.correlation(v, v);
     r
 }


def  correlation(v: Array[Array[Double]]): Array[Array[Double]] =   {
     var r =  corr(v,v);
     r
 }
 

def  det(M: Matrix) = {
  scalaSci.Matrix.det(M)
}

def det(A: Array[Array[Double]]) = {
  scalaSci.DoubleDoubleArr.det(A)
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

def absFFT(vin: Vec): Array[Double] = {
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
def getRealValues(carr: Array[JSci.maths.Complex]): Array[Double] = {
    var len = carr.length
    var doubleArr = new Array[Double](len)
    for (k<-0 to len-1)
      doubleArr(k) = carr(k).real()
    doubleArr
}

def fwt(v: Vec, j: Int): Array[Array[Double]] = {
  var sig = new Signal(v.getv)
  var wcoeffs = sig.fwt(j)
  wcoeffs.getCoefs
}
    // some linear algebra operations using the NUMAL library

def inv(m: Matrix): Matrix = {
     var mcopy = new Matrix(m)
     scalaSci.Matrix.decinv(mcopy)
  }


def inv(a: Array[Array[Double]]) = {
  scalaSci.DoubleDoubleArr.inv(a)
}

def svdRank(a: Matrix) = {
  scalaSci.Matrix.svdRank(a)
}

def svd(a: Matrix) = {
  scalaSci.Matrix.svd(a)
}


def svdRank(a: Array[Array[Double]]) = {
  scalaSci.DoubleDoubleArr.svdRank(a)
}

def svd(a: Array[Array[Double]]) = {
  scalaSci.DoubleDoubleArr.svd(a)
}


def eig(da: Array[Array[Double]]) = {
  scalaSci.DoubleDoubleArr.eig(da)
}

def eig(m: Matrix) = {
   scalaSci.Matrix.eig(m)
 }

 
def eig(ar:  Matrix, ai: Matrix)= {
   scalaSci.Matrix.eig(ar, ai)
}

def gsssol( a: Matrix, b: Array[Double]): Matrix = {
    scalaSci.Matrix.gsssol(a, b)
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



def LU(a: Matrix):(Matrix, Matrix, Matrix) = scalaSci.Matrix.LU(a)





def subm(a: Matrix, rs: Int, incr: Int, re: Int, cs: Int, incc: Int, ce: Int) = {
   a.subm(rs, incr, re, cs, incc, ce)
  }

def submr(a: Matrix, rs: Int, incr: Int, re: Int) = {
   a.submr(rs, incr, re)
  }

def submc(a: Matrix, cs: Int, incc: Int, ce: Int) = {
   a.submc(cs, incc, ce)
  }



def diag(a: Array[Double], oneIndexed: Boolean) =  {  scalaSci.Matrix.diag(a, oneIndexed)  }
def diag(a: Array[Double]) =  {  scalaSci.Matrix.diag(a)  }
def Diag(a: Array[Double]) = { scalaSci.DoubleDoubleArr.diag(a) }

def norm1(a: Array[Array[Double]]) =  { scalaSci.DoubleDoubleArr.norm1(a) }
def norm1(a: Matrix) =  { scalaSci.Matrix.norm1(a) }

def norm2(a: Array[Array[Double]]) =  { scalaSci.DoubleDoubleArr.norm2(a) }
def norm2(a: Matrix) =  { scalaSci.Matrix.norm2(a) }

def normF(a: Array[Array[Double]]) =  { scalaSci.DoubleDoubleArr.normF(a) }
def normF(a: Matrix) =  { scalaSci.Matrix.normF(a) }

def normInf(a: Array[Array[Double]]) =  { scalaSci.DoubleDoubleArr.normInf(a) }
def normInf(a: Matrix) =  { scalaSci.Matrix.normInf(a) }

// Matlab like norm function
def norm(a: Array[Array[Double]], normType: Int) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}


def norm(a:Matrix, normType: Int) = {
  if (normType==1)
      norm1(a)
  else if (normType==2)
      norm2(a)
  else if (normType== GlobalValues.Fro)   // Frobenious norm
      normF(a)
  else
      normInf(a)
}

  
```


# `Examples on the provided global array/matrix operations with the StaticMaths object` #


`Construct Scala arrays with the convenient short forms, AAD(), AD(), AI():`

```
var aa = new AAD(3, 4)  // equivalent to: var aa = new Array[Array[Double]](3,4)
var a = new AD(7)  // equivalent to: var a = new Array[Double](7)
var ai = new AI(5)  // equivalent to: var ai = new Array[Int](5)
```