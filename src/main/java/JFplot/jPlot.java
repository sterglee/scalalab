package JFplot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;

// TODO subplots, load/save
import scalaSci.Vec;

/**
 * This class encapsulates JFreeChart's XY charts. JFreeChart is very powerful,
 * which also means that it is sometimes to complex for trivial tasks. This class
 * simplifies the usage of JFreeChart by providing interface similar to Matlab's 
 * plot command.<p>
 * 
 * To create a chart, we need X and Y values stored in arrays of 
 * doubles. Then we:
 * <ul>
 * <li>create a chart object</li>
 * <li>display the chart in window</li>  
 * <li>plot the data.</li>
 * </ul> 
 * This procedure is illustrated by the following example:
 * <pre>
 *      double x1[] = new double[] { 0.1, 1.2, 1.5, 1.6, 2.0 };
 *      double y1[] = new double[] { 0.1, 0.5, 0.5, 0.6, 0 };

 *      jplot chart = new jplot("example1"); // create a chart object
 *      chart.showInNewFrame();                // display it in new window
 *
 *      chart.plot(x1, y1);                    // draw a line
 * </pre>
 *   
 * <p>
 * If we want to display a chart in existing window, we can get a {@link JPanel}
 * object containing chart by calling method {@link #getPanel()} 
 * <p>
 * We can configure chart properties (colors, axis ranges...), by setter methods.
 * Most <code>set...()</code> methods may be called before or after <code>plot()</code> 
 * and <code>addPlot()</code> methods. The exception to this rule are  
 * methods, which set color and style of lines. They all have 
 * <code>lineIndex</code> or <code>lineId</code> as first parameter. These 
 * methods may be called only after the line with the given index or id has been 
 * drawn.  
 * <p>
 * 
 * <b>Examples:</b><br>
 * Let's draw a green, dashed line, with 'x' markers and legend 'temperature':
 * <pre>
 *        String lineId = chart.plot(x1, y1, "g--x", "temperature");
 * </pre>
 * Note that we used <code>lineId</code> to store id for use in the next statement, 
 * where we'll change the line style and color to red dotted line with diamond 
 * markers. Width will be 5 pixels:
 * <pre>
 *        chart.setLineSpec(lineId, "r:d", 5);
 * </pre>
 * The same can be done by specifying the last plotted line:
 * <pre>
 *        chart.setLineSpec(jPlot.LAST_IDX, "r:d", 5);
 * </pre>
 * or specifying index value:
 * <pre>
 *        chart.setLineSpec(0, "r:d", 5);
 * </pre>
 * 
 * We can have more than one line on the chart at the same time. This can be 
 * achieved in one of three ways:<ul>
 * <li>by specifying all lines in a single plot command, for example 
 *   <code>plot(x1, y1, x2, y2, x3, x3)</code></li>
 * <li>by calling <code>setHold(true)</code> before calling next plot commands</li>
 * <li>by calling method <code>addPlot()</code> instead of <code>plot()</code></li>
 * </ul>
 * <p>
 * <b>Line specification</b>
 * <p>
 * Methods <code>plot(), addPlot()</code>, and <code>setLineSpec()</code> have 
 * parameter <code>lineSpec</code>, which defines line color, style, and markers. 
 * Any combination of color, marker and style is allowed. The following 
 * properties can be specified: 
 * 
 * <ul>
 * <li>Colors:</li>
 * <ul>
 *   <li>r - red</li>
 *   <li>g - green</li>
 *   <li>b - blue</li>
 *   <li>c - cyan</li>
 *   <li>y - yellow</li>
 *   <li>m - magenta</li>
 *   <li>k - black</li>
 * </ul>

 * <li>Markers:</li>
 * <ul>
 *   <li>+ - Plus sign</li>
 *   <li>o - Circle</li>
 *   <li>* - Asterisk</li>
 *   <li>. - Point</li>
 *   <li>x - Cross</li>
 *   <li>^ - Upward-pointing triangle</li>
 *   <li>v - Downward-pointing triangle</li>
 *   <li>> - Right-pointing triangle</li>
 *   <li>< - Left-pointing triangle</li>
 *   <li>'square' or s - Square</li>
 *   <li>'diamond' or d - Diamond</li>
 *   <li>'pentagram' or p - Five-pointed star (pentagram)</li>
 *   <li>'hexagram' or h - Six-pointed star (hexagram)</li>
 * </ul>
 * <li>Styles</li>
 * <ul>
 *  <li><b>-</b> solid line</li> 
 *  <li><b>--</b> dashed line</li>
 *  <li><b>:</b> dotted line</li> 
 *  <li><b>-.</b> dash-dot line</li> 
 * </ul>
 * </ul>
 * 
 * <b>Examples:</b><p>
 * "yx--" - yellow dashed line with crosses at points<br>
 * ":c" - dotted cyan line without markers<br>
 * "w" - white solid line (the same as "w-")<br>
 * <p>
 *
 */


/*
 
 var x1 = inc(0, 0.1, 10);
 var x2 = sin(x1)
 var jp = new  jplot("example1"); // create a chart object
 jp.plot(x1, x2);                    // draw a line
 jp.showInNewFrame
 */
