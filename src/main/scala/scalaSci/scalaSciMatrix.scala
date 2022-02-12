
/*
Defines and enforces  the basic functionality of all scalaSci Matrix types.
It also implements the common patterns of functionality for all matrix types.

The user should use these routines in order to have portable scalaSci code, 
that can directly use different lower-level matrix libraries.
 */
package scalaSci

// use to implement multithreaded operations
import scalaExec.Interpreter.GlobalValues
import java.util.concurrent.Future
import edu.emory.mathcs.utils.ConcurrencyUtils


import scala.language.postfixOps
// specificMatrix is the type of the matrix for which the trait is mixed-in,
// in order to define the functionality, e.g. scalaSci.Mat or scalaSci.EJML.Mat
trait  scalaSciMatrix[specificMatrix]  {
      var   Nrows: Int   // keeps  the number of rows of the Matrix
      var   Ncols: Int  //  keeps  the number of columns of the Matrix

     val LargeMatrix = 100
    final val thisObj = this   // keeps a reference to the object where the scalaSciMatrix trait is fixed in
    def numRows(): Int // returns the number of rows
    def numColumns(): Int // returns the number of columns
     
      /* 
       getv:  returns the low-level data representation of the Matrix, whatever it is.
        Common representations are for example, a one-dimensional array of doubles arranged 
        in either row-storage format or column-storage format, or a two-dimensional array of doubles
  */    
   def getv: AnyRef
      
      //  getLibraryMatrixRef:  returns the library dependent class that implements native operations.
      //  This allows ScalaLab code to combine Scala implemented operations, with the existing 
      //  native operations provided by the Java library. In this way the full potential of the 
      //  underlying Java class can be utilized
      def getLibraryMatrixRef:  AnyRef   

      //  return the matrix from the native representation whatever the native representation is. In that way we can process the native representation 
      //  with the methods of the native library and after that take the updated matrix class
      def matFromLibrary(): specificMatrix
      
      def length(): Int  // returns the length of each Matrix object, i.e. the number of its elements
    
      def size(): (Int, Int)  // returns the number of rows and columns, for banded matrix returns the lower-band width
                                //  and upper band width
     
     
     def copy(): specificMatrix   // makes a copy of the Matrix . It is not implemented in general terms 
                         //  in order to allow efficient implementations that exploit the low-level storage format of a particular matrix
  
  // copy to a new matrix, perhaps resizing also the matrix
     def copy(newNrows: Int, newNcols: Int): specificMatrix 
  
  //  the abstract apply and update methods. 
  //  the implementation of these methods depends on  the particular matrix storage format of each library
  //  each zero-indexed scalaSci matrix type that mixes-in the trait scalaSciMatrix[specificMatrix] 
  //  is enforced by the compiler to provide concrete implementations for these two abstract methods
  def apply(r:Int, c: Int): Double   //  gets the element at row r and column c. 
  def  update(r: Int, c: Int, value: Double): Unit   // sets the element at row r and column c to the corresponding value 
 
  //  the generic apply() methods follow, that are implemented in terms of the two abstract apply and update methods (defined above)
  //  the functionality of the abstract methods is defined by the patricular matrix type that mixes-in the scalaSciMatrix[specificMatrix] trait
  
// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ::) corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummySymbol). if low>high then rows are returned in reverse
 def apply(rowL: Int, rowH: Int, allColsSymbol: scala.::.type): specificMatrix  = {
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

def apply(rowSpec: scalaSci.Vec, allColsSymbol: scala.::.type): specificMatrix = {
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
  apply(rowL, rowInc, rowH, ::)
}   

  def apply(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, allColsSymbol: scala.::.type): specificMatrix = {
    var rowL = rowSpec.mStart.inc.toInt
    
    var rowH = rowSpec.mStart.endv.toInt
    
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply(rowL, rowInc, rowH, ::)
  }
  
// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ::) corresponds to Matlab's m(2:4:12, :)'
def apply(rowL: Int, rowInc: Int, rowH: Int, allColsSymbol: scala.::.type): specificMatrix = {
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


// e.g. var xx = rand0(70); var xx2 = xx(::, 1::2::7)
def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.Vec): specificMatrix = {
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt
  apply(::, colL, colsInc, colH)
}   

  // e.g. var xx = rand0(20); var yy2 = xx(::, 1::4)
  def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): specificMatrix = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(::, colL, colInc, colH)
  }
  
// extracts a submatrix, e.g. m( ::, 2,  12 ) corresponds to Matlab's m(:, 2:12)'
  def apply(allRowsSymbol:  scala.::.type,  colLow: Int,  colHigh: Int): specificMatrix  = {
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
    var colNum = colStart-colEnd+1
    var subMatr = scalaSci.MatrixFactory(this, rowNum, colNum)   // create a Matrix to keep the extracted range
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
 subMatr.asInstanceOf[specificMatrix]   // return the submatrix
    }
    
   }



// extracts a submatrix, e.g. m( ::, 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  def apply(allRowsSymbol: scala.::.type, colLow: Int, colInc: Int, colHigh: Int): specificMatrix = {
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

 // e.g. var xx = rand0(70);  var yy = xx(1::2::8, 2)
def apply(rowsSpec: scalaSci.Vec,  col: Int): specificMatrix = {
  var rowsL = rowsSpec(0).toInt
  var vlr = rowsSpec.length
  var rowsInc = (rowsSpec(1)-rowsSpec(0)).toInt
  var rowsH = rowsSpec(vlr-1).toInt
  
  
  apply(rowsL, rowsInc, rowsH, col, 1, col)
}

   // e.g. var xx = rand0(70);  var yy = xx(1,  2::3::9)
def apply(row: Int,  colsSpec: scalaSci.Vec): specificMatrix = {
  
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt

  apply(row, 1, row,  colL, colsInc, colH)
}   

  // e.g. var xx = rand0(70);  var yy = xx(1::2::8, 2::3::9)
