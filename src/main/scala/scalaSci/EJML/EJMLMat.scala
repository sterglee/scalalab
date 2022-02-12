
// this class wraps operations on EJML library (http://code.google.com/p/efficient-java-matrix-library/)

package scalaSci.EJML

import java.util._
import no.uib.cipr.matrix.DenseMatrix
import org.ejml.ops._
import org.ejml.simple.SimpleMatrix
import org.ejml.data.DenseMatrix64F
import org.ejml.alg.dense.decomposition.lu.LUDecompositionAlt_D64
import org.ejml.alg.dense.decomposition.chol._
import org.ejml.alg.dense.decomposition.qr._
import org.ejml.alg.dense.linsol.qr._


import scala.language.postfixOps
import scala._
import scala.collection.generic._
import scalaSci.Vec

import scalaExec.Interpreter.GlobalValues
import scalaExec.Interpreter.GlobalValues._

import java.util.stream.DoubleStream


// this class provides Matrix operations, by wrapping the Efficient Java Matrix Library
class Mat(smi: SimpleMatrix )  extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.EJML.Mat]  {
    var  sm = smi   // keep the reference to the SimpleMatrix
    
    
// getters for size
    var   Nrows = smi.numRows()
    var   Ncols = smi.numCols()
    
  
  final def numRows() = Nrows
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols
  final def size() = (Nrows, Ncols)
    
  final var isLargeMatrix = if (Ncols * Nrows > 100) true else false
 
import Mat._

 override final def clone() =  {
   val smc =  sm.copy()   // perform a copy of the SimpleMatrix representation
   new Mat(smc)   // return a new EJML Matrix 
 }
   
  // alias for clone
final def copy() = {
  clone()
}

  // Array[Array[Double]] * Array[Array[Double]]
  final def * (that: Mat): Mat = {
        new Mat(scalaSci.ParallelMult.pmul(this.getArray, that.getArray))
        }

  // Array[Array[Double]] * Array[Array[Double]]
 final def * (that: Array[Array[Double]]): Mat = {
   new Mat(scalaSci.ParallelMult.pmul(this.getArray, that))
  }
   
  override final def * (that: scalaSci.RichDouble2DArray): Mat =  {
     new Mat(scalaSci.ParallelMult.pmul(this.getArray, that.getArray))
    
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
      sm.get(nr, nc)
    }

// auxiliary constructor that makes a matrix of that size
final def this(N: Int, M: Int) = {
     this(new SimpleMatrix(N, M))
   } 

  
// construct a Matrix from a tuple of values corresponding to its size
final def this( tuple: (Int, Int)) = 
   this(tuple._1, tuple._1)  
  
// auxiliary constructor that makes a matrix of that size and inits it
  final def this(N: Int, M: Int,  initV: Double) = {
     this(new SimpleMatrix(N, M))
     var n = 0
     while (n <  N) {
       var m = 0
       while  (m <  M) {
         this(n, m) = initV
         m += 1
       }
       n += 1
     }
   } 

  
 final def this(values: Array[Array[Double]]) = {
  this(new SimpleMatrix(values))
}

final def this(values: Array[Double]) = {
   this(values.size, 1)
   var k = 0
   while  ( k < values.size) {
     this(k, 0) = values(k)
     k += 1
   }
  }
    
  
final def getv() = {
    sm.getMatrix.data
  }

  
 final def maps(f: java.util.function.DoubleUnaryOperator) = {
   var xm = sm.getMatrix.data
   DoubleStream.of(xm: _*).map(f).toArray
 } 
 
final def getLibraryMatrixRef() =   sm   // the scalaSci.EJML.EJMLMat wraps an EJML SimpleMatrix
  
final def matFromLibrary()  = new Mat(sm)
final def matFromLibrary(sm: SimpleMatrix) = new Mat(sm)

// prints the matrix out with the specified precision
final def printEJML(numChar: Int, precision: Int) = { sm.print(numChar, precision) }

// Prints the matrix to standard out given a printf() style floating point format, e.g. print("%f").
final def printEJML( format: String) = { sm.print(format) }

// short print method
final def pEJML( format: String) = { sm.print(format) }

final def pEJML = print

// Extracts a row or column from this matrix and returns it as a row matrix of the appropriate length.
//    @param extractRow is a row vector begin extracted.
//    @param element The row or column the vector is contained in.
//    @return Extracted vector.
// used to implement the apply()
final def extractVector(extractRow: Boolean, element: Int) =
   {
 new Mat( sm.extractVector(extractRow, element))
 }
   

final def isVector =  sm.isVector   //  a 1-D Matrix

final def dot(that: Mat) =  {
    sm.dot(that.sm)
}

  
//   Returns a reference to the matrix that it uses internally.  This is useful
//    when an operation is needed that is not provided by this class.
//      @return Reference to the internal DenseMatrix64F.
final def getMatrix = {
  sm.getMatrix
  }

  
  // cross(point-wise)  product of a Mat with a Mat
final def  cross(that: scalaSci.EJML.Mat):  scalaSci.EJML.Mat = {
  var nv = new scalaSci.EJML.Mat(this.Nrows, this.Ncols )
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

  
final def floor(v: Mat): Mat = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
          j=0
          while (j < Ncols)  {
             om(i, j) = java.lang.Math.floor(v(i, j))
             j += 1
            }
            i += 1
        }
          om
    }



 final def round(v: Mat): Mat = {
   var Nrows = v.Nrows; var Ncols = v.Ncols;
   var om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
         j=0
         while (j < Ncols)  {
             om(i, j) = java.lang.Math.round(v(i, j))
             j += 1
            }
            i += 1
        }
          om
    }


 final def pow(v: Mat, exponent:Double): Mat = {
   val Nrows = v.Nrows; val Ncols = v.Ncols;
   val om = new Mat(Nrows, Ncols)
   var i=0; var j=0;
     while (i < Nrows) {
           j=0
           while (j < Ncols)  {
             om(i, j) = java.lang.Math.pow(v(i, j), exponent)
             j += 1
            }
            i += 1
        }
          om
    }

  



