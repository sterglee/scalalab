
// serves for implicitly converted to that type when certain operations cannot be performed, 
// e.g  2+Mat
package scalaSci


class RichNumber(v: Double) {
   val  value = v    // the RichNumber includes a Double field that corresponds to its value

  
  // integer exponent
  final def ^^^( exponent: Int) = java.lang.Math.pow(v, exponent).asInstanceOf[Int]

  final def ^^ ( exponent: Double) = java.lang.Math.pow(v, exponent)
  
  final def +(that: Array[Double]): Array[Double] = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r = 0
    while (r < N) {
         result(r) = that(r) + value
         r += 1
      }
 
    result
    
  }
  
 
  final def -(that: Array[Double]): Array[Double] = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r = 0 
    while (r < N) {
         result(r) = -that(r) + value
         r += 1
      }
 
    result
    
  }
  

  
 final def *(that: Array[Double]): Array[Double] = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r = 0 
    while (r < N ) {  
         result(r) = that(r) * value
         r += 1
     }
 
    result
    
  }
  

  

 final def /( that:  Array[Double]): Array[Double]= {
    var   N  =  that.length
    
   var result = new Array[Double](N)
   var r=0
   while (r < N)  {
        var tmp = that(r)
        if (tmp != 0)
            result(r) = value /  tmp
        else
            result(r) = 0.0
         r += 1
       }
      
    result
 }

  


  final def +(that: Array[Array[Double]]): Array[Array[Double]] = {
    var   N  =  that.length
    var   M = that(0).length

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r < N) {
       c = 0
       while (c <  M)   {
         result(r)(c) = that(r)(c) + value
         c += 1
       }
       r += 1
     }
 
    result
    
  }
  

  final def -(that: Array[Array[Double]]): Array[Array[Double]] = {
    var   N  =  that.length
    var   M = that(0).length

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0 
    while (r <  N)  {
      c = 0
      while (c < M)  {
         result(r)(c) = -that(r)(c) + value
         c += 1
      }
      r += 1
    }
 
    result
    
  }
  
  
  
  final def *(that: Array[Array[Double]]): Array[Array[Double]] = {
    var   N  =  that.length
    var   M = that(0).length

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0 
    while (r < N)   {
      c = 0
      while (c < M)  {
         result(r)(c) = that(r)(c) * value
         c += 1
      }
      r += 1
    }
      
    result
    
  }
  

 final def /( that:  Array[Array[Double]]): Array[Array[Double]]= {
    var   N  =  that.length
    var   M = that(0).length

   var result = Array.ofDim[Double](M, N)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r)(c)
        if (tmp != 0)
            result(r)(c) = value /  tmp
        else
            result(r)(c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

  
// handle RichDouble1DArray, RichDouble2DArray

  final def +(that: RichDouble1DArray): RichDouble1DArray = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r = 0 
    while  (r < N)  {
         result(r) = that(r) + value
         r += 1
    }
      
    new RichDouble1DArray(result)
    
  }
  

  final def -(that: RichDouble1DArray): RichDouble1DArray = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r=0
    while (r < N)   {
         result(r) = -that(r) + value
         r += 1
    }
 
    new RichDouble1DArray(result)
    
  }
  
  

  
 final def *(that: RichDouble1DArray): RichDouble1DArray = {
    var   N  =  that.length
    
    var result = new Array[Double](N)
    var r = 0 
    while (r < N)   {
         result(r) = that(r) * value
         r += 1
    }
 
    new RichDouble1DArray(result)
    
  }
  

  

 final def /( that:  RichDouble1DArray): RichDouble1DArray= {
    var   N  =  that.length
    
   var result = new Array[Double](N)
   var r=0
   while (r < N)  {
        var tmp = that(r)
        if (tmp != 0)
            result(r) = value /  tmp
        else
            result(r) = 0.0
         r += 1
       }
      
    new RichDouble1DArray(result)
 }

  


  final def +(that: RichDouble2DArray): RichDouble2DArray = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0 
    while  (r < N)  {
      c = 0
      while (c < M)   {
         result(r)(c) = that(r, c) + value
         c += 1
      }
      r += 1
    }
 
    new RichDouble2DArray(result)
    
  }
  

  final def -(that: RichDouble2DArray): RichDouble2DArray = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
    while (r < N)   {
      c = 0
      while (c < M)  {
         result(r)(c) = -that(r, c) + value
         c += 1
      }
      r += 1
    }
      
    new RichDouble2DArray(result)
    
  }
  
  
  
  final def +(that: ComplexMatrix): ComplexMatrix  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = new ComplexMatrix(N, M)
    var r = 0; var c = 0
    while (r < N)  {
      c = 0
      while (c <  M)  {
         result(r, c) = that(r,c) + value
         c += 1
      }
      r += 1
    }
    result
    
  }
  
  final def -(that: ComplexMatrix): ComplexMatrix  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = new ComplexMatrix(N, M)
    var r = 0; var c = 0 
    while  (r < N)  {
      c = 0
      while  (c < M)  {
         result(r, c) =  -that(r,c) + value
         c += 1
      }
      r += 1
    }
 
    result
  }
  
  final def *(that: ComplexMatrix): ComplexMatrix  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = new ComplexMatrix(N, M)
    var r = 0; var c = 0
    while  (r <  N)   {
      c = 0
      while (c <  M)   {
         result(r, c) = that(r,c) * value
         c += 1
      }
      r += 1
    }
    result
  }
  
  final def *(that: RichDouble2DArray): RichDouble2DArray = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
    while (r < N)   {
      c = 0
      while (c < M)  {
         result(r)(c) = that(r,c) * value
         c += 1
      }
      r += 1
    }
 
    new RichDouble2DArray(result)
  }

 final def /( that:  RichDouble2DArray): RichDouble2DArray = {
    var   N  =  that.Nrows
    var   M = that.Ncols
    
   var result = Array.ofDim[Double](M, N)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r)(c) = value /  tmp
        else
            result(r)(c) = 0.0
         c += 1
       }
      r += 1
    }
    new RichDouble2DArray(result)
 }

  
 // addition of RichNumber and Vec
 final def +( that:  scalaSci.Vec ): scalaSci.Vec = {
   var   N  =  that.length
    
   var result = new scalaSci.Vec(N)
   var r=0
   while (r < N)   {
         result(r) = that(r) + value
         r += 1
       }
    result 
 }

   // subtraction of Vec from a RichNumber 
 final def -( that:  scalaSci.Vec ): scalaSci.Vec = {
   var   N  =  that.length
    
   var result = new scalaSci.Vec(N)
   var r=0
   while (r < N)   {
         result(r) =  value - that(r) 
         r += 1
       }
    result
 }

  
 // multiplication of RichNumber and Vec
 final def *( that:  scalaSci.Vec ): scalaSci.Vec = {
   var   N  =  that.length
    
   var result = new scalaSci.Vec(N)
   var r=0
   while (r < N)   {
         result(r) = that(r) * value
         r += 1
       }
    result
 }

  
 // division of a  RichNumber with a Vec
 final def /( that:  scalaSci.Vec ): scalaSci.Vec = {
   var   N  =  that.length
    
   var result = new Vec(N)
   var r=0
   while (r < N)   {
         var tmp = that(r)
         if (tmp!=0)
            result(r) =  value / tmp
          else
            result(r) = 0.0
         r += 1
       }
    result
 }

  
   
  final def +(that: scalaSci.Matrix): scalaSci.Matrix = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
    while (r < N)   {
      c = 0
      while (c < M)   {
         result(r)(c) = that(r,c) + value
         c += 1
      }
      r += 1
    }
 
    new scalaSci.Matrix(result, true)
    
  }

  final def +(that: scalaSci.JBLAS.Mat): scalaSci.JBLAS.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0;
    while (r <  N)  {
      c = 0
      while (c <  M)  {
         result(r)(c) = that(r,c) + value
         c += 1
      }
      r += 1
    }
     new scalaSci.JBLAS.Mat(result)
    
  }
  
