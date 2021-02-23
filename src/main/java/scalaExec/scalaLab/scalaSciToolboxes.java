package scalaExec.scalaLab;

import java.awt.Font;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.GBC;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


// a class to handle ScalaSci toolboxes
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import scalaExec.gui.scalaSciScriptsPathsTree;


// this class updates the system in order to consider the additional .jar toolboxes placed at the "Available" toolboxes list
    
public class scalaSciToolboxes {

        public   static JList  scalaSciToolboxesAvailableList;    // the available toolboxes for ScalaSci as full .jar pathnames
        public   static JList  scalaSciToolboxesLoadedList;   // the list of already loaded toolboxes as full .jar pathnames
        public   static DefaultListModel  scalaSciToolboxesAvailableListModel;   // the ListModel that keeps the data displayed at the scalaSciToolboxesAvailable list
        public   static DefaultListModel  scalaSciToolboxesLoadedListModel; // the ListModel that keeps the data displayed at the scalaSciToolboxesAvailable list
        public   static JPanel scalaSciToolboxesPanel = new JPanel(new GridBagLayout());
        // keeps information on all loaded toolboxes. Each entry keeps the toolbox name as the full pathname of the .jar file of the toolbox and
        // a Vector that keeps the loaded classes of the toolbox
        public   static Vector <scalaSciToolbox>   ssciToolboxes = new Vector();

        public   static HashMap  <String, JFrame> framesOfToolboxClasses  = new HashMap();   // keeps track of the toolboxes frames in order to be able to remove them
        public   static int selectedAvailableToolboxIndex;   // the selected toolbox index by the user from the list of the available toolboxes
        public   static int selectedLoadedToolboxIndex;   // the selected toolbox index by the user from the list of the loaded toolboxes
        
        public static scalaSciScriptsPathsTree paramTree;
        
   // handles the main tab panel for ScalaSci's toolboxes
   public static  JPanel  handleScalaSciTab()  {
       if (scalaSciToolboxesAvailableListModel == null)  {
          scalaSciToolboxesAvailableListModel = new DefaultListModel();   // the list of .jar files that will be available for ScalaSci toolboxes
                    // put the available toolboxes in a list
          scalaSciToolboxesAvailableList = new JList(scalaSciToolboxesAvailableListModel);
          int toolboxForLoadCnt = GlobalValues.ScalaSciToolboxes.size();   // the number of toolboxes available for loading
          if (toolboxForLoadCnt > 0)
              for (int k=0; k<toolboxForLoadCnt; k++)
                     scalaSciToolboxesAvailableListModel.addElement(GlobalValues.ScalaSciToolboxes.elementAt(k));
                  
          //scalaSciToolboxesAvailableList.addMouseListener(new MouseAdapterForSSIAvailableToolboxes());
          scalaSciToolboxesAvailableList.addListSelectionListener(new ListSelectionListener() {
    //    this listener tracks the toolbox selected by the user from the list
            @Override
            public void valueChanged(ListSelectionEvent event)
            {
               selectedAvailableToolboxIndex  = scalaSciToolboxesAvailableList.getSelectedIndex();
            }
        });
       }

       if (scalaSciToolboxesLoadedListModel == null)   {    // the list of .jar files that are already loaded as ScalaSci toolboxes
           scalaSciToolboxesLoadedListModel = new DefaultListModel(); 
           scalaSciToolboxesLoadedList = new JList(scalaSciToolboxesLoadedListModel);
           //scalaSciToolboxesLoadedList.addMouseListener(new MouseAdapterForSSILoadedToolboxes());
          scalaSciToolboxesLoadedList.addListSelectionListener(new ListSelectionListener() {
    //    this listener tracks the toolbox selected by the user from the list
            @Override
            public void valueChanged(ListSelectionEvent event)
            {  
               selectedLoadedToolboxIndex = scalaSciToolboxesLoadedList.getSelectedIndex();
            }
        });
       }
       
        //scalaSciToolboxesLoadedList
        JScrollPane availableToolboxesScrollPane = new JScrollPane(scalaSciToolboxesAvailableList);
        JScrollPane loadedToolboxesScrollPane = new JScrollPane(scalaSciToolboxesLoadedList);
        
          // the help panel displays the steps that the user has to follow in order to add toolboxes
        JTextArea helpArea = new JTextArea();
        helpArea.setFont(new Font("Arial", Font.BOLD, 11));
        helpArea.append("Help on basic toolbox operations: \n"+
                "\n  Add new toolboxes: \n\n"+
                "METHOD 1:   \n "+
                "Place them within the \"DefaultToolboxes\" folder in order to be available at the next session \n"+
                "\nMETHOD 2 \n"+
                "Step 1. Unzip the .jar file of the toolbox \n"+
                "Step 2. Update the ScalaClassPath to include the root directory where the .jar file was extracted \n "+
                "\n\n METHOD 3: \n\n"+
                " Import the specified toolboxes in the 'Available Toolboxes' list with the 'Import toolboxes' button \n"+
                " Toolboxes can be removed from the 'Available Toolboxes' list with a right-mouse click\n\n"+
                "An installed toolbox during one Scalalab session, remains on the ScalaClassPath, \n"+
                "e.g. having installed weka.jar, weka.jar remains on the ClassPath for next sessions\n\n"+
                "\n  Remove toolboxes: \n"+
                "Use `Clear ScalaSci ClassPaths` and then reload your favourite toolboxes\n");

         paramTree = new scalaSciScriptsPathsTree();
         paramTree.buildVariablesTree();
                       
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new GridLayout(1, 2));
        helpPanel.add(new JScrollPane(helpArea));
        helpPanel.add(paramTree); 

