
package scalaExec.scalaLab;

        
        
import java.io.*; 
import java.util.*; 
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*; 
import javax.swing.tree.*; 
import javax.swing.event.*;
import java.lang.reflect.*;
import scalaExec.Wizards.JavaCompile;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.ClassLoaders.ExtensionClassLoader;

import java.io.IOException;
import java.nio.CharBuffer;
import scalaExec.gui.scalalabConsole;

// the FileTreeExplorer class implements the functionality of an integrated in the ScalaLab environment file explorer
// specialized to support effectively operations related to ScalaLab. e.g. compile and run Scala and Java files
public class FileTreeExplorer  {
     public  JTree  pathsTree;    // the tree that represents the file system
     public  DefaultTreeModel  model;
     static   JPopupMenu  pathsPopupMenu;  // the popup menu that handles operations with right mouse clicks
// used for invoking the main method of a Java class
     static Class [] formals = { String [].class };
     static Object [] actuals = { new String [] {""}};
     static public Font defaultFont=  scalaExec.Interpreter.GlobalValues.guifont; // new Font("Times New Roman", Font.PLAIN, 12);
// some fonts for better rendering of special types of files
     static public Font javaFilesFont = new Font("Arial", Font.BOLD+Font.ITALIC, Integer.parseInt(GlobalValues.guiFontSize));
     static public Font scalaFilesFont =  new Font("Arial", Font.BOLD, Integer.parseInt(GlobalValues.guiFontSize));
     static public Font classFilesFont = new Font("Times New Roman", Font.ITALIC, Integer.parseInt(GlobalValues.guiFontSize));
     static public Font directoryFont = new Font(Font.SANS_SERIF, Font.PLAIN, Integer.parseInt(GlobalValues.guiFontSize));

