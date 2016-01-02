# Introduction #

`The Numerical Recipes is a classic book, an excellent reference for any researcher and engineer in the field of numerical computation. ScalaLab present a Matlab-like interface to many routines from Numerical Recipes. Numerical Recipes routines are efficient and perhaps even more important they have extensive description in the book. We present some example code based on NR.`

## FFT and inverse FFT of a test signal ##

```

closeAll  // close any open figure

var N = 2^^^14  // the signal length
var t = linspace(0, 1, N)
var F1 = 78.7; var F2 = 34.6
var x = sin(F1*t)+cos(t*F2)
var SF = 1.0/(t(1)-t(0))  // the sampling frequency

var (realffts, imffts, freqs) = fft(x, SF)
var rx = ifft(realffts, imffts)

figure(1)
subplot(3,1,1); plot(x); title("Original Signal")
subplot(3,1,2); plot(freqs, realffts); title("Real Coefficients of FFT")
subplot(3,1,3); plot(rx); title("Reconstructed by inverse FFT")

```

## Power Spectrum with Hann window ##

```


closeAll()
 // M is the segment length
var M = 128
  // perform power spectral estimation using overlapping data segments
  // The user sends non-overlapping segments of length M, which are processed
  // in pairs of 2M, with overlap
var  spobj = new com.nr.sp.Spectolap(M)

val N = 2*M    // length of the long signal is not a power of 2
var bw = new com.nr.sp.Hann(N)
bw

var dN = 10*N
val t = linspace(0,10,dN)
val F1 = 4.5; val F2 = 15.8*F1; val F3 = 21.7   // take some frequencies
val A1 = 0.45; val A2 = 1.5; val A3 = 2   // take some amplitudes

// a synthetic long signal
var longSignal = A1*sin(F1*t)+A2*cos(F2*t)+A3*sin(F3*t)

 // perform PSD estimation of the long signal
 // addlongdata() initializes processing of the long signal as
 // overlapping segments of length 2*M
spobj.addlongdata(longSignal, bw)

   
val cspectrum = spobj.spectrum()  // compute spectrum
val cfreqs = spobj.frequencies()  // the frequencies 

figure(1); subplot(2,1,1); 
plot(t, longSignal, "r", 1); title("signal")
subplot(2,1,2);
plot(cfreqs, cspectrum); title("power spectrum")         

```


## Power Spectrum with Bartlett Window ##

```


closeAll
var  dx =  0.001
var t = inc(0, dx, 2*PI)   // like 0:dx:2*PI
var fc = (0.5/dx)*2*PI  // Nyquist frequency

// we pick M, a power of 2, such that estimates at M+1 frequencies between 0 and fc (inclusive) are enough
var M = 2 ^^^8

// create a synthetic signal
var F1 = fc/2; var F2 = fc/8
var x = 15*sin(F1*t)+12.6*cos(t*F2)+120*vrand(t.size)
x = x-mean(x)  // remove DC component

import com.nr.sp._
// compute the power spectrum using overlapping data segments
var  mySpec =  new Spectolap(M)
var bartlett = new BartlettWin
mySpec.addlongdata(x, bartlett)
var psd = mySpec.spectrum()
var freq = mySpec.frequencies()

figure(1); 
subplot(2,1,1); plot(t, x, Color.GREEN, "Signal"); 
subplot(2,1,2); plot(freq, psd, Color.RED, "Power Spectral Density")



```


# One-Dimensional FFT example #

