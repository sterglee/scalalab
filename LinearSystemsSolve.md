# Introduction #

`The ` _`solve()`_ `is the basic routine for solving linear systems A * x = b. That routine has many overloads that we list below.`

```

def solve(A: scalaSci.Mat,  b: scalaSci.Mat) = {
    scalaSci.Mat.solve(A, b)
}



  def solve(A: scalaSci.EJML.Mat, b: scalaSci.EJML.Mat) = 
   A.solve(b)
  
  // solve with the CSparse
def solve(A: scalaSci.Sparse,  b: Array[Double]) = {
    scalaSci.Sparse.sparseSolve(A, b)
}

// solve with the MTJ Column Compressed Matrix
def solve(A: scalaSci.CCMatrix, b: Array[Double]) = {
    scalaSci.CCMatrix.BiCGSolve(A, b)
}

  def solve(A: Array[Array[Double]],  b: Array[Array[Double]]) = {
   LinearAlgebra.solve( A, b)
}

  def solve(A: scalaSci.CommonMaths.Mat, b: scalaSci.CommonMaths.Mat): scalaSci.CommonMaths.Mat = 
   new scalaSci.CommonMaths.Mat(solve(A.v, b.v) )
 
  
 def solve(A: scalaSci.JBLAS.Mat, B: scalaSci.JBLAS.Mat) =  {
   new scalaSci.JBLAS.Mat(org.jblas.Solve.solve(A.dm, B.dm))
 }

def solve(A: RichDoubleDoubleArray,  b: RichDoubleDoubleArray) = {
   new RichDoubleDoubleArray(LinearAlgebra.solve( A.getv, b.getv))
}

  def solve(A: RichDoubleDoubleArray,  b: RichDoubleArray): RichDoubleArray = {
    var N = b.length
    var bb = new RichDoubleDoubleArray(N,1)
    for (k<-0 until N)
      bb(k, 0) = b(k)
    var sol = solve(A, bb)
    // convert the solution to a RichDoubleArray
    var x = new Array[Double](sol.numRows)
    for (k<-0 until sol.numRows)
      x(k) = sol(k, 0)
    new RichDoubleArray(x)
    
}

  def solve(A:scalaSci.MTJ.Mat, B: scalaSci.MTJ.Mat, X: scalaSci.MTJ.Mat) = 
      A.solve(B, X)
  
```



`There are also other routines for solving linear systems (of course one can use the native Java interfaces of the ScalaLab libraries). We demonstrate some solving routines in the following examples. `


## Examples ##
```


val tol = 0.000001  // tolerance  on error


// a two-dimensional double array, a RichDouble2DArray type is created
var  A = $$(2.11, -4.21, 0.921, null,
           4.01, 10.2, -1.12, null,
           1.09, 0.987, 0.832)

// a 1-d double array
var  b = Array(2.01, -3.09, 4.21)

// solve with LAPACK
var x = la_LUSolve(A, b)
var OK_la_LU = if  (max(A*x-b) < tol) true else false


// JAMA based solver requires  b as an Array[Array[Double]] with the right-hand sides of the equations as columns
var  bb = $$(2.01,null, -3.09,null, 4.21)
var xjama = jama_LUSolve(A, bb)

var OK_la_JAMA = if  (max(max(A*xjama-bb)) < tol) true else false

// standard solve()  method
var xsolve  = solve(A,b)
var OK_solve = if (max(A*xsolve-b)<tol) true else false

```

## `Demonstration of exact, overdetermined and underdetermined cases` ##

`The following example demonstrates both the N equations by N unknowns case and the overdetermined and underdetermined ones.`

