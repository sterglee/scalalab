
package scalaExec.scalaLab;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

import scalaExec.gui.*;
import scalaExec.Interpreter.GlobalValues;

import scalaExec.Wizards.*;
import java.awt.Color;
import java.lang.reflect.*;
import java.net.URL;
import scalaExec.ClassLoaders.ExtensionClassLoader;



// adjust the fonts of the Scala Interpreter Pane
class matlabMatFileAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        JFileChooser  chooser = new JFileChooser(GlobalValues.workingDir);
    chooser.setDialogTitle("Load .mat Matlab file in workspace");
    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
          File selectedFile = null;
    if (retVal == JFileChooser.APPROVE_OPTION) { 
       selectedFile = chooser.getSelectedFile();
    }
    
    scalaSci.math.io.MatIO.load(selectedFile.getAbsolutePath());
    
 }
}


   
   class scalaLabServerAction extends AbstractAction {
       scalaLabServerAction() { super("Control IP of ScalaLab server"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   GlobalValues.serverIP =
                           JOptionPane.showInputDialog(null,  "Specify IP of ScalaLab server", GlobalValues.serverIP);
                   GlobalValues.settings.setProperty("serverIPProp", GlobalValues.serverIP);
                           
                  }
               });
           };
   }


class scalalabScriptsPathsAction extends AbstractAction  {
       scalalabScriptsPathsAction()  { super(" Browse ScalaLabPath / Remove ScalaLabPath components"); }
       
         public void actionPerformed(java.awt.event.ActionEvent evt) {
                       JFrame paramFrame = new JFrame("Browse ScalaLabPath / Remove ScalaLabPath components");
                       paramFrame.setSize((int)(GlobalValues.scalalabMainFrame.xSizeMainFrame*0.3), (int)(GlobalValues.scalalabMainFrame.ySizeMainFrame*0.3));
                       scalaSciScriptsPathsTree paramTree = new scalaSciScriptsPathsTree();
                       paramTree.buildVariablesTree();
                       JPanel helpPanel = new JPanel();
                       JLabel helpLabelBrowse = new JLabel("The parameter frame allows to browse on the ScalalClassPath by clicking upon them");
                       JLabel helpLabelDelete = new JLabel("You can remove the selected ScalaClasspath component by pressing the DEL key");
                       JLabel helpLabelInsert = new JLabel("You can append new directories to the ScalaClasspath from the ScalalabExplorer");
                       helpPanel.setLayout(new GridLayout(3,1));
                       helpPanel.add(helpLabelBrowse);
                       helpPanel.add(helpLabelDelete);
                       helpPanel.add(helpLabelInsert);

                       JPanel paramPanel = new JPanel();
                       paramPanel.add(helpPanel);
                        paramPanel.add(paramTree);
                       paramFrame.add(paramPanel);
                       paramFrame.setVisible(true);
                       	 }
                   }
  
                 
   
   
           
           
 class  loadAllClassesFromJLabClassPathAction extends  AbstractAction    {

    public loadAllClassesFromJLabClassPathAction() {
        super("Load  All Classes from ScalaSciClassPath");
    }
    
    public  void actionPerformed(ActionEvent e)  {
        
                 ExtensionClassLoader  extClassLoader = new ExtensionClassLoader(GlobalValues.ScalaSciClassPath);
                 
                 Enumeration enumElem = GlobalValues.ScalaSciClassPathComponents.elements();
        while  (enumElem.hasMoreElements())  {   // for all ScalaSciClassPath Components
         	    Object next = (Object)enumElem.nextElement();
                    String pathName = next.toString().trim();
                     File dir = new File(pathName);
                     String[] files = dir.list();
                     if (files != null) {  
              for (int k=0; k<files.length; k++)  {   // for all files in the current directory
                      String currentFile = files[k]; 
                      
                    int idx  = currentFile.indexOf(".class");
                    int idxOfBackup = currentFile.indexOf("~");  // avoid using backup UNIX style files
                    if (idx != -1 && idxOfBackup==-1)  {  // file is a .class file
                        String  classNameToLoad = currentFile.substring(0, idx);
                        try {
                        Class  loadedClass = extClassLoader.loadClass(classNameToLoad);
                        Method m = null;
                        try {
                            m = loadedClass.getMethod("main", scalaExec.scalaLab.scalaLab.formals);
                        }
                        catch (NoSuchMethodException exc) {
                            System.out.println(" no main in  "+classNameToLoad);
                            exc.printStackTrace();
                            break;
                        }
                        
                        try {
                            m.invoke(null, scalaExec.scalaLab.scalaLab.actuals);
                        }
                        catch (Exception exc)  {
                            exc.printStackTrace();
                          }
                        }
                        
                        catch (ClassNotFoundException ex)  {
                            System.out.println("Class: "+classNameToLoad+" not found");
                            ex.printStackTrace();
                        } 
                        System.out.println("pathName ="+pathName);
                    }   // file is a .class file
                    
                    int javaIdx = currentFile.indexOf(".java");
                    idxOfBackup = currentFile.indexOf("~");
                    if (javaIdx != -1 && idxOfBackup==-1)  {
                      JavaCompile javaCompileObj = new JavaCompile();
        
         String packageName = "";
         String javaFile = pathName+File.separatorChar+currentFile;  
         boolean compilationResult = javaCompileObj.compileFile(javaFile);
         if (compilationResult == true)  // success
         {
             System.out.println("Compilation success for file "+packageName+"."+javaFile);
             int lastPos = javaFile.length()-5;  // for ".java"
             String  classNameToLoad = javaFile.substring(javaFile.lastIndexOf(File.separatorChar)+1, lastPos);
                       
           try {   // try to load the class
                        Class  loadedClass = extClassLoader.loadClass(classNameToLoad);
                        Method m = null;
                        try {
                            m = loadedClass.getMethod("main", scalaExec.scalaLab.scalaLab.formals);
                        }
                        catch (NoSuchMethodException exc) {
                            System.out.println(" no main in  "+classNameToLoad);
                            exc.printStackTrace();
                            break;
                        }
                        
                        try {
                            m.invoke(null, scalaExec.scalaLab.scalaLab.actuals);
                        }
                        catch (Exception exc)  {
                            exc.printStackTrace();
                          }
           }  // try to load the class
                        
                        catch (ClassNotFoundException ex)  {
                            System.out.println("Class: "+classNameToLoad+" not found");
                            ex.printStackTrace();
                        } 
                        System.out.println("pathName ="+pathName);
                   
            }   // compilation result success
         }    // javaIdx != -1
              }   // for all files in the current directory
                                            }   // files != null
        }  // for all ScalaSciClassPath Components
    }    
 }                    




class asciiFileAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
                    
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("ASCIIDataFiles.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
        }
  }
 }



// adjust the fonts of the RSyntaxArea based editor
   class rsyntaxFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
            if (GlobalValues.globalRSyntaxEditorPane==null)
                JOptionPane.showMessageDialog(null, "Please display first the RSyntaxTextArea based editor from the Edit menu, then retry");
            else {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 Font choosingFont = myFontChooser.getFont();
 GlobalValues.globalRSyntaxEditorPane.setFont(choosingFont);
         }
       }
   }
                
// adjust the fonts of the Scala Interpreter Pane
   class paneFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 Font choosingFont = myFontChooser.getFont();
 GlobalValues.editorPane.setFont(choosingFont);
         }
       }

   class UIFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 GlobalValues.uifont = myFontChooser.getFont();
         }
       }

// popup font adjuster
 class pUIFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 GlobalValues.puifont = myFontChooser.getFont();
            }
       }



// GUI font adjuster
 class gUIFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 GlobalValues.guifont = myFontChooser.getFont();
            }
       }

// HTML fonts 
 class htmlFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 GlobalValues.htmlfont = myFontChooser.getFont();
            }
       }

// buttons font adjuster
 class bUIFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 GlobalValues.buifont = myFontChooser.getFont();
            }
       }

             
