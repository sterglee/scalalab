package scalaSci.math.plot;

import java.awt.*;

import javax.swing.JFrame;
import scalaSci.math.plot.canvas.*;


/**
 * Class suitable for plotting 2D data on a panel, to be added to a swing
 * container.
 */
public class Plot2DPanel extends PlotPanel {
    
    /**
     * Constructor for Plot2DPanel.
     * Create a new blank instance of Plot2DPanel, to be added to a swing
     * component.
     */
    public Plot2DPanel() {
        super(new Plot2DCanvas());
    }
   
   public Plot2DPanel(Plot2DPanel [][]  _panels, String legendOrientation) {
       super(_panels, legendOrientation);
   }
   
    public Plot2DPanel(double[] min, double[] max, String[] axesScales, String[] axesLabels) {
        super(new Plot2DCanvas(min, max, axesScales, axesLabels));
    }
    
    public Plot2DPanel(PlotCanvas _canvas, String legendOrientation) {
        super(_canvas, legendOrientation);
    }
    
    public Plot2DPanel(PlotCanvas _canvas) {
        super(_canvas);
    }
    
    public Plot2DPanel(String legendOrientation) {
        super(new Plot2DCanvas(), legendOrientation);
    }
    
    /**
     * Adds a scatter plot (each data point is plotted as a single dot marker) to the current plot panel.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY Pairs of array of double. First one contains the X position
     * of the data points, second contains Y position.
     * <br>
     * Each array of the pair must be of the same length; if not a ArrayIndexOutOfBoundsException
     * exception will be thrown.
     * <br>
     * Each data set must come in <b>pair</b> of
     * array of double; if not a ArrayIndexOutOfBoundsException exception
     * will be thrown.
     * @return the index of the plot in the panel (int).
     * @see #addLinePlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[][], double[][])
     * @see #addHistogramPlot(String, Color, double[][], double[])
     * @see #addStaircasePlot(String, Color, double[]...)
     */
    public int addScatterPlot(String name, Color color, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addScatterPlot(name, color, XY);
    }
    
    public int addScatterPlot(String name, Color color, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addScatterPlot(name, color, Y);
    }
    
    public int addScatterPlot(String name, Color color, double[] X, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addScatterPlot(name, color, X,Y);
    }
    
    public int addScatterPlot(String name, double[][] XY) {
        return addScatterPlot(name, getNewColor(), XY);
    }

    
    public int addScatterPlot(String name, double[] Y) {
        return addScatterPlot(name, getNewColor(), Y);
    }
    
    public int addScatterPlot(String name, double[] X, double[] Y) {
        return addScatterPlot(name, getNewColor(), X,Y);
    }
    
    
    // Real-Time plots
    public int addRealTimePlot(String name, Color color, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addRealTimePlot(name, color, XY);
    }
    
    public int addRealTimePlot(String name, Color color, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addRealTimePlot(name, color, Y);
    }
    
    public int addRealTimePlot(String name, Color color, double[] X, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addScatterPlot(name, color, X,Y);
    }
    
    public int addRealTimePlot(String name, double[][] XY) {
        return addScatterPlot(name, getNewColor(), XY);
    }

    
    public int addRealTimePlot(String name, double[] Y) {
        return addScatterPlot(name, getNewColor(), Y);
    }
    
