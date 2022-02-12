package scalaSci.math.plot.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import scalaSci.math.plot.components.DatasFrame;
import scalaSci.math.plot.components.LegendPanel;
import scalaSci.math.plot.components.SetScalesFrame;
import scalaSci.math.plot.plotObjects.Base;
import scalaSci.math.plot.plotObjects.BaseDependant;
import scalaSci.math.plot.plotObjects.BasePlot;
import scalaSci.math.plot.plotObjects.Plotable;
import scalaSci.math.plot.plots.Plot;
import scalaSci.math.plot.render.AbstractDrawer;

import java.awt.Container;
import java.awt.Font;
import javax.swing.JRadioButton;
import scalaSci.math.plot.PlotGlobals;
import scalaSci.math.plot.utils.PArray;
import static java.lang.Math.*;

import static scalaSci.math.plot.plotObjects.Base.*;


public  abstract class PlotCanvas extends JPanel implements MouseListener, MouseMotionListener, ComponentListener, BaseDependant, MouseWheelListener {
    public Base base;
    protected AbstractDrawer draw;     // the class that defines the drawing properties
    protected BasePlot grid;
    public LegendPanel linkedLegendPanel;     // the legend for this panel
    public LinkedList<Plot> plots;    // the plots on that canvas
    public LinkedList<Plotable> objects;   // the plotable objects for that canvas

	// ////// Constructor & inits ////////////////
    public PlotCanvas() {
        initPanel();
        initBasenGrid();
        initDrawer();
        setOpaque(false);
     }

public PlotCanvas(Base b, BasePlot bp) {
    initPanel();
    initBasenGrid(b, bp);
    initDrawer();
    setOpaque(false);
}

public PlotCanvas(double[] min, double[] max) {
    initPanel();
    initBasenGrid(min, max);
    initDrawer();
    setOpaque(false);
}

public PlotCanvas(double[] min, double[] max, String[] axesScales, String[] axesLabels) {
    initPanel();
    initBasenGrid(min, max, axesScales, axesLabels);
    initDrawer();
    setOpaque(false);
}

// attach a legend panel
public void attachLegend(LegendPanel lp) {
    linkedLegendPanel = lp;
}

private void initPanel() {
    objects = new LinkedList<>();
    plots = new LinkedList<>();
    setDoubleBuffered(true);
    setBackground(PlotGlobals.legendPanelBackgroundColor);
    addComponentListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    addMouseWheelListener(this);
}

public abstract void initDrawer();

public void initBasenGrid(double[] min, double[] max, String[] axesScales, String[] axesLabels) {
    base = new Base(min, max, axesScales);
    grid = new BasePlot(base, axesLabels);
}

public abstract void initBasenGrid(double[] min, double[] max);

public abstract void initBasenGrid();

public void initBasenGrid(Base b, BasePlot bp) {
    base = b;
    grid = bp;
}

public void configCanvas() {
    
   JFrame configFrame = new JFrame("Figure configuration");
            
   JPanel configPanel = new JPanel();
            
            Plot  plotElems = plots.getFirst();
            JButton plotButton = new JButton("Setting Plot Color");
            configPanel.add(plotButton);
            plotButton.addActionListener(new ActionListener() {

            JRadioButton  rbFigs = new JRadioButton();
            
            public void actionPerformed(ActionEvent e) {
                try {
                JColorChooser colorChooser = new JColorChooser();
                Color selectedColor = JColorChooser.showDialog(null, "Choose a color for this plot", new Color(200,200,200));
                changePlotColor(0, selectedColor);  
                }
            catch (Exception ex) {  
                System.out.println("Exception in setting Color");
               }
              }
            });
            
            
            JButton changeNameButton = new JButton("Setting Plot Name");
            configPanel.add(changeNameButton);
            changeNameButton.addActionListener(new ActionListener()  {
                public void actionPerformed(ActionEvent e) {
                    String newPlotName = JOptionPane.showInputDialog("Please specify the new name for the current plot");
                    changePlotName(0, newPlotName);
                    }
                });
            
            configFrame.add(configPanel);
            configFrame.setSize(400, 300);
            configFrame.setVisible(true);
            configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                       
      }
            
 //  set actions 
    public void setActionMode(int am) {         ActionMode = am;      }

    public void setNoteCoords(boolean b) {     allowNoteCoord = b;  	}

    public void setEditable(boolean b) {  	allowEdit = b;  	}

