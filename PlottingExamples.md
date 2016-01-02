# Plotting Examples #

`We present some plotting examples. These plotting functions are based on a MATLAB-like wrapping of JMathPlot. `


# Examples #

```

// EXAMPLE 1

import _root_.javax.swing._ 

         var t = inc(0, 0.01, 20)
         var x = sin(0.2*t)
         figure(1); title("Demonstrating ploting multiple plots at the same figure")
        plot(t, x, Color.GREEN, "sin(0.2*t)")
// the xlabel(), ylabel() here refer to the axis of the current plot (i.e. PlotPanel object)
         xlabel("t-Time axis")
         ylabel("y=f(x) axis")
         var  y = sin(0.2*t)+5*cos(0.23*t)
         hold("on")
         plot(t,y, new Color(0, 0, 30), "sin(0.2*t)+5*cos(0.23*t)" )


          t = inc(0, 0.01, 20)
          y = sin(0.32*t)+5*cos(0.23*t)
          var z = sin(1.2*t)+0.5*cos(0.23*t)
          var fig = figure3d(2)
          plot(t,y, z,  Color.BLUE, "Ploting in 3-D")
// specify labels explicitly for the fig PlotPanel object
         fig.xlabel("t - Time axis ")
         fig.ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         fig.zlabel("sin(1.2*t)+0.5*cos(0.23*t);")
         title("A 3-D plot");

 
         var zDot = z cross z   // take the cross product
         fig = figure3d(3)  // the 3nd figure handle
         plot(t,y, zDot)
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("(sin(1.2*t)+0.5*cos(0.23*t)) .* (sin(1.2*t)+0.5*cos(0.23*t))")

         


// EXAMPLE 2


   
        var  t = inc(0, 0.01, 20)
        var  x = sin(0.2*t)
        var  y = sin(0.2*t)+5*cos(0.23*t)
        var  z = exp(-0.2*y)
        figure(1)
        hold("on")
        plot(t, x, Color.RED, "sin(0.2*t)")
        plot(t, y, Color.BLUE, "sin(0.2*t)+5*cos(0.23*t)")
        plot(t, z, Color.GREEN, "exp(-0.2*y)")
        xlabel("x-Time axis")
        ylabel("x-y-z plots")
        title("With hold on")

        figure(2)
        plot(t,x)
        hold("off")
        plot(t, y)
        xlabel("x-Time axis")
        ylabel("y-sin(0.2*t)")
        title("With hold off")

        figure(3);  
        plot(t, x, Color.RED)
        hold("on");
        plot(t, y, Color.BLUE)
        xlabel("x-Time axis")
        ylabel("y-sin(0.2*t)")
        title("With hold on")

        var fig = figure(4)
// specify labels explicitly for the fig PlotPanel object
        fig.xlabel("x - Time axis ")
        fig.ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
        x = t
   	y = sin(0.2*t)+5*cos(0.23*t)
	plot(x,y)

        z = sin(1.2*t)+0.5*cos(0.23*t)
        fig = figure3d(5)
        plot(t, y,  z, Color.GREEN)
// specify labels explicitly for the fig PlotPanel object
         fig.xlabel("x - Time axis  t");
         fig.ylabel("y = sin(0.2*t)+5*cos(0.23*t)")
         fig.zlabel("z = sin(1.2*t)+0.5*cos(0.23*t)")


         var  zCross = z cross z
         fig = figure3d(6)
         plot(t, y,  zCross)
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("(sin(1.2*t)+0.5*cos(0.23*t)) .* (sin(1.2*t)+0.5*cos(0.23*t))")

         var zCross2 =  zDot cross zCross
         fig = figure3d(7) 
         subplot3d(2,1,1) 
         plot(t, y, zCross)
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("(sin(1.2*t)+0.5*cos(0.23*t)) .* (sin(1.2*t)+0.5*cos(0.23*t))")

        subplot3d(2,1,2)
        plot(t, y, zCross)
         
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("zCross")



// EXAMPLE 3

 
// demonstrates 3-D surface plot

val N = 100
var x = linspace(-2.0, 2.0, N)
var y = linspace(-2.0, 2.0, N)

var z = Array.ofDim[Double](N, N)
for (n<-0 until N)
  for (m<-0 until N)
    z(n)(m) = cos( x(n)*y(m))*(x(n)*x(n)-y(m)*y(m))
    
  
surf( x, y, z, "Surface Plot")      
    
      


// EXAMPLE 4

   
        var  t = inc(0, 0.01, 20)
        var  x = sin(0.2*t)
        var  y = sin(0.2*t)+5*cos(0.23*t)
        var  z = exp(-0.2*y)
        figure(1)
        hold("on")
        plot(t, x, Color.RED, "sin(0.2*t)")
        plot(t, y, Color.BLUE, "sin(0.2*t)+5*cos(0.23*t)")
        plot(t, z, Color.GREEN, "exp(-0.2*y)")
        xlabel("x-Time axis")
        ylabel("x-y-z plots")
        title("With hold on")

        figure(2)
        plot(t,x)
        hold("off")
        plot(t, y)
        xlabel("x-Time axis")
        ylabel("y-sin(0.2*t)")
        title("With hold off")

        figure(3);  
        plot(t, x, Color.RED)
        hold("on");
        plot(t, y, Color.BLUE)
        xlabel("x-Time axis")
        ylabel("y-sin(0.2*t)")
        title("With hold on")

        var fig = figure(4)
// specify labels explicitly for the fig PlotPanel object
        fig.xlabel("x - Time axis ")
        fig.ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
        x = t
   	y = sin(0.2*t)+5*cos(0.23*t)
	plot(x,y)

        z = sin(1.2*t)+0.5*cos(0.23*t)
        fig = figure3d(5)
        plot(t, y,  z, Color.GREEN)
// specify labels explicitly for the fig PlotPanel object
         fig.xlabel("x - Time axis  t");
         fig.ylabel("y = sin(0.2*t)+5*cos(0.23*t)")
         fig.zlabel("z = sin(1.2*t)+0.5*cos(0.23*t)")


         var  zCross = z cross z
         fig = figure3d(6)
         plot(t, y,  zCross)
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("(sin(1.2*t)+0.5*cos(0.23*t)) .* (sin(1.2*t)+0.5*cos(0.23*t))")

         var zCross2 =  zCross cross zCross
         fig = figure3d(7) 
         subplot3d(2,1,1) 
         plot(t, y, zCross)
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("(sin(1.2*t)+0.5*cos(0.23*t)) .* (sin(1.2*t)+0.5*cos(0.23*t))")

        subplot3d(2,1,2)
        plot(t, y, zCross)
         
// specify labels explicitly for the fig PlotPanel object
         xlabel("x - Time axis ")
         ylabel("y - sin(0.2*t)+5*cos(0.23*t)")
         zlabel("zCross")



// EXAMPLE  5


// plots a sigmoid
var a = 1.7159
var b=2.0/3.0
var u = inc(-2, 0.001, 2)

var fu = a*tanh(b*u)
for (k<-0 to 20) {
 if (k<5)
   plot(u, fu, new Color(k*20, 0, 0));
 else {
 if (k<10)
   plot(u, fu, new Color(100, k*20, 0));
 else
   plot(u, fu, new Color(0, 10*k, k*10));
}

hold("on")
   b=1.4*b
   fu = a*tanh(b*u)
  }



// EXAMPLE 6


// display a plot with  'X' connected by a line 
var t= inc(0, 0.1, 10);
var x = sin(0.23*t)+0.2252*cos(3.4*t)

plotMarkedLine(t,x, Color.GREEN)



// EXAMPLE 7

		val t=inc(0, 1, 10)
        
        	var x= sin(2.3*t)

	   	var color = Color.BLUE 
	   	val  font = new java.awt.Font("Arial", java.awt.Font.BOLD, 12)
        	val skipPoints = 1
        	plotPoints("", color,  'x', font,  skipPoints,  t,x)
        	hold( "on" )
        	val t2=inc(0, 0.1, 10)
        
        
        	var x2= sin(2.3*t2)
        
        	plot(t2,x2)

		color = Color.GREEN
         	plotPoints("dense", color, '#', font, skipPoints, t2, x2)  
        
        

```