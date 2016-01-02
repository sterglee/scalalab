# Introduction #

`There are some important conventions about the naming of static routines, as for example: ` _`rand(n,m)`_ `copes with a ` _`RichDoubleDoubleArray`_ `type, `_`rand1(n, m)`_ `with an one-indexed NUMAL based ` _`scalaSci.Matrix`_ `type.`

`These conventions are very simple and are illustrated with the code below: `


```
// test rand()
var  mra1 = rand1(2,3)   //  one indexed Matrix
var  mra0 = rand0(2,3)   //  zero indexed Mat
var r1d = rand(9) // RichDoubleArray
var rra = rand(2,3)  //  RichDoubleDoubleArray
var r1D = Rand(9) //  Array[Double]
var r2D = Rand(3,7) // Array[Array[Double]
var vrnd = vrand(9)  // vector 

// test fill()
var v = 4.5
var  mfa1 = fill1(2,3, v)   //  one indexed Matrix
var  mfa0 = fill0(2,3, v)   //  zero indexed Mat
var mf1d = fill(6, v) // RichDoubleArray
var rfa = fill(2,3, v)  //  RichDoubleDoubleArray
var v1D = Fill(9, v)  // Array[Double]
var v2D = Fill(3,9, v) // Array[Array[Double]]
var vf = vfill(7, v)  // vector


// test ones()
var  moa1 = ones1(2,3)   //  one indexed Matrix
var  moa0 = ones0(2,3)   //  zero indexed Mat
var  mo1d = ones(8)  // RichDoubleArray
var mo2d = ones(2,3)  //  RichDoubleDoubleArray
var mO1D = Ones(8)  // Array[Double]
var mO2D = Ones(6,8)  // Array[Array[Double]]
var vo = vones(9)  // vector

// test zeros()
var  mza1 = zeros1(2,3)   //  one indexed Matrix
var  mza0 = zeros0(2,3)   //  zero indexed Mat
var  mz1d = zeros(8)  // RichDoubleArray
var mz2d = zeros(2,3)  //  RichDoubleDoubleArray
var mZ1D = Zeros(8)  // Array[Double]
var mZ2D = Zeros(6,8)  // Array[Array[Double]]
var vz = vzeros(9)  // vector

```