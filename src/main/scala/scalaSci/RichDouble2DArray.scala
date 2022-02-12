
//  The RichDouble2DArray type serves for implicitly converted to that type when
//   certain operations cannot be performed 
//  Also, it provides an extensive set of operations.
 
package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray
import java.util._
import Jama._


import scala.language.postfixOps
// for the fast JBLAS based routines
import org.jblas.DoubleMatrix._ 
import org.jblas.DoubleMatrix
import org.jblas.ComplexDoubleMatrix



import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

import scala.concurrent._

import edu.emory.mathcs.utils.ConcurrencyUtils


import java.util.function.DoubleUnaryOperator
import java.util.function.UnaryOperator
import java.util.stream.DoubleStream

import scalaExec.Interpreter.GlobalValues
import scalaExec.Interpreter.GlobalValues._

import cern.colt.matrix._
import cern.colt.matrix.tdouble._
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D


// by default RichDouble2DArray is initialized with the Array[Array[Double]] that it wraps
class RichDouble2DArray(final var  v: Array[Array[Double]]) extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.RichDouble2DArray]  {
  
  import scalaSci.RichDouble2DArray._
  
  final val rdObj = this
  
  final var  Nrows  = v.length  // number of rows
  final var  Ncols =  v(0).length  // number of columns
  final var isLargeMatrix = if (Ncols * Nrows > 100) true else false
  final def numRows() = Nrows  
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols  // the total number of elements of the array
  final def size() = (Nrows, Ncols)  // the array size as a tuple
    
  final def  getv() = v    // return the data array
   
  final def getLibraryMatrixRef() =   v // the scalaSci.RichDouble2DArray does not wrap a Matrix class of a specific library, thus return simply the data representation

  override final def toDoubleArray = v

// we can change the native representation with whatever operation and with this call we can take an updated object   
  final def matFromLibrary() = new RichDouble2DArray(v)
  final def matFromLibrary(v: Array[Array[Double]]) = new RichDouble2DArray(v)
    
  
  // compares for value equality
   override final def equals(other: Any): Boolean = other match { 
     case that: Array[Array[Double]] =>   // compare with an Array[Array[Double]]
       var thatd = new RichDouble2DArray(that)
       equals(thatd)
     case that: RichDouble2DArray => 
         var smDiffs = 0.0
         
         var r = 0; var c = 0
         var diff = 0.0
         var otherRD = other.asInstanceOf[RichDouble2DArray]
         while (r < Nrows) {
           c = 0
           while (c < Ncols) {
           diff = this(r, c)-otherRD(r, c)
           smDiffs += diff*diff
           if (smDiffs > 0.0)  return false
          c += 1         
         }
         r += 1
         }
         if (smDiffs > 0.0) false
         else true
         
      case _ => false 

 }

  final def this(nrows: Int, ncols: Int) = {
    this( Array.ofDim[Double](nrows, ncols))
  }
    
  final def this( tuple: (Int, Int)) =  this(tuple._1, tuple._2)
    
    // construct from an Array[Double]. It constructs a column  vector (i.e. the first column is the Array[Double]
  final def this(da: Array[Double]) = {
    this(da.length, 1)
    var k = 0
    while (k<da.length) {
      this(k, 0) = da(k)
      k += 1
    }
  }
  
  //   construct from a RichDouble1DArray. It constructs a column vector (i.e. the first column is an Array[Double]
  final def this(da: RichDouble1DArray) = {
    this(da.length, 1)
    var k = 0
    while (k<da.length) {
      this(k, 0) = da(k)
      k += 1
    }
  }

  
  // following are consructors from all ScalaLab's matrix types to RichDouble2DArray
  
  // constructor for JAMA based matrix
  final def this(jamaMat: scalaSci.Mat) = {
    this(Array.ofDim[Double](jamaMat.numRows, jamaMat.numColumns))
    val Nrows = jamaMat.numRows()
    val Ncols = jamaMat.numColumns()
  var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
        v(r)(c) = jamaMat(r, c)
        c += 1
     }
     r += 1
   }
  }
  
  // constructor for 1-indexed matrix
  final def this(numalMat: scalaSci.Matrix) = {
    this(Array.ofDim[Double](numalMat.numRows, numalMat.numColumns))
    val Nrows = numalMat.numRows
    val Ncols = numalMat.numColumns
      var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
    v(r)(c) = numalMat(r+1, c+1)
    c += 1
     }
     r += 1
   }
  }
  
  // constructor for JBLAS matrix
  final def this(jblasMat: scalaSci.JBLAS.Mat) = {
    this(Array.ofDim[Double](jblasMat.numRows(), jblasMat.numColumns()))
    val Nrows = jblasMat.numRows
    val Ncols = jblasMat.numColumns
     var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
       v(r)(c) = jblasMat(r, c)
       c += 1
     }
     r += 1
    }
  }
  
  // constructor for EJML matrix
  final def this(ejmlMat: scalaSci.EJML.Mat) = {
    this(Array.ofDim[Double](ejmlMat.numRows, ejmlMat.numColumns))
    val Nrows = ejmlMat.numRows
    val Ncols = ejmlMat.numColumns
      var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
          v(r)(c) = ejmlMat(r, c)
          c += 1
     }
     r += 1
  }
  }
  
  // constructor for MTJ matrix
  final def this(mtjMat: scalaSci.MTJ.Mat) = {
    this(Array.ofDim[Double](mtjMat.numRows, mtjMat.numColumns))
    val Nrows = mtjMat.numRows
    val Ncols = mtjMat.numColumns
      var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
        v(r)(c) = mtjMat(r, c)
        c += 1
     }
     r += 1
  }
        
  }
  
  // constructor for Apache Common Maths
  final def this(commonsMat: scalaSci.CommonMaths.Mat) = {
    this(Array.ofDim[Double](commonsMat.numRows, commonsMat.numColumns))
    val Nrows = commonsMat.numRows
    val Ncols = commonsMat.numColumns
      var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
        v(r)(c) = commonsMat(r, c)
        c += 1
     }
     r += 1
  }
  
  }

    // indexes the corresponding Matrix element without updating automatically sizes for fast access
@inline
final def apply(n: Int, m: Int): Double = {
       v(n)(m)
}

override final def clone() = {
  var a =  Array.ofDim[Double](Nrows, Ncols)
  var r = 0
  while (r < Nrows) {
     var c = 0
     while (c < Ncols) {
        a(r)(c) = v(r)(c)
        c += 1
     }
      r += 1
  }
  new RichDouble2DArray(a)
}  
  
  final def copy() = {  // same as clone()
    clone()
  }
  
  
  // copy to a new matrix, perhaps resizing also matrix
  final def copy(newNrows: Int, newNcols: Int)  =  {
    var cpMat = new RichDouble2DArray(newNrows, newNcols)   // create a new Matrix 
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
  
  
final def Size() = {
  (v.length, v(0).length)
}


// Override operations for efficiency

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ::) corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummySymbol). if low>high then rows are returned in reverse
 override final def apply(rowL: Int, rowH: Int, allColsSymbol: ::.type) = {
   var rowStart = rowL; var rowEnd=rowH;
   var colStart = 0;     var colEnd =  Ncols-1;   // all columns
   var colNum = Ncols
   var colInc = 1

if (rowStart <= rowEnd) {   // positive increment
    var rowInc = 1
    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    var rowNum = rowEnd-rowStart+1
    var  subMatr =  new RichDouble2DArray(rowNum, colNum)  // create a Mat to keep the extracted range
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
subMatr// return the submatrix

} // rowStart <= rowEnd
else { // rowStart > rowEnd
    var rowInc = -1
    var rowNum = rowStart-rowEnd+1
    var subMatr = new RichDouble2DArray(rowNum, colNum)  // create a Mat to keep the extracted range
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

subMatr // return the submatrix

} // rowStart > rowEnd

}

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ::) corresponds to Matlab's m(2:4:12, :)'
override final def apply(rowL: Int, rowInc: Int, rowH: Int, allColsSymbol: ::.type) = {
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
    var subMatr = new RichDouble2DArray(rowNum, colNum)     // create a Matrix to keep the extracted range
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
     subMatr // return the submatrix
     }  // positive increment
  else  {  //  negative increment
     var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
     var subMatr = new RichDouble2DArray(rowNum, colNum)  // create a Matrix to keep the extracted range
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
       subMatr  // return the submatrix
     }  // negative increment
}



// extracts a submatrix, e.g. m( ::, 2,  12 ) corresponds to Matlab's m(:, 2:12)'
  override final def apply(allRowsSymbol:  ::.type,  colLow: Int,  colHigh: Int)  = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = 1
    var colInc = 1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = colEnd-colStart+1
        var subMatr = new RichDouble2DArray(rowNum, colNum)   // create a Matrix to keep the extracted range
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
 subMatr
} // positive increment
  else {  // negative increment
    var colNum = colStart-colEnd+1
    var subMatr = new RichDouble2DArray(rowNum, colNum)  // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
    colInc = -1
    
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
 subMatr  // return the submatrix
    }
    
   }



// extracts a submatrix, e.g. m( ::, 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  override final def apply(allRowsSymbol: ::.type, colLow: Int, colInc: Int, colHigh: Int) = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr =new RichDouble2DArray(rowNum, colNum)  // create a Matrix to keep the extracted range
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
 subMatr
} // positive increment
  else {  // negative increment
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = new RichDouble2DArray(rowNum, colNum)  // create a Matrix to keep the extracted range
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
 subMatr   // return the submatrix
   }
   }


// extracts a submatrix, e.g. m( 2, 3, 12, 4, 2,  8 ) corresponds to Matlab's m(2:3:12, 4:2:8)'
  override final def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int) = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new RichDouble2DArray(rowNum, colNum)     // create a Matrix to keep the extracted range

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
 subMatr
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
 subMatr   // return the submatrix
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
 subMatr // return the submatrix
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
 subMatr // return the submatrix
    }
  }


// extracts a specific row, take all columns, e.g. m(2, ::) corresponds to Matlab's m(2, :)'
  
// extracts a specific row, take all columns, e.g. m(2, ::) corresponds to Matlab's m(2, :)'
override  final def apply(row: Int, allColsSymbol: ::.type): RichDouble1DArray = {
    var subMatr = new Array[Double](Ncols)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var ccol = 0
    while  (ccol <  Ncols)   {
          subMatr(ccol) = this(row, ccol)
          ccol += 1
         }

     new RichDouble1DArray(subMatr)
}


// extracts a specific column, take all rows, e.g. m(::, 2) corresponds to Matlab's m(:,2:)'
  override final def apply(allRowsSymbol: ::.type, col: Int): RichDouble1DArray = {
    var subMatr = new Array[Double](Nrows)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = 0
    while  (crow < Nrows)   {
          subMatr(crow) = this(crow,  col)
          crow += 1
         }

     new RichDouble1DArray(subMatr)
}




