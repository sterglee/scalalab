package ScalaSciExamples.Chaos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaSci.math.plot.*;
import scalaSci.math.plot.render.*;

public class bifurLogist  {
  	Plot2DPanel p2;
                double  totalTime;
    public  bifurLogist() {
     p2 = new Plot2DPanel();
     new FrameView(p2).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     computeBifurcations();
     
    }

    public void computeBifurcations () {
        int xmax = 600;
        int ymax = 400;

        long starting = System.currentTimeMillis();
        Graphics g  = p2.plotCanvas.getGraphics();
        int j, k, m, n;
        double x, r, xplot, yplot;

        r =  2.0;
        while (r <= 4.0) {
            xplot = xmax*(r-2.0)/2.0;
            x = 0.5;
            for (k=0; k<= 400; k++)  {
                x = r*x*(1.0-x);
            }
            for (k=0; k<=400; k++) {
                x = r*x*(1.0-x);
                yplot = ymax*(1.0-x);
                m = (int) Math.round(xplot);
                n = (int) Math.round(yplot);
                g.drawLine(m, n, m, n);
            }
            r = r+0.0005;
        }
        long ending = System.currentTimeMillis();
        totalTime = (ending-starting)/1000.0;
    }


public static void main(String [] args) {
    new bifurLogist();
    
    
    }
}