// adjust the fonts of the output Console
   class outConsFontAdjusterAction extends AbstractAction  {
        public void actionPerformed(ActionEvent e) {
 JFontChooser  myFontChooser = new JFontChooser(GlobalValues.scalalabMainFrame);
 myFontChooser.setVisible(true);
 Font choosingFont = myFontChooser.getFont();
 GlobalValues.consoleOutputWindow.output.setFont(choosingFont);
         }
       }

     
   
       class FontAdjusterAction extends AbstractAction  {
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

     class HTMLMagAdjustAction extends AbstractAction {
         public void actionPerformed(ActionEvent e) {
             String newMagFactor = String.valueOf(GlobalValues.helpMagnificationFactor);
             newMagFactor =         JOptionPane.showInputDialog(null, "Specify default magnification factor for HTML Help",GlobalValues.helpMagnificationFactor);
             if (newMagFactor != null)
               GlobalValues.helpMagnificationFactor = Double.parseDouble(newMagFactor);
         }
     }

       class LookAndFeelAdjusterAction extends AbstractAction {
           public void actionPerformed(ActionEvent e) {
               JFrame lookAndFeelFrame = new JFrame("Configure Look and Feel");
               JButton nativeLookAndFeel = new JButton("Native Look and Feel");
               nativeLookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           try {
            String sysLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(sysLookAndFeel);
            scalaExec.scalaLab.scalaLab.updateTree();
	} catch (Exception exc) {
	    System.err.println("Error loading L&F: " + exc);
	}
      }
        });
        
        JPanel lookAndFeelConfigPanel = new JPanel();
        
          JButton crossPlatformLookAndFeel = new JButton("Cross Platform Look and Feel");
               crossPlatformLookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           try {
            String  crossPlatformLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(crossPlatformLookAndFeel);
            scalaExec.scalaLab.scalaLab.updateTree();
	} catch (Exception exc) {
	    System.err.println("Error loading L&F: " + exc);
	}
      }
        });

        lookAndFeelConfigPanel.add(nativeLookAndFeel);
        lookAndFeelConfigPanel.add(crossPlatformLookAndFeel);
        lookAndFeelFrame.add(lookAndFeelConfigPanel);
        lookAndFeelFrame.setSize(300, 200);
        lookAndFeelFrame.setVisible(true);
           }
       }
       
       
   class browseFileSysForPaths extends AbstractAction {

    public browseFileSysForPaths() {
      super("Browse File System For Updating Class Paths");
    }
       
    @Override
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
            @Override
           public void run() {  // run in  */
     String initialSelectionDir = "/";
     if (GlobalValues.hostIsUnix==false)
         initialSelectionDir = "c:\\";
     
     initialSelectionDir =    JOptionPane.showInputDialog("Specify the root of the file system to browse", initialSelectionDir);
     
                try {
                FileTreeExplorer ftree = new FileTreeExplorer(initialSelectionDir);
                GlobalValues.currentFileExplorer = ftree;
                JFrame treeFrame = new JFrame("Select directory");
                treeFrame.add(new JScrollPane(ftree.pathsTree));
                treeFrame.setSize(600, 500);
                treeFrame.setVisible(true);
                }
                catch (FileNotFoundException fnfexce) { 
                    System.out.println("File not found exception in FileTreeExplorer");
                    fnfexce.printStackTrace();
                } 
    }
                           });
    
    }
   }
   
   
 class configAlphaAction extends AbstractAction {
     configAlphaAction() { super("Configuring alpha - transparency parameter"); }
     public void actionPerformed(ActionEvent e) {
                 
  SwingUtilities.invokeLater(new Runnable() {
public void run() {  // run in  */
      
String newAlpha = JOptionPane.showInputDialog("Alpha Parameter",  GlobalValues.alphaComposite);
GlobalValues.alphaComposite = Float.valueOf(newAlpha);
}
});
                 }
      
        
     }
 

 class promptConfigAction extends AbstractAction { 
     promptConfigAction()   {super("Switches Display/Hide working directory at ScalaLab's Console prompt. Current displaying directory  is:  "+GlobalValues.displayDirectory); }
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
            @Override
           public void run() {  // run in  */
      GlobalValues.displayDirectory = !GlobalValues.displayDirectory;
      GlobalValues.scalalabMainFrame.getPromptJMenuItem().setText("Switches Display/Hide working directory at prompt. Current displaying directory  is:  "+GlobalValues.displayDirectory); 
      GlobalValues.scalalabMainFrame.scalalabConsole.displayPrompt();
            }
                           }
                           );
                
                    }
   }
 
 
 
