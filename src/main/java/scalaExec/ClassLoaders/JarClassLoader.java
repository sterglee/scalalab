package scalaExec.ClassLoaders;


import java.awt.Graphics2D;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import scalaExec.Interpreter.GlobalValues;
import scalaExec.gui.AutoCompletionScalaSci;

/**
 * This class implements a simple class loader  that can be used to load at runtime 
 * classes contained in a JAR file.
 *
 */
public class JarClassLoader extends ExtensionClassLoader // implements ScalaClassLoader
{
    public int  numLoadedClass;
    Hashtable  clTable;  // reference to the global hashtable of loaded classes
    TreeSet    sortedLoadedClasses;   // keeps sorted the loaded classes of the toolbox
            
    JFrame progressFrame;
    JPanel progressPanel;
    Graphics2D   g2d;
    String  dots = ".";
    int numJarAutoCompletionItems=0;
    Vector avoidClassPatterns = new Vector();  // some classes can be problematic for autocompletion or for loading their methods. We can specify them
                            // in a file name "AvoidClassPatterns.txt" in order to avoid loading them
        /**
   * Creates a new JarClassLoader that will allow the loading
   * of classes stored in a jar file.
   *
   * @param jarFileName   the name of the jar file
   * @exception IOException   an error happened while reading
   * the contents of the jar file
   */
    
    public JarClassLoader() {
        super("");//  (ClassLoader) GlobalValues.scalalabMainFrame.scalalabConsole.scalaInterpreter.scala$tools$nsc$Interpreter$$classLoader());

    }


 
    // scan the jarFileName for patterns beginning with LibDir
  public  static Vector scanLib(String jarFileName, String LibDir)  
  {
    Vector scannedLibs = new Vector();  
    int libPathLen = LibDir.length();  // the length of the prefix
    int numLibAllClasses = 0;      // number of classes
    int numLibAllMethods=0;     // number of methods
    //  get reference to the global Hashtable that holds all loaded classes
  JarEntry je;
  JarInputStream jis=null;
        try {
            jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            while ((je = jis.getNextJarEntry()) != null)
                {   // while jar file has entries unprocessed
                 String nameOfEntry = je.toString();
                 
          // make sure we have only slashes, i.e. use UNIX path conventions
                  String name ='/'+je.getName().replace('\\', '/');
          
                 int strLen = nameOfEntry.length();
                 if (strLen > libPathLen) {   // entry can be considered further
                     
                     nameOfEntry = nameOfEntry.substring(0, libPathLen);   // extract prefix

                     if (nameOfEntry.equalsIgnoreCase(LibDir))  { // scan only entries starting with LibDir subdirectory
                  
                      String javaName = name.replace('/', '.');
                      String lowerCaseJavaName = javaName.toLowerCase();  
                      if (lowerCaseJavaName.contains("test") == false && lowerCaseJavaName.contains("$") == false) {  // avoid test classes
                            
                            int idx = javaName.lastIndexOf(".class");
                            int javaNameLen = javaName.length();
                            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
                            if  (idx != -1 && !classStringIsWithinName) {  // a class file
                            javaName = javaName.substring(1, idx);    // remove the first '.'
                 
                  
                         Class  foundClass = null;  
                             try {
                     foundClass = GlobalValues.globalInterpreter.classLoader().loadClass(javaName);
                       }
                       catch (ClassNotFoundException e) {
                           foundClass = null;
                       }
                    if (foundClass!=null)
                        scannedLibs.add(foundClass);
                      
                       }   // a class file
                      }  // avoid test classes
                      
                   }   // scan only  LibDir  subdirectory
                 
              } // strLen > libPathLen
            } // while jar file has entries unprocessed
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            jis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
  
 return scannedLibs;  // return the Vector of methods
 }
 

  

  /**
     * loads the built-in NUMAL library classes from the .jar file
     * Numerical analysis functions of the NUMAL library are housed at the directory /numal.
     * Returns the number of the classes that successfully loaded into the 
     * global loadedClasses table
     **/
  public  int  scanBuiltInNumAllClasses(String jarFileName, TreeSet classesOfToolbox)    throws IOException 
  {
    String  LibDir = "numal";   //  NUMAL library functions
    int libPathLen = LibDir.length();
    int numNumAllClasses = 0;    
    int numNumAllMethods=0;
    //  get reference to the global Hashtable that holds all loaded classes
  JarEntry je;
  JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
       int strLen = nameOfEntry.length();
       if (strLen > libPathLen) {  // strLen > libPathLen
        nameOfEntry = nameOfEntry.substring(0, libPathLen);
       if (nameOfEntry.equalsIgnoreCase(LibDir))  { // scan only  LibDir  subdirectory
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
        String   remainingClassName = name.substring(libPathLen+1, name.length());
        
            String javaName = name.replace('/','.');
          if (javaName.indexOf("Test") == -1)     { // do not consider the testing classes of the NUMAL package
            int idx = javaName.lastIndexOf(".class");
            int javaNameLen = javaName.length();
            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
            if (idx != -1 && !classStringIsWithinName) {  // a class file
            javaName = javaName.substring(1, idx);    // remove the first '.'
       
        
               Class  foundClass = null;  
                   try {
           foundClass = GlobalValues.globalInterpreter.classLoader().loadClass(javaName);
             }
             catch (ClassNotFoundException e) {
                 foundClass = null;
             }

           if (javaName.indexOf("$") == -1) {
        String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName;
        AutoCompletionScalaSci.scanMethodsScalaSci.add(nameToInsert);
        numNumAllClasses++;
           }

        if (foundClass != null)  {
            Method [] classMethods = foundClass.getDeclaredMethods();
            for (Method currentMethod: classMethods) {
                if (Modifier.isPublic(currentMethod.getModifiers() )) {
                    String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackageSeparator+javaName;
                    if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(methodName)==-1)  {
                         AutoCompletionScalaSci.scanMethodsScalaSci.add(methodName);
                         numNumAllMethods++;
                    }
                         
               }
          }
        }
            }   // a class file
            }    // do not consider testing classes of the NUMAL package
         }   // scan only  LibDir  subdirectory
       
    } // strLen > libPathLen
  } // while jar file has entries unprocessed
  
