package scalaSci.math.plot.plotObjects;

import java.awt.*;

import javax.swing.JLabel;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import scalaExec.Interpreter.GlobalValues;
import scalaSci.math.plot.PlotGlobals;
import scalaSci.math.plot.render.*;


public class texLabel implements Plotable {
    protected int  coordx, coordy;
    protected double logicalx, logicaly;  // logical coordinates, if specified
    protected boolean useLogical = true;  // use logical coordinates
    protected double[] base_offset;
    protected String label;
    protected Color plotColor = PlotGlobals.latexColor;
    protected double cornerN = 0.5;
    protected double cornerE = 0.5;
    boolean visible = true;
    double angle;  
    int  size = 20;


    public Font font = AbstractDrawer.DEFAULT_FONT;

    public texLabel(String l, int cx, int cy) {
           label = l;    coordx = cx;  coordy = cy;  
           useLogical = false;
    }


    public texLabel(String l, int Size, int  cx, int cy) {
            this(l, cx, cy);
            size = Size;
            useLogical = false;
     }

        
    public texLabel(String l, Font f,  int  cx, int cy) {
                label = l; 	coordx = cx;    coordy = cy; font = f;
                useLogical = false;
}


    public texLabel(String l, double  cx,  double  cy) {
           label = l;     
           logicalx = cx;   logicaly = cy; 
           useLogical = true;
    }

    public texLabel(String l, int Size, double  cx, double  cy) {
            this(l, cx, cy);
            size = Size; 
     }


    public texLabel(String l, Font f,  double cx, double  cy) {
                label = l;  logicalx = cx;   logicaly = cy;  font = f;
                useLogical = true;
}


    public void setText(String _t) {  label = _t;  	}

    public String getText() { 	return label;  	}

    public void setCoord(int _cx, int _cy) {  coordx = _cx; coordy = _cy;	}

    public void setColor(Color c) {  plotColor = c; 	}

    public Color getColor() {  return plotColor;	}

	/**
	 * reference point center: 0.5, 0.5 lowerleft: 0,0 upperleft 1, 0 ...
	 */
	public void setCorner(double north_south, double east_west) {
		cornerN = north_south;
		cornerE = east_west;
	}

	public void setVisible(boolean v) {	visible = v; 	}

	public boolean getVisible() { return visible; 	}

	/**
	 * shift by given screen coordinates offset
	 */
	/*
	 * public void setOffset(double[] offset) { double[] newCoord =
	 * coord.getPlotCoordCopy(); for (int i = 0; i < newCoord.length; i++) {
	 * newCoord[i] += offset[i]; } coord.setPlotCoord(newCoord); }
	 */

	/**
	 * see Text for formatted text output
	 */
	public void plot(AbstractDrawer  draw) {
            	    if (!visible) return;
		
                            TeXFormula formula = new TeXFormula(label);
                            
                            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
                            icon.setTeXIconColor(plotColor);
                            //icon.setInsets(new Insets(5, 5, 5, 5));

                            Graphics2D g2 = draw.getGraphics();
                            g2.setColor(plotColor);
                            
                            JLabel jl = new JLabel();
                            
                             jl.setForeground(plotColor);  // GlobalValues.defaultFormulaColor);
                            if (useLogical == false)   // do not use logical coordinates
                              icon.paintIcon(jl, g2, coordx, coordy);
                            else  // use logical coordinates to place the formula
                            {
                                double [] logicalCoords = { logicalx, logicaly};
                                int [] screenCoords = draw.project(logicalCoords[0], logicalCoords[1]);
                                icon.paintIcon(jl, g2, screenCoords[0], screenCoords[1]);
                                

	}
        }

	public void rotate(double _angle) { 	angle = _angle;  }

	public void setFont(Font _font) {  	font = _font; 	}

	public static double approx(double val, int decimal) {
		// double timesEn = val*Math.pow(10,decimal);
		// if (Math.rint(timesEn) == timesEn) {
		// return val;
		// } else {
		// to limit precision loss, you need to separate cases where decimal<0
		// and >0
		// if you don't you'll have this : approx(10000.0,-4) => 10000.00000001
		if (decimal < 0) {
			return Math.rint(val / Math.pow(10, -decimal)) * Math.pow(10, -decimal);
		} else {
			return Math.rint(val * Math.pow(10, decimal)) / Math.pow(10, decimal);
		}
		// }
	}

	public static String coordToString(int... c) {
		StringBuffer sb = new StringBuffer("(");
		for (int i = 0; i < c.length; i++)
			sb.append(c[i]).append(",");
		// sb.append(dec.format(c.getPlotCoordCopy()[i])).append(",");

		sb.setLength(sb.length() - 1);
		if (sb.length() > 0)
			sb.append(")");

		return sb.toString();
	}


public Font getFont() {
		return font;
	}
}


