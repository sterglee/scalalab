package scalaSci.math.plot;

import JSci.maths.wavelet.Signal;
import java.awt.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import scalaExec.Interpreter.GlobalValues;
import scalaSci.*;
import scalaSci.Mat;
import scalaSci.Vec;

import static scalaSci.math.plot.ComplexArrayPlots.*;

//  In order to implement the grid like structure of Matlab subplots  a constructor
// is defined that builds these objects from an array of graphic panel objects of the same type.
// The FrameView class represents plotting frame objects similar to Matlab figures.
// They can contain a rectangular grid of subplot objects.


import static scalaSci.math.plot.plot.*;
import scalaSci.math.plot.render.AbstractDrawer;
import static scalaSci.math.plot.utils.PArray.*;

// supports all the plot routines 
public class plotTypes {
    
      static  int currentColorIndex = 0;
      static int maxColors = 20;
      static Color [] colorTable  = { Color.black, Color.blue, Color.green, Color.red, Color.magenta,
              Color.yellow, Color.darkGray};
    
      // extract the range of values x[low..high] and return them with a new array
        static double [] getRange(double [] x, int low, int high) {
            int N = high-low+1;
            double [] nv = new double[N];
            int idx = low;
            for (int k=0; k<N; k++)
                nv[k] = x[idx++];
            return nv;
        }
    
        
        static public scalaSci.math.plot.PlotPanel  plotComplex(org.apache.commons.math3.complex.Complex [] x) {
            return  ComplexArrayPlots.plotApacheCommonsComplexArray(x,  "Real Values", "l", Color.GREEN, 1, "Plotting of Real Parts");
            
        }
     

        
 // static public void fxplot(double [] values) {
     // javaFXPlotLine.plot(values);
// }
 
        // some routines that define plot range     

        // a 3-D plot of  x[low..high] vs y[low..high] vs z[low..high]
        static public PlotPanel plot(double [] x, double [] y, double [] z, int low, int high, Color color, String name) {
         double [] nx = getRange(x, low, high);
         double [] ny = getRange(y, low, high);
         double [] nz = getRange(z, low, high);
         return plot(nx, ny, nz, color, name);
       }
      
    static public PlotPanel plot(double [] x, double [] y, double [] z, int low, int high,  String name) {
         double [] nx = getRange(x, low, high);
         double [] ny = getRange(y, low, high);
         double [] nz = getRange(z, low, high);
         return plot(nx, ny, nz,  name);
       }
       
    static public PlotPanel plot(double [] x, double [] y, double [] z, int low, int high) {
         double [] nx = getRange(x, low, high);
         double [] ny = getRange(y, low, high);
         double [] nz = getRange(z, low, high);
         return plot(nx, ny, nz);
       }
       
        
    static public PlotPanel plot(double [] x, double [] y,  int low, int high,  String name) {
         double [] nx = getRange(x, low, high); 
         double [] ny = getRange(y, low, high);
         return plot(nx, ny,  PlotGlobals.defaultAbstractDrawerColor, name);
       }
       
        static public PlotPanel plot(double [][] xa,  int low, int high,  Color color, String name) {
            double [] x = xa[0];
            double [] y = xa[1];
            return plot(x, y, low, high, name);
        }
 
       static public PlotPanel plot(double [] x, double [] y,  int low, int high,  Color color, String name) {
         double [] nx = getRange(x, low, high); 
         double [] ny = getRange(y, low, high);
         return plot(nx, ny, color,  name);
       }
       
       static public PlotPanel plot(double [] x, double [] y,  int low, int high) {
         double [] nx = getRange(x, low, high); 
         double [] ny = getRange(y, low, high);
         return plot(nx, ny);
       }
       
       
       static public PlotPanel plot(double [][]xa, int low, int high, Color color) {
           double [] x = xa[0];
           double [] y = xa[1];
           return plot( x, y, low, high, color);
       }
       
  static public PlotPanel plot(double [] x, double [] y,  int low, int high, Color color) {
         double [] nx = getRange(x, low, high); 
         double [] ny = getRange(y, low, high);
         return plot(nx, ny, color);
       }

       
        
        static public PlotPanel plot3d_line(Object  x,  Object y,  Object z, String name) {
            double [][] xyz = new double [3][];
            xyz[0] = (double []) x;
            xyz[1] = (double []) y;
            xyz[2] = (double []) z;
            if (new_figure == true)  newPlot3D();
            if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, xyz);
            else
                ((Plot3DPanel)currentPlot).addLinePlot(name, xyz);
	    return currentPlot;
        }



        // smoother plot using spline interpolation
        static public PlotPanel splinePlot( double [] xvals, double [] yvals, int NP, Color color, String name)
        {
            
            DhbScientificCurves.Curve  curve = new DhbScientificCurves.Curve();
            int len = xvals.length;
            for (int k=0; k<len; k++) 
                curve.addPoint(xvals[k], yvals[k]);
                    
            DhbInterpolation.SplineInterpolator  splines = new DhbInterpolation.SplineInterpolator(curve);
            int splineLen = len*NP;
            double [] xSpline = new double[splineLen];
            double [] ySpline = new double[splineLen];
            double dx = (xvals[1]-xvals[0])/NP;
            double x0 = xvals[0];
            for (int k=0; k<splineLen; k++) {
                xSpline[k] = x0+k*dx;
                ySpline[k] = splines.value(xSpline[k]);
            }
            ((Plot2DPanel) currentPlot).addLinePlot(name, color, xSpline, ySpline);
            
            
            return currentPlot;
        }
        
        static public PlotPanel splinePlot( double [] xvals, double [] yvals, int NP)
        {
            String name = "Spline Plot";
            Color color = Color.ORANGE;
            return splinePlot(xvals, yvals, NP, color, name);
        }
        
        static public PlotPanel splinePlot( double [] xvals, double [] yvals, int NP, String name)
        {
            Color color = Color.DARK_GRAY;
            return splinePlot(xvals, yvals, NP, color, name);
        }
        
        static public PlotPanel splinePlot( double [] xvals, double [] yvals, int NP, Color color)
        {
            String name = "Spline Plot";
            return splinePlot(xvals, yvals, NP, color, name);
        }
        
        
       static public PlotPanel plot(double [] x, double [] y, double [] z, Color color, String name) {
             newPlot3D();
              if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, color, x, y, z);
            else
                ((Plot3DPanel)currentPlot).addLinePlot(name, color, x, y, z);

            return currentPlot;
	}
       
       
       
       static public PlotPanel  plotMarkedLine(String name, Color color, double [] x, double [] y) {
           newPlot2D();
           ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, x, y);
           return currentPlot;
       }

       static public PlotPanel  plotMarkedLine(double [] x, double [] y) {
           newPlot2D();
           ((Plot2DPanel)currentPlot).addMarkedLinePlot("Marked Line", x, y);
           return currentPlot;
       }

       static public PlotPanel  plotMarkedLine(Vec x, Vec y) {
           newPlot2D();
           ((Plot2DPanel)currentPlot).addMarkedLinePlot("Marked Line", x.getv(), y.getv());
           return currentPlot;
       }

       static public PlotPanel  plotMarkedLine(Vec x, Vec y, Color c) {
           newPlot2D();
           ((Plot2DPanel)currentPlot).addMarkedLinePlot("Marked Line", c, x.getv(), y.getv());
           return currentPlot;
       }

       static public PlotPanel  plotPoints(String name, Color color, double []x, double []y) {
           newPlot2D();
           PlotGlobals.defaultMarksColor = color;
           ((Plot2DPanel)currentPlot).addMarkedPlot(name, color,  x, y);
           return currentPlot;
       }

       static public PlotPanel  plotPoints(String name, Color color, char mark, Font font, int skipPoints,  double []x, double []y) {
           newPlot2D();
           PlotGlobals.defaultSkipPoints = skipPoints;
           PlotGlobals.defaultMarksColor = color;
           PlotGlobals.defaultMarkChar = mark;
           ((Plot2DPanel)currentPlot).addMarkedPlot(name, x, y);
           return currentPlot;
       }

       static public PlotPanel  plotPoints(String name, Color color, Vec x, Vec y) {
           newPlot2D();
           PlotGlobals.defaultMarksColor = color;
           ((Plot2DPanel)currentPlot).addMarkedPlot(name, color,  x.getv(), y.getv());
           return currentPlot;
       }

       static public PlotPanel  plotPoints(double []x, double []y) {
           newPlot2D();
           ((Plot2DPanel)currentPlot).addMarkedPlot("Marked Line", Color.BLACK,  x, y);
           return currentPlot;
       }

       static public PlotPanel plot(float  [] x, float [] y, float[] z, Color color, String name) {
         return plot(arrToDouble(x), arrToDouble(y), arrToDouble(z), color, name);
       }

       
	//////////////
	// 3D plots //
	//////////////

        
 static public PlotPanel plot(double [] x, double [] y, double [] z, String name) {
            newPlot3D();
            if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, x, y, z);
            else
            ((Plot3DPanel)currentPlot).addLinePlot(name, x, y, z);

            return currentPlot;
}

 
 // convert float arrays to double arrays, then plot
 static public PlotPanel plot(float  [] x, float [] y, float[] z, String name) {
         return plot(arrToDouble(x), arrToDouble(y), arrToDouble(z), name);
       }


 static public PlotPanel plot(double [] x, double [] y, double [] z) {
        String name = "3-D Plot";
        newPlot3D();
        if (scatterPlotOn)
            ((Plot3DPanel)currentPlot).addScatterPlot(name, x, y, z);
        else
            ((Plot3DPanel)currentPlot).addLinePlot(name, x, y, z);

            return currentPlot;
}

 
 // convert float arrays to double arrays then plot
