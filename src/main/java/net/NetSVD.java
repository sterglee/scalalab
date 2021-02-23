
package net;

import scalaExec.Interpreter.GlobalValues;
import static scalaExec.Interpreter.GlobalValues.reader;
import static scalaExec.Interpreter.GlobalValues.svdCode;
import static scalaExec.Interpreter.GlobalValues.writer;
import scalaSci.SvdResults;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ScalaLabNet.readArray;
import static net.ScalaLabNet.writeArray;


// this class supports remote SVD computation using socket based interprocess communication
public class NetSVD  implements  Callable<SvdResults> {


    public static SvdResults  svdresults;
    double [] [] xa;   // the data array for which SVD computation is performed
    
    NetSVD(double [][]x ) {
        xa = x;
    }
    
// compute eigenvalues/eigenvectors 
 // reads the matrix from the reader stream and outputs the results to the writer stream
  // this code is called at the server side  
 public static  void  serverCompSVD(DataInputStream   reader,  DataOutputStream  writer)   {
          
        try {
            // read size of the matrix
            int  Nrows = reader.readInt();  // #rows of the matrix
            int   Ncols = reader.readInt();   // #cols of the matrix
            // System.out.println("Nrows ="+  Nrows + "Ncols = "+Ncols);
            
            double [][]  da =  new double[Nrows][Ncols];  // allocate array for matrix data
            
            readArray( reader, da, Nrows, Ncols);   // read the array from the client
         //   System.out.println("data array for eigenvalue computation readed");
            
            // perform the SVD decomposition using the Numerical Recipes method
            com.nr.la.SVD   svdOfda = new com.nr.la.SVD(da);
            
            //  the results of the SVD computation
            double [][] u = svdOfda.u;
            double [][] v = svdOfda.v;
            double [] w = svdOfda.w;
            
            // write the computed eigenvalues to client
            // System.out.println(" writing SVD");
            writeArray( writer, u, Nrows, Ncols);
            writeArray( writer, v, Nrows, Ncols);
            writeArray( writer, w, Nrows);
            
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ScalaLabNet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
  } 
  
 
         
    @Override
    public SvdResults call() throws Exception {
     
 //   a client side call to compute the SVD on the server
 //   it is non-blocking call, i.e. netSVD() returns immediately.
 //   Blocking of course happens when the results are required 
 //   but are not ready yet.
 
        svdresults = new SvdResults();
            
        try {
            int  nrows = xa.length;
            int  ncols = xa[0].length;
            
            writer.writeInt(svdCode);    //  code for the server to compute the SVD computation
            
            writer.writeInt(nrows);    // #rows of the matrix
            writer.writeInt(ncols);   //  #cols of the matrix
            
// write the matrix to the server
            writeArray( writer, xa,  nrows, ncols);
            
// retrieve the results
            svdresults.U = new double[nrows][ncols];
            svdresults.V = new double[ncols][ncols];
            svdresults.W = new double[ncols];
            
            readArray( reader, svdresults.U, nrows, ncols);
            readArray( reader, svdresults.V, ncols, ncols);
            readArray( reader, svdresults.W, ncols);
            
        } catch (IOException ex) {
            Logger.getLogger(ScalaLabNet.class.getName()).log(Level.SEVERE, null, ex);
        }
   return svdresults;
        
    }
    
    public static Future<SvdResults> netSVD( double [][]x) {
        NetSVD  nsvd = new NetSVD(x);
        
        Future <SvdResults> svdresults = GlobalValues.execService.submit(nsvd);
        return svdresults;
        
    }

    
}
