
package scalaSci

// object that aims to provide easier interface to Numerical Recipes Routines
object NROps {
	
 // Solves the system  A*x = b, using Gaussian elimination,
 // returning the inverse of the matrix A and the soultion vector x

/* e.g.
import scalaSci.NROps._
var A = rand(5)
var b = rand(5,1)

var (iA, x) = Gaussj(A, b)

var shouldBeZero = A*x-b
var shouldBeIdentity = iA*A

*/
def Gaussj(  A: RichDouble2DArray,  b: RichDouble2DArray) = {
  // keep copy of the arrays
    var ac = A.copy  
    var bc = b.copy
    com.nr.la.GaussJordan.gaussj(ac.getv, bc.getv)
    (new RichDouble2DArray(ac.getv), new RichDouble2DArray(bc.getv))
     }
  	
}


  