// extracts a submatrix, e.g. m( 2,  12, 4,   8 ) corresponds to Matlab's m(2:12, 4:8)'
  override final def apply(rowLow: Int,  rowHigh: Int, colLow: Int, colHigh: Int) = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = if (rowHigh > rowLow) 1 else -1
    var colInc = if (colHigh > colLow) 1 else -1

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new RichDouble2DArray(rowNum, colNum)        // create a Matrix to keep the extracted range

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
 subMatr
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
 subMatr // return the submatrix
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
 subMatr   // return the submatrix
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
 subMatr   // return the submatrix
   }

   }


              // extracts a submatrix, e.g. m(3:2:7, :)
  override final def apply(rowLow: Int, rowInc: Int, rowHigh: Int) = {
    var rowStart = rowLow;     var rowEnd =  rowHigh;    if (rowEnd < rowStart) { rowStart = rowHigh; rowEnd = rowLow; }
    var colStart = 1;     var colEnd =  Ncols-1;
    var colInc = 1
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = new RichDouble2DArray(rowNum, colNum)     // create a Matrix to keep the extracted range
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
     subMatr
}

  

    
 
/* extract the columns specified with indices specified with  the array colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 The columns at the new matrix are arranged in the order specified with the array colIndices
 e.g. 
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(3, 1)
 var extract3_1cols = testMat(::, colIndices)
   */
override   final def apply(allRowsSymbol: ::.type, colIndices: Array[Int])  = {
    var lv = colIndices.length
    if (lv > Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols)
        new RichDouble2DArray(1, 1)     
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  colFiltered =  new RichDouble2DArray(Nrows, lv)     
      
      var col = 0
      while (col < lv)  {
           var currentColumn = colIndices(col)  // the specified column
           var row = 0
           while  (row < Nrows) {  // copy the corresponding row
               colFiltered(row, col) = this(row, currentColumn)
               row += 1
           }
      col += 1 
      }  
    
      colFiltered   // return the column filtered array
    } // dimension of array with column indices to use is correct
  }
  


  /* extract the rows specified with indices specified with  the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 The rows at the new matrix are arranged in the order specified with the array rowIndices
 e.g. 
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 var rowIndices = Array(3, 1)
 var extract3_1rows = testMat(rowIndices, ::)
   */
   
  override final def apply(rowIndices: Array[Int], allColsSymbol: ::.type) = {
    var lv = rowIndices.length
    if (lv > Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows)
        new RichDouble2DArray(1, 1)     
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  rowFiltered =  new RichDouble2DArray(lv, Ncols)     
      var row = 0
      while (row <  lv)  {
           var currentRow = rowIndices(row)  // the specified row
           var col = 0
           while  (col < Ncols)  {  // copy the corresponding row
               rowFiltered(row, col) = this(currentRow, col)
               col += 1
             }
           row += 1  
       }  
    
      rowFiltered// return the column filtered array
   } // dimension of array with column indices to use is correct
  }
  
  
  
  
/* extract the columns specified with true values at the array  colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 e.g. 
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(true, false, true, false)
 var extract0_2cols = testMat(::, colIndices)
   */
  override final def apply(allRowsSymbol: ::.type, colIndices: Array[Boolean])  = {
    var lv = colIndices.length
    if (lv != Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols)
        new RichDouble2DArray(1, 1)     
        
      }
      else {  // dimension of array with column indices to use is correct
        // count the number of trues
        var ntrues = 0
        var k = 0
        while ( k <  Ncols) {
          if (colIndices(k)==true)  
            ntrues += 1
          k += 1
        }
        
      // allocate array
      var  colFiltered = new RichDouble2DArray(Nrows, ntrues)     
        
      var currentColumn=0
      var col = 0
      while (col < Ncols)  {
         if (colIndices(col))   { // copy the corresponding column
           var row = 0   
           while  (row < Nrows)  {
               colFiltered(row, currentColumn) = this(row, col)
               row += 1
            }
             currentColumn += 1
         }  // copy the corresponding column
         col += 1
      }        
    
      colFiltered    // return the column filtered array
      
      } // dimension of array with column indices to use is correct
  }
  
  
    
/* extract the rows specified with true values at the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 e.g. 
 var testMat = $$( 1.0, 2.0, 3.0, null, 5.0, 6.0, 7.0, null, 8, 9, 10, null,  11, 12, 13)
 var rowIndices = Array(false, true, false, true)
 var extract1_3rows = testMat(rowIndices, ::)
   */
  override final def apply(rowIndices: Array[Boolean], allCols: ::.type ) = {
    var lv = rowIndices.length
    if (lv != Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows)
        new RichDouble2DArray(1, 1)     
        
      }
      else {  // dimension of array with row indices to use is correct
        // count the number of trues
        var ntrues = 0
        var k = 0
        while (k < Nrows) {
          if (rowIndices(k))  
            ntrues += 1
          k += 1
        }
        
      // allocate array
      var  rowFiltered =  new RichDouble2DArray( ntrues, Ncols)    
        
      var currentRow=0
      var row = 0 
      while  (row <  Nrows)   {  // all rows
          if (rowIndices(row))  {  // copy the corresponding row
            var  col = 0
            while  (col < Ncols) {
               rowFiltered(currentRow, col) = this(row, col)
               col += 1
                }
             currentRow += 1
          }
          row += 1
        }  // all rows
        rowFiltered
      }  // dimension of array with row indices to use is correct 
          
    }
    
  
  // the common functionality  for the matrix assignment operations of all zero-indexed scalaSci matrix types
  // is defined with the following update() methods  
  
  // update a single row with index r to have the value v
  // e.g. val x = rand(3, 4);  x(-1, ::) = 20   // set the last row to 20s
override final def update(r: Int, colonSymbol:  ::.type, v: Double)   {
   val  row  =  if ( r < 0)  Nrows+r else r   // allow negative indices to take higher numbered rows
   var i = 0
   while (i < Ncols) {
     this(row, i) = v
     i += 1
   }
 }
  
  
  // update a single column with index c  to have the value v
  // e.g.  val x = rand(3, 4);  x(::, -1) = 30    // set the last column to 30s
 override final def update( colonSymbol:  ::.type,  c: Int, v: Double)   {
   val  col  =  if ( c < 0)  Ncols+c else c   // allow negative idices to take higher numbered columns
   var i = 0
   while (i < Nrows) {
     this(i, col) = v
     i += 1
   }
 }
  
  
  
    // update a matrix range to have the value v
  // e.g.  val x = rand(12, 14);  x(1, 3, 2, 6) = 30    // set the last column to 30s
  override final def update( rs: Int,  re: Int, cs: Int, ce: Int, v: Double)   {
   var r = rs; var c = cs
     while (r <= re) {
      c = cs
      while (c <= ce) {
        this(r, c) = v
        c += 1
     }
     r += 1
   }
}

  // update the column c with the contents of the vector v
  // e.g.  val x = rand(4, 5); val v = vones(4); x(::, 2) = v 
 override  final def update(colonSymbol: ::.type, c: Int, v: Vec)  {
  var  vl = v.length
    if (vl > Nrows)  // use a part of the vector only to fill the rows
      vl = Nrows
      val col = if (c < 0) Ncols + c  else c
   var row = 0
   while (row < vl)  {
     this(row, col) = v(row)
     row += 1
   }
 }
 
  // update the row r with the contents of the vector v
  // e.g.  val x = rand(4, 5);  val v = vones(5);  x(3,  ::) = v
 override final def update(r: Int, colonSymbol: ::.type, v: Vec)  {
   var vl = v.length
    if (vl > Ncols)  // use a part of the vector only to fill the columns
     vl = Ncols
   val row = if (r < 0) Nrows + r else r
   var col = 0
   while (col < vl) {
     this(row, col) = v(col)
     col += 1
   }
 }
 
  
  // update the column c with the contents of the array  v
  // e.g.  val x = rand(4, 5); val v = vones(4); x(::, 2) = v 
 override  final def update(colonSymbol: ::.type, c: Int, v: Array[Double])  {
  var  vl = v.length
    if (vl > Nrows) // use a part of the array only to fill the rows
      vl = Nrows
    
   val col = if (c < 0) Ncols + c  else c
   var row = 0
   while (row < vl)  {
     this(row, col) = v(row)
     row += 1
   }
 }
 
  // update the row r with the contents of the array v
  // e.g.  val x = rand(4, 5);  val v = vones(5);  x(3,  ::) = v
 override final def update(r: Int, colonSymbol: ::.type, v: Array[Double])  {
   var vl = v.length
    if (vl > Ncols)  // use a part of the array only to fill the columns
     vl = Ncols
   
   val row = if (r < 0) Nrows + r else r
   var col = 0
   while (col < vl) {
     this(row, col) = v(col)
     col += 1
   }
 }
 
// update a Matrix subrange by assigning a Matrix, starting from m(rlowp, clowp) and
// copying the elements of matrix mr, every rincp rows and every cincp columns  
/*
val m=rand(20, 30); 
m( 2, 1, 2, 3) = ones1(2,2)    // start from m(2,1), every 2 rows and every 3 cols
 */
 override final def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: Matrix ): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rinc = rincp; var cinc = cincp
    var rlow=rlowp; var rhigh=rlow+mrM*rinc 
    var clow=clowp; var chigh=clow+mrN*cinc
  
        if (rhigh >= Nrows || chigh >= Ncols)  {   
          println("accessing out of range element")
      }  

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



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand(20, 30);  mm(2, 3, ::) = ones1(2,2);
 override final def update(rlowp:Int, clowp:Int, ch: ::.type, mr: Matrix): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rlow=rlowp; var rhigh=rlow+mrM; var rinc = 1;
    var clow=clowp; var chigh=clow+mrN; var cinc = 1;
        
     if (rhigh >= Nrows || chigh >= Ncols)  {   // dynamically increase the size of the matrix when subassigning out of its range
       println("accessing out of range element")
     }   

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

  
  
// update a Matrix subrange by assigning a zero-indexed matrix, starting from m(rlowp, clowp) and
// copying the elements of matrix mr, every rincp rows and every cincp columns  
/*
val m=rand(20, 30); 
m( 2, 1, 2, 3) = ones(2,2)    // start from m(2,1), every 2 rows and every 3 cols
 */
 override  final def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: RichDouble2DArray ): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rinc = rincp; var cinc = cincp
    var rlow=rlowp; var rhigh=rlow+mrM*rinc 
    var clow=clowp; var chigh=clow+mrN*cinc
    
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

     
      if (rhigh >= Nrows || chigh >= Ncols)  {   
          println("accessing out of range element")
          return
      }   // dynamically increase the size of the matrix


     // copy the values of the mr
        var rrow=0; var rcol=0; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=0
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



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand(20, 30);  mm(2, 3, ::) = ones(2,2);
 override final def update(rlowp:Int, clowp:Int, ch: ::.type, mr: RichDouble2DArray): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rlow=rlowp; var rhigh=rlow+mrM; var rinc = 1;
    var clow=clowp; var chigh=clow+mrN; var cinc = 1;
        
     if (rhigh >= Nrows || chigh >= Ncols)  {   
          println("accessing out of range element")
          return
      }   // dynamically increase the size of the matrix


     // copy the values of the mr
        var rrow=0; var rcol=0; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=0
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


  
  
