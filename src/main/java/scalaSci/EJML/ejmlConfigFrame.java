
package scalaSci.EJML;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ejmlConfigFrame extends JFrame {
  JTextField  blockSizeField;
  JTextField  blockCholeskySizeField;
  JTextField  smallMatrixMultiplyField;
  JTextField  blockSwitchCholeskyField;
  JTextField  blockSwitchQRField;
  
    public ejmlConfigFrame() {
       setLayout(new GridLayout(6,  3));
       
       add(new JLabel("Block Size"));
       blockSizeField = new JTextField(Integer.toString(org.ejml.EjmlParameters.BLOCK_WIDTH));
       add(blockSizeField);
       JButton updateBlockSizeButton = new JButton("Update Block Size");
       add(updateBlockSizeButton);
       updateBlockSizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              int newBlockSize = Integer.parseInt(blockSizeField.getText());
              StaticMathsEJML.setBlockWidth(newBlockSize);
              System.out.println("block size updated to: "+newBlockSize);
            }
        });
       
       add(new JLabel("Block Cholesky Size"));
       blockCholeskySizeField = new JTextField(Integer.toString(org.ejml.EjmlParameters.BLOCK_WIDTH));
       add(blockCholeskySizeField);
       JButton updateBlockCholeskySizeButton = new JButton("Update Block Cholesky Size");
       add(updateBlockCholeskySizeButton);
       updateBlockCholeskySizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              int newBlockCholeskySize = Integer.parseInt(blockCholeskySizeField.getText());
              StaticMathsEJML.setBlockCholWidth(newBlockCholeskySize);
              System.out.println("block Cholesky size updated to: "+newBlockCholeskySize);
            }
        });
       
       
       add(new JLabel("Switch small matrix multiply"));
       smallMatrixMultiplyField = new JTextField(Integer.toString(org.ejml.EjmlParameters.MULT_COLUMN_SWITCH));
       add(smallMatrixMultiplyField);
       JButton updateSmallMatrixMultiplyButton = new JButton("Update Small Matrix Multiply Switch");
       add(updateSmallMatrixMultiplyButton);
       updateSmallMatrixMultiplyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              int newSmallMatrixSwitch  = Integer.parseInt(smallMatrixMultiplyField.getText());
              StaticMathsEJML.setMultColumnSwitch(newSmallMatrixSwitch);
              System.out.println("small matrix multiply switch updated to: "+newSmallMatrixSwitch);
            }
        });
       
       add(new JLabel("Block Switch Cholesky"));
       blockSwitchCholeskyField  = new JTextField(Integer.toString(org.ejml.EjmlParameters.SWITCH_BLOCK64_CHOLESKY));
       add(blockSwitchCholeskyField);
       JButton updateSwitchCholeskyButton = new JButton("Update Block Switch Cholesky");
       add(updateSwitchCholeskyButton);
       updateSwitchCholeskyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              int newSwitchCholesky = Integer.parseInt(blockSwitchCholeskyField.getText());
              StaticMathsEJML.setSwitchNlock64Cholesky(newSwitchCholesky);
              System.out.println("block switch Cholesky updated to: "+newSwitchCholesky);
            }
        });
       
       
       add(new JLabel("Block Switch QR"));
       blockSwitchQRField  = new JTextField(Integer.toString(org.ejml.EjmlParameters.SWITCH_BLOCK64_QR));
       add(blockSwitchQRField);
       JButton updateSwitchQRButton = new JButton("Update Block Switch QR");
       add(updateSwitchQRButton);
       updateSwitchQRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              int newSwitchQR  = Integer.parseInt(blockSwitchQRField.getText());
              StaticMathsEJML.setSwitchNlock64QR(newSwitchQR);
              System.out.println("block switch QR updated to: "+newSwitchQR);
            }
        });
       
       setTitle("EJML Library Parameter Configuration");
       //setSize(600, 400);
       pack();
       setVisible(true);
    }
    
}
