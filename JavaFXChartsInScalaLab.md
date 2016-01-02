# Introduction #

`JavaFX comes with the ChartFX component that performs nice looking charting. In ScalaLab we develop an easier interface t utilize ChartFX for plotting. We demonstrate examples. This examples run the Java 8 based version of ScalaLab. JavaFX functionality can be exploited within the Swing based user interface of ScalaLab, but that requires provisions, such as considering the threading issues of the two environments. `


## `Line Plots` ##

```

var x = 0::0.01::20  // the axis

var y = sin(0.56*x)

setlinePlotTitlefx("A sine function  plotted with JavaFX")

setlinePlotNamefx(" sin function")
lineplotfx(x, y)

```

## `Scatter Plots` ##

```

var x = 0::0.5::20

var y = sin(0.56*x)

setscatterPlotTitlefx("A sine function  plotted with JavaFX")

setscatterPlotNamefx(" sin function")
scatterplotfx(x, y)

```


## `Pie Charts ` ##

```


var languages = Array("Scala", "Java", "Groovy", "C/C++", "C#")

var speedIndex = Array(0.8, 0.82, 0.6, 0.95, 0.81)

setpieChartTitlefx("Speed indexes of some languages")

setpieChartNamefx("Speed of languages")


pieChartfx(languages, speedIndex)

```

## `Bar Chart ` ##

```

var languages = Array("Scala", "C/C++", "Java", "C#", "Groovy")   // the items

var benchmarksPerformed = Array("Array Access", "Loops", "Sieve")   // the attributes

var resultsOfBenches = Array(Array(0.8, 0.9, 0.6), Array(0.9, 0.95, 0.91), Array(0.85, 0.92, 0.7), Array(0.82, 0.9, 0.67), Array(0.4, 0.45, 0.47))


setbarChartLabelsOfItems( languages)

setbarChartAttributeNames ( benchmarksPerformed )

setbarChartAttributeValues( resultsOfBenches )


barChartfx( )

```
