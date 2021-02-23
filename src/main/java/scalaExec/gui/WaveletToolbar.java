
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class WaveletToolbar  extends JPanel {
        JButton bHaar, bfwt, bMP;
        
        
    public WaveletToolbar() {
        JPanel waveletPanel = new JPanel();
        setLayout(new BorderLayout());

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

