
package scalaSci.math.plot
// a class to plot in an object-oriented way
import java.awt.Color

// object to define the possible line types
object  LineTypes extends Enumeration  {
  type LineType = Value
  val ContinuousLine, DottedLine, RoundDottedLine, CrossDotLine, ThickLine, PatternLine = Value
}


// object to define preferred plotting system
object  PlottingSystems extends Enumeration  {
  type PlottingSystem = Value
  val jMathPlot, jzy3d, visad, jfreechart = Value
}

class PlotController() {
  
  import LineTypes.LineType
  // keeps track of the line type of the controller context
  var  lineType =  LineTypes.ContinuousLine 
// resets the default settings of the graphics context
   def resetDefaultSettings()  = {
       setContinuousLine()
       lineType = LineTypes.ContinuousLine
       setlineWidth(1)
       setColor(Color.RED)
       setxlabel("X-axis")
       setylabel("Y-axis")
       setzlabel("Z-axis")
       }
     
// this routine is useful when we have a particular PlotController context that we want to
// use for some plot operations  
   def passControllerContextToPlotContext() = {
     lineType match {
         case  LineTypes.ContinuousLine => setContinuousLine()
         case  LineTypes.DottedLine => setDottedLine()
         case  LineTypes.RoundDottedLine => setRoundDotLine()
         case  LineTypes.CrossDotLine => setCrossDotLine()
         case  LineTypes.ThickLine => setThickLine()
         case  LineTypes.PatternLine => setPatternLine()
         case  _ => setContinuousLine()
    }
   }
   
// these routines affect AbstractDrawer properties and can be used to change the defaults 
// for external plotting routines that do not specify their own values
       // set drawing to continuous lines
    def setContinuousLine() = { 
      scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.CONTINOUS_LINE 
      lineType = LineTypes.ContinuousLine
  }
       // set drawing to dotted lines
    def setDottedLine() = { 
      scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.DOTTED_LINE 
      lineType = LineTypes.DottedLine
  }
       // set drawing to Round Dot
    def setRoundDotLine() = { 
      scalaSci.math.plot.render.AbstractDrawer.dot_type = scalaSci.math.plot.render.AbstractDrawer.ROUND_DOT
      lineType = LineTypes.RoundDottedLine
  }
       // set drawing to cross dot
    def setCrossDotLine() = { 
      scalaSci.math.plot.render.AbstractDrawer.dot_type = scalaSci.math.plot.render.AbstractDrawer.CROSS_DOT 
      lineType = LineTypes.CrossDotLine
  }
      
    def setThickLine() = { 
      scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.THICK_LINE
      lineType = LineTypes.ThickLine
    }  
      // set the pattern line
    def setPatternLine() =  {
      scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.PATTERN_LINE
      lineType = LineTypes.PatternLine
    }   
    
       // set font
    def setFont(font: java.awt.Font) = { scalaSci.math.plot.PlotGlobals.defaultAbstractDrawerFont = font } 
       // set Color
    def setColor(color: java.awt.Color) = { scalaSci.math.plot.PlotGlobals.defaultAbstractDrawerColor = color } 
       // set line width
    def setlineWidth(lw: Int) = { scalaSci.math.plot.render.AbstractDrawer.line_width = lw } 
    
      // references to the data for plot
    var xData = new Array[Double](1)
    var yData = new Array[Double](1)
    var zData = new Array[Double](1)
        
      // adjust the data references for plotting to the actual data   
    def setX(x: Array[Double]) = { xData = x }  // sets the x-data for plotting
    def setY(y: Array[Double]) = { yData = y }  // sets the y-data for plotting
    def setZ(z: Array[Double]) = { zData = z }  // sets the z-data for plotting
    
    def setX(x: scalaSci.Vec) = { xData = x.getv}  // sets the x-data using Vec 
    def setY(y: scalaSci.Vec) = { yData = y.getv} // sets the y-data using Vec
    def setZ(z: scalaSci.Vec) = { zData = z.getv} // sets the z-data using Vec
    
    def setHoldOn() = { scalaSci.math.plot.plot.hold(true) } // set hold on @new
    def setHoldOff() = { scalaSci.math.plot.plot.hold(false) } // set hold off @new
    
    var xlabelStr = "X-axis"
    def setxlabel(xl: String) = xlabelStr = xl
    
    var ylabelStr = "Y-axis"
    def setylabel(yl: String) = ylabelStr = yl
    
    var zlabelStr = "Z-axis"
    def setzlabel(zl: String) = zlabelStr = zl
    
    var plotTitle2D = "2-D Plot"
    def setplotTitle2D(plTitle: String) = { plotTitle2D = plTitle }
    var plotTitle3D = "3-D Plot"
    def setplotTitle3D(plTitle: String) = { plotTitle3D = plTitle }

    
    
       
    // perform the plot using the object's properties
    def mkplot() = { 
        scalaSci.math.plot.plotTypes.plot(xData, yData, scalaSci.math.plot.PlotGlobals.defaultAbstractDrawerColor, plotTitle2D)
        scalaSci.math.plot.plot.xlabel(xlabelStr)
        scalaSci.math.plot.plot.ylabel(ylabelStr)
        
       }
 
 // perform the plot using the object's properties
    def mkplot3D() = { 
        
        scalaSci.math.plot.plotTypes.plot(xData, yData, zData, scalaSci.math.plot.PlotGlobals.defaultAbstractDrawerColor,  plotTitle3D)
        scalaSci.math.plot.plot.xlabel(xlabelStr)
        scalaSci.math.plot.plot.ylabel(ylabelStr)
        scalaSci.math.plot.plot.zlabel(zlabelStr)
       }
  
    
  
 }
