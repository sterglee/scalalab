

package scalaSci.EJML



import scala.language.postfixOps
import java.util._
import org.ejml.data.BlockMatrix64F

// this class provides Matrix operations, by wrapping the Efficient Java Matrix Library and using block matrix operations
class BMat(bmi: BlockMatrix64F) extends AnyRef  {
    var  bm = bmi   // keep the reference to the Block Matrix
// getters for size
    var  Nrows = bmi.numRows
    var  Ncols = bmi.numCols
    final def size = {  (Nrows, Ncols) }  // the size of the Matrix
    final def length =  { Nrows }


  final def  numRows()  = bmi.numRows
  final def  numColumns() =  bmi.numCols
  
  final def this(N: Int, M: Int) = {
    this(new BlockMatrix64F(N, M))
  }
  
  
// construct a Matrix from a tuple of values corresponding to its size
final def this( tuple: (Int, Int)) = 
   this(tuple._1, tuple._1)  
  
  
final def this(values: Array[Array[Double]]) = {
  this (new BlockMatrix64F(values.length, values(0).length))
  var r = 0
  while  (r < Nrows) {
    var c = 0
    while  (c < Ncols)        {
      this(r,c) = values(r)(c)
      c += 1
    }
    r += 1
  }
}

override final def clone() = {
  var bb =  bmi.copy
  new BMat(bb)
}

  final def copy() = {  // same as clone()
    clone()
  }

  final def print = {
     bm.print
  }
  
  final def  getv() = {
    bm.getData
  }
  
 final def toDoubleArray = {
   getv()
 } 
  final def getBM = bm    // gets the wrapped EJML matrix

  
override  final def  toString(): String = {
  var digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var nelems = Nrows
    var melems = Ncols
    var truncated = false

    var truncationIndicator = ""
    if  (Mat.mxElemsToDisplay < Nrows) {
        nelems = Mat.mxElemsToDisplay
        truncationIndicator = " ... "
        truncated = true
      }
     if  (Mat.mxElemsToDisplay < Ncols) {
        melems = Mat.mxElemsToDisplay
        truncationIndicator = " ... "
      }
     var s:String =" \n"
     var i=0; var j=0;
    while (i<nelems) {
        j=0
        while (j<melems) {
       s = s+digitFormat.format(bm.get(i, j))+"  "
       j+=1
        }
      s += (truncationIndicator+"\n")
     i+=1
    }
   if (truncated)     // handle last line
    s+= "........................................................................................................................................."

     return s
 }

   
  // apply the function f to all the elements of the Matrix and return the results with a new Matrix
 final def  map( f: (Double => Double)): BMat = {
   var mres = new BMat(Nrows, Ncols)
   
    var r = 0
    while (r < Nrows) {
      var c = 0
     while  (c < Ncols)  {
       mres(r, c) = f(this(r, c) )
       c += 1
     }
     r += 1
    }
   
   mres
 }

  final def apply( row: Int, col: Int) = {
    bm.get( row, col )
   }

  final def apply( row: Int, col: Int, resize: Boolean ) = {  // TODO-Sterg: implement resizing
    bm.get( row, col )
   }

 final def  set(row: Int, column: Int, value: Double) =     bm.set(row, column, value)
  
 final def  get(row: Int, column: Int) = bm.get(row, column)

 


// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ::) corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummyChar). if low>high then rows are returned in reverse
 final def apply(rowL: Int, rowH: Int, allColsChar:  ::.type ): BMat = {
   var rowStart = rowL; var rowEnd=rowH;
   var colStart = 0;     var colEnd =  Ncols-1;   // all columns
   var colNum = Ncols
   var colInc = 1

    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    
  if (rowStart <= rowEnd) {   // positive increment
    var rowInc = 1
    var rowNum = rowEnd-rowStart+1
    var subMatr = new BMat(rowNum, colNum)   // create a Mat to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd )   { 
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       } // crow <= rowEnd
subMatr   // return the submatrix

} // rowStart <= rowEnd
else { // rowStart > rowEnd
    var rowInc = -1
    var rowNum = rowStart-rowEnd+1
    var subMatr = new BMat(rowNum, colNum)   // create a Mat to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row at the source matrix
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new Mat
    while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }

subMatr   // return the submatrix

} // rowStart > rowEnd

}

 final  def apply(rowSpec: scalaSci.Vec, allColsSymbol: scala.::.type): BMat  = {
  var rowL = rowSpec(0).toInt
  var vl = rowSpec.length
  var rowInc = (rowSpec(1)-rowSpec(0)).toInt
  var rowH = rowSpec(vl-1).toInt
  apply(rowL, rowInc, rowH, ::)
}   

 final def apply(rowSpec: scalaSci.MatlabRange.MatlabRangeNext, allColsSymbol: scala.::.type): BMat = {
    var rowL = rowSpec.mStart.inc.toInt
    var rowH = rowSpec.mStart.endv.toInt
    
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply(rowL, rowInc, rowH, ::)
  }
   
// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ::) corresponds to Matlab's m(2:4:12, :)'
final def apply(rowL: Int, rowInc: Int, rowH: Int, allColsChar: ::.type ): BMat = {
    var rowStart = rowL;     var rowEnd =  rowH;
    var colStart = 0;  var colEnd = Ncols-1;   // all columns
    var colNum = Ncols
    var colInc = 1

    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    
  if (rowInc > 0) { // positive increment
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colStart = 0;     var colEnd =  Ncols-1   // all columns
    var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx = 0; var colIdx = 0  // indexes at the new Mat
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
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
     var subMatr = new BMat(rowNum, colNum)  // create a Matrix to keep the extracted range
        // fill the created matrix with values
     var  crow = rowStart   // indexes current row at the source matrix
     var  ccol = colStart
     var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
     while (crow >= rowEnd)  {
         ccol = colStart;  colIdx = 0
         while (ccol <= colEnd)  {
             subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
             colIdx += 1
             ccol += colInc
         }
         rowIdx += 1
         crow += rowInc
      }
       subMatr  // return the submatrix
     }  // negative increment
}


// extracts a submatrix, e.g. m( ::, 2, 12 ) corresponds to Matlab's m(:, 2:12)'
  final def apply(allRowsChar: ::.type, colLow: Int, colHigh: Int): BMat = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowNum = Nrows    // take all the rows

    if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        
    if  (colStart <= colEnd)   {    // positive increment
        var colNum = colEnd-colStart+1
        var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
             
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += 1
               }
            rowIdx += 1
            crow += 1
     } // crow <= rowEnd
 subMatr
} // positive increment
  else {  // negative increment
    var colNum = colEnd-colStart+1
    var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += 1
               }
            rowIdx += 1
            crow += 1
     } // crow <= rowEnd
 subMatr   // return the submatrix
    }
   }


  // extracts a submatrix, e.g. m( ::, 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  final def apply(allRowsChar: ::.type, colLow: Int, colInc: Int, colHigh: Int): BMat = {
   var rowStart = 0;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows    // take all the rows

    if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        
    if  (colStart <= colEnd)   {    // positive increment
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
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
    var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr   // return the submatrix
   }
   }

  
  

// e.g. var xx = rand0b(70,70); var xx2 = xx(::, 1::2::7)
final def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.Vec): BMat = {
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt
  apply(::, colL, colsInc, colH)
}   

  // e.g. var xx = rand0(20,20); var yy2 = xx(::, 1::4)
final def apply(allRowsSymbol: scala.::.type, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): BMat = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(::, colL, colInc, colH)
  }
  

// extracts a submatrix, e.g. m( 2, 3, 12, 4, 2,  8 ) corresponds to Matlab's m(2:3:12, 4:2:8)'
  final def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): BMat = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new BMat(rowNum, colNum)   // create a Matrix to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
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
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
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
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
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
                subMatr.bm.set(rowIdx, colIdx, bm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow > rowEnd
 subMatr   // return the submatrix
   }

   }
   
  
 // e.g. var xx = rand0b(70);  var yy = xx(1::2::8, 2)
final def apply(rowsSpec: scalaSci.Vec,  col: Int): BMat = {
  var rowsL = rowsSpec(0).toInt
  var vlr = rowsSpec.length
  var rowsInc = (rowsSpec(1)-rowsSpec(0)).toInt
  var rowsH = rowsSpec(vlr-1).toInt
  
  
  apply(rowsL, rowsInc, rowsH, col, 1, col)
}

  
   // e.g. var xx = rand0b(70, 70);  var yy = xx(1,  2::3::9)
final def apply(row: Int,  colsSpec: scalaSci.Vec): BMat = {
  
  var colL = colsSpec(0).toInt
  var vl = colsSpec.length
  var colsInc = (colsSpec(1)-colsSpec(0)).toInt
  var colH = colsSpec(vl-1).toInt

  apply(row, 1, row,  colL, colsInc, colH)
}   
  
  
  // e.g. var xx = rand0b(70);  var yy = xx(1::2::8, 2::3::9)
