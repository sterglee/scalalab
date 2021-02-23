package scalalabEdit;

import java.awt.BorderLayout;
import java.awt.Container;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import jsyntaxpane.DefaultSyntaxKit;
import scala.reflect.io.Directory;
import scalaExec.Interpreter.GlobalValues;

// a simple editor for ScalaLab, with support for syntax highlighting for Java/Scala files 
// but not with interaction with the Scala interpreter that has the programmer's ScalaLab editor

public class EditorPaneEdit  extends JFrame {
    JEditorPane  jep;
    static  JFrame currentFrame  = null;
    boolean editorTextSaved = false;
    public String editedFileName;   // the full pathname of the file being currently edited
    public static String titleStr = "Simple ScalaLab editor";

    static boolean documentEditsPendable = false;
    
    // edit the file with name selectedValue
    public EditorPaneEdit(String selectedValue) {
        currentFrame = this;    // keep the current frame handle
        editedFileName = selectedValue;    // keep the edited filename
        jep = new JEditorPane();   // construct a JEditorPane component
      
        DefaultSyntaxKit.initKit();
      
        EditorKeyMouseHandler  keyAndMouseHandler = new EditorKeyMouseHandler();
        jep.addKeyListener(keyAndMouseHandler);
        jep.addMouseListener(keyAndMouseHandler);

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
            if (editorTextSaved == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+editedFileName+" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
                dispose();
              }
       
            }
        });

    JMenu helpMenu = new JMenu("Help");
    helpMenu.setFont(GlobalValues.uifont);
    JMenuItem editorBasicCommandsMenuItem = new JMenuItem("Editor Basic Commands");
    editorBasicCommandsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame helpFrame = new JFrame("Help on editor");
                JTextArea helpArea = new JTextArea("   F6:  execute selected text  or current line. \n "+
" Mouse Double Click on an identifier: Displays its value \n"+
"  Press F2 to execute code up to cursor position  \n"+    
"  Press F10 on a selected identifier: Displays information for the class of the selected identifier \n"+
    " Select a keyword (e.g. \"fft\") and press F1 for obtaining help on the selected identifier using Java reflection  \n");

                helpFrame.add(new JScrollPane(helpArea));
                helpFrame.setSize(400, 300);
                helpFrame.setLocation(300, 300);
                helpFrame.setVisible(true);
            }
    });
        
        fileMenu.add(saveEditorTextJMenuItem);
        fileMenu.add(saveAsEditorTextJMenuItem);
        fileMenu.add(loadEditorTextJMenuItem);
        fileMenu.add(exitJMenuItem); 
        mainJMenuBar.add(fileMenu);
        helpMenu.add(editorBasicCommandsMenuItem);
        mainJMenuBar.add(helpMenu);
        setJMenuBar(mainJMenuBar);

        setTitle(titleStr+":  File: "+selectedValue);
        
// use user settings for edit frames to adjust location and size
        setLocation(Integer.parseInt(GlobalValues.editFrameLocX),  Integer.parseInt(GlobalValues.editFrameLocY));
        setSize(Integer.parseInt(GlobalValues.editFrameSizeX),  Integer.parseInt(GlobalValues.editFrameSizeY));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
        
        JScrollPane scrPane = new JScrollPane(jep);
    
        if (selectedValue.endsWith(".java"))
            jep.setContentType( "text/java");
        else if (selectedValue.endsWith(".scala"))
            jep.setContentType("text/scala");
        
     
        add(scrPane);
        
  // load the file      
                   FileReader fr = null;
            try {
                fr = new FileReader(selectedValue);
                jep.read(fr, null);
                
            } catch (FileNotFoundException ex) {
                System.out.println("file "+selectedValue+" not found");
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            finally {
                try {
                    fr.close();
   
// set the font of the displayed text to the same font as the main ScalaLab Interpreter Pane
        jep.setFont(new Font(scalaExec.Interpreter.GlobalValues.paneFontName, Font.PLAIN,  scalaExec.Interpreter.GlobalValues.paneFontSize));
      
                } 
                
                catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            
            }
        
             
        setVisible(true);
        EditorPaneEdit.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
    
 }
    
    
    // save the current file kept in editor
class saveEditorTextAction extends AbstractAction  {
    public saveEditorTextAction() { super("Save Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        String saveFileName = editedFileName;   // file name to save is the one currently edited
        if (saveFileName == null)  { // not file specified thus open a FileChooser in order the user to determine it
        javax.swing.JFileChooser chooser = new JFileChooser(new File(Directory.Current().get().path()));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 saveFileName = selectedFile.getAbsolutePath();
                 editedFileName = saveFileName;    // update the edited file
                 EditorPaneEdit.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
   
         }
        }
        
        File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jep.write(fw);
                        editorTextSaved = true;  //  not need to save anything yet
                        
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }

  // save the contents of the edit buffer to a file, asking the user to specify it 
class saveAsEditorTextAction extends AbstractAction  {
    public saveAsEditorTextAction() { super("Save As Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        javax.swing.JFileChooser chooser = new JFileChooser(new File(Directory.Current().get().path()));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String saveFileName = selectedFile.getAbsolutePath();
                 File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jep.write(fw);
                        editorTextSaved = true;  //  not need to save anything yet
                     
                        EditorPaneEdit.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
                       
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }
}



// load a new file for editing
class loadEditorTextAction extends AbstractAction  {
    public loadEditorTextAction() { super("Load Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
           int userOption = JOptionPane.CANCEL_OPTION;
            if (editorTextSaved == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+editedFileName +" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
         
        javax.swing.JFileChooser chooser = new JFileChooser(new File(Directory.Current().get().path()));
        
        int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String loadFileName = selectedFile.getAbsolutePath();
                       
                   FileReader fr = null;
            try {
                fr = new FileReader(loadFileName);
                jep.read(fr, null);
                
            } catch (FileNotFoundException ex) {
                System.out.println("file "+loadFileName+" not found");
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
            
            editedFileName = loadFileName;   // current file is the new loaded one
            editorTextSaved = true;  // a freshly loaded file doesn't require saving
            EditorPaneEdit.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
                           
     }
   }
 }
}
}











