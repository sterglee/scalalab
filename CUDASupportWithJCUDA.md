# Introduction #

`ScalaLab can perform ultra parallel processing by exploiting the NVDIA CUDA framework and the JCUDA library that provides the Java bindings. `

`Currently, the downloads ` **`JCuda_5_5_SupportWin64.zip`** ` (CUDA 5.5) and ` **`JCUDASupport.zip`** ` (CUDA 5.0) provide support only for Windows 64 bit. The installation of JCUDA support in ScalaLab is very simple: we copy all the .jar files within the` **`defaultToolboxes`** ` folder and all the .dll files within the ` **`lib`** ` folder. `


` If you install the CUDA support you can execute directly the examples. The speedup for large size problems is very significant, e.g. the following JCublas example, for N=1000, is executed 565  times faster, for N=2000, 1314 times faster!!  `

**`JCUDA in ScalaLab is currently tested with Windows 8`**

## JCublas Example ##

```


/*
 * JCublas - Java bindings for CUBLAS, the NVIDIA CUDA BLAS library,
 * to be used with JCuda <br />
 * http://www.jcuda.org
 *
 * Copyright 2009 Marco Hutter - http://www.jcuda.org
 
 */

import java.util.Random

import jcuda._
import jcuda.jcublas.JCublas


import jcuda.jcublas.JCublas2._
import jcuda.jcublas.cublasOperation.CUBLAS_OP_N
import jcuda.runtime.JCuda._

import jcuda.jcublas.cublasHandle

/**
     * Simple implementation of sgemm, using plain Scala
     */
 def  sgemmScala(n: Int, alpha: Float, A: Array[Float],  B: Array[Float],  beta: Float,  C: Array[Float]) = 
 {
     	var i = 0
        while  ( i <  n)
        {
        	var j = 0
            while ( j <  n)
            {
                var  prod = 0f
                var k = 0
                while  ( k <  n)
                {
                	prod += A(k * n + i) * B(j * n + k)
                	k += 1
                    
                }
                C(j * n + i) = alpha * prod + beta * C(j * n + i)
                j += 1
            }
            i += 1
        }
    }

 
     /**
     * Creates an array of the specified size, containing some random data
     */
     def createRandomFloatData(n: Int) = {
     	var random = new Random();
     	var x = new Array[Float](n)
     	for (i<-0 until n) {
     		x(i) = random.nextFloat()
     	}
     	x
     }
     

 
    /**
     * Implementation of sgemm using JCublas
     */
    def sgemmJCublas(n: Int, alpha: Float, A: Array[Float], B: Array[Float], beta: Float, C: Array[Float]) = 
    {
   var  nn = n * n

          // Create a CUBLAS handle
       var handle = new cublasHandle()
        cublasCreate(handle)

        // Allocate memory on the device
        var  d_A = new Pointer()
        var  d_B = new Pointer()
        var  d_C = new Pointer()
        cudaMalloc(d_A, nn * Sizeof.FLOAT)
        cudaMalloc(d_B, nn * Sizeof.FLOAT)
        cudaMalloc(d_C, nn * Sizeof.FLOAT)
        
        // Copy the memory from the host to the device
        cublasSetVector(nn, Sizeof.FLOAT, Pointer.to(A), 1, d_A, 1)
        cublasSetVector(nn, Sizeof.FLOAT, Pointer.to(B), 1, d_B, 1)
        cublasSetVector(nn, Sizeof.FLOAT, Pointer.to(C), 1, d_C, 1)

        // Execute sgemm
        var  pAlpha = Pointer.to( Array(alpha))
        var  pBeta = Pointer.to(Array(beta))
        cublasSgemm(handle, CUBLAS_OP_N, CUBLAS_OP_N, n, n, n, 
            pAlpha, d_A, n, d_B, n, pBeta, d_C, n)

        // Copy the result from the device to the host
        cublasGetVector(nn, Sizeof.FLOAT, d_C, 1, Pointer.to(C), 1)

        // Clean up
        cudaFree(d_A)
        cudaFree(d_B)
        cudaFree(d_C)
        cublasDestroy(handle)
    }

    
 
    /**
     * Compares the given result against a reference, and returns whether the
     * error norm is below a small epsilon threshold
     */
    def  isCorrectResult(result: Array[Float], reference: Array[Float]) = 
    {
        var  errorNorm = 0.0
        var  refNorm = 0.0
        for (i <-  0 until  result.length)
        {
            var diff = reference(i) - result(i)
            errorNorm += diff * diff*1.0
            refNorm += reference(i) * result(i)
        }
        errorNorm =  Math.sqrt(errorNorm)
        refNorm = Math.sqrt(refNorm);
        if (Math.abs(refNorm) < 1e-6)
        {
            false
        }
        (errorNorm / refNorm < 1e-6f);
    }



  var n = 2000
   var  alpha = 0.3f
    var beta = 0.7f
    var  nn = n * n

        
        println("Creating input data...")
        var h_A  = createRandomFloatData(nn)
        var h_B = createRandomFloatData(nn)
        var h_C = createRandomFloatData(nn)
        var h_C_ref = h_C.clone()

        println("Performing Sgemm with Scala...")
 	   tic
        sgemmScala(n, alpha, h_A, h_B, beta, h_C_ref)
        var tmScala = toc
        
        println("Performing Sgemm with JCublas...")
        tic
        sgemmJCublas(n, alpha, h_A, h_B, beta, h_C)
        var tmJCUDA = toc
        
        var passed = isCorrectResult(h_C, h_C_ref)
        var success = if  (passed) "PASSED" else "FAILED"
        
        println("testSgemm "+success+ " timScala = "+tmScala+ " tmJCUDA = "+tmJCUDA)
        var accelerationFactor = tmScala.toDouble / tmJCUDA.toDouble
        println("GPU performed  "+ accelerationFactor  +" times faster than CPU")

        
 
```

