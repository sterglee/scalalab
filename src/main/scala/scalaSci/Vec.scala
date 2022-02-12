
package scalaSci


import scala.language.postfixOps


// a class that implements "Vector" functionality. Data are kept as a simple one-dimensional Java array
class Vec( n: Int)  extends  no.uib.cipr.matrix.AbstractVector(n)
    {
    var data = new Array[Double](n)  // the array storage of the data
    
    final def getv() = { data }   // return the data
    
  
  // returns the Vec's data as a two-dimensional array, with a single row
    final def toDoubleArray = { 
       var da = Array.ofDim[Double](1, n)
       var k=0
       while (k<n)  { da(0)(k) = data(k);  k += 1 }
       da
    }
  
  // sets the data efficiently using the reference, i.e. without copying values
  // e.g.  var x = inc(0, 0.01, 10);   var y = Array(0.4, 0.5, -0.33);   x.setv(y)
    final def setv( x: Array[Double]) =  { this.data = x; length = x.length }  
  
    var  length = n
    override final def size() = length
    
  import Vec._
  
final def this(vals: Array[Double]) = {
    this(vals.length)
    var k=0
    while  (k < vals.length) {
      data(k) = vals(k)
      k += 1
    }
}

    // gets the k'th Vec element
  final def apply(k: Int): Double = {
     data(k)
 }
  
  // compares for value equality
   override def equals(other: Any) = other match { 
       
     case that: Array[Double] => // convert the Array[Double] to Vec then compare
       var thatd = new Vec(that)
       equals(thatd)
   
    case that: RichDouble1DArray =>   // compare directtly all the elements of the RichDouble1DArray
         var smDiffs = 0.0
         var k = 0
         var diff = 0.0
         var otherRD = other.asInstanceOf[RichDouble1DArray]
         while (k < n) {
           diff = data(k)-otherRD(k)
           smDiffs += diff*diff
           k += 1
         }
         if (smDiffs > 0.0) false
         else true
    
    case that: Vec => 
         var smDiffs = 0.0
         var k = 0
         var diff = 0.0
         var otherRD = other.asInstanceOf[Vec]
         while (k < n) {
           diff = data(k)-otherRD(k)
           smDiffs += diff*diff
           k += 1
         }
         if (smDiffs > 0.0) false
         else true
         
      case _ => false 

 }

  /* example of clone
var v = vrand(4)   // create one random vector
var vc = v.clone   // close it
v(0)=100     // change at the original vector
vc(0)=200   // change at the cloned vector
   */
override final def clone() = {
  var a =  data.clone()
  new Vec(a)
}  

  // apply the function f to all the elements of the Vec and return the results with a new Vec
 final def  map( f: (Double => Double)): Vec = {
   var vres = new Vec(length)
   var r = 0
   while (r <  length) {
       vres(r) = f(this(r) )
       r += 1
   }
   vres
 }

  // returns these range of elements of the vector with a new vector
  final def slice(from: Int, to: Int): Vec = {
    val vsiz = to-from+1
    var vres = new Vec(vsiz) 
    var r=0
    while (r < vsiz) {
      vres(r) = this(from+r)
      r += 1
    }
    vres
  }
  
  
  final def toList =   data.toList
  final def toSeq =     this.data toSeq   
  final def toIterable = this.data toIterable
  final def toIndexedSeq = this.data toIndexedSeq
  final def toStream = this.data toStream
  final def toSet = this.data toSet
  final def isEmpty = this.data isEmpty
  final def hasDefiniteSize = this.data hasDefiniteSize
  final def head = this.data head
  final def headOption = this.data headOption
  final def last = this.data last
  final def lastOption = this.data lastOption  
  
  final def find( predicate: (Double => Boolean)) =  this.data find predicate 
  
  final def tail = new Vec( this.data tail ) 
  final def init = new Vec( this.data init )
  final def take (n: Int) = new Vec(this.data.take(n))
  final def drop (n: Int) = new Vec(this.data.drop(n))
  final def takeWhile(predicate: Double => Boolean) = new Vec( this.data.takeWhile(predicate))
  final def dropWhile(predicate: Double => Boolean) = new Vec( this.data.dropWhile(predicate))
  
  final def filter( predicate: (Double => Boolean)): Vec =    new Vec ( this.data filter predicate)
  final def filterNot( predicate: (Double => Boolean)): Vec =    new Vec ( this.data filterNot predicate)
  
  final def splitAt(n: Int) = this.data splitAt n
  final def span( predicate: Double => Boolean) =  this.data span predicate
  final def partition( predicate: (Double => Boolean)) = this.data partition predicate
  
  final def forall(predicate: (Double => Boolean)) = this.data forall predicate
  final def exists(predicate: (Double => Boolean)) = this.data exists predicate
  final def count (predicate: (Double => Boolean)) = this.data count predicate
   
  // norm1 of vector X is:   sum_{i} |X_i |
override final def  norm1() =  {
  var sm=0.0
  var k=0
  var cv=0.0
  while (k<length) {
    cv = data(k)
    if (cv < 0)  cv = -cv
    sm += cv
    k += 1
    }
    sm
    }
    
  // norm2 of vector X is:   sqrt(sum_{i} (X_i)^2)
override final def  norm2() =  {
  var nrm = 0.0
  var k = 0
  var cv = 0.0
  while (k < length) {
    cv = data(k)
    nrm += (cv*cv)
    k += 1
      }
   java.lang.Math.sqrt( nrm)
    }
  
  
 override final def  norm2_robust() ={ 
   var scale = 0.0; var ssq = 1.0;
   var i = 0
   while   (i < length) {
            if (data(i) != 0) {
                var absxi = java.lang.Math.abs(data(i));
                if (scale < absxi) {
                    ssq = 1 + ssq * (scale / absxi) * (scale / absxi);
                    scale = absxi;
                } else
                    ssq += (absxi / scale) * (absxi / scale);
            }
            i += 1
   }
        scale * java.lang.Math.sqrt(ssq);
  }
  
override final def  normInf() =  {
  var max = 0.0;
  var i = 0
  while  (i  < length) {
    max = Math.max(Math.abs(data(i)), max)
    i += 1
  }
   max
  }
  
override final def  set(k: Int, value: Double) = { data(k) = value }

override final def get(k: Int) =    data(k)

  // transpose works by converting vector to column RichDouble2DArray
  final def ~ = {
     var Nrows = this.length
  var rdaa = new scalaSci.RichDouble2DArray(Nrows, 1)
  var k = 0
  while ( k < Nrows) {
   rdaa(k, 0) = this(k)
   k += 1
    }
 rdaa
}

  //  for operation between matrices and vectors we try first
  //  if it is compatible to perform them column-wise else
  //  we try row-wise
  //  in case of incompatible dimensions we return the matrix unaltered


  /*
   var m = ones0(2,4)
   var v4 = vones(4)
   var v2 = vones(2)
   var v4m = v4 + m
   var v2m = v2 + m
   
   */
 final def +(m:  Mat): Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
         while (c < m.Ncols) {
           r = 0
          while (r < m.Nrows) {
            res(r, c) = m(r,c) + data(r)
            r += 1
          }
          c += 1
         }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new Mat(m.Nrows, m.Ncols)
          var c = 0; var r = 0
          while (r <  m.Nrows) {
            c = 0
           while  (c < m.Ncols) {
               res(r, c) = m(r,c) + data(c)
               c += 1
           }
           r += 1
          }
        res
      }
  else
