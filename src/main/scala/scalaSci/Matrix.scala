

package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray
import java.util._
import Jama._

import scala.language.postfixOps

// this class provides Matrix operations, uses one indexed values for direct interfacing with NumAL
class Matrix( n: Int, m: Int)  extends AnyRef  with scalaSciMatrix[scalaSci.Matrix] 
{    // the default constructor allocates a double array of size n x m
   var  Nrows: Int=n+1
   var  Ncols: Int=m+1
   var  v = Array.ofDim[Double](Nrows, Ncols)  // i.e. v(1:n, 1:m)

    override final def  numRows()  = n
    override final def  numColumns() =  m

 // the scalaSci.Matrix does not wrap a Matrix class of a specific library, thus return simply the data representation  
  final def getLibraryMatrixRef() =   v

  final def matFromLibrary() = new Matrix(v)
  final def matFromLibrary(v: Array[Array[Double]]) = new Matrix(v)
  
  final def  setv(values: Array[Array[Double]], n: Int, m: Int) = { v = values; Nrows = n+1; Ncols = m+1 }
   
  override final def size() =   (Nrows-1, Ncols-1)   // the size of the Matrix
  override final def length()  = (Nrows-1)*(Ncols-1)
  
 import Matrix._
 
 
// indexes the corresponding Matrix element
  final def apply(n: Int, m: Int): Double =  v(n)(m)   
 

 final def apply(n: Int) = {
      var nr = n/(Ncols-1)
      var nc = n - nr*(Ncols-1)
      v(nr+1)(nc+1)
    }
 
 final def  set(row: Int, column: Int, value: Double) =   {  v(row+1)(column+1) = value }

 final def  get(row: Int, column: Int) = v(row+1)(column+1)

  final def getv() =    v
  
  
    // construct a Matrix from a zero-indexed double [][] array
final def this(vals: Array[Array[Double]]) = {
     this(vals.length, vals(0).length)  // allocate memory with the default constructor
    var i=0; var j=0
    while (i<vals.length) {
        j=0
        while (j<vals(0).length) {
       v(i+1)(j+1) = vals(i)(j)
       j+=1
        }
        i+=1
     }
 }



final def this(Nrows: Int, Ncols: Int, df: Double) = {   // initializes to a default value
     this(Nrows, Ncols)  // allocate memory with the default constructor
    var i=0; var j=0
    while (i<v.length-1) {
        j=0
        while (j<v(1).length-1) {
       v(i+1)(j+1) = df
       j+=1
        }
        i+=1
     }
 }
 

// construct a Matrix from a tuple of values corresponding to its size
final def this( tuple: (Int, Int)) = 
   this(tuple._1, tuple._1)  
    

    // construct a Matrix from an one-indexed double [][] array
final def this(vals: Array[Array[Double]], flag: Boolean) = {
    this(1,1)
    v = vals
    Nrows = vals.length
    Ncols = vals(0).length
 }

  // construct a Matrix from a one-indexed double [] array
final def this( vals: Array[Double]) = {
    this(1, vals.length)   // keep the array as a row of the Matrix
    var i=1
    while (i<vals.length)  {
       v(1)(i) = vals(i)
       i+=1
   }
  }

  // construct a Matrix from a double [] array
final def this( vals: Array[Double], oneIndexed: Boolean) = {
    this(1, vals.length-1)   // keep the array as a row of the Matrix 
    var i=1
    while (i<vals.length)  {
       v(1)(i) = vals(i)
       i+=1
   }
  }
  
  // convert to Vec
 override final def toVec(): Vec = {
  var v = new Vec(Nrows*Ncols)
  var cnt=0
  var r = 1; var  c = 1
  while (r <= Nrows) {
    c = 1
    while  (c <= Ncols)  {
      v(cnt) = this(r, c)
      cnt += 1
      c += 1
      }
      r += 1
  }
    v
  }
    
override final def toDoubleArray =  {
  val v0 = Array.ofDim[Double](numRows(), numColumns())
  var  r = 0;  var c = 0
  while  (r <  numRows()) {
    c = 0
    while (c < numColumns()) {
      v0(r)(c) = v(r+1)(c+1)
      c += 1
    }
    r += 1
  }
   v0 
    
}
  
// apply the function f to all the elements of the Matrix and return the results with a new Matrix
 override final def  map( f: (Double => Double)): Matrix = {
   var mres = new Matrix(Nrows-1, Ncols-1)
    var r = 1; var c = 1
    while  (r <  Nrows) {
      c = 1
      while (c <  Ncols)  {
       mres(r, c) = f(this(r, c) )
       c += 1
      }
      r += 1
    }
   
   mres
 }
 
