
package scalaSci

import java.util.Iterator
import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.DenseVector
import no.uib.cipr.matrix.MatrixEntry
import no.uib.cipr.matrix.sparse.CompColMatrix
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException

// Scala wrapper for MTJ sparse matrices
class CCMatrix {

  var ccm: CompColMatrix = _   // the representation of the sparse matrix
  var numRows: Int = _
  var numColumns: Int = _
  var z =  Array.ofDim[Int](1,1)
  final def length() = numRows*numColumns  // the total number of elements of the array
  final def size() = (numRows, numColumns)  // the array size as a tuple
  
  def this(_numRows: Int, _numColumns: Int, _z: Array[Array[Int]]) =
    {
    this()  
    numRows = _numRows; 
    numColumns = _numColumns;
    z = _z;
    ccm = new CompColMatrix(numRows, numColumns, _z)
   }
   
  def this(Ccm: CompColMatrix) = {
    this()
    numRows = Ccm.numRows()
    numColumns = Ccm.numColumns()
    ccm = new CompColMatrix(Ccm)
  }

def this( A: DenseMatrix) = {
   this()
   ccm = new CompColMatrix(A, true)
  }
  
def this( A: Array[Array[Double]]) = {
   this(new DenseMatrix(A))
    }

  
// perform matrix addition  
def +(that:  CCMatrix) = {
  var result = new CCMatrix(this.ccm)
  var miter = that.ccm.iterator()
  while (miter.hasNext()) {
    var current = miter.next()
    result.ccm.add(current.row(), current.column(), current.get());
    }
    result
  }
  
// perform addition with a scalar
def +(that: Double) = {
  var result = new CCMatrix(this.ccm)
  var miter = this.ccm.iterator()
  while (miter.hasNext()) {
    var current = miter.next()
    var crow = current.row(); var ccol = current.column()
    var celem = this.ccm.get(crow, ccol)
    if (celem != 0.0)
      result.ccm.set(crow, ccol, that+celem)
    }
    result
   }
    
  // perform multiplication with a scalar
def *(that: Double) = {
  var result = new CCMatrix(this.ccm)
  var miter = this.ccm.iterator()
  while (miter.hasNext()) {
    var current = miter.next()
    var crow = current.row(); var ccol = current.column()
    var celem = this.ccm.get(crow, ccol)
    if (celem != 0.0)
      result.ccm.set(crow, ccol, that*celem)
    }
    result
   }
   
    
// perform matrix subtraction
def -(that:  CCMatrix) = {
  var result = new CCMatrix(this.ccm)
  var miter = that.ccm.iterator()
  while (miter.hasNext()) {
    var current = miter.next()
    result.ccm.add(current.row(), current.column(), -current.get());
    }
    result
  }

// multiply with a ScalaSci Vector
  def *(that: scalaSci.Vec) = {
    var dv = new DenseVector(that.getv())
    this.ccm.multAdd(dv.getData())
    }
      
  // multiply with a double [] array
  def *(that: Array[Double]) = {
    this.ccm.multAdd(that)
    }
    
  def apply(row: Int, col: Int) = {
    ccm.get(row, col)
    }
    
  def  update(row: Int, col: Int, value: Double) = {
    ccm.set(row, col, value)
    }
    
    // returns the column pointers
  def getColumnPointers() = {
    ccm.getColumnPointers()
    }
    
  // get the MTJ based Column Compressed Matrix
    def getCCM() =  ccm
      
  // returns the row indices
  def getRowIndices() = {
    ccm.getRowIndices()
    }
    
  // returns the internal data storage
  def getData() = {
    ccm.getData()
    }
    
  def multAdd(alpha: Double, x: DenseVector, y: DenseVector) = {
    ccm.multAdd(alpha, x, y).asInstanceOf[DenseVector]
    }
      
  def multAdd(alpha: Double, x: Array[Double], y: Array[Double]) = {
    var dx = new DenseVector(x)
    var dy = new DenseVector(y)
    var result =   ccm.multAdd(alpha, dx, dy).asInstanceOf[DenseVector]
    result.getData()
    }
   
  def transMult(x: DenseVector, y: DenseVector) = {
    ccm.transMult(x, y).asInstanceOf[DenseVector]
    }  
  
  def transMult(x: Array[Double], y: Array[Double]) = {
    var dx = new DenseVector(x)
    var dy = new DenseVector(y)
    var result = ccm.transMult(dx, dy).asInstanceOf[DenseVector]
    result.getData()
    }
  
    
  def transMultAdd(alpha: Double, x: DenseVector, y: DenseVector) = {
    ccm.transMultAdd(alpha, x, y).asInstanceOf[DenseVector]
    }  
  
