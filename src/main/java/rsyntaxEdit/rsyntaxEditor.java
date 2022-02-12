package rsyntaxEdit;


import scalainterpreter.executeRightClickAction;
import javax.script.ScriptException;
import scalaExec.Interpreter.GlobalValues;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;
import java.util.concurrent.Executors;
import javax.help.plaf.basic.BasicFavoritesNavigatorUI;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import scalaExec.scalaLab.EditorPaneHTMLHelp;
import scalalab.JavaGlobals;

public class rsyntaxEditor   {
    // the JFrame where the editor resides
        public  JFrame currentFrame  = null;
    
        JMenuBar mainJMenuBar;
                        
        JMenu  fileMenu;
        JMenuItem  saveEditorTextJMenuItem;
        JMenuItem  saveAsEditorTextJMenuItem;
        JMenuItem   loadEditorTextJMenuItem;
        JMenuItem exitJMenuItem;
        JMenu recentPaneFilesMenu = new JMenu("Recent Files");  // created dynamically to keep the recent files list
        JMenu controlTasks = new JMenu("CancelPendingTasks");
        
        JMenuItem cancelJMenuItem = new JMenuItem("Cancel Taks");
        JMenuItem clearRecentFilesJMenuItem;    
        JMenuItem  recentFileMenuItem;
        JMenu helpMenu;

        private JTextField  searchField;
        private JCheckBox regexCB;
        private JCheckBox matchCaseCB;
        private boolean forward=true;
        private JButton gotoLineButton;
        private JTextField gotoLineField;
        
    static boolean documentEditsPendable;
    public boolean editorTextSaved = false;
    public String editedFileName;   // the full pathname of the file being currently edited
    public static String titleStr = "ScalaLab programmer's editor based on rsyntaxarea";

    public  Vector<String>  recentPaneFiles = new Vector<String>();  // keeps the full names of the recent files
    public  String  fileWithFileListOfPaneRecentFiles = "recentsPaneFile.txt"; // the list of the recent editor's pane files

    public RTextScrollPane  scrPane;
    org.fife.ui.rsyntaxtextarea.RSyntaxTextArea  jep;
    

    public void jsyntaxEdit(String selectedValue) {
                       
      
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

            editedFileName = selectedValue;   // current file is the new loaded one
            editorTextSaved = false;  // a freshly loaded file doesn't require saving
            this.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
           
    }
    
    
  
