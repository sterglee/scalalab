package scalaSci.math.plot.render;

public class Projection3D extends Projection {
    protected double theta;
    protected double phi;

    public Projection3D(AWTDrawer _draw) {
        super(_draw);
        theta = Math.PI / 4;
        phi = Math.PI / 4;
        initBaseCoordsProjection();
}

    protected double[] baseCoordsScreenProjectionRatio(double[] xyz) {
        double factor = 1.7;
        double[] sC = new double[2];
        sC[0] = 0.5+ (cos(theta)* ((xyz[1] - (draw.canvas.base.roundXmax[1] + draw.canvas.base.roundXmin[1]) / 2) / (draw.canvas.base.roundXmax[1] - draw.canvas.base.roundXmin[1])) - sin(theta)
	* ((xyz[0] - (draw.canvas.base.roundXmax[0] + draw.canvas.base.roundXmin[0]) / 2) / (draw.canvas.base.roundXmax[0] - draw.canvas.base.roundXmin[0])))
	/ factor;
        sC[1] = 0.5+ (cos(phi)* ((xyz[2] - (draw.canvas.base.roundXmax[2] + draw.canvas.base.roundXmin[2]) / 2) / (draw.canvas.base.roundXmax[2] - draw.canvas.base.roundXmin[2]))
	- sin(phi)* cos(theta)	* ((xyz[0] - (draw.canvas.base.roundXmax[0] + draw.canvas.base.roundXmin[0]) / 2) / (draw.canvas.base.roundXmax[0] - draw.canvas.base.roundXmin[0])) - sin(phi)
	* sin(theta)	* ((xyz[1] - (draw.canvas.base.roundXmax[1] + draw.canvas.base.roundXmin[1]) / 2) / (draw.canvas.base.roundXmax[1] - draw.canvas.base.roundXmin[1])))
		/ factor;
	return sC;
}

    private double cos(double x) {
        return Math.cos(x);
}

    private double sin(double x) {
        return Math.sin(x);
}

public void setView(double _theta, double _phi) {
        theta = _theta;
        phi = _phi;
        initBaseCoordsProjection();
}

public void rotate(int[] screenTranslation, int[] dimension) {
    theta = theta - ((double) screenTranslation[0]) / 100;
    phi = phi + ((double) screenTranslation[1]) / 100;
    initBaseCoordsProjection();
}

}

