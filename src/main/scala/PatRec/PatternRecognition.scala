package PatRec

import org.apache.commons.math.linear.Array2DRowRealMatrix
import org.apache.commons.math.random.CorrelatedRandomVectorGenerator
import org.apache.commons.math.random.GaussianRandomGenerator
import org.apache.commons.math.random.JDKRandomGenerator
import scala.language.postfixOps


import _root_.scalaSci.StaticMaths._

object PatternRecognition {

  // generate Nvecs,  l-dimensional vectors,  from a Gaussian distribution with mean m 
  // and covariance matrix S
  def mvnrnd(m: Array[Double], S:  Array[Array[Double]], Nvecs: Int) = {
// create and seed a RandomGenerator (could use any of the generators in the random package here)
    var rg = new JDKRandomGenerator()
    
    // create a GaussianRandomGenerator using rg as its source of randomness
    var rawGenerator = new GaussianRandomGenerator(rg)
    
//    var covariance = MatrixUtils.createRealMatrix(S)
    var covariance = new Array2DRowRealMatrix(S)
    
 
    var generator = new CorrelatedRandomVectorGenerator(m, covariance, 1.0e-12 * covariance.getNorm(), rawGenerator)

    // use the generator to generate correlated vectors
    var  Ncomps = m.length
    var vecsAll = Array.ofDim[Double](Ncomps, Nvecs)
for (k<-0 until Nvecs) {
 var  randomVector = generator.nextVector()
 for (m <- 0 until Ncomps ) 
  vecsAll(m)(k) = randomVector(m)
 } 
 
vecsAll
  
  }
    


 
def comp_gauss_dens_val(m: Array[Array[Double]], S: Array[Array[Double]], x: Array[Array[Double]]) = {
     val l = m.length
     var z =  (1.0/ (pow(2.0*PI, l/2.0)*
                   pow(det(S),0.5)))   * exp(-0.5*(  ((x-m)~) * inv(S) * (x-m)) )
   z
  }


}
