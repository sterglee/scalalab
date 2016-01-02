# Introduction #

`It is convenient to be able to select/update Matrix ranges with a Matlab-like style. ScalaLab (staring from Apr 17 version) improved on the routines that select and update Matrix sub-ranges. These routines have the same interface independent of the Matrix type.  We illustrate those operations by means of examples. `


# Examples of Matrix range select operations #

`Let create a zero-indexed matrix and fill it with some entries. `

```

var N = 4; var M = 5;
var a = new Mat(N,M)
for (r<-0 until N)
  for (c<-0 until M)
     a(r, c) = r*c
```

`We can select the row with index 2 with: `

```
val ridx = 2
val ar2 = a(ridx, ::)
```

` We can select rows 1 and 2 as: `
```
val a_r1_r2 = a(1, 2, ::)
```

` We can specify a "step" parameter, in order not to take rows consecutively, e.g. `

```
val a_r1_s2_r4 = a(1, 2, 4, ::)
```

`Symmetrically we can work with columns: `
```
val cidx = 3
val ac3 = a(::, cidx)
val a_c1_c3 = a(::, 1, 3)
val a_c1_s2_c5 = a(::, 1, 2, 4)
```

` We can select across both rows and columns using the parameter pattern` _`a(startRow, stepRow, endRow, startCol, stepCol, endCol)`_ `, e.g. `
```
val apart = a(0, 2, 3, 0, 2, 4)
```

# `And an example of MATLAB like access/update operations:` #
```

// create a random matrix
var xx = rand(20)

var rowS = 1; var rowE = 16; var rowI = 2
var colS = 1; var colE = 16; var colI = 2

// select a row range
var ff = xx(rowS::rowE, ::)
// update that row range
xx(rowS::rowE, ::) = 8.8

// select a row range with increment rowI
var fff = xx(rowS::rowI::rowE, ::)
// update that row range
xx(rowS::rowI::rowE, ::) = 55.4

 
// select a column range
var ffc = xx(::, colS::colE)
// update that column range
xx(::, colS::colE)= -99.9

 
// select a column range with increment colI
var fffc = xx(::, colS::colI::colE)
// update that column range
xx(::, colS::colI::colE)= 33

// some more select operations
var sel1 = xx(rowS::2::rowE, 3::2::colE)+5.7
var sel2 = xx(rowS::rowE, 3::2::colE)+57.9
var sel3 = xx(rowS::rowE, 3::colE)
var sel4 = xx(rowS::rowE, 3::2::colE)
var sel5 = xx(rowS::rowE, ::)
var sel6 = xx(::, 3::colE)

var sel7 = xx(2, ::)  // 2nd row
var sel8 = xx(::, 2) // 2nd column
var sel9 = xx(2, 2::3) // 2nd row, elements 2 up to 3
var sel10 = xx(2, 2::2::10)   // 2nd row, elements 2 up to 10 by step 2
var sel11 = xx(3::6, 3)  // 3nd column, rows 3 up to 6
var sel12 = xx(2::3::15, 4) // 4th column, rows 2 to 15 by step 3


// And some update operations:

var x = rand0(20); x(1::2::6, 2::3)= 150.4
var x2 = rand0(20); x2(3::5, 2::2::12)= 50.4
var x3 = rand0(9); x3(2::3, 5::6)=88

x(2,::) = 22.3   // all elements of row 2 to 22.3
x(::, 3) = 12   // all elements of column 3 to 12
x(2, 3::4) = 44 // elements 3 up to 4 of row 2 to 44
x(2, 3::2::12) = 122.2  // elements 3 up to 12 by step 2 of row 2 to 122.2
x(::, 3) = 33.3  // all elements of column 3 to 33.3
x(2::5, 3) = -4.3  // elements of rows 2 up to 5 of column 3 to -4.3
x(2::3::15, 3) = sin(x(1,1)) // elements of rows 2 to 15 by step 3 to sin(x(1,1))




```


