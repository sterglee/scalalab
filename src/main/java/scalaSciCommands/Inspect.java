package scalaSciCommands;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.lang.reflect.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.tree.*;
import scalaExec.Interpreter.GlobalValues;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
// it is useful to visualize some properties of the completion list items, e.g.
// whether they are static
// For that purpose we implement a specialized font cell renderer
class FontCellRenderer extends JComponent implements ListCellRenderer {
            private String currentText;   // the current text of the completion item
            private Color background;  
            private Color foreground;
            private int currentIndex;
       

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            currentText = (String)value;  // used by paintComponent to draw the text
            background = isSelected ? list.getSelectionBackground() : list.getBackground();
            foreground = isSelected ? list.getSelectionForeground() :  list.getForeground();
            currentIndex = index;  // keep current index
            return this;
        }
        
        public void paintComponent(Graphics g) {
              // adjust font properly
            if (GlobalValues.isStaticMarks[currentIndex])
                GlobalValues.fontForCompletionListItem = GlobalValues.staticsFont;
            else
                GlobalValues.fontForCompletionListItem = GlobalValues.instancesFont;
            
            FontMetrics fm = g.getFontMetrics(GlobalValues.fontForCompletionListItem);
            g.setColor(background);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(foreground);
            g.setFont(GlobalValues.fontForCompletionListItem);
            g.drawString(currentText, 0, fm.getAscent());
                     }
        
        public Dimension getPreferredSize() {
            String text = GlobalValues.fontForCompletionListItem.getFamily();
            String sampleMethod = "plot(scalaSci.Vec x, scalaSci.Vec y, Color color, String name"; 
            Graphics g = getGraphics();
            FontMetrics fm = g.getFontMetrics(GlobalValues.fontForCompletionListItem);
            return new Dimension(fm.stringWidth(sampleMethod), fm.getHeight());
        }
            
    }


