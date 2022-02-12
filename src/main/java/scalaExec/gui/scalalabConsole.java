package scalaExec.gui;

import scala.Option;
import scalaExec.Interpreter.GlobalValues;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;
// SOS-Sterg import scala.ScalaObject;

import scala.tools.nsc.*;
import scalaExec.scalaLab.StreamGobbler;

import static scalalab.JavaGlobals.*;


import scala.tools.nsc.interpreter.*;
import scala.tools.nsc.interpreter.shell.*;
import scala.tools.nsc.reporters.Reporter;


import static scalaExec.Interpreter.ControlInterpreter.*;
import scalalab.JavaGlobals;

public class scalalabConsole  extends Console  {
    
    JPopupMenu  consolePopup = new JPopupMenu();
    
    static  scala.tools.nsc.interpreter.Results.Result  rs;
    static  String inputString;
    
    
      JMenuItem cutJMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      JMenuItem copyJMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      JMenuItem pasteJMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      JMenuItem helpItem = new JMenuItem(new ConsoleHelpAction());
      JMenuItem resetScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports");
      JMenuItem resetScalaInterpreterEJMLItem = new JMenuItem("Reset Scala Interpreter using Efficient Java Matrix Library of Peter Abeles");
      JMenuItem resetScalaInterpreterMTJItem = new JMenuItem("Reset Scala Interpreter using MTJ  Java Matrix Library ");
      JMenuItem resetToolboxesScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports and installed toolboxes");
      JMenuItem clearAllScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter without any default Imports");
      JMenuItem adjustFontItem = new JMenuItem("Adjust Console's font");
      JMenuItem clearConsoleItem = new JMenuItem("Clear Console");
      JMenuItem displayBindingItem =  new JMenuItem("Display Current Binding");
      JMenuItem simpleJavaClassFile = new JMenuItem(new simpleJavaApplAction());
      JMenuItem simpleJavaClassFileUsingScalaLab = new JMenuItem(new simpleJavaApplUsingScalaLabAction());
      JMenuItem simpleJavaClassFileUsingScalaLabBioJava = new JMenuItem(new simpleJavaApplUsingScalaLabBioJavaAction());
      JMenuItem ScalaClassWithCompanionObject = new JMenuItem(new scalaClassWithCompanionObject() );
      JMenuItem ScalaSingletonObject = new JMenuItem(new scalaStandAloneObject());
             

     public Settings scalaSettings = new Settings();
      
     public Settings getSettings() {
         return scalaSettings;
     }
     
