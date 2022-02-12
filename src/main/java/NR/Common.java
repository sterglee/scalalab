package NR;

import org.apache.commons.math.complex.Complex;

public class Common {

    static public  double [][]  copy(double [] [] source) {
        int M = source.length;
        int N = source[0].length;
        double [][] dst = new double[M][N];
        for (int m=0; m<M; m++)
            for (int n=0; n<N; n++)
                dst[m][n] = source[m][n];
       return  dst;
                
    }
    
    
    static public  double []  copy(double []  source) {
        int M = source.length;
        double []dst = new double[M];
        for (int m=0; m<M; m++)
                dst[m] = source[m];
       return  dst;
                
    }

 static void fill(int [] ia, int v, int n) {
     for (int k=0; k<n; k++)
         ia[k] = v;
 }
 
 static int []  nfill( int v, int n) {
     int [] ia = new int[n];
     for (int k=0; k<n; k++)
         ia[k] = v;
     return ia;
 }
 
 static double  anorm2(double [][] a)
{
    int i,j;
    double  sum=0.0;

    int n = a.length;
    for (j = 0; j < n; j++)
        for (i=0;i < n;i++)
            sum += a[i][j]*a[i][j];
    return Math.sqrt(sum)/n;
}

    static public  void  swap(double [] data, int i, int j) 
{
    double tmp = data[i];
    data[i]=data[j];
    data[j] = tmp;
}

     static public  void  swap(double [][]  data, int i1, int j1, int i2, int j2) 
{
    double tmp = data[i1][j1];
    data[i1][j1] = data[i2][j2];
    data[i2][j2] = tmp;
}  
     
    static public  void  swap(int [] data, int i, int j) 
{
    int tmp = data[i];
    data[i]=data[j];
    data[j] = tmp;
}

     static public  void  swap(int [][]  data, int i1, int j1, int i2, int j2) 
{
    int  tmp = data[i1][j1];
    data[i1][j1] = data[i2][j2];
    data[i2][j2] = tmp;
}  
  
     static public double MAX(double x1, double x2) {
         if (x1 >= x2) return x1;
         return x2;
     }
    
     static public int MAX(int x1, int x2) {
         if (x1 >= x2) return x1;
         return x2;
     }
    
     
     static public double MIN(double x1, double x2) {
         if (x1 >= x2) return x2;
         return x1;
     }
     
     static public int  MIN(int x1, int x2) {
         if (x1 >= x2) return x2;
         return x1;
     }
     
     static public double SQR(double x) {
         return x*x;
     }
     
     static public double  SIGN(double a,  double b) {
         double rv =  (b >= 0) ? (a >= 0 ? a : -a) : (a >= 0 ? -a : a);
         return rv;
     }
     
     
}
