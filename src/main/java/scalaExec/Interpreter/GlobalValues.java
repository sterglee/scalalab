package scalaExec.Interpreter;
import com.googlecode.totallylazy.Sequence;
import edu.emory.mathcs.utils.ConcurrencyUtils;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.*;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import scala.tools.nsc.interpreter.IMain;
import scalaExec.ClassLoaders.ExtensionClassLoader;
import scalaExec.ClassLoaders.JarClassLoader;
import scalaExec.gui.*;
import scalaExec.scalaLab.*;
import scalainterpreter.ScalaInterpreterPane;
import scalalabEdit.*;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxy;

import org.scilab.modules.javasci.Scilab;




// keeps important global definitions for ScalaLab
public class GlobalValues
{

    
    static public   String  scalalabBuildVersion = "12 - Feb - 2022";
   
    static public ScalaInterpreterPane globalInterpreterPane;  // the interpreter pane object which is the main interface to the Scala interpreter
    static public Vector <String> interpreterClassPathComponents = new Vector<>();  // the class path components of the main interpreter
    
    static public  JScrollPane  outputPane;     // the System Console output scroll pane
    static public  JSplitPane   mainSplitPane;    // the main ScalaLab scroll pane
    //  this file lists in text format the paths and the tolbox .jar files that the user appends to the ScalaSci classpath
    static public String  userPathsFileName = "scalalabUserPaths.txt";  
    
     
            
    static public String  editingFileInPane = null;  // the user has loaded a specific file to edit
    static public boolean  editorTextSaved = false; 
    static public JPanel  pathPresentationPanel = new JPanel();
         
    static public JFrame toolbarFrame;  // the toolbar frame
    
    static public boolean ScalaLabInInit = false;  // used to avoid some Swing component repaints duting initialization
    
    // ScalaLab utilizes the following two editors for editing code, the jsyntaxPane being the default editor
    static public JEditorPane editorPane; // the jsyntaxPane based editor
    static public RSyntaxTextArea globalRSyntaxEditorPane;  // the RSyntaxArea based editor
     
    public static  scala.tools.nsc.interpreter.IMain  globalInterpreter;   // the main interpreter
     
      // it is used to adjust the number of threads for multithreaded operations, as matrix multiplication
    static public int numOfThreads = ConcurrencyUtils.getNumberOfThreads();
  
    // a thread pool for tasks as parallel multiplication, multithreaded computations, asynchronous future based computations etc.
    static public ExecutorService execService =   Executors.newFixedThreadPool(ConcurrencyUtils.getNumberOfProcessors()*4);

  
    // for managing background execution of scripts
    static public scalaExec.Interpreter.PendingFutures pendingFutures = new scalaExec.Interpreter.PendingFutures();
    static public scalaExec.Interpreter.PendingThreads pendingThreads = new scalaExec.Interpreter.PendingThreads();
      
    static public  java.lang.Thread currentThread = null;   // the currently executed thread
    static public   double  mulMultithreadingLimit = 1000;  // a multiplication that involves more than that number of elements, is large thus use multithreading
    

    
   // these concern the socket based ScalaLab's server computations 
 static public ServerSocket scalaLabServerSocket;
 static public int scalaLabServerPort = 8000;
 static public final int exitCode = -1;
 static public final int svdCode = 1; 
 static public String serverIP  = "195.130.94.125";
 static public Socket sclient = null;
 static public InputStream   clientReadStream = null;
 static public OutputStream  clientWriteStream =  null;
 static public DataInputStream    reader = null;
 static public DataOutputStream     writer = null;
  
// variables for ScalaLab - MatLab interface
    static public boolean matlabInitedFlag = false;   // whether  ScalaLab - MATLAB connection is inited
    static public MatlabProxyFactory factory = null;
    static public MatlabProxy proxy = null; 
    
        // variables for ScalaLab - SciLab interface
    static public boolean sciLabInitedFlag = false;   // whether  ScalaLab - SciLab connection is inited
    static public Scilab scilabObj  = null; 
    
    
    
    // variables controlling window for watching variables of the running scripting session
    static public boolean watchVariablesFlag = false;
    static public String watchVariablesProp = "false";
    
    static public JFrame watchFrame = null;  // the watch frame for variables
    static public JTable tableOfVars = null; // JTable to display the variables
    static public JTabbedPane uiTabbedPane;
    
    
    static public String scriptObjectName = "ScriptObject";    // using for compiling standalone applications
    
    static public boolean inspectClass = false;   // class vs identifier inspection for code completion using Java reflection
 
    
    // symbolic algebra related variables
    static public org.matheclipse.core.eval.EvalUtilities  symUtil;  // symbolic Algebra object 
    static public TeXUtilities texUtil;  // for LaTex displaying
    static public boolean displayLatexOnEval = true;  // controls whether to display in LaTex style, the outcome of symbolic evaluation with symja
    static public int FONT_SIZE_TEX = 18;
    
    static public Font defaultTextFont = new Font("DejaVu Sans Mono", Font.PLAIN, 14);     
   
     // some variables used to implement code completion
   static public boolean completionIsForSyntaxPane = true; //  controls if the jsyntaxpane or the rsyntaxarea editor triggered the completion
   static public JFrame completionFrame;  // the open completion Frame, it is used in order to dispose it with ESC
   static public String  completionText;  // the text that the completion operation inserts
   static public int selectionStart;
   static public int selectionEnd;
   static public boolean methodNameSpecified = false;   // method name specified at completion
   // at code completion (with F4) static members are denoted in bold font
   static public Font staticsFont = new Font("Arial", Font.BOLD, 11);
   // at code completion (with F4) instance members are denoted in plain font text
   static public Font instancesFont = new Font("Arial", Font.PLAIN, 11);
   static public Font fontForCompletionListItem = instancesFont; // font to use for rendering the current item
   static public String staticsMarker = "#####";
   static public String nonStaticsMarker = "%%%%%";
   static public boolean [] isStaticMarks;    // whether each item of the completion list items is static
   static public String nameOfType;  // it is needed to access the class name for static members
   static public int  selectionBeginning;  // for static members completion to keep the start of the identifier
   static public int maxItemsToDisplayAtCompletions = 15;
   
    // controls whether we connect the code completion of the RSyntaxArea with the results of the Scala Completion
    static public boolean rsyntaxInScalaCompletionMode = false;
    static public String   rsyntaxInScalaCompletionModeProp = "Global"; 
    
    // results obtained using Scala completer
    static public java.util.ArrayList<String>  scalaResultsForCompletion = new java.util.ArrayList();
    
    // for which JVM target Scala compiler should produce code
    static public String [] targetSetting = {"jvm-1.5", "jvm-1.5-fjbg", "jvm-1.5-asm", "jvm-1.6", "jvm-1.7","jvm-1.8"};
    static public int currentTargetSelectionIndex = 5; 
    static public String currentTargetSelection = targetSetting[currentTargetSelectionIndex];
    
    static public Boolean  compilerOptimizationFlag = true;  // controls whether to optimize the code
    
    static public Boolean preferRSyntaxEditor = false;  // controls whether to initialize the environment for jsyntaxPane or rsyntaxArea based editors
    static public  CompletionProvider provider;      // used for RSyntaxArea based code completion
    
    // enable/disable mouse motion listeners, mouse listeners control displaying the contexts of the variables when the mouse cursor is over them
    static  public  boolean mouseMotionListenerForRSyntax = true;
    static  public  boolean mouseMotionListenerForJSyntax = true;
    static  public  MouseMotionAdapter  jsyntaxMouseMotionAdapter;
    static  public  MouseMotionAdapter  rsyntaxMouseMotionAdapter;
    static  public  String  lastVariableUnderMouseCursor = "";    // in order to avoid redisplaying the same variable
    
// accessing values for variables of all types vs. the normal behaving ones (i.e. primitive types and scalaSci types)
    static  public  boolean getValuesForAllRSyntax = true;
    static  public  boolean getValuesForAllJSyntax = true;
   
    
    static public  File  forHTMLHelptempFile;     // used for displaying html help files
        
    // keeps the names of the toolboxes, the contents of which are currently displayed
    // in  order to avoid redisplaying them
    static public Vector<String> displayedToolboxes = new Vector<>();  

    static public boolean startupHelpFlag = true;   // present some help in pane on startup

    static public int consoleCharsPerLine;
    
    static public   JProgressBar   ggProgress;
    static public   JComponent   ggProgressInvis;
    
    // ScalaLab's auxiliary editor parameters
    static public  String  editFrameLocX = "100";  static public String editFrameLocY = "100";
    static public  String  editFrameSizeX = "600";  static public String editFrameSizeY = "600";
  
    static public boolean useSystemBrowserForHelp = true;  // use the system's browser vs using the Swing JEditorPane
    static public Desktop  desktop;
    
    static public double helpMagnificationFactor = 1.0; // magnification factor for HTML help pages
    static public  Vector <String>  replayingBuffer = new Vector<>();  // keeps the commands for replaying
    static public boolean  recordCommandsForReplayingFlag = true;    // if true the interpreting commands are recorded for replaying
    
     public static   java.text.DecimalFormat availMemFormat = new java.text.DecimalFormat("0.00");
     public static JLabel availMemLabel;  // available memory model
    
