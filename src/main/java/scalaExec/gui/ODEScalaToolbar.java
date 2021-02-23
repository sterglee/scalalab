
package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import scalaExec.scalaLab.EditorPaneHTMLHelp;


public class ODEScalaToolbar  extends JPanel {
       JButton brk1, brke, brk4a;
       JButton brk4na, brk5na, bmultistep, bdiffsys, bark, befrk, befsirk;
       JButton beferk, bliniger1vs, blininger2vs;
       JButton bgms, bimpex, bmodifiedTaylor, beft, brk2, brk2n, brk3, brk3n;
       JButton barkmat;
       
        
    public ODEScalaToolbar() {
        JPanel ode1Panel = new JPanel();
        JPanel ode2Panel = new JPanel();
        JPanel ode3Panel = new JPanel();
        JPanel ode4Panel = new JPanel();

           
        brk1 = new JButton("rk1");
        brk1.setToolTipText("Solves an initial value problem for a single first order ODE by means of 5-th order Runge-Kutta method");
        brk1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("rk1.html");
if (GlobalValues.useSystemBrowserForHelp==false) {
        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
        inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
        inPlaceHelpPane.setVisible(true);
}
            }
        });


        brke = new JButton("rke");
        brke.setToolTipText("Solves an initial value problem for a system of first order ODE by means of 5-th order Runge-Kutta method");
        brke.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditorPaneHTMLHelp  rkeHelpPane = new EditorPaneHTMLHelp("rke.html");
            if (GlobalValues.useSystemBrowserForHelp==false) {
                rkeHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 rkeHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 rkeHelpPane.setVisible(true);
            }
            }
        });

        brk4a = new JButton("rk4a");
        brk4a.setToolTipText("Solves an initial value problem for a system of first order ODE by means of 5-th order Runge-Kutta method");
        brk4a.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditorPaneHTMLHelp  rkeHelpPane = new EditorPaneHTMLHelp("rk4a.html");
              if (GlobalValues.useSystemBrowserForHelp==false) {
                 rkeHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 rkeHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 rkeHelpPane.setVisible(true);
              }
            }
        });

        
        brk4na = new JButton("brk4na");
        brk4na.setToolTipText("");
        brk4na.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 EditorPaneHTMLHelp  rkeHelpPane = new EditorPaneHTMLHelp("rk4na.html");
                if (GlobalValues.useSystemBrowserForHelp==false) {
                 rkeHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 rkeHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 rkeHelpPane.setVisible(true);
                }
            }
        });

        brk5na = new JButton("brk5na");
        brk5na.setToolTipText("");
        brk5na.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                EditorPaneHTMLHelp  rkeHelpPane = new EditorPaneHTMLHelp("rk5na.html");
            if (GlobalValues.useSystemBrowserForHelp==false) {
                 rkeHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 rkeHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 rkeHelpPane.setVisible(true);
            }
            }
        });


        bmultistep = new JButton("multistep");
        bmultistep.setToolTipText("Based on two linear multistep methods. For stiff problems, it uses the backward differentiation method, for nonstiff the Adams-Bashforth-Moulon method");
        bmultistep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditorPaneHTMLHelp  multistepHelpPane = new EditorPaneHTMLHelp("multistep.html");
              if (GlobalValues.useSystemBrowserForHelp==false) {
                 multistepHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 multistepHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 multistepHelpPane.setVisible(true);
              }
            }
        });

        
        bdiffsys = new JButton("diffsys");
        bdiffsys.setToolTipText("Based on a high order extrapolation method build on the modified midpoint rule");
        bdiffsys.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  diffsysHelpPane = new EditorPaneHTMLHelp("diffsys.html");
                if (GlobalValues.useSystemBrowserForHelp==false) {
                 diffsysHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 diffsysHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 diffsysHelpPane.setVisible(true);
                }
            }
        });
        
        
        
        bark = new JButton("ark");
        bark.setToolTipText("Semi-discretization of an initial boundary value problem");
        bark.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  arkHelpPane = new EditorPaneHTMLHelp("ark.html");
              if (GlobalValues.useSystemBrowserForHelp==false) {
                 arkHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 arkHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 arkHelpPane.setVisible(true);
              }
            }
        });
        
        befrk = new JButton("efrk");
        befrk.setToolTipText("exponentially fitted explicit Runge-Kutta");
        befrk.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  efrkHelpPane = new EditorPaneHTMLHelp("efrk.html");
                if (GlobalValues.useSystemBrowserForHelp==false) {
                  efrkHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                  efrkHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                  efrkHelpPane.setVisible(true);
                }
            }
        });
        
        befsirk = new JButton("efsirk");
        befsirk.setToolTipText("exponentially fitted, A-stable, semi-implicit Runge-Kutta");
        befsirk.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  befsirkHelpPane = new EditorPaneHTMLHelp("efsirk.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                   befsirkHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                   befsirkHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                   befsirkHelpPane.setVisible(true);
                  }
            }
        });
        
        beferk= new JButton("efsirk");
        befsirk.setToolTipText("exponentially fitted, semi-implicit Runge-Kutta of third order");
        befsirk.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  beferkHelpPane = new EditorPaneHTMLHelp("eferk.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                   beferkHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                   beferkHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                   beferkHelpPane.setVisible(true);
                  }
             }
        });
        
        bliniger1vs = new JButton("lininger1vs");
        bliniger1vs.setToolTipText("implicit, first-order accurate, exponentially fitted one-step method");
        bliniger1vs.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("lininger1vs.html");
             if (GlobalValues.useSystemBrowserForHelp==false) {
                  inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                  inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                  inPane.setVisible(true);
             }
            }
        });
        
        blininger2vs  = new JButton("lininger2vs");
        blininger2vs.setToolTipText("implicit, second (or possibly third)  order accurate, exponentially fitted one-step method");
        blininger2vs.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("lininger2vs.html");
               if (GlobalValues.useSystemBrowserForHelp==false) {
                  inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                  inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                  inPane.setVisible(true);
               }
            }
        });
        
             
        bgms  = new JButton("gms");
        bgms.setToolTipText("third order, exponentially fitted, generalized multistep method with automatic stepsize control");
        bgms.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("gms.html");
                 if (GlobalValues.useSystemBrowserForHelp==false) {
                   inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                   inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                   inPane.setVisible(true);
                 }
            }
        });
        
        bimpex = new JButton("impex");
        bimpex.setToolTipText("implicit midpoint rule with smoothing and extrapolation");
        bimpex.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("impex.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                    inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                    inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                    inPane.setVisible(true);
                  }
                }
        });
        
        bmodifiedTaylor = new JButton("modidiedtaylor");
        bmodifiedTaylor.setToolTipText("large systems arising from partial differential equations");
        bmodifiedTaylor.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("modifiedTaylor.html");
               if (GlobalValues.useSystemBrowserForHelp==false) {
                  inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                  inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                  inPane.setVisible(true);
               }
           }
        });
        
        beft  = new JButton("eft");
        beft.setToolTipText("third order, first order consistent, exponentially fitted Taylor series method");
        beft.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("eft.html");
                 if (GlobalValues.useSystemBrowserForHelp==false) {
                  inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                  inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                  inPane.setVisible(true);
                 }
           }
        });
        
             
        brk2  = new JButton("rk2");
        brk2.setToolTipText("single second order ordinary differential equation");
        brk2.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("rk2.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                     inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                     inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                     inPane.setVisible(true);
                  }
           }
        });
        
                 
        brk2n  = new JButton("rk2n");
        brk2n.setToolTipText("system of second order ordinary differential equations");
        brk2n.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("rk2n.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                   inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                   inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                   inPane.setVisible(true);
                  }
           }
        });
        
                      
        brk3  = new JButton("rk3");
        brk3.setToolTipText("single second order ordinary differential equation without first derivative");
        brk3.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("rk3.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                    inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                    inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                    inPane.setVisible(true);
                  }
                  }
        });
        
                         
        brk3n  = new JButton("rk3n");
        brk3n.setToolTipText("system of second order ordinary differential equations without first derivative");
        brk3n.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("rk3n.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                     inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                     inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                     inPane.setVisible(true);
                  }
                  }
        });
        
       barkmat = new JButton("arkmat");
       barkmat.setToolTipText("system of first order (nonlinear) differential equations");
       barkmat.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                  EditorPaneHTMLHelp  inPane = new EditorPaneHTMLHelp("arkmat.html");
                  if (GlobalValues.useSystemBrowserForHelp==false) {
                    inPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                    inPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                    inPane.setVisible(true);
                  }
           }
        });
        
        ode1Panel.add(brk1);  ode1Panel.add(brke); ode1Panel.add(brk4a);  ode1Panel.add(brk4na);  ode1Panel.add(brk5na);
        ode2Panel.add(bmultistep); ode2Panel.add(bdiffsys); ode2Panel.add(bark); ode2Panel.add(befrk); ode2Panel.add(befsirk); 
        ode2Panel.add(bliniger1vs);  ode2Panel.add(blininger2vs);
        ode3Panel.add(bgms); ode3Panel.add(bimpex);  ode3Panel.add(bmodifiedTaylor); ode3Panel.add(beft); ode3Panel.add(brk2);
        ode3Panel.add(brk2n); ode3Panel.add(brk3); ode3Panel.add(brk3n); ode3Panel.add(barkmat);
        
        setLayout(new GridLayout(4,1));
        add(ode1Panel); add(ode2Panel); add(ode3Panel); add(ode4Panel);
                
   }
}

