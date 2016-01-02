# Linear Model Fitting example #

`This example illustrates linear model fitting. The original points are fitted with a linear model.`

```



// test Fitab
// Object for fitting a straight line y = a+bx to a set of points (x_i, y_i), with or without available
 //errors σ_i. Call one of the two constructors to calculate the fit. The answers are then available 
 // as the variables  a, b, siga, sigb, chi2 and either q or sigdat
   /* 
   Constructor. Given a set of data points x[0..ndata-1], y[0..ndata-1] with individual standard 
   deviations sig[0..ndata-1], sets a, b and their respective probable uncertainties siga and sigb, the chi-square 
   chi2, and the goodness-of-fit probability q (that the fit would have χ^2 this large or larger)
   */

   // create the data arrays
   var xx = Array(0.4, 3.5, 6.7,  8.6, 9.8, 15.6, 26.8, 45.8 )  // x-points
   var yy = 3.4*xx + 6.5*vrand(xx.length)   // y-points are generated from a linear model plus random deviation

 // fit a linear model, the fitting coefficients are returned in fp.a and fp.b
   var  fp = new com.nr.model.Fitab(xx, yy)

  var N = 1000  // on how many points to evaluate the model
  var xxa = linspace( min(xx), max(xx), N)  // construct the x-axis
  var yya = xxa map ( (x: Double) => fp.a*x+fp.b )   // apply the function

     figure(1)
     plotMarks(xx, yy.getv, Color.GREEN, "Initial Points");   // the plotted marks here denote the initial points, on which fit is performed
     hold(on)   // hold the axis on
     plot(xxa, yya, "Fitted Line")   //  plot the fitted line
     


   
        

```

# Levenberg-Marquardt example #

```



import com.nr.NRUtil.SQR
import com.nr.NRUtil.buildVector

import com.nr.model.FGauss
import com.nr.model.Fitmrq
import com.nr.ran.Normaldev

// Levenberg-Marquardt example

closeAll   // close any open figures

   var  N = 200; var  MA=6
   var aa = Array(5.0, 1.0, 1.5, 2.0, 9.0, 3.0)
   var gguess = Array(4.5, 1.2, 2.8, 2.5, 8.9, 2.8)
   var  SPREAD = 0.01
   var  x = new AD(N)  // AD abbreviates Array[Double]
   var  y = new AD(N)
   var  sig = new AD(N) 
   var  a = aa.clone
   var guess = gguess.clone
    
    // Test Fitmrq
    println("Testing Fitmrq")

    var  ndev = new Normaldev(0.0, 1.0, 17)  // object to generate deviates from the Normal distribution

    var Np = 5000  // number of points to generate
    var nd = new Vec(Np)  // test some normaldev points
    
    var k = 0
    while (k<Np) {
        nd(k) = ndev.dev
        k += 1
     }
        
    figure(1);  plot(nd, Color.GREEN, "Normal deviates")

    figure(2); title("Normal  density")
    var slicesX = 30
    plot2d_histogram(nd.getv, slicesX, "Normal Probability Density")
    
    // First try a sum of two Gaussians
    // y(i) = a(0)*exp(-((x(i)-a(i))/a(2))^2+a(3)*exp(-((x(i)-a(i))/a(5))^2
    // the actual unknown function
    def ag(x: Double, a: Array[Double]) = {
       a(0)*exp(-SQR((x-a(1))/a(2)))+a(3)*exp(-SQR((x-a(4))/a(5)))      
    }

 // compute the sum of two Gaussians with some added noise
     var dx= 0.1
     var i = 0
     while (i < N) {
         x(i) = 0.1*(i+1)   // the x-axis value
         y(i) =  ag(dx*i, a)  // compute the function on that value
         y(i) *= (1.0+SPREAD*ndev.dev())   // add some noise
         sig(i) = SPREAD*y(i)  // individual standard deviations
         i += 1
   }

figure(3); plot(y,  Color.BLUE, "Sum of two Gaussians with some added noise")


    var fgauss = new FGauss()
    var myfit = new Fitmrq(x, y, sig, guess, fgauss)
    myfit.fit()

// evaluate using the coefficients of the model mf passed as parameter
def evg(x: Double, mf: com.nr.model.Fitmrq) = 
  ag(x, mf.a)

var yc = new Array[Double](y.length)
  // evaluate reconstructed Gaussian model
 for (i <- 0 until N) 
    yc(i) = evg(dx*i, myfit)

figure(4); hold("on");    plot(yc, Color.GREEN, "Reconstructed" );
    plot(y,  Color.RED, "Original")
   

  


println( "chi-squared:  \n"+ myfit.chisq)
for (i <- 0 until MA)  println( myfit.a(i))
println
println("Uncertainties:")
for (i <- 0 until  MA)  println(sqrt(myfit.covar(i, i)))

println("Expected results:")

 for (i <- 0 until  MA)  println(a(i))



  var localflag = false  // signals an error in the estimated uncertainty
  var globalflag = false
 for (j<-0 until MA) {
        
      localflag = abs(myfit.a(j)-a(j)) > 2.0*sqrt(myfit.covar(j)(j))  // a true value signals a bad fit
      globalflag = globalflag || localflag
      if (localflag) {
        println("*** Fitmrq: Fitted parameter "+j+ "  not within estimated uncertainty")
        
      }
    }
```