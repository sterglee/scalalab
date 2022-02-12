package utils;

import java.io.*;

public class FindDirectories {
    public static void removeHidden(String pathNameStr) {
        try {
            File pathName = new File(pathNameStr);
        
            String [] fileNames = pathName.list();
            
            // enumerate all files in the directory
            for (int i=0; i<fileNames.length; i++) {
                File f = new File(pathName.getPath(), fileNames[i]);
                
                if (f.isHidden()) {
                    System.out.println("removing: " + f.getName());
                    f.delete();
                }
                
                // if the file is agaimn a directory, call the  removeHidden method recursively
                if (f.isDirectory()) {
                    removeHidden(f.getPath());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
                    
        }
    }
    
}
