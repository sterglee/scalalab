
 // RichDouble1D is used for:
 //  1.   implicitly converted to that type when certain operations cannot be performed 
 //  2.   providing a lot of static operations for Array[Double] 

package scalaSci

import scalaSci.math.LinearAlgebra.LinearAlgebra
import scalaSci.math.array.DoubleArray



class RichDouble1DArray(n: Int) extends scalaSci.Vec(n)  {
final def this(a: Array[Double]) =  {
    this(a.length)
    data = a
    }
    
  // compares for value equality
   override def equals(other: Any) = other match { 
     case that: Array[Double] =>
       var thatd = new RichDouble1DArray(that)
       equals(thatd)
     case that: RichDouble1DArray => 
         var smDiffs = 0.0
         var k = 0
         var diff = 0.0
         var otherRD = other.asInstanceOf[RichDouble1DArray]
         while (k < n) {
           diff = data(k)-otherRD(k)
           smDiffs += diff*diff
           k += 1
         }
         if (smDiffs > 0.0) false
         else true
         case that: Vec => 
         var smDiffs = 0.0
         var k = 0
         var diff = 0.0
         var otherRD = other.asInstanceOf[Vec]
         while (k < n) {
           diff = data(k)-otherRD(k)
           smDiffs += diff*diff
           k += 1
         }
         if (smDiffs > 0.0) false
         else true
     
         
      case _ => false 

 }
 
 }


// for "shortcut " constructor
object D1D {
  
  /* e.g.
var xx = 3.4
var a =D1D(  3.4, 5.6, -6.7, -xx,   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/

 final def apply(values: Double*) = {
      values.toArray
  }
}


// for "shortcut " constructor
object RD1D {
  
  /* e.g.
var xx = 3.4
var a = RD1D(    3.4, 5.6, -6.7, -xx,   -6.1,  2.4, -0.5, cos(0.45*xx)) 
*/

 final def apply(values: Double*)= {
      new RichDouble1DArray(values.toArray)
  }
  
}

object  RichDouble1DArray   {
  final def apply(values: Double*)= {
      new RichDouble1DArray(values.toArray)
  }
   

}