// subtraction of a Matrix from a RichNumber
  final def -(that: scalaSci.Matrix): scalaSci.Matrix = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
    while (r < N)  {
      c = 0
      while (c < M)  {
         result(r)(c) =  value - that(r,c) 
         c += 1
      }
      r += 1
    }
 
    new scalaSci.Matrix(result, true)
    
  }

  
  final def -(that: scalaSci.JBLAS.Mat): scalaSci.JBLAS.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N)  {
       c = 0
       while (c < M)  {
         result(r)(c) = -that(r,c) + value
         c += 1
       }
       r += 1
     }
 
    new scalaSci.JBLAS.Mat(result)
    
  }
  
  final def *(that: scalaSci.Matrix): scalaSci.Matrix = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while  (r < N)  {
       c = 0
       while (c < M)   {
         result(r)(c) = that(r,c) * value
         c += 1
       }
       r += 1
     }
 
    new scalaSci.Matrix(result, true)
    
  }
  

  final def *(that: scalaSci.JBLAS.Mat): scalaSci.JBLAS.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N)  {
        c = 0
        while (c < M)  {
         result(r)(c) = that(r,c) * value
         c += 1
        }
        r += 1
     }
 
    new scalaSci.JBLAS.Mat(result)
    
  }
  
  // division of a RichNumber with a  Matrix. Divides the corresponding number with each of the Matrix's element'
 final def /( that:  scalaSci.Matrix ): scalaSci.Matrix = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.Matrix(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

 final def /( that:  scalaSci.JBLAS.Mat ): scalaSci.JBLAS.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.JBLAS.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }
 
   
  final def +(that: scalaSci.Mat): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r < N)   {
       c = 0
       while (c < M)   {
         result(r)(c) = that(r,c) + value
         c += 1
       }
       r += 1
     }
 
    new scalaSci.Mat(result, true)
    
  }
  
