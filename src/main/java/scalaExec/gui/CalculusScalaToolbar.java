
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class CalculusScalaToolbar  extends JPanel {
        JButton bqadrat, bintegral, btricub, breccof, bjacobnnf, bjacobnmf, bjacobnbndf;
        
    public CalculusScalaToolbar() {
        JPanel basicCalculusPanel = new JPanel();
        
        bqadrat = new JButton("qadrat");
        bqadrat.setToolTipText("Run the Java example within the ScalaLab Editor with F5");
        bqadrat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qadrat.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
             }
            }
        });
        
        bintegral = new JButton("integral");
        bintegral.setToolTipText("Run the Java example within the ScalaLab Editor with F5");
        bintegral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("qadrat.html");
                if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
                }
            }
        });
        
        btricub = new JButton("tricub");
        btricub.setToolTipText("Run the Java example within the ScalaLab Editor with F5");
        btricub.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("tricub.html");
                if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
                }
    
            }
        });
            
        breccof = new JButton("reccof");
        breccof.setToolTipText("Run the Java example within the ScalaLab Editor with F5");
        breccof.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("reccof.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
                 }    
            }
        });
        
        
        bjacobnnf = new JButton("jacobnnf");
        bjacobnnf.setToolTipText("Jacobian matrix of an n-dimensional function of n variables using forward differences");
        bjacobnnf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("jacobnnf.html");
         if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
    } 
            }
        });

        bjacobnmf = new JButton("jacobnmf");
        bjacobnmf.setToolTipText("Jacobian matrix of an n-dimensional function of m variables using forward differences");
        bjacobnmf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
      EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("jacobnmf.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
                  } 
            }
        });

        bjacobnbndf = new JButton("jacobnbndf");
        bjacobnbndf.setToolTipText("Jacobian matrix of an n-dimensional function of n variables, if this Jacobian is known to be a band matrix and have to be stored rowwise in a one-dimensional array" );
        bjacobnbndf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("jacobnbndf.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
             }
            }
        });
        
        basicCalculusPanel.add(bqadrat);   basicCalculusPanel.add(bintegral);
        basicCalculusPanel.add(btricub);  basicCalculusPanel.add(breccof);
        basicCalculusPanel.add(bjacobnnf); basicCalculusPanel.add(bjacobnmf);
        basicCalculusPanel.add(bjacobnbndf);
                
        add(basicCalculusPanel);
   }
}

