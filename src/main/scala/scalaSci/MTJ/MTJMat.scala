
package scalaSci.MTJ

import Jama.jMatrix
import  no.uib.cipr.matrix._ 


import scalaSci.Vec
import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray

import scala.language.postfixOps
// this class wraps the Matrix Toolkits for Java (MTJ) library 
class Mat( Nin: Int,  Min: Int) extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.MTJ.Mat]  {
  
    var Nrows = Nin  // number of rows
    var Ncols = Min  // number of columns
    var dm = new DenseMatrix(Nrows, Ncols)   // an MTJ dense matrix
    
  final def numRows() = Nrows
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols
  final def size() = (Nrows, Ncols)
  
  final def getLibraryMatrixRef() =   dm // the scalaSci.MTJ.Mat class wraps the no.uib.cipr.matrix.DenseMatrix class, thus return simply the data representation
  
   // construct and return a scalaSci Mat from the native MTJ DenseMatrix representation
  final def matFromLibrary() = new Mat(dm) 
  final def matFromLibrary(dm: DenseMatrix) = new Mat(dm)
    
  import Mat._
  
// MTJ DenseMatrix is stored as a one-dimensional linear array, in a column major format,
// i.e. starting with the first column, then the second etc.  
  final def apply(n: Int) = {
      var nr = n/Ncols
      var nc = n - nr*Ncols
      dm.get(nr, nc)
    }
  


// indexes the corresponding Matrix element without updating automatically sizes for fast access
  final def apply(n: Int, m: Int): Double = {
        dm.get(n, m)

 }

  final def getDM = dm    // retrieves the DenseMatrix MTJ representation
  
  final def this(v: Array[Array[Double]]) = {
     this(v.length, v(0).length)
  
    var r = 0; var c = 0
    while (r < Nrows) {
      c = 0
      while (c <  Ncols) {
            dm.set(r, c, v(r)(c))
            c += 1
  }
    r += 1
    }
  }
  
  
  final def this(tuple: (Int,  Int)) = this(tuple._1,  tuple._2) 

  final def this(denseMatr: DenseMatrix) = {
    this(denseMatr.numRows, denseMatr.numColumns)    // simply to call the main constructor
    dm = denseMatr  // keep the correct reference
    Nrows = dm.numRows
    Ncols = dm.numColumns
    
  }

  // construct from an MTJ lower triangular dense matrix
  final def this(lowerTriag: LowerTriangDenseMatrix) = {
    this(lowerTriag.numRows(), lowerTriag.numColumns())
    var N = lowerTriag.numRows()
    dm = new DenseMatrix(N, N)
    var r = 0; var c = 0
    while ( r <  Nrows) {
      c = 0
      while  (c < Ncols)  {
        dm.set(r, c, lowerTriag.get(r,c))
        c += 1
       }
      r += 1
     }
   }
  
  
  // construct from an MTJ uper triangular dense matrix
  final def this(upperTriag: UpperTriangDenseMatrix) = {
    this(upperTriag.numRows(), upperTriag.numColumns())
    var N = upperTriag.numRows()
    dm = new DenseMatrix(N, N)
   var r = 0; var c = 0
   while ( r <  Nrows) {
     c = 0
      while  (c <   Ncols) {
        dm.set(r, c, upperTriag.get(r,c))
        c += 1
      }
      r += 1
   }
  }
  
  final def setValues(v: Array[Array[Double]]) = 
      {
        Nrows = v.length
        Ncols = v(0).length
        dm = new DenseMatrix(Nrows, Ncols)
       var r = 0; var c = 0
       while (r <  Nrows)  {
         c = 0
         while  (c <  Ncols) {
            dm.set(r, c, v(r)(c))
            c += 1
         }
         r += 1
       }
      }

  
  override final def clone() = {
    var dmc = dm.copy()
    new Mat(dmc)
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
        c=0
      while (c < mnNcols) {
        cpMat(r, c) = this(r, c)
        c += 1
      }
      r += 1
    }
    cpMat
    
  }
  
  
    // updating a single element of the Matrix, without  resizing
  final def update(r: Int, c: Int, value: Double) = dm.set(r, c, value)
  

  final def getv() = {
    toDoubleArray
  }
  
  
  final def * (that: Mat): Mat =  {  
    var mulMat = dm.multAdd(1.0, that.dm, new DenseMatrix(this.dm.numRows, that.dm.numColumns)).asInstanceOf[DenseMatrix]
    //var mulMat = dm.mult(that.dm,  new DenseMatrix(this.dm.numRows, that.dm.numColumns)).asInstanceOf[DenseMatrix]
    new Mat(mulMat)
  } 

   
  // cross(point-wise)  product of a Mat with a Mat