m    
  
}

  
  final def +(m:  Matrix): Matrix=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new Matrix(m.Nrows, m.Ncols)
        var c = 1; var r = 1
        while  (c < m.Ncols) {
          r = 1
          while  (r < m.Nrows) {
            res(r, c) = m(r,c) + data(r-1)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
         var res = new Matrix(m.Nrows, m.Ncols)
         var r = 1; var c = 1
         while  ( r < m.Nrows) {
           c = 1
           while  (c < m.Ncols) {
               res(r, c) = m(r,c) + data(c-1)
               c += 1
           }
         r += 1
         }
        res
      }
  else
m    
  
}

  final def +(m:  EJML.Mat): EJML.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new EJML.Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while  (c < m.Ncols)  {
          r = 0
          while   (r <  m.Nrows) {
            res(r, c) = m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
         var res = new EJML.Mat(m.Nrows, m.Ncols)
         var  r = 0; var c = 0 
         while  ( r <  m.Nrows)  {
           c = 0
           while  (c < m.Ncols)  {
               res(r, c) = m(r,c) + data(c)
               c += 1
           }
           r += 1
         }
        res
      }
  else
m    
  
}
  
  
  final def +(m:  EJML.BMat): EJML.BMat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new EJML.BMat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while  (c <  m.Ncols) {
          r = 0
          while  (r < m.Nrows) {
            res(r, c) = m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
         var res = new EJML.BMat(m.Nrows, m.Ncols)
         var r = 0; var c = 0 
         while  (r < m.Nrows) {
           c = 0
           while  (c < m.Ncols)  {
               res(r, c) = m(r,c) + data(c)
               c += 1
           }
           r += 1
         }
        res
      }
  else
m    
  
}

  
  final def +(m:  MTJ.Mat): MTJ.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new MTJ.Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while (c < m.Ncols)  {
          r = 0
          while  (r <  m.Nrows) {
            res(r, c) = m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new MTJ.Mat(m.Nrows, m.Ncols)
          var r = 0; var c = 0   
       while  (r < m.Nrows) {
           c = 0
           while  ( c < m.Ncols) {
               res(r, c) = m(r,c) + data(c)
               c += 1
           }
           r += 1
       }
        res
      }
  else
m    
  
}

  
  final def +(m:  CommonMaths.Mat): CommonMaths.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new CommonMaths.Mat(m.Nrows, m.Ncols)
        var   c = 0;  var r = 0
        while  ( c < m.Ncols)  {
          r = 0 
          while  ( r < m.Nrows)  {
            res(r, c) = m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new CommonMaths.Mat(m.Nrows, m.Ncols)
          var r = 0; var c = 0
          while  ( r <  m.Nrows)  {
            c = 0
            while  ( c <  m.Ncols)  {
               res(r, c) = m(r,c) + data(c)
               c += 1
            }
            r += 1
          }
        res
      }
  else
m    
  
}

  
  final def -(m:  Mat): Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while  (c < m.Ncols)  {
          r = 0
          while  (r < m.Nrows)  {
            res(r, c) = -m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new Mat(m.Nrows, m.Ncols)
          var r = 0;   var  c = 0
          while (r < m.Nrows)  {
            c = 0
            while  (c < m.Ncols) {
               res(r, c) = -m(r,c) + data(c)
               c += 1
            }
            r += 1
          }
        res
      }
  else
m    
  
}

  
  final def -(m:  Matrix): Matrix=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new Matrix(m.Nrows, m.Ncols)
        var c = 1; var r = 1
        while  ( c < m.Ncols)  {
          r = 1
          while  ( r < m.Nrows)  {
            res(r, c) = -m(r,c) + data(r-1)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
         var res = new Matrix(m.Nrows, m.Ncols)
         var r = 1; var c = 1 
         while  ( r < m.Nrows)  {
           c = 1
           while  ( c < m.Ncols)  {
               res(r, c) = -m(r,c) + data(c-1)
               c += 1
           }
           r += 1
         }
        res
      }
  else
m    
  
}

  final def -(m:  EJML.Mat): EJML.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new EJML.Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while  ( c <  m.Ncols)  {
          r = 0
          while  ( r < m.Nrows)   {
            res(r, c) = -m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new EJML.Mat(m.Nrows, m.Ncols)
          var r = 0; var c = 0
          while  ( r < m.Nrows)  {
            c = 0
           while  (c <  m.Ncols)  {
               res(r, c) = -m(r,c) + data(c)
               c += 1
           }
           r += 1
          }
        res
      }
  else
m    
  
}
  
  
  final def -(m:  EJML.BMat): EJML.BMat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
       var res = new EJML.BMat(m.Nrows, m.Ncols)
       var c = 0; var r = 0 
       while  (c <  m.Ncols)   {
          r = 0
          while  (r < m.Nrows)  {
            res(r, c) = -m(r,c) + data(r)
            r += 1
          }
          c += 1
       }
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
          var res = new EJML.BMat(m.Nrows, m.Ncols)
          var r = 0; var c = 0
          while  ( r < m.Nrows)  {
            c = 0
            while  (c < m.Ncols)  {
               res(r, c) = -m(r,c) + data(c)
               c += 1
            }
            r += 1
          }
        res
      }
  else