static public PlotPanel plot(float  [] x, float [] y, float[] z) {
         return plot(arrToDouble(x), arrToDouble(y), arrToDouble(z));
       }

   static public PlotPanel plot(double [][] x) {
       int N = x.length;
       int M = x[0].length;
      
        String name = "2-D Plot";
        newPlot2D();

        // the two-dimensional matrix specifies a column vector
       if (M ==1) {
           double [] y1 = new double[N];
           double [] y2 = new double[N];
           for (int c=0; c<N; c++) {
               y1[c] = c;
               y2[c] = x[c][0];
           }
       if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, y1, y2);
         else
                ((Plot2DPanel)currentPlot).addLinePlot(name, y1,  y2);
       }
       else if (N==1) {
         // the two-dimensional matrix specifies a row vector 
           double [] y1 = new double[M];
           double [] y2 = new double[M];
           for (int c=0; c<M; c++) {
               y1[c] = c;
               y2[c] = x[0][c];
           }
       if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, y1, y2);
         else
                ((Plot2DPanel)currentPlot).addLinePlot(name,  y1,  y2);
           
       }
       
       else {
    //  a two-dimensional matrix has been specified, plot its first two rows   
      	if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, x[0], x[1]);
            else
               ((Plot2DPanel)currentPlot).addLinePlot(name,  x[0], x[1]);
       }
 
       
  return currentPlot;
}

  static public PlotPanel plot(double [][] x, Color color, String name) {
       int N = x.length;
       int M = x[0].length;
      
        newPlot2D();

     if (M ==1) {
           double [] y1 = new double[N];
           double [] y2 = new double[N];
           for (int c=0; c<N; c++) {
               y1[c] = c;
               y2[c] = x[c][0];
           }
       if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, color, y1, y2);
         else
                ((Plot2DPanel)currentPlot).addLinePlot(name, color, y1,  y2);
       }
       else if (N==1) {
           double [] y1 = new double[M];
           double [] y2 = new double[M];
           for (int c=0; c<M; c++) {
               y1[c] = c;
               y2[c] = x[0][c];
           }
        if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, color, y1, y2);
         else
                ((Plot2DPanel)currentPlot).addLinePlot(name, color, y1,  y2);
       
       }
       
       else {
       
      	if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, color,  x[0], x[1]);
            else
               ((Plot2DPanel)currentPlot).addLinePlot(name, color, x[0], x[1]);
       }
 

return currentPlot;
}


  static public PlotPanel plot(double [][] x, Color color) {
      String name = "2D Plot";  // default name
   
      return plot(x, color, name);
}
  
   static public PlotPanel plot(double [][] x, String name) {
       Color color = Color.BLACK;  // default color
       
       return plot(x, color, name);
   }
  
  
       static public PlotPanel plot(float  [] []x) {
         return plot(arrToDoubleDouble(x));
       }

        static public PlotPanel plot(double [] x, double [] y, double [][] z) {
                String name = "3-D Plot";
                newPlot3D();
                ((Plot3DPanel)currentPlot).addGridPlot(name, x, y, z);
		return currentPlot;
	}

       static public PlotPanel plot(float  [] x, float [] y, float[][] z) {
         return plot(arrToDouble(x), arrToDouble(y), arrToDoubleDouble(z));
       }

       