     public static final double nearZeroValue = 1.0e-10;
     public static final int Inf = 9999999;   // a value denoting Inf at norm computations
     public static final int Fro = 6; // a value for the Frobenious norm
    // which Mat toolbox is used
    public static final int JAMAMat = 0;
    public static final int EJMLMat = 1;
    public static final int MTJMat = 2;
    public static final int ApacheCommonMathsMat=3;
    public static final int JBLASMat = 4;
    public static final int D2Das1DMat = 5;
    public static final int NotAnyImports = 14;
    
      
    public static int interpreterTypeForPane = JBLASMat;  // the interpreter type used
    static public String scalaInterpreterTypeProp=String.valueOf(JBLASMat);   // default 
        
    public static boolean autoCompletionVariableSelected = false;  // avoid to execute text after selection of a variable from the autocompletion workspace
    public static boolean notOverlapTheConsoleOutput = true;  // avoid overlaping the Console with other windows
    public static double  mainFrameRelativeSize = 0.8;
    
    public static Runtime rt;  // the runtime for observing available memory
    public static long memAvailable;  // free memory available
   
    static public boolean autocompletionEnabled = false;   // controls loading automatically the autocompletion information
    static public Map desktophints;
    static public FileTreeExplorer currentFileExplorer = null;
    // keep the variables participated at the ScalaSci binding. It is updated with scalaExec.gui.watchScalaWorkspace.displayScalaSciBinding()
    
   static public int  RGBAlpha, RGBRed, RGBBlue, RGBGreen;
   
// for retrieving Matlab .mat file variables
   static public double [][] data;
   static public double scalarData;
   static public Hashtable<String, Double> scalarValuesFromMatlab = new Hashtable<String, Double>();
   static public Hashtable<String, double[][]> arrayValuesFromMatlab = new Hashtable<String, double[][]>();
   
   static public String [] cellStrings;
  
   static public  JTabbedPane  scalaSciTabbedToolBar;
   static public JPanel  mainStatusPanel;  // the status panel of the ScalaLab's main frame
   static public boolean withoutScalaLabLibs = true;    // do not import ScalaLabLibs
   static public boolean explorerVisible = false;
   static public boolean mainToolbarVisible = false;

     // Defaults for Main Help
    static public  String  JavaHelpSetName  = "jdk6Help";
    static public  String  ScalaHelpSetName = "scalaHelp";
    static public  CSHObject  JavaCSHObject = null;
    static public  CSHObject  ScalaCSHObject = null;
   
     // some variables used for passing bindings  to the Scala interpreter
    static public double [] doubleArrayValForBinding;
    static public double [][]  double2DArrayValForBinding;
    static public scalaExec.Interpreter.MatlabComplex  matlabComplexForBinding;  
       
    // the ExtensionClassLoader can be used to load at runtime classes contained in paths specified in ScalaSciClassPath
    static public ExtensionClassLoader extensionClassLoader;
        
    static public String selectedExplorerPath;    // keeps the selected file system path with the ScalaLabExplorer
        
    static public   int  doubleFormatLen = 4; // how many digits to display for doubles, Matrix
    static public   DecimalFormat fmtString = new  DecimalFormat("0.0000");
    static public   DecimalFormat fmtMatrix = new DecimalFormat("0.000"); // format Matrix results
    // these colors of the console input window  indicate the corresponding scalaLab operating modes
    static public  Color  ColorScalaSci = new Color(50, 50, 55);
    static public float alphaComposite = 0.5f;

         
         
        /**Constant with the application title.*/
    public static String TITLE= scalaExec.Interpreter.GlobalValues.buildTitle();
    static public JFrame  globalRSyntaxFrame;  // the JFrame where the editor resides
     // parameters determining the location and size of the RSyntaxEditorPane
    static public int rlocX, rlocY, rWidth, rHeight;
    
    static public String workingDir;

    static public String buildTitle() {
      
      String mainFrameTitle =        "ScalaLab:    Scala "+scala.tools.nsc.Properties.versionString()+",  library type: "+ GlobalValues.interpreterType()+ 
                   ",   "+System.getProperty("java.vm.name", "").toLowerCase()+",  "+ System.getProperty("os.name", "").toLowerCase()+
                   "  "+ System.getProperty("os.arch", "").toLowerCase()+" ,   "+
                   GlobalValues.scalalabBuildVersion;
      
  return mainFrameTitle;
    }
    
    public GlobalValues() {
    
         
        
       
    
    }

     //  get a string denoting the type of the current interpreter
  static public String interpreterType() {
        
        switch (interpreterTypeForPane) {
                case JAMAMat:  return "JAMA ";
                case EJMLMat:  return "EJML "; 
                case MTJMat: return "MTJ "; 
                case ApacheCommonMathsMat:  return "Apache Commons"; 
                case JBLASMat: return "JBLAS";  

                case D2Das1DMat: return "D2Das1D";
                
                case NotAnyImports: return  "  not using any imports";
                
                default: return "Uknown library";
        }
                   
                        
        };

    static public long  timeForTic; // save the current time in milliseconds to implement tic-toc functionality
 
    static public boolean interruptcifor = false;  // a flag that when it is set true, the cifor loop terminates with an exception
 
    static public boolean commandLineModeOn = true;  // execute line-by-line or accumulate buffer until F8?
    static public boolean scalaJarClassesLoaded = false;  // controls the loading/reloading of Scala classes
    
    static public boolean truncateOutput  = false;   // displays all the output results from the Scala interpreter without truncating
    static public boolean globalVerboseOff = false;
    static public boolean displayAtOutputWindow = false;   // controls displaying at output window
    
    static public String currentToolboxName;   // keeps the currently loaded toolbox name
    
    // required libs for the embedded Apache Commons Math library that is placed within the /lib folder
    // that version is somewhat outdated but it is required for the Symbolic Algebra code
    // a newer Apache Common Maths library exists within the default toolboxes folder
    static public  String ApacheCommonsImports =
                    "\nimport org.apache.commons.math._\n"+
                    "import org.apache.commons.math.analysis._ \n"+
                    "import org.apache.commons.math.analysis.function._ \n"+
                    "import org.apache.commons.math.analysis.integration._ \n"+
                    "import org.apache.commons.math.analysis.interpolation._ \n"+
                    "import org.apache.commons.math.analysis.polynomials._ \n"+
                    "import org.apache.commons.math.analysis.solvers._ \n"+
//                    "import org.apache.commons.math.complex._ \n"+
                    "import org.apache.commons.math.dfp._ \n"+
                    "import org.apache.commons.math.distribution._ \n"+
                    "import org.apache.commons.math.estimation._ \n"+
                    "import org.apache.commons.math.exception._ \n"+
                    "import org.apache.commons.math.exception.util._ \n"+
                    "import org.apache.commons.math.filter._ \n"+
                    "import org.apache.commons.math.fraction._ \n"+
                    "import org.apache.commons.math.genetics._ \n"+
                    "import org.apache.commons.math.geometry._ \n"+
                    "import org.apache.commons.math.geometry.euclidean.oned._ \n"+
                    "import org.apache.commons.math.geometry.euclidean.threed._ \n"+
                    "import org.apache.commons.math.geometry.euclidean.twod._ \n"+
                    "import org.apache.commons.math.geometry.partitioning._ \n"+
                    "import org.apache.commons.math.geometry.partitioning.utilities._ \n"+
                    "import org.apache.commons.math.linear._ \n"+
                    "import org.apache.commons.math.ode._ \n"+
                    "import org.apache.commons.math.ode.events._ \n"+
                    "import org.apache.commons.math.ode.nonstiff._\n"+
                    "import org.apache.commons.math.ode.sampling._\n"+
                    "import org.apache.commons.math.optimization._\n"+
                    "import org.apache.commons.math.optimization.direct._\n"+
                    "import org.apache.commons.math.optimization.fitting._\n"+
                    "import org.apache.commons.math.optimization.general._\n"+
                    "import org.apache.commons.math.optimization.linear._\n"+
                    "import org.apache.commons.math.optimization.univariate._\n"+
                    "import org.apache.commons.math.random._\n"+
                    "import org.apache.commons.math.special._\n"+
                    "import org.apache.commons.math.stat._\n"+
                    "import org.apache.commons.math.stat.clustering._\n"+
                    "import org.apache.commons.math.stat.correlation._\n"+
                    "import org.apache.commons.math.stat.descriptive._\n"+
                    "import org.apache.commons.math.stat.descriptive.moment._\n"+
                    "import org.apache.commons.math.stat.descriptive.rank._\n"+
                    "import org.apache.commons.math.stat.descriptive.summary._\n"+
                    "import org.apache.commons.math.stat.inference._\n"+
                    "import org.apache.commons.math.stat.ranking._\n"+
                    "import org.apache.commons.math.stat.regression._\n"+
                    "import org.apache.commons.math.transform._\n"+
                    "import org.apache.commons.math.util._\n"+
            