```
//  define N equations by N uknowns system
var A = $$(3.5, 5.6, -2, null,
        4.5, 6.7, 9.4, null,
        2.3, -2.3, 9.4)

var b = $$(-0.2, null, 0.45, null, -3.4)

var x= solve(A, b)   // solve the linear  system
A*x-b   // verify that is zero

var x2 = A \ b    // we can solve it with the operator '\'
A*x2- b  // verify that is zero

// test DGELS
var lssol = DGELS(A, b)  // solve with the LAPACK routine
A*lssol-b  // verify that is zero


// define an over determined system, i.e. number of equations > number of uknowns
var ARowsMoreThanCols = $$(3.5, 5.6, -2, null, 4.5, 6.7, 9.4, null,
   2.3, -2.3, 9.4, null, 8.1, 2.3, -0.2)
var bRowsMoreThanCols = $$(-0.2,  null, 0.45, null, -3.4, null, -0.3)

// solve overdetermined system with the LAPACK routine
var lssolOverDetermined = DGELS(ARowsMoreThanCols, bRowsMoreThanCols)




// define an under determined system, i.e. number of equations < number of unknows 
 var AColsMoreThanRows= $$(13.5, 5.6,  9.3, -2, null,
            3.5, 3.7, 9.4, -0.7, null,  2.3, -2.3, 0.9, 9.4)
var bColsMoreThanRows= $$(-0.4, null, 0.5, null,  -3.4)

// solve with the LAPACK routine
var lssolUnderDetermined = DGELS(AColsMoreThanRows, bColsMoreThanRows)

AColsMoreThanRows*lssolUnderDetermined

 
```


## `Example for the Matlab-like backslash operator` ##

`The following example uses the backslash Matlab-like operator to demonstrate solving linear systems using various libraries `


```

// testing \ (backslash) operator for solving linear systems
// testing \ (backslash) operator for solving linear systems

// RichDoubleDoubleArray
var ARDDA = $$(3.4, 4.5, null, -1.2, 5.6)
var bRDDA = $$(4.3, null,  9.2)
var xRDDA = ARDDA \  bRDDA
var shouldBeZeroRDDA = ARDDA*xRDDA - bRDDA

// JAMA Matrix
var AJamaMat = scalaSci.StaticMaths.M0("3.4 4.5; -1.2 5.6")
var bJamaMat = scalaSci.StaticMaths.M0("4.3; 9.2")
var xJamaMat = AJamaMat \ bJamaMat
var shouldBeZeroJamaMat = AJamaMat*xJamaMat-bJamaMat

// EJML Matrix
var AEJMLMat = scalaSci.EJML.StaticMathsEJML.M0("3.4 4.5; -1.2 5.6")
var bEJMLMat = scalaSci.EJML.StaticMathsEJML.M0("4.3; 9.2")
var xEJMLMat = AEJMLMat \ bEJMLMat
var shouldBeZeroEJMLMat = AEJMLMat*xEJMLMat-bEJMLMat


// MTJ Matrix
var AMTJMat = scalaSci.MTJ.StaticMathsMTJ.M0("3.4 4.5; -1.2 5.6")
var bMTJMat = scalaSci.MTJ.StaticMathsMTJ.M0("4.3 ; 9.2")
var xMTJMat = AMTJMat \ bMTJMat
var shouldBeZeroMTJMat = AMTJMat*xMTJMat-bMTJMat

// Apache Commons  Matrix
var AApacheCommonsMat = scalaSci.CommonMaths.StaticMathsCommonMaths.M0("3.4 4.5; -1.2 5.6")
var bApacheCommonsMat = scalaSci.CommonMaths.StaticMathsCommonMaths.M0("4.3 ; 9.2")
var xCommonMathsMat = AApacheCommonsMat \ bApacheCommonsMat
var shouldBeZeroApacheCommonsMat = AApacheCommonsMat*xCommonMathsMat-bApacheCommonsMat



// JBLAS  Matrix
var AJBLASMat = scalaSci.JBLAS.StaticMathsJBLAS.M0("3.4 4.5; -1.2 5.6")
var cpAJBLASMat = new scalaSci.JBLAS.Mat(AJBLASMat.getv)  // copy since Native BLAS overwrittes original
var bJBLASMat = scalaSci.JBLAS.StaticMathsJBLAS.M0("4.3 ; 9.2")
var cpbJBLASMat = new scalaSci.JBLAS.Mat(bJBLASMat.getv)  // copy since Native BLAS overwrittes original
var xJBLASMat = AJBLASMat \ bJBLASMat
var shouldBeZeroJBLASMat = cpAJBLASMat*xJBLASMat   -cpbJBLASMat


// sparse solve
var Asparse = SparseFromDoubleArray(ARDDA.getv)
var b = Array(4.3, 9.2)
var sol = Asparse  \ b  // solve the sparse system

```