package scalaSci.CommonMaths

import org.apache.commons.math3.linear._

 
import scalaSci.RichDouble1DArray
import scalaSci.Vec


import scala.language.postfixOps

// this class performs a higher level interface to the Apache Common Maths,
// Array2DRowRealMatrix class
class Mat( ar: Array2DRowRealMatrix )  extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.CommonMaths.Mat]   { 
  var Nrows = ar.getDataRef().length
  var Ncols = ar.getDataRef()(0).length
  
  var   v = ar.getDataRef()
  final def getv = ar.getDataRef()
  
  var rm = ar  
  
  final def getLibraryMatrixRef() =  rm // the scalaSci.CommonMaths.Mat class wraps the org.apache.commons.math.linear.Array2DRowRealMatrix  class
  
  final def matFromLibrary = new Mat(rm)
  final def matFromLibrary(ar: Array2DRowRealMatrix) = new Mat(ar.getDataRef())
  
  
  final def numRows() = Nrows
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols
  final def size() = (Nrows, Ncols)
  
  final def this(n: Int, m: Int) = 
     this(new Array2DRowRealMatrix(n, m))
    
 final def this(d: Array[Array[Double]])   = 
   this(new Array2DRowRealMatrix(d, false))  // false means do not copy the array
  
    
// construct a Matrix from a tuple of values corresponding to its size
final def this( tuple: (Int, Int)) = 
   this(tuple._1, tuple._1)  
  
  final def  set(row: Int, column: Int, value: Double) =   {  v(row)(column) = value }

  final def  get(row: Int, column: Int) = v(row)(column)

 
import Mat._


override final def clone() =  {
   val arc =  ar.copy().asInstanceOf[Array2DRowRealMatrix]
   new Mat(arc)
 }
 
final def copy() = {  // same as clone()
    clone()
  }
  
  // copy to a new matrix, perhaps resizing also matrix
  final def copy(newNrows: Int, newNcols: Int)  =  {
    var cpMat = new Mat(newNrows, newNcols)   // create a new Matrix 
    val mnNrows = if (newNrows < Nrows)  newNrows else Nrows
    val mnNcols = if (newNcols < Ncols)   newNcols else Ncols
      // copy the original matrix whithin
    var r = 0; var c = 0
    while (r < mnNrows) {
      c = 0
      while (c < mnNcols) {
        cpMat(r, c) = this(r, c)
        c += 1
      }
      r += 1
    }
    cpMat
    
  }
  

final def apply(n: Int) = {
      var nr = n/Ncols
      var nc = n - nr*Ncols
      v(nr)( nc)
    }
  
  final def   apply(n: Int, m: Int) = v(n)(m)
  
  
    // updating a single element of the Matrix
  final def   update(n: Int, m: Int, value: Double) = { v(n)(m) = value }
  
  
  
  
  final def *(that: Mat) = new Mat(this.rm.multiply(that.rm))
 
  
 final def det() = {
   var aa = Array.ofDim[Double](this.Nrows, this.Ncols)
   var r = 0
    while (r < this.Nrows)  {
      var c = 0
      while (c < this.Ncols) {
      aa(r)(c) = this(r, c)
      c += 1
    }
    r += 1
    }
  var rdaa = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.det(rdaa)
  }
  

  
 final def inv() = {
    var aa = Array.ofDim[Double](this.Nrows, this.Ncols)
    var r = 0;  var c = 0 
    while  (r <  this.Nrows)  {
      c = 0
      while  (c <  this.Ncols) {
        aa(r)(c) = this(r, c)
        c += 1
      }
      r += 1
    }
  var iaa  = new Jama.jMatrix(aa).inverse()
  new scalaSci.CommonMaths.Mat(iaa.getArray)
  }

 final def norm2(): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.norm2( this.toDoubleArray)
}

final def  norm1(): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.norm1( this.toDoubleArray)
}

final def  normF(Mmat: Mat): Double = {
  scalaSci.math.LinearAlgebra.LinearAlgebra.normF(this.toDoubleArray)
}

final def  normInf(Mmat: Mat): Double = {
   scalaSci.math.LinearAlgebra.LinearAlgebra.normInf( this.toDoubleArray)
}
 
  final def trace() =  scalaSci.math.LinearAlgebra.LinearAlgebra.trace(v)
  
  final def rank() =   scalaSci.math.LinearAlgebra.LinearAlgebra.rank(v)
  
  final def cond() =   scalaSci.math.LinearAlgebra.LinearAlgebra.cond(v)
  
  
// solve the system using JLAPACK for overdetermine/undetermined cases
  final def solve(b: Mat) =  {
    if (b.numRows() == this.numColumns)  // direct solve
      {
    var solution = scalaSci.math.LinearAlgebra.LinearAlgebra.solve( this.v, b.v)
    new Mat( solution)
    }     
   else  // overdetermined/underdetermined case
     new Mat(scalaSci.ILapack.DGELS(this toDoubleArray, b.v))
  }
   
  final def \(b: Mat) = solve(b)
  
  
  // slash or right matrix divide
final def /(B: Mat) = this * B.inv()
  
final def /(B: scalaSci.RichDouble2DArray) = this * B.inv()

final def /(B: Array[Array[Double]]) = this * (new scalaSci.RichDouble2DArray(B)).inv()

final def /(B: scalaSci.RichDouble1DArray) = this *(new scalaSci.RichDouble2DArray(B)).inv()

  
  
