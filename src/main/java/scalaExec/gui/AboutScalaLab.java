package scalaExec.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class AboutScalaLab extends javax.swing.JPanel {
    
        private JLabel scalalabLabel;
        private JLabel oderskyLabel;
        private JLabel stergLabel;
        private JLabel imgLabel;
        private JLabel paneLabel;
                
        private JLabel EJMLLabel;
        private JLabel EJMLDescrLabel;
        private JLabel plotsLabel;
        private JLabel calcScalaLabel;
        private JLabel calcScalaDescrLabel;
        private JLabel numalLabel;
        private JLabel numalDescrLabel;
        private JLabel jsciLabel;
        private JLabel jsciDescrLabel;
        private JLabel seperatorLabel1;
        private JLabel seperatorLabel2;
        private JLabel seperatorLabel3;
        private JLabel seperatorLabel4;
        private JLabel seperatorLabel5;
        private JLabel seperatorLabel6;
         private JLabel seperatorLabel7;
         
    public AboutScalaLab() {
        setLayout(new GridLayout(24,1));
        initComponents();
    }
    
    private void initComponents() {
        
                seperatorLabel1 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel1.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel1.setForeground(Color.BLUE);
                
                seperatorLabel2 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel2.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel2.setForeground(Color.BLUE);
                
                seperatorLabel3 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel3.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel3.setForeground(Color.BLUE);
                
                seperatorLabel4 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel4.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel4.setForeground(Color.BLUE);
                
                seperatorLabel5 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel5.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel5.setForeground(Color.BLUE);
                
                seperatorLabel6 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel6.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel6.setForeground(Color.BLUE);
                
                 seperatorLabel7 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel7.setFont( new Font("Arial", Font.BOLD, 24));
                seperatorLabel7.setForeground(Color.BLUE);
                
                scalalabLabel = new JLabel("scalalab:    Scientific Scripting for the Java Platform");
		scalalabLabel.setFont(new Font("Arial", Font.BOLD, 26));
                scalalabLabel.setForeground(Color.RED);
		
                        
		stergLabel = new JLabel("Research project for building a powerful and easy to use scientific programming environment");
		stergLabel.setFont(new Font("Arial", Font.BOLD, 18));
		stergLabel.setForeground(Color.BLUE);
                        
                oderskyLabel = new JLabel("Based on the powerful Scala functional-object language created by Martin Odersky");
                oderskyLabel.setFont(new Font("Arial", Font.BOLD, 22));
                oderskyLabel.setForeground(Color.RED);
                
		imgLabel = new JLabel("Stergios Papadimitriou, Dept. of Computer and Informatics Engineering,  Kavala,  Greece ");
		imgLabel.setFont(new Font("Arial", Font.BOLD, 12));
		imgLabel.setForeground(Color.BLUE);
                
                paneLabel = new JLabel("Main GUI Interface is adapted from ScalaInterpreterPane of Hanns Holger Rutz");
                paneLabel.setFont(new Font("Arial", Font.BOLD, 22));
                paneLabel.setForeground(Color.RED);
                
        
                plotsLabel = new JLabel("Plotting library is adapted from jMathPlot of Yann Richet");
                plotsLabel.setFont(new Font("Arial", Font.BOLD, 18));
                plotsLabel.setForeground(Color.DARK_GRAY);
		
                EJMLLabel  = new JLabel("Efficient Java Matrix library of Peter Abeles, Matrix Toolkit for Java and JAMA ");
                EJMLLabel.setFont(new Font("Arial", Font.BOLD, 16));
                EJMLLabel.setForeground(Color.DARK_GRAY);
                EJMLDescrLabel =  new JLabel("are used for providing zero-indexed matrices");
                EJMLDescrLabel.setFont(new Font("Arial", Font.BOLD, 16));
		EJMLDescrLabel.setForeground(Color.DARK_GRAY);

		        
                numalLabel = new JLabel("Numerical Routines for 1-indexed matrices are based on the NUMAL Numerical Analysis Library:  ");
                numalLabel.setFont(new Font("Arial", Font.BOLD, 16));
		numalLabel.setForeground(Color.DARK_GRAY);
                numalDescrLabel =  new JLabel(" \"A Numerical Library in Java for Scientists & Engineers, Hang. T. Lau,Chapman & Hall, 2004");
                numalDescrLabel.setFont(new Font("Arial", Font.BOLD, 16));
		numalDescrLabel.setForeground(Color.DARK_GRAY);
                
                jsciLabel = new JLabel("the jSci Scientific library");
                jsciLabel.setFont(new Font("Arial", Font.BOLD, 16));
		jsciLabel.setForeground(Color.BLACK);
                
                jsciDescrLabel = new JLabel("Provides scientific routines for signal processing, Wavelets etc.");
                jsciDescrLabel.setFont(new Font("Arial", Font.BOLD, 16));
                jsciDescrLabel.setForeground(Color.BLACK);
                
                add(scalalabLabel);
                add(oderskyLabel);
                add(seperatorLabel2);
                add(stergLabel);
                add(imgLabel);
                add(paneLabel);
                add(seperatorLabel3);
                add(seperatorLabel1);
                add(plotsLabel);
                add(seperatorLabel4);
                add(numalLabel);
                add(numalDescrLabel);
                add(seperatorLabel5);
                add(EJMLLabel);
                add(EJMLDescrLabel);
                add(seperatorLabel7);
                add(jsciLabel);
                add(jsciDescrLabel);
                
    }

   

}
