package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

// display in scatter plot style
public class RealTimePlot extends Plot {
    public  int type;
    private int radius;
    private boolean[][] pattern;
    private boolean use_pattern;
    double[][] XY;
    double [][] XYbuffer;
    public static int bufLen = 10;
    public static int currentNumPointsToPlot;

    public RealTimePlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
        super(n, c);
        currentNumPointsToPlot = _XY[0].length;
        XY = _XY;
        int xlen = XY.length;
        int ylen = XY[0].length;
        use_pattern = true;
        pattern = _pattern;
}

     public RealTimePlot( String n,  Color c,  boolean[][] _pattern,  double[]  _X,   double [] _Y)  {
            super(n,c); 
             
           currentNumPointsToPlot = _X.length;
            
           int len =  _X.length;
           currentNumPointsToPlot = len;
          double [][] XYc = new double[2][_X.length+bufLen];
         for (int col=0; col<len; col++) {
            XYc[0][col] = _X[col];
            XYc[1][col] = _Y[col];
        }
          XY = XYc;
          use_pattern = true;
          pattern = _pattern;
}

    public RealTimePlot(String n, Color c, int _type, int _radius, double[][] _XY) {
        super(n, c);
        
        int len =  _XY.length;
        currentNumPointsToPlot = len;
        double [][] XYc = new double[len+bufLen][2];
        for (int col=0; col<len; col++) {
            XYc[col][0] = _XY[col][0];
            XYc[col][1] = _XY[col][1];
        }
        
        XY = _XY;
        use_pattern = false;
        type = _type;
        radius = _radius;
}

    public RealTimePlot(String n, Color c, int _type, double[][] _XY) {
        super(n, c);
        int len =  _XY.length;
        currentNumPointsToPlot = len;
        double [][] XYc = new double[len+bufLen][2];
        for (int col=0; col<len; col++) {
            XYc[col][0] = _XY[col][0];
            XYc[col][1] = _XY[col][1];
        }
        
        XY = _XY;
        
        use_pattern = false;
        type = _type;
        radius = 1;
}

    public RealTimePlot(String n, Color c, double[][] _XY) {
        this(n, c, AbstractDrawer.ROUND_DOT, AbstractDrawer.DEFAULT_DOT_RADIUS, _XY);
}

    public void plot(AbstractDrawer draw, Color c) {
        if (!visible)
            return;

        draw.setColor(c);
        if (use_pattern) {
	draw.setDotType(AbstractDrawer.PATTERN_DOT);
	draw.setDotPattern(pattern);
            } else {
	draw.setDotRadius(radius);
	if (type == AbstractDrawer.CROSS_DOT)
                        draw.setDotType(AbstractDrawer.CROSS_DOT);
                  else if (type == AbstractDrawer.THICK_LINE)
                        draw.setLineType(type);
	else
                        draw.setDotType(AbstractDrawer.ROUND_DOT);
	}

	for (int i = 0; i < currentNumPointsToPlot; i++)
                        draw.drawDot(XY[i]);
}

    public void setDotPattern(int t) {
        type = t;
        use_pattern = false;
    }

    public void setDotPattern(boolean[][] t) {
        use_pattern = true;
        pattern = t;
    }

    @Override
    public void setData(double[][] d) {
        XY = d;
    }

    @Override
    public double[][] getData() {
        return XY;
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
        for (int i = 0; i < XY.length; i++) {
            int[] screenCoord = draw.project(XY[i]);

            if ((screenCoord[0] + note_precision > screenCoordTest[0]) && (screenCoord[0] - note_precision < screenCoordTest[0])
	&& (screenCoord[1] + note_precision > screenCoordTest[1]) && (screenCoord[1] - note_precision < screenCoordTest[1]))
            return XY[i];
        }
    return null;
    }

	public static void main(String[] args) {
		Plot2DPanel p2 = new Plot2DPanel();
		for (int i = 0; i < 3; i++) {
			double[][] XYZ = new double[10][2];
			for (int j = 0; j < XYZ.length; j++) {
				XYZ[j][0] = /*1 + */Math.random();
				XYZ[j][1] = /*100 * */Math.random();
			}
			p2.addScatterPlot("toto" + i, XYZ);
		}

		p2.setLegendOrientation(PlotPanel.SOUTH);
		new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Plot3DPanel p = new Plot3DPanel();
		for (int i = 0; i < 3; i++) {
			double[][] XYZ = new double[10][3];
			for (int j = 0; j < XYZ.length; j++) {
				XYZ[j][0] = /*1 +*/Math.random();
				XYZ[j][1] = /*100 **/Math.random();
				XYZ[j][2] = /*0.0001 **/Math.random();
			}
			p.addScatterPlot("toto" + i, XYZ);
		}

		p.setLegendOrientation(PlotPanel.SOUTH);
		new FrameView(p).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}