// Mat * RichDouble2DArray
 override final def * (that: scalaSci.RichDouble2DArray): Mat =  {
   var  rN = this.Nrows;   var rM = this.Ncols;
   var  sN = that.Nrows;  var sM = that.Ncols;
   
   var  v1Colj = new Array[Double](rM)
   var result = new Mat(this.Nrows, that.Ncols)
   var j=0; var k=0;
   while (j < sM)  {
       k=0
      while  (k < rM) {
        v1Colj(k) =that(k, j)
        k += 1
      }

      var i=0;
      while (i<rN) {
        var   Arowi = this.v(i)
        var   s = 0.0;
        k=0
        while (k< rM) {
          s += Arowi(k)*v1Colj(k)
          k += 1
        }
      result(i, j) = s;
      i += 1
      }
 j += 1
   }
  return result
  }
    
//   compute eigenvalues/eigenvectors using MTJ
final def eig() = {
  // construct an MTJ Matrix
  var mtjMat = new scalaSci.MTJ.Mat(this.toDoubleArray)
  mtjMat.eig()
}
  
  final def svd() = {
    var S  = scalaSci.ILapack.svd(this.toDoubleArray)
  (new scalaSci.RichDouble2DArray(S._1), new scalaSci.RichDouble1DArray(S._2),  new scalaSci.RichDouble2DArray(S._3))

  }

// SVD using Apache Common Library  
  final def asvd(ac: Mat) ={
  val rm = ac.getLibraryMatrixRef   // get the native real matrix reference
  val svdObj = new org.apache.commons.math3.linear.SingularValueDecomposition(rm)
	(new Mat(svdObj.getU().getData),  new Mat(svdObj.getS().getData),  new Mat(svdObj.getV().getData))
}

  final def pinv() =  {
    val ejmlM = new scalaSci.EJML.Mat(this.getv)
    val pejml = ejmlM.pinv
    val nrows = pejml.Nrows
    val ncols = pejml.Ncols
    var pM = new Mat(nrows, ncols)
    var n = 0
    while  (n < nrows)  {
      var m = 0
      while  (m <  ncols) {
        pM(n, m) = pejml(n, m)
        m += 1
      }
      n += 1
    }
    
    pM
  }

  
  // Reduced-Row Echelon form
  final def rref() = {
    var xd = this.toDoubleArray
    var exd  = new org.ejml.data.DenseMatrix64F(xd)
    
    var reduced = org.ejml.ops.CommonOps.rref(exd, -1, null)
    new Mat(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }
  
}


object Mat  {
  
  // a conveniency constructor that allows to construct a matrix e.g. as
  //   var x = Mat(3,7) instead of  var x = new Mat(3, 7)
 final def apply(nrows: Int, ncols: Int) = new Mat(nrows, ncols) 
 
    /* e.g.
var xx = 3.4
var a = Mat( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/    

 final def apply(values: Double*)  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   sm = new Mat( nrows, ncols)  // create a Mat
    var r=0; var c=0
    while (r < nrows) {
        c = 0
        while (c < ncols) 
         {
           sm(r, c) = values(cpos)  // copy value
           cpos += 1
           c += 1
         }
        r+=1
       }

    sm  // return the constructed matrix

  }

  /* e.g. 
  var xx = 8.3
  var am = $(xx, 1-xx, cos(xx), null, xx+0.3*xx, 5.6, -3.4)
  */

final def   $( values :Any*) = {
    // count number of nulls, number of nulls will be the number of rows 
    var nullCnt = 0
    for (v <- values)  
       if (v == null) nullCnt+=1

    // count number of columns
     var colCnt = 0
     var vl = values.length
     while (colCnt < vl && values(colCnt) != null)
       colCnt += 1
       
        var rowCnt = nullCnt+1  // number of rows iof the new Matrix
        
        // take the first element.
        // It can be either a Matrix or a double number
        var vv = values(0) 
     if (vv.isInstanceOf[scalaSci.scalaSciMatrix[Any]]) { // we synthesize our Matrix from Matrices
           
           // take parameters of the submatrices
         var vv0 = vv.asInstanceOf[scalaSci.scalaSciMatrix[Any]]
         var nrowsSubm = vv0.numRows()
         var ncolsSubm = vv0.numColumns()
         
     // construct the new Matrix
   var nm = new Mat(rowCnt*nrowsSubm, colCnt*ncolsSubm)
   var cpos = 0
   var r = 0; var c = 0
   while (r < rowCnt)  {
     c = 0
     while (c < colCnt)   {
        var cv = values(cpos)
        if (cv == null) cpos+=1
        cv = values(cpos)
        
        var crow = r*nrowsSubm
        var ccol = c*ncolsSubm
              
              cv match {
            case null => 
            case v: scalaSci.scalaSciMatrix[Any] =>
              var rs = 0; var cs = 0
              while (rs  < nrowsSubm) {
                 cs = 0
                 while (cs < ncolsSubm)  {
                 nm(crow+rs, ccol+cs) = v(rs, cs)
                 cs += 1
                 }
                 rs += 1
              }
                 
             case _ => 
             }
                 
         cpos += 1  // next element
         c += 1
     }
     r += 1
   }
   nm
     
    }
         else {

     // construct the new Matrix
      var nm = new Mat(rowCnt, colCnt)
   var cpos = 0
   var  r = 0; var c = 0
   while (r < rowCnt) {
     c =  0
     while (c < colCnt)  {
        var cv = values(cpos)
        if (cv == null) cpos+=1
        cv = values(cpos)
        cv match {
            case null => 
            case v: Int => nm(r, c) =  v
            case v: Double => nm(r, c) = v
            case _ =>
           }
                     
      cpos += 1
      c += 1
      }
   r += 1
   }
     nm                         
     }
    }
 }    



