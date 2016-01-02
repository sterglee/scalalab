# Developing new routines by exploiting the conveniences of scripting #

`Perhaps the easiest and fastest way to develop scalaSci extensions (as well as generally numerical routines) is within the ScalaLab environment. This allows, step-by-step development of the code in an interactive way, without the time consumed explicit compilations, and the potential of directly watching intermediate results. Clearly, debugging time becomes much shorter.`

`We demonstrate this type of development by means of an example. Suppose that we want to implement a new method to the object ` _`scalaSci.ILAPACK`_ `that offers an easy to use interface to many JLAPACK routines.`

`The traditional way of compiling the code is difficult, since it requires competent knowledge of the ScalaLab source code. `


`However, we can implement our code for the method as pure script, i.e. code not in any class, object or even method. That approach allows to debug the code line-by-line, observing all the changes at the variables (i.e. like a step-by-step execution in an advanced IDE). When we succeed in assuring the correctness of our code we can place it within the proper method and class/object/trait. `


`Suppose for the specific LAPACK routine that we want to offer a simple interface to the routine ` _`DGELD:`_

`That routine computes the minimum norm solution to a real linear least squares problem: ` **`  minimize || b - A * x ||`** <sub>2</sub> ` using the singular value decomposition (SVD) of `**`A`**`.`

**`A`** `is an `_`m`_`-by-`_`n`_ `matrix which may be rank-deficient.  Several right hand side vectors` **`b`** `and solution vectors` **`x`** `can be handled in a single call; they are stored as columns of the` _`m`_`-by-`_`nrhs`_ `right hand side matrix` **`B`**` and the `_`n`_`-by-`_`nrhs`_` solution matrix` **`X`**

`The `_`DGELD`_ `routine accepts as input the matrices` **`A`** `and `**`B`** `and returns a tuple consisting of the solution` **`X`**`, the singular values of` **`A`** `in decreasing order and the effective rank of` **`A`**, i.e. `
```
def  DGELD(A: Array[Array[Double]], B: Array[Array[Double]]): (X: Array[Array[Double]], Array[Double], Int)
```


`The first step is to use the proper imports that we can find at the souce of the scalaSci.ILAPACK object. There are: (perhaps all are not required but do not harm either)`

```

import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.DenseVector
import no.uib.cipr.matrix.EVD
import no.uib.cipr.matrix.NotConvergedException
import org.netlib.util.intW
import org.netlib.lapack.LAPACK
import java.lang.Math._
import no.uib.cipr.matrix.LQ
import no.uib.cipr.matrix.LowerTriangDenseMatrix
import scalaSci.math.array.LUResults

```

`Then  we create two arrays that will serve as the methods parameters, e.g. `
```
var A = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6")
var B = AAD("4.5; 3.4")
```

`Subsequently, we develop the code by having the potential to test it, line-by-line. Only, when we assure that the results are what we expect, we pack the code in a method with parameters A and B. We list below the relevant code:`

```
        val  dA = new DenseMatrix(A)
    	val  dB = new DenseMatrix(B)
 	val  dAc = dA.copy()
    	var dBc = new DenseMatrix(1,1)

	var rcond =  -1.0  // used to determine the effective rank of  A. Singular values S(i) <= rcond*S(1) are treated as zero. If rcond<0 machine precision is used instead
    	var M = A.length   // number of rows of matrix A
    	var N = A(0).length  // number of columns of matrix A
    	var  NRHS = B(0).length;  // number of right hand sides, i.e., the number of columns of the matrices B and X

     var minMN  = N
     if (M<minMN) minMN = M  
     var S = new Array[Double](minMN)   // the singular values of A in decreasing order
     												// the condition number of A in the 2-norm = (S(1)/S(m,n))
     
    var  solVecLength = N
    if  (M < N)  {  // undetermined problem
       dBc  = new DenseMatrix(N, NRHS)
       for ( r <- 0 until M)
           for ( c<- 0 until  NRHS)
               dBc.set(r, c, B(r)(c))
       
    }
    else  // overdetermined problem
        dBc = dB.copy()
    
    // each column of resultsMatrix will have a solution vector
    var  resultsMatrix = Array.ofDim[Double](solVecLength, NRHS)
    
    
    var info = new intW(0)
    
    var  workSize = -1 // issue workspace query
    var  work = new Array[Double](2)
    
    var  LDA = ld(M)  // leading dimension of A
    var  LDB = Math.max(M, N)  // since LDB >= max(1, M, N)

    var rank = new intW(0)
    var iwork = new Array[Int](1)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB, S, rcond, rank,  work, workSize, iwork,  info)
    workSize =  work(0).asInstanceOf[Int]  // take the computed optimal workspace size
    
    println("returned worksize = "+workSize)
    
    work = new Array[Double](workSize)
    iwork = new Array[Int](workSize)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB,S, rcond, rank, work, workSize, iwork, info)

    if (info.`val` != 0)  {
        println("illegal DGELS with code:  "+info.`val`)
        resultsMatrix
    }
    
    // copy the results to the resultsMatrix
    for (c <- 0 until NRHS)  // for all RHS
        for (v <- 0 until  solVecLength)  // for all variables of the system
            resultsMatrix(v)(c) = dBc.get(v, c)
    
