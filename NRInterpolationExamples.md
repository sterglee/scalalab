# Introduction #

**`Numerical Recipes`** `is a classic text on Numerical Computation containing a lot of superb quality routines covering wide fields of numerical computing. Perhaps even more important,is that the book explains very well the algorithms on which the routines are based. Therefore, Numerical Recipes (NR) is excellent for students and researchers. Furthermore, generally NR routines are efficient, therefore they also fit for "production" applications. `

`Thanks to Huang Wen Hui that performed a translation to Java, and to the authors of the Numerical Recipes book we can utilize that routines as a standard ScalaLab library and explore them in a Matlab-like way from ScalaLab. This page aims to provide some examples in order to practice the NR ` **`interpolation`** `routines. `

## Polynomial Interpolation ##

```


import com.nr.test.NRTestUtil._

import com.nr.interp.Poly_interp
import com.nr.ran.Ran


    	var N = 20
    	var x = new Array[Double](N)
    	var y = new Array[Double](N)
    	var xx = new Array[Double](N)
    	var yy = new Array[Double](N)
    	var zz = new Array[Double](N)
	var dyy = new Array[Double](N)

		
    // Test Poly_interp
    println("Testing Poly_interp")
    for (i<-0 until N) {
      x(i) = (i * 1.0)/(N-1.0)
      y(i)=1.0 + x(i)*(1.0+x(i)*(1.0+x(i)*(1.0+x(i))))
    }
    
    var  myran = new Ran(17)
    var  z = new Poly_interp(x,y,3)
    
    for (i <- 0  until N) {
      xx(i) = myran.doub()
      yy(i) = z.interp(xx(i))     // interpolated values
      dyy(i) = z.dy        // Estimated errors
      zz(i) = 1.0 + xx(i)*(1.0 + xx(i) *(1.0 + xx(i) * (1.0 + xx(i))))  // Actual Values
    }
    println("     Poly_interp: Max. estimated error: " + maxel(dyy))
    println("     Poly_interp: Max. actual error:  " + maxel(vecsub(zz,yy)))

figure(1); title("Demonstration of Polynomial Interpolation")
//plot(x, y, Color.RED, "Actual function")
plot(x, y, Color.RED, "Actual Points")
hold(on)
scatterPlotsOn
plotMarks(xx, yy, Color.BLUE, "Interpolated points")





```


## Laplace Interpolation ##
```

import  com.nr.NRUtil._
import  com.nr.test.NRTestUtil._
import  java.lang.Math._

import com.nr.interp.Laplace_interp
import com.nr.ran.Ran


    
    val  N = 100
    val  M = 100
    val  NBAD = 1000
    var  sbeps = 0.01
    var  actual = Array.ofDim[Double](N, M)
    

    // Test Laplace_interp
    println("Laplace_interp")
    var  myran = new Ran(17)
    for (i <- 0 until N)
      for (j <- 0 until M)
        actual(i)(j) = cos(i.toDouble / 20.0)*cos(j.toDouble/20.0)
    
    var  mat = buildMatrix(actual)
    for (i <- 0 until NBAD) {  // insert "missing" data
      var p = myran.int32p() % N
      var q = myran.int32p() % M
      mat(p)(q) = 1.0e99
    }
    
    println("     Initial discrepancy: " + maxel(matsub(actual,mat)))
    var mylaplace = new Laplace_interp(mat)
    mylaplace.solve()
    println("     Final discrepancy: " + maxel(matsub(actual,mat)))
    
```


## Spline Interpolation ##

```

import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub
import  java.lang.Math.acos
import  java.lang.Math.sin

import com.nr.interp.Spline_interp
import com.nr.ran.Ran


 var N = 20
    
    
 var  x = new Array[Double](N)
 var  y = new Array[Double](N)
 var  xx = new Array[Double](N)
 var  yy = new Array[Double](N)
 var  zz = new Array[Double](N)
 
    

    // Test Spline_interp
 println("Testing Spline_interp")
 var pi = acos(-1.0)
 for(i <- 0 until N) {
      x(i) = (i)*pi/(N-1)
      y(i) = sin(x(i))
   }

    
    var yp1 = 1.0
    var ypn = -1.0
    var z = new Spline_interp(x, y, yp1, ypn)
    
    var  myran = new Ran(17)
    for(i <- 0 until N) {
      xx(i) = pi*myran.doub()
      yy(i) = z.interp(xx(i))
      zz(i) = sin(xx(i))
    }

   
    println("     Spline_interp: Max. actual error:    " +maxel(vecsub(zz,yy)))

linePlotsOn
figure(1); title("Demonstration of Spline interpolation")
plot(x, y, Color.RED, "Actual")
scatterPlotsOn
plotMarks(xx, yy, Color.GREEN, "Spline Interpolated")


    

```

