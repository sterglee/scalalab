# Introduction #

`The Java symbolic algebra system symja is included with the ScalaLab libraries. Thefore we can evaluate symbolic expressions using Java code. We present some examples that can be executed from the ScalaLab editor with F9. These examples have been adapted from the symja page: http://code.google.com/p/symja/`


# Examples #

## Direct Java Integration ##
```

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.form.output.StringBufferWriter;
import org.matheclipse.core.interfaces.IExpr;

import edu.jas.kern.ComputerThreads;

public class EvalExpand {
  public static void main(String[] args) {
    // Static initialization of the MathEclipse engine instead of null 
    // you can set a file name to overload the default initial
    // rules. This step should be called only once at program setup:
    F.initSymbols(null);
    EvalUtilities util = new EvalUtilities();

    IExpr result;

    try {
      StringBufferWriter buf = new StringBufferWriter();
      String input = "Expand[(AX^2+BX)^2]";
      result = util.evaluate(input);
      OutputFormFactory.get().convert(buf, result);
      String output = buf.toString();
      System.out.println("Expanded form for " + input + " is " + output);

      // set some variable values
      input = "A=2;B=4";
      result = util.evaluate(input);

      buf = new StringBufferWriter();
      input = "Expand[(A*X^2+B*X)^2]";
      result = util.evaluate(input);
      OutputFormFactory.get().convert(buf, result);
      output = buf.toString();
      System.out.println("Expanded form for " + input + " is " + output);
      
      buf = new StringBufferWriter();
      input = "Factor["+output+"]";
      result = util.evaluate(input);
      OutputFormFactory.get().convert(buf, result);
      output = buf.toString();
      System.out.println("Factored form for " + input + " is " + output);
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      // Call terminate() only one time at the end of the program  
      ComputerThreads.terminate();
    }

  }
}


```