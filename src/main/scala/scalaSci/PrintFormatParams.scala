
package scalaSci

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util._

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



   def  printArray(va: Array[Double])  = {
     
   var   sb = new StringBuilder()
   var   formatString = "0."
   for (k <- 0 until scalaSci.PrintFormatParams.vecDigitsPrecision) 
       formatString += "0"
    var  digitFormat = new DecimalFormat(formatString)
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")))
    
     var  mxElems = va.length
     var  moreElems = ""
     if (mxElems > scalaSci.PrintFormatParams.vecMxElemsToDisplay )  {
          // vector has more elements than we can display
         mxElems = scalaSci.PrintFormatParams.vecMxElemsToDisplay
         moreElems = " .... "
     }
    var   i = 0
    while (i < mxElems) {
       sb.append(digitFormat.format(va(i))+"  ")
       i += 1
       }
     sb.append(moreElems+"\n")
     
 
    sb.toString
}

  
  // global routine used to display 2-d arrays with toString() that truncates presentation of rows/cols and
  // controls the digits of precision that the numbers are displayed
  def   printArray( a: Array[Array[Double]] )  =  {
    
 if  (scalaSci.PrintFormatParams.getVerbose()==true)  {
    var  formatString = "0."
    for ( k <- 0 until  matDigitsPrecision)  
       formatString += "0"
    
    var   digitFormat = new DecimalFormat(formatString)
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")))
     
    var   rowsToDisplay = a.length
    var   colsToDisplay = a(0).length;
    var   truncated = false

    var  truncationIndicator = ""
    if  ( matMxRowsToDisplay < rowsToDisplay ) {
        rowsToDisplay = matMxRowsToDisplay
        truncationIndicator = " ... "
        truncated = true
      }
     if  (matMxColsToDisplay < colsToDisplay) {
        colsToDisplay  = matMxColsToDisplay
        truncationIndicator = " ... "
      }
      var  sb = new StringBuilder("\n")

     var  crow = 0; var  ccol = 0
     while (crow < rowsToDisplay) {
        ccol = 0
        while (ccol < colsToDisplay ) {
       sb.append(digitFormat.format(a(crow)(ccol))+"  ")
       ccol += 1
        }
      sb.append(truncationIndicator+"\n")
     crow += 1
    }
   if (truncated)     // handle last line
    sb.append( ".........................................................................................................................................");

      sb.toString()
 }
 
 else  
    ""
 }

}

