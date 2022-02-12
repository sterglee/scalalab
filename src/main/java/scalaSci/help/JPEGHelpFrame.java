
package scalaSci.help;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class JPEGHelpFrame  extends JFrame {
  BufferedImage [] images;
  
      
        
      public JPEGHelpFrame(File jpegFile) {
                    
          
          Box  box = Box.createVerticalBox();
          try {
       String suffix = "jpg";
       Iterator<ImageReader> iter  =  ImageIO.getImageReadersBySuffix(suffix);
       ImageReader reader = iter.next();
       ImageInputStream imageIn = ImageIO.createImageInputStream(jpegFile);
       reader.setInput(imageIn);
       int count = reader.getNumImages(true);
       images = new BufferedImage[count];
       for (int i = 0; i < count; i++) {
           images[i] = reader.read(i);
           box.add(new JLabel(new ImageIcon(images[i])));
         }
       }
          catch (IOException e) {
              JOptionPane.showMessageDialog(this, e);
          }
          
       setContentPane(new JScrollPane(box));
       validate();
       setSize(500,500);
       setVisible(true);
         
         
      }      

      
        
          

}