public class jPlot   {

    /**
     * Use this value as line index, to refer to the last line added to
     * the chart.
     */
    static public   final int LAST_IDX = -1; 
    
    public JFreeChart m_chart;
    public ChartPanel m_chartPanel;
    private JFrame m_frame;
    private DefaultXYDataset m_dataset;
    private boolean m_isHoldOn = false;

    private String m_title;
    private String m_xAxisLabel;
    private String m_yAxisLabel;
    private XYPointerAnnotation m_xyAnnotation;
    private ValueMarker m_xMarker;
    private ValueMarker m_yMarker;
    private double[] m_xAxisRange;
    private double[] m_yAxisRange;
    private Paint m_backgroundPaint;
    private Paint m_gridPaint;
    private double m_xTickDelta;
    private double m_yTickDelta;
    
    private int defaultXSize = 500;
    private int defaultYSize = 500;


   public JFreeChart getChart() {
       return m_chart;
   }
        /**
     * Enumeration of chart axes.
     */
    public enum AxisEnum {
        /** Identifies X axis. */
        X, 
        /** Identifies Y axis. */
        Y
    };


 

    /**
     * Call this method to get the panel with chart, which can be displayed
     * inside custom window.
     * 
     * @return panel containing this chart
     */
    public  JPanel getPanel() {
        if (m_chartPanel == null) {
            if (m_chart == null) {
                m_dataset = new DefaultXYDataset();
                m_chart = ChartFactory.createXYLineChart(m_title,  m_xAxisLabel,  m_yAxisLabel,
                                                         m_dataset,  PlotOrientation.VERTICAL,  true,  true,  false);
                initChart();
            }
             m_chartPanel = new ChartPanel(m_chart);
        }
        return m_chartPanel;
        
    }
    
     public  JPanel getPanel(jPlot jp) {
        if (jp.m_chartPanel == null) {
            if (jp.m_chart == null) {
                jp.m_dataset = new DefaultXYDataset();
                jp.m_chart = ChartFactory.createXYLineChart(m_title,  m_xAxisLabel,  m_yAxisLabel,
                                                         m_dataset,  PlotOrientation.VERTICAL,  true,  true,  false);
                initChart();
            }
            jp.m_chartPanel = new ChartPanel(jp.m_chart);
        }
        return jp.m_chartPanel;
        
    }


    /**
     * Call this method to open chart in a new window.
     * 
     * @return the created frame with the chart
     */
    public JFrame showInNewFrame() {
        if (m_frame == null) {
            m_frame = new JFrame();
            m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            m_frame.setContentPane(getPanel());
            m_frame.pack();
            m_frame.setVisible(true);
        }
        return m_frame;
    }


public JFrame showInNewFrame(int figId) {
        if (m_frame == null) {
            m_frame = new JFrame();
            m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            m_frame.setContentPane(getPanel());
            String figStr = "Figure "+figId;
            m_frame.setTitle(figStr);
            m_frame.pack();
            m_frame.setVisible(true);
        }
        return m_frame;
    }

    /**
     * Call this method to open chart in a new window. The only difference 
     * between this method and showInNewFrame() is in return value. This method
     * was implemented because MAtlab did not recognize method showInNewFrame()
     * possibly because of unknown class JFrame???
     * 
     * @return the created frame with the chart
     */
    public void showInNewWindow() {
        if (m_frame == null) {
            m_frame = new JFrame();
            m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            m_frame.setContentPane(getPanel());
            m_frame.pack();
            m_frame.setVisible(true);
        }
    }

        
    /**
     * Another method for testing in Matlab - without variable argument list.
     *  
     * @param x
     * @param y
     * @param dummy
     * @return
     */
      public String jplot(double []x, double []y, double dummy) {
        return jplot(x, y)[0];
    }
    
  /**
     * This method plots lines. Colors are cycled in the following order:
     * "y", "c", "m", "k", "r", "g", "b".
     *  
     * @param x array of X values
     * @param y array of Y values
     * @param args optional arrays of doubles for additional lines. Even arguments
     * are used as X values, odd arguments are used as Y values.
     * @return ids of lines. These ids can be used instead of index when calling
     * methods, which take lineId as parameter, for example setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation().
     */
       public String[] jplot(double []x, double []y, double[]... args) {
        
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("There should be even number of " + 
                                           "input arrays! It is: " + args.length);
        }
        int noOfSeries = args.length / 2 + 1; 
        String [] seriesKeys = new String[noOfSeries];
        seriesKeys[0] = jplot(x, y, "y", "")[0];
        
        int keyIndex = 1;
        String colors [] = {"y", "c", "m", "k", "r", "g", "b"};
        for (int i = 0; i < args.length; i += 2) {
            double xa[] = args[i];
            double ya[] = args[i + 1];
            seriesKeys[keyIndex++] = addPlot(xa, ya, colors[i % colors.length], "");
        }
        
