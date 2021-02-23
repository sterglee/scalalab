
package SysUtils;

import scalaExec.Interpreter.GlobalValues;
import  java.awt.*;
import  java.awt.event.*;
import  javax.swing.*;
import  java.io.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;

// provides convenient output for the results of the scripting commands 
// also, we redirect the System.out stream, and thus printed output from JVM appears 
// at the ConsoleWindow's output JTextArea component
public class ConsoleWindow {
    public static PrintStream consoleStream;
    
    JPopupMenu  consolePopup = new JPopupMenu();  // a popup menu for the ConsoleWindow
    JMenuItem cutJMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
    JMenuItem copyJMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
    JMenuItem pasteJMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
    JMenuItem verboseJMenuItem = new JMenuItem(new verboseOutputAction());
    public JTextArea output = new JTextArea();   // here output appear
    JMenuItem clearOutputItem = new JMenuItem(new clearOutputAction(this));
        
    int totalLength=0;   // the length of the contents of the output
         
  
            public ConsoleWindow()
	{
 
     consolePopup.setFont(GlobalValues.puifont);
     pasteJMenuItem.setFont(GlobalValues.puifont);
     consolePopup.add(pasteJMenuItem);
     copyJMenuItem.setFont(GlobalValues.puifont);
     consolePopup.add(copyJMenuItem);
     cutJMenuItem.setFont(GlobalValues.puifont);
     consolePopup.add(cutJMenuItem);
     clearOutputItem.setFont(GlobalValues.puifont);
     consolePopup.add(clearOutputItem);
     verboseJMenuItem.setFont(GlobalValues.puifont);
     consolePopup.add(verboseJMenuItem);

     output.add(consolePopup);
     output.setToolTipText("Displays Java's output sent at System.out stream and messages produced from Java's runtime exceptions");

      output.addMouseListener(new MouseAdapterForConsole());   // handles right mouse clicks

         Font consoleFont = new Font(GlobalValues.outConsoleFontName, Font.PLAIN, Integer.parseInt(GlobalValues.outConsoleFontSize));
                 
         output.setFont(consoleFont);
         output.setEditable(false);
         output.setBackground( Color.black );
         output.setForeground( Color.white);

         output.setToolTipText("Displays the program output redirecting the System.out.print(), System.err.print(), System.out.println() etc commands");
         
  
         
         // define a PrintStream that sends its bytes to the output text area
         consoleStream = new PrintStream( new OutputStream () {
            @Override
                public void write(int b)  {}   // never called
                public void write(  byte []  b, int off, int len )
                {
                    String outStr = new String(b, off, len);
                    append(outStr);   // append the output to the text area
                }
            });

         // set both System.out and System.err to that stream
        System.setOut(consoleStream);
        System.setErr(consoleStream);

     GlobalValues.outputPane  =  new JScrollPane();
     GlobalValues.outputPane.setViewportView(output);
     
     DefaultCaret caret = (DefaultCaret) output.getCaret();
     caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        
     }
   
        public  void  append(  String str  )  {

        output.append( str );
        totalLength += str.length();
       updateCaret();
      }

                  
             public void resetText( String str ) {

                   output.setText(str);
         if ( str == null ) 
             totalLength = 0;
         else
             totalLength = str.length();
         updateCaret();
             }
             

      public void  updateCaret()  {
          int pos = totalLength-1;   // last position of the output buffer
          int prevPos = pos;

            // detect position of the last newline in console's text
          String txt = output.getText();
          while (txt.charAt(pos)!='\n' && pos > 0) {
              pos--;
          }
          
          int caretPos = Math.max( 0, pos+1);
          if (GlobalValues.scalalabMainFrame !=null) {
           Font consoleFont = GlobalValues.consoleOutputWindow.output.getFont();
           int pts = consoleFont.getSize();
               
           int xSize = GlobalValues.scalalabMainFrame.getSize().width;
           GlobalValues.consoleCharsPerLine =(int) (0.8*(xSize/pts));
          }           
          if (prevPos-pos > GlobalValues.consoleCharsPerLine)  {
              // caretPos = pos+1;
              output.append("\n");
              caretPos = output.getText().length();
                      
          }
          
          try {
            output.setCaretPosition( caretPos );  // totalLength - 1 ));
         }
         catch (Exception e) {
             e.printStackTrace();
         }
      }
      
      
   
            
   private class MouseAdapterForConsole  extends  MouseAdapter {
          public void mousePressed(MouseEvent e) {
              if (e.isPopupTrigger()){
                consolePopup.show((Component) e.getSource(), e.getX(), e.getY());
             }
           }

        public void mouseReleased(MouseEvent e) {
           if (e.isPopupTrigger()){
                 consolePopup.show((Component) e.getSource(), e.getX(), e.getY());
             }

          }

        }


   
}


