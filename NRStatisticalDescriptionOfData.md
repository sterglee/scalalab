# Introduction #

`This page presents examples illustrating the statistical routines presented in the Numerical Recipes book.`


## Example 1 - Testing avevar ##


```


import com.nr.NRUtil.buildVector
import com.nr.stat.Moment.avevar
import com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub
import java.lang.Math.acos
import java.lang.Math.sin
import org.netlib.util.doubleW

 var  NPTS=10000
 var  NBIN=101
 var  NPNB=NPTS+NBIN
 
 var  n=0
 var  pi=acos(-1.0)
 
    // Expected ave,vrnce
 var  expect= Array(pi/2.0, 0.467401)
    
 var ave=new doubleW(0)
 var vrnce=new doubleW(0)
    
 var temp=new Array[Double](NPNB)
 var e=buildVector(expect)
 var  localflag=false; var globalflag=false;

    
    // Test avevar
    println("Testing avevar")

    // Create a sinusoidal distribution
    var x=0.0
    while (x<=pi) {
    	 var nlim=(0.5+sin(x)*pi/2.0*NPTS/NBIN).asInstanceOf[Int]
      for (k<-0 until  nlim) { temp(n) = x; n+=1 }
      x += pi/NBIN
     }

  var data = new   Array[Double](n)
  for (k<-0 until n) data(k) = temp(k)

   avevar(data, ave, vrnce)

   var observe  = Array(ave.`val`, vrnce.`val`)

   var o = buildVector(observe)

   var sbeps = 2.0e-4
   localflag = maxel(vecsub(e,o))>sbeps
   globalflag = globalflag || localflag
  if (localflag) {
      fail("*** moment: Reported average or variance of sinusoidal distribution was out of spec");
    }

    if (globalflag) println("Failed\n")
    else println("Passed\n")
   
  
     
```


> ## Testing Student's t-Test for Significantly Different Means ##
```


// a test for Student's t-Test for significantly different means

import  com.nr.stat.Stattests.ttest
import org.netlib.util.doubleW
import com.nr.ran.Normaldev

 var NPTS=2000
 var  data1 = new Array[Double](NPTS)
 var  data2 = new Array[Double](NPTS)
 var  data3 = new Array[Double](NPTS)   
    // Test ttest
    println("Testing ttest")

    // Generate gaussian distributed data
     var  ndev1 = new Normaldev(0.0, 1.0, 17)
     var  ndev2 = new Normaldev(0.02, 2.0, 14)   // a distribution with close mean 
    // Special case: identical distributions
	for (i<-0 until  NPTS)   data1(i) = ndev1.dev()
	for (i<-0 until  NPTS)  data2(i) =  ndev2.dev()
	
     var  twSimilar = new doubleW(0)
     var  pwSimilar = new doubleW(0)
     ttest(data1, data2, twSimilar, pwSimilar)
     var tvSimilar = twSimilar.`val`  // Student's t
     var pvSimilar = pwSimilar.`val`   // p-value

    println(" t value for similar = "+tvSimilar +", p value for similar = "+pvSimilar)
 

     var  ndev3 = new Normaldev(2, 1.0, 14)   // a distribution with not close mean 
     for (i<-0 until  NPTS) data3(i) =  ndev3.dev()
     var  twDifferent = new doubleW(0)
     var  pwDifferent = new doubleW(0)
     ttest(data1, data3, twDifferent, pwDifferent)
     
     var tvDifferent = twDifferent.`val`
     var pvDifferent = pwDifferent.`val`
     
    println(" t value for different = "+tvDifferent + ", p value for different = "+pvDifferent)
     
```