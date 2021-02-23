
package scalaExec.gui;

import scalaExec.gui.MathDialogs.ExpressionDialogPlot3D_Grid;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Line;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Histo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class BasicScalaMathOperationsToolbar  extends JPanel {
        JButton bwhos, bdir, bcd, bmd, bcls;
        JButton bsin, bcos, btan, basin, bacos, batan,  bsinh, bcosh, btanh, basinh, bacosh, batanh;
        JButton bsqrt, blog, bexp, babs, bfloor, bceil, bln;
         
    public BasicScalaMathOperationsToolbar() {
        JPanel basicPanel = new JPanel();
        
        setLayout(new GridLayout(2,1));

        
        bsin = new JButton("sin");
        bsin.setToolTipText("double  sin(double x); \n Computes the sine of x");
        bsin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"sin(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
      
        bcos= new JButton("cos");
        bcos.setToolTipText("double  cos(double x); \n Computes the cosine of x");
        bcos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"cos(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        btan= new JButton("tan");
        btan.setToolTipText("double  tan(double x); \n Computes the tangent of x");
        btan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"tan(");
             GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
             // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
          }
        });
      
      
        bsinh = new JButton("sinh");
        bsin.setToolTipText("double  sinh(double x); \n Computes hyperbolic sine of x");
        bsinh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"sinh(");
             GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
        
            }
        });
        bcosh= new JButton("cosh");
        bcosh.setToolTipText("double  bcosh(double x); \n Computes the hyperbolic cosine of x");
        bcosh.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"cosh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        btanh= new JButton("tanh");
        btanh.setToolTipText("double  btanh(double x); \n Computes the hyperbolic tangent of x");
        btanh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"tanh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        basinh = new JButton("asinh");
        basinh.setToolTipText("double  asinh(double x); \n Computes the arc sine of x");
        basinh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"asinh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bacosh = new JButton("acosh");
        bacosh.setToolTipText("double  acosh(double x); \n Computes the arc cosine of x");
        bacosh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"acosh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        batanh = new JButton("atanh");
        batanh.setToolTipText("double  atanh(double x); \n Computes the arc tangent of x");
        batanh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"atanh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        basin = new JButton("asin");
        basin.setToolTipText("double  asin(double x); \n Computes the arc sine of x");
        basin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"asin(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bacos= new JButton("acos");
        bacos.setToolTipText("double  acos(double x); \n Computes the arc cosine of x");
        bacos.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"acos(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
           
        batan= new JButton("atan");
        batan.setToolTipText("double  atan(double x); \n Computes the arc tangent of x");
        batan.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"atan(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
          
        babs = new JButton("abs");
        babs.setToolTipText("double  abs(double x); \n Computes the absolute value of x");
        babs.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"abs(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bsqrt = new JButton("sqrt");
        bsqrt.setToolTipText("double  sqrt(double x); \n Computes the square root of x");
        bsqrt.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"sqrt(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bfloor = new JButton("floor");
        bfloor.setToolTipText("double  floor(double x); \n Computes the nearest integer smaller than x");
   
        bfloor.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"floor(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bceil = new JButton("ceil");
        bceil.setToolTipText("double  ceil(double x); \n Computes the nearest integer larger than x");
        bceil.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"ceil(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        
        blog= new JButton("log");
        blog.setToolTipText("double  log(double x, double base);    Computes the logarithm of the number x to the base base");
        blog.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"log(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bln = new JButton("ln");
        bln.setToolTipText("double  ln(double x); \n Computes the natural logarithm of the number x ");
        bln.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"ln(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bexp= new JButton("exp");
        bexp.setToolTipText("double  exp(double x)   Computes the e^x");
        bexp.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"exp(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        basicPanel.add(bsin); basicPanel.add(bcos); basicPanel.add(btan);  basicPanel.add(basin); basicPanel.add(bacos); basicPanel.add(batan);
        basicPanel.add(bsinh); basicPanel.add(bcosh); basicPanel.add(btanh); basicPanel.add(basinh); basicPanel.add(bacosh); basicPanel.add(batanh);
        
        basicPanel.add(babs); basicPanel.add(bceil); basicPanel.add(bfloor); basicPanel.add(bsqrt);
        basicPanel.add(blog); basicPanel.add(bln); basicPanel.add(bexp);  

      
        add(basicPanel); 
        
   }
}

