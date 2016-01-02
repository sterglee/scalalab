# `Introduction ` #

`ScalaLab can utilize the MTJ  (http://code.google.com/p/matrix-toolkits-java/) library upon which builds a Matlab-like easy to use way of performing matrix operations. To work with the MTJ library more conveniently, you can open an MTJ based Scala interpreter from the pop-up menu of the ScalaLab console. It is important that the lower level routines of MTJ are all available, and we can call them, either when ScalaLab has not a higher-level equivalent or when we require maximum execution speed. Here we describe some aspects of working with the MTJ library presenting many examples.`


# A high-level interface to the MTJ library #

`The MTJ.Mat class implements zero-indexed two-dimensional dense matrices in ScalaSci that are based upon the Matrix Toolkit for Java Library.`

# Constructors #

`Mat(rows: Integer, cols: Integer) // Creates an MTJ Mat of size rows, cols initialized to zeros`

```
var m=new Mat(2, 3)
m.print // print the contents
```

`Construction by specifying the initial elements, e.g.`
```
var m = M("4.5 5.6 -4; 4.5 3 -3.4") // space separated
var m = M("4.5, 5.6, -4; 4.5, 3, -3.4") // comma separated
```

`Construct Mat by copying the array values,` _`Mat(da: Array[Array[Double]])`_ , `creates an MTJ Mat initialized with the da array`
```
var dd = new Array[Array[Double]](2,4)
dd(1)(1)=11
var mdd = new Mat(dd)
mdd.print // print finally the MTJ Matrix
```

# Retrieve lower-level MTJ  data structures #

`This is particularly useful when we have to work directly with MTJ routines`

`We can retrieve the DenseMatrix on which ScalaSci's Mat is based with: `
```
var rnd = rand0(5,6)  // a random MTJ based matrix
var dmmat = rnd.getDM
```


# Access operations #

`Access operations are implemented conveniently with a Matlab-like style, e.g. `
```
var a = rand0(10, 20)
var a1 = a(1) // get the 1th row as a matrix
var a1_3 = a(1, 3, ':') // get rows 1 until 3 all columns
var ac2_5 = a(':', 2, 5) // get columns 2 until 5 all rows
var ac2_3_1_5 = a(2, 3, 1, 5) // get rows 2 until 3, columns 1 until 5 
```



# Operators #

> `Matrix addition, subtraction and multiplication has been overloaded and works properly. Also addition and multiplication with scalars operates properly, e.g.`

```
var m1 = rand0(4,7)
var m2 = rand0(7,9)
var mmul = m1*m2 // multiplication
var madd = m1+7*m1+m1*9.4

```

# Basic Methods #


`def size: (Int, Int)  // Returns the number of rows and columns of the Mat`
```
var md = ones0(9)
md.size
```

`def length: Int  // Returns the number of rows of Mat `
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


`Transpose Matrix, operator ~, or method transpose`

```
var mm = ones0(9,4)  // create an MTJ matrix of 1s
mm(2,3) = 23  // update one element
var mmt = mm~   // transpose the matrix
mm.print
mmt.print
```


`Trigonometric functions`

`All the basic trigonometric functions, e.g. sin, cos, tan, asin, acos, atan, cosh, sinh, tanh etc. are applicable to matrices, e.g. `

```
val  aa = rand0(5,8)
var aas = sin(0.34*aa)+tan(aa*8.9)-3.4*tanh(0.23*aa)
aa.print
aas.print
```


val  aa = rand0(5,8)
var aas = sin(0.34*aa)+tan(aa*8.9)-3.4*tanh(0.23*aa)
aa.print
aas.print
}}}```