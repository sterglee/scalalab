# Introduction #

_`EJML`_ ` is an efficient Java linear algebra library (http://code.google.com/p/efficient-java-matrix-library/). `

_`ScalaLab`_ `utilizes by default for zero-indexed matrices the EJML  library upon which builds a Matlab-like easy to use way of performing matrix operations. It is important that the lower level routines of EJML are all available, and we can call them, either when ScalaLab has not a higher-level equivalent or when we require maximum execution speed. Here we describe some aspects of working with EJML library presenting many examples.`


# `A high-level interface to the EJML library` #

`The EJMLMat class implements zero-indexed two-dimensional dense matrices in ScalaSci that are based upon the Efficient Java Matrix Library.`

## `Constructors` ##

`Mat(rows: Integer, cols: Integer) // Creates a EJML Mat of size rows, cols initialized to zeros`

```
var m=new Mat(2, 3)
m.print // print the contents
```

`Construction by specifying the number of rows and columns and then the elements, e.g.`
```
var m1 = Mat(2, 3,   //  number of rows and  columns
4.5, 5.6, -4.0,  // first row
 4.5, 3.0, -3.4   // second row
)
```
`or by specifying directly the elements, where` _`null`_ `seperates the rows `
```
var m2 = $(4.5, 5.6, -4, null, 4.5,  3,  -3.4) 
```

`Construct Mat by copying the array values,` _`Mat(da: Array[Array[Double]])`_ , `creates an EJML Mat initialized with the da array`
```
var dd = Array.ofDim[Double](2,4)
dd(1)(1)=11
var mdd = new Mat(dd)
mdd.print // print finally the EJML Matrix
```

## `Retrieve lower-level EJML data structures` ##

`This is particularly useful when we have to work directly with EJML routines. Therefore, we can utilize` **`all`** `the EJML functionality, using the Java routines of the EJML library from ScalaLab. `

`We can retrieve the` _`SimpleMatrix`_ `on which ScalaSci's Mat is based with: `
```
var rnd = rand0(5,6)  // a random EJML based matrix
var smat = rnd.sm
```

`And also, the` _`DenseMatrix64F`_`on which` _`SimpleMatrix`_ `is based: `
```
var denseM = smat.getMatrix
```

## `Access operations` ##

`Access operations are implemented conveniently with a Matlab-like style, e.g. `

```
var a = rand0(10, 20)
var a1 = a(1) // get the 1th row as a matrix
var a1_3 = a(1::3, ::) // get rows 1 until 3 all columns
var ac2_5 = a(::, 2::5) // get columns 2 until 5 all rows
var ac2_3_1_5 = a(2::3, 1::5) // get rows 2 until 3, columns 1 until 5 
```



## `Operators` ##

`Matrix addition, subtraction and multiplication has been overloaded and works properly. Also addition and multiplication with scalars operates properly, e.g.`

```
var m1 = rand0(4,7)
var m2 = rand0(7, 9)
var mmul = m1*m2 // multiplication
var madd = m1+7*m1+m1*9.4
org.ejml.ops.MatrixVisualization.show(madd.sm.getMatrix, "madd matrix")
```

## `Basic Methods` ##

`def getv: Array[Array[Double]] // returns the Java 1-D double array used to store the contents of the matrix`

`def size: (Int, Int)  // Returns the number of rows and columns of the Mat`
```
var md = ones0(9)
md.size
```

`def length: Int  // Returns the number of elements of Mat `
```
var md = ones0(9, 2)
md.length
```


`def Nrows: Int   // Returns the number of rows of Mat`

`def Ncols: Int   // Returns the number of columns of Mat`
```
md.Nrows
md.Ncols
```


# Routines #


`def ones0(n: Int): Mat - constructs a nXn Mat of ones`

`def ones0(n: Int, m: Int): Mat - constructs a nXm Mat of ones`

`def zeros0(n: Int): Mat - constructs a nXn Mat of zeros`

`def zeros0(n: Int, m: Int): Mat - constructs a nXm Mat of zeros`

`def rand0(n: Int): Mat - constructs a nXn Mat of random values`

`def rand0(n: Int, m: Int): Mat - constructs a nXm Mat of random values`


**`Transpose`** `Matrix, operator ~, or method transpose`

```
var mm = ones0(9,4)  // create an EJML matrix of 1s
mm(2,3) = 23  // update one element
var mmt = mm~   // transpose the matrix
var mmt2 = mm.transpose  // also transposes
mm.print
mmt.print
```


**`Inverse`** `Matrix, inv()`
```
var mm= rand0(8)
var mmi = inv(mm) // get the inverse
var oned = mm*mmi // verify that indeed we get the inverse
oned.print
```

**`Solve`** `Linear System, solve() `



