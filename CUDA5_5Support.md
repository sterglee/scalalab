# Introduction #

`ScalaLab provides experimentally support for CUDA 5.5. CUDA offers the potential for significant speedup of many difficult computationally operations. `

**`High-level operators, work correctly with NVIDIA devices of compute capability 1.3 and above. This is because, we use double precision arithmetic, and older devices supported adequately only single precision arithmetic. In any case, older GPUs either yield an insignificant speedup, or even work slower!`**

**`Thus to benefit from CUDA in ScalaLab, a newer NVIDIA GPU is required, and the speedup can be significant, e.g. ten or hundrends of times faster than the CPU!`**

**`To use CUDA operations, it is very easy, only the installation of CUDA from NVIDIA is required that can be obtained from `**

https://developer.nvidia.com/cuda-downloads

`Also, for ` **`Windows64`** ` there exists at the ScalaLab downloads a ` **`JCUDA`** ` version, that offers the potential to use a lot of very fast routines. To use JCUDA: `

  * `Download ` _`Jcuda_5_5_SupportWin64.zip`_ `and unzip it`
  * `Copy all the .jar files that it contains within the `     **`DefaultToolboxes`** `ScalaLab directory.`
  * `Copy all the .dll files that it contains within the lib ScalaLab directory.`


`Currently, there exists a ` **`KernelOps.cu`** `file. It can be obtained from the ScalaLab sources in the directory ` **`CUDAOps`** `That file implements all the CUDA linking functionality, i.e. the ` **`CUDA kernels`** `, C utility routines and JNI related routines. `

`The Java file ` **`KernelOps.java`** ` declares the relating JNI interface for CUDA routines. `



`An example ScalaSci program that utilizes CUDA is: `
```


var km = new CUDAOps.KernelOps()


var N=10
var a = new Array[Float](N)
var b = new Array[Float](N)
var c = new Array[Float](N)

for (k<-0 until N) a(k)=k*1.0f
for (k<-0 until N) b(k)=k*1.0f

// test matrix addition
km.cma(a, b, c)

c



// test multiplication 
var Aw = 100; var Ah = 30; var Bw = 15; var Bh = Aw
var amg = new Array[Float](Aw*Ah)
var bmg = new Array[Float](Ah*Bh)
var cmg = new Array[Float](Ah*Bw)

for (k<-0 until Aw*Ah) amg(k)=1.0f
for (k<-0 until Ah*Bh) bmg(k)=1.0f

tic
km.cmm(amg, bmg, cmg, Ah, Aw, Bw)
var tm = toc

```

`Currently, there exist a few high level operators, some are based on direct kernel implementations and some on CUDA BLAS. CUDA BLAS versions are faster, and also single precision arithmetic based routines perform faster. This fact can change as new GPUs support better double precision arithmetic hardware.These operators are illustrated at the script example below. For example one such operator is the ` **`*@`** `,defined in the ` **`RichDoubleArray`** `class. The execution times on newer NVDIA cards make a significant difference. `

`Specifically, I tested two configurations: `
` 1.  a newer ` **`NVIDIA GeForce GTX 650Ti BOOST, CPU Intel i5-3470, 3.2GHz : Scala: 2.13 sec, GPU: 0.15 sec`**
` 2.  an older ` **`NVIDIA GeForce 9800 GT, CPU Intel i7-920-2.67GHz,  Scala: 2.74 sec, GPU: 2.41 sec`**

`Therefore, the difference becomes very significant with the increasing multithreaded offered by newer NVDIA cards! Also, the implementation of CUDA multiplication is not optimized, i.e.` **`shared memory`** `is not utilized. Finally, the overhead of memory transfers, between device and host spaces and also JVM and host spaces, is important. However, that overhead will become of less importance when we implement complex computational algorithms with much higher complexity than matrix multiplication. `

`A test script for CUDA based matrix multiplication follows: `


