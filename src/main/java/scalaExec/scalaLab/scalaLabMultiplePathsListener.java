
package scalaExec.scalaLab;

import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;


// implements the functionality for browsing
public class scalaLabMultiplePathsListener  implements TreeSelectionListener   {
    JTree  pathsTree;
  // the selectedValue keeps the full pathname of the selected object for further processing
    public static String [] selectedValues;
    public static String [] selectedPaths;
    public static String  selectedValue;
    public static String  selectedPath;
    public static DefaultMutableTreeNode  []  parentsOfSelectedNodes;  // the parent nodes of the selected nodes of the JTree
    public static DefaultMutableTreeNode []  selectedNodes;  // the currently selected nodes of the JTree

    public scalaLabMultiplePathsListener(JTree classPathsTree) {
      pathsTree = classPathsTree;
}

    public void explicitUpdate()  {
        TreePath [] paths = pathsTree.getSelectionPaths();
        if (paths==null) return;  // not any tree's node selected
        int numOfSelectedNodes = paths.length;
        selectedNodes = new DefaultMutableTreeNode[numOfSelectedNodes];
        selectedValues = new String[numOfSelectedNodes];
        selectedPaths = new String[numOfSelectedNodes];
        parentsOfSelectedNodes = new DefaultMutableTreeNode[numOfSelectedNodes];
        for (int k=0; k<numOfSelectedNodes; k++)  { // for all the selected nodes
          selectedNodes[k] =  (DefaultMutableTreeNode) paths[k].getLastPathComponent();
          parentsOfSelectedNodes[k] = (DefaultMutableTreeNode) selectedNodes[k].getParent();
          Object [] objPath = selectedNodes[k].getUserObjectPath();
          int len = objPath.length;
        // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path
          scalaLabMultiplePathsListener.selectedValues[k] = objPath[len-1].toString();
          scalaLabMultiplePathsListener.selectedPaths[k] = selectedValues[k].substring(0, selectedValues[k].lastIndexOf(File.separator));
      }
        selectedValue = selectedValues[0];
        selectedPath = selectedPaths[0];
    }
    @Override
    	  public void valueChanged(TreeSelectionEvent event) {
              new Thread(new Runnable()   {  // Runnable-out
            @Override
           public void  run()  { // run-out
                  SwingUtilities.invokeLater(new Runnable() {  // Runnable-in
                    @Override
           public void run() {  // run in  */
                TreePath [] paths = pathsTree.getSelectionPaths();
                if (paths==null) return;  // not any tree's node selected
                int numOfSelectedNodes = paths.length;
                selectedNodes =  new DefaultMutableTreeNode[numOfSelectedNodes];
                selectedValues = new String[numOfSelectedNodes];
                selectedPaths = new String[numOfSelectedNodes];
                parentsOfSelectedNodes = new DefaultMutableTreeNode[numOfSelectedNodes];
                for (int k=0; k<numOfSelectedNodes; k++)   { // for all the selected nodes
                    selectedNodes[k] = (DefaultMutableTreeNode) paths[k].getLastPathComponent();
                    parentsOfSelectedNodes[k] = (DefaultMutableTreeNode) selectedNodes[k].getParent();
                    Object [] objPath = selectedNodes[k].getUserObjectPath();
                    int len = objPath.length;
        // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path
                  scalaLabMultiplePathsListener.selectedValues[k] = objPath[len-1].toString();
                  scalaLabMultiplePathsListener.selectedPaths[k] = selectedValues[k].substring(0, selectedValues[k].lastIndexOf(File.separator));
                      }
        selectedValue = selectedValues[0];
        selectedPath = selectedPaths[0];
                   }
                  });  // Runnable-in
           }  // run-out
              }).start();  // Runnable-out

          }  // valueChanged


}
