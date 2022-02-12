package scalaExec.Functions.Chaotic;

import  numal.*; 

public class LorenzRKE extends Object 
             implements AP_rke_methods 
 
 { 
public void der(int n, double t, double y[]) 
{ 
  double xx,yy,zz; 

    xx=y[1];     yy=y[2];     zz=y[3]; 

    y[1] = 10*(yy-xx);  
    y[2] = -xx*zz+143*xx - yy;  
    y[3] = xx*yy - 2.66667*zz;  
  } 
 

public void out(int n, double t[], double te[], double y[], double data[])  {  } 
}
