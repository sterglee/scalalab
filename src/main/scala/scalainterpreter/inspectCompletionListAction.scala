
package  scalainterpreter


import java.awt.event.ActionEvent
import scalaExec.Interpreter.GlobalValues

class inspectCompletionListAction() extends javax.swing.AbstractAction() {
   

   override def actionPerformed(  e: ActionEvent ) {
         
         scalaExec.Interpreter.GlobalValues.inspectClass = false
         scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane = true  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
         
         var editor = scalaExec.Interpreter.GlobalValues.editorPane   // get editor instance
         
         var pos = editor.getCaretPosition-1  
         var doc = editor.getDocument()
      
        GlobalValues.methodNameSpecified = false
        GlobalValues.selectionStart = -1
       
       var exited = false
        // take word part before cursor position
       var wb = ""
       var offset = pos
       while (offset >= 0 && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
          if (ch == '.') {
               GlobalValues.methodNameSpecified = true;
               GlobalValues.selectionStart = offset+1;   // replace the text after '.'
          }

         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || ch=='.'  || ch=='_' || ch=='$'
         if (!isalphaNumeric)  exited=true
          else {
           wb = wb + ch
           offset -= 1
          }
          }
          
    GlobalValues.selectionBeginning = offset+1; // keep the identifier start for static members completion 

    if (GlobalValues.selectionStart == -1)  // a method name is not specified, thus set selection start to the beginning of the word
      GlobalValues.selectionStart = pos+1
        
    // take word part after cursor position
       var wa = ""
       var docLen = doc.getLength()
       offset = pos+1
       exited = false
       while (offset < docLen && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || ch=='.' || ch=='_' || ch=='$'
         if (!isalphaNumeric)  exited=true
           else {
         wa = wa + ch
         offset += 1
           }
         }
        
       GlobalValues.selectionEnd = offset
           
      //   form total word that is under caret position
         var wordAtCursor = wb.reverse+wa       
         
           var etext = wordAtCursor 
           var className = wordAtCursor
          if (etext != null) {
          if (etext.indexOf(".") > -1) {
            var classNameSplitted = etext.split('.')
            className = classNameSplitted(0)
            if (classNameSplitted.size > 1)
             scalaSciCommands.Inspect.methodSubString = classNameSplitted(1)
           }
          else
            scalaSciCommands.Inspect.methodSubString = null
            
          var completionCommand = "scalaSciCommands.Inspect.inspectCompletionList("+className+")  "
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(completionCommand)
             }
            }  // actionPerformed
    }
    
         
