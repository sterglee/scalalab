
package scalaSci.math.plot


// this object implements functional plotting by adaptively adjusting the sampling rate 
// where the function changes quickly.
//  The fastest the function change, the faster sampling rate becomes,
//  in order to catch signal variations

object  PlotAdaptiveFunctional  {
  var dxForDeriv = 0.000001  // a very small increment to compute the function's derivative
  var thresholdSmallRateOfChange = 1.0    // for smaller than this we use the default dx
  var thresholdLargeRateOfChange = 1000.0  // for very fast changing functions avoid taking too much samples
    
  def setDxForDeriv(newDx: Double) = { dxForDeriv = newDx }

  def setThresholdSmallRateOfChange(newThresholdSmallRateOfChange: Double) =  { thresholdSmallRateOfChange = newThresholdSmallRateOfChange }
  
  def setThresholdLargeRateOfChange(newThresholdLargeRateOfChange: Double) = { thresholdLargeRateOfChange = newThresholdLargeRateOfChange }
        
// adaptive function plotting. Plot a function by changing automatically the sampling rate in regions where the function
// changes quickly
def  faplot( f: Double => Double, low: Double, high: Double, nP: Int = 200) =  { 
    
    var N = nP
    // default sampling increment interval
    var dx = (high-low)/N
    var defaultDx = dx  // the default sampling interval, dx
    var ax = List[Double]()    // the "time" axis with the non-uniform sampling
    var ay = List[Double]()    // the corresponding values of the function
    
    // at this loop we will try to estimate the rate of change of the function
    // If the rate of change is large we decrease the sampling interval
    //  in order to have better function description.
    // If it is small we reset to the defaultDx
    var currx = low  // current value of x, i.e. the variable used to sample the function
    while (currx < high) {  // currx steps across the whole sampling axis
        var frate = (f(currx+dxForDeriv)-f(currx)) / dxForDeriv  // an estimate of the rate of change near currx
        // avoid to use too large and too small rate of change
        if (frate > thresholdLargeRateOfChange) 
            frate = thresholdLargeRateOfChange
        else if (frate < thresholdSmallRateOfChange)
            frate = thresholdSmallRateOfChange
        
        var currStep = defaultDx / frate   // adjust the stepping size with the rate of change
        ax = currx :: ax   // append "time" sample
        ay = f(currx) :: ay  // append a function value
  
        currx += currStep   // update the point on the x-axis, fast variation areas lead to dense sampling
   }
  
     // convert lists to arrays
    var axa = ax.toArray
    var aya = ay.toArray
   
    scalaSci.math.plot.plotTypes.plot(axa, aya)   // plot the computed arrays      
   
   (axa, aya)
  }

}

// Example: the function sin(x*x) changes generally more rapidly as x increases,
//  however, as can be seen from its derivative x*x*cos(x*x),
//  the rate of change oscilates also with increasing frequency as x increases 
 /* 
def f(x: Double) = sin(x*x)

closeAll
var Npoints = 200
figure(1)
subplot(2, 1, 1)
fplot(f, 0, 10, nP = Npoints )
xlabel("Fixed sampling functional ploting")
subplot(2, 1, 2)
var (ax, ay) = faplot(f, 0, 10, nP = Npoints)
xlabel("Adaptive sampling functional ploting")

  
*/


