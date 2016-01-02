**`Most of that material is explained best at the new ScalaLab user guide, January 2015 version`**

## `Descriptive Statistics` ##

```

import org.apache.commons.math3.stat.descriptive._
var stats = new DescriptiveStatistics
var inputArray = Array(0.2, 0.3444, -4.556, 0.344, 2.345)
// add the data from the array
for (i<-0 until inputArray.length)
 stats.addValue(inputArray(i))
 
 // Compute some statistics
var mean = stats.getMean
var std = stats.getStandardDeviation
var kurtosis = stats.getKurtosis

```

## `Summary Statistics ` ##

```

import org.apache.commons.math3.stat.descriptive._



// Get a SummaryStatistics instance
var stats = new SummaryStatistics
// read data from a List of values
// adding values and updating sums, counters, etc.
var listOfValues = List(0.333, -2.34, 4.333, 0.3333, -0.33322)
var inputArray = Array(0.2, 0.3444, -4.556, 0.344, 2.345)
// add the data from the array
listOfValues foreach ( stats.addValue(_) )
 
 // Compute some statistics
var mean = stats.getMean
var std = stats.getStandardDeviation

```

## `Using the StatUtils class` ##
```

import org.apache.commons.math3.stat._

var arrayOfValues = Array(0.333, -2.34, 4.333, 0.3333, -0.33322)
var mean = StatUtils.mean(arrayOfValues)
var std = StatUtils.variance(arrayOfValues)
var median = StatUtils.percentile(arrayOfValues, 50.0)
// compute the mean of the first three values in the array
var mn = StatUtils.mean(arrayOfValues, 0, 3)


```


## `Frequency Distributions ` ##

### `Compute a frequency distribution based on integer values` ###
```

import org.apache.commons.math3.stat._

var f = new Frequency

f.addValue(1)
f.addValue(new Integer(1))
f.addValue(2)
f.addValue(new Integer(-1))
println( f.getCount(1))
println( f.getCumPct(0))
println(f.getPct(new Integer(1)))
println(f.getCumPct(-2))
println(f.getCumPct(10))


```

### `Count string frequencies Using case-sensitive comparison, alpha sort order (natural comparator) ` ###

```


import org.apache.commons.math3.stat._
var f = new Frequency
f.addValue("one")
f.addValue("One")
f.addValue("oNe")
f.addValue("Z")
println(f.getCount("one"))
println(f.getCumPct("Z"))
println(f.getCumPct("Ot"))

```


### `Count string frequencies Using case-insensitive comparison` ###

```
import org.apache.commons.math3.stat._
var f = new Frequency(String.CASE_INSENSITIVE_ORDER)
f.addValue("one")
f.addValue("One")
f.addValue("oNe")
f.addValue("Z")
println(f.getCount("one"))
println(f.getCumPct("Z"))
println(f.getCumPct("Ot"))

```


## `Simple Regression` ##

```
import org.apache.commons.math3.stat.regression._
var regression = new SimpleRegression()
regression.addData(1d, 2d)
// At this point, with only one observation, all regression statistics will return NaN
regression.addData(3d, 3d)
// with only two observations, slope and intercept can be computed 
// but inference statistics will return NaN
regression.addData(3d, 3d)
// Compute some statistics based on observations added so far
// displays the intercept of regression line
println(regression.getIntercept())
// displays slope of regression line
println(regression.getSlope())
// displays slope standard error
println(regression.getSlopeStdErr())

```

`Use the regression model to predict the y value for a new x value `

```
// displays predicted y value for x = 1.5
println(regression.predict(1.5d))
```


## `Another regression example` ##

```

import org.apache.commons.math3.stat.regression._
var data = Array(Array(1.0, 3.0), Array(2.0, 5.0), Array(3, 7.0), Array(4.0, 14), Array(5.0, 11.0))
var regression = new SimpleRegression()
regression.addData(data)
// estimate regression model based on data
// displays the intercept of regression line
println(regression.getIntercept())
// displays slope of regression line
println(regression.getSlope())
// displays slope standard error
println(regression.getSlopeStdErr())

```

## `OLS Regression` ##

