
package scalaExec.gui;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import scalaExec.Interpreter.GlobalValues;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;


public class watchExamples  {
    private JTree  examplesTree;  // the JTree that organizes loaded class information
    static int  watchXSize,  watchYSize;  // the size of the watch Frame
    static public int  watchXLoc = 100,  watchYLoc = 100;   // the location of the watch frame
    static TreeSet  examplesFound = new TreeSet();
    static JTree loadedExamplesTree = null;
    static String selectedExample;
    
    public RSyntaxTextArea  exampleArea;
    public RTextScrollPane exampleScrollPane;            
            

    public watchExamples()
    	{
        Dimension screenSiz = Toolkit.getDefaultToolkit().getScreenSize();
        watchXSize = (int)(0.4*(double)screenSiz.width);
        watchYSize =(int)(0.4*(double)screenSiz.height);
    }


     /**
      Scans the contents of the Jar archive and populates
      the combo box.
   */
   public  void scanMainJarFile()
   {  
       Dimension screenSiz = Toolkit.getDefaultToolkit().getScreenSize();
        watchXSize = (int)(0.4*(double)screenSiz.width);
        watchYSize =(int)(0.4*(double)screenSiz.height);
       String JarName = GlobalValues.jarFilePath;
      examplesFound.clear();
      try
      {  
         JarInputStream zin = new JarInputStream(new FileInputStream(JarName));
         JarEntry entry;
         while ((entry = zin.getNextJarEntry()) != null)
         {  
            String nameOfEntry = entry.getName();
            if (nameOfEntry.endsWith(".sssci") || nameOfEntry.endsWith(".plots-ssci") )
              examplesFound.add(nameOfEntry);
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }
   }

   public  void scanMainJarFile(String pattern)
   {  
       Dimension screenSiz = Toolkit.getDefaultToolkit().getScreenSize();
        watchXSize = (int)(0.4*(double)screenSiz.width);
        watchYSize =(int)(0.4*(double)screenSiz.height);
       String JarName = GlobalValues.jarFilePath;
      examplesFound.clear();
      try
      {  
         JarInputStream zin = new JarInputStream(new FileInputStream(JarName));
         JarEntry entry;
         while ((entry = zin.getNextJarEntry()) != null)
         {  
            String nameOfEntry = entry.getName();
            if (nameOfEntry.endsWith(pattern) )
              examplesFound.add(nameOfEntry);
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }
   }

   
  // display the classes of the toolbox with name toolboxName using a JTree component. The bytecodes of the classes are kept in the Vector classes.
  public   void displayExamples(String rootLabel) {
        
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootLabel);
    
        Iterator  iter = examplesFound.iterator();
        while(iter.hasNext()) {
    Object next = (Object)iter.next();
    String exampleName = (String)next;
                     {
                    DefaultMutableTreeNode nCurrentClass = new DefaultMutableTreeNode(exampleName);
                    root.add(nCurrentClass);
                    }
	}
    
    loadedExamplesTree = new JTree(root);  // the tree of global system variables
    // single selection
    int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
    
    
    loadedExamplesTree.addTreeSelectionListener(new TreeSelectionListener() {
    	public void valueChanged(TreeSelectionEvent event) {
    		TreePath path = loadedExamplesTree.getSelectionPath();
    		if (path==null) return;  // not any tree's node selected
    		DefaultMutableTreeNode selectedNode =
                 (DefaultMutableTreeNode) path.getLastPathComponent();
    		String selectedValue;
    	
//  variable values are at depth 0, variable names at depth 1    		
    		
                selectedValue = (String) selectedNode.getUserObject();
                
                selectedExample = selectedValue;
                
                loadJarFile(selectedValue);
                      
                
        }
    });  // addTreeSelectionListener
 
 


    loadedExamplesTree.getSelectionModel().setSelectionMode(mode);
       
    JScrollPane  treePane = new JScrollPane(loadedExamplesTree);
    JFrame watchFrame = new JFrame("ScalaSci Examples");
    
    watchFrame.add(treePane);
    watchFrame.setSize( watchXSize, watchYSize);
    watchFrame.setLocation(watchXLoc, watchYLoc);
    
    watchFrame.setVisible(true);
    
      }
 
  
 public void loadJarFile(String name)
   {  
       String jarName = GlobalValues.jarFilePath;
         
      try
      {  
         JarEntry entry;

        exampleArea = new RSyntaxTextArea();
        exampleArea.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize));
      
        exampleArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
        exampleArea.setCodeFoldingEnabled(true);
 
        exampleArea.setText("");

         JarInputStream zin = new JarInputStream(new FileInputStream(jarName));
         
         while ((entry = zin.getNextJarEntry()) != null)
         {  
             
            if (entry.getName().equals(name))
            {  
         
               // read entry into text area
               BufferedReader in = new BufferedReader(new InputStreamReader(zin));
               String line;
               while ((line = in.readLine()) != null)
               {
                  exampleArea.append(line);
                  exampleArea.append("\n");
               }
            }
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }

      exampleScrollPane = new RTextScrollPane(exampleArea);
      JFrame exampleFrame = new JFrame(selectedExample);
      exampleFrame.add(exampleScrollPane);
      exampleFrame.pack();
      exampleFrame.setVisible(true);
   }
    

    }

