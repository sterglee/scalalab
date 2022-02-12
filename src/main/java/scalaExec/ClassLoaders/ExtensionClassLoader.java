package scalaExec.ClassLoaders; 


import scalaExec.Interpreter.GlobalValues;
import java.io.*;

/**  This class implements a simple class loader  that can be used to load at runtime 
 * classes contained in paths specified in ScalaSciClassPath
 *
 */
public class ExtensionClassLoader extends ClassLoader
{
    String [] dirs;
    
    static public int classesAdditionalFnt=0;  // classes loaded from additional paths (i.e. not with the parent classloader)
    
    public ExtensionClassLoader( String path) {
        dirs = path.split(System.getProperty("path.separator"));  // use both the Linux/Unix (i.e. ":") and the Windows separators (i.e. ";")
        try {
                   extendClassPath(scalalab.JavaGlobals.ARPACKFile);
                   extendClassPath(scalalab.JavaGlobals.jarFilePath);
                   extendClassPath(scalalab.JavaGlobals.ejmlFile);
                   extendClassPath(scalalab.JavaGlobals.jblasFile);
                   extendClassPath(scalalab.JavaGlobals.jsciFile);
                   extendClassPath(scalalab.JavaGlobals.mtjColtSGTFile);
                   extendClassPath(scalalab.JavaGlobals.ApacheCommonsFile);                   
                   extendClassPath(scalalab.JavaGlobals.numalFile);
                   extendClassPath(scalalab.JavaGlobals.LAPACKFile);
              
                
           //   any .jar file in the defaultToolboxes folder is automatically appended to classpath 
          String  defaultToolboxesFolder = GlobalValues.scalalabLibPath.replace("lib", "defaultToolboxes");
          //System.out.println("appending toolboxes of DefaultToolboxes folder:  "+defaultToolboxesFolder);
          
          File [] toolboxesFolderFiles = (new java.io.File(defaultToolboxesFolder)).listFiles();  // get the list of files at the default toolboxes folder
          if (toolboxesFolderFiles!=null) {  // DefaultToolboxes folder not empty
           int numFiles = toolboxesFolderFiles.length; 
           for (int f=0; f < numFiles;  f++) {
               String currentFileName = toolboxesFolderFiles[f].getAbsolutePath();
           
                  if (currentFileName.endsWith(".jar")) {
               
                  extendClassPath(currentFileName);
                 
               }  // endsWith("jar")
            }   // for all files of then DefaultToolboxes folder
          }   // DefaultToolboxes folder not empty
          
        }
        catch (Exception e) {System.out.println("exception appending classpath for Java REPL"+e.getMessage());};

    }
    
    
    public ExtensionClassLoader( String path, ClassLoader parent) {
        super(parent);
        dirs = path.split(System.getProperty("path.separator")); //// use both the Linux/Unix (i.e. ":") and the Windows separators (i.e. ";")
    }
    
        
    // extend the class path in order to search the path(s) designated with the parameter path also
    public void extendClassPath( String path)  {
        // if path is already in the extension's loader class path do not reinsert it
        for (int k=0; k<dirs.length; k++) {
            if (dirs[k].equals(path)) 
                return;
        }
        String [] exDirs = path.split(System.getProperty("path.separator"));  // vector with the paths to be added
        String [] newDirs = new String[dirs.length+exDirs.length];
        System.arraycopy(dirs, 0, newDirs, 0, dirs.length);  // copy current path
        System.arraycopy(exDirs, 0, newDirs, dirs.length, exDirs.length);  // append new paths 
        dirs = newDirs;
        }
    
    public synchronized Class findClass( String name)  
                throws  ClassNotFoundException
    {
     
        Class sysClass = findSystemClass(name);
        if (sysClass!=null)
            return sysClass;
    
        for (int i=0; i < dirs.length; i++)  {  // for all dirs of extension class path
            byte [] buf =  getClassData( dirs[i], name);  // class found
            if (buf != null)  {
                classesAdditionalFnt++;  // one more class found at the additional paths
                return defineClass(name, buf, 0, buf.length);
            }
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






                