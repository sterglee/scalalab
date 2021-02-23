
package  scalainterpreter


import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane

// handles abbreviation task for the text at the current cursor's position
object AbbreviationHandler {

  
import javax.swing._

    var  abbreviationsMapInited = false
    
  // the abbreviations are kept with a Scala Map 
    var  abbreviationsMap = Map[String, String]()
    
    def   initAbbreviationMap()  = {
         // build the abbreviations map at the first time
      if (abbreviationsMapInited == false) {
         abbreviationsMap = scalalabEdit.EditUtils.buildAbbreviationsMap(scalaExec.Interpreter.GlobalValues.workingDir+java.io.File.separatorChar+"Abbreviations.txt")
         //println("Abbreviations map = "+abbreviationsMap.toString())
         abbreviationsMapInited = true
       }
        abbreviationsMapInited    // should be true for successful init
      }
      
  def displayAbbreviations() = {
    initAbbreviationMap()
     val  abbrFrame = new JFrame("Abbreviations for editors")
     var  abbrevs = new StringBuilder()
     for (entry <-  scalainterpreter.AbbreviationHandler.abbreviationsMap) 
       abbrevs.append(entry.toString() + "\n")
     
     
     val  jt = new RSyntaxTextArea()
      jt.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA)
      jt.setCodeFoldingEnabled(true)
      
      jt.setText(abbrevs.toString())
     
     
     var jscr = new RTextScrollPane(jt)
     abbrFrame.add(jscr)
     abbrFrame.pack()
     abbrFrame.setVisible(true)
   
    }
  
  // detects and returns the word at the current caret location
  def  detectAndReplaceWordAtCaret  = {
    
    var wordAtCursor = scalaExec.Interpreter.GlobalValues.editorPane.getSelectedText

    if (wordAtCursor!=null) {
    
    initAbbreviationMap()  // if not inited init the abbreviations map
    
    if (abbreviationsMap.contains(wordAtCursor))
  scalaExec.Interpreter.GlobalValues.editorPane.replaceSelection( abbreviationsMap(wordAtCursor))
    }
    wordAtCursor
   }
   
  
   
  }
  
      
      
    
      

