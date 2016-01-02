# Introduction #

`This page describes useful convenience features of ScalaLab as they are added, and with the date stamp of their implementation.`

## Controlling verbose of output for matrices/vectors, digits of precision, truncation of large matrices/vectors (Sep 08 -2012) ##

`It is very convenient practically to control the output of commands. For that reason we have implemented a menu option at the Configuration menu, i.e. the "Control the format of displayed numbers and truncation of large matrices" that also controls the verbose on/off status. The preferences selected by the user are saved and are available for the next ScalaLab session.`

`Also, it is easy to access these controls programmatically through the following scalaSci object, that we list below:`

```


package scalaSci

//  parameters that determine the format with which numbers and arrays are printed
object PrintFormatParams {

    var verboseFlag = true   // controls verbosing the results of  toString()
    def setVerbose(vflag: Boolean) = { verboseFlag = vflag }
    def getVerbose() = verboseFlag 
    
    var vecDigitsPrecision = 4   // controls pecision in toString()  
    def getVecDigitsPrecision() = vecDigitsPrecision
    var vecMxElemsToDisplay = 20    // controls maximum number of Vector elements to display
    def setVecMxElemsToDisplay( mxElems: Int): Int  = { var prevElems = vecMxElemsToDisplay; vecMxElemsToDisplay = mxElems; prevElems}
    def setVecDigitsPrecision(precision: Int) = { vecDigitsPrecision = precision }
  
     var matDigitsPrecision = 4  // controls pecision in toString()  
     def getMatDigitsPrecision() = matDigitsPrecision
     var matMxRowsToDisplay = 6  
     var matMxColsToDisplay = 6
     def getMatMxRowsToDisplay() = matMxRowsToDisplay
     def getMatMxColsToDisplay() = matMxColsToDisplay
     def setMatMxRowsToDisplay(nrows: Int) = { matMxRowsToDisplay = nrows }
     def setMatMxColsToDisplay(ncols: Int) = { matMxColsToDisplay = ncols }
     def setMatDigitsPrecision(precision: Int) = { matDigitsPrecision = precision; vecDigitsPrecision = precision }
}


```


`Additionally, the same routines are available at the REPL mode of ScalaLab operation, with commands available through the ` _`BasicCommands`_ `object. These commands are (also displayed with the command "help"): `

```

def setVerbose(vflag: Boolean)  // controls verbosing the results of  toString() 

def getVerbose() // returns the current verbosing state 

def setVecMxElemsToDisplay( mxElems: Int) : Int  // controls the number of elements displayed for vectors 

def  setVecDigitsPrecision(precision: Int) // controls the precision of vector elements 
          
def getVecDigitsPrecision() // returns the precision with which vector elements are displayed

def getMatMxRowsToDisplay() // returns the number of rows displayed for matrices
         
def getMatMxColsToDisplay()  // returns the number of cols displayed for matrices

def setMatMxRowsToDisplay(nrows: Int) // sets the number of rows displayed for matrices

def setMatMxColsToDisplay(ncols: Int)  //sets the number of cols displayed for matrices

def setMatDigitsPrecision(precision: Int) // controls the precision of Matrix elements

def getMatDigitsPrecision() // returns the precision with which Matrix elements are displayed
                

```


## Reading information from Libraries for code completion (July 27 - 2012) ##

`This feature works for the RSyntaxArea based editor.`

`From the "Edit" menu choose the RSyntaxArea based editor. From the editor's menu, use the "Completion" menu option. We can load using Java reflection information concerning Classes and Methods for various libraries, e.g. EJML, MTJ, JLAPACK, Numerical Recipes, NUMAL, Apache Commons etc.`

`Then, we can use that information with the CTRL-SPACE code completion feature of the RSyntaxArea editor.`