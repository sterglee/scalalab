package scalaSci.math.plot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import scalaSci.math.plot.canvas.Plot2DCanvas;
import scalaSci.math.plot.canvas.PlotCanvas;
import scalaSci.math.plot.components.LegendPanel;
import scalaSci.math.plot.components.PlotToolBar;
import scalaSci.math.plot.plotObjects.Axis;
import scalaSci.math.plot.plotObjects.Plotable;
import scalaSci.math.plot.plots.Plot;


 import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
 import org.apache.xmlgraphics.java2d.GraphicContext;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;


// this class extends the JPanel class in order to cope with plots
public abstract class PlotPanel extends JPanel {
        public FrameView  holdingFrameView = null;  // the FrameView that holds the current plot
        public PlotToolBar plotToolBar;  // each PlotPanel is associated with a toolbar
        public PlotCanvas plotCanvas;
        public LegendPanel plotLegend;
        public final static String EAST = BorderLayout.EAST;
        public final static String SOUTH = BorderLayout.SOUTH;
        public final static String NORTH = BorderLayout.NORTH;
        public final static String WEST = BorderLayout.WEST;
        public final static String INVISIBLE = "INVISIBLE";
// the plot types
        public final static String SCATTER = "SCATTER";
        public final static String LINE = "LINE";
        public final static String BAR = "BAR";
        public final static String HISTOGRAM = "HISTOGRAM";
        public final static String BOX = "BOX";
        public final static String STAIRCASE = "STAIRCASE";
        public final static String GRID = "GRID";
        public final static Color[] COLORLIST = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA };
 
         // identifiers for x and y axis
        public final static int xAxisId = 0;  
        public final static int yAxisId = 1; 
        public final static int zAxisId = 2;
        
        static PlotPanel  currentPanel;
        
        public PlotPanel(PlotCanvas _canvas, String legendOrientation) {
                             
	plotCanvas = _canvas;
	setBackground(Color.WHITE);
	setLayout(new BorderLayout());

	addPlotToolBar(NORTH);

	addLegend(legendOrientation);

	add(plotCanvas, BorderLayout.CENTER);
                  setOpaque(false);
    }

     
        
            public PlotPanel(Plot2DPanel [][]  panels, String legendOrientation) {
	  int nPanelsX = panels.length;
                    int nPanelsY = panels[0].length;
                    currentPanel = panels[0][0];   // use this panel as the current one
	  setBackground(Color.WHITE);
       // panels are arranged  with a GridLayout within plot panels                    
                    JPanel  plotsPanel = new JPanel(new GridLayout(nPanelsX, nPanelsY));
                    for (int x=0; x<nPanelsX; x++)
                        for (int y=0; y<nPanelsY; y++)  
                            plotsPanel.add(panels[x][y]); 
            
                    PlotPanel firstPanel = panels[0][0];
                    plotCanvas = firstPanel.plotCanvas;  // set plotting canvas to draw to the first plot canvas
            
                    setLayout(new BorderLayout());
                    add(plotsPanel, BorderLayout.CENTER);
                    setOpaque(false);
    }


          public PlotPanel(Plot3DPanel [][] panels, String legendOrientation) {
                    int nCanvasX = panels.length;
                    int nCanvasY = panels[0].length;
                    currentPanel = panels[0][0];   // use this panel as the current one
	  setBackground(Color.WHITE);
            JPanel  plotsPanel = new JPanel(new GridLayout(nCanvasX, nCanvasY));
            for (int x=0; x<nCanvasX; x++)
                for (int y=0; y<nCanvasY; y++)  
                    plotsPanel.add(panels[x][y]); 
                
            PlotPanel firstPanel = panels[0][0];
            plotCanvas = firstPanel.plotCanvas;
            
            setLayout(new BorderLayout());
            add(plotsPanel, BorderLayout.CENTER);
            setOpaque(false);
	}

        
     public PlotPanel(PlotCanvas _canvas) {
        this(_canvas, INVISIBLE);  // legend will be invisible
  }

      
	/**
	 * Defines where the legend of the plot should be added to the plot 
	 * panel.
	 *
	 * @param location Location where should be put the legend (String).
	 * location can have the following values (case insensitive): EAST,
	 * SOUTH, WEST, NORTH, INVISIBLE (legend will be hidden in this case).
	 * Any other value will be ignored and an error message will be sent to
	 * the error output.
	 */
    public void addLegend(String location) {
        if (location.equalsIgnoreCase(EAST)) {
	plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
	add(plotLegend, EAST);
        } else if (location.equalsIgnoreCase(SOUTH)) {
	plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
	add(plotLegend, SOUTH);
        } else if (location.equalsIgnoreCase(WEST)) {
	plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
	add(plotLegend, WEST);
        } else if (location.equalsIgnoreCase(NORTH)) {
	plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
	add(plotLegend, NORTH);
        } else if (location.equalsIgnoreCase(INVISIBLE)) {
	plotLegend = new LegendPanel(this, LegendPanel.INVISIBLE);
			
	} else
        System.err.println("Orientation " + location + " is unknown.");
}

    public void addLegend(String location, Plot2DPanel commentedPanel) {
        if (location.equalsIgnoreCase(EAST)) {
	plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
	commentedPanel.add(plotLegend, EAST);
        } else if (location.equalsIgnoreCase(SOUTH)) {
	plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
	commentedPanel.add(plotLegend, SOUTH);
        } else if (location.equalsIgnoreCase(WEST)) {
	plotLegend = new LegendPanel(this, LegendPanel.VERTICAL);
	add(plotLegend, WEST);
        } else if (location.equalsIgnoreCase(NORTH)) {
	plotLegend = new LegendPanel(this, LegendPanel.HORIZONTAL);
	commentedPanel.add(plotLegend, NORTH);
        } else if (location.equalsIgnoreCase(INVISIBLE)) {
	plotLegend = new LegendPanel(this, LegendPanel.INVISIBLE);
	// add(legends, BorderLayout.NORTH);
	} else
           System.err.println("Orientation " + location + " is unknown.");
 }

	
	/**
	 * Removes the current legend from the plot panel.
	 */
	public void removeLegend() {
		remove(plotLegend);
	}

	/**
	 * Moves the legend to the specified location.
	 * 
	 * @param location Location where should be put the legend (String).
	 * location can have the following values (case insensitive): EAST,
	 * SOUTH, WEST, NORTH, INVISIBLE (legend will be hidden in this case).
	 * Any other value will be ignored and an error message will be sent to
	 * the error output.
	 */
                public void setLegendOrientation(String location) {
                    removeLegend();
                    addLegend(location);
	}

	/**
	 * Adds a new plot toolbar to the specified location. The previous toolbar 
	 * is deleted.
	 * @param location Location where should be put the toolbar (String).
	 * location can have the following values (case insensitive): EAST,
	 * SOUTH, WEST, NORTH.
	 * Any other value will be ignored and an error message will be sent to
	 * the error output.
	 */
	public void addPlotToolBar(String location) {
		if (location.equalsIgnoreCase(EAST)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(this);
			plotToolBar.setFloatable(false);
			add(plotToolBar, EAST);
		} else if (location.equalsIgnoreCase(SOUTH)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(this);
			plotToolBar.setFloatable(false);
			add(plotToolBar, SOUTH);
		} else if (location.equalsIgnoreCase(WEST)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(this);
			plotToolBar.setFloatable(false);
			add(plotToolBar, WEST);
		} else if (location.equalsIgnoreCase(NORTH)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(this);
			plotToolBar.setFloatable(false);
			add(plotToolBar, NORTH);
		} else
			System.err.println("Location " + location + " is unknown.");
	}

        public void addPlotToolBar(String location, Plot2DPanel controledPanel) {
		if (location.equalsIgnoreCase(EAST)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(controledPanel);
			plotToolBar.setFloatable(false);
			add(plotToolBar, EAST);
		} else if (location.equalsIgnoreCase(SOUTH)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(controledPanel);
			plotToolBar.setFloatable(false);
			add(plotToolBar, SOUTH);
		} else if (location.equalsIgnoreCase(WEST)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(controledPanel);
			plotToolBar.setFloatable(false);
			add(plotToolBar, WEST);
		} else if (location.equalsIgnoreCase(NORTH)) {
			removePlotToolBar();
			plotToolBar = new PlotToolBar(this);
			plotToolBar.setFloatable(false);
			add(plotToolBar, NORTH);
		} else
			System.err.println("Location " + location + " is unknown.");
	}

	
	/**
	 * Removes the plot toolbar from the panel.
	 */
	public void removePlotToolBar() {
		if (plotToolBar == null)
			return;
		remove(plotToolBar);
	}

	/**
	 * Moves the plot toolbar to the specified location. 
	 * @param location Location where should be put the toolbar (String).
	 * location can have the following values (case insensitive): EAST,
	 * SOUTH, WEST, NORTH.
	 * Any other value will be ignored and an error message will be sent to
	 * the error output.
	 */
	public void setPlotToolBarOrientation(String location) {
		addPlotToolBar(location);
	}

	// ///////////////////////////////////////////
	// ////// set actions ////////////////////////
	// ///////////////////////////////////////////

	public void setActionMode(int am) {
		plotCanvas.setActionMode(am);
	}

	public void setNoteCoords(boolean b) {
		plotCanvas.setNoteCoords(b);
	}

	public void setEditable(boolean b) {
		plotCanvas.setEditable(b);
	}

	public boolean getEditable() {
		return plotCanvas.getEditable();
	}

	public void setNotable(boolean b) {
		plotCanvas.setNotable(b);
	}

	public boolean getNotable() {
		return plotCanvas.getNotable();
	}

	// ///////////////////////////////////////////
	// ////// set/get elements ///////////////////
	// ///////////////////////////////////////////

	public LinkedList<Plot> getPlots() {
		return plotCanvas.getPlots();
	}

	public Plot getPlot(int i) {
		return plotCanvas.getPlot(i);
	}

	public int getPlotIndex(Plot p) {
		return plotCanvas.getPlotIndex(p);
	}

	public LinkedList<Plotable> getPlotables() {
		return plotCanvas.getPlotables();
	}

	public Plotable getPlotable(int i) {
		return plotCanvas.getPlotable(i);
	}

	/**
	 * Return the axis specified in parameter.
	 * @param i Axis number. 0 for X, 1 for Y, 2 for Z.
	 * @return The axis which number is given in parameter.
	 */
	public Axis getAxis(int i) {
		return plotCanvas.getGrid().getAxis(i);
	}

	/**
	 * Returns the scaling for all of the axis of the plot. 
	 * @return  An array of String 
	 * 
	 */
	public String[] getAxisScales() {
		return plotCanvas.getAxisScales();
	}

	//	 TODO axes labels are rested after addPlot... correct this.
	/**
	 * Sets the name of the axis, in this order: X, Y and Z.
	 * @param labels One to three strings containing the name of each axis.
	 */
	public void setAxisLabels(String... labels) {
		plotCanvas.setAxisLabels(labels);
	}

	/**
	 * Sets the name of the axis specified in parameter.
	 * @param axe Axis number. 0 for X, 1 for Y, 2 for Z.
	 * @param label Name to be given.
	 */
	public void setAxisLabel(int axe, String label) {
		plotCanvas.setAxisLabel(axe, label);
	}

	/**
	 * Sets the scale of the axes, linear or logarithm, in this order: X,Y,Z.
	 * @param scales Strings containing the scaling, LOG or LIN (case insensitive) for the axes.
	 */
	public void setAxisScales(String... scales) {
		plotCanvas.setAxisScales(scales);
	}

	/**
	 * Sets the scaling of the specified axis.
	 * @param axe Axis number. 0 for X, 1 for Y, 2 for Z.
	 * @param scale String specifying the scaling. LIN or LOG, case insensitive.
	 */
	public void setAxisScale(int axe, String scale) {
		plotCanvas.setAxiScale(axe, scale);
	}

	/**
	 * Sets the boundaries for each axis.
	 * @param min Array of at most 3 doubles specifying the min bound of each axis, in this order: X,Y,Z.
	 * @param max Array of at most 3 doubles specifying the max bound of each axis, in this order: X,Y,Z.
	 */
	public void setFixedBounds(double[] min, double[] max) {
		plotCanvas.setFixedBounds(min, max);
	}

	/**
	 * Sets the boundaries for the specified axis.
	 * @param axe Axis number to modify. 0 for X, 1 for Y, 2 for Z.
	 * @param min Min bound of the axis.
	 * @param max Max bound of the axis.
	 */
	public void setFixedBounds(int axe, double min, double max) {
		plotCanvas.setFixedBounds(axe, min, max);
	}

	/**
	 * Modify bounds of the axes so as to include the point given in parameter.
	 * @param into Coords of the point to include in bounds.
	 */
	public void includeInBounds(double... into) {
		plotCanvas.includeInBounds(into);
	}

	/**
	 * Modify axes boundaries so as to include all the points of a given plot.
	 * @param plot Plot to include.
	 */
	public void includeInBounds(Plot plot) {
		plotCanvas.includeInBounds(plot);
	}

	/**
	 * Set bounds automatically.
	 */
	public void setAutoBounds() {
		plotCanvas.setAutoBounds();
	}

	/**
	 * Set bounds automatically for one axis.
	 * @param axe Number of the axis to modify. 0 for X, 1 for Y, 2 for Z.
	 */
	public void setAutoBounds(int axe) {
		plotCanvas.setAutoBounds(axe);
	}

	public double[][] mapData(Object[][] stringdata) {
		return plotCanvas.mapData(stringdata);
	}

	public void resetMapData() {
		plotCanvas.resetMapData();
	}

	// ///////////////////////////////////////////
	// ////// add/remove elements ////////////////
	// ///////////////////////////////////////////

	public void addLabel(String text, Color c, double... where) {
		plotCanvas.addLabel(text, c, where);
	}

	public void addBaseLabel(String text, Color c, double... where) {
		plotCanvas.addBaseLabel(text, c, where);
	}

	public void addPlotable(Plotable p) {
		plotCanvas.addPlotable(p);
	}

	public void removePlotable(Plotable p) {
		plotCanvas.removePlotable(p);
	}

	public void removePlotable(int i) {
		plotCanvas.removePlotable(i);
	}

	public int addPlot(Plot newPlot) {
		return plotCanvas.addPlot(newPlot);
	}

	protected Color getNewColor() {
		return COLORLIST[plot.currentPlotCnt % COLORLIST.length];
	}

	public int addPlot(String type, String name, double[]... v) {
		return addPlot(type, name, getNewColor(), v);
	}

	public abstract int addPlot(String type, String name, Color c, double[]... v);

	public void setPlot(int I, Plot p) {
		plotCanvas.setPlot(I, p);
	}

	public void changePlotData(int I, double[]... XY) {
		plotCanvas.changePlotData(I, XY);
	}

	public void changePlotName(int I, String name) {
		plotCanvas.changePlotName(I, name);
	}

	public void changePlotColor(int I, Color c) {
		plotCanvas.changePlotColor(I, c);
	}

	public void removePlot(int I) {
		plotCanvas.removePlot(I);
	}

	public void removePlot(Plot p) {
		plotCanvas.removePlot(p);
	}

	public void removeAllPlots() {
		plotCanvas.removeAllPlots();
	}

	public void addVectortoPlot(int numPlot, double[][] v) {
		plotCanvas.addVectortoPlot(numPlot, v);
	}
   
        public int addScalogramPlot(String name, double [][] XY)  {
       return ((Plot2DCanvas) plotCanvas).addScalogramPlot(name, getNewColor(), XY);
        }
        
        
	public void addQuantiletoPlot(int numPlot, int numAxe, double rate, boolean symetric, double[] q) {
		plotCanvas.addQuantiletoPlot(numPlot, numAxe, rate, symetric, q);
	}

	public void addQuantiletoPlot(int numPlot, int numAxe, double rate, boolean symetric, double q) {
		plotCanvas.addQuantiletoPlot(numPlot, numAxe, rate, symetric, q);
	}

	public void addQuantilestoPlot(int numPlot, int numAxe, double[][] q) {
		plotCanvas.addQuantilestoPlot(numPlot, numAxe, q);
	}

	public void addQuantilestoPlot(int numPlot, int numAxe, double[] q) {
		plotCanvas.addQuantilestoPlot(numPlot, numAxe, q);
	}

	public void addGaussQuantilestoPlot(int numPlot, int numAxe, double[] s) {
		plotCanvas.addGaussQuantilestoPlot(numPlot, numAxe, s);
	}

	public void addGaussQuantilestoPlot(int numPlot, int numAxe, double s) {
		plotCanvas.addGaussQuantilestoPlot(numPlot, numAxe, s);
	}

