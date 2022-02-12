package scalaExec.gui.MathDialogs;

import scalaExec.Interpreter.GlobalValues;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

        
public class ExpressionDialogPlot3D_Grid extends ExpressionDialog3DCommon
{  
      String  plotStr = "plot(x, y, z);";

  public  ExpressionDialogPlot3D_Grid(String title)
   {
      super(title);
   setTitle("3-D Grid Plot");
  
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
         double lowY = Double.parseDouble(fYLow.getText());
         double upY = Double.parseDouble(fYUp.getText());
 // compute default stepX and stepY values to use, not specified explicitly         
         double stepX = (upX-lowX)/(double)defaultSampleNumPoints;
         double stepY = (upY-lowY)/(double)defaultSampleNumPoints;
         
         String fXStr = fXStep.getText();
         if (fXStr.equals("")==false)
              stepX = Double.parseDouble(fXStep.getText());
         
         String fYStr = fYStep.getText();
         if (fYStr.equals("")==false)
              stepY = Double.parseDouble(fYStep.getText());
         
         String expr = fFun.getText();
         expr = expr.trim().toLowerCase();
         expr = expr.replaceAll("exp", "dummy");   // "x" in "exp" causes a problem so remove exp- temporarily
         expr = expr.replaceAll("x", "x(nxi)");
         expr = expr.replaceAll("dummy", "exp");
         expr = expr.replaceAll("y", "y(nyi)");
               
        String codeStr = "       import java.lang.Math._;  // shadow any other definitions of basic Math functions  \n"+
                "var  nx = Math.floor("+(upX-lowX)+"/"+stepX+").asInstanceOf[Int];  var x = new Array[Double](nx);  \n"+
                "var  ny = Math.floor("+(upY-lowY)+"/"+stepY+").asInstanceOf[Int];   var y = new Array[Double](ny); \n"+
                 "var z = Array.ofDim[Double](ny); \n"+
                 "var c=0;  while (c<ny) { z(c) = new Array[Double](nx);  c += 1; } \n"+
                 " var nxi=0;  while  (nxi<nx) {  x(nxi) = nxi*"+stepX+" ; \n nxi += 1; \n } \n"+
                 " var nyi=0;  while (nyi<ny)  { y(nyi) = nyi*"+stepY+"; \n nyi += 1; \n } \n"+
                 " nxi=0;  nyi=0;  while (nxi<nx) { while (nyi<ny)  {  z(nyi)(nxi) = "+expr + ";"+ " nyi+=1; }  \n nxi += 1; } \n "+
                 plotStr+"\n";

        GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(codeStr);     
            }

            
        });
       
        generateCodeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         double lowX = Double.parseDouble(fXLow.getText());
         double upX = Double.parseDouble(fXUp.getText());
         
         double lowY = Double.parseDouble(fYLow.getText());
         double upY = Double.parseDouble(fYUp.getText());
   
  // compute default stepX and stepY values to use, not specified explicitly         
         double stepX = (upX-lowX)/(double)defaultSampleNumPoints;
         double stepY = (upY-lowY)/(double)defaultSampleNumPoints;
         
         String fXStr = fXStep.getText();
         if (fXStr.equals("")==false)
              stepX = Double.parseDouble(fXStep.getText());
         
         String fYStr = fYStep.getText();
         if (fYStr.equals("")==false)
              stepY = Double.parseDouble(fYStep.getText());
         
         String expr = fFun.getText();
         expr = expr.trim().toLowerCase();
         
         expr = expr.replaceAll("exp", "dummy");   // "x" in "exp" causes a problem so remove exp- temporarily
         expr = expr.replaceAll("x", "x(nxi)");
         expr = expr.replaceAll("dummy", "exp");
         expr = expr.replaceAll("y", "y(nyi)");
         
        String codeStr = "       import java.lang.Math._;  // shadow any other definitions of basic Math functions  \n"+
                "var  nx = Math.floor("+(upX-lowX)+"/"+stepX+").asInstanceOf[Int];  var x = new Array[Double](nx);  \n"+
                "var  ny = Math.floor("+(upY-lowY)+"/"+stepY+").asInstanceOf[Int];   var y = new Array[Double](ny); \n"+
                 "var z = Array.ofDim[Double](ny); \n"+
                 "var c=0;  while (c<ny) { z(c) = new Array[Double](nx);  c += 1; } \n"+
                 " var nxi=0;  while  (nxi<nx) {  x(nxi) = nxi*"+stepX+" ; \n nxi += 1; \n } \n"+
                 " var nyi=0;  while (nyi<ny)  { y(nyi) = nyi*"+stepY+"; \n nyi += 1; \n } \n"+
                 " nxi=0;  nyi=0;  while (nxi<nx) { while (nyi<ny)  {  z(nyi)(nxi) = "+expr + ";"+ " nyi+=1; }  \n nxi += 1; } \n "+
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