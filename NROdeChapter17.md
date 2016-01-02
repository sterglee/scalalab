# Introduction #
`Currently, there are three excellent library sources for performing numerical integration with ScalaLab: ` **`a. The NUMAL library, b. The Appache Common Maths and c. The Numerical Recipes Book, Ed. 3, `** `as is translated in Java. `

`Although, the former two libraries are strong, personally I like more the Numerical Recipes code, since the book offers me the oppurtunity, to study the algorithms, and to fully understand the code. `

`Here we adapt some examples of the Numerical Recipes book. For the description of the algorithms, please see the excellent presentation of the Numerical Recipes 3nd edition book.`

## Lorenz Chaotic System Integration with Dormand-Prince fifth-order stepper ##

```
// evaluate the Lorenz chaotic attractor with Dormand-Prince fifth-order stepper
import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub

import  com.nr.ode.DerivativeInf
import  com.nr.ode.Odeint
import  com.nr.ode.Output



  var ystart = Array( 0.1,  0.2,  0.12)    // starting values of integration
  var xx1 = 1.0; var xx2 = 10.0  // integrate from xx1 to xx2
  var atol = 0.001; var rtol = 0.01  // absolute tolerance and relative tolerance
  var h1 = 0.02  // guessed first stepsize
  var hminn = 0.01   // minimum allowed stepsize (can be zero)
  var nsavePoints = getInt("How many points to save ?", 5000)
  var outt = new Output(nsavePoints)
  var s = new com.nr.ode.StepperDopr5()
  var  derivss = new LorenzDerivs()
    
   
    
/**
   *   The routine integrates starting values
   * ystart[0..nvar-1] from xx1 to xx2 with absolute tolerance atol and relative
   * tolerance rtol. The quantity h1 should be set as a guessed first stepsize,
   * hmin as the minimum allowed stepsize (can be zero). An Output object out
   * should be input to control the saving of intermediate values. On output,
   * nok and nbad are the number of good and bad (but retried and fixed) steps
   * taken, and ystart is replaced by values at the end of the integration
   * interval. derivs is the user-supplied routine (function or functor) for
   * calculating the right-hand side derivative.
   * 
   */
  var  lorenzODE = new  Odeint(ystart, xx1, xx2, atol, rtol,  h1,  hminn, outt, derivss,  s) 
  lorenzODE.integrate()   // proceed with the integration

  // convert to RichDoubleDoubleArray in order to extract the variables for plotting
 var yout = new RichDouble2DArray(outt.ysave)
 var y0 = yout.getRow(0).getv  
 var y1 = yout.getRow(1).getv
 var y2 = yout.getRow(2).getv
 plot(y0, y1, y2,"Lorenz Attractor integrated with Dormand-Prince fifth-order stepper ")  


    // specifies the derivatives of the Lorenz system
  class LorenzDerivs extends AnyRef with  DerivativeInf {
    
    def derivs(x: Double, y: Array[Double], dydx: Array[Double]) = {
      lorenz_derivs(x, y, dydx)
    }
    
    def  lorenz_derivs(x: Double,   y: Array[Double],  dydx: Array[Double]) = {
        var xx = y(0); var yy = y(1); var zz = y(2)
        
      dydx(0) = 10*(yy-xx)
      dydx(1) = -xx*zz+143*xx-yy
      dydx(2) = xx*yy - 2.66667*zz
      
  }
  
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]) = {}
  }
  
```

# Runge-Kutta #

```

import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub

import  com.nr.ode.DerivativeInf
import  com.nr.ode.Odeint
import  com.nr.sf.Bessjy

var  nvar = 4
val y = new Array[Double](nvar)
val dydx = new Array[Double](nvar)
val yout = new Array[Double](nvar)
val yexp = new Array[Double](nvar)

var globalflag=false

  // Test rk4
println("testting  rk4")


    var x = 1.0
    var h = 0.1

    var bess = new Bessjy()
    for (i <- 0 until nvar) {
      y(i) = bess.jn(i, x)
      yexp(i) = bess.jn(i, x+h) 
    }

    var  rk4_derivs = new RK4_derivs()
    
    rk4_derivs.rk4_derivs(x, y, dydx)
    
    Odeint.rk4(y, dydx, x, h, yout, rk4_derivs)

    for (i <- 0 until  nvar)
      println( yout(i), yexp(i))

    var sbeps = 1.e-6
    println(maxel(vecsub(yout,yexp)))
    var localflag = maxel(vecsub(yout,yexp)) > sbeps
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** rk4: Inaccurate Runge-Kutta step")
    }

    if (globalflag)  println("Failed\n")
    else  println("Passed\n")
  
  
  class RK4_derivs extends AnyRef with  DerivativeInf {
    
    def derivs(x: Double, y: Array[Double], dydx: Array[Double]) = {
      rk4_derivs(x, y, dydx)
    }
    
    def  rk4_derivs(x: Double,   y: Array[Double],  dydx: Array[Double]) = {
      dydx(0) = -y(1) 
      dydx(1) = y(0)- (1.0 / x) * y(1)
      dydx(2) = y(1) - (2.0 / x) * y(2)
      dydx(3) = y(2) - (3.0 / x) * y(3)
  }
  
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]) = {}
  }
  

```


## Bulirsch-Stoer Method ##

```


import com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub

import com.nr.ode._
import com.nr.sf.Bessjy

  class rhs_StepperBS extends AnyRef with DerivativeInf {
    def  derivs(x: Double, y: Array[Double], dydx: Array[Double]) {
      dydx(0) =  -y(1)
      dydx(1)=y(0)-(1.0/x)*y(1)
      dydx(2)=y(1)-(2.0/x)*y(2)
      dydx(3)=y(2)-(3.0/x)*y(3)
    }
    def  jacobian(  x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]){}
  }

    var nvar=4
    var atol=1.0e-6;  var rtol=atol; var h1=0.01; var hmin=0.0; var x1=1.0; var x2=200.0
    var sbeps = 1.0e-8
        
    var y = new Array[Double](nvar)
    var yout = new Array[Double](nvar)
    var yexp = new Array[Double](nvar)
    var globalflag = false
    var localflag = false
    
    // Test StepperBS
    println("Testing StepperBS")

    var bess = new Bessjy()
    for (i <- 0 until nvar) {
      y(i) = bess.jn(i,x1)
      yexp(i)=bess.jn(i,x2)
    }
    
    var out = new Output(1500)
    var derivs = new rhs_StepperBS()
    var s = new StepperBS()
    // integrates values y[0..nvar-1] from x1 to x2 with absolute tolerance atol and
    // relative tolerance rtol
    // The quantity h1 should be set as a guessed first stepsize,
    // hmin as the minimum allowed stepsize (can be zero). An Output object out should be input 
    // to control the saving of intermediate values. On output, nok and nbad 
    // are the number of good and bad (but retried and fixed) steps taken,
    // and ystart is replaced by values at the end of the integration interval.
     
   
    var ode = new Odeint(y, x1, x2, atol, rtol, h1, hmin, out, derivs, s)
    ode.integrate()

    for (i<-0 until nvar) {
      yout(i) = out.ysave(i)(out.count-1)
      printf("%f  %f\n", yout(i), yexp(i))
    }

    plot(out.ysave(1), out.ysave(2))

    sbeps = 1.0e-8
    println(maxel(vecsub(yout,yexp)))
    localflag = (maxel(vecsub(yout,yexp)) > sbeps)
    globalflag = globalflag || localflag
    if (localflag) {
      fail("*** StepperBS: Inaccurate integration")
      
    }

    if (globalflag)  println("Failed\n")
    else  println("Passed\n")
  
  


```