 jis.close();
        
 System.out.println("NUMAL  Classes = "+numNumAllClasses);
 System.out.println("NUMAL  Methods = "+numNumAllMethods);
  
 return numNumAllClasses;
 }
 
  
  
    
  /**
     * scans the built-in Scala library classes from the .jar file
     * Scala classes are housed at the directory /Scala .
     * Returns the number of the classes that successfully accessible and fills their information for autocompletion
     **/
  public  int  scanBuiltInScalaSciClasses(String jarFileName)    throws IOException 
  {
    String  ScalaSciDir = "scalaSci";   // matrix functions, plotting functions etc.
    String ScalaSciCommands = "scalaSciCommands";
    String  jplotDir = "JFPlot"; 
    int libPathLen = jplotDir.length();  // the length of the smallest dir
    int numScalaLoadedClasses = 0;    
    int numScalaMethods = 0;
    String NRRoutines = "com";
    
    
    JarEntry je;
    JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
       int strLen = nameOfEntry.length();
       if (strLen > libPathLen) {  
           int idxSeparator = nameOfEntry.indexOf('/');
           if (idxSeparator != -1)  {
       nameOfEntry = nameOfEntry.substring(0, idxSeparator);
// scan only subdirectories that correspond to ScalaSci's libraries
       if ( nameOfEntry.equalsIgnoreCase(ScalaSciDir) || nameOfEntry.equalsIgnoreCase(ScalaSciCommands) || nameOfEntry.equalsIgnoreCase(jplotDir)   || nameOfEntry.equalsIgnoreCase(NRRoutines))  { 
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
          
        String javaName = name.replace('/','.');
        int idx = javaName.lastIndexOf(".class");
        int javaNameLen = javaName.length();
        boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
        if (idx != -1 && !classStringIsWithinName) {  // a class file
        javaName = javaName.substring(1, idx);    // remove the first '.'
  
        if (javaName.indexOf("$") == -1) {   // not a special class
       
  Class  foundClass = null;  
                   try {
           foundClass = GlobalValues.globalInterpreter.classLoader().loadClass(javaName);
             }
             catch (Exception e) {
                 foundClass = null;
             }
            
           if (foundClass != null)  {
            Method [] classMethods = foundClass.getDeclaredMethods();
            for (Method currentMethod: classMethods) {
                if (Modifier.isPublic(currentMethod.getModifiers() )) {
                    String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackageSeparator+javaName;
                  if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(methodName)==-1) {
                    AutoCompletionScalaSci.scanMethodsScalaSci.add(methodName);
                    numScalaMethods++;
                  }
                 }
            }
         }  // foundClass != null

  
        String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName;
        AutoCompletionScalaSci.scanMethodsScalaSci.add(nameToInsert); 
        numScalaLoadedClasses++;
                     
                   }     // not a special class
                 }   // a class file
              }
            } // strLen > libPathLen
       }  // idxDot != -1
  } // while jar file has entries unprocessed
  
 jis.close();
        
 System.out.println("ScalaSci Classes = "+numScalaLoadedClasses);
 System.out.println("ScalaSci Methods = "+numScalaMethods);
  
 return numScalaLoadedClasses;
 }

  /**
     * scans the basic library classes
     * Returns the number of the classes that successfully accessible and fills their information for autocompletion
     **/
  public  int  scanLibsScalaSciClasses(String jarFileName)    throws IOException 
  {
    String  JSciDir = "JSci";   
    String numalDir = "numAll";
    String ejmlDir = "org";
    int libPathLen = ejmlDir.length();  // the length of the smallest dir
    int numScalaLoadedClasses = 0;    
    int numScalaMethods = 0;
    
    JarEntry je;
    JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
       int strLen = nameOfEntry.length();
       if (strLen > libPathLen) {  
           int idxSeparator = nameOfEntry.indexOf('/');
           if (idxSeparator != -1)  {
       nameOfEntry = nameOfEntry.substring(0, idxSeparator);
// scan only subdirectories that correspond to ScalaSci's libraries
       if ( nameOfEntry.equalsIgnoreCase(JSciDir) || nameOfEntry.equalsIgnoreCase(numalDir) || nameOfEntry.equalsIgnoreCase(ejmlDir) )  { 
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
          
        String javaName = name.replace('/','.');
        int idx = javaName.lastIndexOf(".class");
        int javaNameLen = javaName.length();
        boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
        if (idx != -1 && !classStringIsWithinName) {  // a class file
        javaName = javaName.substring(1, idx);    // remove the first '.'
  
        if (javaName.indexOf("$") == -1) {   // not a special class
       
  Class  foundClass = null;  
                   try {
           foundClass = GlobalValues.globalInterpreter.classLoader().loadClass(javaName);
             }
             catch (Exception e) {
                 foundClass = null;
             }
            
           if (foundClass != null)  {
            Method [] classMethods = foundClass.getDeclaredMethods();
            for (Method currentMethod: classMethods) {
                if (Modifier.isPublic(currentMethod.getModifiers() )) {
                    String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackageSeparator+javaName;
                  if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(methodName)==-1) {
                    AutoCompletionScalaSci.scanMethodsScalaSci.add(methodName);
                    numScalaMethods++;
                  }
                 }
            }
         }  // foundClass != null

  
        String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName;
        AutoCompletionScalaSci.scanMethodsScalaSci.add(nameToInsert); 
        numScalaLoadedClasses++;
                     
                   }     // not a special class
                 }   // a class file
              }
            } // strLen > libPathLen
       }  // idxDot != -1
  } // while jar file has entries unprocessed
  
 jis.close();
        
 System.out.println("ScalaSci Classes = "+numScalaLoadedClasses);
 System.out.println("ScalaSci Methods = "+numScalaMethods);
  
 return numScalaLoadedClasses;
 }

  
  
   
 
 public static void copyStreams( InputStream in, OutputStream out, int bufLen ) {
      byte [] buf = new byte[bufLen];
      int bytesReaded;
      try {
      while  ( (bytesReaded = in.read(buf, 0, buf.length)) != -1)  {
          out.write(buf, 0, bytesReaded);
      }
      out.flush();
      }
      catch (IOException ioe) { ioe.printStackTrace(); return; }
  }
  

   /*
  public static void appendZip( String  fromZip, String toZip)   throws IOException {
   ZipEntry jie;
   ZipInputStream jis = new ZipInputStream(new BufferedInputStream  (new FileInputStream(fromZip)));
   ZipOutputStream  jos = new ZipOutputStream(new BufferedOutputStream  (new FileOutputStream(toZip, true)) );
   while ((jie = jis.getNextEntry()) != null) {
       String nameOfEntry = jie.toString();
       int siz = (int)jie.getSize();
      System.out.println(nameOfEntry+" size = "+siz);

      jos.putNextEntry(new ZipEntry(jie.getName()));
       
       if (siz >0) {
        copyStreams(jis, jos, siz);
      }
      }
      jos.close();
      
}
*/