m    
  
}

  
  final def -(m:  MTJ.Mat): MTJ.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
        var res = new MTJ.Mat(m.Nrows, m.Ncols)
        var c = 0; var r = 0
        while  ( c < m.Ncols)  {
          r = 0
          while  (r < m.Nrows)   {
            res(r, c) = -m(r,c) + data(r)
            r += 1
          }
          c += 1
        }
            
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
        var res = new MTJ.Mat(m.Nrows, m.Ncols)
        var r = 0; var c = 0  
        while  (r < m.Nrows)  {
          c = 0
          while   (c < m.Ncols)  {
               res(r, c) = -m(r,c) + data(c)
               c += 1
          }
          r += 1
        }
        res
      }
  else
m    
  
}

  
  final def -(m:  CommonMaths.Mat): CommonMaths.Mat=  {
    var  N = m.Nrows
    if  (N==length)  // add vector to each column
      {
       var res = new CommonMaths.Mat(m.Nrows, m.Ncols)
       var c = 0;  var r = 0 
       while  (c < m.Ncols)  {
         r = 0
         while  ( r < m.Nrows) {
            res(r, c) = -m(r,c) + data(r)
            r += 1
         }
         c += 1
       }
           
        res
      }
      else
        if (m.Ncols==length)  // add vector to each row
      {
        var res = new CommonMaths.Mat(m.Nrows, m.Ncols)
        var   r = 0; var  c = 0 
      while ( r <  m.Nrows)   {
          c = 0
          while  (c < m.Ncols)  {
               res(r, c) = -m(r,c) + data(c)
               c += 1
          }
          r += 1
      }
        res
      }
  else
m    
  
}

  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
final def *(that: scalaSci.RichDouble2DArray): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length  // length of the vector
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
        var c = 0; var r = 0
        while  (c <  rM) {
           sm = 0.0
           r = 0
          while (r <  rN)  {
             sm += (that(r, c)*this(r))
             r += 1
          }
          rv(c) =  sm
          c += 1
        }
      
      rv
 }
 else if (rM == sN) { // multiply row-wise
    var rv = new Vec(rN)
    var r = 0; var c = 0  
    while  (r < rN) {
           sm = 0.0 
           c = 0
          while  (c < sN)  {
            sm += (that(r, c)*this(c))
            c += 1
          }
          rv(r) = sm
          r += 1
         }
       
         rv
 }
 else
   new Vec(1)   
  }
  

  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
