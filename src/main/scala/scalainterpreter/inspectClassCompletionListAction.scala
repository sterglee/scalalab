
package  scalainterpreter


import java.awt.event.ActionEvent
import scalaExec.Interpreter.GlobalValues

class inspectClassCompletionListAction() extends javax.swing.AbstractAction() {
   

   override def actionPerformed(  e: ActionEvent ) {
         
        scalaExec.Interpreter.GlobalValues.inspectClass = true
        scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane = true  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
         
         var editor = scalaExec.Interpreter.GlobalValues.editorPane   // get editor instance
         
         var pos = editor.getCaretPosition-1  
         var doc = editor.getDocument()
       
        GlobalValues.selectionStart = -1

       var exited = false
        // take word part before cursor position
       var wb = ""
       var offset = pos
       while (offset >= 0 && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         if (ch == '.') {
               GlobalValues.methodNameSpecified = true;
               GlobalValues.selectionStart = offset+1;   
          }

         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || ch=='.' || ch=='_' || ch=='$' 
         if (!isalphaNumeric)  exited=true
          else {
           wb = wb + ch
           offset -= 1
          }
          }
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
       
    // form total word that is under caret position
         var wordAtCursor = wb.reverse+wa       
          
    //println("wordAtCursor = "+wordAtCursor)
    
           var etext = wordAtCursor 
           var className = wordAtCursor
          if (etext != null) {
          if (etext.indexOf(".") > -1) {  // the user has typed a method after the period
            var classNameSplitted = etext.split('.')
            className = classNameSplitted(0)
            if (classNameSplitted.size > 1)
             scalaSciCommands.Inspect.methodSubString = classNameSplitted(1)
           }
          else
            scalaSciCommands.Inspect.methodSubString = null
            
        println("inspecting class name: "+etext)
        scalaSciCommands.Inspect.inspectg(etext)
          
            
             }
            }  // actionPerformed
    }
    
         
