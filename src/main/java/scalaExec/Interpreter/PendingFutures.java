
package scalaExec.Interpreter;

// keeps the most recent futures in order to be able to cancel tasks
// currently ScalaLab uses the PendingThreads array but not PendingFutures

import java.util.concurrent.Future;


public class PendingFutures {
  static public final int  maxFuturesToKeep =50;
  static public Future<?> [] recentFutures = new Future<?>[maxFuturesToKeep];
  static public int insertCursor = 0;  // cursor that points where to insert a new future. 
  
  // add a new future 
  static public void addFuture(Future <?> future) {
      recentFutures[insertCursor] = future;
      insertCursor++; // increment current insert location
      if (insertCursor == maxFuturesToKeep)
          insertCursor = 0;
  }
  
  // cancel all pending futures
  static public void cancelPendingFutures() {
      int cntFuturesCanceled = 0;
      for (int i=0; i<maxFuturesToKeep; i++)
          if (recentFutures[i] != null) {
              (recentFutures[i]).cancel(false);  // cancel the future
              recentFutures[i] = null;
              cntFuturesCanceled++;
          }
      
  //    System.out.println("\nCancelled "+ cntFuturesCanceled+" futures");
  }
  
}
