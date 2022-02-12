
package rsyntaxEdit

import scalaExec.Interpreter.GlobalValues

// process the word under mouse cursor position for the RSyntaxPane editor,
// the behavior depends on whether we are in ScalaSci mode (last execution key stroke is F6),
// 
object ProcessWordAtCursorRSyntaxPane  {

def processWordAtCursorRSyntaxPane(wd: String) = {
  
    var editor = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane
    
  var wordAtCursor = wd
     
      var sI = scalaExec.Interpreter.GlobalValues.globalInterpreter   // the global Scala interpreter
  
       // let as an example that the wordAtCursor variable, is: var aa =10, wordAtCursor=="aa" 
     var  typeOfId = sI.typeOfTerm(wordAtCursor).toString()
          
  if (typeOfId != "<notype>") {

      typeOfId = typeOfId filter (_ != '(' ) filter ( _ != ')')   // remove some strange parenthesis the interpreter returns before type
      
      if (typeOfId.contains(":")==false) 
       {  // not a function: avoid displaying values for functions
       // take in $$dummy synthertic variable the identifier as a string, e.g. for : var aa = 10, is  $$dymmy = "aa"
     // var $$dummy = ""+wordAtCursor  
      // construct command to extract the value of the variable, e.g. var $$dummy = aa
   //   var execString = "var $$dummy = "+$$dummy 
  //    sI.quietRun(execString)  // execute quitely, the required value is assigned to the synthetic variable $$dummy

//        var valueOfId = scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm("$$dummy").getOrElse("none")

           if (GlobalValues.lastVariableUnderMouseCursor != wordAtCursor)  {
             
             GlobalValues.lastVariableUnderMouseCursor = wordAtCursor;
     
        
        var valueOfId =      scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(wordAtCursor)
                  
      
      if (GlobalValues.getValuesForAllRSyntax==true) {
        
        if (valueOfId != "none")  
          editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  " +valueOfId)   
         else
          editor.setToolTipText(wordAtCursor+" [ "+ typeOfId+" ]  ")
       
      }
      else {  // values for controlled types only
     var isPrimitiveType =  ( ( typeOfId.contains("Double") || typeOfId.contains("Int") || typeOfId.contains("Long")
           || typeOfId.contains("Char") || typeOfId.contains("Short") || typeOfId.contains("Boolean")
 || typeOfId.contains("String"))   && (typeOfId.contains("[")==false ))
     
        // for scalaSci types we have the provision to cut large strings at toString
     var isScalaSciType = typeOfId.contains("scalaSci")  
      
          
            // display also size for scalaSci types
        if (isScalaSciType) {
         val  sizeOfId =      scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(wordAtCursor+".size")
         wordAtCursor = wordAtCursor+  " ("+sizeOfId+") "
        }
        
      if (valueOfId != "none")   {
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
      
  }
