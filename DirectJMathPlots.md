# Introduction #

`The JMathPlot library can be used directly from Scalalab. Here we present some examples adapted in Scala from jMathPlot of Yann Richet`


# Line Plot Example #
```


import java.awt._
import scalaSci.math.plot._
import scalaSci.math.plot.render._


var x = Array(1,2,3,4,5,6.0)
var y = Array(45.0, 89, 6, 32, 63, 12)

var plot = new Plot2DPanel

plot.addLegend("SOUTH")

plot.addLinePlot("my plot", x, y)

var frame = new JFrame("a plot panel")
frame.setSize(600, 600)
frame.setContentPane(plot)
frame.setVisible(true)

```


# Grid Plot Example #

```
import java.lang.Math.PI 
import java.awt._
import scalaSci.math.plot._
import scalaSci.math.plot.render._

def  fxy1(x: Double, y: Double) =  cos(x*PI)*sin(y*PI)
  
def f1( x: Array[Double], y: Array[Double]) =  {
    var z  = Array.ofDim[Double](y.length, x.length)
    for (i<-0 until x.length)
      for (j<-0 until y.length)
       z(j)(i) = fxy1(x(i), y(j))
    z
    }     
    
 def fxy2(x: Double, y: Double) = sin(x*PI)*cos(y*PI)
 
 def f2( x: Array[Double], y: Array[Double]) = {
     var z =  Array.ofDim[Double](y.length, x.length)
     for (i<- 0 until x.length)
       for (j<-0 until y.length)
           z(j)(i) = fxy2(x(i), y(j))
   z
   }
   
   // define your data
   var  x = Inc(0.0, 0.1, 1.0)
   var y = Inc(0.0, 0.05, 1.0)
   var z1 = f1(x, y)
   var z2 = f2(x, y)
   
   // create your PlotPanel
   var plot = new Plot3DPanel
   
   // add grid plot to the PlotPanel
plot.addGridPlot("z = cos(PI*x)*sin(PI*y)", x, y, z1)
plot.addGridPlot("z = sin(PI*x)*cos(PI*y)", x, y, z2)

   var frame = new JFrame("a plot panel")
   frame.setSize(800, 600)   
   frame.setContentPane(plot)
   frame.setVisible(true)


```

# Histogram #
```

import scalaSci.math.array.StatisticSample._
import java.lang.Math.PI 
import java.awt._
import scalaSci.math.plot._
import scalaSci.math.plot.render._

// define your data
var x = randomLogNormal(1000, 0, 0.5)   // 1000 random numbers from  a log normal statistical law

// create your PlotPanel (you can use it as a JPanel)
var plot = new Plot2DPanel()

// add the histogram (50 slices)   of  x  to the PlotPanel
plot.addHistogramPlot("Log Normal population", x, 50)

var frame = new JFrame("a plot panel")
frame.setSize(600, 600)
frame.setContentPane(plot)
frame.setVisible(true)

```



# Customize your plot #
```
import java.lang.Math.PI 
import java.awt._
import scalaSci.math.plot._
import scalaSci.math.plot.render._
import  scalaSci.math.array.StatisticSample._
import  scalaSci.math.plot.plotObjects._


// 1000 random numbers from a normal (Gaussian)  statistical law
var  x = randomNormal(1000, 0, 1)
// 1000 random numbers from a uniform statistical law
var y = randomUniform(1000, -3, 3)

// create your PlotPanel (you can use it as a JPanel)
var plot = new Plot2DPanel

// legend at SOUTH
plot.addLegend("SOUTH")

// add the histogram (50 slices) of x to the PlotPanel
plot.addHistogramPlot("Gaussian population", x, 50)

// add the histogram (50 slices) of y to  the  PlotPanel in GREEN
plot.addHistogramPlot("Uniform population", Color.RED, y, 50)

// add a title
var title = new BaseLabel("... My nice plot ... ", Color.RED, 0.5, 1.1)
title.setFont(new Font("Courier", Font.BOLD, 20))
plot.addPlotable(title)

// change name of axis
plot.setAxisLabels("<X>", "frequency")

// customize X axis
// rotate light labels
plot.getAxis(0).setLightLabelAngle(-PI/4)
// change axis title  position  relatively to  the  base of the plot
plot.getAxis(0).setLabelPosition(0.5, -0.15)

// customize Y axis
// rotate light labels
plot.getAxis(0).setLightLabelAngle(-PI/4)
// change axis title position relatively to the base of the plot
plot.getAxis(0).setLabelPosition(-0.15, 0.5)
  // change axe title  angle
plot.getAxis(1).setLabelAngle(-PI/2)

// put the PlotPanel in a JFrame like a JPanel
var frame = new JFrame("a plot panel")
frame.setSize(600, 600)
frame.setContentPane(plot)
frame.setVisible(true)
  
```