## Matrix Multiplication Example (Aug 08 version) ##

`This example is from a first attempt to provide a higher level interface to CUDA from ScalaLab. `


```
import jcuda._
import jcuda.jcublas.JCublas


import jcuda.jcublas.JCublas2._
import jcuda.jcublas.cublasOperation.CUBLAS_OP_N
import jcuda.runtime.JCuda._

import jcuda.jcublas.cublasHandle

import scalaSci.jcublas._
import scalaSci.jcublas.FloatMatrix._

   /**
     * Implementation of sgemm using JCublas
     *  C = alpha * A * B + beta * C 
     */
    def sgemmJCublas(m: Int, n: Int, k: Int, alpha: Float, A: Array[Float], B: Array[Float], beta: Float, C: Array[Float]) = 
    {
   
   // leading dimensions
     var lda = m
     var ldb = n
     var ldc = m
     
          // Create a CUBLAS handle
        var handle = new cublasHandle()
        cublasCreate(handle)

        // Allocate memory on the device
        var  d_A = new Pointer()
        var  d_B = new Pointer()
        var  d_C = new Pointer()
        cudaMalloc(d_A, m * n * Sizeof.FLOAT)
        cudaMalloc(d_B, n * k * Sizeof.FLOAT)
        cudaMalloc(d_C, m * k * Sizeof.FLOAT)
        
        // Copy the memory from the host to the device
        cublasSetVector(m*n, Sizeof.FLOAT, Pointer.to(A), 1, d_A, 1)
        cublasSetVector(n*k, Sizeof.FLOAT, Pointer.to(B), 1, d_B, 1)
        cublasSetVector(m*k, Sizeof.FLOAT, Pointer.to(C), 1, d_C, 1)

        // Execute sgemm
        var  pAlpha = Pointer.to( Array(alpha))
        var  pBeta = Pointer.to(Array(beta))
        cublasSgemm(handle, CUBLAS_OP_N, CUBLAS_OP_N, m, n, k, 
            pAlpha, d_A, lda, d_B, ldb, pBeta, d_C, ldc)

        // Copy the result from the device to the host
        cublasGetVector(m*k, Sizeof.FLOAT, d_C, 1, Pointer.to(C), 1)

        // Clean up
        cudaFree(d_A)
        cudaFree(d_B)
        cudaFree(d_C)
        cublasDestroy(handle)
    }

    


class JM(var flvals: FloatMatrix)  extends scalaSci.jcublas.FloatMatrix(flvals.data) {
	var mrows = flvals.rows
    var mcolumns = flvals.columns
    
  def *( B: JM) = {
    var Arows = this.mrows
    var Acols = this.mcolumns
    var Brows = B.mrows
    var Bcols = B.mcolumns
    
    var Cf = FloatMatrix.zeros(Arows, Bcols)
    
    var C = new JM(Cf)
  
    var alpha = 1.0f; var beta = 0.0f
    sgemmJCublas(Arows, Acols, Bcols, alpha, this.flvals.data, B.flvals.data, beta, C.flvals.data)
    
    C
    }
	
}  

// test

var Ar = 2000; var Ac = 1000; var Br = Ac; var Bc = 500 // dimensions
var af = FloatMatrix.ones(Ar, Ac)
var bf = FloatMatrix.ones(Br, Bc)
var A = new JM(af) 
var B = new JM(af)

// test GPU ( mine is: NVIDIA GeForce 650 Ti BOOST)
tic
var C = A*B
var tmCUDA = toc

javax.swing.JOptionPane.showMessageDialog(null, "time JCUDA = "+tmCUDA)

// test Java
var Aj = ones0(Ar, Ac)
var Bj = ones0(Br, Bc)
tic
var Cj = Aj*Bj
var tmJava = toc

javax.swing.JOptionPane.showMessageDialog(null, "time Java = "+tmJava)



```

