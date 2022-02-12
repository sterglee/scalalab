package scalaSci.math.plot.plotObjects;

import java.awt.*;

import scalaSci.math.plot.render.*;


public interface Plotable {
    public void plot(AbstractDrawer draw);
    public void setVisible(boolean v);
    public boolean getVisible();
    public void setColor(Color c);
    public Color getColor();

}