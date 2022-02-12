
// scalaSciCommands.WatchInterpreterState.bindingForCommonTypes
package scalaSciCommands

object WatchInterpreterState {

  import javax.swing.table.DefaultTableModel
  import javax.swing.JTable
  import javax.swing.JFrame
  import javax.swing.JPanel
  import java.awt.BorderLayout
  import javax.swing.JScrollPane
  import scalaExec.Interpreter.GlobalValues

var excludeNames = Set("append", "bindings", "checkError", "clearError", "close", "flush", "format","out", "print", "printf", "println", "setError", "write", "engine")
var dPanel = new JPanel


//   print the binding that currently the interpreter maintains for common variable types  
   def bindingForCommonTypes = {
       // the current interpreter instance
    var myI = scalaExec.Interpreter.GlobalValues.globalInterpreter

    
      println("Variables of  Common Variable Types: \n")
// get defined types
 var ictx = myI.namedDefinedTerms  // Interpreter context

var numVars = ictx.length
for (vid<-0 until numVars)  {
    var varName = ictx(vid).toString
    
    var typeVarBefore = myI.typeOfTerm(varName).toString

    var typeVar = typeVarBefore.replace("()","") 
    
      // print information  about variables of common types  
      typeVar match {
  case "Int"  => 
    var valueOfTerm = myI.valueOfTerm(varName).getOrElse("")
    println(varName+": "+"Int"+" = "+valueOfTerm)
  case "Long"  => 
    var valueOfTerm = myI.valueOfTerm(varName).getOrElse("")
    println(varName+": "+"Long"+" = "+valueOfTerm)
  case "Short"  => 
    var valueOfTerm = myI.valueOfTerm(varName).getOrElse("")
    println(varName+": "+"Short"+" = "+valueOfTerm)
  case "Double"  => 
    var valueOfTerm = myI.valueOfTerm(varName).getOrElse("")
    println(varName+": "+"Double"+" = "+valueOfTerm)
  case "Float"  => 
    var valueOfTerm = myI.valueOfTerm(varName).getOrElse("")
    println(varName+": "+"Float"+" = "+valueOfTerm)
  case  "Array[Double]" =>
    println(varName+": "+"Array[Double]")
   case "Array[Array[Double]]" =>
    println(varName+": "+"Array[Array[Double]]")
   case x if x.contains("scalaSci") =>
    println(varName+": "+typeVar)    
   case _ =>  
        }
     }
   }
   

  
  def printUserNames = {
   
  val x=scalaExec.Interpreter.GlobalValues.globalInterpreter

     // Terms with user-given names (i.e. not res0 and not synthetic)
  val userNames = x.definedTerms filter (_.startsWith("$$")==false)
  
  
  println("\n User defined names and their types: \n")   
 
userNames foreach { name =>
 {
var sname = name.toString   
        
        if (sname.startsWith("res")==false){
        if (excludeNames.contains(sname ) == false)  {
     
    var typeVarBefore = x.typeOfTerm(sname).toString

    var typeOfString = typeVarBefore.replace("()","")
    
     println(sname +" : "+ typeOfString ) 
      }
}
    }
 }

 }

   
  def displayUserNamesAndValues = {
   
     var namesVec = new java.util.Vector[String]()
    var typesVec = new java.util.Vector[String]()
   // var sizesVec = new java.util.Vector[String]()
    var valuesVec = new java.util.Vector[String]()

  val x=scalaExec.Interpreter.GlobalValues.globalInterpreter

     // Terms with user-given names (i.e. not res0 and not synthetic)
  val userNames = x.namedDefinedTerms filter (_.startsWith("$$")==false)
 
//    println("\n User defined names, their types and their values: \n")  
    var varCnt = 0
userNames foreach { name =>
 {
var sname = name.toString   
if (excludeNames.contains(sname ) == false)
     {
     var typeVarBefore = x.typeOfTerm(sname).toString
     var  typeOfString = typeVarBefore.replace("()","")

     var valueOfString = x.valueOfTerm(sname).getOrElse("").toString
     
      namesVec.add(sname)  // variable's name 
      typesVec.add(typeOfString)  // variable's type 
      valuesVec.add(valueOfString)    
      varCnt += 1
          }      
        }
        
      

   if (varCnt >= 1) {   // scalaSci variables exist in the workspace
    var columnNames = new java.util.Vector[String]()
    
  columnNames.add("Name")
  columnNames.add("Type")
  //columnNames.add("Size")
  columnNames.add("Value")

        var  model = new DefaultTableModel(columnNames, varCnt)
 
     
 for (k <- 0 until varCnt) {
      model.setValueAt(namesVec.elementAt(k), k, 0)  // variable's name
      model.setValueAt(typesVec.elementAt(k), k, 1)  // variable's type
      model.setValueAt(valuesVec.elementAt(k), k, 2)
      //model.setValueAt(sizesVec.elementAt(k), k, 2)           
     // model.setValueAt(valuesVec.elementAt(k), k, 3)
      }

          
        if (scalaExec.Interpreter.GlobalValues.watchFrame == null)  {
          GlobalValues.watchFrame = new JFrame("Workspace Variables")
          GlobalValues.watchFrame.add(dPanel)
          var locX = GlobalValues.scalalabMainFrame.getLocation
          var sizeFrame = GlobalValues.scalalabMainFrame.getSize
          GlobalValues.watchFrame.setLocation(locX.x+sizeFrame.width, locX.y)
          GlobalValues.watchFrame.setSize(300, 200);
   }
GlobalValues.tableOfVars = new JTable(model)
          
          dPanel.removeAll()
          dPanel.setLayout(new BorderLayout())
          dPanel.add(new JScrollPane(GlobalValues.tableOfVars))
                    
        
//        GlobalValues.watchFrame.setLocation(GlobalValues.rlocX, GlobalValues.rlocY)
//GlobalValues.watchFrame.setSize(GlobalValues.rWidth, GlobalValues.rHeight)
GlobalValues.watchFrame.setVisible(true)

          
          
   } // scalaSci variables exist in the workspace

        
 }

  }
  
  def printUserNamesAndValues = {
   
  val x=scalaExec.Interpreter.GlobalValues.globalInterpreter

     // Terms with user-given names (i.e. not res0 and not synthetic)
  val userNames = x.namedDefinedTerms filter (_.startsWith("$$")==false)
 
//    println("\n User defined names, their types and their values: \n")  
userNames foreach { name =>
 {
var sname = name.toString   
if (excludeNames.contains(sname ) == false)
     {
     var typeVarBefore = x.typeOfTerm(sname).toString
     var  typeOfString = typeVarBefore.replace("()","")

     var valueOfString = x.valueOfTerm(sname).getOrElse("")
     println(name.toString +" : "+ typeOfString +" = "+ valueOfString) 
          }      
        }
 }

  }
 
}
