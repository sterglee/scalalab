# Introduction #

`Wavelet Theory has been emerged in recent years and has found a lot of applications. It still remains a hot research topic. `

`In ScalaLab we can experiment easily with Wavelets by using either the ` **`wavelet package of the jSci library`** `or the `**`jWave`** `toolbox which is available for installation. `

`The installation of the toolbox is very easy, we go through the ` **`ScalaSci Toolboxes`** ` tab of ScalaLab, we specify the ` **`jWave.jar`** `file and then we create a new Scala interpreter that will have that toolbox at its classpath. `

`Here we present some examples of using these libraries.`


# Example 1 - Daubechies wavelet with the jWave toolbox #

`After downloading and installing the Wavelet toolbox, jWave.jar, you can execute the following example code:`

```


// import the relevant material from the jWave toolbox
import  _root_.math.transform.jwave.handlers.wavelets._
import _root_.math.transform.jwave.handlers._

// create a Daubechies Wavelet
var daubWav = new Daub03

// create a synthetic signal and inject to it some noise
var N = 2 ^^^ 14
var F1 = 23.4; var F2 = 0.45; var F3= 9.8;
var taxis  = linspace(0, 5, N)
var sig = 2.5*sin(F1*taxis)+8.9*cos(F2*taxis)-9.8*sin(F3*taxis)
var rndSig = vrand(N)
var sigAll = (sig+rndSig).getv


// create a FWT object
var fwtObj = new FastWaveletTransform(daubWav)

// perform a Fast Wavelet Transform with the Daubechies Wavelet
var transfSig = fwtObj.forwardWavelet(sigAll)

// plot the results
figure(1); subplot(2,1,1); plot(sigAll); title("A Signal with Noise");
subplot(2,1,2); plot(transfSig); title("Wavelet Transformed")
```

# `Example 2 - Continuous Wavelet Transform Example` #

`The following example demonstrates performing a Continuous Wavelet Transform (CWT). The CWT class is included with the standard ScalaLab libraries. The toolbox jWave should not be installed in order to run the following code, since there are name collisions.`

```


var fs = 600; // 2050; 
var dt = 1.0/fs 
var t =  inc(1, dt, 2)

var PI = 3.1415926
var PI2 = 2*PI
var y = sin(PI2*10*t)+4*cos(PI2*4*t)
var rv = rand(y.length)  // a random vector of the same size as y
var summedVec = 4*y+rv
figure(1); plot(summedVec); title("Signal and Noise")

var fstart = 1  // frequency to start
var fmax = fs/2.0
var maxNf = 20

var  linlog = "log"
var stepfac=16
var df0=3


var ycwt = new _root_.wavelets.CWT(y.getv,  fs, fmax, maxNf, linlog, stepfac, df0)
var ed = ycwt.ed()  // energy density coefficients as a double[][] vector

figure(2); plot(ed); title("Continuous Wavelet Transform")
 


var edm = new Matrix(ed);
//subsampledEdm = edm.resample(5, 1);  // subsample matrix before displaying it in contour plot
figure(1); 
subplot(2,1,1);
plot(y); title("signal");
subplot(2,1,2);
plot2d_scalogram(ed, "scalogram");

```

# Example for 1-D DFT #

```


import  _root_.math.transform.jwave.Transform
import  _root_.math.transform.jwave.handlers.DiscreteFourierTransform

var  t = new Transform( new DiscreteFourierTransform())

var arrTime = Array(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)


var arrFreq = t.forward(arrTime)   // 1-D DFT forward

var arrReco = t.reverse(arrFreq)
```

# Example for 1-D FWT, 2-D FWT #
```

import  _root_.math.transform.jwave.Transform

import  _root_.math.transform.jwave.handlers.wavelets._
import _root_.math.transform.jwave.handlers.FastWaveletTransform

var  t = new Transform( new FastWaveletTransform(new Haar02()))

var arrTime = Array(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)  


var arrHilb = t.forward(arrTime)    // 1-D FWT Haar forward

var arrReco = t.reverse(arrHilb)  // 1-D FWT Haar reverse



 var  matTime = Array(Array( 1., 1., 1., 1.), Array( 1., 1., 1., 1. ), Array(1., 1., 1., 1. ), Array( 1., 1., 1., 1. ))

 var  matHilb = t.forward( matTime ); // 2-D FWT Haar forward

 var  matReco = t.reverse( matHilb ); // 2-D FWT Haar reverse

```


# Example for Wavelet Packet Transform #

```

import  _root_.math.transform.jwave.Transform

import  _root_.math.transform.jwave.handlers.wavelets._
import _root_.math.transform.jwave.handlers.WaveletPacketTransform

var  t = new Transform( new WaveletPacketTransform(new Haar02()))

var arrTime = Array(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)  


var arrHilb = t.forward(arrTime)    // 1-D FWT Haar forward

var arrReco = t.reverse(arrHilb)  // 1-D FWT Haar reverse



 var  matTime = Array(Array( 1., 1., 1., 1.), Array( 1., 1., 1., 1. ), Array(1., 1., 1., 1. ), Array( 1., 1., 1., 1. ))

 var  matHilb = t.forward( matTime ); // 2-D WPT Haar forward

 var  matReco = t.reverse( matHilb ); // 2-D WPT Haar reverse

```