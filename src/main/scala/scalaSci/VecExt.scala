package scalaSci

class VecExt(n: Int) extends scalaSci.Vec(n)  {
    
    final def this(v: scalaSci.Vec) {
        this(v.length)
        for (k<-0 until v.length)
         this(k) = v(k)
        }
        
        
  // apply the function f to all the elements of the Mat and return the results with a new Mat
 final def  map2( f: (Double => Double)): Vec = {
   var len = this.length
   var vres = new Vec(len)
   
    for (r<-0 until len)
       vres(r) = f(f(this(r)) )
   
   vres
 }
 
 }
 
 /*
 var v = sin(0.12*inc(0, 0.01, 10))
 
 var mv = new VecExt(v)
 
 plot(mv)
 
 final def  mf(x: Double) = { x*sin(8.9*x) }
 
 var m2v = mv map2 mf
 
 var mv1mv1 = mv map mf map mf
 
 figure(1)
 subplot(3,1,1); plot(m2v, "result of map2")
 subplot(3,1,2); plot(mv1mv1, "result of map(map())")
 subplot(3,1,3); plot(m2v-mv1mv1, "difference")
 */