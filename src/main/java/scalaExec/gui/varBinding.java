package scalaExec.gui;

import scalaExec.Interpreter.GlobalValues;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the variable bindings of a script which can be altered
 * from outside the script object or created outside of a script and passed
 * into it.
 */
public class varBinding  {
    private Map variables;

    public varBinding() {
    }

    public varBinding(Map variables) {
        this.variables = variables;
    }

    
    /**
     * @param name the name of the variable to lookup
     * @return the variable value
     */
    public Object getVariable(String name) {
        if (variables == null)
            return null;
        Object result = variables.get(name);
         //Class tmp = result.getClass();        tmp.get
        if (result == null && !variables.containsKey(name)) {  // variable not yet defined 
            return null;
        }
        return result;
    }

    /**
     * Sets the value of the given variable
     *
     * @param name  the name of the variable to set
     * @param value the new value for the given variable
     */
    public void setVariable(String name, Object value) {
        if (variables == null)
            variables = new LinkedHashMap();
        variables.put(name, value);
    }

    public Map getVariables() {
        if (variables == null)
            variables = new LinkedHashMap();
        return variables;
    }

}
