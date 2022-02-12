package scalaSci.math.plot.render;

import scalaSci.math.plot.canvas.*;

public class AWTDrawer2D extends AWTDrawer {

	public AWTDrawer2D(PlotCanvas _canvas) {
		super(_canvas);
		projection = new Projection2D(this);
	}

}