```

   
var  NN=32
var NN2=NN+NN
var   data = new Array[Double](NN2)
var  dcmp = new Array[Double](NN2)

     println ("TEST 1")
     println ("h(t) = real-valued even function")
     println ("h(n) = h(N-n) and real")
          // construct the function
      var i=0
      while (i< NN2-1) {
          data(i)=1.0/((( (i-NN)*(i-NN) )/NN)+1.0)
          data(i+1)=0.0
          i += 2
        }

      var  cpdata = data.clone  // copy data since FFT is done in-place

                var isign=1
                NR.NRFFT.four1(cpdata, isign);
                figure(1); subplot(2,1,1);     plot(data, Color.BLUE,  "Real-Valued even function");
                subplot(2,1,2); plot(cpdata, Color.RED, "H(n) = H(N-n) and Real")

// print the smallest FFT coefficients up to 5
     NR.NRFFT.printft(cpdata, 5)


        println ("TEST 2")
        println ("h(t) = imaginary-valued even-function")
        println ("h(n) = h(N-n) and imaginary")
        i = 0
       while ( i<NN2-1) {
          data(i+1)=1.0/((( (i-NN)*(i-NN) )/NN)+1.0)
          data(i)=0.0
          i += 2
        }

                cpdata = data.clone  // copy data since FFT is done in-place
                

                isign=1
                NR.NRFFT.four1(cpdata, isign);
                figure(2); subplot(2,1,1);     plot(data, Color.BLUE,  "Imaginary-Valued even function");
                subplot(2,1,2); plot(cpdata, Color.RED, "H(n) = H(N-n) ad Imaginary")


 
        // testing simplified interface to four1
 
 var realparts = new Array[Double](NN)
 var imparts  = new Array[Double](NN)

 NR.NRFFT.four1S(data, realparts, imparts)
 


```



## Another one dimensional FFT example ##

```

import com.nr.test.NRTestUtil._
import java.lang.Math._
import com.nr.fft.FFT._

 var N=256
 var sbeps=1.0e-14
 var pi=acos(-1.0)
    
   var  data1 = new Array[Double](N)
   var  data2 = new Array[Double](N)
   var localflag=false
   var globalflag=false

    println("Testing realft")

    // Round-trip test for random numbers
    ranvec(data1)
    data2 = data1
    for (i<-0 until N) data2(i) *= (2.0/N)
    figure(1); subplot(3,1,1); plot(data2, "Data before FFT")
    realft(data2,1)
    subplot(3,1,2); plot(data2, "FFT of data")
    realft(data2,-1)
    subplot(3,1,3); plot(data2, "Inverse FFT")
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** realft: Round-trip test for random real values failed")
      
    }

    // Test delta-function in to sine-wave out, forward transform
    for (i<-0  until N) data1(i) = 0.0
    data1(5) = 1.0
    figure(2); subplot(3,1,1); plot(data1, "delta-function")
    realft(data1, 1)
    subplot(3,1,2); plot(data1, "Computed Real FFT of delta-function")
    data2(0)=1.0
    data2(1)=cos(pi*5)
    for (i <- 1 until (N/2)) {
      data2(2*i)=cos(2.0*pi*5*i/N)
      data2(2*i+1)=sin(2.0*pi*5*i/N)
    }
   subplot(3,1,3); plot(data2, "Expected Real FFT of delta-function")
    
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** realft: Forward transform of a chosen delta function did not give expected result");
      
    }

    if (globalflag) println("Failed\n")
    else println("Passed\n")
  



```

> ## .. and another example ##

```


import  com.nr.NRUtil.buildVector
import  com.nr.fft.FFT.four1
import  com.nr.test.NRTestUtil.maxel   
import  com.nr.test.NRTestUtil.ranvec
import  com.nr.test.NRTestUtil.vecsub
import  java.lang.Math.acos
import  java.lang.Math.cos
import  java.lang.Math.sin

import com.nr.ran.Ran

    var  N=256
    var  sbeps=1.0e-14
    var  pi=acos(-1.0)
    var  data1 = new Array[Double](2*N)
    var  localflag=false
    var  globalflag=false
    

    // Test four1
    println("Testing four1")
    var  myran = new Ran(17)

    // Round-trip test for reals
    for (i<-0 until N) {
      data1(2*i)  = myran.doub()
      data1(2*i+1) = 0.0
    }
    
    var data2=buildVector(data1)
    for (i<-0 until 2*N)  data2(i)  /=  N
    four1(data2,1)
    four1(data2,-1)
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** four1: Round-trip test for random real values failed");
      
    }

    // Round-trip test for imaginaries
    for (i<-0 until N) {
      data1(2*i) = 0.0
      data1(2*i+1) = myran.doub()
    }
    for (i<-0 until  2*N) data2(i) = data1(i)/N
    four1(data2,1)
    four1(data2,-1)
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** four1: Round-trip test for random imaginary values failed");
      
    }

    // Round-trip test for complex numbers
    ranvec(data1)
    for (i<-0 until 2*N) data2(i) = data1(i)/N
    four1(data2,1)
    four1(data2,-1)
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** four1: Round-trip test for random complex values failed");
      
    }

    // Test delta-function in to sine-wave out, forward transform
    for (i<-0 until 2*N) data1(i) = 0.0
    data1(2*5) = 1.0
    four1(data1,1)
    for (i <- 0 until N) {
      data2(2*i) = cos(2.0*pi*5*i/N)
      data2(2*i+1) = sin(2.0*pi*5*i/N)
    }
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** four1: Forward transform of a chosen delta function did not give expected result");
      
    }

    // Test delta-function in to sine-wave out, backward transform
    for ( i <- 0 until  2*N) data1(i) = 0.0
    data1(2*7)=1.0
    four1(data1,-1)
    for (i <- 0 until N) {
      data2(2*i)=cos(2.0*pi*7*i/N)
      data2(2*i+1)= -sin(2.0*pi*7*i/N)

    }
    localflag = localflag || maxel(vecsub(data1,data2)) > sbeps
    globalflag = globalflag || localflag;
    if (localflag) {
      fail("*** four1: Backward transform of a chosen delta function did not give expected result")
      
    }

    if (globalflag) println("Failed\n")
    else println("Passed\n")
  

```