        // the buttons for specifying and importing toolboxes
        JPanel buttonsPanel = new JPanel();   // keeps the control buttons together
        
        JButton loadBtnNewInterpreter = new JButton("Import toolboxes creating new Interpreter");
        loadBtnNewInterpreter.setToolTipText("Creates a new Scala Interpreter that has the toolboxes in its classpath");
        loadBtnNewInterpreter.addActionListener(new updateSystemWithJarToolboxesNewInterpreter());
        
        JButton loadBtnNewEJMLInterpreter = new JButton("Import toolboxes creating new EJML Interpreter");
        loadBtnNewEJMLInterpreter.setToolTipText("Creates a new Scala Interpreter based on EJML library that has the toolboxes in its classpath");
        loadBtnNewEJMLInterpreter.addActionListener(new updateSystemWithJarToolboxesNewEJMLInterpreter());
        
        //JButton loadBtnSameInterpreter = new JButton("Update current classpath only");
        //loadBtnSameInterpreter.setToolTipText("Updates the current classpath. That classpath is used to create new interpreters.");
        //loadBtnSameInterpreter.addActionListener(new updateSystemWithJarToolboxesCurrentInterpreter());
        
               
         JButton addBtn = new JButton("Specify toolboxes");
        addBtn.setToolTipText("Specify additional toolboxes for Scala");
         
        
        buttonsPanel.add(addBtn);
        buttonsPanel.add(loadBtnNewInterpreter);
        buttonsPanel.add(loadBtnNewEJMLInterpreter);
//        buttonsPanel.add(loadBtnSameInterpreter);
        
