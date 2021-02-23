package scalaExec.gui;

import java.lang.reflect.Method;
import java.net.URL;
import scalaExec.Interpreter.GlobalValues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ConsoleKeyHandler implements KeyListener 
{
       private Console  inputConsole;   // keeps the inputCons Console object
       static public String  text;
       static public String prevtext;
       private boolean firstUpArrow = false;
       private int lastNewLineCommandStartIdx;
       private  char endCommandChar = ';';   // character denoting the end of the previous command
       private  int previousSemicolonCommandStartIdx;
       private  int  tokIndex;  // index of token (in order to match the text after it)
       private int posAutoCompletion;
       private String [] matches;
       private JList  topLevelResultsList;
       private autoCompleteListHandler detailHelpAdapter;
       private final int tabDotIndex = -1;  // fix the index of '.' at the TAB completion request
       private final String frozenInputConsoleContents="";
       private URL watchURL;
       
	public ConsoleKeyHandler()
	{
            
	}

        
         
    public void keyTyped(KeyEvent e){
        
   }

	/**Interpret key pressed */
    public void keyPressed(KeyEvent e)
    {
  Console inputCons = ((Console)e.getSource());  // get the source Object of the event
  
        GlobalValues.userConsole = inputCons;
    	int keyValue = e.getKeyCode();
        scala.collection.immutable.List<String> matchesAll;
        
        switch (keyValue) {

            case  KeyEvent.VK_ALT:  e.consume(); break;
            
            case   KeyEvent.VK_UP:			//up cursor
			e.consume();
                        if (GlobalValues.commandLineModeOn)  
                            if (firstUpArrow) {
                                  ((scalalabConsole)inputCons).mostRecentCommand();
                                firstUpArrow = false;
                            }
                            else
                            ((scalalabConsole)inputCons).prevCommand();
                        break;
                        
                         
            case  KeyEvent.VK_DOWN:		//down cursor
	    		e.consume();
        // display next command only when in line mode                        
                        if (GlobalValues.commandLineModeOn)  
            		  ((scalalabConsole)inputCons).nextCommand();
                        break;
                        
            case   KeyEvent.VK_LEFT:       //left cursor
			//check the cursor isn't moving off the current line
                        inputCons.checkPosition();
                        break;

            case KeyEvent.VK_BACK_SPACE:
            //check the cursor isn't moving off the current line
                        inputCons.checkPosition();
                        break;
            

            case    KeyEvent.VK_ENTER:  // at commandLineMode, each newline causes the execution of the current line
                firstUpArrow = true;   // allow for the previous command whether correcr or not
                if (scalaExec.Interpreter.GlobalValues.commandLineModeOn==true)  
                     scalaExec.Interpreter.GlobalValues.userConsole.textArea = "";  // reset the text from the current command
                        break; 

 case KeyEvent.VK_TAB:   // use Scala autocompletion to complete the user expression
/* SOS-Sterg
                    inputConsole = (Console)e.getSource();
                    e.consume();
                        //get the text on the current line
                    text = inputConsole.getText();
                        // get the starting point of the current command start                    
                    lastNewLineCommandStartIdx = text.lastIndexOf(GlobalValues.scalalabPromptChar)+1;
                
             // check for multiple commands per line       
                    endCommandChar = ';';   // character denoting the end of the previous command
                    previousSemicolonCommandStartIdx = text.lastIndexOf(endCommandChar)+1;
                    if (previousSemicolonCommandStartIdx > lastNewLineCommandStartIdx)  {  // multiple commands per line separated by ';''
                        lastNewLineCommandStartIdx = previousSemicolonCommandStartIdx;
                     }
                    posAutoCompletion = lastNewLineCommandStartIdx;
                    String toksToCheckForLastIndex = "= +*-";
                    int toksCnt = toksToCheckForLastIndex.length();
                    for (int k=0; k<toksCnt; k++)  {
                    tokIndex = text.lastIndexOf(toksToCheckForLastIndex.charAt(k))+1;  // index of token  (in order to match the text after it)
                    if (tokIndex > lastNewLineCommandStartIdx)  {
                        posAutoCompletion = tokIndex;
                        lastNewLineCommandStartIdx = tokIndex;
                      }
                    }
                   
                     text = text.substring(posAutoCompletion, text.length()).trim();
                     
                     // check for '.' that marks that the user requests to know the members of a structure
                     String iString = text; //= text.substring(startPos, text.length());
                     scala.Option<Class<?>> classOfItem;
                     String  afterDot="";
                     boolean membersRequested = false;  // when the user presses dot, we imply that the completion should provide the members of a structure
       
                     int dotIndex = text.indexOf('.');
                     if (dotIndex != -1)    {    // members of a structure should be provided
                         membersRequested = true;
                         frozenInputConsoleContents = inputConsole.getText();
                         tabDotIndex = frozenInputConsoleContents.lastIndexOf('.');
                         frozenInputConsoleContents = frozenInputConsoleContents.substring(0, tabDotIndex+1);
                         afterDot = iString.substring(dotIndex+1, iString.length());    // get member typed after dot
                         afterDot = afterDot.trim();   // strip any blanks
                         iString = iString.substring(0, dotIndex);  // remove the dot
                     }
                     boolean afterDotCharsExist = false;
                     if (afterDot.length()>0)
                         afterDotCharsExist = true;

// create a completion object                    
                 scala.tools.nsc.interpreter.JLineCompletion   completion = new  scala.tools.nsc.interpreter.JLineCompletion(scalaExec.Interpreter.GlobalValues.globalInterpreter);
                    
                    if (membersRequested == false)   {   // provide the top level elements
               Parsed ps = Parsed.apply(iString);
               scala.collection.immutable.List<String> scalaResultsList = completion.topLevelFor(ps);
               int matchedSize = scalaResultsList.size();
               if (matchedSize > 0) {  // top level elements exist, edit them
                     String []    dmatches =  new String[matchedSize];
                     Iterator  complResults = scalaResultsList.iterator();
                     int k=0;
                     while (complResults.hasNext())  {   // collect the matched results
                         String currentCompletion = (String)complResults.next();
                         dmatches[k++] = currentCompletion;
                  }
                    
                topLevelResultsList = new JList(dmatches);
                topLevelResultsList.addListSelectionListener(new ListSelectionListener() {
                     public void valueChanged(ListSelectionEvent lse) {
                             
                             String  selValue = topLevelResultsList.getSelectedValue().toString();
                             
                             String currentText = inputConsole.getText();
                             int    posPreviousText  =  currentText.lastIndexOf(GlobalValues.scalalabPromptChar) + 2;
                             int    posEndPreviousCommand = currentText.lastIndexOf(endCommandChar)+1;
                             if  (posEndPreviousCommand > posPreviousText)
                                 posPreviousText = posEndPreviousCommand;
                             //int lastEqualsIdx = currentText.lastIndexOf('=')+1;
                             if (posAutoCompletion > posPreviousText)
                                 posPreviousText = posAutoCompletion;
                             inputConsole.setText(currentText.substring(0,posPreviousText)+selValue);
                             GlobalValues.selectedStringForAutoCompletion = selValue;
                             inputConsole.setCaretPosition(inputConsole.getText().length());   	// set cursor at the end of the text area
                             
	                 }
                     }
         );
        
           GlobalValues.autoCompletionFrame = new AutoCompletionFrame("scalalabConsole AutoCompletion, Workspace variables");
           GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList);
                     }     //  top level elements exist, edit them
                    }   //  // provide the top level elements
                   
                    else   {   // a dot pressed which implies that the completion should provide members of a structure
                        
                    //  see if we have methods available
                    scala.collection.immutable.List<String> scalaComplResultsList = completion.completions(iString);

                   classOfItem =  scalaExec.Interpreter.GlobalValues.globalInterpreter.classOfTerm(iString);
                    
                        if (classOfItem.toString().equalsIgnoreCase("None") == false )  {
                    // completion.
                    Iterator  ccomplResults = scalaComplResultsList.iterator();
                    int membersSize = ccomplResults.size();
                    if (membersSize > 0)  {   // member elements exist, edit them 
                        Vector  memberMatches = new Vector();
                        Iterator  complResults = scalaComplResultsList.iterator();
                        while (complResults.hasNext())  {   // collect the matched results
                            String currentCompletion = (String) complResults.next();
                            currentCompletion = currentCompletion.trim();
                            if (afterDotCharsExist)  {   // user typed after "."
                             if (currentCompletion.startsWith(afterDot))  {
                                String [] classMatchedMethods =  getMethodsOfClass(classOfItem, currentCompletion);
                                int classMatchedMethodsLen = classMatchedMethods.length;
                                
                                for (int matchedCnt=0; matchedCnt<classMatchedMethodsLen; matchedCnt++)
                                  memberMatches.add( (String)classMatchedMethods[matchedCnt]);
                                }
                            }
                            else  {
                                String [] classMatchedMethods =  getMethodsOfClass(classOfItem, currentCompletion);
                                int classMatchedMethodsLen = classMatchedMethods.length;
                                
                                for (int matchedCnt=0; matchedCnt<classMatchedMethodsLen; matchedCnt++)
                                  memberMatches.add((String) classMatchedMethods[matchedCnt]);
                                
          //                      if (classMatchedMethodsLen==0)   // a special Scala method, e.g. "+"
            //                        memberMatches.add(currentCompletion);
                            }
                          }   // collect the matched results
                        
                    membersResultList = new JList(memberMatches);
                    membersResultList.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                          Object selValueObj = membersResultList.getSelectedValue();
                          String  selValue = "";
                         if (selValueObj != null)  {
                          selValue = (String)selValueObj;
                          inputConsole.setText(frozenInputConsoleContents+selValue);
                          GlobalValues.selectedStringForAutoCompletion = selValue;
                          inputConsole.setCaretPosition(inputConsole.getText().length());   	// set cursor at the end of the text area  
                          }
                          String currentText = inputConsole.getText();
                          int    posPreviousText  =  currentText.lastIndexOf(GlobalValues.scalalabPromptChar) + 2;
                          int    posEndPreviousCommand = currentText.lastIndexOf(endCommandChar)+1;
                          if  (posEndPreviousCommand > posPreviousText)
                                 posPreviousText = posEndPreviousCommand;
                          int lastEqualsIdx = currentText.lastIndexOf('.')+1;
                          if (lastEqualsIdx > posPreviousText)
                                 posPreviousText = lastEqualsIdx;
                          inputConsole.setText(currentText.substring(0,posPreviousText)+" "+selValue);
                          GlobalValues.selectedStringForAutoCompletion = selValue;
                          inputConsole.setCaretPosition(inputConsole.getText().length());   	// set cursor at the end of the text area  
         
	                 }   // 
                    });
                 
                     
             GlobalValues.autoCompletionFrame = new AutoCompletionFrame(" Members of "+iString);
             GlobalValues.autoCompletionFrame.displayMatches(membersResultList);
                     };   // a dot pressed which implies that the completion should provide members of a structure
             
                   }   // member elements exist, edit them 
                    }

     
      */
     break;
                
                  
    
            case KeyEvent.VK_F4:  // switches executing each line with the newline press or with buffered inputCons 
                scalaExec.Interpreter.GlobalValues.commandLineModeOn = !scalaExec.Interpreter.GlobalValues.commandLineModeOn;
                updateModeStatusInfo();  // update the information about scalaLab's console inputCons mode 
                break;
                
                
 
            // triggers autocompletion for the manually added items 
            case KeyEvent.VK_F1:
            case KeyEvent.VK_F3:

                   if (GlobalValues.autocompletionEnabled == false)   {  // enable autocompletion on the first F1 press
                       GlobalValues.autocompletionEnabled = true;
                       scalaExec.scalaLab.scalaLab.initAutocompletion();
                   }
                    inputConsole = (Console)e.getSource();
                    e.consume();
                  		//get the text on the current line
                     text = scalaExec.Interpreter.GlobalValues.userConsole.getText();
                                // get the starting point of the current command start                    
                    lastNewLineCommandStartIdx = text.lastIndexOf(GlobalValues.scalalabPromptChar)+1;
                
                    endCommandChar = ';';   // character denoting the end of the previous command
                    previousSemicolonCommandStartIdx = text.lastIndexOf(endCommandChar)+1;
                    if (previousSemicolonCommandStartIdx > lastNewLineCommandStartIdx)  {  // multiple commands per line separated by ';''
                        lastNewLineCommandStartIdx = previousSemicolonCommandStartIdx;
                     }
                    int eqIndex = text.lastIndexOf('=')+1;  // index of equality  (in order to match the text after it
                    posAutoCompletion = lastNewLineCommandStartIdx;
                    if (eqIndex > lastNewLineCommandStartIdx) 
                        posAutoCompletion = eqIndex;
                   
                     text = text.substring(posAutoCompletion, text.length());
                    String  inputString = text; //= text.substring(startPos, text.length());
                    inputString = inputString.trim();

                    matches = null;
                    if (keyValue==KeyEvent.VK_F1) {
                       matches = scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.getMatched(inputString);
                    }
                    else {
                       matches = scalaExec.Interpreter.GlobalValues.autoCompletionScalaSci.getMatchedRegEx(inputString);
                    }
                        
                      topLevelResultsList = new JList(matches);
                     detailHelpAdapter = new autoCompleteListHandler();
                     topLevelResultsList.addKeyListener(detailHelpAdapter); 
                         
                     topLevelResultsList.addListSelectionListener(new ListSelectionListener() {
                         public void valueChanged(ListSelectionEvent lse) {
                             
                             String  selValue = topLevelResultsList.getSelectedValue().toString();
                             selValue = selValue.substring(selValue.indexOf(GlobalValues.smallNameFullPackageSeparator)+3, selValue.length());
                               
                             GlobalValues.selectedStringForAutoCompletion = selValue;
                             
	                 }
                     }
         );
         
                    
                GlobalValues.autoCompletionFrame = new AutoCompletionFrame("scalalabConsole AutoCompletion ( F1 for detailed help on the selected entry)");
                
            GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList);
            break;
                
            case KeyEvent.VK_ESCAPE:
                int i,j, lc;
            // Erase the current editing line
                e.consume();
                        if (GlobalValues.commandLineModeOn)  
            		  ((scalalabConsole)inputCons).clearCommandLine();
                        break;

                
           case KeyEvent.VK_F5:
               GlobalValues.consoleOutputWindow.output.setText("");
               break;
           
           case KeyEvent.VK_F10:
                 inputConsole = (Console)e.getSource();
                 e.consume();
                  	// get the text on the current line
                 text = inputConsole.getSelectedText();
                 if (text != null) {
          String  inspectCommand = "scalaSciCommands.BasicCommands.inspectg("+text+")  ";
          GlobalValues.globalInterpreter.interpret(inspectCommand);
                 }
          break;
                       
           case KeyEvent.VK_F6:
                    scalaExec.Interpreter.GlobalValues.autoCompletionWorkspace = new scalaExec.gui.AutoCompletionWorkspace();
                    inputConsole = (Console)e.getSource();
                    e.consume();
                  	// get the text on the current line
                    text = inputConsole.getText();
                        // get the starting point of the current command start                    
                    lastNewLineCommandStartIdx = text.lastIndexOf(GlobalValues.scalalabPromptChar)+1;
                
                    endCommandChar = ';';   // character denoting the end of the previous command
                    previousSemicolonCommandStartIdx = text.lastIndexOf(endCommandChar)+1;
                    if (previousSemicolonCommandStartIdx > lastNewLineCommandStartIdx)  {  // multiple commands per line separated by ';''
                        lastNewLineCommandStartIdx = previousSemicolonCommandStartIdx;
                     }
                    eqIndex = text.lastIndexOf('=')+1;  // index of equality  (in order to match the text after it
                    posAutoCompletion = lastNewLineCommandStartIdx;
                    if (eqIndex > lastNewLineCommandStartIdx) 
                        posAutoCompletion = eqIndex;
                   
                     text = text.substring(posAutoCompletion, text.length());
                    String iString = text; //= text.substring(startPos, text.length());
                    iString = iString.trim();

                     matches = null;
                     matches = scalaExec.Interpreter.GlobalValues.autoCompletionWorkspace.getMatched(iString);
                     topLevelResultsList = new JList(matches);
                     topLevelResultsList.addListSelectionListener(new ListSelectionListener() {
                     public void valueChanged(ListSelectionEvent lse) {
                             
                             String  selValue = topLevelResultsList.getSelectedValue().toString();
                             
                             String currentText = inputConsole.getText();
                             int    posPreviousText  =  currentText.lastIndexOf(GlobalValues.scalalabPromptChar) + 2;
                             int    posEndPreviousCommand = currentText.lastIndexOf(endCommandChar)+1;
                             if  (posEndPreviousCommand > posPreviousText)
                                 posPreviousText = posEndPreviousCommand;
                             int lastEqualsIdx = currentText.lastIndexOf('=')+1;
                             if (lastEqualsIdx > posPreviousText)
                                 posPreviousText = lastEqualsIdx;
                             inputConsole.setText(currentText.substring(0,posPreviousText)+" "+selValue);
                             GlobalValues.selectedStringForAutoCompletion = selValue;
                             inputConsole.setCaretPosition(inputConsole.getText().length());   	// set cursor at the end of the text area
	                 }
                     }
         );
        
         GlobalValues.autoCompletionFrame = new AutoCompletionFrame("Top-Level Workspace variables");
         GlobalValues.autoCompletionFrame.displayMatches(topLevelResultsList);
          
            
               
            default:
                break;
                    
        }
    }

    public static String [] getMethodsOfClass(scala.Option<Class<?>>  theClass, String nameOfMethod)  {
        Method [] methodsOfClass = theClass.get().getMethods();   // gets the methods of the class
        // collect all the methods named "nameOfMethod"
        int methodCnt = methodsOfClass.length;
        int matchedMethodCnt=0;
        for (int k=0; k<methodCnt; k++)   {   // for all methods find matched ones
            String methodName = methodsOfClass[k].getName();
      //      System.out.println("methodName = "+methodName);
            if (methodName.equals(nameOfMethod))
                matchedMethodCnt++;
        }  // for all methods find matched ones
        String [] matchedMethodsStringArray;
        if (matchedMethodCnt==0)  {   // no matches with Java reflection since Scala special method
           matchedMethodsStringArray = new String[1];
           String  methodString = constructSignature(methodsOfClass[0]);
           matchedMethodsStringArray[0] = nameOfMethod;
        }
        else             {
        matchedMethodsStringArray = new String[matchedMethodCnt];
        int matchedCnt=0;
        for (int k=0; k<methodCnt; k++) {
             String methodName = methodsOfClass[k].getName();
             Method currentMethod = methodsOfClass[k];
             if (methodName.equals(nameOfMethod))  {  // construct the signature of the matched method
                   String methodString = constructSignature(currentMethod);
                   matchedMethodsStringArray[matchedCnt++] = methodString;
            }
         }
        }
        
        return matchedMethodsStringArray;
    }