// returns the corresponding row of the Mat class as an Array[Double]
// e.g.  var x = rand(300, 400); var firstRowRandVector = x.getRow(0); plot(firstRowRandVector)  
override  final def getRow(row: Int): Array[Double] = {
    var rowArray = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
       rowArray(ccol) = this(row, ccol)
       ccol += 1
     }
    rowArray
  }

  
//  returns the corresponding column of the Mat class as an Array[Double]
//  e.g.  var x = rand(120, 230); var thirdColumnRandVector = x.getCol(2); plot(thirdColumnRandVector)  
override final def getCol(col: Int): Array[Double] = {
    var colArray = new Array[Double](Nrows)
    var rrow = 0
    while  (rrow < Nrows) {
       colArray(rrow) = this(rrow, col)
       rrow += 1
      }
    colArray
  }
  
  
// cross (pointwise) product of a Matrix with a Matrix
final def  cross(that: RichDouble2DArray) = {
  var nv = new RichDouble2DArray(Nrows, Ncols) // create the new matrix
   var i=0; var j=0
   while (i<Nrows) {
     j=0
    while (j<Ncols)  {
      nv(i, j) = this(i, j) * that(i, j)
      j += 1
    }
    i += 1
   }
 nv // return the new Matrix
}


  

// dot  product of a Matrix with a Matrix
final def  dot(that: RichDouble2DArray ) = {
  var dotProduct = 0.0
   var i=0; var j=0
   while (i<Nrows) {
     j = 0
    while (j<Ncols)  {
      dotProduct += this(i, j) * that(i, j)
      j  +=  1
    }
    i += 1
   }
 dotProduct
}

// dot product of a Matrix with an Array[Array[Double]]
override final def  dot(that: Array[Array[Double]])  = {
   var dotProduct = 0.0 
   var i = 0; var j = 0
   while (i<Nrows) {
     j = 0
    while (j<Ncols)  {
      dotProduct += this(i, j) * that(i)(j)
      j += 1
    }
    i += 1
   }
 dotProduct
}

  
  
