
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class RichDouble2DScalaOperationsToolbar  extends JPanel {
        JButton brddAccess, bdirect, bconstr,  bsubm,  bsubmr, bsubmc,  bdotProduct;
        JButton bones, bzeros, brand, beye, btrans, binv, butriag, breshape;
        JButton bsum, bdet,  blu, bsvd,  bqr;
        JButton bsin, bcos, btan, basin, bacos, batan,  bsinh, bcosh, btanh;
        JButton btoMat, btoMatrix, vecToMat, bmap;
        JButton binplace, brightAssoc, browAppend, browPrepend, bcolAppend, bcolPrepend;
        JButton bcorr, brank, beig3, beigV, beigD, btrace,  bLU_L, bLU_P, bLU_U;
        JButton bnormInf, bnorm1, bnorm2, bnormF;
        
    
    public RichDouble2DScalaOperationsToolbar() {
        JPanel matrixPanel = new JPanel();
        JPanel matrixPanel2 = new JPanel();
        JPanel matrixPanel3 = new JPanel();
        JPanel matrixPanel4 = new JPanel();
        JPanel matrixPanel5 = new JPanel();
        JPanel matrixPanel6 = new JPanel();
        
        setLayout(new GridLayout(6,1));
        
  
        JButton brddHelp = new JButton("Help");
        brddHelp.setToolTipText("Basic Help on the RichDouble2DArray class");
        brddHelp.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 EditorPaneHTMLHelp  matHelpPane = new EditorPaneHTMLHelp("RichDouble2DArray.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
                 matHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 matHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 matHelpPane.setVisible(true);
}
            }
        });
        
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
        matrixPanel5.add(bcorr);
        
        brank = new JButton("rank");
        brank.setToolTipText("rank(M): rank of M");
        brank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"rank(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(brank);
        
              beig3 = new JButton("eig");
        beig3.setToolTipText("computing eigenvalues and eigenVectors of an array");
        beig3.addActionListener(new ActionListener() {
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
        matrixPanel6.add(beig3);
  
        
    beigD = new JButton("eigD");
    beigD.setToolTipText("eigD(M): eigenvalues of M");
    beigD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"eigD(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel6.add(beigD);
        
        
    beigV = new JButton("eigV");
    beigV.setToolTipText("eigV(M): eigenvectors of M");
    beigV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"eigV(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel6.add(beigV);
        
    btrace = new JButton("trace");
    btrace.setToolTipText("trace of Matrix");
    btrace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"trace(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel5.add(btrace);

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

        //   bLU_P, bLU_U,
    bLU_L = new JButton("LU_L");
    bLU_L.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"LU_L(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel6.add(bLU_L);

//    bLU_U
    bLU_P = new JButton("LU_P");
    bLU_P.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"LU_P(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel6.add(bLU_P);

    bLU_U = new JButton("LU_U");
    bLU_U.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"LU_U(M)");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());

           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel6.add(bLU_U);

        brddAccess = new JButton("Accessing");
        brddAccess.setToolTipText("Accessing RichDouble2DArray elements with Matlab-like operations");
        brddAccess.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {

                 EditorPaneHTMLHelp  eigHelpPane = new EditorPaneHTMLHelp("MatrixAccess.htm");
                 eigHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 eigHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 eigHelpPane.setVisible(true);

           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bconstr = new JButton("new RichDouble2DArray ..");
        bconstr.setToolTipText("specify a  zero-indexed Matrix from its elements, e.g. var rd = new RichDouble2DArray(Array(Array(3.4, 5, 6.7), Array(5.6, -5.6, 7.8)))");
        bconstr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"new Mat(Array(Array(0.0, 0.0   ), Array(0.0, 0.0))) ");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(brddHelp);
        matrixPanel.add(brddAccess);
        matrixPanel.add(bconstr);
        
        bdirect = new JButton("AAD(s:String)");
        bdirect.setToolTipText("initialize directly the RichDouble2DArray with some elements, e.g. var m = AAD(\"6.7, -6.5; -3.4 23)");
        bdirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"var m =  M0(\" ...  ; ... \"");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
        
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(bdirect);
        matrixPanel.add(brddAccess);
        matrixPanel.add(bconstr);
        
        btoMat = new JButton("toMat");
        btoMat.setToolTipText("convert an one indexed Matrix (i.e. Matrix class) to a  zero indexed one (i.e. Mat)");
        btoMat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"toMat(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(btoMat);
        
        btoMatrix = new JButton("toMatrix");
        btoMatrix.setToolTipText("convert a zero indexed Mat (i.e. Mat class) to an one indexed one (i.e. Matrix)");
        btoMatrix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"toMatrix(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(btoMatrix);
        
  
        vecToMat = new JButton("vecToMat");
        vecToMat.setToolTipText("convert a Vec to zero indexed Mat (i.e. Mat class) ");
        vecToMat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"vecToMat(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(vecToMat);
      
        
        bmap = new JButton("map");
        bmap.setToolTipText("maps a function to all the elements of the Matrix, returning a new matrix, see Help foe example");
        matrixPanel.add(bmap);
        
        
        
        bsubm = new JButton("subm");
        bsubm.setToolTipText("implements submatrix operator, e.g. m = M.subm(3, 2, 8, 2, 4, 16), or m=M(3, 2, 8, 2, 4, 16)  is as m  = M(3:2:8, 2:4:16)");
        bsubm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"subm(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(bsubm);

        bsubmr = new JButton("submr");
        bsubmr.setToolTipText("implements row submatrix operator, e.g. mr = M.subm(3, 2, 8),  is as mr  = M(3:2:8, :)");
        bsubmr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"submr(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(bsubmr);

        
        bsubmc = new JButton("submc");
        bsubmc.setToolTipText("implements column submatrix operator, e.g. mc = M.submc(2, 4, 16),  is as m  = M(:, 2:4:16)");
        bsubmc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"submc(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(bsubmc);

     
        bdotProduct = new JButton(" . ");
        bdotProduct.setToolTipText("implements Matrix dot product, e.g. mc = dot(M, M) is M . M");
        bdotProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"dot(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        matrixPanel.add(bdotProduct);

     // TO DO STERG
        bcolAppend = new JButton(">>>");
        bcolAppend.setToolTipText("Append columns to matrix");
        bcolAppend.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+">>>");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

             // TO DO STERG
        browAppend = new JButton(">>");
        browAppend.setToolTipText("Append rows to matrix");
        browAppend.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+">>");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bones = new JButton("ones");
        bones.setToolTipText("creating matrices that consist of ones    (e.g.: ones(2,2) will return a 2-by-2 matrix of ones)");
        
        bones.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"ones(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bzeros = new JButton("zeros");
        bzeros.setToolTipText("creating matrices that consist of zeros    (e.g.: zeros(2,3) will return a 2-by-3 matrix of zeroes)");
        bzeros.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"zeros(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        brand = new JButton("rand");
        brand.setToolTipText("construct a matrix filled with pseudorandom values(e.g.: rand(2,3)  will return a 2-by-3 matrix of pseudorandom values ");
                
        brand.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"rand(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        beye = new JButton("eye");
        beye.setToolTipText( "creating matrices that consist of zeros."+
                   "everywhere except in the diagonal. The diagonal consists of ones.  \n\n"+
                   "e.g.: eye(3) will return a 3-by-3 matrix [1,0,0;0,1,0;0,0,1], \n"+
                   "eye(4,3) will return a 4-by-3 matrix with diagonal set to 1 "); 
        beye.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"eye(");
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
        btrans.setToolTipText("determining the transpose of a matrix M, e.g. var Mt = T(M) or or var Mt = trans(M) or  var Mt = M~");
        btrans.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"T(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        butriag = new JButton("utriag");
        butriag.setToolTipText("converting a matrix into upper triangular form");
        butriag.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"utriag(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        breshape = new JButton("reshape");
        breshape.setToolTipText("reshaping matrices  (e.g. reshape([1,2;3,4;5,6],2,3) return [1,5,4;3,2,6]) \n"+
                  "The original matrix is read column for column and rearranged  to a new dimension  ");
        breshape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"reshape(");
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
        bdet.setToolTipText("computes the determinant of a square matrix A, d = det(Matrix A)");
        bdet.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"det(");
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
        bsin.setToolTipText("def sin( x: Mat): Mat ; \n Computes the sine of x");
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
        bcos.setToolTipText("def cos( x: Mat): Mat ; \n Computes the cosine of x");
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
        btan.setToolTipText("def tan( x: Mat): Mat; \n Computes the tangent of x");
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
        bsin.setToolTipText("def sinh( x: Mat): Mat ; \n Computes hyperbolic sine of x");
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
        bcosh.setToolTipText("def cosh( x: Mat): Mat; \n Computes the hyperbolic cosine of x");
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
        btanh.setToolTipText("def tanh( x: Mat): Mat; \n Computes the hyperbolic tangent of x");
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
        basin.setToolTipText("def asin( x: Mat): Mat; \n Computes the arc sine of x");
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
        bacos.setToolTipText("def acos( x: Mat): Mat; \n Computes the arc cosine of x");
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
        batan.setToolTipText("def atan( x: Mat): Mat; \n Computes the arc tangent of x");
        batan.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"atan(");
           GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        binplace = new JButton("++, --, ..");
        binplace.setToolTipText("in-place Mat operations for efficiency");
        binplace.addActionListener(new ActionListener() {
       @Override
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("inplace.html");
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
          }
        });

        
        brightAssoc = new JButton("+:, -:, ..");
        brightAssoc.setToolTipText("right associative Mat operators to avoid the implicit conversion");
        brightAssoc.addActionListener(new ActionListener() {
       @Override
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  rightAssocHelpPane = new EditorPaneHTMLHelp("rightAssoc.html");
        rightAssocHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        rightAssocHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        rightAssocHelpPane.setVisible(true);
            }
        });

        browAppend = new JButton("RA");
        browAppend.setToolTipText("append rows to the Matrix");

        browPrepend = new JButton("RP");
        browPrepend.setToolTipText("prepend rows to the Matrix");

        bcolAppend = new JButton("CA");
        bcolAppend.setToolTipText("append columns to the Matrix");

        bcolPrepend = new JButton("CP");
        bcolPrepend.setToolTipText("prepend columns to the Matrix");

        
        matrixPanel.add(bdotProduct);
        matrixPanel2.add(bones);  matrixPanel2.add(bzeros);  matrixPanel2.add(brand);
        matrixPanel2.add(beye);  matrixPanel2.add(btrans);  matrixPanel2.add(binv); matrixPanel2.add(btrans);
        matrixPanel2.add(breshape); 
        matrixPanel2.add(bsum); 
        matrixPanel2.add(bqr);    
        
        
        matrixPanel3.add(bsin); matrixPanel3.add(bcos); matrixPanel3.add(btan);  matrixPanel3.add(basin); matrixPanel3.add(bacos); matrixPanel3.add(batan);
        matrixPanel3.add(bsinh); matrixPanel3.add(bcosh); matrixPanel3.add(btanh);

        matrixPanel4.add(binplace);   matrixPanel4.add(brightAssoc);
        matrixPanel4.add(browAppend); matrixPanel4.add(browPrepend);
        matrixPanel4.add(bcolAppend);  matrixPanel4.add(bcolPrepend);

        matrixPanel6.add(bdet); matrixPanel6.add(blu); matrixPanel6.add(bsvd);
        
        add(matrixPanel);       add(matrixPanel2);    add(matrixPanel3); add(matrixPanel4); add(matrixPanel5); add(matrixPanel6);
        
   }
}
