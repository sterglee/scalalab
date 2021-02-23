package scalaExec.scalaLab;

import java.util.Vector;
  // a class used as a structure to keep the association between the toolbox name (i.e. the pathname of the .jar file of the toolbox)
  // and a Vector that is filled with the bytecodes of the toolbox classes
public class scalaSciToolbox {
            public String    toolboxName;    // the full pathname of the .jar file of the toolbox
            public Vector   toolboxClasses = new Vector();   // is filled with the bytecodes of the toolbox classes
        }
   

