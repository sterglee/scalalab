# Introduction #

`This page adapts the Java examples of the Numerical Recipes ed. 3, to ScalaSci code.`


## Runge-Kutta 4th order ##

```


import  com.nr.test.NRTestUtil.maxel
import  com.nr.test.NRTestUtil.vecsub

import  com.nr.ode.DerivativeInf
import  com.nr.ode.Odeint
import  com.nr.sf.Bessjy


  
  class RK4_derivs extends AnyRef with DerivativeInf {
    def  derivs(x: Double, y: Array[Double], dydx: Array[Double]) = {
      rk4_derivs(x, y, dydx)
    }
 
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]] ){}
  }
  


var  nvar = 4
val y = new Array[Double](nvar)
val dydx = new Array[Double](nvar)
val yout = new Array[Double](nvar)
val yexp = new Array[Double](nvar)

var globalflag=false

  // Test rk4
println("testting  rk4")

 var   x = 1.0
 var   h = 0.1

  var bess=new Bessjy()
    for (i <- 0 until  nvar) {
      y(i) = bess.jn(i, x) 
      yexp(i) = bess.jn(i, x+h)
    }

def  rk4_derivs(x: Double, y: Array[Double], dydx: Array[Double]) = {
    dydx(0) = -y(1)
    dydx(1) = y(0) - (1.0/x)*y(1)
    dydx(2) = y(1) - (2.0/x)*y(2)
    dydx(3) = y(2) - (3.0/x)*y(3)
  }

    rk4_derivs(x, y, dydx)
    
  var  rk4_derivs = new RK4_derivs()
    
  Odeint.rk4(y, dydx, x, h, yout, rk4_derivs)

    //for (i <- 0 until nvar)
    cfor (0)(_ < nvar, _ + 1) { i =>
      println( yout(i)+"  "+ yexp(i));
      }

    var sbeps = 1.0e-6
    println(maxel(vecsub(yout,yexp)))
    var localflag = maxel(vecsub(yout,yexp)) > sbeps
    
    if (globalflag == false)   
         globalflag  =   localflag
    
    if (localflag) {
      fail("*** rk4: Inaccurate Runge-Kutta step");
      
    }

    if (globalflag) println("Failed\n")
    else  println("Passed\n")    

```