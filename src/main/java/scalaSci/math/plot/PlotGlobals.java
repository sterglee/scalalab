
package scalaSci.math.plot;

// controls plotting system properties

import java.awt.Color;
import java.awt.Font;


// TODO:  Add configuration dialog for these settings?

public class PlotGlobals {
   static  public Color legendPanelBackgroundColor = Color.WHITE;
   
    // plotting system default configuration options
    static public Font defaultAbstractDrawerFont = new Font("BitStream Vera Sans", Font.PLAIN, 12);
    static public Color defaultAbstractDrawerColor = Color.BLACK;
    static public Color defaultFormulaColor = Color.BLUE;
    static public float lineThickness = 5.0f;  // width for pattern line
    
    static public char defaultMarkChar = 'X';
    static public Font lineMarksPlotFont = new Font("Arial", Font.PLAIN, 10);
    static public Color  defaultMarksColor = Color.PINK;
    
    static public Color latexColor = Color.GREEN;
    
    static public int limitPlotPoints = 5000; // do not plot more than these for speed
    static public boolean skipPointsOnPlot = true;
    static public int defaultSkipPoints = 5;
    
    static public boolean draw_dot = false;
    // for 3-D plots
    static public int limitPlotPointsX = 500; // do not plot more than these for speed
    static public int limitPlotPointsY= 500; // do not plot more than these for speed
    
     
}
