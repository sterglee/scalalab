package scalaSci.math.plot.canvas;

import java.awt.*;

import scalaSci.math.plot.plotObjects.*;
import scalaSci.math.plot.plots.*;
import scalaSci.math.plot.render.*;

import static scalaSci.math.plot.plotObjects.Base.*;
import static scalaSci.math.plot.utils.Histogram.*;
import static scalaSci.math.plot.utils.PArray.*;



public class Plot2DCanvas extends PlotCanvas {
    public Plot2DCanvas() {
        super();
        ActionMode = ZOOM;
        }

    public Plot2DCanvas(Base b, BasePlot bp) {
        super(b, bp);
        ActionMode = ZOOM;
        }

    public Plot2DCanvas(double[] min, double[] max, String[] axesScales, String[] axesLabels) {
        super(min, max, axesScales, axesLabels);
        ActionMode = ZOOM;
        }

    public void initDrawer() {
        draw = new AWTDrawer2D(this);
        }

    public void initBasenGrid(double[] min, double[] max) {
        initBasenGrid(min, max, new String[] { LINEAR, LINEAR }, new String[] { "X", "Y" });
    }

        // the default grid is confined to (0, 1) X (0, 1)
    public void initBasenGrid() {
        initBasenGrid(new double[] { 0, 0 }, new double[] { 1, 1 });
    }

        // convert an one-dimensional array to 2-D, where the first dimension simply indexes the second
    private static double[][] convertY(double[] XY) {
        double[] x = increment(XY.length, 1, 1);
        return mergeColumns(x, XY);
    }

    private static double[][] convertXY(double[]... XY) {
        if (XY.length == 2 && XY[0].length != 2)
	return mergeColumns(XY[0], XY[1]);
        else
	return XY;
    }

    // add plot components on the 2D Canvas
    public int addScatterPlot(String name, Color c, double[] Y) {
	return addPlot(new ScatterPlot(name, c,  convertY(Y)));
    }
	
    public int addScatterPlot(String name, Color c, double[][] XY) {
	return addPlot(new ScatterPlot(name, c, convertXY(XY)));
    }
	
    public int addScatterPlot(String name, Color c, double[] X, double[] Y) {
    	return addPlot(new ScatterPlot(name, c, convertXY(X,Y)));
    }

    public int addRealTimePlot(String name, Color c, double[] Y) {
	return addPlot(new RealTimePlot(name, c,  convertY(Y)));
    }
	
    public int addRealTimePlot(String name, Color c, double[][] XY) {
    	return addPlot(new RealTimePlot(name, c, convertXY(XY)));
    }
	
    public int addRealTimePlot(String name, Color c, double[] X, double[] Y) {
	return addPlot(new RealTimePlot(name, c, convertXY(X,Y)));
    }


      public int addMarkedLinePlot(String name, Color c, double [] X, double [] Y) {
            return addPlot(new LineMarksPlot(name, c, convertXY(X,Y)));
        }

        public int addMarkedLinePlot(String name, Color c, Font f, char mark, double [] X, double [] Y) {
            return addPlot(new LineMarksPlot(name, c, f, mark, convertXY(X,Y)));
        }

        public int addMarkedPlot(String name, Color c, double [] X, double [] Y) {
            return addPlot(new MarksPlot(name, c, convertXY(X,Y)));
        }

        public int addMarkedPlot(String name, Color c, Font f, char mark, double [] X, double [] Y) {
            return addPlot(new MarksPlot(name, c, f, mark, convertXY(X,Y)));
        }

        public int addMarkedPlot(String name, Color c, Font f, char mark, int _skipPoints, double [] X, double [] Y) {
            return addPlot(new MarksPlot(name, c, f, mark, _skipPoints, convertXY(X,Y)));
        }

        public int addLinePlot(String name, Color c, double[] Y) {
            return addPlot(new LinePlot(name, c, convertY(Y)));
        }
	
        public int addLinePlot(String name, Color c, int lineType, double[] Y) {
            return addPlot(new LinePlot(name, c, lineType,  1,  convertY(Y)));
        }
	
        public int addLinePlot(String name, Color c, int lineType, double[][] XY) {
            return addPlot(new LinePlot(name, c, lineType,  convertXY(XY)));
        }

        public int addLinePlot(String name, Color c, int lineType, int lineWidth, double[][] XY) {
            return addPlot(new LinePlot(name, c, lineType,  convertXY(XY), lineWidth));
        }

