# Introduction #

`We present an example of using the Apache Commons Math library to fit data with a polynomial model.`



# Polynomial fitting code #

`The function ` **`avalue`** `is the function with which the data are generated. We inject some amount of uniformly distributed noise to that data.`

`Next, we proceed to recover the generating function, knowing however its model, i.e. that is a 2nd degree polynomial. In order to perform the recovery we use the Levenberg Marquardt Optimizer from the Apache Commons Maths library. The plot demonstrates that the algorithm succeeds in accurately recovering the parameters of the polynomial from the noise measurements. `

```

// these imports are from an older version of the Apache Common
// Maths library that is included within the ScalaLab libraries
// ScalaLab sources include a new version of the Apache Common Maths
import org.apache.commons.math._
import org.apache.commons.math.analysis._ 
import org.apache.commons.math.analysis.function._ 
import org.apache.commons.math.analysis.integration._ 
import org.apache.commons.math.analysis.interpolation._ 
import org.apache.commons.math.analysis.polynomials._ 
import org.apache.commons.math.analysis.solvers._ 
import org.apache.commons.math.dfp._ 
import org.apache.commons.math.distribution._ 
import org.apache.commons.math.estimation._ 
import org.apache.commons.math.exception._ 
import org.apache.commons.math.exception.util._ 
import org.apache.commons.math.filter._ 
import org.apache.commons.math.fraction._ 
import org.apache.commons.math.genetics._ 
import org.apache.commons.math.geometry._ 
import org.apache.commons.math.geometry.euclidean.oned._ 
import org.apache.commons.math.geometry.euclidean.threed._ 
import org.apache.commons.math.geometry.euclidean.twod._ 
import org.apache.commons.math.geometry.partitioning._ 
import org.apache.commons.math.geometry.partitioning.utilities._ 
import org.apache.commons.math.linear._ 
import org.apache.commons.math.ode._ 
import org.apache.commons.math.ode.events._ 
import org.apache.commons.math.ode.nonstiff._
import org.apache.commons.math.ode.sampling._
import org.apache.commons.math.optimization._
import org.apache.commons.math.optimization.direct._
import org.apache.commons.math.optimization.fitting._
import org.apache.commons.math.optimization.general._
import org.apache.commons.math.optimization.linear._
import org.apache.commons.math.optimization.univariate._
import org.apache.commons.math.random._
import org.apache.commons.math.special._
import org.apache.commons.math.stat._
import org.apache.commons.math.stat.clustering._
import org.apache.commons.math.stat.correlation._
import org.apache.commons.math.stat.descriptive._
import org.apache.commons.math.stat.descriptive.moment._
import org.apache.commons.math.stat.descriptive.rank._
import org.apache.commons.math.stat.descriptive.summary._
import org.apache.commons.math.stat.inference._
import org.apache.commons.math.stat.ranking._
import org.apache.commons.math.stat.regression._
import org.apache.commons.math.transform._
import org.apache.commons.math.util._
import edu.jas.arith._  
import edu.jas.poly._  
import edu.jas.integrate._  

        
                        
def  avalue(x: Double) =  4.5*x*x -  3.7*x+5.6

var nP  = 20
var x = linspace(0, 2, nP) 
var ay = new Array[Double](nP)
var y = new Array[Double](nP)
x.length

var k=0
while (k < nP) {
  ay(k) = avalue(x(k))
  y(k) = ay(k)+(java.lang.Math.random()-0.5)
  k += 1
}


var optimizer = new LevenbergMarquardtOptimizer()

var weights = new Array[Double](nP)
k=0
while (k < nP) { weights(k)=1.0; k+=1 }
var initialSolution = Array(1.0, 1.0, 1.0)

var  optProblem = new PolynomialProblem(x, ay)

var optimum = optimizer.optimize(100, optProblem, y, weights, initialSolution)

var oV = optimum.getPoint
var yrecover = new Array[Double](nP)
k = 0
while (k < nP) {
  yrecover(k) = oV(0)*x(k)*x(k)+oV(1)*x(k)+oV(2)
  k += 1
}

hold("on")
linePlotsOn
plot(new Vec(x), new Vec(yrecover), Color.BLUE, "Recovered")  
plot(x,ay)
scatterPlotsOn
plot(new Vec(x), new Vec(y), Color.GREEN, "Actual")



class PolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorialFunction {
  
    def jacobian( variables: Array[Double]) = {
        var jac = Array.ofDim[Double](x.length, 3)
        var i = 0
        while (i < x.length) {
            jac(i)(0) = x(i)*x(i)
            jac(i)(1) = x(i)
            jac(i)(2) = 1.0
            i += 1
         }
         jac
      }
      
     def  value( variables: Array[Double]) = {
         var values = new Array[Double](x.length)
         var i = 0
         while (i < values.length) {
           values(i) = (variables(0)*x(i)+variables(1))*x(i) + variables(2)
           i += 1
           }
       values
       }
       
     def jacobian() = {
         new  MultivariateMatrixFunction() {
             def value( point: Array[Double] ) =
               jacobian( point )
               }
            }            
      }
                                   
          
```