```

import org.apache.commons.math3.stat.regression._
var regression = new OLSMultipleLinearRegression()
var y = Array(11.0, 12.0, 13.0, 14.0, 15.0, 16.0)
var x = new Array[Array[Double]](6)
x(0) = Array(0.0, 0.0, 0.0, 0.0, 0.0)
x(1) = Array(2.0, 0, 0, 0, 0)
x(2) = Array(0, 3.0, 0, 0, 0)
x(3) = Array(0, 0, 4.0, 0, 0)
x(4) = Array(0, 0, 0, 5.0, 0)
x(5) = Array(0, 0, 0, 0, 6.0)
regression.newSampleData(y, x)
// get regression parameters and diagnostics
var beta = regression.estimateRegressionParameters()
var residuals = regression.estimateResiduals()
var parametersVariance = regression.estimateRegressionParametersVariance()
var regressandVariance = regression.estimateRegressandVariance()
var rSsquared = regression.calculateRSquared()
var sigma = regression.estimateRegressionStandardError()

```

## `Rank Transformations ` ##

```
import org.apache.commons.math3.stat.ranking._

var ranking = new NaturalRanking(NaNStrategy.MINIMAL, TiesStrategy.MAXIMUM)
var data = Array(20, 17, 30, 42.3, 17, 50, Double.NaN, java.lang.Double.NEGATIVE_INFINITY, 17) 
var ranks = ranking.rank(data)

```

## `Covariance` ##

```
import org.apache.commons.math3.stat.correlation._
var N = 40
var x = vrand(N).getv  // a random Array[Double]
var y = vrand(N).getv  // a second one
// unbiased covariance
var unbiasedCov = new Covariance().covariance(x, y)
// non-biased corrected covariance
var nonBiasedCorrectedCov = new Covariance().covariance(x, y, false)


```


## `Random Vector Generation ` ##
```

import org.apache.commons.math3.random._

var randomData = new RandomDataImpl()
var rv = new Vec(1000)
for (i <- 0 until 1000) 
   rv(i) = randomData.nextLong(1, 1000000)
plot(rv)
```


```


// generate vectors with uncorrelated components.
// Components of generated vectors follow (independent)
// Gaussian distributions, with parameters supplied in the constructor

import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.random.UniformRandomGenerator

// build an uncorrelated random vector generator from
// its mean and standard deviation vectors
// means: expected mean values for each component
// stds: standard deviation for each component
// generator: underlying generator for uncorrelated normalized components


var means = Array(-3.4, 4.5, 6,9)  // the Gaussian means
var stds = Array(2.3, 8.9, 13.4, 7.65)  // the standard deviation vector
var generator = new UniformRandomGenerator(new JDKRandomGenerator)
var uncorrelatedRandomGen = new UncorrelatedRandomVectorGenerator(means, stds, generator)


// create now samples
var nsamples = 2000
var samples = Array.ofDim[Double](4, nsamples)
for (k<-0 until nsamples) {
     
  var sample = uncorrelatedRandomGen.nextVector
  samples( 0, k) = sample(0)
  samples( 1, k) = sample(1)
  samples( 2, k) = sample(2)
  samples( 3, k) = sample(3)
  
  }
  
  figure(1)
  subplot(4,1,1)
  plot(samples(0,::))
  subplot(4,1,2)
  plot(samples(1,::))
  subplot(4,1,3)
  plot(samples(2,::))
  subplot(4,1,4)
  plot(samples(3,::))
  
```


**`Bivariate Normal Distribution example`**

```
import org.apache.commons.math3.random._
import org.apache.commons.math3.linear._
// Create and seed a RandomGenerator (could use any of the generators in the random package here)
var rg = new JDKRandomGenerator()
rg.setSeed(17399225432l)  // Fixed seed means same results every time
// Create a GaussianRandomGenerator using rg as its source of randomness
var rawGenerator = new GaussianRandomGenerator(rg)
var mean = Array(1.0, 2.0)
var c = -2.0
var covDarr = Array( Array(9, c), Array(c, 16))
var covariance = new Array2DRowRealMatrix(covDarr)
// Create a CorrelatedRandomVectorGenerator using rawGenerator for the components
var generator = new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-12 * covariance.getNorm(), rawGenerator)
// Use the generator to generate correlated vectors
var Nvecs = 500
var vecsAll = Array.ofDim[Double](2, Nvecs)
for (k<-0 until Nvecs) {
  var randomVector = generator.nextVector()
  vecsAll(0)(k) = randomVector(0)
  vecsAll(1)(k) = randomVector(1)
} 
figure(1); subplot(3,1,1);  plot(vecsAll(0)); subplot(3,1,2); plot(vecsAll(1));
subplot(3,1,3); scatterPlotsOn; plot(vecsAll(0), vecsAll(1))

```

