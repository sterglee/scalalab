# Introduction #
`The Apache Common Math is a powerful, elegant library for Java. It is included in the default ScalaLab installation. This page adapts material from its user guide for ScalaLab. The Java code of the user guide is implemented in ScalaSci that is both scriptable and higher-level. In essence, we adapt the Java examples, to Scala code executed conveniently within the scripting environment of ScalaLab. Since Apache Common Math is a very useful library, ScalaLab performs by default the relevant imports to facilitate the user.`

**` NOTICE:  the adaptation of the user guide is in work, not yet finished, but the presented examples have been tested and operate well with ScalaLab291 `**


` In order to use the Apache common maths library the following imports are performed implicitly. Therefore the ScalaLab programmer can refer conveniently to the routines of the Apache common maths library with their short names. `

```
import org.apache.commons.math._
import org.apache.commons.math.analysis._ 
import org.apache.commons.math.analysis.function._ 
import org.apache.commons.math.analysis.integration._ 
import org.apache.commons.math.analysis.interpolation._ 
import org.apache.commons.math.analysis.polynomials._ 
import org.apache.commons.math.analysis.solvers._ 
import org.apache.commons.math.complex._ 
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
    
```

## Complex numbers ##

`The complex number class of ScalaLab is based on Apache Commons Complex number. Here are some examples of its use: `

```

 var tstCN = new Complex(2.3, 9.3)
 var ntstCN = -tstCN
 tstCN.abs  // the absolute value of this complex number
 var tstCN2 = new Complex(3, 10.0)
 var sumComplex = tstCN+tstCN2
  var conjTstCN = tstCN.conjugate
  var mulCompl = tstCN*ntstCN
  var divCompl = tstCN / tstCN
  
  
   // test some standard math. functions
  var acos_tstCN = tstCN.acos
  var asin_tstCN = tstCN.asin
  var atan_tstCN = tstCN.atan
  var cos_tstCN = tstCN.cos
  var tan_tstCN = tstCN.tan
  var sin_tstCN = tstCN.sin
  var cosh_tstCN = tstCN.cosh
  var sinh_tstCN = tstCN.sinh
  var tanh_tstCN = tstCN.tanh
  var sqrt_tstCN = tstCN.sqrt
  var exp_tstCN = tstCN.exp
  var log_tstCN = tstCN.log
  var pow_tstCN_tstCN = tstCN.pow(tstCN)
  var nthRoots_tstCN = tstCN.nthRoot(4)
  
```


# 1. Statistics #

> ## 1.1 Overview ##

`The statistics package provides frameworks and implementations for basic Descriptive statistics, frequency distributions, bivariate regression, and t-, chi-square and ANOVA test statistics.`

## 1.2 Descriptive Statistics ##

`The stat package includes a framework and default implementations for the following Descriptive statistics: `
  * `arithmetic and geometric means`
  * `variance and standard deviation`
  * `sum, product, log sum, sum of squared values`
  * `minimum, maximum, median, and percentiles`
  * `skewness and kurtosis`
  * `first, second, third and fourth moments`

`With the exception of percentiles and the median, all of these statistics can be computed without maintaining the full list of input data values in memory. The stat package provides interfaces and implementations that do not require value storage as well as implementations that operate on arrays of stored values.`

`The top level interface is ` _`UnivariateStatistic`_`. This interface, implemented by all statistics, consists of evaluate() methods that take Array[Double] as arguments and return the value of the statistic. This interface is extended by  ` _`StorelessUnivariateStatistic`_ `, which adds increment(), getResult() and associated methods to support "storageless" implementations that maintain counters, sums or other state information as values are added using the increment() method.`

`Abstract implementations of the top level interfaces are provided in`  _`AbstractUnivariateStatistic`_ ` and ` _`AbstractStorelessUnivariateStatistic`_` respectively.`

`Each statistic is implemented as a separate class, in one of the subpackages (moment, rank, summary) and each extends one of the abstract classes above (depending on whether or not value storage is required to compute the statistic). There are several ways to instantiate and use statistics. Statistics can be instantiated and used directly, but it is generally more convenient (and efficient) to access them using the provided aggregates, ` _`DescriptiveStatistics`_ ` and `_`SummaryStatistics.`_

_`DescriptiveStatistics`_ ` maintains the input data in memory and has the capability of producing "rolling" statistics computed from a "window" consisting of the most recently added values. `

_`SummaryStatistics`_ ` does not store the input data values in memory, so the statistics included in this aggregate are limited to those that can be computed in one pass through the data without access to the full array of values.`

| **`Aggregate`** | **`Statistics Included`** | **`Values stored?`** | **`"Rolling" capability?`** |
|:----------------|:--------------------------|:---------------------|:----------------------------|
| `DescriptiveStatistics` | `min, max, mean, geometric mean, n, sum, sum of squares, standard deviation, variance, percentiles, skewness, kurtosis, median` | `Yes`                | `Yes`                       |
| `SummaryStatistics` | `min, max, mean, geometric mean, n, sum, sum of squares, standard deviation, variance` | `No`                 | `No`                        |

