package scalaSci.math.plot.plotObjects;


import java.awt.*;

import scalaSci.math.plot.render.*;

public class DLabel implements Plotable {
    protected int [] coord;
    protected double[] base_offset;
    protected String label;
    protected Color color;
    protected double cornerN = 0.5;
    protected double cornerE = 0.5;
    boolean visible = true;
    public double angle = 0;
    public Font font = AbstractDrawer.DEFAULT_FONT;
    
    public DLabel(String l, Color col, int... c) {
        label = l;    coord = c; 	color = col;
    }
    public DLabel(String l, int... c) {
        this(l, AbstractDrawer.DEFAULT_COLOR, c);
    }
/** show coord itself	 */
    public DLabel(int... c) {
        this(coordToString(c), AbstractDrawer.DEFAULT_COLOR, c);
    }
   public DLabel(String l, Color col, Font f,  int... c) {
        label = l; 	coord = c;	color = col; font = f;
    }
   public DLabel(String l, Font f, int... c) {
        this(l, AbstractDrawer.DEFAULT_COLOR, f, c);
    }
    public DLabel(Font f, int ... c) {
        this(coordToString(c), AbstractDrawer.DEFAULT_COLOR, f, c);
    }
    public void setText(String _t) {  label = _t;  	}
    public String getText() { 	return label;  	}
    public void setCoord(int... _c) {  coord = _c;	}
    public void setColor(Color c) {  color = c; 	}
    public Color getColor() {  return color;	}

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
        draw.setColor(color);
        draw.setFont(font);
        draw.setBaseOffset(base_offset);
        draw.setTextOffset(cornerE, cornerN);
        draw.setTextAngle(angle);
        draw.drawString(label, coord[0], coord[1]);
        draw.setBaseOffset(null);
    }

    public void rotate(double _angle) { 	angle = _angle;  }

    public void setFont(Font _font) {  	font = _font; 	}

    public static double approx(double val, int decimal) {
        if (decimal < 0) {
            return Math.rint(val / Math.pow(10, -decimal)) * Math.pow(10, -decimal);
	} else {
            return Math.rint(val * Math.pow(10, decimal)) / Math.pow(10, decimal);
	}
}

    public static String coordToString(int... c) {
        StringBuffer sb = new StringBuffer("(");
        for (int i = 0; i < c.length; i++)
            sb.append(c[i]).append(",");
        sb.setLength(sb.length() - 1);
        if (sb.length() > 0)
            sb.append(")");
        return sb.toString();
    }


public Font getFont() {
        return font;
    }
}
