

package scalaSci.math.plot

/* use more flexible syntax, the following variants are valid:
 hold on;  
 hold off; 
 hold(on) 
 hold(off)
 hold("on")
 hold("off")
 
 Also for close(), the following variants are valid:
 close all;   // closes all figures
 close(2)   // closes figure with identifier 2
 close("all")   // closes all figures
 closeAll  // closes all figures
 */
object SyntaxHelper {
    object on
    object off
   def hold(mode: on.type) =  scalaSci.math.plot.plot.hold("on")
   def hold(mode: off.type) =  scalaSci.math.plot.plot.hold("off")
   def hold (s: String) =    scalaSci.math.plot.plot.hold(s)
   object hold {
    def on  {   scalaSci.math.plot.plot.hold("on") }
    def off  { scalaSci.math.plot.plot.hold("off") }
  }
  
  object all
  def close(whichFig: all.type) = scalaSci.math.plot.plot.close("all")
  def close(str: String) = if (str == "all") scalaSci.math.plot.plot.close("all")
  def close(figId: Int) = scalaSci.math.plot.plot.close(figId)
  object close {
    def all  { scalaSci.math.plot.plot.close("all")  }
     }
    
}
