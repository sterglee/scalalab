
package scalaExec.gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;

/**
 * This is a test class used to demonstrate
 * a simple usage of the TextConsole component.
 */
public class TextConsoleTest {
	
	
	/**
	 * Test method.
	 */
	public static void main(String[] args) {
		
		//define a frame and add a console to it
		JFrame frame = new JFrame("TextConsole test");
		TextConsole console = new TextConsole(60,20, 20, "Courier New");
		frame.getContentPane().add(console);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		//write the form to the screen
		console.gotoPosition(1,2);
		console.write("  Please provide your first name, last name, age and \n  profession. Leave out no information.", 
				Color.YELLOW);
		console.gotoPosition(4,4);
		console.write("**********************************************",Color.BLUE);
		console.write("First name  : ",5,5, Color.YELLOW);
		console.addFormField("first name",30, Color.RED);			
		console.write("Last name   : ",5,6,Color.YELLOW);
		console.addFormField("last name",30, Color.RED);
		console.write("Age         : ",5,7, Color.YELLOW);
		console.addFormField("age",3, Color.RED);
		console.write("Profession  : ",5,8, Color.YELLOW);
		console.addFormField("profession",30, Color.RED);
		console.gotoPosition(4,10);
		console.write("**********************************************",Color.BLUE);
		console.gotoFirstField();
		
		//wait for the user to press <enter> and 
		//receive user input in a map.
		Map values = console.getValues();		
		for (Iterator iter = values.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			//@TODO do something with the map		
		}
		
		//clear the console
		console.clear();
		
		
	}
}
