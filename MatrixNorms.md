# Computation of matrix norms #

`Matrix norms are a fundamental operation for various numerical algorithms. ScalaLab allows to compute these norms using many different Java libraries. The illustrative example creates different matrix types wrapping the same 2-D double array and then computes various norms on them.`


# Illustrative example for norm computations #

```
var d = AAD("7.8 -4 0.55; 8.9 -0.344 9.66") 

//  create from the same random array Matrices of different types
var ed  = new scalaSci.EJML.Mat(d)
var md  = new scalaSci.MTJ.Mat(d)
var ad  = new scalaSci.CommonMaths.Mat(d)
var nd  = new scalaSci.Matrix(d)
var nmd = new scalaSci.Mat(d)
var rrdd = new scalaSci.RichDoubleDoubleArray(d)

// test norm1:  maximum column sum of absolute values
var edn1=scalaSci.EJML.StaticMathsEJML.norm1(ed)
var mdn1=scalaSci.MTJ.StaticMathsMTJ.norm1(md)
var adn1=scalaSci.CommonMaths.StaticMathsCommonMaths.norm1(ad)
var nn1 = scalaSci.StaticMaths.norm1(nd)
var nj1 = scalaSci.StaticMaths.norm1(nmd)
var nrd1 = scalaSci.StaticMaths.norm1(rrdd)


// test Frobenius norm: sqrt of sum of squares of all elements.
var ednf=scalaSci.EJML.StaticMathsEJML.normF(ed)
var mdnf=scalaSci.MTJ.StaticMathsMTJ.normF(md)
var adnf=scalaSci.CommonMaths.StaticMathsCommonMaths.normF(ad)
var nnf = scalaSci.StaticMaths.normF(nd)
var njf = scalaSci.StaticMaths.normF(nmd)
var rrdf = scalaSci.StaticMaths.normF(rrdd)

// Infinity norm:    maximum row sum.
var ednInf=scalaSci.EJML.StaticMathsEJML.normInf(ed)
var mdnInf=scalaSci.MTJ.StaticMathsMTJ.normInf(md)
var adnInf=scalaSci.CommonMaths.StaticMathsCommonMaths.normInf(ad)
var nnInf = scalaSci.StaticMaths.normInf(nd)
var njInf = scalaSci.StaticMaths.normInf(nmd)

//  norm2(X) is the largest singular value of X, max(svd(X)).
var edn2=scalaSci.EJML.StaticMathsEJML.norm2(ed)
var mdn2=scalaSci.MTJ.StaticMathsMTJ.norm2(md)
var adn2=scalaSci.CommonMaths.StaticMathsCommonMaths.norm2(ad)
var nn2 = scalaSci.StaticMaths.norm2(nd)
var nj2 = scalaSci.StaticMaths.norm2(nmd)


```