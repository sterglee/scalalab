# Introduction #

`Plotting routines tend to be complicated and can easily confuse the user. Therefore, we start to develop more convenient, object oriented plotting routines. These routines keep state information and permit the user to easily control the properties of the plots. A snapshot of such code is illustrated below. `

`Note: The code for the` _`PlotController`_ `object exists in ScalaLab211, therefore we can execute directly the application examples.`

# Design of a Plot Facilitator Object #
```


/* a class to plot in an object-oriented way
 we perform plotting in two steps:
   1. We specify the properties of our desired plot using the set*() routines
   2. We perform the plot using the simple mkPlot*() routines
*/
 
class PlotController() {

// these routines affect AbstractDrawer properties and can be used to change the defaults 
// for external plotting routines that do not specify their own values
       // set drawing to continuous lines
    def setContinuousLine() = { scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.CONTINOUS_LINE }
       // set drawing to dotted lines
    def setDottedLine() = { scalaSci.math.plot.render.AbstractDrawer.line_type = scalaSci.math.plot.render.AbstractDrawer.DOTTED_LINE }
       // set drawing to Round Dot
    def setRoundDot() = { scalaSci.math.plot.render.AbstractDrawer.dot_type = scalaSci.math.plot.render.AbstractDrawer.ROUND_DOT }
       // set drawing to cross dot
    def setCrossDot() = { scalaSci.math.plot.render.AbstractDrawer.dot_type = scalaSci.math.plot.render.AbstractDrawer.CROSS_DOT }
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
    

```

## Testing the Plot Controller Object ##

`We can now test the plot controller object as: `

```

    // Illustrates how to  construct a PlotController object to facilitate the plotting
    var po = new PlotController()
    
    // construct some signals
    var x = inc(0, 1, 100)
    var y1 = sin(0.45*x)
    var y2 = sin(0.778*x)+0.2*cos(3.4*x)
    var y3 = cos(y1+y2)
    
    // construct the first plot object
    closeAll()  // close any previous figure
    po.setX(x.getv)  
    po.setY(y1.getv)
    po.setColor(Color.GREEN)  // use GREEN color for plotting
    po.setplotTitle2D(" Demonstrating 2-D plots")
    po.setDottedLine()
    po.mkplot()   // plot the first signal
    
    po.setContinuousLine();   // set now to continous line plotting
    po.setColor(Color.RED)
    po.setlineWidth(15)   // set to thicker width
    // redefine the new signals for plotting
    hold(true); 
    po.setY(y2)   // we change only the Y signal

    figure; po.mkplot()
    
    var z = cos(4.5*x)
    po.setZ(z)
    
    po.setColor(Color.BLUE)  // change the plot color
    po.setlineWidth(1)  // set the line width to 1
    po.mkplot3D
 

```