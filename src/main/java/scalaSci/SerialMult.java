package scalaSci;

public class SerialMult {

    
    // A is a M X N matrix
    // B is a N X Bn matrix
    public static double [][]  times (double [][]A, double [][] B) {
      int M =   A.length; int N = A[0].length;  // dimensions of matrix A
      int Bn = B[0].length;  // # columns of matrix B
      double[][] C =  new double[M][Bn];
      double[] Bcolj = new double[N];  // used to extract a column from B for better locality of access
         
      for (int j = 0; j < Bn; j++) {  // for all columns of B

          for (int k = 0; k < N; k++) {  // extract a column of B
            Bcolj[k] = B[k][j];
         }
         
         for (int i = 0; i < M; i++) {  // for all rows of A 
            double[] Arowi = A[i];   // get current row of A
            
            // form dot product of current row i  from A with current column j from B
            double s = 0.0;
            for (int k = 0; k < N; k++) {   // multiply current row of A with extracted column from B
               s += Arowi[k]*Bcolj[k];
            }
          
            C[i][j] = s;
         }
      }
      return C;
    }
      
      
       
}
