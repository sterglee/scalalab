

package scalaSci.JBLAS

/*
 import scalaSci.JBLAS.Mat
 import scalaSci.JBLAS.Mat._
 var a0 = ones0(1000, 1100)
 var a1 = ones0(1100, 1200)
 
 tic
 var am = a0*a1
 var tmNative = toc
 
 */
import org.jblas.DoubleMatrix._ 
import org.jblas.DoubleMatrix
import org.jblas.ComplexDoubleMatrix

import scalaSci.Vec
import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray

import scala.language.postfixOps
// implements a high-level interface to the fast JBLAS library
class Mat(var da: Array[Array[Double]])  extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.JBLAS.Mat]  { 
    
    var Nrows = da.length  // number of rows
    var Ncols = da(0).length  // numberf of columns

    override final def size = (Nrows, Ncols) 
    override final def length() = Nrows*Ncols
    
  var dm = new DoubleMatrix(da)   // the JBLAS DoubleMatrix that will keep the contents of the double [][] array

  final def getLibraryMatrixRef() =   dm // the scalaSci.JBLAS.Mat class wraps the org.jblas.DoubleMatrix  class, thus return simply the data representation

  final def matFromLibrary() = new Mat(dm)
  final def matFromLibrary(dm: DoubleMatrix) = new Mat(dm)
  
  // returns a copy Array [Array[Double]] for the contents of the matrix
   final def getv = {
     var da = Array.ofDim[Double] (dm.rows, dm.columns)
     var r = 0; var c = 0
     while (r<dm.rows) {
       c = 0
       while (c<dm.columns) {
         da(r)(c) = dm.get(r, c)
         c += 1
         }
         r += 1
         }
         da
         }
         
     
  override final def toDoubleArray  = getv
    
  override final def  numRows()  = Nrows
  override final def  numColumns() =  Ncols
    
    final def this(nrows: Int, ncols: Int) = {
      this(Array.ofDim[Double](nrows, ncols))
    }
    
  
// construct a Matrix from a tuple of values corresponding to its size
final def this( tuple: (Int, Int)) = 
   this(tuple._1, tuple._1)  

final def this(a: DoubleMatrix,  cpFlag: Boolean)   = {
  this(1, 1)   // we avoid creating large DoubleMatrix for efficiency
  dm = a // keep the reference
  Nrows = dm.rows
  Ncols = dm.columns
}
    
 final def this(a: DoubleMatrix) = {
   this(a.rows, a.columns)
   var r = 0
   while  (r <  a.rows)  {
      var c = 0
      while (c < a.columns) {
        this(r, c) = a.get(r,c)
        c += 1
      }
      r += 1
   }
      dm = a
 } 
 
