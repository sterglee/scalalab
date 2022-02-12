package graph;
 
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
 
public class ScatterPlot extends Graph  implements MouseMotionListener, MouseListener {
  public double  xPrev, yPrev;
  public static final String rcsid = "Graph";  protected Rectangle currentRect = null;
  public String CurrentGroup;
  protected GraphPoint selectedPoint = null;
  protected boolean dragging = false;
  protected double xTolerance = 25;
  protected double yTolerance = 25;
  protected double xLow = 0;
  protected double xHigh = 0;
  protected double yLow = 0;
  protected double yHigh = 0;
  protected int groupCount = 0;
  protected String[] groups = null;
  public ScatterPlotControls graphControls = null;
  protected double[][] zoomList = null;
  protected int maxZoom = 16;
  protected int currZoom = 0;
  private   boolean   LineGraphFlag = true;
  int CurrentPointCnt = 0;
  
  public ScatterPlot() {
    groups = new String[colors.length]; 
    addMouseListener(this);
    addMouseMotionListener(this);
   }

    public double getXTolerance() { return xTolerance; }
  public double getYTolerance() { return yTolerance; }
  public void setXTolerance(double x) { 
    xTolerance = x; 
    refresh();
  }
  public void setYTolerance(double y) { 
    yTolerance = y;
    refresh();
  }

  public void addNotify() {
    super.addNotify();

    currZoom = 0;
    zoomList = new double[maxZoom + 1][];
    zoomList[currZoom] = getCurrentScale();
    zoomList[currZoom + 1] = null;
  }

  protected double[] getCurrentScale() {
    double coords[] = new double[4];
    coords[0] = xStart;
    coords[1] = xEnd;
    coords[2] = yStart;
    coords[3] = yEnd;

    return coords;
  }

  // The next few functions implement forward/backward zooming
  // capability.  It operates similar to the forward/backward/home
  // buttons on a WWW browser.  "Home" brings you to the default
  // (original) zoom level.  

  // rescale to specified zoom coords 
  public void zoomForward(double[] coords) {
    //    System.out.println("zoomForward(coords):: " + currZoom);
    if(currZoom == maxZoom) {
      System.err.println("maximum zoom (" + maxZoom + ") reached");
      return;
    }

    reScale(zoomList[++currZoom] = coords);
    zoomList[currZoom + 1] = null;
  }

  // rescale to next zoom level, if present
  public void zoomForward() {
    if(zoomList[currZoom + 1] != null)
      reScale(zoomList[++currZoom]);
  }

  // rescale to previous settings
  public void zoomBackward() {
    if (currZoom != 0)
      reScale(zoomList[--currZoom]);
  }

  // rescale to default (starting point)
  public void zoomHome() {
    reScale(zoomList[currZoom = 0]);
  }

  protected void pointSelected(GraphPoint p) {
    if(graphControls != null) 
      graphControls.setSelectedPoint(p);
  }

  // point has requested that we draw its tolerance box
  public void drawBox(GraphPoint p) {
    selectedPoint = p;
    repaint();
  }

  // point has requested that its tolerance box be erased
  public void unDrawBox(GraphPoint p) {
    selectedPoint = null;
    repaint();
  }

  // provide a consistent format for printing double values
  protected String formatdouble(double f) {
    DecimalFormat df = new DecimalFormat("0.00");
    df.setMinimumIntegerDigits(1);
    return df.format(f);
  }

   protected int xOrigin() {
    return leftBorder;
  }

  protected int yOrigin() {
    Dimension d = getSize();
    return d.height - bottomBorder;
  }

  public Color groupColor(String group) {
    // linear search through groups array
    for(int i = 0; i < groups.length; i++) {
      if (groups[i] == null)
	return null;

      if(groups[i].equalsIgnoreCase(group)) 
	return colors[i];
    }

    return null;
  }

  public int groupId(String group) {
    // linear search through groups array
    for(int i = 0; i < groups.length; i++) {
      if (groups[i] == null)	return -1;  
      if(groups[i].equalsIgnoreCase(group))  
	return i;
    }
    return -1;
  }

  
  // the groups[] array should have a one-to-one correspondence
  // with the colors[] array:  groups[i] -> colors[i]
  //
  protected void addGroup(String group) {
    if(groupCount >= groups.length) {
      System.out.println("max groups (" + groupCount + ") exceeded!");
      return;
    }
    groups[groupCount++] = group;
  }
 
