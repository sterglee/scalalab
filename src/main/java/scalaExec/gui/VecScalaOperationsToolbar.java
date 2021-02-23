
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;
import  scalaSciCommands.BasicCommands;


public class VecScalaOperationsToolbar  extends JPanel {
        JButton bconstr, bdirect, bcolon, blinspace, blogspace, bsubv,  bdotProduct;
        JButton bones, bzeros, bfill, brand;
        JButton bsum, bmean, bmin, bmax;
        JButton bsin, bcos, btan, basin, bacos, batan,  bsinh, bcosh, btanh;
        JButton bsqrt, blog, bexp, babs, bfloor, bceil;
        JButton bhelp;
        JButton binplace, brightAssoc, bscalarAppend, bscalarPrepend, bscalarAppendLA, bscalarPrependLA, bvectorAppend, bvectorPrepend;
        JButton bfft, bmap;
        
    public VecScalaOperationsToolbar() {
        JPanel vectorPanel1 = new JPanel();
        JPanel vectorPanel2 = new JPanel();
        JPanel vectorPanel3 = new JPanel();
        JPanel vectorPanel4 = new JPanel();
        JPanel vectorPanel5 = new JPanel();
        JPanel vectorPanel6 = new JPanel();
         
        setLayout(new GridLayout(5,1));

        bhelp = new JButton("Help");
        bhelp.setToolTipText("HTML Help on Vector operations");
        bhelp.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("Vec.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
  }
        });
          
        
        bconstr = new JButton(" new Vec(...)");
        bconstr.setToolTipText("specify a Vector from its elements, e.g. var vv = new Vec(Array(9, 8, -0.2)");
        bconstr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"var vv = new Vec(Array(0.0, 0.0   )) ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
       
         
        bdirect = new JButton("V(s:String)");
        bdirect.setToolTipText("initialize directly the Vector with some elements, e.g. var v = V(\"6.7, -6.5\""); 
        bdirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"var v = V(\"  \")");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        
        bcolon = new JButton("inc");
        bcolon.setToolTipText("implements colon operator, e.g. var  t = inc(0, 0.01, 10) is as t = 0:0.01:10");
        bcolon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        

        blinspace = new JButton("linspace");
        blinspace.setToolTipText("Linearly spaced vector, linspace(x1, x2, N) generates N points between x1 and x2");
        blinspace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        blogspace = new JButton("logspace");
        blogspace.setToolTipText("Logarithmically spaced vector, logspace(x1, x2, N, base) generates N points between x1 and x2, with base logarithm");
        blogspace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        bsubv = new JButton("var vr = v(low, inc, high)");
        bsubv.setToolTipText("implements subvector operator, e.g. var  vr = v(3,2,8),  is as vr  = v(3:2:8), var vv = v(8, -1, 2) is v(-8:-1:2)");
        bsubv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"subv(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        
     
        bdotProduct = new JButton(" . ");
        bdotProduct.setToolTipText("implements Vector  dot product, e.g. vd = v dot v is v . v");
        bdotProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+" dot ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
     
        bones = new JButton("vones");
        bones.setToolTipText("creating matrices that consist of ones    (e.g.: vones(20) will return an 20 element vector of ones)");
        
        bones.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"vones(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bzeros = new JButton("vzeros");
        bzeros.setToolTipText("creating vectors that consist of zeros    (e.g.: vzeros(23) will return an  23 element vector)");
        bzeros.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"vzeros(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });


        bfill = new JButton("vfill");
        bfill.setToolTipText("creating vectors that consist of a given value (e.g.: vfill(10, 2.4) will return a  10 element vector with values 2.4)");
        bfill.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"vfill(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });


        brand = new JButton("vrand");
        brand.setToolTipText("construct a matrix filled with pseudorandom values(e.g.: vrand(20)  will return a vector with  20 pseudorandom values ");
                
        brand.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"vrand(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        
        
        bsum = new JButton("sum");
        bsum.setToolTipText( "the sum of all values of the Vector");
        bsum.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"sum(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        
        bmean = new JButton("mean");
        bmean.setToolTipText( "the mean of all values of the Vector");
        bmean.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"mean(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
 
        bmin = new JButton("min");
        bmin.setToolTipText( "the minimum of all values of the Vector");
        bmin.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"min(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });

        bmax = new JButton("max");
        bmax.setToolTipText( "the maximum of all values of the Vector");
        bmax.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"max(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });

        bsin = new JButton("sin");
        bsin.setToolTipText("def sin( x: Vec): Vec ; \n Computes the sine of x");
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
        bcos.setToolTipText("def cos( x: Vec): Vec ; \n Computes the cosine of x");
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
        btan.setToolTipText("def tan( x: Vec): Vec; \n Computes the tangent of x");
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
        bsin.setToolTipText("def sinh( x: Vec): Vec ; \n Computes hyperbolic sine of x");
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
        bcosh.setToolTipText("def cosh( x: Vec): Vec; \n Computes the hyperbolic cosine of x");
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
        btanh.setToolTipText("def tanh( x: Vec): Vec; \n Computes the hyperbolic tangent of x");
        btanh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"tanh(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        basin = new JButton("asin");
        basin.setToolTipText("def asin( x: Vec): Vec; \n Computes the arc sine of x");
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
        bacos.setToolTipText("def acos( x: Vec): Vec; \n Computes the arc cosine of x");
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
        batan.setToolTipText("def atan( x: Vec): Vec; \n Computes the arc tangent of x");
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
        babs.setToolTipText("def abs( x: Vec): Vec; \n Computes the absolute value of x");
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
        bsqrt.setToolTipText("def sqrt( x: Vec): Vec; \n Computes the square root of x");
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
        bfloor.setToolTipText("def floor( x: Vec): Vec; \n Computes the nearest integer smaller than x");

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
        bceil.setToolTipText("def ceil( x: Vec): Vec; \n Computes the nearest integer larger than x");
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
        blog.setToolTipText("def log( x: Vec): Vec;    Computes the natural logarithm ");
        blog.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"log(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        
        bexp= new JButton("exp");
        bexp.setToolTipText("def exp( x: Vec): Vec ;   Computes the e^x");
        bexp.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"exp(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        binplace = new JButton("++, --, ..");
        binplace.setToolTipText("in-place Vec operations for efficiency");
        binplace.addActionListener(new ActionListener() {
       @Override
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("inplace.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
       }
        });

        
        brightAssoc = new JButton("+:, -:, ..");
        brightAssoc.setToolTipText("right associative operators to avoid the implicit conversion");
        brightAssoc.addActionListener(new ActionListener() {
       @Override
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  rightAssocHelpPane = new EditorPaneHTMLHelp("rightAssoc.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
   rightAssocHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
   rightAssocHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
   rightAssocHelpPane.setVisible(true);
       }
       }
        });


        bscalarAppend  = new JButton("::");
        bscalarAppend.setToolTipText("append scalar element as last vector's element,  e.g. var v = vrand(2); var vv = 4.55 ::  v   // 4.55 is placed as the last element of the vector");

        bscalarPrepend  = new JButton(":::");
        bscalarPrepend.setToolTipText("prepend element as first vector's element, e.g.  var v = vrand(2); var vv = 4.55 :::  v   // 4.55 is placed at the front of the vector");

        bscalarAppendLA = new JButton("::<");
        bscalarAppendLA.setToolTipText(" append element as last vector's element,  e.g.  var v = vrand(2); var vv =  v ::<  4.55 // 4.55 is placed as the last element of the vector");

        bscalarPrependLA  = new JButton(":::<");
        bscalarPrependLA.setToolTipText("prepend element as first vector's element,  e.g.  var v = vrand(2); var vv =  v :::< 4.55   // 4.55 is placed at the front of the vector");

        bvectorAppend = new JButton("Vec :: Vec");
        bvectorAppend.setToolTipText("append left-side vector to right-side vector, e.g.  vones(2) :: vrand(3),  0.597  0.122  0.045  1.000  1.000");

        bvectorPrepend = new JButton("Vec:::Vec");
        bvectorPrepend.setToolTipText("prepend left-side vector to right-side vector, e.g.  vones(2) ::: vrand(3), 1.000  1.000  0.212  0.633  0.130");

        bmap = new JButton("map");
        bmap.setToolTipText("applies a function to all the vector's elements, see help for example");
        
        bfft = new JButton("fft");
        bfft.setToolTipText("performs Fast Fourier Transform of the vector's contents");
        
        vectorPanel1.add(bcolon);   vectorPanel1.add(bhelp);         vectorPanel1.add(bconstr);        
        vectorPanel1.add(bdirect);        vectorPanel1.add(blinspace);  vectorPanel1.add(blogspace);
        
        vectorPanel2.add(bsubv);   vectorPanel2.add(bdotProduct);
        vectorPanel2.add(bones);  vectorPanel2.add(bzeros);  vectorPanel2.add(bfill); vectorPanel2.add(brand);
        vectorPanel2.add(bsum); 
        
        vectorPanel3.add(babs); vectorPanel3.add(bmean);
        vectorPanel3.add(bmin);  vectorPanel3.add(bmax);
        vectorPanel3.add(bceil); vectorPanel3.add(bfloor); vectorPanel3.add(bsqrt);
        
        vectorPanel4.add(blog); vectorPanel4.add(bexp);
        vectorPanel4.add(bsin); vectorPanel4.add(bcos); vectorPanel4.add(btan);  vectorPanel4.add(basin); vectorPanel4.add(bacos); 
        vectorPanel4.add(batan);
        
        vectorPanel5.add(bsinh); vectorPanel5.add(bcosh); vectorPanel5.add(btanh); 
        vectorPanel5.add(binplace);   vectorPanel5.add(brightAssoc); vectorPanel5.add(bscalarAppend); vectorPanel5.add(bscalarPrepend);
        
        vectorPanel6.add(bscalarAppendLA); vectorPanel6.add(bscalarPrependLA); vectorPanel6.add(bvectorAppend); 
        vectorPanel6.add(bvectorPrepend);
        vectorPanel6.add(bmap);   vectorPanel6.add(bfft);

        add(vectorPanel1);       add(vectorPanel2);   add(vectorPanel3);  add(vectorPanel4);  add(vectorPanel5); add(vectorPanel6);
        
   }
}

