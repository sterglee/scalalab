
package scalaExec.scalaLab;

import java.io.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import scalaExec.Interpreter.GlobalValues;

import java.io.IOException;
import scalaExec.scalaLab.FileTreeExplorer.FileTreeNode;

public class FileTreeKeyListener extends KeyAdapter {
                @Override
            public void keyPressed(KeyEvent e) {
            int keyValue = e.getKeyCode();

        switch (keyValue) {
            case KeyEvent.VK_F1:
                 EditorPaneHTMLHelp  eigHelpPane = new EditorPaneHTMLHelp("ExplorerKeys.html");
               if (GlobalValues.useSystemBrowserForHelp==false) {
                eigHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                 eigHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
                 eigHelpPane.setVisible(true);
               }   
                break;

                // create a new file at the current node
            case  KeyEvent.VK_INSERT:
                e.consume();
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;  //  selected node not exists

                FileTreeNode parent = (FileTreeNode)  selectedNode.getParent();

                String newFileName = JOptionPane.showInputDialog(null, "Name for your new file?", JOptionPane.QUESTION_MESSAGE);

                // create the specified file actually at the filesystem
                String filePath = scalaLabPathsListener.selectedPath;  // the path of the selected node
                String newFileFullPathName = filePath+File.separator+newFileName;
                File newFile = new File(newFileFullPathName);
                boolean OKforNewFile = true;
                int userResponse = JOptionPane.YES_OPTION;   // allows further processing if file either not exists or user responds to overwrite
                if (newFile.exists())   {  // file already exists
                    userResponse = JOptionPane.showConfirmDialog(null, "File: "+newFileFullPathName+" already exists. Overwrite? ", "File already exists",
                            JOptionPane.YES_NO_OPTION);
                    OKforNewFile = (userResponse == JOptionPane.YES_OPTION);
                    if (OKforNewFile) {
                      boolean deleteSuccess = newFile.delete();
                      if (deleteSuccess == false)
                          JOptionPane.showMessageDialog(null, "Failing to delete file: "+newFileFullPathName, "File delete failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                if (OKforNewFile)   {  // create new file and update tree
                    // create new file
                     try {
                    newFile.createNewFile();
                     }
                     catch (IOException ioEx)  {
    JOptionPane.showMessageDialog(null, "IOException trying to create file"+newFileFullPathName, "IO Exception", JOptionPane.INFORMATION_MESSAGE );
                     }

                      // update tree
                    int selectedIndex = 0;
                    if (parent != null) {
                selectedIndex = parent.getIndex(selectedNode);
                    }

                    // update the tree model
                    FileTreeNode  newNode = null;
                    try {
                      newNode = new FileTreeNode(new File(filePath),  newFileName);
                      newNode.setUserObject(filePath+File.separator+newFileName);
                    // now display the new node
                    GlobalValues.currentFileExplorer.model.insertNodeInto(newNode, parent, selectedIndex+1);
                    TreeNode [] nodes = GlobalValues.currentFileExplorer.model.getPathToRoot(newNode);
                    TreePath pathScroll = new TreePath(nodes);
                    GlobalValues.currentFileExplorer.pathsTree.expandPath(pathScroll);
                    }
                    catch (FileNotFoundException ex)  { System.out.println("File not  found exception in creating new File"); ex.printStackTrace();}
                    catch (SecurityException ex)  { System.out.println("Security exception in creating new File"); ex.printStackTrace();}

                }
      break;

      case KeyEvent.VK_UP:    //   Up Folder
                e.consume();
                selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;

                String selectedPath = selectedNode.toString();
                int idxLastPath = selectedPath.lastIndexOf(File.separatorChar);

                if (idxLastPath==-1) {
                    JOptionPane.showMessageDialog(null, "Already in root level", "There is no parent folder", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String pathComponent = selectedPath.substring(0, idxLastPath);

                 GlobalValues.selectedExplorerPath = pathComponent;
                 GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();
                 break;

        case  KeyEvent.VK_F2:   //  New File within the directory
            System.out.println("ALT-INSERT");
            e.consume();
                selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;

                parent = (FileTreeNode)  selectedNode.getParent();

                newFileName = JOptionPane.showInputDialog(null, "Name for your new file?", JOptionPane.QUESTION_MESSAGE);

                // create the specified file actually at the filesystem
                filePath = scalaLabPathsListener.selectedValue;  // the path of the selected node
                File directoryOfNewFile = new File(filePath);
                if (directoryOfNewFile.isDirectory()==false) {
                    JOptionPane.showMessageDialog(null, "Cannot place a file within another file!!", "Improper attempt to create a file within a directory", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                newFileFullPathName = directoryOfNewFile+File.separator+newFileName;
                newFile = new File(newFileFullPathName);
                OKforNewFile = true;
                userResponse = JOptionPane.YES_OPTION;   // allows further processing if file either not exists or user responds to overwrite
                if (newFile.exists())   {  // file already exists
                    userResponse = JOptionPane.showConfirmDialog(null, "File: "+newFileFullPathName+" already exists. Overwrite? ", "File already exists",
                            JOptionPane.YES_NO_OPTION);
                    OKforNewFile = (userResponse == JOptionPane.YES_OPTION);
                    if (OKforNewFile) {
                      boolean deleteSuccess = newFile.delete();
                      if (deleteSuccess == false)
                          JOptionPane.showMessageDialog(null, "Failing to delete file: "+newFileFullPathName, "File delete failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                if (OKforNewFile)   {  // create new file and update tree
                    // create new file
                     try {
                    newFile.createNewFile();
                     }
                     catch (IOException ioEx)  {
    JOptionPane.showMessageDialog(null, "IOException trying to create file"+newFileFullPathName, "IO Exception", JOptionPane.INFORMATION_MESSAGE );
                     }

                      // update tree
                    int selectedIndex = 0;
                    if (parent != null)
                        selectedIndex = parent.getIndex(selectedNode);

                    // update the tree model
                    FileTreeNode  newNode = null;
                    try {
                      newNode = new FileTreeNode(new File(filePath),  newFileName);
                      newNode.setUserObject(filePath+File.separator+newFileName);  // sets the user object for this node
                    // now display the new node
                    DefaultTreeModel model = GlobalValues.currentFileExplorer.model;
                    model.insertNodeInto(newNode, selectedNode, 0);
                    TreeNode [] nodes = model.getPathToRoot(newNode);
                    TreePath pathScroll = new TreePath(nodes);
                    GlobalValues.currentFileExplorer.pathsTree.expandPath(pathScroll);
                    }
                    catch (FileNotFoundException ex)  { System.out.println("File not  found exception in creating new File"); ex.printStackTrace();}
                    catch (SecurityException ex)  { System.out.println("Security exception in creating new File"); ex.printStackTrace();}


                }
               
            break;


            case KeyEvent.VK_F6:  // compile Scala file
                e.consume();
                String selectedValue = scalaLabPathsListener.selectedValue;
                pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
       
                GlobalValues.selectedExplorerPath = pathComponent; 
                 
                if (selectedValue.indexOf(".scala")!=-1)  {   // file is a Scala source file
                 
                 String [] command  = new String[9];
                 int idx = GlobalValues.fullJarFilePath.lastIndexOf(File.separatorChar);
                 if (idx == -1) idx = GlobalValues.fullJarFilePath.lastIndexOf('/');  // try Java standard File seperator
                 
                 command[0] =  "java";
                 command[1] = "-cp";
                 command[2] =   "."+File.pathSeparator+GlobalValues.scalalabLibPath+File.pathSeparator+
                          scalalab.JavaGlobals.libFile+ File.pathSeparator+
                         scalalab.JavaGlobals.reflectFile+File.pathSeparator+
                        scalalab.JavaGlobals.ejmlFile+File.pathSeparator+
                         scalalab.JavaGlobals.jblasFile+File.pathSeparator+
                         scalalab.JavaGlobals.jsciFile+File.pathSeparator+
                         scalalab.JavaGlobals.jsciFile+File.pathSeparator+
                         scalalab.JavaGlobals.javacppFile+File.pathSeparator+
                         scalalab.JavaGlobals.mtjColtSGTFile+File.pathSeparator+
                         scalalab.JavaGlobals.ApacheCommonsFile+File.pathSeparator+
                         scalalab.JavaGlobals.jfreechartFile+File.pathSeparator+
                         scalalab.JavaGlobals.numalFile+File.pathSeparator+
                         scalalab.JavaGlobals.LAPACKFile+File.pathSeparator+
                         scalalab.JavaGlobals.ARPACKFile+File.pathSeparator+
                         scalalab.JavaGlobals.JASFile+ File.pathSeparator+
                         GlobalValues.jarFilePath;
                 command[3] =  "scala.tools.nsc.Main";    // the name of the Scala compiler class
                 command[4] = "-classpath";
                 command[5] = "."+File.pathSeparator+GlobalValues.scalalabLibPath+File.pathSeparator+
                         scalalab.JavaGlobals.libFile+File.pathSeparator+
                         scalalab.JavaGlobals.reflectFile+File.pathSeparator+
                         scalalab.JavaGlobals.ejmlFile+File.pathSeparator+
                         scalalab.JavaGlobals.jblasFile+File.pathSeparator+
                         scalalab.JavaGlobals.jsciFile+File.pathSeparator+
                         scalalab.JavaGlobals.jsciFile+File.pathSeparator+
                         scalalab.JavaGlobals.javacppFile+File.pathSeparator+
                         scalalab.JavaGlobals.mtjColtSGTFile+File.pathSeparator+
                         scalalab.JavaGlobals.ApacheCommonsFile+File.pathSeparator+
                         scalalab.JavaGlobals.jfreechartFile+File.pathSeparator+
                         scalalab.JavaGlobals.numalFile+File.pathSeparator+
                         File.pathSeparator+scalalab.JavaGlobals.LAPACKFile+
                         File.pathSeparator+scalalab.JavaGlobals.ARPACKFile+
                         File.pathSeparator+scalalab.JavaGlobals.JASFile+   
              File.pathSeparator+GlobalValues.jarFilePath;
                 command[6] =  selectedValue;
                 command[7] = "-d";  // option to specify the directory where to output the compiled .class
                 command[8] = pathComponent;
                 
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
                String commandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3]+" "+command[4]+" "+command[5]+" "+command[6];
                if (rv==0)
                 System.out.println("Process:  "+commandString+"  exited successfully ");
                else
                 System.out.println("Process:  "+commandString+"  exited with error, error value = "+rv);
 
                GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();
 
                } catch (IOException ex) {
                    System.out.println("IOException trying to executing "+command);
                    ex.printStackTrace();
                            
                }
               catch (InterruptedException ie) {
                    System.out.println("Interrupted Exception  trying to executing "+command);
                    ie.printStackTrace();
                            
                }
                
            }
           
           break;

           case KeyEvent.VK_F7:   // run a class file
                e.consume();
                selectedValue = scalaLabPathsListener.selectedValue;
             if (selectedValue.indexOf(".class")!=-1)  {   // not a compiled .class file

                 pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                 String fileNameComponent = selectedValue.substring( selectedValue.lastIndexOf(File.separatorChar)+1, selectedValue.length());
                 String classFile = fileNameComponent.substring(0,  fileNameComponent.indexOf('.'));

                 GlobalValues.selectedExplorerPath = pathComponent;

                 String [] command  = new String[4];

                 int idx = GlobalValues.fullJarFilePath.lastIndexOf(File.separatorChar);
                 if (idx == -1) idx = GlobalValues.fullJarFilePath.lastIndexOf('/');  // try Java standard File seperator

                 selectedValue = selectedValue.substring(0, selectedValue.indexOf(".class"));
                 command[0] =  "java";
                 command[1] = "-cp";
                 command[2] =  pathComponent+File.pathSeparator+"."+ File.pathSeparator+ GlobalValues.jarFilePath;
                 command[3] =  classFile;    // the name of the Scala compiler class

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
                if (rv==0)
                 System.out.println("Process:  "+commandString+"  exited successfully ");
                else
                 System.out.println("Process:  "+commandString+"  exited with error, error value = "+rv);

                GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();

                } catch (IOException ex) {
                    System.out.println("IOException trying to executing "+command);
                    ex.printStackTrace();

                }
                catch (InterruptedException ie) {
                   System.out.println("Interrupted Exception  trying to executing "+command);
                   ie.printStackTrace();
                  }
            
           }
        break;

           
            case KeyEvent.VK_F4:   // compile .java file with javac
                e.consume();
                selectedValue = scalaLabPathsListener.selectedValue;
                pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                GlobalValues.selectedExplorerPath = pathComponent;

                String  javaFile = selectedValue.substring(selectedValue.lastIndexOf(File.separatorChar)+1, selectedValue.length());

                 String commandc =  "javac "+ selectedValue +  // the Java file to be compiled
                         " -d " +  // option to specify the directory where to output the compiled .class
                 pathComponent;

                try {

                Runtime rt = Runtime.getRuntime();
                Process javaProcess = rt.exec(commandc);
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
                if (rv==0)
                 System.out.println("Process:  "+commandc+"  exited successfully ");
                else
                 System.out.println("Process:  "+commandc+"  exited with error, error value = "+rv);

                GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();

                } catch (IOException ex) {
                    System.out.println("IOException trying to executing "+commandc);
                    ex.printStackTrace();

                }
                catch (InterruptedException ie) {
                    System.out.println("Interrupted Exception  trying to executing "+commandc);
                    ie.printStackTrace();
              }
              break;
                
            case KeyEvent.VK_ENTER: // edit
                e.consume();
                selectedValue =  scalaLabPathsListener.selectedValue;

               int response = JOptionPane.showConfirmDialog(null, "Edit file "+selectedValue+ " ?", "Edit Confirmation Dialog", JOptionPane.YES_NO_OPTION);
               if (response == JOptionPane.YES_OPTION)  {
                      GlobalValues.editingFileInPane = selectedValue;  // keep name of selected file
               
                FileInputStream input;
                try {
           input = new FileInputStream(selectedValue);
                
            //create reader stream
           BufferedReader  fileReader= new BufferedReader(new InputStreamReader(input));

          StringBuilder sb = new StringBuilder();
          String  line;
          while ((line = fileReader.readLine())!= null) 
              sb.append(line+"\n");
           
          scalaExec.Interpreter.GlobalValues.editorPane.setText(sb.toString());
               
           } catch (Exception ex) {
                    System.out.println("Exception in file loading: "+ex.getMessage());
                    ex.printStackTrace();
                }
          }
          break;

              
            case KeyEvent.VK_DELETE:  // delete the file

                DefaultMutableTreeNode [] allSelectedNodes = scalaLabMultiplePathsListener.selectedNodes;
                if (allSelectedNodes == null)   return;
                String allFileNames = "";
                for (DefaultMutableTreeNode currentNode: allSelectedNodes)
                    allFileNames = allFileNames+currentNode.toString()+"  ";

                boolean OKforDeleteFile = true;
                userResponse = JOptionPane.YES_OPTION;   // confirm the user for file deletetion
                userResponse = JOptionPane.showConfirmDialog(null, "Delete Files: "+allFileNames+" ? ", "Confirm delete",
                            JOptionPane.YES_NO_OPTION);
                    OKforDeleteFile = (userResponse == JOptionPane.YES_OPTION);
                    if (OKforDeleteFile) {

                 selectedNode=null;
                int nodeCnt=0; // count of selected node currently processed
                for (DefaultMutableTreeNode currentNode: allSelectedNodes) {
                  selectedNode = (FileTreeNode) currentNode;
                 parent = (FileTreeNode)  selectedNode.getParent();
                if (parent == null) {
                    JOptionPane.showMessageDialog(null, "Files cannot be deleted at the root level", "Cannot delete top level root directories", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                // delete the specified file actually at the filesystem
                String fileToDelete= scalaLabMultiplePathsListener.selectedValues[nodeCnt];  // the path of the selected node
                newFile = new File(fileToDelete);
          
                if (newFile.exists())   {  // file object exists
                      boolean deleteSuccess = newFile.delete();
                      if (deleteSuccess == false)
                          JOptionPane.showMessageDialog(null, "Failing to delete file: "+fileToDelete, "File delete failed", JOptionPane.INFORMATION_MESSAGE);

                }

                    GlobalValues.currentFileExplorer.model.removeNodeFromParent(selectedNode);
                    nodeCnt++;

                }   // for all selected nodes
               }

        break;
                



            default: e.consume();  break;

        }
           


            }

            @Override
            public void keyReleased(KeyEvent e) {
            
        }
}
        



