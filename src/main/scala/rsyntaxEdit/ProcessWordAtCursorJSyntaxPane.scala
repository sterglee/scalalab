
package rsyntaxEdit

import scalaExec.Interpreter.GlobalValues

// process the word under mouse cursor position for the JSyntaxPane editor

object ProcessWordAtCursorJSyntaxPane {

def processWordAtCursorJSyntaxPane(wd: String) = {
  
     val editor = scalaExec.Interpreter.GlobalValues.editorPane  // the jSyntaxPane based ScalaLab's editor'
    
     val wordAtCursor = wd
      
     val sI = scalaExec.Interpreter.GlobalValues.globalInterpreter   // the global Scala interpreter
  
       // let as an example that the wordAtCursor variable, is: var aa =10, wordAtCursor=="aa" 
     val  typeOfId = sI.typeOfTerm(wordAtCursor).toString()
          
  if (typeOfId.contains("<notype>")==false) {

           if (GlobalValues.lastVariableUnderMouseCursor != wordAtCursor)  {
             
             GlobalValues.lastVariableUnderMouseCursor = wordAtCursor;
             
        var valueOfId =     sI.valueOfTerm(wordAtCursor)
             //scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(wordAtCursor)

      if (GlobalValues.getValuesForAllJSyntax==true) {
        
        if (valueOfId.toString.contains("None")==false)
          editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  " +valueOfId)   

      }
      else {  // values for controlled types only
     var isPrimitiveType =  ( ( typeOfId.contains("Double") || typeOfId.contains("Int") || typeOfId.contains("Long")
           || typeOfId.contains("Char") || typeOfId.contains("Short") || typeOfId.contains("Boolean")
 || typeOfId.contains("String"))   && (typeOfId.contains("[")==false ))
     
        // for scalaSci types we have the provision to cut large strings at toString
     var isScalaSciType = typeOfId.contains("scalaSci")  
      

      if (valueOfId.toString.contains("None") == false)   {
            if (isScalaSciType == false && isPrimitiveType == false) {  // not a scalaSci or primitive type, avoid displaying value
            editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  " )   
                  }  
                  else  {
        //   var valueOfId = scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm("$$dummy").getOrElse("none")
           editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  " +valueOfId)   
                  }
                    
        } // valueOfId != "none"
        else
          editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  ")
        
       }  // values for controlled types only 
     }     // not a function: avoid displaying values for functions
     else 
       editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ] ")
      }   // <notype>                   

      
      else
             editor.setToolTipText("")
    


     
}
  
}