_`SummaryStatistics`_ `can be aggregated using ` _`AggragateSummaryStatistics.`_ `This class can be used to concurrently gather statistics for multiple datasets as well as for a combined sample including all of the data.`

`Neither ` _`DescriptiveStatistics`_ nor _`SummaryStatistics`_ `is thread-safe. `

_`SynchronizedDescriptiveStatistics`_ ` and ` _`SynchronizedSummaryStatistics`_ `, respectively, provide thread-safe versions for applications that require concurrent access to statistical aggregates by multiple threads. ` _`SynchronizedMultivariateSummeryStatistics`_ `provides thread-safe ` _`MultivariateSummaryStatistics.`_

`There is also a utility class, `_`StatUtils,`_ `that provides static methods for computing statistics directly from Array[Double].`

`Here are some examples showing how to compute Descriptive statistics.`

**`Compute summary statistics for a list of double values`**


`Using the ` _`DescriptiveStatistics`_ `aggregate (values are stored in memory): `

```

// Get a DescriptiveStatistics instance
var stats = new DescriptiveStatistics

var inputArray = Array(0.2, 0.3444, -4.556, 0.344, 2.345)
// add the data from the array
for (i<-0 until inputArray.length)
   stats.addValue(inputArray(i))
   
   // Compute some statistics
var mean = stats.getMean
var std = stats.getStandardDeviation
var kurtosis  = stats.getKurtosis
   
```

`Using the ` _`SummaryStatistics`_ `aggregate (values are ` **`not`** `stored in memory):`

```


// Get a SummaryStatistics instance
var stats = new SummaryStatistics

// read data from a List of values
// adding values and updating sums, counters, etc.

var listOfValues = List(0.333, -2.34, 4.333, 0.3333, -0.33322)

var inputArray = Array(0.2, 0.3444, -4.556, 0.344, 2.345)
// add the data from the array

listOfValues foreach  (  stats.addValue(_) )
   
   // Compute some statistics
var mean = stats.getMean
var std = stats.getStandardDeviation
```

`Using the ` _`StatUtils`_ `utility class:`
```


var arrayOfValues = Array(0.333, -2.34, 4.333, 0.3333, -0.33322)

var mean = StatUtils.mean(arrayOfValues)
var std = StatUtils.variance(arrayOfValues)
var median = StatUtils.percentile(arrayOfValues, 50.0)

// compute the mean of the first three values in the array
var mn = StatUtils.mean(arrayOfValues, 0, 3)

```


**`Compute statistics in a thread-safe manner`**

`Use a ` _`SynchronizedDescriptiveStatistics`_ instance
```
// Create a SynchronizedDescriptiveStatistics instance and
// use it as any other DescriptiveStatistics instance
var stats: DescriptiveStatistics = new SynchronizedDescriptiveStatistics()
```


**`Frequency distributions`**

**`Frequency`** `provides a simple interface for maintaining counts and percentages of discrete values. `

`Strings, integers, longs and chars are all supported as value types, as well as instances of any class that implements ` _`Comparable`_`. The ordering of values used in computing cumulative frequencies is by default the ` _`natural ordering`_`, but this can be overriden by supplying a ` _`Comparator`_ `to the constructor. Adding values that are not comparable to those that have already been added results in an ` _`IllegalArgumentException.`_

`Here are some examples. `

**`Compute a frequency distribution based on integer values.`**

```


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


**`Count string frequencies`**

`Using case-sensitive comparison, alpha sort order (natural comparator): `
```

var f = new Frequency
f.addValue("one")
f.addValue("One")
f.addValue("oNe")
f.addValue("Z")
println(f.getCount("one"))
println(f.getCumPct("Z"))
println(f.getCumPct("Ot"))
```

`Using case-insensitive comparator:`
```

var f = new Frequency(String.CASE_INSENSITIVE_ORDER)
f.addValue("one")
f.addValue("One")
f.addValue("oNe")
f.addValue("Z")
println(f.getCount("one"))
println(f.getCumPct("Z"))
println(f.getCumPct("Ot"))
```

## 1.4. Simple regression ##

_`SimpleRegression`_ `provides ordinary least squares regression with one independent variable estimating the linear model:`

`y = intercept + slope * x`

`Standard errors for intercept and slope are available as well as ANOVA, r-square and Pearson's r statistics.`

`Observations (x,y pairs) can be added to the model one at a time or they can be provided in a 2-dimensional array. The observations are not stored in memory, so there is no limit to the number of observations that can be added to the model.`

**`Usage Notes:`**

  * `When there are fewer than two observations in the model, or when there is no variation in the x values (i.e. all x values are the same) all statistics return NaN. At least two observations with different x coordinates are requred to estimate a bivariate regression model.`
  * `getters for the statistics always compute values based on the current set of observations -- i.e., you can get statistics, then add more data and get updated statistics without using a new instance. There is no "compute" method that updates all statistics. Each of the getters performs the necessary computations to return the requested statistic.`

