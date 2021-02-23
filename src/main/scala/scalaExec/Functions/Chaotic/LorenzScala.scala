
package scalaExec.Functions.Chaotic;
import  numal._

 class LorenzScala extends Object with  AP_rke_methods 
 { 
def  der(n: Int, t: Double, y: Array[Double]): Unit = 
{ 
   var  xx=y(1);   var  yy=y(2);    var  zz=y(3); 

    y(1) = 10*(yy-xx);  
    y(2) = -xx*zz+143*xx - yy;  
    y(3) = xx*yy - 2.66667*zz;  
  } 
 

def  out(n: Int, t: Array[Double], te: Array[Double],  y: Array[Double],  data: Array[Double]): Unit=  {  } 
}