  public GraphPoint addPoint(int x, int y, String label, String group, boolean rep, GraphPoint prevPoint) {   
      GraphPoint currentPoint = addPoint((double) x, (double) y, label, group, rep, prevPoint); 
      
      return currentPoint;
  }
  
  public GraphPoint addPoint(double x, double y, String label, String group, boolean rep, GraphPoint prevPoint) {
        // keep coordinates of previously added GraphPoint at the ScatterPlot fields xPrev, yPrev
      if (groupColor(group) == null)     addGroup(group);

    GraphPoint p = new GraphPoint(x, y, label, group, groupColor(group), prevPoint);  

    if (p.getXPos() < xLow)    xLow = p.getXPos();
    if (p.getYPos() < yLow)   yLow = p.getYPos();
    if (p.getXPos() > xHigh)   xHigh = p.getXPos();
    if (p.getYPos() > yHigh)  yHigh = p.getYPos();

    add(p);
    
    if (rep==true) {    invalidate();    validate();        }

     return p;
  }

  public void doLayout() {
    Component[] components = getComponents();
    CurrentPointCnt = 0;
    for (int i = 0; i < components.length; i++)
      if (components[i] instanceof GraphPoint) {
          GraphPoint componentPoint = (GraphPoint) components[i];
	setPointLocation(componentPoint);
        componentPoint.repaint(); 
      }
  }


      

  protected void setPointLocation(GraphPoint p) {
    String Group = p.group;
    int x = xCoordinate(p.getXPos()) - (p.getBounds().width / 2);
    int y = yCoordinate(p.getYPos()) - (p.getBounds().height / 2);

    p.setLocation(x, y);
     } 

  protected double HpixelsToUnits(int pixels) {
    return (pixels / xRes());
  }

  protected double VpixelsToUnits(int pixels) {
    return (pixels / yRes());
  }

  public int HunitsToPixels(double units) {
    double l = units * xRes();
    return (int)l;
  }

  protected int VunitsToPixels(double units) {
    double l = units * yRes();
    return (int) l;
  }

  protected double xValue(int x) {
    if(xInverted()) 
      return xStart + HpixelsToUnits(xOrigin() - x);
    else
      return xStart + HpixelsToUnits(x - xOrigin());
  }

  protected double yValue(int y) {
    if(yInverted()) 
      return yStart - VpixelsToUnits(yOrigin() - y);
    else 
      return yStart + VpixelsToUnits(yOrigin() - y);
  }

   public int xCoordinate(double x) {
    int i;

    if (xInverted()) 
      i = xOrigin() + HunitsToPixels(xStart - x);
    else 
      i = xOrigin() + HunitsToPixels(x - xStart);
    

    if (i < xOrigin())
      i = xOrigin();

    return i;
  }

  public int yCoordinate(double y) {
    int i;
    if(yInverted())
      i = yOrigin() - VunitsToPixels(yStart - y);
    else
      i = yOrigin() - VunitsToPixels(y - yStart);

    if (i > yOrigin())
      i = yOrigin();

    return i;
  }

  protected boolean xInverted() {
    if (xStart > xEnd)
      return true;
    else
      return false;
  }

  protected boolean yInverted() {
    if (yStart > yEnd)
      return true;
    else
      return false;
  }

  protected double xRes() {
    Dimension d = getSize();
    int pixelWidth = (d.width - rightBorder) - xOrigin() ;
    double unitWidth;
     if (xEnd > xStart)
       unitWidth = Math.abs(xEnd - xStart);
     else 
       unitWidth = Math.abs(xStart - xEnd);
    
    return pixelWidth / unitWidth;
  }

  protected double yRes() {
    int pixelHeight = yOrigin() - topBorder;
    double unitHeight;
    if(yEnd > yStart)
      unitHeight = yEnd - yStart;
    else
      unitHeight = yStart - yEnd;
    
    return pixelHeight / unitHeight;
  }