## Shepard Interpolation ##

```

import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub
import  java.lang.Math.cos


import com.nr.interp.Shep_interp
import com.nr.ran.Ran


  val  NPTS = 100
  val  NDIM = 2
  val  N = 10
  val  M = 10
  
  var  p = 5.0
  var  sbeps = 0.05
  
  val  pts = Array.ofDim[Double](NPTS, NDIM)
    
  val  y = new Array[Double](NPTS)
  val  actual = new Array[Double](M)
  val  estim = new Array[Double](M)
  val  ppt = new Array[Double](2)

    
  // Test Shep_interp
 println("Testing Shep_interp")
 var  myran = new Ran(17)
    
    val  pt = Array.ofDim[Double](M, 2)
    for (i <- 0 until M) {
      pt(i)(0) = N * myran.doub()
      pt(i)(1) = N * myran.doub()
      actual(i) = cos(pt(i)(0) / 20.0 )*cos( pt(i)(1) / 20.0)
    }



    var i = 0
    while  (i < N)
     {
     var j = 0
     while (j < N) {
        var k = N * i + j
        pts(k)(0) = j.toDouble
        pts(k)(1) = i.toDouble
        y(k) = cos(pts(k)(0)/20.0)*cos(pts(k)(1)/20.0)
        j += 1
      }
      i += 1
    }
    
    var  shep = new Shep_interp(pts, y, p) 
    for (i <- 0 until M) {
      ppt(0) = pt(i)(0)
      ppt(1) = pt(i)(1)
      estim(i) = shep.interp(ppt)
    }
    
    printf("     Discrepancy: " + maxel(vecsub(actual,estim)))

figure(1); title("GREEN: Actual data, RED: Shepard interpolated")
plot(pt(::, 0).getv, pt(::, 1).getv, actual, Color.GREEN)
hold(on)
plot(pt(::, 0).getv, pt(::, 1).getv, estim, Color.RED)

    
```

## Kridge Interpolation ##

```

import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub
import  java.lang.Math.cos

import com.nr.interp.Krig
import com.nr.interp.Powvargram
import com.nr.ran.Ran


  val  NPTS = 100
  val  NDIM = 2
  val  N = 10
  val  M = 10
  
   var sbeps = 0.01 // p=5.0,
    
    var  pts = Array.ofDim[Double](NPTS, NDIM)
    var  y = new Array[Double](NPTS)
    var  actual = new Array[Double](M)
    var  estim = new Array[Double](M) 
    var  ppt = new Array[Double](2)
    
    

    // Test Krig (and Powvargram)
    println("Testing Krig (and Powvargram)")
    var  myran = new Ran(17)
    var  pt = Array.ofDim[Double](M, 2)
    for (i <- 0 until M) {
      pt(i)(0) =  N.toDouble * myran.doub()
      pt(i)(1) = N.toDouble * myran.doub()
      actual(i) = cos(pt(i)(0)/20.0)*cos(pt(i)(1)/20.0)
    }
    
    for (i <- 0 until N) {
      for (j <- 0 until N) {
        var k = N * i + j
        pts(k)(0) = j.toDouble
        pts(k)(1) = i.toDouble
        y(k) = cos(pts(k)(0) / 20.0) *cos(pts(k)(1)/20.0)
      }
    }
    
    var   vgram = new Powvargram(pts, y)
    var   krig = new Krig(pts, y, vgram)
    for (i <- 0 until M) {
      ppt(0) = pt(i)(0)
      ppt(1) = pt(i)(1)
      estim(i) = krig.interp(ppt)
    }
    
    println("     Discrepancy: " + maxel(vecsub(actual,estim)))
    
```

## Bilinear Interpolation ##

```


import  com.nr.test.NRTestUtil._

import com.nr.interp.Bilin_interp
import com.nr.ran.Ran

  val N = 20
  var x1 = new Array[Double](N)
  var x2 = new Array[Double](N)
  var yy = new Array[Double](N)
  var zz = new Array[Double](N)

   var y = Array.ofDim[Double](N, N)
   
   
    // Test Bilin_interp
    println("Testing Bilin_interp")
    for(i <- 0 until N ) {
      x1(i) = i.toDouble/(N-1)
      for(j <- 0 until N) {
        x2(j) = j.toDouble/(N-1)
        y(i)(j) = x1(i) + x2(j)
      }
    }


    val  z = new Bilin_interp(x1, x2, y)
    var myran = new Ran(17)
    for (i <- 0 until N)  {
      var xx1 = myran.doub()
      var xx2 = myran.doub()
      yy(i) = z.interp(xx1, xx2)  // interpolated values
      zz(i) = xx1 + xx2        // Actual values
    }
    
    println("     Bilin_interp: Max. actual error:    " + maxel(vecsub(zz,yy)))
    
```


