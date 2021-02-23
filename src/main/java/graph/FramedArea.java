package graph;
import java.awt.*;


class FramedArea extends Panel {
  public static final String rcsid = "Graph";
  public FramedArea() {
    super();
  }

  public Insets getInsets() {
    return new Insets(4,4,5,5);
  }

  public void paint(Graphics g) {
    Dimension d = getSize();
    Color bg = getBackground();
     
    g.setColor(bg);
    g.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
    g.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
  }
}
