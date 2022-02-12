package scalaSciCommands

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.swing.JOptionPane
import java.net.URL
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Enumeration
import java.util.Locale
import java.util.Vector
import scala.Option
import scala.tools.nsc.io.Directory

object FileOps {

// returns directory entries using a Scala regular expression re
  def  xdir(re: String)  = {
        val sb = new StringBuilder()
        val regExp = re.r
        val  directoryToDisplay =  Directory.Current.get.path
        
        val  pathName = new  File(directoryToDisplay)
        val  fileNames = pathName.list()

        var filesInDirList =  List[String]()
        var foldersInDirList = List[String] ()
        // enumerate all files in the directory
        for (i<-0 until  fileNames.length)
        {
        	var currentFileName = fileNames(i)
        	if ( (regExp findFirstIn  currentFileName) != None) {
            var  f = new File(pathName.getPath, currentFileName)
            if (f.isDirectory()) {
                sb.append("\n "+currentFileName+"    <dir>")
                foldersInDirList = currentFileName :: foldersInDirList
            }
            else {
                sb.append("\n"+currentFileName)
                filesInDirList = currentFileName :: filesInDirList
            }
          }
        }
    println(sb.toString())   // print the results to the console
         
    (filesInDirList, foldersInDirList)   // return the two lists
  }

//  display the contents of the current directory and return the files and folders that the 
//  current directory contains in two lists 
def  dir()  = {
        val sb = new StringBuilder()
        val  directoryToDisplay =  Directory.Current.get.path
        
        val  pathName = new  File(directoryToDisplay)
        val  fileNames = pathName.list()

        var filesInDirList =  List[String]()
        var foldersInDirList = List[String] ()
        // enumerate all files in the directory
        for (i<-0 until  fileNames.length)
        {
            var  f = new File(pathName.getPath, fileNames(i))
            if (f.isDirectory()) {
                sb.append("\n "+fileNames(i)+"    <dir>")
                foldersInDirList = fileNames(i) :: foldersInDirList
            }
            else {
                sb.append("\n"+fileNames(i))
                filesInDirList = fileNames(i) :: filesInDirList
            }
         }
    println(sb.toString())       // print the contents to the Console
    (filesInDirList, foldersInDirList)   // return the list of files and folders of the current directory
         
  }

  
  // display the contents of the current directory
    def  dir(directory: String) =   {
        var sb = new StringBuilder()
        
        var directoryToDisplay = directory // the user specified  directory
        if (directory == "")   
             directoryToDisplay = Directory.Current.get.path
        else  
             directoryToDisplay = directory; 
        
        var  pathName = new  File(directoryToDisplay)
        var  fileNames = pathName.list()   // return the list of files the current directory contains
        var filesInDirList =  List[String]()
        var foldersInDirList = List[String] ()
        
        // enumerate all files in the directory
        for (i <- 0 until  fileNames.length)
        {
            var  f = new File(pathName.getPath(), fileNames(i))
            if (f.isDirectory())  {
            	sb.append("\n "+fileNames(i)+"    <dir>")
            	foldersInDirList = fileNames(i) :: foldersInDirList
            }
            else {
                sb.append("\n"+fileNames(i))
                filesInDirList = fileNames(i) :: filesInDirList
            }
         }
         println(sb.toString())   // print the contents of the specified directory
         (filesInDirList, foldersInDirList)   // return the list of files and folders the specified directory contains
  }

  // print the current working directory
  def  pwd() =  {
  	val  currentWorkingDirectory = Directory.Current.get.path
  	println("\nCurrent working directory is: "+currentWorkingDirectory)
  	currentWorkingDirectory
 }

  def ls() = dir()
  
  
  def lsR() = dirR()
  
    // recursively display the contents of the subdirectories
   def  dirR(directory: String ): String =  {
        var sb = new StringBuilder()
        var currentWorkingDirectory = directory
        
       var  pathName = new  File(currentWorkingDirectory)
       var fileNames = pathName.list()
        println("number of files = "+fileNames.length)
        // enumerate all files in the directory
        for (i <-  0 until  fileNames.length)
        {
            var  f = new File(pathName.getPath(), fileNames(i))
    
            var fullName = pathName.getPath()+File.separatorChar+fileNames(i)
    
            if (f.isDirectory())  {
                sb.append("\n "+fullName+"    <dir>")
                //println("calling recursively for "+fileNames(i))
                var nested = dirR(directory+File.separatorChar+fileNames(i))
                sb.append("\n\n"+nested+"\n\n")
             }
            else
                sb.append("\n"+fullName)
         }
        sb.toString()
        }
   
    // recursively display the contents of the current directory and of its subdirectories
   def dirR():String = {
        var  sb = new StringBuilder()
        var currentWorkingDirectory = Directory.Current.get.path
        
        var pathName = new  File(currentWorkingDirectory)
        var  fileNames = pathName.list()
        
        // enumerate all files in the directory
        for (i<-0 until  fileNames.length)
        {
            var f = new File(pathName.getPath(), fileNames(i))
            var fullName = pathName.getPath()+File.separatorChar+fileNames(i)
            
            if (f.isDirectory()==true)  {
                sb.append("\n "+fullName+"    <dir>")
                var nested = dirR(currentWorkingDirectory+File.separatorChar+fileNames(i))
                sb.append("\n\n"+nested+"\n\n")
             }
            else
                sb.append("\n"+fullName)
         }
         sb.toString()
        }
    
  
  // change the current working directory to the specified directory
  def  cd(newDirectory: String ) =     {
     var  newDir = newDirectory
      if (newDirectory == "..")  {   // change to "upper" directory
          var currentDirectory =  Directory.Current.get.path   // take the current directory
             // extract from the current directory the path corresponding to its parent
          var  pathSepIndex = currentDirectory.lastIndexOf(File.separatorChar) 
          currentDirectory = currentDirectory.substring(0, pathSepIndex)
          newDir = currentDirectory
      }
      
    // if an absolute path is specified retain it, otherwise construct an absolute path
    // by appending the specified directory to the current working directory
      var wholePath = ""
      if (newDir.charAt(0)==File.separatorChar || newDir.charAt(1)==':')  // absolute name
          wholePath = newDir
         else 
          {
       var currentWorkingDirectory = Directory.Current.get.path
       wholePath = currentWorkingDirectory+File.separatorChar+newDirectory
          }
          
       var f = new File(wholePath)
     // update the current working directory if it is valid  
    if (f.exists())
         System.setProperty("user.dir", wholePath)
  }

  
 def  mkdir(newDirectory: String ) =  {
      md(newDirectory)
  }

  // create a new directory
  def  md(newDirectory: String  ) =     {
      var  newDirectoryFullPathName = newDirectory
      var fNewDirectory:File = null
      if (newDirectory.charAt(0)==File.separatorChar || newDirectory.charAt(1)==':')
         fNewDirectory = new File(newDirectory) 	// absolute file path name
  else  // relative path name to the current working directory
     {
   // create the specified directory actually at the filesystem
   var directoryPath = Directory.Current.get.path

    newDirectoryFullPathName = directoryPath+File.separator+newDirectory
    fNewDirectory = new File(newDirectoryFullPathName)
    }

     var  userResponse = JOptionPane.YES_OPTION
     var dirExists = fNewDirectory.exists()
     if (dirExists)     // directory already exists
       JOptionPane.showMessageDialog(null, "Directory: "+newDirectoryFullPathName+" already exists")

       if (dirExists==false)   {  // create new directory and update tree
                    // create new directory
            fNewDirectory.mkdir()  // creates the corresponding directory
       
           }
       }

    
  }

  