/*
        setMarkChar('*')   // set the character used to plot the marks
        
        var t=inc(0, 0.1, 10)
        var x= sin(2.3*t)
        plotMarks(t,x)
        
        */
       
       // sets the char used for mark plots
       static public void setMarkChar(char ch) {
           PlotGlobals.defaultMarkChar = ch;
       }
       
       static public void  plotMarks(Vec t,  double [] x) {
            plotMarks(t.getv(), x);
       }
       
       static public void  plotMarks(double []  t,  Vec  x) {
            plotMarks(t, x.getv());
       }
       
       static public void  plotMarks(Vec t,  Vec x) {
               String name = "2-D Mark plot";
                newPlot2D();
                 int  len = t.size();
         plot(t, x, Color.WHITE);   
         for (int k=0; k<len; k++)
             markX(t.apply(k), x.apply(k));

       }

       static public void  plotMarks(double []  t,  double [] x) {
               String name = "2-D Mark plot";
               newPlot2D();
	 int  len = t.length;
         plot(t, x, Color.WHITE);
         for (int k=0; k<len; k++)
             markX(t[k], x[k]);

       }

        static public void  plotMarks(Vec t,  Vec x, Color color, String name) {
         newPlot2D();
         int  len = t.size();
          plot(t, x, color, name);   
          for (int k=0; k<len; k++)
             markX(t.apply(k), x.apply(k));

       }

       static public void  plotMarks(double []  t,  double [] x, Color color, String name) {
         newPlot2D();
         int  len = t.length;
         plot(t, x, color, name);
         for (int k=0; k<len; k++)
             markX(t[k], x[k]);

       }

        // plot a JSci signal
        static public PlotPanel plot(Signal sig) {
            return plot(sig.getValues());
        }

        static public PlotPanel plot(Object  x,  Object y,  Object z, String name) {
            double [][] xyz = new double [3][];
            xyz[0] = (double []) x;
            xyz[1] = (double []) y;
            xyz[2] = (double []) z;
            if (new_figure == true)  newPlot3D();

            if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, xyz);
            else
                ((Plot3DPanel)currentPlot).addLinePlot(name, xyz);

        return currentPlot;
        }


 static public PlotPanel plot3d_bar(double [] x, double [] y, double [] z, String name) {
        newPlot3D();
        ((Plot3DPanel)currentPlot).addBarPlot(name, x, y, z);
        return currentPlot;
	}
 
 static public PlotPanel plot3d_bar(double [] x, double [] y, double [] z) {
     String name = "3D bar plot"; // default name
     return plot3d_bar(x, y, z, name);
 }
 
 
static public PlotPanel plot3d_bar(float  [] x, float [] y, float[] z, String name) {
         return plot3d_bar(arrToDouble(x), arrToDouble(y), arrToDouble(z), name);
       }


 static public PlotPanel plot3d_bar(float []x, float [] y, float [] z) {
   String name = "3D bar plot"; // default name    
   return plot3d_bar(x, y, z, name);
 }
 
static public PlotPanel surf(double [] x, double [] y, double [][]z,  String name) {
    if ( currentPlot == null || (currentPlot instanceof  Plot3DPanel)==false)
            figure3d();
    ((Plot3DPanel)currentPlot).addGridPlot(name,x, y, z);
      return currentPlot;
    }

static public PlotPanel surf(double[] x, double [] y, double [][] z) {
    String name = "Surface Plot";  // default name
    return surf(x, y, z, name);
}

  static public PlotPanel surf(double [] x, double [] y, double [][]z, Color color,  boolean drawLines, boolean fillShape, String name) {
     if (currentPlot == null ||  (currentPlot instanceof  Plot3DPanel)==false)
             figure3d();
     ((Plot3DPanel)currentPlot).addGridPlot(name, color, drawLines, fillShape, x, y, z);
      return currentPlot;
    }

        
        static public PlotPanel surf(double []  x, Vec  y, double [][]z,  String name) {
               if (currentPlot == null || (currentPlot instanceof  Plot3DPanel)==false)
                   figure3d();
                ((Plot3DPanel)currentPlot).addGridPlot(name,x, y.getv(), z);
		return currentPlot;
	}
        
        
        static public PlotPanel surf(Vec x, Vec  y, double [][]z,  String name) {
               if (currentPlot == null ||  (currentPlot instanceof  Plot3DPanel)==false)
                   figure3d();
                ((Plot3DPanel)currentPlot).addGridPlot(name,x.getv(), y.getv(), z);
		return currentPlot;
	}
        

     static public PlotPanel surf(float  [] x, float [] y, float[] []z, String name) {
         return surf(arrToDouble(x), arrToDouble(y), arrToDoubleDouble(z), name);
       }

  static public PlotPanel surf(double [] x, double [] y, double [][]z,  Color color, String name) {
               if (currentPlot == null)
                   figure3d();
               ((Plot3DPanel)currentPlot).addGridPlot(name, color, x, y, z);
		return currentPlot;
	}


     static public PlotPanel surf(float  [] x, float [] y, float[] []z, Color color, String name) {
         return surf(arrToDouble(x), arrToDouble(y), arrToDoubleDouble(z), color,  name);
       }
// specify Color also
      
        static public PlotPanel surf(Vec  x, double [] y, double [][]z,   Color color, String name) {
               if (currentPlot == null)
                   figure3d();
                ((Plot3DPanel)currentPlot).addGridPlot(name, color, x.getv(), y, z);
		return currentPlot;
	}
        
        
        static public PlotPanel surf(double []  x, Vec  y, double [][]z, Color color, String name) {
               if (currentPlot == null)
                   figure3d();
                ((Plot3DPanel)currentPlot).addGridPlot(name, color, x, y.getv(), z);
		return currentPlot;
	}
        
        
        static public PlotPanel surf(Vec x, Vec  y, double [][]z,  Color color, String name) {
               if (currentPlot == null)
                   figure3d();
                ((Plot3DPanel)currentPlot).addGridPlot(name, color, x.getv(), y.getv(), z);
		return currentPlot;
	}
        
        
  static public PlotPanel surf(float  [] x, float [] y, float[] []z) {
         return surf(arrToDouble(x), arrToDouble(y), arrToDoubleDouble(z));
       }

static public PlotPanel surf(double [] x, double [] y, double [][]z, Color color) {
                String name = "Surface Plot";
                ((Plot3DPanel)currentPlot).addGridPlot(name, color, x, y, z);
                return currentPlot;
	}


   static public PlotPanel surf(float  [] x, float [] y, float[] []z, Color color) {
         return surf(arrToDouble(x), arrToDouble(y), arrToDoubleDouble(z), color);
       }
	//////////////
	// 2D plots //
	//////////////

static public PlotPanel plot2d_scatter(Object  x,  Object y, String name) {
            double [][] xy = new double [2][];
            xy[0] = (double []) x;
            xy[1] = (double []) y;
            if (new_figure == true)  newPlot2D();
            ((Plot2DPanel)currentPlot).addScatterPlot(name,xy);
            return currentPlot;
        }


static public PlotPanel plot2d_scatter(double []  x,  double [] y, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] = y;
            if (new_figure == true)  newPlot2D();
            ((Plot2DPanel)currentPlot).addScatterPlot(name, xy);
            return currentPlot;
        }

static public PlotPanel plot2d_scatter(double [] x, double [] y) {
    String name = "2-D Scatter Plot";  // default name
    return plot2d_scatter(x, y, name);
}

static public PlotPanel plot2d_scatter(float  [] x, float [] y, String name) {
         return plot2d_scatter(arrToDouble(x), arrToDouble(y), name);
       }

static public PlotPanel plot2d_line(Object  x,  Object y, String name) {
            double [][] xy = new double [2][];
            xy[0] = (double []) x;
            xy[1] = (double []) y;
            if (new_figure == true)  newPlot2D();
            ((Plot2DPanel)currentPlot).addLinePlot(name,xy);

            return currentPlot;
   }


        static public PlotPanel plot2d_line(double []  x,  double [] y, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] = y;
            if (new_figure == true)  newPlot2D();
            ((Plot2DPanel)currentPlot).addLinePlot(name,xy);

            return currentPlot;
        }

        
static public PlotPanel plot(Vec x, Vec y, char type, String name) {
    return      plot(x.getv(), y.getv(), type, name);
}

