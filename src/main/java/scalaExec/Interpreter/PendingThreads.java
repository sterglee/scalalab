
package scalaExec.Interpreter;

/*pending threads are those that start by executing ScalaLab scripts in the 
 background (e.g. with the Shift-F6 keystroke)
 Background execution is useful since the user is not blocked waiting for the 
 computation that may be delayed.
 These threads are not designed for interruption, thus we can only forcefully 
 cancel them.
 However, cancelling background tasks is problematic, 
 and unpredictable behavior can be observed.
  
This class keeps the most recent threads in order to be able to cancel tasks
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class PendingThreads {
  static public final int  maxThreadsToKeep =50;
    // we keep the most recently executed maxThreadsToKeep
  static public Thread []  recentThreads = new Thread[maxThreadsToKeep];
  static public int insertCursor = 0;  // cursor that points where to insert a new thread. 
  
// add a new thread
  static public void addThread(Thread thread) {
      recentThreads[insertCursor] = thread;
      insertCursor++; // increment current insert location
      if (insertCursor == maxThreadsToKeep)
          insertCursor = 0;
  }
  
  // cancel all pending threads
  static public void cancelPendingThreads() {
      int cntThreadsCanceled = 0;
      for (int i=0; i<maxThreadsToKeep; i++)
          if (recentThreads[i] != null) {
              Thread   examinedThread = recentThreads[i];
              //System.out.println("thread["+i+"] state = "+examinedThread.getState().toString());
              if (examinedThread.getState() != Thread.State.TERMINATED) 
              {
                  examinedThread.stop();  // cancel the thread
                 cntThreadsCanceled++;
          }
              recentThreads[i] = null;
              
          }
      
      System.out.println("\nCancelled "+ cntThreadsCanceled+"  threads");
  }
  
}