      public scalalabConsole()
	{
        
      consolePopup.setFont(scalaExec.Interpreter.GlobalValues.puifont);
    
      cutJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      copyJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      pasteJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      helpItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      resetScalaInterpreterItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      resetScalaInterpreterEJMLItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      resetScalaInterpreterMTJItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      resetToolboxesScalaInterpreterItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      clearAllScalaInterpreterItem.setFont(scalaExec.Interpreter.GlobalValues.puifont); 
      adjustFontItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      clearConsoleItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      simpleJavaClassFile.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      simpleJavaClassFileUsingScalaLab.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      simpleJavaClassFileUsingScalaLabBioJava.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      ScalaClassWithCompanionObject.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      ScalaSingletonObject.setFont(scalaExec.Interpreter.GlobalValues.puifont);
      
      
      setToolTipText("ScalaLab Console interface to the Scala Interpreter. Is suitable only for small one-line scripts. Uses the same Interpreter as ScalaInterpreterPane ");
        
        setFont(new Font(GlobalValues.defaultConsoleFontName, Font.PLAIN, Integer.parseInt(GlobalValues.defaultConsoleFontSize )));
        
        
        resetToolboxesScalaInterpreterItem.addActionListener(new ActionListener() {
   @Override
            public void actionPerformed(ActionEvent e) {
                interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
            }
        });

        
          resetScalaInterpreterEJMLItem.addActionListener(new ActionListener() {
   @Override
            public void actionPerformed(ActionEvent e) {
                createInterpreterForResetEJML();
            }
        });

     resetScalaInterpreterMTJItem.addActionListener(new ActionListener() {
   @Override
            public void actionPerformed(ActionEvent e) {
                createInterpreterForResetMTJ();
            }
        });

          resetScalaInterpreterItem.addActionListener(new ActionListener() {
   @Override
            public void actionPerformed(ActionEvent e) {
                     interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
            }
        });

        clearAllScalaInterpreterItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              createInterpreterForClearAll();
            }
        });
      adjustFontItem.addActionListener(new  ConsoleFontAdjusterAction());
            
  clearConsoleItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          scalaSciCommands.BasicCommands.cls();
          displayPrompt();
            }
        });

                
            makePopup();
            this.add(consolePopup);
            this.addMouseListener(new MouseAdapterForConsole());   // handles right mouse clicks
            
            this.setBackground(Color.WHITE);
            commandNo = 0;
            previousCommands = new Vector(10, 10);
            lineStart = getText().length() + 1;
		
            keyHandler = new ConsoleKeyHandler();
		
            addKeyListener(keyHandler);
            
            
            }	
 
      static   URL  sjarPathOfClass(String className) {
        try {
            return Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
        } catch (ClassNotFoundException ex) {
           System.out.println("error in jarPathOfClass"+className+")");
           ex.printStackTrace();
           return null;
        }
}
      
  
     URL  jarPathOfClass(String className) {
        try {
            return Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
        } catch (ClassNotFoundException ex) {
           System.out.println("error in jarPathOfClass"+className+")");
           ex.printStackTrace();
           return null;
        }
}
      
     public static void setByUser(Settings set) { 
         set.classpath().setByUser_$eq(true);
     }
     
     public void mkPaths() {
         String jarFile = GlobalValues.jarFilePath;
         if (GlobalValues.hostIsUnix) {   
  compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "/");
  libFile =  jarPathOfClass("scala.io.Source").toString().replace("file:/","/");
  reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/","/");
  swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "/");
  

  jfreechartFile =jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "/");
  jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "/");

  javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "/");
  openblasFile = jarPathOfClass("org.bytedeco.javacpp.presets.openblas").toString().replace("file:/", "/");
  mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "/");
  ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "/");
  ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "/");
  rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "/");
  matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", "/");
  jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "/");
  numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","/");
         
  JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "/");
  LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "/");
  
  ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "/");
      }
         else {
   compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "");
   libFile =  jarPathOfClass("scala.io.Source").toString().replace("file:/","");
   reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/","");
   swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "");
   

   jfreechartFile =jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "");
   jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "");
   javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "");
   openblasFile = jarPathOfClass("org.bytedeco.javacpp.presets.openblas").toString().replace("file:/", "");
  
   mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "");
   ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "");
   ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "");
   rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "");
   matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", ""); 
   jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "/");
   numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","");
  
    JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "");
   LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "");
   ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "");
    }   
     
          scalaSettings = new Settings();
          
         scalaSettings.classpath().setByUser_$eq(true);
     
         scalaSettings.classpath().append(jarFile);
         scalaSettings.classpath().append(compFile);
         scalaSettings.classpath().append(reflectFile);
         scalaSettings.classpath().append(libFile);
         scalaSettings.classpath().append(swingFile);
  
         if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsWin64 || GlobalValues.hostIsMac) {
          scalaSettings.classpath().append(javacppFile);
          scalaSettings.classpath().append(openblasFile);
         }

         scalaSettings.classpath().append(jfreechartFile);
         scalaSettings.classpath().append(jsciFile);
         scalaSettings.classpath().append(mtjColtSGTFile);
         scalaSettings.classpath().append(ApacheCommonsFile);
         scalaSettings.classpath().append(ejmlFile);
         scalaSettings.classpath().append(rsyntaxTextAreaFile);
         scalaSettings.classpath().append(matlabScilabFile);
         scalaSettings.classpath().append(jblasFile);
         scalaSettings.classpath().append(numalFile);
         scalaSettings.classpath().append(JASFile);
         scalaSettings.classpath().append(LAPACKFile);
         scalaSettings.classpath().append(ARPACKFile);
     
     }
     
     


     
