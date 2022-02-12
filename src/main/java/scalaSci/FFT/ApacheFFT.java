package scalaSci.FFT;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.DftNormalization;
        
import scalaSci.Vec;

public class ApacheFFT  {
    private double[] signal;

        /*
    var f1=9; var f2 = 0.3; var t = inc(0, 0.01, 10);  var x = sin(f1*t)+4*cos(f2*t); subplot(2,1,1); plot(t,x);
    var fx = fft(x); 
    var fxre = getReParts(fx)
    subplot(2,1,2); plot(fxre)
     */
	
   static  public Complex [] fft(double [] sig)  {
       int N = sig.length;
       int M = (int)(Math.log(N)/Math.log(2.0));
       int NpowerTwo = (int) Math.pow(2, M);
       if (NpowerTwo != N)  {   // padd the signal with zeros in order to be a power of 2
           NpowerTwo = (int) Math.pow(2, M+1);
           double [] paddSig = new double[NpowerTwo];
           for (int k=0; k<N; k++)
               paddSig[k] = sig[k];
           for (int k=N; k<NpowerTwo; k++)   
               paddSig[k] = 0.0;
           sig = paddSig;
       }
        Complex []  result;
        
        FastFourierTransformer fftj = new FastFourierTransformer(DftNormalization.STANDARD);
        result =   fftj.transform(sig, TransformType.FORWARD);
        
        return result;
        }            
        
  
  static public double [] absfft(double [] sig) {
      Complex  [] fftSig = fft(sig);
      int N = fftSig.length;
      double [] absSig = new double[N];
      for (int k=0; k<N; k++)
          absSig[k] = fftSig[k].abs();
      return absSig;
  }
  
  static public double [] realsfft(double [] sig) {
      Complex  [] fftSig = fft(sig);
      int N = fftSig.length;
      double [] realsSig = new double[N];
      for (int k=0; k<N; k++)
          realsSig[k] = fftSig[k].getReal();
      return realsSig;
  }
  
  static public double [] imagsfft(double [] sig) {
      Complex  [] fftSig = fft(sig);
      int N = fftSig.length;
      double [] imagsSig = new double[N];
      for (int k=0; k<N; k++)
          imagsSig[k] = fftSig[k].getImaginary();
      return imagsSig;
  }
  
  static public double [] absfft(Vec vsig) {
      return absfft(vsig.getv());
  } 
  
  static public double [] realsfft(Vec vsig) {
      return realsfft(vsig.getv());
  } 
  
  static public double [] imagsfft(Vec vsig) {
      return imagsfft(vsig.getv());
  } 
  
  static  public Complex [] ifft(Complex [] sig)  {
        
        FastFourierTransformer fftj = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex [] result =   fftj.transform(sig, TransformType.INVERSE);
        
        return result;
        }            
   
   static  public Complex []  fft(Vec  sigVec)  {
     return fft(sigVec.getv());    
   }            
  
  
  //static public powsp()
}