                    "import edu.jas.arith._  \n"+
                    "import edu.jas.poly._  \n"+
                    "import edu.jas.integrate._  \n";
            
            
            
            
                // facilitates the use of the basic ScalaLab libraries from Java code
    static public    String  basicImportsJava =
                     "import java.awt.*; \n"+
                    "import java.awt.event.*; \n"+
                    "import java.lang.Math.*; \n"+
                    "import javax.swing.*; \n"+   // Java standard UI and graphics support
                    "import javax.swing.event.*; \n"+
                    "import java.util.Vector; \n"+
                    "import numal.*; \n"+    // numerical analysis library routines
                    "import static numal.Linear_algebra.*;\n"+
                    "import scalaSci.math.plot.plot.*;\n"+     // plotting routines
                    "import static scalaSci.math.plot.plot.*; \n"+
                    "import static scalaSci.math.plot.plotTypes.*; \n"+
                    "import scalaSci.math.io.MatIO.*; \n"+   // support for .mat Matlab files
                    "import scalaSci.math.plot.*;\n"+
                    "import scalaSci.math.plot.canvas.*; \n"+
                    "import scalaSci.math.plot.render.*; \n"+
                    "import scalaSci.math.plot.*;\n";
            

    
            
            
                    
            
   static public String helperImport =
             "import scalaExec.Interpreter.importHelper._;\n";  // essential imporet to "bootstrap";
    
            
   // common imports used independently of the Matrix type that ScalaLab uses
    static public String commonImports = 
           "import _root_.java.awt.Color ; \n" +   // Java standard UI and graphics support
            "import scalaExec.Interpreter.importHelper._;\n"+
            "import java.lang.Math._; \n"+
            
            "import scala.concurrent.Future\n" +
            "import scala.concurrent.ExecutionContext.Implicits.global\n"+

            "import _root_.scalaSci.ILapack._ \n"+
 
             "import _root_.scalaSci.MTJ.BandMat\n"+
            
            "import _root_.scalaSci.math.array.DoubleArray._ \n"+

            "import _root_.scalaExec.Interpreter.MatlabConnection._ \n"+
            "import _root_.scalaExec.Interpreter.MatlabComplex \n"+
            
            "import _root_.scalaExec.Interpreter.SciLabConnection._ \n"+
            
            
          "import javax.swing._; \n"+   // Java standard UI and graphics support
         "import javax.swing.event._; \n"+
         
            "import java.text.DecimalFormat \n"+
            "import System.out._ \n"+
            
            "import _root_.scalaSci.math.plot.canvas._; \n"+
            "import _root_.scalaSci.math.plot.plotObjects._; \n"+
            "import _root_.scalaSci.math.plot.plots._; \n"+
            "import _root_.scalaSci.math.plot.plot._;\n "+     // plotting routines
            "import _root_.scalaSci.math.plot.plotTypes._;\n "+     // plotting routines
            "import _root_.scalaSci.math.plot.PlotController;\n"+

            "import  scalaSci.math.plot.SyntaxHelper._; \n"+
            
            "import _root_.scalaSci.math.plot.ComplexArrayPlots._; \n"+
          
            "import _root_.scalaSci.Complex;\n"+
            "import _root_.scalaSci.Complex._; \n"+
            
            
            "import _root_.scalaSci.ComplexMatrix;\n"+
            "import _root_.scalaSci.ComplexMatrix._; \n"+
            
            "import _root_.scalaSci.math.plot.namedPlotsObj._;\n"+
            "import _root_.scalaSci.Vec ; \n "+
            "import _root_.scalaSci.Vec._ ; \n "+
            "import _root_.scalaSci.Matrix ; \n"+
            "import _root_.scalaSci.Matrix._ ; \n"+
            "import _root_.scalaSci.Sparse; \n"+
            "import _root_.scalaSci.Sparse._ ; \n"+
            "import _root_.scalaSci.CCMatrix ; \n"+
            "import _root_.scalaSci.CCMatrix._ ; \n"+
            "import _root_.java.util.Random; \n"+

            "import _root_.scalaSci.RichNumber; \n "+
            "import _root_.scalaSci.RichDouble2DArray; \n "+
            "import _root_.scalaSci.RichDouble2DArray._; \n "+
            "import _root_.scalaSci.RichDouble1DArray ; \n "+
            "import _root_.scalaSci.RichDouble1DArray._ ; \n "+
            
            "import _root_.scalaSci.RD2D \n "+
            "import _root_.scalaSci.RD1D \n "+
            "import _root_.scalaSci.D1D \n "+
            "import _root_.scalaSci.D2D \n "+
            "import _root_.scalaSci.RD2D \n "+
            
            "import _root_.scalaSci.MatlabRange._\n"+
            
            "import _root_.scalaSci.StaticScalaSciGlobalExt ; \n "+
            
            "import  _root_.scalaSciCommands.BasicCommands \n  "+// + // support for ScalaSci's console commands
            "import  _root_.scalaSciCommands.BasicCommands._; \n  "+// + // support for ScalaSci's console commands
            
            "import _root_.scalaSciCommands.FileOps \n"+
            "import _root_.scalaSciCommands.FileOps._ \n"+
            "import _root_.scalaSci.math.io.MatIO._ ; \n"+  // Matlab .mat compatibility
            "import _root_.scalaSci.math.io.ioUtils._ ; \n"+  //   load / save ASCII files etc.
            
             "import  _root_.JFplot._;\n "+
             "import _root_.JFplot.jFigure._;\n"+
             
            "import _root_.scalaSci.math.plot.PlotFunctional._ \n"+
            "import _root_.scalaSci.math.plot.PlotAdaptiveFunctional._ \n"+
 
             "import scala.util.control.Breaks._; \n"+

            "import scalaSci.JBLAS.JBLASNativeJavaInterface._\n"+
            "import _root_.scalaSci.Bench.bench \n"+
            "import _root_.scalaSci.Bench.timed \n"+
            
            "import _root_.scalaSci.math.array.StatisticSample; \n"+
            "import _root_.scalaSci.math.array.StatisticSample._; \n"+
            
            "import _root_.scalaSci.FFT.FFTScala._ \n"+
            
              "import scalaxy.loops._ \n"+
            "import _root_.scalaSci.sound.SoundUtils._ \n"+
    
            "import _root_.scalaExec.Interpreter.ConcurrencyUtils._ \n"+
            
            "import _root_.scalaUtils.ScalaUtilitiesObject._ \n"+
            
            "import _root_.net.ScalaLabNet._; \n"+
            "import _root_.net.NetSVD._; \n"+

            "import _root_.scalaSci.asynch._\n"+
            "import _root_.scalaSci.asynch.asynchEig._\n"+
            "import _root_.scalaSci.asynch.asynchfft._\n"+
            "import _root_.scalaSci.asynch.asynchInv._ \n"+
            "import _root_.scalaSci.asynch.asynchSolve._\n"+
            "import _root_.scalaSci.asynch.asynchSvd._\n"+
            "import _root_.scalaSci.asynch.asynchMul._\n"+

             "import scala.language.postfixOps \n"+

            // some short types
            "type  A1D = Array[Double]\n"+
            "type  A2D = Array[Array[Double]]\n"+
            "type  R1D = RichDouble1DArray\n"+
             "type R2D = RichDouble2DArray\n";   
  
    

    // these are intended for standalone applications that make use of the ScalaLab libraries
    static public String standAloneImports = 
           "import _root_.java.awt.Color ; \n" +   // Java standard UI and graphics support
            "import scalaExec.Interpreter.importHelper._;\n"+
            "import java.lang.Math._; \n"+

            "import _root_.scalaSci.ILapack._ \n"+
 
             "import _root_.scalaSci.MTJ.BandMat\n"+

          "import javax.swing._; \n"+   // Java standard UI and graphics support
         "import javax.swing.event._; \n"+
         
            "import java.text.DecimalFormat \n"+
            "import System.out._ \n"+
            
            "import _root_.scalaSci.math.plot.canvas._; \n"+
            "import _root_.scalaSci.math.plot.plotObjects._; \n"+
            "import _root_.scalaSci.math.plot.plots._; \n"+
            "import _root_.scalaSci.math.plot.plot._;\n "+     // plotting routines
            "import _root_.scalaSci.math.plot.plotTypes._;\n "+     // plotting routines
            "import _root_.scalaSci.math.plot.PlotFunctional._;\n"+
            "import _root_.scalaSci.math.plot.PlotAdaptiveFunctional._;\n"+
            "import _root_.scalaSci.math.plot.PlotController;\n"+

            "import  scalaSci.math.plot.SyntaxHelper._; \n"+
            
            "import _root_.scalaSci.math.plot.ComplexArrayPlots._; \n"+
          
           
            "import _root_.scalaSci.Complex;\n"+
            "import _root_.scalaSci.Complex._; \n"+
            
            "import _root_.scalaSci.ComplexMatrix;\n"+
            "import _root_.scalaSci.ComplexMatrix._; \n"+
            
            "import _root_.scalaSci.math.plot.namedPlotsObj._;\n"+
            "import _root_.scalaSci.Vec ; \n "+
            "import _root_.scalaSci.Vec._ ; \n "+
            "import _root_.scalaSci.Matrix ; \n"+
            "import _root_.scalaSci.Matrix._ ; \n"+
            "import _root_.scalaSci.Sparse; \n"+
            "import _root_.scalaSci.Sparse._ ; \n"+
            "import _root_.scalaSci.CCMatrix ; \n"+
            "import _root_.scalaSci.CCMatrix._ ; \n"+
            "import _root_.java.util.Random; \n"+

