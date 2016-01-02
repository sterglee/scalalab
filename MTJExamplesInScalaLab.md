# Introduction #

`This page provides examples of using the MTJ library from ScalaLab. `

## LU decomposition of dense matrices ##



```

import no.uib.cipr.matrix.DenseLU
import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.Matrices
import no.uib.cipr.matrix.Matrix
import no.uib.cipr.matrix.Utilities

// Tests the LU decomposition
 
var  n = 100
       
// create an appropriate band matrix
var A = new DenseMatrix(n, n)
Utilities.populate(A)

tic

// perform an LU decomposition
var lu = new DenseLU(n, n)
// factorize matrix A
lu.factor(A.copy())


// make a DenseMatrix of size nXn
var I = Matrices.identity(n)
var solution = lu.solve(I)

var tmLUSolveDense = toc()

var J = I.multAdd(1.0, A, new DenseMatrix(n, n))



```


## LU Decomposition of banded matrices ##

```
var  n = 100 

var kl = 2 
var ku = 3
       
// create an appropriate band matrix
var A = new BandMatrix(n, kl, kl + ku)

// populate A with random values returning the result as an Array[Array[Double]]
var rvs = Utilities.bandPopulate(A, kl, ku)

tic
// perform an LU decomposition
var lu = new BandLU(n, kl, ku)
// factorize matrix A
lu.factor(A.copy())


// make a DenseMatrix of size nXn
var I = Matrices.identity(n)
// the solution x = A^{-1} \cdot x, is overwritten on I
var solution = lu.solve(I)

// compute the time for the solution
var tmLUSolveband = toc()

// test the solution
var J = I.multAdd(1.0, A, new DenseMatrix(n, n))

```

## Lower Symmetrical Positive Definite Packed Matrices ##

```

import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.LowerSPDPackMatrix
import no.uib.cipr.matrix.Matrices
import no.uib.cipr.matrix.Matrix
import no.uib.cipr.matrix.PackCholesky
import no.uib.cipr.matrix.UpperSPDPackMatrix

import no.uib.cipr.matrix.Utilities



var  n = 5
// matrix is "packed" since the upper part is not stored by symmetry
var      L = new LowerSPDPackMatrix(n)

Utilities.lowerPopulate(L)  // populate the lower part: the upper part is populated by symmetry
Utilities.addDiagonal(L, 1)   // add 1 to all the diagonal elements
// assure symmetric posistive definiteness i.e. real positive eigenvalues
while (!Utilities.spd(L))
     Utilities.addDiagonal(L, 1)
var I = Matrices.identity(n)  // construct an Identity matrix
// solve the corresponding system: L*x = I  returning solution in x
var xl = L.solve(I, new DenseMatrix(n,n))  
//  form: yl = 1.0*L*xl + 0
var yl = L.multAdd(1.0, xl, new DenseMatrix(n,n) )


// matrix is "packed" since the lower part is not stored by symmetry
var  U = new UpperSPDPackMatrix(n)
Utilities.upperPopulate(U)    // populate the upper part: the lower part is populated by symmetry
Utilities.addDiagonal(U, 1)    // add 1 to all the diagonal elements
// assure symmetric posistive definiteness i.e. real positive eigenvalues
while (!Utilities.spd(U))
          Utilities.addDiagonal(U, 1);
I = Matrices.identity(n)  // construct an Identity matrix
// solve the corresponding system: L*x = I  returning solution in x
var xu = U.solve(I, new DenseMatrix(n,n))  
//  form: y = 1.0*U*xu + 0
var yu = U.multAdd(1.0, xu, new DenseMatrix(n,n) )



```

## Eigenvalue decomposition for lower symmetrical positive definite banded matrix and for general dense matrices ##

```

import no.uib.cipr.matrix.Utilities
import no.uib.cipr.matrix.LowerSPDBandMatrix
import no.uib.cipr.matrix.SymmBandEVD

var n = 20
var kd = 3

        
// Lower symmetrical positive definite banded matrix. It does not enforce this
// property (except for symmetry), and has the same storage layout as
//  no.uib.cipr.matrix.LowerSymmBandMatrix LowerSymmBandMatrix

var A = new LowerSPDBandMatrix(n, kd)

// Populates the banded matrix
//      @param A
//          Matrix to populate
//     @param kl
//  Number of subdiagonls
//      @param ku
//  Number of superdiagonals
//  @return The matrix data in dense format

var Ad = Utilities.bandPopulate(A, kd, kd)

tic
var evv = new SymmBandEVD(n, false, false)
var fevv = no.uib.cipr.matrix.SymmBandEVD.factorize(A, kd)
var tmBanded = toc

var ee = fevv.getEigenvalues

// construct a dense matrix
var dm = new no.uib.cipr.matrix.DenseMatrix(Ad)

tic
var dmevd = new no.uib.cipr.matrix.EVD(n)
var dmfactorize = no.uib.cipr.matrix.EVD.factorize(dm)
var tmGeneral = toc
var eevals = dmfactorize.getRealEigenvalues



```