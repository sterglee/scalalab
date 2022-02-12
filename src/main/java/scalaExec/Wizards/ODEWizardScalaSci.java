
package scalaExec.Wizards;

import scalaExec.ClassLoaders.ExtensionClassLoader;
import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Vector;

import scalaExec.gui.GBC;

// synthesize the proper Java code and the appropriate scalaSci-Script
import java.util.Enumeration;
import scala.reflect.io.Directory;
import scalaExec.scalaLab.StreamGobbler;
// for Java-based solution of ODEs implementation
  
     // processes generation of script code
     
     public   class ODEWizardScalaSci  {
         String editingClassName = "";
         int systemOrder;   // the order of ODE system

         static final  int   ODErke = 0;
         static final  int   ODEmultistep = 1;
         static final  int   ODEdiffsys = 2;
         
         static public String packageName = "";
         static  int   ODESolveMethod = ODErke;   // default choosen method
         
        Vector availODEMethods = new Vector();    // keeps all the available ODE methods that the wizard supports 
         
         JButton  copyTemplateButton, generateEditingButton, saveJavaClassButton,
                 compileJavaClassButton, compileJavaInternalCompilerButton, generateScriptCodeButton;
         JPanel editPanel, bottomPanel, paramPanel, statusPanel;
         
         JFrame ODEWizardFrame;  // the main frame of the ODE wizard
         static JFrame scriptFrame;  // the frame where the produced ScalaSci code is displayed
                  
        JTextField javaFileTextBox;   // where to save the Java class that implements the specific ODE
        JTextField systemOrderText;  // the system order that the ODE implements
        
        JTextArea statusAreaTop, statusAreaBottom;
         
         JTextArea templateTextArea;
         String  templateText;
             // template text for rke() ODE method
         final String   templateTextRKE= "public void der(int n, double t, double y[]) \n"+
                "{ \n"+ "  double xx,yy,zz; \n"+ "\n    xx=y[1];     yy=y[2];     zz=y[3]; \n"+
                 "\n    y[1] = 10*(yy-xx);  \n" +"    y[2] = -xx*zz+143*xx - yy;  \n"+
                 "    y[3] = xx*yy - 2.66667*zz;  \n"+"  } \n"+
                 " \n\npublic void out(int n, double t[], double te[], double y[], double data[])  {  } \n";
         
       

        // template text for multistep() ODE method
         final String   templateTextMultistep = "  // an example template implementing the Lorenz attractor \n"+
                 "public void deriv(double df[], int n, double x[], double y[])  { \n"+
            "double xx,yy,zz; \n"+
            "    xx=y[1];     yy=y[2];     zz=y[3]; \n"+
            "   df[1] = 10*(yy-xx); \n     df[2] = -xx*zz+143*xx - yy; \n     df[3] = xx*yy - 2.66667*zz; \n   } \n \n"+
            "     public boolean available(int n, double x[], double y[], double jac[][])  { \n"+
            "       jac[1][1] = -10;     jac[1][2] = 10;  jac[1][3] = 0; \n"+
            "       jac[2][1] = -y[3];   jac[2][2] = -1;  jac[2][3] = -y[1]; \n"+
            "       jac[3][1] = y[2];    jac[3][2] = 0;  jac[3][3] = -2.66; \n"+
            "\n        return true; \n     } \n\n"+
                 "    public void out(double h, int k, int n, double x[], double y[]) { \n"+
                " return; \n } \n"; 

         // template text for diffsys() ODE method
         final String   templateTextDiffsys = "  // an example template implementing the Lorenz attractor \n"+
            "public void derivative(int n, double x, double y[], double dy[])  { \n"+
            "double xx,yy,zz; \n"+
            "xx=y[1];     yy=y[2];     zz=y[3]; \n"+
            "dy[1] = 10*(yy-xx); \n"+
            "dy[2] = -xx*zz+143*xx - yy; \n"+
            "dy[3] = xx*yy - 2.66667*zz; \n   }\n\n\n"+
            " public void output(int n, double x[],  double xe, double y[], double dy[])  { \n"+
            "return; \n  } \n";
 
                 
        final String [] implementingInterfaces ={  "AP_rke_methods", "AP_multistep_methods",
          "AP_diffsys_methods" };
                           
         JScrollPane templateScrollPane;
         JViewport templateViewPort;
         
         JTextArea ODEWizardTextArea;
         JScrollPane ODEWizardScrollPane;
         String    ODEWizardText;
         JViewport wizardViewPort;
         
         JPanel paramMethodPanel;
         JLabel  ODEselectMethodLabel;
         JComboBox ODEselectMethodComboBox;
         JLabel  currentlySelectedLabel;
            
         public ODEWizardScalaSci() {
            editingClassName = "Lorenz";
            systemOrder=3;   // the order of ODE system
            
            copyTemplateButton = new JButton("1. Copy and Edit Template", new ImageIcon("/scalaLab.jar/yellow-ball.gif"));  
            generateEditingButton = new JButton("2. Generate Java Class", new ImageIcon("./blue-ball.gif"));
            saveJavaClassButton = new JButton("3. Save Java Class", new ImageIcon("scalaLab.jar/red-ball.gif"));
            compileJavaClassButton = new JButton("4.a. Java Compile - External Compiler", new ImageIcon("blue-ball.gif") );
            compileJavaInternalCompilerButton = new JButton("4.b. Java Compile - Internal Compiler", new ImageIcon("blue-ball.gif"));
            generateScriptCodeButton = new JButton("Generate scalaSci Script", new ImageIcon("red-ball.gif"));
            
            
            ODEWizardFrame = new JFrame("ODE Wizard for scalaSci with Java implementation of. ODEs");
         
            editPanel = new JPanel();
            editPanel.setLayout(new GridLayout(1,2));

            
            paramPanel = new JPanel(new GridLayout(1, 4));
            availODEMethods.add("ODErke"); 
            availODEMethods.add("ODEmultistep");
            availODEMethods.add("ODEdiffsys");
            ODEselectMethodLabel = new JLabel("ODE method: ");
            ODEselectMethodComboBox = new JComboBox(availODEMethods);
            ODEselectMethodComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ODESolveMethod = ODEselectMethodComboBox.getSelectedIndex();
                    updateTemplateText();
                    templateTextArea.setText(templateText);
                    currentlySelectedLabel.setText("Selected Method: "+(String)availODEMethods.get(ODESolveMethod));
                       }
            });
            
            JLabel  javaFileTextLabel = new JLabel("Java File Name: ");
            javaFileTextBox = new JTextField(editingClassName);
            javaFileTextBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  editingClassName = javaFileTextBox.getText();
                   String updatedStatusText = prepareStatusText();
                  statusAreaTop.setText(updatedStatusText);
                }
            });
           
            JLabel  systemOrderLabel = new JLabel("System order: ");
            systemOrderText = new JTextField(String.valueOf(systemOrder));
            systemOrderText.addActionListener(new editSystemOrder());
            JPanel  javaFilePanel = new JPanel();
            javaFilePanel.add(javaFileTextLabel);
            javaFilePanel.add(javaFileTextBox);
            JPanel systemOrderPanel = new JPanel();
            systemOrderPanel.add(systemOrderLabel);
            systemOrderPanel.add(systemOrderText);
         
            paramMethodPanel = new JPanel();
            paramMethodPanel.add(ODEselectMethodLabel);
            paramMethodPanel.add(ODEselectMethodComboBox);
            paramPanel.add(paramMethodPanel);
            paramPanel.add(javaFilePanel);
            paramPanel.add(systemOrderPanel);
            currentlySelectedLabel = new JLabel("Selected Method: "+(String)availODEMethods.get(ODESolveMethod));
            paramPanel.add(currentlySelectedLabel);
            
            statusPanel = new JPanel(new GridLayout(2,1));
            statusAreaTop = new JTextArea();
            statusAreaTop.setFont(new Font("Arial", Font.BOLD, 16));
            String statusText = prepareStatusText();
          
            statusAreaTop.setText(statusText);
            statusAreaBottom = new JTextArea();
            statusAreaBottom.setText("Step1:  Copy and edit the template ODE  (implements the famous Lorenz chaotic system),\n"+
                    "Then set the name of your Java Class (instead of \"Lorenz\"),  without the extension .java\n"+
                    "Also set the proper order (i.e. number of equations and variables) of your system. ");                              
            statusPanel.add(statusAreaTop);
            statusPanel.add(statusAreaBottom);
            
            
            templateTextArea = new JTextArea();
            updateTemplateText();
            
            templateTextArea.setFont(new Font("Arial", Font.ITALIC, 12));
            templateTextArea.setText(templateText);
            templateScrollPane = new JScrollPane();
            templateViewPort = templateScrollPane.getViewport();
            templateViewPort.add(templateTextArea);
	 
            ODEWizardTextArea = new JTextArea();
            ODEWizardText = "";
            ODEWizardTextArea.setText(ODEWizardText);
            ODEWizardTextArea.setFont(new Font("Arial", Font.BOLD, 12));
 
            ODEWizardScrollPane = new JScrollPane();
            wizardViewPort = ODEWizardScrollPane.getViewport();
            wizardViewPort.add(ODEWizardTextArea);
	
            editPanel.add(ODEWizardScrollPane);
            editPanel.add(templateScrollPane);
                  
         // Step 1: copy template of ODE implementation from the
         // templateTextArea to ODEWizardTextArea
         copyTemplateButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           ODEWizardTextArea.setText(templateTextArea.getText());
           generateEditingButton.setEnabled(true);    
           statusAreaBottom.setText("Step2:  If you have implemented correctly your ODE, the wizard completes the ready to compile Java class");
             }
          }                 
         );
         
         // Step 2: generate Java Class from template 
         JPanel buttonPanel = new JPanel();
         generateEditingButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           String editingODE = ODEWizardTextArea.getText();
           String classImplementationString =  // "package javaPluggins; \n"+
            "import  numal.*; \n\n"+
            "public class "+editingClassName+" extends Object \n             implements "+implementingInterfaces[ODESolveMethod]+
                   " \n \n "+ "{ \n";
           
           classImplementationString += ( editingODE+"}\n");
             
           ODEWizardTextArea.setText(classImplementationString);
           saveJavaClassButton.setEnabled(true);
           statusAreaBottom.setText("Step3:  The generated Java source is ready, you can check it, and then proceed to save.");
           
             }
         });
         
         // Step 3: save generated Java Class on disk
         saveJavaClassButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
          String currentWorkingDirectory = Directory.Current().get().path();
          JFileChooser chooser = new JFileChooser(currentWorkingDirectory);
          chooser.setSelectedFile(new File(editingClassName+".java"));
          int ret = chooser.showSaveDialog(ODEWizardFrame);

          if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
         File f = chooser.getSelectedFile();
         try {
         PrintWriter out = new PrintWriter(f);
         String javaCodeText = ODEWizardTextArea.getText();
         out.write(javaCodeText);
         out.close();
         // update the notion  of the working directory
         String fullPathOfSavedFile = f.getAbsolutePath();
         GlobalValues.workingDir = fullPathOfSavedFile.substring(0, fullPathOfSavedFile.lastIndexOf(File.separatorChar)+1);

         compileJavaClassButton.setEnabled(true);
         compileJavaInternalCompilerButton.setEnabled(true);
         statusAreaBottom.setText("Step4:  The Java source file was saved to disk,  \n "+
                 "you can proceed to compile and load the corresponding class file");
             }
         catch (java.io.FileNotFoundException enf) {
             System.out.println("File "+f.getName()+ " not found");
             enf.printStackTrace();
          }
          catch (Exception eOther) {
              System.out.println("Exception trying to create PrintWriter");
              eOther.printStackTrace();
          }   
           }
         });
         
         
         // Step 4: Compile the generated Java class
         compileJavaClassButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
           String currentWorkingDirectory = GlobalValues.workingDir;
          JFileChooser chooser = new JFileChooser(currentWorkingDirectory);
          int ret = chooser.showOpenDialog(ODEWizardFrame);

          if (ret != JFileChooser.APPROVE_OPTION) {
                return;
         
              }
         File f = chooser.getSelectedFile();
         String javaFile=null;
         try {
            javaFile = f.getCanonicalPath();
            }
         catch (IOException ex) {
               System.out.println("I/O Exception in getCanonicalPath");
               ex.printStackTrace();             
         }
      
   //   extract the path specification of the generated Java class that implements the ODE solution method,
  //    in order to update the ScalaSci class path        
      String SelectedFileWithPath = f.getAbsolutePath();
      String SelectedFilePathOnly = SelectedFileWithPath.substring(0, SelectedFileWithPath.lastIndexOf(File.separatorChar));
   
      if (GlobalValues.ScalaSciClassPath.length()==0)
          GlobalValues.ScalaSciClassPath=".";
      if (GlobalValues.ScalaSciClassPath.indexOf(SelectedFilePathOnly)==-1)  {
        GlobalValues.ScalaSciClassPathComponents.add(0,SelectedFilePathOnly);
        GlobalValues.ScalaSciClassPath = GlobalValues.ScalaSciClassPath+File.pathSeparator+SelectedFilePathOnly;
        }
          // update also the ScalaSciClassPath property
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
    GlobalValues.settings.setProperty("ScalaSciClassPath", fileStr.toString());
    
      ClassLoader parentClassLoader = getClass().getClassLoader();
      GlobalValues.extensionClassLoader = new  ExtensionClassLoader(GlobalValues.ScalaSciClassPath, parentClassLoader);
       
      // update GUI components to account for the updated ScalaSci classpath
                scalaExec.scalaLab.scalaLab.updateTree();
                
      boolean compilationSucccess = true;
   
      String tempFileName = "";
