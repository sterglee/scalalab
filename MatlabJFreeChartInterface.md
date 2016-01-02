# Matlab-like interface to JFreeChart #

`ScalaLab has an easy to use Matlab-like interface to the JFreeChart plotting library. The following code presents an example of using that interface. `


# Example #
```
 jfigure(1)
 var t = inc(0, 0.01, 10); var x = sin(0.23*t)
 var lineSpecs = "."
 jplot(t,x, lineSpecs)
jtitle("drawing multiple line styles")
jhold(true)  // hold axis
var SX = 5  // subsample factor
var tsub = t(1, SX, t.length-SX)   // subsampe
var xsub = x(1, SX, x.length-SX)
jplot(tsub, 0.4*xsub, lineSpecs)
lineSpecs = ":r+"
jplot(tsub, 0.1*xsub, lineSpecs)
// redefine the color of line 2
jlineColor(2, Color.BLUE)

jfigure(2)
jsubplot(222)
var x11 = sin(8.23*t)
jplot(t,x11)
jhold(true)
lineSpecs = ":g"
jplot(t,sin(5*x11), lineSpecs)
jsubplot(223)
lineSpecs = ":r"
jplot(t,x11, lineSpecs)


// create a new figure and preform a plot at subplot 3,2,1
 var nf = jfigure
 jsubplot(3,2,1)
 var t2 = inc(0, 0.01, 10); var x2 = sin(3.23*t2)+2*cos(0.23*t2)
 jplot(t2,x2, ".-")
jsubplot(3,2,3)
var x3 = cos(2.3*t2)+9*sin(4.5*t2)
jplot(t2, x3)
jlineColor(1, Color.RED)
jsubplot(3,2,5)
var x4 = cos(12.3*t2)+9*sin(2.5*t2)
jplot(t2, x4+x3)
jlineColor(1, Color.GREEN)
jsubplot(3,2,6)
jplot(t2, 6*x4+x3)
jtitle("6*x4+x3")
jlineColor(1, Color.BLUE)

// now plot again at figure 2
jfigure(2)  // concetrate on figure 2
jsubplot(2, 2, 1)
var vr = vrand(2000)
jplot(vr)
jtitle("A Random Vector")
var td = t.getv  // get time as Array[Double]
jsubplot(224)
jhold(true)
jplot(td, sin(1.34*td))
jplot(sin(3.6*td), td)
jplot( cos(7.8*td), 2.3*sin(3.2*td))
jtitle("Multiple Plots")



// demonstrate PieDataChart

var categories = Array("Class1", "Class2", "Class3")
var values = Array(5.7, 9.8, 3.9)
var pieChartName = "Test Pie Chart"
var myPie = jplot(pieChartName, categories, values)
```