final def *(that: scalaSci.Mat): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0; var r = 0
      while  (c <  rM) {
           sm = 0.0
           r = 0
         while (r <  rN)  {
             sm += (that(r, c)*this(r))
             r += 1
          }
          c += 1
      }
          rv(c) =  sm
        
      rv
 }
 else if (rM == sN) { // multiply row-wise
      var rv = new Vec(rN)
      var r = 0; var c = 0
      while  (r <  rN) {
           sm = 0.0 
           c = 0
           while  (c < sN)  {
            sm += (that(r, c)*this(c))
            c += 1
           }
          rv(r) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  

  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
final def *(that: scalaSci.Matrix): Vec = {
   var rN = that.numRows();   var rM = that.numColumns();  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 1; var r = 1
        while  (c < rM) {
           sm = 0.0
           r = 1
         while (r < rN)  {
             sm += (that(r, c)*this(r-1))
             r += 1
         }
          rv(c-1) =  sm
          c += 1
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
     var r = 1;  var c = 1
     while ( r < rN) {
           sm = 0.0 
           c = 1
          while  (c < sN)   {
            sm += (that(r, c)*this(c-1))
            c += 1
          }
          rv(r-1) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  
  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
  final def *(that: scalaSci.EJML.Mat): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0; var r = 0
     while  (c < rM) {
           sm = 0.0
           r = 0
         while (r < rN)  {
             sm += (that(r, c)*this(r))
             r += 1
         }
          rv(c) =  sm
          c += 1
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
     var r = 0; var c = 0
     while  (r <  rN) {
           sm = 0.0 
           c = 0
          while (c < sN) {
            sm += (that(r, c)*this(c))
            c += 1
          }
          rv(r) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  
  
  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
  final def *(that: scalaSci.EJML.BMat): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0; var r = 0
     while (c <  rM)  {
           sm = 0.0
           r = 0
         while  (r < rN)  {
             sm += (that(r, c)*this(r))
             r += 1
         }
          rv(c) =  sm
          c += 1
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
     var r = 0; var c = 0
     while (r <  rN) {
           sm = 0.0 
           c = 0
          while (c <  sN)  {
            sm += (that(r, c)*this(c))
            c += 1
          }
          rv(r) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  
  
  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
  final def *(that: scalaSci.MTJ.Mat): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0; var r = 0
     while  (c < rM) {
           sm = 0.0
           r = 0
           while   (r < rN)  {
             sm += (that(r, c)*this(r))
             r += 1
           }
          rv(c) =  sm
          c += 1
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
      var rv = new Vec(rN)
      var r = 0; var c = 0 
      while (r < rN) {
           sm = 0.0 
           c = 0
          while (c < sN) {
            sm += (that(r, c)*this(c))
            c += 1
          }
          rv(r) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  
  // multiply vector with a matrix along compatible dimensions if possible
  // i.e. Vec(N) * Mat(N, M) -> Vec(M) treating Mat column-wise,
  //      Vec(N) * Mat(M, N) -> Vec(M) treating Mat row-wise
  final def *(that: scalaSci.CommonMaths.Mat): Vec = {
   var rN = that.Nrows;   var rM = that.Ncols;  
   var sN = this.length
   var sm = 0.0
   
    if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0;  var r = 0
     while  (c < rM) {
           sm = 0.0
           r = 0
         while  (r < rN)  {
             sm += (that(r, c)*this(r))
             r += 1
         }
          rv(c) =  sm
          c += 1
        }
      rv
 }
 else if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
     var r = 0; var c = 0
     while  (r <  rN) {
           sm = 0.0 
           c = 0
           while  (c < sN) {
            sm += (that(r, c)*this(c))
            c += 1
           }
          rv(r) = sm
          r += 1
       }
         rv
 }
 else
   new Vec(1)   
  }
  

  

  final def zeros() =  {
    var k=0
    while  (k < length) {
      data(k) = 0.0
      k += 1
    }
}


    // APPEND OR PREPEND SCALAR AT VECTOR
    // append element as last vector's element,  e.g. var data = vrand(2); var vv = 4.55 ::  data   // 4.55 is placed as the last element of the vector
final def ::(elem: Double): Vec = {
 var v2 = new Array[Double](length+1)
   // final def copy (src : AnyRef, srcPos : Int, dest : AnyRef, destPos : Int, length : Int) : Unit
 Array.copy (data, 0, v2, 0, length)
 v2(length) = elem
 length += 1
 data = v2
 this
}

// prepend element as first vector's element, e.g.  var data = vrand(2); var vv = 4.55 :::  data   // 4.55 is placed at the front of the vector
final def :::(elem: Double): Vec = {
 var v2 = new Array[Double](length+1)
 v2(0) = elem
 Array.copy (data, 0, v2, 1, length)
 length += 1
 data = v2
 this
}

    // append element as last vector's element,  e.g.  var data = vrand(2); var vv =  data ::<  4.55 // 4.55 is placed as the last element of the vector
final def ::<(elem: Double): Vec = {
 var v2 = new Array[Double](length+1)
 Array.copy (data, 0, v2, 0, length)
 v2(length) = elem
 length += 1
 data = v2
 this
}

// prepend element as first vector's element,  e.g.  var data = vrand(2); var vv =  data :::< 4.55   // 4.55 is placed at the front of the vector
final def :::<(elem: Double): Vec = {
 var v2 = new Array[Double](length+1)
 v2(0) = elem
 Array.copy (data, 0, v2, 1, length)
 length += 1
 data = v2
 this
}



    // APPEND OR PREPEND VECTOR AT VECTOR

// append left-side vector to right-side vector, e.g.  vones(2) :: vrand(3)
//  res0: scalaSci.Vec =  0.597  0.122  0.045  1.000  1.000
//  returns:  <this, v2>
final def ::(v2: Vec): Vec = {
    var len2 = v2.length
    var lenAll = length+len2
    var vall = new Array[Double](lenAll)
    Array.copy(data,  0, vall, 0, length)  // copy first array
    var secIdx=0 // copy second array
    var k = length
    while  (k < lenAll)  { vall(k) = v2(secIdx); secIdx+=1; k+=1 }
    
 new Vec(vall)
}
 
//  prepend left-side vector to right-side vector, e.g.  vones(2) ::: vrand(3)
//  res1: scalaSci.Vec = 1.000  1.000  0.212  0.633  0.130
//  returns:    <v2, this > 
final def :::(v2: Vec): Vec = {
    var len2 = v2.length
    var lenAll = length+len2   // total vector length
    var vall = new Array[Double](lenAll)  // allocate space for the result vector
    Array.copy(v2.data,  0, vall, 0, len2)  // copy second array
    var secIdx=0
    var k = len2
    while  (k<lenAll)  { vall(k) = data(secIdx); secIdx+=1; k+=1 }
    
new Vec(vall)
}

final def apply(rangeSpec: scalaSci.Vec): Vec = {
  var sL = rangeSpec(0).toInt
  var sl = rangeSpec.length
  var sInc = (rangeSpec(1)-rangeSpec(0)).toInt
  var sH = rangeSpec(sl-1).toInt

  apply(sL, sInc, sH)   
}  
  
  
 final def apply(rangeSpec: scalaSci.MatlabRange.MatlabRangeNext): Vec = {
    var sL = rangeSpec.mStart.inc.toInt
    var sH = rangeSpec.mStart.endv.toInt
    
    apply(sL, sH)
 } 

// extracts a subvector, data(low:high)
  final def apply(low: Int,  high: Int): Vec = {
    var subs = high-low+1;
    var num = Math.abs(subs) 
    var subVec = new Vec(num)   // create a Vector to keep the extracted range
      // fill the created vector with values
    var celem = low  // indexes current element
    var idx = 0  // indexes at the new Vector
    var inc = 1; if (low > high) inc = -1
    if (inc > 0) {
        while  ( celem <= high )   {
          subVec.data(idx) = data(celem)
          idx += 1
          celem += inc
          } 
 }
else {
 while  ( celem >= high )   {
          subVec.data(idx) = data(celem)
          idx += 1
          celem += inc
          }
      }
     subVec
}

    
// extracts a subvector, data(low:inc:high)
  final def apply(low: Int,  inc: Int, high: Int): Vec = {
    
    var num = Math.floor( (high-low) / inc).asInstanceOf[Int]+1
    if (num <0 ) {
      println("incorrect parameters in apply("+low+","+inc+","+high+")")
      this
    }
    var subVec = new Vec(num)   // create a Vector to keep the extracted range
      // fill the created vector with values
    var celem = low  // indexes current element
    var idx = 0  // indexes at the new Vector
    if (inc > 0) {
        while  ( celem <= high )   {
          subVec.data(idx) = data(celem)
          idx += 1
          celem += inc
          } 
 }
else {
 while  ( celem >= high )   {
          subVec.data(idx) = data(celem)
          idx += 1
          celem += inc
          }
      }
     subVec
 }


// the default update does not support dynamic resizing for speed
  final def  update(k: Int, value: Double): Unit = {
 data(k) = value;
 }

  
final def  update(k: Int, value: Double, resize: Boolean): Unit = {
    if (k >= length) {   // increase the size of the vector dynamically
 var newSize  = (Vec.vecResizeFactor*k).asInstanceOf[Int]
 var v2 = new Array[Double](newSize)
 Array.copy (data, 0, v2, 0, length) 
 data = v2
 length = newSize
  }

 data(k) = value;
 }

      /* update a Vector subrange by assigning a Vector, e.g.
  var v = vrand(20)
  var vin  = vones(2)
  v(2, 2+vin.length-1) = vin   //  insert the vin, overwritten other elements 
 */
  final def update(lowp:Int, incp: Int, highp: Int, vr: Vec): Unit = {
     var low=lowp; var high=highp; var inc = incp;
     if (high < low)  {
            if (inc > 0)  {
                println("negative subrange increment is required")
                this
            }
            var tmp=highp; var high=lowp; low = tmp;
            inc  = -inc
        }

      var vl_rhs = vr.length   // length of right-hand side vector
     var rangeLen = ((high-low)/inc).asInstanceOf[Int]+1    // length of target range
     if (vl_rhs != rangeLen)  {  // improper sizes of the involved vector subranges for assignment
         println("target vector subrange and right-hand size vector lengths do not agree, i.e. v1_rhs =  "+vl_rhs+", rangeLen = "+rangeLen)
         this   // return the vector unaltered
     }

      if (high >= length)  {   // dynamically increase the size of the Vector
 var newSize  = (Vec.vecResizeFactor*high).asInstanceOf[Int]
 var v2 = new Array[Double](newSize)
 Array.copy (data, 0, v2, 0, length)
 data = v2
 length = newSize
     }

     // copy the values of the vr
        var k=0; var lidx=low
        while (k < vr.length) {
            data(lidx) = vr(k)
            k += 1
            lidx += inc 
        }
  }
 
  
// update the range from lowp up to highp to v
  final def update(lowp: Int, highp: Int, v: Double) = 
    {
      var k = lowp
      while (k<=highp) {
        data(k) = v
        k += 1
      }
    }
  
// alternative notation
final def update(allSymbol: scala.::.type, lowp: Int, highp: Int, v: Double): Unit = 
      update(lowp, highp, v)

  // e.g. var v = 4(10);  v(2, 5, ::) = 4.34
 final def update( lowp: Int, highp: Int,  allSymbol:  scala.::.type, v: Double): Unit = 
      update(lowp, highp, v)
    
final def update(allSymbol: scala.::.type, lowp:Int,  highp: Int, vr: Vec): Unit = 
  update(lowp, highp, vr)

final def update(lowp:Int,  highp: Int,  allSymbol:  scala.::.type, vr: Vec): Unit = 
  update(lowp, highp, vr)
  
 
    // update a Vector subrange by assigning a Vector
 final def update(lowp:Int,  highp: Int, vr: Vec): Unit = {
     var low=lowp; var high=highp; var inc = 1;
     if (high < low)  {
            if (inc > 0)  {
                println("negative subrange increment is required")
                this
            }
            var tmp=highp; var high=lowp; low = tmp;
        }

      var vl_rhs = vr.length   // length of right-hand side vector
     var rangeLen = ((high-low)/inc).asInstanceOf[Int]+1    // length of target range
     if (vl_rhs != rangeLen)  {  // improper sizes of the involved vector subranges for assignment
         println("target vector subrange and right-hand size vector lengths do not agree, i.e. v1_rhs =  "+vl_rhs+", rangeLen = "+rangeLen)
         this   // return the vector unaltered
     }

      if (high >= length)  {   // dynamically increase the size of the Vector
 var newSize  = (Vec.vecResizeFactor*high).asInstanceOf[Int]
 var v2 = new Array[Double](newSize)
 Array.copy (data, 0, v2, 0, length)
 data = v2
 length = newSize
     }

     // copy the values of the vr
        var k=0; var lidx=low
        while (k < vr.length) {
            data(lidx) = vr(k)
            k += 1
            lidx += inc
        }
  }

// randomizes the contents of the vector
 final def rand() {
   var  ran = com.nr.test.NRTestUtil.ran  // global ranno generator
   var k=0
   while  (k < length) {
      data(k) = 2.0*ran.doub()-1.0
      k += 1
    }
}

// unary Minus applied to a Vector implies negation of all of its elements
final def unary_- : Vec =  {
      var nv = new Vec(this.length)  // construct a Vector of the same dimension
   var i=0; 
   while (i<this.length) {
      nv.data(i) = -data(i)  // negate element
      i += 1
    }
    nv
}    
   // perform an addition of the Vector specified as a parameter to this
   // if the Vectors are not of the same size:
   //   if the receiver's length is 1 expand the receiver filling with the value of the 0th element (since implicit conversion is assumed)
final def + (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    
    var nv = new Vec(nR)  
    var k=0
    while (k < nR) {
          nv(k)=data(k)+that.data(k)
          k += 1
        }
         nv
      }
      
  
// IN-PLACE Operations: Update directly the receiver, avoiding creating a new return object
// in-place addition
final def ++ (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    var nThat = that.length   // right argument length
    var minLen = nThat;
    if (nR < minLen)  minLen = nR;
 // perform the addition
    var k=0
    while (k<minLen)  {
        this.data(k) += that.data(k)
        k+=1
    }
    this
}

  // in-place addition
final def ++ (that: Double): Vec =  {
    var nR = this.length // receiver's length
    // perform the addition
    var k=0
    while (k<nR)  {
        this.data(k) += that
        k+=1
    }
    this
}

// in-place subtraction
final def -- (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    var nThat = that.length   // right argument length
    var minLen = nThat;
    if (nR < minLen)  minLen = nR;
 // perform the addition
    var k=0
    while (k<minLen)  {
        this.data(k) -= that.data(k)
        k+=1
    }
    this
}

  // in-place subtraction
final def -- (that: Double): Vec =  {
    var nR = this.length // receiver's length
    // perform the addition
    var k=0
    while (k<nR)  {
        this.data(k) -= that
        k+=1
    }
    this
}


  // in-place multiplication
final def ** (that: Double): Vec =  {
    var nR = this.length // receiver's length
    // perform the addition
    var k=0
    while (k<nR)  {
        this.data(k) *= that
        k+=1
    }
    this
}

  
  // in-place multiplication
final def ** (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    // perform the addition
    var k=0
    while (k<nR)  {
        this.data(k) *= that.data(k)
        k+=1
    }
    this
}

  // in place division
 final def \/ (that: Double): Vec =  {
    var nR = this.length // receiver's length
    // perform the addition
    var k=0
    while (k<nR)  {
        this.data(k) /= that
        k+=1
    }
    this
}

// any method that ends in a ':' character is invoked on its right operand, passing in the left operand
// we define some operators that help to avoid implicit conversions for even better code efficiency

final def +: (that: Double): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) += that
       k+=1
   }
        this 
}

final def +: (that: Int): Vec =  {
  var N=this.length
   var k=0
   while (k<N) {
       this.data(k) += that
       k+=1
   }
        this
}

final def +: (that: Long): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) += that
       k+=1
   }
        this
}

final def -: (that: Double): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that-this.data(k)
       k+=1
   }
    this
}

final def -: (that: Int): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that-this.data(k)
       k+=1
   }
    this
}

