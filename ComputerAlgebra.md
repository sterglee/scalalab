# `Introduction` #

`Although ScalaLab is not a Computer Algebra System, we integrate within the embedded libraries, the` **`symja  (http://code.google.com/p/symja/ )`** `Java Computer Algebra system.`

`A top level menu choice, `**`"Symbolic Algebra"`** `aims to provide symbolic Algebra operations. This choice opens a GUI Window that allows the user to perform many Computer Algebra operations, e.g. to simplify expressions, to multiply/divide polynomials, factorizations, differentiations, integrations etc. Additionally, the user can easily plot 2-D and 3-D functions. Although, the operation of this window is currently independent from the rest ScalaLab components, we consider it very useful, since it is a way for the ScalaLab user to perform easily many Computer Algebra tasks`.

`In this page we present material that will facilitate the user to explore the potential of symja from within ScalaLab.`

# `Using  the symja Java Computer Algebra System from within ScalaLab` #

`ScalaLab permits to perform Computer Algebra tasks with symja more conveniently and effectively than from the plain Java. For example we can run the following Scala code that uses symja to perform Computer Algebra tasks.`

## `Examples using the sym() ScalaLab command ` ##

```

sym("Integrate[x^(-1),x]")

sym("Integrate[x^a,x]");

sym("Integrate[x^10,x]")  // "1/11*x^11"
sym("Simplify[1/2*(2*x+2) ]")
sym("Simplify[1/2*(2*x+2)*(1/2)^(1/2)]")
sym("Simplify[Integrate[(8*x+1)/(x^2+x+1)^2,x]]")

sym("Apart[1/(x^3+1)]") 
sym("Integrate[1/(x^5+x-7),x]") //, "Integrate[(x^5+x-7)^(-1),x]");
sym("Integrate[1/(x^5-7),x]")  //, "Integrate[(x^5-7)^(-1),x]");
sym("Integrate[1/(x-2),x]") //, "Log[x-2]");
sym("Integrate[(x-2)^(-2),x]") //, "(-1)*(x-2)^(-1)");
sym("Integrate[(x-2)^(-3),x]") //, "(-1/2)*(x-2)^(-2)");
sym("Integrate[(x^2+2*x+3)^(-1),x]") // "ArcTan[1/2*(2*x+2)*(1/2)^(1/2)]*(1/2)^(1/2)")
sym("Integrate[1/(x^2+1),x]") //, "ArcTan[x]");
sym("Integrate[(2*x+5)/(x^2-2*x+5),x]") // "7/2*ArcTan[1/4*(2*x-2)]+Log[x^2-2*x+5]");
sym("Integrate[(8*x+1)/(x^2+2*x+1),x]") // "7*(x+1)^(-1)+8*Log[x+1]");

sym("Integrate[1/(x^3+1),x]") // "ArcTan[(2*x-1)*3^(-1/2)]*3^(-1/2)-1/6*Log[x^2-x+1]+1/3*Log[x+1]");
sym("Simplify[Integrate[1/3*(2-x)*(x^2-x+1)^(-1),x]]") // "ArcTan[(2*x-1)*3^(-1/2)]*3^(-1/2)-1/6*Log[x^2-x+1]");
sym("Integrate[1/3*(2-x)*(x^2-x+1)^(-1)+1/3*(x+1)^(-1),x]") // "ArcTan[(2*x-1)*3^(-1/2)]*3^(-1/2)-1/6*Log[x^2-x+1]+1/3*Log[x+1]");
sym("Integrate[E^x*(2-x^2),x]") // "-E^x*x^2+2*E^x*x");
sym("Integrate[(x^2+1)Log[x],x]") // "1/3*Log[x]*x^3-1/9*x^3+x*Log[x]-x");
sym("Integrate[x*Log[x],x]") // "1/2*Log[x]*x^2-1/4*x^2");

sym("Apart[2*x^2/(x^3+1)]")  //, "(4/3*x-2/3)*(x^2-x+1)^(-1)+2/3*(x+1)^(-1)");

sym("Integrate[2*x^2/(x^3+1),x]") // "2/3*Log[x^2-x+1]+2/3*Log[x+1]");
		// check("Integrate[Sin[x]^3,x]", "-1/3*Cos[x]*Sin[x]^2-2/3*Cos[x]");
sym("Integrate[Sin[x]^3,x]") // "1/3*Cos[x]^3-Cos[x]");
		// check("Integrate[Cos[2x]^3,x]", "1/6*Cos[2*x]^2*Sin[2*x]+1/3*Sin[2*x]");
sym("Integrate[Cos[2x]^3,x]") // "1/2*Sin[2*x]-1/6*Sin[2*x]^3");
sym("Integrate[x,x]") // "1/2*x^2");
sym("Integrate[2x,x]") // "x^2");
sym("Integrate[h[x],x]") // "Integrate[h[x],x]");
sym("Integrate[f[x]+g[x]+h[x],x]")// "Integrate[h[x],x]+Integrate[g[x],x]+Integrate[f[x],x]");
sym("Integrate[Sin[x],x]") // "(-1)*Cos[x]");
sym("Integrate[Sin[10*x],x]") // "(-1/10)*Cos[10*x]");
sym("Integrate[Sin[Pi+10*x],x]") // "(-1/10)*Cos[10*x+Pi]");

```
## `Examples demonstrating basic Symbolic Algebra tasks` ##

