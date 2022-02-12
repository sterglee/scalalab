
package rsyntaxEdit



import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.Point
import javax.swing.JEditorPane
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import scalaExec.Interpreter.GlobalValues

class RSyntaxEditorMouseMotionAdapter()  extends MouseMotionAdapter {
     override def  mouseMoved(e: MouseEvent) = {
            // get the editor's instance for which the mouse motion event is directed
       var editor = e.getSource().asInstanceOf[RSyntaxTextArea]
       var  pt = new Point(e.getX(), e.getY())  // the point of the mouse cursor
       var pos = editor.viewToModel(pt)
       var doc = editor.getDocument()

     // scan to detect the beginning of the word at cursor position
       var exited = false
       var wb = ""    // word begin
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
       var wa = ""  // word after
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
         var wordAtCursor = (wb.reverse+wa).trim     // total word under cursor

if (wordAtCursor.length>0)  
  rsyntaxEdit.ProcessWordAtCursorRSyntaxPane.processWordAtCursorRSyntaxPane(wordAtCursor)

    }
     
     }
  
  
