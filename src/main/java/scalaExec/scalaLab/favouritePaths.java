
package scalaExec.scalaLab;

import scalaExec.Interpreter.GlobalValues;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;
import javax.swing.JComboBox;
import scala.reflect.io.Directory;

public class favouritePaths {
   // save favourite paths of a session to a file
     public static void saveFavouritePaths(String fileName, JComboBox fvCB)
    {
         fileName = Directory.Current().get().path()+File.separator+fileName;
        try
        {    	
            //create streams
            FileOutputStream output = new FileOutputStream(fileName);
            
            //create object stream
           OutputStreamWriter  commandWriter= new OutputStreamWriter(output);
            
            int numPaths = fvCB.getItemCount();
            for (int k=0; k<numPaths; k++)  {  // write favourite paths
                String currentFavouritePath = fvCB.getItemAt(k) + "\n";
                commandWriter.write(currentFavouritePath, 0, currentFavouritePath.length());
            }
            commandWriter.close();
            output.close();
        }
            
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in saveFavouritePaths");
            System.out.println(except.getMessage());
            //except.printStackTrace();
        }
   }
   
   //  load favourite paths of a session from a file
     public static void loadFavouritePaths(String fileName, JComboBox cbToFill)
    {
         GlobalValues.favouriteElements.clear();
         fileName = Directory.Current().get().path()+File.separator+fileName;
            //create streams
            try {
            FileInputStream input = new FileInputStream(fileName);
            
            //create object stream
           BufferedReader  commandReader= new BufferedReader(new InputStreamReader(input));
           cbToFill.removeAllItems();
           String currentLine;
           while ((currentLine = commandReader.readLine()) != null)  {
               if (GlobalValues.favouriteElements.contains(currentLine) == false) {
                   GlobalValues.favouriteElements.add(currentLine);
                   cbToFill.addItem(currentLine);
                } 
             }
            }
        catch (java.io.FileNotFoundException e) {
            System.out.println("File "+ fileName+" cannot be opened for reading command history list");
        }    
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in readCommandHistory");
            System.out.println(except.getMessage());
            //except.printStackTrace();
        }
   }

}