public static void appendJar( String  fromJar, String toJar)   throws IOException {
   JarInputStream in = new JarInputStream(new FileInputStream(fromJar)); // oldJarName is the JAR that contains all the files we want to keep
   String toJarComp = toJar.substring(toJar.lastIndexOf(File.separatorChar)+1, toJar.length());

   String tempJarName = fromJar.replaceAll(".jar", "")+toJarComp;
   JarOutputStream out = new JarOutputStream(new FileOutputStream(tempJarName)); // this is your "out" variable

// copy the files from the old JAR to the new, but don't close the new JAR yet
JarEntry inEnt;
while ((inEnt = in.getNextJarEntry()) != null) {
  JarEntry outEnt = new JarEntry(inEnt); // copy size, modification time etc.
  byte[] data = new byte[(int)inEnt.getSize()];
  in.read(data); // read data for this old entry
  in.closeEntry();
  out.putNextEntry(outEnt);
  out.write(data); // copy it to the new entry
  out.closeEntry();
}
in.close();

in = new JarInputStream(new FileInputStream(toJar));
// copy the files from the old JAR to the new, but don't close the new JAR yet
while ((inEnt = in.getNextJarEntry()) != null) {
  JarEntry outEnt = new JarEntry(inEnt); // copy size, modification time etc.
  byte[] data = new byte[(int)inEnt.getSize()];
  in.read(data); // read data for this old entry
  in.closeEntry();
  out.putNextEntry(outEnt);
  out.write(data); // copy it to the new entry
  out.closeEntry();
}
// and *now* we close the new JAR file.
out.close();
in.close();

// We then delete the old JAR file...
//File origFile = new File(fromJar);
//boolean status = origFile.delete();
//File tempJarFile = new File(tempJarName);

// ... and rename the new JAR file to use the old one's name.
//tempJarFile.renameTo(origFile);
}