    public int addRealTimePlot(String name, double[] X, double[] Y) {
        return addScatterPlot(name, getNewColor(), X,Y);
    }
    
    
    /**
     * Adds a line plot (each data point is connected to the next one by a
     * solid line) to the current plot panel.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY Pairs of array of double. First one contains the X position
     * of the data points, second contains Y position.
     * <br>
     * Each array of the pair
     * must be of the same length; if not a ArrayIndexOutOfBoundsException
     * exception will be thrown.
     * <br>
     * Each data set must come in <b>pair</b> of
     * array of double; if not a ArrayIndexOutOfBoundsException exception
     * will be thrown.
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[]... )
     * @see #addHistogramPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     */
    
    
    public int addLinePlot(String name, Color color, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color, XY);
    }

     public int addLinePlot(String name, Color color, double[][] XY, int lineWidth) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color, XY, lineWidth);
    }

    public int addLinePlot(String name, Color color, int lineType,  double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color,  lineType,   XY);
    }

    public int addLinePlot(String name, Color color, int lineType,  int  lineWidth, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color,  lineType, lineWidth,  XY);
    }

    public int addLinePlot(String name, Color color, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color, Y);
    }
    
    public int addLinePlot(String name, Color color, double[] X, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addLinePlot(name, color, X,Y);
    }
    
    
    public int addLinePlot(String name, double[][] XY) {
        return addLinePlot(name, getNewColor(), XY);
    }
    
    public int addLinePlot(String name, double[][] XY, boolean dotted) {
        return ((Plot2DCanvas)plotCanvas).addLinePlot(name, getNewColor(), dotted, XY);
    }
    
     public int addLinePlot(String name, Color color, double[][] XY, boolean dotted) {
        return ((Plot2DCanvas)plotCanvas).addLinePlot(name, color, dotted, XY);
    }
    
    public int addLinePlot(String name, double[] Y) {
        return addLinePlot(name, getNewColor(), Y);
    }
    
    public int addLinePlot(String name, double[] X, double[] Y) {
        return addLinePlot(name, getNewColor(), X,Y);
    }
    