static public PlotPanel plot(double []  x, Vec y, char type, String name) {
    return      plot(x, y.getv(), type, name);
}


static public PlotPanel plot(Vec  x, double [] y, char type) {
    return      plot(x.getv(), y, type, "2-D Plot");
}


          /*
         PLOT   Linear plot.
    PLOT(X,Y) plots vector Y versus vector X. If X or Y is a matrix,
    then the vector is plotted versus the rows or columns of the matrix,
    whichever line up.  If X is a scalar and Y is a vector, length(Y)
    disconnected points are plotted.

    PLOT(Y) plots the columns of Y versus their index.
    If Y is complex, PLOT(Y) is equivalent to PLOT(real(Y),imag(Y)).
    In all other uses of PLOT, the imaginary part is ignored.

    Various line types, plot symbols and colors may be obtained with
    PLOT(X,Y,S) where S is a character string made from one element
    from any or all the following 3 columns:

           b     blue          .     point              -     solid
           g     green         o     circle             :     dotted
           r     red           x     x-mark             -.    dashdot
           c     cyan          +     plus               --    dashed
           m     magenta       *     star             (none)  no line
           y     yellow        s     square
           k     black         d     diamond
                               v     triangle (down)
                               ^     triangle (up)
                               <     triangle (left)
                               >     triangle (right)
                               p     pentagram
                               h     hexagram

    For example, PLOT(X,Y,'c+:') plots a cyan dotted line with a plus
    at each data point; PLOT(X,Y,'bd') plots blue diamond at each data
    point but does not draw any line. */

          static public PlotPanel plot(Vec  x, Vec y, char plotType) {

              return  plot(x.getv(), y.getv(), plotType, "2-D plot");

	}
          
          static public PlotPanel  plot(Vec x,  Vec y)  {
              return plot(x.getv(), y.getv());
          }
          
          static public PlotPanel  plot(Vec x,  double[] y)  {
              return plot(x.getv(), y);
          }
          
          static public PlotPanel  plot(double []  x,  Vec y)  {
              return plot(x, y.getv());
          }
          
          
          
/*
 var t = inc(0, 0.01, 10)
 var x = sin(0.56*t)
 var plotType = "b"  // blue as color
 plot(t, x, plotType)
 jplot(t, x, plotType)   // the same plot using the JFreeChart  library
           
 */ 

          /*
 
 var tv = inc(0, 0.02, 10); var xv = sin(6.7*tv);
 var t = tv.getv; var x = xv.getv
 
figure(1); plot(t, x, "b", "Blue - width: 10 ",10)    // a blue curve with width 10

 figure(1); plot(t, x, ":", "Dotted")   // a dotted curve

 figure(2); plot(t, x, "--", "Dashed")
 figure(3); plot(t, x, "t5", "Thick-Line")   // a line with thickness 5
 */
static public PlotPanel plot(double [] x, double [] y, String  typeStr, String name) {
    return  plot(x, y, typeStr, name, 1);
}

static public PlotPanel plot(Vec x, double [] y, String  typeStr, String name) {
    return  plot(x.getv(), y, typeStr, name, 1);
}

static public PlotPanel plot(double [] x, Vec y, String  typeStr, String name) {
    return  plot(x, y.getv(), typeStr, name, 1);
}

static public PlotPanel plot(double [] x, double [] y, String  typeStr, int lineWidth) {
    String name = "2-D Plot";
    return plot(x, y, typeStr, name, lineWidth);
}

static public PlotPanel plot(double [] x, double [] y, String  typeStr, String name, int lineWidth) {
             double [][] xy = new double [2][];
            xy[0] = x;     xy[1] = y;
            if (new_figure == true)  newPlot2D();
            char type='k';
            if (typeStr.length()==1)
                type=typeStr.charAt(0);
            if (typeStr.equals("--"))  // dashed
                type='-';
            else if (typeStr.equals("-."))   // dash-dot
                type = '#';
            else if (typeStr.charAt(0)=='t') {  // control line thickness
                String thickness = typeStr.substring(1, typeStr.length());
                float fthick = Float.valueOf(thickness);
                AbstractDrawer.line_type = AbstractDrawer.THICK_LINE;
                AbstractDrawer.lineThickness = fthick;
                type='t';
            }

 switch (type) {
     case 'b':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.BLUE, xy, lineWidth); break;
     case 'g':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.GREEN, xy, lineWidth); break;
     case 'r':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.RED, xy, lineWidth); break;
     case 'c':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.CYAN, xy, lineWidth); break;
     case 'm':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.MAGENTA, xy, lineWidth); break;
     case 'y':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.YELLOW, xy, lineWidth); break;
     case 'k':  ((Plot2DPanel)currentPlot).addLinePlot(name, Color.BLACK, xy, lineWidth); break;
     case '.':  ((Plot2DPanel)currentPlot).addLinePlot(name, xy, true); break;  // add a dotted plot
     case 'x':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, Color.BLACK, new Font("Arial", Font.BOLD, 12), 'x', xy[0], xy[1]);    break;
     case '*':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, Color.BLACK, new Font("Arial", Font.BOLD, 12), '*', xy[0], xy[1]);    break;
     case 'o':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, Color.BLACK, new Font("Arial", Font.BOLD, 12), 'o', xy[0], xy[1]);    break;
     case 's':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, Color.BLACK, new Font("Arial", Font.BOLD, 12), '*', xy[0], xy[1]);    break;
     case 'd':   ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, Color.BLACK, new Font("Arial", Font.BOLD, 12),  '\u0101', xy[0], xy[1]);    break;
     case ':':   	((Plot2DPanel)currentPlot).addLinePlot(name, Color.GREEN, AbstractDrawer.DOTTED_LINE, lineWidth, xy);  break;
     case 't':    	((Plot2DPanel)currentPlot).addLinePlot(name, Color.GREEN, AbstractDrawer.THICK_LINE, (int)AbstractDrawer.lineThickness, xy);  break;
     case '-':    	((Plot2DPanel)currentPlot).addLinePlot(name, Color.GREEN, AbstractDrawer.PATTERN_LINE, lineWidth, xy);  break;


     default:  break;
 }
 return currentPlot;
}

static public PlotPanel plot(Vec  x, Vec  y, Color color, String  typeStr, String name, int lineWidth) {
  return   plot(x.getv(), y.getv(), color, typeStr, name, lineWidth);
}

static public PlotPanel plot(double []  x, Vec  y, Color color, String  typeStr, String name, int lineWidth) {
  return   plot(x, y.getv(), color, typeStr, name, lineWidth);
}

static public PlotPanel plot(Vec  x, double []  y, Color color, String  typeStr, String name, int lineWidth) {
  return   plot(x.getv(), y, color, typeStr, name, lineWidth);
}


