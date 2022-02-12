package ScalaSciExamples.Chaos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class bifLogistFrame extends JFrame implements WindowListener {
  public long  totalTime;

    public  bifLogistFrame() {
        addWindowListener(this);
        setSize(800, 600);
    }

    public void paint (Graphics g) {
        int xmax = 600;
        int ymax = 400;

        int j, k, m, n;
        double x, r, xplot, yplot;

          long starting = System.currentTimeMillis();
      r =  2.0;
        while (r <= 4.0) {
            xplot = xmax*(r-2.0)/2.0;
            x = 0.5;
            for (k=0; k< 400; k++)  {
                x = r*x*(1.0-x);
            }
            for (k=0; k<400; k++) {
                x = r*x*(1.0-x);
                yplot = ymax*(1.0-x);
                m = (int) Math.round(xplot);
                n = (int) Math.round(yplot);
                g.drawLine(m, n, m, n);
            }
            r = r+0.0001;
        }
        long ending = System.currentTimeMillis();
        totalTime = (ending-starting);
        System.out.println("totalTime = "+totalTime);

    }

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        {System.exit(0); }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

public static void main(String [] args) {
    bifLogistFrame  f = new bifLogistFrame();
    f.setVisible(true);
    f.setTitle("Bifurcation Diagramcomputed in time = "+f.totalTime);
    }
}