// class to implement code completion functionality using Java reflection
public class Inspect
{

static   int numCompletionItems = 100;  // initial size for the number of items retrieved for completion
static   Vector  scanMethodsFields = new Vector(numCompletionItems);
static   String []   listOfAllMethodsFields;

// it is used by the editors to detect the string after a typed "." (i.e. method cal)
static public String methodSubString="";


// returns the index of the first entry in the Vector scanMethodsFields
// that matches the prefix using a binary search
// entries in scanMethodsFields are assumed sorted
    private static  int firstIndexOfMatchedString(String prefix)
    {
        int up = 1;
        int low = 0;
        int ce;  // currently examined element
        int prefLen = prefix.length();
        int currentMethodCnt = scanMethodsFields.size();
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < currentMethodCnt)
            {
                String currentDescription = listOfAllMethodsFields[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m >= 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }


// returns the index of the last entry in the Vector scanMethodsFields
// that matches the prefix using a binary search
// entries in scanMethodsFields are assumed sorted
 private static int lastIndexOfMatchedString(String prefix, int startingPoint)
    {
     
        int up = 1;
        int low = startingPoint;
        int ce;  // curerntly examined element
        int prefLen = prefix.length();
        int currentMethodCnt = scanMethodsFields.size();
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < currentMethodCnt)
            {
                String currentDescription = listOfAllMethodsFields[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m > 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }

        // returns an array of Strings that start with prefix
  public static String[] getMatched(String prefix)
    {
          if (prefix.equals(""))  // all methods match to a null string
        {
            return listOfAllMethodsFields;
        }
        int i = firstIndexOfMatchedString(prefix);  // get first index
        int j = lastIndexOfMatchedString(prefix, i);
        String[] matches = new String[j-i];

        for (int k = 0; k < matches.length; k++)        {    // collect all matches
            matches[k] = listOfAllMethodsFields[i+k];
         }
        return matches;
    }
    
   // inspect information about the object using Java reflection 
   public static void inspect(Object object)
   {
      
         // print class name and superclass name (if != Object)
         Class cl = null;
         if (object instanceof Class)
             cl = (Class)object;
         else
             cl = object.getClass();
         String name = cl.getName();

         Class supercl = cl.getSuperclass();
         String modifiers = Modifier.toString(cl.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");
         System.out.print("class " + name);
         if (supercl != null && supercl != Object.class) System.out.print(" extends "
               + supercl.getName());

         System.out.print("\n{\n");
         printConstructors(cl);
         System.out.println();
         printMethods(cl);
         System.out.println();
         printFields(cl);
         System.out.println("}");
      }

   /* inspect the variable passed by its identifier, e.g.
    var   myVec= vrand(900)
    inspectg(myVec)
    */
        public static void inspectg(Object object)
   {
         // print class name and superclass name (if != Object)
         Class cl = null;
         if (object instanceof Class)
             cl = (Class)object;
         else
             cl = object.getClass();
         String name = cl.getName();
         
         inspectg(name);
        }
        
     
        // inspect the variable type graphically
        // it is required since when we select the variable myVec, we have available the variable as String
     public static void inspectg(String nameOfType)
   {
      
       DefaultMutableTreeNode root = new DefaultMutableTreeNode("Contents of class " +nameOfType);
   
         Class cl=null;
         Class supercl = null;
         String modifiers = "";
        try {
         cl = scalaExec.Interpreter.GlobalValues.globalInterpreter.classLoader().loadClass(nameOfType);
         supercl = cl.getSuperclass();
         modifiers = Modifier.toString(cl.getModifiers());
    } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inspect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                   
         String extendsClass = " ";
         if (supercl != null ) 
             extendsClass +=  " extends " + supercl.getName();
            
         DefaultMutableTreeNode clDescr = new DefaultMutableTreeNode(modifiers + "  class "+nameOfType+" extends "+extendsClass);
         root.add(clDescr);
        
  DefaultMutableTreeNode nConstructors  = new DefaultMutableTreeNode("Constructors");
  DefaultMutableTreeNode nMethods  = new DefaultMutableTreeNode("Methods");
  DefaultMutableTreeNode nFields  = new DefaultMutableTreeNode("Fields");

  java.lang.reflect.Constructor [] classConstructors = cl.getConstructors();
 for (int k=0; k<classConstructors.length; k++) {
      Constructor  currentConstructor = classConstructors[k];
       DefaultMutableTreeNode  constructorNode = new DefaultMutableTreeNode(classConstructors[k].toString());
       nConstructors.add(constructorNode);
     }
     
    java.lang.reflect.Method []  classMethods = cl.getMethods();
for (int k=0; k<classMethods.length; k++) {
      Method currentMethod = classMethods[k];
      int  methodModifier = currentMethod.getModifiers();
      if (Modifier.isPublic(methodModifier) || Modifier.isStatic(methodModifier)) {
          DefaultMutableTreeNode  methodNode = new DefaultMutableTreeNode(classMethods[k].toString());
          nMethods.add(methodNode);
     }
   }

    java.lang.reflect.Field [] classFields  = cl.getDeclaredFields();
    for (int k=0; k<classFields.length; k++) {
      Field  current = classFields[k];
      DefaultMutableTreeNode  fieldNode = new DefaultMutableTreeNode(current.toString());
      nFields.add(fieldNode);
     }

   root.add(nConstructors);
   root.add(nMethods);
   root.add(nFields);

    JTree  inspectTree = new JTree(root);
    JScrollPane  inspectPane = new JScrollPane(inspectTree);
    JFrame inspectFrame = new JFrame("Inspecting Type:  "+nameOfType);
    inspectFrame.add(inspectPane);
    inspectFrame.setSize(300, 300);
    inspectFrame.setVisible(true);
        
        
   }
     
        public static void inspectCompletionList(Object object)
   {
      
         Class cl = null;
         if (object instanceof Class)
             cl = (Class)object;
         else
             cl = object.getClass();
         String name = cl.getName();
         
         inspectCompletionList(name);
        }
        
         
        
       //  inspect the String nameOfType,
       //  constructing a completion list using Java reflection
       // For example, given the command: 
       //    var x = new org.apache.commons.math3.distribution.NormalDistribution
       //  Then the call inspectCompletionList("x") interrogates the contents of the x object,
       //   inspectCompletionList("x.sa") all fields/methods of x starting with sa 
     public static void inspectCompletionList(String nameOfType)
   {
     DefaultListModel dcl = new DefaultListModel();  // the model for the completion list
     final JList clList = new JList(dcl);   // the completion's list
    
     GlobalValues.nameOfType = nameOfType;  // keep class name in order to construct fully qualified names for accessing static members       
     // register our specialized list cell renderer that displays static members in bold
    // clList.setCellRenderer(new FontCellRenderer());
     
     scanMethodsFields = new Vector(numCompletionItems);
     
         Class cl=null;
         try {
         
             cl = scalaExec.Interpreter.GlobalValues.globalInterpreter.classLoader().loadClass(nameOfType);
         
         
    } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inspect.class.getName()).log(Level.SEVERE, null, ex);
        }
     
         // retrieve the methods of the class using Java reflection
    java.lang.reflect.Method []  classMethods = cl.getMethods();  
    
for (int k=0; k<classMethods.length; k++) {      // all class methods
      Method currentMethod = classMethods[k];
      int  methodModifier = currentMethod.getModifiers();
      String methodName = currentMethod.getName();
      String longDescr = currentMethod.toString();
      String nameAndParamOnly = longDescr.substring(longDescr.indexOf(methodName), longDescr.length());
         // add only public methods
          if (Modifier.isPublic(methodModifier) ) {
            // mark static vs instance methods
          if (Modifier.isStatic(methodModifier)) 
              nameAndParamOnly += GlobalValues.staticsMarker;
          else nameAndParamOnly += GlobalValues.nonStaticsMarker;
        
           scanMethodsFields.add(nameAndParamOnly);
              
     }  // isPublic
      
   }  // all class methods

        // retrieve the fields of the class using Java reflection
    java.lang.reflect.Field [] classFields  = cl.getFields();
    for (int k=0; k<classFields.length; k++) {
      Field  current = classFields[k];
      int fmodifiers = current.getModifiers();
      // add only public fields
      if (Modifier.isPublic(fmodifiers)) {
          String fieldName = current.getName();
           // mark static vs instance fields
            if (Modifier.isStatic(fmodifiers)) 
              fieldName += GlobalValues.staticsMarker;
          else fieldName += GlobalValues.nonStaticsMarker;
      
          scanMethodsFields.add(fieldName);
             
         }  // isPublic
        
      }   // all class fields
      
            // sort the vector of all the methods and fields
            // it is required since at the completion process we perform
            // binary search to detect all items beginning with a substring
       Collections.sort(scanMethodsFields, new Comparator()  {
             public int compare(Object v1, Object v2) {
                 return ((String)v1).compareToIgnoreCase(((String)v2));
             }
        });
        
        // construct a view of the sorted Vector as an array of Strings
        int countMethods = scanMethodsFields.size();
        listOfAllMethodsFields = new String[countMethods];
        for (int k=0; k<countMethods; k++)
            listOfAllMethodsFields[k] = (String) scanMethodsFields.elementAt(k);

        //  filter the results if a substring after the dot is specified
        String [] filteredWithStringPattern =  listOfAllMethodsFields;   // initially all the methods
        //  fix the array of Strings that start with methodSubString
        if (scalaSciCommands.Inspect.methodSubString!=null)
               filteredWithStringPattern =   getMatched(scalaSciCommands.Inspect.methodSubString);
       
        StringBuilder  sb = new StringBuilder();
        // keep whether the method/field  is static or instance
        GlobalValues.isStaticMarks = new boolean[filteredWithStringPattern.length];
        
         // for all filtered completion items add to the list's data model adjusting properly the font
        for (int k=0; k<filteredWithStringPattern.length; k++) { 
            String currentElement = filteredWithStringPattern[k];
            if (currentElement.endsWith(GlobalValues.nonStaticsMarker)) 
                    GlobalValues.isStaticMarks[k] = false;
            else
                    GlobalValues.isStaticMarks[k] = true;
     
            // remove markers (markers they have the same length)
            currentElement = currentElement.substring(0, currentElement.length()-GlobalValues.staticsMarker.length());

            dcl.addElement(currentElement);
        }  // for all filtered completion items add to the list's data model adjusting properly the font
        
        JScrollPane  completionPane = new JScrollPane(clList);
    
    GlobalValues.completionFrame  = new JFrame("Completion List for "+nameOfType +". Boldfaced are the static members");
    Point p = null;
    if (scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane == true)  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
        p = scalaExec.Interpreter.GlobalValues.editorPane.getCaret().getMagicCaretPosition();
     else    
      p = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.getCaret().getMagicCaretPosition();
    
    if (p != null) {
    if (scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane == true)  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
          SwingUtilities.convertPointToScreen(p, scalaExec.Interpreter.GlobalValues.editorPane);
    else
          SwingUtilities.convertPointToScreen(p, scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane);
    
            p.x = p.x + 2;
            p.y = p.y + 20;
            GlobalValues.completionFrame.setLocation(p.x, p.y);

     }        
    
     GlobalValues.completionFrame.add(completionPane);
    int numOfComponentsForCompletion =  dcl.getSize();
    if (numOfComponentsForCompletion > GlobalValues.maxItemsToDisplayAtCompletions)
        numOfComponentsForCompletion = GlobalValues.maxItemsToDisplayAtCompletions;
   if (numOfComponentsForCompletion < 5) // some minimum size
        numOfComponentsForCompletion = 5;
   
    
    int approxPixelsPerItem = 20;    // approx. how many pixels to take vertically for a list item
                                                           // in order to approximate the vertical size of the completion list
    GlobalValues.completionFrame.setSize(600,  numOfComponentsForCompletion*approxPixelsPerItem);
    GlobalValues.completionFrame.setVisible(true);
   
    clList.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    processListSelection(clList);
             }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
   
     // add a KeyListener to the JList in order to destroy itself with either ESC or ENTER
clList.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
              int keyValue = e.getKeyCode();
        switch (keyValue) {
                      
            case   KeyEvent.VK_ENTER:
                processListSelection(clList);
  
                break;
                
            case   KeyEvent.VK_ESCAPE:      // disposes the completion's list frame
        GlobalValues.completionFrame.dispose();
                break;
            default: 
                break;
              }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    
        
   }


