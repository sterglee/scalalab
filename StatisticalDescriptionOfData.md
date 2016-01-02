# Introduction #

`The classic ` _`Numerical Recipes Third Edition`_ `book has a high quality presentation of many techniques for extracting useful statistics from data, at Chapter 14. Here we present some examples, on how these routines can be used in ScalaLab.`


## Pearson Coefficient ##

```

import com.nr.NRUtil.buildVector
import com.nr.stat.Stattests.pearsn
import java.lang.Math.abs
import java.lang.Math.log
import org.netlib.util.doubleW

   var N=10
   var  doses = Array(56.1,64.1,70.0,66.6,82.0,91.3,90.0,99.7,115.3,110.0)
   var  spores = Array(0.11, 0.40, 0.37,0.48,0.75,0.66,0.71,1.20,1.01, 0.95)
   
    var   prob1=new doubleW(0)
    var   r1 = new doubleW(0)
    var   z1 = new doubleW(0)
    var   prob2=new doubleW(0)
    var   r2 = new doubleW(0)
    var   z2 = new doubleW(0)
    var   sbeps=1.e-16
    var  expect = Array(0.9069586,0.2926505e-3,1.510110)
    var dose = buildVector(doses)
    var spore = buildVector(spores)
    var dose2 = new Array[Double](N)
    var  localflag = false
    var  globalflag=false

    

    // Test pearsn
    println("Testing pearsn")

    pearsn(dose, dose, r1, prob1, z1)
    r1.`val`

    localflag = (r1.`val`!= 1.0)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Correlation of an array with itself is not reported as perfect")
   }

    localflag = (prob1.`val` > 1.e-16)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Probability for a perfect correlation was not zero")
   }

    for (i <- 0 until N) 
       dose2(i) = 200.0-dose(i)
       
    pearsn(dose, dose2, r2, prob2, z2)

    localflag = (r2.`val` != -1.0)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Correlation of an array with its negative is not reported as perfect")
   }

    localflag = (prob2.`val`> sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Probability for a perfect anticorrelation was not zero")
    }

    sbeps = 1.e-6
    pearsn(dose, spore, r1, prob1, z1)   // Data with known results
    localflag = (abs(r1.`val` - expect(0)) > sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Unexpected correlation coefficient for test data")
     }

    localflag = (abs(prob1.`val` - expect(1))  >  sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Unexpected probability for test data")
     }

    localflag = (abs(z1.`val` - expect(2)) > sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Unexpected Fisher's z coefficient for test data")
   }

    pearsn(spore, dose, r2, prob2, z2)
    localflag = (r2.`val` != r1.`val`)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Correlation coefficient modified when arrays swapped");
   }

    localflag = (abs(prob2.`val`- prob1.`val`) > sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Probability modified when arrays swapped")
   }

    localflag = (abs(z2.`val` - z1.`val`) > sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Fisher's z modified when arrays swapped")
   }

    localflag = (abs(z2.`val` - 0.5*log((1+r2.`val`)/(1-r2.`val`)))) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** pearsn: Fisher's z not compatible with correlation coefficient")
   }

    if (globalflag)  println("Failed\n")
    else println("Passed\n");
  

```

## Spearman Rank-Order Correlation Coefficient ##

