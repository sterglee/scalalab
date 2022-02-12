package scalainterpreter;

import java.awt.event.ActionEvent;
import javax.script.ScriptException;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import jsyntaxpane.actions.DefaultSyntaxAction;
import scalaExec.Interpreter.GlobalValues;

public class displayMatrixAction  extends  DefaultSyntaxAction {
        
        public displayMatrixAction() {
            super("Display Matrix");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
        JTextComponent tc = getTextComponent(e);
        String matrixName = null;
        
        // Get the name of the matrix to display. If there is a selection, use that as the matrix  name,
        // otherwise, scan for a matrix name around the caret
        try {
            int selStart = tc.getSelectionStart();
            int selEnd = tc.getSelectionEnd();
            if (selStart != selEnd) {
                matrixName = tc.getText(selStart, selEnd - selStart);
            }
            else {
                matrixName = getMatrixNameAtCaret(tc);
            }
        }
        catch (BadLocationException ble) {
            ble.printStackTrace();;
            UIManager.getLookAndFeel().provideErrorFeedback(tc);
            return;
            }
                 GlobalValues.globalInterpreter.interpret("displayMatrix("+matrixName+")");
          
        }
    
    // gets the signal name that the caret is sitting on
    public String getMatrixNameAtCaret(JTextComponent tc) throws BadLocationException {
        int caret = tc.getCaretPosition();
        int start = caret;
        Document doc = tc.getDocument();
        while (start > 0) {
            char ch = doc.getText(start-1, 1).charAt(0);
            if (isMatrixNameChar(ch)) {
                start--;
            }
            else {
                break;
            }
          }
        int end = caret;
        while (end < doc.getLength()) {
            char ch = doc.getText(end, 1).charAt(0);
            if (isMatrixNameChar(ch)) {
                end++;
            }
            else {
                break;
            }
        }
        return doc.getText(start, end-start);
    }
    
    public boolean isMatrixNameChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }

}
