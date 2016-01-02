# Introduction #

`JTransforms of Piotr Wendykier, is a powerful Java library for Fourier transforms, integrated within the source of ScalaLab. We present here some examples of its use.`

# Real FFT Example #

```


var N  =  2 ^^ 8

val dfft = new edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D(N )

val data = new Array[Double](N)
val cpdata = new Array[Double](N)
val recons = new Array[Double](2*N)

// create a signal
for (k<-0 until N)
  data(k) = 8.9*sin(0.0023*k)+18.9*cos(0.0223*k)+0.02*Math.random
  
  // copy it
for (k<-0 until N)
  cpdata(k) = data(k)
  
  // perform a real FFT
dfft.realForward(data)  
for (k<-0 until N)
  recons(k) = data(k)


// perform an inverse FFT
dfft.realInverseFull(recons, true)
  
  
  figure(1); subplot(3,1,1); plot(cpdata, "original");
  
var   validrecons = new Array[Double](N)
for (k<-0 until N)
  validrecons(k) = recons(k)
  
  subplot(3,1,2); plot(data, "FFT")
  
  subplot(3,1,3); plot(validrecons, "reconstructed")
```