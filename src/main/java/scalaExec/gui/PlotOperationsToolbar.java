
package scalaExec.gui;

import scalaExec.gui.MathDialogs.ExpressionDialogPlot3D_Grid;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Line;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Histo;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;
import scalaExec.scalaLab.scalaLab;


public class PlotOperationsToolbar  extends JPanel {
         JButton  bplot2d_line, bplot2d_bar, bplot2d_histo, bplot2d_cloud, bplot2d_box, 
                 bplot3d_box, bplot3d_cloud, bplot3d_grid;
         JButton bfunctionalPlots, bnamedPlots;
         
    public PlotOperationsToolbar() {
        JPanel plotPanel = new JPanel();
        
        setLayout(new GridLayout(2,1));

         JButton bhelp = new JButton("Help");
        bhelp.setToolTipText("HTML Help on Plot operations");
        bhelp.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("Plot.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
  }
        });
        plotPanel.add(bhelp);
        
        JButton bhelpJFC = new JButton("JFreeChart Help");
        bhelpJFC.setToolTipText("HTML Help on Plot operations with JFreeChart");
        bhelpJFC.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("JFreeChartPlot.html");
  if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
  }
  }
        });
        plotPanel.add(bhelpJFC);
         
        JButton bhelpVISAD = new JButton("VISAD Help");
        bhelpVISAD.setToolTipText("Plot operations with VISAD (VISualization for Algorithm Development)");
        bhelpVISAD.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("VISAD.html");
    if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
    }
    }
        });
        plotPanel.add(bhelpVISAD);
         
        JButton bfunctionalPlots = new JButton("Functional Plots");
        bfunctionalPlots.setToolTipText("HTML Help on Functional Plot operations");
        bfunctionalPlots.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("FunctionalPlots.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
  }
        });
        plotPanel.add(bfunctionalPlots);
        
        JButton bnamedPlots = new JButton("Named Plots");
        bnamedPlots.setToolTipText("HTML Help on Named Plot operations");
        bnamedPlots.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("namedPlots.html");
      if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
      }
  }
        });
        plotPanel.add(bnamedPlots);
        
        bplot2d_line = new JButton("plot2d_line ...");
        bplot2d_line.setToolTipText("2D plots: Plotting a function y = f(x). Line plot");
        bplot2d_line.setFont(new Font("Arial", Font.BOLD, 14));
        bplot2d_line.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           ExpressionDialogPlot2D_Line  exprDialog = new ExpressionDialogPlot2D_Line();
           exprDialog.setLocation(GlobalValues.scalalabMainFrame.getLocation());
           exprDialog.pack();
           exprDialog.setVisible(true);
                  
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bplot2d_histo = new JButton("plot2d_histo ...");
        bplot2d_histo.setToolTipText("2D plots: Histogram of a function y = f(x). ");
        bplot2d_histo.setFont(new Font("Arial", Font.BOLD, 14));
        bplot2d_histo.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           ExpressionDialogPlot2D_Histo  exprDialog = new ExpressionDialogPlot2D_Histo();
           exprDialog.setLocation(GlobalValues.scalalabMainFrame.getLocation());
           exprDialog.pack();
           exprDialog.setVisible(true);
                  
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        
        bplot3d_grid = new JButton("plot3d_grid ...");
        bplot3d_grid.setToolTipText("3D plots: Plotting a function z = f(x,y). Grid Plot");
        bplot3d_grid.setFont(new Font("Arial", Font.BOLD, 14));
        bplot3d_grid.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           ExpressionDialogPlot3D_Grid  exprDialog = new ExpressionDialogPlot3D_Grid("3-D Grid Plor");
           exprDialog.setLocation(GlobalValues.scalalabMainFrame.getLocation());
           exprDialog.pack();
           exprDialog.setVisible(true);
                  
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);    
            }
        });
        plotPanel.add(bplot2d_line); plotPanel.add(bplot2d_histo);  plotPanel.add(bplot3d_grid);
                
        add(plotPanel);
        
   }
}