  def transMultAdd(alpha: Double, x: Array[Double], y: Array[Double]) = {
    var dx = new DenseVector(x)
    var dy = new DenseVector(y)
    var result = ccm.transMultAdd(alpha, dx, dy).asInstanceOf[DenseVector]
    result.getData()
    }
  
  override def toString() = 
    ccm.toString


/*
var a = AAD("1, 2, 0; -1, 0, -2;  -3, -5, 1")

var b = Array(3.0, -5, -4)
var am = new CCMatrix(a)

var sol = am.BiCGSolve(am, b)

     max(a*sol-b)   // should be very small
      
     * 
     */
}
      

object CCMatrix {
  /*  
   // load as a CSparse matrix
 var s = loadSparse("/home/sp/NBProjects/csparseJ/CSparseJ/matrix/t1")
   // convert to an MTJ CCMatrix
 var ccms =    CSparseToCCMatrix(s)
  */
 
  
    // convert from CSparse to CCMatrix using a DenseMatrix intermediate
    def  CSparseToCCMatrixDM(sm: Sparse) = {
        var A = sm.csm  // the CSparse compressed matrix representation
        var m = A.m  //  number of rows
        var n = A.n   //  number of columns
        var  dm = new no.uib.cipr.matrix.DenseMatrix(m, n) 
        var Ap = A.p  // column pointers (size n+1) 
        var Ai = A.i   // row indices, size nzmax
        var Ax = A.x    // numerical values, size nzmax
        var nzmax = A.nzmax
        var nz = A.nz
        var row = 0  
        var value = 0.0
        var col = 0
        while  (col <  n) {  // column col
          var p = Ap(col)
           while  (p < Ap(col + 1)) {   // indices for storiung column j
                 row = Ai(p)   // row index
                 value = Ax(p) // value        
                 dm.set(row, col, value)
                 p += 1
                    }
             col += 1
        }
     new CCMatrix(dm)
      
     }
     
  
    // convert from CSparse to CCMatrix
def  CSparseToCCMatrix(sm: Sparse) =  {
        var   A = sm.csm   // the CSparse compressed matrix representation
        var   m = A.m  //  number of rows
        var   n = A.n  //  number of columns
        var   Ap = A.p  // column pointers (size n+1) 
        var   Ai = A.i   // row indices, size nzmax
        var   Ax = A.x    // numerical values, size nzmax
        var   dataLen = Ax.length
        var   numColumnPointers = Ap.length
        var   numRowIndices = Ai.length
        var   data = new Array[Double](dataLen)
        var   columnPointer = new Array[Int](numColumnPointers)
        var   rowIndex = new Array[Int](numRowIndices)
        
        
        //  copy column pointers
        var cp = 0
        while  (cp <  numColumnPointers) {
            columnPointer(cp) = Ap(cp)
            cp += 1  
        }
        // copy row indices
        var ri = 0
        while ( ri <  numRowIndices)  {
            rowIndex(ri) = Ai(ri)
            ri += 1
        }
        // copy data
        var di = 0
        while  (di  < dataLen)  {
            data(di)  = Ax(di)
            di += 1
        }
        
        var  ccm = new CompColMatrix(m, n, columnPointer, rowIndex, data)
        var ccmatrix = new CCMatrix(ccm)
        
        ccmatrix
      
     }

//  BiCG solver. BiCG solves the unsymmetric linear system Ax = b
 // using the Preconditioned BiConjugate Gradient method.
 def BiCGSolve(A: CCMatrix, b: Array[Double]) =  {
        var template = new DenseVector(b)
        var BiCCSolver = new no.uib.cipr.matrix.sparse.BiCG(template)
        var db = new DenseVector(b)
        var x = new Array[Double](db.size())
        var  dx = new DenseVector(x)
        var result = new DenseVector(1)
        try {
            result =  BiCCSolver.solve(A.ccm, db, dx).asInstanceOf[DenseVector]
        } catch {
            case ex: IterativeSolverNotConvergedException =>
                println("Iterative BiCGSolver not converged")
                ex.printStackTrace()
                
        }
        
        result.getData()
        
    }

 // Conjugate Gradients solver. CG solves the symmetric positive definite linear
 // system <code>Ax=b</code> using the Conjugate Gradient method.
 def CGSolve(A: CCMatrix, b: Array[Double]) =  {
        var template = new DenseVector(b)
        var CGSolver = new no.uib.cipr.matrix.sparse.CG(template)
        var db = new DenseVector(b)
        var x = new Array[Double](db.size())
        var  dx = new DenseVector(x)
        var result = new DenseVector(1)
        try {
            result =  CGSolver.solve(A.ccm, db, dx).asInstanceOf[DenseVector]
        } catch {
            case ex: IterativeSolverNotConvergedException =>
                println("Iterative CGSolver not converged")
                ex.printStackTrace()

        }

        result.getData()

    }

}
  
    
  