## Testing a fit both with a correct and a higher-order polynomial ##

`The example below demonstrates that the fitting of a higher order polynomial fails on testing data.  `

```
import org.apache.commons.math._
import org.apache.commons.math.analysis._ 
import org.apache.commons.math.analysis.function._ 
import org.apache.commons.math.analysis.integration._ 
import org.apache.commons.math.analysis.interpolation._ 
import org.apache.commons.math.analysis.polynomials._ 
import org.apache.commons.math.analysis.solvers._ 
import org.apache.commons.math.dfp._ 
import org.apache.commons.math.distribution._ 
import org.apache.commons.math.estimation._ 
import org.apache.commons.math.exception._ 
import org.apache.commons.math.exception.util._ 
import org.apache.commons.math.filter._ 
import org.apache.commons.math.fraction._ 
import org.apache.commons.math.genetics._ 
import org.apache.commons.math.geometry._ 
import org.apache.commons.math.geometry.euclidean.oned._ 
import org.apache.commons.math.geometry.euclidean.threed._ 
import org.apache.commons.math.geometry.euclidean.twod._ 
import org.apache.commons.math.geometry.partitioning._ 
import org.apache.commons.math.geometry.partitioning.utilities._ 
import org.apache.commons.math.linear._ 
import org.apache.commons.math.ode._ 
import org.apache.commons.math.ode.events._ 
import org.apache.commons.math.ode.nonstiff._
import org.apache.commons.math.ode.sampling._
import org.apache.commons.math.optimization._
import org.apache.commons.math.optimization.direct._
import org.apache.commons.math.optimization.fitting._
import org.apache.commons.math.optimization.general._
import org.apache.commons.math.optimization.linear._
import org.apache.commons.math.optimization.univariate._
import org.apache.commons.math.random._
import org.apache.commons.math.special._
import org.apache.commons.math.stat._
import org.apache.commons.math.stat.clustering._
import org.apache.commons.math.stat.correlation._
import org.apache.commons.math.stat.descriptive._
import org.apache.commons.math.stat.descriptive.moment._
import org.apache.commons.math.stat.descriptive.rank._
import org.apache.commons.math.stat.descriptive.summary._
import org.apache.commons.math.stat.inference._
import org.apache.commons.math.stat.ranking._
import org.apache.commons.math.stat.regression._
import org.apache.commons.math.transform._
import org.apache.commons.math.util._
import edu.jas.arith._  
import edu.jas.poly._  
import edu.jas.integrate._  


// the model function of the data. It is used to generate noise samples.
def  avalue(x: Double) =  4.5*x*x -  3.7*x+5.6

var nPtrain  = 20   // number of training set samples
var nPtest  = 40   // number of testing set samples

var x = linspace(0, 2, nPtrain)   
var ay = new Array[Double](nPtrain)   // the actual values
var dy = new Array[Double](nPtrain)   // the measured data values

var k=0
while (k < nPtrain) {
  ay(k) = avalue(x(k))  // actual value
  dy(k) = ay(k)+4*(Math.random-0.5)   // add some noise for the measured data value
  k += 1
}

// construct  test points
var tx = linspace(2, 4, nPtest)    
 
var aty = new Array[Double](nPtest)   // the actual values for test points

k = 0
while (k <  nPtest)  {
  aty(k) = avalue(tx(k))  // actual value for test points
  k += 1
  }
  
var cmy = new Array[Double](nPtest)   // the correct model predicted values

var optimizer = new LevenbergMarquardtOptimizer()

// weight vector accounting significant of points
var weights = new Array[Double](nPtrain)
k = 0
while (k < nPtrain) {  weights(k)=1.0; k += 1 }

var initialSolutionCorrectModel = Array(1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the correct model
var initialSolutionComplexModel = Array(1.0, 1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the complex model


//  construct an optimization problem using the correct model of the data
var  optCorrectModelProblem = new PolynomialProblem(x, dy)


//  construct an optimization problem using a more complex model of the data
var  optComplexModelProblem = new HOPolynomialProblem(x, dy)

var maxEval = 100
var optimumCorrectModel = optimizer.optimize(maxEval, optCorrectModelProblem, dy, weights, initialSolutionCorrectModel)
var optimumComplexModel = optimizer.optimize(maxEval, optComplexModelProblem, dy, weights, initialSolutionComplexModel)

var oV = optimumCorrectModel.getPoint  // get correct model parameters
var oVCM = optimumComplexModel.getPoint    // get complex model parameters


// evaluate models on train data
var yrecoverTrainCorrectModel = new Array[Double](nPtrain)
k = 0
while (k <  nPtrain) {
  yrecoverTrainCorrectModel(k) = oV(0)*x(k)*x(k)+oV(1)*x(k)+oV(2)
  k += 1
  }

var yrecoverTrainComplexModel = new Array[Double](nPtrain)
k = 0
while (k < nPtrain) {
  yrecoverTrainComplexModel(k) = oVCM(0)*x(k)*x(k)*x(k)+oVCM(1)*x(k)*x(k)+oVCM(2)*x(k)+oVCM(3)
  k += 1
  }


figure(1)
hold("on")
linePlotsOn
plot(x.getv, yrecoverTrainCorrectModel, "Correct Model", Color.BLUE )  
plot(x.getv, yrecoverTrainComplexModel, "Complex Model", Color.GRAY)
plot(x.getv,ay,  "Actual", Color.RED) 
 
scatterPlotsOn
plot(x.getv, dy, "Measured points", Color.GREEN)

//  evaluate models on test data
var yactualData = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yactualData(k) = avalue(tx(k))

var yrecoverTestCorrectModel = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yrecoverTestCorrectModel(k) = oV(0)*tx(k)*tx(k)+oV(1)*tx(k)+oV(2)

var yrecoverTestComplexModel = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yrecoverTestComplexModel(k) = oVCM(0)*tx(k)*tx(k)*tx(k)+oVCM(1)*tx(k)*tx(k)+oVCM(2)*tx(k)+oVCM(3)

figure(2)
hold("on")
plot(tx.getv, yactualData, "Actual Data", Color.RED)
linePlotsOn
plot(tx.getv, yrecoverTestCorrectModel, "Recovered with Correct Model", Color.BLUE)
plot(tx.getv, yrecoverTestComplexModel, "Recovered with Complex Model", Color.GRAY)
 

//  construct a Polynomial model with the correct order, i.e. 3nd degree
class PolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorialFunction {
  
    def jacobian( variables: Array[Double]) = {
        var jac = Array.ofDim[Double](x.length, 3)
        for (i <- 0  until x.length) {
            jac(i)(0) = x(i)*x(i)
            jac(i)(1) = x(i)
            jac(i)(2) = 1.0
         }
         jac
      }
      
     def  value( variables: Array[Double]) = {
         var values = new Array[Double](x.length)
         for (i <- 0 until values.length)
           values(i) = (variables(0)*x(i)+variables(1))*x(i) + variables(2)
       values
       }
       
     def jacobian() = {
         new MultivariateMatrixFunction() {
             def value( point: Array[Double] ) =
               jacobian( point )
               }
            }            
      }
      
      
          
//  construct a Higher-Order Polynomial model than the correct order, i.e. a fourth degree instead of three
class HOPolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorialFunction {
  
// the jacobian of the 4th-degree polynomial
    def jacobian( variables: Array[Double]) = {
        var jac = Array.ofDim[Double](x.length, 4)
        for (i <- 0  until x.length) {
            jac(i)(0) = x(i)*x(i)*x(i)
            jac(i)(1) = x(i)*x(i)
            jac(i)(2) = x(i)
            jac(i)(3) = 1.0
         }
         jac
      }
      
// the evaluated value of the 4th-degree polynomial
     def  value( variables: Array[Double]) = {
         var values = new Array[Double](x.length)
         for (i <- 0 until values.length)
           values(i) = variables(0)*x(i)*x(i)*x(i)+variables(1)*x(i)*x(i) + variables(2)*x(i) + variables(3)
       values
       }
       
     def jacobian() = {
         new MultivariateMatrixFunction() {
             def value( point: Array[Double] ) =
               jacobian( point )
               }
            }            
      }

```
`The same example with somewhat better user interface is listed below: `

