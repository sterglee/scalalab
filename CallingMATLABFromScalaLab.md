# Introduction #

`The power of MATLAB can be combined with ScalaLab with the ` **`matlabcontrol`** ` API. The project's page is `


http://code.google.com/p/matlabcontrol


`To utilize MATLAB commands from ScalaLab, it is very easy, the ` **`matlabcontrol-4.1.0.jar`** `is by default at the ScalaLab's lib folder, and thus not any installation is required. Of course, you must have installed MATLAB on your system. `

`We present some examples of using MATLAB from ScalaLab. `

## Demonstration of accessing MATLAB arrays ##

```


// this script requires to have installed MATLAB on your system 

import matlabcontrol._
import matlabcontrol.extensions._

// create a proxy, which we will use to control MATLAB
var factory = new MatlabProxyFactory

var proxy = factory.getProxy
  
// create a 4X3X2 array filled with random values

proxy.eval("array=randn(4,3,2)")

// print a value of the array into the MATLAB Command Window 
proxy.eval("disp(['entry: ' num2str(array(3,2,1))])")

// get the array from  MATLAB
var processor = new  MatlabTypeConverter(proxy)
var array = processor.getNumericArray("array")

// print out the same entry, using Java's 0-based indexing
println( array.getRealValue(2,1,0))

// convert to a Java array and print the same value again
var javaArray = array.getRealArray3D
println("entry: "+javaArray(2)(1)(0) )
proxy.disconnect

```

## SVD using MATLAB ##

`Since Scala is statically typed, the compiler does not permit to use variables that are defined from MATLAB computations. For that reason, we introduce the ` **`@MATLAB`** ` annotation in ScalaLab.`

`MATLAB calls should be bracketed with that annotations. ScalaLab seperates the script in parts, therefore the parts that depend on variables that MATLAB computes are executed after they are evaluated by MATLAB and retrieved from the Scala interpreter. `

`The following SVD computation script, also demonstrates how to use the @MATLAB annotation.`

```
initMatlabConnection  // init connection to MATLAB

var N=500
var x = Rand(N,N)  // a Java random 2D double array 

tic
var svdc = "[u, s, v] = svd(x);"   //  the MATLAB script for SVD computation

var inParams = Array("x")
var outParams = Array("u", "s", "v")

// call MATLAB to evaluate the svd
// the first parameter of meval(), i.e. svdc, is the MATLAB script to evaluate
// the second parameter of meval() is  the array of input parameters from ScalaLab to MATLAB
// the third parameter of meval() is the array of output parameters from MATLAB to ScalaLab
@MATLAB
meval( svdc, inParams, outParams)
var tmMatlab = toc  // 2.41 sec 
@MATLAB

var shouldBeZero = u*s*(v~)-x  // from the definition of SVD


// perform SVD with Java
tic
var svdJ = svd(x)
var tmJava = toc()  // 221.78 sec


```

`The script above evaluates the SVD with Java for comparison. SVD turns out to be much faster in MATLAB, but this is because MATLAB uses a much faster algorithm than the Java implementation does. `



## `FFT of a matrix with MATLAB, ScalaLab serial, and ScalaLab multithreaded` ##

`The script below demonstrates that ScalaLab FFT based on a Java implementation adapted from the Numerical Recipes 3d edition book, is remarkable faster than MATLAB's!`

`Specifically, when we apply the FFT routine only one time, the speed is slightly slower than MATLAB's FFT spped. However, when we use the routine to FFT all the rows of a large 2-D matrix, Java outperforms significantly MATLAB!`

`Perhaps an explanation is that JITing the code, after the first running of the FFT routine, has as a consequence the rest FFTs to run significantly faster (is that explanation correct?)`

`ScalaLab has also a multithreaded implementation of matrix FFT, that simply shares the job of FFTing rows, to seperate threads (is the ` **`fftp`** `routine demonstrated below). The speedup of that routine is quite reasonable, e.g. for i5 with 4 cores, the speedup is about 3 times faster. `

`We present below the example. MATLAB performs with  17.134031811 sec, serial Java/Scala with 0.075558609 secs, and multithreaded Java/Scala with 0.041835529 secs.`

`However, the slow time of MATLAB is due to communication with the external JVM on which ScalaLab runs. When the code is executed directly in MATLAB, the delay is about 0.021 secs, i.e. about two times faster than multithreaded ScalaLab, something reasonable, since MATLAB's internal FFT is highly optimized.`





```



// demonstrates multithreaded FFT of a random matrix
var N = 2^^^11
var SF=0.1

var x = Rand(N,N)
var row=0
while (row < N) {
  x(row) = x(row)+sin(SF*(row+10)*linspace(0,1,N).getv)
  row += 1
  }
  
  
// serial FFT of all the rows of the matrix
tic
var fxs = fft(x)
var tms = toc

// mutlithreaded FFT 
tic
var fxp = fftp(x)
var tmp = toc // time for multithreaded FFT

var refxp = fxp._1
subplot(2,1,1); plot(refxp(0)); subplot(2,1, 2); plot(refxp(N-1))

initMatlabConnection

tic
var fftm = """
ym = fft(x);
"""

@MATLAB
meval(fftm, Array("x"), Array("ym"))
@MATLAB
var tmMatlab = toc

whos
var reym = ym.re  // real part computed by MATLAB
var imym = ym.im  // imaginary part computed by MATLAB


```