# Introduction #

`ScalaLab can use effective implementations of FFT from Java libraries. The following code tests: 1. an Oregon DSP based implementation, 2. the Apache Common Maths, 3. the Numerical Recipes implementation.`

`We note that the Apache Commons is not for complex vectors. Also, another important observation is that by using float arrays instead of doubles we have not obtained any speedup, therefore in today's JVMs the more accurate double type does not seem to impose any overhead. Thus, we kept only the version of our routines for double arrays. `

`Here is the code to test the FFTs: `

```

// this will run with ScalaLab versions from 17-July and after

closeAll
var  N  = 16384
 var log2N = 14

var  xr =  new Array[Double](N)
 var  xi =  new Array[Double](N)
 var  Xr =  new Array[Double](N)
 var  Xi =  new Array[Double](N)

 for (k<-0 until N)
  {
  	xr(k) = rand
     xi(k) = rand
  }

 var Xfm = new DSP.fft.CDFT( log2N )
 var Xfmr = new DSP.fft.RDFT(log2N)
 
    // evaluate transform of data
    
var Nt = 100 // how many times to evaluate the FFTs
    tic
  for (k<-0  until Nt)
    Xfm.evaluate( xr, xi, Xr, Xi )
   
    var tmOregon = toc

// FFT with real only data
    tic
  for (k<-0  until Nt)
    Xfmr.evaluate( xr, Xr  )
   var tmOregonR = toc

   figure(1); plot(Xr); title("Oregon DSP based FFT took "+tmOregonR)   // plot the resulting Fourier transform
    

// test the Apache Commons FFT
   var fftXr2 = scalaSci.FFT.ApacheFFT.fft(xr )
   tic
    for (k<-0  until Nt)
     fftXr2 = scalaSci.FFT.ApacheFFT.fft(xr )
    var tmApache = toc
// uncomment the following only for ScalaLabLight
//    figure(2); plotApacheCommonsComplexArray(fftXr2)
//    title("Apache Commons FFT took "+tmApache)

    

// test the Numerical Recipes FFT
 var (nrR, nrIm) = scalaSci.FFT.FFTScala.fft(xr)
   tic
   for (k<-0  until Nt) {
     var (nrR, nrIm) = scalaSci.FFT.FFTScala.fft(xr)
   }
  var tmNR = toc

  figure(3); plot(nrR)
  title("Numerical Recipes based FFT took "+tmNR)

  
  
```