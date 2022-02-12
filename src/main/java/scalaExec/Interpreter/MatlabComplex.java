
package scalaExec.Interpreter;

// interface to the representation of complex numbers in MATLAB 

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MatlabComplex {
    public double [][] re;  // the real parts
    public double [][]im;  // the imaginary parts
    final int matDigitsPrecision = 16;
    final int matMxRowsToDisplay = 10;
    final int matMxColsToDisplay = 10;
    

// format the presentation of a complex matrix returned from Matlab
  @Override
  public String toString() {

      String  formatString = "0.";
    for (int k =0; k <  matDigitsPrecision; k++)   // control the digits of precision
       formatString += "0";
    
    DecimalFormat  digitFormat = new DecimalFormat(formatString);
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
  
      int rowsToDisplay = re.length; 
      int colsToDisplay = re[0].length;
      
      boolean   truncated = false;  // output for large matrices is truncated
      String  truncationIndicator = "";
    if  ( matMxRowsToDisplay < rowsToDisplay ) {
        rowsToDisplay = matMxRowsToDisplay;
        truncationIndicator = " ... ";
        truncated = true;
      }
    if  (matMxColsToDisplay < colsToDisplay) {
        colsToDisplay  = matMxColsToDisplay;
        truncationIndicator = " ... ";
       }
  
     
     int  i=0; int  j=0;
     StringBuilder sb = new StringBuilder("\n");
    while (i < rowsToDisplay) {
        j = 0;
        while (j < colsToDisplay ) {
       sb.append(digitFormat.format(this.re[i][j])+"+"+digitFormat.format(this.im[i][j])+"i ");
       j++;
       if (j < colsToDisplay)
           sb.append(", ");
       
        }
      sb.append(truncationIndicator+"\n");
     i++;
    }
  
    if (truncated)     // handle last line
    sb.append( ".........................................................................................................................................");

     return sb.toString();
      }
    
  }
