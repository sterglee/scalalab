# Introduction #

`Here some scripts that benchmark different matrix libraries are presented. Note, that some libraries (e.g. NUMAL) have many different routines for the same or similar operation. The faster such routine is not selected. `


## Matrix multiply ##

```



var nreps=10  // number of repetitions
var INC = 150  // matrix size increment

// matrix sizes array
var sizes = new Array[Double](nreps)

// arrays to keep times
var ejml_t = new Array[Double](nreps)
var mtj_t = new Array[Double](nreps)
var jama_t = new Array[Double](nreps)
var numal_t = new Array[Double](nreps)
var jblas_t = new Array[Double](nreps)
var commonMaths_t = new Array[Double](nreps)

for (rep <- 0 until nreps) {

    var cSiz = (rep+1)* INC   // matrix will be of size:  cSiz X cSiz
    sizes(rep) = cSiz*cSiz  // matrix size

        // EJML
    var ejml_x = scalaSci.EJML.StaticMathsEJML.rand0(cSiz, cSiz)
    tic
    var ejml_mx = ejml_x*ejml_x
    ejml_t(rep) = toc.toDouble

        // MTJ
    var mtj_x = scalaSci.MTJ.StaticMathsMTJ.rand0(cSiz, cSiz)
    tic
    var mtj_mx = mtj_x * mtj_x
    mtj_t(rep) = toc.toDouble

        // JAMA
    var jama_x = scalaSci.StaticMaths.rand0(cSiz, cSiz)
    tic
    var jama_mx = jama_x*jama_x
    jama_t(rep) = toc.toDouble

        // NUMAL
     var numal_x = scalaSci.StaticMaths.rand1(cSiz, cSiz)
     tic
     var numal_mx = numal_x*numal_x
     numal_t(rep) = toc.toDouble

        // JBLAS
     var jblas_x = scalaSci.JBLAS.StaticMathsJBLAS.rand0(cSiz, cSiz)
     tic
     var jblas_mx = jblas_x * jblas_x
     jblas_t(rep) = toc.toDouble

      // Apache Commons
      var commonMaths_x = scalaSci.CommonMaths.StaticMathsCommonMaths.rand0(cSiz, cSiz)
      tic
      var commonMaths_mx = commonMaths_x*commonMaths_x
      commonMaths_t(rep) = toc.toDouble

}

figure(1); hold("on")
plot(sizes, ejml_t, Color.BLUE, "EJML")
plot(sizes, mtj_t, Color.RED, "MTJ")
plot(sizes, jama_t, Color.GREEN, "Jama")
plot(sizes, numal_t, Color.YELLOW, "NUMAL")
plot(sizes, jblas_t, Color.MAGENTA, "JBLAS")
plot(sizes, commonMaths_t, Color.GRAY, "Apache Common Maths")


xlabel("Matrix Size"); ylabel("Time (secs)")

```

## Inverse of a matrix ##

