package scalaExec.Functions.Chaotic;

import  numal.*; 

public class NScrollDiffSys extends Object 
             implements AP_diffsys_methods 
 
 { 
 static  final   double  alpha, beta,  d1, ci;
 static  final   double  k, h1, p1, q1;
 
 
 static {
         alpha = 0.7;  beta = 0.7;  d1 = 0.7;  ci = 0.7;
         k =10; h1=20; p1=0;  q1=1;
 }
  // an example template implementing the Lorenz attractor 
public void derivative(int n, double x, double y[], double dy[])  { 
double xx,yy,zz; 
xx=y[1];     yy=y[2];     zz=y[3]; 

    dy[1] = yy;
    dy[2] = zz;  
    dy[3] = -alpha*xx - beta*yy - ci*zz + d1*f(xx, k, h1, (int)p1, (int)q1);
   }

private double f(double x, double k, double h, int p, int q) {
  double sum=0.0;
  for (int idx = -p; idx <=q; idx++) 
      sum +=( f0(x, k, h, idx)+f0n(x,k,h,idx));
  
  return sum;
}

private double f0(double x, double k, double h, double i) {
 if (x > i*h+1)
    return 2*k;
 else if ( Math.abs(x -i*h) <= 1)
     return k*(x-i*h)+k;
 
 return 0;
}
 

private double f0n(double x, double k, double h, double i) {
 if (x > -i*h+1)
    return 0;
 else if ( Math.abs(x +i*h) <= 1)
     return k*(x+i*h)-k;
 
 return -2*k;
}
 

 public void output(int n, double x[],  double xe, double y[], double dy[])  { 
return; 
  } 
}
 



