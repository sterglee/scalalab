
package scalaExec.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import scalaExec.Interpreter.GlobalValues;


    public class autoCompleteListHandler extends KeyAdapter {
        static private boolean  helpDisplayed = false;
    @Override
        public void keyPressed(KeyEvent ktev) {
            int  keyCode = ktev.getKeyCode();
            if (keyCode == KeyEvent.VK_F1) {
             if (helpDisplayed == false) 
                display_detailed_help(GlobalValues.selectedStringForAutoCompletion);
                helpDisplayed = !helpDisplayed;
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