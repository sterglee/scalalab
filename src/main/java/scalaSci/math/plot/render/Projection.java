
package scalaSci.math.plot.render;

import scalaSci.math.plot.plotObjects.*;

public abstract class Projection {

    int[][] baseScreenCoords;
    public static double DEFAULT_BORDER = 0.15;
    protected double borderCoeff = DEFAULT_BORDER;
    protected AWTDrawer draw;

    public Projection(AWTDrawer _draw) {
        draw = _draw;
}

    protected void initBaseCoordsProjection() {
        baseScreenCoords = new int[draw.canvas.base.baseCoords.length][2];
	for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
                    double[] ratio = baseCoordsScreenProjectionRatio(draw.canvas.base.baseCoords[i]);
                    baseScreenCoords[i][0] = (int) (draw.canvas.getWidth() * (borderCoeff + (1 - 2 * borderCoeff) * ratio[0]));
                    baseScreenCoords[i][1] = (int) (draw.canvas.getHeight() - draw.canvas.getHeight() * (borderCoeff + (1 - 2 * borderCoeff) * ratio[1]));
	}
}

	// ///////////////////////////////////////////
	// ////// move methods ///////////////////////
	// ///////////////////////////////////////////

    public void translate(int[] screenTranslation) {
        for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
            baseScreenCoords[i][0] = baseScreenCoords[i][0] + screenTranslation[0];
            baseScreenCoords[i][1] = baseScreenCoords[i][1] + screenTranslation[1];
	}
}

    public void dilate(int[] screenOrigin, double[] screenRatio) {
        for (int i = 0; i < draw.canvas.base.dimension + 1; i++) {
            baseScreenCoords[i][0] = (int) ((baseScreenCoords[i][0] - screenOrigin[0]) / screenRatio[0]);
            baseScreenCoords[i][1] = (int) ((baseScreenCoords[i][1] - screenOrigin[1]) / screenRatio[1]);
	}
}

	// ///////////////////////////////////////////
	// ////// projection method //////////////////
	// ///////////////////////////////////////////
    public int[] screenProjection(double... pC) {
        double[] sC = new double[2];
                // baseScreenCoords[0]: screen SW coordinates, baseScreenCoords[1]: screen  SE coordinates, baseScreenCoord[2]: screen  NW coordinates
        sC[0] = baseScreenCoords[0][0];    
        sC[1] = baseScreenCoords[0][1];    
  
         // baseCoords[0]: logical SW coordinates, baseCoords[1]: logical SE coordinates, baseCoords[2]: logical NW coordinates
 for (int i = 0; i < draw.canvas.base.dimension; i++) {
    double bc0_0 = draw.canvas.base.baseCoords[0][0];  // x logical SW
    double bc0_i = draw.canvas.base.baseCoords[0][i];    // 
    double bci1_i = draw.canvas.base.baseCoords[i+1][i];
    double bci1_1 = draw.canvas.base.baseCoords[i+1][1];
    double bc0_1 = draw.canvas.base.baseCoords[0][1];
    double bci1_0 = draw.canvas.base.baseCoords[i+1][0];
            
 
     if (draw.canvas.base.axesScales[i].equalsIgnoreCase(Base.LOGARITHM)) {
         double factor = ((log(pC[i]) - log(bc0_i)) / (log(bci1_i) - log(bc0_i)));
 sC[0] += factor * (baseScreenCoords[i+1][0] - baseScreenCoords[0][0]);
 sC[1] += factor * (baseScreenCoords[i+1][1] - baseScreenCoords[0][1]);
			} 
     else if (draw.canvas.base.axesScales[i].equalsIgnoreCase(Base.LINEAR)||draw.canvas.base.axesScales[i].equalsIgnoreCase(Base.STRINGS)) {
double factor = ((pC[i] -bc0_i) / (bci1_i - bc0_i));
sC[0] += factor * (baseScreenCoords[i+1][0] - baseScreenCoords[0][0]);
sC[1] += factor * (baseScreenCoords[i+1][1] - baseScreenCoords[0][1]);
			}
    }

    if (draw.base_offset != null) {
        for (int i = 0; i < draw.canvas.base.dimension; i++) {
	sC[0] += draw.base_offset[i] * (baseScreenCoords[i+1][0] - baseScreenCoords[0][0]);
	sC[1] += draw.base_offset[i] * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
	}
  }
		
    if (draw.screen_offset != null) {
        sC[0] += draw.screen_offset[0];
        sC[1] += draw.screen_offset[1];
        }

    return new int[] { (int) sC[0], (int) sC[1] };
}

public int[] screenProjectionBaseRatio(double... rC) {
    double[] sC = new double[2];
    sC[0] = baseScreenCoords[0][0];
    sC[1] = baseScreenCoords[0][1];
    for (int i = 0; i < draw.canvas.base.dimension; i++) {
        sC[0] += rC[i] * (baseScreenCoords[i + 1][0] - baseScreenCoords[0][0]);
        sC[1] += rC[i] * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
	}
		
    if (draw.base_offset != null) {
        for (int i = 0; i < draw.canvas.base.dimension; i++) {
	sC[0] += draw.base_offset[i] * (baseScreenCoords[i + 1][0] - baseScreenCoords[0][0]);
	sC[1] += draw.base_offset[i] * (baseScreenCoords[i + 1][1] - baseScreenCoords[0][1]);
    }
}
		
if (draw.screen_offset != null) {
    sC[0] += draw.screen_offset[0];
    sC[1] += draw.screen_offset[1];
 }
		
return new int[] { (int) sC[0], (int) sC[1] };
}

private double log(double x) {
    return Math.log(x);
}

protected abstract double[] baseCoordsScreenProjectionRatio(double[] xyz);

}