    public static void displayCompletionList(String nameOfType, List<String> elems)
    {
        DefaultListModel dcl = new DefaultListModel();  // the model for the completion list

        GlobalValues.nameOfType = nameOfType;  // keep class name in order to construct fully qualified names for accessing static members

        for (String x: elems)
            dcl.addElement(x);

        final JList clList = new JList(dcl);   // the completion's list

        // register our specialized list cell renderer that displays static members in bold
     //   clList.setCellRenderer(new FontCellRenderer());


        clList.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    processListSelection(clList);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // add a KeyListener to the JList in order to destroy itself with either ESC or ENTER
        clList.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyValue = e.getKeyCode();
                switch (keyValue) {

                    case   KeyEvent.VK_ENTER:
                        processListSelection(clList);

                        break;

                    case   KeyEvent.VK_ESCAPE:      // disposes the completion's list frame
                        GlobalValues.completionFrame.dispose();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        JScrollPane  completionPane = new JScrollPane(clList);

        GlobalValues.completionFrame  = new JFrame("Completion List for "+nameOfType);
        Point p = null;
        if (scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane == true)  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
            p = scalaExec.Interpreter.GlobalValues.editorPane.getCaret().getMagicCaretPosition();
        else
            p = scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.getCaret().getMagicCaretPosition();

        if (p != null) {
            if (scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane == true)  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
                SwingUtilities.convertPointToScreen(p, scalaExec.Interpreter.GlobalValues.editorPane);
            else
                SwingUtilities.convertPointToScreen(p, scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane);

            p.x = p.x + 2;
            p.y = p.y + 20;
            GlobalValues.completionFrame.setLocation(p.x, p.y);

        }

        GlobalValues.completionFrame.add(completionPane);
        int numOfComponentsForCompletion =  dcl.getSize();
        if (numOfComponentsForCompletion > GlobalValues.maxItemsToDisplayAtCompletions)
            numOfComponentsForCompletion = GlobalValues.maxItemsToDisplayAtCompletions;
        if (numOfComponentsForCompletion < 5) // some minimum size
            numOfComponentsForCompletion = 5;


        int approxPixelsPerItem = 20;    // approx. how many pixels to take vertically for a list item
        // in order to approximate the vertical size of the completion list
        GlobalValues.completionFrame.setSize(600,  numOfComponentsForCompletion*approxPixelsPerItem);
        GlobalValues.completionFrame.setVisible(true);



    }
   
        private static void  processListSelection(JList clList) {
                 String selected = (String) clList.getSelectedValue();
/*
                  int leftParenthesisIndex = selected.indexOf('(');
                if (leftParenthesisIndex != -1) 
                    selected = selected.substring(0, leftParenthesisIndex+1);
                    
                int selectedIndex = clList.getSelectedIndex();  // index of selected value
                  // a static method in Scala needs to be accessed with its fully qualified name
                if (GlobalValues.isStaticMarks[selectedIndex])  {
                    selected = GlobalValues.nameOfType+ "."+selected;
                  // update selection start to remove also the idenfier 
                    GlobalValues.selectionStart = GlobalValues.selectionBeginning;
                    
                }
              else
                 if (GlobalValues.methodNameSpecified==false)
                    selected = "." + selected;  // append a dot
*/
                
          if (scalaExec.Interpreter.GlobalValues.completionIsForSyntaxPane == true) {  // completion is for JSyntaxPane editor (in order to display the completion popup properly)
                scalaExec.Interpreter.GlobalValues.editorPane.setSelectionStart(GlobalValues.selectionStart);
                scalaExec.Interpreter.GlobalValues.editorPane.setSelectionEnd(GlobalValues.selectionEnd);
                scalaExec.Interpreter.GlobalValues.editorPane.replaceSelection( selected);
                GlobalValues.selectionEnd = GlobalValues.selectionStart+selected.length();  // update selection end for cascaded static member choice
                }
                else {  
          scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.setSelectionStart(GlobalValues.selectionStart);
          scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.setSelectionEnd(GlobalValues.selectionEnd);
          scalaExec.Interpreter.GlobalValues.globalRSyntaxEditorPane.replaceSelection( selected);
          GlobalValues.selectionEnd = GlobalValues.selectionStart+selected.length();  // update selection end for cascaded static member choice
                
                }
    }
     
   /**
    * Prints all constructors of a class
    * @param cl a class
    */
   public static void printConstructors(Class cl)
   {
      Constructor[] constructors = cl.getDeclaredConstructors();

      for (Constructor c : constructors)
      {
         String name = c.getName();
         System.out.print("   ");
         String modifiers = Modifier.toString(c.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.print(name + "(");

         // print parameter types
         Class[] paramTypes = c.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {
            if (j > 0) System.out.print(", ");
            System.out.print(paramTypes[j].getName());
         }
         System.out.println(");");
      }
   }

   /**
    * Prints all methods of a class
    * @param cl a class
    */
   public static void printMethods(Class cl)
   {
      Method[] methods = cl.getDeclaredMethods();

      for (Method m : methods)
      {
         Class retType = m.getReturnType();
         String name = m.getName();

         System.out.print("   ");
         // print modifiers, return type and method name
         String modifiers = Modifier.toString(m.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.print(retType.getName() + " " + name + "(");

         // print parameter types
         Class[] paramTypes = m.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {
            if (j > 0) System.out.print(", ");
            System.out.print(paramTypes[j].getName());
         }
         System.out.println(");");
      }
   }

   /**
    * Prints all fields of a class
    * @param cl a class
    */
   public static void printFields(Class cl)
   {
      Field[] fields = cl.getDeclaredFields();

      for (Field f : fields)
      {
         Class type = f.getType();
         String name = f.getName();
         System.out.print("   ");
         String modifiers = Modifier.toString(f.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.println(type.getName() + " " + name + ";");
      }
   }
}

