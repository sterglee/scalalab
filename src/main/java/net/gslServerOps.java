package net;

// operations using a remote GSL server

import scalaSci.RichDouble2DArray;
import java.io.IOException;


public class gslServerOps {
    
    final static double eigOperationCode = 1.0;
    
  public static double [][] gslServerEig(RichDouble2DArray  A) throws IOException {
     return gslServerEig(A.getArray());
     }
    
  public static double [][] gslServerEig(double [][] A) throws IOException {
        
scalaExec.Interpreter.GlobalValues.writer.writeDouble(eigOperationCode);

// write #rows and #cols of the matrix
int Nrows = A.length;
double [][] evals = new double[2][Nrows];

scalaExec.Interpreter.GlobalValues.writer.writeDouble(Nrows*1.0);

// write matrix to the GSL server
for (int r=0; r < Nrows; r++) 
   for (int c=0; c < Nrows; c++) 
   scalaExec.Interpreter.GlobalValues.writer.writeDouble(A[r][c]);

  
// read evaluation results from GSL server
for (int r=0; r < 2; r++)
 for (int c=0; c < Nrows; c++)
  evals[r][c] =  scalaExec.Interpreter.GlobalValues.reader.readDouble();

  return evals;
        
    }
    
}