```


var ARows = 1000; var ACols = 1500; var BRows = ACols;
var BCols = 1400; var CRows = ARows; var CCols = BCols;


var A = ones(ARows, ACols)
var B = ones(BRows, BCols)

tic
var C = A*B
var tmScala = toc

// CUDA using CBLAS, single precision
tic
var Cc1 = A *@ B
var tmCUDABLASf = toc

// CUDA using kernel multiply, single precision
tic
var Cc2 = A *@@ B
var tmCUDAf = toc

// CUDA using CBLAS, double precision
tic
var Cc3 = A *& B
var tmCUDABLASd = toc

// CUDA using kernel multiply, double precision
tic
var Cc4 = A *&& B
var tmCUDAd = toc

// CUDA using CBLAS, double precision
// CAUTION: double precision is not implemented on older NVIDIA cards
tic
var Cc3 = A *& B
var tmCUDABLASd = toc

```

`It can be interested that I used simple commands to generate the interface, using only the ` **`nvcc`** `compiler (that calls automatically the gcc for Linux and cl for Windows. The relevant commands that I used are: `


```

// compile Java interface class
javac KernelOps.java


// create the .h file for JNI
javah CUDAOps.KernelOps

// Linux compile
nvcc -shared -Xcompiler -fPIC, -o KernelOps.so  KernelOps.cu 


// Windows compile
nvcc -I "C:\Program Files\Java\jdk1.7.0_25\include" -shared  -Xcompiler -LD KernelOps.cu
ren a.exe KernelOps.dll

```


`The critical code that implements the CUDA interface is: `