```
var A = $(3, -1, 1, null, -1.0, 3, 1, null, 1, 1, 3)
var b = $(3, null, -5, null, -4)
var x = A.solve(b) // solve the system
var bres = A*x // estimate the quality of the solution 
var diff = b-bres
diff.print // we should have zeros
```


**`Map`** ` a function to each element of the matrix`

`Let now transform all the elements of a matrix with the function f(x) = 2*x+10`
```
def f(x: Double) = 2*x+10
var m = ones0(5,10) // a simple test matrix consisting of all ones
var mTrans = m map f // the transformed matrix
mTrans.print
```

`Create a ` **`diagonal`** ` matrix (diag0)`
```
var diagElems = Array(0.5, 2.3, -3.4, 2.3) // specify with an array of the diagonal elements
var mm = diag0(diagElems) // create the matrix
mm.print
```

**`Trigonometric`** ` functions`

`All the basic trigonometric functions, e.g. sin, cos, tan, asin, acos, atan, cosh, sinh, tanh etc. are applicable to matrices, e.g. `

```
val  aa = rand0(5,8)
var aas = sin(0.34*aa)+tan(aa*8.9)-3.4*tanh(0.23*aa)
aa.print
aas.print
```


**`Norm`** ` Operations`

```
var zeroMatrix = new Mat(3,4)
var unzeroMatrix = $(0.2,  1, null, -2, 3, null,  6, 5) // create the 2X3 matrix
var unzeroVector = $(0.3, null,  1, null,  -2, null,  3, null, 4)
var squareMatrix = $(0.2, 1, null, -2, 3)

var vs = conditionP(squareMatrix, 1) // should be 7.69
var vsq = conditionP(unzeroMatrix, 1) // should be 3.43

var vt = unzeroMatrix~ // transpose matrix
var vtt = conditionP(vt,1) // should be 3.48
```


**`Covariance`** ` operations`
```
// isValid returns: 0 = is valid, 1 = failed positive diagonal, 
// 2 = failed on symmetry, 3 = failed on positive definite
var m = identity0(3)
m.print
// nothing is wrong with it, is a valid covariance matrix, return 0
isValid(m) // 0
// negative diagonal term
m(1,1) = -3
isValid(m) // 1
// not symmetric
m = identity0(3)
m(1, 0)=30
isValid(m) // 2
// not positive definite
m = identity0(3)
m(1, 2)= -400; m(2,1) = -400
isValid(m) // 3

```


# Block Matrices #

`EJML implements algorithms that are defined in terms of matrix blocks in order to better exploit the caches of modern processors. The class` _`BMat`_ `in ScalaLab wraps operations in those matrices`

## Constructors ##

`We can construct directly an NXM block matrix ` _`BMat`_ `with ` _`BMat(N: Int, M: Int)`_, `e.g. `
```
var bm = new BMat(5,12)   // construct directly a BMat
```

`Also, we can construct first an EJML ` _`BlockMatrix`_ `and then wrap a ScalaLab ` _`BMat`_ `e.g. `
```
var ejmlbm = new org.ejml.data.BlockMatrix64F(10, 20)  // construct an EJML BlockMatrix
var bm2 = new BMat(ejmlbm)  // wrap a BMap around the EJML BlockMatrix64F
```

## Apply operators ##
`The selection of BMat elements is performed in a Matlab-like style, e.g. :`
```
var bmr = rand0b(30, 40)  // get a random block matrix of 30 rows by 40 cols
var bmr3_4 = bmr(3,4)  // get the corresponding element
bmr(3,4) *= 100   // multiply it by 100
bmr3_4 = bmr(3,4)   // take the upadated value
var brows3to5 = bmr(3::5, ::)  // take rows 3 to 5 all columns
var brows3to5by2 = bmr(3::2::5, ::)  // take rows 3 to 5 by 2, all columns 
var bcols3to6 = bmr(::, 3::6)  // take columns 3 to 6, all rows
var bcols3to6by2 = bmr(::, 3::2::6)  // take columns 3 to 6 by 2, all rows
var brows2to5by2cols3to9by3 = bmr(2::2::5, 3::3::9)   // extract the corresponding submatrix
```


## Get the wrapped BlockMatrix64F matrix ##
`It can be very useful to get the EJMLs BlockMatrix64F object on which BMat is based. This is accomplished with ` _`getBM`_, `eg : `
```
var ejmlBM64F = bmr.getBM
```


## Apply a function to all the BMat's elements with map ##

`One of the most useful functional style operation is to apply a function to all the elements f. This can be done with ` **`map`**, `e.g.`
```
var bmo = ones0b(20, 30)  // a block matrix of ones
def  f(x: Double): Double = x*x*x/1.5
bmo = (bmo map f) map sin  // map the function f and the sin function
```