`The following Scala code uses symja to perform basic Computer Algebra tasks. You can run it, as any other ScalaLab script.`

```



import org.matheclipse.core.eval.EvalUtilities
import org.matheclipse.core.expression.F
import org.matheclipse.core.interfaces.IExpr
import org.matheclipse.core.form.output.OutputFormFactory
import org.matheclipse.core.form.output.StringBufferWriter


    // Static initialization of the MathEclipse engine instead of null 
    // you can set a file name to overload the default initial
    // rules. This step should be called only once at program setup:
    F.initSymbols()
    var  util = new EvalUtilities()

      var  buf = new StringBufferWriter()
      var  input = "Expand[(AX^2+BX)^2]"
  // evaluate the expression. The result corresponds to the internal 
  // AST representation (not much human readable expression)     
      var result = util.evaluate(input)

  // convert the result in a human redable expression
      OutputFormFactory.get().convert(buf, result)
      var  output = buf.toString()
      println("Expanded form for " + input + " is " + output)

      // set some variable values. These values are substituted properly
      // by the evaluation engine
      input = "A=2;B=4"
      result = util.evaluate(input)

      buf = new StringBufferWriter()
      input = "Expand[(A*X^2+B*X)^2]"
      result = util.evaluate(input)
      OutputFormFactory.get().convert(buf, result)
      output = buf.toString()
      println("Expanded form for " + input + " is " + output)
      
      // now prepare the expression for factoring
      input = "Factor["+output+"]"
      result = util.evaluate(input)
      buf = new StringBufferWriter()
      OutputFormFactory.get().convert(buf, result)
      output = buf.toString()
      
      // print the factored form of the expression
      println("Factored form for " + input + " is " + output)
     
    
```

`And another example using symja from ScalaLab.`

```

import org.matheclipse.core.eval.EvalUtilities
import org.matheclipse.core.expression.F
import org.matheclipse.core.interfaces.IExpr
import org.matheclipse.core.form.output.OutputFormFactory
import org.matheclipse.core.form.output.StringBufferWriter
import edu.jas.arith.BigRational
import edu.jas.poly.GenPolynomialRing
import edu.jas.poly.TermOrder
import edu.jas.integrate.ElementaryIntegration


var br = new BigRational(0)
var vars = Array[String]("x")
var fac =  new GenPolynomialRing[BigRational](br, vars.length, new TermOrder(TermOrder.INVLEX), vars)

var  eIntegrator = new ElementaryIntegration[BigRational](br)

var a = fac.parse("x^7 - 24 x^4 - 4 x^2 + 8 x - 8")
println("A: " + a.toString())
var d = fac.parse("x^8 + 6 x^6 + 12 x^4 + 8 x^2")
println("D: " + d.toString())
  // compute greatest common divisor
var gcd = a.gcd(d)


var ret = eIntegrator.integrateHermite(a, d)
println("Result: " + ret(0) + " , " + ret(1))

println("-----")


 a = fac.parse("10 x^2 - 63 x + 29")
 println("A: " + a.toString())
 
 d = fac.parse("x^3 - 11 x^2 + 40 x -48")
 println("D: " + d.toString())
 
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("-----")

 a = fac.parse("x+3")
 println("A: " + a.toString());
 d = fac.parse("x^2 - 3 x - 40")
 println("D: " + d.toString())
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("-----")

 a = fac.parse("10 x^2+12 x + 20")
 println("A: " + a.toString())
 d = fac.parse("x^3 - 8")
 println("D: " + d.toString())
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("------------------------------------------------------\n")
  
```