## Power Spectrum Estimation Example ##

```
import  com.nr.NRUtil.SQR
import  java.lang.Math.abs
import  java.lang.Math.acos
import  java.lang.Math.cos
import  java.lang.Math.exp


import com.nr.sp.Spectreg
import com.nr.sp.WindowFun

 class Win extends WindowFun {
    
    def  window(j: Int,  n: Int ) = {
       1.0
    }
  }


var pi = acos(-1.0)

var  M = 128;  var  N = 2*M;  var   K = 64

var   index = new Array[Int](2)
var   data = new Array[Double](N)
var   spec = new Array[Double](M+1)
var   freq = new Array[Double](M+1)

var    globalflag=false
var    localflag = false    

    // Test Spectreg
 println("Testing Spectreg")
 var   window = new Win()
 var  sp=new Spectreg(M)
   
    for (i <- 0 until  K) {
      // Generate a data set
      for (j <- 0 until  N) {
        data(j) = 10.0*exp(-1.0*SQR(j-M)/SQR(N/8))*(cos(2.0*pi*32*j/N)+cos(2.0*pi*64*j/N))
     }

      sp.adddataseg(data,window)    // Note: text should clarify this usage
    }
    
    
    spec=sp.spectrum()
    freq=sp.frequencies()

  var  maxs = -1.0
  var  j = 0
  for (i <- 0 until  M+1) {
      if (spec(i) > maxs) {
        maxs = spec(i)
        index(j) = i
      }
      else {
          j = 1
          maxs = spec(i)
          }
    }

    // Check center frequencies
    var  sbeps = 1.0e-12
    for (i <- 0 until 2) {
      localflag = abs(freq(index(i))-0.125*(i+1)) > sbeps
      globalflag = globalflag || localflag
      if (localflag) {
        println("*** Spectreg: Frequency component "+ i +" was incorrectly identified")
      }
    }

    // Check symmetry
    sbeps = 1.0e-8
    for (i <- 0 until 2) {
      for (j <- 0 until 5) {
//        System.out.printf(setprecision(15) << abs(spec[index[i]+j]-spec[index[i]-j]));
        localflag = abs(spec(index(i)+j)-spec(index(i)-j)) > sbeps
        globalflag = globalflag || localflag; 
        if (localflag) {
          println("*** Spectreg: Spectral peak " + i + " is not symmetric");
       }
      }
    }

    // Compare two peaks
    sbeps=1.0e-8
    for (i <- 0 until 5) {
//      System.out.printf(setprecision(15) << abs(spec[index[0]+i]-spec[index[1]+i]));
      localflag = abs(spec(index(0)+i)-spec(index(1)+i)) > sbeps
      globalflag = globalflag || localflag; 
      if (localflag) {
        println("*** Spectreg: Two spectral peaks are not same size and shape");
      }
    }

    if (globalflag)  println("Failed\n")
    else println("Passed\n")

  figure(1)
  subplot(2,1,1);   plot(data, "Analyzed data");
  subplot(2,1,2);   plot(freq, spec, "Frequency spectrum"); xlabel("Freq")
  

```