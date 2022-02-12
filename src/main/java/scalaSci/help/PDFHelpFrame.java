
package scalaSci.help;

import com.sun.pdfview.PDFViewer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;


public class PDFHelpFrame  extends JFrame {
  BufferedImage [] images;
  
      
        
      public PDFHelpFrame(File pdfFile) {
            
            PDFViewer pdfv = new PDFViewer(false);
            try {
                pdfv.openFile(pdfFile);
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
         
        
      }      

      
        
          

}
