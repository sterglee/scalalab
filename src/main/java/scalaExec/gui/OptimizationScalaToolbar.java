
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.MathDialogs.MinInDerDialog;
import scalaExec.gui.MathDialogs.MinInDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class OptimizationScalaToolbar  extends JPanel {
        JButton bzeroin, bzeroinrat, bzeroinder, bminin, bmininder;
        JButton bquanewbnd1;
        JButton bpraxis, brnk1min;
        JButton bmarquardt, bgssnewton;
        
        
    public OptimizationScalaToolbar() {
        JPanel allOptimizationPanel = new JPanel();
        JPanel zeroFindingPanel = new JPanel();
        JPanel minPanel = new JPanel();
        JPanel overdeterminedPanel = new JPanel();
        
        setLayout(new BorderLayout());

        bzeroin = new JButton("zeroin");
        bzeroin.setToolTipText("The zero of the function f(x), in an interval [x0, y0]");
        bzeroin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("zeroin.html");
     if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
     }   
            }
        });
        
          
        bzeroinrat = new JButton("zeroinrat");
        bzeroinrat.setToolTipText("The zero of the function f(x), in an interval [x0, y0]");
        bzeroinrat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("zeroinrat.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bzeroinder = new JButton("zeroinder");
        bzeroinder.setToolTipText("The zero of the function f(x), in an interval [x0, y0]");
        bzeroinder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("zeroinder.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bquanewbnd1  = new JButton("quanewbnd1");
        bquanewbnd1.setToolTipText("Solvws systems of nonlinear equations of which the Jacobian is known to be a band matrix");
        bquanewbnd1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("quanewbnd1.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bminin= new JButton("minin");
        bminin.setToolTipText("Determines a point x in [a, b] at which a real valued function f(x) assumes a minimum value");
        bminin.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
        
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("minin.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
           MinInDialog   minInDialog = new MinInDialog();
           minInDialog.setLocation(GlobalValues.scalalabMainFrame.getLocation());
           minInDialog.pack();
           minInDialog .setVisible(true);
                  
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bmininder = new JButton("mininder");
        bmininder.setToolTipText("Determines a point x in [a, b] at which a real valued function f(x) assumes a minimum value");
        bmininder.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                  
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("mininder.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
    
         MinInDerDialog   minInDerDialog = new MinInDerDialog();
         minInDerDialog.setLocation(GlobalValues.scalalabMainFrame.getLocation());
         minInDerDialog.pack();
         minInDerDialog .setVisible(true);
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bpraxis= new JButton("praxis");
        bpraxis.setToolTipText("Determines a point x in [a, b] at which a real valued function f(x) assumes a minimum value");
        bpraxis.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                        
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("praxis.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
    
        brnk1min = new JButton("rnk1min");
        brnk1min.setToolTipText("Determines a vector x \\in R^n for which the real values function F(x) attains a minimum");
        brnk1min.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                        
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("rnk1min.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
            }
        });
    
        bmarquardt = new JButton("marquardt");
        bmarquardt.setToolTipText("Calculates the least squares solution of an overdetermined system of nonlinear equations with Marquardt's method");
        bmarquardt.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                             
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("marquardt.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bgssnewton = new JButton("gsnewton");
        bgssnewton.setToolTipText("Calculates the least squares solution of an overdetermined system of nonlinear equations with the Gauss-Newton method");
        bgssnewton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gsnewton.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        zeroFindingPanel.add(bzeroin);   zeroFindingPanel.add(bzeroinrat);  zeroFindingPanel.add(bzeroinder); zeroFindingPanel.add(bquanewbnd1);
        
        minPanel.add(bminin); minPanel.add(bmininder); minPanel.add(bpraxis);
        overdeterminedPanel.add(bmarquardt);  overdeterminedPanel.add(bgssnewton);
        
        allOptimizationPanel.add(zeroFindingPanel); allOptimizationPanel.add(minPanel);  allOptimizationPanel.add(overdeterminedPanel);
        add(allOptimizationPanel);
   }
}

