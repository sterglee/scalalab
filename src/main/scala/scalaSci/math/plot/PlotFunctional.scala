
package scalaSci.math.plot

/* It is particularly convenient to be able to plot a function specified as a text string, within a specified domain.
For example we like to write:
   splot("sin(3.4*x)*cos(0.12*x)", -5, 15)
to plot the corresponding function in the interval  [-5, 15]. 
Also we like to be able to plot easily any function y = f(x) or z=f(x,y)
*/
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.Settings

import scalaExec.Interpreter.ControlInterpreter._

object fPlotObj {
  var globalFunction1d: Function1[Double, Double]  = _   // 1-D function 
  var globalFunction2d: Function2[Double, Double, Double] = _   // 2-D function
  
  def mkFunction1d(expr: String) = {

// instantiate  a Function1 trait with the function's expression 
    var classText = """scalaSci.math.plot.fPlotObj.globalFunction1d = 
    new scala.Function1[Double, Double] { 
      def apply(x: Double) = """+expr+
""" 
}"""

if (scalaExec.Interpreter.GlobalValues.globalInterpreter == null) {  
  // global interpreter not initialized
   val settings = {
               val set = new Settings()
// detect the paths of the core ScalaLab jars at the local file system
               _root_.scalalab.JavaUtilities.detectBasicPaths
       
      prepareSettings(set)        
    
      set.classpath.append(scalalab.JavaGlobals.jarFilePath)    
      set.classpath.append(scalalab.JavaGlobals.compFile)  // Scala Compiler
      set.classpath.append(scalalab.JavaGlobals.libFile)     // Scala Libraries
      if (scalalab.JavaGlobals.reflectFile != null)
        set.classpath.append(scalalab.JavaGlobals.reflectFile)  // scala-reflect file
      set.classpath.append(scalalab.JavaGlobals.swingFile)  // Scala Swing

         set.classpath.append(scalalab.JavaGlobals.ejmlFile)   // EJML file
      set.classpath.append(scalalab.JavaGlobals.rsyntaxTextAreaFile)
      set.classpath.append(scalalab.JavaGlobals.jblasFile)   // JBLAS file
      set.classpath.append(scalalab.JavaGlobals.jsciFile)   // JSci library
      set.classpath.append(scalalab.JavaGlobals.javacppFile)
      set.classpath.append(scalalab.JavaGlobals.JASFile)    // Java Algebra System
      set.classpath.append(scalalab.JavaGlobals.LAPACKFile)    // LAPACK library file
      set.classpath.append(scalalab.JavaGlobals.ARPACKFile)    // ARPACK library file
      
        
      set
   }
     scalaExec.Interpreter.GlobalValues.globalInterpreter = new IMain(settings, null) 
  }
    scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(classText)
  }
  
  
  def mkFunction2d(expr: String) = {

// instantiate  a Function2 trait with the function's expression 
    var classText = """scalaSci.math.plot.fPlotObj.globalFunction2d = 
    new scala.Function2[Double, Double, Double] { 
      def apply(x: Double, y: Double) = """+expr+
""" 
}"""

  if (scalaExec.Interpreter.GlobalValues.globalInterpreter == null) {
    val settings = {
               val set = new Settings()
// detect the paths of the core ScalaLab jars at the local file system
               _root_.scalalab.JavaUtilities.detectBasicPaths
       
          
    prepareSettings(set)        
    
      set.classpath.append(scalalab.JavaGlobals.jarFilePath)    
      set.classpath.append(scalalab.JavaGlobals.compFile)  // Scala Compiler
      set.classpath.append(scalalab.JavaGlobals.libFile)     // Scala Libraries
      if (scalalab.JavaGlobals.reflectFile != null)
        set.classpath.append(scalalab.JavaGlobals.reflectFile)  // scala-reflect file
      set.classpath.append(scalalab.JavaGlobals.swingFile)  // Scala Swing
      set.classpath.append(scalalab.JavaGlobals.ApacheCommonsFile)   // Apache Common Maths current version file
      set.classpath.append(scalalab.JavaGlobals.ejmlFile)   // EJML file
      set.classpath.append(scalalab.JavaGlobals.rsyntaxTextAreaFile)
      set.classpath.append(scalalab.JavaGlobals.jblasFile)  // JBLAS File
      set.classpath.append(scalalab.JavaGlobals.jsciFile)   // JSci library
      set.classpath.append(scalalab.JavaGlobals.JASFile)    // Java Algebra System
      set.classpath.append(scalalab.JavaGlobals.LAPACKFile)    // LAPACK library file
      set.classpath.append(scalalab.JavaGlobals.ARPACKFile)    // ARPACK library file
      
      set
   }
     scalaExec.Interpreter.GlobalValues.globalInterpreter = new IMain(settings, null) 
  } 
   
  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(classText)
  }
  
  
}


// splot("sin(x)", 1, 5)
object PlotFunctional {
  
