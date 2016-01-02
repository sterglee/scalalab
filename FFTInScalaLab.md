# Introduction #

`FFT transforms are fundamental in signal processing. We redesign the FFT routines of ScalaSci and the material in this page applies to the versions after 29 April 2012. `

`FFT routines provide many standard Java libraries of ScalaLab, as the Apache Commons, Colt, NUMAL, JSci. We have chosen as the default implementation the one provided with the Apache Commons Library. Of course any other Java library can be used with its native Java interface.`

`An example illustrates an FFT transformation in ScalaLab.`


# Example #

`The following example constructs a synthetic signal, then performs an FFT, and an inverse FFT to reconstruct the signal. Finally, it plots the signals.`

```

import scalaSci.FFT.ApacheFFT
import scalaSci.FFT.ApacheFFT._

var t = inc(0, 0.01, 100)
var x = sin(0.56*t)+ cos(2.3*t)
plot(t,x)

var y = fft(x)

var r = ifft(y)

var rr = new Vec(getReParts(r))
var im = new Vec(getImParts(r))

figure(2); 
var N = length(t)-1
subplot(3,1,1); plot(x(0,N), "Original Signal"); xlabel("x"); 
subplot(3,1,2); plot(rr(0,N), "reconstructed real part"); xlabel("rr");  
subplot(3,1,3); plot(im(0, N), "imaginary real part"); xlabel("im");  

```