final def -: (that: Long): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that-this.data(k)
       k+=1
   }
    this
}


final def *: (that: Double): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that*this.data(k)
       k+=1
   }
    this
}

final def *: (that: Int): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that*this.data(k)
       k+=1
   }
    this
}

final def *: (that: Long): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that*this.data(k)
       k+=1
   }
    this
}

  final def jmul(that: Vec) = scalaSci.math.array.FMJ.mul(data, that.data)

final def /: (that: Double): Vec =  {
   var N=this.length
   var k=0
   while (k<N) {
       this.data(k) = that/this.data(k)
       k+=1
   }
    this
}


final def / (that: Double): Vec =  {
   var nv = new Vec(length)
   var k=0
   while (k<length) {
       nv.data(k) = this.data(k)/that
       k+=1
   }
    nv
}



final def / (that: Array[Double]): Vec =  {
   var nR = this.length // receiver's length
    var nThat = that.length   // right argument length
    var maxLen = nThat;
    if (nR > maxLen)  maxLen = nR;

    var nv = new Vec(maxLen)  // create a new Vector equal to the maximum sized Vector
    if (nR == 1 && nR<maxLen)  {  // implicit conversion
        var value = this.data(0)  // get the value to copy to the coverted array
        var k=0
        while (k < maxLen) {
          nv(k)=value / that(k)
          k += 1
        }
         nv
      }
    else  {
        if (nR > nThat) {   // receiver's Vector largest
       var i=0
       while (i < nThat)  {
          nv(i) = data(i) / that(i)
          i += 1
       }
       i = nThat
       while (i < nR) {
         nv(i) = data(i)
         i += 1
        }
        nv
    }
    else  {   // parameter's Vector largest
        var i = 0
        while (i < nR) {
         nv(i) = data(i) / that(i)
         i += 1
        }
        i = nR
        while (i < nThat) {
         nv(i) = that(i)
         i += 1
        }
      nv
       }
    }
}


