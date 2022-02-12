package scalaExec.scalaLab;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import scalaExec.ClassLoaders.ExtensionClassLoader;
import scalaExec.ClassLoaders.JarClassLoader;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.GBC;

// the scalaLab Explorer acts as a file system manager for the file system components
import scalaExec.gui.WatchClasses;

// handles operations on the scalaLab paths, i.e.  the ScalaSciClassPath for Java classes and sources
// The scalaLab Explorer allows compiling and running Java classes, running Scala scripts and Files, 
// editing files and creating new files and directories

public    class  scalaLabExplorer extends JPanel   {
       JPanel   ScriptsClassesPanel = new JPanel(new GridBagLayout());
       JPanel   TreePanel = new JPanel(new BorderLayout());
       public static JComboBox  ScalaScriptClassesCB;
       public static JComboBox  favouritePathsCB;
       JCheckBox removeFavourite;
       Vector scalaClassesPaths = new Vector();

       JTextField  specifyPathsField;
       

protected void paintComponent(Graphics g) {
    if (GlobalValues.desktophints!=null)
    {
    Graphics2D g2d = (Graphics2D)g;
    g2d.addRenderingHints(GlobalValues.desktophints);
    }
}

       private class MouseCBAdapterForScalaSci  extends  MouseAdapter {
          public void mousePressed(MouseEvent event) {
              if ((event.getModifiersEx() ) != 0)    // right mouse click
                  new browseScalaSciFilesAction().actionPerformed(null);
          }
       }
       
    public scalaLabExplorer() {
      super();
                
    }