## `EigenDecomposition` ##

```

import org.apache.commons.math3.linear.EigenDecomposition
import org.apache.commons.math3.linear.Array2DRowRealMatrix


// make a Java 2D array
var a2d = Array(
             Array(0.1, -0.3, 0.55, 1.3),
             Array(-0.7, -0.56, 1.23, 0.667),
             Array(-1.23, 0.9, -0.5, 1.8),
             Array(9.4, 0.44, -0.3, 9.2)
             )
             
// construct with it an Apache Common Maths Array2DRowRealMatrix             
var rm = new Array2DRowRealMatrix(a2d)

// construct an eigendecomposition object
var splitTolerance = 0.001
var ed = new EigenDecomposition(rm, splitTolerance)

var realEigenvalues = ed.getRealEigenvalues
var imagEigenvalues = ed.getImagEigenvalues

var eigvecs  = ed.getV




```

## Gumbel Distribution ##

```

import org.apache.commons.math3.distribution.GumbelDistribution


var mu = 7.8
var beta = 2.3


var gd = new GumbelDistribution(mu, beta)


var x =  0::0.01::30

var ydensity = x map  gd.density

 figure(1)
 plot(x, ydensity, "Gumbel Density")

var ydistribution = x map gd.cumulativeProbability
 figure(2)
 plot(x, ydistribution, "Gumbel Cumulatve Probability")

```

## Binomial Distribution ##

```
import org.apache.commons.math3.distribution.BinomialDistribution


var numberOftrials = 8
var probabilityOfSuccess = 0.9


// construct a Binomial Distribution
var bd = new BinomialDistribution(numberOftrials, probabilityOfSuccess)


var N=20
var x =  new Array[Int](N)
for (k <- 0 until N) x(k) = k

var y = new Array[Double](N)
for (k<-0 until N) y(k) = bd.probability(x(k))


 figure(1)
 plot(y, "Binomial Probability, numberOfTrials = "+numberOftrials+", probabilityOfSuccess = "+probabilityOfSuccess)

```


# Curve Fitting #
## Overview ##
`The fitting package deals with curve fitting for univariate real functions. When a univariate real function y = f(x) does depend on some unknown parameters p_0,  p_1..., pn-1, curve fitting can be used to find these parameters. It does this by fitting the curve so it remains very close to a set of observed points (x0, y0), (x1, y1) ... (xk-1, yk-1). This fitting is done by finding the parameters values that minimizes the objective function Σ(yi - f(xi))2. This is actually a least-squares problem.`

`For all provided curve fitters, the operating principle is the same. Users must first create an instance of the fitter, then add the observed points and once the complete sample of observed points has been added they must call the fit method which will compute the parameters that best fit the sample. A weight is associated with each observed point, this allows to take into account uncertainty on some points when they come from loosy measurements for example. If no such information exist and all points should be treated the same, it is safe to put 1.0 as the weight for all points.`


```
  // This example is Groovy code, thus execute it with Groovy mode, i.e. with F8 
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer
import org.apache.commons.math3.analysis.ParametricUnivariateFunction
import org.apache.commons.math3.util.FastMath
import org.apache.commons.math3.fitting.*
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction

         CurveFitter fitter = new CurveFitter(new LevenbergMarquardtOptimizer())
fitter.addObservedPoint(-1.00, 2.021170021833143)
fitter.addObservedPoint(-0.99, 2.221135431136975)
fitter.addObservedPoint(-0.98, 2.09985277659314)
fitter.addObservedPoint(-0.97, 2.0211192647627025)
// ... Lots of lines omitted ...
fitter.addObservedPoint( 0.99, -2.4345814727089854)

// The degree of the polynomial is deduced from the length of the array containing
// the initial guess for the coefficients of the polynomial.
init = [ 12.9, -3.4, 2.1 ] as  double [] // 12.9 - 3.4 x + 2.1 x^2

// Compute optimal coefficients.
best = fitter.fit(new PolynomialFunction.Parametric(), init)

// Construct the polynomial that best fits the data.
fitted = new PolynomialFunction(best)
        
```

