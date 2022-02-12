
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class DoubleDoubleScalaOperationsToolbar  extends JPanel {
        JButton bconstr, bcolon;
        JButton bones, bzeros, brand, binv, btrans;
        JButton bsum, bdet, beig, blu, bsvd,  bqr;
        JButton bsin, bcos, btan, basin, bacos, batan,  bsinh, bcosh, btanh;
        JButton bcorr;
        JButton bnormInf, bnorm1, bnorm2, bnormF;
        JButton bdirect, bsolsys;
        
    public DoubleDoubleScalaOperationsToolbar() {
        JPanel matrixPanel1 = new JPanel();
        JPanel matrixPanel2 = new JPanel();
        JPanel matrixPanel3 = new JPanel();
        JPanel matrixPanel4 = new JPanel();
        JPanel matrixPanel5 = new JPanel();
        
        setLayout(new GridLayout(5,1));

        
        bdirect = new JButton("ADD(s:String)");
        bdirect.setToolTipText("initialize directly the Array[Array[Double]] with some elements, e.g. var A = ADD(\"6.7, -6.5; -3.4 23)\"");
        bdirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"var A =  ADD(\" ...  ; ... \"");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel1.add(bdirect);
        
        
        bsolsys  = new JButton("\\");
        bsolsys.setToolTipText("Solves a linear system, e.g. var A = AAD(\"1 2 4; 1 3 9; 1 4 9\"); var b = AD(\"2 4 7\"); var x = A \\ b\"");
        matrixPanel1.add(bsolsys);
        
        bcorr = new JButton("corr");
        bcorr.setToolTipText("corr(M1. M2): correlation of matrices M1  and M2");
        bcorr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"corr(M1, M2)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel4.add(bcorr);
        
        bdet = new JButton("det");
        bdet.setToolTipText("det(M): determinant of M");
        bdet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"det(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel4.add(bdet);
        
        bconstr = new JButton("Array[Array[Double]](N,M)");
        bconstr.setToolTipText("specify a 2-D double array from its elements, e.g.  Array(Array(3.4, 5, 6.7), Array(5.6, -5.6, 7.8))");
        bconstr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Array(Array(0.0, 0.0   ), Array(0.0, 0.0)) ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel1.add(bconstr);
        
        
             
        bones = new JButton("Ones");
        bones.setToolTipText("creating matrices that consist of ones    (e.g.: ones(2) will return a 2-by-2 matrix of ones)");
        
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
        bzeros.setToolTipText("creating matrices that consist of zeros    (e.g.: zeroes(2,3) will return a 2-by-3 matrix of zeroes)");
        bzeros.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Zeros(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        brand = new JButton("Rand");
        brand.setToolTipText("construct a matrix filled with pseudorandom values(e.g.: rand(2,3)  will return a 2-by-3 matrix of pseudorandom values ");
                
        brand.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Rand(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        binv = new JButton("inv");
        binv.setToolTipText("determining the inverse of a matrix");
        binv.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"inv(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        btrans = new JButton("~");
        btrans.setToolTipText("determining the transpose of a 2-D array A, e.g. var At = T(A) or or var Ar = trans(A) or  var At = A~");
        btrans.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"T(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        
        bsum = new JButton("sum");
        bsum.setToolTipText( "the sum of all values within the matrix or structure.  Sums are computed columnwise on matrices");
        bsum.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"sum(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bdet = new JButton("det");
        bdet.setToolTipText("computes the determinant of the Array[Array[Double]], d = det(Array[Array[Double]] A)");
        bdet.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"det(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        beig = new JButton("eig");
        beig.setToolTipText("computing eigenvalues and eigenVectors of an array");
        beig.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
             EditorPaneHTMLHelp  eigHelpPane = new EditorPaneHTMLHelp("eig.html");
                 if (GlobalValues.useSystemBrowserForHelp==false) {
                eigHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 eigHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 eigHelpPane.setVisible(true);
                 } 
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"eig(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        blu = new JButton("lu");
        blu.setToolTipText("For an m-by-n matrix A with m >= n, the LU decomposition is \n"+
                  "an m-by-n unit lower triangular matrix L, an n-by-n upper triangular matrix U, \n"+
                  "and a permutation Mattor piv of length m so that A(piv,:) = L*U. \n"+
                  "If m < n, then L is m-by-m and U is m-by-n. \n\n"+
                  "The LU decompostion with pivoting always exists, even if the matrix is \n"+
                  "singular, so the constructor will never fail.  The primary use of the \n"+
                  "LU decomposition is in the solution of square systems of simultaneous \n"+
                  "linear equations.  This will fail if isNonsingular() returns false. \n\n"+
                  " usage: [L,U]   = lu (A) \n"+
                  "[L,U,P] = lu (A) \n"+
                  "[L]     = lu (A) \n"+
                  "x       = lu (A,B) as a solution to A*X = B; ");
        blu.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"lu(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        
        bsvd = new JButton("svd");
        bsvd.setToolTipText(" Singular Value Decomposition: \n\n"+
        "For an m-by-n matrix A with m >= n, the singular value decomposition is \n"+
        "an m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and \n"+
        "an n-by-n orthogonal matrix V so that A = U*S*V'. \n\n"+
        "The singular values, sigma[k] = S[k][k], are ordered so that \n"+
        "sigma[0] >= sigma[1] >= ... >= sigma[n]. \n"+
        "The singular value decompostion always exists, so the constructor will \n"+
        "never fail.  The matrix condition number and the effective numerical \n"+
        "rank can be computed from this decomposition. \n\n"+
        "usage: s = svd(A) \n"+
        "[U,S,V]=svd(A)");
        bsvd.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"svd(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bqr = new JButton("qr");
        bqr.setToolTipText("QR Decomposition. \n\n"+
        "For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n \n"+
        "orthogonal matrix Q and an n-by-n upper triangular matrix R so that \n"+
        "A = Q*R. \n"+
        "The QR decompostion always exists, even if the matrix does not have \n"+
        "full rank, so the constructor will never fail.  The primary use of the \n"+
        "QR decomposition is in the least squares solution of nonsquare systems \n"+
        "of simultaneous linear equations.  This will fail if isFullRank() \n"+
        "returns false.");
        bqr.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"qr(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
                
        
        

        bsin = new JButton("sin");
        bsin.setToolTipText("def sin( x: Array[Array[Double]]): Array[Array[Double]] ; \n Computes the sine of x");
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
        bcos.setToolTipText("def cos( x: Array[Array[Double]]): Array[Array[Double]] ; \n Computes the cosine of x");
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
        btan.setToolTipText("def tan( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the tangent of x");
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
        bsin.setToolTipText("def sinh( x: Array[Array[Double]]): Array[Array[Double]] ; \n Computes hyperbolic sine of x");
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
        bcosh.setToolTipText("def cosh( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the hyperbolic cosine of x");
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
        btanh.setToolTipText("def tanh( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the hyperbolic tangent of x");
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
        basin.setToolTipText("def asin( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the arc sine of x");
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
        bacos.setToolTipText("def acos( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the arc cosine of x");
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
        batan.setToolTipText("def atan( x: Array[Array[Double]]): Array[Array[Double]]; \n Computes the arc tangent of x");
        batan.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"atan(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        
            bnorm1 = new JButton("norm1");
    bnorm1.setToolTipText("norm1 (or norm(M, 1): sum_{i,j}abs(M(i,j)");
    bnorm1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"norm1(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(bnorm1);


        bnorm2 = new JButton("norm2");
        bnorm2.setToolTipText("norm1 (or norm(M, 2)");
       bnorm2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"norm2(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(bnorm2);

    bnormF = new JButton("normF");
    bnormF.setToolTipText("normF (or norm(M, Fro)");
    bnormF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"normF(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(bnormF);

        //  bLU_L, bLU_P, bLU_U,
    bnormInf = new JButton("normInf");
    bnormInf.setToolTipText("normInf  (or norm(M, Inf)");
    bnormInf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"normInf(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(bnormInf);


        //matrixPanel.add(browAppend); matrixPanel.add(bcolAppend); 
        matrixPanel2.add(bones);  matrixPanel2.add(bzeros);  matrixPanel2.add(brand);
        matrixPanel2.add(binv); 
        matrixPanel2.add(btrans);
        matrixPanel2.add(bsum); 
        matrixPanel2.add(bdet); 
        matrixPanel2.add(beig); 
        matrixPanel2.add(blu); matrixPanel2.add(bsvd);
        matrixPanel2.add(bqr);    
        
        
        matrixPanel3.add(bsin); matrixPanel3.add(bcos); matrixPanel3.add(btan);  matrixPanel3.add(basin); matrixPanel3.add(bacos); matrixPanel3.add(batan);
        matrixPanel3.add(bsinh); matrixPanel3.add(bcosh); matrixPanel3.add(btanh);

        matrixPanel5.add(bnorm1); matrixPanel5.add(bnorm2); matrixPanel5.add(bnormF); matrixPanel5.add(bnormInf);
   
     
        add(matrixPanel1);       add(matrixPanel2);    add(matrixPanel3); add(matrixPanel4); add(matrixPanel5);
        
   }
}

