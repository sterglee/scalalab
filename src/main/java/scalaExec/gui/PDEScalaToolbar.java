
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class PDEScalaToolbar  extends JPanel {
      JButton bfemlagsym, bfemlag, bfemlagspher, bfemlagskew, bfemhermsym;
      JButton bnonlinfemlagskew, brichardson, belimination;
      JButton bpeide;
      JPanel pde1Panel = new JPanel();
      JPanel pde2Panel = new JPanel();
      JPanel pde3Panel = new JPanel();
 
        
    public PDEScalaToolbar() {
           
        bfemlagsym = new JButton("femlagsym");
        bfemlagsym.setToolTipText("Solves a second order self-adjoint linear two point boundary value problem by means of Galerkin's method with continuous piecewise polynomials");
        bfemlagsym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("femlagsym.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
            }
        });
           
        
        bfemlag = new JButton("femlag");
        bfemlag.setToolTipText("Solves a second order self-adjoint linear two point boundary value problem by means of Galerkin's method with continuous piecewise polynomials");
        bfemlag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("femlag.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bfemlagspher = new JButton("femlagspher");
        bfemlagspher.setToolTipText("Solves a second order self-adjoint linear two point boundary value problem with spherical coordinates by means of Galerkin's method with continuous piecewise polynomials");
        bfemlagspher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("femlagspher.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bfemlagskew = new JButton("femlagskew");
        bfemlagskew.setToolTipText("Solves a second order skew-adjoint linear two point boundary value problem with spherical coordinates by means of Galerkin's method with continuous piecewise polynomials");
        bfemlagskew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("femlagskew.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bfemhermsym  = new JButton("femhermsym");
        bfemhermsym.setToolTipText("Solves a fourth order self-adjoint linear two point boundary value problem by means of Galerkin's method with continuous piecewise polynomials");
        bfemhermsym.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("femhermsym.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        } 
        }
        });
        
        bnonlinfemlagskew  = new JButton("nonlinfemlagskew");
        bnonlinfemlagskew.setToolTipText("Solves a nonlinear two point boundary value problem with spherical coordinates");
        bnonlinfemlagskew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("nonlinfemlagskew.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        brichardson  = new JButton("richardson");
        brichardson.setToolTipText("Solves a system of linear equations with a coefficient matrix having positive real eigenvalues ");
        brichardson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("richardson.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        belimination  = new JButton("elimination");
        belimination.setToolTipText("Determines an approximation u'_n to the solution of the system of equations Au=f");
        belimination.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("elimination.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bpeide   = new JButton("peide");
        bpeide.setToolTipText("Estimates unknown variables in a system of first order differential equations by using a given set of observed values");
        bpeide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("peide.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        pde1Panel.add(bfemlagsym);  pde1Panel.add(bfemlag);  pde1Panel.add(bfemlagspher); 
        pde2Panel.add(bfemlagskew);       pde2Panel.add(bfemhermsym);        pde2Panel.add(bnonlinfemlagskew); 
        pde3Panel.add(brichardson);         pde3Panel.add(belimination);         pde3Panel.add(bpeide);         
        setLayout(new GridLayout(3,1));
        add(pde1Panel);  add(pde2Panel);  add(pde3Panel);         
   }
}

