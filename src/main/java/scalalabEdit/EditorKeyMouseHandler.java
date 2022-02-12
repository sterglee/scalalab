package scalalabEdit;


import com.lowagie.text.Document;
import java.util.logging.Level;
import java.util.logging.Logger;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.DetailHelpFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.Iterator;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;

public class EditorKeyMouseHandler extends MouseAdapter implements KeyListener
{
        int caretPos = 0;      // track the cursor position 
        int prevCaretPos = 0;
        int  textLen = 0;  // the text length
        int fromLoc = 0;
        int toLoc = 0;
       
        public JTextComponent inputTextComponent;    // the component that keeps and handles the editing text
        
        public EditorKeyMouseHandler()
	{
	}

   public  String  getSelectedText()  {
      String  txt = inputTextComponent.getSelectedText();
      return txt;
        }
        
   public  String  getCurrentLine() {
       String currentLine=""; 
       try {
            SyntaxDocument sd =   (SyntaxDocument)inputTextComponent.getDocument();
            currentLine = sd.getLineAt(inputTextComponent.getCaretPosition());
           
        } catch (BadLocationException ex) {
            System.out.println("Bad Location exception ");
        }
        return currentLine;
   }
     
   public  String   getSelectedTextOrCurrentLine() {
       String selectedTextOrCurrentLine = getSelectedText();
       if (selectedTextOrCurrentLine==null)
           selectedTextOrCurrentLine = getCurrentLine();
       
       return selectedTextOrCurrentLine;
   }
     


        
         
    public void keyTyped(KeyEvent e){
       /* int  keyValue = e.getKeyChar();
       
        if (keyValue == KeyEvent.VK_F10);
                 display_help();      */
   }

	/**Interpret key presses*/
    public void keyPressed(KeyEvent e)
    {
        scalalabEdit.EditorPaneEdit.documentEditsPendable = true;
        int keyValue = e.getKeyCode();
        inputTextComponent = (JTextComponent)e.getSource();  // editor's keystrokes have as source the inputTextComponent JTextComponent
        prevCaretPos = caretPos;   
        
        switch (keyValue) {
                        
                      
          case   KeyEvent.VK_ENTER:
                caretPos = inputTextComponent.getCaretPosition();
                String text = inputTextComponent.getText();
                int newLineCnt = 0;
                int idx = 0;
                while (idx<caretPos)   {
                    if (text.charAt(idx) == '\n') 
                       newLineCnt++;
                    idx++;
                }
                break;

          case KeyEvent.VK_F10:
                text = inputTextComponent.getSelectedText();
                if (text != null)  {
                    String inspectCommand = "scalaSciCommands.BasicCommands.inspectg("+text+")  ";
                    GlobalValues.globalInterpreter.interpret(inspectCommand);
                }
                e.consume();
                break;
          
          
          case KeyEvent.VK_F6:
                 
     String currentText = getSelectedTextOrCurrentLine();
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(currentText);
      e.consume();
    
    break;
                
          

            case KeyEvent.VK_F2:
     String etext =  inputTextComponent.getText();
     int currentTextLen = etext.length();
     if  (currentTextLen != textLen)   // text altered at the time between F2 clicks
      {
         fromLoc = 0;    // reset
     }
    
     int cursorLoc = inputTextComponent.getCaretPosition();
     if (cursorLoc < toLoc)  {
     // reset if cursor is within the already executed part
         fromLoc = 0;
     }
     toLoc = cursorLoc;
     String textToExec = etext.substring(fromLoc, toLoc);
     
     inputTextComponent.setSelectionStart(fromLoc);
     inputTextComponent.setSelectionEnd(toLoc);
     inputTextComponent.setSelectedTextColor(java.awt.Color.RED);
     textToExec = textToExec.substring(0, textToExec.lastIndexOf("\n"));
     fromLoc += textToExec.length();
     
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(textToExec);
     
      e.consume();
    break;

            
            
            default:
                caretPos = inputTextComponent.getCaretPosition();
                
          }
    }
    
        
    public void mouseClicked(MouseEvent me)
        {
   if (me.getClickCount()>=2)  {  //only on ndouble-clicks
       JEditorPane   editor = (JEditorPane) me.getSource();
       Point  pt = new Point(me.getX(), me.getY());
       int  pos = editor.viewToModel(pt);
       javax.swing.text.Document  doc = editor.getDocument();
       
       boolean  exited = false;
       String  wb = "";
       int  offset = pos;
         // extract the part of the word before the mouse click position
       while (offset >= 0 && exited==false) {
         char  ch=' ';
                try {
                    ch = doc.getText(offset, 1).charAt(0);
                } catch (BadLocationException ex) {
                    System.out.println("Bad Location exception");
                    ex.printStackTrace();
                }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_');
         if (!isalphaNumeric)  exited=true;
          else {
           wb = wb + ch;
           offset -= 1;
           }
          }
       
       String  wa = "";
       int  docLen = doc.getLength();
       offset = pos+1;
       exited = false;
         // extract the part of the word after the mouse click position
       while (offset < docLen && exited==false) {
         char ch=' ';
                try {
                    ch = doc.getText(offset, 1).charAt(0);
                } catch (BadLocationException ex) {
                     System.out.println("Bad Location exception");
                     ex.printStackTrace();
               }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_');
         if (!isalphaNumeric)  exited=true;
           else {
         wa = wa + ch;
         offset += 1;
           }
         }
         
         StringBuffer wbreverse = new StringBuffer();
         for (int k=wb.length()-1; k>=0; k--)
             wbreverse.append(wb.charAt(k));
         
         String  wordAtCursor = wbreverse.toString()+wa;       
          
                // .scalainterpreter.displayWatchedVars watchVar(wordAtCursor)
          GlobalValues.globalInterpreter.interpret(wordAtCursor);
       }
      }
              
    
    void display_help() {
        JFrame helpFrame = new JFrame("JLab help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(400, 400);
        Container container = helpFrame.getContentPane();
        JTextArea helpText = new JTextArea();

        int classCnt = 0;
        Hashtable  clTable= new Hashtable(); 
        Enumeration enumer = clTable.elements();
        TreeSet  sortedClasses =  new TreeSet();
        while(enumer.hasMoreElements())
		{
		    Object next = (Object)enumer.nextElement();
		    Class currentClass = (Class)next;
                    String className = currentClass.getCanonicalName();
                    sortedClasses.add(className);
                    classCnt++;
        }

          Iterator iter = sortedClasses.iterator();
          while (iter.hasNext()) {
                    String className = (String)iter.next();
                    helpText.append(className+"\n");
            }
          JScrollPane  helpPane = new JScrollPane(helpText);
        
        container.add(helpPane);
        helpFrame.setVisible(true);  
                
      }
    
        
    
    public void keyReleased(KeyEvent e)
    {
    	        
    }	
    
    class autocompleteListHandler extends KeyAdapter {
        public void keyPressed(KeyEvent ktev) {
            int  keyCode = ktev.getKeyCode();
            if (keyCode == KeyEvent.VK_F1) {
                display_detailed_help(GlobalValues.selectedStringForAutoCompletion);
            }
            if (keyCode == KeyEvent.VK_SPACE) {
                ktev.consume();
                GlobalValues.autoCompletionFrame.dispose();
            }
            
        }
        
    
    // displays detailed help for the selected item
    void display_detailed_help(String selectedItem) {
GlobalValues.detailHelpStringSelected = selectedItem;
DetailHelpFrame detailFrame = new DetailHelpFrame();
detailFrame.setVisible(true);
        
      }
    
        
}
}