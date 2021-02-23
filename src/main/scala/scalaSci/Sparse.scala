
package scalaSci

import edu.emory.mathcs.csparsej.tdouble._
import edu.emory.mathcs.csparsej.tdouble.Dcs_common.Dcs

class Sparse {

  import Sparse._
  
  // the sparse matrix representation in triplet form
   var   tsm   =  Dcs_util.cs_spalloc(1, 1, 1, true, true)
   var   csm: Dcs  = _     // the sparse matrix representation in compressed form
     
   var Nrows: Int = _
   var Ncols: Int = _
   
    final def length() = Nrows*Ncols
    final def size() = length()
  
  final def numRows() = Nrows
  final def numColumns() = Ncols
     /*
   var s =    loadSparse("L:\\NBProjects\\CSPARSEJ\\CSparseJ\\matrix\\t1")
   var s = loadSparse("/home/sp/NBProjects/csparseJ/CSparseJ/matrix/t1")
   
      */
     
     // convert to triplet representation
     final def   toTriplet(_csm: Dcs): Dcs = {
          Dcs_CSToTriplet.cs_CSToTriplet(_csm)
     }
     
  

     
final def  +( that: Sparse) =   {
       val   result = new Sparse()
       val   sm  =  Dcs_add.cs_add(this.csm, that.csm, 1.0, 1.0)
       
       result.csm = sm
       result
     }
     
     
     final def update( i: Int,  j: Int, x: Double): Unit = {
         // convert the representation to triplet format
         var   triplet = toTriplet(csm)
          // add the new entry
         var success = Dcs_entry.cs_entry(triplet, i, j, x)
         if (success == false) {
             println("Failing to insert element ["+i+","+"j"+", "+x+" ] ")
             return
         }
         // convert back to compressed matrix format
         csm = Dcs_compress.cs_compress(triplet)
     }
     
     
   final def  apply(i: Int, j: Int) =  {
         var columnStartPos = csm.p(j)   // get column start
         var columnEndPos = csm.p(j+1)   // get column end
         // scan to find the row
         
         var rowFoundAtIndex = -1
         var rowIndex = columnStartPos
            // for all the elements of the row
         while (rowIndex < columnEndPos && rowFoundAtIndex == -1)  {
             if (csm.i(rowIndex) == i)    // we have found the corresponding row
                  rowFoundAtIndex = rowIndex   // keep the index of the element
           rowIndex += 1
          }
         if (rowFoundAtIndex > 0)
             csm.x(rowFoundAtIndex)
         else
             -1.0
     }
       
         
  final def  print() =  {
         var  brief = false
         Dcs_print.cs_print(csm,  brief)
       }
     
   final def   printCS(brief: Boolean) = {
         Dcs_print.cs_print(csm, brief)
     }
     
     
     final def  printTriplet(brief: Boolean) = {
         Dcs_print.cs_print(tsm, brief)
     }
  
  final def solve(b: Sparse) = {
    var bdd = SparseToDoubleArray(b)
    var solution = Array.ofDim[Double](Nrows, Ncols)  // the solution array
    var currentColumn = new Array[Double](Nrows)
    var col = 0
    while  (col <  Ncols)  {
      // extract a column from b
      var row = 0
      while  (row < Nrows) {
        currentColumn(row) = b(row, col)
        row += 1
      }
     var currentSolution = sparseSolve(this, currentColumn)
     // copy solution
     row = 0
     while  (row < Nrows)  {
       solution(row)(col) = currentSolution(row)
       row += 1
     }
     
      col += 1
    }
    solution
  }
  
  final def solve(b: Array[Double]) = 
    sparseSolve(this, b)
  
  final def \(b: Array[Double]) = solve(b)
  final def  \(b: Sparse) = solve(b)
  
   }
    

object Sparse {
  
    final def SparseToDoubleArray(sm: Sparse) = {
         Dcs_toDouble.cs_toDouble(sm.csm)
     }
         
     final def  SparseFromDoubleArray(a: Array[Array[Double]]) =  {
         var  dcsa = Dcs_fromDoubleArray.cs_fromDoubleArray(a)
         var  sm = new Sparse()
         sm.csm = dcsa
         sm.Nrows = dcsa.m
         sm.Ncols = dcsa.n
         sm
     }
     
     
     final def  SparseFromDoubleArray(a: Array[Array[Double]], keepDimension: Boolean) =  {
         var  dcsa = Dcs_fromDoubleArray.cs_fromDoubleArray(a)
         var  sm = new Sparse()
         sm.csm = dcsa
         dcsa.m = a.length
         dcsa.n = a(0).length
         sm.Nrows = dcsa.m
         sm.Ncols = dcsa.n
         sm
     }
    
