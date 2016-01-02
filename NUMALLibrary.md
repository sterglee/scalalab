# `The NUMAL Java Scientific Library in ScalaLab ` #

`NUMAL is a powerful Java scientific library that can be exploited in ScalaLab either by its Java interface or with Scala based code. We present a few examples of using NUMAL with Scala code`


# Examples #

## Example 1: Demonstration of Least Squares fitting with the Gauss-Newton method ##
```

    import _root_.java.util.Vector ; 
    import _root_.numal._ ; 
    import _root_.numal.Algebraic_eval._;
    import _root_.numal.Analytic_eval._
    import _root_.numal.Analytic_problems._
    import _root_.numal.Approximation._
    import _root_.numal.Basic._;
    import _root_.numal.FFT._;
    import  _root_.numal.Linear_algebra._; 
    import _root_.numal.Special_functions._;
    import java.text.DecimalFormat 
    
    
    // EXAMPLE 1


// determine the parameters p_1, p_2, and p_3 of the best fit of the function
//   g(x, p) = p_1+p_2*exp(p_3*x)
// when x = x_i, to data readings y_i (i=1,..., b), where
// x_1, ..., x_6 are -5, -3, -1, 1, 3, 5, and y_1, ..., y_6 are 
// 127.0, 151.0, 379.0, 421.0, 460.0, 426.0
// The components of the residual vector are:
//     p_1+p_2*exp(p3*x_i)-y_i (i=1,..,6)




object test_gssnewton extends AnyRef  with AP_gssnewton_methods {
   var x = new AD(7)
   var y = new AD(7)

   def run {
      var fiveDigit = new DecimalFormat("0.00000e0")
      var oneDigit = new DecimalFormat("0.0")

      var in = new AD(8)
      var out = new AD(10)
      var g = new AD(7)
      var par = new AD(4)
      var v = Array.ofDim[Double](4, 4)

     
// values to fit
      x(1) = -5.0;  x(2) = -3.0;  x(3) = -1.0;  x(4) = 1.0;  x(5) = 3.0;  x(6) = 5.0;
      y(1) = 127.0;  y(2) = 151.0;  y(3) = 379.0;  y(4) = 421.0;  y(5) = 460.0;  y(6) = 426.0;

      par(1) = 580.0;  par(2) = -180.0;  par(3) = -0.160   // exit: the calculated least squares solution

    var m = 6   // the number of equations
    var n = 3 // the number of unknown variables
    var rv = new Array[Double](m+1)   // rv(1:m): the residual vector at the calculated solution
    var jjinv = Array.ofDim[Double](n+1, n+1)

    in(0) = 1.0e-6;   // the machine's precision
    in(1) = 1.0e-6;  in(2) = 1.0e-6
    in(5) = 75.0
    in(4) = 1.0e-6; in(6) = 14.0;  in(7) = 1.0

   
    Analytic_problems.gssnewton(m, n, par, g, v, test_gssnewton, in, out)


   scatterPlotsOn
   plot(x, y, Color.GREEN, "Points to fit")  // plot the data readings
  
  var t  = inc(-6, 0.01, 6)
  var fx =  par(1) + par(2)*exp(par(3)*t)
  hold("on")
  linePlotsOn
  plot(t, fx,  Color.BLUE, "Fitted curve")
  title("Demonstration Least Squares fitting with the Gauss-Newton method")

//   g(x, p) = p_1+p_2*exp(p_3*x)

    
println("Parameters: \n  "+ fiveDigit.format(par(1)) + "  "+
        fiveDigit.format(par(2))+ "   "+fiveDigit.format(par(3)) +
        "\n\nOUT: \n   "+ fiveDigit.format(out(7))+"\n "+ fiveDigit.format(out(2))+"\n  "+
        fiveDigit.format(out(6))+"\n "+ fiveDigit.format(out(2))+"\n  "+
        fiveDigit.format(out(3))+"\n  "+fiveDigit.format(out(4))+"\n  "+
        fiveDigit.format(out(5))+"\n  "+fiveDigit.format(out(1))+"\n  "+
        fiveDigit.format(out(7))+"\n  "+fiveDigit.format(out(8))+"\n  "+
        fiveDigit.format(out(9))+
  "\n\n Last residual vector: \n "+
        oneDigit.format(g(1)) + "  "+oneDigit.format(g(2))+"  "+
        oneDigit.format(g(3))+ "  "+oneDigit.format(g(4))+"  "+
        oneDigit.format(g(5))+ "  "+oneDigit.format(g(6)))
 }

def  funct(m: Int, n: Int, par: AD, g: AD): Boolean = {
   for (i<-1 to m) {
      if (par(3)*x(i) > 680.0)  return  false
      g(i) = par(1)+par(2)*Math.exp(par(3)*x(i)) - y(i)
 }
  return true
}

def  jacobian(m: Int, n: Int, par: AD, g: AD, jac: AAD) =  {
  var ex = 0.0 
  for (i<-1 to m) {
     jac(i)(1) = 1.0
     ex = Math.exp(par(3)*x(i))
     jac(i)(2) = ex
     jac(i)(3) = x(i)*par(2)*ex
     }
  }
}

 test_gssnewton.run   // call the test routine

```


