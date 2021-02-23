
package scalaExec.scalaLab;


import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;



public class ProcessMatlabCompatibilty  extends JFrame {
   private String JarName;

   public static final int DEFAULT_WIDTH = 800;
   public static final int DEFAULT_HEIGHT = 500;  

   private JComboBox fileCombo;
   private JTextArea fileText;
       
/**
   A frame with a text area to show the contents of a file inside
   a Jar archive, a combo box to select different files in the
   archive, and a menu to load a new archive.
*/

   public  ProcessMatlabCompatibilty(String jarFile)
   {
      setTitle("scalaSci Examples: To execute,  Copy & Paste in the Console window  ");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      setVisible(true);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
     
      // add the text area and combo box
      fileText = new JTextArea();
      fileText.setFont(GlobalValues.defaultTextFont);
      fileCombo = new JComboBox();
      fileCombo.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                loadJarFile((String) fileCombo.getSelectedItem());
            }
         });

      JPanel bottomPanel = new JPanel(new BorderLayout());
      JLabel examplesLabel = new JLabel();
      examplesLabel.setFont(new Font("Arial", Font.BOLD, 16));
      examplesLabel.setText("Matlab interfacing and .m file examples: ");
      bottomPanel.add(examplesLabel, BorderLayout.WEST);
      bottomPanel.add(fileCombo, BorderLayout.CENTER);
      add(bottomPanel, BorderLayout.SOUTH);
      add(new JScrollPane(fileText), BorderLayout.CENTER);
   
     JarName = jarFile;
     scanJarFile();
   }

   
   
   /**
      Scans the contents of the Jar archive and populates
      the combo box.
   */
   public void scanJarFile()
   {  
      fileCombo.removeAllItems();
      try
      {  
         JarInputStream zin = new JarInputStream(new FileInputStream(JarName));
         JarEntry entry;
         while ((entry = zin.getNextJarEntry()) != null)
         {  
            String nameOfEntry = entry.getName();
            if (nameOfEntry.endsWith(".mat-ssci")   || nameOfEntry.endsWith(".m"))
              fileCombo.addItem(nameOfEntry);
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }
   }

   /**
      Loads a file from the Jar archive into the text area
      @param name the name of the file in the archive
   */
   public void loadJarFile(String name)
   {  
      try
      {  
         JarInputStream zin = new JarInputStream(new FileInputStream(JarName));
         JarEntry entry;
         fileText.setText("");

         // find entry with matching name in archive
         while ((entry = zin.getNextJarEntry()) != null)
         {  
            if (entry.getName().equals(name))
            {  
               // read entry into text area
               BufferedReader in = new BufferedReader(new InputStreamReader(zin));
               String line;
               while ((line = in.readLine()) != null)
               {
                  fileText.append(line);
                  fileText.append("\n");
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
   }

/**
   This file filter matches all files with a given set of 
   extensions. From FileChooserTest in chapter 9
*/
class ExtensionFileFilter extends FileFilter
{
   /**
      Adds an extension that this file filter recognizes.
      @param extension a file extension (such as ".txt" or "txt")
   */
   public void addExtension(String extension)
   {
      if (!extension.startsWith("."))
         extension = "." + extension;
      extensions.add(extension.toLowerCase());     
   }

   /**
      Sets a description for the file set that this file filter
      recognizes.
      @param aDescription a description for the file set
   */
   public void setDescription(String aDescription)
   {
      description = aDescription;
   }

   /**
      Returns a description for the file set that this file
      filter recognizes.
      @return a description for the file set
   */
   public String getDescription()
   {
      return description; 
   }

   public boolean accept(File f)
   {
      if (f.isDirectory()) return true;
      String name = f.getName().toLowerCase();

      // check if the file name ends with any of the extensions
      for (String e : extensions)
         if (name.endsWith(e))
            return true;
      return false;
   }
   
   private String description = "";
   private ArrayList<String> extensions = new ArrayList<String>();
}
}