        return seriesKeys;
    }

       /**
     * This method plots lines. Colors are cycled in the following order:
     * "y", "c", "m", "k", "r", "g", "b".
     *  
     * @param x Vec of X values
     * @param y Vec of Y values
     * @param args optional arrays of Vecs for additional lines. Even arguments
     * are used as X values, odd arguments are used as Y values.
     * @return ids of lines. These ids can be used instead of index when calling
     * methods, which take lineId as parameter, for example setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation().
     */
       // dummy parameter is needed for disambiguation
       public String[] jplot(Vec  x, Vec  y, int dummy, Vec ... args) {
        
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("There should be even number of " + 
                                           "input arrays! It is: " + args.length);
        }
        int noOfSeries = args.length / 2 + 1; 
        String [] seriesKeys = new String[noOfSeries];
        seriesKeys[0] = jplot(x, y, "y", "")[0];
        
        int keyIndex = 1;
        String colors [] = {"y", "c", "m", "k", "r", "g", "b"};
        for (int i = 0; i < args.length; i += 2) {
            double xa[] = args[i].getv();
            double ya[] = args[i + 1].getv();;
            seriesKeys[keyIndex++] = addPlot(xa, ya, colors[i % colors.length], "");
        }
        
        return seriesKeys;
    }

   
       public String[] jplot(Vec x, Vec y, double[]... args) {
        return jplot(x.getv(), y.getv(), args);
    }

       public String[] jplot(double [] x, Vec y, double[]... args) {
        return jplot(x, y.getv(), args);
    }

      public String[] jplot(Vec  x, double [] y, double[]... args) {
        return jplot(x.getv(), y, args);
    }

       public String[] jplot(double [] x) {
         // construct implicitly the axis
           int sigLen = x.length;
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
                   
        return jplot(taxis, x);
    }

       public String[] jplot(Vec  x) {
            // construct implicitly the axis
           int sigLen = x.size();
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x.getv());
    }
 
       
       public String[] jplot(Vec  x, String specs) {
            // construct implicitly the axis
           int sigLen = x.size();
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x.getv(), specs);
    }
 
        public String[] jplot(double x[], String lineSpec, Object... args) {
    // construct implicitly the axis
           int sigLen = x.length;
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x, lineSpec, args);
        }
        
        public String[] jplot(Vec x, String lineSpec, Object... args) {
    // construct implicitly the axis
           int sigLen = x.size();
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x.getv(), lineSpec, args);
        }
        
        
    /**
     * This method plots lines with the specified line style and color.
     *  
     * @param x array of X values
     * @param y array of Y values
     * @param lineSpec line specification, see class description for details
     * @param args triples of (double x[], double y[], String lineSpec) for next
     * lines 
     * @return ids of lines. These ids can be used instead of index when calling
     * methods, which take lineId as parameter, for example setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation().
     */
       public String[] jplot(double x[], double y[], String lineSpec, Object... args) {
        
        if (args.length % 3 != 0) {
            throw new IllegalArgumentException("The number of input args should " + 
                                           "be multiple of 3! It is: " + args.length);
        }
        
        int noOfSeries = args.length / 3 + 1; 
        String [] seriesKeys = new String[noOfSeries];
        seriesKeys[0] = jplot(x, y, lineSpec, "")[0];
        
        int keyIndex = 1;
        for (int i = 0; i < args.length; i += 3) {
            double xa[] = (double[])args[i];
            double ya[] = (double[])args[i + 1];
            String lineSpecA = (String)args[i + 2];
            seriesKeys[keyIndex++] = addPlot(xa, ya, lineSpecA, "");
        }
        
        return seriesKeys;
    }

    /*
     var opl = new plot
     var  t = Inc(0, 0.01, 10)
     var  x = sin(0.234*t)+0.45*cos(0.3*t)
  //  we draw a red dashed line, with 'x' markers and legend sin-cos
     opl.jplot(t,x, "r--x", "sin-cos")
     opl.showInNewFrame
*/    
     
     /**
     * This method plots lines with the specified line style and color.
     *  
     * @param x array of X values
     * @param y array of Y values
     * @param lineSpec line specification, see class description for details
     * @param legend text for legend
     * @param args tuples of (double x[], double y[], String lineSpec, String legend) for next
     * lines 
     * @return ids of lines. Usually the 'legend' parameter is used as id, unless
     * the line with the same legend already exists in the chart. In such case
     * the legend string is modified. These ids can be used instead of indices when calling
     * methods, which take <code>lineId </code> as parameter, for example <code>setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation()</code>.
     */