// dot product of a Matrix with an Array[Double]
override final def  dot(that: Array[Double])  = {
   var dotProduct = 0.0 
   var thatLen = that.length
   if (thatLen == Nrows) {  // rowwise
   var row = 0
   while (row < Nrows) {
      dotProduct += this(row, 0) * that(row)
      row += 1
    }
    dotProduct
   }
   else if  (thatLen == Ncols)  {  // columnwise
     var col = 0
     while (col < Ncols) {
       dotProduct += this(0, col) * that(col)
       col += 1
     }
     dotProduct
   }
   else 
     dotProduct
}
   
  
// dot product of a Matrix with a Vec
override final def  dot(that: Vec)  = {
   var dotProduct = 0.0 
   var thatLen = that.length
   if (thatLen == Nrows) {  // rowwise
   var row = 0
   while (row < Nrows) {
      dotProduct += this(row, 0) * that(row)
      row += 1
    }
    dotProduct
   }
   else if  (thatLen == Ncols)  {  // columnwise
     var col = 0
     while (col < Ncols) {
       dotProduct += this(0, col) * that(col)
       col += 1
     }
     dotProduct
   }
   else 
     dotProduct
}
  
  
  // apply the function f to all the elements of the Matrix and return the results with a new Matrix
  // e.g. val  x = ones(4, 6); val y = x map sin   // return a new matrix with the sines of all matrix elements 
 override final def  map(  f: (Double => Double)) = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
    
     
    var r = 0
    while (r < Nrows)  {
      var c = 0
      while (c < Ncols)  {
         mres(r, c) = f(this(r, c) )
         c += 1
       }
     r += 1
    }
   mres
 }
 
  
  // apply the function f to all the elements of the Matrix in-place
  // e.g. val  x = ones(4, 6);  x mapi sin  //  apply the sin function to all the matrix elements
 override  final def  mapi( f: (Double => Double)) = {
    var r = 0
    while ( r < Nrows) {
      var c = 0
      while  (c < Ncols)  {
        this(r, c) = f(this(r, c) )
        c += 1
      }
     r += 1
    }
   
   this
 }
 
  
  
  
  // Parallel operations. They are implemented specifically for 
  // the RichDouble2DArray type for a slightly efficiency
  // gain, comparing to the general scalaSci matrix implementation
  
  override def  psin(): RichDouble2DArray = {
    var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

   
  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.sin(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  pcos(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.cos(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  ptan(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.tan(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  psinh(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.sinh(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  pcosh(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.cosh(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  ptanh(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.tanh(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  pasin(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.asin(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  pacos(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.acos(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  patan(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.atan(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}


  override def  pfloor(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.floor(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  pceil(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.ceil(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  pround(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.round(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  psqrt(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.sqrt(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  plog(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.log(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  
  override def  pexp() = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.exp(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}
     
  
  
  
  override def  plog2(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
   val conv = java.lang.Math.log(2.0)
  
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.log(rdObj(r, c)/conv )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  
  override def  pabs(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
   
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.abs(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}

  override def  plog10(): RichDouble2DArray = {
   var mres = new RichDouble2DArray(Nrows, Ncols)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         mres(r, c) = Math.log10(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres
}


  // Parallel in-place operations implemented and for RichDouble2Array
  // for a slight efficiency gain in comparison with the generic
  // scalaSciMatrix implementation
  
  
  // Parallel in-place operations
  
  override def pisin() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.sin(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def picos() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.cos(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def pitan() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.tan(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def pitanh() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.tanh(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def pisinh() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.sinh(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def picosh() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.sin(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  override def pisqrt() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.sqrt(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def piabs() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.abs(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  override def piasin() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.asin(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def piacos() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.acos(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  override def piatan() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.atan(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  
  override def piceil() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.ceil(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  
  override def pifloor() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.floor(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
  
  
  override def piround() = {
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         rdObj(r, c) = Math.round(rdObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
   
}
  
   
      
 final def psmap(f: DoubleUnaryOperator ) = {
   
    val   rN = v.length;   val rM = this.v(0).length;
    var res  =new RichDouble2DArray(rN, rM)
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads =  Math.min(nthreads, rN)
  
  val futures = new Array[Future[_]](nthreads)
  val rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes
 
  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
   
      // compute range of rows handled by that thread
    var firstRow = threadId * rowsPerThread
     var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r = firstRow   // the first row of the matrix that this thread processes
      while (r < lastRow) {  // the last row of the matrix that this thread processes
          
          var ds = DoubleStream.of(v(r): _*)
          res.v(r) = ds.map(f).parallel().toArray()
          r += 1
      
      }
   
      }
    })


            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
 
    res
}

 // parallel map in place: map the function f to each element of the matrix using multithreading
 // pmap is faster than map only for large matrices
 
 override final def pmapi(f: (Double => Double)) = {
 
    val   rN = v.length;   val rM = this.v(0).length;
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads =  Math.min(nthreads, rN)
  
  val futures = new Array[Future[_]](nthreads)
  val rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes
 
  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
   
      // compute range of rows handled by that thread
    var firstRow = threadId * rowsPerThread
     var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r = firstRow   // the first row of the matrix that this thread processes
      while (r < lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         v(r)(c) = f(v(r)(c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
 
}

 /*
override final def pmapi(f: (Double => Double)) = {
 println("pmapi entered")

    val   rN = v.length;   val rM = this.v(0).length;
   
  var nthreads = scalaExec.Interpreter.PendingThreads.nthreads
  nthreads = Math.min(nthreads, rN)
  
  val futures = new Array[Future[_]](nthreads)
  val rowsPerThread = (rN / nthreads).toInt + 1  // how many rows the thread processes
 println("pmapi started, numthreads = "+nthreads)

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
   
      // compute range of rows handled by that thread
    var firstRow = threadId * rowsPerThread
     var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = scalaExec.Interpreter.PendingThreads.pool.submit(new Runnable() {
    def run = {
      var r = firstRow   // the first row of the matrix that this thread processes
      while (r < lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         v(r)(c) = f(v(r)(c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  for (t<-0 until nthreads)
      futures(t).get
      
  println("pmapi finished, numthreads = "+nthreads)

}
*/
 // parallel map in place: map the function f to each element of the matrix using multithreading
 // pmap is faster than map only for large matrices
override final  def pmap(f: (Double => Double)) = {
    
   var   rN = v.length;   var rM = this.v(0).length;
   
  var res  =new RichDouble2DArray(rN, rM)
   
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  val futures = new Array[Future[_]](nthreads)
  val rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes
  //println("rowsPerThread = "+rowsPerThread)

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
      // compute range of rows handled by that thread
    var firstRow = threadId * rowsPerThread
     var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  // println("thread "+threadId+ "  firstRow = "+firstRow+ ", lastRow = "+lastRow)
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var fl = f
      var r = firstRow   // the first row of the matrix that this thread processes
      while (r < lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         res(r, c) = f(v(r)(c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    res
}

   
      
  
 // parallel filter: filter the elements of the matrix using multithreading
override final def pfilter(p: (Double => Boolean)) = {
    var fres = new RichDouble2DArray(Nrows, Ncols)
    var   rN = v.length;   var rM = this.v(0).length;
   
  
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
        var cval = v(r)(c)     // the current element of the matrix
        if ( p(cval) == true)    // element passed the predicate condition,
          fres(r, c) = cval       // copy it to the result filtered matrix
         c  += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    fres
}

      
  
 // parallel exists:  detect if there exists some element that satisfies the predicate p
override final def pexists(p: (Double => Boolean)): Boolean = {
    var   rN = v.length;   var rM = this.v(0).length;
    var found = false
    
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run: Unit = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
        var cval = v(r)(c)     // the current element of the matrix
        if ( p(cval) == true)    // element passed the predicate condition,
            {
                  found = true
              //   simply to force exiting the loop 
                  c = Ncols
                  r = lastRow
            }
          c  += 1
      }
      r += 1
      }
    return 
  }
    
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    return found
}

  
  
 // parallel forall::  detect if all elements of the matrix satisfy the predicate p
override final def pforall(p: (Double => Boolean)): Boolean = {
    var   rN = v.length;   var rM = this.v(0).length;
    var condHoldsForAll = true
    
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((rN-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) rN else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run: Unit = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
        var cval = v(r)(c)     // the current element of the matrix
        if ( p(cval) == false)    // element passed the predicate condition,
            {
                  condHoldsForAll = false
              //   simply to force exiting the loop 
                  c = Ncols
                  r = lastRow
            }
          c  += 1
      }
      r += 1
      }
    return 
  }
    
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    return condHoldsForAll
}
      
              
  //  forall returns a boolean indicating whether the predicate p holds for all the elements of the matrix, e.g.
  /*
    val  x = zeros0(4,6); val allZeros = x forall ( _ == 0)
     x(2,2)=22.2; val allZerosShouldBeFalse = x forall (_ == 0)
     */
 override final def forall( p: (Double => Boolean)) = {
   var  isTrueForAll = true
   var r = 0
   while  (r < Nrows)  {
     var c = 0
     while  (c < Ncols) {
       if ( p(this(r, c)) == false)
         isTrueForAll = false
       c += 1
     }
     r += 1
   }
   isTrueForAll
 }
 
// exists returns a boolean indicating whether the predicate p holds for some element of the matrix, e.g.
/*
  val  x = zeros0(4,6); val existsLargerThanZero = x exists  ( _ > 0)
   x(2,2) = 22.2; val existsLargerThanZeroAfter = x exists (_ > 0)
   */
 override  final def exists(p: (Double => Boolean)): Boolean  = {
    var  r = 0
    while  (r < Nrows) {
      var c = 0
       while  (c < Ncols) {
         if ( p(this(r, c)) == true)
           return true
         c += 1
       }
       r += 1
    }
     
    return false  
 }
     
// returns a new matrix consisting of the elements for which the predicate p evaluates to true
/*
  val x = randn(3,5)
  final def isPositive(x: Double) = x > 0
  val y = x filter isPositive
 */  
override  final def filter(p: (Double => Boolean)) = {
   var fres = new RichDouble2DArray(Nrows, Ncols) // construct the matrix to keep the filtered result
   var r = 0   
   while  (r < Nrows)  {
       var c = 0
       while  (c < Ncols) {
         if ( p(this(r, c)) == true)    // element passed the predicate condition
            fres(r, c) = this(r, c)
          c += 1
      }
      r += 1
   }
   fres // return the matrix consisting of the elements that pass the filter condition
}  
             



  /*  filter all the rows/columns of the matrix according to the predicate
   the predicate is a function from the Int index of row/column to a boolean value
   For example, to return all the even numbered rows and columns of a matrix: 
    
      val  x = rand(5, 7)
      final def isEven(n: Int) = if (n % 2 == 0) true else false   // define the predicate
      val xevenRows = x filterRows isEven
      val xevenCols = x filterColumns isEven
   
  */
   
  override final def  filterRows( predicate:  Int  => Boolean) = {
      var rowCnt = 0
      var r = 0
    // count the number of rows that fullfill the predicate
    while (r < this.numRows()) {
          if (predicate(r))  
            rowCnt += 1
         r += 1
     }
    var  newMat =new RichDouble2DArray(rowCnt, this.numColumns)
      
     r = 0
    var rCnt=0
     while  ( r < this.numRows())  {   // for all the rows
      if (predicate(r)) {  // copy the row
       var c = 0 
        while  (c < this.numColumns())  {  // for all columns
          newMat(rCnt,c) = this(r,c)
          c += 1
          }  // for all columns
           rCnt += 1
       }  // copy the row
         r += 1  // next row
        
      }  // for all the rows
                    
    newMat
  
  }		 

  // return cols according to the predicate
 override final def  filterColumns( predicate:  Int => Boolean) = {
    var colCnt = 0
    var c = 0
    while  (c <  this.numColumns()) {
      if (predicate(c))
       colCnt += 1
     c += 1
  }
  var newMat = new RichDouble2DArray(this.numRows(), colCnt)
    
    c = 0; 
    var cCnt=0
    while  (c <  this.numColumns()) {  // for all the columns
      if (predicate(c) )  {  // copy the column
        var r = 0
      while  (r <  this.numRows())  {  // for all rows
        newMat(r, cCnt) = this(r,c)
        r += 1
        }  // for all rows
         cCnt += 1
      } // copy the column
      c += 1 // next column
    }   // for all the columns
    
      newMat
  
   }		 

  
// Row append and prepend routines    
/*
 var  mm = rand(4, 5)
 var mmo = ones(2, 5)
 var mmappend = mm RA mmo  // prepend mmo 
 var mmprepend = mm RA mmo // append mmo 
   */   
override final def  RA(rowsToAppend: RichDouble2DArray ): RichDouble2DArray = {
    if (rowsToAppend.Ncols != this.Ncols )   // incompatible number of columns
      return this
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToAppend.Nrows   //  number of rows of the new matrix
    var res = new RichDouble2DArray (exrows, this.Ncols)

    // copy "this" Matrix
  var r = 0; var c = 0
  while (r <  this.Nrows) {
       c = 0
       while  (c < this.Ncols) {
          res(r, c) = this(r, c)
          c += 1
       }
       r += 1
    }

  //  append the passed matrix at the end
    r = 0  
    while  (r < rowsToAppend.Nrows)  {
      c = 0
      while  (c < rowsToAppend.Ncols)  {
         res(Nrows + r, c) = rowsToAppend(r, c)
         c += 1
          }
          r += 1
        }
    res
}

final def  RA(rowsToAppend: scalaSci.Mat): RichDouble2DArray = 
  this.RA ( new RichDouble2DArray(rowsToAppend.toDoubleArray))
  

final def  RA(rowsToAppend: scalaSci.EJML.Mat): RichDouble2DArray = 
  this.RA ( new RichDouble2DArray(rowsToAppend.toDoubleArray))

final def  RA(rowsToAppend: scalaSci.MTJ.Mat): RichDouble2DArray = 
  this.RA ( new RichDouble2DArray(rowsToAppend.toDoubleArray))
 
final def  RA(rowsToAppend: scalaSci.CommonMaths.Mat): RichDouble2DArray = 
  this.RA ( new RichDouble2DArray(rowsToAppend.toDoubleArray)) 
    
final def  RA(rowsToAppend: scalaSci.JBLAS.Mat): RichDouble2DArray = 
  this.RA ( new RichDouble2DArray(rowsToAppend.toDoubleArray))

  
override  final def  RA(rowToAppend: Array[Double]): RichDouble2DArray  = {
    if (rowToAppend.length != this.Ncols )   // incompatible number of columns
      return this
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+1    // new number of rows
    var res = new RichDouble2DArray (exrows, this.Ncols)
    
    // copy "this" Matrix
  var r = 0; var c = 0
  while (r <  this.Nrows) {
       c = 0
       while  (c < this.Ncols) {
          res(r, c) = this(r, c)
          c += 1
       }
       r += 1
    }

    c = 0
    while  (c < rowToAppend.length) {
         res(Nrows, c) = rowToAppend(c)
         c += 1
      }
      
    res
}

 override final def  RA(rowToAppend: RichDouble1DArray): RichDouble2DArray = {
    this RA rowToAppend.getv 
 }
 
  
 override final def  RA(rowToAppend: scalaSci.Vec): RichDouble2DArray  = {
     this RA rowToAppend.getv  
 }
 
 
// prepend rowwise the Matrix Ncols
override final def  RP(rowsToPrepend: RichDouble2DArray): RichDouble2DArray  = {
          if (rowsToPrepend.Ncols != this.Ncols )   // incompatible number of columns
      return this
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToPrepend.Nrows   // new number of rows
    var res = new RichDouble2DArray ( exrows, this.Ncols)
    // copy prepended  Matrix
    var r = 0
    while  (r <  rowsToPrepend.Nrows)   {
      var c = 0
      while  (c < rowsToPrepend.Ncols)  {
         res(r, c) = rowsToPrepend(r, c)
         c += 1
      }
      r += 1
    }
    
    // copy "this" matrix
    var rowsPrepended = rowsToPrepend.Nrows
    r = 0
    while  (r < this.Nrows)  {
      var c = 0 
      while  (c < this.Ncols)  {
         res(rowsPrepended+r, c) = this(r, c)
         c += 1
        }
      r += 1
  }
    res
}


  
final def  RP(rowsToPrepend: scalaSci.Mat): RichDouble2DArray = 
  this.RP ( new RichDouble2DArray(rowsToPrepend.toDoubleArray))
  

final def  RP(rowsToPrepend: scalaSci.EJML.Mat): RichDouble2DArray = 
  this.RP ( new RichDouble2DArray(rowsToPrepend.toDoubleArray))

final def  RP(rowsToPrepend: scalaSci.MTJ.Mat): RichDouble2DArray = 
  this.RP ( new RichDouble2DArray(rowsToPrepend.toDoubleArray))
 
final def  RP(rowsToPrepend: scalaSci.CommonMaths.Mat): RichDouble2DArray = 
  this.RP ( new RichDouble2DArray(rowsToPrepend.toDoubleArray)) 
    
final def  RP(rowsToPrepend: scalaSci.JBLAS.Mat): RichDouble2DArray = 
  this.RP ( new RichDouble2DArray(rowsToPrepend.toDoubleArray))

  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: Array[Double]): RichDouble2DArray = {
    if (rowToPrepend.length != this.Ncols )   // incompatible number of columns
      return this
    // create a new extended matrix to have also the added rows
   var  exrows = Nrows+1  // new number of rows
   var res = new RichDouble2DArray(exrows, this.Ncols)
   
    // prepend the passed 1-d array
    var c = 0 
    while  (c < this.Ncols)  {
         res(0, c) = rowToPrepend(c)
         c += 1
        }
       
// copy "this" Matrix
    var r = 0
    while  (r <  this.Nrows)   {
      var c = 0
      var r1 = r + 1 
      while  (c < this.Ncols)  {
         res(r1, c) = this(r, c)
         c += 1
      }
      r += 1
    }
          
    res
}

  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: RichDouble1DArray): RichDouble2DArray = {
    this  RP rowToPrepend.getv 
}
  
  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: scalaSci.Vec): RichDouble2DArray = {
   this RP rowToPrepend.getv 
}

  
//  perhaps easier to remember alias
//  perhaps easier to remember alias
 
override final def   >> (rowsToAppend: RichDouble2DArray): RichDouble2DArray = 
    this RA  rowsToAppend 
 
override final def   >>  (rowToAppend: Array[Double]): RichDouble2DArray = 
     this RA rowToAppend 

override final def   >> (rowToAppend: RichDouble1DArray): RichDouble2DArray = 
     this RA rowToAppend 

final def   >> (rowToAppend: scalaSci.Mat ): RichDouble2DArray = 
     this RA rowToAppend 
  

final def   >> (rowToAppend: scalaSci.EJML.Mat ): RichDouble2DArray = 
     this RA rowToAppend 
  

final def   >> (rowToAppend: scalaSci.MTJ.Mat ): RichDouble2DArray = 
     this RA rowToAppend 

 
final def   >> (rowToAppend: scalaSci.CommonMaths.Mat ): RichDouble2DArray = 
     this RA rowToAppend 
   

final def   >> (rowToAppend: scalaSci.JBLAS.Mat ): RichDouble2DArray = 
     this RA rowToAppend 
  
 

override  final def  >>(rowToAppend: scalaSci.Vec): RichDouble2DArray =
    this RA rowToAppend 
  
override final def  <<(rowsToPrepend: RichDouble2DArray): RichDouble2DArray = 
    this RP rowsToPrepend
 
override final def   << (rowToPrepend: Array[Double]): RichDouble2DArray = 
   this RP rowToPrepend   

override  final def  <<(rowToPrepend: RichDouble1DArray): RichDouble2DArray = 
   this RP rowToPrepend 
 
override  final def  << (rowToPrepend: scalaSci.Vec): RichDouble2DArray =
   this RP rowToPrepend   
 
  
  
final def   <<  (rowToPrepend: scalaSci.Mat ): RichDouble2DArray = 
     this RP rowToPrepend
  

final def   << (rowToPrepend: scalaSci.EJML.Mat ): RichDouble2DArray = 
     this RP  rowToPrepend
  

final def   << (rowToPrepend: scalaSci.MTJ.Mat ): RichDouble2DArray = 
     this RP  rowToPrepend 

 
final def   << (rowToPrepend: scalaSci.CommonMaths.Mat ): RichDouble2DArray = 
     this RP rowToPrepend
   

final def   << (rowToPrepend: scalaSci.JBLAS.Mat ): RichDouble2DArray = 
     this RP  rowToPrepend 
 
    // Column append and prepend routines, e.g.
/* 
 var  mm = rand0(4, 5)
 var mmo = ones0(4, 2)
 var mmappend = mm CP mmo  // prepend mmo 
 var mmprepend = mm  CA mmo // append mmo 
 */
override final def  CA(colsToAppend: RichDouble2DArray): RichDouble2DArray = {
    if (colsToAppend.Nrows != this.Nrows )   // incompatible number of rows
      return this
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToAppend.Ncols  // new number of columns
    var res = new RichDouble2DArray(this.Nrows, excols)
 
    // copy "this" Matrix
    var r = 0
    while  (r <  this.Nrows) {
      var c = 0 
      while (c < this.Ncols)  {
         res(r, c) = this(r, c)
         c += 1
      }
      r += 1 
    }
    
    r = 0
    while  (r < colsToAppend.Nrows)   {
      var c = 0 
      while  (c < colsToAppend.Ncols)  {
         res(r, Ncols+c) = colsToAppend(r, c)
         c += 1
       }
      r += 1 
    }
    res
}

  
final def  CA(colsToAppend: scalaSci.Mat): RichDouble2DArray = 
  this.CA ( new RichDouble2DArray(colsToAppend.toDoubleArray))
  

final def  CA(colsToAppend: scalaSci.EJML.Mat): RichDouble2DArray = 
  this.CA ( new RichDouble2DArray(colsToAppend.toDoubleArray))

final def  CA(colsToAppend: scalaSci.MTJ.Mat): RichDouble2DArray = 
  this.CA ( new RichDouble2DArray(colsToAppend.toDoubleArray))
 
final def  CA(colsToAppend: scalaSci.CommonMaths.Mat): RichDouble2DArray = 
  this.CA ( new RichDouble2DArray(colsToAppend.toDoubleArray)) 
    
final def  CA(colsToAppend: scalaSci.JBLAS.Mat): RichDouble2DArray = 
  this.CA ( new RichDouble2DArray(colsToAppend.toDoubleArray))

  // append an Array[Double] as the last column
override  final def  CA(colsToAppend: Array[Double]): RichDouble2DArray = {
    if (colsToAppend.length != this.Nrows )   // incompatible number of rows
      return this
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+1 // new number of columns
    var res = new RichDouble2DArray(this.Nrows, excols)
 
    // copy "this" Matrix
    var r = 0
    while  (r <  this.Nrows) {
      var c = 0 
      while (c < this.Ncols)  {
         res(r, c) = this(r, c)
         c += 1
      }
      r += 1 
    }
    
    // copy the double array
    r = 0
    while  (r < colsToAppend.length)   {
         res(r, Ncols) = colsToAppend(r)
         r += 1
       }
      
    res
}

  // append a RichDouble1DArray as the last column
  override  final def  CA (colsToAppend: RichDouble1DArray): RichDouble2DArray = {
     this CA colsToAppend.getv
  }

   // append a scalaSci.Vec as the last column
  override final def CA (colsToAppend: scalaSci.Vec): RichDouble2DArray  = {
     this CA colsToAppend.getv  
  }
  

// prepend a Mat  
  override final def  CP(colsToPrepend: RichDouble2DArray): RichDouble2DArray= {
    if (colsToPrepend.Nrows != this.Nrows )   // incompatible number of rows
      return this
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToPrepend.Ncols  // new number of columns
    var res = new RichDouble2DArray(this.Nrows, excols)

    // copy prepended matrix
    var r = 0
    while  (r < colsToPrepend.Nrows)   {
      var c = 0 
      while  (c < colsToPrepend.Ncols)  {
         res(r,  c) = colsToPrepend(r, c)
         c += 1
       }
      r += 1 
    }
    
    var    ncolsPrepended  = colsToPrepend.Ncols
// copy "this" Matrix
    r = 0
    while  (r <  this.Nrows) {
      var c = 0 
      while (c < this.Ncols)  {
         res(r, c+ncolsPrepended) = this(r, c)
         c += 1
      }
      r += 1 
    }
    
    res
}


  
final def  CP(colsToPrepend: scalaSci.Mat): RichDouble2DArray = 
  this.CP( new RichDouble2DArray(colsToPrepend.toDoubleArray))
  

final def  CP(colsToPrepend: scalaSci.EJML.Mat): RichDouble2DArray = 
  this.CP ( new RichDouble2DArray(colsToPrepend.toDoubleArray))

final def  CP(colsToPrepend: scalaSci.MTJ.Mat): RichDouble2DArray = 
  this.CP( new RichDouble2DArray(colsToPrepend.toDoubleArray))
 
final def  CP(colsToPrepend: scalaSci.CommonMaths.Mat): RichDouble2DArray = 
  this.CP ( new RichDouble2DArray(colsToPrepend.toDoubleArray)) 
    
final def  CP(colsToPrepend: scalaSci.JBLAS.Mat): RichDouble2DArray = 
  this.CP ( new RichDouble2DArray(colsToPrepend.toDoubleArray))

// prepend an Array[Double] to matrix
override final def  CP(colsToPrepend:  Array[Double]): RichDouble2DArray = {
    var arrayLen = colsToPrepend.length
    if (arrayLen!= this.Nrows )   // incompatible number of rows
      return this
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+1  // new number of columns
    var res = new RichDouble2DArray(this.Nrows, excols)
    
    // copy Array[Double]
    var  r = 0
     while ( r < colsToPrepend.length)  {
         res(r, 0) = colsToPrepend(r)
        r += 1 
      }
      
    // copy "this" Matrix
    r = 0
    while  (r < this.Nrows) {
      var c = 0
      while  (c < this.Ncols)  {
         res(r, 1+c) = this(r, c)
         c += 1
      }
      r += 1
    }

    res
}

  // prepend a RichDouble2DArray
override final def CP(colToPrepend: RichDouble1DArray): RichDouble2DArray = 
   this CP colToPrepend.getv 

 // prepend a scalaSci.Vec
override  final def CP(colToPrepend: scalaSci.Vec): RichDouble2DArray = 
   this CP colToPrepend.getv 

 
//  perhaps easier to remember alias
override final def   >>>(colsToAppend: RichDouble2DArray): RichDouble2DArray = 
    this CA colsToAppend  
 
override  final def   >>>(colToAppend: Array[Double]): RichDouble2DArray = 
   this CA colToAppend 

override  final def   >>>(colToAppend: RichDouble1DArray): RichDouble2DArray = 
   this CA colToAppend 
 
override  final def  >>>(colToAppend: scalaSci.Vec): RichDouble2DArray =
   this CA colToAppend 
 
  
final def   >>>  (colsToAppend:  scalaSci.Mat ): RichDouble2DArray = 
     this CA colsToAppend
  

final def   >>> (colsToAppend: scalaSci.EJML.Mat ): RichDouble2DArray = 
     this CA colsToAppend
  

final def   >>> (colsToAppend: scalaSci.MTJ.Mat ): RichDouble2DArray = 
     this CA  colsToAppend

 
final def   >>> (rowToAppend : scalaSci.CommonMaths.Mat ): RichDouble2DArray = 
     this CA rowToAppend
   

final def   >>>(rowToAppend:  scalaSci.JBLAS.Mat ): RichDouble2DArray = 
     this CA  rowToAppend 
 
  
    
override final def   <<<(colsToPrepend: RichDouble2DArray): RichDouble2DArray = 
   this CP colsToPrepend  
 
override  final def   <<<(colToPrepend: Array[Double]): RichDouble2DArray = 
   this CP colToPrepend 

override  final def   <<<(colToPrepend: RichDouble1DArray): RichDouble2DArray = 
    this CP colToPrepend 
 
override  final def  <<<(colToPrepend: scalaSci.Vec): RichDouble2DArray =
   this CP colToPrepend 
   

final def   <<<  (colsToPrepend: scalaSci.Mat ): RichDouble2DArray = 
     this CP  colsToPrepend
  

final def   <<< (colsToPrepend: scalaSci.EJML.Mat ): RichDouble2DArray = 
     this CP colsToPrepend
  

final def   <<< (colsToPrepend: scalaSci.MTJ.Mat ): RichDouble2DArray = 
     this CP colsToPrepend

 
final def   <<< (colsToPrepend: scalaSci.CommonMaths.Mat ): RichDouble2DArray = 
     this CP colsToPrepend
   

final def   <<< (colsToPrepend:  scalaSci.JBLAS.Mat ): RichDouble2DArray = 
     this CP  colsToPrepend





  def  jj(B: RichDouble2DArray): RichDouble2DArray = {
     jj (B.v)
  }
  
  // fast multiply using native BLAS and Java multithreading
def  jj(B: Array[Array[Double]]): RichDouble2DArray = {
   var   Arows = v.length;   var Acols = v(0).length;
   var   Ccols = B(0).length
   var C = Array.ofDim[Double](Arows,  Ccols)  // the result matrix
   
   var   nthreads = ConcurrencyUtils.getNumberOfThreads
   nthreads = Math.min(nthreads,  Arows)
   var futures = new Array[Future[_]](nthreads)

 //  var nthreadsForSplitting = if (nthreads > 1) nthreads-1 else nthreads
   var rowsPerThread = (Arows / nthreads).toInt  // how many rows the thread processes
  
  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Arows else firstRow+rowsPerThread
    var numRowsForThread = lastRow-firstRow
 
//    println("thread = "+ threadId +", firstRow = "+firstRow + " lastRow = "+lastRow+" numRowsForThread = "+numRowsForThread)
       
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      
               // prepare workspace on which the thread will work
      var a = new Array[Double](numRowsForThread*Acols)
      var b = new Array[Double](Acols*Ccols)
      var c = new Array[Double](numRowsForThread*Ccols)
      
            //  copy the part of the A matrix to a linear 1-D array
      var cnt = 0
      var col = 0
       while (col < Acols) {
       var row = firstRow
       while (row < lastRow) {  
             a(cnt) = v(row)(col)
             row += 1
             cnt += 1
             }
           col += 1
           }
             
            // copy the B matrix to a linear 1-D array
         cnt = 0
         col = 0
         while (col < Ccols) {
           var row = 0
           while (row < Acols) {
              b(cnt) = B(row)(col)    
              row += 1
              cnt += 1
              }
        col += 1
        }
                      
            //NativeBlas.dgemm('N', 'N', c.rows, c.columns, a.columns, 
              //               alpha, a.data, 0,
		//		a.rows, b.data, 0, b.rows, 
                                //beta, c.data, 0, c.rows);
		
   org.jblas.NativeBlas.dgemm('N', 'N', numRowsForThread,  Ccols,  Acols, 
                              1.0,  a,  0, 
                              numRowsForThread,  b, 0,  Acols,
                              0.0, c, 0, numRowsForThread); 

  
            // copy contribution of the thread onto the final result
  cnt = 0
  col = 0
  while (col < Ccols) {
    var row = firstRow
    while (row < lastRow) {
          C(row)(col) = c(cnt)
          row += 1
          cnt += 1
          }
        col += 1
        }  
      
  
        } // run
        threadId += 1
        
  })  // for all threads
 }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
  
    new RichDouble2DArray(C)
 }


	

 
// multiplication using openblas
final def *** (that:  Array[Array[Double]]) = {
  import org.bytedeco.javacpp.openblas._
  
   var flmThis = oneDTransposeDoubleArray( this.v  )   // construct a FloatMatrix for the receiver
   var flmThat = oneDTransposeDoubleArray(that)  // construct a FloatMatrix for the argument
   var Arows = Nrows; var Acolumns = Ncols
   var Ccolumns = that(0).length
   var result = new Array[Double](Arows*Ccolumns)
   var alpha=1.0
   var beta=0.0
   var lda = Arows
   var ldb = Acolumns
   var ldc = Arows
      // perform the multiplication using OpenBlas
    cblas_dgemm(CblasColMajor, CblasNoTrans, CblasNoTrans, Arows, Ccolumns,  Acolumns, alpha, flmThis, lda, flmThat, ldb, beta, 
         result, ldc)
   
    var rd = Array.ofDim[Double](Arows, Ccolumns)
    var cnt = 0
    var r = 0;
    var c = 0
    while (c < Ccolumns) {
      r=0
     while (r < Arows ) {
        rd(r)(c) = result(cnt)
        cnt += 1
        r += 1
      }
      c += 1
    }
    new RichDouble2DArray(rd)
    
}   
   

// multiplication using openblas
final def *** (that: RichDouble2DArray) = {
  import org.bytedeco.javacpp.openblas._
  
   val flmThis = oneDTransposeDoubleArray( this.v  )   // construct a FloatMatrix for the receiver
   val flmThat = oneDTransposeDoubleArray(that.v)  // construct a FloatMatrix for the argument
   val Arows = Nrows; var Acolumns = Ncols
   val Ccolumns = that.Ncols
   val result = new Array[Double](Arows*Ccolumns)
   val alpha=1.0
   val beta=0.0
   val lda = Arows
   val ldb = Acolumns
   val ldc = Arows
      // perform the multiplication using OpenBlas
    cblas_dgemm(CblasColMajor, CblasNoTrans, CblasNoTrans, Arows, Ccolumns,  Acolumns, alpha, flmThis, lda, flmThat, ldb, beta, 
         result, ldc)
   
    val rd = Array.ofDim[Double](Arows, Ccolumns)
    var cnt = 0
    var r = 0;
    var c = 0
    while (c < Ccolumns) {
      r=0
     while (r < Arows ) {
        rd(r)(c) = result(cnt)
        cnt += 1
        r += 1
      }
      c += 1
    }
    new RichDouble2DArray(rd)
    
}   
  // Array[Array[Double]] * Array[Array[Double]]
 final def smul (that: Array[Array[Double]]): RichDouble2DArray =  {
   var   rN = v.length;   var rM = this.v(0).length;
   var  sN = that.length;  var sM = that(0).length
  
    
   var  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix

      // keeps column j of "that" matrix 
  var   v1Colj = new Array[Double](rM)

      // copy column j of "that"  matrix, in order to have it in cache during the evaluation loop
   var j=0; var k=0;
   while (j < sM)  {
       k=0
      while  (k < rM) {
        v1Colj(k) = that(k)( j)
        k += 1
      }

      var i=0
      while (i<rN) {
        var   Arowi = this.v(i)   // row i of the "receiver" matrix
        var   s = 0.0
        k=0
        while (k< rM) {
          s += Arowi(k)*v1Colj(k)
          k += 1
        }
      vr(i)(j) = s;
      i += 1
      }
 j += 1
   }
  
   
  return new RichDouble2DArray (vr)
      
  }


 // multithreaded multiplication
  final def pmul (that: Array[Array[Double]]): RichDouble2DArray =  {
      var   rN = v.length;   var rM = this.v(0).length;
   var  sN = that.length;  var sM = that(0).length

    // transpose first matrix that. This operation is very important in order to exploit cache locality
var thatTrans = Array.ofDim[Double](sM, sN)
var r=0; var c = 0
while (r<sN) {
  c=0
  while (c<sM) {
    thatTrans(c)(r) = that(r)(c)
    c += 1
  }
  r += 1
}

  var  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((sM-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) sM else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var a=firstRow   // the first row of the matrix that this thread processes
      while (a<lastRow) {  // the last row of the matrix that this thread processes
             var b = 0
             while (b < rN )  {
                 var s = 0.0
                 var c = 0
                 while (c < rM) {
                    s += v(b)(c) * thatTrans(a)(c)
                    c += 1
                   }
                vr(b)(a)   = s
                b += 1
             }
             a += 1
      }
   }
 })
        threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

  
   return new RichDouble2DArray (vr)
}

  

  // Array[Array[Double]] * Array[Array[Double]]
 final def * (that: Array[Array[Double]]): RichDouble2DArray = {
   if (isLargeMatrix==true )   // seems a large multiplication problem
    {
  
     //if (GlobalValues.hostIs64bit)  
       this *** that   
    // else
      // this *& that
 }
     else
   new RichDouble2DArray(scalaSci.SerialMult.times(this.v, that))
 }
   
 override final def * (that: RichDouble2DArray): RichDouble2DArray =  {
    if (isLargeMatrix==true )   // seems a large multiplication problem
    {
    //if (GlobalValues.hostIs64bit)  
     this *** that   
     //else
       //this *& that
 }
    else
  new RichDouble2DArray(scalaSci.SerialMult.times(this.v, that.v))
 }
 
   // Array[Array[Double]] * Array[Array[Double]]
 final def *& (that: Array[Array[Double]]): RichDouble2DArray =  {
     if (GlobalValues.hostIsLinux ||  GlobalValues.hostIsMac)
        this jj that   // Linux and MacOS JBLAS is fast,  use native BLAS
    else
      new RichDouble2DArray(scalaSci.ParallelMult.pmul(this.v, that))  // for Windows multiply using Java
   }
   
 final def *& (that: RichDouble2DArray): RichDouble2DArray =  {
    if (GlobalValues.hostIsLinux ||  GlobalValues.hostIsMac)
        this jj that.v  // Linux JBLAS is fast,  use native BLAS
         else
      new RichDouble2DArray(scalaSci.ParallelMult.pmul(this.v, that.v))  // for Windows multiply using Java
   }
      
 
 
    
  // multiply with JBLAS
  final def *#(that: RichDouble2DArray): RichDouble2DArray =  {
     val dmThat = new org.jblas.DoubleMatrix(that.v)
     val dmThis =  new org.jblas.DoubleMatrix(this.v)
     val mulBLAS = dmThis.mmul(dmThat)
     new RichDouble2DArray(mulBLAS.toArray2)
  }
  

 
// RichDouble2DArray * Matrix
 final def * (that: Matrix): Matrix =  {
   val toMatrix  = new Matrix(this.v)   // convert the 2-D Double Array to Matrix
    
   toMatrix * that
  }

 
final def * (that: scalaSci.JBLAS.Mat)  =  {
   val toMatrix  = new scalaSci.JBLAS.Mat(this.v)   // convert the 2-D Double Array to Matrix
    
  toMatrix * that
  }

// Array[Array[Double]] * Mat
 final def * (that: Mat): Mat =  {
   val toMat  = new Mat(this.v)  // convert the 2-D Double Array to Mat
    
  return toMat * that
  }
  
  
// Array[Array[Double]] * EJML.Mat
 final def * (that: _root_.scalaSci.EJML.Mat) =  {
   val toMat  = new scalaSci.EJML.Mat(this.v)  // convert the 2-D Double Array to Mat
    
  toMat * that 
  }
  
  
  def +(that: Array[Array[Double]]): RichDouble2DArray = {
    var   N  =  that.length
    var   M = that(0).length

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N) { 
       c = 0
       while (c <  M)  {
         result(r)(c) = that(r)(c) + v(r)(c)
         c += 1
       }
       r+=1
     }
 
    new scalaSci.RichDouble2DArray(result)
    
  }
  
  
  // Array[Array[Double]] + Array[Array[Double]]
 final def +& (that: Array[Array[Double]]): RichDouble2DArray =  {
   val   rN = v.length;   val rM = v(0).length;
   val  sN = that.length;  val sM = that(0).length

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r)(c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    
     
  return new RichDouble2DArray (vr)
  }


  
  
  
 override final def + (that: RichDouble2DArray): RichDouble2DArray =  {
   this + that.v
  }


   
  final def +(that: Matrix): Matrix = {
    new Matrix(this.getv) + that
  }
  
  
  
  def +(that: scalaSci.JBLAS.Mat) = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N) { 
       c = 0
       while (c <  M)  {
         result(r)(c) = that(r,c) + v(r)(c)
         c += 1
       }
       r+=1
     }
 
    new scalaSci.JBLAS.Mat(result)
    
  }
  
  final def +& (that: scalaSci.JBLAS.Mat) = {
   val   rN = v.length;   val rM = v(0).length
   val  sN = that.Nrows ; val sM = that.Ncols


  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    
     new RichDouble2DArray (vr)
 }
  

// subtraction of a Matrix from a RichDouble1DArray
  final def -(that: Matrix): Matrix = {
 new Matrix(this.getv) - that
  }

  
  def -(that: scalaSci.JBLAS.Mat) = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N) { 
       c = 0
       while (c <  M)  {
         result(r)(c) = -that(r,c) + v(r)(c)
         c += 1
       }
       r+=1
     }
 
    new scalaSci.JBLAS.Mat(result)
    
  }
  
  final def -& (that: scalaSci.JBLAS.Mat): RichDouble2DArray = {
    val   rN = v.length;   val rM = v(0).length;
   val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

    
  new RichDouble2DArray (vr)
  }
   
  
  
  def +(that: scalaSci.Mat): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result =  Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N)  {
       c = 0
       while (c <  M)  {
         result(r)(c) = that(r,c) + v(r)(c)
         c += 1
       }
       r += 1
     }
 
    new scalaSci.Mat(result, true)
    
  }
  
  
  
  final def +& (that: scalaSci.Mat): scalaSci.Mat = {
    val   rN = v.length;   val rM = v(0).length;
   val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   
  new scalaSci.Mat(vr, true)
  }
  
  
  def -(that: scalaSci.Mat): scalaSci.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

    var result =  Array.ofDim[Double](N,M)
    var r = 0; var c = 0
     while (r <  N)  {
       c = 0
       while (c <  M)  {
         result(r)(c) = -that(r,c) + v(r)(c)
         c += 1
       }
       r += 1
     }
 
    new scalaSci.Mat(result, true)
    
  }
// subtraction of a Matrix from a RichDouble1DArray
  final def -& (that: Mat):RichDouble2DArray = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   return new RichDouble2DArray (vr)
  }

  

  def -(that: Array[Array[Double]]): RichDouble2DArray = {
    var   N  =  this.Nrows
    var   M = this.Ncols

    var result = Array.ofDim[Double](N,M)
    var r = 0; var c = 0
    while  (r <  N)  {
      c = 0
       while (c < M)  {
         result(r)(c) =  v(r)(c) - that(r)(c) 
         c += 1
       }
       r += 1
    }
    new RichDouble2DArray(result)
    
  }
  
  final def -& (that: Array[Array[Double]]): RichDouble2DArray = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.length; val sM = that(0).length


  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r)(c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   
  new RichDouble2DArray(vr)
    
  }


  final def -(that: Array[Double]): RichDouble2DArray = {
    val len = that.length
    val that2d = Array.ofDim[Double](len, 1)
    var k = 0
    while (k<len) {
      that2d(k)(0) = that(k)
      k += 1
    }
    
    this - that2d
  }
  
  
  
  // addition of RichDouble2DArray and an EJML.Mat
 def +( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }

  
  // addition of RichDouble2DArray and an EJML.Mat
 final def +& ( that:  EJML.Mat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

  
  
  // subtraction of an EJML.Mat from a RichDouble1DArray
 def -( that:  EJML.Mat ): EJML.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an EJML.Mat from a RichDouble1DArray
 final def -& ( that:  EJML.Mat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

   
   
  // addition of RichDouble2DArray and an EJML.BMat
 def +( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }

  // addition of RichDouble2DArray and an EJML.BMat
 final def +& ( that:  EJML.BMat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

  
  
  // subtraction of an EJML.BMat from a RichDouble1DArray
 def -( that:  EJML.BMat ): EJML.BMat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new EJML.BMat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an EJML.BMat from a RichDouble1DArray
 final def -& ( that:  EJML.BMat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   
  // MTJ-Mat
    // addition of RichDouble2DArray and an MTJ.Mat
 def +( that:  MTJ.Mat ) = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new MTJ.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }
   new RichDouble2DArray(vr)
 }

  
  
  // MTJ-Mat
    // addition of RichDouble2DArray and an MTJ.Mat
 final def +& ( that:  MTJ.Mat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

  
  // subtraction of an MTJ.Mat from a RichDouble1DArray
 def -( that:  MTJ.Mat ): MTJ.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new MTJ.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }

  // subtraction of an MTJ.Mat from a RichDouble1DArray
 final def -& ( that:  MTJ.Mat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

  
// Apache Common Maths
  
    // addition of RichDouble2DArray and a CommonMaths.Mat
 def +( that:  CommonMaths.Mat ): CommonMaths.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new CommonMaths.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }
  
// Apache Common Maths
  
    // addition of RichDouble2DArray and a CommonMaths.Mat
 final def +& ( that:  CommonMaths.Mat ) = {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)+that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }

  
  // subtraction of a CommonMaths .Mat from a RichDouble1DArray
 def -( that:  CommonMaths.Mat ): CommonMaths.Mat = {
    var   N  =  that.Nrows
    var   M = that.Ncols

   var result = new CommonMaths.Mat(N, M)
   var r=0; var c=0
   while (r < N)  {
      c = 0
      while (c < M)  {
         result(r, c) = -that(r,c) + v(r)(c)
         c += 1
       }
      r += 1
    }
    result
 }


  
  
  // subtraction of a CommonMaths .Mat from a RichDouble1DArray
 final def -& ( that:  CommonMaths.Mat )= {
    val   rN = v.length;   val rM = v(0).length
    val  sN = that.Nrows ; val sM = that.Ncols

  val  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  
  var nthreads = Math.min(numThreads,Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
         vr(r)(c) = v(r)(c)-that(r, c)
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   new RichDouble2DArray(vr)
 }


  
  // slash or right matrix divide
final def /(B: RichDouble2DArray) = this * B.inv()

final def /(B: Array[Array[Double]]) = this * (new RichDouble2DArray(B)).inv()

final def /(B: RichDouble1DArray) = this *(new RichDouble2DArray(B)).inv()

final def /(B: Array[Double]) = this * (new RichDouble2DArray(B)).inv()


  // solves a linear system Ax = b i.e  x = A \ b (as in Matlab)
final def \( b: RichDouble1DArray): RichDouble2DArray = {
  solve(b)
}

final def \(b: RichDouble2DArray): RichDouble2DArray = {
    solve(b)
  }


final def  solve( b: RichDouble1DArray): RichDouble2DArray = {
  var bb = new RichDouble2DArray(b)
  solve(bb)
}

  // solve the system using JLAPACK for overdetermine/undetermined cases
  final def  solve(b: RichDouble2DArray): RichDouble2DArray = {
    if (b.numRows() == b.numColumns)  { // direct solve
      var  luA = new com.nr.la.LUdcmp(this.v)
      var   res = Array.ofDim[Double](b.Nrows, b.Ncols)
      luA.solve(b.v, res)
      new RichDouble2DArray(res)
    }
     new RichDouble2DArray(scalaSci.ILapack.DGELS(this toDoubleArray, b.toDoubleArray))
   }
 

  
  
  final def printAll() = {
val digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
val nrows = Nrows
val ncols = Ncols
var row = 0
while  (row <  nrows) {
      println()
      var col = 0
      while  (col < ncols)  {
        System.out.print( digitFormat.format( v(row)(col))+"  ") 
        col += 1
      }
      row += 1
    }
  }
  
    // updating a single element of the RichDouble2DArray, without resizing
final def  update(n: Int, m: Int, value: Double): Unit = {
         v(n)(m) = value;
   }
  
final def conditionP2( ) = {
  val ejmlA = new scalaSci.EJML.Mat(v)
  ejmlA.conditionP2
  } 

final def conditionP(p: Double) =  {
  val ejmlA = new scalaSci.EJML.Mat(v)
  ejmlA.conditionP(p)
  }
  
final def cond()  = {
  val jmatV = new Jama.jMatrix(v)
  jmatV.cond()
}
  
  
  final def rank() =   scalaSci.math.LinearAlgebra.LinearAlgebra.rank(v)
  
  final def  det() =   scalaSci.math.LinearAlgebra.LinearAlgebra.det(v)
  
  final def inv() = new RichDouble2DArray(scalaSci.math.LinearAlgebra.LinearAlgebra.inverse(v)) 
  
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
 
  final def pinv() =  {
    val ejmlM = new scalaSci.EJML.Mat(this.getv)
    val pejml = ejmlM.pinv
    val nrows = pejml.Nrows
    val ncols = pejml.Ncols
    var pM = new RichDouble2DArray(nrows, ncols)
    var n = 0
    while  (n < nrows) {
      var m = 0
      while (m < ncols) {
        pM(n, m) = pejml(n, m)
        m += 1
      }
      n += 1
    }
    pM
  }
  
  final def trace() = scalaSci.math.LinearAlgebra.LinearAlgebra.trace(v)

  final def nreig() = {
    com.nr.eig.UnsymmeigScala.unsymmeig(this.getv)
  }
  
final def eig() = {
  new scalaSci.EJML.Mat(this.getv).eig()
  }

  // symmetric eigenvalues
final def symmeig() = {
   com.nr.eig.SymmeigScala.symmeig(this.getv, true)  
}

  
final def svd() = {
   val  S  = scalaSci.ILapack.svd(this.getv)
  (new scalaSci.RichDouble2DArray(S._1), new scalaSci.RichDouble1DArray(S._2),  new scalaSci.RichDouble2DArray(S._3))
}
  

  // saves the contents of the matrix to fileName
final def save(fileName: String) = {
  scalaSci.math.io.ioUtils.saveAscii(getv(), fileName)
}

final def read(fileName: String) = {
   v =  scalaSci.math.io.ioUtils.readD2Ascii(fileName)
  }
  
  
final def load(fileName: String) = {
   read(fileName)
}

  
  // Fast JBLAS based routines
  
  // compute the eigenvalues/eigenvectors using JBLAS
  final def feig() = {
     val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
     val eigVals = org.jblas.Eigen.eigenvalues(dm)  // eigenvalues
     val eigVecs = org.jblas.Eigen.eigenvectors(dm)   // eigenvectors
     
     (eigVals, eigVecs)
     }
     
 // compute the  eigenvalues using JBLAS
 final def feigenvalues() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.eigenvalues(dm)
   }
   
  // compute the eigenvectors using JBLAS
  final def feigenvectors() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.eigenvectors(dm)
    }
    
  // compute the symmetric eigenvalues using JBLAS
  final def fsymmetricEigenvalues() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricEigenvalues(dm)
    }
    
  // compute the symmetric eigenvectors using JBLAS
  final def fsymmetricEigenvectors() = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricEigenvectors(dm)
    }
    
 //  Computes generalized eigenvalues of the problem A x = L B x.
// @param A symmetric Matrix A. Only the upper triangle will be considered. Refers to this
//  @param B symmetric Matrix B. Only the upper triangle will be considered.
//  @return a vector of eigenvalues L.
  final def fsymmetricGeneralizedEigenvalues(B: Array[Array[Double]]) = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricGeneralizedEigenvalues(dm, new DoubleMatrix(B))
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
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Eigen.symmetricGeneralizedEigenvectors(dm, new DoubleMatrix(B))
  }
  
    /**
     * Compute Cholesky decomposition of A ( this )
     * @param A should be symmetric, positive definite matrix (only upper half is used)
     * @return upper triangular matrix U such that  A = U' * U
     */
 final def fcholesky() = {
   val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
   org.jblas.Decompose.cholesky(dm)
   }
   
  
/** Solves the linear equation A*X = B , A is this */
final def fsolve(B: Array[Array[Double]]) = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Solve.solve(dm, new DoubleMatrix(B))
  }
  
  
/** Solves the linear equation A*X = B for symmetric A, A is this */
final def fsolveSymmetric(B: Array[Array[Double]]) = {
    val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
    org.jblas.Solve.solveSymmetric(dm, new DoubleMatrix(B))
  }
  
/** Solves the linear equation A*X = B for symmetric and positive definite A */
final def fsolvePositive(B: Array[Double]) = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Solve.solvePositive(dm, new DoubleMatrix(B))
  }
  
  
 /**
     * Compute a singular-value decomposition of A, A is this
     *
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
final def ffullSVD() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.fullSVD(dm)
  }
  
     /**
     * Compute a singular-value decomposition of A (sparse variant), A is this
     * Sparse means that the matrices U and V are not square but
     * only have as many columns (or rows) as possible.
     * 
     * @return A DoubleMatrix[3] array of U, S, V such that A = U * diag(S) * V'
     */
final def fsparseSVD() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.sparseSVD(dm)
  }
  
final def fsparseSVD( Aimag: Array[Array[Double]]) =  {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.sparseSVD(new org.jblas.ComplexDoubleMatrix(dm, new DoubleMatrix(Aimag)))
  }
  
  
  /**
     * Compute the singular values of a matrix.
     *
     * @param A DoubleMatrix of dimension m * n, A is this
     * @return A min(m, n) vector of singular values.
     */
final def fSPDValues() = {
  val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
  org.jblas.Singular.SVDValues(dm)
  }
  
  
    /**
     * Compute the singular values of a complex matrix.
     *
     * @param Areal, Aimag :Areal is this,  the real and imaginary components of a  ComplexDoubleMatrix of dimension m * n
     * @return A real-valued (!) min(m, n) vector of singular values.
     */
 final def fSPDValues(B: Array[Array[Double]]) = {
   val dm = new DoubleMatrix(this.v)   // convert to JBLAS DoubleMatrix
   org.jblas.Singular.SVDValues(new ComplexDoubleMatrix(dm, new DoubleMatrix(B)))
          }
  
  // Reduced-Row Echelon form
  final def rref() = {
    val  xd = this.toDoubleArray
    val  exd  = new org.ejml.data.DenseMatrix64F(xd)
    
    val  reduced = org.ejml.ops.CommonOps.rref(exd, -1, null)
    new RichDouble2DArray(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }
  
  
  
  
   // implement sone transforms using Parallel Colt library
 
    /**
     * Computes the 2D discrete cosine transform (DCT-II) of this matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dct2(scale: Boolean ) = {
    val   dm = new DenseDoubleMatrix2D(getArray)
    dm.dct2(scale)
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the 2D discrete cosine transform (DCT-II) of this matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dctColumns(scale: Boolean) =  {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dctColumns(scale)
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the discrete cosine transform (DCT-II) of each row of this
     * matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dctRows(scale: Boolean) =  {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dctRows(scale)
    new RichDouble2DArray (dm.toArray())
}

/**
     * Computes the 2D discrete Hartley transform (DHT) of this matrix.
     * 
     */
def  dht2() = {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dht2()
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the discrete Hartley transform (DHT) of each column of this
     * matrix.
     * 
     */
def  dhtColumns()  = {
    val   dm = new DenseDoubleMatrix2D(getArray)
    dm.dhtColumns()
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the discrete Hartley transform (DHT) of each row of this
     * matrix.
     * 
     */
def  dhtRows()  = {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dhtRows()
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the 2D discrete sine transform (DST-II) of this matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dst2(scale: Boolean) = {
    val dm = new DenseDoubleMatrix2D(getArray)
    dm.dst2(scale)
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the discrete sine transform (DST-II) of each column of this
     * matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dstColumns(scale: Boolean) =  {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dstColumns(scale)
    new RichDouble2DArray(dm.toArray())
}


    /**
     * Computes the discrete sine transform (DST-II) of each row of this matrix.
     * 
     * @param scale
     *            if true then scaling is performed
     * 
     */
def  dstRows(scale: Boolean)  = {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.dstRows(scale)
    new  RichDouble2DArray(dm.toArray())
}

/**
     * Computes the 2D discrete Fourier transform (DFT) of this matrix. The
     * physical layout of the output data is as follows:
     * 
     * <pre>
     * this[k1][2*k2] = Re[k1][k2] = Re[rows-k1][columns-k2], 
     * this[k1][2*k2+1] = Im[k1][k2] = -Im[rows-k1][columns-k2], 
     *       0&lt;k1&lt;rows, 0&lt;k2&lt;columns/2, 
     * this[0][2*k2] = Re[0][k2] = Re[0][columns-k2], 
     * this[0][2*k2+1] = Im[0][k2] = -Im[0][columns-k2], 
     *       0&lt;k2&lt;columns/2, 
     * this[k1][0] = Re[k1][0] = Re[rows-k1][0], 
     * this[k1][1] = Im[k1][0] = -Im[rows-k1][0], 
     * this[rows-k1][1] = Re[k1][columns/2] = Re[rows-k1][columns/2], 
     * this[rows-k1][0] = -Im[k1][columns/2] = Im[rows-k1][columns/2], 
     *       0&lt;k1&lt;rows/2, 
     * this[0][0] = Re[0][0], 
     * this[0][1] = Re[0][columns/2], 
     * this[rows/2][0] = Re[rows/2][0], 
     * this[rows/2][1] = Re[rows/2][columns/2]
     * </pre>
     * 
     * This method computes only half of the elements of the real transform. The
     * other half satisfies the symmetry condition. If you want the full real
     * forward transform, use <code>getFft2</code>. To get back the original
     * data, use <code>ifft2</code>.
     * 
     * @throws IllegalArgumentException
     *             if the row size or the column size of this matrix is not a
     *             power of 2 number.
     * 
     */
    
def  fft2(scale: Boolean)  = {
    val  dm = new DenseDoubleMatrix2D(getArray)
    dm.fft2()
    new RichDouble2DArray(dm.toArray())
}


}
  
  
  

  // for "shortcut " constructor
object D2D {
  
  /* e.g.
var xx = 3.4
var a =D2D( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/
/// !!!date= 14 Apr
 final def apply(values: Double*)  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   rd2da = Array.ofDim[Double](nrows, ncols)   // the resulting array
    for (r<-0 until nrows)
      for (c<-0 until ncols)
         {
           rd2da(r)(c) = values(cpos)  // copy value
           cpos += 1
         }
    rd2da    // return the constructed Array[Array[Double]]
  }

    
}


// for "shortcut " constructor
object RD2D {
  
  /* e.g.
var xx = 3.4
var a = RD2D( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/
/// !!!date= 31 Mar
 final def apply(values: Double*):scalaSci.RichDouble2DArray  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   rd2da = new RichDouble2DArray(nrows, ncols)   // the resulting array
    var r = 0
    while  (r < nrows) {
      var c = 0
      while  (c < ncols) 
         {
           rd2da(r, c) = values(cpos)  // copy value
           cpos += 1
           c += 1
         }
         r += 1
    }
    rd2da    // return the constructed RichDouble2DArray    
  }

  
  
  
}

  
object RichDouble2DArray {
  var numThreads = ConcurrencyUtils.getNumberOfThreads

  var resizeFactor = 1.5

    
   
final def oneDFloatArray(x: Array[Array[Double]]) = 
  {
    val Nrows = x.length
    val Ncols = x(0).length
    val fa = new Array[Float](Nrows*Ncols)
    var cnt = 0
    var r = 0
    while (r < Nrows)  {
      var c = 0
      while (c < Ncols) {
           fa(cnt) = x(r)(c).asInstanceOf[Float]
           cnt += 1
           c += 1
      }
      r += 1
      
    }
    fa
  }  
  
  // constructs a 1-D representation of the array
  // reoresentation is in row-major order
final def oneDDoubleArray(x: Array[Array[Double]]) = 
  {
    val Nrows = x.length
    val Ncols = x(0).length
    val fa = new Array[Double](Nrows*Ncols)
    var cnt = 0
    var r = 0
    while (r < Nrows)  {
      var c = 0
      while (c < Ncols) {
           fa(cnt) = x(r)(c)
           cnt += 1
           c += 1
      }
      r += 1
      
    }
    fa
  }  

  
final def oneDTransposeDoubleArray(x: Array[Array[Double]]) = 
  {
    val Nrows = x.length
    val Ncols = x(0).length
    val fa = new Array[Double](Nrows*Ncols)
    var cnt = 0
    var c = 0
    while (c < Ncols) {
      var r = 0
      while (r < Nrows)  {
           fa(cnt) = x(r)(c)
           cnt += 1
           r += 1
      }
      c += 1
      
    }
    fa
  }  

  
  // global routine used to display 2-d arrays with toString() that truncates presentation of rows/cols and
  // controls the digits of precision that the numbers are displayed
  final def printArray(a: Array[Array[Double]]) = {
    
    scalaSci.PrintFormatParams.printArray(a)
  }
  
  
  /* e.g.
var xx = 3.4
var a = RichDouble2DArray( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/

  final def apply(values: Double*):scalaSci.RichDouble2DArray  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   rd2da = new RichDouble2DArray(nrows, ncols)   // the resulting array
    var r = 0
    while (r < nrows) {
      var c = 0
      while (c < ncols)
         {
           rd2da(r, c) = values(cpos)  // copy value
           cpos += 1
           c += 1
         }
         r += 1
    }
    rd2da    // return the constructed RichDouble2DArray    
  }
  
  final def   $$( values :Any*) = {
    // count number of nulls, number of nulls will be the number of rows 
    var nullCnt = 0
    for (v <- values)  
       if (v == null) nullCnt+=1

    // count number of columns
     var colCnt = 0
     var vl = values.length
     while (colCnt < vl && values(colCnt) != null)
       colCnt += 1
       
        val rowCnt = nullCnt+1  // number of rows iof the new Matrix
        
        // take the first element.
        // It can be either a Matrix or a double number
        val vv = values(0) 
     if (vv.isInstanceOf[scalaSci.scalaSciMatrix[Any]]) { // we synthesize our Matrix from Matrices
           
           // take parameters of the submatrices
         val vv0 = vv.asInstanceOf[scalaSci.scalaSciMatrix[Any]]
         val nrowsSubm = vv0.numRows()
         val ncolsSubm = vv0.numColumns()
         
     // construct the new Matrix
   val nm = new RichDouble2DArray(rowCnt*nrowsSubm, colCnt*ncolsSubm)
   var cpos = 0
   var r = 0
   while (r <  rowCnt) {
     var c = 0
     while (c < colCnt) 
      {
        var cv = values(cpos)
        if (cv == null) cpos+=1
        cv = values(cpos)
        val crow = r*nrowsSubm
        val ccol = c*ncolsSubm
              
              cv match {
            case null => 
            case v: scalaSci.scalaSciMatrix[Any] =>
              var rs = 0
              while ( rs < nrowsSubm)  {
                var cs = 0
                while (cs < ncolsSubm) {
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
      val nm = new RichDouble2DArray(rowCnt, colCnt)
      var cpos = 0
      var r = 0
      while  (r < rowCnt) {
        var c = 0
        while (c < colCnt)
         {
        var cv = values(cpos)
        if (cv == null) 
           cpos += 1
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





  


