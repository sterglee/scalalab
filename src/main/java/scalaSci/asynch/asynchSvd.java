
package scalaSci.asynch;

import scalaExec.Interpreter.GlobalValues;
import scalaSci.SvdResults;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


// this class supports asynchronous  SVD computation 
public class asynchSvd  implements  Callable<SvdResults> {


    public static SvdResults  svdresults;
    double [] [] xa;   // the data array for which SVD computation is performed
    
    asynchSvd(double [][]x ) {
        xa = x;
    }
 
         
    @Override
    public SvdResults call() throws Exception {
     
        svdresults = new SvdResults();

        com.nr.la.SVD  nrsvd = new com.nr.la.SVD(xa);
                
                
     // retrieve the results
            svdresults.U = nrsvd.u;
            svdresults.V = nrsvd.v;
            svdresults.W = nrsvd.w;
      
   return svdresults;
        
    }
    
    public static Future<SvdResults> asynchsvd( scalaSci.RichDouble2DArray  mx) {
       return asynchsvd(mx.getArray());
    }
    
    public static Future<SvdResults> asynchsvd( double [][]x) {
        asynchSvd  nsvd = new asynchSvd(x);
        
        Future <SvdResults> svdresults = GlobalValues.execService.submit(nsvd);
        return svdresults;
        
    }

    
}