```

import  com.nr.NRUtil.buildVector
import  com.nr.stat.Stattests.spear
import  java.lang.Math.abs

import org.netlib.util.doubleW


    var  d1 = new doubleW(0)
    var  zd1 = new doubleW(0)
    var probd1 = new doubleW(0)
    var  rs1 = new doubleW(0)
    var probrs1 = new doubleW(0)
    var  d2 = new doubleW(0)
    var zd2 = new doubleW(0)
    var probd2 = new doubleW(0)
    var  rs2 = new doubleW(0)
    var probrs2 = new doubleW(0)
    
    var sbeps = 1.e-6
    var  adata = Array(0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0)
    var  bdata = Array(9.0,8.0,7.0,6.0,5.0,4.0,3.0,2.0,1.0,0.0)
    var  cdata = Array(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,0.0) // Note 0.0 at end
    var  edata = Array(1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0)
    var  fdata = Array( 2.0,1.0,2.0,1.0,2.0,1.0,2.0,1.0,2.0,1.0)
    // Expected results for each test case
    var  ae = Array(0.0,-3.0,0.0026998,1.0,0.0)
    var  be = Array(330.0,3.0,0.0026998,-1.0,0.0)
    var  ce = Array(90.0,-1.363636,0.172682,0.454545,0.186905)
    var  ee = Array(250.0,3.0,0.0026998,-1.0,0.0)
    var   a = buildVector(adata)
    var   b = buildVector(bdata)
    var   c = buildVector(cdata)
    var   e  = buildVector(edata)
    var   f =  buildVector(fdata)
    var  localflag = false
    var globalflag = false


    // Test spear
   println("Testing spear")

    // Test 1
    spear(a, a, d1, zd1, probd1, rs1, probrs1)

    localflag = localflag || (d1.`val` != ae(0))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Squared difference of ranks not zero for identical distributions");
    }

    localflag = localflag || (zd1.`val` != ae(1))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected standard deviation (should be -3) for special case")
    }

    localflag = localflag || abs(probd1.`val`-ae(2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected probability for d, given the standard deviation")
    }

    localflag = localflag || (rs1.`val` != ae(3))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Spearman's rank correlation should be 1 for identical distributions")
    }

    localflag = localflag || (probrs1.`val`!= ae(4))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Probrs should be zero for identical distributions")
    }

    // Test 2
    spear(a, b, d1, zd1, probd1, rs1, probrs1)
//    System.out.printf(d1 << " %f\n", zd1 << " %f\n", probd1 << " %f\n", rs1 << " %f\n", probrs1);
    localflag = localflag || (d1.`val` != be(0))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Sum squared difference of ranks should be 2*(81+49+25+9+1)=330");
    }

    localflag = localflag || (zd1.`val` != be(1))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Standard deviation should be (330/55)-3=3")
    }

    localflag = localflag || abs(probd1.`val`-be(2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected probability for d, given the standard deviation")
    }

    localflag = localflag || (rs1.`val` != be(3))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Spearman's rank correlation should be -1 for perfect anticorrelation")
    }

    localflag = localflag || abs(probrs1.`val` - be(4))>1.e-16
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Probrs should be zero for perfect anticorrelation")
    }

    // Test 3
    spear(b, a, d2, zd2, probd2, rs2, probrs2)
//    System.out.printf(d2 << " %f\n", zd2 << " %f\n", probd2 << " %f\n", rs2 << " %f\n", probrs2);
    localflag = localflag || (d1.`val` != d2.`val`) || (zd1.`val` != zd2.`val`) || 
        (probd1.`val` != probd2.`val`) || (rs1.`val` != rs2.`val`) || (probrs1.`val` != probrs2.`val`)
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** spear: Results changed when two arrays were swapped (case 1)")
    }

    // Test 4
    spear(a, c, d1, zd1, probd1, rs1, probrs1)

    localflag = localflag || (d1.`val` != ce(0))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Sum squared difference of ranks should be 9*(1^2)+1*(9^2)=90");
    }

    localflag = localflag || abs(zd1.`val`-ce(1)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Standard deviation should be (90/55)-3=-1.363636")
      }

    localflag = localflag || abs(probd1.`val`-ce(2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected probability for d, given the standard deviation")
    }

    localflag = localflag || abs(rs1.`val`-ce(3)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Spearman's rank correlation should be 1-6*90/990=0.454545")
    }

    localflag = localflag || abs(probrs1.`val`-ce(4)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected probability for rs, given the rank correlation")
    }

    // Test 5
    spear(c, a, d2, zd2, probd2, rs2, probrs2)
//    System.out.printf(d2 << " %f\n", zd2 << " %f\n", probd2 << " %f\n", rs2 << " %f\n", probrs2);
    localflag = localflag || (d1.`val` != d2.`val`) || (zd1.`val` != zd2.`val`) ||
        (probd1.`val` != probd2.`val`) || (rs1.`val` != rs2.`val`) || (probrs1.`val` != probrs2.`val`)
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** spear: Results changed when two arrays were swapped (case 2)")
     }

    // Test 6
    spear(e, f, d1, zd1, probd1, rs1, probrs1)
    localflag = localflag || (d1.`val` != ee(0))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Sum squared difference of ranks should be 10*(8-3)^2=250")
    }

    localflag = localflag || (zd1.`val` != ee(1))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Standard deviation should be (250-125)/55/(1-240/990)=3")
    }

    localflag = localflag || abs(probd1.`val`-ee(2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Unexpected probability for d, given the standard deviation")
    }

    localflag = localflag || (rs1.`val` != ee(3))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Spearman's rank correlation should be -1 for perfect anticorrelation (case 2)")
    }

    localflag = localflag || (probrs1.`val` != ee(4))
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Probrs should be zero for perfect anticorrelation (case 2)")
    }

    // Test 7
    spear(f, e, d2, zd2, probd2, rs2, probrs2)
//    System.out.printf(d2 << " %f\n", zd2 << " %f\n", probd2 << " %f\n", rs2 << " %f\n", probrs2);
    localflag = localflag || (d1.`val` != d2.`val`) || (zd1.`val` != zd2.`val`) ||
        (probd1.`val` != probd2.`val`) || (rs1.`val` != rs2.`val`) || (probrs1.`val` != probrs2.`val`)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** spear: Results changed when two arrays were swapped (case 3)")
    }

    if (globalflag)  println("Failed\n")
    else  println("Passed\n");

```