public static void appendZip( String  fromJar, String toJar)   throws IOException {
   ZipInputStream in = new ZipInputStream(new FileInputStream(fromJar)); // oldJarName is the JAR that contains all the files we want to keep
   String toJarComp = toJar.substring(toJar.lastIndexOf(File.separatorChar)+1, toJar.length());

   String tempJarName = fromJar.replaceAll(".jar", "")+toJarComp;
   int response = JOptionPane.showOptionDialog(null, "Create file: "+tempJarName+" from "+fromJar+" and "+toJar+ " ?",  "Confirm toolbox creation", 
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
   if (response == JOptionPane.OK_OPTION)  {
   
       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempJarName)); // this is your "out" variable

// copy the files from the old JAR to the new, but don't close the new JAR yet
ZipEntry inEnt;
while ((inEnt = in.getNextEntry()) != null) {
  ZipEntry outEnt = new ZipEntry(inEnt); // copy size, modification time etc.
  byte[] data = new byte[(int)inEnt.getSize()];
  in.read(data); // read data for this old entry
  in.closeEntry();
  out.putNextEntry(outEnt);
  out.write(data); // copy it to the new entry
  out.closeEntry();
}
in.close();

in = new JarInputStream(new FileInputStream(toJar));
// copy the files from the old JAR to the new, but don't close the new JAR yet
while ((inEnt = in.getNextEntry()) != null) {
  ZipEntry outEnt = new ZipEntry(inEnt); // copy size, modification time etc.
  byte[] data = new byte[(int)inEnt.getSize()];
  in.read(data); // read data for this old entry
  in.closeEntry();
try{
    out.putNextEntry(outEnt);
    out.write(data); // copy it to the new entry
    out.closeEntry();
}
catch (IOException ioe) {
    System.out.println("exception trying to put entry "+outEnt.getName());
    out.closeEntry();
}

}
// and *now* we close the new JAR file.
out.close();
in.close();
    }
// We then delete the old JAR file...
//File origFile = new File(fromJar);
//boolean status = origFile.delete();
//File tempJarFile = new File(tempJarName);

