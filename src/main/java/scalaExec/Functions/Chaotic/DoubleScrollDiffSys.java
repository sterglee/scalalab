package scalaExec.Functions.Chaotic;

import  numal.*; 

public class DoubleScrollDiffSys extends Object 
             implements AP_diffsys_methods 
 
 { 

 //static  final   double  c1 = 15.6, c2 = 1, m0 = -8/7;
 static  final   double  alpha = 0.7, beta = 0.7,  d1 = 0.7, ci = 0.7;
 static  final   double  k =10;
  // an example template implementing the Lorenz attractor 
public void derivative(int n, double x, double y[], double dy[])  { 
double xx,yy,zz; 
xx=y[1];     yy=y[2];     zz=y[3]; 

    dy[1] = yy;
    dy[2] = zz;  
    dy[3] = -alpha*xx - beta*yy - ci*zz + d1*f0(xx, k);
   }

private double f0(double x, double k) {
 if (x > 1.0)
    return k;
 else if (x >= -1.0)
    return k*x;
 else
   return -k;
}


 public void output(int n, double x[],  double xe, double y[], double dy[])  { 
return; 
  } 
}
 



