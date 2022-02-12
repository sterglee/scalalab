
package scalaExec.gui;


import java.util.logging.Level;
import java.util.logging.Logger;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.scalaLab.scalaSciToolboxes;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;


// this class uses reflection to examine classes. The extracted information is displayed with the JTree format
public class WatchClasses  {
    private JTree  loadedClassesTree;  // the JTree that organizes loaded class information
    int  watchXSize,  watchYSize;  // the size of the watch Frame
    static public int  watchXLoc = 100,  watchYLoc = 100;   // the location of the watch frame
    
    public WatchClasses()
    	{
        Dimension screenSiz = Toolkit.getDefaultToolkit().getScreenSize();
        watchXSize = (int)(0.4*(double)screenSiz.width);
        watchYSize =(int)(0.4*(double)screenSiz.height);
    }


  // get the classes of the toolbox with name toolboxName using a String. The bytecodes of the classes are kept in the Vector classes.
  public  int  displayClassesAndMethodsAsString(Vector  classes, String toolboxName, String filterString, int watchXLoc, int watchYLoc) {
        JFrame watchFrame = new JFrame("Classes and methods from toolbox: "+toolboxName+" relevant to keyword:  "+filterString);
        watchFrame.setSize( watchXSize, watchYSize);
        watchFrame.setLocation(watchXLoc, watchYLoc);
       
        Container container  = watchFrame.getContentPane();
        
    StringBuilder  loadedClasses = new StringBuilder();
    
        int classCnt = 0;
        Enumeration  iter = classes.elements();  // each element corresponds to a structure Class
        while(iter.hasMoreElements())
		{
            Object next = (Object)iter.nextElement();
            Class currentClass = (Class)next;
            String className = currentClass.getCanonicalName();
                    
            if (className.contains(filterString))   // it has the pattern
               loadedClasses.append(className+"\n");
                    
            java.lang.reflect.Method []  classMethods = currentClass.getDeclaredMethods();
            for (int k=0; k<classMethods.length; k++) {
                Method currentMethod = classMethods[k];
                int  methodModifier = currentMethod.getModifiers();
                if (Modifier.isPublic(methodModifier) || Modifier.isStatic(methodModifier)) {
                   String  classMethodsStr = classMethods[k].toString();
                    if (classMethodsStr.contains(filterString))    // it has the pattern
                       loadedClasses.append(classMethodsStr+"\n");
                         }
                    }
                    classCnt++;
                }
    
        JTextArea classesArea = new JTextArea(loadedClasses.toString());
        classesArea.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize) );
        JScrollPane  classesPane = new JScrollPane(classesArea);

    container.add(classesPane);
    watchFrame.setVisible(true);
    
    return classCnt;

      }
    

  // display the classes of the toolbox with name toolboxName using a JTree component. The bytecodes of the classes are kept in the Vector classes.
  public  int  displayOnlyClassNames(Vector  classes, String toolboxName, int watchXLoc, int watchYLoc) {
        JFrame watchFrame = new JFrame("Class  Files  from toolbox: "+toolboxName);
        watchFrame.setSize( watchXSize, watchYSize);
        watchFrame.setLocation(watchXLoc, watchYLoc);
       
        Container container  = watchFrame.getContentPane();
        
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Classes Files ");
    
        int classCnt = 0;
        Enumeration  iter = classes.elements();  // each element corresponds to a structure Class
        while(iter.hasMoreElements())
		{
		    Object next = (Object)iter.nextElement();
		    String className = (String)next;
                    if (!className.contains("$"))  // display only user classes
                     {
                    DefaultMutableTreeNode nCurrentClass = new DefaultMutableTreeNode(className);
                    root.add(nCurrentClass);
                    }
	}
    
    loadedClassesTree = new JTree(root);  // the tree of global system variables
    // single selection
    int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
    loadedClassesTree.getSelectionModel().setSelectionMode(mode);
       
    JScrollPane  treePane = new JScrollPane(loadedClassesTree);
    container.add(treePane);
    watchFrame.setVisible(true);
    
    // keep the correspondance between toolboxnames and their frames in order to be able to remove the
    // frame upon toolbox removal
    scalaSciToolboxes.framesOfToolboxClasses.put(toolboxName, watchFrame);
    return classCnt;

      }
 
  
    
  // display the classes of the toolbox with name toolboxName using a JTree component. The bytecodes of the classes are kept in the Vector classes.
  public  int  displayClassesAndMethods(Vector  classes, String toolboxName, int watchXLoc, int watchYLoc) {
                JFrame watchFrame = new JFrame("Loaded "+classes.size()+"  classes from toolbox: "+toolboxName);
        watchFrame.setSize( watchXSize, watchYSize);
        watchFrame.setLocation(watchXLoc, watchYLoc);
       
        Container container  = watchFrame.getContentPane();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Loaded Classes");
    
        int classCnt = 0;
        Enumeration  iter = classes.elements();  // each element corresponds to a structure Class
        while(iter.hasMoreElements())
		{
            Object next = (Object)iter.nextElement();
            Class currentClass = (Class)next;
            String className = currentClass.getCanonicalName();
                    
            DefaultMutableTreeNode nCurrentClass = new DefaultMutableTreeNode(className);
            root.add(nCurrentClass);
                    
            java.lang.reflect.Method []  classMethods = currentClass.getDeclaredMethods();
            for (int k=0; k<classMethods.length; k++) {
                Method currentMethod = classMethods[k];
                int  methodModifier = currentMethod.getModifiers();
                if (Modifier.isPublic(methodModifier) || Modifier.isStatic(methodModifier)) {
                          DefaultMutableTreeNode  methodNode = new DefaultMutableTreeNode(classMethods[k].toString());
                          nCurrentClass.add(methodNode);
                         }
                    }
                    classCnt++;
                }
    
    loadedClassesTree = new JTree(root);  // the tree of global system variables
    // single selection
    int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
    loadedClassesTree.getSelectionModel().setSelectionMode(mode);
       
    JScrollPane  treePane = new JScrollPane(loadedClassesTree);
    container.add(treePane);
    watchFrame.setVisible(true);
    
    return classCnt;

      }

    


    
  // display the classes of the toolbox with name toolboxName using a JTree component. The bytecodes of the classes are kept in the Vector classes.
  public  int  displayClasses(Vector  classes, String toolboxName, int watchXLoc, int watchYLoc) {
        JFrame watchFrame = new JFrame("Loaded classes from toolbox: "+toolboxName);
        watchFrame.setSize( watchXSize, watchYSize);
        watchFrame.setLocation(watchXLoc, watchYLoc);
       
        Container container  = watchFrame.getContentPane();
        
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Loaded Classes");
        int classCnt = 0;
        Enumeration  iter = classes.elements();  // each element corresponds to a structure Class
        while(iter.hasMoreElements())
		{
                    Object next = (Object)iter.nextElement();
                    String  examineClassName = next.toString();
                        if  ((examineClassName.contains("$")==false) && (examineClassName.endsWith(".class")==true))   {  // not a special class file
                    Class currentClass=null;
                try {
                    examineClassName = examineClassName.substring(0, examineClassName.length()-6);   // remove ".class" suffix
                    currentClass = GlobalValues.extensionClassLoader.loadClass(examineClassName);
                if (currentClass != null)  {
                    String className = currentClass.getCanonicalName();
                    
                    DefaultMutableTreeNode nCurrentClass = new DefaultMutableTreeNode(className);
                    root.add(nCurrentClass);
                    
                 if (GlobalValues.retrieveAlsoMethods)  {
                     java.lang.reflect.Constructor [] constructors = currentClass.getConstructors();
                     int numConstructors = constructors.length;
                     for (int c=0; c<numConstructors; c++)  {
                         DefaultMutableTreeNode  constructorNode = new DefaultMutableTreeNode(constructors[c].toString());
                         nCurrentClass.add(constructorNode);
                     }
                         
                             
                    java.lang.reflect.Method []  classMethods = currentClass.getDeclaredMethods();
                    for (int k=0; k<classMethods.length; k++) {
                        Method currentMethod = classMethods[k];
                        int  methodModifier = currentMethod.getModifiers();
                        if (Modifier.isPublic(methodModifier) || Modifier.isStatic(methodModifier)) {
                           DefaultMutableTreeNode  methodNode = new DefaultMutableTreeNode(classMethods[k].toString());
                          nCurrentClass.add(methodNode);
                         }
                    }
                    classCnt++;
                          }
                        }
                        } // try
                 catch (Exception ex) {
                    System.out.println("Cannot load class "+currentClass);
                  }
                }
 }
    
    loadedClassesTree = new JTree(root);  // the tree of global system variables
    // single selection
    int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
    loadedClassesTree.getSelectionModel().setSelectionMode(mode);
       
    JScrollPane  treePane = new JScrollPane(loadedClassesTree);
    container.add(treePane);
    watchFrame.setVisible(true);
    
    // keep the correspondance between toolboxnames and their frames in order to be able to remove the
    // frame upon toolbox removal
    scalaSciToolboxes.framesOfToolboxClasses.put(toolboxName, watchFrame);
    return classCnt;

      }
    


    }

