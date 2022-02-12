package scalainterpreter;

import scala.Option;
import scalaExec.Interpreter.GlobalValues;

public class displayWatchedVars {

    public static void watchVar(String varName)  {
        Option<Object>  valOfVar = GlobalValues.globalInterpreter.valueOfTerm(varName);
        System.out.println("watching var = "+varName);
        javax.swing.JOptionPane.showMessageDialog(null, valOfVar);
    }
}