class browseJavaClassesAction extends AbstractAction {
       browseJavaClassesAction()  {super("Browse file system for specifyng new ScalaSci ClassPath component"); }
       
    @Override
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
      
    JFileChooser  chooser = new JFileChooser(GlobalValues.workingDir);
    chooser.setDialogTitle("Browse file system for specifyng new ScalaSci ClassPath component");
    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    if (retVal == JFileChooser.APPROVE_OPTION) { 
      File selectedFile = chooser.getSelectedFile();
      String SelectedFileWithPath = selectedFile.getAbsolutePath();
      String SelectedFilePathOnly = SelectedFileWithPath.substring(0, SelectedFileWithPath.lastIndexOf(File.separatorChar));
      if (GlobalValues.ScalaSciClassPath.indexOf(SelectedFilePathOnly)==-1)  {
        GlobalValues.ScalaSciClassPathComponents.add(0,SelectedFilePathOnly);
        GlobalValues.ScalaSciClassPath = GlobalValues.ScalaSciClassPath+File.pathSeparator+SelectedFilePathOnly;
        GlobalValues.ScalaSciUserPaths.add(SelectedFilePathOnly);  // add to the user specified directories
        }
          // update also the ScalaSciClassPath property
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce.toString().trim());
    }
    GlobalValues.settings.setProperty("ScalaSciClassPathProp", fileStr.toString());
    
      ClassLoader parentClassLoader = getClass().getClassLoader();
      GlobalValues.extensionClassLoader = new  ExtensionClassLoader(GlobalValues.ScalaSciClassPath, parentClassLoader);
      
    
                    scalaExec.scalaLab.scalaLab.updateTree();
                    
                }
                     }
              });
                
     }
}
 

// this class permits the user to browse the filesystem and to append directories to the ScalaSciClassPath   
class browseScalaSciFilesAction extends AbstractAction {
       browseScalaSciFilesAction()  {super("Browse File System for specifying new ScalaSciClassPath component"); }
       
    @Override
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
            @Override
           public void run() {  // run in  */
                     
    JFileChooser  chooser = new JFileChooser(GlobalValues.workingDir);
    chooser.setDialogTitle("Browse file system and select your file to update ScalaSciClassPath to include it");
    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
    
      if (retVal == JFileChooser.APPROVE_OPTION) {    // update the paths according to the selected file
      File selectedFile = chooser.getSelectedFile();  // the selected file
      String SelectedFileWithPath = selectedFile.getAbsolutePath();   // the full path of the selected file
      String SelectedFilePathOnly = SelectedFileWithPath.substring(0, SelectedFileWithPath.lastIndexOf(File.separatorChar)+1); // extract the path only
        
      int idx = -1; 
      // update structures for ScalaSciClassPath
      if (GlobalValues.ScalaSciClassPath !=null) 
          idx = GlobalValues.ScalaSciClassPath.indexOf(SelectedFilePathOnly);
      boolean appendPath = (GlobalValues.ScalaSciClassPath==null) || (idx == -1);
      if (appendPath)        
               {    // path not already exist in ScalaSciClassPath
        
          // append the new path to the ScalaSci's path
        scalaSciCommands.BasicCommands.appendClasspath(SelectedFilePathOnly);
               
          GlobalValues.ScalaSciUserPaths.add(SelectedFilePathOnly);  // append to the user specified path components for retrieving Scala classes    
            
          // rebuild the class path property (i.e. "ScalaSciClassPathProp") in order to account for the new path
          StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce.toString().trim());
    }
      GlobalValues.ScalaSciClassPath = fileStr.toString();   // the ScalaSci classpath as a String
      GlobalValues.settings.setProperty("ScalaSciClassPathProp", GlobalValues.ScalaSciClassPath);    // keep in property in order to be saved
      }   // path not already exist in ScalaSciClassPath
      
           
      // update structures for ScalaSciUserPaths
      if (GlobalValues.ScalaSciUserPaths.contains(SelectedFilePathOnly)==false)        
               {    // path not already exist in ScalaSciUserPaths
          GlobalValues.ScalaSciUserPaths.add(SelectedFilePathOnly);  // append to the ScalaSciUserPaths components for retrieving Scala classes
            
          // rebuild the ScalaSci User paths property (i.e. "ScalaSciUserPathsProp") in order to account for the new path
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciUserPaths.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
      GlobalValues.settings.setProperty("ScalaSciUserPathsProp", fileStr.toString());    // keep in property in order to be saved
      }  // path not already exist in ScalaSciUserPaths
      
