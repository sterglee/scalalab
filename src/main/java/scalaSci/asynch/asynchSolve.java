
package scalaSci.asynch;

import scalaExec.Interpreter.GlobalValues;


import java.util.concurrent.Callable;
import java.util.concurrent.Future;


// this class supports asynchronous  solving of linear systems
public class asynchSolve  implements  Callable<double []> {
   // solve  A*x = b
    double [] [] mA; 
    double [] mb;
    
    asynchSolve(double [][]A, double [] b ) {
        mA = A;  mb = b;
    }
 
         
    @Override
    public double []  call() throws Exception {
         com.nr.la.LUdcmp  luA = new com.nr.la.LUdcmp(mA);
         double [] res = new double[mb.length];
         luA.solve(mb, res);

        return res;
    }    

      public static Future<double []> asynchsolve( scalaSci.RichDouble2DArray  mA, double [] b) {
   return asynchsolve(mA.getArray(), b);
      }
    
    public static Future<double []> asynchsolve( double [][]A, double [] b) {
        asynchSolve  nsolve = new asynchSolve(A, b);
        
        Future <double []> solveResults = GlobalValues.execService.submit(nsolve);        
        return solveResults;
        
    }

    
}