    public boolean getEditable() {    return allowEdit;  	}

    public void setNotable(boolean b) {     allowNote = b;   }

    public boolean getNotable() {   return allowNote; 	}

//   set/get elements 
   public LinkedList<Plot> getPlots() {    return plots;  	}

   // get a plot from the linked list of plots
    public Plot getPlot(int i) {  	  
        return (Plot) plots.get(i);  	
     }  

    public int getPlotIndex(Plot p) {
        for (int i = 0; i < plots.size(); i++)
	if (getPlot(i) == p)
                        return i;
	return -1;
    }

    public LinkedList<Plotable> getPlotables() {
	return objects;
    }

    public Plotable getPlotable(int i) {
	return (Plotable) objects.get(i);
    }

    public BasePlot getGrid() {
    	return grid;
    }

    public String[] getAxisScales() {
	return base.getAxesScales();
    }

    public void setAxisLabels(String... labels) {
    	grid.setLegend(labels);
	repaint();
    }

    public void setAxisLabel(int axe, String label) {
	grid.setLegend(axe, label);
	repaint();
    }

    public void setAxisScales(String... scales) {
	base.setAxesScales(scales);
	setAutoBounds();
    }

    public void setAxiScale(int axe, String scale) {
	base.setAxesScales(axe, scale);
	setAutoBounds(axe);
    }

    public void setFixedBounds(double[] min, double[] max) {
	base.setFixedBounds(min, max);
	resetBase();
	repaint();
    }

    public void setFixedBounds(int axe, double min, double max) {
    	base.setFixedBounds(axe, min, max);
	resetBase();
	repaint();
    }

    public void includeInBounds(double... into) {
	base.includeInBounds(into);
	grid.resetBase();
	repaint();
    }

    public void includeInBounds(Plot plot) {
	base.includeInBounds(PArray.min(plot.getData()));
	base.includeInBounds(PArray.max(plot.getData()));
	resetBase();
	repaint();
    }

    public void setAutoBounds() {
	if (plots.size() > 0) {
                    Plot plot0 = this.getPlot(0);
                    base.setRoundBounds(PArray.min(plot0.getData()), PArray.max(plot0.getData()));
	} else { // build default min and max bounds
            double[] min = new double[base.dimension];
            double[] max = new double[base.dimension];
            for (int i = 0; i < base.dimension; i++) {
	if (base.getAxeScale(i).equalsIgnoreCase(LINEAR)) {
                        min[i] = 0.0;
                        max[i] = 1.0;
	} 
        else if (base.getAxeScale(i).equalsIgnoreCase(LOGARITHM)) {
	min[i] = 1.0;
	max[i] = 10.0;
	}
            }
	base.setRoundBounds(min, max);
	}
	for (int i = 1; i < plots.size(); i++) {
                    Plot ploti = this.getPlot(i);
                    base.includeInBounds(PArray.min(ploti.getData()));
                    base.includeInBounds(PArray.max(ploti.getData()));
	}
	resetBase();
	repaint();
    }

    public void setAutoBounds(int axe) {
	if (plots.size() > 0) {
                    Plot plot0 = this.getPlot(0);
                    base.setRoundBounds(axe, PArray.min(plot0.getData())[axe], PArray.max(plot0.getData())[axe]);
	} else { // build default min and max bounds
            double min = 0.0;
            double max = 0.0;
            if (base.getAxeScale(axe).equalsIgnoreCase(LINEAR) | base.getAxeScale(axe).equalsIgnoreCase(STRINGS)) {
	min = 0.0;
	max = 1.0;
	}
            else if (base.getAxeScale(axe).equalsIgnoreCase(LOGARITHM)) {
	min = 1.0;
	max = 10.0;
	}
	base.setRoundBounds(axe, min, max);
	}

    for (int i = 1; i < plots.size(); i++) {
	Plot ploti = this.getPlot(i);
	base.includeInBounds(axe, PArray.min(ploti.getData())[axe]);
	base.includeInBounds(axe, PArray.max(ploti.getData())[axe]);
	}
        resetBase();
        repaint();
    }

    public void resetBase() {
        draw.resetBaseProjection();
        grid.resetBase();
        for (int i = 0; i < objects.size(); i++) {
	if (objects.get(i) instanceof BaseDependant) {
        ((BaseDependant) (objects.get(i))).resetBase();
	}
    }
    	repaint();
    }


    // add/remove elements 

