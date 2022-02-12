package scalaExec.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import scalaExec.Interpreter.GlobalValues;

import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import scalaExec.scalaLab.FileTreeExplorer;

/**A function for displaying and configuring scalaLab's parameters */
public class scalaSciScriptsPathsTree extends JPanel {
	private JTree GVarsTree;
        public  DefaultTreeModel  model;

 public void buildVariablesTree()  {
        // root node 
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Global Path Variables");
     
    DefaultMutableTreeNode nscalaLabJavaClassPath = new DefaultMutableTreeNode("ScalaSci Classes Dir");
    
    int numOfDirsOfClassPath = GlobalValues.ScalaSciClassPathComponents.size();
    for (int k=0; k<numOfDirsOfClassPath; k++) {
    	String currentDirElem = GlobalValues.ScalaSciClassPathComponents.elementAt(k).toString().trim();
    	DefaultMutableTreeNode nvscalaLabClassPath = new DefaultMutableTreeNode(currentDirElem);
        nscalaLabJavaClassPath.add(nvscalaLabClassPath);
    }
    root.add(nscalaLabJavaClassPath);
    
    // workingDir: Current Working Directory
    String wkDir = GlobalValues.workingDir;
    DefaultMutableTreeNode cwdFilesDir = new DefaultMutableTreeNode("Current Working Directory");
    DefaultMutableTreeNode vcwdFilesDir = new DefaultMutableTreeNode(wkDir);
    root.add(cwdFilesDir); cwdFilesDir.add(vcwdFilesDir);

    model = new DefaultTreeModel(root);   // a simple tree data model that uses TreeNodes


    GVarsTree = new JTree(model);  // JTree displays the set of hierarchical data of model
    GVarsTree.setFont(GlobalValues.guifont);
    
    // single selection
    int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
    GVarsTree.getSelectionModel().setSelectionMode(mode);
 
    GVarsTree.expandRow(1);  // expand the tree in order the user to see it
    GVarsTree.setToolTipText("User paths for ScalaSci Classpath. To remove a component from Scala classpath press DEL and restart a new interpreter ");
       
    add(new JScrollPane(GVarsTree));

    GVarsTree.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
              if (keyCode == e.VK_DELETE) {
               TreePath path = GVarsTree.getSelectionPath();
    		if (path==null) return;  // not any tree's node selected
    		DefaultMutableTreeNode selectedNode =
                 (DefaultMutableTreeNode) path.getLastPathComponent();
    		String selectedValue;
    		Enumeration enumVars;
    		String cVarName;
//  variable values are at depth 0, variable names at depth 1
    		if (selectedNode.getDepth() == 0)  {
    			selectedValue = (String) selectedNode.getUserObject();
               javax.swing.JOptionPane.showMessageDialog(null, "removing  "+selectedValue);
               
               model.removeNodeFromParent(selectedNode);

                     // remove the specified path from the scalaSci paths if already exists
                if (GlobalValues.ScalaSciClassPathComponents.contains(selectedValue) == true)
                   GlobalValues.ScalaSciClassPathComponents.remove(selectedValue);

                if (GlobalValues.ScalaSciUserPaths.contains(selectedValue) == true)
                    GlobalValues.ScalaSciUserPaths.remove(selectedValue);

                //scalaSciCommands.BasicCommands.scalaInterpreterWithClassPathComponents();   // create an interpreter with the reduced classpath

                StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce.toString().trim());
    }
      GlobalValues.ScalaSciClassPath  = fileStr.toString();

      scalaExec.scalaLab.scalaLab.updateTree();
            }
        }
            } // keypressed

         
            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });

    GVarsTree.addTreeSelectionListener(new TreeSelectionListener() {
    	public void valueChanged(TreeSelectionEvent event) {
    		TreePath path = GVarsTree.getSelectionPath();
    		if (path==null) return;  // not any tree's node selected
    		DefaultMutableTreeNode selectedNode =
                 (DefaultMutableTreeNode) path.getLastPathComponent();
    		String selectedValue;
    		Enumeration enumVars;
    		String cVarName;
//  variable values are at depth 0, variable names at depth 1    		
    		if (selectedNode.getDepth() == 0)  {  
    			selectedValue = (String) selectedNode.getUserObject();
                File testExists = new File(selectedValue);
                if (testExists.exists() && testExists.isDirectory()) {
                try {
                FileTreeExplorer ftree = new FileTreeExplorer(selectedValue);
                GlobalValues.currentFileExplorer = ftree;
                JFrame treeFrame = new JFrame("Files under the directory "+selectedValue);
                treeFrame.add(new JScrollPane(ftree.pathsTree));
                treeFrame.setSize(600, 500);
                treeFrame.setVisible(true);
                 }
                        catch (FileNotFoundException ex) {
                            Logger.getLogger(scalaSciScriptsPathsTree.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(scalaSciScriptsPathsTree.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                else {
                selectedValue = (String) selectedNode.getUserObject();
               int  response = javax.swing.JOptionPane.showConfirmDialog(null, "remove node? ");
               if (response == JOptionPane.OK_OPTION) {
                model.removeNodeFromParent(selectedNode);

                     // remove the specified path from the scalaSci paths if already exists
                if (GlobalValues.ScalaSciClassPathComponents.contains(selectedValue) == true)
                   GlobalValues.ScalaSciClassPathComponents.remove(selectedValue);

                if (GlobalValues.ScalaSciUserPaths.contains(selectedValue) == true)
                    GlobalValues.ScalaSciUserPaths.remove(selectedValue);

                //scalaSciCommands.BasicCommands.scalaInterpreterWithClassPathComponents();   // create an interpreter with the reduced classpath

                StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciClassPathComponents.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce.toString().trim());
    }
      GlobalValues.ScalaSciClassPath  = fileStr.toString();

      scalaExec.scalaLab.scalaLab.updateTree();
            }  // OK to remove the node
        }  // FileNotFoundException

                 }
                }
             });  // addTreeSelectionListener
 
 }
}
