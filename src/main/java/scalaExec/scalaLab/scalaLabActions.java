
package scalaExec.scalaLab;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;



import scalalabEdit.*;

import scalaExec.Interpreter.GlobalValues;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Properties;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import scalalab.JavaGlobals;


   class autoCompletionRegExHelpAction extends AbstractAction {
       autoCompletionRegExHelpAction() {
           super("AutoCompletion with regular expression matching-- F3  (scalaSci), F11 (Java)");
       }    
     public void actionPerformed(ActionEvent e)  {
         
                 JFrame  regExpFrame = new JFrame("Help on Regular Expression based autocompletion");
                 regExpFrame.setSize(400, 300);
                 regExpFrame.setLayout(new BorderLayout());
                 
                JTextArea  regExpArea = new JTextArea();
                
                regExpArea.setText("Examples of regular expression based matching: \n"+
 "1.   p.*             // All commands that start with p \n"+
 "2.   [ap].*         // All commands that start with a or p \n"+
 "3.   [^a].*         // All commands that do not start with a \n"+
 "4.   .*cos.*        // All commands that include \"cos\" somewhere in their description \n"+
 "5.   [b-e].*        // All commands that start with a letter at the range b-e (i.e. b,c,d,e) \n+" +
 "6.   [B-De-h].*     // All commands that start with B-D (i.e. B, C, D) or e-h (i.e. e,f,g,h) \n"+
 "7.   [^A-Z^a-e].*  // All commands that do not start with A-Z and also not with a-e\n"+
 "8.   [^A-Z&&[^a]].*   // not with A-Z and not with a \n"+
 "9.  ^p.*           // p at the start \n"+
 "10.  [^a-c].*     // first character not in  [a-c] \n"+
 "11.   *.component  // last word is 'component'\n"
                        );
                
                regExpFrame.add(regExpArea);
                regExpFrame.setVisible(true);
               }
    }
   
   

   class scalaSciExamplesAction extends AbstractAction {
       scalaSciExamplesAction() {
           super("scalaSci Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessScalaSciInJar  processScalaSciInJar = new scalaExec.scalaLab.ProcessScalaSciInJar(scalaLabJarFile);
    }
   }





class scalaSciExamplesJTreeAction extends AbstractAction {
       scalaSciExamplesJTreeAction() {
           super("scalaSci Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        scalaExec.gui.watchExamples weObj = new scalaExec.gui.watchExamples();
        weObj.scanMainJarFile(".sssci");
        weObj.displayExamples("ScalaSci Examples");
   
   }
   }


class scalaSciPlottingExamplesJTreeAction extends AbstractAction {
       scalaSciPlottingExamplesJTreeAction() {
           super("scalaSci Plotting  Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        scalaExec.gui.watchExamples weObj = new scalaExec.gui.watchExamples();
        weObj.scanMainJarFile(".plots-ssci");
        weObj.displayExamples("ScalaSci Plotting Examples");
   
    }
   }



   class scalaSciMatlabCompatibilityAction extends AbstractAction {
       scalaSciMatlabCompatibilityAction() {
           super("scalaSci Matlab compatibility examples - Matlab .m files for benchmarking comparisons");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode

        scalaExec.scalaLab.ProcessMatlabCompatibilty  processMatlabInJar = new scalaExec.scalaLab.ProcessMatlabCompatibilty(scalaLabJarFile);
    }
   }    
 

   class scalaSciNumAlExamplesAction extends AbstractAction {
       scalaSciNumAlExamplesAction() {
           super("scalaSci NUMAL Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessNumAlInJar    processNUMALInJar = new scalaExec.scalaLab.ProcessNumAlInJar(scalaLabJarFile);
    }
   }


   class scalaSciWaveletExamplesAction extends AbstractAction {
       scalaSciWaveletExamplesAction() {
           super("scalaSci Wavelet Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessWaveletExamplesInJar    processWaveletsInJar = new scalaExec.scalaLab.ProcessWaveletExamplesInJar(scalaLabJarFile);
    }
   }



class scalaSciBioJavaExamplesAction extends AbstractAction {
       scalaSciBioJavaExamplesAction() {
           super("Java  BioJava Examples - Requires installation of the BioJava toolbox");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode

        scalaExec.scalaLab.ProcessBioJavaExamplesInJar    processBioJavaInJar = new scalaExec.scalaLab.ProcessBioJavaExamplesInJar(scalaLabJarFile);
    }
   }


      class scalaSciWEKAExamplesAction extends AbstractAction {
        scalaSciWEKAExamplesAction() {
           super("scalaSci WEKA Examples - Requires installation of the WEKA toolbox");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode

        scalaExec.scalaLab.ProcessWEKAExamplesInJar    processWEKAInJar = new scalaExec.scalaLab.ProcessWEKAExamplesInJar(scalaLabJarFile);
    }
   }
   
    class scalaSciODEExamplesAction extends AbstractAction {
       scalaSciODEExamplesAction() {
           super("scalaSci ODE Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessODEExamplesInJar   processODEInJar = new scalaExec.scalaLab.ProcessODEExamplesInJar(scalaLabJarFile);
    }
   }
   
     class scalaSciExamplesPlottingAction extends AbstractAction {
       scalaSciExamplesPlottingAction() {
           super("scalaSci Plotting Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessPlottingExamplesInJar   processPlottingiInJar = new scalaExec.scalaLab.ProcessPlottingExamplesInJar(scalaLabJarFile);
    }
   }
   
     class JavaSGTExamplesPlottingAction extends AbstractAction {
       JavaSGTExamplesPlottingAction() {
           super("Scientific Graphics Toolbox Plotting Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();  // update the information about scalaLab's console input mode 
  
        scalaExec.scalaLab.ProcessPlottingSGTExamplesInJar   processPlottingInJar = new scalaExec.scalaLab.ProcessPlottingSGTExamplesInJar(scalaLabJarFile);
    }
   }
 
   
   
         
   class scalaExamplesAction extends AbstractAction {
       scalaExamplesAction() {
           super("Scala Examples");
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.scalaLab.ProcessScalaInJar   processScalaInJar = new scalaExec.scalaLab.ProcessScalaInJar(scalaLabJarFile);
    }
   }
      
   class scalaSciExamplesLAAction  extends AbstractAction {
       scalaSciExamplesLAAction() {
           super("ScalaSci Linear Algebra Examples");
           
       }

    public void actionPerformed(ActionEvent e) {
        String scalaLabJarFile = GlobalValues.jarFilePath;
        scalaExec.scalaLab.ProcessLinearAlgebraExamplesInJar   processLAScalaInJar = new scalaExec.scalaLab.ProcessLinearAlgebraExamplesInJar(scalaLabJarFile);
    }
   }
   
   
   
   class ODEWizardscalaSciAction extends AbstractAction {
       ODEWizardscalaSciAction() { super("ODE Wizard ScalaSci - Java based ODE implementations"); }
       public void actionPerformed(ActionEvent e) {
      SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                           scalaExec.Wizards.ODEWizardScalaSci  odeWizardScalaSci  = new scalaExec.Wizards.ODEWizardScalaSci();
           }
                     });
                }
   }
   
   class ODEWizardscalaSciScalaAction extends AbstractAction {
       ODEWizardscalaSciScalaAction() { super("ODE Wizard ScalaSci - Scala based ODE implementations"); }
       public void actionPerformed(ActionEvent e) {
      SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                           scalaExec.Wizards.ODEWizardScalaSciScala  odeWizardScalaSciScala  = new scalaExec.Wizards.ODEWizardScalaSciScala();
           }
                     });
                }
   }

class saveEditorTextAction extends AbstractAction  {
    public saveEditorTextAction() { super("Save Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        String saveFileName = GlobalValues.editingFileInPane;
        if (saveFileName == null)  { // not file specified thus open a FileChooser it
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 saveFileName = selectedFile.getAbsolutePath();
                 GlobalValues.editingFileInPane = saveFileName;
                 scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
         }
        }
        
        File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        scalaExec.Interpreter.GlobalValues.editorPane.write(fw);
                        GlobalValues.editingFileInPane = saveFileName;
                        GlobalValues.editorTextSaved = true;
     
                    
                                          // test whether we should update the ScalaPane's recent file information
                       if (GlobalValues.scalalabMainFrame.recentPaneFiles.indexOf(saveFileName) == -1)  {
                           GlobalValues.scalalabMainFrame.recentPaneFiles.add(saveFileName);
                           GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
                       }
     
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    
  }
}


class saveAsEditorTextAction extends AbstractAction  {
    public saveAsEditorTextAction() { super("Save As Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String saveFileName = selectedFile.getAbsolutePath();
                 File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        scalaExec.Interpreter.GlobalValues.editorPane.write(fw);
                       GlobalValues.editingFileInPane = saveFileName;
                       GlobalValues.editorTextSaved = true;
                       
                       scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                       
                       
                       // test whether we should update the ScalaPane's recent file information
                       if (GlobalValues.scalalabMainFrame.recentPaneFiles.indexOf(saveFile) == -1)  {
                           GlobalValues.scalalabMainFrame.recentPaneFiles.add(saveFileName);
                           GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
                       }
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }
}


class loadEditorTextAction extends AbstractAction  {
    public loadEditorTextAction() { super("Load Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
      
        int choice = JOptionPane.OK_OPTION;
        
        if (GlobalValues.editorTextSaved == false)  {
           choice = JOptionPane.showConfirmDialog(null, "Currently edited file not saved. Proceed? ", "Proceed with load?", JOptionPane.OK_OPTION);
      }
    
        if  (choice == JOptionPane.OK_OPTION)  {
            
            GlobalValues.editorTextSaved = true;
            
          javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String loadFileName = selectedFile.getAbsolutePath();
                 File loadFile = new File(loadFileName);
                    try {
                       FileReader  fr = new FileReader(loadFile);
                       
                        
            StringBuilder sb=new StringBuilder();
            
            while (true) {
                int ch = fr.read();
                if (ch==-1) break;
                sb.append((char)ch);
            }
                      scalaExec.Interpreter.GlobalValues.editorPane.setText(sb.toString());
                       
                    //   GlobalValues.globalInterpreterPane.updateDocument();
                       
                       GlobalValues.editingFileInPane = loadFileName;
                       GlobalValues.scalalabMainFrame.recentPaneFiles.add(loadFileName);
                       GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
                       scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                       
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+loadFile+" for loading editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception reading editor's text "+ex.getMessage());
                    }
           }
                           
         }
      }
    }

   
      class jeditAction extends AbstractAction {
       public jeditAction()  { super("jEdit Editor - supports Scala editing if you name the files with .scala extension");}
       public void actionPerformed(ActionEvent e) {
               String [] command  = new String[6];
               command[0] =  "java";
// classpath to the jedit seems that is not used, but in any case it  is not harmful
               String  jeditClassPath =  "-classpath";
               jeditClassPath +=  " "+GlobalValues.jarFilePath+File.pathSeparator+scalalab.JavaGlobals.compFile+
                      File.pathSeparator+scalalab.JavaGlobals.libFile+
                      File.pathSeparator+scalalab.JavaGlobals.reflectFile+
                      File.pathSeparator+scalalab.JavaGlobals.swingFile+
                        File.pathSeparator+scalalab.JavaGlobals.ejmlFile+
                      File.pathSeparator+scalalab.JavaGlobals.jblasFile+
                      File.pathSeparator+scalalab.JavaGlobals.mtjColtSGTFile+
                      File.pathSeparator+scalalab.JavaGlobals.ApacheCommonsFile+   
                      File.pathSeparator+scalalab.JavaGlobals.jfreechartFile+
                      File.pathSeparator+scalalab.JavaGlobals.numalFile;
               
               command[1] = "-cp";
               command[2] = jeditClassPath;
               
               command[3] = "-jar";
               String jeditPath = GlobalValues.scalalabLibPath + "4.3.2"+File.separator+"jedit.jar";
               command[4] =   jeditPath;
               
           
               
               String fileName = "Untitled.scala";
               command[5] = fileName;
               
            String jEditcommandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3];
            System.out.println("jEditCommandString = "+jEditcommandString); 
            try {
                Runtime rt = Runtime.getRuntime();
                Process javaProcess = rt.exec(command);
                // an error message?
                StreamGobbler errorGobbler = new StreamGobbler(javaProcess.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(javaProcess.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                } catch (IOException exio) {
                    System.out.println("IOException trying to executing "+command);
                    exio.printStackTrace();

                }
               
           }
                     
                    }
                
       
   
   class editDSPAction extends AbstractAction {
      public  editDSPAction()  { super("scalaLab Editor DSP Application");  }
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   GlobalValues.myEdit = new  scalalabEditor();
                   //GlobalValues.myEdit.editorPane.docVar_$eq(x$1);
                   
                   
           }
                     });
                    }
    }

   
 class  simpleJavaApplAction  extends AbstractAction {
     public simpleJavaApplAction()  { super("simple Java Application "); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.java");
           }
                     });
                    }
    }


             

 class scalaClassWithCompanionObject extends AbstractAction {
     public scalaClassWithCompanionObject() {  super("Scala Class with Companion object"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                    new scalalabEdit.EditorPaneEdit("Untitiled.java");
            }
                     });
                    }
     }
 
 class scalaStandAloneObject extends AbstractAction {
     public scalaStandAloneObject() {  super("Scala StandAlone object"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                    new scalalabEdit.EditorPaneEdit("Untitiled.java");
                      }
                     });
                    }
     }
 

 class simpleJavaApplUsingScalaLabAction  extends AbstractAction {
     public simpleJavaApplUsingScalaLabAction()  { super("simple Java Application using the scalaLab libraries"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.java");
           }
                     });
                    }
    }


