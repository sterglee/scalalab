## Cholesky Decomposition ##

```



import  com.nr.NRUtil._
import  com.nr.test.NRTestUtil._

import com.nr.la.Cholesky

var  diag=10.0
var a = Array.ofDim[Double](50, 50)
    
var r=new Array[Double](50)
  
var y=new Array[Double](50)
    
 ranmat(a, diag)
 ranvec(r)   

println("Testing cholesky")

// make a positive definite matrix
var aposdef = matmul(a, transpose(a).getv)    

// perform a Cholesky factorization of the matrix, p. 101 NR3
var ach = new Cholesky(aposdef)

// solve using Cholesky factorization, p. 101 NR3
ach.solve(r,y)


var residual = aposdef*y-r   // should be zero

     

```