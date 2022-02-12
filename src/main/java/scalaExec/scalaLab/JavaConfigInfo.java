
package scalaExec.scalaLab;

import java.util.Map;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import scalaExec.Interpreter.GlobalValues;

class JavaConfigInfo {
    
public static void displayJavaConfigInfo() {
    JFrame dframe = new JFrame("Java Runtime Information");
    final RSyntaxTextArea jt = new RSyntaxTextArea();
        
     jt.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize));
      
      jt.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
      jt.setCodeFoldingEnabled(true);
    
    StringBuilder sb = new StringBuilder();
    for (Map.Entry e:   System.getProperties().entrySet()) {
     String  se = (String) e.getKey();
     if (se.startsWith("java")) {
         //System.out.println("se = "+e.toString());
         sb.append(e.toString()+"\n" );
     }
    }
       jt.setText(sb.toString());
    
    RTextScrollPane   jp = new RTextScrollPane(jt);
    dframe.add(jp);
    dframe.setLocation(200, 200);
    dframe.setSize(400, 400);
    dframe.setVisible(true);
    
}
}