  // forall returns a boolean indicating whether the predicate p holds for all the elements of the matrix
 override final def forall( p: (Double => Boolean)) = {
   var  isTrueForAll = true
   var r = 1; var c = 1
    while( r < Nrows)  {
      c = 1
     while (c < Ncols) {
       if ( p(this(r, c)) == false)
         isTrueForAll = false
       c += 1
     }
     r += 1
    }
   isTrueForAll
 }
  
  
  // exists returns a boolean indicating whether the predicate p holds for some element of the matrix
 override final def exists(p: (Double => Boolean)): Boolean  = {
    var r = 1; var c = 1
    while (r < Nrows) {
      c = 1
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
override final def filter(p: (Double => Boolean)): Matrix = {
   var fres = new Matrix(Nrows-1, Ncols-1)  // construct the matrix to keep the filtered result
   var r = 1; var c = 1  
   while ( r <  Nrows) {
       c = 1
       while (c <  Ncols) {
         if ( p(this(r, c)) == true)    // element passed the predicate condition
            fres(r, c) = this(r, c)
         c += 1
       }
       r += 1
   }
   
   fres   // return the matrix consisting of the elements that pass the filter condition
}   
  
  override final def clone() = {
    var a = new Matrix(this)
    a
  }
  
 final def copy() = {  // same as clone()
    clone()
  }
  
  
  // copy to a new matrix, perhaps resizing also matrix
  final def copy(newNrows: Int, newNcols: Int)  =  {
    var cpMat = new Matrix(newNrows, newNcols)   // create a new Matrix 
    val mnNrows = if (newNrows < Nrows)  newNrows else Nrows
    val mnNcols = if (newNcols < Ncols)   newNcols else Ncols
      // copy the original matrix whithin
    var r = 1; var c = 1
    while (r < mnNrows) {
      c = 1
      while (c < mnNcols) {
        cpMat(r, c) = this(r, c)
        c += 1
      }
      r += 1
    }
    cpMat
    
  }
  


  // clone a Matrix from another Matrix
final def this(a: Matrix) = {
     this(a.Nrows-1, a.Ncols-1)  // allocate memory with the default constructor
    var i=1; var j=1
    while (i<a.Nrows) {
        j=1
        while (j<a.Ncols) {
       v(i)(j) = a.v(i)(j)
       j+=1
        }
        i+=1
     }
 }
     


 override  final def print() =  {
   val digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var r = 1; var c = 1
    while (r <  Nrows) {
        c = 1
        while (c <  Ncols)  {
            System.out.print(digitFormat.format(this(r, c)) + "  ")
            c += 1
        }
        r += 1
          println("\n")
     }
   }
    
   
   
  
override final def p = print   // short "print" method


override  final def  toString(): String = {
  if (scalaSci.PrintFormatParams.getVerbose==true)  {
    var digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var nelems = Nrows
    var melems = Ncols
    var truncated = false

    var truncationIndicator = ""
     if  (Matrix.mxElemsToDisplay < Nrows) {
        nelems = Matrix.mxElemsToDisplay
        truncationIndicator = " ... "
        truncated = true
      }
     if  (Matrix.mxElemsToDisplay < Ncols) {
        melems = Matrix.mxElemsToDisplay
        truncationIndicator = " ... "
      }
     
    var s:String =" \n"
     var i=1; var j=1;
    while (i<nelems) {
        j=1
        while (j<melems) {
       s = s+digitFormat.format(v(i)(j))+"  "
       j+=1
        }
     s += (truncationIndicator+"\n")
     i+=1
    }
 if (truncated)     // handle last line
    s+= "........................................................................................................................................."
     
      s
  }
  else  // not verbosing, thus empty String 
    ""
     
 }
  

 
  
  
/* extract the columns specified with  the array colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 The columns at the new matrix are arranged in the order specified with the array colIndices
 e.g. 
 var testMat = M1(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(3, 1)
 var extract3_1cols = testMat(::, colIndices)
   */
  override final def apply(allRowsSymbol: ::.type, colIndices: Array[Int]): Matrix = {
    var lv = colIndices.length
    if (lv > numColumns())  // do nothing 
      {
        println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols)
        this
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  colFiltered = new Matrix(numRows(), lv)
      var col = 0; var row = 0
      while  (col< lv)  {
           var currentColumn = colIndices(col)  // the specified column
           row = 0
           while  (row < numRows()) {  // copy the corresponding row
               colFiltered(row+1, col+1) = this(row+1, currentColumn)
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
 var testMat = M1(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 var rowIndices = Array(3, 1)
 var extract3_1rows = testMat(rowIndices, ::)
   */
  override final def apply(rowIndices: Array[Int], allColsSymbol: ::.type): Matrix = {
    var lv = rowIndices.length
    if (lv > numRows())  // do nothing
      {
        println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows)
        this
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      var  rowFiltered = new Matrix(lv,  numColumns())
      var row = 0; var col = 0
      while  (row <  lv)  {
           var currentRow = rowIndices(row)  // the specified row
           col = 0
           while  (col < numColumns()) {  // copy the corresponding row
               rowFiltered(row+1, col+1) = this(currentRow, col+1)
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
 var testMat = M1(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(true, false, true, false)
 var extract0_2cols = testMat(::, colIndices)
   */
  override final def apply(allRowsSymbol: ::.type, colIndices: Array[Boolean]): Matrix = {
    var lv = colIndices.length
    if (lv != numColumns())  // do nothing
      {
        println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols)
        this
      }
      else {  // dimension of array with column indices to use is correct
        // count the number of trues
        var ntrues = 0
        var k = 0 
       while ( k <  numColumns()) {
          if (colIndices(k)==true)  
            ntrues += 1
          k += 1
       }
        
      // allocate array
      var  colFiltered = new Matrix(numRows(), ntrues)
      var currentColumn=1
      var col = 0; var row = 0
      while (col < numColumns())  {
         if (colIndices(col))   { // copy the corresponding column
             row = 0
            while  (row <  numRows())  {
               colFiltered(row+1, currentColumn) = this(row+1, col+1)
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
 var testMat = M1(" 1.0 2.0 3.0 ; 5.0 6.0 7.0 ; 8 9 10 ; 11 12 13")
 var rowIndices = Array(false, true, false, true)
 var extract1_3rows = testMat(rowIndices, ::)
   */
  override final def apply(rowIndices: Array[Boolean], allRowsSymbol: ::.type): Matrix= {
    var lv = rowIndices.length
    if (lv != numRows())  // do nothing
      {
        println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows)
        this
      }
      else {  // dimension of array with row indices to use is correct
        // count the number of trues
        var ntrues = 0
        var k = 0
        while  ( k <  numRows()) {
          if (rowIndices(k))  
            ntrues += 1
          k += 1
        }
        
      // allocate array
      var  rowFiltered = new Matrix(ntrues, numColumns())
      var currentRow=1
      var row = 0;  var col = 0
      while  (row <  numRows())  {
          if (rowIndices(row))  {  // copy the corresponding row
           col = 0  
           while (col < numColumns()) {
               rowFiltered(currentRow, col+1) = this(row+1, col+1)
               col += 1
           }
             currentRow += 1
          }
          row  += 1
      }
        rowFiltered
        }  // dimension of array with row indices to use is correct 
      
    }
    
  
 
// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ':') corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummySymbol). if low>high then rows are returned in reverse
 override final def apply(rowL: Int, rowH: Int, allColsSymbol: ::.type): Matrix = {
   var rowStart = rowL; var rowEnd=rowH;
   var colStart = 1;     var colEnd =  Ncols-1;   // all columns
   var colNum = Ncols-1
   var colInc = 1

if (rowStart <= rowEnd) {   // positive increment
    var rowInc = 1   
    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    var rowNum = rowEnd-rowStart+1 
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx =1; var colIdx = 1  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd )   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row at the source matrix
    var ccol = colStart
    var rowIdx =1; var colIdx = 1  // indexes at the new Matrix
    while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }

subMatr   // return the submatrix
        
} // rowStart > rowEnd
  
}

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 4, 12, ::) corresponds to Matlab's m(2:4:12, :)'
override final def apply(rowL: Int, rowInc: Int, rowH: Int, allColsSymbol: ::.type): Matrix = {
    var rowStart = rowL;     var rowEnd =  rowH;
    var colStart = 1;  var colEnd = Ncols-1;   // all columns
    var colNum = Ncols-1
    var colInc = 1

  if (rowInc > 0) { // positive increment
    if (rowEnd == -1) { rowEnd = Nrows-1 }  // if -1 is specified take all the rows
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colStart = 1;     var colEnd =  Ncols-1   // all columns
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
     var subMatr = new Matrix(rowNum, colNum)  // create a Matrix to keep the extracted range
        // fill the created matrix with values
     var  crow = rowStart   // indexes current row at the source matrix
     var  ccol = colStart
     var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix
     while (crow >= rowEnd)  {
         ccol = colStart;  colIdx = 1
         while (ccol <= colEnd)  {
             subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
             colIdx += 1
             ccol += colInc
         }
         rowIdx += 1
         crow += rowInc
      }
       subMatr  // return the submatrix
     }  // negative increment
}


// extracts a submatrix, e.g. m( ::, 2, 3 ) corresponds to Matlab's m(:, 2:3)'
  override final def apply(allRowsSymbol: ::.type, colLow: Int, colHigh: Int): Matrix = {
    var rowStart = 1;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows-1    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        var colInc = 1
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = colEnd-colStart+1;
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEbd
 subMatr
} // positive increment
  else {  // negative increment
    var colInc = -1
    var colNum = colStart-colEnd+1;
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr   // return the submatrix
   }
   
  }



// extracts a submatrix, e.g. m( ::, 2, 3, 12 ) corresponds to Matlab's m(:, 2:3:12)'
  override final def apply(allRowSymbol: ::.type, colLow: Int, colInc: Int, colHigh: Int): Matrix = {
   var rowStart = 1;     var rowEnd =  Nrows-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = Nrows-1    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1 } // if -1 is specified take all the columns
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix
 
           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
  override final def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): Matrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh
    
        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      
    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow > rowEnd
 subMatr   // return the submatrix
   }

   }




// extracts a specific row, take all columns, e.g. m(2, ::) corresponds to Matlab's m(2, :)'
  override final def apply(row: Int, allColsSymbol: ::.type): RichDouble1DArray = {
    var subMatr = new Array[Double](Ncols-1)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var ccol = 1
    while  (ccol <  Ncols)   {
          subMatr(ccol-1) = this(row, ccol)
          ccol += 1
         }

     new RichDouble1DArray(subMatr)
}


// extracts a specific column, take all rows, e.g. m(::, 2) corresponds to Matlab's m(:,2:)'
  override final def apply(allRowsSymbol: ::.type, col: Int): RichDouble1DArray = {
    var subMatr = new Array[Double](Nrows-1)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = 1
    while  (crow < Nrows)   {
          subMatr(crow-1) = this(crow,  col)
          crow += 1
         }

     new RichDouble1DArray(subMatr)
}


   
// extracts a submatrix, e.g. m( 2,  12, 4,   8 ) corresponds to Matlab's m(2:12, 4:8)'
  override final def apply(rowLow: Int,  rowHigh: Int, colLow: Int, colHigh: Int): Matrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh
    var rowInc = if (rowHigh > rowLow) 1 else -1
    var colInc = if (colHigh > colLow) 1 else -1

        var rowNum = Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      
    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol <= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
    var rowIdx = 1; var colIdx = 1  // indexes at the new Matrix

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 1
          while  (ccol >= colEnd)   {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
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
  override final def apply(rowLow: Int, rowInc: Int, rowHigh: Int): Matrix = {
    var rowStart = rowLow;     var rowEnd =  rowHigh;    if (rowEnd < rowStart) { rowStart = rowHigh; rowEnd = rowLow; }
    var colStart = 1;     var colEnd =  Ncols-1;
    var colInc = 1
    var rowNum = Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colNum = Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = new Matrix(rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var inc = 1
    var ccol = colStart
    var rowIdx =1; var colIdx = 1;  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)    {
                subMatr.v(rowIdx)(colIdx) = v(crow)(ccol)
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }
     subMatr
}




  // updating a single element of the Matrix, without resizing
final def  update(n: Int, m: Int, value: Double )  {
         v(n)(m) = value;
   }

  
  
  // update a single row with index r to have the value v
 override final def update(r: Int, colonSymbol:  ::.type, v: Double)   {
   val  row  = if ( r < 1)  Nrows+r else r
   var i = 1
   while (i < Ncols) {
     this(row, i) = v
     i += 1
   }
 }
  
  
  // update a single column with index c  to have the value v
 override final def update(colonSymbol:  ::.type, c: Int,  v: Double)   {
   val  col  = if ( c < 1)  Ncols+c else c
   var i = 1
   while (i < Nrows) {
     this( i, col ) = v
     i += 1
   }
 }
  
  
 override final def update(colonSymbol: ::.type, c: Int, v: Vec)  {
   if (v.length != Nrows-1)
      throw new IllegalArgumentException("Nrows (%d) != v.length (%d)".format(Nrows, v.length))
   val col = if (c < 1) Ncols + c  else c
   var i = 1
   while (i <= v.length)  {
     this(i, col) = v(i-1)
     i += 1
   }
 }
 
 override final def update(r: Int, colonSymbol: ::.type, v: Vec)  {
   if (v.length != Ncols-1)
     throw new IllegalArgumentException("Ncols (%d) != v.length (%d)".format(Ncols, v.length))
   val row = if (r < 1) Nrows + r else r
   var i=1
   while (i <= v.length) {
     this(row, i) = v(i-1)
     i += 1
   }
 }
 
  
// update a Matrix subrange by assigning a Matrix, e.g. var m=rand0(20, 30);  m( 2, 1, 3, 3, 1, 4) = ones0(2,2)
 override final def update(rlowp:Int,  clowp:Int,   rincp: Int, cincp: Int, mr: Matrix ): Unit = {
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
                return
            }
            var tmp=rhighp; var rhigh=rlowp; rlow = tmp;
            rinc  = -rinc
        }
    if (chigh < clow)  {
            if (cinc > 0)  {
                println("negative column subrange increment is required")
                return
            }
            var tmp=chighp; var chigh=clowp; clow = tmp;
            cinc  = -cinc
        }
      
     var rangeLenRow = ((rhigh-rlow)/rinc).asInstanceOf[Int]+1    // row length of target range
     var rangeLenCol = ((chigh-clow)/cinc).asInstanceOf[Int]+1    // col length of target range

     
      if (rhigh >= Nrows || chigh >= Ncols)  {  
         println("error accessing matrix element out of range: row = "+rhigh+", col = "+chigh)
         return 
    }   


     // copy the values of the mr
        var rrow=1; var rcol=1; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=1
            lcolidx = clow   // starting column within the "subassigned" matrix
            while (rcol < mr.Ncols)  {   // for all cols of the right-hand side matrix
                v(lrowidx)(lcolidx) = mr(rrow, rcol)
                lcolidx += cinc
                rcol += 1
            }
            lrowidx += rinc
            rrow += 1
          }
          return 
        }



// update a Matrix subrange by assigning a Matrix, e.g. var mm = rand(20, 30);  mm(2, 3, ':') = ones(2,2);
 override final def update(rlowp:Int, clowp:Int, ch: ::.type, mr: Matrix): Unit = {
    val mrM = mr.Nrows   // length of right-hand side matrix
    val mrN = mr.Ncols
    var rlow=rlowp; var rhigh=rlow+mrM; var rinc = 1;
    var clow=clowp; var chigh=clow+mrN; var cinc = 1;
        
     if (rhigh >= Nrows || chigh >= Ncols)  {  
          println("error accessing matrix element out of range: row = "+rhigh+", col = "+chigh)
          return 
      }   


     // copy the values of the mr
        var rrow=1; var rcol=1; var lrowidx=rlow; var lcolidx = clow
        while (rrow < mr.Nrows) {   // for all rows of the right-hand side matrix
            rcol=1
            lcolidx = clow   // starting column within the "subassigned" matrix
            while (rcol < mr.Ncols)  {   // for all cols of the right-hand side matrix
                v(lrowidx)(lcolidx) = mr(rrow, rcol)
                lcolidx += cinc
                rcol += 1
            }
            lrowidx += rinc
            rrow += 1
          }
         return 
        }

  
  

// Row append and prepend routines    
/*
 var  mm = rand1(4, 5)
 var mmo = ones1(2, 5)
 var mmappend = mm RA mmo  // prepend mmo 
 var mmprepend = mm RA mmo // append mmo 
   */   
final def  RA(rowsToAppend: Matrix): Matrix = {
  new Matrix( ( new RichDouble2DArray(this) RA new RichDouble2DArray(rowsToAppend)).getv)
}  
  

override final def  RA(rowToAppend: RichDouble1DArray): Matrix = {
   new Matrix((new RichDouble2DArray(this)  RA rowToAppend).getv)
 }
 
  
override final def  RA(rowToAppend: scalaSci.Vec): Matrix = {
   new Matrix((new RichDouble2DArray(this)  RA rowToAppend).getv)
 }
 
final def  RP(colsToAppend: Matrix): Matrix = {
    new Matrix( (new RichDouble2DArray(this) RP new RichDouble2DArray(colsToAppend)).getv)
}




  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: Array[Double]): Matrix = {
   new Matrix((new RichDouble2DArray(this)  RP rowToPrepend).getv)
}

  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: RichDouble1DArray): Matrix = {
    this  RP rowToPrepend.getv 
}
  
  // prepend rowwise the 1-d array rowToPrepend
override final def  RP(rowToPrepend: scalaSci.Vec): Matrix = {
   this RP rowToPrepend.getv 
}

  
//  perhaps easier to remember alias
//  perhaps easier to remember alias
 
final def   >> (rowsToAppend: Matrix): Matrix = 
    this RA  rowsToAppend 
 
override final def   >>  (rowToAppend: Array[Double]): Matrix = 
     this RA rowToAppend 

override final def   >> (rowToAppend: RichDouble1DArray): Matrix = 
     this RA rowToAppend 
 
override final def  >>(rowToAppend: scalaSci.Vec): Matrix =
    this RA rowToAppend 
  
final def  <<(rowsToPrepend: Matrix): Matrix = 
    this RP rowsToPrepend
 
override final def   << (rowToPrepend: Array[Double]): Matrix = 
   this RP rowToPrepend   

override  final def  <<(rowToPrepend: RichDouble1DArray): Matrix = 
   this RP rowToPrepend 
 
override  final def  << (rowToPrepend: scalaSci.Vec): Matrix =
   this RP rowToPrepend   
 
final def  CA(colsToAppend: Matrix): Matrix = {
      new Matrix( (new RichDouble2DArray(this) CA new RichDouble2DArray(colsToAppend)).getv)
}

  // append an Array[Double] as the last column
override  final def  CA(colsToAppend: Array[Double]): Matrix = {
      new Matrix( (new RichDouble2DArray(this) CA colsToAppend ).getv)
}

  // append a RichDouble1DArray as the last column
override final def  CA (colsToAppend: RichDouble1DArray): Matrix = {
     this CA colsToAppend.getv
  }

   // append a scalaSci.Vec as the last column
override  final def CA (colsToAppend: scalaSci.Vec): Matrix  = {
     this CA colsToAppend.getv  
  }
  

// prepend a Mat  
final def  CP(colsToPrepend: Matrix): Matrix = {
    new Matrix( (new RichDouble2DArray(this) CP  new RichDouble2DArray(colsToPrepend)).getv)
}


// prepend an Array[Double] to matrix
override  final def  CP(colsToPrepend:  Array[Double]): Matrix = {
    new Matrix( (new RichDouble2DArray(this) CP colsToPrepend).getv)
}

  // prepend a RichDouble2DArray
override  final def CP(colToPrepend: RichDouble1DArray): Matrix = 
   this CP colToPrepend.getv 

 // prepend a scalaSci.Vec
override  final def CP(colToPrepend: scalaSci.Vec): Matrix = 
   this CP colToPrepend.getv 

 
//  perhaps easier to remember alias
final def   >>>(colsToAppend: Matrix): Matrix = 
    this CA colsToAppend  
 
override  final def   >>>(colToAppend: Array[Double]): Matrix = 
   this CA colToAppend 

override  final def   >>>(colToAppend: RichDouble1DArray): Matrix = 
   this CA colToAppend 
 
override  final def  >>>(colToAppend: scalaSci.Vec): Matrix =
   this CA colToAppend 
  
final def   <<<(colsToPrepend:  Matrix): Matrix = 
   this CP colsToPrepend  
 
override  final def   <<<(colToPrepend: Array[Double]): Matrix = 
   this CP colToPrepend 

override  final def   <<<(colToPrepend: RichDouble1DArray): Matrix = 
    this CP colToPrepend 
 
override final def  <<<(colToPrepend: scalaSci.Vec): Matrix =
   this CP colToPrepend 
   


    // Matrix -< Vec : column oriented addition
override final def -< (that: Vec): Matrix =  {
    var vl = that.length
    if (vl != this.Nrows) 
      return this

      var nv = new Matrix(this.Nrows, this.Ncols)
   var i=1; var j=1
   while (j<=Ncols) {   // for all columns
       i=1
    while (i<=Nrows) {  // for all rows 
      nv.v(i)(j) = v(i)(j)-that(i-1)
      i +=1
    }
    j += 1
   }
   return nv
}

   // Matrix +< Vec : column oriented addition
override final def +< (that: Vec): Matrix =  {
    var vl = that.length
    if (vl != this.Nrows) 
      return this

      var nv = new Matrix(this.Nrows, this.Ncols)
   var i=1; var j=1
   while (j<=Ncols) {   // for all columns
       i=1
    while (i<=Nrows) {  // for all rows 
      nv.v(i)(j) = v(i)(j)+that(i-1)
      i +=1
    }
    j += 1
   }
   return nv
}

  /*
   var v=vrand(5);  var m= rand(5); var rr = m+v
   */
  override final def + (that: Vec): Matrix =  {
    var vl = that.length
    if (vl != this.Nrows) 
      return this

      var nv = new Matrix(this.Nrows, this.Ncols)
   var i=1; var j=1
   while (j<=Ncols) {   // for all columns
       i=1
    while (i<=Nrows) {  // for all rows 
      nv.v(i)(j) = v(i)(j)+that(i-1)
      i +=1
    }
    j += 1
   }
   return nv
}

   // Mat (NXM)*< Vec(MX1) : Mat(NX1) Matrix-Vector multiplication
override final def *< (that: Vec): Matrix =  {
    var vl = that.length
    if (vl != this.Ncols ) 
      return this
   
    var sm=0.0
    var nv = new Matrix(this.Nrows, 1)
    var r = 1; var c = 1
    while  (r<=  Nrows) {      // all rows of the Mat
     sm=0.0
     c = 1
     while  (c <= Ncols-1) {
       sm += v(r)(c)*that(c-1)
       c += 1
     }
     nv(r,1) = sm
     
      r += 1
    }
   
   nv
}

// cross (pointwise) product of a Matrix with a Matrix
final def  cross(that: Matrix):  Matrix = {
  var nv = new Matrix(this.Nrows-1, this.Ncols-1 )
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols)  {
      nv.v(i)(j) = v(i)(j) * that.v(i)(j)
      j += 1
    }
    i += 1
   }
 return nv
}

// cross (pointwise) product of a Matrix with an Array[Array[Double]]
override final def  cross(that: Array[Array[Double]]):  Matrix = {
  var nv = new Matrix(this.Nrows-1, this.Ncols-1 )
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols)  {
      nv.v(i)(j) = v(i)(j) * that(i)(j)
      j += 1
    }
    i += 1
   }
 return nv
}