## Curve Interpolation ##

```

import  com.nr.NRUtil._
import  com.nr.test.NRTestUtil._
import  java.lang.Math._


import com.nr.interp.Curve_interp


    val  NDIM = 3
    val  NPTS = 20
    
    val  vec = Array ( 2.0 / sqrt(29.0), 3.0/sqrt(29.0), 4.0/sqrt(29.0))
    
    var  target = new Array[Double](NDIM)
    var  ptsin = Array.ofDim[Double](NPTS, NDIM)
    
    
    // Test Curve_interp
    println("Testing Curve_interp")

    // Test straight line
    for (i <- 0 until NPTS) {
      ptsin(i)(0) = i*vec(0)
      ptsin(i)(1) = i*vec(1)
      ptsin(i)(2) = i*vec(2)
    }
    target(0) = 2.5*vec(0)
    target(1) = 2.5*vec(1)
    target(2) = 2.5*vec(2)

    var  myCurve = new Curve_interp(ptsin)
    
    var f = myCurve.interp(2.5/(NPTS-1))

    println("     Discrepancy (straight line): " + maxel(vecsub(f,target)))

    
    // Test closed circle
    var  a = Array(0.0, vec(2), -vec(1)) 
    
    var  norm = sqrt(SQR(vec(2))+SQR(vec(1)))  // normalize a[]
    
    for (i <- 0 until  NDIM ) a(i) /= norm
    
    var  b = Array(vec(1)*a(2)-vec(2)*a(1),vec(2)*a(0)-vec(0)*a(2),vec(0)*a(1)-vec(1)*a(0)) // perpendicular to a[] and vec[]
    
    var pi=acos(-1.0)
    for (i <- 0 until NPTS) {
      var  theta = i*2.0*pi / NPTS
      ptsin(i)(0) = a(0) * cos(theta) + b(0)*sin(theta)
      ptsin(i)(1) = a(1) * cos(theta) + b(1)*sin(theta)
      ptsin(i)(2) = a(2) * cos(theta) + b(2)*sin(theta)
    }
    
    var theta = 2.5*2.0*pi/NPTS
    target(0) = a(0) * cos(theta) + b(0) * sin(theta)
    target(1) = a(1) * cos(theta) + b(1) * sin(theta)
    target(2) = a(2) * cos(theta) + b(2) * sin(theta)

    var close = true
    var  myCurve2 = new Curve_interp(ptsin, close)
    f = myCurve2.interp(2.5/NPTS)

    println("     Discrepancy (circle): " + maxel(vecsub(f,target)))
    
```


## Spline 2D Interpolation ##

```

import  com.nr.NRUtil.buildVector
import  java.lang.Math.abs


import com.nr.interp.Spline2D_interp


    var  xx1 = Array(1.0, 2.0, 3.0, 4.0)
    var  xx2 = Array(1.0, 2.0, 3.0, 4.0)
    var  x1 = buildVector(xx1)
    var  x2 = buildVector(xx2)
    
    var  y = Array.ofDim[Double](4, 4)
    

    // Test Spline2D_interp
    println("Testing Spline2D_interp")
    for(i <- 0 until  4) 
      for(j <- 0  until 4) 
        y(i)(j) = xx1(i) + xx2(j)

    var  z = new Spline2D_interp(x1, x2, y)

    var res = z.interp(2.5, 2.5)  // this should be 5.0

    if (res-5.0==0)
     println("Correct")
 

```

## RBF Interpolation ##

