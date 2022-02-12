package scalaExec.Functions.Chaotic;

import  numal.*; 

public class ChuaRKE extends Object 
             implements AP_rke_methods 
{
 static  final   double  c1, c3;
 static  final   double  m0, m1;

 static {
        c1 = 15;  c3 = 25.58;
        m1=-5.0/7.0;  m0=-8.0/7.0;
}
    
 public void der(int n, double t, double y[]) 
{ 
  double xx,yy,zz; 

    xx=y[1];     yy=y[2];     zz=y[3]; 

    y[1] =  c1*(yy - xx - f(xx));  
    y[2] = xx-yy+zz;  
    y[3] =  -c3*yy; 
  } 
 

public void out(int n, double t[], double te[], double y[], double data[])  {  } 

 

private double f(double x) {
 if (x <= -1.0)
    return m1*x +m1-m0;
 else if (x <= 1.0)
    return m0*x;
 else
   return m1*x+m0-m1;
}


}
