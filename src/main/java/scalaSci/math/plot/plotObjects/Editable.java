package scalaSci.math.plot.plotObjects;

import scalaSci.math.plot.render.*;


public interface Editable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void edit(Object editParent);
    public void editnote(AbstractDrawer draw);
}