    scalaExec.scalaLab.scalaLab.updateTree();
    
            
     }  // update the paths according to the selected file
    }
                     
            
  });
                
 }
   
    
   class commandHistoryAction extends AbstractAction {
       commandHistoryAction()  {super("Displays the command history"); }
       
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
         });  // new ListSelectionListener

         
                            }  // run in EDT context
                          });  // new Runable()
                     }  // actionPerformed
                 }
}



   class controlMainToolBarAction extends AbstractAction {
           controlMainToolBarAction() {
               super("Show/Hide Main Toolbar");
           }
                    public void actionPerformed(ActionEvent e) {
        
                        if (GlobalValues.mainToolbarVisible == false ) {
        
                            GlobalValues.mainToolbarVisible = true; 
                            
                         if (GlobalValues.toolbarFrame  != null )   {  // remove any previous toolbar
        GlobalValues.toolbarFrame.removeAll();
        GlobalValues.toolbarFrame.dispose();
                     }
                         
        GlobalValues.toolbarFrame = new JFrame("Toolbars");
        // prepare "Close" option
        JMenu closeToolbarsMenu = new JMenu("Close");
        JMenuItem closeToolbarsMenuItem = new JMenuItem("Close");
        closeToolbarsMenuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
               GlobalValues.mainToolbarVisible = false;     
               GlobalValues.toolbarFrame.dispose();
                }
            });
        closeToolbarsMenu.add(closeToolbarsMenuItem);
        JMenuBar closeMenuBar = new JMenuBar();
        closeMenuBar.add(closeToolbarsMenu);
        GlobalValues.toolbarFrame.setJMenuBar(closeMenuBar);
        
        scalaExec.Interpreter.GlobalValues.scalalabMainFrame.InitTabbedToolbars();                     
        GlobalValues.toolbarFrame.add(GlobalValues.scalaSciTabbedToolBar);
        GlobalValues.toolbarFrame.setSize(GlobalValues.jfExplorerFrame.getSize().width, 300);
        GlobalValues.toolbarFrame.setLocation(GlobalValues.jfExplorerFrame.getLocation().x, GlobalValues.jfExplorerFrame.getLocation().y+ GlobalValues.jfExplorerFrame.getSize().height );
        GlobalValues.toolbarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GlobalValues.toolbarFrame.setVisible(true);
           
                    }
                        else {
           GlobalValues.mainToolbarVisible = false;
                      if  (GlobalValues.toolbarFrame  != null )   {  // remove any previous toolbar
        GlobalValues.toolbarFrame.removeAll();
        GlobalValues.toolbarFrame.dispose();
                     }  
                }
         }
   }


class controlScalaCompilerAction extends AbstractAction {

    public controlScalaCompilerAction() {
      super("Controls basic options of the Scala Compiler as optimization and JVM target generation");
    }

    public void actionPerformed(ActionEvent e)  {
        JFrame compilerConfigFrame = new JFrame("Scala Compiler Configuration");
        compilerConfigFrame.setLayout(new GridLayout(3,2));
        
        JButton configButton = new JButton("Config Scala");
        configButton.setToolTipText("Controlling the Scala Compiler used by ScalaLab properties ( usually makes small differences) ");
        compilerConfigFrame.add(configButton);
        compilerConfigFrame.add(new JLabel("These options require an Interpreter restart and are saved upon exit"));
        compilerConfigFrame.add(new JLabel("Optimization Switch"));
        JCheckBox optimizationCheckBox = new JCheckBox("Optimization Flag ");
        optimizationCheckBox.setToolTipText("Checked means compiler performs optimizations");
        optimizationCheckBox.setSelected(GlobalValues.compilerOptimizationFlag);
        System.out.println("checkOptCheckBox state = "+GlobalValues.compilerOptimizationFlag);
        optimizationCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              GlobalValues.compilerOptimizationFlag = !GlobalValues.compilerOptimizationFlag;
              System.out.println("Current state of Scala Compiler optimization flag = "+ GlobalValues.compilerOptimizationFlag);
            }
        });
        compilerConfigFrame.add(optimizationCheckBox);
        
        compilerConfigFrame.add(new JLabel("JVM target setup"));
        
        final JComboBox  JVMTargetComboBox = new JComboBox(GlobalValues.targetSetting);
        JVMTargetComboBox.setSelectedIndex(GlobalValues.currentTargetSelectionIndex);
        
        JVMTargetComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          GlobalValues.currentTargetSelectionIndex = JVMTargetComboBox.getSelectedIndex();
          System.out.println("Selected for target code generation: "+GlobalValues.targetSetting[GlobalValues.currentTargetSelectionIndex]);
                  }  
        });
    
        compilerConfigFrame.add(JVMTargetComboBox);
        compilerConfigFrame.pack();
        compilerConfigFrame.setLocation(GlobalValues.scalalabMainFrame.getX()+200, GlobalValues.scalalabMainFrame.getY());
        compilerConfigFrame.setVisible(true);
                }
    
    
    
}