final def  cross(that: scalaSci.MTJ.Mat):  scalaSci.MTJ.Mat = {
  var nv = new scalaSci.MTJ.Mat(this.Nrows, this.Ncols )
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols)  {
      nv(i, j) = this(i, j) * that(i, j)
      j += 1
    }
    i += 1
   }
 return nv
}


// dot  product of a Mat with a Mat
final def  dot(that: Mat):  Double = {
  var dotProduct = 0.0
   var i=0; var j=0;
   while (i<Nrows) {
     j=0
    while (j<Ncols)  {
      dotProduct += this(i, j) * that(i, j)
      j += 1
    }
    i += 1
   }
 dotProduct
}


    
  // solve the system using JLAPACK for overdetermine/undetermined cases
  final def solve(b: Mat) =  {
    if (b.numRows() == this.numColumns)  { // direct solve
       var X = new Mat(this.Ncols, b.Ncols)
       new Mat(dm.solve(b.dm, X.dm) )
    }
   else  // overdetermined/underdetermined case
     new Mat(scalaSci.ILapack.DGELS(this toDoubleArray, b.toDoubleArray))
  }
  

  final def \(b: Mat) = solve(b)  
  
  
  // slash or right matrix divide
final def /(B: Mat) = this * B.inv()
  
final def /(B: scalaSci.RichDouble2DArray) = this * B.inv()

final def /(B: Array[Array[Double]]) = this * (new scalaSci.RichDouble2DArray(B)).inv()

final def /(B: scalaSci.RichDouble1DArray) = this *(new scalaSci.RichDouble2DArray(B)).inv()

  
  final def rank() =   scalaSci.math.LinearAlgebra.LinearAlgebra.rank(toDoubleArray)
  
  final def trace() = scalaSci.math.LinearAlgebra.LinearAlgebra.trace(toDoubleArray)
  
  final def cond() = scalaSci.math.LinearAlgebra.LinearAlgebra.cond(toDoubleArray)
  
 
    
final def  det() = {
  var aa = Array.ofDim[Double](this.dm.numRows, this.dm.numColumns)
  var r = 0
  while (r < this.Nrows) {
     var c = 0
     while  (c <  this.Ncols) {
      aa(r)(c) = this(r, c)
      c += 1
      }
    r += 1
  }
  var rdaa = new scalaSci.RichDouble2DArray(aa)
  scalaSci.StaticMaths.det(rdaa)

  }
  
  
  
  final def inv() = {
    var aa = Array.ofDim[Double](this.dm.numRows, this.dm.numColumns)
    var r = 0
    while (r < this.Nrows) {
      var c = 0
      while  (c < this.Ncols) {
        aa(r)(c) = this(r, c)
        c += 1
      }
      r += 1
    }
     var iaa  = new Jama.jMatrix(aa).inverse()
     new scalaSci.MTJ.Mat(iaa.getArray)
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
      while  (m < ncols)  {
        pM(n, m) = pejml(n, m)
        m += 1
       }
       n += 1
    }
    pM
  }
  
final def eig() = {
    // compute the eigenvalue decompostion by calling a convenience method for computing the 
    // complete eigenvalue decomposition of the given matrix
    // allocate an EVD object. This EVD object in turn allocates all the necessary space to
   // perform the eigendecomposition, and to keep the results, i.e. the real and imaginary 
   // parts of the eigenvalues and the left and right eigenvectors
   var evdObj = no.uib.cipr.matrix.EVD.factorize(this.getDM)
  
    (  new scalaSci.RichDouble2DArray(new Mat(evdObj.getRightEigenvectors()).toDoubleArray), new scalaSci.RichDouble1DArray(evdObj.getRealEigenvalues()))
    
  }  
  
  final def svd() = {
    var S  = scalaSci.ILapack.svd(this.toDoubleArray)
  (new scalaSci.RichDouble2DArray(S._1), new scalaSci.RichDouble1DArray(S._2),  new scalaSci.RichDouble2DArray(S._3))
}

  // perform an LU decomposition
