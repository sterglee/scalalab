
package scalaSci.asynch;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import scalaExec.Interpreter.GlobalValues;

public class asynchfft implements  Callable<Complex []> {


    public static Complex []  fftresults;
    public static double [] xa;  
    asynchfft(double []x ) {
        xa = x;
    }
 
         
    @Override
    public Complex []  call() throws Exception {
       int N = xa.length;
      
       fftresults = scalaSci.FFT.FFTScala.afft(xa);
       
        return fftresults;
        
    }
    
    public static Future<Complex [] >  asynchfft( double [] x) {
        asynchfft nfft = new asynchfft(x);
        
        Future <Complex []> fftresults = GlobalValues.execService.submit(nfft);
        return fftresults;
    }
    
    public static Future<Complex [] > asynchfft( scalaSci.Vec  vx) {
        return asynchfft(vx.getv());
    }
    

    
}
