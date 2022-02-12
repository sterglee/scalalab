
package scalaSci.math.plot.plotObjects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class LatexImage implements Plotable{

    File source;
    BufferedImage img;
    double[] xyzSW, xyzSE,xyzNW;
    String latexStr;
    int xcoord, ycoord;   // the x-y coordinates of the image
    boolean plotAbsolute = false;
	
    boolean visible = true;
    float alpha;
	
    /*
     var t = inc(0, 0.01, 10); var x =cos(9.7*t); var y = sin(4.5*t);
     plot(t,x,y);
     var latex = """cos(9.7*x+sin(4.5*t)"""
     var len = t.length-1
     latexLabel3d(latex, 100, 100)
     // latexLabel3d(latex, t(len), x(len), y(len), t(0), x(0), y(0, t(0), x(0), y(0))
     */
    public LatexImage(String _latexStr, double nw1, double nw2, double nw3, double se1, double se2, double se3, double sw1, double sw2, double sw3) {
        plotAbsolute = false;
        xyzNW = new double[3]; xyzSE = new double[3]; xyzSW = new double[3]; 
        
        xyzNW[0] = nw1; xyzNW[1] = nw2;  xyzNW[2] = nw3; 
        xyzSE[0] = se1; xyzSE[1] = se2;  xyzSE[2] = se3;
        xyzSW[0] = sw1; xyzSW[1] = sw2;  xyzSW[2] = sw3;
        
        latexStr = _latexStr;
        
        TeXFormula formula = new TeXFormula(true, latexStr);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
        icon.setInsets(new Insets(5, 5, 5, 5));

        img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);

        alpha = (float)0.5;
      
}

    public LatexImage(String _latexStr, int x, int y) {
        plotAbsolute = true;
        xcoord = x;
        ycoord = y;
        latexStr = _latexStr;
        
        TeXFormula formula = new TeXFormula(true, latexStr);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
        icon.setInsets(new Insets(5, 5, 5, 5));

        img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);

        alpha = (float)0.5;
      
}

    @Override
    public void plot(AbstractDrawer draw) {
        if (!visible) return;
       if (plotAbsolute==false)
            draw.drawImage(img,alpha, xyzSW, xyzSE,xyzNW);
       else
           draw.drawImage(img, alpha,  xcoord, ycoord);
}

    @Override
    public void setVisible(boolean v) {
        visible = v;
}

    @Override
    public boolean getVisible() {
        return visible;
}
	
    @Override
    public void setColor(Color c) {
        throw new IllegalArgumentException("method not available for this Object: PlotImage");
}

    @Override
    public Color getColor() {
        return null;
}

    	
}
