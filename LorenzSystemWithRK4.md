# Introduction #

`We demonstrate  solving the Lorenz system with while loops and for loops. While loops perform much better!`

`Specifically I obtained for the while based script, tm = 0.071 sec, and for the script based on for-loops, tm = 3.9 sec, i.e. about 55 times slower!! `

`However, the ` _`cfor`_ `loop adapted from ` **`spire`** `,has the same performance as the while loop, it performs even better than Java!`

# Lorenz system with While loops #
```

 
import com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub
import com.nr.ode._
import com.nr.sf.Bessjy

// specify the right-hand side of the Lorenz equations
 class rhs_Lorenz extends AnyRef with DerivativeInf {
    def  derivs( x: Double, y: Array[Double], dydx: Array[Double]): Unit = {
      dydx(0) = 10*(y(1)-y(0))
      dydx(1) = -y(0)*y(2)+143*y(0) - y(1)
      dydx(2) = y(0)*y(1)-2.66667*y(2)
    }
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]): Unit = {}
  }

         
  
 // Given values for the variables y[0..n-1] and their derivatives dydx[0..n-1] known at x,
  // use the fourth-order Runge-Kutta method to advance the solution over an interval h and 
  // return the incremented variables as yout[0..n-1].The user supplies the routine derivs(x, y, dydx),
  // which returns derivatives at x
  def  srk4(y: Array[Double], dydx: Array[Double], x: Double, h: Double, yout:Array[Double], derivs: DerivativeInf) =  {
    var n = y.length
    var dym = new Array[Double](n)
    var dyt = new Array[Double](n)
    var yt =new Array[Double](n)
    
    var hh = h * 0.5
    var h6 = h/6.0
    var xh = x+hh
    var i=0
    while ( i < n) { yt(i) = y(i)+hh*dydx(i); i+=1 }
    derivs.derivs(xh,yt,dyt)
    i = 0
    while (i < n) { yt(i)=y(i)+hh*dyt(i); i += 1 }
    derivs.derivs(xh,yt,dym)
    i = 0
    while (i < n) {
      yt(i) = y(i) + h*dym(i)
      dym(i) += dyt(i)
      i += 1
    }
    derivs.derivs(x+h,yt,dyt)
    i = 0
    while (i <  n) {
      yout(i) = y(i)+h6*(dydx(i)+dyt(i)+2.0*dym(i))
      i += 1
      }
  }




 var lo = new rhs_Lorenz()   // construct a Lorenz object
 var K = 3 // order of our system (Lorenz here)
 var dydx = new Array[Double](K)  // keeps the return results
  
 tic()
  // now we can use Runge-Kutta 4th order formulas to integrate
  var N=200000  // number of points to compute
  var Ntransients = 520  // drop these initial "out-of-orbit" evaluations
  var h = 0.0001    // Runge-Kutta step size
  var yout  = new Array[Double](K)  // keeps the returned results from our integration routine

  var orbit = Array.ofDim[Double](3, N)   // accumulates the orbit

  var y0 = Array(0.1, 0.2, 0.11)        // initial values of the variables
  var t = 0.0  // where the derivatives are computed (e.g. time axis)
  lo.derivs(t, y0, dydx)  // derivatives at y0 are returned at dydx
  var k=0
  while (k <=  Ntransients) { // let the system stabilize
  	srk4(y0, dydx,  t, h, yout, lo)
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
    k += 1
  }
  
  k = 0
  while (k < N) {
    srk4(y0, dydx,  t, h, yout, lo)
    orbit(0)(k) = yout(0); orbit(1)(k) = yout(1); orbit(2)(k) = yout(2);   //   save the orbit's point
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
    k += 1
  }

var tm = toc()
  
  linePlotsOn()
  plot(orbit, "steps = "+N+", time = "+tm)

```


# Lorenz System with For loops #