            "import _root_.scalaSci.RichNumber; \n "+
            "import _root_.scalaSci.RichDouble2DArray; \n "+
            "import _root_.scalaSci.RichDouble2DArray._; \n "+
            "import _root_.scalaSci.RichDouble1DArray ; \n "+
            "import _root_.scalaSci.RichDouble1DArray._ ; \n "+
            
            "import _root_.scalaSci.RD2D \n "+
            "import _root_.scalaSci.RD1D \n "+
            "import _root_.scalaSci.D1D \n "+
            "import _root_.scalaSci.D2D \n "+
            "import _root_.scalaSci.RD2D \n "+
            
            "import _root_.scalaSci.MatlabRange._\n"+
            
            "import _root_.scalaSci.StaticScalaSciGlobalExt ; \n "+
            
            "import  _root_.scalaSciCommands.BasicCommands \n  "+// + // support for ScalaSci's console commands
            "import  _root_.scalaSciCommands.BasicCommands._; \n  "+// + // support for ScalaSci's console commands
            
            "import _root_.scalaSciCommands.FileOps \n"+
            "import _root_.scalaSciCommands.FileOps._ \n"+
            "import _root_.scalaSci.math.io.MatIO._ ; \n"+  // Matlab .mat compatibility
            "import _root_.scalaSci.math.io.ioUtils._ ; \n"+  //   load / save ASCII files etc.
            
             "import  _root_.JFplot._;\n "+
             "import _root_.JFplot.jFigure._;\n"+
             
             "import scala.util.control.Breaks._; \n"+

            "import scalaSci.JBLAS.JBLASNativeJavaInterface._\n"+
            "import  _root_.scalaSci.Bench.bench \n"+
            
            "import _root_.scalaSci.math.array.StatisticSample; \n"+
            "import _root_.scalaSci.math.array.StatisticSample._; \n"+
            
            "import _root_.scalaSci.Mat ; \n"+ 
            "import _root_.scalaSci.Mat._ ; \n"+ 
            "import _root_.scalaSci.StaticMaths._ ; \n";
            
            
                    

    static public    String  basicImportsScala = 
             commonImports+
            "import _root_.scalaSci.Mat ; \n"+ 
            "import _root_.scalaSci.Mat._ ; \n"+ 
            "import _root_.scalaSci.StaticMaths._ ; \n";
                    
         
    static public    String  basicImportsEJMLScala = 
            commonImports+
            "import _root_.scalaSci.EJML.Mat ; \n"+ 
            "import _root_.scalaSci.EJML.BMat ; \n"+ 
            "import _root_.scalaSci.EJML.Mat._ ; \n"+ 
            "import _root_.scalaSci.EJML.BMat._ ; \n"+ 
             "import _root_.scalaSci.EJML.StaticMathsEJML._ ; \n";
            

    
    
    static public    String  basicImportsJBLASScala = 
            commonImports+
            "import _root_.scalaSci.JBLAS.Mat; \n"+
            "import _root_.scalaSci.JBLAS.Mat._; \n"+
            "import _root_.scalaSci.JBLAS.StaticMathsJBLAS._\n";
                    
            
                   
    static public    String  basicImportsCommonMathsScala = 
            commonImports+
           "import _root_.scalaSci.CommonMaths.Mat ; \n"+ 
           "import _root_.scalaSci.CommonMaths.Mat._ ; \n"+ 
           "import _root_.scalaSci.CommonMaths.StaticMathsCommonMaths._ ; \n";

    
    static public    String  basicImportsD2Das1DScala = 
            commonImports+
           "import _root_.scalaSci.D2Das1DMat ; \n"+ 
           "import _root_.scalaSci.StaticMathsD2Das1D._; \n";
             
    static public    String  basicImportsMTJScala = 
            commonImports+
           "import _root_.scalaSci.MTJ.Mat ; \n"+ 
           "import _root_.scalaSci.MTJ.Mat._ ; \n"+ 
           "import _root_.scalaSci.MTJ.StaticMathsMTJ._ ; \n";
           
        
        static public Image  scalaImage;
        static public Image  smallScalaImage;
        
        static public java.text.DecimalFormat  scalalabNumberFormat = new java.text.DecimalFormat("0.000");
	
        static public String  smallNameFullPackageSeparator = "-->";  // separates small Java  method/class names from their full package specifiers for getting help with only the small name
        
        static  public String defaultConsoleFontName = "Times New Roman";
        static  public String  defaultConsoleFontSize = "12";

        static  public String paneFontName = "DejaVu Sans Mono";   // this font seems to work more reliably
        static  public String rsyntaxFontName = "DejaVu Sans Mono";
        static  public int paneFontSize = 14;
        static  public int rsyntaxFontSize = 14;
        static  public boolean paneFontSpecified = true;
        static  public boolean rsyntaxteztareaFontSpecified = true;
        
        // for main menus
        static  public String uiFontName = "Times New Roman";
        static  public String  uiFontSize = "14";
        static public Font uifont = new Font(uiFontName, Font.PLAIN, Integer.parseInt(uiFontSize));
    
        // for popup menus
        static  public String puiFontName = "Times New Roman";
        static  public String  puiFontSize = "14";
        static public Font puifont = new Font(puiFontName, Font.PLAIN, Integer.parseInt(puiFontSize));
    