final def lu = {
  val LUObj = new no.uib.cipr.matrix.DenseLU(numRows, numColumns)
   
   val luResult = LUObj.factor(this.getDM)   // factorize the matrix
   
   (new Mat(LUObj.getL), new Mat(LUObj.getU))
}  
  
final def LU = lu

  // perform a QR decomposition
 final def qr = {
   val QRObj = new no.uib.cipr.matrix.QR(numRows, numColumns)
   
    val qrResult = QRObj.factor(this.getDM)   // factorize the matrix
   
    (new Mat(QRObj.getQ),  new Mat(QRObj.getR))
 } 
  
 final def QR = qr  


  // perform a QL decomposition
 final def ql = {
   val QLObj = new no.uib.cipr.matrix.QL(numRows, numColumns)
   
    val qlResult = QLObj.factor(this.getDM)   // factorize the matrix
   
    (new Mat(QLObj.getQ), new Mat(QLObj.getL))
 } 
  
 final def QL = ql  
 
  
 
   // perform an RQ decomposition
 final def rq = {
   val RQObj = new no.uib.cipr.matrix.RQ(numRows, numColumns)
   
    val rqResult = RQObj.factor(this.getDM)   // factorize the matrix
   
    (new Mat(RQObj.getQ), new Mat(RQObj.getR))
 } 
  
 final def RQ = rq
 
  
    // perform an LQ decomposition
 final def lq = {
   val LQObj = new no.uib.cipr.matrix.LQ(numRows, numColumns)
   
    val lqResult = LQObj.factor(this.getDM)   // factorize the matrix
   
    (new Mat(LQObj.getQ), new Mat(LQObj.getL))
 } 
  
 final def LQ = lq
 
 
  // Reduced-Row Echelon form
  final def rref() = {
    var xd = this.toDoubleArray
    var exd  = new org.ejml.data.DenseMatrix64F(xd)
    
    var reduced = org.ejml.ops.CommonOps.rref(exd, -1, null)
    new Mat(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }
  
}
   
  object Mat {
    
    
  var mxElemsToDisplay = 10
  
final def setDisplayLength(n: Int) = {mxElemsToDisplay = n}  

  // a conveniency constructor that allows to construct a matrix e.g. as
  //   var x = Mat(3,7) instead of  var x = new Mat(3, 7)
 final def apply(nrows: Int, ncols: Int) = new Mat(nrows, ncols) 

  // convert an MTJ DenseMatrix to a double array
 final def MTJDenseMatrixToDoubleArray(dm: no.uib.cipr.matrix.DenseMatrix )  = {
   val Nr = dm.numRows
   val Nc = dm.numColumns
   val rda = Array.ofDim[Double](Nr, Nc)
   var r = 0; var c = 0
   while (r < Nr) {
     c = 0
     while (c < Nc) {
       rda(r)(c) = dm.get(r, c)
       c += 1
     }
     r += 1
   }
  rda   
 }
  
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
    for (r<-0 until nrows)
      for (c<-0 until ncols)
         {
           sm(r, c) = values(cpos)  // copy value
           cpos += 1
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
   for (r<-0 until rowCnt)
     for (c<-0 until colCnt)
         {
        var cv = values(cpos)
        if (cv == null) cpos+=1
        cv = values(cpos)
        
        var crow = r*nrowsSubm
        var ccol = c*ncolsSubm
              
              cv match {
            case null => 
            case v: scalaSci.scalaSciMatrix[Any] =>
            for ( rs <- 0 until nrowsSubm) 
              for (cs <- 0 until ncolsSubm)
                 nm(crow+rs, ccol+cs) = v(rs, cs)
                 
             case _ => 
             }
                 
         cpos += 1  // next element
         }   
         nm
         }
         else {

     // construct the new Matrix
      var nm = new Mat(rowCnt, colCnt)
   var cpos = 0
   for (r<-0 until rowCnt)
     for (c<-0 until colCnt)
         {
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
      }
         
     nm                         
     }
    }
    
}