/* Marked Line plot TODOSterg*/
    public int addMarkedLinePlot(String name, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedLinePlot(name, getNewColor(), X, Y);
    }
   
    public int addMarkedLinePlot(String name, Color c, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedLinePlot(name, c, X, Y);
    }
    
    public int addMarkedLinePlot(String name, Color c, Font f, char mark, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedLinePlot(name, c, f, mark, X, Y);
    }
   
    public int addMarkedPlot(String name, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedPlot(name, getNewColor(), X, Y);
    }
   
    public int addMarkedPlot(String name, Color c, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedPlot(name, c, X, Y);
    }
    
    public int addMarkedPlot(String name, Color c, Font f, char mark, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedPlot(name, c, f, mark, X, Y);
    }
   
    public int addMarkedPlot(String name, Color c, Font f, char mark, int _skipPoints, double [] X, double [] Y) {
        return ((Plot2DCanvas) plotCanvas).addMarkedPlot(name, c, f, mark, _skipPoints, X, Y);
    }
   
    /**
     * Adds a bar plot (each data point is shown as a dot marker connected to
     * the horizontal axis by a vertical line) to the current plot panel.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY Pairs of array of double. First one contains the X position
     * of the data points, second contains Y position.
     * <br>
     * Each array of the pair
     * must be of the same length; if not a ArrayIndexOutOfBoundsException
     * exception will be thrown.
     * <br>
     * Each data set must come in <b>pair</b> of
     * array of double; if not a ArrayIndexOutOfBoundsException exception
     * will be thrown.
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addLinePlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[]... )
     * @see #addHistogramPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     */
    public int addBarPlot(String name, Color color, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addBarPlot(name, color, XY);
    }
    
    public int addBarPlot(String name, Color color, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addBarPlot(name, color, Y);
    }
    
    public int addBarPlot(String name, Color color, double[] X, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addBarPlot(name, color, X,Y);
    }
    
    public int addBarPlot(String name, double[][] XY) {
        return addBarPlot(name, getNewColor(), XY);
    }
    
    public int addBarPlot(String name, double[] Y) {
        return addBarPlot(name, getNewColor(), Y);
    }
    
    public int addBarPlot(String name, double[] X, double[] Y) {
        return addBarPlot(name, getNewColor(), X,Y);
    }
    
    /**
     * Adds a staircase plot (each data point is connected to the following
     * one by a horizontal line then a vertical line) to the current plot panel.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY Pairs of array of double. First one contains the X position
     * of the data points, second contains Y position.
     * <br>
     * Each array of the pair
     * must be of the same length; if not a ArrayIndexOutOfBoundsException
     * exception will be thrown.
     * <br>
     * Each data set must come in <b>pair</b> of
     * array of double; if not a ArrayIndexOutOfBoundsException exception
     * will be thrown.
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[][], double[][])
     * @see #addHistogramPlot(String, Color, double[][], double[])
     * @see #addLinePlot(String, Color, double[]...)
     */
    public int addStaircasePlot(String name, Color color, double[][] XY) {
        return ((Plot2DCanvas) plotCanvas).addStaircasePlot(name, color, XY);
    }
    
    public int addStaircasePlot(String name, Color color, double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addStaircasePlot(name, color, Y);
    }
    
    public int addStaircasePlot(String name, Color color, double[] X,double[] Y) {
        return ((Plot2DCanvas) plotCanvas).addStaircasePlot(name, color, X,Y);
    }
    
    public int addStaircasePlot(String name, double[][] XY) {
        return addStaircasePlot(name, getNewColor(), XY);
    }
    
    public int addStaircasePlot(String name, double[] Y) {
        return addStaircasePlot(name, getNewColor(), Y);
    }
    
    public int addStaircasePlot(String name, double[] X,double[] Y) {
        return addStaircasePlot(name, getNewColor(), X,Y);
    }
    
    /**
     * Adds a box plot to the current plot panel. Each data point is plotted
     * as a dot marker at the center of a rectangle.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY m*2 array of array of double. Contains the x,y coordinates of the
     * m boxes' center (m lines, 2 rows).
     * @param dXdY m*2 array of array of double. Contains the width and heigth of the
     * m boxes (m lines, 2 rows).
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     * @see #addHistogramPlot(String, Color, double[][], double[])
     * @see #addLinePlot(String, Color, double[]...)
     */
    public int addBoxPlot(String name, Color color, double[][] XY, double[][] dXdY) {
        return ((Plot2DCanvas) plotCanvas).addBoxPlot(name, color, XY, dXdY);
    }
    
    public int addBoxPlot(String name, double[][] XY, double[][] dXdY) {
        return addBoxPlot(name, getNewColor(), XY, dXdY);
    }
    
    /**
     * Adds a box plot to the current plot panel. Each data point is plotted
     * as a dot marker at the center of a rectangle.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XYdXdY m*4 array of array of double. Contains the x,y coordinates of the
     * m boxes' center and the boxes width and heigth (m lines, 4 rows).
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     * @see #addHistogramPlot(String, Color, double[][], double[])
     * @see #addLinePlot(String, Color, double[]...)
     */
    public int addBoxPlot(String name, Color color, double[][] XYdXdY) {
        return ((Plot2DCanvas) plotCanvas).addBoxPlot(name, color, XYdXdY);
    }
    
    public int addBoxPlot(String name, double[][] XYdXdY) {
        return addBoxPlot(name, getNewColor(), XYdXdY);
    }
    
    /**
     * Adds a histogram plot to the current plot panel. Each data point is as
     * vertical bar which width can be set.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param XY m*2 array of array of double. Contains the x coordinate and
     * the heigth of each bar (m lines, 2 rows).
     * @param dX Array of double. Contains the width each bar (m lines).
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[][])
     * @see #addLinePlot(String, Color, double[]...)
     */
    public int addHistogramPlot(String name, Color color, double[][] XY, double[] dX) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, color, XY, dX);
    }
    
    public int addHistogramPlot(String name, double[][] XY, double[] dX) {
        return addHistogramPlot(name, getNewColor(), XY, dX);
    }
    
    public int addHistogramPlot(String name, double[][] XY, int dX) {
        double [] dxF = new double [1];
        dxF[0] = dX;
        return addHistogramPlot(name, getNewColor(), XY, dxF);
    }
    
    public int addHistogramPlot(String name, Color color, double[][] XYdX) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, color, XYdX);
    }
    
    /**
     * Adds a histogram plot to the current plot panel. Each data point is as
     * vertical bar which width can be set.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param XYdX m*3 array of array of double. Contains the x coordinate,
     * the heigth of each bar and the width of each bar (m lines, 3 rows).
     * @return the index of the plot in the panel (int).
     * @see #addScatterPlot(String,Color,double[]...)
     * @see #addBarPlot(String, Color, double[]...)
     * @see #addStaircasePlot(String, Color, double[]...)
     * @see #addBoxPlot(String, Color, double[][])
     * @see #addLinePlot(String, Color, double[]...)
     */
    public int addHistogramPlot(String name, double[][] XYdX) {
        return addHistogramPlot(name, getNewColor(), XYdX);
    }
    
    /**
     * Adds a plot of the statistical repartition of a sample, as a histogram.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param sample Array of double containing the data which statistics will be plotted.
     * @param n Bin number for the statistics (int).
     * @return the index of the plot in the panel (int).
     */
    public int addHistogramPlot(String name, Color color, double[] sample, int n) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, color, sample, n);
    }
    
    public int addHistogramPlot(String name, double[] X, int n) {
        return addHistogramPlot(name, getNewColor(), X, n);
    }
    
    /**
     * Adds a plot of the statistical repartition of a sample, as a histogram. 
     * The bins' limits can be set.
     * @param name Name for the plot, which will be used in the legend.
     * (String)
     * @param color Plot color. (Color)
     * @param sample Array of double containing the data which statistics will be plotted.
     * @param bounds Specify the limits for the bins' boundaries.
     * @return the index of the plot in the panel (int).
     */
    public int addHistogramPlot(String name, Color color, double[] sample, double... bounds) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, color, sample, bounds);
    }
    
    public int addHistogramPlot(String name, double[] X, double... bounds) {
        return addHistogramPlot(name, getNewColor(), X, bounds);
    }
    
    public int addHistogramPlot(String name, Color color, double[] X, double min, double max, int n) {
        return ((Plot2DCanvas) plotCanvas).addHistogramPlot(name, color, X, min, max, n);
    }
    
    public int addHistogramPlot(String name, double[] X, double min, double max, int n) {
        return addHistogramPlot(name, getNewColor(), X, min, max, n);
    }
    
    public int addCloudPlot(String name, Color color, double[][] sampleXY, int nX, int nY) {
        return ((Plot2DCanvas) plotCanvas).addCloudPlot(name, color, sampleXY, nX,nY);
    }
    
    public int addCloudPlot(String name, double[][] sampleXY, int nX, int nY) {
        return addCloudPlot(name, getNewColor(), sampleXY, nX,nY);
    }
    
    
    public int addContourPlot(String name, double [][] XY)  {
         return ((Plot2DCanvas) plotCanvas).addContourPlot(name, getNewColor(), XY);
    }
    
    public int addScalogramPlot(String name, double [][] XY)  {
       return ((Plot2DCanvas) plotCanvas).addScalogramPlot(name, getNewColor(), XY);
    }
    
    @Override
    public int addPlot(String type, String name, Color color, double[]... XY) {
        if (type.equalsIgnoreCase(SCATTER)) {
            return addScatterPlot(name, color, XY);
        } else if (type.equalsIgnoreCase(LINE)) {
            return addLinePlot(name, color, XY);
        } else if (type.equalsIgnoreCase(BAR)) {
            return addBarPlot(name, color, XY);
        } else if (type.equalsIgnoreCase(STAIRCASE)) {
            return addStaircasePlot(name, color, XY);
        } else if (type.equalsIgnoreCase(HISTOGRAM)) {
            return addHistogramPlot(name, color, XY);
        } else if (type.equalsIgnoreCase(BOX)) {
            return addBoxPlot(name, color, XY);
        } else {
            throw new IllegalArgumentException("Plot type is unknown : " + type);
        }
    }

    	public static void main(String[] args) {
		Plot2DPanel p2 = new Plot2DPanel();
		for (int i = 0; i < 3; i++) {
			double[][] XYZ = new double[10][2];
			for (int j = 0; j < XYZ.length; j++) {
				XYZ[j][0] = /*1 + */Math.random();
				XYZ[j][1] = /*100 * */Math.random();
			}
                        double[][] XYZ2 = new double[100][2];
			for (int j = 0; j < XYZ2.length; j++) {
				XYZ2[j][0] = j;
				XYZ2[j][1] = /*100 * */Math.random();
			}
			p2.addScatterPlot("toto" + i, XYZ);
                        p2.addLinePlot("toto2" + i, XYZ2);
                        
		}

		p2.setLegendOrientation(PlotPanel.SOUTH);
                p2.plotCanvas.addLabel("sterg", Color.red, 1.2, 1.2);
		new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

    
}
