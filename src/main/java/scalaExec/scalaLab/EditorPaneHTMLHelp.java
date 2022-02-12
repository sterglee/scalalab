
package scalaExec.scalaLab;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import scalaExec.Interpreter.GlobalValues;


/**   displays HTML documents  in an editor pane. */
public class EditorPaneHTMLHelp extends JFrame   { 
   public static Font font;
    public JTextField magFactor;
    public static double magnificationFactor = GlobalValues.helpMagnificationFactor;
    
   

    public EditorPaneHTMLHelp(String htmlFile ) 
    {
        if (GlobalValues.useSystemBrowserForHelp)   {
      Desktop d = GlobalValues.desktop;
try {
    // create a temp file
    GlobalValues.forHTMLHelptempFile = new File("tempHTMLHelpSynthetic.html");
    FileWriter fw = new FileWriter(GlobalValues.forHTMLHelptempFile);
    
     java.util.List<String> list =  readTextFromJar(htmlFile);
    Iterator<String> it = list.iterator();
    while(it.hasNext()) {
      fw.write(it.next()+"\n");
    }

    String canonicalPathOfFile = GlobalValues.forHTMLHelptempFile.getCanonicalPath();
    URL urlFile = new URL("file://"+canonicalPathOfFile);
    URI uriOfFile = urlFile.toURI();
    
    fw.close();
    
    d.browse(uriOfFile);
    
} catch (Exception e) {
e.printStackTrace();
}
        
        }  else {
     URL initialURL  = getClass().getResource(htmlFile);
        
     font = GlobalValues.htmlfont;
        
      String title  = "HTML Help";
      setTitle(title);
    
      final Stack<String> urlStack = new Stack<String>();
      final JEditorPane editorPane;

      
       editorPane = new JEditorPane(new HTMLEditorKit().getContentType(), " ");

        editorPane.setOpaque(false);
        editorPane.setBorder(null);
        editorPane.setEditable(false);


        JPanel magPanel = new JPanel();
        
        magnificationFactor = GlobalValues.helpMagnificationFactor;
        magFactor = new JTextField(Double.toString(magnificationFactor));
      
      JButton  magButton = new JButton("Set Magnification: ");
      magButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
         magnificationFactor = Double.parseDouble(magFactor.getText());
         GlobalValues.helpMagnificationFactor = magnificationFactor;
            }
        });

      magPanel.setLayout(new GridLayout(1, 2));
      magPanel.add(magButton); 
      magPanel.add(magFactor);
      
      
      final JTextField url = new JTextField(initialURL.toString());
      
      // set up hyperlink listener
      editorPane.setEditable(false);
    
      
      try
      {  
                     // remember URL for back button
        urlStack.push(initialURL.toString());
                     // show URL in text field
        url.setText(initialURL.toString());
        
               // add a CSS rule to force body tags to use the default label font
        // instead of the value in javax.swing.text.html.default.csss
        
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize()*GlobalValues.helpMagnificationFactor+ "pt; }";
        ((HTMLDocument)editorPane.getDocument()).getStyleSheet().addRule(bodyRule);

        editorPane.setPage(initialURL);
        
        editorPane.firePropertyChange("dummyProp", true, false);

        }
        catch (IOException e)
        {  
       editorPane.setText("Exception: " + e);
       }
    
      editorPane.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
        
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize()*GlobalValues.helpMagnificationFactor+ "pt; }";
        ((HTMLDocument)editorPane.getDocument()).getStyleSheet().addRule(bodyRule);
   
            }
        });
      
      editorPane.addHyperlinkListener(new  HyperlinkListener()
         {  
            public void hyperlinkUpdate(HyperlinkEvent event)
            {  
               if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
               {  
                  try
                  {  
                     // remember URL for back button
                     urlStack.push(event.getURL().toString());
                     // show URL in text field
                     url.setText(event.getURL().toString());
                    editorPane.setPage(event.getURL());

            editorPane.firePropertyChange("dummyProp", true, false);

                  }
                  catch (IOException e)
                  {  
                     editorPane.setText("Exception: " + e);
                  }
               }
            }
         });

      // set up checkbox for toggling edit mode
      final JCheckBox editable = new JCheckBox();
      editable.addActionListener(new  ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {  
               editorPane.setEditable(editable.isSelected());
        
            }
         });

      // set up load button for loading URL
    ActionListener listener = new  ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {  
               try
               {  
                  // remember URL for back button
                  urlStack.push(url.getText());
                  editorPane.setPage(url.getText());
                    
    editorPane.firePropertyChange("dummyProp", true, false);

            }
               catch (IOException e)
               {  
                  editorPane.setText("Exception: " + e);
               }
            }
         };
   
      JButton loadButton = new JButton("Load/Magnify");
      loadButton.addActionListener(listener);
      url.addActionListener(listener);

      // set up back button and button action

      JButton backButton = new JButton("Back");
      backButton.addActionListener(new  ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {  
               if  (urlStack.size() <= 1)  return;
               try
               {  
                  // get URL from back button
                  urlStack.pop();
                  // show URL in text field
                  String urlString = urlStack.peek();
                  url.setText(urlString);
                  editorPane.setPage(urlString);
               
    editorPane.firePropertyChange("dummyProp", true, false);

                   }
               catch (IOException e)
               {  
                  editorPane.setText("Exception: " + e);
               }
            }
         });

      JPanel allPanel = new JPanel(new BorderLayout());
      
      // put all control components in a panel

      JPanel ctrlPanel = new JPanel(new BorderLayout());
      JPanel urlPanel = new JPanel();
      urlPanel.add(new JLabel("URL"));
      urlPanel.add(url);
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(loadButton);
      buttonPanel.add(backButton);
      buttonPanel.add(new JLabel("Editable"));
      buttonPanel.add(magPanel);
      buttonPanel.add(editable);
      ctrlPanel.add(buttonPanel, BorderLayout.NORTH);
      ctrlPanel.add(urlPanel, BorderLayout.CENTER);
      
      allPanel.add(ctrlPanel, BorderLayout.NORTH);
      allPanel.add(new JScrollPane(editorPane), BorderLayout.CENTER);

      add(allPanel);
        }
     }  
        
      public java.util.List<String> readTextFromJar(String s) {
    InputStream is = null;
    BufferedReader br = null;
    String line;
    ArrayList<String> list = new ArrayList<String>();

    try { 
      is = getClass().getResourceAsStream(s);
      br = new BufferedReader(new InputStreamReader(is));
      while (null != (line = br.readLine())) {
         list.add(line);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (br != null) br.close();
        if (is != null) is.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return list;
  }
      
    /**
   This frame contains an editor pane, a text field and button
   to enter a URL and load a document, and a Back button to 
   return to a previously loaded document.
*/
   public  EditorPaneHTMLHelp(String initialURL, String title)
   {  
       this(initialURL);
       setTitle(title);
   }
      
   

   
}