  // clone the matrix
  override final def clone() = {
    var  dac = Array.ofDim[Double](Nrows, Ncols)
    var r = 0
    while  (r <  Nrows)  {
      var c = 0
      while  (c < Ncols) {
        dac(r)(c) = da(r)(c)
        c += 1
      }
      r += 1
    }
    new Mat(dac)
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
  
  

final def apply(row: Int, col: Int) =  {
    dm.get(row, col)
  }
  
  
  // updating a single element of the Matrix, without  resizing
  final def update(row: Int, col: Int, v: Double) = {
    dm.put(row, col, v)  
  }
  

  
  final def * (that: Mat) = {
     val dmr =  dm.mmul(that.dm)
     new Mat(dmr)
   }
   
  
  // return the sum of the elements of the matrix
  final def  sumAll() = {
    dm.sum()
  }
  
  // return the product of the elements of the matrix
  final def  prodAll() = {
    dm.prod()
  }
  
  // return the mean of the elements of the matrix
  final def  meanAll() = {
    dm.mean()
  }
  
  
  // return the maximal element of the matrix
  final def  maxAll() = {
    dm.max()
  }
 
  // return the minimal element of the matrix
  final def minAll() = {
    dm.min()
  }
  
  
    
 /**
     * Returns the linear index of the maximal element of the matrix. If
     * there are more than one elements with this value, the first one
     * is returned.
     */
 final def argmax() = {
   dm.argmax()
 }

 /**
     * Returns the linear index of the minimal element of the matrix. If
     * there are more than one elements with this value, the first one
     * is returned.
     */
 final def argmin() = {
   dm.argmin()
 }
  
  
   
  // cross(point-wise)  product of a Mat with a Mat
final def  cross(that: scalaSci.JBLAS.Mat):  scalaSci.JBLAS.Mat = {
  var nv = new scalaSci.JBLAS.Mat(this.Nrows, this.Ncols )
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
    if (b.numRows() == b.numColumns)  // direct solve
    {
      val ipiv = new Array[Int](this.numRows)
    val x = org.jblas.SimpleBlas.gesv(dm, ipiv,  b.dm)
    new Mat(x).toDoubleArray
   }
      else  // overdetermined/underdetermined case
     scalaSci.ILapack.DGELS(this toDoubleArray, b.toDoubleArray)
  }
  
  
  final def \(b: Mat) =  solve(b)
    
 
  // slash or right matrix divide
final def /(B: Mat) = this * B.inv()
  
final def /(B: scalaSci.RichDouble2DArray) = this * B.inv()

final def /(B: Array[Array[Double]]) = this * (new scalaSci.RichDouble2DArray(B)).inv()

final def /(B: scalaSci.RichDouble1DArray) = this *(new scalaSci.RichDouble2DArray(B)).inv()
 
  
 final def  det() =   scalaSci.math.LinearAlgebra.LinearAlgebra.det(da)
 
 final def trace() = scalaSci.math.LinearAlgebra.LinearAlgebra.trace(da)

 final def inv() = new Mat(scalaSci.math.LinearAlgebra.LinearAlgebra.inverse(da)) 
 
 
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
  
 final def cond() = scalaSci.math.LinearAlgebra.LinearAlgebra.cond(da)
 
 final def rank() =  scalaSci.math.LinearAlgebra.LinearAlgebra.rank(da)
 
 //   compute eigenvalues/eigenvectors using MTJ
final def eig() = {
// construct an MTJ Matrix
  var mtjMat = new scalaSci.MTJ.Mat(this.toDoubleArray)
  mtjMat.eig()
}

  // compute eigenvalues using JBLAS
final def jblaseigenvalues() = {
  var dmMat = org.jblas.Eigen.eigenvalues(this.dm)
  dmMat
  
}

  // compute eigenvectors using JBLAS
final def jblaseigenvectors() = {
  var dmMat = org.jblas.Eigen.eigenvectors(this.dm)
  dmMat
}  
  

  
  final def svd() = {
    var S  = scalaSci.ILapack.svd(this.toDoubleArray)
  (new scalaSci.RichDouble2DArray(S._1), new scalaSci.RichDouble1DArray(S._2),  new scalaSci.RichDouble2DArray(S._3))
}
  // compute the eigenvalues/eigenvectors using JBLAS
  final def feig() = {
     var eigVals = org.jblas.Eigen.eigenvalues(dm)  // eigenvalues
     var eigVecs = org.jblas.Eigen.eigenvectors(dm)   // eigenvectors
     
     (eigVals, eigVecs)
     }
     
 // compute the  eigenvalues using JBLAS
 final def feigenvalues() = {
   org.jblas.Eigen.eigenvalues(dm)
   }
   
  // compute the eigenvectors using JBLAS
  final def feigenvectors() = {
    org.jblas.Eigen.eigenvectors(dm)
    }
    
  // compute the symmetric eigenvalues using JBLAS
  final def fsymmetricEigenvalues() = {
    org.jblas.Eigen.symmetricEigenvalues(dm)
    }
    
  // compute the symmetric eigenvectors using JBLAS
  final def fsymmetricEigenvectors() = {
    org.jblas.Eigen.symmetricEigenvectors(dm)
    }
    
 //  Computes generalized eigenvalues of the problem A x = L B x.
// @param A symmetric Matrix A. Only the upper triangle will be considered. Refers to this
//  @param B symmetric Matrix B. Only the upper triangle will be considered.
//  @return a vector of eigenvalues L.
  final def fsymmetricGeneralizedEigenvalues(B: Array[Array[Double]]) = {
    org.jblas.Eigen.symmetricGeneralizedEigenvalues(this.dm, new DoubleMatrix(B))
    }
    
  
   /**
     * Solve a general problem A x = L B x.
     *
     * @param A symmetric matrix A, refers to this
     * @param B symmetric matrix B
     * @return an array of matrices of length two. The first one is an array of the eigenvectors X
     *         The second one is A vector containing the corresponding eigenvalues L.
     */
final def fsymmetricGeneralizedEigenvectors(B: Array[Array[Double]]) = {
  org.jblas.Eigen.symmetricGeneralizedEigenvectors(this.dm, new DoubleMatrix(B))
  }
  
    /**
     * Compute Cholesky decomposition of A ( this )
     * @param A should be symmetric, positive definite matrix (only upper half is used)
     * @return upper triangular matrix U such that  A = U' * U
     */
 final def fcholesky() = {
   org.jblas.Decompose.cholesky(this.dm)
   }
   
  
/** Solves the linear equation A*X = B , A is this */
final def fsolve(B: Array[Array[Double]]) = {
  org.jblas.Solve.solve(this.dm, new DoubleMatrix(B))
  }
  
  
/** Solves the linear equation A*X = B for symmetric A, A is this */
final def fsolveSymmetric(B: Array[Array[Double]]) = {
  org.jblas.Solve.solveSymmetric(this.dm, new DoubleMatrix(B))
  }
  
/** Solves the linear equation A*X = B for symmetric and positive definite A */
final def fsolvePositive(B: Array[Double]) = {
  org.jblas.Solve.solvePositive(this.dm, new DoubleMatrix(B))
  }
  
  
 /**
     * Compute a singular-value decomposition of A, A is this
     *
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
final def ffullSVD() = {
  org.jblas.Singular.fullSVD(this.dm)
  }
  
     /**
     * Compute a singular-value decomposition of A (sparse variant), A is this
     * Sparse means that the matrices U and V are not square but
     * only have as many columns (or rows) as possible.
     * 
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
final def fsparseSVD() = {
  org.jblas.Singular.sparseSVD(this.dm)
  }
  
final def fsparseSVD( Aimag: Array[Array[Double]]) =  {
  org.jblas.Singular.sparseSVD(new org.jblas.ComplexDoubleMatrix(this.dm, new DoubleMatrix(Aimag)))
  }
  
  
  /**
     * Compute the singular values of a matrix.
     *
     * @param A DoubleMatrix of dimension m * n, A is this
     * @return A min(m, n) vector of singular values.
     */
final def fSPDValues() = {
  org.jblas.Singular.SVDValues(this.dm)
  }
  
  
    /**
     * Compute the singular values of a complex matrix.
     *
     * @param Areal, Aimag :Areal is this,  the real and imaginary components of a  ComplexDoubleMatrix of dimension m * n
     * @return A real-valued (!) min(m, n) vector of singular values.
     */
 final def fSPDValues(B: Array[Array[Double]]) = {
   org.jblas.Singular.SVDValues(new ComplexDoubleMatrix(this.dm, new DoubleMatrix(B)))
          }
  
 
  final def pinv() =  {
    val ejmlM = new scalaSci.EJML.Mat(this.getv)
    val pejml = ejmlM.pinv
    val nrows = pejml.Nrows
    val ncols = pejml.Ncols
    var pM = new Mat(nrows, ncols)
    for (n<-0 until nrows)
      for (m<-0 until ncols)
        pM(n, m) = pejml(n, m)
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
      


object Mat {  
   

 var mxElemsToDisplay = 6
 var mxToStringElems = mxElemsToDisplay

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
    



