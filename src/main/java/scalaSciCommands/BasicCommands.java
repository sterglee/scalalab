
package scalaSciCommands;

// this package implements basic commands for the scalaSci's console

import java.awt.GridLayout;
import scalaExec.Interpreter.GlobalValues;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.lang.reflect.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector; 
import javax.swing.JFrame;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXEnvironment;
import scala.Function0;
import scala.Option;
import scala.reflect.io.Directory;
import scalaSci.help.PDFHelpFrame;

public class BasicCommands {
        public static String commands = "                              \n--------------scalaLab's Basic System Commands ----------------\n\n                      "+
              "\ndir():Unit       :   displays the directory contents" + "\nls()       :   displays the directory contents (as dir())"+
              "\ndirR():Unit       :   recursively displays the directory contents"+ "\nlsR()       :   recursively displays the directory contents"+
              "\ncd(\"directory\"):Unit   : changes the current directory"+
              "\nmd(\"directory\"): creates the specified directory"+
              "\nmkdir(\"directory\"): creates the specified directory"+
              "\npwd(): Unit : displays the current working directory "+
              "\ncls()      : clears the display"+
                "\nwhos() // prints the names  and types of the user defined variables"+
                "\nwhosv() // prints the names, types and values of the user defined variables"+
                 "\nwhoc() // prints the binding for common types"+
                
                "\ntic()           : starts the timer"+ "\ntoc()           : retrieves the time passed from the previous tick"+
              "\nString getFile(String initialDirectory, [String messageToUser]): gets a File with a File Dialog, starting at directory initialDirectory, specifying optionally a message to display to the user"+
              "\n String getFile( String messageToUser, boolean paramIsMessage): gets a File with a File Dialog,  specifying  a message to display to the user"+ 
                " \ndouble getDouble(String messageOfDialogBox, [double defaultValue]): gets a double value with a dialog box, specifying an optional default parameter"+  
              " \nint getInt(String messageOfDialogBox, [int defaultValue]): gets an int  value with a dialog box, specifying an optional default parameter"+
              " \nString getString(String messageOfDialogBox, [String defaultValue]): gets a String value with a dialog box, specifying an optional default parameter"+
              "\n def  load(fileName: String) : Int     // loads the Matlab .mat file contents to scalaSci workspace"+
                "\n def save(fileName: String, variableToSave: Double, variableName: String): Boolean"+
                "\n def save(fileName: String, variableToSave: RichDouble2DArray, variableName: String): Boolean"+
                "\n def  matVars()   // displays the Matlab variables of the workspace"+
                "\n def getMatScalar(scalarName: String): Double  // returns the value of the Matlab scalar variable"+
                "\n def getMatArray(arrayName: String): Array[Array[Double]]  // returns the value of the Matlab array variable"+
                
                "\nboolean  save(String fileName) :      // writes to the Matlab .mat file the contents of the scalaSci workspace"+
              "\nboolean  save(String fileName, String variableNameToSave)"+
              "\nint format(int decPoints)  // controls how many decimal points to display for doubles, sets to decPoints, returns previous setting  "+
                "\n def setprecision(decPoints: Int): Unit "+
                
                "\nsym(String symbolicCommand)   // evaluates a symbolic Algebra command"+
                
                "\n def setVerbose(vflag: Boolean)  // controls verbosing the results of  toString() "+
                "\n def getVerbose() // returns the current verbosing state "+
                "\n def setVecMxElemsToDisplay( mxElems: Int) : Int  // controls the number of elements displayed for vectors "+
                "\n def  setVecDigitsPrecision(precision: Int) // controls the precision of vector elements "+
                "\n def getVecDigitsPrecision() // returns the precision with which vector elements are displayed"+
                "\n def getMatMxRowsToDisplay() // returns the number of rows displayed for matrices"+
                "\n def getMatMxColsToDisplay()  // returns the number of cols displayed for matrices"+
                "\n def setMatMxRowsToDisplay(nrows: Int) // sets the number of rows displayed for matrices"+
                "\n def setMatMxColsToDisplay(ncols: Int)  //sets the number of cols displayed for matrices"+
                "\n def setMatDigitsPrecision(precision: Int) // controls the precision of Matrix elements "+
                "\n def getMatDigitsPrecision() // returns the precision with which Matrix elements are displayed"+
                