  public void paint(Graphics g) {
      g.setColor(fg);

    FontMetrics fm = g.getFontMetrics();
    if(fm.stringWidth(yLabel) > leftBorder)
      leftBorder = fm.stringWidth(yLabel) + 10;
    
    Dimension dim = getSize();
    
    // horizontal axis
    Point endpoint = new Point(dim.width - rightBorder, yOrigin());
    g.drawLine(xOrigin(), yOrigin(), endpoint.x, endpoint.y);
    g.drawString(xLabel, dim.width / 2, dim.height - 10);
    
//     double increment = xInverted() ? -xTicks : xTicks;
    double start = xInverted() ? xEnd : xStart;

    double increment;
    if(xInverted())      increment = (xStart - xEnd) / -10;    else      increment = (xEnd - xStart) / 10;

    for(double i = xStart; xCoordinate(i) < endpoint.x; i+= increment) {
      String s = formatdouble(i);
      int h = fm.getHeight();
      int length = fm.stringWidth(s);
      
      g.drawString(s, xCoordinate(i) - (length/2), yOrigin() + h + 5);
      g.drawLine(xCoordinate(i), yOrigin(), xCoordinate(i), yOrigin() + 5);
    }
      
    // vertical axis
    //     increment = yInverted() ? -yTicks : yTicks;

    if(yInverted())      increment = (yStart - yEnd) / -10;    else      increment = (yEnd - yStart) / 10;
    start = yInverted() ? yEnd : yStart;

    endpoint.x = xOrigin();
    endpoint.y = topBorder;
    g.drawLine(xOrigin(), yOrigin(), endpoint.x, endpoint.y);
    g.drawString(yLabel, 5, dim.height / 2);
    for(double i = yStart; yCoordinate(i) > endpoint.y; i += increment) {
      //      String s = df.format(i);
      String s = formatdouble(i);
      int length = fm.stringWidth(s);
      int h = fm.getHeight();
	
      int y = yCoordinate(i);
      g.drawString(s, xOrigin() - length - 10 , y + (h/2));
      g.drawLine(xOrigin() - 5, y, xOrigin(), y);
    }

    // If currentRect exists, paint a rectangle on top.
    if (currentRect != null) {
      Dimension d = getSize();
      Rectangle box = getDrawableRect(currentRect, d);
      //Draw the box outline.
      g.drawRect(box.x, box.y,  box.width - 1, box.height - 1);
      }
    // If one of the points is marked as "selected", we draw a box
    // around it as per this graph's tolerance.
    if (selectedPoint != null) {
      Rectangle r = selectedPoint.getBounds();
      int width = HunitsToPixels(xTolerance);
      int height = VunitsToPixels(yTolerance);
      int x = xCoordinate(selectedPoint.xCoord) - width / 2;
      int y = yCoordinate(selectedPoint.yCoord) - height / 2;

      g.drawRect(x, y, width, height);
    }
  }


  public Panel getGraphControls() {
    return graphControls = new ScatterPlotControls(this);
  }

  // Given a rectangular area swept out with the mouse, 
  // rescale the graph to cover those coordinates.
  //
   protected void zoom(Rectangle rect) {
    int leftX, rightX;
    int upperY, lowerY;

    // The width may be negative if the drag occurred right-to-left, 
    // so we must take that into account.
    //
    if(rect.width > 0) {
      leftX = rect.x;      rightX = rect.x + rect.width;}
     else {      leftX = rect.x + rect.width;      rightX = rect.x;    }

    // The height may be negative if the drag occurred down-to-up, 
    // so we must take that into account.
    //
    if(rect.height > 0) {      upperY = rect.y;      lowerY = rect.y + rect.height;    } 
    else {      upperY = rect.y + rect.height;      lowerY = rect.y;    }

    double coords[] = new double[4];
    coords[0] = xValue(leftX);    coords[1] = xValue(rightX);
    coords[2] = yValue(lowerY);    coords[3] = yValue(upperY);
    zoomForward(coords);
  }

  // Given new begin/end points for both axis, rescale
  // the displayed graph.
  //
  public void reScale(double[] coords){
    this.xStart = (double) coords[0];
    this.xEnd = (double)coords[1];
    this.yStart = (double)coords[2];
    this.yEnd = (double)coords[3];

    refresh();
  }

