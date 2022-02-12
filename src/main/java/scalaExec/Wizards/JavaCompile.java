 
package scalaExec.Wizards;
 
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.AutoCompletionScalaSci;
import java.awt.Font;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.swing.*;

import scalalabEdit.*;

public class JavaCompile  extends  ClassLoader  {

    private  boolean verbose = true; 
    String [] dirs;
    
    public boolean getVerbose() { return verbose; }
    public void setVerbose(boolean _verbose) { verbose = _verbose; }
    
    public JavaCompile( String path) {
        dirs = path.split(System.getProperty("path.separator"));  // use both the Linux/Unix (i.e. ":") and the Windows separators (i.e. ";")
        
    }
    
    public JavaCompile( ) {
        String path=".";
        dirs = path.split(System.getProperty("path.separator"));  // use both the Linux/Unix (i.e. ":") and the Windows separators (i.e. ";")
    }
    
    
    public JavaCompile( String path, ClassLoader parent) {
        super(parent);
        dirs = path.split(System.getProperty("path.separator")); //// use both the Linux/Unix (i.e. ":") and the Windows separators (i.e. ";")
    }
    
    
     
// compile a Java file with the installed javac compiler
 public  boolean compileFile( String sourceFile) { 
      boolean compilationResult = true;  // no errors
    
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null)  {  // Sterg-SOS why not loaded? 
        System.out.println("ToolProvider.getSystemJavaCompiler: no compiler provided. Unable to compile");
    }
    DiagnosticCollector<JavaFileObject> diagnostics =
             new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = 
            compiler.getStandardFileManager(diagnostics, null, null);
    
    StringWriter  compileWriter = new StringWriter();
    
    List <File> sourceFileList = new ArrayList<File>();
    sourceFileList.add(new File(sourceFile));
    Iterable<? extends JavaFileObject> compilationUnits = 
            fileManager.getJavaFileObjectsFromFiles(sourceFileList);
   
    String pathOfFile = sourceFile.substring(0, sourceFile.lastIndexOf(File.separatorChar));
    String classpath =GlobalValues.jarFilePath+File.pathSeparatorChar+pathOfFile+File.pathSeparatorChar+".";
    Iterable <String> options = Arrays.asList("-cp", classpath);
    
    CompilationTask task = compiler.getTask(compileWriter, fileManager, null, options, null, compilationUnits);
   
    boolean compileResult = task.call();
    if (compileResult == false) {
        compilationResult = false;  // compilation errors
        JFrame compResultsFrame = new JFrame("Compilation Results");
        String diagnString = compileWriter.toString();
        JTextArea compResultsArea = new JTextArea(diagnString);
        compResultsArea.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel  compResultsPanel = new JPanel();
        compResultsPanel.add(compResultsArea);
        compResultsFrame.add(compResultsPanel);
        compResultsFrame.setSize(800, 200);
        compResultsFrame.setLocation(100, 200);
        compResultsFrame.setVisible(true);
        int response = JOptionPane.showOptionDialog(null, "File "+sourceFile+" has compilation errors ",  "Edit File? ",
                JOptionPane.YES_NO_OPTION,  JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (response == JOptionPane.YES_OPTION)   {   // edit it with scalalabEditor
            new scalalabEdit.EditorPaneEdit(sourceFile);
        }
        
     }       
  
    try {
        fileManager.close();
    }
    catch (IOException e) { }
    
    
    return compilationResult;
 }
 
 
 
 public  Class  loadNewClass(String javaFile, String packageName)  {
       Class myClass =  null;
       
       String className = javaFile.replace(".java", ".class");
       
       File  inpFile = new File(className);
       int idx = className.lastIndexOf(File.separatorChar);
       String javaName = className.substring(idx+1, className.indexOf("."));
       int size = (int)inpFile.length();
       byte[] ba=new byte[size];
       try {
       FileInputStream fis = new FileInputStream(className);
          
	// read the entry
	int bytes_read = 0;
	while (bytes_read != size)
	  {
	    int r = fis.read(ba, bytes_read, size - bytes_read);
 	    if (r < 0)      break;
	    bytes_read += r;
	  }
	if (bytes_read != size)   throw new IOException("cannot read entry");
       }
       catch (FileNotFoundException fnfExc) {
           System.out.println("File : "+className+" not found");
           fnfExc.printStackTrace();
       }
       catch (IOException ioExc) {
           System.out.println("IO Exception in JavaCompile trying to read class: ");
           ioExc.printStackTrace();
       }

       try {
         String packageAppendedFileName ="";
         if (packageName.isEmpty())
                packageAppendedFileName  = javaName;
         else
                packageAppendedFileName = packageName+"."+javaName;
         packageAppendedFileName.replace(File.separatorChar,'.');
        myClass = defineClass(packageAppendedFileName, ba, 0, size);
        // SOS-StergAutoCompletionScalaSci.upDateAutoCompletion(myClass);
        String userClassName = myClass.getName();
        JOptionPane.showMessageDialog(null, "Class "+userClassName+" loaded successfully !");
        }
        catch (ClassFormatError exc) {
            System.out.println("error defining class "+inpFile);
            exc.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println("some error defining class "+inpFile);
            ex.printStackTrace();
               }  
       return myClass;
     }
 
 
    // extend the class path in order to search the path(s) designated with the parameter path also
    public void extendClassPath( String path)  {
        // if path is already in the extension's loader class path do not reinsert it
        for (int k=0; k<dirs.length; k++) {
            if (dirs[k].equals(path)) 
                return;
        }
        String [] exDirs = path.split(System.getProperty("path.separator"));  // vector with the paths to be added
        String [] newDirs = new String[dirs.length+exDirs.length+1];
        newDirs[0] = GlobalValues.jarFilePath;
        System.arraycopy(dirs, 0, newDirs, 1, dirs.length);  // copy current path
        System.arraycopy(exDirs, 0, newDirs, dirs.length+1, exDirs.length);  // append new paths 
        dirs = newDirs;
        }
    
    public synchronized Class findClass( String name)  
                throws  ClassNotFoundException
    {
        for (int i=0; i < dirs.length; i++)  {  // for all dirs of extension class path
            byte [] buf =  getClassData( dirs[i], name);  // class found
            if (buf != null)  
                return defineClass(name, buf, 0, buf.length);
        }
        throw new ClassNotFoundException();
    }
    
    protected byte[] getClassData( String directory, String name)  {
        String classFile = directory+"/"+ name.replace('.', '/')+".class";
                
        int classSize =  (int)new File( classFile ).length();
        byte [] buf = new byte[ classSize ];
        
        try {
            FileInputStream  filein = new FileInputStream(classFile);
            classSize = filein.read( buf );
            filein.close();
        }
        catch (FileNotFoundException e)  {
            return null;
        }
        catch (IOException e) {
            return null;
        }
        return buf;
    }
}
 
// end JavaCompile Linux
 