    public void saveRecentPaneFiles() {  // the file that keeps the recent files list is kept in GlobalValues.scalaLabRecentFilesList
                                                                  // at the same directory as the scalaLab.jar executable, i.e. GlobalValues.jarFilePath
         //create streams
         try {
    // open the file for writing the recent files         
            FileOutputStream output = new FileOutputStream(fileWithFileListOfPaneRecentFiles);  

            //create writer stream
           OutputStreamWriter  recentsWriter= new OutputStreamWriter(output);
            int  fileCnt=0;  // restrict the maximum number of recent files

           for (int k=0; k<recentPaneFiles.size(); k++) {
                String currentRecentFile = (String)recentPaneFiles.elementAt(k)+"\n";
                recentsWriter.write(currentRecentFile, 0, currentRecentFile.length());
                if (fileCnt++ == GlobalValues.maxNumberOfRecentFiles)  break;
            }
            recentsWriter.close();
            output.close();
    }
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in saveRecentFiles");
            System.out.println(except.getMessage());
            except.printStackTrace();
        }
    }

    // update the recent files menu with the items taken from recentFiles
    public void updateRecentPaneFilesMenu()
    {
           recentPaneFilesMenu.removeAll();  // clear previous menu items
           recentPaneFilesMenu.setFont(GlobalValues.uifont);
           clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
           clearRecentFilesJMenuItem.setFont(GlobalValues.uifont);
           clearRecentFilesJMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                recentPaneFiles.clear();
                recentPaneFilesMenu.removeAll();
            }
        });

           recentPaneFilesMenu.add(clearRecentFilesJMenuItem);
           
           int numberRecentFiles = recentPaneFiles.size();
        for (int k=numberRecentFiles-1; k>=0; k--)  {     // reverse order for displaying the most recently loaded first
            final String  recentFileName = (String)recentPaneFiles.elementAt(k);   // take the recent filename
            recentFileMenuItem = new JMenuItem(recentFileName);
            recentFileMenuItem.setFont(GlobalValues.uifont);
            recentFileMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
             jsyntaxEdit(recentFileName);   // reload the recent file in editor
             
            // update the workingDir
            String pathOfLoadFileName = recentFileName.substring(0, recentFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
                }
            });
            recentPaneFilesMenu.add(recentFileMenuItem);    // add the menu item corresponding to the recent file
        }  // for all the recently accessed files 

            recentPaneFilesMenu.setToolTipText("Tracks \"Saved As\" Files");
            mainJMenuBar.add(recentPaneFilesMenu);   // finally add the recent files menu to the main menu bar
        
       }
    
  // load the recent files list from the disk updating also the menu
    public  void loadRecentPaneFiles() {
         // create streams
       
        boolean exists = (new File(fileWithFileListOfPaneRecentFiles)).exists();
    
        if (exists) {
    
            try {
                // open the file containing the stored list of recent files
          FileInputStream input = new FileInputStream(fileWithFileListOfPaneRecentFiles);
             
             //create reader stream
           BufferedReader  recentsReader= new BufferedReader(new InputStreamReader(input));

          recentPaneFiles.clear();    // clear the Vector of recent files
          String currentLine;     // refill it from disk
          while ((currentLine = recentsReader.readLine()) != null)   // read all lines from the file with "recents" opened files
              if (recentPaneFiles.indexOf(currentLine) == -1)    // file not already in list
                recentPaneFiles.add(currentLine);

            recentsReader.close();
            input.close();
            updateRecentPaneFilesMenu();   // update the recent files menu

         }
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in readRecentsFiles. File: "+fileWithFileListOfPaneRecentFiles+"  not found");
            recentPaneFilesMenu.removeAll();  // clear previous menu items
           clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
           clearRecentFilesJMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                recentPaneFiles.clear();
                recentPaneFilesMenu.removeAll();
            }
        });

           recentPaneFilesMenu.add(clearRecentFilesJMenuItem);
            mainJMenuBar.add(recentPaneFilesMenu);   // finally add the recent files menu to the main menu bar
        
        }
     }
    }
    
         
    // perform common editing operations for the file selectedValue.
    // if the isMainFrame flag is true then the file will be edited in the main editor window, 
    // that has attached at its bottom the Console's output, therefore some different chores 
    // should be performed
    public RSyntaxTextArea  commonEditingActions(String selectedValue, boolean isMainFrame) {
        currentFrame = new JFrame("Editing "+selectedValue);    // keep the current frame handle
        GlobalValues.globalRSyntaxFrame  = currentFrame;  // keep a reference to the JFrame where the rsyntax editor resides
        editedFileName = selectedValue;    // keep the edited filename
        jep = new  RSyntaxTextArea();   // construct a JEditorPane component 
        
        jep.setToolTipText("Type here ScalaLab code. Use the corresponding Help menu for help on the available keystrokes");
        
        jep.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize));
      
        jep.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
        jep.setCodeFoldingEnabled(true);
        
        // create a toolbar with searching options
        JToolBar toolBar = new JToolBar();
        
        gotoLineButton = new JButton("Go To Line: ");
        gotoLineButton.setToolTipText("Positions the cursor to the line entered at the corresponding field ");
        toolBar.add(gotoLineButton);
        gotoLineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
        int lineNo = Integer.parseInt(gotoLineField.getText());
        int apos;
                try {
        apos = GlobalValues.globalRSyntaxEditorPane.getLineStartOffset(lineNo-1);
        GlobalValues.globalRSyntaxEditorPane.setCaretPosition(apos);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
        
            }
        });
        
        gotoLineField = new JTextField();
        toolBar.add(gotoLineField);
        
        
        searchField = new JTextField(30);
        toolBar.add(searchField);
        final JButton nextButton = new JButton("Find Next");
        nextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          forward  = true;
          performSearch(forward);
            }
        });
        toolBar.add(nextButton);
        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
         nextButton.doClick();
            }
        });
        toolBar.add(searchField);
        
        JButton prevButton = new JButton("Find Previous");
        prevButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          forward = false;
          performSearch(forward);                 
            }
        });
        toolBar.add(prevButton);

        regexCB = new JCheckBox("Regex");
        toolBar.add(regexCB);
        
        
        matchCaseCB = new JCheckBox("Match Case");
        toolBar.add(matchCaseCB);
        
        
        // add the key and mouse handlers

        // add the key and mouse handlers
        EditorKeyMouseHandler  ekmhandler = new EditorKeyMouseHandler();
        jep.addKeyListener(ekmhandler);
        jep.addMouseListener(ekmhandler);
        RSyntaxEditorMouseMotionAdapter mouseMotionHandler = new RSyntaxEditorMouseMotionAdapter();
        if (GlobalValues.mouseMotionListenerForRSyntax==true)
          jep.addMouseMotionListener(mouseMotionHandler);
                
        mainJMenuBar = new JMenuBar();
                        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setToolTipText("File editing operations");
        fileMenu.setFont(GlobalValues.uifont);
                
        
             
        
        
                
                JMenu  codeCompletionMenu = new JMenu("Completion");
                codeCompletionMenu.setFont(GlobalValues.uifont);
                codeCompletionMenu.setToolTipText("Used with CTRL-SPACE. Retrieves information using Scala Completion (Scala mode) or Global completion  for basic ScalaLab methods and libraries (Global mode)");
                
                JMenuItem  ejmlCompletionJMenuItem = new JMenuItem("Retrieve code completion for the EJML library (for Global completion)");
                ejmlCompletionJMenuItem.setFont(GlobalValues.uifont);
                ejmlCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int nejmlMethods = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "org/ejml", JavaGlobals.ejmlFile);
               System.out.println("readed number of methods: "+nejmlMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
         
    JMenuItem     RSyntaxCompletionHelpJMenuItem  = new JMenuItem("Help on completion"); 
    RSyntaxCompletionHelpJMenuItem.setFont(GlobalValues.uifont);
     RSyntaxCompletionHelpJMenuItem.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
       EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("CompletionHelp.html");
        if (GlobalValues.useSystemBrowserForHelp==false) {
         inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
         inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
         inPlaceHelpPane.setVisible(true);
        }
          }
                });
            
            
            final JMenuItem GlobalScalaCompletionJMenuItem = new JMenuItem("Toggle Global/Scala completion mode, current mode is:  "+GlobalValues.rsyntaxInScalaCompletionModeProp);
             GlobalScalaCompletionJMenuItem.setToolTipText("Global completion concerns basic ScalaLab routines and libraries, Scala completion results are obtained from a Scala completer");
             GlobalScalaCompletionJMenuItem.setFont(GlobalValues.uifont);
             GlobalScalaCompletionJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              if (scalaExec.Interpreter.GlobalValues.rsyntaxInScalaCompletionMode  == true) {
                  scalaExec.Interpreter.GlobalValues.rsyntaxInScalaCompletionMode  = false;
                  scalaExec.Interpreter.GlobalValues.rsyntaxInScalaCompletionModeProp = "Global";
                  rsyntaxEdit.GCompletionProvider.installAutoCompletion();   
                  GlobalScalaCompletionJMenuItem.setText("Toggle Global/Scala completion mode, current mode is:  "+GlobalValues.rsyntaxInScalaCompletionModeProp);
                         }
              else
              {
      scalaExec.Interpreter.GlobalValues.rsyntaxInScalaCompletionMode  = true;
      scalaExec.Interpreter.GlobalValues.rsyntaxInScalaCompletionModeProp = "Scala";
      GlobalScalaCompletionJMenuItem.setText("Toggle Global/Scala completion mode, current mode is: "+GlobalValues.rsyntaxInScalaCompletionModeProp);
            
              }               
            }
             });
          
             JMenuItem  mtjCompletionJMenuItem = new JMenuItem("Retrieve code completion for the MTJ library (for Global completion)");
                 mtjCompletionJMenuItem.setFont(GlobalValues.uifont);
                mtjCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int nmtjMethods  = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "no/uib/cipr", JavaGlobals.mtjColtSGTFile);
               System.out.println("readed number of methods "+nmtjMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
                 
                
                 JMenuItem  apacheCompletionJMenuItem = new JMenuItem("Retrieve code completion for the Apache Commons (for Global completion)");
                 apacheCompletionJMenuItem.setFont(GlobalValues.uifont);
                apacheCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int nApacheMethods = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "org/apache/commons/math", JavaGlobals.JASFile);
               System.out.println("readed number of methods "+nApacheMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
                
                
                JMenuItem  numericalRecipesCompletionJMenuItem = new JMenuItem("Retrieve code completion for the Numerical Recipes  library");
                numericalRecipesCompletionJMenuItem.setFont(GlobalValues.uifont);
                numericalRecipesCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int nrMethods = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "com/nr", JavaGlobals.numalFile);
               System.out.println("readed number of methods "+nrMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
                
                
                JMenuItem  numalCompletionJMenuItem = new JMenuItem("Retrieve code completion for the NUMAL  library (for Global completion)");
                numalCompletionJMenuItem.setFont(GlobalValues.uifont);
                numalCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int numalMethods = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "numal", JavaGlobals.numalFile);
               System.out.println("readed  number of methods "+numalMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
              
                JMenuItem  jlapackCompletionJMenuItem = new JMenuItem("Retrieve code completion for the JLAPACK  library (for Global completion)");
                jlapackCompletionJMenuItem.setFont(GlobalValues.uifont);
                jlapackCompletionJMenuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 if (GlobalValues.provider==null) {
                     JOptionPane.showMessageDialog(null, "Please open the rsyntaxArea based editor first", "Open RSyntaxArea editor then retry completion load ", JOptionPane.INFORMATION_MESSAGE);
                 }        else  {
           try {
               int lapackMethods = rsyntaxEdit.GCompletionProvider.scanBuiltInLibraryClassesForEditor((DefaultCompletionProvider) GlobalValues.provider, "org/netlib", JavaGlobals.LAPACKFile);
               System.out.println("readed number of methods"+lapackMethods);
              }  
           catch (IOException ex) {
            System.out.println("exception in scanBuiltInLibraryClassesForEditor");
            System.out.println(ex.getMessage());
        }
                }
              }
             }
         );
                codeCompletionMenu.add(GlobalScalaCompletionJMenuItem);
                codeCompletionMenu.add(ejmlCompletionJMenuItem);
                codeCompletionMenu.add(numericalRecipesCompletionJMenuItem);
                codeCompletionMenu.add(numalCompletionJMenuItem);
                codeCompletionMenu.add(jlapackCompletionJMenuItem);
                codeCompletionMenu.add(apacheCompletionJMenuItem);
                codeCompletionMenu.add(mtjCompletionJMenuItem);
                codeCompletionMenu.add(RSyntaxCompletionHelpJMenuItem);
              
                
                
        saveEditorTextJMenuItem = new JMenuItem("Save Editor Text ");
        saveEditorTextJMenuItem.addActionListener(new saveEditorTextAction());
        saveEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        saveAsEditorTextJMenuItem = new JMenuItem("Save As Editor Text to File");
        saveAsEditorTextJMenuItem.addActionListener(new saveAsEditorTextAction());
        saveAsEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        loadEditorTextJMenuItem = new JMenuItem("Load  File to Editor");
        loadEditorTextJMenuItem.addActionListener(new loadEditorTextAction());
        loadEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        loadEditorTextJMenuItem.setFont(GlobalValues.uifont);
        

             JMenuItem displayScalaSciVariables = new JMenuItem("Display current ScalaSci Variables");
             displayScalaSciVariables.setToolTipText("Display names, sizes, types and values of the current scalaSci variables in the workspace");
             displayScalaSciVariables.setFont(GlobalValues.uifont);
             displayScalaSciVariables.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
             displayScalaSciVariables.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
          scalaSciCommands.WatchInterpreterState.printUserNamesAndValues();
       }  
      });
            
        exitJMenuItem = new JMenuItem("Exit");
        exitJMenuItem.setFont(GlobalValues.uifont); 
        
        exitJMenuItem.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            int userOption = JOptionPane.CANCEL_OPTION;
            if (editorTextSaved == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+editedFileName+" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
                saveRecentPaneFiles();   // save the list of the recently accessed files
                currentFrame.dispose();
                GlobalValues.mainSplitPane.setBottomComponent(GlobalValues.outputPane);
                
              }
       
            }
        });

        
        fileMenu.add(saveEditorTextJMenuItem);
        fileMenu.add(saveAsEditorTextJMenuItem);
        fileMenu.add(loadEditorTextJMenuItem);
        fileMenu.add(displayScalaSciVariables);
        fileMenu.add(exitJMenuItem);

        cancelJMenuItem.setFont(GlobalValues.uifont);
        cancelJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
             
                scalaExec.Interpreter.PendingThreads.cancelPendingThreads();
           //scalaExec.Interpreter.ScriptExecutors.scriptExecutor.shutdownNow();
           //scalaExec.Interpreter.ScriptExecutors.scriptExecutor = Executors.newCachedThreadPool();
            }          
        });
      
        controlTasks.setFont(GlobalValues.uifont);
        controlTasks.setToolTipText("Attempt to cancel any pending computational threads");
        controlTasks.add(cancelJMenuItem);
        
    helpMenu = new JMenu("Programmer'sHelp");
    helpMenu.setToolTipText("Help on the basic editor commands for controlling script execution");
    helpMenu.setFont(GlobalValues.uifont);
    JMenuItem editorBasicCommandsMenuItem = new JMenuItem("Editor Basic Commands for Programming");
    editorBasicCommandsMenuItem.setFont(GlobalValues.uifont);
    helpMenu.add(editorBasicCommandsMenuItem);
    editorBasicCommandsMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame helpFrame = new JFrame("Help on editor");
                JTextArea helpArea = new JTextArea(
                        "   F6:  execute selected ScalaSci text  or current line (with a seperate thread) . \n "+
                                "  Shift-F6:  execute selected ScalaSci  text  or current line (within the Event Dispatch Thread) . \n "+
    "Ctrl-SPACE: Code Completion using rsyntaxpane with global entries or Scala Completion Results\n"+
    "F7: Fills table with Scala Completion Results for using with Control Space when RSyntaxArea in Scala Completion Mode\n"+
    "SHIFT-F7: Scala Completion - For package completion select the corresponding package name , e.g.  \"scalaSci.math\"  \n"+                        
    "Press F11: Expands Abbreviations \n"+                        
    "Press F2 to execute code up to cursor position  \n"+
    "Press F5 to clear the output console \n"+
     "Double click on an identifier: Displays its value \n"+                                                
    "Select text (e.g. \"svd\") and press F1 displays autocompletion info on that item\n"+
     "Press  F4 on a selected identifier. Presents a completion list,  if a dot exists the results are filtered, e.g.   with:  \"jf.setV\", filters only methods starting with \"setV"+"\n"+                
     "Press  Shift-F4 on a selected identifier corresponding to a class type (e.g. javax.swing.JFrame). Presents information for the type with a JTree \n"
                        );

            helpArea.setFont(GlobalValues.uifont);
            helpFrame.add(helpArea);
            helpFrame.setSize(600, 400);
            helpFrame.setVisible(true);
    
            }
            
    });
    
    mainJMenuBar.add(fileMenu);
    mainJMenuBar.add(codeCompletionMenu);
    mainJMenuBar.add(controlTasks);
    mainJMenuBar.add(helpMenu);
    
    loadRecentPaneFiles();
    
    currentFrame.setJMenuBar(mainJMenuBar);

    currentFrame.setTitle(titleStr+":  File: "+selectedValue);
        
