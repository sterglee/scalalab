# Introduction #

`The new version of Apache Commons is significantly improved and is included by default within the core of ScalaLab libraries. We provide here examples. `


## Polynomial model fitting with Levenberg Marquardt ##

```

// Polynomial model fitting with Levenberg Marquardt 
// that example illustrates the poor performance of a more complex model than 
// the correct one (i.e. a case of overtraining)

import org.apache.commons.math3._
import org.apache.commons.math3.optimization._
import org.apache.commons.math3.analysis._
import org.apache.commons.math3.optimization.general._

close("all")
 
// the model function of the data. It is used to generate noise samples.
def  avalue(x: Double) =  4.5*x*x -  3.7*x+5.6

var nPtrain  = getInt("Number of points to fit the polynomial", 4)  // number of training set samples
var nPtest  = getInt("Number of points to test the fitted polynomial", 20)  // number of testing set samples

var x = linspace(0, 2, nPtrain)   
var ay = new Array[Double](nPtrain)   // the actual values
var dy = new Array[Double](nPtrain)   // the measured data values

for (k<-0 until nPtrain) {
  ay(k) = avalue(x(k))  // actual value
  dy(k) = ay(k)+4*(Math.random-0.5)   // add some noise for the measured data value
}

// construct  test points
var tx = linspace(2, 4, nPtest)     
var aty = new Array[Double](nPtest)   // the actual values for test points

for (k<-0 until nPtest) 
  aty(k) = avalue(tx(k))  // actual value for test points
  
var cmy = new Array[Double](nPtest)   // the correct model predicted values

var optimizer = new LevenbergMarquardtOptimizer();
var figTitle = "Levenberg-Marquardt Optimizer"

// weight vector accounting significant of points
var weights = new Array[Double](nPtrain)
for (k<-0 until nPtrain) weights(k)=1.0
var initialSolutionCorrectModel = Array(1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the correct model
var initialSolutionComplexModel = Array(1.0, 1.0, 1.0, 1.0)   // initial solution for the uknown parameters for the complex model

//  construct an optimization problem using the correct model of the data
var  optCorrectModelProblem = new PolynomialProblem(x, dy)

//  construct an optimization problem using a more complex model of the data
var  optComplexModelProblem = new HOPolynomialProblem(x, dy)

var maxEval = 100
tic
var optimumCorrectModel = optimizer.optimize(maxEval, optCorrectModelProblem, dy, weights, initialSolutionCorrectModel)
var optimumComplexModel = optimizer.optimize(maxEval, optComplexModelProblem, dy, weights, initialSolutionComplexModel)
var tmOpt = toc
var oV = optimumCorrectModel.getPoint  // get correct model parameters
var oVCM = optimumComplexModel.getPoint    // get complex model parameters


// evaluate models on train data
var yrecoverTrainCorrectModel = new Array[Double](nPtrain)
for (k<-0 until nPtrain)
  yrecoverTrainCorrectModel(k) = oV(0)*x(k)*x(k)+oV(1)*x(k)+oV(2)

var yrecoverTrainComplexModel = new Array[Double](nPtrain)
for (k<-0 until nPtrain)
  yrecoverTrainComplexModel(k) = oVCM(0)*x(k)*x(k)*x(k)+oVCM(1)*x(k)*x(k)+oVCM(2)*x(k)+oVCM(3)


figure(1)
hold("on")
linePlotsOn
plot(x, yrecoverTrainCorrectModel, "Correct Model", Color.BLUE )  
plot(x, yrecoverTrainComplexModel, "Complex Model", Color.GRAY)
plot(x,ay,  "Actual", Color.RED)
 
scatterPlotsOn
plot(x, dy, "Measured points", Color.GREEN)

title("Fig 1 : Fitting on Training Data :  "+figTitle+" time to train "+tmOpt)

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
plot(tx, yactualData, "Actual Data", Color.RED)
linePlotsOn
plot(tx, yrecoverTestCorrectModel, "Recovered with Correct Model", Color.BLUE)
plot(tx, yrecoverTestComplexModel, "Recovered with Complex Model", Color.GRAY)

title("Fig 2 : Generalization Performance : "+figTitle) 

//  construct a Polynomial model with the correct order, i.e. 3nd degree
class PolynomialProblem( x: Array[Double], y: Array[Double])
   extends AnyRef with DifferentiableMultivariateVectorFunction {
  
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
   extends AnyRef with DifferentiableMultivariateVectorFunction {
  
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


## Polynomial fitting example ##

```


import java.util.Random

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction.Parametric
import org.apache.commons.math3.exception.ConvergenceException
import org.apache.commons.math3.exception.TooManyEvaluationsException
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer
import org.apache.commons.math3.optimization.SimpleVectorValueChecker
import org.apache.commons.math3.util.FastMath
import org.apache.commons.math3.distribution.RealDistribution
import org.apache.commons.math3.distribution.UniformRealDistribution
import org.apache.commons.math3.optimization.fitting._


    def buildRandomPolynomial(degree: Int, randomizer: Random) =  {
        var  coefficients = new Array[Double](degree + 1)
        for (i <- 0 to degree) {
            coefficients(i) = randomizer.nextGaussian()
        }
         new PolynomialFunction(coefficients)
    }

        var rng = new UniformRealDistribution(-100, 100)
        rng.reseedRandomGenerator(64925784252L)

        var   optim = new LevenbergMarquardtOptimizer()
        var   fitter = new PolynomialFitter(optim)
        
        var  coeff = Array(12.9, -3.4, 2.1)  // 12.9 - 3.4 x + 2.1 x^2
        var   f = new PolynomialFunction(coeff)

        // Collect data from a known polynomial.
        for (i<-0 until 100) {
            var  x = rng.sample()
            fitter.addObservedPoint(x, f.value(x))
        }

        // Start fit from initial guesses that are far from the optimal values.
        var   best = fitter.fit(Array( -1e-20, 3e15, -5e25 ))

  // here the "best" coefficients should be approached the original, we verify that
        var normDiff = norm(best-coeff)
	   println("norm of the difference between the actual polynomial coefficients and the actual is: "+normDiff)
	   if (normDiff < 0.00001) println("SUCCESS in recovering coefficients")
	   
	   
        // testNoError() 
        var  randomizer = new Random(64925784252l)
        for (degree <- 1 until 10) {
            var  p = buildRandomPolynomial(degree, randomizer)

            var   fitter = new PolynomialFitter(new LevenbergMarquardtOptimizer())
            for (i <- 0 to degree) {
                fitter.addObservedPoint(1.0, i, p.value(i))
            }

            var  init = new Array[Double](degree + 1)
            var  fitted = new PolynomialFunction(fitter.fit(init))

		  var x = -1.0
		  while (x < 1.0) {
               var  error = FastMath.abs(p.value(x) - fitted.value(x)) / (1.0 + FastMath.abs(p.value(x)))
                x += 0.01               
                println("error = "+error+" in  fitting polynomial degree "+degree)
            }
        }
  
```