class controlPrecisionAction extends AbstractAction {

    public controlPrecisionAction() {
      super("Controls precision of displayed numbers and truncation of large matrices");
    }

    public void actionPerformed(ActionEvent e)  {
        JFrame precisionConfigFrame = new JFrame("Display Precision and Matrix display truncation Configuration");
        precisionConfigFrame.setLayout(new GridLayout(6,2));
        
        precisionConfigFrame.add(new JLabel("Decimal digits for matrices"));
        final JTextField  precisionMatEditField = new JTextField(java.lang.String.valueOf(scalaSci.PrintFormatParams.getMatDigitsPrecision()));
        precisionMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              scalaSci.PrintFormatParams.setMatDigitsPrecision(java.lang.Integer.parseInt(precisionMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(precisionMatEditField);
        
        precisionConfigFrame.add(new JLabel("Decimal digits for vectors"));
        final JTextField  precisionVecEditField = new JTextField(java.lang.String.valueOf(scalaSci.PrintFormatParams.getVecDigitsPrecision()));
        precisionVecEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scalaSci.PrintFormatParams.setVecDigitsPrecision(java.lang.Integer.parseInt(precisionVecEditField.getText()));
                
            }
        });
        precisionConfigFrame.add(precisionVecEditField);
        
        
        precisionConfigFrame.add(new JLabel("Number of rows for matrices"));
        final JTextField  rowsMatEditField = new JTextField(java.lang.String.valueOf(scalaSci.PrintFormatParams.getMatMxRowsToDisplay()));
        rowsMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              scalaSci.PrintFormatParams.setMatMxRowsToDisplay(java.lang.Integer.parseInt(rowsMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(rowsMatEditField);
        
        precisionConfigFrame.add(new JLabel("Number of columns  for matrices"));
        final JTextField colsMatEditField = new JTextField(java.lang.String.valueOf(scalaSci.PrintFormatParams.getMatMxColsToDisplay()));
        colsMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scalaSci.PrintFormatParams.setMatMxColsToDisplay(java.lang.Integer.parseInt(colsMatEditField.getText()));
                
            }
        });
        precisionConfigFrame.add(colsMatEditField);
      
        
        precisionConfigFrame.add(new JLabel("Verbose"));
        final JCheckBox verboseCheckBox = new JCheckBox("Verbose Flag ");
        verboseCheckBox.setToolTipText("Verbose off stops the output of some results");
        verboseCheckBox.setSelected(scalaSci.PrintFormatParams.getVerbose());
        verboseCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              scalaSci.PrintFormatParams.setVerbose(verboseCheckBox.isSelected());
            }
        });
        precisionConfigFrame.add(verboseCheckBox);
                
        JButton acceptButton = new JButton("Accept all text field values");
        acceptButton.setToolTipText("Press to read all the contents of text fields (each individual text field is readed by pressing ENTER");
        acceptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
        scalaSci.PrintFormatParams.setMatDigitsPrecision(java.lang.Integer.parseInt(precisionMatEditField.getText()));
        scalaSci.PrintFormatParams.setVecDigitsPrecision(java.lang.Integer.parseInt(precisionVecEditField.getText()));
        scalaSci.PrintFormatParams.setMatMxRowsToDisplay(java.lang.Integer.parseInt(rowsMatEditField.getText()));
        scalaSci.PrintFormatParams.setMatMxColsToDisplay(java.lang.Integer.parseInt(colsMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(acceptButton);
        
        JButton displayButton = new JButton("Display the current parameters");
        displayButton.setToolTipText("Displays the current parameter setting");
        displayButton.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
    System.out.println("Matrix Digits Precision = "+scalaSci.PrintFormatParams.getMatDigitsPrecision());
    System.out.println("Vector Digits Precision = "+scalaSci.PrintFormatParams.getVecDigitsPrecision());
    System.out.println("Rows to display = "+scalaSci.PrintFormatParams.getMatMxRowsToDisplay());
    System.out.println("Columns to display = "+scalaSci.PrintFormatParams.getMatMxColsToDisplay());
            }
          });
       precisionConfigFrame.add(displayButton);         
        
        precisionConfigFrame.pack();
        precisionConfigFrame.setLocation(GlobalValues.scalalabMainFrame.getX()+200, GlobalValues.scalalabMainFrame.getY());
        precisionConfigFrame.setVisible(true);
    
    }
    
    
    
}



