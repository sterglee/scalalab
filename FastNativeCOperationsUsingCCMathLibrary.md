# Introduction #

`The CCMath library is a fast C library. Although Java code is generally very fast, some routines can run faster with native C code, if we perform an intense optimization of  that code. `

`We present some examples of optimized routines`

**`The interface of CCMath is in development and works from Oct 12 versions and newer`**


## Square Linear Systems Solution ##

`Here,optimized C performs faster from Java,  we took 1.96 sec for C, and 2.79 sec for Java on Linux (g++ -Ofast), while on Windows 8, times are: 2.77sec for C and 2.86 sec for Java (cl -Ox)`

```


var N=2000
var A = rand(N, N)
var b = Rand(N)

// solve with C routine
tic
var x = ccsolv(A, b)
var tmc= toc


// solve with Java routine
tic
var xj = solve(A,b)
var tmj = toc

```

`And another example, for which we obtain 0.0472sec for C compiled with g++ and 0.144 secs for Java for Linux, and 0.07 sec for Windows compiled with cl with both the -Ox and -O2 option:`

```


var N = 600
var A = rand(N, N)

var b = Rand(N)

tic
var xc = A.ccsolv(b)
var tm= toc


tic
var xcj = A.solve(b)
var tmj= toc
```



## Evaluation of autocorrelation ##

`The following script illustrates evaluation of the autocorrelation using a CCMath based C routine`

```


var t = 0::0.01::1500
var x = sin(0.34*t)+0.45*cos(0.445*t)


var NLags = 500  // lags over which to evaluate autocorrelation
var ax = ccautcor(x, NLags)

figure(1);
subplot(2,1,1); plot(x, "time series for autocorrelation evaluation")
subplot(2,1,2);
plot(ax, "autocorelation demo")
xlabel("lag")

```

## SVD decomposition ##


```

var ccObj = scalaExec.Interpreter.NativeLibsObj.ccObj

 
 var M = 300
 var N = 300
 var d = Zeros(N)
 var MN=M*N
 var a = Rand(MN)
 var aa = a.clone
 var u = Zeros(M*M)
 var v = Zeros(N*N)
  tic
  ccObj.svduv(d, a, u, M, v, N)
  var tm = toc

```

`and another example: `

```
 
var A = $$(4.5, 6.7, -1.2, null, -1.2, 0.3, -1.2, null,
             -0.2, 0.3, -0.4)
             
var (uA, sA, vA) = svd(A)

// check orthogonality
var uAortho = uA*(uA~)
var vAortho = vA*(vA~)

// check validity of decomposition
var shouldBeZero = uA*diag(sA)*(vA~) - A

```

`Using a higher level interface`


```
var N = 1100; var M = 1000

var xx = rand(N,M)

// use C svd
tic
var csvd = ccsvd(xx)
var tmc = toc

var U = csvd._1
var W = diag(csvd._2)
var V = csvd._3

var isIdentity = U*(U~)  // check orthogonality, should be the identity matrix

// use Java svd
tic
var jsvd = svd(xx)
var tmj=toc
```

`At the case above I obtained, 36.51 secs for the optimized C case, and 60.73 secs for the Java version, thus there is some benefit.`


## Inverse Operation ##
`Also, the matrix inverse operation, runs slightly faster in optimized C compared to Java. Without full optimization of C code, Java outperforms C.`

```
// test the C inverse operation
var N = 1200

// test for RichDouble2DArrays
var x = rand(N,N)
var xx = x.clone

tic
var y = ccinv(x)
var tmc = toc



tic
var yj = inv(xx)
var tmj = toc



// test for EJML matrices

var ex = scalaSci.EJML.StaticMathsEJML.rand0(N,N)   // create an EJML random matrix
var cex = ex.clone

tic
var yx = scalaSci.EJML.StaticMathsEJML.ccinv(ex)
var tmcEJML = toc

```


## Solving symmetric positive definite linear systems ##

`Here, the C routine performs significantly faster than Java, but it is specialized for symmetric positive definite square matrices, while for the Java case, the general solver is used. `

```

// illustrates the ccsolvps routine of CCMath
// used for solving symmetric positive definte linear systems
var N=500
var A = rand(N, N)  // a random matrix
var Aspd = A * (A~)   // make it symmetric positive definite

var b = Ones(N)

// use symmetric positive definitive CCMath solver
tic
var x = ccsolvps(Aspd, b)
var tmSPD = toc


// use general solver
tic
var xg = solve(Aspd, b)
var tmg = toc

var shouldBeTheSame = xg-x   // the solutions should agree

var shouldBeZero = Aspd*x - b   // x should be indeed a solution
 


```

## FFT ##

`FFT in ScalaLab perfoms faster with the Java based implementation. However, the Java and C implementations compared are different, therefore we cannot conclude from these results about the quality of code. `

```

var  x = 0::0.001::500
var N = x.length

var y = 67*cos(0.123*x)+3.4*sin(0.0345*x)

plot(x, y)

var xx = scalaExec.Interpreter.NativeLibsObj.ccObj

var reCoeff = new Array[Double](N)
var imCoeff = new Array[Double](N)

tic
xx.ccfft(y, reCoeff, imCoeff, N)
var tmc = toc

tic
var (jfftR, jfftI) = fft(y)
var tmj = toc

figure(1)
subplot(2,1,1); plot(reCoeff(0, 100),"C FFT")
subplot(2,1,2); plot(jfftR(0, 100),"Java FFT")


```

`and another FFT example`

```

var  x = 0::0.001::500
var N = x.length

// illustrates FFT using both CCMath and Java

var y = cos(0.23*x)+2.3*sin(0.78*x)+0.9*cos(0.45+5.6*x)

plot(y)

var ccObj = scalaExec.Interpreter.NativeLibsObj.ccObj  // get the native object for CCMath based operations

// prepare space for the real and imaginary FFT coefficients
var reCoeff = new Array[Double](N)
var imCoeff = new Array[Double](N)

tic
ccObj.ccfft(y, reCoeff, imCoeff, N)
var tmc = toc

tic
var (jfftR, jfftI) = fft(y)
var tmj = toc

new Vec(reCoeff)(0::10)
figure(1)
subplot(2,1,1); plot(new Vec(reCoeff)(0::1000),"C FFT")
subplot(2,1,2); plot(new Vec(jfftR)(0::1000),"Java FFT")

```