/** Create a new interpreter. */
      public void createInterpreterEJMLorJAMA()  {
          GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
          
          JFrame initFrame = new JFrame("Initializing Scala scripting engine ... Please wait ... ");
          initFrame.setSize(800, 100);
          Font myFont = new Font("Arial", Font.BOLD, 20);
          JLabel myLabel = new JLabel("Initializing Scala scripting engine ...  Please wait a few seconds ...");
          myLabel.setFont(myFont);
          initFrame.add(myLabel);
          initFrame.setLocation(200, 300);
          initFrame.setVisible(true);

          mkPaths();
           
          Vector <String> classpath = GlobalValues.ScalaSciClassPathComponents;
           for (int k=0; k<classpath.size(); k++) {
              String clsp = classpath.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }

           
         prepareSettings(scalaSettings);  
      
         scalaExec.Interpreter.GlobalValues.globalInterpreter =  new  scala.tools.nsc.interpreter.IMain(scalaSettings, new ReplReporterImpl(scalaSettings));

          
         if (GlobalValues.interpreterTypeForPane == GlobalValues.EJMLMat)  {
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsEJMLScala);   // interpret the basic imports
         }
     
         initFrame.dispose();

          GlobalValues.editorPane.setText("");   // reset the text of the Scala Interpreter Pane
              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   
          
          if (GlobalValues.ScalaLabInInit == false) {
            scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
            if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

           scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();
    
      }
     
/** Create a new interpreter. */
      public void createInterpreterForReset()  {
          
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "Mat");
          
          GlobalValues.editorPane.setText("");   // reset the text of the Scala Interpreter Pane
         
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

    scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();

          }

      
      public void commonCreateInterpreterCode() {
         
      GlobalValues.interpreterClassPathComponents= null; 
      
      mkPaths();
      
       Vector classpathScalaSci = GlobalValues.ScalaSciClassPathComponents;
       for (int k=0; k<classpathScalaSci.size(); k++) {
              String clsp = (String)classpathScalaSci.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }
  
          
         prepareSettings(scalaSettings);  
    
        scalaExec.Interpreter.GlobalValues.globalInterpreter =  new  scala.tools.nsc.interpreter.IMain(scalaSettings, new ReplReporterImpl(scalaSettings));

         GlobalValues.autoCompletionWorkspace = new AutoCompletionWorkspace();
         GlobalValues.autoCompletionWorkspace.scanVars=new Vector();
        
              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   

          if (GlobalValues.ScalaLabInInit == false) {
           scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
           if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          displayPrompt();
          
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

           scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();
      
            
     }
     
      
     /** Create a new interpreter initialized with EJML library */
      public void createInterpreterForResetEJML()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.EJMLMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsEJMLScala);   // interpret the basic imports


          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "EJML-Mat");
          
          System.out.println("EJML Interpreter created");
          
      }

            
      /** Create a new interpreter initialized with JBLAS library */
      public void createInterpreterForResetJBLAS()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.JBLASMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsJBLASScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "JBLAS-Mat");
          
          System.out.println("JBLAS Interpreter created");
          
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());


      }

      /** Create a new interpreter initialized with EJML library */
      public void createInterpreterForResetCommonMaths()  {
          
          commonCreateInterpreterCode();
  
          GlobalValues.interpreterTypeForPane = GlobalValues.ApacheCommonMathsMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsCommonMathsScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "CommonMaths-Mat");
          
          System.out.println("Apache Common Maths Interpreter created");
          
          

           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

      }

      /** Create a new interpreter. */
      public void createInterpreterForResetMTJ()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.MTJMat;
       
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsMTJScala);   // interpret the basic imports
                 
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "MTJ-Mat");
          
          System.out.println("MTJ  Interpreter created");
          
          

           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

      }

      /** Create a new interpreter initialized with standard library and basic imports */
      public void createInterpreterFast()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
         
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "JAMA-Mat");
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

    
      }

      
      
      /** Create a new interpreter initialized with EJML library */
      public void createInterpreterForResetFastEJML()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.EJMLMat;
         
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsEJMLScala);   // interpret the basic imports

          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "EJML-Mat");
          
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

      }

      
      /** Create a new interpreter initialized with JBLAS library, basic  imports */
      public void createInterpreterForResetFastJBLAS()  {
          
          commonCreateInterpreterCode();
     
          GlobalValues.interpreterTypeForPane = GlobalValues.JBLASMat;
         
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsJBLASScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "JBLAS-Mat");
          
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

      }

                 /** Create a new basic imports interpreter initialized with  Apache commons library */
      public void createInterpreterForResetFastCommonMaths()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.ApacheCommonMathsMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsCommonMathsScala);   // interpret the basic imports
     
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "CommonMaths-Mat");
          
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

   }


      
      /** Create a new basic imports for D2Das1DMat interpreter. */
      public void createInterpreterForResetD2Das1D()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.D2Das1DMat;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsD2Das1DScala);   // interpret the basic  imports
          
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "D2Das1d-Mat");
           

           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

      }

       public void createInterpreterWithNoImports()  {
  
          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.NotAnyImports;
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.helperImport);  
          
          GlobalValues.scalalabMainFrame.tabbedToolbars.setTitleAt(2,  "Mat");
          
          
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

       }

      

      