static public PlotPanel plot(double [] x, double [] y, Color color, String  typeStr, String name, int lineWidth) {
             double [][] xy = new double [2][];
           
           xy[0] = x;     xy[1] = y;
            if (new_figure == true)  newPlot2D();
            char type='k';
            if (typeStr.length()==1)
                type=typeStr.charAt(0);
            if (typeStr.equals("--"))  // dashed
                type='-';
            else if (typeStr.equals("-."))   // dash-dot
                type = '#';
            //else if (typeStr.charAt(0)=='t') {  // control line thickness
              //  String thickness = typeStr.substring(1, typeStr.length());
               // float fthick = Float.valueOf(thickness);
                if (lineWidth > 1) {
                AbstractDrawer.lineThickness = lineWidth;
                type='t';
                }
            

 switch (type) {
     case 'l':   ((Plot2DPanel)currentPlot).addLinePlot(name,  color, xy, false); break;   // a line plot
     case '.':   ((Plot2DPanel)currentPlot).addLinePlot(name,  color,  xy, true); break;
     case 'x':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, new Font("Arial", Font.BOLD, 12), 'x', xy[0], xy[1]);    break;
     case '*':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, new Font("Arial", Font.BOLD, 12), '*', xy[0], xy[1]);    break;
     case 'o':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, new Font("Arial", Font.BOLD, 12), 'o', xy[0], xy[1]);    break;
     case 's':     ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, new Font("Arial", Font.BOLD, 12), '*', xy[0], xy[1]);    break;
     case 'd':   ((Plot2DPanel)currentPlot).addMarkedLinePlot(name, color, new Font("Arial", Font.BOLD, 12),  '\u0101', xy[0], xy[1]);    break;
     case ':':   	((Plot2DPanel)currentPlot).addLinePlot(name, color, AbstractDrawer.DOTTED_LINE, lineWidth, xy);  break;
     case 't':    	((Plot2DPanel)currentPlot).addLinePlot(name, color, AbstractDrawer.THICK_LINE, (int)AbstractDrawer.lineThickness, xy);  break;
     case '-':    	((Plot2DPanel)currentPlot).addLinePlot(name, color, AbstractDrawer.PATTERN_LINE, lineWidth, xy);  break;


     default:  break;
 }
 return currentPlot;
}

static public PlotPanel plot(double [] x,  double []  y, String  typeStr) {
      return  plot(x, y, typeStr, typeStr);
}

static public PlotPanel plot(Vec x,  Vec  y, String  titleOfPlot) {
     return  plot(x.getv(), y.getv(),  titleOfPlot);
}

static public PlotPanel plot(Vec x,  double []  y, String  titleOfPlot) {
     return  plot(x.getv(), y,  titleOfPlot);
}


static public PlotPanel plot(double [] x,  Vec  y, String  titleOfPlot) {
     return  plot(x, y.getv(),  titleOfPlot);
}



static public PlotPanel plot(Vec x,  Vec  y, String  typeStr, String name) {
      return  plot(x.getv(), y.getv(), typeStr, name);
}




static public PlotPanel plot(Vec x,  Vec  y, String  typeStr, String name, int lineWidth) {
      return  plot(x.getv(), y.getv(), typeStr, name, lineWidth);
}


static public PlotPanel plot(double [] x,  Vec  y, String  typeStr, String name, int lineWidth) {
      return  plot(x, y.getv(), typeStr, name, lineWidth);
}


static public PlotPanel plot(Vec x,  double []  y, String  typeStr, String name, int lineWidth) {
      return  plot(x.getv(), y, typeStr, name, lineWidth);
}




static public PlotPanel plot(Vec x,  Vec  y, String  typeStr,  int lineWidth) {
    String name="2-D plot";
    return plot(x, y, typeStr, name, lineWidth);
}

static public PlotPanel plot(double [] x,  Vec  y, String  typeStr,  int lineWidth) {
    String name="2-D plot";
    return plot(x, y, typeStr, name, lineWidth);
}

static public PlotPanel plot(Vec x,  double []  y, String  typeStr,  int lineWidth) {
    String name="2-D plot";
    return plot(x, y, typeStr, name, lineWidth);
}

// var t = inc(0, 0.1, 20); var x = sin(4.3*t); plot(t,x, true);
static public PlotPanel plot(double []  x,  double []  y, boolean dotted) {
            String name = "Line Plot";
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] = y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addLinePlot(name,xy, dotted);

	    return currentPlot;
        }

static public PlotPanel plot(Vec  x,  double []  y, boolean dotted) {
   return plot(x.getv(), y, dotted);
}

static public PlotPanel plot(double []  x,  Vec  y, boolean dotted) {
   return plot(x, y.getv(), dotted);
}

static public PlotPanel plot(Vec  x,  Vec  y, boolean dotted) {
   return plot(x.getv(), y.getv(), dotted);
}

static public PlotPanel plot(double [] x, double [] y, double [] z, Color color)  {
            return plot(x, y, z, color, "Figure");
        }

static public PlotPanel plot(float  [] x, float [] y, float []z, Color color) {
         return plot(arrToDouble(x), arrToDouble(y), arrToDouble(z), color);
       }

static public PlotPanel plot(Vec x, Vec y, Vec z, Color color)  {
            return plot(x.getv(), y.getv(), z.getv(), color, "Figure");
        }

          // plot Scala's Vectors
static public PlotPanel plot(Vec x)  {
            return plot(x.getv());
        }

static public PlotPanel plot(Vec x, String title) {
            return plot(x.getv(), title);
        }

static public PlotPanel plot(Vec x, Color color, String title) {
            return plot(x.getv(), color, title);
        }

        
 static public PlotPanel plot(Vec  x,  Color color) {
    return  plot(x.getv(), color);
 }

 static public PlotPanel plot( String name, Vec x, Vec y)  {
            return plot(x.getv(), y.getv(), name);
        }

 static public PlotPanel plot(Vec x, Vec y, String name, Color color)  {
            return plot(x.getv(), y.getv(), name, color);
        }

 static public PlotPanel plot(Vec x, Vec y, Color color, String name)  {
            return plot(x.getv(), y.getv(), color, name);
        }

 static public PlotPanel plot(Vec x, Vec y, Color color)  {
            return plot(x.getv(), y.getv(), color, "Figure ");
        }

 static public PlotPanel plot(Vec x, Vec y, Vec z, Color color, String name)  {
            return plot(x.getv(), y.getv(), z.getv(), color, name);
        }


 static public PlotPanel plot(Vec x, Vec y, Vec z)  {
            return plot(x.getv(), y.getv(), z.getv());
        }

 

// plot Scala's Matrices
 static public PlotPanel plot(Mat x)  {
            return plot(x.getRow(0));
        }

 static public PlotPanel plot(Mat x, Color color)  {
            return plot(x.getRow(0), color);
        }

    static public PlotPanel plot(Mat x, Mat y)  {
            return plot(x.getRow(0), y.getRow(0));
        }

    static public PlotPanel plot(Mat x, Mat y, String name)  {
            return plot(x.getRow(0), y.getRow(0), name);
        }

        static public PlotPanel plot(Mat x, Mat y, String name, Color color)  {
            return plot(x.getRow(0), y.getRow(0), name, color);
        }

        static public PlotPanel plot(Mat x, Mat y, Color color, String name)  {
            return plot(x.getRow(0), y.getRow(0), color, name);
        }


