
package scalaSci.MTJ

import Jama.jMatrix
import  no.uib.cipr.matrix._ 

import scalaSci.Vec
import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray

import scala.language.postfixOps
import java.lang.Math._

 // n: matrix is nXn
// kl, ku:  number of upper and lower diagonals
class MTJBandMat( val N: Int, val kl: Int,  val ku: Int)   extends AnyRef     { //  with scalaSci.scalaSciMatrix {
  var bdm = new BandMatrix(N, kl, ku)
  
  var Nrows = N
  var Ncols = N  
  final def getv = bdm // return the data array
  
  var Blow = kl  // lower band size
  var Bupper = ku // upper band size
  
  final def size = ( kl, ku)  // returns the lower-band width and upper-band width
  final def length = bdm.getData().length
  
  final def  numRows()  = N
  final def  numColumns() =  N

  // TODOTest
  final def getLibraryMatrixRef()  = bdm

  final def matFromLibrary(nm: BandMatrix) = { new MTJBandMat(nm.numRows(), nm.numSubDiagonals(), nm.numSuperDiagonals() ) } 
  
  // TODOTest
  final def toDoubleArray = {
    var Nr = Nrows; var Nc = Ncols
    var arry = Array.ofDim[Double](Nr, Nc)
    var r = 0; var c = 0
    while (r < Nr) {
      c = 0
      while (c < Nc) {
        arry(r)(c) = this(r, c)
        c += 1
      }
      r += 1
    }
   arry
    
  }
 
    final def this(A: no.uib.cipr.matrix.Matrix, kl: Int, ku: Int) = {
    	 this(A.numRows, kl, ku)
       var Nr  = A.numRows
       var Nc = A.numColumns
       var r = 0; var c = 0
       while  (r < Nr) {
    	    c = 0
    	    while (c < Nc) {
           this(r, c) =  A.get(r, c)
           c += 1
    	   }
        r += 1
     }
    }

 
  
  final def this(v: Array[Array[Double]], kl: Int, ku: Int) = {
    this(v.length, kl, ku)
    var n = 0
    while  (n < N) {
        this(n, n) =  v(n)(n)
        n += 1
    }
// form lower band
    var rowElem=0 
    n = 0
    while  (n <  N)  {
      var lb = 1
      while  (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           this(rowElem, n) =   v(rowElem)(n)
         lb += 1
       }
    n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
       while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           this(n, colElem) =  v(n)(colElem)
         ub += 1
       }     
       n += 1
    }
  }

   
   final def solve(B: MTJBandMat, X: MTJBandMat) =    {
   	new MTJBandMat(getDM.solve(B.getDM, X.getDM), kl, ku)
}

import MTJBandMat._
  

final def getDM = bdm    // retrieves the BandMatrix MTJ representation
    
  


// indexes the corresponding Matrix element without updating automatically sizes for fast access
  final def apply(n: Int, m: Int): Double = {
        bdm.get(n, m)

 }

  
// indexes the corresponding Matrix element 
  final def apply(n: Int, m: Int, resize: Boolean): Double = {   // TODO-Sterg: implement resizing
        bdm.get(n, m)

 }
 
override final def clone() = {
  var bb = bdm.copy()
  var newMat = new  MTJBandMat(N, kl, ku)
  newMat.bdm = bb
  newMat
}

  final def copy() = {  // same as clone()
    clone()
  }

  
  

  final def getBDM = bdm    // retrieves the DenseMatrix MTJ representation

  
  // apply the function f to all the elements of the MTJBandMat and return the results with a new MTJBandMat
 final def  map( f: (Double => Double)): MTJBandMat = {
   var mres = new MTJBandMat(N, kl, ku)
   
    var r = 0
    while  (r <  kl) {
      var c = 0
      while  (c <  ku)  {
       mres(r, c) = f(this(r, c) )
       c += 1
      }
    r += 1
    }
   mres
 }
  
  // convert to Vec
 final def toVec(): Vec = {
  var v = new Vec(kl*ku)
  var cnt=0
  var r = 0
  while  (r < kl )  {
    var c = 0
    while  (c < ku)  {
      v(cnt) = this(r, c)
      cnt += 1
      c += 1
      }
    r += 1  
  }
    v
  }
    
 final def print() =  {
       var r = 0
       while (r <  N) {
         var c = 0
         while  (c <  N)  {
            System.out.print(bdm.get(r, c) + "  ")
            c += 1
         }
          println("\n")
          
      r += 1
      }
   }
   
  
