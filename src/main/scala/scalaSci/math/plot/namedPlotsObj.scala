
package scalaSci.math.plot



// implements named plot routines 
// named plots can handle much more conveniently the large number of 
// parameter combinations, used usually with plotting routines
object namedPlotsObj {
  import scalaSci.Vec
  import java.awt.Color
  import scalaSci.math.plot.plot._
  import scalaSci.math.plot.plotTypes._

  
  def nplot(xvec: Vec=null, yvec: Vec=null,  xdd: Array[Double]=null, ydd: Array[Double]=null, 
            yname: String = "x-y plot", plotType: String = "l",    
            color: Color = Color.GREEN, lineWidth: Int=1 ) = {
     if (xvec != null && yvec != null)
  plot(xvec, yvec,  color, plotType,  yname,  lineWidth)
else
  if (xvec !=null && ydd != null)
    plot(xvec, ydd,  color, plotType,  yname,  lineWidth)
else
  if (xdd !=null && yvec != null)
    plot(xdd, yvec,  color, plotType,  yname,  lineWidth)
else
  if (xdd != null && ydd != null)
    plot(xdd, ydd,  color, plotType,  yname,  lineWidth)
            }
  
  def nplot2(x: Vec, y: Vec, z: Vec,  name: String = "x-y Plot", color:Color = Color.GREEN) = 
    plot(x, y, z, color, name)
}

/*
 var t = 0::0.05::10
 var x = sin(0.6*t)
 var y = sin(2.6*t)
 figure(1)
 subplot(2,1,1)
 nplot(xvec=t,  yvec=x)
 subplot(2,1,2)
 nplot(xvec=t,  yvec=x, lineWidth=8)
 
 figure(3)
 var td = Inc(0, 0.1, 30)
 var xd = sin(0.3*td)
 subplot(3,1,1)
 nplot(xdd=td, ydd=xd, color=Color.BLUE)
 
 
  figure(2)
 subplot(3,1,1)
 nplot(xvec=t,  yvec=x, plotType=".")
subplot(3,1,2)
 nplot(xvec=t,  yvec=x, plotType="x")
// demonstrate that we can change the order of named parameters
 subplot(3,1,3) 
 nplot(yvec=x,  xvec=t, plotType="*")

 figure(2)
 nplot2(x=t, y=x, z = y)
 
 */
/*
 

var N=2000
var t= linspace(0, 1, N)
var x = sin(6.5*t)

nplot(t, x, color=Color.YELLOW)   // specifying some parameters only
hold("on")
var y = 3.4*cos(9.4*t)
nplot(t, y, plotType="x", color=Color.GREEN)   // specifying some parameters only

 */