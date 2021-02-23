
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;
import  scalaSciCommands.BasicCommands;


public class RichDouble1DArrayScalaOperationsToolbar  extends JPanel {
        JButton bconstr, bcolon, blinspace, blogspace, bsubv,  bdotProduct;
        JButton bones, bzeros, bfill, brand;
        JButton bsum, bmean, bmin, bmax;
        JButton bsin, bcos, btan, basin, bacos, batan,  bsinh, bcosh, btanh;
        JButton bsqrt, blog, bexp, babs, bfloor, bceil;
        JButton bhelp;
        JButton bfft;
     
     
    public RichDouble1DArrayScalaOperationsToolbar() {
        JPanel DAPanel = new JPanel();
        JPanel DAPanel2 = new JPanel();
        JPanel DAPanel3 = new JPanel();
        JPanel DAPanel4 = new JPanel();
        
        setLayout(new GridLayout(4,1));

      /*  bhelp = new JButton("Help");
        bhelp.setToolTipText("HTML Help on DA operations");
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
        DAPanel.add(bhelp);

          */
        
        bconstr = new JButton("AD(...)");
        bconstr.setToolTipText("specify a RichDouble1DArray  from its elements, e.g. var zz = new AD(\"9, 8, -0.2 9.8 9.6\")");
        bconstr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"var vv = new Vec(Array(0.0, 0.0   )) ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(bconstr);
        

       
        bcolon = new JButton("Inc");
        bcolon.setToolTipText("implements colon operator, e.g. var  t = Inc(0, 0.01, 10) is as t = 0:0.01:10");
        bcolon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(bcolon);


        blinspace = new JButton("Linspace");
        blinspace.setToolTipText("Linearly spaced RichDouble1DArray, linspace(x1, x2, N).getv generates N points between x1 and x2");
        blinspace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(blinspace);

        blogspace = new JButton("Logspace");
        blogspace.setToolTipText("Logarithmically spaced RichDouble1DArray, logspace(x1, x2, N, base).getv generates N points between x1 and x2, with base logarithm");
        blogspace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(blogspace);

        bsubv = new JButton("var vr = v(low, inc, high)");
        bsubv.setToolTipText("implements subarray operator, e.g. var  vr = v(3,2,8),  is as vr  = v(3:2:8), var vv = v(8, -1, 2) is v(-8:-1:2)");
        bsubv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"subv(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(bsubv);

        
     
        bdotProduct = new JButton(" . ");
        bdotProduct.setToolTipText("implements Array[Double]  dot product, e.g. vd = v dot v is v . v");
        bdotProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+" dot ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        DAPanel.add(bdotProduct);

     
        bones = new JButton("vones");
        bones.setToolTipText("creating matrices that consist of ones    (e.g.: vones(20).getv will return an 20 element Array[Double] of ones)");
        
        bones.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Ones(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bzeros = new JButton("Zeros");
        bzeros.setToolTipText("creating Array[Double] that consist of zeros    (e.g.: Zeros(23).getv will return an  23 element Array[Double])");
        bzeros.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Zeros(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });


        bfill = new JButton("Fill");
        bfill.setToolTipText("creating Array[Double] that consist of a given value (e.g.: Fill(10, 2.4) will return a  10 element Array[Double] with values 2.4)");
        bfill.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Fill(");
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

        
        bfft = new JButton("fft");
        bfft.setToolTipText("performs Fast Fourier Transform of the vector's contents");
        
           bcolon = new JButton("Inc");
        bcolon.setToolTipText("implements colon operator, e.g. var  t = Inc(0, 0.01, 10) is as t = 0:0.01:10");
        bcolon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Inc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        
        DAPanel.add(bdotProduct); DAPanel.add(bcolon);
        DAPanel2.add(bones);  DAPanel2.add(bzeros);  DAPanel2.add(bfill); DAPanel2.add(brand);
        DAPanel2.add(bsum); 
        DAPanel2.add(babs); DAPanel2.add(bmean);
        DAPanel2.add(bmin);  DAPanel2.add(bmax);
        DAPanel2.add(bceil); DAPanel2.add(bfloor); DAPanel2.add(bsqrt);
        DAPanel2.add(blog); DAPanel2.add(bexp);

        DAPanel3.add(bsin); DAPanel3.add(bcos); DAPanel3.add(btan);  DAPanel3.add(basin); DAPanel3.add(bacos); DAPanel3.add(batan);
        DAPanel3.add(bsinh); DAPanel3.add(bcosh); DAPanel3.add(btanh); 

        DAPanel4.add(bfft);

        add(DAPanel);       add(DAPanel2);   add(DAPanel3);  add(DAPanel4);  
        
   }
}

