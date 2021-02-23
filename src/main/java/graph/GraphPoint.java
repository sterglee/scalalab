package graph;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*; 

public class GraphPoint extends Canvas  {
  protected double xCoord, yCoord;
  protected double xPrev, yPrev;  // coordinates of the previously plotted point in order to connect
  protected int width = 6;
  protected int height = width;
  protected String group;
  protected String label;
  protected ScatterPlot parent = null;
  protected boolean selected = false;
  protected Color color;
 
  public GraphPoint(double x, double y, String label, String group, Color color, GraphPoint prevPoint) {
    this.xCoord = x;     this.yCoord = y;
    this.label = label;    this.group = group;    this.color = color;
    if (prevPoint != null) {        this.xPrev = prevPoint.xCoord; this.yPrev = prevPoint.yCoord; }
    else { this.xPrev = x;  this.yPrev = y;  }
  
  }

  public double getXPos() { return xCoord; }
  public double getYPos() { return yCoord; }
  public String getLabel() { return label; }
  public String getGroup() { return group; }

  public void addNotify() {
    super.addNotify();
    setSize(getPreferredSize());
    parent = (ScatterPlot) getParent();
  }

  public Dimension getPreferredSize() {
    return new Dimension(width, height);
  }

  public void paint(Graphics g) {
    parent = (ScatterPlot)this.getParent(); 
    g.setColor(this.color);
  
    if(selected)
      g.fillRect(0, 0, width -1, height - 1);
    else
      g.drawRect(0, 0, width -1, height - 1);
 
    int ixPrev = parent.xCoordinate(xPrev);  
    int iyPrev = parent.yCoordinate(yPrev);
    int ixc = parent.xCoordinate(xCoord);
    int iyc = parent.yCoordinate(yCoord);
    Graphics gParent = parent.getGraphics();
    gParent.setColor(this.color);
    gParent.drawLine(ixPrev, iyPrev, ixc, iyc);   // draw line connecting points

  }

}


