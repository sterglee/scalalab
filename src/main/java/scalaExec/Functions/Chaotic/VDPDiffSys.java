package scalaExec.Functions.Chaotic;

import  numal.*; 

public class VDPDiffSys extends Object 
             implements AP_diffsys_methods 
{
 static  final   double  mu;

 static {
     mu = 2;
}
    
// Vanderpole oscillator
public void derivative(int n, double x, double y[], double dy[])  { 
  double xx,yy; 

    xx=y[1];     yy=y[2];    

    dy[1] = yy;
    dy[2] = mu*(1-xx*xx)*yy - xx;  
    } 
 
 public void output(int n, double x[],  double xe, double y[], double dy[])  { 
return; 
  } 
}