        // for rest gui drawing
        static  public String guiFontName = "Times New Roman";
        static  public String  guiFontSize = "14";
        static public Font guifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(guiFontSize));
    
        // for buttons drawing
        static  public String buiFontName = "Times New Roman";
        static  public String  buiFontSize = "14";
        static public Font buifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(buiFontSize));
    
        // for Help html
        static  public String htmlFontName = "Times New Roman";
        static  public String htmlFontSize = "16";
        static public Font htmlfont = new Font(htmlFontName, Font.PLAIN, Integer.parseInt(htmlFontSize));
    
        static  public String outConsoleFontName = "Times New Roman";
        static  public String  outConsoleFontSize = "14";

        
        static public String detailHelpStringSelected="";
        static public boolean  extensionClassPreload = true;  // preload extension classes into internal
                                                                            // cache to avoid loading them later on demand
        static public boolean loadAlwaysClassesFromSystemPath = true;   // load classes from system path even if preloaded enabled
        static public boolean  loadJarClasses = true;  // load classes from .jar archive
        
        static public boolean hostIsUnix = File.pathSeparatorChar == ':'?  true  :  false;   // Unix like system or Windows?
        static public boolean hostIsWin = !hostIsUnix;  // host is Windows
        static public boolean hostIsWin64 = hostIsWin && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsLinux = System.getProperty("os.name").toLowerCase().contains("linux");
        static public boolean hostIsLinux64 = hostIsLinux && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsMac =    System.getProperty("os.name", "").toLowerCase().contains("mac");
        static public boolean hostIs64bit = hostIsWin64 || hostIsMac || hostIsLinux64;
        static public boolean hostIsFreeBSD = System.getProperty("os.name").toLowerCase().contains("freebsd");
        static public boolean hostIsSolaris = System.getProperty("os.name").toLowerCase().contains("sunos");
        
        static public boolean hostNotWinNotLinux = ( (hostIsUnix==true)  &&  (hostIsLinux==false) ); // Unix-like OS, not Linux  e.g. FreeBSD, MacOS, Solaris etc.
        
      
    
        static public String jarFilePath="";  // the path that contains the main jar file
        static public String fullJarFilePath="";
        static public String scalalabLibPath="";
        static public String scalalabHelpPath="";
        
        
        
  // ScalaSciClassPath is the directory that serves as the "root" for Scala Class Loader class retrieval. 
 // This directory and all its subdirectories are searched for both .Scala script files and .class files        
        static public String ScalaSciClassPath=""; 
        static public Vector<String>  ScalaSciClassPathComponents=new Vector <String>();
        
    // these variables keep the user preferable toolboxes that can be saved during a working session.
   // these toolboxes can then be automatically reloaded
        static public Vector <String>  ScalaSciToolboxes = new Vector <String> ();
        static public Vector <String>  ScalaSciUserPaths = new Vector <String> ();
        
        static public Vector  <String> favouriteElements = new Vector <String> ();


        
        static public String scalalabPropertiesFile;  // the file for obntaining configuration properties
        static public String homeDir;  // the user's home directory
        
        static public HashMap<String, Boolean>  jartoolboxesLoadedFlag;  // associates each jar toolbox name with a flag that indicates whether it was loaded or not
        ////static public JarClassLoader ScalaToolboxesLoader;  // the loader that handles the classes supplied with toolboxes
        
        static public int sizeX = 600;  // the scalaLab's main console window jFrame size
        static public int sizeY = 400;  
        static public int locX = 100;   // location of scalaLab's main window
        static public int locY = 100; 
        
        static public int sizeConsoleX = 600;  // the scalaLab's output console window jFrame size
	static public int sizeConsoleY = 100;  
        static public int locConsoleX = 100;   // location of scalaLab's main window
        static public int locConsoleY = 800; 
        
        static public double figAreaRelSize = 0.9;  // the relative area of the figure plot area
        
    static public int threadCnt = 0;  // the number of threads created

    static public int maxNumberOfRecentFiles = 20;
    static public String scalaLabRecentFilesList = "scalaLabRecentFiles.txt";   // the file for storing list of recent files
    
    static public boolean displayDirectory = false; // controls the displaying of working directory at the prompt
    static public char scalalabPromptChar = '#';  // used to display the command prompt
    static public String scalalabPromptString = scalalabPromptChar+" ";
    static public String scalaCommandPromptString =  "scala >";
    
    static public JarClassLoader ScalaToolboxesLoader = null;
    
    public static TreeSet [] loadedClassesOfToolboxes;  // keeps track of loaded classes from all toolboxes
    public static String [] loadedToolboxesNames;  // the names of the toolboxes
    public static int currentToolboxId = 0; 
    public static int maxNumOfToolboxes = 30;

    
      
    
    // Graphics Configuration
    public static int  maxPointsToPlot = 40;  // limit on the number of points to plot when in point plot mode
    public static int  plotPointWidth = 2;    // control the size of the point at the plots 
    public static int  plotPointHeight =2; 
    public static int  markLineSize = 5;
    public static int  figGridSizeX = 30; 
    public static int  figGridSizeY = 30;     
    public static int  figFrameSizeX = 800; 
    public static int  figFrameSizeY = 600;     
    public static int  limitForLargeRangeOfValues = 10;
    public static double figZoomScaleFactor = 0.5;
    public static int currentMaxNumberOfZooms = 5;

         // the tabs of the main UI 
    public static final int mainTab = 0;
    public static final int javaHelpTab = 1;
    public static final int scalaHelpTab = 2;
    public static final int scalaSciTab = 3;

    public static Dimension ScreenDim;

    static public String scalalabFavoritePathsFile = "scalalabFavoritePaths.log";
    
    static public String DirHavingFile;  // directory having the currently requested file
    static public Properties settings;  // for load/save global properties
    static public String selectedStringForAutoCompletion;
    
    public static AutoCompletionScalaSci  autoCompletionScalaSci;    // structures for supporting autocompletion for ScalaSci
    public static AutoCompletionScalaSciLoader  autoCompletionScalaSciLoader;  // loader of ScalaSci classes for autocompletion
    
    public static AutoCompletionWorkspace autoCompletionWorkspace = new AutoCompletionWorkspace();
    
    public static int numTokTypes = 0;   // counts the number of token types that the lexical analyzer returns
      
    // keep main objects
    public static scalaLab   scalalabMainFrame = null;
    public static JFrame jfExplorerFrame = null;
    public static JFrame varBindingFrame = null;
    public static int xSizeTab;
    public static int ySizeTab;
    public static AutoCompletionFrame autoCompletionFrame = null;
    public static  scalalabEditor  myEdit = null;
    public static scalaExec.gui.Console  userConsole;         //  used to retrieve the text buffer
    
    public static boolean effectsEnabled=false;
    
    public static SysUtils.ConsoleWindow  consoleOutputWindow;
    public static int lastIndexOfOutput=0;
    public static String assemblyFileName;
    public static boolean retrieveAlsoMethods=false;
    
    // read the user defined Scala interpreter classpath components 
    public static void readUserPaths() {
     try {
      File file = new File(GlobalValues.userPathsFileName);  // the file name that keeps the user paths
      FileReader fr = new FileReader(file);
      BufferedReader in = new BufferedReader(fr);
      String currentLine;
      GlobalValues.ScalaSciClassPathComponents.clear();
      
      while ( (currentLine = in.readLine())!= null)  {
          if (GlobalValues.ScalaSciClassPathComponents.contains(currentLine)==false)
             GlobalValues.ScalaSciClassPathComponents.add(currentLine);
       }
     }
            catch (IOException ioe) {
                System.out.println("Exception trying to read "+GlobalValues.userPathsFileName);
                 return;
            }
        
    }
    
    
    public static void writeUserPaths() {
    
        StringBuffer sb = new StringBuffer();
        
        // handle any specified additional user specified paths
        if (GlobalValues.ScalaSciClassPathComponents !=null)  {
            int userSpecPathsCnt = GlobalValues.ScalaSciClassPathComponents.size();
            String userPathsSpecString="";
            for (int k=0; k<userSpecPathsCnt; k++) {
                String currentToolbox  = GlobalValues.ScalaSciClassPathComponents.elementAt(k).toString().trim();
                sb.append(currentToolbox+"\n");
             }
          }
         
        try {
                // take the program's text and save it to a temporary file
                File tempFile = new File(GlobalValues.userPathsFileName);
                FileWriter fw = new FileWriter(tempFile);
                fw.write(sb.toString(), 0, sb.length());
                fw.close();
             }
            catch (IOException ioe) {
                System.out.println("Exception trying to write ScalaLab user paths ");
                 return;
            }
    }
    
    
    public static void initGlobals()
    {
       if (Desktop.isDesktopSupported()) 
            desktop = Desktop.getDesktop();
        else
            useSystemBrowserForHelp = false;  // cannot use system browser
        
        Toolkit tk =Toolkit.getDefaultToolkit();
        desktophints = (Map)(tk.getDesktopProperty("awt.font.desktophints"));

        GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
        GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));

        GlobalValues.workingDir = System.getProperty("user.dir");
        java.util.Map<String, String>  userEnv = System.getenv();

        GlobalValues.homeDir = userEnv.get("HOMEPATH");
          if (GlobalValues.homeDir == null)
              GlobalValues.homeDir = GlobalValues.workingDir; // perhaps old versions of Windows do not have a home path
        
        if (GlobalValues.workingDir.indexOf("Windows")!=-1)  // avoid using Windows system directory
            GlobalValues.workingDir = GlobalValues.homeDir;

        GlobalValues.DirHavingFile = GlobalValues.workingDir;
        ScalaSciClassPathComponents = new Vector();
        
        GlobalValues.ScalaSciUserPaths.add(GlobalValues.homeDir);
        
        ScalaSciToolboxes = new Vector();
        
        hostIsUnix = true;
        if (File.separatorChar!='/') 
            hostIsUnix=false;
        
        myEdit = null;
        loadedClassesOfToolboxes = new TreeSet[maxNumOfToolboxes];
        loadedToolboxesNames = new String[maxNumOfToolboxes];
        
        jartoolboxesLoadedFlag = new HashMap<String, Boolean>();

        String  prefix = "/";
        if (hostIsUnix==false)
            prefix = "C:\\";

        String initialScalalabPath  =prefix;
        
        boolean foundConfigFileFlag = false;   //exists configuration file?
     try
        {  
           settings = new Properties();
       
           FileInputStream in = null;
           
           String configFileName = workingDir+File.separatorChar+"scalalab.props";
           File configFile = new File(configFileName);
           if (configFile.exists())   {
                  in = new FileInputStream(configFile);
                  settings.load(in);
                  foundConfigFileFlag = true;
                  GlobalValues.scalalabPropertiesFile = configFileName;
                  }
           }

        catch (IOException e) 
        {
           e.printStackTrace();
        }
         ScreenDim = Toolkit.getDefaultToolkit().getScreenSize();

         if (foundConfigFileFlag == false)   { // configuration file not exists, thus pass default configuration
        
             // pass default properties
           //position the frame in the centre of the screen
                int xSizeMainFrame = (int)((double)ScreenDim.width/1.4);
                int ySizeMainFrame = (int)((double)ScreenDim.height/1.1);
                GlobalValues.locX  = (int)((double)ScreenDim.width/10.0);  
                GlobalValues.locY = (int)((double)ScreenDim.height/10.0); 
               GlobalValues.sizeX = xSizeMainFrame;
                GlobalValues.sizeY = ySizeMainFrame;
                
                Rectangle  b = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                GlobalValues.rWidth = (b.width / 2)-20;
                GlobalValues.rHeight = b.height * 5 / 6;
                   // default x-location to the right of  the main ScalaLab frame
                GlobalValues.rlocX = GlobalValues.locX+ GlobalValues.sizeX;
                   // default y-location the same as the main ScalaLab frame
                GlobalValues.rlocY = GlobalValues.locY;
        
                
         if (GlobalValues.compilerOptimizationFlag == true)
          settings.put("compilerOptimizationProp", "true");
         else
           settings.put("compilerOptimizationFlag", "false");
         
        settings.put("TargetJVMIndexProp",   Integer.toString(GlobalValues.currentTargetSelectionIndex ));
        settings.put("widthProp",  String.valueOf(xSizeMainFrame));
        settings.put("heightProp", String.valueOf(ySizeMainFrame));
        settings.put("xlocProp",  String.valueOf(GlobalValues.locX));
        settings.put("ylocProp",  String.valueOf(GlobalValues.locY));
        settings.put("rwidthProp",  String.valueOf(rWidth));
        settings.put("rheightProp", String.valueOf(rHeight));
        settings.put("rxlocProp",  String.valueOf(rlocX));
        settings.put("rylocProp",  String.valueOf(rlocY));

        // append homedir to the initial scalaSci classpath
        String userDir = workingDir;


        settings.put("ScalaSciToolboxesProp", "");   // the property that keeps the user toolboxes
        
        settings.put("scalalabWorkingDirProp", userDir);
        
        settings.setProperty("loadScalaSciAutoCompletionProp", "true");
        settings.setProperty("latexDisplayingProp", "true");
        } // configuration file not exists
             
        // init Computer Algebra
         F.initSymbols();
         symUtil = new EvalUtilities();
         EvalEngine EVAL_ENGINE = null;
         texUtil = new TeXUtilities(EVAL_ENGINE);
    }   

    
    
    public static void incrementToolboxCount() {
        currentToolboxId++;
        if (currentToolboxId > maxNumOfToolboxes)  { 
            JOptionPane.showMessageDialog(null, "Maximum toolbox count exceeded", "Cannot load additional toolboxes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
// pass properties readed from settings Property String to the scalaLab workspace structures
    public static void passPropertiesFromSettingsToWorkspace(Properties settings)
     {
         
          String watchVariablesSetting = settings.getProperty("watchVariablesProp");
         if (watchVariablesSetting != null) {
             GlobalValues.watchVariablesFlag = Boolean.parseBoolean(watchVariablesSetting);
                  }
                  else {
             GlobalValues.watchVariablesFlag = false;
                  }
      
         

         String IPServerProperty = settings.getProperty("serverIPProp");
         if (IPServerProperty != null)
             GlobalValues.serverIP = IPServerProperty;
         

         String rsyntaxInScalaGlobalModeSetting = settings.getProperty("rsyntaxInScalaCompletionModeProp");
         if (rsyntaxInScalaGlobalModeSetting != null)
          if (rsyntaxInScalaGlobalModeSetting.equalsIgnoreCase("Scala")) {
              GlobalValues.rsyntaxInScalaCompletionMode = true;
              GlobalValues.rsyntaxInScalaCompletionModeProp = "Scala";
          }
          else {
              GlobalValues.rsyntaxInScalaCompletionMode = false;
                GlobalValues.rsyntaxInScalaCompletionModeProp = "Global";
          }
         
         String displayLatexSetting = settings.getProperty("latexDisplayingProp");
         if (displayLatexSetting != null)
             if (displayLatexSetting != null)
                 if (displayLatexSetting.equalsIgnoreCase("true"))
                     GlobalValues.displayLatexOnEval = true;
                 else
                     GlobalValues.displayLatexOnEval = false;
         
         String  mouseMotionJSyntaxSetting = settings.getProperty("mouseMotionListenerForJSyntaxProp");
         if (mouseMotionJSyntaxSetting  != null)
           if (mouseMotionJSyntaxSetting.equalsIgnoreCase("true"))
              GlobalValues.mouseMotionListenerForJSyntax = true;
           else
              GlobalValues.mouseMotionListenerForJSyntax = false;
         
         
         String  mouseMotionRSyntaxSetting = settings.getProperty("mouseMotionListenerForRSyntaxProp");
         if (mouseMotionRSyntaxSetting  != null)
           if (mouseMotionRSyntaxSetting.equalsIgnoreCase("true"))
              GlobalValues.mouseMotionListenerForRSyntax = true;
           else
              GlobalValues.mouseMotionListenerForRSyntax = false;
  
         String  mouseMotionForAllJSyntaxSetting = settings.getProperty("getValuesForAllJSyntaxProp");
         if (mouseMotionForAllJSyntaxSetting  != null)
           if (mouseMotionForAllJSyntaxSetting.equalsIgnoreCase("true"))
              GlobalValues.getValuesForAllJSyntax  = true;
           else
              GlobalValues.getValuesForAllJSyntax  = false;
         
         
         String  mouseMotionForAllRSyntaxSetting = settings.getProperty("getValuesForAllRSyntaxProp");
         if (mouseMotionForAllRSyntaxSetting  != null)
           if (mouseMotionForAllRSyntaxSetting.equalsIgnoreCase("true"))
              GlobalValues.getValuesForAllRSyntax  = true;
           else
              GlobalValues.getValuesForAllRSyntax  = false;
         
         String compilerOptimizationSetting = settings.getProperty("compilerOptimizationProp");
         if (compilerOptimizationSetting!=null)
           if (compilerOptimizationSetting.equalsIgnoreCase("true"))
              GlobalValues.compilerOptimizationFlag = true;
           else
              GlobalValues.compilerOptimizationFlag = false;
         
         String   targetJVMIndexStr = settings.getProperty("TargetJVMIndexProp");
         if (targetJVMIndexStr!= null) {
             GlobalValues.currentTargetSelectionIndex = Integer.parseInt(targetJVMIndexStr);
         }
                 
        
         String startupHelpStr = settings.getProperty("startupHelpProp");
         if (startupHelpStr == null ) startupHelpStr = "startupHelpOn";
         if (startupHelpStr.equalsIgnoreCase("startupHelpOn"))
             GlobalValues.startupHelpFlag = true;
         else
             GlobalValues.startupHelpFlag = false;
         
        String specifiedDir =  settings.getProperty("scalalabWorkingDirProp");         
        if (specifiedDir != null) workingDir = specifiedDir;
        
        //  Decimal digit formatting properties
        String vecDigitsSetting = settings.getProperty("VecDigitsProp");
        if (vecDigitsSetting != null) {
            int vprec = Integer.parseInt(vecDigitsSetting);
            scalaSci.PrintFormatParams.setVecDigitsPrecision(vprec);
          }
         
        String matDigitsSetting = settings.getProperty("MatDigitsProp");
        if (matDigitsSetting != null) {
            int mprec = Integer.parseInt(matDigitsSetting);

            scalaSci.PrintFormatParams.setMatDigitsPrecision(mprec);
        }
         
         String mxRowsSetting = settings.getProperty("mxRowsProp");
         if (mxRowsSetting != null) {
            int mxrows = Integer.parseInt(mxRowsSetting);
            scalaSci.PrintFormatParams.setMatMxRowsToDisplay(mxrows);
         }
        
         String mxColsSetting = settings.getProperty("mxColsProp");
         if (mxColsSetting != null)  {
            int mxcols = Integer.parseInt(mxColsSetting);
            scalaSci.PrintFormatParams.setMatMxColsToDisplay(mxcols);
         }
         
         String verboseOutputSetting = settings.getProperty("verboseOutputProp");
         if (verboseOutputSetting!=null)
           if (verboseOutputSetting.equalsIgnoreCase("true"))
              scalaSci.PrintFormatParams.setVerbose(true);
           else
              scalaSci.PrintFormatParams.setVerbose(false);
         
        // main frame location and size 
       String locXstr = settings.getProperty("xlocProp");
        if (locXstr != null)      locX = Integer.parseInt(locXstr);  // locX specified
        String locYstr = settings.getProperty("ylocProp");
        if (locYstr != null)      locY = Integer.parseInt(locYstr);  // locY specified
        if (GlobalValues.scalalabMainFrame != null)
            GlobalValues.scalalabMainFrame.setLocation(locX, locY);   // update the location of the scalalab's main frame according to the configuration settings

        String sizeXstr = settings.getProperty("widthProp");
        if (sizeXstr != null)      sizeX = Integer.parseInt(sizeXstr);  // sizeX specified
        String sizeYstr = settings.getProperty("heightProp");
        if (sizeYstr != null)      sizeY = Integer.parseInt(sizeYstr);  // sizeY specified
        if (GlobalValues.scalalabMainFrame != null)
            GlobalValues.scalalabMainFrame.setSize(sizeX, sizeY);   // update the size of the scalalab's main frame according to the configuration settings
        GlobalValues.scalalabMainFrame.xSizeMainFrame = sizeX;
        GlobalValues.scalalabMainFrame.ySizeMainFrame = sizeY;
        
        // rsyntaxFrame location and size
        // track the location and size of the rSyntaxArea window
       String rlocXstr = settings.getProperty("rxlocProp");
        if (rlocXstr != null)      rlocX = Integer.parseInt(rlocXstr);  // rlocX specified
        String rlocYstr = settings.getProperty("rylocProp");
        if (rlocYstr != null)      rlocY = Integer.parseInt(rlocYstr);  // rlocY specified
        if (GlobalValues.globalRSyntaxFrame != null)
            GlobalValues.globalRSyntaxFrame.setLocation(rlocX, rlocY);   // update the location of the rsyntaxFrame according to the configuration settings

        String rsizeXstr = settings.getProperty("rwidthProp");
        if (rsizeXstr != null)      rWidth = Integer.parseInt(rsizeXstr);  // rWidth specified
        String rsizeYstr = settings.getProperty("rheightProp"); 
        if (rsizeYstr != null)      rHeight  = Integer.parseInt(rsizeYstr);  // rHeight specified
        if (GlobalValues.globalRSyntaxFrame != null)
            GlobalValues.globalRSyntaxFrame.setSize(rWidth, rHeight);   // update the size of the rSyntaxFrame according to the configuration settings
        
        
       String sizeConsoleXstr = settings.getProperty("widthConsoleProp");
        if (sizeConsoleXstr != null)      sizeConsoleX = Integer.parseInt(sizeConsoleXstr);  // sizeX Console specified
        String sizeConsoleYstr = settings.getProperty("heightConsoleProp");
        if (sizeConsoleYstr != null)      sizeConsoleY = Integer.parseInt(sizeConsoleYstr);  // sizeY Console specified
        
        String locConsoleXstr = settings.getProperty("xlocConsoleProp");
        if (locConsoleXstr != null)      locConsoleX = Integer.parseInt(locConsoleXstr);  // locX Console specified
        String locConsoleYstr = settings.getProperty("ylocConsoleProp");
        if (locConsoleYstr != null)      locConsoleY = Integer.parseInt(locConsoleYstr);  // sizeY Console specified
        
          // main menus
        String uiFontName = settings.getProperty("uiFontNameProp");
        if (uiFontName==null) uiFontName= GlobalValues.uiFontName;
        String uiFontSize = settings.getProperty("uiFontSizeProp");
        if (uiFontSize==null) uiFontSize= GlobalValues.uiFontSize;
        GlobalValues.uiFontName = uiFontName;
        GlobalValues.uiFontSize = uiFontSize;
        GlobalValues.uifont = new Font(GlobalValues.uiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.uiFontSize));
                
          // pop-up menus
        String puiFontName = settings.getProperty("puiFontNameProp");
        if (puiFontName==null)  puiFontName= GlobalValues.puiFontName;
        String puiFontSize = settings.getProperty("puiFontSizeProp");
        if (puiFontSize==null) puiFontSize= GlobalValues.puiFontSize;
        GlobalValues.puiFontName = puiFontName;
        GlobalValues.puiFontSize = puiFontSize;
        GlobalValues.puifont = new Font(GlobalValues.puiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.puiFontSize));
        
          // general GUI components
        String guiFontName = settings.getProperty("guiFontNameProp");
        if (guiFontName==null)  guiFontName= GlobalValues.guiFontName;
        String guiFontSize = settings.getProperty("guiFontSizeProp");
        if (guiFontSize==null) guiFontSize= GlobalValues.guiFontSize;
        GlobalValues.guiFontName = guiFontName;
        GlobalValues.guiFontSize = guiFontSize;
        GlobalValues.guifont = new Font(GlobalValues.guiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.guiFontSize));
        
        settings.setProperty("helpMagnificationProp", String.valueOf(GlobalValues.helpMagnificationFactor));
        
          // html components
        String htmlFontName = settings.getProperty("htmlFontNameProp");
        if (htmlFontName==null)  guiFontName= GlobalValues.htmlFontName;
        String htmlFontSize = settings.getProperty("htmlFontSizeProp");
        if (htmlFontSize==null) htmlFontSize= GlobalValues.htmlFontSize;
        GlobalValues.htmlFontName = htmlFontName;
        GlobalValues.htmlFontSize = htmlFontSize;
        GlobalValues.htmlfont = new Font(GlobalValues.htmlFontName, Font.PLAIN, Integer.parseInt(GlobalValues.htmlFontSize));
    
              // buttons components
        String buiFontName = settings.getProperty("buiFontNameProp");
        if (buiFontName==null)  buiFontName= GlobalValues.buiFontName;
        String buiFontSize = settings.getProperty("buiFontSizeProp");
        if (buiFontSize==null) buiFontSize= GlobalValues.buiFontSize;
        GlobalValues.buiFontName = buiFontName;
        GlobalValues.buiFontSize = buiFontSize;
        GlobalValues.buifont = new Font(GlobalValues.buiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.buiFontSize));
        
        String outConsFontName = settings.getProperty("outConsFontNameProp");
        if (outConsFontName==null) outConsFontName= GlobalValues.outConsoleFontName;
        String outConsFontSize = settings.getProperty("outConsFontSizeProp");
        if (outConsFontSize==null) outConsFontSize= GlobalValues.outConsoleFontSize;
        GlobalValues.outConsoleFontName = outConsFontName;
        GlobalValues.outConsoleFontSize = outConsFontSize;
        
        
        String consoleFontName = settings.getProperty("consoleFontNameProp");
        if (consoleFontName==null) consoleFontName= GlobalValues.defaultConsoleFontName;
        String consoleFontSize = settings.getProperty("consoleFontSizeProp");
        if (consoleFontSize==null) consoleFontSize= GlobalValues.defaultConsoleFontSize;
        
        
        GlobalValues.defaultConsoleFontName = consoleFontName;
        GlobalValues.defaultConsoleFontSize = consoleFontSize;
            
        // jsyntaxpane font
        boolean paneFontSpecified = true;
        String paneFontName = settings.getProperty("paneFontNameProp");
        if (paneFontName!=null)  
             GlobalValues.paneFontName = paneFontName;
        else
            paneFontSpecified = false;
        String paneFontSize = settings.getProperty("paneFontSizeProp");
        if (paneFontSize!=null)   
            GlobalValues.paneFontSize =  Integer.valueOf(paneFontSize);
        else
            paneFontSpecified = false;
      
        
        // rsyntaxtextarea font
        boolean rsyntaxFontSpecified = true;
        String rsyntaxFontName = settings.getProperty("rsyntaxFontNameProp");
        if (rsyntaxFontName!=null)  
             GlobalValues.rsyntaxFontName  = rsyntaxFontName;
        else
            rsyntaxFontSpecified = false;
        String rsyntaxFontSize = settings.getProperty("rsyntaxFontSizeProp");
        if (rsyntaxFontSize!=null)   
            GlobalValues.rsyntaxFontSize  =  Integer.valueOf(rsyntaxFontSize);
        else
            rsyntaxFontSpecified = false;
      
        GlobalValues.rsyntaxteztareaFontSpecified  = paneFontSpecified;
        
        if (GlobalValues.ScalaSciUserPaths!=null) {
           GlobalValues.ScalaSciUserPaths.clear();
           GlobalValues.ScalaSciUserPaths.add(GlobalValues.homeDir);
        }
        else
           GlobalValues.ScalaSciUserPaths = new Vector();

     
        String notOverlapConsoleOutput = settings.getProperty("notOverlapTheConsoleOutput");
        if (notOverlapConsoleOutput != null)   
         if (notOverlapConsoleOutput.equalsIgnoreCase("false"))
              GlobalValues.notOverlapTheConsoleOutput = false;
         else
             GlobalValues.notOverlapTheConsoleOutput = true;
    
        
        if (scalalabMainFrame != null)  {
           scalalabMainFrame.setSize(sizeX, sizeY);
           scalalabMainFrame.setLocation(locX, locY);
           scalalabMainFrame.setFont(new Font(uiFontName, Font.PLAIN, Double.valueOf(uiFontSize).intValue()));
   }

         //  edit Frame Location and Size
        String tempLocX =  settings.getProperty("editFrameLocXProp");
        if (tempLocX != null)
            GlobalValues.editFrameLocX = tempLocX;
        
        String tempLocY =  settings.getProperty("editFrameLocYProp");
        if (tempLocY != null)
            GlobalValues.editFrameLocY = tempLocY;
        
        String tempSizeX =  settings.getProperty("editFrameSizeXProp");
        if (tempSizeX != null)
            GlobalValues.editFrameSizeX = tempSizeX;
        
        String tempSizeY =  settings.getProperty("editFrameSizeYProp");
        if (tempSizeY != null)
            GlobalValues.editFrameSizeY = tempSizeY;
        
        String explorerVisible = "false";
        String explorerVisibleProp = settings.getProperty("explorerVisibleProp");
        if (explorerVisibleProp != null)
            explorerVisible = explorerVisibleProp;
        if (explorerVisible.equalsIgnoreCase("true"))
            GlobalValues.explorerVisible = true;
        else
            GlobalValues.explorerVisible = false;
        
        
        String toolbarVisible = "false";
        String toolbarVisibleProp = settings.getProperty("toolbarVisibleProp");
        if (toolbarVisibleProp != null)
            toolbarVisible = toolbarVisibleProp;
        if (toolbarVisible.equalsIgnoreCase("true"))
            GlobalValues.mainToolbarVisible = true;
        else
            GlobalValues.mainToolbarVisible = false;
        
        String rsyntaxAreaDefault = "false";
        String rsyntaxAreaDefaultProp = settings.getProperty("rsyntaxAreaDefaultProp");
        if (rsyntaxAreaDefaultProp!=null)
             rsyntaxAreaDefault =  rsyntaxAreaDefaultProp;
        if ( rsyntaxAreaDefault.equalsIgnoreCase("true"))
            GlobalValues.preferRSyntaxEditor = true;
        else
            GlobalValues.preferRSyntaxEditor = false;
        
        // magnification factor 
        String helpMagnificationProp = settings.getProperty("helpMagnificationProp");
        if (helpMagnificationProp!= null)
            helpMagnificationFactor =  Double.parseDouble(helpMagnificationProp);
        
        String useSystemBrowserForHelpProp = settings.getProperty("useSystemBrowserForHelpProp");
        if (useSystemBrowserForHelpProp != null)
            if (useSystemBrowserForHelpProp.equalsIgnoreCase("true"))
                    GlobalValues.useSystemBrowserForHelp = true;
        else
                GlobalValues.useSystemBrowserForHelp = false;
        
        // get the default Interpreter type to create on startup from settings
        String interType = settings.getProperty("DefaultScalaInterpreterTypeProp");
        if (interType==null) {
            interType = GlobalValues.scalaInterpreterTypeProp;
            GlobalValues.interpreterTypeForPane  = Integer.parseInt(interType);
        }
        else   {
            GlobalValues.scalaInterpreterTypeProp = interType;
            GlobalValues.interpreterTypeForPane  = Integer.parseInt(interType);
          }
        
    }
     
    

    
    

