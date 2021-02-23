package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;

import scalaExec.Interpreter.GlobalValues;
import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class LineMarksPlot extends ScatterPlot {
        public Font defaultFont = PlotGlobals.lineMarksPlotFont;
        public boolean draw_dot = false;
        public char activeMark = PlotGlobals.defaultMarkChar;
        public int numMarks = 30;
        public Color markDefaultColor = PlotGlobals.defaultMarksColor;
        public Color lineDefaultColor = Color.RED;


       public LineMarksPlot(String n, Color c, Font f, char mark, double[][] _XY) {
                super(n, c, _XY);
                defaultFont = f;
                activeMark = mark;
	}
        
        public LineMarksPlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
	super(n, c, _pattern, _XY);
	}

        public LineMarksPlot( String n,  Color c,  boolean[][] _pattern,  double[]  _X,   double [] _Y)  {
              super(n, c, _pattern, _X, _Y);
	}

        public LineMarksPlot(String n, Color c, int _type, int _radius, double[][] _XY) {
               super(n, c, _type, _radius, _XY);
	}

         public LineMarksPlot(String n, Color c, double[][] _XY) {
                 super(n, c, _XY);
	}
         
         // set the number of marks to draw on the plot
         public void setNumberOfMarks(int nmarks) {
             numMarks = nmarks;
         }

         public void plot(AbstractDrawer draw, Color c) {
             int lxy = XY.length;
                
             int skipPoints  = lxy/numMarks;
                
             if (!visible)
                return;
             if (draw_dot)
                super.plot(draw, c);
        draw.setFont(defaultFont);
        draw.setColor(markDefaultColor);
        draw.setLineType(AbstractDrawer.CONTINOUS_LINE);
        // draw the markers
        for (int i = 0; i < XY.length - 1-skipPoints;  i+=skipPoints)
	draw.drawText(new String(new char[]{activeMark}), XY[i][0], XY[i][1]);
         draw.setColor(lineDefaultColor);
           // connect with lines
         for (int i=0; i < XY.length - 1; i++)
                draw.drawLine(XY[i], XY[i+1]);
        }

}