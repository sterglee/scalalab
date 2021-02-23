
package scalaExec.scalaLab;

import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;


// implements the functionality for browsing 
public class scalaLabPathsListener  implements TreeSelectionListener   {
    JTree  pathsTree;
        // the selectedValue keeps the full pathname of the selected object for further processing
    public static String selectedValue;
    public static String selectedPath; 
    public static DefaultMutableTreeNode   parentOfSelectedNode;  // the parent node of the selected node of the JTree
    public static DefaultMutableTreeNode   selectedNode;  // the currently selected node of the JTree
         // initialize the listener object for the corresponding JTree
    public scalaLabPathsListener(JTree classPathsTree) {
      pathsTree = classPathsTree;
    }
    
    public void explicitUpdate()  {
        TreePath path = pathsTree.getSelectionPath();  // get the full path to the selected node
        if (path==null) return;  // not any tree's node selected
    	    selectedNode =  (DefaultMutableTreeNode) path.getLastPathComponent();
            parentOfSelectedNode = (DefaultMutableTreeNode) selectedNode.getParent(); 
    	    Object [] objPath = selectedNode.getUserObjectPath();
            int len = objPath.length;
        // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path          
            scalaLabPathsListener.selectedValue = objPath[len-1].toString();
            scalaLabPathsListener.selectedPath = selectedValue.substring(0, selectedValue.lastIndexOf(File.separator));
    }
    @Override
    	  public void valueChanged(TreeSelectionEvent event) {
              new Thread(new Runnable()   {  // Runnable-out
           public void  run()  { // run-out
                  SwingUtilities.invokeLater(new Runnable() {  // Runnable-in
           public void run() {  // run in  */
      
               TreePath path = pathsTree.getSelectionPath();
               if (path==null) return;  // not any tree's node selected
    		  selectedNode =  (DefaultMutableTreeNode) path.getLastPathComponent();
                parentOfSelectedNode = (DefaultMutableTreeNode) selectedNode.getParent(); 
    		Object [] objPath = selectedNode.getUserObjectPath();
                int len = objPath.length;
        // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path          
                  scalaLabPathsListener.selectedValue = objPath[len-1].toString();
                  scalaLabPathsListener.selectedPath = selectedValue.substring(0, selectedValue.lastIndexOf(File.separator));
                   }
                  });  // Runnable-in
           }  // run-out
              }).start();  // Runnable-out
                
          }  // valueChanged


}
