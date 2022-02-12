
package  scalainterpreter


import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.Point
import javax.swing.JEditorPane
import scalaExec.Interpreter.GlobalValues

object currentWordAtCursorObj {
  var  currentWordAtCursor=""
}

class PaneMouseMotionAdapter()  extends MouseMotionAdapter {
  import currentWordAtCursorObj._
  
     override def  mouseMoved(e: MouseEvent) = {
       
     if  (GlobalValues.mouseMotionListenerForJSyntax==true) {  // process mouse listener events
       var editor = e.getSource().asInstanceOf[JEditorPane] 
       var  pt = new Point(e.getX(), e.getY())
       var pos = editor.viewToModel(pt)
       var doc = editor.getDocument()
       
       var exited = false
       var wb = ""
       var offset = pos
       while (offset >= 0 && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
          else {
           wb = wb + ch
           offset -= 1
          }
          }
       var wa = ""
       var docLen = doc.getLength()
       offset = pos+1
       exited = false
       while (offset < docLen && exited==false) {
         var ch = doc.getText(offset, 1).charAt(0)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
           else {
         wa = wa + ch
         offset += 1
           }
         }
         var wordAtCursor = wb.reverse+wa       

    if (currentWordAtCursor != wordAtCursor)  {
       currentWordAtCursor = wordAtCursor

//      System.out.println("word at cursor = "+wordAtCursor)
       
    if (wordAtCursor.trim.length > 0)
      rsyntaxEdit.ProcessWordAtCursorJSyntaxPane.processWordAtCursorJSyntaxPane(wordAtCursor)
      }
     }
     }
     }
  

  
