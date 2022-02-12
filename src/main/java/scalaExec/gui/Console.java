package scalaExec.gui;


import scalaExec.Interpreter.GlobalValues;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.*;

// Class implementing a console style window
// It is used by  ConsoleKeyHandler 
public class Console extends JTextArea 
{
        int xloc, yloc;
        int commandNo;

        /**store for previous commands typed at the current console */
        public Vector previousCommands;
	
	/**The position of the start of the line*/
        int lineStart;

	/**Event Handler used for handling key events*/
        KeyListener keyHandler;

        static String textArea;  // the text area of the Console
    	
        /** clears the Console's text area */
        public void clearConsole() {
            setText("");
            this.repaint();
        }

        
	/**Check that the cursor hasn't moved beyond the start 
	of the line*/	
        public void checkPosition()
	{
            int caretPosition = getCaretPosition();
            if (caretPosition  <= lineStart)  {
                //int lastPromptIndicator = getText().lastIndexOf(GlobalValues.scalalabPromptChar);
                setText(getText()+" ");
                setCaretPosition(lineStart+1);
	 }
        }
	
	/**Go to the home position*/
        public void home()
	{
            setCaretPosition(lineStart - 1);
	}

	/**Go to the end of the line*/
        public void end()
	{
            setCaretPosition(getText().length());
	}
	

	
        public  void displayCursor()
	{
        String currentText = getText();
        lineStart = currentText.length();
        setCaretPosition(lineStart);
	}
        
	//Display a new line*/
        public void newLine()
	{
        append("\n");
	}
	
	/**Display some text on a new line*/
        public void displayText(String text)
	{
        append( text + "\n" );
	}

        public static void setDoubleFmt(int decPoints )  {
            GlobalValues.doubleFormatLen  = decPoints;
            String fmtStr="0.";
            for (int k=0; k<decPoints; k++)   fmtStr+="0";
           GlobalValues.fmtString = new DecimalFormat(fmtStr);
          }
        }
  
                
    

