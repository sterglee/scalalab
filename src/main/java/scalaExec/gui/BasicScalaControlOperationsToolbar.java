
package scalaExec.gui;

import scalaExec.gui.MathDialogs.ExpressionDialogPlot3D_Grid;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Line;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.MathDialogs.ExpressionDialogPlot2D_Histo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class BasicScalaControlOperationsToolbar  extends JPanel {
        JButton bwhos, bdir, bcd, bmd, bcls, bclearAll, bcloseAll, bDecformat, bexec;
        JButton bmatLoadSave;
         
    public BasicScalaControlOperationsToolbar() {
        JPanel controlPanel = new JPanel();
        JPanel matlabPanel = new JPanel();
        
        setLayout(new GridLayout(2,1));

        bexec = new JButton("exec(\"scriptName\")");
        bexec.setToolTipText("Executes the contents of the script file named scriptName");
       
        bclearAll = new JButton("clear()");
        bclearAll.setToolTipText("Clears the contents of the variable workspace");
        bclearAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"clear()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });


        bcloseAll = new JButton("close(\"all\")");
        bcloseAll.setToolTipText("Closes all the open figures");
        bcloseAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"close(\"all\")");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        
        bcls = new JButton("cls");
        bcls.setToolTipText("Clears the screen contents");
        bcls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"cls()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });


        bcd = new JButton("cd");
        bcd.setToolTipText("Changes working directory, e.g. cd(\"/export/home/user/Java\")");
        bcd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"cd(");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bmd = new JButton("md");
        bmd.setToolTipText("Creates a new directory, e.d. md(\"myProgs\")");
        bmd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"md(");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bwhos = new JButton("whos");
        bwhos.setToolTipText("Displays the variables of the workspace");
        bwhos.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"whos()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
    }
        });


        bdir = new JButton("dir/ls");
        bdir.setToolTipText("Lists the current directory: dir(), or lists the specified directory: dir(<nameOfDirectory>)");
        bdir.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"dir()");
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });

        bDecformat = new JButton("format()");
        bDecformat.setToolTipText("controls how many decimal points to display for doubles: format(decPoints),  sets to decPoints, returns previous setting");
        bDecformat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        GlobalValues.scalalabMainFrame.scalalabConsole.setText(GlobalValues.scalalabMainFrame.scalalabConsole.getText()+"format(<decPoints>");;
        GlobalValues.scalalabMainFrame.scalalabConsole.setCaretPosition(GlobalValues.scalalabMainFrame.scalalabConsole.getText().length());
           // construct an explicit focus event in order to display the cursor at the input console
        FocusEvent fe = new FocusEvent(GlobalValues.scalalabMainFrame.scalalabConsole, FocusEvent.FOCUS_GAINED);
        GlobalValues.scalalabMainFrame.scalalabConsole.dispatchEvent(fe);
            }
        });
        
        controlPanel.add(bexec); controlPanel.add(bclearAll); controlPanel.add(bcloseAll); 
        controlPanel.add(bwhos); controlPanel.add(bdir); controlPanel.add(bcd); controlPanel.add(bmd); controlPanel.add(bcls);
        controlPanel.add(bDecformat);
        add(controlPanel);       



        bmatLoadSave = new JButton("MatLoadSave .mat");
        bmatLoadSave.setToolTipText("Matlab .mat file interoperabilty routines");
        bmatLoadSave.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("MatLoadSave.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
       }
    }
        });

        matlabPanel.add(bmatLoadSave);   
        
        add(matlabPanel);
   }
}

