
//  The D2Das1D type serves for implicitly converted to that type when
//   certain operations cannot be performed 
//  Also, it provides an extensive set of operations.

package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray
import java.util._
import Jama._

// for the fast JBLAS based routines
import org.jblas.DoubleMatrix._ 
import org.jblas.DoubleMatrix
import org.jblas.ComplexDoubleMatrix


import scalaSci.jcublas._
import scalaSci.jcublas.FloatMatrix._
import jcuda.jcufft._

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

// a matrix with data representation as an one-dimensional Double array
  // row-major storage is used
class D2Das1DMat( var Nrows: Int, var Ncols: Int) extends AnyRef with scalaSci.scalaSciMatrix[scalaSci.D2Das1DMat]  {
  
  import scalaSci.RichDouble2DArray._
  
  final var  data = new Array[Double](Nrows*Ncols)
  
  final def numRows() = Nrows  
  final def numColumns() = Ncols
  final def length() = Nrows*Ncols  // the total number of elements of the array
  final def size() = (Nrows, Ncols)  // the array size as a tuple
    
  final def  getv() = data    // return the data array
   
  final def getLibraryMatrixRef() =   data
  
 
// we can change the native representation with whatever operation and with this call we can take an updated object   
  final def matFromLibrary() = new D2Das1DMat(Nrows, Ncols)
    
def this(da: Array[Double], Nrows: Int, Ncols: Int) = {
    this(Nrows, Ncols)
    data = da
  }
  
def this( da: Array[Array[Double]]) = {
  this(da.length, da(0).length)
  var r=0; var c=0; var currentElem=0
  while (r < da.length) {
    c = 0
    while (c < da(0).length) {  // row-major storage
      data(currentElem) = da(r)(c)
      currentElem+=1
      c += 1
    }
     r += 1
  }
  
}
    // indexes the corresponding Matrix element without updating automatically sizes for fast access
@inline
final def apply(row: Int, col: Int): Double = {
       data(row*Ncols+col)
}
  
  @inline
final def update(row: Int, col: Int, value: Double)  {
  data(row*Ncols+col) = value
}
  
  
 final def cond() = scalaSci.math.LinearAlgebra.LinearAlgebra.cond(this.toDoubleArray)
 
 final def rank() =  scalaSci.math.LinearAlgebra.LinearAlgebra.rank(this.toDoubleArray)
 
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
  // compute the eigenvalues/eigenvectors using JBLAS

  
  // clone the matrix
  override final def clone() = {
     var nArry =  new D2Das1DMat(Nrows, Ncols)

      var elem = 0
    while  (elem <  Nrows*Ncols)  {
        nArry.data(elem) = data(elem)
        elem+=1
      }
      nArry
    }
  
  
  final def +(that: scalaSci.D2Das1DMat): scalaSci.D2Das1DMat = 
  {
    val   len = Nrows*Ncols
    
    val  vr = new D2Das1DMat(Nrows, Ncols)
  
    var k=0
    while (k<len) {
      vr.data(k) = this.data(k)+that.data(k)
      k+=1
    }
    
    vr
  }
  
