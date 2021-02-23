package scalaSci.math.plot.plotObjects;

import java.awt.*;

import scalaSci.math.plot.render.*;

  // implements a line object
public class Line implements Plotable {
    protected double[][] extrem = new double[2][];   // the two extreme points of the line, for defining the gradient etc.
    protected Color color;
    protected Color gradientColor;
    boolean visible = true;

    public Line(Color col, double[] c1, double[] c2) {
        extrem[0] = c1;   // first line's extreme point
        extrem[1] = c2;   // second line's extreme point
        color = col;
        }

    public void setColor(Color c) {
        color = c;
        }

    public Color getColor() {
        return color;
        }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean getVisible() {
        return visible;
    }

 public void plot(AbstractDrawer draw) {
    if (!visible)   return;
    draw.setColor(color);  // set the color of the line
    if (gradientColor!= null)  // set the gradient for line displaying
        draw.setGradient(extrem[0], color, extrem[1], gradientColor);
    draw.drawLine(extrem[0], extrem[1]);
    if (gradientColor!= null)
          draw.resetGradient();
}

public Color getGradientColor() {
    return gradientColor;
}

public void setGradientColor(Color c) {
    this.gradientColor = c;
 }
}


