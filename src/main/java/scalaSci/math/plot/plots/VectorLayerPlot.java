package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;
import scalaSci.math.plot.utils.*;

/**Layer to add a vector field to an existing Plot*/
public class VectorLayerPlot extends LayerPlot {
    public static int RADIUS = 5;
    double[][] V;
    /**Create a vector field based on data of a plot
      @param p Base plot to support vector field
      @param v Vector field of same length that p data */
    public VectorLayerPlot(Plot p, double[][] v) {
        super("Vector of " + p.name, p);
        if (v != null) {
    PArray.checkRowDimension(v, p.getData().length);
	PArray.checkColumnDimension(v, p.getData()[0].length);
	}
    V = v;
}

@Override
public void setData(double[][] v) {
    V = v;
}

@Override
public double[][] getData() {
    return V;
}

public void plot(AbstractDrawer draw, Color c) {
        double [] dArrow = new double[2];  // holds each arrow line
        double phi = Math.PI/6;    // the angle of the arrow
        double arrowSizeRelativeToPlotSize = 0.1;
        if (!plot.visible)
	return;
    draw.setColor(c);
    draw.setLineType(AbstractDrawer.CONTINOUS_LINE);

    double mx=0.0;     double mn = 0.0;   // used to compute the relative length of the arrow
    double [][] pd = plot.getData();
	for (int i = 0; i < pd.length; i++) {
		double[] d = PArray.getRowCopy(pd, i);
			for (int j = 0; j < d.length; j++) {
                double cElem = V[i][j];
                if ( cElem > mx)  mx = cElem;
                if ( cElem < mn)  mn = cElem;
				d[j] += cElem;
			}

        double arrowLength = (mx-mn)*arrowSizeRelativeToPlotSize;
        
         	draw.drawLine(pd[i], d);
         double tanTheta = (d[1]-pd[i][1]) / (d[0]-pd[i][0]);
         double theta = Math.atan(tanTheta);

         double arrowAngleUp = theta + Math.PI - phi;
         double arrowAngleDown  = Math.PI + theta + phi;
         double xdUp = d[0] + arrowLength * Math.cos(arrowAngleUp);
         double ydUp = d[1] + arrowLength * Math.sin(arrowAngleUp);
         double xdDown = d[0] + arrowLength * Math.cos(arrowAngleDown);
         double ydDown = d[1] + arrowLength * Math.sin(arrowAngleDown);
         draw.setColor(c);
         dArrow[0] = xdUp;  dArrow[1] = ydUp;
         draw.drawLine(d, dArrow);
         dArrow[0] = xdDown;  dArrow[1] = ydDown;
         draw.drawLine(d, dArrow);
         draw.setColor(c);
         draw.drawLine(pd);

 
		}
  
	}  

	public static void main(String[] args) {
		Plot2DPanel p2 = new Plot2DPanel();
		double[][] XYZ = new double[100][2];
		double[][] dXYZ = new double[100][2];

		for (int j = 0; j < XYZ.length; j++) {
			XYZ[j][0] = Math.random()*10;
			XYZ[j][1] = Math.random()*10;
			dXYZ[j][0] = 1.0/Math.sqrt(1+Math.log(XYZ[j][0])*Math.log(XYZ[j][0]));
			dXYZ[j][1] = Math.log(XYZ[j][0])/Math.sqrt(1+Math.log(XYZ[j][0])*Math.log(XYZ[j][0]));
		}
		p2.addScatterPlot("toto", XYZ);

		p2.addVectortoPlot(0, dXYZ);
		new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}