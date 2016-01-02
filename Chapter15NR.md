# Levenberg-Marquardt optimization #

```


import com.nr.NRUtil.SQR
import com.nr.NRUtil.buildVector

import com.nr.model.FGauss
import com.nr.model.Fitmrq
import com.nr.ran.Normaldev

   var  N = 100; var  MA=6
   var aa = Array(5.0,2.0,3.0,2.0,5.0,3.0)
   var gguess = Array(4.5,2.2,2.8,2.5,4.9,2.8)
   var  SPREAD = 0.01
   var  x = new Array[Double](N)
   var  y = new Array[Double](N) 
   var  sig = new Array[Double](N);
   var  a = buildVector(aa)
   var guess = buildVector(gguess)
    
    // Test Fitmrq
    println("Testing Fitmrq")

    var  ndev = new Normaldev(0.0, 1.0, 17)

    var Np = 5000
    var nd = new Vec(Np)  // test some normaldev points
    for (k<-0 until Np)
     nd(k) = ndev.dev
     plot(nd)

    figure(2); title("Normal  density")
    var slicesX = 30
    plot2d_histogram(nd.getv, slicesX, "Normal Probability Density")
    
    // First try a sum of two Gaussians
    for (i <- 0 until N) {
      x(i) = 0.1*(i+1)
      y(i) = 0.0
      var j = 0
      while  (j < MA) {
        y(i) += a(j)*exp(-SQR((x(i)-a(j+1))/a(j+2)))
        j += 3
       }
        y(i) *= (1.0+SPREAD*ndev.dev())
        sig(i) = SPREAD*y(i)
   }

 figure(3); plot(y,  Color.BLUE, "Sum of two Gaussians")
 
    var fgauss = new FGauss()
    var myfit = new Fitmrq(x, y, sig, guess, fgauss)
    myfit.fit()



println( "chi-squared:  \n"+ myfit.chisq)
for (i <- 0 until MA)  println( myfit.a(i))
println
println("Uncertainties:")
for (i <- 0 until  MA)  println(sqrt(myfit.covar(i, i)))

println("Expected results:")

 for (i <- 0 until  MA)  println(a(i))

```