## EXAMPLE 2: Demonstration of Least Squares fitting with the Marquardt method ##

```

    import _root_.java.util.Vector ; 
    import _root_.numal._ ; 
    import _root_.numal.Algebraic_eval._;
    import _root_.numal.Analytic_eval._
    import _root_.numal.Analytic_problems._
    import _root_.numal.Approximation._
    import _root_.numal.Basic._;
    import _root_.numal.FFT._;
    import  _root_.numal.Linear_algebra._; 
    import _root_.numal.Special_functions._;
    import java.text.DecimalFormat 
    

// determine the parameters p_1, p_2, and p_3 of the best fit of the function
//   g(x, p) = p_1+p_2*exp(p_3*x)
// when x = x_i, to data readings y_i (i=1,..., b), where
// x_1, ..., x_6 are -5, -3, -1, 1, 3, 5, and y_1, ..., y_6 are 
// 127.0, 151.0, 379.0, 421.0, 460.0, 426.0
// The components of the residual vector are:
//     p_1+p_2*exp(p3*x_i)-y_i (i=1,..,6)



object testMarquardt extends AnyRef  with AP_marquardt_methods {
   var x = new AD(7)
   var y = new AD(7)

   def run {
      var fiveDigit = new DecimalFormat("0.00000e0")
      var oneDigit = new DecimalFormat("0.0")

     
// values to fit
      x(1) = -5.0;  x(2) = -3.0;  x(3) = -1.0;  x(4) = 1.0;  x(5) = 3.0;  x(6) = 5.0;
      y(1) = 127.0;  y(2) = 151.0;  y(3) = 379.0;  y(4) = 421.0;  y(5) = 460.0;  y(6) = 426.0;

      var par = new AD(4)    // entry: an approximation to a least squares solution of the system
      par(1) = 580.0;  par(2) = -180.0;  par(3) = -0.160   // exit: the calculated least squares solution

    var m = 6   // the number of equations
    var n = 3 // the number of unknown variables
    var rv = new AD(m+1)   // rv(1:m): the residual vector at the calculated solution
    var jjinv =  Array.ofDim[Double](n+1, n+1)

    var in = new AD(7)
    var out = new AD(8)
    in(0) = 1.0e-6;   // the machine's precision
    in(3) = 1.0e-4;    // the absolute tolerance for the difference between the Euclidean norm of the ultimate and penultimate residual vector
    in(4) = 1.0e-1;  // the relative tolerance for the difference between the Euclidean norm of the ultimate and penultimate residual vector
    in(5) = 75.0;   // the maximum number of calls of func allowed
    in(6) = 1.0e-2;  // a starting value used for the relation between the gradient and the Gauss-Newton direction\

    Analytic_problems.marquardt(m, n, par, rv, jjinv, testMarquardt, in, out)


   scatterPlotsOn
   plot(x, y, Color.GREEN, "Points to fit")  // plot the data readings
  
  var t  = inc(-6, 0.01, 6)
  var fx =  par(1) + par(2)*exp(par(3)*t)
  hold("on")
  linePlotsOn
  plot(t, fx,  Color.BLUE, "Fitted curve")
  title("Demonstration Least Squares fitting with the Marquardt method")
//   g(x, p) = p_1+p_2*exp(p_3*x)

    
println("Parameters: \n  "+ fiveDigit.format(par(1)) + "  "+
        fiveDigit.format(par(2))+ "   "+fiveDigit.format(par(3)) +
        "\n\nOUT: \n   "+ fiveDigit.format(out(7))+"\n "+ fiveDigit.format(out(2))+"\n  "+
        fiveDigit.format(out(6))+"\n "+ fiveDigit.format(out(3))+"\n  "+
        fiveDigit.format(out(4))+"\n  "+fiveDigit.format(out(5))+"\n  "+
        fiveDigit.format(out(1))+"\n\n Last residual vector: \n "+
        oneDigit.format(rv(1)) + "  "+oneDigit.format(rv(2))+"  "+
        oneDigit.format(rv(3))+ "  "+oneDigit.format(rv(4))+"  "+
        oneDigit.format(rv(5))+ "  "+oneDigit.format(rv(6)))
 }

def  funct(m: Int, n: Int, par: Array[Double], rv: Array[Double]): Boolean = {
   for (i<-1 to m) {
      if (par(3)*x(i) > 680.0)  return  false
      rv(i) = par(1)+par(2)*Math.exp(par(3)*x(i)) - y(i)
 }
  return true
}

def  jacobian(m: Int, n: Int, par: Array[Double], rv: Array[Double], jac: Array[Array[Double]]) =  {
  var ex = 0.0 
  for (i<-1 to m) {
     jac(i)(1) = 1.0
     ex = Math.exp(par(3)*x(i))
     jac(i)(2) = ex
     jac(i)(3) = x(i)*par(2)*ex
     }
  }
}

 testMarquardt.run   // call the test routine
```