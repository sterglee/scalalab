package scalaSci.math.array;

import scalaSci.Vec;

public class BasicDSPSimple  {
	private double[] signal;

        /*
         var f1=9; var f2 = 0.3; var t = inc(0, 0.01, 10);  var x = sin(f1*t)+4*cos(f2*t); subplot(2,1,1); plot(t,x);
         var fx = fft(x); subplot(2,1,2); plot(fx);

         */
	
   static  public double [] fft(double [] sig)  {
       int N = sig.length;
       int M = (int)(Math.log(N)/Math.log(2.0));
       int NpowerTwo = (int) Math.pow(2, M);
       if (NpowerTwo != N)  {   // truncate the signal to a power of 2
           double [] truncSig = new double[NpowerTwo];
           for (int k=0; k<NpowerTwo; k++)   
               truncSig[k] = sig[k];
           sig = truncSig;
       }
        double []  result;
        result =  fftMag(sig);
        
        return result;
        }            
        
   
   static  public Vec  fft(Vec  sigVec)  {
       double [] sig = sigVec.getv();
       int N = sig.length;
       int M = (int)(Math.log(N)/Math.log(2.0));
       int NpowerTwo = (int) Math.pow(2, M);
       if (NpowerTwo != N)  {   // truncate the signal to a power of 2
           double [] truncSig = new double[NpowerTwo];
           for (int k=0; k<NpowerTwo; k++)   
               truncSig[k] = sig[k];
           sig = truncSig;
       }
       double []  result;
         result = fftMag(sig);

         return new Vec(result);
        }            
   
    private static  int n, nu;

    private static  int bitrev(int j) {

        int j2;
        int j1 = j;
        int k = 0;
        for (int i = 1; i <= nu; i++) {
            j2 = j1/2;
            k  = 2*k + j1 - 2*j2;
            j1 = j2;
        }
        return k;
    }

    static public  double[] fftMag(double[] x) {
        // assume n is a power of 2
        int n = x.length;
        int nu = (int)(Math.log(n)/Math.log(2));
        int n2 = n/2;
        int nu1 = nu - 1;
        double[] xre = new double[n];
        double[] xim = new double[n];
        double[] mag = new double[n2];
        double tr, ti, p, arg, c, s;
        for (int i = 0; i < n; i++) {
            xre[i] = x[i];
            xim[i] = 0.0;
        }
        int k = 0;

        for (int l = 1; l <= nu; l++) {
            while (k < n) {
                for (int i = 1; i <= n2; i++) {
                    p = bitrev (k >> nu1);
                    arg = 2 * (double) Math.PI * p / n;
                    c = (double) Math.cos (arg);
                    s = (double) Math.sin (arg);
                    tr = xre[k+n2]*c + xim[k+n2]*s;
                    ti = xim[k+n2]*c - xre[k+n2]*s;
                    xre[k+n2] = xre[k] - tr;
                    xim[k+n2] = xim[k] - ti;
                    xre[k] += tr;
                    xim[k] += ti;
                    k++;
                }
                k += n2;
            }
            k = 0;
            nu1--;
            n2 = n2/2;
        }
        k = 0;
        int r;
        while (k < n) {
            r = bitrev (k);
            if (r > k) {
                tr = xre[k];
                ti = xim[k];
                xre[k] = xre[r];
                xim[k] = xim[r];
                xre[r] = tr;
                xim[r] = ti;
            }
            k++;
        }

        mag[0] = (double) (Math.sqrt(xre[0]*xre[0] + xim[0]*xim[0]))/n;
        for (int i = 1; i < n/2; i++)
            mag[i]= 2 * (double) (Math.sqrt(xre[i]*xre[i] + xim[i]*xim[i]))/n;
        return mag;
    }
    
}
