// scalaSci routines for operating on Matlab's .mat files
package scalaSci.math.io;

import scalaExec.Interpreter.GlobalValues;
import com.jmatio.types.MLDouble;
import java.util.ArrayList;
import com.jmatio.io.*;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLCell;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import scala.reflect.io.Directory;


public class MatIO {

    // writes to the Matlab .mat file the contents of the variable variableNameToSave of type double [] of the scalaSci workspace
 public static  boolean  save(String fileName, double []  varValues, String varName) {
        
      fileName=fileName.trim();
      String matFileName = fileName;
      if (fileName.endsWith(".mat")==false)
          matFileName = matFileName+".mat";  //  append the default extension
      boolean absoluteFileName = false;
      if (scalaExec.Interpreter.GlobalValues.hostIsUnix)
      {
          if  (fileName.startsWith("/"))
              absoluteFileName = true;
      }
      else  // Windows host
          if (fileName.charAt(1)==':')
              absoluteFileName = true;
    
    if (absoluteFileName == false)  // save to the current working directory
        matFileName = Directory.Current().get().path()+File.separator+matFileName;

      //  varName - array name, valrValues - One-dimensional array of doubles, packed by columns (ala Fortran).
      int m = 1;  // Number of rows
      MLDouble  mlDouble = new MLDouble(varName, varValues, m);
      ArrayList<MLArray>  list = new ArrayList<MLArray>();
      list.add(mlDouble);
        try {
            MatFileWriter mfw = new MatFileWriter(matFileName, list);
        } catch (IOException ex) {
            Logger.getLogger(MatIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean success= false;
      
      return success;
      }
    
 // writes to the Matlab .mat file the contents of the variable variableNameToSave of type double [][] of the scalaSci workspace
 public static  boolean  save(String fileName, double [][]  varValues, String varName) {
        
      fileName=fileName.trim();
      String matFileName = fileName;
      if (fileName.endsWith(".mat")==false)
          matFileName = matFileName+".mat";  //  append the default extension
      boolean absoluteFileName = false;
      if (scalaExec.Interpreter.GlobalValues.hostIsUnix)
      {
          if  (fileName.startsWith("/"))
              absoluteFileName = true;
      }
      else  // Windows host
          if (fileName.charAt(1)==':')
              absoluteFileName = true;
    
    if (absoluteFileName == false)  // save to the current working directory
        matFileName = Directory.Current().get().path()+File.separator+matFileName;

      //  varName - array name, valrValues - One-dimensional array of doubles, packed by columns (ala Fortran).
      int m = 1;  // Number of rows
      MLDouble  mlDouble = new MLDouble(varName, varValues);
      ArrayList<MLArray>  list = new ArrayList<MLArray>();
      list.add(mlDouble);
        try {
            MatFileWriter mfw = new MatFileWriter(matFileName, list);
        } catch (IOException ex) {
            Logger.getLogger(MatIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean success= false;
      
      return success;
      }
    
    // writes to the Matlab .mat file the contents of the variable varName of type Vec  of the scalaSci workspace
  public static  boolean  save(String fileName, scalaSci.Vec  vecValues, String varName) {
        double [] varValues = vecValues.getv();
        return save(fileName, varValues, varName);
    }
      
  // save a Mat
public static  boolean  save(String fileName, scalaSci.Mat  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }
  
// save a Matrix
public static  boolean  save(String fileName, scalaSci.Matrix  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }
     
  
// save an EJML.Mat
public static  boolean  save(String fileName, scalaSci.EJML.Mat  matValue, String varName) {
        double [] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }   


// save an MTJ.Mat
public static  boolean  save(String fileName, scalaSci.MTJ.Mat  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }   
         

// save an CommonMaths.Mat
public static  boolean  save(String fileName, scalaSci.CommonMaths.Mat  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }   



// save a JBLAS .Mat
public static  boolean  save(String fileName, scalaSci.JBLAS.Mat  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }   
     
// save a RichDouble2DArray 
public static  boolean  save(String fileName, scalaSci.RichDouble2DArray  matValue, String varName) {
        double [][] varValues = matValue.getv();
        return save(fileName, varValues, varName);
    }   



    // loads the Matlab .mat file contents to scalaSci workspace
    public static  int  load(String fileName) {
        fileName=fileName.trim();
      String matFileName = fileName;
      if (fileName.endsWith(".mat")==false)
          matFileName = matFileName+".mat";  //  append the default extension
      boolean absoluteFileName = false;
      if (scalaExec.Interpreter.GlobalValues.hostIsUnix)
      {
          if  (fileName.startsWith("/"))
              absoluteFileName = true;
      }
      else {
          if (fileName.length()> 1)
                if (fileName.charAt(1)==':')
              absoluteFileName = true;
      }
    if (absoluteFileName == false)   // construct the path by appending the current directory
        matFileName = Directory.Current().get().path()+File.separator+matFileName;
      
      int numVarsReaded = 0;  

    try {
        //read in the file
  MatFileReader mfr = new MatFileReader( matFileName );
  //  a map of MLArray objects that were inside MAT-file. MLArrays are mapped with MLArrays' names
  Map  matFileVars = mfr.getContent();  // a map of MLArray objects  that were inside the MAT-file, mapped with their names
  
  Set varsSet = matFileVars.keySet();  // a set view of the keys contained in the map
  Iterator <String> varsIter = varsSet.iterator();
  boolean isRoot = true;
  while (varsIter.hasNext())  {  // for all .mat file variables
        String currentVariable = varsIter.next();  // get the String of the Matlab variable
            //  the value to which the read file maps the specified array name. Returns null if the file contains no content for this name.
            if ((mfr.getMLArray(currentVariable) instanceof  MLCell) == true) {
               MLCell mycellvar = (MLCell)mfr.getMLArray(currentVariable);
               int M = mycellvar.getM();
               int N = mycellvar.getN();
               int NMmax = M;
               if ( NMmax < N) NMmax = N;
               ArrayList<MLArray> mycells =  mycellvar.cells();
               
               scalaExec.Interpreter.GlobalValues.cellStrings = new String[NMmax];
               int cnt = 0;
               Iterator imycells = mycells.iterator();
               while (imycells.hasNext()) {
                   com.jmatio.types.MLChar  currentString = (com.jmatio.types.MLChar) imycells.next();
                   scalaExec.Interpreter.GlobalValues.cellStrings[cnt++] = currentString.getString(0);
                   imycells.next();
               }
               scalaExec.Interpreter.GlobalValues.globalInterpreter.directBind(currentVariable, "Array[String]",  scalaExec.Interpreter.GlobalValues.cellStrings);
               }
            
            else {  // variable not an MLCell
      MLArray  objArrayRetrived = mfr.getMLArray(currentVariable);
        if (objArrayRetrived instanceof  MLDouble) {
            // public MLArray getMLArray(String name)
        // Returns the value to which the read file maps the specified array name. Returns null if the file contains no content for this name.
// Returns: - the MLArray to which this file maps the specified name, or null if the file contains no content for this name.
         MLDouble mlArrayRetrieved = (MLDouble)mfr.getMLArray(currentVariable);
         String arrayName = mlArrayRetrieved.getName();
         int nrows  = mlArrayRetrieved.getM();
         int ncols = mlArrayRetrieved.getN();
         double [][] data  = mlArrayRetrieved.getArray();   // get data
         scalaExec.Interpreter.GlobalValues.data = new double[nrows][ncols];  // copy of data array
         for (int r=0; r < nrows; r++)
             for (int c=0; c< ncols; c++)
                  scalaExec.Interpreter.GlobalValues.data[r][c] = data[r][c];
         // keep a global reference in order to be accessible from the interpreter
// prepare and execute a  command in order to pass the variable to the Interpreter
                 
       if (nrows == 1 && ncols == 1)  // get as single double
        {
            double scalarData = data[0][0];
            scalaExec.Interpreter.GlobalValues.scalarData = scalarData;
            scalaExec.Interpreter.GlobalValues.scalarValuesFromMatlab.put(arrayName, scalarData); 
scalaExec.Interpreter.GlobalValues.globalInterpreter.directBind(arrayName, "Double",  scalarData);
       }
       else  {
           
           scalaExec.Interpreter.GlobalValues.arrayValuesFromMatlab.put(arrayName, data);
scalaExec.Interpreter.GlobalValues.globalInterpreter.directBind(arrayName, "Array[Array[Double]]",  data);
        }
        numVarsReaded++;  // one more variable readed
     }   // for all .mat file variables
   }
  }
  return numVarsReaded;
    
    }     
       
    catch (Exception e) 
    {
        System.out.println("Exception ");
        e.printStackTrace();
        return 0;
     }
      
    }      
  
    
}

