package scalaExec.Functions.Chaotic;

// for integration of the Chua's circuit chaotic differential equations using ODE solving code from the NUMAL book
import  numal.*; 

public class ChuaDiffSys extends Object 
             implements AP_diffsys_methods 
{
 static  final   double  c1, c3;
 static  final   double  m0, m1;

 static {
        c1 = 15;  c3 = 25.58;
        m1=-5.0/7.0;  m0=-8.0/7.0;
}
    
// Chua's circuit
public void derivative(int n, double x, double y[], double dy[])  { 
  double xx,yy,zz; 

    xx=y[1];     yy=y[2];     zz=y[3]; 

    dy[1] = c1*(yy - xx - f(xx));
    dy[2] = xx-yy+zz;  
    dy[3] = -c3*yy;
  } 
 

private double f(double x) {
 if (x <= -1.0)
    return m1*x +m1-m0;
 else if (x <= 1.0)
    return m0*x;
 else
   return m1*x+m0-m1;
}


 public void output(int n, double x[],  double xe, double y[], double dy[])  { 
return; 
  } 
}
