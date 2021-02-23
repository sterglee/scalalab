
package scalaSci.asynch;

import scalaExec.Interpreter.GlobalValues;
import scalaSci.RichDouble2DArray;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


// this class supports asynchronous  matrix inverse  computation 
public class asynchInv  implements  Callable<double [][] > {


    public static double [][]  invresults;
    double [] [] xa;   // the data array for which matrix inverse  computation is performed
    
    asynchInv(double [][]x ) {
        xa = x;
    }
 
         
    @Override
    public double [][]  call() throws Exception {
       int N = xa.length;
      
       double [][]  invxa  = scalaSci.StaticMaths.inv(xa);
       return  invxa;
    }
    
    
    public static Future<double [][]> asynchinv( double [][]x) {
        asynchInv  ninv = new asynchInv(x);
        
        Future <double [][] > invresults = GlobalValues.execService.submit(ninv);        
        return invresults;
     }
    
    
    public static Future<double [][]> asynchinv( RichDouble2DArray  mx) {
        return asynchinv(mx.getArray());
    }
    

}

    