                "\n String dump()  // dumps the Scala interpreter state"+
               "\n  def reset():Unit  // resets the Scala interpreter state"+
               "\n  def clear(): Unit // clears the Scala interpreter state without using default imports"+
               "\n String classpath()  // returns the classpath of the Scala interpreter"+
               "\n String appendClasspath(String newPath) // appends the specified path to the classpath of the Scala interpreter if already not exist"+
              "\n String scalaInterpreterWithClassPathComponents() // updates the internal Scala classpath with the ScalaSciClassPath setting"+
               "\n def setVecResizeFactor( newResizeFactor: Double):Double   // sets the Vector resize factor. Returns the previous setting"+
               "\n def scatterPlotsOn()   // turns on scatter plotting"+
               "\n def linePlotsOn()    // turns on line plotting"+
               "\n def exec(ScalaScriptFileName: String)  // executes the code in the file ScalaScriptFileName"+
               "\n def eval(String expr): Object  // evaluates the ScalaSci expression passed as a String, e.g. eval(\"var x = rand(1000); plot(x);\")"+
               "\n def inspect(Object obj)   // inspects the object using reflection  "+
               "\n def inspectg(Object obj) // inspects the object graphically  "+
               "\n def displayMatrix(m: Array[Array[Double]]): Unit,  def displayMatrix(m: scalaSci.Matrix): Unit, def displayMatrix(m: scalaSci.Mat): Unit  "+   // displays the contents of the Matrix
               "\n def clearUserPaths() // clears all the user classpaths used by the interpreter "+
               "\n def disp(a: Array[Array[Double]]):Unit    // displays the contents of the double array a "+
               "\n def disp(v: Vec):Unit    // displays the contents of the Vector v "+
               "\n def disp(m: Mat):Mat    // displays the contents of the Mat m"+
               "\n def disp(m: Matrix):Unit    // displays the contents of the Matrix m "+
                "\ndef setw(nWhites: Int): Unit  // outputs nWhites white spaces "+
               "\n def typeOf() // displays the type of a variable "+
               "\n def freeMem():Unit // displays the free memory"+
               "\n def gc(): Unit  // garbage collect"+
                "\n def  printSettings(): String // display the settings of the ScalaLab global interpreter"+
                
                "\n def setSymbolicVerbose(state: Boolean): Booelan // sets whether to display preety LaTeX maths and returns the previous state"+
                "\n def getSymbolicVerbose(): Boolean  // gets ths display preety LaTeX maths state";
        
