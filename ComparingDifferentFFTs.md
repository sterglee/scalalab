# Introduction #

`The following script compares the Numerical Recipes, JTransforms, Apache Common Maths and Oregon DSP FFT implementations. The results indicate that Oregon DSP seems the fastest, while Numerical Recipes FFT performs also very well.`


```

// this script applies and compares different FFT implementations

// construct signal
closeAll
var log2N = 17
var N = 2^^^log2N
var t = linspace(0, 1, N)
var dx = t(1)-t(0)
var SF = 1/dx
var NF = 0.5*SF
 
 var F1  = NF/20

 var x1= sin(F1*t)+0.66*cos(2*F1*t)

// Numerical Recipes FFT

tic
 var (rx, ix, freqs)  = scalaSci.FFT.FFTScala.fft(x1, SF) 
 
 var recons = scalaSci.FFT.FFTScala.ifft(rx, ix)
var tmNR = toc
 figure(1);  subplot(3,1,1); plot(freqs, rx, "fft")
   subplot(3,1, 2); plot(x1, "original");
   subplot(3, 1, 3); plot(recons, "reconstructed")


   // JTransforms FFT
val dfft = new edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D(N )

val data = new Array[Double](2*N)
val datafft = new Array[Double](2*N)
val cpdata = new Array[Double](2*N)
val reconsJT = new Array[Double](2*N)

// copy the signal
for (k<-0 until N)   data(k) = x1(k)
 
  // another copy 
for (k<-0 until 2*N)  cpdata(k) = data(k)

  // perform a real FFT
tic
dfft.realForwardFull(data)  
for (k<-0 until 2*N)
  datafft(k) = data(k)

// perform an inverse FFT
dfft.complexInverse(data, true)

var rsig = new Array[Double](N)
 for (k<-0 until N) rsig(k) = data(2*k)
 var tmJTransforms = toc
 
     figure(2); subplot(3,1,1); plot(x1, "original JTransforms");
   subplot(3, 1, 2); plot(rsig, "reconstructed");
   subplot(3, 1, 3); plot(datafft, "FFT")

  // Apache Commons FFT
import scalaSci.FFT.ApacheFFT
import scalaSci.FFT.ApacheFFT._

def getReal(x: org.apache.commons.math3.complex.Complex) = {x.getReal}
tic
var y = fft(x1)
var arecons = ifft(y)
var tmApache = toc
 figure(3); subplot(3,1,1); plot(x1, "original Apache Commons");
   subplot(3, 1, 2); plot(arecons map getReal, "reconstructed");
   
   subplot(3,1, 3); plot(y map getReal)
   


 // Oregon DSP FFT
  var  Xfm = new DSP.fft.RDFT( log2N )
  var  osig = new Array[Double](N)
  var  ores = new Array[Double](N)
  tic
  Xfm.evaluate(x1, osig)
  Xfm.evaluateInverse(osig, ores)
  var tmODSP = toc
  figure(4);  subplot(3,1,1);   plot(x1, "Oregon DSP");
                  subplot(3,1, 2);  plot(osig, "FFT");
                  subplot(3,1, 3);  plot(ores, "Reconstructed");


                  
                  println("time NR = "+tmNR +", time JTransforms = "+tmJTransforms + ",  timeApache = "+tmApache+", time Oregon DSP = "+tmODSP)


```