```


var nreps=10  // numbewr of repetitions
var INC = 50  // matrix size increment

// matrix sizes array
var sizes = new Array[Double](nreps)

// arrays to keep times
var ejml_t = new Array[Double](nreps)
var mtj_t = new Array[Double](nreps)
var jama_t = new Array[Double](nreps)
var numal_t = new Array[Double](nreps)
var jblas_t = new Array[Double](nreps)


for (rep <- 0 until nreps) {

  
    var cSiz = (rep+1)* INC   // matrix will be of size:  cSiz X cSiz
    sizes(rep) = cSiz*cSiz  // matrix size

// create a positive definite matrix in order to be nonsingular (thus having inverse)
  var x = scalaSci.EJML.StaticMathsEJML.createSymmPosDef(cSiz, new java.util.Random(234234))
  var y = x.toDoubleArray 

        // EJML
    var ejml_x = new scalaSci.EJML.Mat(y)
    tic
    var ejml_ix = scalaSci.EJML.StaticMathsEJML.inv(ejml_x)
    ejml_t(rep) = toc.toDouble

        // MTJ
    var mtj_x = new scalaSci.MTJ.Mat(y)
    tic
    var mtj_ix = scalaSci.MTJ.StaticMathsMTJ.inv(mtj_x)
    mtj_t(rep) = toc.toDouble

        // JAMA
    var jama_x = new scalaSci.Mat(y)
    tic
    var jama_ix = scalaSci.StaticMaths.inv(jama_x)
    jama_t(rep) = toc.toDouble

        // NUMAL
     var numal_x = new scalaSci.Matrix(y)
     tic
     var numal_ix = scalaSci.StaticMaths.inv(numal_x)
     numal_t(rep) = toc.toDouble

 
}

figure(1); hold("on")
plot(sizes, ejml_t, Color.BLUE, "EJML")
plot(sizes, mtj_t, Color.RED, "MTJ")
plot(sizes, jama_t, Color.GREEN, "Jama")
plot(sizes, numal_t, Color.YELLOW, "NUMAL")

xlabel("Matrix Size"); ylabel("Time (secs)")


```

## Solution of a system of linear equations ##

```


var nreps=10  // number of repetitions
var INC = 50  // matrix size increment

// matrix sizes array
var sizes = new Array[Double](nreps)

// arrays to keep times
var ejml_t = new Array[Double](nreps)
var mtj_t = new Array[Double](nreps)
var jama_t = new Array[Double](nreps)
var numal_t = new Array[Double](nreps)
var jblas_t = new Array[Double](nreps)
var commonsMath_t = new Array[Double](nreps)

for (rep <- 0 until nreps) {

    var cSiz = (rep+1)* INC   // matrix will be of size:  cSiz X cSiz
    sizes(rep) = cSiz*cSiz  // matrix size

// create a positive definite matrix in order to be nonsingular (thus having inverse)
  var x = scalaSci.EJML.StaticMathsEJML.createSymmPosDef(cSiz, new java.util.Random(234234))
  var y = x.toDoubleArray 

        // EJML
    var ejml_x = new scalaSci.EJML.Mat(y)
    var ejml_b = scalaSci.EJML.StaticMathsEJML.rand0(cSiz,1)
    tic
    var ejml_ix = scalaSci.EJML.StaticMathsEJML.solve(ejml_x, ejml_b)
    ejml_t(rep) = toc.toDouble

        // MTJ
    var mtj_x = new scalaSci.MTJ.Mat(y)
    var mtj_b = scalaSci.MTJ.StaticMathsMTJ.rand0(cSiz,1)
    tic
    var mtj_ix = scalaSci.MTJ.StaticMathsMTJ.solve(mtj_x, mtj_b)
    mtj_t(rep) = toc.toDouble

       // JAMA
    var jama_x = new scalaSci.Mat(y)
    var jama_b = scalaSci.StaticMaths.rand0(cSiz,1)
    tic
    var jama_ix = scalaSci.StaticMaths.solve(jama_x, jama_b)
    jama_t(rep) = toc.toDouble

     
        // JBLAS
    var jblas_x = new scalaSci.JBLAS.Mat(y)
    var jblas_b = scalaSci.JBLAS.StaticMathsJBLAS.rand0(cSiz, 1)
    tic
    var jblas_ix = scalaSci.JBLAS.StaticMathsJBLAS.solve(jblas_x, jblas_b)
    jblas_t(rep) = toc.toDouble
        
     
}



figure(1); hold("on")
plot(sizes, ejml_t, Color.BLUE, "EJML")
plot(sizes, mtj_t, Color.RED, "MTJ")
plot(sizes, numal_t, Color.YELLOW, "NUMAL")
plot(sizes, jblas_t, Color.MAGENTA, "JBLAS")


xlabel("Matrix Size"); ylabel("Time (secs)")


```