// Extracts the diagonal from this matrix and returns them inside a column vector.
final def  extractDiag = {
  new Mat(sm.extractDiag())
}

//  Checks to see if matrix 'a' is the same as this matrix within the specified  tolerance.
//    @param a The matrix it is being compared against.
//    @param tol How similar they must be to be equals.
//    @return If they are equal within tolerance of each other.
final def  isIdentical( a: Mat, tol: Double) =  {
  sm.isIdentical( a.sm, tol )
}

// Checks to see if any of the elements in this matrix are either NaN or infinite.
//   @return True of an element is NaN or infinite.  False otherwise.
final def  hasUncountable = {
        sm.hasUncountable
   }
          

 


  
//  Copy matrix B into this matrix at location (insertRow, insertCol).
//   @param insertRow First row the matrix is to be inserted into.
//   @param insertCol First column the matrix is to be inserted into.
//   @param B The matrix that is being inserted.
final def insertIntoThis(insertRow: Int, insertCol: Int, B: Mat)  = {
        CommonOps.insert(B.sm.getMatrix(), sm.getMatrix, insertRow,insertCol);
    }




//  Sets the elements in this matrix to be equal to the elements in the passed in matrix.
//   Both matrix must have the same dimension.
//      @param a The matrix whose value this matrix is being set to.
//final def  set( a: Mat  ) = {
// SOSsterg        sm.getMatrix.set(a.sm.getMatrix())
   // }



//  Sets all the elements in this matrix equal to the specified value.
final def set( value: Double ) = {
        CommonOps.fill(sm.getMatrix,value)
    }

//  Sets all the elements in the matrix equal to zero.
/* e.g.
  var  A = M("2.3  4.5; -0.5 1.3")
  A.Zero
  */
 final def  Zero = {
        sm.getMatrix.zero
    }

  
  // NORM-OPS
  
 final def norm2 = {
   NormOps.normP2(sm.getMatrix)
 }
 

  
// Computes either the vector p-norm or the induced matrix p-norm depending on A being
// a vector or a matrix respectively
final def normP(p: Double) = {
   NormOps.normP(sm.getMatrix, p)
}  

  
// The condition p = 2 number of a matrix is used to measure the sensitivity of the linear
// system  Ax=b.  A value near one indicates that it is a well conditioned matrix.
//     @return The condition number.
final def  conditionP2 = {
      NormOps.conditionP2(sm.getMatrix)
    }

final def  conditionP(p: Double) = {
      NormOps.conditionP(sm.getMatrix, p)
    }
/* e.g.
    var  A = M("2.3  4.5; -0.5 1.3")
    var nrm2 = A.norm2
    var nrmP2 = A.normP(2)
    var nrmP3 = A.normP(1) 
    var conditionA = A.conditionP2 
    var traceA = A.trace
    var detA = A.det
 
  */
  
  // Normalizes the matrix such that the Frobenius norm is equal to one
 final def normalizeF = NormOps.normalizeF(sm.getMatrix)

 final def fastNormF = NormOps.fastNormF(sm.getMatrix)
  
 // Computes the p=1 norm. If A is a matrix then the induced norm is computed