// dot  product of a Matrix with a Matrix
final def  dot(that: Matrix):  Double = {
  var dotProduct = 0.0
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols)  {
      dotProduct += v(i)(j) * that.v(i)(j)
      j += 1
    }
    i += 1
   }
 dotProduct
}

// dot product of a Matrix with an Array[Array[Double]]
override final def  dot(that: Array[Array[Double]]):  Double = {
   var dotProduct = 0.0 
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols)  {
      dotProduct += v(i)(j) * that(i)(j)
      j += 1
    }
    i += 1
   }
 dotProduct
}

// dot product of a Matrix with an Array[Double]
override final def  dot(that: Array[Double]):  Double = {
   var dotProduct = 0.0 
   var thatLen = that.length
   if (thatLen == Nrows) {  // rowwise
   var row = 1
   while (row < Nrows) {
      dotProduct += this(row, 1) * that(row)
      row += 1
    }
    dotProduct
   }
   else if  (thatLen == Ncols)  {  // columnwise
     var col = 1
     while (col < Ncols) {
       dotProduct += this(1, col) * that(col)
       col += 1
     }
     dotProduct
   }
   else 
     dotProduct
}

  
// dot product of a Matrix with a Vec
override final def  dot(that: Vec):  Double = {
   var dotProduct = 0.0 
   var thatLen = that.length
   if (thatLen == Nrows) {  // rowwise
   var row = 1
   while (row < Nrows) {
      dotProduct += this(row, 1) * that(row)
      row += 1
    }
    dotProduct
   }
   else if  (thatLen == Ncols)  {  // columnwise
     var col = 1
     while (col < Ncols) {
       dotProduct += this(1, col) * that(col)
       col += 1
     }
     dotProduct
   }
   else 
     dotProduct
}

