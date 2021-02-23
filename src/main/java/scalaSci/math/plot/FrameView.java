package scalaSci.math.plot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import scalaSci.math.plot.canvas.*;
import scalaExec.Interpreter.GlobalValues;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;


// organizes a frame view of a plot
public class FrameView extends JFrame {
      private int figureId;   // keep the figure Id in order to use it for disposing the figure
      private static int defaultFigSizeX = 600;
      private static int defaultFigSizeY = 500;
      
      public static void setDefaultFigSizeX(int figSizeX) {
          defaultFigSizeX  = figSizeX;
          }

      public static void setDefaltFigSizeY(int figSizeY) {
          defaultFigSizeY = figSizeY;
      }
      // have in frame view the canvases passed as parameters
   public FrameView(Plot2DCanvas... canvas) {
                JPanel panel = new JPanel();
                int canvasLen = canvas.length;
                Plot2DPanel [] innerPlots = new Plot2DPanel[canvasLen];  // keep a Plot2DPanel for each canvas
                for (int i = 0; i < canvas.length; i++) {
                       Plot2DPanel p2Dp = new Plot2DPanel(canvas[i]);   // put canvas in a Plot2DPanel
                       innerPlots[i] = p2Dp;
                       panel.add(p2Dp);
                }
                setContentPane(panel);
                pack();
                setSize(defaultFigSizeX, defaultFigSizeY);
                addWindowListener(new figureClosingAdapter());
                setVisible(true);
               
               AlphaComposite acomp = AlphaComposite.SrcOver.derive(GlobalValues.alphaComposite);
               
               // set the composite for all the Plot2DPanels within the FrameView
               for (int i = 0; i < canvas.length; i++)  {
                   Plot2DPanel innerPlot = innerPlots[i];
                   Graphics2D g2d = (Graphics2D)innerPlot.getGraphics();
                   g2d.setComposite(acomp);
               }

               setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
 }

   
   public FrameView(Plot2DCanvas  canvas) {
                JPanel panel = new JPanel();
                Plot2DPanel p2Dp = new Plot2DPanel(canvas);
                panel.add(p2Dp);
                
                setContentPane(panel);
                pack();
                setSize(defaultFigSizeX, defaultFigSizeY);
                addWindowListener(new figureClosingAdapter());
                setVisible(true);
               
               setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
 }
   
 public FrameView(Plot3DCanvas... canvas) {
                JPanel panel = new JPanel();
                for (int i = 0; i < canvas.length; i++)
                    panel.add(new Plot3DPanel(canvas[i]));
                    setContentPane(panel);
                    pack();
                    setSize(defaultFigSizeX, defaultFigSizeY);
                    addWindowListener(new figureClosingAdapter());
                    setVisible(true);
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

 public FrameView(Plot3DCanvas  canvas) {
                JPanel panel = new JPanel();
                panel.add(new Plot3DPanel(canvas));
                setContentPane(panel);
                pack();
                setSize(defaultFigSizeX, defaultFigSizeY);
                addWindowListener(new figureClosingAdapter());
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

public FrameView(String title, JComponent panel) {
            super(title);
            setIconImage(GlobalValues.scalaImage);
            setContentPane(panel);
            pack();
            setSize(defaultFigSizeX, defaultFigSizeY);
            addWindowListener(new figureClosingAdapter());
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}

 public FrameView(String title, JComponent panel, int _figId) {
            super(title);
            figureId = _figId+1;  // keep the figure Id in order to use it for disposing figure
            setContentPane(panel);
                
            pack();
            setSize(defaultFigSizeX, defaultFigSizeY);
            addWindowListener(new figureClosingAdapter());
            setVisible(true);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
}

public FrameView(JComponent... panels) {
            JPanel panel = new JPanel();
            for (int i = 0; i < panels.length; i++)
                panel.add(panels[i]);
            setContentPane(panel);
            pack();
            setSize(defaultFigSizeX, defaultFigSizeY);
            addWindowListener(new figureClosingAdapter());
            setVisible(true);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}

public FrameView(JPanel panel) {
            setContentPane(panel);
            pack();
            setSize(defaultFigSizeX, defaultFigSizeY);
            addWindowListener(new figureClosingAdapter());
            setVisible(true);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }

private class figureClosingAdapter extends WindowAdapter {
    public void windowClosing(WindowEvent e) 
     {
// upon closing the figure frame, perform an explicit call for closing the figure object
            scalaSci.math.plot.plot.close(figureId);   
     }
    }

}