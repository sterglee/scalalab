# Introduction #

`NUMAL is a powerful numerical library that was translated to Java. However, its routines assume 1-indexed arrays and are somewhat complex. We implement a simplified interface to these routines with which:`

> ` 1. we use zero-indexed arrays (familiar to Java programmers)`

> `2. we simplify significantly the input/output interface of NUMAL routines although this comes at the expense of the generality of the routine). The object ` _`scalaSci.NUMALSimple`_ `implements these routines. Since these routines operate on standard Java 2-D and 1-D double arrays, they have general utility.  Also, the implicit conversions from ` _`RichDoubleArray`_ `to ` _`Array[Double]`_ `and from` _`RichDoubleDoubleArray`_ `to ` _`Array[Array[Double]]`_ `and vice versa, allows to cope with standard Java arrays, conveniently with a Matlab-like style within the ScalaLab environment.`



## ROUTINE:        decsol0 ##

`  Solves a well-conditioned linear system of equations Ax = b whose order is small relative to the number of binary digits in the number representation.`

**`Procedure Parameters: `**

_`	  def  decsol0(a: Array[Array[Double]], b: Array[Double]): RichDoubleArray`_

_`   a:   Array.ofDim[Double](n,n)`_
_`   entry:   the n-th order matrix`_

`  b:  Array[Double](n)`
`  entry: the right-hand side of the linear system`

_`Return Value:`_
` the calculated solution of the linear system`

**`Example`**


```
import scalaSci.NUMALSimple._

// zero-indexed array
var b0 = new Array[Double](4)
var a0 = Array.ofDim[Double](4, 4)

for (r<-1 to  4) {
  for (c<-1 to 4) 
    a0(r-1)(c-1) = 1.0/(r+c-1)
   b0(r-1) = a0(r-1)(2)
   }
    

var  b0s  = scalaSci.NUMALSimple.decsol0(a0,b0)

var shouldBeNearZero0 = a0*b0s-b0
```


## ROUTINE:        gsssol ##

`Solves a linear system of equations Ax = b.`

**`Procedure Parameters: `**

_`	  def  gsssol0(a: Array[Array[Double]], b: Array[Double]): RichDoubleArray`_

`   a:   Array.ofDim[Double](n,n)`
`   entry:   the n-th order matrix`

`  b:  Array[Double](n)`
`  entry: the right-hand side of the linear system`

_`Return Value`_
`the calculated solution of the linear system`


**`Example`**

```
import scalaSci.NUMALSimple._

var b = new Array[Double](4)
var a = Array.ofDim[Double](4, 4)

for (r<-1 to  4) {
  for (c<-1 to 4) 
    a(r-1)(c-1) = 1.0/(r+c-1)
   b(r-1) = a(r-1)(2)
   }
    

var  b0  = scalaSci.NUMALSimple.gsssol0(a,b)

// test the solution accuracy
var shouldBeNearZero = a*b0-b

```


## ROUTINE:        gsssolerb0 ##

`Solves a linear system of equations Ax = b, and provides an upper bound for the relative error in x`

**`Procedure Parameters: `**

_`	  def  gsssol0(a: Array[Array[Double]], b: Array[Double]):  (RichDoubleArray, Double)`_

`   a:   Array.ofDim[Double](n,n)`
`   entry:   the n-th order matrix`

`  b:  Array[Double](n)`
`  entry: the right-hand side of the linear system`

_`Return Value:`_
`the calculated solution of the linear system and an upper bound for the relative error`


**`Example`**

```
import scalaSci.NUMALSimple._

var b = new Array[Double](4)
var a = Array.ofDim[Double](4, 4)

for (r<-1 to  4) {
  for (c<-1 to 4) 
    a(r-1)(c-1) = 1.0/(r+c-1)
   b(r-1) = a(r-1)(2)
   }
    


var (b0, estimatedErrorBound)  = scalaSci.NUMALSimple.gsssolerb0(a,b)

var shouldBeNearZero = a*b0-b

```