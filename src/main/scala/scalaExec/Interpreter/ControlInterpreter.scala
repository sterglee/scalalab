
package scalaExec.Interpreter

import scala.tools.nsc.Settings

// offers some operations for controlling the interpreter
object ControlInterpreter {

  //  control optimization and target JVM class options
  def prepareSettings(settings: Settings) = { 
    
     //   settings.Xexperimental.value = true
     //unreachable-code simplify-jumps empty-line-numbers empty-labels compact-locals nullness-tracking closure-elimination inline-project inline-global l:method l:project l:classpath
  
  //  settings.Ybackend.value = "GenBCode"
    //settings.Ydelambdafy.value = "inline"
 //   println("setting experimental")
  
  settings.deprecation.value = false
  
  if (scalaExec.Interpreter.GlobalValues.compilerOptimizationFlag == true) 
    settings.optimise.value = true
     else
    settings.optimise.value = false
  

    
   //  println("Setting Optimization:  "+ settings.optimise.toString)
   //  println("Setting Target:  "+scalaExec.Interpreter.GlobalValues.targetSetting(scalaExec.Interpreter.GlobalValues.currentTargetSelectionIndex))
    
   // settings.target.tryToSetColon(scala.List[String](scalaExec.Interpreter.GlobalValues.targetSetting(scalaExec.Interpreter.GlobalValues.currentTargetSelectionIndex)))
   }


  
 // if a global flag is true, record and execute a command, else simply execute it
        def recordAndInterpret(code: String): Unit = {

    try {
          var inputString = code
          inputString = inputString.replaceAll("varf", "private[this] var " )
          inputString = inputString.replaceAll("valf", "private[this] val " )

            if (inputString.indexOf("@MATLAB") != -1)  {

var sf = inputString.split("@MATLAB")
for (k<-0 until sf.length) {

  var subcommand = sf(k)
  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret( subcommand )

             }
            }
            else {
          if (scalaExec.Interpreter.GlobalValues.recordCommandsForReplayingFlag == true)  {  // record and execute
            scalaExec.Interpreter.GlobalValues.replayingBuffer.add(inputString)
        var grResult =  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret( inputString )
                     if (grResult != null) {
        var rmSuccess = grResult.toString.replace("Success", "")

        GlobalValues.consoleOutputWindow.output.append(rmSuccess)
        GlobalValues.consoleOutputWindow.output.setCaretPosition(GlobalValues.consoleOutputWindow.output.getText().length())
                 }
             }   // record and execute
            else
              scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret( inputString )
            
    }
    System.out.flush

    }  // try
    catch {
      case ex:  InterruptedException => { println("record and interpret interrupted exception "); return }
       }
     }
   
   

  // replay the buffered commands of the previous session
     def  replayBuffer()  = {
        var r =   new Runnable {
                     def run {
                       var vsize = scalaExec.Interpreter.GlobalValues.replayingBuffer.size
                       for (idx <- 1 until vsize)   { // interpret again all commands avoiding first command which are the automatically inserted imports
                           scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(scalaExec.Interpreter.GlobalValues.replayingBuffer.elementAt(idx))
                           }
                        }
                   }
                var execThread =  new Thread(r)
                execThread.start
                
          }
     
  
}
