
package  scalainterpreter


import java.awt.event.ActionEvent
import jsyntaxpane.SyntaxDocument
import javax.swing.text.JTextComponent
import jsyntaxpane.actions.gui.ComboCompletionDialog
import jsyntaxpane.actions.DefaultSyntaxAction
import scalaExec.Interpreter.GlobalValues
import tools.nsc.interpreter.shell.ReplCompletion
import scala.jdk.CollectionConverters 

//  the action triggered with the F7 key within the JSyntaxPane
class CompletionAction( completer: ReplCompletion )  {
  
   def complete(  ) {

     var posAutoCompletion =  -1
       
    // compute completion word (cw) and its start position (start) at the editing buffer
      val (cw, start) = {

      // if the user has selected text we return it 
      val sel = GlobalValues.editorPane.getSelectedText
         if( sel != null ) {
            (sel, GlobalValues.editorPane.getSelectionStart)
         } 
         
      else {  // the user has not selected text
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
             if (GlobalValues.selectionStart == -1)
                GlobalValues.selectionStart = offset+1;   // replace the text after the last '.'
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

          (wordAtCursor, posAutoCompletion)
        
         }
      }  // close of val (cw,start)
      

  
     val cwlen = cw.length()
     val completions =  completer.complete( cw, cwlen )   // autocomplete with the Scala completer
     val candidates = completions.candidates.filterNot(_.isUniversal)

      var completionList = new java.util.ArrayList[String]
      
    // nothing to complete
   if( candidates.isEmpty )  return
  else {   // extract the names of the completion items from the CompletionCandidate class representation
     var candidateLen = "CompletionCandidate(".length

     candidates.foreach { x =>
      var xs = x.toString
      var xss = xs.substring(candidateLen, xs.length)
      var xname = xss.substring(0, xss.indexOf(","))
      completionList.add (xname)
      }
    }

           //  System.out.println("size of Completion List = "+completionList.size)
             
             scalaSciCommands.Inspect.displayCompletionList(cw, completionList)
             

      
   }
}