// IN-PLACE Operations: Update directly the receiver, avoiding creating a new return object

    // Matrix + Matrix
final def ++ (that: Matrix): Matrix =  {
  if (Nrows != that.Nrows || Ncols != that.Ncols)  // incompatible dimensions
      this
     else {
         var i=1; var j=1;
        while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) += that(i, j)
      j +=1
    }
    i += 1
   }
   this
  }
}

    // Matrix + Double
override  final def ++ (that: Double): Matrix =  {
  var i=1; var j=1
  while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) += that
      j +=1
    }
    i += 1
   }
   this
}

 
    // Matrix - Matrix
final def -- (that: Matrix): Matrix =  {
  if (Nrows != that.Nrows || Ncols != that.Ncols)  // incompatible dimensions
      this
     else {
         var i=1; var j=1;
        while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) -= that(i, j)
      j +=1
    }
    i += 1
   }
   this
  }
}

    // Matrix - Double
override  final def -- (that: Double): Matrix =  {
  var i=1; var j=1
  while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) -= that
      j +=1
    }
    i += 1
   }
   this
}

 

    // Matrix * Double
override  final def ** (that: Double): Matrix =  {
  var i=1; var j=1
  while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) *= that
      j +=1
    }
    i += 1
   }
   this
}


    // Matrix / Double
