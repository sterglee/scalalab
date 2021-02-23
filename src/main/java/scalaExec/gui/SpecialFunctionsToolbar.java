
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class SpecialFunctionsToolbar  extends JPanel {
      JButton bei, beialpha, benx;
      JButton bsincosint, brecipgamma, bgamma, bloggamma, bincomgam, bincombeta;
      JPanel sf1Panel = new JPanel();
      JPanel sf2Panel = new JPanel();
      JPanel sf3Panel = new JPanel();
 
        
    public SpecialFunctionsToolbar() {
           
        bei = new JButton("ei");
        bei.setToolTipText("Calculates the exponential integral");
        bei.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("ei.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
        }
            }
        });
           
                
        beialpha = new JButton("eialpha");
        beialpha.setToolTipText("Calculates a sequence of integrals");
        beialpha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("eialpha.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
           
        benx  = new JButton("enx, nonexpenx");
        benx.setToolTipText("Calculates a sequence of integrals");
        benx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("enx.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
            }
        });
           
        
        bsincosint  = new JButton("sincosint, sincosfg");
        bsincosint.setToolTipText("Calculates sine and cosine integrals");
        bsincosint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("sincosint.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
           
        brecipgamma  = new JButton("recipgamma");
        brecipgamma.setToolTipText("Gamma function");
        brecipgamma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("recipgamma.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
              
        bgamma  = new JButton("gamma");
        bgamma.setToolTipText("Gamma function");
        bgamma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("gamma.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
                  
        bloggamma  = new JButton("loggamma");
        bloggamma.setToolTipText("LogGamma function");
        bloggamma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("loggamma.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bincomgam  = new JButton("incomgam");
        bincomgam.setToolTipText("incomplete gamma functions");
        bincomgam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("incomgam.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        bincombeta  = new JButton("incombeta");
        bincombeta.setToolTipText("incomplete beta functions");
        bincombeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                               
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("incombeta.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
  inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
  inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
  inPlaceHelpPane.setVisible(true);
        }
            }
        });
        
        sf1Panel.add(bei);  sf1Panel.add( beialpha);     sf1Panel.add(benx);
        sf2Panel.add(bsincosint); sf2Panel.add(brecipgamma); sf2Panel.add(bgamma); sf2Panel.add(bloggamma);
        sf3Panel.add(bincomgam); sf3Panel.add(bincombeta);
        setLayout(new GridLayout(3,1));
        add(sf1Panel);  add(sf2Panel);  add(sf3Panel);         
   }
}