## F-test ##

```


import  com.nr.stat.Moment.avevar
import  com.nr.stat.Stattests.ftest
import  java.lang.Math.abs
import  java.lang.Math.sqrt

import com.nr.ran.Normaldev


var  NVAL = 11
var  NPTS = 10000
var  ave1 = new doubleW(0)
var  var1 = new doubleW(0)
var  f = new doubleW(0)
var  prob = new doubleW(0)
var  f1 = new doubleW(0)
var  f2 = new doubleW(0)

var  prob1 = new doubleW(0)
var  prob2 = new doubleW(0)
    
 var EPS = 0.01
 
 var fingerprint = Array(1.0,0.618852,0.32215,0.139461,0.0498994,
      0.0147196,0.00357951,0.000718669,0.000119432,1.64815e-5,  1.89557e-6)
      
 var  data1 = new Array[Double](NPTS)
 var  data2 = new Array[Double](NPTS)
 
 var  localflag = false
 var  globalflag = false

        // Test ftest
  println("Testing ftest")

    // Generate two gaussian distributions with different variances
    var  ndev=new Normaldev(0.0, 1.0, 17)
    
    for (j<-0  until  NPTS)   data1(j) = ndev.dev()
    figure(1);  plot(data1, Color.GREEN, "Gaussian"); title("First gaussian distribution")
    
    avevar(data1, ave1, var1)
    
    for (j<-0 until  NPTS)  data1(j) -= ave1.`val`
    
    for (j<-0 until  NPTS)  data2(j) = data1(j)
    
    ftest(data1, data2, f, prob)
    
    localflag = (f.`val` != 1.0)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** ftest: ftest on identical distributions does not return f=1.0")
    }

    sbeps = 1.e-10
    localflag = (1.0-prob.`val` > sbeps)
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** ftest: ftest on identical distributions does not return prob=1.0");
    }

    for (j<-0 until  NPTS) data2(j) = (1.0+EPS)*data1(j)
    ftest(data1, data2, f1, prob1)
    ftest(data2, data1, f2, prob2)

    localflag = (f1.`val` != f2.`val`) || (prob1.`val` != prob2.`val`)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** ftest: ftest not symmetrical with respect to data arrays");
    }

    var sbeps1 = 1.0e-14
    var sbeps2 = 1.e-6
    for (i<-0 until  NVAL ) {
      var  factor = sqrt(1.0+i*EPS)
      for (j <- 0 until  NPTS)  data2(j) = factor * data1(j)
      ftest(data1, data2, f, prob)
      localflag = (f.`val`-1.0-i*EPS > sbeps1)
      globalflag = globalflag || localflag
      if (localflag) {
        fail("*** ftest: Variance ratio f is incorrect")
      }

//      System.out.printf(abs(prob-fingerprint[i]));
      localflag = (abs(prob.`val`-fingerprint(i)) > sbeps2)
      globalflag = globalflag || localflag
      if (localflag) {
        fail("*** ftest: Probabilities do not agree with previous fingerprint")
         }
    }

    if (globalflag)  println("Failed\n")
    else  println("Passed\n")



```