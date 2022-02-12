
package scalaExec.Interpreter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabType;
import org.scilab.modules.types.ScilabDouble;

public class SciLabConnection {
  
    // initializes a SciLab instance
    static public  void initSciLabConnection() {
        if (GlobalValues.sciLabInitedFlag == false) {
        try {
            GlobalValues.scilabObj = new Scilab();
            GlobalValues.scilabObj.open();
        } 
        catch (org.scilab.modules.javasci.JavasciException e) {
            System.err.println("An exception occurred: initializing SciLab " + e.getLocalizedMessage());
      }
        
        GlobalValues.sciLabInitedFlag = true;
        }
    }


        // "general" type evaluation. Each input and output parameter can be either real (i.e. double [][])  or complex (i.e. MatlabComplex [][])
    // evaluate a SciLab script that has input the vInNames list of ScalaLab's variables and it computes 
    // the vOutNames variables. 
    
    static public void scieval(String expr, String [] vInNames,  String [] vOutNames) {
      Scilab sciobj = GlobalValues.scilabObj; 
     // transfer the variables of the vInNames list,  from ScalaLab to MATLAB's workspace 
     if (vInNames != null) {  // list of variables to transfer from ScalaLab to MATLAB are specified
       int numInVars = vInNames.length;
      for (int v = 0; v<numInVars; v++) {   // for all ScalaLab's variables
         String vname = vInNames[v];
         if  (vname.length() > 0) {
         Object cvar = scalaExec.Interpreter.GlobalValues.globalInterpreter.valueOfTerm(vname).get();  // currently examined input variable
       if (cvar instanceof  double [][])  {  // double [][] array
           double [][] xin = (double [][]) cvar;
           ScilabDouble sd = new ScilabDouble(xin);
             try {
                 sciobj.put(vname, sd);
             } catch (JavasciException ex) {
                 System.out.println("JavaSci exception "+ex.getMessage());
             }
         } // double [][] array    
         else if (cvar instanceof  scalaSci.scalaSciMatrix)  {  // scalaSci.scalaSciMatrix
           double [][] xin =  ((scalaSci.scalaSciMatrix) cvar).toDoubleArray();
           ScilabDouble sd = new ScilabDouble(xin);
           try {
           sciobj.put(vname, sd);
             }
           catch (JavasciException ex) {
                 System.out.println("JavaSci exception "+ex.getMessage());
             }
      }  // scalaSci.scalaSciMatrix   
         else if (cvar instanceof  double [] )  {  // double []
           double [][] xin = new double[1][1];
           xin[0] = (double [] ) cvar;
           ScilabDouble sd = new ScilabDouble(xin);
           try {
           sciobj.put(vname, sd);
           } 
           catch (JavasciException ex) {
                 System.out.println("JavaSci exception "+ex.getMessage());
             }
      }   // double []
         else if (cvar instanceof  Double)  {  // double
           ScilabDouble sd = new ScilabDouble((double)cvar);
            try {
           sciobj.put(vname, sd);
            }
            catch (JavasciException ex) {
                 System.out.println("JavaSci exception "+ex.getMessage());
             }
         }   // Double 
         else if (cvar instanceof  Integer)  {  // int
           ScilabDouble sd = new ScilabDouble((int)cvar);
           try {
           sciobj.put(vname, sd);
           } 
           catch (JavasciException ex) {
                 System.out.println("JavaSci exception "+ex.getMessage());
             }
             
         }   // Integer
       } // vname.length() > 0
      }      // for all ScalaLab's variables
     } // list of variables to transfer from ScalaLab to SciLab are specified
     
     sciobj.exec(expr);  // evaluate the script text with SciLab
    
      // transfer from SciLab's workspace to ScalaLab
      if (vOutNames != null)  {   // list of variables to transfer from SciLab to ScalaLab
      int numOutVars = vOutNames.length;
     for (int v = 0; v<numOutVars; v++) {  
          try {
              String vname = vOutNames[v];
              if (vname.length() > 0)  {
              // get the computed SciLab matrix as Java2D array
              ScilabType scitype = sciobj.get(vname);
              scalaExec.Interpreter.scalaBind.bind(vname, (ScilabType) scitype);
              
          } 
          }
          catch (JavasciException ex) {
              Logger.getLogger(SciLabConnection.class.getName()).log(Level.SEVERE, null, ex);
          }
            
         }
        }
      }
    


}