## Example using the CUDA Sparse Matrices ##

```

import  jcuda.jcusparse.JCusparse._
import  jcuda.jcusparse.cusparseIndexBase.CUSPARSE_INDEX_BASE_ZERO
import jcuda.jcusparse.cusparseMatrixType.CUSPARSE_MATRIX_TYPE_GENERAL
import jcuda.jcusparse.cusparseOperation.CUSPARSE_OPERATION_NON_TRANSPOSE
import jcuda.runtime.JCuda._
import jcuda.runtime.cudaMemcpyKind._
import jcuda._
import jcuda.jcusparse._
import jcuda.runtime.JCuda


/**
 * A sample application showing how to use JCusparse.<br />
 * <br />
 * This sample has been ported from the NVIDIA CURAND 
 * documentation example. 
 */
 
        // Enable exceptions and subsequently omit error checks in this sample
        JCusparse.setExceptionsEnabled(true)
        JCuda.setExceptionsEnabled(true)
        
        // Variable declarations
        var handle = new cusparseHandle()
        var descra = new cusparseMatDescr()
        
        var  cooRowIndexHostPtr = new Array[Int](1)
        var  cooColIndexHostPtr = new Array[Int](1)
        var  cooValHostPtr = new Array[Float](1)
        
        var  cooRowIndex = new Pointer()
        var  cooColIndex = new Pointer()
        var  cooVal = new Pointer()
        
        var  xIndHostPtr = new Array [Int](1)
        var  xValHostPtr = new Array[Float](1)
        var  yHostPtr = new Array[Float](1)
        
        var  xInd = new Pointer()
        var  xVal = new Pointer()
        var  y = new Pointer()
        var csrRowPtr = new Pointer()
        
        var  zHostPtr = new Array[Float](1)
        var z = new Pointer()
        
        println("Testing example")
        // Create the following sparse test matrix in COO format
        // | 1.0      2.0   3.0 |
        // |     4.0                |
        // | 5.0      6.0  7.0  |
        // |     8.0         9.0  |
       var n = 4
        var nnz = 9  // # of non-zero elements
        cooRowIndexHostPtr = new Array[Int](nnz)
        cooColIndexHostPtr = new Array[Int](nnz)
        cooValHostPtr      = new Array[Float](nnz)
        
        cooRowIndexHostPtr(0)=0; cooColIndexHostPtr(0)=0; cooValHostPtr(0)=1.0f;
        cooRowIndexHostPtr(1)=0; cooColIndexHostPtr(1)=2; cooValHostPtr(1)=2.0f;
        cooRowIndexHostPtr(2)=0; cooColIndexHostPtr(2)=3; cooValHostPtr(2)=3.0f;
        cooRowIndexHostPtr(3)=1; cooColIndexHostPtr(3)=1; cooValHostPtr(3)=4.0f;
        cooRowIndexHostPtr(4)=2; cooColIndexHostPtr(4)=0; cooValHostPtr(4)=5.0f;
        cooRowIndexHostPtr(5)=2; cooColIndexHostPtr(5)=2; cooValHostPtr(5)=6.0f;
        cooRowIndexHostPtr(6)=2; cooColIndexHostPtr(6)=3; cooValHostPtr(6)=7.0f;
        cooRowIndexHostPtr(7)=3; cooColIndexHostPtr(7)=1; cooValHostPtr(7)=8.0f;
        cooRowIndexHostPtr(8)=3; cooColIndexHostPtr(8)=3; cooValHostPtr(8)=9.0f;
        
        // Create a sparse and a dense vector
        // xVal=[100.0, 200.0, 400.0] (sparse)
        // xInd=[0      1      3    ]
        // y   =[10.0, 20.0, 30.0, 40.0 | 50.0, 60.0, 70.0, 80.0] (dense)
        var nnz_vector = 3
        n = 4
        xIndHostPtr = new Array[Int](nnz_vector)
        xValHostPtr = new Array[Float](nnz_vector)
        yHostPtr    = new Array[Float](2*n)
        zHostPtr    = new Array[Float](2*(n+1))
        
        yHostPtr(0) = 10.0f;  xIndHostPtr(0)=0; xValHostPtr(0)=100.0f;
        yHostPtr(1) = 20.0f;  xIndHostPtr(1)=1; xValHostPtr(1)=200.0f;
        yHostPtr(2) = 30.0f;  
        yHostPtr(3) = 40.0f;  xIndHostPtr(2)=3; xValHostPtr(2)=400.0f;
        yHostPtr(4) = 50.0f;  
        yHostPtr(5) = 60.0f;  
        yHostPtr(6) = 70.0f;  
        yHostPtr(7) = 80.0f;  
        
        
        // Allocate GPU memory and copy the matrix and vectors into it
        cudaMalloc(cooRowIndex, nnz*Sizeof.INT)
        cudaMalloc(cooColIndex, nnz*Sizeof.INT)
        cudaMalloc(cooVal,      nnz*Sizeof.FLOAT)
        cudaMalloc(y,           2*n*Sizeof.FLOAT)
        cudaMalloc(xInd,        nnz_vector*Sizeof.INT)
        cudaMalloc(xVal,        nnz_vector*Sizeof.FLOAT)
        cudaMemcpy(cooRowIndex, Pointer.to(cooRowIndexHostPtr), nnz*Sizeof.INT,          cudaMemcpyHostToDevice)
        cudaMemcpy(cooColIndex, Pointer.to(cooColIndexHostPtr), nnz*Sizeof.INT,          cudaMemcpyHostToDevice)
        cudaMemcpy(cooVal,      Pointer.to(cooValHostPtr),      nnz*Sizeof.FLOAT,        cudaMemcpyHostToDevice)
        cudaMemcpy(y,           Pointer.to(yHostPtr),           2*n*Sizeof.FLOAT,        cudaMemcpyHostToDevice)
        cudaMemcpy(xInd,        Pointer.to(xIndHostPtr),        nnz_vector*Sizeof.INT,   cudaMemcpyHostToDevice)
        cudaMemcpy(xVal,        Pointer.to(xValHostPtr),        nnz_vector*Sizeof.FLOAT, cudaMemcpyHostToDevice)

        // Initialize JCusparse library
        cusparseCreate(handle)
        
        // Create and set up matrix descriptor
        cusparseCreateMatDescr(descra)
        cusparseSetMatType(descra, CUSPARSE_MATRIX_TYPE_GENERAL)
        cusparseSetMatIndexBase(descra, CUSPARSE_INDEX_BASE_ZERO)
        
        // Exercise conversion routines (convert matrix from COO 2 CSR format)
        cudaMalloc(csrRowPtr, (n+1)*Sizeof.INT)
        cusparseXcoo2csr(handle, cooRowIndex, nnz, n,  csrRowPtr, CUSPARSE_INDEX_BASE_ZERO)
        //csrRowPtr = [0 3 4 7 9]

        // Exercise Level 1 routines (scatter vector elements)
        var  yn = y.withByteOffset(n*Sizeof.FLOAT)
        cusparseSsctr(handle, nnz_vector, xVal, xInd,  yn, CUSPARSE_INDEX_BASE_ZERO)
        // y = [10 20 30 40 | 100 200 70 400]
        
        // Exercise Level 2 routines (csrmv)
        var  y0 = y.withByteOffset(0)
        cusparseScsrmv(handle, CUSPARSE_OPERATION_NON_TRANSPOSE, n, n, 2.0f, 
          descra, cooVal, csrRowPtr, cooColIndex, y0, 3.0f, yn)
        
        // Print intermediate results (y)
        // y = [10 20 30 40 | 680 760 1230 2240]
        cudaMemcpy(Pointer.to(yHostPtr), y, 2*n*Sizeof.FLOAT, cudaMemcpyDeviceToHost)
         var j=0; var i=0
         while  (j<  2)
        {
                i = 0
                
           while  (i< n)
            {
                 println("yHostPtr["+i+","+j+"] = " + yHostPtr(i+n*j))
                 i += 1
            }
            j+=1
        }
        
        // Exercise Level 3 routines (csrmm) 
        cudaMalloc(z, 2*(n+1)*Sizeof.FLOAT)
        cudaMemset(z, 0, 2*(n+1)*Sizeof.FLOAT)
        cusparseScsrmm(handle, CUSPARSE_OPERATION_NON_TRANSPOSE, n, 2, n, 
            5.0f, descra, cooVal, csrRowPtr, cooColIndex, y, n, 0.0f, z, n+1)
        
        // Print final results (z)
        // z = [950 400 2550 2600 0 | 49300 15200 132300 131200 0]
        cudaMemcpy(Pointer.to(zHostPtr), z, 2*(n+1)*Sizeof.FLOAT, cudaMemcpyDeviceToHost)
        
        println("Final results:\n")
         j=0
         i=0
        while (j < 2)
        {
            i = 0
            while (i < n-1)
            {
                println("z["+i+","+j+ "]= "+ zHostPtr(i+(n+1)*j))
                i += 1
            }
            j += 1
        }
        
        // Clean up
        cudaFree(y);
        cudaFree(z);
        cudaFree(xInd);
        cudaFree(xVal);
        cudaFree(csrRowPtr);
        cudaFree(cooRowIndex);
        cudaFree(cooColIndex);
        cudaFree(cooVal);
        cusparseDestroy(handle);
    
```