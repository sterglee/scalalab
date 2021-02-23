
package JFplot;

import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JFrame;
import JFplot.jPlot.AxisEnum;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import scalaSci.Vec;
 
/*
 jsubplot(2,2,1)
 var t=inc(0, 0.01, 10); var x = sin(3.4*t);
 jplot(t, x)
 */
/*
 jsubplot2D(2,2,1)
 */ 

// implements a Matlab-like way of figure handling
public class jFigure {
 
        static int currentPlotCnt2D = 0;  // holds the number of the existing 2D figure  objects
        static int currentPlotCnt3D = 0; // holds the number of the existing 3D figure objects
        static int currentPlotCnt = 0;  // a counter for the current number of figures
        static int maxNumberOfFigs = 20;  // maximum number of figure frames
        static double  figTableIncreaseFactor = 1.5; // factor to increase dynamically the figure table size
        static jPlot [] [] [] allPlots = null;  // all the plot objects that belong to each Matlab-like figure object, e.g. allPlots[f][] is the table with the references to the plots
        static JFrame [] allFrames =  null; // holds the pointers to all the frames for all the figure objects

        static jPlot currentPlot = null;  // the handle where plot operations are directed
        static int currentFigId = -1;
        
        static boolean holdOnMode = true;  // controls holding previous plots, used to implement Matlab's hold("on"), hold("off")

        static int defaultXSize = 600;
        static int defaultYSize = 600;
        
    // increase the size of the global figure table when required
        private  static void  increasejFigTable() {
            int maxNumberOfFigsLarge = (int)(maxNumberOfFigs*figTableIncreaseFactor);  // update number of figs
            JFrame []  cpAllFrames = new JFrame[maxNumberOfFigsLarge];   // table that holds indices to the new figure frames
            for (int k=0; k<allFrames.length; k++)  // copy previous to enlarged
                cpAllFrames[k] = allFrames[k];
            jPlot  [][][] cpAllPlots = new jPlot[maxNumberOfFigsLarge][][];
            for (int k=0; k<maxNumberOfFigs; k++)  // copy previous to enlarged
                cpAllPlots[k] = allPlots[k];
            for (int k=maxNumberOfFigs; k<maxNumberOfFigsLarge; k++) {  // new entries to nulls
                cpAllPlots[k] = null;
                cpAllFrames[k] = null;
            }
            maxNumberOfFigs = maxNumberOfFigsLarge;  // update figure table size
            //   enlarged tables become the current
            allPlots = cpAllPlots;
            allFrames = cpAllFrames;
        }

        // increase the Figure tables to cover the specified Figure number
        private  static void  increasejFigTableSpecifiedSize(int specifiedFigNo) {
            if (specifiedFigNo <= maxNumberOfFigs)
                return;  // specified size smaller than the current Figure table size
            int maxNumberOfFigsLarge = (int)(figTableIncreaseFactor*specifiedFigNo);  // update number of figs
            JFrame []  cpAllFrames = new JFrame[maxNumberOfFigsLarge];
            for (int k=0; k<allFrames.length; k++)  // copy previous to enlarged
                cpAllFrames[k] = allFrames[k];
            jPlot [][][] cpAllPlots = new jPlot[maxNumberOfFigsLarge][][];
            for (int k=0; k<maxNumberOfFigs; k++)  // copy previous to enlarged
                cpAllPlots[k] = allPlots[k];
            for (int k=maxNumberOfFigs; k<maxNumberOfFigsLarge; k++) {  // new entries to nulls
                cpAllPlots[k] = null;
                cpAllFrames[k] = null;
            }
            maxNumberOfFigs = maxNumberOfFigsLarge;  // update figure table size
            //   enlarged tables become the current
            allPlots = cpAllPlots;
            allFrames = cpAllFrames;
        }


        // initializes the ploting system
	private static void initjplots() {
		if (allFrames == null)  {   // plotting system not initialized yet
                    allPlots = new jPlot[maxNumberOfFigs][][];  // all the "subplot" objects
                    for (int k=0; k<maxNumberOfFigs; k++)
                       allPlots[k] = null;
                    allFrames = new JFrame[maxNumberOfFigs];   // all the "figure" objects
                    for (int k=0; k<maxNumberOfFigs; k++)
                        allFrames[k] = null;
                    currentPlotCnt2D = 0;
                    currentPlotCnt3D = 0;
                    currentPlotCnt = 0;

                    currentFigId  = 0;
                    currentPlot = null;
                }
          }

/*
 var jp = jfigure(1)
 var t = inc(0, 0.01, 10); var x = sin(0.23*t)
 jplot(t,x)
 */ 