final def normP1  = NormOps.normP1(sm.getMatrix) 
  
 // Computes the p=2 norm. If A is a matrix then the induced norm is computed 
final def normP2 = NormOps.normP2(sm.getMatrix) 

  // Computes the p=2 norm. If A is a matrix then the induced norm is computed.
  // This implementation is faster, but more prone to buffer overflow or underflow problems
final def fastNormP2 = NormOps.fastNormP2(sm.getMatrix)  
  
final def normPInf = NormOps.normPInf(sm.getMatrix)  

final def inducedP1 = NormOps.inducedP1(sm.getMatrix)  

final def inducedP2 = NormOps.inducedP2(sm.getMatrix)  

final def inducedPInf = NormOps.inducedPInf(sm.getMatrix)

  
  // COMMON-OPS
  
   //   Solves for X in the following equation:
//      x = a^{-1}  b
//   where 'a' is this matrix and 'b' is an n by p matrix.
//   If the system could not be solved then SingularMatrixException is thrown.  Even
//   if no exception is thrown 'a' could still be singular or nearly singular.
//      @param b n by p matrix. Not modified.
//      @return The solution for 'x' that is n by p.
/*      e.g.  
  var  A = M("2.3  4.5; -0.5 1.3")
  var b = M("2.6; 9.4")
  val x = A.solve(b)
     // verify  the solution
  var df = A*x-b  // should be zero
   
  */
 
  // solve the system using JLAPACK for overdetermine/undetermined cases
  final def solve(b: Mat) =  {
    if (b.numRows() == b.numColumns)  // direct solve
       new Mat(sm.solve(b.sm)) 
   else  // overdetermined/underdetermined case
     new Mat(scalaSci.ILapack.DGELS(this toDoubleArray, b.toDoubleArray))
  } 
    
final def \(b: Mat) =  solve(b)  


  // slash or right matrix divide
final def /(B: Mat) = this * B.inv()
  
final def /(B: scalaSci.RichDouble2DArray) = this * B.inv()

final def /(B: Array[Array[Double]]) = this * (new scalaSci.RichDouble2DArray(B)).inv()

final def /(B: scalaSci.RichDouble1DArray) = this *(new scalaSci.RichDouble2DArray(B)).inv()
  
   //  Returns the transpose of this matrix.
final def  transpose() = {
  var  ret = new SimpleMatrix(sm.getMatrix.numCols, sm.getMatrix.numRows)
  CommonOps.transpose(sm.getMatrix, ret.getMatrix)

 new Mat( ret )
  }



//  Computes the trace of the matrix.
final def  trace() = {
     sm.trace
    }
    
  
  // Computes the determinant of the matrix.
final def det() = {
        sm.determinant
    }

 final def cond() = scalaSci.math.LinearAlgebra.LinearAlgebra.cond(toDoubleArray)
 
 final def rank() =  scalaSci.math.LinearAlgebra.LinearAlgebra.rank(toDoubleArray)
 
// Performs a matrix inversion operation on the specified matrix and stores 
// the results in the same matrix
// If the algorithm could not invert the matrix then false is returned. If it returns true
// that just means the algorithm finished. The results could still be bad because
// the matrix is singular or nearly singular
// Note that results are stored in-place 
final def invertInPlace: Boolean  = {
  CommonOps.invert(sm.getMatrix)
}

final def invert(): scalaSci.EJML.Mat = {
  var result = new scalaSci.EJML.Mat(Nrows, Ncols)
  CommonOps.invert(sm.getMatrix,  result.getMatrix)
  result
}  

final def inv() = { invert() }  
// computes the Moore-Penrose pseudo-inverse
// if one needs to solve a system where m != n, then  solve should be used instead since it
// will produce a more accurate answer faster than using the pinv.
 final def pinv: scalaSci.EJML.Mat = {
   var result = new scalaSci.EJML.Mat(Ncols, Nrows)
   CommonOps.pinv(sm.getMatrix,  result.getMatrix)
   result
} 

     
  
 
  
  // updating a single element of the Matrix, without  resizing
final def update( row: Int, col: Int, value: Double) = {
    sm.set(row, col, value)
    } 

  


  
final def apply( row: Int, col: Int) = {
    sm.get( row, col )
   }
   
 final def  set(row: Int, column: Int, value: Double) =     sm.set(row, column, value)
  
 final def  get(row: Int, column: Int) = sm.get(row, column)



