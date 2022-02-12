
package scalaSci.asynch;

import scalaExec.Interpreter.GlobalValues;
import scalaSci.EigResults;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


// this class supports asynchronous  eigenvalue computation 
public class asynchEig  implements  Callable<EigResults> {


    public static EigResults  eigresults;
    double [] [] xa;   // the data array for which SVD computation is performed
    
    asynchEig(double [][]x ) {
        xa = x;
    }
 
         
    @Override
    public EigResults  call() throws Exception {
       int N = xa.length;
      boolean yesvec = true;
      boolean hessenb = false;
      com.nr.eig.Unsymmeig  nrUnsymmeig = new com.nr.eig.Unsymmeig(xa, yesvec, hessenb);
      eigresults  = new EigResults(N);
            // extract the eigenvalues
       com.nr.Complex  [] eigs = nrUnsymmeig.wri;   // the computed eigenvalues
        for (int k=0; k<N; k++) {
             eigresults.realEvs[k] = eigs[k].re();  // the real part of the eigenvalue
             eigresults.imEvs[k] = eigs[k].im();  // the imaginary part
            }
            double [][] eigvecs =  nrUnsymmeig.zz;
           for (int c=0; c<N; c++) { // for all eigenvectors
              for (int r=0; r<N; r++)   // columns are eigenvectors
                 eigresults.realEvecs[c][r] = eigvecs[c][r];
            }
       
        return eigresults;
        
    }
    
    public static Future<EigResults> asyncheig( scalaSci.RichDouble2DArray  mx) {
        return asyncheig(mx.getArray());
    }
    
    public static Future<EigResults> asyncheig( double [][]x) {
        asynchEig  neig = new asynchEig(x);
        
        Future <EigResults> eigresults = GlobalValues.execService.submit(neig);        
        return eigresults;
        
    }

    
}