// use user settings for edit frames to adjust location and size
    currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
        
        
  // load the file      
                   FileReader fr = null;
            try {
                fr = new FileReader(selectedValue);
                if (fr != null)
                  jep.read(fr, null);
                
            } catch (FileNotFoundException ex) {
                System.out.println("file "+selectedValue+" not found");
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            finally {
                try {
        if (fr!=null)
            fr.close();
   
        
                } 
                
                catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            
            }
        
        Rectangle  b = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        currentFrame.setLocation(GlobalValues.rlocX, GlobalValues.rlocY);
        currentFrame.setSize(GlobalValues.rWidth, GlobalValues.rHeight);
        currentFrame.setVisible(true);
     
        JPopupMenu popup = jep.getPopupMenu();
        popup.addSeparator();
        popup.add(new JMenuItem(new plotSignalAction()));
        popup.add(new JMenuItem(new executeRightClickAction()));
        scrPane = new RTextScrollPane(jep);
        
        currentFrame.add(scrPane);
        currentFrame.setTitle(titleStr+":  File: "+selectedValue);
        
        //   if that Editor Frame is the main Editor frame, additional settings is required e.g.
        //   adding  the console output frame at the bottom 
   if (isMainFrame) {
        
        
        JSplitPane sp = new JSplitPane(SwingConstants.HORIZONTAL);
        sp.setTopComponent(scrPane);
        sp.setBottomComponent(GlobalValues.outputPane);
        System.setOut(GlobalValues.consoleOutputWindow.consoleStream);
        System.setErr(GlobalValues.consoleOutputWindow.consoleStream);
        
        
        currentFrame.add(sp);
        currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        currentFrame.setVisible(true);
        sp.setDividerLocation( 0.7 );
        
   
   }
        else {
 fileMenu.add(exitJMenuItem); 
  }
            
        return jep;
      
    }
    
    private class plotSignalAction extends TextAction {
        
        public plotSignalAction() {
            super("Plot Signal");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
        JTextComponent tc = getTextComponent(e);
        String signalName = null;
        
        // Get the name of the signal to plot. If there is a selection, use that as the signal name,
        // otherwise, scan for a signalname around the caret
        try {
            int selStart = tc.getSelectionStart();
            int selEnd = tc.getSelectionEnd();
            if (selStart != selEnd) {
                signalName = tc.getText(selStart, selEnd - selStart);
            }
            else {
                signalName = getSignalNameAtCaret(tc);
            }
        }
        catch (BadLocationException ble) {
            ble.printStackTrace();;
            UIManager.getLookAndFeel().provideErrorFeedback(tc);
            return;
            }
                  GlobalValues.globalInterpreter.interpret("plot("+signalName+")");
         
        }
      }
    
    // gets the signal name that the caret is sitting on
    public String getSignalNameAtCaret(JTextComponent tc) throws BadLocationException {
        int caret = tc.getCaretPosition();
        int start = caret;
        Document doc = tc.getDocument();
        while (start > 0) {
            char ch = doc.getText(start-1, 1).charAt(0);
            if (isSignalNameChar(ch)) {
                start--;
            }
            else {
                break;
            }
          }
        int end = caret;
        while (end < doc.getLength()) {
            char ch = doc.getText(end, 1).charAt(0);
            if (isSignalNameChar(ch)) {
                end++;
            }
            else {
                break;
            }
        }
        return doc.getText(start, end-start);
    }
    
    public boolean isSignalNameChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
    
    private void performSearch(boolean forward) {
        //SearchContext context = new SearchContext();
        String text = searchField.getText();
        if (text.length() ==0)
            return;
        boolean caseSensitive = matchCaseCB.isSelected();
        boolean regExpr = regexCB.isSelected();
        boolean forwardSearch = forward;
        boolean wholeWord = false;
        
        SearchContext context = new SearchContext();
        if (text.length() ==0)
            return;
        context.setSearchFor(text);
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setSearchForward(forward);
        context.setWholeWord(false);
        
        boolean found = SearchEngine.find(jep, context);

        if (!found)
            JOptionPane.showMessageDialog(this.currentFrame, "Text not found");
    }
    
     // edit the file with name selectedValue
    public rsyntaxEditor(String selectedValue) {
        RSyntaxTextArea  jep = commonEditingActions(selectedValue, false);
        GlobalValues.globalRSyntaxEditorPane = jep;
        GCompletionProvider.installAutoCompletion();
        
 }
    

         // edit the file with name selectedValue
    public rsyntaxEditor(String selectedValue, boolean initConsoleWindow) {
        RSyntaxTextArea  jep = commonEditingActions(selectedValue, true);
        GlobalValues.globalRSyntaxEditorPane = jep;
        jep.setCaretPosition(0);
        scalaExec.Interpreter.GlobalValues.editorPane.requestFocus();
        GCompletionProvider.installAutoCompletion();
        
    }
   
      
      

    // save the current file kept in editor