// Computes the Kronecker product between this matrix and the provided B matrix:
//      C = kron(A,B)
//     
//     @param B The right matrix in the operation. Not modified.
//     @return Kronecker product between this matrix and B.
final def   kron( B: Mat ) = {
        var  C = new DenseMatrix64F(sm.getMatrix.numRows*B.sm.numRows(), sm.getMatrix.numCols*B.sm.numCols())
        CommonOps.kron(sm.getMatrix, B.sm.getMatrix, C)

        new Mat(SimpleMatrix.wrap(C))
    }

  final def LU = {
    val dm = sm.getMatrix     // get the DenseMatrix
  
    val lusolver = new LUDecompositionAlt_D64
    lusolver.decompose(dm)
    var lower = new DenseMatrix64F(Nrows, Ncols)
    var upper = new DenseMatrix64F(Nrows, Ncols)
    (new Mat(new SimpleMatrix(lusolver.getLower(lower))),  new Mat(new SimpleMatrix(lusolver.getUpper(upper))))
  }
  
  

 final def CholeskyLDL = {
   val dm = sm.getMatrix     // get the DenseMatrix
  
    val choleskyDecLDL = new CholeskyDecompositionLDL_D64
    choleskyDecLDL.decompose(dm)
    
    val  DChol = choleskyDecLDL.getD(dm)   // Diagonal elements of the diagonal D matrix
    val  LChol = choleskyDecLDL.getL 
    val  VChol = choleskyDecLDL._getVV
    
    (DChol, LChol, VChol)
  }
 
 final def CholeskyBlock = {
    val dm = sm.getMatrix     // get the DenseMatrix
  
    val lower=false
    val choleskyDecBlock = new CholeskyDecompositionBlock_D64(64)
    choleskyDecBlock.decompose(dm)
    
    val  TChol = choleskyDecBlock.getT(dm)
    
    TChol
  }
  
  final def QRHouseholder = QR

  /*
    var N = 100
    var A = scalaSci.EJML.StaticMathsEJML.rand0(N, N)
    var 
   */
  final def QR = {
    val QRDHS = new QRDecompositionHouseholder_D64
    QRDHS.decompose(this.sm.getMatrix)
    var Qp = new DenseMatrix64F(Nrows, Ncols)
    var Rp = new DenseMatrix64F(Nrows, Ncols)
 (new Mat( new SimpleMatrix(QRDHS.getQ(Qp, false))), new Mat( new SimpleMatrix( QRDHS.getR(Rp, false) )))
   }
 

 //* QR decomposition can be used to solve for systems.  However, this is not as computationally efficient
 //  as LU decomposition and costs about 3n<sup>2</sup> flops.
 // It solve for x by first multiplying b by the transpose of Q then solving for the result.
 //    QRx=b
 //     Rx=Q^T b
 final def QRHouseholderSolve(B: scalaSci.EJML.Mat) = {
     val  A = sm.getMatrix  // get the DenseMatrix
    
     val  QRHHSolver = new LinearSolverQrHouse_D64
     QRHHSolver.setA(A)
     var X = new DenseMatrix64F(Nrows, Ncols)
     
     QRHHSolver.solve(B.sm.getMatrix, X) 
     
    new Mat(new SimpleMatrix(X))
  }
  
  final def eigEJML() = {
   val  ejmlEigs = this.sm.eig()
   val numEvals = ejmlEigs.getNumberOfEigenvalues  // get number of eigenvalues
   var eigenVec = ejmlEigs.getEigenVector(0).getMatrix
  val eigenVecDim = eigenVec.numRows   // dimension of eigenvectors
  var  V = Array.ofDim[Double](numEvals, eigenVecDim)   // construct the matrix to fill the eigenvectors
  var D = new Array[Double](numEvals)
  var ev = 0
  while  (ev < numEvals)   {
    D(ev) = ejmlEigs.getEigenvalue(ev).real
    eigenVec = ejmlEigs.getEigenVector(ev).getMatrix
    var k = 0
    while (k <  eigenVecDim)  {
       V(ev)( k) = eigenVec.get(k, 0)
       k += 1
    }
   ev += 1
    }
  
  (new scalaSci.RichDouble2DArray(V), new scalaSci.RichDouble1DArray(D)) 
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

  // Reduced-Row Echelon form
  final def rref() = {
    var xd = this.toDoubleArray
    var exd  = new org.ejml.data.DenseMatrix64F(xd)
    
    var reduced = org.ejml.ops.CommonOps.rref(exd, -1, null)
    new Mat(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }

  
}

  
object Mat  {
  
  import Mat._
  
 
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