// plot Scala's Matrixes
     static public PlotPanel plot(Matrix x)  {
            return plot(x.getRow(1));
        }

     static public PlotPanel plot(Matrix x, Color color)  {
            return plot(x.getRow(1), color);
        }

    static public PlotPanel plot(Matrix x, Matrix y)  {
            return plot(x.getRow(1), y.getRow(1));
        }

    static public PlotPanel plot(Matrix x, Matrix y, String name)  {
            return plot(x.getRow(1), y.getRow(1), name);
        }

        static public PlotPanel plot(Matrix x, Matrix y, String name, Color color)  {
            return plot(x.getRow(1), y.getRow(1), name, color);
        }

        static public PlotPanel plot(Matrix x, Matrix y, Color color, String name)  {
            return plot(x.getRow(1), y.getRow(1), color, name);
        }

        static public PlotPanel plot( String name, double []  x,  double [] y) {
            int xl = x.length;
            double [][] xy = new double [2][xl];
            xy[0] = new double[xl];
            for (int k=0; k<xl; k++)
                 xy[0][k] = x[k];
            xy[1] = new double[xl];
            for (int k=0; k<xl; k++)
                xy[1][k] = y[k];
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, xy);
	    else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, xy);

            return currentPlot;
        }

        // end Scala matrices/vectors

        static public PlotPanel plot(double []  x,  double [] y,  String name, Color color) {
           return plot(x, y, color, name);
       }

       static public PlotPanel plot(float  [] x, float [] y, String name, Color color) {
         return plot(arrToDouble(x), arrToDouble(y), name, color);
       }

        static public PlotPanel plot(double []  x,  double [] y,  Color color, String name) {
            int xl = x.length;
            double [][] xy = new double [2][xl];
            xy[0] = new double[xl];
            for (int k=0; k<xl; k++)
                 xy[0][k] = x[k];
            xy[1] = new double[xl];
            for (int k=0; k<xl; k++)
                xy[1][k] = y[k];
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);
        if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, color,  xy);
	    else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, color, xy);

            return currentPlot;
        }

       static public PlotPanel plot(float  [] x, float [] y, Color color, String name) {
         return plot(arrToDouble(x), arrToDouble(y), color, name );
       }

       static public PlotPanel plot(double []  x,  double [] y) {
            String name = "2-D Line Plot";
            int xl = x.length;
            double [][] xy = new double [2][xl];
            xy[0] = new double[xl];
            for (int k=0; k<xl; k++)
                 xy[0][k] = x[k];
            xy[1] = new double[xl];
            for (int k=0; k<xl; k++)
                xy[1][k] = y[k];
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);
        if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, xy);
	    else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, xy);

	    return currentPlot;
        }

        static public PlotPanel plot(float  [] x, float [] y) {
         return plot(arrToDouble(x), arrToDouble(y));
       }


        static public void setName(String name) {
            currentPlot.setName(name);
        }
        
static public PlotPanel plot(double []  x,  double [] y, Color color ) {
            int xl = x.length;
            String name = "2-D Line Plot";
            double [][] xy = new double [2][xl];
            xy[0] = new double[xl];
            for (int k=0; k<xl; k++)
                 xy[0][k] = x[k];
            xy[1] = new double[xl];
            for (int k=0; k<xl; k++)
                xy[1][k] = y[k];
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)  // remove previous plots
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, color, xy);
	    else
	((Plot2DPanel)currentPlot).addLinePlot(name, color, xy);

	    return currentPlot;
        }

        static public PlotPanel plot(float  [] x, float [] y, Color color) {
         return plot(arrToDouble(x), arrToDouble(y), color );
       }

