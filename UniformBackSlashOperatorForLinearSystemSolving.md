# Uniformity of notation for linear systems solving #

`The \ operator has implemented in new ScalaLab versions (i.e. ScalaLab210 and newer) to yield either the exact solution for a` _`NXN`_ `systems or to generate the proper solutions for overdetermined/under-determined cases otherwise. The following script illustrates its use. `


```


// test Array[Array[Double]]
   var A = $$(-1.2, 0.3, 4.3, null,
      0.2, 3.4, 5.6)
   var B = $$(4.5, null,  2.3)
   var x = scalaSci.ILapack.DGELS(A, B)
   var residual = A*x-B

  var xr = A \ B   // test RichDouble2DArray

// test EJML
   var Aejml = new scalaSci.EJML.Mat(A)
   var Bejml = new scalaSci.EJML.Mat(B)
   var xejml = Aejml \ Bejml
   var g = eye0(9)

// test JAMA Mat
 var Ajama = new scalaSci.Mat(A)
 var Bjama = new scalaSci.Mat(B)
 var xjama = Ajama \ Bjama   

 // test MTJ Mat
 var Amtj = new scalaSci.MTJ.Mat(A)
 var Bmtj = new scalaSci.MTJ.Mat(B)
 var xmtj = Amtj \ Bmtj

 // test Apache Commons
 var Aapache = new scalaSci.CommonMaths.Mat(A)
 var Bapache = new scalaSci.CommonMaths.Mat(B)
 var xapache = Aapache \ Bapache

 // test JBLAS
 var Ajblas = new scalaSci.JBLAS.Mat(A)
 var Bjblas = new scalaSci.JBLAS.Mat(B)
 var xjblas = Ajblas \ Bjblas

```