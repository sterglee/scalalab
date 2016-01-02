# Solving Ordinary Differential Equations #

> `NUMAL provides powerful routines for solving Ordinary Differential Equations (ODEs) numerically. ScalaLab can exploit them effectively. The ODE Wizard facilitates some common cases. We present some examples. `


# Examples #

## `Example 1 - Lorenz attractor with Runge-Kutta ` ##

```
// EXAMPLE 1

 import scala._ 
 import scalaSci._ 
 import scalaSci.Vec._ 
 import scalaSci.Mat._ 
 import java.util.Vector 
 import numal._ 
 


class   IntegrationObject  extends Object       with AP_rke_methods 
  { 
def der(n: Int, t: Double, y: Array[Double]):Unit = 
{ 
  var    xx=y(1);   var   yy=y(2);    var zz=y(3); 

    y(1) = 10*(yy-xx);  
    y(2) = -xx*zz+143*xx - yy;  
    y(3) = xx*yy - 2.66667*zz;  
  } 
 

def  out(n: Int, t: Array[Double], te: Array[Double], 
              y: Array[Double],  data: Array[Double]):Unit =  {  } 
}

 
 close("all")  // close any open figures 
 var  n= 3;  // the number of equations of the system
var  x = new Array[Double](1)     // entry:   x(0) is the initial value of the independent variable  
var  xe = new Array[Double](1)    //  entry:  xe(0) is the final value of the independent variable    
var y = new Array[Double](n+1)   // entry: the dependent variable, the initial values at x = x0 
var data = new Array[Double](7)   // in array data one should give: 
                                        //     data(1):   the relative tolerance 
                                        //     data(2):  the absolute tolerance  
                                        //  after each step data(3:6) contains:  
                                        //      data(3):  the steplength used for the last step 
                                       //      data(4):  the number of integration steps performed 
                                       //      data(5):  the number of integration steps rejected  
                                        //      data(6):  the number of integration steps skipped 
                                 // if upon completion of rke data(6) > 0, then results should be considered most criticallly 
 fi = true;                        // if fi is true then the integration starts at x0 with a trial step xe-x0;  
                                        // if fi is false then the integration is continued with a step length data(3)* sign(xe-x0) 
data(1) = 1.0e-6;  data(2) = 1.0e-6; 

var xOut:Vector[Array[Double]] = new Vector();  
var yOut:Vector[Array[Double]] = new Vector();  
// a Scala class that implements the AP_rke_methods interface should be specified 
// The AP_rke_methods interface requires the implementation of two procedures: 
//    void der(int n, double t, double v[]) 
//              this procedure performs an evaluation of the right-hand side of the system with dependent variable v[1:n] and 
//              and independent variable t; upon completion of der the right-hand side should be overwritten on v[1:n] 
//    void out(int n, double x[], double xe[], double y[], double data[]) 
//              after each integration step performed, out can be used to obtain information from the solution process, 
//              e.g., the values of x, y[1:n], and data[3:6]; out can also be used to update data, but x and xe remain unchanged 


 var xStart = 0.0;
 var xEnd =  100.0;   // start and end values of integration 
 
y(1) = 0.7429591708480034; 
y(2) = 0.9532613610160809; 
y(3) = 0.3627310584465294; 
 x(0) = xStart; 
  xe(0) = xEnd; 

 var integrationObj  = new IntegrationObject 
tic() 
var fi = true 

Analytic_problems.rke(x, xe, n, y, integrationObj,  data, fi,  xOut, yOut); 

 var timeCompute = toc() 
var plotTitle = "Lorenz attractor in ScalaSci, time =  "+timeCompute+ " Runge-Kutta (rke()),  integrating from "+xStart+", to tEnd= "+xEnd  
var color = Color.RED 
  figure3d(1); plotV(yOut, color, plotTitle) 


```