        // displays the result of the symbolic evaluation with a LaTeX style formatting
        public static String sym(String symCommand) throws Exception {
             StringBufferWriter buf = new StringBufferWriter();
             IExpr  result = GlobalValues.symUtil.evaluate(symCommand);
             OutputFormFactory.get().convert(buf, result);
             String output = buf.toString();
             
             output = symCommand+" = "+output;
             
             int  FONT_SIZE_TEX = GlobalValues.FONT_SIZE_TEX;
             
           if (GlobalValues.displayLatexOnEval)  {  
// display the LaTex formula graphically
             final StringBufferWriter bufTex = new StringBufferWriter();
             GlobalValues.texUtil.toTeX(symCommand + "="+result,  bufTex);
             String forLatexPrettyOut = bufTex.toString();
             				
             org.scilab.forge.jlatexmath.TeXFormula formula = new org.scilab.forge.jlatexmath.TeXFormula(forLatexPrettyOut);
             org.scilab.forge.jlatexmath.TeXIcon  ticon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, FONT_SIZE_TEX, TeXConstants.UNIT_PIXEL, 80,  TeXConstants.ALIGN_LEFT);
          
             GlobalValues.uiTabbedPane.setIconAt(0,  ticon);
           
        }
           return output;
      }
        
        public static boolean setSymbolicVerbose(boolean state) {  // sets whether to display preety LaTeX maths and returns the previous state"+
            boolean prevState = GlobalValues.displayLatexOnEval;
            GlobalValues.displayLatexOnEval = state;
            return prevState;
        }
        
        public static boolean getSymbolicVerbose() {
            return GlobalValues.displayLatexOnEval;
        }
        
       
        public static void setw(int nwhites) {
            StringBuilder sb = new StringBuilder(nwhites);
            for (int k=0; k<nwhites; k++)
                sb.append(" ");
            System.out.print(sb.toString());
        }
               //"\n def replay()\n";

     public static void whos() {
         scalaSciCommands.WatchInterpreterState.printUserNames();
     }   
     
     public static void whoc() {
         scalaSciCommands.WatchInterpreterState.bindingForCommonTypes();
     }   
     
     public static void whosv() {
         scalaSciCommands.WatchInterpreterState.printUserNamesAndValues();
     }   
     
    public static void setVerbose(boolean vflag) { 
        scalaSci.PrintFormatParams.setVerbose(vflag);
    }
    
    public static boolean getVerbose() {
        return scalaSci.PrintFormatParams.getVerbose();
     }
    
    public static void  setVecMxElemsToDisplay(int  mxElems)
    {
        scalaSci.PrintFormatParams.setVecMxElemsToDisplay(mxElems);
    }
    
    public static int getVecDigitsPrecision() {
        return scalaSci.PrintFormatParams.getVecDigitsPrecision();
    }
    
    public static void  setVecDigitsPrecision(int precision)  {
        scalaSci.PrintFormatParams.setVecDigitsPrecision(precision);
    }
    
    public static int   getMatDigitsPrecision()  {
        return scalaSci.PrintFormatParams.getMatDigitsPrecision();
    }
    
    public static int   getMatMxRowsToDisplay()  {
        return scalaSci.PrintFormatParams.getMatMxRowsToDisplay();    
    }
    
    public static void  setMatMxRowsToDisplay(int nrows)  {
         scalaSci.PrintFormatParams.setMatMxRowsToDisplay(nrows);
    }
    
    
    public static int   getMatMxColsToDisplay()  {
        return scalaSci.PrintFormatParams.getMatMxColsToDisplay();    
    }
    
    public static void  setMatMxColsToDisplay(int ncols)  {
        scalaSci.PrintFormatParams.setMatMxColsToDisplay(ncols);
    }
    
 public static String printSettings() {
     String     currentInterpreterSettings =  GlobalValues.globalInterpreter.settings().optimise().toString()+"\n"+
                                                                 GlobalValues.globalInterpreter.settings().target().toString();
     return currentInterpreterSettings;
             }
              
public static String fail(String str) {
    System.out.println(str);
    return str;
}               

public static long freeMem()  {
            long frMem = GlobalValues.rt.freeMemory();
            return frMem;
        }
                
public static void gc() { 
    GlobalValues.rt.gc();
}
   // var a = readARFFFile("c:\\test\\quake.arff")
