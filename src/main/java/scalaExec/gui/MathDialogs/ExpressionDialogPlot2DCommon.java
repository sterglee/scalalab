package scalaExec.gui.MathDialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

        
public class ExpressionDialogPlot2DCommon extends JFrame
{  
    static public int defaultSampleNumPoints = 100;
    JTextField  fFun = new JTextField(50);
    JButton generateCodeButton = new JButton("Generate Code");
    JButton plotButton = new JButton(" Plot  ");
    JPanel functionPanel = new JPanel(),  limitsXPanel = new JPanel(), buttonsPanel = new JPanel();
    JPanel plotFormatPanel = new JPanel();
    JPanel limitsAndFormatPanel = new JPanel();
    JTextArea  fCode;  
    JTextField  fXLow = new JTextField(10);
    JTextField  fXStep = new JTextField(10);
    JTextField  fXUp = new JTextField(10);
    String  plotStr = "plot(x, y);";
    JPanel namePanel = new JPanel();
    JTextField  nameOfPlot = new  JTextField(15);

  public  ExpressionDialogPlot2DCommon(String frameName)
   {  
       setTitle(frameName);
   
      addWindowListener(new WindowAdapter()
      {     @Override
  public void windowClosing(WindowEvent e)
         {  dispose();
            }
      });

      functionPanel.add(new JLabel("y = f(x) = ")); 
      functionPanel.add(fFun);
      JPanel  xPanel = new JPanel();
      xPanel.add(new JLabel("xMin (deltaX) xMax")); 
      xPanel.setToolTipText("The limits for the x variable");
      fXLow.setToolTipText("the lower limit of the x variable, e.g. -7");
      fXUp.setToolTipText("the upper limit of the x variable, e.g. 4,8");
      fXStep.setToolTipText("the sampling interval for the x-axis, optional if not specified 100 points in x-range are uniformly sampled");
      xPanel.add(fXLow); xPanel.add(fXStep); xPanel.add(fXUp);
      limitsXPanel.add(xPanel);
      
      buttonsPanel.add(plotButton);   
      buttonsPanel.add(generateCodeButton);
      
      JPanel  lineTypePanel = new JPanel();
      lineTypePanel.add(new JLabel("Line Type: "));
      String [] plotTypes = {"Continuous Line", "Dot-Dot", "Dot-Dash"};
      JComboBox  plotTypeCombo = new JComboBox(plotTypes);
      lineTypePanel.add(plotTypeCombo);
      plotFormatPanel.add(lineTypePanel);
      
      namePanel.add(new JLabel("Plot Name: "));
      namePanel.add(nameOfPlot);
       
      limitsAndFormatPanel.setLayout(new GridLayout(3,1));
      limitsAndFormatPanel.add(namePanel);
      limitsAndFormatPanel.add(limitsXPanel);
      limitsAndFormatPanel.add(plotFormatPanel);
      
      setLayout(new BorderLayout());
      add("North",  functionPanel); 
      add("Center", limitsAndFormatPanel);
      add("South", buttonsPanel);
      
         
  }   
}