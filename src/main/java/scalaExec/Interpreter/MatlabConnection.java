

package scalaExec.Interpreter;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

// the MatlabConnection class allows to execute MATLAB scripts within the ScalaLab environment,
// i.e. connects the Scala Interpreter to the internal JVM of MATLAB

public class MatlabConnection {

// initializes a MATLAB instance
    static public void initMatlabConnection() {
        if (GlobalValues.matlabInitedFlag == false) {
        GlobalValues.factory = new MatlabProxyFactory();
        try {
            GlobalValues.proxy = GlobalValues.factory.getProxy();
        } catch (MatlabConnectionException ex) {
            System.out.println("MATLAB Invocation exception: "+ex.getMessage());
        }
        
        GlobalValues.matlabInitedFlag = true;
        }
        
    }

    
// a new MATLAB connection instance
    static public void newMatlabConnection() {
        GlobalValues.factory = new MatlabProxyFactory();
        try {
            GlobalValues.proxy = GlobalValues.factory.getProxy();
        } catch (MatlabConnectionException ex) {
            System.out.println("MATLAB Invocation exception: "+ex.getMessage());
        }
    
        GlobalValues.matlabInitedFlag = true;
        }
        
    

    // evaluate a MATLAB script that has no input from ScalaLab and it produces no output also,
    // i.e. is like executing the corresponding command at the MATLAB  command window,
    static public void meval(String expr)  {
        try {
            GlobalValues.proxy.eval(expr);
        } catch (MatlabInvocationException ex) {
            System.out.println("MATLAB Invocation exception: "+ex.getMessage());
        }
    }
    
