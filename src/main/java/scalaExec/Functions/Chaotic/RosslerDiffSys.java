package scalaExec.Functions.Chaotic;

import  numal.*; 

public class RosslerDiffSys extends Object 
             implements AP_diffsys_methods 
 
 { 

 static  final   double  alpha = 0.1, beta = 0.1,  c=18;
  // an example template implementing the Rossler attractor 
public void derivative(int n, double x, double y[], double dy[])  { 
double xx,yy,zz; 
xx=y[1];     yy=y[2];     zz=y[3]; 

    dy[1] = -yy-zz;
    dy[2] = xx+alpha*yy;  
    dy[3] = beta+(xx-c)*zz;
   }

public void output(int n, double x[],  double xe, double y[], double dy[])  { 
return; 
  } 
}
 