```

 
import com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub
import com.nr.ode._
import com.nr.sf.Bessjy

// specify the right-hand side of the Lorenz equations
 class rhs_Lorenz extends AnyRef with DerivativeInf {
    def  derivs( x: Double, y: Array[Double], dydx: Array[Double]): Unit = {
      dydx(0) = 10*(y(1)-y(0))
      dydx(1) = -y(0)*y(2)+143*y(0) - y(1)
      dydx(2) = y(0)*y(1)-2.66667*y(2)
    }
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]): Unit = {}
  }

         
  
 // Given values for the variables y[0..n-1] and their derivatives dydx[0..n-1] known at x,
  // use the fourth-order Runge-Kutta method to advance the solution over an interval h and 
  // return the incremented variables as yout[0..n-1].The user supplies the routine derivs(x, y, dydx),
  // which returns derivatives at x
  def  srk4(y: Array[Double], dydx: Array[Double], x: Double, h: Double, yout:Array[Double], derivs: DerivativeInf) =  {
    var n = y.length
    var dym = new Array[Double](n)
    var dyt = new Array[Double](n)
    var yt =new Array[Double](n)
    
    var hh = h * 0.5
    var h6 = h/6.0
    var xh = x+hh
    for ( i <- 0 until n) yt(i) = y(i)+hh*dydx(i)
    derivs.derivs(xh,yt,dyt)
    for (i <- 0 until n) yt(i)=y(i)+hh*dyt(i)
    derivs.derivs(xh,yt,dym)
    for (i <- 0 until n) {
      yt(i) = y(i) + h*dym(i)
      dym(i) += dyt(i)
    }
    derivs.derivs(x+h,yt,dyt)
    for (i <- 0 until n)
      yout(i) = y(i)+h6*(dydx(i)+dyt(i)+2.0*dym(i))
  }




 var lo = new rhs_Lorenz()   // construct a Lorenz object
 var K = 3 // order of our system (Lorenz here)
 var dydx = new Array[Double](K)  // keeps the return results
  
 tic()
  // now we can use Runge-Kutta 4th order formulas to integrate
  var N=200000  // number of points to compute
  var Ntransients = 520  // drop these initial "out-of-orbit" evaluations
  var h = 0.0001    // Runge-Kutta step size
  var yout  = new Array[Double](K)  // keeps the returned results from our integration routine

  var orbit = Array.ofDim[Double](3, N)   // accumulates the orbit

  var y0 = Array(0.1, 0.2, 0.11)        // initial values of the variables
  var t = 0.0  // where the derivatives are computed (e.g. time axis)
  lo.derivs(t, y0, dydx)  // derivatives at y0 are returned at dydx
  for (k <- 0 to Ntransients) { // let the system stabilize
  	srk4(y0, dydx,  t, h, yout, lo)
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
  }
  
  for (k <- 0 until N) {
    srk4(y0, dydx,  t, h, yout, lo)
    orbit(0)(k) = yout(0); orbit(1)(k) = yout(1); orbit(2)(k) = yout(2);   //   save the orbit's point
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
  }

var tm = toc()
  
  linePlotsOn()
  plot(orbit, "steps = "+N+", time = "+tm)

```

# Lorenz with the cfor loop, adapted from spire #

```
 
import com.nr.test.NRTestUtil.maxel
import com.nr.test.NRTestUtil.vecsub
import com.nr.ode._
import com.nr.sf.Bessjy

// specify the right-hand side of the Lorenz equations
 class rhs_Lorenz extends AnyRef with DerivativeInf {
    def  derivs( x: Double, y: Array[Double], dydx: Array[Double]): Unit = {
      dydx(0) = 10*(y(1)-y(0))
      dydx(1) = -y(0)*y(2)+143*y(0) - y(1)
      dydx(2) = y(0)*y(1)-2.66667*y(2)
    }
    def  jacobian(x: Double, y: Array[Double], dfdx: Array[Double], dfdy: Array[Array[Double]]): Unit = {}
  }

         
  
 // Given values for the variables y[0..n-1] and their derivatives dydx[0..n-1] known at x,
  // use the fourth-order Runge-Kutta method to advance the solution over an interval h and 
  // return the incremented variables as yout[0..n-1].The user supplies the routine derivs(x, y, dydx),
  // which returns derivatives at x
  def  srk4(y: Array[Double], dydx: Array[Double], x: Double, h: Double, yout:Array[Double], derivs: DerivativeInf) =  {
    var n = y.length
    var dym = new Array[Double](n)
    var dyt = new Array[Double](n)
    var yt =new Array[Double](n)
    
    var hh = h * 0.5
    var h6 = h/6.0
    var xh = x+hh

    cfor (0) (_<n, _+1) {i =>  yt(i) = y(i)+hh*dydx(i) }
    derivs.derivs(xh,yt,dyt)

    cfor(0) (_<n, _+1) {i => yt(i)=y(i)+hh*dyt(i) }
    
    derivs.derivs(xh,yt,dym)

    cfor(0) (_<n, _+1) {i => 
      yt(i) = y(i) + h*dym(i)
      dym(i) += dyt(i)
    }
    
    derivs.derivs(x+h,yt,dyt)
    cfor(0) (_<n, _+1) { i =>
      yout(i) = y(i)+h6*(dydx(i)+dyt(i)+2.0*dym(i))
    }
  }




 var lo = new rhs_Lorenz()   // construct a Lorenz object
 var K = 3 // order of our system (Lorenz here)
 var dydx = new Array[Double](K)  // keeps the return results
  
 tic()
  // now we can use Runge-Kutta 4th order formulas to integrate
  var N=200000  // number of points to compute
  var Ntransients = 520  // drop these initial "out-of-orbit" evaluations
  var h = 0.0001    // Runge-Kutta step size
  var yout  = new Array[Double](K)  // keeps the returned results from our integration routine

  var orbit = Array.ofDim[Double](3, N)   // accumulates the orbit

  var y0 = Array(0.1, 0.2, 0.11)        // initial values of the variables
  var t = 0.0  // where the derivatives are computed (e.g. time axis)
  lo.derivs(t, y0, dydx)  // derivatives at y0 are returned at dydx

  cfor (0)(_<=Ntransients, _+1) {  // let the system stabilize
        k => {
        srk4(y0, dydx,  t, h, yout, lo)
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
        }
  }
  
  cfor(0) (_ < N, _+1) { k =>
    srk4(y0, dydx,  t, h, yout, lo)
    orbit(0)(k) = yout(0); orbit(1)(k) = yout(1); orbit(2)(k) = yout(2);   //   save the orbit's point
    y0(0) = yout(0); y0(1) = yout(1); y0(2) =  yout(2);  // next step on is from where we left
    t += h  // advance  time
    lo.derivs(t, y0, dydx) 
  }

var tm = toc()
  
  linePlotsOn()
  plot(orbit, "steps = "+N+", time = "+tm)

```