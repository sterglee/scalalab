
package scalaExec.scalaLab;

import scalaExec.Interpreter.GlobalValues;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import javax.help.*;
import javax.swing.*;

/**  a Context Sensitive Help Object */
public class  CSHObject  {

    /**  the HelpSet & Broker of this CSH Object */
   public  HelpSet     theHS = null;
   public  HelpBroker    theHB;

   
/**  Create the  HelpBroker
 *  @param helpSetName:  the .hs help set name
 *  @param helpTopic: the "home" topic of the set
 *  @param  setRootFrameHelp:  if true we set this help set as the main help
 */
 public boolean setHelpSystem(String helpSetName) {
        
    boolean helpSetInited = true;
  	try {
	    ClassLoader cl = CSHObject.class.getClassLoader();
            URL url = HelpSet.findHelpSet(cl, helpSetName);
                //System.out.println("finding help set "+helpSetName+" url = "+url.toString());
	    theHS = new HelpSet(cl, url);
            
	} catch (Exception ee) {
            helpSetInited = false;
            String helpSetNotFoundMsg = "Help Set "+helpSetName+" not found. To use help, install the help .jar files from the PART2 download,  in the 'lib' folder";
	    System.out.println (helpSetNotFoundMsg);
                return false;
	} catch (ExceptionInInitializerError ex) {
	    System.err.println("initialization error:");
	    ex.getException().printStackTrace();
	}
	theHB = theHS.createHelpBroker();
       
return helpSetInited;
    }

 public void setConsoleHelp(String helpTopic)  {
 	JTextArea  consoleArea = GlobalValues.scalalabMainFrame.scalalabConsole;
        theHB.enableHelpKey(consoleArea, helpTopic, theHS,  "javax.help.SecondaryWindow", null);
 }
 
 
public void setHelpForComponents() {
            JRootPane rootPane = GlobalValues.scalalabMainFrame.mainJMenuBar.getRootPane();
            CSH.setHelpSet(rootPane,  theHS);
            //CSH.setHelpIDString(histRootPane, );
            theHB.enableHelpKey(rootPane, "browse.strings", theHS, "javax.help.SecondaryWindow", null);
            	

}

 /** displays the helpSet associated with this CSHObject */
   public void displayTheHelp() {
       if (theHB != null) {
           theHB.setFont(GlobalValues.guifont);
           theHB.setDisplayed(true);
           }
   }
   
   /** enable the display of this help set with the help key (i.e. F1) */
   public void enableHelpKey(String helpTopic) {
      JTextArea  consoleArea = GlobalValues.scalalabMainFrame.scalalabConsole;
       if (theHB != null)
           theHB.enableHelpKey(consoleArea, helpTopic, theHS, "javax.help.SecondaryWindow", null);
   }
   
    
}
