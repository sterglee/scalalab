# Introduction #

`The ` _`jmathPlot`_ `based default plotting system of ScalaLab is fast, but however their routines cannot confront effectively large signals, and in that case a notable delay at the plotting operations is observed.`

`We modified the design in order for large signals, only a subset of their points is plotted. `

`Specifically, we keep a global variable, ` _`limitPlotPoints`_ `that limits the maximum number of points for ` _`line`_ `plots and` _`scatter`_  `plots. When a signal has more points that this, it is sub-sampled before plotting. Therefore the plotting operations are kept limited even for large signals, where the speedup is very significant, without notable quality loss. `

`The programming interface is simple:`

`A flag ` _`skipPointsOnPlot`_ `controls whether the approximate plotting of large signals is allowed (default = true). Also, a variable ` _`limitPlotPoints`_ `controls the limit on signal size, up to which the signal is large.`

`The corresponding ScalaSci code is listed below and it's operation is straightforward: `

```
 static public boolean fullPlotsOn() {
            boolean prevState = PlotGlobals.skipPointsOnPlot;
            PlotGlobals.skipPointsOnPlot = false;
            return prevState;
        }
        
        static public boolean fastPlotsOn() {
            boolean prevState = PlotGlobals.skipPointsOnPlot;
            PlotGlobals.skipPointsOnPlot = true;
            return prevState;
        }
        
        static public int setMaximumPlotPoints(int newMaxPointsToPlot) {
            int prevMaxPointsToPlot = PlotGlobals.limitPlotPoints;
            PlotGlobals.limitPlotPoints = newMaxPointsToPlot;
            return prevMaxPointsToPlot;
        }

        // controls for 3-D plots the max points at the X-dimension
        static public int setMaximumPlotXPoints(int newMaxPointsXToPlot) {
            int prevMaxXPointsToPlot = PlotGlobals.limitPlotPointsX;
            PlotGlobals.limitPlotPointsX = newMaxPointsXToPlot;
            return prevMaxXPointsToPlot;
        }

        // controls for 3-D plots the max points at the Y-dimension
        static public int setMaximumPlotYPoints(int newMaxPointsYToPlot) {
            int prevMaxYPointsToPlot = PlotGlobals.limitPlotPointsY;
            PlotGlobals.limitPlotPointsY = newMaxPointsYToPlot;
            return prevMaxYPointsToPlot;
        
```

`We present also an example where we ask for fast plots and with an explicitly specified signal size limit: `

```

close all

var N = 1000000
var t = linspace(0, 5, N)
var x =  sin(0.78*t)+3.4*cos(4.5*t)

fastPlotsOn()   // enable fast plotting
setMaximumPlotPoints(2000)   // any signal larger than this is sub-sampled in plotting

plot(t,x)
```

`And another example`

```

close all

var t = 0::0.01::2000  // a large time axis
var x = sin(0.0056*t)

fastPlotsOn()  // enable fast plotting
setMaximumPlotPoints(3000)  // any signal larger than this is subsampled in plotting

plot(t, x, Color.GREEN)
```