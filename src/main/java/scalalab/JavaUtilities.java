package scalalab;

import java.io.File;
import java.net.URL;
import scala.tools.nsc.Settings;
import scalaExec.Interpreter.GlobalValues;

public class JavaUtilities {

    public static boolean pathsDetected = false;  // avoid detecting paths multiple times since they do not change for single execution
    
    // detects and returns the URL of the .jar file where the class with  classname  resides.
    // of course that .jar should be on the classpath
      static URL  jarPathOfClass(String className) {
        try {
            return Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
        } catch (ClassNotFoundException ex) {
           System.out.println("error in jarPathOfClass"+className+")");
           ex.printStackTrace();
           return null;
        }

      }

      
      // classpath of the Scala interpreter is set by the user
     public static void setByUser(Settings set) { 
         set.classpath().setByUser_$eq(true);
     }
     
     
     public static void useJavaCP(Settings set) { 
         set.usejavacp();
     }
     
     
     // detect the important .jar libraries for the operation of ScalaLab, in order to place them on the classpath of the Scala Interpreter
     static public void detectPaths() {
          if (pathsDetected == false) {
              
              
          boolean hostIsUnix = true;
          if (File.pathSeparatorChar==';')
              hostIsUnix = false;  // Windows host
          
          if (hostIsUnix) {   
    JavaGlobals.jarFilePath = jarPathOfClass("scalaSci.RichDouble2DArray").toString().replace("file:/", "/");  
    JavaGlobals.compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "/");
    JavaGlobals.libFile =  jarPathOfClass("scala.io.Source").toString().replace("file:/","/");
    JavaGlobals.reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/","/");
    
    JavaGlobals.swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "/");
    JavaGlobals.jfreechartFile =jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "/");
    JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "/");
    
    if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsWin64 || GlobalValues.hostIsMac) {
      JavaGlobals.javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "/");
    }
    else {
      JavaGlobals.javacppFile = ".";

    }
  
    JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "/");
    JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "/");
    JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "/");
    JavaGlobals.rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "/");
    JavaGlobals.matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", "/");
    JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "/");
    JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","/");
    JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "/");
    JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "/");
     JavaGlobals.JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "/");
    
   
          }
         else {
    JavaGlobals.jarFilePath = jarPathOfClass("scalaSci.RichDouble2DArray").toString().replace("file:/", "");
    JavaGlobals.compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "");
    JavaGlobals.libFile =  jarPathOfClass("scala.io.Source").toString().replace("file:/","");
    JavaGlobals.reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/","");
    JavaGlobals.swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "");
    

    JavaGlobals.jfreechartFile =jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "");
    JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "");
    if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsWin64 || GlobalValues.hostIsMac) {
      JavaGlobals.javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "");
      }
    else {
      JavaGlobals.javacppFile = ".";
    }
    JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "");
    JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "");
    JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "");
    JavaGlobals.rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "");
    JavaGlobals.matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", "");
    JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "");
    JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","");
    JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "");
    JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "");
    JavaGlobals.JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "");
         }   
          
          System.out.println("jarFile ="+JavaGlobals.jarFilePath);
          System.out.println("compFile = "+JavaGlobals.compFile);
          System.out.println("libFile = "+JavaGlobals.libFile);
          System.out.println("reflectFile= "+JavaGlobals.reflectFile);
          System.out.println("swingFile = "+JavaGlobals.swingFile);
          
          System.out.println("MTJColtSGTFile = "+JavaGlobals.mtjColtSGTFile);
          System.out.println("JBLAS File = "+JavaGlobals.jblasFile);
          System.out.println("Apache Common Maths File = "+JavaGlobals.ApacheCommonsFile);
          System.out.println("EJMLFile = "+JavaGlobals.ejmlFile);
          System.out.println("RSyntaxTextArea file = "+JavaGlobals.rsyntaxTextAreaFile);
          System.out.println("MATLAB - SciLab connection file = "+JavaGlobals.matlabScilabFile);
          System.out.println("NUMALFile = "+JavaGlobals.numalFile);
          System.out.println("JFreeChartFile = "+JavaGlobals.jfreechartFile);
          System.out.println("Java Algebra System = "+JavaGlobals.JASFile);
          System.out.println("LAPACK library = "+JavaGlobals.LAPACKFile);
          System.out.println("ARPACK library = "+JavaGlobals.ARPACKFile);
          
          pathsDetected = true;   // paths are detected. Avoid the redundant job to rediscover them
          }
      }
     
      
      static public void detectBasicPaths() {
        detectPaths();   // use the  same paths for now
      }
     
}
