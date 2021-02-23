
// the purpose of this class is to allow the user to enter a mathematical formula with 
// plotting it with the mouse.

package Draw;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
 

public class MousePaint extends JFrame implements MouseMotionListener
    {
         private int xp, yp, xn, yn;   // previous and next points
         private static final int MXPOINTS = 200000;
            // the following vectors keep the x and y coordinates of the painted symbols
         private static Vector <Integer> pointsX;    // X-coordinates of the entered points
         private static Vector <Integer> pointsY;   // Y-coordinates of the entered points
         private boolean newStrokeInited = false;
	 
         // it is used to copy the coordinates of the bits drawn by the user that will constitute the strokes for further  processing
         public static double [][] pointsXY;
         
         private static int numPoints = 0;  // counts the points 

         private static int SIZX = 900, SIZY = 500;
         
         public static scalaSci.RichDouble2DArray  strokes;  // it is filled with the strokes entered
                                                                                    // new stroke lines are seperated with -1
         
                 
         // it is called upon the end of drawing to collect the points from the dynamically sized
         // Java vectors and to return a convenient ScalaLab RichDouble2DArray
         public static void   getStrokes() {
  
            pointsXY[0] = new double [numPoints];
            pointsXY[1]=  new double [numPoints];
            //  copy the points
            for (int k=0; k < numPoints; k++) {
                pointsXY[0][k] = pointsX.elementAt(k);
                pointsXY[1][k] = SIZY- pointsY.elementAt(k);
                 }
              strokes = new scalaSci.RichDouble2DArray(pointsXY);     
         }
         
         // the frame where we draw our symbols with the mouse
     public MousePaint()
         {
          
             setTitle("Perform your drawing using the mouse. On close, the strokes are returned to Draw.MousePaint.strokes variable ");
             pointsX  = new Vector<Integer>();
             pointsY  = new Vector<Integer>();
             
             pointsXY = new double[2][];
             numPoints = 0;  // counts points
             
             addWindowListener(new WindowAdapter()
             {
             public void windowClosing(WindowEvent we)
                 {
                getStrokes();   // make the strokes available with the static RichDouble2DArray named "strokes" 
                dispose();                
             }
         });
         addMouseMotionListener(this);
         setBounds(50, 50, SIZX, SIZY);
         setVisible(true);
     }
     
     public static void main(String[] argv)
         {
         new MousePaint();
     }
     
     public void update(Graphics g)
         {
         paint(g);
     }
     
     public void paint(Graphics g)
         {
         g.setColor(Color.black);
         g.drawLine(xp, yp, xn, yn);
     }
     
     // dragged mouse accumulates the path of painted stroke
     public void mouseDragged(MouseEvent e)
         {
         newStrokeInited = false;

         e.consume();
         int x = e.getX();
         int y = e.getY();
           // update previous points
         if ( xp == 0 )   xp = x;
         if ( yp == 0 )   yp = y;
         
         // next points
         xn = x;
         yn = y;
         
         pointsX.addElement(x);
         pointsY.addElement(y);
         numPoints++;		 
        	
         repaint();
         
         xp = xn;
         yp = yn;
     }
     
     
     // upon the first mouse move a new stroke is inited
     public void mouseMoved(MouseEvent me)
     { 
         // new stroke lines are seperated with -1
	 if (newStrokeInited == false) {
	  pointsX.addElement(-1);
	  pointsY.addElement(-1);
 	  numPoints++;
	  newStrokeInited = true;
	    }
	 }
}
