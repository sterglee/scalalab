package scalaExec.gui.MathDialogs;

import scalaExec.Interpreter.GlobalValues;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

        
public class LineMinDialog extends JFrame
{  
    JTextArea  fCode = new JTextArea("public double fx( double [] x) { \n // Code of function  \n \n }\n" +
            "public double dfx(double [] x) { \n+" +
            "  // code for the derivative of the function \n \n }\n ");
    JTextArea  genCode;
    JTextField  fXLow = new JTextField(10);
    JTextField  fXStep = new JTextField(10);
    JTextField  fXUp = new JTextField(10);
    JButton buttonGenerateCode = new JButton("Generate Code");
    JButton plotButton = new JButton(" Plot  ");
    JPanel functionPanel = new JPanel(), limitsPanel = new JPanel(), buttonsPanel = new JPanel();
    JTextArea  fTol;
    String codeStr, tolStr;
    
   public LineMinDialog()
   {  
       setTitle("Minimum of a single variable function in an interval");
   
       tolStr = "public double tolx(double [] x)  { return (Math.abs(x[0])*1.0e-6+1.0e-6); }";
       fTol = new JTextArea(tolStr);

       plotButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         String expr = fCode.getText();    
         double lowX = Double.parseDouble(fXLow.getText());
         double stepX = Double.parseDouble(fXStep.getText());
         double upX = Double.parseDouble(fXUp.getText());
         
         codeStr = "x = inc("+lowX+","+stepX+","+upX+"); \n"+
                                   "y = "+expr+";\n"+
                                   "plot(x,y);";
         
         GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(codeStr);     
            }
        });
       
        buttonGenerateCode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         String expr = fCode.getText();    
         double lowX = Double.parseDouble(fXLow.getText());
         double stepX = Double.parseDouble(fXStep.getText());
         double upX = Double.parseDouble(fXUp.getText());
         
         String codeStr = " x = new double[1]; \n  a = new double[1]; \n b = new double[1]; \n  " +
                 "mininClass = new  MininClassTemplate(); \n"+
                 "a[0] = "+lowX+"\n"+
                 "b[0] = "+upX+"\n"+
                 "m = Analytic_problems.minin(x, a, b, mininClass); \n"+
                 "System.out.println(\"Minimum is  \"+m); "+
         
                 "// the wrapper class declaration follows \n"+
                 "\n\npublic class MininClassTemplate extends Object implements AP_minin_methods { \n"+
                 expr+  
                 tolStr+
                 "\n }";
         
         if (genCode != null) { functionPanel.remove(genCode); }
         if (fTol != null)  { functionPanel.remove(fTol);  }
                   
         genCode = new JTextArea(codeStr);    fTol = new JTextArea(tolStr);
         
         functionPanel.add(genCode); functionPanel.add(fTol);
         pack();
         
            }
        });
       
        
      addWindowListener(new WindowAdapter()
      {     @Override
  public void windowClosing(WindowEvent e)
         {  dispose();
            }
      });

      functionPanel.add(new JLabel("y = f(x) = ")); 
      functionPanel.add(fCode); functionPanel.add(fTol);
      limitsPanel.add(new JLabel("xMin (deltaX) xMax")); 
      JPanel  xAxisPanel = new JPanel();
      xAxisPanel.add(fXLow); xAxisPanel.add(fXStep);  xAxisPanel.add(fXUp);
      limitsPanel.add(xAxisPanel); 
      buttonsPanel.add(plotButton);
      buttonsPanel.add(buttonGenerateCode);
      
      setLayout(new BorderLayout());
      add("North", functionPanel); 
      add("Center", limitsPanel);
      add("South", buttonsPanel);
    }
}