def apply(rowsSpec: scalaSci.Vec,  colsSpec: scalaSci.Vec): specificMatrix = {
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
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, col: Int): specificMatrix = {
    var rowL = rowsSpec.mStart.inc.toInt
    var rowH = rowsSpec.mStart.endv.toInt
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply( rowL, rowInc, rowH,  col, 1, col)
  }

  // e.g. var xx = rand0(7);  var yy = xx(1, 2::4)
  def apply(row: Int, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): specificMatrix = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(row, 1, row, colL, colInc, colH)
  }

  // e.g. var xx = rand0(20); var yy = xx(2::4, 3::5)
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): specificMatrix = {
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
def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.Vec): specificMatrix = {
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
def apply(rowsSpec: scalaSci.Vec, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): specificMatrix = {
    
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


// extracts a specific row, take all columns, e.g. m(2, ::) corresponds to Matlab's m(2, :)'
  def apply(row: Int, allColsSymbol: scala.::.type): RichDouble1DArray = {
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
  def apply(allRowsSymbol: scala.::.type, col: Int): RichDouble1DArray = {
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
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(3, 1)
 var extract3_1cols = testMat(::, colIndices)
   */
  def apply(allRowsSymbol: scala.::.type, colIndices: Array[Int]): specificMatrix  = {
    var lv = colIndices.length
    if (lv > Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  colFiltered =  scalaSci.MatrixFactory(this, Nrows, lv)
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
    
      colFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
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
  def apply(rowIndices: Array[Int], allColsSymbol: scala.::.type): specificMatrix = {
    var lv = rowIndices.length
    if (lv > Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  rowFiltered =  scalaSci.MatrixFactory(this, lv, Ncols)
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
    
      rowFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
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
  def apply(allRowsSymbol: scala.::.type, colIndices: Array[Boolean]): specificMatrix = {
    var lv = colIndices.length
    if (lv != Ncols)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
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
      var  colFiltered =  scalaSci.MatrixFactory(this, Nrows, ntrues)
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
    
      colFiltered.asInstanceOf[specificMatrix]    // return the column filtered array
      
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
  def apply(rowIndices: Array[Boolean], allCols: scala.::.type ): specificMatrix = {
    var lv = rowIndices.length
    if (lv != Nrows)  // do nothing
      {
        println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows)
        scalaSci.MatrixFactory(this, 1, 1).asInstanceOf[specificMatrix]
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
      var  rowFiltered =  scalaSci.MatrixFactory(this,  ntrues, Ncols)
      
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
        rowFiltered.asInstanceOf[specificMatrix]
      }  // dimension of array with row indices to use is correct 
          
    }
    
  
  // the common functionality  for the matrix assignment operations of all zero-indexed scalaSci matrix types
  // is defined with the following update() methods  
  
  // update a single row with index r to have the value v
  // e.g. val x = rand0(3, 4);  x(-1, ::) = 20   ; // set the last row to 20s
  //  x(1, ::)=22.2   // set row 1 to 22.2
 def update(r: Int, colonSymbol:  scala.::.type, v: Double)   {
   val  row  =  if ( r < 0)  Nrows+r else r   // allow negative indices to take higher numbered rows
   var i = 0
   while (i < Ncols) {
     this(row, i) = v
     i += 1
   }
 }
  
  
  // update a single column with index c  to have the value v
  // e.g.  val x = rand0(3, 4);  x(::, -1) = 30    // set the last column to 30s
 def update( colonSymbol:  scala.::.type,  c: Int, v: Double)   {
   val  col  =  if ( c < 0)  Ncols+c else c   // allow negative idices to take higher numbered columns
   var i = 0
   while (i < Nrows) {
     this(i, col) = v
     i += 1
   }
 }
 
  // update a matrix range by assigning to it a single double value
 def update(rs: Int, re: Int, cs: Int, ce: Int, v: Double ): Unit =   {
   var ri = rs // row index
   while (ri <= re)  {
        var ci =cs   // column index
        while (ci <= ce) {
           this(ri, ci) = v
           ci += 1
        }
        ri += 1
   }
 } 
  
  // e.g.  val x = rand0(9); x(2::3, 1::2)=88
 def update(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, colSpec: scalaSci.MatlabRange.MatlabRangeNext, v: Double): Unit = {
   var rowL = rowSpec.mStart.inc.toInt
   var rowH = rowSpec.mStart.endv.toInt
   var colL = colSpec.mStart.inc.toInt
   var colH = colSpec.mStart.endv.toInt   
   update(rowL, rowH, colL, colH, v)
}   

  // e.g. var x = rand0(20); x(3::2::7, 2::10)= 50.4
  def update(rowSpec: scalaSci.Vec, colSpec: scalaSci.MatlabRange.MatlabRangeNext, v: Double): Unit = {
    var rowL = rowSpec(0).toInt
    var vl = rowSpec.length
    var rowInc = (rowSpec(1)-rowSpec(0)).toInt
    var rowH = rowSpec(vl-1).toInt
    var colL = colSpec.mStart.inc.toInt
    var colH = colSpec.mStart.endv.toInt
    var incC = 1
    if (colH < colL)   incC = -1
    update(rowL, rowInc, rowH, colL, incC, colH, v)
  }
  
  
  // e.g. var x = rand0(20); x(1::6, 2::2::6)= 150.4
  def update(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, colSpec: scalaSci.Vec, v: Double): Unit = {
    var colL = colSpec(0).toInt
    var vl = colSpec.length
    var colInc = (colSpec(1)-colSpec(0)).toInt
    var colH = colSpec(vl-1).toInt
    var rowL = rowSpec.mStart.inc.toInt
    var rowH = rowSpec.mStart.endv.toInt
    var incR = 1
    if (rowH < rowL)  incR = -1
    update(rowL, incR, rowH, colL, colInc, colH, v)
  }
    
  //  e.g.  var x = rand0(20);  x(1::2::12, 2::2::14)= 5
 def update(rowSpec: scalaSci.Vec, colSpec: scalaSci.Vec, v: Double): Unit = {
     
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
     
  var colL = colSpec(0).toInt
  var v2 = colSpec.length
  var colInc = (colSpec(1)-colSpec(0)).toInt
  var colH = colSpec(v2-1).toInt

    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
  
 // e.g. var x = rand(6); x(0::2::4, ::) = 9.8
  def update(rowSpec: scalaSci.Vec, colonSymbol: scala.::.type, v: Double): Unit = {
     
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
     
  var colL = 0
  var colInc = 1
  var colH = numColumns()-1

    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
  
 // e.g. var x = rand(6); x(0::2::4, 1) = 9.8
  def update(rowSpec: scalaSci.Vec, col: Int, v: Double): Unit = {
     
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
     
  var colL = col
  var colInc = 1
  var colH = col

    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
  
 // e.g.  var x = rand(6); x(1, 0::2::4) = 22.8
  def update(row: Int, colSpec: scalaSci.Vec, v: Double): Unit = {
    var rowL = row
    var rowInc = 1
    var rowH = row
   
   var colL = colSpec(0).toInt
   var vl = colSpec.length
   var colInc = (colSpec(1)-colSpec(0)).toInt
   var colH = colSpec(vl-1).toInt
    
    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
 // e.g. var x = rand(6); x(1, 0::4) = 9.8
  def update(row: Int, colSpec: scalaSci.MatlabRange.MatlabRangeNext, v: Double): Unit = {
    var rowL = row
    var rowInc = 1
    var rowH = row
 
   var colL = colSpec.mStart.inc.toInt
   var colInc = 1
   var colH = colSpec.mStart.endv.toInt   
    
    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
 // e.g. var x = rand(6); x(0::4, 1) = 9.8
  def update(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, col: Int, v: Double): Unit = {
    var colL = col
    var colInc = 1
    var colH = col
 
   var rowL = rowSpec.mStart.inc.toInt
   var rowInc = 1
   var rowH = rowSpec.mStart.endv.toInt   
    
    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
  
// e.g. var x = rand(8); x(::, 1::2::7) = 99.9
  def update(rowSymbol: scala.::.type, colSpec: scalaSci.Vec, v: Double): Unit = {
    var rowL = 0
    var rowInc = 1
    var rowH = numRows()-1
   
   var colL = colSpec(0).toInt
   var vl = colSpec.length
   var colInc = (colSpec(1)-colSpec(0)).toInt
   var colH = colSpec(vl-1).toInt
     
    update(rowL, rowInc, rowH, colL, colInc, colH, v)
 }
 
  
  def update(rowL: Int, incr: Int, rowH: Int, colL: Int, incc: Int, colH: Int, v: Double): Unit = {
   var r = rowL; var c = colL
   while (r <= rowH) {
     c = colL
     while (c <= colH) {
       this(r, c) = v
       c += incc
     }
     r += incr
   }
  
 }
 
  
  
 
  
 // e.g. var g = rand(9); g(::, 1::7)= 77.7 
 def update(rowSymbol: scala.::.type, colSpec: scalaSci.MatlabRange.MatlabRangeNext, v: Double): Unit = {
   var colL = colSpec.mStart.inc.toInt
   var colH = colSpec.mStart.endv.toInt   
   var rowL = 0
   var rowH = numRows-1
    update(rowL, rowH,  colL, colH, v)
}   

  // e.g. var x = rand0(8); x(2::3, ::) = 33.33
  def update(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, colonSymbol: scala.::.type,  v: Double): Unit = {
   var rowL = rowSpec.mStart.inc.toInt
   var rowH = rowSpec.mStart.endv.toInt
   var colL = 0
   var colH = numColumns-1
    update(rowL, rowH, colL, colH,  v)
}   

  
def update( rangeSymbol: scala.::.type, rs: Int, re: Int, cs: Int, ce: Int, v: Double ): Unit =
  update(rs, re, cs, ce, v)
    
def update( rs: Int, re: Int, cs: Int, ce: Int,  rangeSymbol: scala.::.type, v: Double): Unit =
  update(rs, re, cs, ce, v)

  
  // update the column c with the contents of the vector v
  // e.g.  val x = rand0(4, 5); val v = vones(4); x(::, 2) = v 
 def update(colonSymbol: scala.::.type, c: Int, v: Vec)  {
    var  vl = v.length
    if (vl > Nrows)
      vl = Nrows
     val col = if (c < 0) Ncols + c  else c
   var row = 0
   while (row < vl)  {
     this(row, col) = v(row)
     row += 1
   }
 }
 
  // update the row r with the contents of the vector v
  // e.g.  val x = rand0(4, 5);  val v = vones(5);  x(3,  ::) = v
 def update(r: Int, colonSymbol: scala.::.type, v: Vec)  {
   var vl = v.length
    if (vl > Ncols)
     vl = Ncols
   val row = if (r < 0) Nrows + r else r
   var col = 0
   while (col < vl) {
     this(row, col) = v(col)
     col += 1
   }
 }

  // update the column c with the contents of the array v
  // e.g.  val x = rand0(4, 5); val v = vones(4); x(::, 2) = v 
 def update(colonSymbol: scala.::.type, c: Int, v: Array[Double])  {
    var  vl = v.length
    if (vl > Nrows)
      vl = Nrows
     val col = if (c < 0) Ncols + c  else c
   var row = 0
   while (row < vl)  {
     this(row, col) = v(row)
     row += 1
   }
 }
 
  // update the row r with the contents of the array  v
  // e.g.  val x = rand0(4, 5);  val v = vones(5);  x(3,  ::) = v
 def update(r: Int, colonSymbol: scala.::.type, v: Array[Double])  {
   var vl = v.length
    if (vl > Ncols)
     vl = Ncols
   val row = if (r < 0) Nrows + r else r
   var col=0
   while (col < vl) {
     this(row, col) = v(col)
     col += 1
   }
 }

// update a Matrix subrange by assigning a Matrix, starting from m(rlowp, clowp) and
// copying the elements of matrix mr, every rincp rows and every cincp columns  
/*
val m=rand0(20, 30); 
m( 2, 1, 2, 3) = ones1(2,2)    // start from m(2,1), every 2 rows and every 3 cols
 */
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



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand(20, 30);  mm(2, 3, ::) = ones1(2,2);
 def update(rlowp:Int, clowp:Int, ch: scala.::.type, mr: Matrix): Unit = {
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

  
  
// update a Matrix subrange by assigning a zero-indexed matrix, starting from m(rlowp, clowp) and
// copying the elements of matrix mr, every rincp rows and every cincp columns  
/*
val m=rand0(20, 30); 
m( 2, 1, 2, 3) = ones(2,2)    // start from m(2,1), every 2 rows and every 3 cols
 */
 def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: scalaSciMatrix[specificMatrix] ): Unit = {
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



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand0(20, 30);  mm(2, 3, ::) = ones0(2,2);
 def update(rlowp:Int, clowp:Int, ch: scala.::.type, mr: scalaSciMatrix[specificMatrix]): Unit = {
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


  
// update a Matrix subrange by assigning a RichDouble2DArray , e.g. var m=rand0(20, 30);  m( 2, 1, 2, 1) = ones(2,2)
 def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: RichDouble2DArray ): Unit = {
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



// update a Matrix subrange by assigning a RichDouble2DArray, e.g. var mm = rand0(20, 30);  mm(2, 3, ::) = ones(2,2)
 def update(rlowp:Int, clowp:Int, ch: scala.::.type, mr: RichDouble2DArray): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rlow=rlowp; var rhigh=rlow+mrM; var rinc = 1;
    var clow=clowp; var chigh=clow+mrN; var cinc = 1;
        
     if (rhigh >= Nrows || chigh >= Ncols)  {   
          println("accessing out of range element")
          return
      }   // dynamically increase the size of the matrix


     // copy the values of the mr
        var rrow = 0; var rcol = 0; var lrowidx = rlow; var lcolidx = clow
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
// e.g.  var x = rand0(300, 400); var firstRowRandVector = x.getRow(0); plot(firstRowRandVector)  
def getRow(row: Int): Array[Double] = {
    var rowArray = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
       rowArray(ccol) = this(row, ccol)
       ccol += 1
     }
    rowArray
  }

  
//  returns the corresponding column of the Mat class as an Array[Double]
//  e.g.  var x = rand0(120, 230); var thirdColumnRandVector = x.getCol(2); plot(thirdColumnRandVector)  
def getCol(col: Int): Array[Double] = {
    var colArray = new Array[Double](Nrows)
    var rrow = 0
    while  (rrow < Nrows) {
       colArray(rrow) = this(rrow, col)
       rrow += 1
      }
    colArray
  }
  
  /* an alternative way to apply a function using structural typing, e.g.
  
var N = 4000
var x = rand0(N,N)

object  compute {
    def f(x: Double) = x+x+x
    }

def fx(x: Double) = x+x+x

tic
var y = x.mapf(compute)
var tmc = toc

tic
var y2 = x.map(fx)
var tmmap = toc
*/
 def mapf( mapperClass: { def f(x: Double): Double }): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
    var r = 0
    while (r < Nrows)  {
      var c = 0
      while (c < Ncols)  {
         mres(r, c) = mapperClass.f(this(r, c) )
         c += 1
       }
     r += 1
    }
   mres.asInstanceOf[specificMatrix]
 } 
 
  // apply the function f to all the elements of the Matrix and return the results with a new Matrix
  // e.g. val  x = ones0(4, 6); val y = x map sin   // return a new matrix with the sines of all matrix elements 
 def  map( f: (Double => Double)): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
   
    var r = 0
    while (r < Nrows)  {
      var c = 0
      while (c < Ncols)  {
         mres(r, c) = f(this(r, c) )
         c += 1
       }
     r += 1
    }
   mres.asInstanceOf[specificMatrix]
 }
 
  
  // apply the function f to all the elements of the Matrix in-place
  // e.g. val  x = ones0(4, 6);  x mapi sin  //  apply the sin function to all the matrix elements
 def  mapi( f: (Double => Double)): specificMatrix = {
    var r = 0
    while ( r < Nrows) {
      var c = 0
      while  (c < Ncols)  {
        this(r, c) = f(this(r, c) )
        c += 1
      }
     r += 1
    }
   
   this.asInstanceOf[specificMatrix]
 }
 
  
 // parallel map in place: map the function f to each element of the matrix using multithreading
 // pmap is faster than map only for large matrices
def pmapi(f: (Double => Double)) = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = f(thisObj(r, c) )
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

  // Parallel operations
  
  def  psin(): specificMatrix = {
    
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.sin(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pcos(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.cos(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  def  ptan(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.tan(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  psinh(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.sinh(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pcosh(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.cosh(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  ptanh(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.tanh(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pasin(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.asin(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pacos(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.acos(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  patan(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.atan(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  def  pfloor(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.floor(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pceil(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.ceil(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  pround(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.round(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  def  pabs(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.abs(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  psqrt(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.sqrt(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  ppow(v: Double): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.pow(thisObj(r, c), v )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}
     
  def  pexp(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.exp(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}
     
  
  def  plog(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.log(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  
  
  def  plog2(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
   val conv = java.lang.Math.log(2.0)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.log(thisObj(r, c)/conv )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  def  plog10(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.log10(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  def  ptoDegrees(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.toDegrees(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  def  ptoRadians(): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
  
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = Math.toRadians(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}

  
  // Parallel in-place operations
  
  def pisin() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.sin(thisObj(r, c) )
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

  def picos() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.cos(thisObj(r, c) )
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

  def pitan() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.tan(thisObj(r, c) )
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

  def pitanh() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.tanh(thisObj(r, c) )
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

  def pisinh() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.sinh(thisObj(r, c) )
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

  
  def picosh() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.cosh(thisObj(r, c) )
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

  
  def pisqrt() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.sqrt(thisObj(r, c) )
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

  def piabs() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.abs(thisObj(r, c) )
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

  def piasin() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.asin(thisObj(r, c) )
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

  
  def piacos() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.acos(thisObj(r, c) )
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

  def piatan() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.atan(thisObj(r, c) )
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

  def piceil() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.ceil(thisObj(r, c) )
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

  def pifloor() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.floor(thisObj(r, c) )
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



  def piround() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.round(thisObj(r, c) )
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

  
def pipow(v: Double) = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.pow(thisObj(r, c), v )
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

def pilog() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.log(thisObj(r, c) )
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

def pilog2() = {
   val conv = java.lang.Math.log(2.0)

  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.log(thisObj(r, c)/conv )
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
  
def pilog10() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.log10(thisObj(r, c) )
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

  
def pitoDegrees() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.toDegrees(thisObj(r, c) )
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

  
def pitoRadians() = {
   
  var nthreads = GlobalValues.numOfThreads
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
         thisObj(r, c) = Math.toRadians(thisObj(r, c) )
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

 // parallel map in place: map the function f to each element of the matrix using multithreading
 // pmap is faster than map only for large matrices
def pmap(f: (Double => Double)): specificMatrix = {
   var mres = scalaSci.MatrixFactory(this, Nrows, Ncols)
   
  var nthreads = GlobalValues.numOfThreads
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
         mres(r, c) = f(thisObj(r, c) )
        c += 1
      }
      r += 1
      }
    }
 })

            threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

   mres.asInstanceOf[specificMatrix]
}


  // removes any rows containing an NaN element
 def removeRowsNaN() = {
  var rowHasNaN = new Array[Boolean](Nrows)  // marks rows containing at least one NaN
  var k=0; while (k < Nrows) { rowHasNaN(k) = false; k+=1 }
  
  // scan each row to see if it has an NaN
  var currentRow = 0
  var RowsWithoutNaN = Nrows   // currently assume that all rows have only defined values
  while (currentRow < Nrows) {
    var hasNaN = false
    var currentCol = 0
    while (currentCol < Ncols && hasNaN == false) {
      if (this(currentRow, currentCol).isNaN) {
        hasNaN = true
        rowHasNaN(currentRow) = true
        RowsWithoutNaN -= 1
      }
      currentCol += 1
     }
     currentRow += 1
   }
  
    // create now the new array that will not contain the NaN values
   var  definedValuesArray = Array.ofDim[Double](RowsWithoutNaN, Ncols)
   
    // copy the defined values
   currentRow = 0; 
   var drow = 0; var dcol = 0 // indexing to definedValuesArray
   while (currentRow < Nrows)  {
     if (rowHasNaN(currentRow) == false) { // row with no NaN values
        dcol = 0
        while (dcol < Ncols) {
            definedValuesArray(drow)(dcol) = this(currentRow, dcol)
            dcol += 1
           }
        
          drow += 1
     }  
       currentRow += 1
     
    } // row with no NaN values
        
      
      new RichDouble2DArray(definedValuesArray)
      
     
}
   


  // removes any rows and columns containing an NaN element
def removeNaN() = {
  var rowHasNaN = new Array[Boolean](Nrows)  // marks rows containing at least one NaN
  var k=0; while (k < Nrows) { rowHasNaN(k) = false; k+=1 }
  
  var colHasNaN = new Array[Boolean](Ncols)  // marks columns containing at least one NaN
  k=0; while (k<Ncols) { colHasNaN(k) = false; k+=1 }
  
  // scan each row to see if it has an NaN
  var currentRow = 0
  var RowsWithoutNaN = Nrows   // currently assume that all rows have only defined values
  while (currentRow < Nrows) {
    var hasNaN = false
    var currentCol = 0
    while (currentCol < Ncols && hasNaN == false) {
      if (this(currentRow, currentCol).isNaN) {
        hasNaN = true
        rowHasNaN(currentRow) = true
        RowsWithoutNaN -= 1
      }
      currentCol += 1
     }
     currentRow += 1
   }
  
  // scan each column to see if it has an NaN
  var currentCol = 0
  var ColsWithoutNaN = Ncols   // currently assume that all columns have only defined values
  while (currentCol < Ncols) {
    var hasNaN = false
    var currentRow = 0
    while (currentRow < Nrows && hasNaN == false) {
      if (this(currentRow, currentCol).isNaN) {
        hasNaN = true
        colHasNaN(currentCol) = true
        ColsWithoutNaN -= 1
      }
      currentRow += 1
     }
     currentCol += 1
   }
  
    // create now the new array that will not contain the NaN values
   var  definedValuesArray = Array.ofDim[Double](RowsWithoutNaN, ColsWithoutNaN)
   
    // copy the defined values
   currentRow = 0
   var drow = 0; var dcol = 0 // indexing to definedValuesArray
   while (currentRow < Nrows)  {
     currentCol = 0
     if (rowHasNaN(currentRow) == false) { // row with no NaN values
        dcol = 0
        while (currentCol < Ncols) {
          if (colHasNaN(currentCol) == false)  { // column with no NaN values
            definedValuesArray(drow)(dcol) = this(currentRow, currentCol)
            dcol += 1
           }
          currentCol += 1
        }
          drow += 1
          
        } // row with no NaN values
       currentRow += 1
      }  
      
      new RichDouble2DArray(definedValuesArray)
      
     
}
    
  
 // parallel filter: filter the elements of the matrix using multithreading
def pfilter(p: (Double => Boolean)): specificMatrix = {
   var fres = scalaSci.MatrixFactory(this, Nrows, Ncols)
    
  
  var nthreads = GlobalValues.numOfThreads
  nthreads = Math.min(nthreads, Nrows)
  
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
        var cval = thisObj(r, c)     // the current element of the matrix
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

    fres.asInstanceOf[specificMatrix]
}

      
  
 // parallel exists:  detect if there exists some element that satisfies the predicate p
def pexists(p: (Double => Boolean)): Boolean = {
    var found = false
    
  var nthreads = GlobalValues.numOfThreads
  nthreads = Math.min(nthreads, Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run: Unit = {
      var r=firstRow   // the first row of the matrix that this thread processes
      while (r<lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
        var cval = thisObj(r, c)     // the current element of the matrix
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
def pforall(p: (Double => Boolean)): Boolean = {
    var condHoldsForAll = true   //  if one thread changes to false, then that condition does not hold for all elements
    
  var nthreads = GlobalValues.numOfThreads
  nthreads = Math.min(nthreads, Nrows)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = ((Nrows-1) / nthreads).toInt + 1  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
  
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run: Unit = {
      var r = firstRow   // the first row of the matrix that this thread processes
      while (r < lastRow) {  // the last row of the matrix that this thread processes
      var c = 0
      while  (c < Ncols)  {
        var cval = thisObj(r, c)     // the current element of the matrix
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
 def forall( p: (Double => Boolean)) = {
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
 def exists(p: (Double => Boolean)): Boolean  = {
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
  def isPositive(x: Double) = x > 0
  val y = x filter isPositive
 */  
def filter(p: (Double => Boolean)): specificMatrix = {
   var fres = scalaSci.MatrixFactory(this, Nrows, Ncols)  // construct the matrix to keep the filtered result
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
   fres.asInstanceOf[specificMatrix]   // return the matrix consisting of the elements that pass the filter condition
}  
             



  /*  filter all the rows/columns of the matrix according to the predicate
   the predicate is a function from the Int index of row/column to a boolean value
   For example, to return all the even numbered rows and columns of a matrix: 
    
      val  x = rand0(5, 7)
      def isEven(n: Int) = if (n % 2 == 0) true else false   // define the predicate
      val xevenRows = x filterRows isEven
      val xevenCols = x filterColumns isEven
   
  */
   
  def  filterRows( predicate:  Int  => Boolean): specificMatrix = {
      var rowCnt = 0
      var r = 0
    // count the number of rows that fullfill the predicate
    while (r < this.numRows()) {
          if (predicate(r))  
            rowCnt += 1
         r += 1
     }
    var  newMat =  scalaSci.MatrixFactory(this, rowCnt, this.numColumns())
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
                    
    newMat.asInstanceOf[specificMatrix]     
  
  }		 

  		 

  // return cols according to the predicate
 def  filterColumns( predicate:  Int => Boolean): specificMatrix = {
    var colCnt = 0
    var c = 0
    while  (c <  this.numColumns()) {
      if (predicate(c))
       colCnt += 1
     c += 1
  }
    var newMat = scalaSci.MatrixFactory(this, this.numRows(), colCnt)
    c = 0
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
    
      newMat.asInstanceOf[specificMatrix]     
  
   }		 

    
  
 
    
 def print() =  {
   val digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var r = 0;  
    while (r < Nrows) {
      var c = 0
      while  (c <  Ncols)  {
            System.out.print(digitFormat.format(this(r, c)) + "  ")
            c += 1
             }
          println("\n")
          r += 1
        }
      }
      
   
   
  
def p = print   // short "print" method

  
// Row append and prepend routines    
/*
 var  mm = rand0(4, 5)
 var mmo = ones0(2, 5)
 var mmappend = mm RA mmo  // prepend mmo 
 var mmprepend = mm RA mmo // append mmo 
   */   
def  RA(rowsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (rowsToAppend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToAppend.Nrows   //  number of rows of the new matrix
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)

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
        res.asInstanceOf[specificMatrix]
}


def  RA(rowsToAppend: RichDouble2DArray): specificMatrix = {
    if (rowsToAppend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToAppend.Nrows   //  number of rows of the new matrix
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)

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
        res.asInstanceOf[specificMatrix]
}


  
def  RA(rowToAppend: Array[Double]): specificMatrix = {
    if (rowToAppend.length != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+1    // new number of rows
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
    
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
      
    res.asInstanceOf[specificMatrix]
}

 def  RA(rowToAppend: RichDouble1DArray): specificMatrix = {
    this RA rowToAppend.getv 
 }
 
  
 def  RA(rowToAppend: scalaSci.Vec): specificMatrix = {
     this RA rowToAppend.getv  
 }
 
 
// prepend rowwise the Matrix Ncols
def  RP(rowsToPrepend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (rowsToPrepend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToPrepend.Nrows   // new number of rows
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
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
    res.asInstanceOf[specificMatrix]
}


// prepend rowwise the Matrix Ncols
def  RP(rowsToPrepend: RichDouble2DArray): specificMatrix = {
    if (rowsToPrepend.Ncols != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
    var  exrows = Nrows+rowsToPrepend.Nrows   // new number of rows
    var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
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
    res.asInstanceOf[specificMatrix]
}


  // prepend rowwise the 1-d array rowToPrepend
def  RP(rowToPrepend: Array[Double]): specificMatrix = {
    if (rowToPrepend.length != this.Ncols )   // incompatible number of columns
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added rows
   var  exrows = Nrows+1  // new number of rows
   var res = scalaSci.MatrixFactory(this, exrows, this.Ncols)
   
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
          
    res.asInstanceOf[specificMatrix]
}

  // prepend rowwise the 1-d array rowToPrepend
def  RP(rowToPrepend: RichDouble1DArray): specificMatrix = {
    this  RP rowToPrepend.getv 
}
  
  // prepend rowwise the 1-d array rowToPrepend
def  RP(rowToPrepend: scalaSci.Vec): specificMatrix = {
   this RP rowToPrepend.getv 
}

  
//  perhaps easier to remember alias 
def   >> (rowsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = 
    this RA  rowsToAppend 
 
 def >>(rowsToAppend: RichDouble2DArray): specificMatrix =
   this RA rowsToAppend
 
def   >>  (rowToAppend: Array[Double]): specificMatrix = 
     this RA rowToAppend 

def   >> (rowToAppend: RichDouble1DArray): specificMatrix = 
     this RA rowToAppend 
 
def  >>(rowToAppend: scalaSci.Vec): specificMatrix =
    this RA rowToAppend 
  
def  <<(rowsToPrepend: scalaSciMatrix[specificMatrix]): specificMatrix = 
    this RP rowsToPrepend
 
def <<(rowsToPrepend: RichDouble2DArray): specificMatrix =
   this RP rowsToPrepend
 
def   << (rowToPrepend: Array[Double]): specificMatrix = 
   this RP rowToPrepend   

def  <<(rowToPrepend: RichDouble1DArray): specificMatrix = 
   this RP rowToPrepend 
 
def  << (rowToPrepend: scalaSci.Vec): specificMatrix =
   this RP rowToPrepend   
 
    // Column append and prepend routines, e.g.
/* 
 var  mm = rand0(4, 5)
 var mmo = ones0(4, 2)
  var mmprepend = mm  CA mmo // append mmo 
 */
def  CA(colsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (colsToAppend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToAppend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)
 
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
    res.asInstanceOf[specificMatrix]
}


def  CA(colsToAppend: RichDouble2DArray): specificMatrix = {
    if (colsToAppend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToAppend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)  // create the result matrix
 
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
    
    // append the extra columns
    r = 0
    while  (r < colsToAppend.Nrows)   {
      var c = 0 
      while  (c < colsToAppend.Ncols)  {
         res(r, Ncols+c) = colsToAppend(r, c)
         c += 1
       }
      r += 1 
    }
    res.asInstanceOf[specificMatrix]
}


  // append an Array[Double] as the last column
def  CA(colsToAppend: Array[Double]): specificMatrix = {
    if (colsToAppend.length != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+1 // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)
 
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
      
    res.asInstanceOf[specificMatrix]
}

  // append a RichDouble1DArray as the last column
  def  CA (colsToAppend: RichDouble1DArray): specificMatrix = {
     this CA colsToAppend.getv
  }

   // append a scalaSci.Vec as the last column
  def CA (colsToAppend: scalaSci.Vec): specificMatrix  = {
     this CA colsToAppend.getv  
  }
  

// prepend a Mat  
  def  CP(colsToPrepend: scalaSciMatrix[specificMatrix]): specificMatrix = {
    if (colsToPrepend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToPrepend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)

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
    
    res.asInstanceOf[specificMatrix]
}

def  CP(colsToPrepend: RichDouble2DArray): specificMatrix = {
    if (colsToPrepend.Nrows != this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+colsToPrepend.Ncols  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)

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
    
    res.asInstanceOf[specificMatrix]
}

  
  
// prepend an Array[Double] to matrix
def  CP(colsToPrepend:  Array[Double]): specificMatrix = {
    var arrayLen = colsToPrepend.length
    if (arrayLen!= this.Nrows )   // incompatible number of rows
      return this.asInstanceOf[specificMatrix]
    // create a new extended matrix to have also the added columns
    var  excols = this.Ncols+1  // new number of columns
    var res = scalaSci.MatrixFactory(this, this.Nrows, excols)
    
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
         res(r, c+1) = this(r, c)
         c += 1
      }
      r += 1
    }

    res.asInstanceOf[specificMatrix]
}

  // prepend a RichDouble2DArray
def CP(colToPrepend: RichDouble1DArray): specificMatrix = 
   this CP colToPrepend.getv 

 // prepend a scalaSci.Vec
def CP(colToPrepend: scalaSci.Vec): specificMatrix = 
   this CP colToPrepend.getv 

 
//  perhaps easier to remember alias
def   >>>(colsToAppend: scalaSciMatrix[specificMatrix]): specificMatrix = 
    this CA colsToAppend  
 
def  >>>(colsToAppend: RichDouble2DArray): specificMatrix =
    this  CA  colsToAppend
  
def   >>>(colToAppend: Array[Double]): specificMatrix = 
   this CA colToAppend 

def   >>>(colToAppend: RichDouble1DArray): specificMatrix = 
   this CA colToAppend 
 
def  >>>(colToAppend: scalaSci.Vec): specificMatrix =
   this CA colToAppend 
  
def   <<<(colsToPrepend: scalaSciMatrix[specificMatrix]): specificMatrix = 
   this CP colsToPrepend  
 
def <<<(colsToPrepend: RichDouble2DArray): specificMatrix =
   this  CP  colsToPrepend
 
def   <<<(colToPrepend: Array[Double]): specificMatrix = 
   this CP colToPrepend 

def   <<<(colToPrepend: RichDouble1DArray): specificMatrix = 
    this CP colToPrepend 
 
def  <<<(colToPrepend: scalaSci.Vec): specificMatrix =
   this CP colToPrepend 
   


  
  // IN-PLACE Operations: Update directly the receiver, avoiding creating a new return object

    // Mat + Mat
    /* e.g.
     val  x = ones0(4,6)
     x ++ (10*x)   // this operation adds to x, 10 times x
     */
def ++ (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
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
     /* e.g.
     val  x = ones0(4,6)
     x ++ 100   // this operation adds to x, the number 100
     */

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
  /*
    val  x = ones0(4,6)
     x -- (10*x)   // this operation subtracts from  x, 10 times x
    */  
  
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
  /* 
    val  x = ones0(4,6)
     x -- 100    // this operation subtracts from  x, the number 100
    */
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
    /*
    val  x = ones0(4,6)
     x ** 100    // this operation multiples  x, with  the number 100
     */
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
    /*
    val  x = ones0(4,6)
     x /| 100    // this operation divides x, with  the number 100
     */
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

  
//  convert to Vec, e.g.
//  val  x = rand0(2,3);    val vx = x.toVec
def toVec(): Vec = {
  var v = new Vec(Nrows*Ncols)
  var cnt=0
  var r=0; var c = 0
  while (r < Nrows) {
    c = 0
     while (c < Ncols)  {
      v(cnt) = this(r, c)
      cnt += 1
      c += 1
      }
      r += 1
  }
    v
  }
  
def getArray = toDoubleArray  
  
  
  //  convert from the native to an Array[Array[Double]], e.g.
  //  val  x = rand0(2,3),   val vx = x.toDoubleArray
def toDoubleArray  = {
    var da = Array.ofDim[Double](Nrows, Ncols)
    var r = 0; var c = 0
    while  (r < Nrows) {
      c = 0
      while (c < Ncols) {
        da(r)(c) = this(r,c)
        c += 1
      }
      r += 1
    }
    da
}

  
  
  final def toList =   toDoubleArray toList
  final def toSeq =     toDoubleArray toSeq   
  final def toIterable = toDoubleArray toIterable
  final def toIndexedSeq = toDoubleArray toIndexedSeq
  final def toStream =toDoubleArray  toStream
  final def toSet = toDoubleArray  toSet
  final def isEmpty = toDoubleArray  isEmpty
  final def hasDefiniteSize = toDoubleArray hasDefiniteSize
  final def head = toDoubleArray head
  final def headOption = toDoubleArray headOption
  final def last = toDoubleArray last
  final def lastOption = toDoubleArray lastOption  
   
 // convert from an Array[Array[Double]] to the native representation, e.g.
 // val  x = rand0(2,3);    val vx = x.toDoubleArray;   val xrecovered = x.fromDoubleArray(vx)
def fromDoubleArray(x: Array[Array[Double]]) : specificMatrix = {
  var nrows = x.length; var ncols = x(0).length
  var r = 0; var c = 0
  while (r < nrows) {
    c = 0
    while (c < ncols) {
      this(r, c) = x(r)(c)
      c += 1
    }
    r += 1
    }
    this.asInstanceOf[specificMatrix]
  }
  

// Mat + Double, e.g.
//  val  x = rand0(2,3); var y = x + 20.9
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


// Mat + Double, e.g.
//  val  x = rand0(2,3); var y = x + 20.9
def +& (that: Double): specificMatrix =  {
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
  var cobj = this 
  var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
 
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
         nv(r, c) = cobj(r, c) + that
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

     
   nv.asInstanceOf[specificMatrix]
}

  
// Mat - Double, e.g.
  //   val  x = rand0(2,3); var y = x - 120.9
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


// Mat - Double, e.g.
  //   val  x = rand0(2,3); var y = x - 120.9
def -& (that: Double): specificMatrix =  {
  
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
  var cobj = this 
  var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
 
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
         nv(r, c) = cobj(r,c) - that
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

     
   nv.asInstanceOf[specificMatrix]
}

// Mat +< Vec : column oriented addition, , e.g.
    /*
     val   x = rand0(4, 8)
     val   v = vones(4)
     val   xv = x +< (100*v)   // add 100 times v from all columns of  x
     */
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

    // Mat -< Vec : column oriented subtraction, e.g.
    /*
     val   x = rand0(4, 8)
     val   v = vones(4)
     val   xv = x -< (100*v)   // subtract 100 times v from all columns of  x
     */
    
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

   
   // Mat (NXM)*< Vec(MX1) : Mat(NX1) Matrix-Vector multiplication
   // e.g. 
   /*
    var x = ones0(6, 8)
    var v  = vones(8)
    var vx = x *< v
    */
def *< (that: Vec): specificMatrix =  {
    var vl = that.length
    if (vl != this.Ncols )  {
      println("vector length not equal to the number of rows in *<")
      return this.asInstanceOf[specificMatrix]
    }
    var sm=0.0
    var nv  = scalaSci.MatrixFactory(this, this.Nrows, 1)
    var r = 0
    while (r < Nrows) {      // all rows of the Mat
     sm = 0.0
     var c = 0
     while  (c < Ncols )  {
       sm += this(r, c)*that(c)
       c += 1
      }
     nv(r,0) = sm
     
      r += 1
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
  /* e.g.
   var x = ones0(2, 3)
   // the following are aliases for transposing
   var xt1  = x trans
   var xt2 = x.T
   var xt3 = x ~
   */
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
   var i=0; var j=0
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
    /* e.g.
     val A = eye0(9)
     val A5 = A*5
     */
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

    // multiply Mat*Double
    /* e.g.
     val A = eye0(9)
     val A5 = A *& 5
     */
def *& (that: Double): specificMatrix =  {
   
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
  var cobj = this 
  var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
 
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
         nv(r, c)  =cobj(r, c) * that
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

     
   nv.asInstanceOf[specificMatrix]
}

   // divide Mat/Double
    /* e.g.
     val A = eye0(9)
     val Ad5 = A/5.8
     */
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

    // divide Mat/Double
    /* e.g.
     val A = eye0(9)
     val Ad5 = A /& 5.8
     */
def  /& (that: Double): specificMatrix =  {
   
  var nv  = scalaSci.MatrixFactory(this, this.Nrows, this.Ncols)
  var cobj = this 
  var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
 
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
         nv(r, c) = cobj(r,c) /  that
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

     
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
      var r = 0
      while  (r <  rN)  {  // rows
        var c = 0
        while  (c < rM)  {  // columns
          rm(r, c) = this(r, c)+that(r)
          c += 1
         }
        
        r += 1
      }
      rm.asInstanceOf[specificMatrix]
 }
 else if (rM == sN) { // add row-wise
      var r = 0
      while   (r <  rN) {
        var c = 0  
        while (c < sN) {
            rm(r, c) = this(r, c)+that(c)
            c += 1
         }
        
        r += 1
      }
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
  
 if (rM == sN) { // multiply row-wise
     var rv = new Vec(rN)
     var r = 0
     while  (r < rN) {
           sm = 0.0 
           var c = 0
          while (c <  sN)  {
            sm += (this(r, c)*that(c))
            c += 1
          }
          
          rv(r) = sm
          r += 1
       }
         rv
 }  // multiply row-wise
 else {
     if (rN == sN) {  // multiply column-wise
     var rv = new Vec(rM)
     var c = 0
     while  (c < rM) {
           sm = 0.0
           var r = 0
         while (r < rN)  {
             sm += (this(r, c)*that(r))
             r += 1
            }
        rv(c) =  sm
          
        c += 1
        }
      rv
    }
 else
   new Vec(1)   
  }
 } 
  
def *(that: RichDouble1DArray): RichDouble1DArray = {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length
   var sm = 0.0
 
 
  if (rM == sN) { // multiply row-wise
     var rv = new Array[Double](rN)
     var r = 0
     while  (r < rN) {
           sm = 0.0 
           var c = 0
          while (c <  sN)  {
            sm += (this(r, c)*that(c))
            c += 1
          }
          
          rv(r) = sm
          r += 1
       }
            new RichDouble1DArray(rv)
 }
 else {
      if (rN == sN) {  // multiply column-wise
     var rv = new Array[Double](rM)
     var c = 0
     while  (c < rM) {
           sm = 0.0
           var r = 0
         while (r < rN)  {
             sm += (this(r, c)*that(r))
             r += 1
            }
        rv(c) =  sm
          
        c += 1
        }
      new RichDouble1DArray(rv)
   }
 else
   that
  }
 }
  
def *(that: Array[Double]): RichDouble1DArray = {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length
   var sm = 0.0
   
    if (rM == sN) { // multiply row-wise
     var rv = new Array[Double](rN)
     var r = 0
     while  (r < rN) {
           sm = 0.0 
           var c = 0
          while (c <  sN)  {
            sm += (this(r, c)*that(c))
            c += 1
          }
          
          rv(r) = sm
          r += 1
       }
         new RichDouble1DArray(rv)
 }
 else {
    if (rN == sN) {  // multiply column-wise
     var rv = new Array[Double](rM)
     var c = 0
     while  (c < rM) {
           sm = 0.0
           var r = 0
         while (r < rN)  {
             sm += (this(r, c)*that(r))
             r += 1
            }
        rv(c) =  sm
          
        c += 1
        }
      new RichDouble1DArray(rv)
 }
 
 else
   new RichDouble1DArray(that)   
   }
  }
 
    /*
   var  m = ones0(2,4)
   var  v =  vones(2)
   var  mv2 = m-v
   var v4 = vones(4)
   var mv4 = m-v4
*/
def -(that: Vec): specificMatrix = {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.length
 
    var rm = scalaSci.MatrixFactory(this, rN,  rM)
    if (rN == sN) {  // subtract column-wise
      var r = 0
      while  (r < rN)  {
        var c = 0
        while  (c < rM) {
          rm(r, c) = this(r, c)-that(r)
          c += 1
        }
         r += 1
      }
      rm.asInstanceOf[specificMatrix]
 }
 else if (rM == sN) { // subtract row-wise
      var r = 0
      while ( r < rN) {
          var c = 0
          while (c < sN)  {
            rm(r, c) = this(r, c)-that(c)
            c += 1
          }
          r += 1
      }
      rm.asInstanceOf[specificMatrix]
  }
  else 
    rm.asInstanceOf[specificMatrix]
 }
 
  

  // Mat - RichDouble2DArray
def  - (that: RichDouble2DArray): specificMatrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions

  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
       var i=0; var j=0;
   while (i<sN)  {
        j=0
       while (j<sM)  {
           nv(i, j) = this(i, j) - that(i, j)
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


// Mat +& Mat
def  +& (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions
   var cobj = this
   
  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
   
      var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
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
         nv(r, c) = cobj(r,c) + that(r, c)
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
      
   nv.asInstanceOf[specificMatrix]
  }
  else { // incompatible dimensions
   that.asInstanceOf[specificMatrix]
     }
}


  // Mat + RichDouble2DArray
def  + (that: RichDouble2DArray): specificMatrix =  {
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


  

  // Mat +& RichDouble2DArray
def  +& (that: RichDouble2DArray): specificMatrix =  {
   
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions
   var cobj = this
  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
   
      var nthreads = Math.min(scalaSci.RichDouble2DArray.numThreads, Nrows)
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
         nv(r, c) = cobj(r,c) + that(r, c)
         c += 1
      }
      r += 1
      }
    }
 })

     threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
      
   nv.asInstanceOf[specificMatrix]
  }
  else { // incompatible dimensions
   that.asInstanceOf[specificMatrix]
     }
}



// Mat - Mat
def  - (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions

  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
       var i=0; var j=0;
   while (i<sN)  {
        j=0
       while (j<sM)  {
           nv(i, j) = this(i, j) - that(i, j)
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

  

// Mat - Mat
def  -& (that: scalaSciMatrix[specificMatrix]): specificMatrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions
   
  if (rN == sN && rM == sM)  {     // same dimensions
       var nv  = scalaSci.MatrixFactory(this, sN, sM)
       var i=0; var j=0;
   while (i<sN)  {
        j=0
       while (j<sM)  {
           nv(i, j) = this(i, j) - that(i, j)
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


// generic Mat * RichDouble2DArray
 def * (that: RichDouble2DArray): specificMatrix =  {
   var  rN = this.Nrows;   var rM = this.Ncols;
   var  sN = that.Nrows;  var sM = that.Ncols;
   
   var  v1Colj = new Array[Double](rM)
   var result  = scalaSci.MatrixFactory(this, rN, sM)
   var j=0; var k=0;
   while (j < sM)  {
       k=0
      while  (k < rM) {
        v1Colj(k) =that(k, j)
        k += 1
      }

      var i=0;
      while (i<rN) {
        var   s = 0.0;
        k=0
        while (k< rM) {
          s += this(i, k)*v1Colj(k)
          k += 1
        }
      result(i, j) = s;
      i += 1
      }
 j += 1
   }
  return result.asInstanceOf[specificMatrix]
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
  def browse() = { 
    var browsedValues = this.toDoubleArray
    scalaExec.gui.watchMatrix.display(browsedValues, false)
    browsedValues 
  }
  
// pass also the varName in order be displayed at the browser's window title
  def browse(varName: String) =  { 
               var browsedValues = this.toDoubleArray
               scalaExec.gui.watchMatrix.display(this.toDoubleArray, false, varName)
               browsedValues
  }


  
  // columnwise sum
def sum(): RichDouble1DArray = {
    var N = this.Nrows;     var M = this.Ncols
    var sm = 0.0
    var res = new Array[Double](M)
    var ccol = 0
    while  (ccol < M) {
     sm=0.0
     var crow = 0
     while (crow < N)   {
       sm += this(crow, ccol)
       crow += 1
      }
     res(ccol) = sm 
     ccol += 1
     }
    new RichDouble1DArray(res)
}

  
// columnwise mean
def mean(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
     sm=0.0
     var crow = 0
     while  (crow < Nrows)  {
       sm += this(crow, ccol)
       crow += 1
     }
     res(ccol) = sm/Nrows
     ccol += 1
    }
 new RichDouble1DArray(res)
}

// columnwise product
def prod(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Ncols)
    var ccol = 0
    while  (ccol < Ncols) {
     pd = 1.0
     var  crow = 0
     while  (crow < Nrows)  {
       pd *= this(crow, ccol)
       crow += 1
      }
     res(ccol) = pd
     ccol += 1
     }
    new RichDouble1DArray(res)
}

  // columnwise min
 def min(): RichDouble1DArray = {
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
     new RichDouble1DArray(res)
 }

  // columnwise max
 def max(): RichDouble1DArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Ncols)
     var ccol = 0
     while  (ccol < Ncols) {
          var mx = this(0, ccol)  // keeps the running max element
          var crow = 1
          while  (crow < Nrows)  {
              var tmp = this(crow, ccol)
               if (tmp  > mx)  mx = tmp
               crow += 1
           }
        res(ccol) = mx   // max element for the ccol column
        
        ccol += 1
     }
     new RichDouble1DArray(res)
 }

  
  
// ROWWISE OPERATIONS
// rowwise sum
def sumR(): RichDouble1DArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // sum for all rows
    val  data = getv.asInstanceOf[Array[Array[Double]]]
    
    if (Ncols > LargeMatrix)  // use parallel version
      {
        var crow = 0
        while (crow < Nrows) {
          res(crow) = data(crow).sum
          crow += 1
        }
        
      }
 else {
    var crow = 0
    while  (crow < Nrows)  {
        sm=0.0
        
        var ccol = 0
        while (ccol < Ncols)  {   // sum across column
         sm += this(crow, ccol)
         ccol += 1
        }
     res(crow) = sm
     crow += 1
     }
 }
 new RichDouble1DArray(res)
}
// SOSSOS
  
  // rowwise mean
def meanR(): RichDouble1DArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)  // mean for all rows
    var crow = 0
    while  (crow < Nrows) {
     sm = 0.0
     var ccol = 0
     while (ccol < Ncols)  {  // sum across column
       sm += this(crow, ccol)
       ccol += 1
      }
     res(crow) = sm/Ncols
     crow += 1 
    }
 new RichDouble1DArray(res)
}

  
// rowwise product
def prodR(): RichDouble1DArray  = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Nrows)
    var crow =  0
    while  (crow < Nrows) {
     pd = 1.0
     var ccol = 0
     while   (ccol < Ncols)   {  // product across column
       pd *= this(crow, ccol)
       ccol += 1
     }

     res(crow) = pd
     crow += 1 
      
     }
    new RichDouble1DArray(res)
}
  
  
 // rowwise min
 def minR(): RichDouble1DArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Nrows)
     var crow = 0
     while (crow  <  Nrows) {
         var mn = this(crow, 0)  // keeps the running min element
         var ccol = 1
         while  (ccol < Ncols)  {
              var tmp = this(crow, ccol)
               if (tmp  < mn)  mn = tmp
               ccol += 1
           }
           res(crow) = mn   // min element for the crow row
           crow += 1
     }
      new RichDouble1DArray(res)
 }

  
  // rowwise max
 def maxR(): RichDouble1DArray = {
     var Nrows = this.Nrows;   var Ncols = this.Ncols
     var res = new Array[Double](Nrows)
     var crow = 0
     while (crow < Nrows) {
         var mx = this(crow, 0)  // keeps the running max element
         var ccol = 1
         while (ccol < Ncols)  {
              var tmp = this(crow, ccol)
               if (tmp  > mx)  mx = tmp
               ccol += 1
           }
        res(crow) = mx   // max element for the ccol column
        crow += 1 
    }
     new RichDouble1DArray(res)
 }

    
// resamples the scalaSci Matrix  every n rows and every m cols
/* can be use for downsampling e.g.
val x = rand0(5, 6)
val xr = resample(x, 2, 2)
*/

def resample( n: Int,  m: Int): specificMatrix  = {
    var rows = this.Nrows
    var cols = this.Ncols
    var  rRows =  (rows/n).asInstanceOf[Int]
    if (rRows*n == rows-1)
      rRows += 1
  
    var  rCols =  (cols/m).asInstanceOf[Int]
    if (rCols*m == cols-1)
      rCols += 1
  
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
  /* e.g.
val x = rand0(2, 3)
val xcol = reshape(x, 6, 1)  // reshape as column
val xrow = reshape(x, 1, 6)  // reshape as row
*/
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


// cross (pointwise) product of a Matrix with a Matrix
def  cross(that: scalaSciMatrix[specificMatrix]):  specificMatrix= {
  var nv = scalaSci.MatrixFactory(this, Nrows, Ncols) // create the new matrix
   var i=0; var j=0
   while (i<Nrows) {
     j=0
    while (j<Ncols)  {
      nv(i, j) = this(i, j) * that(i, j)
      j += 1
    }
    i += 1
   }
 nv.asInstanceOf[specificMatrix]  // return the new Matrix
}


  
// cross (pointwise) product of a Matrix with an Array[Array[Double]]
def  cross(that: Array[Array[Double]]):  specificMatrix = {
  var nv = scalaSci.MatrixFactory(this, Nrows, Ncols) // create the new matrix
   var i=0; var j=0
   while (i<Nrows) {
     j=0
    while (j<Ncols)  {
      nv(i,  j) = this(i, j) * that(i)(j)
      j += 1
    }
    i += 1
   }
 nv.asInstanceOf[specificMatrix]
}


// dot  product of a Matrix with a Matrix
def  dot(that: scalaSciMatrix[specificMatrix]):  Double = {
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
def  dot(that: Array[Array[Double]]):  Double = {
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
def  dot(that: Array[Double]):  Double = {
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
def  dot(that: Vec):  Double = {
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
   
  
override def  toString(): String = {
    scalaSci.RichDouble2DArray.printArray(this.toDoubleArray)
}

 //  det() is the determinant of the square matrix X.
def det(): Double 

/*   trace():   is the sum of the diagonal elements of A, which is
    also the sum of the eigenvalues of A. */
def trace(): Double  

def inv(): specificMatrix  // the inverse of the matrix

def pinv(): specificMatrix   // the pseudo-inverse of the matrix  
  
  /*returns the 2-norm condition number (the ratio of the
    largest singular value of X to the smallest).  Large condition
    numbers indicate a nearly singular matrix.
 */
def cond(): Double   
//  provides an estimate of the number of linearly independent rows or columns of a matrix A.
def rank(): Int
  
/*    (V,D) = eig(X) : produces an array  D of eigenvalues and a
    full matrix V whose columns are the corresponding eigenvectors so
    that X*V = V*D */
 def eig(): (RichDouble2DArray, RichDouble1DArray) 
  
/* (U,S,V) = svd() produces a diagonal matrix S, of the same 
    dimension as X and with nonnegative diagonal elements in
    decreasing order, and unitary matrices U and V so that
    X = U*S*V'.  
    */
   def  svd(): (RichDouble2DArray, RichDouble1DArray, RichDouble2DArray)
  
// reduced row-echelon form
  def rref(): specificMatrix
}





