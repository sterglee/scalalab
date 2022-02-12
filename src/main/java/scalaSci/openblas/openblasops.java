package scalaSci.openblas;

import static org.bytedeco.javacpp.openblas.*;

public class openblasops {
 public static double test() {

   int n=100;
   double [] x  = new double[n];
   for (int k=0; k<n; k++) x[k]=1.0;
   
   
   double  r = cblas_dnrm2(n, x, 1);
   return r;
   }
   
   }
   
   