        addBtn.addActionListener(new  ActionListener() {  // adds a toolbox

            public void actionPerformed(ActionEvent e) {
               JFileChooser  chooser = new JFileChooser(GlobalValues.scalalabLibPath);
               chooser.setMultiSelectionEnabled(true);
               chooser.setDialogTitle("Specify JAR file containing Java classes for ScalaSci");
               int retVal = chooser.showOpenDialog(null);
               if (retVal == JFileChooser.APPROVE_OPTION) {   // approve toolbox
                    File []selectedFiles = chooser.getSelectedFiles();
                    for (int k = 0; k<selectedFiles.length; k++) {  // for all selected files
                    String jarFileName = selectedFiles[k].getAbsolutePath();
    if (jarFileName.endsWith(".jar") )  
        {  // if toolbox is not already specified insert it to the list of available toolboxes
           boolean toolboxFound = false;
           int loadedToolboxesCnt = scalaSciToolboxesLoadedListModel.size();
           for (int toolboxIdx=0; toolboxIdx<loadedToolboxesCnt; toolboxIdx++)  {  // search for the selected toolbox
               String currentToolboxName = (String) scalaSciToolboxesLoadedListModel.get(toolboxIdx);   // the name of the current toolbox
               if (currentToolboxName.equalsIgnoreCase(jarFileName) == true)   {  // toolbox found
                   toolboxFound = true;
                   break;
                 }
           }  // search for the selected toolbox
           
           if (toolboxFound == false)  {  // selected toolbox not already exists, thus insert it
        scalaSciToolboxesAvailableListModel.addElement(jarFileName);      // update the list model that keeps the jar files that are used as toolboxes

        GlobalValues.scalaJarClassesLoaded = false;  // reload classes
                          }
                   }
                 }  // for all selected files
              }  // approve toolbox
            }  // ActionPerformed
        });
                     
            scalaSciToolboxesPanel.add(helpPanel, new GBC(0, 0, 6, 1 ));
            scalaSciToolboxesPanel.add(buttonsPanel, new GBC(0, 1, 6, 1));
            scalaSciToolboxesPanel.add(new JLabel("Available ScalaSci Toolboxes for Loading: "), new GBC(0, 2, 1, 1));
            scalaSciToolboxesPanel.add(availableToolboxesScrollPane, new GBC(1, 2, 2, 1 ));
            scalaSciToolboxesPanel.add(new JLabel("  Currently Loaded ScalaSci Toolboxes:  "), new GBC(3, 2, 1, 1));
            scalaSciToolboxesPanel.add(loadedToolboxesScrollPane, new GBC(4, 2, 2, 1));
          