/*public static double [][]  readARFFFile(String fileName )        {
    Instances instances=null; 
		
	try
		{
	FileReader  arffReader = new FileReader(fileName);
	instances = new Instances(arffReader);
	 }
     catch (Exception e)
	 {
	System.out.println("ReadARFF: I/O exception");
	}		    
	int nInstances = instances.numInstances();
	int nAttribs = instances.numAttributes();
	double [][] values = new double[nInstances][nAttribs];
	for (int inst=0; inst<nInstances; inst++) {
		weka.Instance  currentInstance = instances.instance(inst);
		for (int attr=0; attr<nAttribs; attr++) 
		   values[inst][attr]  = currentInstance.value(attr);
		}

         return values;
}
*/
        public static void replay() {
            
        }

         public static int  load(String fileName)  {     // loads the Matlab .mat file contents to scalaSci workspace"+
             return scalaSci.math.io.MatIO.load(fileName);
         }
             
         public static  boolean  save(String fileName, scalaSci.Vec  v, String variableNameToSave)  {      // writes to the Matlab .mat file the contents of the scalaSci workspace"
              return scalaSci.math.io.MatIO.save(fileName, v, variableNameToSave); 
          }
          
         public static  boolean  save(String fileName, double [] varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, double [][] varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, scalaSci.Mat varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, scalaSci.EJML.Mat varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, scalaSci.MTJ.Mat varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
          
          
         public static  boolean  save(String fileName, scalaSci.CommonMaths.Mat varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, scalaSci.RichDouble2DArray varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         public static  boolean  save(String fileName, scalaSci.Matrix varValues, String variableNameToSave) {
             return scalaSci.math.io.MatIO.save(fileName, varValues, variableNameToSave);
          }
          
          
         // display the names of all Matlab vars, both scalars and arrays
    public static void matVars() {
        System.out.println("\nSCALARS READED FROM .mat FILES : \n");
        Enumeration scalarNames =  scalaExec.Interpreter.GlobalValues.scalarValuesFromMatlab.keys();
        while (scalarNames.hasMoreElements()) {
            String currentVarName = scalarNames.nextElement().toString();
            System.out.println(currentVarName);
        }
        System.out.println("\nMATRICES READED FROM .mat FILES: \n");
        Enumeration arrayNames =  scalaExec.Interpreter.GlobalValues.arrayValuesFromMatlab.keys();
        while (arrayNames.hasMoreElements()) {
            String currentArrayName = arrayNames.nextElement().toString();
            System.out.println(currentArrayName);
        }
    }
    
    // get the value of a Matlab scalar 
    public static double getMatScalar(String name) {
        double scalarValue = scalaExec.Interpreter.GlobalValues.scalarValuesFromMatlab.get(name);
        return scalarValue;
    }
    
    // get the value of a Matlab array
    public static double [][] getMatArray(String name) {
        double [][] arrayValue = (double [][] )scalaExec.Interpreter.GlobalValues.arrayValuesFromMatlab.get(name);
        return arrayValue;
    }
    
        public static void disp( double [][] a)  {
            int N = a.length;
            int M = a[0].length;
            for (int n=0; n<N; n++) {
                for (int m=0; m<M; m++)
                    System.out.print(a[n][m]+"  ");
                    System.out.println();
              }
        }
        
        public static void disp( scalaSci.Vec a)  {
            System.out.println("Vector contents: \n");
            double [] av = a.getv();
            for (int m=0; m<a.size(); m++)
                 System.out.print(av[m]+"  ");
        }
        
        public static void disp( scalaSci.Mat m)  {
            System.out.println("Zero-indexed Matrix contents: \n");
            disp(m.getv());
        }
        
        public static void disp( scalaSci.Matrix m)  {
            System.out.println("One-indexed Matrix contents: \n");
            disp(m.getv());
        }
        
        public static void clearUserPaths() {
            GlobalValues.clearUserPaths();
        }
        
        public static void displayMatrix( double [][] matrix) {
            scalaExec.gui.watchMatrix.display(matrix, false);
        }
        
        public static void displayMatrix( scalaSci.Matrix  matrix) {
            scalaExec.gui.watchMatrix.display(matrix.getv(), true);
        }
      
        public static void displayMatrix( scalaSci.scalaSciMatrix  matrix) {
            scalaExec.gui.watchMatrix.display(matrix.toDoubleArray(), false);
        }
      
        
        public static void inspect(Object obj)  {
            Inspect.inspect(obj);
        }

        public static void inspectg(Object obj)  {
            Inspect.inspectg(obj);
        }

 public static void dumpState() {
     System.out.println("dumpState: ");
     //GlobalValues.scalalabMainFrame.scalalabConsole.scalaInterpreter.dumpValAndVarNames();
 }
public static Object eval(String expr)  {
    scala.tools.nsc.interpreter.Results.Result  res = scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(expr);
    return res; 
}

 public static String   typeOf(String ident) {
    String typeStr = scalaExec.Interpreter.GlobalValues.globalInterpreter.typeOfExpression(ident, true).toString();
    return  typeStr;
}

 // # eval("var kk = k"); var hh = 7*kk
public static Integer  getIntValue(String varName)  {  // gets the value of the variable varName if it was defined
    String  evalExpr = "var  vv ="+varName;
    Object rv =  eval(evalExpr); 
    return   (Integer)rv; 
}

 
  public static void setprecision(int decPoints) {
      format(decPoints);
  }      
              
         // controls how many decimal points to display for doubles, sets to decPoints, returns previous setting
  public static int format(int decPoints)   {
            int prevFmtLen = GlobalValues.doubleFormatLen;
            GlobalValues.doubleFormatLen = decPoints;
            String s="0.";
            for (int k=0; k<decPoints;k++)  s +="0";
            GlobalValues.fmtString = new DecimalFormat(s);
            GlobalValues.fmtMatrix = GlobalValues.fmtString;
            GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
            GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
            
            scalaSci.PrintFormatParams.setMatDigitsPrecision(decPoints);
                    
            return prevFmtLen;
        }
                
  public static String tic() {    
      System.gc();
      GlobalValues.timeForTic = System.nanoTime();   // save the current time
        return  Long.toString(GlobalValues.timeForTic);
  }

  public static String toc() {
         double delay = (double)(System.nanoTime()-GlobalValues.timeForTic)/1000000000.0;
        return Double.toString(delay);
  }
//double sm=0.0d; int  k1=1; tic(); while (k1 < 1000000) { sm = (0.0002/(double)k1); k1++; };  String  tm = toc(); System.out.println("sm = "+sm+",   tm = "+tm);

  
  public static void cls() {
              GlobalValues.scalalabMainFrame.scalalabConsole.clearConsole();
              GlobalValues.consoleOutputWindow.output.setText("");
   }
  
  
  public static void help() {
              System.out.println(commands);
   }
  
  
public static void help(String command) throws FileNotFoundException, IOException  {
    String fullFileName =  GlobalValues.scalalabHelpPath+File.separator+command+".pdf";
    File ft = new File(fullFileName);
    long filelen = ft.length();
    
    if (filelen ==0L )   {
        System.out.println("file "+fullFileName+" has zero length");
        return;
    }
    
    //JPEGHelpFrame lf = new JPEGHelpFrame(ft);
    PDFHelpFrame lf = new PDFHelpFrame(ft);
    
}

  public static void disp(String str) {
         System.out.println(str);
  }

        
  

public static double getDouble(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    double val = Double.valueOf(strMsg);
    return val;
}

public static double getDouble()  {
    String strMsg = JOptionPane.showInputDialog("Give double value");
    double val = Double.valueOf(strMsg);
    return val;
}

public static int  getInt(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    int val = Integer.valueOf(strMsg);
    return val;
}

public static int  getInt()  {
    String strMsg = JOptionPane.showInputDialog("Give int value");
    int val = Integer.valueOf(strMsg);
    return val;
}


public static String getString(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    return  strMsg;
}

public static String getString()  {
    String strMsg = JOptionPane.showInputDialog("Give String value");
    return  strMsg;
}

public static double getDouble(String msg, double defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg, Double.toString(defaultVal));
    double val = Double.valueOf(strMsg);
    return val;
}


public static int  getInt(String msg, int defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg, Integer.toString(defaultVal));
    int val = Integer.valueOf(strMsg);
    return val;
}