# Examples of Matrix range update operations #

`Let create two simple zero-indexed matrices `

```
var a = rand0(4, 8)
var o = ones0(2, 3)
```


`We can update a matrix range copying another matrix at the specified index as: `
```
a(1,  2, ::)= o  // copy matrix o within a starting at (1,2)
```

`The operation above overwrites the previous matrix contents at the corresponding positions and resizes the destination matrix, if necessary.`

`We can also specify steps for rows and columns, in order not to copy the source matrix consecutively, e.g. `

```

var al = rand0(15, 30)
var ol = ones0(12, 13)

var dx = 2; var dy = 3
al(1, 2, dx, dy)= ol; 

```

# Using a vector to fill Matrix rows or columns #

`The use of a vector in order to fill rows and columns of a matrix can be convenient. Here are some examples: `

```
val a = zeros0(10, 50)
val v50 = vfill(50, -1)  // used to fill columns
val v10 = vfill(10, 1) // used to fill rows

a(2, ::) = v50  // row 2 has -1 s
a(::, 3) = v10  // column 3 has 1s

```

# Selecting the specified rows and columns of a matrix #

`Extracting sets of rows and columns from a matrix is many times a convenient operation. `

`We can extract the columns specified with true values with an array`  _`colIndices`_`.The new matrix is formed by using all the rows of the original matrix but with using only the specified columns. Example: `
```
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(true, false, true, false)
 var extract0_2cols = testMat(::, colIndices)
```

`We can extract the rows specified with true values with an array ` _`rowIndices`_`. The new matrix is formed by using all the columns of the original matrix but with using only the specified rows. Example: `

```
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var rowIndices = Array(true, false, true)
 var extract0_2rows = testMat( rowIndices, ::)
```



`We can extract the rows specified  with  an  array ` _`rowIndices`_`. The new matrix is formed by using all the columns of the original matrix but with using only the specified rows. The rows at the new matrix are arranged in the order specified with the array `_`rowIndices`_`. Example: `

```
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 var rowIndices = Array(3, 1)
 var extract3_1rows = testMat(rowIndices, ::)
```

`We can extract the columns specified with true values with an array ` _`colIndices`_ `.The new matrix is formed by using all the rows of the original matrix but with using only the specified columns.`
```
 var testMat = M0(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 var colIndices = Array(true, false, true, false)
 var extract0_2cols = testMat(::, colIndices)
```

## Filtering rows and columns of a matrix ##

`We can filter all the rows/columns of the matrix according to a predicate. The predicate is a function from the Int index of row/column to a boolean value. The relevant routines are as follows: `
```

  def  filterRows(predicate:  Int  => Boolean): specificMatrix
  def  filterColumns(predicate:  Int => Boolean): specificMatrix 

```

`For example, to return all the even numbered rows and columns of a matrix:`
```
      val  x = rand0(10, 13)
      def isEven(n: Int) = if (n % 2 == 0) true else false   // define the predicate
      val xevenRows = x filterRows isEven
      val xevenCols = x filterColumns isEven
   
```

> ## Displaying the contents of a matrix ##

`By default a matrix is displayed using the  results returned by ` _`toString()`_ `, that however truncate large matrices, since there is a severe performance problem if we compute and display the resulting huge strings.`

`We can however display the whole contents using the ` _`print()`_ `method.`

`Also, we can browse the matrix contents with the possibility of altering them, using a convenient JTable based presentation, with the ` _`browse()`_ `method. For example: `

```
val x = rand(30, 25)
x.print // display the contents
x.browse  // browse them using a JTable, editing cells affects the contents of the matrix

```


`We can also pass a variable name to the browse command, to help the user especially when many matrix contents windows are open: `

```
val myMatrix  = rand0(10, 15)
myMatrix.browse("myMatrix")  // browse them using a JTable, editing cells affects the contents of the matrix

```