**`Implementation Notes:`**

  * `As observations are added to the model, the sum of x values, y values, cross products (x times y), and squared deviations of x and y from their respective means are updated using updating formulas defined in "Algorithms for Computing the Sample Variance: Analysis and Recommendations", Chan, T.F., Golub, G.H., and LeVeque, R.J. 1983, American Statistician, vol. 37, pp. 242-247, referenced in Weisberg, S. "Applied Linear Regression". 2nd Ed. 1985. All regression statistics are computed from these sums.`
  * `Inference statistics (confidence intervals, parameter significance levels) are based on the assumption that the observations included in the model are drawn from a ` _`Bivariate Normal Distribution`_

`Here are some examples. `

```

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

`Use the regression model to predict the y value for a new x value`
```
// displays predicted y value for x = 1.5
println(regression.predict(1.5d))
```

`More data points can be added and subsequent getXxx calls will incorporate additional data in statistics`

**`Estimate a model from a double[][] array of data points`**

`Instantiate a regression object and load dataset`

```

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

`More data points -- even another double[][] array -- can be added and subsequent getXxx calls will incorporate additional data in statistics.`

## `Multiple linear regression ` ##

_`OLSMultipleLinearRegression`_ ` and ` _`GLSMultipleLinearRegression`_ `provide least squares regression to fit the linear model:`

`Y=X*b+u`

`where Y is an n-vector ` _`regressand`_`, X is a [n,k] matrix whose k columns are called `_`regressors`_`, b is k-vector of ` _`regression parameters`_ `and u is an n-vector of error terms or residuals.`

_`OLSMultipleLinearRegression`_ `provides Ordinary Least Squares Regression, and ` _`GLSMultipleLinearRegression`_ `implements Generalized Least Squares. See the javadoc for these classes for details on the algorithms and formulas used.`

`Data for OLS models can be loaded in a single double[] array, consisting of concatenated rows of data, each containing the regressand (Y) value, followed by regressor values; or using a double[][] array with rows corresponding to observations. GLS models also require a double[][] array representing the covariance matrix of the error terms. See AbstractMultipleLinearRegression#newSampleData(double[],int,int), OLSMultipleLinearRegression#newSampleData(double[], double[][]) and GLSMultipleLinearRegression#newSampleData(double[],double[][],double[][]) for details.`

**`Usage Notes:`**

  * `Data are validated when invoking any of the newSample, newX, newY or newCovariance methods and IllegalArgumentException is thrown when input data arrays do not have matching dimensions or do not contain sufficient data to estimate the model.`
  * `By default, regression models are estimated with intercept terms. In the notation above, this implies that the X matrix contains an initial row identically equal to 1. X data supplied to the newX or newSample methods should not include this column - the data loading methods will create it automatically. To estimate a model without an intercept term, set the noIntercept property to true.`

`Here are some examples.`

**`OLS regression`**

```

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

**`GLS regression `**

`Instantiate a GLS regression object and load a dataset: `

```

var regression = new GLSMultipleLinearRegression()

var y = Array(11.0, 12.0, 13.0, 14.0, 15.0, 16.0 )

var x = new Array[Array[Double]](6)
x(0) = Array(0.0, 0, 0, 0, 0)
x(1) = Array(2.0, 0, 0, 0, 0) 
x(2) = Array(0, 3.0, 0, 0, 0)
x(3) = Array(0, 0, 4.0, 0, 0)
x(4) = Array(0, 0, 0, 5.0, 0)
x(5) = Array(0, 0, 0, 0, 6.0)


var omega = new Array[Array[Double]](6)
omega(0) = Array(1.1, 0, 0, 0, 0, 0)
omega(1) = Array(0, 2.2, 0, 0, 0, 0) 
omega(2) = Array(0, 0, 3.0, 0, 0, 0)
omega(3) = Array(0, 0, 0, 4.4, 0, 0)
omega(4) = Array(0, 0, 0, 0, 5.5, 0)
omega(5) = Array(0, 0, 0, 0, 0, 6.6)

regression.newSampleData(y, x, omega)
```


## `1.6 Rank transformations ` ##

`Some statistical algorithms require that input data be replaced by ranks. The ` _`org.apache.commons.math.stat.ranking`_ `package provides rank transformation.` _`RankingAlgorithm`_ `defines the interface for ranking. ` _`NaturalRanking`_ `provides an implementation that has two configuration options.`

  * _`Ties strategy`_ `determines how ties in the source data are handled by the ranking`
  * _`NaN strategy`_ `determines how NaN values in the source data are handled`


`Examples: `
```

var ranking = new NaturalRanking(NaNStrategy.MINIMAL, TiesStrategy.MAXIMUM)

var data = Array(20, 17, 30, 42.3, 17, 50, Double.NaN, java.lang.Double.NEGATIVE_INFINITY, 17) 

var ranks = ranking.rank(data)