  /*
         var t = inc(0, 0.01, 5); var x1 = sin(0.12*t)
         var mp = jfigure
         jplot(t, x1)
         var x2 = sin(3.4*t)
         
         */ 
         // focus on the figure with the identifier figId
	static public jPlot  jfigure(int figId) {
            initjplots();  // init the plotting system if not initialized yet
            if (figId < 1)  // assume the smallest figId when a zero or negative figId is requested
                figId = 1;
            if (figId > maxNumberOfFigs)   // increase Figure table size
                increasejFigTableSpecifiedSize(figId);
          currentFigId = figId;  // set the figure ID we are working on, figure IDs start at 1
     
            int figMinus1 = figId-1;   // indexes start at 0, figures numbered from 1 according to Matlab conventions
            if (allPlots[figMinus1] != null)  {  // figure id exists
               currentPlot = allPlots[figMinus1][0][0];  // the requested figure panel
               JFrame currentFrame = allFrames[figMinus1];
               currentFrame.setSize(defaultXSize, defaultYSize);
               currentFrame.setVisible(true);
               return  currentPlot;
             }
            else   {  // we do not have the requested figure object, create it explicitly
              jsubplot(1, 1, 1);  
              return currentPlot;
    }
   }

      
     
         // create a new figure
	static public jPlot  jfigure() {
            int figId = currentFigId;   // init current Figure Id
            if (figId < 1)  // assume the smallest figId when a zero or negative figId is requested
                figId = 1;
            if (figId > maxNumberOfFigs)   // increase Figure table size
                increasejFigTableSpecifiedSize(figId);

           currentFigId++;  // increase figure id
           jsubplot(1, 1, 1);
           return currentPlot;
          }
                
        
            // returns the current number of figure objects (e.g. FrameView objects), is the number of non-null entries of the allFrames[] table
        static   public int  getjFigCount()  {
            int figCnt=0;
            for (int k=0; k<maxNumberOfFigs; k++)
                if (allFrames[k] != null)
                    figCnt++;

            return figCnt;
        }

        
public static void jsubplot( int  p)  {
    int  r = p / 100;  
    int  rem = p % 100;
    int c = rem / 10;
    int  id = rem % 10;
    jsubplot(r, c, id);
}

public static void jsubplot2d( int  p)  {
    int  r = p / 100;  
    int  rem = p % 100;
    int c = p / 10;
    int  id = rem % 10;
    jsubplot(r, c, id);
}
        
static public int jsubplot(int rows, int cols, int focusSubPlot)  {
     jsubplot2D(rows, cols, focusSubPlot);
     return currentFigId;
}

// creates a Matlab-like placement of multiple subplots in the current Figure with index currentFigId
// returns the id of the figure object
 static public int jsubplot2D(int rows, int cols, int focusSubPlot)  {
         initjplots();
         int currentPlotId = currentFigId-1;
         if (currentPlotId == -1)
             currentPlotId = 0;
         if (allFrames[currentPlotId] == null) {  // create new 2D-figure panel
                   // create new figure
                jPlot [] [] newPlot2DPanel = new jPlot[rows][cols];
                for (int ni=0; ni<rows; ni++) {   // initialize the new plot panel
                    for (int mi=0; mi<cols; mi++)
                        newPlot2DPanel[ni][mi] = new jPlot();
                }
             
                String figTitle = "jFigure "+(currentPlotId+1);
                JFrame currentFrame = new JFrame(figTitle);
                currentFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jclose(currentFigId);
                }
         });
            
                currentFrame.setLayout(new GridLayout(rows, cols)); 
                for (int ni=0; ni<rows; ni++)  {  // initialize the new plot panel
                    for (int mi=0; mi<cols; mi++)
                        currentFrame.add(newPlot2DPanel[ni][mi].getPanel(newPlot2DPanel[ni][mi]));   // add the subpanels corresponding to subplots
                }
             
                allPlots[currentPlotId] = newPlot2DPanel;
               allFrames[currentPlotId] = currentFrame;

                // construct the new 2D plot frame 
                currentPlotCnt2D++;
                currentPlotCnt++;
                
         // create the objects of the current plot. These are reused at subsequent calls of subplot2D()
                int rowNo = (int)(focusSubPlot / cols);
		if (rowNo*cols == focusSubPlot)  rowNo--;

		int colNo = (int)(focusSubPlot - rowNo*cols) - 1;
                currentPlot = newPlot2DPanel[rowNo][colNo];
                
                currentFrame.setSize(defaultXSize, defaultYSize);
                currentFrame.setVisible(true);

       }
          else {  // either focus on the requested plot if the subplot grid structure matches,
                    // or otherwise create a new subplot structure
             jPlot [][] currentPlot2DPanel = (jPlot [][]) allPlots[currentPlotId];
             jPlot [] [] newPlot2DPanel = currentPlot2DPanel;

             int yGrid = currentPlot2DPanel.length;
             int xGrid = currentPlot2DPanel[0].length;
             if (yGrid != rows || xGrid != cols)   {    // a grid of different size is requested
                 newPlot2DPanel = new jPlot[rows][cols];

                 // create new figure
                for (int ni=0; ni<rows; ni++)    // initialize the new plot panel
                    for (int mi=0; mi<cols; mi++)
                        newPlot2DPanel[ni][mi] = new jPlot();

                 allPlots[currentPlotId] = newPlot2DPanel;   // keep the new configuration of the plotting grid
                 String figTitle = "jFigure "+(currentPlotId+1);
        
                JFrame currentFrame = new JFrame(figTitle);
                currentFrame.setLayout(new GridLayout(rows, cols)); 
                for (int ni=0; ni<rows; ni++)  {  // initialize the new plot panel
                    for (int mi=0; mi<cols; mi++)
                        currentFrame.add(newPlot2DPanel[ni][mi].getPanel(newPlot2DPanel[ni][mi]));   // add the subpanels corresponding to subplots
                }
                currentFrame.setSize(defaultXSize, defaultYSize); 
                currentFrame.setVisible(true);
                
                allFrames[currentPlotId] = currentFrame;
                
                
             }
             int rowNo = (int)(focusSubPlot / cols);
             if (rowNo*cols == focusSubPlot)  rowNo--;

             int colNo = (int)(focusSubPlot - rowNo*cols) - 1;
             currentPlot = newPlot2DPanel[rowNo][colNo];
         }

         return currentPlotId;
     }