### Evaluate an expression in complex mode ###

```
import org.matheclipse.parser.client._
import org.matheclipse.parser.client.eval.api.ObjectEvaluator._

var p = new Parser

var obj = p.parse("x^2+3*x*I")


import org.matheclipse.parser.client.eval._

var vc =  new ComplexVariable(3.0)

var engine = new ComplexEvaluator()

engine.defineVariable("x", vc)

var c = engine.evaluateNode(obj)

var result = ComplexEvaluator.toString(c)

vc.setValue(4)

c = engine.evaluateNode(obj)

result = ComplexEvaluator.toString(c)

```

## Polynomial Rings ##
```


import edu.jas.arith._  
import edu.jas.poly._  
import edu.jas.integrate._  

import edu.jas.arith.BigRational
import edu.jas.poly.GenPolynomialRing
import edu.jas.poly.TermOrder
import edu.jas.integrate.ElementaryIntegration


var br = new BigRational(0)
var vars = Array[String]("x")
var fac =  new GenPolynomialRing[BigRational](br, vars.length, new TermOrder(TermOrder.INVLEX), vars)

var  eIntegrator = new ElementaryIntegration[BigRational](br)

var a = fac.parse("x^7 - 24 x^4 - 4 x^2 + 8 x - 8")
println("A: " + a.toString())
var d = fac.parse("x^8 + 6 x^6 + 12 x^4 + 8 x^2")
println("D: " + d.toString())
var gcd = a.gcd(d)
gcd = a.gcd(d)

var ret = eIntegrator.integrateHermite(a, d)
println("Result: " + ret(0) + " , " + ret(1))

println("-----")


 a = fac.parse("10 x^2 - 63 x + 29")
 println("A: " + a.toString())
 
 d = fac.parse("x^3 - 11 x^2 + 40 x -48")
 println("D: " + d.toString())
 
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("-----")

 a = fac.parse("x+3")
 println("A: " + a.toString());
 d = fac.parse("x^2 - 3 x - 40")
 println("D: " + d.toString())
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("-----")

 a = fac.parse("10 x^2+12 x + 20")
 println("A: " + a.toString())
 d = fac.parse("x^3 - 8")
 println("D: " + d.toString())
 gcd = a.gcd(d)
 println("GCD: " + gcd.toString())
 ret = eIntegrator.integrateHermite(a, d)
 println("Result: " + ret(0) + " , " + ret(1))

 println("------------------------------------------------------\n")

```


## `Evaluating an expression` ##

```



 
 class symja {
import org.matheclipse.core.eval.EvalUtilities
import org.matheclipse.core.expression.F
import org.matheclipse.core.form.output.OutputFormFactory
import org.matheclipse.core.form.output.StringBufferWriter
import org.matheclipse.core.interfaces.IExpr

     
  // static initialization of the MathEclipse engine    
     F.initSymbols(null)
     
     var util = new EvalUtilities()
     
     def eval(input: String) = {
         var buf = new StringBufferWriter
         var result = util.evaluate(input)
         OutputFormFactory.get().convert(buf, result)
         buf.toString
         }
         }
         
         
         
         var se = new symja
         
         var rr = se.eval("Expand[(AX^2+BX)^2]")
         
          
```