package scalaExec.Functions.Chaotic;
// for integration of the Chua's circuit chaotic differential equations using ODE solving code from the NUMAL book
import  numal.*; 

public class ChuaMultiStep extends Object 
             implements AP_multistep_methods 
 
 { 
 static  final   double  c1, c3;
 static  final   double  m0, m1;

 static {
        c1 = 15;  c3 = 25.58;
        m1=-5.0/7.0;  m0=-8.0/7.0;
}
 
 // an example template implementing the Lorenz attractor 
public void deriv(double df[], int n, double x[], double y[])  { 
double xx,yy,zz; 

    xx=y[1];     yy=y[2];     zz=y[3]; 

    df[1] =  c1*(yy - xx - f(xx));  
    df[2] = xx-yy+zz;  
    df[3] =  -c3*yy; 
  } 
 

     public boolean available(int n, double x[], double y[], double jac[][])  { 
       jac[1][1] = -c1;     jac[1][2] = c1;  jac[1][3] = m1; 
       jac[2][1] = 1;   jac[2][2] = -1;  jac[2][3] = 1; 
       jac[3][1] = 0;    jac[3][2] = -c3;  jac[3][3] = 0; 

        return true; 
     } 

    
public void out(double t, int n,  int m, double te[], double y[])  {  } 

 

private double f(double x) {
 if (x <= -1.0)
    return m1*x +m1-m0;
 else if (x <= 1.0)
    return m0*x;
 else
   return m1*x+m0-m1;
}


}


 
