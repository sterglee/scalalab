# Introduction #

`ScalaLab has an internal Java 6 compiler with which we can compile and execute Java code, even if `_`javac`_ `is not installed. Also, we can call the external ` _`javac`_ `compiler, of course if it is installed at your system. The Java code developed in ScalaLab has access to all the installed scientific libraries, either the default ones, or those that exist in toolboxes installed at the classpath. We describe by an example how we can execute Java scientific code. `


# `Example of executing Java scientific code` #

`In order to illustrate how we can execute Java code, let use some code from the NUMAL book. Click at the Toolbars the "Optimization" tab. Then choose for example the "marquardt" button. The example is displayed in html format. Copy and paste the relevant code within the ScalaInterpreterPane. We list the relevant code below:`
```
  
import java.text.DecimalFormat;
import numal.*;
  
public class Test_marquardt extends Object
implements AP_marquardt_methods {
static double x[] = new double[7];
static double y[] = new double[7];
  
public static void main(String args[]) {
  
double in[] = new double[7];
double out[] = new double[8];
double rv[] = new double[7];
double par[] = new double[4];
double jjinv[][] = new double[4][4];
Test_marquardt testmarquardt = new Test_marquardt();
DecimalFormat fiveDigit = new DecimalFormat("0.00000E0");
DecimalFormat oneDigit = new DecimalFormat("0.0");
in[0]=1.0e-6; in[3]=1.0e-4; in[4]=1.0e-1; in[5]=75.0;
in[6]=1.0e-2;
x[1] = -5.0; x[2] = -3.0; x[3] = -1.0; x[4]=1.0;
x[5]=3.0; x[6]=5.0;
y[1]=127.0; y[2]=151.0; y[3]=379.0; y[4]=421.0;
y[5]=460.0; y[6]=426.0;
par[1]=580.0; par[2] = -180.0; par[3] = -0.160;
Analytic_problems.marquardt(6,3,par,rv,jjinv,
testmarquardt,in,out);
System.out.println("Parameters:\n " +
fiveDigit.format(par[1]) + " " +
fiveDigit.format(par[2]) + " " +
fiveDigit.format(par[3]) + "\n\nOUT:\n " +
fiveDigit.format(out[7]) + "\n " +
fiveDigit.format(out[2]) + "\n " +
fiveDigit.format(out[6]) + "\n " +
fiveDigit.format(out[3]) + "\n " +
fiveDigit.format(out[4]) + "\n " +
fiveDigit.format(out[5]) + "\n " +
fiveDigit.format(out[1]) +
"\n\nLast residual vector:\n " +
oneDigit.format(rv[1]) + " " + oneDigit.format(rv[2]) +
" " + oneDigit.format(rv[3]) + " " + 
oneDigit.format(rv[4]) + " " + oneDigit.format(rv[5]) +
" " + oneDigit.format(rv[6]));
}
  
  
public boolean funct(int m, int n,double par[], double rv[])
{
int i;
  
for (i=1; i<=m; i++) {
if (par[3]*x[i] > 680.0) return false;
rv[i]=par[1]+par[2]*Math.exp(par[3]*x[i])-y[i];
}
return true;
}
public void jacobian(int m, int n, double par[],
double rv[], double jac[][])
{
int i;
double ex;
  
for (i=1; i<=m; i++) {
jac[i][1]=1.0;
jac[i][2]=ex=Math.exp(par[3]*x[i]);
jac[i][3]=x[i]*par[2]*ex;
}
}
}

```



`Now we have two options to execute the Java code: `

  1. `We can copy and paste in the ScalaInterpreterPane editor text area. Then from the ScalaLabConsole right mouse click popup menu we can select the option "Compile and execute Java text in editor" `
  1. `We can copy and paste the text in the ScalaLab editor and then press F5`