public static String[] jplot(double []x)  {
    int len = x.length;
    double [] ax = new double[len];
    for (int k=0; k<len; k++)
        ax[k] = (double) k;
    return  jplot(ax, x);
}


public static String[] jplot(Vec  x)  {
    return  jplot(x.getv());
}

public static String[] jplot(double [][]x)  {
    double [] x0 = x[0];
    double [] x1 = x[1];
    return jplot(x0, x1);
}


public static String[] jplot(double []x, double [] y, double [] ... args) 
   {
   if (currentPlot == null)      currentPlot = jfigure();
   return currentPlot.jplot(x, y, args);
        }
        
 public static String[] jplot(Vec x, Vec y) {
   if (currentPlot == null)      currentPlot = jfigure();
   return currentPlot.jplot(x.getv(), y.getv());
        }
        
   public static JFrame  jplot(String pieChartName, String[] categories, double [] values) 
       {
           DefaultPieDataset  data = new DefaultPieDataset();
           for (int k=0; k<categories.length; k++)
            data.setValue(categories[k], new Double(values[k]));
         JFreeChart pieChart = ChartFactory.createPieChart(pieChartName, data, false, false, false);
         pieChart.setTitle("Category Plot "+pieChartName);
         JFrame pieFrame = new ChartFrame(pieChartName, pieChart);
         pieFrame.pack();
         pieFrame.setVisible(true);
         if (currentPlot != null) {
             currentPlot.m_chart = pieChart;
         }
         
         return pieFrame;
       
   }
   
public static String[] jplot(double [][]x, String lineSpec, Object... args)  {
    double [] x0 = x[0];
    double [] x1 = x[1];
    return jplot(x0, x1, lineSpec, args);
}
 
  public static String[] jplot(double x[], double y[], String lineSpec, Object... args) {
        if (currentPlot == null)       currentPlot = jfigure();
   return currentPlot.jplot(x, y, lineSpec, args);
    }
  
   
  public static String[] jplot(Vec  x, String lineSpec, Object... args) {
        if (currentPlot == null)       currentPlot = jfigure();
       int len = x.size();
    double [] ax = new double[len];
    for (int k=0; k<len; k++)
        ax[k] = (double) k;

        return currentPlot.jplot(ax, x.getv(), lineSpec, args);
    }
  
        
public static String[] jplot(double [][]x, String lineSpec, String legend, Object... args)  {
    double [] x0 = x[0];
    double [] x1 = x[1];
    return jplot(x0, x1, lineSpec, legend, args);
}
 
    public static String[] jplot(double x[], Vec  y, String lineSpec, String legend, Object... args) {
        if (currentPlot == null)       currentPlot = jfigure();
   return currentPlot.jplot(x, y.getv(), lineSpec, legend, args);
    } 
   
