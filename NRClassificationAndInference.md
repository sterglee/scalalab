# Introduction #

`Here we adapt examples from Chapter 16 (Classification and Inference) of Numerical Recipes 3nd edition`


## Support Vector Machines ##

```


import com.nr.ci.Svm
import com.nr.ci.Svmgausskernel
import com.nr.ci.Svmlinkernel
import com.nr.ci.Svmpolykernel
import com.nr.ran.Normaldev
import com.nr.ran.Ran

    var  M = 1000; var N = 2
    var  omega = 1.3
    
    var   x = new Array[Double](2)
    var   y = new Array[Double](M)
    var  data = Array.ofDim[Double](M, N)
    var globalflag = false
    var localflag = false

    
    // Test Svm
    println("Testing Svm")

    // Create two disjoint sets of points
    var  myran=new Ran(17)
    for (i <-0 until M/2) {
      y(i) = 1.0
      var a = myran.doub()
      var b = 2.0*myran.doub()-1.0
      data(i)(0)=1.0+(a-b)
      data(i)(1)=1.0+(a+b)
    }
    
    for (i <- M/2 until M) {
      y(i) = -1.0;
      var a = myran.doub();
      var b = 2.0*myran.doub()-1.0
      data(i)(0) = -1.0-(a-b)
      data(i)(1) = -1.0-(a+b)
    }
    
    // Linear kernel
    var linkernel=new Svmlinkernel(data,y)
    var linsvm=new Svm(linkernel)
    var lambda=10
    var k = 0
    var breakVar = false
    while (breakVar == false)  {
      var test = linsvm.relax(lambda,omega)
      k += 1
     if (test < 1.0e-3 || k >  100) breakVar = true
    } 
   
   var nerror = 0
    for (i<-0 until  M)  {
      var predictionError = ((y(i)==1.0) != (linsvm.predict(i) >= 0.0))
      nerror += (if  (predictionError) 1 else  0)
    }
  println("Errors: "+ nerror)

    // Need to add tests for harder test case and resolve issue that the two
    // support vectors give an erroneous indication for two of the kernels above

    // Example similar to the book
  var  ndev = new Normaldev(0.0, 0.5, 17)
    for (j<-0 until 4)  {   // Four quadrants
      for ( i <- 0 until (M/4) ) {
        k=  ((M/4)*j+i).asInstanceOf[Int]
        if (j == 0) {
          y(k) = 1.0
          data(k)(0) = 1.0 + ndev.dev()
          data(k)(1) = 1.0 + ndev.dev()
        } else if (j == 1) {
          y(k) = -1.0
          data(k)(0) = -1.0+ndev.dev()
          data(k)(1) = 1.0+ndev.dev()
        } else if (j == 2) {
          y(k) = 1.0
          data(k)(0) = -1.0 + ndev.dev()
          data(k)(1) = -1.0 + ndev.dev()
        } else {
          y(k) = -1.0
          data(k)(0) = 1.0+ndev.dev()
          data(k)(1) = -1.0+ndev.dev()
        }
      }
    }
        
    // Linear kernel
  var   linkernel2 = new Svmlinkernel(data, y)
  var  linsvm2 = new Svm(linkernel2)
  println("Errors: ")
  var xlambda = 0.001
  while (xlambda < 10000) {
      k = 0
      var breakVar = false
     while (breakVar == false)  {
        var test = linsvm2.relax(lambda,omega)
        k += 1
        if (test < 1.0e-3 || k >  100) breakVar=true
      }
      
      nerror = 0
      for (i <- 0  until  M) {
     var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
      nerror += (if  (predictionError) 1 else  0)
      }
      println(nerror)

            // Test new data
      nerror=0
      for (j <-0 until 4) {   // Four quadrants
        for (i<- 0 until M/4) {
          if (j == 0) {
            var yy =1.0
            x(0) = 1.0+ndev.dev()
            x(1) = 1.0+ndev.dev()
          } else if (j == 1) {
            var yy= -1.0;
            x(0) = -1.0+ndev.dev()
            x(1) = 1.0+ndev.dev()
          } else if (j == 2) {
            var yy=1.0
            x(0) = -1.0+ndev.dev()
            x(1)= -1.0+ndev.dev()
          } else {
            var yy = -1.0
            x(0) = 1.0+ndev.dev()
            x(1)= -1.0+ndev.dev()
          }
     var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
      nerror += (if  (predictionError) 1 else  0)
        }
      }
 
    xlambda *= 10
    println(nerror)
    
    }
    println()

    // Polynomial kernel
    var polykernel2 = new Svmpolykernel(data, y, 1.0, 1.0, 4.0)
    var polysvm2=new Svm(polykernel2)
    println("Errors: ")
    xlambda = 0.001
    while  (xlambda <  10000 ) {
      var k=0
      var breakVar = false
      while (breakVar==false) {
        var test = polysvm2.relax(lambda,omega)
        k += 1
        if (test < 1.0e-3 || k >  100) breakVar = true
      } 
      // Test training set
      nerror = 0
      for (i <- 0 until M) {
      var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
      nerror += (if  (predictionError) 1 else  0)
      }
      println(nerror)
      // Test new data
      nerror = 0 
      for (j <- 0 until  4) {   // Four quadrants
        for (i <- 0 until M/4) {
          if (j == 0) {
            var yy=1.0;
            x(0) =1.0 + ndev.dev()
            x(1)=1.0 + ndev.dev()
          } else if (j == 1) {
            var yy = -1.0
            x(0) = -1.0 + ndev.dev()
            x(1) = 1.0 + ndev.dev()
          } else if (j == 2) {
            var yy=1.0
            x(0) = -1.0 + ndev.dev()
            x(1) = -1.0 + ndev.dev()
          } else {
            var yy = -1.0
            x(0) = 1.0+ndev.dev()
            x(1) = -1.0+ndev.dev()
          }
      
          var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
          nerror += (if  (predictionError) 1 else  0)
        }
      }
      xlambda *= 10
      println(nerror)
    }
    println()

    // Gaussian kernel
    var gausskernel2 = new Svmgausskernel(data, y, 1.0)
    var gausssvm2 = new Svm(gausskernel2)
    println("Errors: ")
    xlambda = 0.001
    while (xlambda < 10000) {
      var k = 0
      var breakVar = false
      while (breakVar==false)  {
        var test = gausssvm2.relax( lambda, omega)
        k += 1
        if (test < 1.0e-3 || k >  100) breakVar = true
      }
      nerror = 0
      for (i<-0 until  M) {
         var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
         nerror += (if  (predictionError) 1 else  0)
      }
      println(nerror)
      
      // Test new data
      nerror = 0
      for (j<-0 until 4) {   // Four quadrants
        for (i <-0 until  M/4) {
          if (j == 0) {
            var yy = 1.0
            x(0) = 1.0 + ndev.dev()
            x(1) = 1.0 + ndev.dev()
          } else if (j == 1) {
            var yy = -1.0
            x(0) = -1.0+ndev.dev()
            x(1) = 1.0+ndev.dev()
          } else if (j == 2) {
            var yy =1.0
            x(0) = -1.0+ndev.dev()
            x(1) = -1.0+ndev.dev()
          } else {
            var yy = -1.0
            x(0) = 1.0+ndev.dev()
            x(1) = -1.0+ndev.dev()
          }
         
         var predictionError = ((y(i)==1.0) != (linsvm2.predict(i) >= 0.0))
         nerror += (if  (predictionError) 1 else  0)
        }
      }
      xlambda *= 10
      println("%d    ",nerror)
    }
    println()

  // Test the algorithm on test data after learning
  // Do a scan over lambda to find best value

    localflag = false;
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** Svm: *************************");
      
    }

    if (globalflag)  println("Failed\n")
    else   println("Passed\n")
  

```

