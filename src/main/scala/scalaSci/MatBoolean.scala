

package scalaSci

import java.util._
import Jama._

//import collection.immutable.Vector


// this class provides Matrix operations, using zero-indexed values
class MatBoolean( n: Int, m: Int)   extends AnyRef
{ 
// getters for size
  var  Nrows =  n // keeps  the number of rows of the Matrix
  var  Ncols =  m   //  keeps  the number of columns of the Matrix
     // the default constructor allocates a double array of size n x m
     // therefore, the representation of the data is a two-dimensional Java double array
  var  v = Array.ofDim[Boolean](Nrows, Ncols)  // i.e. v(0:n-1, 0:m-1)

  final def numRows() = Nrows
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols
  final def size() = (Nrows, Ncols)
  
  // getv() returns the data representation 
  final def getv() = {
    v
}

  // the scalaSci.Mat does not wrap a Matrix class of a specific library, thus return simply the data representation  
final def getLibraryMatrixRef() =   v // the scalaSci.Mat does not wrap a Matrix class of a specific library, thus return simply the data representation
final def matFromLibrary() = new MatBoolean(v)
  
final def  setv(values: Array[Array[Boolean]], n: Int, m: Int) = { v = values; Nrows = n; Ncols = m }

import Mat._
  
final def  set(row: Int, column: Int, value: Boolean) =   {  v(row)(column) = value }

final def  get(row: Int, column: Int) = v(row)(column)

final def this(tuple: (Int, Int)) = this(tuple._1,  tuple._2) 

  // TODOStergJune07: test this
final def this(m: Matrix) = {
        this(m.Nrows, m.Ncols)
    }

// indexes the corresponding Matrix element without updating automatically sizes for fast access
  final def apply(n: Int, m: Int): Boolean = {
        v(n)(m)
 }

  final def apply(n: Int) = {
      var nr = n/Ncols
      var nc = n - nr*Nrows
      v(nr)(nc)
    }
  
  
  
// construct a Matrix from a zero-indexed double [][] array
final def this(vals: Array[Array[Boolean]]) = {
     this(vals.length, vals(0).length)  // allocate memory with the final default constructor
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


final def this(Nrows: Int, Ncols: Int, df: Boolean) = {   // initializes to a default value
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

    // construct a Matrix from an one-indexed double [][] array
final def this(vals: Array[Array[Boolean]], flag: Boolean) = {
    this(1,1)
    v = vals
    Nrows = vals.length
    Ncols = vals(0).length
 }

  // construct a Matrix from a one-indexed double [] array
final def this( vals: Array[Boolean]) = {
    this(1, vals.length)   // keep the array as a row of the Matrix
    var i=0
    while (i<vals.length)  {
       v(0)(i) = vals(i)
       i+=1
   }
  }
  
  /* e.g.
var v = rand0(3,3)
var vc = v.clone
*/
  override final def clone() = {
    var a = new MatBoolean(this)
    a
  }

  /* e.g.
var v = rand0(3,3)
var vc = v.copy
*/
  final def copy() = {  // same as clone()
    clone()
  }

  // copy to a new matrix, perhaps resizing also matrix
  /* e.g.
var v = rand0(3,3)
var vc = v.copy(4, 6)
*/

  final def copy(newNrows: Int, newNcols: Int)  =  {
    var cpMat = new MatBoolean(newNrows, newNcols)   // create a new Matrix 
    val mnNrows = if (newNrows < Nrows)  newNrows else Nrows
    val mnNcols = if (newNcols < Ncols)   newNcols else Ncols
      // copy the original matrix within
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
  
    // clone a Matrix from another Matrix
final def this(a: MatBoolean) = {
     this(a.Nrows, a.Ncols)  // allocate memory with the  default constructor
    var i = 0; var j = 0
    while (i<a.Nrows) {
        j = 0
        while (j<a.Ncols) {
       v(i)(j) = a.v(i)(j)
       j += 1
        }
        i += 1
     }
 }
    
    // updating a single element of the Matrix
final def  update(n: Int, m: Int, value: Boolean) {
         v(n)(m) = value;
   }

  
    
 
}

// Mat's companion object
  object MatBoolean  {

 var digitsPrecision = 4  // controls pecision in toString()  
 var mxRowsToDisplay = 6  
 var mxColsToDisplay = 6
 
  final def setDigitsPrecision(n: Int) = {digitsPrecision = n}
  final def setRowsToDisplay(nrows: Int) = {mxRowsToDisplay = nrows }
  final def setColsToDisplay(ncols: Int) = {mxColsToDisplay = ncols }
  
  
 
  // a conveniency constructor that allows to construct a matrix e.g. as
  //   var x = Mat(3,7) instead of  var x = new Mat(3, 7)
 final def apply(nrows: Int, ncols: Int) = new Mat(nrows, ncols) 
  
  }