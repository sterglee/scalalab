package rsyntaxEdit;

import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AutocompletionRSyntaxArea  extends JFrame {

  JFrame thisFrame;    
    /** Creates a new instance of AutoCompletionFrame */
  public AutocompletionRSyntaxArea(String title) {
        super(title);
        thisFrame = this;
    }
    
  // display the matches of the autocompletion search passed with the resultsList parameter
  public  void displayMatches( JList resultsList )
     {
        JPanel  helpPanel = new JPanel();
        JScrollPane  listScroll = new JScrollPane(resultsList);
           
           
        resultsList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
         if (evt.getClickCount() == 2) {   // double click on an autocompletion result
                     
             JList source = (JList) evt.getSource();   // get the list in which this result belongs
             Object []  selection = source.getSelectedValues();  
             String selValue = (String)selection[0];
         }
        }
        });
        helpPanel.setLayout(new BorderLayout());             
        helpPanel.add(listScroll);
        add(helpPanel);
        setSize(500, 400);
        setLocation(GlobalValues.globalRSyntaxEditorPane.getLocation().x,  GlobalValues.globalRSyntaxEditorPane.getLocation().y);
        setVisible(true);
    }
    
    
    public void windowActivated(WindowEvent e) {
   // JOptionPane.showMessageDialog(null, "ok");
    }

    
        
}
    
    