  public void addLabel(String text, Color c, double... where) {
        addPlotable(new scalaSci.math.plot.plotObjects.Label(text, c, where));
    }

   public void addLabel(String text, Color c, Font f, double... where) {
        addPlotable(new scalaSci.math.plot.plotObjects.Label(text, c, f, where));
    }

    public void addDLabel(String text, Color c, int... where) {
        addPlotable(new scalaSci.math.plot.plotObjects.DLabel(text, c, where));
    }
        
     public void addDLabel(String text, Color c, Font f, int... where) {
        addPlotable(new scalaSci.math.plot.plotObjects.DLabel(text, c, f, where));
    }

     public void addBaseLabel(String text, Color c, double... where) {
        addPlotable(new scalaSci.math.plot.plotObjects.BaseLabel(text, c, where));
    }

    public void addPlotable(Plotable p) {
        objects.add(p);
	// resetBase();
        repaint();
    }

    public void removePlotable(Plotable p) {
        objects.remove(p);
        repaint();
    }

    public void removePlotable(int i) {
        objects.remove(i);
        repaint();
    }

    public int addPlot(Plot newPlot) {
        plots.add(newPlot);
        if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
	if (plots.size() == 1)
                    setAutoBounds();
	else
                    includeInBounds(newPlot);
            return plots.size() - 1;
    }

    public void setPlot(int I, Plot p) {
        plots.set(I, p);
        if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
        repaint();
    }

    public void changePlotData(int I, double[]... XY) {
        getPlot(I).setData(XY);
        repaint();
    }

    public void changePlotName(int I, String name) {
        getPlot(I).setName(name);
        if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
        repaint();
    }

    public void changePlotColor(int I, Color c) {
        getPlot(I).setColor(c);
            if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
          repaint();
    }

    public void removePlot(int I) {
        plots.remove(I);
        if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
        if (plots.size() != 0) {
    	setAutoBounds();
    	}
    }

    public void removePlot(Plot p) {
        plots.remove(p);
        if (linkedLegendPanel != null)
            linkedLegendPanel.updateLegends();
        if (plots.size() != 0) {
    	setAutoBounds();
	}
        }

    public void removeAllPlots() {
        plots.clear();
        if (linkedLegendPanel != null)
	linkedLegendPanel.updateLegends();
        clearNotes();
    }

    public void addVectortoPlot(int numPlot, double[][] v) {
        getPlot(numPlot).addVector(v);
    }

	/*public void addQuantiletoPlot(int numPlot, boolean _symetric, double[]... q) {
		getPlot(numPlot).addQuantiles(q, _symetric);
	}*/

    public void addQuantiletoPlot(int numPlot, int numAxe, double rate, boolean symetric, double[] q) {
        getPlot(numPlot).addQuantile(numAxe, rate, q, symetric);
    }

    public void addQuantiletoPlot(int numPlot, int numAxe, double rate, boolean symetric, double q) {
        getPlot(numPlot).addQuantile(numAxe, rate, q, symetric);
    }

    public void addQuantilestoPlot(int numPlot, int numAxe, double[][] q) {
        getPlot(numPlot).addQuantiles(numAxe, q);
    }

    public void addQuantilestoPlot(int numPlot, int numAxe, double[] q) {
        getPlot(numPlot).addQuantiles(numAxe, q);
    }

    public void addGaussQuantilestoPlot(int numPlot, int numAxe, double[] s) {
        getPlot(numPlot).addGaussQuantiles(numAxe, s);
    }

    public void addGaussQuantilestoPlot(int numPlot, int numAxe, double s) {
        getPlot(numPlot).addGaussQuantiles(numAxe, s);
    }

 //  call for toolbar actions 
    public void toGraphicFile(File file) throws IOException {
        Image image = createImage(getWidth(), getHeight());
        paint(image.getGraphics());
        image = new ImageIcon(image).getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, Color.WHITE, null);
        g.dispose();

