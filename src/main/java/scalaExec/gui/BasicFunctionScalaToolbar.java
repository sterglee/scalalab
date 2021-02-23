
package scalaExec.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class BasicFunctionScalaToolbar  extends JPanel {
        JButton bsqrt, blog, bexp,
                babs, bfloor, bceil, bln;

        
    public BasicFunctionScalaToolbar() {
        JFrame frame = new JFrame("scalaLab Basic Functions ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocation(200, 200);
        
        
        babs = new JButton("abs");
        babs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  abs(double x); \n Computes the absolute value of the number x", "Mathematical Functions - abs(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        bsqrt = new JButton("sqrt");
        bsqrt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  sqrt(double x); \n Computes the square root of x", "Mathematical Functions - sqrt(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
       
        bfloor = new JButton("floor");
        bfloor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  floor(double x); \n Computes the nearest integer smaller than x", "Mathematical Functions - floor(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        bceil = new JButton("ceil");
        bceil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  ceil(double x); \n Computes the nearest integer larger than x", "Mathematical Functions - ceil(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        blog= new JButton("log");
        blog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  log(double x, double base); \n Computes the logarithm of the number x, to the base base", "Mathematical Functions - log(x, base)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
     
        bln = new JButton("ln");
        bln.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  ln(double x); \n Computes the natural logarithm of the number x, to the base base", "Mathematical Functions - log(x, base)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
     
        bexp= new JButton("exp");
        bexp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  exp(double x)  \n Computes the e^x", "Mathematical Functions - exp(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        
        Container box = Box.createHorizontalBox();
        
        box.add(babs); box.add(bceil); box.add(bfloor); box.add(bsqrt);
        box.add(blog); box.add(bln); box.add(bexp);  
        frame.getContentPane().add(box, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    
   }
}