public void toGraphicFile(File file) throws IOException {
		// otherwise toolbar appears
        plotToolBar.setVisible(false);

        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null);
        g.dispose();

// make it reappear
      plotToolBar.setVisible(true);

  try {
   ImageIO.write((RenderedImage) bufferedImage, "png", file);
 } catch (IllegalArgumentException ex) {
  }
}

        
public void toGraphicJPGFile(File file) throws IOException {
		// otherwise toolbar appears
        plotToolBar.setVisible(false);

        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null);
        g.dispose();

// make it reappear
      plotToolBar.setVisible(true);

  try {
   ImageIO.write((RenderedImage) bufferedImage, "jpg", file);
 } catch (IllegalArgumentException ex) {
  }
}
        
        
public void toGraphicBMPFile(File file) throws IOException {
		// otherwise toolbar appears
        plotToolBar.setVisible(false);

        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null);
        g.dispose();

// make it reappear
      plotToolBar.setVisible(true);

  try {
   ImageIO.write((RenderedImage) bufferedImage, "bmp", file);
 } catch (IllegalArgumentException ex) {
  }
}
        
 public void toEPSGraphicFile(File file, int width, int height) throws IOException {
            FileOutputStream  fos = new FileOutputStream(file);
		
            // otherwise toolbar appears
            plotToolBar.setVisible(false);

EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
g2d.setGraphicContext(new GraphicContext());
g2d.setupDocument(fos,  width, height);


Image image = createImage(getWidth(), getHeight());
paint(image.getGraphics());
image = new ImageIcon(image).getImage();

g2d.drawImage(image, 0, 0, Color.WHITE, null);

g2d.finish();
        // make it reappear
plotToolBar.setVisible(true);

        
}

        public void toPDFGraphicFile(File file, int width, int height) throws IOException {
		// otherwise toolbar appears
  plotToolBar.setVisible(false);
                
  com.lowagie.text.Document  document = new com.lowagie.text.Document();
  document.setPageSize(new Rectangle(width, height));
  FileOutputStream  fos = new FileOutputStream(file);
	
   PdfWriter    writer = null;
            try {
                writer = PdfWriter.getInstance(document, fos);
            } catch (DocumentException ex) {
                Logger.getLogger(PlotPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
   document.open();
   PdfContentByte  cb = writer.getDirectContent();
   PdfTemplate  tp = cb.createTemplate(width, height);
   Graphics2D g2d = tp.createGraphics(width, height);

        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

   
    g2d.drawImage(image, 0, 0, Color.WHITE, null);
    g2d.dispose();
    cb.addTemplate(tp, 1, 0, 0, 1, 0, 0);
    
    document.close();
        // make it reappear
plotToolBar.setVisible(true);
        }

        
   /*   these methods allow the handling of axis by retrieving first a PlotPanel reference.
    t = inc(0, 0.01, 20);
         x = sin(0.2*t)+5*cos(0.23*t);;
         fig = figure(1); plot(t,y);
         fig.xlabel("Time axis t"); */
        
        public void xlabel(String xLabelStr) {
            setAxisLabel(scalaSci.math.plot.PlotPanel.xAxisId, xLabelStr);
        }
        
        public void ylabel(String yLabelStr) {
            setAxisLabel(scalaSci.math.plot.PlotPanel.yAxisId, yLabelStr);
        }
        
        public void zlabel(String zLabelStr) {
            if (this instanceof  Plot3DPanel) {
               setAxisLabel(scalaSci.math.plot.PlotPanel.zAxisId, zLabelStr);
            }
        }
        
        
public void title(String titleStr)  {
    int plotCnt = plot.currentPlotCnt-1;
    if (plotCnt < 0 ) 
         plotCnt = 0;
    FrameView currentView = plot.allFrames[plotCnt];
    if (currentView != null)
        currentView.setTitle(titleStr);
}
        
        public static void main(String[] args) {
            
            int n=2; int m=3;
            try {
            Plot2DPanel [] [] mySubplots = new Plot2DPanel[n][m];
            for (int ni=0; ni<n; ni++)  
                for (int mi=0; mi<m; mi++)   
                      mySubplots[ni][mi] = new Plot2DPanel();
            
            Plot2DPanel  mSinePlot = mySubplots[0][0];
            
            int np=2000;
            double SF = 0.012;
            double [] x = new double [np];
            double [] t = new double [np];
            for (int k=0; k<np; k++) {
                t[k] = SF*k;
                x[k] = Math.sin(t[k]);
            }
            mSinePlot.addLinePlot("Sine Plot", new Color(0, 200, 0), t, x);
            
             Plot2DPanel  mTwoSinesPlot = mySubplots[1][1];
            
            double SF2 = 0.018;
            double [] y = new double [np];
            for (int k=0; k<np; k++) {
                y[k] = x[k]+Math.sin(x[k]*SF2*t[k]);
            }
            mTwoSinesPlot.addLinePlot("Complex Sines Plot", new Color(0, 0, 200), t, y);
            
            mySubplots[1][0].addBarPlot("barplot", new Color(0,50, 50), t, y);
                    
            
            Plot2DPanel p = new Plot2DPanel(mySubplots, PlotPanel.NORTH);
            FrameView f = new FrameView(p);
	    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
            
            
            
            try {
            Plot3DPanel [] [] mySubplots3D = new Plot3DPanel[m][n];
            for (int ni=0; ni<n; ni++)  
                for (int mi=0; mi<m; mi++)   
                      mySubplots3D[ni][mi] = new Plot3DPanel();
            
            Plot3DPanel  mySine3Plot = mySubplots3D[0][0];
            
            int np=2000;
            double SF = 0.012;
            double [] x = new double [np];
            double [] t = new double [np];
            double [] y = new double [np];
            for (int k=0; k<np; k++) {
                t[k] = SF*k;
                x[k] = Math.sin(t[k]);
                y[k] = x[k] +t[k]*Math.random();
            }
            mySine3Plot.addLinePlot("Sine3 Plot", new Color(0, 200, 0), t, x, y);
            
            
            Plot3DPanel p = new Plot3DPanel( mySubplots3D, PlotPanel.NORTH);
            		FrameView f = new FrameView(p);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
            
            
            
		/* String man = "Usage: jplot.<sh|bat> <-2D|-3D> [-l <INVISIBLE|NORTH|SOUTH|EAST|WEST>] [options] <ASCII file (n rows, m columns)> [[options] other ASCII file]\n"
				+ "[-l <INVISIBLE|NORTH|SOUTH|EAST|WEST>] giving the legend position\n"
				+ "[options] are:\n"
				+ "  -t <SCATTER|LINE|BAR|HISTOGRAM2D(<integer h>)|HISTOGRAM3D(<integer h>,<integer k>)|GRID3D|CLOUD2D(<integer h>,<integer k>)|CLOUD3D(<integer h>,<integer k>,<integer l>)>    type of the plot\n"
				+ "      SCATTER|LINE|BAR: each line of the ASCII file contains coordinates of one point.\n"
				+ "      HISTOGRAM2D(<integer h>): ASCII file contains the 1D sample (i.e. m=1) to split in h slices.\n"
				+ "      HISTOGRAM3D(<integer h>,<integer k>): ASCII file contains the 2D sample (i.e. m=2) to split in h*k slices (h slices on X axis and k slices on Y axis).\n"
				+ "      GRID3D: ASCII file is a matrix, first row gives n X grid values, first column gives m Y grid values, other values are Z values.\n"
				+ "      CLOUD2D(<integer h>,<integer k>): ASCII file contains the 2D sample (i.e. m=2) to split in h*k slices (h slices on X axis and k slices on Y axis), density of cloud corresponds to frequency of X-Y slice in given 2D sample.\n"
				+ "      CLOUD3D(<integer h>,<integer k>,<integer l>): ASCII file contains the 3D sample (i.e. m=3) to split in h*k*l slices (h slices on X axis, k slices on Y axis, l slices on Y axis), density of cloud corresponds to frequency of X-Y-Z slice in given 3D sample.\n"
				+ "  -n name    name of the plot\n"
				+ "  -v <ASCII file (n,3|2)>    vector data to add to the plot\n"
				+ "  -q<X|Y|Z>(<float Q>) <ASCII file (n,1)>    Q-quantile to add to the plot on <X|Y|Z> axis. Each line of the given ASCII file contains the value of quantile for probvability Q.\n"
				+ "  -qP<X|Y|Z> <ASCII file (n,p)>    p-quantiles density to add to the plot on <X|Y|Z> axis. Each line of the given ASCII file contains p values.\n"
				+ "  -qN<X|Y|Z> <ASCII file (n,1)>    Gaussian density to add to the plot on <X|Y|Z> axis. Each line of the given ASCII file contains a standard deviation.";

		if (args.length == 0) {
			double[][] data = new double[20][];
			for (int i = 0; i < data.length; i++)
				data[i] = new double[] { Math.random(), Math.random(), Math.random() };
			ASCIIFile.writeDoubleArray(new File("tmp.dat"), data);

			args = new String[] { "-3D", "-l", "SOUTH", "-t", "SCATTER", "tmp.dat" };
			System.out.println(man);
			System.out.println("\nExample: jplot.<sh|bat> " + Array.cat(args));
		}

		PlotPanel p = null;
		if (args[0].equals("-2D"))
			p = new Plot2DPanel();
		else if (args[0].equals("-3D"))
			p = new Plot3DPanel();
		else
			System.out.println(man);

		try {

			String leg = "INVISIBLE";
			String type = SCATTER;
			String name = "";

			double[][] v = null;

			double[] qX = null;
			double[] qY = null;
			double[] qZ = null;
			double qXp = 0;
			double qYp = 0;
			double qZp = 0;

			double[][] qPX = null;
			double[][] qPY = null;
			double[][] qPZ = null;

			double[] qNX = null;
			double[] qNY = null;
			double[] qNZ = null;

			for (int i = 1; i < args.length; i++) {
				//System.out.println("<" + args[i] + ">");
				if (args[i].equals("-l")) {
					leg = args[i + 1];
					i++;
				} else if (args[i].equals("-t")) {
					type = args[i + 1];
					i++;
				} else if (args[i].equals("-n")) {
					name = args[i + 1];
					i++;
				} else if (args[i].equals("-v")) {
					v = ASCIIFile.readDoubleArray(new File(args[i + 1]));
					i++;
				} else if (args[i].startsWith("-qX(")) {
					qX = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					qXp = Double.parseDouble(args[i].substring(4, args[i].length() - 1));
					i++;
				} else if (args[i].startsWith("-qY(")) {
					qY = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					qYp = Double.parseDouble(args[i].substring(4, args[i].length() - 1));
					i++;
				} else if (args[i].startsWith("-qZ(")) {
					qZ = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					qZp = Double.parseDouble(args[i].substring(4, args[i].length() - 1));
					i++;
				} else if (args[i].equals("-qPX")) {
					qPX = ASCIIFile.readDoubleArray(new File(args[i + 1]));
					i++;
				} else if (args[i].equals("-qPY")) {
					qPY = ASCIIFile.readDoubleArray(new File(args[i + 1]));
					i++;
				} else if (args[i].equals("-qPZ")) {
					qPZ = ASCIIFile.readDoubleArray(new File(args[i + 1]));
					i++;
				} else if (args[i].equals("-qNX")) {
					qNX = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					i++;
				} else if (args[i].equals("-qNY")) {
					qNY = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					i++;
				} else if (args[i].equals("-qNZ")) {
					qNZ = ASCIIFile.readDouble1DArray(new File(args[i + 1]));
					i++;
				} else {
					File input_file = new File(args[i]);
					int n = 0;
					if (input_file.exists()) {
						if (name.length() == 0)
							name = input_file.getName();

						if (p instanceof Plot2DPanel) {
							Plot2DPanel p2d = (Plot2DPanel) p;
							if (type.equals("SCATTER"))
								n = p2d.addScatterPlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.equals("LINE"))
								n = p2d.addLinePlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.equals("BAR"))
								n = p2d.addBarPlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.startsWith("HISTOGRAM2D(")) {
								n = p2d
										.addHistogramPlot(name, ASCIIFile.readDouble1DArray(input_file), Integer
												.parseInt(type.substring(12, type.length() - 1)));
							} else if (type.startsWith("CLOUD2D(")) {
								n = p2d.addCloudPlot(name, ASCIIFile.readDoubleArray(input_file), Integer.parseInt(type.substring(8, type.indexOf(","))),
										Integer.parseInt(type.substring(type.indexOf(",") + 1, type.length() - 1)));
							} else
								p2d.addPlot(type, name, ASCIIFile.readDoubleArray(input_file));
						} else {
							Plot3DPanel p3d = (Plot3DPanel) p;
							if (type.equals("SCATTER"))
								n = p3d.addScatterPlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.equals("LINE"))
								n = p3d.addLinePlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.equals("BAR"))
								n = p3d.addBarPlot(name, ASCIIFile.readDoubleArray(input_file));
							else if (type.startsWith("HISTOGRAM3D(")) {
								n = p3d.addHistogramPlot(name, ASCIIFile.readDoubleArray(input_file), Integer.parseInt(type.substring(12, type.indexOf(","))),
										Integer.parseInt(type.substring(type.indexOf(",") + 1, type.length() - 1)));
							} else if (type.equals("GRID3D")) {
								n = p3d.addGridPlot(name, ASCIIFile.readDoubleArray(input_file));
							} else if (type.startsWith("CLOUD3D(")) {
								n = p3d.addCloudPlot(name, ASCIIFile.readDoubleArray(input_file), Integer.parseInt(type.substring(8, type.indexOf(","))),
										Integer.parseInt(type.substring(type.indexOf(",") + 1, type.indexOf(",", type.indexOf(",") + 1))), Integer
												.parseInt(type.substring(type.indexOf(",", type.indexOf(",") + 1) + 1, type.length() - 1)));
							} else
								p3d.addPlot(type, name, ASCIIFile.readDoubleArray(input_file));
						}

						if (v != null)
							p.addVectortoPlot(n, v);

						if (qX != null)
							p.addQuantiletoPlot(n, 0, qXp, false, qX);
						if (qY != null)
							p.addQuantiletoPlot(n, 1, qYp, false, qY);
						if (qZ != null)
							p.addQuantiletoPlot(n, 2, qZp, false, qZ);

						if (qPX != null)
							p.addQuantilestoPlot(n, 0, qPX);
						if (qPY != null)
							p.addQuantilestoPlot(n, 1, qPY);
						if (qPZ != null)
							p.addQuantilestoPlot(n, 2, qPZ);

						if (qNX != null)
							p.addGaussQuantilestoPlot(n, 0, qNX);
						if (qNY != null)
							p.addGaussQuantilestoPlot(n, 1, qNY);
						if (qNZ != null)
							p.addGaussQuantilestoPlot(n, 2, qNZ);

						type = "SCATTER";
						leg = "SOUTH";
						name = "";
						qX = null;
						qY = null;
						qZ = null;
						qXp = 0;
						qYp = 0;
						qZp = 0;

						v = null;

						qPX = null;
						qPY = null;
						qPZ = null;

						qNX = null;
						qNY = null;
						qNZ = null;

					} else {
						System.out.println("File " + args[i] + " unknown.");
						System.out.println(man);
					}
				}
			}
			p.setLegendOrientation(leg);
			FrameView f = new FrameView(p);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("\n" + man);
		} */
	}
}
