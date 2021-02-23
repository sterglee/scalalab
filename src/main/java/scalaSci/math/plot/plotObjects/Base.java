package scalaSci.math.plot.plotObjects;

import static java.lang.Math.*;

//  defines basic parameters of the axis base
public class Base {
    public final static String STRINGS = "str";
    public final static String LINEAR = "lin";
    public final static String LOGARITHM = "log";
    public double[][] baseCoords;   // logical coordinates of the base 
    protected double[] precisionUnit;
    public double[] roundXmin;
    public double[] roundXmax;
    protected double[] trueXmin;
    protected double[] trueXmax;
    public int dimension;
    public String[] axesScales;  // i.e. "linear" or "logarithmic"

    public Base(double[] Xmi, double[] Xma, String[] scales) {
        trueXmin = Xmi;
        trueXmax = Xma;
        dimension = trueXmin.length;
        axesScales = scales;
        init(trueXmin.length);
        setRoundBounds(trueXmin, trueXmax);
        resetCoords();
    }

    // init for a specified dimension d
    private void init(int d) {
        precisionUnit = new double[d];
        roundXmin = new double[d];
        roundXmax = new double[d];
        trueXmin = new double[d];
        trueXmax = new double[d];
    }

    
    private void resetCoords() {
        baseCoords = new double[dimension + 1][];  // base coords for each dimension
        for (int i = 0; i < baseCoords.length; i++) {
            baseCoords[i] = (double[]) (roundXmin.clone());
            if (i > 0)
                baseCoords[i][i - 1] = roundXmax[i - 1];
    }
}

 /*
 * protected void setPrecisionUnit(double[] Xmi,double[] Xma) {
 * precisionUnit = new double[Xmi.length]; for (int i = 0; i <
 * precisionUnit.length; i++) { setPrecisionUnit(Xmi[i],Xma[i], i); } }
 */
   private void setPrecisionUnit(int i, double Xmi, double Xma) {
       if (Xma - Xmi > 0) {
            precisionUnit[i] = pow(10, floor(log(Xma - Xmi) / log(10)));
       } else {
	precisionUnit[i] = 1;
            }
 }

    public void setAxesScales(String[] scales) {
        axesScales = scales;
        setRoundBounds(trueXmin, trueXmax);
        resetCoords();
    }

    public void setAxesScales(int i, String scale) {
        axesScales[i] = scale;
        setRoundBounds(trueXmin, trueXmax);
        resetCoords();
    }

    public double[][] getCoords() {
        return baseCoords;
    }

    public String[] getAxesScales() {
        return axesScales;
    }

    public String getAxeScale(int i) {
        return axesScales[i];
}

    public double[] getMinBounds() {
        return roundXmin;
}

    public double[] getMaxBounds() {
        return roundXmax;
 }

    public double[] getPrecisionUnit() {
        return precisionUnit;
    }

	// ///////////////////////////////////////////
	// ////// bounds methods /////////////////////
	// ///////////////////////////////////////////
    private void setBounds(int i, double Xmi, double Xma) {
        if ((Xmi <= 0) && (axesScales[i].equalsIgnoreCase(LOGARITHM))) {
	throw new IllegalArgumentException("Error while bounding dimension " + (i + 1) + " : bounds [" + Xmi + "," + Xma
		+ "] are incompatible with Logarithm scale.");
	}
        if (Xmi == Xma) {
	Xmi = Xma - 1;
	}
        if (Xmi > Xma) {
	throw new IllegalArgumentException("Error while bounding dimension " + (i + 1) + " : min " + Xmi + " must be < to max " + Xma);
	}
	roundXmin[i] = Xmi;
	roundXmax[i] = Xma;
	resetCoords();
    }

	/*
	 * private void setBounds(double[] Xmi, double[] Xma) { for (int i = 0; i <
	 * Xmi.length; i++) { setBounds(i, Xmi[i], Xma[i]); } }
	 */

    public void setFixedBounds(int i, double Xmi, double Xma) {
        setPrecisionUnit(i, Xmi, Xma);
        setBounds(i, Xmi, Xma);
    }

    public void setFixedBounds(double[] Xmi, double[] Xma) {
	for (int i = 0; i < Xmi.length; i++) {
                    setFixedBounds(i, Xmi[i], Xma[i]);
	}
 }

    public void roundBounds(int i) {
        setPrecisionUnit(i, trueXmin[i], trueXmax[i]);
        if (axesScales[i].equalsIgnoreCase(LOGARITHM)) {
	setBounds(i, pow(10, floor(log(trueXmin[i]) / log(10))), pow(10, ceil(log(trueXmax[i]) / log(10))));
        } else if (axesScales[i].equalsIgnoreCase(LINEAR)||axesScales[i].equalsIgnoreCase(STRINGS)) {
	setBounds(i, precisionUnit[i] * (floor(trueXmin[i] / precisionUnit[i])), precisionUnit[i] * (ceil(trueXmax[i] / precisionUnit[i])));
	}
}

    public void setRoundBounds(int i, double Xmi, double Xma) {
        trueXmin[i] = Xmi;
        trueXmax[i] = Xma;
        roundBounds(i);
}

    public void setRoundBounds(double[] Xmi, double[] Xma) {
        int xmiLen = Xmi.length;
        int up = xmiLen;
        if (trueXmin.length < up)
             up = trueXmin.length;
              
        for (int i = 0; i < up; i++) {
            trueXmin[i] = Xmi[i];
            trueXmax[i] = Xma[i];
            roundBounds(i);
    }
}

    // include the value XY within the bound of values displayed with this base
    public void includeInBounds(int dim, double XY) {
        for (int i = 0; i < roundXmin.length; i++) {
            if (i == dim)
	if (XY < trueXmin[i])
                    trueXmin[i] = XY;
            }
        for (int i = 0; i < roundXmax.length; i++) {
	if (i == dim)
                    if (XY > trueXmax[i])
                        trueXmax[i] = XY;
	}
    roundBounds(dim);
}

        // include the values of the array XY within the bounds of values displayed with this base
    public void includeInBounds(double[] XY) {
        for (int i = 0; i < roundXmin.length; i++) {
            if (XY[i] < trueXmin[i])
	trueXmin[i] = XY[i];
	}
            for (int i = 0; i < roundXmax.length; i++) {
	if (XY[i] > trueXmax[i])
                    trueXmax[i] = XY[i];
	}
	setRoundBounds(trueXmin, trueXmax);
}

	// ///////////////////////////////////////////
	// ////// other public methods ///////////////
	// ///////////////////////////////////////////

    public boolean authorizedLogScale(int i) {
        if (roundXmin[i] > 0) {
	return true;
        } else {
        return false;
    }
}

    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < baseCoords.length; i++) {
            s.append("[");
            for (int j = 0; j < baseCoords[i].length; j++)
	s.append(baseCoords[i][j] + ",");
	s.deleteCharAt(s.length() - 1);
	s.append("]");
	}
	return s.toString();
    }
}

