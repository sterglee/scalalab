` We present some examples of using the Statistical Functions implemented in Chapter 6 of Numerical Recipes with ScalaLab `

## Normal Distribution ##

```


// the following script illustrates how to extent the normal distribution class of the 
// Numerical Recipes with routines that compute the pdf, cdf, and inverse cdf

class NormalDist(m: Double, s: Double)  extends  com.nr.sf.Normaldist(m, s) { 
  
  def evalPDF( low: Double, high: Double, npoints: Int = 2000) = {
  	val  x = linspace(low, high, npoints)
  	val  y = x map this.p  // map the PDF function from the Numerical Recipes implementation
  	(x, y)
  }

  def evalCDF(low: Double, high: Double, npoints: Int = 2000) = {
  	val x = linspace(low, high, npoints) 	
  	val y = x map this.cdf // map the CDF function from the Numerical Recipes implementation
  	(x, y)
  }

  def evalInvCDF(low: Double, high: Double, npoints: Int = 2000) = {
  	val x = linspace(low, high, npoints) 	
  	val y = x map this.invcdf // map the CDF function from the Numerical Recipes implementation
  	(x, y)
  	
  }
}


var m= 5.2; var s = 2.7
var nd  = new NormalDist(m, s)
var (axis, ndPDF) = nd.evalPDF(-5, 15, 3000)
figure(1); subplot(3,1,1); plot(axis, ndPDF, "Normal Distribution - Probability Density Function", Color.RED)

var (axis2, ndCDF) = nd.evalCDF(0, 15, 4000)
subplot(3,1,2); plot(axis2, ndCDF, "Normal Distribution - Cumulative Distribution Function", Color.BLUE)

var (axis3, invCDF) = nd.evalInvCDF(0.0001, 0.999, 3000)
subplot(3,1,3); plot(axis3, invCDF, "Normal Distribution - Inverse cumulative distribution function", Color.GREEN)


```