/** Create a new interpreter. */
      public void createInterpreterForClearAll()  {

          commonCreateInterpreterCode();
          
          GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
          
      }
     

      // append a single path specification to the classpath
  public void  interpreterWithAppendedCP(String classpath) {
      
      GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
      classpath = classpath.trim();   
      GlobalValues.interpreterClassPathComponents= null; 
      
      mkPaths();
      
       Vector classpathScalaSci = GlobalValues.ScalaSciClassPathComponents;
       for (int k=0; k<classpathScalaSci.size(); k++) {
              String clsp = (String)classpathScalaSci.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }
  
          
         prepareSettings(scalaSettings);  
    
        scalaExec.Interpreter.GlobalValues.globalInterpreter =  new scala.tools.nsc.interpreter.IMain(scalaSettings, new ReplReporterImpl(scalaSettings));

         GlobalValues.autoCompletionWorkspace = new AutoCompletionWorkspace();
         GlobalValues.autoCompletionWorkspace.scanVars=new Vector();
     
     if (GlobalValues.interpreterTypeForPane  == GlobalValues.EJMLMat) 
     {
         scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsEJMLScala);
     }   // interpret the basic imports
     
     else {
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsScala);   // interpret the basic imports

     }

          if (GlobalValues.ScalaSciClassPathComponents.contains(classpath)==false)
              GlobalValues.ScalaSciClassPathComponents.add(classpath);
         
              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   

          if (GlobalValues.ScalaLabInInit == false) {
           scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
           if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          displayPrompt();
          
           
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

           scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();
           
  }
  

  // append an array of classpath specifications to the classpath
  public void  interpreterWithAppendedCP(String [] classpath) {
      GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
          GlobalValues.interpreterClassPathComponents= null; 
      
          mkPaths();
          
          for (String clsp:classpath)  
              scalaSettings.classpath().append(clsp.trim());
           
          Vector classpathScalaSci = GlobalValues.ScalaSciClassPathComponents;
           for (int k=0; k<classpathScalaSci.size(); k++) {
              String clsp = (String)classpathScalaSci.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }
  
         prepareSettings(scalaSettings);  
      
          scalaExec.Interpreter.GlobalValues.globalInterpreter =  new  scala.tools.nsc.interpreter.IMain(scalaSettings,  new ReplReporterImpl(scalaSettings));
         
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsScala);   // interpret the basic imports
          
          for (String clsp:classpath) {
              if (GlobalValues.ScalaSciClassPathComponents.contains(clsp)==false)
              GlobalValues.ScalaSciClassPathComponents.add(clsp.trim());
          }

              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   

          if (GlobalValues.ScalaLabInInit == false) {
            scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
            if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          displayPrompt();
          
           
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

 scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();

  }
  
  // append a Vector of classpath specifications to the classpath
  public void  interpreterWithAppendedCP(final Vector classpath) {
      
      GlobalValues.interpreterTypeForPane = GlobalValues.JAMAMat;
      GlobalValues.interpreterClassPathComponents= null; 
      
      mkPaths();
      
          for (int k=0; k<classpath.size(); k++) {
              String clsp = (String)classpath.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }
         
     prepareSettings(scalaSettings);  
    
     scalaExec.Interpreter.GlobalValues.globalInterpreter =  new  scala.tools.nsc.interpreter.IMain(scalaSettings, null);
     scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsScala);   // interpret the basic imports
          
     for (int k=0; k<classpath.size(); k++) {
              String clsp = (String)classpath.elementAt(k);
              if (GlobalValues.ScalaSciClassPathComponents.contains(clsp)==false)
                GlobalValues.ScalaSciClassPathComponents.add(clsp.toString().trim());
            }
   
         
              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   

          if (GlobalValues.ScalaLabInInit == false) {
             scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
             if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          displayPrompt();
          
           
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

           
     scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();

     }
  

  // append a Vector of classpath specifications to the classpath
  public void  interpreterWithAppendedCPEJML(Vector classpath) {
      
      GlobalValues.interpreterTypeForPane = GlobalValues.EJMLMat;
      GlobalValues.interpreterClassPathComponents= null; 
  
      mkPaths();
      
          for (int k=0; k<classpath.size(); k++) {
              String clsp = (String)classpath.elementAt(k);
              scalaSettings.classpath().append(clsp.trim());  
          }
    
          prepareSettings(scalaSettings);  
    
         scalaExec.Interpreter.GlobalValues.globalInterpreter =  new  scala.tools.nsc.interpreter.IMain(scalaSettings, new ReplReporterImpl(scalaSettings));
          
          scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(GlobalValues.basicImportsEJMLScala);   // interpret the basic imports

          for (int k=0; k<classpath.size(); k++) {
              String clsp = (String)classpath.elementAt(k);
              if (GlobalValues.ScalaSciClassPathComponents.contains(clsp)==false)
                GlobalValues.ScalaSciClassPathComponents.add(clsp.toString().trim());
            }
   
              // update the variable that keeps the Scala Interpreter Pane interpreter
          GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter);   

          if (GlobalValues.ScalaLabInInit == false) {
             scalaExec.scalaLab.scalaLab.constructPathPresentationPanel();
             if (GlobalValues.jfExplorerFrame!=null)  GlobalValues.scalalabMainFrame.createExplorerPanel();
          }
          displayPrompt();

           
           
           GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());

           scalaExec.Interpreter.GlobalValues.globalInterpreterPane.installScalaCompletion();
    
     }
  

  public void Completion(String line) {
    //  boolean completion = scala.tools.nsc.interpreter.Completion$.MODULE$.isValidCompletion(line);
      System.out.println("completion = ");
  }
 
  
    public void interpretLine(String theLine)
    { 
        
        inputString = theLine.trim();
        
                 Runnable  r = new Runnable() {

                    public void run() {
                 rs =  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(inputString);
 
                    }
                };
                 Thread execThread = new Thread(r);
                 execThread.start();
                 
         commandNo = previousCommands.size();
        GlobalValues.scalalabMainFrame.scalalabConsole.requestFocus();
    }
    

      /**Interpret the current line*/
	public void interpretLine()
	{
    
		//get the text on the current line
        String text = getText();
        inputString = text.substring(text.lastIndexOf(GlobalValues.scalalabPromptString) + 2, text.length());
        inputString = inputString.trim();
      
        
        
        /* exit application */
        if (inputString.equals("quit") || inputString.equals("exit"))
		{
	GlobalValues.scalalabMainFrame.closeGUI();   // call the main close() routine
		}
        else {
        if (!inputString.equals(""))
        {
                
                 Runnable  r = new Runnable() {

                    public void run() {
                  rs =  scalaExec.Interpreter.GlobalValues.globalInterpreter.interpret(inputString);
 
                  commandNo = previousCommands.size();
                if (rs.toString().contains("Error")) {}  // nothing to display at the console window for the error
                else  {  // not Error in result
     
                     if (inputString.length()>0)
                    previousCommands.addElement(inputString);
        
                //scalaExec.Interpreter.GlobalValues.scalalabMainFrame.scalalabConsole.displayPrompt();
                    
                GlobalValues.scalalabMainFrame.scalalabConsole.requestFocus();
                    }  // not Error in result
           GlobalValues.scalalabMainFrame.scalalabConsole.requestFocus();
                    }  // run()
                 };  // new Runnable()
                         
                 Thread execThread = new Thread(r);
                 execThread.start();
                  
                 
                    }  // verboseOutput
               
         } 
              
        }

        

         
  private void  makePopup() {
        
   
            consolePopup.removeAll();
            consolePopup.add(pasteJMenuItem);
            consolePopup.add(copyJMenuItem);
            consolePopup.add(cutJMenuItem);
            consolePopup.add(helpItem);
            consolePopup.add(resetScalaInterpreterItem);
            consolePopup.add(resetScalaInterpreterEJMLItem);
            consolePopup.add(resetScalaInterpreterMTJItem);
            consolePopup.add(resetToolboxesScalaInterpreterItem);
            consolePopup.add(clearAllScalaInterpreterItem);
            consolePopup.add(adjustFontItem);
            consolePopup.add(clearConsoleItem);
            consolePopup.add(simpleJavaClassFile);
            consolePopup.add(simpleJavaClassFileUsingScalaLab);
            consolePopup.add(simpleJavaClassFileUsingScalaLabBioJava);
            consolePopup.add(ScalaClassWithCompanionObject);
            consolePopup.add(ScalaSingletonObject);
            
  }
      
      
      
      private class ConsoleHelpAction extends AbstractAction {
               ConsoleHelpAction() {
                   super("Help");
               }
         public void actionPerformed(ActionEvent e) {
          Font  df = new Font("SansSerif", Font.BOLD, 14);
         
          JFrame consHelpFrame = new JFrame("Basic Help");
          JPanel consHelpPanel = new JPanel(new BorderLayout());
          JTextArea consHelpTextArea = new JTextArea();
          consHelpFrame.setLocation(xloc, yloc);
          consHelpFrame.add(consHelpPanel);
          consHelpFrame.setVisible(true);
          Graphics2D gc = (Graphics2D) consHelpPanel.getGraphics();
         FontRenderContext context = gc.getFontRenderContext();
           Rectangle2D bounds = df.getStringBounds(" ", context);
          int mwidth =(int) bounds.getWidth()*150;
          int mheight =(int) bounds.getHeight()*20;
         
           consHelpFrame.setSize(mwidth, mheight);
         
          consHelpTextArea.setFont(df);
          consHelpTextArea.setText(scalaSciCommands.BasicCommands.commands);
          JScrollPane spHelpText = new JScrollPane(consHelpTextArea);
          consHelpPanel.add(spHelpText, BorderLayout.CENTER);
          }
      
      }
      
      
      
     /**display the previous command in the list*/
	public void prevCommand()
	{
		commandNo--;
    	
    	String text = "";
    	if(commandNo >= 0 && commandNo < previousCommands.size())
    	{
    		text = ((String)previousCommands.elementAt(commandNo)).trim();
    	}
    	else if(commandNo < 0)
    	{
    		text = "";
    		commandNo = -1;
    	}

    	// replace current command with previous command
    	textArea = getText();
        int    pos1     = textArea.lastIndexOf(GlobalValues.scalalabPromptString) + 2;
    	String prev = textArea.substring(0, pos1);
        
    	setText(textArea.substring(0, pos1)+text);
    	
// set cursor at the end of the text area
	setCaretPosition(getText().length());
	}	
	
        /**display the previous command in the list*/
	public void mostRecentCommand()
	{
	String text = ConsoleKeyHandler.prevtext;
    	
        // replace current command with previous command
    	textArea = getText();
        int    pos1     = textArea.lastIndexOf(GlobalValues.scalalabPromptString) + 2;
    	String prev = textArea.substring(0, pos1);
        
    	setText(textArea.substring(0, pos1)+text);
    	
// set cursor at the end of the text area
	setCaretPosition(getText().length());
	}	
	
	/**display the next command in the list*/
	public void nextCommand()
	{
	commandNo++;

    	String text = "";
    	if(commandNo >= 0 && commandNo < previousCommands.size())
    	{
    		text = ((String)previousCommands.elementAt(commandNo)).trim();
    	}
    	else if(commandNo >= previousCommands.size())
    	{
    		text = "";
    		commandNo = previousCommands.size();
    	}		    	

    	// replace current command with next command
    	textArea = getText();
    	int    pos1     = textArea.lastIndexOf(GlobalValues.scalalabPromptString) + 2;
    	setText(textArea.substring(0,pos1)+text);
    	
    	// set cursor at the end of the text area
		setCaretPosition(getText().length());
	}

	
	/**Display the command prompt*/
	public  void displayPrompt()
	{
        if (GlobalValues.displayDirectory==true)     
          append("\n"+GlobalValues.workingDir+GlobalValues.scalalabPromptString);
        else 
          append("\n"+GlobalValues.scalalabPromptString);
        String currentText = getText();
        lineStart = currentText.length();
	setCaretPosition(lineStart);
	}
	

	/** clears the current command line */
	public void clearCommandLine()
	{
	
    	String text = "";
    	
    	// replace current command with next command
    	textArea = getText();
    	int    pos1     = textArea.lastIndexOf(GlobalValues.scalalabPromptString) + 2;
    	setText(textArea.substring(0,pos1)+text);
    	
    	// set cursor at the end of the text area
	setCaretPosition(getText().length());
	}

   
        
           private class MouseAdapterForConsole  extends  MouseAdapter {
        @Override
          public void mousePressed(MouseEvent e) {   
              GlobalValues.userConsole =  (Console) e.getSource();
              xloc = e.getX();
              yloc = e.getY();
              if (e.isPopupTrigger()){  
                consolePopup.show((Component) e.getSource(), e.getX(), e.getY());
             }
           }
    
        @Override
        public void mouseReleased(MouseEvent e) { 
           if (e.isPopupTrigger()){
                 consolePopup.show((Component) e.getSource(), e.getX(), e.getY());
             }       
             
          }
       }
           
           
   
   private    class ConsoleFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 Font choosingFont = myFontChooser.getFont();
 Color fontColorChoosed =  myFontChooser.getForegroundColor();
 int rgbAlpha = fontColorChoosed.getAlpha(), rgbRed = fontColorChoosed.getRed(), rgbBlue = fontColorChoosed.getBlue(), rgbGreen = fontColorChoosed.getGreen();
 GlobalValues.scalalabMainFrame.scalalabConsole.setFont(choosingFont);
 GlobalValues.scalalabMainFrame.scalalabConsole.setForeground(myFontChooser.getForegroundColor());
 GlobalValues.settings.setProperty("alphaProp",  Integer.toString(rgbAlpha));
 GlobalValues.settings.setProperty("redProp",  Integer.toString(rgbRed));
 GlobalValues.settings.setProperty("greenProp",  Integer.toString(rgbGreen));
 GlobalValues.settings.setProperty("blueProp",  Integer.toString(rgbBlue));

 int  isBold = 0;   if (choosingFont.isBold()) isBold = 1;
 int  isItalic = 0;   if (choosingFont.isItalic()) isItalic = 1;
 GlobalValues.settings.setProperty("isBoldProp", Integer.toString(isBold));
 GlobalValues.settings.setProperty("isItalicProp", Integer.toString(isItalic));
         }
       }

     
 
 private class scalaStandAloneObject extends AbstractAction {
     public scalaStandAloneObject() {  super("Scala StandAlone object"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */\
               String expression = "object  SSO    {    // change \"SSO\" to the name of your Scala singleton object \n"+
                    "def main(args: Array[String]) { \n \n \n } \n} \n";
              GlobalValues.editorPane.setText(expression);
              GlobalValues.editorPane.setCaretPosition(expression.length());
            }
          });
      }
   }
 
 	
  private class editDSPAction extends AbstractAction {
      public  editDSPAction()  { super("scalaLab Editor DSP Application");}
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                      String expression  =  GlobalValues.basicImportsScala +"\n\n";

          GlobalValues.editorPane.setText(expression);
          GlobalValues.editorPane.setCaretPosition(expression.length());

           }
      });
    }
 }

   
 private  class  simpleJavaApplAction  extends AbstractAction {
     public simpleJavaApplAction()  { super("simple Java Application "); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
               String expression = "public class yourNameForClass { \n"+
                    "public static void main(String [] args) { \n \n \n } \n} \n";
               GlobalValues.editorPane.setText(expression);
              GlobalValues.editorPane.setCaretPosition(expression.length());
            }
        });
     }
   }


             

 private  class scalaClassWithCompanionObject extends AbstractAction {
     public scalaClassWithCompanionObject() {  super("Scala Class with Companion object"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
               String expression = "class  SSO {    // change \"SSO\" to the name of your Scala class with Singleton object \n }\n"+
                    "object  SSO     {    // change \"SSO\" to the name of your Scala class with Singleton object \n"+
                    "def main(args: Array[String]) { \n \n \n } \n} \n" +
                    "\n\n // create objects with:\n"+
                    "// myScalaObj  = new SSO$()\n"+
                    "//from Java  refer to fields of object SSO as: \n"+
                    "//e.g. if var df = 200.3, is a  field value,   SSO$.MODULE$.df \n"+
                    "// or myScalaObj.df \n";
              GlobalValues.editorPane.setText(expression);
              GlobalValues.editorPane.setCaretPosition(expression.length());
            }
          });
     }
  }
 

 private class simpleJavaApplUsingScalaLabAction  extends AbstractAction {
     public simpleJavaApplUsingScalaLabAction()  { super("simple Java Application using the scalaLab libraries"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
            String expression  =  GlobalValues.basicImportsScala +"\n\n";

            GlobalValues.editorPane.setText(expression);
            GlobalValues.editorPane.setCaretPosition(expression.length());
           }
       });
     }
   }


private class simpleJavaApplUsingScalaLabBioJavaAction  extends AbstractAction {
     public simpleJavaApplUsingScalaLabBioJavaAction()  { super("simple Java Application using the scalaLab libraries and the BioJava toolbox"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
               String expression  =  GlobalValues.basicImportsJava+"\n\n"+ scalalabEdit.EditorImports.bioJavaImportsJava+"\n\n";

               expression +=
             "\n \n public class yourNameForClass { \n"+
                    "public static void main(String [] args) { \n \n \n } \n} \n";
           
            GlobalValues.editorPane.setText(expression);
            GlobalValues.editorPane.setCaretPosition(expression.length());

           }
         });
      }
   }

 
}

 