/*
 var t=inc(0,0.01, 2); var x = sin(2.3*t);
 jplot(t, x)
 */
      public String[] jplot(double []x, Vec  y, String lineSpec, String legend, Object... args) {
          return   jplot(x, y.getv(), lineSpec, legend, args);
    }
    
       public String[] jplot(Vec x, Vec  y, String lineSpec, String legend, Object... args) {
           return   jplot(x.getv(), y.getv(), lineSpec, legend, args);
    }
    
       public String[] jplot(Vec x, double []  y, String lineSpec, String legend, Object... args) {
           return  jplot(x.getv(), y, lineSpec, legend, args);
    }
    
       
        public String[] jplot(double x[], String lineSpec, String legend, Object... args) {
    // construct implicitly the axis
           int sigLen = x.length;
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x, lineSpec, legend, args);
        }
        
        public String[] jplot(Vec x, String lineSpec, String legend,  Object... args) {
    // construct implicitly the axis
           int sigLen = x.size();
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
         return jplot(taxis, x.getv(), lineSpec, legend, args);
        }
        
      public String[] jplot(double x[], double y[], String lineSpec, String legend, Object... args) {

        if (args.length % 4 != 0) {
            throw new IllegalArgumentException("The number of input args should " + 
                                           "be multiple of 4! It is: " + args.length);
        }
        
        LineAttrs lineAttrs = new LineAttrs(lineSpec);
        int noOfSeries = args.length / 4 + 1; 
        String [] seriesKeys = new String[noOfSeries];
        seriesKeys[0] = jplot(x,
                             y,
                             lineAttrs.getColor(),
                             lineAttrs.getMarker(),
                             lineAttrs.getStyle(),
                             legend);

        int keyIndex = 1;
        for (int i = 0; i < args.length; i += 4) {
            double xa[] = (double[])args[i];
            double ya[] = (double[])args[i + 1];
            String lineSpecA = (String)args[i + 2];
            String legendA = (String)args[i + 3]; 
            seriesKeys[keyIndex++] = addPlot(xa, ya, lineSpecA, legendA);
        }
        
        return seriesKeys;
    }

public String jplot(double x[], Paint color, Shape marker,  float[] style,  String legend) {
// construct implicitly the axis
           int sigLen = x.length;
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
    
           return jplot(taxis, x, color, marker, style, legend);
}



public String jplot(Vec  x, Paint color, Shape marker,  float[] style,  String legend) {
// construct implicitly the axis
           int sigLen = x.size();
           double [] taxis = new double[sigLen];
           for (int k=0; k<sigLen; k++)
               taxis[k] = k;
    
           return jplot(taxis, x.getv(), color, marker, style, legend);
}

