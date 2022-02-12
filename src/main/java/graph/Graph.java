  
package graph;

import java.awt.event.*;
import java.awt.*;
import java.util.*;


// Base class for Graphs
//
abstract public class Graph extends Panel {
public static final String rcsid = "Graph";
  protected double xStart = 0;
  protected double xEnd = 200;
  protected double yStart = 0; 
  protected double yEnd = 200;
  protected int leftBorder = 100;
  protected int rightBorder = 25;
  protected int topBorder = 25;
  protected int bottomBorder = 50;
  protected double xTicks = 50;
  protected double yTicks = 50;
  protected Color fg = Color.blue;
  protected Color bg = Color.lightGray;
  protected String xLabel = "X Axis";
  protected String yLabel = "Y Axis";
  protected String title = null;
  protected Graphics offGraphics = null;
  protected Image offImage = null;
  protected Dimension offDim = null;
  protected final Color[] colors = { Color.red,	     Color.blue,     Color.green, Color.orange,     Color.black,    Color.white,    Color.gray,
				     Color.pink,    Color.magenta,    Color.yellow,     Color.cyan};
  public Graph() {
    setForeground(Color.blue);
    setBackground(Color.lightGray);
  }

  abstract public Panel getGraphControls();

  public String getTitle() { return title;}
  public void setTitle(String title) { this.title = title;}

  public double getXStart() { return xStart;}
  public void setXStart(double x) { xStart = x;}

  public double getXEnd() { return xEnd; }
  public void setXEnd(double x) { xEnd = x;}

  public double getYStart() { return yStart;} 
  public void setYStart(double y) { yStart = y;}

  public double getYEnd() { return yEnd;} 
  public void setYEnd(double y) { yEnd = y;}
 
  public double getXTicks() { return xTicks;}
  public void setXTicks(double x) { xTicks = x; }

  public double getYTicks() { return yTicks;}
  public void setYTicks(double y) { yTicks = y; }

  public String getXLabel() { return xLabel; }
  public void setXLabel(String x) { xLabel = x;}

  public String getYLabel() { return yLabel; }
  public void setYLabel(String y) { yLabel = y;}


  // Provide double-buffered graphics update for descendant classes.
  public void update(Graphics g) {
    Dimension d = getSize();

    // draw to an off-screen area before displaying, in order to reduce flicker
    if ((offGraphics == null) || (d.width != offDim.width) || (d.height != offDim.height) ) {
      offDim = d;
      offImage = createImage(d.width, d.height);
      offGraphics = offImage.getGraphics();
    }
    offGraphics.clearRect(0, 0, d.width, d.height);  // must clear slate in update()

    paint(offGraphics);

    // copy the rendered image to the on-screen display
    g.drawImage(offImage, 0, 0, null);
  }
}
