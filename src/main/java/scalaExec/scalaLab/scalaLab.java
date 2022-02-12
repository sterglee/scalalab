package scalaExec.scalaLab;

/*
This file implements the main Swing GUI for ScalaLab
 */
import scala.collection.JavaConverters.*;

import scalainterpreter.ScalaInterpreterPane;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URL;

import java.util.regex.*;
import scalaExec.gui.*;

import java.net.URLDecoder;

import javax.swing.event.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import rsyntaxEdit.rsyntaxEditor;
import scala.reflect.io.Directory;
import scalaExec.ClassLoaders.JarClassLoader;
import scalaExec.gui.scalalabConsole;

import scalaExec.Interpreter.GlobalValues;
import scalalab.JavaGlobals;

import static scalaExec.Interpreter.ControlInterpreter.*;


/**
 * The main GUI for the scalaLab
 */
public class scalaLab extends JFrame implements WindowListener, ActionListener {

    public scalalabConsole scalalabConsole;    // displays  anything printed to Java's System.out and ScalaLab's output (e.g. variable contents)
    public ScalaInterpreterPane scalaPane;      // is the same reference as GlobalValues.globalInterpreterPane
    static private String netbeansScalaLabArg;  // name of ScalaLab executable .jar when starting from Netbeans

    public Vector<String> recentPaneFiles = new Vector<>();  // keeps the full names of the recent files
    public JMenu recentPaneFilesMenu = new JMenu("Recent Files");  // created dynamically to keep the recent files list
    public String fileWithFileListOfPaneRecentFiles = "recentsPaneFile.txt"; // the list of the recent editor's pane files

    static public JList pathsList;    // handles the path of the Scala Interpreter

    public JMenuBar mainJMenuBar;    // the main menu bar of the ScalaLab main window

    private JMenu editMenu;   // edit menu for the jSyntaxPane based ScalaLab editor
    private JMenuItem displayCurrentAbbreviationsJMenuItem;     // abbreviations programmed with the "Abbreviations.txt" file
    private JMenuItem displayScalaSciVariables;  // display the current variables of the workspace
    private JMenuItem fileExplorerFocusMenuItem;
    private JMenuItem rsyntaxAreaEditJMenuItem;
    private JMenuItem editJMenuItem;
    private JMenuItem jeditJMenuItem;

    public JMenu FileMenu;

    private JMenuItem saveEditorTextJMenuItem;
    private JMenuItem saveAsEditorTextJMenuItem;
    private JMenuItem loadEditorTextJMenuItem;

    private JMenu scalaInterpreterMenu;
    private final JMenuItem resetScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports");
    private final JMenuItem resetScalaInterpreterNoImportsItem = new JMenuItem("Reset Scala Interpreter not using any ScalaLab related  imports (useful for e.g. avoiding problems with ScalaLab related implicits)");
    private final JMenuItem resetScalaInterpreterEJMLItem = new JMenuItem("Reset Scala Interpreter using Efficient Java Matrix Library of Peter Abeles");
    private final JMenuItem resetScalaInterpreterD2Das1DItem = new JMenuItem("Reset Scala Interpreter using D2Das1D matrix");
    private final JMenuItem resetScalaInterpreterJBLASItem = new JMenuItem("Reset Scala Interpreter using JBLAS Library");
    private final JMenuItem resetScalaInterpreterCommonMathsItem = new JMenuItem("Reset Scala Interpreter using Apache Common Maths Library");
    private final JMenuItem resetScalaInterpreterMTJItem = new JMenuItem("Reset Scala Interpreter using MTJ  Java Matrix Library ");
    private final JMenuItem resetToolboxesScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports and installed toolboxes");
    private final JMenuItem clearAllScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter without any default Imports");
    private final JMenuItem resetReplayScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports, replaying commands");
    private final JMenuItem resetReplayScalaInterpreterEJMLItem = new JMenuItem("Reset Scala Interpreter using Efficient Java Matrix Library of Peter Abeles, replaying commands");
    private final JMenuItem resetReplayScalaInterpreterCommonMathsItem = new JMenuItem("Reset Scala Interpreter using Apache Common Maths Library, replaying commands");
    private final JMenuItem resetReplayScalaInterpreterMTJItem = new JMenuItem("Reset Scala Interpreter using MTJ  Java Matrix Library, replaying commands");
    private final JMenuItem resetReplayToolboxesScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter using Scalalab default Imports and installed toolboxes, replaying commands");
    private final JMenuItem clearReplayAllScalaInterpreterItem = new JMenuItem("Reset Scala Interpreter without any default Imports, replaying last session commands");
    private final JMenuItem clearBufferOfPreviousCommandsScalaInterpreterItem = new JMenuItem("Clear the buffer of previous commands");
    private final JMenuItem closeAllFiguresJMenuItem = new JMenuItem("Close All Figures (F12)");

    private final JMenuItem toggleCommandRecordingFlagItem = new JMenuItem("Command Recording state is ON");
    private final JMenuItem queryCurrentInterpreterOfPaneItem = new JMenuItem("Which interpreter type is the current main interpreter?");

    private JMenu confMenu;   // configuration menu 
    private JMenu appearanceMenu;   // apperance menu

    private JMenuItem watchVariablesJMenuItem;


    private JMenu libConfMenu;
    private JMenuItem controlMainToolbarJMenuItem;
    private JMenuItem controlScalaCompilerJMenuItem;
    private JMenuItem controlExplorerJMenuItem;
    private JMenuItem paneAdjustFontMenuItem;
    private JMenuItem rsyntaxAreaAdjustFontMenuItem;
    private JMenuItem outConsoleAdjustFontMenuItem;
    private JMenuItem adjustFontMenuItem;
    private JMenuItem adjustFontsUIMenuItem;  // for main menus
    private JMenuItem adjustFontspUIMenuItem;  // for popup menus
    private JMenuItem adjustFontsgUIMenuItem; // for other GUI components 
    private JMenuItem adjustHtmlFontsJMenuItem; // for html help
    private JMenuItem adjustsFontsbUIMenuItem;   // for buttons
    private JMenuItem htmlWithSystemBrowser;

    private JMenuItem toolboxesMenuItem;

    private JMenuItem adjustLookAndFeelMenuItem;
    private JMenuItem varsConfJMenuItem;
    private JMenuItem saveConfParamsJMenuItem;
    private JMenuItem browseFileSysForPathsJMenuItem;
    private JMenuItem browseClassesJMenuItem;
    private JMenuItem promptJMenuItem;

    private JMenuItem swingConsoleMenuItemJAMA;
    private JMenuItem swingConsoleMenuItemEJML;

    public JMenuItem getPromptJMenuItem() {
        return promptJMenuItem;
    }

    private JMenuItem loadConfParamsJMenuItem;
    private JMenuItem trigonometricFunctionsJMenuItem;
    private JMenuItem toolboxesToolbarJMenuItem;

    private JMenu examplesMenu;
    private JMenuItem ScalaSciExamplesHelpJMenuItem;
    private JMenuItem ScalaSciExamplesJTreeJMenuItem;
    private JMenuItem ScalaSciPlotExamplesJTreeJMenuItem;

    
    private JMenu JavaHelpMenu;

    private JMenu symbolicAlgebraJMenu;
    private JMenuItem startsymJavaJMenuItem;
    private JMenuItem displayLatexOnSymEvalJMenuItem;

    private JMenu helpMenu;
    private JMenuItem usefulInputCommandsJMenuItem;
    private JMenuItem matrixConversionJMenuItem;
    private JMenuItem basicHelpJMenuItem;
    private JMenuItem aboutHelpJMenuItem;
    private JMenuItem memoryHelpJMenuItem;

    private JMenu wizardsMenu;
    private JMenuItem wizardsScalaSciJMenuItem, wizardsScalaSciScalaJMenuItem;

    private JMenuItem JavaHelpJMenuItem;
    private JMenuItem ScalaHelpJMenuItem;
    private JMenuItem exitJMenuItem;

    static public Dimension ScreenDim;

    public static JScrollPane pathsScrollPane;
    static public JTabbedPane uiTabbedPane;

    /**
     * The area used for user input and where the answers are displayed
     */
    public static JScrollPane variablesWorkSpacePane;

    public static scalaLabExplorer explorerPanel;

    public JFrame myId;    // keeps a reference to the main ScalaLab's frame

    public static int xSizeMainFrame, ySizeMainFrame;
    public static int xLocMainFrame, yLocMainFrame;

    private String JavaHelpStr, ScalaHelpStr;

    public Image scalaImage, scalaImageSmall;

    static Class[] formals = {String[].class};
    static Object[] actuals = {new String[]{""}};

    static public JTable varsTable;
    static public JPopupMenu variablesPanelPopupMenu;

    public GlobalValues instanceGlobals;

    public static boolean paneInited = false;   // tracks the initialization of the main ScalaLab Interpreter Pane

    public JTabbedPane tabbedToolbars;    // a helper tabbed toolbar, with tabs organizing categories of routines (e..g. "Optimizaion") and providing some help  

