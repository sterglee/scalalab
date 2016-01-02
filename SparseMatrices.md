# Introduction #

**` ATTENTION: this class is in development, features are not yet stable!! `**

`ScalaLab integrates support for sparse matrices based on the  CSparse implementation of Timothy A. Davis, translated to Java by Piotr Wendykier. We describe here some sparse matrix operations by means of examples.`


# Handling sparse matrices #

`Suppose that we have a sparse matrix that is stored in a file in triplet format, e.g. the first entry: ` _`2 2 3.0`_`,  means ` _`a(2,2) = 3.0`_`. The file that stores the matrix suppose that it has the following contents:`
```
2 2 3.0
1 0 3.1
3 3 1.0
0 2 3.2
1 1 2.9
3 0 3.5
3 1 0.4
1 3 0.9
0 0 4.5
2 1 1.7
```

`Suppose that we store the matrix in a file: ` _`/home/sp/NBProjects/csparseJ/CSparseJ/matrix/t1`_

`The command to load the sparse matrix is: `

```
var s = loadSparse("/home/sp/NBProjects/csparseJ/CSparseJ/matrix/t1")
```

`We can display its contents with: `

```

s.print

s.display
```

`The former prints the contents in the format close to the internal representation while the later in a two-dimensional array format.`

`We can access an element of the sparse matrix as usual, e.g. `
```
val s22 = s(2,2)
```

`takes the corresponding element of the matrix.`

`We can also assign new values as usual, e.g. `
```
s(1,2) = 12
```

`We can add and multiply two sparse matrices as usual `
```
val s2 = s+s
val sMs = s*s
```

`The transpose of a sparse matrix is obtained with the operator: ~ `

```
var ts = s~
```

` We can add sparse matrices as usual, e.g. `
```
var s2 = s+s
```

`Also, multiplication is as a matrix multiplication, e.g. `
```
var sm = s*s
```

`We can transform a sparse matrix to an Array[Array[Double]] as`
```
var da = toDouble(s)
```

`Conversely we can transform an Array[Array[Double]] to a Sparse matrix, e.g. `
```
var dd = Array.ofDim[Double](3,4)
dd(2)(3) = 23
dd(1)(2) = 12
var sparseDD = fromDoubleArray(dd)
```

`We can add, subtract and multiply Sparse matrices with numbers, e.g. `
```
var x10 = s+10
var x10ic = 10+s  // using implicit conversion
var y10 = s*10
var y10ic = 10*s  // using implicit conversion
```

`We can also negate the matrix, e.g. `
```
var sm = -s
```

`Applying a function to all the matrix elements with map can be very useful, e.g. : `

```

def f(x: Double) = x*x*1000   // some function

var sf = s map f  // apply the function
```