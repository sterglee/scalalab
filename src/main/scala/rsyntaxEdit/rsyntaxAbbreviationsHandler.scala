
package rsyntaxEdit

// handles abbreviation task for the text at the current cursor's position
object rsyntaxAbbreviationsHandler {

    var  abbreviationsMapInited = false
    
    var  abbreviationsMap = Map[String, String]()
    
    def   initAbbreviationMap()  = {
         // build the abbreviations map at the first time
      if (abbreviationsMapInited == false) {
         abbreviationsMap = scalalabEdit.EditUtils.buildAbbreviationsMap(scalaExec.Interpreter.GlobalValues.workingDir+java.io.File.separatorChar+"Abbreviations.txt")
         // println("Abbreviations map = "+abbreviationsMap.toString())
         abbreviationsMapInited = true
       }
        abbreviationsMapInited    // should be true for successful init
      }
      
  
  // detects and returns the word at the current caret location
  def  detectAndReplaceWordAtCaret  = {
    var caretPosition = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.getCaretPosition-1
  
    var txt = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.getText  // the whole editor's text
    
    var exited = false
    var wb = ""
    var offset = caretPosition
       while (offset >= 0 && exited==false) {
         var ch = txt(offset)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
          else {
           wb = wb + ch
           offset -= 1
          }
          }
          
    scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.setSelectionStart(offset+1)
          
       var wa = ""
       var docLen = txt.length()
       offset = caretPosition+1
       exited = false
       while (offset < docLen && exited==false) {
         var ch = txt(offset)
         var isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || (ch == '_')
         if (!isalphaNumeric)  exited=true
           else {
         wa = wa + ch
         offset += 1
           }
         }
         var wordAtCursor = wb.reverse+wa       

    scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.setSelectionEnd(offset)
    
    initAbbreviationMap()  // if not inited init the abbreviations map
    
    if (abbreviationsMap.contains(wordAtCursor))
  scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.replaceSelection( abbreviationsMap(wordAtCursor))
  
    wordAtCursor
   }
   
  
   
  }
  