	try {
    ImageIO.write((RenderedImage) bufferedImage, "PNG", file);
        } catch (IllegalArgumentException ex) {
    	}
  }

    public void displaySetScalesFrame() {
            new SetScalesFrame(this);
    }

    public void displayDatasFrame(int i) {
        DatasFrame df = new DatasFrame(this, linkedLegendPanel);
        df.panels.setSelectedIndex(i);
    }

    public void displayDatasFrame() {
        displayDatasFrame(0);
    }

    boolean mapset = false;
    public void resetMapData() {
        for (int i = 0; i < grid.getAxis().length; i++) {
	grid.getAxis()[i].setStringMap(null);
	setAxiScale(i, Base.LINEAR);
        }
    mapset = false;
    }

    public double[][] mapData(Object[][] data) {
        double[][] mapeddata = new double[data.length][data[0].length];
        if (!mapset) {
            for (int j = 0; j < data[0].length; j++) {
             if (!PArray.isDouble(data[0][j].toString())) {
	setAxiScale(j, Base.STRINGS);

	ArrayList<String> string_PArray_j = new ArrayList<String>(data.length);
	for (int i = 0; i < data.length; i++)
	   string_PArray_j.add(data[i][j].toString());

	grid.getAxis(j).setStringMap(PArray.mapStringArray(string_PArray_j));
	grid.getAxis(j).init();

	for (int i = 0; i < data.length; i++)
	  mapeddata[i][j] = grid.getAxis(j).getStringMap().get(data[i][j].toString());

                  initReverseMap(j);
	  } else {
	for (int i = 0; i < data.length; i++)
	   mapeddata[i][j] = Double.valueOf(data[i][j].toString());
		}
	}
	mapset = true;
	} else {
	for (int j = 0; j < data[0].length; j++)
	   if (!PArray.isDouble(data[0][j].toString())) {
	        if (base.getAxeScale(j).equals(Base.STRINGS)) {
	              for (int i = 0; i < data.length; i++) {
		if (!grid.getAxis(j).getStringMap().containsKey(data[i][j].toString())) {
		   Set<String> s = grid.getAxis(j).getStringMap().keySet();
		   ArrayList<String> string_PArray_j = new ArrayList<String>(s.size() + 1);
		   string_PArray_j.addAll(s);
		   string_PArray_j.add(data[i][j].toString());
		   grid.getAxis(j).setStringMap(PArray.mapStringArray(string_PArray_j));

		    initReverseMap(j);
		 }
		mapeddata[i][j] = grid.getAxis(j).getStringMap().get(data[i][j].toString());
		}
		} else {
		throw new IllegalArgumentException("The mapping of this PlotPanel was not set on axis " + j);
		  }
		} else {
		for (int i = 0; i < data.length; i++)
		  mapeddata[i][j] = Double.valueOf(data[i][j].toString());
		}
	}
		return mapeddata;
    }

    public Object[][] reverseMapedData(double[][] mapeddata) {
        Object[][] stringdata = new Object[mapeddata.length][mapeddata[0].length];
        for (int i = 0; i < mapeddata.length; i++)
            stringdata[i] = reverseMapedData(mapeddata[i]);
	return stringdata;
    }

    public Object[] reverseMapedData(double[] mapeddata) {
        Object[] stringdata = new Object[mapeddata.length];
        if (reversedMaps == null)
            reversedMaps = new HashMap[grid.getAxis().length];
        for (int j = 0; j < mapeddata.length; j++)
            if (reversedMaps[j] != null)
	stringdata[j] = reversedMaps[j].get((Double) (mapeddata[j]));
	else
	stringdata[j] = (Double) (mapeddata[j]);
        return stringdata;
    }

    HashMap<Double, String>[] reversedMaps;

    private void initReverseMap(int j) {
        if (reversedMaps == null)
            reversedMaps = new HashMap[grid.getAxis().length];
        if (grid.getAxis(j) != null)
	reversedMaps[j] = PArray.reverseStringMap(grid.getAxis(j).getStringMap());
    }


    // Paint method 
// anti-aliasing constant
final protected static RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

public static Color NOTE_COLOR = Color.BLACK;

public static Color EDIT_COLOR = Color.BLACK;

public boolean allowEdit = true;

public boolean allowNote = true;

public boolean allowNoteCoord = true;

protected double[] coordNoted;