class simpleJavaApplUsingScalaLabBioJavaAction  extends AbstractAction {
     public simpleJavaApplUsingScalaLabBioJavaAction()  { super("simple Java Application using the scalaLab libraries and the BioJava toolbox"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.java");
           }
                     });
                    }
    }

 
class simpleScalaApplAction  extends AbstractAction {
     public simpleScalaApplAction()  { super("simple Scala Application "); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.scala");
           
           }
                     });
                    }
    }

class simpleScalaApplUsingScalaLabAction  extends AbstractAction {
     public simpleScalaApplUsingScalaLabAction()  { super("simple Scala Application using the scalaLab libraries"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.scala");
           }
                     });
                    }
    }


class simpleScalaApplUsingScalaLabBioJavaAction  extends AbstractAction {
     public simpleScalaApplUsingScalaLabBioJavaAction()  { super("simple Scala Application using the scalaLab libraries and the BioJava Toolbox"); }
     public void actionPerformed(ActionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   new scalalabEdit.EditorPaneEdit("Untitiled.scala");
           
           }
                     });
                    }
    }

 
             
   class openProjectAction extends AbstractAction {
      public  openProjectAction() { super("Open scalaSci Project"); }
    @Override
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
           
               public void run() {
    
                   Properties settings = new Properties();  // for load/save global properties
    
                   boolean foundConfigFileFlag = false;
                   
                    String [] extensions = {"gsciProps", "props"};
//                    weka.gui.ExtensionFileFilter  scalaSciPrFilter = new weka.gui.ExtensionFileFilter(extensions, "scalaSci Project Files (\".props\", \".gsciProps\")");
                    JFileChooser  chooser = new JFileChooser(GlobalValues.workingDir);
  //                  chooser.setFileFilter(scalaSciPrFilter);
                   
                    chooser.setDialogTitle("Specify the scalaSci project properties configuration file");
                    
                    
                     int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
                    if (retVal == JFileChooser.APPROVE_OPTION) { 
                            File selectedFile = chooser.getSelectedFile();
                            String projectPropsFileName = selectedFile.getAbsolutePath();
                            FileInputStream in = null;
                            File configFile = new File(projectPropsFileName);
               if (configFile.exists())   {
                    try {
                        in = new FileInputStream(configFile);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+configFile+" for open Project, exception "+ex.getMessage());
                    }
                    
                  foundConfigFileFlag = true;
                 }
            
             if (foundConfigFileFlag==true)  { // load configuration info
                    try {
                        // load configuration info
                        settings.load(in);
                    } catch (IOException ex) {
                        System.out.println("Failing to read settings from "+configFile+" for open Project, exception "+ex.getMessage());
                    }
                    try {
                        in.close();
                    } catch (IOException ex) {
                        System.out.println("Failing to close "+configFile+" for open Project, exception "+ex.getMessage());
                    }
                    
                    GlobalValues.passPropertiesFromSettingsToWorkspace(settings);
                    
                        scalaExec.scalaLab.scalaLab.updateTree();
                        
              }
            }
           
                  }
               });
           };
       }
   
   

     class compileScalaSciScriptsOnScalaSciClasspathAction extends AbstractAction {
       compileScalaSciScriptsOnScalaSciClasspathAction() { super("Compile ScalaSci Scripts on the default ScalaSciClassPath"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
       JFrame configureScalaFrame = new JFrame("Configure Scala Frame");
       JPanel configureScalaPanel  = new JPanel();
       configureScalaPanel.setLayout(new GridLayout(3,2));
       configureScalaFrame.add(configureScalaPanel);
       configureScalaFrame.pack();
       configureScalaFrame.setVisible(true);
               }
               });
           };
       }

     class configureScalaInterpreterAction extends AbstractAction {
       configureScalaInterpreterAction() { super("Configure Scala Interpreter"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
       JFrame configureScalaFrame = new JFrame("Configure Scala Frame");
       JPanel configureScalaPanel  = new JPanel();
       configureScalaPanel.setLayout(new GridLayout(3,2));
       configureScalaFrame.add(configureScalaPanel);
       configureScalaFrame.pack();
       configureScalaFrame.setVisible(true);
               }
               });
           };
       }

   
    class newProjectAction extends AbstractAction {
       newProjectAction() { super("New scalaSci Project"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
       JFrame newProjectFrame = new JFrame("New scalaSci Frame Wizard");
       JPanel newProjectPanel  = new JPanel();
       JPanel projectRootPanel = new JPanel(new GridLayout(1, 3));
       JTextField newProjectField = new JTextField(30);
       JLabel newProjectLabel = new JLabel("scalaSci root folder: ");
       JButton newProjectBrowse = new JButton("Browse");
       projectRootPanel.add(newProjectLabel);
       projectRootPanel.add(newProjectField);
       projectRootPanel.add(newProjectBrowse);
       newProjectPanel.add(projectRootPanel);
       newProjectFrame.add(newProjectPanel);
       newProjectFrame.pack();
       newProjectFrame.setVisible(true);
               }
               });
           };
       }
   
    
   class saveProjectAction extends AbstractAction {
       saveProjectAction() { super("Save scalaSci Project"); }
    @Override
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
           
               public void run() {
    
                   Properties settings = new Properties();  // for load/save global properties
    
                   GlobalValues.passPropertiesFromWorkspaceToSettings(settings);  // pass the current properties
                   
                   boolean foundConfigFileFlag = false;
                   
                    String [] extensions = {"ssciProps", "props"};
                     JFileChooser  chooser = new JFileChooser(GlobalValues.workingDir);
                    //chooser.setFileFilter(scalaSciPrFilter);
                   
                    chooser.setDialogTitle("Specify the scalaSci project properties configuration file");
                    
                    
                     int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
                    if (retVal == JFileChooser.APPROVE_OPTION) { 
                            File selectedFile = chooser.getSelectedFile();
                            String projectPropsFileName = selectedFile.getAbsolutePath();
                            FileOutputStream out = null;
                            File configFile = new File(projectPropsFileName);
                    try {
                        out = new FileOutputStream(configFile);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+configFile+" for saving Project, exception "+ex.getMessage());
                    }
                    
                    try {
                        // save configuration info
                        settings.store(out, "");
                    } catch (IOException ex) {
                        System.out.println("Failing to save settings to "+configFile+" for saving Project, exception "+ex.getMessage());
                    }
                    try {
                        out.close();
                    } catch (IOException ex) {
                        System.out.println("Failing to close "+configFile+" for saving  Project, exception "+ex.getMessage());
                    }
                    
                    
              }
            }
               });
           };
       }
   
   class closeProjectAction extends AbstractAction {
       closeProjectAction() { super("Close ScalaSci Project"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  }
               });
           };
   }
           

         
        
           class displayHistoryAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
                            @Override
     public void run() {  // run in  EDT context */
                final JList  historyList = new JList(GlobalValues.userConsole.previousCommands);
               JPanel auxPanel = new JPanel();
               auxPanel.add(historyList);
               JScrollPane auxPane = new JScrollPane(auxPanel);
               JFrame  auxFrame = new JFrame("Command History");
               auxFrame.add(auxPane);
               auxFrame.setSize(300, 300);
               auxFrame.setLocation(100, 100);
               auxFrame.setVisible(true);
               
                historyList.addListSelectionListener(new        ListSelectionListener()
         {
            public void valueChanged(ListSelectionEvent event)
            {  
               Object[] values = historyList.getSelectedValues();

               StringBuilder text = new StringBuilder();
               for (int i = 0; i < values.length; i++)
               {  
                  String word = (String) values[i];
                  text.append(word);
                  text.append(" ");
               }
               
               // replace current command with next command
    	String textArea = GlobalValues.userConsole.getText();
    	int    pos1     = textArea.lastIndexOf("# ") + 2;
    	GlobalValues.userConsole.setText(textArea.substring(0,pos1)+text);
    	
    	// set cursor at the end of the text area
        GlobalValues.userConsole.setCaretPosition(GlobalValues.userConsole.getText().length());
            }

                            });
                           }
        
        });
      }

           

    // this handler removes the selected loaded toolbox
    class MouseAdapterForSSILoadedToolboxes extends  MouseAdapter {
             JPopupMenu  ssciToolboxesPanelPopupMenu;
             public void mouseClicked(MouseEvent evt) {
                 if (evt.getClickCount() == 2) {
                 
               System.out.println("Double Click");

                 }
             }

           public void mousePressed(MouseEvent e) {   
               
                  ssciToolboxesPanelPopupMenu  = new JPopupMenu(); 
                  ssciToolboxesPanelPopupMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
                 JMenuItem removeLocalItem = new JMenuItem("Remove loaded toolbox");
                 removeLocalItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
    
                 removeLocalItem.addActionListener(new ActionListener() {
   @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = scalaSciToolboxes.selectedLoadedToolboxIndex;   // the index of the selected toolbox from the loaded toolboxes list
                int toolboxesListSize = scalaSciToolboxes.scalaSciToolboxesLoadedListModel.getSize();
                if (selectedIndex >=0 && toolboxesListSize > 0)   {  // currently exist a selected toolbox for removal 
                    
                String toolboxName = (String) scalaSciToolboxes.scalaSciToolboxesLoadedListModel.getElementAt(selectedIndex);
                scalaSciToolboxes.scalaSciToolboxesLoadedListModel.removeElementAt(selectedIndex);
                
        if (scalaSciToolboxes.ssciToolboxes  != null)       {  // already loaded toolboxes 
            int loadedToolboxesCnt = scalaSciToolboxes.ssciToolboxes.size();
            
            System.out.println("loadedToolboxesCnt = "+loadedToolboxesCnt); 
            
        // remove the toolbox from the scalaSciToolboxes.ssciToolboxes table that keeps 
        // the name of each loaded toolbox and a Vector of the loaded classes    
            for (int toolboxIdx=0; toolboxIdx<loadedToolboxesCnt; toolboxIdx++)  {  // search for the selected toolbox
               scalaSciToolbox currentToolbox = scalaSciToolboxes.ssciToolboxes.get(toolboxIdx);    
               String currentToolboxName = currentToolbox.toolboxName;   // the name of the current toolbox
               if (currentToolboxName.equalsIgnoreCase(toolboxName) == true)   
                     {    // selected toolbox detected at the table, so remove it
               System.out.println("removing loaded toolbox : "+toolboxIdx+"  with name : "+currentToolboxName);
               scalaSciToolboxes.ssciToolboxes.remove(toolboxIdx);      // remove the Vector entry that keeps the toolbox classes
               break;
               }
            }    // search for the selected toolbox  at scalaSciToolboxes.ssciToolboxes table
            
        // remove the frame  of the toolbox classes if one exists
            JFrame  toolboxFrame = scalaSciToolboxes.framesOfToolboxClasses.get(toolboxName);
            if (toolboxFrame != null) {
                toolboxFrame.dispose();   // destroy the window displaying the toolbox classes
                scalaSciToolboxes.framesOfToolboxClasses.remove(toolboxName);
            }
          }   // already loaded toolboxes
                
                 int jarToolboxesCnt = GlobalValues.ScalaSciToolboxes.size();
            for (int toolboxIdx=0; toolboxIdx<jarToolboxesCnt; toolboxIdx++)  {  // search for the selected toolbox at the jar toolboxes for scalaSci table
               String currentToolboxName = (String) GlobalValues.ScalaSciToolboxes.elementAt(toolboxIdx);
               if (currentToolboxName.equalsIgnoreCase(toolboxName) == true)    {
                 System.out.println("removing jar toolbox entry from list,  toolboxId = "+toolboxIdx+", name = "+toolboxName);    
                 GlobalValues.ScalaSciToolboxes.removeElementAt(toolboxIdx);   
                 scalaSciToolboxes.scalaSciToolboxesLoadedListModel.removeElementAt(toolboxIdx);   
                 GlobalValues.jartoolboxesLoadedFlag.remove(toolboxName);   // remove the toolbox name in order to reinitialize the toolbox later
                 break;
               }
        }   // search for the selected toolbox  at the jar toolboxes for scalaSci table
      
                }  // currently exist a selected toolbox for removal 
   }  // actionPerformed
 });
                 ssciToolboxesPanelPopupMenu.add(removeLocalItem);
                 
               if (e.isPopupTrigger()){  
               ssciToolboxesPanelPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
             }
           }
    
            public void mouseReleased(MouseEvent e) { 
                   if (e.isPopupTrigger()){
                ssciToolboxesPanelPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
             }       
   
          }
       }
       

    class MouseAdapterForSSIAvailableToolboxes extends  MouseAdapter {
             JPopupMenu  ssciToolboxesPanelPopupMenu;
             public void mouseClicked(MouseEvent evt) {
                 if (evt.getClickCount() == 2) {
                 
               System.out.println("Double Click");

                 }
             }

            }
          }
