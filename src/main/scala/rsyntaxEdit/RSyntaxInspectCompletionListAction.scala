
package rsyntaxEdit


import java.awt.event.ActionEvent
import scalaExec.Interpreter.GlobalValues

// this action inspects a completion list for the type of an identifier, e.g. var jf = new javax.swing.JFrame, jf is the identifier
// and the completion list is formed by discovering the contents of the javax.swing.JFrame class using Java reflection
class RSyntaxInspectCompletionListAction() extends javax.swing.AbstractAction() {
   

   override def actionPerformed(  e: ActionEvent ) {
         
         var editor = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane   // get RSyntaxArea based editor instance
         
         var pos = editor.getCaretPosition-1      //position of the caret
         var doc = editor.getDocument()   // the document being edited
         
        GlobalValues.methodNameSpecified = false  // a dot implies that the user tries to specify a method name
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
         if (etext.indexOf(".") > -1) {   // the user has typed a method after the period
            var classNameSplitted = etext.split('.')
            className = classNameSplitted(0)
            if (classNameSplitted.size > 1)
             scalaSciCommands.Inspect.methodSubString = classNameSplitted(1)
           }
          else
            scalaSciCommands.Inspect.methodSubString = null  // a method name not specified, scan all the methods
            
      if (scalaExec.Interpreter.GlobalValues.inspectClass==false) {
          var completionCommand = "scalaSciCommands.Inspect.inspectCompletionList("+className+")  "
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(completionCommand)
      }
      else {   // inspect a class name
        scalaSciCommands.Inspect.inspectg(etext)
      }
          
            
             }
            }  // actionPerformed
    }
    
         
