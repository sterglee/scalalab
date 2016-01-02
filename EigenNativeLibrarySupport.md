# Introduction #

`ScalaLab exploits the Jeigen project `

https://github.com/hughperkins/jeigen

`in order to utilize the fast C++ matrix library Eigen.`

`Currently, the relevant native library ` **`libjeigen.so`** `is preinstalled for Linux64. Also, the necessary .jar files are within the defaultToolboxes ScalaLab folder and thus they are installed automatically.`

`We provide some examples of using Eigen routines.`

## Matrix multiplication ##

```
import jeigen.DenseMatrix
import jeigen.Shortcuts._

                   
var N=3000
var dm1 = ones(N,N)

tic
var  dd = dm1.mmul(dm1)  // multiply with Eigen
var tm=toc

```

## `Matrix multiplication using ScalaSci matrix class that wraps JEigen library` ##

`Currently, ScalaLab provides a ScalaSci matrix class that wraps the JEigen class for Linux64 systems only (for Windows the DLLs for native JEigen support fail on load, I don't know why). That matrix class can be used as the other ScalaLab classes, e.g. we can perform matrix multiplication as: `

```


var N=2000

var x = ones0(N, N)

tic
var xx = x*x
var tm=toc


```