// pass properties from the scalaLab workspace structures to the settings Property String to 
    public static void passPropertiesFromWorkspaceToSettings(Properties settings)
     {
         
         settings.setProperty("serverIPProp", GlobalValues.serverIP);
         

         
         
         settings.setProperty("watchVariablesProp", Boolean.toString(GlobalValues.watchVariablesFlag));
         
         if (GlobalValues.displayLatexOnEval == true)
             settings.setProperty("latexDisplayingProp", "true");
         else
            settings.setProperty("latexDisplayingProp", "false");
            
         if (GlobalValues.rsyntaxInScalaCompletionMode == true)
             settings.setProperty("rsyntaxInScalaCompletionModeProp", "Scala");
         else
             settings.setProperty("rsyntaxInScalaCompletionModeProp", "Global");
         
         if (GlobalValues.mouseMotionListenerForJSyntax == true) 
             settings.setProperty("mouseMotionListenerForJSyntaxProp", "true");
         else
             settings.setProperty("mouseMotionListenerForJSyntaxProp", "false");
             
         if (GlobalValues.mouseMotionListenerForRSyntax == true) 
             settings.setProperty("mouseMotionListenerForRSyntaxProp", "true");
         else
             settings.setProperty("mouseMotionListenerForRSyntaxProp", "false");
    
        if (GlobalValues.getValuesForAllJSyntax == true) 
             settings.setProperty("getValuesForAllJSyntaxProp", "true");
         else
             settings.setProperty("getValuesForAllJSyntaxProp", "false");
         
         if (GlobalValues.getValuesForAllRSyntax == true) 
             settings.setProperty("getValuesForAllRSyntaxProp", "true");
         else
             settings.setProperty("getValuesForAllRSyntaxProp", "false");
         
         if (GlobalValues.compilerOptimizationFlag == true) 
             settings.setProperty("compilerOptimizationProp", "true");
         else
             settings.setProperty("compilerOptimizationProp", "false");
         
         settings.setProperty("TargetJVMIndexProp", Integer.toString(GlobalValues.currentTargetSelectionIndex));
               
         if (GlobalValues.startupHelpFlag == true)
            settings.setProperty("startupHelpProp", "startupHelpOn");
         else
            settings.setProperty("startupHelpProp", "startupHelpOff");
                             
        if (GlobalValues.workingDir!=null)  settings.setProperty("scalalabWorkingDirProp", GlobalValues.workingDir);
        
        
        
        //  Decimal digit formatting properties
        
         int vprec = scalaSci.PrintFormatParams.getVecDigitsPrecision();
         settings.setProperty("VecDigitsProp", String.valueOf(vprec));
         
         int mprec = scalaSci.PrintFormatParams.getMatDigitsPrecision();
         settings.setProperty("MatDigitsProp", String.valueOf(vprec));
         
         int mxrows = scalaSci.PrintFormatParams.getMatMxRowsToDisplay();
         settings.setProperty("mxRowsProp", String.valueOf(mxrows));
         
         int mxcols = scalaSci.PrintFormatParams.getMatMxColsToDisplay();
         settings.setProperty("mxColsProp", String.valueOf(mxcols));
         
         
         if (scalaSci.PrintFormatParams.getVerbose()==true)
             settings.setProperty("verboseOutputProp", "true");
         else
             settings.setProperty("verboseOutputProp", "false");
         
           // track the location and size of the main ScalaLab window
        int width = scalalabMainFrame.getSize().width;
        int height = scalalabMainFrame.getSize().height;        
        settings.setProperty("widthProp", String.valueOf(width));
        settings.setProperty("heightProp", String.valueOf(height));
        int xloc = scalalabMainFrame.getLocation().x;
        int yloc = scalalabMainFrame.getLocation().y;
        settings.setProperty("xlocProp", String.valueOf(xloc));
        settings.setProperty("ylocProp", String.valueOf(yloc));
      
        // track the location and size of the rSyntaxArea window
          if (globalRSyntaxFrame!=null) {
        int rxloc = globalRSyntaxFrame.getLocation().x;
        int ryloc = globalRSyntaxFrame.getLocation().y;
        int rwidth = globalRSyntaxFrame.getSize().width;
        int rheight = globalRSyntaxFrame.getSize().height;
        
        
        settings.setProperty("rxlocProp", String.valueOf(rxloc));
        settings.setProperty("rylocProp", String.valueOf(ryloc));
        settings.setProperty("rwidthProp", String.valueOf(rwidth));
        settings.setProperty("rheightProp", String.valueOf(rheight));
          }
// track the location and size of the output Console  window
        
        settings.setProperty("paneFontNameProp", String.valueOf(GlobalValues.editorPane.getFont().getName()));
        settings.setProperty("paneFontSizeProp", String.valueOf(GlobalValues.editorPane.getFont().getSize())); 
        
        if (GlobalValues.globalRSyntaxEditorPane != null) {
        settings.setProperty("rsyntaxFontNameProp", String.valueOf(GlobalValues.globalRSyntaxEditorPane.getFont().getName()));
        settings.setProperty("rsyntaxFontSizeProp", String.valueOf(GlobalValues.globalRSyntaxEditorPane.getFont().getSize())); 
        }
        else {  // set the property to the same as ScalaInterpreterPane
        settings.setProperty("rsyntaxFontNameProp", String.valueOf(GlobalValues.editorPane.getFont().getName()));
        settings.setProperty("rsyntaxFontSizeProp", String.valueOf(GlobalValues.editorPane.getFont().getSize())); 
        }
        
        settings.setProperty("consoleFontNameProp", String.valueOf(scalalabMainFrame.scalalabConsole.getFont().getName()));
        settings.setProperty("consoleFontSizeProp", String.valueOf(scalalabMainFrame.scalalabConsole.getFont().getSize()));
        
        // main menus
        settings.setProperty("uiFontNameProp", GlobalValues.uifont.getName());
        settings.setProperty("uiFontSizeProp", String.valueOf(GlobalValues.uifont.getSize()));
        
        // popup menus
        settings.setProperty("puiFontNameProp", GlobalValues.puifont.getName());
        settings.setProperty("puiFontSizeProp", String.valueOf(GlobalValues.puifont.getSize()));
        
        // html help
        settings.setProperty("htmlFontNameProp", GlobalValues.htmlfont.getName());
        settings.setProperty("htmlFontSizeProp", String.valueOf(GlobalValues.htmlfont.getSize()));
        
        // rest GUI components
        settings.setProperty("guiFontNameProp", GlobalValues.guifont.getName());
        settings.setProperty("guiFontSizeProp", String.valueOf(GlobalValues.guifont.getSize()));
        
        // GUI buttons
        settings.setProperty("buiFontNameProp", GlobalValues.buifont.getName());
        settings.setProperty("buiFontSizeProp", String.valueOf(GlobalValues.buifont.getSize()));
        
        settings.setProperty("outConsFontNameProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getName()));
        settings.setProperty("outConsFontSizeProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getSize()));
        
         //  edit Frame Location and Size
        settings.setProperty("editFrameLocXProp", GlobalValues.editFrameLocX);
        settings.setProperty("editFrameLocYProp", GlobalValues.editFrameLocY);
        settings.setProperty("editFrameSizeXProp", GlobalValues.editFrameSizeX);
        settings.setProperty("editFrameSizeYProp", GlobalValues.editFrameSizeY);
        
        if (GlobalValues.explorerVisible)
          settings.setProperty("explorerVisibleProp",  "true");
        else
          settings.setProperty("explorerVisibleProp",  "false");        
        
        if (GlobalValues.mainToolbarVisible)
          settings.setProperty("toolbarVisibleProp",  "true");
        else
          settings.setProperty("toolbarVisibleProp",  "false");        
        
        if (GlobalValues.preferRSyntaxEditor)
          settings.setProperty("rsyntaxAreaDefaultProp",  "true");
        else
          settings.setProperty("rsyntaxAreaDefaultProp",  "false");        
        
        settings.setProperty("useSystemBrowserForHelpProp",  Boolean.toString(GlobalValues.useSystemBrowserForHelp));
        
        GlobalValues.scalaInterpreterTypeProp = String.valueOf(GlobalValues.interpreterTypeForPane);
        settings.setProperty("DefaultScalaInterpreterTypeProp", GlobalValues.scalaInterpreterTypeProp);
        
        
    }
 
        

 // updates the paths of targetVector by adding additionalPaths and any subdirectories
public static void updatePathVectors(Vector targetVector,  String additionalPaths, boolean recurse) {
    if (targetVector != null)  {
            StringTokenizer  tokenizer;
             tokenizer = new StringTokenizer(additionalPaths, "\n\t "+File.pathSeparator);
            while (tokenizer.hasMoreTokens())  {  // construct full paths to search for j-files
                String nextToken = tokenizer.nextToken()+File.separatorChar;
                if (recurse == false)
                    targetVector.add(nextToken);
                else
                    scalaLabUtils.appendAllSubDirectories(nextToken, targetVector);
          }
          
    }
}

// clear the properties 
    public static void clearProperties()  {
       settings.setProperty("scalalabWorkingDirProp","");
    }

    public static void clearUserPaths() {
       ScalaSciUserPaths = new Vector();
    }
    
    /** @return actual working directory */
    protected String getWorkingDirectory()
    {
        return workingDir;
    }

    /** @param set working directory */
    protected void setWorkingDirectory(String _workingDir)
    {
        workingDir = _workingDir;
    }

    public static IMain getInterpreter() {
        return globalInterpreter;
    }
    
    
 
     }
 
