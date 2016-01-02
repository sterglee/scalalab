# Introduction #

`The ` _`RichDoubleDoubleArray`_ `type aims to be a powerful and convenient type for numerical computing in ScalaLab. It is compatible with the Java's double [][] type, therefore it can be directly interfaced with external Java libraries. `

`We started to develop multiple versions of fundamental operations, all operating on` _`RichDoubleDoubleArray`_ `type. The rationale for that is to allow the ScalaLab user to verify correctness of the routines and to compare the efficiency of different implementations. `


# Examples #

## `Invert a ` _`RichDoubleDoubleArray`_ `using multiple libraries.` ##

```
  val a = Rand(3, 3)
  val ia = ejml_inv(a)   // using EJML
  val mtjinv = mtj_inv(a)  // using MTJ
  val acinv = ac_inv(a)   //using Apache Commons
  val isOnes = a*ia
```