// ... and rename the new JAR file to use the old one's name.
//tempJarFile.renameTo(origFile);
}


  public static void main(String [] args) {
   try {
     appendJar("C:\\test\\jfes.jar" , "c:\\test\\na.jar");
   }
  catch (IOException ie) {
       System.out.println("exception I/O");
       System.out.println("exception = "+ie.getMessage());
       ie.printStackTrace();
       return;
     }
   }
  
  // returns true if the Java class javaName is not within the class names that we have to avoid
  private boolean checkAllowed(String javaName) {
    for (int k=0; k<avoidClassPatterns.size(); k++) {
        String currentClassToAvoid = (String)avoidClassPatterns.get(k);
        if (javaName.contains(currentClassToAvoid))
            return false;
      }
    return true;
    }
      
  
  // scan the jarFileName for classes without loading them for extracting information
  public  int  scanAllToolboxClasses(String jarFileName)    throws IOException
  {

    int numOfPatternsToAvoid = 0;
    int numJarClasses = 0;    
    //  get reference to the global Hashtable that holds all loaded classes
    String startupCode = null, patternsToAvoid = null;
    
    numJarAutoCompletionItems=0;
     
    progressFrame = new JFrame("Reading ..");
     
    progressPanel = new JPanel();
    progressFrame.add(progressPanel);
    progressFrame.setSize(400, 100);
    progressFrame.setVisible(true);
    g2d = (Graphics2D) progressPanel.getGraphics();
      
    GlobalValues.scalalabMainFrame.scalalabConsole.interpreterWithAppendedCP(jarFileName);    // create a Scala interpreter that includes in its classpath the toolbox file

    HashMap <String, Boolean> specifiedToolboxes = GlobalValues.jartoolboxesLoadedFlag;  // the table of already loaded toolboxes
    boolean toolboxInited = specifiedToolboxes.containsKey(jarFileName);
    if (toolboxInited == false)  {  // load the specified jar toolbox
     
        GlobalValues.jartoolboxesLoadedFlag.put(jarFileName, true);
        
        int toolboxIndex = 0;   // the scalaSciToolbox class keeps the name of the .jar file of each toolbox and a Vector that holds the bytecodes of its classes
        scalaExec.scalaLab.scalaSciToolbox  ssciToolbox = new scalaExec.scalaLab.scalaSciToolbox();
        ssciToolbox.toolboxName = jarFileName;
       
        JarEntry je;
  JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  
  
    //  scan first the jar for the file "AvoidClassPatterns.txt" and for the startup code
    while  ((je = jis.getNextJarEntry()) != null)  {
        String nameOfEntry = je.toString();
       
        if (nameOfEntry.contains("AvoidClassPatterns.txt"))  {
             patternsToAvoid = readTextFromJar(jarFileName, nameOfEntry);    // get the patterns to avoid from the .jar file
             StringTokenizer strTok = new StringTokenizer(patternsToAvoid, " \n,\t;");
             numOfPatternsToAvoid =strTok.countTokens();
             while (strTok.hasMoreElements())  
                 avoidClassPatterns.add(strTok.nextToken());
             break;
         }
        
    }
  
  
  jis.close();
  jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
    while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
      
         if (nameOfEntry.contains("startup.ssci"))  {
             startupCode = readTextFromJar(jarFileName, nameOfEntry);    // get the startup code from the .jar file
         }
      
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
  
            String javaName = name.replace('/','.');
            int idx = javaName.lastIndexOf(".class");
            int javaNameLen = javaName.length();
            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
            if (idx != -1 && !classStringIsWithinName) {  // a class file
               javaName = javaName.substring(1, idx);    // remove the first '.'
       
             if (checkAllowed(javaName) == true)   {   // not a class name that we should avoid to load  
               Object  foundClass=null;
               
          try {
                 foundClass = GlobalValues.globalInterpreter.classLoader().loadClass(javaName);
          }
             catch (Exception   e) { 
                   foundClass = null;
               }
               
             
         boolean classIsPublic = false;
          if (foundClass != null)  {
              int modifier = ((Class) foundClass).getModifiers();
               if (Modifier.isAbstract(modifier)==false && Modifier.isInterface(modifier)== false &&  Modifier.isStatic(modifier)==false)    {  // class is not abstract or interface
                 if ( Modifier.isPublic(modifier) )  {  // class is public
                     classIsPublic = true;
                   ssciToolbox.toolboxClasses.add(foundClass);
                 AutoCompletionScalaSci.scanMethodsScalaSci.add(((Class)foundClass).getName());
                  numJarAutoCompletionItems++;
              if (numJarAutoCompletionItems % 100 == 0) {   // update visual progress
                    dots += ".";
                    String classCntDots = "Classes: "+ numJarAutoCompletionItems+"  "+dots;
                    g2d.clearRect(0, 0, 400, 100);
                    g2d.drawString(classCntDots, 20, 10);
                }
          
         }  // class is public  
                 if (GlobalValues.retrieveAlsoMethods && classIsPublic)  {    // classIsPublic
                     Method [] classMethods=null;
                   try {
             classMethods = ((Class)foundClass).getDeclaredMethods();
            if (classMethods != null)
             if (classMethods.length > 0)
              for (Method currentMethod: classMethods) {
                if (Modifier.isPublic(currentMethod.getModifiers() )) {
                    
                  
                      String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackageSeparator+javaName;
                  if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(methodName)==-1) {
                    AutoCompletionScalaSci.scanMethodsScalaSci.add(methodName);
                     numJarAutoCompletionItems++;
                      }
                   }
                   }
                   }
                   catch (SecurityException e) {
                       System.out.println("Security Exception in getDeclaredMethods");
                   }
                     catch (Exception e) {
                         System.out.println("Exception in getDeclaredMethods");
                         
                     }
                   }   // classIsPublic
if (javaName.indexOf("$") == -1) {
       
         String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName;
        //String elemToInsert = nameToInsert+" #Toolbox  class";
        if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(nameToInsert) == -1)   {
           AutoCompletionScalaSci.scanMethodsScalaSci.add(nameToInsert);
           numJarAutoCompletionItems++;
           numJarClasses++;
                         
                      }
                  }     // class is public
               }  // class is not abstract or interface
           }    // foundClasss != null
            
           
        }   // a class file
            } //
    }  // while jar file has entries unprocessed
       
  //System.out.println("Found in toolbox "+jarFileName+" #classes = "+JarClassLoader.classesFnt);
 // construct a new AutoCompletion Object (needed in order to sort the list of methods, and to permit the access of the new methods)
  scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci = new scalaExec.gui.AutoCompletionScalaSci();  // create the autocompletion object
               
 jis.close();
 System.out.println("number of Toolbox LoadedClasses = "+numJarClasses);
 System.out.println("number of Toolbox AutoCompletion Items = "+numJarAutoCompletionItems);
 System.out.println("Total items of ScalaSci Autocompletion = "+ scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.scanMethodsScalaSci.size());
 
            scalaExec.scalaLab.scalaSciToolboxes.ssciToolboxes.add(ssciToolbox);
            
            // try to execute any startup code
          if (startupCode!=null)             
              GlobalValues.scalalabMainFrame.scalalabConsole.interpretLine(startupCode);
 
    }   // load the specified jar toolbox

       progressFrame.setVisible(false);
       progressFrame.dispose();
   
    return numJarAutoCompletionItems;
 
 }
   
    
    
    public static Vector  scanAll( String jarFileName)  {
        int numLibAllClasses = 0;
        int numLibAllMethods=0;
        Vector scannedLibs = new Vector();
        
        //  get reference to the global Hashtable that holds all loaded classes
        JarEntry je;
        JarInputStream jis=null;
        try {
            jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        
        try {
            while ((je = jis.getNextJarEntry()) != null)
            {   // while jar file has entries unprocessed
                String nameOfEntry = je.toString();
                
                int strLen = nameOfEntry.length();
                
                // make sure we have only slashes, i.e. use Unix path conventions
                String name ='/'+je.getName().replace('\\', '/');
                
                String javaName = name.replace('/','.');
                int idx = javaName.lastIndexOf(".class");
                int javaNameLen = javaName.length();
                boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
                if (idx != -1 && !classStringIsWithinName) {  // a class file
                    javaName = javaName.substring(1, idx);    // remove the first '.'
                    
                    
                    Class  foundClass = null;
                    try {
                        foundClass = GlobalValues.extensionClassLoader.loadClass(javaName);
                    }
                    catch (Exception e) {
                        foundClass = null;
                    }
                    
                    if (javaName.indexOf("$") == -1) {
                        String smallName = javaName.substring(javaName.lastIndexOf(".")+1, javaName.length());
                        String nameToInsert = smallName+GlobalValues.smallNameFullPackageSeparator+javaName;
                        AutoCompletionScalaSci.scanMethodsScalaSci.add(nameToInsert);
                        numLibAllClasses++;
                    }
                    
                    if (foundClass==null)
                        scannedLibs.add(javaName);
                    else  {
                        scannedLibs.add(foundClass);
                        try {
                            
                            
                            
                            Method [] classMethods = foundClass.getDeclaredMethods();
                            for (Method currentMethod: classMethods) {
                                if (Modifier.isPublic(currentMethod.getModifiers() )) {
                                    // String methodName = currentMethod.getName()+GlobalValues.smallNameFullPackagSeparator+javaName;
                                    
                                    String methodName = currentMethod.getName();
                                    Class<?> [] methodArgs = currentMethod.getParameterTypes();
                                    
                                    
                                    // build the method's argument list
                                    StringBuilder args = new StringBuilder();
                                    int nargs = methodArgs.length;
                                    int argcnt=0;
                                    for (Class arg: methodArgs) {
                                        args.append(arg.getName());
                                        if (++argcnt < nargs) // avoid adding ","  after the last argument
                                            args.append(", ");
                                    }
                                    
                                    methodName = methodName+"(" + args.toString()+")";
                                    if (AutoCompletionScalaSci.scanMethodsScalaSci.indexOf(methodName)==-1)  {
                                        AutoCompletionScalaSci.scanMethodsScalaSci.add(methodName);
                                        numLibAllMethods++;
                                    }
                                }
                            }
                        }  // try
                        
                        
                        catch (Exception e ) {}
                    }   // foundClass != null
                
                } // a class file
            } // while jar file has entries unprocessed
        } catch (IOException ex) {
             System.out.println(ex.toString());
        }
        
        try {
            jis.close();
        } catch (IOException ex) {
             System.out.println(ex.toString());
        }
        
        return scannedLibs;
    }
    
 
  // scan the jarFileName for classes without loading them for extracting information
  public  Vector  scanAllJarClasses(String jarFileName)    throws IOException
  {

    Vector  jarClasses = new Vector();  
    //  get reference to the global Hashtable that holds all loaded classes
      
   JarEntry je;
  JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
  
            String javaName = name.replace('/','.');
            int idx = javaName.lastIndexOf(".class");
            int javaNameLen = javaName.length();
            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
            if (idx != -1 && !classStringIsWithinName) {  // a class file
                javaName = javaName.substring(1, javaNameLen);    // remove the first '.'
       
               jarClasses.add(javaName);
             
        }   // a class file
    }  // while jar file has entries unprocessed
       
               
 jis.close();
   
    return  jarClasses;
 
 }
  
  
 
    
    
 
  // scan the jarFileName for the classes in order to propose the proper import statements
  public  static  String  scanClass(String jarFileName, String classNameToSearch)    throws IOException
  {

    String proposedImports="";
      
   JarEntry je;
  JarInputStream jis = new JarInputStream(new BufferedInputStream  (new FileInputStream(jarFileName)));
  while ((je = jis.getNextJarEntry()) != null)
      {   // while jar file has entries unprocessed
       String nameOfEntry = je.toString();
       
// make sure we have only slashes, i.e. use Unix path conventions
	String name ='/'+je.getName().replace('\\', '/');
  
            String javaName = name.replace('/','.');
            int idx = javaName.lastIndexOf(".class");
            int javaNameLen = javaName.length();
            boolean classStringIsWithinName = javaNameLen > ( idx+".class".length() );
            if (idx != -1 && !classStringIsWithinName  && javaName.contains(classNameToSearch)) {  // a class file
                javaName = javaName.substring(1, javaNameLen);    // remove the first '.'
       
               javaName = javaName.replace(".class", "");
               
               proposedImports +="import  "+  javaName+"\n";
             
        }   // a class file
    }  // while jar file has entries unprocessed
       
               
 jis.close();
   
    return  proposedImports;
 
 }
  
  
 

  
 
  public String  readTextFromJar(String jarFileName, String s) {
       StringBuilder  initCode = new StringBuilder();
       String code="";
       
               try {
      JarFile jarFile = new JarFile(jarFileName);
     InputStream in = jarFile.getInputStream(jarFile.getEntry(s));
     Scanner inScanner = new Scanner(in);
     while (inScanner.hasNextLine())
         initCode.append(inScanner.nextLine()+"\n");
       code = initCode.toString();
     
               }
    catch (IOException ioe)  
    { 
        ioe.printStackTrace();
    }
 
       return code;
       
  }


  
}