## `Example  2 - The Lorenz system with Multistep methods ` ##
```

 import scala._ 
 import scalaSci._ 
 import scalaSci.Vec._ 
 import scalaSci.Mat._ 
 import java.util.Vector 
 import numal._ 
 import  scalaSci.math.plot.plot._  


class   IntegrationObject  extends Object       with AP_multistep_methods 
  { 
  // an example template implementing the Lorenz attractor 
def deriv(df: Array[Double], n: Int, x: Array[Double],  y: Array[Double]):Unit = { 
    var xx=y(1);    var yy=y(2);    var  zz=y(3); 
   df(1) = 10*(yy-xx); 
     df(2) = -xx*zz+143*xx - yy; 
     df(3) = xx*yy - 2.66667*zz; 
   } 
 
     def available( n: Int,  x: Array[Double], y: Array[Double],  jac: Array[Array[Double]]):Boolean =  { 
       jac(1)(1) = -10;     jac(1)(2) = 10;  jac(1)(3) = 0; 
       jac(2)(1) = -y(3);   jac(2)(2) = -1;  jac(2)(3) = -y(1); 
       jac(3)(1) = y(2);    jac(3)(2) = 0;  jac(3)(3) = -2.66; 

        return true; 
     } 

   def out(h: Double, k: Int, n: Int, 
       x: Array[Double], y: Array[Double]): Unit = { }
}

 var n= 3; // the number of equations of the system 
var first = new  Array[Boolean](1);   // if first is true then the procedure starts the integration with a first order Adams method 
// and a steplength equal to hmin,  upon completion of a call, first is set to false 
first(0)=true; 
var btmp = new Array[Boolean](3) 
var itmp = new Array[Int](4) 
var xtmp = new Array[Double](8) 
var x = new Array[Double](1) 
var y = new Array[Double](6*n+1) 
var ymax = new Array[Double](3 + 1 ) 
var save = new Array[Double](7*n+39)  //    in this array the procedure stores information which can be used in a continuing call 
          // with first = false; also the following messages are delivered: 
          //      save[38] == 0;  an Adams method has been used  
          //      save[38] == 1;  the procedure switched to Gear's method 
          //      save[37] == 0;  no error message  
          //      save[37] == 1; with the hmin specified the procedure cannot handle the nonlinearity (decrease hmin!) 
          //      save[36] ;  number of times that the requested local error bound was exceeded   
         //      save[35] ;  if save[36] is nonzero then save[35] gives an estimate of the maximal local error bound, otherwise save[35]=0 


  var   jac = new Array[Array[Double]](n+1)  
var k=0;     while (k<=n) {        jac(k) = new Array[Double](n+1);    k += 1; } 
var xOut:Vector[Array[Double]] = new Vector() 
var yOut:Vector[Array[Double]] = new Vector() 
var hmin=1.0e-10;    var eps=1.0e-9 

 y(1)=0.12; y(2)=0.3; y(3)=0.12; 
ymax(1) = 0.00001;   ymax(2) = 0.00001;    ymax(3) = 0.00001;    var tstart = 0.0;    x(0) = tstart 
var xendDefault ="100.0"  // end point of integration, default value 
var prompt = "Specify the end integration value" 
var inVal  = JOptionPane.showInputDialog(prompt, xendDefault) 
var tend = inVal.toDouble 

 var integrationObj  = new IntegrationObject 
tic() 

Analytic_problems.multistep(x, tend,y,hmin,5,ymax,eps,first, save, integrationObj, jac, true,n,btmp,itmp,xtmp, xOut, yOut) 
var  runTime =  toc() 
var plotTitle = "Lorenz system, method Multistep,  integrated from "+tstart+", to tEnd= "+tend+", runTime = "+runTime 
var color = Color.RED 
  figure3d(1); plotV(yOut, color, plotTitle) 

```


## `Example 3 - The Double Scroll attractor system with DiffSys` ##

```
 import scala._ 
 import scalaSci._ 
 import scalaSci.Vec._ 
 import scalaSci.Mat._ 
 import java.util.Vector 
 import numal._ 
 import  scalaSci.math.plot.plot._  


class   IntegrationObject  extends Object       with AP_diffsys_methods 
  { 
  // an example template implementing the Double Scroll attractor 
def  derivative(n: Int, x: Double, y: Array[Double], dy: Array[Double]): Unit =  { 
var xx=y(1); var yy=y(2); var zz=y(3); 
dy(1) = 10*(yy-xx); 
dy(2) = -xx*zz+143*xx - yy; 
dy(3) = xx*yy - 2.66667*zz; 
   }


 def  output(n: Int, x: Array[Double],  
               xe:Double, y: Array[Double], dy: Array[Double]): Unit = { } 
}
var tol = 0.0000000000004 
 var aeta = tol; var reta = tol
 var n = 3; // the number of equations of the system 
var x = new Array[Double](1)  // entry: x(0) is the initial value of the independent variable 
var y = new Array[Double](n+1)   // entry: the dependent variable, the initial values at x = x0 
aeta = tol  // aeta: required absolute precision in the integration process 
reta = tol // reta: required relative precision in the integration process 
var s = new Array[Double](n+1) 
var h0=0.000001  // h0: the initial step to be taken 
var xOut:Vector[Array[Double]] = new Vector() 
var yOut:Vector[Array[Double]] = new Vector() 
y(1)=0.4; y(2)= -0.3; y(3)=0.9; 

 x(0)=0;  var xe = 720; 

 var integrationObj  = new IntegrationObject 

 tic() 
Analytic_problems.diffsys(x, xe, n, y, integrationObj, aeta, reta , s, h0, xOut, yOut) 
var timeCompute = toc() 
var plotTitle = "Double Scroll attractor with ScalaSci, method DiffSys,  time "+timeCompute+ " end point = "+xe 
var color = Color.RED 
 scatterPlotsOn
 figure3d(1); plotV(yOut, color, plotTitle); 

```