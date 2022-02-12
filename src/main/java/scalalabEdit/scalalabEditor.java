package scalalabEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.scalaLab.StreamGobbler;



public class scalalabEditor extends JFrame   {
    public  scalainterpreter.EditorPane   jeditorPane;
    
    String editedFileName;
    static String editingFileInPane;
    static  JFrame currentFrame  = null;
    private String currentFileName;   // the full path name
    
    public scalalabEditor() {

        currentFrame = this;
    
         editedFileName = "Untitled";
         editingFileInPane = "Untitled";
        
        jeditorPane = new  scalainterpreter.EditorPane();
        jeditorPane.init();
    
        JMenuBar mainJMenuBar = new JMenuBar();
                        
        JMenu  fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setToolTipText("File editing operations");
        fileMenu.setFont(GlobalValues.uifont);
                
        JMenuItem  saveEditorTextJMenuItem = new JMenuItem("Save Editor Text ");
        saveEditorTextJMenuItem.addActionListener(new saveEditorTextAction());
        saveEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        JMenuItem  saveAsEditorTextJMenuItem = new JMenuItem("Save As Editor Text to File");
        saveAsEditorTextJMenuItem.addActionListener(new saveAsEditorTextAction());
        saveAsEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        JMenuItem   loadEditorTextJMenuItem = new JMenuItem("Load  File to Editor");
        loadEditorTextJMenuItem.addActionListener(new loadEditorTextAction());
        loadEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        loadEditorTextJMenuItem.setFont(GlobalValues.uifont);
        
        JMenuItem exitJMenuItem = new JMenuItem("Exit");
        exitJMenuItem.setFont(GlobalValues.uifont); 
        exitJMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            int userOption = JOptionPane.CANCEL_OPTION;
            if (jeditorPane.editorTextSaved() == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+scalalabEditor.editingFileInPane+" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
                
                // save size and location of  edit frame
                GlobalValues.editFrameLocX = String.valueOf( currentFrame.getLocation().x);
                GlobalValues.editFrameLocY = String.valueOf( currentFrame.getLocation().y);
                GlobalValues.editFrameSizeX = String.valueOf( currentFrame.getSize().width);
                GlobalValues.editFrameSizeY = String.valueOf( currentFrame.getSize().height);
                
                dispose();
              }
       
            }
        });

        
        fileMenu.add(saveEditorTextJMenuItem);
        fileMenu.add(saveAsEditorTextJMenuItem);
        fileMenu.add(loadEditorTextJMenuItem);
        fileMenu.add(exitJMenuItem); 
        mainJMenuBar.add(fileMenu);
        setJMenuBar(mainJMenuBar);

        setTitle("ScalaLab Editor, File: "+editedFileName);
        

        setLocation(Integer.parseInt(GlobalValues.editFrameLocX),  Integer.parseInt(GlobalValues.editFrameLocY));
        setSize(Integer.parseInt(GlobalValues.editFrameSizeX),  Integer.parseInt(GlobalValues.editFrameSizeY));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(jeditorPane);
        setVisible(true);
    }
   
    public   scalalabEditor(int fileType) {
       this();
        
    }
   
 public static void loadFile(String loadFileName,  scalainterpreter.EditorPane ep) {
                 File loadFile = new File(loadFileName);
        {
            FileReader fr = null;
            try {
                fr = new FileReader(loadFile);
                ep.getPane().read(fr, null);
                ep.updateDocument();
            } catch (FileNotFoundException ex) {
                System.out.println("file "+loadFileName+" not found");
                Logger.getLogger(scalalabEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+loadFileName);
                }
            finally {
                try {
                    fr.close();
                } 
                
                catch (IOException ex) {
                    System.out.println("cannot close file "+loadFileName);
                }
            
            }
        }
 }
 
 
 public static void updateTitle() {
       currentFrame.setTitle("ScalaLab Editor, File: "+scalalabEditor.editingFileInPane);
    }
    
class saveEditorTextAction extends AbstractAction  {
    public saveEditorTextAction() { super("Save Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        String saveFileName = scalalabEditor.editingFileInPane;
        if (saveFileName == null)  { // not file specified thus open a FileChooser it
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 saveFileName = selectedFile.getAbsolutePath();
                 scalalabEditor.editingFileInPane = saveFileName;
                 scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                 
                 updateTitle();
         }
        }
        
        File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jeditorPane.getPane().write(fw);
                        GlobalValues.editingFileInPane = saveFileName;
                        GlobalValues.editorTextSaved = true;
                        
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }



class saveAsEditorTextAction extends AbstractAction  {
    public saveAsEditorTextAction() { super("Save As Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String saveFileName = selectedFile.getAbsolutePath();
                 File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jeditorPane.getPane().write(fw);
                        scalalabEditor.editingFileInPane = saveFileName;
                        GlobalValues.editorTextSaved = true;
                       
                        scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                        
                        updateTitle();
                       
                       // test whether we should update the ScalaPane's recent file information
                       if (GlobalValues.scalalabMainFrame.recentPaneFiles.contains(saveFileName) ==  false)  {
                           GlobalValues.scalalabMainFrame.recentPaneFiles.add(saveFileName);
                           GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
                       }
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }
}


class loadEditorTextAction extends AbstractAction  {
    public loadEditorTextAction() { super("Load Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
           int userOption = JOptionPane.CANCEL_OPTION;
            if (jeditorPane.editorTextSaved() == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+scalalabEditor.editingFileInPane+" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
         
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String loadFileName = selectedFile.getAbsolutePath();
                    try {
                        scalalabEditor.loadFile(loadFileName, jeditorPane);
                       
                       scalalabEditor.editingFileInPane = loadFileName;
                       GlobalValues.scalalabMainFrame.recentPaneFiles.add(loadFileName);
                       if (GlobalValues.scalalabMainFrame.recentPaneFiles.contains(loadFileName) ==  false)  {
                           GlobalValues.scalalabMainFrame.recentPaneFiles.add(loadFileName);
                           GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
                       }
                       
                       scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                       
                       updateTitle();
                    }   
                    catch (Exception ex) {
                        System.out.println("Exception reading editor's text "+ex.getMessage());
                    }
                           
      }
   }
 }
}




    
}    
    

    
    

    
    