class saveEditorTextAction extends AbstractAction  {
    public saveEditorTextAction() { super("Save Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        String saveFileName = editedFileName;   // file name to save is the one currently edited
        if (saveFileName == null)  { // not file specified thus open a FileChooser in order the user to determine it
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 saveFileName = selectedFile.getAbsolutePath();
                 editedFileName = saveFileName;    // update the edited file
                 GlobalValues.globalRSyntaxFrame.setTitle(titleStr+":  File: "+editedFileName);
   
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
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String saveFileName = selectedFile.getAbsolutePath();
                 File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jep.write(fw);
                        editorTextSaved = true;  //  not need to save anything yet
                     
                        GlobalValues.globalRSyntaxFrame.setTitle(titleStr+":  File: "+editedFileName);

                        //  add the loaded file to the recent files menu
            if (recentPaneFiles.contains(saveFileName) ==  false)  {
                recentPaneFiles.add(saveFileName);
                updateRecentPaneFilesMenu();
              }

            // update the workingDir
            String pathOfLoadFileName = saveFileName.substring(0, saveFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
            
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
         
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String loadFileName = selectedFile.getAbsolutePath();
                       
                   FileReader fr = null;
            try {
                fr = new FileReader(loadFileName);
                jep.read(fr, null);
  
           //  add the loaded file to the recent files menu
            if (recentPaneFiles.contains(loadFileName) ==  false)  {
                recentPaneFiles.add(loadFileName);
                updateRecentPaneFilesMenu();
              }
        }
            catch (FileNotFoundException ex) {
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
            // update the workingDir
            String pathOfLoadFileName = editedFileName.substring(0, editedFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
            
            editorTextSaved = true;  // a freshly loaded file doesn't require saving
            GlobalValues.globalRSyntaxFrame.setTitle(titleStr+":  File: "+editedFileName);
                           
     }
   }
 }
    }
}