final def / (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    
    var nv = new Vec(nR)  
    var k=0
    while (k < nR) {
          nv(k)=data(k) / that(k)
          k += 1
        }
         nv
      }


final def + (that: Array[Double]): Vec =  {
    var nR = this.length // receiver's length
    var k = 0
    var nv = new Vec(nR)  
    while (k < nR) {
          nv(k)=data(k)+that(k)
          k += 1
        }
         nv
      }
      

final def - (that: Vec): Vec =  {
  var nR = this.length // receiver's length
  
   var nv = new Vec(nR) 
    var k = 0
    while (k < nR) {
          nv(k)=data(k) -that.data(k)
          k += 1
        }
         nv
      }


final def * (that: Vec): Vec =  {
    var nR = this.length // receiver's length
    var nv = new Vec(nR)
    var k=0
    while (k < nR) {
          nv(k)  = data(k)*that.data(k)
          k += 1
        }
       nv
      }
      
// return a vector of the differences v_{k+1} - v_k
  final def diff() = {
  var Dl = this.length -  1
  var vdiff = new Vec(Dl)
  var  k = 0
  while (k<Dl) {
     vdiff(k)  = this(k+1) - this(k)
     k += 1
  }
  vdiff
}


  
 final def dot(that: Array[Double]) = {
    var sm=0.0
  var N = this.length
  var k = 0
  while  ( k < N) {
    sm += (data(k)*that(k))
    k += 1
  }
 sm
 }
  
  
  
 final def dot(that: RichDouble1DArray) = {
    var sm=0.0
  var N = this.length
  var k = 0
  while  ( k < N) {
    sm += (data(k)*that(k))
    k += 1
  }
 sm
 }
  
 final def dot(that: Vec)  = {
    var sm=0.0
  var N = this.length
  var k = 0
  while  ( k < N) {
    sm += (data(k)*that(k))
    k += 1
  }
 sm
 }
 