```



`Finally, we can easily pack our debugged code within a method, e.g. `

```
  
/* computes the minimum norm solution to a real linear least squares problem
       minimize || b - A * x || _2
    using the singular value decomposition (SVD) of A.
    A is an m-by-n matrix which may be rank-deficient.
	Several right hand side vectors b and solution vectors x can be handled 
	in a single call; they are stored as columns of the m-by-nrhs right hand side matrix B
	and the n-by-nrhs solution matrix X
Example:

   val testA = AAD("-1.2 0.3 4.3; 0.2 3.4 5.6")
   val testB = AAD("4.5; 3.4")
   val testR = DGELD(testA, testB)
   val X = testR._1  // the solution
   val S = testR._2 // the singular values in decreasing order
   val rnk = testR._3.`val` // the effective rank of testA
   

 */  

def  DGELD(A: Array[Array[Double]], B: Array[Array[Double]]) = {
    	val  dA = new DenseMatrix(A)
    	val  dB = new DenseMatrix(B)
 	val  dAc = dA.copy()
    	var dBc = new DenseMatrix(1,1)

	var rcond =  -1.0  // used to determine the effective rank of  A. Singular values S(i) <= rcond*S(1) are treated as zero. If rcond<0 machine precision is used instead
    	var M = A.length   // number of rows of matrix A
    	var N = A(0).length  // number of columns of matrix A
    	var  NRHS = B(0).length;  // number of right hand sides, i.e., the number of columns of the matrices B and X

     var minMN  = N
     if (M<minMN) minMN = M  
     var S = new Array[Double](minMN)   // the singular values of A in decreasing order
     												// the condition number of A in the 2-norm = (S(1)/S(m,n))
     
    var  solVecLength = N
    if  (M < N)  {  // undetermined problem
       dBc  = new DenseMatrix(N, NRHS)
       for ( r <- 0 until M)
           for ( c<- 0 until  NRHS)
               dBc.set(r, c, B(r)(c))
       
    }
    else  // overdetermined problem
        dBc = dB.copy()
    
    // each column of resultsMatrix will have a solution vector
    var  resultsMatrix = Array.ofDim[Double](solVecLength, NRHS)
    
    
    var info = new intW(0)
    
    var  workSize = -1 // issue workspace query
    var  work = new Array[Double](2)
    
    var  LDA = ld(M)  // leading dimension of A
    var  LDB = Math.max(M, N)  // since LDB >= max(1, M, N)

    var rank = new intW(0)
    var iwork = new Array[Int](1)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB, S, rcond, rank,  work, workSize, iwork,  info)
    workSize =  work(0).asInstanceOf[Int]  // take the computed optimal workspace size
    
    println("returned worksize = "+workSize)
    
    work = new Array[Double](workSize)
    iwork = new Array[Int](workSize)
    LAPACK.getInstance().dgelsd( M, N, NRHS, dAc.getData(), LDA, dBc.getData(), LDB,S, rcond, rank, work, workSize, iwork, info)

    if (info.`val` != 0)  {
        println("illegal DGELS with code:  "+info.`val`)
        resultsMatrix
    }
    
    // copy the results to the resultsMatrix
    for (c <- 0 until NRHS)  // for all RHS
        for (v <- 0 until  solVecLength)  // for all variables of the system
            resultsMatrix(v)(c) = dBc.get(v, c)
    
     (resultsMatrix, S, rank)
  }


```