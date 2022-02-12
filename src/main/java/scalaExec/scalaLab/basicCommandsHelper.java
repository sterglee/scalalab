
package scalaExec.scalaLab;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;

public class basicCommandsHelper extends JFrame {

  public basicCommandsHelper() {
    JTextArea textArea = new JTextArea(scalaSciCommands.BasicCommands.commands);
    textArea.setFont(new Font("Arial", Font.PLAIN, 12));
    JScrollPane scrollText = new JScrollPane(textArea);
    
    add(scrollText,  BorderLayout.CENTER);
    setTitle("scalalab basic commands");
    setSize(800, 500);
    setVisible(true);
  }
}
