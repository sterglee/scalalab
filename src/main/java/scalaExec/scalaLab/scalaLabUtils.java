
package scalaExec.scalaLab;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.*;

import java.lang.reflect.*;
import scalaExec.Wizards.JavaCompile;
import scalaExec.Interpreter.GlobalValues;

// class that implements utility functions used by other scalaLab's classes
public class scalaLabUtils {

   // appends all the subdirectories rooted at pathName at appendedStrings
   static public void appendAllSubDirectories(  String  pathName,  Vector  appendedStrings )  
   {
            int currentNumOfStrings = appendedStrings.size();
            
            appendedStrings.add(pathName);   // add current path to scalaLab script paths
            File filePath = new File(pathName);   
            String [] filesOfPath = filePath.list();    // take all files within this directory
            if (filesOfPath != null) {
            for (int f=0; f<filesOfPath.length; f++)  // add files under this directory 
                   {
                        String fullFileName = pathName+filesOfPath[f]+File.separatorChar;
                        File currentFile = new File(fullFileName);
                        if (currentFile.isDirectory())  // append subdirectories recursively
                            appendAllSubDirectories(fullFileName, appendedStrings);
                       } // add files under this directory
                    }     // fileofPath != null
               }  
   
   
}