## Gaussian Mixture Models ##

```


import  com.nr.NRUtil.SQR
import  com.nr.NRUtil._
import  com.nr.test.NRTestUtil._
import  java.lang.Math.sqrt

import com.nr.ci.Gaumixmod
import com.nr.ran.Normaldev


    var NDIM=2;   var NMEANS=4; var NPT=1000
    var flag = 0.0;  var sqrt2=sqrt(2.0)
    var ffrac = Array(0.25,0.25,0.25,0.25)
    var mmeans = Array(0.0,0.0,0.75,0.0,-0.25,-0.25,0.33,0.66)
    var gguess = Array(0.1,0.1,0.7,0.1,-0.2,-0.3,0.3,0.5)
    var ssigma = Array(0.1,0.1,0.02,0.2,0.01,0.1,0.1,0.05)
    var vvec1 = Array(1.0,0.0,1.0,0.0,sqrt2,sqrt2,sqrt2,sqrt2)
    var vvec2 = Array(0.0,1.0,0.0,1.0,-sqrt2,sqrt2,-sqrt2,sqrt2)

    var frac = buildVector(ffrac)

       
    var offset = new Array[Double](NMEANS)
    var guess = buildMatrix(NMEANS, NDIM, gguess) 
    var means = buildMatrix(NMEANS, NDIM, mmeans)
    var sigma = buildMatrix(NMEANS, NDIM, ssigma)
    var vec1 = buildMatrix(NMEANS, NDIM, vvec1)
    var vec2 = buildMatrix(NMEANS, NDIM, vvec2)
    var x = new RichDouble2DArray(NPT, NDIM)
    var globalflag = false

    // Test Gaumixmod
    println("Testing Gaumixmod")

    var    ndev = new Normaldev(0.0, 1.0, 17)
    
    // Generate four groups of data
    var k = 0
    for (i <- 0 until 4) {
      for (j <-0 until (NPT * frac(i)).asInstanceOf[Int]) {
        var d0 = sigma(i)(0) *  ndev.dev()
        var d1 = sigma(i)(1) *  ndev.dev()
        x(k, 0) = means(i)(0) + d0 * vec1(i)(0) + d1 * vec2(i)(0)
        x(k, 1) = means(i)(1) + d0 * vec1(i)(1) + d1 * vec2(i)(1)
        k+=1
      }
    }
   
    var xx =  x~    // transpose the array
    

 var gmix = new Gaumixmod(x, guess)

   var  i = 0
   var  breakFlag = false
   while ( i < 100 && breakFlag == false) {
      flag = gmix.estep()
      if (flag < 1.0e-6) breakFlag = true
      gmix.mstep()
      i += 1
    }

    // check for convergence
    //    System.out.println("  flag: %f\n", flag);
    var localflag = (flag > 1.0e-6)
    globalflag = (globalflag || localflag)
    if (localflag) {
      fail("*** Gaumixmod: No solution with 100 iterations");
    }

    // Check for correct determination of population fractions
    //    System.out.printf(maxel(vecsub(gmix.frac,frac)));
    var sbeps = 0.005
    localflag = maxel(vecsub(gmix.frac,frac)) > sbeps
    globalflag = (globalflag || localflag)
    if (localflag) {
      fail("*** Gaumixmod: Population fractions not accurately determined");
      }

    // Check for correct determination of means
    for (i <- 0 until NMEANS) 
       offset(i) = sqrt(SQR(gmix.means(i)(0) - means(i)(0))
      +SQR(gmix.means(i)(1)-means(i)(1)))
    //    System.out.printf(maxel(offset));
    localflag = maxel(offset) > 0.01
    globalflag = (globalflag || localflag)
    if (localflag) {
      fail("*** Gaumixmod: Means are incorrectly identified")
      
    }

    // Check for correct determination of covariance matrices
    for (i <- 0 until NMEANS) {
      println("%f %f\n", gmix.sig(i)(0)(0), gmix.sig(i)(0)(1)) 
      println("%f %f\n\n",gmix.sig(i)(1)(0), gmix.sig(i)(1)(1)) 
    }

    if (globalflag)  println("Failed\n")
    else  println("Passed\n")



var gmixTrans = (gmix.means)~

figure(1); 
scatterPlotsOn; plot(xx)   // plot the four groups of data that we constructed for testing K-means
hold("on")    
plotMarks( gmixTrans(0),  gmixTrans(1))

```