override  final def /| (that: Double): Matrix =  {
  var i=1; var j=1
  while (i<Nrows) {
       j=1
    while (j<Ncols) {
      this(i, j) /= that
      j +=1
    }
    i += 1
   }
   this
}




    // END OF IN-PLACE OPERATIONS
     
     // Matrix + Long
final def ++ (that: Long): Matrix =  {
  var i=1; var j=1
  while (i<Nrows) {
       j=1
    while (j<Ncols) {
      v(i)(j) += that
      j +=1
    }
    i += 1
   }
   this
}



    // Matrix + Double
override final def + (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)+that
      j +=1
    }
    i += 1
   }
   return nv
}

  // Matrix - Double
override final def - (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)-that
      j +=1
    }
    i += 1
   }
   return nv
}

  
  // Matrix * Double
override final def * (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)*that
      j +=1
    }
    i += 1
   }
   return nv
}

   
  // Matrix /  Double
override final def / (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)/that
      j +=1
    }
    i += 1
   }
   return nv
}

    // Matrix + Double, for example at:   var m = ones(3,4); var mm = 10.0 +: m,
  // the implicit conversion of 10.0 to RichNumber is avoided since the expression is evaluated as m.+(10.0)
final def +: (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)+that
      j +=1
    }
    i += 1
   }
   return nv
}

final def -: (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
       j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)-that
      j +=1
    }
    i += 1
   }
   return nv
}



// Matrix * Double
final def *: (that: Double): Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols) {
      nv.v(i)(j) = v(i)(j)*that
      j += 1
    }
    i += 1
   }
   return nv
}

    // unary Minus applied to a Matrix implies negation of all of its elements