final def cross(that: Array[Double]): RichDouble1DArray = {
    var   N  =  that.length
    var result = new Array[Double](N)
    var r = 0
    while  (r <  N)   {
         result(r) = that(r) * data(r)
         r += 1
    }
 new RichDouble1DArray(result)
  }
 
final def cross(that: Vec): RichDouble1DArray = {
    var   N  =  that.length
    var result = new Array[Double](N)
    var r = 0
    while  (r <  N)   {
         result(r) = that(r) * data(r)
         r += 1
    }
 new RichDouble1DArray(result)
  }
 
  
final def cross(that: RichDouble1DArray): RichDouble1DArray = {
    var   N  =  that.length
    var result = new Array[Double](N)
    var r = 0
    while  (r <  N)   {
         result(r) = that(r) * data(r)
         r += 1
    }
 new RichDouble1DArray(result)
  }
 
  

final def + (that: Double): Vec =  {
      var nv = new Vec(length)
      var i=0
      while  (i< length) {
        nv(i) = data(i)+that
        i += 1
      }
   nv
}

// Vec - Array[Double]
 final def - (that: Array[Double]): Vec =  {
   var  rN = this.length
   var nv = new Vec(rN)                     // is a 1 X 1 matrix and the parameter is a larger one
       
   var i=0
   while (i<rN) {
       nv.data(i) = data(i) - that(i)
       i+=1
     }
     return nv
    }  // implicit conversion

 
final def -: (that: Array[Double]): Vec =  {
   this - that
}

 final def - (that: Double): Vec =  {
    var nv = new Vec(length)
    var i=0
      while  (i< length) {
       nv(i) = data(i)-that
       i += 1
      }
   nv
}

 final def * (that: Double): Vec =  {
    var length = this.length
    var nv = new Vec(length)
    var i=0
    while  (i< length) {
       nv(i) = data(i)*that
       i += 1
      }
   nv
}



    // Vector * Double[Array]
 final def * (that: Array[Double]): Vec =  {
    var thatLen = that.length
    var nv = new Vec(thatLen)
    var i=0
    while  (i< thatLen) {
       nv(i) = data(i)*that(i)
       i += 1
      }
   return nv
        }
        
  
final def  print = {
  var digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var nelems = length

     var elemsPerLine = 30
     var i=1
     var lineElems = 0
    while (i<nelems) {
        var str = digitFormat.format(data(i)) +"  "
        Console.print(str)
        lineElems += 1
        if (lineElems==elemsPerLine) {
          lineElems=0
          println
         }
       i+=1
        }
 }


override  final def  toString(): String = {
     import java.text.DecimalFormat
     import java.text.DecimalFormatSymbols
     import java.util._
     import scalaSci.PrintFormatParams._

    var formatString = "0."
     for (k<-0 until scalaSci.PrintFormatParams.vecDigitsPrecision)
       formatString += "0"
    var digitFormat = new DecimalFormat(formatString)
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")))
    
     var mxElems = length
     var moreElems = ""
     if (mxElems > scalaSci.PrintFormatParams.vecMxElemsToDisplay )  {
          // vector has more elements than we can display
         mxElems = scalaSci.PrintFormatParams.vecMxElemsToDisplay
         moreElems = " .... "
     }
    var sb = new StringBuilder()
    var i=0
     while (i < mxElems) {
       sb.append(digitFormat.format(data(i))+"  ")
        i += 1
       }
     sb.append(moreElems+"\n")
     sb.toString

 }

  
  
  
  
final def sin(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sin(this(i))
            i += 1
            }
          ov
    }

    final def cos(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.cos(this(i))
            i += 1
            }
          ov
    }

final def tan(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.tan(this(i))
            i += 1
            }
          ov
    }

    final def asin(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.asin(this(i))
            i += 1
            }
          ov
    }

final def acos(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.acos(this(i))
            i += 1
            }
          ov
    }

    final def atan(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.atan(this(i))
            i += 1
            }
          ov
    }

    final def sinh(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sinh(this(i))
            i += 1
            }
          ov
    }

    final def cosh(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.cosh(this(i))
            i += 1
            }
          ov
    }

    final def tanh(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.tanh(this(i))
            i += 1
            }
          ov
    }

final def exp(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.exp(this(i))
            i += 1
            }
          ov
    }

    final def log(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log(this(i))
            i += 1
            }
          ov
    }

    final def log2(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var conv = java.lang.Math.log(2.0)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log(this(i))/conv
            i += 1
            }
          ov
    }

    final def log10(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.log10(this(i))
            i += 1
            }
          ov
    }

    final def abs(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.abs(this(i))
            i += 1
            }
          ov
    }

    final def ceil(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.ceil(this(i))
            i += 1
            }
          ov
    }

    final def floor(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.floor(this(i))
            i += 1
            }
          ov
    }

    final def round(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.round(this(i))
            i += 1
            }
          ov
    }

  final def sqrt(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.sqrt(this(i))
            i += 1
            }
          ov
    }

    final def toDegrees(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.toDegrees(this(i))
            i += 1
            }
          ov
    }

    final def toRadians(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.toRadians(this(i))
            i += 1
            }
          ov
    }
    
    final def pow(exponent:Double): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            ov.data(i) =  java.lang.Math.pow(this(i), exponent)
            i += 1
            }
          ov
    }
  
final def  mean(): Double =  {
       var  mn = 0.0
       var  m = this.length
       var i=0
       while  (i < m) {
            mn += this.data(i)
            i += 1
          }
        mn /=  m;
       mn
    }

final def sum(): Double = {
    var sm = 0.0
    var m = this.length
    var i=0
    while (i < m) {
        sm += this(i)
        i += 1
    }
    sm
}

final def prod(): Double = {
    var pd = 1.0
    var m = this.length
    var i=0
    while (i < m) {
        pd *= this(i)
        i += 1
    }
    pd
}