public static String getString(String msg, String defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg,  defaultVal);
    return  strMsg;
}

public static String getFile() {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(Directory.Current().get().path());
    chooser.setDialogTitle("Specify file for loading data");
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static String getFile(String dir) {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(dir);
    chooser.setDialogTitle("Specify file for loading data");
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static String getFile(String dir, String messageToUser) {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(dir);
    chooser.setDialogTitle(messageToUser);
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  


public static String getFile( String messageToUser, boolean paramIsMessage) {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
    chooser.setDialogTitle(messageToUser);
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static  Object arrayGrow( Object o)  {
    Class  cl = o.getClass();
    if (!cl.isArray() )   return  null;
    Class componentType = cl.getComponentType();
    int  length = Array.getLength(o);
    int  newLength = length * 11 / 10 + 10;
    
    Object newArray = Array.newInstance(componentType, newLength);
    System.arraycopy(o, 0,  newArray, 0, length);
    return newArray;
}

public static String dump() {
    //String scalaInterpreterState = GlobalValues.scalalabMainFrame.scalalabConsole.scalaInterpreter.dumpBoundNames();

    return ""; //scalaInterpreterState;
}

// reset the Scala interpreter
public static void reset() {
 GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForReset();
}

public static void clear() {
 GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForReset();
}



public static String classpath() {
    scala.collection.immutable.List <URL> classp = scalaExec.Interpreter.GlobalValues.globalInterpreter.compilerClasspath().toList();
    return classp.toString();
}

public static String appendClasspath(String newPath) {
   // construct and initialize a new Scala Interpreter with appended classpath that includes the 'newPath'   
    newPath = newPath.trim();
  GlobalValues.globalInterpreter.settings().classpath().append(newPath);
    //  update also the GUI ScalaSciClassPathComponents parameter to include the new path   
    if (GlobalValues.ScalaSciClassPathComponents.contains(newPath)==false)  {
     GlobalValues.ScalaSciClassPathComponents.add(newPath);
     scalaExec.scalaLab.scalaLab.updateTree();
     }
    if (GlobalValues.interpreterClassPathComponents.contains(newPath) == false)  {
        GlobalValues.interpreterClassPathComponents.add(newPath);
        
        GlobalValues.scalalabMainFrame.constructPathPresentationPanel();
        
    }
    // return the appended classpath
    return classpath();
}

public static String appendClasspaths(String [] newPaths) {
    int len = newPaths.length;
            
   // construct and initialize a new Scala Interpreter with appended classpath that includes the 'newPath'   
    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(newPaths);
  // update also the GUI ScalaSciClassPathComponents parameter to include the new path   
  for (int k=0; k<newPaths.length; k++)
    if (GlobalValues.ScalaSciClassPathComponents.contains(newPaths[k])==false)
        GlobalValues.ScalaSciClassPathComponents.add(newPaths[k].trim());
    
     scalaExec.scalaLab.scalaLab.updateTree();   
       // return the appended classpath
    return classpath();
}

public static String appendClasspaths(Vector <String> newPaths) {
    int len = newPaths.size();

   // construct and initialize a new Scala Interpreter with appended classpath that includes the 'newPath'
    if (GlobalValues.interpreterTypeForPane == GlobalValues.EJMLMat)
    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCPEJML(newPaths);
     else
    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(newPaths);
    
  // update also the GUI ScalaSciClassPathComponents parameter to include the new path
  for (int k=0; k<newPaths.size(); k++)
    if (GlobalValues.ScalaSciClassPathComponents.contains(newPaths.elementAt(k))==false)
        GlobalValues.ScalaSciClassPathComponents.add(newPaths.elementAt(k));

     scalaExec.scalaLab.scalaLab.updateTree();
       // return the appended classpath
    return classpath();
}

// creates a new Scala Interpreter that is initialized to include all the ScalaSciClassPathComponents in its classpath
public static String  scalaInterpreterWithClassPathComponents() {
    int cplen = GlobalValues.ScalaSciClassPathComponents.size();
    String [] cpElems = new String[cplen];
    for (int k=0; k < cplen; k++) 
        cpElems[k] = (String) GlobalValues.ScalaSciClassPathComponents.elementAt(k);
    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
   scalaExec.scalaLab.scalaLab.updateTree(); 
return classpath();
}



   public static void exec(String fileName)  {

        if (fileName.indexOf(File.separatorChar)==-1)  // not absolute path: append current directory
           fileName = Directory.Current().get().path()+File.separator+fileName;
       
       if (fileName.indexOf(".")==-1)  // append as default extension ".scala"
           fileName = fileName+".scala";
       
   if (fileName != null) {  // load the contents of the file at the editor's textArea for editing
       int idxDir = fileName.indexOf(File.separatorChar);
          try {
      File file = new File(fileName);
      FileReader fr = new FileReader(file);
      BufferedReader in = new BufferedReader(fr);
      int MaxBufLen = (int) file.length();
      char [] cbuf = new char[MaxBufLen];
      in.read(cbuf, 0, MaxBufLen);
      String textIn = String.valueOf(cbuf);
      GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(textIn);
            }
    catch (Exception e) {
      System.out.println("Cannot read file "+fileName);
    }
  }
   }     
  
   
}
   