public static String constructSignature(Method method) {
    String methodStr=method.getName()+"(";
     Class<?> [] methodTypes = method.getParameterTypes();
     int numOfParams = methodTypes.length;
     for (int k=0; k<numOfParams-1; k++) {
         methodStr += methodTypes[k].getName()+",";
     }
     if (numOfParams > 0)
         methodStr += methodTypes[numOfParams-1].getName();
     methodStr += " ) ";
     Class retType = method.getReturnType();
     methodStr += ": "+retType.getName();
     return methodStr;
        
    
}    

public static String scriptModeText() {
    String scriptingMode = "scalaSci";
        return scriptingMode;
}    

// updates information about scalaLab's console interaction mode
  public static void updateModeStatusInfo() {
      
      
      String  bufferedLineMode = ",  Line Input     ";
      if (GlobalValues.commandLineModeOn == false)
          bufferedLineMode = ",  Buffered Input ";
                
         if (GlobalValues.editingFileInPane != null)
      GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle()+ ", editing file: "+GlobalValues.editingFileInPane);
         else
      GlobalValues.scalalabMainFrame.setTitle(scalaExec.Interpreter.GlobalValues.buildTitle());
         
         scalaExec.Interpreter.GlobalValues.userConsole.setBackground(GlobalValues.ColorScalaSci);
    }
  
      
    void display_help() {
        JFrame helpFrame = new AutoCompletionFrame("Scala-Lab  help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(400, 400);
        Container container = helpFrame.getContentPane();
        JTextArea helpText = new JTextArea();

        int classCnt = 0;
        JScrollPane  helpPane = new JScrollPane(helpText);
        
        container.add(helpPane);
        helpFrame.setVisible(true);  
      }

    
    // displays detailed help for the selected item
    public static void display_detailed_help(String selectedItem) {
GlobalValues.detailHelpStringSelected = selectedItem;
DetailHelpFrame detailFrame = new DetailHelpFrame();
detailFrame.setVisible(true);
    }
    
        
    
    public void keyReleased(KeyEvent e)
    {
    	Console inputCons = ((Console)e.getSource());
    	int keyValue = e.getKeyCode();
        
		if (keyValue == KeyEvent.VK_ENTER) {  
                    if (GlobalValues.autoCompletionVariableSelected)    
                        GlobalValues.autoCompletionVariableSelected = false; 
                    else {
                       String text = scalaExec.Interpreter.GlobalValues.scalalabMainFrame.scalalabConsole.getText();
                       String inputString = text.substring(text.lastIndexOf(GlobalValues.scalalabPromptString) + 2, text.length());
                        inputString = inputString.trim();
                        prevtext = inputString;
                
                    if (GlobalValues.commandLineModeOn == true)  {
                      ((scalalabConsole)inputCons).interpretLine();

                    scalaExec.Interpreter.GlobalValues.scalalabMainFrame.scalalabConsole.displayPrompt();
                    String  mem =  GlobalValues.availMemFormat.format( (double)GlobalValues.rt.freeMemory()/1000000);
                    String dispStr = "Available memory : "+mem+"MB. ";
                    GlobalValues.availMemLabel.setText(dispStr);
                      }
                    }
                    GlobalValues.scalalabMainFrame.scalalabConsole.requestFocus();
                }
                else if(keyValue == KeyEvent.VK_HOME)
			inputCons.home();
		else if(keyValue == KeyEvent.VK_UP || keyValue == KeyEvent.VK_DOWN)
			inputCons.end();
                
    }	
    
 
    

}