override final def unary_- : Matrix =  {
      var nv = new Matrix(this.Nrows-1, this.Ncols-1)  // get a Matrix of the same dimension
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols) {
      nv.v(i)(j) = -v(i)(j)  // negate element
      j += 1
    }
    i += 1
   }
   return nv
}

// transpose the Matrix
override final def trans: Matrix = {
   var nv = new Matrix(this.Ncols-1, this.Nrows-1)  // get a Matrix of dimension MXN
   var i=1; var j=1
   while (i<Nrows) {
     j=1
    while (j<Ncols) {
      nv.v(j)(i) = v(i)(j)  // negate element
      j += 1
    }
    i += 1
   }

   return nv
}


// transpose the Matrix
override final def T: Matrix = {
   var nv = new Matrix(this.Ncols-1, this.Nrows-1)  // get a Matrix of dimension MXN
   var i=1; var j=1;
   while (i<Nrows) {
     j=1
    while (j<Ncols) {
      nv.v(j)(i) = v(i)(j)  // negate element
      j += 1
    }
    i += 1
   }

   return nv
}

   

// Matrix + Matrix
final def  + (that: Matrix): Matrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;  // receiver's dimensions
   var sN = that.Nrows;  var sM = that.Ncols;  // parameter's dimensions

  if (rN == sN && rM == sM)  {     // same dimensions
       var nv = new Matrix(sN-1, sM-1)
       var i=1; var j=1;
   while (i<sN)  {
        j=1
       while (j<sM)  {
           nv.v(i)(j) = v(i)(j) + that.v(i)(j)
           j+=1
     }
     i += 1
   }
   return nv
  }
  else { // incompatible dimensions
   that
     }
}




// Matrix - Matrix
final def  -  (that: Matrix): Matrix =  {
   var rN = this.Nrows;   var rM = this.Ncols;
   var sN = that.Nrows;  var sM = that.Ncols;
  
    if (rN == sN && rM == sM)  {     // same dimensions
       var nv = new Matrix(sN-1, sM-1)
       var i=1; var j=1;
   while (i<sN)  {
       j=1
    while (j<sM)  {
           nv.v(i)(j) = v(i)(j) - that.v(i)(j)
           j += 1
          }
         i += 1
    }
   return nv
  }  // same dimensions
  else { // incompatible dimensions
   return  that
     }
}

// Matrix * Matrix
 final def * (that: Matrix): Matrix =  {
   var  rN = this.Nrows;   var rM = this.Ncols;
   var  sN = that.Nrows;  var sM = that.Ncols;
   
    var  v1Colj = new Array[Double](rM)
   var result = new Matrix(this.Nrows-1, that.Ncols-1)
   var j=1; var k=1;
   while (j < sM)  {
       k=1
      while  (k < rM) {
        v1Colj(k) =that.v(k)(j)
        k += 1
      }
    
      var i=1;
      while (i<rN) {
        var   Arowi = this.v(i)
        var   s = 0.0;
        k=1
        while (k< rM) {
          s += Arowi(k)*v1Colj(k)
          k += 1
        }
      result.v(i)(j) = s;
      i += 1
      }
 j += 1
   }
  return result
  }


// Matrix + Array[Array[Double]]
 final def + (that: Array[Array[Double]]): Matrix =  {
   this + new Matrix(that)
}



// Matrix + RichDouble2DArray
 override final def + (that: RichDouble2DArray): Matrix =  {
    this + new Matrix(that.getv)
}

// Matrix - RichDouble2DArray
 override final def - (that: RichDouble2DArray): Matrix =  {
    this - new Matrix(that.getv)
}

  // Matrix * RichDouble2DArray
 override final def * (that: RichDouble2DArray): Matrix =  {
    this * new Matrix(that.getv)
}

// Matrix - Array[Array[Double]]
 final def - (that: Array[Array[Double]]): Matrix =  {
   this - new Matrix(that)
  }
  
  
// Matrix * Array[Array[Double]]
 final def * (that: Array[Array[Double]]): Matrix =  {
   var  rN = this.Nrows;   var rM = this.Ncols;
   var  sN = that.length;  var sM = that(0).length

  var  vr:Matrix =new Matrix(rN-1, sM-1)

  
   var   v1Colj = new Array[Double](rM)

   var j=1; var k=1;
   while (j < sM)  {
       k=1
      while  (k < rM) {
        v1Colj(k) =that(k)(j)
        k += 1
      }

      var i=1;
      while (i<rN) {
        var   Arowi = this.v(i)
        var   s = 0.0;
        k=1
        while (k< rM) {
          s += Arowi(k)*v1Colj(k)
          k += 1
        }
      vr.v(i)(j) = s;
      i += 1
      }
 j += 1
   }
  return vr
  }



override final def ~() =  {
var  transposed = new Matrix(this.Ncols-1, this.Nrows-1)
    var r = 1; var c = 1
    while  (r <=  this.Ncols-1) {
      c = 1
      while  (c <=  this.Nrows-1) {
        transposed(r,c) = this(c,r)
        c += 1
      }
    r += 1
    }
   transposed
} 


  
  // IN-PLACE OPERATIONS
override final def absi()  = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.abs(this(i, j))
               j += 1
            }
            i += 1
       }
       this   
    }



override final def sini() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sin(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }



override final def cosi()  = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.cos(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

override final def tani() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.tan(this(i, j))
               j += 1
            }
            i += 1
       }
       this
     }

override final def asini() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.asin(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  
override final def acosi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.acos(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }
    
    
override final def atani() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.atan(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }


override final def sinhi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sinh(this(i, j))
               j += 1
            }
            i += 1
       }
      this   
    }

  
  

override final def coshi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.cosh(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  
override final def tanhi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.tanh(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }
  
  
override final def powi(v: Double) = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.pow(this(i, j), v)
               j += 1
            }
            i += 1
       }
          this
    }
    
  
override final def logi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  
override final def log2i() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       val conv = java.lang.Math.log(2.0)
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          this
    }
    
 
override final def log10i() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       val conv = java.lang.Math.log(10.0)
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          this
    }
    
  override final def ceili() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.ceil(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  override final def floori() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.floor(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

    
  override final def roundi() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.round(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }

  override final def sqrti() = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               this(i,j) = java.lang.Math.sqrt(this(i, j))
               j += 1
            }
            i += 1
       }
          this
    }
    
  //  matrix common operations
