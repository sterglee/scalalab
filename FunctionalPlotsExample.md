# Functional Style Plotting #

Functional programming opens new opportunities for effective plotting.
The following example demonstrates that fact.


# Demonstrates functional style plotting #

```

// define and plot the corresponding 1-D function
 val color = Color.BLUE  // the plotting color 
 val lineplotsFlag = true  // i.e. connect plotted points with a line

  def  fx(x:Double) = { x -sin(x)*x }  // the function to plot
  fplot(fx,  1, 10, color, lineplotsFlag)  // plot the function
 
 def fy(x: Double) = { x-2*cos(x)*x*x }
 fplot(fy, 1, 10, Color.GREEN, false, 1000)


// define as String and plot the corresponding 1-D function
splot("sin(x)", 1, 5, color, lineplotsFlag)


// plot with JFreeChart
 jsplot("sin(x)", 1, 5)

// define and plot the corresponding 2-D function
def  fxy(x:Double, y: Double) = { x*sin(x)+x*y*cos(y) }
  figure3d; fplot2d(fxy,  1, 10, 1,5)
 
// define directly as a String and plot the corresponding 1-D function
 figure3d; splot2d("x*y*cos(x*y)", -4, 4, -10, 10)
```


# Using Partial Functions #
```

// define the Gaussian
def  gs(x: Double, m: Double, s: Double ) = (1/(2*java.lang.Math.PI*s*s))*(exp(-(x-m)*(x-m)/(2*s*s)))

var m1 = 10.0
var s1 = 1.0
var  gs10_1 = gs(_:Double, m1, s1)  // a Gaussian with m1=10.0 and s1=1.0 defined as partial function
var m2 = 100
var s2 = 6.0
var gs100_6 = gs(_: Double, m2, s2)   // a Gaussian with m1=100 and s1=6.0 defined as partial function

var Np = 2000
var t1 = linspace(m1-4*s1, m1+4*s1, Np)
var t2 = linspace(m2-4*s2, m2+4*s2, Np)

subplot(2,1,1); plot(t1, t1 map gs10_1, Color.RED)
hold("on")
subplot(2,1,2); plot(t2, t2 map gs100_6, Color.GREEN )

```

# Adaptive Functional Plotting #

`Generally, we can improve the plot of a function significantly by adjusting the sampling density according to the rate of function change. The ` _`faplot() ` `method is a first attempt towards adaptive functional plotting. We illustrate it by means of an example: `_

```
// Example: the function sin(x*x) changes generally more rapidly as x increases,
//  however, as can be seen from its derivative x*x*cos(x*x),
//  the rate of change oscilates also with increasing frequency as x increases 
 // Example: the function sin(x*x) changes generally more rapidly as x increases,
//  however, as can be seen from its derivative x*x*cos(x*x),
//  the rate of change oscilates also with increasing frequency as x increases 
 
def f(x: Double) = sin(x*x)

closeAll
var Npoints = 200
figure(1)
subplot(2, 1, 1)
fplot(f, 0, 10, nP = Npoints )
xlabel("Fixed sampled x")
subplot(2, 1, 2)
var (ax, ay) = faplot(f, 0, 10, nP = Npoints)
xlabel("Adaptively sampled x")

```