# Introduction #

`jblas (http://jblas.org/) is a fast linear algebra library for Java. It is designed by Mikio Braun. It is based on BLAS and LAPACK, the de-facto industry standard for matrix computations, and uses state-of-the-art implementations like ATLAS for all its computational routines, making it very fast. It is essentially a lightweight wrapper around the BLAS and LAPACK routines. These packages originated in the Fortran community, which explains their archaic API. On the other hand, modern implementations are hard to beat performance-wise. jblas aims to make this functionality available to Java programmers such that they do not have to worry about writing JNI interfaces and calling conventions of Fortran code.`

`ScalaLab started experimentally to interface with these routines with its scalaSci.JBLAS.Mat and scalaSci.JBLAS.StaticMathsJBLAS classes. `

`You can switch to the experimental JBLAS based Scala Interpreter from the ` **`Configuration`** `menu `

`Then you execute for example the code: `
```
var m1 = ones0(1000, 1100)
var m2 = ones0(1100, 1500)

tic
var m12 = m1*m2
var tmMul = toc
```

`If you use also Scala Interpreters based on pure Java libraries to execute the same code, e.g. the EJML one, you should observe that matrix multiplication is significantly faster, i.e. about 4 to 6 times faster, using the Native BLAS.`

## `Fast operations with the RichDoubleDoubleArray using Native BLAS` ##

` The ` **`RichDoubleDoubleArray`** `is the fundamental ScalaLab array type that is interoperable easily with Java/Scala Array[Array[Double]] type. We started to explore the Native BLAS for speeding some operations. Linear time operations as addition/subtraction and multiplication with a scalar cannot be improved with native code, since the copy of data between JVM and native memory areas also requires linear time. However matrix multiplication for large matrices can be improved significantly and therefore we have the ` **`*#`** `operator that multiplies using Native BLAS. The following script demonstrates significant speedup. `

```
var a = ones(1600, 1200)

var b = ones(1200, 1200)

// default RichDoubleDoubleArray multiply
tic
var c = a * b
var tmDefaultMult = toc

// fast RichDoubleDoubleArray multiply
tic
var cb = a *# b
var tmFastMult = toc


```

`Also, we have implemented and eigenvalue computation routines using Native BLAS but in this case the speedup is not important in comparison to the Java based eigenvalue computation. The following script demonstrates these routines. `

```

// benchmark eigendecompositions for RichDoubleDoubleArray
var m = ones(140,140)

var N = 1000 // repetitions
var g = bench(N, {()=> var evs = eig(m)})  // using default 
 
var gfevals = bench(N, {()=> var eigvals = eigVals(m)})  // using native BLAS eigenvalues
var gfevecs = bench(N, {()=> var eigvecs = eigVecs(m)})  // using native BLAS eigenvectors

```