// subtraction of a Mat  from a RichNumber
  final def -(that: scalaSci.Mat): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0 
    while  (r < N)  {
      c = 0
      while (c < M)   {
         result(r)(c) =  value - that(r,c) 
         c += 1
      }
      r += 1
    }
 
    new scalaSci.Mat(result, true)
    
  }

  final def *(that: scalaSci.Mat): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N)  {
       c = 0
       while (c < M)  {
         result(r)(c) = that(r,c) * value
         c += 1
       }
       r += 1
     }
 
    new scalaSci.Mat(result, true)
    
  }
  

  
  // division of a RichNumber with a  Matrix. Divides the corresponding number with each of the Matrix's element'
 final def /( that:  scalaSci.Mat ): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

  

  
 final def +( that: scalaSci.MTJ.MTJBandMat) = { 
  var ret = new scalaSci.MTJ.MTJBandMat(that.N, that.kl, that.ku)
       // form diagonal    
    var n = 0
    while  (n < that.N) {
            ret(n, n) =  that(n, n)+value
            n += 1
    }
       // from lower band
    var rowElem=0 
    n = 0; var lb = 1
    while (n < that.N)  {
      lb = 1
      while (lb < that.kl) {
         rowElem = n+lb
         if (rowElem < that.N)
           ret(rowElem, n) =  that(rowElem, n)+value
         lb += 1
       }
       n += 1
     }
    
    // from upper band
    var colElem=0 
    n = 0
    while( n < that.N)  {
      var ub = 1 
      while (ub  < that.ku) {
         colElem = n+ub
         if (colElem<that.N)
           ret(n, colElem) =  that(n, colElem)+value
         ub += 1
       }
       n += 1
     }
    
       ret
 
  }

  
 final def -( that: scalaSci.MTJ.MTJBandMat) = { 
  var ret = new scalaSci.MTJ.MTJBandMat(that.N, that.kl, that.ku)
       // form diagonal    
     var n = 0  
     while  (n < that.N) {
            ret(n, n) =  -that(n, n)+value
            n += 1
     }
       // from lower band
    var rowElem=0 
    n = 0
    while  (n < that.N)  {
      var lb = 1
      while  (lb < that.kl) {
         rowElem = n+lb
         if (rowElem < that.N)
           ret(rowElem, n) =  -that(rowElem, n)+value
         lb += 1
       }
       n += 1
    }
    
    // from upper band
   var colElem=0 
   n = 0  
   while  (n < that.N)  {
       var ub = 1
       while (ub < that.ku) {
         colElem = n+ub
         if (colElem<that.N)
           ret(n, colElem) =  -that(n, colElem)+value
         ub += 1
       }
       n += 1
   }
       
       ret
 
  }
  
    
 final def *( that: scalaSci.MTJ.MTJBandMat) = { 
  var ret = new scalaSci.MTJ.MTJBandMat(that.N, that.kl, that.ku)
       // form diagonal    
   var n = 0  
   while (n < that.N) {
            ret(n, n) =  that(n, n)*value
            n += 1
   }
       // from lower band
    var rowElem=0 
    n = 0
    while (n < that.N)  {
      var lb  = 1
      while (lb < that.kl) {
         rowElem = n+lb
         if (rowElem < that.N)
           ret(rowElem, n) =  that(rowElem, n)*value
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n < that.N)  {
      var ub = 1
      while (ub < that.ku) {
         colElem = n+ub
         if (colElem<that.N)
           ret(n, colElem) =  that(n, colElem)*value
         ub += 1
       }
       n += 1
    }
       ret
 
  }

  
  
  
  // addition of RichNumber and an EJML.Mat
 final def +( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an EJML.Mat from a RichNumber
 final def -( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // multiplication of a RichNumber with a Mat
 final def *( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) * value
         c += 1
       }
      r += 1
    }
    result
 }

  
  // division of a RichNumber with a  Mat. Divides the corresponding number with each of the Mat's element'
 final def /( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

  // addition of RichNumber and an EJML.BMat
 final def +( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an EJML.BMat from a RichNumber
 final def -( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // multiplication of a RichNumber with a BMat
 final def *( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) * value
         c += 1
       }
      r += 1
    }
    result
 }

  
  // division of a RichNumber with a  BMat. Divides the corresponding number with each of the Mat's element'
 final def /( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

  
  
  
  // addition of RichNumber and an MTJMat
 final def +( that:  scalaSci.MTJ.Mat  ): scalaSci.MTJ.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.MTJ.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an scalaSci.MTJ.Mat  from a RichNumber
 final def -( that:  scalaSci.MTJ.Mat ): scalaSci.MTJ.Mat  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.MTJ.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // multiplication of a RichNumber with an scalaSci.MTJ.Mat 
 final def *( that:  scalaSci.MTJ.Mat ): scalaSci.MTJ.Mat  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.MTJ.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) * value
         c += 1
       }
      r += 1
    }
    result
 }

  
  // division of a RichNumber with a  scalaSci.MTJ.Mat. Divides the corresponding number with each of the Mat's element'
 final def /( that:  scalaSci.MTJ.Mat ): scalaSci.MTJ.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.MTJ.Mat (N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }
  