public void paint(Graphics gcomp) {
	
   Graphics2D gcomp2D = (Graphics2D) gcomp;

	// anti-aliasing methods
    gcomp2D.addRenderingHints(AALIAS);
    gcomp2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    Container parent = this.getParent().getParent();
    Container grandParent = null;
    Container grandGrandParent  = null;
    if (parent.getParent() != null) {
        grandParent  =parent.getParent();
        if  (grandParent != null) {
           grandGrandParent = grandParent.getParent();
         }
       }
                
    gcomp2D.setColor(Color.WHITE);
    gcomp2D.fillRect(0, 0, getSize().width, getSize().height);
               
    draw.initGraphics(gcomp2D);

    // draw plot
    grid.plot(draw);

    // for all the plots of this canvas
    for (int i = 0; i < plots.size(); i++) {
        Plot currentPlot = getPlot(i);
        currentPlot.plot(draw);
        //if (currentPlot instanceof  scalaSci.math.plot.plots.contourPlot)                            return;
        //if (linkedLegendPanel != null)
           // linkedLegendPanel.nonote(i);   // SOS-Sterg: uncommenting this causes a problem when GSci is called from Java
    }

    for (int i = 0; i < objects.size(); i++)
        getPlotable(i).plot(draw);

        // draw note
    if (allowNote) {
        if (allowNoteCoord && coordNoted != null) {
            draw.setColor(NOTE_COLOR);
            draw.drawCoordinate(coordNoted);
            draw.drawText(PArray.cat(reverseMapedData(coordNoted)), coordNoted);
        }
        for (int i = 0; i < plots.size(); i++) {
            if (getPlot(i).noted) {
	if (linkedLegendPanel != null)
                        linkedLegendPanel.note(i);
	getPlot(i).note(draw);
	//return;
	}
                }
        }
    }

	// ///////////////////////////////////////////
	// ////// Listeners //////////////////////////
	// ///////////////////////////////////////////

	public final static int ZOOM = 0;
	public final static int TRANSLATION = 1;
                      public final static int EDIT = 2;
	public int ActionMode;
	protected boolean dragging = false;
	protected int[] mouseCurrent = new int[2];
	protected int[] mouseClick = new int[2];

	public void clearNotes() {
		coordNoted = null;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
	/*	System.out.println("PlotCanvas.mousePressed");
		
		 System.out.println("PlotCanvas.mousePressed"); 
                 System.out.println("  mouseClick = [" + mouseClick[0] + " " + mouseClick[1] + "]");
		 System.out.println(" mouseCurrent = [" + mouseCurrent[0] + " " +
		 mouseCurrent[1] + "]"); */
		
		mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		mouseClick[0] = mouseCurrent[0];
		mouseClick[1] = mouseCurrent[1];
	}

	public void mouseDragged(MouseEvent e) {
	//	System.out.println("PlotCanvas.mouseDragged");

		dragging = true;
	/*	
		 System.out.println("PlotCanvas.mouseDragged"); 
                 System.out.println(" mouseClick = [" + mouseClick[0] + " " + mouseClick[1] + "]");
		 System.out.println(" mouseCurrent = [" + mouseCurrent[0] + " " +mouseCurrent[1] + "]");
	*/	
		mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		switch (ActionMode) {
		case TRANSLATION:
			draw.translate(mouseCurrent[0] - mouseClick[0], mouseCurrent[1] - mouseClick[1]);
			mouseClick[0] = mouseCurrent[0];
			mouseClick[1] = mouseCurrent[1];
			repaint();
			break;
		case ZOOM:
			Graphics gcomp = getGraphics();
			gcomp.setColor(Color.black);
			gcomp.drawRect(min(mouseClick[0], mouseCurrent[0]), min(mouseClick[1], mouseCurrent[1]), abs(mouseCurrent[0] - mouseClick[0]), abs(mouseCurrent[1]
					- mouseClick[1]));
			repaint();
			break;
		}
		//repaint();
	}

	public void mouseReleased(MouseEvent e) {
		//System.out.println("PlotCanvas.mouseReleased");

		/*
		 * System.out.println("PlotCanvas.mouseReleased"); System.out.println("
		 * mouseClick = [" + mouseClick[0] + " " + mouseClick[1] + "]");
		 * System.out.println(" mouseCurrent = [" + mouseCurrent[0] + " " +
		 * mouseCurrent[1] + "]");
		 */
		mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		switch (ActionMode) {
		case ZOOM:
			if (abs(mouseCurrent[0] - mouseClick[0]) > 10 && abs(mouseCurrent[1] - mouseClick[1]) > 10) {
				int[] origin = { min(mouseClick[0], mouseCurrent[0]), min(mouseClick[1], mouseCurrent[1]) };
				double[] ratio = { abs((double) (mouseCurrent[0] - mouseClick[0]) / (double) getWidth()),
						abs((double) (mouseCurrent[1] - mouseClick[1]) / (double) getHeight()) };
				draw.dilate(origin, ratio);
				repaint();
			}
			break;
		}
		//repaint();
	}

	public void mouseClicked(MouseEvent e) {
		//System.out.println("PlotCanvas.mouseClicked");

		/*
		 * System.out.println("PlotCanvas.mouseClicked"); System.out.println("
		 * mouseClick = [" + mouseClick[0] + " " + mouseClick[1] + "]");
		 * System.out.println(" mouseCurrent = [" + mouseCurrent[0] + " " +
		 * mouseCurrent[1] + "]");
		 */
		mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		mouseClick[0] = mouseCurrent[0];
		mouseClick[1] = mouseCurrent[1];

		if (allowEdit) {
			if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount() > 1) {
				for (int i = 0; i < grid.getAxis().length; i++)
					if (grid.getAxis(i).isSelected(mouseClick, draw) != null) {
						grid.getAxis(i).edit(this);
						return;
					}

				for (int i = 0; i < plots.size(); i++)
					if (getPlot(i).isSelected(mouseClick, draw) != null) {
						getPlot(i).edit(this);
						return;
					}
			}
		}

		if (!dragging && allowNote) {
			for (int i = 0; i < plots.size(); i++) {
				double[] _coordNoted = getPlot(i).isSelected(mouseClick, draw);
				if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
					if (_coordNoted != null) {
						getPlot(i).noted = !getPlot(i).noted;
					} else {
						getPlot(i).noted = false;
					}
				} else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
					if (_coordNoted != null) {
						if (coordNoted != null) {
							boolean alreadyNoted = true;
							for (int j = 0; j < _coordNoted.length; j++)
								alreadyNoted = alreadyNoted && _coordNoted[j] == coordNoted[j];
							if (alreadyNoted) {
								coordNoted = null;
							} else {
								coordNoted = _coordNoted;
							}
						} else {
							coordNoted = _coordNoted;
						}
					}
				}
			}
			repaint();
		} else
			dragging = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		//System.out.println("PlotCanvas.mouseMoved");
		/*
		 * System.out.println("PlotCanvas.mouseClicked"); System.out.println("
		 * mouseClick = [" + mouseClick[0] + " " + mouseClick[1] + "]");
		 * System.out.println(" mouseCurrent = [" + mouseCurrent[0] + " " +
		 * mouseCurrent[1] + "]");
		 */
		/*mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		if (allowNote) {
			for (int i = 0; i < plots.size(); i++) {
				double[] _coordNoted = getPlot(i).isSelected(mouseCurrent, draw);
				if (_coordNoted != null) {
					getPlot(i).noted = !getPlot(i).noted;
				} else {
					getPlot(i).noted = false;
				}
			}
			repaint();
		}*/
	}
 
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("PlotCanvas.mouseWheelMoved");
		//System.out.println("PlotCanvas.mouseWheelMoved");
		// System.out.println(" mouseClick = [" + mouseClick[0] + " " +
		// mouseClick[1] + "]"); System.out.println(" mouseCurrent = [" +
		// mouseCurrent[0] + " " + mouseCurrent[1] + "]");
		
		mouseCurrent[0] = e.getX();
		mouseCurrent[1] = e.getY();
		e.consume();
		int[] origin;
		double[] ratio;
		double factor = 1.2;
		switch (ActionMode) {
		case ZOOM:
			if (e.getWheelRotation() == -1) {
				origin = new int[] { (int) (mouseCurrent[0] - getWidth() / 3/* (2*factor) */), (int) (mouseCurrent[1] - getHeight() / 3/* (2*factor) */) };
				ratio = new double[] {  1/factor, 1/factor };
			} else {
				origin = new int[] { (int) (mouseCurrent[0] - getWidth() / 1.333/* (2/factor) */),
						(int) (mouseCurrent[1] - getHeight() / 1.333/* (2/factor) */) };
				ratio = new double[] { factor, factor};
			}
			draw.dilate(origin, ratio);
			repaint();
			break;
		}
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		//System.out.println("PlotCanvas.componentResized");
		//panelSize = new int[] { (int) (getSize().getWidth()), (int) (getSize().getHeight()) };
		draw.resetBaseProjection();
		//System.out.println("PlotCanvas : "+panelSize[0]+" x "+panelSize[1]);
		repaint();
		linkedLegendPanel.componentResized(e);  // SOS sterg:  keep it?
	}

	public void componentShown(ComponentEvent e) {
	}

}