package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;

import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class LinePlot extends ScatterPlot {

        public boolean draw_dot = false;
        public int plotType= AbstractDrawer.CONTINOUS_LINE;
        public int lineWidth = AbstractDrawer.line_width;

        public LinePlot(String n, Color c, boolean[][] _pattern, double[][] _XY ) { 
	super(n, c, _pattern, _XY);
       }

        public LinePlot( String n,  Color c,  boolean[][] _pattern,  double[]  _X,   double [] _Y)  {
                super(n, c, _pattern, _X, _Y);
	}

        public LinePlot(String n, Color c, int _type, int _radius, double[][] _XY) {
                super(n, c, _type, _radius, _XY);
	}

        public LinePlot(String n, Color c, double[][] _XY) {
                super(n, c, _XY);
	}

        public LinePlot(String n, Color c, double[][] _XY, int _lineWidth) {
                super(n, c, _XY);
                lineWidth = _lineWidth;
	}

        public LinePlot(String n, Color c, int _plotType, double[][] _XY) {
                super(n, c, _XY);
                plotType = _plotType;
	}

       public LinePlot(String n, Color c, int _plotType,  double[][] _XY, int  _lineWidth) {
                super(n, c, _XY);
                plotType = _plotType;
                lineWidth = _lineWidth;
	}

       public LinePlot(String n, Color c, double[][] _XY, boolean drawDots) {
            super(n, c, _XY);
            draw_dot = drawDots;
        }
	
       
    public void plot(AbstractDrawer draw, Color c) {
            if (!visible)
                return;

                draw.setLineType(plotType);

                draw.setLineWidth(lineWidth);
                draw.getGraphics().setStroke(new BasicStroke((float)lineWidth));

                if (draw_dot)
	super.plot(draw, c);
                else {
                  draw.setColor(c);
     
                  int xyl = XY.length;
                    if (xyl < PlotGlobals.limitPlotPoints  || PlotGlobals.skipPointsOnPlot==false) {
         // plot all the points               
                for (int i = 0; i < XY.length - 1; i++)
	draw.drawLine(XY[i], XY[i + 1]);
                }
                else {
     int skipPoints =  ((int)(xyl/PlotGlobals.limitPlotPoints)) + 1;
     for (int i = 0; i < XY.length - 1-skipPoints-1; i+=skipPoints)
        draw.drawLine(XY[i], XY[i + skipPoints+1]);
                  }
     
}
  }
    

  
public static void main(String[] args) {
		Plot2DPanel p2 = new Plot2DPanel();
                

			double[][] XYZ = new double[100][2];
			for (int j = 0; j < XYZ.length; j++) {
				XYZ[j][0] = 2*Math.PI*(double)j/XYZ.length;
				XYZ[j][1] = Math.sin(XYZ[j][0]);
			}
                        double[][] XYZ2 = new double[100][2];
			for (int j = 0; j < XYZ2.length; j++) {
				XYZ2[j][0] = 2*Math.PI*(double)j/XYZ.length;
				XYZ2[j][1] = Math.sin(2*XYZ2[j][0]);
			}
	
			p2.addLinePlot("sin" , Color.RED, AbstractDrawer.DOTTED_LINE, XYZ);
                        p2.setBackground(new Color(200,0,0));
                        p2.addLinePlot("sin2", Color.BLUE, AbstractDrawer.CROSS_DOT, XYZ2);
		

		p2.setLegendOrientation(PlotPanel.SOUTH);
		new FrameView(p2).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		Plot3DPanel p = new Plot3DPanel();

			XYZ = new double[100][3];
			for (int j = 0; j < XYZ.length; j++) {
				XYZ[j][0] = 2*Math.PI*(double)j/XYZ.length;
				XYZ[j][1] = Math.sin(XYZ[j][0]);
				XYZ[j][2] = Math.sin(XYZ[j][0])*Math.cos(XYZ[j][1]);
			}
			p.addLinePlot("toto" , XYZ);
                        
		
		p.setLegendOrientation(PlotPanel.SOUTH);
		new FrameView(p).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}