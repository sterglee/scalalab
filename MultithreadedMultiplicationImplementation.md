# Multithreaded matrix multiplication implementation #

`Matrix multiplication is a very common operation with significant computational requirements for large matrices. Therefore, we try to improve the execution time of this operation in ScalaLab, using Java multithreading. `

`Specifically, we use multithreading with ` _`RichDouble2DArrays.`_ `For the library dependent matrix classes, up to now, we have the serial multiplication.`

`The multithreaded matrix multiplication is still experimental. There is a speedup, but perhaps a significant improvement can be achieved with better design. The speedup is very dependent on matrix sizes, therefore cache locality issues are very important. `

`The current multithreaded matrix multiplication code is listed below: `

```


  
  // Array[Array[Double]] * Array[Array[Double]]
 override final def * (that: RichDouble2DArray): RichDouble2DArray =  {
   var   rN = v.length;   var rM = this.v(0).length;
   var  sN = that.v.length;  var sM = that.v(0).length
  
    // transpose first matrix that. This operation is very important in order to exploit cache locality
var thatTrans = Array.ofDim[Double](sM, sN)
var r=0; var c = 0
while (r<sN) {
  c=0
  while (c<sM) {
    thatTrans(c)(r) = that(r, c)
    c += 1
  }
  r += 1
}

  var  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix
  var nthreads = ConcurrencyUtils.getNumberOfThreads
  nthreads = Math.min(nthreads, rN)
  
  var futures = new Array[Future[_]](nthreads)
  var rowsPerThread = (sM / nthreads).toInt  // how many rows the thread processes

  var threadId = 0  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    var firstRow = threadId * rowsPerThread
    var lastRow = if (threadId == nthreads-1) sM else firstRow+rowsPerThread
  
 futures(threadId) = ConcurrencyUtils.submit(new Runnable() {
    def run = {
      var a=firstRow   // the first row of the matrix that this thread processes
      while (a<lastRow) {  // the last row of the matrix that this thread processes
             var b = 0
             while (b < rN )  {
                 var s = 0.0
                 var c = 0
                 while (c < rM) {
                    s += v(b)(c) * thatTrans(a)(c)
                    c += 1
                   }
                vr(b)(a)   = s
                b += 1
             }
             a += 1
      }
   }
 })
        threadId += 1
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures)

        
    
  return new RichDouble2DArray (vr)
  }

```

`The following script demonstrates the improvement at the execution speed of the multithreaded multiplication version, vs the sequential matrix multiplication `

```

// test multithreading multiply vs serial

var N=1000; var M = 2000; var K=2200

var x = ones(N, M)
var y = ones(M, K)

var x0 = ones0(N, M)
var y0 = ones0(M, K)

// multithreaded multiplication for RichDouble2DArrays
tic
var xy = x*y  
var tmp = toc  // multithreaded time 

// serial multiplication for the Mat class
tic
var xy0 = x0*y0
var tms = toc  // serial time

  

```

`At my laptop I obtain:`

```
 tmp = 0.797175489 sec
 tms = 3.377879568 sec
```

`which for 4 cores is a very good speedup. `



## `Fast multiplication using JBLAS native BLAS implementation and Java multithreading ` ##

`The ` **`RichDouble2DArray`** ` class has a new multiplication implementation that exploits both multithreading and the native BLAS fast matrix multiplication implementation of the jEigen library. The new multiplication operator is the ` **`jj`** ` operator.`

`Linux64 systems perform native multiplication much faster, about 7 times faster than Windows64. We demonstrate an example: `

```

 
var N=3000

var x = ones(N, N)

tic
var xx = x jj x   // perform fast multithreaded native matrix multiplication
var tm = toc

// the default multiplication operator for RichDouble2DArray 
// also uses native BLAS combined with Java multithreading for Linux64 systems
// For Windows 64, native BLAS is not efficient, and thus pure Java multithreaded
// multiplication is used

tic
var xx2 = x * x   // perform fast multithreaded native matrix multiplication
var tmd = toc


```