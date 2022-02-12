
package scalaSci.math.array;

// class that provides fast Matrix operations for double[][] arrays in Java

public class FMJ {

    public static double [][]alloc(int m, int n) {
           return new double[m][n];
    }

public static double mul(double [] A, double []B) {
    double sm=0.0;
    int N = A.length;
    for (int k=0; k<N; k++)
        sm += A[k]*B[k];
    return sm;
    
}
  public static double [][]  mul( double [][] A, double [][] B) {
    int rN = A.length;  int rM = A[0].length;
    int  sN = B.length;  int  sM = B[0].length;

    System.out.println(" in mul,  rN = "+rN+" rM = "+rM);
    if (rM != sN) {
        System.out.println("incompatible dimensions in Matrix multiplication");
        return null;
    }
   double []   v1Colj = new double[rM];
   double [][]  result = new double[rN][sM];
   for (int j=0; j < sM; j++)  {  // for all columns of matrix B
       // extract the j'th column from matrix B
       for (int k=0; k < rM; k++)
          v1Colj[k] =B[k][j];
       
 // multiply each row of A with the column
      for  (int i=0; i<rN; i++) {
        double []  Arowi = A[i];   // take the i'th row
        double   s = 0.0;
        for (int k=0; k< rM; k++)
          s += Arowi[k]*v1Colj[k];
          
      result[i][j] = s;
         }
   }
  return result;
}    

public static double [][] mul(double x, double [][]A)  {
    int N = A.length; int M = A[0].length;
    double [][] result = new double[N][M];
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            result[r][c] = x*A[r][c];
    return result;
}

// multiply with a number in place
public static double[][] mulIn(double x, double [][]A) {
    int N = A.length; int M = A[0].length;
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            A[r][c] *=  x;
    return A;
}


public static double [][] add(double x, double [][]A)  {
    int N = A.length; int M = A[0].length;
    double [][] result = new double[N][M];
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            result[r][c] = x+A[r][c];
    return result;
}

// add with a number in place
public static double[][] addIn(double x, double [][]A) {
    int N = A.length; int M = A[0].length;
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            A[r][c] +=  x;
    return A;
}


public static double [][] sub(double x, double [][]A)  {
    int N = A.length; int M = A[0].length;
    double [][] result = new double[N][M];
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            result[r][c] = -x+A[r][c];
    return result;
}

// subtract with a number in place
public static double[][] subIn(double x, double [][]A) {
    int N = A.length; int M = A[0].length;
    for (int r=0; r<N; r++)
        for (int c=0; c<M; c++)
            A[r][c] -=  x;
    return A;
}


public static double [][]  add( double [][] A, double [][] B) {
    int  rN = A.length;  int  rM = A[0].length;
    int  sN = B.length;  int  sM = B[0].length;

    if (rN != sN || rM != sM)   { // incompatible dimensions
        System.out.println("incompatible dimensions for Matrix addition");
        return null;
    }
   double [][] result = new double[rN][rM];
   for (int r=0; r<rN; r++)
       for (int c=0; c<rM; c++)
           result[r][c] = A[r][c]+B[r][c];

  return result;
}


public static double [][]  sub( double [][] A, double [][] B) {
    int  rN = A.length;  int  rM = A[0].length;
    int  sN = B.length;  int  sM = B[0].length;

    if (rN != sN && rM != sM)   { // incompatible dimensions
        System.out.println("incompatible dimensions for Matrix addition");
        return null;
    }
   double [][] result = new double[rN][rM];
   for (int r=0; r<rN; r++)
       for (int c=0; c<rM; c++)
           result[r][c] = A[r][c]-B[r][c];

   return result;
 }



  public static double[][] randj(int n, int m) {
      double [][] v =new double[n][];
 for (int i=0; i<n; i++)
     v[i] = new double[m];
      
      for (int  i=0; i<n; i++)
       for (int j=0; j<m; j++)
              v[i][j] = java.lang.Math.random();

      System.out.println("returning from randj");
         return v;
    }
}