  final def copy() = {  // same as clone()
    clone()
  }

  
  // copy to a new matrix, perhaps resizing also matrix
  final def copy(newNrows: Int, newNcols: Int)  =  {
    var cpMat = new D2Das1DMat(newNrows, newNcols)   // create a new Matrix 
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
  
  
  
  
 final def  det() =   scalaSci.math.LinearAlgebra.LinearAlgebra.det(this.toDoubleArray)
 
 final def trace() = scalaSci.math.LinearAlgebra.LinearAlgebra.trace(this.toDoubleArray)

 final def inv() = new D2Das1DMat(scalaSci.math.LinearAlgebra.LinearAlgebra.inverse(this.toDoubleArray)) 
 
  
  final def pinv() =  {
    val ejmlM = new scalaSci.EJML.Mat(this.getv)
    val pejml = ejmlM.pinv
    val nrows = pejml.Nrows
    val ncols = pejml.Ncols
    var pM = new D2Das1DMat(nrows, ncols)
    var n=0; var m=0
    while (n < nrows)  {
      m=0
      while (m < ncols) {
        pM(n, m) = pejml(n, m)
        m += 1
      }
      n += 1
    }
    pM
  }
  
  
  // Reduced-Row Echelon form
  final def rref() = {
    var xd = this.toDoubleArray
    var exd  = new org.ejml.data.DenseMatrix64F(xd)
    
    var reduced = org.ejml.ops.CommonOps.rref(exd, -1, null)
    new D2Das1DMat(scalaSci.EJML.StaticMathsEJML.DenseMatrixToDoubleArray(reduced))
    
  }
 
    // fast multiply using native BLAS
def  blasmj(b : Array[Double],  Ccols: Int)  = {
  
   var   nthreads = ConcurrencyUtils.getNumberOfThreads
   nthreads = Math.min(nthreads,  Nrows)
   var futures = new Array[Future[_]](nthreads)
   var rowsPerThread = (Nrows / nthreads).toInt  // how many rows the thread processes
  
    var R = new D2Das1DMat(Nrows, Ccols)   // the result matrix
  //   println("creating matrix R, dim ="+Nrows+"x"+Ccols)
     
    var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) Nrows else firstRow+rowsPerThread
    var numRowsForThread = lastRow-firstRow   // number of rows that the current thread will process
 // println("num of rows for thread "+threadId+ " = "+numRowsForThread+" firstRow  = "+firstRow+", lastRow = "+lastRow)
 
       
 futures(threadId) = GlobalValues.execService.submit(new Runnable() {
    def run = {
      
               // prepare workspace on which the thread will work
      var al = new Array[Double](numRowsForThread*Ncols)  // the portion of the first matrix that the thread will process
      var bl = new Array[Double](Ncols*Ccols)
      var cl = new Array[Double](numRowsForThread*Ccols)  // the portion of the result that the thread will produce
      
            //  copy the part of the first matrix to a linear 1-D a[] array
      var cnt = 0;  var col = 0
       while (col < Ncols) {
       var row = firstRow
       while (row < lastRow) {   al(cnt) = data(row*Ncols+col);  row += 1; cnt += 1; }
           col += 1
           }
            // copy the B matrix to a linear 1-D array
         cnt = 0; col = 0;
         while (col < Ccols) {
           var row = 0
           while (row < Ncols) {  bl(cnt) = b(row*Ccols+col);    row += 1;  cnt += 1;  }
        col += 1
        }
        
             org.jblas.NativeBlas.dgemm('N', 'N', numRowsForThread,  Ccols,  Ncols, 
                              1.0,  al,  0, 
                              numRowsForThread,  bl, 0,  Ncols,
                              0.0, cl, 0, numRowsForThread); 
   
         
  cnt = 0;   col = 0
  while (col < Ccols) {
    var row = firstRow
    while (row < lastRow) {  R.data(row*Ccols+col) = cl(cnt); row += 1; cnt += 1        }
        col += 1
        }  
        } // run
        threadId += 1
      })  // Runnable
    //  all threads
 }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)
  
    R
 }

  
  
  // fast multiply using native BLAS
def  jjs(b: Array[Double], Arows: Int, Acols: Int, Ccols: Int)  = {
   
               // prepare workspace on which the thread will work
      //var a = new Array[Double](numRowsFo  
      //var b = new Array[Double](Acols*Ccols)
      var c = new D2Das1DMat(Nrows, Ncols)
      
                      
            //NativeBlas.dgemm('N', 'N', c.rows, c.columns, a.columns, 
              //               alpha, a.data, 0,
		//		a.rows, b.data, 0, b.rows, 
                                //beta, c.data, 0, c.rows);
		
   org.jblas.NativeBlas.dgemm('N', 'N', Nrows,  Ccols,  Acols, 
                              1.0,  data,  0, 
                              Nrows,  b, 0,  Acols,
                              0.0, c.data, 0, Nrows); 

  
   c
  }

  def * (that: D2Das1DMat) = {
    var dm1 = new org.jblas.DoubleMatrix(Nrows, Ncols, data: _*)
    var dm2 = new org.jblas.DoubleMatrix(that.Nrows, that.Ncols, that.data: _*)
    new D2Das1DMat((dm1.mmul(dm2)).data, Nrows, that.Ncols)
  
  }

	
	
  
}





  



  
