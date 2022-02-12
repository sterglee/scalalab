package scalaSci;

import edu.emory.mathcs.utils.ConcurrencyUtils;
import java.util.concurrent.Future;
import scalaExec.Interpreter.GlobalValues;


// provides multithreaded multiplication for 2-D Java arrays
public class ParallelMult {

    

    public static double [][]  pmul( final double [][] v1, final  double [][] v2)  {
            // return new Matrix(LinearAlgebra.times( getRef(),  v2.getRef()), false );
        final int    rN = v1.length;   final int  rM = v1[0].length;
        int    sN = v2.length;  int   sM = v2[0].length;
        

      
    // transpose first matrix that. This operation is very important in order to exploit cache locality
final double [][]  thatTrans = new double[sM][sN];
for (int  r=0; r < sN; r++)
    for (int c = 0; c  < sM; c++) 
       thatTrans[c][r] = v2[r][c];
    
  final double [][]   vr = new double[rN][sM];   // for computing the return Matrix
  int  nthreads = GlobalValues.numOfThreads;
  nthreads = Math.min(nthreads, rN);  // larger number of threads than the number of cores of the system deteriorate performance
  
  
  Future<?>[] futures = new Future[nthreads];
            
  int   rowsPerThread = (int)(sM / nthreads)+1;  // how many rows the thread processes

  int threadId = 0;  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
    final int  firstRow = threadId * rowsPerThread;
    final int  lastRow =   threadId == nthreads-1? sM: firstRow+rowsPerThread;
    
    
 futures[threadId] = GlobalValues.execService.submit(new Runnable() {
    public void run()  {
      int  a = firstRow;   // the first row of the matrix that this thread processes
      while (a < lastRow) {  // the last row of the matrix that this thread processes
             int  b = 0;
             while (b < rN )  {
                 double  s = 0.0;
                 int  c = 0;
                 while (c < rM) {
                    s += v1[b][c] * thatTrans[a][c];
                    c++;
                   }
                vr[b][a]   = s;
                b++;
             }
             a++;
      }
   }
 });
        threadId++;
        
  }  // for all threads

   // wait for all the multiplication worker threads to complete
  ConcurrencyUtils.waitForCompletion(futures);
  
  return vr;
	
    
    }
}