  protected void refresh() {
    invalidate();
    validate();
    repaint();
    if(graphControls != null)
      graphControls.updateValues();
  }


  Rectangle getDrawableRect(Rectangle originalRect, 
			    Dimension drawingArea) {
    int x = originalRect.x;    int y = originalRect.y;
    int width = originalRect.width;    int height = originalRect.height;

    //Make sure rectangle width and height are positive.
    if (width < 0) {      width = 0 - width;      x = x - width + 1;
      if (x < 0) {	width += x;	x = 0;      }
    }
    if (height < 0) {      height = 0 - height;      y = y - height + 1;
      if (y < 0) {	height += y;	y = 0;      }
    }
    
    //The rectangle shouldn't extend past the drawing area.
    if ((x + width) > drawingArea.width) {
      width = drawingArea.width - x;
    }
    if ((y + height) > drawingArea.height) {
      height = drawingArea.height - y;
    }

    //If the width or height is 0, make it 1
    //so that the box is visible.
    if (width == 0) {      width = 1;    }
    if (height == 0) {      height = 1;    }
    
    return new Rectangle(x, y, width, height);
  }


  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public void mousePressed(MouseEvent e) {
    int x = e.getX();    int y = e.getY();
    currentRect = new Rectangle(x, y, 0, 0);
    repaint();
  }


  public void mouseReleased(MouseEvent e) {
    int x = e.getX();    int y = e.getY();
    updateSize(e);
    if (dragging) {      dragging = false;      zoom(currentRect);
      currentRect = null;      repaint();    }
  }

  public void mouseDragged(MouseEvent e) {
    dragging = true;    updateSize(e); 
  }

  public void mouseMoved(MouseEvent e) {}

  void updateSize(MouseEvent e) {
    int x = e.getX();    int y = e.getY();
    currentRect.setSize(x - currentRect.x, y - currentRect.y);
    repaint();
  }


  static public void main(String[] argv) {
    ScatterPlot graph = new ScatterPlot();
    
    double xS, yS, xE, yE;
    
    JFrame frame = new JFrame();
    Container container = frame.getContentPane();
    container.setLayout(new BorderLayout());
    container.add(graph);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Developing Java Scientic Charting");
    
    int np = 100;
    double [] x = new double[np];
    double [] y = new double[np];
    double [] z = new double[np];
    double x0 = 0.1; x[0] = x0;
    for (int i=1; i<np; i++) {
        x[i] = 3.99*x[i-1]*(1-x[i-1]);
        y[i] = Math.cos(0.123*i);
        z[i] = 2*Math.sin(0.255*i)+Math.cos(3.45*i);
    }
    graph.setTitle("Logistic Map");
    graph.setXLabel("x values");
    
   GraphPoint prev = null, current;
    for (int i=0; i<np; i++) {
        current =    graph.addPoint((double)i, (double)x[i], "l", "Group1", false, prev);
        prev = current;
            }
   prev=null;
   for (int i=0; i<np; i++)  {
        current = graph.addPoint((double)i, (double)y[i], "s","GroupSin", false,  prev); 
        prev = current;
    }
   prev = null;
   for (int i=0; i<np; i++) {
       current = graph.addPoint((double)i, z[i], "3", "TwoSines", false, prev);
       prev = current;
   }
   
    xS = (double)0; xE = (double)np; yS = (double)-3.0; yE = (double)3.0;
    graph.setXStart(xS); graph.setXEnd(xE); graph.setYStart(yS); graph.setYEnd(yE);
    
    frame.setSize(1000, 800);
    frame.setVisible(true);
    frame.repaint();
    //graph.doLayout();
    /*
    ScatterPlotControls cntrGraph = new ScatterPlotControls(graph);
    container.add(cntrGraph);
    JFrame cntrFrame = new JFrame();
    Container cntrlContainer = cntrFrame.getContentPane();
    cntrlContainer.add(cntrGraph);
    cntrGraph.setSize(200,200);
    cntrGraph.setVisible(true);
    cntrFrame.setLocation(500,500);
    cntrFrame.setSize(300,300);
    cntrFrame.setVisible(true);
    graph.graphControls = cntrGraph;
  */
      }
}


