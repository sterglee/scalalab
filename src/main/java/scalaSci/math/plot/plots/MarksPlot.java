package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class MarksPlot extends ScatterPlot {
        public Font marksFont = PlotGlobals.lineMarksPlotFont;  // set the font for drawing the marks
        public boolean draw_dot = PlotGlobals.draw_dot;
        public char  activeMark =PlotGlobals.defaultMarkChar;
        public int skipPoints = PlotGlobals.defaultSkipPoints;
        public Color markDefaultColor = PlotGlobals.defaultMarksColor;
        public Color lineDefaultColor = Color.RED;


    public MarksPlot(String n, Color c, Font f, char mark, double[][] _XY) {
         super(n, c, _XY);
         marksFont = f;
         activeMark = mark;
}
    
    public MarksPlot(String n, Color c, Font f, char mark, int _skipPoints,  double[][] _XY) {
         super(n, c, _XY);
         marksFont = f;
         skipPoints = _skipPoints;
         activeMark = mark;
}
    
    public MarksPlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c, _pattern, _XY);
}

    public MarksPlot( String n,  Color c,  boolean[][] _pattern,  double[]  _X,   double [] _Y)  {
           super(n, c, _pattern, _X, _Y);
 }

    public MarksPlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c, _type, _radius, _XY);
}

     public MarksPlot(String n, Color c, double[][] _XY) {
        super(n, c, _XY);
}

     // sets how many points to skip
       public void setSkipPoints(int _skipPoints) {
           skipPoints = _skipPoints;
       }
       
       // sets the default mark char
       public void setMarkChar(char _markChar) {
           activeMark = _markChar;
       }
       
    public void plot(AbstractDrawer draw, Color c) {
                if (!visible)
                    return;

                if (draw_dot)
	super.plot(draw, c);
                draw.setFont(marksFont);
                draw.setColor(markDefaultColor);
                draw.setLineType(AbstractDrawer.CONTINOUS_LINE);
                for (int i = 0; i < XY.length - 1-skipPoints;  i+=skipPoints)
	   draw.drawText(new String(new char[]{activeMark}), XY[i][0], XY[i][1]);
	}

}