final def apply(row: Int, ch: Char) : MTJBandMat = {
    var rowMat = new MTJBandMat(N, 1,  ku)
    var k = 0
    while  (k < ku) {
      rowMat(0, k) = bdm.get(row, k)
      k += 1
    }
    
    rowMat
    
  }

// extracts a submatrix specifying rows only, take all columns, e.g. m(2, 3, ::) corresponds to Matlab's m(2:3, :)'
// m(low:high,:) is implemented with m(low, high, dummyChar). if low>high then rows are returned in reverse
final def apply(rowL: Int, rowH: Int, allColsChar: ::.type): MTJBandMat = {
   var rowStart = rowL; var rowEnd=rowH;
   var colStart = 0;     var colEnd =  ku-1;   // all columns
   var colNum = ku
   var colInc = 1

    if (rowEnd == -1) { rowEnd = kl-1 }  // if -1 is specified take all the rows
    
if (rowStart <= rowEnd) {   // positive increment
    var rowInc = 1
    var rowNum = rowEnd-rowStart+1
    var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a MTJBandMat to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new Matrix
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd )   { 
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
    var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a MTJBandMat to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row at the source matrix
    var ccol = colStart
    var rowIdx =0; var colIdx = 0  // indexes at the new MTJBandMat
    while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
final def apply(rowL: Int, rowInc: Int, rowH: Int, allColsChar: ::.type): MTJBandMat = {
    var rowStart = rowL;     var rowEnd =  rowH;
    var colStart = 0;  var colEnd = ku-1;   // all columns
    var colNum = ku
    var colInc = 1

    if (rowEnd == -1) { rowEnd = kl-1 }  // if -1 is specified take all the rows
    
  if (rowInc > 0) { // positive increment
    var rowNum = java.lang.Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
    var colStart = 0;     var colEnd =  ku-1   // all columns
    var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart
    var rowIdx = 0; var colIdx = 0  // indexes at the new MTJBandMat
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
       }
     subMatr   // return the submatrix
     }  // positive increment
  else  {  //  negative increment
     var rowNum =java.lang.Math.floor( (rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
     var subMatr = new MTJBandMat(N, rowNum, colNum)  // create a Matrix to keep the extracted range
        // fill the created matrix with values
     var  crow = rowStart   // indexes current row at the source matrix
     var  ccol = colStart
     var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
     while (crow >= rowEnd)  {
         ccol = colStart;  colIdx = 0
         while (ccol <= colEnd)  {
             subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
  final def apply(allRowsChar: ::.type, colLow: Int, colHigh: Int): MTJBandMat = {
   var rowStart = 0;     var rowEnd =  kl-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowNum = kl    // take all the rows

    if (colEnd == -1)  { colEnd = ku-1 } // if -1 is specified take all the columns
        
    if  (colStart <= colEnd)   {    // positive increment
        var colNum = colEnd-colStart+1
        var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
             
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
    var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
  final def apply(allRowsChar: ::.type, colLow: Int, colInc: Int, colHigh: Int): MTJBandMat = {
   var rowStart = 0;     var rowEnd =  kl-1   // all rows
    var colStart = colLow;  var colEnd = colHigh
    var rowInc=1
    var rowNum = kl-1    // take all the rows

    if (colEnd == -1)  { colEnd = ku-1 } // if -1 is specified take all the columns
        
    if  (colStart <= colEnd)   {    // positive increment
        var colNum = java.lang.Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr
} // positive increment
  else {  // negative increment
    var colNum = java.lang.Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
    var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    var crow = rowStart  // indexes current row
    var ccol = colStart  // indexes current column
    var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol >= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow <= rowEnd
 subMatr   // return the submatrix
   }
   }

  final def apply(rowLow: Int,  rowHigh: Int, colLow: Int, colHigh: Int): MTJBandMat = {
      var rowInc = 1
      var colInc = 1
      apply(rowLow, rowInc, rowHigh, colLow, colInc, colHigh)
  }
  
  
// extracts a submatrix, e.g. m( 2, 3, 12, 4, 2,  8 ) corresponds to Matlab's m(2:3:12, 4:2:8)'
  final def apply(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): MTJBandMat = {
    var rowStart = rowLow;     var rowEnd =  rowHigh
    var colStart = colLow;  var colEnd = colHigh

        var rowNum = java.lang.Math.floor((rowEnd-rowStart) / rowInc).asInstanceOf[Int]+1
        var colNum = java.lang.Math.floor( (colEnd-colStart) / colInc).asInstanceOf[Int]+1
        var subMatr = new MTJBandMat(N, rowNum, colNum)   // create a Matrix to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        var crow = rowStart  // indexes current row
        var ccol = colStart  // indexes current column
        var rowIdx = 0; var colIdx = 0  // indexes at the new Matrix
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0
          while  (ccol <= colEnd)   {
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
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
                subMatr.bdm.set(rowIdx, colIdx, bdm.get(crow, ccol))
                colIdx += 1
                ccol += colInc
               }
            rowIdx += 1
            crow += rowInc
     } // crow > rowEnd
 subMatr   // return the submatrix
   }

   }
  
  
  
final def update(r: Int, c: Int, value: Double) = bdm.set(r, c, value)
  
override  final def  toString(): String = {
  
    var digitFormat = scalaExec.Interpreter.GlobalValues.fmtMatrix
    var nelems = N
    var melems = N
    var truncated = false

    var truncationIndicator = ""
    if  (MTJBandMat.mxElemsToDisplay < N) {
        nelems = MTJBandMat.mxElemsToDisplay
        truncationIndicator = " ... "
        truncated = true
      }
     if  (MTJBandMat.mxElemsToDisplay < N) {
        melems = MTJBandMat.mxElemsToDisplay
        truncationIndicator = " ... "
      }
     var s:String =" \n"
     var i=0; var j=0;
    while (i<nelems) {
        j=0
        while (j<melems) {
       s = s+digitFormat.format(bdm.get(i, j))+"  "
       j+=1
        }
      s += (truncationIndicator+"\n")
     i+=1
    }
   if (truncated)     // handle last line
    s+= "........................................................................................................................................."

     return s
  }
  
  
// perform Matrix addition with a number
final def +( that: Double) = { 
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal 
     var n = 0
     while  (n < N) {
        ret(n, n) =  this(n, n)+that
        n += 1
     }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb =1
      while (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  this(rowElem, n)+that
         lb += 1 
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
       var ub = 1
       while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  this(n, colElem)+that
         ub += 1
       }
       n += 1
    }
       ret
 
  }

  
    // unary Minus applied to a MTJBandMat implies negation of all of its elements
final def unary_- : MTJBandMat =  {
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
    var n = 0 
    while  (n< N) {
      ret(n, n) =  -this(n, n)
      n += 1
    }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb = 1
      while  (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  -this(rowElem, n)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  -this(n, colElem)
         ub += 1
       }
       n += 1
    }
       ret
 
  
}

// perform Matrix subtraction with a number
final def -( that: Double) = { 
  
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
     var n = 0  
     while  (n< N) {
        ret(n, n) =  this(n, n)-that
        n += 1
     }
       // form lower band
    var rowElem=0 
    n = 0
    while (n < N)  {
      var lb = 1
      while (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  this(rowElem, n)-that
        lb += 1 
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n < N)  {
      var ub = 1
      while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  this(n, colElem)-that
         ub += 1
       }
       n += 1
    }
       ret
 
  

}
  

// perform Matrix multiplication with a number
final def *( that: Double) = { 
  
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
    var n = 0
    while (n < N) {
        ret(n, n) =  this(n, n)*that
        n += 1
    }
    
       // form lower band
    var rowElem=0 
    n = 0
    while ( n < N)  {
      var lb = 1
      while  (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  this(rowElem, n)*that
        lb += 1 
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  this(n, colElem)*that
         ub += 1
       }
       n += 1
    }
       ret
 
}

  final def * ( that: Array[Double]): Array[Double] = {
    var vthat = new no.uib.cipr.matrix.DenseVector(that)  // construct an MTJ DenseVector
    
     //  perform y = alpha*A*x + y
     val y = new no.uib.cipr.matrix.DenseVector(Nrows)
     var res = bdm.multAdd(1.0, vthat, y).asInstanceOf[no.uib.cipr.matrix.DenseVector]
    
     res.getData
  }

final def *( that: scalaSci.Vec): Array[Double] = this *  that.getv
 
// perform Matrix addition with a Matrix
final def +( that: MTJBandMat) = { 
 
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
       var n = 0
       while  (n < N) {
            ret(n, n) =  this(n, n)+that(n, n)
            n += 1
       }
       
       // form lower band
    var rowElem=0 
    n = 0
    while  (n <  N)  {
      var lb = 1
      while  (lb <=  kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  this(rowElem, n)+that(rowElem, n)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while  (n < N)  {
      var ub = 1
      while  (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  this(n, colElem)+that(n, colElem)
         ub += 1
       }
       n += 1
    }
       ret
 
  
}

// perform Matrix subtraction with a Matrix
final def -( that: MTJBandMat) = { 
  
  var ret = new MTJBandMat(N, kl, ku)
       // form diagonal    
       var n = 0
       while  (n < N) {
            ret(n, n) =  this(n, n)-that(n, n)
            n += 1
       }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb =1
      while (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           ret(rowElem, n) =  this(rowElem, n)-that(rowElem, n)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n <  N)  {
      var ub = 1
      while (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           ret(n, colElem) =  this(n, colElem)-that(n, colElem)
       ub += 1
      }
       n += 1
    }
       ret
}
 

// postfix transpose operator
final def ~() =  {
  var  transposed = new MTJBandMat(N, this.ku, this.kl)
// form diagonal    
       var n = 0
       while  (n < N) {
          transposed(n, n) =  this(n, n)
          n += 1
       }
       // form lower band
    var rowElem=0 
    n = 0
    while  (n < N)  {
      var lb =1
      while (lb <= kl) {
         rowElem = n+lb
         if (rowElem < N)
           transposed( n, rowElem) =  this(rowElem, n)
         lb += 1
       }
       n += 1
    }
    
    // from upper band
    var colElem=0 
    n = 0
    while (n <  N)  {
      var ub = 1
      while (ub <=  ku) {
         colElem = n+ub
         if (colElem<N)
           transposed( colElem, n) =  this(n, colElem)
       ub += 1
      }
       n += 1
    }
       transposed
    
    
} 
  

  // implement those operations by converting to a general RichDoubleDArray, thus they will not be efficient
  
  final def cond() = new scalaSci.RichDouble2DArray(this.toDoubleArray).cond
  final def det()= new scalaSci.RichDouble2DArray(this.toDoubleArray).det
  final def inv() =  new scalaSci.RichDouble2DArray(this.toDoubleArray).inv
  final def pinv() = new scalaSci.RichDouble2DArray(this.toDoubleArray).pinv
  final def rank() = new scalaSci.RichDouble2DArray(this.toDoubleArray).rank
  final def rref() = new scalaSci.RichDouble2DArray(this.toDoubleArray).rref
  final def trace() = new scalaSci.RichDouble2DArray(this.toDoubleArray).trace

  
}
   
// it is used as a suitable constructor, to construct a band matrix as:
//     var bm = BandMat(5, 2, 3)
//  instead of 
//     var bm = new scalaSci.MTJ.MTJBandMat(5, 2, 3)
  object BandMat {   
final def  apply(N: Int, kl: Int, ku: Int) = new scalaSci.MTJ.MTJBandMat(N, kl, ku)  
}

  object MTJBandMat {
    
    
    var mxElemsToDisplay = 10
   
  
  }
  