public static String[] jplot(Vec  x, Vec  y, String lineSpec, String legend, Object... args) {
        if (currentPlot == null)      currentPlot = jfigure();
   return currentPlot.jplot(x.getv(), y.getv(), lineSpec, legend, args);
    } 
   
public static String[] jplot(Vec  x, double []  y, String lineSpec, String legend, Object... args) {
        if (currentPlot == null)      currentPlot = jfigure();
   return currentPlot.jplot(x.getv(), y, lineSpec, legend, args);
    } 

public static String jplot(double x[], double []  y, Paint color, Shape  marker, float [] style,  String legend) {
        if (currentPlot == null)      currentPlot = jfigure();
   return currentPlot.jplot(x, y, color, marker, style, legend);
    } 

public static String jplot(double x[],  Paint color, Shape  marker, float [] style,  String legend) {
        if (currentPlot == null)      currentPlot = jfigure();
    int len = x.length;
    double [] ax = new double[len];
    for (int k=0; k<len; k++)
        ax[k] = (double) k;

    return currentPlot.jplot(ax, x, color, marker, style, legend);
    } 


public static String jplot(Vec  x,  Paint color, Shape  marker, float [] style,  String legend) {
    return currentPlot.jplot( x.getv(), color, marker, style, legend);
    } 

      
public static String jplot(double [][]x, Paint color, Shape marker, float [] style, String legend)  {
    double [] x0 = x[0];
    double [] x1 = x[1];
    return jplot(x0, x1, color, marker, style, legend);
}
 
 public static void jlabel(AxisEnum axis, String label) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLabel(axis, label);
 }
 
 public static void jtitle( String title ) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setTitle(title);
 }
 
  public static void jbackground( Paint color ) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setBackground(color);
  }
  
  public static void jgridColor( Paint color ) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setGridColor(color);
 }
   
  public static void jlineVisibility(int lineIndex, boolean isLineVisible, boolean isShapeVisible) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLineVisibility(lineIndex, isLineVisible, isShapeVisible);
 }
  
    public static void jlineVisibility(String lineId, boolean isLineVisible, boolean isShapeVisible) {
        if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLineVisibility(lineId, isLineVisible, isShapeVisible);
 }
    
public static void jlineColor(int lineIndex, Paint linePaint)  {
           if (currentPlot == null)    currentPlot = jfigure();
           currentPlot.setLineColor(lineIndex, linePaint);
      }
  
public static void jlineColor(String lineId, Paint linePaint)  {
           if (currentPlot == null)   currentPlot = jfigure();
           currentPlot.setLineColor(lineId, linePaint);
      }
  
public static void jlineStyle(int lineIndex, Shape marker, int lineWidth) {
          if (currentPlot == null)     currentPlot = jfigure();
         currentPlot.setLineStyle(lineIndex, marker, lineWidth);
}
       
public static void jlineStyle(String lineId, Shape marker, int lineWidth) {
          if (currentPlot == null)      currentPlot = jfigure();
         currentPlot.setLineStyle(lineId, marker, lineWidth);
}
 
public static void jlineStyle(int lineIndex, Shape marker, int lineWidth, float[] style) {
 if (currentPlot == null)      currentPlot = jfigure();
  currentPlot.setLineStyle(lineIndex, marker, lineWidth, style);        
}
  
public static void jlineStyle(String lineId, Shape marker, int lineWidth, float[] style) {
    if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLineStyle(lineId, marker, lineWidth, style);        
}
        
public static void jlineSpec(int lineIndex, String lineSpec, int lineWidth) {
    if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLineSpec(lineIndex, lineSpec, lineWidth);
}

public static void jlineSpec(String lineId, String lineSpec, int lineWidth) {
    if (currentPlot == null)      currentPlot = jfigure();
   currentPlot.setLineSpec(lineId, lineSpec, lineWidth);
}

 public static void jTickUnit(AxisEnum axis, double delta) {
    if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.setTickUnit(axis, delta);
  }
 
public static void jaddAnnotation(double x, double y, String annotation) {
        if (currentPlot == null)      currentPlot = jfigure();
        currentPlot.addAnnotation(x, y, annotation);
 }

 public static void jaddAnnotation(int lineIndex, int pointIdx, String annotation) {
        if (currentPlot == null)      currentPlot = jfigure();
        currentPlot.addAnnotation(lineIndex, pointIdx, annotation);
 }

 
