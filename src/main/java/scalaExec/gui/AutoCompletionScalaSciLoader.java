
package scalaExec.gui;


import javax.swing.JLabel;
import java.io.*;
import java.util.TreeSet;
import scalaExec.ClassLoaders.JarClassLoader;
import scalaExec.Interpreter.GlobalValues;

// this class loads Scala basic classes and also basic ScalaSci libraries 
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class AutoCompletionScalaSciLoader  
{ 
 String jarFilePath;     String scalalabClassPath;   String [] toolboxes;
   public AutoCompletionScalaSciLoader(String _jarFilePath, String _scalalabClassPath, String [] _toolboxes)
   { 
      jarFilePath = _jarFilePath;      scalalabClassPath = _scalalabClassPath; toolboxes = _toolboxes;
   }
   
   public void loadClasses() {
               try {
         
           JFrame progressFrame = new JFrame("Scanning Class Libraries for ScalaSci Autocompletion  ..");
           JPanel  progressPanel = new JPanel();
           JLabel progressLabel = new JLabel("Loading .. ");
           progressPanel.add(progressLabel, BorderLayout.CENTER);
           progressFrame.add(progressPanel);
           progressFrame.setSize(600, 100);
           progressFrame.setLocation(10, 10);
           progressFrame.setVisible(true);
                  
         JarClassLoader  myJarLoader = new JarClassLoader();
         
         int scalaSciClasses = myJarLoader.scanBuiltInScalaSciClasses(jarFilePath);
         int libsEJMLScalaSciClasses = myJarLoader.scanLibsScalaSciClasses(scalalab.JavaGlobals.ejmlFile);
         int libsMTJScalaSciClasses = myJarLoader.scanLibsScalaSciClasses(scalalab.JavaGlobals.mtjColtSGTFile);
         int libsApacheCommonsScalaSciClasses = myJarLoader.scanLibsScalaSciClasses(scalalab.JavaGlobals.ApacheCommonsFile);
         int libsNumalScalaSciClasses = myJarLoader.scanLibsScalaSciClasses(scalalab.JavaGlobals.numalFile);
         
         scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci = new scalaExec.gui.AutoCompletionScalaSci();  // create the autocompletion object
 
         System.out.println("#ScalaSciClasses =  "+scalaSciClasses+", # numaliClasses = "+libsNumalScalaSciClasses +
                 ", # ejmlClasses = "+ libsEJMLScalaSciClasses+", # MTJColtSGTClasses ="+libsMTJScalaSciClasses+
                 ", # ApacheCommonMathsClasses ="+ libsApacheCommonsScalaSciClasses);
         
         progressFrame.dispose();
              
         
       }
         
             catch (IOException ioEx) {
                 System.out.println("I/O error in JarClassLoader");
                 ioEx.printStackTrace();
                 
             }
        } // run
   
   
   public void loadClasses(String toolboxJarFile) {
               try {
         
                  
         JarClassLoader  myJarLoader = new JarClassLoader();
         
              
        int numClassesLoadedFromToolbox = myJarLoader.scanAllToolboxClasses(toolboxJarFile);
         String smallName = toolboxJarFile;
        int idx;
        while ((idx = smallName.indexOf('.')) != -1)   // extract class name
           smallName = smallName.substring(idx+1, smallName.length());
        GlobalValues.loadedToolboxesNames[GlobalValues.currentToolboxId]  = smallName;
        GlobalValues.incrementToolboxCount();
        System.out.format("loaded %d built classes %n from toolbox %s %n", numClassesLoadedFromToolbox, toolboxJarFile);
             
         
       }
         
             catch (IOException ioEx) {
                 System.out.println("I/O error in JarClassLoader");
                 ioEx.printStackTrace();
                 
             }
        } // run
   
}
