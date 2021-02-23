package scalaSci.MatlabRange

// startv::incv::endv is evaluated as (endv.::(incv)).::(startv)

// class MatlabRangeStart analyzes endv.::(incv)
class MatlabRangeStart(v: Double) {
        
   var endv = v  // the ended value of the range
   var inc=0.0
    
    def ::(incp: Double) = {
        inc = incp  // keep increment with the inc  field  variable
        new MatlabRangeNext(this)
        }
  }
  
class MatlabRangeNext(v: MatlabRangeStart) {
  
     var mStart = v
    
    def :: (startv: Double) = {
       scalaSci.StaticMaths.inc(startv, mStart.inc, mStart.endv)  // e.g. a :: b, applies as b.::(a) so it is necessary to restore the correct order
        }
        
}

        
     /* e.g.
     
 closeAll
 figure(1); 
subplot(2,1,1); plot(sin(7.8*0::0.01::8))   

 subplot(2,1,2); 
var low = 0
var inc = 0.01
var up=90
plot(100+low::inc::up, cos(100+low::inc::up))
*/
        