public static void jaddAnnotation(String lineId, int pointIdx, String annotation) {
        if (currentPlot == null)      currentPlot = jfigure();
        currentPlot.addAnnotation(lineId, pointIdx, annotation);
 }

public static void jaddAnnotation(int lineIndex, int pointIdx, String annotation, float angle) {
     if (currentPlot == null)      currentPlot = jfigure();
     currentPlot.addAnnotation(lineIndex, pointIdx, annotation, angle);
}

 public static void jaddAnnotation(String  lineId, int pointIdx, String annotation, float angle) {
     if (currentPlot == null)      currentPlot = jfigure();
      currentPlot.addAnnotation(lineId, pointIdx, annotation, angle);
}

public static void jlegendPosition(RectangleEdge position) {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.setLegendPosition(position);
}

public static void jaddMarker(AxisEnum axis, double position)  {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.addMarker(axis, position);
}


public static void jaddMarker(AxisEnum axis, double position, Paint paint)  {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.addMarker(axis, position, paint);
}

public static void jaddMarker(AxisEnum axis, double position, Paint paint, int width)  {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.addMarker(axis, position, paint, width);
}

public static void jaddMarker(AxisEnum axis, double position, Paint paint, int width, float [] style)  {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.addMarker(axis, position, paint, width, style);
}

 public static void jaxisRange(AxisEnum axis, double min, double max) {
   if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.setAxisRange(axis, min, max);
 }

public static void jhold(boolean isHoldOn) {
     if (currentPlot == null)      currentPlot = jfigure();
    currentPlot.setHold(isHoldOn);
 }
 
 public static boolean jtoggleHold() {
     if (currentPlot == null)      currentPlot = jfigure();
    return currentPlot.toggleHold();
 }
  
public static boolean jgetHold() {
     if (currentPlot == null)      currentPlot = jfigure();
    return currentPlot.getHold();
 }
    
 public static  String jaddPlot(double x[], double y[], String lineSpec) {
    if (currentPlot == null)      currentPlot = jfigure();
    return currentPlot.addPlot(x, y, lineSpec);
  }
  
public static  String jaddPlot(double x[], double y[], String lineSpec, String legend) {
    if (currentPlot == null)      currentPlot = jfigure();
    return currentPlot.addPlot(x, y, lineSpec, legend);
  }
    
 public static void javeAsPNG(String fileName, int width, int height) throws IOException {
         if (currentPlot == null)      currentPlot = jfigure();
 currentPlot.saveAsPNG(fileName, width, height);
  }

 
// closes all the available figure objects
 public static  void jcloseAll()   {
            if (allFrames != null)
             for (int figId=0; figId<maxNumberOfFigs; figId++)
                 if (allFrames[figId] != null)
                    jclose(figId);
            allFrames = null;
        }


public static void jclose(String all) {
    if (all.equalsIgnoreCase("all"))
        jcloseAll();
}

// close an explicitly requested figure id
   // it focuses automatically on the previous figure object which it returns
   static public int jclose(int figId) {
        int freeSlot = -1;    // find next free s;ot to use
            
        if (allFrames != null) {   // plot system inited
         JFrame closingFrameView = allFrames[figId];
             // find next usable plot

          if (closingFrameView != null)     {   // figure frame exists
           int len1 = allPlots[figId][0].length;
           int len2 = allPlots[figId].length;
           closingFrameView.dispose();
 
            for (int i=0; i<len2; i++)
                for (int j=0; j<len1; j++)
                    allPlots[figId][i][j] = null;

            allPlots[figId] = null;  
            allFrames[figId] = null;
            currentPlotCnt--;   // one less plot

              boolean  atLeastOnefigureRemains = false;
              for (int figSlot = 0; figSlot < maxNumberOfFigs; figSlot++)  {  // scan all possible slots
                  if (allFrames[figSlot] != null)  {   // we have found an unused slot for focusing figure
                      atLeastOnefigureRemains = true;
                      freeSlot = figSlot;
                      break;
                 }
                
            if (atLeastOnefigureRemains == true)   {   // focus on the detected "survived" figure object
              jPlot [][] currentPlotPanel = (jPlot[][]) allPlots[freeSlot];
              currentPlot = currentPlotPanel[0][0];    // make the first subplot of the figure object as the current one
              currentPlot.getPanel().repaint();
            }
            else
                currentPlot = null;   // if figures do not remain create one on next plot
              }
          }   // figure frame exists
        }  // plot system inited
            return  freeSlot;   // return index of the next free slot
        
   }


 }
    
  
