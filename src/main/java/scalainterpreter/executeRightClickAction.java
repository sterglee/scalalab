package scalainterpreter;

import java.awt.event.ActionEvent;
import javax.script.ScriptException;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import jsyntaxpane.actions.DefaultSyntaxAction;
import scalaExec.Interpreter.GlobalValues;

public class executeRightClickAction  extends  DefaultSyntaxAction {
        
        public executeRightClickAction() {
            super("Display Matrix");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = GlobalValues.globalInterpreterPane.getSelectedTextOrCurrentLine().get();
            
            if (text!=null)
            GlobalValues.globalInterpreter.interpret(text);
          
        }
    
 
}
