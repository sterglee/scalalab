
package  scalainterpreter


import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.Point
import javax.swing.JEditorPane
import scalaExec.Interpreter.GlobalValues


class PaneMouseListener()  extends MouseListener {
     override def  mouseClicked(e: MouseEvent) = {
       if (e.getClickCount()>=2)  {  // only on double-clicks
         
         //.getSelectedText.get.length == 0)
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
         var wordAtCursor = (wb.reverse+wa).trim       
          if (wordAtCursor.length>0)  
      rsyntaxEdit.ProcessDoubleClick.processDoubleClick(wordAtCursor)
     }
     
     }
  override  def mouseEntered(x$1: java.awt.event.MouseEvent): Unit = {}
  override  def mouseExited(x$1: java.awt.event.MouseEvent): Unit = {}
  override  def mousePressed(x$1: java.awt.event.MouseEvent): Unit = {}
  override def mouseReleased(x$1: java.awt.event.MouseEvent): Unit = {}


}