/**
     * We use this method when we want to specify line properties, which can not
     * be specified with standard specification string.
     * 
     * @param x array of X values
     * @param y array of Y values
     * @param color line color
     * @param marker shape used to mark points given by x[] and y[]
     * @param style definition for lengths of dashes and dots, for example:
     * new float[]{2, 5, 8, 5} specifies dot dash line.
     * @param legend text to appear in the legend
     * @return id of line. Usually the 'legend' parameter is used as id, unless
     * the line with the same legend already exists in the chart. In such case
     * the legend string is modified. This id can be used instead of index when calling
     * methods, which take lineId as parameter, for example <code>setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation()</code>.
     */
       public String jplot(double x[], double y[], Paint color, Shape marker,  float[] style,  String legend) {

        if (x.length != y.length) {
            throw new IllegalArgumentException("Arrays x and y must have the same size!"
                    + " x.len = " + x.length + ",  y.len = " + y.length);
        }

        String seriesKey = null;
        
        if (m_chart == null) {
            m_dataset = new DefaultXYDataset();
            
            seriesKey = getUniqueSeriesKey(m_dataset, legend);
            m_dataset.addSeries(seriesKey, 
                                new double[][] { x, y });

            m_chart = ChartFactory.createXYLineChart(m_title,
                                                     m_xAxisLabel,
                                                     m_yAxisLabel,
                                                     m_dataset,
                                                     PlotOrientation.VERTICAL,
                                                     true,
                                                     true,
                                                     false);
            initChart();
        } else { // chart already exists - we'll use it
            if (m_isHoldOn) {
                seriesKey = getUniqueSeriesKey(m_dataset, legend);
                m_dataset.addSeries(seriesKey, new double[][] { x, y });
            } else {   // hold not ON
                for (int i = 0; i < m_dataset.getSeriesCount(); i++) {
                    Comparable key = m_dataset.getSeriesKey(i);
                    m_dataset.removeSeries(key);
                }
                seriesKey = getUniqueSeriesKey(m_dataset, legend);
                m_dataset.addSeries(seriesKey, new double[][] { x, y });
            }
        }

        if (color == null) {
            color = Color.yellow;
        }
        
        setLineColor(LAST_IDX, color);
        
        if (marker != null  ||  style != null) {
            setLineStyle(LAST_IDX, 
                         marker, 
                         1, 
                         style);
        } 
        
        return seriesKey;
    }

       public ChartFrame  jPieChart(String [] categories, double [] values, String chartTitle) {
           DefaultPieDataset data = new DefaultPieDataset();
           for (int k=0; k<values.length; k++)  
              data.setValue(categories[k], values[k]);
            m_chart = ChartFactory.createPieChart(chartTitle, data, true, true, true);
           
           ChartFrame cf = new ChartFrame("Pie Chart", m_chart);
           cf.setSize(defaultXSize, defaultYSize);
           cf.setVisible(true);
           return cf;
       
       }
       
       
       /*
        var categories  = Array("category1", "category2", "category3")
        var values = Array(8.9, 5.6, 3.4)
        var jchart  = new jPlot()
        jchart.jPieChart(categories, values, "A demo pie chart")
        jchart.getChart().setBackgroundPaint(Color.blue)
        * 
        
        */


    /**
     * If setters were called before chart has been created, set them now.
     */
     private void initChart() {
        XYPlot plot = m_chart.getXYPlot();

        if (m_xyAnnotation != null) {
            plot.addAnnotation(m_xyAnnotation);
        }
        
        if (m_xMarker != null) {
            plot.addDomainMarker(m_xMarker);
        }
        
        if (m_yMarker != null) {
            plot.addRangeMarker(m_yMarker);
        }
        
        if (m_xAxisRange != null) {
            NumberAxis rangeAxis = getAxis(AxisEnum.X);
            rangeAxis.setRange(m_xAxisRange[0], m_xAxisRange[1]);
        }
        
        if (m_yAxisRange != null) {
            NumberAxis rangeAxis = getAxis(AxisEnum.Y);
            rangeAxis.setRange(m_yAxisRange[0], m_yAxisRange[1]);
        }
        
        if (m_backgroundPaint != null) {
            m_chart.setBackgroundPaint(m_backgroundPaint);
            plot.setBackgroundPaint(m_backgroundPaint);
        }
        
        if (m_gridPaint != null) {
            plot.setDomainGridlinePaint(m_gridPaint);
            plot.setRangeGridlinePaint(m_gridPaint);
        }
        
        if (m_xTickDelta > 0) {
            NumberAxis rangeAxis = getAxis(AxisEnum.X);
            rangeAxis.setTickUnit(new NumberTickUnit(m_xTickDelta));
        }
        
        if (m_yTickDelta > 0) {
            NumberAxis rangeAxis = getAxis(AxisEnum.Y);
            rangeAxis.setTickUnit(new NumberTickUnit(m_yTickDelta));
        }
        
    }


    /**
     * Sets text for the given axis.
     * @param label axis text
     */
    public void setLabel(AxisEnum axis, String label) {
        if (m_chart != null) {
            ValueAxis vaxis = getAxis(axis);  // plot.getRangeAxis();
            vaxis.setLabel(label);
        }
        
        switch (axis) {
        case X:
            m_xAxisLabel = label;
            break;
        case Y:
            m_yAxisLabel = label;
            break;
        }
    }


    /**
     * Sets text for X axis.
     * @param label x axis text */
    public void setXLabel(String label) {
        m_xAxisLabel = label;
        XYPlot plot = m_chart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setLabel(label);
    }
    
    /**
     * Sets text for Y axis.
     * @param label y axis text */
    public void setYLabel(String label) {
        m_yAxisLabel = label;
        XYPlot plot = m_chart.getXYPlot();
        ValueAxis axis = plot.getRangeAxis();
        axis.setLabel(label);
    }
     


    /**
     * Sets the chart title.
     * @param title title text.
     */
    public void setTitle(String title) {
        if (m_chart != null) {
            m_chart.setTitle(title);
        }
        m_title = title;
    }


    /**
     * Sets the chart background color.
     *  
     * @param color new background color. For example 
     * <code>new java.awt.Color(12, 45, 145),</code> or <code>java.awt.Color.grey</code>.
     */
    public void setBackground(Paint color) {
        if (m_chart != null) {
            m_chart.setBackgroundPaint(color);
            
            XYPlot plot = m_chart.getXYPlot();
            plot.setBackgroundPaint(color);
        }
        
        m_backgroundPaint = color;
    }


    /**
     * Sets the grid color.
     * 
     * @param color grid color.
     */
    public void setGridColor(Paint color) {
        
        if (m_chart != null) {
            XYPlot plot = m_chart.getXYPlot();
            plot.setDomainGridlinePaint(color);
            plot.setRangeGridlinePaint(color);
        }
        
        m_gridPaint = color;
    }


    /**
     * This method sets visibility of line parts.
     * Line is composed of line segments and markers. This method specifies, 
     * which of these should be visible.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlo.LAST_IDX
     * to refer to the last line added.
     * @param isLineVisible if true, line segments are visible
     * @param isShapeVisible if true, markers are visible.
     */
    public void setLineVisibility(int lineIndex,
                                  boolean isLineVisible,
                                  boolean isShapeVisible) {
        XYPlot plot = m_chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        
        lineIndex = getLastIndex(lineIndex);
        renderer.setSeriesLinesVisible(lineIndex, isLineVisible);
        renderer.setSeriesShapesVisible(lineIndex, isShapeVisible);
    }


    /**
     * This method sets visibility of line parts.
     * Line is composed of line segments and markers. This method specifies, 
     * which of these should be visible.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods. Usually the 'legend' parameter is used as id, unless
     * the line with the same legend already exists in the chart. In such case
     * the legend string is modified.  
     * @param isLineVisible if true, line segments are visible
     * @param isShapeVisible if true, markers are visible.
     */
    public void setLineVisibility(String lineId,
                                  boolean isLineVisible,
                                  boolean isShapeVisible) {
        setLineVisibility(getLineIndex(lineId), isLineVisible, isShapeVisible);
    }
    
    
    /**
     * Sets color of existing line.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.
     * @param linePaint for example: Paint paint = Color.magenta;
     */
    public void setLineColor(int lineIndex, Paint linePaint) {
        XYPlot plot = m_chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesPaint(getLastIndex(lineIndex), linePaint);
    }


    /**
     * Sets color of existing line.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.  
     * @param linePaint for example: Paint paint = Color.magenta;
     */
    public void setLineColor(String lineId, Paint linePaint) {
        setLineColor(getLineIndex(lineId), linePaint);
    }
    
    
    /**
     * This method specifies shape of markers and line width.
     *  
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.
     * 
     * @param marker
     *            shape drawn at point positions, for example new
     *            Rectangle2D.Double(-4, -4, 8, 8). May also be null, to keep
     *            the existing shape.
     * @param lineWidth line width
     * 
     * @see #LAST_IDX
     */
    public void setLineStyle(int lineIndex, Shape marker, int lineWidth) {
        setLineStyle(lineIndex, marker, lineWidth, new float[] { 1 });
    }


    /**
     * This method specifies shape of markers and line width.
     *  
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.  
     * 
     * @param marker
     *            shape drawn at point positions, for example new
     *            Rectangle2D.Double(-4, -4, 8, 8). May also be null, to keep
     *            the existing shape.
     * @param lineWidth line width
     * 
     * @see #LAST_IDX
     */
    public void setLineStyle(String lineId, Shape marker, int lineWidth) {
        setLineStyle(getLineIndex(lineId), marker, lineWidth, new float[] { 1 });
    }


    /**
     * This method specifies shape of markers and line width.
     * It is intended for advanced users, who are familiar with Java2D API.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.
     * 
     * @param marker
     *            shape drawn at point positions, for example new
     *            Rectangle2D.Double(-4, -4, 8, 8). May also be null, to keep
     *            the existing shape.
     * @param lineWidth line width
     * @param style
     *            specifies lenghts of dashes and spaces, for example new
     *            float[]{1, 10, 5, 5}
     */
    public void setLineStyle(int lineIndex,
                             Shape marker,
                             int lineWidth,
                             float[] style) {

        XYPlot plot = m_chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        lineIndex = getLastIndex(lineIndex);
        if (marker != null) {
            renderer.setSeriesShape(lineIndex, marker);
            if (style == null) { // no line, draw only markers
                /* float dashPhase = 0;
                Stroke stroke = new BasicStroke(lineWidth,
                                                BasicStroke.CAP_ROUND,
                                                BasicStroke.JOIN_ROUND,
                                                2,
                                                null,
                                                dashPhase);
                renderer.setSeriesStroke(lineIndex, stroke);
                */
                setLineVisibility(lineIndex, false, true);
            } else {
                setLineVisibility(lineIndex, true, true); // show line and markers
            }
        }
        
        if (style != null) {
            float dashPhase = 0;
            Stroke stroke = new BasicStroke(lineWidth,
                                            BasicStroke.CAP_ROUND,
                                            BasicStroke.JOIN_ROUND,
                                            2,
                                            style,
                                            dashPhase);
            renderer.setSeriesStroke(lineIndex, stroke);
        }
    }


    /**
     * This method specifies shape of markers and line width.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.
     * 
     * @see #setLineStyle(int, Shape, int, float[])
     */
    public void setLineStyle(String lineId,
                             Shape marker,
                             int lineWidth,
                             float[] style) {
        setLineStyle(getLineIndex(lineId), marker, lineWidth, style);
    }
    

    /**
     * This method sets line specification - color, style, markers, and width.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.

     * @param lineSpec line specification, see class description for details
     * @param lineWidth width of the line
     */
    public void setLineSpec(int lineIndex, String lineSpec, int lineWidth) {

        LineAttrs lineAttrs = new LineAttrs(lineSpec);
        setLineColor(lineIndex, lineAttrs.getColor());
        setLineStyle(lineIndex, lineAttrs.getMarker(), lineWidth, lineAttrs.getStyle());
    }


    /**
     * This method sets line specification - color, style, markers, and width.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.

     * @param lineSpec line specification, see class description for details
     * @param lineWidth width of the line
     */
    public void setLineSpec(String lineId, String lineSpec, int lineWidth) {
        setLineSpec(getLineIndex(lineId), lineSpec, lineWidth);
    }

    
    /**
     * Defines delta between ticks on the specified axis. 
     * @param delta the delta
     */
    public void setTickUnit(AxisEnum axis, double delta) {

        if (m_chart != null) {
            NumberAxis rangeAxis = getAxis(axis);
        
            //  change the auto tick unit selection to integer units only...
            //  rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            rangeAxis.setTickUnit(new NumberTickUnit(delta));
        } 
        
        switch (axis) {
        case X:
            m_xTickDelta = delta;
            break;
        case Y:
            m_yTickDelta = delta;
            break;
        }
    }


    private NumberAxis getAxis(AxisEnum axis) {
        XYPlot plot = m_chart.getXYPlot();
        switch (axis) {
        case X:
            return (NumberAxis)plot.getDomainAxis();
        case Y:
            return (NumberAxis)plot.getRangeAxis();
        }
        return null; // should never happen
    }
    

    /**
     * Draws annotation at default angle of 45 degrees.
     * 
     * @param x pointer X position
     * @param y pointer Y position
     * @param annotation annotation text
     */
    public void addAnnotation(double x, double y, String annotation) {
        addAnnotation(x, y, annotation, -0.78f);
    }


    /**
     * Draws annotation on the chart.
     * 
     * @param x pointer X position
     * @param y pointer Y position
     * @param annotation annotation text
     * @param angle annotation line angle in degrees
     */
    public void addAnnotation(double x, double y, String annotation, float angle) {
        m_xyAnnotation = new XYPointerAnnotation(annotation,
                                                                   x,
                                                                   y,
                                                                   -angle * Math.PI / 180);
        m_xyAnnotation.setLabelOffset(m_xyAnnotation.getLabelOffset() * 2 + 10);

        if (m_chart != null) {
            XYPlot plot = m_chart.getXYPlot();
            plot.addAnnotation(m_xyAnnotation);
        }
    }


    /**
     * Draws annotation at default angle of 45 degrees. Annotation coordinates 
     * are given implicitly by specifying data index.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.
     * @param pointIdx index of point on the specified line
     * @param annotation annotation text
     */
    public void addAnnotation(int lineIndex, int pointIdx, String annotation) {
        addAnnotation(lineIndex, pointIdx, annotation, -0.78f);
    }


    /**
     * Draws annotation on the chart.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.
     * 
     * @see #addAnnotation(int, int, String)
     */
    public void addAnnotation(String lineId, int pointIdx, String annotation) {
        addAnnotation(getLineIndex(lineId), pointIdx, annotation, -0.78f);
    }


    /**
     * Draws annotation on the chart. Annotation coordinates 
     * are given implicitly by specifying data index.
     * 
     * @param lineIndex index specifying the line. Indices are assigned to
     * lines in the same order as they were added to chart. Use jPlot.LAST_IDX
     * to refer to the last line added.
     * @param pointIdx index of point on the specified line
     * @param annotation annotation text
     * @param angle annotation line angle in degrees
     */
    public void addAnnotation(int lineIndex, int pointIdx, String annotation, float angle) {
        lineIndex = getLastIndex(lineIndex);
        double x = m_dataset.getXValue(lineIndex, pointIdx);
        double y = m_dataset.getYValue(lineIndex, pointIdx);
        addAnnotation(x, y, annotation, angle);
    }


    /**
     * Draws annotation on the chart.
     * 
     * @param lineId id specifying the line. Ids are returned from jplot() and 
     * addPlot() methods.
     * 
     * @see #addAnnotation(int, int, String, float)
     */
    public void addAnnotation(String lineId,
                              int pointIdx,
                              String annotation,
                              float angle) {
        addAnnotation(getLineIndex(lineId), pointIdx, annotation, angle);
    }
    
    
    /**
     * Sets position of the legend. Legend can currently be placed only on one of
     * the chart edges.  
     * @param position legend position, for example RectangleEdge.BOTTOM
     */
    public void setLegendPosition(RectangleEdge position) {
        if (m_chart != null) {
            LegendTitle legend = m_chart.getLegend();
            legend.setPosition(position);
        } else {
            throw new IllegalStateException("Can not set legend position before " +
            		"the chart is drawn. Call jplot() method first!");
        }
    }


    /**
     * Adds marker line to the chart. The default color is red.
     * 
     * @param axis axis on which to add the marker.
     * @param position marker position
     */
    public void addMarker(AxisEnum axis, double position) {
        addMarker(axis, position, Color.red, 1, new float[] { 1 });
    }


    /**
     * Adds marker line to the chart.
     * 
     * @param axis axis on which to add the marker.
     * @param position marker line position
     * @param paint marker line paint
     */
    public void addMarker(AxisEnum axis, double position, Paint paint) {
        addMarker(axis, position, paint, 1, new float[] { 1 });
    }


    /**
     * Adds marker line to the chart.
     * 
     * @param axis axis on which to add the marker.
     * @param position marker position
     * @param paint marker paint
     * @param width marker width
     */
    public void addMarker(AxisEnum axis, double position, Paint paint, int width) {
        addMarker(axis, position, paint, width, new float[] { 1 });
    }


    /**
     * Adds marker line to the chart.
     * 
     * @param axis axis on which to add the marker.
     * @param position marker position
     * @param paint marker paint, for example <code>Color.yellow</code>
     * @param width marker width
     * @param style marker style, for example:
     * <cocde>new float[]{2, 5, 8, 5}</code> specifies dot dash line.
     */
    public void addMarker(AxisEnum axis,  double position, Paint paint, int width, float[] style) {

        float dashPhase = 0;
        Stroke stroke = new BasicStroke(width,  BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND, 2, style, dashPhase);

        ValueMarker marker = new ValueMarker(position, paint, stroke);

        switch (axis) {
        case X:
            m_xMarker = marker;
            if (m_chart != null) {
                m_chart.getXYPlot().addDomainMarker(m_xMarker);
            }
            break;
        case Y:
            m_yMarker = marker;
            if (m_chart != null) {
                m_chart.getXYPlot().addRangeMarker(m_yMarker);
            }
            break;
        }
    }


    /**
     * Sets range on the given axis.
     * 
     * @param min minimum value shown
     * @param max maximum value shown
     */
    public void setAxisRange(AxisEnum axis, double min, double max) {
        if (m_chart != null) {
            NumberAxis rangeAxis = getAxis(axis);
            rangeAxis.setRange(min, max);
        } 
        
        switch (axis) {
        case X:
            m_xAxisRange = new double[]{min, max};
            break;
        case Y:
            m_yAxisRange = new double[]{min, max};
            break;
        }
    }


    /**
     * If hold is set to true, then calls to jplot() method add lines to the
     * chart and keep existing ones. If set to false, calling jplot() method
     * deletes existing lines and adds new ones.
     *   
     * @param isHoldOn new hold status
     */
    public void setHold(boolean isHoldOn) {
        m_isHoldOn = isHoldOn;
    }


    /**
     * Toggles hold status.
     * 
     * @return hold status after it is toggled.
     * @see #setHold(boolean)
     */
    public boolean toggleHold() {
        m_isHoldOn = !m_isHoldOn;
        return m_isHoldOn;
    }


    /**
     * Returns current hold status.
     * 
     * @see #setHold(boolean)
     */
    public boolean getHold() {
        return m_isHoldOn;
    }
   
    
    /**
     * Adds lines to the chart regardless of hold status.
     * 
     * @param x array of X values
     * @param y array of Y values
     * @param lineSpec line specification, see class description for details
     * 
     * @return id of line. This id can be used instead of index when calling
     * methods, which take lineId as parameter, for example setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation().
     */
    public String addPlot(double []x, double []y, String lineSpec) {
        return addPlot(x, y, lineSpec, "");
    }

    public String addPlot(double []x, double []y, int dummy) {  // use a default line specification
        // dummy parameter is needed for disambiguation of overloaded definition
        // double [] x, double [] y, double []..
        return addPlot(x, y, "c", "") ;
    }
     
    public String addPlot(Vec x, Vec  y, String lineSpec) {
        // dummy parameter is needed for disambiguation of overloaded definition
        // Vec x, Vec y, Vec..
        return addPlot(x.getv(), y.getv(), lineSpec, "");
    }

    public String addPlot(Vec x, Vec  y, int dummy) {
        return addPlot(x.getv(), y.getv(), "");
    }
    /**
     * Adds lines to the chart regardless of hold status.
     * 
     * @param x array of X values
     * @param y array of Y values
     * @param lineSpec line specification, see class description for details
     * @param legend text for legend
     * 
     * @return id of line. Usually the 'legend' parameter is used as id, unless
     * the line with the same legend already exists in the chart. In such case
     * the legend string is modified. This id can be used instead of index when calling
     * methods, which take lineId as parameter, for example setLineColor(), 
     * setLinestyle(), setLineSpec(), addAnnotation().
     */
      public  String addPlot(double x[], double y[], String lineSpec, String legend) {
        if (m_chart != null) {
            String seriesKey = getUniqueSeriesKey(m_dataset, legend);
            m_dataset.addSeries(seriesKey, new double[][] {x, y});
            int lastSeriesIndex = m_dataset.getSeriesCount() - 1;

            LineAttrs lineAtrs = new LineAttrs(lineSpec);
            setLineColor(lastSeriesIndex, lineAtrs.getColor());
            setLineStyle(lastSeriesIndex, lineAtrs.getMarker(), 1, lineAtrs.getStyle());
            return seriesKey;
        } 

        return jplot(x, y, lineSpec, legend)[0];
    }

