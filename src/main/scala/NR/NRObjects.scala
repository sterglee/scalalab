
package NR


 
 // a class that implements the com.nr.UniVarRealValueFun interface using
 // a more convenient Scala function
 class UniValRealValueFun(f: Double=>Double)  extends AnyRef with com.nr.UniVarRealValueFun {
     def funk(x: Double) = f(x)  // call the supplied Scala function
     }
 
 // a class that implements the com.nr.RealMultiValueFun interface using
 // a more convenient Scala function
 class RealMultiValueFun(f: Array[Double] => Array[Double]) extends AnyRef with com.nr.RealMultiValueFun {
     def funk(x: Array[Double]) = f(x)  // call the supplied Scala function
     }
     
  // a class that implements the com.nr.RealValueFunWithDiff interface using
  // a more convenient Scala function
 class RealValueFunWithDiff( f: Array[Double]=>Double, derivf: ((Array[Double], Array[Double]) => Unit)) extends AnyRef with com.nr.RealValueFunWithDiff {
    def funk(x: Array[Double]) = f(x) 
    def df(x: Array[Double], y: Array[Double]) = derivf(x, y)
     }
     
  // a class that implements the com.nr.UniValRealValueFunWithDiff interface using
  // a more convenient Scala function
 class UniValRealValueFunWithDiff(f: Double=>Double, difff: Double => Double) extends AnyRef with com.nr.UniVarRealValueFun {
   def funk(x: Double ) = f(x) 
   def df(x: Double) = difff(x)
     }
     
  // a class that implements the com.nr.UniVarRealMultiValueFun interface using
  // a more convenient Scala function
 class UniVarRealMultiValueFun(f: Double => Array[Double]) extends AnyRef with com.nr.UniVarRealMultiValueFun {
     def funk(x: Double) = f(x)
   }
      
        
     
  // allows a more convenient use of the com.nr.min.Bracketmethod.bracket() routine       
object Bracketmethod {
    import com.nr.UniVarRealValueFun
    
  //  Given a function f: Double => Double, and given distinct initial points ax and
  //  bx, this routine searches in the downhill direction (defined by the
  // function as evaluated at the initial points) and returns new points ax, bx,
  // cx that bracket a minimum of the function. Also returned are the function
  // values at the three points, fa, fb, and fc.
 
    def bracket(a: Double, b: Double, f: Double => Double)=  {
         // construct a NR "bracket" object 
        val  brObj = new com.nr.min.Bracketmethod()
         // the UniValRealValueFun is a Scala class that implements the
         // UniVarRealValueFun interface using a supplied Scala function
        val  sfuncObj = new UniValRealValueFun(f) 
        // call the NR "bracket" method
        brObj.bracket(a, b, sfuncObj)
        
      //  returns new points ax, bx, cx that bracket a minimum of the function. 
      // Also returned are the function values at the three points, fa, fb, and fc.
      (brObj.ax, brObj.bx, brObj.cx, brObj.fa, brObj.fb, brObj.fc)
        }
      
    }
    
    /* Example: 
    def f(x: Double) = -x+1.2*x*x
    fplot(f,  -10, 10)
    var (a, b, c, fa, fb, fc) = Bracketmethod.bracket(-10, 10, f)
    
    */
    
    