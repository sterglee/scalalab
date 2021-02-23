package scalaSci.math.plot.plotObjects;

import java.awt.*;

import scalaSci.math.plot.render.*;
// 9-Jan
public class BaseLabel extends Label   {

    public BaseLabel(String l, Color c, double... rC) {
        super(l, c, rC);
        }


    /*
     var p3d = new Plot3DCanvas(Array(0.0, 0.0, 0.0), Array(10.0, 10.0, 10.0), Array("lin", "lin", "lin"),  Array("x", "y", "z"))
     var baseLabel = new BaseLabel("label", Color.RED, Array(-0.1, 0.5, 0.5): _*)
     p3d.addPlotable(baseLabel)
     var fr = new FrameView(p3d)
     fr.setVisible(true)
     */
    @Override
 public void plot(AbstractDrawer draw) {
    draw.setColor(color);
    draw.setFont(font);
    draw.setTextAngle(angle);
    draw.setTextOffset(cornerE, cornerN);
    draw.drawTextBase(label, coord);
    }
}