public  String addPlot(Vec x, Vec y, String lineSpec, String legend) {
    return   addPlot(x.getv(), y.getv(), lineSpec, legend);
}
 
/* public void load(String fileName) throws IOException {
    } */


    /**
     * Saves chart as image in PNG format.
     * 
     * @param fileName name of file to save image to
     * @param width image width in pixels
     * @param height image height in pixels
     * @throws IOException if the output file can not be opened for writing
     */
    public void saveAsPNG(String fileName, int width, int height) throws IOException {
        ChartUtilities.saveChartAsPNG(new File(fileName), m_chart, width, height);
    }


    // Returns index of the last line added to the chart 
    private int getLastIndex(int index) {
        int maxIdx = m_dataset.getSeriesCount() - 1;
        
        if (index == LAST_IDX  ||  index > maxIdx) {
            index = maxIdx;
        }
        
        return index;
    }
    
    
    private String getUniqueSeriesKey(DefaultXYDataset dataset, String suggestedKey) {
        if (dataset.indexOf(suggestedKey) == -1) {
            return suggestedKey;
        }
         
        suggestedKey += '.';
        return getUniqueSeriesKey(dataset, suggestedKey);
    }
    
    
    private int getLineIndex(String lineId) {
        int id = m_dataset.indexOf(lineId);
        if (id == -1) {
            throw new IllegalStateException("Line with the given id does not exist! " +
            		"Id = " + lineId);
        }
        return id;
    }
}