    /**
     * Reacts to the user menu and update (if necessary) the interface.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == exitJMenuItem) {
            closeGUI();

        }
    }

    // set the URLs for obtaining on-line help
    private void initHelpObjects() {

        //  html documentation URLs     
        JavaHelpStr = "https://docs.oracle.com/javase/8/docs/api/";
        ScalaHelpStr = "http://www.scala-lang.org/docu/files/api/index.html";

    }

    public static void initAutocompletion() {

        scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci = new scalaExec.gui.AutoCompletionScalaSci();

        scalaExec.Interpreter.GlobalValues.autoCompletionScalaSciLoader = new scalaExec.gui.AutoCompletionScalaSciLoader(GlobalValues.jarFilePath, GlobalValues.ScalaSciClassPath, null);

        scalaExec.Interpreter.GlobalValues.autoCompletionScalaSciLoader.loadClasses();
    }

    /**
     * Function called when the gui is being close
     */
    public void closeGUI() {

        int userOption = JOptionPane.YES_OPTION;
        if (GlobalValues.editorTextSaved == false) {
            userOption = JOptionPane.showConfirmDialog(scalaPane, "File: " + GlobalValues.editingFileInPane + " not saved. Proceed? ",
                    "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
        } else {
            userOption = JOptionPane.YES_OPTION;
        }

        if (userOption == JOptionPane.YES_OPTION) {
            saveRecentPaneFiles();   // save the list of the recently accessed files
            File outPropFile = new File(Directory.Current().get().path() + File.separator + "scalalab.props");
            try {
                FileOutputStream outFile = new FileOutputStream(outPropFile);
                GlobalValues.passPropertiesFromWorkspaceToSettings(GlobalValues.settings);  // update properties to the current values kept in workspace
                GlobalValues.settings.store(outFile, "Saved scalaLab global conf parameters");
                outFile.close();

                GlobalValues.writeUserPaths();  // write the user defined paths for configuring the Scala Interpreter classpath

                scalaExec.scalaLab.favouritePaths.saveFavouritePaths(scalaExec.Interpreter.GlobalValues.scalalabFavoritePathsFile, scalaExec.Interpreter.GlobalValues.scalalabMainFrame.explorerPanel.favouritePathsCB);

                if (GlobalValues.forHTMLHelptempFile != null) {
                    GlobalValues.forHTMLHelptempFile.delete();  // delete the temporary file used for presenting .html files in .jar
                }
                System.exit(0);
            } catch (Exception fnfe) {
                System.out.println("error opening file for writing configuration");
                fnfe.printStackTrace();
                System.exit(0);
            }
        }
    }


    /*
    * This method will take a file name and try to "decode" any URL encoded characters.  For example
    * if the file name contains any spaces this method call will take the resulting %20 encoded values
    * and convert them to spaces.
    *
     */
    public static String decodeFileName(String fileName) {
        String decodedFile = fileName;

        try {
            decodedFile = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encounted an invalid encoding scheme when trying to use URLDecoder.decode().");
            System.err.println("Please note that if you encounter this error and you have spaces in your directory you will run into issues. ");
        }

        return decodedFile;
    }

    private void initInterpreterPane() {
        scalaPane = new scalainterpreter.ScalaInterpreterPane();  // we do not initialize InterpreterPane till we read ClassPath properties
        GlobalValues.globalInterpreterPane = scalaPane;   //  the globally accessible interpreter pane used
        scalaPane.init();
    }

    private void initScreenAttributes() {
        // prevent closing ScalaLab accidentically by closing its main window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        String crossPlatformLookAndFeel = "";
        ScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //position the frame in the centre of the screen
        xSizeMainFrame = (int) ((double) ScreenDim.width / 1.2);
        ySizeMainFrame = (int) ((double) ScreenDim.height / 1.4);
        xLocMainFrame = (ScreenDim.width - xSizeMainFrame) / 25;
        yLocMainFrame = 50 + (ScreenDim.height - ySizeMainFrame) / 25;

        GlobalValues.sizeX = xSizeMainFrame;
        GlobalValues.sizeY = ySizeMainFrame;

        GlobalValues.figFrameSizeX = (int) ((double) xSizeMainFrame / 1.5);
        GlobalValues.figFrameSizeY = (int) ((double) ySizeMainFrame / 1.5);

        // Force SwingSet to come up in the Cross Platform L&F
        try {
            // if (GlobalValues.hostIsUnix==false)  {
            String sysLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(sysLookAndFeel);
            //}
            //     else {
            //       crossPlatformLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            //       UIManager.setLookAndFeel(crossPlatformLookAndFeel);
            //      }
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }

    }

    private void initURLsMenuBarMainFrame() {

        // in order to take e.g. the font properties
        GlobalValues.passPropertiesFromSettingsToWorkspace(GlobalValues.settings);

        URL scalaImageURL = getClass().getResource("resources/scala.gif");
        URL scalaSmallImageURL = getClass().getResource("resources/smallScalaImage.jpg");

        scalaImage = Toolkit.getDefaultToolkit().getImage(scalaImageURL);
        scalaImageSmall = Toolkit.getDefaultToolkit().getImage(scalaSmallImageURL);

        InitJMenuBar(this);

        myId = this;    // the reference to the main ScalaLab's frame
        GlobalValues.scalalabMainFrame = this;   // keep instance of scalaLab main frame
        myId.setSize(xSizeMainFrame, ySizeMainFrame);
        myId.setLocation(xLocMainFrame, yLocMainFrame);
        myId.addWindowListener((WindowListener) myId);
        myId.setResizable(true);

        GlobalValues.scalaImage = scalaExec.Interpreter.GlobalValues.scalalabMainFrame.scalaImage;
        GlobalValues.smallScalaImage = scalaExec.Interpreter.GlobalValues.scalalabMainFrame.scalaImageSmall;
        GlobalValues.scalalabMainFrame.setIconImage(GlobalValues.smallScalaImage);

    }

    private void initTabbedPane() {

        uiTabbedPane = new JTabbedPane();

        uiTabbedPane.addTab("", new ImageIcon(GlobalValues.scalaImage), null);
        uiTabbedPane.setToolTipTextAt(GlobalValues.mainTab, "The main tab consists of the jSyntaxPane based editor and the Console Output window");

        uiTabbedPane.addTab("Java Help", null, null);
        uiTabbedPane.addTab("Scala Help", null, null);
        uiTabbedPane.setToolTipTextAt(GlobalValues.javaHelpTab, "Link to the official Java APIs HTML documentation");

        uiTabbedPane.addTab("ScalaSci Toolboxes", null, null);

        GlobalValues.mainSplitPane = new JSplitPane(SwingConstants.HORIZONTAL);
        GlobalValues.mainSplitPane.setTopComponent(uiTabbedPane);
        GlobalValues.uiTabbedPane = uiTabbedPane;
        GlobalValues.mainSplitPane.setBottomComponent(GlobalValues.outputPane);

        GlobalValues.mainSplitPane.setDividerLocation(this.getSize().height * 3 / 4);

        add(GlobalValues.mainSplitPane, "Center");

        uiTabbedPane.addChangeListener((ChangeEvent e) -> {
            int n = uiTabbedPane.getSelectedIndex();
            loadTab(n);
        });

    }

    /**
     * Create the main graphical interface (menu, buttons, delays...).
     */
    public scalaLab(String ScalaSciClassPath, String[] additionalToolboxes) {


        initHelpObjects();

        initScreenAttributes();

        initURLsMenuBarMainFrame();

        GlobalValues.readUserPaths();    // read the saved user paths for configuration of ScalaLabClassPath

        InitConsole();

        initInterpreterPane();

        InitTabbedToolbars();

        initTabbedPane();

        loadRecentPaneFiles();

        loadTab(GlobalValues.mainTab);

        GlobalValues.ScalaLabInInit = false;   // ScalaLab inited

        GlobalValues.extensionClassLoader = new scalaExec.ClassLoaders.ExtensionClassLoader(".");
        

    }

    public void createExplorerPanel() {
        if (GlobalValues.explorerVisible == true) {

            if (GlobalValues.jfExplorerFrame != null) {
                GlobalValues.jfExplorerFrame.removeAll();
                GlobalValues.jfExplorerFrame.dispose();
            }

            GlobalValues.jfExplorerFrame = new JFrame("ScalaLab Explorer");
            GlobalValues.jfExplorerFrame.setLayout(new GridLayout(2, 1));

            explorerPanel = new scalaLabExplorer();
            explorerPanel.setLayout(new BorderLayout());
            explorerPanel.buildClassScriptPathsTree();

            JPanel textPanelExplorerDirs = new JPanel(new BorderLayout());  // up panel containing label text
            java.text.DecimalFormat fmt = new java.text.DecimalFormat("0.00");
            String mem = fmt.format((double) GlobalValues.rt.freeMemory() / 1000000);
            String dispStr = "Available memory : " + mem + "MB.  ";
            GlobalValues.availMemLabel = new JLabel(dispStr);
            GlobalValues.availMemLabel.setFont(new Font("Arial", Font.BOLD, 12));
            textPanelExplorerDirs.add(GlobalValues.availMemLabel, BorderLayout.EAST);

            JPanel textPanelExplorerBrowser = new JPanel(new BorderLayout());  // up panel containing label text
            JLabel labelExplorerBrowser = new JLabel("scalaLab explorer: edit, compile, run, create, delete files");
            labelExplorerBrowser.setFont(new Font("Arial", Font.BOLD, 12));
            textPanelExplorerBrowser.add(labelExplorerBrowser, BorderLayout.EAST);

            JPanel descriptionPanel = new JPanel(new BorderLayout());
            descriptionPanel.add(textPanelExplorerDirs, BorderLayout.WEST);
            descriptionPanel.add(textPanelExplorerBrowser, BorderLayout.EAST);

            JPanel explorerLabelPanel = new JPanel(new BorderLayout());
            explorerLabelPanel.add(descriptionPanel, BorderLayout.NORTH);
            explorerLabelPanel.add(explorerPanel, BorderLayout.SOUTH);

            pathsScrollPane = new JScrollPane(explorerLabelPanel);

            GlobalValues.jfExplorerFrame.add(pathsScrollPane);

            scalalabConsole cons = new scalalabConsole();
            GlobalValues.scalalabMainFrame.scalalabConsole = cons;
            cons.displayPrompt();
            JPanel auxiliaryPanel = new JPanel();
            auxiliaryPanel.setLayout(new GridLayout(1, 2));
            auxiliaryPanel.add(new JScrollPane(cons));

            constructPathPresentationPanel();
            auxiliaryPanel.add(GlobalValues.pathPresentationPanel);

            GlobalValues.jfExplorerFrame.add(auxiliaryPanel);

            GlobalValues.jfExplorerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            GlobalValues.jfExplorerFrame.setSize(GlobalValues.ScreenDim.width - GlobalValues.scalalabMainFrame.getLocation().x
                    - GlobalValues.scalalabMainFrame.getWidth(), (int) ((double) GlobalValues.scalalabMainFrame.getSize().height / 1.4));
            GlobalValues.jfExplorerFrame.setLocation(GlobalValues.scalalabMainFrame.getLocation().x + GlobalValues.scalalabMainFrame.getSize().width,
                    GlobalValues.scalalabMainFrame.getLocation().y);

            JMenu explMenu = new JMenu("Close");
            explMenu.setFont(GlobalValues.guifont);
            JMenuItem closeExplMenuItem = new JMenuItem("Close");
            closeExplMenuItem.setFont(GlobalValues.guifont);
            closeExplMenuItem.setToolTipText("Closes the explorer");
            closeExplMenuItem.addActionListener((ActionEvent e) -> {
                GlobalValues.explorerVisible = false;
                GlobalValues.jfExplorerFrame.removeAll();
                GlobalValues.jfExplorerFrame.dispose();
            });
            explMenu.add(closeExplMenuItem);

            JMenuBar explMenuBar = new JMenuBar();
            explMenuBar.add(explMenu);
            GlobalValues.jfExplorerFrame.setJMenuBar(explMenuBar);

            GlobalValues.jfExplorerFrame.setVisible(true);

            paneInited = true;
        }
    }

    public void createToolbarPanel() {

        if (GlobalValues.mainToolbarVisible == true) {

            // dispose any previous toolbar instance
            if (GlobalValues.toolbarFrame != null) {
                GlobalValues.toolbarFrame.removeAll();
                GlobalValues.toolbarFrame.dispose();
            }

            GlobalValues.toolbarFrame = new JFrame("Toolbars");

            // prepare "Close" option
            JMenu closeToolbarsMenu = new JMenu("Close");
            JMenuItem closeToolbarsMenuItem = new JMenuItem("Close");
            closeToolbarsMenuItem.addActionListener((ActionEvent e) -> {
                GlobalValues.mainToolbarVisible = false;
                GlobalValues.toolbarFrame.dispose();
            });
            closeToolbarsMenu.add(closeToolbarsMenuItem);
            JMenuBar closeMenuBar = new JMenuBar();
            closeMenuBar.add(closeToolbarsMenu);
            GlobalValues.toolbarFrame.setJMenuBar(closeMenuBar);
            GlobalValues.toolbarFrame.add(tabbedToolbars);
            if (GlobalValues.jfExplorerFrame != null) {
                // place toolbar relative to the explorer frame
                GlobalValues.toolbarFrame.setSize(GlobalValues.jfExplorerFrame.getSize().width, 300);
                GlobalValues.toolbarFrame.setLocation(GlobalValues.jfExplorerFrame.getLocation().x, GlobalValues.jfExplorerFrame.getLocation().y + GlobalValues.jfExplorerFrame.getSize().height);
            } else {
                // place toolbar absolutely
                GlobalValues.toolbarFrame.setSize(400, 300);
                GlobalValues.toolbarFrame.setLocation(100, 100);
            }

            GlobalValues.toolbarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            GlobalValues.toolbarFrame.setVisible(true);

        }
    }

    private void loadTab(int n) {
        String title = uiTabbedPane.getTitleAt(n);

        Dimension dimTab = uiTabbedPane.getSize();
        GlobalValues.xSizeTab = dimTab.width;
        GlobalValues.ySizeTab = dimTab.height;

        switch (n) {
            case GlobalValues.mainTab:
                createExplorerPanel();
                createToolbarPanel();
                uiTabbedPane.setComponentAt(GlobalValues.mainTab, scalaPane);

                break;

            case GlobalValues.javaHelpTab: // Java help
                try {
                    try {
                        // Java help
                        Desktop.getDesktop().browse(new URI(JavaHelpStr));
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(scalaLab.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(scalaLab.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case GlobalValues.scalaHelpTab: // Scala help
                try {
                    try {
                        // Scala help
                        Desktop.getDesktop().browse(new URI(ScalaHelpStr));
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(scalaLab.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(scalaLab.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

            case GlobalValues.scalaSciTab:
                //  JOptionPane.showMessageDialog(null, "To install a toolbox please place it in the \"DefaultToolboxes\" folder and restart ScalaLab.  To remove delete it",
                //          "Installing Toolboxes", JOptionPane.INFORMATION_MESSAGE);

                JScrollPane ScalaSciToolboxesPane = new JScrollPane(scalaSciToolboxes.handleScalaSciTab());
                uiTabbedPane.setComponentAt(GlobalValues.scalaSciTab, ScalaSciToolboxesPane);
                break;

            default:
                break;
        }

    }

    // displays graphically the user paths used at the classpath of the Scala Interpreter with a JTree Swing component,
    // as well the whole classpath of the Scala interpreter with a JList Swing component
    static public void constructPathPresentationPanel() {
        scalaSciScriptsPathsTree paramTree = new scalaSciScriptsPathsTree();

        paramTree.buildVariablesTree();

        GlobalValues.pathPresentationPanel.removeAll();

        GlobalValues.pathPresentationPanel.setLayout(new GridLayout(1, 2));

        GlobalValues.pathPresentationPanel.add(paramTree);
        if (GlobalValues.globalInterpreter != null) {
            JScrollPane pathsPane = buildListOfMainScalaInterpreterClassPathComponents();
            GlobalValues.pathPresentationPanel.add(pathsPane);
        }

        if (paneInited && explorerPanel != null) {
            GlobalValues.scalalabMainFrame.pathsList.repaint();
        }
    }

    public static JScrollPane buildListOfMainScalaInterpreterClassPathComponents() {
        if (GlobalValues.interpreterClassPathComponents == null) {
            GlobalValues.interpreterClassPathComponents = new Vector<String>();
        }

        scala.collection.immutable.List<URL> globalInterpreterClassPath = GlobalValues.globalInterpreter.compilerClasspath().toList();

        int listSize = globalInterpreterClassPath.size();
        for (int k = 0; k < listSize; k++) {
            URL urlFile = globalInterpreterClassPath.apply(k);
            String url = urlFile.getFile();
            if (url.charAt(0) == '/' && File.pathSeparatorChar == ';') // for Windows only
            {
                url = url.substring(1, url.length());
            }
            if ((url.contains("jre") == false) && (url.contains("jdk") == false)) // not a standard Java library
            {
                if (GlobalValues.interpreterClassPathComponents.contains(url) == false) {
                    GlobalValues.interpreterClassPathComponents.add(url);
                }
            }
        }

        pathsList = new JList(GlobalValues.interpreterClassPathComponents);
        pathsList.setFont(GlobalValues.guifont);
        pathsList.setToolTipText("The current classpath used by the main Scala Interpreter. This is saved upon exit for restoration at the next session.");
        pathsList.addListSelectionListener((ListSelectionEvent e) -> {
            String selValue = pathsList.getSelectedValue().toString();

            if (selValue.endsWith(".jar")) {

                String jarFileSelected = selValue;
                Vector toolboxClasses = null;

                if (GlobalValues.displayedToolboxes.contains(jarFileSelected) == false) {    // not displayed  toolbox
                    GlobalValues.displayedToolboxes.add(jarFileSelected);
                    try {
                        GlobalValues.ScalaToolboxesLoader = new JarClassLoader();  // instantiate a .jar ClassLoader
                        GlobalValues.ScalaToolboxesLoader.extendClassPath(jarFileSelected);
                        toolboxClasses = GlobalValues.ScalaToolboxesLoader.scanAllJarClasses(jarFileSelected);
                    } catch (java.io.IOException ioe) {
                        System.out.println("IO Exception in reading from " + jarFileSelected);
                    }

                    boolean retrieveMethodsSetup = GlobalValues.retrieveAlsoMethods;
                    GlobalValues.retrieveAlsoMethods = true;

                    WatchClasses watchClasses = new WatchClasses();
                    watchClasses.displayClasses(toolboxClasses, jarFileSelected, 100, 100);
                    GlobalValues.retrieveAlsoMethods = retrieveMethodsSetup;
                }  // not displayed toolbox
            } // ends with .jar
            else {
                File elemFile = new File(selValue);
                if (elemFile.exists()) {
                    try {
                        FileTreeExplorer currentJPathTree = new FileTreeExplorer(selValue);
                        GlobalValues.currentFileExplorer = currentJPathTree;
                        JFrame explorerFrame = new JFrame("Browsing " + currentJPathTree);
                        explorerFrame.setSize(600, 600);
                        explorerFrame.setLocation(200, 200);
                        JScrollPane classesPane = new JScrollPane(currentJPathTree.pathsTree);
                        explorerFrame.add(classesPane);
                        explorerFrame.setVisible(true);
                    } catch (FileNotFoundException ex) {
                        System.out.println("File: " + selValue + " not found in scalaLabExplorer");
                        ex.printStackTrace();
                    }
                }    // file exists
            }
        } // this action listener permits the user to select a .jar file used as a toolbox and to have its contents displayed
        // in terms of the classes and methods that the toolbox contains
        );

        JScrollPane listScrollPane = new JScrollPane(pathsList);
        listScrollPane.setVisible(true);

        return listScrollPane;
    }

    public static void updateTree() {
        SwingUtilities.invokeLater(() -> {
            // run in  */
            if (explorerPanel != null) {
                explorerPanel.removeAll();
                explorerPanel.buildClassScriptPathsTree();
                explorerPanel.revalidate();
            }
        });
    }

    // the console initializer
    public void InitConsole() {
        String bufferedLineMode = ",  Line Input Mode, ";
        if (GlobalValues.commandLineModeOn == false) {
            bufferedLineMode = ",  Buffered Input Mode, ";
        }

        int xSize = GlobalValues.scalalabMainFrame.getSize().width;


        GlobalValues.consoleOutputWindow = new SysUtils.ConsoleWindow();

        Font consoleFont = GlobalValues.consoleOutputWindow.output.getFont();
        int pts = consoleFont.getSize();

        GlobalValues.consoleCharsPerLine = (int) (0.8 * (xSize / pts));

    }

    /**
     * The tabbed toolbars initializer.
     */
    public void InitTabbedToolbars() {

        scalalabConsole = new scalaExec.gui.scalalabConsole();
        scalaExec.Interpreter.GlobalValues.userConsole = scalalabConsole;

        GlobalValues.mainStatusPanel = new JPanel();

        tabbedToolbars = new JTabbedPane();
        GlobalValues.scalaSciTabbedToolBar = tabbedToolbars;

        RichDouble2DScalaOperationsToolbar richDoubleDoubleOpsToolbar = new RichDouble2DScalaOperationsToolbar();
        BasicScalaControlOperationsToolbar basicScalaOpsToolbar = new BasicScalaControlOperationsToolbar();
        MatScalaOperationsToolbar matScalaOpsToolbar = new MatScalaOperationsToolbar();
        VecScalaOperationsToolbar vecScalaOpsToolbar = new VecScalaOperationsToolbar();
        RichDouble1DArrayScalaOperationsToolbar RichDouble1DArrayScalaOpsToolbar = new RichDouble1DArrayScalaOperationsToolbar();
        MatrixScalaOperationsToolbar matrixScalaOpsToolbar = new MatrixScalaOperationsToolbar();
        DoubleDoubleScalaOperationsToolbar doubleDoubleScalaOpsToolbar = new DoubleDoubleScalaOperationsToolbar();
        PlotOperationsToolbar plotOpsToolbar = new PlotOperationsToolbar();

        LAScalaToolbar laOpsToolbar = new LAScalaToolbar();
        laOpsToolbar.setToolTipText("Linear Algebra (mainly examples from NUMAL");

        WaveletScalaToolbar waveletScalaToolbar = new WaveletScalaToolbar();
        waveletScalaToolbar.setToolTipText("Scala Wavelet Analysis Toolbar. At initial state of development yet!!");
        OptimizationScalaToolbar optimizationScalaToolbar = new OptimizationScalaToolbar();
        optimizationScalaToolbar.setToolTipText("Scala Wizards for performing numerical optimization tasks.  At initial state of development yet!!");
        ODEScalaToolbar odeScalaToolbar = new ODEScalaToolbar();
        odeScalaToolbar.setToolTipText("Scala Wizards for performing ODE numerical solving tasks.  At initial state of development yet!!");
        PDEScalaToolbar pdeScalaToolbar = new PDEScalaToolbar();
        pdeScalaToolbar.setToolTipText("Partial Differential Equations solving tasks");
        SpecialFunctionsToolbar specialFunctionsToolbar = new SpecialFunctionsToolbar();
        specialFunctionsToolbar.setToolTipText("Special Functions");

        CalculusScalaToolbar calculusScalaToolbar = new CalculusScalaToolbar();
        calculusScalaToolbar.setToolTipText("Scala Wizards for performing Calculus.  At initial state of development yet!!");

        tabbedToolbars.setToolTipText("The Scala toolbar. ");

        int toolbarCnt = 0;
        tabbedToolbars.addTab("Vec", vecScalaOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "1-D Vector Operations (class Vec)");
        tabbedToolbars.addTab("RichDoubleDouble", richDoubleDoubleOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Wraps and extends 2-D Double Arrays, i.e. Array[Array[Double]]");
        tabbedToolbars.addTab("Matrix", matrixScalaOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "One indexed  matrix,  i.e.. starts at M(1,1)  (class Matrix)m based on NUMAL library");
        if (GlobalValues.interpreterTypeForPane == GlobalValues.EJMLMat) {
            tabbedToolbars.addTab("EJMLMat", matScalaOpsToolbar);
            tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Zero indexed matrix based on EJML,  i.e.. starts at M(0,0),  (class Mat), support library can be switched, e.g. JAMA, EJML, MTJ");
        } else {
            tabbedToolbars.addTab("Mat", matScalaOpsToolbar);
            tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Zero indexed matrix,  i.e.. starts at M(0,0),  (class Mat), support library can be switched, e.g. JAMA, EJML, MTJ");
        }
        tabbedToolbars.addTab("RichDouble", RichDouble1DArrayScalaOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Wraps and extends 1-D Double Arrays, i.e. Array[Double]");
        tabbedToolbars.addTab("[[D]]", doubleDoubleScalaOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "2-D Array, i.e. Array[Array[Double]], aliased to AAD,  supports NUMAL based one-indexed routines");
        tabbedToolbars.addTab("Control", basicScalaOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Operations for controlling the environment");
        tabbedToolbars.addTab("Plot", plotOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "GUI for Plotting Functions ");
        tabbedToolbars.addTab("LinearAlgebra", laOpsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Linear Algrebra Functions");

        tabbedToolbars.addTab("Optimization", optimizationScalaToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Numerical Optimization of functions");
        tabbedToolbars.addTab("ODE", odeScalaToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Ordinary Differential Equations (ODEs) solvers");
        tabbedToolbars.addTab("PDE", pdeScalaToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Partial Differential Equations (PDEs) solvers");
        tabbedToolbars.addTab("SpecialFunctions", specialFunctionsToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Special Functions toolbar");
        tabbedToolbars.addTab("Calculus", calculusScalaToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Calculus utility routines, e.g. numerical integration, differentiation");
        tabbedToolbars.addTab("Wavelet", waveletScalaToolbar);
        tabbedToolbars.setToolTipTextAt(toolbarCnt++, "Wavelet toolbox interface");

// create Undocked
        basicScalaOpsToolbar = new BasicScalaControlOperationsToolbar();
        matScalaOpsToolbar = new MatScalaOperationsToolbar();
        vecScalaOpsToolbar = new VecScalaOperationsToolbar();
        RichDouble1DArrayScalaOpsToolbar = new RichDouble1DArrayScalaOperationsToolbar();
        matrixScalaOpsToolbar = new MatrixScalaOperationsToolbar();
        doubleDoubleScalaOpsToolbar = new DoubleDoubleScalaOperationsToolbar();
        plotOpsToolbar = new PlotOperationsToolbar();

        laOpsToolbar = new LAScalaToolbar();
        laOpsToolbar.setToolTipText("Linear Algebra");

        waveletScalaToolbar = new WaveletScalaToolbar();
        waveletScalaToolbar.setToolTipText("Scala Wavelet Analysis Toolbar. At initial state of development yet!!");
        optimizationScalaToolbar = new OptimizationScalaToolbar();
        optimizationScalaToolbar.setToolTipText("Scala Wizards for performing numerical optimization tasks.  At initial state of development yet!!");
        odeScalaToolbar = new ODEScalaToolbar();
        odeScalaToolbar.setToolTipText("Scala Wizards for performing ODE numerical solving tasks.  At initial state of development yet!!");
        pdeScalaToolbar = new PDEScalaToolbar();
        pdeScalaToolbar.setToolTipText("Partial Differential Equations solving tasks");
        specialFunctionsToolbar = new SpecialFunctionsToolbar();
        specialFunctionsToolbar.setToolTipText("Special Functions");

        calculusScalaToolbar = new CalculusScalaToolbar();
        calculusScalaToolbar.setToolTipText("Scala Wizards for performing Calculus.  At initial state of development yet!!");

        GlobalValues.scalaSciTabbedToolBar = tabbedToolbars;

    }

    /**
     * The menu initializer
     *
     * @param listener.
     */
    public void InitJMenuBar(final ActionListener listener) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // run in  */
                mainJMenuBar = new JMenuBar();
                FileMenu = new JMenu("File");
                FileMenu.setMnemonic('F');
                FileMenu.setToolTipText("File editing, Command history, Variable workspace operations");
                FileMenu.setFont(GlobalValues.uifont);
                editMenu = new JMenu("Edit");
                editMenu.setFont(GlobalValues.uifont);
                editMenu.setToolTipText("The ScalaLab specialized editor and the jEdit general text editor are supported");
                saveEditorTextJMenuItem = new JMenuItem("Save Editor Text ");
                saveEditorTextJMenuItem.addActionListener(new saveEditorTextAction());
                saveEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
                saveEditorTextJMenuItem.setToolTipText("Save the current text buffer of the editor to the currently edited file");
                saveEditorTextJMenuItem.setFont(GlobalValues.uifont);
                saveAsEditorTextJMenuItem = new JMenuItem("Save As Editor Text to File");
                saveAsEditorTextJMenuItem.addActionListener(new saveAsEditorTextAction());
                saveAsEditorTextJMenuItem.setToolTipText("Save the current text buffer, asking always for the file. Keeps the file name at the Recent Files list");
                saveAsEditorTextJMenuItem.setFont(GlobalValues.uifont);
                loadEditorTextJMenuItem = new JMenuItem("Load  File to Editor");
                loadEditorTextJMenuItem.addActionListener(new loadEditorTextAction());
                loadEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
                loadEditorTextJMenuItem.setToolTipText("Loads a new file to editor, confirming if contents of the editor have been modified");
                loadEditorTextJMenuItem.setFont(GlobalValues.uifont);
                displayCurrentAbbreviationsJMenuItem = new JMenuItem("Display current abbreviations");
                displayCurrentAbbreviationsJMenuItem.setToolTipText("Displays the currently defined abbreviations from the file Abbreviations.txt. Use TAB to expand an abbreviation");
                displayCurrentAbbreviationsJMenuItem.setFont(GlobalValues.uifont);
                displayCurrentAbbreviationsJMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        scalainterpreter.AbbreviationHandler.displayAbbreviations();
                    }
                });
                
                displayScalaSciVariables = new JMenuItem("Display current ScalaSci Variables");
                displayScalaSciVariables.setToolTipText("Display names, sizes, types and values of the current scalaSci variables in the workspace");
                displayScalaSciVariables.setFont(GlobalValues.uifont);
                displayScalaSciVariables.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
                
                displayScalaSciVariables.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {  // run in  EDT context */
                                
                                scalaSciCommands.WatchInterpreterState.printUserNamesAndValues();
                            }
                        });
                    }
                });
                JMenuItem tableDisplayScalaSciVariables = new JMenuItem("Display with table current ScalaSci Variables");
                tableDisplayScalaSciVariables.setToolTipText("Display names, sizes, types and values of the current scalaSci variables in the workspace");
                tableDisplayScalaSciVariables.setFont(GlobalValues.uifont);
                tableDisplayScalaSciVariables.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
                tableDisplayScalaSciVariables.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {  // run in  EDT context */
                                
                                scalaSciCommands.WatchInterpreterState.displayUserNamesAndValues();
                            }
                        });
                    }
                });
                rsyntaxAreaEditJMenuItem = new JMenuItem("RSyntaxArea based programmer's editor - An alternative editor to jsyntaxPane");
                rsyntaxAreaEditJMenuItem.addActionListener(new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        GlobalValues.globalRSyntaxFrame = (new rsyntaxEditor("Untitled", true)).currentFrame;
                        scalaExec.Interpreter.GlobalValues.editorPane.setCaretPosition(0);
                    }
                });
                rsyntaxAreaEditJMenuItem.setToolTipText("Editor based on rsyntaxArea component, very stable editor,  supports Scala code-completion");
                rsyntaxAreaEditJMenuItem.setFont(new Font(GlobalValues.uiFontName, Font.BOLD, Integer.parseInt(GlobalValues.uiFontSize)));
                rsyntaxAreaEditJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
                JMenuItem jeditJMenuItem1 = new JMenuItem(new jeditAction());
                jeditJMenuItem1.setToolTipText("Open a jedit instance that provides support for Scala editing");
                jeditJMenuItem1.setMnemonic('J');
                jeditJMenuItem1.setAccelerator(KeyStroke.getKeyStroke("ctrl J"));
                jeditJMenuItem1.setFont(GlobalValues.uifont);
             
                exitJMenuItem = new JMenuItem("Exit");
                exitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        closeGUI();
                    }
                });
                exitJMenuItem.setFont(GlobalValues.uifont);
                editMenu.setFont(GlobalValues.uifont);
                editMenu.add(rsyntaxAreaEditJMenuItem);
                editMenu.add(jeditJMenuItem1);
                editMenu.add(displayScalaSciVariables);
                editMenu.add(tableDisplayScalaSciVariables);
                   editMenu.add(displayCurrentAbbreviationsJMenuItem);
                FileMenu.add(saveEditorTextJMenuItem);
                FileMenu.add(saveAsEditorTextJMenuItem);
                FileMenu.add(loadEditorTextJMenuItem);
                FileMenu.add(exitJMenuItem);
                JMenuItem helpSwingScalaConsoleItem = new JMenuItem("Help for the basic commands of ScalaSwingConsole based on ScalaInterpreterPane of Sciss");
                helpSwingScalaConsoleItem.setFont(GlobalValues.uifont);
                helpSwingScalaConsoleItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        EditorPaneHTMLHelp inPlaceHelpPane = new EditorPaneHTMLHelp("EditorPaneBasicCommands.html");
                        if (GlobalValues.useSystemBrowserForHelp == false) {
                            inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                            inPlaceHelpPane.setLocation(GlobalValues.sizeX / 4, GlobalValues.sizeY / 4);
                            inPlaceHelpPane.setVisible(true);
                        }
                    }
                });
                JMenu compileMenu = new JMenu("Compile");
                compileMenu.setToolTipText("Compiles complete Java and Scala files (i.e. not scripts) and executes them");
                compileMenu.setFont(GlobalValues.uifont);
                JMenuItem compileExecuteScalaJMenuItem = new JMenuItem(new CompileExecuteActionScala());
                compileExecuteScalaJMenuItem.setFont(GlobalValues.uifont);
                compileExecuteScalaJMenuItem.setToolTipText("Compiles and executes the Scala object in the jsyntaxPane editor ");
                JMenuItem compileExecuteExternalJavaJMenuItem = new JMenuItem(new CompileExecuteExternalActionJava());
                compileExecuteExternalJavaJMenuItem.setFont(GlobalValues.uifont);
                JMenuItem transformStandAloneJMenuItem = new JMenuItem("Transform a Script to standalone application");
                transformStandAloneJMenuItem.setFont(GlobalValues.uifont);
                transformStandAloneJMenuItem.setToolTipText("Transforms the script to a Scala application that we can compile and  run as standalone");
                transformStandAloneJMenuItem.addActionListener(new StandAloneApplicationActionScala());
                compileMenu.add(compileExecuteScalaJMenuItem);
                compileMenu.add(compileExecuteExternalJavaJMenuItem);
                compileMenu.add(transformStandAloneJMenuItem);
                JMenu importWizardMenu = new JMenu("Imports");
                importWizardMenu.setToolTipText("Injects the proper import statements to support the corresponding functionality");
                importWizardMenu.setFont(GlobalValues.uifont);
                JMenuItem basicPlotsImportJMenuItem = new JMenuItem("Basic Plots Imports");
                basicPlotsImportJMenuItem.setFont(GlobalValues.uifont);
                basicPlotsImportJMenuItem.setToolTipText("Injects a statement for importing the JMathPlot based routines");
                basicPlotsImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectBasicPlots();
                });
                
                importWizardMenu.add(basicPlotsImportJMenuItem);
                JMenuItem basicPlotsDirectlyImportJMenuItem = new JMenuItem("Basic Plots Imports Directly");
                basicPlotsDirectlyImportJMenuItem.setFont(GlobalValues.uifont);
                basicPlotsDirectlyImportJMenuItem.setToolTipText("Injects directlythe statements for the JMathPlot based routines");
                basicPlotsDirectlyImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectBasicPlotsDirectly();
                });
                
                importWizardMenu.add(basicPlotsDirectlyImportJMenuItem);
                JMenuItem ssMatImportJMenuItem = new JMenuItem("JAMA Matrix ScalaSci Imports");
                ssMatImportJMenuItem.setFont(GlobalValues.uifont);
                ssMatImportJMenuItem.setToolTipText("Injects a statement for importing the JAMA Matrix ScalaSci functionality");
                ssMatImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciDefaultMat();
                });
                
                importWizardMenu.add(ssMatImportJMenuItem);
                JMenuItem matImportDirectlyJMenuItem = new JMenuItem("JAMA Mat Imports: Directly");
                matImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                matImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the JAMA based Matrix ");
                matImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciDefaultMatDirectly();
                });
                
                importWizardMenu.add(matImportDirectlyJMenuItem);
                JMenuItem ejmlMatImportJMenuItem = new JMenuItem("EJML Matrix ScalaSci Imports");
                ejmlMatImportJMenuItem.setFont(GlobalValues.uifont);
                ejmlMatImportJMenuItem.setToolTipText("Injects a statement for importing the EJML Matrix ScalaSci functionality");
                ejmlMatImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciEJMLMat();
                });
                
                importWizardMenu.add(ejmlMatImportJMenuItem);
                JMenuItem ejmlmatImportDirectlyJMenuItem = new JMenuItem("EJML Mat Imports: Directly");
                ejmlmatImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                ejmlmatImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the EJML based Matrix ");
                ejmlmatImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciEJMLMatDirectly();
                });
                importWizardMenu.add(ejmlmatImportDirectlyJMenuItem);
                JMenuItem mtjMatImportJMenuItem = new JMenuItem("MTJ Matrix ScalaSci Imports");
                mtjMatImportJMenuItem.setFont(GlobalValues.uifont);
                mtjMatImportJMenuItem.setToolTipText("Injects a statement for importing the MTJ Matrix ScalaSci functionality");
                mtjMatImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciMTJMat();
                });
                
                importWizardMenu.add(mtjMatImportJMenuItem);
                JMenuItem mtjmatImportDirectlyJMenuItem = new JMenuItem("MTJ  Mat Imports: Directly");
                mtjmatImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                mtjmatImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the MTJ based Matrix ");
                mtjmatImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciMTJMatDirectly();
                });
                
                importWizardMenu.add(mtjmatImportDirectlyJMenuItem);
                JMenuItem commonMathsMatImportJMenuItem = new JMenuItem("Apache Common Maths Matrix ScalaSci Imports");
                commonMathsMatImportJMenuItem.setFont(GlobalValues.uifont);
                commonMathsMatImportJMenuItem.setToolTipText("Injects a statement for importing the Apache Common Maths Matrix ScalaSci functionality");
                commonMathsMatImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciCommonMathsMat();
                });
                
                importWizardMenu.add(commonMathsMatImportJMenuItem);
                JMenuItem apacheCommonsMatImportDirectlyJMenuItem = new JMenuItem("Apache Commons Mat Imports: Directly");
                apacheCommonsMatImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                apacheCommonsMatImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the Apache Commons based Matrix ");
                apacheCommonsMatImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectScalaSciCommonMathsMatDirectly();
                });
                
                importWizardMenu.add(apacheCommonsMatImportDirectlyJMenuItem);
                JMenuItem apacheCommonsLibsImportJMenuItem = new JMenuItem("Apache Commons Library Imports");
                apacheCommonsLibsImportJMenuItem.setFont(GlobalValues.uifont);
                apacheCommonsLibsImportJMenuItem.setToolTipText("Injects a statement for importing the Apache Commons library");
                apacheCommonsLibsImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectApacheCommons();
                });
                
                importWizardMenu.add(apacheCommonsLibsImportJMenuItem);
                JMenuItem apacheCommonsImportDirectlyJMenuItem = new JMenuItem("Apache Commons Library Imports: Directly");
                apacheCommonsImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                apacheCommonsImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the Apache Commons library");
                apacheCommonsImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectApacheCommonsDirectly();
                });
                
                importWizardMenu.add(apacheCommonsImportDirectlyJMenuItem);
                JMenuItem ioImportJMenuItem = new JMenuItem("Input/Output  Imports");
                ioImportJMenuItem.setFont(GlobalValues.uifont);
                ioImportJMenuItem.setToolTipText("Injects a statement for importing the Input/Output (e.g. Matlab .mat files, XML routines)");
                ioImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectIO();
                });
                
                importWizardMenu.add(ioImportJMenuItem);
                JMenuItem ioImportDirectlyJMenuItem = new JMenuItem("Input/Output  Imports: Directly");
                ioImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                ioImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the Input/Output (e.g. Matlab .mat files, XML routines)");
                ioImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectIODirectly();
                });
                
                importWizardMenu.add(ioImportDirectlyJMenuItem);
                JMenuItem numalImportJMenuItem = new JMenuItem("NUMAL library Imports");
                numalImportJMenuItem.setFont(GlobalValues.uifont);
                numalImportJMenuItem.setToolTipText("Injects a statement for importing the NUMAL library imports");
                numalImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectNumAl();
                });
                
                importWizardMenu.add(numalImportJMenuItem);
                JMenuItem numalImportDirectlyJMenuItem = new JMenuItem("NUMAL library Imports: Directly");
                numalImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                numalImportDirectlyJMenuItem.setToolTipText("Injects the statements for importing the NUMAL library imports");
                numalImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectNumAlDirectly();
                });
                
                importWizardMenu.add(numalImportDirectlyJMenuItem);
                JMenuItem javaSwingImportsJMenuItem = new JMenuItem("Java Swing Imports");
                javaSwingImportsJMenuItem.setFont(GlobalValues.uifont);
                javaSwingImportsJMenuItem.setToolTipText("Injects a statement for importing the Java Swing  imports");
                javaSwingImportsJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectJavaSwing();
                });
                
                importWizardMenu.add(javaSwingImportsJMenuItem);
                JMenuItem javaSwingDirectImportsJMenuItem = new JMenuItem("Java Swing Imports:  Directly");
                javaSwingDirectImportsJMenuItem.setFont(GlobalValues.uifont);
                javaSwingDirectImportsJMenuItem.setToolTipText("Injects directly the statements for importing the Java Swing  imports");
                javaSwingDirectImportsJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectDirectlyJavaSwing();
                });
                
                importWizardMenu.add(javaSwingDirectImportsJMenuItem);
                JMenuItem computerAlgebraImportJMenuItem = new JMenuItem("Computer Algebra Imports");
                computerAlgebraImportJMenuItem.setFont(GlobalValues.uifont);
                computerAlgebraImportJMenuItem.setToolTipText("Injects a statement for importing the symja based Computer Algebra system");
                computerAlgebraImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectComputerAlgebra();
                });
                
                importWizardMenu.add(computerAlgebraImportJMenuItem);
                JMenuItem computerAlgebraImportDirectlyJMenuItem = new JMenuItem("Computer Algebra Imports: Directly");
                computerAlgebraImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                computerAlgebraImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the symja based Computer Algebra system");
                computerAlgebraImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectComputerAlgebraDirectly();
                });
                
                importWizardMenu.add(computerAlgebraImportDirectlyJMenuItem);
                JMenuItem LAPACKImportJMenuItem = new JMenuItem("LAPACK Imports");
                LAPACKImportJMenuItem.setFont(GlobalValues.uifont);
                LAPACKImportJMenuItem.setToolTipText("Injects a statement for importing the LAPACK related routines");
                LAPACKImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectLAPACK();
                });
                
                importWizardMenu.add(LAPACKImportJMenuItem);
                JMenuItem LAPACKImportDirectlyJMenuItem = new JMenuItem("LAPACK Imports: directly");
                LAPACKImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                LAPACKImportDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the LAPACK related routines");
                LAPACKImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectLAPACKDirectly();
                });
                
                importWizardMenu.add(LAPACKImportDirectlyJMenuItem);
                JMenuItem jfreeChartImportJMenuItem = new JMenuItem("JFreeChart Imports");
                jfreeChartImportJMenuItem.setFont(GlobalValues.uifont);
                jfreeChartImportJMenuItem.setToolTipText("Injects a statement for importing the JFreeChart based plotting system (i.e. jplot, etc)");
                jfreeChartImportJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectJPlots();
                });
                importWizardMenu.add(jfreeChartImportJMenuItem);
                JMenuItem jfreeChartImportDirectlyJMenuItem = new JMenuItem("JFreeChart Imports: directly");
                jfreeChartImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                jfreeChartImportDirectlyJMenuItem.setToolTipText("Inject directly the statements for importing the JFreeChart based plotting system (i.e. jplot, etc)");
                jfreeChartImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectJPlotsDirectly();
                });
                
                importWizardMenu.add(jfreeChartImportDirectlyJMenuItem);
                JMenuItem LAPACKDirectlyJMenuItem = new JMenuItem("LAPACK Imports: Directly");
                LAPACKDirectlyJMenuItem.setFont(GlobalValues.uifont);
                LAPACKDirectlyJMenuItem.setToolTipText("Injects directly the statements for importing the LAPACK library");
                LAPACKDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectLAPACKDirectly();
                });
                importWizardMenu.add(LAPACKDirectlyJMenuItem);
                
                JMenuItem biojavaImportDirectlyJMenuItem = new JMenuItem("BioJava Imports: directly");
                biojavaImportDirectlyJMenuItem.setFont(GlobalValues.uifont);
                biojavaImportDirectlyJMenuItem.setToolTipText("Inject directly the statements for importing most useful Biojava classes");
                biojavaImportDirectlyJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.importHelper.injectBioJavaDirectly();
                });
                importWizardMenu.add(biojavaImportDirectlyJMenuItem);
                
                scalaInterpreterMenu = new JMenu("Scala Interpreter");
                scalaInterpreterMenu.setToolTipText("Starts instances of the Scala Interpreter configured with different llibraries and with the option of replaying previous commands. \"Fast\" versions use minimal imports for fast response");
                scalaInterpreterMenu.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetScalaInterpreterItem);
                resetScalaInterpreterNoImportsItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetScalaInterpreterNoImportsItem);
                scalaInterpreterMenu.add(resetScalaInterpreterEJMLItem);
                scalaInterpreterMenu.add(resetScalaInterpreterMTJItem);
                scalaInterpreterMenu.add(resetScalaInterpreterCommonMathsItem);
                scalaInterpreterMenu.add(resetScalaInterpreterItem);
                resetToolboxesScalaInterpreterItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetToolboxesScalaInterpreterItem);
                resetScalaInterpreterEJMLItem.setFont(GlobalValues.uifont);
                   resetScalaInterpreterJBLASItem.setFont(GlobalValues.uifont);
                resetScalaInterpreterD2Das1DItem.setFont(GlobalValues.uifont);
                resetScalaInterpreterCommonMathsItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetScalaInterpreterEJMLItem);
                scalaInterpreterMenu.add(resetScalaInterpreterJBLASItem);
                scalaInterpreterMenu.add(resetScalaInterpreterD2Das1DItem);
                scalaInterpreterMenu.add(resetScalaInterpreterCommonMathsItem);
                scalaInterpreterMenu.setFont(GlobalValues.uifont);
                resetScalaInterpreterMTJItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetScalaInterpreterMTJItem);
                resetScalaInterpreterItem.setFont(GlobalValues.uifont);
                resetReplayToolboxesScalaInterpreterItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetReplayToolboxesScalaInterpreterItem);
                resetReplayScalaInterpreterEJMLItem.setFont(GlobalValues.uifont);
                resetReplayScalaInterpreterCommonMathsItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetReplayScalaInterpreterEJMLItem);
                scalaInterpreterMenu.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetReplayScalaInterpreterCommonMathsItem);
                resetReplayScalaInterpreterMTJItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetReplayScalaInterpreterMTJItem);
                resetReplayScalaInterpreterItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(resetReplayScalaInterpreterItem);
                clearBufferOfPreviousCommandsScalaInterpreterItem.setFont(GlobalValues.uifont);
                scalaInterpreterMenu.add(clearBufferOfPreviousCommandsScalaInterpreterItem);
                JMenuItem cancelJMenuItem = new JMenuItem("Cancel Pending Tasks");
                cancelJMenuItem.setFont(GlobalValues.uifont);
                cancelJMenuItem.setToolTipText("Attempt  to cancel any pending computational threads");
                cancelJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.Interpreter.PendingThreads.cancelPendingThreads();
                });
                scalaInterpreterMenu.add(cancelJMenuItem);
                JMenuItem  interruptciforLoopJMenuItem = new JMenuItem("Interrupt cifor loop");
                interruptciforLoopJMenuItem.setFont(GlobalValues.uifont);
                interruptciforLoopJMenuItem.setToolTipText("Interrupt cifor loop");
                interruptciforLoopJMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
            scalaExec.Interpreter.GlobalValues.interruptcifor=true;
                    }
                });
                scalaInterpreterMenu.add(interruptciforLoopJMenuItem);
                
                closeAllFiguresJMenuItem.setFont(GlobalValues.uifont);
                closeAllFiguresJMenuItem.setToolTipText("Closes all displayed figures");
                closeAllFiguresJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaSci.math.plot.plot.closeAll();
                    JFplot.jFigure.jcloseAll();
                });
                
                closeAllFiguresJMenuItem.setMnemonic('C');
                toggleCommandRecordingFlagItem.setFont(GlobalValues.uifont);
                toggleCommandRecordingFlagItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.recordCommandsForReplayingFlag = !GlobalValues.recordCommandsForReplayingFlag;
                    if (GlobalValues.recordCommandsForReplayingFlag == true) {
                        toggleCommandRecordingFlagItem.setText("Command Recording state is ON");
                    } else {
                        toggleCommandRecordingFlagItem.setText("Command Recording state is OFF");
                    }
                });
                
                queryCurrentInterpreterOfPaneItem.setFont(GlobalValues.uifont);
                queryCurrentInterpreterOfPaneItem.addActionListener((ActionEvent e) -> {
                    String interpreterType = "JAMA";
                    if (GlobalValues.interpreterTypeForPane == GlobalValues.EJMLMat) {
                        interpreterType = "EJML";
                    } else if (GlobalValues.interpreterTypeForPane == GlobalValues.MTJMat) {
                        interpreterType = "MTJ";
                    } else if (GlobalValues.interpreterTypeForPane == GlobalValues.ApacheCommonMathsMat) {
                        interpreterType = "Apache Common Maths";
                    } else if (GlobalValues.interpreterTypeForPane == GlobalValues.JBLASMat) {
                        interpreterType = " JBLAS";
                    } else if (GlobalValues.interpreterTypeForPane==GlobalValues.D2Das1DMat) {
                        interpreterType="D2Das1D";
                    }
                    else if (GlobalValues.interpreterTypeForPane == GlobalValues.NotAnyImports) {
                        interpreterType = " Not Any Imports";
                    } else {
                        interpreterType = "Uknown type";
                    }
                    
                    JOptionPane.showMessageDialog(null, "Main Scala Interpreter is inititialized for " + interpreterType, "Type of Main scala Interpreter", JOptionPane.INFORMATION_MESSAGE);;
                });
                scalaInterpreterMenu.add(toggleCommandRecordingFlagItem);
                scalaInterpreterMenu.add(queryCurrentInterpreterOfPaneItem);
                JMenuItem displayTheBufferOfRecordCommandsMenuItem = new JMenuItem("Display the buffer of recorded commands");
                displayTheBufferOfRecordCommandsMenuItem.setFont(GlobalValues.uifont);
                displayTheBufferOfRecordCommandsMenuItem.addActionListener((ActionEvent e) -> {
                    JFrame prevCommandsFrame = new JFrame("Previous Commands");
                    int commandNum = GlobalValues.replayingBuffer.size();
                    prevCommandsFrame.setLayout(new GridLayout(commandNum, 1));
                    for (int k = 0; k < commandNum; k++) {
                        String command = GlobalValues.replayingBuffer.elementAt(k);
                        JTextArea commandArea = new JTextArea(command);
                        JScrollPane commandPane = new JScrollPane(commandArea);
                        prevCommandsFrame.add(commandPane);
                    }
                    prevCommandsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    prevCommandsFrame.setSize(500, 600);
                    prevCommandsFrame.setLocation(200, 200);
                    prevCommandsFrame.setVisible(true);
                });
                scalaInterpreterMenu.add(displayTheBufferOfRecordCommandsMenuItem);
                JMenuItem displayInstalledToolboxesJMenuItem = new JMenuItem("Display the currently installed toolboxes");
                displayInstalledToolboxesJMenuItem.setFont(GlobalValues.uifont);
                displayInstalledToolboxesJMenuItem.addActionListener(new displayCurrentToolboxesAction());
                scalaInterpreterMenu.add(displayInstalledToolboxesJMenuItem);
                resetToolboxesScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
                });
                
                resetScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
                    
                });
                
                resetScalaInterpreterNoImportsItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterWithNoImports();
                    
                });
                
                resetScalaInterpreterJBLASItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetJBLAS();
                    
                });
                
                resetScalaInterpreterEJMLItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetEJML();
                    
                });

                resetScalaInterpreterD2Das1DItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements ();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetD2Das1D();
                });

                resetScalaInterpreterMTJItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetMTJ();
                });
                
                resetScalaInterpreterCommonMathsItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetCommonMaths();
                });
                
                clearAllScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForClearAll();
                });
                
                resetReplayToolboxesScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
                    replayBuffer();
                });
                
                resetReplayScalaInterpreterEJMLItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetEJML();
                    replayBuffer();
                });
                
                resetReplayScalaInterpreterCommonMathsItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetCommonMaths();
                    replayBuffer();
                });
                
                resetReplayScalaInterpreterMTJItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForResetMTJ();
                    replayBuffer();
                });
                
                resetReplayScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(GlobalValues.ScalaSciClassPathComponents);
                    replayBuffer();
                });
                
                clearBufferOfPreviousCommandsScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.replayingBuffer.removeAllElements();
                });
                
                clearReplayAllScalaInterpreterItem.addActionListener((ActionEvent e) -> {
                    
                    GlobalValues.scalalabMainFrame.scalalabConsole.createInterpreterForClearAll();
                    replayBuffer();
                });
                
                JMenu UtilitiesMenu = new JMenu("Utilities");
                UtilitiesMenu.setFont(GlobalValues.uifont);
                
                JMenuItem searchJarContentsForImportJMenuItem = new JMenuItem("Search jar file for proper import statement");
                searchJarContentsForImportJMenuItem.setFont(GlobalValues.uifont);
                searchJarContentsForImportJMenuItem.addActionListener((ActionEvent e) -> {
                    javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
                    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
                    if (retVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        String jarFileToDisplay = selectedFile.getAbsolutePath();
                        String classToScan = JOptionPane.showInputDialog(null, "Input the class to scan");
                        
                        String proposedImports="";
                        try {
                            proposedImports = scalaExec.ClassLoaders.JarClassLoader.scanClass(jarFileToDisplay, classToScan);
                            scalaExec.Interpreter.GlobalValues.editorPane.setText(proposedImports+scalaExec.Interpreter.GlobalValues.editorPane.getText());
                            
                        } catch (IOException ex) {
                            System.out.println("exception "+ex.toString());
                        }
                        
                        
                    }
                });
                
                
                JMenuItem searchClassPathForImportJMenuItem = new JMenuItem("Search all classpath for injecting the proper import statements");
                searchClassPathForImportJMenuItem.setFont(GlobalValues.uifont);
                searchClassPathForImportJMenuItem.setToolTipText("Searches the whole classpath for classes matching the specified keyword, which then automatically imports");
                searchClassPathForImportJMenuItem.addActionListener((ActionEvent e) -> {
                    String classToScan = JOptionPane.showInputDialog(null, "Input the keyword to scan for matched classes, e.g.   eig.  to inject libraries implementing eigenvalue computations", "Keyword for importing matching classes", JOptionPane.INFORMATION_MESSAGE);
                    
                    scala.collection.immutable.List   <URL> allURLS  = GlobalValues.globalInterpreter.compilerClasspath().toList();
                    
                    scala.collection.Iterator<URL>  iURLS = allURLS.iterator();
                    
                    while (iURLS.hasNext()) {
                        URL jarFileToSearchURL = iURLS.next();
                        if (jarFileToSearchURL.getFile().isEmpty()==false) {
                            try {
                                String jarFileToSearch = jarFileToSearchURL.getPath();
                                
                                String proposedImports = scalaExec.ClassLoaders.JarClassLoader.scanClass(jarFileToSearch, classToScan);
                                scalaExec.Interpreter.GlobalValues.editorPane.setText(proposedImports+scalaExec.Interpreter.GlobalValues.editorPane.getText());
                                
                            } catch (IOException ex) {
                                System.out.println("exception "+ex.toString());
                            }
                        }
                        
                    }
                });
                
                
                JMenuItem displayJarContentsMenuItem = new JMenuItem("Display the contents of a jar file");
                displayJarContentsMenuItem.setFont(GlobalValues.uifont);
                displayJarContentsMenuItem.addActionListener((ActionEvent e) -> {
                    javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
                    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
                    if (retVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        String jarFileToDisplay = selectedFile.getAbsolutePath();
                        
                        Vector toolboxClasses = scalaExec.ClassLoaders.JarClassLoader.scanAll(jarFileToDisplay);
                        System.out.println("readed " + toolboxClasses.size() + "  classes");
                        scalaExec.gui.WatchClasses watchClassesOfToolbox = new scalaExec.gui.WatchClasses();
                        
                        watchClassesOfToolbox.displayClassesAndMethods(toolboxClasses, jarFileToDisplay, scalaExec.gui.WatchClasses.watchXLoc + 50, scalaExec.gui.WatchClasses.watchYLoc + 50);
                        
                    }
                }
                );
                
                UtilitiesMenu.add(displayJarContentsMenuItem);
                
                JMenuItem displayJarContentsClassesOnlyMenuItem = new JMenuItem("Display only the classes of a jar file");
                displayJarContentsClassesOnlyMenuItem.setFont(GlobalValues.uifont);
                displayJarContentsClassesOnlyMenuItem.addActionListener((ActionEvent e) -> {
                    javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
                    int retVal = chooser.showOpenDialog(GlobalValues.scalalabMainFrame);
                    if (retVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        String jarFileToDisplay = selectedFile.getAbsolutePath();
                        
                        Vector toolboxClasses = scalaExec.ClassLoaders.JarClassLoader.scanAll(jarFileToDisplay);
                        System.out.println("readed " + toolboxClasses.size() + "  classes");
                        scalaExec.gui.WatchClasses watchClassesOfToolbox = new scalaExec.gui.WatchClasses();
                        
                        watchClassesOfToolbox.displayOnlyClassNames(toolboxClasses, jarFileToDisplay, scalaExec.gui.WatchClasses.watchXLoc + 50, scalaExec.gui.WatchClasses.watchYLoc + 50);
                        
                    }
                }
                );
                
                UtilitiesMenu.add(displayJarContentsClassesOnlyMenuItem);
                
                JMenuItem matlabJMenuItem = new JMenuItem("Load Matlab .mat file");
                matlabJMenuItem.addActionListener(new matlabMatFileAction());
                matlabJMenuItem.setFont(GlobalValues.uifont);
                JMenuItem searchKeywordsJMenuItem = new JMenuItem("Search Libraries for keywords");
                searchKeywordsJMenuItem.setFont(GlobalValues.uifont);
                searchKeywordsJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaSci.Search.SearchLibs.GUISearchLibs();
                    
                });
                
                JMenuItem asciiJMenuItem = new JMenuItem("Load ASCII data file");
                asciiJMenuItem.addActionListener(new asciiFileAction());
                asciiJMenuItem.setFont(GlobalValues.uifont);
                UtilitiesMenu.add(searchJarContentsForImportJMenuItem);
                UtilitiesMenu.add(searchClassPathForImportJMenuItem);
                UtilitiesMenu.add(searchKeywordsJMenuItem);
                UtilitiesMenu.add(matlabJMenuItem);
                UtilitiesMenu.add(asciiJMenuItem);
                UtilitiesMenu.add(closeAllFiguresJMenuItem);
                appearanceMenu = new JMenu("Appearance", true);
                appearanceMenu.setFont(GlobalValues.uifont);
                appearanceMenu.setToolTipText("Configure fonts, appearance etc.  Many settings are saved upon exit for the next sessions");
                confMenu = new JMenu("Configuration", true);
                confMenu.setMnemonic('G');
                confMenu.setFont(GlobalValues.uifont);
                confMenu.setToolTipText("Configures Scala compiler, number formats  etc. Many settings are saved upon exit for the next sessions");
                
                

                
                libConfMenu = new JMenu("LibConfiguration", true);
                libConfMenu.setFont(GlobalValues.uifont);
                libConfMenu.setToolTipText("Configures parameters of ScalaLab libraries");
                final JMenuItem jsyntaxMouseMotionListenerOnJMenuItem = new JMenuItem("Toggle Mouse Motion triggerring on identifiers for jsyntax editor (current state is " + GlobalValues.mouseMotionListenerForJSyntax + " )");
                jsyntaxMouseMotionListenerOnJMenuItem.setToolTipText("Mouse motion listener allows displaying information for identifiers when the mouse cursor is over them");
                jsyntaxMouseMotionListenerOnJMenuItem.setFont(GlobalValues.uifont);
                jsyntaxMouseMotionListenerOnJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.mouseMotionListenerForJSyntax = !GlobalValues.mouseMotionListenerForJSyntax;
                    if (GlobalValues.mouseMotionListenerForJSyntax == true) {
                        GlobalValues.jsyntaxMouseMotionAdapter = new scalainterpreter.PaneMouseMotionAdapter();
                        scalaExec.Interpreter.GlobalValues.editorPane.addMouseMotionListener(GlobalValues.jsyntaxMouseMotionAdapter);
                    } else {
                        scalaExec.Interpreter.GlobalValues.editorPane.removeMouseMotionListener(GlobalValues.jsyntaxMouseMotionAdapter);
                    }
                    
                    jsyntaxMouseMotionListenerOnJMenuItem.setText("Toggle Mouse Motion triggerring on identifiers for jsyntax editor (current state is " + GlobalValues.mouseMotionListenerForJSyntax + " )");
                });
                
                final JMenuItem rsyntaxMouseMotionListenerOnJMenuItem = new JMenuItem("Toggle Mouse Motion triggerring on identifiers for rsyntax editor (current state is " + GlobalValues.mouseMotionListenerForRSyntax + " )");
                rsyntaxMouseMotionListenerOnJMenuItem.setToolTipText("Mouse motion listener allows displaying information for identifiers when the mouse cursor is over them");
                rsyntaxMouseMotionListenerOnJMenuItem.setFont(GlobalValues.uifont);
                rsyntaxMouseMotionListenerOnJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.mouseMotionListenerForRSyntax = !GlobalValues.mouseMotionListenerForRSyntax;
                    if (GlobalValues.globalRSyntaxEditorPane != null) {
                        if (GlobalValues.mouseMotionListenerForRSyntax == true) {
                            GlobalValues.rsyntaxMouseMotionAdapter = new rsyntaxEdit.RSyntaxEditorMouseMotionAdapter();
                            scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.addMouseMotionListener(GlobalValues.rsyntaxMouseMotionAdapter);
                        } else {
                            scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.removeMouseMotionListener(GlobalValues.rsyntaxMouseMotionAdapter);
                        }
                    }
                    
                    rsyntaxMouseMotionListenerOnJMenuItem.setText("Toggle Mouse Motion triggerring on identifiers for rsyntax editor (current state is " + GlobalValues.mouseMotionListenerForRSyntax + " )");
                });
                
                final JMenuItem jsyntaxForAllValuesMouseMotionListenerOnJMenuItem = new JMenuItem("Displaying all values of  identifiers with mouse motion listener for jsyntax editor (current state is " + GlobalValues.getValuesForAllJSyntax + " )");
                jsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setToolTipText("Displaying information not only for controlling types can cause trouble when large Strings are returned by toString");
                jsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setFont(GlobalValues.uifont);
                jsyntaxForAllValuesMouseMotionListenerOnJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.getValuesForAllJSyntax = !GlobalValues.getValuesForAllJSyntax;
                    jsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setText("Displaying all values of  identifiers with mouse motion listener for jsyntax editor (current state is " + GlobalValues.getValuesForAllJSyntax + " )");
                });
                
                final JMenuItem rsyntaxForAllValuesMouseMotionListenerOnJMenuItem = new JMenuItem("Displaying all values of  identifiers with mouse motion listener for rsyntax editor (current state is " + GlobalValues.getValuesForAllRSyntax + " )");
                rsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setToolTipText("Displaying information not only for controlling types can cause trouble when large Strings are returned by toString");
                rsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setFont(GlobalValues.uifont);
                rsyntaxForAllValuesMouseMotionListenerOnJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.getValuesForAllRSyntax = !GlobalValues.getValuesForAllRSyntax;
                    rsyntaxForAllValuesMouseMotionListenerOnJMenuItem.setText("Displaying all values of  identifiers with mouse motion listener for rsyntax editor (current state is " + GlobalValues.getValuesForAllRSyntax + " )");
                });
                //ImageIcon configIcon = new ImageIcon(scalalabPathsConfigImage);
                varsConfJMenuItem = new JMenuItem("scalaLab Paths Configuration"); //, configIcon);
                varsConfJMenuItem.addActionListener(new scalalabScriptsPathsAction());
                varsConfJMenuItem.setFont(GlobalValues.uifont);
                controlMainToolbarJMenuItem = new JMenuItem(new controlMainToolBarAction());
                controlMainToolbarJMenuItem.setFont(GlobalValues.uifont);
                JMenuItem controlScalaCompilerJMenuItem1 = new JMenuItem(new controlScalaCompilerAction());
                controlScalaCompilerJMenuItem1.setFont(GlobalValues.uifont);
                controlScalaCompilerJMenuItem1.setToolTipText("Contols the Scala compiler JVM target (e.g. jvm-1.7) and optimization");
                controlExplorerJMenuItem = new JMenuItem(new explorerAction());
                controlExplorerJMenuItem.setToolTipText("We can perform with ScalaLab Explorer file operations, viewing current classpath, use ScalaLab Console");
                controlExplorerJMenuItem.setFont(GlobalValues.uifont);
                String defaultEditor = "jSyntaxPane";
                if (GlobalValues.preferRSyntaxEditor) {
                    defaultEditor = "RSyntaxArea";
                }
                final JMenuItem preferJSyntaxPaneJMenuItem = new JMenuItem("Prefer JSyntaxArea instead of jsyntaxpane as default editor, current setting is " + defaultEditor);
                preferJSyntaxPaneJMenuItem.setFont(GlobalValues.uifont);
                preferJSyntaxPaneJMenuItem.setToolTipText("Controls the default editor, JSyntaxArea vs jsyntaxpane");
                preferJSyntaxPaneJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.preferRSyntaxEditor = !GlobalValues.preferRSyntaxEditor;  // toggle setting
                    String defaultEditorLocal = "jSyntaxPane";
                    if (GlobalValues.preferRSyntaxEditor) {
                        defaultEditorLocal = "RSyntaxArea";
                    }
                    preferJSyntaxPaneJMenuItem.setText("Prefer JSyntaxArea instead of jsyntaxpane as default editor, current setting is " + defaultEditorLocal);
                });
                
                String watchVarsState = "ON";
                if (GlobalValues.watchVariablesFlag == false) {
                    watchVarsState = "OFF";
                }
                watchVariablesJMenuItem = new JMenuItem("Control watch variables state. Currently watch variables state is: " + watchVarsState);
                watchVariablesJMenuItem.setToolTipText("Controls whether to display the workspace variables with a JTable after each command execution with F6");
                watchVariablesJMenuItem.setFont(GlobalValues.uifont);
                watchVariablesJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.watchVariablesFlag = !GlobalValues.watchVariablesFlag;
                    String watchVarsStateLocal = "ON";
                    if (GlobalValues.watchVariablesFlag == false) {
                        watchVarsStateLocal = "OFF";
                    }
                    watchVariablesJMenuItem.setText("Control watch variables state. Currently watch variables state is: " + watchVarsStateLocal);
                    
                });
                
                JMenuItem controlPrecisionJMenuItem = new JMenuItem("Control  the format of displayed numbers and truncation of large matrices");
                controlPrecisionJMenuItem.setToolTipText("Controls verbose on/off, the precision displayed for floating point numbers, truncation of large matrices/vectors");
                controlPrecisionJMenuItem.setFont(GlobalValues.uifont);
                controlPrecisionJMenuItem.addActionListener(new controlPrecisionAction());
              
                paneAdjustFontMenuItem = new JMenuItem("Adjust Fonts of Interpreter Pane and ScalaLab edtor (good choices: DejaVu Sans Mono, Courier, Courier New, Monospaced)"); // configIcon);
                paneAdjustFontMenuItem.setToolTipText("The particular font used can be important, DejaVu Sans Mono, Courier, Courier New, Monospaced seem to perform well");
                paneAdjustFontMenuItem.addActionListener(new paneFontAdjusterAction());
                paneAdjustFontMenuItem.setFont(GlobalValues.uifont);
                JMenuItem increasePaneFontMenuItem = new JMenuItem("Increase the font size of the jsyntaxpane Scala Interpreter Pane");
                increasePaneFontMenuItem.setToolTipText("Increases the font size of the jsyntaxpane Scala Interpreter Pane");
                increasePaneFontMenuItem.setFont(GlobalValues.guifont);
                increasePaneFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, java.awt.event.InputEvent.ALT_DOWN_MASK));
                increasePaneFontMenuItem.addActionListener((ActionEvent e) -> {
                    // increase the font size of the jsyntaxpane Scala Interpreter Pane
                    Font currentFont = GlobalValues.editorPane.getFont();
                    Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() + 1);
                    GlobalValues.editorPane.setFont(newFont);
                    
                });
                
                JMenuItem decreasePaneFontMenuItem = new JMenuItem("Decrease the font size of the jsyntaxpane Scala Interpreter Pane");
                decreasePaneFontMenuItem.setToolTipText("Decreases the font size of the jsyntaxpane Scala Interpreter Pane");
                decreasePaneFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_DOWN_MASK));
                decreasePaneFontMenuItem.setFont(GlobalValues.guifont);
                decreasePaneFontMenuItem.addActionListener((ActionEvent e) -> {
                    Font currentFont = GlobalValues.editorPane.getFont();
                    Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() - 1);
                    GlobalValues.editorPane.setFont(newFont);
                });
                
                JMenuItem increaseRSyntaxFontMenuItem = new JMenuItem("Increase the font size of the rsyntaxarea editor");
                increaseRSyntaxFontMenuItem.setToolTipText("Increases the font size of the rsyntaxarea editor");
                increaseRSyntaxFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK));
                increaseRSyntaxFontMenuItem.setFont(GlobalValues.guifont);
                increaseRSyntaxFontMenuItem.addActionListener((ActionEvent e) -> {
                    // increase the font size of the rsyntaxarea editor
                    Font currentFont = GlobalValues.globalRSyntaxEditorPane.getFont();
                    Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() + 1);
                    GlobalValues.globalRSyntaxEditorPane.setFont(newFont);
                });
                
                JMenuItem decreaseRSyntaxFontMenuItem = new JMenuItem("Decrease the font size of the rsyntaxarea editor");
                decreaseRSyntaxFontMenuItem.setToolTipText("Decreases the font size of the rsyntaxarea editor");
                decreaseRSyntaxFontMenuItem.setFont(GlobalValues.guifont);
                decreaseRSyntaxFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK));
                decreaseRSyntaxFontMenuItem.addActionListener((ActionEvent e) -> {
                    // increase the font size of the rsyntaxarea editor
                    Font currentFont = GlobalValues.globalRSyntaxEditorPane.getFont();
                    Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize() - 1);
                    GlobalValues.globalRSyntaxEditorPane.setFont(newFont);
                });
                
                rsyntaxAreaAdjustFontMenuItem = new JMenuItem("Adjust Fonts of RSyntaxArea based editor (editor should be displayed for changes to affect) "); // configIcon);
                rsyntaxAreaAdjustFontMenuItem.addActionListener(new rsyntaxFontAdjusterAction());
                rsyntaxAreaAdjustFontMenuItem.setFont(GlobalValues.uifont);
                adjustFontMenuItem = new JMenuItem("Adjust Fonts of ScalaLab Console");
                adjustFontMenuItem.addActionListener(new FontAdjusterAction());
                adjustFontMenuItem.setFont(GlobalValues.uifont);
                adjustFontsUIMenuItem = new JMenuItem("Adjust Fonts of ScalaLab main menus and help menus (changes apply after restart)"); // configIcon);
                adjustFontsUIMenuItem.addActionListener(new UIFontAdjusterAction());
                adjustFontsUIMenuItem.setFont(GlobalValues.uifont);
                adjustFontspUIMenuItem = new JMenuItem("Adjust Fonts of ScalaLab popup menus (changes apply after restart)"); // configIcon);
                adjustFontspUIMenuItem.addActionListener(new pUIFontAdjusterAction());
                adjustFontspUIMenuItem.setFont(GlobalValues.uifont);
                adjustFontsgUIMenuItem = new JMenuItem("Adjust Fonts of ScalaLab GUI components, e.g. JTrees, JLists (changes apply after restart)"); // configIcon);
                adjustFontsgUIMenuItem.addActionListener(new gUIFontAdjusterAction());
                adjustFontsgUIMenuItem.setFont(GlobalValues.uifont);
                adjustHtmlFontsJMenuItem = new JMenuItem("Adjust JEditorPane based HTML Help Pages Fonts "); // configIcon);
                adjustHtmlFontsJMenuItem.addActionListener(new htmlFontAdjusterAction());
                adjustHtmlFontsJMenuItem.setFont(GlobalValues.uifont);
                adjustsFontsbUIMenuItem = new JMenuItem("Adjust Fonts for buttons/labels (changes apply after restart)"); // configIcon);
                adjustsFontsbUIMenuItem.addActionListener(new bUIFontAdjusterAction());
                adjustsFontsbUIMenuItem.setFont(GlobalValues.uifont);
                outConsoleAdjustFontMenuItem = new JMenuItem("Adjust Fonts of output Console"); // configIcon);
                outConsoleAdjustFontMenuItem.addActionListener(new outConsFontAdjusterAction());
                outConsoleAdjustFontMenuItem.setFont(GlobalValues.uifont);
                adjustLookAndFeelMenuItem = new JMenuItem("Configure Look and Feel"); //, configIcon);
                adjustLookAndFeelMenuItem.addActionListener(new LookAndFeelAdjusterAction());
                adjustLookAndFeelMenuItem.setFont(GlobalValues.uifont);
                htmlWithSystemBrowser = new JMenuItem("Use System Browser for HTML help, Current State is " + GlobalValues.useSystemBrowserForHelp);
                htmlWithSystemBrowser.setFont(GlobalValues.uifont);
                htmlWithSystemBrowser.addActionListener((ActionEvent e) -> {
                    GlobalValues.useSystemBrowserForHelp = !GlobalValues.useSystemBrowserForHelp;
                    htmlWithSystemBrowser.setText("Use System Browser for HTML help, Current State is " + GlobalValues.useSystemBrowserForHelp);
                });
                JMenuItem htmlMagFactorJMenuItem = new JMenuItem("Adjust default magnification factor for  JEditorPane based HTML help");
                htmlMagFactorJMenuItem.setFont(GlobalValues.uifont);
                htmlMagFactorJMenuItem.addActionListener(new HTMLMagAdjustAction());
                browseFileSysForPathsJMenuItem = new JMenuItem("Browse File system For Paths");
                browseFileSysForPathsJMenuItem.addActionListener(new browseFileSysForPaths());
                browseFileSysForPathsJMenuItem.setFont(GlobalValues.uifont);
                browseClassesJMenuItem = new JMenuItem(new browseJavaClassesAction());
                browseClassesJMenuItem.setFont(GlobalValues.uifont);
                promptJMenuItem = new JMenuItem(new promptConfigAction());
                promptJMenuItem.setFont(GlobalValues.uifont);
                JMenuItem presentStartupHelpJMenuItem = new JMenuItem("Toggle presentation of startup help. Current state is " + GlobalValues.startupHelpFlag);
                presentStartupHelpJMenuItem.setFont(GlobalValues.uifont);
                presentStartupHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.startupHelpFlag = !GlobalValues.startupHelpFlag;
                    JMenuItem jmi = (JMenuItem) e.getSource();
                    jmi.setText("Toggle presentation of startup help. Current state is " + GlobalValues.startupHelpFlag);
                });
                JMenuItem resetToolboxClassesFrameJMenuItem = new JMenuItem("Reset information on displayed toolboxes");
                resetToolboxClassesFrameJMenuItem.setFont(GlobalValues.uifont);
                resetToolboxClassesFrameJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.displayedToolboxes.clear();
                    System.out.println("\n\n Displayed Toolboxes information cleared. You can reopen toolbox windows as required \n\n");
                });
                JMenuItem scalaSciRoutinesMenuItem = new JMenuItem("ScalaSci  routines");
                scalaSciRoutinesMenuItem.setToolTipText("Display information using reflection for all the ScalaSci classes and methods");
                scalaSciRoutinesMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(scalaSciRoutinesMenuItem);
                scalaSciRoutinesMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector scalaSciPlottingClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "scalaSci");
                    
                    int k = 1;
                    watchClassesOfScalaSci.displayClassesAndMethods(scalaSciPlottingClasses, "ScalaSci Classes", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                JMenuItem scalaSciGraphicsRoutinesMenuItem = new JMenuItem("ScalaSci  Plotting routines");
                scalaSciGraphicsRoutinesMenuItem.setToolTipText("Display information using reflection for the ScalaSci plotting classes and methods");
                scalaSciGraphicsRoutinesMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(scalaSciGraphicsRoutinesMenuItem);
                scalaSciGraphicsRoutinesMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector scalaSciPlottingClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "scalaSci/math/plot");
                    
                    int k = 1;
                    watchClassesOfScalaSci.displayClassesAndMethods(scalaSciPlottingClasses, "ScalaSci Plotting Classses", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem ejmlConfig = new JMenuItem("EJML Library Configuration");
                ejmlConfig.setFont(GlobalValues.uifont);
                ejmlConfig.setToolTipText("Settings that can affect performance of the EJML library");
                ejmlConfig.addActionListener((ActionEvent e) -> {
                    new scalaSci.EJML.ejmlConfigFrame();
                    
                });
                
                JMenuItem ejmlRoutinesMenuItem = new JMenuItem("EJML  routines");
                ejmlRoutinesMenuItem.setToolTipText("Display information using reflection for the EJML library classes and methods");
                ejmlRoutinesMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(ejmlRoutinesMenuItem);
                ejmlRoutinesMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfEJML = new scalaExec.gui.WatchClasses();
                    
                    Vector EJMLClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ejmlFile, "org");
                    
                    int k = 1;
                    watchClassesOfEJML.displayClassesAndMethods(EJMLClasses, "EJML Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                libConfMenu.add(ejmlConfig);
                JMenuItem numalMenuItem = new JMenuItem("NUMAL routines");
                numalMenuItem.setToolTipText("Display information using reflection for the NUMAL classes and methods");
                numalMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(numalMenuItem);
                numalMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfNRNumAl = new scalaExec.gui.WatchClasses();
                    
                    Vector numalClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "numal");
                    
                    int k = 1;
                    watchClassesOfNRNumAl.displayClassesAndMethods(numalClasses, "NUMAL", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                    
                });
                JMenuItem jlapackMenuItem = new JMenuItem("JLAPACK routines");
                jlapackMenuItem.setToolTipText("Display information using reflection for the JLAPACK  classes and methods");
                jlapackMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(jlapackMenuItem);
                jlapackMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJLAPACK = new scalaExec.gui.WatchClasses();
                    
                    Vector numalClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.LAPACKFile, "org");
                    
                    int k = 1;
                    watchClassesOfJLAPACK.displayClassesAndMethods(numalClasses, "JLAPACK", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                    
                });
                JMenuItem jsciMenuItem = new JMenuItem("JSci  routines (contains Wavelet library, plotting, statistical routines)");
                jsciMenuItem.setToolTipText("Display information using reflection for the JSci classes and methods");
                jsciMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(jsciMenuItem);
                jsciMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJSci = new scalaExec.gui.WatchClasses();
                    
                    Vector JSciClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jsciFile, "JSci");
                    
                    int k = 1;
                    watchClassesOfJSci.displayClassesAndMethods(JSciClasses, "JSci Library Routines", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                    
                });
                
                JMenuItem nrMenuItem = new JMenuItem("Numerical Recipes routines");
                nrMenuItem.setToolTipText("Display information using reflection for the Numerical Recipes classes and methods");
                nrMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(nrMenuItem);
                nrMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfNRNumAl = new scalaExec.gui.WatchClasses();
                    
                    Vector NRNumALClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "com");
                    
                    int k = 1;
                    watchClassesOfNRNumAl.displayClassesAndMethods(NRNumALClasses, "Numerical Recipes", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                    
                });
                
                JMenuItem mtjMenuItem = new JMenuItem("Matrix Toolkit for Java  routines");
                mtjMenuItem.setToolTipText("Display information using reflection for the MTJ classes and methods");
                mtjMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(mtjMenuItem);
                mtjMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJSci = new scalaExec.gui.WatchClasses();
                    
                    Vector mtjClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "no");
                    
                    int k = 1;
                    watchClassesOfJSci.displayClassesAndMethods(mtjClasses, "MTJ Library Routines", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem coltMenuItem = new JMenuItem("CERN Colt  routines");
                coltMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Colt Linear Algebra Library of CERN ");
                coltMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(coltMenuItem);
                coltMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfNRNumAl = new scalaExec.gui.WatchClasses();
                    
                    Vector NRNumALClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "cern");
                    
                    int k = 1;
                    watchClassesOfNRNumAl.displayClassesAndMethods(NRNumALClasses, "Colt  Linear Algebra Library of CERN", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem sgtMenuItem = new JMenuItem("Scientific Graphics Toolbox (SGT) library routines");
                sgtMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Scientific Graphics Toolbox (SGT) library ");
                sgtMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(sgtMenuItem);
                sgtMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfSGT = new scalaExec.gui.WatchClasses();
                    
                    Vector SGTClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "gov");
                    
                    int k = 1;
                    watchClassesOfSGT.displayClassesAndMethods(SGTClasses, "Scientific Graphics Toolbox (SGT) Graphics Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem csparseMenuItem = new JMenuItem("CSparse library routines");
                csparseMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the CSparse library  for sparse matrices");
                csparseMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(csparseMenuItem);
                csparseMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfCSparse = new scalaExec.gui.WatchClasses();
                    
                    String csparseFile = JavaGlobals.jsciFile;
                    
                    Vector csparseClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(csparseFile, "edu");
                    
                    int k = 1;
                    watchClassesOfCSparse.displayClassesAndMethods(csparseClasses, "CSparse Library for sparse matrices", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem apacheCommonsRoutinesJMenuItem = new JMenuItem("Apache Commons Math Routines");
                apacheCommonsRoutinesJMenuItem.setToolTipText("Display information using reflection for the Apache Commons math library classes and methods");
                apacheCommonsRoutinesJMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(apacheCommonsRoutinesJMenuItem);
                apacheCommonsRoutinesJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfApacheCommonsMath = new scalaExec.gui.WatchClasses();
                    
                    Vector ApacheCommonsClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "org/apache/commons/math");
                    
                    int k = 1;
                    watchClassesOfApacheCommonsMath.displayClassesAndMethods(ApacheCommonsClasses, "Apache Common Maths Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem apache3CommonsRoutinesJMenuItem = new JMenuItem("Apache Common Math Routines - Current Version");
                apache3CommonsRoutinesJMenuItem.setToolTipText("Display information using reflection for the current version of the Apache Common math library classes and methods");
                apache3CommonsRoutinesJMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(apache3CommonsRoutinesJMenuItem);
                apache3CommonsRoutinesJMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfApacheCommonsMath = new scalaExec.gui.WatchClasses();
                    
                    Vector ApacheCommonsClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ApacheCommonsFile, "org/apache/commons/math3");
                    
                    int k = 1;
                    watchClassesOfApacheCommonsMath.displayClassesAndMethods(ApacheCommonsClasses, "Apache Common Maths Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem joregonDSPMenuItem = new JMenuItem("Digital Signal Processing library routines");
                joregonDSPMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the  DSP  library ");
                joregonDSPMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(joregonDSPMenuItem);
                joregonDSPMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfDSP = new scalaExec.gui.WatchClasses();
                    
                    Vector DSPClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "DSP/");
                    
                    int k = 1;
                    watchClassesOfDSP.displayClassesAndMethods(DSPClasses, "DSP Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem JASRoutinesJMenuItem = new JMenuItem("Java Algebra System (JAS) Routines");
                JASRoutinesJMenuItem.setToolTipText("Display information using reflection for the Java Algebra System (JAS library classes and methods");
                JASRoutinesJMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(JASRoutinesJMenuItem);
                JASRoutinesJMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJAS = new scalaExec.gui.WatchClasses();
                    
                    Vector JASClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "edu/jas");
                    
                    int k = 1;
                    watchClassesOfJAS.displayClassesAndMethods(JASClasses, "Java Algebra System (JAS) Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem MathEclipseRoutinesJMenuItem = new JMenuItem("Math Eclipse  Routines");
                MathEclipseRoutinesJMenuItem.setToolTipText("Display information using reflection for the Math Eclipse system for symbolic maths");
                MathEclipseRoutinesJMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(MathEclipseRoutinesJMenuItem);
                MathEclipseRoutinesJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfMathEclipse = new scalaExec.gui.WatchClasses();
                    
                    Vector MathEclipseClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "org/matheclipse/core");
                    
                    int k = 1;
                    watchClassesOfMathEclipse.displayClassesAndMethods(MathEclipseClasses, "Math Eclipse symbolic math evaluator", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                JMenuItem JASMenuItem = new JMenuItem("Java Algebra System routines");
                JASMenuItem.setToolTipText("Display information using reflection for the Java Algebra System (JAS) classes and methods");
                JASMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(JASMenuItem);
                JASMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJAS = new scalaExec.gui.WatchClasses();
                    
                    Vector numalClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "edu");
                    
                    int k = 1;
                    watchClassesOfJAS.displayClassesAndMethods(numalClasses, "Java Algebra System", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem jFreeChartMenuItem = new JMenuItem("JFreeChart library routines");
                jFreeChartMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the jFreeChart system ");
                jFreeChartMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(jFreeChartMenuItem);
                jFreeChartMenuItem.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfJFreeChart = new scalaExec.gui.WatchClasses();
                    
                    Vector JFreeChartClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jfreechartFile, "org");
                    
                    int k = 1;
                    watchClassesOfJFreeChart.displayClassesAndMethods(JFreeChartClasses, "JFreeChart System", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem jtransformsMenuItem = new JMenuItem("JTransforms  library routines");
                jtransformsMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Jtransforms  library ");
                jtransformsMenuItem.setFont(GlobalValues.uifont);
                libConfMenu.add(jtransformsMenuItem);
                jtransformsMenuItem.addActionListener((ActionEvent e) -> {
                    scalaExec.gui.WatchClasses watchClassesOfJTransforms = new scalaExec.gui.WatchClasses();
                    
                    String jtransformsFile = JavaGlobals.jsciFile;
                    
                    Vector jtransformsClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(jtransformsFile, "edu");
                    
                    int k = 1;
                    watchClassesOfJTransforms.displayClassesAndMethods(jtransformsClasses, "JTransforms Library", scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                // now prepare the "Search Keyword" menu items
                
                JMenuItem ScalaSciRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in ScalaSci Routines");
                ScalaSciRoutinesMenuItemWithKeyword.setToolTipText("Display information for the ScalaSci classes and methods having a keyword");
                ScalaSciRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(ScalaSciRoutinesMenuItemWithKeyword);
                ScalaSciRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector scalaSciClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "scalaSci");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(scalaSciClasses, "scalaSci ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem ScalaSciPlottingRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in ScalaSci Plotting Routines");
                ScalaSciPlottingRoutinesMenuItemWithKeyword.setToolTipText("Display information for the ScalaSci Plotting classes and methods having a keyword");
                ScalaSciPlottingRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(ScalaSciPlottingRoutinesMenuItemWithKeyword);
                ScalaSciPlottingRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector scalaSciPlottingClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "scalaSci/math/plot");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(scalaSciPlottingClasses, "scalaSci  Plotting", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem EJMLRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in EJML");
                EJMLRoutinesMenuItemWithKeyword.setToolTipText("Display information for the EJML classes and methods having a keyword");
                EJMLRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(EJMLRoutinesMenuItemWithKeyword);
                EJMLRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector EJMLClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ejmlFile, "org");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(EJMLClasses, "EJML ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem ApacheCommonsRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Apache Common Maths - the embedded version in the Java Algebra System");
                ApacheCommonsRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Apache Common Maths (the embedded version in the Java Algebra System) classes and methods having a keyword");
                ApacheCommonsRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(ApacheCommonsRoutinesMenuItemWithKeyword);
                ApacheCommonsRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector ApacheCommonsClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "org/apache/commons/math");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(ApacheCommonsClasses, "Apache Common Maths: the embedded in JAS version", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem Apache3CommonsRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in the current version of Apache Common Maths");
                Apache3CommonsRoutinesMenuItemWithKeyword.setToolTipText("Display information for the current version of the Apache Commons classes and methods having a keyword");
                Apache3CommonsRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(Apache3CommonsRoutinesMenuItemWithKeyword);
                Apache3CommonsRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector ApacheCommonsClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ApacheCommonsFile, "org/apache/commons/math3");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(ApacheCommonsClasses, "Apache Common Maths: the current version", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem JASRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Java Algebra System (JAS)");
                JASRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Java Algebra System (JAS) classes and methods having a keyword");
                JASRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(JASRoutinesMenuItemWithKeyword);
                JASRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector JASClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "edu/jas");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(JASClasses, "Java Algebra System (JAS)  ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem NUMALRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in NUMAL");
                NUMALRoutinesMenuItemWithKeyword.setToolTipText("Display information for the NUMAL classes and methods having a keyword");
                NUMALRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(NUMALRoutinesMenuItemWithKeyword);
                NUMALRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector NUMALClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "numal");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(NUMALClasses, "NUMAL  ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem JLAPACKRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in JLAPACK");
                JLAPACKRoutinesMenuItemWithKeyword.setToolTipText("Display information for the JLAPACK classes and methods having a keyword");
                JLAPACKRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(JLAPACKRoutinesMenuItemWithKeyword);
                JLAPACKRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector JLAPACKClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.LAPACKFile, "org");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(JLAPACKClasses, "JLAPACK  ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem jsciRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in JSci");
                jsciRoutinesMenuItemWithKeyword.setToolTipText("Display information for the JSci classes and methods having a keyword");
                jsciRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(jsciRoutinesMenuItemWithKeyword);
                jsciRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector jsciClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jsciFile, "JSci");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(jsciClasses, "JSci ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem nrRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Numerical Recipes");
                nrRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Numerical Recipes classes and methods having a keyword");
                nrRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(nrRoutinesMenuItemWithKeyword);
                nrRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector nrClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "com");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(nrClasses, "Numerical Recipes ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem joregonDSPMenuItemWithKeyword = new JMenuItem("Search Keyword in Digital Signal Processing library routines");
                joregonDSPMenuItemWithKeyword.setToolTipText("Display information using reflection for the classes and methods of  the DSP  library having a keyword");
                joregonDSPMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(joregonDSPMenuItemWithKeyword);
                joregonDSPMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfDSP = new scalaExec.gui.WatchClasses();
                    
                    Vector DSPClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "DSP/");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfDSP.displayClassesAndMethodsAsString(DSPClasses, "DSP", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem mtjRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in MTJ");
                mtjRoutinesMenuItemWithKeyword.setToolTipText("Display information for the MTJ classes and methods having a keyword");
                mtjRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(EJMLRoutinesMenuItemWithKeyword);
                mtjRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector mtjClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "no");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(mtjClasses, "MTJ ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem csparseRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in CSparse");
                csparseRoutinesMenuItemWithKeyword.setToolTipText("Display information for the CSparse classes and methods having a keyword");
                csparseRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(csparseRoutinesMenuItemWithKeyword);
                csparseRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    String csparseFile = JavaGlobals.jsciFile;
                    Vector csparseClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(csparseFile, "edu");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(csparseClasses, "CSparse ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem sgtRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Scientific Graphics Toolbox (SGT)");
                sgtRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Scientific Graphics Toolkit (SGT) classes and methods having a keyword");
                sgtRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(sgtRoutinesMenuItemWithKeyword);
                sgtRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector sgtClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "gov");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(sgtClasses, "Scientifc Graphics Toolkit (SGT) ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                JMenuItem coltRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in CERN Colt");
                coltRoutinesMenuItemWithKeyword.setToolTipText("Display information for the CERN Colt classes and methods having a keyword");
                coltRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                libConfMenu.add(coltRoutinesMenuItemWithKeyword);
                coltRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    
                    scalaExec.gui.WatchClasses watchClassesOfScalaSci = new scalaExec.gui.WatchClasses();
                    
                    Vector coltClasses = scalaExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "cern");
                    
                    int k = 1;
                    String filterString = scalaSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(coltClasses, "CERN Colt  ", filterString, scalaExec.gui.WatchClasses.watchXLoc + k * 50, scalaExec.gui.WatchClasses.watchYLoc + k * 50);
                });
                
                
                appearanceMenu.add(increasePaneFontMenuItem);
                appearanceMenu.add(decreasePaneFontMenuItem);
                appearanceMenu.add(increaseRSyntaxFontMenuItem);
                appearanceMenu.add(decreaseRSyntaxFontMenuItem);
                appearanceMenu.add(paneAdjustFontMenuItem);
                appearanceMenu.add(rsyntaxAreaAdjustFontMenuItem);
                appearanceMenu.add(adjustFontMenuItem);
                appearanceMenu.add(outConsoleAdjustFontMenuItem);
                appearanceMenu.add(adjustFontsUIMenuItem);
                appearanceMenu.add(adjustFontspUIMenuItem);
                appearanceMenu.add(adjustFontsgUIMenuItem);
                appearanceMenu.add(adjustsFontsbUIMenuItem);
                appearanceMenu.add(adjustLookAndFeelMenuItem);
                appearanceMenu.add(adjustHtmlFontsJMenuItem);
                JMenuItem scalaLabServerPortJMenuItem = new JMenuItem("IP address on which ScalaLab server runs");
                scalaLabServerPortJMenuItem.setToolTipText("set the IP o which ScalaLab server runs");
                scalaLabServerPortJMenuItem.setFont(GlobalValues.uifont);
                scalaLabServerPortJMenuItem.addActionListener(new scalaLabServerAction());
                confMenu.add(controlExplorerJMenuItem);
                confMenu.add(controlScalaCompilerJMenuItem1);
                confMenu.add(controlMainToolbarJMenuItem);
                confMenu.add(watchVariablesJMenuItem);
                confMenu.add(controlPrecisionJMenuItem);
                confMenu.add(preferJSyntaxPaneJMenuItem);
                confMenu.add(browseFileSysForPathsJMenuItem);
                confMenu.add(jsyntaxMouseMotionListenerOnJMenuItem);
                confMenu.add(rsyntaxMouseMotionListenerOnJMenuItem);
                confMenu.add(jsyntaxForAllValuesMouseMotionListenerOnJMenuItem);
                confMenu.add(rsyntaxForAllValuesMouseMotionListenerOnJMenuItem);
                confMenu.add(controlMainToolbarJMenuItem);
                confMenu.add(promptJMenuItem);
                confMenu.add(htmlWithSystemBrowser);
                confMenu.add(scalaLabServerPortJMenuItem);
                confMenu.add(htmlMagFactorJMenuItem);
                confMenu.add(presentStartupHelpJMenuItem);
                confMenu.add(resetToolboxClassesFrameJMenuItem);
                wizardsMenu = new JMenu("Wizards");
                wizardsMenu.setMnemonic('W');
                wizardsMenu.setFont(GlobalValues.uifont);
                wizardsMenu.setToolTipText("The Wizards menu allows to build almost automatically the code for categories of applications, e.g. ODE solvers");
                wizardsScalaSciScalaJMenuItem = new JMenuItem(new ODEWizardscalaSciScalaAction());
                wizardsScalaSciScalaJMenuItem.setToolTipText("Builds a ScalaSci ODE Solver with a full Scala-based implementation ");
                wizardsScalaSciScalaJMenuItem.setFont(GlobalValues.uifont);
                wizardsMenu.add(wizardsScalaSciScalaJMenuItem);
                wizardsScalaSciJMenuItem = new JMenuItem(new ODEWizardscalaSciAction());
                wizardsScalaSciJMenuItem.setToolTipText("Builds a ScalaSci ODE Solver with the ODEs implemented with Java code");
                wizardsScalaSciJMenuItem.setFont(GlobalValues.uifont);
                wizardsMenu.add(wizardsScalaSciJMenuItem);
                JMenuItem wizardLinearSystemsJMenuItem = new JMenuItem("Wizard for solving linear systems of equations");
                wizardLinearSystemsJMenuItem.setFont(GlobalValues.uifont);
                wizardLinearSystemsJMenuItem.setToolTipText("Helps with solving linear system equations related tasks using several libraries");
                wizardLinearSystemsJMenuItem.addActionListener((ActionEvent e) -> {
                    scalaSci.Wizards.LinearSystemsWizard.runWizard();
                });
                
                wizardsMenu.add(wizardLinearSystemsJMenuItem);
                examplesMenu = new JMenu("Demos  ", true);
                examplesMenu.setToolTipText("Examples and Demos. Some demos will require additional import statements to  run on minimal imports (i.e. \"fast\") interpreters");
                examplesMenu.setFont(GlobalValues.uifont);
                ScalaSciExamplesHelpJMenuItem = new JMenuItem("ScalaSci  Examples");
                ScalaSciExamplesHelpJMenuItem.addActionListener(new scalaSciExamplesAction());
                ScalaSciExamplesHelpJMenuItem.setFont(GlobalValues.uifont);
                
                ScalaSciExamplesJTreeJMenuItem = new JMenuItem("ScalaSci  Examples with JTree display");
                ScalaSciExamplesJTreeJMenuItem.addActionListener(new scalaSciExamplesJTreeAction());
                ScalaSciExamplesJTreeJMenuItem.setFont(GlobalValues.uifont);
                ScalaSciPlotExamplesJTreeJMenuItem = new JMenuItem("ScalaSci  Plotting Examples with JTree display");
                ScalaSciPlotExamplesJTreeJMenuItem.addActionListener(new scalaSciPlottingExamplesJTreeAction());
                ScalaSciPlotExamplesJTreeJMenuItem.setFont(GlobalValues.uifont);
                
                
                examplesMenu.setMnemonic('e');
                examplesMenu.setFont(GlobalValues.uifont);
                examplesMenu.add(ScalaSciExamplesJTreeJMenuItem);
                examplesMenu.add(ScalaSciPlotExamplesJTreeJMenuItem);
                JavaHelpJMenuItem = new JMenuItem("Java API  JavaHelp");
                JavaHelpJMenuItem.setToolTipText("Extensive help for the Java 6 SDK API platform");
                JavaHelpJMenuItem.setFont(GlobalValues.uifont);
                JavaHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    
                    if (GlobalValues.JavaCSHObject == null) {
                        GlobalValues.JavaCSHObject = new scalaExec.scalaLab.CSHObject();
                        GlobalValues.JavaCSHObject.setHelpSystem(GlobalValues.JavaHelpSetName);   // set the help system to Java JDK help
                        GlobalValues.JavaCSHObject.setConsoleHelp("top");
                        GlobalValues.JavaCSHObject.displayTheHelp();
                    } else {
                        GlobalValues.JavaCSHObject.displayTheHelp();
                    }
                });
                
                ScalaHelpJMenuItem = new JMenuItem("Scala API  JavaHelp");
                ScalaHelpJMenuItem.setToolTipText("Extensive help for the Scala Language");
                ScalaHelpJMenuItem.setFont(GlobalValues.uifont);
                ScalaHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    
                    if (GlobalValues.ScalaCSHObject == null) {
                        GlobalValues.ScalaCSHObject = new scalaExec.scalaLab.CSHObject();
                        GlobalValues.ScalaCSHObject.setHelpSystem(GlobalValues.ScalaHelpSetName);   // set the help system to Scala help
                        GlobalValues.ScalaCSHObject.setConsoleHelp("top");
                        GlobalValues.ScalaCSHObject.displayTheHelp();
                    } else {
                        GlobalValues.ScalaCSHObject.displayTheHelp();
                    }
                });
                
                symbolicAlgebraJMenu = new JMenu("ComputerAlgebra");
                symbolicAlgebraJMenu.setFont(GlobalValues.uifont);
                symbolicAlgebraJMenu.setToolTipText("A bridge to Computer Albegra functionality in ScalaLab");
                startsymJavaJMenuItem = new JMenuItem("Main symja interface");
                startsymJavaJMenuItem.setFont(GlobalValues.uifont);
                startsymJavaJMenuItem.setToolTipText("Start the main symja GUI window");
                startsymJavaJMenuItem.addActionListener((ActionEvent e) -> {
                    String[] nullArgs = new String[1];
                    nullArgs[0] = "";
                    org.matheclipse.symja.Main.main(nullArgs);
                });
                
                symbolicAlgebraJMenu.add(startsymJavaJMenuItem);
                String latexDisplayingStr = "Latex displaying is ON";
                if (GlobalValues.displayLatexOnEval == false) {
                    latexDisplayingStr = "Latex displaying is OFF";
                }
                displayLatexOnSymEvalJMenuItem = new JMenuItem(latexDisplayingStr);
                displayLatexOnSymEvalJMenuItem.setToolTipText("Toggles displaying Latex Formulas graphically");
                displayLatexOnSymEvalJMenuItem.setFont(GlobalValues.uifont);
                displayLatexOnSymEvalJMenuItem.addActionListener((ActionEvent e) -> {
                    GlobalValues.displayLatexOnEval = !GlobalValues.displayLatexOnEval;
                    if (GlobalValues.displayLatexOnEval == true) {
                        displayLatexOnSymEvalJMenuItem.setText("Latex displaying is ON");
                    } else {
                        displayLatexOnSymEvalJMenuItem.setText("Latex displaying is OFF");
                    }
                });
                
                symbolicAlgebraJMenu.add(displayLatexOnSymEvalJMenuItem);
                helpMenu = new JMenu("Help", true);
                helpMenu.setMnemonic('h');
                helpMenu.setFont(GlobalValues.uifont);
                JMenuItem JSyntaxCompletionHelpJMenuItem = new JMenuItem("Help on completion");
                JSyntaxCompletionHelpJMenuItem.setFont(GlobalValues.uifont);
                JSyntaxCompletionHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    EditorPaneHTMLHelp inPlaceHelpPane = new EditorPaneHTMLHelp("CompletionHelp.html");
                    if (GlobalValues.useSystemBrowserForHelp == false) {
                        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                        inPlaceHelpPane.setLocation(GlobalValues.sizeX / 4, GlobalValues.sizeY / 4);
                        inPlaceHelpPane.setVisible(true);
                    }
                });
                
                JMenuItem ScalaLabResourcesJMenuItem = new JMenuItem("The standard Java Scientific Libraries in ScalaLab");
                ScalaLabResourcesJMenuItem.setFont(GlobalValues.uifont);
                ScalaLabResourcesJMenuItem.addActionListener((ActionEvent e) -> {
                    EditorPaneHTMLHelp inPlaceHelpPane = new EditorPaneHTMLHelp("ScalaLabBooks.html");
                    if (GlobalValues.useSystemBrowserForHelp == false) {
                        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                        inPlaceHelpPane.setLocation(GlobalValues.sizeX / 4, GlobalValues.sizeY / 4);
                        inPlaceHelpPane.setVisible(true);
                    }
                });
                
                JavaHelpMenu = new JMenu("JavaHelp", true);
                JavaHelpMenu.setToolTipText("Provides help on basic components (e.g. Java,  Scala)  using the JavaHelp system");
                JavaHelpMenu.setFont(GlobalValues.uifont);
                usefulInputCommandsJMenuItem = new JMenuItem("Useful Input Commands");
                usefulInputCommandsJMenuItem.setFont(GlobalValues.uifont);
                usefulInputCommandsJMenuItem.addActionListener((ActionEvent e) -> {
                    EditorPaneHTMLHelp inPlaceHelpPane = new EditorPaneHTMLHelp("BasicInputCommands.html");
                    if (GlobalValues.useSystemBrowserForHelp == false) {
                        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                        inPlaceHelpPane.setLocation(GlobalValues.sizeX / 4, GlobalValues.sizeY / 4);
                        inPlaceHelpPane.setVisible(true);
                    }
                }
                );
                
                matrixConversionJMenuItem = new JMenuItem("Matrix Conversion Commands");
                matrixConversionJMenuItem.setFont(GlobalValues.uifont);
                matrixConversionJMenuItem.addActionListener((ActionEvent e) -> {
                    EditorPaneHTMLHelp inPlaceHelpPane = new EditorPaneHTMLHelp("MatrixConversion.html");
                    if (GlobalValues.useSystemBrowserForHelp == false) {
                        inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
                        inPlaceHelpPane.setLocation(GlobalValues.sizeX / 4, GlobalValues.sizeY / 4);
                        inPlaceHelpPane.setVisible(true);
                    }
                }
                );
                
                
                helpMenu.add(usefulInputCommandsJMenuItem);
                helpMenu.add(matrixConversionJMenuItem);
                helpMenu.add(JSyntaxCompletionHelpJMenuItem);
                helpMenu.add(ScalaLabResourcesJMenuItem);
                helpMenu.add(helpSwingScalaConsoleItem);
                JavaHelpMenu.add(JavaHelpJMenuItem);
                JavaHelpMenu.add(ScalaHelpJMenuItem);
                basicHelpJMenuItem = new JMenuItem("Basic Commands");
                basicHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    new basicCommandsHelper();
                });
                
                basicHelpJMenuItem.setFont(GlobalValues.uifont);
                memoryHelpJMenuItem = new JMenuItem("Available memory - How to icrease");
                memoryHelpJMenuItem.setFont(GlobalValues.uifont);
                memoryHelpJMenuItem.addActionListener((ActionEvent e) -> {
                    String mem = GlobalValues.availMemFormat.format((double) GlobalValues.rt.freeMemory() / 1000000);
                    String dispStr = "Available memory is : " + mem + "MB.  To increase it start ScalaLab with e.g. :  java -Xss10m -Xms1000m -Xmx16000m -jar scalalab.jar";
                    JOptionPane.showMessageDialog(null, dispStr, "Available memory", JOptionPane.INFORMATION_MESSAGE);
                });
                
                JMenuItem JavaConfigInfoJMenuItem = new JMenuItem("Java Version Info");
                JavaConfigInfoJMenuItem.setFont(GlobalValues.uifont);
                JavaConfigInfoJMenuItem.addActionListener((ActionEvent e) -> {
                    JavaConfigInfo.displayJavaConfigInfo();
                });
                
                
                helpMenu.add(basicHelpJMenuItem);
                helpMenu.add(memoryHelpJMenuItem);
                helpMenu.add(JavaConfigInfoJMenuItem);
                helpMenu.setMnemonic('h');
                mainJMenuBar.add(FileMenu);
                mainJMenuBar.add(editMenu);
                mainJMenuBar.add(importWizardMenu);
                mainJMenuBar.add(compileMenu);
                mainJMenuBar.add(scalaInterpreterMenu);
                
                mainJMenuBar.add(confMenu);
                mainJMenuBar.add(appearanceMenu);
                mainJMenuBar.add(libConfMenu);
                mainJMenuBar.add(wizardsMenu);
                mainJMenuBar.add(UtilitiesMenu);
                mainJMenuBar.add(symbolicAlgebraJMenu);
                mainJMenuBar.add(examplesMenu);
                mainJMenuBar.add(JavaHelpMenu);
                mainJMenuBar.add(helpMenu);
                

                mainJMenuBar.setOpaque(true);
                myId.setJMenuBar(mainJMenuBar);
            }
        });

    }

    public void installInLayeredPane(JComponent component) {
        JLayeredPane layeredPane = getRootPane().getLayeredPane();
        layeredPane.add(component, JLayeredPane.PALETTE_LAYER, 20);
        Dimension size = component.getPreferredSize();
        component.setSize(size);
        component.setLocation((getWidth() - size.width) / 2,
                (getHeight() - size.height) / 2);
        component.revalidate();
        component.setVisible(true);
    }

    private Vector searchForClassWithRegularExpression(String regExp) {
        Vector patternMatches = new Vector();
        Pattern classPattern = Pattern.compile(regExp);
        Hashtable clTable = new Hashtable(); ///// SOS-Sterg scalaExec.Interpreter.GlobalValues.functionManager.getClassLoader().loadedClasses;
        Enumeration iter = clTable.elements();
        while (iter.hasMoreElements()) {   // consider all .class functions
            Object next = (Object) iter.nextElement();
            Class currentClass = (Class) next;
            String className = currentClass.getCanonicalName();

            Matcher classPatternMatcher = classPattern.matcher(className);
            if (classPatternMatcher.find()) // class name fits with pattern
            {
                patternMatches.add(className);
            }
        }
        return patternMatches;
    }

    public static void detectJarLibPaths(String watchStr) {
        if (File.pathSeparatorChar == ';') {  // handle Windows file system naming
            int idxOfColon = watchStr.lastIndexOf(':');
            watchStr = watchStr.substring(idxOfColon - 1, watchStr.length());
        }
        int sepIndex = watchStr.indexOf('!');

        String fullJarPath = watchStr.substring(0, sepIndex);

        //  test if ScalaLab is installed in a directory containing special charactes, e..g. spaces, symbols, etc)
        //  and quit if so, displaying an appropriate message to the user
        boolean specialCharsInPath = false;
        int pathLen = fullJarPath.length();
        for (int k = 0; k < pathLen; k++) {
            char ch = fullJarPath.charAt(k);
            if (ch != File.separatorChar && ch != '/' && ch != ':' && ch != '.' && ch != '-' && ch != '_' && ch != '-' && ch != '_') {
                if (Character.isLetterOrDigit(ch) == false) {
                    specialCharsInPath = true;
                    break;
                }
            }
        }
        if (specialCharsInPath == true) {
            JOptionPane.showMessageDialog(null, "Path where ScalaLab is installed: " + fullJarPath + " , contains speciial characters. Please install ScalaLab in a path name with no special chars and no spaces.", "Please install ScalaLab in a simple path name", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        }

        System.out.println("Full Path of the main ScalaLab .jar file  = " + fullJarPath);
        String scalaLabJarName = fullJarPath.substring(fullJarPath.lastIndexOf(File.separatorChar) + 1, fullJarPath.length());
        if (scalaLabJarName.indexOf(File.separator) != -1) {
            scalaLabJarName = scalaLabJarName.replaceAll(File.separator, "/");
        }
        if (scalaLabJarName.contains("file:")) {
            scalaLabJarName = scalaLabJarName.replaceAll("file:", "");
        }

        System.out.println("scalalabJarName = = " + scalaLabJarName);
        GlobalValues.jarFilePath = scalaLabJarName;
        GlobalValues.fullJarFilePath = fullJarPath;
        System.out.println("jarFilePath= = " + GlobalValues.jarFilePath);

        String scalalabLibPath = GlobalValues.jarFilePath;
        String scalalabHelpPath = GlobalValues.jarFilePath;

        // remove jar file name from the path name
        int dotIndex = scalalabLibPath.indexOf(".");
        int lastPos = dotIndex;
        while (scalalabLibPath.charAt(lastPos) != '/' && scalalabLibPath.charAt(lastPos) != '\\' && lastPos > 0) {
            lastPos--;
        }
        scalalabLibPath = scalalabLibPath.substring(0, lastPos);

        // remove jar file name from the path name
        dotIndex = scalalabHelpPath.indexOf(".");
        lastPos = dotIndex;
        while (scalalabHelpPath.charAt(lastPos) != '/' && scalalabHelpPath.charAt(lastPos) != '\\' && lastPos > 0) {
            lastPos--;
        }
        scalalabHelpPath = scalalabHelpPath.substring(0, lastPos);

        if (scalalabHelpPath.length() > 0) {
            scalalabLibPath = scalalabLibPath + File.separator + "lib" + File.separator;
            scalalabHelpPath = scalalabHelpPath + File.separator + "help" + File.separator;
        } else {
            scalalabLibPath = "lib" + File.separator;
            scalalabHelpPath = "help" + File.separator;
        }

        GlobalValues.scalalabLibPath = scalalabLibPath;
        GlobalValues.scalalabHelpPath = scalalabHelpPath;

    }

    /**
     * Usage scalaLab -jar scalaLab.jar [<ScalaSciClassPath>] [pathToJarFile>]
     * the optional ScalaSciClassPath controls where to search for additional
     * optional class files
     */
    public static void main(String[] args) {

        int argc;
        if (args != null) {
            argc = args.length;
        } else {
            argc = 0;
        }

        if (argc > 0) {
            netbeansScalaLabArg = args[0];
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //System.out.println("UnsupportedOperationException in main thread of ScalaLab.");

            }
        });

        //   the watchURL will serve to retrieve the name of the .jar file of the scalaLab system (i.e. "scalaLab.jar") 
        //   the scalaLab's .jar file is used to load all the basic external classes and thus is very important.
        //   When scalaLab is running within the Netbeans environment it is passed as the first program argument.          
        URL watchURL = (new scalaExec.scalaLab.scalaSciToolbox()).getClass().getResource("resources/scala.gif");

        String watchStr = watchURL.toString();
        watchStr = decodeFileName(watchStr);

        detectJarLibPaths(watchStr);

        GlobalValues.rt = Runtime.getRuntime();
        GlobalValues.memAvailable = GlobalValues.rt.freeMemory();

        //scalaExec.scalaLab.fxgui fxObj = new scalaExec.scalaLab.fxgui();
        //fxObj.main(null);
        try {
            String vers = System.getProperty("java.version");
            if (vers.compareTo("1.5") < 0) {
                System.out.println("!!!scalaLab: Swing must be run with a 1.5 or higher version VM!!!");
                System.exit(1);
            }
            String currentWorkingDirectory = System.getenv("PWD");
            if (currentWorkingDirectory == null) {
                currentWorkingDirectory = "c:\\";
            }

            String userDir = System.getProperty("user.dir");
            currentWorkingDirectory = userDir;
            if (File.pathSeparatorChar != ':') { // detect OS type
                // Windows host
                GlobalValues.hostIsUnix = false;
                if (currentWorkingDirectory == null) // e.g. for Windows the current working directory is undefined
                {
                    currentWorkingDirectory = "C:\\";
                }
            }
            if (GlobalValues.workingDir == null) {
                GlobalValues.workingDir = currentWorkingDirectory;
            }

            String[] additionalToolboxes = null;

            if (argc >= 2) {
                GlobalValues.ScalaSciClassPath = args[1];
            }

            GlobalValues.initGlobals();

            final scalaLab myGui = new scalaLab(GlobalValues.ScalaSciClassPath, additionalToolboxes);

    //        scalaExec.Interpreter.GlobalValues.globalJavaEvaluator.reset();

            SwingUtilities.invokeLater(() -> {
                // run in  */
                scalaExec.gui.ConsoleKeyHandler.updateModeStatusInfo();
                myGui.setLocation(xLocMainFrame, yLocMainFrame);

                myGui.loadTab(GlobalValues.mainTab);

                if (GlobalValues.preferRSyntaxEditor) {
                    GlobalValues.globalRSyntaxFrame = (new rsyntaxEditor("Untitled", true)).currentFrame;
                    scalaExec.Interpreter.GlobalValues.editorPane.setCaretPosition(0);
                }
                myGui.setVisible(true);
            });
        } catch (StackOverflowError se) {
            JOptionPane.showMessageDialog(null, "Stack overflow error, try to use:   java -jar -Xss20m -Xms200m -Xmx1000m -jar scala;ab.jar ");
            se.printStackTrace();
        } catch (OutOfMemoryError oe) {
            JOptionPane.showMessageDialog(null, "Out-of memory error, try to use:   java -jar -Xss20m -Xms200m -Xmx1000m -jar scala;ab.jar ");
            oe.printStackTrace();
        }

        //   if (GlobalValues.explorerVisible)
        //      GlobalValues.scalalabMainFrame.createMainTab();    // create and display the Explorer window
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        closeGUI();
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        closeGUI();
    }

    // update the recent files menu with the items taken from recentFiles
    public void updateRecentPaneFilesMenu() {

        mainJMenuBar.remove(recentPaneFilesMenu);

        recentPaneFilesMenu.removeAll();  // clear previous menu items
        recentPaneFilesMenu.setFont(GlobalValues.uifont);
        JMenuItem clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
        clearRecentFilesJMenuItem.setFont(GlobalValues.uifont);
        clearRecentFilesJMenuItem.addActionListener((ActionEvent e) -> {
            recentPaneFiles.clear();
            recentPaneFilesMenu.removeAll();
        });

        recentPaneFilesMenu.add(clearRecentFilesJMenuItem);

        int numberRecentFiles = recentPaneFiles.size();
        for (int k = numberRecentFiles - 1; k >= 0; k--) {     // reverse order for displaying the most recently loaded first
            final String recentFileName = (String) recentPaneFiles.elementAt(k);   // take the recent filename
            JMenuItem recentFileMenuItem = new JMenuItem(recentFileName);
            recentFileMenuItem.setFont(GlobalValues.uifont);
            recentFileMenuItem.addActionListener((ActionEvent e) -> {
                scalaLabPaneEdit(recentFileName);   // reload the recent file in editor
            });

            recentPaneFilesMenu.add(recentFileMenuItem);    // add the menu item corresponding to the recent file
        }  // for all the recently accessed files 

        recentPaneFilesMenu.setToolTipText("Tracks \"Saved As\" Files");
        mainJMenuBar.add(recentPaneFilesMenu);   // finally add the recent files menu to the main menu bar

    }

    // edit the specified file within the Interpreter Pane
    public void scalaLabPaneEdit(String fileName) {
      
        
                File loadFile = new File(fileName);
          StringBuilder sb=new StringBuilder();

                try {
            FileReader fr = new FileReader(loadFile);
            
            
            while (true) {
                int ch = fr.read();
                if (ch==-1) break;
                sb.append((char)ch);
            }
            
            
               scalaExec.Interpreter.GlobalValues.editorPane.setText(sb.toString()); 
     //    scalaExec.Interpreter.GlobalValues.editorPane.read(fr, null);  // read the file into the Pane editor

          //  GlobalValues.globalInterpreterPane.updateDocument();  // update the document kept by the pane

            GlobalValues.editingFileInPane = fileName;   // keep the currently edited file name
            if (GlobalValues.scalalabMainFrame.recentPaneFiles.contains(fileName) == false) {
                GlobalValues.scalalabMainFrame.recentPaneFiles.add(fileName);
                GlobalValues.scalalabMainFrame.updateRecentPaneFilesMenu();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot open file " + loadFile + " for loading editor text " + ex.getMessage());
        } catch (Exception ex) {
         //   System.out.println("Exception reading editor's text " + ex.getMessage());
            scalaExec.Interpreter.GlobalValues.editorPane.setText(sb.toString()); 
        }

    }

    // save to disk the list of recent files
    private void saveRecentPaneFiles() {  // the file that keeps the recent files list is kept in GlobalValues.scalaLabRecentFilesList
        // at the same directory as the scalaLab.jar executable, i.e. GlobalValues.jarFilePath

        //create streams
        try {
            // open the file for writing the recent files         
            FileOutputStream output = new FileOutputStream(fileWithFileListOfPaneRecentFiles);

            //create writer stream
            OutputStreamWriter recentsWriter = new OutputStreamWriter(output);
            int fileCnt = 0;  // restrict the maximum number of recent files
            int cntRecentFiles = recentPaneFiles.size();

            for (int k = 0; k < cntRecentFiles; k++) {
                String currentRecentFile = (String) recentPaneFiles.elementAt(k) + "\n";
                recentsWriter.write(currentRecentFile, 0, currentRecentFile.length());
                if (fileCnt++ == GlobalValues.maxNumberOfRecentFiles) {
                    break;
                }
            }
            recentsWriter.close();
            output.close();
        } catch (java.io.IOException except) {
            System.out.println("IO exception in saveRecentFiles");
            System.out.println(except.getMessage());
            except.printStackTrace();
        }
    }

    // load the recent files list from the disk updating also the menu
    private void loadRecentPaneFiles() {
        // create streams
        try {
            // open the file containing the stored list of recent files
            FileInputStream input = new FileInputStream(fileWithFileListOfPaneRecentFiles);

            //create reader stream
            BufferedReader recentsReader = new BufferedReader(new InputStreamReader(input));

            recentPaneFiles.clear();    // clear the Vector of recent files
            String currentLine;     // refill it from disk
            while ((currentLine = recentsReader.readLine()) != null) {
                if (recentPaneFiles.indexOf(currentLine) == -1) // file not already in list
                {
                    recentPaneFiles.add(currentLine);
                }
            }

            recentsReader.close();
            input.close();
            updateRecentPaneFilesMenu();   // update the recent files menu

        } catch (java.io.IOException except) {
            System.out.println("IO exception in readRecentsFiles. File: " + fileWithFileListOfPaneRecentFiles + "  not found");
            recentPaneFilesMenu.removeAll();  // clear previous menu items
            JMenuItem clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
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

    class StandAloneApplicationActionScala extends AbstractAction {

        public StandAloneApplicationActionScala() {
            super("Transform Script to Stand Alone Application");
        }

        public void transformScriptToStandAlone() {
            String scriptText = GlobalValues.editorPane.getText();

            GlobalValues.scriptObjectName = JOptionPane.showInputDialog(null, "Creating StandAlone application", GlobalValues.scriptObjectName);
            String standAloneText = GlobalValues.standAloneImports
                    + "\n\n\n\nobject " + GlobalValues.scriptObjectName + "  extends AnyRef with scalaSci.StaticScalaSciGlobal" + " { \n\n"
                    + "def  main(args: Array[String]): Unit  = \n  { " + "\n\n"
                    + scriptText
                    + "\n} \n } \n";

            String toRunCommand = "java " + " -cp " + GlobalValues.jarFilePath + File.pathSeparator
                    + scalalab.JavaGlobals.compFile + File.pathSeparator + scalalab.JavaGlobals.libFile + File.pathSeparator
                    + scalalab.JavaGlobals.reflectFile + File.pathSeparator
                    + scalalab.JavaGlobals.ejmlFile + File.pathSeparator
                    + scalalab.JavaGlobals.jblasFile + File.pathSeparator
                    + scalalab.JavaGlobals.mtjColtSGTFile + File.pathSeparator
                    + scalalab.JavaGlobals.numalFile + File.pathSeparator
                    + scalalab.JavaGlobals.LAPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.ARPACKFile + File.pathSeparator
                    + "./lib/ApacheCommonMaths.jar" + File.pathSeparator
                    + "./lib/JSciRSyntaxTextArea.jar" + File.pathSeparator
                    + "." + "   "
                    + GlobalValues.scriptObjectName;

            standAloneText = standAloneText + " /*  \n \n To Run the Script Compile it and run with java with the following command (perhaps all the libraries are not required) \n "
                    + toRunCommand + " \n */";

            String runScriptExtension = ".sh";
            if (GlobalValues.hostIsUnix == false) {
                runScriptExtension = ".bat";  // Windows host
            }
            File scriptToRunFile = new File(Directory.Current().get().path() + File.separator + GlobalValues.scriptObjectName + runScriptExtension);
            try {
                FileWriter outScriptFile = new FileWriter(scriptToRunFile);
                outScriptFile.write(toRunCommand);
                outScriptFile.close();
            } catch (Exception e) {
                System.out.println("exception trying to write standalone script " + GlobalValues.scriptObjectName);
            }

            GlobalValues.editorPane.setText(standAloneText);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            transformScriptToStandAlone();
        }

    }

    /* . execute Scala code placed with the "main" object, e.g.
    plot(sin(0.56*inc(0, 0.01, 70)))
     */
    class CompileExecuteActionScala extends AbstractAction {

        String tempFileName = "";
        File tempFile = null;

        public CompileExecuteActionScala() {
            super("Scala Compile and Execute");
        }

        public void executeTextScala() {
            String currentFileName = "Untitled";
            String programText = GlobalValues.editorPane.getText();  // get the program's text in order to search for the object's name
            String className = "";

            int objPos = programText.indexOf("object");
            if (objPos != -1) { // "object" exists
                int afterObjectPos = objPos + 7;
                // after the word "object an identifier and a "{" must exist
                programText = programText.substring(afterObjectPos);
                StringTokenizer stTok = new StringTokenizer(programText, " ");
                className = stTok.nextToken();
                tempFileName = className + ".scala";   // public classes and Java files should have the same name
            } else {
                int clPos = programText.indexOf("class");
                if (clPos != -1) { // "class" exists
                    int afterClassPos = clPos + 6;
                    // after the word "object an identifier and a "{" must exist
                    programText = programText.substring(afterClassPos);
                    StringTokenizer stTok = new StringTokenizer(programText, " ");
                    className = stTok.nextToken();
                    tempFileName = className + ".scala";   // public classes and Java files should have the same name
                } else {
                    return;
                }
            }

            String jarFileName = GlobalValues.jarFilePath;
            // get the user's home path
            String homePath = GlobalValues.fullJarFilePath;
            char JavaPathChar = '/';
            homePath = homePath.substring(0, homePath.lastIndexOf(JavaPathChar));
            if (homePath.startsWith("jar:file:")) {
                homePath = homePath.substring(9, homePath.length());
            }
            try {
                // take the program's text and save it to a temporary file
                String wholeFileName = homePath + File.separatorChar + tempFileName;
                System.out.println("wholeFileName = " + wholeFileName);
                tempFile = new File(wholeFileName);
                FileWriter fw = new FileWriter(tempFile);
                GlobalValues.editorPane.write(fw);
            } catch (IOException ioe) {
                System.out.println("Exception trying to write to " + tempFileName);
                return;
            }
            // compile the temporary file

            String toolboxes = "";
            for (int k = 0; k < GlobalValues.ScalaSciClassPathComponents.size(); k++) {
                toolboxes = toolboxes + File.pathSeparator + GlobalValues.ScalaSciClassPathComponents.elementAt(k);
            }

            String selectedValue = tempFileName;
            String[] command = new String[7];

            command[0] = "java";
            command[1] = "-cp";
            command[2] = GlobalValues.jarFilePath + File.pathSeparator
                    + scalalab.JavaGlobals.compFile + File.pathSeparator
                    + scalalab.JavaGlobals.libFile + File.pathSeparator
                    + scalalab.JavaGlobals.reflectFile + File.pathSeparator
                    + scalalab.JavaGlobals.swingFile + File.pathSeparator
                    + scalalab.JavaGlobals.ejmlFile + File.pathSeparator
                    + scalalab.JavaGlobals.jblasFile + File.pathSeparator
                    + scalalab.JavaGlobals.mtjColtSGTFile + File.pathSeparator
                    + scalalab.JavaGlobals.numalFile + File.pathSeparator
                    + scalalab.JavaGlobals.jfreechartFile + File.pathSeparator
                    + scalalab.JavaGlobals.LAPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.ARPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.JASFile + File.pathSeparator
                    + scalalab.JavaGlobals.jsciFile + File.pathSeparator
                    + scalalab.JavaGlobals.ApacheCommonsFile + File.pathSeparator
                    + scalalab.JavaGlobals.jfreechartFile + File.pathSeparator
                    + toolboxes + File.pathSeparator + homePath;
            command[3] = "scala.tools.nsc.Main";    // the name of the Scala compiler class
            command[4] = "-classpath";
            command[5] = GlobalValues.jarFilePath + File.pathSeparator
                    + scalalab.JavaGlobals.compFile + File.pathSeparator
                    + scalalab.JavaGlobals.libFile + File.pathSeparator
                    + scalalab.JavaGlobals.reflectFile + File.pathSeparator
                    + scalalab.JavaGlobals.swingFile + File.pathSeparator
                    + scalalab.JavaGlobals.ejmlFile + File.pathSeparator
                    + scalalab.JavaGlobals.jblasFile + File.pathSeparator
                    + scalalab.JavaGlobals.mtjColtSGTFile + File.pathSeparator
                    + scalalab.JavaGlobals.numalFile + File.pathSeparator
                    + scalalab.JavaGlobals.jfreechartFile + File.pathSeparator
                    + scalalab.JavaGlobals.LAPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.ARPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.JASFile + File.pathSeparator
                    + scalalab.JavaGlobals.jsciFile + File.pathSeparator
                    + scalalab.JavaGlobals.ApacheCommonsFile + File.pathSeparator
                    + scalalab.JavaGlobals.jfreechartFile + File.pathSeparator
                    + toolboxes + File.pathSeparator + homePath;

            try {
                command[6] = tempFile.getCanonicalPath();
            } catch (IOException ex) {
                System.out.println("Exception in tempFile.getCanonicalPath()");
                return;
            }

            String compileCommandString = command[0] + "  " + command[1] + "  " + command[2] + " " + command[3] + " " + command[4] + " " + command[5] + " " + command[6];

            System.out.println("compileCommand = " + compileCommandString);

            try {
                Runtime rt = Runtime.getRuntime();
                Process javaProcess = rt.exec(command);
                // an error message?
                StreamGobbler errorGobbler = new StreamGobbler(javaProcess.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(javaProcess.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                // any error???
                javaProcess.waitFor();
                int rv = javaProcess.exitValue();

                if (rv == 0) {
                    System.out.println("Process:  " + compileCommandString + "  exited successfully ");
                } else {
                    System.out.println("Process:  " + compileCommandString + "  exited with error, error value = " + rv);
                }

            } catch (IOException exio) {
                System.out.println("IOException trying to executing " + command);
                exio.printStackTrace();

            } catch (InterruptedException ie) {
                System.out.println("Interrupted Exception  trying to executing " + command);
                ie.printStackTrace();
            }

            tempFile.delete(); // delete the temporary source file

            // execute now the compiled code
            int sepPos = GlobalValues.jarFilePath.lastIndexOf(File.separatorChar);
            if (sepPos == -1) // in Windows sometimes things are confused with Java default separator (i.e. "/") and Windows one (i.e. "\")
            {
                sepPos = GlobalValues.jarFilePath.lastIndexOf("/");
            }

            command = new String[4];   // the run command
            command[0] = "java";
            command[1] = "-cp";
            command[2] = GlobalValues.jarFilePath + File.pathSeparator
                    + scalalab.JavaGlobals.compFile + File.pathSeparator + scalalab.JavaGlobals.libFile + File.pathSeparator
                    + scalalab.JavaGlobals.reflectFile + File.pathSeparator
                    + scalalab.JavaGlobals.swingFile + File.pathSeparator
                    + scalalab.JavaGlobals.ejmlFile + File.pathSeparator
                    + scalalab.JavaGlobals.jblasFile + File.pathSeparator
                    + scalalab.JavaGlobals.mtjColtSGTFile + File.pathSeparator
                    + scalalab.JavaGlobals.numalFile + File.pathSeparator
                    + scalalab.JavaGlobals.jfreechartFile + File.pathSeparator
                    + scalalab.JavaGlobals.ARPACKFile + File.pathSeparator
                    + scalalab.JavaGlobals.JASFile + File.pathSeparator
                    + "./lib/ApacheCommonMaths.jar" + File.pathSeparator
                    + "./lib/JSciRSyntaxTextArea.jar" + File.pathSeparator
                    + "." + File.pathSeparator
                    + toolboxes + File.pathSeparator + homePath;
            command[3] = className;    // the name of the Scala compiler class
            String runCommandString = command[0] + "  " + command[1] + "  " + command[2] + "  " + command[3];
            System.out.println("executing:  " + runCommandString);
            try {
                Runtime rt = Runtime.getRuntime();
                Process javaProcess = rt.exec(command);
                // an error message?
                StreamGobbler errorGobbler = new StreamGobbler(javaProcess.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(javaProcess.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                // any error???
                javaProcess.waitFor();
                int rv = javaProcess.exitValue();
                if (rv == 0) {
                    System.out.println("Process:  " + runCommandString + "  exited successfully ");
                } else {
                    System.out.println("Process:  " + runCommandString + "  exited with error, error value = " + rv);
                }

            } catch (IOException ex) {
                System.out.println("IOException trying to executing " + command);
                ex.printStackTrace();

            } catch (InterruptedException ie) {
                System.out.println("Interrupted Exception  trying to executing " + command);
                ie.printStackTrace();
            }

        }

        public void actionPerformed(ActionEvent e) {
            executeTextScala();
        }
    }

  


    class CompileExecuteExternalActionJava extends AbstractAction {

        String tempFileName = "";

        File tempFile = null;

        public CompileExecuteExternalActionJava() {
            super("Java Compile and Execute with the external javac compiler");
        }

        public void actionPerformed(ActionEvent e) {
            new scalaExec.gui.CompileExecutePaneActionJava().executeTextExternalJava();
        }
    }

}
