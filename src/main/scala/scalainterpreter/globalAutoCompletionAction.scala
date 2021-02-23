
package  scalainterpreter


import java.awt.event.ActionEvent
import scalaExec.Interpreter.GlobalValues
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import scalaExec.gui.AutoCompletionFrame


class globalAutoCompletionAction() extends javax.swing.AbstractAction() {
   

      def actionPerformed(e: ActionEvent) {
            val selectedStr = scalaExec.Interpreter.GlobalValues.editorPane.getSelectedText
            if (selectedStr != null)  {
            if (GlobalValues.autocompletionEnabled == false)   {  // enable autocompletion on the first Ctrl-Space press
                 GlobalValues.autocompletionEnabled = true
                 scalaExec.scalaLab.scalaLab.initAutocompletion()
              }
               
        val matches = scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.getMatchedObj(selectedStr)
                 if (matches != null) {
                 val topLevelResultsList = new javax.swing.JList(matches)
                 val detailHelpAdapter = new scalaExec.gui.autoCompleteListHandler
                 topLevelResultsList.addKeyListener(detailHelpAdapter)
                 topLevelResultsList.addListSelectionListener( new ListSelectionListener() {
                   override def valueChanged(lse: ListSelectionEvent) {
                     var selValue = topLevelResultsList.getSelectedValue().toString()
                     selValue = selValue.substring(selValue.indexOf(GlobalValues.smallNameFullPackageSeparator)+3, selValue.length())
                     GlobalValues.selectedStringForAutoCompletion = selValue
                   }
                 })
                 
       GlobalValues.autoCompletionFrame = new AutoCompletionFrame("scalalabConsole AutoCompletion ( F1 for detailed help on the selected entry)")
       val xloc = scalaExec.Interpreter.GlobalValues.editorPane.getCaret.getMagicCaretPosition.x
       val yloc = scalaExec.Interpreter.GlobalValues.editorPane.getCaret.getMagicCaretPosition.y
       GlobalValues.autoCompletionFrame.setLocation(xloc, yloc)
       GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList)
       GlobalValues.autoCompletionFrame.requestFocus
                  }
                 }
      } // actionPerformed
       
}