static public PlotPanel plot(double []  x) {
        int xlen = x.length;
        String name = "2-D Line Plot";
        double [][] xy = new double [2][xlen];
        xy[0] = new double [ xlen ];
        for (int k=0; k<xlen; k++)
              xy[0][k] = k;
        for (int k=0; k<xlen; k++)
            xy[1][k] = x[k];
            if (new_figure == true)  newPlot2D();
               if (holdOnMode == false)
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, xy);
	    else
	((Plot2DPanel)currentPlot).addLinePlot(name, xy);


       return currentPlot;
  }

 static public PlotPanel plot(float  [] x) {
         return plot(arrToDouble(x) );
   }

 static public PlotPanel plot(double []  x, Color color ) {
            int xlen = x.length;
            String name = "2-D Line Plot";
            double [][] xy = new double [2][xlen];
            xy[0] = new double [ xlen ];
            for (int k=0; k<xlen; k++)
                xy[0][k] = k;
            for (int k=0; k<xlen; k++) 
                xy[1][k] = x[k];
            if (new_figure == true)  newPlot2D();
               if (holdOnMode == false)
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, color, xy);
	    else
 	((Plot2DPanel)currentPlot).addLinePlot(name, color, xy);

    return currentPlot;
  }

 

 static public PlotPanel plot(float  [] x,  Color color) {
         return plot(arrToDouble(x), color);
       }

   static public PlotPanel plot(double []  x, String name) {
            Color color =  Color.BLACK;
            if (name.length() == 1) {
                char colorChar = name.charAt(0);
            switch (colorChar) {
                case 'b':  color = Color.BLUE;
                               break;
                case 'g':  color = Color.GREEN;
                                break;
                case 'r':  color = Color.RED;
                                break;
                case 'm': color = Color.MAGENTA;
                                break;
                case 'y':  color = Color.YELLOW;
                                break;
                default:   color = Color.BLACK;
                                break;
              }

            }

            int xlen = x.length;
            double [][] xy = new double [2][xlen];
            xy[0] = new double [ xlen ];
            for (int k=0; k<xlen; k++)
            {
                xy[0][k] = k;
                xy[1][k] =  x[k];
            }
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, color, xy);
	    else
	((Plot2DPanel)currentPlot).addLinePlot(name, color, xy);

            return currentPlot;

      }

   /*
    
    var t = linspace(0, 10, 2000)
    var x = sin(2.3*t)
    rplot(x, "Real-Time Plot")
    */
   static public PlotPanel rplot(double []  x, String name) {
            Color color =  Color.BLACK;
            if (name.length() == 1) {
                char colorChar = name.charAt(0);
            switch (colorChar) {
                case 'b':  color = Color.BLUE;
                               break;
                case 'g':  color = Color.GREEN;
                                break;
                case 'r':  color = Color.RED;
                                break;
                case 'm': color = Color.MAGENTA;
                                break;
                case 'y':  color = Color.YELLOW;
                                break;
                default:   color = Color.BLACK;
                                break;
              }

            }

            int xlen = x.length;
            double [][] xy = new double [2][xlen];
            xy[0] = new double [ xlen ];
            for (int k=0; k<xlen; k++)
            {
                xy[0][k] = k;
                xy[1][k] =  x[k];
            }
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);

            ((Plot2DPanel)currentPlot).addRealTimePlot(name, color, xy);
	
            return currentPlot;

      }


   static public PlotPanel plot(float  [] x, String name) {
         return plot(arrToDouble(x), name );
       }

      static public PlotPanel plot(double []  x,  Color color, String name) {
            int xlen = x.length;
            double [][] xy = new double [2][xlen];
            xy[0] = new double [ xlen ];
            for (int k=0; k<xlen; k++) {
                xy[0][k] = k;
                xy[1][k] = x[k];
            }
            if (new_figure == true)  newPlot2D();
	    if (holdOnMode == false)
                    clf(currentPlot);

            if (scatterPlotOn)
            	((Plot2DPanel)currentPlot).addScatterPlot(name, color, xy);
	    else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, color, xy);

            return currentPlot;
        }


       static public PlotPanel plot(float  [] x, Color color, String name) {
         return plot(arrToDouble(x), color, name );
       }

        /*
           b     blue          .     point              -     solid
           g     green         o     circle             :     dotted
           r     red           x     x-mark             -.    dashdot
           c     cyan          +     plus               --    dashed
           m     magenta       *     star             (none)  no line
           y     yellow        s     square
           k     black         d     diamond
                               v     triangle (down)
                               ^     triangle (up)
                               <     triangle (left)
                               >     triangle (right)
                               p     pentagram
                               h     hexagram
           */

        static public PlotPanel plot2d_staircase(Object  x,  Object y, String name) {
            double [][] xy = new double [2][];
            xy[0] = (double []) x;
            xy[1] = (double []) y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addStaircasePlot(name, xy);
	    return currentPlot;
        }


        static public PlotPanel plot2d_staircase(double []  x,  double [] y, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] = y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addStaircasePlot(name, xy);
	    return currentPlot;
        }

       static public PlotPanel plot2d_staircase(float  [] x, float [] y, String name) {
         return plot2d_staircase(arrToDouble(x), arrToDouble(y),  name );
       }

        static public PlotPanel plot2d_staircase(double []  x,  double [] y, Color color, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] = y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addStaircasePlot(name, color, xy);
	    return currentPlot;
        }


       static public PlotPanel plot2d_staircase(float  [] x, float [] y, Color color, String name) {
         return plot2d_staircase(arrToDouble(x), arrToDouble(y),  color, name );
       }



        static public PlotPanel plot2d_bar(double []  x,  double [] y, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] =  y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addBarPlot(name,xy);
	    return currentPlot;
        }



        
        static public PlotPanel plot2d_bar(double []  x,  double [] y, Color color, String name) {
            double [][] xy = new double [2][];
            xy[0] = x;
            xy[1] =  y;
            if (new_figure == true)  newPlot2D();
	    ((Plot2DPanel)currentPlot).addBarPlot(name, color, xy);
	    return currentPlot;
        }
        
           static public PlotPanel plot2d_contour(double [][]XY, String name) {
            	  
            if (new_figure == true)  newPlot2D();
	        if (holdOnMode == false)
                    clf(currentPlot);
		((Plot2DPanel)currentPlot).addContourPlot(name, XY);

                return currentPlot;

        }


       static public PlotPanel plot2d_countour(float  [][] xy, String name) {
         return plot2d_contour(arrToDoubleDouble(xy), name );
       }

        static public PlotPanel plot2d_contour(double [][]XY, Color color, String name) {
            	  
            if (new_figure == true)  newPlot2D();
	        if (holdOnMode == false)
                    clf(currentPlot);
		((Plot2DPanel)currentPlot).addContourPlot(name, XY);

                return currentPlot;
        }


       static public PlotPanel plot2d_countour(float  [][] xy, Color color, String name) {
         return plot2d_contour(arrToDoubleDouble(xy), color, name );
       }

        static public PlotPanel plot2d_contour(Matrix mXY, String name) {
                double [][] XY = mXY.getv();
       if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addContourPlot(name, XY);
                return currentPlot;
        }


        static public PlotPanel plot2d_contour(Matrix mXY,  Color color, String name) {
            double [][] XY = mXY.getv();
       if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addContourPlot(name, XY);
                return currentPlot;

        }

             static public PlotPanel plot2d_contour(Mat mXY, String name) {
                double [][] XY = mXY.getv();
       if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addContourPlot(name, XY);
                return currentPlot;
        }


        static public PlotPanel plot2d_contour(Mat mXY,  Color color, String name) {
            double [][] XY = mXY.getv();
       if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addContourPlot(name, XY);
                return currentPlot;

        }

        static public PlotPanel plot2d_scalogram(double [][]XY, String name) {
            	if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addScalogramPlot(name, XY);
                return currentPlot;
        }

       static public PlotPanel plot2d_scalogram(float  [][] xy, String name) {
         return plot2d_scalogram(arrToDoubleDouble(xy), name );
       }

        static public PlotPanel plot2d_scalogram(Matrix mXY, String name) {
            double [][] XY = mXY.getv();
            if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addScalogramPlot(name, XY);
                return currentPlot;
        }

       static public PlotPanel plot2d_scalogram(Mat mXY, String name) {
            double [][] XY = mXY.getv();
            if (new_figure == true)  newPlot2D();
	        ((Plot2DPanel)currentPlot).addScalogramPlot(name, XY);
                return currentPlot;
        }



        static public PlotPanel plot2d_cloud(double [][] sample, int slices_x,int slices_y, String name) {
		if (new_figure == true)  newPlot2D();
	    	((Plot2DPanel)currentPlot).addCloudPlot(name, sample, slices_x, slices_y);
		return currentPlot;
	}


       static public PlotPanel plot2d_cloud(float  [][] sample, int slices_x,int slices_y, String name) {
         return plot2d_cloud(sample, slices_x, slices_y,  name);
       }

        static public PlotPanel plot2d_cloud(double[][] sample, int slices_x,int slices_y, Color color, String name) {
		if (new_figure == true)  newPlot2D();
	    	((Plot2DPanel)currentPlot).addCloudPlot(name, color, sample, slices_x, slices_y);
		return currentPlot;
	}


       static public PlotPanel plot2d_cloud(float  [][] sample, int slices_x, int slices_y,  Color color, String name) {
         return plot2d_cloud(sample, slices_x, slices_y,  color, name);
       }


        static public PlotPanel plot3d_cloud(Matrix sample, int slices_x,int slices_y,int slices_z, String name) {
		 
        	newPlot3D();
		((Plot3DPanel)currentPlot).addCloudPlot(name,sample.getv(),slices_x, slices_y, slices_z);
		return currentPlot;
	}



	static public PlotPanel plot3d_cloud(Matrix sample, int slices_x,int slices_y,int slices_z, Color color, String name) {
		 
        	newPlot3D();
		((Plot3DPanel)currentPlot).addCloudPlot(name, color, sample.getv(),slices_x, slices_y, slices_z);
		return currentPlot;
	}


        static public PlotPanel plot3d_cloud(Mat sample, int slices_x,int slices_y,int slices_z, String name) {
		 
        	newPlot3D();
		((Plot3DPanel)currentPlot).addCloudPlot(name,sample.getv(),slices_x, slices_y, slices_z);
		return currentPlot;
	}


	static public PlotPanel plot3d_cloud(Mat sample, int slices_x,int slices_y,int slices_z, Color color, String name) {
		 
        	   newPlot3D();
		((Plot3DPanel)currentPlot).addCloudPlot(name, color, sample.getv(),slices_x, slices_y, slices_z);
		return currentPlot;
	}

        static public PlotPanel plot2d_histogram(double [][] xy, int slices_x, String name) {
		if (new_figure == true)  newPlot2D();
                ((Plot2DPanel)currentPlot).addHistogramPlot(name, xy, slices_x);
                return currentPlot;
        }

        static public PlotPanel plot2d_histogram(float [][] xy, int slices_x, String name) {
             return    plot2d_histogram(xy, slices_x, name);
        }

        static public PlotPanel plot2d_histogram(double [] x, double [] y, int slices_x, String name) {
		if (new_figure == true)  newPlot2D();
                double [][] xy = { x, y};
                ((Plot2DPanel)currentPlot).addHistogramPlot(name, xy, slices_x);
                return currentPlot;
        }

        static public PlotPanel plot2d_histogram(double [] x, int slices_x, String name) {
		if (new_figure == true)  newPlot2D();
                ((Plot2DPanel)currentPlot).addHistogramPlot(name, x, slices_x);
                return currentPlot;
        }
        
        
        static public PlotPanel plot2d_histogram(Vec x, int slices_x, String name) {
		if (new_figure == true)  newPlot2D();
                ((Plot2DPanel)currentPlot).addHistogramPlot(name, x.getv(), slices_x);
                return currentPlot;
        }

        static public PlotPanel plot2d_histogram(float [] x, float [] y,  int slices_x, String name) {
           return  plot2d_histogram(x, y,  slices_x, name);
        }

        static public  void setAxes(int axe, String axisType)  {  // ?????
            currentPlot.setAxisScale(axe, axisType);
        }

        static public void  markX(double  x, double  y) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, x, y);
        }

      static public void  markX(int x, int y) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, x, y);
        }

      static public void  markX(double  x, double  y, Color c) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, c, x, y);
        }

     static public void  markX(int x, int y, Color c) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, c, x, y);
        }

     static public void  mark(char ch, double  x, double  y) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, x, y);
        }

      static public void  mark(char ch, int x, int y) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, x, y);
        }

      static public void  mark(char ch, double  x, double  y, Color c) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, c, x, y);
        }

     static public void  mark(char ch, int x, int y, Color c) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, c, x, y);
        }


