# Introduction #

`MTJ offers many sparse matrix classes and associated iterative algorithms for the solution of classical problems. `

`ScalaLab started to implement a higher-level interface to this functionality. For example, the CCMatrix class implements a Sparse class based on the Compressed Column Storage format of MTJ. `

## Example of Compressed Column Storage format ##
`Here is an example illustrating some basic operations. `

```



import no.uib.cipr.matrix
import no.uib.cipr.matrix.sparse


var nrows = 10; var ncols = 10
var d = Array.ofDim[Double](nrows, ncols)
d(2)(3) = 10
d(4)(4) = 44
// create a sparse matrix from the double [][] array
var sd = new CCMatrix(d)

var mtjCCM = sd.getCCM()   // get the MTJ based column cmpressed matrix representation

d(3)(5) =35
var sd2 = new CCMatrix(d)

// set an entry using putAt
sd(2, 1) =  21

// get entries using implicitly getAt
var elem2_1 = sd(2,1)
var elem2_2 = sd(2,2)


// test matrix addition
var sd1  = sd+sd2
sd
sd1

var sd10 = sd*100
sd10

var sdd = sd1-sd1

```

`We can solve also a sparse linear system as: `
```
var a = AAD("1, 2, 0; -1, 0, -2;  -3, -5, 1")

var b = Array(3.0, -5, -4)
var am = new CCMatrix(a)

var sol = am.BiCGSolve(am, b)

var residual = max(a*sol-b)   // should be very small
      

```


## Example on solving sparse systems ##

```

var filename = "/home/sp/NBProjects/csparseJ/CSparseJ/matrix/t1"
  
// load the sparse matrix stored in triplet format
var A = loadSparse(filename)
 
var b = vrand(A.Nrows).getv()

var x = solve(A, b)   // solve the system with the CSparse method

var Ad = SparseToDoubleArray(A)  // convert to double array

var residual = Ad*x - b  // verify: should be near zero


   // convert to an MTJ CCMatrix
 var ccms =    CSparseToCCMatrix(A)

 var xmtj = solve(ccms, b) // solve with the MTJ based iterative solver

var df = xmtj-x  // verify that the two solutions are equal

```