  // evaluate and plot the function y=f(x), given by the expression within [low, high].
  // Note that the variable should be named 'x' '
  def splot(expr: String, low: Double, high: Double, color: java.awt.Color=java.awt.Color.RED, linePlotsFlag: Boolean=true, nP: Int = 2000): Unit = {
    
    fPlotObj.mkFunction1d(expr)  // make the 1D function object according to the expr
    fplot(scalaSci.math.plot.fPlotObj.globalFunction1d, low, high, color, linePlotsFlag, nP )
  }

 def splot2d(expr: String, lowx: Double, highx: Double, lowy: Double, highy: Double, color: java.awt.Color = java.awt.Color.GREEN, 
            drawLines: Boolean = true,
            nPx: Int = 100, nPy: Int=100): Unit = {
    
    fPlotObj.mkFunction2d(expr)  // make the 2-D function object according to the expr
    fplot2d(scalaSci.math.plot.fPlotObj.globalFunction2d, lowx, highx, lowy, highy, color, drawLines, nPx, nPy )
  }

  
// plot with VISAD
// vsplot("sin(x)", 1, 5)
 /*def vsplot(expr: String, low: Double, high: Double, nP: Int = 2000): Unit = {
    
    fPlotObj.mkFunction1d(expr)
    vfplot(scalaSci.math.plot.fPlotObj.globalFunction1d, low, high, nP )
  }*/

// plot with JFreeChart
// jsplot("sin(x)", 1, 5)
 def jsplot(expr: String, low: Double, high: Double, nP: Int = 2000): Unit = {
    
    fPlotObj.mkFunction1d(expr)
    jfplot(scalaSci.math.plot.fPlotObj.globalFunction1d, low, high, nP )
  }
   
// f1d: the function to plot
  /*
 def  fx(x:Double) = { x -sin(x)*x }
 fplot(fx,  1, 10, Color.BLUE, true,100)
 
 def fy(x: Double) = { x-2*cos(x)*x*x }
 fplot(fy, 1, 10, Color.GREEN, false, 1000)
 */

  def fplot(f1d:  Double=> Double,  low: Double, high: Double, color:java.awt.Color= java.awt.Color.RED, linePlotsFlag: Boolean=true, nP:Int=2000 ): Unit = {
    var dx:Double= (high-low)/nP
    var valsx = new Array[Double](nP)
    var valsy = new Array[Double](nP)
    
    var currx:Double=low
    for (k<-0 until nP) {
      valsx(k) = currx
      valsy(k) = f1d(currx)
      currx =  currx+dx
    }
    
    if (linePlotsFlag) 
       plot.linePlotsOn()
     else
       plot.scatterPlotsOn()
    scalaSci.math.plot.plotTypes.plot(valsx, valsy, color)
    
  }
 
// f2d: the 2-D function to plot
  /*
   def  fxy(x:Double, y: Double) = { x*sin(x)+x*y*cos(y) }
   figure3d(1)
   fplot2d(fxy,  1, 10, 1, 5, Color.BLUE, true )
   def fxy2(x: Double, y: Double) = { x*y*cos(x*y)-x*sin(x) }
   figure3d(2)
   fplot2d(fxy2, 1, 10, 1, 5, Color.RED, true)
 
  figure3d(3); splot2d("x*y*cos(x*y)", -4, 4, -10, 10)
 */
  
def fplot2d(f2d:  (Double, Double) => Double,  lowx: Double, highx: Double, lowy: Double, highy: Double,  
            color: java.awt.Color = java.awt.Color.GREEN, 
            drawLines: Boolean = true,
             nPx :Int=100, nPy:Int=100, title: String = "Surface Plot of function "): Unit = {
    var dx:Double = (highx-lowx) / nPx
    var dy:Double = (highy-lowy) / nPy
    
    var valsx = new Array[Double](nPx)    // the x-axis
    var valsy = new Array[Double](nPy)   // the y-axis
    var valsz = Array.ofDim[Double](nPx, nPy)   // the function values
    var currx: Double=lowx
    var curry: Double = lowy
       // sample x-axis
    for (xp<-0 until nPx) {
      valsx(xp) = currx
      currx += dx
    }
     // sample y-axis
    for (yp<-0 until nPy)
       {
      valsy(yp) = curry
      curry +=  dy
    }
    // compute function points
    for (xp<-0 until nPx)
      for (yp<-0 until nPy)
        valsz(xp)(yp) = f2d(valsx(xp), valsy(yp))
    
    
    scalaSci.math.plot.plotTypes.surf(valsx, valsy, valsz, color, drawLines,  false,  "Functional Surface Plot")
    
  }
 


  // f1d: the function to plot. Uses JFreeChart system
/*
 def  fx(x:Double) = { x -sin(x)*x }
 jfplot(fx,  1, 10)
 */
  def jfplot(f1d:  Double=> Double,  low: Double, high: Double, nP:Int=2000): Unit = {
    var dx:Double= (high-low)/nP
    var valsx = new Array[Double](nP)
    var valsy = new Array[Double](nP)
    
    var currx:Double=low
    for (k<-0 until nP) {
      valsx(k) = currx
      valsy(k) = f1d(currx)
      currx =  currx+dx
    }
    
    JFplot.jFigure.jplot(valsx, valsy)
    
  }
 
  
  }

