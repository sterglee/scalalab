package scalainterpreter;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.Document;
import jsyntaxpane.*;
//  .scalainterpreter.testEditorPane
public class testEditorPane {
    
    JEditorPane jepane = new JEditorPane();
    JFrame testFrame =  new JFrame("test Frame");

    public testEditorPane() {
        testFrame.setSize(500, 500);
        jepane.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseMoved(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        
            public void mouseClicked(MouseEvent e) {
                JEditorPane editor = (JEditorPane) e.getSource();
    Point pt = new Point(e.getX(), e.getY());
    int pos = editor.viewToModel(pt);
    Document doc = editor.getDocument();
    
    }
    
           

            public void mousePressed(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseReleased(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseEntered(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void mouseExited(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        testFrame.add(jepane);
        testFrame.setVisible(true);
    }
    
    public static void main(String[] args) {
        testEditorPane testAppl = new testEditorPane();
       
        //jepane.rep
    }
    
    
}
