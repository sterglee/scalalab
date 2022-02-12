

package  scalainterpreter

object DisplayWatchedVars {

  def watchVar(varName: String) = {
    var valOfVar = scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm(varName)
    var typeOfVar = scalaExec.Interpreter.GlobalValues.globalInterpreter.typeOfTerm(varName)
    
    //println("var = "+varName + " has value = "+valOfVar)
  }
}