override final def abs(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.abs(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }



override  final def sin(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sin(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }



override  final def cos(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.cos(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

override  final def tan(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.tan(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

override  final def asin(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.asin(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  
override  final def acos(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.acos(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }
    
    
override  final def atan(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.atan(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }


override  final def sinh(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sinh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  
  

override  final def cosh(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.cosh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  
override final def tanh(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.tanh(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }
  
  
override final def pow(v: Double): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.pow(this(i, j), v)
               j += 1
            }
            i += 1
       }
          nm
    }
    
  
override  final def log(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  
override final def log2(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       val conv = java.lang.Math.log(2.0)
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          nm
    }
    
 
override final def log10(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       val conv = java.lang.Math.log(10.0)
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.log(this(i, j)/conv)
               j += 1
            }
            i += 1
       }
          nm
    }
    
  override final def ceil(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.ceil(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  override  final def  floor(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.floor(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

    
  override  final def  round(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.round(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }

  override  final def sqrt(): Matrix = {
       var Nrows = this.Nrows; var Ncols = this.Ncols;
       var nm = new Matrix(Nrows, Ncols)
       var i=1; var j=1;
       while  (i< Nrows) {
            j=1
            while (j < Ncols) {
               nm(i,j) = java.lang.Math.sqrt(this(i, j))
               j += 1
            }
            i += 1
       }
          nm
    }
    
  
      // e.g. 
  //   var   rdd = rand1(20, 30)
  //   final def pred(k: Int) = if (k % 2 == 0) true else false
//     rdd.filterRows(pred)
// return rows according to the predicate
  override final def  filterRows( predicate:  Int  => Boolean) = {
      var rowCnt = 0
      var r = 1  
      while (r <=  this.numRows()) {
          if (predicate(r))
           rowCnt += 1
         r += 1
     }
 
    var newMat = new Matrix(rowCnt, this.numColumns())
    var rCnt=0
    r = 1; var c = 1
    while  ( r <=  this.numRows())  {
      if (predicate(r)) {  // copy the row
        c = 1 
        while  (c <=  this.numColumns()) {
           newMat(rCnt+1,c) = this(r,c)
           c += 1
        }
          rCnt += 1
          }
          r += 1
          
     }
                    
    newMat     
  
  }		 

  // return cols according to the predicate
 override final def  filterColumns(predicate:  Int => Boolean) = {
    var colCnt = 0
    var c = 1
    while  (c <=  numColumns()) {
      if (predicate(c))
       colCnt += 1
     c += 1
  }
  var cCnt=0
  var newMat = new Matrix(this.numRows(), colCnt)
    c = 1; var r = 1
    while  (c <=   this.numColumns()) {
      if (predicate(c) )  {  // copy the column
        r = 1
        while  (r <= this.numRows())  {
         newMat(r, cCnt+1) = this(r,c)
         r += 1
        }
      cCnt += 1
      }
      c += 1
    }
      newMat     
  
   }		 

  
  // allows to display the contents of the matrix with a JTable and to interactively manipulate them
  override final def browse() =  { 
      scalaExec.gui.watchMatrix.display(this.getv, true);
      this.getv
    }
  // pass also the varName in order be displayed at the browser's window title
  override final def browse(varName: String) = { 
      scalaExec.gui.watchMatrix.display(this.getv, true, varName)
      this.getv
  }
  

   // COLUMN BASED ROUTINES
// columnwise sum
override final def sum(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 1; var crow = 1
    while  (ccol <=  Ncols-1) {
     sm=0.0
     crow = 1
     while  (crow<= Nrows-1) {
       sm += this.v(crow)(ccol)
       crow += 1
     }
     res(ccol) = sm 
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// columnwise mean
override final def mean(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Ncols)
    var ccol = 1; var crow = 1
    while (ccol <= Ncols-1) {
     sm=0.0
     crow = 1
     while  (crow <= Nrows-1) {
       sm += this.v(crow)(ccol)
       crow += 1
      } 
     res(ccol) = sm/(Nrows-1)
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// columnwise product
override final def prod(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Ncols)
    var ccol = 1; var crow = 1
    while  (ccol <=  Ncols-1) {
     pd=1.0
     crow = 1
     while (crow <=  Nrows-1) {
       pd *= this.v(crow)(ccol)
       crow += 1
     }
     res(ccol) = pd 
     ccol += 1
     }
  new RichDouble1DArray( res )
}


// columnwise min
override final def min(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var res = new Array[Double](Ncols)
    var ccol = 1; var crow = 2
    while  (ccol <=  Ncols-1) {
     var mn = this.v(1)(ccol)  
     crow = 2
     while  ( crow <=  Nrows-1)
        {
       var tmp = this.v(crow)(ccol)
       if (tmp < mn)  mn = tmp
       crow += 1
       }
     res(ccol) = mn
     ccol += 1
     }
    new RichDouble1DArray(res)
}


// columnwise max
override final def max(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var res = new Array[Double](Ncols)
    var ccol = 1; var crow = 2
    while  (ccol <= Ncols-1) {
     var mx = this.v(1)(ccol)  
     crow = 2
     while  (crow <=  Nrows-1)
        {
       var tmp = this.v(crow)(ccol)
       if (tmp > mx)  mx = tmp
       crow += 1
       }
     res(ccol) = mx
     ccol += 1
     }
    new RichDouble1DArray(res)
}

// ROW BASED ROUTINES

// rowwise sum
override final def sumR(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)
    var crow = 1; var ccol = 1
    while  (crow <=  Nrows-1) {
     sm=0.0  // accumulates sum of all row elements
     ccol = 1
     while (ccol <=  Ncols-1)  {
        sm += this.v(crow)(ccol)
        ccol  += 1
     }
     res(crow) = sm
     crow += 1
      }
    new RichDouble1DArray(res)
}

// rowwise mean
override  final def meanR(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var sm = 0.0
    var res = new Array[Double](Nrows)
    var crow = 1; var ccol = 1
    while  (crow <=  Nrows-1) {
     sm=0.0
     ccol = 1
    while  (ccol <=  Ncols-1)  {
       sm += this.v(crow)(ccol)
       ccol += 1
      }
     res(crow) = sm/(Ncols-1)
     crow += 1
      }
    new RichDouble1DArray(res)
}

// rowwise product
override  final def prodR(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var pd = 1.0
    var res = new Array[Double](Nrows)
    var crow = 1; var ccol = 1
    while  (crow <=  Nrows-1) {
     pd=1.0
     ccol = 1
     while (ccol <=  Ncols-1) {
       pd *= this.v(crow)(ccol)
       ccol += 1
     }
     res(crow) = pd
     crow += 1
     }
    new RichDouble1DArray(res)
}


// rowwise min
  /*
  var N = 2500
var x = rand1(N, N)
 var r=1
  while  (r < N-1) {
    x(r, 1) =  200
    x(r, 2) = -200
    r += 1
    }


    tic; var xminWithWhile = x.minR;
    for (k<-0 until 50) {
    	xminWithWhile = x.minR; 
    }
    var tmWhile = toc
    
    tic; var xmaxWithFor = x.maxR; 
    var k=0
    while  (k< 50) {
     xmaxWithFor = x.maxR; 
     k += 1
    }
    var tmFor = toc

 // tmFor =    0.967
  // tmWhile = 0.515
  
  */ 
override  final def minR(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var res = new Array[Double](Nrows)
    var crow = 1; var ccol = 2
    while  (crow <=  Nrows-1) {
     var mn = this.v(crow)(1)
     ccol = 2
     while   (ccol <= Ncols-1)
        {
       var tmp = this.v(crow)(ccol)
       if (tmp < mn)  mn = tmp
       ccol += 1
       }
     res(crow) = mn
     crow += 1
     }
    new RichDouble1DArray(res)
}

// rowwise max
override  final def maxR(): RichDouble1DArray = {
    var Nrows = this.Nrows;     var Ncols = this.Ncols
    var res = new Array[Double](Nrows)
    var crow = 1
    while  (crow < Nrows)   {
      var mx = this.v(crow)(1)
      var ccol = 2
      while  (ccol < Ncols)
        {
       var tmp = this.v(crow)(ccol)
       if (tmp > mx)  mx = tmp
       ccol += 1
       }
     res(crow) = mx
     crow += 1
     }
    new RichDouble1DArray(res)
}

  
  // returns the corresponding row of the Mat class as an Array[Double]
override final def getRow(row: Int): Array[Double] = {
    var rowArray = new Array[Double](Ncols-1)
    var ccol = 1
    while (ccol <  Ncols)  {
       rowArray(ccol-1) = this(row, ccol)
       ccol += 1
      }
    rowArray
  }

  
// returns the corresponding row of the Mat class as an Array[Double]
override final def getCol(col: Int): Array[Double] = {
    var colArray = new Array[Double](Nrows-1)
    var rrow = 1
    while (rrow <  Nrows)  {
         colArray(rrow-1) = this(rrow, col)
         rrow += 1
    }
    colArray
  }
  

final def  det() = scalaSci.math.LinearAlgebra.LinearAlgebra.det(toDoubleArray)

final def trace() = scalaSci.math.LinearAlgebra.LinearAlgebra.trace(toDoubleArray)

final def inv() = new Matrix(scalaSci.math.LinearAlgebra.LinearAlgebra.inverse(toDoubleArray)) 

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
    val ejmlM = new scalaSci.EJML.Mat(this.toDoubleArray)
    val pejml = ejmlM.pinv
    val nrows = pejml.Nrows
    val ncols = pejml.Ncols
    var pM = new Matrix(nrows, ncols)
    for (n<-0 until nrows)
      for (m<-0 until ncols)
        pM(n, m) = pejml(n+1, m+1)
    pM
  }
  
 final def cond() = scalaSci.math.LinearAlgebra.LinearAlgebra.cond(toDoubleArray)
 
 final def rank() =  scalaSci.math.LinearAlgebra.LinearAlgebra.rank(toDoubleArray)
 
  
  final def eig() = {
    var Nrows = this.Nrows-1
    var arcp = Array.ofDim[Double](Nrows+1, Nrows+1)
      // make a copy of the matrix to pass to comeig1
    var r = 1
    while  (r <= Nrows) {
      var c = 1
      while (c <=  Nrows)  {
         arcp(r)(c) = this.v(r)(c)
         c += 1
       }
       r += 1
    }

    var  em =  new Array[Double](10)
    em(0) = 5.0e-6;   // the machine precision
    em(2)=1.0e-5;   // the relative tolerance used for the QR iteration (em(2) > em(0))
    em(4)=10.0;   // the maximum allowed number of iterations
    em(6)=1.0e-5; // the tolerance used for the eigenvectors
    em(8)=5; // the maximum allowed number of inverse iterations for the calculation of each calculation
      // arrays for the real and imaginary parts of the calculated eigenvalues of the given matrix
    var re = new Array[Double](Nrows+1); 
    var im = new Array[Double](Nrows+1);
      // the calculated eigenvectors are delivered in the columns of vec; an eigenvector corresponding to a real eigenvalue given in array re
      // is delivered in the corresponding column of array vec;
      // the real and imaginary part of an eigenvector corresponding to the first member of a nonreal complex conjugate pair
     // of eigenvalues given in the arrays re, im are delivered in the two consecutive columns of array vec corresponding to this pair
     // (the eigenvectors corresponding to the second members of nonreal complex complex conjugate pairs are not delivered,
     // since they are simply the complex conjugate of those corresponding to the first member of such pairs)
    var vec = Array.ofDim[Double](Nrows+1,Nrows+1); 
    numal.Linear_algebra.comeig1(arcp, Nrows, em, re, im, vec )

    var vecEig= Array.ofDim[Double](Nrows, Nrows)  // the eigenvectors
        // the calculated eigenvectors are delivered in the columns of vec
        // one-indexed is also the [][] vec result !!
       r = 0; var c  = 0
     while (r< Nrows) {
       c = 0
       while  (c< Nrows) {
          vecEig(r)(c) = vec(r+1)(c+1)  // copy eigenvectors
          c += 1
       }
       r += 1
     }

    var vecEiv= new Array[Double](Nrows)  // the eigenvalues
    r = 0
    while  (r<Nrows)  {
        vecEiv(r) = re(r+1)  // real eigenvalues
        r += 1
      }
 (new RichDouble2DArray(vecEig), new RichDouble1DArray(vecEiv)  )

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
    new Matrix(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }
}


// Matrix's companion object
  object Matrix  {

   
 var mxElemsToDisplay = 6
 

  // a conveniency constructor that allows to construct a matrix e.g. as
  //   var x = Matrix(3,7) instead of  var x = new Matrix(3, 7)
 final def apply(nrows: Int, ncols: Int) = new Matrix(nrows, ncols) 
     
  /* e.g.
var xx = 3.4
var a = Matrix( 2, 4,
   3.4, 5.6, -6.7, -xx,
   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/    
/// !!!date= 29 Mar
 final def apply(values: Double*)  = {
    val   nrows = values(0).toInt  //   number of rows
    val   ncols = values(1).toInt   // number of cols
    val   dvalues = values.toArray
    var   cpos = 2  // current position in array
    var   sm = new Matrix( nrows, ncols)  // create a Mat
    for (r<-1 to nrows)
      for (c<-1 to ncols)
         {
           sm(r, c) = values(cpos)  // copy value
           cpos += 1
         }

    sm  // return the constructed matrix

  }
  
    


}