// uniformly distributed vector
final def rand(N: Int):Vec = {
    var  data = new Array[Double](N);
    var k=0
    while  (k < N) {
      data(k) = java.lang.Math.random
      k += 1
    }
   new Vec(data)

}


  
final def min(): Double = {
  var l = this.length
  var mn = this(0)
  var k = 1
  while  (k <  l) {
    if (this(k) < mn) mn = this(k)
    k += 1
  }
mn
}

final def max(): Double = {
  var l = this.length
  var mx = this(0)
  var k = 1
  while  (k <  l) {
   if (this(k) > mx) mx = this(k)
   k += 1
  }
mx
}
  
    
    // in-place operations
      
  
  
final def sini(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.sin(this(i))
            i += 1
            }
         this
    }

final def cosi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.cos(this(i))
            i += 1
            }
          this
    }

final def tani(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.tan(this(i))
            i += 1
            }
          this
    }

 final def asini(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.asin(this(i))
            i += 1
            }
          this
    }

final def acosi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.acos(this(i))
            i += 1
            }
          this
    }

    final def atani(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.atan(this(i))
            i += 1
            }
          this
    }

    final def sinhi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.sinh(this(i))
            i += 1
            }
          this
    }

    final def coshi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.cosh(this(i))
            i += 1
            }
          this
    }

    final def tanhi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.tanh(this(i))
            i += 1
            }
          this
    }

final def expi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.exp(this(i))
            i += 1
            }
          this
    }

    final def logi(): Vec = {
       var N = data.length;
       var ov = new Vec(N)
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.log(this(i))
            i += 1
            }
          this
    }

    final def log2i(): Vec = {
       var N = data.length;
       var conv = java.lang.Math.log(2.0)
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.log(this(i))/conv
            i += 1
            }
          this
    }

    final def log10i(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.log10(this(i))
            i += 1
            }
         this
    }

    final def absi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.abs(this(i))
            i += 1
            }
          this
    }

    final def ceili(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.ceil(this(i))
            i += 1
            }
          this
    }

    final def floori(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.floor(this(i))
            i += 1
            }
          this
    }

    final def roundi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.round(this(i))
            i += 1
            }
          this
    }

  final def sqrti(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.sqrt(this(i))
            i += 1
            }
          this
    }

    final def toDegreesi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.toDegrees(this(i))
            i += 1
            }
          this
    }

    final def toRadiansi(): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.toRadians(this(i))
            i += 1
            }
          this
    }
    
    final def powi(exponent:Double): Vec = {
       var N = data.length;
       var i=0
       while  (i< N) {
            this(i) =  java.lang.Math.pow(this(i), exponent)
            i += 1
            }
          this
    }
    
  
  
    
  // saves the contents of the matrix to fileName
final def save(fileName: String) = {
  scalaSci.math.io.ioUtils.saveAscii(getv(), fileName)
}

final def read(fileName: String) = {
   data  =  scalaSci.math.io.ioUtils.readD1Ascii(fileName)
  }
  
  
final def load(fileName: String) = {
   read(fileName)
}

  }


object Vec  {

   var vecResizeFactor = 1.5   // controls by how much to increase dynamically the size of the vector }
   final def setVecResizeFactor( newResizeFactor: Double):Double = { var prevResizeFactor = vecResizeFactor;  vecResizeFactor = newResizeFactor; prevResizeFactor }

    

  /* construct a Vector directly, e.g. 
  var x = 9.6  // some value
var v = Vec(0.6, x, -0.3, cos(0.6*x))  // we use arbitrary expressions inside
  */
 final def apply(values: Double*):scalaSci.Vec = {
      new scalaSci.Vec(values.toArray)
  }

  
  final def fromSeq(buf: Seq[Double]) =  {
     var  length = buf.length
     var data = new Vec(length)
     for (k <- 0 until length)
       data(k) = buf(k)
     
      data
  }

  
// convert the Vec to a row matrix
final def toMatrix(data: Vec):Matrix    =  {
  var  N = data.length
  var mm = new Matrix(1, N)
  var k = 0
  while (k <  N) {
    mm(k, 0) = data(k)
    k += 1
  }
  
    mm
}

  
final def toEJMLMat(data: Vec):scalaSci.EJML.Mat    =  {
  var  N = data.length
  var mm = new scalaSci.EJML.Mat(1, N)
  var k = 0
  while  (k <  N) {
    mm(k, 0) = data(k)
    k += 1
  }
  mm
}
   
  
final def toMTJMat(data: Vec):scalaSci.MTJ.Mat  =  {
  var  N = data.length
  var mm = new scalaSci.MTJ.Mat(1, N)
  var k = 0
  while (k <  N) {
    mm(k, 0) = data(k)
    k += 1
  }
    mm
}

final def toCommonMathsMat(data: Vec):scalaSci.CommonMaths.Mat  =  {
  var  N = data.length
  var mm = new scalaSci.CommonMaths.Mat(1, N)
  var k = 0
  while (k <  N) {
    mm(k, 0) = data(k)
    k += 1
  }
  
    mm
}

  
}

         

object  matlabColons {

// treat pattern low:inc:high, e.g.  data("3:2:15")
    final def unapply (input: String) : Option[(Int, Int, Int)] = {
         try {
             if (input contains ":") {
                 val splitQuote = input split ":"
                 val length = splitQuote.length
                 if (length == 3)   // increment also specified
                    Some(splitQuote(0).toInt, splitQuote(1).toInt, splitQuote(2).toInt)
                 else {
                     val firstLimit = splitQuote(0).toInt
                     val secondLimit = splitQuote(1).toInt
                     var inc = 1
                     if (firstLimit > secondLimit)
                        inc  = -1
                    Some(firstLimit, inc, secondLimit)
             } 
  }
          
             else {
                 None
             }
         }
         catch {
             case _ : NumberFormatException => None
         }
     }

  
  
}