  return scalaSciToolboxesPanel;
  
                    
     }
}
   




    class updateSystemWithJarToolboxesNewInterpreter extends AbstractAction {
             updateSystemWithJarToolboxesNewInterpreter() { super("Update system with jar toolboxes creating a new Interpreter "); }

      // performs the operations needed in order the classes of the toolboxes to become available at the ScalaSci scripting engine
           public void actionPerformed(ActionEvent e) {
             
                       if (GlobalValues.scalaJarClassesLoaded ==  false) {   // the classes of the toolboxes for Scala were not loaded
       
               // for all the elements of the available toolboxes for ScalaSci load the corresponding toolbox
           int numToolboxes = scalaSciToolboxes.scalaSciToolboxesAvailableListModel.size();
           for (int k=0; k<numToolboxes; k++)    {  // for all toolboxes for ScalaSci
               int numClassesOfToolbox=0;
               String  forScalaToolboxJarName = (String) scalaSciToolboxes.scalaSciToolboxesAvailableListModel.getElementAt(k);  // full path name of the toolbox JAR file
               GlobalValues.currentToolboxName = forScalaToolboxJarName;   // keep the currently loaded toolbox globally
                
                 // update structures for ScalaSciClassPath
       if (GlobalValues.ScalaSciClassPathComponents.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciClassPath
          GlobalValues.ScalaSciClassPathComponents.add(forScalaToolboxJarName);  // append to the ScalaSciClassPathComponents
            
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
 
           
      // update structures for ScalaSciToolboxes
      if (GlobalValues.ScalaSciToolboxes.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciUserPaths
          GlobalValues.ScalaSciToolboxes.add(forScalaToolboxJarName);  // append to the ScalaSciUserPaths components for retrieving Scala classes
            
          // rebuild the ScalaSci Toolboxes property (i.e. "ScalaSciToolboxesProp") in order to account for the new path
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciToolboxes.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
      GlobalValues.settings.setProperty("ScalaSciToolboxesProp", fileStr.toString());    // keep in property in order to be saved
      }  // path not already exist in ScalaSciUserPaths

           }  // for all toolboxes  
     
           // move the .jar toolboxes strings from scalaSciToolboxesAvailableList to scalaSciToolboxesLoadedList
           Enumeration  availableToolboxes =  scalaSciToolboxes.scalaSciToolboxesAvailableListModel.elements();
                 // copy the toolboxes to the loaded list
           while (availableToolboxes.hasMoreElements())   {
               String currentToolbox = (String)availableToolboxes.nextElement();
               scalaSciToolboxes.scalaSciToolboxesLoadedListModel.addElement(currentToolbox);
           }
           scalaSciToolboxes.scalaSciToolboxesAvailableListModel.removeAllElements();   // remove the toolboxes list from the available list

          // create a new interpreter with the appended classpath
       GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterFast();

       GlobalValues.scalaJarClassesLoaded = true;    // sets this flag in order to avoid reloading the toolbox list unless there is a change
            
       scalaSciToolboxes.paramTree.removeAll();
       scalaSciToolboxes.paramTree.buildVariablesTree();
       
       GlobalValues.scalalabMainFrame.setSize(GlobalValues.scalalabMainFrame.getSize().width, GlobalValues.scalalabMainFrame.getSize().height);
                       }
                       
                
         
        }
    }
               
       

    

    class updateSystemWithJarToolboxesNewEJMLInterpreter extends AbstractAction {
             updateSystemWithJarToolboxesNewEJMLInterpreter() { super("Update system with jar toolboxes creating a new Interpreter based on EJML library "); }

      // performs the operations needed in order the classes of the toolboxes to become available at the ScalaSci scripting engine
           public void actionPerformed(ActionEvent e) {
             
               if (GlobalValues.scalaJarClassesLoaded ==  false) {   // the classes of the toolboxes for Scala were not loaded
           // for all the elements of the available toolboxes for ScalaSci load the corresponding toolbox
           int numToolboxes = scalaSciToolboxes.scalaSciToolboxesAvailableListModel.size();
           for (int k=0; k<numToolboxes; k++)    {  // for all toolboxes for ScalaSci
               int numClassesOfToolbox=0;
               String  forScalaToolboxJarName = (String) scalaSciToolboxes.scalaSciToolboxesAvailableListModel.getElementAt(k);  // full path name of the toolbox JAR file
               GlobalValues.currentToolboxName = forScalaToolboxJarName;   // keep the currently loaded toolbox globally
                
                 // update structures for ScalaSciClassPath
       if (GlobalValues.ScalaSciClassPathComponents.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciClassPath
          GlobalValues.ScalaSciClassPathComponents.add(forScalaToolboxJarName);  // append to the ScalaSciClassPathComponents
            
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
 
           
      // update structures for ScalaSciToolboxes
      if (GlobalValues.ScalaSciToolboxes.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciUserPaths
          GlobalValues.ScalaSciToolboxes.add(forScalaToolboxJarName);  // append to the ScalaSciUserPaths components for retrieving Scala classes
            
          // rebuild the ScalaSci Toolboxes property (i.e. "ScalaSciToolboxesProp") in order to account for the new path
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciToolboxes.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
      GlobalValues.settings.setProperty("ScalaSciToolboxesProp", fileStr.toString());    // keep in property in order to be saved
      }  // path not already exist in ScalaSciUserPaths

           }  // for all toolboxes  
     
           // move the .jar toolboxes strings from scalaSciToolboxesAvailableList to scalaSciToolboxesLoadedList
           Enumeration  availableToolboxes =  scalaSciToolboxes.scalaSciToolboxesAvailableListModel.elements();
                 // copy the toolboxes to the loaded list
           while (availableToolboxes.hasMoreElements())   {
               String currentToolbox = (String)availableToolboxes.nextElement();
               scalaSciToolboxes.scalaSciToolboxesLoadedListModel.addElement(currentToolbox);
           }
           scalaSciToolboxes.scalaSciToolboxesAvailableListModel.removeAllElements();   // remove the toolboxes list from the available list

          // create a new interpreter with the appended classpath
       GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetFastEJML();

       GlobalValues.scalaJarClassesLoaded = true;    // sets this flag in order to avoid reloading the toolbox list unless there is a change
       scalaSciToolboxes.paramTree.removeAll();
       scalaSciToolboxes.paramTree.buildVariablesTree();
       scalaSciToolboxes.scalaSciToolboxesPanel.repaint();
       
          }
               
       }
           
    }

    class updateSystemWithJarToolboxesCurrentInterpreter extends AbstractAction {
             updateSystemWithJarToolboxesCurrentInterpreter() { 
                  super("Append the toolboxes to the classpath of the current Interpreter ");
             } 
    

      // performs the operations needed in order the classes of the toolboxes to become available at the ScalaSci scripting engine
           public void actionPerformed(ActionEvent e) {
             
               if (GlobalValues.scalaJarClassesLoaded ==  false) {   // the classes of the toolboxes for Scala were not loaded
           
           // for all the elements of the available toolboxes for ScalaSci load the corresponding toolbox
           int numToolboxes = scalaSciToolboxes.scalaSciToolboxesAvailableListModel.size();
           for (int k=0; k<numToolboxes; k++)    {  // for all toolboxes for ScalaSci
               int numClassesOfToolbox=0;
               String  forScalaToolboxJarName = (String) scalaSciToolboxes.scalaSciToolboxesAvailableListModel.getElementAt(k);  // full path name of the toolbox JAR file
               GlobalValues.currentToolboxName = forScalaToolboxJarName;   // keep the currently loaded toolbox globally
                
                 // update structures for ScalaSciClassPath
       if (GlobalValues.ScalaSciClassPathComponents.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciClassPath
          GlobalValues.ScalaSciClassPathComponents.add(forScalaToolboxJarName);  // append to the ScalaSciClassPathComponents
            
           GlobalValues.globalInterpreter.settings().classpath().append(forScalaToolboxJarName);    // append the path to the current Scala Interpreter
          // ??? why classpath is not updated dynamically
          
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
 
           
      // update structures for ScalaSciToolboxes
      if (GlobalValues.ScalaSciToolboxes.contains(forScalaToolboxJarName)==false)        
               {    // path not already exist in ScalaSciUserPaths
          GlobalValues.ScalaSciToolboxes.add(forScalaToolboxJarName);  // append to the ScalaSciUserPaths components for retrieving Scala classes
            
          // rebuild the ScalaSci Toolboxes property (i.e. "ScalaSciToolboxesProp") in order to account for the new path
      StringBuilder fileStr = new StringBuilder();
      Enumeration enumDirs = GlobalValues.ScalaSciToolboxes.elements();
      while (enumDirs.hasMoreElements())  {
         Object ce = enumDirs.nextElement();
         fileStr.append(File.pathSeparator+(String)ce);
    }
      GlobalValues.settings.setProperty("ScalaSciToolboxesProp", fileStr.toString());    // keep in property in order to be saved
      }  // path not already exist in ScalaSciUserPaths

           }  // for all toolboxes  
     
           // move the .jar toolboxes strings from scalaSciToolboxesAvailableList to scalaSciToolboxesLoadedList
           Enumeration  availableToolboxes =  scalaSciToolboxes.scalaSciToolboxesAvailableListModel.elements();
                 // copy the toolboxes to the loaded list
           while (availableToolboxes.hasMoreElements())   {
               String currentToolbox = (String)availableToolboxes.nextElement();
               scalaSciToolboxes.scalaSciToolboxesLoadedListModel.addElement(currentToolbox);
           }
           scalaSciToolboxes.scalaSciToolboxesAvailableListModel.removeAllElements();   // remove the toolboxes list from the available list
          
    GlobalValues.scalalabMainFrame.constructPathPresentationPanel();   // update the panel with the new paths
    GlobalValues.scalaJarClassesLoaded = true;    // sets this flag in order to avoid reloading the toolbox list unless there is a change
          }
               
       

     }
    }

    
    

       
