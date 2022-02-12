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

        
public class MinInDialog extends JFrame
{  
    JTextArea  fCode = new JTextArea("def  fx( x: Array[Double]) = { \n // Code of function, use x(0) as x argument, e.g. cos(x(0)) instead of cos(x)  \n \n }\n");
    JTextArea  genCode;
    JTextField  fXLow = new JTextField(10);
    JTextField  fXStep = new JTextField(10);
    JTextField  fXUp = new JTextField(10);
    JButton buttonGenerateCode = new JButton("Generate Code");
    JButton plotButton = new JButton(" Plot  ");
    JPanel functionPanel = new JPanel(), limitsPanel = new JPanel(), buttonsPanel = new JPanel();
    JTextArea  fTol;
    String codeStr, tolStr;
    
   public MinInDialog()
   {  
       setTitle("Minimum of a single variable function in an interval");
       fCode.setFont(GlobalValues.defaultTextFont);
    
       tolStr = "def  tolx(x: Array[Double]) =  { Math.abs(x(0))*1.0e-6+1.0e-6 }";
       fTol = new JTextArea(tolStr);
       fTol.setFont(GlobalValues.defaultTextFont);

       plotButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         String expr = fCode.getText();
                     // extract the function name
         int startIdx = expr.indexOf("def")+4;  // after def
         int endIdx = expr.indexOf('(');
         String fname = expr.substring(startIdx, endIdx);
         double lowX = Double.parseDouble(fXLow.getText());
         double stepX = Double.parseDouble(fXStep.getText());
         double upX = Double.parseDouble(fXUp.getText());
         
         codeStr = expr+ "\n\nvar  x = Inc("+lowX+","+stepX+","+upX+"); \n"+
                                    "var xlen = x.length \n"+
                                   "var y = new Array[Double](xlen) \n"+
                                   "var yt = new Array[Double](1) \n"+
                                   "for (k<-0 to xlen-1)  { yt(0)=x(k);  y(k) = "+fname+"(yt); }\n"+
                                   "plot(x,y);";
        System.out.println("codeStr = "+codeStr);
        GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(codeStr);     
            }
        });
       
        buttonGenerateCode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         String expr = fCode.getText();    
         double lowX = Double.parseDouble(fXLow.getText());
         double stepX = Double.parseDouble(fXStep.getText());
         double upX = Double.parseDouble(fXUp.getText());
         
         String codeStr = " var x = new  Array[Double](1); \n  var a = new Array[Double](1); \n  var  b = new Array[Double](1); \n  " +
                 "var mininClass = new  MininClassTemplate(); \n"+
                 "a(0) = "+lowX+";\n"+
                 "b(0) = "+upX+";\n"+
                 "var m = Analytic_problems.minin(x, a, b, mininClass); \n"+
                 "System.out.println(\"Minimum is  \"+x(0)); "+

                 "// the wrapper class declaration follows \n"+
                 "\n\n class MininClassTemplate extends AnyRef  with  AP_minin_methods { \n"+
                 expr+
                 tolStr+
                 "\n }";
  
         if (genCode != null) { functionPanel.remove(genCode); }
         if (fTol != null)  { functionPanel.remove(fTol);  }
                   
         genCode = new JTextArea(codeStr);    fTol = new JTextArea(tolStr);
         genCode.setFont(GlobalValues.defaultTextFont);  fTol.setFont(GlobalValues.defaultTextFont);
         
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