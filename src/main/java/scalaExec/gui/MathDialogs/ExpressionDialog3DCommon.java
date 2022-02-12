
package scalaExec.gui.MathDialogs;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** the common part of the dialogs for plotting 3-D functions 
 */

public class ExpressionDialog3DCommon extends JFrame {
    static public int defaultSampleNumPoints = 100;  // default number of points to sample on each axis
    JTextField  fFun = new JTextField(50);   // the function specification in terms of x and y variables
    JTextField  fXLow = new JTextField(10);  // low limit for x-variable    
    JTextField  fXStep = new JTextField(10); // increment for x-variable
    JTextField  fXUp = new JTextField(10);  // up limit for x-variable
    JTextField fYLow = new JTextField(10);  // low limit for y-variable
    JTextField fYStep = new JTextField(10);  // increment for y-variable
    JTextField fYUp = new JTextField(10);   // up limit for y-variable
    JButton generateCodeButton = new JButton("Generate Code");
    JButton plotButton = new JButton(" Plot  ");
    JPanel functionPanel = new JPanel(),  limitsPanel = new JPanel(), limitsXPanel = new JPanel(), limitsYPanel = new JPanel(), buttonsPanel = new JPanel();
    JTextArea  fCode;  
    
    public ExpressionDialog3DCommon(String frameName) {
     setTitle(frameName);
   
      functionPanel.add(new JLabel("z = f(x, y) = ")); 
      functionPanel.add(fFun);
      fFun.setToolTipText("Specify your 2-dimensional function using variables x and y, e.g.:    sin(0.2*x)*cos(6.1*y) ");
      JPanel  xPanel = new JPanel();
      xPanel.add(new JLabel("xMin (deltaX) xMax")); 
      xPanel.setToolTipText("The limits for the x variable");
      fXLow.setToolTipText("the lower limit of the x variable, e.g. -7");
      fYLow.setToolTipText("the lower limit for the y variable, e,g, -4,5");
      fXUp.setToolTipText("the upper limit of the x variable, e.g. 4,8");
      fYUp.setToolTipText("the upper limit for the y variable, e,g, 5");
      fXStep.setToolTipText("the sampling interval for the x-axis, optional if not specified 100 points in x-range are uniformly sampled");
      fYStep.setToolTipText("the sampling interval for the y-axis, optional if not specified 100 points in y-range are uniformly sampled");
      xPanel.add(fXLow); xPanel.add(fXStep); xPanel.add(fXUp);
      limitsXPanel.add(xPanel);
      JPanel  yPanel = new JPanel();
      yPanel.add(new JLabel("        yMin (deltaY) yMax")); 
      yPanel.add(fYLow); yPanel.add(fYStep); yPanel.add(fYUp);
      limitsYPanel.add(yPanel);
      
      buttonsPanel.add(plotButton);   
      buttonsPanel.add(generateCodeButton);

              
      limitsPanel.add(limitsXPanel); limitsPanel.add(limitsYPanel);
      setLayout(new BorderLayout());
      add("North",  functionPanel); 
      add("Center", limitsPanel);
      add("South", buttonsPanel);
      
       
         }


}