// Apache Common Maths
  
  // addition of RichNumber and a CommonMaths Mat
 final def +( that:  scalaSci.CommonMaths.Mat  ): scalaSci.CommonMaths.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.CommonMaths.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an scalaSci.CommonMaths.Mat  from a RichNumber
 final def -( that:  scalaSci.CommonMaths.Mat ): scalaSci.CommonMaths.Mat  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.CommonMaths.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + value
         c += 1
       }
      r += 1
    }
    result
 }

  // multiplication of a RichNumber with an scalaSci.CommonMaths.Mat 
 final def *( that:  scalaSci.CommonMaths.Mat ): scalaSci.CommonMaths.Mat  = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.CommonMaths.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) * value
         c += 1
       }
      r += 1
    }
    result
 }

  
  // division of a RichNumber with a  scalaSci.CommonMaths.Mat. Divides the corresponding number with each of the CommonMaths's element'
 final def /( that:  scalaSci.CommonMaths.Mat ): scalaSci.CommonMaths.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new scalaSci.CommonMaths.Mat (N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
        var tmp = that(r, c)
        if (tmp != 0)
            result(r, c) = value /  tmp
        else
            result(r, c) = 0.0
         c += 1
       }
      r += 1
    }
    result
 }

  // define operations for Complex
final def +( that: Complex): Complex = new Complex(this.value+that.re, that.im)  
final def -( that: Complex): Complex = new Complex(this.value-that.re, -that.im)  
final def *( that: Complex): Complex = new Complex(this.value*that.re, this.value*that.im)  

final def  i = Complex(0.0, 1.0)  
}