```

// includes, system
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>


#include "CUDAOps_KernelOps.h"



  // Thread block size 
#define BLOCK_SIZE 16
 

#define SDATA( index)      cutilBankChecker(sdata, index)

////////////////////////////////////////////////////////////////////////////////
//! Simple test kernel for device functionality
//! @param g_idata  input data in global memory
//! @param g_odata  output data in global memory
////////////////////////////////////////////////////////////////////////////////
// Kernel that executes on the CUDA device
    
__global__ void add_matrix(float *a, float *b, float *c, int N)
{
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < N) c[idx] = a[idx] + b[idx];
}

	
void cuda_matrixAdd(float *a_h, float *b_h, float *c_h, int N)
{
    float *a_d, *b_d, *c_d;
   
    size_t size = N * sizeof (float);
        
    // allocate memory in the GPU device for a, b and c
    cudaMalloc((void **) & a_d, size);
    cudaMalloc((void **) & b_d, size);
    cudaMalloc((void **) & c_d, size);
    // copy from host to GPU device
    cudaMemcpy(a_d, a_h, size, cudaMemcpyHostToDevice);
    cudaMemcpy(b_d, b_h, size, cudaMemcpyHostToDevice);
    // do calculations on device
    int block_size = 256;
    int n_blocks = N / block_size + (N % block_size == 0 ? 0 : 1);
    add_matrix <<<n_blocks, block_size >>>(a_d, b_d, c_d, N);
    // Retrieve results from the device
    cudaMemcpy(c_h, c_d, size, cudaMemcpyDeviceToHost);
        // Cleanup
        
    cudaFree(a_d);
    cudaFree(b_d);
    cudaFree(c_d);
    
}

  
  
 

// Device multiplication function called by Mul() 
// Compute C = A * B
//   hA is the height of A (i.e. # rows) 
//   wA is the width of A (i.e. # columns)
//   wB is the width of B 
__global__ void Muld(float* A, float* B, int hA, int wA, int wC, float* C) 
{ 
   // each thread computes one element of C
   // by accumulating results into Cvalue
     float Cvalue = 0.0;
	 int row = blockIdx.y * blockDim.y + threadIdx.y;
	 int col = blockIdx.x * blockDim.x + threadIdx.x;
	   
	 if (row >= hA || col >= wC) return;
	    
	 for (int e=0; e<wA; ++e)
	  Cvalue += (A[row*wA+e]) *(B[e*wC+col]);
     
	 C[row*wC+col] = Cvalue;
 } 
	 
	  
	      
 
 
// Host multiplication function 
// Compute C = A * B 
//   hA is the height of A (i.e. # rows)
//   wA is the width of A (i.e. # cols)
//   wB is the width of B 
void Mul(const float* A, const float* B, int hA, int wA, int wB, float* C) 
{ 
    int size; 
 
    // Load A and B to the device 
    float* Ad; 
    size = hA * wA * sizeof(float); 
    cudaError_t err = cudaMalloc((void**)&Ad, size);
    //printf("CUDA malloc A: %s \n", cudaGetErrorString(err));
    err = cudaMemcpy(Ad, A, size, cudaMemcpyHostToDevice);
    //printf("Copy A to device: %s \n", cudaGetErrorString(err));
    
    float* Bd; 
    int hB = wA;   // #rows of B == #columns of A 
    size = hB * wB * sizeof(float); 
    err = cudaMalloc((void**)&Bd, size); 
   // printf("CUDA malloc B: %s \n", cudaGetErrorString(err));
    err = cudaMemcpy(Bd, B, size, cudaMemcpyHostToDevice);
    //printf("Copy B to device: %s \n", cudaGetErrorString(err));
 
    // Allocate C on the device  
    float* Cd; 
    int hC = hA;   // #rows of C == #rows of A
    int wC = wB;   // #columns of C == #columns of B
    size = hC * wC * sizeof(float);
    err = cudaMalloc((void**)&Cd, size); 
   // printf("CUDA malloc C: %s \n", cudaGetErrorString(err));
    
    // Compute the execution configuration assuming 
    // the matrix dimensions are multiples of BLOCK_SIZE 
    
    /******************** 
    calculates the execution configuration
    effectively the kernel function <Muld> will be
    executed concurrently by BLOCK_SIZE^2 GPU threads
    ************************/
    dim3 dimBlock(BLOCK_SIZE, BLOCK_SIZE); 
    dim3 dimGrid((wB + dimBlock.x-1)/dimBlock.x, (hA+dimBlock.y-1) / dimBlock.y); 
    // Launch the device computation 
    Muld<<<dimGrid, dimBlock>>>(Ad, Bd, hA, wA, wC, Cd); 

    err = cudaThreadSynchronize();
    //printf("Run kernel:   %s \n", cudaGetErrorString(err));
    
    
    // Read C from the device 
    err = cudaMemcpy(C, Cd, size, cudaMemcpyDeviceToHost);
   // printf("Copy C off the device:  %s \n", cudaGetErrorString(err));
    
 
    // Free device memory 
    cudaFree(Ad); 
    cudaFree(Bd); 
    cudaFree(Cd); 
}      



   
  
// multiply for rectangular matrices
extern "C"
JNIEXPORT void JNICALL Java_CUDAOps_KernelOps_cmm(JNIEnv *env, jobject obj, jfloatArray aArray, jfloatArray bArray, jfloatArray cArray, jint hA, jint wA, jint wB)
  {
    jfloat *a = env->GetFloatArrayElements( aArray, 0);
    jfloat *b = env->GetFloatArrayElements( bArray, 0);
    jfloat *c = env->GetFloatArrayElements( cArray, 0);
    
	// call the C multiplication routine 
	Mul(a,  b, hA, wA, wB, c); 
	 
	env->ReleaseFloatArrayElements( aArray, a, 0);
	env->ReleaseFloatArrayElements( bArray, b, 0);
	env->ReleaseFloatArrayElements( cArray, c, 0);
	
	}


extern "C"
JNIEXPORT void JNICALL Java_CUDAOps_KernelOps_cma(JNIEnv *env, jobject obj, jfloatArray aArray, jfloatArray bArray, jfloatArray cArray)
{
    
    jfloat *a = env->GetFloatArrayElements( aArray, 0);
    jfloat *b = env->GetFloatArrayElements( bArray, 0);
    jfloat *c = env->GetFloatArrayElements( cArray, 0);
    
    jsize N = env->GetArrayLength( aArray);
    
    cuda_matrixAdd(a, b, c, N);
    
    env->ReleaseFloatArrayElements( aArray, a, 0);
    env->ReleaseFloatArrayElements( bArray, b, 0);
    env->ReleaseFloatArrayElements( cArray, c, 0);
  
}


```

## Fast multiplication of RichDouble2DArrays with CUDA ##

`Matrix multiplication concerning RichDouble2DArrays can obtain significant speedup using CUDA. The following example illustrates: `


```
var ARows = 1000; var ACols = 1500; var BRows = ACols;
var BCols = 1400; var CRows = ARows; var CCols = BCols;


var A = ones(ARows, ACols)
var B = ones(BRows, BCols)

tic
var C = A*B
var tmScala = toc

// CUDA using CBLAS, single precision
tic
var Cc1 = A *@ B
var tmCUDABLASf = toc

```