    // "general" type evaluation. Each input and output parameter can be either real (i.e. double [][])  or complex (i.e. MatlabComplex [][])
    // evaluate a MATLAB script that has input the vInNames list of ScalaLab's variables and it computes 
    // the vOutNames variables. 
    static public void meval(String expr, String [] vInNames,  String [] vOutNames) {
      MatlabTypeConverter  processor = new MatlabTypeConverter(GlobalValues.proxy);
       
     // prepare the MATLAB workspace by transferring
     //  the variables of the vInNames list,  from ScalaLab to MATLAB's workspace 
     if (vInNames != null) {  // list of variables to transfer from ScalaLab to MATLAB are specified
       int numInVars = vInNames.length;
      for (int v = 0; v<numInVars; v++) {   // for all ScalaLab's variables that we want to feed to MATLAB
         String vname = vInNames[v];
      
         Object cvar =  scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm(vname).get();  // currently examined input variable
         if (cvar instanceof  MatlabComplex) {  // MatlabComplex array
           MatlabComplex xin = (MatlabComplex ) cvar;
          try {      
              processor.setNumericArray(vInNames[v], new MatlabNumericArray(xin.re, xin.im));
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception in getting parameters of type MatlabComplex: "+ex.getMessage());
          }
        }  //  MatlabComplex  array
         else if (cvar instanceof  double [][])  {  // double [][] array
           double [][] xin = (double [][]) cvar;
          try {      
              processor.setNumericArray(vInNames[v], new MatlabNumericArray(xin, null));
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception in getting parameters of type double [][] from ScalaLab: "+ex.getMessage());
          }  
         }   // double [][] array
         else    if (cvar instanceof  scalaSci.scalaSciMatrix)  {  // scalaSci.scalaSciMatrix
           double [][] xin = ((scalaSci.scalaSciMatrix) cvar).toDoubleArray();
          try {      
              processor.setNumericArray(vInNames[v], new MatlabNumericArray(xin, null));
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception in getting parameters of type double [][] from ScalaLab: "+ex.getMessage());
          }  
         }   // scalaSci.scalaSciMatrix
     
         else    if (cvar instanceof  double [])  {  // double [] array
           double [] xin1 = ((double [] ) cvar);
           double [][] xin = new double[1][1];
           xin[0] = xin1;
                   try {      
              processor.setNumericArray(vInNames[v], new MatlabNumericArray(xin, null));
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception in getting parameters of type double [][] from ScalaLab: "+ex.getMessage());
          }  
         }   // double [] array
         else    if (cvar instanceof  scalaSci.Vec ) {  // scalaSci.Vec
           double [] xin1 = ((scalaSci.Vec) cvar).getv();
           double [][] xin = new double[1][1];
           xin[0] = xin1;
                   try {      
              processor.setNumericArray(vInNames[v], new MatlabNumericArray(xin, null));
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception in getting parameters of type double [][] from ScalaLab: "+ex.getMessage());
          }  
         }   // // scalaSci.Vec
     
      
      }    // for all ScalaLab's variables
     } // list of variables to transfer from ScalaLab to MATLAB are specified
     
     // MATLAB can now perform our computation
      meval(expr);   // evaluate the script text with MATLAB
    
      
      // transfer from MATLAB's workspace to ScalaLab to get the results
      if (vOutNames != null)  {   // list of variables to transfer from MATLAB to ScalaLab
      int numOutVars = vOutNames.length;
     for (int v = 0; v<numOutVars; v++) {  
          try {      
         String vname = vOutNames[v];
         // get the computed MATLAB matrix as Java2D array
                if (processor.getNumericArray(vname).isReal()) {
          double [][]xoutRe = processor.getNumericArray(vname).getRealArray2D();  
          scalaExec.Interpreter.scalaBind.bind(vname, xoutRe);
                }
                else  // complex MATLAB matrix: retrieve the result using an array of  MatlabComplex objects
                    { 
                double [][]xoutRe = processor.getNumericArray(vname).getRealArray2D();   
                double [][]xoutIm = processor.getNumericArray(vname).getImaginaryArray2D();
                MatlabComplex mtcmplx = new MatlabComplex();
                mtcmplx.re = xoutRe;
                mtcmplx.im = xoutIm;
                scalaExec.Interpreter.scalaBind.bind(vname, (MatlabComplex) mtcmplx);

            }
                
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception at returning results to ScalaLab: "+ex.getMessage());
          }
        }
      }
    }
    
     
// evaluate a MATLAB script that has input the vInNames list of ScalaLab's variables and it computes 
    // the vOutNames variables. All variables are double [][] 
    static public void meval(String expr, String [] vOutNames) {
      MatlabTypeConverter  processor = new MatlabTypeConverter(GlobalValues.proxy);
             
      meval(expr);   // evaluate the script text with MATLAB
    
      // transfer from MATLAB's workspace to ScalaLab
      if (vOutNames != null)  {   // list of variables to transfer from MATLAB to ScalaLab
      int numOutVars = vOutNames.length;
     for (int v = 0; v<numOutVars; v++) {  
          try {      
         String vname = vOutNames[v];
         // get the computed MATLAB matrix as Java2D array
                if (processor.getNumericArray(vname).isReal()) {
          double [][]xoutRe = processor.getNumericArray(vname).getRealArray2D();  
           scalaExec.Interpreter.scalaBind.bind(vname, xoutRe);

                }
                else  // imaginary
                    { 
                double [][]xoutRe = processor.getNumericArray(vname).getRealArray2D();   
                double [][]xoutIm = processor.getNumericArray(vname).getImaginaryArray2D();
                MatlabComplex mtcmplx = new MatlabComplex();
                mtcmplx.re = xoutRe;
                mtcmplx.im = xoutIm;
                
                scalaExec.Interpreter.scalaBind.bind(vname, mtcmplx);

            }
                
          } catch (MatlabInvocationException ex) {
              System.out.println("MATLAB Invocation exception: "+ex.getMessage());
          }
        }
      
    }
  }
    
    // parses the svdc string that contains a MATLAB svd command 
    // and then calls MATLAB to execute it
static public void msvd(String  svdc) {
 String invar = svdc.substring(svdc.indexOf("(")+1, svdc.indexOf(")")).trim();  // get the input variable
 int  firstCommaIndex = svdc.indexOf(",");
 String uvar = svdc.substring(svdc.indexOf("[")+1, firstCommaIndex ).trim();  // get the first output variable
 String restCommand = svdc.substring(firstCommaIndex+1, svdc.length()).trim();
 int secondCommaIndex = restCommand.indexOf(",");
 String svar = restCommand.substring(0, secondCommaIndex).trim();  // get the second output variable
 restCommand = restCommand.substring(secondCommaIndex+1, restCommand.length()).trim();
 String vvar = restCommand.substring(0, restCommand.indexOf("]")).trim();  // get the third output variable

 String [] inList = new String[1];
 inList[0] = invar;   // input variable
 String []  outList = new String[3];
 outList[0] = uvar; outList[1] = svar; outList[2] = vvar; 
  meval(svdc,  inList,  outList);
}

   }