// display the currentlyh installed toolboxes
 class displayCurrentToolboxesAction  extends AbstractAction {
   displayCurrentToolboxesAction() {
       super("Display which toolboxes are currently installed");
   }
   public void actionPerformed(ActionEvent  e)  {
       JFrame toolboxesInfoFrame = new JFrame("Currently installed toolboxes");
       int numAllPathComponents  = GlobalValues.ScalaSciClassPathComponents.size();
       int numToolboxes = 0;
       for (int k=0; k< numAllPathComponents; k++)  {
           String currentPathComponent = GlobalValues.ScalaSciClassPathComponents.elementAt(k);
           if (currentPathComponent.contains(".jar") && (!currentPathComponent.contains("/lib")) ) 
           numToolboxes++;
       }
       
       JPanel  toolboxesPanel = new JPanel();
       toolboxesPanel.setLayout(new GridLayout(numToolboxes+1, 1));
       if (numToolboxes == 0) {
           toolboxesPanel.setLayout(new GridLayout(numToolboxes+1, 1));
           toolboxesPanel.add(new JLabel("You have no installed toolboxes. You can install toolboxes using the ScalaSci Tolboxes tab"));
       }
       else {
           toolboxesPanel.setLayout(new GridLayout(numToolboxes+2, 1));
           toolboxesPanel.add(new JLabel("The currently installed toolboxes are( You can remove toolboxes using the ScalaLab Explorer):"));
           toolboxesPanel.add(new JLabel("-----------------------------------------------------------------------------------------------"));
       for (int k=0; k< numToolboxes; k++)  {
           String currentPathComponent = GlobalValues.ScalaSciClassPathComponents.elementAt(k);
           if (currentPathComponent.contains(".jar") && (!currentPathComponent.contains("/lib"))) 
           {
               String toolboxName = GlobalValues.ScalaSciClassPathComponents.elementAt(k);
              toolboxesPanel.add(new JLabel(toolboxName));
           }
       }
       }    
      toolboxesInfoFrame.add(toolboxesPanel);
      toolboxesInfoFrame.pack();
      toolboxesInfoFrame.setVisible(true);
       }
   }
     
 
 
   class explorerAction extends AbstractAction {
           explorerAction() {
               super("Show/Hide ScalaLab Explorer, state is saved upon exit");
           }
                    public void actionPerformed(ActionEvent e) {
                        GlobalValues.explorerVisible = !GlobalValues.explorerVisible;
                        if (GlobalValues.explorerVisible) 
                            GlobalValues.scalalabMainFrame.createExplorerPanel();
                        else {
                            GlobalValues.jfExplorerFrame.removeAll();
                            GlobalValues.jfExplorerFrame.dispose();
                        }
                    }
                }

class resetInterpreterAction extends AbstractAction {
           resetInterpreterAction() {
               super("Create a new Scala Interpreter that includes ScalaSciClassPath componets");
           }
                    public void actionPerformed(ActionEvent e) {
  GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForClearAll();
                }
                    

 }
                
   
              
              
       
 
             
    
       
