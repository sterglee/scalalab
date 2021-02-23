
package scalaSci

import _root_.scalaSci.math.array.DoubleArray
import _root_.java.util._
  
  import _root_.scalaSci.ComplexMatrix._
  import _root_.scalaSci.StaticMaths.i
  

/* this class provides Matrix operations, for Complex numbers efficiently using a zero-indexed two-dimensional double 
 to keep both the real and imaginary parts of the Complex numbers.
 We avoid to keep each complex number as an object for efficiency reasons.
 The main problem is heap overflow from allocating too many Complex objects for large Complex matrices.
 Also accessing real and imaginary parts from objects is slower (about 40-60%) than accessing arrays.
 Therefore, we store the real parts and the imaginary parts in consecutive locations at the same row of 
 a 2-d Java double array.
 In this way, the access of each complex number involves two operations on consecutive row locations,
 and since Java stores 2-d arrays by rows it is more efficient to use row based storage, than column based.
*/
class ComplexMatrix( n: Int, m: Int)    
{ 
  
// getters for size
  var  Nrows =  n // keeps  the number of rows of the Matrix
  var  Ncols =  m   //  keeps  the number of columns of the Matrix
     // the default constructor allocates a double array of size n x (2*m)
     // therefore, the representation of the data is a two-dimensional Java double array
  var  v = Array.ofDim[Double](Nrows, (Ncols*2))  // i.e. v(0:n-1, 0:m-1)

  def numRows() = Nrows
  def numColumns() = Ncols
  def length() = Nrows*Ncols   // total number of elements
  def size() = (Nrows, Ncols) 
  
  // getv() returns the data representation 
  def getv() = {
    v
}

  // the scalaSci.Mat does not wrap a Matrix class of a specific library, thus return simply the data representation  
def getLibraryMatrixRef() =   v // the scalaSci.Mat does not wrap a Matrix class of a specific library, thus return simply the data representation
def matFromLibrary() = new Mat(v)
def matFromLibrary(v: Array[Array[Double]]) = new Mat(v)
  
def  setv(values: Array[Array[Double]], n: Int, m: Int) = { v = values; Nrows = n; Ncols = m }

//import ComplexMatrix._
  
  // set the corresponding Complex number
def  set(row: Int, column: Int, re: Double, im: Double) =   {  v(row)(2*column) = re;  v(row)(2*column+1) = im }
  
def  get(row: Int, column: Int) =  (v(row)(2*column), v(row)(2*column+1)) 

def this(tuple: (Int, Int)) = this(tuple._1,  tuple._2) 

def this(m: ComplexMatrix) = {
        this(m.Nrows, m.Ncols)
    }

// indexes the corresponding Complex number element  returning a Complex object
  def apply(n: Int, m: Int) = {
        new Complex(v(n)(2*m), v(n)(2*m+1))
 }

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ::) corresponds to Matlab's m(2:4:12, :)'
def apply(rowL: Int, rowInc: Int, rowH: Int, allColsSymbol: scala.::.type): ComplexMatrix = {
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
    var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range
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
     subMatr   // return the submatrix
     }  // positive increment
  else  {  //  negative increment
     var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
     var subMatr = new ComplexMatrix(rowNum, colNum)  // create a Matrix to keep the extracted range
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

  
  def apply(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, allColsSymbol: scala.::.type): ComplexMatrix = {
    var rowL = rowSpec.mStart.inc.toInt
    
    var rowH = rowSpec.mStart.endv.toInt
    
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply(rowL, rowInc, rowH, ::)
  }
  
def apply(rowSpec: scalaSci.Vec, allColsSymbol: scala.::.type): ComplexMatrix = {
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
  apply(rowL, rowInc, rowH, ::)
}   

  
// e.g. var xx = rand0(70); var xx2 = xx(::, 1::2::7)
def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.Vec): ComplexMatrix = {
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt
  apply(::, colL, colsInc, colH)
}   

  // e.g. var xx = rand0(20); var yy2 = xx(::, 1::4)
  def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): ComplexMatrix = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(::, colL, colInc, colH)
  }
  