tempFileName = javaFile+".java";   // public classes and Java files should have the same name

      String [] command  = new String[6];
       command[0] =  "javac";
       command[1] = "-cp";
       command[2] =  GlobalValues.jarFilePath+File.pathSeparator+GlobalValues.homeDir;
       command[3] = "-d";   // where to place output class files
       command[4] = SelectedFilePathOnly;   //  the path to save the compiled class files
       command[5] =  javaFile;  // full filename to compile

       String compileCommandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3]+" "+command[4]+" "+command[5];

       System.out.println("compileCommand Java= "+compileCommandString);

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

                // any error???
                javaProcess.waitFor();
                int rv = javaProcess.exitValue();
                String commandString = command[0]+"  "+command[1]+"  "+command[2];
                if (rv==0) {
                 System.out.println("Process:  "+commandString+"  exited successfully ");
                  generateScriptCodeButton.setEnabled(true);
                   statusAreaBottom.setText("Step5:  You can proceed now to create a draft script that utilizes your Java-based  ODE integrator");
                }
                else
                 System.out.println("Process:  "+commandString+"  exited with error, error value = "+rv);

                } catch (IOException exio) {
                    System.out.println("IOException trying to executing "+command);
                    exio.printStackTrace();

                }
               catch (InterruptedException ie) {
                    System.out.println("Interrupted Exception  trying to executing "+command);
                    ie.printStackTrace();
                }
   
                    }
         });
         
                 // Compile with the internal compiler
                 compileJavaInternalCompilerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
        String currentWorkingDirectory = GlobalValues.workingDir;
          JFileChooser chooser = new JFileChooser(currentWorkingDirectory);
          int ret = chooser.showOpenDialog(ODEWizardFrame);

          if (ret != JFileChooser.APPROVE_OPTION) {
                return;
              }
         File f = chooser.getSelectedFile();
         String javaFile=null;
         try {
            javaFile = f.getCanonicalPath();
            }
         catch (IOException ex) {
               System.out.println("I/O Exception in getCanonicalPath");
               ex.printStackTrace();
         }

   //   extract the path specification of the generated Java class that implements the ODE solution method,
  //    in order to update the ScalaSci class path
      String SelectedFileWithPath = f.getAbsolutePath();
      String SelectedFilePathOnly = SelectedFileWithPath.substring(0, SelectedFileWithPath.lastIndexOf(File.separatorChar));

      if (GlobalValues.ScalaSciClassPath.length()==0)
          GlobalValues.ScalaSciClassPath=".";
      if (GlobalValues.ScalaSciClassPath.indexOf(SelectedFilePathOnly)==-1)  {
        GlobalValues.ScalaSciClassPathComponents.add(0,SelectedFilePathOnly);
        GlobalValues.ScalaSciClassPath = GlobalValues.ScalaSciClassPath+File.pathSeparator+SelectedFilePathOnly;
        }
          // update also the ScalaSciClassPath property
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
    GlobalValues.settings.setProperty("ScalaSciClassPath", fileStr.toString());

      ClassLoader parentClassLoader = getClass().getClassLoader();
      GlobalValues.extensionClassLoader = new  ExtensionClassLoader(GlobalValues.ScalaSciClassPath, parentClassLoader);

      // update GUI components to account for the updated ScalaSci classpath
                scalaExec.scalaLab.scalaLab.updateTree();
                
      String [] command  = new String[11];
       String toolboxes = "";
       for (int k=0; k<GlobalValues.ScalaSciClassPathComponents.size();k++)
         toolboxes = toolboxes+File.pathSeparator+GlobalValues.ScalaSciClassPathComponents.elementAt(k);

        // compile the temporary file
       command[0] =  "java";
       command[1] = "-classpath";
       command[2] =  "."+File.pathSeparator+GlobalValues.jarFilePath+File.pathSeparator+toolboxes+File.pathSeparator+SelectedFilePathOnly;
       command[3] =  "com.sun.tools.javac.Main";    // the name of the Java  compiler class
       command[4] = "-classpath";
       command[5] =  command[2];
       command[6] = "-sourcepath";
       command[7] =  command[2];
       command[8] = "-d";   // where to place output class files
       command[9] = SelectedFilePathOnly;
       command[10] = SelectedFileWithPath;
       String compileCommandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3]+" "+command[4]+" "+command[5]+" "+command[6]+" "+command[7]+" "+command[8]+" "+command[9]+" "+command[10];


       System.out.println("compileCommand Java= "+compileCommandString);

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

                // any error???
                javaProcess.waitFor();
                int rv = javaProcess.exitValue();
                if (rv==0) {
                 System.out.println("Process:  exited successfully ");

   JavaCompile javaCompileObj = null;
                 try {
      javaCompileObj = new JavaCompile();
         }
        catch (Exception ex)  {
            JOptionPane.showMessageDialog(null,
                    "Unable to compile. Please check if your system's PATH variable includes the path to your javac compiler",
                    "Cannot compile - Check PATH", JOptionPane.INFORMATION_MESSAGE);
             ex.printStackTrace();
        }
        generateScriptCodeButton.setEnabled(true);
        statusAreaBottom.setText("Step5:  You can proceed now to create a draft ScalaSci-Script that utilizes your Java-based  ODE integrator");

              }
                else
                 System.out.println("Process:  exited with error, error value = "+rv);

                } catch (IOException exio) {
                    System.out.println("IOException trying to executing "+command);
                    exio.printStackTrace();

                }
               catch (InterruptedException ie) {
                    System.out.println("Interrupted Exception  trying to executing "+command);
                    ie.printStackTrace();
                }


            }
        });
         
         // Step 5: Generate Script Code
         generateScriptCodeButton.addActionListener( 
           new  ActionListener() {
              public void actionPerformed(ActionEvent e) {
         new scriptCodeGenerationFrame("ODE Script code", editingClassName, systemOrder);
             } 
         });
            
          
         buttonPanel.add(copyTemplateButton);  // Step 1
         buttonPanel.setEnabled(true);
         buttonPanel.add(generateEditingButton);  // Step 2
         generateEditingButton.setEnabled(false);
         buttonPanel.add(saveJavaClassButton);        // Step 3
         saveJavaClassButton.setEnabled(false);
         buttonPanel.add(compileJavaClassButton);  // Step 4
         buttonPanel.add(compileJavaInternalCompilerButton);
         compileJavaClassButton.setEnabled(false);
         compileJavaInternalCompilerButton.setEnabled(false);
         buttonPanel.add(generateScriptCodeButton); // Step 5
         generateScriptCodeButton.setEnabled(false);
         
         ODEWizardFrame.add(buttonPanel, BorderLayout.NORTH);
         ODEWizardFrame.add(editPanel, BorderLayout.CENTER);
         JPanel bottomPanel = new JPanel(new GridLayout(2,1));
         
         bottomPanel.add(paramPanel);
         bottomPanel.add(statusPanel);
         ODEWizardFrame.add(bottomPanel, BorderLayout.SOUTH);
         ODEWizardFrame.setLocation(100, 100);
         ODEWizardFrame.setSize(1400, 800);
         ODEWizardFrame.setVisible(true); 
         
           }
         
         private void updateTemplateText() {
                 switch (ODESolveMethod) {
                case   ODErke:                 
                        templateText = templateTextRKE;
                        break;
                 case ODEmultistep:
                         templateText = templateTextMultistep;
                         break;
                 case ODEdiffsys:
                         templateText = templateTextDiffsys;
                         break;
                default:
                        templateText = templateTextRKE;
                        break;
                }
         }
         
         private String prepareStatusText() {
              String statusText = "Java Class Name to generate: "+"\""+editingClassName+"\""+
               ",   ODE System Order = "+systemOrder;
              return statusText;
          }  
         
         
         class editSystemOrder  implements ActionListener  {
             public void actionPerformed(ActionEvent e)  {
                  JTextField  fieldEdited = (JTextField) e.getSource();
                  try {
                  systemOrder = Integer.parseInt(fieldEdited.getText());
                  }
                  catch (NumberFormatException nfex) {
                      System.out.println("Number format exception in getting system order");
                      nfex.printStackTrace();
                  }

                  String updatedStatusText = prepareStatusText();
                  statusAreaTop.setText(updatedStatusText);
                }
           }
         
         class editJavaClassFileName   implements ActionListener  {
             public void actionPerformed(ActionEvent e)  {
                  JTextField  fieldEdited = (JTextField) e.getSource();
                  try {
                editingClassName = fieldEdited.getText();
                  }
                  catch (NullPointerException npex) {
                      System.out.println("Null pointer exception in getting java class name");
                      npex.printStackTrace();
                  }

                  String updatedStatusText = prepareStatusText();
                  statusAreaTop.setText(updatedStatusText);
                }
           }
  
        }
     
     
     // a class that implements the automatic generation of the ScalaSci "driver" script 
     class scriptCodeGenerationFrame extends JFrame  {
                static JPanel  scriptPanel;
                
                static JTextArea scriptTextArea;
                static JScrollPane scriptScrollPane;
                static JViewport scriptViewPort;
                static String scriptText;
                static String editingClassName;
                static int systemOrder;
                static double xStart = 0.0;  static double xEnd = 40.0;  // some values
             
                JLabel xStartLabel, xEndLabel;
                JTextField  xStartField, xEndField;
                
         scriptCodeGenerationFrame(String title,  String _editingClassName, int _systemOrder) {
             super(title);
             GridBagLayout layout = new GridBagLayout();
             setLayout(layout);
             editingClassName = _editingClassName;
             systemOrder = _systemOrder;
             getParamsForScript();
         }
         
         public void getParamsForScript() {
             scriptTextArea = new JTextArea();
             scriptScrollPane = new JScrollPane();
             JPanel  scriptTextPane = new JPanel();
             scriptTextPane.add(scriptScrollPane);
             
             xStartLabel = new JLabel("Start Value");
             xStartField = new JTextField(String.valueOf(xStart));
             xStartField.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                try  {
                     xStart = Double.valueOf(xStartField.getText());
                }
                catch (NumberFormatException nfe) {
                    System.out.println("Number format exception for xStart value");
                    nfe.printStackTrace();
                   }
                 }
             });
             
             xEndLabel = new JLabel("End Value");
             xEndField = new JTextField(String.valueOf(xEnd));
             xEndField.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                try  {
                     xEnd = Double.valueOf(xEndField.getText());
                }
                catch (NumberFormatException nfe) {
                    System.out.println("Number format exception for xEnd value");
                    nfe.printStackTrace();
                   }
                 }
             });
             
             JButton proceedButton = new JButton("Proceed to script ");
             proceedButton.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     xStart = Double.valueOf(xStartField.getText());
                     xEnd = Double.valueOf(xEndField.getText());
                     scriptCodeGenerationFrame.makescalaSciCodeFromParams(ODEWizardScalaSci.ODESolveMethod);
                    };
             });
             
             JPanel startParamsPanel = new JPanel(new GridLayout(1,2));
             startParamsPanel.add(xStartLabel);   startParamsPanel.add(xStartField); 
             JPanel endParamsPanel = new JPanel(new GridLayout(1,2));
             endParamsPanel.add(xEndLabel);   endParamsPanel.add(xEndField); 
             JPanel proceedPanel = new JPanel();
             proceedPanel.add(proceedButton);
             setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
             add(startParamsPanel);  add(endParamsPanel); add(proceedPanel);
             setLocation(100, 100);
             pack();
             setVisible(true);
         
         }
         
           public static void makescalaSciCodeFromParams(int odeSolveMethod)   {
               switch  (odeSolveMethod)  {
                   case  ODEWizardScalaSci.ODErke:
                scriptText = " import scala._ \n import scalaSci._ \n import scalaSci.Vec._ \n"+
                        " import scalaSci.Mat._ \n import java.util.Vector \n"+
                        " import numal._ \n import  scalaSci.math.plot.plot._  \n";
                
                scriptText +=          " \n var  n= "+systemOrder+";  // the number of equations of the system\n"+
                    "var  x = new Array[Double](1)     // entry:   x(0) is the initial value of the independent variable  \n"+
                    "var  xe = new Array[Double](1)    //  entry:  xe(0) is the final value of the independent variable    \n"+
                    "var y = new Array[Double](n+1)   // entry: the dependent variable, the initial values at x = x0 \n"+
                    "var data = new Array[Double](7)   // in array data one should give: \n"+
                    "                                        //     data(1):   the relative tolerance \n"+
                    "                                        //     data(2):  the absolute tolerance  \n"+
                    "                                        //  after each step data(3:6) contains:  \n"+
                    "                                        //      data(3):  the steplength used for the last step \n"+
                    "                                       //      data(4):  the number of integration steps performed \n"+
                    "                                       //      data(5):  the number of integration steps rejected  \n "+
                    "                                       //      data(6):  the number of integration steps skipped \n"+
                    "                                 // if upon completion of rke data(6) > 0, then results should be considered most criticallly \n"+
                    " fi = true;                        // if fi is true then the integration starts at x0 with a trial step xe-x0;  \n"+
                    "                                        // if fi is false then the integration is continued with a step length data(3)* sign(xe-x0) \n"+
                    "data(1) = 1.0e-6;  data(2) = 1.0e-6; \n\n"+
                    "var xOut:Vector[Array[Double]] = new Vector();  \n"+
                    "var yOut:Vector[Array[Double]] = new Vector();  \n"+
    "// a Java class that implements the AP_rke_methods interface should be specified \n"+
    "// The AP_rke_methods interface requires the implementation of two procedures: \n"+
    "//    void der(int n, double t, double v[]) \n"+
    "//              this procedure performs an evaluation of the right-hand side of the system with dependent variable v[1:n] and \n"+
    "//              and independent variable t; upon completion of der the right-hand side should be overwritten on v[1:n] \n"+
    "//    void out(int n, double x[], double xe[], double y[], double data[]) \n"+
    "//              after each integration step performed, out can be used to obtain information from the solution process, \n"+
    "//              e.g., the values of x, y[1:n], and data[3:6]; out can also be used to update data, but x and xe remain unchanged \n\n";
                    
                        
         
                scriptText +=    "\n var xStart = "+xStart+";"+
                        "\n var xEnd =  "+xEnd+";   // start and end values of integration \n \n";  
                for (int k=1; k<=systemOrder; k++) 
                    scriptText += "y("+k+") = "+Math.random()+"; \n";
                
                scriptText +=  " x(0) = xStart; \n  xe(0) = xEnd; \n"; 
                scriptText += "var javaClassName  = "+"\""+ editingClassName+"\""+";   // name of the Java class that implements the ODE \n";

                scriptText +=   "\n var invocationObject = Class.forName(javaClassName, false, GlobalValues.globalInterpreter.classLoader).newInstance(); \n"+
                        "\n var lorenz2RKEObject = invocationObject.asInstanceOf[AP_rke_methods] \n";
                
                scriptText += "tic() \n";
                scriptText += "var fi = true \n";
                scriptText += "\nAnalytic_problems.rke(x, xe, n, y, lorenz2RKEObject,  data, fi,  xOut, yOut); \n"+
                                   "\n var timeCompute = toc() \n";
                
                scriptText +=  "var plotTitle = \"Lorenz attractor in ScalaSci, time =  \"+timeCompute+ \" Runge-Kutta (rke()),  integrating from \"+xStart+\", to tEnd= \"+xEnd  \n";
                scriptText += "var color = Color.RED \n";
                
                scriptText += "  figure3d(1); plotV(yOut, color, plotTitle) \n";



                    break;
                    
                   
                   case ODEWizardScalaSci.ODEmultistep:
             scriptText = " import scala._ \n import scalaSci._  \n import scalaSci.Vec._ \n import scalaSci.Mat._ \n "+
                     "import java.util.Vector \n import numal._ \n import  scalaSci.math.plot.plot._\n";

             scriptText += "\n var n= "+systemOrder+"; // the number of equations of the system \n"+
               "var first = new  Array[Boolean](1);   // if first is true then the procedure starts the integration with a first order Adams method \n"+
                            "// and a steplength equal to hmin,  upon completion of a call, first is set to false \n"+
            "first(0)=true; \n"+
            "var btmp = new Array[Boolean](2) \n"+
            "var itmp = new Array[Int](3) \n"+
            "var xtmp = new Array[Double](7) \n"+
            "var x = new Array[Double](1) \n"+
            "var y = new Array[Double](6*n+1) \n"+
            "var ymax = new Array[Double](4) \n"+
            "var save = new Array[Double](6*n+39)  //    in this array the procedure stores information which can be used in a continuing call \n"+
            "          // with first = false; also the following messages are delivered: \n"+
            "          //      save[38] == 0;  an Adams method has been used  \n"+
            "          //      save[38] == 1;  the procedure switched to Gear's method \n"+
            "          //      save[37] == 0;  no error message  \n"+
            "          //      save[37] == 1; with the hmin specified the procedure cannot handle the nonlinearity (decrease hmin!) \n"+
            "          //      save[36] ;  number of times that the requested local error bound was exceeded   \n"+
            "         //      save[35] ;  if save[36] is nonzero then save[35] gives an estimate of the maximal local error bound, otherwise save[35]=0 \n\n"+
           "\n  var   jac = Array.ofDim[Double](n+1)  \n"+
           "var k=0;     while (k<=n) {        jac(k) = new Array[Double](n+1);    k += 1; } \n"+
            "var xOut:Vector[Array[Double]] = new Vector() \n"+
            "var yOut:Vector[Array[Double]] = new Vector() \n"+
            "var hmin=1.0e-10;    var eps=1.0e-9 \n"+
            "\n y(1)=0.12; y(2)=0.3; y(3)=0.12; \n"+
            "ymax(1) = 0.00001;   ymax(2) = 0.00001;    ymax(3) = 0.00001;    var tstart = 0.0;    x(0) = tstart \n"+
            "var xendDefault =\"100.0\"  // end point of integration, default value \n"+
            "var prompt = \"Specify the end integration value\" \n"+
            "var inVal  = JOptionPane.showInputDialog(prompt, xendDefault) \n"+
            "var tend = inVal.toDouble \n";
             
             scriptText +=    "var javaClassName  = "+"\""+ editingClassName+"\""+";   // name of the Java class that implements the ODE \n";

             scriptText +=   "\n var invocationObject = Class.forName(javaClassName, false, GlobalValues.globalInterpreter.classLoader).newInstance(); \n";
             
             scriptText += "\n var multistepObject = invocationObject.asInstanceOf[AP_multistep_methods] \n";
             
             scriptText += "\ntic() \n"+
               "\nAnalytic_problems.multistep(x, tend,y,hmin,5,ymax,eps,first, save, multistepObject, jac, true,n,btmp,itmp,xtmp, xOut, yOut) \n"+
               "var  runTime =  toc() \n"+
               "var plotTitle = \"Lorenz system, method Multistep,  ntegratin from \"+tstart+\", to tEnd= \"+tend+\", runTime = \"+runTime \n";

             scriptText += "var color = Color.RED \n  figure3d(1); plotV(yOut, color, plotTitle) ";

             break;     
                   
                   case  ODEWizardScalaSci.ODEdiffsys:
                scriptText = " import scala._ \n import scalaSci._ \n import scalaSci.Vec._  \n "+
                        "import scalaSci.Mat._ \n import java.util.Vector  \n import  numal._  \n import scalaSci.math.plot.plot._  \n";
                
                scriptText += "var tol = 0.0000000000004 \n var aeta = tol; var reta = tol\n "+
                                "var n = 3; // the number of equations of the system \n"+
                                "var x = new Array[Double](1)  // entry: x(0) is the initial value of the independent variable \n"+
                                "var y = new Array[Double](n+1)   // entry: the dependent variable, the initial values at x = x0 \n"+
                                "aeta = tol  // aeta: required absolute precision in the integration process \n"+
                                "reta = tol // reta: required relative precision in the integration process \n"+
                                "var s = new Array[Double](n+1) \n"+
                                "var h0=0.000001  // h0: the initial step to be taken \n"+
                                "var xOut:Vector[Array[Double]] = new Vector() \n"+
                                "var yOut:Vector[Array[Double]] = new Vector() \n"+
                                "y(1)=0.4; y(2)= -0.3; y(3)=0.9; \n"+
                                "\n x(0)=0;  var xe = 720; \n"+
                                "\nvar javaClassName  = "+"\""+ editingClassName+"\""+";   // name of the Java class that implements the ODE \n";

                scriptText +=  "\n var invocationObject = Class.forName(javaClassName, false, GlobalValues.globalInterpreter.classLoader).newInstance(); \n"+
                    "var diffSysObject = invocationObject.asInstanceOf[AP_diffsys_methods] \n"+
                    "\n tic() \n"+
                    "Analytic_problems.diffsys(x, xe, n, y, diffSysObject, aeta, reta , s, h0, xOut, yOut) \n"+
                    "var timeCompute = toc() \n"+
                    "var plotTitle = \"Double Scroll attractor with ScalaSci, time \"+timeCompute+ \" end point = \"+xe \n"+
                    "var color = Color.RED \n"+
                    " figure3d(1); plotV(yOut, color, plotTitle); \n";


                  break;
                         
                        default:
                  scriptText = "";
               }               
                       
                scriptTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
                scriptTextArea.setRows(30);
                scriptTextArea.setText(scriptText);
                
             
                scriptPanel = new JPanel();
                scriptScrollPane = new JScrollPane(scriptTextArea);
                scriptPanel.add(scriptScrollPane);
                
                JButton scriptSaveButton = new JButton("Save Script Code");
                scriptSaveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                      String currentWorkingDirectory = GlobalValues.ScalaSciClassPath;       
                      JFileChooser chooser = new JFileChooser(currentWorkingDirectory);
                      chooser.setSelectedFile(new File(editingClassName+".gsci"));
                      int ret = chooser.showSaveDialog(ODEWizardScalaSci.scriptFrame);

                     if (ret != JFileChooser.APPROVE_OPTION) {         return;      }
                     File f = chooser.getSelectedFile();
                        try {
                            PrintWriter out = new PrintWriter(f);
                            String  scriptCodeText = scriptTextArea.getText();
                            out.write(scriptCodeText);
                            out.close();
                        }
                        catch (java.io.FileNotFoundException enf) {
                              System.out.println("File "+f.getName()+ " not found");
                              enf.printStackTrace();
                      }
                 }  
                });
                        
                JButton scriptRunButton = new JButton("Run Script Code");
                scriptRunButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                  GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(scriptText+"\n");
                    }
                }  );
                JPanel buttonsPanel = new JPanel();
                buttonsPanel.add(scriptSaveButton);
                buttonsPanel.add(scriptRunButton);        
                
          
                ODEWizardScalaSciScala.scriptFrame = new JFrame("Your script code");
                ODEWizardScalaSciScala.scriptFrame.setLayout(new BorderLayout());
                ODEWizardScalaSciScala.scriptFrame.add(scriptPanel, BorderLayout.CENTER);
                ODEWizardScalaSciScala.scriptFrame.add(buttonsPanel, BorderLayout.SOUTH);
                ODEWizardScalaSciScala.scriptFrame.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                ODEWizardScalaSciScala.scriptFrame.setVisible(true);
           }      
           
           
           
          
     } 
                 
          