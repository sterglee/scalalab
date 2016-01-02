# Introduction #

`ScalaSci source code is open source. Therefore, it is easy to extend ScalaSci classes in applications. We provide some examples. `


# Extending the Vector class #

`We illustrate how we can extend the scalaSci.Vec class and to provide for the new VecExt class, a map2 method that applies two times the given function to the vector elements. `

```

class VecExt(n: Int) extends scalaSci.Vec(n)  {
    
// an auxiliary constructor that copies the contents of 
// the parameter v to the newly created VecExt
    def this(v: scalaSci.Vec) {
        this(v.length)
        for (k<-0 until v.length)
         this(k) = v(k)
        }
        
        
  // apply the function f to all the elements of the VecExt and return the results with a new Vec
 def  map2( f: (Double => Double)): Vec = {
   var len = this.length
   var vres = new Vec(len)
   
    for (r<-0 until len)
       vres(r) = f(f(this(r)) )
   
   vres
 }
 
 }
 
 // create a scalaSci.Vec  
 var v = sin(0.12*inc(0, 0.01, 10))
 
 // create an extended Vector
 var mv = new VecExt(v)
 
  // define a function 
 def  mf(x: Double) = { x*sin(8.9*x) }
 
// map it two times to our extended Vec class, using map2
 var m2v = mv map2 mf
 
// map the function two times again, but this time by using the function map
 var mv1mv1 = mv map mf map mf
 
// plot the results and the difference, to verify that map2 performs a map two times
 figure(1)
 subplot(3,1,1); plot(m2v, "result of map2")
 subplot(3,1,2); plot(mv1mv1, "result of map(map())")
 subplot(3,1,3); plot(m2v-mv1mv1, "difference")
 

```