     // path: a string that specifies the path of the root of the file system to explore
    public FileTreeExplorer(String path) 
      throws FileNotFoundException, SecurityException {

        // Create the first node 
       FileTreeNode  rootNode = null;
try  {
       rootNode = new FileTreeNode(null, path); 
}
catch (FileNotFoundException ex)  {
    System.out.println("File not found exception to construct root with specified path: "+path);
    ex.printStackTrace();
}
  // Populate the root node with its subdirectories
  boolean addedNodes = rootNode.populateDirectories(true);
  model = new DefaultTreeModel(rootNode);   // a simple tree data model that uses TreeNodes
  
  pathsTree = new JTree(model);  // JTree displays the set of hierarchical data of model

  pathsTree.setFont(GlobalValues.guifont);

  explorerTreeCellRenderer renderer = new explorerTreeCellRenderer();

  pathsTree.setCellRenderer(renderer);
  // Use horizontal and vertical lines
  pathsTree.putClientProperty("JTree.lineStyle", "Angled"); 
  
  pathsPopupMenu = new JPopupMenu();
  pathsPopupMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
  pathsTree.setToolTipText("Configuring paths and editing/running Scala and Java Files. F1 for more help " +
            "Use  right mouse click on a selected file to display the popup menu with the relevant operations");
          
    // single selection
    pathsTree.addMouseListener(new MouseListener() {
            @Override
        public void mouseClicked(MouseEvent e) {    }
            @Override
        public void mouseEntered(MouseEvent e) {        }
            @Override
        public void mouseExited(MouseEvent e) {        }
        public void mousePressed(MouseEvent e) {   
            if (e.isPopupTrigger()){  // i.e. right mouse-click
               pathsPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
             }
           }
            
        public void mouseReleased(MouseEvent e) { 
           if (e.isPopupTrigger()){
               pathsPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
             }       
      }
    } );
    
 // add the keyboard listener for the File Tree Explorer
    pathsTree.addKeyListener(new FileTreeKeyListener());
 
  // Listen for Tree Selection Events
  pathsTree.addTreeExpansionListener(new TreeExpansionHandler()); 
    // add a paths listener for this tree
   scalaLabPathsListener  pathsListener = new scalaLabPathsListener(pathsTree);  
   pathsTree.addTreeSelectionListener(pathsListener);
   pathsTree.addTreeSelectionListener(new scalaLabMultiplePathsListener(pathsTree));
   
           JMenuItem newFileItem = new JMenuItem("New File at the current top level");
           newFileItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           newFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;
                
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
                    model.insertNodeInto(newNode, parent, selectedIndex+1);
                    TreeNode [] nodes = model.getPathToRoot(newNode);
                    TreePath pathScroll = new TreePath(nodes);
                    pathsTree.expandPath(pathScroll);
                    }
                    catch (FileNotFoundException ex)  { System.out.println("File not  found exception in creating new File"); ex.printStackTrace();}
                    catch (SecurityException ex)  { System.out.println("Security exception in creating new File"); ex.printStackTrace();}
                    
                    
                }
               }
           });
       
             JMenuItem browseUpItem = new JMenuItem("Up Folder");
             browseUpItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
             browseUpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
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
                
           }
           });
       
          
             
             JMenuItem newFileInDirItem = new JMenuItem("New File within the directory");
             newFileInDirItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           newFileInDirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;
                
                FileTreeNode parent = (FileTreeNode)  selectedNode.getParent();
                
                String newFileName = JOptionPane.showInputDialog(null, "Name for your new file?", JOptionPane.QUESTION_MESSAGE);
                
                // create the specified file actually at the filesystem
                String filePath = scalaLabPathsListener.selectedValue;  // the path of the selected node
                File directoryOfNewFile = new File(filePath);
                if (directoryOfNewFile.isDirectory()==false) {
                    JOptionPane.showMessageDialog(null, "Cannot place a file within another file!!", "Improper attempt to create a file within a directory", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String newFileFullPathName = directoryOfNewFile+File.separator+newFileName;
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
                    if (parent != null)
                        selectedIndex = parent.getIndex(selectedNode);
                
                    // update the tree model
                    FileTreeNode  newNode = null;
                    try {
                      newNode = new FileTreeNode(new File(filePath),  newFileName);
                      newNode.setUserObject(filePath+File.separator+newFileName);  // sets the user object for this node
                    // now display the new node
                    model.insertNodeInto(newNode, selectedNode, 0);
                    TreeNode [] nodes = model.getPathToRoot(newNode);
                    TreePath pathScroll = new TreePath(nodes);
                    pathsTree.expandPath(pathScroll);
                    }
                    catch (FileNotFoundException ex)  { System.out.println("File not  found exception in creating new File"); ex.printStackTrace();}
                    catch (SecurityException ex)  { System.out.println("Security exception in creating new File"); ex.printStackTrace();}
                    
                    
                }
               }
              
         
           });
                       JMenuItem newDirectoryItem = new JMenuItem("New Directory at the current directory");
                       newDirectoryItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           newDirectoryItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;
                
                FileTreeNode parent = (FileTreeNode)  selectedNode.getParent();
                if (parent == null) {
                    JOptionPane.showMessageDialog(null, "Files cannot be created at the root level", "Please select inner nodes", JOptionPane.INFORMATION_MESSAGE);  
                    return;
                }
                
                String newDirectoryName = JOptionPane.showInputDialog(null, "Name for your new directory?", JOptionPane.QUESTION_MESSAGE);
                
                // create the specified directory actually at the filesystem
                String directoryPath = scalaLabPathsListener.selectedPath;  // the path of the selected node
                String newDirectoryFullPathName = directoryPath+File.separator+newDirectoryName;
                File newDirectory = new File(newDirectoryFullPathName);
                File pathOfDirectory  = new File(directoryPath);
            
                int userResponse = JOptionPane.YES_OPTION;  
                boolean  dirExists = newDirectory.exists();
                if (dirExists)     // directory already exists
                     JOptionPane.showMessageDialog(null, "Directory: "+newDirectoryFullPathName+" already exists");
                    
                if (dirExists==false)   {  // create new directory and update tree
                    // create new directory
                     try {
                newDirectory.mkdir();  // creates the corresponding directory
                     }
                     catch (SecurityException ioEx)  { 
    JOptionPane.showMessageDialog(null, "IOException trying to create file"+newDirectoryFullPathName, "IO Exception", JOptionPane.INFORMATION_MESSAGE );
                     }
                  
                     // update tree model to correspond to the altered filesystem structure 
                    int selectedIndex = parent.getIndex(selectedNode);
                
                    // update the tree model
                    try {
                    FileTreeNode  newNode = new FileTreeNode(pathOfDirectory, newDirectoryName);
                    
                    model.insertNodeInto(newNode, parent, selectedIndex+1);
                    // now display the new node
                    TreeNode [] nodes = model.getPathToRoot(newNode);
                    TreePath path = new TreePath(nodes);
                    pathsTree.scrollPathToVisible(path);
                  }
                    catch (SecurityException ex) { System.out.println("Security exception in creating pathOfDirectory"); ex.printStackTrace(); }
                    catch (FileNotFoundException ex) { System.out.println("FileNotFoundException exception in creating pathOfDirectory"); ex.printStackTrace(); }
                    
                    }
               }
           });
           

           JMenuItem  renameFileJItem = new JMenuItem("Rename");
           renameFileJItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           renameFileJItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileTreeNode selectedNode = (FileTreeNode) scalaLabPathsListener.selectedNode;
                if (selectedNode == null)   return;
                
                FileTreeNode parent = (FileTreeNode)  selectedNode.getParent();
                if (parent == null) {
                    JOptionPane.showMessageDialog(null, "Files cannot be created at the root level", "Please select inner nodes", JOptionPane.INFORMATION_MESSAGE);  
                    return;
                }
                String filePath = scalaLabPathsListener.selectedValue;  // the path of the selected node
                int fileNameStartIdx = filePath.lastIndexOf(File.separatorChar)+1;
                String fileNameComponent = filePath.substring(fileNameStartIdx, filePath.length());
                String pathNameComponent = filePath.substring(0, fileNameStartIdx);
                String newFileName = JOptionPane.showInputDialog("Specify new file", fileNameComponent);  
                String newFullFileName = pathNameComponent + newFileName;
                File selectedFileToRename = new File(filePath);
                File renamedFile = new File(newFullFileName);
                selectedFileToRename.renameTo(renamedFile);   // rename the file at the filesystem
                
                // update tree
                int selectedIndex = parent.getIndex(selectedNode);  // index of the selected child in this node's child array
                model.removeNodeFromParent(selectedNode);
                FileTreeNode newNode = null;
                try {
                    File parentRenamedFile = new File(pathNameComponent);
                    newNode = new FileTreeNode(parentRenamedFile, newFileName);
                    newNode.setUserObject(newFullFileName);  // update the tree model
                    model.insertNodeInto(newNode, parent, parent.getChildCount());                    
                    // now display the new node
                    TreeNode [] nodes = model.getPathToRoot(newNode);
                    TreePath pathScroll = new TreePath(nodes);
                    pathsTree.expandPath(pathScroll);
            }
                    catch (SecurityException ex) { System.out.println("Security exception in creating pathOfDirectory"); ex.printStackTrace(); }
                    catch (FileNotFoundException ex) { System.out.println("FileNotFoundException exception in creating pathOfDirectory"); ex.printStackTrace(); }
                    }
           });
                
                        
           JMenuItem specifyAsJarToolbox = new JMenuItem("Specify as .jar toolbox without adding to the classpath of the current Interpreter");
           specifyAsJarToolbox.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           specifyAsJarToolbox.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {
                String selectedValue =  scalaLabPathsListener.selectedValue;
                        
               if ( (selectedValue.indexOf(".jar")!=-1)) {
                   if (GlobalValues.ScalaSciToolboxes.contains(selectedValue)== false)
                        GlobalValues.ScalaSciToolboxes.add(selectedValue);                   
                   if (GlobalValues.ScalaSciClassPathComponents.contains(selectedValue) == false)
                       GlobalValues.ScalaSciClassPathComponents.add(selectedValue);
               }
            }
           });
           
           JMenuItem deleteFileItem = new JMenuItem("Delete File");
           deleteFileItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           deleteFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode [] allSelectedNodes = scalaLabMultiplePathsListener.selectedNodes;
                if (allSelectedNodes == null)   return;
                String allFileNames = "";
                for (DefaultMutableTreeNode currentNode: allSelectedNodes)
                    allFileNames = allFileNames+currentNode.toString()+"  ";
                
                boolean OKforDeleteFile = true;
                int userResponse = JOptionPane.YES_OPTION;   // confirm the user for file deletetion
                userResponse = JOptionPane.showConfirmDialog(null, "Delete Files: "+allFileNames+" ? ", "Confirm delete",
                            JOptionPane.YES_NO_OPTION);
                    OKforDeleteFile = (userResponse == JOptionPane.YES_OPTION);
                    if (OKforDeleteFile) {

                FileTreeNode selectedNode=null;
                int nodeCnt=0; // count of selected node currently processed
                for (DefaultMutableTreeNode currentNode: allSelectedNodes) {
                  selectedNode = (FileTreeNode) currentNode;
                FileTreeNode parent = (FileTreeNode)  selectedNode.getParent();
                if (parent == null) {
                    JOptionPane.showMessageDialog(null, "Files cannot be deleted at the root level", "Cannot delete top level root directories", JOptionPane.INFORMATION_MESSAGE);  
                    return;
                }
                // delete the specified file actually at the filesystem
                String fileToDelete= scalaLabMultiplePathsListener.selectedValues[nodeCnt];  // the path of the selected node
                File newFile = new File(fileToDelete);
                
                if (newFile.exists())   {  // file object exists
                      boolean deleteSuccess = newFile.delete();
                      if (deleteSuccess == false)
                          JOptionPane.showMessageDialog(null, "Failing to delete file: "+fileToDelete, "File delete failed", JOptionPane.INFORMATION_MESSAGE);
                    
                }
                
                    model.removeNodeFromParent(selectedNode);
                    nodeCnt++;
                
                }   // for all selected nodes
               }
            } 
           });
           
           
        
              
           JMenuItem editInNewItem = new JMenuItem("Simple ScalaLab Editor");
           editInNewItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           editInNewItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue =  scalaLabPathsListener.selectedValue;
                new scalalabEdit.EditorPaneEdit(selectedValue);
            }
           });
                
           JMenuItem jeditItem = new JMenuItem("Edit with jEdit editor");
           jeditItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           jeditItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue =  scalaLabPathsListener.selectedValue;
                        
              String [] command  = new String[4];
               command[0] =  "java";
               command[1] = "-jar";
               String jeditPath = GlobalValues.jarFilePath+ File.separator+"lib"+File.separator+"4.3.2"+File.separator+"jedit.jar";
               String scalalabStr = "scalalabPr"+File.separator+"dist"+File.separator+"Scalalab291.jar";
               if (jeditPath.contains(scalalabStr))   // starting from Netbeans
                  jeditPath = jeditPath.replace(scalalabStr,"");
               else
                   jeditPath = jeditPath.replace("Scalalab291.jar", "");
               
               if (jeditPath.startsWith(File.separator+"lib"))  // for UNIX 
                   jeditPath = "."+jeditPath;
            command[2] =   jeditPath;
            command[3] = selectedValue;
                    
            String jEditCommandString = command[0]+"  "+command[1]+"  "+command[2]+command[3];
            System.out.println("jEditCommandString = "+jEditCommandString); 
             
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
                });
                
                
        JMenuItem favouritesPathsAddItem = new JMenuItem("Append the path to the favourites Paths");
        favouritesPathsAddItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
        favouritesPathsAddItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            
                // create the specified file actually at the filesystem
                String filePath = scalaLabPathsListener.selectedValue;  // the path of the selected node
                File directoryOfNewFile = new File(filePath);
                if (directoryOfNewFile.isDirectory()==false) {
                    JOptionPane.showMessageDialog(null, "Please select for paths only directories!!", "Improper attempt to select a file as a directory",  JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
     
                String directoryName = directoryOfNewFile.getAbsolutePath();
                if (GlobalValues.favouriteElements.contains(directoryName) == false) {
                        GlobalValues.favouriteElements.add(directoryName);
                        scalaLabExplorer.favouritePathsCB.addItem(directoryName);
        }
            }});
                
                
                JMenuItem addPathToscalaSciPathsItem = new JMenuItem("Append path to the classpath of the current Scala Interpreter, requires restarting new interpreter");
                addPathToscalaSciPathsItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
                addPathToscalaSciPathsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            
                // create the specified file actually at the filesystem
                String filePath = scalaLabPathsListener.selectedValue.trim();  // the path of the selected node
                File directoryOfNewFile = new File(filePath);
                if (directoryOfNewFile.isDirectory()==false) {
                    JOptionPane.showMessageDialog(null, "Please select for paths only directories!!", "Improper attempt to select a file as a directory",  JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
     
                // append the specified path to scalaSci paths if not already exists
                if (GlobalValues.ScalaSciClassPathComponents.contains(filePath) == false)
                   GlobalValues.ScalaSciClassPathComponents.add(filePath);
        
                if (GlobalValues.ScalaSciUserPaths.contains(filePath) == false)
                    GlobalValues.ScalaSciUserPaths.add(filePath);
                
                scalaSciCommands.BasicCommands.appendClasspath(filePath);
               
                StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce.toString().trim());
    }
      GlobalValues.ScalaSciClassPath  = fileStr.toString();
  
      scalaExec.scalaLab.scalaLab.updateTree();
      
            }
        });

                   
        JMenuItem removePathFromScalaSciPathsItem = new JMenuItem("Remove the path from the scalaSci Paths");
        removePathFromScalaSciPathsItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
        removePathFromScalaSciPathsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String filePath = scalaLabPathsListener.selectedValue.trim();  // the path of the selected node
                File directoryOfNewFile = new File(filePath);
                if (directoryOfNewFile.isDirectory()==false) {
                    JOptionPane.showMessageDialog(null, "Please select for paths only directories!!", "Improper attempt to select a file as a directory",  JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // remove the specified path from the scalaSci paths if already exists
                if (GlobalValues.ScalaSciClassPathComponents.contains(filePath) == true)
                   GlobalValues.ScalaSciClassPathComponents.remove(filePath);

                if (GlobalValues.ScalaSciUserPaths.contains(filePath) == true)
                    GlobalValues.ScalaSciUserPaths.remove(filePath);

                // scalaSciCommands.BasicCommands.scalaInterpreterWithClassPathComponents();   // create an interpreter with the reduced classpath

                StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
      GlobalValues.ScalaSciClassPath  = fileStr.toString();

      scalaExec.scalaLab.scalaLab.updateTree();

            }
        });


        JMenuItem compileJavaWithJavaCMenuItem = new JMenuItem("Compile .java with javac (F4) ");
        compileJavaWithJavaCMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
        compileJavaWithJavaCMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue = scalaLabPathsListener.selectedValue;
                String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                GlobalValues.selectedExplorerPath = pathComponent; 
                
                String javaFile = selectedValue.substring(selectedValue.lastIndexOf(File.separatorChar)+1, selectedValue.length());
               
                String  command = "javac "+ selectedValue +  // the Java file to be compiled
                         " -d " +  // option to specify the directory where to output the compiled .class
                 pathComponent;
                 
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
                if (rv==0)
                 System.out.println("Process:  "+command+"  exited successfully ");
                else
                 System.out.println("Process:  "+command+"  exited with error, error value = "+rv);
 
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
           });
      
        JMenuItem runJavaWithJavaMenuItem = new JMenuItem("Run .class file with java");
        runJavaWithJavaMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
               
               runJavaWithJavaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String selectedValue = scalaLabPathsListener.selectedValue;
             if (selectedValue.indexOf(".class")!=-1)  {   // not a compiled .class file
         
                 String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                 String fileNameComponent = selectedValue.substring( selectedValue.lastIndexOf(File.separatorChar)+1, selectedValue.length());
                 String classFile = fileNameComponent.substring(0,  fileNameComponent.indexOf('.'));
               
                 GlobalValues.selectedExplorerPath = pathComponent; 
                 
               
                 String [] command  = new String[4];
             
                 command[0] =  "java";
                 command[1] = "-cp";
                 command[2] = pathComponent+"."+File.pathSeparator+GlobalValues.scalalabLibPath+
                         File.pathSeparator+GlobalValues.jarFilePath;
                 command[3] =  classFile;    
                 
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
                String commandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3];
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
           }
           });
     
             JMenuItem compileScalaCMenuItem = new JMenuItem("Compile .scala file  (F6)  ");
             compileScalaCMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           compileScalaCMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String selectedValue = scalaLabPathsListener.selectedValue;
                String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
       
                GlobalValues.selectedExplorerPath = pathComponent; 
                 
                if (selectedValue.indexOf(".scala")!=-1)  {   // file is a Scala source file
                 
                 String [] command  = new String[9];
                 int idx = GlobalValues.fullJarFilePath.lastIndexOf(File.separatorChar);
                 if (idx == -1) idx = GlobalValues.fullJarFilePath.lastIndexOf('/');  // try Java standard File seperator
                 
                 command[0] =  "java";
                 command[1] = "-cp";
                 command[2] =   "."+File.pathSeparator+GlobalValues.scalalabLibPath+File.pathSeparator+
                         scalalab.JavaGlobals.compFile+   File.pathSeparator+
                         scalalab.JavaGlobals.libFile+ File.pathSeparator+
                         scalalab.JavaGlobals.reflectFile+ File.pathSeparator+
                          scalalab.JavaGlobals.ejmlFile+File.pathSeparator+
                         scalalab.JavaGlobals.jblasFile+File.pathSeparator+
                         scalalab.JavaGlobals.jsciFile+File.pathSeparator+
                         scalalab.JavaGlobals.javacppFile+File.pathSeparator+
                         scalalab.JavaGlobals.mtjColtSGTFile+File.pathSeparator+
                         scalalab.JavaGlobals.ApacheCommonsFile+File.pathSeparator+
                         scalalab.JavaGlobals.jfreechartFile+File.pathSeparator+
                         scalalab.JavaGlobals.numalFile+File.pathSeparator+
                         scalalab.JavaGlobals.LAPACKFile+File.pathSeparator+
                         scalalab.JavaGlobals.ARPACKFile+File.pathSeparator+
                         scalalab.JavaGlobals.JASFile+File.pathSeparator+
                        GlobalValues.jarFilePath;
                 command[3] =  "scala.tools.nsc.Main";    // the name of the Scala compiler class
                 command[4] = "-classpath";
                 command[5] = command[2];
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
           }
           });
           
   
           JMenuItem runScalaMenuItem = new JMenuItem("Run compiled Scala class with Java");
           runScalaMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           runScalaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String selectedValue = scalaLabPathsListener.selectedValue;
             if (selectedValue.indexOf(".class")!=-1)  {   // not a compiled .class file
         
                 String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
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
                String commandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3];
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
           }
           });
     
     JMenuItem runScalaWithScalaMenuItem = new JMenuItem("Run Scala script with Scala");
     runScalaWithScalaMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
     runScalaWithScalaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String selectedValue = scalaLabPathsListener.selectedValue;
             
                 String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                 
                 GlobalValues.selectedExplorerPath = pathComponent;


                 String [] command  = new String[7];

                 int idx = GlobalValues.fullJarFilePath.lastIndexOf(File.separatorChar);
                 if (idx == -1) idx = GlobalValues.fullJarFilePath.lastIndexOf('/');  // try Java standard File seperator

                 command[0] =  "java";
                 command[1] = "-classpath";
                 command[2] =   "."+File.pathSeparator+GlobalValues.jarFilePath+File.pathSeparator+pathComponent;  // set the classpath for running
                 command[3] =  "scala.tools.nsc.MainGenericRunner";    // the name of the Scala compiler class
                 command[4] = "-classpath";
                 command[5] = "."+File.pathSeparator+GlobalValues.jarFilePath+File.pathSeparator+pathComponent;  // set the classpath for running
                 command[6] = selectedValue;
                 
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
           
           });


           JMenuItem compileRunItem = new JMenuItem("Compile .java file and run");
           compileRunItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           compileRunItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GlobalValues.extensionClassLoader = new ExtensionClassLoader(GlobalValues.ScalaSciClassPath);
                JavaCompile javaCompileObj = new JavaCompile();
                ExtensionClassLoader  extClassLoader = GlobalValues.extensionClassLoader;
                String selectedValue = scalaLabPathsListener.selectedValue;
                String  pathComponent = selectedValue.substring(0, selectedValue.lastIndexOf(File.separatorChar));
                GlobalValues.selectedExplorerPath = pathComponent; 
                String javaFile = selectedValue;
                String packageName = "";   // do not place the new Java Classes within some package !!
               if (selectedValue.indexOf(".java")!=-1)  {   // file is a Java source file
         boolean compilationResult = javaCompileObj.compileFile(javaFile);
         if (compilationResult == true)  // success
         {
             GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();
 
             System.out.println("Compilation success for file "+packageName+"."+javaFile);
             int lastPos = javaFile.length()-5;  // for ".java"
             
             String javaFileWithoutExt = javaFile.substring(0, javaFile.indexOf('.'));
             String javaFileWithoutExtWithoutPath = javaFileWithoutExt.substring(javaFileWithoutExt.lastIndexOf(File.separatorChar)+1, javaFileWithoutExt.length());
             pathComponent = javaFile.substring(0, javaFile.lastIndexOf(File.separatorChar));
      
             
             String [] command  = new String[4];
             
                 command[0] =  "java";
                 command[1] = "-cp";
                 command[2] = pathComponent+"."+File.pathSeparator+GlobalValues.scalalabLibPath+
                         File.pathSeparator+GlobalValues.jarFilePath;
                 command[3] =  javaFileWithoutExtWithoutPath;    
                 
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
                String commandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3];
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
                
                
                
                
            }   // compilation result success
               }  // file is a java source file
                   }   // actionPerformed
                });  // addActionListener
        
                
            JMenuItem updatePathsJMenuItem = new JMenuItem("Update the display of the selected object folder");
            updatePathsJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont);
            updatePathsJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
             GlobalValues.selectedExplorerPath = scalaLabPathsListener.selectedValue;
             GlobalValues.scalalabMainFrame.explorerPanel.updatePaths();
            }
        });
           
           JMenu  fileOpsItems = new JMenu("File Operations");
           fileOpsItems.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           fileOpsItems.add(browseUpItem);
           fileOpsItems.add(newFileItem);
           fileOpsItems.add(newFileInDirItem);
           fileOpsItems.add(newDirectoryItem);
           fileOpsItems.add(renameFileJItem);
           fileOpsItems.add(deleteFileItem);
           pathsPopupMenu.add(fileOpsItems);
           
           pathsPopupMenu.add(specifyAsJarToolbox);
           
           
           JMenu JavaMenu = new JMenu("Java");
           JavaMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           JavaMenu.add(compileJavaWithJavaCMenuItem);
           JavaMenu.add(runJavaWithJavaMenuItem);
           JavaMenu.add(compileRunItem);
           pathsPopupMenu.add(JavaMenu);
                   
                
           
           JMenu scalaMenu = new JMenu("Scala");
           scalaMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           scalaMenu.add(compileScalaCMenuItem);
           scalaMenu.add(runScalaMenuItem);
           scalaMenu.add(runScalaWithScalaMenuItem);
           pathsPopupMenu.add(scalaMenu);
           
           
           
           JMenu  pathsMenu = new JMenu("Paths");
           pathsMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           pathsMenu.add(addPathToscalaSciPathsItem);
           pathsMenu.add(specifyAsJarToolbox);
           pathsMenu.add(removePathFromScalaSciPathsItem);
           pathsMenu.add(favouritesPathsAddItem);
           pathsPopupMenu.add(pathsMenu);
           
           JMenu editMenu = new JMenu("Edit");
           editMenu.setFont(scalaExec.Interpreter.GlobalValues.puifont);
           editMenu.add(editInNewItem);
           editMenu.add(jeditItem);
           pathsPopupMenu.add(editMenu);
           
           pathsPopupMenu.add(updatePathsJMenuItem);

}
    