final def apply(rowsSpec: scalaSci.Vec,  colsSpec: scalaSci.Vec): BMat  = {
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

  // e.g. var xx = rand0b(70, 70);  var yy = xx(1::8, 2)
final def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, col: Int): BMat  = {
    var rowL = rowsSpec.mStart.inc.toInt
    var rowH = rowsSpec.mStart.endv.toInt
    var rowInc = 1
    if (rowH < rowL)  rowInc = -1
    apply( rowL, rowInc, rowH,  col, 1, col)
  }
    
   // e.g. var xx = rand0b(7, 7);  var yy = xx(1, 2::4)
  final def apply(row: Int, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): BMat = {
    var colL = colsSpec.mStart.inc.toInt
    var colH = colsSpec.mStart.endv.toInt
    var colInc = 1
    if (colH < colL)  colInc = -1
    apply(row, 1, row, colL, colInc, colH)
  }

  
  // e.g. var xx = rand0b(20, 20); var yy = xx(2::4, 3::5)
final def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): BMat  = {
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
  
  
  // e.g. var xx = rand0b(12, 12); var ff = xx(2::3, 1::2::10)
final def apply(rowsSpec: scalaSci.MatlabRange.MatlabRangeNext, colsSpec: scalaSci.Vec): BMat  = {
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
  
  
    // e.g. var xx = rand0b(12, 12); var gg = xx(2::3::11, 3::7)
final def apply(rowsSpec: scalaSci.Vec, colsSpec: scalaSci.MatlabRange.MatlabRangeNext): BMat  = {
    
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
  
  
  final def update( row: Int, col: Int, value: Double) = {
    bm.set(row, col, value)
    } 


  // perform Matrix addition with a number
  final def +(that: Double ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)+that)
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}

   // perform Matrix subtraction with a number
  final def -(that: Double ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)-that)
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}

  
  // perform Matrix addition with a number
  final def +(that: BMat ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)+that.bm.get(i, j))
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}

   // perform Matrix subtraction with a number
  final def -(that: BMat ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)-that.bm.get(i,j))
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}
   // perform Matrix multiplication with a number
  final def *(that: Double ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)*that)
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}

  
  // unary Minus applied to a Matrix implies negation of all of its elements
final def unary_- : BMat =  {
      var nv = new BMat(this.Nrows, this.Ncols)  // get a Matrix of the same dimension
   var i=0; var j=0
   while (i<Nrows) {
     j=0
    while (j<Ncols) {
      nv(i, j) = -bm.get(i,j)  // negate element
      j += 1
    }
    i += 1
   }
   return nv
}


   // perform Matrix division with a number
  final def /(that: Double ) = {
    var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
    var i=0; var j=0;
    var smSub = new org.ejml.data.BlockMatrix64F(rN, rM)   // new SimpleMatrix to perform the subtraction
    while (i<rN) {
        j=0
       while (j<rM) {
          smSub.set(i, j, bm.get(i, j)/that)
          j += 1
         }
     i += 1
  }
  new BMat(smSub)
}

final def *(that: BMat): BMat = {
  var C = new org.ejml.data.BlockMatrix64F(this.Nrows, that.Ncols) 
  org.ejml.alg.block.BlockMatrixOps.mult(bm, that.bm, C)
  new BMat(C)
  
 }
 
  final def reshape(n: Int, m: Int, saveFlag: Boolean): BMat = { 
    bm.reshape(n, m, saveFlag)
    new BMat(bm)
  }
  
  final def reshape(n: Int, m: Int): BMat = {
   bm.reshape(n, m, true)
   new BMat(bm)
  }
    
}



object  BMat {
  import  BMat._
  
   var mxElemsToDisplay = 6
  var matResizeFactor = 1.5
  var mxToStringElems = mxElemsToDisplay

    
  // wrap a Java type 1-D double array
  final def wrap(data: Array[Double],  numRows: Int, numCols: Int, blockLength: Int)  =  {
    var blockMat = BlockMatrix64F.wrap( data, numRows, numCols, blockLength)
    
    new BMat(blockMat)
      
  }
  
  // converts an EJML Mat to an EJML BMat, i.e. a conversion to block matrix form
  final def convert(mat: Mat): BMat = {
     var dst: BlockMatrix64F = new BlockMatrix64F(mat.Nrows, mat.Ncols)
     
    org.ejml.alg.block.BlockMatrixOps.convert(mat.sm.getMatrix, dst)
    
    new BMat(dst)
  }
  
  
  final def   random(numRows: Int, numCols: Int) = {
        var ret = new BMat(numRows, numCols)
        var r, c=0
        while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  java.lang.Math.random
             c += 1
             }
          r += 1
        }
        ret
}

  