## Arithmetic Operators and Trigonometric functions ##

`Arithmetic operators on block matrices are overloaded and can be performed as usual. Also trigonometric functions work on block matrices, e.g. `
```
var bm = ones0b(40, 30)
var bm2 = ones0b(30, 50)
var ff = 10+bm 
var sinb = 10.2*sin(bm)
var mminusTimes = -bm*bm2
var sm = ones0b(25,25)
var mExpr = sm+0.23*sm*sm - sm*7.4 +cos(0.45*sm)  // arbitrary expression

```

## More EJML Examples ##

`Create a random symmetric positive definite matrix `

```
  // Creates a random symmetric positive definite matrix.
    // @param width The width of the square matrix it returns.
     // @param rg Random number generator used to make the matrix.
     // @return The random symmetric  positive definite matrix.
     var rnd = new java.util.Random
     var width = 8
     var  symmPosDefEJML = scalaSci.EJML.StaticMathsEJML.createSymmPosDef(width, rnd)
```

` Creates a random symmetric matrix whose values are selected from an uniform distribution  from min to max, inclusive.`

```
  //   @param length Width and height of the matrix.
  //    @param mn Minimum value an element can have.
  //   @param mx Maximum value an element can have.
  //   @param rg Random number generator.
  //    @return A symmetric matrix.
 

     var rnd = new java.util.Random
     var N = 8; var mn = -7.5; var mx = 4.5
     var symmEJML = scalaSci.EJML.StaticMathsEJML.createSymmetric(N, mn, mx,  rnd)
```



`Creating an upper triangular matrix whose values are selected from a uniform distribution.  If hessenberg is greater than zero then a hessenberg matrix of the specified degree is created instead.`
```
//   @param dimen Number of rows and columns in the matrix..
//  @param hessenberg 0 for triangular matrix and > 0 for hessenberg matrix.
//  @param mn minimum value an element can be.
//  @param mx maximum value an element can be.
//  @param rg random number generator used.
//  @return The randomly generated matrix.
  
     var rnd = new java.util.Random
     var dimen = 8; var hessenberg = 0; var mn = -7.5; var mx = 4.5
     var triagEJML = scalaSci.EJML.StaticMathsEJML.createUpperTriangle(dimen, hessenberg, mn, mx,  rnd)
     hessenberg = 1
     var hessenbergEJML = scalaSci.EJML.StaticMathsEJML.createUpperTriangle(dimen, hessenberg, mn, mx, rnd)
```



> ` Creating a random orthogonal or isometric matrix, depending on the number of rows and columns. The number of rows must be more than or equal to the number of columns. `

```
     //  @param numRows Number of rows in the generated matrix.
     //  @param numCols Number of columns in the generated matrix.
     // @param rg  Random number generator used to create matrices.
     //  @return A new isometric matrix.
     
     var rnd = new java.util.Random
     var numRows = 5; var numCols = 5;  
     var orthogEJML = scalaSci.EJML.StaticMathsEJML.createOrthogonal(numRows, numCols, rnd)
        // verify orthogonality
     var shouldBeIdentity = (orthogEJML~) * orthogEJML 
```



> `Creates a random diagonal matrix where the diagonal elements are selected from a uniform distribution that goes from min to max.`
```
    //   @param N, M Dimension of the matrix.
    //  @param mn Minimum value of a diagonal element.
    //  @param mx Maximum value of a diagonal element.
     //  @param rg Random number generator.
     // @return A random diagonal matrix.
    
     var rnd = new java.util.Random
     var N = 10; var mn = 0.2; var mx = 3.5; 
     var diagEJML = scalaSci.EJML.StaticMathsEJML.createDiagonal(N, mn, mx, rnd)
```

`Creating a diagonal matrix`

```
     var rnd = new java.util.Random
     var N = 10; var M=15; var mn = -2.2; var mx = 3.5; 
     var diagEJML = scalaSci.EJML.StaticMathsEJML.createDiagonal(N, M,  mn, mx, rnd)
```


` Creating a random matrix which will have the provided singular values.  The length of sv is assumed to be the rank of the matrix.  This can be useful for testing purposes when one needs to ensure that a matrix is not singular but randomly generated.`
```
     //  @param numRows Number of rows in generated matrix.
     // @param numCols NUmber of columns in generated matrix.
     // @param rg Random number generator.
     //  @param sv Singular values of the matrix.
     // @return A new matrix with the specified singular values.
    
     var rnd = new java.util.Random
     var N = 5; var M=8; 
     var sv = Array(0.2, 3.4, -4.5, 5.6, 0.34)
     var singEJML = scalaSci.EJML.StaticMathsEJML.createSingularValues(N, M,  rnd, sv)
```





