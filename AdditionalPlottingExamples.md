## Plotting with mark characters ##

```
  setMarkChar('*')   // set the character used to plot the marks
        
  var t=inc(0, 0.1, 10)
  var x= sin(2.3*t)
  plotMarks(t,x)
  
```

## Specifying colors with a Matlab-like style ##
```
 var t = inc(0, 0.01, 10)
 var x = sin(0.56*t)
 var plotType = "b"  // blue as color
 plot(t, x, plotType)
 jplot(t, x, plotType)   // the same plot using the JFreeChart  library
```


```

var tv = inc(0, 0.02, 10); var xv = sin(6.7*tv);
var t = tv.getv; var x = xv.getv
 
figure(1); plot(t, x, "b", "Blue - width: 10 ",10)    // a blue curve with width 10

 figure(2); plot(t, x, ":", "Dotted")   // a dotted curve

 figure(3); plot(t, x, "--", "Dashed")
 figure(4); plot(t, x, "t5", "Thick-Line")   // a line with thickness 5

```


## Using directly the 2-D bar-plot graphs ##

```


var p2 = new Plot2DPanel
var np = 10  // number of points
 var XYZ1 = new Array[Array[Double]](np, 2)
 var XYZ2 = new Array[Array[Double]](np, 2)
   for (j <- 0 until np) {
      XYZ1(j)(0) = Math.random
      XYZ1(j)(1) = exp(XYZ1(j)(0))
      XYZ2(j)(0) = XYZ1(j)(0)
      XYZ2(j)(1) = log(XYZ2(j)(0))
    }

 p2.addBarPlot("Exponential  "+i,  Color.RED, XYZ1)
 p2.addBarPlot("Logarithmic  "+i,  Color.GREEN, XYZ2 )

p2.setLegendOrientation(PlotPanel.SOUTH)

new FrameView(p2)

```

## Using directly cloud plots ##

```

// Using directly cloud plots
var  p = new Plot2DPanel


var   N=8
var  cloud = new  Array[Array[Double]](1000, 2)
for ( i <- 0 until  cloud.length) {
   cloud(i)(0) = N * exp(Math.random + Math.random)
   cloud(i)(1) = N * exp(Math.random + Math.random)
    }
  p.addCloudPlot("cloud", Color.RED, cloud, N, N)

var  cloud2 = new Array[Array[Double]](1000, 2)
for (i <- 0 until cloud2.length) {
   cloud2(i)(0) = 2 + Math.random + Math.random
   cloud2(i)(1) = 2 + Math.random + Math.random
   }
   p.addCloudPlot("cloud2", Color.GREEN, cloud2, 2, 2)

new FrameView(p)

```