final def ones(numRows: Int, numCols: Int) = {
   var ret = new BMat(numRows, numCols)
    var r, c=0
        while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  1.0
             c += 1
             }
          r += 1
        }
        ret
 }


final def zeros(numRows: Int, numCols: Int) = {
   var ret = new BMat(numRows, numCols)
    var r, c=0
        while (r<numRows) {
          c=0
          while (c<numCols) {
             ret(r, c) =  0.0
             c += 1
             }
          r += 1
        }
        ret
 }


final def fill(numRows: Int, numCols: Int, value: Double) = {
   var ret = new BMat(numRows, numCols)
   org.ejml.alg.block.BlockMatrixOps.set(ret.bm, value)
  
    ret
 }


final def sin(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.sin(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }


  
final def cos(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.cos(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }


final def tan(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.tan(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }


final def acos(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.acos(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }



final def asin(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.asin(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }


final def atan(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  Math.atan(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }




final def cosh(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  java.lang.Math.cosh(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }



final def sinh(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  java.lang.Math.sinh(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }


final def tanh(m: BMat) = {
   var numRows = m.Nrows
   var numCols = m.Ncols
   var ret = new BMat(numRows, numCols) 
   var r, c=0
   while (r<numRows) {
     c=0
          while (c<numCols) {
             ret(r, c) =  java.lang.Math.tanh(m(r,c))
             c += 1
             }
          r += 1
        }
        ret
 }

// convert from Matrix to EJMLMat to 
final def toEJMLMat(m: scalaSci.Matrix) = {
  var dataArray = m.getv   // get the data array
  var sm = new org.ejml.simple.SimpleMatrix(dataArray)
  var EJMLmat = new Mat(sm)
  EJMLmat
 }

final def  transpose(m: scalaSci.EJML.Mat)  = {
   m.transpose
   }

final def det(m: scalaSci.EJML.Mat) = 
   m.det
   
// construct a Matrix from a String 
// var m = Mb("3.4 -6.7; -1.2 5.6")
final def Mb(s: String) =  {
    var nRows = 1
    var nCols = 0
    var i = 0
    while (i < s.length) {   // count how many rows are specified
      if  (s(i)==';')
        nRows += 1
     i += 1 
    }

  // seperate rows to an ArrayBuffer
   var buf = new scala.collection.mutable.ArrayBuffer[String]()
   var strtok = new java.util.StringTokenizer(s, ";")  // rows are separated by ';'
   while (strtok.hasMoreTokens) {
         val tok = strtok.nextToken
         buf += tok
      }    

// count how many numbers each row has. Assuming that each line has the same number of elements
 val firstLine = buf(0)
 strtok = new java.util.StringTokenizer(firstLine, ", ")  // elements are separated by ',' or ' '
 while (strtok.hasMoreTokens) {
   val tok = strtok.nextToken
   nCols += 1  
}

var numbersArray = Array.ofDim[Double](nRows, nCols)   
    
    var k = 0
    while  (k <  nRows)  {  // read array
   var currentLine = buf(k)
   strtok = new java.util.StringTokenizer(currentLine, ", ")  // elements are separated by ',' or ' '
var c=0 
while (strtok.hasMoreTokens) {  // read row
   val tok = strtok.nextToken
   numbersArray(k)(c) = tok.toDouble
    c += 1
     }   // read row
     k += 1
   }  // read array
   new BMat(numbersArray)
 }  
 




final def testMat(N: Int, M: Int) = {
  var a = new BMat(N, M)
  var rows =  0
  while  (rows < N) {
    var cols = 0
    while  (cols < M) {
      a(rows, cols) = rows*10+cols
      cols += 1
    }
    rows += 1
  }
 a
}
  
}


/*
 
var bm = new scalaSci.EJML.BMat(4,5)

bm.print
var arr = bm.getv   // get the array as double[], shoulb be zeros

 var ra = vrand(20*30).getv
 var bwraped = scalaSci.EJML.BMat.wrap(ra, 20, 30, 10)
 bwraped.print
 
 
 var aa =   rand0(10, 20)  // create a random Mat
 var baa = scalaSci.EJML.BMat.convert(aa)   // convert to block matrix
 
 
 
 */