// extracts a submatrix, e.g. m( ::, 2,  12 ) corresponds to Matlab's m(:, 2:12)'
  def apply(allRowsSymbol:  scala.::.type,  colLow: Int,  colHigh: Int): ComplexMatrix  = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = 1
    var colInc = 1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = colEnd-colStart+1
        var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range
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
    var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range
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
 subMatr // return the submatrix
    }
    
   }



// extracts a submatrix, e.g. m( ::, 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  def apply(allRowsSymbol: scala.::.type, colLow: Int, colInc: Int, colHigh: Int): ComplexMatrix = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range
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
    var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range
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

 // e.g. var xx = rand0(70);  var yy = xx(1::2::8, 2)
def apply(rowsSpec: scalaSci.Vec,  col: Int): ComplexMatrix = {
  var rowsL = rowsSpec(0).toInt
  var vlr = rowsSpec.length
  var rowsInc = (rowsSpec(1)-rowsSpec(0)).toInt
  var rowsH = rowsSpec(vlr-1).toInt
  
  
  apply(rowsL, rowsInc, rowsH, col, 1, col)
}

   // e.g. var xx = rand0(70);  var yy = xx(1,  2::3::9)
def apply(row: Int,  colsSpec: scalaSci.Vec): ComplexMatrix = {
  
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt

  apply(row, 1, row,  colL, colsInc, colH)
}   

  // e.g. var xx = rand0(70);  var yy = xx(1::2::8, 2::3::9)
def apply(rowsSpec: scalaSci.Vec,  colsSpec: scalaSci.Vec): ComplexMatrix = {
  var rowsL = rowsSpec(0).toInt
  var vlr = rowsSpec.length
  var rowsInc = (rowsSpec(1)-rowsSpec(0)).toInt
  var rowsH = rowsSpec(vlr-1).toInt
  
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt

  apply(rowsL, rowsInc, rowsH, colL, colsInc, colH)
}   
  // e.g. var xx = rand0(70);  var yy = xx(1::8, 2)
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, col: Int): ComplexMatrix = {
    var rowL = rowsSpec.mStart.inc.toInt
    var rowH = rowsSpec.mStart.endv.toInt
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply( rowL, rowInc, rowH,  col, 1, col)
  }

  // e.g. var xx = rand0(7);  var yy = xx(1, 2::4)
  def apply(row: Int, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): ComplexMatrix = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(row, 1, row, colL, colInc, colH)
  }

  // e.g. var xx = rand0(20); var yy = xx(2::4, 3::5)
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): ComplexMatrix = {
    var rowL = rowsSpec.mStart.inc.toInt
    var rowH = rowsSpec.mStart.endv.toInt
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(rowL, rowInc, rowH,  colL, colInc, colH)
  }
  
  
  // e.g. var xx = rand0(12); var ff = xx(2::3, 1::2::10)
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.Vec): ComplexMatrix = {
    var rowL = rowsSpec.mStart.inc.toInt
    var rowH = rowsSpec.mStart.endv.toInt
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    
    var colL = colsSpec(0).toInt
    var vl = colsSpec.length
    var colInc = (colsSpec(1)-colsSpec(0)).toInt
    var colH = colsSpec(vl-1).toInt
  
    apply(rowL, rowInc, rowH,  colL, colInc, colH)
  }
  
  
  // e.g. var xx = rand0(12); var gg = xx(2::3::11, 3::7)
def apply(rowsSpec: scalaSci.Vec, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): ComplexMatrix = {
    
   var rowL = rowsSpec(0).toInt
   var vlr = rowsSpec.length
   var rowInc = (rowsSpec(1)-rowsSpec(0)).toInt
   var rowH = rowsSpec(vlr-1).toInt
 
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)    colInc = -1
    apply(rowL, rowInc, rowH,  colL, colInc, colH)
  }
  
  
// extracts a submatrix, e.g. m( 2, 3, 12, 4, 2,  8 ) corresponds to Matlab's m(2:3:12, 4:2:8)'
  def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): ComplexMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range

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


