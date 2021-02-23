
package scalaExec.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.lexers.JavaLexer;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.ClassLoaders.ReflectionUtils;

public class DetailHelpFrame extends JFrame    {
          JPanel  helpPanel;
          JEditorPane  helpText;
          SyntaxDocument docVar;
          JScrollPane  helpPane;
          String selectedItem;
          Point autoCompletionFrameLoc;
          
 public DetailHelpFrame() {
         
    String fullItem = selectedItem = GlobalValues.detailHelpStringSelected;
    setTitle("Detailed help on "+selectedItem);
    int selIdx = fullItem.indexOf(GlobalValues.smallNameFullPackageSeparator);
    int seperatorLen = GlobalValues.smallNameFullPackageSeparator.length();
    String smallName = null;
    if ( selIdx != -1 )  { // extract the full Java name in order to provide help with reflection
            selectedItem = fullItem.substring(selIdx+seperatorLen, selectedItem.length());
            selectedItem = selectedItem.trim();
            smallName = (fullItem.substring(0, selIdx)).trim();
  }
    
    String helpString="";
         
    if (helpString==null) helpString="";
    
      try {
         Class selectedClass= scalaExec.Interpreter.GlobalValues.globalInterpreter.classLoader().loadClass(selectedItem);
         
         helpString += ReflectionUtils.getConstructors(selectedClass);
         helpString += ReflectionUtils.getMethods(selectedClass);
         helpString += ReflectionUtils.getFields(selectedClass);
      }
        catch (ClassNotFoundException ex)  {
        }

    helpText = new JEditorPane();   // construct a JEditorPane component
      
    helpPane =  new JScrollPane(helpText);
    helpText.setContentType("text/java");
    helpText.setText(helpString);
        
     helpPanel = new JPanel();
     helpPanel.setLayout(new BorderLayout());
     helpPanel.add(helpPane);
     
     setLayout(new BorderLayout());         
     
     setSize((int)((double) GlobalValues.sizeX/2.0),  (int)((double) GlobalValues.sizeY/1.5));
    autoCompletionFrameLoc = GlobalValues.autoCompletionFrame.getLocation();
    //setLocation(0autoCompletionFrameLoc.x, autoCompletionFrameLoc.y+GlobalValues.autoCompletionFrame.getSize().height);
 setLocation(0,  autoCompletionFrameLoc.y+GlobalValues.autoCompletionFrame.getSize().height);
     //helpText.append(helpString);  
    
    if (smallName != null) {
      int idxStart = helpString.indexOf(smallName);
      helpText.setSelectionStart(idxStart);
      helpText.setSelectionEnd(idxStart+smallName.length());
    }
    
     helpText.setSelectedTextColor(Color.RED);
     helpPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
     helpPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
          
     add(helpPanel);
     setVisible(true);
     
     addComponentListener(new ComponentListener() {
         public void componentHidden(ComponentEvent e) {
         }
         public void componentMoved(ComponentEvent e) {
         }
         public void componentResized(ComponentEvent e) {
        repaint(); 
    }
         public void componentShown(ComponentEvent e) {
         }
     });
     
     
        }             
        
 

}
        
    
        

    
  
    

