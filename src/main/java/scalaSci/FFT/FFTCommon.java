package scalaSci.FFT;

import org.apache.commons.math3.complex.Complex;

public class FFTCommon {

    static public double SQR(double x)  { return x*x; }
    
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
    // returns an array consisting of the real partas of the complex array sig
  static public double [] getReParts(Complex [] sig)  {
      int N = sig.length;
      double []  realParts = new double[N];
      for (int k=0; k<N; k++)
          realParts[k] = sig[k].getReal();
      return realParts;
  } 
    
  // returns an array consisting of the imaginary partas of the complex array sig
  static public double [] getImParts(Complex [] sig) {
      int N = sig.length;
      double []  imParts = new double[N];
      for (int k=0; k<N; k++)
          imParts[k] = sig[k].getImaginary();
      return imParts;
  }     
  
  static public double [] threeDMatrixToVector(double [][][]matr) {
      int nn1 = matr.length;
      int nn2 = matr[0].length;
      int nn3 = matr[0][0].length;
      int totalLength = nn1*nn2*nn3;
      double []  v = new double[totalLength];
      int cnt=0;
      for (int n1=0; n1<nn1; n1++)
          for (int n2=0; n2<nn2; n2++)
              for (int n3=0; n3<nn3; n3++)
                  v[cnt++] = matr[n1][n2][n3];
      return v;
  }
}
