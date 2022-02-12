package scalaSci.math.plot.plotObjects;

import scalaSci.math.plot.render.*;


public interface Noteable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void note(AbstractDrawer draw);
}