static public void  markX(double  x, double  y, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  markX(int x, int y, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  markX(double  x, double  y, Color c, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, c, f, x, y);
        }

     static public void  markX(int x, int y, Color c, Font f) {
             String sch = String.valueOf(PlotGlobals.defaultMarkChar);
             currentPlot.plotCanvas.addDLabel(sch, c, f, x, y);
        }

     static public void  mark(char ch, double  x, double  y, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  mark(char ch, int x, int y, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  mark(char ch, double  x, double  y, Color c, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, c, f, x, y);
        }

     static public void  mark(char ch, int x, int y, Color c, Font f) {
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, c, f, x, y);
        }

// take font size only
     static public void  markX(double  x, double  y, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  markX(int x, int y, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  markX(double  x, double  y, Color c, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addLabel(sch, c, f, x, y);
        }

     static public void  markX(int x, int y, Color c, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(PlotGlobals.defaultMarkChar);
            currentPlot.plotCanvas.addDLabel(sch, c, f, x, y);
        }

     static public void  mark(char ch, double  x, double  y, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(ch);
            currentPlot.plotCanvas.addLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  mark(char ch, int x, int y, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(ch);
            currentPlot.plotCanvas.addDLabel(sch, Color.BLUE, f, x, y);
        }

      static public void  mark(char ch, double  x, double  y, Color c, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(ch);
            currentPlot.plotCanvas.addLabel(sch, c, f, x, y);
        }

     static public void  mark(char ch, int x, int y, Color c, int fontSize ) {
            Font f = new Font("Arial", Font.BOLD, fontSize);
            String sch = String.valueOf(ch);
            currentPlot.plotCanvas.addDLabel(sch, c, f, x, y);
        }

 

        static public PlotPanel plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z) {
           Color color = Color.BLACK;
           String name = "2-D  Vector Plot";
           return plotv(x, y, color, name);
       }

       static public PlotPanel plotV(Vector <double [] >  x) {
           Color color = Color.BLACK;
           String name = "2-D  Vector Plot";
         return plotV(x, color, name);
       }



       static public PlotPanel plotv(Vector <Double>  x, Vector <Double> y, Color color) {
           String name = "2-D  Vector Plot";
           return plotv(x,  y, color, name);
       }

       static public PlotPanel plotv(Vector <double [] >  x, Color color) {
           String name = "2-D  Vector Plot";
         return plotV(x, color, name);
       }

       static public PlotPanel plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z, String name) {
           Color color = Color.BLACK;
           return plotv(x, y, z, color, name);
       }

       static public PlotPanel plotV(Vector <double [] >  x, String name) {
           Color color = Color.BLACK;
           return plotV(x, color, name);
       }



static public PlotPanel plotv(Vector <Double>  x, Vector <Double> y, Color color, String name) {
           double [] xx = toArr(x); double [] yy = toArr(y);
           	newPlot2D();
	          if (scatterPlotOn)
                ((Plot2DPanel)currentPlot).addScatterPlot(name, color, xx, yy);
            else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, color, xx, yy);

        	return currentPlot;
	}


static public PlotPanel plotv(Vector <Double>  x, Vector <Double> y, Vector <Double> z, Color color, String name) {
           double [] xx = toArr(x); double [] yy = toArr(y);  double [] zz = toArr(z);
           	newPlot3D();
	          if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, color, xx, yy, zz);
            else
	 	((Plot3DPanel)currentPlot).addLinePlot(name, color, xx, yy, zz);

        	return currentPlot;
	}

     static public PlotPanel plotV(Vector <double [] >  x, Color color, String name) {
         double [][] elems = toArrv(x);
         int siz = elems.length;
         int dims = elems[0].length;   // dimensions of elements in the Vector
         if (dims == 2)  {  // two-dimensional case
               double [] xelem = elems[0];
               double [] yelem = elems[1];
               newPlot2D();
	          if (scatterPlotOn) {
                ((Plot2DPanel)currentPlot).addScatterPlot(name, color, xelem, yelem);
                  }
            else
	 	((Plot2DPanel)currentPlot).addLinePlot(name, color, xelem, yelem);
           }
           else { // three-dimensional case
               double [] xelem = elems[0];
               double [] yelem = elems[1];
               double [] zelem = elems[2];
               double [][] allelems = new double[3][];
               newPlot3D();
	         if (scatterPlotOn)
                ((Plot3DPanel)currentPlot).addScatterPlot(name, color, xelem, yelem, zelem);
            else
	 	((Plot3DPanel)currentPlot).addLinePlot(name, color, xelem, yelem, zelem);

           }

        	return currentPlot;
	}


     // returns a new Color each time is called
     static public  Color  newColor() {
         Color colorToUse  =  colorTable[currentColorIndex];
         currentColorIndex++;
         if (currentColorIndex == colorTable.length)
             currentColorIndex = 0;
         return colorToUse;
         
     }

}