// extracts a submatrix, e.g. m( 2,  12, 4,   8 ) corresponds to Matlab's m(2:12, 4:8)'
  def apply(rowLow: Int,  rowHigh: Int, colLow: Int, colHigh: Int): ComplexMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = if (rowHigh > rowLow) 1 else -1
    var colInc = if (colHigh > colLow) 1 else -1

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new ComplexMatrix(rowNum, colNum)   // create a Matrix to keep the extracted range

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
 subMatr  // return the submatrix
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


              // extracts a submatrix, e.g. m(3:2:7, :)
  def apply(rowLow: Int, rowInc: Int, rowHigh: Int): ComplexMatrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh;    if (rowEnd < rowStart) { rowStart = rowHigh; rowEnd = rowLow; }
    var colStart = 1;     var colEnd =  Ncols-1;
    var colInc = 1
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = new ComplexMatrix( rowNum, colNum)   // create a Matrix to keep the extracted range
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
  def apply(allRowsSymbol: scala.::.type, colIndices: Array[Int]): ComplexMatrix  = {
    var lv = colIndices.length
    if (lv > Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols)
        new ComplexMatrix(1, 1)
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  colFiltered =  new ComplexMatrix(Nrows, lv)
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
    
      colFiltered    // return the column filtered array
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
  def apply(rowIndices: Array[Int], allColsSymbol: scala.::.type): ComplexMatrix = {
    var lv = rowIndices.length
    if (lv > Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows)
        new ComplexMatrix(1, 1)
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  rowFiltered =  new ComplexMatrix(lv, Ncols)
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
    
      rowFiltered    // return the column filtered array
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
  def apply(allRowsSymbol: scala.::.type, colIndices: Array[Boolean]): ComplexMatrix = {
    var lv = colIndices.length
    if (lv != Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols)
        new ComplexMatrix(1, 1)
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
      var  colFiltered =  new ComplexMatrix(Nrows, ntrues)
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
 var testMat = M0(" 1.0 2.0 3.0 ; 5.0 6.0 7.0 ; 8 9 10 ; 11 12 13")
 var rowIndices = Array(false, true, false, true)
 var extract1_3rows = testMat(rowIndices, ::)
   */
  def apply(rowIndices: Array[Boolean], allCols: scala.::.type ): ComplexMatrix = {
    var lv = rowIndices.length
    if (lv != Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows)
        new ComplexMatrix(1, 1)
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
      var  rowFiltered =  new ComplexMatrix(ntrues, Ncols)
      
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

  
  
// construct a ComplexMatrix from a zero-indexed double [][] array
def this(vals: Array[Array[Double]]) = {
     this(vals.length, vals(0).length)  // allocate memory with the default constructor
    var i=0; var j=0
    while (i<vals.length) {
        j=0
        while (j<vals(0).length) {
       v(i)(j) = vals(i)(j)
       j+=1
        }
        i+=1
     }
 }


def this(Nrows: Int, Ncols: Int, df: Double) = {   // initializes to a default value
     this(Nrows, Ncols)  // allocate memory with the default constructor
    var i=0; var j=0
    while (i<v.length) {
        j=0
        while (j<v(1).length) {
       v(i)(j) = df
       j+=1
        }
        i+=1
     }
 }

    // construct a ComplexMatrix from an one-indexed double [][] array
def this(vals: Array[Array[Double]], flag: Boolean) = {
    this(1,1)
    v = vals
    Nrows = vals.length
    Ncols = vals(0).length
 }

  // construct a ComplexMatrix from a one-indexed double [] array
def this( vals: Array[Double]) = {
    this(1, vals.length)   // keep the array as a row of the Matrix
    var i=0
    while (i<vals.length)  {
       v(0)(i) = vals(i)
       i+=1
   }
  }
  
    
def +(that: ComplexMatrix) = {   
    var res = new ComplexMatrix(Nrows, Ncols)
    var i=0; var j=0
    while (i<v.length) {
        j=0
        while (j<v(1).length) {
       res.v(i)(j) =v(i)(j)+that.v(i)(j)
       j+=1
        }
        i+=1
     }
     res
 }


def +(that: Double) = {   
    var res = new ComplexMatrix(Nrows, Ncols)
    var i=0; var j=0
    while (i<v.length) {
        j=0
        while (j<v(1).length) {
       res.v(i)(j) =v(i)(j)+that
       j+=2
        }
        i+=1
     }
     res
 }

def -(that: Double) = {   
    var res = new ComplexMatrix(Nrows, Ncols)
    var i=0; var j=0
    while (i<v.length) {
        j=0
        while (j<v(1).length) {
       res.v(i)(j) =v(i)(j)-that
       j+=2
        }
        i+=1
     }
     res
 }
  
  
def *(that: Double) = {   
    var res = new ComplexMatrix(Nrows, Ncols)
    var i=0; var j=0
    while (i<v.length) {
        j=0
        while (j<v(1).length) {
       res.v(i)(j) =v(i)(j)*that
       j+=1
        }
        i+=1
     }
     res
 }
  
  
def * (that: ComplexMatrix): ComplexMatrix =  {
   var  rN = this.Nrows;   var rM = this.Ncols;
   var  sN = that.Nrows;  var sM = that.Ncols;
   
   var result = new ComplexMatrix(this.Nrows, that.Ncols)
   var r = 0; var c = 0; var j = 0
   while (r < rN) {
       c = 0
       while (c < sM) {
           var sm = new Complex(0.0, 0.0)
           j = 0
           while (j < rM) {
               sm =  sm + this(r, j)*that(j, c)
               j += 1
               }
            result(r, c ) = sm   
            c += 1
          }   
        r += 1
        }  
   
  return result
  }
   
  
override  def  toString(): String = {
    import java.text.DecimalFormat
    import java.text.DecimalFormatSymbols
    import java.util._
    import scalaSci.PrintFormatParams._
    
 if  (scalaSci.PrintFormatParams.getVerbose==true)  {
    var formatString = "0."
    for (k<-0 until  matDigitsPrecision)
       formatString += "0"
    var digitFormat = new DecimalFormat(formatString)
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")))
     
    var rowsToDisplay = v.length
    var colsToDisplay = v(0).length
    var truncated = false

    var truncationIndicator = ""
    if  ( matMxRowsToDisplay < rowsToDisplay ) {
        rowsToDisplay = matMxRowsToDisplay
        truncationIndicator = " ... "
        truncated = true
      }
     if  (matMxColsToDisplay < colsToDisplay) {
        colsToDisplay  = matMxColsToDisplay
        truncationIndicator = " ... "
      }
     var i=0; var j=0;
     var sb = new StringBuilder("\n")
    while (i < rowsToDisplay) {
        j=0
        while (j < colsToDisplay ) {
            if (v(i)(j+1) >= 0)
       sb.append(digitFormat.format(v(i)(j))+"+"+digitFormat.format(v(i)(j+1))+"i  ")
        else
       sb.append(digitFormat.format(v(i)(j))+digitFormat.format(v(i)(j+1))+"i  ")
       
       j+=2
        }
      sb.append(truncationIndicator+"\n")
     i+=1
    }
   if (truncated)     // handle last line
    sb.append( ".........................................................................................................................................")

     sb.toString
 }
 else ""
  }
  
    // updating a single element of the Complex Matrix
def  update(n: Int, m: Int, cn: Complex) {
         v(n)(2*m) = cn.re
         v(n)(2*m+1) =  cn.im 
     }

   def  update(n: Int, m: Int, cn: Array[Double]) {
         v(n)(2*m) = cn(0)
         v(n)(2*m+1) =  cn(1) 
     }
   
   


    def  cos() =  {
        var cosMat = new ComplexMatrix(Nrows, Ncols)  // the result matrix
      var r = 0; var c = 0
      while (r < Nrows) {
          c = 0
          while (c < Ncols) {
              var re = v(r)(2*c)  // the real part
              var im = v(r)(2*c+1)  // the imaginary part
              var newre =  Math.cos(re) * Math.cosh(im)
              var newim = -Math.sin(re) * Math.sinh(im)
              cosMat.v(r)(2*c) = newre
              cosMat.v(r)(2*c+1) = newim
              c += 1
              }
              r+=1
            }
        cosMat    
              
    }

  def sqrt() = this map scalaSci.Complex.sqrt
  def abs() = this mapd scalaSci.Complex.abs
  def sin() = this map scalaSci.Complex.sin
  def asin() =  this map scalaSci.Complex.asin
  def sinh() = this map scalaSci.Complex.sinh
  def acos() = this map scalaSci.Complex.acos
  def cosh() = this map scalaSci.Complex.cosh
  def tan() = this map scalaSci.Complex.tan
  def atan() = this map scalaSci.Complex.atan
  def tanh() = this map scalaSci.Complex.tanh
  def sqrt1z() = this map scalaSci.Complex.sqrt1z
  def exp() = this map scalaSci.Complex.exp
  def log() = this map scalaSci.Complex.log
  def getArg() = this mapd scalaSci.Complex.getArg
  def getArgument() = this mapd scalaSci.Complex.getArgument
  
  
  def map(f : Complex => Complex) = {
        var resMat = new ComplexMatrix(Nrows, Ncols)  // the result matrix
      var r = 0; var c = 0
      while (r < Nrows) {
          c = 0
          while (c < Ncols) {
            resMat(r, c) = f(this(r, c) )
            c += 1
            }
            r += 1
            }
  resMat
     }

      def mapd(f : Complex => Double) = {
        var resMat = new ComplexMatrix(Nrows, Ncols)  // the result matrix
      var r = 0; var c = 0
      while (r < Nrows) {
          c = 0
          while (c < Ncols) {
            resMat.v(r)(2* c) = f(this(r, c) )
            resMat.v(r)(2* c+1) = 0.0
            c += 1
            }
            r += 1
            }
  resMat
     }
     
  
  // get the real part of this ComplexMatrix
def real() = {
  val Nr = this.Nrows
  val Nc = this.Ncols
  var dm = new RichDouble2DArray(Nr, Nc)
  var r = 0; var c = 0
  while ( r < Nr ) {
    c = 0
    while ( c < Nc ) {
      dm(r, c) = this.v(r)(2*c)  // get real part
      c+= 1
    }
    r += 1
  }
  dm
}

  // get the imaginary part of this ComplexMatrix
def imag() = {
  val Nr = this.Nrows
  val Nc = this.Ncols
  var dm = new RichDouble2DArray(Nr, Nc)
  var r = 0; var c = 0
  while ( r < Nr ) {
    c = 0
    while ( c < Nc ) {
      dm(r, c) = this.v(r)(2*c+1)  // get real part
      c+= 1
    }
    r += 1
  }
  dm
}
     
    }
    

     

// ComplexMatrix's companion object
  object ComplexMatrix  {

 var digitsPrecision = 4  // controls pecision in toString()  
 var mxRowsToDisplay = 6  
 var mxColsToDisplay = 6
 
  def setDigitsPrecision(n: Int) = {digitsPrecision = n}
  def setRowsToDisplay(nrows: Int) = {mxRowsToDisplay = nrows }
  def setColsToDisplay(ncols: Int) = {mxColsToDisplay = ncols }
  
  
 
  // a conveniency constructor that allows to construct a matrix e.g. as
  //   var x = ComplexMatrix(3,7) instead of  var x = new ComplexMatrix(3, 7)
 def apply(nrows: Int, ncols: Int) = new ComplexMatrix(nrows, ncols) 
  
    /* e.g.
var xx = 3.4
var a = ComplexMatrix( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/    
/// !!!date= 29 Mar
 def apply(values: Double*)  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   sm = new ComplexMatrix( nrows, ncols)  // create a Mat
    for (r<-0 until nrows)
      for (c<-0 until ncols)
         {
           sm.v(r)( c) = values(cpos)  // copy value
           cpos += 1
         }

    sm  // return the constructed matrix

  }
  
  
}