```

import org.apache.commons.math._
import org.apache.commons.math.analysis._ 
import org.apache.commons.math.analysis.function._ 
import org.apache.commons.math.analysis.integration._ 
import org.apache.commons.math.analysis.interpolation._ 
import org.apache.commons.math.analysis.polynomials._ 
import org.apache.commons.math.analysis.solvers._ 
import org.apache.commons.math.dfp._ 
import org.apache.commons.math.distribution._ 
import org.apache.commons.math.estimation._ 
import org.apache.commons.math.exception._ 
import org.apache.commons.math.exception.util._ 
import org.apache.commons.math.filter._ 
import org.apache.commons.math.fraction._ 
import org.apache.commons.math.genetics._ 
import org.apache.commons.math.geometry._ 
import org.apache.commons.math.geometry.euclidean.oned._ 
import org.apache.commons.math.geometry.euclidean.threed._ 
import org.apache.commons.math.geometry.euclidean.twod._ 
import org.apache.commons.math.geometry.partitioning._ 
import org.apache.commons.math.geometry.partitioning.utilities._ 
import org.apache.commons.math.linear._ 
import org.apache.commons.math.ode._ 
import org.apache.commons.math.ode.events._ 
import org.apache.commons.math.ode.nonstiff._
import org.apache.commons.math.ode.sampling._
import org.apache.commons.math.optimization._
import org.apache.commons.math.optimization.direct._
import org.apache.commons.math.optimization.fitting._
import org.apache.commons.math.optimization.general._
import org.apache.commons.math.optimization.linear._
import org.apache.commons.math.optimization.univariate._
import org.apache.commons.math.random._
import org.apache.commons.math.special._
import org.apache.commons.math.stat._
import org.apache.commons.math.stat.clustering._
import org.apache.commons.math.stat.correlation._
import org.apache.commons.math.stat.descriptive._
import org.apache.commons.math.stat.descriptive.moment._
import org.apache.commons.math.stat.descriptive.rank._
import org.apache.commons.math.stat.descriptive.summary._
import org.apache.commons.math.stat.inference._
import org.apache.commons.math.stat.ranking._
import org.apache.commons.math.stat.regression._
import org.apache.commons.math.transform._
import org.apache.commons.math.util._
import edu.jas.arith._  
import edu.jas.poly._  
import edu.jas.integrate._  


// the model function of the data. It is used to generate noise samples.
def  avalue(x: Double) =  4.5*x*x -  3.7*x+5.6

var nPtrain  = 20   // number of training set samples
var nPtest  = 40   // number of testing set samples

var x = linspace(0, 2, nPtrain)   
var ay = new Array[Double](nPtrain)   // the actual values
var dy = new Array[Double](nPtrain)   // the measured data values

var k = 0
while (k < nPtrain) {
  ay(k) = avalue(x(k))  // actual value
  dy(k) = ay(k)+4*(Math.random-0.5)   // add some noise for the measured data value
  k += 1
}

// construct  test points
var tx = linspace(2, 4, nPtest)     
var aty = new Array[Double](nPtest)   // the actual values for test points

k = 0
while (k < nPtest)  {
  aty(k) = avalue(tx(k))  // actual value for test points
  k += 1
 }
  
var cmy = new Array[Double](nPtest)   // the correct model predicted values

var optimizer = new LevenbergMarquardtOptimizer()

// weight vector accounting significant of points
var weights = new Array[Double](nPtrain)
k = 0
while (k < nPtrain) {
    weights(k)=1.0
    k += 1
    }
    
var initialSolutionCorrectModel = Array(1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the correct model
var initialSolutionComplexModel = Array(1.0, 1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the complex model


//  construct an optimization problem using the correct model of the data
var  optCorrectModelProblem = new PolynomialProblem(x, dy)


//  construct an optimization problem using a more complex model of the data
var  optComplexModelProblem = new HOPolynomialProblem(x, dy)

var maxEval = 100
var optimumCorrectModel = optimizer.optimize(maxEval, optCorrectModelProblem, dy, weights, initialSolutionCorrectModel)
var optimumComplexModel = optimizer.optimize(maxEval, optComplexModelProblem, dy, weights, initialSolutionComplexModel)

var oV = optimumCorrectModel.getPoint  // get correct model parameters
var oVCM = optimumComplexModel.getPoint    // get complex model parameters


// evaluate models on train data
var yrecoverTrainCorrectModel = new Array[Double](nPtrain)
k = 0
while (k < nPtrain) {
  yrecoverTrainCorrectModel(k) = oV(0)*x(k)*x(k)+oV(1)*x(k)+oV(2)
  k += 1
  }
  

var yrecoverTrainComplexModel = new Array[Double](nPtrain)
k = 0
while (k < nPtrain) {
  yrecoverTrainComplexModel(k) = oVCM(0)*x(k)*x(k)*x(k)+oVCM(1)*x(k)*x(k)+oVCM(2)*x(k)+oVCM(3)
  k += 1
  }


figure(1)
hold("on")
linePlotsOn
plot(new Vec(x), new Vec(yrecoverTrainCorrectModel), "Correct Model", Color.BLUE )  
plot(new Vec(x), new Vec(yrecoverTrainComplexModel), "Complex Model", Color.GRAY)
plot(new Vec(x), new Vec(ay),  "Actual", Color.RED) 
 
scatterPlotsOn
plot(new Vec(x), new Vec(dy), "Measured points", Color.GREEN)

//  evaluate models on test data
var yactualData = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yactualData(k) = avalue(tx(k))

var yrecoverTestCorrectModel = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yrecoverTestCorrectModel(k) = oV(0)*tx(k)*tx(k)+oV(1)*tx(k)+oV(2)

var yrecoverTestComplexModel = new Array[Double](nPtest)
for (k<-0 until nPtest)
  yrecoverTestComplexModel(k) = oVCM(0)*tx(k)*tx(k)*tx(k)+oVCM(1)*tx(k)*tx(k)+oVCM(2)*tx(k)+oVCM(3)

figure(2)
hold("on")
plot(new Vec(tx), new Vec(yactualData), "Actual Data", Color.RED)
linePlotsOn
plot(new Vec(tx), new Vec(yrecoverTestCorrectModel), "Recovered with Correct Model", Color.BLUE)
plot(new Vec(tx), new Vec(yrecoverTestComplexModel), "Recovered with Complex Model", Color.GRAY)
 

//  construct a Polynomial model with the correct order, i.e. 3nd degree
class PolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorialFunction {
  
    def jacobian( variables: Array[Double]) = {
        var jac = Array.ofDim[Double](x.length, 3)
        var i = 0
        while (i < x.length) {
            jac(i)(0) = x(i)*x(i)
            jac(i)(1) = x(i)
            jac(i)(2) = 1.0
            i += 1
         }
         jac
      }
      
     def  value( variables: Array[Double]) = {
         var values = new Array[Double](x.length)
         var i = 0
         while (i < values.length) {
           values(i) = (variables(0)*x(i)+variables(1))*x(i) + variables(2)
           i += 1
           }
       values
       }
       
     def jacobian() = {
         new MultivariateMatrixFunction() {
             def value( point: Array[Double] ) =
               jacobian( point )
               }
            }            
      }
      
      
          
//  construct a Higher-Order Polynomial model than the correct order, i.e. a fourth degree instead of three
class HOPolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorialFunction {
  
// the jacobian of the 4th-degree polynomial
    def jacobian( variables: Array[Double]) = {
        var jac = Array.ofDim[Double](x.length, 4)
        var i = 0
        while (i < x.length) {
            jac(i)(0) = x(i)*x(i)*x(i)
            jac(i)(1) = x(i)*x(i)
            jac(i)(2) = x(i)
            jac(i)(3) = 1.0
            i += 1
         }
         jac
      }
      
// the evaluated value of the 4th-degree polynomial
     def  value( variables: Array[Double]) = {
         var values = new Array[Double](x.length)
         var i = 0
         while (i < values.length) {
           values(i) = variables(0)*x(i)*x(i)*x(i)+variables(1)*x(i)*x(i) + variables(2)*x(i) + variables(3)
           i += 1
           }
       values
       }
       
     def jacobian() = {
         new MultivariateMatrixFunction() {
             def value( point: Array[Double] ) =
               jacobian( point )
               }
            }            
      }
```