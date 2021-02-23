
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class LAScalaToolbar  extends JPanel {
      JButton bdecsol, bgsssol, bgsssolerb, bdecinv, bgssinv, bgssinverb, bgssitisol, bgssitisolerb;
      JButton bchol1, bchol2, bcholdec1, bcholdec2, bdetermSym2, bdecsolsym2;
      JButton blsqsol, blsqortdecsol, blsqinv, blsqdecomp, bsolovr, bsolund, bhomsol;
      JButton bpsdinv, bdetermbnd, bdecsolbnd, bdecsoltri, bsoltripiv, bdecsoltripiv;
      JButton bchlsolbnd, bdecsolsymtri;
      JButton bconjgrad, beqilbrcom, bhshhrmtri, bvalsymtri, beigsym1, bsymeigimp, bcomvalqri, breaeig3;
      JButton beighrm, bqrihrm, bvalqricom, bqricom, beigcom, bqzival, bqzi, bqrisngvaldec, bzerpol;
      
      JPanel la1Panel = new JPanel();
      JPanel la2Panel = new JPanel();
      JPanel la3Panel = new JPanel();
      JPanel la4Panel = new JPanel();
      JPanel la5Panel = new JPanel();
 
        
    public LAScalaToolbar() {
           
        bdecsol = new JButton("decsol");
        bdecsol.setToolTipText("Solves a well-conditioned linear system of equations Ax=b");
        bdecsol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsol.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
       
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
         }
        }
        });
           
        bgsssol = new JButton("gsssol");
        bgsssol.setToolTipText("Solves a linear system, with Gaussian elimination");
        bgsssol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gsssol.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
               }
             }
        });
        
        bgsssolerb = new JButton("gsssolerb");
        bgsssolerb.setToolTipText("Solves a linear system, with Gaussian elimination, providing upper bounds for the relative error");
        bgsssolerb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gsssolerb.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
            }  
           }
        });
        
        bdecinv = new JButton("decinv");
        bdecinv.setToolTipText("Obtains the inverse of the nxn matrix");
        bdecinv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decinv.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
 }
}
        });
        
        bgssinv = new JButton("gssinv");
        bgssinv.setToolTipText("Obtains the inverse of the nxn matrix with Gaussian elimination");
        bgssinv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gssinv.html");
 if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
 }     
  }
        });
        
        bgssinverb = new JButton("gssinverb");
        bgssinverb.setToolTipText("Obtains the inverse of the nxn matrix with Gaussian elimination, and provides upper error bounds");
        bgssinverb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gssinverb.html");
   if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
   }
   }
        });
        
        bgssitisol = new JButton("gssitisol");
        bgssitisol.setToolTipText("Calculates iteratively refined solution of the given linear system");
        bgssitisol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
      EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gssitisol.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
     }
         }
        });
        
        bgssitisolerb = new JButton("gssitisolerb");
        bgssitisolerb.setToolTipText("Calculates iteratively refined solution of the given linear system, providing error bounds");
        bgssitisolerb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gssitisolerb.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
       }
            }
        });
        
        bchol1 = new JButton("Cholesky1");
        bchol1.setToolTipText("Cholesky factorization");
        bchol1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("chol1.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}       
}
        });
        
        
        bchol2 = new JButton("Cholesky2");
        bchol2.setToolTipText("Cholesky factorization");
        bchol2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("chol2.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
  }
        });
        
        bcholdec1 = new JButton("CholDec1");
        bcholdec1.setToolTipText("Cholesky decomposition");
        bcholdec1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("choldec1.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
            }
        });
        
        
        bcholdec2 = new JButton("CholDec2");
        bcholdec2.setToolTipText("Cholesky factorization");
        bcholdec2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("choldec2.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}       
}
        });
        
        bdetermSym2 = new JButton("DetermSym");
        bdetermSym2.setToolTipText("Determinant of Symmetric Matrix");
        bdetermSym2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("determsym2.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
       
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }      
  }
        });
        
        bdecsolsym2  = new JButton("Decsolsym2");
        bdecsolsym2.setToolTipText("Solution of Symmetric System of linear equations");
        bdecsolsym2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsolsym2.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }   
    }
        });
        
        blsqsol  = new JButton("lsqsol");
        blsqsol.setToolTipText("Least squares solution");
        blsqsol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("lsqsol.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
      }
        });
        
        blsqortdecsol  = new JButton("lsqortdecsol");
        blsqortdecsol.setToolTipText("Least squares solution, principal diagonal elements pf the inverse of A^T A");
        blsqortdecsol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("lsqortdecsol.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
        }
        }
        });
        
        blsqinv  = new JButton("lsqinv");
        blsqinv.setToolTipText("Calculates the inverse with least squares,  of the matrix A^T A");
        blsqinv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("lsqinv.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
        }
        }
        });
        
              
        blsqdecomp  = new JButton("lsqdecomp");
        blsqdecomp.setToolTipText("Least Squares with linear constraints");
        blsqdecomp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("lsqdecomp.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
         }
        });
        
        bsolovr  = new JButton("solover");
        bsolovr.setToolTipText("Solves Overdetermined System with SVD");
        bsolovr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("solovr.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
  }
        });
        
        bsolund  = new JButton("solundr");
        bsolund.setToolTipText("Solves Underdetermined System with SVD");
        bsolund.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("solund.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }   
     }
        });
        
        bhomsol  = new JButton("homsol");
        bhomsol.setToolTipText("Solution of homogenous equation");
        bhomsol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("homsol.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
      }
        });
        
        bpsdinv  = new JButton("psdinv");
        bpsdinv.setToolTipText("Pseudo-inverse");
        bpsdinv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("psdinv.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}       
}
        });
        
        bdetermbnd  = new JButton("determbnd");
        bdetermbnd.setToolTipText("Determinant of upper triangular matrix");
        bdetermbnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("determbnd.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
             inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
  }
        });
        
        bdecsolbnd  = new JButton("decsolbnd");
        bdecsolbnd.setToolTipText("Solution of system of linear equations, coefficient matrix is in band form");
        bdecsolbnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsolbnd.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
           inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
    }
        });
        
        
        bdecsoltri  = new JButton("decsoltri");
        bdecsoltri.setToolTipText("tri-diagonal LU decomposition");
        bdecsoltri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsoltri.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
            }
        });
        
        bsoltripiv  = new JButton("soltripiv");
        bsoltripiv.setToolTipText("solution of systems with tri-diagonal LU decomposition");
        bsoltripiv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("soltripiv.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
               inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
}
        });
        
        bdecsoltripiv  = new JButton("decsoltripiv");
        bdecsoltripiv.setToolTipText("tri-diagonal LU decomposition with memory savings");
        bdecsoltripiv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsoltripiv.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }     
  }
        });
        
        bchlsolbnd  = new JButton("chlsolbnd");
        bchlsolbnd.setToolTipText("Solution of positive definite band matrix systems");
        bchlsolbnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("chlsolbnd.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
            }
        });
        
        
        bdecsolsymtri  = new JButton("decsolsymtri");
        bdecsolsymtri.setToolTipText("Decomposition of symmetric tridiagonal matrix");
        bdecsolsymtri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("decsolsymtri.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
        
        bconjgrad  = new JButton("conjgrad");
        bconjgrad.setToolTipText("Solution with conjugate gradient iterative method");
        bconjgrad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("conjgrad.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}       
}
        });
        
        beqilbrcom  = new JButton("eqilbrcom");
        beqilbrcom.setToolTipText("Equilibration - complex matrices");
        beqilbrcom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("eqilbrcom.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
            }
        });
        
        bhshhrmtri  = new JButton("hshhrmtri");
        bhshhrmtri.setToolTipText("Hessenberg form-complex Hermitian");
        bhshhrmtri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("hshhrmtri.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
         
        bvalsymtri  = new JButton("valsymtri");
        bvalsymtri.setToolTipText("Eigenvalues - real symmetric tridiagonal matrices");
        bvalsymtri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("valsymtri.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}       
}
        });
        
        beigsym1  = new JButton("eigsym1");
        beigsym1.setToolTipText("Eigenvalues - real symmetric tridiagonal matrices");
        beigsym1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("eigsym1.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
  }
        });
        
        
        bsymeigimp  = new JButton("symeigimp");
        bsymeigimp.setToolTipText("Symmetric matrices - iterative improvement");
        bsymeigimp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("symeigimp.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
           inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
            }
        });
        
        // stergSOS
        bcomvalqri  = new JButton("comvalqri");
        bcomvalqri.setToolTipText("Real and complex eigenvalues by QR iteration of Francis");
        bcomvalqri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("comvalqri.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
         }
        });
        
        
            
        breaeig3  = new JButton("reaeig3");
        breaeig3.setToolTipText("Eigenvalues with QR iteration");
        breaeig3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("reaeig3.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
       inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
            }
        });
        
        beighrm  = new JButton("eighrm");
        beighrm.setToolTipText("Eigenvalues of Hermitian matrix");
        beighrm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("eighrm.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
        bqrihrm  = new JButton("qrihrm");
        bqrihrm.setToolTipText("Eigenvalues and eigenvectors of Hermitian matrix");
        bqrihrm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qrihrm.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
 } 
 }
        });
        
        bvalqricom  = new JButton("valqricom");
        bvalqricom.setToolTipText("Complex upper Hessenberg matrices");
        bvalqricom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("valqricom.html");
 if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
 }
            }
        });
        
        
        bqricom  = new JButton("qricom");
        bqricom.setToolTipText("Complex upper Hessenberg matrices");
        bqricom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qricom.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
         
        beigcom  = new JButton("beigcom");
        beigcom.setToolTipText("Eigenvalues/Eigenvectors computation");
        beigcom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("eigcom.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
        bqzival  = new JButton("bqzival");
        bqzival.setToolTipText("Generalized eigenvalue problem");
        bqzival.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qzival.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
        bqzi  = new JButton("bqzi");
        bqzi.setToolTipText("Generalized eigenvalue problem");
        bqzi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qzi.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
        
        bqrisngvaldec  = new JButton("qrisngvaldec");
        bqrisngvaldec.setToolTipText("Singular Valued Decomposition");
        bqrisngvaldec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qrisngvaldec.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });
                
        la1Panel.add(bdecsol);  la1Panel.add(bgsssol);    la1Panel.add(bgsssolerb);  la1Panel.add(bdecinv); la1Panel.add(bgssinv);  
        la1Panel.add(bgssinverb); la1Panel.add(bgssitisol); la1Panel.add(bgssitisolerb);  
        la1Panel.add(bchol1);  la1Panel.add(bchol2); la1Panel.add(bcholdec1); la1Panel.add(bcholdec2);
        
        la2Panel.add(bdetermSym2); la2Panel.add(bdecsolsym2); 
        la2Panel.add(blsqsol);  la2Panel.add(blsqortdecsol); la2Panel.add(blsqinv); la2Panel.add(blsqdecomp); la2Panel.add(bsolovr); la2Panel.add(bsolund);
        la2Panel.add(bhomsol);       la2Panel.add(bpsdinv);
        
        la3Panel.add(bdetermbnd); la3Panel.add(bdecsolbnd); la3Panel.add(bsoltripiv); la3Panel.add(bdecsoltripiv); la3Panel.add(bchlsolbnd);
        la3Panel.add(bdecsolsymtri);
        
        la4Panel.add(bconjgrad);  la4Panel.add(beqilbrcom); la4Panel.add(bhshhrmtri); la4Panel.add(bvalsymtri);  la4Panel.add(beigsym1);
        la4Panel.add(bsymeigimp); la4Panel.add(bcomvalqri); la4Panel.add(breaeig3); 
        
        la5Panel.add(beighrm); la5Panel.add(bqrihrm); la5Panel.add(bvalqricom); la5Panel.add(bqricom); la5Panel.add(beigcom);
        la5Panel.add(bqzival); la5Panel.add(bqzi); la5Panel.add(bqrisngvaldec);
        
        setLayout(new GridLayout(5,1));
        add(la1Panel);  add(la2Panel);  add(la3Panel);   add(la4Panel); add(la5Panel);
   }
}