```

`results in ranks containing Array(6.0, 5.0, 7.0, 8.0, 5.0, 9.0, 2.0, 2.0, 5.0),`

```
new NaturalRanking(NaNStrategy.REMOVED, TiesStrategy.SEQUENTIAL).rank(data)
```

`returns Array(5.0, 2.0, 6.0, 7.0, 3.0, 8.0, 1.0, 4.0)`

`The default NaNStrategy is NaNStrategy.MAXIMAL. This makes NaN values larger than any other value (including Double.POSITIVE_INFINITY). The default` _`TiesStrategy`_ `is ` _`TiesStrategy.AVERAGE`_ `, which assigns tied values the average of the ranks applicable to the sequence of ties. `


## `1.7 Covariance and correlation` ##

`The org.apache.commons.math.stat.correlation package computes covariances and correlations for pairs of arrays or columns of a matrix. ` _`Covariance`_ `computes covariances, ` _`PearsonsCorrelation`_ `provides Person's Product-Moment correlation coefficients and ` _`SpearmansCorrelation`_ `computes Spearman's rank correlation.`

**`Implementation Notes`**

  * `Unbiased covariances are given by the formula `
`cov(X,Y) = sum[(xi - E(X))(yi - E(Y))] / (n-1)`
`where E(X) is the mean of X and E(Y) is the mean of Y values. Non-bias-corrected estimates use n in place of n-1. Whether or not covariances are bias-corrected is determined by the optional parameter, "biasCorrected,", which defaults to true.`

  * _`PearsonsCorrelation`_  `computes correlations defined by the formula `
`cor(X,Y = sum[(xi-E(X))(y_i-E(Y)]/[(n-1)s(X)s(Y)] `
`where E(X) and E(Y) are means of X and Y and s(X), s(Y) are standard deviations.`

  * _`SpearmansCorrelation`_  `applies a rank transformation to the input data and computes Pearson's correlation on the ranked data. The ranking algorithm is configurable. By default,` _`NaturalRanking`_ `with default strategies for handling ties and NaN values is used. `

**`Examples: `**

**`Covariance of 2 arrays`**

`To compute the unbiased covariance between 2 double arrays, x and y, use: `

```
        
var N = 40
var x = vrand(N).getv   // a random Array[Double]

var y = vrand(N).getv   // a second one


// unbiased covariance
var unbiasedCov = new Covariance().covariance(x, y)

// non-biased corrected covariance
var nonBiasedCorrectedCov = new Covariance().covariance(x, y, false)
          
```

# Data Generation #

`The Commons Math random package includes utilities for `
  * ` generating random numbers`
  * `generating random vectors`
  * `generating random strings`
  * `generating cryptographically secure sequences of random numbers or strings`
  * `generating random samples and permutations`
  * `analyzing distributions of values in an input file and generating values "like" the values in the file`
  * `generating data for grouped frequency distributions or histograms`

`The source of random data used by the data generation utilities is pluggable. By default, the JDK-supplied PseudoRandom Number Generator (PRNG) is used, but alternative generators can be "plugged in" using an adaptor framework, which provides a generic facility for replacing java.util.Random with an alternative PRNG. Other very good PRNG suitable for Monte-Carlo analysis (but not for cryptography) provided by the library are the Mersenne twister from Makoto Matsumoto and Takuji Nishimura and the more recent WELL generators (Well Equidistributed Long-period Linear) from François Panneton, Pierre L'Ecuyer and Makoto Matsumoto.`

`Below show how to use the commons math API to generate different kinds of random data. The examples all use the default JDK-supplied PRNG. PRNG pluggability is covered in 2.7. The only modification required to the examples to use alternative PRNGs is to replace the argumentless constructor calls with invocations including a RandomGenerator instance as a parameter. `


## Random numbers ##

`The  `_`RandomData`_` interface defines methods for generating random sequences of numbers. The API contracts of these methods use the following concepts:`

**`Random sequence of numbers from a probability distribution`**

`There is no such thing as a single "random number." What can be generated are sequences of numbers that appear to be random. When using the built-in JDK function Math.random(), sequences of values generated follow the Uniform Distribution, which means that the values are evenly spread over the interval between 0 and 1, with no sub-interval having a greater probability of containing generated values than any other interval of the same length. The mathematical concept of a probability distribution basically amounts to asserting that different ranges in the set of possible values of a random variable have different probabilities of containing the value. Commons Math supports generating random sequences from each of the distributions in the distributions package. The javadoc for the nextXxx methods in RandomDataImpl describes the algorithms used to generate random deviates.`


**`Cryptographically secure random sequences`**

`It is possible for a sequence of numbers to appear random, but nonetheless to be predictable based on the algorithm used to generate the sequence. If in addition to randomness, strong unpredictability is required, it is best to use a secure random number generator to generate values (or strings). The nextSecureXxx methods in the RandomDataImpl implementation of the RandomData interface use the JDK SecureRandom PRNG to generate cryptographically secure sequences. The  setSecureAlgorithm method allows you to change the underlying PRNG. These methods are ` **`much slower`** `than the corresponding "non-secure" versions, so they should only be used when cryptographic security is required.`


**`Seeding pseudo-random number generators`**

`By default, the implementation provided in ` _`RandomDataImpl`_ `uses the JDK-provided PRNG. Like most other PRNGs, the JDK generator generates sequences of random numbers based on an initial "seed value". For the non-secure methods, starting with the same seed always produces the same sequence of values. Secure sequences started with the same seeds will diverge. When a new RandomDataImpl is created, the underlying random number generators are ` _`not`_ `initialized. The first call to a data generation method, or to a ` _`reSeed()`_ `method initializes the appropriate generator. If you do not explicitly seed the generator, it is by default seeded with the current time in milliseconds. Therefore, to generate sequences of random data values, you should always instantiate one ` _`RandomDataImpl`_ `and use it repeatedly instead of creating new instances for subsequent values in the sequence. For example, the following will generate a random sequence of 50 long integers between 1 and 1,000,000, using the current time in milliseconds as the seed for the JDK PRNG:`

```

var randomData = new RandomDataImpl()

var rv = new Vec(1000)

for (i <- 0 until 1000) 
    rv(i) = randomData.nextLong(1, 1000000)

plot(rv)

```

`The following will not in general produce a good random sequence, since the PRG is reseeded each time through the loop with the current time in milliseconds: `
```


var randomData = new RandomDataImpl()

var rv = new Vec(1000)

for (i <- 0 until 1000)  {
    randomData = new RandomDataImpl()
    rv(i) = randomData.nextLong(1, 1000000)
  }

plot(rv)
```

`The following will produce the same random sequence each time it is executed:`

```

var randomData = new RandomDataImpl()

var rv = new Vec(1000)
randomData.reSeed(1000)
for (i <- 0 until 1000)  {
    rv(i) = randomData.nextLong(1, 1000000)
  }

 
 var rv2 = new Vec(1000)
randomData.reSeed(1000)
for (i <- 0 until 1000)  {
    rv2(i) = randomData.nextLong(1, 1000000)
  }

figure(1); subplot(3,1,1); plot(rv, "rv"); subplot(3,1,2); plot(rv2, "rv2"); 
subplot(3,1,3); plot(rv-rv2, "rv-rv2")

```

`The following will produce a different random sequence each time it is executed. `

```

var randomData = new RandomDataImpl()

var rv = new Vec(1000)
randomData.reSeedSecure(1000)
for (i <- 0 until 1000)  {
    rv(i) = randomData.nextLong(1, 1000000)
  }

 
 var rv2 = new Vec(1000)
randomData.reSeedSecure(1000)
for (i <- 0 until 1000)  {
    rv2(i) = randomData.nextLong(1, 1000000)
  }

figure(1); subplot(3,1,1); plot(rv, "rv"); subplot(3,1,2); plot(rv2, "rv2"); 
subplot(3,1,3); plot(rv-rv2, "rv-rv2")
```



## Random Vectors ##


`Some algorithms require random vectors instead of random scalars. When the components of these vectors are uncorrelated, they may be generated simply one at a time and packed together in the vector. The ` _`UncorrelatedRandomVectorGenerator`_ `class simplifies this process by setting the mean and deviation of each component once and generating complete vectors. When the components are correlated however, generating them is much more difficult. The ` _`CorrelatedRandomVectorGenerator`_ `class provides this service. In this case, the user must set up a complete covariance matrix instead of a simple standard deviations vector. This matrix gathers both the variance and the correlation information of the probability law.`

`The main use for correlated random vector generation is for Monte-Carlo simulation of physical problems with several variables, for example to generate error vectors to be added to a nominal vector. A particularly common case is when the generated vector should be drawn from a ` _`Multivariate Normal Distribution.`_

**`Generating random vectors from a bivariate normal distribution`**

```


// Create and seed a RandomGenerator (could use any of the generators in the random package here)
var rg = new JDKRandomGenerator()
rg.setSeed(17399225432l)  // Fixed seed means same results every time

// Create a GaussianRandomGenerator using rg as its source of randomness
var  rawGenerator = new GaussianRandomGenerator(rg)

var mean = Array(1.0, 2.0)
var c = -2.0
var covDarr = Array( Array(9, c), Array(c, 16))
var covariance = new Array2DRowRealMatrix(covDarr)
// Create a CorrelatedRandomVectorGenerator using rawGenerator for the components
var  generator =      new CorrelatedRandomVectorGenerator(mean, covariance, 1.0e-12 * covariance.getNorm(), rawGenerator)

// Use the generator to generate correlated vectors
var Nvecs = 500
var vecsAll = new Array[Array[Double]](2, Nvecs)
for (k<-0 until Nvecs) {
 var  randomVector = generator.nextVector()
 vecsAll(0)(k) = randomVector(0)
 vecsAll(1)(k) = randomVector(1)
 } 

figure(1); subplot(3,1,1);  plot(vecsAll(0)); subplot(3,1,2); plot(vecsAll(1));
   subplot(3,1,3); scatterPlotsOn; plot(vecsAll(0), vecsAll(1))
```

`The mean argument is an Array[Double] holding the means of the random vector components. In the bivariate case, it must have length 2. The covariance  argument is a RealMatrix, which needs to be 2 x 2. The main diagonal elements are the variances of the vector components and the off-diagonal elements are the covariances. For example, if the means are 1 and 2 respectively, and the desired standard deviations are 3 and 4, respectively, then we need to use`

```
var mean = Array(1.0, 2.0)
var c = -2.0
var covDarr = Array( Array(9, c), Array(c, 16))
var covariance = new Array2DRowRealMatrix(covDarr)
```

`where c is the desired covariance. If you are starting with a desired correlation, you need to translate this to a covariance by multiplying it by the product of the standard deviations. For example, if you want to generate data that will give Pearson's R of 0.5, you would use c = 3 * 4 * .5 = 6. `


` In addition to multivariate normal distributions, correlated vectors from multivariate uniform distributions can be generated by creating a UniformRandomGenerator  in place of the GaussianRandomGenerator above. More generally, any NormalizedRandomGenerator  may be used.`


## Random Strings ##

`The methods` _`nextHexString`_ `and `_`nextSecureHexString`_ `can be used to generate random strings of hexadecimal characters. Both of these methods produce sequences of strings with good dispersion properties. The difference between the two methods is that the second is cryptographically secure. Specifically, the implementation of nextHexString(n) in RandomDataImpl uses the following simple algorithm to generate a string of n hex digits:`

  1. `n/2+1 binary bytes are generated using the underlying Random`
  1. `Each binary byte is translated into 2 hex digits`

`The RandomDataImpl implementation of the "secure" version, nextSecureHexString generates hex characters in 40-byte "chunks" using a 3-step process:`

  1. `20 random bytes are generated using the underlying SecureRandom.`
  1. `SHA-1 hash is applied to yield a 20-byte binary digest.`
  1. `Each byte of the binary digest is converted to 2 hex digits.`

`Similarly to the secure random number generation methods, nextSecureHexString is much slower than the non-secure version. It should be used only for applications such as generating unique session or transaction ids where predictability of subsequent ids based on observation of previous values is a security concern. If all that is needed is an even distribution of hex characters in the generated strings, the non-secure method should be used.`


# Linear Algebra #


## Overview ##

`Linear algebra support in commons-math provides operations on real matrices (both dense and sparse matrices are supported) and vectors. It features basic operations (addition, subtraction ...) and decomposition algorithms that can be used to solve linear systems either in exact sense and in least squares sense.`

## Real matrices ##

`The  ` _`RealMatrix`_ `interface represents a matrix with real numbers as entries. The following basic matrix operations are supported: `
  * `Matrix addition, subtraction, multiplication`
  * `Scalar addition and multiplication`
  * `transpose`
  * `Norm and Trace`
  * `Operation on a vector`

`Example: `

```


// Create a real matrix with two rows and three columns
var  matrixData = Array(Array(1.0, 2.0, 3.0), Array(2.0, 5.0, 3.0))
var  m = new Array2DRowRealMatrix(matrixData)

// One more with three rows, two columns
var  matrixData2 = Array( Array(1.0, 2.0), Array(2.0, 5.0), Array(1.0, 7.0))
var  n = new Array2DRowRealMatrix(matrixData2)

// Note: The constructor copies  the input double[][] array.
// Now multiply m by n

var  p = m.multiply(n)
println(p.getRowDimension())    // 2
println(p.getColumnDimension()) // 2

// Invert p, using LU decomposition
var  pInverse = new LUDecompositionImpl(p).getSolver().getInverse()
         
```

`The three main implementations of the interface are ` _`Array2DRowRealMatrix`_ `and ` _`BlockRealMatrix`_ `for dense matrices (the second one being more suited to dimensions above 50 or 100) and ` _`SparseRealMatrix`_ `for sparse matrices. `


## `Real Vectors` ##

`The `_`RealVector`_ `interface represents a vector with real numbers as entries. The following basic matrix operations are supported: `
  * `Vector addition, subtraction`
  * `Element by element multiplication, division`
  * `Scalar addition, subtraction, multiplication, division and power`
  * `Mapping of mathematical functions (cos, sin ...)`
  * `Dot product, outer product`
  * `Distance and norm according to norms L1, L2 and Linf`

`The ` _`RealVectorFormat`_ `class handles input/output of vectors in a customizable textual format.`

## `Solving linear systems` ##

`The ` _`solve()`_ ` methods of the ` _`DecompositionSolver`_ `interface support solving linear systems of equations of the form AX=B, either in linear sense or in least square sense. A `_`RealMatrix`_ ` instance is used to represent the coefficient matrix of the system. Solving the system is a two phases process: first the coefficient matrix is decomposed in some way and then a solver built from the decomposition solves the system. This allows to compute the decomposition and build the solver only once if several systems have to be solved with the same coefficient matrix.`

`For example, to solve the linear system`

`           2x + 3y - 2z = 1`

`           -x + 7y + 6x = -2`

`           4x - 3y - 5z = 1`


`Start by decomposing the coefficient matrix A (in this case using LU decomposition) and build a solver`

```

var coefficients = new Array2DRowRealMatrix(Array(Array(2.0, 3.0, -2), Array(-1.0, 7.0, 6), Array(4, -3.0, -5)))
   
var solver = new LUDecompositionImpl(coefficients).getSolver()   
```

`Next create a ` _`RealVector`_ `array to represent the constant vector B and use ` _`solve(RealVector)`_ `to solve the system`

```

var constants = new ArrayRealVector(Array(1.0, -2.0, 1), false)
var solution = solver.solve(constants)

```

`The ` _`solution`_ ` vector will contain values for ` _`x (solution.getEntry(0)), y(solution.getEntry(1)), `_ `and ` _`z (solution.getEntry(2)`_ `that solve the system.`

`Each type of decomposition has its specific semantics and constraints on the coefficient matrix as shown in the following table. For algorithms that solve AX=B in least squares sense the value returned for X is such that the residual AX-B has minimal norm. If an exact solution exist (i.e. if for some X the residual AX-B is exactly 0), then this exact solution is also the solution in least square sense. This implies that algorithms suited for least squares problems can also be used to solve exact problems, but the reverse is not true.`

**`Decomposition algorithms`**

| **Name** | **Coefficients Matrix** | **problem type** |
|:---------|:------------------------|:-----------------|
| LU       | square                  | exact solution only |
| Cholesky | symmetric positive definite | exact solution only |
| QR       | any                     | least squares solution |
| eigen decomposition |square                   |exact solution only |
| SVD      | any                     | least squares solution |

`It is possible to use a simple array of double instead of a ` _`RealVector.`_ `In this case, the solution will be provided also as an array of double.`

`It is possible to solve multiple systems with the same coefficient matrix in one method call. To do this, create a matrix whose column vectors correspond to the constant vectors to be solved and use ` _`solve(RealMatrix)`_ `, which returns a matrix with column vectors representing the solutions.`

## `Eigenvalues/eigenvectors and singular values/singular vectors` ##

`Decomposition algorithms may be used for themselves and not only for linear system solving. This is of prime interest with eigen decomposition and singular value decomposition. `

`The ` _`getEigenvalue(), getEigenvalues(), getEigenVector(), getV(), getD()`_ `and ` _`getVT()`_ `methods of the ` _`EigenDecomposition`_ `interface support solving eigenproblems of the form ` _`AX = lambda X`_ `where ` _`lambda`_` is a real scalar.`

`The ` _`getSingularValues(), getU(), getS()`_` and `_` getV()`_` methods of the` _`SingularValueDecomposition`_ `interface allow to solve singular values problems.`



# Ordinary Differential Equations #

## Overview ##

`The ode package of Apache Commons provides classes to solve Ordinary Diferential Equations problems. ScalaLab provides routines to solve ODEs with the NUMAL library, the Apache Commons is an alternative option. `

`This package solves Initial Value Problems of the form y'=f(t,y) with t0 and y(t0)=y0 known. The provided integrators compute an estimate of y(t) from t=t0 to t=t1.`

`All integrators provide dense output. This means that besides computing the state vector at discrete times, they also provide a cheap mean to get both the state and its derivative between the time steps. They do so through classes extending the` _`StepInterpolator`_ `abstract class, which are made available to the user at the end of each step.`

`All integrators handle multiple discrete events detection based on switching functions. This means that the integrator can be driven by user specified discrete events (occurring when the sign of user-supplied` _`switching function`_ `changes). The steps are shortened as needed to ensure the events occur at step boundaries (even if the integrator is a fixed-step integrator). When the events are triggered, integration can be stopped (this is called a G-stop facility), the state vector can be changed, or integration can simply go on. The latter case is useful to handle discontinuities in the differential equations gracefully and get accurate dense output even close to the discontinuity. `

`All integrators support setting a maximal number of evaluations of differential equations function. If this number is exceeded, an exception will be thrown during integration. This can be used to prevent infinite loops if for example error control or discrete events create a really large number of extremely small steps. By default, the maximal number of evaluation is set to Integer.MAX_VALUE (i.e. 2^31-1 or 2147483647). It is recommended to set this maximal number to a value suited to the ODE problem, integration range, and step size or error control settings.`

`The user should describe his problem in his own classes which should implement the FirstOrderDifferentialEquations interface. Then he should pass it to the integrator he prefers among all the classes that implement the` _`FirstOrderIntegrator`_ `interface. The following example shows how to implement the simple two-dimensional problem:`
  * `y'0(t) = ω × (c1 - y1(t))`
  * `y'1(t) = ω × (y0(t) - c0) `

`with some initial state y(t0) = (y0(t0), y1(t0)). In fact, the exact solution of this problem is that y(t) moves along a circle centered at c = (c0, c1) with constant angular rate ω.`

`We present class CircleODE that implements our differential equation below.`

```
 
class CircleODE(var c: Array[Double], omega: Double)  extends AnyRef with FirstOrderDifferentialEquations {
     
      def getDimension = 2
      
      def computeDerivatives(t: Double, y: Array[Double], yDot: Array[Double]) = {
          yDot(0) = omega*(c(1) - y(1))
          yDot(1) = omega*(y(0)-c(0))
          }
        
     }
          
```


`Computing the state y(16.0) starting from y(0.0) = (0.0, 1.0) and integrating the ODE is done as follows (using Dormand-Prince 8(5,3) integrator as an example): `

```
 var  dp853 =  new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10)
 var ode = new CircleODE( Array(1.0, 1.0), 0.1)
 var y = Array(0.0, 1.0)  // initial state
 dp853.integrate(ode, 0.0, y, 16.0, y)
 
 y  // now y contains final state at time t = 16.0

```


## Continuous Output ##

`The solution of the integration problem is provided by two means. The first one is aimed towards simple use: the state vector at the end of the integration process is copied in the y array of the` _`FirstOrderIntegrator.integrate`_ `method, as shown by previous example. The second one should be used when more in-depth information is needed throughout the integration process. The user can register an object implementing the ` _`StepHandler`_ `interface or a ` _`StepNormalizer`_ `object wrapping a user-specified object implementing the` _`FixedStepHandler`_ `interface into the integrator before calling the` _`FirstOrderIntegrator.integrate`_ `method. The user object will be called appropriately during the integration process, allowing the user to process intermediate results. The default step handler does nothing. Considering again the previous example, we want to print the trajectory of the point to check it really is a circle arc. We simply add the following before the call to integrator.integrate: `

```
 
 def stepHandler = new StepHandler() {
     def reset= {}
     def requiresDenseOutput = false
     def handleStep(interpolator: StepInterpolator, isLast: Boolean) =
        {
            var t = interpolator.getCurrentTime
            var y = interpolator.getInterpolatedState
            println(t+" "+y(0)+" "+y(1))
            }
           }
     integrator.addStepHandler(stepHandler)

```

_`ContinuousOutputModel`_ `is a special-purpose step handler that is able to store all steps and to provide transparent access to any intermediate result once the integration is over. An important feature of this class is that it implements the` _`Serializable`_ `interface. This means that a complete continuous model of the integrated function throughout the integration range can be serialized and reused later (if stored into a persistent medium like a file system or a database) or elsewhere (if sent to another application). Only the result of the integration is stored, there is no reference to the integrated problem by itself.`

`Other default implementations of the ` _`StepHandler`_ `interface are available for general needs (`_`DummyStepHandler, StepNormalizer`_ `) and custom implementations can be developed for specific needs. As an example, if an application is to be completely driven by the integration process, then most of the application code will be run inside a step handler specific to this application.`

`Some integrators (the simple ones) use fixed steps that are set at creation time. The more efficient integrators use variable steps that are handled internally in order to control the integration error with respect to a specified accuracy (these integrators extend the ` _`AdaptiveStepsizeIntegrator`_ `abstract class). In this case, the step handler which is called after each successful step shows up the variable stepsize. The `_`StepNormalizer`_ `class can be used to convert the variable stepsize into a fixed stepsize that can be handled by classes implementing the `_`FixedStepHandler`_ `interface. Adaptive stepsize integrators can automatically compute the initial stepsize by themselves, however the user can specify it if he prefers to retain full control over the integration or if the automatic guess is wrong.`

## Discrete Events Handling ##

`ODE problems are continuous ones. However, sometimes discrete events must be taken into account. The most frequent case is the stop condition of the integrator is not defined by the time t but by a target condition on state y (say y(0) = 1.0 for example).`

`Discrete events detection is based on switching functions. The user provides a simple g(t, y) function depending on the current time and state. The integrator will monitor the value of the function throughout integration range and will trigger the event when its sign changes. The magnitude of the value is almost irrelevant, it should however be continuous (but not necessarily smooth) for the sake of root finding. The steps are shortened as needed to ensure the events occur at step boundaries (even if the integrator is a fixed-step integrator). Note that g function signs changes at the very beginning of the integration (from t0 to t0 + ε where ε is the events detection convergence threshold) are explicitly ignored. This prevents having the integration stuck at its initial point when a new integration is restarted just at the same point a previous one had been stopped by an event.`

`When an event is triggered, the event time, current state and an indicator whether the switching function was increasing or decreasing at event time are provided to the user. Several different options are available to him: `
  1. `integration can be stopped (this is called a G-stop facility),`
  1. `the state vector or the derivatives can be changed,`
  1. `or integration can simply go on.`

`The first case, G-stop, is the most common one. A typical use case is when an ODE must be solved up to some target state is reached, with a known value of the state but an unknown occurrence time. As an example, if we want to monitor a chemical reaction up to some predefined concentration for the first substance, we can use the following switching function setting: `