## Kalman Filter ##

```

  // This example is Groovy code, thus execute it with Groovy mode, i.e. with F8 

import org.apache.commons.math3.filter.*

import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.MatrixDimensionMismatchException
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.util.Precision

        // simulates a vehicle, accelerating at a constant rate (0.1 m/s)

        // discrete time interval
        dt = 0.1d
        // position measurement noise (meter)
        measurementNoise = 10d
        // acceleration noise (meter/sec^2)
        accelNoise = 0.2d

        // A = [ 1 dt ]
        //     [ 0  1 ]
        A = new Array2DRowRealMatrix( [ [1, dt ], [ 0, 1 ]] as double [][])

        // B = [ dt^2/2 ]
        //     [ dt     ]
        B = new Array2DRowRealMatrix([ [Math.pow(dt, 2d) / 2d ], [ dt ]] as double [][])

        // H = [ 1 0 ]
        H = new Array2DRowRealMatrix([ [1d, 0d ]] as double [][])

        // x = [ 0 0 ]
        x = new ArrayRealVector( [0, 0] as double [])

        tmp = new Array2DRowRealMatrix(  [ [ Math.pow(dt, 4d) / 4d, Math.pow(dt, 3d) / 2d ],
                                 [ Math.pow(dt, 3d) / 2d, Math.pow(dt, 2d) ] ] as double [][])

        // Q = [ dt^4/4 dt^3/2 ]
        //     [ dt^3/2 dt^2   ]
        Q = tmp.scalarMultiply(Math.pow(accelNoise, 2))

        // P0 = [ 1 1 ]
        //      [ 1 1 ]
        P0 = new Array2DRowRealMatrix([ [ 1, 1 ],[ 1, 1 ]] as double [][])

        // R = [ measurementNoise^2 ]
        R = new Array2DRowRealMatrix( [  Math.pow(measurementNoise, 2) ] as double [])

        // constant control input, increase velocity by 0.1 m/s per cycle
        u = new ArrayRealVector( [ 0.1d] as double[])
        
        pm = new DefaultProcessModel(A, B, Q, x, P0)
        
        mm = new DefaultMeasurementModel(H, R)
        
        filter = new KalmanFilter(pm, mm)

	  shouldBe1 =  filter.getMeasurementDimension()    // should be 1
       shouldBe2 = filter.getStateDimension()   //  should be 2

        P0data = P0.getData() 
        FilterErrorCovariance =  filter.getErrorCovariance()
        P0dataEqFilterErrorCovariance = P0data-FilterErrorCovariance  // should be zero
        

        // check the initial state
        expectedInitialState = [ 0.0, 0.0 ] as double []
        stateEstimation = filter.getStateEstimation()
        stateEstimationEqExpectedInitialState = stateEstimation - expectedInitialState  // should be zero
        

        randg = new JDKRandomGenerator()

        tmpPNoise = new ArrayRealVector( [  Math.pow(dt, 2d) / 2d, dt ] as double [])

        mNoise = new ArrayRealVector(1)

        // iterate 60 steps
        for (int i = 0; i < 60; i++) {
            filter.predict(u)

            // Simulate the process
             pNoise = tmpPNoise.mapMultiply(accelNoise * randg.nextGaussian())

            // x = A * x + B * u + pNoise
            x = A.operate(x).add(B.operate(u)).add(pNoise)

            // Simulate the measurement
            mNoise.setEntry(0, measurementNoise * randg.nextGaussian())

            // z = H * x + m_noise
            z = H.operate(x).add(mNoise)

            filter.correct(z)

            // state estimate shouldn't be larger than the measurement noise
            diff = Math.abs(x.getEntry(0) - filter.getStateEstimation()[0])
            println("diff = "+diff)
        }


```