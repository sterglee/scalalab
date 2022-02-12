package rsyntaxEdit;

import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Sequence;
import java.awt.Container;
import java.awt.Point;

import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.AutoCompletionFrame;
import scalaExec.gui.DetailHelpFrame;



// handles the mouse clicks for the rsyntaxArea editor
public class EditorKeyMouseHandler extends MouseAdapter implements KeyListener
{
        int caretPos = 0;      // track the cursor position
        int prevCaretPos = 0;
        int  textLen = 0;  // the text lenngth
        int fromLoc = 0;
        int toLoc = 0;

        public RSyntaxTextArea  editorPane=null;    // the component that keeps and handles the editing text
        public RSyntaxDocument syntaxDocument=null;

public EditorKeyMouseHandler()
	{
	}


// update fields denoting the document in the editor, necessary when a new document is edited
  public  RSyntaxDocument updateDocument()  {
     // get the document from the editor
 syntaxDocument  = (RSyntaxDocument) editorPane.getDocument();
          
 return syntaxDocument;
  }


 // get the text of the current line (the line over which the caret is placed)
   public  String  getCurrentLine() {
       if (syntaxDocument   == null)
           updateDocument();

       RSyntaxDocument  myDoc = syntaxDocument;  // the RSyntaxDocument being edited

       int caretpos = editorPane.getCaretPosition();  // the caret's current position
       int startpos = editorPane.getCaretOffsetFromLineStart();  // offset of caret from the start of line
       // subtract the offset from the start of line, in order scanpos to be the position of the start of the line
       int scanpos = caretpos-startpos;   
        // scan until a newline accumulating the line
       String s = "";
       try {
            char ch = myDoc.charAt(scanpos);
       while (ch!='\n') {
           s += myDoc.charAt(scanpos);
           scanpos += 1;
           ch = myDoc.charAt(scanpos);
       }
       } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

       return s;   // return the string of the current line
   }

// get the selected text if a selection exists, else the current line
   public  String   getSelectedTextOrCurrentLine() {
       String selectedTextOrCurrentLine = editorPane.getSelectedText();
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
        rsyntaxEditor.documentEditsPendable = true;  // set the flag in order to warn the user to save the changes
        int keyValue = e.getKeyCode();  
        editorPane  = (RSyntaxTextArea)e.getSource();  // editor's keystrokes have as source the inputTextComponent JTextComponent
        prevCaretPos = caretPos;

        switch (keyValue) {

             case KeyEvent.VK_F4:
                 scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane = false;  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
         
                if (e.isShiftDown())  {   // treat the identifier under the cursor as a variable, e.g.  var  jf = new javax.swing.JFrame(), jf is a variable
                 GlobalValues.inspectClass = true;
                 rsyntaxEdit.RSyntaxInspectCompletionListAction inspect = new rsyntaxEdit.RSyntaxInspectCompletionListAction();
                 inspect.actionPerformed(null);
                }
                else {   //  treat the identifier under the cursor as a type, e.g. var jf = new javax.swing.JFrame(), javax.swing.JFrame() is a type
                GlobalValues.inspectClass = false;   
                rsyntaxEdit.RSyntaxInspectCompletionListAction inspect = new rsyntaxEdit.RSyntaxInspectCompletionListAction();
                inspect.actionPerformed(null);
                    
                }
                
                
                break;

                 
                     
                   
            case KeyEvent.VK_F6:

                
                if (e.isControlDown()) {      // stop the current thread
  if (GlobalValues.currentThread != null)   {
      System.out.println("Stopping current thread"); 
      GlobalValues.currentThread.stop();
      GlobalValues.currentThread = null;
  }
                } 
                
                
                else
                 if (e.isShiftDown() == true) {   // run with a seperate thread,  Swing is not thread-safe, thus this is not safe!!
 
                     
   try {
                    String bcurrentText = getSelectedTextOrCurrentLine();
                    bcurrentText = bcurrentText.replaceAll("varf", "private[this] var " );
                    final String currentText = bcurrentText.replaceAll("valf", "private[this] val " );

     Runnable newThreadRunnable  = new Runnable() {

            public void run() {
         
               if (currentText.indexOf("@MATLAB") != -1)  {
              
String  [] sf = currentText.split("@MATLAB");
for (int k=0; k<sf.length; k++) {
  
  String  subcommand = sf[k];
  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret( subcommand );
  
             }
            }
               else
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(currentText);

            }
        };  //  newThreadRunnable
     
     Thread execThread  = new Thread(newThreadRunnable);
     execThread.start();
     
     // add the future to the recent future list
     if (execThread.getState() == Thread.State.RUNNABLE)
       scalaExec.Interpreter.GlobalValues.pendingThreads.addThread(execThread);

     //execFuture.get();
     //Thread execThread = new Thread(newThreadRunnable);
     //GlobalValues.currentThread = execThread;
     //execThread.start();
     System.out.flush();
   }

            catch (Exception ex) {
                ex.printStackTrace();
            }

      e.consume();
   }  //// isShiftDown()
  else   if (e.isShiftDown()==false) {    //  if Shift is down, F6 runs safely at the context of the Event  Dispatch Thread
                    String bcurrentText = getSelectedTextOrCurrentLine();
                    bcurrentText = bcurrentText.replaceAll("varf", "private[this] var " );
                    final String currentText = bcurrentText.replaceAll("valf", "private[this] val " );

                    
 SwingUtilities.invokeLater(new Runnable() {

                @Override
               public void run() {
                

               if (currentText.indexOf("@MATLAB") != -1)  {
              
String  [] sf = currentText.split("@MATLAB");
for (int k=0; k<sf.length; k++) {
  
  String  subcommand = sf[k];
  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret( subcommand );
  
             }
            }
               else
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(currentText);

  
     GlobalValues.consoleOutputWindow.output.setCaretPosition(GlobalValues.consoleOutputWindow.output.getText().length());


            }
        });
      e.consume();

                }

    break;


        
                
     case KeyEvent.VK_F9:
          JOptionPane.showMessageDialog(null, "To execute Java code paste it at the SyntaxPane editor", "RSyntaxTextArea does not support Java code execution", JOptionPane.INFORMATION_MESSAGE);
          break;


     
     case KeyEvent.VK_F5:
         GlobalValues.consoleOutputWindow.resetText( " ");
         e.consume();
         break;

     case KeyEvent.VK_F11:
         if (e.isShiftDown()==false)  {   // treat the identifier under the cursor as a variable, e.g.  var  jf = new javax.swing.JFrame(), jf is a variable
          rsyntaxEdit.rsyntaxAbbreviationsHandler.detectAndReplaceWordAtCaret();
         }
         else {
          new scalaExec.gui.CompileExecutePaneActionJava().executeTextExternalJava();
          e.consume();
          break;
    
         }
          e.consume();
         break;
         
            case KeyEvent.VK_F2:
     String etext =  editorPane.getText();
     int currentTextLen = etext.length();
     if  (currentTextLen != textLen)   // text altered at the time between F2 clicks
      {
         fromLoc = 0;    // reset
     }

     int cursorLoc = editorPane.getCaretPosition();
     if (cursorLoc < toLoc)  {
     // reset if cursor is within the already executed part
         fromLoc = 0;
     }
     toLoc = cursorLoc;
     String textToExec = etext.substring(fromLoc, toLoc);

     editorPane.setSelectionStart(fromLoc);
     editorPane.setSelectionEnd(toLoc);
     editorPane.setSelectedTextColor(java.awt.Color.RED);
     textToExec = textToExec.substring(0, textToExec.lastIndexOf("\n"));
     fromLoc += textToExec.length();

     final String partOfText  = textToExec;

     Runnable newThreadRunnable  = new Runnable() {

            public void run() {
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(partOfText);
            }
        };

     Thread execThread = new Thread(newThreadRunnable);
     GlobalValues.currentThread = execThread;
     execThread.start();

      e.consume();
   break;

            case KeyEvent.VK_SPACE:
            break;
                
            case KeyEvent.VK_F7:
                new scala.tools.nsc.interpreter.shell.ReplCompletion(GlobalValues.globalInterpreter, new scala.tools.nsc.interpreter.shell.Accumulator());
                break;

                
            case KeyEvent.VK_F1:
            case KeyEvent.VK_F3:

                String inputString  = editorPane.getSelectedText();
                    if (inputString != null)   {   // some text is selected

                 if (GlobalValues.autocompletionEnabled == false)   {  // enable autocompletion on the first F1 press
                       GlobalValues.autocompletionEnabled = true;
                       scalaExec.scalaLab.scalaLab.initAutocompletion();
                   }

               String [] matches = scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.getMatched(inputString);

                final JList  resultsList = new JList(matches);
                autocompleteListHandler  detailHelpAdapter = new autocompleteListHandler();
                resultsList.addKeyListener(detailHelpAdapter);


                resultsList.addListSelectionListener(new ListSelectionListener() {
                         public void valueChanged(ListSelectionEvent e) {
                             String  selValue = resultsList.getSelectedValue().toString();
                             GlobalValues.selectedStringForAutoCompletion = selValue;

                         }
                     }
                            );

                GlobalValues.autoCompletionFrame = new AutoCompletionFrame("ScalaLab editor autocompletion, Press F1  for detailed help on the selected entry");
                GlobalValues.autoCompletionFrame.displayMatches(resultsList);
                         }     // some text is selected

                e.consume();  // consume this event so it will not be processed in the default manner by the source that originated it
                  		//get the text on the current line
                    break;


            default:
                caretPos = editorPane.getCaretPosition();

          }
    }


    public void mouseClicked(MouseEvent me)
        {

   if (me.getClickCount()>=2)  {  //only on ndouble-clicks
       RSyntaxTextArea    editor = (RSyntaxTextArea) me.getSource();
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

         String  wordAtCursor = (wbreverse.toString()+wa).trim();


         
if (wordAtCursor.length()>0)      
      rsyntaxEdit.ProcessDoubleClick.processDoubleClick(wordAtCursor);
    }
   }



    void display_help() {
        JFrame helpFrame = new JFrame("ScalaLab  help");
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




}



    // displays detailed help for the selected item
    public static void display_detailed_help(String selectedItem) {
GlobalValues.detailHelpStringSelected = selectedItem;
DetailHelpFrame detailFrame = new DetailHelpFrame();
detailFrame.setVisible(true);

      }



}