        public int addLinePlot(String name, Color c, double[][] XY) {
            return addPlot(new LinePlot(name, c, convertXY(XY)));
        }

        public int addLinePlot(String name, Color c, double[][] XY, int lineWidth) {
            return addPlot(new LinePlot(name, c, convertXY(XY), lineWidth));
        }

        public int addLinePlot(String name, Color c, boolean dotted, double[][] XY) {
            return addPlot(new LinePlot(name, c, convertXY(XY), dotted));
        }
	
        public int addLinePlot(String name, Color c, double[] X, double[] Y) {
            return addPlot(new LinePlot(name, c, convertXY(X,Y)));
        }

        public int addBarPlot(String name, Color c, double[] Y) {
            return addPlot(new BarPlot(name, c, convertY(Y)));
        }
	
        public int addBarPlot(String name, Color c, double[][] XY) {
            return addPlot(new BarPlot(name, c, convertXY(XY)));
        }
	
        public int addBarPlot(String name, Color c, double[] X, double[] Y) {
            return addPlot(new BarPlot(name, c, convertXY(X,Y)));
        }

        public int addStaircasePlot(String name, Color c, double[] Y) {
            return addPlot(new StaircasePlot(name, c, convertY(Y)));
        }
	
        public int addStaircasePlot(String name, Color c, double[][] XY) {
            return addPlot(new StaircasePlot(name, c, convertXY(XY)));
        }
	
        public int addStaircasePlot(String name, Color c, double[] X, double[] Y) {
            return addPlot(new StaircasePlot(name, c, convertXY(X,Y)));
        }
	

        public int addBoxPlot(String name, Color c, double[][] XY, double[][] dX) {
            return addPlot(new BoxPlot2D(XY, dX, c, name));
        }

        public int addBoxPlot(String name, Color c, double[][] XYdX) {
            return addPlot(new BoxPlot2D(getColumnsRangeCopy(XYdX, 0, 1), getColumnsRangeCopy(XYdX, 2, 3), c, name));
        }

        public int addHistogramPlot(String name, Color c, double[][] XY, double[] dX) {
            return addPlot(new HistogramPlot2D(name, c, XY, dX));
        }

        public int addHistogramPlot(String name, Color c, double[][] XY, double dX) {
            return addPlot(new HistogramPlot2D(name, c, XY, dX));
        }

        public int addHistogramPlot(String name, Color c, double[][] XYdX) {
            return addPlot(new HistogramPlot2D(name, c, getColumnsRangeCopy(XYdX, 0, 1), getColumnCopy(XYdX, 2)));
        }

        public int addHistogramPlot(String name, Color c, double[] X, int n) {
	double[][] XY = histogram_classes(X, n);
	return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
        }

        public int addHistogramPlot(String name, Color c, double[] X, double... bounds) {
	double[][] XY = histogram_classes(X, bounds);
	return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
        }

        public int addHistogramPlot(String name, Color c, double[] X, double min, double max, int n) {
	double[][] XY = histogram_classes(X, min, max, n);
	return addPlot(new HistogramPlot2D(name, c, XY, XY[1][0] - XY[0][0]));
            }

        public int addCloudPlot(String name, Color c, double[][] sampleXY, int nX, int nY) {
	double[][] XYh = histogram_classes_2D(sampleXY, nX, nY);
	return addPlot(new CloudPlot2D(name, c, XYh, XYh[1][0] - XYh[0][0], XYh[nX][1] - XYh[0][1]));
            }
	
 	
        public int addContourPlot(String name, Color c, double[][] XY) {
	return addPlot(new contourPlot(name, c, XY));
            }

        	
        public int addScalogramPlot(String name, Color c, double[][] XY) {
	return addPlot(new scalogram(name, c, XY));
	}
	
 

    
 	
	public static void main(String[] args) {
		
	/*	 Plot2DPanel p2d = new Plot2DPanel(DoubleArray.random(10, 2), "plot 1", PlotPanel.SCATTER); 
                 new FrameView(p2d);
		 p2d.addPlot(DoubleArray.random(10, 2), "plot 2", PlotPanel.SCATTER);
		 p2d.grid.getAxe(0).darkLabel.setCorner(0.5, -10);
		 p2d.grid.getAxe(1).darkLabel.setCorner(0, -0.5);
	*/	 
	}
}