```
 
import  com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub
import java.lang.Math.cos

import com.nr.interp.RBF_gauss
import com.nr.interp.RBF_interp
import com.nr.interp.RBF_inversemultiquadric
import com.nr.interp.RBF_multiquadric
import com.nr.interp.RBF_thinplate
import com.nr.ran.Ran


    var NPTS=100    // the number of points of the data set
    var NDIM=2   // the dimensionality of the data set
    var N=10
    var M=10
    var sbeps=0.05
    var pts  = Array.ofDim[Double](NPTS, NDIM)
    var y = new Array[Double](NPTS)  
    
    var actual = new Array[Double](M)  
    
    var estim1 = new Array[Double](M)
    var estim2 = new Array[Double](M)
    var estim3 = new Array[Double](M)
    var estim4 = new Array[Double](M)
    var estim5 = new Array[Double](M)
    var ppt = new Array[Double](2)
    var globalflag=false

    
    // Test RBF_interp
    var myran = new Ran(17)
    var pt = Array.ofDim[Double](M, 2)
    
    // fill M  2-D points
    for (i<-0 until M) {
      pt(i)( 0) = N*myran.doub()
      pt(i) (1) =N*myran.doub()
      actual(i)=(cos(pt(i)(0))/20.0)*(cos(pt(i)(1))/20.0)
    }
    
    
    var k=0
    for (i<-0 until N) {
      for (j <- 0 until N) {
        k=N*i+j
        pts(k)(0)= j
        pts(k)(1)= i
        y(k) = (cos(pts(k)(0))/20.0)*(cos(pts(k)(1))/20.0)
      }
    }

    println("Testing RBF_interp with multiquadric function")
    var scale=3.0
    var multiquadric = new RBF_multiquadric(scale)
    
    //  the nXdim matrix   pts inputs the data points, the vector y 
    // the function values, multiquadratic contains the chosen radial basis function,
    // derived from the class RBF_fn. The false boolean gives RBF 
    // interpolation, otherwise true gives NRBF
    var myRBFmqf = new RBF_interp(pts, y, multiquadric,false)
    
    for (i<-0 until M) {
      ppt(0) = pt(i)(0)
      ppt(1) = pt(i)(1)
      estim1(i)=myRBFmqf.interp(ppt)
    }

                    
    printf("     Discrepancy: %f\n", maxel(vecsub(actual,estim1)))
    var localflag = maxel(vecsub(actual,estim1)) > sbeps
    globalflag = (globalflag || localflag)
    if (localflag) {
      println("*** RBF_interp,multiquadric: Inaccurate multquadric interpolation with no normalization.")
    }
   
    var diffRBF = actual-estim1
    figure(1); plot(diffRBF, "Difference with RBF interpolation")
    
    println("Testing RBF_interp with thinplate function")
    scale=2.0
    var thinplate = new RBF_thinplate(scale)
    var myRBFtpf = new RBF_interp(pts, y, thinplate, false)
    for (i<-0 until M) {
      ppt(0) = pt(i)(0)
      ppt(1)=pt(i)(1)
      estim2(i) =myRBFtpf.interp(ppt)
    }
    printf("     Discrepancy: %f\n", maxel(vecsub(actual,estim2)))
    localflag = maxel(vecsub(actual,estim2)) > sbeps;
    globalflag = (globalflag || localflag)
    if (localflag) {
      println("*** RBF_interp,thinplate: Inaccurate thinplate interpolation with no normalization.");
    }

     var diffRBFthinPlate = actual-estim2
    figure(2); plot(diffRBFthinPlate, "Difference with RBF interpolation, thin plate")
    
    println("Testing RBF_interp with gauss function")
    scale=5.0
    var gauss = new RBF_gauss(scale)
    var myRBFgf = new RBF_interp (pts,y,gauss,false)
    for (i<-0 until M) {
      ppt(0) = pt(i)( 0)
      ppt(1)=pt(i)(1)
      estim3(i)=myRBFgf.interp(ppt)
    }
    printf("     Discrepancy: %f\n", maxel(vecsub(actual,estim3)))
    localflag = maxel(vecsub(actual,estim3)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      println("*** RBF_interp,gauss: Inaccurate gauss interpolation with no normalization.")
      
    }
    
    var diffRBFGaussFunction = actual-estim3
    figure(3); plot(diffRBFGaussFunction, "Difference with RBF interpolation, Gauss function")
   

    println("Testing RBF_interp with inversemultiquadric function")
    scale=3.0
    var inversemultiquadric = new RBF_inversemultiquadric(scale)
    var myRBFimqf =new RBF_interp(pts,y,inversemultiquadric, false)
    for (i<-0 until M) {
      ppt(0)=pt(i)(0)
      ppt(1)=pt(i)(1)
      estim4(i)=myRBFimqf.interp(ppt)
    }
    printf("     Discrepancy: %f\n", maxel(vecsub(actual,estim4)))
    localflag = maxel(vecsub(actual,estim4)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      println("*** RBF_interp,inversemultiquadric: Inaccurate inversemultiquadric interpolation with no normalization.")
    }


    var diffRBFmultiquadraticFunction = actual-estim4
    figure(4); plot(diffRBFmultiquadraticFunction, "Difference with RBF inverse multiquadratic function")
   
    // Test same interpolators with normalization turned on
    scale=3.0
    println("Testing RBF_interp with multiquadric function")
    var myRBFmqt = new RBF_interp(pts,y,multiquadric,true)
    for (i<-0 until M) {
      ppt(0)=pt(i)(0)
      ppt(1)=pt(i)(1)
      estim5(i) =myRBFmqt.interp(ppt)
    }
    printf("     Discrepancy: %f\n", maxel(vecsub(actual,estim5)))
    localflag = maxel(vecsub(actual,estim5)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      println("*** RBF_interp,multiquadric: Inaccurate multiquadric interpolation with normalization.");
     }

    var diffRBFnormalization = actual-estim5
    figure(4); plot(diffRBFnormalization, "Difference with RBF normalization turned on")
   
    
```