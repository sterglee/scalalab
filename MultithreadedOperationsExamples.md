# Introduction #

`Multithreading in ScalaLab is exploited to speed up basic operations, as matrix multiplication. Java multithreading exploits very well multicore processing. We tested also a Pthreads based optimized C multithreaded matrix multiplication, and we surprised by the fact that Java multithreaded multiplication works slightly faster, both on Linux and Windows platforms!`

`Therefore, we will gradually introduce more multithreaded operations in future ScalaLab versions.`

`The speedup of multithreaded routines, is very good, it approaches the number of processor cores, i.e. for an i5 with four cores, is near 3.6!`

`Curretly, there are a lot of multithreaded operations, for example, ` **`psin`** `(i.e. parallel sin),` **`pcos, ptan, pcosh, psinh, ptanh, plog`** `etc. `

`Also, for the object based calls we provide and ` **`in-place`** `versions, i.e. the operation takes place on the receiver without creating a copy of its memory space. Obviously, the mutable in-place operations are faster and less memory consuming. For example`

```
var x = rand(1000, 2000)
var xisin  = x.psin  // parallel sin
x.pisin  // parallel in-place sin

```


# Examples #


## Parallel mapping in place a function ##

```


// parallel map in place example
var N=3000
def f(x: Double) = 10*x   // the function to map

var x = fill(N, N, 2)   // fill the array with some value

x pmapi f   // map the function in-place

x.max.max ==x.min.min  // should hold, since the resulted array must be also constant

```

## Parallel mapping a function returning the result array ##

```

// parallel map in place example
var N=3000
def f(x: Double) = 10*x   // the function to map

var x = fill(N, N, 2)   // fill the array with some value

var y = x pmap f   // map the function in-place

y.max.max ==y.min.min  // should hold, since the resulted array must be also constant

```

## `Multithreaded matrix FFT` ##

`Here, we perform FFT of a matrix, accessed by rows, i.e. one separate FFT for each row. We demonstrate both the serial and the multithreaded FFT routine. `

```


// demonstrates multithreaded FFT of a random matrix
var N = 2^^^12
var x = Rand(N,N)

// serial FFT of all the rows of the matrix
tic
var fxs = fft(x)
var tms = toc

// mutlithreaded FFT 
tic
var fxp = fftp(x)
var tmp = toc // time for multithreaded FFT

var fxRe = fx._1  // real part
var fxIm = fx._2  // imaginary part


```

## `Multithreaded operations ` ##

`Using the multithreaded versions of the basic matrix operations we can gain significant speed improvements on mulitcore machines. We illustrate an example.`


```

var M=200
var xRDA = rand(M,M)  // a RichDouble2DArray
var xRDAsin = psin(xRDA)  // parallel sine

var xMatrix = rand0(M, M) // a Matrix
var xMatrixSin = psin(xMatrix) // parallel sine

var N=4000
var x = rand0(N,N)

// parallel sin
tic
var xx = x.psin
var tmsinp = toc


// parallel cos
tic
var xxy = x.pcos
var tmcosp = toc


// serial sin
tic
xx = x.sin
var tmsin = toc


// serial cos
tic
xxy = x.cos
var tmcos = toc


// parallel in-place sin
tic
x.pisin
var tmsinpi = toc


// parallel in-place cos
tic
x.picos
var tmcospi = toc


// serial in-place sin
tic
x.sini
var tmsini = toc


// serial cos
tic
 x.cosi
var tmcosi = toc



```