package scalaSci.math.plot.plots;

import java.awt.*;

import javax.swing.*;
import scalaExec.Interpreter.GlobalValues;

import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;
import scalaSci.math.plot.utils.*;

public class BarPlot extends ScatterPlot {
    public boolean draw_dot = true;
        public BarPlot(String n, Color c, boolean[][] _pattern, double[][] _XY) {
            super(n, c, _pattern, _XY);
        }
        public BarPlot(String n, Color c, int _type, int _radius, double[][] _XY) {
            super(n, c, _type, _radius, _XY);
        }
        public BarPlot(String n, Color c, double[][] _XY) {
            super(n, c, _XY);
        }

public void plot(AbstractDrawer draw, Color c) {
    if (!visible)
        return;
    if (draw_dot)
        super.plot(draw, c);

draw.setColor(c);
draw.setLineType(AbstractDrawer.CONTINOUS_LINE);
int xyl = XY.length;
if (xyl < PlotGlobals.limitPlotPoints  || PlotGlobals.skipPointsOnPlot==false) {
  // plot the whole               
for (int i = 0; i < XY.length; i++) {
    double[] axeprojection = PArray.copy(XY[i]);
    axeprojection[axeprojection.length - 1] = draw.canvas.base.baseCoords[0][axeprojection.length - 1];
    draw.drawLine(XY[i], axeprojection);
  }
 }
else {
    int skipPoints =  ((int)(xyl/PlotGlobals.limitPlotPoints)) + 1;
for (int i = 0; i < XY.length-skipPoints-1; i+=skipPoints) {
    double[] axeprojection = PArray.copy(XY[i]);
    axeprojection[axeprojection.length - 1] = draw.canvas.base.baseCoords[0][axeprojection.length - 1];
    draw.drawLine(XY[i], axeprojection);
  }

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
    p2.addBarPlot("toto" + i, XYZ);
   }
p2.setLegendOrientation(PlotPanel.SOUTH);
new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

Plot3DPanel p = new Plot3DPanel();
for (int i = 0; i < 3; i++) {
    double[][] XYZ = new double[10][3];
    for (int j = 0; j < XYZ.length; j++) {
	XYZ[j][0] = Math.random();
	XYZ[j][1] = Math.random();
	XYZ[j][2] = Math.random();
    }
p.addBarPlot("toto" + i, XYZ);
}

p.setLegendOrientation(PlotPanel.SOUTH);
new FrameView(p).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}