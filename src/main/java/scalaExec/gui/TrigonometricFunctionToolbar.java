
package scalaExec.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class TrigonometricFunctionToolbar  extends JPanel {
        JButton bsin, bcos, btan, basin, bacos, batan,
                bsinh, bcosh, btanh;
                
        
    public TrigonometricFunctionToolbar() {
        JFrame frame = new JFrame("scalaLab Trigonometric Functions ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocation(400, 200);
        
        
        bsin = new JButton("sin");
        bsin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  sin(double x); \n Computes the sine of x", "Mathematical Functions - sin(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bcos= new JButton("cos");
        bcos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  cos(double x); \n Computes the cosine of x", "Mathematical Functions - cos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btan= new JButton("tan");
        btan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  tan(double x); \n Computes the tangent of x", "Mathematical Functions - cos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
      
      
        bsinh = new JButton("sinh");
        bsinh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  sinh(double x); \n Computes the hyperbolic sine of x", "Mathematical Functions - hsin(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bcosh= new JButton("cosh");
        bcosh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  cosh(double x); \n Computes the hyperbolic cosine of x", "Mathematical Functions - acos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btanh= new JButton("tanh");
        btanh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  tanh(double x); \n Computes the hyperbolic tangent of x", "Mathematical Functions - cos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        basin = new JButton("asin");
        basin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  asin(double x); \n Computes the arc sine of the number x", "Mathematical Functions - asin(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        bacos= new JButton("acos");
        bacos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  acos(double x); \n Computes the arc cosine of the number x", "Mathematical Functions - acos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        batan= new JButton("atan");
        batan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          JOptionPane.showMessageDialog(null, "double  atan(double x); \n Computes the arc tangent of the number x", "Mathematical Functions - cos(x)", JOptionPane.INFORMATION_MESSAGE);
            }
        });
      
        Container box = Box.createHorizontalBox();
        box.add(bsin); box.add(bcos); box.add(btan);  box.add(basin); box.add(bacos); box.add(batan);
        box.add(bsinh); box.add(bcosh); box.add(btanh); 
        frame.getContentPane().add(box, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    
   }
}

