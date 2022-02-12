
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class WaveletScalaToolbar  extends JPanel {
        JButton bHaar, bfwt, bMP;
        
        
    public WaveletScalaToolbar() {
        JPanel waveletPanel = new JPanel();
        setLayout(new BorderLayout());

        
        JButton bhelp = new JButton("Help");
        bhelp.setToolTipText("HTML Help on Wavelet operations");
        bhelp.addActionListener(new ActionListener() {
  @Override
            public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("Wavelet.html");
 if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
 }
  }
        });
        waveletPanel.add(bhelp);
        
        bHaar = new JButton("Haar");
        bHaar.setToolTipText("Haar Wavelet");
        bHaar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"Haar()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bfwt = new JButton("FWT");
        bfwt.setToolTipText("Forward Wavelet Transform");
        bfwt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"fwt()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bMP = new JButton("MP");
        bMP.setToolTipText("Matching Pursuit");
        bMP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"MP()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        
        waveletPanel.add(bHaar); waveletPanel.add(bfwt);
        waveletPanel.add(bMP);  
        
        add(waveletPanel);
     
   }
}

