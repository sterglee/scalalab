
package JFplot;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.jfree.ui.RectangleEdge;

import JFplot.jPlot.AxisEnum;


public class jplotDemo {
    
    private static void test1() {
        jPlot chart = new jPlot();
        chart.setTitle("test1");
        final int NUM_PTS = 500;
        double x[] = new double[NUM_PTS];
        double y1[] = new double[NUM_PTS];
        double y2[] = new double[NUM_PTS];
        double y3[] = new double[NUM_PTS];
        double y4[] = new double[NUM_PTS];
        double y5[] = new double[NUM_PTS];
        double y6[] = new double[NUM_PTS];
        double y7[] = new double[NUM_PTS];
        double y8[] = new double[NUM_PTS];
        double y9[] = new double[NUM_PTS];
        double y10[] = new double[NUM_PTS];
        double y11[] = new double[NUM_PTS];
        for (int i = 0; i < NUM_PTS; i++) {
            x[i] = i;
            y1[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*2;
            y2[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*3;
            y3[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*4;
            y4[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*5;
            y5[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*6;
            y6[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*7;
            y7[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*8;
            y8[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*9;
            y9[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*10;
            y10[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*11;
            y11[i] = Math.sin(6. * i / NUM_PTS * Math.PI)*12;
        }
        chart.jplot(x, y1, "k-+", "sin1",
                   x, y2, "k-*", "sin2",
                   x, y3, "k-^", "sin3",
                   x, y4, "k-v", "sin4",
                   x, y5, "k-<", "sin5",
                   x, y6, "k->", "sin6",
                   x, y7, "k-o", "sin7",
                   x, y8, "k-x", "sin8",
                   x, y9, "k-s", "sin9",
                   x, y10, "k-d", "sin10",
                   x, y11, "k.-", "sin11");
        
        chart.setLineVisibility(jPlot.LAST_IDX, true, true);
        chart.showInNewFrame();
    }
    
    
    private static void test2() throws IOException {
        jPlot chart = new jPlot();
        chart.setAxisRange(AxisEnum.Y, -1, 2);
        double x1[] = new double[] { 1.1, 1.2, 1.5, 1.6, 2.0 };
        double y1[] = new double[] { 0.1, 0.5, 0.5, 0.6, 0 };
        chart.addPlot(x1, y1, "g");
        
        chart.setLineColor(1, Color.orange);
        chart.setLineStyle(1,
                           new Rectangle2D.Double(-5, -5, 10, 10),
                           6,
                           new float[] { 10, 30, 50, 30 });

        chart.addMarker(AxisEnum.X, 1.5);
        chart.addMarker(AxisEnum.Y, 0.5, Color.darkGray, 3, new float[] { 4, 8 });

        chart.setTickUnit(AxisEnum.X, 0.25);
        chart.showInNewFrame();
        chart.setTitle("test2");
        chart.saveAsPNG("test.png", 1000, 800);
    }
    
    
    private static void test3() {
        final int NUM_PTS = 10;
        double[] x = new double[NUM_PTS];
        double[] y1 = new double[NUM_PTS];
        double[] y2 = new double[NUM_PTS];
        double[] y3 = new double[NUM_PTS];
        double[] y4 = new double[NUM_PTS];
        double[] y5 = new double[NUM_PTS];
        double[] y6 = new double[NUM_PTS];
        
        for (int i = 0; i < NUM_PTS; i++) {
            x[i] = i;
            y1[i] = Math.log(i + 1);
            y2[i] = Math.log(i*10 + 10);
            y3[i] = Math.log(i*100 + 100);
            y4[i] = Math.log(i*1000 + 1000);
            y5[i] = Math.log(i*10000 + 10000);
            y6[i] = Math.log(i*100000 + 100000);
        }
        jPlot chart = new jPlot();
        chart.jplot(x, y1, x, y2, x, y3, x, y4, x, y5, x, y6);
        
        chart.setLineSpec(0, "-", 1);
        chart.setLineSpec(1, "--", 1);
        chart.setLineSpec(2, "-.", 1);
        chart.setLineSpec(3, ":", 1);
        chart.setLineSpec(4, "x-", 3);
        chart.setLineSpec(5, "d--r", 2); 
        chart.showInNewFrame();
    }
    
    
    private static void test4() {
        final int NUM_PTS = 10;
        double[] x = new double[NUM_PTS];
        double[] y1 = new double[NUM_PTS];
        double[] y2 = new double[NUM_PTS];
        double[] y3 = new double[NUM_PTS];
        double[] y4 = new double[NUM_PTS];
        double[] y5 = new double[NUM_PTS];
        double[] y6 = new double[NUM_PTS];
        
        for (int i = 0; i < NUM_PTS; i++) {
            x[i] = i;
            y1[i] = Math.exp(i + 1);
            y2[i] = Math.exp(i + 2);
            y3[i] = Math.exp(i + 3);
            y4[i] = Math.exp(i + 4 );
            y5[i] = Math.exp(i + 5);
            y6[i] = Math.exp(i + 6);
        }
        jPlot chart = new jPlot();
        String[] ids = chart.jplot(x, y1, x, y2);
        chart.setLineColor(ids[0], Color.blue);
        chart.setLineStyle(ids[0], new Rectangle2D.Double(-5, -5, 10, 10), 3);
        
        String thirdLineId = chart.addPlot(x, y3, "r:", "thirdLine");
        chart.setLineSpec(thirdLineId, "g-d", 5);
        chart.showInNewFrame();
    } 
    
    
    private static void test5() {
        jPlot chart = new jPlot();
        chart.showInNewFrame();

        double x1[] = new double[] { 0.1, 1.2, 1.5, 1.6, 2.0 };
        double y1[] = new double[] { 0.1, 0.5, 0.5, 0.6, 0 };
        chart.jplot(x1, y1, 1.);
        
        chart.setLineSpec(0, "g-.", 1);
    }
    
    
    /**
     * This method is used for testing only.
     * 
     * @param args
     */
    public static void main(String args[]) throws IOException {
        jPlot chart = new jPlot();
        double x1[] = new double[] { 1.1, 1.2, 1.5, 1.6, 2.0 };
        double y1[] = new double[] { 0.1, 0.5, 0.5, 0.6, 0 };
        double x2[] = new double[] { 0.2, 0.4, 1.5, 1.6, 2 };
        double y2[] = new double[] { 1.1, 0.4, 2.5, 2.6, 2.1 };
        chart.jplot(x1, y1, "k", "first line");
        chart.setHold(true);
        chart.jplot(x2, y2, "r-.", "second line");

        double x3[] = { 0.2, 0.23, 0.39, 0.9, 0.98 };
        double y3[] = { 1.8, 2, 3, 3.2, 3.8 };
        chart.addPlot(x3, y3, "m.", "third line");

        chart.showInNewFrame(); // .setSize(200, 200);
        chart.setBackground(Color.lightGray);
        chart.setGridColor(Color.red);
        chart.setLineVisibility(jPlot.LAST_IDX, true, true);

        chart.addAnnotation(0, 2, "note A");
        chart.addAnnotation(1.3, 0.3, "note B");

        chart.setTitle("Distribution");
        chart.setLegendPosition(RectangleEdge.RIGHT);

        chart.setLabel(AxisEnum.Y, "Y");
        chart.setLabel(AxisEnum.X, "seconds");

        test1(); 
        test2();
        test3();
        test4(); 
        test5();
    }
}