  // convert to triplet representation
      final def   toTriplet(sm: Sparse): Dcs = {
         Dcs_CSToTriplet.cs_CSToTriplet(sm.csm)
       }
       
   final def scanPositives( pi: Array[Int]) = {
       var k = 1
       var N = pi.length-1
       while (pi(k) > 0 && k<N)  {
          k += 1
       }
    k
     }
     
     final def  max (x: Array[Int]) = {
         var mx = x(0)
         for (k <-1 until x.length)
             if (x(k)>mx) mx = x(k)
         mx
     }

  final def  loadSparse(filename: String) =  {
        var   dcs  =  Dcs_load.cs_load(filename)
        var   loadedSparse = new Sparse()
        loadedSparse.tsm = dcs
        loadedSparse.csm = Dcs_compress.cs_compress(dcs)
        
        loadedSparse.Ncols = scanPositives(loadedSparse.csm.p)  // no of columns
        loadedSparse.Nrows = max(dcs.i)+1  // no of rows
        loadedSparse
      }
 
       // implement the primary CSparse routines
     
     // adds two sparse matrices, C = alpha*A+beta*B
     final def  cs_add(A: Sparse, B: Sparse, alpha: Double, beta: Double) = {
         var result = new Sparse();
         result.csm = Dcs_add.cs_add(A.csm, B.csm, alpha, beta)
         result.Nrows = A.Nrows
         result.Ncols = A.Ncols
         result
      }
     
     // solve Ax = b using Cholesky factorization
     // int cs_cholsol( int order, Sparse A, double [] b)
     //    order:  in, ordering method to use (0 or 1)
     //    A:  in, sparse matrix; only upper triangular part used
     //    b:  in/out,  size n, b on input, x on output
    //          returns   true  if successful; false  on error
     final def  cs_cholsol(order: Int,  B: Sparse, b: Array[Double]) = {
        var  csm = B.csm
        var  success = Dcs_cholsol.cs_cholsol(order, csm, b)
        success
      }
         
     // remove duplicate entries
     // Removes and sums duplicate entries in a sparse matrix
     //  A:   in/out,    sparse matrix; duplicates summed on output
     //        returns:   true  if successful; false on error
     final def  cs_dupl( A: Sparse) =  {
        Dcs_dupl.cs_dupl(A.csm)
     }

     // sparse matrix times dense column vector, y = Ax+y
     //    A:   in,   sparse matrix
     //    x:    in,   size n
     //    y:    in/out, size m
     //          returns true if successful, false on error
     final def  cs_gaxpy(A: Sparse,  x: Array[Double], y: Array[Double]) = {
         Dcs_gaxpy.cs_gaxpy(A.csm, x,  y)
     }
     
     // solve Ax=b using LU factorization
     //  solves Ax = b, where A is square and nonsingular
     //    order:  in,   ordering method to use (0 to 3),
     // 0 results in natural orderin, 1 is a minimum degree ordering of A + A^T
     // 2 is a minimum degree ordering of S^T \cdot S where S = A, except rows 
     // with more than 10\sqrt(n) entries are removed
     // and 3 is a minimum degree ordering of A^T \cdot A
     //    A:        in,    sparse matrix
     //    b:         in/out,  size n; b on input, x on output
     //   tol:        in, partial pivoting tolerance
     //               returns,  true if successful, false on error
     final def  cs_lusol(order: Int, A: Sparse, b: Array[Double], tol: Double) =  {
          Dcs_lusol.cs_lusol(order, A.csm, b, tol)
   }

     // a simplified interface
     final def  sparseSolve(A: Sparse,  b: Array[Double]) =  {
         var  bc = new Array[Double](b.length)
         var k =  0
         while  (k <  b.length) {
             bc(k) = b(k)
             k += 1
         }
         var order = 0
         var tol = 0.00001
         var success = cs_lusol(order, A, bc, tol)
         if (success)  bc
         else   b
     }

  
     // matrix 1-norm
    final def   cs_norm(A: Sparse) =  {
        Dcs_norm.cs_norm(A.csm)
    }
    
    // solve a least squares or underdetermined problem
    // Solves a least squares problem (min||Ax-b||_{2}, where A is m-by-n with m>=n),
    // or an underdetermined system (Ax=b, where m<n)
    //   order: in,  ordering method to use (0 or 3)
    //   A:      in,   sparse matrix
    //   b:       in/out,  size max(m, n); b(size m) on input, x(size n) on output
    //             returns,  true if successful; false on error
    final def  cs_qrsol(order: Int, A: Sparse, b: Array[Double]) = {
       Dcs_qrsol.cs_qrsol(order, A.csm, b)
     }

    }