// Returns the full pathname for a path, or null
// if not a known path
public String getPathName(TreePath path) {
  Object o = path.getLastPathComponent();
  if (o instanceof FileTreeNode) {
     return ((FileTreeNode)o).file.getAbsolutePath();
  } return null;
} 

// Returns the File for a path, or null if not a known path
public File getFile(TreePath path) { 
  Object o = path.getLastPathComponent();
  if (o instanceof FileTreeNode) {
    return ((FileTreeNode)o).file;
  }
  return null;
} 

// Inner class that represents a node in this file system tree
protected static class FileTreeNode extends DefaultMutableTreeNode {
    protected File file;// File object for this node
    protected String name;// Name of this node
    protected boolean populated;    // true if we have been populated this node
    protected boolean interim;    // true if we are in interim state
    protected boolean isDir;// true if this is a directory

    public FileTreeNode(File parent, String name)
         throws SecurityException, FileNotFoundException { 
      this.name = name; 

  // see if this node exists and whether it is a directory
  file = new File(parent, name);  // Creates a new File instance from a parent abstract pathname and a child pathname string
  if (!file.exists()) {
    throw new FileNotFoundException("File " + name + " does not exist");
  }

  isDir = file.isDirectory(); 

  // hold the File as the user object for the tree node
  setUserObject(file); 

} 

// Override isLeaf to check whether this is a directory 
public boolean isLeaf() {
  return !isDir;   // files correspond to leaf nodes
} 

// Override getAllowsChildren to check whether
// this is a directory
public boolean getAllowsChildren() {
  return isDir;
} 


// For display purposes, we return our own name 
public String toString() { return name; } 

// If we are a directory, scan our contents and populate
// with children. In addition, populate those children
// if the "descend" flag is true. We only descend once,
// to avoid recursing the whole subtree.
// Returns true if some nodes were added
boolean populateDirectories(boolean descend) {
  boolean addedNodes = false; 
// Do this only once 
if (populated == false) {
  if (interim == true) { 
    // We have had a quick look here before:
    // remove the dummy node that we added last time
    removeAllChildren();
    interim = false; 
  } 

  String[] names = file.list();// Get list of contents 

  // Process the directories
  for (int i = 0; i < names.length; i++) {
    String name = names[i];  
    File d = new File(file, name);
    try {
        FileTreeNode node =  new FileTreeNode(file, name);
        this.add(node);
      if (d.isDirectory()) {  // file is a directory
        if (descend) {
          node.populateDirectories(false); 
        }
        addedNodes = true;
        if (descend == false) {
          // Only add one node if not descending 
          break; 
        }
      } 
    } catch (Throwable t) {
      // Ignore phantoms or access problems
    } 
  } 

  // If we were scanning to get all subdirectories,
  // or if we found no subdirectories, there is no
  // reason to look at this directory again, so
  // set populated to true. Otherwise, we set interim
  // so that we look again in the future if we need to
  if (descend == true || addedNodes == false) {
    populated = true; 
  } else {
  // Just set interim state
          interim = true;
        }
      }
      return addedNodes; 
    } 

  } 

  // Inner class that handles Tree Expansion Events  (i.e. when a tree expands or collapses a node)
  protected class TreeExpansionHandler  implements TreeExpansionListener {
    public void treeExpanded(TreeExpansionEvent evt) { 
      TreePath path = evt.getPath();// The expanded path JTree tree  (JTree)evt.getSource();// The tree 

      // Get the last component of the path and
      // arrange to have it fully populated.
      FileTreeNode node =  (FileTreeNode)path.getLastPathComponent();
      if (node.populateDirectories(true)) {
          JTree tree = (JTree)evt.getSource();
        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
  } 
    } 

    public void treeCollapsed(TreeExpansionEvent evt) {
      // Nothing to do 
    } 
  } 
  
  public static void main(String [] args) {
      JFrame testFrame = new JFrame("testing FileTree");
      FileTreeExplorer   ftree=null;
              
              try {
          ftree = new FileTreeExplorer("/export/home/sterg");
      }
      catch (FileNotFoundException exc)  {}
      
      JPanel  myPanel = new JPanel();
      myPanel.add(ftree.pathsTree);
      testFrame.add(myPanel);
      testFrame.setSize(500, 600);
      testFrame.setVisible(true);
 }
  
         

  class explorerTreeCellRenderer  extends DefaultTreeCellRenderer {
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
      {
          super.getTreeCellRendererComponent(tree, value,selected, expanded, leaf, row, hasFocus);
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
          File  treeNodeLabel = (File) node.getUserObject();
          if (treeNodeLabel.isDirectory())
              setFont(directoryFont);
          else  {
          String fileName = treeNodeLabel.getName();
          if (fileName.endsWith(".java"))
              setFont(javaFilesFont);
          else if (fileName.endsWith(".scala"))
              setFont(scalaFilesFont);
          else if (fileName.endsWith(".class"))
              setFont(classFilesFont);
          else
              setFont(defaultFont);
          }
          return this;
      }
  }
} 