       public  void updatePaths() {
          String pathSelected="";
           try {
         pathSelected = GlobalValues.selectedExplorerPath;
         FileTreeExplorer  currentJPathTree = new FileTreeExplorer(pathSelected);
         GlobalValues.currentFileExplorer = currentJPathTree;
         TreePanel.removeAll();
         JScrollPane   scriptsPane = new JScrollPane(currentJPathTree.pathsTree);
         TreePanel.add(scriptsPane);
         TreePanel.revalidate();
    }
    catch (FileNotFoundException ex) {
        System.out.println("Error exploring "+pathSelected); 
        ex.printStackTrace();
      }
       }
       
               
       public void buildClassScriptPathsTree()  {
       // the ScriptsClassesPanel has components that enable the user to navigate to the roots of the 
      // Java classes and Scala  classes and scripts filesystems   
    Font smallFont = GlobalValues.buifont; //new Font("Arial", Font.PLAIN, 10);                   
    ScriptsClassesPanel.removeAll();
    ScriptsClassesPanel.setLayout(new GridLayout(7,1));
    
    JButton  scalaSciClassPathButton = new JButton("Update ScalaClassPath to include selected files");
    scalaSciClassPathButton.setFont(GlobalValues.buifont);
    scalaSciClassPathButton.setToolTipText("Browse filesystem and select the file that you want to have accessible from the ScalaClassLoader");
    scalaSciClassPathButton.addMouseListener(new MouseCBAdapterForScalaSci());
    ScriptsClassesPanel.add(scalaSciClassPathButton);
    
    int numOfDirsOfscalaSciPath = GlobalValues.ScalaSciClassPathComponents.size();
            scalaClassesPaths.clear();
            scalaClassesPaths.add("ScalaClassPath");
               for (int k=0; k<numOfDirsOfscalaSciPath; k++) {
        String currentDirElem = GlobalValues.ScalaSciClassPathComponents.elementAt(k).toString().trim();
        scalaClassesPaths.add(currentDirElem);
    }
    ScalaScriptClassesCB  = new JComboBox(scalaClassesPaths);
    ScalaScriptClassesCB.setFont(smallFont);
    ScalaScriptClassesCB.setToolTipText("The user paths of the Scala Interpreter ClassPath. Select a path to browse it");
    ScalaScriptClassesCB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (ScalaScriptClassesCB.getItemCount()>0)  {
                    String currentDirElem = (String)ScalaScriptClassesCB.getSelectedItem();
                    if (currentDirElem.endsWith(".jar")) {
                        String jarFileSelected = currentDirElem;
                        Vector toolboxClasses=null;
                        
                        try {
                            GlobalValues.ScalaToolboxesLoader = new JarClassLoader();
                            GlobalValues.ScalaToolboxesLoader.extendClassPath(jarFileSelected);
                            toolboxClasses = GlobalValues.ScalaToolboxesLoader.scanAllJarClasses(jarFileSelected);
                        }
                        catch (java.io.IOException ioe) {
                            System.out.println("IO Exception in reading from "+jarFileSelected);
                        }
                        WatchClasses watchClasses = new WatchClasses();
                        watchClasses.displayOnlyClassNames(toolboxClasses, jarFileSelected, 100, 100);
                    }
                    else {
                   File elemFile = new File(currentDirElem);
                        if (elemFile.exists()) {
                    try {    
         FileTreeExplorer  currentJPathTree = new FileTreeExplorer(currentDirElem);
         GlobalValues.currentFileExplorer = currentJPathTree;
         TreePanel.removeAll();
         JScrollPane   classesPane = new JScrollPane(currentJPathTree.pathsTree);
         TreePanel.add(classesPane);
         TreePanel.revalidate();
      }
      catch (FileNotFoundException  ex) { 
          System.out.println("File: "+currentDirElem+" not found in scalaLabExplorer");
          ex.printStackTrace();
                    }
                  }    // file exists
                 }
                }
                }
            });
    ScriptsClassesPanel.add( ScalaScriptClassesCB);
            
    
    
    JPanel pathSpecPanel = new JPanel(new GridLayout(1,3));
    JLabel  specifyPathLabel = new JLabel("Specify Path");
    specifyPathLabel.setFont(GlobalValues.buifont);
    pathSpecPanel.add(specifyPathLabel);
    specifyPathsField = new JTextField(5);
    specifyPathsField.setFont(smallFont);
    if (GlobalValues.hostIsUnix)  specifyPathsField.setText("/");  else specifyPathsField.setText("c:/");
    pathSpecPanel.add(specifyPathsField);
    ScriptsClassesPanel.add(pathSpecPanel);
    JButton specifyPathButton = new JButton("Browse");
    specifyPathButton.setToolTipText("Browse the specified path");
    specifyPathButton.setFont(smallFont);
    specifyPathButton.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {
              String  pathSelected = specifyPathsField.getText();
    try {
        boolean favoritePathExists = GlobalValues.favouriteElements.contains(pathSelected);
        
        if (favoritePathExists==false)  {
            favouritePathsCB.addItem(pathSelected);
            GlobalValues.favouriteElements.add(pathSelected);
        }
        
           GlobalValues.selectedExplorerPath = pathSelected;
         FileTreeExplorer  currentJPathTree = new FileTreeExplorer(pathSelected);
         GlobalValues.currentFileExplorer = currentJPathTree;
         TreePanel.removeAll();
         JScrollPane   scriptsPane = new JScrollPane(currentJPathTree.pathsTree);
         TreePanel.add(scriptsPane);
         TreePanel.revalidate();
    }
    catch (FileNotFoundException ex) {
        System.out.println("Error exploring "+pathSelected); 
        ex.printStackTrace();
      }
              
            }
        });
    pathSpecPanel.add(specifyPathButton);
    ScriptsClassesPanel.add(pathSpecPanel);
    
    JPanel  favouritePanel = new JPanel (new GridLayout(1,2));
    JLabel  favouritePathsLabel = new JLabel("Favourite Paths");
    favouritePathsLabel.setFont(smallFont);
    favouritePanel.add(favouritePathsLabel);
    removeFavourite = new JCheckBox();
    removeFavourite.setToolTipText("When checked the selected favourite path is removed");

    favouritePathsCB = new JComboBox();
    favouritePathsCB.setFont(smallFont);
    favouritePathsCB.setToolTipText("Displays a list with the favourite paths and allows to browse  them in exploler");
    scalaExec.scalaLab.favouritePaths.loadFavouritePaths(GlobalValues.scalalabFavoritePathsFile, favouritePathsCB);   // add also the favourite paths from the file
    
    favouritePathsCB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (favouritePathsCB.getItemCount() >= 1)  { // items exist
               int emask = e.getModifiers();
            if (removeFavourite.isSelected()) {
      int selIdx = favouritePathsCB.getSelectedIndex();
      favouritePathsCB.removeItemAt(selIdx);
      GlobalValues.favouriteElements.removeElementAt(selIdx);
           }  else  {
                
               String  pathSelected = (String) favouritePathsCB.getSelectedItem();
              specifyPathsField.setText(pathSelected);
    try {
         GlobalValues.selectedExplorerPath = pathSelected;
         FileTreeExplorer  currentJPathTree = new FileTreeExplorer(pathSelected);
         TreePanel.removeAll();
         JScrollPane   scriptsPane = new JScrollPane(currentJPathTree.pathsTree);
         TreePanel.add(scriptsPane);
         TreePanel.revalidate();
    }
    catch (FileNotFoundException ex) {
        System.out.println("Error exploring "+pathSelected); 
        ex.printStackTrace();
            } 
        }
      }   // items exist
     
    }
    });
            
    if (GlobalValues.hostIsUnix) {
        if (GlobalValues.favouriteElements.contains("/")==false) {
           favouritePathsCB.addItem("/");
           GlobalValues.favouriteElements.add("/");
        }
    }
    else {
        if (GlobalValues.favouriteElements.contains("c:\\")==false)  {    
         favouritePathsCB.addItem("c:\\");
         GlobalValues.favouriteElements.add("c:\\");
     }
    }
     favouritePanel.add(favouritePathsCB);    
     ScriptsClassesPanel.add(favouritePanel);

     JPanel removePanel = new JPanel(new GridLayout(1,2));
    JLabel checkboxIsForRemoveLabel = new JLabel("Remove from Favourite Paths on click: ");
    checkboxIsForRemoveLabel.setFont(smallFont);
    checkboxIsForRemoveLabel.setToolTipText("When selected the path selected from the favourite paths list is removed");
    removePanel.add(checkboxIsForRemoveLabel);
    removePanel.add(checkboxIsForRemoveLabel);
    removePanel.add(removeFavourite);
    ScriptsClassesPanel.add(removePanel);
    
    JPanel clearButtonsPanel = new JPanel(new GridLayout(1,2));
    JButton clearFavouritePathsButton = new JButton("Clear Favourite Paths");
    clearFavouritePathsButton.setFont(smallFont);
    clearFavouritePathsButton.setToolTipText("Clears the favourite paths list");
    clearFavouritePathsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          favouritePathsCB.removeAllItems();
          GlobalValues.favouriteElements.removeAllElements();
            }
        });
    clearButtonsPanel.add(clearFavouritePathsButton);
    JButton clearScalaSciPathsButton = new JButton("Clear ScalaSci ClassPaths");
    clearScalaSciPathsButton.setFont(smallFont);
    clearScalaSciPathsButton.setToolTipText("Clears the favourite paths list");
    clearScalaSciPathsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
          GlobalValues.ScalaSciUserPaths.removeAllElements();
          GlobalValues.ScalaSciClassPathComponents.removeAllElements();
          ScalaScriptClassesCB.removeAllItems();
              }
        });
    clearButtonsPanel.add(clearScalaSciPathsButton);
    ScriptsClassesPanel.add(clearButtonsPanel);
            
    
    JSplitPane PathsExplorerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ScriptsClassesPanel, TreePanel);
    PathsExplorerPane.setDividerLocation(0.5);
    //JPanel pathsExplorerPanel = new JPanel(new GridLayout(1,2));
   // pathsExplorerPanel.add(ScriptsClassesPanel);
    //pathsExplorerPanel.add(TreePanel);
    add(PathsExplorerPane);    

    try {
         FileTreeExplorer  currentJPathTree = new FileTreeExplorer(GlobalValues.workingDir);
         GlobalValues.currentFileExplorer = currentJPathTree;
         TreePanel.removeAll();
         JScrollPane   scriptsPane = new JScrollPane(currentJPathTree.pathsTree);
         TreePanel.add(scriptsPane);
         TreePanel.revalidate();
    }
    catch (FileNotFoundException ex) {
        System.out.println("Error exploring "+GlobalValues.workingDir); 
        ex.printStackTrace();
      }
    }
    
     }  // buildScriptPathsTree
               
       

               