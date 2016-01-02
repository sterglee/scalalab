# Introduction #

`Matrix multiplication is a basic operation that should be performed as fast as possible. We illustrate a method for fast matrix multiplication using the Pthreads library.`

`The code for Pthread based matrix multiplication is illustrated below. `

`The ` **`pt`** `method performs fast matrix multiplication. `

`You can use fast matrix multiplication as: `

```
var N = 2000; var M = 1500; var K = 2500;

var x1 = rand(N, M)
var x2 = rand(M, K)

// test fast matrix multiplication
tic
var xx = x1 pt x2
var tm = toc

// use Java multiplication
tic
var xxj = x1 * x2
var tmj = toc

// verify results
var dif = xx-xxj
max(max(dif)) // should be zero
min(min(dif)) // should be zero
```

`However, the Pthreads based implementation of multiplication, runs at about the same speed as Java's multithreaded multiplication. Thus, Java JIT compilation prooves excellent in performance!`

# Pthread based fast matrix multiplication #

```


static int N1, N2, N3;  // A  is N1XN2, B is N2XN3
double *AA, *BB, *CC;  // pointer to matrices A, B, C
int NumThreads = 20;  // number of threads to use
int SliceRows;   // how many rows each slice has


// multiply a slice of the matrix consisted from rows sliceStart up to sliceEnd
void multiplySlice( int sliceStart, int sliceEnd, int threadId)  {
	double *pA;
	double *pB;
	double *pC;
	double smrowcol;
  
  if (sliceStart >= N1) return;
  if (sliceEnd >= N1) sliceEnd = N1;
  
  pC = CC + threadId*SliceRows*N3;  // position that this thread starts outputting results to C matrix
  // for all the rows of the matrix A that the thread has the responsibility to compute
  for (int i=sliceStart; i<sliceEnd; i++) {
 for (int j=0; j<N3; j++) {

	  smrowcol = 0.0;
	  pA = AA + i*N2;  // current row start
	  pB = BB+j*N2;
	  
	  for (int k=0; k<N2; k++) {
		smrowcol += *pA * *pB;
		pA++;  
		//pB += n3;  // normally this advances to the next column element of B, but it's transposed!
		pB++;  // matrix B enters the routine transposed to exploit cache locality
		}
		*pC++ = smrowcol;
	}
  }
 }

 // the thread function
void * threadMultiply( void * slice) {
  int s = (int) slice; // which slice of the matrix belongs to that thread

	multiplySlice( SliceRows*s,  SliceRows*s+SliceRows, s);  // each thread multiplies its part of rows
	
  return (int *)1;
}


  

  
extern "C"
JNIEXPORT void JNICALL Java_CCOps_CCOps_pt
 (JNIEnv *env, jobject obj, jdoubleArray a, jint n1, jint n2, 
	jdoubleArray b, jint n3, jdoubleArray c) {
	AA = (double *)env->GetDoubleArrayElements( a, NULL);
	BB = (double *)env->GetDoubleArrayElements( b, NULL);
	CC = (double *)env->GetDoubleArrayElements( c, NULL);
		
	double *pA;
	double *pB;
	double *pC;
	double smrowcol;
  
  // keep these parameters globally since they are needed by all threads
   N1 = n1;
   N2 = n2;
   N3 = n3;
  
   SliceRows = (int)(N1/NumThreads);  // number of rows to process each thread
   
   if (SliceRows==0)  // a very small matrix: multiply serially
      multiplySlice(0, N1, 0);
	else {
   // allocate memory for the threads. One more thread is required to process the last part of the matrix
   pthread_t * thread;
   thread = (pthread_t*) malloc((NumThreads+1)*sizeof(pthread_t));
   
   // wait for the threads to finish
   for (int i = 0; i <= NumThreads; i++) {
    if (pthread_create(&thread[i], NULL, threadMultiply, (void *)i) != 0)
	{
	perror("Can't create thread");
	free(thread);
	return;
	 }
	}
	
   for (int i=0; i<= NumThreads; i++)
     pthread_join(thread[i], NULL);
	 
	 free(thread);
	}
	
	env->ReleaseDoubleArrayElements(a, AA, 0);
	env->ReleaseDoubleArrayElements( b, BB, 0);
	env->ReleaseDoubleArrayElements(c, CC, 0);
	
	
}


```


## Scala based multithreaded matrix multiplication ##

`Even though the C based pthread multiplication seems very optimized, Scala based multithreaded multiplication performs slightly faster!`

`The code for Scala based multithreaded multiplication in ScalaLab is illustrated below: `

```

  // Array[Array[Double]] * Array[Array[Double]]
 override final def * (that: RichDouble2DArray): RichDouble2DArray =  {
   var   rN = v.length;   var rM = this.v(0).length;
   var  sN = that.v.length;  var sM = that.v(0).length
  
    var productDims = 1.0*rN*rM*sM
    var useMultithreading = if (productDims> scalaExec.Interpreter.GlobalValues.mulMultithreadingLimit) true else false
   if (useMultithreading )  {
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
  var rowsPerThread = (sM / nthreads).toInt  + 1   // how many rows the thread processes

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
   else {   // serial multiplication
    var  vr = Array.ofDim[Double] (rN, sM)   // for computing the return Matrix

      // keeps column j of "that" matrix 
    var   v1Colj = new Array[Double](rM)

    var j=0; var k=0;   
    while (j < sM)  {
     
        // copy column j of "that"  matrix, in order to have it in cache during the evaluation loop
       k=0
      while  (k < rM) {
        v1Colj(k) = that(k, j)
        k += 1
      }

      var i=0
      while (i<rN) {
        var   Arowi = this.v(i)  // row i of the "receiver" matrix
        var   s = 0.0
        k=0
        while (k< rM) {
          s += Arowi(k)*v1Colj(k)
          k += 1
        }
      vr(i)(j) = s;
      i += 1
      }
 j += 1
   }
  return new RichDouble2DArray (vr)
  
   }
      
  }


```

`For example, we can observe with the following script that Java/Scala based multithreaded multiplication outperforms slightly the optimized pthread based C multiplication: `

```


var N = 2500

var M = 1800

var K = 2111

var x = rand(N, M)

var y = rand(M, K)


// multiply matrices using Java/Scala multithreading
tic
var xy = x * y
var tm = toc

// multiply matrices using Pthread based C multithreading
tic
var xyc = x pt y
var tmc = toc


```