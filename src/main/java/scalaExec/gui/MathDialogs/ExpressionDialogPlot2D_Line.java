package scalaExec.gui.MathDialogs;

import scalaExec.Interpreter.GlobalValues;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        
public class ExpressionDialogPlot2D_Line extends ExpressionDialogPlot2DCommon
{  
    
  public  ExpressionDialogPlot2D_Line()
   {  
       super("Plotting of Function of one variable");
   

      addWindowListener(new WindowAdapter()
      {     @Override
  public void windowClosing(WindowEvent e)
         {  dispose();
            }
      });

      
         plotButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
         
         double lowX = Double.parseDouble(fXLow.getText());
         
         double upX = Double.parseDouble(fXUp.getText());
         double stepX = (upX-lowX)/(double)defaultSampleNumPoints;
         
         String fXStr = fXStep.getText();
         if (fXStr.equals("")==false)
              stepX = Double.parseDouble(fXStep.getText());
         
         String expr = fFun.getText();
         expr = expr.trim().toLowerCase();
         
         expr = expr.replaceAll("exp", "dummy");   // "x" in "exp" causes a problem so remove exp- temporarily
         expr = expr.replaceAll("x", "x(xi)");
         expr = expr.replaceAll("dummy", "exp");
               
        String codeStr =  "import java.lang.Math._;  // shadow any other definitions of basic Math functions \n"+
                "var  nx = Math.floor("+(upX-lowX)+"/"+stepX+").asInstanceOf[Int];  var x = new Array[Double](nx);  \n"+
                 "var y = new Array[Double](nx); \n"+
                 " var nxi=0;  while  (nxi<nx) {  x(nxi) = nxi*"+stepX+" ; \n nxi += 1; \n } \n"+
                 "var xi=0;  while (xi<nx)   {    y(xi) = "+expr+" ; \n"+ "xi += 1; \n  } \n"+
                 plotStr+"\n";
        
        boolean withoutScalaLabLibs = GlobalValues.withoutScalaLabLibs;   // keep previous operation mode
        GlobalValues.withoutScalaLabLibs = false;   // execute in the direct Scala mode avoiding importing scalalab's additional libs
        GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(codeStr);       
        GlobalValues.withoutScalaLabLibs = withoutScalaLabLibs;    // restore previous operation mode
            }

            
        });
       
        generateCodeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         double lowX = Double.parseDouble(fXLow.getText());
         double upX = Double.parseDouble(fXUp.getText());
      
        double stepX = (upX-lowX)/(double)defaultSampleNumPoints;
             String fXStr = fXStep.getText();
         if (fXStr.equals("")==false)
              stepX = Double.parseDouble(fXStep.getText());
      
         
         String expr = fFun.getText();
         expr = expr.trim().toLowerCase();
         
         expr = expr.replaceAll("exp", "dummy");   // "x" in "exp" causes a problem so remove exp- temporarily
         expr = expr.replaceAll("x", "x(xi)");
         expr = expr.replaceAll("dummy", "exp");
         
        String codeStr =  "import java.lang.Math._;  // shadow any other definitions of basic Math functions \n"+
                "var  nx = Math.floor("+(upX-lowX)+"/"+stepX+").asInstanceOf[Int];  var x = new Array[Double](nx);  \n"+
                 "var y = new Array[Double](nx); \n"+
                 " var nxi=0;  while  (nxi<nx) {  x(nxi) = nxi*"+stepX+" ; \n nxi += 1; \n } \n"+
                 "var xi=0;  while (xi<nx)   {    y(xi) = "+expr+" ; \n"+ "xi += 1; \n  } \n"+
                 plotStr+"\n";
        
         if (fCode != null)
           buttonsPanel.remove(fCode);
         fCode = new JTextArea(codeStr);
         buttonsPanel.add(fCode);
         pack();
         
            }
        });
    
  }   
}